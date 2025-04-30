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
  
### 03단계
- error 디렉터리에 exception 디렉터리를 새로 만들고 BussinessBaseException.java 파일을 생성한 뒤 다음 코드를 입력한다.
- BusinessBaseException.java
```java
public class BusinessBaseException extends  RuntimeException
{
    private final ErrorCode errorCode;


    public BusinessBaseException(String message , ErrorCode errorCode) {
        super(message)  ;
        this.errorCode = errorCode;
    }
    
    public BusinessBaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

```
- 이 예외 클래스는 비즈니스 로직을 작성하다 발생하는 예외를 모아둘 최상위 클래스이다.
- BusinessBaseException을 상속받은 구조로 비즈니스 로직 관련 예외를 만드는 것이죠. 예를 들면 조회 대상이 없는 경우에 대한 예외를 정의하는 NoFoundException이나, 블로그 글을 조회 했을 때 발생할 수 있는 예외인 ArticleNotFoundException을 만든다.
- 예외 이름만 봐도 예외가 난 이유를 명확하게 파악할 수 있다. 이외에도 인증되지 않은 사용자에 대한 예외를 처리할 UnauthorizedException, 중복키에 대한 예외를 처리할 DuplicateKeyException 등이 있다.
- ![BusinessBaseException.png](appendB%2FBusinessBaseException.png)

### 04 단계
- 계속해서 코드를 작성한다. exception 디렉터리에 NotFoundException.java, ArticleNotFoundException.java 파일을 만들어 다음 코드를 작성한다.
- NotFoundException
```java
public class NotFoundException extends BusinessBaseException{
  public NotFoundException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }

  public NotFoundException(ErrorCode errorCode) {
    super(errorCode.NOT_FOUND);
  }

}
```
- ArticleNotFoundException
```java
public class ArticleNotFoundException extends NotFoundException{
    public ArticleNotFoundException(ErrorCode errorCode) {
      super(errorCode.ARTICLE_NOT_FOUND);
    }
}
```

### 05 단계
- error 디렉터리에 GlobalExceptionHandler.java를 만들고 다음 코드를 입력한다.
- 이 코드는 @ControllerAdvice를 사용한 예외 처리 핸들러 인데, @ControllerAdvice를 사용하면 모든 컨트롤러에서 발생하는 예외를 중앙에서 한꺼번에 처리를 할 수 있다.
```java
@Slf4j
@ControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 잡아서 처리
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // HttpRequestMethodNotSupportedException 예외를 잡아서 처리
    protected ResponseEntity<ErrorResponse> handle (HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException.class", e);
        return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(BusinessBaseException.class)
    protected ResponseEntity<ErrorResponse> handle(BusinessBaseException e) {
        log.error("BusinessException", e);
        return createErrorResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handle(Exception e) {
        e.printStackTrace();
        log.error("Exception", e);
        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }

// import 가 정확하게 되엇는지 확인하기!
    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return new ResponseEntity<>(
                ErrorResponse.of(errorCode),errorCode
                .getStatus());

    }
}
```
- @ExceptionHandler 애너테이션을 사용해 특정 예외 상황에 대한 처리를  정의할 수 있다.
- 에를 들어 HttpRequestMethodNotSupportedException 예외는 handle(HttpRequestMethodNotSupportedException e) 메서드로 예외를 처리한다. HttpRequestMethodNotSupportedException 예외는 지원하지 않은 HTTP 메서드를 호출하면 발생하는 예외 이다.
- 이 예외가 발생하면 405 응답 코드와 함께 "잘못된 HTTP 메서드를 호출했다." 라는 메시지를 보내준다.
- 이렇게 하면 스프링이 동작하며 자체적으로 발생하는 예외를 @ExceptionHandler에서 잡아 적절한 ErrorResponse로 변환하여 일관성 있는 예외 처리를 할 수 있다.
- 이외에도 BusinessBaseException 예외는 예외를 던질 때 전달 받는 ErrorCode를 바탕으로 ErrorResponse를 만들고, 여기에 정의한 예외가 아니라면 Exception을 잡는 핸들러에 걸리므로 500 응답 코드와 함께 "서버 에러가 발생했습니다." 라는 메시지를 보내준다. 테스트 코드를 통해 실제로 그런지 확인해 볼까요?

