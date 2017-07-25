package com.digitalchina.web.common.user.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.digitalchina.web.common.cache.impl.XMemcachedCache;
import com.digitalchina.web.common.user.vo.UserVo;

/**
 * 类型描述：<br/>
 * 用户缓存
 * 
 * @createTime 2016年6月26日
 * @author maiwj
 * 
 */
@Component
public class UserCache {

	private final static String CACHE_PREFIX_ = "_usercenter_info_";

	@Autowired
	private XMemcachedCache cache;
	
	/**
	 * 方法描述：<br/>
	 * 更新，只有当id存在值的时候才进行更新
	 * 
	 * @createTime 2016年6月27日
	 * @author maiwj
	 *
	 * @param userId
	 * @param flushLiveSeconds
	 * @return
	 */
	public boolean more(Long userId, int flushLiveSeconds) {
		UserVo result = cache.touch(CACHE_PREFIX_ + userId, flushLiveSeconds);
		if (result != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * 方法描述：<br/>
	 * 存在
	 * 
	 * @createTime 2016年6月26日
	 * @author maiwj
	 *
	 * @param userId
	 * @return
	 */
	public boolean has(Long userId) {
		return cache.get(CACHE_PREFIX_ + userId) != null;
	}
	
	/**
	 * 方法描述：<br/>
	 * 获取
	 * 
	 * @createTime 2016年6月26日
	 * @author maiwj
	 *
	 * @param userId
	 * @return
	 */
	public UserVo get(Long userId) {
		return cache.get(CACHE_PREFIX_ + userId);
	}

	/**
	 * 方法描述：<br/>
	 * 添加
	 * 
	 * @createTime 2016年6月26日
	 * @author maiwj
	 *
	 * @param userId
	 * @param user
	 */
	public void set(Long userId, UserVo user) {
		cache.set(CACHE_PREFIX_ + userId, user);
	}

	/**
	 * 方法描述：<br/>
	 * 删除
	 * 
	 * @createTime 2016年6月26日
	 * @author maiwj
	 *
	 * @param userId
	 */
	public void del(Long userId) {
		cache.delete(CACHE_PREFIX_ + userId);
	}
}
