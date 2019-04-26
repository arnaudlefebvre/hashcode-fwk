package fr.noobeclair.hashcode.worker;

import java.util.ArrayList;
import java.util.List;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.config.Config;
import fr.noobeclair.hashcode.bean.config.WorkerConfig;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.ProgressBar.Builder;

public class MultipleConfFileWorker<T extends BeanContainer, V extends Config, S extends ConfigSolver<T, V>, W extends WorkerConfig>
		extends MultipleConfWorker<T, V, S, W> {

	public MultipleConfFileWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer,
			final S solver, final List<InOut> files, V config, W wconfig) {
		super(writer, reader, scorer, new ArrayList<>(), config, wconfig);
		this.solvers.add(solver);
		this.files = files;
	}

	public MultipleConfFileWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer,
			final S solver, V config, W wconfig) {
		super(writer, reader, scorer, new ArrayList<>(), config, wconfig);
		this.solvers.add(solver);
		this.files = new ArrayList<>();
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