### 06 단계
- BlogApiControllerTest.java 파일을 열어 다음과 같이 테스트 코드를 작성한다.
```java
    @DisplayName("findArticle: 잘못된  HTTP 메서드로 아티클을 조회하려고 하면 조회에 실패한다.")
    @Test
    public void invalidHttpMethod() throws Exception {
        // given
        final String url = "/api/articles/{id}";



        // when
        final ResultActions resultActions = mockMvc.perform(post(url, 1));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.message").value(ErrorCode.METHOD_NOT_ALLOWED.getMessage()))
    }
```
- 위 테스트 코드는 GET 요청을 처리하는 컨트롤러만 있는 URL에 HttpRequestMethodNotSupportedException 예외가 발생할 POST 요청을 보낸다.
- 테스트 코드를 실행하면 실제로 METHOD_NOT_ALLOWED 에러 코드에 정의한 상태 코드인 405 응답과 에러 메시지를 보내준다.
- ![06단계.png](appendB%2F06%EB%8B%A8%EA%B3%84.png)
- 테스트는 잘 통과한다. 검증문에 andDo(print()) 라는 내용을 작성했는데 이 코드를 추가하면 실제 응답이 어떻게 나오는지 콘솔 로그에서 확인 할 수가 있다. 로그 아래 쯤에 다음과 같은 로그를 확인 할 수가 있다.
- ![06단계 2.png](appendB%2F06%EB%8B%A8%EA%B3%84%202.png)
- 그럼 이제 블로그 조회 로직의 예외도 바꿔보도록 하자. 그전에 지금은 어떤 응답이 오고 있는지 호가인하기 위해 테스트 코드를 먼저 작성해보겠다.

### 07 단계
- BlogApiControllerTest.java 파일을 열어 다음 테스트 코드를 이어서 작성하고 테스트 코드를 실행한다.
```java
    @DisplayName("findArticle: 존재하지 않는 아티클을 조회하려고 하면 조회에 실패한다.")
    @Test
    public void findArticleInvalidHttpMethod() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        final long invalidId = 1;


        // when
        final ResultActions resultActions = mockMvc.perform(get(url, invalidId));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.message").value(ErrorCode.ARTICLE_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.ARTICLE_NOT_FOUND.getCode()));
    }
```
- ![07단계 2.png](appendB%2F07%EB%8B%A8%EA%B3%84%202.png)
- 이번에는 테스트가 실패했다. 이 이유는 블로그 글이 조회되자 않아 IllegalArgumentException 을 반환하고 있는데 IllegalArgumentException 을 ExceptionHandler에 정의하지 않았기 때문이다.
- 지금 상위 클래스인 Exception이 발생했을 때 처리하는 모습을 보여준다. 즉 , 응답 코드는 500, 에러 메시지는 "서버 에러가 발생 했습니다."가 나온다.

### 08 단계
- BlogService.java 파일을 열어 다음과 같이 코드를 수정한다.
```java
    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
    }
```
- 그런데 여기서  `reason: Incompatible parameter types in method reference expression` 에러가 발생 했다.

## 여기서 알고 보니 내가 코딩 실수였다.

```java
    public ArticleNotFoundException() {
        super(errorCode.ARTICLE_NOT_FOUND);
    }
```
- 위에 처럼  했어야 했었다.
- 그런데 나는 밑에처럼 ArticleNotFoundException 코딩을 하게되어 에러가 난것이었다.
```java
    public ArticleNotFoundException(ErrorCode errorCode) {
        super(errorCode.ARTICLE_NOT_FOUND);
    }
```



현재 코드에서 **`ArticleNotFoundException` 생성자 호출 시 문제**가 발생하고 있습니다.
구체적인 원인과 해결 방법을 설명드리겠습니다.

---

## 문제 분석

### 1. `orElseThrow()` 사용 시 에러 발생

아래 코드에서:

```java
.orElseThrow(ArticleNotFoundException::new);
```

- `orElseThrow()`는 **`Supplier<? extends Throwable>`**을 요구합니다.
- 즉, **파라미터 없는 생성자**를 참조해야 합니다.

하지만 현재 `ArticleNotFoundException` 클래스는
**`ErrorCode`를 받는 생성자만 존재**합니다.

```java
public ArticleNotFoundException(ErrorCode errorCode) {
    super(errorCode.ARTICLE_NOT_FOUND); // 문제의 핵심!
}
```


---

### 2. 상속 체인 문제

부모 클래스 `NotFoundException`의 생성자를 확인해 보면:

```java
public NotFoundException(ErrorCode errorCode) {
    super(errorCode.NOT_FOUND); // 🔴 여기서 errorCode.NOT_FOUND를 전달
}
```

하지만 `ArticleNotFoundException` 생성자에서
`super(errorCode.ARTICLE_NOT_FOUND)`를 호출하려고 하면,
**`errorCode` 파라미터로 전달된 객체가 `ARTICLE_NOT_FOUND`를 가지고 있다고 보장할 수 없습니다.**

---

## 해결 방법 2가지

### 1. ErrorCode를 직접 전달 (추천 ⭐️)

`ArticleNotFoundException` 생성 시 **명시적으로 `ErrorCode`를 전달**하세요.

