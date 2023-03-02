package head_first_design_patterns.strategy_pattern.quack;

public class MuteQuack implements QuackBehavior {
	
	@Override
	public void quack() {
		System.out.println("말을 할 수 없습니다!!");
	}
	
}
