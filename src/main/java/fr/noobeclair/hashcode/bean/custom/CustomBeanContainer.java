/**
 * 
 */
package fr.noobeclair.hashcode.bean.custom;

import fr.noobeclair.hashcode.bean.BeanContainer;

/**
 * @author arnaud
 *
 */
public class CustomBeanContainer extends BeanContainer {

	/**
	 * 
	 */
	public CustomBeanContainer(String inName) {
		super(inName);
	}

	@Override
	public Object getNew() {
		// Add custom properties copy
		// this.mycustom properties = o.custom;
		return this;
	}

}
