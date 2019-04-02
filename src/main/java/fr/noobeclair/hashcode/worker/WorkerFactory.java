package fr.noobeclair.hashcode.worker;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Preconditions;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.worker.AbstractMultipleWorker.WORK_ORDER;

/***
 * WIP DOES NOT WORKS
 * 
 * @author arnaud
 *
 * @param <T>
 * @param <I>
 * @param <O>
 * @param <C>
 */
public class WorkerFactory<T extends BeanContainer, I extends InReader<T>, O extends OutWriter<T>, C extends Config, S extends ConfigSolver<T, C>, K extends ScoreCalculator<T>> {
	
	private Class<T> beanContainer;
	private Class<I> reader;
	private Class<O> writer;
	private Class<C> config;
	
	public WorkerFactory() {
		super();
	}
	
	public WorkerFactory(Class<T> beanContainer, Class<I> reader, Class<O> writer, Class<C> config) {
		super();
		this.beanContainer = beanContainer;
		this.reader = reader;
		this.writer = writer;
		this.config = config;
	}
	
	public Builder builder() {
		return new Builder<T, I, O, C, S, K>();
	}
	
	public static final class Builder<T extends BeanContainer, I extends InReader<T>, O extends OutWriter<T>, C extends Config, S extends ConfigSolver<T, C>, K extends ScoreCalculator<T>> {
		private Class<T> bc;
		private Class<I> i;
		private Class<O> o;
		private Class<C> c;
		private Class<S> S;
		private T beanC;
		private I reader;
		private O writer;
		private C config;
		private K scorer;
		private List<InOut> files = new ArrayList<InOut>();
		private List<S> solvers = new ArrayList<>();
		private WORK_ORDER runOrder;
		
		public Builder() {
			// TODO Auto-generated constructor stub
		}
		
		public Builder<T, I, O, C, S, K> readWrite(I reader, O writer) {
			this.reader = reader;
			this.writer = writer;
			return this;
			
		}
		
		public Builder<T, I, O, C, S, K> score(K scorer) {
			this.scorer = scorer;
			return this;
		}
		
		public Builder<T, I, O, C, S, K> config(C config) {
			this.config = config;
			return this;
		}
		
		public Builder<T, I, O, C, S, K> csv(String path) {
			this.config.setCsvStatsPath(path);
			return this;
		}
		
		public Builder<T, I, O, C, S, K> nocsv() {
			this.config.setCsvStatsPath(null);
			this.config.setStatisticKeysToWriteToCSV(null);
			return this;
		}
		
		public Builder<T, I, O, C, S, K> progressBar() {
			this.config.setProgressBar(true);
			return this;
		}
		
		public Builder<T, I, O, C, S, K> csv(String path, Config.FLUSH_CSV_STATS flush) {
			this.config.setCsvStatsPath(path);
			this.config.setFlushOpt(flush);
			return this;
		}
		
		public Builder<T, I, O, C, S, K> file(InOut io) {
			this.files.add(io);
			return this;
		}
		
		public Builder<T, I, O, C, S, K> files(List<InOut> files) {
			this.files.addAll(files);
			return this;
		}
		
		public Builder<T, I, O, C, S, K> solver(S solver) {
			this.solvers.add(solver);
			return this;
		}
		
		public Builder<T, I, O, C, S, K> solvers(List<S> solvers) {
			this.solvers.addAll(solvers);
			return this;
		}
		
		public Builder<T, I, O, C, S, K> filefirst() {
			this.runOrder = WORK_ORDER.FILE;
			return this;
		}
		
		public MultipleConfFileSolverWorker<T, C, S> Build() {
			MultipleConfFileSolverWorker<T, C, S> result = null;
			Preconditions.checkNotNull(this.reader, "Reader must be set. Builder.readwrite(...)");
			Preconditions.checkNotNull(this.writer, "Writer must be set. Builder.readwrite(...)");
			Preconditions.checkNotNull(this.config, "Config must be set. Builder.Config(...)");
			Preconditions.checkArgument(CollectionUtils.isNotEmpty(files), "Set at least one file to process. Builder.file(...)/Builder.files(...)");
			Preconditions.checkArgument(CollectionUtils.isNotEmpty(solvers), "Set at least one solver. Builder.solver(...)/Builder.solvers(...)");
			result = new MultipleConfFileSolverWorker(reader, writer, scorer, solvers, config);
			result.addFiles(this.files);
			return result;
		}
	}
	
}
