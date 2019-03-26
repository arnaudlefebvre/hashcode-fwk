package fr.noobeclair.hashcode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.hashcode2018.H2018BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2018.H2018Config;
import fr.noobeclair.hashcode.bean.hashcode2018.H2018Config.CarStrategy;
import fr.noobeclair.hashcode.bean.hashcode2019.H2019Config;
import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.in.hashcode2018.H2018Reader;
import fr.noobeclair.hashcode.in.hashcode2019.Hashcode2019Reader;
import fr.noobeclair.hashcode.out.H2018Writer;
import fr.noobeclair.hashcode.out.hashcode2019.Hashcode2019Writer;
import fr.noobeclair.hashcode.score.hashcode2018.H2018ScoreCalculator;
import fr.noobeclair.hashcode.score.hashcode2019.Hashcode2019ScoreCalculator;
import fr.noobeclair.hashcode.solve.SolverFactory;
import fr.noobeclair.hashcode.solve.hashcode2018.H2018Solver;
import fr.noobeclair.hashcode.solve.hashcode2019.HashCode2019DummyStepSolver;
import fr.noobeclair.hashcode.solve.hashcode2019.HashCode2019StepSolver;
import fr.noobeclair.hashcode.utils.Utils;
import fr.noobeclair.hashcode.worker.InOut;
import fr.noobeclair.hashcode.worker.MultipleConfFileSolverWorker;
import fr.noobeclair.hashcode.worker.MultipleConfFileWorker;
import fr.noobeclair.hashcode.worker.MultipleConfSolverWorker;
import fr.noobeclair.hashcode.worker.SimpleConfWorker;

