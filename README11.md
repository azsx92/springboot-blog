## 11.3.3 로컬에서 RDS 연결하기
- 지금까지 로컬에서 사용하던 H2 대신 AWS의 RDS를 사용할 예정이다. RDS는 H2와 달리 데이터를 콘솔에서 조회할 수 없어서 데이터베이스 도구를 사용해야 한다.
- 그 방법을 알아 보자
### 01 단계
- 다시 일래스틱 빈스토크 구성에서 데이터베이스 영역의 엔드포인트를 눌러 데이터베이스 관리 페이지로 이동한다.
- ![일레스틱 rds 생성 03단계 3.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20RDS%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20rds%20%EC%83%9D%EC%84%B1%2003%EB%8B%A8%EA%B3%84%203.png)
- 그런데 책 과는 달리 엔드포인트를 눌러도 이동이 되지 않았다. 이유를 찾아보니

# 왜 RDS의 엔드포인트를 누르면 관리페이지로 이동이 안되고 제목없음 about이 뜰까?

이미지와 검색 결과를 종합하면,
**RDS 엔드포인트를 클릭했을 때 AWS 관리 콘솔의 RDS 관리페이지로 이동하지 않고 "제목없음 about" 같은 빈 페이지가 뜨는 현상**은 아래와 같은 원인 때문입니다.

---

## 원인 분석

### 1. **RDS 콘솔 메뉴 구조 및 UI 변경**

- AWS 콘솔의 메뉴가 최근 "Aurora and RDS"로 통합되어 표기되고 있습니다[^2].
- 기존의 "RDS - Relational Database Service" 메뉴가 보이지 않고,
  "Aurora and RDS"로만 접근이 가능한 상황입니다.
- 메뉴 구조가 바뀌면서, Elastic Beanstalk 환경에서 제공되는 RDS 엔드포인트 링크가
  **더 이상 RDS 인스턴스 상세 페이지로 직접 연결되지 않을 수 있습니다**.


### 2. **권한 또는 서비스 활성화 문제**

- 계정의 서비스 활성화 내역(세 번째 이미지)에서
  "관계형 데이터베이스(RDS)"가 **비활성화**로 표시되어 있습니다.
- RDS 서비스가 계정에서 완전히 활성화되지 않은 경우,
  엔드포인트 링크가 정상적으로 동작하지 않고 빈 페이지나 "about:blank"로 이동할 수 있습니다.


### 3. **리전(Region) 불일치**

- RDS 인스턴스가 생성된 리전과 현재 콘솔의 리전이 다르면
  링크가 올바른 RDS 인스턴스 상세 페이지로 이동하지 못하고,
  빈 페이지로 연결될 수 있습니다[^6][^8].


### 4. **브라우저/캐시 문제**

- AWS 콘솔의 UI가 업데이트되면서,
  브라우저 캐시나 세션 문제로 인해 링크가 제대로 작동하지 않을 수 있습니다.

---

## 해결 방법

### 1. **RDS 서비스 활성화 확인**

- **계정의 서비스 활성화 내역**에서 "관계형 데이터베이스(RDS)"가 비활성화로 표시되어 있다면,
  먼저 RDS 서비스를 활성화해야 합니다.
  - AWS 콘솔 > 계정 관리 > 서비스 활성화에서 "관계형 데이터베이스(RDS)"를 활성화하세요.
  - 서비스가 활성화되면, "Aurora and RDS" 메뉴를 통해 RDS 콘솔에 정상 접근할 수 있습니다.


### 2. **리전 일치 확인**

- AWS 콘솔 오른쪽 상단에서 **Elastic Beanstalk 환경과 동일한 리전(ap-northeast-2, 서울)**이 선택되어 있는지 확인하세요.
- RDS 인스턴스가 생성된 리전과 콘솔의 리전이 다르면,
  RDS 콘솔에서 인스턴스가 보이지 않거나 링크가 올바르게 동작하지 않을 수 있습니다[^6][^8].


### 3. **RDS 콘솔 직접 진입**

- 상단 검색창에 "Aurora and RDS"를 입력하여 RDS 콘솔로 직접 이동하세요.
- 또는, 아래 URL을 직접 입력해서 진입할 수 있습니다(서울 리전 기준):

```
https://ap-northeast-2.console.aws.amazon.com/rds/home?region=ap-northeast-2
```

- RDS 콘솔 > "데이터베이스" 메뉴에서 인스턴스를 직접 클릭해 상세 정보를 확인하세요.


### 4. **브라우저 캐시/세션 초기화**

- 캐시/쿠키 삭제, 시크릿 모드, 다른 브라우저에서 시도해 보세요.

---

## 요약

- **RDS 서비스가 계정에서 비활성화되어 있으면, 엔드포인트 링크가 정상적으로 동작하지 않고 빈 페이지가 뜹니다.**
- **서비스 활성화 후, "Aurora and RDS" 메뉴 또는 직접 URL로 RDS 콘솔에 진입**해야 합니다.
- **리전이 일치하는지** 확인하고,
  **브라우저 문제**도 점검하세요.

---

