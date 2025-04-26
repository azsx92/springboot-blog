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
- 그런데 확인 해 보니 502 게이트가 발생했다. 
- 우선 첫 번째는 **포트 불일치** 라고 한다.

## 502 Bad Gateway 오류 원인 및 해결 방법

AWS Elastic Beanstalk에서 502 Bad Gateway 오류가 발생하는 가장 일반적인 원인은 **포트 불일치 문제**입니다. 이미지와 설정을 확인한 결과, 다음과 같은 문제가 있을 수 있습니다.

## 주요 원인 및 해결책

### 1. **포트 불일치 문제**

- **원인:** Elastic Beanstalk의 Nginx는 기본적으로 포트 5000으로 요청을 전달하지만, Spring Boot는 기본적으로 포트 8080에서 실행됩니다[^5][^7][^9].
- **확인:** 환경 변수 설정에 `SERVER_PORT=5000`이 있는지 확인하세요.
- **해결:**
    - 이미 `SERVER_PORT=5000`으로 설정되어 있다면, 애플리케이션에서 이 값을 제대로 읽어오는지 확인하세요.
    - `application.yml`에 명시적으로 `server.port: 5000`을 추가해 볼 수도 있습니다.


### 2. **환경 변수 이름 확인**

- **원인:** Spring Boot에서 환경 변수를 올바르게 인식하지 못할 수 있습니다[^8][^10].
- **해결:**
    - 환경 변수는 대문자와 밑줄(_)을 사용하는 형식으로 지정해야 합니다.
    - `GOOGLE_CLIENT_ID`와 `GOOGLE_CLIENT_SECRET`, `JWT_SECRET_KEY`가 올바르게 설정되었는지 확인하세요.


### 3. **데이터베이스 연결 문제**

- **원인:** RDS 연결 설정이 잘못되었거나, 보안 그룹 설정으로 인해 데이터베이스에 접근할 수 없을 수 있습니다[^6].
- **해결:**
    - RDS 엔드포인트, 사용자 이름, 비밀번호가 올바른지 확인하세요.
    - 보안 그룹이 애플리케이션 서버에서 데이터베이스로의 접근을 허용하는지 확인하세요.


## 추가 확인 사항

### 로그 확인

- Elastic Beanstalk 콘솔에서 로그를 확인하여 더 상세한 오류 메시지를 찾으세요:

1. 환경 → 로그 → "전체 로그 요청"
2. `/var/log/nginx/error.log`와 `/var/log/eb-activity.log` 파일을 특히 주목하세요.


### 애플리케이션 설정 확인

- `application.yml`이 배포된 JAR/WAR 파일 내에 올바르게 포함되어 있는지 확인하세요.
- 로컬에서는 정상 작동하는지 테스트해보세요.


### 전체 해결 과정

1. 환경 변수가 올바르게 설정되었는지 확인 (특히 `SERVER_PORT=5000`)
2. Elastic Beanstalk 로그에서 오류 메시지 검토
3. 필요하다면 `application.yml`에 `server.port: 5000` 명시적으로 추가
4. 변경 사항을 적용한 후 애플리케이션 재배포
5. 환경이 정상적으로 업데이트된 후 URL에 다시 접속해 보세요


---
## 1. 포트 불일치로 해결책을 사용해 봤지만 실패
#### 로그를 확인해봣다.
1. 환경 → 로그 → "전체 로그 요청"
2. `/var/log/nginx/error.log`와 `/var/log/eb-activity.log` 파일을 특히 주목해 봣더니 
3. Nginx 502 Bad Gateway가 자꾸 뜨는 이유


>Nginx에서 "502 Bad Gateway" 에러가 반복적으로 발생하는 주된 원인은 **Nginx가 프록시(리버스 프록시)로 동작하면서 백엔드 서버(예: 애플리케이션 서버)와의 연결에 실패**했기 때문입니다[^2][^6][^8].
첨부된 nginx error.log를 보면 다음과 같은 메시지가 계속 반복되고 있습니다:

```
connect() failed (111: Connection refused) while connecting to upstream, ... upstream: "http://127.0.0.1:5000/..."
```

