##### 항해 플러스 [ 2주차 과제 ] 특강 신청 서비스

# 요구 사항 

```
💡 아래 명세를 잘 읽어보고, 서버를 구현합니다.

📌 Description

- `특강 신청 서비스`를 구현해 봅니다.
- 항해 플러스 토요일 특강을 신청할 수 있는 서비스를 개발합니다.
- 특강 신청 및 신청자 목록 관리를 RDBMS를 이용해 관리할 방법을 고민합니다.

📌 Requirements

- 아래 2가지 API 를 구현합니다.
   - 특강 신청 API
   - 특강 신청 여부 조회 API
- 각 기능 및 제약 사항에 대해 단위 테스트를 반드시 하나 이상 작성하도록 합니다.
- 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 작성하도록 합니다.
- 동시성 이슈를 고려하여 구현합니다.

📌 API Specs

1️⃣(핵심) 특강 신청 API POST /lectures/apply

- 특정 userId 로 선착순으로 제공되는 특강을 신청하는 API 를 작성합니다.
- 동일한 신청자는 한 번의 수강 신청만 성공할 수 있습니다.
- 특강은 `4월 20일 토요일 1시` 에 열리며, 선착순 30명만 신청 가능합니다.
- 이미 신청자가 30명이 초과되면 이후 신청자는 요청을 실패합니다.
- 어떤 유저가 특강을 신청했는지 히스토리를 저장해야한다.

2️⃣(기본) 특강 신청 완료 여부 조회 API GET /lectures/application/{userId}

- 특정 userId 로 특강 신청 완료 여부를 조회하는 API 를 작성합니다.
- 특강 신청에 성공한 사용자는 성공했음을, 특강 등록자 명단에 없는 사용자는 실패했음을 반환합니다. (true, false)


📌 심화 과제

3️⃣(필수) 특강 선택 API GET /lectures

- 단 한번의 특강을 위한 것이 아닌 날짜별로 특강이 존재할 수 있는 범용적인 서비스로 변화시켜 봅니다.
- 이를 수용하기 위해, 특강 엔티티의 경우 기존의 설계에서 변경되어야 합니다.
- 특강의 정원은 30명으로 고정이며, 사용자는 각 특강에 신청하기전 목록을 조회해볼 수 있어야 합니다.
   - 추가로 정원이 특강마다 다르다면 어떻게 처리할것인가..? 고민해 보셔라~

💡 KEY POINT

- 정확하게 30명의 사용자에게만 특강을 제공할 방법을 고민해 봅니다.
- 같은 사용자에게 여러 번의 특강 슬롯이 제공되지 않도록 제한할 방법을 고민해 봅니다.
```


# 구상

### 총괄 시나리오

1. 사용자는 특강 신청 페이지에 접속한다.
2. 클라이언트는 서버에서 현재 특강 목록을 조회한다.
3. 서버는 특정 시간 전에 특강 신청을 막는다.
4. 클라이언트는 특정 시간이 되면 특강 신청 버튼을 활성화한다.
5. 사용자가 특강 신청 버튼을 클릭하면 클라이언트는 서버에 신청 요청을 보낸다.
6. 서버는 신청 요청을 처리하고, 결과를 클라이언트에 응답한다.

### 세부 시나리오

#### 1. 특강 신청 페이지 접속
- 사용자는 특강 신청 웹페이지에 접속하여 현재 제공되는 특강 목록을 확인한다.
- 클라이언트는 서버로부터 특강 목록을 가져와 사용자에게 표시한다.

#### 2. 특강 목록 조회
- 클라이언트는 서버에 `/lectures` API를 호출하여 현재 제공되는 모든 특강 목록을 조회한다.
- 서버는 DB에서 모든 특강 정보를 조회하여 클라이언트에 반환한다.
- 클라이언트는 반환된 특강 목록을 사용자에게 표시한다.

