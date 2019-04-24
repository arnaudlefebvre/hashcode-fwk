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

import fr.noobeclair.hashcode.annotation.CsvExport;
import fr.noobeclair.hashcode.annotation.CsvField;
import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.Utils;
import fr.noobeclair.hashcode.utils.dto.SolverResultDto;

/**
 * Abstract Solver
 * 
 * Compute a solution of a in datasets (encapsulated in a BeanContainer
 * 
 * This class provides a simple solving. Which means that solver can not be
 * configured with an ConfigInstance.
 * Thus all solver of this normally gives the same result for a same input
 * datasets.
 * 
 * a timeout handler is provided. Default is 1 hour
 * 
 * @author arnaud
 *
 * @param <T>
 */
@CsvExport
public abstract class Solver<T extends BeanContainer> {

	protected static final Logger logger = LogManager.getLogger(ConfigSolver.class);
	public static final Long DEFAULT_TIMEOUT = 3600L;
	public static final Long DISABLE_TIMEOUT = 0L;
	protected static final String BAR_MSG_SEP = "->";

	/** Name of ConfigSolver, must be unique **/
	@CsvField
	protected String name;
	/** data BeanContainer **/
	protected T data;
	/** Configured timeout for solving **/
	protected Long timeout;
	/** Statistical Map that can be filled by solver implementations **/
	protected Map<Integer, String> stats;
	/** Progress bar start progress before solving **/
	protected Long barStart;
	/** Container for solver stats and scoring **/
	protected SolverResultDto resultInfo;
	/** Number of item processed in BeanContainer (for stats) **/
	@CsvField
	protected Long nbItem;

	@CsvField
	protected Long totalItem;

	protected ProgressBar bar;

	/**
	 * Effectively run the computation and returns data into your <T extends
	 * BeanContainer>
	 * 
	 * @param data
	 *             datas
	 * @return data computed BeanContainer
	 */
	protected abstract T run(T data);

	/**
	 * Change Solver timeout and init stats
	 * 
	 * @param timeout
	 *                Long, timeout in seconds
	 */
	public void build(Long timeout) {
		this.timeout = timeout;
		this.stats = new TreeMap<>();
	}

	/**
	 * New ConfigSolver which expires at DEFAULT_TIMEOUT in seconds.
	 *
	 * @see #fr.noobeclair.hashcode.solve.ConfigSolver.Solver(Long) for tiemout
	 */
	public Solver() {
		this.timeout = DEFAULT_TIMEOUT;
		this.stats = new TreeMap<>();
		this.name = "ConfigSolver" + new Double(Math.random() * 1000).intValue();
	}

	/**
	 * 
	 * @param name
	 *             String solver name
	 */
	public Solver(String name) {
		this.timeout = DEFAULT_TIMEOUT;
		this.stats = new TreeMap<>();
		this.name = name;
	}

	/**
	 * New solver wich will expire after a timeout in SECONDS
	 * 
	 * @param name
	 *                String solver name
	 * @param timeout
	 *                in seconds.
	 * 
	 */
	public Solver(String name, final Long timeout) {
		this.timeout = timeout;
		this.stats = new TreeMap<>();
		this.name = name;
	}

	/**
	 * Solve with no timeout
	 * 
	 * @param data
	 * @return <T extends BeanContainer> Computed data
	 */
	public T solve(final T data) {
		return solve(data, null);
	}

	/**
	 * Solve
	 * Handles ProgressBar if provided, time logging,
	 * and Stats aggregation.
	 * 
	 * @param data
	 * @param bar
	 *             ProgressBar that can be updated by implemented solver and provides
	 *             feedback
	 * @return <T extends BeanContainer> Computed data
	 */
	public T solve(final T data, ProgressBar bar) {
		this.bar = bar;
		init(data);
		final long start = System.currentTimeMillis();
		stats.put(StatsConstants.TIME_START, "" + start);
		logger.debug("-- Solve start : {} - timeout {} sec ({})", this.getName(), timeout,
				Utils.formatToHHMMSS(timeout));
		try {
			if (data != null && this.timeout == DISABLE_TIMEOUT) {
				return run(data);
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

	/**
	 * Show ProgressBar if it exists
	 * 
	 * @param bar
	 *                 the ProgressBar
	 * @param progress
	 *                 current progress to display
	 * @param msg
	 *                 String msg to display
	 */
	protected void showBar(Long progress, String msg) {
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

	/**
	 * Init Solver before effective solving
	 * 
	 * @param data
	 * @param bar
	 */
	protected void init(final T data) {
		this.nbItem = 0L;

		this.resultInfo = new SolverResultDto();
		this.resultInfo.setInResource(data.getInName());
		this.resultInfo.setSolverName(this.getName());

		barStart = (bar != null) ? bar.getStep() : 0;
		this.data = data;
	}

	/**
	 * Close solver : Write duration, nbItems and time to results;
	 * 
	 * @param total
	 *              Long total solving time
	 */
	protected void close(Long total) {
		this.resultInfo.setDuration(total);
		this.resultInfo.setNbItemProcessed(getNbItems());
		this.resultInfo.setNbInputItem(totalItem != null ? totalItem : 0L);
		stats.put(StatsConstants.TIME_TOTAL, "" + total);
	}

	/**
	 * Runs solver in threaded Sync mode which enable a timeout termination
	 * 
	 * @param data
	 * @param bar
	 * @return
	 */
	private T solveSync(final T data, ProgressBar bar) {
		final Callable<T> task = () -> {
			final String threadName = Thread.currentThread().getName();
			logger.debug("Solve Thread {} started", threadName);
			return run(data);

		};
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		final Future<T> future = executor.submit(task);
		try {
			this.data = future.get(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException | TimeoutException e) {
			logger.error("Solve interrupted (Timeout)");
			future.cancel(true);
			return null;
		} catch (final Exception e) {
			logger.error("Solve aborted due to error : ", e);
			future.cancel(true);
			throw new RuntimeException("ConfigSolver abort");
		} catch (final Throwable e) {
			logger.error("Solve aborted due to error : ", e);
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
