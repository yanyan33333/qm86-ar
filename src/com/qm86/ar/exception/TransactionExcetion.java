package com.qm86.ar.exception;

/**
 * @Title: TransactionExcetion.java
 * @Package com.qm86.ar.exception
 * @Description: TODO
 * @author HeroW
 * @date Dec 10, 2012 2:38:58 PM
 * @version 
 */

public class TransactionExcetion extends ActiveRecordException{

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -4462251609463841035L;

	public TransactionExcetion(String msg){
		super(msg);
	}
	
	public TransactionExcetion(String msg , Throwable root){
		super(msg , root);
	}
	
	public TransactionExcetion(Throwable root){
		super(root);
	}
}
