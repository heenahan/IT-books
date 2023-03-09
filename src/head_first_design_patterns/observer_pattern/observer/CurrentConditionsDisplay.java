package head_first_design_patterns.observer_pattern.observer;

import head_first_design_patterns.observer_pattern.display.Display;
import head_first_design_patterns.observer_pattern.subject.Subject;
import head_first_design_patterns.observer_pattern.subject.WeatherData;

public class CurrentConditionsDisplay implements Observer, Display {
	
	private Subject weatherData;
	private float temperature;
	private float humidity;
	
	public CurrentConditionsDisplay(Subject weatherData) {
		this.weatherData = weatherData;
		weatherData.registerObserver(this); // 구독하는 주제
	}
	
	@Override
	public void display() {
		System.out.println("현재 상태 : 온도 " + temperature + "℃, 습도 " + humidity + "%");
	}
	
	@Override
	public void update(float temperature, float humidity, float pressure) {
		this.temperature = temperature;
		this.humidity = humidity;
		display();
	}
	
}
