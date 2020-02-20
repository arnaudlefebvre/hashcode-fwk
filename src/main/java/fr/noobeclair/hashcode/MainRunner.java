package fr.noobeclair.hashcode;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.hashcode2020.H2020Config;
import fr.noobeclair.hashcode.bean.hashcode2020.H2020WorkerConfig;
import fr.noobeclair.hashcode.bean.hashcode2020.HashCode2020BeanContainer;
import fr.noobeclair.hashcode.in.hashcode2020.Hashcode2020Reader;
import fr.noobeclair.hashcode.out.hashcode2020.Hashcode2020Writer;
import fr.noobeclair.hashcode.score.hashcode2020.Hashcode2020ScoreCalculator;
import fr.noobeclair.hashcode.solve.hashcode2020.HashCode2020Solver;
import fr.noobeclair.hashcode.utils.Utils;
import fr.noobeclair.hashcode.utils.dto.WorkerResultDto;
import fr.noobeclair.hashcode.worker.InOut;
import fr.noobeclair.hashcode.worker.MultipleConfFileSolverWorker;

public class MainRunner {
	
	private static final Logger logger = LogManager.getLogger(MainRunner.class);
	
	public static final String CR = "\r";
	
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
		
		WorkerResultDto scores = null;
		try {
			final Hashcode2020Reader reader = new Hashcode2020Reader();
			H2020WorkerConfig wcfg2020 = new H2020WorkerConfig();
			final H2020Config cfg2020 = new H2020Config();
			final Hashcode2020Writer writer = new Hashcode2020Writer();
			final Hashcode2020ScoreCalculator scorer = new Hashcode2020ScoreCalculator();
			
			List<HashCode2020Solver> solvers = new ArrayList<>();
			// TODO 2020 : ajouter les solvers
			solvers.add(new HashCode2020Solver());
			
			final MultipleConfFileSolverWorker<HashCode2020BeanContainer, H2020Config, HashCode2020Solver, H2020WorkerConfig> mfsw = new MultipleConfFileSolverWorker<>(reader, writer, scorer, solvers,
					cfg2020, wcfg2020);
			final List<InOut> files = new ArrayList<>();
			
			files.add(new InOut("src/main/resources/in/a_example.txt"));
//			files.add(new InOut("src/main/resources/in/b_read_on.txt"));
//			files.add(new InOut("src/main/resources/in/c_incunabula.txt"));
//			files.add(new InOut("src/main/resources/in/d_tough_choices.txt"));
//			files.add(new InOut("src/main/resources/in/e_so_many_books.txt"));
//			files.add(new InOut("src/main/resources/in/f_libraries_of_the_world.txt"));
			
			// TODO 2020 : ajouter les fichiers d'entrées dans files
			mfsw.addFiles(files);
			scores = mfsw.run();
			
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