이 메시지는 Nginx가 127.0.0.1:5000(즉, 로컬호스트의 5000번 포트)로 요청을 전달하려 했지만, 해당 포트에서 **연결이 거부(Connection refused)**되었다는 뜻입니다.

---

## 주요 원인

- **백엔드 서버 미작동 또는 다운**
  127.0.0.1:5000에서 서비스해야 하는 백엔드 서버(예: Flask, Node.js, Spring 등)가 실행되고 있지 않거나, 비정상 종료된 상태입니다[^6][^9].
- **포트 불일치**
  Nginx 설정에서 `proxy_pass http://127.0.0.1:5000`과 같이 지정했지만, 실제 백엔드 서버가 다른 포트(예: 8080, 3000 등)에서 실행 중일 수 있습니다[^6][^8].
- **방화벽/보안 그룹 문제**
  로컬 방화벽 또는 클라우드 보안 그룹 설정으로 인해 5000번 포트 접근이 차단되어 있을 수 있습니다[^6][^7].
- **애플리케이션 서버 과부하 또는 크래시**
  백엔드 서버가 과부하로 인해 일시적으로 다운되거나, 프로세스가 죽어있는 경우입니다[^2][^7].
- **Nginx 설정 오류**
  Nginx의 upstream 설정이 잘못되어 있거나, 오타가 있을 수 있습니다[^8][^9].

---

## 해결 방법

1. **백엔드 서버가 정상적으로 실행 중인지 확인**
    - 서버에서 아래 명령어로 5000번 포트가 열려 있는지 확인:

```bash
netstat -tulnp | grep 5000
```

    - 프로세스가 없다면, 백엔드 애플리케이션을 재시작하세요.
2. **Nginx 설정 확인**
    - `/etc/nginx/nginx.conf` 또는 사이트별 conf 파일에서 `proxy_pass` 지시어가 실제 백엔드 서버의 IP와 포트와 일치하는지 확인하세요[^8][^9].
    - 예시:

```
location / {
    proxy_pass http://127.0.0.1:5000;
}
```

3. **포트 및 방화벽 확인**
    - 서버 내 방화벽(ufw, firewalld 등)이나 클라우드 보안 그룹에서 5000번 포트가 허용되어 있는지 확인하세요[^6][^7].
4. **백엔드 서버 로그 확인**
    - 백엔드 서버의 로그를 확인해 크래시나 에러가 있는지 점검하세요.
5. **Nginx 재시작**
    - 설정 변경 후에는 반드시 Nginx를 재시작하세요.

```bash
sudo systemctl restart nginx
```


---

## 요약

- Nginx 502 Bad Gateway 에러는 **Nginx가 지정한 백엔드 서버(127.0.0.1:5000)와 통신하지 못해서** 발생합니다.
- 첨부 로그의 "connect() failed (111: Connection refused)"는 **해당 포트에 서버가 없거나, 접근이 차단**되어 있음을 의미합니다.
- **가장 흔한 원인**은 백엔드 서버가 실행되고 있지 않거나, 포트 설정이 잘못된 경우입니다.
- 백엔드 서버를 올바른 포트에서 실행하고, Nginx 설정을 맞게 수정하면 해결할 수 있습니다[^2][^6][^8][^9].


<div style="text-align: center">⁂</div>

[^1]: https://ppl-ai-file-upload.s3.amazonaws.com/web/direct-files/attachments/64535424/6a003ca2-ef22-43d4-8bdd-4f6dd76c5d2e/paste.txt

[^2]: https://stackify.com/error-502-bad-gateway-in-nginx-what-it-is-and-how-to-fix-it/

[^3]: https://nordvpn.com/blog/502-bad-gateway/

[^4]: https://www.linode.com/community/questions/24702/502-bad-gateway-but-nginx-status-is-running-ok

[^5]: https://stackoverflow.com/questions/74558547/nginx-reverse-proxy-throwing-502-bad-gateway

[^6]: https://earscoming.tistory.com/entry/ERROR-502-Bad-Gateway-오류-해결-방법-원인과-해결책

[^7]: https://mochahost.com/blog/nginx-502-bad-gateway-error-fix

