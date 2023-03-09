# 05장

스트림 API가 지원하는 다양한 연산을 살펴보자.

## 1. 필터링
### 1.1. filter
스트림 인터페이스는 filter 메서드를 지원한다. 
filter 메서드는 **프레디케이트**(T -> boolean)를 인수로 받아서 프레디케이트와 일치하는 모든 요소를 포함하는 스트림을 반환한다.

```java
List<Dish> vegeterianMenu = menu.stream()
                                .filter(Dish::isVegetarian)
                                .collect(toList());
```

### 1.2. distinct
스트림은 고유 요소로 이루어진, 중복된 요소가 없는 스트림을 반환하는 distinct 메서드도 지원한다.
고유 여부는 스트림에서 만든 객체의 hashCode, equals로 결정된다.

```java
List<Integer> numbers = Arrays.asList( 1, 2, 1, 4, 3, 3, 2 );
numbers.stream()
        .filter(i -> i % 2 == 0) // 짝수만 필터링
        .distinct()
        .forEach(System.out::println);
```

## 2. 슬라이싱

자바 9에 새롭게 추가된 기능으로 스트림의 요소를 선택하거나 스킵하는 다양한 방법을 제공한다.

### 2.1. takeWhile과 dropWhile
```java
List<Integer> nums = Arrays.asList( 1, 4, 7, 19, 24 );
```
위 리스트는 이미 정렬되어 있다. 
여기에 filter 연산을 이용하여 10보다 작은 숫자만 필터링한다면 어떻게 동작할끼?
**전체 스트림을 반복하면서** 각 요소에 프레디케이트를 적용하게 된다.

하지만 리스트가 이미 정렬되어 있다는 사실을 이용해 10보다 크거나 같은 수가 나왔을 때 **반복 작업을 중단**할 수 있다.
이와 같은 동작은 아주 많은 요소를 포함하는 큰 스트림에서는 상당한 차이가 될 수 있다.

takeWhile 연산을 이용하여 이를 간단하게 처리한다.
takeWhile을 이용하면 무한 스트림을 포함한 모든 스트림에 프레디케이트를 적용해 스트림을 슬라이스할 수 있다.

```java
List<Integer> underTenNums = 
        nums.stream()
            .takeWhile(n -> n < 10)
            .collect(toList());
```

dropWhile은 takeWhile과 정반대의 작업을 수행한다.
dropWhile은 프레디케이트가 처음으로 거짓이 되는 지점까지 발견된 요소를 버린다.
따라서 위 코드에 dropWhile을 적용하면 10보다 크거나 같은 요소를 선택한다.

```java
List<Integer> upTenNums =
        nums.stream()
            .dropWhile(n -> n < 10)
            .collect(toList());
```

### 2.2. 스트림 축소와 요소 건너뛰기

스트림은 주어진 값 이하의 크기를 갖는 새로운 스트림을 반환하는 limit(n) 메서드를 지원한다.
스트림이 정렬되어 있으면 최대 요소 n개를 반환할 수 있다. 
하지만 소스가 정렬되어 있지 않다면 limit의 결과도 정렬되지 않은 상태로 반환된다.

그리고 스트림은 처음 n개 요소를 제외한 스트림을 반환하는 skip(n) 메서드를 지원한다.
n개 이하의 요소를 포함하는 스트림에 skip(n)을 호출하면 빈 스트림이 반환된다.

## 3. 매핑

특정 객체에서 특정 데이터를 선택하는 기능을 제공한다.

### 3.1. map

스트림은 Function(T -> R)을 인수로 받는 map 메서드를 지원한다.
인수로 제공된 함수는 각 요소에 적용되며 함수를 적용한 결과가 새로운 요소로 매핑된다.

```java
List<String> dishNames = 
        menu.stream()
            .map(Dish::getName)
            .collect(toList());
```

다음은 스트림의 요리명을 추출하는 코드이다.
getName은 문자열을 반환하므로 map 메서드의 출력 스트림은 Stream<String>형식을 갖는다.

### 3.2. flatMap

리스트에서 고유 문자로 이루어진 리스트를 반환하는 코드를 작성해보자.
예를 들어 ["Hello", "World"] 리스트가 있다면 결과로 ["H", "e", "l", "o", "W", "r", "d"]를 포함하는 리스트가 반환되어야 한다.

```java
List<String[]> uniqueCharacters =
    words.stream()
        .map(word -> word.split("")) // Stream<String[]> 반환
        .distinct()
        .collect(toList());
```

