package fr.noobeclair.hashcode.utils;

public class Utils {

	private Utils() {
		// useless utils constructor
	}
	
	public static double roundMiliTime(long ms, int n) {
		return Math.round(((ms)) * Math.pow(10, 3)) / Math.pow(10, 3 + n);
	}

}
