package com.qm86.test.main;

import java.util.Date;
import java.util.List;

import com.qm86.ar.ActiveRecordBase;
import com.qm86.ar.exception.ActiveRecordException;
import com.qm86.test.model.TestTable;

/**
 * @Title: TestDemo.java
 * @Package com.qm86.test.main
 * @Description: TODO
 * @author HeroW
 * @date Mar 21, 2013 8:29:27 PM
 * @version 
 */

public class TestDemo {
	
	void Insert(){
		TestTable tt = new TestTable();
		tt.setName("Hello qm86-ar~~");
		tt.setWhere("Everywhere");
		tt.setWhen(new Date());
		try {
			tt.insert();
		} catch (ActiveRecordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void find(){
		try {
			List<TestTable> tt =  TestTable.findAll(TestTable.class);
			for(TestTable t : tt){
				System.out.print("\ntable  : \n" + t);
			}
		} catch (ActiveRecordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		ActiveRecordBase ar = new ActiveRecordBase();
		TestDemo  demo = new TestDemo();
		//demo.Insert();
		demo.find();
	}
}
