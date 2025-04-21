## 3.  스프링 시큐리티로 OAuth2를 구현하고 적용하기
- 가장 먼저 쿠키 관리 클래스를 구현하고, OAuth2에서 제공 받은 인증 객체로 사용자 정보를 가져오는 역할을 하는 서비스를 구현한다.
- 그 뒤에는 WebSecurityConfig 클래스 대신 사용할 OAuth2 설정 파일을 구현한다.
- 마지막으로는 직접 테스트하도록 뷰를 구성한다.

### 3.1 의존성 추가하기
- build.gradle에 의존성을 추가한다.
```yaml
dependencies {
  ... 생략 ...
        // OAuth2를 사용하기 위한 스타터 추가
  implementation 'org.springframwork.boot:spring-boot-starter-oauth2-client'
}
```

### 3.2 쿠키 관리 클래스 구현하기
- OAuth2 인증 플로우를 구현하며 쿠키를 사용할 일이 생기는 데 그때마다 쿠키를 생성하고 삭제하는 로직을 추가하면 불편하므로 유틸리티로 사용할 쿠키 관리 클래스를 미리 구현한다.
- util 패키지를 새로 만들고 CookieUtil.java 파일을 생성한 뒤 코드를 입력한다.
![스크린샷 2025-04-16 오후 10.04.31.png](..%2F..%2F..%2FDesktop%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-16%20%EC%98%A4%ED%9B%84%2010.04.31.png)


```java
package com.springboot.blog.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtil {
    // 요청값(이름, 값, 만료 기간)을 바탕으로 쿠키 추가
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    // 쿠키의 이름을 입력받아 쿠키 삭제
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    // 객체를 직렬화해 쿠키의 값으로 변환
    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    // 쿠키를 역직렬화해 객체로 변환
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
}

```

#### addCookie
- 요청값(이름, 값, 만료 기간)을 바탕으로 HTTP 웅답에 쿠키를 추가한다.


#### deleteCookie
- 쿠키 이름을 입력받아 쿠키를 삭제한다. 실제로 삭제하는 방법은 없으므로 파라미터로 넘어온 키의 쿠키를 빈 값으로 바꾸고 만료 시간을 0으로 설정해 쿠키가 재생성 되자마자 만료처리합니다.

#### serialize
- 객체를 직렬화해 쿠키의 값으로 들어갈 값으로 변환한다.

#### deserialize
쿠키를 역직렬화 객체로 변환한다.

## 3.3 OAuth3 서비스 구현하기
- 사용자 정보를 조회해 users 테이블에 사용자 정보가 있다면 리소스 서버에서 제공해주는 이름을 업데이트하고 없다면
- users 테이블에 새 사용자를 생성해 데이터베이스에 저장하는 서비스를 구현한다.

