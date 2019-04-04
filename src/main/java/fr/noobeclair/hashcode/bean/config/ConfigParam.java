package fr.noobeclair.hashcode.bean.config;

import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.annotation.ConfGenerable;
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.bean.ConfigFactory;

/**
 * @author arnaud
 *
 * @param <C>
 */
/**
 * @author arnaud
 *
 * @param <C>
 */
public class ConfigParam<C extends Config> implements Comparable<ConfigParam<C>> {
	
	protected ConfigFactory.TYPE type;
	protected String min;
	protected String max;
	protected String step;
	protected ConfGenerable cf;
	
	protected String fieldName = StringUtils.EMPTY;
	protected String current = StringUtils.EMPTY;
	protected Integer nb = null;
	
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
	
	public void reset(String i) {
		if ("0".equals(i)) {
			reset();
		} else {
			this.current = getValFromStep(i);
		}
	}
	
	protected String getValFromStep(String step) {
		return StringUtils.EMPTY;
	}
	
	public Integer getNbConf() {
		if (nb == null) {
			nb = getNb();
		}
		return nb;
	}
	
	public Integer getNb() {
		return 0;
	}
	
	@Override
	public int compareTo(ConfigParam<C> o) {
		int thisnb = this.getNbConf();
		int onb = o.getNbConf();
		if (thisnb > onb) {
			return 1;
		} else if (thisnb < onb) {
			return -1;
		} else {
			return 0;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConfigParam [");
		if (type != null) {
			builder.append("type=");
			builder.append(type);
			builder.append(", ");
		}
		if (fieldName != null) {
			builder.append("fieldName=");
			builder.append(fieldName);
			builder.append(", ");
		}
		if (nb != null) {
			builder.append("nb=");
			builder.append(nb);
		}
		builder.append("]");
		return builder.toString();
	}
	
}
