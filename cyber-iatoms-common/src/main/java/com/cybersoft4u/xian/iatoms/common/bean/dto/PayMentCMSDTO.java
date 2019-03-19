package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.math.BigDecimal;


/**
 * Purpose: 求償項目DTO
 * @author neiljing
 * @since  JDK 1.7
 * @date   2018/4/10
 * @MaintenancePersonnel neiljing
 */
public class PayMentCMSDTO {

	/**
	 * 求償項目
	 */
	private String Item;
	/**
	 * 名稱
	 */
	private String Name;
	/**
	 * 數量
	 */
	private Integer Number;
	/**
	 * 金額
	 */
	private Integer Amount;
	
	
	
	/**
	 * @param item
	 * @param name
	 * @param number
	 * @param amount
	 */
	public PayMentCMSDTO(String item, String name, Integer number,
			Integer amount) {
		this.Item = item;
		this.Name = name;
		this.Number = number;
		this.Amount = amount;
	}
	public String getItem() {
		return Item;
	}
	public void setItem(String item) {
		Item = item;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Integer getNumber() {
		return Number;
	}
	public void setNumber(Integer number) {
		Number = number;
	}
	public Integer getAmount() {
		return Amount;
	}
	public void setAmount(Integer amount) {
		Amount = amount;
	}
	
}
