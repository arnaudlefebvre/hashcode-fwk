package fr.noobeclair.hashcode.out.hashcode2018;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.collections4.CollectionUtils;

import fr.noobeclair.hashcode.bean.hashcode2018.Car;
import fr.noobeclair.hashcode.bean.hashcode2018.H2018BeanContainer;
import fr.noobeclair.hashcode.out.OutWriter;

public class H2018Writer extends OutWriter<H2018BeanContainer> {

	private boolean enabled = true;

	public H2018Writer() {
		// TODO Auto-generated constructor stub
	}

	public H2018Writer(boolean enabled) {
		super();
		this.enabled = enabled;
	}

	@Override
	protected void writeFile(H2018BeanContainer out, String path) {
		if (enabled) {
			try (FileWriter writer = new FileWriter(path)) {
				if (CollectionUtils.isNotEmpty(out.getCars())) {
					for (Car c : out.getCars()) {
						writer.write(c.write());
					}
				}
			} catch (IOException e) {
				logger.error("Erreur lors de l'écriture du fichier {}", path, e);
			}
		}
	}

}