[^8]: https://www.statsig.com/perspectives/bad-gateway-nginx-fix

[^9]: https://www.reddit.com/r/nginx/comments/176iyyt/502_bad_gateway_on_my_first_setup/

[^10]: https://velog.io/@dlehdtjq00/Nginx-502-Gateway가-발생했을-때-해결법과-근본적인-발생-원인

[^11]: https://potato-hyun.tistory.com/40

[^12]: https://community.fly.io/t/502-bad-gateway-for-nginx/19630

[^13]: https://serverfault.com/questions/810149/what-is-causing-this-error-on-nginx-server-502-bad-gateway

[^14]: https://www.metricfire.com/blog/understanding-nginx-502-bad-gateway-php-fpm/

[^15]: https://www.redswitches.com/blog/nginx-502-bad-gateway-error/

[^16]: https://codest.tistory.com/21

[^17]: https://www.redswitches.com/wp-content/uploads/2023/07/502-bad-gateway-Nginx-1024x687.jpg?sa=X\&ved=2ahUKEwjvs9WGmPWMAxUvnK8BHdbWA94Q_B16BAgCEAI

---

## macOS AirPlay Receiver가 5000번 포트를 사용해 502 에러가 발생하는 문제??
- 내각 맥 OS 를 사용하다보니 이런 문제가 있는걸 처음 알게 되었다.
- 터미널에서 왜 5000번호대가 실행되고 있는데도 안되는걸까 또 킬해봐도 다시 살아나는 문제가 있었는데 알고보니 AirPlay Receiver가 5000포트를 잡고 있어 일래스틱 빈스토크가 접근을 못한다는것이다.

1. 터미널 출력과 Elastic Beanstalk 설정을 확인한 결과, 502 Bad Gateway 에러의 주요 원인은 **macOS의 AirPlay Receiver 기능이 5000번 포트를 선점하고 있기 때문**입니다.

### 현재 상황 분석

`lsof -i tcp:5000` 명령어 결과를 보면:

```
COMMAND    PID           USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
ControlCe 84113 pyeonghwajeong   10u  IPv4 0x698fd8a51ed34024      0t0  TCP *:commplex-main (LISTEN)
ControlCe 84113 pyeonghwajeong   11u  IPv6 0x32ca336bce6d4efd      0t0  TCP *:commplex-main (LISTEN)
```

- `ControlCe` 프로세스가 5000번 포트를 사용 중입니다[^2][^6]
- `kill -9` 명령어로 프로세스를 종료해도 macOS가 자동으로 다시 시작시킵니다[^2][^6]
- 이는 macOS Monterey부터 도입된 **AirPlay Receiver** 기능으로, 5000번 포트를 사용합니다[^3][^6]


### 해결 방법

1. **AirPlay Receiver 비활성화** (로컬 개발용)
    - System Preferences(시스템 환경설정) → Sharing(공유) → AirPlay Receiver 체크 해제[^2][^3][^6]
    - 이렇게 하면 5000번 포트가 해제되어 로컬에서 Spring Boot를 실행할 수 있습니다
2. **애플리케이션 포트 변경** (대안)
    - Spring Boot 애플리케이션의 포트를 다른 번호(예: 5001, 8080 등)로 변경[^4]
    - `application.yml`에서 다음과 같이 수정:

```yaml
server:
  port: ${SERVER_PORT:8080}
```

    - Elastic Beanstalk 환경 변수에서도 `SERVER_PORT`를 새 포트로 변경
3. **Elastic Beanstalk 설정 맞추기**
    - 환경 변수가 올바르게 설정되어 있는지 확인 (이미지 상으로는 `SERVER_PORT`가 5000으로 설정됨)
    - Nginx 설정과 애플리케이션 포트가 일치하는지 확인

### 추가 확인사항

- Elastic Beanstalk에 배포하는 경우, EC2 인스턴스에서는 포트 5000 사용에 문제가 없습니다. 이 문제는 **로컬 macOS 개발 환경에서만** 발생합니다[^2][^6]
- `netstat -tulnp` 명령어는 Linux에서 사용하는 형식으로, macOS에서는 다른 형식(예: `lsof -i :5000`)을 사용해야 합니다[^3]


