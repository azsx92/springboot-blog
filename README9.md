## 11.3 일래스틱 빈스토크로 서버 구축 하기
- 이미 계정 생성을 완료 했으니 일래스틱 빈스토크 서버를 구축한다. 

### 11.3.1 일래스틱 빈스토크 서비스 생성하기
#### 01 단계 
- AWS 사이트에 로그인한 다음 앞으로 우리가 만들 서버의 제공 위치를 지정한다.
- 우리가 살고 있는 위치에서 가까울수록 응답 속도 등이 빠르므로 지역을 서울로 설정한다.
- ![일래스틱 빈스톡 서비스 생성하기 01단계.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%9E%98%EC%8A%A4%ED%8B%B1%20%EB%B9%88%EC%8A%A4%ED%86%A1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%2001%EB%8B%A8%EA%B3%84.png)

#### 02 단계
- 검색 창에서 Elastic Beanstalk를 검색해 일래스틱 빈스트크 서비스로 들어간다. 그런 다음 [Create Application] 버튼을 눌러 서비스를 생성한다.
- ![일레스틱 서비스 생성 02 단계 1.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%2002%20%EB%8B%A8%EA%B3%84%201.png)
- ![일레스틱 서비스 생성 02단계 02.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%2002%EB%8B%A8%EA%B3%84%2002.png)

#### 03 단계
- 애플리케이션 이름을 입력하고 플랫폼으로 [Java Corretto 17]을 선택한다. 애플리케이션 코드에는 '샘플 애플리케이션'을 선택한 뒤 [애플리케이션 생성] 버튼을 선택한다.
- ![일레스틱 서비스 생성하기 03 단계.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%2003%20%EB%8B%A8%EA%B3%84.png)
- [다음] 버튼을 누르면 여러 단계가 있는데 [검토 단계로 건너뛰기] 버튼을 누르고 나서 [제출] 버튼을 눌러 환경을 구성한다.
- 나는 참고로 **2단계 서비스 액세스 구성**이 중요한 하다는 걸 04단계에서 알게 되었다.
- ![일레스틱 서비스 생성하기 03단계 02 .png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%2003%EB%8B%A8%EA%B3%84%2002%20.png)


#### 04 단계
- 생성 요청한 웹 앱을 사용할 준비가 되기까지 잠시 기다린다. 필자의 경우 한 5분 정도 기다렸다.
- 프로젝트 생성이 완료되면 화면이 전환된다. [환경]을 눌러  빈스 토크 환경 목록을 본다.
- 환경 목록에 방금 생성한 환경의 상태가 OK로 보이는지 확인하고 URL을 클릭해 해당 URL에 잘 접속되는지 확인한다.
> 필자는 계속 실패하여 고생을 많이 했다. 이유는 **2단계 서비스 액세스 구성** 을 간과하고 넘어갔기 때문이었다.

#### 문제 원인
- 이미지의 이벤트 로그에서 가장 중요한 에러 메시지는 다음과 같습니다.
- ![일레스틱 서비스 생성 04 단계 실패.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%2004%20%EB%8B%A8%EA%B3%84%20%EC%8B%A4%ED%8C%A8.png)
> "The instance profile aws-elasticbeanstalk-ec2-role associated with the environment does not exist."
- 이 메시지는 Elastic Beanstalk 환경이 EC2 인스턴스를 실행하려고 할 때 필수 **IAM** 인스턴스 프로파일(aws-elasticbeanstalk-ec2-role)이 없어서 환경 생성에 실패 했다는 의미이다.

---

## 해결 방법

아래 순서대로 진행하면 문제를 해결할 수 이다.

1. **IAM 역할(Instance Profile) 생성**
    - AWS 콘솔에서 "IAM" 서비스로 이동
    - 왼쪽 메뉴에서 "역할" 클릭 → "역할 만들기" 클릭
    - **신뢰할 수 있는 엔터티 유형**: AWS 서비스
    - **사용 사례 선택**: EC2
    - **권한 정책 연결**:
        - `AWSElasticBeanstalkWebTier`
        - `AWSElasticBeanstalkMulticontainerDocker`
        - `AWSElasticBeanstalkWorkerTier`
        - (최소한 `AWSElasticBeanstalkWebTier`는 반드시 추가)
    - 역할 이름을 **aws-elasticbeanstalk-ec2-role**로 지정하고 역할 생성
2. **Elastic Beanstalk 환경에 역할 연결**
    - Elastic Beanstalk 콘솔로 돌아가 환경을 다시 생성하거나,
      기존 환경의 "구성" > "인스턴스" > "EC2 인스턴스 프로파일" 항목에서
      방금 만든 **aws-elasticbeanstalk-ec2-role**을 선택
3. **환경 재생성 또는 재배포**
    - 역할이 정상적으로 연결되었는지 확인한 후,
      환경을 다시 생성하거나 "업로드 및 배포" 버튼을 눌러 재배포

---
### AWSElasticBeanstalkWebTier

AWSElasticBeanstalkMulticontainerDocker

AWSElasticBeanstalkWorkerTier
각각 어떤 정책 역할 을 하는거야?

각 정책의 역할과 용도를 아래와 같이 정리할 수 있습니다.

---

### AWSElasticBeanstalkWebTier

- **역할:**
  웹 서버 환경(예: Spring, Node.js, PHP 등 웹 애플리케이션)의 EC2 인스턴스가 **로그 파일을 Amazon S3에 업로드**할 수 있도록 허용하는 권한을 제공합니다.
- **주요 권한:**
    - S3에 로그 업로드
- **적용 대상:**
  Elastic Beanstalk의 **Web 환경** 인스턴스 프로파일[^1][^4][^6].

---

