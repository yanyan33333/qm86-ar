package com.qm86.ar.dialect;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.qm86.ar.exception.ActiveRecordException;
import com.qm86.ar.orm.FieldInfo;
import com.qm86.ar.orm.IdField;
import com.qm86.ar.orm.OrmInfo;

/**
 * @Title: Dialect.java
 * @Package com.qm86.ar.adapter
 * @Description: TODO
 * @author HeroW
 * @date Dec 10, 2012 2:50:27 PM
 * @version
 */

public abstract class Dialect {

	public static Logger logger = Logger.getLogger(Dialect.class);

	public abstract String getDialectName();

	/**
	 * @author: HeroW
	 * @Title: forPaginate
	 * @date Dec 15, 2012 2:30:06 PM
	 * @Description: 生成分页
	 * @param sql
	 * @param limit
	 * @param offset
	 * @return
	 * @returnType String
	 * @throws
	 */
	public abstract String forPaginate(String sql, int limit, int offset);

	/**
	 * @author: HeroW
	 * @Title: saveToTable
	 * @date Dec 15, 2012 4:18:53 PM
	 * @Description: 复合主键的值写在mapping中的前几个
	 * @param orm
	 *            表的信息
	 * @param args
	 *            对应 的值
	 * @return 生成保存的SQL语句
	 * @returnType String
	 * @throws
	 */
	public abstract String saveToTable(OrmInfo orm, List args);
			

	/**
	 * @throws ActiveRecordException 
	 * @author: HeroW
	 * @Title: updateToTable
	 * @date Dec 16, 2012 7:50:44 PM
	 * @Description: 更新表
	 * @param orm
	 *            ormInfo
	 * @param colMapping
	 *            非主键列Map
	 * @param idMapping
	 *            主键列Map
	 * @param args
	 *            要更新的值
	 * @return sql语句
	 * @returnType String
	 * @throws
	 */
	public abstract String updateToTable(OrmInfo orm,  List args) ;
	

	/**
	 * @throws ActiveRecordException 
	 * @author: HeroW
	 * @Title: deleteById
	 * @date Dec 28, 2012 12:56:51 PM
	 * @Description: 按Id删除
	 * @param orm
	 * @param idMap
	 *            主键列Map
	 * @param args
	 *            要删除的值
	 * @return
	 * @returnType String
	 * @throws
	 */
	public String deleteById(OrmInfo orm,  List<Object> args) throws ActiveRecordException {
		//delete from "tableName" where "id" = ?
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ");
		sql.append(orm.getTableName());
		sql.append(" where ");
		int idNum = 1;
		for (Entry<String, IdField> e : orm.getIdFields().entrySet()) {
			String key = (String) e.getKey();
			if (orm.hasIdField(key)) {
				sql.append(key).append(" = ? ");
				args.add(e.getValue());
				if (idNum < orm.getIdFields().size()) {
					sql.append(" , ");
					idNum++;
				}
			}
		}
		return sql.toString();
	}
	/*
	 * public int count(){
	 * 
	 * } public int max(){
	 * 
	 * } public int min(){
	 * 
	 * }
	 */
}