### 결론

1. **로컬 개발 시**: AirPlay Receiver를 비활성화하거나 애플리케이션 포트를 변경하세요
2. **Elastic Beanstalk 배포 시**: 애플리케이션이 실제로 5000번 포트에서 실행 중인지 EC2 로그를 확인하세요

macOS의 ControlCe 프로세스는 AirPlay Receiver의 일부로, 수동으로 종료해도 자동으로 재시작되도록 설계되어 있습니다[^3]. 이 문제를 해결하려면 시스템 설정에서 AirPlay 기능을 비활성화하는 것이 가장 확실한 방법입니다.

<div style="text-align: center">⁂</div>

[^1]: https://pplx-res.cloudinary.com/image/private/user_uploads/uXUlmySYkstGmad/image.jpg

[^2]: https://stackoverflow.com/questions/69736492/how-can-i-kill-a-process-that-restart-itself-each-time-on-mac

[^3]: https://www.youtube.com/watch?v=t1hD1VWuhw4

[^4]: https://tbot.plusgenie.com/resolving-port-conflict-with-apples-airplay-receiver-on-port-5000/

[^5]: https://unix.stackexchange.com/questions/158194/kill-a-process-that-keeps-restarting

[^6]: https://nono.ma/port-5000-used-by-control-center-in-macos-controlce

[^7]: https://github.com/googlecolab/colabtools/issues/4535

[^8]: https://github.com/remy/nodemon/issues/1247

[^9]: https://nono.ma/port-5000-used-by-control-center-in-macos-ventura-controlce

[^10]: https://github.com/deviantony/docker-elk/issues/739

[^11]: https://serverfault.com/questions/724334/aws-elasticbeanstalk-container-keeps-restarting

[^12]: https://dev.to/sylwiavargas/how-to-properly-close-a-port-2p36

[^13]: https://kyraweb.ca/5-steps-to-kill-a-process-on-a-port-in-ubuntu/

[^14]: https://www.reddit.com/r/cs50/comments/yt3eqi/port_5000_is_in_use_by_another_program_either/

[^15]: https://stackoverflow.com/questions/72369320/why-always-something-is-running-at-port-5000-on-my-mac

[^16]: https://www.lambrospetrou.com/articles/elastic-beanstalk-al2-go/

[^17]: https://tosbourn.com/kill-process-particular-port-explaination/

[^18]: https://askubuntu.com/questions/1339738/linux-process-automatically-starts-again-with-new-new-pid-after-killing

[^19]: https://learn.microsoft.com/en-us/troubleshoot/developer/webapps/aspnetcore/practice-troubleshoot-linux/2-3-configure-aspnet-core-application-start-automatically

[^20]: https://stackoverflow.com/questions/77470614/why-i-need-set-server-port5000-in-aws-beanstalk-to-make-spring-boot-app-to-work

[^21]: https://superuser.com/questions/451032/how-to-kill-a-process-by-port-on-macos-a-la-fuser-k-9000-tcp


---

## <q>AirPlay Receiver</q> 무슨 기능이야?

AirPlay Receiver는 **Apple의 AirPlay 기술을 이용해 다른 기기(아이폰, 아이패드, 맥 등)에서 내 맥(Mac)으로 화면이나 오디오를 무선으로 전송(미러링)할 수 있게 해주는 기능**입니다[^2][^3][^5].

- AirPlay는 Apple이 개발한 무선 통신 프로토콜로, 사진, 음악, 동영상, 화면 전체 등을 네트워크를 통해 한 기기에서 다른 기기로 전송할 수 있게 해줍니다[^2][^4].
- macOS Monterey(이상)에서는 "AirPlay Receiver" 기능이 추가되어, 내 맥을 **AirPlay 수신기**로 사용할 수 있습니다. 즉, 아이폰이나 아이패드에서 화면 미러링 또는 음악/동영상 재생을 맥으로 바로 전송할 수 있습니다[^5].
- 이 기능은 시스템 환경설정 > 공유 > AirPlay Receiver에서 켜고 끌 수 있습니다[^5][^6].

