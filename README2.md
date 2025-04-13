## 사전 지식 : 토큰 기반 인증
## 토큰 기반 인증이란 ?
> 사용자가 서버에 접근할 때 이 사용자가 인증된 사용자인지 확인하는 방법은 다양합니다. 
> 대표적인 사용자 인증 확인 방법으로 **서버 기반 인증**과 **토큰 기반 인증**이 있습니다. 
> 스프링 스큐리티에서는 **기본적으로 세션 기반 인증을 제공**해주는데요.
> 토큰은 서버에서 클라이언트를 구분하기 위한 유일한 값인데 서버가 토큰을 생성해서 클라이언트에게 제공하면,
> 클라이언트는 이 토큰을 갖고 있다가 여러 요청을 이 토큰과 함께 신청합니다.
> 
>**그럼 서버는 토큰만 보고 유효한 사용자인지 검증하죠.**

### 토큰을 전달하고 인증 받은 과정 
![토큰을 전달하고 인증 받는 과정.png](..%2F..%2F..%2FDownloads%2F%ED%86%A0%ED%81%B0%EC%9D%84%20%EC%A0%84%EB%8B%AC%ED%95%98%EA%B3%A0%20%EC%9D%B8%EC%A6%9D%20%EB%B0%9B%EB%8A%94%20%EA%B3%BC%EC%A0%95.png)
- 1.클라이언트가 아이디와 비밀번호를 서버에게 전달하면서 인증을 요청하면
- 2.서버는 아이디와 비밀번호를 확인해 유효한 사용자인지 검증하고 유효한 사용자면 토큰을 생성해서 응답한다.
- 3.클라이언트는 서버에서 준 토큰을 저장한다.
- 4.이후 인증이 필요한 API를 사용할 때 토큰을 함께 보낸다.. 
- 5.그러면 서버는 토큰이 유효한지 검증한다.
- 6.토큰이 유효하다면 클라이언트가 요청한 내용을 처리한다.

### 토큰 기반 인증의 특징
- 무상태성
> 사용자의 인증 정보가 담겨 있는 토큰이 서버가 아닌 클라이언트에 있으므로 서버에 저장할 필요가 없다.
> 클라이언트에서 인증 정보가 담긴 토큰을 생성하고 인증한다.
> 클라이언트에서는 사용자 인증 상태를 유지하면서 이휴 요청을 해야 하는데 이것을 **상태관리** 한다고 한다.

- 확장성
> 서버를 확장 할 때 **상태 관리**를 신경 쓸 필요가 없으니 서버 확장에도 용이 하다.
> 예를 들어 물건을 파는 서비스가 있고 , 결제를 위한 서버와 주문을 위한 서버가 분리되어 있다고 가정해 보겠습니다. 
> 세션 인증 기반은 각각 API에서 인증을 해야 되는 것과는 달리 토큰을 가지는 주체는 서버가 아니라 클라이언트이기 때문에 가지고 있는 하나의 토큰으로 결제 서버와 주문 서버에게 요청을 보낼 수 있다.
> 추가로, 소셜 로그인처럼, 토큰 기반 인증을 사용하는 다른 시스템에 접근해 로그인 방식을 확장할 수도 있고,
> 다른 서비스에 권한을 공유할 수도 있다.

- 무결성
> 토큰 방식 HMAC(hash-based message authentication) 기법이라고도 부르는데요. 토큰을 발급한 이우에는 토큰 정보를 변경하는 행위를 할 수 없다. 
> 즉 토큰의 무결성이 보장된다.

### JWT
- 발급받은 JWT를 이용해 인증을 하려면 HTTP 요청 헤더 중에 Authorization 키값에 **Bearer + JWT 토큰값**을 넣어 보내야 한다.
![jwt 토큰값.png](..%2F..%2F..%2FDownloads%2Fjwt%20%ED%86%A0%ED%81%B0%EA%B0%92.png)

- JWT 구성요소
![jwt 구성요소.png](..%2F..%2F..%2FDownloads%2Fjwt%20%EA%B5%AC%EC%84%B1%EC%9A%94%EC%86%8C.png)
- 헤더에는 토큰의 타입과 해싱 알고리즘을 지정하는 정보가 담겨 있다.
```json
{
  "typ": "JWT",
  "alg": "HS256"
}
```
- 헤더의 구성표

