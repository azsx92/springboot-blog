## 11.4.2 OAuth 서비스에 승인된 URI 추가하기
### 01 단계
- 구글 클라우드 콘솔에 접속한 후 [API 및 서비스 -> 사용자 인증 정보 -> OAuth2.0 클라이언트 ID]에 추가되어 있는 클라이어트 ID를 클릭한다.
- 그런 다음 [OAuth 클라이언트 수정 -> 승인된 리다이렉트 URI] 에 일래스틱 빈스토크에서 띄어준 서버의 URL을 추가한다.
- `승인된 리다이렉션 URI에는 다음과 같이 /login/oauth2/code/google을 붙여야 한다.`
- 승인된 리디렉션 입력 예
> http://........생략....elasticbeanstalk.com **/login/oauth2/code/google**
- ![oauth 서비스에 승인되 URI 추가 01 단계 .png](oauth%20%EC%84%9C%EB%B9%84%EC%8A%A4%EC%97%90%20%EC%8A%B9%EC%9D%B8%EB%90%9C%20URL%20%EC%B6%94%EA%B0%80%ED%95%98%EA%B8%B0%2Foauth%20%EC%84%9C%EB%B9%84%EC%8A%A4%EC%97%90%20%EC%8A%B9%EC%9D%B8%EB%90%98%20URI%20%EC%B6%94%EA%B0%80%2001%20%EB%8B%A8%EA%B3%84%20.png)
- ![oauth 서비스에 승인되 URI 추가 01 단계 2.png](oauth%20%EC%84%9C%EB%B9%84%EC%8A%A4%EC%97%90%20%EC%8A%B9%EC%9D%B8%EB%90%9C%20URL%20%EC%B6%94%EA%B0%80%ED%95%98%EA%B8%B0%2Foauth%20%EC%84%9C%EB%B9%84%EC%8A%A4%EC%97%90%20%EC%8A%B9%EC%9D%B8%EB%90%98%20URI%20%EC%B6%94%EA%B0%80%2001%20%EB%8B%A8%EA%B3%84%202.png)

### 02 단계
- 적용까지 시간이 조금 필요하다. 3분 정도 기다린 후 /login 페이제이서 로그인을 시도 한다. 
- 구글 로그인 화면이 제대로 나오는 것을 확인 할 수 있다.
- ![승인된 리디렉션 URI 02 단계.png](oauth%20%EC%84%9C%EB%B9%84%EC%8A%A4%EC%97%90%20%EC%8A%B9%EC%9D%B8%EB%90%9C%20URL%20%EC%B6%94%EA%B0%80%ED%95%98%EA%B8%B0%2F%EC%8A%B9%EC%9D%B8%EB%90%9C%20%EB%A6%AC%EB%94%94%EB%A0%89%EC%85%98%20URI%2002%20%EB%8B%A8%EA%B3%84.png)
- ![승인된 리디렉션 URI 02 단계 2.png](oauth%20%EC%84%9C%EB%B9%84%EC%8A%A4%EC%97%90%20%EC%8A%B9%EC%9D%B8%EB%90%9C%20URL%20%EC%B6%94%EA%B0%80%ED%95%98%EA%B8%B0%2F%EC%8A%B9%EC%9D%B8%EB%90%9C%20%EB%A6%AC%EB%94%94%EB%A0%89%EC%85%98%20URI%2002%20%EB%8B%A8%EA%B3%84%202.png)
- ![승인된 리디렉션 URI 02 단계 3.png](oauth%20%EC%84%9C%EB%B9%84%EC%8A%A4%EC%97%90%20%EC%8A%B9%EC%9D%B8%EB%90%9C%20URL%20%EC%B6%94%EA%B0%80%ED%95%98%EA%B8%B0%2F%EC%8A%B9%EC%9D%B8%EB%90%9C%20%EB%A6%AC%EB%94%94%EB%A0%89%EC%85%98%20URI%2002%20%EB%8B%A8%EA%B3%84%203.png)

---
- 로그인 되지 않았던 이유는 환경설정이 제대로 되지 매핑 설정을 하지 못했다...


## 401 invalid_client 오류 원인 분석 및 해결 방법

### 1. **환경 변수명 오타 및 잘림 문제**

이미지에서 설정한 환경 변수명을 보면 아래와 같이 **중간에 잘리거나 오타가 있습니다**:

- `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_G`
- `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_G`
- `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_G`

이는 실제로는 각각

- `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID`
- `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET`
- `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_SCOPE`
  여야 합니다.