다음 코드에서 map 메소드가 반환한 스트림의 형식은 Stream<String[]>이다.
하지만 우리는 문자열의 스트림을 표현할 Stream<String>이다.

우선 배열 스트림 대신 문자열 스트림이 필요하다.

```java
List<Stream<String>> uniqueCharacters = 
    words.stream()
        .map(word -> word.split("")) // Stream<String[]> 반환
        // 각 배열을 별도의 스트림으로 생성
        .map(Arrays::stream) // Stream<Stream<String>> 반환
        .distinct()
        .collect(toList()); 
```

문자열을 받아 스트림을 만드는 Arrays.stream() 메서드를 이용한 결과이다.
결국 문자열 스트림 리스트가 만들어지면서 문제가 해결되지 않았다.
flatMap을 사용하면 다음 문제를 해결할 수 있다.

```java
List<String> uniqueCharaters = 
    words.stream()
        .map(word -> word.split(""))
        // 생성된 스트림을 하나의 스트림으로 평면화
        .flatMap(Arrays::stream) // Stream<String> 반환
        .distinct()
        .collect(toList());
```

flatMap은 map과 달리 **하나의 평면화된 스트림**을 반환한다. flatMap의 함수 디스크립터는 T -> Stream<R>이다.
flatMap 메서드는 스트림의 각 값을 다른 스트림으로 만든 다음에 **모든 스트림을 하나의 스트림으로 연결**하는 기능을 수행한다.

## 4. 검색과 매칭
### 4.1. 매칭
- anyMatch
  
  프레디케이트가 주어진 스트림에서 적어도 한 요소와 일치하는지 확인한다.

- allMatch

  anyMatch와 달리 스트림의 모든 요소가 주어진 프레디케이트와 일치하는지 검사한다.

- noneMatch

  allMatch와 반대 연산을 수행한다. 주어진 프레디케이트와 일치하는 요소가 없는지 확인한다.

위 세 메서드는 모두 불리언(boolean)을 반환하므로 최종 연산이다.

### 4.2. 쇼트 서킷
그리고 세 메서드는 스트림 **쇼트서킷** 기법, 자바의 &&, ||과 같은 연산을 활용한다.

때로는 전체 스트림을 처리하지 않았더라도 결과를 반환할 수 있다.
예를 들어 여러 and 연산으로 연결된 커다란 표현식을 평가한다고 가정하자.
표현식에서 하나라도 거짓하는 결과가 나오면 나머지 표현식의 결과와 상관없이 전체 결과는 거짓이 된다.
이러한 상황을 **쇼트 서킷**이라고 한다.

스트림의 일부 연산은 모든 스트림의 요소를 처리하지 않고도 결과를 반환할 수 있다.

### 4.3. 검색
- findAny
  
  현재 스트림에서 임의의 요소를 반환한다. findAny 메서드는 다른 스트림 연산과 연결해서 사용할 수 있다.
  그리고 쇼트서킷을 이용해서 결과를 찾는 즉시 실행을 종료한다.
  ```java
  Optional<Dish> dish = menu.stream()
                            .filter(Dish::isVegetarian)
                            .findAny();
  ```
  
- findFirst
  
  정렬된 연속 데이터로부터 생성된 스트림처럼 일부 스트림에는 **논리적인 아이템 순서**가 정해져 있을 수 있다.
  이러한 스트림에서 첫 번째 요소를 찾기 위해 findFirst 연산을 이용한다.
  ```java
  Optional<Integer> firstEvenNumber =
    numbers.stream()
           .filter(n -> n % 2 == 0)
           .findFirst();
  ```
  
병렬 실행에서는 첫 번째 요소를 찾기 어렵다. 
따라서 요소의 반환 순서가 상관없다면 병렬 스트림에서는 제약이 적은 findAny를 사용한다.

### 4.4. Optional

findAny와 findFirst는 Optional<T\> 클래스(java.util.Optional)를 반환한다.

Optional<T\> 클래스는 값의 존재 여부를 표현하는 컨테이너 클래스이다.

위에서 소개한 findAny는 아무 요소도 반환하지 않을 수 있다. 
null은 쉽게 에러를 일으킬 수 있으므로 자바 8 설계자가 Optional<T\>를 만들었다.

Optional은 값이 존재하는지 확인하고 값이 없을 때 어떻게 처리할지 강제하는 기능을 제공한다.

- isPresent()
  
  Optional이 값을 포함하면 true를 반환하고 값을 포함하지 않으면 false를 반환한다.

