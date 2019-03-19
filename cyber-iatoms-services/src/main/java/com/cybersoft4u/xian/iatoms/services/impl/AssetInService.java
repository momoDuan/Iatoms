package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
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

import org.apache.poi.hssf.usermodel.HSSFCell;
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
import cafe.core.config.SystemConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CheckSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetInInfoFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PropertyNumberImportFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.cybersoft4u.xian.iatoms.services.IAssetInService;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetInInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetInListDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractAssetDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContract;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetInInfo;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetInList;
/**
 * 
 * Purpose: 設備入庫Service
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016/5/12
 * @MaintenancePersonnel CarrieDuan
 */
public class AssetInService extends AtomicService implements IAssetInService {

	/**
	 * 日誌記錄物件
	 */
	public static final CafeLog LOGGER = CafeLogFactory.getLog(AssetInService.class);

	/**
	 * 設備入庫單主檔dao
	 */
	private IAssetInInfoDAO assetInInfoDAO;
	/**
	 * 設備入庫單明細檔dao
	 */
	private IAssetInListDAO assetInListDAO;
	/**
	 * 倉庫主檔dao
	 */
	private IWarehouseDAO wareHouseDAO;
	/**
	 * 合約設備檔dao
	 */
	private IContractAssetDAO contractAssetDAO;
	
	/**
	 * 庫存主當DAO
	 */
	private IDmmRepositoryDAO repositoryDAO;	
	
	/**
	 * 公司DAO
	 */
	private ICompanyDAO companyDAO;
	
	/**
	 * 合約DAO
	 */
	private IContractDAO contractDAO;
	
