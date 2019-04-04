package fr.noobeclair.hashcode.bean.config.param;

import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.annotation.ConfGenerable;
import fr.noobeclair.hashcode.bean.config.AbstractFactory;
import fr.noobeclair.hashcode.bean.config.Config;

public class BooleanConfigParam<C extends Config> extends ConfigParam<C> {
	
	Boolean cur = true;
	
	public BooleanConfigParam() {
		// TODO Auto-generated constructor stub
	}
	
	public BooleanConfigParam(String fieldname, AbstractFactory.TYPE type, ConfGenerable cf) {
		super(fieldname, type, "0", "0", "0", cf);
	}
	
	@Override
	public C go(C config) {
		if (StringUtils.isEmpty(current)) {
			cur = true;
		} else {
			cur = Boolean.valueOf(current);
		}
		cur = !cur;
		
		current = cur.toString();
		return config;
	}
	
	@Override
	public Integer getNb() {
		return 1;
	}
	
	@Override
	protected String getValFromStep(String step) {
		return (cur ? "0" : "1");
	}
}
