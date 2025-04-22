## 11장 AWS에 프로젝트 배포하기
### 학습 목표
- AWS 일래스틱 빈스토크(Elastic Beanstalk) 기능을 사용해 실제 서버에 스프링 부트 프로젝트를 배포 한다.

### 사전 지식 : AWS
- AWS 란?
- 지금까지는 내 컴퓨터에서 스프링 부트 서버를 실행 했다. 이를 로컬에서 실행했다고 한다.
- 이렇게 로컬에서만 실행하고 테스트하면 나의 휴대폰이나, 다른 PC에서 접속 할 수 가 없다.
- 만약 다른 PC에서도 이 서비스에 접속하려면 실제 서버에 스프링 부트 서버를 올려 실행 해야 한다. 이런 행위를 실무에서는 배포라고 하는데 , 배포는 **서버용 PC를 구매해서 배포하는 방법**과 AWS와 같은 **클라우드 컴퓨팅 서비스**를 이용해 배포하는 방법이 있다.
- AWS는 컴퓨팅 서비스, 네트워크 서비스 , 데이터베이스 서비스, 스토리지 서비스 등 다양한 서비스를 제공한다.

- 서비스를  AWS에 배포하려면 AWS 상에서 서버를 구성해야 한다. 쉽게 말해 AWS 상의 가상 PC를 마련해야 한다.


- ![aws 프로젝트 배포하기.png](aws%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%20%EB%B0%B0%ED%8F%AC%ED%95%98%EA%B8%B0.png)


- EC2
  - AWS에서 제공하는 클라우드 컴퓨팅 서비스로 가상의 PC 즉, 서버 한 대를  임대하는 서비스 

- 오토 스케일링 그룹 (auto scaling group)
  - 유동적으로 EC2를 관리
  - 사용자의 요청 횟수에 따라 EC2를 늘이거나 줄인다.

- 로드 밸런서 (load balancer)
  - 요청을 분산 시키는 역할
  - 로드 밸런서를 만들 때는 요청을 어디로 분산시킬지 그룹을 정해야한다. 이러한 그룹을 대상 그룹(Target Group, TG) 이라고 한다.
  
- RDS( amazon relational database service)
  - 범용 데이터베이스인 아마존 관계형 데이터베이스(amazon relational database service)
  - 데이터베이스도 클라우드에 올려야 한다.
  - AWS에서 제공하는 클라우드 데이터 서비스는 RDS, Redshift, DocumentDB, ElastiCache 등이 있다.

### 1) 일래스틱 빈스토크
- 개발자가 애플리케이션을 쉽게 배포하고 확장할 수 있도록 해주는 Pass(Platform as a Service) 이다.
- 개발자가 애프리케이션 코드를 업로드하면, Elastic Beanstalk는 배포, 프로비저닝, 로드 밸런싱, 확장 , 모니터링 등을 자동으로 처리해준다.

### Elastic Beanstalk 을 사용하는 과정
- ![버전 흐름.png](%EB%B2%84%EC%A0%84%20%ED%9D%90%EB%A6%84.png)

- 흐름도 
  1. 애플리케이션을 생성
  2. 애플리케이션의 소스를 번들 형태, 예를 들어 java.war 파일로 애플리케이션 버전을 일래스톡 빈스토크에 업로드 한다.
  3. 그 뒤에 일래스틱 빈스토크가 자동으로 환경을 실행하고 코드 실행에 필요한 AWS 리소스를 생성하고 구성한다.
  4. 환경 실행 후에는 환경을 직접 관리하고 필요한 경우 버전 업데이트를 하거나 새로운 앱 버번을 배포한 수 있다.

## AWS 계정 생성하기
- 원래는 처음 이용한 클라이언트에게 계정 생성 후 1년동안 프리티어 자걱을 준다. 
- 프리티어는 서비스별로 지정된 한도 내에서 무료로 AWS 서비스를 사용할 수 있는 혜택이다.