### AWSElasticBeanstalkMulticontainerDocker

- **역할:**
  **멀티 컨테이너 Docker 환경**(Amazon ECS 기반)에서 EC2 인스턴스가 **컨테이너 관리와 관련된 AWS 리소스**(예: ECS, S3 등)에 접근할 수 있도록 필요한 권한을 제공합니다.
- **주요 권한:**
    - ECS와 통신 및 컨테이너 배포
    - S3 접근 등
- **적용 대상:**
  Elastic Beanstalk의 **멀티 컨테이너 Docker 환경** 인스턴스 프로파일[^3][^6].

---

### AWSElasticBeanstalkWorkerTier

- **역할:**
  **워커 환경**(백그라운드 작업 처리 등)에서 EC2 인스턴스가 **SQS(큐) 사용, S3 로그 업로드, CloudWatch 모니터링, DynamoDB 리더 선출** 등 다양한 AWS 리소스에 접근할 수 있도록 권한을 제공합니다.
- **주요 권한:**
    - S3 로그 업로드
    - SQS 큐 모니터링 및 메시지 처리
    - DynamoDB를 통한 리더 선출
    - CloudWatch에 메트릭 전송
- **적용 대상:**
  Elastic Beanstalk의 **Worker 환경** 인스턴스 프로파일[^2][^6].

---

## 비교 표

| 정책 이름 | 용도/역할 요약 | 대표 적용 환경 |
| :-- | :-- | :-- |
| AWSElasticBeanstalkWebTier | 웹 서버 인스턴스의 S3 로그 업로드 권한 | Web 환경 |
| AWSElasticBeanstalkMulticontainerDocker | 멀티 컨테이너 Docker(ECS) 환경의 컨테이너 관리 권한 및 S3 등 리소스 접근 | Multicontainer Docker 환경 |
| AWSElasticBeanstalkWorkerTier | 워커 인스턴스의 SQS, S3, CloudWatch, DynamoDB 등 다양한 리소스 접근 권한 | Worker 환경 |


---

각 환경에 맞는 정책을 인스턴스 프로파일(역할)에 연결해야 Elastic Beanstalk가 정상적으로 동작합니다.
정책은 AWS 공식 관리형 정책이며, 환경 유형에 따라 적절히 선택해 사용하면 됩니다[^1][^2][^3][^4][^6].

<div style="text-align: center">⁂</div>

[^1]: https://docs.aws.amazon.com/aws-managed-policy/latest/reference/AWSElasticBeanstalkWebTier.html

[^2]: https://docs.aws.amazon.com/aws-managed-policy/latest/reference/AWSElasticBeanstalkWorkerTier.html

[^3]: https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/create_deploy_docker_ecs_role.html

[^4]: https://docs.aws.amazon.com/ko_kr/aws-managed-policy/latest/reference/AWSElasticBeanstalkWebTier.html

[^5]: https://docs.aws.amazon.com/aws-managed-policy/latest/reference/AWSElasticBeanstalkRoleWorkerTier.html

[^6]: https://repost.aws/questions/QUvrLmpQ_3QluE_1-3LN8ugw/elastic-beanstalk-environment-health-suspended-grey

[^7]: https://repost.aws/questions/QUE4AXn3a1TjaPrOfOfVz7AQ/role-created-to-use-as-ec-instance-profile-for-elastic-beanstalk-not-working

[^8]: https://yes5.tistory.com/55

[^9]: https://velog.io/@coaudtn0276/AWS-Elastic-Beanstalk-IAM-등록

[^10]: https://repost.aws/questions/QUx_V0HSWxRJSRndVaSlsvOQ/elastic-bean-stalk-environment-is-not-getting-created

[^11]: https://jayendrapatil.com/tag/elastic-beanstalk-worker-environment-tier/

[^12]: https://stackoverflow.com/questions/76138569/elastic-beanstalk-says-aws-elasticbeanstalk-ec2-role-does-not-exist-when-creat

[^13]: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/AWSHowTo.iam.managed-policies.html

[^14]: https://pronteff.com/demystifying-aws-elastic-beanstalk-and-environment-tiers/

[^15]: https://www.reddit.com/r/aws/comments/15j6t76/deploying_to_elastic_beanstalk_getting_a_warning/

[^16]: https://codingapple.com/forums/topic/elastic-beanstalk-에서-aws-elasticbeanstalk-ec2-role-이-안-나타납니다/

[^17]: https://stackoverflow.com/questions/23238366/amazon-elastic-beanstalk-worker-tier

[^18]: https://serverfault.com/questions/891488/elastic-beanstalk-iam-policy-for-deploying-a-multicontainer-docker-environment

[^19]: https://stackoverflow.com/questions/30790666/error-with-not-existing-instance-profile-while-trying-to-get-a-django-project-ru

[^20]: https://docs.aws.amazon.com/aws-managed-policy/latest/reference/AWSElasticBeanstalkRoleECS.html


---
## 요약

- **IAM에서 aws-elasticbeanstalk-ec2-role 역할을 반드시 만들어야 함**
- 역할에 필요한 권한 정책을 연결해야 함
- 환경 구성에서 해당 역할을 지정해야 환경이 정상적으로 생성됨

---

> 실제로 **aws-elasticbeanstalk-ec2-role** 없어 해결 방법 대로 만들어서 **IAM에서 aws-elasticbeanstalk-ec2-role** 역할을 만들고 정책도 넣고 다시 재배포를 할려고 했는데 또 문제가 발생했다.

- ec2 인스턴스 프로파일에 **aws-elasticbeanstalk-ec2-role** 선택 목록에 보이지 않았다.
- ![일레스틱 서비스 생성 04 단계 실패_2.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%2004%20%EB%8B%A8%EA%B3%84%20%EC%8B%A4%ED%8C%A8_2.png)

