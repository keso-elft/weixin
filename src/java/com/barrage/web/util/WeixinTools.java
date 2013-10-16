package com.barrage.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.barrage.web.server.message.request.InMessage;
import com.barrage.web.server.message.response.Articles;
import com.barrage.web.server.message.response.OutMessage;
import com.thoughtworks.xstream.XStream;

public final class WeixinTools {

	/**
	 * 解析请求消息
	 * @param httpReq
	 * @return
	 */
	public static InMessage parseRequest(HttpServletRequest httpReq) {
		try {
			ServletInputStream in = httpReq.getInputStream();
			// 转换微信post过来的xml内容
			XStream xs = XStreamFactory.init(false);
			xs.alias("xml", InMessage.class);
			String xmlMsg = inputStream2String(in);
			InMessage msg = (InMessage) xs.fromXML(xmlMsg);

			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 输出加码并应答
	 * @param out
	 * @param httpResp
	 */
	public static void printOutMessage(OutMessage out, HttpServletResponse httpResp) {
		httpResp.setCharacterEncoding("UTF-8");
		httpResp.setContentType("text/xml");

		// 把发送发送对象转换为xml输出
		try {
			XStream xs = XStreamFactory.init(false);
			xs = XStreamFactory.init(false);
			xs.alias("xml", OutMessage.class);
			xs.alias("item", Articles.class);
			xs.toXML(out, httpResp.getWriter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 验证签名
	 * @param token
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static final boolean checkSignature(String token, String signature, String timestamp, String nonce) {
		List<String> params = new ArrayList<String>();
		params.add(token);
		params.add(timestamp);
		params.add(nonce);
		Collections.sort(params, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		String temp = params.get(0) + params.get(1) + params.get(2);
		return SHA1.encode(temp).equalsIgnoreCase(signature);
	}

	private static final String inputStream2String(InputStream in) throws UnsupportedEncodingException, IOException {
		if (in == null)
			return "";

		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n, "UTF-8"));
		}
		return out.toString();
	}
}
