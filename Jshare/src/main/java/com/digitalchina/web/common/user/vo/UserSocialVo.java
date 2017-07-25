package com.digitalchina.web.common.user.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.ScriptAssert;

import com.digitalchina.web.common.user.vo.validation.BindGroup;
import com.digitalchina.web.common.user.vo.validation.FindGroup;
import com.digitalchina.web.common.user.vo.validation.LoginGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 类型描述：<br/>
 * 社交Vo
 * 
 * @createTime 2016年10月6日
 * @author LJn
 * 
 */
@JsonInclude(Include.NON_EMPTY)
public class UserSocialVo implements Serializable {

	private static final long serialVersionUID = 5942541464099865792L;

	private Long id; // 主键
	private Long userId; // 内部用户Id
	@NotBlank(message = "{userSocial.socialId.blank}", groups = { LoginGroup.class, BindGroup.class })
	private String socialId; // 社交用户Id
	private Long createTime; // 创建时间

	public UserSocialVo() {

	}
	
	public static UserSocialVo createBySocialId(String socialId) {
		UserSocialVo usv = new UserSocialVo();
		usv.socialId = socialId;
		return usv;
	}
	
	public static UserSocialVo createByUserId(Long userId) {
		UserSocialVo usv = new UserSocialVo();
		usv.userId = userId;
		return usv;
	}
	
	public static UserSocialVo createByAll(Long userId, String socialId) {
		UserSocialVo usv = new UserSocialVo();
		usv.userId = userId;
		usv.socialId = socialId;
		return usv;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}