현재 **EC2 인스턴스 프로파일(aws-elasticbeanstalk-ec2-role)** 이 Elastic Beanstalk 환경 설정 화면에서 선택지로 안 보이는 이유는,
**단순히 IAM 역할만 만들었지, “인스턴스 프로파일”로 등록되지 않았기 때문**이다.[^2][^3][^6][^8].

AWS에서는 **EC2 인스턴스 프로파일**이란 “EC2 인스턴스에 부여할 수 있는 IAM 역할을 감싸는 컨테이너”이다.
콘솔에서 역할만 만들면 자동으로 프로파일이 만들어지는 것처럼 보이지만,
최근에는 계정이나 콘솔 정책에 따라 인스턴스 프로파일이 자동 생성되지 않는 경우가 이다.

---

## 해결 방법: **인스턴스 프로파일을 직접 생성해서 역할을 연결해야 합니다**

### 1. **AWS CLI(CloudShell)로 인스턴스 프로파일 생성**

콘솔에서 인스턴스 프로파일을 직접 만드는 메뉴가 보이지 않으면,
아래처럼 AWS CLI(CloudShell)에서 명령어로 생성하면 바로 해결됩니다[^6]:

```bash
# 1. 인스턴스 프로파일 생성
aws iam create-instance-profile --instance-profile-name aws-elasticbeanstalk-ec2-role

# 2. 인스턴스 프로파일에 역할 연결
aws iam add-role-to-instance-profile --instance-profile-name aws-elasticbeanstalk-ec2-role --role-name aws-elasticbeanstalk-ec2-role
```

- 위 명령어에서 `--role-name`은 이미 만든 IAM 역할 이름과 동일해야 합니다.
- CloudShell은 AWS 콘솔 우상단 터미널 아이콘(>)을 클릭하면 바로 실행할 수 이다.

---

### 2. **이후 콘솔에서 다시 시도**

- 위 과정을 마치고 1~2분 후 Elastic Beanstalk 환경 구성의 “EC2 인스턴스 프로파일” 선택란에
  **aws-elasticbeanstalk-ec2-role**이 나타납니다.
- 이제 해당 프로파일을 선택해 환경을 생성하거나 재설정하면 정상 동작합니다.

---

## 참고: **왜 이런 현상이 발생하는가?**

- 예전에는 역할만 만들면 자동으로 인스턴스 프로파일이 생성됐지만,
  최근 AWS 보안 정책 강화로 인해 **역할과 인스턴스 프로파일을 별도로 만들어 직접 연결해야** 하는 경우가 많아졌습니다[^2][^3][^4][^6][^8][^9].

---

## 요약

| 단계 | 설명 |
| :-- | :-- |
| 1 | IAM에서 `aws-elasticbeanstalk-ec2-role` 역할을 생성(이미 완료) |
| 2 | AWS CLI(CloudShell)에서 인스턴스 프로파일을 직접 생성하고, 역할을 연결 |
| 3 | Elastic Beanstalk 환경 설정에서 해당 인스턴스 프로파일을 선택해 환경 생성 |


---

**즉, AWS CLI(CloudShell)에서 위 명령어 두 줄만 실행하면 바로 해결됩니다!**
(콘솔로만 하려다 막히는 경우가 많으니, CLI 사용이 가장 빠릅니다.)
---
## 나는 여기서 인스턴스 프로파일에 대해 궁금 했다.
  ![인스턴스 프로파일.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%B8%EC%8A%A4%ED%84%B4%EC%8A%A4%20%ED%94%84%EB%A1%9C%ED%8C%8C%EC%9D%BC.png)

### 인스턴스 프로파일(Instance Profile)이란?

**인스턴스 프로파일**은 AWS에서 EC2 인스턴스에 **IAM 역할(Role)** 을 연결하기 위한 “컨테이너”입니다.
즉, **EC2 인스턴스가 특정 권한을 갖고 AWS 리소스에 접근할 수 있도록 해주는 중간 역할**을 합니다[^2][^4][^5][^6].

---

### 쉽게 설명하면

- **IAM 역할(Role)**: 어떤 AWS 리소스에 어떤 권한을 줄지 정의하는 정책(예: S3 읽기/쓰기, DynamoDB 접근 등)
- **인스턴스 프로파일(Instance Profile)**: 이 역할(Role)을 EC2 인스턴스에 “붙여주는” 용기(Wrapper)

EC2 인스턴스는 직접 IAM 역할을 가질 수 없고, **반드시 인스턴스 프로파일을 통해 역할을 할당**받습니다[^2][^3][^4][^5][^6].

---

### 왜 필요한가?

- 예를 들어, EC2 인스턴스에서 S3에 파일을 업로드하려면,
  S3 접근 권한이 있는 IAM 역할이 필요합니다.
- 이 역할을 “인스턴스 프로파일”에 담아서 EC2 인스턴스에 연결하면,
  인스턴스 내부의 프로그램(예: AWS CLI, SDK)이 자동으로 임시 자격증명을 받아
  S3 등 AWS 리소스에 접근할 수 있습니다.

---

### 실제 동작 방식

1. **IAM 역할을 만든다** (예: S3 접근 권한 포함)
2. **인스턴스 프로파일을 만든다** (역할을 담는 용기)
3. **인스턴스 프로파일에 역할을 연결한다**
4. **EC2 인스턴스에 인스턴스 프로파일을 연결한다**

이렇게 하면 EC2 인스턴스가 해당 권한을 사용해서 AWS 리소스에 안전하게 접근할 수 있습니다.

---

### 참고

