package fr.noobeclair.hashcode.bean;

import java.util.List;

public abstract class Config {
	
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
	 * @see fr.noobeclair.hashcode.bean.Config.FLUSH_CSV_STATS
	 */
	protected FLUSH_CSV_STATS flushOpt = FLUSH_CSV_STATS.EACH_RUN;
	
	protected String csvSeparator = ";";
	
	protected boolean progressBar = false;
	
	public Config() {
		super();
	}
	
	protected Config(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath) {
		super();
		this.statisticKeysToWriteToCSV = statisticKeysToWriteToCSV;
		this.csvStatsPath = csvStatsPath;
	}
	
	protected Config(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath, FLUSH_CSV_STATS flushOpt) {
		super();
		this.statisticKeysToWriteToCSV = statisticKeysToWriteToCSV;
		this.csvStatsPath = csvStatsPath;
		this.flushOpt = flushOpt;
	}
	
	protected Config(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath, FLUSH_CSV_STATS flushOpt, String csvSeparator) {
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
	
	public Config clone(Config c) throws CloneNotSupportedException {
		Config res = (Config) c.clone();
		res.statisticKeysToWriteToCSV = c.statisticKeysToWriteToCSV;
		res.csvStatsPath = c.csvStatsPath;
		res.flushOpt = c.flushOpt;
		return res;
	}
	
	public void cloneTo(Config c) {
		c.csvSeparator = this.csvSeparator;
		c.csvStatsPath = this.csvStatsPath;
		c.flushOpt = this.flushOpt;
		c.progressBar = this.progressBar;
		c.statisticKeysToWriteToCSV = this.statisticKeysToWriteToCSV;
	}
	
}
