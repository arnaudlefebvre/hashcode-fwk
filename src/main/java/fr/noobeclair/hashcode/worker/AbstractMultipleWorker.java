package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.ProgressBar.Builder;
import fr.noobeclair.hashcode.utils.dto.SolverResultDto;
import fr.noobeclair.hashcode.utils.dto.WorkerResultDto;

public abstract class AbstractMultipleWorker<T extends BeanContainer> {
	
	protected static final Logger logger = LogManager.getLogger(AbstractMultipleWorker.class);
	
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
	protected WORK_ORDER execOrder = WORK_ORDER.SOLVER;
	
	public enum WORK_ORDER {
		SOLVER, // Runs all files on a solver and move to next solver
		FILE // Runs each solver on a file and move to next file
	}
	
	protected abstract void prepare();
	
	protected abstract WorkerResultDto solve();
	
	public AbstractMultipleWorker(List<InOut> files, InReader<T> reader, OutWriter<T> writer,
			ScoreCalculator<T> scorer) {
		super();
		this.files = files;
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
	}
	
	public AbstractMultipleWorker() {
		// TODO Auto-generated constructor stub
	}
	
	public WorkerResultDto run() {
		readAll();
		prepare();
		WorkerResultDto result = solve();
		return result;
	}
	
	protected void readAll() {
		for (InOut io : this.files) {
			this.reader.read(io.in);
		}
	}
	
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
	
	protected SolverResultDto runSolverForFile(final Solver<T> solver, final InOut io) {
		T d = this.reader.read(io.in);
		Long start = bar.getStep();
		d = solver.solve(d, bar);
		SolverResultDto score = solver.getResultInfo();
		if (scorer != null) {
			score = scorer.score(d, score);
		} else {
			score.setScore(BigDecimal.ZERO);
		}
		if (bar != null) {
			if (bar.getStep() == start) {
				bar.show(System.out, bar.getStep() + score.getNbInputItem());
			}
			this.total = start + score.getNbInputItem();
		}
		writer.write(d, getOut(solver, io));
		return score;
	}
	
	protected String getOut(final Solver<T> solver, final InOut io) {
		if (StringUtils.isNotEmpty(io.out)) {
			return io.out + "#" + solver.getName() + ".out";
		}
		return "src/main/resources/out/" + io.in.substring(io.in.lastIndexOf("/"), io.in.length()) + "#"
				+ solver.getName() + ".out";
		
	}
	
	protected abstract Builder addbarOpt(Builder b);
	
	public void setExecOrder(WORK_ORDER execOrder) {
		this.execOrder = execOrder;
	}
	
}
