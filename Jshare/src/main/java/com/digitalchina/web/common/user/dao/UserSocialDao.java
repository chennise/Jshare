package com.digitalchina.web.common.user.dao;

import java.util.List;
import com.digitalchina.web.common.dao.IDao;
import com.digitalchina.web.common.dao.IRepositoryWithoutPage;
import com.digitalchina.web.common.user.vo.UserSocialVo;

/**
 * 类型描述：<br/>
 * 社交Dao
 * 
 * @createTime 2016年6月30日
 * @author maiwj
 * 
 */
public interface UserSocialDao extends IRepositoryWithoutPage<UserSocialVo>, IDao {

	/**
	 * 方法描述：<br/>
	 * 查询对应用户的社交Id
	 * 
	 * @createTime 2016年7月14日
	 * @author maiwj
	 *
	 * @param vo
	 * @return
	 */
	List<UserSocialVo> selectVos(UserSocialVo vo);

}
