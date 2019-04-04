package fr.noobeclair.hashcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.config.ConfigStratFactory;
import fr.noobeclair.hashcode.bean.custom.CustomBeanContainer;
import fr.noobeclair.hashcode.bean.custom.CustomConfig;
import fr.noobeclair.hashcode.bean.hashcode2018.H18LegConf;
import fr.noobeclair.hashcode.bean.hashcode2018.H2018BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2018.H2018Config;
import fr.noobeclair.hashcode.bean.hashcode2019.H2019Config;
import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.in.custom.CustomReader;
import fr.noobeclair.hashcode.in.hashcode2018.H2018Reader;
import fr.noobeclair.hashcode.in.hashcode2019.Hashcode2019Reader;
import fr.noobeclair.hashcode.out.NullWriter;
import fr.noobeclair.hashcode.out.custom.CustomWriter;
import fr.noobeclair.hashcode.out.hashcode2018.H2018Writer;
import fr.noobeclair.hashcode.out.hashcode2019.Hashcode2019Writer;
import fr.noobeclair.hashcode.score.custom.CustomScoreCalculator;
import fr.noobeclair.hashcode.score.hashcode2018.H2018ScoreCalculator;
import fr.noobeclair.hashcode.score.hashcode2019.Hashcode2019ScoreCalculator;
import fr.noobeclair.hashcode.solve.SolverFactory;
import fr.noobeclair.hashcode.solve.custom.CustomConfigSolver;
import fr.noobeclair.hashcode.solve.custom.CustomConfigStepSolver;
import fr.noobeclair.hashcode.solve.custom.CustomSolver;
import fr.noobeclair.hashcode.solve.hashcode2018.H2018Solver;
import fr.noobeclair.hashcode.solve.hashcode2018.H2018SolverLegConf;
import fr.noobeclair.hashcode.solve.hashcode2019.HashCode2019DummyStepSolver;
import fr.noobeclair.hashcode.solve.hashcode2019.HashCode2019StepSolver;
import fr.noobeclair.hashcode.utils.Utils;
import fr.noobeclair.hashcode.utils.dto.WorkerResultDto;
import fr.noobeclair.hashcode.worker.InOut;
import fr.noobeclair.hashcode.worker.MultipleConfFileSolverWorker;
import fr.noobeclair.hashcode.worker.MultipleConfFileWorker;
import fr.noobeclair.hashcode.worker.MultipleConfSolverWorker;
import fr.noobeclair.hashcode.worker.SimpleConfWorker;
import fr.noobeclair.hashcode.worker.SimpleWorker;
import fr.noobeclair.hashcode.worker.WorkerFactory;

public class MainRunner {
	public static final String CR = "\r";
	private static final Logger logger = LogManager.getLogger(MainRunner.class);
	// 1 - create � worker.
	// 2 - workers use � reader to read input
	// 3 - worker use � solver which provide � solution
	// 4 - worker use � writer to write out
	// 5 - Eventually provide stats informations
	