**예시:**
아이폰에서 "화면 미러링"을 선택하면, 같은 네트워크에 있는 내 맥이 목록에 나타나고, 선택하면 맥 화면에 아이폰 화면이 그대로 나타납니다[^5].

**참고:**
AirPlay Receiver 기능이 켜져 있으면, macOS에서 5000번 포트(commplex-main)를 사용하게 되므로, 개발용 웹서버나 Spring Boot 같은 프로그램이 5000번 포트를 사용하려 할 때 충돌이 발생할 수 있습니다[^6].
이럴 때는 AirPlay Receiver를 꺼야 5000번 포트를 정상적으로 사용할 수 있습니다.

---

**요약:**
AirPlay Receiver는 아이폰, 아이패드, 다른 맥 등에서 내 맥으로 화면/음악/동영상을 무선으로 전송받게 해주는 macOS의 기능입니다.
개발할 때 포트 충돌이 발생하면 시스템 환경설정에서 이 기능을 꺼야 합니다.

<div style="text-align: center">⁂</div>

[^1]: https://pplx-res.cloudinary.com/image/private/user_uploads/uXUlmySYkstGmad/image.jpg

[^2]: https://en.wikipedia.org/wiki/AirPlay

[^3]: https://www.imobie.com/iphone-tips/what-is-airplay.htm

[^4]: https://www.ooberpad.com/blogs/audio-video-tips/how-to-use-airplay-with-av-receiver

[^5]: https://www.youtube.com/watch?v=Az6Bql4afkk

[^6]: https://help.airsquirrels.com/reflector/410-connect-an-iphone-or-ipad-to-reflector-4-on-macos-monterey

[^7]: https://support.apple.com/en-bw/102661

[^8]: https://support.apple.com/en-tm/guide/apple-vision-pro/tanf4ca9ada2/visionos

[^9]: https://www.reddit.com/r/MacOS/comments/tmwhpt/are_you_happy_how_the_airplay_receiver/

[^10]: https://www.apple.com/airplay/

[^11]: https://www.reddit.com/r/MacOS/comments/u8wj5v/whats_the_meaning_behind_of_receiving_requests_to/

[^12]: https://darko.audio/2021/10/turn-a-macbook-into-an-airplay-receiver-w-macos-monterey/

[^13]: https://www.lonelyscreen.com

[^14]: https://forums.macrumors.com/threads/ipados-17-2-beta-airplay-receiver-setting.2409099/

[^15]: https://www.reddit.com/r/VisionPro/comments/18gxr80/you_can_set_your_iphone_to_be_an_airplay_receiver/

[^16]: https://apple.stackexchange.com/questions/446333/airplay-receiver-option-in-sharing-prefs

[^17]: https://www.cnet.com/tech/home-entertainment/should-you-care-about-built-in-airplay-on-your-av-receiver/

[^18]: https://www.airserver.com/PC

[^19]: https://www.airserver.com/Mac


---

## <q>AirPlay Receiver</q>  기능 off 후 다시 재 배포를 해봤지만 결론적으로 또 "502 gateWay" 발생
### 나를 개빡치게 만드는 재주가 있는 문제였다.
#### 왜 또 502가 뜨는 걸까? 
- gpt에게 전체 로그를 전달 후 원인은  **데이터베이스 연결 설정 문제가 주요 원인** 이라고 확인 되었다.
- 그런데  application.yml 현재 datasource 부분을 제거 하고 Elastic beanstalk 구성 환경에 이미 설정을 해준 상태다.
```yaml
spring:
datasource:
url: jdbc:mysql://your-aws-rds-endpoint:3306/your-db
username: ${DB_USERNAME}
```
- 이게 원인이라고? 이렇게 설정을 해 놨는데  DB 설정도
-   | name                                                            | values                                       |
    |-----------------------------------------------------------------|----------------------------------------------|
    | SERVER_PORT                                                     | 5000                                         |
    | JWT_SECRET_KEY                                                  | study-springboot                             |
    | SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID     | <구글 OAuth2에서 발급받은 클라이언트 아이디> |
    | SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET | <구글 OAuth2에서 발급받은 클라이언트 비밀키> |
    | SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_SCOPE         | email,profile                                |


