package com.taotao.item.pojo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.taotao.pojo.TbItem;

public class Item extends TbItem {
	
	public String[] getImages(){
		if(StringUtils.isNotBlank(this.getImage())){
			String[] split = this.getImage().split(",");
			return split;
		}
		return null;
	}
	
	public Item(TbItem tbitem){
		//源数据
		//后一个参数：目标数据
		BeanUtils.copyProperties(tbitem, this);//根据sett方法  如果有一样的sett方法就copy
		
		//
		//this.setBarcode(tbitem.getBarcode());//作用是一样
		//this.setBarcode(tbitem.getBarcode());//作用是一样
		//this.setBarcode(tbitem.getBarcode());//作用是一样
		//this.setBarcode(tbitem.getBarcode());//作用是一样
		//this.setBarcode(tbitem.getBarcode());//作用是一样
		//this.setBarcode(tbitem.getBarcode());//作用是一样
	}
}
