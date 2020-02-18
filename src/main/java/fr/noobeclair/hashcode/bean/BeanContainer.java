package fr.noobeclair.hashcode.bean;

/**
 * Bean Container for your bean \o/
 * 
 * Aims to store any data in your solving process. From input read data to
 * calculate solution to write out.
 * It transit through all layer of worker process and must be extended
 * 
 * @author arnaud
 *
 */
public abstract class BeanContainer {
	/**
	 * name of the input resources
	 * 
	 */
	protected String inName;

	public abstract Object getNew();

	public BeanContainer(Object o) {
		super();
		BeanContainer b = (BeanContainer) o;
		this.inName = b.inName;
	}

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