- AWS 콘솔에서 역할을 만들면 보통 같은 이름의 인스턴스 프로파일이 자동 생성되지만,
  CLI나 일부 환경에서는 **직접 인스턴스 프로파일을 만들어야 할 수도 있습니다**[^2][^4][^6].
- Elastic Beanstalk 등 자동화 서비스도 내부적으로 인스턴스 프로파일을 사용합니다[^6].

---

## 요약

- **인스턴스 프로파일** = EC2 인스턴스에 IAM 역할을 연결해주는 컨테이너
- **EC2가 AWS 리소스에 안전하게 접근할 수 있게 해주는 필수 구성요소**
- 콘솔에서 역할을 만들면 자동 생성되지만, CLI나 일부 환경에서는 직접 만들어야 함

---

> **즉, 인스턴스 프로파일이 없으면 EC2 인스턴스(혹은 Beanstalk 환경)가 필요한 권한을 얻지 못해 S3, DynamoDB 등 AWS 리소스에 접근할 수 없고, 환경 생성도 실패합니다.**[^2][^4][^6]

- gpt에게 문의 를 해보았다. 왜냐면 맞게 한 것이 맞는지 몰랐기 때문에
- 질문 내용은 AWS CLI(CloudShell)로 인스턴스 프로파일 생성 을 해봤는데 잘 된건가?
```shell


~ $ # 1. 인스턴스 프로파일 생성
~ $ aws iam create-instance-profile --instance-profile-name aws-elasticbeanstalk-ec2-role
{
"InstanceProfile": {
"Path": "/",
"InstanceProfileName": "aws-elasticbeanstalk-ec2-role",
"InstanceProfileId": "AIPATS4RFO3KQ2JMSUVQW",
"Arn": "arn:aws:iam::246728193749:instance-profile/aws-elasticbeanstalk-ec2-role",
"CreateDate": "2025-04-22T20:03:50+00:00",
"Roles": []
}
}
~ $
~ $ # 2. 인스턴스 프로파일에 역할 연결
~ $ aws iam add-role-to-instance-profile --instance-profile-name aws-elasticbeanstalk-ec2-role --role-name aws-elasticbeanstalk-ec2-role

An error occurred (NoSuchEntity) when calling the AddRoleToInstanceProfile operation: The role with name aws-elasticbeanstalk-ec2-role cannot be found.
~ $ # 2. 인스턴스 프로파일에 역할 연결
~ $ aws iam add-role-to-instance-profile --instance-profile-name aws-elasticbeanstalk-ec2-role --role-name aws-elasticbeanstalk-ec2-role

An error occurred (NoSuchEntity) when calling the AddRoleToInstanceProfile operation: The role with name aws-elasticbeanstalk-ec2-role cannot be found.
~ $
```

## 결과 해석 및 문제 원인

### 1. **인스턴스 프로파일 생성**

- 아래 명령어는 **정상적으로 실행**되었습니다.

```bash
aws iam create-instance-profile --instance-profile-name aws-elasticbeanstalk-ec2-role
```

    - 결과로 `"InstanceProfile"` 오브젝트가 반환되었으니, 인스턴스 프로파일 자체는 잘 만들어졌습니다.


### 2. **역할(Role) 연결 시도**

- 아래 명령어에서 **오류가 발생**했습니다.

```bash
aws iam add-role-to-instance-profile --instance-profile-name aws-elasticbeanstalk-ec2-role --role-name aws-elasticbeanstalk-ec2-role
```

- 오류 메시지:

```
An error occurred (NoSuchEntity) when calling the AddRoleToInstanceProfile operation: The role with name aws-elasticbeanstalk-ec2-role cannot be found.
```

- **원인:**
`aws-elasticbeanstalk-ec2-role`이라는 이름의 IAM 역할(Role)이 **아직 생성되지 않았기 때문**입니다.

---

## 다음에 해야 할 일

### 1. **IAM 역할(Role) 먼저 생성**

- 인스턴스 프로파일은 "껍데기"이고, 실제 권한은 역할(Role)이 담당합니다.
- **아직 역할이 없으니, 아래 절차로 역할을 먼저 만들어야 합니다.**


#### IAM 역할 생성 방법 (콘솔 또는 CLI)

**A. 콘솔에서 생성**

1. AWS 콘솔 → IAM → 역할(Role) → 역할 만들기
2. **신뢰할 수 있는 엔터티 유형:** AWS 서비스
3. **사용 사례:** EC2
4. **권한 정책:**
    - `AWSElasticBeanstalkWebTier`
    - `AWSElasticBeanstalkWorkerTier`
    - `AWSElasticBeanstalkMulticontainerDocker`
      (최소한 WebTier는 반드시 추가)
5. 역할 이름을 **aws-elasticbeanstalk-ec2-role**로 지정
6. 역할 생성

**B. CLI에서 생성**

```bash
aws iam create-role \
  --role-name aws-elasticbeanstalk-ec2-role \
  --assume-role-policy-document '{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": { "Service": "ec2.amazonaws.com" },
        "Action": "sts:AssumeRole"
      }
    ]
  }'
```

- 그 다음, 필요한 권한 정책을 연결합니다:

```bash
aws iam attach-role-policy --role-name aws-elasticbeanstalk-ec2-role --policy-arn arn:aws:iam::aws:policy/AWSElasticBeanstalkWebTier
aws iam attach-role-policy --role-name aws-elasticbeanstalk-ec2-role --policy-arn arn:aws:iam::aws:policy/AWSElasticBeanstalkWorkerTier
aws iam attach-role-policy --role-name aws-elasticbeanstalk-ec2-role --policy-arn arn:aws:iam::aws:policy/AWSElasticBeanstalkMulticontainerDocker
```


