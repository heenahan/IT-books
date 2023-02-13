# 02 장

## 1. 변화하는 요구사항
사용자의 요구사항은 항상 바뀐다. 이때 우리는 최소한의 비용으로 시시각각 변하는 요구사항에 대응하고 싶어한다.

뿐만 아니라 새로 추가한 기능은 _쉽게 구현할_ 수 있어야 하고 장기적인 관점에서 _유지보수가 쉬워야 한다._

**동작 파라미터화**란 아직은 어떻게 실행할 것인지 결정하지 않는 코드 블록을 의미한다.

동작 파라미터화를 이용하면 자주 바뀌는 요구사항에 효과적으로 대응할 수 있다.

### 예시) 농장 재고목록 리스트에서 필터링하는 기능 추가
처음에 농장 재고목록 리스트에서 녹색 사과만 필터링하는 기능을 추가한다고 가정하자.

하지만 변심하여 빨간 사과를 필터링하고 싶어졌다. 더 나아가 무게가 150그램 이상인 무거운 사과만 필터링하고 싶다.

```java
enum Color { RED, GREEN }

// 색깔을 기준으로 필터링
public static List<Apple> filterApplesByColor(List<Apple> inventory, Color color) {
	List<Apple> result = new ArrayList<>();
    for (Apple apple: inventory) {
    	if (apple.getColor().equals(color)) result.add(apple); 
    }
    return result;
}

// 무게를 기준으로 필터링
public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
	List<Apple> result = new ArrayList<>();
    for (Apple apple: inventory) {
    	if (apple.getWeight() >= weight) result.add(apple); 
    }
    return result;
}
```

두 메서드를 살펴보면, 목록을 검색하고 각 사과에 필터링 조건을 적용하는 **코드가 대부분 중복된다.**

이렇게 변화하는 요구사항에 대응하기 위해 여러 중복된 필터 메서드 만들거나 모든 것을 처리하는 거대한 하나의 필터 매서드를 구현한다는 것은 좋은 방법이 아니다.

## 2. 동작 파라미터화를 통한 유연한 대응
변화하는 요구사항에 좀 더 유연하게 대응하기 위해 동작 파라미터화를 이용한다.

### 2.1. 전략 디자인 패턴
**전략 디자인 패턴**은 각 알고리즘(전략)을 캡슐화하는 알고리즘 패밀리를 정의해둔 다음에 런타임에 알고리즘을 선택하는 기법이다.

먼저 사과의 어떤 속성에 기초해서 참 또는 거짓을 반환하는 인터페이스(**알고리즘 패밀리**)를 정의한다.
```java
public interface ApplePredicate {
    boolean test(Apple apple);
}
```

다음으로 다양한 선택 조건을 나타내는 구현체(**전략**)를 정의한다. 앞으로 요구사항이 변경되면 ApplePredicate를 적절하게 구현하는 클래스를 만들면 된다.
```java
// 무거운 사과만 선택
public class AppleHeavyWeightPredicate implements ApplePredicate {
    public boolean test(Apple apple) {
    	return apple.getWeight() >= 150;
    }
}
// 녹색 사과만 선택
public class AppleGreenColorPredicate implements ApplePredicate {
    public boolean test(Apple apple) {
    	return Green.equals(apple.getColor());
    }
}
```

위에서 정의한 ApplePredicate를 이용한 필터 메서드는 다음과 같다.
```java
public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
    	if (p.test(apple)) result.add(apple);
    }
    return result;
}

List<Apple> redApples = filterApples(inventory, new AppleGreenColorPredicate());
```
이제 메서드는 다양한 동작을 받아서 내부적으로 다양한 동작을 수행할 수 있다.

이렇게 _filterApples 메서드 내부에서 컬렉션을 반복하는 로직_ 과 _컬렉션의 각 요소에 적용할 동작_ 을 **분리할 수 있다는 점**은 소프트웨어 엔지니어링적으로 큰 이득이다.

동작 파라미터화를 통해 filterApples 메서드가 다른 동작을 수행하도록 재활용할 수 있다.

그런데 위 방식처럼 메서드로 새로운 동작을 전달하기 위해 인터페이스를 구현하는 여러 클래스를 정의한 다음 인스터스화하는 과정은 상당히 번거로운 작업이다.

### 2.2. 익명 클래스
자바의 블록 내부에 선언된 지역 클래스와 비슷하게 **익명 클래스**는 클래스의 선언과 인스턴스화를 **동시에 수행**할 수 있다.

```java
List<Apple> redApples = filterApples(inventory, new ApplePredicate() {
    public boolean test(Apple apple){
    	return Red.equals(apple.getColor());
    }
});
```

그런데 익명 클래스를 사용하면 코드가 장황해진다. 장황한 코드는 구현하고 유지보수하는데 시간이 오래걸리므로 개발자로부터 외면받는다.

### 2.3. 람다 표현식
동작 인터페이스와 익명 클래스 모두 메서드에 동작을 전달할 때 새로운 동작을 정의하는 메서드를 객체로 감싸서 전달했다.

자바 8의 람다 표현식을 이용하면 더 **간단하게** 코드를 전달할 수 있다.

```java
List<Apple> result = filterApples(inventory, (Apple apple) -> RED.equals(apple.getColor()));
```

---

💡 **Key Words**

동작 파라미터화, 변화에 유연한 대응