package com.cybersoft4u.xian.iatoms.common.reportsetting;

import java.util.List;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose:作业别費用單價 
 * @author Hermanwang
 * @since  JDK 1.6
 * @date   2017/8/22
 * @MaintenancePersonnel Hermanwang
 */
public class WorkFeeSetting extends DataTransferObject<String> {
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -8202807629225445585L;
	/**
	 * 
	 * Constructor:空構造
	 */
	public WorkFeeSetting() {
		super();
	}
	/**
	 * 作业類别
	 */
	private String workCategory;
	/**
	 * 刷卡機(含週邊設備)裝/移機
	 */
	private String name;
	/**
	 * 第一台單價
	 */
	private Integer firstPrice;
	private double firstPriceDou;
	/**
	 * 第一台數量
	 */
	private String firstCount;
	/**
	 * 其他台單價
	 */
	private Integer otherPrice;
	/**
	 * 其他台數量
	 */
	private String otherCount;
	/**
	 * 合計
	 */
	private Integer sum;
	/**
	 * 總計
	 */
	private Integer total;
	/**
	 * 備註
	 */
	private String description;
	/**
	 * 台數
	 */
	private Integer number;
	/**
	 * 免費TMS次數
	 */
	private String freeTmsFrequency;
	/**
	 * 剩餘免費次數
	 */
	private String surplusFreeFrequency;
	/**
	 * 子報表1list
	 */
	private List<WorkFeeSetting> workFeeSettingList;
	/**
	 * 子報表2list
	 */
	private List<WorkFeeSetting> workFeeSettingFeeList;
	/**
	 * 備註
	 */
	private String remarks;
	private String month;
	private String year;
	private String totalNumber;
	/**
	 * 單價
	 */
	private Integer price;
	/**
	 * 其他備註說明
	 */
	private String otherRemarks;
	/**
	 * 子報表title
	 */
	private String subTitle;
	private Integer countSum;
	private Integer priceSum;
	/**
	 * 環匯、捷達威格式使用單價
	 */
	private String priceString;
	/**
	 * @return the workCategory
	 */
	public String getWorkCategory() {
		return workCategory;
	}
	/**
	 * @param workCategory the workCategory to set
	 */
	public void setWorkCategory(String workCategory) {
		this.workCategory = workCategory;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}
	/**
	 * @return the freeTmsFrequency
	 */
	public String getFreeTmsFrequency() {
		return freeTmsFrequency;
	}
	/**
	 * @param freeTmsFrequency the freeTmsFrequency to set
	 */
	public void setFreeTmsFrequency(String freeTmsFrequency) {
		this.freeTmsFrequency = freeTmsFrequency;
	}
	/**
	 * @return the surplusFreeFrequency
	 */
	public String getSurplusFreeFrequency() {
		return surplusFreeFrequency;
	}
	/**
	 * @param surplusFreeFrequency the surplusFreeFrequency to set
	 */
	public void setSurplusFreeFrequency(String surplusFreeFrequency) {
		this.surplusFreeFrequency = surplusFreeFrequency;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}
	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
	/**
	 * @return the totalNumber
	 */
	public String getTotalNumber() {
		return totalNumber;
	}
	/**
	 * @param totalNumber the totalNumber to set
	 */
	public void setTotalNumber(String totalNumber) {
		this.totalNumber = totalNumber;
	}
	

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the firstCount
	 */
	public String getFirstCount() {
		return firstCount;
	}
	/**
	 * @param firstCount the firstCount to set
	 */
	public void setFirstCount(String firstCount) {
		this.firstCount = firstCount;
	}
	
	/**
	 * @return the otherCount
	 */
	public String getOtherCount() {
		return otherCount;
	}
	/**
	 * @param otherCount the otherCount to set
	 */
	public void setOtherCount(String otherCount) {
		this.otherCount = otherCount;
	}
	/**
	 * @return the firstPrice
	 */
	public Integer getFirstPrice() {
		return firstPrice;
	}
	/**
	 * @param firstPrice the firstPrice to set
	 */
	public void setFirstPrice(Integer firstPrice) {
		this.firstPrice = firstPrice;
	}
	/**
	 * @return the otherPrice
	 */
	public Integer getOtherPrice() {
		return otherPrice;
	}
	/**
	 * @param otherPrice the otherPrice to set
	 */
	public void setOtherPrice(Integer otherPrice) {
		this.otherPrice = otherPrice;
	}
	/**
	 * @return the sum
	 */
	public Integer getSum() {
		return sum;
	}
	/**
	 * @param sum the sum to set
	 */
	public void setSum(Integer sum) {
		this.sum = sum;
	}
	/**
	 * @return the total
	 */
	public Integer getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}
	/**
	 * @return the price
	 */
	public Integer getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}
	/**
	 * @return the otherRemarks
	 */
	public String getOtherRemarks() {
		return otherRemarks;
	}
	/**
	 * @param otherRemarks the otherRemarks to set
	 */
	public void setOtherRemarks(String otherRemarks) {
		this.otherRemarks = otherRemarks;
	}
	/**
	 * @return the workFeeSettingList
	 */
	public List<WorkFeeSetting> getWorkFeeSettingList() {
		return workFeeSettingList;
	}
	/**
	 * @param workFeeSettingList the workFeeSettingList to set
	 */
	public void setWorkFeeSettingList(List<WorkFeeSetting> workFeeSettingList) {
		this.workFeeSettingList = workFeeSettingList;
	}
	/**
	 * @return the workFeeSettingFeeList
	 */
	public List<WorkFeeSetting> getWorkFeeSettingFeeList() {
		return workFeeSettingFeeList;
	}
	/**
	 * @param workFeeSettingFeeList the workFeeSettingFeeList to set
	 */
	public void setWorkFeeSettingFeeList(List<WorkFeeSetting> workFeeSettingFeeList) {
		this.workFeeSettingFeeList = workFeeSettingFeeList;
	}
	/**
	 * @return the subTitle
	 */
	public String getSubTitle() {
		return subTitle;
	}
	/**
	 * @param subTitle the subTitle to set
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	/**
	 * @return the countSum
	 */
	public Integer getCountSum() {
		return countSum;
	}
	/**
	 * @param countSum the countSum to set
	 */
	public void setCountSum(Integer countSum) {
		this.countSum = countSum;
	}
	/**
	 * @return the priceSum
	 */
	public Integer getPriceSum() {
		return priceSum;
	}
	/**
	 * @param priceSum the priceSum to set
	 */
	public void setPriceSum(Integer priceSum) {
		this.priceSum = priceSum;
	}
	/**
	 * @return the priceString
	 */
	public String getPriceString() {
		return priceString;
	}
	/**
	 * @param priceString the priceString to set
	 */
	public void setPriceString(String priceString) {
		this.priceString = priceString;
	}
	/**
	 * @return the firstPriceDou
	 */
	public double getFirstPriceDou() {
		return firstPriceDou;
	}
	/**
	 * @param firstPriceDou the firstPriceDou to set
	 */
	public void setFirstPriceDou(double firstPriceDou) {
		this.firstPriceDou = firstPriceDou;
	}
	
	
}
