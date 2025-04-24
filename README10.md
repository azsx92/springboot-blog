## 11.3.2 Elastic Beanstalk RDS 생성하기
- 이제 클라우드에 올릴 데이터 베이스를 생성하겠다. 앞서 언급했듯 아마존 RDB 서비스를 사용한다.
- 이 역시도 일래스틱 빈스토크의 메뉴를 이용해 간편하게 구축할 수 있다.

### 01 단계
- Elastic Beanstalk 환경에서 [구성]을 눌러 환경 설정 메뉴에 들어간다.
- 그런 다음 스크롤바를 내려 데이터베이스 메뉴를  찾아 [편집]을 눌러 데이터 베이스 설정을 추가한다.
- ![일래스틱 rds 생성 01 단계.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20RDS%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%9E%98%EC%8A%A4%ED%8B%B1%20rds%20%EC%83%9D%EC%84%B1%2001%20%EB%8B%A8%EA%B3%84.png)

### 02 단계
- 사용할 데이터 베이스 엔진으로 mysql을 선택하고 용량은 프리티어를 지원하는 db.t3.micro를 선택한다.
- 사용자 이름과 암호를 채운 다음 [적용] 버튼을 눌러 데이터베이스를 생성한다.
- 나는 **db.t3.micro** 선택하여  [적용] 버튼을 눌렀다.
- ![일레스틱 rds 생성 02 단계.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20RDS%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20rds%20%EC%83%9D%EC%84%B1%2002%20%EB%8B%A8%EA%B3%84.png)
- 책과는 달리 db.t2.micro가 보기에는 없어 찾아 보았다.

## AWS RDS MySQL 프리티어 한도 및 조건

AWS RDS에서 MySQL을 **프리티어(무료)**로 사용할 때 적용되는 주요 제한사항은 다음과 같습니다.

---

### 1. **사용 기간 및 대상**

- **프리티어는 계정 생성일로부터 12개월(1년) 동안만 적용**됩니다[^5].
- MySQL, MariaDB, PostgreSQL, SQL Server Express Edition 등이 해당됩니다[^5][^7].

---

### 2. **인스턴스 사양 및 개수**

- **DB 인스턴스 클래스:**
    - MySQL의 경우 **db.t3.micro** 또는 **db.t4g.micro** 인스턴스에서만 프리티어가 적용됩니다.
    - 일부 리전에서는 **db.t2.micro**도 지원합니다[^3][^5][^6][^8].
- **사용 시간:**
    - **한 달에 750시간**까지 무료(즉, 한 대를 한 달 내내 켜둘 수 있음)[^5].
    - 여러 인스턴스를 동시에 사용하면 총합이 750시간을 넘지 않아야 무료입니다[^1][^5].

---

### 3. **스토리지 용량**

- **20GB 범용 SSD(gp2) 스토리지**까지 무료[^5][^6][^8][^10].
- **스토리지 자동 조정(Autoscaling) 옵션을 끄지 않으면** 20GB를 초과할 때 요금이 부과될 수 있으니 주의해야 합니다[^1][^10].

---

### 4. **백업 스토리지**

- **활성 DB 인스턴스의 총 프로비저닝 스토리지 크기(최대 20GB)만큼 백업 스토리지 무료**[^5].
- 백업 스토리지가 이 한도를 초과하면 초과분에 대해 요금이 부과됩니다.

---

### 5. **기타 주의 사항**

- **Multi-AZ 배포, 프로비저닝 IOPS, 자동 스토리지 조정, 스냅샷 추가 보관 등은 프리티어에 포함되지 않으며 사용 시 요금이 부과**됩니다[^1].
- **DB 인스턴스는 반드시 1개만 생성**해야 하며, 여러 개를 만들면 총 사용량이 750시간을 초과할 수 있습니다[^1].
- **백업 보존 기간**을 0일로 설정하면 불필요한 백업 스토리지 요금을 피할 수 있습니다[^1].

---

### 6. **Aurora, Oracle 등은 프리티어에서 일부 제한**

- Aurora는 프리티어에서 지원하지 않습니다[^7].
- Oracle, SQL Server는 일부 에디션만 프리티어에 포함됩니다.

---

## 요약 표

