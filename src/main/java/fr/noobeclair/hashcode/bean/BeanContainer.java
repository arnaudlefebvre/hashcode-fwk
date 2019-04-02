package fr.noobeclair.hashcode.bean;

/**
 * Bean Container for your bean \o/
 * 
 * Aims to store any data in your solving process. From input readed data to
 * calculate solution to write out.
 * It transit trhough all layer of worker process and must be extended
 * 
 * @author arnaud
 *
 */
public class BeanContainer {
	/**
	 * name of the input resources
	 * 
	 */
	protected String inName;
	
	public BeanContainer(String inName) {
		super();
		this.inName = inName;
	}
	
	public String getInName() {
		return inName;
	}
	
	public void setInName(String inName) {
		this.inName = inName;
	}
	
}