#### 3. 특강 신청 시간 제한
- 서버는 특강의 시작 시간(`4월 20일 토요일 1시`) 전에 특강 신청을 막는다.
- 클라이언트는 특강 시작 시간 전까지 신청 버튼을 비활성화 상태로 유지한다.
- 클라이언트는 특강 시작 시간이 되면 신청 버튼을 활성화한다.

#### 4. 특강 신청
- 특강 신청 시간이 되면 사용자는 신청 버튼을 클릭할 수 있다.
- 클라이언트는 사용자가 신청 버튼을 클릭할 때, `/lectures/apply` API를 호출하여 신청 요청을 보낸다.
- 서버는 신청 요청을 처리하여 다음과 같은 로직을 수행한다:
   - 신청자가 30명을 초과하는지 확인한다.
   - 동일한 사용자가 이미 신청했는지 확인한다.
   - 신청이 가능하면 신청 정보를 DB에 저장하고, 현재 참가자 수를 업데이트한다.
   - 신청이 완료되면 클라이언트에 성공 응답을 반환한다.
   - 신청자가 30명을 초과하거나 이미 신청한 사용자인 경우, 클라이언트에 실패 응답을 반환한다.

#### 5. 신청 결과 응답
- 클라이언트는 서버로부터 신청 결과 응답을 받아 사용자에게 표시한다.
- 성공적으로 신청된 경우, 사용자는 성공 메시지를 확인하고 신청 목록이 갱신된다.
- 신청이 실패한 경우, 사용자는 실패 이유를 확인한다.

#### 6. 범용적인 특강 서비스
- 사용자는 특정 날짜에 열리는 여러 특강 목록을 조회할 수 있다.
- 클라이언트는 서버에 `/lectures?date={date}` 와 같은 검색 파라미터가 추가된 API를 호출하여 특정 날짜에 열리는 특강 목록을 조회한다.
- 서버는 해당 날짜에 열리는 모든 특강 정보를 DB에서 조회하여 클라이언트에 반환한다.
- 클라이언트는 반환된 특강 목록을 사용자에게 표시한다.
- 각 특강의 정원이 30명으로 고정되어 있지만, 특정 특강은 다른 정원을 가질 수 있다.
- 특강의 정원이 다를 경우, 서버는 각 특강별로 최대 정원 수를 저장하고 관리한다.
- 클라이언트는 각 특강의 현재 신청자 수와 최대 정원 수를 사용자에게 표시한다.


### ERD 


![lecture-lectureapplication-erd.png](document%2Ferd%2Flecture-lectureapplication-erd.png)





# 기술적 고민과 결정들

## 1. 대체키 사용

### 배경

현업에서 식별자의 노출 문제를 경험하면서 대체키의 사용을 고려하게 되었다. 

서버에서 사용하는 엔티티의 식별자가 클라이언트에게 노출되는 문제가 있다. 

예를 들어, 특정 사용자의 정보에 접근하기 위해 URL에 사용자의 고유 식별자를 포함시킬 때가 있다. 아래와 같은 URL을 생각해보자. 

```
https://example.com/users/12345
```

여기서 `12345`는 사용자의 고유 식별자로, 클라이언트에게 그대로 노출된다. 당연하겠지만 보안에 있어서 크리티컬한 잠재적 위험성을 갖는다고 생각했다. 

문제는 이미 완성된 시스템에서 식별자를 대체키로 변경하는 것에는 많은 리스크가 따른다는 점이었다.     

식별자 노출 문제를 해결하기 위해 클라이언트와의 통신에서 암호화 방식도 고려했지만 비용이 많이 들어 쉽지 않았다. 

이런 경험을 하면서 대체키 도입의 필요성을 느꼈고, 이번 프로젝트에 처음으로 사용해보기로 했다.

### 방법

프로젝트에서 구현한 특강과 신청 엔티티에서 대체키를 사용했다. 



