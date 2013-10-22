package com.barrage.task;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.barrage.web.AccessToken;
import com.barrage.web.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GetAccessTokenTask {

	protected Logger log = LogManager.getLogger("weixinServer");

	protected String GET_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	public void run() {
		if (Constants.ACCESS_TOKEN == null || Constants.ACCESS_TOKEN.isExpire()) {
			AccessToken token = getAccessToken();

			if (token != null) {
				token.setGetTime(System.currentTimeMillis());
				Constants.ACCESS_TOKEN = token;
			} else {
				log.error("[GetAccessTokenTask] token get fail.");
			}
		}
	}

	private AccessToken getAccessToken() {
		try {
			Response response = Jsoup.connect(GET_URL).ignoreContentType(true).method(Method.GET).execute();
			String json = response.body();
			Gson gson = new Gson();
			return gson.fromJson(json, new TypeToken<AccessToken>() {
			}.getType());
		} catch (Exception e) {
			log.error("[GetAccessTokenTask] getAccessToken Error:", e);
			return null;
		}
	}
}
