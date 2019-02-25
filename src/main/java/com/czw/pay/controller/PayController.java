package com.czw.pay.controller;

import java.io.IOException;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.czw.alipay.service.AlipayService;
import com.czw.alipay.types.ClientType;
import com.czw.pay.entity.JpayOrderItem;
import com.czw.pay.service.JpayOrderItemService;
import com.czw.pay.service.JpayOrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 创建者:崔志伟 emai：cuizhiwei4930@sina.com 日期：2017年11月27日下午11:17:31
 */
@Api(tags = "支付宝支付api")
@Controller
@PropertySource(value = { "classpath:/common.properties" }, encoding = "utf-8")
@RequestMapping("/api/pay")
public class PayController {
	@Autowired
	private AlipayService alipayService;

	@Resource(name = "jpayOrderService")
	private JpayOrderService jpayOrderService;

	@Resource(name = "jpayOrderItemService")
	private JpayOrderItemService jpayOrderItemService;

	/**
	 * 支付宝支付
	 * @param request
	 * @param response
	 * @param orderNo
	 * @param clientType
	 */
	@ApiOperation(value = "支付", notes = "支付宝支付", httpMethod = "GET")
	@GetMapping("/alipay")
	public void alipay(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("orderId") String orderId,
			@RequestParam("orderNo") String orderNo,@RequestParam("price") BigDecimal price,
			@RequestParam("title") String title,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") int clientType) {
		JpayOrderItem jpayOrderItem = new JpayOrderItem();
		jpayOrderItem.setOrderNo(orderNo);
		jpayOrderItem.setPrice(price);
		jpayOrderItem.setTitle(title);
		jpayOrderItem.setOrderId(orderId);

		ClientType ct = ClientType.values()[clientType];
		String resultform = alipayService.payJump(jpayOrderItem, ct);
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		try {
			response.getWriter().print(resultform);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@ApiOperation(value = "wxpay", notes = "wxpay", httpMethod = "GET")
	@RequestMapping("/wxpay")
	public void wxpay(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "") String outTradeNo,
			@RequestParam(required = false, defaultValue = "0") int clientType) throws IOException {
		
		response.sendRedirect("http://www.baidu.com");

	}

}
