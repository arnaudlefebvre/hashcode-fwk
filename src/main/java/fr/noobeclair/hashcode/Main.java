package fr.noobeclair.hashcode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.in.NullReader;
import fr.noobeclair.hashcode.out.NullWriter;
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
			NullWorker worker = new NullWorker(new NullReader(), new NullSolver(), new NullWriter());
			worker.run();
		} finally {
			logger.debug("------------------------------------------------------------------------");
			logger.debug("-- End. Total Time : "+Utils.roundMiliTime((System.currentTimeMillis() - start), 3) + "s --");
			logger.debug("------------------------------------------------------------------------");
		}
	
	}

}
