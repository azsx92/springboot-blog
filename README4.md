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
#### 03 단계
#### 04 단계
#### 05 단계
