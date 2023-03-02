package head_first_design_patterns.strategy_pattern.quack;

public class Quack implements QuackBehavior{
	
	@Override
	public void quack() {
		System.out.println("꽥꽥");
	}
	
}
