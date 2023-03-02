package head_first_design_patterns.strategy_pattern.duck;

import strategy_pattern.fly.FlyBehavior;
import strategy_pattern.quack.QuackBehavior;

public class DecoyDuck extends Duck {
	
	@Override
	public void display() {
		System.out.println("나무로 만든 장식용 오리 입니다.");
	}
	
}