---

## 직접적 원인: 데이터베이스 연결 실패

`/var/log/web.stdout.log`에서 다음과 같은 **중요한 JPA/Hibernate 에러**가 발견됩니다:

```
Caused by: org.hibernate.HibernateException: 
Unable to determine Dialect without JDBC metadata 
(please set 'javax.persistence.jdbc.url', 'hibernate.connection.url', or 'hibernate.dialect')
```

**이 에러는 Spring Boot가 데이터베이스 연결 정보를 찾지 못해 발생**합니다.
→ **H2 데이터베이스가 실제 배포 환경에서 구성되지 않았거나, 프로덕션 DB 연결 설정이 누락**된 상태입니다.

---

## 문제 상세 분석

### 1. **H2 인메모리 DB 한계**

- 로컬 개발 시 `spring.h2.console.enabled=true`로 설정했지만, **Elastic Beanstalk 환경에서는 인메모리 DB가 재시작 시 데이터가 초기화**됩니다.
- 실제 프로덕션 환경에서는 **MySQL, PostgreSQL 등 외부 DB를 사용해야 합니다**.


### 2. **데이터베이스 설정 누락**

- `application.yml`에 아래 설정이 **프로덕션 프로필에 누락**되어 있습니다:

```yaml
spring:
  datasource:
    url: jdbc:mysql://your-db-endpoint/dbname
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
```


### 3. **환경 변수 미설정**

- Elastic Beanstalk 환경 변수에 **DB 연결 정보가 입력되지 않았습니다**:
    - `DB_USERNAME`
    - `DB_PASSWORD`
    - `SPRING_PROFILES_ACTIVE=prod`

---

## 해결 방법

### 1. 프로덕션 DB 설정 추가

`application-prod.yml` 생성 후 다음 내용 추가:

```yaml
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: ${SPRING_DATASOURC
    username: ${SPRING_DATA
    password: ${SPRING_DATA
    driver-class-name: com.
  jpa:
    hibernate:
      ddl-auto: validate
    database-platform: ${SP

---
spring:
  config:
    activate:
      on-profile: default
  h2:
    console:
      enabled: true
  datasource:
    url: jdbc:h2:mem:testdb
```


### 2. Elastic Beanstalk 환경 변수 설정

EB 콘솔 → 환경 → 구성 → 환경 변수에 추가:


### 1. **필수 환경 변수 구성**

| 환경 변수 이름 | 예시 값 | 설명                 |
| :-- | :-- |:-------------------|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://your-rds-endpoint:3306/dbname` | RDS 연결 URL         |
| `SPRING_DATASOURCE_USERNAME` | `admin` | RDS 사용자명           |
| `SPRING_DATASOURCE_PASSWORD` | `your_password` | RDS 비밀번호           |
| `SPRING_JPA_DATABASE_PLATFORM` | `org.hibernate.dialect.MySQL8Dialect` | Hibernate    방언 설정 |
| `SPRING_PROFILES_ACTIVE` | `prod` | 프로덕션 프로필 활성화       |


### 3. 의존성 확인

`build.gradle`에 MySQL Connector 추가:

```gradle
runtimeOnly 'com.mysql:mysql-connector-j'
```


---

## 추가 점검 사항

1. **RDS 보안 그룹 확인**: EC2 인스턴스가 RDS에 접근할 수 있도록 보안 그룹 인바운드 규칙 설정
2. **VPC 피어링 연결**: RDS와 EB가 같은 VPC 내에 있는지 확인
3. **데이터베이스 테이블 생성**: `ddl-auto: update` 설정 시 최초 실행 전 빈 데이터베이스 생성 필요

---

## 결과 예상

- 위 설정 적용 후 재배포하면 **Spring Boot가 정상 기동**되며 502 에러가 사라집니다.
- `/var/log/web.stdout.log`에서 아래 메시지 확인 가능:

```
Tomcat started on port(s): 5000 (http)
Started BlogApplication in 5.234 seconds
```