- isPresent(Consumer<T> block) 

  Consumer 함수형 인터페이스의 시그니처는 T -> void이다. 
  값이 있으면 주어진 블록을 실행한다.

- T get()

  값이 존재하면 값을 반환하고 값이 없으면 NoSuchElementException을 일으킨다.

- T orElse(T other)
  
  값이 있으면 값을 반환하고 값이 없으면 기본값(other)을 반환한다.

## 5. 리듀싱
**리듀싱 연산**은 모든 스트림 요소를 처리해서 값으로 도출하는 연산이다.
함수형 프로그래밍 언어 용어로는 이 과정이 마치 종이를 작은 조각이 될 때까지 반복해서 접는 것과 비슷하단 의미로
**폴드**라고 부른다.

### 5.1. 요소의 합
for-each 루프를 이용해서 리스트의 숫자 요소를 모두 더하는 코드는 다음과 같다
```java
int sum = 0;
for (int num : nums) sum += num;
```
이러한 상황에서 reduce를 이용하면 애플리케이션의 반복된 패턴을 추상화할 수 있다.
```java
int sum = numbers.stream().reduce(0, (a, b) -> a + b);
```
여기서 첫번째 인자인 0은 초깃값이다. 
그리고 두번째 인자는 두 요소를 조합해서 새로운 값을 만드는 BinaryOperator<T\>이다.

메서드 참소를 이용해서 이 코드를 더 간결하게 만들 수 있다.
자바 8의 Integer 클래스는 두 숫자는 더하는 정적 메서드 sum을 제공한다.
```java
int sum = numbers.stream().reduce(0, Integer::sum);
```

초깃값을 받지 않도록 오버로드된 reduce도 있다.
그러나 이 reduce는 Optional 객체를 반환한다.
스트림에 아무 요소도 없는 상황에서 초깃값이 없으므로 reduce는 합계를 반환할 수 없다.
따라서 합계가 없음을 가리킬 수 있도록 Optional 객체로 감싼 결과를 반환한다.
```java
Optional<Integer> sum = numbers.stream().reduce((a, b) -> a + b);
```

### 5.2. 요소의 최댓값과 최솟값
reduce를 이용하여 스트림 요소들의 최댓값과 최솟값을 찾을 수 있다.
두 요소에서 최댓값을 반환하는 람다만 있으면 된다.

reduce 연산은 새로운 값을 이용해서 _스트림의 모든 요소를 소비할 때까지 람다를 반복 수행_ 하면서 최댓값을 생상한다.

```java
Optional<Integer> max = numbers.stream().reduce(Integer::max);
```

반복적인 합계에서는 sum 변수를 공유해야하므로 쉽게 병렬화하기 어렵다.
그러나 reduce를 이용하면 내부 반복이 추상화되면서 내부 구현에서 병렬로 reduce를 실행할 수 있다.

### 5.3. 스트림 연산 - 상태 없음과 상태 있음

스트림의 각각의 연산은 내부적인 상태를 고려해야 한다. 

map, filter 등은 입력 스트림에서 각 요소를 받아 결과를 출력 스트림으로 보낸다.
이러한 연산들은 상태가 없는, 내부 상태를 갖지 않는 연산이다.

하지만 reduce, sum, max 같은 연산은 결과를 누적할 내부 상태가 필요하다.
앞선 예제에서 int 또는 double로 내부 상태로 사용했다.
스트림에서 처리하는 요소의 수와 상관없이 **내부 상태의 크기는 한정(bound)** 되어 있다.

그리고 sorted와 distinct 같은 연산은 스트림의 요소를 정렬하거나 중복을 제거하려면 **과거의 이력을 알고 있어야 한다.**
결국 **모든 요소가 버퍼에 추가되어 있어야 하고** 따라서 데이터의 스트림의 크기가 크거나 무한이라면 문제가 생길 수 있다.
이러한 연산을 **내부 상태를 갖는 연산**이라고 한다.

😅 여담

사실 이 부분이 아무리 읽어도 이해가 가지 않았다. 
하지만 뒷부분을 읽을수록 조금씩 이해가 된다.
상태가 있음과 없음을 판단하는 기준은 "과거의 이력을 알고 있어야 한다" 이것이다. 

n번째 요소까지의 합을 계산하려면 n - 1번째 요소의 합을 알아야 한다.
n개의 요소를 정렬하려면 1부터 n번째 수가 무엇인지 알아야 한다.
이러한 연산들이 상태가 있는 연산이다.
참고도 뒤에서 예시가 계속 나온다.

