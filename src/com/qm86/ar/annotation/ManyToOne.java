package com.qm86.ar.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title: ManyToOne.java
 * @Package com.qm86.ar.annotation
 * @Description: TODO
 * @author HeroW
 * @date Jan 30, 2013 11:34:10 AM
 * @version 
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOne {
	String foreignKey();
}