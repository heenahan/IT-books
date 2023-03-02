package head_first_design_patterns.strategy_pattern.fly;

public class FlyWithRocket implements FlyBehavior {
	
	@Override
	public void fly() {
		System.out.println("로켓의 추진력을 통해 날고 있습니다.");
	}
	
}
