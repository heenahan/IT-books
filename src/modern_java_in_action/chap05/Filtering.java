package modern_java_in_action.chap05;

import java.util.Arrays;
import java.util.List;

import static modern_java_in_action.chap05.Dish.Type.*;
import static java.util.stream.Collectors.toList;

public class Filtering {
	
	public static void main(String[] args) {
		Filtering T = new Filtering();
		// 고기 요리 두 개만 반환
		List<Dish> menu = T.getDishList();
		List<Dish> twoMeatDish = menu.stream()
				.filter(dish -> MEAT.equals(dish.getType()))
				.limit(2)
				.collect(toList());
		
		System.out.println(twoMeatDish);
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
