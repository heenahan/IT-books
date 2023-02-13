# 03 장

## 1. 람다 표현식
**람다 표현식**은 메서드로 전달할 수 있는 익명 함수를 단순화한 것이다.

### 1.1. 람다의 특징
- 보통의 메서드와 달리 이름이 없으므로 **익명**이라 표현한다.
- 메서드처럼 특정 클래스에 종속되지 않으므로 **함수**라고 부른다.
- 람다 표현식을 **메서드 인수로 전달**하거나 **변수로 저장**할 수 있다.
- 익명 클래스와 달리 **코드가 간결**하다.

### 1.2. 람다 문법
- 표현식 스타일
```java
(Apple a1, Apple a2) // 파라미터
-> // 화살표 : 파라미터와 바디 구분
a1.getWeight().compareTo(a2.getWeight()) // 바디 : 반환값에 해당
```
- 블록 스타일
```java
(int x, int y) // 파라미터
-> // 화살표
{
    System.out.println("x와 y를 더한다.");
    return x + y;
} // 바디
```
## 2. 람다의 사용 위치
람다 표현식은 _변수에 할당_ 하거나 _함수형 인터페이스를 인수로 받는 메서드로 전달_ 할 수 있다. 여기서 함수형 인터페이스란 무엇일까?
### 2.1. 함수형 인터페이스
함수형 인터페이스는 정확히 하나의 추상 메서드를 지정하는 인터페이스이다.
```java
public interface Comparator<T> { // ← java.util.Comparator
    int compare(T o1, T o2);
}

public interface Runnable { // ← java.lang.Runnable
    void run();
}

public interface Callabe<V> { // ← java.util.concurrent.Callable
    V call throws Exception;
}
```
람다 표현식으로 함수형 인터페이스의 추상 메서드 구현을 직접 전달할 수 있다. 따라서 **전체 표현식을 함수형 인터페이스의 인스턴스로 취급**할 수 있다.

함수형 인터페이스인 Runnable을 인수로 받는 메서드를 정의했다. 다음은 이 메서드에 람다 표현식을 전달하는 예제이다.
```java
public static void process(Runnable r) {
    r.run();
}

Runnable r1 = () -> System.out.println("Hello, World 1"); // 람다를 변수에 할당

Runnable r2 = new Runnable() { // 익명 클래스 사용
    public void run() {
    	System.out.println("Hello, World 2"); 
    }
};

process(r1); // Hello, World 1 출력
process(r2); // Hello, World 2 출력
process(() -> System.out.println("Hello, World 3")); // Hello, World 3 출력 
```
### 2.2. 함수 디스크립터
람다 표현식의 시그니처를 서술하는 메서드를 **함수 디스크립터**라고 부른다.

람다 표현식은 함수형 인터페이스의 추상 메서드와 같은 시그니처를 갖는다. 추상 메서드의 시그니처와 일치하지 않으면 유효한 람다 표현식이 아니다.

```java
// 1번
execute(() -> {});
public void execute(Runnable r) {
    r.run();
}

// 2번
Predicate<Apple> p = (Apple a) -> a.getWeight();
```
다음 예제의 1번을 살펴보면 Runaable의 추상 메서드 run의 시그니처는 () -> void 이다. 람다 표현식의 시그니처와 일치하므로 유효한 람다 표현식이다.

2번을 살펴보면 Predicate의 추상 메서드 test의 시그니처는 (Apple) -> boolean 이다. 람다 표현식의 시그니처, (Apple) -> Integer와 일치하지 않으므로 유효한 람다 표현식이 아니다.

## 3. 자바 8의 새로운 함수형 인터페이스

자바 8은 java.util.function 패키지로 여러 가지 새로운 함수형 인터페이스를 제공한다.

### 3.1. @FunctionalInterface

자바 API를 살펴보면 @FunctionalInterface 어노테이션이 함수형 인터페이스에 추가되었다.

참고로 @FunctionalInterface로 인터페이스를 선언했지만 실제로 함수형 인터페이스가 아니라면, 예를 들어 추상 메서드가 한 개 이상이라면 컴파일러가 에러를 발생시킨다.

### 3.2. 기본형 특화

java.util.function 패키지의 함수형 인터페이스 Predicate<T\>, Consumer<T\>, Function<T, R> 등은 제네릭 함수형 인터페이스이다.

제네릭 파라미터에는 참조형(Integer, List, String 등) 만 사용할 수 있다.

자바에서는 기본형(int, double, byte, char 등)을 참조형으로 변환하는 기능인 **박싱**을 제공한다. 반대 동작을 **언박싱**이라고 하며 박싱과 언방식이 자동으로 이루어지는 **오토박싱**이라는 기능도 제공한다.