	// TODO - Ajouter un test unitaire pour la distance ? si ce calcul est faux et
	// utilis�, c'est chaud...
	// TODO - Voir pour creer une tache maven Zipator (ie : prends les sources, les
	// zip et ajoute les fichiers de r�sultats dans un dossier sp�cifique)
	// Comment on peut automatiser l'upload
	public static void main(final String[] args) {
		final long start = System.currentTimeMillis();
		logger.info("------------------------------------------------------------------------");
		logger.info("--                        Hashcode Noobeclair                          --");
		logger.info("------------------------------------------------------------------------");
		
		final Long timeout = TimeUnit.MINUTES.toSeconds(2);
		final Long timeout2019 = TimeUnit.SECONDS.toSeconds(5);
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
			
			//csw.run();
			//cfgsw.run();
			//mw.run();
			
			// Simple worker - 1 file in/out - 1 Algorithm - Optionnal : scorer
			final Hashcode2019Reader reader = new Hashcode2019Reader();
			final H2019Config cfg2019 = new H2019Config();
			final HashCode2019StepSolver solver = new HashCode2019StepSolver("StepSolver2019", timeout2019, cfg2019);
			final HashCode2019DummyStepSolver dummySolver = new HashCode2019DummyStepSolver("Dummy2019", timeout, cfg2019);
			final Hashcode2019Writer writer = new Hashcode2019Writer();
			final Hashcode2019ScoreCalculator scorer = new Hashcode2019ScoreCalculator();
			final SimpleConfWorker<HashCode2019BeanContainer, H2019Config> sw = new SimpleConfWorker<>(reader, dummySolver, scorer, writer,
					new InOut("src/main/resources/in/b_lovely_landscapes.txt", "src/main/resources/out/b_example.out.txt"));
			// logger.info("Dummy solver on bigfile b {}", sw.run());
			
			// Mutiple File Worker - n files in/out - 1 algorithm
			final MultipleConfFileWorker<HashCode2019BeanContainer, H2019Config, HashCode2019StepSolver> mfw = new MultipleConfFileWorker<>(reader, writer, scorer, solver, cfg2019);
			final List<InOut> files = new ArrayList<>();
			files.add(new InOut("src/main/resources/in/a_example.txt",
					"src/main/resources/out/a_example.out.txt"));
			files.add(new InOut("src/main/resources/in/b_lovely_landscapes.txt",
					"src/main/resources/out/b_example.out.txt"));
			files.add(new InOut("src/main/resources/in/c_memorable_moments.txt",
					"src/main/resources/out/c_example.out.txt"));
			mfw.addFiles(files);
			scores = null;
			// scores = mfw.run();
			//			if (MapUtils.isNotEmpty(scores)) {
			//				MapUtils.verbosePrint(System.out, mfw.getClass() + "-" + solver.getClass() +
			//						" : Score de chaque fichier ", scores);
			//			}
			
			// Multiple ConfigSolver Worker - 1 files int/out - n algorithm
			final MultipleConfSolverWorker<HashCode2019BeanContainer, H2019Config, HashCode2019StepSolver> msw = new MultipleConfSolverWorker<>(reader, writer, scorer,
					new InOut("src/main/resources/in/a_example.txt",
							"src/main/resources/out/a_example.out.txt"),
					cfg2019);
			msw.addSolver(solver);
			msw.addSolver(dummySolver);
			// scores = msw.run();
			//			if (MapUtils.isNotEmpty(scores)) {
			//				MapUtils.verbosePrint(System.out, msw.getClass().getSimpleName() + " : Score du fichier " + msw.getInOut().in, scores);
			//			}
			
			final MultipleConfFileSolverWorker<HashCode2019BeanContainer, H2019Config, HashCode2019StepSolver> mfsw = new MultipleConfFileSolverWorker<>(reader, writer, scorer,
					Arrays.asList(dummySolver, solver), cfg2019);
			mfsw.addFiles(files);
			// scores = mfsw.run();
			//			if (MapUtils.isNotEmpty(scores)) {
			//				MapUtils.verbosePrint(System.out, msw.getClass().getSimpleName() + " : Score des fichiers pour chaque solver ", scores);
			//			}
			
			H2018Reader read2018 = new H2018Reader();
			H2018ScoreCalculator scor2018 = new H2018ScoreCalculator();
			H2018Writer write2018 = new H2018Writer();
			
			H2018Config workerCfg = new H2018Config(0.0, 0.0, 0.0);
			//			ConfigFactory<H2018Config> config2018factory = new ConfigFactory<>(H2018Config.class);
			ConfigStratFactory<H2018Config> config2018factory = new ConfigStratFactory<>(H2018Config.class);
			List<H2018Config> configs2018 = config2018factory.generate(workerCfg);
			SolverFactory<H2018Solver, H2018BeanContainer, H2018Config> sfactory = new SolverFactory<>(H2018Solver.class, H2018BeanContainer.class, H2018Config.class);
			WorkerFactory<H2018BeanContainer, H2018Reader, H2018Writer, H2018Config, H2018Solver, H2018ScoreCalculator> sw0 = new WorkerFactory<H2018BeanContainer, H2018Reader, H2018Writer, H2018Config, H2018Solver, H2018ScoreCalculator>();
			MultipleConfFileSolverWorker<H2018BeanContainer, H2018Config, H2018Solver> mfsw2018 = sw0.builder()
					.readWrite(read2018, new NullWriter())
					.score(scor2018)
					.config(workerCfg).solvers(sfactory.createFromConfs(configs2018, timeout))
					.progressBar()
					.file(new InOut("src/main/resources/in/2018/a_example.in", null))
					.file(new InOut("src/main/resources/in/2018/b_should_be_easy.in", null))
					.file(new InOut("src/main/resources/in/2018/c_no_hurry.in", null))
					.file(new InOut("src/main/resources/in/2018/d_metropolis.in", null))
					.file(new InOut("src/main/resources/in/2018/e_high_bonus.in", null))
					.Build();
			scores = mfsw2018.run();
			
			scores.displayAll();
			scores.displayBest();
			
			/************************************************************************************************************************/
			/*
			 * OLD STYLE
			 * 
			 */
			
			List<H18LegConf.CarStrategy> carSt1 = Arrays.asList();
			List<H18LegConf.CarStrategy> carSt2 = Arrays.asList(H18LegConf.CarStrategy.NEAR_FIRST);
			List<H18LegConf.CarStrategy> carSt3 = Arrays.asList(H18LegConf.CarStrategy.NEAR_FIRST, H18LegConf.CarStrategy.QUICK_FIRST);
			List<H18LegConf.CarStrategy> carSt4 = Arrays.asList(H18LegConf.CarStrategy.LONG_FIRST);
			List<H18LegConf.CarStrategy> carSt5 = Arrays.asList(H18LegConf.CarStrategy.LONG_FIRST, H18LegConf.CarStrategy.QUICK_FIRST);
			List<H18LegConf.CarStrategy> carStAgr = Arrays.asList(H18LegConf.CarStrategy.AGGRESSIVE);
			
			List<H18LegConf> configs = new ArrayList<>();
			// EXP
			
			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV, H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt2));
			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV, H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt3));
			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV, H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt4));
			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV, H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt5));
			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV, H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5, 0.0,
					1.5, 0.0, carStAgr));
			//			
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV,
			//					H18LegConf.AdjustMethod.LINEAR, 2.0, 0.0, 3.0, 0.0, 1.5,
			//					0.0, 1.5, 0.0, carSt2));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV,
			//					H18LegConf.AdjustMethod.LINEAR, 2.0, 0.0, 3.0, 0.0, 1.5,
			//					0.0, 1.5, 0.0, carSt3));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV,
			//					H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5,
			//					0.0, 1.5, 0.0, carSt4));
			//			
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.INV,
			//					H18LegConf.AdjustMethod.LINEAR, 2.0, 0.0, 3.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carSt2));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.INV,
			//					H18LegConf.AdjustMethod.LINEAR, 2.0, 0.0, 3.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carSt3));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.INV,
			//					H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carSt4));
			//			
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.INV,
			//					H18LegConf.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carSt4));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.INV,
			//					H18LegConf.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carSt5));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.INV,
			//					H18LegConf.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carStAgr));
			//			
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carSt4));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carSt5));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carStAgr));
			//			
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.INV,
			//					H18LegConf.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carSt4));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.INV,
			//					H18LegConf.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carSt5));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.LINEAR,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.INV,
			//					H18LegConf.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carStAgr));
			//			
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.EXP,
			//					H18LegConf.AdjustMethod.EXP, 2.0, 0.0, 3.0, 0.0, 1.5,
			//					0.0, 1.5, 0.0, carSt2));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.EXP,
			//					H18LegConf.AdjustMethod.EXP, 2.0, 0.0, 3.0, 0.0, 1.5,
			//					0.0, 1.5, 0.0, carSt3));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.EXP,
			//					H18LegConf.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carSt4));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.EXP,
			//					H18LegConf.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carSt5));
			//			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP,
			//					H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.EXP,
			//					H18LegConf.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
			//					1.5, 0.0, carStAgr));
			//			
			//			// Winners Custom Try
			configs.add(new H18LegConf(3.0, H18LegConf.AdjustMethod.LINEAR,
					H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV,
					H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5,
					0.0, 1.5, 0.0, carSt4));
			configs.add(new H18LegConf(1.0, H18LegConf.AdjustMethod.LINEAR,
					H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV,
					H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.3,
					0.0, 1.5, 0.0, carSt4));
			configs.add(new H18LegConf(1.0, H18LegConf.AdjustMethod.LINEAR,
					H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV,
					H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 1.0, 0.0, 1.1,
					0.0, 1.3, 0.0, carSt4));
			configs.add(new H18LegConf(4.0, H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV, H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt3));
			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV, H18LegConf.AdjustMethod.LINEAR, 2.0, 0.0, 1.8, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt3));
			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV, H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 2.0, 0.0,
					2.0, 0.0, carSt4));
			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV, H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.8, 0.0,
					1.8, 0.0, carSt3));
			configs.add(new H18LegConf(2.0, H18LegConf.AdjustMethod.EXP, H18LegConf.AdjustMethod.LINEAR, H18LegConf.AdjustMethod.INV, H18LegConf.AdjustMethod.LINEAR, 1.0, 0.0, 1.5, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt3));
			//logger.error("Count EXP {}", configs.stream().filter(c -> c.getNearTravelAdjustFct().equals(H18LegConf.AdjustMethod.EXP)).count());
			logger.error("Count LINEAR {}", configs.stream().filter(c -> c.getNearTravelAdjustFct().equals(H18LegConf.AdjustMethod.LINEAR)).count());
			logger.error("Count INV {}", configs.stream().filter(c -> c.getNearTravelAdjustFct().equals(H18LegConf.AdjustMethod.INV)).count());
			H18LegConf wcfgOld = new H18LegConf(0.0, 0.0, 0.0);
			SolverFactory<H2018SolverLegConf, H2018BeanContainer, H18LegConf> sfactoryOld = new SolverFactory<>(H2018SolverLegConf.class, H2018BeanContainer.class, H18LegConf.class);
			WorkerFactory<H2018BeanContainer, H2018Reader, H2018Writer, H18LegConf, H2018SolverLegConf, H2018ScoreCalculator> sw0Old = new WorkerFactory<H2018BeanContainer, H2018Reader, H2018Writer, H18LegConf, H2018SolverLegConf, H2018ScoreCalculator>();
			MultipleConfFileSolverWorker<H2018BeanContainer, H18LegConf, H2018SolverLegConf> mfsw2018Old = sw0Old.builder()
					.readWrite(read2018, new NullWriter())
					.score(scor2018)
					.config(wcfgOld).solvers(sfactoryOld.createFromConfs(configs, timeout))
					.progressBar()
					.file(new InOut("src/main/resources/in/2018/a_example.in", null))
					.file(new InOut("src/main/resources/in/2018/b_should_be_easy.in", null))
					.file(new InOut("src/main/resources/in/2018/c_no_hurry.in", null))
					.file(new InOut("src/main/resources/in/2018/d_metropolis.in", null))
					.file(new InOut("src/main/resources/in/2018/e_high_bonus.in", null))
					.Build();
			
			//			List<H2018Solver> solvers = sfactory.createFromConfs(configs, timeout);
			//			
			//			final List<InOut> files2018 = new ArrayList<>();
			//			files2018.add(new InOut("src/main/resources/in/2018/a_example.in", null));
			//			files2018.add(new InOut("src/main/resources/in/2018/b_should_be_easy.in", null));
			//			files2018.add(new InOut("src/main/resources/in/2018/c_no_hurry.in", null));
			//			files2018.add(new InOut("src/main/resources/in/2018/d_metropolis.in", null));
			//			files2018.add(new InOut("src/main/resources/in/2018/e_high_bonus.in", null));
			//			
			//			final MultipleConfFileSolverWorker<H2018BeanContainer, H2018Config, H2018Solver> mfsw2018 = new MultipleConfFileSolverWorker<>(read2018, nwriter, scor2018, 1, workerCfg);
			//			mfsw2018.addFiles(files2018);
			//			mfsw2018.addSolver(solvers);
			//mfsw2018.setGlobalConstantsClass(CustomConstants.class);
			//			scores = mfsw2018Old.run();
			//			
			//			scores.displayAll();
			//			scores.displayBest();
			// if (MapUtils.isNotEmpty(scores)) {
			// MapUtils.verbosePrint(System.out, mfsw2018.getClass().getSimpleName() + " :
			// Score des fichiers pour chaque solver ", scores);
			// }
			// if (MapUtils.isNotEmpty(scores)) {
			// MapUtils.verbosePrint(System.out, "Meilleurs scores", scores);
			// }
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
