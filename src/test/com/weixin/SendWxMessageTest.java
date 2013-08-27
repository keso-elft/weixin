package com.weixin;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class SendWxMessageTest {

	static int multiple = 2000;

	static byte[] b = new byte[1024];

	private static String token = "ELFT"; // TOKEN

	private static String timestamp = "1348831860"; // 时间戳

	private static String nonce = "1234567"; // 随机数

	private static String echostr = "abcd"; // 随机字符串

	public static void main(String[] args) {

		String url = "http://127.0.0.1:8080/weixin/weixin/ELFT";

		String resp1 = authWxMessage(url);
		System.out.println(resp1);

		// String xmlString = "<xml>" +
		// "<ToUserName><![CDATA[4422]]></ToUserName>"
		// + "<FromUserName><![CDATA[1000]]></FromUserName>" +
		// "<CreateTime>1348831860</CreateTime>"
		// + "<MsgType><![CDATA[text]]></MsgType>" +
		// "<Content><![CDATA[ 1 2]]></Content>" + "</xml>";
		//
		// String resp2 = submitWxMessage(url, xmlString);
		// System.out.println(resp2);
	}

	public static String authWxMessage(String url) {

		HttpClient http = new HttpClient();
		http.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		http.getParams().setSoTimeout(60000);

		String signature = SignatureUtil.signature(token, timestamp, nonce);

		url += "?signature=" + signature + "&timestamp=" + timestamp + "&nonce=" + nonce + "&echostr=" + echostr;

		GetMethod get = new GetMethod(url);

		try {

			get.setDoAuthentication(true);

			get.addRequestHeader("Content-type", "text/XML; charset=UTF-8");

			http.executeMethod(get);
			return get.getResponseBodyAsString();

		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}

	}

	public static String submitWxMessage(String url, String text) {

		HttpClient http = new HttpClient();
		http.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		http.getParams().setSoTimeout(60000);

		PostMethod post = new PostMethod(url);

		try {
			post.setDoAuthentication(true);

			@SuppressWarnings("deprecation")
			RequestEntity requestEntity = new StringRequestEntity(text);
			post.setRequestEntity(requestEntity);

			post.addRequestHeader("Content-type", "text/XML; charset=UTF-8");

			http.executeMethod(post);
			return post.getResponseBodyAsString();

		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		} finally {
			post.releaseConnection();
		}

	}
}