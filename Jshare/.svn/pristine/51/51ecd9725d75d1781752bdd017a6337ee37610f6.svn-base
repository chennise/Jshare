package com.digitalchina.web.common.user.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.digitalchina.web.common.user.vo.validation.BindGroup;
import com.digitalchina.web.common.user.vo.validation.LoginGroup;
import com.digitalchina.web.common.user.vo.validation.PasswordModifyGroup;
import com.digitalchina.web.common.user.vo.validation.RegisteGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * 描述：</br>
 * 用户信息
 * 
 * @author LJn
 * @createTime 2016年10月4日
 */
@JsonInclude(Include.NON_NULL)
public class UserVo implements Serializable {

	private static final long serialVersionUID = 3148494018271948205L;

	private Long id; // 用户id
	private String nickname;// 昵称
	@NotNull(message = "{user.phone.null}", groups = { LoginGroup.class, RegisteGroup.class, PasswordModifyGroup.class,
			BindGroup.class })
	private Long phone; // 电话号码
	@NotBlank(message = "{user.password.blank}", groups = { LoginGroup.class, RegisteGroup.class,
			PasswordModifyGroup.class })
	private String password; // 用户密码
	private Integer sex; // 性别
	private String imageUrl; // 头像
	private Long registerTime; // 注册时间
	private Integer available; // 是否有效

	@NotBlank(message = "{user.verifyCode.blank}", groups = { RegisteGroup.class, PasswordModifyGroup.class,
			BindGroup.class })
	private String verifyCode;// 手机验证码
	private String loginType; // 登录类型

	@NotEmpty(message = "{userSocial.socialId.blank}", groups = { BindGroup.class })
	private String socialId; // 社交Id

	public UserVo() {
	}

	/**
	 * 方法描述：<br/>
	 * 清理函数
	 * 
	 * @createTime 2016年8月8日
	 * @author LJn
	 *
	 * @param user
	 * @return
	 */
	public static UserVo clean(UserVo user) {

		user.setPassword(null);
		user.setAvailable(null);
		user.setRegisterTime(null);
		user.setVerifyCode(null);
		user.setLoginType(null);
		user.setSocialId(null);

		return user;
	}
	
	/**
	 * 方法描述：<br/>
	 * 通过手机和密码创建用户
	 * 
	 * @createTime 2016年8月12日
	 * @author LJn
	 *
	 * @param phone
	 * @param password
	 * @return
	 */
	public static UserVo create(Long phone, String password) {
		UserVo uv = new UserVo();

		uv.phone = phone;
		uv.password = password;

		return uv;
	}

	/**
	 * 方法描述：<br/>
	 * 通过密码创建用户
	 * 
	 * @createTime 2016年7月9日
	 * @author LJn
	 *
	 * @param id
	 * @param password
	 * @return
	 */
	public static UserVo createByPassword(Long id, String password) {
		UserVo uv = new UserVo();

		uv.id = id;
		uv.password = password;

		return uv;
	}

	/**
	 * 方法描述：<br/>
	 * 通过id创建
	 * 
	 * @createTime 2016年7月9日
	 * @author LJn
	 *
	 * @param id
	 * @return
	 */
	public static UserVo createById(Long id) {
		UserVo uv = new UserVo();
		uv.id = id;
		return uv;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Long registerTime) {
		this.registerTime = registerTime;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

}