package fr.noobeclair.hashcode;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.custom.CustomBeanContainer;
import fr.noobeclair.hashcode.bean.custom.CustomConfig;
import fr.noobeclair.hashcode.constants.custom.CustomConstants;
import fr.noobeclair.hashcode.in.custom.CustomReader;
import fr.noobeclair.hashcode.out.custom.CustomWriter;
import fr.noobeclair.hashcode.score.custom.CustomScoreCalculator;
import fr.noobeclair.hashcode.solve.custom.CustomConfigSolver;
import fr.noobeclair.hashcode.solve.custom.CustomConfigStepSolver;
import fr.noobeclair.hashcode.solve.custom.CustomSolver;
import fr.noobeclair.hashcode.utils.Utils;
import fr.noobeclair.hashcode.utils.dto.WorkerResultDto;
import fr.noobeclair.hashcode.worker.InOut;
import fr.noobeclair.hashcode.worker.MultipleConfFileSolverWorker;
import fr.noobeclair.hashcode.worker.SimpleConfWorker;
import fr.noobeclair.hashcode.worker.SimpleWorker;

public class CustomMain {
	private static final Logger logger = LogManager.getLogger(CustomMain.class);
	
	public static void main(final String[] args) {
		final long start = System.currentTimeMillis();
		logger.info("------------------------------------------------------------------------");
		logger.info("--                        Hashcode Noobeclair                          --");
		logger.info("------------------------------------------------------------------------");
		
		final Long timeout = TimeUnit.MINUTES.toSeconds(2);
		WorkerResultDto scores = null;
		try {
			
			//Path to in file - out file
			String in = "";
			String out = "";
			// InOut object
			InOut io = new InOut(in, out);
			
			//Create a reader
			CustomReader creader = new CustomReader();
			CustomWriter cwriter = new CustomWriter();
			//Create Config
			CustomConfig cfg = new CustomConfig();
			//Create ScoreCalculator
			CustomScoreCalculator cscorecalc = new CustomScoreCalculator();
			
			//Create solvers
			// 1 - Simple Solver 
			CustomSolver csolver = new CustomSolver("Simple Custom Solver", timeout);
			// 2 - Config Solver
			CustomConfigSolver cfgsolver = new CustomConfigSolver("Simple Custom Solver", cfg, timeout);
			// 3 - Stepped Config Solver
			CustomConfigStepSolver stepsolver = new CustomConfigStepSolver("Simple Custom Solver", cfg, timeout);
			
			//Create some workers
			// 1 Simple no Config : 1 file - 1 solver
			SimpleWorker<CustomBeanContainer> csw = new SimpleWorker<>(creader, csolver, cscorecalc, cwriter, io);
			// 2 Simple Config : 1 file - 1 solver
			SimpleConfWorker<CustomBeanContainer, CustomConfig> cfgsw = new SimpleConfWorker<>(creader, cfgsolver, cscorecalc, cwriter, io);
			// 3 Multiple config : n files, n workers
			MultipleConfFileSolverWorker<CustomBeanContainer, CustomConfig, CustomConfigSolver> mw = new MultipleConfFileSolverWorker<CustomBeanContainer, CustomConfig, CustomConfigSolver>(creader,
					cwriter, cscorecalc, 0, cfg);
			mw.addFiles(Arrays.asList(io));
			mw.addSolver(Arrays.asList(cfgsolver));
			
			//Run solvers and display results
			scores = csw.run();
			//Display results - all, then bests
			scores.displayAll();
			scores.displayBest();
			
			scores = cfgsw.run();
			//Display results - all, then bests
			scores.displayAll();
			scores.displayBest();
			
			//Additionnaly you can override GlobalConstants using CutomConstants
			mw.setGlobalConstantsClass(CustomConstants.class);
			scores = mw.run();
			//Display results - all, then bests
			scores.displayAll();
			scores.displayBest();
			
		} catch (Throwable e) {
			logger.error("Erreur : ", e);
		} finally {
			logger.info("------------------------------------------------------------------------");
			logger.info("-- End. Total Time : " + Utils.roundMiliTime((System.currentTimeMillis() - start), 3) + "s --");
			logger.info("------------------------------------------------------------------------");
		}
		return;
	}
	
}
