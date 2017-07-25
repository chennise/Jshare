package com.digitalchina.web.common.share.micromsg.req;

import com.digitalchina.web.common.share.micromsg.MicroMessage;


/**
 * 请求消息 @@ 文本消息
 * @author ZRD
 *
 */
public class TextMessageRequest extends MicroMessage {

	/**
	 * 消息内容
	 */
	private String Content;

	@Override
	public String getXml() {
		
		return null;
	}
	
	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
