package fr.noobeclair.hashcode.solve;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class Solver<T extends BeanContainer, V extends Config> {
	
	protected static final Logger logger = LogManager.getLogger(Solver.class);
	public static final Long DEFAULT_TIMEOUT = 3600L;
	public static final Long DISABLE_TIMEOUT = 0L;
	private static final String BAR_MSG_SEP = "->";
	
	protected String name;
	protected T data;
	protected V config;
	protected Long timeout;
	protected Map<Integer, String> stats;
	protected Long barStart;
	
	/**
	 * Effectively run the computation and returns data into your <T extends
	 * BeanContainer>
	 * 
	 * @param data
	 *            datas
	 * @return data
	 */
	protected abstract T run(T data, ProgressBar bar);
	
	public T run(T data) {
		return run(data, null);
	}
	
	public void build(V config, Long timeout) {
		this.timeout = timeout;
		this.config = config;
		this.stats = new TreeMap<>();
	}
	
	/**
	 * New Solver which never expires.
	 *
	 * @see #fr.noobeclair.hashcode.solve.Solver.Solver(Long) for tiemout
	 */
	public Solver() {
		this.timeout = DEFAULT_TIMEOUT;
		this.stats = new TreeMap<>();
	}
	
	/**
	 * New solver wich will expire after a timeout in SECONDS
	 *
	 * @param timeout
	 *            in seconds.
	 */
	public Solver(final Long timeout) {
		this.timeout = timeout;
		this.stats = new TreeMap<>();
	}
	
	/**
	 * New solver wich will expire after a timeout in SECONDS
	 *
	 * @param timeout
	 *            in seconds.
	 * @param V
	 *            conf config
	 */
	public Solver(final V conf, final Long timeout) {
		this.timeout = timeout;
		this.config = conf;
		this.stats = new TreeMap<>();
	}
	
	public T solve(final T data, ProgressBar bar) {
		barStart = (bar != null) ? bar.getStep() : 0;
		final long start = System.currentTimeMillis();
		stats.put(StatsConstants.TIME_START, "" + start);
		logger.debug("-- Solve start : {} - timeout {} sec ({})", this.getClass().getSimpleName(), timeout, Utils.formatToHHMMSS(timeout));
		this.data = data;
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
			final long end = System.currentTimeMillis();
			final long tot = end - start;
			stats.put(StatsConstants.TIME_TOTAL, "" + tot);
			stats.put(StatsConstants.TIME_TOTAL, "" + end);
			logger.debug("--Solve End ({}). Total Time : {}s --", this.getClass().getSimpleName(), Utils.roundMiliTime(tot, 3));
		}
	}
	
	public T solve(final T data) {
		return solve(data, null);
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
			throw new RuntimeException("Solver abort");
		} finally {
			executor.shutdownNow();
		}
		return this.data;
	}
	
	private T solveAsync(final T data) {
		Set<Callable<T>> callables = new HashSet<Callable<T>>();
		final Callable<T> task = () -> {
			final String threadName = Thread.currentThread().getName();
			logger.info("Solve Thread {} started", threadName);
			return run(data);
			
		};
		callables.add(task);
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		List<Future<T>> futures = null;
		try {
			T res = executor.invokeAny(callables, timeout, TimeUnit.SECONDS);
			executor.awaitTermination(timeout, TimeUnit.SECONDS);
			this.data = res;
		} catch (InterruptedException | TimeoutException e) {
			logger.error(" <###----- !!!!!! -----#> Solve interrupted (Timeout)");
			return null;
		} catch (final Exception e) {
			logger.error(" <###----- !!!!!! -----#> Solve aborted due to error : ", e);
			throw new RuntimeException("Solver abort");
		} finally {
			executor.shutdownNow();
		}
		return this.data;
	}
	
	protected void showBar(ProgressBar bar, Long progress, String msg) {
		if (bar != null) {
			String m = StringUtils.isNotEmpty(bar.getMsg()) ? bar.getMsg().trim() : StringUtils.EMPTY;
			Integer pos = m.lastIndexOf(BAR_MSG_SEP);
			String fmsg = (pos != -1) ? (m.substring(0, pos) + BAR_MSG_SEP + msg).trim() : m + BAR_MSG_SEP + msg;
			bar.show(System.out, barStart + progress, fmsg);
		}
	}
	
	protected T getData() {
		return data;
	}
	
	protected void setData(final T data) {
		this.data = data;
	}
	
	public String getAdditionnalInfo() {
		return "";
	}
	
	public Map<Integer, String> getStats() {
		return stats;
	}
	
	public V getConfig() {
		return config;
	}
	
}