**요약**: 포트 충돌 문제는 이미 해결되었으며, 현재 502 에러는 **데이터베이스 연결 실패로 인한 애플리케이션 시작 불가** 상태에서 발생합니다. 프로덕션용 DB 설정을 완료해야 합니다.

<div style="text-align: center">⁂</div>

[^1]: https://ppl-ai-file-upload.s3.amazonaws.com/web/direct-files/attachments/64535424/4590ec92-b0e8-496e-b1a9-52e938cc9c8d/paste.txt

## 위에서 제시한 방법 중 Elastic Beanstalk 환경 변수 설정을 사용해보기로 했다.
### 그러던 도중에 나와 동일한 문제를 격은 사람들이  해결한 내용을 알게되었다.
- https://blog.naver.com/na03266/223189598873
- ![Elastic Beanstalk 환경 변수 설정.png](%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%20%ED%95%98%EA%B8%B0%2FElastic%20Beanstalk%20%ED%99%98%EA%B2%BD%20%EB%B3%80%EC%88%98%20%EC%84%A4%EC%A0%95.png)
```java
RDS_HOSTNAME 엔드포인트 숫자 네게와 콜론 지우면 호스트이름!

RDS_PORT 엔드포인트 맨뒤에숫자4개 3306

RDS_DB_NAME ebdb요건 고정입니다.

RDS_USERNAME : DB 수정할 때 적은 ID
RDS_PASSWORD : DB 수정할 때 적은 PW
```

- 결과는 성공!!!
- ![애플리케이션배포하기 05단계.png](%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%20%ED%95%98%EA%B8%B0%2F%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%EB%B0%B0%ED%8F%AC%ED%95%98%EA%B8%B0%2005%EB%8B%A8%EA%B3%84.png)
- ![애플리케이션 배포하기 05단계 2.png](%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%20%ED%95%98%EA%B8%B0%2F%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%B0%B0%ED%8F%AC%ED%95%98%EA%B8%B0%2005%EB%8B%A8%EA%B3%84%202.png)

## 왜 성공한 걸까?

### Elastic Beanstalk에서 502 오류가 해결된 이유

첨부하신 이미지의 환경 변수 설정이 성공한 주된 이유는 **AWS Elastic Beanstalk에 최적화된 환경 변수 네이밍 패턴을 사용**했기 때문입니다.

### 주요 성공 요인

1. **`RDS_` 접두사 환경 변수 사용**
    - `SPRING_DATASOURCE_URL` 대신 `RDS_HOSTNAME`, `RDS_PORT`, `RDS_DB_NAME` 사용[^3][^5]
    - `SPRING_DATASOURCE_USERNAME` 대신 `RDS_USERNAME` 사용
    - `SPRING_DATASOURCE_PASSWORD` 대신 `RDS_PASSWORD` 사용
2. **정확한 포트 구성**
    - `SERVER_PORT=5000` 환경 변수를 명시적으로 설정[^5]
    - application.yml에도 `server.port=${SERVER_PORT:5000}` 설정 유지

### 왜 이렇게 구성하면 작동하는가?

1. **Elastic Beanstalk의 기본 아키텍처**
    - Elastic Beanstalk은 nginx를 리버스 프록시로 사용해 **포트 80 요청을 내부적으로 5000번 포트로 전달**[^5]
    - Spring Boot가 5000번 포트에서 실행되지 않으면 nginx는 연결할 수 없어 502 오류 발생[^2][^6]
2. **Spring Boot의 자동 구성 기능**
    - Spring Boot는 `RDS_` 접두사 환경 변수를 자동으로 인식하여 데이터소스를 구성[^3][^4]
    - 내부적으로 다음과 같은 JDBC URL을 생성:

```
jdbc:mysql://RDS_HOSTNAME:RDS_PORT/RDS_DB_NAME
```

3. **H2 데이터베이스에서 RDS로 전환**
    - 기존 설정은 H2 인메모리 데이터베이스만 구성되어 있어, 프로덕션 환경에서는 실제 DB 연결 정보가 필요[^6]
    - `RDS_` 접두사 환경 변수를 통해 MySQL DB 연결 정보를 제공