public class Main {
	public static final String CR = "\r";
	private static final Logger logger = LogManager.getLogger(Main.class);
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
		Map<String, BigDecimal> scores = null;
		try {
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
			if (MapUtils.isNotEmpty(scores)) {
				MapUtils.verbosePrint(System.out, mfw.getClass() + "-" + solver.getClass() +
						" : Score de chaque fichier ", scores);
			}
			
			// Multiple ConfigSolver Worker - 1 files int/out - n algorithm
			final MultipleConfSolverWorker<HashCode2019BeanContainer, H2019Config, HashCode2019StepSolver> msw = new MultipleConfSolverWorker<>(reader, writer, scorer,
					new InOut("src/main/resources/in/a_example.txt",
							"src/main/resources/out/a_example.out.txt"),
					cfg2019);
			msw.addSolver(solver);
			msw.addSolver(dummySolver);
			// scores = msw.run();
			if (MapUtils.isNotEmpty(scores)) {
				MapUtils.verbosePrint(System.out, msw.getClass().getSimpleName() + " : Score du fichier " + msw.getInOut().in, scores);
			}
			
			final MultipleConfFileSolverWorker<HashCode2019BeanContainer, H2019Config, HashCode2019StepSolver> mfsw = new MultipleConfFileSolverWorker<>(reader, writer, scorer,
					Arrays.asList(dummySolver, solver), cfg2019);
			mfsw.addFiles(files);
			// scores = mfsw.run();
			if (MapUtils.isNotEmpty(scores)) {
				MapUtils.verbosePrint(System.out, msw.getClass().getSimpleName() + " : Score des fichiers pour chaque solver ", scores);
			}
			
			H2018Reader read2018 = new H2018Reader();
			H2018ScoreCalculator scor2018 = new H2018ScoreCalculator();
			H2018Writer nwriter = new H2018Writer();
			
			List<CarStrategy> carSt1 = Arrays.asList();
			List<CarStrategy> carSt2 = Arrays.asList(CarStrategy.NEAR_FIRST);
			List<CarStrategy> carSt3 = Arrays.asList(CarStrategy.NEAR_FIRST, CarStrategy.QUICK_FIRST);
			List<CarStrategy> carSt4 = Arrays.asList(CarStrategy.LONG_FIRST);
			List<CarStrategy> carSt5 = Arrays.asList(CarStrategy.LONG_FIRST, CarStrategy.QUICK_FIRST);
			List<CarStrategy> carStAgr = Arrays.asList(CarStrategy.AGGRESSIVE);
			
			H2018Config workerCfg = new H2018Config(0.0, 0.0, 0.0);
			List<H2018Config> configs = new ArrayList<>();
			// EXP
			
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV, H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt2));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV, H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt3));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV, H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt4));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV, H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt5));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV, H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5, 0.0,
					1.5, 0.0, carStAgr));
			
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.LINEAR, 2.0, 0.0, 3.0, 0.0, 1.5,
					0.0, 1.5, 0.0, carSt2));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.LINEAR, 2.0, 0.0, 3.0, 0.0, 1.5,
					0.0, 1.5, 0.0, carSt3));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5,
					0.0, 1.5, 0.0, carSt4));
			
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.LINEAR, 2.0, 0.0, 3.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt2));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.LINEAR, 2.0, 0.0, 3.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt3));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt4));
			
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt4));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt5));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carStAgr));
			
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt4));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt5));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carStAgr));
			
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt4));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt5));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carStAgr));
			
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.EXP,
					H2018Config.AdjustMethod.EXP, 2.0, 0.0, 3.0, 0.0, 1.5,
					0.0, 1.5, 0.0, carSt2));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.EXP,
					H2018Config.AdjustMethod.EXP, 2.0, 0.0, 3.0, 0.0, 1.5,
					0.0, 1.5, 0.0, carSt3));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.EXP,
					H2018Config.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt4));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.EXP,
					H2018Config.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt5));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP,
					H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.EXP,
					H2018Config.AdjustMethod.EXP, 1.0, 0.0, 2.0, 0.0, 1.5, 0.0,
					1.5, 0.0, carStAgr));
			
			// Winners Custom Try
			configs.add(new H2018Config(3.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5,
					0.0, 1.5, 0.0, carSt4));
			configs.add(new H2018Config(1.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.3,
					0.0, 1.5, 0.0, carSt4));
			configs.add(new H2018Config(1.0, H2018Config.AdjustMethod.LINEAR,
					H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV,
					H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 1.0, 0.0, 1.1,
					0.0, 1.3, 0.0, carSt4));
			configs.add(new H2018Config(4.0, H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV, H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt3));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV, H2018Config.AdjustMethod.LINEAR, 2.0, 0.0, 1.8, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt3));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV, H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 2.0, 0.0,
					2.0, 0.0, carSt4));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV, H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 1.1, 0.0, 1.8, 0.0,
					1.8, 0.0, carSt3));
			configs.add(new H2018Config(2.0, H2018Config.AdjustMethod.EXP, H2018Config.AdjustMethod.LINEAR, H2018Config.AdjustMethod.INV, H2018Config.AdjustMethod.LINEAR, 1.0, 0.0, 1.5, 0.0, 1.5, 0.0,
					1.5, 0.0, carSt3));
			
			SolverFactory<H2018Solver, H2018BeanContainer, H2018Config> sfactory = new SolverFactory<>(H2018Solver.class, H2018BeanContainer.class, H2018Config.class);
			List<H2018Solver> solvers = sfactory.createFromConfs(configs, timeout);
			
			final List<InOut> files2018 = new ArrayList<>();
			files2018.add(new InOut("src/main/resources/in/2018/a_example.in", null));
			files2018.add(new InOut("src/main/resources/in/2018/b_should_be_easy.in", null));
			files2018.add(new InOut("src/main/resources/in/2018/c_no_hurry.in", null));
			files2018.add(new InOut("src/main/resources/in/2018/d_metropolis.in", null));
			files2018.add(new InOut("src/main/resources/in/2018/e_high_bonus.in", null));
			
			final MultipleConfFileSolverWorker<H2018BeanContainer, H2018Config, H2018Solver> mfsw2018 = new MultipleConfFileSolverWorker<>(read2018, nwriter, scor2018, 1, workerCfg);
			mfsw2018.addFiles(files2018);
			mfsw2018.addSolver(solvers);
			scores = mfsw2018.run();
			
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
