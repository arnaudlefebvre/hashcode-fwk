package fr.noobeclair.hashcode.bean.hashcode2018;

import java.util.Arrays;
import java.util.List;

import fr.noobeclair.hashcode.bean.config.WorkerConfig;
import fr.noobeclair.hashcode.solve.StatsConstants;
import fr.noobeclair.hashcode.utils.ProgressBar.ProgressBarOption;

public class H2018WorkerConfig extends WorkerConfig {

	protected static final List<Integer> CSV_CST = Arrays.asList(StatsConstants.CF_STRAT, StatsConstants.SCORE,
			StatsConstants.TIME_TOTAL, StatsConstants.ITEM0_TOTAL, StatsConstants.IN_0, StatsConstants.IN_1,
			StatsConstants.CF_TTFC, StatsConstants.CF_NTFCT, StatsConstants.CF_NDFCT, StatsConstants.CF_LTFCT,
			StatsConstants.CF_LDFCT, StatsConstants.CF_NAT, StatsConstants.CF_NBT, StatsConstants.CF_NAD,
			StatsConstants.CF_NBD, StatsConstants.CF_LAT, StatsConstants.CF_LBT, StatsConstants.CF_LAD,
			StatsConstants.CF_LBD);
	protected static final String CSV_PATH = "src/main/resources/out/2018/global-stats.csv";

	public H2018WorkerConfig() {
		super();
		this.statisticKeysToWriteToCSV = CSV_CST;
		this.csvStatsPath = CSV_PATH;
		this.flushOpt = FLUSH_CSV_STATS.EACH_RUN;

		this.progressBar = true;
		this.barOpts = Arrays.asList(ProgressBarOption.MSG, ProgressBarOption.BAR, ProgressBarOption.PERCENT,
				ProgressBarOption.ETA);
		this.barMaxWidth = 120;
		this.barMsgWidth = 40;
		this.barRefreshTime = 100L;
	}

}