- 나는 이미 계정이 사용하고 정지한 이력이 있어 유로라도 진행을 하고 싶었지만 문제가 발생했는데 , AWS 계정 정책으로 기존에 사용하던 구글 계정은 사용을 못하는 것 같다.
- ![프리티어.png](%ED%94%84%EB%A6%AC%ED%8B%B0%EC%96%B4.png)![프리티어 무려 계정생성.png](%ED%94%84%EB%A6%AC%ED%8B%B0%EC%96%B4%20%EB%AC%B4%EB%A0%A4%20%EA%B3%84%EC%A0%95%EC%83%9D%EC%84%B1.png)
- 계정 생성을 눌러 로그인 콘솔로 계정을 찾고 싶었지만 계속 해서 실패 하였다. 이미 지정한 이메일 있다고 해서 [내 계정에 로그인] 누르면 다시 로그인 화면으로 가지고 또 이메일을 인증하고 하는데 밑에 이미지처럼 반복된다.
- ![문제 사항 발생.png](%EB%AC%B8%EC%A0%9C%20%EC%82%AC%ED%95%AD%20%EB%B0%9C%EC%83%9D.png)

### 안되는 이유는 
- 계정을 해지(폐쇄)한 뒤 **90일이 지나면 해당 이메일로는 새 계정 생성이 불가** 하며, 기존 계정도 복구할 수 없다고 한다.
- 우선 내일 계속 조금 더 찾아보다가 안되면 새로운 이메일을 만들어서 진행을 할 예정이다.

<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" class="logo" width="120"/>

## 상황 요약

이미지와 설명에 따르면, 루트 계정으로 로그인하고 이메일 인증을 시도해도 "지정한 이메일이 이미 AWS 계정과 연결되어 있습니다"라는 메시지가 반복적으로 나오고, 정상적으로 계정 사용이 불가한 상황입니다. 이런 현상은 다음과 같은 경우에 주로 발생합니다:

- 해당 이메일로 이미 AWS 계정이 생성되어 있음
- 계정이 일시 중지(정지)되었거나 폐쇄됨
- 이메일 인증이 정상적으로 완료되지 않음
- 계정 활성화 과정에서 오류 발생


## 주요 원인 및 확인 사항

- **이미 계정이 존재**: AWS는 하나의 이메일 주소로 하나의 루트 계정만 생성할 수 있습니다. 만약 해당 이메일로 이미 계정이 있다면, 새 계정 생성이나 인증이 반복적으로 실패할 수 있습니다[^2][^5].
- **계정이 정지/폐쇄 상태**: 계정이 폐쇄된 뒤 90일이 지나면 동일 이메일로 새 계정 생성이 불가합니다. 정지된 계정은 복구 절차를 거쳐야만 다시 사용할 수 있습니다[^2].
- **이메일 인증 미완료/오류**: 인증 메일을 받았더라도, 링크 클릭 시 오류가 반복된다면 AWS 시스템 내부 오류나 계정 활성화 미완료 상태일 수 있습니다[^6][^4].
- **브라우저/네트워크 문제**: 쿠키, 캐시, VPN, 브라우저 문제로 인해 인증이 제대로 진행되지 않을 수 있습니다. 여러 브라우저/기기에서 시도해보는 것도 방법입니다[^6].


## 해결 방법

**1. 루트 계정 로그인/비밀번호 재설정**

- AWS 루트 계정 로그인 페이지에서 "비밀번호 재설정(Forget password?)"을 시도해보세요. 인증 메일이 정상적으로 도착하는지 확인합니다[^2][^5].
- 인증 메일이 도착하지 않거나, 링크 클릭 시 오류가 난다면 브라우저 캐시/쿠키 삭제, 다른 브라우저/기기/네트워크(모바일 등)에서 시도해 보세요[^6].

**2. 계정 활성화 상태 확인**

- 계정이 "활성화"되지 않은 상태라면 로그인 및 인증이 정상적으로 되지 않습니다. AWS에서 계정 활성화가 완료되었는지 확인하세요[^6].

**3. 계정 정지/폐쇄 여부 확인 및 복구**

- 계정이 정지(일시 중지)된 경우, AWS 지원센터에서 복구 요청을 해야 합니다[^2][^4].
- 계정이 폐쇄된 후 90일이 지났다면 동일 이메일로는 새 계정 생성이 불가합니다. 이 경우, 이메일 주소에 +tag(예: email+aws@domain.com) 형식으로 새로운 계정을 만들어야 합니다[^2].

**4. AWS 지원센터 문의**

- 위 방법으로도 해결되지 않으면, AWS 지원센터(Account and Billing)로 문의를 남겨야 합니다.
    - 이미 로그인이 안 된다면, 다른 이메일로 새 AWS 계정을 만든 뒤 해당 계정으로 지원 요청을 해야 합니다[^4][^6].
    - 지원 요청 시, 현재 상황(이메일 인증 반복, 계정 활성화 불가 등)을 상세히 설명해야 합니다.