#### 01 단계
- domain 패키지의 User.java 파일에 사영자 이름과 OAuth 관련 키를 저장하는 코드를 추가한다.
```java
package com.springboot.blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User implements UserDetails { //UserDetails를 상속받아 인증 객체를 사용
//    ... 생략 ...
// 사용자 이름
@Column(name = "nickname", unique = true)
private String nickname;

    @Builder
    public User(String email, String password, String nickname) {
        this.email    = email;
        this.password = password;
        this.nickname = nickname;
    }

//    ... 생략 ...
    
    // 사용자 이름 변경
    public User update(String nickname) {
        this.nickname = nickname;
        return this;
    }

}

```
#### 02 단계
- config 패키지에 oauth 패키지를 만들고 OAuth2UserCustomService.java 파일을 생성한 다음 리소스 서버에서 보내주는
- 사용자 정보를 불러오는 메서드인 loadUser()를 통해 사용자를 조회하고, users 테이블에 사용자 정보가 있다면 이름을 업데이트하고 없다면 saveOrUpdate() 메서드를 실행해 users 테이블에 회원 데이터를  추가한다.
```java
package com.springboot.blog.config.oauth;

import com.springboot.blog.domain.User;
import com.springboot.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 요청을 바탕으로 유저 정보를 담은 객체 반환
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);
        return user;
    }
// 유저가 있으면 업데이트, 없으면 유저 생성
    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .nickname(name)
                        .build());
        return userRepository.save(user);
    }
}

```
> 부모 클래스인 DefaultOAuth2UserService 에서 제공하는 OAuth 서비스에서 제공하는 정보를 
> 기반으로 유저 객체를 만들어 주는 loadUser() 메서드를 사용해 사용자 객체를 불러온다.
> 사용자 객체는 식별자, 이름, 이메일, 프로필 사진 등의 정보를 담고 있다.
> 다음 그림은 이해를 돕기 위해 불러온 객체다. 그리고 saveOrUpdate() 메서드는 사용자가 user 테이블에 있으면 업데이트하고 없으면 사용자를 새로 생성해서 데이터베이스에 저장한다.
#### 4. OAuth2 설정 파일 작성하기
- OAuth2와 JWT를 함께 사용하려면 기존 스프링 시큐리티를 구현하며 작성한 설정이 아니라 다른 설정을 사용해야 한다.
- OAuthw, JWT에 알맞게 설정 파일을 수정한다.
#### 01 단계
- 기존의 폼 로그인 방식을 사용하기 위해 구성했던 설정 파일인 WebSecurityConfig.java 내용을 모두 주석 처리한다.
```java
/*

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final UserDetailService userService;

    //1. 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**"));
    }

    //    2. 특정 http 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .requestMatchers("/login", "/signup", "/user").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/articles")
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .and()
                .csrf().disable()
                .build();
    }


    // 7 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
*/

```
#### 02 단계
- config 파일에 WebOAuthSecurityConfig.java 파일을 생성하고 코드를 작성한다.
```java
package com.springboot.blog.config;

import com.springboot.blog.config.jwt.TokenProvider;
import com.springboot.blog.config.oauth.OAuth2UserCustomService;
import com.springboot.blog.repository.RefreshTokenRepository;
import com.springboot.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;


@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Bean
    public WebSecurityCustomizer configure() { // 스프링 시큐리티 기능 비활성화
        return (web -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/img/**", "/css/**", "/js/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. 토큰 방식으로 인증을 하기 때문에 기존에 사용하던 폼로그인, 세션 비활성화
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 2 헤더를 확인 할 커스텀 필터 추가
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 3 토큰 재발급 URL은 인증 없이 접근 가능하도록 설정. 나머지 API URL은 인증 필요
        http.authorizeRequests()
                .requestMatchers("/api/token").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll();

        http.oauth2Login()
                .loginPage("/login")
                .authorizationEndpoint()
                // 4. authorzation 요청과 관련된 상태 저장
                .authorizationRequestRepository(oAuth2AuthorizationRequestBaseOnCookieRepository())
                .and()
                .successHandler(oAuth2SuccessHandler()) // 5. 인증 성공 시 실행 할 핸들러
                .userInfoEndpoint()
                .userService(oAuth2UserCustomService);

        http.logout()
                .logoutSuccessUrl("/login");

        // 6. /api로 시작하는 url인 경우 401 상태 코드를 반환하도록 예외 처리
        http.exceptionHandling()
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**"));
        return http.build();

    }
    
    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBaseOnCookieRepository(),
                userService);
    }
    
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBaseOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }
    
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

```

1. filterChain() 메서드
- 토큰 방식으로 인증을 하므로 기존 폼 로그인, 세션 기능을 비활성화 한다.

2. addFilterBefore() 헤더값 확인용 커스텀 필터  추가
- 헤더값을 확인할 커스텀 필터를 추가한다.
- 이 필터는 9.2.4 '토큰 필터 구한하기'에서 구현한 TokenAuthenticationFilter 클래스 이다.

3. authorizeRequests() 메서드 URL 인증 설정
- 토큰 재발급 URL은 인증 없이 접근 하도록 설정하고 나머지 API들은 모두 인증을 해야 접근하도록 설정한다.

4,5. oauth2Login() 메서드 이후 체인 메서드 
- OAuth2에 필요한 정보를 세션이 아닌 쿠키에 저장해서 쓸 수 있도록 인증 요청과 관련된 상태를 저장할 저장소를 설정한다.
- 인증 성공시 실행할 핸들러도 설정한다. 해당 클래스는 아직 구현하지 않았으므로 에러가 발생할 거다.
- 바로 다음에 관련 코드를 작성하므로 우선 넘어 간다.

6. exceptionHandling() 메서드 예외 처리 설정
- /api로 시작하는 url인 경우 인증 실패 시 401 상태 코드 즉 Unauthorized를 반환한다.

### 03 단계
- OAuth2에 필요한 정보를 세션이 아닌 쿠키에 저장해서 쓸 수 있도록 인증 요청 과 관련된 상태를 저장할 저장소를 구현 한다.
- config/oauth 패키지 파일을 생성한다. OAuth2AuthorizationRequestBasedCookieRepository.java 파일을 생성한다.
- 권한 인증 흐름에서 클라이언트의 요청을 유하하는 데 사용하는 AuthorizationRequestRepository 클래스를 구현해 쿠키를 사용해 OAuth의 정보를 가져오고 저장하는 로직을 작성한다.

```java
package com.springboot.blog.config.oauth;

import com.springboot.blog.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;

public class OAuth2AuthorizationRequestBasedCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME ="oauth2_auth_request";
    private final static int COOKIE_EXPIRE_SECONDS = 18000;
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }
    
    
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        return CookieUtil.deserialize(cookie,OAuth2AuthorizationRequest.class);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request,response);
            return;
        }
        CookieUtil.addCookie(response , OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
    }
    
    public void removeAuthorizationRequestCookies(HttpServletRequest request , HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    }
}

```

