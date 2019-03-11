package fr.noobeclair.hashcode.score;

import java.io.InputStream;

public abstract class ScoreCalculator {

	public ScoreCalculator() {
		// TODO Auto-generated constructor stub
	}
	
	protected  abstract void run();
	
	public void score(InputStream in) {
		
		
		//TODO
		// 1 - read à file (a voir si pas plus intéressant d'avoir une "Solution" mais nécessite d'être plus spécifique
		// 2 - Eventuellement lire tous les fichiers de solutions générer pour ainsi faire un comparo des résultats de chacun des Solver
		// 3 - Ecrire des stats et les persister pour comparer entre les exécutions
		
	}

}
