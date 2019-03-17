package fr.noobeclair.hashcode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.in.hashcode2019.Hashcode2019Reader;
import fr.noobeclair.hashcode.out.hashcode2019.Hashcode2019Writer;
import fr.noobeclair.hashcode.score.hashcode2019.Hashcode2019ScoreCalculator;
import fr.noobeclair.hashcode.solve.hashcode2019.HashCode2019DummyStepSolver;
import fr.noobeclair.hashcode.solve.hashcode2019.HashCode2019StepSolver;
import fr.noobeclair.hashcode.utils.Utils;
import fr.noobeclair.hashcode.worker.InOut;
import fr.noobeclair.hashcode.worker.MultipleFileWorker;
import fr.noobeclair.hashcode.worker.MultipleSolverWorker;
import fr.noobeclair.hashcode.worker.SimpleWorker;

public class Main {
	
	private static final Logger logger = LogManager.getLogger(Main.class);
	// 1 - create � worker.
	// 2 - workers use � reader to read input
	// 3 - worker use � solver which provide � solution
	// 4 - worker use � writer to write out
	// 5 - Eventually provide stats informations
	
	// TODO - Voir pour creer une tache maven Zipator (ie : prends les sources, les
	// zip et ajoute les fichiers de r�sultats dans un dossier sp�cifique)
	// Comment on peut automatiser l'upload
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		logger.info("------------------------------------------------------------------------");
		logger.info("--                        Hascode Noobeclair                          --");
		logger.info("------------------------------------------------------------------------");
		
		try {
			//Simple worker - 1 file in/out - 1 Algorithm - Optionnal : scorer  
			Hashcode2019Reader reader = new Hashcode2019Reader();
			//HashCode2019StepSolver solver = new HashCode2019StepSolver(TimeUnit.MINUTES.toSeconds(1));
			HashCode2019StepSolver solver = new HashCode2019StepSolver(20L);
			Hashcode2019Writer writer = new Hashcode2019Writer();
			Hashcode2019ScoreCalculator scorer = new Hashcode2019ScoreCalculator();
			SimpleWorker<HashCode2019BeanContainer> sw = new SimpleWorker<>(reader, solver, writer, "src/main/resources/in/c_memorable_moments.txt", "src/main/resources/out/c_example.out.txt");
			//sw.run();
			
			
			//Mutiple File Worker - n files in/out - 1 algorithm
			MultipleFileWorker<HashCode2019BeanContainer> mfw = new MultipleFileWorker<>(reader, writer, scorer, solver);
			List<InOut> files = new ArrayList<>();
			files.add(new InOut("src/main/resources/in/a_example.txt", "src/main/resources/out/a_example.out.txt"));
			//files.add(new InOut("src/main/resources/in/b_lovely_landscapes.txt", "src/main/resources/out/b_example.out.txt"));
			//files.add(new InOut("src/main/resources/in/c_memorable_moments.txt", "src/main/resources/out/c_example.out.txt"));
			mfw.addFiles(files);
			Map<String, BigDecimal> scores = mfw.run();
			if(MapUtils.isNotEmpty(scores)) {
				MapUtils.verbosePrint(System.out, mfw.getClass() +"-"+solver.getClass()+ " : Score de chaque fichier ", scores);
			}
			
			HashCode2019DummyStepSolver dummySolver = new HashCode2019DummyStepSolver();
			MultipleSolverWorker<HashCode2019BeanContainer> msw = new MultipleSolverWorker<>(reader, writer, scorer, new InOut("src/main/resources/in/a_example.txt", "src/main/resources/out/a_example.out.txt"));
			msw.addSolver(solver);
			msw.addSolver(dummySolver);
			//scores = msw.run();
			if(MapUtils.isNotEmpty(scores)) {
				//MapUtils.verbosePrint(System.out, msw.getClass().getSimpleName() + " : Score du fichier "+msw.getInOut().in, scores);
			}
			

		} finally {
			logger.info("------------------------------------------------------------------------");
			logger.info("-- End. Total Time : " + Utils.roundMiliTime((System.currentTimeMillis() - start), 3) + "s --");
			logger.info("------------------------------------------------------------------------");
		}
		
	}
	
}
