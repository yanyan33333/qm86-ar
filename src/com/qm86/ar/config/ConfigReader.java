package com.qm86.ar.config;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.qm86.ar.IDataSourceProvider;
import com.qm86.ar.exception.ActiveRecordException;

/**
 * @Title: ConfigReader.java
 * @Package com.qm86.ar.config
 * @Description: TODO
 * @author HeroW
 * @date Dec 10, 2012 9:14:16 PM
 * @version 
 */

public class ConfigReader implements IDataSourceProvider{

	private String jdbcUrl;
	private String username;
	private String password;
	private String driverClass;
	private String connectionPool = "C3P0";
	private int maxPoolSize = 50;
	private int minPoolSize = 10;
	private int initialPoolSize = 10;
	private int maxIdleTime = 20;
	private int acquireIncrement = 2;
	private String database;
	
	private DataSource dataSource;
	
	public int toInt(String arg) throws NumberFormatException{
		return Integer.parseInt(arg);
	}
	
	public void init() throws ActiveRecordException{
		Properties prop = new Properties();
		try {
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			throw new ActiveRecordException("找不到配置文件!!" , e);
		}
		//InputStream resource = getClass().getClassLoader().getResourceAsStream("config.properties");
		/*
		if(null == resource){
			//resource = getClass().getClassLoader().getResourceAsStream("/config.properties");
			if (null == resource) {
				throw new ActiveRecordException("找不到配置文件!!" , e);
			}
		}
		*/
		try{
			this.database = prop.getProperty("database");
			jdbcUrl = prop.getProperty("jdbcUrl");
			username = prop.getProperty("username");
			password = prop.getProperty("password");
			driverClass = prop.getProperty("driverClass");
			connectionPool = prop.getProperty("connectionPool");
			maxPoolSize = toInt(prop.getProperty("maxPoolSize"));
			minPoolSize = toInt(prop.getProperty("minPoolSize"));
			initialPoolSize = toInt(prop.getProperty("initialPoolSize"));
			maxIdleTime = toInt(prop.getProperty("maxIdleTime"));
			//acquireIncrement = toInt(prop.getProperty("acqurieIncrement"));
			
			Pattern pattern = Pattern.compile("[Cc]3[Pp]0");
			Matcher matcher = pattern.matcher(connectionPool);
			if(matcher.matches()){
			/*	
			    // C3P0 连接池
				ComboPooledDataSource ds = new ComboPooledDataSource();
				ds.setJdbcUrl(jdbcUrl);
				ds.setUser(username);
				ds.setPassword(password);
				ds.setDriverClass(driverClass);
				ds.setMaxPoolSize(maxPoolSize);
				ds.setMinPoolSize(minPoolSize);
				ds.setInitialPoolSize(initialPoolSize);
				ds.setMaxIdleTime(maxIdleTime);
				
				ds.setMaxStatements(0);
			
				this.dataSource = ds;
				*/
				throw new ActiveRecordException("找不到C3P0连接池~~~");
			}else{
				//Droid连接池
				DruidDataSource ds = new DruidDataSource();
				ds.setUrl(jdbcUrl);
				ds.setUsername(username);
				ds.setPassword(password);
				ds.setDriverClassName(driverClass);
				ds.setMaxActive(maxPoolSize);
				ds.setInitialSize(initialPoolSize);
				ds.setMaxIdle(maxIdleTime);
				//若为oracle
				if(this.database.toLowerCase().equals("oracle")){ds.setPoolPreparedStatements(true);}
				else{ds.setPoolPreparedStatements(false);}
				
				this.dataSource = ds;

				//到时候再自己写一个连接池!!!
				//throw new ActiveRecordException("找不到连接池~~~");
			}
		} catch (NumberFormatException e) {
			String msg = "配置文件中可能一些值的类型错了~~";
			throw new ActiveRecordException(msg ,e);
			/*
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			String msg = "找不到 driverClass [ " + this.driverClass + " ]";
			throw new ActiveRecordException(msg , e);
			*/
		}
		
		
	}
	@Override
	public DataSource getDataSource() {
		// TODO Auto-generated method stub
		return this.dataSource;
	}
}