### 2. **역할을 인스턴스 프로파일에 연결**

- 역할이 정상적으로 생성된 후, 아래 명령어를 다시 실행하세요:

```bash
aws iam add-role-to-instance-profile --instance-profile-name aws-elasticbeanstalk-ec2-role --role-name aws-elasticbeanstalk-ec2-role
```

- 이제 오류 없이 연결됩니다.

---

## 요약

- **인스턴스 프로파일은 잘 만들어졌지만, 역할(Role)이 없어서 연결에 실패**했습니다.
- **먼저 aws-elasticbeanstalk-ec2-role 역할을 생성**하고,
- **필요한 정책을 연결**한 뒤,
- **add-role-to-instance-profile 명령어를 다시 실행**하면 정상적으로 인스턴스 프로파일이 완성됩니다[^2][^3][^5][^6].

---




[^1]: https://docs.aws.amazon.com/cli/latest/reference/iam/list-roles.html

[^2]: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/iam-instanceprofile.html

[^3]: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/concepts-roles-instance.html

[^4]: https://repost.aws/questions/QUE4AXn3a1TjaPrOfOfVz7AQ/role-created-to-use-as-ec-instance-profile-for-elastic-beanstalk-not-working

[^5]: https://dev.to/backendbro/how-to-fix-the-aws-elasticbeanstalk-ec2-role-error-in-aws-elastic-beanstalk-1ack

[^6]: https://www.reddit.com/r/aws/comments/13f2jdg/error_the_instance_profile/

[^7]: https://docs.aws.amazon.com/IAM/latest/APIReference/API_ListRoles.html

[^8]: https://docs.aws.amazon.com/cli/latest/reference/iam/list-role-tags.html

[^9]: https://github.com/aws/aws-sdk/issues/636

[^10]: https://repost.aws/questions/QUwwbV_gh_QiyyOMtLFOayCQ/iam-roles-full-list-to-date

[^11]: https://docs.aws.amazon.com/ko_kr/cli/latest/userguide/cli-configure-role.html

[^12]: https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/iam-instanceprofile.html

[^13]: https://guides.zadarastorage.com/cs-iam-guide/2107/aws-roles.html

[^14]: https://joewithtech.tistory.com/75

[^15]: https://codingapple.com/forums/topic/elastic-beanstalk-에서-aws-elasticbeanstalk-ec2-role-이-안-나타납니다/

[^16]: https://dangdangee.tistory.com/entry/AWS-Elastic-Beanstalk-상태-unknown-오류-해결방법

[^17]: https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/concepts-roles-instance.html

[^18]: https://velog.io/@kjw9684/04-Elastic-Beanstalk

[^19]: https://ironmask43.tistory.com/113

[^20]: https://repost.aws/knowledge-center/iam-assume-role-cli

[^21]: https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/iam-roles-for-amazon-ec2.html

[^22]: https://awscli.amazonaws.com/v2/documentation/api/latest/reference/iam/list-roles.html

[^23]: https://docs.aws.amazon.com/ko_kr/IAM/latest/UserGuide/iam_example_iam_ListRoles_section.html

[^24]: https://serverfault.com/questions/962397/how-to-show-an-iam-users-roles-with-aws-iam

[^25]: https://dev.to/backendbro/how-to-fix-the-aws-elasticbeanstalk-ec2-role-error-in-aws-elastic-beanstalk-1ack

[^26]: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/iam-instanceprofile.html

[^27]: https://velog.io/@coaudtn0276/AWS-Elastic-Beanstalk-IAM-등록

[^28]: https://yes5.tistory.com/55

[^29]: https://stackoverflow.com/questions/78558239/role-created-to-use-as-ec2-instance-profile-for-elastic-beanstalk-not-visible


[^1]: https://pplx-res.cloudinary.com/image/private/user_uploads/mkDeoygSUEbnyTQ/seukeurinsyas-2025-04-23-ojeon-4.16.48.jpg

[^2]: https://jibinary.tistory.com/389

[^3]: https://yonghyuc.wordpress.com/2021/01/25/iam-role-vs-instance-profile/

[^4]: https://devopscube.com/aws-iam-role-instance-profile/

[^5]: https://cloud.google.com/kubernetes-engine/multi-cloud/docs/aws/how-to/update-instance-profile

[^6]: https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/iam-instanceprofile.html

[^7]: https://docs.aws.amazon.com/ko_kr/IAM/latest/UserGuide/id_roles_use_switch-role-ec2_instance-profiles.html

[^8]: https://docs.aws.amazon.com/ko_kr/codedeploy/latest/userguide/getting-started-create-iam-instance-profile.html

[^9]: https://docs.aws.amazon.com/codedeploy/latest/userguide/getting-started-create-iam-instance-profile.html

[^10]: https://docs.aws.amazon.com/ko_kr/IAM/latest/UserGuide/id_tags_instance-profiles.html

[^11]: https://jirak.net/wp/운영-중인-ec2-인스턴스에-iam-역할-연결하기/

[^12]: https://repost.aws/ko/knowledge-center/attach-replace-ec2-instance-profile

[^13]: https://docs.aws.amazon.com/managedservices/latest/userguide/defaults-instance-profile.html

[^14]: https://repost.aws/knowledge-center/attach-replace-ec2-instance-profile

[^15]: https://docs.aws.amazon.com/ko_kr/athena/latest/ug/odbc-v2-driver-instance-profile.html

[^16]: https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_switch-role-ec2_instance-profiles.html

[^17]: https://devlog-wjdrbs96.tistory.com/302

[^18]: https://www.reddit.com/r/aws/comments/1hkep9l/what_is_an_ec2_instance_profile/

[^19]: https://kim-dragon.tistory.com/239

