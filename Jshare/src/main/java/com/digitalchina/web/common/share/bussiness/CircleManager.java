package com.digitalchina.web.common.share.bussiness;

import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.digitalchina.web.common.api.ResultPageVo;
import com.digitalchina.web.common.db.nosql.Query;
import com.digitalchina.web.common.db.nosql.mongo.MongoDBId;
import com.digitalchina.web.common.db.nosql.mongo.MongoQuery;
import com.digitalchina.web.common.db.nosql.mongo.MongoQuery.Select.From.Where;
import com.digitalchina.web.common.share.dao.CircleMongoDao;
import com.digitalchina.web.common.share.vo.CircleVo;
import com.mongodb.client.model.Filters;

/**
 * 类型描述：<br/>
 * 圈子管理Manager
 * 
 * @createTime 2016年9月5日
 * @author LJn
 * 
 */
@Service
public class CircleManager {

	@Autowired
	private CircleMongoDao circleMongoDao;
	
	/**
	 * 方法描述：<br/>
	 * 增加
	 * 
	 * @createTime 2016年9月6日
	 * @author lijin
	 *
	 * @param applicationMetaSave
	 */
	public void doSave(CircleVo circleToSave) {

		circleToSave.setPublishTime(System.currentTimeMillis());
		circleMongoDao.insert(circleToSave); // MongoDB
		
	}

	/**
	 * 方法描述：<br/>
	 * 删除
	 * 
	 * @createTime 2016年9月6日
	 * @author lijin
	 *
	 * @param id
	 */
	public boolean doRemove(String id) {

		List<CircleVo> records = circleMongoDao
				.select(MongoQuery.select(MongoDBId.field).from(CircleVo.class)
						.where(Filters.and(MongoDBId.eq(id))).done()).getResultList();

		if (CollectionUtils.isEmpty(records)) {
			return false;
		}

		CircleVo circleToRemove = records.get(0);

		circleMongoDao.delete(circleToRemove); // MongoDB
		
		return true;
	}
	

	/**
	 * 方法描述：<br/>
	 * 修改
	 * 
	 * @createTime 2016年9月6日
	 * @author lijin
	 *
	 * @param applicationMetaModify
	 * @return
	 */
	public boolean doModify(CircleVo circleModify) {

		CircleVo result = findVo(circleModify.getId());

		if (result == null) {
			return false;
		}

		circleMongoDao.update(circleModify); // MongoDB
		
		return true;
	}

	/**
	 * 方法描述：<br/>
	 * 条项查询
	 * 
	 * @createTime 2016年9月6日
	 * @author lijin
	 *
	 * @param id
	 * @return
	 */
	public CircleVo findVo(String circleId) {

		Query query = MongoQuery.select().from(CircleVo.class).where(MongoDBId.eq(circleId)).done();

		List<CircleVo> result = circleMongoDao.select(query).getResultList();

		if(CollectionUtils.isNotEmpty(result)){
			return result.get(0);
		}
		
		return null;
	}
	
	/**
	 * 方法描述：<br/>
	 * 管理端使用，列表查询，可分页
	 * 
	 * @createTime 2016年9月6日
	 * @author lijin
	 *
	 * @param requestPage
	 *            请求页码
	 * @param pageSize
	 *            查询每页大小
	 * @param conditions
	 *            条件
	 * @return
	 */
	public ResultPageVo<CircleVo> findVos(Integer requestPage, Integer pageSize,
			Map<String, Object> conditions) {

		Where whereQuery = MongoQuery.select().from(CircleVo.class);

		Query query = whereQuery.limit(requestPage, pageSize).desc("publishTime").done();

		return circleMongoDao.select(query);
	}
	
}
