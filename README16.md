## 값 검증 가이드
> 값 검증(validation)은 사용자가 요청을 보냈을 때 올바른 값이닞 유효성 검사를 하는 과정이다.
> 예를 등러 서버에서 로직을 처리하기 전에 사용자가 잘못된 데이터를 보냈다고 해본다. 이럴 때는 서버에서 로직을 
> 처리하기 전에 사용자에게 `입력한 데이터가 올바르지 않다`라는 에러 메시지를 보여주면 된다. 이렇게 하면 서버에서 서비스 로직을 실행하지 않으니 조금 더 시스템을 안정적으로 관리할 수 있다.
> 여러분이 이 책을 통해 지금까지 작성한 코드를 바탕으로 값 검증에 대해 이야기해본다. AddArticleRequest.java 파일을 열어준다.

```java
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {

    private String title;
    private String content;
    private String author;
    //toEntity는 빌더 패턴을 사용해 DTO로 만들어 주는 메서드
    public Article toEntity(String userName) { //생성자를 사용해 객체 생성
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
```
> 이 코드는 블로그 글 추가 요청을 받기 위한 DTO 이다. 코드에서 보듯 이 DTO는 String형 title, content를 가지고 있다. 그런데 만약 누군가 이러헤구 요청을 보내면 어떻게 처리르 해야 할까?
```json 
content가 없는 블로그 글 추가 요청
{
    "tile": "제목"
}
```
>이런 경우 title에는  "제목"이, content에는 null이 들어올 것이다. 그런데 Article 엔티티에서는 content의 속성이 nullable = false로 정의되어 있다. 그러면 null을 저장하는 순간 예외가 발생하며 서버에 문제가 생길 것이다.
>이런 경우 서버단이 아니라 요청단에서 값 검증을 하여 처음부터 content값이 없으면 사용자에게 알려주는 등의 방법을 사용하면 이런 상황을 예방할 수 있다. 그 방법을 알아보도록 하자.

>스프링에서는 자바 빈 밸리데이션 java bean validation이라는 API를 제공한다. 이 API를 사용하면 애너테이션 기반으로 다양한 검증 규칙을 간편하게 사용할 수  있고 입력 데이터의 유효성을 검사할 수 있다.
> 자주 사용하는 몇가지 애너테이션을 살펴본다. 애너테이션 이름을 보면 알겠지만 이름에 `검증 규칙 목적` 이 명확하게 보이므로 직관적으로 이해할 수 있을 것이다.
> 자주 사용하는 자바 빈 밸리데이션

```java
/* 문자열을 다룰 때 사용 */
@NotNull // null, 허용하지 않음
@NotEmpty // null, 빈 문자열(공백) 또는 공백만으로 채워진 문자열 허용하지 않음
@NotBlank // null,  빈 문자열(공백) 허용하지 않음
@Size(min=? , max=?) // 최소 길이 , 최대 길이 제한
@Null // null만 가능

/* 숫자를 다룰 때 사용 */
@Positive // 양수만 가능
@PositiveOrZero // 양수와 0만 허용
@Negative // 음수만 허용
@NegativeOrZero // 음수와 0만 허용
@Min(?) // 최솟값 제한
@Max(?) // 최댓값 wpgks

/* 정규식 사용 */
@Email // 이메일 형식만 사용
@Pattern(regexp="?") // 직접 작성한 정규식에 맞는 문자열만 사용
```
>값 검증은 어느 계층에서 해도 상관은 없다. 프리제테이션 계층에서 컨트롤러에 요청이 오는 순간 검증할 수도 있고, 퍼시스턴스 계층에서 엔티티에 적용할 수도 있다. 부록에서는 프레젠테이션계층에서 검증하는 과정을 소개한다.
> 보통은 프레젠테이션 계층에 검증 코드를 작성해야 불필요한 서비스 로직을 실행하지 않을 수 있고, 또, 사용자요청마다 세부 조건을 적용할 수가 있기 때문이다. 그럼 실습을 통해서 실제로 값 검증을 하는 방법을 알아 보도록 하자.

### 01 단계
- build.gradle 파일을 열어 의존성을 추가해준다.
```json
build.gradle
dependendies {
  ... 생략 ...
  testImplementation 'com.github.javafaker:javafaker:1.0.2'
}
```

