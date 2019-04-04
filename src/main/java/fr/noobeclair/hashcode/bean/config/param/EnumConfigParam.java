package fr.noobeclair.hashcode.bean.config.param;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.annotation.ConfGenerable;
import fr.noobeclair.hashcode.bean.config.AbstractFactory.TYPE;
import fr.noobeclair.hashcode.bean.config.Config;

public class EnumConfigParam<C extends Config> extends ConfigParam<C> {
	
	Integer mini;
	Integer maxi;
	Integer stepi;
	Object cur;
	int curpos;
	Object[] val;
	Class<? extends Enum> enumClass;
	String[] excludes;
	String[] includes;
	
	public EnumConfigParam() {
		// TODO Auto-generated constructor stub
	}
	
	public EnumConfigParam(String fieldname, TYPE type, ConfGenerable cf, Class<? extends Enum> enumClass, String[] excludes, String[] includes) {
		super(fieldname, type, "0", "0", "1", cf);
		this.enumClass = enumClass;
		Field f;
		this.val = null;
		try {
			//			f = enumClass.getDeclaredField("$VALUES");
			//			Object o = f.get(null);
			//			val = (Object[]) o;
			Method m = enumClass.getDeclaredMethod("values");
			Object obj = m.invoke(null);
			this.val = (Object[]) obj;
			
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.excludes = excludes;
		this.includes = includes;
		//		if (ArrayUtils.isNotEmpty(this.excludes) && ArrayUtils.isNotEmpty(this.includes)) {
		//			throw RuntimeException("Includes or Excludes")
		//		}
		
		if (ArrayUtils.isNotEmpty(this.includes)) {
			initIncludes();
		} else if (ArrayUtils.isNotEmpty(this.excludes)) {
			initExcludes();
		}
		
		this.max = "" + (val.length);
		init(min, max, "1");
	}
	
	private void initExcludes() {
		Object[] tmp = new Object[(val.length - (this.excludes != null ? this.excludes.length : 0))];
		if (this.excludes != null && this.excludes.length > 0) {
			int id = 0;
			for (int i = 0; i < val.length; i++) {
				if (!Arrays.asList(this.excludes).contains(getValName(i))) {
					tmp[id] = val[i];
					id = id + 1;
				}
			}
			this.val = tmp;
		}
	}
	
	private void initIncludes() {
		Object[] tmp = new Object[(this.includes != null ? this.includes.length : 0)];
		if (this.includes != null && this.includes.length > 0) {
			int id = 0;
			for (int i = 0; i < val.length; i++) {
				if (Arrays.asList(this.includes).contains(getValName(i))) {
					tmp[id] = val[i];
					id = id + 1;
				}
			}
			this.val = tmp;
		}
	}
	
	private void init(String min, String max, String step) {
		this.mini = Integer.parseInt(min);
		this.maxi = Integer.parseInt(max);
		this.stepi = Integer.parseInt(step);
	}
	
	private int getPosInVal() {
		for (int i = 0; i < val.length; i++) {
			Object o = val[i];
			if (current.equals(enumClass.cast(o).name())) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public void reset() {
		super.reset();
		this.curpos = 0;
	}
	
	@Override
	public void reset(String i) {
		super.reset(i);
		this.curpos = 0;
	}
	
	private String getValName(int i) {
		return enumClass.cast(val[i]).name();
	}
	
	@Override
	public C go(C config) {
		if (StringUtils.isEmpty(current)) {
			curpos = mini;
		} else {
			curpos = getPosInVal();
		}
		if (curpos <= val.length) {
			if (this.excludes == null || !Arrays.asList(this.excludes).contains(getValName(curpos))) {
				try {
					Field f = config.getClass().getDeclaredField(this.fieldName);
					f.setAccessible(true);
					f.set(config, val[curpos]);
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			cur = val[curpos];
			curpos = curpos + stepi;
		} else {
			return null;
		}
		
		current = cur.toString();
		return config;
	}
	
	@Override
	public Integer getNb() {
		return this.maxi;
	}
	
	@Override
	protected String getValFromStep(String step) {
		Integer i = Integer.parseInt(step);
		return getValName(i);
	}
}
