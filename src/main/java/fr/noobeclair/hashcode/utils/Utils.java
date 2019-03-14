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
			return String.format("%02dh%02dm%02ds", 
					TimeUnit.SECONDS.toHours(sec),
					TimeUnit.SECONDS.toMinutes(sec) -  
					TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(sec)), // The change is in this line
					TimeUnit.SECONDS.toSeconds(sec) - 
					TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(sec)));
		} else if (sec < maxd) {
			return String.format("%02dd%02dh%02dm", 
					TimeUnit.SECONDS.toDays(sec),
					TimeUnit.SECONDS.toHours(sec) -  
					TimeUnit.DAYS.toHours(TimeUnit.SECONDS.toDays(sec)), // The change is in this line
					TimeUnit.SECONDS.toMinutes(sec) - 
					TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(sec)));
		} else {
			return ">100days";
		}
		//si moins de 100h  	alors 00h00m00s = 100*3600
		//si moins de 100d		alors 00d00h00m = 3600 *24 * 100
		//si moins de 100y		alors 00y00d00h
		//si moins de 
//		return String.format("%02dh%02dm%02ds", TimeUnit.MILLISECONDS.toHours(millis),
//			    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
//			    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
		
		 
	}

}
