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

## 총괄 시나리오

1. 사용자는 특강 신청 페이지에 접속한다.
2. 클라이언트는 서버에서 현재 특강 목록을 조회한다.
3. 서버는 특정 시간 전에 특강 신청을 막는다.
4. 클라이언트는 특정 시간이 되면 특강 신청 버튼을 활성화한다.
5. 사용자가 특강 신청 버튼을 클릭하면 클라이언트는 서버에 신청 요청을 보낸다.
6. 서버는 신청 요청을 처리하고, 결과를 클라이언트에 응답한다.

## 세부 시나리오

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


## ERD


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




## 3. 레이어드 아키텍처의 단점 및 클린 아키텍처의 필요성

웹 어플리케이션에서 레이어드 아키텍처는 흔하고 안정적으로 사용되는 구조이다. 비유하자면 마치 국밥과 같다. 많은 개발자들이 선택하는 든든한 구조인 레이어드 아키텍처는 어떤 문제를 해결하기 위해 등장했으며, 또 어떤 단점이 있을까? 그리고 이러한 단점을 극복하기 위한 대안은 무엇일까? 이번 섹션에서는 프로젝트의 패키지 및 아키텍처 구성을 예시와 함께 살펴보며 이러한 질문에 답해보고자 한다.

### 레이어드 아키텍처가 해결해주는 것

레이어드 아키텍처는 소프트웨어 개발에서 기능을 계층별로 분리하여 코드의 모듈화와 유지보수성을 높이는 것을 목표로 한다. 이 아키텍처는 전통적인 모놀리틱 아키텍처의 복잡성을 해결하고자 등장했다. 주요 특징으로는 프레젠테이션 레이어, 비즈니스 로직 레이어, 데이터 접근 레이어로 나누어져 각 계층이 독립적으로 작동하도록 하는 구조를 갖춘다. 다음과 같은 장점을 가진다.

1. **모듈화**: 각 계층이 독립적으로 개발되고 테스트된다. 당연히 코드의 재사용성과 유지보수성이 좋아진다.
2. **의존성 관리**: 의존성이 단방향으로 구성되어 변경의 영향 범위가 좁아지며, 각 계층이 명확한 역할을 가지게 된다.
3. **역할 분리**: UI, 비즈니스 로직, 데이터 접근의 역할이 명확히 분리되어 코드의 가독성과 관리가 용해진다.

레이어드 아키텍처의 가장 큰 특징은 **의존성이 단방향으로 흐른다**는 것이다.

그림으로 살펴보면 다음과 같다. 

![layered-architecture.png](document%2Flayered-architecture.png)

### 레이어드 아키텍처의 단점

하지만 레이어드 아키텍처는 다음과 같은 몇 가지 단점을 갖는다. 

먼저 데이터베이스 중심의 사고에서 도메인 중심의 사고로의 전환 과정을 역사적으로 살펴볼 필요가 있다.

전통적인 데이터베이스 중심의 아키텍처에서는 비즈니스 로직 레이어가 데이터 접근 레이어에 강하게 의존한다. *도메인 주도 설계로 시작하는 마이크로 서비스 개발*에 따르면, "데이터베이스 중심 아키텍처란 특정 관계형 데이터베이스에 의존한 데이터 모델링을 수행한 다음 이 물리 테이블 모델을 중심에 두고 애플리케이션을 구현하기 위한 사고를 하는 방식이다."(85p) 해당 책에서 저자는 "이러한 구조에서 일반적으로 비즈니스 로직은 서비스에 존재해야 한다고 말하지만 서비스에 존재하게 될 로직은 흐름 제어 로직 밖에 없다. 그 밖의 비즈니스 개념과 규칙들은 앞에서 언급한 사례처럼 테이블과 SQL 질의에 존재한다."(85p)고 지적한다.

레이어드 아키텍처가 이러한 문제를 가질까? 

그 주요 이유는 의존성의 방향 때문이다. 레이어드 아키텍처의 가장 큰 특징인 의존성의 방향을 생각해보자. 레이어드 아키텍처에서는 의존성이 프레젠테이션 -> 비즈니스 -> 데이터 접근 레이어로 단방향으로 흐른다. 이러한 의존성 구조는 데이터 접근 레이어에 대한 강한 결합을 초래한다.

에릭 에반스는 *도메인 주도 설계*에서 도메인 모델은 데이터베이스에 종속되어서는 안 된다"라고 강조하고 있다. 왜 그럴까? 데이터베이스 스키마가 변경되면 상위 레이어들에도 영향을 미치게 되기 때문이다. 예를 들어, 데이터베이스 스키마의 변경이나 새로운 기술 스택의 도입은 비즈니스 계층의 변경을 초래하고, 결론적으로는 모든 계층에 걸쳐 수정이 필요하게 되어 전체 시스템에 대한 수정을 불가피하게 한다.

