# 전략 패턴
## 1. 문제
다음은 SimUDuck 애플리케이션이다.
```java
// Duck.java
public abstract class Duck {
    display();
    quack(); // 꽥꽥하고 울음
}

// MallardDuck.java
public class MallardDuck extends Duck {
    display() {
        // 청둥오리 모양
    }
}

// RubberDuck.java
public class RubberDuck extends Duck {
    display() {
        // 고무 오리 모양
    }
    quack() {
        // 삑삑하고 울음
    }
}
```
<code>Duck</code>이라는 슈퍼클래스를 만든 다음, 그 클래스를 확장해서 서로 다른 종류의 오리 클래스를 만들었다.
<br><br>
이때 오리에 날 수 있는 기능을 추가하려 한다. 
따라서 슈퍼클래스에 <code>fly()</code> 메소드를 추가했다.
하지만 **슈퍼클래스에 메소드를 추가했더니 모든 서브클래스가 영향을 받았다.**
<br><br>
청둥오리는 날 수 있지만 고무 오리는 날지 못한다. 
프로그램 전체에 고무 오리가 날아다니는 오류가 발생했다.
따라서 다음과 같이 <code>RubberDuck</code>에 <code>fly()</code>를 Override한다.
```java
// Duck.java
public abstract class Duck {
    /* 그 외 메소드 생략 */
    fly(); // 날개짓을 통해 날음
}

// RubberDuck.java
public class RubberDuck extends Duck {
    @Override
    fly() {
        // 날지 못함
    }
}
```
이번엔 나무로 만든 장식용 오리를 추가하려고 한다. 
참고로 이 장식용 오리는 날지도 못하고 소리도 내지 못한다.
<br><br>
Duck을 상속받는 <code>DecoyDuck</code> 클래스를 추가한 뒤 
<code>fly()</code>와 <code>quack()</code>을 오버라이드한다.
```java
// Duck.java
public abstract class Duck {
    /* 그 외 메소드 생략 */
    fly(); // 날개짓을 통해 날음
    quack(); // 꽥꽥하고 울음
}

// RubberDuck.java
public class DecoyDuck extends Duck {
    @Override
    fly() {
        // 날지 못함
    }
    @Override
    quack() {
        // 울지 못함
    }
}
```
앞으로도 <code>Duck</code>에 서브클래스가 추가될 때마다 <code>fly()</code>와 <code>quack()</code> 메소드를 살펴보고 
상황에 따라 오버라이드해야 한다. 너무 번거로운 작업이다.
<br><br>
그러면 슈퍼클래스에서 <code>fly()</code>를 없애고 <code>fly()</code>메소드가 들어있는 <code>Flyable</code> 인터페이스를 만들어보자.
날 수 있는 오리만 인터페이스를 구현하는 것이다.
```java
// Flyable.java
public interface Flyable {
    fly();
}

// MallardDuck.java
public class MallardDuck extends Duck implements Flyable {
    @Override
    fly() {
        // 날개짓을 통해 날음
    }
}

// RedHatDuck.java
public class RedHatDuck extends Duck implements Flyable {
    @Override
    fly() {
        // 날개짓을 통해 날음
    }
}
```
<code>Flyable</code>를 구현하는 모든 서브클래스는 <code>fly()</code>를 오버라이드한다. 
이 과정에서 코드 중복이 발생했다. 
<br><br>
따라서 이러한 디자인은 **코드를 재사용하지 않으므로** 코드 관리에 문제가 발생한다.
## 2. 패턴 활용
코드에 새로운 요구사항이 있을 때마다 바뀌는 부분이 있다면 **분리**, 즉 **캡슐화** 해야 한다.
결국 변화하는 부분을 그대로 있는 부분과 분리하기 위해 **완전히 별개의 클래스 집합**을 만들어야 한다. 
<br><br>
앞선 상황을 살펴보면, <code>fly()</code>와 <code>quack()</code>에서 문제가 발생했다.
두 메소드가 서브클래스에 따라, 결국 어떤 오리냐에 따라 달라지고 있다. 
<br><br>
우선 오리의 행동을 최대한 유연하게 만들어보자. 
두 오리의 행동(나는 행동과 꽥꽥거리는 행동)은 인터페이스로 표현하고 이런 인터페이스를 이용해서 행동을 구현한다. 
```java
// FlyBehavior.java
public interface FlyBehavior {
	fly();
}

// FlyWithWings.java
public class FlyWithWings implements FlyBehavior {
    @Override
    fly() {
        // 날개짓을 통해 날음
    }
}

public class FlyNoWay implements FlyBehavior {
    @Override
    fly() {
        // 날지 못함
    }
}

/* 꽥꽥거리는 행동 인터페이스 생략 */
```
이러한 디자인은 행동이 더 이상 <code>Duck</code> 안에 숨겨져 있지 않으니 **다른 객체가 행동을 재사용**할 수 있다.
그리고 <code>Duck</code> 클래스를 전혀 건드리지 않고 행동을 수정하거나 확장할 수 있다.
<br><br>
마지막으로 나는 행동과 꽥꽥거리는 행동을 <code>Duck</code> 클래스에 **위임**한다.

```java
// Duck.java
public abstract class Duck {
    FlyBehavior flyBehavior; // 인스턴스 변수
    QuackBehavior quackBehavior;
    
    fly() { // 참조되는 객체에게 위임
        flyBehavior.fly();
    }
    
    setFlyBehavior(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }
    
    /* QuackBehavior 생략 */
}
```
행동 인터페이스를 <code>Duck</code>의 인스턴스 변수로 선언한다. 
<code>Duck</code>은 나는 행동을 직접 처리하지 않고 **인스턴스 변수에 의해 참조되는 객체에게 행동을 위임**한다.
그리고 **인터페이스에 맞춰 프로그래밍했으므로** 참조되는 객체의 종류에는 전형 신경 쓸 필요 없이 그저 <code>fly()</code>를 실행하면 된다.
```java
// Main.java
Duck duck = new DecoyDuck();

duck.setFlyBehavior(new FlyNoWay());
duck.fly();
duck.setFlyBehavior(new FlyWithRocket());
duck.fly();
```
<code>DecoyDuck</code>을 인스턴스화 한 뒤, 세터(Setter) 메소드를 호출해 <code>flyBehavior</code>가 <code>FlyNoWay</code>를 참조했다.
이제 장식용 오리는 날지 못한다.
<br><br>
하지만 갑자기 로켓을 이용해 장식용 오리를 날게 해주고 싶었다. 
<code>FlyBehavior</code>를 구현하는 <code>FlyWithRocket</code>을 정의하고 세터 메소드를 호출해 <code>FlyWithRocket</code>로 초기화했다.
장식용 오리는 로켓 추진력으로 날 수 있게 되었다.
<br><br>
다형성을 활용해서 인스턴스 변수는 **실행 시에 동적으로 <code>FlyBehavior</code>를 구현한 다른 클래스를 할당**할 수 있다. 
따라서 위 코드는 상당히 유연하다. 
## 3. 정리
지금까지 전략 패턴을 이용하여 문제를 해결했다. 
오리의 행동들을 일련의 행동이 아닌 알고리즘군(family of algorithms)으로 생각하여 디자인했다.

**전략 패턴**은 알고리즘군을 정의하고 캡슐화해서 각각의 알고리즘군을 수정해서 사용할 수 있게 해준다.
전략 패턴을 사용하면 클라이언트로부터 **알고리즘을 분리**해서 **독립적으로 변경**할 수 있다.