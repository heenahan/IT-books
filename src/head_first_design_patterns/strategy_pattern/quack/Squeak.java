package head_first_design_patterns.strategy_pattern.quack;

public class Squeak implements QuackBehavior{
	
	@Override
	public void quack() {
		System.out.println("삑삑");
	}
	
}