[^20]: https://docs.aws.amazon.com/ko_kr/pcs/latest/userguide/security-instance-profiles.html

[^21]: https://dev.to/yuta28/what-is-iam-instance-profile-3h28


<div style="text-align: center">⁂</div>

[^1]: https://pplx-res.cloudinary.com/image/private/user_uploads/mkDeoygSUEbnyTQ/seukeurinsyas-2025-04-23-ojeon-4.16.48.jpg

[^2]: https://stackoverflow.com/questions/78558239/role-created-to-use-as-ec2-instance-profile-for-elastic-beanstalk-not-visible

[^3]: https://stackoverflow.com/questions/76138569/elastic-beanstalk-says-aws-elasticbeanstalk-ec2-role-does-not-exist-when-creat

[^4]: https://ginghambagle.tistory.com/162

[^5]: https://stackoverflow.com/questions/78751749/how-to-create-ec2-instance-profile-for-aws-elastic-beanstalk

[^6]: https://repost.aws/questions/QUE4AXn3a1TjaPrOfOfVz7AQ/role-created-to-use-as-ec-instance-profile-for-elastic-beanstalk-not-working

[^7]: https://dev.to/backendbro/how-to-fix-the-aws-elasticbeanstalk-ec2-role-error-in-aws-elastic-beanstalk-1ack

[^8]: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/iam-instanceprofile.html

[^9]: https://www.reddit.com/r/aws/comments/13f2jdg/error_the_instance_profile/

[^10]: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/concepts-roles-instance.html

[^11]: https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/iam-instanceprofile.html

[^12]: https://dangdangee.tistory.com/entry/AWS-Elastic-Beanstalk-상태-unknown-오류-해결방법

[^13]: https://www.youtube.com/watch?v=cfO5mfI_EdM

[^14]: https://joewithtech.tistory.com/75

[^15]: https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/concepts-roles-instance.html

[^16]: https://repost.aws/questions/QUdnIu02GeT4eTunn4JnPmIA/how-can-i-add-an-instance-manually-to-elastic-beanstalk-environment-that-uses-the-same-application-stack

[^17]: https://repost.aws/questions/QUWMMydAbgSxSOgICit0fp3g/story-time-the-instance-profile-aws-elasticbeanstalk-ec2-role-associated-with-the-environment-does-not-exist

[^18]: https://velog.io/@coaudtn0276/AWS-Elastic-Beanstalk-IAM-등록

[^19]: https://ironmask43.tistory.com/113

### 여기서 나는 궁금했다. 
> aws-elasticbeanstalk-service-role 과 aws-elasticbeanstalk-ec2-role 무슨 차이 점이 있는지.

## 질문 요약

- **Elastic Beanstalk 환경에서 EC2 인스턴스 프로파일로 `aws-elasticbeanstalk-service-role`을 사용하고 싶음**
- 현재 IAM 역할 목록에는 `aws-elasticbeanstalk-service-role`만 있고,
  `aws-elasticbeanstalk-ec2-role`은 없음

---

## Elastic Beanstalk 역할 구조 요약

Elastic Beanstalk 환경에는 **두 가지 주요 역할**이 필요합니다:


| 역할 유형 | 용도 및 연결 대상 | 예시 이름 |
| :-- | :-- | :-- |
| **서비스 역할** (Service Role) | Beanstalk 자체가 AWS 리소스에 접근할 때 사용 | `aws-elasticbeanstalk-service-role` |
| **인스턴스 프로파일** (Instance Profile) | EC2 인스턴스가 S3 등 AWS 리소스에 접근할 때 사용 | `aws-elasticbeanstalk-ec2-role` |

- **서비스 역할**은 Beanstalk가 환경 관리, 모니터링, 업데이트 등 **AWS 리소스 작업**을 할 때 사용합니다.
- **인스턴스 프로파일**은 **EC2 인스턴스가 S3, CloudWatch 등 AWS 리소스에 접근**할 때 사용합니다.

---

## 현재 상황

- **`aws-elasticbeanstalk-service-role`** 은 서비스 역할로서,
  EC2 인스턴스 프로파일(Instance Profile)로 바로 사용할 수 없습니다[^7][^8].
- EC2 인스턴스 프로파일에는 **EC2가 사용할 수 있게 신뢰 정책(Principal: ec2.amazonaws.com)이 필요**합니다.
- 현재 IAM 역할 목록에는 `aws-elasticbeanstalk-ec2-role`이 없으므로,
  인스턴스 프로파일로 사용할 수 있는 역할이 없는 상태입니다.

---

## 해결 방법

### 1. **EC2 인스턴스 프로파일용 역할을 새로 만들어야 합니다**

#### 콘솔에서 생성하는 방법

1. **IAM 콘솔 > 역할 > 역할 생성**
2. **신뢰할 수 있는 엔터티 유형**: AWS 서비스
3. **사용 사례**: EC2
4. **권한 정책**:
    - `AWSElasticBeanstalkWebTier`
    - `AWSElasticBeanstalkWorkerTier`
    - (필요에 따라 추가)
5. **역할 이름**: `aws-elasticbeanstalk-ec2-role` (권장)
6. 생성 완료 후, 이 역할을 **인스턴스 프로파일**로 사용

#### CLI로 생성하는 방법

```bash
aws iam create-role \
  --role-name aws-elasticbeanstalk-ec2-role \
  --assume-role-policy-document '{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": { "Service": "ec2.amazonaws.com" },
        "Action": "sts:AssumeRole"
      }
    ]
  }'

aws iam attach-role-policy --role-name aws-elasticbeanstalk-ec2-role --policy-arn arn:aws:iam::aws:policy/AWSElasticBeanstalkWebTier
aws iam attach-role-policy --role-name aws-elasticbeanstalk-ec2-role --policy-arn arn:aws:iam::aws:policy/AWSElasticBeanstalkWorkerTier
```


