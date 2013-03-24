package com.qm86.ar.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title: Table.java
 * @Package com.qm86.ar.annotation
 * @Description: TODO
 * @author HeroW
 * @date Dec 10, 2012 3:04:56 PM
 * @version 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	String name() default "_null";
}
