package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.utils.ProgressBar;

public class SimpleProgressWorker<T extends BeanContainer, V extends Config> extends SimpleConfWorker<T, V> {
	
	ProgressBar bar;
	
	public SimpleProgressWorker(InReader<T> reader, ConfigSolver<T, V> solver, OutWriter<T> writer, InOut inOut, ProgressBar bar) {
		super(reader, solver, writer, inOut);
		this.bar = bar;
	}
	
	public SimpleProgressWorker(InReader<T> reader, ConfigSolver<T, V> solver, OutWriter<T> writer, String in, String out, ProgressBar bar) {
		super(reader, solver, writer, in, out);
		this.bar = bar;
	}
	
	public SimpleProgressWorker(InReader<T> reader, ConfigSolver<T, V> solver, ScoreCalculator<T> scorer, OutWriter<T> writer, InOut inOut, ProgressBar bar) {
		super(reader, solver, scorer, writer, inOut);
		this.bar = bar;
	}
	
	public SimpleProgressWorker(InReader<T> reader, ConfigSolver<T, V> solver, ScoreCalculator<T> scorer, OutWriter<T> writer, String in, String out, ProgressBar bar) {
		super(reader, solver, scorer, writer, in, out);
		this.bar = bar;
	}
	
	@Override
	public BigDecimal run() {
		if (scorer == null) {
			this.writer.write(this.solver.solve(this.reader.read(inOut.in), bar), getOut(this.solver));
			return BigDecimal.ZERO;
		} else {
			return this.scorer.score(this.writer.write(this.solver.solve(this.reader.read(inOut.in), bar), getOut(this.solver)));
		}
	}
	
}
