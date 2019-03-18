package fr.noobeclair.hashcode.solve.step;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.Utils;

/**
 * AbstractClass of a solver Step. Remember that solver can decide to exit with
 * null after a timeout period. So if your step is very long use CODE BELOW in
 * your LOOPs if (Thread.currentThread().isInterrupted()) { return null; }
 *
 * More, you can use a {@link ProgressBar}
 *
 * @author alb
 *
 *
 *
 * @param <T> Solver Impl
 */
public abstract class AbstractStep<T extends BeanContainer> implements Step<T> {

    protected static final Logger logger = LogManager.getLogger(AbstractStep.class);
    protected String id;
    protected T solver;

    protected abstract T runStep(T solver);

    public AbstractStep(final String id, final T solver) {
        super();
        this.id = id;
        this.solver = solver;
    }

    @Override
    public T run(final T solver) {
        final long start = System.currentTimeMillis();
        logger.debug("-- Step {} start : {}", id);
        try {
            return runStep(solver);
        } catch (final Exception e) {
            logger.error(" <###----- !!!!!! -----#> Error in step {} : ", id, e);
            return null;
        } finally {
            logger.debug("-- Step {} End. Total Time : {}s --", id,
                            Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
        }
    }

    protected void halt(final Long milli) {
        if (milli > 0L) {
            try {
                Thread.sleep(milli);
            } catch (final InterruptedException e) {
                logger.error(" <###----- !!!!!! -----#> You loose waiting for nothing bro", e);
            }
        }
    }

}
