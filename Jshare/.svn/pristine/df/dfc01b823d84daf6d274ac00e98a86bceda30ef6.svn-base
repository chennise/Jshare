package com.digitalchina.web.common.share.micromsg;

/**
 * 消息的基类
 * 包含两种：
 * 1. 请求消息（普通用户 -> 公众帐号）
 * 2. 响应消息（公众帐号 -> 普通用户）
 * @author LJn
 *
 */
public abstract class MicroMessage {
	
	private String ToUserName;		// 将消息发往何处
	private String FromUserName;	// 该消息来自哪里
	private String MsgType;			// 消息的类型
	private Long CreateTime;		// 消息创建时间戳
	private String MsgId;			// 请求消息的消息id
	private Integer FuncFlag;		// 位0x0001被标志时，星标刚收到的消息
	
	/**
	 * 返回消息类型：文本
	 */
	public static final String MESSAGE_RESP_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String MESSAGE_RESP_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String MESSAGE_RESP_TYPE_NEWS = "news";

	/**
	 * 请求消息类型：文本
	 */
	public static final String MESSAGE_REQ_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String MESSAGE_REQ_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String MESSAGE_REQ_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String MESSAGE_REQ_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String MESSAGE_REQ_TYPE_VOICE = "voice";

	/**
	 * 请求消息类型：推送
	 */
	public static final String MESSAGE_REQ_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";
	
	public abstract String getXml();

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long createTime) {
		CreateTime = createTime;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}

	public Integer getFuncFlag() {
		return FuncFlag;
	}

	public void setFuncFlag(Integer funcFlag) {
		FuncFlag = funcFlag;
	}
}
