package com.digitalchina.web.common.user.api;

import com.digitalchina.web.common.api.IManager;
import com.digitalchina.web.common.user.vo.UserVo;

/**
 * 
 * 描述：</br>
 * 用户信息管理
 * 
 * @author wukj
 * @createTime 2016年3月1日
 */
public interface IUserManager extends IManager<UserVo> {

	/**
	 * 方法描述：<br/>
	 * 进行用户登录
	 * 
	 * @createTime 2016年6月25日
	 * @author maiwj
	 *
	 * @param phone
	 * @param passwordSHA1
	 *            进过SHA1的加密密码
	 * @return
	 */
	UserVo login(Long phone, String passwordSHA1);

	/**
	 * 方法描述：<br/>
	 * 进行注册
	 * 
	 * @createTime 2016年6月27日
	 * @author maiwj
	 *
	 * @param user
	 * @param verifyCodeBusinsessType
	 * @return
	 */
	boolean doRegiste(UserVo user, String verifyCodeBusinsessType);
	
	/**
	 * 方法描述：<br/>
	 * 进行注册
	 * 
	 * @createTime 2016年6月27日
	 * @author maiwj
	 *
	 * @param user
	 * @return
	 */
	boolean doRegiste(UserVo user);

	/**
	 * 方法描述：<br/>
	 * 根据手机号查询一个用户
	 * 
	 * @createTime 2016年6月26日
	 * @author maiwj
	 *
	 * @param phone
	 * @return
	 */
	UserVo findOneByPhone(Long phone);

	/**
	 * 方法描述：<br/>
	 * 修改密码
	 * 
	 * @createTime 2016年6月26日
	 * @author maiwj
	 *
	 * @param userId
	 * @param phone
	 * @param verifyCode
	 * @param password
	 * @return
	 */
	boolean doModifyPassword(Long userId, Long phone, String verifyCode, String password);

}
