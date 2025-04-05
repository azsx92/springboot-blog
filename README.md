# springboot-blog
### spring boot 3 로 블로그 만들어 보기

### Thread란?
간단한 예시 )

- 프로세스 안에서 사용되는 자원

- 기본적으로 커넥터에 종속된다.
- 각 커넥터에 별도로 쓰레드를 각각 정하기 보다는, shared excuter에 정의하는 쓰레드를 상속받아서 하나의 쓰레드 풀에서 관리할 수 있게 한다.
- 기본값으로 최대 쓰레드 200개의 설정을 가지고 있다.

### Thread Dump
- 앞서 든 예시에 덧붙여 표현하자면, 길에 여러가지 종류의 차량이 다니는데 이러한 차에 대한 정보를 알려주는 역할이다.
- 실행중인 Thread의 종류와 시작점, 실행한 클래스와 메소드 순서, 현재 상태등을 기록하는 JVM의 고유 기능이다.
- 쓰레드 덤프로 서비스의 흐름과 서비스 지연시 수행중인 작업, 병목등을 확인할 수 있다.
- 쓰레드 덤프의 시작에는 쓰레드 이름과 쓰레드의 정보가 기록되며 이후 쓰레드 상태에 대해 설명해준다.
- 트레이스의 읽는 순서는 위가 최근 실행한 클래스와 메소드이기 때문에 아래서부터 위로 읽습니다.

### [Spring] DB커넥션풀과 Hikari CP 알아보기
#### JDBC란?
- **Hikari CP**(히카리 커넥션풀)을 알아보기에 앞서 JDBC의 개념을 정리하자면,
- **JDBC**는 Java Database Connectivity의 약자로 자바에서 데이터베이스에 접속할 수 있도록 하는 자바 API다.
- JDBC는 데이터베이스에서 자료를 쿼리하거나 업데이트하는 방법을 제공한다.

### DB 커넥션 풀이란
- 일반적인 데이터 연동과정은 웹 어플리케이션이 필요할 때마다 데이터베이스에 연결하여 작업하는 방식입니다.
- 하지만 이런 식으로 필요할 때마다 연동해서 작업할 경우 데이터베이스 연결에 시간이 많이 걸리는 문제가 발생합니다.

- 예를들어 거래소의 경우, 동시에 몇천명이 동시에 거래 및 조회 기능을 사용하는데 매번 데이터베이스와 커넥션을 맺고 푸는 작업을 한다면 굉장히 비효율적일 것입니다.

- 이 문제를 해결하기 위해 현재는 웹 어플리케이션이 실행됨과 동시에 연동할 데이터베이스와의 연결을 미리 설정해 둡니다.

- 그리고 필요할 때마다 미리 연결해 놓은 상태를 이용해 빠르게 데이터베이스와 연동하여 작업을 합니다.

- 이렇게 미리 데이터베이스와 연결시킨 상태를 유지하는 기술을 **커넥션 풀**
(Connection Pool, CP)라고 합니다.

### 스프링에서의 커넥션 풀
- 자바에서는 기본적으로 **DataSource 인터페이스를 사용하여 커넥션풀을 관리**한다.
- Spring에서는 사용자가 직접 커넥션을 관리할 필요없이 자동화된 기법들을 제공하는데
- SpringBoot 2.0 이전에는 tomcat-jdbc를 사용하다,
- 현재 2.0이후 부터는 **HikariCP를 기본옵션**으로 채택 하고있다.

#### 왜 Hikari Cp일까?
히카리 벤치마킹 페이지를 참고하면 아래와 같이 월등한 성능을 보인다는 것을 알 수있다.

![img.png](img.png)

- HikariCp가 다른 커넥션풀 관리 프레임워크보다 빠른 성능을 보여주는 이유는 커넥션풀의 관리 방법에 있다.

- 히카리는 Connection 객체를 한번 Wrappring한 **PoolEntry**로 Connection을 관리하며,
- 이를 관리하는 **ConcurrentBag**이라는 구조체를 사용하고 있다.

- **ConcurrentBag은 HikariPool.getConnection() -> ConcurrentBag.borrow()** 라는 메서드를 통해 사용 가능한(idle) Connection을 리턴하도록 되어있다.

- 이 과정에서 커넥션생성을 요청한 스레드의 정보를 저장해두고 다음에 접근시 저장된 정보를 이용해 빠르게 반환을 해준다.

- 이러한 방법 때문에 속도에 이점이 있으며 해당 방법의 자세한 설명은 아래 블로그를 참조하면 좋을 것 같다.

### Hikari CP 사용법
- build.gradle에 따로 추가할 필요 없이
- "org.springframework.boot:spring-boot-starter-jdbc"를 추가하면 자동으로 추가된다.
- 이후 application.yml에 설정값을 추가하면 되는데

```text
spring:
 datasource:
   url: jdbc:mysql://localhost:3306/world?serverTimeZone=UTC&CharacterEncoding=UTF-8
   username: root
   password: your_password
   hikari:
     maximum-pool-size: 10
     connection-timeout: 5000
     connection-init-sql: SELECT 1
     validation-timeout: 2000
     minimum-idle: 10
     idle-timeout: 600000
     max-lifetime: 1800000

server:
 port: 8000
```

### options

- maximum-pool-size: 최대 pool size (defailt 10)
- connection-timeout: (말 그대로)
- connection-init-sql: SELECT 1
- validation-timeout: 2000
- minimum-idle: 연결 풀에서 HikariCP가 유지 관리하는 최소 유휴 연결 수
- idle-timeout: 연결을위한 최대 유휴 시간
- max-lifetime: 닫힌 후 pool 에있는 connection의 최대 수명 (ms)입니다.
- auto-commit: auto commit 여부 (default true)

### DeadLock 피하기
- 이론적으로 필요한 최소한의 커넥션 풀 사이즈를 알아보면 다음과 같다.
```text
PoolSize = Tn × ( Cm -1 ) + 1

Tn : 전체 Thread 갯수
Cm : 하나의 Task에서 동시에 필요한 Connection 수
```
- 위와 같은 식으로 설정을 한다면 데드락을 피할 수는 있겠지만 여유 커넥션풀이 하나 뿐이라 성능상 좋지 못하다.
- 따라서 커넥션풀의 여유를 주기위해 아래와 같은 식을 사용하는것을 권장한다.

```text
PoolSize = Tn × ( Cm - 1 ) + ( Tn / 2 )

thread count : 16
simultaneous connection count : 2
pool size : 16 * ( 2 – 1 ) + (16 / 2) = 24
```

궁금한 한 점은 tomcat 커넥션 풀과 스프링 커넥션 풀은 어떤 점이 다른 걸까?
차이점 정리로 여기까지 안다.
![img_1.png](img_1.png)