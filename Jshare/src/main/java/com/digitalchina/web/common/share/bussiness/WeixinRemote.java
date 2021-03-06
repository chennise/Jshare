package com.digitalchina.web.common.share.bussiness;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.digitalchina.web.common.cache.impl.XMemcachedCache;
import com.digitalchina.web.common.share.micromsg.service.impl.AutoResponseManager;
import com.digitalchina.web.common.share.vo.ShareRecordVo;
import com.digitalchina.web.common.util.config.PropertiesHandler;
import com.digitalchina.web.common.util.http.Request;

/**
 * 类型描述：<br/>
 * 微信服务
 * 
 * @createTime 2016年9月7日
 * @author lijin
 * 
 */
@Component
public class WeixinRemote {

	private final static Logger LOG = LogManager.getLogger(WeixinRemote.class);
	private final static String KEY_ACCESSTOKEN = "wx_api_accessToken";
	
	private final String wxAppID = PropertiesHandler.getProperty("wx.appID");
	private final String wxAppSecret = PropertiesHandler.getProperty("wx.appSecret");
	
	@Autowired
	private XMemcachedCache memCache;
	
	public String getAccessToken(){
		String access_token = memCache.get(KEY_ACCESSTOKEN);
		if(access_token==null){
			String response = Request.sync()
							.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxAppID + "&secret=" + wxAppSecret)
							.responseString();
			JSONObject json = JSONObject.parseObject(response);
			access_token = (String) json.get("access_token");
			memCache.set(KEY_ACCESSTOKEN, access_token);
		}
		return access_token;
	}
	
	/**
	 * 方法描述：<br/>
	 * 获取微信图片路径
	 * 
	 * @createTime 2016年11月8日
	 * @author LJn
	 * 
	 * @return
	 */
	public String getMediaUrl(String mediaId) {
		
		return "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
					+ getAccessToken() +"&media_id="+ mediaId;
	}
	
	/**
	 * 方法描述：<br/>
	 * 发送模板消息
	 * 
	 * @createTime 2016年11月4日
	 * @author LJn
	 * 
	 * @param socialId
	 * @param header
	 * @param recordVo
	 * @param status
	 * @return
	 */
	public boolean sendTemplateMessage(String socialId, ShareRecordVo recordVo, String url,
			String header, String status, String footer) {
		Map<String, Object> httpRequestParams= new HashMap<String, Object>(4);
		httpRequestParams.put("touser", socialId);
		httpRequestParams.put("template_id", "zuz9WiCrsbyWvcKTzKvyxeDhXgBH7h8iPwVe5lBmC7M");
		httpRequestParams.put("url", url);
			Map<String, Object> data= new HashMap<String, Object>(5);
				Map<String, Object> first= new HashMap<String, Object>(1);
				first.put("value", header);
			data.put("first", first);
				Map<String, Object> keyword1= new HashMap<String, Object>(1);
				keyword1.put("value", recordVo.getName());
			data.put("keyword1", keyword1);
				Map<String, Object> keyword2= new HashMap<String, Object>(1);
				keyword2.put("value", recordVo.getId());
			data.put("keyword2", keyword2);
				Map<String, Object> keyword3= new HashMap<String, Object>(1);
				keyword3.put("value", status);
			data.put("keyword3", keyword3);
				Map<String, Object> remark= new HashMap<String, Object>(1);
				remark.put("value", footer);
			data.put("remark", remark);
		httpRequestParams.put("data", data);
			
		Request.async()
				.addHttpRequestParams(httpRequestParams)
				.post("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+getAccessToken())
				.responseString();
		return true;
	}
}
