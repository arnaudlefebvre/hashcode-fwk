package fr.noobeclair.hashcode.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import fr.noobeclair.hashcode.annotation.ConfGenerable;
import fr.noobeclair.hashcode.bean.config.BigDecConfigParam;
import fr.noobeclair.hashcode.bean.config.ConfigParam;
import fr.noobeclair.hashcode.bean.config.DoubleConfigParam;
import fr.noobeclair.hashcode.bean.config.IntegerConfigParam;
import fr.noobeclair.hashcode.bean.config.LongConfigParam;

public class ConfigFactory<C extends Config> {
	public enum TYPE {
		INT, LONG, DOUBLE, BIGDEC, BOOLEAN, ENUM
	}
	
	Class<C> c;
	List<ConfigParam<C>> cfgparams = new ArrayList<>();
	List<C> result = new ArrayList<>();
	
	public ConfigFactory(Class<C> c) {
		this.c = c;
	}
	
	public C generate(C config) throws InstantiationException, IllegalAccessException {
		
		C res = c.newInstance();
		
		//if (c.isAnnotationPresent(ConfGenerable.class)) {
		for (Field f : c.getFields()) {
			if (f.isAnnotationPresent(ConfGenerable.class)) {
				ConfGenerable cf = f.getAnnotation(ConfGenerable.class);
				validateAddConfParam(cf, f);
			}
		}
		//}
		return res;
	}
	
	private C newConf(C toAdd) throws InstantiationException, IllegalAccessException {
		C res = (C) toAdd.getClass().newInstance();
		toAdd.cloneTo(res);
		return res;
	}
	
	private List<C> genConf(C config) throws InstantiationException, IllegalAccessException {
		C toadd = newConf(config);
		
		addConf(0, toadd);
		return result;
	}
	
	private C addConf(int c, C toAdd) {
		if (c >= cfgparams.size()) {
			System.out.println("End");
		}
		for (int i = c; i < cfgparams.size(); i++) {
			int next = i + 1;
			C tmp = cfgparams.get(i).go(toAdd);
			while (tmp != null) {
				result.add(toAdd);
				tmp = cfgparams.get(i).go(tmp);
			}
			toAdd = addConf(next, toAdd);
		}
		return toAdd;
	}
	
	private void validateAddConfParam(ConfGenerable cf, Field f) {
		cfgparams = new ArrayList<>();
		Preconditions.checkNotNull("ConFGenerable.TYPE is required ", cf.type());
		TYPE type = cf.type();
		if (ConfigFactory.TYPE.ENUM == type || ConfigFactory.TYPE.BOOLEAN == type) {
			
		} else {
			String min = cf.min();
			String max = cf.max();
			String step = cf.step();
			Preconditions.checkNotNull("ConFGenerable.min is required ", min);
			Preconditions.checkNotNull("ConFGenerable.max is required ", max);
			Preconditions.checkNotNull("ConFGenerable.step is required ", step);
			switch (type) {
			case INT:
				cfgparams.add(new IntegerConfigParam<C>(f.getName(), type, min, max, step, cf));
				break;
			case BIGDEC:
				cfgparams.add(new BigDecConfigParam<C>(f.getName(), type, min, max, step, cf));
				break;
			case DOUBLE:
				cfgparams.add(new DoubleConfigParam<C>(f.getName(), type, min, max, step, cf));
				break;
			case LONG:
				cfgparams.add(new LongConfigParam<C>(f.getName(), type, min, max, step, cf));
				break;
			default:
				break;
			}
		}
	}
}
