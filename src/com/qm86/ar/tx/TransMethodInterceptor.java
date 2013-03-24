package com.qm86.ar.tx;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.log4j.Logger;

import com.qm86.ar.DbKit;
import com.qm86.ar.annotation.Transaction;
import com.qm86.ar.exception.ActiveRecordException;
import com.qm86.ar.exception.TransactionExcetion;

/**
 * @Title: TransMethodInterceptor.java
 * @Package com.qm86.ar.tx
 * @Description: TODO
 * @author HeroW
 * @date Dec 11, 2012 10:55:16 PM
 * @version
 */

public class TransMethodInterceptor implements MethodInterceptor {

	public static Logger logger = Logger.getLogger(TransMethodInterceptor.class);
	
	

	@Override
	public Object intercept(Object obj, Method method, Object[] args , MethodProxy proxy)
			throws Throwable {
		Object result = null;
		TransactionManager manager = null;
		//取得注释
		Transaction myAnnotation =method.getAnnotation(Transaction.class);	
		try{
			if (null != myAnnotation) {
				manager =TransactionManager.getTransactionManager();
				manager.begainTransaction();
			}
			
			logger.info("before~~^_^");
			result = proxy.invokeSuper(obj, args);		
			logger.info("after~~^_^");
			
			if(null != myAnnotation){
				manager.commitTransaction();
			}
		}catch(Exception e){
			String msg = "rollback!";
			if(null != myAnnotation){
				manager.rollbackTransaction();
			}			
			logger.error(msg, e);
			throw new ActiveRecordException(msg, e);
		}
			
		return result;
	}

	
}