이와 같은 문제의 결과로, 레이어드 아키텍처는 **OCP(Open/Closed Principle) 위반** 문제를 가지고 있다. OCP는 객체 지향 설계의 SOLID 원칙 중 하나로, 소프트웨어 개체는 확장에는 열려 있어야 하고 변경에는 닫혀 있어야 한다는 원칙이다. 그러나 레이어드 아키텍처에서는 새로운 기능을 추가하거나 변경할 때 기존 코드를 수정해야 하는 경우가 많아, 이 원칙을 위반하게 된다. 

따라서, 레이어드 아키텍처는 데이터베이스 중심의 사고로 인해 도메인 로직이 데이터베이스에 종속되고 비즈니스 로직이 데이터베이스와 밀접하게 결합되는 문제를 여전히 안고 있다. 이러한 구조적 단점들은 변경의 영향을 크게 받아 시스템의 유연성과 확장성을 저해한다. 이러한 문제를 극복하고 보다 유연하고 확장 가능한 시스템을 설계하기 위해 도메인 중심의 사고로의 전환이 필요하다.



### 클린 아키텍처의 필요성

이러한 레이어드 아키텍처의 단점을 극복하기 위해 등장한 것이 클린 아키텍처이다. 클린 아키텍처는 도메인 중심 설계(DDD, Domain-Driven Design)를 기반으로 하며, 다음과 같은 특징을 가진다. 

1. **도메인 중심 설계**: 비즈니스 로직이 도메인 계층에 집중되어 도메인 지식이 코드에 잘 반영되도록 한다.
2. **의존성 역전**: 인프라와 UI가 도메인 계층에 의존하도록 하여 인프라 변경이 도메인에 영향을 미치지 않도록 한다.
3. **높은 테스트 용이성**: 각 계층이 독립적으로 테스트 가능하여 코드의 품질을 높일 수 있다.
4. **유연한 확장성**: 새로운 기능 추가 시 기존 코드의 변경 없이도 확장이 가능하도록 설계되어 있다.

### 프로젝트의 패키지 및 아키텍처 구성 예시

프로젝트의 패키지 구성을 먼저 살펴보면 다음과 같다. 


```
hhpluslectureapplicationsystem
├── api
│   ├── interfaces
│   │   ├── controller
│   │   │   ├── ApplyController
│   │   │   └── CrudController
│   │   └── argumentresolver
│   │       └── TimestampArgumentResolver
│   ├── application
│   │   ├── facade
│   │   │   ├── ApplyFacade
│   │   │   └── CrudFacade
│   │   └── dto
│   │       ├── Request
│   │       └── Response
├── business
│   ├── model
│   │   ├── dto
│   │   │   ├── Info
│   │   │   └── Command
│   │   ├── entity
│   │   │   ├── Lecture
│   │   │   ├── LectureApplication
│   │   │   ├── LectureApplicationHistory
│   │   │   └── LectureStatus
│   ├── operators
│   │   ├── aspect
│   │   ├── eventhandler
│   │   └── pkgenerator
│   ├── persistence
│   │   ├── Repository
│   │   ├── HistoryFactory
│   │   ├── HistoryResolver
│   │   └── Repository
│   ├── service
│   │   └── impl
│   │       ├── ApplyService
│   │       └── CrudService
│   ├── validation
│   │   ├── specification
│   │   └── validators
├── infrastructure
│   └── persistence
│       ├── CustomRepository
│       ├── CustomRepositoryImpl
│       ├── ApplicationHistoryResolver
│       └── ApplyHistoryFactory
```


이제 각 패키지가 어떻게 아키텍처의 레이어에 대응되는지 살펴보자.

- **interfaces**: 이 패키지는 사용자와의 상호작용을 처리하며 프레젠테이션 레이어(Presentation Layer)에 해당한다. 여기에는 `controller`와 `argumentresolver`가 포함되어 있다.

- **application**: 애플리케이션의 비즈니스 로직을 처리하는 계층으로, 애플리케이션 레이어(Application Layer)에 해당한다. 여기에는 `dto`, `facade`, 그리고 `business`가 포함된다.

- **business**: 도메인 모델과 관련된 모든 비즈니스 로직을 포함하며, 도메인 레이어(Domain Layer)에 해당한다. `model`과 `dto`, `operators`, `validators`, 그리고 `persistence(if)`가 여기에 속한다.

- **infrastructure**: 데이터 접근 및 외부 시스템과의 통신을 처리하며, 인프라스트럭처 레이어(Infrastructure Layer)에 해당한다. 여기에는 `persistence(impl)` 및 기타 구현체들이 포함된다.


보다 추상화된 그림으로 정리해보면 다음과 같다. 


![layered_with_clean.png](document%2Flayered_with_clean.png)


그림에서처럼 주요 내용은 **비즈니스 레이어(도메인 레이어)와 인프라스트럭처 레이어 사이에 의존성이 역전된다는 것**이다. 구현체들(impl)은 인프라스트럭처 레이어에 구현되고, 도메인 레이어는 그 세부 구현 사항을 알지 못한다.

