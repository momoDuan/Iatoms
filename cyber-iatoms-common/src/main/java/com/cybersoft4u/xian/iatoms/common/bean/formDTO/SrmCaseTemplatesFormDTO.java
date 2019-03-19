package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTemplatesDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterDetailDTO;
/**
 * Purpose:  工單範本維護的FormDTO
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/9/23
 * @MaintenancePersonnel echomou
 */
public class SrmCaseTemplatesFormDTO extends AbstractSimpleListFormDTO{
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 5488277857900136894L;
	/**
	 * 上傳文件名稱
	 */
	public static final String PAPAM_PAGE_NAME_FILE_NAME 	= "fileName";
	/**
	 * 工單範本維護dto
	 */
	private SrmCaseTemplatesDTO srmCaseTemplatesDTO;
	
	/**
	 * 
	 * Constructor:構造器
	 */
	public SrmCaseTemplatesFormDTO() {
		super();
		this.srmCaseTemplatesDTO = srmCaseTemplatesDTO;
	}
	/**
	 * 文件保存路徑
	 */
	private String path;
	/**
	 * 文件保存id
	 */
	private String fileId;
	/**
	 * 文件名稱
	 */
	private String fileName;
	/**
	 * 範本類別
	 */
	private String category;
	
	private String categoryId;

	/**
	 * SrmCaseTemplatesDTO List
	 */
	private List<SrmCaseTemplatesDTO> srmCaseTemplatesDTOList;
	/**
	 * SrmTransactionParameterDetailDTO List交易參數
	 */
	private List<SrmTransactionParameterDetailDTO> srmTransactionParameterDetailDTO;
	/**
	 * 案件處理信息
	 */
	private SrmCaseHandleInfoDTO srmCaseHandleInfoDTO;
	/**
	 * 上传文档
	 */
	private MultipartFile uploadFiled;
	/**
	 * 
	 */
	private Map<String, MultipartFile> fileMap;
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the srmCaseTemplatesDTOList
	 */
	public List<SrmCaseTemplatesDTO> getSrmCaseTemplatesDTOList() {
		return srmCaseTemplatesDTOList;
	}

	/**
	 * @param srmCaseTemplatesDTOList the srmCaseTemplatesDTOList to set
	 */
	public void setSrmCaseTemplatesDTOList(
			List<SrmCaseTemplatesDTO> srmCaseTemplatesDTOList) {
		this.srmCaseTemplatesDTOList = srmCaseTemplatesDTOList;
	}

	/**
	 * @return the srmCaseTemplatesDTO
	 */
	public SrmCaseTemplatesDTO getSrmCaseTemplatesDTO() {
		return srmCaseTemplatesDTO;
	}

	/**
	 * @param srmCaseTemplatesDTO the srmCaseTemplatesDTO to set
	 */
	public void setSrmCaseTemplatesDTO(SrmCaseTemplatesDTO srmCaseTemplatesDTO) {
		this.srmCaseTemplatesDTO = srmCaseTemplatesDTO;
	}

	/**
	 * @return the uploadFiled
	 */
	public MultipartFile getUploadFiled() {
		return uploadFiled;
	}

	/**
	 * @param uploadFiled the uploadFiled to set
	 */
	public void setUploadFiled(MultipartFile uploadFiled) {
		this.uploadFiled = uploadFiled;
	}

	/**
	 * @return the fileId
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileMap
	 */
	public Map<String, MultipartFile> getFileMap() {
		return fileMap;
	}

	/**
	 * @param fileMap the fileMap to set
	 */
	public void setFileMap(Map<String, MultipartFile> fileMap) {
		this.fileMap = fileMap;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the categoryId
	 */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the srmTransactionParameterDetailDTO
	 */
	public List<SrmTransactionParameterDetailDTO> getSrmTransactionParameterDetailDTO() {
		return srmTransactionParameterDetailDTO;
	}

	/**
	 * @param srmTransactionParameterDetailDTO the srmTransactionParameterDetailDTO to set
	 */
	public void setSrmTransactionParameterDetailDTO(
			List<SrmTransactionParameterDetailDTO> srmTransactionParameterDetailDTO) {
		this.srmTransactionParameterDetailDTO = srmTransactionParameterDetailDTO;
	}

	/**
	 * @return the srmCaseHandleInfoDTO
	 */
	public SrmCaseHandleInfoDTO getSrmCaseHandleInfoDTO() {
		return srmCaseHandleInfoDTO;
	}

	/**
	 * @param srmCaseHandleInfoDTO the srmCaseHandleInfoDTO to set
	 */
	public void setSrmCaseHandleInfoDTO(SrmCaseHandleInfoDTO srmCaseHandleInfoDTO) {
		this.srmCaseHandleInfoDTO = srmCaseHandleInfoDTO;
	}
	
	
}
