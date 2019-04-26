package fr.noobeclair.hashcode.bean.config.param;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.annotation.ConfGenerable;
import fr.noobeclair.hashcode.bean.config.AbstractFactory.TYPE;
import fr.noobeclair.hashcode.bean.config.Config;

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
		init(min, max, step);
	}
	
	private void init(String min, String max, String step) {
		this.mini = new BigDecimal(min);
		this.maxi = new BigDecimal(max);
		this.stepi = new BigDecimal(step);
	}
	
	@Override
	public C go(C config) {
		if (StringUtils.isEmpty(current)) {
			cur = mini;
		} else {
			cur = new BigDecimal(current);
		}
		
		if (cur.compareTo(maxi) <= 0) {
			try {
				Field f = config.getClass().getDeclaredField(this.fieldName);
				f.setAccessible(true);
				f.set(config, cur);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cur = cur.add(stepi);
		}
		
		current = cur.toString();
		return config;
	}
	
	@Override
	public Integer getNb() {
		return maxi.subtract(mini).divide(stepi, 3, RoundingMode.HALF_UP).add(BigDecimal.ONE).intValue();
	}
	
	@Override
	protected String getValFromStep(String step) {
		BigDecimal i = new BigDecimal(step);
		return stepi.multiply(i).add(mini).toString();//"" + (min + (stepi * i));
	}
}
