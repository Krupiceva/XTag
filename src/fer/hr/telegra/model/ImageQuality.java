package fer.hr.telegra.model;

/**
 * Enum class for image quality
 * This is annotation of whole dataset
 * @author dmlinaric
 *
 */
public enum ImageQuality {
	Unknown, Blurry, Sharp, Mix;
	
	public static String printImageQuality(ImageQuality quality) {
		String stringQuality;
		if (quality == null) {
			stringQuality = "Unknown";
		}
		else {
			switch(quality) {
			case Blurry:	stringQuality = "Blurry";
							break;
			case Sharp:		stringQuality = "Sharp";
							break;
			case Mix:		stringQuality = "Mix";
							break;
			default:		stringQuality = "Unknown";
							break;
			}
		}
		return stringQuality;
	}
}