| name | explanation                       |
|------|-----------------------------------|
| typ  | 토큰의 타입을 지정하고 JWT라는 문자열이 들어가게 됩니다. |
| alg  | 해싱 알고리즘을 지정한다.                    |

- 내용에는 토큰과 관련된 정보를 담는다. 내용의 한 덩어리를 **클래임(claim)**이라고 부르며, 클레임은 키값의 한 쌍으로 이루어져 있습니다. 
- 클레임은 **등록된 클레임, 공개 클레임, 비공개 클래임**으로 나눌 수 있다.

- 등록된 클래임(registered claim)은 토크에 대한 정보를 담는 데 사용한다.

| name | explanation                                                                                         |
|------|-----------------------------------------------------------------------------------------------------|
| iss  | 토큰 발급자(issuer)                                                                                      |
| sub  | 토큰 제목(subject)                                                                                      |
| aud  | 토큰 대상자(audience)                                                                                    |
| exp  | 토큰의 만료 시간(expiraton).시간은 NumericDate 형식으로 하며(예:1480849147370), 항상 현재 시간 이후로 설정한다.                   |
| nbf  | 토큰의 활성 날짜와 비슷한 개념으로 nbf는 Not Before를 의미합니다. NumericDate 형식으로 날짜를 지정하며, 이 날짜가 지나기 전까지는 토큰이 처리되지 않는다. |
| iat  | 토큰이 발급된 시간으로 iat은 issued at을 의미한다.                                                                  |
| jti  | JWT의 고유 식별자로서 주로 일회용 토큰에 사용한다.    <br/>                                                                  |

- 공개 클레임(public claim)은  공개되어도 상관없는 클레임을 의미한다. 충동을 방지할 수 있는 이름을 가져야하며,
- 보통은 클레임 이름을 URI로 짓는다.
- 비공개 클레임(private claim) 은 공개되면 안되는 클레임을 의미합니다. 클라이어트와 서버 간의 통신에 사용된다.
- 예를 들어 다음과 같은 JWT의 내용이 있다고 가정한다.
```json
{
  "iss": "azsx92@gmail.com", // 등록된 클레임
  "iat": "1622370878", // 등록된 클레임
  "iss": "1622372678", // 등록된 클레임
  "https://jeongpyeonghwa.com/jwt_claims/is_admin": true, // 공개 클레임
  "email": "azsx92@gmail.com", // 비공개 클레임
  "hello": "안녕하세요!", // 비공개 클레임
}
```
- **서명**은 해당 코튼이 조작되었거나 변경되지 않았음을 확인하는 용도로 사용하며, 
- 헤더의 인코딩값과 내용의 인코딩값을 합친 후에 주어진 비밀키를 사용해 해시값을 생성한다.


### 토큰 유효기간 - 리프레시 토큰
> 토큰을 주고 받는 환경이 보안에 취약해서 토큰 자체가 노출되면 위험하다.
> 그래서 토큰 유효기간을 줄이고 **리프레시 토큰**을 사용해야 한다.
> 리프레시 토큰은 액세스 토큰과 별개의 토큰이다.
> 액세스 토큰이 만료되었을 때 새로운 액세스 토큰을 발급하기 위해 사용한다.
> 
>![리프레시.png](..%2F..%2F..%2FDownloads%2F%EB%A6%AC%ED%94%84%EB%A0%88%EC%8B%9C.png)
> 1. 클라이언트가 서버에게 인증을 요청한다.
> 2. 서버는 클라이언트에서 전달한 정보를 바탕으로 인증 정보가 유효한지 확인한 뒤, 액세스 토큰과 리프레시 토큰을 만들어 클랑이언트에게 전달한다.
> 3. 서버에서 생성한 리프레시 토큰은 DB에도 저장해 둔다.
> 4. 인증이 필요로 하는 API를 호출할 때 클아이언트에 저장된 액세스 토큰과 함께 API를 요청한다.
> 5. 서버는 전달받은 액세스 토큰이 유효한지 검사한 뒤에 유효하다면 클라이언트에서 요청한 내용을 처리한다.
> 6. 시간이 지나고 액세스 토큰이 만료된 뒤에 클라이언트에서 원하는 정보를 얻기 위해 서버에게 API 요청을 보낸다. 
> 7. 서버는 액세스 토큰이 유효한지 검사한다.
> 8. 클라이언트는 이 응답을 받고 저장해둔 리프레시 토큰과 함께 새로운 액세스 토큰을 발급하는 요청을 전송한다.
> 9. 서버는 전달받은 리프레시 토큰이 유효한지, DB에서 리프레시 토큰을 조회한 후 저장해둔 리프레시 토큰과 같은지 확인한다.
> 10. 만약 유요한 리프레시 토큰이라면 새로운 액세스 토큰을 생성한 뒤 응답한다. 이후 4번과 같이 다시 API를 요청한다.

