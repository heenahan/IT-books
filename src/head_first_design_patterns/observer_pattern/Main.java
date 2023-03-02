package head_first_design_patterns.observer_pattern;

import observer_pattern.observer.CurrentConditionsDisplay;
import observer_pattern.observer.Observer;
import observer_pattern.observer.StaticsDisplay;
import observer_pattern.subject.WeatherData;

public class Main {
	
	public static void main(String[] args) {
		WeatherData weatherData = new WeatherData();
		
		Observer currentConditionsDisplay = new CurrentConditionsDisplay(weatherData);
		Observer staticsDisplay = new StaticsDisplay(weatherData);
		
		weatherData.setMeasurement(80, 65, 30.4f);
		weatherData.setMeasurement(82, 70, 29.2f);
		weatherData.setMeasurement(78, 90, 29.2f);
	}
	
}