```java
@Entity
public class Lecture {

    @Id
    private String lectureId;

    @Column(nullable = false, unique = true)
    private String lectureExternalId;

	// ... 
    
    @PrePersist
    private void onCreate() {
        this.lectureExternalId = UUID.randomUUID().toString().substring(0, 12);
    }
}
```

`Lecture` 엔티티에서, `@PrePersist` 메서드인 `onCreate()`를 통해 `lectureExternalId`가 생성된다. 

이때 UUID를 사용하여 무작위로 생성된 문자열의 일부를 대체키로 사용한다. 대체키 초기화 방식은 이와 같이 `@PrePersist`를 활용해 엔티티의 처음 저장 시점에 자동 생성되도록 할 수도 있고, 서비스 코드에서 `PK` 생성 후 세팅하듯이 주입해도 될 것이다.

UUID는 성능상 좋지는 않다. 고유성을 보장하는 방식으로는 다른 대안들도 고려될 수 있다. 
예를 들어, 데이터베이스 시퀀스(sequence)를 활용하거나, 타임스탬프와 랜덤 숫자를 결합하여 고유 식별자를 생성하는 방법이 있다. 
또 다른 방법으로는 해시 함수(예: SHA-256)를 사용해 고유한 키를 생성하는 것도 가능하다. 또 트위터에서 만든 라이브러리인 `Snowflake`를 이용하는 방법도 있다. 

이번 프로젝트에서는 간편함에 중점을 두고 UUID를 사용했다. 

이렇게 생성한 대체키를 기존의 식별자 대신에 클라이언트에게 넘겨준다. 예를 들어 생성 이후 값을 반환하는 응답은 다음과 같다. 

```java

public record LectureRegisterationResponse(
	String lectureExternalId,
	Long version,
	String title,
	String description,
    //... 
```

마찬가지로 특강을 조회할 때도 `lectureExternalId`를 사용하여 특강을 조회한다. 

```java
    @Override
    @Transactional(readOnly = true)
    public LectureGeneralInfo searchSingleLectureById(String externalId) {
        return LectureGeneralInfo.from(lectureRepository.findByLectureExternalId(externalId).orElseThrow(LectureNotFoundException::new));
    }
```


#### 소회

대체키를 사용하면서 몇 가지 불편함을 느꼈다. 

예를 들어, 내부 통신에서 엔티티 간의 참조에서도 대체키를 사용할지, PK를 사용할지에 대한 고민이 생긴다. 다음과 같이 History 객체는 연관 객체로서 Lecture와 LectureApplication을 참조해야 한다. 이때 PK로 참조해야 할까? 대체키로 참조해야 할까?


```java

@Entity
@Getter
@NoArgsConstructor
public class LectureApplicationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lectureExternalId;  // ?? 
    private String lectureApplicationId; // ?? 
	//...
```

분명 내부 엔티티 간의 참조에서는 PK를 사용하는 것이 성능과 코드의 단순성 측면에서 유리할 수 있다. 그런데 그렇게 되면 이런 문제가 생긴다. 

만약 해당 객체를 테스트 코드에서 사용하려면 어떻게 해야할까? 예를 들어 해당 객체를 반환받고, 그 내부의 탐색을 통해서 특강 혹은 특강 신청 정보를 얻은 뒤 이를 기반으로 다시 조회를 해야한다면? PK를 사용했다면 이미 PK가 노출되었다는 점에서, 그리고 PK를 알아도 PK로 검색이 되지 않는다는 점에서 불편함을 겪는다.

이렇게 관리 포인트가 늘어나면서 코드 복잡성을 증대된다. 

그럼에도 PK 자체가 클라이언트에게 노출될 일은 없다는 측면에서 더 안정성 있는 시스템이 되는 것만은 확실할 것이다. 



## 2. 파사드 레이어의 필요성에 대한 고찰

### 서비스 간 순환 참조 문제

