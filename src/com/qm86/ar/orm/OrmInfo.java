package com.qm86.ar.orm;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qm86.ar.ActiveRecordBase;
import com.qm86.ar.annotation.Column;
import com.qm86.ar.annotation.Id;
import com.qm86.ar.annotation.ManyToOne;
import com.qm86.ar.annotation.OneToMany;
import com.qm86.ar.annotation.OneToOne;
import com.qm86.ar.annotation.Table;
import com.qm86.ar.exception.ActiveRecordException;
import com.qm86.ar.exception.FieldAccessException;

/**
 * @Title: OrmInfo.java
 * @Package com.qm86.ar.orm
 * @Description: 反射DAO中的信息
 * @author HeroW
 * @date Dec 10, 2012 6:41:19 PM
 * @version
 */

public class OrmInfo {
/*
	//id元素
	public static int IDFIELD = 1;
	//普通column元素
	public static int COLFIELD = 2;
	*/
	private String table;
	
	//数据库表池
	//private static Map<String, OrmInfo> tablePool = new HashMap<String, OrmInfo>();
	
	//主键
	private Map<String , IdField> idFields = new HashMap<String , IdField>();
	//列元素
	private Map<String , ColumnField> columnFields = new HashMap<String , ColumnField>();
	//多对一列元素
	private Map<String , ManyToOneField> MTOFields = new HashMap<String , ManyToOneField>();
	//一对一列元素
	private Map<String , OneToOneField> OTOFields = new HashMap<String , OneToOneField>();
	//一对多列元素
	private Map<String , OneToManyField> OTMFields = new HashMap<String , OneToManyField>();
	
	public static  <E> OrmInfo getOrmInfo(/*Object*/ E obj) throws ActiveRecordException {
		
		OrmInfo orm = new OrmInfo();
		/*//
		boolean isInstance = true;
		//如果obj未被实例化
		if(!(obj instanceof ActiveRecordBase)){
			try {
				Class<E> c = (Class<E>) obj;
				obj = c.newInstance();
			} catch (Exception e) {
				throw new FieldAccessException(e);
			}
			isInstance = false;
		}*/
		Class<?> clasz  =  obj.getClass();
		
		Table t = clasz.getAnnotation(Table.class);
		if (null != t) {
			if (t.name().equals("_null")) {
				orm.table = clasz.getName().toLowerCase();
			} else {
				orm.table = t.name().toLowerCase();
			}
		}else{
			//throw new ActiveRecordException( "[ " + obj.getName() + " ] 未被定义为表~");
		}

		for (Field field : clasz.getDeclaredFields()) {

			FieldInfo fieldInfo = new FieldInfo();
			fieldInfo.setName(field.getName().toLowerCase());
			fieldInfo.setField(field);
			fieldInfo.setType(field.getType());
			fieldInfo.setValue(orm.getFieldValue(field, obj));
			// 反射非ID列元素
			Column col = field.getAnnotation(Column.class);
			if (null != col) {
				ColumnField colField = new ColumnField(fieldInfo);
				if (!col.name().equals("_null")) {
					// 若没在@Column的name属性中写上值,就用变量的名字作为列名
					// 否则用name上的值作为列名
					colField.setName(col.name().toLowerCase());
				}
				colField.setAnnotation(col);
				
				orm.columnFields.put(colField.getName(), colField);
				continue;
			}
			
			// 反射ID元素
			Id id = field.getAnnotation(Id.class);
			if (null != id) {
				IdField idField = new IdField(fieldInfo);
				if (!id.name().equals("_null")) {
					// 若没在@Id的name属性中写上值,就用变量的名字作为列名
					// 否则用name的值作为列名
					idField.setName(id.name().toLowerCase());
				}
				idField.setGenerator(id.generator());
				idField.setAnnotation(id);
				
				
				orm.idFields.put(idField.getName(), idField);
				continue;
			}

			
			
			//反射一对多关系列元素
			OneToMany otm = field.getAnnotation(OneToMany.class);
			if(null != otm){
				OneToManyField otmf = new OneToManyField(fieldInfo);
				otmf.setForeignKey(otm.foreignKey());				
				otmf.setAnnotation(otm);
				//取得参数化的类型,如List<String>类型
				ParameterizedType ptype = (ParameterizedType) field.getGenericType();
				//转换成实际类型
				Class<?> subClasz = (Class<?>) ptype.getActualTypeArguments()[0];
				otmf.setType(subClasz);				
				
				
				orm.OTMFields.put(otmf.getName(), otmf);
				continue;
			}
			//反射一对一关系列元素
			OneToOne oto = field.getAnnotation(OneToOne.class);
			if(null != oto){
				OneToOneField otof = new OneToOneField(fieldInfo);
				otof.setForeignKey(oto.foreignKey());				
				otof.setAnnotation(oto);
				orm.OTOFields.put(otof.getName(), otof);
				continue;
			}
			//反射多对一关系列元素
			ManyToOne mto = field.getAnnotation(ManyToOne.class);
			if(null != mto){
				ManyToOneField mtof = new ManyToOneField(fieldInfo);
				mtof.setForeignKey(mto.foreignKey());				
				mtof.setAnnotation(mto);
				orm.MTOFields.put(mtof.getName(), mtof);
			}
		}
		return orm;
	}
	
