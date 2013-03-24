package com.qm86.ar;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import com.qm86.ar.exception.ActiveRecordException;
import com.qm86.ar.orm.ColumnField;
import com.qm86.ar.orm.IdField;
import com.qm86.ar.orm.OrmInfo;
//import com.sun.xml.internal.ws.org.objectweb.asm.Type;

/**
 * @Title: DaoSupport.java
 * @Package com.qm86.ar
 * @Description: TODO
 * @author HeroW
 * @date Dec 13, 2012 8:58:51 PM
 * @version
 */

public class DaoSupport {

	public static Logger logger = Logger.getLogger(DaoSupport.class);
	private Connection conn;
	//刚插入的主键id
	private int keyId = -1;

	public DaoSupport(Connection conn) {
		this.conn = conn;
	}

	/**
	 * @author: HeroW
	 * @Title: execute
	 * @date Dec 16, 2012 4:52:19 PM
	 * @Description: 执行Sql语句
	 * @param sql
	 *            sql语句
	 * @param args
	 *            所要执行的值
	 * @return 执行成功的行id,若执行不成功返回0
	 * @throws ActiveRecordException
	 * @returnType int
	 * @throws
	 */
	public int execute(String sql, List args) throws ActiveRecordException {
		int update = 0;
		
		logger.info(" sql = " + sql);
		PreparedStatement pstmt = null;
		try {
			pstmt = this.conn.prepareStatement(sql);
			if (DbKit.getDialect().getDialectName().equals("oracle")) { //oracle数据库
				
				//如果是带有returing的insert或者update语句,可以取得刚插入sequence的值
				if((sql.startsWith("insert") || sql.startsWith("update")) && (sql.contains("returning") && sql.endsWith("?"))){
					
					
					OraclePreparedStatement oraclestmt = pstmt.unwrap(OraclePreparedStatement.class);
					//error 在用连接池的情况下 OraclePreparedStatement不能转换为连接池中的PreparedStatement
					//OraclePreparedStatement ppst = (OraclePreparedStatement)this.conn.prepareStatement(sql);
					
					//CallableStatement cstmt = (CallableStatement) this.conn.prepareCall(sql);
					//cstmt.registerOutParameter(args.size() + 1, Type.INT);
					
					// 返回行id,返回值是第args.size() + 1个问号 insert into tb values(seq_.nextval, ?, ?, ?, ?)return id into ?
					oraclestmt.registerReturnParameter(args.size() + 1, OracleTypes.INTEGER);					
					DbKit.fillStatement(oraclestmt, args);
					oraclestmt.executeUpdate();
					ResultSet rs = oraclestmt.getReturnResultSet();
					//ResultSet rs = ppst.getGeneratedKeys();
					rs.next();
					this.keyId = rs.getInt(1);
					//this.keyId = cstmt.getInt(args.size() + 1);
					
					
				}else{
					DbKit.fillStatement(pstmt, args);
					update = pstmt.executeUpdate();
					
				}				
			} else {
				PreparedStatement ppst = this.conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				DbKit.fillStatement(ppst, args);
				update = ppst.executeUpdate();
				ResultSet rs = ppst.getGeneratedKeys();
				rs.next();
				this.keyId = rs.getInt(1);
			}

			/*
			 * long t1 = System.currentTimeMillis();
			 * 
			 * long t2 = System.currentTimeMillis(); logger.info("Insert时间 = " +
			 * (t2-t1) + " ms")
			 */
		} catch (SQLException e) {
			DbKit.close(pstmt, this.conn);
			throw new ActiveRecordException("执行sql语句不成功!", e);
		}
		DbKit.close(pstmt, this.conn);
		return update;

	}
	
	
	public int getKeyId(){
		return this.keyId;
	}

	/**
	 * @author: HeroW
	 * @Title: insert
	 * @date Dec 28, 2012 11:57:44 AM
	 * @Description: TODO
	 * @return
	 * @throws ActiveRecordException
	 * @returnType int
	 * @throws
	 */
	public int insert(OrmInfo orm) throws ActiveRecordException {
		
		List args = new ArrayList();

		//创造带有数据库言语的sql语句
		String sql = DbKit.getDialect().saveToTable(orm, args);

		return execute(sql, args);

	}

	public <E> int update(E obj) throws ActiveRecordException {
		Class<?> c = obj.getClass();
		OrmInfo orm = OrmInfo.getOrmInfo(c);

		List args = new ArrayList();
		

		String sql = DbKit.getDialect().updateToTable(orm, args);

		return execute(sql, args);
	}

