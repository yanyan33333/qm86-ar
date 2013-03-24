package com.qm86.ar;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import oracle.jdbc.driver.OracleConnection;

import com.qm86.ar.dialect.Dialect;
import com.qm86.ar.dialect.OracleDialect;
import com.qm86.ar.exception.ActiveRecordException;
import com.qm86.ar.exception.TransactionExcetion;
import com.qm86.ar.tx.TransactionManager;

/**
 * @Title: DbKit.java
 * @Package com.qm86.ar
 * @Description: 数据库工具类,用于获取连接等
 * @author HeroW
 * @date Dec 11, 2012 2:11:50 PM
 * @version 
 */



public final class DbKit {

	
	private static DataSource dataSource;
	//线程局部变量
	private static final ThreadLocal<Connection> connections = new ThreadLocal<Connection>();
	
	private static Dialect dialect = new OracleDialect();
	
	private static int transactionLevel = Connection.TRANSACTION_READ_COMMITTED;
	
	//类保护
	private DbKit(){}


	
	public static final void removeThreadLocalConnection(){
		connections.remove();
	}
/*	
	public static final OracleConnection getOracleConnection() throws ActiveRecordException{
		OracleConnection oracleConn = (OracleConnection) connections.get();
		
		if(DbKit.getDialect().getDialectName().equals("oracle")){
			try {
				oracleConn = (OracleConnection)getDataSource().getConnection();
			} catch (SQLException e) {
				throw new ActiveRecordException("无法获取数据库连接~~" , e);
			}
			connections.set(oracleConn);			
		}else{
			throw new ActiveRecordException("你连接的不是Oracle数据库!");
		}
			return oracleConn;
	}
*/
	public static final Connection getConnection() throws ActiveRecordException {
		Connection conn = connections.get();
		if(null ==conn){
			try {						
					conn = getDataSource().getConnection();
					connections.set(conn);			
			} catch (SQLException e) {
				throw new ActiveRecordException("无法获取数据库连接~~" , e);
			}
		}
		return conn;
	
	}
	
	public static final boolean isExistsThreadLocalConnection(){
		return connections.get() != null;
	}
	
	public static final void close(Connection conn) throws ActiveRecordException{
		if(connections.get() == null)
			if(null != conn){
				try{
					conn.close();
				}catch(SQLException e){
					throw new ActiveRecordException(e);
				}
			}
	}
	
	
	public static synchronized TransactionManager getTransaction(){
		try {
			return new TransactionManager(getConnection());
		} catch (TransactionExcetion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ActiveRecordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static final void close(ResultSet rs , Statement stmt) throws ActiveRecordException{
		try{if(null != rs) rs.close();}catch(SQLException e){ throw new ActiveRecordException("ResultSet无法关闭! " , e);}
		try{if(null != stmt) stmt.close();}catch(SQLException e){ throw new ActiveRecordException("Statemet无法关闭! " , e);}	
	}
	
	
	public static final void close(Statement stmt , Connection conn) throws ActiveRecordException{
		if(stmt != null){
			try{
				stmt.close();
			}catch(SQLException e){
				throw new ActiveRecordException(e);
			}			
			close(conn);
		}
	}
	
	public static void fillStatement(PreparedStatement pstmt, List<Object> paras) throws ActiveRecordException {
		try {
			if(null == paras) return;
			for (int i = 0; i <  paras.size(); i++) {

				Object o = paras.get(i);
				//若要插入的是util.Date类型
				if(null != o&&o.getClass().getCanonicalName().equals("java.util.Date")){
					pstmt.setTimestamp(i+1, new Timestamp((((java.util.Date)o)).getTime()));
				}else{
					pstmt.setObject(i + 1, o);

				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new ActiveRecordException("无法设置PerparedStatemet参数!" , e);
		}
	}
	
	public static void fillSatement(PreparedStatement pstmt , Object... paras) throws ActiveRecordException{
		try{
			for(int i = 0 ; i <= paras.length ; i ++ ){
				pstmt.setObject(i + 1, paras[i]);
			}
		}catch(SQLException e){
			throw new ActiveRecordException("无法设置PerparedStatemet参数!" , e);
		}
	}

	public static final DataSource getDataSource() {
		return dataSource;
	}



	public static final void setDataSource(DataSource dataSource) {
		DbKit.dataSource = dataSource;
	}


	public static final void setThreadLocalConnection(Connection conn){
		connections.set(conn);
	}

	public static final ThreadLocal<Connection> getConnections() {
		return connections;
	}



	public static Dialect getDialect() {
		return dialect;
	}



	public static void setDialect(Dialect dialect) {
		DbKit.dialect = dialect;
	}



	public static final int getTransactionLevel() {
		return transactionLevel;
	}



	public static final void setTransactionLevel(int transactionLevel) {
		DbKit.transactionLevel = transactionLevel;
	}
}