기본형을 스택에서 탐색한 뒤 래퍼(wrapper) 감싸고 힙에 저장하는 박싱 과정은 비용이 소모된다.

자바 8은 기본형을 입출력으로 사용하는 상황에서 오토박싱 동작을 피할 수 있도록 기본형에 특화된 함수형 인터페이스를 제공한다.
```java
// 기본형 특화
@FunctionalInterface
public interface IntPredicate {
    boolean test(int t);
}
// 제네릭
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
}
```

### 3.3. 예외

함수형 인터페이스는 확인된 예외를 던지는 동작을 허용하지 않는다.

따라서 예외를 던지는 람다 표현식을 만들려면 **1) 확인된 예외를 선언하는 인터페이스를 직접 정의**하거나
**2) 람다를 try/catch 블록으로 감싸야 한다**.

```java
/**
 * BufferedReader의 readLine()은 IOException을 던진다.
 */
// 1)
@FunctionInterface 
public interface BufferedReaderProcessor {
    String process(BufferedReader br) throws IOException;
}
// 2)
Function<BufferedReader, String> f = (BufferedReader br) -> {
    try {
        return br.readLine();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
};
```

## 4. 형식 검사, 형식 추론, 제약

### 4.1. 형식검사

자바 컴파일러는 람다가 사용되는 콘텍스트(context)를 이용해서 람다의 형식(type)을 추론할 수 있다. 

어떤 콘텍스트에서 기대되는 람다 표현식의 형식을 **대상 형식(target type)** 이라고 부른다. 

```java
List<Apple> heavierThan150g = filter(inventory, (Apple apple) -> apple.getWeight() > 150);
```

1. 람다가 사용된 콘텍스트를 찾기 위해 filter의 정의를 확인한다.

    ```java
    filter(List<Apple> inventory, Predicate<Apple> p)
    ```
2. 대상 형식은 Predicate<Apple\>이다. 다음으로 함수형 인터페이스의 추상메서드를 확인한다.
    ```java
    boolean test(Apple apple)
    ```
3. 함수 디스크립터는 <code> Apple -> boolean </code>이다. 람다의 시그니처와 일치하니 형식 검사에 성공한다.

### 4.2. 형식 추론

반대로 컴파일러는 람다 표현식이 사용된 콘텍스트(대상 형식)을 이용해서 람다의 파라미터 형식도 추론할 수 있다. 

```java
List<Apple> greenApples = filter(inventory, apple -> Green.equals(apple.getColor())); 
```
다음과 같이 파라미터에 형식을 명시적으로 지정하지 않아도 컴파일러가 람다 파라미터의 형식을 추론한다.

### 4.3. 제약

람다 표현식에서는 익명 함수가 하는 것처럼 **자유 변수**, 즉 파라미터로 넘겨진 변수가 아닌 외부에서 정의된 변수를 활용할 수 있다.

이와 같은 동작을 **람다  캡처링**이라고 부른다.

```java
int portNumber = 8080;
Runnable r = () -> System.out.println(portNumber);
```

하지만 자유 변수에 제약이 있다. 람다는 인스턴스 변수와 정적 변수(static)를 자신의 바디에서 참조할 수 있도록 자유롭게 캡처할 수 있다.

하지만 지역 변수는 명시적으로 final로 선언되어 있거나 실질적으로 final로 선언된 변수와 똑같이 사용되어야 한다.

```java
int portNumber = 8080;
Runnable r = () -> System.out.println(portNumber); // 에러 발생
portNumber = 8081;
```

왜 지역 변수에는 이러한 제약이 필요할까? 인스턴스 변수는 힙에 저장되는 반면 지역 변수는 스택에 위치한다. 힙은 모든 스레드가 공유하지만 스택은 스레드끼리 침범할 수 없다. 

변수 할당이 해제되어 변수를 할당한 스레드가 사라졌음에도 람다를 실행하는 스레드가 해당 변수에 접근할 수 있다. 

따라서 자바 구현에서는 원래 변수에 접근을 허용하는 것이 아닌 자유 지역 변수의 복사본을 제공한다. 따라서 복사본의 값이 바뀌지 않아야 하므로 지역 변수에는 한 번만 값을 할당해야 한다는 제약이 생긴다.

## 5. 메서드 참조

메서드 참조를 이용하면 기존의 메서드 정의를 재활용해서 람다처럼 전달할 수 있다. 

메서드 참조는 특정 메서드만을 호출하는 람다의 축약형으로 메서드 참조를 사용함으로써 가독성을 높일 수 있다.