**Elastic Beanstalk 환경 변수 입력란이 길어서 잘려 보이지만, 실제로도 잘려 저장된 경우 Spring Boot가 값을 읽지 못합니다.**
이 경우 Spring Boot는 `${SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID}` 값을 못 읽어와서,
Google에 요청할 때 "client_id" 필드가 비어 있거나, 잘못된 값이 들어가 **401 invalid_client** 오류가 발생합니다[^4][^5][^7][^9][^10].

---

### 2. **정확한 환경 변수명 사용법**

아래와 같이 **전체 이름을 정확히 입력**해야 합니다(복사/붙여넣기 권장):

- `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID`
- `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET`
- `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_SCOPE`

**Scope는 보통 환경 변수로 둘 필요 없이 yml에 직접 적어도 무방합니다.**

---

### 3. **구글 클라우드 콘솔 설정 점검**

- **승인된 리디렉션 URI**가 정확히 일치해야 하며,
  반드시 실제 배포 URL(예: `http://springboot-developer-env-1.eba-bxpjcmt3.ap-northeast-2.elasticbeanstalk.com/login/oauth2/code/google`)이 등록되어야 합니다[^3].
- `http`와 `https` 구분도 엄격하게 적용됩니다.
  (현재는 http로 등록되어 있지만, 추후 https로 배포하면 https로 추가 필요)

---

### 4. **추가 체크리스트**

- 환경 변수 값에 **공백, 따옴표, 줄바꿈**이 포함되어 있지 않은지 확인하세요[^6].
- Google Cloud 콘솔에서 **클라이언트 ID/시크릿이 삭제되거나 비활성화되지 않았는지** 확인하세요.
- **클라이언트 ID/시크릿이 올바른지** 복사/붙여넣기로 다시 한 번 점검하세요.

---

## **정리: 해결 방법**

1. **환경 변수명 전체를 정확히 입력**
    - EB 환경 변수 입력란에 아래와 같이 전체 이름을 정확히 입력:
        - `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID`
        - `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET`
        - (필요하다면) `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_SCOPE`
2. **Google Cloud Console의 승인된 리디렉션 URI**가 실제 배포 URL과 완전히 일치하는지 확인
3. 입력값에 오타, 공백, 줄바꿈, 따옴표 등이 포함되어 있지 않은지 점검

---

> **환경 변수명이 잘리거나 오타가 있으면 Spring Boot가 값을 읽지 못해 401 invalid_client 오류가 발생합니다.
> 반드시 전체 이름을 정확히 입력해야 하며, 값도 정확히 복사해 입력해야 합니다.**

---

**참고:**
이 문제는 거의 대부분 환경 변수명 오타/잘림, 값 오타, 리디렉션 URI 불일치에서 발생합니다[^4][^5][^6][^7][^9][^10].
환경 변수명과 값을 다시 한 번 꼼꼼히 확인하세요!

<div style="text-align: center">⁂</div>

[^1]: https://pplx-res.cloudinary.com/image/private/user_uploads/yDmnZvHrNyYtqOQ/image.jpg

[^2]: https://pplx-res.cloudinary.com/image/private/user_uploads/xpUUEdWYMilvUzU/image.jpg

[^3]: https://stackoverflow.com/questions/71957784/google-oauthn-error-400-redirect-uri-mismatch-after-deploy-my-application-on-ela

[^4]: https://sendlayer.com/docs/401-error-invalid-client/

[^5]: https://stackoverflow.com/questions/58323684/how-can-i-fix-the-google-oauth-401-error-invalid-client

[^6]: https://stackoverflow.com/questions/17166848/invalid-client-in-google-oauth2

[^7]: https://stackoverflow.com/questions/51179066/google-oauth2-error-401-invalid-client

[^8]: https://stackoverflow.com/questions/32205824/401-that-s-an-error-error-invalid-client-the-oauth-client-was-not-found

[^9]: https://dive-into-frontend.tistory.com/178

[^10]: https://blog.postman.com/how-to-access-google-apis-using-oauth-in-postman/

[^11]: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/create_deploy_docker.container.console.html

[^12]: https://www.reddit.com/r/aws/comments/15a84tv/how_do_i_work_around_elastic_beanstalks_4kb/

[^13]: https://repost.aws/questions/QU2Wd-HZ1VTtmV892zXKVvAw/environment-variables-in-elastic-beanstalk-not-updating-after-cloning-an-environment

[^14]: https://memo-the-day.tistory.com/159

[^15]: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/

[^16]: https://developer-nyong.tistory.com/60

[^17]: https://stackoverflow.com/questions/71704132/how-to-provide-spring-boot-application-yml-property-values-coming-from-files

