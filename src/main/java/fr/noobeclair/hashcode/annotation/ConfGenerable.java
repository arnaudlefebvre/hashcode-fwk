package fr.noobeclair.hashcode.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.noobeclair.hashcode.bean.config.AbstractFactory;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfGenerable {
	AbstractFactory.TYPE type();
	
	Class<? extends Enum> eClass() default Enum.class;
	
	String[] excludes() default {};
	
	String[] includes() default {};
	
	String min() default "";
	
	String max() default "";
	
	String step() default "1";
	
}