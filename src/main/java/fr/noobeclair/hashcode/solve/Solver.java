package fr.noobeclair.hashcode.solve;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.Utils;
import fr.noobeclair.hashcode.utils.dto.SolverResultDto;

public abstract class Solver<T extends BeanContainer> {
	
	protected static final Logger logger = LogManager.getLogger(ConfigSolver.class);
	public static final Long DEFAULT_TIMEOUT = 3600L;
	public static final Long DISABLE_TIMEOUT = 0L;
	protected static final String BAR_MSG_SEP = "->";
	
	/** Name of ConfigSolver, must be unique **/
	protected String name;
	protected T data;
	protected Long timeout;
	protected Map<Integer, String> stats;
	protected Long barStart;
	protected SolverResultDto resultInfo;
	protected Long nbItem;
	
	/**
	 * Effectively run the computation and returns data into your <T extends
	 * BeanContainer>
	 * 
	 * @param data
	 *            datas
	 * @return data
	 */
	protected abstract T run(T data, ProgressBar bar);
	
	public void build(Long timeout) {
		this.timeout = timeout;
		this.stats = new TreeMap<>();
	}
	
	/**
	 * New ConfigSolver which never expires.
	 *
	 * @see #fr.noobeclair.hashcode.solve.ConfigSolver.Solver(Long) for tiemout
	 */
	public Solver() {
		this.timeout = DEFAULT_TIMEOUT;
		this.stats = new TreeMap<>();
		this.name = "ConfigSolver" + new Double(Math.random() * 1000).intValue();
	}
	
	public Solver(String name) {
		this.timeout = DEFAULT_TIMEOUT;
		this.stats = new TreeMap<>();
		this.name = name;
	}
	
	/**
	 * New solver wich will expire after a timeout in SECONDS
	 *
	 * @param timeout
	 *            in seconds.
	 */
	public Solver(String name, final Long timeout) {
		this.timeout = timeout;
		this.stats = new TreeMap<>();
		this.name = name;
	}
	
	public T solve(final T data) {
		return solve(data, null);
	}
	
	public T solve(final T data, ProgressBar bar) {
		init(data, bar);
		final long start = System.currentTimeMillis();
		stats.put(StatsConstants.TIME_START, "" + start);
		logger.debug("-- Solve start : {} - timeout {} sec ({})", this.getName(), timeout, Utils.formatToHHMMSS(timeout));
		try {
			if (data != null && this.timeout == DISABLE_TIMEOUT) {
				return run(data, bar);
			} else if (data != null) {
				return solveSync(data, bar);
			} else {
				return null;
			}
		} catch (Throwable e) {
			logger.error("ERROR in solver", e);
			return null;
		} finally {
			final long tot = System.currentTimeMillis() - start;
			close(tot);
			logger.debug("--Solve End ({}). Total Time : {}s --", this.getName(), Utils.roundMiliTime(tot, 3));
		}
	}
	
	protected T run(T data) {
		return run(data, null);
	}
	
	protected void showBar(ProgressBar bar, Long progress, String msg) {
		if (bar != null) {
			String m = StringUtils.isNotEmpty(bar.getMsg()) ? bar.getMsg().trim() : StringUtils.EMPTY;
			Integer pos = m.lastIndexOf(BAR_MSG_SEP);
			String fmsg = "";
			if (pos == -1) {
				fmsg = m + BAR_MSG_SEP + msg;
			} else if (pos == 0) {
				fmsg = BAR_MSG_SEP + msg;
			} else {
				fmsg = (m.substring(0, pos) + BAR_MSG_SEP + msg).trim();
			}
			
			bar.show(System.out, barStart + progress, fmsg);
		}
	}
	
	private void init(final T data, ProgressBar bar) {
		this.nbItem = 0L;
		
		this.resultInfo = new SolverResultDto();
		this.resultInfo.setInResource(data.getInName());
		this.resultInfo.setSolverName(this.getName());
		
		barStart = (bar != null) ? bar.getStep() : 0;
		this.data = data;
	}
	
	private void close(Long total) {
		this.resultInfo.setDuration(total);
		this.resultInfo.setNbItemProcessed(getNbItems());
		stats.put(StatsConstants.TIME_TOTAL, "" + total);
	}
	
	private T solveSync(final T data, ProgressBar bar) {
		final Callable<T> task = () -> {
			final String threadName = Thread.currentThread().getName();
			logger.debug("Solve Thread {} started", threadName);
			return run(data, bar);
			
		};
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		final Future<T> future = executor.submit(task);
		try {
			this.data = future.get(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException | TimeoutException e) {
			logger.error(" <###----- !!!!!! -----#> Solve interrupted (Timeout)");
			future.cancel(true);
			return null;
		} catch (final Exception e) {
			logger.error(" <###----- !!!!!! -----#> Solve aborted due to error : ", e);
			future.cancel(true);
			throw new RuntimeException("ConfigSolver abort");
		} finally {
			executor.shutdownNow();
		}
		return this.data;
	}
	
	/**
	 * Must returns the numbers of items to process, (not these that actually has
	 * been processed)
	 * 
	 * @return numbers of items to process at start of solve
	 */
	protected Long getNbItems() {
		return this.nbItem;
	}
	
	protected void nbItmPlus() {
		this.nbItem = this.nbItem + 1;
	}
	
	protected void nbItmPlus(Long nb) {
		this.nbItem = this.nbItem + nb;
	}
	
	protected T getData() {
		return data;
	}
	
	protected void setData(final T data) {
		this.data = data;
	}
	
	public Map<Integer, String> getStats() {
		return stats;
	}
	
	public SolverResultDto getResultInfo() {
		return resultInfo;
	}
	
	public String getName() {
		return name;
	}
	
}
