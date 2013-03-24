package com.qm86.ar.orm;

import java.lang.reflect.Field;

import com.qm86.ar.annotation.OneToOne;

/**
 * @Title: OneToOneField.java
 * @Package com.qm86.ar.orm
 * @Description: TODO
 * @author HeroW
 * @date Jan 30, 2013 11:56:07 AM
 * @version 
 */

public class OneToOneField extends FieldInfo{
	

	public OneToOneField(){}
	public OneToOneField(FieldInfo field) {
		super(field);
		// TODO Auto-generated constructor stub
	}
	private String foreignKey;
	private OneToOne annotation;
	
	public String getForeignKey() {
		return foreignKey;
	}
	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}

	public OneToOne getAnnotation() {
		return annotation;
	}
	public void setAnnotation(OneToOne annotation) {
		this.annotation = annotation;
	}

}
