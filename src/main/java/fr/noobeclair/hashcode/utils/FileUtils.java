package fr.noobeclair.hashcode.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utilitaire de distance
 * 
 * @author apichard
 *
 */
public final class FileUtils {
	
	private static final Logger logger = LogManager.getLogger(FileUtils.class);
	
	/**
	 * Constructeur privée
	 */
	private FileUtils() {
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
	
	public static Long countLines(String filepath) {
		Long lines = 0L;
		try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
			while (reader.readLine() != null)
				lines++;
		} catch (Exception e) {
			logger.error("Erreur de lecture du fichier {}", filepath);
		}
		return lines;
	}
	
	public static void rm(String path) {
		try {
			File f = new File(path);
			if (f.exists()) {
				org.apache.commons.io.FileUtils.forceDelete(f);
			}
		} catch (IOException e) {
			logger.error("Erreur suppression du fichier {}", path);
		}
	}
	
}
