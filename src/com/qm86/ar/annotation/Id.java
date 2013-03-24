package com.qm86.ar.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title: Id.java
 * @Package com.qm86.ar.annotation
 * @Description: TODO
 * @author HeroW
 * @date Dec 10, 2012 2:56:13 PM
 * @version 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
	String name() default "_null";
	GeneratorType generator() default GeneratorType.NONE;

}
