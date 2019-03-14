package fr.noobeclair.hashcode.utils;

import java.util.concurrent.TimeUnit;

public class Utils {
	
	private Utils() {
		// useless utils constructor
	}
	
	public static double roundMiliTime(long ms, int n) {
		return Math.round(((ms)) * Math.pow(10, 3)) / Math.pow(10, 3 + n);
	}
	
	public static String formatToHHMMSS(Long sec) {
		long maxh = 100L * 3600L;
		long maxd = 24L * 3600L * 100L;
		if (sec < maxh) {
			return String.format("%02dh%02dm%02ds", TimeUnit.SECONDS.toHours(sec),
					TimeUnit.SECONDS.toMinutes(sec) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(sec)), 
					TimeUnit.SECONDS.toSeconds(sec) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(sec)));
		} else if (sec < maxd) {
			return String.format("%02dd%02dh%02dm", TimeUnit.SECONDS.toDays(sec),
					TimeUnit.SECONDS.toHours(sec) - TimeUnit.DAYS.toHours(TimeUnit.SECONDS.toDays(sec)), 
					TimeUnit.SECONDS.toMinutes(sec) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(sec)));
		} else {
			return ">100days";
		}
		
	}
	
}
