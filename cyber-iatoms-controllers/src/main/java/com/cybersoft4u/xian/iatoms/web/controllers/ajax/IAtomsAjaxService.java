package com.cybersoft4u.xian.iatoms.web.controllers.ajax;

import java.io.File;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.service.ServiceException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.ajax.AjaxServiceProxy;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmFunctionTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTransInfoHitoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAttachedFileDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimDepartmentDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmSuppliesDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PvmDtidDefDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAttFileDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTemplatesDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmComplaintCaseFileDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmQueryTemplateDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmRoleFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmUserFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTransInfoFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTransInfoHistoryFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.BaseParameterManagerFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PaymentFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PropertyNumberImportFormDTO;
import com.cybersoft4u.xian.iatoms.services.impl.PropertyImportService;
import com.cybersoft4u.xian.iatoms.web.controllers.authenticator.AuthenticatorHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Purpose:Ajax处理Service 
 * @since  JDK 1.7
 * @date   2015/4/8
 * @MaintenancePersonnel Allenchen
 */
public class IAtomsAjaxService extends AjaxServiceProxy implements IIAtomsAjaxService {
	
	/**
	 * 识别管理服务类别Service
	 */
	private AuthenticatorHelper authenticatorHelper;
	
	/**
	 * 系统日志记录工具
	 */
	private static final CafeLog log = CafeLogFactory.getLog(IAtomsAjaxService.class);
	
