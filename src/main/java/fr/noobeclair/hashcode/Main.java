package fr.noobeclair.hashcode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.in.Hashcode2019Reader;
import fr.noobeclair.hashcode.in.NullReader;
import fr.noobeclair.hashcode.out.Hashcode2019Writer;
import fr.noobeclair.hashcode.out.NullWriter;
import fr.noobeclair.hashcode.solve.HashCode2019Solver;
import fr.noobeclair.hashcode.solve.NullSolver;
import fr.noobeclair.hashcode.utils.Utils;

public class Main {
	
	private static final Logger logger = LogManager.getLogger(Main.class);
	//1 - create � worker. 
	//2 - workers use � reader to read input
	//3 - worker use � solver which provide � solution
	//4 - worker use � writer to write out
	//5 - Eventually provide stats informations
	
	//TODO - Voir pour creer une tache maven Zipator (ie : prends les sources, les zip et ajoute les fichiers de r�sultats dans un dossier sp�cifique)
	// Comment on peut automatiser l'upload
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		logger.debug("------------------------------------------------------------------------");
		logger.debug("--                        Hascode Noobeclair                          --");
		logger.debug("------------------------------------------------------------------------");
		
		try {
//			NullWorker worker = new NullWorker(new NullReader(), new NullSolver(), new NullWriter());
//			worker.run();
//			SimpleWorker simpleWorker = new SimpleWorker(new Hashcode2019Reader(), new HashCode2019Solver(), new Hashcode2019Writer(), "src/main/resources/hascode2019/in/b_lovely_landscapes.txt","src/main/resources/hascode2019/out/b_example.out.txt");
//			simpleWorker.run();
			SimpleWorker simpleWorker2 = new SimpleWorker(new Hashcode2019Reader(), new HashCode2019Solver(), new Hashcode2019Writer(), "src/main/resources/in/b_lovely_landscapes.txt","src/main/resources/out/b_example.out.txt");
			//simpleWorker2.runSteps();
			SimpleWorker simpleWorker3 = new SimpleWorker(new Hashcode2019Reader(), new HashCode2019Solver(), new Hashcode2019Writer(), "src/main/resources/in/c_memorable_moments.txt","src/main/resources/out/c_example.out.txt");
			simpleWorker3.runSteps();
		} finally {
			logger.debug("------------------------------------------------------------------------");
			logger.debug("-- End. Total Time : "+Utils.roundMiliTime((System.currentTimeMillis() - start), 3) + "s --");
			logger.debug("------------------------------------------------------------------------");
		}
	
	}

}
