## 11.4 일래스틱 빈스토그에 우리의 서비스 배포하기
### 11.4.1 애플리케이션  배포하기
- 01단계 인텔리제이를 실행해서 [Gradle] 탭을 누른 다음 [Tasks -> build -> build]를 더블 클릭해 빌드를 진행한다.
- ![애플리케이션 배포 01단계.png](%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%20%ED%95%98%EA%B8%B0%2F%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%2001%EB%8B%A8%EA%B3%84.png)
- 여기서 나는 application.yml에 나의 cloudID 와 password를 보안화 하고 싶었다. 
---

## 인텔리제이에서 환경 변수로 민감 정보 숨기기

### 1. **application.yml에서 환경 변수 사용**

예시:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
jwt:
  secret_key: ${JWT_SECRET_KEY}
```

- `${...}` 형태로 환경 변수 값을 참조합니다.

---

### 2. **IntelliJ에서 환경 변수 설정 방법**

1. **오른쪽 위 실행/디버그 버튼 옆의 드롭다운 클릭**
   → **Edit Configurations...** 메뉴 선택
2. **실행/디버그 설정 창에서**
   → 아래쪽의 **"Environment variables"** 항목을 찾음
   → 우측의 `...` 버튼 클릭
3. **환경 변수 추가**
    - `+` 버튼 클릭
    - `GOOGLE_CLIENT_ID=값`, `GOOGLE_CLIENT_SECRET=값`, `JWT_SECRET_KEY=값` 등
   ```text
    GOOGLE_CLIENT_ID=your-client-id;GOOGLE_CLIENT_SECRET=your-client-secret;JWT_SECRET_KEY=your-jwt-secret
   ```
   - 원하는 key-value 쌍을 입력
4. **확인(OK) 후 실행**
    - 이제 Spring Boot가 환경 변수로부터 값을 읽어와서 실행됩니다.

> 이 방법은 인텔리제이에서만 적용되며, 배포 시에는 AWS Elastic Beanstalk 등에서 환경 변수를 별도로 등록해야 합니다[^2][^3][^5].

---

### 3. **장점**

- application.yml에 민감 정보가 들어가지 않으므로 GitHub에 노출 위험이 없음
- 로컬/운영 환경 모두 환경 변수로 일관성 있게 관리 가능
- 실수로 yml을 올려도 정보 유출 우려 없음

---

### 4. **참고**

- 환경 변수 이름은 대소문자를 구분하므로 yml과 환경 변수 이름이 정확히 일치해야 합니다.
- 환경 변수로 값을 주입할 때는 `${ENV_NAME}` 형태로만 사용해야 합니다.
- 여러 실행 환경(로컬, 서버, CI 등)에서 동일한 방식으로 관리할 수 있습니다.

---

## 요약

- **application.yml**에 `${환경변수명}`으로 민감 정보 입력
- **IntelliJ → Edit Configurations → Environment variables**에서 key-value로 환경 변수 등록
- 안전하게 실행 및 GitHub 노출 방지

이 방식이 인텔리제이에서 공식적으로 지원하는 가장 안전하고 편리한 방법입니다[^2][^3][^5].
- 성공적으로 빌드 완료
- ![애플리케이션 배포하기 01단계 2.png](%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%20%ED%95%98%EA%B8%B0%2F%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%ED%95%98%EA%B8%B0%2001%EB%8B%A8%EA%B3%84%202.png)

<div style="text-align: center">⁂</div>

[^1]: https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000111630-Issue-reading-yml-yaml-files-from-a-spring-boot-app-inside-IntelliJ

[^2]: https://velog.io/@hjm2530/환경변수-설정을-통해-민감한-정보-숨기기

[^3]: https://devlog-wjdrbs96.tistory.com/363

[^4]: https://stackoverflow.com/questions/37404703/spring-boot-how-to-hide-passwords-in-properties-file

[^5]: https://www.youtube.com/watch?v=oYfd9pDXip8

[^6]: https://stackoverflow.com/questions/48675377/how-to-get-intellij-to-recognize-properties-in-application-yml

[^7]: https://www.jetbrains.com/help/idea/security-model.html

[^8]: https://stackoverflow.com/questions/46827374/spring-boot-yaml-autocomplete-support-in-intellij-community-edition/60136160

[^9]: https://www.jetbrains.com/help/idea/pro-tips.html

[^10]: https://www.inflearn.com/community/questions/1464386/인텔리제이-무료버전-사용중입니다-프로젝트-생성-시

[^11]: https://sdtimes.com/security/the-key-to-successful-secrets-management-is-to-make-the-best-way-the-easiest-way/

[^12]: https://www.baeldung.com/intellij-idea-environment-variables

[^13]: https://intellij-support.jetbrains.com/hc/en-us/community/posts/9901730030098-Possible-bug-on-auto-complete-for-application-properties-and-application-yml-IntelliJ-Ultimate

[^14]: https://github.com/ChrisCarini/environment-variable-settings-summary-intellij-plugin

[^15]: https://velog.io/@ji-jjang/application.yml-management

[^16]: https://cloud.google.com/code/docs/intellij/secret-manager

[^17]: https://docs.micronaut.io/latest/guide/

[^18]: https://iwoohaha.tistory.com/297

[^19]: https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html

[^20]: https://www.jetbrains.com/help/idea/kubernetes.html

--- 
### 02 단계
- 빌드가 끝나면 [build-> libs]에 빌드 완성 파일이 생성된다. 생성된 jar 파일을 기억하기 쉬운 위치에 복사 해둔다.
- ![애플리케이션 배포하기 02단계.png](%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%20%ED%95%98%EA%B8%B0%2F%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%ED%95%98%EA%B8%B0%2002%EB%8B%A8%EA%B3%84.png)

### 03 단계
- 일래스틱 빈스토크로 돌아가 생성된 환경의 이름을 선택하고 [업로드 및 배포]를 누른 다음 [파일 선택] 버튼을 눌러 jar 파일을 선택한다.
- ![애플리케이션 배포하기 03단계.png](%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%20%ED%95%98%EA%B8%B0%2F%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%ED%95%98%EA%B8%B0%2003%EB%8B%A8%EA%B3%84.png)
- ![애플리케이션 배포하기 03단계 2.png](%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%20%ED%95%98%EA%B8%B0%2F%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%ED%95%98%EA%B8%B0%2003%EB%8B%A8%EA%B3%84%202.png)

### 04 단계
- 완료했으면 [구성]을 클릭한다. 그리고 소프트웨어에서 [편집]을 선택한다. 여기서 애플리케이션을 실하기 위한 하위 포트 값과 공개 사이트에 올리면 안 되는 민감한 정보를 추가한다.
- | name                                                            | values                                       |
  |-----------------------------------------------------------------|----------------------------------------------|
  | SERVER_PORT                                                     | 5000                                         |
  | JWT_SECRET_KEY                                                  | study-springboot                             |
  | SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID     | <구글 OAuth2에서 발급받은 클라이언트 아이디> |
  | SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET | <구글 OAuth2에서 발급받은 클라이언트 비밀키> |
  | SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_SCOPE         | email,profile                                |

### 05 단계
- 값 추가 후 [확인]을 누르면 환경을 업데이트한다. 업데이트를 완료한 다음 다음 일래스틱 빈스토크에서 제공하는 서버의 URL에 접속한 다음 /login 패스를 붙여 로그인 화면에 제대로 접속되는지 확인 한다.
- 
- 여기까지 왔으면 제대로 배포가 된거다. **다만 OAuth 로그인을 시도하면 액세스 거부 에러 메시지가 발생한다.**
- 이제 하나만 더 하면 된다. OAuth 서비스에서 승인된 URL에 일래스틱 빈스토크 서버 URL을 추가하면 된다.
- 