	public <E> int deleteById(E obj) throws ActiveRecordException {
		Class<?> c = obj.getClass();
		OrmInfo orm = OrmInfo.getOrmInfo(c);

		List<Object> args = new ArrayList<Object>();
		
		String sql = DbKit.getDialect().deleteById(orm, args);
		return execute(sql, args);
	}

	/**
	 * @author: HeroW
	 * @Title: excLoginByProcedure
	 * @date Jan 5, 2013 7:30:27 PM
	 * @Description: 调用储存过程 LOGIN_PROC(usernme, password)
	 * @param sql
	 * @param username
	 * @param pw
	 * @return 返回用户ID 角色名字 角色ID
	 * @throws ActiveRecordException
	 * @returnType Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> excLoginByProcedure(String username, String pw)
			throws ActiveRecordException {
		CallableStatement proc = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			proc = conn.prepareCall("{ call LOGIN_PROC(? , ?, ? ,? ,?) }");
			proc.setString(1, username);
			proc.setString(2, pw);
			proc.registerOutParameter(3, Types.INTEGER);
			proc.registerOutParameter(4, Types.VARCHAR);
			proc.registerOutParameter(5, Types.INTEGER);
			proc.execute();
			result.put("UserId", proc.getInt(3));
			result.put("RoleName", proc.getString(4));
			result.put("RoleId", proc.getString(5));

		} catch (SQLException e) {
			throw new ActiveRecordException(
					"处理存储过程 [ { call LOGIN_PROC  }]  出错", e);
		}
		DbKit.close(conn);
		return result;

	}

	public List<Map<String, Object>> select(String sql, List<Object> args, int limit,
			int offset) throws ActiveRecordException {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		String sql_ = DbKit.getDialect().forPaginate(sql, limit, offset);

		logger.info(" sql = " + sql_);

		PreparedStatement ppst = null;
		ResultSet rs = null;
		try {
			long t1 = System.currentTimeMillis();

			ppst = this.conn.prepareStatement(sql_);
			DbKit.fillStatement(ppst, args);

			rs = ppst.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				Map<String, Object> item = new HashMap<String, Object>();
				// 我了个去....ResultSetMetaData居然是从1开始计数的....
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String name = rsmd.getColumnName(i).toLowerCase();
					Object o = rs.getObject(i);
					item.put(name, o);
				}
				data.add(item);
			}
		} catch (SQLException e) {
			throw new ActiveRecordException(e);
		}
		// finally{
		DbKit.close(rs, ppst);
		return data;
		// }
	}

	public <E> List<E> select(Class<E> clasz, String sql, List args, int limit,
			int offset) throws ActiveRecordException {
		
		List<E> result = new ArrayList<E>();
		List<Map<String, Object>> data = select(sql, args, limit, offset);
		for (Map<String, Object> item : data) {
			E obj;
			try {
				obj = clasz.newInstance(); // 实例化
			} catch (InstantiationException e) {
				String msg = "find 中 " + clasz.getName() + " 无法实例化!!";
				throw new ActiveRecordException(msg, e);
			} catch (IllegalAccessException e) {
				String msg = "find 中" + clasz.getName() + "无法得到!!";
				throw new ActiveRecordException(msg, e);
			}
			OrmInfo orm = OrmInfo.getOrmInfo(obj);
			
			for (IdField id : orm.getIdFields().values()) {
				String name = id.getName();
				Object value = item.get(name);
				try {
					value = ConvertUtil.castFromObject(value, id.getType());
				} catch (ParseException e) {
					String msg = "field[ " + name + " ] 类型转换出错";
					throw new ActiveRecordException(msg, e);
				}
				orm.setFieldValue(id.getField(), obj, value);
			}
			for (ColumnField col : orm.getColumnFields().values()) {
				String name = col.getName();
				Object value = item.get(name);
				try {
					value = ConvertUtil.castFromObject(value, col.getType());
				} catch (ParseException e) {
					String msg = "field[ " + name + " ] 类型转换出错";
					throw new ActiveRecordException(msg, e);
				}
				if (null != value) {
					orm.setFieldValue(col.getField(), obj, value);
				}
			}
			result.add(obj);
		}
		if (0 == result.size()) {
			return null;
		} else {
			return result;
		}
	}
}
