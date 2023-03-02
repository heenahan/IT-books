package modern_java_in_action.chap05;

import java.util.Arrays;
import java.util.List;

import static modern_java_in_action.chap05.Dish.Type.*;

public class Searching {
	
	public static void main(String[] args) {
		Searching T = new Searching();
		List<Dish> menu = T.getDishList();
		
		menu.stream()
			.filter(Dish::isVegetarian)
			.findAny() // Optional<Dish> 반환
			.ifPresent(dish -> System.out.println(dish.getName()));
		
		List<Integer> nums = Arrays.asList(1, 3, 5);
		Integer num = nums.stream()
						.filter(n -> n % 2 == 0)
						.findFirst()
						.orElse(2); // 값이 존재하지 않아 2 반환
		
		System.out.println(num);
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
