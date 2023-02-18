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

그리고 sorted와 distinct 같은 연산은 스트림의 요소를 정렬하거나 중복을 제거하려면 과거의 이력을 알고 있어야 한다.
결국 **모든 요소가 버퍼에 추가되어 있어야 하고** 따라서 데이터의 스트림의 크기가 크거나 무한이라면 문제가 생길 수 있다.
이러한 연산을 **내부 상태를 갖는 연산**이라고 한다.