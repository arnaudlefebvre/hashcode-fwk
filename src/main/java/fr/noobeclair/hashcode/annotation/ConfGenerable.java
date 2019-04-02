package fr.noobeclair.hashcode.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.noobeclair.hashcode.bean.ConfigFactory;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfGenerable {
	ConfigFactory.TYPE type();
	
	String min() default "";
	
	String max() default "";
	
	String step() default "1";
}
