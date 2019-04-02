package fr.noobeclair.hashcode.bean.config;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.annotation.ConfGenerable;
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.bean.ConfigFactory.TYPE;

public class IntegerConfigParam<C extends Config> extends ConfigParam<C> {
	
	Integer mini;
	Integer maxi;
	Integer stepi;
	
	Integer cur;
	
	public IntegerConfigParam() {
		// TODO Auto-generated constructor stub
	}
	
	public IntegerConfigParam(String fieldname, TYPE type, String min, String max, String step, ConfGenerable cf) {
		super(fieldname, type, min, max, step, cf);
		this.mini = Integer.parseInt(min);
		this.maxi = Integer.parseInt(max);
		this.stepi = Integer.parseInt(step);
	}
	
	@Override
	public C go(C config) {
		if (StringUtils.isEmpty(current)) {
			cur = mini;
		}
		if (cur < maxi) {
			for (Field f : config.getClass().getFields()) {
				if (f.getName().equals(this.fieldName)) {
					f.setAccessible(true);
					try {
						f.setInt(config, cur);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			cur = cur + stepi;
		} else {
			return null;
		}
		
		current = cur.toString();
		return config;
	}
	
}
