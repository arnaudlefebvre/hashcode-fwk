package fr.noobeclair.hashcode.in;

import fr.noobeclair.hashcode.bean.BeanContainer;

public class NullReader extends InReader {
	
	public NullReader() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected BeanContainer readFile(String in) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
}
