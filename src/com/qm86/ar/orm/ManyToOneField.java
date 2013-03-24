package com.qm86.ar.orm;

import java.lang.reflect.Field;

import com.qm86.ar.annotation.ManyToOne;

/**
 * @Title: ManyToOneField.java
 * @Package com.qm86.ar.orm
 * @Description: TODO
 * @author HeroW
 * @date Jan 30, 2013 11:49:58 AM
 * @version 
 */

public class ManyToOneField extends FieldInfo{
	
	public ManyToOneField(){}
	public ManyToOneField(FieldInfo field) {
		super(field);
		// TODO Auto-generated constructor stub
	}
	private String foreignKey;
	
	private ManyToOne annotation;
	
	public String getForeignKey() {
		return foreignKey;
	}
	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}

	public ManyToOne getAnnotation() {
		return annotation;
	}
	public void setAnnotation(ManyToOne annotation) {
		this.annotation = annotation;
	}


}