| 항목 | 프리티어 한도 및 조건 |
| :-- | :-- |
| 적용 DB | MySQL, MariaDB, PostgreSQL, SQL Server Express |
| 인스턴스 클래스 | db.t3.micro, db.t4g.micro (일부 리전 db.t2.micro) |
| 인스턴스 수/시간 | 월 750시간(인스턴스 합산, 1대 24시간 x 31일) |
| 스토리지 | 20GB 범용 SSD(gp2) |
| 백업 스토리지 | 활성 인스턴스 용량만큼 무료(최대 20GB) |
| Multi-AZ, IOPS, Autoscaling | 프리티어 미포함, 사용 시 요금 부과 |
| 기간 | 계정 생성 후 12개월(1년) |


---

## 결론

**AWS RDS MySQL 프리티어는 "db.t3.micro" 또는 "db.t4g.micro" 인스턴스 1대(월 750시간), 20GB 스토리지, 20GB 백업 스토리지까지 1년간 무료**로 제공됩니다.
이 조건을 초과하거나, Multi-AZ, 자동 스토리지 조정, 추가 스냅샷, 고성능 IOPS 등을 사용하면 요금이 부과됩니다.

**프리티어 내에서만 사용하려면 인스턴스 1개, 20GB 이하 스토리지, 옵션 기능 해제(Autoscaling, Multi-AZ 등), 백업 스토리지 한도 준수에 신경 써야 합니다.**

<div style="text-align: center">⁂</div>

[^1]: https://suloth.tistory.com/17

[^2]: https://velog.io/@piecemaker/AWS의-서비스인-S3와-RDS에-대해-알아보자

[^3]: https://velog.io/@shawnhansh/AWS-RDSmySql-프리티어-생성하기

[^4]: https://pss240.tistory.com/10

[^5]: https://aws.amazon.com/ko/rds/free/

[^6]: https://park-algorithm.tistory.com/entry/AWS-RDS-구축하기

[^7]: https://cofls6581.tistory.com/197

[^8]: https://chickenpaella.tistory.com/33

[^9]: https://okky.kr/questions/1154651

[^10]: https://jih3508.tistory.com/167

---

### 03 단계
- 여기도 조금 기다려야 한다. 10분 정도 시간이 지나면 상태가 체크 표시로 변한다.
- 상태가 변한 것을 확인한 다음 [구성] 메뉴에서 데이터베이스 영역을 확인하면 생성된 데이터베이스의 정보를 확인 할 수가 있다.
- 이 정보는 H2 데이터베이스에서 RDS로 연결할 때 사용한다. 엔드포인트를 미리 복사 해준다.
- ![일레스틱 rds 생성 03 단계.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20RDS%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20rds%20%EC%83%9D%EC%84%B1%2003%20%EB%8B%A8%EA%B3%84.png)
- ![일레스틱 rds 생성 03 단계 4.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20RDS%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20rds%20%EC%83%9D%EC%84%B1%2003%20%EB%8B%A8%EA%B3%84%204.png)
- 여기서 주의할 점 상태가 완료 여서 [구성]을 눌러 데이터베이스의 [편집]을 눌러 엔드포인트를 확인 할려고 했으나 보이지가 않았다.

# 정상적으로 만들어 졌지만 왜  엔드포인트가 없을까?

정상적으로 Elastic Beanstalk 환경과 RDS 설정이 완료된 것처럼 보이지만, **RDS 엔드포인트가 보이지 않는 이유**는 다음과 같은 원인 때문일 가능성이 높습니다.

---

## 1. 실제 RDS 인스턴스가 생성되지 않았을 가능성

- **Beanstalk 환경 설정**에서 “결합된 데이터베이스 존재: true”로 표시되지만,
  실제로 AWS RDS 콘솔 > 데이터베이스(Instances) 메뉴에서 **MySQL 인스턴스가 생성되어 있고 상태가 available(사용 가능)** 인지 반드시 확인해야 합니다.
- Beanstalk에서 RDS 생성을 시도했지만 IAM 권한, VPC/서브넷, 네트워크 문제 등으로 **RDS 인스턴스 생성이 실패**했을 수 있습니다.
- 이벤트 로그에 “Created RDS database named: ...” 메시지는 있지만, 실제 인스턴스가 없으면 엔드포인트가 표시되지 않습니다.

---