### 5.1. 다양한 유형의 메서드 참조

메서드 참조는 세 가지 유형으로 구분할 수 있다.

1. 정적 메서드 참조
    ```java
    // 람다
    (args) -> ClassName.staticMethod(args)
    // 메서드 참조
    ClassName::staticMethod
    
    // 예시
    ToIntFunction<String> stringToInt = (String s) -> Integer.parseInt(s);
    
    ToIntFunction<String> stringToInt = Integer::parseInt; // Integer 클래스의 parseInt는 정적 메서드이다.
    ```
2. 다양한 형식의 인스턴스 메서드 참조
    
    여기서 arg0은 ClassName의 형식(Type)이다.
    ```java
    // 람다
    (arg0, rest) -> arg0.instanceMethod(rest)
    // 메서드 참조
    ClassName::instanceMethod

    // 예시
    BiPredicate<List<String>, String> contains = (list, element) -> list.contains(element);

    BiPredicate<List<String>, String> contains = List::contains; // 첫번째 인수가 List 형식이다.
    ```
3. 기존 객체의 인스턴스 메서드 참조
    람다 표현식에서 현존하는 외부 객체의 메서드를 호출할 때 사용된다. 
    
    이 유형의 참조는 비공개 헬퍼 메서드를 정의한 상황에서 유용하게 활용할 수 있다.
    ```java
    // 람다
    (args) -> expr.instanceMethod(args)
    // 메서드 참조
    expr::instanceMethod

    // 예시
    // 비공개 헬퍼 메서드
    private boolean isValidName(String string) {
        return Character.isUpperCase(string.charAt(0));
    } 

    Predicate<String> startsWithUpper = (String s) -> this.isValidName(s);

    Predicate<String> startsWithUpper = this::isValidName;
    ```

컴파일러는 람다 표현식의 형식을 검사하던 방식과 비슷한 과정으로 메서드 참조가 주어진 함수형 인터페이스와 호환하는지 확인한다.

따라서 메서드 참조는 콘텍스트의 형식과 일치해야 한다.

### 5.2. 생성자 참조
클래스명과 new 키워드를 이용해서 기존 생성자의 참조를 만들 수 있다. 이것은 정적 메서드의 참조를 만드는 방법과 비슷하다. 

```java
// 인수가 없는 생성자, Apple()의 생성자 참조
// Supplier의 함수 디스크립터는 void -> T 이다.
Supplier<Apple> c1 = () -> new Apple();

Supplier<Apple> c1 = Apple::new;
Apple a1 = c1.get();

// 인수가 한 개인 생성자, Apple(Integer weight)의 생성자 참조
// Function의 함수 디스크립터는 Integer -> T
Function<Integer, Apple> c2 = (weight) -> new Apple(weight);

Function<Integer, Apple> c2 = Apple::new;
Apple a2 = c2.apply(100);
```

## 6. 람다 표현식의 조합
자바 8의 API의 몇몇 함수형 인터페이스는 다양한 유틸리티 메서드를 포함한다.

유틸리티 메서드를 이용해 여러 개의 람다 표현식을 조합해서 복잡한 람다 표현식을 만들 수 있다.

1. Comparator
    ```java
    List<Apple> inventory = new ArrayList<>();
    inventory.sort(comparing(Apple::getWeight)) 
             .reversed() // 무게를 내림차순으로 정렬
             .thenComparing(Apple::getColor); // 무게가 같으면 색깔별로 정렬
    ```
2. Predicate
    ```java
    // 빨간색 사과
    Predicate<Apple> redApple = (apple) -> RED.equals(apple.getColor());
    // 특정 프레디케이트 반전, 빨간색이 아닌 사과
    Predicate<Apple> notRedApple = redApple.negate();
    // 빨간색이면서 무거운 사과 혹은 그냥 녹색 사과
    Predicate<Apple> redAndHeavyApple = redApple.and(apple -> apple.getWeight() > 150)
                                                .or(apple -> GREEN.equals(apple.getColor()));
    ```
3. Function
    ```java
    Function<Integer, Integer> f = x -> x + 1;
    Function<Integer, Integer> g = x -> x * 2;
    // andThen g(f(x))
    Function<Integer, Integer> h1 = f.andThen(g);
    // compose f(g(x))
    Function<Integer, Integer> h2 = f.compose(g);
    ```
   
위와 같이 여러 유틸리티 메서드를 조합해서 다양한 변환 파이프라인을 만들 수 있다.

---
💡 **Key Words**

람다 표현식, 함수형 인터페이스, 대상 형식, 메서드 참조, 유틸리티 메서드