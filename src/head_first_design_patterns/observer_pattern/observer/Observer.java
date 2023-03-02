package head_first_design_patterns.observer_pattern.observer;

public interface Observer {
	
	// 주제 상태 변경 시 호출
	void update(float temperature, float humidity, float pressure);
	
}
