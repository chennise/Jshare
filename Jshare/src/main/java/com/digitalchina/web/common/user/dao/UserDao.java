package com.digitalchina.web.common.user.dao;

import org.apache.ibatis.annotations.Param;
import com.digitalchina.web.common.dao.IDao;
import com.digitalchina.web.common.dao.IRepositoryWithoutPage;
import com.digitalchina.web.common.user.vo.UserVo;

/**
 * 
 * 描述：</br>
 * 用户信息操作dao
 * 
 * @author wukj
 * @createTime 2016年3月1日
 */
public interface UserDao extends IRepositoryWithoutPage<UserVo>, IDao {

	/**
	 * 方法描述：<br/>
	 * 通过手机查找用户
	 * 
	 * @createTime 2016年6月29日
	 * @author maiwj
	 *
	 * @param phone
	 * @return
	 */
	UserVo selectVoByPhone(@Param("phone") Long phone);

}
