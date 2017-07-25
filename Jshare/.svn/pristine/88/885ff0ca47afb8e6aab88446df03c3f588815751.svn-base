package com.digitalchina.web.common.user.spring;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.digitalchina.web.common.user.api.IUserManager;
import com.digitalchina.web.common.user.api.exception.UserWithoutLoginException;
import com.digitalchina.web.common.user.business.UserCache;
import com.digitalchina.web.common.user.business.UserToken;

/**
 * 
 * 描述：</br>
 * 登录解析器
 * 
 * @author wukj
 * @createTime 2016年3月3日
 */
public class SSOUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private UserCache userCache;
	@Autowired
	private UserToken userToken;
	@Autowired
	private IUserManager userManager;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return ClassUtils.isAssignable(SSOUser.class, parameter.getParameterType());
	}

	/**
	 * 
	 * 描述：</br>
	 * 对于需要登录的接口(在请求参数中，用SSOUser或者SSOIds注解标识该接口需要登录)
	 * 如果已经登录，则从session中获取用户信息，放入user参数 如果未登录，提示用户需要登录
	 * 
	 * @param parameter
	 * @param mavContainer
	 * @param webRequest
	 * @param binderFactory
	 * @return
	 * @throws Exception
	 * @author wukj
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		// 获取JUSERTOKEN
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String userLoginedToken =  request.getParameter(SSOConstants.USER_TOKEN_COOKIE_NAME);
		if (StringUtils.isBlank(userLoginedToken)) {
			Cookie[] cookies = request.getCookies();
			Cookie userLoginedTokenCookie = null;
			if (cookies != null) {
				for (Cookie cookie : request.getCookies()) {
					if (StringUtils.equals(cookie.getName(), SSOConstants.USER_TOKEN_COOKIE_NAME)) {
						userLoginedTokenCookie = cookie;
						userLoginedToken = userLoginedTokenCookie.getValue();
						break;
					}
				}
			}
		}
		
		// 获取用户
		boolean isLazy = parameter.getParameterAnnotation(Lazy.class) != null;
		if (isLazy) {
			if (StringUtils.isNotBlank(userLoginedToken)) {
				Long userId = userToken.export(userLoginedToken); // 导出用户
				if (userCache.more(userId, 900)) {
					return new SSOUser(userId, userCache, userManager);
				}
			}
			return null;
		} else if (StringUtils.isNotBlank(userLoginedToken)) {
			Long userId = userToken.export(userLoginedToken); // 导出用户
			if (userCache.more(userId, 300)) {
				return new SSOUser(userId, userCache, userManager);
			}
		}
		
		throw new UserWithoutLoginException(); // 用户未登录
	}
	
}