이로써 도메인 레이어는 독립된 상태를 유지한다. 도메인에서의 변경 사항은 전체 애플리케이션에 영향을 미칠 수 있지만, 다른 레이어에서의 변경은 도메인에 전혀 영향을 미치지 않는다. 

또한, 이와 같은 구조는 각 레이어의 책임을 명확히 분리하여 코드의 가독성과 유지보수성을 높이는 데 중점을 두고 있다. 도메인 레이어가 비즈니스 로직과 도메인 모델의 핵심을 유지하고, 인프라스트럭처 레이어가 데이터 접근 및 외부 시스템과의 통신을 처리함으로써, 시스템은 보다 유연하고 변화에 강한 구조를 가지게 된다.


##### 도메인 모델로서의 엔티티 vs 순수 도메인 모델  

추가로 한가지 더 생각해볼 점은 도메인 객체와 엔티티에 대한 것이다. 클린 아키텍처를 사용하면서도 엔티티를 어디에 두느냐에 따라 패키지 구성이 달라질 수 있다.

원칙적으로 엔티티는 인프라스트럭처 레이어에 위치하며, 데이터 계층에서의 데이터에 대응되는 역할을 하는 것이 맞다. 에릭 에반스는 *도메인 주도 설계*에서 엔티티를 다음과 같이 정의하고 있다. 

> 어떤 객체를 일차적으로 해당 객체의 식별성으로 정의되는 것으로 ENTITY의 생명주기 동안에 내용이나 속성이 바뀌어도 연속성이 유지되는 것을 말한다. ENTITY에 포함된 속성보다는 정체성(식별성)에 초점을 맞춘 객체이다.

한편 도메인 모델은 다음과 같이 정의된다. 

> 도메인 모델은 특정 도메인 내의 중요한 개념과 로직을 포착하는 객체 모델이다. 이 모델은 도메인 전문가와 개발자 사이의 공통 언어로서 작용하며, 도메인 로직을 구현하는 데 중점을 둔다.

이 정의에 따르면, 도메인 모델은 단순히 데이터 구조를 넘어, 도메인의 행동과 규칙을 포착하는 데 중요한 역할을 한다.

그러나 실제로는 JPA를 사용하면서 엔티티를 도메인 모델로 사용하는 경우가 흔하고, 관례적으로도 많이 사용되기 때문에 도메인 계층에서 엔티티를 사용하는 것도 흔한 실정이다.

그래서 타협점을 찾아 사용되는 것이 **도메인 모델로서의 엔티티**라고 생각한다. 도메인 모델로서의 엔티티는 비즈니스 로직을 포함하고, 애플리케이션의 핵심 개념을 표현하는 객체다. 

JPA 엔티티를 도메인 모델로 사용하는 경우, 엔티티는 데이터베이스와의 매핑뿐만 아니라 비즈니스 로직도 함께 가지고 있다. 이러한 접근 방식은 간결하고 직관적이지만, 엔티티가 데이터베이스와 강하게 결합되어 도메인 로직이 데이터베이스 변경에 취약해질 수 있다는 단점은 있다.
그러나 실용적인 관점에서 JPA에서 제공하는 여러 기능을 사용할 수 있는 이점이 있다. 예를 들어, **더티 체킹(dirty checking)**과 **영속성 컨텍스트의 1차 캐시**는 엔티티 상태 변화를 자동으로 감지하고 관리해준다. 또한, JPA의 **쿼리 언어(JPQL)**나 **명시적 쿼리 작성**을 통해 복잡한 데이터 조회 작업을 쉽게 처리할 수 있다.

반면에, **순수 도메인 모델**은 데이터베이스와 독립적인 비즈니스 로직을 가지며, 도메인 모델과 데이터베이스 엔티티를 분리하는 것이다. 도메인 모델은 비즈니스 규칙과 로직을 표현하고, 데이터베이스와의 상호작용은 별도의 엔티티나 리포지토리에서 처리한다. 이 경우, 도메인 모델은 데이터베이스의 변경에 영향을 받지 않으며, 더 높은 유연성과 유지보수성을 제공한다.

이때의 단점은, 도메인 모델과 데이터베이스 엔티티 간의 변환을 수행해야 하므로, 추가적인 코드와 복잡성을 수반할 수 있다는 점이다. 

그렇다면 정리해보자. 

**도메인 모델과 엔티티의 분리**를 통해 얻게 되는 이점과 단점이다.

**이점**:
1. **유연성**: 도메인 모델은 데이터베이스의 변경에 영향을 받지 않기 때문에 비즈니스 로직의 변경이 자유롭다.
2. **유지보수성**: 도메인 로직과 데이터베이스 로직이 분리되어 있어 각각을 독립적으로 관리하고 테스트할 수 있다.
3. **명확한 책임 분리**: 도메인 모델은 비즈니스 규칙을, 데이터베이스 엔티티는 데이터 저장을 담당함으로써 코드의 역할이 명확해진다.

-> 보다 더 클린 아키텍처에 가까워진다!

