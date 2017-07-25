package com.digitalchina.web.common.share.dao;

import java.util.List;
import com.digitalchina.web.common.db.nosql.Query;
import com.digitalchina.web.common.db.nosql.annotation.Database;
import com.digitalchina.web.common.db.nosql.annotation.Select;
import com.digitalchina.web.common.db.nosql.dao.INoSQLDao;
import com.digitalchina.web.common.share.vo.UserVo;

/**
 * 类型描述：<br/>
 * 用户管理DAO
 * 
 * @createTime 2016年9月2日
 * @author LJn
 * 
 */
@Database("user")
public interface UserMongoDao extends INoSQLDao<UserVo> {

	/**
	 * 方法描述：<br/>
	 * 列表查询
	 * 
	 * @createTime 2016年9月6日
	 * @author LJn
	 *
	 * @param query
	 * @return
	 */
	@Select
	List<UserVo> selectVos(Query query);
}
