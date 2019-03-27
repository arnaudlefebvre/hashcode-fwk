package fr.noobeclair.hashcode;

import java.util.Arrays;

import fr.noobeclair.hashcode.utils.ProgressBar.ProgressBarOption;

/**
 * @author arnaud
 *
 */
public class CustomConstants extends GlobalConstants {
	
	static {
		BAR_OPTS = Arrays.asList(ProgressBarOption.MSG, ProgressBarOption.BAR, ProgressBarOption.PERCENT, ProgressBarOption.ETA);
		BAR_MAX_WIDTH = 200;
		BAR_MSG_WIDTH = 50;
		BAR_REFRESH_TIME = 10L;
	}
	
	public CustomConstants() {
		// TODO Auto-generated constructor stub
	}
	
}
