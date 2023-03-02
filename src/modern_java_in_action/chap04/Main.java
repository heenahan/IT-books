package modern_java_in_action.chap04;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static modern_java_in_action.chap04.Dish.Type.*;
import static java.util.stream.Collectors.*;

public class Main {
	
	public static void main(String[] args) {
		Main T = new Main();
		List<Dish> menu = T.getDishList();
		List<String> threeHighCaloricDishNames = menu.stream()
				                                         .filter(dish -> dish.getCalories() > 300) // 고칼로리 필터링
				                                         .map(Dish::getName)// 요리명 추출
				                                         .limit(3) // 선착순 세 개
				                                         .collect(toList()); // 다른 리스트 저장
		
		System.out.println(threeHighCaloricDishNames);
		
		List<String> highCaloricDishes = menu.stream()
				                                 .filter(dish -> {
					                                 // 스트림의 게으른 특성으로 처음 3개만 선택됨
					                                 System.out.println("filtering: " + dish.getName());
													 return dish.getCalories() > 300;
				                                 })
				                                 .map(dish -> {
					                                 System.out.println("mapping: " + dish.getName());
													 return dish.getName();
				                                 })
				                                 .limit(3)
				                                 .collect(toList());
		
		Stream<String> s = threeHighCaloricDishNames.stream();
		s.forEach(System.out::println);
		s.forEach(System.out::println); // 에러 발생
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
