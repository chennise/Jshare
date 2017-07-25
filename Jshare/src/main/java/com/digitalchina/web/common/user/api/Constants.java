package com.digitalchina.web.common.user.api;

import com.digitalchina.web.common.util.config.PropertiesHandler;

/**
 * 
 * 描述：</br>
 * 常量
 * @author wukj
 * @createTime 2016年3月1日
 */
public interface Constants {
	
	/**
	 * 常量描述：<br/>
	 * 图片服务
	 */
	String IMAGE_AK = PropertiesHandler.getProperty("image.ak");
	
	String IMAGE_SK = PropertiesHandler.getProperty("image.sk");
	
	String IMAGE_BUCKETNAME =  PropertiesHandler.getProperty("image.bucketname");

	/**
	 * 常量描述：<br/>
	 * 注册
	 */
	String BUSINESS_TYPE_REGISTER = "01";
	
	/**
	 * 常量描述：<br/>
	 * 重置密码
	 */
	String BUSINESS_TYPE_RESET_PWD = "02";
	
	/**
	 * 常量描述：<br/>
	 * 社交校验
	 */
	String BUSINESS_TYPE_SOCIAL = "03";
	
	/**
	 * 常量描述：<br/>
	 * 短信登录
	 */
	String BUSINESS_TYPE_MESSAGE_LOGIN = "04";
	
}
