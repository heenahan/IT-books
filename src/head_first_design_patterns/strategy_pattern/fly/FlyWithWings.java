package head_first_design_patterns.strategy_pattern.fly;

public class FlyWithWings implements FlyBehavior{
	
	@Override
	public void fly() {
		System.out.println("날개짓을 통해 날고 있습니다.");
	}
	
}