---

### 2. **Beanstalk 환경에서 역할 지정**

- **환경 생성 또는 수정 시**
    - EC2 인스턴스 프로파일로 **`aws-elasticbeanstalk-ec2-role`**을 선택
    - 서비스 역할로 **`aws-elasticbeanstalk-service-role`**을 선택

---

## 참고

- **`aws-elasticbeanstalk-service-role`**은 EC2 인스턴스 프로파일로 사용하면 안 됩니다.
  역할의 신뢰 정책(AssumeRole Policy)이 다르기 때문입니다[^4][^5][^7][^8].
- 두 역할의 차이와 필요성은 [AWS 공식 문서](https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/concepts-roles.html)에서도 명확히 설명되어 있습니다.

---

## 결론

- **EC2 인스턴스 프로파일로 `aws-elasticbeanstalk-service-role`을 지정하는 것은 올바르지 않습니다.**
- 반드시 **EC2 인스턴스 프로파일용 역할(`aws-elasticbeanstalk-ec2-role`)을 새로 만들어서 사용**해야 합니다.
- 서비스 역할(`aws-elasticbeanstalk-service-role`)은 환경 생성 시 별도로 지정하면 됩니다.



<div style="text-align: center">⁂</div>

---

## 실제로 aws-elasticbeanstalk-ec2-role 없어서 해결 방법 대로 만들어서 다시 재배포를 진행 했다.

[^1]: https://pplx-res.cloudinary.com/image/private/user_uploads/vOhxRXfMJhwAsDc/seukeurinsyas-2025-04-16-ohu-5.23.12.jpg

[^2]: https://pplx-res.cloudinary.com/image/private/user_uploads/oJtOASAtTuAqjSf/screencapture-us-east-1-console-aws-amazon-iam-home-2025-04-23-05_07_10-1.jpg

[^3]: https://pplx-res.cloudinary.com/image/private/user_uploads/WikBrtrLHluQrYR/screencapture-us-east-1-console-aws-amazon-iam-home-2025-04-23-05_08_13.jpg

[^4]: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/concepts-roles-service.html

[^5]: https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/iam-servicerole.html

[^6]: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/iam-servicerole.html

[^7]: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/concepts-roles.html

[^8]: https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/concepts-roles-service.html

[^9]: https://velog.io/@coaudtn0276/AWS-Elastic-Beanstalk-IAM-등록

[^10]: https://dangdangee.tistory.com/entry/AWS-Elastic-Beanstalk-상태-unknown-오류-해결방법

[^11]: https://stackoverflow.com/questions/77742666/aws-elastic-beanstalk-sample-app-not-able-to-use-role-to-obtain-required-permiss

[^12]: https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/concepts-roles.html

[^13]: https://shinsunyoung.tistory.com/105

[^14]: https://dev.to/backendbro/how-to-fix-the-aws-elasticbeanstalk-ec2-role-error-in-aws-elastic-beanstalk-1ack

[^15]: https://dangdangee.tistory.com/entry/JSCODE-Elastic-Beanstalk을-통한-배포

[^16]: https://stackoverflow.com/questions/77554364/aws-elastic-beanstalk-service-role-permissions-stop-working-after-a-day-for-no-r

[^17]: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/using-service-linked-roles.html

[^18]: https://yes5.tistory.com/55

[^19]: https://repost.aws/questions/QUySxNIwSCQMW7rlvaoyV8bQ/elastic-beanstalk-unable-to-assume-role-arn-aws-iam-role-aws-elasticbeanstalk-service-role-verify-that-the-role-exists-and-is-configured-correctly


- 
- ![일레스틱 역할 ec2 생성.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%97%AD%ED%95%A0%20ec2%20%EC%83%9D%EC%84%B1.png)
- 결과는 실패 
- ![일레스틱 서비스 생성 04 단계 실패_3.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%2004%20%EB%8B%A8%EA%B3%84%20%EC%8B%A4%ED%8C%A8_3.png)
---

### ERROR 원인
> The instance profile aws-elasticbeanstalk-ec2-role associated with the environment has no role. Please attach a role to the instance profile.

- Elastic Beanstalk 에러가 왜 자꾸 나는걸까? 궁금했다. 그래서 gpt에게 문의를 했다. 정확성 유무는 잘 모르겠다. 

- 이 에러의 정확한 원인과 해결 방법을 정리해드립니다.

---

## 에러 메시지 해석

> **The instance profile aws-elasticbeanstalk-ec2-role associated with the environment has no role. Please attach a role to the instance profile.**

- **의미:**
  Elastic Beanstalk 환경에 연결된 인스턴스 프로파일(aws-elasticbeanstalk-ec2-role)에 **IAM 역할(Role)이 연결되어 있지 않다**는 뜻입니다.
- **결과:**
  EC2 인스턴스에 권한이 없으므로 환경 생성이 실패합니다.
- 나는 분명히 다 CLI에서 다 한거 같았는데 알고보니 에러가 났고 그 이후에는 연결 작업을 하지 않았다. 이런....
---

## 원인

- 최근 AWS 보안 정책 변경으로, Elastic Beanstalk가 자동으로 인스턴스 프로파일을 만들어주지 않습니다[^1][^2][^5][^6][^7].
- 사용자가 **직접 인스턴스 프로파일을 만들고, 그 안에 EC2용 역할(Role)을 연결**해야 합니다.
- 현재 인스턴스 프로파일(aws-elasticbeanstalk-ec2-role)은 만들어졌지만, **역할이 비어 있기 때문에 에러가 발생**합니다.

