package fr.noobeclair.hashcode.bean.config.param;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.annotation.ConfGenerable;
import fr.noobeclair.hashcode.bean.config.AbstractFactory;
import fr.noobeclair.hashcode.bean.config.Config;

public class DoubleConfigParam<C extends Config> extends ConfigParam<C> {
	
	Double mini;
	Double maxi;
	Double stepi;
	Double cur;
	
	public DoubleConfigParam() {
		// TODO Auto-generated constructor stub
	}
	
	public DoubleConfigParam(String fieldname, AbstractFactory.TYPE type, String min, String max, String step, ConfGenerable cf) {
		super(fieldname, type, min, max, step, cf);
		init(min, max, step);
	}
	
	private void init(String min, String max, String step) {
		this.mini = Double.parseDouble(min);
		this.maxi = Double.parseDouble(max);
		this.stepi = Double.parseDouble(step);
	}
	
	@Override
	public C go(C config) {
		init(min, max, step);
		if (StringUtils.isEmpty(current)) {
			cur = mini;
		} else {
			cur = Double.parseDouble(current);
		}
		if (cur <= maxi) {
			try {
				Field f = config.getClass().getDeclaredField(this.fieldName);
				f.setAccessible(true);
				f.set(config, cur);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			cur = cur + stepi;
		} else {
			return null;
		}
		
		current = cur.toString();
		return config;
	}
	
	@Override
	public Integer getNb() {
		return (int) ((maxi - mini) / stepi) + 1;
	}
	
	@Override
	protected String getValFromStep(String step) {
		Double i = Double.parseDouble(step);
		return "" + (mini + (stepi * i));
	}
}