## 2. RDS 인스턴스가 아직 “available” 상태가 아님

- RDS 인스턴스가 **생성 중(creating)** 상태라면 엔드포인트가 표시되지 않습니다[^4][^7].
- 인스턴스 생성에는 수 분~20분 이상 걸릴 수 있으니, RDS 콘솔에서 인스턴스 상태를 확인하세요.

---

## 3. 리전(Region) 불일치

- Beanstalk 환경과 RDS 인스턴스가 **서로 다른 리전에 생성**되었을 수 있습니다.
- AWS 콘솔 오른쪽 상단의 리전 선택 메뉴에서 Beanstalk와 동일한 리전을 선택한 후, RDS 콘솔을 확인하세요.

---

## 4. RDS 인스턴스가 삭제되었거나, 생성 후 Beanstalk 환경 삭제/재생성 과정에서 사라졌을 가능성

- Beanstalk 환경을 삭제하면, 기본 설정에 따라 RDS 인스턴스도 함께 삭제됩니다.
- “데이터베이스 삭제 정책: Snapshot”으로 되어 있으니, 삭제 시 스냅샷만 남고 인스턴스는 사라질 수 있습니다.

---

## 5. 네트워크/보안 그룹 문제

- RDS 인스턴스가 VPC, 서브넷, 보안 그룹 설정 문제로 인해 **정상적으로 생성되지 못했을 수 있습니다**[^6].
- 이 경우에도 엔드포인트가 보이지 않습니다.

---

## 6. 엔드포인트 확인 방법

- **RDS 콘솔** > **데이터베이스** 메뉴에서 MySQL 인스턴스가 “사용 가능(available)” 상태인지 확인
  → 인스턴스를 클릭하면 “연결 및 보안” 탭에서 엔드포인트(호스트명)와 포트를 확인할 수 있습니다[^8][^9].
- **AWS CLI**로도 확인 가능:

```bash
aws rds describe-db-instances --query 'DBInstances[*].[DBInstanceIdentifier,Endpoint.Address,Endpoint.Port,DBInstanceStatus]' --output table
```

→ 엔드포인트가 나오지 않으면 실제 인스턴스가 없는 것입니다.

---
- 생각보다 RDS의 인스턴스 생성시간이 10분이상 더 오래 걸린다는 걸 알게 되었고
- 콘솔의  Aurora and RDS > 데이터베이스가 만들어 졌는지 확인 하고 나서 다시 보면 엔드포인트가 보이는 걸 알 수 가 있었다.

- ![일레스틱 rds 생성 03단계 3.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20RDS%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20rds%20%EC%83%9D%EC%84%B1%2003%EB%8B%A8%EA%B3%84%203.png)

### 04 단계
- 데이터베이스가 생성되었으니 애플리케이션도 생성한 데이터베이스를 사용하게 변견하도록 하자.
- AWS 일래스틱 빈스토크에서 [구성]을 누르고 플레폼 소프트웨어에서 [편집]을 선택해 데이터베이스의 정보를 입력한 다음 [적용] 버튼을 눌러 마무리 한다.
- 그러면 일래스틱 빈스토크가 환경을 업데이트 한다.
- ![일레스틱 rds 생성 04단계.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20RDS%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20rds%20%EC%83%9D%EC%84%B1%2004%EB%8B%A8%EA%B3%84.png)
- ![일레스틱 rds 생성 04 단계 02.png](%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20RDS%20%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0%20%EC%9D%B4%EB%AF%B8%EC%A7%80%2F%EC%9D%BC%EB%A0%88%EC%8A%A4%ED%8B%B1%20rds%20%EC%83%9D%EC%84%B1%2004%20%EB%8B%A8%EA%B3%84%2002.png)

### 05 단계
- 이렇게 설정한 환경 속성 값은 애플리케이션 실행 시 스프링 부트의 환경 변수, 즉, properties.yml 파일의 설정값의 역할을 한다.
- application.yml 에서 spring 항목 datasource 항목들만 지운다.
```yaml
# 애플리케이션 실행 시 일래스틱 빈스토크에서 값을 덮어쓰므로 여기는 삭제
spring:
  datasource: # database 정보 추가
    url: jdbc:h2:mem:testdb
  h2: # h2 콘솔 활성화
    console:
      enabled: true
```