이런 예시를 들어보자. 한 어플리케이션에서 주문과 결제 도메인이 다음과 같이 존재한다고 해보자.

```
com.example
├── order
│   ├── presentation
│   │   └── OrderController.java
│   ├── application
│   │   └── OrderService.java
│   ├── business
│   │   ├── Order.java
│   │   └── OrderRepository.java
│   └── infrastructure
│       ├── OrderJpaRepository.java
│       └── OrderEntity.java
└── payment
    ├── presentation
    │   └── PaymentController.java
    ├── application
    │   └── PaymentService.java
    ├── business
    │   ├── Payment.java
    │   └── PaymentRepository.java
    └── infrastructure
        ├── PaymentJpaRepository.java
        └── PaymentEntity.java
```


이때 이런 상황이 발생하는 것이다.

1. **결제 서비스에서 주문 서비스 참조:**
    - 결제 서비스를 구현할 때, 사용자의 결제 내역을 처리하기 위해 주문 정보가 필요하다. 
    - 이를 위해 `PaymentService`에서 `OrderService`를 참조하여 주문 정보를 가져온다.
      ```java
      public class PaymentService {
          private final OrderService orderService;
		  
          public void processPayment(Payment payment) {
              Order order = orderService.getOrder(payment.getOrderId());
              // 결제 처리 로직
          }
      }
      ```

2. **주문 서비스에서 결제 서비스 참조:**
    - 반대로, 주문 서비스를 구현할 때, 주문을 완료하기 위해 결제 정보를 확인해야 한다. 
    - 이를 위해 `OrderService`에서 `PaymentService`를 참조하여 결제 상태를 확인한다.
    - 예시:
      ```java
      public class OrderService {
          private final PaymentService paymentService;
		  
          public void completeOrder(Order order) {
              Payment payment = paymentService.getPayment(order.getPaymentId());
              // 주문 완료 로직
          }
      }
      ```

이처럼 `PaymentService`와 `OrderService`가 서로를 참조하는 순환 참조 문제가 발생한다. 

### 파사드의 역할

이래서 이 둘을 오케스트레이션하기 위해 등장하는 것이 파사드이다. 
파사드는 복잡한 시스템을 단순화하기 위해 고수준의 인터페이스를 제공하는 일종의 디자인 패턴이기도 하다.
파사드를 두어 서비스 간에 서로 직접 참조하지 않고, 파사드를 통해 간접적으로 상호작용하게 함으로써 순환 참조 문제를 해결할 수 있는 것이다.

   ```java
   public class OrderPaymentFacade {
       private final OrderService orderService;
       private final PaymentService paymentService;

       public OrderPaymentFacade(OrderService orderService, PaymentService paymentService) {
           this.orderService = orderService;
           this.paymentService = paymentService;
       }

       public void processOrderPayment(Order order, Payment payment) {
           orderService.completeOrder(order);
           paymentService.processPayment(payment);
       }
   }
   ```

파사드를 사용함으로써 `OrderService`와 `PaymentService`는 서로 직접 참조하지 않고, `OrderPaymentFacade`를 통해 상호작용한다. 
이렇게 하면 의존성이 문제가 해결된다.


### 단점은?

파사드의 단점으로는 싱크홀 안티패턴과 트랜잭션 문제를 들 수 있다. 
잘못 사용하면 단순히 데이터만 전송하는 계층이 되어버린다. 예를 들어 다음과 같이 별 다른 비즈니스 로직을 수행하지 않고 데이터를 전송하기만 할 수 있다. 

```java
@Facade
@RequiredArgsConstructor
public class LectureApplyFacade {
	private final LectureApplyService applyService;

	public LectureApplyResponse applyForLecture(LectureApplyRequest request) {
		return LectureApplyResponse.from(applyService.applyForLecture(request.toCommand()));
	}
}
```

또 다른 문제는 트랜잭션을 보장하지 못한다는 것이다. 

