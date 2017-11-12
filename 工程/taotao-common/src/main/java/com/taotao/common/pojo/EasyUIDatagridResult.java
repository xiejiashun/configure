package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 序列化 和 反序列化
 * @title
 * @description
 * @author ljh 
 * @version 1.0
 */
public class EasyUIDatagridResult implements Serializable{
	private Long total;
	private List rows;
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
}
