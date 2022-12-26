package com.yue.chip.constant;

/**
 * 查询常量参数
 * @author mrliu
 *
 */
public class SearchConstant {
	
	/**
	 * 等于(用于字符串)
	 */
	public static final String EQUAL = "EQUAL";
	
	/**
	 * 不等于(用于字符串)
	 */
	public static final String NOT_EQUAL = "NOTEQUAL";
	
	/**
	 * 
	 */
	public static final String IS_NULL = "ISNULL";
	
	/**
	 * 
	 */
	public static final String IS_NOT_NULL = "ISNOTNULL";
	
	/**
	 * 
	 */
	public static final String IN = "IN";
	
	/**
	 * 
	 */
	public static final String NOT_IN = "NOTIN";
	
	/**
	 * 模糊查询(用于字符串)
	 */
	public static final String LIKE = "LIKE";
		
	/**
	 * 模糊查询(用于字符串)
	 */
	public static final String NOT_LIKE = "NOTLIKE";
	
	/**
	 * 大于等于(用于数字类型)
	 */
	public static final String GE = "GE";
	
	/**
	 * 大于(用于数字类型)
	 */
	public static final String GT = "GT";
	
	/**
	 * 小于(用于数字类型)
	 */
	public static final String LE = "LE";
	
	/**
	 * 小于等于(用于数字类型)
	 */
	public static final String LT = "LT";
	
	/**
	 * 
	 */
	public static final String OR = "OR";
	
	/**
	 * 
	 */
	public static final String OR_LIKE = "ORLIKE";
	
	/**
	 * 大于等于当前时间
	 */
	public static final String GREATER_THAN_OR_EQUAL_TO = "GREATERTHANOREQUALTO";
	
	/**
	 * 大于当前时间
	 */
	public static final String GREATER_THAN = "GREATERTHAN";
	
	/**
	 * 小于当前时间
	 */
	public static final String LESS_THAN = "LESSTHAN";
	
	/**
	 * 小于等于当前时间
	 */
	public static final String LESS_THAN_OR_EQUAL_TO = "LESSTHANOREQUALTO";
	
	/**
	 * 未现实禁止使用
	 */
	@Deprecated
	public static final String BETWEEN ="BETWEEN";
	
	/**
	 * 未现实禁止使用
	 */
	@Deprecated
	public static final String IS_EMPTY ="ISEMPTY";
	
	public static final String IS_FALSE ="ISFALSE";
	
	/**
	 * 未现实禁止使用
	 */
	@Deprecated
	public static final String IS_MEMBER ="ISMEMBER";
	
	/**
	 * 未现实禁止使用
	 */
	@Deprecated
	public static final String IS_NOT_EMPTY ="ISNOTEMPTY";
	
	/**
	 * 未现实禁止使用
	 */
	@Deprecated
	public static final String IS_NOT_MEMBER ="ISNOTMEMBER";
	
	public static final String IS_TRUE ="ISTRUE";

	public static final String LEFT_JOIN ="LEFTJOIN";

	public static final String RIGHT_JOIN ="RIGHTJOIN";

	public static final String INNER_JOIN ="INNERJOIN";

	/**
	 * 未现实禁止使用
	 */
	@Deprecated
	public static final String FULL_JOIN ="FULLJOIN";
}
