package com.digitalchina.web.common.user.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalchina.web.common.user.api.IUserSocialManager;
import com.digitalchina.web.common.user.dao.UserSocialDao;
import com.digitalchina.web.common.user.vo.UserSocialVo;
import com.digitalchina.web.common.util.lang.DateHandler;

/**
 * 类型描述：<br/>
 * 用户社交管理
 * 
 * @createTime 2016年7月1日
 * @author maiwj
 * 
 */
@Service
public class UserSocialManager implements IUserSocialManager {

	@Autowired
	private UserSocialDao userSocialDao;

	@Override
	public boolean doSave(UserSocialVo userSocial) {
		try {
			userSocial.setCreateTime(DateHandler.getTimeInMillis());
			userSocialDao.insertVo(userSocial);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public boolean doRemove(UserSocialVo userSocial) {
		UserSocialVo result = userSocialDao.selectVo(userSocial);
		if (result != null) {
			userSocialDao.deleteVo(userSocial);
			return true;
		}
		return false;
	}

	@Override
	public UserSocialVo findVo(UserSocialVo userSocial) {
		return userSocialDao.selectVo(userSocial);
	}
	
	@Override
	public List<UserSocialVo> findVos(UserSocialVo userSocialVo) {
		return userSocialDao.selectVos(userSocialVo);
	}

}
