package fer.hr.telegra.model;

public enum ImageQuality {
	Unknown, Blurry, Sharp;
	
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
			default:		stringQuality = "Unknown";
							break;
			}
		}
		return stringQuality;
	}
}
