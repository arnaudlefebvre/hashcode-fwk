package fr.noobeclair.hashcode.bean.config;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.annotation.ConfGenerable;
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.bean.ConfigFactory.TYPE;

public class BigDecConfigParam<C extends Config> extends ConfigParam<C> {
	
	BigDecimal mini;
	BigDecimal maxi;
	BigDecimal stepi;
	
	BigDecimal cur;
	
	public BigDecConfigParam() {
		// TODO Auto-generated constructor stub
	}
	
	public BigDecConfigParam(String fieldname, TYPE type, String min, String max, String step, ConfGenerable cf) {
		super(fieldname, type, min, max, step, cf);
		this.maxi = new BigDecimal(min);
		this.mini = new BigDecimal(max);
		this.stepi = new BigDecimal(step);
	}
	
	@Override
	public C go(C config) {
		if (StringUtils.isEmpty(current)) {
			cur = mini;
		}
		if (cur.compareTo(maxi) < 0) {
			for (Field f : config.getClass().getFields()) {
				if (f.getName().equals(this.fieldName)) {
					f.setAccessible(true);
					try {
						f.set(config, cur);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			cur = cur.add(stepi);
		}
		
		current = cur.toString();
		return config;
	}
}
