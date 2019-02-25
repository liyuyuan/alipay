package com.czw.alipay.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.czw.alipay.types.ClientType;
import com.czw.common.tools.Tools;
/**
 * 创建者:崔志伟
 * emai：cuizhiwei4930@sina.com
 * 日期：2017年11月27日
 */
import com.czw.pay.entity.JpayOrderItem;

@Component
@PropertySource(value = {"classpath:/common.properties"},encoding="utf-8")  
public class AlipayService {
	
	@Value("${com.alipay.app.publickey}")  
	private String alipayAppPublicKey; //应用公钥
	
	@Value("${com.alipay.publickey}")  
	private String alipayPublicKey; //支付宝公钥
	 
	@Value("${com.alipay.app.privatekey}")  
	private String alipayAppPrivateKey;  //应用私钥
	
	@Value("${com.alipay.pay.url}")  
	private String alipayUrl;  //阿里网关
	
	@Value("${com.alipay.pay.appid}")  
	private String alipayAppid; //appid
	
	@Value("${com.czw.pay.returnurl}")  
	private String alipayReturnUrl; //前台同步返回地址
	
	@Value("${com.czw.pay.notifyurl}")  
	private String alipayNotifyUrl; //后台异步通知地址
	
	private static String CHARSET="UTF-8";

	public String payJump(JpayOrderItem jpayOrderItem,ClientType clientType){
		AlipayTradeWapPayModel alipayTradeWapPayModel = new AlipayTradeWapPayModel();
		AlipayClient alipayClient = new DefaultAlipayClient(alipayUrl, alipayAppid, alipayAppPrivateKey, "json", CHARSET, alipayPublicKey, "RSA2"); //获得初始化的AlipayClient
		String form="";
		if(clientType == ClientType.WAP){
			AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
			alipayRequest.setReturnUrl(alipayReturnUrl);
			alipayRequest.setNotifyUrl(alipayNotifyUrl);//在公共参数中设置回跳和通知地址
			alipayRequest.setBizContent("{" +
			"\"out_trade_no\":\""+alipayTradeWapPayModel.getOutTradeNo()+"\"," +
			"\"total_amount\":\""+alipayTradeWapPayModel.getTotalAmount()+"\"," +
			"\"subject\":\""+alipayTradeWapPayModel.getSubject()+"\"," +
			"\"product_code\":\"QUICK_WAP_PAY\"," + //固定值
			"\"passback_params\":\""+Tools.encodePar("clientType="+ClientType.WAP.ordinal())+"\"" +
			"}");//填充业务参数
			try {
				form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}
		}else if(clientType == ClientType.PC){
			AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
		    alipayRequest.setReturnUrl(alipayReturnUrl);
		    alipayRequest.setNotifyUrl(alipayNotifyUrl);//在公共参数中设置回跳和通知地址
		    JSONObject content = new JSONObject();
		    content.put("order_id",jpayOrderItem.getOrderId());
		    content.put("out_trade_no",jpayOrderItem.getOrderNo());
		    content.put("total_amount",jpayOrderItem.getPrice());
		    content.put("product_code", "FAST_INSTANT_TRADE_PAY");
		    content.put("subject",jpayOrderItem.getTitle());
		    content.put("body",jpayOrderItem.getTitle());
		    
		    alipayRequest.setBizContent(JSON.toJSONString(content));//填充业务参数
		    
		    try {
		        form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
		    } catch (AlipayApiException e) {
		        e.printStackTrace();
		    }
		}
		return form;
	}
}