## 6. 숫자형 스트림
### 6.1. 기본형 특화 스트림
reduce 메서드를 이용해 칼로리의 합을 구하는 코드는 다음과 같다.
```java
int calories = 
        menu.stream()
            .map(Dish::getCalories)
            .reduce(0, Integer::sum);
```
위 코드는 내부적으로 합계를 계산하기 전에 Integer을 기본형으로 언박싱해야 하므로 박싱 비용이 발생한다.
그리고 map 메서드가 Stream<T\>를 생성하므로 sum 메서드를 직접 호출할 수 없다.

스트림 API는 박싱 비용을 피할 수 있도록 숫자 스트림을 효율적으로 처리하는 **기본형 특화 스트림**을 제공한다.

스트림을 특화 스트림으로 변환할 때는 mapToInt, mapToDouble, mapToLong 세 가지 메서드를 가장 많이 사용한다.
map과 정확히 같은 기능을 수행하지만 Stream<T\>대신 특화 스트림(IntStream, DoubleStream, LongStream)을 반환한다.

IntStream은 sum, max, min, average 등 다양한 유틸리티 메서드를 지원한다. 
여기서 sum을 살펴보자면 sum은 스트림이 비어있으면 기본값 0을 반환한다.  

반대로 boxed 메서드를 이용해서 특화 스트림을 객체 스트림으로 변환할 수 있다.
```java
IntStream intStream = menu.stream().mapToInt(Dish::getCalories); 
Stream<Integer> stream = intStream.boxed();
```
### 6.2. Optional 기본형
4.4에서 값이 존재하는지 여부를 가리킬 수 있는 컨테이너 클래스 Optional이 등장했다.
OptionalInt, OptionalDouble, OptionalLong 세 가지 기본형 특화 버전도 있다.

앞에서 IntStream의 sum은 스트림에 요소가 없을 때 기본값 0을 반환했다.
하지만 IntStream에서 최댓값을 찾을 때 0이라는 기본값 때문에 잘못된 결과가 도출될 수 있다.
바로 스트림에 요소가 없는 경우와 실제 최댓값이 0인 상황을 구분할 수 없다는 점이다.

따라서 max 메서드는 OptionalInt를 반환한다. 
그리고 OptionalInt를 이용해서 최댓값이 없는 상황에 사용할 기본값을 명시적으로 정의할 수 있다. 
```java
OptionalInt maxCalories = menu.stream()
                        .maxToInt(Dish::getCalories)
                        .max();

int max = maxCalories.orElse(1); // 값이 없을 경우 1 반환
```

### 6.3. 숫자 범위
자바 8의 IntStream과 LongStream에서는 range와 rangeClosed라는 두 가지 정적 메서드를 제공한다.
두 메서드는 특정 범위의 숫자를 이용해야 하는 상황에서 사용한다.

두 메서드 모두 첫 번째 인수로 시작값을 두 번째 인수로 종료값을 가진다.

**range**는 시작값은 포함되지만 종료값은 결과에 포함되지 않는다.
반면에 **rangeClosed**는 시작값과 종료값이 결과에 포함된다는 점이 다르다.

🙄 **여담**

책에서는 range는 시작값과 종료값 모두 결과에 포함되지 않는다고 적혀있었다.
그런데 자바는 range(startInclusive, endExclusive)라고 말한다.
그러면 시작값은 포함이고 종료값은 포함이 아니다. 이런, 내가 독해력이 부족한건지 책이 잘못된건지..

## 7. 다양한 스트림 만들기
### 7.1. 값, 배열, 파일을 이용한 스트림
- 값으로 스트림 만들기
  임의의 수를 인수로 받는 정적 메서드 **Stream.of**를 이용해서 스트림을 만들 수 있다.
  그리고 empty 메서드를 이용해서 스트림을 비울 수 있다.
  ```java
  Stream<String> stream = Stream.of("Modern", "Java", "In", "Action"); // 문자열 스트림 만들기
  Stream<String> emptyStream = Stream.empty(); // 스트림 비우기
  ```
  자바 9에서 null이 될 수 있는 객체를 스트림으로 만들 수 있는 새로운 메서드가 추가되었다.
  **ofNullable** 메서드는 객체가 null일 경우 빈 스트림으로 만든다.
  ```java
  Stream<String> homeValueStream = Stream.ofNullable(System.getProperty("home"));
  ```
  여기서 System.getProperty는 자바가 실행되는 곳과 운영체제 정보를 알아낼 때 사용하는 메서드이다.
  System.getProperty는 제공된 키에 대응하는 속성이 없으면 null을 반환한다.

