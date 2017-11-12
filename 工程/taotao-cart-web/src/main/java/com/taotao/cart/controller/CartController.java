package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.service.ItemService;
import com.taotao.sso.service.UserLoginService;

@Controller
public class CartController {
	@Autowired
	private UserLoginService userloginservice;

	@Autowired
	private CartService cartservice;

	@Autowired
	private ItemService itemservice;

	// url:/cart/add/{itemId}?num=2
	@RequestMapping("/cart/add/{itemId}")
	public String addCartItem(@PathVariable Long itemId, Integer num, HttpServletRequest request,HttpServletResponse response) {
		// 1.从cookie中获取token
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 2.调用sso的服务获取用户的信息(依赖jar 引入服务，注入服务，调用服务)
		TaotaoResult result = userloginservice.getUserByToken(token);

		if (result.getStatus() == 200) {
			// 3.判断是否登录 如果登录 调用cartservice的方法 存储到redis购物车中
			TbItem tbItem = itemservice.getItemById(itemId);
			TbUser user = (TbUser) result.getData();
			cartservice.addCartItem(user.getId(), tbItem, num);

		} else {
			// 4.如果没有登录 购物车存储在cookie中 操作cookie
			
			//4.1 查询购物车的商品的列表 
			List<TbItem> cartList = getCartList(request);
			boolean flag = false;//没有要添加的商品在购物车中
			for (TbItem tbItem : cartList) {
				//4.2 判断要添加的商品是否在购物车里面，如果存在，数量相加即可----》存储到cookie中
				if(itemId.longValue()==tbItem.getId()){//说明要添加的商品在购物车里面
					//数量相加即可
					tbItem.setNum(num+tbItem.getNum());
					flag=true;
					//
					break;
				}
				
			}
			if(!flag){
				//4.3如果要添加的商品没有在购物车里面，直接添加到购物车（再存储到cookie）
				TbItem tbItem = itemservice.getItemById(itemId);
				//设置商品的图片 取一张
				if(StringUtils.isNotBlank(tbItem.getImage())){
					String[] strings = tbItem.getImage().split(",");
					tbItem.setImage(strings[0]);
				}
				//设置商品的购买数量
				tbItem.setNum(num);
				cartList.add(tbItem);
				//
			}
			//设置购物车列表回cookie中
			CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartList), 604800, true);
		}
		return "cartSuccess";
	}

	// url:/cart/cart
	// 参数：没有参数
	// 返回值：逻辑视图 （传递数据到页面中显示 购物车的列表）

	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request) {
		// 1.从cookie中获取token
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 2.调用sso的服务获取用户的信息(依赖jar 引入服务，注入服务，调用服务)
		TaotaoResult result = userloginservice.getUserByToken(token);

		if (result.getStatus() == 200) {
			// 3.判断是否登录 如果登录 调用cartservice的方法 查询某一个用户的购物车列表
			TbUser user = (TbUser) result.getData();
			List<TbItem> cartList = cartservice.getCartListByUserId(user.getId());
			// 传递数据到页面中
			request.setAttribute("cartList", cartList);
		} else {
			// 4.如果没有登录 购物车存储在cookie中 操作cookie
			// 操作cookie
			List<TbItem> cartList = getCartList(request);
			request.setAttribute("cartList", cartList);
		}
		return "cart";
	}

	// url:/cart/update/num/{itemId}/{num}
	// 参数：{itemId} {num}
	// 返回值：json

	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult updateCartItem(@PathVariable Long itemId, @PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response) {
		// 1.从cookie中获取token
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 2.调用sso的服务获取用户的信息(依赖jar 引入服务，注入服务，调用服务)
		TaotaoResult result = userloginservice.getUserByToken(token);
		// 用户已经登录
		if (result.getStatus() == 200) {
			// 3.判断是否登录 如果登录 调用cartservice的方法 查询某一个用户的购物车列表
			TbUser user = (TbUser) result.getData();
			// 更新redis
			TaotaoResult result2 = cartservice.updateCartItem(user.getId(), itemId, num);
			return result2;
		} else {
			// 4.如果没有登录 购物车存储在cookie中 操作cookie
			updateCookieCartItem(request,response,itemId,num);
			System.out.println("gengx>>>>>>");
			return TaotaoResult.ok();
			// 操作cookie
		}
	}

	// 删除

	// url:/cart/delete/150734975282624
	// 参数：商品的id
	// 返回值：页面 （重定向）
	@RequestMapping("/cart/delete/{itemId}")

	public String deleteItemCart(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response) {

		// 1.从cookie中获取token
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 2.调用sso的服务获取用户的信息(依赖jar 引入服务，注入服务，调用服务)
		TaotaoResult result = userloginservice.getUserByToken(token);
		// 用户已经登录
		if (result.getStatus() == 200) {
			// 3.判断是否登录 如果登录 调用cartservice的方法 查询某一个用户的购物车列表
			TbUser user = (TbUser) result.getData();
			// 更新redis
			TaotaoResult result2=	cartservice.deleteCartItem(user.getId(), itemId);
			//springmvc的重定向 浏览器地址需要改变。url的拦截的形式就是 *.html 所以必须有后缀
			return "redirect:/cart/cart.html";
		} else {
			// 4.如果没有登录 购物车存储在cookie中 操作cookie
			// 操作cookie
			deleteCookieCartItem(itemId,request,response);
			System.out.println(">>>>>>>>");
			return "redirect:/cart/cart.html";
		}
	}
	
	//--------------------------------------cookie的操作   ---------------------
	
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
	
	//更新cookie中的购物车的数量
	private void updateCookieCartItem(HttpServletRequest request,HttpServletResponse response,Long itemId,Integer num){
		//先获取购物车的列表
		List<TbItem> cartList = getCartList(request);
		//判断商品是否在购物车的列表中  如果存在 就更新  
		for (TbItem tbItem : cartList) {
			//4.2 商品是否在购物车里面，如果存在，数量更新
			if(itemId.longValue()==tbItem.getId()){
				//说明要添加的商品在购物车里面
				tbItem.setNum(num);
				//
				//设置购物车列表回cookie中
				CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartList), 604800, true);
				break;
			}
			
		}
		
		//如果不存在 就不管了。
	}
	
	////删除cookie中的购物车的商品
	
	private void deleteCookieCartItem(Long itemId,HttpServletRequest request,HttpServletResponse response){
		List<TbItem> cartList = getCartList(request);
		for (TbItem tbItem : cartList) {
			if(tbItem.getId()==itemId.longValue()){//找到要删除的商品
				cartList.remove(tbItem);//删除
				//设置回cookie就可以了
				CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartList), 604800, true);
				break;
			}
		}
	}
	
	

}
