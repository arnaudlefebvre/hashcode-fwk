package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;

public abstract class MultipleWorker<T extends BeanContainer, V extends Config, S extends Solver<T, V>> {
	
	public enum WORK_ORDER {
		SOLVE_ALL_FILES, // Runs all files on a solver and move to next solver
		SOLVE_BY_FILE // Runs each solver on a file and move to next file
	}
	
	protected static final Logger logger = LogManager.getLogger(MultipleWorker.class);
	
	protected OutWriter<T> writer;
	protected InReader<T> reader;
	protected ScoreCalculator<T> scorer;
	
	public MultipleWorker() {
		
	}
	
	public abstract Map<String, BigDecimal> run();
}