	/**
	 * 
	 * Constructor: 无参构造函数
	 */
	public IAtomsAjaxService() {
		super();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#listContractIdByVendor(java.lang.String)
	 */
	public List<Parameter> getContractListByVendorId(String vendor)
			throws ServiceException {
		List<Parameter> contractList = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(ContractSlaFormDTO.ORIGINAL_CUSTOMER_ID, vendor);
			param.addParameter(IAtomsConstants.QUERY_PAGE_ORDER, ContractManageFormDTO.PARAM_ORDER_BY_CONTRACT_CODE);
			param.addParameter(IAtomsConstants.PARAM_PAGE_ORDER, IAtomsConstants.PARAM_PAGE_ORDER);
			contractList = (List<Parameter>) this.doService(IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_CONTRACT_CODE_LIST, param);
			return contractList;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception listContractIdByVendor()"+ e , e);
			throw new ServiceException(e);
		}
		
	}
	
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetTypeIdByContractId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Parameter> getAssetTypeIdByContractId(String contractId,
			String assetCategory) throws ServiceException {
		List<Parameter> assetTypeIdList = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(ContractSlaDTO.ATTRIBUTE.CONTRACT_ID.getValue(), contractId);
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), assetCategory);
			assetTypeIdList = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_TICKET_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TYPE_ID_LIST, param);
			return assetTypeIdList;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception listContractIdByVendor()"+ e , e);
			throw new ServiceException(e);
		}
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getMerchantHeader(java.lang.String)
	 */
	@Override
	public MerchantDTO getMerchantHeader(String merchantHeaderId)
			throws ServiceException {
		MerchantDTO merchantDTO = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(MerchantDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue(),merchantHeaderId);
			merchantDTO = (MerchantDTO) this.doService(IAtomsConstants.SERVICE_TICKET_SERVICE, IAtomsConstants.ACTION_GET_MERCHANT_HEADER, param);
			return merchantDTO;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getDeviceStockDTOByContractNo()"+ e , e);
			throw new ServiceException(e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetInfoByContractNo(java.lang.String)
	 */
	public List<Parameter> getAssetInfoByContractNo(String contractNo)
			throws ServiceException {
		List<Parameter> assetTypes = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue(), contractNo);
			assetTypes = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE,IAtomsConstants.PARAM_ASSET_LIST, param);
			
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getDeviceStockDTOByContractNo()"+ e , e);
			throw new ServiceException(e);
		}
		return assetTypes;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetInInfoByAssetInId(java.lang.String)
	 */
	public AssetInInfoDTO getAssetInInfoByAssetInId(String assetInId)
			throws ServiceException {
		AssetInInfoDTO assetInInfoDTO = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(AssetInInfoDTO.ATTRIBUTE.ASSET_IN_ID.getValue(),assetInId);
			assetInInfoDTO = (AssetInInfoDTO) this.doService(IAtomsConstants.SERVICE_ASSET_IN_SERVICE, IAtomsConstants.ACTION_GET_ASSET_IN_INFO_DTO_BY_ASSET_IN_ID, param);
			return assetInInfoDTO;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getAssetInInfoByAssetInId()"+ e , e);
			throw new ServiceException(e);
		}
	}
	
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getRepositoryHistDTOByHistId(java.lang.String)
	 */
	@Override
	public DmmRepositoryHistoryDTO getRepositoryHistDTOByHistId(String histId)
			throws ServiceException {
		DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.HISTORY_ID.getValue(),histId);
			dmmRepositoryHistoryDTO = (DmmRepositoryHistoryDTO) this.doService(IAtomsConstants.SERVICE_REPOSITORY_SERVICE, IAtomsConstants.ACTION_GET_REPOSITORY_BY_HIST_ID, param);
			return dmmRepositoryHistoryDTO;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getRepositoryHistDTOByHistId()"+ e , e);
			throw new ServiceException(e);
		}
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getEmailByUserId(java.lang.String)
	 */
	@Override
	public AdmUserDTO getEmailByUserId(String userId, String maintenanceCompany) throws ServiceException {
		try {
			AdmUserDTO admUserDTO = null;
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(AdmUserDTO.ATTRIBUTE.USER_ID.getValue(),userId);
			param.addParameter(DmmRepositoryDTO.ATTRIBUTE.MAINTAIN_COMPANY.getValue(),maintenanceCompany);
			admUserDTO = (AdmUserDTO) this.doService(IAtomsConstants.SERVICE_REPOSITORY_SERVICE, IAtomsConstants.ACTION_GET_EMAIL_BY_USER_ID, param);
			return admUserDTO;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getEmailByUserId()"+ e , e);
			throw new ServiceException(e);
		}
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetTypeIdList(java.lang.String)
	 */
	@Override
	public List<Parameter> getAssetTypeIdList(String contractId)
			throws ServiceException {
		try {
			List<Parameter> parameters = null;
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(AdmUserDTO.ATTRIBUTE.USER_ID.getValue(),contractId);
			parameters = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_REPOSITORY_SERVICE, IAtomsConstants.ACTION_GET_EMAIL_BY_USER_ID, param);
			return parameters;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getAssetTypeIdList()"+ e , e);
			throw new ServiceException(e);
		}
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetTransInfoByAssetTransId(java.lang.String)
	 */
	@Override
	public DmmAssetTransInfoDTO getAssetTransInfoByAssetTransId(String assetTransId)
			throws ServiceException {
		DmmAssetTransInfoDTO assetTransInfoDTO = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue(),assetTransId);
			assetTransInfoDTO = (DmmAssetTransInfoDTO) this.doService(IAtomsConstants.SERVICE_DMM_ASSET_TRANS_INFO_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TRANS_INFO_DTO_BY_ASSET_TRANS_ID, param);
			return assetTransInfoDTO;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getAssetTransInfoByAssetTransId()"+ e , e);
			throw new ServiceException(e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#listAssetInId(java.lang.Boolean)
	 */
	public List<Parameter> getAssetInIdList(String isDone) throws ServiceException {
		List<Parameter> list = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(AssetInInfoDTO.ATTRIBUTE.IS_DONE.getValue(), isDone);
			list = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ASSET_IN_SERVICE, IAtomsConstants.ACTION_GET_ASSET_IN_ID_LIST, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception listAssetInId()"+ e , e);
			throw new ServiceException(e);
		}
		return list;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetTransIdList(java.lang.String)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Parameter> getAssetTransIdList(String tabType, String userId) throws ServiceException {
		List<Parameter> list = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(AssetTransInfoFormDTO.TAB_TYPE, tabType);
			param.addParameter(IAtomsLogonUser.FIELD_ID, userId);
			list = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_DMM_ASSET_TRANS_INFO_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TRANS_ID_LIST, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception listAssetInId()"+ e , e);
			throw new ServiceException(e);
		}
		return list;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getFunctionByParentId(java.lang.String)
	 */
	@Override
	public List<Parameter> getFunctionByParentId(String parentFunctionId) throws ServiceException {
		List<Parameter> functionList = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(AdmFunctionTypeDTO.ATTRIBUTE.PARENT_FUNCTION_ID.getValue(), parentFunctionId);
			functionList = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ADM_ROLE_SERVICE, IAtomsConstants.ACTION_GET_FUNCTION_BY_PARENT_ID, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getFunctionByParentId()"+ e , e);
			throw new ServiceException(e);
		}
		return functionList;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#checkExsitDownLoadFile(java.lang.String, java.lang.String)
	 */
	public boolean checkExsitDownLoadFile(String fileName) throws ServiceException {
		boolean exsitFile = false;
		try {
			log.debug(this.getClass().getName()+".checkExsitDownLoadFile() --> fileName: "+ fileName);
			String tempFailString = IAtomsConstants.TEMPLATE_DOWNLOAD_PATH + PropertyNumberImportFormDTO.UPLOAD_EN_FILE_NAME;
			log.debug(this.getClass().getName()+".checkExsitDownLoadFile() --> filePath: "+ IAtomsConstants.TEMPLATE_DOWNLOAD_PATH);
			log.debug(this.getClass().getName()+".checkExsitDownLoadFile() --> tempFailString: "+ tempFailString);
			URL url = PropertyImportService.class.getResource(tempFailString);
			log.debug(this.getClass().getName()+".checkExsitDownLoadFile() --> url: "+ url);
			if (url != null) {
				exsitFile = true;
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception checkExsitDownLoadFile()"+ e , e);
			throw new ServiceException(e);
		}
		return exsitFile;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getContractList(java.lang.String)
	 */
	@Override
	public List<Parameter> getContractList(String customer)
			throws ServiceException {
		List<Parameter> contractList = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(ContractSlaDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customer);
			contractList =  (List<Parameter>) this.doService(IAtomsConstants.SERVICE_CONTRACT_SLA_SERVICE, IAtomsConstants.ACTION_GET_CONTRACT_ID_LIST, param);
			return contractList;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception listContractIdByCustomer()"+ e , e);
			throw new ServiceException(e);
		}
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getContractIdListByCustomer(java.lang.String)
	 */
	@Override
	public List<Parameter> getContractIdListByCustomer(String customer) throws ServiceException {
		List<Parameter> contractIdList = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(ContractSlaDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customer);
			contractIdList =  (List<Parameter>) this.doService(IAtomsConstants.SERVICE_CONTRACT_SLA_SERVICE, IAtomsConstants.ACTION_GET_LIST_BY_CONTRACT_ID, param);
			
			return contractIdList;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception listContractId()"+ e , e);
			throw new ServiceException(e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getInventoryNumberList()
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Parameter> getInventoryNumberList() throws ServiceException {
		//盤點批號集合
		List<Parameter> inventoryNumberList = null;
		try {
			//MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			inventoryNumberList =  (List<Parameter>) this.doService(IAtomsConstants.SERVICE_DMM_ASSET_STACKTAKE_SERVICE, IAtomsConstants.ACTION_GET_INVENTORY_NUMBER_LIST, null);
			return inventoryNumberList;
		} catch (Exception e) {
			log.error(this.getClass().getName()+".getInventoryNumberList() is error !!!",e);
			throw new ServiceException(e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetStocktackInfoByStocktackId(java.lang.String)
	 */
	@Override
	public DmmAssetStacktakeListDTO getAssetStocktackInfoByStocktackId(
			String stocktackId) throws ServiceException {
		DmmAssetStacktakeListDTO assetStocktackInfoDTO = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue(),stocktackId);
			assetStocktackInfoDTO =  (DmmAssetStacktakeListDTO) this.doService(IAtomsConstants.SERVICE_DMM_ASSET_STACKTAKE_SERVICE, IAtomsConstants.ACTION_GET_ASSET_STOCKTACK_INFO_BY_STOCKTACK_ID, param);
			return assetStocktackInfoDTO;
		} catch (Exception e) {
			log.error(this.getClass().getName()+".getInventoryNumberList() is error !!!",e);
			throw new ServiceException(e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetInfoList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<Parameter> getAssetInfoList(String userId, String queryFromDateStart,String queryFromDateEnd,String queryToDateStart,String queryToDateEnd,String queryFromWarehouseId,String queryToWarehouseId)
			throws ServiceException {
		List<Parameter> assetInfoList = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(IAtomsLogonUser.FIELD_ID, userId);
			param.addParameter(AssetTransInfoHistoryFormDTO.QUERY_ASSET_FROM_DATE_START, queryFromDateStart);
			param.addParameter(AssetTransInfoHistoryFormDTO.QUERY_ASSET_FROM_DATE_END, queryFromDateEnd);
			param.addParameter(AssetTransInfoHistoryFormDTO.QUERY_ASSET_TO_DATE_START, queryToDateStart);
			param.addParameter(AssetTransInfoHistoryFormDTO.QUERY_ASSET_TO_DATE_END, queryToDateEnd);
			param.addParameter(AssetTransInfoHistoryFormDTO.QUERY_ASSET_FROM_WARE, queryFromWarehouseId);
			param.addParameter(AssetTransInfoHistoryFormDTO.QUERY_ASSET_TO_WARE, queryToWarehouseId);
			assetInfoList = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_DMM_ASSET_TRANS_INFO_SERVICE, IAtomsConstants.ACTION_GET_ASSET_INFO_LIST, param);
			return assetInfoList;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception listContractIdByVendor()"+ e , e);
			throw new ServiceException(e);
		}
		
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetTransInfoById(java.lang.String)
	 */
	public String getAssetTransInfoById(String assetTransId)
			throws ServiceException {
		AssetTransInfoHitoryDTO assetTransInfoHitoryDTO = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue(),assetTransId);
			assetTransInfoHitoryDTO = (AssetTransInfoHitoryDTO) this.doService(IAtomsConstants.SERVICE_DMM_ASSET_TRANS_INFO_HISTORY_SERVICE, IAtomsConstants.ACTION_GET_ASSET_INFO_BY_ID, param);
			Gson gsonss = new GsonBuilder().create();
			String result = gsonss.toJson(assetTransInfoHitoryDTO);
			return result;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getAssetTransInfoById()"+ e , e);
			throw new ServiceException(e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getRoleCode()
	 */
	@Override
	public List<Parameter> getRoleCode() throws ServiceException {
		List<Parameter> list;
		try {
			list = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ADM_ROLE_SERVICE, IAtomsConstants.ACTION_GET_ROLE_CODE, null);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getRoleCode()"+ e , e);
			throw new ServiceException(e);
		}
		return list;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getDeptByCompanyId(java.lang.String)
	 */
	@Override
	public List<Parameter> getDeptList(String companyId, Boolean ignoreDeleted) throws ServiceException {
		List<Parameter> deptList = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue(),companyId);
			// 忽略刪除
			param.addParameter(IAtomsConstants.PARAM_IGNORE_DELETED, ignoreDeleted);
			deptList = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_BIM_DEPARTMENT_SERVICE, IAtomsConstants.ACTION_GET_DEPT_LIST, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getDeptByCompanyId()"+ e , e);
			throw new ServiceException(e);
		}
		return deptList;
	}
	
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getUserByDeparment(java.lang.String)
	 */
	@Override
	public List<Parameter> getUserByDeparment(String departmentId)
			throws ServiceException {
		List<Parameter> userList = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue(),departmentId);
			userList = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_REPOSITORY_SERVICE, IAtomsConstants.ACTION_GET_USER_BY_DEPT, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getUserByDeparment()"+ e , e);
			throw new ServiceException(e);
		}
		return userList;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getWorkFlowRoleList(java.lang.String)
	 */
	public List<Parameter> getWorkFlowRoleList(String attributeValue) throws ServiceException {
		List<Parameter> workFlows = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			if (StringUtils.hasText(attributeValue)) {
				param.addParameter(AdmRoleDTO.ATTRIBUTE.ATTRIBUTE.getValue(), attributeValue);
				workFlows = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ADM_ROLE_SERVICE, IAtomsConstants.ACTION_GET_WORK_FLOW_ROLE, param);
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getWorkFlowRoleList()"+ e , e);
			throw new ServiceException(e);
		}
		return workFlows;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#checkUseRole(java.lang.String)
	 */
	public Boolean checkUseRole(String roleId) throws SecurityException {
		Boolean isUse = true;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			if (StringUtils.hasText(roleId)) {
				param.addParameter(AdmRoleDTO.ATTRIBUTE.ROLE_ID.getValue(), roleId);
				isUse = (Boolean) this.doService(IAtomsConstants.SERVICE_ADM_ROLE_SERVICE, IAtomsConstants.ACTION_CHECK_USE_ROLE, param);
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception checkUseRole()"+ e , e);
			throw new ServiceException(e);
		}
		return isUse;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getReportNameList(java.lang.String)
	 */
	@Override
	public List<Parameter> getReportNameList(String customerId) throws ServiceException {
		List<Parameter> reportNames = null;
	    try {
	    	MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(ReportSettingDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
			reportNames = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_REPORT_SETTING_SERVICE, 
									IAtomsConstants.ACTION_GET_PRE_REPORT_CODE_LIST, param);
	    } catch (Exception e) {
	    	log.error(this.getClass().getSimpleName() + "Exception getStorageList-->" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE,e);
	    }
		return reportNames;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getReportDetailList(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Parameter> getReportDetailList(String reportCode, String bptdCode, String detailCode) throws ServiceException {
		List<Parameter> reportDetails = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_ITEM_VALUE, reportCode);
			param.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_BPTD_CODE, bptdCode);
			param.addParameter(BaseParameterManagerFormDTO.PARAMETER_CHILDREN_BPTD_CODE, detailCode);
			reportDetails = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_BASE_PARAMETER_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_PARAMETER_ITEMS_BY_PARENT , param);
		} catch (Exception e) {
			log.error(this.getClass().getSimpleName() + "Exception getStorageList-->" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE,e);
		}
		return reportDetails;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getContractByCustomerAndStatus(java.lang.String)
	 */
	@Override
	public List<Parameter> getContractByCustomerAndStatus(String customerId, String status, String companyType) throws ServiceException {
		// 合同列表下拉框
		List<Parameter> contractList = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			// 設置客户编号
			param.addParameter(ContractSlaFormDTO.ORIGINAL_CUSTOMER_ID, customerId);
			// 設置合約狀態
			param.addParameter(BimContractDTO.ATTRIBUTE.CONTRACT_STATUS.getValue(), status);
			// 公司類型
			param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), companyType);
			contractList = (List<Parameter>) this.doService(IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_CONTRACT_BY_CUSTOMER_AND_STATUS, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getContractByCustomerAndStatus()"+ e , e);
			throw new ServiceException(e);
		}
		return contractList;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getContractCodeList(java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public List<Parameter> getContractCodeList(String customerId, String status, Boolean isHaveSla, Boolean ignoreDeleted) throws ServiceException {
		// 合同列表下拉框
		List<Parameter> contractList = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			// 設置客户编号
			param.addParameter(ContractSlaFormDTO.ORIGINAL_CUSTOMER_ID, customerId);
			// 設置合約狀態
			param.addParameter(BimContractDTO.ATTRIBUTE.CONTRACT_STATUS.getValue(), status);
			// 是否有sla信息
			param.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), isHaveSla);
			// 忽略刪除
			param.addParameter(IAtomsConstants.PARAM_IGNORE_DELETED, ignoreDeleted);
			contractList = (List<Parameter>) this.doService(IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_CONTRACT_CODE_LIST, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getContractCodeList()"+ e , e);
			throw new ServiceException(e);
		}
		return contractList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getUnityNameByCompanyId(java.lang.String)
	 */
	public String getUnityNameByCompanyId(String companyId) throws ServiceException {
		String unityName = "";
		try {
			if (StringUtils.hasText(companyId)) {
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
				unityName = (String) this.doService(IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_UNITYNAME_BY_COMPANYID, param);
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getUnityNameByCompanyId()"+ e , e);
			throw new ServiceException(e);
		}
		return unityName;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getCompanyByCompanyCode(java.lang.String)
	 */
	public CompanyDTO getCompanyByCompanyCode(String companyCode) throws ServiceException {
		CompanyDTO company = null;
		try {
			if (StringUtils.hasText(companyCode)) {
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue(), companyCode);
				company = (CompanyDTO) this.doService(IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_BY_COMPANY_CODE, param);
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getUnityNameByCompanyId()"+ e , e);
			throw new ServiceException(e);
		}
		return company;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetTypeList(java.lang.String)
	 */
	public List<Parameter> getAssetTypeList(String assetCategoryId) throws SecurityException {
		List<Parameter> assetTypes = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), assetCategoryId);
			assetTypes = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE,IAtomsConstants.PARAM_ASSET_LIST, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getAssetTypeList()"+ e , e);
			throw new ServiceException(e);
		}
		return assetTypes;
	}
	/**
	 * @return the authenticatorHelper
	 */
	public AuthenticatorHelper getAuthenticatorHelper() {
		return authenticatorHelper;
	}

	/**
	 * @param authenticatorHelper the authenticatorHelper to set
	 */
	public void setAuthenticatorHelper(AuthenticatorHelper authenticatorHelper) {
		this.authenticatorHelper = authenticatorHelper;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getMerchantList(java.lang.String)
	 */
	@Override
	public Map<String, Object> getMerchantList(String merchantId)
			throws ServiceException {
		Map<String, Object> map = new HashMap<String, Object>();
		Boolean flag = false;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(MerchantDTO.ATTRIBUTE.MERCHANT_ID.getValue(), merchantId);
		//	merchantHeaderDTOList = (List<BimMerchantHeaderDTO>) 
			flag = (Boolean)this.doService(IAtomsConstants.SERVICE_MERCHANT_SERVICE, IAtomsConstants.ACTION_GET_MERCHANT_LIST, param);
			if(flag){
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.MERCHANT_HAS_HEADER_NOT_DELETE));
			}
			map.put(IAtomsConstants.PARAM_FLAG,flag);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getMerchantList()"+ e , e);
			throw new ServiceException(e);
		}
		return map;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetTransInfoByKeyId(java.lang.String)
	 */
	/*@Override
	public DmmAssetTransInfoDTO getAssetTransInfoByKeyId(String assetTransId)
			throws ServiceException {
		DmmAssetTransInfoDTO assetTransInfoDTO = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue(), assetTransId);
			assetTransInfoDTO = (DmmAssetTransInfoDTO) this.doService(IAtomsConstants.SERVICE_DMM_ASSET_TRANS_INFO_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TRANS_INFO_DTO_BY_ASSET_TRANS_ID, param);
		} catch (Exception e){
			log.error(this.getClass().getName() + "Exception getAssetTransInfoById()" + e, e);
			throw new ServiceException(e);
		}
		return assetTransInfoDTO;
	}*/

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getContractWarranty(java.lang.String, java.sql.Timestamp)
	 */
	public Integer getContractWarranty(String contractId, String factoryWarranty, String customerWarranty) throws SecurityException {
		Integer warranty;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue(), contractId);
			if (StringUtils.hasText(customerWarranty)) {
				param.addParameter(BimContractDTO.ATTRIBUTE.CUSTOMER_WARRANTY.getValue(), customerWarranty);
			}
			if (StringUtils.hasText(factoryWarranty)) {
				param.addParameter(BimContractDTO.ATTRIBUTE.FACTORY_WARRANTY.getValue(), factoryWarranty);
			}
			warranty = (Integer) this.doService(IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_CONTRACT_WARRANTY, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getContractWarranty()"+ e , e);
			throw new ServiceException(e);
		}
		return warranty;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetTransIds(java.lang.String)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Parameter> getAssetTransIds(String tabType, String userId) throws ServiceException {
		List<Parameter> assetTransIds = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(IAtomsLogonUser.FIELD_ID, userId);
			param.addParameter(AssetTransInfoFormDTO.TAB_TYPE, tabType);
			assetTransIds =  (List<Parameter>) this.doService(IAtomsConstants.SERVICE_CHECK_TRANS_INFO_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TRANS_ID_LIST, param);
		} catch (Exception e){
			log.error(this.getClass().getName() + "Exception getAssetTransInfoById()" + e, e);
			throw new ServiceException(e);
		}
		return assetTransIds;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getStocktackIdByWarehouseAndAssetType(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Parameter> getStocktackIdByWarehouseAndAssetType(String warehouseId, String assetTypeIdList) throws ServiceException {
		List<Parameter> stocktackIds = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			if (StringUtils.hasText(warehouseId)) {
				param.addParameter(WarehouseDTO.ATTRIBUTE.WAREHOUSE_ID.getValue(), warehouseId);
			}
			if (StringUtils.hasText(assetTypeIdList)) {
				param.addParameter(DmmAssetStacktakeInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), assetTypeIdList);
			}
			stocktackIds =  (List<Parameter>) this.doService(IAtomsConstants.SERVICE_DMM_ASSET_STACKTAKE_SERVICE, IAtomsConstants.ACTION_GET_STOCKTACK_ID_LIST, param);
		} catch (Exception e){
			log.error(this.getClass().getName() + "Exception getAssetTransInfoById()" + e, e);
			throw new ServiceException(e);
		}
		return stocktackIds;
	}

	
	@Override
	public List<Parameter> getAssetListTypeByCustomer(String customerId) throws ServiceException {
		List<Parameter> assetTypeLists = null;
		try {
			if (StringUtils.hasText(customerId)) {
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(ApplicationDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				assetTypeLists = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_APPLICATION_SERVICE,IAtomsConstants.ACTION_GET_ASSET_TYPE_BY_CUSTOMER, param);
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getAssetTypeList()"+ e , e);
			throw new ServiceException(e);
		}
		return assetTypeLists;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetList(java.lang.String)
	 */
	@Override
	public Map<String, Object> getAssetList(String editAssetTypeId) throws ServiceException {
		Boolean flag = false;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), editAssetTypeId);
			flag = (Boolean)this.doService(IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_IS_ASSET_LIST, param);
			if(flag){
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.ASSET_HAVE_INVENTORY));
			}
			map.put(IAtomsConstants.PARAM_FLAG,flag);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getAssetList()"+ e , e);
			throw new ServiceException(e);
		}
		return map;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getWareHouseUserNameList(java.lang.String)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Parameter> getWareHouseUserNameList(String assetTransId) throws ServiceException {
		List<Parameter> wareHouseUserNameList = null;
		try {
			if (StringUtils.hasText(assetTransId)) {
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue(), assetTransId);
				wareHouseUserNameList = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_DMM_ASSET_TRANS_INFO_SERVICE,IAtomsConstants.ACTION_GET_WARE_HOUSE_USER_NAME_LIST, param);
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getWareHouseUserNameList()"+ e , e);
			throw new ServiceException(e);
		}
		return wareHouseUserNameList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#checkDownLoadFile(java.lang.String)
	 */
	public boolean checkDownLoadFile(String fileId) throws ServiceException {
		boolean exsitFile = false;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(BimContractAttachedFileDTO.ATTRIBUTE.ATTACHED_FILE_ID.getValue(), fileId);
			String filePath = (String) this.doService(IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_FILE_PATH, param);
			if (StringUtils.hasText(filePath)) {
				File file=new File(filePath);
				if(file.exists()){
					return true;
				}
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception checkDownLoadFile()"+ e , e);
			throw new ServiceException(e);
		}
		return exsitFile;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#checkDownLoadFile(java.lang.String)
	 */
	public boolean checkCaseTemplatesDownLoadFile(String fileId) throws ServiceException {
		boolean exsitFile = false;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseTemplatesDTO.ATTRIBUTE.ID.getValue(), fileId);
			String filePath = (String) this.doService(IAtomsConstants.SERVICE_SRM_CASE_TEMPLATES_SERVICE, IAtomsConstants.ACTION_GET_FILE_PATH, param);
			if (StringUtils.hasText(filePath)) {
				File file=new File(filePath);
				if(file.exists()){
					return true;
				}
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception checkDownLoadFile()"+ e , e);
			throw new ServiceException(e);
		}
		return exsitFile;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetModelAndBrand(java.lang.String)
	 */
	public Map<String, List<Parameter>> getAssetModelAndBrand(String assetId) throws ServiceException {
		List<Parameter> assetModels = null;
		List<Parameter> assetBrands = null;
		Map<String, List<Parameter>> map = new HashMap<String, List<Parameter>>();
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), assetId);
			assetModels = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.GET_ASSET_MODEL_LIST, param);
			assetBrands = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.GET_ASSET_BRAND_LIST, param);
			map.put(AssetTypeDTO.ATTRIBUTE.MODEL.getValue(), assetModels);
			map.put(AssetTypeDTO.ATTRIBUTE.BRAND.getValue(), assetBrands);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getAssetModel()"+ e , e);
			throw new ServiceException(e);
		}
		return map;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetModelList(java.lang.String)
	 */
	@Override
	public List<Parameter> getAssetModelList(String assetTypeId) throws ServiceException {
		List<Parameter> assetModels = null;
		try {
			if (StringUtils.hasText(assetTypeId)) {
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), assetTypeId);
				assetModels = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.GET_ASSET_MODEL_LIST, param);
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getAssetModelList()"+ e , e);
			throw new ServiceException(e);
		}
		return assetModels;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getMerchantHeaderDTOById(java.lang.String)
	 */
	public BimMerchantHeaderDTO getMerchantHeaderDTOById(String merchantHeaderId) throws ServiceException {
		BimMerchantHeaderDTO merchantHeaderDTO = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue(), merchantHeaderId);
			merchantHeaderDTO = (BimMerchantHeaderDTO) this.doService(IAtomsConstants.SERVICE_MERCHANT_HEADER_SERVICE, IAtomsConstants.GET_MERCHANT_HEADER_BY_ID, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getAssetModel()"+ e , e);
			throw new ServiceException(e);
		}
		return merchantHeaderDTO;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getMerchantDTOById(java.lang.String, java.lang.String, java.lang.String)
	 */
	public MerchantDTO getMerchantDTOById(String merchantId, String merchantCode,String customerId) throws ServiceException {
		MerchantDTO merchantDTO = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(MerchantDTO.ATTRIBUTE.MERCHANT_ID.getValue(), merchantId);
			param.addParameter(MerchantDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), merchantCode);
			param.addParameter(MerchantDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
			merchantDTO = (MerchantDTO) this.doService(IAtomsConstants.SERVICE_MERCHANT_SERVICE, IAtomsConstants.GET_MERCHANTDTO_BY, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getAssetModel()"+ e , e);
			throw new ServiceException(e);
		}
		return merchantDTO;
	}
	
	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getMerchantsByCodeAndCompamyId(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Parameter> getMerchantsByCodeAndCompamyId(String merchantCode, String customerId) throws ServiceException {
		// TODO Auto-generated method stub
		List<Parameter> merchants = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(MerchantDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), merchantCode);
			param.addParameter(MerchantDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
			merchants = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_MERCHANT_SERVICE, IAtomsConstants.GET_MERCHANS_BY_CODE_AND_COMPANYID, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getAssetModel()"+ e , e);
			throw new ServiceException(e);
		}
		return merchants;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getMerchantHeaderList(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Parameter> getMerchantHeaderList(String customerId, String merchantCode, String merchantId) throws ServiceException {
		List<Parameter> merchantHeaders = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_ID.getValue(), merchantId);
			param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), merchantCode);
			param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
			merchantHeaders = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_MERCHANT_HEADER_SERVICE, IAtomsConstants.GET_MERCHANT_HEADER_LIST, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getMerchantHeader()"+ e , e);
			throw new ServiceException(e);
		}
		return merchantHeaders;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getCaseMessageByDTID(java.lang.String, java.lang.String)
	 */
	public SrmCaseHandleInfoDTO getCaseMessageByDTID(String dtid, String customerId, String isNewFlag,String isCheck) throws ServiceException {
		SrmCaseHandleInfoDTO caseHandleInfoDTO = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), dtid);
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
			 // 如果設備異動 客服帶值 完修時 查最新資料檔 2018/01/04 Bug #3055
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_NEW_CASE.getValue(), isNewFlag);
			//查核案件 查詢最新資料檔 2018/01/30
			param.addParameter(IAtomsConstants.CASE_CATEGORY.CHECK.getCode(), isCheck);
			caseHandleInfoDTO = (SrmCaseHandleInfoDTO) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_CASE_MESSAGE_BY_CASEID, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getCaseMessageByCaseId()"+ e , e);
			throw new ServiceException(e);
		}
		return caseHandleInfoDTO;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getBuiltInFeature(java.lang.String)
	 */
	public List<Parameter> getBuiltInFeature(String edcType) throws ServiceException {
		List<Parameter> builtInFeatures = null;
		try {
			MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
			parameterInquiryContext.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), edcType);
			builtInFeatures = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_BUILT_IN_FEATURE, parameterInquiryContext);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getBuiltInFeature()"+ e , e);
			throw new ServiceException(e);
		}
		return builtInFeatures;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getComboboxValueByContractId(java.lang.String)
	 */
	/*public Map getComboboxValueByContractId(String contractId, String type) throws ServiceException {
		List<Parameter> peripherals = null;
		List<Parameter> edcType = null;
		List<Parameter> vendors = null;
		Map map = new HashMap();
		try {
			MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
			parameterInquiryContext.addParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue(), contractId);
			parameterInquiryContext.addParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.ASSET_CATEGORY_EDC);
			edcType = (List<Parameter>) this.doService(IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_PERIPHERALS, parameterInquiryContext);
			vendors = (List<Parameter>) this.doService(IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_VENDORS, parameterInquiryContext);
			parameterInquiryContext.addParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), type);
			peripherals = (List<Parameter>) this.doService(IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_PERIPHERALS, parameterInquiryContext);
			map.put("peripherals", peripherals);
			map.put("edcType", edcType);
			map.put("vendors", vendors);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getComboboxValueByContractId()"+ e , e);
			throw new ServiceException(e);
		}
		return map;
	}*/

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getSoftwareVersions(java.lang.String, java.lang.String)
	 */
	public List<Parameter> getSoftwareVersions(String customerId, String edcType, String searchDeletedFlag) throws ServiceException {
		List<Parameter> softwareVersions = null;
		try {
			MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
			parameterInquiryContext.addParameter(ApplicationDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
			parameterInquiryContext.addParameter(ApplicationDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), edcType);
			parameterInquiryContext.addParameter(ApplicationDTO.ATTRIBUTE.SEARCH_DELETED_FLAG.getValue(), searchDeletedFlag);
			softwareVersions = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_APPLICATION_SERVICE, IAtomsConstants.ACTION_GET_SOFTWAREVERSIONS, parameterInquiryContext);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getSoftwareVersions()"+ e , e);
			throw new ServiceException(e);
		}
		return softwareVersions;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#saveSystemLog(java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void saveSystemLog(String actionId, String logContent, String ucNo, HttpServletRequest request)throws ServiceException {
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			IAtomsLogonUser logonUser = (IAtomsLogonUser)SessionHelper.getLogonUser(request);
			if (IAtomsConstants.UC_NO_ADM_01010.equals(ucNo)) {
				param.addParameter(IAtomsConstants.FIELD_ACTION_ID, actionId);
				param.addParameter(AdmUserFormDTO.PARAM_LOG_CONTENT, logContent);
				param.addParameter(AdmUserFormDTO.PARAM_USER_LOGON_USER, logonUser);
				param.addParameter(AdmUserFormDTO.PARAM_USER_UC_NO, ucNo);
				this.doService(IAtomsConstants.SERVICE_ADM_USER_SERVICE, IAtomsConstants.ACTION_SAVE_SYSTEM_LOG, param);
			} else if (IAtomsConstants.UC_NO_ADM_01030.equals(ucNo)){
				param.addParameter(IAtomsConstants.FIELD_ACTION_ID, actionId);
				param.addParameter(AdmRoleFormDTO.PARAM_LOG_CONTENT, logContent);
				param.addParameter(AdmRoleFormDTO.PARAM_USER_LOGON_USER, logonUser);
				param.addParameter(AdmRoleFormDTO.PARAM_USER_UC_NO, ucNo);
				this.doService(IAtomsConstants.SERVICE_ADM_ROLE_SERVICE, IAtomsConstants.ACTION_SAVE_SYSTEM_LOG, param);
			} 
		} catch (Exception e){
			log.error(this.getClass().getName() + "Exception saveSystemLog()" + e, e);
			throw new ServiceException(e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getConnectionType(java.lang.String)
	 */
	public List<Parameter> getConnectionType(String assetTypeId) throws ServiceException {
		List<Parameter> connectionTypes = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), assetTypeId);
			connectionTypes =  (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_CONNECTION_TYPE_LIST, param);
		} catch (Exception e){
			log.error(this.getClass().getName() + "Exception getConnectionType()" + e, e);
			throw new ServiceException(e);
		}
		return connectionTypes;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#checkCaseFile(java.lang.String)
	 */
	public Boolean checkCaseFile(String attFileId, String isHistory) throws ServiceException {
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseAttFileDTO.ATTRIBUTE.ATT_FILE_ID.getValue(), attFileId);
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_HISTORY.getValue(), isHistory);
			String filePath = (String) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_CHECK_CASE_ATT_FILE, param);
			if (StringUtils.hasText(filePath)) {
				File file=new File(filePath);
				if(file.exists()){
					return true;
				}
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception checkCaseFile()" + e, e);
			throw new ServiceException(e);
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#checkDtidType(java.lang.String)
	 */
	public Boolean checkDtidType(String companyId) throws ServiceException {
		Boolean isEquals = false;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
			isEquals = (Boolean) this.doService(IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_IS_DTID_TYPE, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception checkCaseFile()" + e, e);
			throw new ServiceException(e);
		}
		return isEquals;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#isUseDtid(java.lang.String)
	 */
	public Boolean isUseDtid(String id) throws ServiceException {
		Boolean isUse = false;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(PvmDtidDefDTO.ATTRIBUTE.ID.getValue(), id);
			isUse = (Boolean) this.doService(IAtomsConstants.SERVICE_DTID_DEF_SERVICE, IAtomsConstants.IS_USE_DTID, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception isUseDtid()" + e, e);
			throw new ServiceException(e);
		}
		return isUse;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#checkDtidNumber(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, Object> checkDtidNumber(String caseNumber, String customerId, String edcType, Boolean isSingle) throws ServiceException {
		Map<String, Object> map = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_NUMBER.getValue(), caseNumber);
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE.getValue(), edcType);
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_SINGLE.getValue(), isSingle);
			map = (Map<String, Object>) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_CHECK_DTID_NUMBER, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception checkDtidNumber()" + e, e);
			throw new ServiceException(e);
		}
		return map;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getPayInfo(java.lang.String, java.lang.String, java.lang.String)
	 */
	public SrmCaseHandleInfoDTO getPayInfo(String dtid, String customerId, String caseId) throws ServiceException {
		SrmCaseHandleInfoDTO paymentInfoDTO = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), dtid);
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
			paymentInfoDTO = (SrmCaseHandleInfoDTO) this.doService(IAtomsConstants.SERVICE_PAYMENT_SERVICE, IAtomsConstants.ACTION_GET_PAY_INFO, param);
		} catch (Exception e) {
			log.error("getPayInfo", "Exception checkDtidNumber()", e);
			throw new ServiceException(e);
		}
		return paymentInfoDTO;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getSuppliesTypeNameList(java.lang.String)
	 */
	public List<Parameter> getSuppliesTypeNameList(String customerId, String suppliesType) throws ServiceException {
		List<Parameter> suppliesTypeNameList = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(PaymentFormDTO.PARAM_QUERY_COMPANY_ID, customerId);
			param.addParameter(DmmSuppliesDTO.ATTRIBUTE.SUPPLIES_TYPE.getValue(), suppliesType);
			suppliesTypeNameList = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_SUPPLIES_TYPE_SERVICE, IAtomsConstants.ACTION_GET_SUPPLIES_TYPE_NAME_LIST, param);
			//将集合转化为JSON字符串
			/*Gson gsonss = new GsonBuilder().create();
			suppliesTypeString = gsonss.toJson(suppliesTypeNameList);*/
		} catch (Exception e) {
			log.error("getSuppliesTypeNameList", "Exception checkDtidNumber()", e);
			throw new ServiceException(e);
		}
		return suppliesTypeNameList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getPaymentItemByItemIds(java.lang.String)
	 */
	public List<SrmPaymentItemDTO> getPaymentItemByItemIds (String itemId, String updateDate) throws ServiceException {
		List<SrmPaymentItemDTO> paymentItemDTOs = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			/*GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
				@Override
				public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
					return new Date(json.getAsJsonPrimitive().getAsLong());
				}
			});
			Gson gsons = builder.create();
			List<SrmPaymentItemDTO> updateColumns = (List<SrmPaymentItemDTO>) gsons.fromJson(updateDate, new TypeToken<List<SrmPaymentItemDTO>>(){}.getType());*/
			param.addParameter(SrmPaymentItemDTO.ATTRIBUTE.ITEM_ID.getValue(), itemId);
 			param.addParameter(SrmPaymentItemDTO.ATTRIBUTE.CHECK_UPDATE_DATE.getValue(), updateDate);
			paymentItemDTOs = (List<SrmPaymentItemDTO>) this.doService(IAtomsConstants.SERVICE_PAYMENT_SERVICE, IAtomsConstants.ACTION_GET_PAYMENT_ITEM_BY_PAYMENT_ID, param);
		} catch (Exception e) {
			log.error("getPaymentItemByPaymentId", "Exception checkDtidNumber()", e);
			throw new ServiceException(e);
		}
		return paymentItemDTOs;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getUserByDepartmentAndRole(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Parameter> getUserByDepartmentAndRole(String deptCode, String roleCode, boolean flag, boolean isDeptAgent) throws ServiceException {
		List<Parameter> users = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue(), deptCode);
			param.addParameter(AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue(), roleCode);
			param.addParameter(IAtomsConstants.PARAM_FLAG, flag);
			param.addParameter("isDeptAgent", isDeptAgent);
			users =  (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ADM_ROLE_SERVICE, IAtomsConstants.ACTION_GET_USEER_BY_DEPT_AND_ROLE, param);
		} catch (Exception e){
			log.error(this.getClass().getName() + "Exception getUserByDepartmentAndRole()" + e, e);
			throw new ServiceException(e);
		}
		return users;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getDeptByCompanyId(java.lang.String)
	 */
	@Override
	public List<Parameter> getDeptByCompanyId(String companyId) throws ServiceException {
		List<Parameter> deptList = null;
		Parameter tempParameter = null;
		try {
			// 羣組集合
			List<Parameter> groupList = new ArrayList<Parameter>();
			// 添加客服
			tempParameter = new Parameter();
			tempParameter.setValue(IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode());
			tempParameter.setName(i18NUtil.getName(IAtomsConstants.FIELD_CASE_ROLE_CUSTOMER_SERVICE));
			groupList.add(tempParameter);
			// 添加TMS
			tempParameter = new Parameter();
			tempParameter.setValue(IAtomsConstants.CASE_ROLE.TMS.getCode());
			tempParameter.setName(IAtomsConstants.CASE_ROLE.TMS.getCode());
			groupList.add(tempParameter);
			// 添加QA
			tempParameter = new Parameter();
			tempParameter.setValue(IAtomsConstants.CASE_ROLE.QA.getCode());
			tempParameter.setName(IAtomsConstants.CASE_ROLE.QA.getCode());
			groupList.add(tempParameter);
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
			deptList = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_BIM_DEPARTMENT_SERVICE, IAtomsConstants.ACTION_GET_CDEPARTMENT_LIST, param);
			// 	沒有集合創建
			if(CollectionUtils.isEmpty(deptList)){
				deptList = new ArrayList<Parameter>();
			}
			deptList.addAll(groupList);
		} catch (Exception e){
			log.error(this.getClass().getName() + "Exception getDeptByCompanyId()" + e, e);
			throw new ServiceException(e);
		}
		return deptList;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getCaseRepeatList(java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, Object> getCaseRepeatList(String dtid, String caseId, boolean isSignFlag)
			throws ServiceException {
		Map<String, Object> map = new HashMap<String, Object>();
		//List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOList = null;
		Boolean flag = false;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), dtid);
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
			//查看改筆案件是否有簽收或者線上排除的記錄
			Boolean isSignAndOnlineExclusion = (Boolean) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.IS_SIGN_AND_ONLINE_EXCLUSION, param);
			//如果沒有需要驗證重複進建 CR #2551 update 簽收案驗證時不需要卡是否完修
			if((!isSignAndOnlineExclusion) || isSignFlag) {
				map =  (Map<String, Object>) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.GET_CASE_REPEAT_LIST, param);
				if(!CollectionUtils.isEmpty(map)) {
					flag = true;
				}
			}
			map.put(IAtomsConstants.PARAM_FLAG,flag);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getMerchantList()"+ e , e);
			throw new ServiceException(e);
		}
		return map;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getCaseRepeatByInstallUncomplete(java.lang.String)
	 */
	@Override
	public Map<String, Object> getCaseRepeatByInstallUncomplete(String dtid, String caseId)
			throws ServiceException {
		Map<String, Object> map = new HashMap<String, Object>();
		Boolean flag = false;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), dtid);
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
				map =  (Map<String, Object>) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.GET_CASE_REPEAT_BY_INSTALL_UNCOMPLETE, param);
				if(!CollectionUtils.isEmpty(map) && (Boolean) map.get(IAtomsConstants.IS_CHANGE_CASE_LINK)) {
					flag = true;
				}
			map.put(IAtomsConstants.PARAM_FLAG,flag);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getCaseRepeatByInstallUncomplete()"+ e , e);
			throw new ServiceException(e);
		}
		return map;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getCaseLinkIsChange(java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, Object> getCaseLinkIsChange(String dtid, String caseId)
			throws ServiceException {
		Map<String, Object> map = new HashMap<String, Object>();
		Boolean flag = false;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), dtid);
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
			//查看改筆案件是否有簽收或者線上排除的記錄
			//Boolean isSignAndOnlineExclusion = (Boolean) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.IS_SIGN_AND_ONLINE_EXCLUSION, param);
			//if(!isSignAndOnlineExclusion) {
				map = (Map<String, Object>) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.GET_CASELINK_IS_CHANGE, param);
				if(map.get(IAtomsConstants.IS_CHANGE_CASE_LINK).equals(true)){
					flag = true;
				}
				//最新資料檔 是否有設備 2018/01/30
				if (map.containsKey(IAtomsConstants.INIT_EDIT_CHECK_UPDATE) && IAtomsConstants.PARAM_YES.equals(map.get(IAtomsConstants.INIT_EDIT_CHECK_UPDATE))) {
					map.put(IAtomsConstants.INIT_EDIT_CHECK_UPDATE, IAtomsConstants.PARAM_YES);
				} else {
					map.put(IAtomsConstants.INIT_EDIT_CHECK_UPDATE, IAtomsConstants.PARAM_NO);
				}
			//}
			map.put(IAtomsConstants.PARAM_FLAG,flag);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getMerchantList()"+ e , e);
			throw new ServiceException(e);
		}
		return map;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getSuppliesListByCustomseId(java.lang.String)
	 */
	@Override
	public Map<String, Object>  getSuppliesListByCustomseId(String customerId)
			throws ServiceException {
		List<Parameter> suppliesTypeList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		Boolean flag = false;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(DmmSuppliesDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
			suppliesTypeList = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_SUPPLIES_TYPE_SERVICE, IAtomsConstants.PARAM_GET_SUPPLIES_LIST_BY_CUSTOMERID, param);
			Gson gsonss = new GsonBuilder().create();
			String suppliesTypeListString = gsonss.toJson(suppliesTypeList);
			if(StringUtils.hasText(suppliesTypeListString)) {
				flag = true;
				map.put("jsonData",suppliesTypeListString);
			}
			map.put(IAtomsConstants.PARAM_FLAG,flag);
			return map;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getSuppliesListByCustomseId()"+ e , e);
			throw new ServiceException(e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getSuppliesNameByCustomseId(java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, Object> getSuppliesNameList(String customerId,
			String suppliesType) throws ServiceException {
		List<Parameter> suppliesNameList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		Boolean flag = false;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(DmmSuppliesDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
			param.addParameter(DmmSuppliesDTO.ATTRIBUTE.SUPPLIES_TYPE.getValue(), suppliesType);
			suppliesNameList = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_SUPPLIES_TYPE_SERVICE, IAtomsConstants.PARAM_GET_SUPPLIES_NAME_LIST, param);
			Gson gsonss = new GsonBuilder().create();
			String suppliesNameListString = gsonss.toJson(suppliesNameList);
			if(StringUtils.hasText(suppliesNameListString)) {
				flag = true;
				map.put("jsonData",suppliesNameListString);
			}
			map.put(IAtomsConstants.PARAM_FLAG,flag);
			return map;
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getSuppliesNameList()"+ e , e);
			throw new ServiceException(e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getUploadTemplatesId(java.lang.String, java.lang.String)
	 */
	@Override
	public String getUploadTemplatesId(String uploadCategory, String fileName)
			throws ServiceException {
		String templatesId = "";
		try {
			if (StringUtils.hasText(uploadCategory) && StringUtils.hasText(fileName)) {
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(SrmCaseTemplatesDTO.ATTRIBUTE.CATEGORY.getValue(), uploadCategory);
				param.addParameter(SrmCaseTemplatesDTO.ATTRIBUTE.FILE_NAME.getValue(), fileName);
				templatesId = (String) this.doService(IAtomsConstants.SERVICE_SRM_CASE_TEMPLATES_SERVICE, IAtomsConstants.PARAM_GET_UPLOAD_TEMPLATES_ID, param);
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getUploadTemplatesId()"+ e , e);
			throw new ServiceException(e);
		}
		return templatesId;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getCaseInfoById(java.lang.String,java.lang.String)
	 */
	@Override
	public Map getCaseInfoById(String caseId, String dtoFlag) throws ServiceException {
		Map map = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
			param.addParameter(IAtomsConstants.PARAM_FLAG, dtoFlag);
			map = (HashMap) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_CASE_INFO_BY_ID, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getCaseInfoById()"+ e , e);
			throw new ServiceException(e);
		}
		return map;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getChangeCaseInfoById(java.lang.String)
	 */
	@Override
	public Map getChangeCaseInfoById(String caseId) throws ServiceException {
		Map map = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
			map = (HashMap) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_CHANGE_CASE_INFO_BY_ID, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getChangeCaseInfoById()"+ e , e);
			throw new ServiceException(e);
		}
		return map;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAoEmailByCaseId(java.lang.String)
	 */
	@Override
	public Map<String, Object> getAoEmailByCaseId(String caseId)
			throws ServiceException {
		Map map = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
			map =  (HashMap) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_AOEMAIL_BY_CASEID, param);
		} catch (Exception e){
			log.error(this.getClass().getName() + "Exception getAoEmailByCaseId()" + e, e);
			throw new ServiceException(e);
		}
		return map;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getcaseInfoEmailByCaseId(java.lang.String)
	 */
	@Override
	public Map<String, Object> getcaseInfoEmailByCaseId(String caseId, String actionId)
			throws ServiceException {
		Map map = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
			param.addParameter("actionId", actionId);
			map =  (HashMap) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_CASE_EMAIL_BY_CASEID, param);
		} catch (Exception e){
			log.error(this.getClass().getName() + "Exception getcaseInfoEmailByCaseId()" + e, e);
			throw new ServiceException(e);
		}
		return map;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getAssetListForCase(java.lang.String)
	 */
	public List<Parameter> getAssetListForCase(String customerId, String assetCategory, Boolean ignoreDeleted) throws ServiceException {
		List<Parameter> edcAssets = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(AssetTypeDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
			// 設備類別 Task #2496 
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), assetCategory);
			// 忽略刪除
			param.addParameter(IAtomsConstants.PARAM_IGNORE_DELETED, ignoreDeleted);
			edcAssets =  (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_ASSET_LIST_FOR_CASE, param);
		} catch (Exception e){
			log.error(this.getClass().getName() + "Exception getAssetListForCase()" + e, e);
			throw new ServiceException(e);
		}
		return edcAssets;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getVendersByContractId(java.lang.String)
	 */
	public List<Parameter> getVendersByContractId(String contractId)
			throws ServiceException {
		List<Parameter> vendors = null;
		try {
			MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
			parameterInquiryContext.addParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue(), contractId);
			vendors = (List<Parameter>) this.doService(IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_VENDORS, parameterInquiryContext);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getComboboxValueByContractId()"+ e , e);
			throw new ServiceException(e);
		}
		return vendors;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#initEditCheckUpdate(java.lang.String, java.sql.Timestamp)
	 */
	@Override
	public Boolean initEditCheckUpdate(String paymentId, Timestamp updatedDate) throws ServiceException {
		Boolean isUpdate = Boolean.FALSE;
		try {
			MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
			parameterInquiryContext.addParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ID.getValue(), paymentId);
			parameterInquiryContext.addParameter(SrmPaymentInfoDTO.ATTRIBUTE.UPDATED_DATE.getValue(), updatedDate);
			isUpdate = (Boolean) this.doService(IAtomsConstants.SERVICE_PAYMENT_SERVICE, IAtomsConstants.INIT_EDIT_CHECK_UPDATE, parameterInquiryContext);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getComboboxValueByContractId()"+ e , e);
			throw new ServiceException(e);
		}
		return isUpdate;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getCountByInstall(java.lang.String, java.lang.String)
	 */
	@Override
	public String getCountByInstall(String dtid, String caseId) throws ServiceException {
		String isForCancel = null;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), dtid);
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
			isForCancel = (String) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_COUNT_BY_INSTALL, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getChangeCaseInfoById()"+ e , e);
			throw new ServiceException(e);
		}
		return isForCancel;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#checkRoleRepeat(java.lang.String, java.lang.String)
	 */
	@Override
	public String checkRoleRepeat(String roleCode, String roleName, String roleId) throws ServiceException {
		String repeatName = null;
		try {
			MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
			parameterInquiryContext.addParameter(AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue(), roleCode);
			parameterInquiryContext.addParameter(AdmRoleDTO.ATTRIBUTE.ROLE_NAME.getValue(), roleName);
			parameterInquiryContext.addParameter(AdmRoleDTO.ATTRIBUTE.ROLE_ID.getValue(), roleId);
			repeatName = (String) this.doService(IAtomsConstants.SERVICE_ADM_ROLE_SERVICE, IAtomsConstants.CHECK_ROLE_REPEAT, parameterInquiryContext);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception checkRoleRepeat()"+ e , e);
			throw new ServiceException(e);
		}
		return repeatName;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#checkAssetUserIsTaixinRent(java.lang.String)
	 */
	@Override
	public String checkAssetUserIsTaixinRent(String serialNumbers) throws ServiceException {
		String msg = null;
		try {
			MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
			parameterInquiryContext.addParameter(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumbers);
			msg = (String) this.doService(IAtomsConstants.SERVICE_REPOSITORY_SERVICE, IAtomsConstants.CHECK_ASSET_USER_IS_TAIXIN_RENT, parameterInquiryContext);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception checkAssetUserIsTaixinRent()"+ e , e);
			throw new ServiceException(e);
		}
		return null;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getMailGroupList()
	 */
	@Override
	public List<Parameter> getMailGroupList() throws ServiceException {
		List<Parameter> mailgroupListString = null;
		try {
			mailgroupListString = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ADM_USER_SERVICE, IAtomsConstants.ACTION_GET_MAIL_GROUP_LIST, null);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception checkAssetUserIsTaixinRent()"+ e , e);
			throw new ServiceException(e);
		}
		return mailgroupListString;
		
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getNameList()
	 */
	@Override
	public String getNameList() throws ServiceException {
		String mailgroupListString = null;
		try {
			mailgroupListString = (String) this.doService(IAtomsConstants.SERVICE_ADM_USER_SERVICE, IAtomsConstants.ACTION_GET_NAME_LIST, null);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception checkAssetUserIsTaixinRent()"+ e , e);
			throw new ServiceException(e);
		}
		return mailgroupListString;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#isChangeAsset(java.lang.String, java.lang.StringBuilder)
	 */
	@Override
	public boolean isChangeAsset( String caseId) throws ServiceException {
		boolean result = false;
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
			result = (Boolean) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_IS_CHANGE_ASSET, param);
		} catch (Exception e){
			log.error(this.getClass().getName() + "Exception isChangeAsset()" + e, e);
			throw new ServiceException(e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getExportFlag()
	 */
	@Override
	public boolean getExportFlag(String ucNo) throws ServiceException {
		boolean flag = false;
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			SessionHelper.setAttribute(request, ucNo, request.getSession().getId(), IAtomsConstants.NO);
			int count = 0;
			while(count < 300){
				count ++;
				// 成功退出
				if(IAtomsConstants.YES.equals(SessionHelper.getAttribute(request, ucNo, request.getSession().getId()))){
					flag = true;
					break;
				} else if(IAtomsConstants.NO.equals(SessionHelper.getAttribute(request, ucNo, request.getSession().getId()))){
					
				} else {
					flag = false;
					break;
				}
				// 等待1s
				Thread.sleep(1000);
			}
			SessionHelper.removeAttribute(request, ucNo, request.getSession().getId());
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getUserListByCompany(java.lang.String)
	 */
	@Override
	public List<Parameter> getUserListByCompany(String companyId) throws ServiceException {
		List<Parameter> agents = null;
		try {
			MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
			parameterInquiryContext.addParameter(AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
			agents = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_ADM_USER_SERVICE, IAtomsConstants.ACTION_GET_USERLIST_BY_COMPANY, parameterInquiryContext);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getUserListByCompany()"+ e , e);
			throw new ServiceException(e);
		}
		return agents;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getHaveEdcContract(java.lang.String)
	 */
	@Override
	public String getHaveEdcContract(String companyId) throws ServiceException {
		String result = null;
		try {
			MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
			parameterInquiryContext.addParameter(BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
			result = (String) this.doService(IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_HAVE_EDC_CONTRACT, parameterInquiryContext);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getHaveEdcContract()"+ e , e);
			throw new ServiceException(e);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getUserColumnTemplate(java.lang.String)
	 */
	@Override
	public Map getUserColumnTemplate(String templateId) throws ServiceException {
		Map result = null;
		try {
			MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
			parameterInquiryContext.addParameter(SrmQueryTemplateDTO.ATTRIBUTE.TEMPLATE_ID.getValue(), templateId);
			result = (HashMap) this.doService(IAtomsConstants.SERVICE_SRM_QUERY_TEMPLATE_SERVICE, IAtomsConstants.ACTION_GET_USER_COLUMN_TEMPLATE, parameterInquiryContext);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getUserColumnTemplate()"+ e , e);
			throw new ServiceException(e);
		}
		return result;
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getSerialNumber(java.lang.String, java.lang.String)
	 */
	@Override
	public String checkSerialNumber(String assetTransId, String serialNumber)
			throws ServiceException {
		String result = null;
		try {
			MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
			parameterInquiryContext.addParameter(DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue(), assetTransId);
			parameterInquiryContext.addParameter(DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumber);
			result = (String) this.doService(IAtomsConstants.SERVICE_DMM_ASSET_TRANS_INFO_SERVICE, IAtomsConstants.ACTION_CHECK_SERIAL_NUMBER, parameterInquiryContext);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getSerialNumber()"+ e , e);
			throw new ServiceException(e);
		}
		return result;
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#checkComplaintFile(java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean checkComplaintFile(String fileId)
			throws ServiceException {
		// TODO Auto-generated method stub
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(SrmComplaintCaseFileDTO.ATTRIBUTE.FILE_ID.getValue(), fileId);
			String filePath = (String) this.doService(IAtomsConstants.SERVICE_COMPLAINT_SERVICE, IAtomsConstants.ACTION_GET_COMPLAINT_FILE_PATH, param);
			if (StringUtils.hasText(filePath)) {
				File file=new File(filePath);
				if(file.exists()){
					return true;
				}
			}
		} catch(Exception e) {
			log.error(this.getClass().getName() + "Exception checkComplaintFile()" + e, e);
			throw new ServiceException(e);
		}
		return false;
	}
	
		/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getBatchNum(java.lang.String, java.lang.String)
	 * 取得TMS自動化 批次流水號
	 */
	@Override
	public String createBatchNum()
			throws ServiceException {
		String num = "";
		try {			
			num = (String) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_CREATE_BATCH_NUM, null);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getBatchNum()"+ e , e);
			throw new ServiceException(e);
		}
		return num;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#TMSParaContents(java.lang.String)
	 * 案件處理頁面多筆勾選，並以案件標號多筆POST至TMS API
	 */
	public Map TMSParaContents(String caseIds, String ucNo, HttpServletRequest request) throws ServiceException {
		Map map = null;
		Boolean flag = false;
		try {		
			String batchNum = createBatchNum();
			if(batchNum.equals("")){
				return map;
			}			
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			IAtomsLogonUser logonUser = (IAtomsLogonUser)SessionHelper.getLogonUser(request);
			param.addParameter("caseIds", caseIds);	
			param.addParameter("ucNo", ucNo);	
			param.addParameter("batchNum", batchNum);	
			param.addParameter("remoteAddr", request.getRemoteAddr());	
			param.addParameter("createdByName", logonUser.getName());
			param.addParameter("createdById", logonUser.getId());	
			param.addParameter("account", logonUser.getAdmUserDTO().getAccount());	
			map = (HashMap) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.TMS_PARA_CONTENTS, param);
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception TMSPost()"+ e , e);
			throw new ServiceException(e);
		}
		return map;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getPostCodeList(java.lang.String)
	 */
	@Override
	public List<Parameter> getPostCodeList(String location) throws ServiceException {
		List<Parameter> postCodeList = null;
		try {
			if (StringUtils.hasText(location)) {
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue(), location);
				postCodeList = (List<Parameter>) this.doService(IAtomsConstants.SERVICE_MERCHANT_HEADER_SERVICE, "getPostCodeList", param);
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getPostCodeList()"+ e , e);
			throw new ServiceException(e);
		}
		return postCodeList;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getInstallCaseId(java.lang.String)
	 */
	@Override
	public String getInstallCaseId(String dtid)throws ServiceException {
		String installCaseId = null;
		log.debug(".getInstallCaseId() paramter dtid=" + dtid);
		try {
			if (StringUtils.hasText(dtid)) {
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), dtid);
				installCaseId = (String) this.doService(IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "getInstallCaseId", param);
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getInstallCaseId()"+ e , e);
			throw new ServiceException(e);
		}
		return installCaseId;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#checkLogFileExist(java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean checkLogFileExist(String fileType, String fileName)
			throws ServiceException {
		// TODO Auto-generated method stub
		log.debug(".checkLogFileExist() --> fileType: " + fileType);
		log.debug(".checkLogFileExist() --> fileName: " + fileName);
		try {
			String filePath = null;
			
			if (fileType.equals("ap")) {
				//取得log4j.properties 裡logfile 的檔案路徑
				Enumeration e = Logger.getRootLogger().getAllAppenders();
			    while (e.hasMoreElements()) {
			    	Appender app = (Appender)e.nextElement();
			    	if (app instanceof FileAppender) {
			    		filePath = "C:" + ((FileAppender)app).getFile();
			    	}
			    }
	    		filePath = filePath.replace('/', '\\');
	    		filePath = filePath.substring(0, filePath.lastIndexOf("\\") + 1) + fileName;
			} else if (fileType.equals("tomcat")) {
				//取得Tomcat logfile 的檔案路徑
				filePath = System.getProperty("catalina.home") + File.separator + "logs" + File.separator + fileName;
			}
			
			if (StringUtils.hasText(filePath)) {
				File file = new File(filePath);
				if(file.exists()){
					return true;
				}
			}
		} catch(Exception e) {
			log.error(this.getClass().getName() + "Exception checkLogFileExist()" + e, e);
			throw new ServiceException(e);
		}
		return false;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#getBorrowAssetItemByIds(java.lang.String)
	 */
	@Override
	public List<DmmAssetBorrowInfoDTO> getBorrowAssetItemByIds(String borrowId) throws ServiceException {
		List<DmmAssetBorrowInfoDTO> assetBorrowInfoDTOs = null;
		try {
			if (StringUtils.hasText(borrowId)) {
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_CASE_ID.getValue(), borrowId);
				assetBorrowInfoDTOs = (List<DmmAssetBorrowInfoDTO>) this.doService(IAtomsConstants.SERVICE_ASSET_BORROW_SERVICE, "getBorrowAssetItemByIds", param);
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception getBorrowAssetItemByIds()"+ e , e);
			throw new ServiceException(e);
		}
		return assetBorrowInfoDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#checkBorrowSerialNumber(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkBorrowSerialNumber(String assetTypeId, String serialNumber) throws ServiceException {
		try {
			if (StringUtils.hasText(assetTypeId) && StringUtils.hasText(serialNumber)) {
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), assetTypeId);
				param.addParameter(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumber);
				return (Boolean) this.doService(IAtomsConstants.SERVICE_REPOSITORY_SERVICE, "checkBorrowSerialNumber", param);
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception checkBorrowSerialNumber()"+ e , e);
			throw new ServiceException(e);
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService#checkAssetIsBorrow(java.lang.String)
	 */
	@Override
	public String checkAssetIsBorrow(String serialNumber) throws ServiceException {
		try {
			if (StringUtils.hasText(serialNumber)) {
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(DmmAssetBorrowListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumber);
				String msg = (String) this.doService(IAtomsConstants.SERVICE_ASSET_BORROW_SERVICE, "checkAssetIsBorrow", param);
				if (msg != null) {
					return msg;
				}
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + "Exception checkBorrowSerialNumber()"+ e , e);
			throw new ServiceException(e);
		}
		return null;
	}
}
