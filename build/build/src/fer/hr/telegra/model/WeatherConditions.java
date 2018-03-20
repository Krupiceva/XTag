package fer.hr.telegra.model;

public enum WeatherConditions {
	Unknown, Cloudy, Sunny, Rainy, Snowy, Mix;
	
	public static String printWeatherConditions(WeatherConditions condition) {
		String conditionString;
		if (condition == null) {
			conditionString = "Unknown";
		}
		else {
			switch(condition) {
			case Cloudy:	conditionString = "Cloudy";
							break;
			case Sunny:		conditionString = "Sunny";
							break;
			case Rainy:		conditionString = "Rainy";
							break;
			case Snowy:		conditionString = "Snowy";
							break;
			case Mix:		conditionString = "Mix";
							break;
			default:		conditionString = "Unknown";
							break;
			}
		}
		return conditionString;
	}
}