---

## 해결 방법

### 1. **IAM 역할(Role) 생성**

- IAM 콘솔 → 역할(Role) → 역할 만들기
- **신뢰할 수 있는 엔터티 유형:** AWS 서비스
- **사용 사례:** EC2
- **권한 정책:**
    - AWSElasticBeanstalkWebTier
    - AWSElasticBeanstalkWorkerTier
    - AWSElasticBeanstalkMulticontainerDocker
- **역할 이름:** aws-elasticbeanstalk-ec2-role (프로파일 이름과 동일하게)


### 2. **인스턴스 프로파일에 역할 연결**

- AWS CLI(CloudShell)에서 아래 명령어 실행:

```bash
aws iam add-role-to-instance-profile --instance-profile-name aws-elasticbeanstalk-ec2-role --role-name aws-elasticbeanstalk-ec2-role
```

- 이 명령어가 성공하면 인스턴스 프로파일 안에 역할이 연결됩니다.


### 3. **Elastic Beanstalk 환경에 인스턴스 프로파일 지정**

- 환경 생성/수정 시 EC2 인스턴스 프로파일로 `aws-elasticbeanstalk-ec2-role`을 선택

---

## 요약 표

| 단계 | 설명 |
| :-- | :-- |
| 역할 생성 | EC2용 역할 생성, 권한 정책 연결 |
| 역할-프로파일 연결 | add-role-to-instance-profile 명령 실행 |
| 환경에 지정 | Elastic Beanstalk에서 해당 프로파일 선택 |


---

## 참고 문서

- [AWS 공식 문서: 인스턴스 프로파일 관리](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/iam-instanceprofile.html)[^2]
- [에러 사례 및 해결 방법 블로그](https://joewithtech.tistory.com/75)[^1]
- [Stack Overflow 해결 가이드](https://stackoverflow.com/a/76620598/12326605)[^5]

---

**정리:**
이 에러는 "인스턴스 프로파일에 역할이 비어 있어서" 발생합니다.
반드시 **EC2용 IAM 역할을 만들고, 인스턴스 프로파일에 연결**해야 정상적으로 환경이 생성됩니다.
이 과정을 마치면 더 이상 해당 에러가 발생하지 않습니다.

<div style="text-align: center">⁂</div>

[^1]: https://joewithtech.tistory.com/75

[^2]: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/iam-instanceprofile.html

[^3]: https://dangdangee.tistory.com/entry/AWS-Elastic-Beanstalk-상태-unknown-오류-해결방법

[^4]: https://repost.aws/questions/QUE4AXn3a1TjaPrOfOfVz7AQ/role-created-to-use-as-ec-instance-profile-for-elastic-beanstalk-not-working

[^5]: https://www.reddit.com/r/aws/comments/13f2jdg/error_the_instance_profile/

[^6]: https://stackoverflow.com/questions/30790666/error-with-not-existing-instance-profile-while-trying-to-get-a-django-project-ru

[^7]: https://dev.to/backendbro/how-to-fix-the-aws-elasticbeanstalk-ec2-role-error-in-aws-elastic-beanstalk-1ack

[^8]: https://repost.aws/questions/QURMEc7-pmT0OT4-ui2u55mg/the-instance-profile-aws-elasticbeanstalk-ec2-role-associated-with-the-environment-does-not-exist

[^9]: https://repost.aws/questions/QUWMMydAbgSxSOgICit0fp3g/story-time-the-instance-profile-aws-elasticbeanstalk-ec2-role-associated-with-the-environment-does-not-exist

[^10]: https://ginghambagle.tistory.com/162

[^11]: https://logging-panda.tistory.com/120

[^12]: https://www.inflearn.com/community/questions/1519785/the-instance-profile-aws-elasticbeanstalk-ec2-role-associated-with-the-environme

[^13]: https://ironmask43.tistory.com/113

[^14]: https://repost.aws/ko/questions/QURMEc7-pmT0OT4-ui2u55mg/the-instance-profile-aws-elasticbeanstalk-ec2-role-associated-with-the-environment-does-not-exist

[^15]: https://stackoverflow.com/questions/78558239/role-created-to-use-as-ec2-instance-profile-for-elastic-beanstalk-not-visible

--- 
## 해결 방법 : 2. 인스턴스 프로파일에 역할 연결로 마침내 성공을 하였다.
- ![일랙스텍 서비스 생성 04 단계 성공.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%9E%99%EC%8A%A4%ED%85%8D%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%2004%20%EB%8B%A8%EA%B3%84%20%EC%84%B1%EA%B3%B5.png)
- ![일래스틱 서비스 생성하기 04단계 02.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%9E%98%EC%8A%A4%ED%8B%B1%20%EC%84%9C%EB%B9%84%EC%8A%A4%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%2004%EB%8B%A8%EA%B3%84%2002.png)

### 정리 보다는 원인을 분석하고 해석정리 위주로 정리하게 되었다.
#### 나의 생각 
- 후아 난 잘 모르겠다 아직도 갈 길이 멀다. 공부를 하는게 버겁다.. 하지만 습관을 만들어 보려고 한다. 조금씩이라도 앞으로 나아 갈려고 한다.
- 아직도 열심히 해본적이 없다는게 나의 발목을 잡게 된다. 이렇게 해서는 내가 생각하는 중요한 일원이 될 수 없고 대체가능한 사람이 된다는 생각이 들게된다.
- 나의 문제점은 한번 막히면 진행을 하다가 포기를 한다는 것이다. 그리고 나서 시간이 지나고 나서 또 시도 해보다가 안되면 또 회피하고 이게 과연 맞는건지 모르겠지만 시간이 너무 오래 걸리는 거 같다.




