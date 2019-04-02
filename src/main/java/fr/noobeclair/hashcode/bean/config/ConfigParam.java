package fr.noobeclair.hashcode.bean.config;

import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.annotation.ConfGenerable;
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.bean.ConfigFactory;

public class ConfigParam<C extends Config> {
	
	protected ConfigFactory.TYPE type;
	protected String min;
	protected String max;
	protected String step;
	protected ConfGenerable cf;
	
	protected String fieldName = StringUtils.EMPTY;
	protected String current = StringUtils.EMPTY;
	
	public ConfigParam() {
		// TODO Auto-generated constructor stub
	}
	
	public ConfigParam(String fieldname, ConfigFactory.TYPE type, String min, String max, String step, ConfGenerable cf) {
		super();
		this.fieldName = fieldname;
		this.type = type;
		this.min = min;
		this.max = max;
		this.step = step;
		this.cf = cf;
	}
	
	public ConfigFactory.TYPE getType() {
		return type;
	}
	
	public void setType(ConfigFactory.TYPE type) {
		this.type = type;
	}
	
	public String getMin() {
		return min;
	}
	
	public void setMin(String min) {
		this.min = min;
	}
	
	public String getMax() {
		return max;
	}
	
	public void setMax(String max) {
		this.max = max;
	}
	
	public String getStep() {
		return step;
	}
	
	public void setStep(String step) {
		this.step = step;
	}
	
	public C go(C config) {
		return config;
	}
	
	public void reset() {
		this.current = StringUtils.EMPTY;
	}
}
