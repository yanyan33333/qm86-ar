package com.qm86.ar.orm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.qm86.ar.annotation.Column;

/**
 * @Title: FieldInfo.java
 * @Package com.qm86.ar.orm
 * @Description: TODO
 * @author HeroW
 * @date Jan 30, 2013 2:40:22 PM
 * @version 
 */

public class FieldInfo {

	//列元素的属性
	private Field field;
	//列元素的值
	private Object value = null;
	//列元素的名字
	private String name;
	//列元素的类型
	private Class<?> type;
	
	public FieldInfo(){}
	public FieldInfo(FieldInfo field){
		this.field = field.field;
		this.name = field.name;
		this.value = field.value;
		this.type = field.type;
	}
	
	
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