**단점**:
1. **복잡성 증가**: 도메인 모델과 데이터베이스 엔티티 간의 변환을 처리하는 추가적인 코드와 로직이 필요하다.
2. **성능 오버헤드**: 변환 과정에서의 성능 저하가 발생할 수 있다.
3. **개발 부담**: 개발자가 두 개의 모델을 모두 이해하고 유지해야 하므로 학습 곡선이 가파를 수 있다.

-> JPA의 기능을 완전히 사용하지 못한다!

결론적으로, JPA 엔티티를 도메인 모델로 사용할지, 순수 도메인 모델을 유지할지는 설계의 선택과 요구사항에 따라 달라질 수 있다고 생각한다.  






## 4. 사용자 요청 이력 저장하기 챌린지

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





# 동시성 이슈

프로젝트는 다음과 같은 요구사항에 따라 동시성 처리를 필요로 한다.

> 각 강의는 선착순 30명만 신청 가능합니다.
> 이미 신청자가 30명이 초과되면 이후 신청자는 요청을 실패합니다.

먼저 낙관적 락을 이용해 구현해보았다.

## 낙관적 락을 이용한 구현

JPA에서 이용하는 기능을 이용해 어플리케이션 수준에서는 쉽게 낙관적 락을 구현할 수 있다. 다음과 같은 어노테이션을 붙여만 주면 된다.

```
    @Version
    private Long version; // 낙관적 락을 위한 버전 필드
```

낙관적 락에는 버전 혹은 시간 정보가 데이터베이스 레벨에서의 충돌을 감지하는데 사용된다.

일반적으로 이 방식은 주로 읽기 위주의 시스템에서 효과적이다.

**엔티티 구현**

```java
public class LectureApplication extends AbstractAggregateRoot<LectureApplication> {
	@Id
	private String lectureApplicationId;

	@Version
	private Long version; // 낙관적 락을 위한 버전 필드

// ...
```


**서비스 레이어에서 낙관적 락 처리**

서비스 레이어에서는 트랜잭션을 사용하여 데이터를 처리한다.

@Transactional 애노테이션을 사용하여 트랜잭션을 관리하고, 낙관적 락 충돌이 발생할 때 예외를 처리한다.


```java
@Service
public class SimpleLectureApplyService implements LectureApplyService {

	@Transactional
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

@Version 애노테이션이 붙은 version 필드를 통해 낙관적 락을 구현한다. 이 필드는 엔티티가 갱신될 때마다 자동으로 증가한다.

applyForLecture 메서드는 트랜잭션으로 처리되며, 낙관적 락 충돌 시 OptimisticLockingFailureException을 발생시켜 트랜잭션을 롤백된다.

그런데 낙관적 락은 이와 같은 구현만으로는 충분하지 않다. 왜 그럴까?


### 실패하는 테스트

다음과 같은 동시성 요청 시나리오를 작성했고 테스트한 결과, 실패했다!

```
  Scenario: 동시 다발적인 특강 신청 시나리오
    Given 특강 신청 페이지에 접속하여 현재 제공되는 특강 목록을 조회한다
    When 31명의 사용자가 동시에 "항해 특강 1" 특강 신청을 요청한다
    Then 총 이력의 개수는 31개로 확인된다
    And 31명에 대해 "항해 특강 1" 특강 신청 완료 여부를 조회하면 30명은 성공했고 1명은 실패했음이 확인된다
```

![optimistic-concurrency-failure1.png](document%2Foptimistic-concurrency-failure1.png)


### 문제의 원인

결론부터 이야기하자면 낙관적 락이 제대로 작동하지 않는 이유는 충돌이 발생하지 않았기 때문이다.

### 검증 로직의 문제

현재 검증 로직에서는 실제 데이터베이스에 저장된 신청자의 개수와 강의에 설정된 수용 인원을 비교하여 검증을 수행한다.

```java
@Component
@RequiredArgsConstructor
public class LectureApplyValidator implements Validator<LectureApplyCommand, Lecture> {
	private final LectureCapacitySpecification capacitySpecification;
	private final UniqueUserApplicationSpecification uniqueUserApplicationSpecification;

	@Override
	public void validate(LectureApplyCommand command, Lecture lecture) {
		if (uniqueUserApplicationSpecification.isNotSatisfiedBy(command, lecture)) {
			throw new LectureNotApplicableException(DUPLICATE_APPLICATION);
		}
		if (capacitySpecification.isNotSatisfiedBy(command, lecture)) {
			throw new LectureNotApplicableException(LECTURE_FULL);
		}
	}
}
```

이 검증 로직에서 수용 인원을 체크하는 구현은 `capacitySpecification`에 다음과 같이 구현되어 있다.

```java
@Component
@RequiredArgsConstructor
public class LectureCapacitySpecification implements Specification<LectureApplyCommand, Lecture> {
	private final LectureApplicationRepository applicationRepository;

