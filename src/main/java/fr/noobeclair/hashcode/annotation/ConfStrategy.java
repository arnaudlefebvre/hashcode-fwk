package fr.noobeclair.hashcode.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(ConfStrategiesAnnotation.class)
public @interface ConfStrategy {
	String id();
	
	String[] excludes() default {};
	
	String[] includes() default {};
	
	String min() default "";
	
	String max() default "";
	
	String step() default "1";
}