**5. 조직(Organizations) 계정 여부 확인**

- 만약 계정이 AWS Organizations의 멤버 계정이라면, 루트 계정으로 직접 로그인/복구가 제한될 수 있습니다. 이 경우 조직의 관리자를 통해 계정 복구를 요청해야 합니다[^8][^5].


## 요약 표

| 원인/상황 | 해결 방법 요약 |
| :-- | :-- |
| 기존 계정 존재 | 기존 계정 로그인/비밀번호 재설정 |
| 계정 정지/폐쇄 | AWS 지원센터에 복구 요청 |
| 이메일 인증 오류 | 브라우저 변경, 캐시 삭제, 다른 네트워크 시도 |
| 계정 활성화 미완료 | AWS 지원센터 문의, 활성화 상태 확인 |
| 조직 멤버 계정 | 조직 관리자에게 문의 |
| 동일 이메일 새 계정 생성 | +tag 이메일로 새 계정 생성 |

## 추가 조치

- **지원센터 문의 시 참고 링크**
    - [AWS 계정 지원](https://support.aws.amazon.com/#/contacts/aws-account-support/)
    - [로그인 문제 해결 가이드](https://docs.aws.amazon.com/ko_kr/signin/latest/userguide/troubleshooting-sign-in-issues.html)


## 결론

현재 상황은 AWS 계정 활성화/인증 단계에서 시스템적으로 막혀 있거나, 계정이 정지/폐쇄된 상태일 가능성이 높습니다. 위의 방법을 모두 시도해도 해결되지 않으면, 반드시 AWS 지원센터에 계정 관련 문의를 남기고, 필요하다면 새로운 이메일로 임시 계정을 만들어 지원 요청을 진행해야 합니다[^4][^6][^2].

<div style="text-align: center">⁂</div>

[^1]: https://pplx-res.cloudinary.com/image/private/user_uploads/vhhqwyPtepmmhUu/seukeurinsyas-2025-04-22-ohu-9.08.14.jpg

[^2]: https://docs.aws.amazon.com/signin/latest/userguide/troubleshooting-sign-in-issues.html

[^3]: https://repost.aws/ko/questions/QUeAJqjUtvQNCTHoI9DchDbA/aws-루트-계정-로그인이-안됩니다

[^4]: https://repost.aws/questions/QURIkuWfftQYKuiygn-cilHQ/aws-root-email-verification-failed

[^5]: https://docs.aws.amazon.com/ko_kr/signin/latest/userguide/troubleshooting-sign-in-issues.html

[^6]: https://www.reddit.com/r/aws/comments/1i5t2f9/cant_log_in_to_aws_root_account_we_experienced_an/

[^7]: https://repost.aws/questions/QUwy2I52m0QL6caj3iH0BXkQ/issue-with-changed-root-account-email

[^8]: https://docs.aws.amazon.com/IAM/latest/UserGuide/troubleshooting_root-user.html

[^9]: https://docs.aws.amazon.com/ko_kr/IAM/latest/UserGuide/id_root-user.html

[^10]: https://docs.aws.amazon.com/accounts/latest/reference/manage-acct-update-root-user-email.html

[^11]: https://repost.aws/questions/QUuzj8jVMqR2KYnBuLeRNAtA/my-root-account-is-not-reciving-email-verification-code-email

[^12]: https://docs.aws.amazon.com/ko_kr/accounts/latest/reference/troubleshooting_close-account.html

[^13]: https://eccouncil.zendesk.com/hc/en-us/articles/35711904702477-AWS-Sign-In-Account-does-not-exist

[^14]: https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_mfa_lost-or-broken.html

[^15]: https://docs.aws.amazon.com/ko_kr/IAM/latest/UserGuide/troubleshooting_root-user.html

[^16]: https://docs.aws.amazon.com/ko_kr/IAM/latest/UserGuide/id_credentials_passwords_account-policy.html

[^17]: https://docs.aws.amazon.com/IAM/latest/UserGuide/id_root-user.html

[^18]: https://docs.aws.amazon.com/IAM/latest/UserGuide/root-user-best-practices.html

[^19]: https://docs.aws.amazon.com/ko_kr/IAM/latest/UserGuide/root-user-best-practices.html

[^20]: https://ghdwlsgur.github.io/docs/AWS-Workshop/Protect_Root

[^21]: https://www.reddit.com/r/aws/comments/1ah4rtt/root_console_account_blocked_unable_to_access/

