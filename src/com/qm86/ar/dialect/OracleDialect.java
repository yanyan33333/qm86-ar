package com.qm86.ar.dialect;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.qm86.ar.exception.ActiveRecordException;
import com.qm86.ar.orm.ColumnField;
import com.qm86.ar.orm.FieldInfo;
import com.qm86.ar.orm.IdField;
import com.qm86.ar.orm.OrmInfo;

/**
 * @Title: OracleAdapter.java
 * @Package com.qm86.ar.adapter
 * @Description: TODO
 * @author HeroW
 * @date Dec 10, 2012 2:53:00 PM
 * @version 
 */

public class OracleDialect extends Dialect{

	public static Logger logger = Logger.getLogger(OracleDialect.class);
	
	public String getDialectName(){
		return "oracle";
	}
	
	//select * from ( select tb_.*  rownum rownum_ from (sql) tb_ where rownum >= limit +offset ) where rownum_ >=offset
	public String forPaginate(String sql , int limit , int offset){
		StringBuffer pageSql = new StringBuffer(sql.length() + 100);
		if(0 == offset){
			pageSql.append("select * from ( ");
			pageSql.append(sql);
			pageSql.append(" ) where rownum >= ").append(limit);
		}else{
			pageSql.append("select * from ( ");
			pageSql.append("select tb_.* rownum rownum_  from (");
			pageSql.append(sql);
			pageSql.append(" ) tb_ where rownum <= ").append(limit + offset);
			pageSql.append(" ) where rownum_ >= ").append(offset);
		}
		return pageSql.toString();
				
	}	
	
	
	//insert into tableName (id , col ) value( tableName_seq , ? ) returning id into ?
	//returning id into ? 是用来返回执行SQL语句成功的行ID
	public String saveToTable(OrmInfo orm, List args) {
		StringBuffer sql = new StringBuffer();
		StringBuffer var = new StringBuffer();
		sql.append("insert into ").append(orm.getTableName()).append(" ( ");
		//sql.append("begin ").append("insert into ").append(orm.getTableName()).append(" ( ");
		int colCount = 1;
		String idName = null;
		Map<String, ColumnField> colMap = orm.getColumnFields();
		for(String key : orm.getIdFields().keySet()){ // 可能表有复合主键
			idName  = key;
			if(orm.hasIdField(idName)){
			sql.append(idName).append(" ,  ");
			}
		}
		for(String key : colMap.keySet()){
			String colName = key;
			if(orm.hasColumnField(colName)){
				
				sql.append(colName);
				args.add(colMap.get(colName).getValue());
				var.append(" ? ");
				
				if(colCount < colMap.size()){
				sql.append(" , ");
				var.append(" , ");
				}
			}			
			colCount++;			
		}
		sql.append(" ) ").append("values( ").append(orm.getTableName() + "_seq.nextval , ");
		sql.append(var + " )");
	if(idName != null){sql.append(" returning ").append(idName).append(" into ?")/*.append("; end;")*/;}
		
		
		return sql.toString();
	}
	
	
	// update tableName set col1 = ? , col2 = ? , col3 = ? where id = ? returning id into ?
	public String updateToTable(OrmInfo orm, List args)  {		
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(orm.getTableName()).append(" set ");
		Map<String, ColumnField> colMapping = orm.getColumnFields();
		Map<String, IdField> idMapping = orm.getIdFields();
		int colNum = 1;
		for (String key : colMapping.keySet()) {
			String colName = key;
			if (orm.hasColumnField(colName)) {
				sql.append(colName).append(" = ? ");
				args.add(colMapping.get(key).getValue());
				if (colNum < colMapping.size()) {
					sql.append(" , ");
					colNum++;
				}
			}
		}
		sql.append(" where ");
		int idNum = 1;
		String idName = null;
		for (String key : idMapping.keySet()) {
			idName = key;
			if (orm.hasIdField(idName)) {
				sql.append(idName + " = ?");
				args.add(idMapping.get(key).getValue());
				if (idNum < idMapping.size()) {
					sql.append(" and ");
					idNum++;
				}
			}			
		}
		sql.append(" returning ").append(idName).append(" into ?");
		return sql.toString();
	}




	
	
}
