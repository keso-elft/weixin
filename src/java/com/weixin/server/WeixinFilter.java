package com.weixin.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.weixin.util.WeixinTools;

public class WeixinFilter implements Filter {

	protected Logger log = LogManager.getLogger("weixinServer");

	private static final long serialVersionUID = 1L;

	private String _token;

	private Server server;

	@Override
	public void destroy() {
		log.info("WeixinFilter已经销毁");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		Boolean isGet = request.getMethod().equals("GET");

		String path = request.getServletPath();
		String pathInfo = path.substring(path.lastIndexOf("/"));

		if (pathInfo == null) {
			response.getWriter().write("error");
		} else {
			_token = pathInfo.substring(1);
			if (isGet) {
				doGet(request, response);
			} else {
				doPost(request, response);
			}
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		try {
			server = new Server();
			server.init(config);
		} catch (Exception e) {
			log.error(" HttpServer init error ", e);
			e.printStackTrace();
		}
	}

	/**
	 * 消息处理
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String signature = req.getParameter("signature");// 微信加密签名
		String timestamp = req.getParameter("timestamp");// 时间戳
		String nonce = req.getParameter("nonce");// 随机数
		String echostr = req.getParameter("echostr");//
		// 验证
		if (signature != null && timestamp != null && nonce != null
				&& WeixinTools.checkSignature(_token, signature, timestamp, nonce)) {
			resp.getWriter().write(echostr);
		}
	}

	/**
	 * 验证处理
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		server.doServer(req, resp);
	}

}
