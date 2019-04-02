/**
 * 
 */
package fr.noobeclair.hashcode;

import java.util.Arrays;
import java.util.List;

import fr.noobeclair.hashcode.utils.ProgressBar.ProgressBarOption;

/**
 * @author arnaud
 *
 */
public class GlobalConstants {
	
	/** Progress Bar Opts **/
	//public static final List<ProgressBarOption> BAR_OPTS = Arrays.asList(ProgressBarOption.ALL);
	public static List<ProgressBarOption> BAR_OPTS = Arrays.asList(ProgressBarOption.MSG, ProgressBarOption.BAR, ProgressBarOption.PERCENT, ProgressBarOption.ETA);
	public static Integer BAR_MAX_WIDTH = 100;
	public static Integer BAR_MSG_WIDTH = 37;
	public static Long BAR_REFRESH_TIME = 1000L;
	
	public GlobalConstants() {
		// TODO Auto-generated constructor stub
	}
	
}