- Faker는 테스트를 진행할 때 가짜 데이터를 생성해주는 오픈소스 라이브러리이다. 쉽게 말해 이 라이브러리를 사용하면 이름, 주소, 이메일 같은 가짜 정보를 쉽게 생성할 수 있다.
- 예를 들어 다음과 같이 가짜 정보를 생성할 수 있다.
```java
Faker faker = new Faker(new Locale("ko")); // Local을 넣지 않으면 영어로 생성

String name = faker.address().fullAddress(); // 85877 구로읍, 부천구, 부산
String firstName = faker.name.name() //  홍 길동
String lastName = faker.name.name() //  Melon

```

- 글을 생성할 때는 이런 유효성이 있어야 한다고 생각한다. 이 유효성을 바탕으로 코드를 작성해 본다.
- 
|       | 제목                              | 내용                           |
|:------|:--------------------------------|:-----------------------------|
| 유효성 1 | Null값은 허용하지 않음                  | Null값은 허용하지 않음               |
| 유효성 2 | 1자 이상 10자 이하                    |                              |

### 02 단계 
- 검증 로직을 작성하기 전에 검증 로직을 테스트할 테스트 코드부터 작성한다.
- BlogApiControllerTest.java 파일을 열어 다음과 같이 코드를 작성한다.
```java
    @DisplayName("addArticle: 아티클 추가할 때 title이 null이면 실패한다.")
    @Test
    public void addArticleNullValidation() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = null;
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
        result.andExpect(status().isBadRequest());

    }

    @DisplayName("addArticle: 아티클 추가할 때 title이 10를 넘으면 실패한다.")
    @Test
    public void addArticleSizeValidation() throws Exception {
        Faker faker = new Faker();
        // given
        final String url = "/api/articles";
        final String title = faker.lorem().characters(11);
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
        result.andExpect(status().isBadRequest());

    }
```
- 값의 유효성을 검증하기 위한 2개의 테스트 케이스의 새로 작성했다. 각 테스트 케이스는 다음과 같은 given-when-then 패턴을 간다.
>addArtitleNullValidatoin()
> 
| given | 블로그 글 추가에 필요한 요청 객체를 만든다. 이때 title에는 null값으로 설정한다.                            |
|:------|:------------------------------------------------------------------------------|
| when  | 블로그 글 추가 API에 요청을 보낸다. 이때 요청 타입은 JSON이며, given절에서 미리 만들어둔 객체를 요청 본문으로 함께 보낸다. | Null값은 허용하지 않음               |
| then  | 응답 코드가 400 Bad Request인지 확인한다.                                                |

>addArtitleSizeValidatoin()
>
| given | 블로그 글 추가에 필요한 요청 객체를 만든다.  이때 title에는 11자의 문자가 들어가게 설정한다.                     |
|:------|:------------------------------------------------------------------------------|
| when  | 블로그 글 추가 API에 요청을 보낸다. 이때 요청 타입은 JSON이며, given절에서 미리 만들어둔 객체를 요청 본문으로 함께 보낸다. | Null값은 허용하지 않음               |
| then  | 응답 코드가 400 Bad Request인지 확인한다.                                                |

- 테스트를 실행하면 이번에 추가한 2개의 테스트는 모두 실패한 것이다. 왜냐하면 아직 유효값 검증 로직을 작성하지 않았기 때문이다.
- ![유효값 검증 로직이 없어 실패.png](test%20case%2F%EC%9C%A0%ED%9A%A8%EA%B0%92%20%EA%B2%80%EC%A6%9D%20%EB%A1%9C%EC%A7%81%EC%9D%B4%20%EC%97%86%EC%96%B4%20%EC%8B%A4%ED%8C%A8.png)

### 03 단계
- 블로그 글 추가 요청을 받을 때 사용하는 DTO인 AddArticleRequest.java 파일을 열어 값 검증 애너테이션을 추가한다.
```java
package com.springboot.blog.dto;

import com.springboot.blog.domain.Article;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파마미터로 받는 생성자 추가
@Getter
public class AddArticleRequest {
    @NotNull
    @Size(min = 1, max = 10)
    private String title;
    @NotNull
    private String content;

    //toEntity는 빌더 패턴을 사용해 DTO로 만들어 주는 메서드
    public Article toEntity(String author) { //생성자를 사용해 객체 생성
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}

```
- title에는 Null을 허용하지 않는 @NulNull과 1자 이상 10자 이하의 조건을 설정하는 @Size 애너테이션을 , content에는 @NotNull만 추가했다.
- 여기서 나는 여기서 @Size가 임폴트가 안되는 문제가 발생했다.

# 왜 jdk 17에는 @Size가 없다고 하지?

