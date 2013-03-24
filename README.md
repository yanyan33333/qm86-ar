qm86-ar
=======

A simple ORM

这是一个简单的ORM,由于我觉得Hibernate的配置比较麻烦,而JFinal的ORM又不够灵活,之前看到过一个叫ETMVC的框架ORM写的挺好的,所以在参考了ETMVC和JFinal的部分源码后编写了qm86-ar.纯属个人娱乐,主要作学习用途,代码并不复杂,希望大家都能了解ORM的运作原理.目前只支持Oracle,之后还会支持其它DB的!前期肯定会有很多的bug,我会慢慢修好的^_^

功能
--------
(注意:目前只支持Oracle)
基本的CRUD
支持关联表的插入删除操作
支持声明式建立model
支持声明式事务
支持插入行后取得刚插入行的行号

例子
----------
在源码的test中会有个简单的例子
假设DB中表名为tb_testtable(目前只支持小写的表名)
表中列名为 tb_id(NUMBER) , name(VARCHAR2) , address(VARCHAR2) ,when(DATE)

为这个表建立的model为
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

........
(some getters and setters)
}

@Table声明了该类是一个表的对象,name属性是可选的,如果填写name,则该类对应的表名为name的值,本例中name的值为表名
tb_testtable,若不填写name,则qm86默认认为类名为DB表名testtable(注意是小写的)

@Id声明主键名,Oracle用的是sequence自增,每个表必须有一个sequence,默认sequence名为小写的表名+_seq,本例的sequence名为
tb_testtable_seq.@Id的name属性也是可选的,在本例中,id就对应DB表中的tb_id列,如果填写name属性,则默认以类中对应的属性名为DB中列的名字

@Column声明了普通列,用法跟@Id相同
注意一点在Oracle中,当DB中列的类型为DATE时,类中对应的属性可以设为java.util.Date以方便处理
