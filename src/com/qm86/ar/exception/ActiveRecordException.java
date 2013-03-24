package com.qm86.ar.exception;


/**
 * @Title: ActiveRecordException.java
 * @Package com.qm86.ar.exception
 * @Description: TODO
 * @author HeroW
 * @date Dec 10, 2012 2:39:36 PM
 * @version 
 */

public class ActiveRecordException extends Exception{

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -3023927422517586277L;
	
	public ActiveRecordException(){
		
	}

	public ActiveRecordException(String msg){
		super(msg);
	}
	
	public ActiveRecordException(String msg, Throwable root){
		super(msg ,root);
	}
	
	public ActiveRecordException(Throwable root){
		super(root);
	}
}
