# 06 장

## 1. Collector
스트림에서 최종 연산 collect는 Collector 함수형 인터페이스를 매개변수로 받는다.
collect를 호출하면 스트림의 요소에 리듀싱 연산이 수행된다. 
collect에서는 _리듀싱 연산을 이용해서 스트림의 각 요소를 방문하면서 Collector가 작업을 처리한다._

리듀싱 연산에 대해 [5장](https://github.com/heenahan/IT-books/tree/main/src/modern_java_in_action/chap05#5-리듀싱) 에서 설명했다.

데이터 자체를 변환하는 것보다는 데이터 저장 구조를 변환할 때 Collector를 적용한다.
Collector를 적용하면 최종 결과를 저장하는 자료구조에 값을 누적한다.

Collectors 클래스에서 팩토리 메서드를 제공한다. 팩토리 메서드의 기능은 크게 세 가지로 구분된다.

- 리듀싱과 요약(summarize)
- 요소 그룹화
- 요소 분할

## 2. 리듀싱과 요약

Collector로 스트림의 항목을 컬렉션으로 재구성할 수 있다. 
즉, Collector로 스트림의 모든 항목을 하나의 결과로 합칠 수 있다.
다수준 맵(Map), 단순한 정수(int, long) 등 다양한 형식으로 결과가 도출될 수 있다.

### 2.1. couting, maxBy, minBy
- counting
  
  다음과 같이 counting이라는 팩토리 메서드로 스트림 요소의 수를 계산한다.

  ```java
  import static java.util.stream.Collectors.*;
    
  long howManyDishes = menu.stream().collect(counting());
  ```
  
앞으로 Collectors 클래스의 정적 팩토리 메서드를 모두 import했다고 가정한다.  

- maxBy, minBy
  
  maxBy와 minBy 두 메서드를 이용해서 스트림의 최댓값과 최솟값을 계산할 수 있다.
  두 Collector는 스트림의 요소를 비교하는 데 사용할 Comparator를 인수로 받는다.

  ```java  
  // 칼로리를 기준으로 비교
  Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
  Optional<Dish> mostCalorieDish = menu.stream()
                                       .collect(maxBy(dishCaloriesComparator));
  ```
  
스트림에 있는 객체의 숫자 필드의 합계나 평균 등을 반환하는 연산에도 리듀싱이 자주 사용된다.
이러한 연산을 **요약(summarization) 연산**이라 부른다.

### 2.2. summingInt, averagingInt, summarizingInt

- summingInt
  <br><br>
  summingInt는 객체를 int로 매핑하는 함수를 인수로 받는다. 
  여기서 객체를 int로 매핑하는 함수는 ToIntFuction 함수형 인터페이스이며 함수 시그니처는 T -> int이다.
  <br><br>
  int로 매핑된 각 Collector의 값을 탐색하면서 초깃값, 0으로 설정되어 있는 누적자에 더한다.  

  ```java
  int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
  ```
  
  Collectors.summingLong과 Collectors.summingDouble 메서드는 같은 방식으로 동작하며 각각 long 또는 double 형식의 데이터로 요약한다는 점만 다르다.
  <br><br>
- averagingInt
  <br><br>
  Collector은 단순 합계 외에 평균값 계산 등의 연산도 요약 기능으로 제공한다.
  averagingInt, averagingLong, averagingDouble 등으로 다양한 형식으로 이루어진 숫자 집합의 평균을 계산할 수 있다.
  <br><br>
  ```java
  double avgCalories = menu.stream().collect(averagingInt(Dish::getCalories));
  ```
  <br><br>
- summarizingInt
  <br><br>
  Collector를 이용해 스트림의 요소의 개수를 계산하고, 최댓값과 최솟값을 찾고 합계와 평균을 계산했다.
  이들 중 두 개 이상의 연산을 한 번에 수행해야할 때, 팩토리 메서드 summarizingInt가 반환하는 Collector를 사용한다.
  <br><br>
  다음은 메뉴의 요소의 개수, 요리의 칼로리의 합계, 평균, 최댓값, 최솟값을 계산하는 코드이다.
  코드가 실행되면 IntSummaryStatistics 클래스로 모든 정보가 수집된다.

  ```java
  IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));
  // IntSummaryStatistics{count=9, sum=4300, min=120, average=477.777778, max=800}
  ```
  
  int뿐만 아니라 long, double에 대응하는 summarizingLong, summarizingDoulbe 메서드가 있다.
  이 메서드들은 각각 LongSummaryStatistics와 DoubleSummaryStatistics 클래스를 반환한다.

### 2.3. 문자열 연결

Collector의 **joining** 팩토리 메서드를 이용하면 스트림의 각 객체에 toString 메서드를 호출해서 추출한 모든 문자열을 하나의 문자열로 연결해서 반환한다.

```java
String shortMenu = menu.stream().map(Dish::getName).collect(joining());
```
  
joining 메서드는 내부적으로 StringBuilder를 이용해서 문자열을 하나로 만든다.
위 코드에서 Dish 클래스가 요리명을 반환하는 toString 메서드를 포함하고 있다면 map으로 요리명을 추출하는 과정을 생략할 수 있다.

```java
String shortMenu = menu.stream().collect(joining());
```

연결된 두 요소 사이에 구분 문자열을 넣을 수 있도록 오버로드된 joining 팩토리 메서드도 있다.

```java
String shortMenu = menu.stream().collect(joining(", "));
```

### 2.4. 범용 리듀싱

2.1부터 2.3까지 살펴본 모든 컬렉터는 reducing 팩토리 메서드로도 정의할 수 있다.
범용 팩토리 메서드인 reducing 대신 특화된 Collector를 사용한 이유는 프로그래밍적 편의성 때문이다.

앞서 summingInt로 칼로리의 합계를 계산했다. 아래는 반대로 reducing 메서드를 이용한 계산이다.

```java
int totalCalories = menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> i + j));
```

reducing은 세 개의 인수를 받는다.
- 첫 번째 인수는 identity로 리듀싱 연산의 시작값이거나 스트림에 요소가 없을 때는 반환값이다.
  (책에는 '스트림에 인수가 없을 때'라 되어 있는데 요소같다... 실제로 빈 스트림을 돌려보면 결과값이 첫번째 인수가 나온다.)
- 두 번째 인수는 mapper로 Function<T, R> 함수형 인터페이스로 변환 함수이다. 함수 시그니처는 T -> R이다.
- 세 번째 인수는 같은 종류의 두 항목을 하나의 값으로 더하는 BinaryOperator<T, T, T>이다. 함수 시그니처는 (T, T) -> T이다.

다음은 한 개의 인수를 갖는 reducing 팩토리 메서드를 이용해서 가장 칼로리가 높은 요리를 찾는 방법이다.

```java
Optional<Dish> mostCalorieDish = 
        menu.stream()
            .collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
```

위 코드를 세 개의 인수를 받는 reducing으로 바꿔보면 다음과 같다. 
하나씩 살펴보면 스트림의 첫번째 요소를 첫 번째 인수로 받는다. 
그리고 자신을 그대로 반환하는 항등 함수를 두 번째 인수로 받는다.

```java
Dish mostCalorieDish = 
        menu.stream()
            .collect(reducing(menu.get(0), d -> d, (d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
```

하지만 전자는 컨테이너 Optional를 반환한다. 
빈 스트림에 한 개의 인수를 갖는 reducing 연산을 수행하면 시작값이 설정되지 않기 때문이다.

### 2.5. collect vs reduce

Stream 인터페이스의 collect와 reduce 메서드의 차이를 알아보자.
아래는 reduce 메서드를 사용하여 Collector의 toList를 구현한 코드이다.

```java
List<Integer> numbers = stream.reduce(
            new ArrayList<Integer>(), 
            (List<Integer> l, Integer e) -> {
                l.add(e);
                return l;
            },
            (List<Integer> l1, List<Integer> l2) ->{
                l1.addAll(l2);
                return l1;
            }
        );
```

다음 코드는 의미론적 문제와 실용성 문제, 즉 두 가지 문제가 발생한다.
collect 메서드는 도출하려는 결과를 컨테이너에 누적한다. 따라서 이 누적하는 컨테이너를 바꾸도록 설계된 메서드이다.
반면에 reduce 메서드는 두 값을 하나로 도출하는 **불변형 연산**이다. 따라서 의미론적인 문제가 발생한다.

위 코드의 reduce 메서드는 누적자로 사용된 리스트를 변환시킨다.
의미론적으로 reduce 메서드를 잘못 사용하면 실용성 문제도 발생한다.
여러 스레드가 동시에 같은 데이터 구조체를 고치면 리스트 자체가 망가지므로 리듀싱 연산을 병렬로 수행할 수 없다.
따라서 **가변 컨테이너 관련 작업이면서 병렬성을 확보**하려면 collect 메서드로 리듀싱 연산을 구현해야 한다.

함수형 프로그래밍에서는 하나의 연산을 다양한 방법으로 해결할 수 있다.
스트림 인터페이스에서 직접 제공하는 메서드를 이용하는 것에 비해 Collector를 이용하는 코드가 더 복잡하다.
하지만 코드가 복잡한 대신 재사용성과 커스터마이징 가능성을 제공하며 높은 수준의 추상화와 일반화를 얻을 수 있다.