	@Override
	public boolean isSatisfiedBy(LectureApplyCommand command, Lecture lecture) {
		return applicationRepository.countByLecture(lecture) < lecture.getCapacity();
	}
}
```


문제는 이렇다.

낙관적 락은 `@Version` 필드를 이용해 엔티티의 변경 사항을 감지하고 충돌을 처리하지만, 현재 구조에서는 `LectureApplication` 엔티티와 `Lecture` 엔티티 간의 상호작용이 제대로 이루어지지 않아 충돌을 감지하지 못하고 있다.

### 버전 필드의 문제

- `LectureApplication` 엔티티와 `Lecture` 엔티티 모두에 `@Version` 필드가 있지만, `LectureApplication`이 추가될 때마다 `Lecture` 엔티티가 갱신되지 않는다.
- `Lecture` 엔티티의 버전 정보는 그대로 유지되기 때문에, 강의 신청이 추가될 때마다 `Lecture` 엔티티의 버전 충돌이 발생하지 않는다.

즉, `Lecture` 엔티티의 버전 정보는 강의 수용 인원 체크와 무관하게 유지되기 때문에, 실제로 낙관적 락에 의한 충돌이 발생할 로직이 없다.

### 검증 로직의 현재 동작 방식

- 현재 검증 로직은 `LectureApplication`의 수를 매번 데이터베이스에서 조회하여, `Lecture` 엔티티에 설정된 수용 인원과 비교한다.
- 이 과정에서 데이터베이스 읽기와 쓰기가 동시에 발생하지만, 트랜잭션 격리 수준에 따라 읽기 작업은 아직 커밋되지 않은 쓰기 작업을 반영하지 않는다.
- 결과적으로 여러 트랜잭션이 동시에 수용 인원 검사를 통과하고, 31명이 넘는 사용자가 동시에 강의 신청에 성공하게 된다.

어떻게 해야할까? 간단하다. 충돌이 발생하도록 하면된다.


즉 카운트에 대한 책임을 `Lecture`가 지고 매번 `LectureApplication`이 생성될 때마다 이를 갱신해주는 것이다.

```java
@Entity
public class Lecture {

	//... 

	@Column(nullable = false)
	private int registeredCount = 0; // 등록된 신청자 수

	// ...

	public void incrementRegisteredCount() {
		this.registeredCount++;
	}
}
```

검증에서는 다음과 같이 수정한다.

```java
public class LectureCapacitySpecification implements Specification<LectureApplyCommand, Lecture> {

	@Override
	public boolean isSatisfiedBy(LectureApplyCommand command, Lecture lecture) {
		return !lecture.isCapacityExceeded();
	}
}
```

### 여전히 실패하는 테스트

그런데도 테스트가 실패했다.

낙관적 락은 기본적으로 충돌을 감지하여 예외를 발생시킨다. 동시적으로 요청이 쏟아져 올 때 충돌은 너무 발생하게 되다.
결과적으로 너무 많은 요청에 대해 너무 많은 충돌이 발생하며 트랜잭션 충돌에 따라 데이터베이스에서 데드락이 발생해버린 것이다!

데드락은 여러 트랜잭션이 서로가 필요로 하는 리소스를 점유하고 있을 때 발생하는 교착 상태를 말한다.
즉, 두 개 이상의 트랜잭션이 서로 상대방의 리소스가 해제되기를 기다리면서 무한 대기 상태에 빠지는 상황이다.

여러 트랜잭션이 동시에 동일한 레코드를 업데이트하려고 하면, 각 트랜잭션은 다른 트랜잭션이 작업을 완료할 때까지 기다려야 한다. 이 과정에서 충돌이 발생하고, 충돌이 빈번할수록 대기 시간이 길어진다.

트랜잭션 A가 레코드 X에 락을 걸고, 트랜잭션 B가 레코드 Y에 락을 걸었다고 가정해보자. 이후 트랜잭션 A가 레코드 Y에 접근하려고 하고, 트랜잭션 B가 레코드 X에 접근하려고 하면, 두 트랜잭션은 서로가 점유한 리소스를 기다리면서 데드락 상태에 빠진다.

다음과 같은 로그를 보자.

```plaintext
20:54:05.765 [ERROR] [http-nio-auto-1-exec-2] [org.hibernate.orm.jdbc.batch] - HHH100501: Exception executing batch [java.sql.BatchUpdateException: Deadlock found when trying to get lock; try restarting transaction], SQL: /* update for io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture */update lecture set application_close_time=?, application_open_time=?, capacity=?, description=?, duration_minutes=?, instructor=?, lecture_external_id=?, lecture_start_time=?, location=?, registered_count=?, status=?, title=?, version=? where lecture_id=? and version=?
20:54:05.765 [ERROR] [http-nio-auto-1-exec-30] [org.hibernate.orm.jdbc.batch] - HHH100501: Exception executing batch [java.sql.BatchUpdateException: Deadlock found when trying to get lock; try restarting transaction], SQL: /* update for io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture */update lecture set application_close_time=?, application_open_time=?, capacity=?, description=?, duration_minutes=?, instructor=?, lecture_external_id=?, lecture_start_time=?, location=?, registered_count=?, status=?, title=?, version=? where lecture_id=? and version=?
...
```

특히, 31명의 사용자가 동시에 동일한 강의에 신청하는 경우, 각 트랜잭션이 `registered_count` 필드를 갱신하려고 하면서 충돌이 발생한다.

1. **동시성 충돌 증가**:
    - 여러 트랜잭션이 동시에 동일한 `Lecture` 엔티티의 `registered_count` 필드를 갱신하려고 하면, 데이터베이스는 충돌을 감지하고 트랜잭션을 롤백시킨다.

2. **낙관적 락의 한계**:
    - 낙관적 락은 충돌이 적을 때 효과적이다. 그러나 동시성 요청이 매우 높은 시나리오에서는 많은 충돌이 발생하고, 이에 따른 롤백이 빈번하게 일어나 성능이 저하되고 이와 같이 데드락 위험성도 발생한다.

따라서, 낙관적 락을 사용한 현재 방식은 동시성 충돌이 빈번하게 발생하는 상황에서는 적합하지 않다는 결론에 도달했다.



## 비관적 락을 이용한 구현

낙관적 락 대신 비관적 락을 사용하여 데이터베이스 차원에서 동시성 문제를 해결하고자 하였다. 분산 락을 사용하기에 앞서 데이터베이스 차원에서의 접근으로 풀어보고 싶었다.

비관적 락은 레코드에 락을 걸어 다른 트랜잭션이 접근하지 못하게 하여 충돌을 방지한다. 테이블 수준의 락으로 select for update를 사용하여 해당 레코드를 조회하고, 업데이트를 진행한다.

구현 자체는 `JPA`의 기능을 이용하므로 어렵지 않다.

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT l FROM Lecture l WHERE l.lectureExternalId = :externalId")
Optional<Lecture> findByLectureExternalIdForUpdate(@Param("externalId") String externalId);
```

