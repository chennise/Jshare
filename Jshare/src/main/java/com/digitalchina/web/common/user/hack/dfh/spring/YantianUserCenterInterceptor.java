package com.digitalchina.web.common.user.hack.dfh.spring;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;

import com.digitalchina.cptl.remote.interceptor.RemoteServiceInterceptor;
import com.digitalchina.cptl.remote.message.ServiceMessageExt;
import com.digitalchina.cptl.service.context.CspServiceContext;
import com.digitalchina.cptl.service.context.CspServiceContextImpl;
import com.digitalchina.frame.exception.BusinessException;
import com.digitalchina.frame.message.ServiceMessage;
import com.digitalchina.frame.security.AuthUser;
import com.digitalchina.frame.utils.TranConfigHelper.ServiceConfig;
import com.digitalchina.web.common.util.http.Request;
import com.digitalchina.web.common.util.http.RequestExceptionHandler;
import com.digitalchina.web.common.util.json.JsonHandler;

/**
 * 类型描述：<br/>
 * 用户中心拦截器
 * 
 * @createTime 2016年5月5日
 * @author maiwj
 * 
 */
public class YantianUserCenterInterceptor implements RemoteServiceInterceptor {

	private final static String SUCCESS_CODE = "000000";
	private final static String ILLEGAL_CODE = "444444";

	private String usercenterApiUrl = "https://ytsso.scity.cn/user/info"; // 用户中心API地址
	private String userCookieTokenName = "JUSERTOKEN"; // 用户CookieToken名
	private String proxyUrl; // 代理地址
	private Integer proxyPort; // 代理端口

	public void setUsercenterApiUrl(String usercenterApiUrl) {
		this.usercenterApiUrl = usercenterApiUrl;
	}

	public void setUserCookieTokenName(String userCookieTokenName) {
		if (!isBlank(userCookieTokenName)) {
			this.userCookieTokenName = userCookieTokenName;
		}
	}

	public void setProxyUrl(String proxyUrl) {
		this.proxyUrl = proxyUrl;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = Integer.valueOf(proxyPort);
	}

	private boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 方法描述：<br/>
	 * 查找用户令牌
	 * 
	 * @createTime 2016年7月6日
	 * @author maiwj
	 *
	 * @param request
	 * @return
	 */
	private String findUserToken(HttpServletRequest request) {
		String userToken = request.getParameter(userCookieTokenName);

		if (isBlank(userToken)) {
			Cookie[] cookies = request.getCookies();
			if (cookies != null && cookies.length > 0) {
				for (Cookie cookie : cookies) {
					if (userCookieTokenName.equals(cookie.getName())) {
						userToken = cookie.getValue();
						break;
					}
				}
			}
		}

		return userToken;
	}

	/**
	 * 方法描述：<br/>
	 * 创建请求配置
	 * 
	 * @createTime 2016年7月6日
	 * @author maiwj
	 *
	 * @return
	 */
	private RequestConfig createRequestConfig() {
		if (isBlank(proxyUrl) && proxyPort != null) {
			return RequestConfig.custom().setProxy(new HttpHost(proxyUrl, proxyPort)).build();
		}
		return null;
	}

	@Override
	public boolean before(ServiceMessage serviceMessageExt, CspServiceContext cspServiceContextImpl, Object springbean,
			Method tranServiceMethod) throws Exception {

		// 1、通过检查ServiceMessage类来获知当前方法是否需要使用用户信息
		final ServiceMessageExt sme = (ServiceMessageExt) serviceMessageExt;
		final ServiceConfig serviceConfig = sme.getServiceConfig();
		final CspServiceContextImpl csci = (CspServiceContextImpl) cspServiceContextImpl;
		final String userToken = findUserToken(csci.getRequest());

		switch (serviceConfig.getAccessType()) {
		case Protected: {
			if (isBlank(userToken)) {
				throw new BusinessException(ILLEGAL_CODE, "Service[" + serviceConfig.getName()
						+ "] is protected, but YantianUserCenterInterceptor can`t find login cookie");
			} else {
				HttpResponse response = Request.sync().setHttpResponseBodyType(ContentType.APPLICATION_JSON)
						.setConfig(createRequestConfig()).setExceptionHanlder(new RequestExceptionHandler() {

							@Override
							public void deal(Throwable t, HttpRequestBase request, HttpResponse response,
									boolean neverCloseResponse) {
								throw new BusinessException(ILLEGAL_CODE,
										"Service[" + serviceConfig.getName()
												+ "] is protected, but YantianUserCenterInterceptor call Usercenter["
												+ usercenterApiUrl + "] fail",
										t);
							}

						}).get(usercenterApiUrl + "?" + userCookieTokenName + "=" + userToken).response();

				try {
					Map<String, Object> jsonResult = JsonHandler.fromJson(response.getEntity().getContent(), Map.class);

					if (SUCCESS_CODE.equals(jsonResult.get("statusCode"))) { // 登录成功，获取到用户的信息
						@SuppressWarnings("unchecked")
						AuthUser au = new AuthUser(userToken, (Map<String, Object>) jsonResult.get("data"));
						csci.setAuthUser(au);
					} else {
						throw new BusinessException(ILLEGAL_CODE,
								"Service[" + serviceConfig.getName()
										+ "] is protected, but YantianUserCenterInterceptor call Usercenter["
										+ usercenterApiUrl + "] fail with response [ " + jsonResult + " ]");
					}
				} catch (Exception e) {
					throw new BusinessException(ILLEGAL_CODE, "Service[" + serviceConfig.getName()
							+ "] is protected, but YantianUserCenterInterceptor response fail", e);
				}
			}

			break;
		}
		case LazyProtected: {
			AuthUser au = null;
			if (isBlank(userToken)) {
				au = new AuthUser(); // 空的用户信息
			} else {
				au = new AuthUser(userToken, usercenterApiUrl, userCookieTokenName, createRequestConfig());
			}
			csci.setAuthUser(au);

			break;
		}
		default:
			csci.setAuthUser(new AuthUser());
		}
		return true;
	}

	@Override
	public boolean after(ServiceMessage serviceMessageExt, CspServiceContext cspServiceContextImpl, Object springbean,
			Method tranServiceMethod) throws Exception {
		return true;
	}

}
