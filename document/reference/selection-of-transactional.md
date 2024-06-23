# Transaction 어노테이션 선택 기준: jakarta VS. spring

### Q: `jakarta(javax).transaction.Transactional`과 `org.springframework.transaction.annotation.Transactional` 중에서 어떤 것을 선택해야 할까?

트랜잭션 애노테이션을 선택할 때 어떤 것이 더 적합할지에 대한 고민에서 두 가지 옵션이 있다.

Spring 애플리케이션에서는 Spring Framework의 @Transactional 애노테이션을 사용하는 것이 더 적절하다고 판단하여 사용했다.

### Spring Framework `@Transactional`

#### 1. **Spring의 트랜잭션 관리와의 통합**:
- Spring의 `@Transactional` 애노테이션은 Spring의 트랜잭션 관리 인프라와 깊이 통합되어 더 많은 기능과 트랜잭션 전파, 격리 수준, 롤백 규칙에 대한 더 나은 제어를 제공한다.

#### 2. **확장된 기능**:
- Spring의 `@Transactional` 애노테이션은 표준 Java EE `@Transactional` 애노테이션에서는 제공되지 않는 추가 구성 옵션을 제공한다. 예를 들어, `REQUIRES_NEW`, `NESTED`와 같은 트랜잭션 전파에 관한 옵션 등이다.

#### 3. **Spring 컨텍스트 인식**:
- Spring 컨테이너와 통합되어 컨텍스트를 인식할 수 있다. AOP 및 의존성 주입과 같은 다른 Spring 기능을 활용할 수 있어 전체 트랜잭션 관리 기능을 향상시킨다.

예를 들어 ...
> - AOP 통합: 트랜잭션 관리가 AOP를 통해 쉽게 적용되어 특정 메서드나 클래스에 선언적 트랜잭션을 설정할 수 있다.
> - 의존성 주입: Spring의 의존성 주입을 통해 트랜잭션 매니저를 쉽게 주입하고 관리할 수 있다. 이를 통해 다양한 트랜잭션 매니저를 사용할 수 있으며, 애플리케이션의 요구에 따라 쉽게 변경할 수 있다.

    
### 프로젝트 사용 예시

```java
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Service
@RequiredArgsConstructor
public class SimpleLectureCrudService implements LectureCrudService {

	@Override
    @Transactional
    public LectureRegisterInfo register(LectureRegisterCommand command) {
        // ...
    }

}
```
