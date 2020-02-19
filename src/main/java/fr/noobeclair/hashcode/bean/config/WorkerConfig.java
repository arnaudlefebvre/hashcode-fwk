package fr.noobeclair.hashcode.bean.config;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import fr.noobeclair.hashcode.utils.ProgressBar.ProgressBarOption;

public abstract class WorkerConfig {
	
	/**
	 * Enum for statistics flushing options :
	 * EACH_RUN : For each run of a solver on a file
	 * EACH_GROUP : For each goup of data. Only make sens with
	 * MultipleConfFileSolverWorker.
	 * If work order is file MultipleConfWorker.WORK_ORDER.SOLVE_ALL_FILES, then
	 * stats
	 * will be flushed before moving to next solver
	 * If work order is file MultipleConfWorker.WORK_ORDER.SOLVE_BY_FILE, then stats
	 * will be flushed before moving to next file
	 * END : at the end
	 * 
	 * @author arnaud
	 *
	 */
	public enum FLUSH_CSV_STATS {
		EACH_RUN, EACH_GROUP, END
	}
	
	public enum SHOW_OPT {
		SIMPLE, ALL, GEN
	}
	
	/**
	 * This must be overrided if you want to write stats for each solver runs into a
	 * csv file This attributes represents a list of keys that will be used to know
	 * which data needs to be write in csv output. Order is maintained in csv cols
	 * 
	 * @see fr.noobeclair.hashcode.bean.Config.csvStatsPath
	 */
	protected List<Integer> statisticKeysToWriteToCSV = null;
	
	/**
	 * This must be overrided if you want to write stats for each solver runs into a
	 * csv file
	 * 
	 * This is the path to output csv file.
	 */
	protected String csvStatsPath = null;
	
	/**
	 * May be overrided if you want to write stats into a csv file indicates when
	 * datas will be flushed to file by the worker.
	 * 
	 * @see fr.noobeclair.hashcode.bean.config.WorkerWorkerConfig.FLUSH_CSV_STATS
	 */
	protected FLUSH_CSV_STATS flushOpt = FLUSH_CSV_STATS.EACH_RUN;
	
	protected String csvSeparator = ";";
	
	/** Flag - enable or disable progressbar display **/
	protected boolean progressBar = false;
	
	/** Bar options **/
	protected List<ProgressBarOption> barOpts = Arrays.asList(ProgressBarOption.MSG, ProgressBarOption.BAR,
			ProgressBarOption.PERCENT, ProgressBarOption.ETA);
	protected Integer barMaxWidth = 100;
	protected Integer barMsgWidth = 37;
	protected Long barRefreshTime = 1000L;
	
	public WorkerConfig() {
		super();
	}
	
	public WorkerConfig(WorkerConfig c) {
		this.statisticKeysToWriteToCSV = c.statisticKeysToWriteToCSV;
		this.csvStatsPath = c.csvStatsPath;
		this.flushOpt = c.flushOpt;
		this.csvSeparator = c.csvSeparator;
		this.progressBar = c.progressBar;
	}
	
	public WorkerConfig(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath, FLUSH_CSV_STATS flushOpt,
			String csvSeparator, boolean progressBar) {
		super();
		this.statisticKeysToWriteToCSV = statisticKeysToWriteToCSV;
		this.csvStatsPath = csvStatsPath;
		this.flushOpt = flushOpt;
		this.csvSeparator = csvSeparator;
		this.progressBar = progressBar;
	}
	
	protected WorkerConfig(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath) {
		super();
		this.statisticKeysToWriteToCSV = statisticKeysToWriteToCSV;
		this.csvStatsPath = csvStatsPath;
	}
	
	protected WorkerConfig(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath, FLUSH_CSV_STATS flushOpt) {
		super();
		this.statisticKeysToWriteToCSV = statisticKeysToWriteToCSV;
		this.csvStatsPath = csvStatsPath;
		this.flushOpt = flushOpt;
	}
	
	protected WorkerConfig(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath, FLUSH_CSV_STATS flushOpt,
			String csvSeparator) {
		super();
		this.statisticKeysToWriteToCSV = statisticKeysToWriteToCSV;
		this.csvStatsPath = csvStatsPath;
		this.flushOpt = flushOpt;
		this.csvSeparator = csvSeparator;
	}
	
	public boolean withProgressBar() {
		return progressBar;
	}
	
	public List<Integer> getStatisticKeysToWriteToCSV() {
		return statisticKeysToWriteToCSV;
	}
	
	public void setStatisticKeysToWriteToCSV(List<Integer> statisticKeysToWriteToCSV) {
		this.statisticKeysToWriteToCSV = statisticKeysToWriteToCSV;
	}
	
	public String getCsvStatsPath() {
		return csvStatsPath;
	}
	
	public void setCsvStatsPath(String csvStatsPath) {
		this.csvStatsPath = csvStatsPath;
	}
	
	public FLUSH_CSV_STATS getFlushOpt() {
		return flushOpt;
	}
	
	public void setFlushOpt(FLUSH_CSV_STATS flushOpt) {
		this.flushOpt = flushOpt;
	}
	
	public String getCsvSeparator() {
		return csvSeparator;
	}
	
	public void setCsvSeparator(String csvSeparator) {
		this.csvSeparator = csvSeparator;
	}
	
	public void setProgressBar(boolean progressBar) {
		this.progressBar = progressBar;
	}
	
	public WorkerConfig clone(WorkerConfig c) throws CloneNotSupportedException {
		WorkerConfig res = (WorkerConfig) c.clone();
		res.statisticKeysToWriteToCSV = c.statisticKeysToWriteToCSV;
		res.csvStatsPath = c.csvStatsPath;
		res.flushOpt = c.flushOpt;
		return res;
	}
	
	public void cloneTo(WorkerConfig c) {
		c.csvSeparator = this.csvSeparator;
		c.csvStatsPath = this.csvStatsPath;
		c.flushOpt = this.flushOpt;
		c.progressBar = this.progressBar;
		c.statisticKeysToWriteToCSV = this.statisticKeysToWriteToCSV;
	}
	
	public String show() {
		return show(SHOW_OPT.SIMPLE);
	};
	
	public String show(SHOW_OPT opt) {
		switch (opt) {
		case SIMPLE:
			return showSimple();
		case ALL:
			return showAll();
		case GEN:
			return showGen();
		default:
			break;
		}
		return toString();
	};
	
	protected String showSimple() {
		return toString();
	};
	
	protected String showAll() {
		return ReflectionToStringBuilder.toString(this);
	};
	
	protected String showGen() {
		return ReflectionToStringBuilder.toStringExclude(this,
				Arrays.asList("statisticKeysToWriteToCSV", "csvStatsPath", "csvSeparator", "progressBar"));
	}
	
	public List<ProgressBarOption> getBarOpts() {
		return barOpts;
	}
	
	public Integer getBarMaxWidth() {
		return barMaxWidth;
	}
	
	public Integer getBarMsgWidth() {
		return barMsgWidth;
	}
	
	public Long getBarRefreshTime() {
		return barRefreshTime;
	}
	
}
