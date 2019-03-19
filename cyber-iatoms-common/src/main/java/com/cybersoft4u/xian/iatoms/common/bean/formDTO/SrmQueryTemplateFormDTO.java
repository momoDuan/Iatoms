package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmQueryTemplateDTO;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;
/**
 * Purpose: 用戶欄位模板維護檔FormDTO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/6/28
 * @MaintenancePersonnel CrissZhang
 */
public class SrmQueryTemplateFormDTO extends AbstractSimpleListFormDTO<SrmQueryTemplateDTO>{
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -2783219996704130049L;
	
	/**
	 * 用戶編號
	 */
	private String templateId;
	
	/**
	 * 用戶編號
	 */
	private String userId;
	
	/**
	 *  用戶欄位模板維護檔DTO
	 */
	private  SrmQueryTemplateDTO admUserColumnTemplateDTO;
	
	/**
	 * 是否只是顯示
	 */
	private boolean isOnlyShow;
	
	/**
	 * 是否保存假顯示
	 */
	private boolean isSaveAndShow;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the admUserColumnTemplateDTO
	 */
	public SrmQueryTemplateDTO getAdmUserColumnTemplateDTO() {
		return admUserColumnTemplateDTO;
	}

	/**
	 * @param admUserColumnTemplateDTO the admUserColumnTemplateDTO to set
	 */
	public void setAdmUserColumnTemplateDTO(SrmQueryTemplateDTO admUserColumnTemplateDTO) {
		this.admUserColumnTemplateDTO = admUserColumnTemplateDTO;
	}

	/**
	 * @return the templateId
	 */
	public String getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * @return the isOnlyShow
	 */
	public boolean getIsOnlyShow() {
		return isOnlyShow;
	}

	/**
	 * @param isOnlyShow the isOnlyShow to set
	 */
	public void setIsOnlyShow(boolean isOnlyShow) {
		this.isOnlyShow = isOnlyShow;
	}

	/**
	 * @return the isSaveAndShow
	 */
	public boolean getIsSaveAndShow() {
		return isSaveAndShow;
	}

	/**
	 * @param isSaveAndShow the isSaveAndShow to set
	 */
	public void setIsSaveAndShow(boolean isSaveAndShow) {
		this.isSaveAndShow = isSaveAndShow;
	}
	
}
