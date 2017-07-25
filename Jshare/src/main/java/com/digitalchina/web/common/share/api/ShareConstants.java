package com.digitalchina.web.common.share.api;

/**
 * 类型描述：<br/>
 * 系统常量
 * 
 * @createTime 2016年9月5日
 * @author lijin
 * 
 */
public interface ShareConstants {

	/**
	 * 申请中
	 */
	int STATUS_APPLY = 0;
	
	/**
	 * 已结束
	 */
	int STATUS_FINISH = 100;
	
	/**
	 * 尚未结束
	 */
	int APPLYSTATE_NO = 0;
	
	/**
	 * 申请失败
	 */
	int APPLYSTATE_FAIL = 100;
	
	/**
	 * 申请成功
	 */
	int APPLYSTATE_SUCC = 200;
	
	/**
	 * 公众号接口配置的Token
	 */
	String TOKEN = "share";
	
	/**
	 * 字符编码:UTF-8
	 */
	String ENCODING = "UTF-8";
}