파사드 레이어를 사용할 때 여러 서비스를 참조하면서 데이터를 조합하는 경우, 트랜잭션 관리는 어떻게 할 것인가? 
트랜잭션을 파사드 레이어에 달아야 할까? 
그렇게 하면 결국 서비스 레이어에 트랜잭션을 붙인 것과 다를 바 없으며, 이는 다시 순환 참조 문제를 일으킬 수 있다.

다음과 같은 예시를 살펴보자. 

```java
@Facade
@RequiredArgsConstructor
public class OrderPaymentFacade {
    private final OrderService orderService;
    private final PaymentService paymentService;

    public void processOrderPayment(Order order, Payment payment) {
        orderService.completeOrder(order);
        paymentService.processPayment(payment);
    }
}
```

위 코드에서 `OrderPaymentFacade`는 단순히 `OrderService`와 `PaymentService`의 메서드를 호출하고 있다. 트랜잭션 문제가 발생할 수 있는 상황은 다음과 같다.

1. `orderService.completeOrder(order)` 호출 후 성공.
2. `paymentService.processPayment(payment)` 호출 시 예외 발생.

이 경우 주문은 완료되었지만 결제는 실패하게 되어 데이터 일관성 문제가 발생한다. 

**트랜잭션 관리: 보상**

보상 트랜잭션과 같은 보정 로직을 별도로 구현해야 한다.

```java
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class OrderPaymentFacade {
    private final OrderService orderService;
    private final PaymentService paymentService;

    public void processOrderPayment(Order order, Payment payment) {
        try {
            orderService.completeOrder(order);
            paymentService.processPayment(payment);
        } catch (Exception e) {
            // 예외 발생 시 보상 트랜잭션 수행
            handleCompensation(order, payment);
        }
    }

    private void handleCompensation(Order order, Payment payment) {
        // 주문을 취소하거나, 결제 취소 로직을 수행
        if (orderService.isOrderCompleted(order)) {
            orderService.cancelOrder(order);
        }
        if (paymentService.isPaymentProcessed(payment)) {
            paymentService.refundPayment(payment);
        }
    }
}
```



### 조금 더 넓게 본다면

파사드는 서비스를 조합하고 유연하게 사용하기에 유용할 수 있다. 
하지만 이 문제는 단순히 패키지 구조에 국한되지 않는다. 
예를 들어, 마이크로서비스 아키텍처에서 결제 서비스와 주문 서비스를 각각 별도 애플리케이션으로 분리해도 발생할 수 있는 문제다.

각 서비스가 서로의 정보를 필요로 할 때, API 통신을 어떻게 할 것인가? 
이 과정에서 발생하는 데이터 정합성 이슈는 어떻게 해결할 것인가? 
이러한 상황은 서로가 서로를 필요로 하는 설계 자체에 결함이 있을 수 있다는 점을 시사한다.

### 다양한 접근들

이러한 문제를 피하기 위해서는, 가능한 한 서비스를 독립적으로 설계하는 것이 좋다. 그러나 불가피하게 상호 의존이 필요한 경우들이 존재한다. 그래서 다음과 같은 해결책들이 등장한다.

1. **오케스트레이션 서비스**: 별도의 오케스트레이션 서비스 또는 BFF(Backend for Frontend)를 구성하여 서비스 간의 상호작용을 관리한다.
2. **메시지 큐 사용**: 메시지 큐를 사용하여 서비스 간의 비동기 통신을 통해 데이터 정합성을 유지한다. 

### 요점은...

파사드의 필요성은 프로젝트별로, 애플리케이션별로 다를 수 있다. 
특정 상황에서는 결제 서비스에 파사드를 구현할 수 있고, 주문 서비스에는 구현하지 않을 수도 있다. 
반대로, 일관성을 위해 모든 서비스에 파사드를 구현할 수도 있다.

