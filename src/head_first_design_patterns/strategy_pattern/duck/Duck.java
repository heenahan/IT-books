package head_first_design_patterns.strategy_pattern.duck;

import head_first_design_patterns.strategy_pattern.fly.FlyBehavior;
import head_first_design_patterns.strategy_pattern.quack.QuackBehavior;

public abstract class Duck {
	
	FlyBehavior flyBehavior;
	QuackBehavior quackBehavior;
	
	public void display() {
		System.out.println("오리 입니다.");
	}
	
	public void fly() {
		flyBehavior.fly();
	}
	
	public void quack() {
		quackBehavior.quack();
	}
	
	public void setFlyBehavior(FlyBehavior flyBehavior) {
		this.flyBehavior = flyBehavior;
	}
	
	public QuackBehavior getQuackBehavior() {
		return quackBehavior;
	}
	
}
