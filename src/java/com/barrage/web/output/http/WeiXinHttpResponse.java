package com.barrage.web.output.http;

/** 
 * 类说明 
 * @author  程辉 
 * @version V1.0  创建时间：2013-4-12 下午5:56:08 
 */
public class WeiXinHttpResponse {
	private String Ret;
	private String ErrMsg;
	private String ShowVerifyCode;
	private String ErrCode;

	public String getRet() {
		return Ret;
	}

	public void setRet(String ret) {
		Ret = ret;
	}

	public String getErrMsg() {
		return ErrMsg;
	}

	public void setErrMsg(String errMsg) {
		ErrMsg = errMsg;
	}

	public String getShowVerifyCode() {
		return ShowVerifyCode;
	}

	public void setShowVerifyCode(String showVerifyCode) {
		ShowVerifyCode = showVerifyCode;
	}

	public String getErrCode() {
		return ErrCode;
	}

	public void setErrCode(String errCode) {
		ErrCode = errCode;
	}

}
