package com.qm86.ar.orm;

import java.lang.reflect.Field;

import com.qm86.ar.annotation.OneToMany;


/**
 * @Title: OneToManyField.java
 * @Package com.et.ar.orm
 * @Description: TODO
 * @author HeroW
 * @date Jan 30, 2013 11:53:00 AM
 * @version 
 */

public class OneToManyField extends FieldInfo{

	public OneToManyField(){}
	public OneToManyField(FieldInfo field) {
		super(field);
		// TODO Auto-generated constructor stub
	}
	private String foreignKey;
	private OneToMany annotation;
	
	public String getForeignKey() {
		return foreignKey;
	}
	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}
	
	public OneToMany getAnnotation() {
		return annotation;
	}
	public void setAnnotation(OneToMany annotation) {
		this.annotation = annotation;
	}

}