## 구현하기
### 의존성 추가 
- build.gradle
```json
testAnnotationProcessor 'org.projectlombok:lombok'
testImplementation 'org.projectlombok:lombok'
implementation 'io.jsonwebtoken:jjwt:0.9.1' // 자바 JWT 라이브러리
implementation 'javax.xml.bind:jaxb-api:2.3.1' // XML 문서와 Java 객체 간 매핑 자동화
```

### 토큰 제공자 추가
#### 1 단계 
- JWT 토큰을 만들려면 이슈 발급자, 비밀키를  필수로 설정해야 한다.
- application.yml
```yaml
jwt:
  issuer: {사용자 이메일}
  secret_key: {임의의 문자}
```
#### 2 단계
- 해당 값들을 변수로 접근하는 데 사용할 JwtProperties 클래스 생성
- config/jwt 패키지
```java
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt") // 자바 클래스에 프로퍼티값을 가져와서 사용하는 애너테이션
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
```
#### 3 단계
- 토큰을 생성하고 올바른 토큰인지 유효성 검사를 하고, 토큰에서 필요한 정보를 가져오는 클래스를 작성.
- TokenProvider.java 파일을 config/jwt 디렉터리에 생성
```java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.shinsunyoung.musthavespringboot.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // JWT 토큰 생성 메서드
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤어 typ : JWT
                //내용 iss : properties에서 설정한 이메일
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)   //내용 iat : 현재 시간
                .setExpiration(expiry)  //내용 exp : expiry 멤버 변숫값
                .setSubject(user.getEmail())    //내용 sub : 유저 이메일
                .claim("id", user.getId())  //클레임 id : 유저 ID
                // 서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    // JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) { //복호화 과정에서 에러가 나면 유효하지 않은 토큰
            return false;
        }
    }


    //토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject
                (), "", authorities), token, authorities);
    }

    //토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser() // 클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
```

#### 4 단계
- 테스트 코드
```java
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
public class JwtFactory {

    private String subject = "test@email.com";

    private Date issuedAt = new Date();

    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());

    private Map<String, Object> claims = emptyMap();

    //빌더 패턴을 사용해 설정이 필요한 데이터만 선택 설정
    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expiration,
                      Map<String, Object> claims) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiration = expiration != null ? expiration : this.expiration;
        this.claims = claims != null ? claims : this.claims;
    }

    public static JwtFactory withDefaultValues() {
        return JwtFactory.builder().build();
    }

    //jjwt 라이브러리를 사용해 JWT 토큰 생성
    public String createToken(JwtProperties jwtProperties) {
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }
}
```
- 빌더 패턴을 사용하지 않으면 필드 기본값 사용

#### 5 단계
- TokenProvider Test
```java
@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    //generateToken() 검증 테스트
    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        // given
        //토큰에 유저 정보를 추가하기 위한 테스트 유저 생성

        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        // when
        //토큰 제공자의 generateToken()메서드를 호출해 토큰 생성

        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        // then
        //jjwt 라이브러리를 사용해 토큰 복호화.
        //토큰을 만들 때 클레임으로 넣어둔 id값이 given절에서 만든 유저 ID와 동일한지 확인
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    //validToken() 검증 테스트
    //검증 실패 테스트
    @DisplayName("validToken(): 만료된 토큰인 경우에 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken() {
        // given
        //jjwt 라이브러리 사용해서 토큰 생성
        //이미 만료된 토큰으로 생성
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        // when
        // 토큰 제공자의 validToken() 메서드를 호출해 유효한 토큰인지 검증한 뒤 결괏값 반환
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isFalse();
    }

    //검증 성공 테스트
    @DisplayName("validToken(): 유효한 토큰인 경우에 유효성 검증에 성공한다.")
    @Test
    void validToken_validToken() {
        // given
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isTrue();
    }

    //getAuthentication() 검증 테스트
    @DisplayName("getAuthentication(): 토큰 기반으로 인증정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        // given
        //토큰 제목인 subject는 유저 이메일
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        // when
        //인증 객체 반환
        Authentication authentication = tokenProvider.getAuthentication(token);

        // then
        // 반환받은 인증 객체의 유저 이름을 가져와 given절에서 설정한 subject값이랑 같은지 확인
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    //getUserId() 검증 테스트
    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getUserId() {
        // given
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        // when
        Long userIdByToken = tokenProvider.getUserId(token);

        // then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}
```

