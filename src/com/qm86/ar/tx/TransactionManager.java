package com.qm86.ar.tx;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.qm86.ar.DbKit;
import com.qm86.ar.exception.ActiveRecordException;
import com.qm86.ar.exception.TransactionExcetion;

/**
 * @Title: TransactionManager.java
 * @Package com.qm86.ar.tx
 * @Description: TODO
 * @author HeroW
 * @date Dec 11, 2012 3:11:20 PM
 * @version 
 */

public class TransactionManager {
	Logger logger = Logger.getLogger(TransactionManager.class);
	
	private Connection conn = null;
	
	public static TransactionManager getTransactionManager() throws ActiveRecordException{
		return new TransactionManager(DbKit.getConnection());
	}
	public TransactionManager(Connection conn) throws TransactionExcetion{
		this.conn = conn;
		
	}
	public void begainTransaction() throws TransactionExcetion{
		try{
			
			this.conn.setTransactionIsolation(DbKit.getTransactionLevel());
			this.conn.setAutoCommit(false);
			logger.info("事务开启!");
			}catch(SQLException e){
				String msg = "事务开启错误~~"; 
				logger.error(msg, e);
			throw new TransactionExcetion(msg , e);
		}
	}
	
	public void commitTransaction() throws ActiveRecordException{
		try{			
			
				this.conn.commit();
				DbKit.close(this.conn);
			logger.info("事务提交!");
		}catch(SQLException e){
			String msg = "事务提交时出错!!"; 
			logger.error(msg, e);
			throw new TransactionExcetion( msg, e);
		}
	}
	
	public void rollbackTransaction() throws ActiveRecordException{
		try{
		
			
				this.conn.rollback();
				DbKit.close(this.conn);
				logger.info("事务回滚!");
				
		}catch(SQLException e){
			String msg = "事务回滚时出错!!"; 
			logger.error(msg, e);
			throw new TransactionExcetion(msg , e);
		}
	}
	/*
	public boolean isFinished(){
	
	}
	*/
	public Connection getConnection() {
		return this.conn;
	}

}
