package com.digitalchina.web.common.share.micromsg.service.impl;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalchina.web.common.share.bussiness.CircleManager;
import com.digitalchina.web.common.share.bussiness.UserBindManager;
import com.digitalchina.web.common.share.micromsg.MicroMessage;
import com.digitalchina.web.common.share.micromsg.req.ImageMessageRequest;
import com.digitalchina.web.common.share.micromsg.req.ScanSubscribeMessageRequest;
import com.digitalchina.web.common.share.micromsg.req.TextMessageRequest;
import com.digitalchina.web.common.share.micromsg.req.VoiceMessageRequest;
import com.digitalchina.web.common.share.micromsg.resp.TextMessageResponse;
import com.digitalchina.web.common.share.micromsg.service.IAutoResponseManager;
import com.digitalchina.web.common.share.vo.CircleVo;
import com.digitalchina.web.common.share.vo.UserVo;
import com.digitalchina.web.common.util.config.PropertiesHandler;
import com.digitalchina.web.common.util.lang.ObjectHandler;


/**
 * 自动回复服务类
 * @author ZRD
 *
 */
@Service
@SuppressWarnings({"serial","unchecked"})
public class AutoResponseManager implements IAutoResponseManager{

	private final static Logger LOG = LogManager.getLogger(AutoResponseManager.class);
	private String userInfoUrl = PropertiesHandler.getProperty("userinfo.url");
	
	@Autowired
	private UserBindManager userBindManager;
	@Autowired
	private CircleManager circleManager;

	/**
	 * 自动回复的消息处理
	 * 由用户发送消息到公众号，公众号对其作出响应。
	 * update 是为了让该方法出现事务
	 * @param request
	 * @return
	 */
	public List<MicroMessage> updateProcessRequestMessage(HttpServletRequest request) {
		
		ArrayList<MicroMessage> messageList = new ArrayList<MicroMessage>();
		MicroMessage message = this.updateParse(request);
		if(message != null) {
			if(message instanceof ScanSubscribeMessageRequest) {
				ScanSubscribeMessageRequest scanMsg = (ScanSubscribeMessageRequest) message;
				
				UserVo findUser = null;
				CircleVo circle = null;
				
				if(scanMsg.getScene_id()!=null){
					findUser = userBindManager.findBySocailId(scanMsg.getFromUserName());
					circle = circleManager.findVo(scanMsg.getScene_id());
					
					if(ObjectHandler.isEmpty(findUser)){
						findUser = UserVo.createBySocialId(scanMsg.getFromUserName());
						findUser.getCircles().add(circle);
						userBindManager.doSave(findUser);
					} else {
						List<CircleVo> currentCircles = findUser.getCircles();
						List<CircleVo> circlesToSave = new ArrayList<CircleVo>();
						boolean isExist = false;
						for(CircleVo circleVo:currentCircles){
							if(StringUtils.equals(circleVo.getId(), scanMsg.getScene_id())){
								isExist = true;
							}
						}
						if(!isExist){
							circlesToSave.add(circle);
							circlesToSave.addAll(currentCircles);
							findUser.setCircles(circlesToSave);
							userBindManager.doModify(findUser);
						}
					}
				}
				
				String content = null;
				if(!scanMsg.getIsScene() && scanMsg.getScene_id()==null){ // 刚关注公众号，没带任何的参数
					content = "哟，客官！碳闲置终于等到你了。\n"
							+ "在碳闲置，你的朋友圈就是你的闲置圈，轻松分享买卖你社交圈里朋友间的闲置\n"
							+ "\n"
							+ "听说只要扫一扫闲置圈的二维码就能进入专属的闲置圈。\n"
							+ "只有在闲置圈里的好友才可以分享进入二维码，赶紧找找你的小伙伴吧。\n"
							+ "\n"
							+ "闲置在朋友圈里会流动地更快哦。\n"
							+ "点此 <a href=\""+ userInfoUrl +"\">完善个人信息</a>";			
				}else if(!scanMsg.getIsScene() && scanMsg.getScene_id()!=null){ // 刚关注公众号，且带有参数二维码
					content = "欢迎加入【"+ circle.getName() +"】，碳闲置终于等到你。\n"
							+ "听说扫了闲置圈的二维码就已经进入专属的闲置圈。\n"
							+ "点击【寻找闲置】，可以查看只属于我们的闲置品。\n"
							+ "\n"
							+ "在碳闲置，你的朋友圈就是你的闲置圈，轻松分享买卖你社交圈里朋友间的闲置。\n"
							+ "闲置在朋友圈里会流动地更快哦。\n"
							+ "\n"
							+ "点此 <a href=\""+ userInfoUrl +"\">完善个人信息</a>";	
				}else if(scanMsg.getIsScene() && scanMsg.getScene_id()!=null){ // 已关注公众号，且带有参数二维码
					content = "哟，客官！你终于进来【"+ circle.getName() +"】。\n"
							+ "在【寻找闲置】里有只属于我们的闲置品哦。\n"
							+ "\n"
							+ "在碳闲置，可以轻松分享买卖你社交圈里朋友间的闲置。\n"
							+ "闲置在朋友圈里会流动地更快哦。";	
				}
				
				TextMessageResponse textMessageResponse = new TextMessageResponse(content);
				textMessageResponse.setFromUserName(message.getToUserName());
				textMessageResponse.setToUserName(message.getFromUserName());
				textMessageResponse.setCreateTime(new Date().getTime());
				
				messageList.add(textMessageResponse);
			}
		}
		// 组装Message集合List
		return messageList;
	}
	
