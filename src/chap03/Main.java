package chap03;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.*;

class Apple {
	private String color;
	private Integer weight;
	
	public Apple(String color, Integer weight) {
		this.color = color;
		this.weight = weight;
	}
	
	public String getColor() {
		return color;
	}
	
	public Integer getWeight() {
		return weight;
	}
}

class AppleComparator implements Comparator<Apple> {
	
	@Override
	public int compare(Apple o1, Apple o2) {
		return o1.getWeight().compareTo(o2.getWeight());
	}
	
}

public class Main {
	
	public static void main(String[] args) {
		/**
		 * 사과의 무게를 기준으로 사과 리스트를 정렬한다.
		 * List API는 sort 메서드를 지원한다.
		 * sort 메서드는 Comparator 객체를 인수로 받아 두 사과를 비교한다.
		 * 다양한 방식으로 sort 메서드에 정렬 전략을 전달, sort의 동작을 파라미터화하자.
		 */
		List<Apple> apples = new ArrayList<>();
		// 1. 객체 안에 동작을 포함시킴
		apples.sort(new AppleComparator());
		
		// 2. 익명 클래스 사용
		apples.sort(new Comparator<Apple>() {
			@Override
			public int compare(Apple o1, Apple o2) {
				return o1.getWeight().compareTo(o2.getWeight());
			}
		});
		
		// 3. 람다 표현식 사용, Comparator의 함수 디스크립터는 (T, T) -> int
		apples.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
		apples.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight())); // 형식 추론
		
		// 4. Comparable의 키를 추출해서 Comparator 객체로 만드는 comparing 정적 메서드 사용
		// 참고로 comparing은 Function 함수를 인수로 받는다.
		// static import
		Comparator<Apple> c1 = comparing(a -> a.getWeight());
		apples.sort(c1);
		
		// 5. 메서드 참조 사용
		Comparator<Apple> c2 = comparing(Apple::getWeight);
		apples.sort(c2);
	}
	
}