중요한 것은 각각의 선택에 따른 합당한 이유가 있어야 하며, 선택에 따른 트레이드오프를 인지하는 것이다. 
파사드가 만능 해결책이 아니며, 적절히 사용해야만 진정한 효과를 볼 수 있다.

다시 말해, 파사드를 사용한다고 해서 모든 문제가 해결되는 것은 아니며, 상황에 맞는 올바른 사용이 중요하다. 
결국에는 파사드를 어떻게 설계하고 사용하는지가 핵심이다.






## 3. 사용자 요청 이력 저장하기 챌린지

특강 신청 서비스 API Specs의 요구 사항에 다음과 같은 주문이 있었다.

> 어떤 유저가 특강을 신청했는지 히스토리를 저장해야한다.

이에 대해서 모든 이력을 저장해야 하는 것으로 해석했다. 예를 들어 50개의 요청이 왔을 때, 정상 동작을 한다면 20개는 실패하고 30개는 성공할 것이다. 여기서 30개의 성공에 대한 이력 뿐만 아니라 20개도 저장하여 이력 역시 50개 남아야 한다.

어떻게 구현해야 할까? 챌린지라고 느껴졌다!

사용자 요청 이력을 저장하는 방법에 대해 두 가지 접근 방식을 고민해보았다. 

1. 모든 이력 저장하기
2. 성공한 이력 저장하기

결론적으로는 모든 `시도 요청`에 대해서 신청 로직 처리 전 비동기로 이력을 생성하고, `성공한 이력`에 대해서 동기적으로 생성한다. 

플로우를 설명하기에 앞서 먼저 정의한 이력부터 살펴보자. 


### 이력 속성

이력 엔티티를 다음과 같이 구성했다. 

```java

@Entity
public class LectureApplicationHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Version
	private Long version;
	private String userId;
	private String lectureExternalId;
	private String lectureApplicationId;
	private boolean success;
	private LocalDateTime appliedAt;
	private LocalDateTime requestAt;
}
```


### 이력 생성 전체 과정 

![lecture-history-process-1.png](document%2Flecture-history-process-1.png)

`LectureApplyService`의 `applyForLecture`에 요청이 들어오면 `@LogLectureApplyTry` 애노테이션에 대한 Aspect가 동작하여 요청을 가로챈다. 

```java
@Override
	@Transactional
	@LogLectureApplyTry
	public LectureApplyInfo applyForLecture(LectureApplyCommand command) {
		Lecture lecture = lectureRepository.findByLectureExternalId(command.lectureExternalId()).orElseThrow(LectureNotFoundException::new);

		lectureApplyValidator.validate(command, lecture);

		LectureApplication lectureApplication = command.toEntity()
			.withPk(lectureApplicationPkGenerator.generate(lecture.getTitle()))
			.withSk(randomUUID().toString().substring(0,12))
			.withLecture(lecture)
			.withAppliedAt(now())
			.asApplied()
			.publish();

		return LectureApplyInfo.from(lectureApplicationRepository.save(lectureApplication));
	}
```

즉, Aspect는 모든 요청에 대해 `LectureApplyTryEvent`를 발행한다. 

```java
@Aspect
@Component
@RequiredArgsConstructor
public class LectureApplicationAspect {


	private final ApplicationEventPublisher eventPublisher;

	@Before("@annotation(io.hhpluslectureapplicationsystem.common.annotation.LogLectureApplyTry)")
	public void logLectureApplyTry(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		LectureApplyCommand command = (LectureApplyCommand) args[0];
		eventPublisher.publishEvent(new LectureApplyTryEvent(this, command.userId(), command.lectureExternalId(), command.requestAt()));
	}
}
```

여기서 발행은 비동기적으로 실행된다. 

이후 서비스에서 비즈니스 로직에 따라 신청에 대한 처리가 이루어지고, 만약 신청이 가능하여 성공된다면 이번에는 `AbstractAggregateRoot`를 활용해 도메인 이벤트 방식으로 이벤트를 발행한다. 


