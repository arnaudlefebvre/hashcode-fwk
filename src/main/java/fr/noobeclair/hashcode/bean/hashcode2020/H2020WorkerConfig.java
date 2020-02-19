package fr.noobeclair.hashcode.bean.hashcode2020;

import java.util.Arrays;

import fr.noobeclair.hashcode.bean.config.WorkerConfig;
import fr.noobeclair.hashcode.utils.ProgressBar.ProgressBarOption;

public class H2020WorkerConfig extends WorkerConfig {
	
	public H2020WorkerConfig() {
		super();
		this.statisticKeysToWriteToCSV = null;
		this.csvStatsPath = null;
		this.flushOpt = null;
		
		this.progressBar = true;
		this.barOpts = Arrays.asList(ProgressBarOption.MSG, ProgressBarOption.BAR, ProgressBarOption.PERCENT,
				ProgressBarOption.ETA);
		this.barMaxWidth = 120;
		this.barMsgWidth = 47;
		this.barRefreshTime = 1000L;
		
	}
}