**정리:**
RDS 서비스가 계정에서 비활성화되어 있거나, 메뉴 구조 변경/리전 불일치/브라우저 문제 등으로
엔드포인트 링크가 관리 페이지로 이동하지 않고 빈 페이지가 뜨는 현상이 발생합니다.
**계정에서 RDS 서비스를 활성화**하고,
**리전을 일치**시킨 뒤,
**"Aurora and RDS" 메뉴 또는 직접 URL로 RDS 콘솔에 진입**하면 정상적으로 관리 페이지를 확인할 수 있습니다[^6][^8].

<div style="text-align: center">⁂</div>

[^1]: https://pplx-res.cloudinary.com/image/private/user_uploads/tLbFAOXGXCOplUD/screencapture-ap-northeast-2-console-aws-amazon-elasticbeanstalk-home-2025-04-24-20_57_52.jpg

[^2]: https://pplx-res.cloudinary.com/image/private/user_uploads/RSBoBkTRBPeLQeV/image.jpg

[^3]: https://pplx-res.cloudinary.com/image/private/user_uploads/sGswrbUjCqAluRJ/screencapture-us-east-1-console-aws-amazon-billing-home-2025-04-24-21_03_22.jpg

[^4]: https://www.reddit.com/r/msp/comments/xhahxs/rds_2022_random_blank_login_screen/

[^5]: https://stackoverflow.com/questions/58931286/redirect-old-rds-traffic-to-the-new-rds-in-aws

[^6]: https://docs.aws.amazon.com/AmazonRDS/latest/gettingstartedguide/connecting-console.html

[^7]: https://serverfault.com/questions/263260/why-cant-i-connect-to-amazon-rds-after-setting-it-up

[^8]: https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_Endpoint.html

[^9]: https://stackoverflow.com/questions/37212945/aws-cant-connect-to-rds-database-from-my-machine

[^10]: https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_Troubleshooting.html

[^11]: https://repost.aws/questions/QU_7D2xmEkR8uu6xbh_Xcmlg/unable-to-connect-to-new-rds-instance-timeout

[^12]: https://learn.microsoft.com/en-au/answers/questions/204147/windows-server-2019-rds-start-search-does-not-work?page=3

[^13]: https://stackoverflow.com/questions/37555670/aws-lambda-cant-connect-to-rds-instance-but-i-can-locally

[^14]: https://serverfault.com/questions/963866/unable-to-connect-to-aws-rds-using-endpoint

[^15]: https://learn.microsoft.com/en-us/answers/questions/204147/windows-server-2019-rds-start-search-does-not-work?page=3

[^16]: https://repost.aws/knowledge-center/rds-cannot-connect

[^17]: https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/aurora-endpoint-viewing.html

[^18]: https://repost.aws/questions/QUYd6sBSRaTYWvQpTb-OEFpw/can-we-edit-modify-the-end-point-url-after-rds-database-creation

[^19]: https://www.reddit.com/r/rstats/comments/tykkqw/hard_time_connecting_to_an_aws_rds_database/

[^20]: https://repost.aws/knowledge-center/rds-ip-address-issues

[^21]: https://support.microsoft.com/en-us/topic/blank-page-is-displayed-when-you-try-to-access-remoteapps-on-a-windows-based-rd-web-access-server-d0870118-847b-0f05-a8a9-2da29e544751

[^22]: https://repost.aws/questions/QUnmqVd0n8SEmGMbQlSVGP9A/restor-rds-from-snapshot-no-data

[^23]: https://knowledge.broadcom.com/external/article/275236/aws-console-blank-on-rds.html

### 02 단계
- 여기에서 해야 하는 일은 로컬에서 데이터베이스에 접근 하도록 로컬의 아이피를 허용하는 거다.
- 우선 데이터베이스 항목의 DB  식별자에 있는 링크를 눌러 이동하고 , [연결 & 보안] 탭의 보안 그룹에 있는  VPC 보안 그룹 하이퍼링크를 클릭한다.
- ![로컬에서 rds 연결 02 단계.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20RDS%20%EC%97%B0%EA%B2%B0%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EB%A1%9C%EC%BB%AC%EC%97%90%EC%84%9C%20rds%20%EC%97%B0%EA%B2%B0%2002%20%EB%8B%A8%EA%B3%84.png)
- ![로컬에서 rds 연결 02 단계 2.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20RDS%20%EC%97%B0%EA%B2%B0%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EB%A1%9C%EC%BB%AC%EC%97%90%EC%84%9C%20rds%20%EC%97%B0%EA%B2%B0%2002%20%EB%8B%A8%EA%B3%84%202.png)

### 03 단계 
- 아래에 보이는 인바운드 규칙 탭을 눌러 이동한 뒤 [인바운드 규칙 편집] 버튼을 클릭한다.
- 인바운드 편집 메뉴에서는 데이터베이스 인스턴스의 트래픽을 관리할 수 있다.
- ![로컬에서 연결하기 03단계.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20RDS%20%EC%97%B0%EA%B2%B0%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EB%A1%9C%EC%BB%AC%EC%97%90%EC%84%9C%20%EC%97%B0%EA%B2%B0%ED%95%98%EA%B8%B0%2003%EB%8B%A8%EA%B3%84.png)

### 04 단계
- 기본값을 수정한다. 유형은 [MYSQL/Aurora] , 소스는 [내 IP]를 선택하고 [저장]을 클릭한다.
- 그러면 로컬에서 일래스틱 빈스토크 데이터베이스에 접근 할 수가 있다.
- 