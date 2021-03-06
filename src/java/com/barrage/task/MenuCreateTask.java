package com.barrage.task;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.barrage.web.Constants;

public class MenuCreateTask {

	protected Logger log = LogManager.getLogger("weixinServer");

	protected String GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";

	protected String DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=";

	protected String CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";

	protected String FILE_URL = "menu.txt";

	public void run() {

		if (Constants.ACCESS_TOKEN != null) {
			String token = Constants.ACCESS_TOKEN.getAccess_token();

			String file_menu = getFileMenu();

			if (!getUrlMenu(token).equalsIgnoreCase(file_menu)) {
				deleteMenu(token);
				createMenu(token, file_menu);
			}
		}
	}

	private String getUrlMenu(String token) {
		try {
			Response response = Jsoup.connect(GET_URL + token).ignoreContentType(true).method(Method.GET).execute();
			return response.body();
		} catch (Exception e) {
			log.error("[MenuCreateTask] getUrlMenu Error:", e);
			return "";
		}
	}

	private String getFileMenu() {
		InputStream is = null;
		DataInputStream dis = null;
		BufferedReader reader = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(FILE_URL);
			dis = new DataInputStream(is);
			reader = new BufferedReader(new InputStreamReader(dis, "UTF-8"));
			String tmp = "";
			String result = "";
			while ((tmp = reader.readLine()) != null) {
				tmp = tmp.replace(" ", "");
				result += tmp;
			}

			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "";
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private void deleteMenu(String token) {
		try {
			Response response = Jsoup.connect(DELETE_URL + token).ignoreContentType(true).method(Method.POST).execute();
			log.info(response.body());
		} catch (Exception e) {
			log.error("[MenuCreateTask] deleteMenu Error:", e);
		}
	}

	private void createMenu(String token, String menu) {
		try {
			Response response = Jsoup.connect(CREATE_URL + token).ignoreContentType(true).method(Method.POST)
					.data("body", menu).execute();
			log.info(response.body());
		} catch (Exception e) {
			log.error("[MenuCreateTask] createMenu Error:", e);
		}
	}

	public static void main(String[] args) {
		MenuCreateTask task = new MenuCreateTask();
		task.run();
	}
}