락 자체는 순차 처리를 보장하지 않지만, 비관적 락의 경우 락을 획득하는 과정에서 자연스럽게 순차 처리가 이루어진다. 이는 비관적 락의 메커니즘 특성으로 인해 발생하는 결과로 이해할 수 있다.

따라서, 데이터 검증(validation) 처리 이후 `Lecture` 클래스의 `count` 필드를 증가시키면 된다.

1. `lectureExternalId`를 가진 `Lecture` 엔터티를 비관적 락 모드로 조회한다.
2. 데이터 검증을 수행하여 유효성을 확인한다.
3. 검증이 완료되면 `Lecture` 엔터티의 `count` 필드를 증가시킨다.
4. 특강 신청 외 기타 변경 사항을 데이터베이스에 커밋하여 트랜잭션을 종료한다.

이 과정을 통해 비관적 락을 사용한 동시성 제어와 안전한 데이터 업데이트를 구현할 수 있다.

이제 정상적으로 테스트가 통과해야 할텐데... 역시 또 문제가 발생했다!

### 문제 1: 이력 기록에서의 롤백 문제

특정 이유로 하나의 트랜잭션이 실패하면 시도 이력도 롤백되는 문제가 발생했다.

![history-failure-rollback-problem1.png](document%2Fhistory-failure-rollback-problem1.png)

![history-failure-rollback-problem2.png](document%2Fhistory-failure-rollback-problem2.png)


이 문제는 시도 이력이 동기적으로 엮여 있어서 발생한 문제였다.

원래 의도는 비동기였지만 `@Async` 애노테이션을 누락한 상태였다.  
`@Async` 애노테이션을 추가하여 비동기적으로 처리되도록 수정하여 비교적 간단하게 해결할 수 있었다.

```java
public class LectureApplyEventHandler {

	@Async
	@EventListener
	public void handleLectureApplyTryEvent(LectureApplyTryEvent event) {
		lectureApplyHistoryFactory.saveTryHistory(event);
	}
}
```

이제 `handleLectureApplyTryEvent` 메서드는 비동기적으로 실행되므로, 트랜잭션이 롤백되더라도 시도 이력은 별도의 비동기 트랜잭션에서 처리되어 롤백되지 않는다.

그렇게 고치자마자 두번째 문제가 발생했다.


### 문제 2: 성공 이력의 조회 문제

성공 이력을 `upsert` 할 때 동일한 파라미터로 외부에서 조회한 결과 커밋된 행이 존재함에도 불구하고 코드 상에서는 조회되지 않는 문제가 발생했다.
결과적으로 다음과 같이 실제 시도 이력이 31개인데도 upsert가 되지 않아 더 많은 개수의 시도가 생기는 문제가 된 것이다.


![history-not-consistency1.png](document%2Fhistory-not-consistency1.png)


![history-not-consistency2.png](document%2Fhistory-not-consistency2.png)


이 문제는 무엇 때문인 걸까?

트랜잭션 격리 수준과 트랜잭션의 비동기 처리 방식이 상호 작용하면서 발생한 것 같다.

