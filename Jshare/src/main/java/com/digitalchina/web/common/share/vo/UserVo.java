package com.digitalchina.web.common.share.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.digitalchina.web.common.db.nosql.mongo.annotation.Collection;
import com.digitalchina.web.common.db.nosql.mongo.annotation.Embedded;
import com.digitalchina.web.common.db.nosql.mongo.annotation.Id;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 类型描述：<br/>
 * 用户对象
 * 
 * @author LJn
 *
 */
@Collection("user")
@JsonInclude(Include.NON_EMPTY)
public class UserVo implements Serializable{

	private static final long serialVersionUID = 3441520528432385318L;

	@Id
	private String id; // 主键
	private String userId; // 用户编号
	private String socialId; // 社交编号
	private String name; // 手机号，登录名
	private String nickname; // 昵称
	@Embedded
	private List<CircleVo> circles = new ArrayList<CircleVo>(); // 圈子
	
	/**
	 * 申请者辅助
	 */
	private Integer applyState; // 申请状态 
	private Long applyTime; // 申请时间
	private String remark; // 备注
	
	/**
	 * 清理函数
	 * @param vo
	 * @return
	 */
	public UserVo clean(UserVo vo){
		vo.setRemark(null);
		vo.setApplyTime(null);
		return vo;
	}
	
	public static UserVo createById(String id){
		UserVo vo = new UserVo();
		vo.id = id;
		return vo;
	}
	
	public static UserVo createByUserId(String userId){
		UserVo vo = new UserVo();
		vo.userId = userId;
		return vo;
	}
	
	public static UserVo createBySocialId(String socialId){
		UserVo vo = new UserVo();
		vo.socialId = socialId;
		return vo;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Long getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Long applyTime) {
		this.applyTime = applyTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<CircleVo> getCircles() {
		return circles;
	}
	public void setCircles(List<CircleVo> circles) {
		this.circles = circles;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSocialId() {
		return socialId;
	}
	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}
	public Integer getApplyState() {
		return applyState;
	}
	public void setApplyState(Integer applyState) {
		this.applyState = applyState;
	}
}