```java
// ArticleNotFoundException 수정
public class ArticleNotFoundException extends NotFoundException {
    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND); // ✅ 명시적 전달
    }
}
```

이제 아래 코드가 정상 작동합니다:

```java
.orElseThrow(ArticleNotFoundException::new);
```


---

### 2. 전체 상속 구조 개선

모든 예외 클래스가 **자신의 ErrorCode**를 직접 관리하도록 리팩토링:

```java
// BusinessBaseException 수정
public abstract class BusinessBaseException extends RuntimeException {
    private final ErrorCode errorCode;

    protected BusinessBaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

// NotFoundException 수정
public class NotFoundException extends BusinessBaseException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

// ArticleNotFoundException 수정
public class ArticleNotFoundException extends NotFoundException {
    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND); // ✅ 명확한 ErrorCode 지정
    }
}
```


---

## 최종 코드 적용 후 결과

```java
public Article findById(Long id) {
    return blogRepository.findById(id)
            .orElseThrow(ArticleNotFoundException::new); // 정상 동작
}
```


---

## 추가 설명

- **의존성 주입 원칙**: 각 예외 클래스가 자신의 `ErrorCode`를 알고 있도록 설계해야 합니다.
- **생성자 위임**: 부모 생성자에게 적절한 `ErrorCode`를 명시적으로 전달해야 합니다.
- **확장성**: 새로운 예외 추가 시 동일한 패턴을 따르면 일관성이 유지됩니다.

---

- id에 해당하는 레코드가 없으면 ArticleNotFoundException 예외를 던지도록 수정했다.
- 다시 테스트 코드를 실행하면 테스트가 잘 되고, 메시지도 잘 나온다.
- ![08단계.png](appendB%2F08%EB%8B%A8%EA%B3%84.png)

---
## 그런데 계속 해서 test가 실패 하는 문제가 발생해 gpt에게 문의를 한 결과 

이미지와 로그를 세밀하게 분석해보니 현재 상황은 다음과 같습니다:

```
java.lang.AssertionError: JSON path "$.message" expected:&lt;존재하지 않는 아티클입니다.&gt; but was:&lt;존재하지 않는 엔티티입니다.&gt;
```


## 문제 분석

로그에서 볼 수 있듯이:

- 실제 응답: `{"message":"존재하지 않는 엔티티입니다.","code":"E4"}`
- 기대 응답: `{"message":"존재하지 않는 아티클입니다.","code":"A1"}`

즉, `ARTICLE_NOT_FOUND` 대신 `NOT_FOUND` 에러코드가 사용되고 있습니다.

## 문제 원인

로그를 살펴보면:

```
com.springboot.blog.config.exception.ArticleNotFoundException: 존재하지 않는 엔티티입니다.
```

이것은 `ArticleNotFoundException`이 `NOT_FOUND`의 메시지를 사용하고 있다는 증거입니다. 이런 현상은 다음과 같은 이유로 발생할 수 있습니다:

1. `ArticleNotFoundException` 생성자에서:

```java
public ArticleNotFoundException() {
    super(ErrorCode.ARTICLE_NOT_FOUND); // 수정했지만 다른 문제 있음
}
```

2. `NotFoundException` 생성자에서:

```java
public NotFoundException(ErrorCode errorCode) {
    // 여기서 errorCode 파라미터가 무시되고 항상 NOT_FOUND를 사용할 수 있음
    super(ErrorCode.NOT_FOUND); // 문제 부분!
}
```


## 해결 방법

1. **NotFoundException 클래스 수정**:

```java
public class NotFoundException extends BusinessBaseException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode); // 전달받은 errorCode를 그대로 사용
    }
}
```

2. **모든 예외 클래스 검사**:
   모든 예외 클래스의 생성자를 검사하여 상위 클래스에 올바른 ErrorCode를 전달하는지 확인하세요.
3. **디버깅을 통한 추적**:
  - 테스트를 디버그 모드로 실행하고
  - `ArticleNotFoundException` 생성 시점의 변수 값을 확인하세요
  - `errorCode` 값이 실제로 `ARTICLE_NOT_FOUND`인지 확인하세요

## 핵심 수정 포인트

`NotFoundException` 클래스에서 `errorCode` 파라미터를 받는 생성자가 파라미터를 사용하지 않고 `NOT_FOUND`를 하드코딩하고 있을 가능성이 매우 높습니다. 이 부분을 확인하고 수정하세요.

```java
// 수정 전 (문제의 원인)
public NotFoundException(ErrorCode errorCode) {
    super(ErrorCode.NOT_FOUND); // 여기가 문제!
}

// 수정 후 (해결책)
public NotFoundException(ErrorCode errorCode) {
    super(errorCode); // 전달받은 errorCode 사용
}
```

