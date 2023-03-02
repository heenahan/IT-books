package head_first_design_patterns.strategy_pattern.fly;

public class FlyNoWay implements FlyBehavior{
	
	@Override
	public void fly() {
		System.out.println("날지 못합니다.");
	}
	
}
