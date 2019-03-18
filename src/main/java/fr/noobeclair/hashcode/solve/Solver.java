package fr.noobeclair.hashcode.solve;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class Solver<T extends BeanContainer> {

    protected static final Logger logger = LogManager.getLogger(Solver.class);
    private static final Long DEFAULT_TIMEOUT = 3600L;

    protected String name;
    protected T data;
    protected Long timeout;

    protected abstract T run(T data);

    /**
     * New Solver which never expires.
     *
     * @see #fr.noobeclair.hashcode.solve.Solver.Solver(Long) for tiemout
     */
    public Solver() {
        this.timeout = DEFAULT_TIMEOUT;
    }

    /**
     * New solver wich will expire after a timeout in SECONDS
     *
     * @param timeout in seconds.
     */
    public Solver(final Long timeout) {
        this.timeout = timeout;
    }

    public T solve(final T data) {
        final long start = System.currentTimeMillis();
        logger.debug("-- Solve start : {} - timeout {} sec ({})", this.getClass().getSimpleName(), timeout, Utils.formatToHHMMSS(timeout));        
        this.data = data;
        try {
            if (data != null && this.timeout == 0L) {
                return run(data);
            } else if (data != null) {
                return solveTimeout(data);
            } else {
            	return null;
            }
        } finally {
            logger.info("--Solve End ({}). Total Time : {}s --",this.getClass().getSimpleName(), Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
        }
    }

    private T solveTimeout(final T data) {
        final Callable<T> task = () -> {
            final String threadName = Thread.currentThread().getName();
            logger.info("Solve Thread {} started", threadName);
            return run(data);

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

    protected T getData() {
        return data;
    }

    protected void setData(final T data) {
        this.data = data;
    }

}
