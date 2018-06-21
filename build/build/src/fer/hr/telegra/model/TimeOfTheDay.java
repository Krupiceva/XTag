package fer.hr.telegra.model;
/**
 * Enum class for time of the day in dataset
 * This is annotation of whole dataset
 * @author dmlinaric
 *
 */
public enum TimeOfTheDay {
	Unknown, Day, Night, Mix;
	
	public static String printTimeOfTheDay(TimeOfTheDay time) {
		String stringTime;
		if (time == null) {
			stringTime = "Unknown";
		}
		else {
			switch(time) {
			case Day:		stringTime = "Day";
							break;
			case Night:		stringTime = "Night";
							break;
			case Mix:		stringTime = "Mix";
							break;
			default:		stringTime = "Unknown";
							break;
			}
		}
		return stringTime;
	}
	
	
}