프로젝트는 MySQL의 기본 격리 수준인 **REPEATABLE READ** 격리 수준을 사용한다. **REPEATABLE READ**에서는 트랜잭션이 시작된 시점의 스냅샷을 기준으로 데이터를 조회하기 때문에, 다른 트랜잭션에서 커밋된 변경 사항이 현재 트랜잭션에서는 보이지 않는 문제가 발생할 수 있다.

생각해 보자. `LectureApplySuccessEvent`와 `LectureApplyTryEvent`가 서로 다른 트랜잭션에서 처리되는데, `LectureApplyTryEvent`가 처리되는 트랜잭션에서 `REPEATABLE READ` 격리 수준 때문에 `LectureApplySuccessEvent`에 의해 업데이트된 이력이 보이지 않을 수 있다.
따라서 `LectureApplySuccessEvent`에서 커밋된 데이터가 `LectureApplyTryEvent`에서 조회되지 않는 결과를 초래한 것이다.

이를 해결하기 위해 `upsert` 메서드에 대해서도 `REQUIRES_NEW` 전파 수준을 적용하여 새로운 트랜잭션에서 처리되도록 수정했다. 또한, 특강 신청 성공 이력 트랜잭션이 필요한 데이터를 읽을 때에도 해당 행을 잠그도록 했다. 우선 코드부터 살펴보면 다음과 같다.

```java
@Retryable(
	value = { CannotAcquireLockException.class },
	maxAttempts = 5,
	backoff = @Backoff(delay = 1000)
)
public void upsertSuccessEvent(LectureApplySuccessEvent event){
	LectureApplicationHistory history = historyRepository
		.findByUserIdAndLectureExternalIdAndRequestAtWithLock(event.getUserId(), event.getLectureExternalId(), event.getRequestAt())
		.map(existingHistory -> existingHistory.updateSuccess(event.isSuccess()))
		.map(existingHistory -> existingHistory.updateLectureApplicationInfoWithSuccessEvent(event))
		.orElseGet(event::toEntity);

	historyRepository.save(history);
}
```

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT lah FROM LectureApplicationHistory lah WHERE lah.userId = :userId AND lah.lectureExternalId = :lectureExternalId AND lah.requestAt = :requestAt")
	Optional<LectureApplicationHistory> findByUserIdAndLectureExternalIdAndRequestAtWithLock(String userId, String lectureExternalId, LocalDateTime requestAt);
```

여기서 발생한 상황은 MVCC 기능에 따라 트랜잭션이 시작된 시점의 스냅샷을 기준으로 데이터를 조회하기 때문에, 다른 트랜잭션에서 커밋된 변경 사항이 보이지 않는 것이었다.  
비관적 락을 사용하여 트랜잭션이 데이터를 읽을 때 해당 데이터를 잠가 타 트랜잭션에 대한 순차성을 보장하도록 한 것이다.

그리고 실패가 나는 경우 재처리를 할 수 있도록 명시적인 `Retry` 를 추가했다.

이렇게 함으로써 의도대로(?!) 작동하는 로직이 되긴 했다. 했는데...


### 문제 3: 동시 요청 처리 문제

곧바로 또 다른 문제가 발생했다.

다음과 같은 로그를 볼 수 있었다.


![connection-pool.png](document%2Fconnection-pool.png)


커넥션 풀 문제였다. HikariCP가 더 이상 새로운 커넥션을 제공할 수 없다는 것이다. 요청이 몰리면서 커넥션 풀이 포화 상태에 이르고, 그로 인해 SQL 요청이 대기 중 타임아웃 되는 상황이 발생했다.

즉, 현재 설정된 커넥션 풀의 크기가 요청을 처리하기에 충분하지 않은 상황에서, 비동기 트랜잭션 처리로 인해 동시에 많은 수의 데이터베이스 커넥션이 필요해졌으며, 커넥션 풀이 감당할 수 없는 수준까지 증가한 것이다.

해결은 우선 커넥션 풀을 늘려주는 것으로 시도했다.

`yml` 파일에서 HikariCP의 커넥션 풀 설정을 다음과 같이 조정했다.

```properties
    hikari:
        maximum-pool-size: 50
        minimum-idle: 50
```


또 하나의 방법은 동시 요청의 수를 제어하여 한 번에 처리하는 요청의 수를 제한하는 법이다. 다음과 같이 비동기 작업의 스레드 풀 크기를 조정하여 한 번에 너무 많은 비동기 작업이 실행되지 않도록 했다.

```java

@Configuration
public class AsyncConfig implements AsyncConfigurer {

	private Executor createTaskExecutor(String threadNamePrefix) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(1000);
		executor.setThreadNamePrefix(threadNamePrefix);
		executor.initialize();
		return executor;
	}

	@Bean
	public Executor lectureApplyTryEventExecutor(){
		return createTaskExecutor("lecture-apply-try-event-");
	}
}
```
```java
@Async("lectureApplyTryEventExecutor")
	@EventListener
	public void handleLectureApplyTryEvent(LectureApplyTryEvent event) {
		lectureApplyHistoryFactory.saveTryHistory(event);
	}
