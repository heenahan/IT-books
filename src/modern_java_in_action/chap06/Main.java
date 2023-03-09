package modern_java_in_action.chap06;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

import static modern_java_in_action.chap06.Dish.Type.*;

public class Main {
	
	public static void main(String[] args) {
		Main T = new Main();
		List<Dish> menu = T.getDishList();
		// 칼로리를 계산하는 다양한 방법
		
		// 1. Collector의 reducing
		Integer sum1 = menu.stream().collect(reducing(0, Dish::getCalories, (a, b) -> a + b));
	
		// 2. reduce 연산
		Optional<Integer> sum2 = menu.stream().map(Dish::getCalories).reduce(Integer::sum);
	
		// 3. IntStream으로 자동 언박싱 후 sum 메서드 사용
		int sum3 = menu.stream().mapToInt(Dish::getCalories).sum();
	}
	
	public List<Dish> getDishList() {
		return Arrays.asList(
				new Dish("pork", false, 800, MEAT),
				new Dish("beef", false, 700, MEAT),
				new Dish("chicken", false, 400, MEAT),
				new Dish("french fries", true, 530, OTHER),
				new Dish("rice", true, 350, OTHER),
				new Dish("season fruit", true, 120, OTHER),
				new Dish("pizza", true, 550, OTHER),
				new Dish("prawns", false, 300, FISH),
				new Dish("salmon", false, 450, FISH)
		);
	}
	
}
