package com.digitalchina.web.common.user.spring;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.digitalchina.web.common.user.business.UserCache;
import com.digitalchina.web.common.user.business.UserToken;
import com.digitalchina.web.common.user.vo.UserVo;
import com.digitalchina.web.common.util.json.JsonHandler;

/**
 * 类型描述：<br/>
 * 单点登录一次认证拦截器
 * 
 * @createTime 2016年7月2日
 * @author maiwj
 * 
 */
public class SSOOneAuthorizationInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserCache userCache;
	@Autowired
	private UserToken userToken;

	/**
	 * 方法描述：<br/>
	 * 从Cookie中获取令牌
	 * 
	 * @createTime 2016年7月1日
	 * @author maiwj
	 *
	 * @param servletRequest
	 * @return
	 */
	private String getTokenFromCookie(HttpServletRequest servletRequest) {
		String userLoginedToken = null;

		Cookie[] cookies = servletRequest.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (StringUtils.equals(cookie.getName(), SSOConstants.USER_TOKEN_COOKIE_NAME)) {
					userLoginedToken = cookie.getValue();
					break;
				}
			}
		}
		return userLoginedToken;
	}

	/**
	 * 方法描述：<br/>
	 * 附件令牌到Cookie中
	 * 
	 * @createTime 2016年7月7日
	 * @author maiwj
	 *
	 * @param response
	 * @param userLoginedToken
	 */
	private HttpServletResponse attachCookieWithToken(HttpServletResponse response, String userLoginedToken) {
		Cookie userLoginedTokenCookie = new Cookie(SSOConstants.USER_TOKEN_COOKIE_NAME, userLoginedToken);
		String userLoginedTokenCookieDomain = SSOConstants.USER_TOKEN_COOKIE_DOMAIN;
		String userLoginedTokenCookiePath = SSOConstants.USER_TOKEN_COOKIE_PATH;
		if (StringUtils.isNotBlank(userLoginedTokenCookieDomain)) {
			userLoginedTokenCookie.setDomain(userLoginedTokenCookieDomain);
		}
		if (StringUtils.isNotBlank(userLoginedTokenCookiePath)) {
			userLoginedTokenCookie.setPath(userLoginedTokenCookiePath);
		}
		userLoginedTokenCookie.setSecure(SSOConstants.USER_TOKEN_COOKIE_HTTPS);
		userLoginedTokenCookie.setHttpOnly(true);

		response.addCookie(userLoginedTokenCookie);
		
		return response;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		boolean isGetMethod = StringUtils.equalsIgnoreCase("GET", request.getMethod());
		boolean isLoginPath = request.getRequestURI().endsWith("/login");

		if (!isGetMethod || !isLoginPath) {
			return true;
		}

		String userLoginedToken = request.getParameter(SSOConstants.USER_TOKEN_COOKIE_NAME);
		if (StringUtils.isBlank(userLoginedToken)) {
			userLoginedToken = getTokenFromCookie(request);
		}

		if (StringUtils.isNotBlank(userLoginedToken)) { // token验证成功
			Long userId = userToken.export(userLoginedToken);
			if (userCache.has(userId)) { // 用户已经登录
				String redirectUrl = request.getParameter("redirect");
				if (StringUtils.isNotBlank(redirectUrl)) {
					if (redirectUrl.lastIndexOf("?") == -1) {
						redirectUrl += "?" + SSOConstants.USER_TOKEN_COOKIE_NAME + userLoginedToken;
					} else {
						redirectUrl += "&" + SSOConstants.USER_TOKEN_COOKIE_NAME + userLoginedToken;
					}
					attachCookieWithToken(response, userLoginedToken).sendRedirect(redirectUrl);
				} else {
					UserVo user = userCache.get(userId);
					user.setPassword(null);
					response.getWriter().write(JsonHandler.Jackson.toJson(user));
				}
				return false;
			}
		}
		String loginUrl = request.getParameter("login");
		if (StringUtils.isBlank(loginUrl)) {
			loginUrl = "login.jsp";
		}
		response.sendRedirect(loginUrl);

		return false;
	}

}
