package fr.noobeclair.hashcode.utils;

import java.util.List;

/**
 * Utilitaire de distance
 * 
 * @author apichard
 *
 */
public final class ReadFileUtil {
	
	/**
	 * Constructeur privée
	 */
	private ReadFileUtil() {
	}
	
	/**
	 * Récupère les données séparés par des espaces
	 * 
	 * @param lines
	 *            les lignes
	 * @param index
	 *            l'index
	 * @return le tableau de données
	 */
	public static String[] getTabFromLineSpace(List<String> lines, int index) {
		return lines.get(index).split(" ");
	}
	
}