```java
@Entity
public class LectureApplication extends AbstractAggregateRoot<LectureApplication> {

	//... 
    
	public LectureApplication publish(){
		this.registerEvent(new LectureApplySuccessEvent(
			this,
			this.userId,
			this.getLectureExternalId(),
			this.lectureApplicationId,
			this.appliedAt,
			this.requestAt
		));
		return this;
	}
```

이때의 이벤트는 다음과 같이 `TransactionalEventListener`의 `TransactionPhase.BEFORE_COMMIT` 옵션으로 커밋 전에 한 트랜잭션 내에서 수행된다.

```java
	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void handleLectureApplyEvent(LectureApplySuccessEvent event) {
		lectureApplyHistoryFactory.upsertSuccessEvent(event);
	}
```

이러한 구현에서 고민한 몇가지 결정사항들에 대해서 살펴보자. 


### 왜 시도 이력은 비동기적으로 처리하고 성공 이력은 트랜잭션을 유지하면서 동기적으로 처리했나?  

특강 신청 시도 이력은 단순히 시도한 사실을 기록하기 위한 것으로, 비동기적으로 처리함으로써 메인 비즈니스 로직에 지장을 주지 않고 빠르게 처리할 수 있다. 
반면에, 성공 이력은 신청이 실제로 성공했음을 의미하므로, 신청 처리가 완료된 후 같은 트랜잭션 내에서 동기적으로 처리하여 데이터의 일관성을 보장해야 한다. 
이는 성공한 신청과 그에 대한 성공 이력이 반드시 함께 저장되도록 하기 위함이다.

다음과 같은 경우의 수를 탐색해보자. 

| 시도 이력 저장 | 성공 이력 저장 | 설명 |
|----------------|----------------|------|
| O              | O              | 정상적인 신청 처리 및 이력 저장, 데이터 일관성 보장 |
| O              | X              | 신청은 성공했으나 성공 이력이 저장되지 않음, 데이터 일관성 문제 |
| X              | O              | 시도 이력이 저장되지 않고 성공 이력만 저장됨, 데이터 일관성 문제 |
| X              | X              | 모든 처리 실패, 시스템 결함이나 비정상적인 상태 |


이러한 이유로 시도 이력은 비동기적으로 처리하여 메인 비즈니스 로직의 성능에 영향을 주지 않도록 하고, 성공 이력은 동기적으로 처리하여 데이터의 일관성을 보장한다.




### 비동기 처리로 인한 잠재적 이슈 

비동기 처리의 경우, 순서가 보장되지 않아 시도 이력보다 성공 이력이 먼저 저장될 수 있다.

예를 들어 앞선 다이어그램에서 비동기 처리 부분이 다음 그림과 같이 비순차적으로 실행된다면 어떨까? 


![lecture-history-process-2.png](document%2Flecture-history-process-2.png)

이와 같이 성공 이력이 시도 이력보다 먼저 도착하게 된다. 
이로 인해 성공 이벤트가 먼저 처리되고 성공 이력이 저장된 후 시도 이벤트가 나중에 도착할 경우, 성공한 신청임에도 불구하고 시도 이력이 뒤늦게 저장되어 이력 필드인 `success`가 `true`가 아니라 `false`로 기록될 수 있다. 

어떻게 해결할 수 있을까? 


### 저장 로직 세부 구현

이 문제를 해결하기 위해 `SimpleLectureApplyHistoryFactory` 클래스에서는 성공 이벤트와 시도 이벤트를 처리하는 로직을 다음과 같이 구현했다.

성공 이벤트에 대해서는 다음과 같은 두 가지 경우의 수를 고려할 수 있다.
1. 성공 이벤트가 먼저 도달하는 경우: 이력이 존재하지 않을 것이므로 새로 생성하여 저장한다.
2. 시도 이벤트가 먼저 도달하는 경우: 이미 저장된 시도 이력이 존재하므로 해당 이벤트를 조회하여 덮어쓴다.

