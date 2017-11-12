package com.taotao.order.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserLoginService;

@Controller
public class OrderController {
	
	@Autowired
	private UserLoginService loginservice;
	
	@Autowired
	private OrderService orderservice;
	
	@Autowired
	private CartService cartservice;
	
	//要求用户登录之后才能展示页面
	@RequestMapping("/order/order-cart")
	public String getOrderCartInfo(Model model,HttpServletRequest request,HttpServletResponse response){
		//1.从cookie获取token 
//		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
//		//2.调用sso的服务 获取用户的信息  （引入依赖，引入服务，注入服务）
//		TaotaoResult result = loginservice.getUserByToken(token);
		//if(result.getStatus()==200){
		TbUser user = (TbUser) request.getAttribute("USER_INFO");
//			TbUser user = (TbUser) result.getData();
			//3.判断用户是否登录   登录，展示订单确认页面
			//展示该用户的地址列表  ---根据userid 查询  。这里用静态数据
			//展示支付的方式---》静态的数据
			//展示购物车的数据   
		
		
			//合并cookie中的购物车的数据到redis购物车中
			   //第一 ：取出cookie中的购物车的列表
			List<TbItem> cookieList = getCartList(request);
			   //第二：取出redis数据中的购物车的列表
			List<TbItem> redislist = cartservice.getCartListByUserId(user.getId());
			   //第三：合并
			for (TbItem tbItem : cookieList) {
				
				boolean flag = false;
				
				for (TbItem redisItem : redislist) {
					if(tbItem.getId()==redisItem.getId()){
						//redis中有cookie种的商品 更新数量
						//redisItem.setNum(redisItem.getNum()+tbItem.getNum());
						//设置回redis
						cartservice.updateCartItem(user.getId(), redisItem.getId(), (redisItem.getNum()+tbItem.getNum()));
						flag=true;//说明找到
						break;
					}
				}
				if(flag==false){
					//如果没有找到   说明：cookie中的商品 没有存在到redis中，将cookie中的商品添加到redis中
					cartservice.addCartItem(user.getId(), tbItem, tbItem.getNum());
				}
				
			}
			//清除cookie中的购物车
			CookieUtils.deleteCookie(request, response, "TT_CART");
			
			
			
			
			//调用cartservice的方法 从redis数据库中获取购物车的数据
			
			redislist= cartservice.getCartListByUserId(user.getId());//获取合并的数据
			
			//
			model.addAttribute("cartList", redislist);
		//}	
		return "order-cart";
	}
	
	

	//取得cookie中的购物车列表
	private List<TbItem> getCartList(HttpServletRequest request){
		//获取购物车的列表的json串
		String jsoncartstring = CookieUtils.getCookieValue(request, "TT_CART", true);
		if(StringUtils.isNotBlank(jsoncartstring)){
			List<TbItem> list = JsonUtils.jsonToList(jsoncartstring, TbItem.class);
			return list;
		}
		//转成List集合
		return new ArrayList<>();
	}
	
	@RequestMapping("/order/create")
	public String createOrderInfo(OrderInfo info,HttpServletRequest request){
		//用户的信息需要在controller设置
		TbUser user = (TbUser) request.getAttribute("USER_INFO");
		info.setUserId(user.getId());
		info.setBuyerNick(user.getUsername());
		TaotaoResult result = orderservice.saveOrderInfo(info);
		request.setAttribute("orderId", result.getData());
		request.setAttribute("payment", info.getPayment());
		DateTime time  = DateTime.now();
		DateTime dateTime = time.plusDays(3);//加三天
		
		request.setAttribute("date", dateTime.toString("yyyy/MM/dd"));//jotatime
		
		return "success";
	}
}
