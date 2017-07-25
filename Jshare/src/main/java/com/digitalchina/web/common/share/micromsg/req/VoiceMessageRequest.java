package com.digitalchina.web.common.share.micromsg.req;

import com.digitalchina.web.common.share.micromsg.MicroMessage;

/**
 * 请求消息 @@ 语音消息
 * 
 * @author ZRD
 *
 */
public class VoiceMessageRequest extends MicroMessage{

	/**
	 * 语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
	 */
	private String MediaId;
	
	/**
	 * 语音格式，如amr，speex等
	 */
	private String Format;

	@Override
	public String getXml() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}
}
