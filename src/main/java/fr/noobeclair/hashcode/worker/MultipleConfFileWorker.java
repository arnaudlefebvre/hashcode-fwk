package fr.noobeclair.hashcode.worker;

import java.util.ArrayList;
import java.util.List;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.config.Config;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.ProgressBar.Builder;

public class MultipleConfFileWorker<T extends BeanContainer, V extends Config, S extends ConfigSolver<T, V>> extends MultipleConfWorker<T, V, S> {
	
	public MultipleConfFileWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final ConfigSolver<T, V> solver, final List<InOut> files, V config) {
		super();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.solvers = new ArrayList<>();
		this.solvers.add(solver);
		this.files = files;
		this.config = config;
	}
	
	public MultipleConfFileWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final ConfigSolver<T, V> solver, V config) {
		super();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.solvers = new ArrayList<>();
		this.solvers.add(solver);
		this.files = new ArrayList<>();
		this.config = config;
	}
	
	public void addFiles(final List<InOut> files) {
		this.files = files;
	}
	
	public void addFile(final String in, final String out) {
		this.files.add(new InOut(in, out));
	}
	
	@Override
	protected Builder buildBar() {
		return ProgressBar.builder(approxEnd);
	}
}
