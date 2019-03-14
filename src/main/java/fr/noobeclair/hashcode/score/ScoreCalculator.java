package fr.noobeclair.hashcode.score;

import java.io.InputStream;

public abstract class ScoreCalculator {
	
	public ScoreCalculator() {
		// TODO Auto-generated constructor stub
	}
	
	protected abstract void run();
	
	public void score(InputStream in) {
		
		// TODO
		// 1 - read � file (a voir si pas plus int�ressant d'avoir une "Solution" mais
		// n�cessite d'�tre plus sp�cifique
		// 2 - Eventuellement lire tous les fichiers de solutions g�n�rer pour ainsi
		// faire un comparo des r�sultats de chacun des Solver
		// 3 - Ecrire des stats et les persister pour comparer entre les ex�cutions
		
	}
	
}
