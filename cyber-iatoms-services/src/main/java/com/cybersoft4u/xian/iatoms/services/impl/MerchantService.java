package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.config.SystemConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.parameter.IBaseParameterItemDefDAO;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantFormDTO;
import com.cybersoft4u.xian.iatoms.services.IMerchantService;
import com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterPostCodeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMerchant;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMerchantHeader;
/**
 * Purpose: 客戶特店維護Service
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/6/14
 * @MaintenancePersonnel DavidZheng
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MerchantService extends AtomicService implements IMerchantService {
	
	/**
	 * 日誌記錄
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, MerchantService.class);	
	
	/**
	 * 客戶特店維護DAO
	 */
	private IMerchantDAO merchantNewDAO;
	/**
	 * 特店表頭DAO
	 */
	private IMerchantHeaderDAO merchantHeaderDAO;
	/**
	 * 客户DAO
	 */
	private ICompanyDAO companyDAO;
	
	/**
	 * 基本參數DAO
	 */
	private IBaseParameterItemDefDAO bpidDAO;
	/**
	 * 郵遞區號DAO
	 */
	private IBaseParameterPostCodeDAO baseParameterPostCodeDAO;
	/**
	 * 上限數500
	 */
	private Integer limitNumber;
	/**
	 * 
	 */
	private Map<String, Object> attributes;
	
	/**
	 * 無參的構造方法 
	 */
	public MerchantService() {
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		try {
			MerchantFormDTO bimMerchantFormDTO = null;
			bimMerchantFormDTO = (MerchantFormDTO) sessionContext.getRequestParameter();
			String size = SystemConfigManager.getProperty(IAtomsConstants.SETTING, IAtomsConstants.UPLOAD_FILE_SIZE);
			if (!StringUtils.hasText(size)) {
				size = IAtomsConstants.PARAM_UPLOAD_FILE_SIZE;
			}
			int rate = Integer.valueOf(IAtomsConstants.PARAM_UPLOAD_CONVERSION_RATE);
			size = String.valueOf(Integer.valueOf(size) * rate);
			bimMerchantFormDTO.setUploadFileSize(size);
			//Task #3583 當 【客戶廠商角色】 時 不設倉庫控管，可見本公司下所有設備
			setUserRoleAttribute(bimMerchantFormDTO);
			
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(bimMerchantFormDTO);
			return sessionContext;
		} catch (Exception e) {
			LOGGER.error(".init(SessionContext sessionContext):" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
	}
	/**
	 * 判斷用戶角色屬性
	 */
	private void setUserRoleAttribute(MerchantFormDTO formDTO) {
		IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
		if (logonUser != null) {
			//當前登入者角色屬性
			String userAttribute = null;
			// 是客戶角色
			Boolean isCustomerAttribute = false;
			// 是廠商角色
			Boolean isVendorAttribute = false;
			// Task #3583  是客戶廠商角色
			Boolean isCustomerVendorAttribute = false;
			//得到用戶角色列表
			List<AdmRoleDTO> userRoleList = logonUser.getUserFunctions();
			//遍歷所有的角色
			for (AdmRoleDTO admRoleDTO : userRoleList) {
				userAttribute = admRoleDTO.getAttribute();
				// 是廠商角色
				if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(userAttribute)) {
					// 如果即是客戶角色又是廠商角色則設置為廠商角色
					isVendorAttribute = true;
					//Task #3583  客戶廠商      如果即是客戶廠商角色又是廠商角色則設置為廠商角色
				} else if (IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(userAttribute)) {
					isCustomerVendorAttribute = true;
					// 是客戶角色
				} else if(IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE.equals(userAttribute)){
					isCustomerAttribute = true;
				}
			}
			if (isVendorAttribute) {
				//如果即是客戶角色又是廠商角色則設置為廠商角色
				formDTO.setRoleAttribute(IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
			} else if (isCustomerVendorAttribute) {
				formDTO.setRoleAttribute(IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE);
			} else if (isCustomerAttribute) {
				formDTO.setRoleAttribute(IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE);
			}
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantService#query(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			MerchantFormDTO merchantNewFormDTO = (MerchantFormDTO) sessionContext.getRequestParameter();
			//查詢符合條件的特護特店
			List<MerchantDTO> merchantNewDTOs = this.merchantNewDAO.listBy(
					merchantNewFormDTO.getQueryCompanyId(),
					merchantNewFormDTO.getQueryName(),
					merchantNewFormDTO.getQueryMerchantCode(),
					//merchantNewFormDTO.getQueryStagesMerchantCode(),
					merchantNewFormDTO.getRows(),
					merchantNewFormDTO.getPage(),
					merchantNewFormDTO.getSort(),
					merchantNewFormDTO.getOrder());
			// 如果沒有查到資料就提示查詢失敗,如果查到資料就把資料都放到FormDTO中
			if (!CollectionUtils.isEmpty(merchantNewDTOs)) {
				//把資料放在formDTO中
				merchantNewFormDTO.setList(merchantNewDTOs);
				int count = (this.merchantNewDAO.count(merchantNewFormDTO.getQueryCompanyId(), merchantNewFormDTO.getQueryName(), merchantNewFormDTO.getQueryMerchantCode())).intValue();
				merchantNewFormDTO.getPageNavigation().setRowCount(count);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			} else {
				//沒有查到資料就返回查無資料信息
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(merchantNewFormDTO);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".query() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".query() Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}


	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantService#initEdit(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException {
		try {
			MerchantFormDTO merchantNewFormDTO = (MerchantFormDTO) sessionContext.getRequestParameter();
			String merchantId = merchantNewFormDTO.getMerchantId();
			//String customerId = merchantNewFormDTO.getQueryCompanyId();
			//String merchantCode = merchantNewFormDTO.getQueryMerchantCode();
			//修改初始化，如果客戶特店id不等於空就去查到一筆資料
			if (StringUtils.hasText(merchantId)) {
				MerchantDTO bimMerchantDTO = this.merchantNewDAO.getMerchantInfo(merchantId, null, null, null);
				merchantNewFormDTO.setBimMerchantDTO(bimMerchantDTO);
				sessionContext.setResponseResult(merchantNewFormDTO);
			}
		} catch (DataAccessException e) {
			LOGGER.error(".initEdit() DataAccess Exception:" + e, e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".initEdit():", "service Exception", e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantService#initAdd(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initAdd(SessionContext sessionContext) throws ServiceException {
		try {
			MerchantFormDTO merchantNewFormDTO = (MerchantFormDTO) sessionContext.getRequestParameter();
			//Task #3583 當 【客戶廠商角色】 時 不設倉庫控管，可見本公司下所有設備
			setUserRoleAttribute(merchantNewFormDTO);

		} catch (DataAccessException e) {
			LOGGER.error(".initAdd() DataAccess Exception:" + e, e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".initAdd():", "service Exception", e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantService#save(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		Map map = new HashMap();
		LogonUser logonUser = null;
		try {
			//獲取對象
			MerchantFormDTO merchantNewFormDTO = (MerchantFormDTO) sessionContext.getRequestParameter();
			MerchantDTO bimMerchantDTO = merchantNewFormDTO.getBimMerchantDTO();
			//獲取主鍵
			String merchantId = bimMerchantDTO.getMerchantId();
			//獲取MId
			String merchantCode = bimMerchantDTO.getMerchantCode();
			String companyId = bimMerchantDTO.getCompanyId();
			String name = null;
			//獲取name
			//String stagesMerchantCode = bimMerchantDTO.getStagesMerchantCode();
			logonUser = merchantNewFormDTO.getLogonUser();
			if (logonUser != null) {
				if(StringUtils.hasText(merchantCode)){
					boolean isRepeat = this.merchantNewDAO.isCheck(merchantCode, merchantId, companyId);
					if (!isRepeat) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.MERCHANT_CODE_REPEAT);
						sessionContext.setReturnMessage(msg);
						return sessionContext;
					}
				} 
				//分期特店代號是否有值，是否重複
				/*if (StringUtils.hasText(stagesMerchantCode)) {
					boolean isRepeat = merchantNewDAO.check(null, stagesMerchantCode, merchantId,companyId);
					if (!isRepeat) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.STAGES_MERCHANT_CODE_REPEAT);
						sessionContext.setReturnMessage(msg);
						return sessionContext;
					}
				} */
				if(StringUtils.hasText(merchantId)){
					BimMerchant bimMerchant = this.merchantNewDAO.findByPrimaryKey(BimMerchant.class, merchantId);
					if(bimMerchant != null){
						name = bimMerchantDTO.getName();
						bimMerchant.setCompanyId(bimMerchantDTO.getCompanyId());
						bimMerchant.setMerchantCode(bimMerchantDTO.getMerchantCode());
						//Task #3249
						bimMerchant.setUnityNumber(bimMerchantDTO.getUnityNumber());
						bimMerchant.setName(bimMerchantDTO.getName());
						bimMerchant.setRemark(bimMerchantDTO.getRemark());
						bimMerchant.setUpdatedById(logonUser.getId());
						bimMerchant.setUpdatedByName(logonUser.getName());
						bimMerchant.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()});
						this.merchantNewDAO.getDaoSupport().saveOrUpdate(bimMerchant);
					}else{
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()});
					}
				}else {
					Transformer transformer = new SimpleDtoDmoTransformer();
					BimMerchant bimMerchant = null;
					//把DTO對象轉成DMO
					bimMerchant = (BimMerchant) transformer.transform(bimMerchantDTO, new BimMerchant());
					name = bimMerchantDTO.getName();
					merchantId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_MERCHANT);
					bimMerchant.setMerchantId(merchantId);
					bimMerchant.setDeleted(IAtomsConstants.NO);
					bimMerchant.setCreatedById(logonUser.getId());
					bimMerchant.setCreatedByName(logonUser.getName());
					bimMerchant.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					bimMerchant.setUpdatedById(logonUser.getId());
					bimMerchant.setUpdatedByName(logonUser.getName());
					bimMerchant.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
					this.merchantNewDAO.getDaoSupport().saveOrUpdate(bimMerchant);
				}
			}else{
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT);				
			}
			this.merchantNewDAO.getDaoSupport().flush();
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("merchantId", merchantId);
			resultMap.put("merchantCode", merchantCode);
			resultMap.put("name", name);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROW, resultMap);
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(true));
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(merchantNewFormDTO);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".save() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[] {this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".save():", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[] {this.getMyName()}, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantService#delete(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			MerchantFormDTO merchantNewFormDTO = (MerchantFormDTO) sessionContext.getRequestParameter();
			//刪除先獲得客戶特店的Id
			String merchantId = merchantNewFormDTO.getMerchantId();
			if(StringUtils.hasText(merchantId)) {
				//去驗證這筆資料是否存在
				BimMerchant bimMerchant = this.merchantNewDAO.findByPrimaryKey(BimMerchant.class, merchantId);
				if(bimMerchant != null) {
					//在執行刪除操作
					bimMerchant.setDeleted(IAtomsConstants.YES);
					this.merchantNewDAO.getDaoSupport().saveOrUpdate(bimMerchant);
					this.merchantNewDAO.getDaoSupport().flush();
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[] { this.getMyName() });
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".delete() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".delete():", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantService#getMerchantList(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public Boolean getMerchantList(MultiParameterInquiryContext param)
			throws ServiceException {
		Boolean flag = false;
		try {
			String merchantId = (String) param.getParameter(MerchantDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			List<BimMerchantHeaderDTO> merchantHeaderDTOs = this.merchantHeaderDAO.listby(null, null, null, null, null, null, null, 0, 0, true, null, null, null, merchantId);
			if (!CollectionUtils.isEmpty(merchantHeaderDTOs)) {
				flag = true;
			}
			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(".getMerchantList() is error : " + e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		} catch (Exception e){
			LOGGER.error(".getMerchantList() is error : " + e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantService#upload(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext upload(SessionContext sessionContext)
			throws ServiceException {
		Message msg = null;
		BimMerchant bimMerchant = null;
		BimMerchantHeader bimMerchantHeader = null;
		Transformer transformer = new SimpleDtoDmoTransformer();
		LogonUser logonUser = null;
		try {
			MerchantFormDTO merchantNewFormDTO = (MerchantFormDTO) sessionContext.getRequestParameter();
			logonUser = merchantNewFormDTO.getLogonUser();
			//獲得上傳文件
			MultipartFile uploadFiled = merchantNewFormDTO.getUploadFiled();
			if(uploadFiled != null){
				Map<String, List<String>> errorMap = new LinkedHashMap<String, List<String>>();
				//拿到解析之後的數據
				String[] str = new String[1];
				//Task #3583 角色表單屬性
				setUserRoleAttribute(merchantNewFormDTO);
				List<MerchantDTO> TempMerchantDTOs = this.checkUploadFiled(uploadFiled, errorMap,str,merchantNewFormDTO);
				if(str[0] == null){
					if(!CollectionUtils.isEmpty(errorMap)){
						//寫入錯誤信息
						Map<String, String> map = this.saveErrorMsg(errorMap);
						
						merchantNewFormDTO.setErrorFileName(map.get(MerchantFormDTO.ERROR_FILE_NAME));
						merchantNewFormDTO.setTempFilePath(map.get(MerchantFormDTO.TEMP_FILE_PATH));
						msg = new Message(Message.STATUS.FAILURE);
					} else {
						String id = null;
						String merchantId = null;
						for(int i = 0;i<(TempMerchantDTOs.size());i++){
							MerchantDTO tempMerchantDTO =  TempMerchantDTOs.get(i);
							//匯入的資料 特店已經在db存在，只儲存表頭信息
							if(!IAtomsConstants.YES.equals(tempMerchantDTO.getExportRepeatMerchant())) {
								bimMerchant = (BimMerchant) transformer.transform(tempMerchantDTO, new BimMerchant());
								merchantId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_MERCHANT);
								bimMerchant.setMerchantId(merchantId);
								bimMerchant.setUpdatedById(logonUser.getId());
								bimMerchant.setUpdatedByName(logonUser.getName());
								bimMerchant.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
								this.merchantNewDAO.getDaoSupport().saveOrUpdate(bimMerchant);
								this.merchantNewDAO.getDaoSupport().flush();
							} else {
								merchantId = tempMerchantDTO.getMerchantId();
							}
							for(int j=0;j< tempMerchantDTO.getBimMerchantHeaderDTOs().size();j++){
								BimMerchantHeaderDTO bimmHeaderDTO = tempMerchantDTO.getBimMerchantHeaderDTOs().get(j);
								bimMerchantHeader = (BimMerchantHeader) transformer.transform(bimmHeaderDTO, new BimMerchantHeader());
								id = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_MERCHANT_HEADER);
								bimMerchantHeader.setMerchantHeaderId(id);
								bimMerchantHeader.setMerchantId(merchantId);
								this.merchantHeaderDAO.getDaoSupport().saveOrUpdate(bimMerchantHeader);
								this.merchantHeaderDAO.getDaoSupport().flush();
								msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPLOAD_SECCUSS_TION,new String[]{""});
							}
						}
					}
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ABOUCHEMENT_FAILURE_FOR_OVER_LIMIT, new String[]{"" + limitNumber});
				}
				sessionContext.setResponseResult(merchantNewFormDTO);
				sessionContext.setReturnMessage(msg);
				Map map = new HashMap();
				// 存放生成错误文件的文件名
				map.put(merchantNewFormDTO.ERROR_FILE_NAME, merchantNewFormDTO.getErrorFileName());
				map.put(merchantNewFormDTO.TEMP_FILE_PATH, merchantNewFormDTO.getTempFilePath());
				if (Message.STATUS.SUCCESS.equals(msg.getStatus())) {
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				} else {
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				}
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			}
		} catch (DataAccessException e) {
			LOGGER.error(".upload() DataAccess Exception : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".upload() Exception : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	/**
	 * Purpose::檢查上傳文件
	 * @author HermanWang
	 * @param uploadFiled：上傳文件
	 * @param errorMap：錯誤訊息
	 * @return List<MerchantDTO>：返回一個MerchantDTO對象
	 */
	private List<MerchantDTO> checkUploadFiled(MultipartFile uploadFiled, Map<String, List<String>> errorMap, String[] str, MerchantFormDTO formDTO) {
		MerchantDTO bimMerchantDTO = new MerchantDTO();
		bimMerchantDTO.setBimMerchantHeaderDTOs(new ArrayList<BimMerchantHeaderDTO>());
		List<MerchantDTO> bimMerchantDTOs = new ArrayList<MerchantDTO>();
		List<MerchantDTO> TempMerchantDTOs = new ArrayList<MerchantDTO>();
		BimMerchantHeaderDTO bimMerchantHeaderDTO =new BimMerchantHeaderDTO();
		List<BimMerchantHeaderDTO> bimMerchantHeaderDTOs = new ArrayList<BimMerchantHeaderDTO>();
		boolean isRepeat = false;
		try {
			if(uploadFiled != null){
				// 错误消息List
				List<String> errorList = new ArrayList<String>() ;
				// 获取上传文件输入流
				InputStream inputStream = uploadFiled.getInputStream();
				Workbook workbook = null;
				Sheet sheet = null;
				Row row = null;
				int rowCount = 0;
				String fileName = uploadFiled.getOriginalFilename();
				String fileTxt = fileName.substring(fileName.lastIndexOf(IAtomsConstants.MARK_NO)+1);
				if (IAtomsConstants.FILE_TXT_MSEXCEL.equals(fileTxt)) {
					//2003版本
					workbook = new HSSFWorkbook(inputStream);
				} else if (IAtomsConstants.FILE_TXT_MSEXCEL_X.equals(fileTxt)) {
					//2007版本
					workbook = new XSSFWorkbook(inputStream);
				}
				if (workbook != null) {
					sheet = workbook.getSheetAt(0);
				} else {
					errorList = new ArrayList<String>();
					String errorMsg = i18NUtil.getName(IAtomsMessageCode.FILE_FORMAT_ERROR);
					errorList.add(errorMsg);
					errorMap.put(String.valueOf(0), errorList);
					LOGGER.error("workbook is null >>>");
					throw new ServiceException();
				}
				//特店屬性
				String companyName = null;
				String merchantId = null;
				String companyId = null;
				String merchantCode = null;
				String name = null;
				String remark = null;
				//特店表頭屬性
				String isVip = null;
				String headerName = null;
				String area = null;
				//特店聯絡人
				String contact = null;
				String contactTel = null;
				String contactTel2 = null;
				String phone = null;
				String location = null;
				//Task #3481 郵遞區域
				String postName = null;
				String postCodeId = null;
				String businessAddress = null;
				String openHour = null;
				String closeHour = null;
				String aoName = null;
				String aoEmail = null;
				String contactEmail = null;
				// 錯誤消息
				String errorMsg = null;
				String companyInfo = IAtomsConstants.COMPANY_INFO;
				String baseInfo = IAtomsConstants.BASE_INFO;
				String logonUserCompanyId = IAtomsConstants.MARK_EMPTY_STRING;
				List<String> companyTypeList = new ArrayList<String>();
				companyTypeList.add(IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
				//拿到公司列表
				List<Parameter> companys = this.companyDAO.getCompanyList(companyTypeList,IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE,false, null);
				// 縣市列表
				List<Parameter> contys = (List<Parameter>)this.bpidDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.LOCATION.getCode(), null);
				// 是否列表
				List<Parameter> yesOrNoList = (List<Parameter>)this.attributes.get(IATOMS_PARAM_TYPE.YES_OR_NO.getCode());
				List<Parameter> areaList = (List<Parameter>) this.bpidDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.REGION.getCode(), null);
				// 獲取行數
				rowCount = this.getExcelRealRowCount(sheet);
				//Task #3249 統一編號
				String unityNumber = null;
				int i;
				if (rowCount <= 1) {
					errorList = new ArrayList<String>();
					errorList.add( i18NUtil.getName(IAtomsMessageCode.IATOMS_MSG_NONE_DATA));
					errorMap.put(String.valueOf(0), errorList);
				}  else if( rowCount > (limitNumber+1) ){
					str[0] = IAtomsMessageCode.ABOUCHEMENT_FAILURE_FOR_OVER_LIMIT;
				}else { 
					IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
					//Task #3583  
					if (logonUser != null) {
						logonUserCompanyId = logonUser.getCompanyId();
						for (i = 1; i < rowCount; i++) {
							//errorList = new ArrayList<String>();
							//獲取行
							row = sheet.getRow(i);
							if (row == null) {
								continue;
							} 
							//設置設備序號的單元格屬性
							for(int j = 0; j< 17; j++){
								if (row.getCell(j) != null) {
									row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
								}
							}
							//獲取數據
							companyName = this.getCellFormatValue(row.getCell(0), false);
							merchantCode = this.getCellFormatValue(row.getCell(1), false);
							name = this.getCellFormatValue(row.getCell(2), false);
							//Task #3249 統一編號
							unityNumber = this.getCellFormatValue(row.getCell(3), false);
							remark = this.getCellFormatValue(row.getCell(4), false);
							isVip = this.getCellFormatValue(row.getCell(5), false);
							headerName = this.getCellFormatValue(row.getCell(6), false);
							area = this.getCellFormatValue(row.getCell(7), false);
							contact = this.getCellFormatValue(row.getCell(8), false);
							contactTel = this.getCellFormatValue(row.getCell(9), false);
							contactTel2 = this.getCellFormatValue(row.getCell(10), false);
							phone = this.getCellFormatValue(row.getCell(11), false);
							contactEmail = this.getCellFormatValue(row.getCell(12), false);
							location = this.getCellFormatValue(row.getCell(13), false);
							//Task #3481 郵遞區域
							postName = this.getCellFormatValue(row.getCell(14), false);
							businessAddress = this.getCellFormatValue(row.getCell(15), false);
							openHour = this.getCellFormatValue(row.getCell(16), true);
							closeHour = this.getCellFormatValue(row.getCell(17), true);
	 						aoName = this.getCellFormatValue(row.getCell(18), false);
							aoEmail = this.getCellFormatValue(row.getCell(19), false);
							//客戶
							this.check(companyName, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_CUSTOMER), false, 20, errorList);
							if(StringUtils.hasText(companyName)){
								companyId = this.getValueByName(companys, companyName, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_CUSTOMER), companyInfo, errorList);
								/*if (!StringUtils.hasText(companyId)) {
									errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_COLUMN_NAME, new String[]{Integer.toString(i+1),i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_COMPANY_ID) + i18NUtil.getName(IAtomsMessageCode.IMPORT_VALUE_NOT_FOUND)}, null));
								}*/
								//Task #3583  當 【客戶廠商角色】 時 只可匯入本公司資料
								if (IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(formDTO.getRoleAttribute())) {
									if ((StringUtils.hasText(companyId)) && (!companyId.equals(logonUserCompanyId))) {
										//當前登陸者角色不符，不可新增非本公司特店
										errorList.add(i18NUtil.getName(IAtomsMessageCode.CAN_NOT_NEW_OTHER_MID, new String[]{Integer.toString(i+1)}, null));
									}
								}
							}
							//特店代號
							this.check(merchantCode, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_MERCHANT_CODE),false, 20, errorList);
							//特店名稱
							this.check(name, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_NAME),false, 50, errorList);
							//表頭
							this.check(headerName, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_NAME), false, 100, errorList);
							this.check(isVip, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_VIP), false, 1, errorList);
							if(StringUtils.hasText(isVip)){
								isVip = this.checkValueByName(yesOrNoList, isVip, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_VIP), errorList);
							}
							this.check(area, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_AREA), false, 40, errorList);
							//區域
							if(StringUtils.hasText(area)){
								area = this.getValueByName(areaList, area, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_AREA), baseInfo, errorList);
							}
							//縣市
							if(StringUtils.hasText(location)){
								location = this.getValueByName(contys, location, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_LOCATION), baseInfo, errorList);
								//Task #3481 郵遞區域
								if(StringUtils.hasText(postName)){
									String[] tempPost = postName.split("\\(");
									if (tempPost.length != 2) {
										errorList.add(i18NUtil.getName(IAtomsMessageCode.PARAM_POST_CODE_ERROR, new String[]{Integer.toString(i+1)}, null));
									} else {
										//檢核郵遞區域是否屬於該縣市
										List<Parameter> postCodeList = this.baseParameterPostCodeDAO.getPostCodeList(location, tempPost[1].substring(0, tempPost[1].length() - 1), tempPost[0]);
										if(!CollectionUtils.isEmpty(postCodeList)){
											postCodeId = (String) postCodeList.get(0).getValue();
										}else{
											//郵遞區域錯誤，不屬於該縣市
											errorList.add(i18NUtil.getName(IAtomsMessageCode.PARAM_POST_CODE_ERROR, new String[]{Integer.toString(i+1)}, null));
										}
									}
									
								}
							}else if(StringUtils.hasText(postName)){
								//若存在郵遞區域，則縣市不可為空
								errorList.add(i18NUtil.getName(IAtomsMessageCode.PARAM_LOCATION_ERROR, new String[]{Integer.toString(i+1)}, null));
							}
							//存放在dto
							bimMerchantDTO = new MerchantDTO();
							bimMerchantHeaderDTO = new BimMerchantHeaderDTO();
							//把當前行的數據存入一個臨時DTO裡面
							bimMerchantDTO.setMerchantCode(merchantCode);
							//Task #3249 統一編號
							bimMerchantDTO.setUnityNumber(unityNumber);
							bimMerchantDTO.setName(name);
							bimMerchantDTO.setRemark(remark);
							bimMerchantDTO.setCompanyId(companyId);
							bimMerchantDTO.setDeleted(IAtomsConstants.NO);
							bimMerchantDTO.setHeaderName(headerName);
							bimMerchantHeaderDTO.setIsVip(isVip);
							bimMerchantHeaderDTO.setHeaderName(headerName);
							bimMerchantHeaderDTO.setArea(area);
							bimMerchantHeaderDTO.setContact(contact);
							bimMerchantHeaderDTO.setContactTel(contactTel);
							bimMerchantHeaderDTO.setContactTel2(contactTel2);
							bimMerchantHeaderDTO.setPhone(phone);
							bimMerchantHeaderDTO.setContactEmail(contactEmail);
							bimMerchantHeaderDTO.setLocation(location);
							bimMerchantHeaderDTO.setBusinessAddress(businessAddress);
							bimMerchantHeaderDTO.setOpenHour(openHour);
							bimMerchantHeaderDTO.setCloseHour(closeHour);
							bimMerchantHeaderDTO.setAoemail(aoEmail);
							bimMerchantHeaderDTO.setAoName(aoName);
							bimMerchantHeaderDTO.setPostCodeId(postCodeId);
							//把bimMerchantHeaderDTO放到bimmerchantDTO裡面
							bimMerchantHeaderDTOs.add(bimMerchantHeaderDTO);
							bimMerchantDTOs.add(bimMerchantDTO);
						}
						
						//開始自身判斷是否重複
						boolean isMerchantExists = false;
						if (CollectionUtils.isEmpty(errorList)) {
							for(i = 0;i<bimMerchantDTOs.size(); i++){
								isMerchantExists = false;
								bimMerchantDTO = bimMerchantDTOs.get(i);
								bimMerchantHeaderDTO = bimMerchantHeaderDTOs.get(i);
								isRepeat = false;
								//遍歷臨時的dto
								for (MerchantDTO tempMerchantDTO : TempMerchantDTOs) {
									if(tempMerchantDTO.getCompanyId().equals(bimMerchantDTO.getCompanyId())){
										if (tempMerchantDTO.getMerchantCode().equals(bimMerchantDTO.getMerchantCode())) {
											isMerchantExists = true;
											//判斷相同特店其他信息是否輸入一致
											if(!(tempMerchantDTO.getName().equals(bimMerchantDTO.getName()) 
													&& tempMerchantDTO.getRemark().equals(bimMerchantDTO.getRemark()) ) ) {
												errorMsg = i18NUtil.getName(IAtomsMessageCode.MERCHANT_OTHER_INFO_NOT_CONSISTENT);
												errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i+2))}, null) + errorMsg);
												break;
											} else {
												isRepeat = false;
												//同一特店下的表頭
												for (BimMerchantHeaderDTO tempBimMerchantHeaderDTO : tempMerchantDTO.getBimMerchantHeaderDTOs()) {
													if (tempBimMerchantHeaderDTO.getHeaderName().equals(bimMerchantHeaderDTO.getHeaderName())) {
														isRepeat = true;
														break;
													}
												}
												if (isRepeat) {
													errorMsg = i18NUtil.getName(IAtomsMessageCode.MERCHANT_HEADER_NAME_NOT_REPEAT);
													errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i+2))}, null) + errorMsg);
													break;
												} else {
													tempMerchantDTO.getBimMerchantHeaderDTOs().add(bimMerchantHeaderDTO);
													break;
												}
											}
										}
									} 
								}
								if(isRepeat == true){
									continue;
								}
								//如果不存在，一直往bimMerchantDTO裡面
								if (!isMerchantExists) {
									if (bimMerchantDTO.getBimMerchantHeaderDTOs() == null) {
										bimMerchantDTO.setBimMerchantHeaderDTOs(new ArrayList<BimMerchantHeaderDTO>());
									}
									bimMerchantDTO.getBimMerchantHeaderDTOs().add(bimMerchantHeaderDTO);
									TempMerchantDTOs.add(bimMerchantDTO);
								}
							}
						}
						//自身檢核完成，開始檢核數據庫重複
						if (CollectionUtils.isEmpty(errorList)) {
							for(i = 0;i<TempMerchantDTOs.size(); i++){
								MerchantDTO merchantDTO = TempMerchantDTOs.get(i);
								//檢查特店代號是否重複
								if(StringUtils.hasText(merchantDTO.getCompanyId())){
									if (StringUtils.hasText(merchantDTO.getMerchantCode())) {
										isRepeat = this.merchantNewDAO.isCheck(merchantDTO.getMerchantCode(), merchantId,merchantDTO.getCompanyId());
										//特店代號有重複，需要查出來特店的id
										if (!isRepeat) {
											//update by 2017/07/31 
											//修改原因：匯出時候，若特店代號數據庫已經存在，只匯入表頭信息.
											//errorMsg = i18NUtil.getName(IAtomsMessageCode.MERCHANT_CODE_REPEAT);
											//errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i+1)), null}, null) + errorMsg);
											merchantDTO.setExportRepeatMerchant(IAtomsConstants.YES);
										}
									}
								}
							}
							for(i = 0;i<bimMerchantDTOs.size(); i++){
								MerchantDTO merchantDTO = bimMerchantDTOs.get(i);
								//檢查特店代號是否重複
								if(StringUtils.hasText(merchantDTO.getCompanyId())){
									if (StringUtils.hasText(merchantDTO.getMerchantCode())) {
										isRepeat = this.merchantNewDAO.isCheck(merchantDTO.getMerchantCode(), merchantId,merchantDTO.getCompanyId());
										//特店代號有重複，需要查出來特店的id
										if (!isRepeat) {
											List<MerchantDTO> merchantDTOList = this.merchantNewDAO.listBy(merchantDTO.getCompanyId(), null, merchantDTO.getMerchantCode(), -1, -1, null, null);
											if(!CollectionUtils.isEmpty(merchantDTOList)) {
												String repeatMerchantId = merchantDTOList.get(0).getMerchantId();
												merchantDTO.setMerchantId(repeatMerchantId);
												boolean headerIsRepeat = this.merchantHeaderDAO.check(repeatMerchantId, merchantDTO.getHeaderName(), null);
												if(headerIsRepeat) {
													errorMsg = i18NUtil.getName(IAtomsMessageCode.MERCHANT_HEAD_NAME_NOT_REPEAT);
													errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i+2)), null}, null) + errorMsg);
												}
											}
										}
									}
								}
							}
						}
						//
						boolean isError = true;
						//和數據庫查重檢核完畢，開始檢核各個欄位
						if (CollectionUtils.isEmpty(errorList)) {
							for (i = 1; i < rowCount; i++) {
								errorList = new ArrayList<String>();
								//獲取行
								row = sheet.getRow(i);
								if (row == null) {
									continue;
								} 
								//設置設備序號的單元格屬性
								for(int j = 0; j<17; j++){
									if (row.getCell(j) != null) {
										row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
									}
								}
								//獲取數據
								companyName = this.getCellFormatValue(row.getCell(0), false);
								merchantCode = this.getCellFormatValue(row.getCell(1), false);
								name = this.getCellFormatValue(row.getCell(2), false);
								//Task #3249 統一編號
								unityNumber = this.getCellFormatValue(row.getCell(3), false);
								remark = this.getCellFormatValue(row.getCell(4), false);
								isVip = this.getCellFormatValue(row.getCell(5), false);
								headerName = this.getCellFormatValue(row.getCell(6), false);
								area = this.getCellFormatValue(row.getCell(7), false);
								contact = this.getCellFormatValue(row.getCell(8), false);
								contactTel = this.getCellFormatValue(row.getCell(9), false);
								contactTel2 = this.getCellFormatValue(row.getCell(10), false);
								phone = this.getCellFormatValue(row.getCell(11), false);
								contactEmail = this.getCellFormatValue(row.getCell(12), false);
								location = this.getCellFormatValue(row.getCell(13), false);
								//Task #3481 郵遞區域
								postName = this.getCellFormatValue(row.getCell(14), false);
								businessAddress = this.getCellFormatValue(row.getCell(15), false);
								openHour = this.getCellFormatValue(row.getCell(16), true);
								closeHour = this.getCellFormatValue(row.getCell(17), true);
		 						aoName = this.getCellFormatValue(row.getCell(18), false);
								aoEmail = this.getCellFormatValue(row.getCell(19), false);
								//檢查數據
								this.check(companyName, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_CUSTOMER), false, 20, errorList);
								if(StringUtils.hasText(companyName)){
									companyId = this.getValueByName(companys, companyName, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_CUSTOMER), companyInfo,errorList);
									/*if (!StringUtils.hasText(companyId)) {
										errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_COLUMN_NAME, new String[]{Integer.toString(i+1),i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_COMPANY_ID) + i18NUtil.getName(IAtomsMessageCode.IMPORT_VALUE_NOT_FOUND)}, null));
									}*/
									//Task #3583  當 【客戶廠商角色】 時 只可匯入本公司資料
									if (IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(formDTO.getRoleAttribute())) {
										if ((StringUtils.hasText(companyId)) && (!companyId.equals(logonUserCompanyId))) {
											//當前登陸者角色不符，不可新增非本公司特店
											errorList.add(i18NUtil.getName(IAtomsMessageCode.CAN_NOT_NEW_OTHER_MID, new String[]{Integer.toString(i+1)}, null));
										}
									}
								}
								this.check(merchantCode,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_MERCHANT_CODE),false,20, errorList);
								//檢查merchantCode是否是英文或數字
								if(StringUtils.hasText(merchantCode)){
									this.checkMerchantCode(merchantCode,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_MERCHANT_CODE), errorList);
								}
								//this.check(stagesMerchantCode,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_STAGES_MERCHANT_CODE),true,20, errorList);
								//檢查stagesMerchantCode是否是英文或數字
								//if(StringUtils.hasText(stagesMerchantCode)){
								//	this.checkMerchantCode(stagesMerchantCode,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_STAGES_MERCHANT_CODE), errorList);
								//}
								this.check(name,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_NAME),false,50,errorList);
								
								//Task #3249  檢驗 統一編號 的長度 -- 10
								this.check(unityNumber,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_UNITY_NUMBER),true,10, errorList);
								//檢查 統一編號 是否 英數字
								if (StringUtils.hasText(unityNumber)) {
									this.checkMerchantCode(unityNumber,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_UNITY_NUMBER), errorList);
								}
								//檢驗備註的長度 -- 200
								this.check(remark,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_REMARK),true,200, errorList);
								this.check(isVip,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_VIP),false,1, errorList);
								if(StringUtils.hasText(isVip)){
									isVip = this.checkValueByName(yesOrNoList, isVip, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_VIP), errorList);
								}
								this.check(headerName,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_NAME),false,100, errorList);
								this.check(area,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_AREA),false,40, errorList);
								//區域
								if(StringUtils.hasText(area)){
									area = this.getValueByName(areaList, area, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_AREA), baseInfo,errorList);
								}
								this.check(contact,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CONTACT),true, 50, errorList);
								this.check(contactTel,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CONTACT_TEL),true, 20, errorList);
								this.check(contactTel2,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CONTACT_TEL2),true, 20, errorList);
								this.check(phone,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_PHONE),true, 10, errorList);
								//檢查contactEmail -- 長度
								this.check(contactEmail, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CONTACT_EMAIL),true, 50,  errorList);
								//檢查contactEmail-- 格式
								this.checkEmail(contactEmail, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CONTACT_EMAIL), errorList);
								if(StringUtils.hasText(phone)){
									//如果符合標準，判斷是否電話格式
									this.checkPhone(phone, i+1, errorList);
								}
								this.check(location,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_LOCATION),true, 40, errorList);
								//縣市
								if(StringUtils.hasText(location)){
									location = this.getValueByName(contys, location, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_LOCATION), baseInfo, errorList);
								}
								this.check(businessAddress,i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_ADDRESS),true,100, errorList);
								//businessAddress = this.getValueByName(contys, businessAddress, i, IAtomsConstants.COLUMN_N, baseInfo,errorList);
								//判斷開始時間是否符合標準或未空
								this.check(openHour,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_OPENHOUR),true, 5, errorList);
								if(StringUtils.hasText(openHour)){
									//如果符合標準，判斷是否時間格式
									this.checkTimeFormat(openHour, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_OPENHOUR), errorList);
								}
								//如果不是時間格式 break
								this.check(closeHour,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CLOSEHOUR),true, 5, errorList);
								if(StringUtils.hasText(closeHour)){
									this.checkTimeFormat(closeHour, i+1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CLOSEHOUR), errorList);
								}
								//起止日期對比
								this.checkTime(openHour, closeHour,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_OPENHOUR),i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CLOSEHOUR), errorList);
								this.check(aoName,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_AO_NAME),true, 50, errorList);
								//檢查email -- 長度
								this.check(aoEmail,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_AO_EMAIL),true, 50,  errorList);
								//檢查email -- 格式
								this.checkEmail(aoEmail,i+1,i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_AO_EMAIL), errorList);
								if (!CollectionUtils.isEmpty(errorList)) {
									isError = false;
									errorMap.put(String.valueOf(i), errorList);
								}
							}
						}
						
						if (isError && !CollectionUtils.isEmpty(errorList)) {
							errorMap.put(String.valueOf(i), errorList);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception checkUploadFiled()---->", "service Exception", e);
		}
		return TempMerchantDTOs;
	}
	/**
	 * Purpose::獲取上傳的數據
	 * @author HermanWang
	 * @param cell:獲取上傳的單元格
	 * @param getTime:是否為時間格式
	 * @return String：單元數據格內容
	 */
	private String getCellFormatValue(Cell cell, boolean getTime) {
		String cellvalue = "";
		try {
			if (cell != null) {
				// 判断当前Cell的Type
				switch (cell.getCellType()) {
					// 如果当前Cell的Type为NUMERIC
					case Cell.CELL_TYPE_NUMERIC:
					case Cell.CELL_TYPE_FORMULA: {
						// 判断当前的cell是否为Date
						if (DateUtil.isCellDateFormatted(cell)) {
							SimpleDateFormat sdf = null;
							Date date = cell.getDateCellValue();
							if (getTime) {
								sdf = new SimpleDateFormat("HH:mm");
							}
							cellvalue = sdf.format(date);
						}else {
							// 取得当前Cell的数值
							cellvalue = String.valueOf(((cell.getNumericCellValue())));
						}
						break;
					}
					case Cell.CELL_TYPE_STRING:
						cellvalue = cell.getRichStringCellValue().getString();
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						cellvalue = String.valueOf(cell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_BLANK:
						cellvalue = "";
						break;
					default:
						cellvalue = " ";
				}
			} else {
				cellvalue = "";
			}
		} catch (Exception e) {
			LOGGER.error("Exception getCellFormatValue()---->", "service Exception", e);
		}
		return cellvalue;
	}
	
	/**
	 * Purpose:根據name拿到value
	 * @author HermanWang
	 * @param parameters：下拉框列表
	 * @param name 傳入的name
	 * @return 得到的value
	 */
	private String getValueByName(List<Parameter> parameterList, String name, int row, String cell, String form, List<String> errorList){
		String value = null;
		// 错误信息
		for (Parameter param : parameterList){
			if((param.getName()).equals(name)){
				value = (String) param.getValue();
				break;
			}
		}
		// 没有得到相应的value值给出提示信息
		if(!StringUtils.hasText(value)){
			errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_IS_NOT_EXIST, new String[]{String.valueOf((row)), cell, form}, null) );
		}
		return value;
	}
	/**
	 * Purpose:根據name拿到value,並判斷格式是否有誤
	 * @author HermanWang
	 * @param parameters：下拉框列表
	 * @param name 傳入的name
	 * @return 得到的value
	 */
	private String checkValueByName(List<Parameter> parameterList, String name, int row, String cell, List<String> errorList){
		String value = null;
		// 错误信息
		for (Parameter param : parameterList){
			if((param.getName()).equals(name)){
				value = (String) param.getValue();
				break;
			}
		}
		// 没有得到相应的value值给出提示信息
		if(!StringUtils.hasText(value)){
			errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_FORMAT_ERROR, new String[]{String.valueOf((row)), cell}, null));
		}
		return value;
	}
	
	/**
	 * Purpose:英文和數字檢測
	 * @author HermanWang
	 * @param param:參數
	 * @param errorList:錯誤信息
	 */
	private void checkMerchantCode(String param, int row, String cell,List<String> errorList) {
		//判斷是否numberOrEnglish
		try {
			if(!param.matches("^[a-zA-Z0-9]+$")){
				errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_LIMIT_ENGLISH_AND_NUMBER, new String[]{String.valueOf((row)), cell}, null));
			}
		} catch (Exception e) {
			LOGGER.error("Exception checkMerchantCode()---->", "service Exception", e);
		}
	}	
	/**
	 * Purpose:判斷起止日期是否符合要求
	 * @author HermanWang
	 * @param openHour:開始時間
	 * @param closeHour:結束時間
	 * @param errorList:錯誤信息
	 */
	private void checkTimeFormat(String time, int row, String cell,List<String> errorList) {
		String errorMsg = null;
		try {
			if(StringUtils.hasText(time)){
				String check = "^([0-1][0-9]|[2][0-3]):([0-5][0-9])$";
				if(!time.matches(check)){
					errorMsg = i18NUtil.getName(IAtomsMessageCode.OPEN_TIME_FORMAT);
					errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_COLUMN_NAME, new String[]{String.valueOf((row)), cell}, null) + errorMsg);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception checkTimeFormat()---->", "service Exception", e);
		}
	}
	/**
	 * Purpose:判斷起止日期是否符合要求
	 * @author HermanWang
	 * @param openHour:開始時間
	 * @param closeHour:結束時間
	 * @param errorList:錯誤信息
	 */
	private void checkTime(String openHour, String closeHour, int row, String cell,String cell2,List<String> errorList) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String errorMsg = null;
		try {
			if((StringUtils.hasText(closeHour)||StringUtils.hasText(openHour))){
				if(sdf.parse(closeHour).before(sdf.parse(openHour))){
					errorMsg = i18NUtil.getName(IAtomsMessageCode.TIME_OUT_INPUT);
					errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((row)), cell}, null) + errorMsg);
				}
			}
		} catch (ParseException e) {
			LOGGER.error("Exception checkTime()---->"+ e, e);
		}
	}
	
	/**
	 * Purpose:檢查ENAIL格式
	 * @author HermanWang
	 * @param aoEmail:參數
	 * @param errorList:錯誤信息
	 */
	private void checkEmail(String aoEmail, int row, String cell, List<String> errorList) {
		try {
			if(StringUtils.hasText(aoEmail)){
				String check = "^[a-zA-Z0-9_+.-]+\\@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,4}$";
				if(!aoEmail.matches(check)){
					errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_FORMAT_ERROR, new String[]{String.valueOf((row)), cell}, null));
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception checkEmail()---->"+ e, e);
		}
	}
	/**
	 * Purpose:檢查移動電話的格式
	 * @author HermanWang
	 * @param phone:移動電話
	 * @param row:行
	 * @param cell：列
	 * @param errorList：錯誤list
	 * @return void：無返回值
	 */
	private void checkPhone(String phone, int row,List<String> errorList) {
		String errorMsg = null;
		try {
			if(StringUtils.hasText(phone)){
				String check = "^(09)\\d{8}$";
				if(!phone.matches(check)){
					errorMsg = i18NUtil.getName(IAtomsMessageCode.PHONE_INPUT);
					errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((row))}, null) + IAtomsConstants.MARK_SEPARATOR + errorMsg);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception checkPhone()---->"+ e, e);
		}
	}
	
	
	/**
	 * Purpose:檢查參數的長度或者是否為空
	 * @author HermanWang
	 * @param param：參數
	 * @param row：行號
	 * @param cell：列號
	 * @param isNull：是否可以為空，為空則不需檢查長度,
	 * @param length：允許最大長度
	 * @param errorList 錯誤消息集合
	 */
	private void check(String param,int row,String cell,boolean isNull,int length, List<String> errorList) {
		try {
			if (isNull) {
				//可以為空
				if (StringUtils.hasText(param) && param.trim().length() > length) {
					errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_LIMIT_LENGTH, new String[]{String.valueOf((row)), cell,length+""}, null));
				}
			} else {
				if (!StringUtils.hasText(param)) {
					errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_IS_NOT_EMPTY, new String[]{String.valueOf((row)), cell}, null));
				}else {
					if (param.trim().length() > length) {
						errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_LIMIT_LENGTH, new String[]{String.valueOf((row)), cell,length+""}, null));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception check()---->"+ e, e);
		}
	}
	
	/**
	 * Purpose::將錯誤訊息存入txt文檔
	 * @author HermanWang
	 * @param errorMap:錯誤信息集合
	 * @return String 文檔名
	 */
	private Map<String, String> saveErrorMsg(Map<String, List<String>> errorMap) {
		PrintWriter printWriter = null;
		String fileName = null; 
		Map<String, String> map = new HashMap<String, String>();
		try {
			if (!CollectionUtils.isEmpty(errorMap)) {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(i18NUtil.getName(IAtomsMessageCode.PARAM_ERROR_INFORMATION) + IAtomsConstants.MARK_ENTER);
				Set<String> keySet = errorMap.keySet();
				Iterator<String> iterator = keySet.iterator();
				String key = null;
				List<String> errorList = null;
				while (iterator.hasNext()) {
					key = iterator.next();
					errorList = errorMap.get(key);
					if (!CollectionUtils.isEmpty(errorList)) {
						for (String errorMsg : errorList) {
							stringBuffer.append(errorMsg + IAtomsConstants.MARK_ENTER);
						}
					}
				}
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				//錯誤信息文件名
				fileName = UUID.randomUUID().toString() + IAtomsConstants.MARK_NO + IAtomsConstants.PARAM_FILE_SUFFIX_TXT;
				
				//文件路徑
				//String errorFilePath = WfSystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
				String errorFilePath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH) + File.separator 
						+ yearMonthDay + File.separator + IAtomsConstants.UC_NO_BIM_02070 + File.separator + IAtomsConstants.PARAM_STRING_IMPORT;
				File filePath = new File(errorFilePath);
				if (!filePath.exists() || !filePath.isDirectory()) {
					filePath.mkdirs();
				}
				map.put(MerchantFormDTO.ERROR_FILE_NAME, fileName);
				map.put(MerchantFormDTO.TEMP_FILE_PATH, errorFilePath);
				File saveFile = new File(errorFilePath, fileName);
				printWriter = new PrintWriter(saveFile, IAtomsConstants.ENCODE_UTF_8);
				printWriter.print(stringBuffer.toString());
				}
			} catch (Exception e) {
				LOGGER.error("saveErrorMsg() Error -->" + e);
			} finally {
				if (printWriter != null) {
					printWriter.close();
				}
			}
			return map;
		}
	/**
	 * Purpose::求出真實的匯入行數（有值的）
	 * @author HermanWang
	 * @param sheet
	 * @return
	 */
	private int getExcelRealRowCount(Sheet sheet) {
		int rowCount = 0;
		if(sheet != null){
			//拿到sheet下第一行的行號
			int beginRow = sheet.getFirstRowNum();
			//拿到最後一行的行號
			int endRow = sheet.getLastRowNum();  
			int beginCell = 0;
			int endCell = 0;
			Row tempRow = null;
			Boolean emptyRow = false;
			//循環sheet裡面的所有行
			for (int i = beginRow; i <= endRow; i++) {  
				tempRow = sheet.getRow(i);
				emptyRow = false;
				if(tempRow != null){
					//拿到第一個cell
					beginCell = tempRow.getFirstCellNum();
					//拿到最後一個cell
			    	endCell = tempRow.getLastCellNum();
			    	for(int j = beginCell; j <= endCell; j++){
				        if (!StringUtils.hasText(this.getCellFormatValue(tempRow.getCell(j), true))) {  
				            continue;  
				        } else {
				        	emptyRow = true;
				        	break;
				        }
			    	}
			    	if(emptyRow){
			    		 rowCount ++;
			    	} else {
			    		break;
			    	}
		    	}
		    }  
		}
		return rowCount;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantService#initMid(cafe.core.context.SessionContext)
	 */
	public SessionContext initMid(SessionContext sessionContext) throws ServiceException {
		try {
			MerchantFormDTO bimMerchantFormDTO = null;
			String companyId = "";
			CompanyDTO companyDTO = new CompanyDTO();
			bimMerchantFormDTO = (MerchantFormDTO) sessionContext.getRequestParameter();
			if (bimMerchantFormDTO != null) {
				companyId = bimMerchantFormDTO.getQueryCompanyId();
				companyDTO = this.companyDAO.getCompanyDTOByCompanyId(companyId);
			} else {
				bimMerchantFormDTO = new MerchantFormDTO();
			}
			bimMerchantFormDTO.setCompanyDTO(companyDTO);
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(bimMerchantFormDTO);
			return sessionContext;
		} catch (Exception e) {
			LOGGER.error(".initMid(SessionContext sessionContext):" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
	}
	
	
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantService#getMerchantDTO(cafe.core.context.MultiParameterInquiryContext)
	 */
	public MerchantDTO getMerchantDTOBy(MultiParameterInquiryContext param) throws ServiceException {
		MerchantDTO merchantDTO = null;
		try{
			String merchantId = (String) param.getParameter(MerchantDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			String customerId = (String) param.getParameter(MerchantDTO.ATTRIBUTE.COMPANY_ID.getValue());
			String merchantCode = (String) param.getParameter(MerchantDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			merchantDTO = this.merchantNewDAO.getMerchantInfo(merchantId, merchantCode, customerId, null);
		} catch (DataAccessException e) {
			LOGGER.error(".getMerchantDTO() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".getMerchantDTO() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return merchantDTO;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantService#getMerchantsByCodeAndCompamyId(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getMerchantsByCodeAndCompamyId(
			MultiParameterInquiryContext param) throws ServiceException {
		// TODO Auto-generated method stub
		 List<Parameter> merchants = null;
		try {
			String merchantCode = (String) param.getParameter(MerchantDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			String customerId = (String) param.getParameter(MerchantDTO.ATTRIBUTE.COMPANY_ID.getValue());
			merchants = this.merchantNewDAO.getMerchantsByCodeAndCompamyId(merchantCode, customerId);
		} catch (DataAccessException e) {
			LOGGER.error(".getMerchantDTO() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".getMerchantDTO() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return merchants;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantService#queryMid(cafe.core.context.SessionContext)
	 */
	public SessionContext queryMid(SessionContext sessionContext) throws ServiceException {
			Message msg = null;
			try {
				MerchantFormDTO merchantNewFormDTO = (MerchantFormDTO) sessionContext.getRequestParameter();
				int count = 0;
				//查詢符合條件的特護特店
				List<MerchantDTO> merchantNewDTOs = this.merchantNewDAO.listBy(
						merchantNewFormDTO.getQueryCompanyId(),
						merchantNewFormDTO.getQueryName(),
						merchantNewFormDTO.getQueryMerchantCode(),
						//merchantNewFormDTO.getQueryStagesMerchantCode(),
						merchantNewFormDTO.getRows(),
						merchantNewFormDTO.getPage(),
						merchantNewFormDTO.getSort(),
						merchantNewFormDTO.getOrder());
				// 如果沒有查到資料就提示查詢失敗,如果查到資料就把資料都放到FormDTO中
				if (!CollectionUtils.isEmpty(merchantNewDTOs)) {
					//把資料放在formDTO中
					merchantNewFormDTO.setList(merchantNewDTOs);
					count = (this.merchantNewDAO.count(merchantNewFormDTO.getQueryCompanyId(), merchantNewFormDTO.getQueryName(), merchantNewFormDTO.getQueryMerchantCode())).intValue();
					merchantNewFormDTO.getPageNavigation().setRowCount(count);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
				} else {
					//沒有查到資料就返回查無資料信息
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				}
				Map map = new HashMap();
				if (!CollectionUtils.isEmpty(merchantNewDTOs)) {
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, count);
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, merchantNewDTOs);
				} else {
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, 0);
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, "");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.DATA_NOT_FOUND));
				}
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		//		sessionContext.setReturnMessage(msg);
		//		sessionContext.setResponseResult(merchantNewFormDTO);
				return sessionContext;
		} catch (DataAccessException e) { 
			LOGGER.error(".queryMid() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".queryMid() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}
	/**
	 * @param merchantHeaderDAO the merchantHeaderDAO to set
	 */
	public void setMerchantHeaderDAO(IMerchantHeaderDAO merchantHeaderDAO) {
		this.merchantHeaderDAO = merchantHeaderDAO;
	}
	/**
	 * @return the merchantNewDAO
	 */
	public IMerchantDAO getMerchantNewDAO() {
		return merchantNewDAO;
	}
	
	/**
	 * @param merchantNewDAO the merchantNewDAO to set
	 */
	public void setMerchantNewDAO(IMerchantDAO merchantNewDAO) {
		this.merchantNewDAO = merchantNewDAO;
	}
	/**
	 * @return the merchantHeaderDAO
	 */
	public IMerchantHeaderDAO getMerchantHeaderDAO() {
		return merchantHeaderDAO;
	}
	/**
	 * @return the companyDAO
	 */
	public ICompanyDAO getCompanyDAO() {
		return companyDAO;
	}
	/**
	 * @param companyDAO the companyDAO to set
	 */
	public void setCompanyDAO(ICompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}
	/**
	 * @return the bpidDAO
	 */
	public IBaseParameterItemDefDAO getBpidDAO() {
		return bpidDAO;
	}
	/**
	 * @param bpidDAO the bpidDAO to set
	 */
	public void setBpidDAO(IBaseParameterItemDefDAO bpidDAO) {
		this.bpidDAO = bpidDAO;
	}
	/**
	 * @return the attributes
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	/**
	 * @return the limitNumber
	 */
	public Integer getLimitNumber() {
		return limitNumber;
	}
	/**
	 * @param limitNumber the limitNumber to set
	 */
	public void setLimitNumber(Integer limitNumber) {
		this.limitNumber = limitNumber;
	}
	public IBaseParameterPostCodeDAO getBaseParameterPostCodeDAO() {
		return baseParameterPostCodeDAO;
	}
	public void setBaseParameterPostCodeDAO(
			IBaseParameterPostCodeDAO baseParameterPostCodeDAO) {
		this.baseParameterPostCodeDAO = baseParameterPostCodeDAO;
	}
	
	
}