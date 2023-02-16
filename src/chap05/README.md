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

flatMap은 map과 달리 **하나의 평면화된 스트림**을 반환한다.
flatMap 메서드는 스트림의 각 값을 다른 스트림으로 만든 다음에 모든 스트림을 하나의 스트림으로 연결하는 기능을 수행한다.