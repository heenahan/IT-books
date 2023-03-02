package modern_java_in_action.chap05;

public class Dish { // 불변형 클래스
	private final String name;
	private final boolean vegetarian;
	private final int calories;
	private final Type type;
	
	public Dish(String name, boolean vegetarian, int calories, Type type) {
		this.name = name;
		this.vegetarian = vegetarian;
		this.calories = calories;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isVegetarian() {
		return vegetarian;
	}
	
	public int getCalories() {
		return calories;
	}
	
	public Type getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "Dish{" +
				       "name='" + name + '\'' +
				       ", vegetarian=" + vegetarian +
				       ", calories=" + calories +
				       ", type=" + type +
				       '}';
	}
	
	public enum Type { MEAT, FISH, OTHER }
}
