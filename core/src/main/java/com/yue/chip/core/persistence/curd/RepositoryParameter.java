package com.yue.chip.core.persistence.curd;

import jakarta.persistence.Query;
import java.util.Map;


/**
 * 
 * @author mrliu
 *
 */
public class RepositoryParameter {
	
	/**
	 * 设置参数
	 * @param query
	 * @param parameter
	 */
	public static Query setParameter(Query query, Map<String, Object> parameter) {
		if(parameter==null || query==null){
			return query;
		}
		for (String key : parameter.keySet()) {
			query.setParameter(key, parameter.get(key));
		}
		return query;
	}
	
	

}
