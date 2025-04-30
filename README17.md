## 예외 처리 가이드
- 스프링 , 스프링 부트는 예외 처리 를 쉽고 명확하기 처리할 수 있는 다양한 애너테이션을 지원한다.
- 이번에는 스프링, 스프링 부트를 사용할 때 어떤 방식으로 예외 처리를 하는 지 알아보겠다.
- BlogService.java 파일을 연 다음 글을 조회하는 findById() 메서드를 살펴보겠다.

```java

public Article findByid(long id) {
    return blogRepository.findById(id)
        .orElsethrow(() -> new IllegalArgumentException("not found : " + id));
    }
```
- 이 코드는 id를 입력받아 특정 블로그 글을 찾은 다음, 글이 없으면 IllegalArgumentException 예외와 함께 "not found ${id}" 라는 에러 메시지를 보낸다.
- 실제로 다음 포맷으로 에러 메시지를 보낸다.
- 실제로 예외가 발생하면 생기는 에러 메시지
  - ```json
    {
        "timestamp" : "2023-04-16T07:28:34.039+00:00" , # 예외 발생 시간
        "status": 500, # HTTP 상태 코드
        "error": "Internal Server Error", # 예외 유형
        "path": "/api/articles/123" # 예외가 발생한 요청 경로
    }    
    ```
- 이 포맷은 스프링 부트에서 기본으로 제공하는 DefaultErrorAttribute이다. 여기에 추가로 정보를 담고 싶다면 ErrorAttributes를 구현하고 빈으로 등록하면 구현한 ErrorAttributes에 맞게 에러 메시지를 만들 수 있다.
- 다은 DefaultErrorAttributes에 customValue라는 키값을 추가한 예이다. 따라 하지말고 눈으로만 봐달라.
```java

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    @Override   
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> result = super.getErrorAttributes(webRequest, options);
        result.put("customValue", "Hello, World!");
        return result;
    }
}
```
- 이렇게 구현하면 다음과ㅜ같이 임의 키값이 추가된 포맷을 에러 메시지로 만들어 준다.
- 임의 키갑싱 추가되누 포맷의 에러 메시지
  ```json
  {
  "timestamp" : "2023-04-16T07:37:16.999+00:00" , # 예외 발생 시간
  "status": 500, # HTTP 상태 코드
  "error": "Internal Server Error", # 예외 유형
  "path": "/api/articles/333" # 예외가 발생한 요청 경로
  "customValue": "Hello, World!"
  }```
- 그럼 다른 방법으로 에러 메시지를 만들 방법은 없는 걸까? 다른 방법도 있다. 팔자의 경우 에러 메시지용 객체를 만들어 사용하기를 더 좋아한다.
- 객체로 에러 메시지를 만들면 어떤 키값이 잇는지 한누에 보기 좋다. 그리고 구조를 바꾸기도 용이하다. 그래서 앞서 소개한 ErrorAttributes를 구현하는 방법 대신 에러 메시지용 객체를 별도로 만드는 방법을 실습하겠다.
- 기존이 에외 처리 로직에는 다음과 같은 두 가지의 아쉬운 점이 잇었는데 이것도 해결해보겠습니다.

1. 예외 이름만 보고는 왜 발생한 예외인지 파악이 어렵다.
2. 예외 메시지가 여러 곳에 퍼저 있기 때문에 관리하기가 어렵다.

### 01 단계 
- config 디렉토리에 error 디렉토리를 만들고 ErrorCode라는 이름을 가진 enum을 생성한다.
```java

@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E1", "올바르지 않은 입력값 입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E2", "잘못된 HTTP 메서드를 호출 했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E3", "서버 에러가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E4", "존재하지 않는 엔티티입니다."),
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "A1", "존재하지 않는 아티클입니다.");



    private final String message;
    private final String code;
    private final HttpStatus status;
    

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.message = message;
        this.code = code;
        this.status = httpStatus;
    }
}
```
- 이 코드는 에러 코드를 한 곳에 모아 관리하기 위한 enum이다. 에러가 발생했을 때 어떤 HTTP상태값으로 응답하는지 , 어떤 기본 메시지를 가지고 있는지, 어떤 고유한 에러 코드를 가지는지를 정의한 것이다. 이렇게 저의하면 예외를 한곳에서  관리할 수 있다.

### 02 단게
- ErrorResponse.java 파일을 생성한 뒤 다음 코드를 따라 입력한다.
```java

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    
    private String message;
    private String code;

    private ErrorResponse(final ErrorCode code ) {
        this.message = code.getMessage();
        this.code = code.getCode();
    }
    public ErrorResponse(final ErrorCode code , final String message) {
        this.message = message;
        this.code = code.getCode();
    }
    public static ErrorResponse of(final ErrorCode code ) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code , final String message) {
        return new ErrorResponse(code, message);
    }
}

```
- ErrorAttributes를 대체할 에러 메시지용 객체이다. 에러 메시지가 포함된 message 필드와 고유 에러 코드인 code 필드를 가지고 있다. ErrorResponse 객체를 사용하면 다음 형식의 JSON 응답을 받게 될 것이다.
  - 실제로 예외가 발생하면 생기는 에러메시지
    - 
  ```json
  {
  "message" : "존재하지 않는 엔티티이다.",
  "code": "E4"
  }
  ```