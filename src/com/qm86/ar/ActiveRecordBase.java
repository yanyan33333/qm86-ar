package com.qm86.ar;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.qm86.ar.annotation.Table;
import com.qm86.ar.config.ConfigReader;
import com.qm86.ar.exception.ActiveRecordException;
import com.qm86.ar.orm.OneToManyField;
import com.qm86.ar.orm.OneToOneField;
import com.qm86.ar.orm.OrmInfo;
import com.qm86.ar.tx.TransactionManager;

/**
 * @Title: ActiveRecordBase.java
 * @Package com.qm86.ar
 * @Description: TODO
 * @author HeroW
 * @date Dec 11, 2012 2:29:24 PM
 * @version 
 */

public class ActiveRecordBase {
	
	//刚插入的主键ID
	private int keyId = -1;

	//载入配置信息
	static {
		ConfigReader config = new ConfigReader();
		try {
			config.init();
			// 加载连接
			DbKit.setDataSource(config.getDataSource());
		} catch (ActiveRecordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author: HeroW
	 * @Title: findBySql
	 * @date Dec 28, 2012 11:23:36 AM
	 * @Description: 根据Sql查询
	 * @param clasz
	 * @param sql
	 * @param args
	 * @param order
	 * @param limit
	 * @param offset
	 * @return
	 * @throws ActiveRecordException 
	 * @returnType List<E>   
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public static  <E> List<E> findBySql(Class<E> clasz , String sql , Object[] args , String order , int limit , int offset) throws ActiveRecordException{
		List<Object> para = null;
		if(null != args){
			para =Arrays.asList(args);
		}else{
		//	return null;
		}
		 
		if(null != order && !order.equals("")){
			sql = sql + " order by " + order ;
		}
		Connection conn = DbKit.getConnection();
		DaoSupport dao = new DaoSupport(conn);
		return (List<E>) dao.select(clasz, sql, para, limit, offset);
		
	}
	
	public static List<Map<String , Object>> findBySql (String sql , Object[] args , String order) throws ActiveRecordException {
		return findBySql(sql, args, order,  0);
		
	}
	
	public static List<Map<String , Object>> findBySql (String sql , Object[] args , String order , int limit) throws ActiveRecordException {
		return findBySql(sql, args, order, limit, 0);
		
	}
	public static List<Map<String , Object>> findBySql (String sql , Object[] args , String order , int limit , int offset) 
			throws ActiveRecordException{
		List<Object> para = null;
		if(null != args){
			para =Arrays.asList(args);
		}else{
			return null;
		}
		if(null != order && !order.equals("")){
			sql = sql + " order by " + order;
		}
		Connection conn = DbKit.getConnection();
		DaoSupport dao = new DaoSupport(conn);
		
		return dao.select(sql, para, limit, offset);
	}
	
	/**
	 * @author: HeroW
	 * @Title: findBySql
	 * @date Dec 28, 2012 11:29:01 AM
	 * @Description: 查询limit行数据 
	 * @param clasz
	 * @param sql
	 * @param args
	 * @param order
	 * @param limit
	 * @return
	 * @throws ActiveRecordException 
	 * @returnType List<E>   
	 * @throws
	 */
	public static  <E> List<E> findBySql(Class<E> clasz  , String sql , Object[] args , String order , int limit) throws ActiveRecordException{
		return findBySql(clasz, sql, args, order, limit, 0);
	}
	
	/**
	 * @author: HeroW
	 * @Title: findBySql
	 * @date Dec 28, 2012 11:39:55 AM
	 * @Description: 按sql查询
	 * @param clasz
	 * @param sql
	 * @param args
	 * @param order
	 * @return
	 * @throws ActiveRecordException 
	 * @returnType List<E>   
	 * @throws
	 */
	public static  <E> List<E> findBySql(Class<E> clasz , String sql , Object[] args , String order) throws ActiveRecordException{
		return findBySql(clasz, sql, args, order, 0);
	}
	
	/**
	 * @author: HeroW
	 * @Title: findBySql
	 * @date Dec 28, 2012 11:41:15 AM
	 * @Description: TODO
	 * @param clasz
	 * @param sql
	 * @param args
	 * @return
	 * @throws ActiveRecordException 
	 * @returnType List<E>   
	 * @throws
	 */
	public static  <E> List<E> findBySql(Class<E> clasz , String sql , Object[] args ) throws ActiveRecordException{
		return findBySql(clasz, sql, args, null);
	}
	public static <E> List<E> findAll(Class<E> clasz) throws ActiveRecordException{
		return findAll(clasz, null, null);
	}
	/**
	 * @author: HeroW
	 * @Title: findAll
	 * @date Dec 28, 2012 11:48:58 AM
	 * @Description: 全表查询
	 * @param clasz
	 * @param conditions 查询的条件 sql:where + condition
	 * @param args
	 * @param order
	 * @param limit
	 * @param offset
	 * @return
	 * @throws ActiveRecordException 
	 * @returnType List<E>   
	 * @throws
	 */
	public static  <E> List<E> findAll(Class<E> clasz , String conditions , Object[] args , String order , int limit , int offset) throws ActiveRecordException{
		//OrmInfo orm = OrmInfo.getOrmInfo(clasz);
		
		String sql = "select * from " + clasz.getAnnotation(Table.class).name().toLowerCase();
		if(null != conditions && !conditions.equals("")){
			sql += " where " + conditions + " ";
		}
		return findBySql(clasz, sql, args, order, limit, offset);
	}
	
	/**
	 * @author: HeroW
	 * @Title: findAll
	 * @date Dec 28, 2012 11:52:16 AM
	 * @Description: TODO
	 * @param clasz
	 * @param conditions 查询的条件 sql:where + condition
	 * @param args
	 * @param order
	 * @param limit
	 * @return
	 * @throws ActiveRecordException 
	 * @returnType List<E>   
	 * @throws
	 */
	public static  <E> List<E> findAll(Class<E> clasz , String conditions , Object[] args , String order , int limit ) throws ActiveRecordException{
		return findAll(clasz, conditions, args, order, limit, 0);
	}
	
	/**
	 * @author: HeroW
	 * @Title: findAll
	 * @date Dec 28, 2012 11:53:01 AM
	 * @Description: TODO
	 * @param clasz
	 * @param conditions 查询的条件 sql:where + condition
	 * @param args
	 * @param order
	 * @return
	 * @throws ActiveRecordException 
	 * @returnType List<E>   
	 * @throws
	 */
	public static  <E> List<E> findAll(Class<E> clasz , String conditions , Object[] args , String order) throws ActiveRecordException{
		return findAll(clasz, conditions, args, order, 0);
	}
	
	/**
	 * @author: HeroW
	 * @Title: findAll
	 * @date Dec 28, 2012 11:54:11 AM
	 * @Description: TODO
	 * @param clasz
	 * @param conditions 查询的条件 sql:where + condition
	 * @param args
	 * @return
	 * @throws ActiveRecordException 
	 * @returnType List<E>   
	 * @throws
	 */
	public static  <E> List<E> findAll(Class<E> clasz , String conditions , Object[] args) throws ActiveRecordException{
		return findAll(clasz, conditions, args, null);
	}
	
	/**
	 * @author: HeroW
	 * @Title: findFirst
	 * @date Dec 28, 2012 12:32:12 PM
	 * @Description: 查询第一个
	 * @param clasz
	 * @param conditions 查询的条件 sql:where + condition
	 * @param args
	 * @return
	 * @throws ActiveRecordException 
	 * @returnType E   
	 * @throws
	 */
	public static  <E> E findFirst(Class<E> clasz , String conditions , Object[] args) throws ActiveRecordException{
		return (E) findAll(clasz, conditions, args).get(0);
	}
	/**
	 * @author: HeroW
	 * @Title: findById
	 * @date Dec 28, 2012 12:32:37 PM
	 * @Description: 按Id查询
	 * @param clasz
	 * @param id Id值
	 * @return
	 * @throws ActiveRecordException 
	 * @returnType E   
	 * @throws
	 */
	public static  <E> E findById(Class<E> clasz , Object id) throws ActiveRecordException{
				
		Object[] args = new Object[]{id};
		OrmInfo orm = OrmInfo.getOrmInfo(clasz);
		List<String> is = (List<String>) orm.getIdFields().keySet();
		String idName = is.get(0);
		List<E> result = findAll(clasz, idName + " = ? ", args);
		if(null == result){
			return null;
		}else{
			return result.get(0);
		}
	}
	
	
	/**
	 * @throws ActiveRecordException 
	 * @author: HeroW
	 * @Title: insert
	 * @date Dec 28, 2012 12:46:18 PM
	 * @Description: 插入数据
	 * @return
	 * @throws ActiveRecordException 
	 * @returnType int   
	 * @throws
	 */
	public int insert() throws ActiveRecordException {
		Connection conn = DbKit.getConnection();
		TransactionManager tx = new TransactionManager(conn);
		//开启事务
		tx.begainTransaction();
		DaoSupport dao = new DaoSupport(conn);
		OrmInfo orm;
		int update = 0;
		try {
			orm = OrmInfo.getOrmInfo(this);
			update  = dao.insert(orm);
			//取得刚插入行的id
			this.keyId = dao.getKeyId();
			if(/*0 != update &&*/ -1 != getKeyId()){
				//处理一对多关系的插入
				for (Entry<String, OneToManyField> entry : orm.getOTMFields()	.entrySet()) {
					//String fieldName = entry.getKey();
					List<?> values = (List<?>) orm.getFieldValue(entry.getValue().getField(), this);
					if(null != values){
						for (Object obj : values) {							
							ActiveRecordBase ar = (ActiveRecordBase) obj;
							Field f = obj.getClass().getDeclaredField(entry.getValue().getForeignKey());
							orm.setFieldValue(f, obj, getKeyId());
							ar.insert();
						}
					}
				}
				//处理一对一关系的插入
				for(Entry<String, OneToOneField> entry : orm.getOTOFields().entrySet()){
					//String fieldName = entry.getKey();
					Object obj = orm.getFieldValue(entry.getValue().getField(), this);
					if(null != obj){
						Field f= obj.getClass().getDeclaredField(entry.getValue().getForeignKey());
						orm.setFieldValue(f, obj, getKeyId());
						ActiveRecordBase ar = (ActiveRecordBase) obj;
						ar.insert();
					}
					
				}
			}
			
		} catch (Exception e) {
			tx.rollbackTransaction();
			throw new ActiveRecordException(e);
		}

		tx.commitTransaction();
		return update; 		
	}
	
	/**
	 * @author: HeroW
	 * @Title: update
	 * @date Dec 28, 2012 12:47:19 PM
	 * @Description: 更新数据
	 * @return
	 * @throws ActiveRecordException 
	 * @returnType int   
	 * @throws
	 */
	public  int update() throws ActiveRecordException{
		Connection conn = DbKit.getConnection();
		DaoSupport dao = new DaoSupport(conn);
		int update = dao.update(this);
		this.keyId = dao.getKeyId();
		return update;
	}
	
	/**
	 * @author: HeroW
	 * @Title: deleteById
	 * @date Dec 28, 2012 1:05:58 PM
	 * @Description: 按Id删除数据
	 * @return
	 * @throws ActiveRecordException 
	 * @returnType int   
	 * @throws
	 */
	public  int deleteById() throws ActiveRecordException{
		Connection conn = DbKit.getConnection();
		DaoSupport dao = new DaoSupport(conn);
		return dao.deleteById(this);
	}
	
	/**
	 * @author: HeroW
	 * @Title: getKeyId
	 * @date Jan 26, 2013 4:37:20 PM
	 * @Description: 取得最近一次插入的主键ID ,若没有插入过数据返回-1
	 * @return 
	 * @returnType int   
	 * @throws
	 */
	public int getKeyId(){
		return this.keyId;
	}
}
