package fr.noobeclair.hashcode.bean.config;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public abstract class Config {

	public enum SHOW_OPT {
		SIMPLE, ALL, GEN
	}

	public Config() {
		super();
	}

	public Config(Config c) {

	}

	public Config clone(Config c) throws CloneNotSupportedException {
		Config res = (Config) c.clone();

		return res;
	}

	public void cloneTo(Config c) {

	}

	public String show() {
		return show(SHOW_OPT.SIMPLE);
	};

	public String show(SHOW_OPT opt) {
		switch (opt) {
		case SIMPLE:
			return showSimple();
		case ALL:
			return showAll();
		case GEN:
			return showGen();
		default:
			break;
		}
		return toString();
	};

	protected String showSimple() {
		return toString();
	};

	protected String showAll() {
		return ReflectionToStringBuilder.toString(this);
	};

	protected String showGen() {
		return ReflectionToStringBuilder.toStringExclude(this);
	}

}