## 이 부분만 수정하면 테스트가 통과할 수가 있었다.

<div style="text-align: center">⁂</div>

[^1]: https://pplx-res.cloudinary.com/image/private/user_uploads/mUluvPGdMYHReqz/image.jpg

[^2]: https://ppl-ai-file-upload.s3.amazonaws.com/web/direct-files/attachments/64535424/e270dacc-347d-4a79-93ef-85c2174989e0/paste-2.txt

[^3]: https://www.mscharhag.com/page/2

[^4]: https://stackoverflow.com/questions/26483585/spring-boot-custom-exception-within-an-rest-service

[^5]: https://goldenrabbit.co.kr/2024/04/03/spring-스프링-부트-예외-처리-가이드/

[^6]: https://stackoverflow.com/questions/69790033/spring-boot-junit-tests-fail-with-status-expected200-but-was404

[^7]: https://bbeomgeun.tistory.com/172

[^8]: https://w97ww.tistory.com/74

[^9]: https://stackoverflow.com/questions/56586147/globalexceptionhandler-always-returns-500

[^10]: https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc

[^11]: https://velog.io/@letsdev/Spring-예외-처리-쉽게-관심사-나누기-Global-Exception-HandlerController-Advice

[^12]: https://dev.to/noelopez/spring-rest-exception-handling-problem-details-2hkj

[^13]: https://mangkyu.tistory.com/204

[^14]: https://dzone.com/articles/global-exception-handling-using-spring-restcontrol

[^15]: https://choincnp.tistory.com/54

[^16]: https://stackoverflow.com/questions/41041346/exceptionhandling-not-found

[^17]: https://stackoverflow.com/questions/72480157/error-globalexceptionhandler-error-401-doesnt-show-error-details

[^18]: https://stackoverflow.com/questions/19422366/java-uncaught-global-exception-handler

[^19]: https://stackoverflow.com/questions/55308053/spring-globalexceptionhandler-java-lang-illegalstateexception-could-not-resol/55308054

[^20]: https://github.com/spring-projects/spring-framework/issues/23622

[^21]: https://stackoverflow.com/questions/43630862/spring-junit-test-jsonpath-for-integer-not-matching

[^22]: https://github.com/spring-projects/spring-boot/issues/5574

[^23]: https://sjparkk-dev1og.tistory.com/140

[^24]: https://codingnomads.com/api-testing-mockmvc-jsonpath-example

[^25]: https://www.inflearn.com/vi/community/questions/1390790/rest-api-개발-중-비즈니스-로직-적용-부분의-json-에러?focusComment=369556

[^26]: https://www.baeldung.com/spring-mvc-test-exceptions

[^27]: https://chordplaylist.tistory.com/223

[^28]: https://jehuipark.github.io/spring/boot-2-2-x-mock-mvc-encoding-issue

[^29]: https://github.com/nielsutrecht/controller-advice-exception-handler/blob/master/my-service/src/test/java/com/nibado/example/errorhandlers/service/controller/UserControllerTest.java

[^30]: https://dzone.com/articles/rest-endpoint-testing-with-mockmvc

[^31]: https://dgjinsu.tistory.com/74

[^32]: https://kdyspring.tistory.com/45

[^33]: https://www.mscharhag.com/spring/rest-api-error-messages

[^34]: https://velog.io/@rungoat/SpringBoot-Custom-Validation-Exception-처리

[^35]: https://dev-allday.tistory.com/77

[^36]: https://bcp0109.tistory.com/303

[^37]: https://www.baeldung.com/kotlin/spring-rest-error-handling

[^38]: https://stackoverflow.com/questions/29689742/how-to-globally-handle-404-exception-by-returning-a-customized-error-page-in-spr

[^39]: https://learn.microsoft.com/en-us/aspnet/core/fundamentals/error-handling?view=aspnetcore-9.0

[^40]: https://adjh54.tistory.com/79

[^41]: https://dev.to/anupam_tarai_3250344e48cd/enhance-exception-handling-and-implement-global-exception-handler-for-usernotfoundexception-22ap

[^42]: https://velog.io/@sjkimplus09/Error-Handling

[^43]: https://stackoverflow.com/questions/19138255/status-expected200-but-was404-in-spring-test

[^44]: https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html

[^45]: https://stackoverflow.com/questions/38749335/testing-an-error-response-with-mockmvc-and-jsonpath

[^46]: https://www.codementor.io/@marcinpiczkowski/json-conversion-errors-with-spring-mockmvc-o2vmtb42y

[^47]: https://github.com/spring-projects/spring-framework/issues/25480

[^48]: https://programmerfriend.com/biggest-antipattern-webmvc-tests/

[^49]: https://jypark1111.tistory.com/186

