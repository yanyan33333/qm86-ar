package com.qm86.ar.tx;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.qm86.ar.exception.TransactionExcetion;

import net.sf.cglib.proxy.Enhancer;

/**
 * @Title: TransObjectFactory.java
 * @Package com.qm86.ar.tx
 * @Description: 对象工厂,用管理dao ,service和普通对象实例化
 * @author HeroW
 * @date Dec 12, 2012 12:07:15 AM
 * @version
 */

public class TransObjectFactory {

	public static Logger logger = Logger.getLogger(TransObjectFactory.class);
	// 对象池
	private static Map<String, Object> objPool = new HashMap<String, Object>();

	/**
	 * @author: HeroW
	 * @Title: getObject
	 * @date Jan 23, 2013 12:34:00 PM
	 * @Description: 取得对象实例,若以Service结尾则识定为Service层对象,创建代理对象 若是普通对象,就直接实例化
	 * @param clazName
	 * @return
	 * @returnType Object
	 * @throws
	 */
	public static Object getObject(String clazName) {
		Object result = null;
		if (objPool.containsKey(clazName)) {// 已经存在
			result = objPool.get(clazName);
		} else {
			try {
				if (clazName.endsWith("Service")) { // 是Service层对象
					// 动态创建对象
					Enhancer enhancer = new Enhancer();
					// 设置代理实例父类
					enhancer.setSuperclass(Class.forName(clazName));
					// 设置回调
					enhancer.setCallback(new TransMethodInterceptor());
					result = enhancer.create();
				} else {// 是普通对象就直接实例化
					result = Class.forName(clazName).newInstance();
				}
				objPool.put(clazName, result);
			} catch (Exception e) {
				String msg = "对象实例化出错!";
				logger.error(msg, e);
			}
		}
		return result;
	}
}