	public <E> Object getFieldValue(Field field, E  obj) throws FieldAccessException{
		field.setAccessible(true);
		try {
			return field.get(obj);
		} catch (IllegalArgumentException e) {
			String msg = "表 [" + getTableName() + "] 中属性 [ " + field.getName() + " ]!!类型不对";
			throw new FieldAccessException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "表 [" + getTableName() + "] 中属性 [ " + field.getName() + " ]!!不能访问";
			throw new FieldAccessException(msg, e);
		}
	}
	
	public <E>  void setFieldValue(Field field, E obj, Object value) throws FieldAccessException{
		
		field.setAccessible(true);
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException e) {
			String msg = "表 [" + getTableName() + "] 中属性 [ " + field.getName() + " ]!!类型不对";
			throw new FieldAccessException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "表 [" + getTableName() + "] 中属性 [ " + field.getName() + " ]!!不能访问";
			throw new FieldAccessException(msg, e);
		}
	}
	
	
	
	/*
	public static <E> Object getIdValue(OrmInfo orm, String name, E  obj)
			throws FieldAccessException {

		IdField id = orm.idFields.get(name);
		if (null == id) {
			String msg = "表 [" + orm.getTableName() + "] 中没有找到属性 [ " + name + " ]!!";
			throw new FieldAccessException(msg);
		}
		Field f = id.getField();
		f.setAccessible(true);
		try {
			return f.get(obj);
		} catch (IllegalArgumentException e) {
			String msg = "表 [" + orm.getTableName() + "] 中属性 [ " + name + " ]!!类型不对";
			throw new FieldAccessException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "表 [" + orm.getTableName() + "] 中属性 [ " + name + " ]!!不能访问";
			throw new FieldAccessException(msg, e);
		}

	}

	public static <E> Object getColumnValue(OrmInfo orm, String name, E obj)
			throws FieldAccessException {
		ColumnField col = orm.columnFields.get(name);
		if (null == col) {
			String msg = "表 [" + orm.getTableName() + "] 中没有找到属性 [ " + name + " ]!!";
			throw new FieldAccessException(msg);
		}
		
		
		Field f = col.getField();
		f.setAccessible(true);
		try {
			return f.get(obj);
		} catch (IllegalArgumentException e) {
			String msg = "表 [" + orm.getTableName() + "] 中属性 [ " + name + " ]!!类型不对";
			throw new FieldAccessException(msg, e);
		} catch (IllegalAccessException e) {
			String msg ="表 [" + orm.getTableName() + "] 中属性 [ " + name + " ]!!不能访问";
			throw new FieldAccessException(msg, e);
		}

	}
*/
	/*
	public static <E> void setIdValue(OrmInfo orm, String name, Object obj, E value) throws FieldAccessException {

		IdField id = orm.idFields.get(name);
		if (null == id) {
			String msg = "表 [" + orm.getTableName() + "] 中没有找到属性 [ " + name + " ]!!";
			throw new FieldAccessException(msg);
		}
		Field field = id.getField();
		field.setAccessible(true);
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException e) {
			String msg = "表 [" + orm.getTableName() + "] 中属性 [ " + name + " ]!!类型不对";
			throw new FieldAccessException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "表 [" + orm.getTableName() + "] 中属性 [ " + name + " ]!!不能访问";
			throw new FieldAccessException(msg, e);
		}

	}

	public static <E> void setColumnValue(OrmInfo orm, String name, Object obj, E value) throws ActiveRecordException {

		ColumnField col = orm.columnFields.get(name);
		if (null == col) {
			String msg = "表 [" + orm.getTableName() + "] 中没有找到属性 [ " + name + " ]!!";
			throw new ActiveRecordException(msg);
		}
		
		Field field = col.getField();
		field.setAccessible(true);
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException e) {
			String msg = "表 [" + orm.getTableName() + "] 中属性 [ " + name + " ]!!类型不对";
			throw new ActiveRecordException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "表 [" + orm.getTableName() + "] 中属性 [ " + name + " ]!!不能访问";
			throw new ActiveRecordException(msg, e);
		}

	}
	*/