[^18]: https://velog.io/@jshong0907/Spring-Boot-OAuth2-1

[^19]: https://d0.awsstatic.com/whitepapers/deploying-wordpress-with-aws-elastic-beanstalk.pdf

[^20]: https://velog.io/@discphy/SNS-로그인-Spring-OAuth2-Client

[^21]: https://bono039.tistory.com/1126

[^22]: https://godekdls.github.io/Spring Security/oauth2/

[^23]: https://yelimkim98.tistory.com/45

[^24]: https://velog.io/@summer_today/11.4-Elastic-Beanstalk%EC%97%90-%EC%84%9C%EB%B9%84%EC%8A%A4-%EB%B0%B0%ED%8F%AC%ED%95%98%EA%B8%B0

[^25]: https://github.com/micronaut-projects/micronaut-security/issues/480

[^26]: https://dive-into-frontend.tistory.com/178

[^27]: https://velog.io/@maintain0404/Google-OpenIDOauth-로그인-구현하기-with-django

[^28]: https://cloud.google.com/iap/docs/custom-oauth-configuration

[^29]: https://forums.online-go.com/t/401-invalid-client-error/45270

[^30]: https://velog.io/@readnthink/OAuth-구글-로그인-에러-엑세스-차단-401-오류-invalidclient

[^31]: https://docs.aws.amazon.com/elasticloadbalancing/latest/application/listener-authenticate-users.html

[^32]: https://www.reddit.com/r/aws/comments/13cs8p7/aws_elastic_beanstalk_with_google_signin/

[^33]: https://cloud.google.com/apigee/docs/api-platform/reference/policies/oauth-http-status-code-reference

[^34]: https://support.google.com/accounts/answer/12917337

[^35]: https://velog.io/@naninaniyoyoyoyo/Spring-Boot-구글-로그인-3-401-error엑세스-차단

[^36]: https://devbattery.com/project/foodymoody-solution-5/

[^37]: https://velog.io/@naninaniyoyoyoyo/Spring-Boot-구글-로그인-4-400-Error-Invalidrequest

[^38]: https://github.com/spring-projects/spring-security-oauth/issues/1035

[^39]: https://github.com/spring-projects/spring-security/issues/6922

[^40]: https://github.com/microsoft/azure-spring-boot/issues/526

[^41]: https://tech.kakaopay.com/post/spring-oauth2-authorization-server-practice/

[^42]: https://devforum.okta.com/t/the-github-java-sample-fails-with-a-401-while-obtaining-a-token/13083

[^43]: https://traeper.tistory.com/223

[^44]: https://docs.spring.io/spring-security/site/docs/5.0.13.RELEASE/api/index.html?org%2Fspringframework%2Fsecurity%2Foauth2%2Fcore%2FOAuth2ErrorCodes.html

[^45]: https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-elasticbeanstalk-environment.html

[^46]: https://stackoverflow.com/questions/11211007/how-do-you-pass-custom-environment-variable-on-amazon-elastic-beanstalk-aws-ebs

[^47]: https://repost.aws/questions/QU9EWFFjMPSLSrqbUUYM4D4g/environment-specific-option-setings-for-beanstalk

[^48]: https://docs.spring.io/spring-security/site/docs/5.2.12.RELEASE/reference/html/oauth2.html

[^49]: https://jun10920.tistory.com/49

[^50]: https://stackoverflow.com/questions/77629519/for-springboot-application-it-is-good-idea-to-store-client-secret-in-applicatio

[^51]: https://docs.spring.io/spring-security/reference/servlet/oauth2/login/core.html

[^52]: https://velog.io/@khhkmg0205/Spring-Boot-11.-AWS에-프로젝트-배포하기

[^53]: https://dingdingmin-back-end-developer.tistory.com/entry/SpringBoot-스프링부트-Spring-Security-Oauth2-6-Google-Token-활용

[^54]: https://developers.google.com/identity/protocols/oauth2/web-server

[^55]: https://developers.google.com/workspace/guides/configure-oauth-consent

[^56]: https://www.googlecloudcommunity.com/gc/Community-Hub/Configure-Consent-screen/m-p/851695

[^57]: https://stackoverflow.com/questions/63126365/oauth2-0-token-post-request-is-responding-401-invalid-client

[^58]: https://connect2id.com/products/server/docs/guides/oauth-client-authentication

[^59]: https://docs.spring.io/spring-security/reference/api/java/org/springframework/security/oauth2/core/OAuth2ErrorCodes.html

[^60]: https://stackoverflow.com/questions/74989945/spring-authorization-server-1-0-0-invalid-client-error-while-requesting-oauth2

