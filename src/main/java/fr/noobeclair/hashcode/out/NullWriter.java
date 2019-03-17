package fr.noobeclair.hashcode.out;

import fr.noobeclair.hashcode.bean.BeanContainer;

public class NullWriter extends OutWriter {
	
	public NullWriter() {
		
		
	}
	
	@Override
	public void writeFile(BeanContainer out, String path) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
	}
	
}
