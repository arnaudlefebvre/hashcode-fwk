package fr.noobeclair.hashcode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.utils.Utils;

public class Main {
	
	private static final Logger logger = LogManager.getLogger(Main.class);
	//1 - create � worker. 
	//2 - workers use � reader to read input
	//3 - worker use � solver which provide � solution
	//4 - worker use � writer to write out
	//5 - Eventually provide stats informations
	public Main() {
		long start = System.currentTimeMillis();
		logger.debug("------------------------------------------------------------------------");
		logger.debug("--                          RuleFileRotate                            --");
		logger.debug("------------------------------------------------------------------------");
		
		try {
			//Worker.run()
		} finally {
			logger.info("------------------------------------------------------------------------");
			logger.info("-- End. Total Time : "+Utils.roundMiliTime((System.currentTimeMillis() - start), 3) + "s --");
			logger.info("------------------------------------------------------------------------");
		}
	
	}

}