### 이전 시도의 문제점

1. **불완전한 환경 변수**
    - `SPRING_DATASOURCE_URL`과 같은 환경 변수만 설정하고 `SERVER_PORT`를 설정하지 않았을 가능성[^5]
    - 또는 이러한 변수를 설정했지만 값이 정확하지 않았을 수 있음
2. **애플리케이션 구성과 환경 불일치**
    - application.yml은 디폴트로 H2 데이터베이스를 사용하도록 구성되어 있으나, 프로덕션에서는 RDS 연결이 필요[^3][^4]

### 결론

AWS Elastic Beanstalk은 `RDS_` 접두사 환경 변수를 통해 Spring Boot 애플리케이션이 자동으로 RDS 데이터베이스에 연결되도록 특별한 통합을 제공합니다. 이 방식을 사용하면 별도의 `application-prod.yml` 파일 없이도 환경 변수만으로 프로덕션 배포를 성공적으로 구성할 수 있습니다[^3][^5].

<div style="text-align: center">⁂</div>

[^1]: https://pplx-res.cloudinary.com/image/private/user_uploads/quDAmcXwobBoFBP/image.jpg

[^2]: https://stackoverflow.com/questions/37755765/spring-boot-application-deployed-on-elastic-beanstalk-java-environment-returns-5

[^3]: https://repost.aws/questions/QURjyf9BsDQpOPl2qh6I8cGg/502-bad-gateway-on-aws-elastic-beanstalk-with-java-spring-boot-app-and-rds-database-postgres

[^4]: https://repost.aws/questions/QUsHmM8LNjSmKoLzL27pM0TQ/troubleshooting-502-bad-gateway-error-in-spring-boot-application-on-elastic-beanstalk

[^5]: https://dream-and-develop.tistory.com/509

[^6]: https://www.reddit.com/r/aws/comments/arku7p/elastic_beanstalk_springboot_502_bad_gateway/

[^7]: https://velog.io/@woojin/AWS-배포하며-502-Bad-Gateway를-만났을-때-Elastic-Beanstalk

[^8]: https://codingapple.com/forums/topic/aws-elastic-beanstalk-502에러/

[^9]: https://stackoverflow.com/questions/63477413/502-bad-gateway-when-i-deploy-springboot-application-to-aws-elastic-beanstalk-an

[^10]: https://blog.ahmadwkhan.com/how-to-deploy-a-django-app-to-aws-elastic-beanstalk

[^11]: https://developer.okta.com/blog/2019/08/07/deploy-a-spring-boot-app-with-aws-elastic-beanstalk

[^12]: https://www.callicoder.com/deploy-host-spring-boot-apps-on-aws-using-elastic-beanstalk/

[^13]: https://www.youtube.com/watch?v=7Ng0iquExjs

[^14]: https://blog.ycrash.io/troubleshooting-http-502-bad-gateway-in-aws-ebs/

[^15]: https://ranidiant.tistory.com/entry/aws-Elastic-Beanstalk-502-Bad-Gatway-해결방법

[^16]: https://java.ihoney.pe.kr/475

[^17]: https://repost.aws/questions/QUjr-AQ_gHR_uQNU971kOyKw/502-bad-gateway-on-a-spring-boot-application-in-elastic-beanstalk

[^18]: https://stackoverflow.com/questions/71544256/after-deployment-to-aws-i-get-502-bad-gateway-error

[^19]: https://www.linkedin.com/pulse/using-elastic-beanstalk-deploy-host-spring-boot-apps-aws-pattiwar

[^20]: https://superuser.com/questions/976066/elasticbeanstalk-auto-scaling-causes-502-bad-gateway

[^21]: https://circleci.com/blog/deploy-spring-boot-aws-elastic-beanstalk/


---
- 여기까지 왔으면 제대로 배포가 된거다. **다만 OAuth 로그인을 시도하면 액세스 거부 에러 메시지가 발생한다.**
- 이제 하나만 더 하면 된다. OAuth 서비스에서 승인된 URL에 일래스틱 빈스토크 서버 URL을 추가하면 된다.
