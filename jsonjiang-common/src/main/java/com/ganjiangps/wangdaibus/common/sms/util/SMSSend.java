package com.ganjiangps.wangdaibus.common.sms.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ganjiangps.wangdaibus.common.sms.request.SmsSendRequest;
import com.ganjiangps.wangdaibus.common.sms.response.SmsSendResponse;
import com.ganjiangps.wangdaibus.model.SmsChannel;
import com.ipi.cloud.interfaces.dto.ErrorInfo;
import com.ipi.cloud.sms.access.request.CloudSmsCore;
import com.ipi.cloud.sms.access.vo.SmsAccount;
import com.ipi.cloud.sms.access.vo.response.SmsBatchSmbitResult;
import com.ipi.cloud.sms.access.vo.response.SmsBatchSumbitResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;

/**
 *
 * @author tianyh
 * @Description:HTTP 请求
 */
public class SMSSend {

	public static final String charset = "utf-8";
	public static ObjectMapper mapper = new ObjectMapper();

	public static Logger log= LoggerFactory.getLogger(SMSSend.class);


	/**
	 *
	 * @param smsChannel  短信通道
	 * @param mobile 手机号
	 * @param message  短信内容
	 * @author :liuqh
	 * @date :2018/3/23 10:12
	 * @return
	 */
	public static String sendMessge(SmsChannel smsChannel, String mobile, String message){
		//获取短信接口URL
		String url = smsChannel.getSmsurl();
		//账号
		String account = smsChannel.getPusername();
		//密码
		String password = smsChannel.getPpassword();
		//短信通道名称
		String smscname = smsChannel.getSmscname();

		//企业编号
		String serialno = smsChannel.getSerialno();

		String msg = "";
		// 发送短信
		try {
			if (smscname.equals("创蓝云通讯")) {
				msg = SMSSend.sendMsg(mobile, message, url, account, password);
			}else if (smscname.equals("短信通")) {
				SmsBatchSumbitResponse response = SMSSend.smsSendDXT(url, serialno, account, password, mobile, message);
				System.out.println(response);
				if (response.isSuccess() == true) {
					msg = "";
				} else {
					msg = response.getErrorInfo().getErrorMsg();
				}
			}else{
				log.info("没有此短信服务商");
				return "短信发送异常";
			}
		} catch (Exception e) {
			System.out.println("短信发送异常:" + e.toString());
			return "短信发送异常";
		}
		return msg;
	}

	/**
	 *  发送普通短信（创蓝云）
	 * @author 作者 gengfl:
	 * @version 创建时间：2018年1月23日 上午11:44:28
	 * @param url     短信地址
	 * @param account  账号
	 * @param pswd     密码
	 * @param phone    手机
	 * @param message      内容
	 * @return
	 */
	public static String sendMsg(String phone, String message, String url,String account,String pswd) throws Exception {
		//加以下语句为了解决在本地tomcat（war包）运行时短信中文乱码-----liuqh
		message= URLEncoder.encode(message, "utf-8");

		// 普通短信地址
		String smsSingleRequestServerUrl = url;
		// 短信内容
		String msg = message;
		// 状态报告
		String report = "true";

		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone, report);

		String requestJson = mapper.writeValueAsString(smsSingleRequest);

		System.out.println("before request string is: " + requestJson);

		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);

		System.out.println("response after request result is :" + response);

		SmsSendResponse smsSingleResponse = mapper.readValue(response, SmsSendResponse.class);

		System.out.println("response  toString is :" + smsSingleResponse);

		return smsSingleResponse.getErrorMsg();
	}

	/**
	 * 短信通
	 *
	 * @param destPhone
	 *            发送的手机号码 以,分隔
	 * @param msg
	 *            短信内容
	 */
	public static boolean smsSend(String destPhone, String msg) {
		// 接口地址
		String interUrl = "http://interface.i314.net:6666/interface/SendMsg";
		// 企业编号
		String entNo = "21471837331005";
		// 用户账号
		String account = "13644057565";
		// 用户密码
		String password = "kk951357";

		/*** 调用接口进行请求批量发送 ***/
		SmsBatchSumbitResponse resp = CloudSmsCore.getInstance().batchSendSms(interUrl,
				new SmsAccount(entNo, account, password), destPhone, msg);
		System.out.println("发送状态： " + resp.isSuccess());
		if (resp.isSuccess()) // 提交成功
		{
			SmsBatchSmbitResult result = resp.getBatchSmbitResult();
			// 根据结果进行业务处理
			System.out.println(result);
		} else { // 提交返回错误
			ErrorInfo errorInfo = resp.getErrorInfo();
			// 根据结果进行相关错误处理
			System.out.println(errorInfo);
		}
		return resp.isSuccess();
	}



	/**
	 * 发送短信（短信通） 手机号码以,隔开
	 * @author 作者 xiaoy:
	 * @version 创建时间：2017年6月1日 下午5:34:55
	 * @param interUrl 接口地址
	 * @param entNo    企业号
	 * @param account   账号
	 * @param password  密码
	 * @param destPhone 手机号
	 * @param msg       内容
	 * @return  true 成功, false 失败
	 */
	public static SmsBatchSumbitResponse smsSendDXT(String interUrl, String entNo, String account, String password, String destPhone,
													String msg) {
		/*** 调用接口进行请求批量发送 ***/
		SmsBatchSumbitResponse resp = CloudSmsCore.getInstance().batchSendSms(interUrl,
				new SmsAccount(entNo, account, password), destPhone, msg);
		System.out.println("发送状态： " + resp.isSuccess());
		System.out.println(resp);
		/*
		if (resp.isSuccess()) // 提交成功
		{
			SmsBatchSmbitResult result = resp.getBatchSmbitResult();
			// 根据结果进行业务处理
			System.out.println(result);
		} else { // 提交返回错误
			ErrorInfo errorInfo = resp.getErrorInfo();
			// 根据结果进行相关错误处理
			System.out.println(errorInfo);
		}*/
		return resp;
	}

	public static void main(String[] args) throws Exception {
		sendMsg("18814493730","【火车头理财】 恭喜您获得【12.00】元,请尽快查看","http://vsms.253.com/msg/send/json","N7503157","xO5azgLeSuee85");
//		smsSend("13923495439", "短信测试");
	}
}
