package modern_java_in_action.chap05;

import java.util.Arrays;
import java.util.List;

import static modern_java_in_action.chap05.Dish.Type.*;

public class Reducing {
	
	public static void main(String[] args) {
		Reducing T = new Reducing();
		List<Dish> menu = T.getDishList();
		
		// map과 reduce를 사용하여 요리 개수 구하기
		int count = menu.stream().map(dish -> 1).reduce(0, (x, y) -> x + y);
		
		long count2 = menu.stream().count();
		
		System.out.println(count);
		System.out.println(count2);
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
