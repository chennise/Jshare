package com.digitalchina.web.common.share.micromsg.resp;

import com.digitalchina.web.common.share.micromsg.MicroMessage;
import com.digitalchina.web.common.share.utils.XmlFormaterUtil;


/**
 * 响应消息 @@ 文本消息
 * @author ZRD
 *
 */
public class TextMessageResponse extends MicroMessage{

	/**
	 * 文本消息内容
	 */
	private String Content;
	
	/**
	 * 构造函数。初始化响应文本消息的相关信息
	 * @param Content
	 */
	public TextMessageResponse(String Content) {
		this.Content = Content;
		super.setMsgType(MicroMessage.MESSAGE_RESP_TYPE_TEXT);
		super.setCreateTime(System.currentTimeMillis());
	}
	
	/**
	 * 获取一个响应文本消息的字符串.
	 * 注：在获取之前必须设置FromUserName和ToUserName的值，通过调用父类的set方法。
	 * @return
	 */
	public String getXml() {
		return XmlFormaterUtil.textMessageToXml(this);
	}
	
	public String getJson() {
		return null;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
