package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.ProgressBar.Builder;
import fr.noobeclair.hashcode.utils.ProgressBar.ProgressBarOption;

public abstract class AbstractMultipleWorker<T extends BeanContainer> {
	
	protected Long total = 0L;
	protected Long approxEnd = 0L;
	protected ProgressBar bar;
	protected List<InOut> files;
	protected T data;
	protected OutWriter<T> writer;
	protected InReader<T> reader;
	protected ScoreCalculator<T> scorer;
	
	/**
	 * execOrder, 0 : apply each solver and move to next file 1 : run each file and
	 * move next solver
	 */
	protected Integer execOrder = 0;
	
	public enum WORK_ORDER {
		SOLVE_ALL_FILES, // Runs all files on a solver and move to next solver
		SOLVE_BY_FILE // Runs each solver on a file and move to next file
	}
	
	public AbstractMultipleWorker() {
		// TODO Auto-generated constructor stub
	}
	
	public Map<String, BigDecimal> run() {
		readAll();
		prepare();
		Map<String, BigDecimal> result = solve();
		return result;
	}
	
	protected void readAll() {
		for (InOut io : this.files) {
			this.reader.read(io.in);
		}
	}
	
	protected abstract void prepare();
	
	protected abstract Map<String, BigDecimal> solve();
	
	protected void barShow(String msg, boolean force) {
		if (bar != null) {
			bar.show(System.out, total, msg, force);
		}
	}
	
	protected void barShow(String msg) {
		if (bar != null) {
			bar.show(System.out, total, msg);
		}
	}
	
	protected void barEnd() {
		if (bar != null) {
			bar.end(System.out);
		}
	}
	
	protected Map<String, BigDecimal> runSolverForFile(final Solver<T> solver, final InOut io) {
		Map<String, BigDecimal> result = new TreeMap<>();
		T d = this.reader.read(io.in);
		d = solver.solve(d, bar);
		BigDecimal score = scorer.score(d);
		result.put(solver.getName() + ":" + solver.getAdditionnalInfo() + "--" + io.in, score);
		writer.write(d, getOut(solver, io));
		return result;
	}
	
	protected String getOut(final Solver<T> solver, final InOut io) {
		if (StringUtils.isNotEmpty(io.out)) {
			return io.out + "#" + solver.getName() + ".out";
		}
		return io.in + "#" + solver.getName() + ".out";
		
	}
	
	protected Builder addbarOpt(Builder b) {
		List<ProgressBarOption> opts = Arrays.asList(ProgressBarOption.MSG, ProgressBarOption.BAR, ProgressBarOption.PERCENT, ProgressBarOption.ETA);
		return b.withMaxWidth(100).withOptions(opts).withBarMsgSize(30).withBarMsgSize(40).withRefreshTime(10L);
		
	}
	
}
