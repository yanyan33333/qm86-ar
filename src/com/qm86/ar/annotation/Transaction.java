package com.qm86.ar.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title: Transaction.java
 * @Package com.qm86.ar.annotation
 * @Description: TODO
 * @author HeroW
 * @date Dec 11, 2012 10:52:16 PM
 * @version 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transaction {
	boolean autoCommit() default false;
}
