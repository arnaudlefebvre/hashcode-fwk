package fr.noobeclair.hashcode.bean.custom;

import java.util.List;

import fr.noobeclair.hashcode.bean.config.WorkerConfig;

public class CustomWorkerConfig extends WorkerConfig {

	public CustomWorkerConfig() {
		// TODO Auto-generated constructor stub
	}

	public CustomWorkerConfig(WorkerConfig c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	public CustomWorkerConfig(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath, FLUSH_CSV_STATS flushOpt,
			String csvSeparator, boolean progressBar) {
		super(statisticKeysToWriteToCSV, csvStatsPath, flushOpt, csvSeparator, progressBar);
		// TODO Auto-generated constructor stub
	}

	public CustomWorkerConfig(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath) {
		super(statisticKeysToWriteToCSV, csvStatsPath);
		// TODO Auto-generated constructor stub
	}

	public CustomWorkerConfig(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath, FLUSH_CSV_STATS flushOpt) {
		super(statisticKeysToWriteToCSV, csvStatsPath, flushOpt);
		// TODO Auto-generated constructor stub
	}

	public CustomWorkerConfig(List<Integer> statisticKeysToWriteToCSV, String csvStatsPath, FLUSH_CSV_STATS flushOpt,
			String csvSeparator) {
		super(statisticKeysToWriteToCSV, csvStatsPath, flushOpt, csvSeparator);
		// TODO Auto-generated constructor stub
	}

}