	/**
	 * 設備DAO
	 */
	private IAssetTypeDAO assetTypeDAO;
	/**
	 * 
	 */
	private List<CheckSettingDTO> checkSettingList;
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException{
		try {
			AssetInInfoFormDTO formDTO =  (AssetInInfoFormDTO) sessionContext.getRequestParameter();
			String size = SystemConfigManager.getProperty(IAtomsConstants.SETTING, IAtomsConstants.UPLOAD_FILE_SIZE);
			if (!StringUtils.hasText(size)) {
				size = IAtomsConstants.PARAM_UPLOAD_FILE_SIZE;
			}
			int rate = Integer.valueOf(IAtomsConstants.PARAM_UPLOAD_CONVERSION_RATE);
			size = String.valueOf(Integer.valueOf(size) * rate);
			formDTO.setUploadFileSize(size);
			sessionContext.setResponseResult(formDTO);
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS));
			return sessionContext;
		} catch (Exception e) {
			LOGGER.error("init()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#query(cafe.core.context.SessionContext)
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		AssetInInfoFormDTO formDTO = (AssetInInfoFormDTO) sessionContext.getRequestParameter();
		//入庫資料集合
		List<AssetInInfoDTO> assetInInfoDTOs = null;
		//入庫資料總條數
		Integer count = Integer.valueOf(0);
		Message msg = null;
		try {
			if (formDTO != null) {
				//查詢入庫資料總條數
				count = this.assetInInfoDAO.getCount(formDTO.getQueryContractId(), formDTO.getQueryAssetInId(), formDTO.getQueryCompanyId());
				if (!count.equals(Integer.valueOf(0))) {
					//查詢入庫資料集合
					assetInInfoDTOs = this.assetInInfoDAO.listAssetInInfoDTO(formDTO.getQueryContractId(), formDTO.getQueryAssetInId(), formDTO.getQueryCompanyId(), formDTO.getOrder(), 
										formDTO.getSort(), formDTO.getPage(), formDTO.getRows());
					if (!CollectionUtils.isEmpty(assetInInfoDTOs)) {
						for (AssetInInfoDTO assetInInfoDTO : assetInInfoDTOs) {
							String updatedByName = assetInInfoDTO.getUpdatedByName();
							if(StringUtils.hasText(updatedByName) && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
								assetInInfoDTO.setUpdatedByName(updatedByName.substring(updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
							}
							String createdByName = assetInInfoDTO.getCreatedByName();
							if(StringUtils.hasText(createdByName) && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
								assetInInfoDTO.setCreatedByName(createdByName.substring(createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
							}
						}
						formDTO.setList(assetInInfoDTOs);
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
					} 
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				}
			}
			//設置返回總條數
			formDTO.getPageNavigation().setRowCount(count.intValue());
			//設置返回結果
			sessionContext.setResponseResult(formDTO);
			sessionContext.setReturnMessage(msg);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error("query()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("query()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#listAssetInList(cafe.core.context.SessionContext)
	 */
	public SessionContext listAssetInList(SessionContext sessionContext) throws ServiceException {
		try {
			AssetInInfoFormDTO formDTO = (AssetInInfoFormDTO) sessionContext.getRequestParameter();
			String assetInId = formDTO.getQueryAssetInId();
			List<AssetInListDTO> assetInListDTOs = new ArrayList<AssetInListDTO>();
			Integer assetListCount = Integer.valueOf(0);
			DmmAssetInInfo assetInInfo = null;
			//調用查詢
			if (StringUtils.hasText(assetInId)) {
				assetListCount = this.assetInListDAO.count(assetInId);
				if (!assetListCount.equals(Integer.valueOf(0))) {
					assetInListDTOs = this.assetInListDAO.listBy(assetInId, formDTO.getOrder(), formDTO.getSort(), formDTO.getPage(), formDTO.getRows());
					for (AssetInListDTO assetInListDTO : assetInListDTOs) {
						//將修改人員名稱改為中文
						String updatedByName = assetInListDTO.getUpdateUserName();
						if(StringUtils.hasText(updatedByName) && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
							assetInListDTO.setUpdateUserName(updatedByName.substring(updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
						}
					}
				}
			}
			//查詢待入庫資料時，設置返回值
			Map map = new HashMap();
			//設備入庫主檔
			if (!CollectionUtils.isEmpty(assetInListDTOs)) {
				boolean isAcceptance = this.checkAcceptance(assetInId);
				if (!isAcceptance) {
					map.put(IAtomsConstants.PARAM_ACTUAL_ACCEPTANCE, Boolean.FALSE);
				} else {
					//驗證是否已經完成實際驗收
					assetInInfo = this.assetInInfoDAO.findByPrimaryKey(DmmAssetInInfo.class, assetInId);
					if(assetInInfo != null && IAtomsConstants.YES.equals(assetInInfo.getIsFinished())) {
						map.put(IAtomsConstants.PARAM_FINISH_ACCEPTANCE, Boolean.TRUE);
					} else {
						map.put(IAtomsConstants.PARAM_FINISH_ACCEPTANCE, Boolean.FALSE);
					}
					map.put(IAtomsConstants.PARAM_ACTUAL_ACCEPTANCE, Boolean.TRUE);
				}
			}
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, assetListCount);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, assetInListDTOs);
		
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		} catch (DataAccessException e) {
			LOGGER.error("listAssetInList()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("listAssetInList()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#listAssetInId(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getAssetInIdList(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		//入庫批號集合
		List<Parameter> result = null;
		try {
			//獲取入庫批號集合
			String  isDone =  (String) inquiryContext.getParameter(AssetInInfoDTO.ATTRIBUTE.IS_DONE.getValue());
			//查詢入庫批號集合
			result = this.assetInInfoDAO.listAssetInId(isDone);
		} catch (Exception e) {
			LOGGER.error("getAssetInIdList()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return result;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#getAssetInfoDTOByAssetInId(cafe.core.context.MultiParameterInquiryContext)
	 */
	public AssetInInfoDTO getAssetInfoDTOByAssetInId(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		AssetInInfoDTO assetInInfoDTO = null;
		try {
			//入庫批號
			String assetInId = (String) inquiryContext.getParameter(AssetInInfoDTO.ATTRIBUTE.ASSET_IN_ID.getValue());
			if (StringUtils.hasText(assetInId)) {
				this.assetInInfoDAO.getDaoSupport().flush();
				//根據入庫批號獲取設備入庫單主檔
				DmmAssetInInfo assetInInfo = this.assetInInfoDAO.findByPrimaryKey(DmmAssetInInfo.class, assetInId);
				if (assetInInfo != null) {
					//設備入庫主檔
					assetInInfoDTO = new AssetInInfoDTO();
					//transformer
					Transformer transformer = new SimpleDtoDmoTransformer();
					//dmo to DTO
					assetInInfoDTO = (AssetInInfoDTO) transformer.transform(assetInInfo, assetInInfoDTO);
					//將修改人員名稱改為中文
					String updatedByName = assetInInfoDTO.getUpdatedByName();
					if(StringUtils.hasText(updatedByName) && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
						assetInInfoDTO.setUpdatedByName(updatedByName.substring(updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
					}
					String createdByName = assetInInfoDTO.getCreatedByName();
					if(StringUtils.hasText(createdByName) && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
						assetInInfoDTO.setCreatedByName(createdByName.substring(createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("getAssetInfoDTOByAssetInId()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return assetInInfoDTO;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#save(cafe.core.context.SessionContext)
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		LOGGER.debug(this.getClass().getName() +".save()--> start");
		//formDTO
		AssetInInfoFormDTO formDTO = (AssetInInfoFormDTO) sessionContext.getRequestParameter();
		AssetInInfoDTO assetInInfoDTO = null;
		String assetInId = "";
		Message message = null;
		try {
			if (formDTO != null) {
				assetInInfoDTO = formDTO.getAssetInInfoDTO();
				if (assetInInfoDTO != null) {
					//只選擇了合約id，沒有選擇客戶，則需要用合約id帶入客戶id
					//update by 2017/08/16 hermanwang Bug #2191
					if(StringUtils.hasText(assetInInfoDTO.getContractId()) && !StringUtils.hasText(assetInInfoDTO.getCustomerId())) {
						BimContract bimContract = this.contractDAO.findByPrimaryKey(BimContract.class, assetInInfoDTO.getContractId());
						assetInInfoDTO.setCustomerId(bimContract.getCompanyId());
					}
					//設置主鍵
					assetInId = assetInInfoDTO.getAssetInId();
					if (StringUtils.hasText(assetInId)) {
						assetInInfoDTO.setId(assetInInfoDTO.getAssetInId());
					} else {
						//設備入庫批號
						String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD);
						//流水號
						long id = this.getSequenceNumberControlDAO().getSeqNo(IAtomsConstants.IATOMS_TB_NAME_ASSET_IN_INFO, yearMonthDay);
						if (id == 0) {
							id++;
						}
						String swiftNumber = StringUtils.toFixString(4, id );
						StringBuffer transInfoId = new StringBuffer();
						//生成轉倉批號
						transInfoId.append(AssetInInfoFormDTO.PARAM_IN).append(yearMonthDay).append(swiftNumber);
						if (transInfoId != null) {
							assetInId = transInfoId.toString();
						}
						assetInInfoDTO.setId(assetInId);
					}
				}
				IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
				if ( logonUser == null) {
					throw new ServiceException(IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT);
				}
				if (assetInInfoDTO != null && message == null) {
					DmmAssetInInfo assetInInfo = this.getAssetInInfoDAO().findByPrimaryKey(DmmAssetInInfo.class,assetInInfoDTO.getId());
					if (assetInInfo != null) {
						//廠商id
						assetInInfo.setCompanyId(assetInInfoDTO.getCompanyId());
						//合約編號
						assetInInfo.setContractId(assetInInfoDTO.getContractId());
						//客戶
						assetInInfo.setCustomerId(assetInInfoDTO.getCustomerId());
						//設備編號
						assetInInfo.setAssetTypeId(assetInInfoDTO.getAssetTypeId());
						//使用人
						assetInInfo.setUserId(assetInInfoDTO.getUserId());
						//保管人
						assetInInfo.setKeeperName(assetInInfoDTO.getKeeperName());
						//倉庫編號
						assetInInfo.setWarehouseId(assetInInfoDTO.getWarehouseId());
						//說明
						assetInInfo.setComment(assetInInfoDTO.getComment());
						//維護模式
						assetInInfo.setMaType(assetInInfoDTO.getMaType());
						//設備型號
						assetInInfo.setAssetModel(assetInInfoDTO.getAssetModel());
						//cyber驗收日期
						assetInInfo.setCyberApprovedDate(assetInInfoDTO.getCyberApprovedDate());
						//客戶驗收日期
						assetInInfo.setCustomerApproveDate(assetInInfoDTO.getCustomerApproveDate());
						//資產owner
						assetInInfo.setOwner(assetInInfoDTO.getOwner());
						//設備廠牌
						assetInInfo.setBrand(assetInInfoDTO.getBrand());
					} else {
						assetInInfo = new DmmAssetInInfo();
						//DTO轉DMO
						Transformer transformer = new SimpleDtoDmoTransformer();
						assetInInfo = (DmmAssetInInfo) transformer.transform(assetInInfoDTO, assetInInfo);
						//是否已入庫
						assetInInfo.setIsDone(IAtomsConstants.NO);
						//取得日期
						//assetInInfo.setGetDate(DateTimeUtils.getCurrentDate());
						//是否為備機
					//	assetInInfo.setIsSpareParts(IAtomsConstants.NO);
						assetInInfo.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
						assetInInfo.setCreatedById(logonUser.getId());
						assetInInfo.setCreatedByName(logonUser.getName());
					}
					//設置更新訊息
					assetInInfo.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					assetInInfo.setUpdatedById(logonUser.getId());
					assetInInfo.setUpdatedByName(logonUser.getName());
					//保存或修改
					this.getAssetInInfoDAO().getDaoSupport().saveOrUpdate(assetInInfo);
					//flush
					this.getAssetInInfoDAO().getDaoSupport().flush();
					message = new Message(Message.STATUS.SUCCESS,IAtomsMessageCode.SAVE_SUCCESS,new String []{this.getMyName()});
				}
			}
		
			//返回消息message
			sessionContext.setReturnMessage(message);	
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			map.put(AssetInInfoFormDTO.QUERY_ASSET_IN_ID, assetInId);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			LOGGER.debug(this.getClass().getName() +".save()--> end");	
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error("save()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE,new String []{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("save()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		
		
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#delete(cafe.core.context.SessionContext)
	 */
	public SessionContext delete(SessionContext sessionContext)
			throws ServiceException {
		LOGGER.debug(this.getClass().getName() +".delete()--> start");
		//formDTO
		AssetInInfoFormDTO formDTO = (AssetInInfoFormDTO) sessionContext.getRequestParameter();
		Message message = null;
		try {
			if (formDTO != null) {
				//設備入庫主檔DTO
				AssetInInfoDTO assetInInfoDTO =  formDTO.getAssetInInfoDTO();
				IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
				if (assetInInfoDTO != null && StringUtils.hasText(assetInInfoDTO.getAssetInId())) {
					//設備入庫主檔
					DmmAssetInInfo assetInInfo = this.getAssetInInfoDAO().findByPrimaryKey(DmmAssetInInfo.class, assetInInfoDTO.getAssetInId());
					if (assetInInfo != null) {
						//刪除明細表
						this.getAssetInListDAO().deleteAssetInListByAssetInId(assetInInfo.getId());
						this.getAssetInInfoDAO().delete(assetInInfo);
						this.getAssetInInfoDAO().getDaoSupport().evict(assetInInfo);
						//返回消息message
						message = new Message(Message.STATUS.SUCCESS,IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
					} else {
						message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_ALERDAY_UPDATE);
					}
				}
			}
			sessionContext.setReturnMessage(message);
			LOGGER.debug(this.getClass().getName() +".delete()--> end");
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error("delete()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE,new String[]{this.getMyName()},e);
		} catch (Exception e) {
			LOGGER.error("delete()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#addAssetInList(cafe.core.context.SessionContext)
	 */
	public SessionContext addAssetInList(SessionContext sessionContext) throws ServiceException {
		LOGGER.debug(this.getClass().getName() +".addAssetInList()--> start");
		//formDTO
		AssetInInfoFormDTO formDTO = ( AssetInInfoFormDTO ) sessionContext.getRequestParameter();
		//設置返回訊息
		Map map = new HashMap();
		Message message = null;
		
		try {
			if (formDTO != null) {
				AssetInInfoDTO assetInInfoDTO = formDTO.getAssetInInfoDTO();
				if (assetInInfoDTO != null && StringUtils.hasText(assetInInfoDTO.getAssetInId())) {
					DmmAssetInInfo assetInInfo = this.assetInInfoDAO.findByPrimaryKey(DmmAssetInInfo.class, assetInInfoDTO.getAssetInId());
					if (assetInInfo == null || IAtomsConstants.YES.equals(assetInInfo.getIsDone())) {
						message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_ALERDAY_UPDATE);
					}
				}
				//登錄者資訊
				IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
				if ( logonUser == null) {
					throw new ServiceException(IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT);
				}
				//獲取設備序號
				String serialNumber = assetInInfoDTO.getSerialNumber();
				//核檢設備序號與設備入庫資料明細檔是否重複
				AssetInListDTO assetInInfoDTOBySerial = this.assetInListDAO.getAssetInListDTO(serialNumber);
				//核檢設備序號與庫存主檔是否重複
				DmmRepositoryDTO repositoryDTO = this.repositoryDAO.getRepositoryBySerialNumber(serialNumber, null, null);
				if (assetInInfoDTOBySerial != null || repositoryDTO != null) {
					message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ASSET_NUMBER_IS_REPETITION);
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				} else if (StringUtils.hasText(assetInInfoDTO.getPropertyId())) {
					//核檢財產編號與設備入庫資料明細檔是否重複
					boolean isAssetInListRepeat = this.assetInListDAO.isCheckRropertyIdRepeat(assetInInfoDTO.getPropertyId());
					//核檢財產編號與庫存主檔是否重複
					boolean isRepositoryRepeat = this.repositoryDAO.isPropertyIdRepeat(assetInInfoDTO.getPropertyId(), null);
					if (isAssetInListRepeat || isRepositoryRepeat) {
						message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PROPERTY_ID_IS_REPETITION);
						map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					}
				} 
				if (message == null) {
					//設備入庫單明細檔
					DmmAssetInList assetInList = new DmmAssetInList(); 
					//id----
					assetInList.setId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_ASSET_IN_LIST));
					//入庫批號
					assetInList.setAssetInId(assetInInfoDTO.getAssetInId());
					//財產編號
					assetInList.setPropertyId(assetInInfoDTO.getPropertyId());
					//設備序號
					assetInList.setSerialNumber(assetInInfoDTO.getSerialNumber());
					//是否實際驗收
					assetInList.setIsChecked(IAtomsConstants.NO);
					//是否客戶驗收
					assetInList.setIsCustomerChecked(IAtomsConstants.NO);
					//更新者訊息
					assetInList.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					assetInList.setUpdatedById(logonUser.getId());
					assetInList.setUpdatedByName(logonUser.getName());
					//保存
					this.getAssetInListDAO().save(assetInList);
					this.getAssetInListDAO().getDaoSupport().evict(assetInList);
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
					//返回訊息
					message = new Message(Message.STATUS.SUCCESS);
					DmmAssetInInfo assetInInfo = this.assetInInfoDAO.findByPrimaryKey(DmmAssetInInfo.class, assetInInfoDTO.getAssetInId());
					if (assetInInfo != null) {
						assetInInfo.setIsFinished(IAtomsConstants.NO);
						this.assetInInfoDAO.getDaoSupport().saveOrUpdate(assetInInfo);
					}
				}
			} else {
				message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INSERT_FAILURE, new String[]{IAtomsConstants.ASSET});
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
			}
			sessionContext.setReturnMessage(message);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			LOGGER.debug(this.getClass().getName() +".addAssetInList()--> end");
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error("addAssetInList()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.INSERT_FAILURE, new String[]{IAtomsConstants.ASSET}, e);
		} catch (Exception e) {
			LOGGER.error("addAssetInList()", "Service Exception", e);
			LOGGER.error(this.getClass().getName() + ".addAssetInList() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.INSERT_FAILURE, new String[]{IAtomsConstants.ASSET}, e);
		}
		
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#deleteAssetInList(cafe.core.context.SessionContext)
	 */
	public SessionContext deleteAssetInList(SessionContext sessionContext)
			throws ServiceException {
		LOGGER.debug(this.getClass().getName() +".deleteAssetInList()--> start");
		//formDTO
		AssetInInfoFormDTO formDTO = (AssetInInfoFormDTO) sessionContext.getRequestParameter();
		try {
			if (formDTO != null) {
				String assetInId = formDTO.getQueryAssetInId();
				//核檢需要刪除的資料是否刪除或者異動
				List<String> assetListIds = formDTO.getAssetListIds();
				if (StringUtils.hasText(assetInId) && !CollectionUtils.isEmpty(assetListIds)) {
					DmmAssetInList assetInList = null;
					//核檢資料是否已被刪除或者入庫
					DmmAssetInInfo assetInInfo = this.assetInInfoDAO.findByPrimaryKey(DmmAssetInInfo.class, assetInId);
					if (assetInInfo == null || (assetInInfo != null && IAtomsConstants.YES.equals(assetInInfo.getIsDone()))) {
						sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_ALERDAY_UPDATE));
						return sessionContext;
					}
					//核檢需要刪除的資料是否存在
					for (String id : assetListIds) {
						assetInList = this.assetInListDAO.findByPrimaryKey(DmmAssetInList.class, id);
						if (assetInList == null) {
							sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_ALERDAY_UPDATE));
							return sessionContext;
						}
					}
					//根據id刪除設備入庫明細檔
					this.getAssetInListDAO().deleteAssetListByIds(assetListIds);
					assetInInfo.setIsFinished(IAtomsConstants.NO);
					this.assetInInfoDAO.getDaoSupport().saveOrUpdate(assetInInfo);
					sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS));
				} else {
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DELETE_FAILURE,new String[]{IAtomsConstants.ASSET}));
				}
			} else {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DELETE_FAILURE,new String[]{IAtomsConstants.ASSET}));
			}
			
			//設置返回訊息
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			LOGGER.debug(this.getClass().getName() +"----deleteAssetList()--> end");
			return sessionContext;
		}  catch (DataAccessException e) {
			LOGGER.error("deleteAssetList()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE,new String[]{IAtomsConstants.ASSET}, e);
		} catch (Exception e) {
			LOGGER.error("deleteAssetList()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE,new String[]{IAtomsConstants.ASSET}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#actualAcceptance(cafe.core.context.SessionContext)
	 */
	public SessionContext actualAcceptance(SessionContext sessionContext) throws ServiceException {
		LOGGER.debug(this.getClass().getName() +".actualAcceptance()--> start");
		try {
			//設置返回訊息
			Map map = new HashMap();
			Integer actualNum = null;
			//formDTO
			AssetInInfoFormDTO formDTO = (AssetInInfoFormDTO) sessionContext.getRequestParameter();
			if (formDTO != null && !CollectionUtils.isEmpty(formDTO.getAssetListIds())) {
				//登錄者資訊
				IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
				if ( logonUser == null) {
					throw new ServiceException(IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT);
				}
				//設備ID
				String assetInId = formDTO.getQueryAssetInId();
				DmmAssetInInfo assetInInfo = this.assetInInfoDAO.findByPrimaryKey(DmmAssetInInfo.class, assetInId);
				if (assetInInfo == null || IAtomsConstants.YES.equals(assetInInfo.getIsDone())) {
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_ALERDAY_UPDATE));
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
				//需要驗收的編號
				List<String> updateIds = formDTO.getAssetListIds();
				for (String assetInListId : updateIds) {
					//設備入庫明細檔
					DmmAssetInList assetInList = this.getAssetInListDAO().findByPrimaryKey(DmmAssetInList.class, assetInListId);
					if (assetInList != null) {
						//設置更新訊息
						assetInList.setCheckedDate(DateTimeUtils.getCurrentTimestamp());
						assetInList.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						assetInList.setUpdatedById(logonUser.getId());
						assetInList.setUpdatedByName(logonUser.getName());
						//設置驗收
						assetInList.setIsChecked(IAtomsConstants.YES);
						this.getAssetInListDAO().getDaoSupport().update(assetInList);
						this.getAssetInListDAO().getDaoSupport().flush();
					}
				}
				//設備入庫主檔
				DmmAssetInInfo assetInfo =	this.getAssetInInfoDAO().findByPrimaryKey(DmmAssetInInfo.class, assetInId);
				if (assetInfo != null) {
					boolean isAcceptance = this.checkAcceptance(assetInId);
					actualNum = this.getAssetInListDAO().getAcceptCount(assetInId, Boolean.TRUE);
					if (!isAcceptance) {
						map.put(IAtomsConstants.PARAM_ACTUAL_ACCEPTANCE, false);
					} else {
						map.put(IAtomsConstants.PARAM_ACTUAL_ACCEPTANCE, true);
					}
				}
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.ACTUAL_ACCEPTANCE, new String[]{actualNum.toString() }));
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			} else {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE,IAtomsMessageCode.ACTUAL_ACCEPTANCE_FAILURE));
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			LOGGER.debug(this.getClass().getName() +".actualAcceptance()--> end");
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error("actualAcceptance()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.ACTUAL_ACCEPTANCE_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("actualAcceptance()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.ACTUAL_ACCEPTANCE_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#customesAcceptance(cafe.core.context.SessionContext)
	 */
	public SessionContext customesAcceptance(SessionContext sessionContext)
			throws ServiceException {
		LOGGER.debug(this.getClass().getName() +".actualAcceptance()--> start");
		try {
			//formDTO
			AssetInInfoFormDTO formDTO = (AssetInInfoFormDTO) sessionContext.getRequestParameter();
			//設置返回訊息
			Map map = new HashMap();
			Integer actualNum = null;
			if (formDTO != null && !CollectionUtils.isEmpty(formDTO.getAssetListIds())) {
				//登錄者資訊
				IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
				if ( logonUser == null) {
					throw new ServiceException(IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT);
				}
				//設備ID
				String assetInId = formDTO.getQueryAssetInId();
				DmmAssetInInfo assetInInfo = this.assetInInfoDAO.findByPrimaryKey(DmmAssetInInfo.class, assetInId);
				if (assetInInfo == null || IAtomsConstants.YES.equals(assetInInfo.getIsDone())) {
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_ALERDAY_UPDATE));
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
				//需要驗收的編號
				List<String> updateIds = formDTO.getAssetListIds();
				for (String assetInListId : updateIds) {
					//設備入庫明細檔
					DmmAssetInList assetInList = this.getAssetInListDAO().findByPrimaryKey(DmmAssetInList.class, assetInListId);
					if (assetInList != null) {
						//設置更新訊息
						assetInList.setCustomerCheckedDate(DateTimeUtils.getCurrentTimestamp());
						assetInList.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						assetInList.setUpdatedById(logonUser.getId());
						assetInList.setUpdatedByName(logonUser.getName());
						//設置驗收
						assetInList.setIsCustomerChecked(IAtomsConstants.YES);
						this.getAssetInListDAO().getDaoSupport().update(assetInList);
						this.getAssetInListDAO().getDaoSupport().flush();
					}
				}
				actualNum = this.getAssetInListDAO().getCustomerAcceptCount(assetInId, Boolean.TRUE);
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS,IAtomsMessageCode.CUSTOMES_ACCEPTANCE, new String[]{actualNum.toString() }));
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			} else {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE,IAtomsMessageCode.CUSTOMES_ACCEPTANCE_FAILURE));
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			LOGGER.debug(this.getClass().getName() +"----actualAcceptance()--> end");
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error("customesAcceptance()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.CUSTOMES_ACCEPTANCE_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("customesAcceptance()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.CUSTOMES_ACCEPTANCE_FAILURE, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#finishAcceptance(cafe.core.context.SessionContext)
	 */
	public SessionContext finishAcceptance(SessionContext sessionContext)
			throws ServiceException {
		try {
			//formDTO
			AssetInInfoFormDTO formDTO = (AssetInInfoFormDTO) sessionContext.getRequestParameter();
			//返回消息
			Message msg = null;
			//設置返回訊息
			Map map = new HashMap();
			if (formDTO != null) {
				AssetInInfoDTO assetInInfoDTO =  formDTO.getAssetInInfoDTO();
				if (assetInInfoDTO != null && StringUtils.hasText(assetInInfoDTO.getAssetInId())) {
					IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
					if ( logonUser == null) {
						throw new ServiceException(IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT);
					}
					DmmAssetInInfo assetInInfo = this.assetInInfoDAO.findByPrimaryKey(DmmAssetInInfo.class, assetInInfoDTO.getAssetInId());
					if (assetInInfo == null || IAtomsConstants.YES.equals(assetInInfo.getIsDone())) {
						sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_ALERDAY_UPDATE));
						map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
						sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
						return sessionContext;
					}
					//入庫批號
					String assetInId = assetInInfoDTO.getAssetInId();
					//設備入庫主檔
					DmmAssetInInfo assetInfo =	this.getAssetInInfoDAO().findByPrimaryKey(DmmAssetInInfo.class, assetInId);
					if (assetInfo != null) {
						boolean isAcceptance = this.checkAcceptance(assetInId);
						if (isAcceptance) {
							assetInfo.setIsFinished(IAtomsConstants.YES);
							this.assetInInfoDAO.update(assetInfo);
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.FINISH_ACCEPTANCE);
						} else {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.FINISH_ACCEPTANCE_UNQUALIFIED);
						}
					}
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.FINISH_ACCEPTANCE_FAILURE);
			}
			sessionContext.setResponseResult(formDTO);
			sessionContext.setReturnMessage(msg);
			if (msg == null) {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error("finishAcceptance()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.FINISH_ACCEPTANCE_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("finishAcceptance()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.FINISH_ACCEPTANCE_FAILURE, e);
		}
		
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#storage(cafe.core.context.SessionContext)
	 */
	public SessionContext storage(SessionContext sessionContext) throws ServiceException {
		try {
			//formDTO
			AssetInInfoFormDTO formDTO = (AssetInInfoFormDTO) sessionContext.getRequestParameter();
			//返回消息
			Message msg = null;
			if (formDTO != null) {
				AssetInInfoDTO assetInInfoDTO =  formDTO.getAssetInInfoDTO();
				if (assetInInfoDTO != null && StringUtils.hasText(assetInInfoDTO.getAssetInId())) {
					IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
					if ( logonUser == null) {
						throw new ServiceException(IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT);
					}
					//入庫批號
					String assetInId = assetInInfoDTO.getAssetInId();
					//設備入庫主檔
					DmmAssetInInfo assetInfo =	this.getAssetInInfoDAO().findByPrimaryKey(DmmAssetInInfo.class, assetInId);
					if (assetInfo != null && !IAtomsConstants.YES.equals(assetInfo.getIsDone())) {
						//核檢資料是否驗收完成
						if (IAtomsConstants.NO.equals(assetInfo.getIsFinished())) {
							msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.ACCEPTANCE_NOUPTO_STANDARD);
							//核檢資料是否已被確認入庫
						} else if (IAtomsConstants.YES.equals(assetInfo.getIsDone())) {
							msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_ALERDAY_UPDATE);
						} else {
							sessionContext = this.save(sessionContext);
							msg = sessionContext.getReturnMessage();
							if (msg.getStatus() == Message.STATUS.SUCCESS) {
								//核檢資料是否以驗收達標
								boolean isAcceptance = this.checkAcceptance(assetInInfoDTO.getAssetInId());
								if (isAcceptance) {
									Date customerWarrantyDate = null;
									Date factoryWarrantyDate = null;
									//记录最原始客户验收日期，因为后续代码需要对客户验收日期进行计算
									Date customerApproveDate = null;
									Date cyberApprovedDate = null;
									//深度複製 - 取出cyber驗收日期與客戶驗收日期
									if (assetInfo.getCyberApprovedDate() != null) {
										cyberApprovedDate = (Date) assetInfo.getCyberApprovedDate().clone();
									}
									if (assetInfo.getCustomerApproveDate() != null) {
										customerApproveDate = (Date) assetInfo.getCustomerApproveDate().clone();
									}
									AssetInInfoDTO assetInInfoDTO2 = new AssetInInfoDTO();
									Transformer transformer = new SimpleDtoDmoTransformer();
									transformer.transform(assetInfo,assetInInfoDTO2);
									//獲取客戶保固起與原廠保固期
									BimContract contract = this.contractDAO.findByPrimaryKey(BimContract.class, assetInfo.getContractId());
									//驗收完成后的資料才能入庫
									if (assetInfo != null) {
										//如果維護模式為買斷，則根據客戶驗收日期與合約中的客戶保固期計算客戶保固日期
										if (assetInfo.getMaType().equals(IAtomsConstants.MA_TYPE_BUYOUT)) {
											Integer customerWarranty = contract.getCustomerWarranty();
											if (customerWarranty == null) {
												customerWarranty = 0;
											}
											customerWarrantyDate = assetInfo.getCustomerApproveDate();
											if (customerWarrantyDate != null) {
												customerWarrantyDate.setMonth(customerWarrantyDate.getMonth() + customerWarranty);
											}
										}
										//根據cyber驗收日期與合約中的廠商保固日計算原廠保固日期
										Integer factoryWarranty = contract.getFactoryWarranty();
										if (factoryWarranty == null) {
											factoryWarranty = 0;
										}
										factoryWarrantyDate = assetInfo.getCyberApprovedDate();
										if (factoryWarrantyDate != null) {
											factoryWarrantyDate.setMonth(factoryWarrantyDate.getMonth() + factoryWarranty);
										}
										//檢核入庫的倉庫是否為登錄者所控管的倉庫
										boolean isControlWarehouse = this.checkControlWarehouse(assetInfo.getWarehouseId(), logonUser);
										//只能入庫自己所控管的倉庫
										if (isControlWarehouse) {
											//設置更新訊息
											assetInfo.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
											assetInfo.setUpdatedById(logonUser.getId());
											assetInfo.setUpdatedByName(logonUser.getName());
											assetInfo.setCustomerApproveDate(customerApproveDate);
											assetInfo.setCyberApprovedDate(cyberApprovedDate);
											if (customerWarrantyDate != null) {
												assetInfo.setCustomerWarrantyDate(customerWarrantyDate);
											}
											assetInfo.setFactoryWarrantyDate(factoryWarrantyDate);
											//入庫標記
											assetInfo.setIsDone(IAtomsConstants.YES);
											//更新
											this.getAssetInInfoDAO().update(assetInfo);
											//新增至庫存表與庫存歷史表
											this.saveRepository(assetInfo,logonUser);
											//將入庫明細表中的數據刪除，存入歷史明細表中
											this.assetInListDAO.saveAssetInListInfo(assetInId);
											//入庫成功
											 msg = new Message(Message.STATUS.SUCCESS,IAtomsMessageCode.STORAGE_SUCCESS); 
										} else {
											msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.STORAGE_FAILURE); 
										}
									} else {
										msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.STORAGE_FAILURE); 
									}
								} else {
									msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.ACCEPTANCE_NOUPTO_STANDARD);
								}
								
							}
						}
					} else {
						msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_ALERDAY_UPDATE);
					}
				}
			}	
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
			//設置返回訊息
			Map map = new HashMap();
			if (msg.getStatus() == Message.STATUS.SUCCESS) {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error("storage()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.STORAGE_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("storage()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.STORAGE_FAILURE, e);
		}
	}
	
	/**
	 * Purpose: 驗證是否達到驗收標準
	 * @author CarrieDuan
	 * @param assetInId ：批號
	 * @return Map<String,String>
	 */
	public boolean checkAcceptance(String assetInId) {
		try {
			//獲取總條數
			Integer totalNumber = this.assetInListDAO.getAcceptCount(assetInId, false);
			//獲取已完成驗收的台數
			Integer checkNumber = this.assetInListDAO.getAcceptCount(assetInId, true);
			//需要完成的驗收筆數
			Double beDoNumber = 0.0;
			for (CheckSettingDTO checkSettingDTO: checkSettingList) {
				if (checkSettingDTO.getMinNumber() > totalNumber || 
						checkSettingDTO.getMaxNumber() < totalNumber) {
							continue;
				}
				if (IAtomsConstants.CHECK_SETTING_PERCENT.equals(checkSettingDTO.getCheckType())) {
					//百分比計算
					beDoNumber = Math.ceil(checkSettingDTO.getCheckValue() * totalNumber); //進一法
				} else if (IAtomsConstants.CHECK_SETTING_FIXED.equals(checkSettingDTO.getCheckType())) {
					//固定驗收條數。
					beDoNumber = checkSettingDTO.getCheckValue();
				} else if (IAtomsConstants.CHECK_SETTING_INTERVAL.equals(checkSettingDTO.getCheckType())) {
					//每滿多少台，則驗收多少台
					beDoNumber = (int)(Math.ceil((double)(totalNumber - checkSettingDTO.getMinNumber() + 1) / checkSettingDTO.getIntervalValue())) * checkSettingDTO.getAddValue() + checkSettingDTO.getCheckValue(); //進一法
				}
			}
			if (beDoNumber > checkNumber) {
				return false;
			}
		} catch (DataAccessException e) {
			LOGGER.error("checkAcceptance()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		} catch (Exception e) {
			LOGGER.error("checkAcceptance()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return true;
	}
	
	/**
	 * 
	 * Purpose: 保存入庫存表與庫存歷史
	 * @author hungli
	 * @param assetInfo 設備入庫檔
	 * @param logonUser 登錄者資訊
	 * @return void
	 */
	private void saveRepository(DmmAssetInInfo assetInfo, IAtomsLogonUser logonUser) {
		try {
			String assetId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_REPOSITORY);
			String propertyId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY);
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			//sdf.format(assetInfo.getCustomerApproveDate());
			Timestamp cyberApprovedDate = DateTimeUtils.toTimestamp(sdf.format(assetInfo.getCyberApprovedDate()));
			this.repositoryDAO.saveRepository(assetId, propertyId, logonUser.getId(), logonUser.getName(), assetInfo.getId(), cyberApprovedDate);
		} catch (Exception e) {
			LOGGER.error("saveRepository()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}

	/**
	 * 
	 * Purpose:
	 * @author cybersoft
	 * @param warehouseId
	 * @param logonUser
	 * @return boolean ture；自己控管的倉庫，false：不是
	 */
	private boolean checkControlWarehouse(String warehouseId,
			IAtomsLogonUser logonUser) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInService#upload(cafe.core.context.SessionContext)
	 */
	public SessionContext upload(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			//formDTO
			AssetInInfoFormDTO formDTO = (AssetInInfoFormDTO) sessionContext.getRequestParameter();
			//logonUser
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			//下載的錯誤文件信息名稱
			Map errorFileMap = new HashMap();
			Map map = new HashMap();
			if ( logonUser == null) {
				throw new ServiceException(IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT);
			}
			//匯入的文件
			MultipartFile uploadFiled = formDTO.getUploadFiled();
			if (uploadFiled != null) {
			Map<String, List<String>> errorMap = new LinkedHashMap<String, List<String>>();
				List<DmmAssetInList> list = this.checkUploadFiled(uploadFiled, errorMap);
				if (CollectionUtils.isEmpty(errorMap)) {
					if (!CollectionUtils.isEmpty(list)) {
						AssetInInfoDTO assetInInfoDTO = formDTO.getAssetInInfoDTO();
						for (DmmAssetInList assetInList : list) {
							String id = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_ASSET_IN_LIST);
							assetInList.setId(id);
							assetInList.setAssetInId(assetInInfoDTO.getAssetInId());
							//是否實際驗收
							assetInList.setIsChecked(IAtomsConstants.NO);
							//是否客戶驗收
							assetInList.setIsCustomerChecked(IAtomsConstants.NO);
							//更新者訊息
							assetInList.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
							assetInList.setUpdatedById(logonUser.getId());
							assetInList.setUpdatedByName(logonUser.getName());
							//保存
							this.getAssetInListDAO().getDaoSupport().save(assetInList);
						}
					}
					this.getAssetInListDAO().getDaoSupport().flush();
					msg = new Message(Message.STATUS.SUCCESS,IAtomsMessageCode.UPLOAD_SECCUSS);
				} else {
					if (errorMap.get(String.valueOf(0)) != null) {
						List<String> errorMsg = errorMap.get(String.valueOf(0));
						if (errorMsg.get(0).equals(IAtomsMessageCode.IATOMS_MSG_NONE_DATA)) {
							msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.IATOMS_MSG_NONE_DATA);
						} else {
							msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.ABOUCHEMENT_FAILURE_FOR_OVER_LIMIT, new String[]{PropertyNumberImportFormDTO.UPLOAD_NUMBER });
						}
					} else {
						//寫入錯誤信息
						errorFileMap = this.saveEorrorMsg(errorMap);
						if (!errorFileMap.isEmpty()) {
							map.put(AssetInInfoFormDTO.ERROR_FILE_NAME, errorFileMap.get(AssetInInfoFormDTO.ERROR_FILE_NAME));
							map.put(AssetInInfoFormDTO.ERROR_FILE_PATH, errorFileMap.get(AssetInInfoFormDTO.ERROR_FILE_PATH));
						}
						msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.UPLOAD_FAILURE);
					}
				}
			}
			sessionContext.setResponseResult(formDTO);
			sessionContext.setReturnMessage(msg);
			if (Message.STATUS.SUCCESS.equals(msg.getStatus())) {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		} catch (Exception e) {
			LOGGER.error("upload()", "Service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	/**
	 * 
	 * Purpose:將錯誤訊息保存入text文檔
	 * @author hungli
	 * @return
	 * @return String：文檔的name
	 */
	private Map saveEorrorMsg(Map<String, List<String>> errorMap) {
		PrintWriter printWriter = null;
		String fileName = null; 
		Map map = new HashMap();
		try {
			if (!CollectionUtils.isEmpty(errorMap)) {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("错误讯息:" + IAtomsConstants.MARK_ENTER);
				Set<String> keySet = errorMap.keySet();
				Iterator<String> iterator = keySet.iterator();
				String key = null;
				List<String> errorList = null;
				
				while (iterator.hasNext()) {
					key = iterator.next();
					errorList =  errorMap.get(key);
					if (!CollectionUtils.isEmpty(errorList)) {
						for (String errorMsg : errorList) {
							stringBuffer.append(errorMsg + IAtomsConstants.MARK_ENTER);
						}
					}
				}
				//錯誤信息文件名
				fileName = UUID.randomUUID().toString() + IAtomsConstants.MARK_NO + IAtomsConstants.PARAM_FILE_SUFFIX_TXT;
				//文件路徑
				//File filePath = new File(AssetInInfoFormDTO.TEMP_ERROR_FILE_PATH);//exists()
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				String errorFilePath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH)
						+ File.separator + yearMonthDay + File.separator + IAtomsConstants.UC_NO_DMM_03040 + File.separator + IAtomsConstants.PARAM_STRING_IMPORT;
				File filePath = new File(errorFilePath);
				if (!filePath.exists() || !filePath.isDirectory()) {
					filePath.mkdirs();
				}
				File saveFile = new File(filePath, fileName);
				printWriter = new PrintWriter(saveFile , "UTF-8");
				printWriter.print(stringBuffer.toString());
				
				map.put(AssetInInfoFormDTO.ERROR_FILE_PATH, errorFilePath);
				map.put(AssetInInfoFormDTO.ERROR_FILE_NAME, fileName);
			}
		} catch (Exception e) {
			LOGGER.error("saveEorrorMsg()", "Service Exception", e);
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
		return map;
	}

	/**
	 * Purpose:核檢匯入文件的內容
	 * @author CarrieDuan
	 * @param uploadFiled ：匯入的文件
	 * @param errorMap ：記錄錯誤信息
	 * @return List<DmmAssetInList> ：返回設備明細清單
	 */
	private List<DmmAssetInList> checkUploadFiled(MultipartFile uploadFiled,Map<String, List<String>> errorMap) {
		List<DmmAssetInList> list = new ArrayList<DmmAssetInList>();
		DmmAssetInList assetInList = null;
		try {
			if (uploadFiled != null) {
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
					LOGGER.error("workbook is null >>> ");
					throw new ServiceException();
				}
				//設備序號
				String serialNumber = null;
				//財產編號
				String propertyId = null;
				//記錄設備序號重複的行號
				//List<Integer> flagArrs = new ArrayList<Integer>();
				//標記財產編號重複的行號
				//List<Integer> propertyIdRepeats = new ArrayList<Integer>();
				//記錄為重複的DTID
				Map<String, Integer> propertyIdRepeatMap = new HashMap<String, Integer>();
				//記錄設備序號重複的行號
				Map<String, Integer> serialNumberRepeatMap = new HashMap<String, Integer>();
				// 獲取行數
				rowCount = this.getExcelRealRowCount(sheet);
				if (rowCount <= 1) {
					errorList = new ArrayList<String>();
					errorList.add(IAtomsMessageCode.IATOMS_MSG_NONE_DATA);
					errorMap.put(String.valueOf(0), errorList);
				} else if (rowCount > 501) {
					errorList = new ArrayList<String>();
					errorList.add(IAtomsMessageCode.ABOUCHEMENT_FAILURE_FOR_OVER_LIMIT);
					errorMap.put(String.valueOf(0), errorList);
				}else {
					AssetInListDTO assetInInfoDTO = null;
					//標記設備序號是否錯誤
					boolean isSerial = false;
					//標記財產編號是否錯誤
					boolean isPropertyId = false;
					//獲取明細檔的所有設備序號以及財產編號
					List<AssetInListDTO> assetInListDTOs = this.assetInListDAO.getAssetInListDTOs();
					//獲取庫存主檔的所有設備序號以及財產編號
					List<DmmRepositoryDTO> repositoryDTOs = this.repositoryDAO.getDmmRepositoryDTOs();
					boolean isAssetInListRepeat = false;
					boolean isRepositoryRepeat = false;
					for (int i = 1; i <= rowCount; i++) {
						isSerial = false;
						isPropertyId = false;
						errorList = new ArrayList<String>();
						//獲取行
						row = sheet.getRow(i);
						if (isRowEmpty(row)) continue;
						//获取设备編號
						serialNumber = this.getCellFormatValue(row.getCell(0), null, Boolean.TRUE);
						//获取财产编号
						propertyId = this.getCellFormatValue(row.getCell(1), null, Boolean.TRUE);
						//核檢設備序號是否為空
						if (!StringUtils.hasText(serialNumber)) {
							errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i+1))}, null) 
									+ i18NUtil.getName(IAtomsMessageCode.PARAM_VALUE_IS_EMPTY, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_SERIAL_NUMBER)}, null));
						} else {
							//驗證設備序號格式是否正確
							if (!ValidateUtils.numberOrEnglish(serialNumber)) {
								errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null) 
										+ i18NUtil.getName(IAtomsMessageCode.INPUT_LIMIT_ENGLISH_AND_NUMBER, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_SERIAL_NUMBER)}, null));
								isSerial = true;
							}
							//驗證設備序號長度是否符合標準
							if (!ValidateUtils.length(serialNumber, 1, 20)) {
								errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null) 
										+ i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_SERIAL_NUMBER)
										+ i18NUtil.getName(IAtomsMessageCode.IMPORT_LENGTH_LIMIT, new String[]{IAtomsConstants.PROPERTY_ID_LENGTH}, null));
								isPropertyId = true;
							}
							if (serialNumberRepeatMap.containsKey(serialNumber)) {
								errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_ROW, new String[]{String.valueOf((i+1)), serialNumberRepeatMap.get(serialNumber).toString()}, null) + 
										i18NUtil.getName(IAtomsMessageCode.SERIAL_NUMBER_REPETITION));
								errorMap.put(String.valueOf(i), errorList);
								continue;
							} else {
								serialNumberRepeatMap.put(serialNumber, i+1);
							}
							//核檢設備序號是否與數據庫重複
							if (!isSerial) {
								//核檢設備序號與設備入庫資料明細檔是否重複
								isAssetInListRepeat = this.repeatInAssetInListDTO(assetInListDTOs, serialNumber, null);
								//核檢設備序號與庫存主檔是否重複
								isRepositoryRepeat = this.repeatInDmmRepositoryDTO(repositoryDTOs, serialNumber, null);
								if (isAssetInListRepeat || isRepositoryRepeat) {
									errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i + 1))}, null) 
											+ i18NUtil.getName(IAtomsMessageCode.INPUT_VALUE_REPEAT, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_SERIAL_NUMBER)}, null));
								} 
							}
							//如果財產編號不為空
							if (StringUtils.hasText(propertyId)) {
								if (propertyIdRepeatMap.containsKey(propertyId)) {
									errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_ROW, new String[]{String.valueOf((i+1)), propertyIdRepeatMap.get(propertyId).toString()}, null) + 
											i18NUtil.getName(IAtomsMessageCode.PROPERTY_ID_REPETITION));
									errorMap.put(String.valueOf(i), errorList);
									continue;
								} else {
									propertyIdRepeatMap.put(propertyId, i+1);
								}
								//驗證財產編號格式是否正確
								if (!ValidateUtils.numberOrEnglish(propertyId)) {
									errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null) 
											+ i18NUtil.getName(IAtomsMessageCode.INPUT_LIMIT_ENGLISH_AND_NUMBER, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_PROPERTY_ID)}, null));
									isPropertyId = true;
								}
								//驗證財產編號長度是否符合標準
								if (!ValidateUtils.length(propertyId, 1, 20)) {
									errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null) 
											+ i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_PROPERTY_ID)
											+ i18NUtil.getName(IAtomsMessageCode.IMPORT_LENGTH_LIMIT, new String[]{IAtomsConstants.PROPERTY_ID_LENGTH}, null));
									isPropertyId = true;
								}
								//核檢財產編號是否與數據庫重複
								if (!isPropertyId) {
									//核檢財產編號與設備入庫資料明細檔是否重複
									isAssetInListRepeat = this.repeatInAssetInListDTO(assetInListDTOs, null, propertyId);
									//核檢財產編號與庫存主檔是否重複
									isRepositoryRepeat = this.repeatInDmmRepositoryDTO(repositoryDTOs, null, propertyId);
									if (isAssetInListRepeat || isRepositoryRepeat) {
										errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i + 1))}, null) 
												+ i18NUtil.getName(IAtomsMessageCode.INPUT_VALUE_REPEAT, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_PROPERTY_ID)}, null));
									}
								}
							}
							//核檢設備序號、財產編號是否重複
							/*if (!flagArrs.contains(i) || !propertyIdRepeats.contains(i)) {
								for (int j = i + 1; j <= rowCount; j++) {
									//獲取行
									Row rowRepeat = sheet.getRow(j);
									if (isRowEmpty(rowRepeat)) continue;
									if (!propertyIdRepeats.contains(j)) {
										//獲取財產編號
										String propertyIdRepeat = this.getCellFormatValue(rowRepeat.getCell(1), null);
										if ((StringUtils.hasText(propertyIdRepeat) && StringUtils.hasText(propertyId)) && propertyId.equals(propertyIdRepeat)) {
											propertyIdRepeats.add(j);
											errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_ROW, new String[]{String.valueOf((i+1)), String.valueOf((j+1))}, null) 
													+ i18NUtil.getName(IAtomsMessageCode.PROPERTY_ID_REPETITION));
										}
									}
									if (!flagArrs.contains(j)) {
										//获取设备編號
										String serialNumberRepeat = this.getCellFormatValue(rowRepeat.getCell(0), null);
										if (serialNumber.equals(serialNumberRepeat)) {
											//記錄重複行的設備序號
											flagArrs.add(j);
											//將錯誤信息放到錯誤信息集合中，用於下載錯誤信息。
											errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_ROW, new String[]{String.valueOf((i+1)), String.valueOf((j+1))}, null) + 
													i18NUtil.getName(IAtomsMessageCode.SERIAL_NUMBER_REPETITION));
										}
									}
								}
							}*/
						}
						if (CollectionUtils.isEmpty(errorList)) {
							assetInList = new DmmAssetInList();
							assetInList.setSerialNumber(serialNumber);
							assetInList.setPropertyId(propertyId);
							list.add(assetInList);
						} else {
							errorMap.put(String.valueOf(i), errorList);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("checkUploadFiled()", "Service Exception", e);
		}
		return list;
	}
	
	/**
	 * Purpose:
	 * @author CarrieDuan
	 * @param assetInListDTOs : 
	 * @param serialNumber
	 * @param propertyId
	 * @return boolean
	 */
	public boolean repeatInAssetInListDTO(List<AssetInListDTO> assetInListDTOs, String serialNumber, String propertyId) {
		try {
			if (!CollectionUtils.isEmpty(assetInListDTOs)) {
				for (AssetInListDTO assetInListDTO : assetInListDTOs) {
					if (StringUtils.hasText(serialNumber)) {
						if (serialNumber.equals(assetInListDTO.getSerialNumber())) {
							return true;
						}
					} else {
						if (StringUtils.hasText(propertyId)) {
							if (propertyId.equals(assetInListDTO.getPropertyId())) {
								return true;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("repeatInAssetInListDTO()", "Service Exception", e);
		}
		return false;
	}
	
	
	public boolean repeatInDmmRepositoryDTO(List<DmmRepositoryDTO> dmmRepositoryDTOs, String serialNumber, String propertyId) {
		try {
			if (!CollectionUtils.isEmpty(dmmRepositoryDTOs)) {
				for (DmmRepositoryDTO dmmRepositoryDTO : dmmRepositoryDTOs) {
					if (StringUtils.hasText(serialNumber)) {
						if (serialNumber.equals(dmmRepositoryDTO.getSerialNumber())) {
							return true;
						}
					} else {
						if (StringUtils.hasText(propertyId)) {
							if (propertyId.equals(dmmRepositoryDTO.getPropertyId())) {
								return true;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("repeatInDmmRepositoryDTO()", "Service Exception", e);
		}
		return false;
	}
	
	/**
	 * Purpose:獲取excel表格真實行數
	 * @author CrissZhang
	 * @param sheet ： 傳入參數sheet
	 * @return int : 返回行數
	 */
	private int getExcelRealRowCount(Sheet sheet) {
		int rowCount = 0;
		if(sheet != null){
			int beginRow = sheet.getFirstRowNum();  
		    int endRow = sheet.getLastRowNum();  
		    int beginCell = 0;
		    int endCell = 0;
		    Row tempRow = null;
		    Boolean emptyRow = false;
		    for (int i = beginRow; i <= endRow; i++) {  
		    	tempRow = sheet.getRow(i);
		    	emptyRow = false;
		    	if(tempRow != null){
		    		beginCell = tempRow.getFirstCellNum();
			    	endCell = tempRow.getLastCellNum();
			    	for(int j = beginCell; j <= endCell; j++){
				        if (!StringUtils.hasText(this.getCellFormatValue(tempRow.getCell(j), null, Boolean.FALSE))) {  
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
	 * Purpose:判斷Row是否為空
	 * @author CrissZhang
	 * @return Boolean
	 */
	private Boolean isRowEmpty(Row row){
		Boolean flag = true;
		if(row != null){
		  int beginCell = row.getFirstCellNum();
		  int endCell = row.getLastCellNum();
		  for(int i = beginCell; i <= endCell; i++){
		        if (!StringUtils.hasText(this.getCellFormatValue(row.getCell(i), null, Boolean.FALSE))) {  
		            continue;  
		        } else {
		        	flag = false;
		        	break;
		        }
	    	}
		} 
		return flag;
	}
	
	/**
	 * Purpose:依據Cell類型獲取數據
	 * @param cell:取得Excel cell單元值
	 * @param getTime 日期格式
	 * @return String:单元格数据内容
	 */
	private String getCellFormatValue(Cell cell, String getTime, Boolean isString) {
		String cellvalue = "";
		if (cell != null) {
			if (isString) {
	    		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    	}
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
				// 如果当前Cell的Type为NUMERIC
				case Cell.CELL_TYPE_NUMERIC:
				case Cell.CELL_TYPE_FORMULA: {
					// 判断当前的cell是否为Date
					if (DateUtil.isCellDateFormatted(cell)) {
						SimpleDateFormat sdf = null;
						Date date = cell.getDateCellValue();
						if (StringUtils.hasText(getTime)) {
							sdf = new SimpleDateFormat(getTime);
						} else {
							sdf = new SimpleDateFormat("yyyy-MM-dd");
						}
						cellvalue = sdf.format(date);
					}else {
						// 取得当前Cell的数值
						cellvalue = String.valueOf((((Double)cell.getNumericCellValue())));
						if (StringUtils.hasText(cellvalue)) {
							String[] cellValues = cellvalue.split("\\.");
							if ("0".equals(cellValues[1])) {
								cellvalue = cellValues[0];
							}
						}
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
		return cellvalue.trim();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetInInfoService#saveHtml(cafe.core.context.SessionContext)
	 */
/*	public SessionContext savaHtml(SessionContext sessionContext) throws ServiceException {
		try {
			AssetInInfoFormDTO formDTO = (AssetInInfoFormDTO) sessionContext.getRequestParameter();
			
			AssetInInfoDTO assetInInfoDTO = formDTO.getAssetInInfoDTO();
			if (assetInInfoDTO != null) {
				final String html = "ListAssetInInfo.html";
				final String path = "/com/cybersoft4u/xian/iatoms/templete/mail/";
				final String  textTemplate = path + html;
				final Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("vendorId", assetInInfoDTO.getCompanyId());
				MimeMessagePreparator preparator = new MimeMessagePreparator() {
					public void prepare(MimeMessage mimeMessage)
							throws MessagingException {
							String text = VelocityUtil.merge(
									findTemplate(textTemplate,
											"utf-8"),
											variables,"utf-8");
							System.out.println(text);
					}
							
					};
					InputStream inputStream = new FileInputStream(new File("tempErrorFile"));
					MimeMessage mimeMessage = new MimeMessage(null,inputStream);
					preparator.prepare(mimeMessage);
			}
			
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".upload() Error -->" + e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		return sessionContext;
	}*/

	/**
	 * Purpose:读取mail内容
	 * @param key:模板地址
	 * @param charset:编码方式
	 * @return String
	 */
	/*private String findTemplate(String key, String charset) {
		try {
			// look up classpath
			InputStream inStream = this.getClass().getResourceAsStream(key);
			if (inStream == null) {
				throw new IllegalStateException("resouce can not be found - "
						+ key);
			}
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}
			return new String(outStream.toByteArray(), charset);
		} catch (Exception e) {
			LOGGER.error(e, e);
			throw new IllegalStateException(e);
		}
	}*/

	/**
	 * @return the assetInInfoDAO
	 */
	public IAssetInInfoDAO getAssetInInfoDAO() {
		return assetInInfoDAO;
	}

	/**
	 * @param assetInInfoDAO the assetInInfoDAO to set
	 */
	public void setAssetInInfoDAO(IAssetInInfoDAO assetInInfoDAO) {
		this.assetInInfoDAO = assetInInfoDAO;
	}

	/**
	 * @return the assetInListDAO
	 */
	public IAssetInListDAO getAssetInListDAO() {
		return assetInListDAO;
	}

	/**
	 * @param assetInListDAO the assetInListDAO to set
	 */
	public void setAssetInListDAO(IAssetInListDAO assetInListDAO) {
		this.assetInListDAO = assetInListDAO;
	}

	/**
	 * @return the wareHouseDAO
	 */
	public IWarehouseDAO getWareHouseDAO() {
		return wareHouseDAO;
	}

	/**
	 * @param wareHouseDAO the wareHouseDAO to set
	 */
	public void setWareHouseDAO(IWarehouseDAO wareHouseDAO) {
		this.wareHouseDAO = wareHouseDAO;
	}

	/**
	 * @return the contractAssetDAO
	 */
	public IContractAssetDAO getContractAssetDAO() {
		return contractAssetDAO;
	}

	/**
	 * @param contractAssetDAO the contractAssetDAO to set
	 */
	public void setContractAssetDAO(IContractAssetDAO contractAssetDAO) {
		contractAssetDAO = contractAssetDAO;
	}

	
	
	/**
	 * @return the repositoryDAO
	 */
	public IDmmRepositoryDAO getRepositoryDAO() {
		return repositoryDAO;
	}

	/**
	 * @param repositoryDAO the repositoryDAO to set
	 */
	public void setRepositoryDAO(IDmmRepositoryDAO repositoryDAO) {
		this.repositoryDAO = repositoryDAO;
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
	 * @return the contractDAO
	 */
	public IContractDAO getContractDAO() {
		return contractDAO;
	}

	/**
	 * @param contractDAO the contractDAO to set
	 */
	public void setContractDAO(IContractDAO contractDAO) {
		this.contractDAO = contractDAO;
	}

	/**
	 * @return the assetTypeDAO
	 */
	public IAssetTypeDAO getAssetTypeDAO() {
		return assetTypeDAO;
	}

	/**
	 * @param assetTypeDAO the assetTypeDAO to set
	 */
	public void setAssetTypeDAO(IAssetTypeDAO assetTypeDAO) {
		this.assetTypeDAO = assetTypeDAO;
	}

	/**
	 * @return the checkSettingList
	 */
	public List<CheckSettingDTO> getCheckSettingList() {
		return checkSettingList;
	}

	/**
	 * @param checkSettingList the checkSettingList to set
	 */
	public void setCheckSettingList(List<CheckSettingDTO> checkSettingList) {
		this.checkSettingList = checkSettingList;
	}
	
	
}
