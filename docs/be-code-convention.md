## 1. 공통 규칙
### 1.1 기본 스타일
- 기본적으로 [우테코 자바 스타일 가이드](https://github.com/woowacourse/woowacourse-docs/tree/main/styleguide/java)를 따른다.

### 1.2 추가 스타일
- 클래스명 밑 한 줄은 줄바꿈한다.
- 클래스 마지막 줄은 줄바꿈하지 않는다.
- 필드마다 한 줄 씩 줄바꿈한다.
- `static method`는 사용해야 하는 경우, Issue로 올려 미리 논의한다.
- 메서드는 public 먼저 CRUD 순으로, private은 사용하는 순으로 작성한다.
  ```java
  public class Class {

    public static final String NAME = "NAME";

    private static final int AGE = 0;

    public String name;

    private String name;

    private int age;

    public void create() {
        ...
    }

    public void read() {
        ...
    }

    private void create() {
        ...
    }

    private void read() {
        ...
    }
  }
  ```

## 2. 패키지 구조
- 도메인 기준으로 패키지를 분리한다.
- 하나의 도메인에 종속되지 않는 경우 `global` 패키지로 분리한다.

### 2.1 도메인 패키지
- `presentation` : Controller
- `application` : Service, DTO
- `domain` : Entity, Repository
- `infrastructure` : external system

### 2.2 global 패키지
- `common` : BaseEntity
- `config` : Configuration
- `exception` : Exception, ExceptionHandler
- `interceptor` : Interceptor
- `resolver` : Resolver

## 3. 메서드 네이밍
- Controller 메서드명과 Service 메서드명은 통일한다.
- Repository 메서드명은 쿼리 메서드가 아니면 통일한다.

### 3.1 Controller, Service 계층에서 사용하는 CRUD 메서드 네이밍
> 예외를 터뜨리면 find(Optional), 아니면 get
- `createDomain`
- `updateDomain`
- `deleteDomain`
- `getDomain` (단건 조회)
- `findDomain` (예외를 처리해야 할 때 == `Optional` 반환 시)
- `getDomains` (여러건 조회)

## 4. DTO
### 4.1 네이밍 및 사용 범위
- Service와 Controller가 동일한 DTO를 사용한다.
- DTO는 application 레이어에 위치한다.
- `record`를 사용한다.
- 인자마다 한 줄 띄운다.
  ```java
  public record DomainRequest(
      @NotBlank(message = "필수값)
      String name,
  
      @Email(message = "이메일 형식 시켜줘요)
      String email
  ) {
  }
  ```
- request, response 고정, 앞부분은 자유롭게 네이밍한다.
    - request와 response의 스펙이 같아도, DTO는 공용하지 않는다.
    - request와 response 패키지를 분리한다.
- 앞부분 네이밍은 아래 내용을 따른다.
    - 엔티티 안의 모든 필드를 반환할 때는 `DomainResponse`와 같이 네이밍한다.
    - 모든 필드를 반환하지 않는다면, 추후 논의후 네이밍한다.

### 4.2 response DTO 형식
- 안드로이드와 논의하여 수정이 필요하다면 수정한다.

|method|statusCode|return|
|:--:|:--:|:--:|
|post|201|id|
|get|200|형식에 맞게|
|put / patch|200|id|
|delete|204|void|

## 5. Controller Response
- exception일 경우 data는 null로 처리한다.
- controller에서 리턴타입은 ResponseEntity로 고정한다.
- 안드로이드와 논의하여 수정이 필요하다면 수정한다.
  ```java
  @Getter
  @Builder
  public class Response<T> {

    private int statusCode;

    private String message;

    private T data;
  }    
  ```

## 6. 서비스 구조
### 6.1 2개 이상의 서비스 참조
- 서비스는 다른 서비스를 참조하지 못하도록 한다.
- 구체적인 상황이 생긴다면 추후 논의한다.

### 6.2 CQS 패턴 적용
- 모든 도메인에 대하여 Command(CUD)와 Query(R)를 분리한다.
- 이유
    - 트랜잭션의 적용 범위를 클래스 단위로 나눌 수 있다.
    - 추후 분리할 가능성이 높다고 판단하여 리팩터링 리소스를 줄이고자 미리 나눈다.

## 7. Exception
### 7.1 Custom Exception 사용 여부
- 기본적으로 자바에서 제공하는 Exception을 사용하는 것으로 한다.
- 외부 API 연동 등 특수한 상황에서 Custom Exception이 필요하다고 판단될 경우, 이슈에 올려 회의를 통해 결정하는 것으로 한다.

## 8. Annotation
- 기본적으로 짧은 Annotation부터 나열한다.
- 한 줄에 하나의 Annotation만 쓴다.
- AccessLevel과 같은 조건은 롬복에 추가한다.
- equals & hashCode를 재정의할 땐, 롬복을 사용하지 않고 직접 오버라이딩해서 사용한다.

### 8-1. Entity Annotation 예시
```java
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
    
@Entity
@SqlRestriction(value = "deleted_at is NULL")
@SQLDelete(sql = "update member set deleted_at = NOW() where member_id = ?")
public class Member extends TimeStamp {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
```

## 9. 인자
- 메서드 생성 시, 모든 인자에 `final`을 붙인다.
- 컨트롤러, DTO만 가독성을 위해 파라미터 개수가 한 개여도 개행 통일한다.
- 그 외에는 파라미터 개수가 한 개면 개행하지 않는다.

```java
public void registerController(
    final String title
) {
    ...
}

public void register(final String title) {
  ...
}

public void register(
    final String title,
    final String name
) {
    ...
}
```

## 10. 기타
### 10.1 Message Convention
- 모든 메시지의 마지막에는 `.`을 제외한다.

### 10.2 변수
- 불변을 위해 메서드 내 모든 변수에 `final`을 붙인다.

### 10.3 주석
- 주석은 `commit` 하지 않는다.

## 11. 테스트
### 11.1 고전파 ? 런던파 ?
- 고전파 : 슬라이스 테스트 중시
- 런던파 : End To End 중시
- 결론 : `domain`은 슬라이스 테스트, 그 외는 End To End로 테스트한다.

### 11.2 E2E 테스트 설정
- `DirtiesContext`는 사용하지 않는다.
- 대신 테스트마다 데이터 초기화를 수행한다.
- `RestAssured`를 사용해 E2E 테스트한다.
- `List` 조회 API에 한해서만 `size`를 검증한다.
- 나머지 API는 상태코드만 검증한다.

### 11.3 Nested Class
- 테스트 케이스가 많이 나뉘는 경우 부분적으로 사용한다.

### 11.4 테스트 메서드명
- 기본적으로 프로덕션 코드의 메서드명과 동일하게 영어로 작성하고, `Test`를 추가한다.
- `DisplayName` 어노테이션을 활용하여 한글로 부가설명을 작성한다.
- `DisplayName` 문구는 `xx를 하면 예외가 발생한다`, ``xx를 한다`의 형식으로 작성한다.

### 11.5 주석
- 테스트마다 `given, when, then` 주석을 작성한다.
- 분야가 합쳐지는 경우 `-` 으로 구분한다.
  ```java
  void test() {
    // given
    // when
    // then
  }

  void test() {
    // given
    // when - then
  }
  ```

### 11.6 여러 개 테스트
- 여러 개의 테스트를 한 번에 할 때는 `assertSoftly`가 아닌 `assertAll`을 사용한다.
