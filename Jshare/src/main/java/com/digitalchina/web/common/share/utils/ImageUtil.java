package com.digitalchina.web.common.share.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.stream.FileImageOutputStream;

import org.springframework.util.Base64Utils;

public class ImageUtil {

	/**
	 * base64字符串转化成图片
	 * 
	 * @param imgFilePath
	 * @param imgStr
	 * @return
	 */
	public static boolean GenerateImage(String imgFilePath, String imgStr) { // 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) { // 图像数据为空
			return false;
		}
		try {
			imgStr = imgStr.substring(imgStr.indexOf("base64,") + 7);
			// Base64解码
			byte[] b = Base64Utils.decodeFromString(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// byte数组到图片
	public static void byte2image(byte[] data, String path) {
		if (data.length < 3 || path.equals(""))
			return;
		try {
			FileImageOutputStream imageOutput = new FileImageOutputStream(
					new File(path));
			imageOutput.write(data, 0, data.length);
			imageOutput.close();
			System.out.println("Make Picture success,Please find image in "
					+ path);
		} catch (Exception ex) {
			System.out.println("Exception: " + ex);
			ex.printStackTrace();
		}
	}
	
	/**
	 * 下载文件到本地
	 * @param urlString
	 * @param filename
	 * @throws Exception
	 */
	public static void download(String urlString, String filename) throws Exception {
	    // 构造URL
	    URL url = new URL(urlString);
	    // 打开连接
	    URLConnection con = url.openConnection();
	    // 输入流
	    InputStream is = con.getInputStream();
	    // 1K的数据缓冲
	    byte[] bs = new byte[1024];
	    // 读取到的数据长度
	    int len;
	    // 输出的文件流
	    OutputStream os = new FileOutputStream(filename);
	    // 开始读取
	    while ((len = is.read(bs)) != -1) {
	      os.write(bs, 0, len);
	    }
	    // 完毕，关闭所有链接
	    os.close();
	    is.close();
	}   

}