- 배열로 스트림 만들기
  배열을 인수로 받는 정적 메서드 Arrays.stream을 이용해서 스트림을 만들 수 있다.
  예를 들어 기본형 int로 이루어진 배열을 IntStream으로 변환한다.

- 파일로 스트림 만들기
  파일을 처리하는 등의 I/O 연산에 사용하는 자바의 NIO API(비블록 I/O)는 스트림 API를 사용할 수 있다.
  예를 java.nio.file.Files의 정적 메서드, Files.lines는 주어진 파일의 각 행 요소를 문자열 스트림(Stream<String\>)으로 반환한다.
  
  스트림의 소스가 I/O 자원이므로 메모리 누수를 막으려면 자원을 닫아야 한다. 
  하지만 Stream 인터페이스는 AutoCloseable 인터페이스를 구현한다. 따라서 I/O 자원은 자동으로 관리된다. 

### 7.2. 무한 스트림 - iterate, generator
스트림 API는 함수에서 스트림을 만들 수 있는 두 정적 메서드 Stream.iterate와 Stream.generator를 제공한다.
두 연산을 이용해서 크기가 고정되지 않는 스트림, **무한 스트림**을 만들 수 있다.

먼저 iterate을 살펴보면, iterate 메서드는 초깃값과 람다(UnaryOperator<T\>를 사용)를 인수로 받아서 새로운 값을 끊임없이 생산할 수 있다.
```java
Stream.iterate(0, n -> n + 2)
    .limit(10)
    .forEach(System.out::println);
```
위 코드는 짝수를 무한히 생성하고 있다. 
iterate는 요청할 때마다 값을 생산할 수 있으며 끝이 없으므로 무한 스트림을 만든다.
이러한 스트림을 **언 바운드 스트림(unbounded stream)**이라고 표현한다. 이러한 특징이 스트림과 컬렉션의 큰 차이점이다.

그리고 자바 9은 iterate 메서드는 프레디케이트를 지원한다.
iterate는 두 번째 인수로 프레디케이트를 받아 언제까지 작업을 수행할 것인지 기준으로 사용한다.
아래는 100 이하의 4의 배수를 생산하는 코드이다. 
```java
IntStream.iterate(0, n -> n < 100, n -> n + 4)
        .forEach(System.out::println);
```

다음으로 generate는 iterate과 달리 생산된 각 값을 연속적으로 계산하지 않는다.
generate는 Supplier<T\>를 인수로 받아서 새로운 값을 생산한다.

아래는 0에서 1 사이의 임의의 double 숫자 다섯 개를 만드는 코드이다. 
```java
Stream.generate(Math::random)
    .limit(5)
    .forEach(System.out::println);
```

iterate와 generator의 예제에서 두 메서드의 차이를 확인할 수 있다.
iterate는 기존 결과에 의존해서 순차적으로 연산을 수행한다. 
따라서 나중에 계산에 사용할 어떠한 값이 있어야 하므로 상태가 있는 연산이다.

반대로 generator의 Supplier은 나중에 계산에 사용할 어떤 값도 저장해두지 않는다.
따라서 상태가 없는 메서드이다. 
물론 발행자, Supplier에 상태를 저장하여 다음에 스트림의 다음 값을 만들 때 상태를 고칠 수 있다.
(modern_java_in_action.chap05.Fibonacci 참고)

하지만 가변 상태 객체는 병렬 코드에서 안전하지 않음을 [1장](https://github.com/heenahan/IT-books/tree/main/src/modern_java_in_action/chap01/#23-공유되지-않는-가변-데이터) 에서 설명했다.
iterate의 경우 각 과정에서 새로운 값을 생성하면서도 기존 상태를 바꾸지 않는 **순수한 불변상태**를 유지한다. 

결론은 스트림을 병렬로 처리하면서 올바른 결과를 얻으려면 **불변 상태 기법**을 사용해야 한다.

그리고 무한한 크기를 가진 스트림을 처리할 때는 limit를 이용해서 명시적으로 스트림의 크기를 제한해야 한다.
마찬가지로 무한 스트림의 요소는 무한적으로 계산이 반복되므로 정렬하거나 리듀스할 수 없다.

---
💡 **Key word**

다양한 스트림 연산, 쇼트서킷, 상태 없는 연산, 상태 있는 연산, 기본형 특화 스트림, 무한 스트림