/*
	// 找出在数据库列中的名称的ID属性
	public static Field getIdFieldByName(Class<?> clasz, String name) {
		for (Field f : clasz.getDeclaredFields()) {
			Id id = f.getAnnotation(Id.class);

			if (null != id) {
				String nameInDB = null;
				if (id.name().equals("_null")) {
					// 若没在@Id的name属性中写上值,就用变量的名字
					nameInDB = f.getName();
				} else {
					// 否则用name的值
					nameInDB = id.name();
				}
				if (name.equals(nameInDB)) {
					return f;
				}
			}
		}
		return null;
	}

	// 找出在数据库列中的名称的Column属性
	public static Field getColumnByName(Class<?> clasz, String name) {
		for (Field f : clasz.getDeclaredFields()) {
			Column col = f.getAnnotation(Column.class);
			if (null != col) {
				String nameInDB = null;
				if (col.name().equals("_null")) {
					// 若没在@Column的name属性中写上值,就用变量的名字
					nameInDB = f.getName();
				} else {
					// 否则用name的值
					nameInDB = col.name();
				}
				if (name.equals(nameInDB)) {
					return f;
				}
			}
		}
		return null;
	}

	public static <E> Object getIdValue(Class<?> clasz, String name, E  obj)
			throws ActiveRecordException {

		Field f = getIdFieldByName(clasz, name);
		if (null == f) {
			String msg = "没有找到属性 [ " + name + " ]!!";
			throw new ActiveRecordException(msg);
		}
		f.setAccessible(true);
		try {
			return f.get(obj);
		} catch (IllegalArgumentException e) {
			String msg = "属性 [ " + name + " ]!!类型不对";
			throw new ActiveRecordException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "属性 [ " + name + " ]!!不能访问";
			throw new ActiveRecordException(msg, e);
		}

	}

	public static <E> Object getColumnValue(Class<?> clasz, String name, E obj)
			throws ActiveRecordException {
		Field f = getColumnByName(clasz, name);
		if (null == f) {
			String msg = "没有找到属性 [ " + name + " ]!!";
			throw new ActiveRecordException(msg);
		}
		f.setAccessible(true);

			try {
				return f.get(obj);
			} catch (IllegalArgumentException e) {
				String msg = "属性 [ " + name + " ]!!类型不对";
				throw new ActiveRecordException(msg, e);
			} catch (IllegalAccessException e) {
				String msg = "属性 [ " + name + " ]!!不能访问";
				throw new ActiveRecordException(msg, e);
			}
		
	}

	public static <E> void setIdValue(Class<?> clasz, String name, Object obj, E value) throws ActiveRecordException {

		Field field = getIdFieldByName(clasz, name);
		if (null == field) {
			String msg = "没有找到属性 [ " + name + " ]!!";
			throw new ActiveRecordException(msg);
		}
		field.setAccessible(true);
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException e) {
			String msg = "属性 [ " + name + " ]!!类型不对";
			throw new ActiveRecordException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "属性 [ " + name + " ]!!不能访问";
			throw new ActiveRecordException(msg, e);
		}

	}

	public static <E> void setColumnValue(Class<?> clasz, String name, Object obj, E value) throws ActiveRecordException {

		Field field = getColumnByName(clasz, name);

		if (null == field) {
			String msg = "没有找到属性 [ " + name + " ]!!";
			throw new ActiveRecordException(msg);
		}
		field.setAccessible(true);
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException e) {
			String msg = "属性 [ " + name + " ]!!类型不对";
			throw new ActiveRecordException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "属性 [ " + name + " ]!!不能访问";
			throw new ActiveRecordException(msg, e);
		}

	}
*/
	public boolean hasColumnField(String col) {
		return this.columnFields.containsKey(col);
	}
	public  boolean  isDate(String key){
		ColumnField c = this.columnFields.get(key);
		 String typeName = c.getType().getClass().getCanonicalName();
		 if (typeName.equals("java.sql.Date")){
			 return true;
		 }
		
		return false;
	}

	public boolean hasIdField(String id) {
		return this.idFields.containsKey(id);
	}

	public static String getMethodName(String fieldName) {
		String str1 = fieldName.substring(0, 1);
		String str2 = fieldName.substring(1);
		return "get" + str1.toUpperCase() + str2;
	}

	public static String getFieldName(String methodName) {
		String str1 = methodName.substring(3, 4);
		String str2 = methodName.substring(4);
		return str1.toLowerCase() + str2;
	}

	public String getTableName() {
		return table.toLowerCase();
	}
/*	
	public List<IdField> getIdFields() {
		return new ArrayList<IdField>(idFields.values());
	}

	public List<ColumnField> getColumnFields() {
		return new ArrayList<ColumnField>(columnFields.values());
	}
*/
	public Map<String , IdField> getIdFields() {
		return idFields;
	}

	public Map<String , ColumnField> getColumnFields() {
		return columnFields;
	}

	public Map<String, ManyToOneField> getMTOFields() {
		return MTOFields;
	}

	public Map<String, OneToOneField> getOTOFields() {
		return OTOFields;
	}

	public Map<String, OneToManyField> getOTMFields() {
		return OTMFields;
	}
	
	
}
