package com.digitalchina.web.common.user.business;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalchina.web.common.cache.impl.XMemcachedCache;
import com.digitalchina.web.common.user.api.Constants;
import com.digitalchina.web.common.util.config.PropertiesHandler;
import com.digitalchina.web.common.util.encode.HexHandler;
import com.digitalchina.web.common.util.encryption.EncryptionHandler;
import com.digitalchina.web.common.util.http.Request;
import com.digitalchina.web.common.util.lang.DateHandler;

/**
 * 类型描述：<br/>
 * 验证码管理器
 * 
 * @createTime 2016年6月25日
 * @author maiwj
 *
 */
@Service
public class UserVerifyCode {

	private final static Logger LOG = LogManager.getLogger(UserVerifyCode.class);

	private static final Integer LIMIT_MS = 1000 * 60 * 30; // token 半小时内可持续
	private static final String SPLITE = "@";
	private static final String VERIFY_CODE_TOKEN_KEY_PREFIX = "_verify_code_token_";
	private static final byte[] SMS_AES_KEY = HexHandler
			.decode(PropertiesHandler.getProperty("sms.aes.key", "e4d8782d462c532e79a9f88bde539a11"));
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("000000");
	private static final String SMS_URL = PropertiesHandler.getProperty("sms.url");
	private static final String SMS_ACCOUNT = PropertiesHandler.getProperty("sms.account");
	private static final String SMS_PSWD = PropertiesHandler.getProperty("sms.pswd");
	@Autowired
	private XMemcachedCache cache;

	/**
	 * 方法描述：<br/>
	 * 获取验证码发送令牌
	 * 
	 * @createTime 2016年6月25日
	 * @author maiwj
	 *
	 * @param phone
	 *            手机号
	 * @param businessType
	 *            业务类型
	 * @return
	 */
	public String getToken(Long phone, String businessType) {
		String verifyCode = DECIMAL_FORMAT.format((Math.random()) * (Math.pow(10, 6) - 1));

		// TIMESTAMP@PHONE@BUSINESS_TYPE@VERIFY_CODE
		String tokenRaw = new StringBuilder().append(DateHandler.getTimeInMillis() + LIMIT_MS).append(SPLITE)
				.append(phone).append(SPLITE).append(businessType).append(SPLITE).append(verifyCode).toString();

		return EncryptionHandler.key(tokenRaw, SMS_AES_KEY).encryptAES(); // AES(phone@businessType@timestamp)
	}

	/**
	 * 方法描述：<br/>
	 * 发送验证码
	 * 
	 * @createTime 2016年6月25日
	 * @author maiwj
	 *
	 * @param token
	 *            发送令牌
	 * @return
	 */
	public boolean send(String token) {

		if (StringUtils.isBlank(token)) {
			return false;
		}

		// 解密token
		String tokenRaw = EncryptionHandler.key(token, SMS_AES_KEY).decryptAES(); // TIMESTAMP@PHONE@BUSINESS_TYPE@VERIFY_CODE
		String[] tokenRaw_ = tokenRaw.split(SPLITE);
		if (tokenRaw_.length != 4) {
			return false;
		}

		// 获取参数
		String timestamp = tokenRaw_[0];
		String phone = tokenRaw_[1];
		String businessType = tokenRaw_[2];
		String verifyCode = tokenRaw_[3];
		String content = null;

		String cacheKey = VERIFY_CODE_TOKEN_KEY_PREFIX + phone + businessType;

		// 如果令牌已经超时，将不再产生短信
		if (System.currentTimeMillis() > Long.valueOf(timestamp)) {
			cache.delete(cacheKey);
			return false;
		}

		// 如果已经发送了短信验证码
		String toCacheVerifyCode = verifyCode;
		if (StringUtils.equals(businessType, Constants.BUSINESS_TYPE_MESSAGE_LOGIN)) {
			toCacheVerifyCode = EncryptionHandler.messageDigest(verifyCode, null).encryptSHA1();
		}
		if (StringUtils.equals((String) cache.touch(cacheKey, 600), toCacheVerifyCode)) {
			return true;
		}

		if (StringUtils.equals(businessType, Constants.BUSINESS_TYPE_REGISTER)) {
			content = "您注册的短信验证码是" + verifyCode + "（有效期10分钟），请勿向任何人泄漏，如非本人操作请忽略。";
		} else if (StringUtils.equals(businessType, Constants.BUSINESS_TYPE_RESET_PWD)) {
			content = "您修改密码的短信验证码是" + verifyCode + "（有效期10分钟），请勿向任何人泄漏，如非本人操作请忽略。";
		} else if (StringUtils.equals(businessType, Constants.BUSINESS_TYPE_SOCIAL)) {
			content = "您绑定的短信验证码是" + verifyCode + "（有效期10分钟），请勿向任何人泄漏，如非本人操作请忽略。";
		} else if (StringUtils.equals(businessType, Constants.BUSINESS_TYPE_MESSAGE_LOGIN)) {
			content = "您登录的短信验证码是" + verifyCode + "（有效期10分钟），请勿向任何人泄漏，如非本人操作请忽略。";
		} else {
			return false;
		}

		try {
			String result = send(SMS_ACCOUNT, SMS_PSWD, phone, content, true);
			if (result.startsWith("-")) {
				LOG.error("verifycode:{} send to {}, result is {}", verifyCode, phone, result);
			} else {
				if (LOG.isInfoEnabled()) {
					LOG.info("verifycode:{} send to {}", verifyCode, phone);
				}
				cache.set(cacheKey, toCacheVerifyCode, 600);
				return true;
			}
		} catch (Exception e) {
			LOG.error(e);
		}

		return false;
	}

	/**
	 * 方法描述：<br/>
	 * 检验验证码
	 * 
	 * @createTime 2016年6月25日
	 * @author maiwj
	 *
	 * @param phone
	 *            手机号
	 * @param expectVerifyCode
	 *            期望的验证码
	 * @param businessType
	 *            业务类型
	 * @return
	 */
	public boolean check(Long phone, String expectVerifyCode, String businessType) {
		if (phone != null && StringUtils.isNotBlank(expectVerifyCode)) {

			String cacheKey = VERIFY_CODE_TOKEN_KEY_PREFIX + phone + businessType;

			boolean result = StringUtils.equals((String) cache.get(cacheKey), expectVerifyCode);
			if (result) {
				cache.delete(cacheKey);
			}
			return result;
		}
		return false;
	}

	/**
	 * 方法描述：<br/>
	 * 发送短信
	 * 
	 * @createTime 2016年10月6日
	 * @author LJn
	 * 
	 * @param account 账号
	 * @param pswd 密码
	 * @param mobile 手机号码，多个号码使用","分割
	 * @param msg 短信内容
	 * @param needstatus 是否需要状态报告，需要true，不需要false
	 * @return 返回值定义参见HTTP协议文档
	 */
	public static String send(String account, String pswd, String mobile, String msg,
			boolean needstatus){
		
		Map<String, Object> requestParams = new HashMap<>(5);
		requestParams.put("account", account);
		requestParams.put("pswd", pswd);
		requestParams.put("mobile", mobile);
		requestParams.put("needstatus", String.valueOf(needstatus));
		requestParams.put("msg", msg);
		
		return Request.sync()
			.addHttpRequestParams(requestParams)
			.get(SMS_URL)
			.responseString();
		
	}
}
