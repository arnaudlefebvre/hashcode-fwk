package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.constants.GlobalConstants;
import fr.noobeclair.hashcode.solve.Solver;
import fr.noobeclair.hashcode.utils.ProgressBar.Builder;
import fr.noobeclair.hashcode.utils.dto.SolverResultDto;
import fr.noobeclair.hashcode.utils.dto.WorkerResultDto;

public abstract class MultipleWorker<T extends BeanContainer, S extends Solver<T>> extends AbstractMultipleWorker<T> {

	protected static final Logger logger = LogManager.getLogger(MultipleWorker.class);
	protected List<Solver<T>> solvers;

	public MultipleWorker() {

	}

	@Override
	protected void prepare() {
		return;
	}

	@Override
	protected WorkerResultDto solve() {
		WorkerResultDto result = new WorkerResultDto();
		if (WORK_ORDER.SOLVER == this.execOrder) {
			result = runSolverFirst();
		} else {
			result = runFileFirst();
		}
		if (files.isEmpty()) {
			logger.error("No file - No run ... !");
		}
		if (solvers.isEmpty()) {
			logger.error("No solver - No run ... !");
		}
		return result;
	}

	protected WorkerResultDto runFileFirst() {
		final WorkerResultDto result = new WorkerResultDto();
		for (final InOut io : files) {
			for (final Solver<T> solver : this.solvers) {
				try {
					result.addResult(runSolverForFile(solver, io));
				} catch (final Exception e) {
					logger.error("Something went wrong running this worker : {}", solver.getName(), e);
					result.addResult(new SolverResultDto(BigDecimal.ZERO, solver.getName(), io.in, -1L, -1L, -1L));
				}
			}
		}
		return result;
	}

	protected WorkerResultDto runSolverFirst() {
		final WorkerResultDto result = new WorkerResultDto();
		for (final Solver<T> solver : this.solvers) {
			for (final InOut io : files) {
				try {
					result.addResult(runSolverForFile(solver, io));
				} catch (final Exception e) {
					logger.error("Something went wrong running this worker : {}", solver.getName(), e);
					result.addResult(new SolverResultDto(BigDecimal.ZERO, solver.getName(), io.in, -1L, -1L, -1L));
				}
			}
		}
		return result;
	}

	@Override
	protected Builder addbarOpt(Builder b) {
		return b.withMaxWidth(GlobalConstants.BAR_MAX_WIDTH).withOptions(GlobalConstants.BAR_OPTS)
				.withBarMsgSize(GlobalConstants.BAR_MSG_WIDTH).withRefreshTime(GlobalConstants.BAR_REFRESH_TIME);
	}

}
