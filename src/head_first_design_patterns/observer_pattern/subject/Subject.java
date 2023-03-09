package head_first_design_patterns.observer_pattern.subject;

import head_first_design_patterns.observer_pattern.observer.Observer;

public interface Subject {
	
	// 옵저버 등록
	void registerObserver(Observer o);
	// 옵저버 제거
	void removeObserver(Observer o);
	// 주제 상태 변경
	void notifyOberser();
}
