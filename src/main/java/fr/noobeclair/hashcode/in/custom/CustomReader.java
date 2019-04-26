/**
 * 
 */
package fr.noobeclair.hashcode.in.custom;

import java.util.List;

import fr.noobeclair.hashcode.bean.custom.CustomBeanContainer;
import fr.noobeclair.hashcode.in.InReader;

public class CustomReader extends InReader<CustomBeanContainer> {

	public CustomReader() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected CustomBeanContainer readFile(List<String> lines, String in) {
		CustomBeanContainer result = null;

		int i = 0;
		for (String ln : lines) {
			// TODO Do something
		}

		return result;
	}

}