```



휴 🤣

이로써 마침내 테스트가 통과했다 !


![concurreny-test-success.png](document%2Fconcurreny-test-success.png)




### 문제 0: 이 불편한 느낌적인 느낌

> 이게 맞아...?

그런 생각이 머리를 떠나지 않았다.

비관적 락을 사용하면서 동시성 문제를 해결하고자 했지만, 결과적으로 너무 많은 복잡성을 초래했다.

MVCC 격리 수준의 문제를 우회하기 위해 트랜잭션 전파 레벨을 조정하고, 재시도 로직을 추가하며, 비동기 처리의 복잡성을 관리하는 등의 작업을 반복하며, 한마디로 들쑤시고 난리 부르스를 친 것이다.

다음과 같은 문제를 낳았다.

1. **복잡한 트랜잭션 관리**: 트랜잭션 전파 수준을 조정하고 비관적 락을 적용하는 과정에서 코드가 복잡해지고 관리가 어려워졌다.
2. **성능 문제**: 비관적 락과 잦은 재시도로 인해 데이터베이스 커넥션 풀이 포화 상태에 이르렀고, 이는 곧 성능 문제로 이어졌다.
3. **높은 유지보수 비용**: 복잡한 동시성 제어 로직은 코드 유지보수 비용을 증가시켰고, 향후 확장이나 변경이 어려운 구조를 만들었다.

이 문제에 대해서 항해 플러스 백엔드 5기 아고라 방에 공유했고 감사하게도 석범 코치님께 다음과 같은 답변을 받았다.

![hhplus_qna_history_transaction_problem.png](document%2Fhhplus_qna_history_transaction_problem.png)

![hhplus_qna_history_transaction_problem2.png](document%2Fhhplus_qna_history_transaction_problem2.png)

즉, 근본적으로 설계부터 다시 접근해보면 어떨까?

시도 이력과 성공 이력을 하나의 트랜잭션 내에서 함께 저장하려는 접근 방식 자체에 문제가 있는 것은 아닐까?

애초에 시도 이력과 성공 이력을 분리하여 관리했다면, 이러한 복잡한 동시성 제어와 락 관리가 필요하지 않았을 것이다.


### 삽질의 최후

무엇보다 중요한 건 역시 제대로 된 설계이다. 어딘가 꼬이기 시작하면 탄로가 나는 것일 뿐이다.
만약 처음부터 시도 이력과 성공 이력을 분리하여 관리했다면, 비관적 락을 사용하면서도 문제가 덜 발생했을 것이다.

그럼에도 실무에서 비관적 락을 얼마나 자주 사용할지는 잘 모르겠다.

시니어 한 분과 이런 이야기를 나눈 적이 있다.

> 나: "이 부분에서 락이 필요할 것 같아요" <br>
> 동료: "그렇네요. 락 뭐 쓰시게요?" <br>
> 나: <이런 락 저런 락 따져보는 이야기...> <br>
> 동료: "DB 락은.. 왠만하면 근데 쓰지 마세요! 내가 정말 잘 안다, 그럼 써도 되는데... 아니 그래도 그냥 왠만하면 쓰지 마세요!" <br>

이번에 비관적 락의 위험성을 몸소 체험하며, 그때 이 짧은 대화가 불현듯 떠올랐다.

이번 경험으로 비관적 락의 리스크를 실감하게 된 건 확실하다!

- DB 락은 신중하게 사용해야 한다. 트랜잭션 관리와 성능 문제를 유발할 수 있다.
- 복잡한 동시성 제어 로직은 유지보수 비용을 증가시키고, 시스템의 확장성을 저해한다.

또 하나, 삽질 덕분에 얻은 것이라면 데드락 경험을 한 것인데 실무에서 보면 안되는 로그를 집에서 볼 수 있어서 다행이라고 생각한다.

![deadlock-found.png](document%2Fdeadlock-found.png)

데드락, 트랜잭션 문제를 겪으며, 책에서만 읽던 이론을 실제로 경험할 수 있었다. 트랜잭션 격리 수준, 전파 수준, 그리고 MVCC와 같은 개념들을 사실 이론적으로 알고 있었지만 직접 사용하면서 문제를 겪어보진 않았는데 이번 기회를 통해 직접 체험할 수 있었다.

특히 MVCC에 대해서는 책으로 읽을 때는 팬텀 리드를 해결해주는 훌륭한 메커니즘이라고 까지만 생각했었다. 책에서 `Seriarizable`은 왠만하면 잘 쓰이지 않는다고 하니, MVCC로 인해 다른 트랜잭션의 변경 사항을 즉시 반영하지 못하는 건 또 문제가 아닐까? 라는 생각을 해보지 않았던 것 같다.

이런 경험들을 통해 조금 더 성장할 수 있었다고 생각한다.








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
    ./gradlew bootRun
    ```
6. 정상 작동 후 확인

도커가 정상적으로 실행되면 `localhost:3301` 포트와 `3302` 포트로 `Database` 조회시 다음과 같이 확인되어야 합니다.

![docker-after-database.png](document%2Fdocker-after-database.png)