```java
@Override
@Transactional
public void upsertSuccessEvent(LectureApplySuccessEvent event){
	LectureApplicationHistory history = historyRepository
		.findByUserIdAndLectureExternalIdAndRequestAt(event.getUserId(), event.getLectureExternalId(), event.getRequestAt())
		.map(existingHistory -> existingHistory.updateSuccess(event.isSuccess()))
		.map(existingHistory -> existingHistory.updateLectureApplicationInfoWithSuccessEvent(event))
		.orElseGet(event::toEntity);

	historyRepository.save(history);
}
```

전체 시도 이벤트에 대해서는 다음과 같은 두 가지 경우의 수를 고려할 수 있다.
1. 성공 이벤트가 먼저 도달하는 경우: 이미 이력이 생성되었을 것이므로 조회하여 존재한다면 별도로 저장할 필요 없이 리턴한다.
2. 시도 이벤트가 먼저 도달하는 경우: 언제나 저장한다.

```java
@Override
@Transactional
public void saveTryHistory(LectureApplyTryEvent event) {
	Optional<LectureApplicationHistory> optionalHistory = historyRepository
		.findByUserIdAndLectureExternalIdAndRequestAt(event.getUserId(), event.getLectureExternalId(), event.getRequestAt());

	if (optionalHistory.isPresent()){
		return;
	}

	historyRepository.save(event.toEntity());
}
```



##### 즉,

1. **성공 이벤트가 먼저 도달한 경우**:
    - 성공 이벤트가 도착하면, `LectureApplySuccessEvent`를 통해 성공 이력을 업데이트하거나 새로 생성한다.
    - 시도 이력이 나중에 도착하면, 기존 이력을 확인하고 덮어쓴다.

2. **시도 이벤트가 먼저 도달한 경우**:
    - 시도 이벤트가 도착하면, `LectureApplyTryEvent`를 통해 시도 이력을 저장한다.
    - 성공 이벤트가 나중에 도착하면, 기존 시도 이력을 성공 이력으로 업데이트한다.



### 검증 

기본 시나리오에 대해서 다음과 같이 테스트했다.


![apply-default-cucumber-scenario.png](document%2Fapply-default-cucumber-scenario.png)

Database에 정상적으로 하나의 이력으로 생성되는 것을 볼 수 있다. 

![apply-default-cucumber-scenario-result.png](document%2Fapply-default-cucumber-scenario-result.png)

















# 프로젝트 실행 전 데이터베이스 설정 방법

> Docker가 설치되어 있고 구동중이어야 합니다.

1. 프로젝트를 클론합니다.
    ```sh
    git clone <repository-url>
    cd hhplus-lecture-application-system
    ```

2. 프로젝트 루트 디렉토리에 `.env` 파일을 생성하고 다음 내용을 추가합니다.
    ```env
    SPRING_DATASOURCE_USERNAME=root
    SPRING_DATASOURCE_PASSWORD=1234
    ```

   필요한 경우, 데이터베이스 사용자 이름과 비밀번호를 자신의 환경에 맞게 변경합니다.

3. Docker Compose를 사용하여 데이터베이스를 시작합니다.
    ```sh
    docker-compose up -d
    ```

4. 데이터베이스가 설정되고 컨테이너가 실행 중인지 확인합니다.
    ```sh
    docker ps
    ```

5. Spring Boot 애플리케이션을 실행합니다.
    ```sh
    ./mvnw spring-boot:run
    ```
6. 정상 작동 후 확인

도커가 정상적으로 실행되면 `localhost:3301` 포트와 `3302` 포트로 `Database` 조회시 다음과 같이 확인되어야 합니다. 

![docker-after-database.png](document%2Fdocker-after-database.png)

