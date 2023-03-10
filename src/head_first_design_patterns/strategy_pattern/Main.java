package head_first_design_patterns.strategy_pattern;

import head_first_design_patterns.strategy_pattern.duck.DecoyDuck;
import head_first_design_patterns.strategy_pattern.duck.Duck;
import head_first_design_patterns.strategy_pattern.fly.FlyNoWay;
import head_first_design_patterns.strategy_pattern.fly.FlyWithRocket;

public class Main {
	
	public static void main(String[] args) {
		Duck duck = new DecoyDuck();
		duck.setFlyBehavior(new FlyNoWay());
		duck.fly();
		duck.setFlyBehavior(new FlyWithRocket());
		duck.fly();
	}
	
}