### 리프레시 토큰 도메인 구현
> 리프레시 토큰은 데이터베이스에 저장하는 정보이므로 엔티티와 리포지터리를 추가해야한다.
```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public RefreshToken(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;

        return this;
    }
}
```

#### 1. TokenAuthenticationFilter
>config 디렉터리에 TokenAuthenticationFilter를 만든다.
액세스 토큰값이 담긴 Authorization 헤더값을 가져온 뒤 액세스 토큰이 유효하다면 인증 정보를 설정한다.

```java
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {

        //요청 헤더의 Authorization 키의 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        //가져온 값에서 접두사 제거
        String token = getAccessToken(authorizationHeader);
        //가져온 토큰이 유효한지 확인하고, 유효할 때는 인증 정보 설정
        if (tokenProvider.validToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
```
- 요청 헤더에서 키가 'Authorization'인 필드의 값을 가져온 다음 토큰의 접두사 Bearer를 제외한 값을 얻는다.
- 만약 값이 null이거나 Bearer로 시작하지 않으면 null 반환
- 이어서 가져온 토큰이 유효한지 확인하고, 유효하다면 인증 정보를 관리하는 시큐리티 컨텍스트에 인증 정보 설정.
- 이후, 컨텍스트 홀더에서 getAuthentication()을 사용해 인증 정보를 가져오면 유저 객체 반환

### 토큰 API 구현
> 유효한 리프레시 토큰이라면 새로운 액세시 토큰을 생성하는 토큰 API 구현
#### 토큰 서비스 -UserSERVICE
```java
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Long save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }


}

```

#### RefreshTokenService
> 전달받은 리프레시 토큰으로 리프레시 토큰 객체를 검색해서 전달하는 findByRefreshToken() 구현
```java
@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
```

#### TokenService
>createNewAccessToken()은 전달받은 리프레시 토큰으로 토큰 유효성 검사를 진행하고, 유효한 토큰인 때 리프레시 토큰으로 사용자 ID를 찾는다.
후에, 토큰 제공자의 generateToken()을 호출해서 새로운 액세스 토큰 생성

```java
@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
```

#### CreateAccessTokenRequest, CreateAccessTokenResponse
> dto 패키지에 토큰 생성 요청 및 응답 담당 DTO
```java
@Getter
@Setter
public class CreateAccessTokenRequest {
    private String refreshToken;
}

@AllArgsConstructor
@Getter
public class CreateAccessTokenResponse {
    private String accessToken;
}
```

#### TokenApiController
> 실제로 요청을 받고 처리할 컨트롤러 생성
> /api/token POST 요청이 오면 토큰 서비스에서 리프레시 토큰을 기반으로 새로운 액세스 토큰 생성
```java
@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
```

### 테스트
#### TokenApiControllerTest
```java
@SpringBootTest
@AutoConfigureMockMvc
class TokenApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        userRepository.deleteAll();
    }

    @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
    @Test
    public void createNewAccessToken() throws Exception {
        // given
        //테스트 유저를 생성하고, jjwt 라이브러리를 이용해 리프레시 토큰을 만들어 데이터베이스에 저장
        // 토큰 생성 API의 요청 본문에 리프레시 토큰을 포함하여 요청 객체 생성
        final String url = "/api/token";

        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        String refreshToekn = JwtFactory.builder()
                .claims(Map.of("id", testUser.getId()))
                .build()
                .createToken(jwtProperties);

        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToekn));

        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.setRefreshToken(refreshToekn);
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        //토큰 추가 API에 요청을 보냄.
        //요청 타입은 JSON, given 절에서 미리 만들어둔 객체를 요청 본문으로 함께 보낸다.
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        //응답 코드가 201 Created인지 확인, 응답으로 온 액세스 토큰이 비어있지 않은지 확인
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

}
```