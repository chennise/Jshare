package com.digitalchina.web.common.share.bussiness;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.digitalchina.web.common.api.ResultPageVo;
import com.digitalchina.web.common.db.nosql.Query;
import com.digitalchina.web.common.db.nosql.mongo.MongoDBId;
import com.digitalchina.web.common.db.nosql.mongo.MongoQuery;
import com.digitalchina.web.common.db.nosql.mongo.MongoQuery.Select.From.Where;
import com.digitalchina.web.common.db.nosql.mongo.MongoQuery.Select.From.Where.Limit;
import com.digitalchina.web.common.db.nosql.mongo.MongoQuery.Select.From.Where.Limit.OrderBy;
import com.digitalchina.web.common.search.elasticsearch.ESQuery;
import com.digitalchina.web.common.search.elasticsearch.ESQuery2;
import com.digitalchina.web.common.search.elasticsearch.ESSession;
import com.digitalchina.web.common.search.elasticsearch.ESQuery2.Equal;
import com.digitalchina.web.common.search.elasticsearch.ESQuery2.Like;
import com.digitalchina.web.common.search.elasticsearch.ESQuery2.Select.From.Where.CustomWhere.Begin.End;
import com.digitalchina.web.common.share.api.ShareConstants;
import com.digitalchina.web.common.share.dao.ShareRecordMongoDao;
import com.digitalchina.web.common.share.vo.CircleVo;
import com.digitalchina.web.common.share.vo.ShareRecordVo;
import com.digitalchina.web.common.share.vo.UserVo;
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
public class ShareRecordManager {

	private final static Logger LOG = LogManager.getLogger(ShareRecordManager.class);
	
	@Autowired
	private ShareRecordMongoDao shareRecordMongoDao;
	@Lazy
	@Autowired
	private ESSession shareRecordEssession;
	
