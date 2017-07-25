package com.digitalchina.web.common.share.utils;

import java.io.InputStream;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.digitalchina.web.common.share.micromsg.resp.TextMessageResponse;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 消息工具类
 */
public class XmlFormaterUtil {

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
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
		// 释放资源
		/*inputStream.close();
		inputStream = null;*/
		return map;
	}

	/**
	 * 文本消息对象转换成XML
	 * 
	 * @param textMessage 文本消息对象
	 * @return XML
	 */
	public static String textMessageToXml(TextMessageResponse textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}
	
	/**
	 * 客服消息对象转换成XML
	 * 
	 * @param textMessage 文本消息对象
	 * @return XML
	 */
	/*public static String customerServiceMessageToXml(CustomerServiceMessageResponse serviceMessageResponse) {
		xstream.alias("xml", serviceMessageResponse.getClass());
		return xstream.toXML(serviceMessageResponse);
	}*/

	/**
	 * 音乐消息对象转换成XML
	 * 
	 * @param musicMessage 音乐消息对象
	 * @return XML
	 */
	/*public static String musicMessageToXml(MusicMessage musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}*/

	/**
	 * 图文消息对象转换成XML
	 * 
	 * @param newsMessage 图文消息对象
	 * @return XML
	 */
	/*public static String newsMessageToXml(NewsMessageResponse newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", Article.class);
		return xstream.toXML(newsMessage);
	}*/
	
	/**
	 * 将微信消息中的CreateTime转换成标准格式的时间（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param createTime 消息创建时间
	 * @return
	 */
	public static String formatTime(String createTime) {
		// 将微信传入的CreateTime转换成long类型，再乘以1000
		long msgCreateTime = Long.parseLong(createTime) * 1000L;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(msgCreateTime));
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 */
	public static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}