JDK 17 자체에는 `@Size` 어노테이션이 포함되어 있지 않습니다. `@Size`는 JDK의 표준 라이브러리가 아니라, Bean Validation(자카르타 Bean Validation, 이전에는 JSR-303/JSR-380) 명세에서 제공하는 어노테이션입니다. 이 어노테이션은 `javax.validation.constraints.Size`(구버전) 또는 `jakarta.validation.constraints.Size`(신버전) 패키지에 속합니다[^1][^2].

JDK 17로 업그레이드되면서, Bean Validation 관련 패키지들이 `javax.validation`에서 `jakarta.validation`으로 변경되었습니다. 따라서, JDK 17 이상에서는 `javax.validation`이 아닌 `jakarta.validation` 패키지의 어노테이션을 사용해야 하며, 관련 라이브러리(예: Hibernate Validator, spring-boot-starter-validation 등)를 별도로 의존성에 추가해야 합니다[^1][^2].

만약 JDK 17 환경에서 `javax.validation.constraints.Size`를 임포트하거나, 관련 라이브러리가 누락된 경우, `@Size` 어노테이션을 찾을 수 없거나 동작하지 않는 현상이 발생할 수 있습니다. 이 때문에 "JDK 17에는 @Size가 없다"고 오해할 수 있습니다. 실제로는 JDK가 아니라, Bean Validation 라이브러리와 패키지 변경이 원인입니다[^1][^2].

**정리**

- `@Size`는 JDK에 포함된 것이 아니라 Bean Validation 라이브러리에 포함된 어노테이션입니다.
- JDK 17 이상에서는 `jakarta.validation.constraints.Size`를 사용해야 하며, 관련 라이브러리를 의존성에 추가해야 합니다.
- `javax.validation` 패키지는 구버전(주로 JDK 8~11)에서 사용되었고, JDK 17 이상에서는 지원되지 않습니다[^1][^2].
- 	implementation 'org.springframework.boot:spring-boot-starter-validation' 라이브러리를 해주지 않아서 였다.
<div style="text-align: center">⁂</div>

[^1]: https://stackoverflow.com/questions/77942639/size-annotation-is-not-working-for-the-validation

[^2]: https://stackoverflow.com/questions/79200048/how-can-i-use-a-common-java-11-library-with-validation-annotations-in-a-java-17

[^3]: https://github.com/OpenAPITools/openapi-generator/issues/11795

[^4]: https://github.com/unitycatalog/unitycatalog/issues/19

[^5]: https://www.baeldung.com/java-validation

[^6]: https://docs.oracle.com/javaee/7/tutorial/bean-validation001.htm

[^7]: https://docs.oracle.com/en/java/javase/17/docs/api/java.compiler/javax/lang/model/AnnotatedConstruct.html

[^8]: https://jakarta.ee/specifications/bean-validation/3.1/jakarta-validation-spec-3.1

[^9]: https://a1010100z.tistory.com/179

[^10]: https://pretius.com/blog/java-17-features/

[^11]: https://luvstudy.tistory.com/152

[^12]: https://hibernate.org/validator/documentation/migration-guide/

[^13]: https://velog.io/@cmsskkk/Java-Bean-Validation-사용하기

[^14]: https://www.baeldung.com/java-validation-list-annotations

[^15]: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/reflect/AnnotatedElement.html

[^16]: https://www.oracle.com/java/technologies/javase/17-relnote-issues.html

[^17]: https://samtao.tistory.com/44

[^18]: https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html

[^19]: https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/javax/swing/SizeSequence.html

[^20]: https://0soo.tistory.com/165

### 04 단계
- 블로그 글 추가 요청을 받는 BlogApiController.java 파일을 열어 코드를 수정해준다.
```java
    // HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
    @PostMapping("/api/articles")
    // @RequestBody로 요청 본문 값 매핑
    public ResponseEntity<Article> addArticle( @RequestBody @Validated AddArticleRequest request , Principal principal) {
        System.out.println(principal.toString());
        Article saveArticle = blogService.save(request, principal.getName());

        // 요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
        // create 반환값 201
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saveArticle);
    }
```

- @Validated 애너테이션을 추가하여 메서드에 들어오는 파라미터가 유효한 값이닞 검증한다.
- 이제 코드 수정이 모두 끝났으니 테스트 코드를 다시 실행해본다. BlogApiControllerTest.java 파일을 다시 실행하면 다음과 같이 모두 테스트가 잘 실행될 것이다.
- ![테스트 성공.png](test%20case%2F%ED%85%8C%EC%8A%A4%ED%8A%B8%20%EC%84%B1%EA%B3%B5.png)
- 이런 방식으로 값 검증을 하면 쉽게 서버의 안정성을 챙길 수 있을 것이다. 꼭 알아두었다가 실무에 잘 활용하도록 하자.