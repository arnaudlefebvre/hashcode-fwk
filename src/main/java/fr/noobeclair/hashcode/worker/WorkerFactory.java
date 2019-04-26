package fr.noobeclair.hashcode.worker;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Preconditions;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.config.Config;
import fr.noobeclair.hashcode.bean.config.WorkerConfig;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.worker.AbstractMultipleWorker.WORK_ORDER;

/***
 * 
 * 
 * @author arnaud
 *
 * @param <T>
 * @param <I>
 * @param <O>
 * @param <C>
 */
public class WorkerFactory<T extends BeanContainer, I extends InReader<T>, O extends OutWriter<T>, C extends Config, S extends ConfigSolver<T, C>, K extends ScoreCalculator<T>, W extends WorkerConfig> {

	public WorkerFactory() {
		super();
	}

	public Builder<T, I, O, C, S, K, W> builder() {
		return new Builder<T, I, O, C, S, K, W>(null);
	}

	public Builder<T, I, O, C, S, K, W> builder(Class<W> wcClass) {
		return new Builder<T, I, O, C, S, K, W>(wcClass);
	}

	public static final class Builder<T extends BeanContainer, I extends InReader<T>, O extends OutWriter<T>, C extends Config, S extends ConfigSolver<T, C>, K extends ScoreCalculator<T>, W extends WorkerConfig> {
		private T beanC;
		private I reader;
		private O writer;
		private C config;
		private K scorer;
		private W wconfig;
		private Class<W> wcClass;
		private List<InOut> files = new ArrayList<InOut>();
		private List<S> solvers = new ArrayList<>();
		private WORK_ORDER runOrder = WORK_ORDER.SOLVER;

		public Builder(Class<W> wcClass) {
			this.wcClass = wcClass;
		}

		public Builder<T, I, O, C, S, K, W> readWrite(I reader, O writer) {
			this.reader = reader;
			this.writer = writer;
			return this;

		}

		public Builder<T, I, O, C, S, K, W> score(K scorer) {
			this.scorer = scorer;
			return this;
		}

		public Builder<T, I, O, C, S, K, W> solverConfig(C config) {
			this.config = config;
			return this;
		}

		public Builder<T, I, O, C, S, K, W> csv(String path) {
			initWConfig();
			this.wconfig.setCsvStatsPath(path);
			return this;
		}

		public Builder<T, I, O, C, S, K, W> nocsv() {
			initWConfig();
			this.wconfig.setCsvStatsPath(null);
			this.wconfig.setStatisticKeysToWriteToCSV(null);
			return this;
		}

		public Builder<T, I, O, C, S, K, W> progressBar() {
			initWConfig();
			this.wconfig.setProgressBar(true);
			return this;
		}

		public Builder<T, I, O, C, S, K, W> config(W config) {
			this.wconfig = config;
			return this;
		}

		public Builder<T, I, O, C, S, K, W> csv(String path, WorkerConfig.FLUSH_CSV_STATS flush) {
			initWConfig();
			this.wconfig.setCsvStatsPath(path);
			this.wconfig.setFlushOpt(flush);
			return this;
		}

		public Builder<T, I, O, C, S, K, W> file(InOut io) {
			this.files.add(io);
			return this;
		}

		public Builder<T, I, O, C, S, K, W> files(List<InOut> files) {
			this.files.addAll(files);
			return this;
		}

		public Builder<T, I, O, C, S, K, W> solver(S solver) {
			this.solvers.add(solver);
			return this;
		}

		public Builder<T, I, O, C, S, K, W> solvers(List<S> solvers) {
			this.solvers.addAll(solvers);
			return this;
		}

		public Builder<T, I, O, C, S, K, W> filefirst() {
			this.runOrder = WORK_ORDER.FILE;
			return this;
		}

		public MultipleConfFileSolverWorker<T, C, S, W> Build() {
			MultipleConfFileSolverWorker<T, C, S, W> result = null;
			Preconditions.checkNotNull(this.reader, "Reader must be set. Builder.readwrite(...)");
			Preconditions.checkNotNull(this.writer, "Writer must be set. Builder.readwrite(...)");
			Preconditions.checkNotNull(this.config, "Config must be set. Builder.Config(...)");
			Preconditions.checkArgument(CollectionUtils.isNotEmpty(files),
					"Set at least one file to process. Builder.file(...)/Builder.files(...)");
			Preconditions.checkArgument(CollectionUtils.isNotEmpty(solvers),
					"Set at least one solver. Builder.solver(...)/Builder.solvers(...)");
			result = new MultipleConfFileSolverWorker<T, C, S, W>(files, reader, writer, scorer);
			result.setWorkerConfig(wconfig);
			result.setConfig(config);
			result.setSolvers(solvers);
			result.addFiles(this.files);
			result.setExecOrder(this.runOrder);
			return result;
		}

		private void initWConfig() {
			if (this.wconfig == null && wcClass != null) {
				try {
					this.wconfig = wcClass.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (this.wconfig == null) {
				throw new RuntimeException(
						"Builder must have WorkerConfig.class to use a WorkerConfiguration. Use WorkerFactory.buider(<MyClass extends WorkerConfig>");
			}
		}
	}

}
