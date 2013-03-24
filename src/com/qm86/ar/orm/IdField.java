package com.qm86.ar.orm;

import java.lang.reflect.Field;

import com.qm86.ar.annotation.GeneratorType;
import com.qm86.ar.annotation.Id;

/**
 * @Title: IdField.java
 * @Package com.qm86.ar.orm
 * @Description: TODO
 * @author HeroW
 * @date Dec 10, 2012 6:53:39 PM
 * @version 
 */

public class IdField extends FieldInfo{

	public IdField(){}
	public IdField(FieldInfo field) {
		super(field);
	}
	private Id annotation;
	private GeneratorType generator;
	
	
	
	public GeneratorType getGenerator() {
		return generator;
	}
	public void setGenerator(GeneratorType generator) {
		this.generator = generator;
	}

	public Id getAnnotation() {
		return annotation;
	}
	public void setAnnotation(Id annotation) {
		this.annotation = annotation;
	}
}
