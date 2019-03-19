package com.cybersoft4u.xian.iatoms.common.bean.dto;
import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 設備盤點报表DTO
 * @author AllenChen
 * @since  JDK 1.7
 * @date   2016-7-26
 * @MaintenancePersonnel AllenChen
 */
public class AssetInventoryReportDTO extends DataTransferObject<String>{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -165971416146688696L;

	/**
	 * Constructor:
	 */
	public AssetInventoryReportDTO() {
		super();
	}

	/**
	 * 子報表第一列
	 */
	private String dataColumn1;
	
	/**
	 * 子報表第二列
	 */
	private String dataColumn2;
	
	/**
	 * 子報表第三列
	 */
	private String dataColumn3;
	
	/**
	 * 子報表第四列
	 */
	private String dataColumn4;
	/**
	 * 当前页
	 */
	private Integer pageNum;

	/**
	 * @return the dataColumn1
	 */
	public String getDataColumn1() {
		return dataColumn1;
	}

	/**
	 * @return the dataColumn2
	 */
	public String getDataColumn2() {
		return dataColumn2;
	}

	/**
	 * @return the pageNum
	 */
	public Integer getPageNum() {
		return pageNum;
	}

	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * @return the dataColumn3
	 */
	public String getDataColumn3() {
		return dataColumn3;
	}

	/**
	 * @return the dataColumn4
	 */
	public String getDataColumn4() {
		return dataColumn4;
	}

	/**
	 * @param dataColumn1 the dataColumn1 to set
	 */
	public void setDataColumn1(String dataColumn1) {
		this.dataColumn1 = dataColumn1;
	}

	/**
	 * @param dataColumn2 the dataColumn2 to set
	 */
	public void setDataColumn2(String dataColumn2) {
		this.dataColumn2 = dataColumn2;
	}

	/**
	 * @param dataColumn3 the dataColumn3 to set
	 */
	public void setDataColumn3(String dataColumn3) {
		this.dataColumn3 = dataColumn3;
	}

	/**
	 * @param dataColumn4 the dataColumn4 to set
	 */
	public void setDataColumn4(String dataColumn4) {
		this.dataColumn4 = dataColumn4;
	}

	

}
