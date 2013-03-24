package com.qm86.ar.orm;

import java.lang.reflect.Field;

import com.qm86.ar.annotation.Column;

/**
 * @Title: ColumnField.java
 * @Package com.qm86.ar.orm
 * @Description: TODO
 * @author HeroW
 * @date Dec 10, 2012 6:49:06 PM
 * @version 
 */

public class ColumnField extends FieldInfo{
	
	public ColumnField(){}
	public ColumnField(FieldInfo field){
		super(field);
	}

	private Column annotation;

	public Column getAnnotation() {
		return annotation;
	}
	public void setAnnotation(Column annotation) {
		this.annotation = annotation;
	}
}
