package head_first_design_patterns.observer_pattern.subject;

import head_first_design_patterns.observer_pattern.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class WeatherData implements Subject {
	
	private List<Observer> observers;
	private float temperature;
	private float humidity;
	private float pressure;
	
	public WeatherData() {
		observers = new ArrayList<>();
	}
	
	@Override
	public void registerObserver(Observer o) {
		observers.add(o);
	}
	
	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);
	}
	
	@Override
	public void notifyOberser() {
		for (Observer o : observers) {
			o.update(temperature, humidity, pressure);
		}
	}
	// 2. 구독하고 있는 옵저버에게 알림
	public void measurementsChanged() {
		notifyOberser();
	}
	
	// 1. 주제의 상태가 변경되면
	public void setMeasurement(float temperature, float humidity, float pressure) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		measurementsChanged();
	}
	
}
