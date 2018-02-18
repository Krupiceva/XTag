package fer.hr.telegra.model;

public enum WeatherConditions {
	Unknown, Cloudy, Sunny, Rain;
	
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
			case Rain:		conditionString = "Rain";
							break;
			default:		conditionString = "Unknown";
							break;
			}
		}
		return conditionString;
	}
}
