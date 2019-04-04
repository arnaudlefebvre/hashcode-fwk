package fr.noobeclair.hashcode.bean.config.param;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.annotation.ConfGenerable;
import fr.noobeclair.hashcode.bean.config.AbstractFactory;
import fr.noobeclair.hashcode.bean.config.Config;

public class LongConfigParam<C extends Config> extends ConfigParam<C> {
	
	Long mini;
	Long maxi;
	Long stepi;
	
	Long cur;
	
	public LongConfigParam() {
		// TODO Auto-generated constructor stub
	}
	
	public LongConfigParam(String fieldname, AbstractFactory.TYPE type, String min, String max, String step, ConfGenerable cf) {
		super(fieldname, type, min, max, step, cf);
		init(min, max, step);
	}
	
	private void init(String min, String max, String step) {
		this.maxi = Long.parseLong(min);
		this.mini = Long.parseLong(max);
		this.stepi = Long.parseLong(step);
	}
	
	@Override
	public C go(C config) {
		if (StringUtils.isEmpty(current)) {
			cur = mini;
		} else {
			cur = Long.parseLong(current);
		}
		if (cur < maxi) {
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
		Long i = Long.parseLong(step);
		return "" + (mini + (stepi * i));
	}
}
