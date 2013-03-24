package com.qm86.test.model;



import com.qm86.ar.ActiveRecordBase;
import com.qm86.ar.annotation.Column;
import com.qm86.ar.annotation.Id;
import com.qm86.ar.annotation.Table;

/**
 * @Title: TestTable.java
 * @Package com.qm86.test.model
 * @Description: TODO
 * @author HeroW
 * @date Mar 21, 2013 8:19:15 PM
 * @version 
 */
@Table(name = "tb_TestTable")
public class TestTable extends ActiveRecordBase{
	@Id(name = "tb_id")
	private int id;
	@Column
	private String name;
	@Column(name = "address")
	private String where;
	@Column
	private java.util.Date when;
	
	
	public String toString(){
		return "id = " + getId() + " name = " + getName() + " where = " + getWhere() + " when = " + getWhen();
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public java.util.Date getWhen() {
		return when;
	}
	public void setWhen(java.util.Date when) {
		this.when = when;
	}
	

}