	/**
	 * 解析用户发送过来的消息。
	 * 该方法可以解析成：
	 * TextMessageRequest、ImageMessageRequest、VoiceMessageRequest还有事件推送等消息类型
	 * @param request
	 * @return
	 */
	public MicroMessage updateParse(HttpServletRequest request) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			InputStream inputStream = request.getInputStream();
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			Element root = document.getRootElement();
			List<Element> elementList = root.elements();
			// 遍历所有子节点
			for (Element e : elementList) {
				map.put(e.getName(), e.getText());
			}
			String msgType = map.get("MsgType");
			MicroMessage message = null;
			if(MicroMessage.MESSAGE_REQ_TYPE_TEXT.equals(msgType)) {
				// 文本消息
				TextMessageRequest textMsg = new TextMessageRequest();
				textMsg.setContent(map.get("Content"));
				message = textMsg;
				buildBasicMessageData(map, msgType, message);
			} else if(MicroMessage.MESSAGE_REQ_TYPE_IMAGE.equals(msgType)) {
				// 图片消息
				ImageMessageRequest imageMsg = new ImageMessageRequest();
				imageMsg.setPicUrl(map.get("PicUrl"));
				imageMsg.setMediaId(map.get("PicUrl"));
				message = imageMsg;
				buildBasicMessageData(map, msgType, message);
			} else if(MicroMessage.MESSAGE_REQ_TYPE_VOICE.equals(msgType)) {
				// 音频消息
				VoiceMessageRequest voiceMsg = new VoiceMessageRequest();
				voiceMsg.setMediaId(map.get("MediaId"));
				voiceMsg.setFormat(map.get("Format"));
				message = voiceMsg;
				buildBasicMessageData(map, msgType, message);
			} else if(MicroMessage.MESSAGE_REQ_TYPE_EVENT.equals(msgType)) {
				String eventType = map.get("Event");
				if (eventType.equals(MicroMessage.EVENT_TYPE_SUBSCRIBE)) {
					// 新的用户订阅:
					// TODO 新用户入库和发送订阅推送
					String fromUserName = map.get("FromUserName");
					String EventKey = map.get("EventKey");
					if(StringUtils.isEmpty(EventKey)){
						ScanSubscribeMessageRequest scanSubscribeMessageRequest = new ScanSubscribeMessageRequest();
						scanSubscribeMessageRequest.setScene_id(null);
						scanSubscribeMessageRequest.setFromUserName(fromUserName);
						scanSubscribeMessageRequest.setIsScene(false);
						message = scanSubscribeMessageRequest;
						buildBasicMessageData(map, msgType, message);
					}else if(EventKey.startsWith("qrscene_")) {
						String scene_id = StringUtils.substringAfter(EventKey, "qrscene_");
						ScanSubscribeMessageRequest scanSubscribeMessageRequest = new ScanSubscribeMessageRequest();
						scanSubscribeMessageRequest.setScene_id(scene_id);
						scanSubscribeMessageRequest.setFromUserName(fromUserName);
						scanSubscribeMessageRequest.setIsScene(false);
						message = scanSubscribeMessageRequest;
						buildBasicMessageData(map, msgType, message);
					}
				} else if("SCAN".equals(eventType)) {
					String fromUserName = map.get("FromUserName");
					String sceneid = map.get("EventKey");
					ScanSubscribeMessageRequest scanSubscribeMessageRequest = new ScanSubscribeMessageRequest();
					scanSubscribeMessageRequest.setScene_id(sceneid);
					scanSubscribeMessageRequest.setFromUserName(fromUserName);
					scanSubscribeMessageRequest.setIsScene(true);
					message = scanSubscribeMessageRequest;
					buildBasicMessageData(map, msgType, message);
				} 
			}
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void buildBasicMessageData(Map<String, String> map, String msgType, MicroMessage message) {
		message.setCreateTime(new Date().getTime());
		message.setFromUserName(map.get("FromUserName"));
		message.setToUserName(map.get("ToUserName"));
		message.setMsgId(map.get("MsgId"));
		message.setMsgType(msgType);
	}

}
