## 5 테스트 코드 실패 해결하고 코드 수정하기
### 01 단계
- 지금까지 작성한 코드를 기준으로 테스트 코드를 작성해 테스트를 해보도록 하자.
- 디렉토리를 우클릭하고 [Run 'Tests in 'springBoot-...'] 를 누르면 테스트가 실행된다.
- ![우클릭 테스트.png](..%2F..%2F..%2FDesktop%2F%EC%9A%B0%ED%81%B4%EB%A6%AD%20%ED%85%8C%EC%8A%A4%ED%8A%B8.png)

### 02 단계
- 앗! BlogApiControllerTest의 테스트가 실패했다.
- 10장 'OAuth2로 로그인/로그아웃 구현하기' 를 진행하며 추가한 인증 관련 로직 때문에 그렇다.
- 여기도 성공하도록 코드를 수정한다.
- ![일부실패.png](..%2F..%2F..%2FDesktop%2F%EC%9D%BC%EB%B6%80%EC%8B%A4%ED%8C%A8.png)

### 03 단계
- BlogApiControllerTest.java 파일을 열어 코드를 수정한다.
```java
package com.springboot.blog.controller;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }


    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }


    @DisplayName("addArticle: 아티클 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticles: 아티클 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception {
        // given
        final String url = "/api/articles";
        Article savedArticle = createDefaultArticle();

        // when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));
    }

    @DisplayName("findArticle: 아티클 단건 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        // when
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()));
    }


    @DisplayName("deleteArticle: 아티클 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        // when
        mockMvc.perform(delete(url,savedArticle.getId()))
                .andExpect(status().isOk());

        // then
        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }


    @DisplayName("updateArticle: 아티클 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        final String newTitle = "new title";
        final String newContent = "new content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        // when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

    private Article createDefaultArticle() {
        return blogRepository.save(Article.builder()
                .title("title")
                .author(user.getUsername())
                .content("content")
                .build());
    }
}

```
1. 인증 객체를 저장하는 시큐리티 콘텍스트에 setAuthentication() 메서드를 사용해 테스트 유저를 지정한다.
2. 글을 생성하는 API에서는  파라미터로 Principal 객체를 받고 있는데 이 객체에 테스트 유저가 등러가도록 모킹한다. 이 테스트 코드에서는 Principal 객체를 모킹해서 스프링 부트 애플리케이션에서 getName() 메서드를 호출하면 "userName" 값을 반환 한다.
3. 중복 코드를 제거하기 위해 글을 만드는 로직을 createDefaultArticle() 메서드로 추출한다.
4. 코드를 수정한 뒤 다시 테스트를 해보면 모두 성공한다.
5. ![모두성공.png](..%2F..%2F..%2FDesktop%2F%EB%AA%A8%EB%91%90%EC%84%B1%EA%B3%B5.png)