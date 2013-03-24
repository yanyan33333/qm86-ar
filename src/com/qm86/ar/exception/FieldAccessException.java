package com.qm86.ar.exception;

/**
 * @Title: FieldAccessException.java
 * @Package com.qm86.ar.exception
 * @Description: TODO
 * @author HeroW
 * @date Jan 30, 2013 3:10:24 PM
 * @version 
 */

public class FieldAccessException extends ActiveRecordException{

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 6163929718722965926L;
	
	public static final String ILLEGALARGUMENT = "类型不对!";
	public static final String ILLEGALACCESS = "不能访问!!";
	public static final String FIELDNOTFOUND = "找不到!!";

	public FieldAccessException(String msg) {
		super(msg);
	}
	public FieldAccessException(String msg, Throwable root){
		super(msg, root);
	}
	
	public FieldAccessException(Throwable root){
		super(root);
	}
/*	
	public FieldAccessException(String table, String field, String ExType, Throwable root){		
		String msg = "表 [" + table + "] 中属性 [ " + field + " ]!!" + ExType;
		FieldAccessException(msg, root);
		
	}*/

}