### 04 단계
- 이어서 인증 성공 시 실행할 핸들러를 구현한다. 해당 빈을 구현할 때 사용할 메서드를 만들기 위해 service 패키지의 UserService.java 파일을 연 뒤 다음과 같이 수정한다.
- BCryptPasswordEncoder를 삭제하고 BCryptPasswordEncoder를 생성자를 사용해 직접 생성해서 패스워드를 암호화 할 수 있게 코드를 수정한 다음 findByEmail() 메서드를 추가한다.
```java
package com.springboot.blog.service;

import com.springboot.blog.domain.User;
import com.springboot.blog.dto.AddUserRequest;
import com.springboot.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                // 1. password 암호화
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

// 메서드 추가
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
- findByEmail() 메서드는 이메일을 입력받아 users 테이블에서 유처를 찾ㅂ고, 없으면 예외를 발생한다.

### 05 단계 config/oauth 패키지에 OAuth2SuccessHandler.java 파일을 생성해 다음과 같이 작성한다.
```java
package com.springboot.blog.config.oauth;

import com.springboot.blog.config.jwt.TokenProvider;
import com.springboot.blog.domain.RefreshToken;
import com.springboot.blog.domain.User;
import com.springboot.blog.repository.RefreshTokenRepository;
import com.springboot.blog.service.UserService;
import com.springboot.blog.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION  = Duration.ofDays(1);
    public static final String REDIRECT_PATH = "/articlers";

    private final TokenProvider tokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userService.findByEmail((String) oAuth2User.getAttributes().get("email"));
        
        // 1 리프레시 토큰 생성 -> 저장 -> 쿠카에 저장
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken);
        addRefreshTokenToCookie(request, response , refreshToken);
        // 2. 액세스 토큰 생성 -> 패스에 액세스 토큰 추가
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(accessToken);
        // 3. 인증 관려누 설정값 , 쿠키 제거
        clearAuthenticationAttributes(request, response);
        // 4 리다이렉트 
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        
    }
// 생성된 리프레시 토큰을 전달받아 데이터베이스에 저장
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));
        
        refreshTokenRepository.save(refreshToken);
    }
    
    // 생성된 리프레시 토큰을 쿠키에 저장
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }
    
    // 인증 관련 설정 값, 쿠케 제거
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
    
// 액세스 토큰을 패스에 저장
    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token",token)
                .build()
                .toUriString();
                
    }


}

```

- 스프링 시큐리티의 기본 로직에서는 별도의 authenticationSuccessHandler를 지정하지 않으면 로그인 성공 이후 SimpleUrlAuthenticationSuccessHandler 를 사용한다.
- 일반적인 로직은 동일하게 사용하고, 로큰과 관련된 작업만 추가로 처리하기 위해 SimpleUrlAuthenticationSuccessHandler을 상속받은 뒤에 onAuthenticationSuccess() 메서드를 오버라이트 한다.

1. 리프레시 토큰 생성, 저장 , 쿠키에 저장
- 토큰 제공자를 사용해 리프레시 토큰을 만든 뒤에, saveRefreshToken() 메서드를 호출해 해당 리프레시 토큰을 데이터베이스에 유저 아이디와 함께 저장한다.
- 그 이후에는 클라이언트에서 액세스 토큰이 만료되면 재발급 요청하도록 addRefreshTokenCookie() 메서드를 호출해 쿠키에 리프레시 토큰을 저장한다.

2. 액세스 토큰 생성, 패스에 액세스 토큰 추가
- 토큰 제공자를 사용해 액세스 토큰을 만든 뒤에 쿠키에서 리다이렉트 경로가 담긴 값을 가져와 쿼리 파라미터에 액세스 토큰 을 추가 한다.
```http request
액세스 토큰을 클아이너트에게 전달
http:// localhost:8080/articles?token=eyJ0XAiOiJKV1QiLCJhbGciOiJIUZI1NiJ9.eyJpc3MiOiJanVmcmVzaEBnbwFpbvj..
```

3. 인증과 관련 설정 값, 쿠키 제거
- 인증 프로세스를 진행하면서 세션과 쿠키에 임시로 저장해둔 인증 관려 데이터를 제거한다.
- 기본적으로 제공하는 메서드인 clearAuthenticationAttributes()는 그대로 호출하고 removeAuthorizationRequestCookies() 를 추가하고 호출해 OAuth 인증을 위해 저장된 정보로 삭제 한다.

4. 리다이렉트
5. 2 에서 만들 URL로 리다이렉트 한다.

