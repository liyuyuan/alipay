package com.czw.pay.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.czw.alipay.service.AlipayService;
import com.czw.pay.entity.JpayOrder;
import com.czw.pay.entity.JpayRecord;
import com.czw.pay.service.JpayOrderItemService;
import com.czw.pay.service.JpayOrderService;
import com.czw.pay.service.JpayRecordService;
import com.czw.pay.type.PayStatusType;
import com.czw.pay.type.PaymentChannelType;
import com.czw.pay.type.PaymentType;

/**
 * @author 崔志伟
 * 联系方式：493067123@qq.com
 * 创建日期：2017年12月12日
 * www.cuizhiwei.com
 */
@Controller
@PropertySource(value = {"classpath:/common.properties"},encoding="utf-8") 
@RequestMapping("/cb")
public class CallBackController {
	
	private static Logger logger = Logger.getLogger(CallBackController.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private AlipayService alipayService;
	@Resource(name="jpayOrderService")
	private JpayOrderService jpayOrderService;
	
	@Resource(name="jpayOrderItemService")
	private JpayOrderItemService jpayOrderItemService;
	
	@Resource(name="jpayRecordService")
	private JpayRecordService jpayRecordService;
	
	@Value("${com.alipay.publickey}")  
	private String alipayPublicKey; //公钥

	@Value("${order.status.update.url}")
	private String updateOrderUrl;
	
	/**
	 * 同步返回地址
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/payreturn")
	public Object payReturn(HttpServletRequest request, 
    		HttpServletResponse response,
    		RedirectAttributes attr){
		if(isSign(request)){
			//根据返回标识处理相应订单返给前台状态页面
			String orderNo = request.getParameter("out_trade_no");
			attr.addAttribute("orderNo", orderNo);
			JpayOrder order = jpayOrderService.findByOrderNo(orderNo);
			if(order!=null){
				if(order.getPayStatus()==PayStatusType.HASPAY){
					return "redirect:/p/tradefinish";
				}
			}
			return "redirect:/p/process";
		}else{
			return "404";
		}
		
		
	}
	
	/**
	 * 后台异步通知地址
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/paynotify")
	@ResponseBody
	public Object payNotify(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		if(isSign(request)){
			//异步回到确保不漏单
			String orderNo = request.getParameter("out_trade_no");
			String tradeNo = request.getParameter("trade_no");
			String totalAmount = request.getParameter("receipt_amount");
			String buyerId = request.getParameter("buyer_id");
			String buyerLogin = request.getParameter("buyer_logon_id");
			String sellerId = request.getParameter("seller_id");
			String sellerLogin = request.getParameter("seller_email");
			String orderId = request.getParameter("order_id");
			
			Map<String,String> params = new HashMap<>();
			params.put("zxId", orderId);
			params.put("zhiFuStatus", 1+"");
			
			Object object = restTemplate.postForObject(updateOrderUrl,params,Object.class);
			 
			return "success";
		}else{
			return "false";
		}
	}
	
	/**
	 * 校验sign是否正确
	 * @param request
	 * @return
	 */
	private boolean isSign(HttpServletRequest request){
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			try {
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			params.put(name, valueStr);
		}
		String sign = request.getParameter("sign");
		boolean result = false;
		if(params!=null && params.size()>0){
			try {
				result = AlipaySignature.rsaCheckV1(params, alipayPublicKey, "UTF-8","RSA2");
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