	/**
	 * 方法描述：<br/>
	 * 增加
	 * 
	 * @createTime 2016年9月6日
	 * @author lijin
	 */
	public void doSave(ShareRecordVo shareRecordToSave) {
		
		String shareRecordId = new ObjectId().toHexString();
		shareRecordToSave.setId(shareRecordId);
		shareRecordToSave.setPublishTime(System.currentTimeMillis());
		shareRecordToSave.setStatus(ShareConstants.STATUS_APPLY);
		shareRecordToSave.setApplyNum(0);
		shareRecordMongoDao.insert(shareRecordToSave); // MongoDB

		if(CollectionUtils.isNotEmpty(shareRecordToSave.getCircles())){
			for(CircleVo circle:shareRecordToSave.getCircles()){
				String id = shareRecordId + circle.getPublishTime(); // 主键为id+圈子创建时间
				shareRecordToSave.setId(id);
				shareRecordToSave.setRecordId(shareRecordId);
				shareRecordToSave.setCircle(circle);
				shareRecordEssession.insert(shareRecordToSave); // Elasticsearch
			}
		}
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

		List<ShareRecordVo> records = shareRecordMongoDao
				.select(MongoQuery.select(MongoDBId.field).from(ShareRecordVo.class)
						.where(Filters.and(MongoDBId.eq(id))).done()).getResultList();

		if (CollectionUtils.isEmpty(records)) {
			return false;
		}

		ShareRecordVo shareRecordToRemove = records.get(0);

		shareRecordMongoDao.delete(shareRecordToRemove); // MongoDB
		
		if(CollectionUtils.isNotEmpty(shareRecordToRemove.getCircles())){
			for(CircleVo circle:shareRecordToRemove.getCircles()){
				shareRecordToRemove.setId(shareRecordToRemove.getId()+circle.getPublishTime()+"");
				shareRecordEssession.delete(shareRecordToRemove); // Elasticsearch
			}
		}
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
	public boolean doModify(ShareRecordVo shareRecordModify) {

		ShareRecordVo result = findVo(shareRecordModify.getId());
		if (result == null) {
			return false;
		}
		shareRecordMongoDao.update(shareRecordModify); // MongoDB
		
		if(CollectionUtils.isNotEmpty(shareRecordModify.getCircles())){
			for(CircleVo circle:shareRecordModify.getCircles()){
				shareRecordModify.setId(shareRecordModify.getId()+circle.getPublishTime()+"");
				shareRecordEssession.update(shareRecordModify); // Elasticsearch
			}
		}
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
	public ShareRecordVo findVo(String shareRecordId) {

		Query query = MongoQuery.select().from(ShareRecordVo.class).where(MongoDBId.eq(shareRecordId)).done();

		List<ShareRecordVo> result = shareRecordMongoDao.select(query).getResultList();

		if(CollectionUtils.isNotEmpty(result)){
			return result.get(0);
		}
		
		return null;
	}
	
	/**
	 * 方法描述：<br/>
	 * 列表查询，可分页
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
	public ResultPageVo<ShareRecordVo> findVos(Integer requestPage, Integer pageSize,
			Map<String, Object> conditions) {
		
		Integer type = (Integer) conditions.get("type");
		String circleId = (String) conditions.get("circleId");
		
		Where whereQuery = MongoQuery.select().from(ShareRecordVo.class);
		
		Query query = null;
		
		if(type.intValue()!=0){
			query = whereQuery
					.where(Filters.and(Filters.eq("type", type), Filters.elemMatch("circles", Filters.eq(MongoDBId.field,  new ObjectId(circleId)))))
					.limit(requestPage, pageSize).desc("publishTime").asc("status").done();
		}else{
			query = whereQuery
					.where(Filters.elemMatch("circles", Filters.eq(MongoDBId.field,  new ObjectId(circleId))))
					.limit(requestPage, pageSize).desc("publishTime").asc("status").done();
		}
		
		return shareRecordMongoDao.select(query);
	}
	
	/**
	 * 方法描述：<br/>
	 * 查找我的闲置列表
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
	public ResultPageVo<ShareRecordVo> findByPublisher(String userId, Integer requestPage, Integer pageSize) {
		
		Where whereQuery = MongoQuery.select().from(ShareRecordVo.class);
		
		Query query = whereQuery
					.where(Filters.eq("publisher._id", new ObjectId(userId)))
					.limit(requestPage, pageSize).desc("publishTime").done();

		return shareRecordMongoDao.select(query);
	}
	
	/**
	 * 方法描述：<br/>
	 * 查找我的待办
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
	public ResultPageVo<ShareRecordVo> findMyWork(String userId, Integer requestPage, Integer pageSize) {
		
		Where whereQuery = MongoQuery.select().from(ShareRecordVo.class);
		
		Query query = whereQuery
					.where(Filters.and(Filters.eq("publisher._id", new ObjectId(userId)),
								Filters.eq("status", ShareConstants.STATUS_APPLY),
								Filters.gt("applyNum", 0)))
					.limit(requestPage, pageSize).desc("publishTime").done();

		return shareRecordMongoDao.select(query);
	}
	
	/**
	 * 方法描述：<br/>
	 * 查找我的申请
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
	public ResultPageVo<ShareRecordVo> findMyApply(String userId, Integer requestPage, Integer pageSize) {
		
		Where whereQuery = MongoQuery.select().from(ShareRecordVo.class);
		
		Query query = whereQuery
					.where(Filters.elemMatch("applicants", Filters.eq(MongoDBId.field, new ObjectId(userId))))
					.limit(requestPage, pageSize)
					.desc("publishTime")
					.done();

		ResultPageVo<ShareRecordVo> resultPageVo = shareRecordMongoDao.select(query);
		if(resultPageVo.getTotalCount()>0){
			List<ShareRecordVo> recordVos = resultPageVo.getResultList();
			for(ShareRecordVo recordVo : recordVos){
				List<UserVo> userVos = recordVo.getApplicants();
				for(UserVo userVo : userVos){
					if(StringUtils.equals(userVo.getId(), userId) 
							&& userVo.getApplyState()==ShareConstants.APPLYSTATE_SUCC){
						recordVo.setApplyState(true);
					}
				}
			}
		}
		
		return resultPageVo;
	}
	
	/**
	 * 方法描述：<br/>
	 * ES查询
	 * 
	 * @createTime 2016年9月29日
	 * @author lijin
	 *
	 * @param userBinds
	 * @param keyword
	 * @param requestPage
	 * @param pageSize
	 * @param orderByCollectionTimeInASC
	 * @return
	 */
	public ResultPageVo<?> findVos(List<CircleVo> circles, String keyword, Integer requestPage,
			Integer pageSize, boolean orderByPublicTimeInASC) {

		ESQuery query = null;
		
		Equal equal = Equal.as(circles.get(0).getId(), "circle.id");
		for(int i = 1;i<circles.size();i++){
			equal.or(Equal.as(circles.get(i).getId(), "circle.id"));
		}
		
		End limitQuery = ESQuery2.select()
				.from(ShareRecordVo.class)
				.where()
					.begin()
						.merge(equal)
						.merge(Like.as(keyword, "name", "summary"))
					.end();
			
		if (pageSize == null) {
			pageSize = 20;
		}
		if (requestPage == null) {
			requestPage = 1;
		}
			
		if (orderByPublicTimeInASC) {
			query = limitQuery.limit(requestPage, pageSize).asc("publishTime").done();
		} else {
			query = limitQuery.limit(requestPage, pageSize).desc("publishTime").done();
		}
			
		return shareRecordEssession.selectListAsMap(query);
		
	}
}
