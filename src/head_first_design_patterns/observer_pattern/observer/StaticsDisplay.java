package head_first_design_patterns.observer_pattern.observer;

import observer_pattern.display.Display;
import observer_pattern.subject.Subject;
import observer_pattern.subject.WeatherData;

public class StaticsDisplay implements Observer, Display {
	
	private int total = 0;
	private float min = Float.MAX_VALUE;
	private float max = Float.MIN_VALUE;
	private float sum = 0;
	private Subject weatherData;
	
	public StaticsDisplay(Subject weatherData) {
		this.weatherData = weatherData;
		weatherData.registerObserver(this);
	}
	
	@Override
	public void display() {
		System.out.println("평균/최고/최저 온도 = " + sum / total + "/" + max + "/" + min);
	}
	
	@Override
	public void update(float temperature, float humidity, float pressure) {
		if (min > temperature) min = temperature;
		if (temperature > max) max = temperature;
		total++;
		sum += temperature;
		display();
	}
}
