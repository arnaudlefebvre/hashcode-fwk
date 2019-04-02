/**
 * 
 */
package fr.noobeclair.hashcode.bean;

import java.util.List;

/**
 * @author arnaud
 *
 */
public class CustomConfig extends Config {
	
	/**
	 * 
	 */
	public CustomConfig() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param statisticKeysToWriteToCSV
	 * @param csvStatsPath
	 */
	public CustomConfig(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath) {
		super(statisticKeysToWriteToCSV, csvStatsPath);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param statisticKeysToWriteToCSV
	 * @param csvStatsPath
	 * @param flushOpt
	 */
	public CustomConfig(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath, FLUSH_CSV_STATS flushOpt) {
		super(statisticKeysToWriteToCSV, csvStatsPath, flushOpt);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param statisticKeysToWriteToCSV
	 * @param csvStatsPath
	 * @param flushOpt
	 * @param csvSeparator
	 */
	public CustomConfig(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath, FLUSH_CSV_STATS flushOpt, String csvSeparator) {
		super(statisticKeysToWriteToCSV, csvStatsPath, flushOpt, csvSeparator);
		// TODO Auto-generated constructor stub
	}
	
}
