package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.CheckSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTransInfoFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTransInfoHistoryFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CheckTransInfoFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PropertyNumberImportFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.services.IAssetTransferService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransListDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
import com.cybersoft4u.xian.iatoms.services.dmo.BimWarehouse;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetTransInfo;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetTransList;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepository;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryHistory;

/**
 * Purpose: 設備轉倉作業Service
 * @author Amanda Wang
 * @since  JDK 1.6
 * @date   2016/7/12
 * @MaintenancePersonnel Amanda Wang
 */
public class AssetTransferService extends AtomicService implements IAssetTransferService{

	/**
	 * 系统日志文件控件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, AssetTransferService.class);
	
	/**
	 * 設備轉倉主檔DAO
	 */
	private IDmmAssetTransInfoDAO assetTransInfoDAO;
	
	/**
	 * 設備轉倉明細DAO
	 */
	private IDmmAssetTransListDAO assetTransListDAO;
	/**
	 * 庫存主檔DAO
	 */
	private IDmmRepositoryDAO repositoryDAO;
	
	/**
	 * 發送mail組件
	 */
	private MailComponent mailComponent;

	/**
	 * admUserDAO
	 */
	private IAdmUserDAO admUserDAO;
	
	/**
	 * 倉庫DAO
	 */
	private IWarehouseDAO warehouseDAO;
	
	/**
	 * 
	 */
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		// 页面显示信息
		Message msg = null;
		try {
			AssetTransInfoFormDTO formDTO =  (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
			String size = SystemConfigManager.getProperty(IAtomsConstants.SETTING, IAtomsConstants.UPLOAD_FILE_SIZE);
			if (!StringUtils.hasText(size)) {
				size = IAtomsConstants.PARAM_UPLOAD_FILE_SIZE;
			}
			int rate = Integer.valueOf(IAtomsConstants.PARAM_UPLOAD_CONVERSION_RATE);
			size = String.valueOf(Integer.valueOf(size) * rate);
			formDTO.setUploadFileSize(size);
			sessionContext.setResponseResult(formDTO);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOGGER.error("init(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#initCheck(cafe.core.context.SessionContext)
	 */
	public SessionContext initCheck(SessionContext sessionContext) throws ServiceException {
		try{
			AssetTransInfoFormDTO formDTO = null;
			if(sessionContext != null){
				formDTO = (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS));
				sessionContext.setResponseResult(formDTO);
			}
		}catch(Exception e){
			LOGGER.error("initCheck()", "is error: ", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#initHist(cafe.core.context.SessionContext)
	 */
	public SessionContext initHist(SessionContext sessionContext) throws ServiceException {
		try{
			AssetTransInfoFormDTO formDTO = null;
			if(sessionContext != null){
				formDTO = (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS));
				sessionContext.setResponseResult(formDTO);
			}
		}catch(Exception e){
			LOGGER.error("initHist()", "is error: ", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#getAssetTransIdList(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getAssetTransIdList(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		List<Parameter> assetTransIdList = null;
		try {
			String userId = (String) inquiryContext.getParameter(IAtomsLogonUser.FIELD_ID);
			String tabType = (String) inquiryContext.getParameter(AssetTransInfoFormDTO.TAB_TYPE);
			if(StringUtils.hasText(userId)){
					AdmUser admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, userId);
				if (IAtomsConstants.NO.equals(admUser.getDataAcl())) {
					userId = "";
				}
				if (AssetTransInfoFormDTO.TAB_ASSET_TRANFER_OUT.equals(tabType)) {
					assetTransIdList = assetTransInfoDAO.listBy(userId);
				} else if (AssetTransInfoFormDTO.TAB_ASSET_TRANFER_IN_CHECK.equals(tabType)) {
					assetTransIdList = assetTransInfoDAO.listToWarehouseBy(userId);
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("getAssetTransInfoList()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getAssetTransInfoList():", "error", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return assetTransIdList;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#save(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		AssetTransInfoFormDTO assetTransInfoFormDTO = null;
		DmmAssetTransInfo dmmAssetTransInfo = null;
		// 轉倉批號
		String assetTransInfoId = null;
		try{
			Map map = new HashMap();
			assetTransInfoFormDTO = (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
			//獲取轉倉退回標記
			DmmAssetTransInfoDTO assetTransInfoDTO = assetTransInfoFormDTO.getAssetTransInfoDTO();
			Transformer transformer = new SimpleDtoDmoTransformer();
			//判斷assetTransInfoDTO是否為空
			if(assetTransInfoDTO != null){
				//獲取轉倉批號
				assetTransInfoId = assetTransInfoDTO.getAssetTransId();
				//獲取登錄信息
				LogonUser logonUser = assetTransInfoFormDTO.getLogonUser();
				String userId = null; 
				String userName = null;
				if (logonUser != null) {
					//得到當前登入者的Id和Name
					userId = logonUser.getId();
					userName = logonUser.getName();
				} else {
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.LIMITED_LOGON_ID));
					return sessionContext;
				}
				if (!StringUtils.hasText(assetTransInfoId)) {
					//新增轉倉批號信息
					dmmAssetTransInfo = (DmmAssetTransInfo) transformer.transform(assetTransInfoDTO, new DmmAssetTransInfo());
					//設置轉倉批號
					String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD);
					//流水號
					long id = this.getSequenceNumberControlDAO().getSeqNo(IAtomsConstants.IATOMS_TB_NAME_ASSET_TRANS_INFO, yearMonthDay);
					if (id == 0) {
						id++;
					}
					//格式化流水號
					String swiftNumber = StringUtils.toFixString(4, id);
					StringBuffer transInfoId = new StringBuffer();
					//生成轉倉批號
					transInfoId.append(IAtomsConstants.PARAM_ASSET_TRANS_ID_TW).append(yearMonthDay).append(swiftNumber);
					if (StringUtils.hasText(transInfoId)) {
						assetTransInfoId = transInfoId.toString();
					}
					dmmAssetTransInfo.setAssetTransId(assetTransInfoId);
					//是否轉倉
					dmmAssetTransInfo.setIsListDone(IAtomsConstants.NO);
					//是否被退回
					dmmAssetTransInfo.setIsBack(IAtomsConstants.NO);
					dmmAssetTransInfo.setIsTransDone(IAtomsConstants.NO);
					dmmAssetTransInfo.setIsCancel(IAtomsConstants.NO);
					//設置創建信息
					dmmAssetTransInfo.setCreatedById(userId);
					dmmAssetTransInfo.setCreatedByName(userName);
					dmmAssetTransInfo.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					dmmAssetTransInfo.setUpdatedById(userId);
					dmmAssetTransInfo.setUpdatedByName(userName);
					dmmAssetTransInfo.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROW, assetTransInfoId);
					//將新增產生的轉倉批號放到map中
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_SUCCESS, new String[]{this.getMyName()});
				}else{
					//得到已經存儲的轉倉信息
					dmmAssetTransInfo = (DmmAssetTransInfo) this.assetTransInfoDAO.findByPrimaryKey(DmmAssetTransInfo.class, assetTransInfoId);
					dmmAssetTransInfo.setToWarehouseId(assetTransInfoDTO.getToWarehouseId());
					dmmAssetTransInfo.setComment(assetTransInfoDTO.getComment());
					//設置更新信息
					dmmAssetTransInfo.setUpdatedById(userId);
					dmmAssetTransInfo.setUpdatedByName(userName);
					dmmAssetTransInfo.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_SUCCESS, new String[]{this.getMyName()});
				}
				
				this.assetTransInfoDAO.getDaoSupport().saveOrUpdate(dmmAssetTransInfo);
				this.assetTransInfoDAO.getDaoSupport().flush();
			}
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(true));
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(), null));
			//把map放入session
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("saveAssetTransId()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("saveAssetTransId()", "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#getAssetInfoList(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getAssetInfoList(MultiParameterInquiryContext inquiryContext) throws ServiceException{
		List<Parameter> assetInfoList = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		String userId = (String) inquiryContext.getParameter(IAtomsLogonUser.FIELD_ID);
		String queryFromDateStart = (String) inquiryContext.getParameter(AssetTransInfoHistoryFormDTO.QUERY_ASSET_FROM_DATE_START);
		String queryFromDateEnd = (String) inquiryContext.getParameter(AssetTransInfoHistoryFormDTO.QUERY_ASSET_FROM_DATE_END);
		String queryToDateStart = (String) inquiryContext.getParameter(AssetTransInfoHistoryFormDTO.QUERY_ASSET_TO_DATE_START);
		String queryToDateEnd = (String) inquiryContext.getParameter(AssetTransInfoHistoryFormDTO.QUERY_ASSET_TO_DATE_END);
		String queryFromWarehouseId = (String) inquiryContext.getParameter(AssetTransInfoHistoryFormDTO.QUERY_ASSET_FROM_WARE);
		String queryToWarehouseId = (String) inquiryContext.getParameter(AssetTransInfoHistoryFormDTO.QUERY_ASSET_TO_WARE);
		Timestamp transOutStart = null;
		Timestamp transOutEnd = null;
		Timestamp transInStart = null;
		Timestamp transInEnd = null;
		try {
			if (StringUtils.hasText(queryFromDateStart)) {
				transOutStart = DateTimeUtils.toTimestamp(queryFromDateStart);
			}
			if (StringUtils.hasText(queryFromDateEnd)) {
				Calendar calendar = new GregorianCalendar(); 
				calendar.setTime(DateTimeUtils.toDate(queryFromDateEnd)); 
				calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动 
				transOutEnd = DateTimeUtils.toTimestamp(sf.format(calendar.getTime()));
			}
			if (StringUtils.hasText(queryToDateStart)) {
				transInStart = DateTimeUtils.toTimestamp(queryToDateStart);
			}
			if (StringUtils.hasText(queryToDateEnd)) {
				Calendar calendar = new GregorianCalendar(); 
				calendar.setTime(DateTimeUtils.toDate(queryToDateEnd)); 
				calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动 
				transInEnd = DateTimeUtils.toTimestamp(sf.format(calendar.getTime()));
			}
			assetInfoList = this.assetTransInfoDAO.getAssetInfoList(userId, transOutStart, transOutEnd, transInStart, transInEnd, queryFromWarehouseId, queryToWarehouseId);
		}	catch (DataAccessException e) {
			LOGGER.error("getAssetTransIdList()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("getAssetTransIdList()", "is failed", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
		return assetInfoList;
		
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#saveTranferCheck(cafe.core.context.SessionContext)
	 */
	public SessionContext saveTranferCheck(SessionContext sessionContext) throws ServiceException {
		try{
			AssetTransInfoFormDTO formDTO = null; 
			IAtomsLogonUser logonUser = null;
			Message msg = new Message();
			String serialNumber = null;
			//設備歷史
			DmmRepositoryHistory repositoryHist = null;
			//庫存信息
			DmmRepositoryDTO repositoryDTO = null;
			DmmRepository repository = null;
			if(sessionContext != null){
				formDTO = (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
				if(formDTO != null){
					logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
					//轉倉批號
					String queryAssetTransId = formDTO.getQueryAssetTransId();
					//入庫/退回標記
					String option = formDTO.getOption();
					if(StringUtils.hasText(queryAssetTransId)){
						//根據轉倉批號查詢該批號信息
						DmmAssetTransInfo assetTransInfo = (DmmAssetTransInfo) this.assetTransInfoDAO.findByPrimaryKey(DmmAssetTransInfo.class, queryAssetTransId);
						if(assetTransInfo != null){
							if (IAtomsConstants.YES.equals(assetTransInfo.getIsBack()) || IAtomsConstants.YES.equals(assetTransInfo.getIsTransDone())) {
								if (IAtomsConstants.YES.equals(assetTransInfo.getIsBack())) {
									msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_ALERDAY_UPDATE);
								}
								if (IAtomsConstants.YES.equals(assetTransInfo.getIsTransDone())) {
									msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_ALERDAY_UPDATE);
								}
							} else {
								//如果為轉倉退回，則修改轉倉記錄。
								if (DmmAssetTransInfoDTO.ATTRIBUTE.IS_BACK.getValue().equals(option)) {
									String toMailId = assetTransInfo.getTransOutUserId();
									//則修改isBack為Y,isListDone為N
									assetTransInfo.setIsBack(IAtomsConstants.YES);//退回操作，發送郵件
									assetTransInfo.setAcceptanceDate(DateTimeUtils.getCurrentTimestamp());
									assetTransInfo.setAcceptanceUserId(logonUser.getId());
									assetTransInfo.setAcceptanceUserName(logonUser.getName());
									this.sendMail(toMailId, assetTransInfo.getAssetTransId(), formDTO.getRefuseReason());
									msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.TRANS_BACK_SUCCESS, new String[]{queryAssetTransId});
								} else {
									//核檢轉入倉是否被刪除
									String toWareHouseId = assetTransInfo.getToWarehouseId();
									if (StringUtils.hasText(toWareHouseId)) {
										BimWarehouse bimWarehouse = this.warehouseDAO.findByPrimaryKey(BimWarehouse.class, toWareHouseId);
										if (bimWarehouse != null) {
											if (IAtomsConstants.YES.equals(bimWarehouse.getDeleted())) {
												msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.TRANS_DELETED_TO_WAREHOUSE);
											} else {
												// #3405 轉倉驗收 提供整批驗收機制
												if(StringUtils.hasText(formDTO.getIsSelectedAll())
														&& IAtomsConstants.LEAVE_CASE_STATUS_ONE.equals(formDTO.getIsSelectedAll())){
													//為確認轉倉，則調用存儲過程進行保存更新操作
													this.repositoryDAO.confirmStorageAll(queryAssetTransId, null, IAtomsConstants.YES,
															this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY),
															logonUser, IAtomsConstants.PARAM_ACTION_TRANSFER_STORAGE,
															IAtomsConstants.PARAM_TRANS_STATUS_TRANSFER_SUCCESS);
												}else{
													//CR #2703 確認入庫 調用存儲過程 2017/11/10
													String[] serialNumberArr = formDTO.getSerialNumbers().split(",");
													String serialNumbers = null;
													int size = serialNumberArr.length;
													int i = 0;
													String endFlag = IAtomsConstants.PARAM_NO;
													if (size>300) {
														for (String number : serialNumberArr) {
															if ((i%300 == 1 || i == 0 ) && i!=1) {
																serialNumbers = number;
															} else if (i < size) {
																serialNumbers +=  IAtomsConstants.MARK_SEPARATOR + number;
															}
															if (i == size-1) {
																endFlag = IAtomsConstants.PARAM_YES;
															}
															if ((i%300 == 0 || i == size-1) && i>1) {
																//為確認轉倉，則調用存儲過程進行保存更新操作
																this.repositoryDAO.confirmInStorage(queryAssetTransId, this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY), 
																											logonUser, IAtomsConstants.PARAM_ACTION_TRANSFER_STORAGE, serialNumbers, 
																											IAtomsConstants.PARAM_TRANS_STATUS_TRANSFER_SUCCESS, IAtomsConstants.PARAM_TRANS_STATUS_CANCEL_SUCCESS, 
																											endFlag, IAtomsConstants.PARAM_ACTION_CANCEL_TRANSFER);
																serialNumbers = IAtomsConstants.MARK_EMPTY_STRING;
															}
															i++;
														}
													} else {
														if (size<2) {
															serialNumbers = serialNumberArr[0] + IAtomsConstants.MARK_SEPARATOR;
														} else {
															for (String number : serialNumberArr) {
																if (i == 0) {
																	serialNumbers = number;
																} else if (i < size) {
																	serialNumbers +=  IAtomsConstants.MARK_SEPARATOR + number;
																}
																i++;
															}
														}
														//為確認轉倉，則調用存儲過程進行保存更新操作
														this.repositoryDAO.confirmInStorage(queryAssetTransId, this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY),
																										logonUser, IAtomsConstants.PARAM_ACTION_TRANSFER_STORAGE, serialNumbers, 
																										IAtomsConstants.PARAM_TRANS_STATUS_TRANSFER_SUCCESS, IAtomsConstants.PARAM_TRANS_STATUS_CANCEL_SUCCESS,
																										IAtomsConstants.PARAM_YES, IAtomsConstants.PARAM_ACTION_CANCEL_TRANSFER);
													}

												}
												msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.TRANS_SUCCESS, new String[]{queryAssetTransId});
											}
										}
									}
								}
							}
						} else {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.TRANS_FALURE, new String[]{queryAssetTransId});
						}
					}
					this.assetTransInfoDAO.getDaoSupport().flush();
				}
				sessionContext.setReturnMessage(msg);
				sessionContext.setResponseResult(formDTO);
			}
		} catch (DataAccessException e) {
			LOGGER.error("saveTranferCheck()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		} catch(Exception e){
			LOGGER.error("saveTranferCheck()", "is error: ", e);
			throw new ServiceException(IAtomsMessageCode.TRANS_FALURE, new String[]{this.getMyName()});
		}
		return sessionContext;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#query(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		//轉倉資料集合
		List<DmmAssetTransListDTO> assetTransListDTOs = null;
		
		Message msg = null;
		try {
			AssetTransInfoFormDTO assetTransInfoFormDTO = (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
			//轉倉批號
			String assetTransId = assetTransInfoFormDTO.getAssetTransId();
			//排序列
			String order = assetTransInfoFormDTO.getOrder();
			//排序
			String sort = assetTransInfoFormDTO.getSort();
			//頁碼
			Integer page = assetTransInfoFormDTO.getPage();
			//一頁顯示多少行
			Integer rows = assetTransInfoFormDTO.getRows();
			//轉倉資料總條數
			Integer count = null;
			//CR #2703 
			Boolean isShowStatus = false;
			Boolean isQueryHistory = false;
			//是否查詢歷史
			Boolean isHistory = assetTransInfoFormDTO.getIsHistory();
			if (isHistory == null) {
				isHistory = Boolean.FALSE;
			}
			if (assetTransInfoFormDTO.getShowStatusFlag() != null && assetTransInfoFormDTO.getShowStatusFlag()) {
				isQueryHistory = true;
			}
			if (StringUtils.hasText(assetTransId)) {
				count = this.getAssetTransInfoDAO().getCount(assetTransId, isHistory);
				if (count > 0) {
					if (isHistory) {
						isShowStatus = true;
					}
					//查詢轉倉清單集合
					assetTransListDTOs = this.assetTransInfoDAO.listBy(assetTransId, isHistory, order, sort, page, rows, isQueryHistory, isShowStatus);
					if (!CollectionUtils.isEmpty(assetTransListDTOs)) {
						for(DmmAssetTransListDTO assetTransListDTO : assetTransListDTOs){
							String updateByName = assetTransListDTO.getUpdatedByName();
							if(StringUtils.hasText(updateByName) && updateByName.indexOf("(") >0 && updateByName.indexOf(")") > 0){
								assetTransListDTO.setUpdatedByName(updateByName.substring(updateByName.indexOf("(") + 1, updateByName.indexOf(")")));
							}
						}
						//設置返回總條數
						assetTransInfoFormDTO.getPageNavigation().setRowCount(count);
						assetTransInfoFormDTO.setList(assetTransListDTOs);
					} 
				}
			}
			//設置返回結果
			sessionContext.setResponseResult(assetTransInfoFormDTO);
			sessionContext.setReturnMessage(msg);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error("query()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("query()", "Exception--->", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE,e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStransInfoService#delete(cafe.core.context.SessionContext)
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			AssetTransInfoFormDTO assetTransInfoFormDTO = (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
			// 轉倉批號
			String assetTransId = assetTransInfoFormDTO.getAssetTransId();
			//判斷是否有轉倉批號
			if (StringUtils.hasText(assetTransId)) {	
				//設備轉倉主檔
				DmmAssetTransInfo assetTransInfo = (DmmAssetTransInfo) this.getAssetTransInfoDAO().findByPrimaryKey(DmmAssetTransInfo.class, assetTransId);
				if (assetTransInfo != null) {
					//刪除轉倉批號對應轉倉清單的信息
					this.assetTransListDAO.deleteAssetTransListById(null, assetTransId);
					//設置轉倉主當刪除標誌
					this.assetTransInfoDAO.delete(assetTransInfo);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
				}
			}
			//返回消息message
			sessionContext.setReturnMessage(msg);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error("delete()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("delete()", "Exception --->", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#addAssetTransList(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext addAssetTransList(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		//庫存信息
		DmmRepositoryDTO repositoryDTO = null;
		//設備序號
		String serialNumber = null;
		//轉倉批號
		String assetTransId = null;
		try {	
			AssetTransInfoFormDTO assetTransInfoFormDTO = (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
			//獲取登錄信息
			LogonUser logonUser = assetTransInfoFormDTO.getLogonUser();
			String userId = null; 
			String userName = null;
			if (logonUser != null) {
				//得到當前登入者的Id和Name
				userId = logonUser.getId();
				userName = logonUser.getName();
			} else {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.LIMITED_LOGON_ID));
				return sessionContext;
			}
			//得到設備序號
			serialNumber = assetTransInfoFormDTO.getSerialNumber();
			//得到轉倉批號
			assetTransId = assetTransInfoFormDTO.getAssetTransId();
			///根據轉倉批號得到轉倉表相關信息
			DmmAssetTransInfo dmmAssetTransInfo = (DmmAssetTransInfo) this.assetTransInfoDAO.findByPrimaryKey(DmmAssetTransInfo.class, assetTransId);
			//轉出倉
			String fromWarehouseId = null;
			if(dmmAssetTransInfo != null) {
				fromWarehouseId = dmmAssetTransInfo.getFromWarehouseId();
			}
			Map map = new HashMap();
			//檢查轉倉清單中是否重複的轉倉批號
			Boolean hasSerialNumber = this.assetTransListDAO.isCheckHasSerialNumber(serialNumber);
			if (hasSerialNumber) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ASSET_NUMBER_IS_REPETITION);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(), null));
				//把map放入session
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
				return sessionContext;
			}
			//得到庫存信息
			repositoryDTO = this.repositoryDAO.getRepositoryBySerialNumber(serialNumber, fromWarehouseId, null);
			if (repositoryDTO != null) {
				//(Task2555-可轉倉新增已拆回狀態)
				if (!IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY.equals(repositoryDTO.getStatus())
						&& !IAtomsConstants.PARAM_ASSET_STATUS_RETURNED.equals(repositoryDTO.getStatus())
						&& !IAtomsConstants.PARAM_ASSET_STATUS_REPAIR.equals(repositoryDTO.getStatus())) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SERIAL_NUMBER_NO_IN_REPOSITORY);
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(), null));
				} else {
					//設備清單ListDMO
					DmmAssetTransList assetTransList = new DmmAssetTransList();
					String id = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_ASSET_TRANS_LIST);
					assetTransList.setId(id);
					assetTransList.setAssetTransId(assetTransId);
					assetTransList.setSerialNumber(repositoryDTO.getSerialNumber());
					assetTransList.setAssetUser(repositoryDTO.getAssetUser());
					assetTransList.setContractId(repositoryDTO.getContractId());
					assetTransList.setIsCup(repositoryDTO.getIsCup());
					assetTransList.setIsEnabled(repositoryDTO.getIsEnabled());
					//設置創建者信息
					assetTransList.setCreatedById(userId);
					assetTransList.setCreatedByName(userName);
					assetTransList.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					//設置更新者信息
					assetTransList.setUpdatedById(userId);
					assetTransList.setUpdatedByName(userName);
					assetTransList.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					assetTransList.setOldStatus(repositoryDTO.getStatus());
					//保存新增的信息
					this.assetTransInfoDAO.getDaoSupport().saveOrUpdate(assetTransList);
					this.assetTransInfoDAO.getDaoSupport().flush();
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NO_SERIAL_NUMBER_IN_WAREHOUSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(), null));
			}
			//把map放入session
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		} catch (DataAccessException e) {
			LOGGER.error("addAssetTransList()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("addAssetTransList()",  "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}	
		return sessionContext;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#update(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext update(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		// 轉倉清單信息
		List<DmmAssetTransListDTO> assetTransListList = null;
		try {
			AssetTransInfoFormDTO assetTransInfoFormDTO = (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
			//得到轉倉清單列表信息
			assetTransListList = assetTransInfoFormDTO.getList();
			//獲取登錄信息
			LogonUser logonUser = assetTransInfoFormDTO.getLogonUser();
			String userId = null; 
			String userName = null;
			if (logonUser != null) {
				//得到當前登入者的Id和Name
				userId = logonUser.getId();
				userName = logonUser.getName();
			} else {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.LIMITED_LOGON_ID));
				return sessionContext;
			}
			//轉倉清單DMO
			DmmAssetTransList dmmAssetTransList = null;
			//轉倉清單ID
			String assetTransListId = null;
			//轉倉說明
			String comment = null;
			//原有的轉倉說明
			String transComment = null;
			if (!CollectionUtils.isEmpty(assetTransListList)) {
				for (DmmAssetTransListDTO assetTransListDTO : assetTransListList) {
					if(assetTransListDTO != null){
						assetTransListId = assetTransListDTO.getAssetTransListId();
						comment = assetTransListDTO.getComment();
						if (StringUtils.hasText(assetTransListId)){
							if (comment != null) {
								//得到數據庫中存儲的轉倉清單信息
								dmmAssetTransList = this.assetTransListDAO.findByPrimaryKey(DmmAssetTransList.class, assetTransListId);
								this.assetTransListDAO.getDaoSupport().flush();
								if (dmmAssetTransList != null) {
									//設置轉倉說明
									dmmAssetTransList.setComment(comment);
									//設置更新信息
									dmmAssetTransList.setUpdatedById(userId);
									dmmAssetTransList.setUpdatedByName(userName);
									dmmAssetTransList.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
									//this.assetTransListDAO.getDaoSupport().saveOrUpdate(dmmAssetTransList);
								}
							}
						}
					}
				}
				this.assetTransListDAO.getDaoSupport().flush();
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_TRANS_COMMENT_SUCCESS);
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SAVE_TRANS_COMMENT_FALURE);
			}
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(),null));
			//把map放入session
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		} catch (DataAccessException e) {
			LOGGER.error("update()", "DataAccessException:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("update()", "Excpetion:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}	
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#deleteAssetTransList(cafe.core.context.SessionContext)
	 */
	public SessionContext deleteAssetTransList(SessionContext sessionContext) throws ServiceException {
		try {
			AssetTransInfoFormDTO assetTransInfoFormDTO = (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
			// 轉倉清單信息
			List<DmmAssetTransListDTO> assetTransListList = null;
			//得到轉倉清單列表信息
			assetTransListList = assetTransInfoFormDTO.getList();
			//判斷是否有主鍵id
			if (!CollectionUtils.isEmpty(assetTransListList)) {
				// 轉倉清單ID
				String assetTransListId = null;
				//根據轉倉清單Id刪除轉倉清單信息
				for (DmmAssetTransListDTO assetTransListDTO:assetTransListList) {
					if (assetTransListDTO != null) {
						assetTransListId = assetTransListDTO.getAssetTransListId();
						if (StringUtils.hasText(assetTransListId)) {
							this.assetTransInfoDAO.getDaoSupport().delete(assetTransListId, DmmAssetTransList.class);
							this.assetTransInfoDAO.getDaoSupport().flush();
						}
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("deleteTransInfoList()", "DataAccessException:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("deleteTransInfoList()", "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#transferWarehouse(cafe.core.context.SessionContext)
	 */
	public SessionContext transferWarehouse(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		//轉倉批號
		String assetTransId = null;
		try {
			AssetTransInfoFormDTO assetTransInfoFormDTO = (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
			assetTransId = assetTransInfoFormDTO.getAssetTransId();
			//獲取登錄信息
			LogonUser logonUser = assetTransInfoFormDTO.getLogonUser();
			String userId = null; 
			String userName = null;
			if (logonUser != null) {
				//得到當前登入者的Id和Name
				userId = logonUser.getId();
				userName = logonUser.getName();
			} else {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.LIMITED_LOGON_ID));
				return sessionContext;
			}
			//轉倉確認通知人員
			String toMailId = assetTransInfoFormDTO.getToMailId();
			//修改設備轉倉單主檔的確認轉倉信息
			DmmAssetTransInfo dmmAssetTransInfo = (DmmAssetTransInfo) this.assetTransInfoDAO.findByPrimaryKey(DmmAssetTransInfo.class, assetTransId);
			Map map = new HashMap();
			if (dmmAssetTransInfo != null) {
				
				/*//設置通知人員
				//dmmAssetTransInfo.setNoticeUserId(toMailId);
				//設置轉倉日期
				dmmAssetTransInfo.setTransOutDate(DateTimeUtils.getCurrentTimestamp());
				dmmAssetTransInfo.setTransOutUserId(userId);
				dmmAssetTransInfo.setTransOutUserName(userName);
				//設置異動信息
				dmmAssetTransInfo.setUpdatedById(userId);
				dmmAssetTransInfo.setUpdatedByName(userName);
				dmmAssetTransInfo.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				//設置轉倉標誌
				dmmAssetTransInfo.setIsListDone(IAtomsConstants.YES);*/
				//轉倉確認發送mail給通知人員
				String historyId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY);
				if (IAtomsConstants.YES.equals(dmmAssetTransInfo.getIsListDone())) {
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_ALERDAY_UPDATE));
					return sessionContext;
				} 
				this.assetTransInfoDAO.updateAssetTransInfo(assetTransId, IAtomsConstants.PARAM_ASSET_STATUS_ON_WAY, IAtomsConstants.YES, IAtomsConstants.NO, historyId, userId, userName, IAtomsConstants.PARAM_ACTION_TRANSFER, toMailId, "");
				this.sendMail(toMailId, assetTransId);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.TRANS_OUT_SUCCESS, new String[]{assetTransId});
				//將消息放入map中
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(),null));
			} 
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		} catch (DataAccessException e) {
			LOGGER.error("transferWarehouse()", "DataAccessException:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("transferWarehouse()", "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}	
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#cancleTransferWarehouse(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext cancleTransferWarehouse(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		//轉倉批號
		String assetTransId = null;
		try {
			AssetTransInfoFormDTO assetTransInfoFormDTO = (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
			assetTransId = assetTransInfoFormDTO.getAssetTransId();
			//獲取登錄信息
			LogonUser logonUser = assetTransInfoFormDTO.getLogonUser();
			String userId = null; 
			String userName = null;
			if (logonUser != null) {
				//得到當前登入者的Id和Name
				userId = logonUser.getId();
				userName = logonUser.getName();
			} else {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.LIMITED_LOGON_ID));
				return sessionContext;
			}
			//修改設備轉倉單主檔的確認轉倉信息
			DmmAssetTransInfo dmmAssetTransInfo = (DmmAssetTransInfo) this.assetTransInfoDAO.findByPrimaryKey(DmmAssetTransInfo.class, assetTransId);
			Map map = new HashMap();
			if (StringUtils.hasText(assetTransId)) {
				if (IAtomsConstants.YES.equals(dmmAssetTransInfo.getIsCancel())) {
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_ALERDAY_UPDATE));
					return sessionContext;
				}
				String historyId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY);
				this.assetTransInfoDAO.updateAssetTransInfo(assetTransId, null, IAtomsConstants.NO, IAtomsConstants.YES, historyId, userId, userName, IAtomsConstants.PARAM_ACTION_CANCEL_TRANSFER, "", IAtomsConstants.PARAM_TRANS_STATUS_CANCEL_SUCCESS);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.CANCLE_TRANS_SUCCESS, new String[]{assetTransId});
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				//將消息放入map中
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(), null));
			}
			/*//根據轉倉批號得到轉倉清單
			List<DmmAssetTransListDTO> assetTransList = this.assetTransInfoDAO.listBy(assetTransId, null, null, -1, -1);
			//設備序號
			String serialNumber = null;
			if (!CollectionUtils.isEmpty(assetTransList)) {
				SimpleDtoDmoTransformer transformer = new SimpleDtoDmoTransformer();
				//庫存信息
				DmmRepository repository = null;
				DmmRepositoryDTO repositoryDTO = null;
				//歷史庫存信息
				DmmRepositoryHistory repositoryHist = null;
				//對轉倉清單進行循環修改設備狀態
				for (DmmAssetTransListDTO assetTransListDTO : assetTransList) {
					serialNumber = assetTransListDTO.getSerialNumber();
					if (serialNumber != null) {
						//得到設備的庫存信息
						repositoryDTO = this.repositoryDAO.getRepositoryBySerialNumber(serialNumber, null, null);
						if (repositoryDTO != null) {
							repository = this.repositoryDAO.findByPrimaryKey(DmmRepository.class, repositoryDTO.getAssetId());
							repository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY);
							this.repositoryDAO.getDaoSupport().saveOrUpdate(repository);
							this.repositoryDAO.getDaoSupport().flush();
							
							//存儲庫存歷史
							repositoryHist = new DmmRepositoryHistory();
							repositoryHist = (DmmRepositoryHistory) transformer.transform(repository, repositoryHist);
							repositoryHist.setUpdateDate(DateTimeUtils.getCurrentTimestamp());
							repositoryHist.setUpdateUser(userId);
							repositoryHist.setUpdateUserName(userName);
							repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
							this.repositoryDAO.getDaoSupport().saveOrUpdate(repositoryHist);
						}
					}
				}
			}
			//修改設備轉倉單主檔的確認轉倉信息
			DmmAssetTransInfo dmmAssetTransInfo = (DmmAssetTransInfo) this.assetTransInfoDAO.findByPrimaryKey(DmmAssetTransInfo.class, assetTransId);
			Map map = new HashMap();
			if (dmmAssetTransInfo != null) {
				dmmAssetTransInfo.setIsListDone(IAtomsConstants.NO);
				//將改批號刪除
				dmmAssetTransInfo.setIsCancel(IAtomsConstants.YES);
				//設置取消轉倉信息轉倉日期
				dmmAssetTransInfo.setCancelDate(DateTimeUtils.getCurrentTimestamp());
				dmmAssetTransInfo.setCancelUserId(userId);
				dmmAssetTransInfo.setCancelUserName(userName);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.CANCLE_TRANS_SUCCESS);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				//將消息放入map中
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(),null));
			}
			this.assetTransInfoDAO.getDaoSupport().saveOrUpdate(dmmAssetTransInfo);
			this.assetTransInfoDAO.getDaoSupport().flush(); */
			//把map放入session
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		} catch (DataAccessException e) {
			LOGGER.error("cancleTransferWarehouse()", "DataAccessException:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("cancleTransferWarehouse()", "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}	
		return sessionContext;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#getAssetTransInfoDTOByAssetTransId(cafe.core.context.MultiParameterInquiryContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DmmAssetTransInfoDTO getAssetTransInfoDTOByAssetTransId(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		DmmAssetTransInfoDTO returnDTO = new DmmAssetTransInfoDTO();
		try {
			String assetTransId = (String) inquiryContext.getParameter(DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue());
			//如果主鍵存在則求返回值
			if (StringUtils.hasText(assetTransId)) {
				//DmmAssetTransInfo assetTransInfo = (DmmAssetTransInfo) this.getAssetTransInfoDAO().findByPrimaryKey(DmmAssetTransInfo.class, assetTransId);
				returnDTO = this.assetTransInfoDAO.getAssetTransInfoById(assetTransId);
				//Transformer transformer = new SimpleDtoDmoTransformer();
				//returnDTO = (DmmAssetTransInfoDTO) transformer.transform(assetTransInfo, returnDTO);
			}
			return returnDTO;
		} catch (DataAccessException e) {
			LOGGER.error("getAssetTransInfoDTOByAssetTransId()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("getAssetTransInfoDTOByAssetTransId()", "is failed!!!", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#upload(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SessionContext upload(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			AssetTransInfoFormDTO assetTransInfoFormDTO = (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
			
			IAtomsLogonUser logonUser = (IAtomsLogonUser) assetTransInfoFormDTO.getLogonUser();
			if ( logonUser == null) {
				throw new ServiceException(IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT);
			}
			//匯入的文件
			MultipartFile uploadFiled = assetTransInfoFormDTO.getUploadFiled();
			//轉倉批號
			String assetTransId = assetTransInfoFormDTO.getAssetTransId();
			//設備序號
			String serialNumber = null;
			//設備說明
			String comment = null;
			//得到庫存信息
			DmmRepositoryDTO repositoryDTO = null;
			if (uploadFiled != null) {
				//錯誤信息
				Map<String, List<String>> errorMap = new LinkedHashMap<String, List<String>>();
				LOGGER.debug("update--start----->" + DateTimeUtils.getCurrentTimestamp());
				List<DmmAssetTransList> list  = this.checkUploadFiled(uploadFiled, errorMap, assetTransId);
				LOGGER.debug("update--end----->" + DateTimeUtils.getCurrentTimestamp());
				if (CollectionUtils.isEmpty(errorMap)) {
					if (!CollectionUtils.isEmpty(list)) {
						//轉倉清單主鍵
						String id = null;
						int size = list.size();
						DmmAssetTransList assetTransList = null;
						LOGGER.debug("save--start----->" + DateTimeUtils.getCurrentTimestamp());
						int startIndex = 0;
						int endIndex = 0;
						StringBuffer serialNumbers = new StringBuffer();
						StringBuffer comments = new StringBuffer();
						for (int i = 0; i < size/30 + 1; i++) {
							id = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_ASSET_TRANS_LIST);
							startIndex = i * 30;
							if ((size - startIndex)/30 == 0) {
								endIndex = size;
							} else {
								endIndex = startIndex + 30;
							}
							for (int j = startIndex; j < endIndex; j++) {
								assetTransList = list.get(j);
								serialNumbers.append(assetTransList.getSerialNumber()).append(IAtomsConstants.MARK_DELIMITER);
								comments.append(assetTransList.getComment()).append(IAtomsConstants.MARK_DELIMITER);
							}
							this.getAssetTransListDAO().addDmmAssetTransList(assetTransId, id, serialNumbers.toString(), comments.toString(), logonUser.getId(), logonUser.getName());
							serialNumbers = new StringBuffer();
							comments = new StringBuffer();
						}
						LOGGER.debug("save--end----->" + DateTimeUtils.getCurrentTimestamp());
					}
					this.getAssetTransListDAO().getDaoSupport().flush();
					msg = new Message(Message.STATUS.SUCCESS,IAtomsMessageCode.UPLOAD_SECCUSS);
				} else {
					if (errorMap.get(String.valueOf(0)) != null) {
						List<String> errorMsg = errorMap.get(String.valueOf(0));
						if (errorMsg.get(0).equals(IAtomsMessageCode.IATOMS_MSG_NONE_DATA)) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.IATOMS_MSG_NONE_DATA);
						} else {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ABOUCHEMENT_FAILURE_FOR_OVER_LIMIT, new String[]{PropertyNumberImportFormDTO.UPLOAD_NUMBER });
						}
					} else {
						//寫入錯誤信息
						//寫入錯誤信息
						Map<String, String> map = this.saveEorrorMsg(errorMap);
						assetTransInfoFormDTO.setErrorFileName(map.get(AssetTransInfoFormDTO.ERROR_FILE_NAME));
						assetTransInfoFormDTO.setTempFilePath(map.get(AssetTransInfoFormDTO.TEMP_ERROR_FILE_PATH));
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPLOAD_FAILURE);
					}
					
				}
			}
			sessionContext.setResponseResult(assetTransInfoFormDTO);
			sessionContext.setReturnMessage(msg);
			Map map = new HashMap();
			map.put(AssetTransInfoFormDTO.ERROR_FILE_NAME, assetTransInfoFormDTO.getErrorFileName());
			map.put(AssetTransInfoFormDTO.TEMP_ERROR_FILE_PATH, assetTransInfoFormDTO.getTempFilePath());
			if (Message.STATUS.SUCCESS.equals(msg.getStatus())) {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		} catch (Exception e) {
			LOGGER.error("upload()", "Error -->", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	/**
	 * Purpose:將錯誤訊息存入txt文檔
	 * @author barryzhang
	 * @param errorMap：錯誤信息集合
	 * @return String 文檔名
	 */
	private Map<String, String> saveEorrorMsg(Map<String, List<String>> errorMap) {
		PrintWriter printWriter = null;
		String fileName = null; 
		Map<String, String> map = new HashMap<String, String>();
		try {
			if (!CollectionUtils.isEmpty(errorMap)) {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(i18NUtil.getName(IAtomsMessageCode.PARAM_ERROR_INFORMATION)  + IAtomsConstants.MARK_ENTER);
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
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				//錯誤信息文件名
				fileName = UUID.randomUUID().toString() + IAtomsConstants.MARK_NO + IAtomsConstants.PARAM_FILE_SUFFIX_TXT;
				//文件路徑
				String errorFilePath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH) + File.separator 
						+ yearMonthDay + File.separator + IAtomsConstants.UC_NO_DMM_03050 + File.separator + IAtomsConstants.PARAM_STRING_IMPORT;
				File filePath = new File(errorFilePath);
				if (!filePath.exists() || !filePath.isDirectory()) {
					filePath.mkdirs();
				}

				map.put(AssetTransInfoFormDTO.ERROR_FILE_NAME, fileName);
				map.put(AssetTransInfoFormDTO.TEMP_ERROR_FILE_PATH, errorFilePath);
				File saveFile = new File(filePath, fileName);
				printWriter = new PrintWriter(saveFile, IAtomsConstants.ENCODE_UTF_8);
				printWriter.print(stringBuffer.toString());
			}
		} catch (Exception e) {
			LOGGER.error("saveEorrorMsg()", "Error -->", e);
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
		return map;
	}
	
	/**
	 * Purpose:檢核上傳文件
	 * @author amandawang
	 * @param uploadFiled
	 * @param errorMap
	 * @return List<AssetTransList>
	 */
	private List<DmmAssetTransList> checkUploadFiled(MultipartFile uploadFiled, Map<String, List<String>> errorMap, String assetTransId) {
		List<DmmAssetTransList> list = new ArrayList<DmmAssetTransList>();
		List<DmmAssetTransList> tempList = new ArrayList<DmmAssetTransList>();
		List<Integer> repeatRow = new ArrayList<Integer>();
		DmmAssetTransList assetTransList = null;
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
				String comment = null;
				//獲取行數
				rowCount = this.getExcelRealRowCount(sheet);
				if (rowCount <= 1) {
					errorList = new ArrayList<String>();
					errorList.add(IAtomsMessageCode.IATOMS_MSG_NONE_DATA);
					errorMap.put(String.valueOf(0), errorList);
				} else if(rowCount>501){
					errorList = new ArrayList<String>();
					errorList.add(IAtomsMessageCode.ABOUCHEMENT_FAILURE_FOR_OVER_LIMIT);
					errorMap.put(String.valueOf(0), errorList);
				}else {
					Map<String, Integer> serialNumberRepeatMap = new HashMap<String, Integer>();
					//根據轉倉批號得到轉倉表相關信息
					DmmAssetTransInfo dmmAssetTransInfo = (DmmAssetTransInfo) this.assetTransInfoDAO.findByPrimaryKey(DmmAssetTransInfo.class, assetTransId);
					for (int i = 1; i <= rowCount; i++) {
						errorList = new ArrayList<String>();
						//獲取行
						row = sheet.getRow(i);
						if (isRowEmpty(row)) continue;
						//設置設備序號的單元格屬性
						if (row.getCell(0) != null) {
							row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
						}
						if (row.getCell(1) != null) {
							row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
						}
						serialNumber = this.getCellFormatValue(row.getCell(0), null);
						comment = this.getCellFormatValue(row.getCell(1), null);
						
						//轉出倉
						String fromWarehouseId = null;
						if(dmmAssetTransInfo != null) {
							fromWarehouseId = dmmAssetTransInfo.getFromWarehouseId();
						}
						//檢核單元格信息，如長度，不允許為空,以及轉出倉下是否有設備狀態為庫存的設備信息
						this.checkCell(serialNumber, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TRANS_LIST_SERIAL_NUMBER), false, 20, errorList, assetTransId, fromWarehouseId, serialNumberRepeatMap);
						this.checkCell(comment, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TRANS_LIST_COMMENT), true, 200, errorList, null, null, null);
						//檢查匯入的列是否有重複的設備序號
						DmmAssetTransList tempAssetTransList = new DmmAssetTransList();
						tempAssetTransList.setSerialNumber(serialNumber);
						tempAssetTransList.setComment(comment);
						tempList.add(tempAssetTransList);
						if (CollectionUtils.isEmpty(errorList)) {
							assetTransList = new DmmAssetTransList();
							assetTransList.setSerialNumber(serialNumber);
							assetTransList.setComment(comment);
							list.add(assetTransList);
						} else {
							errorMap.put(String.valueOf(i), errorList);
						}
					}
				}
				
			}
		} catch (Exception e) {
			LOGGER.error("checkUploadFiled()", "error---->", e);
		}
		return list;
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
		        if (!StringUtils.hasText(this.getCellFormatValue(row.getCell(i), null))) {  
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
				        if (!StringUtils.hasText(this.getCellFormatValue(tempRow.getCell(j), null))) {  
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
	 * Purpose:檢核上傳的參數
	 * @author barryzhang
	 * @param param：參數
	 * @param row：行
	 * @param cell：列
	 * @param isNull：是否可以為空
	 * @param length：最大長度
	 * @param errorList：錯誤信息
	 * @param assetTransId：轉倉批號
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	private void checkCell(String param, int row, String cell, boolean isNull, int length, List<String> errorList, String assetTransId,
			String fromWarehouseId, Map<String, Integer> serialNumberRepeatMap) {
		String errorMsg = null;
		Boolean isError = false;
		//核檢是否可以為空
		if (isNull) {
			if (StringUtils.hasText(param) && param.trim().length() > length) {
				errorMsg = i18NUtil.getName(IAtomsMessageCode.IMPORT_LENGTH_LIMIT, new String[]{"" + length},null);
				errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_COLUMN_NAME, new String[]{String.valueOf((row)), cell}, null) + errorMsg);
			}
		} else {
			if (!StringUtils.hasText(param)) {
				errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_COLUMN_NAME, new String[]{String.valueOf((row)), cell}, null) + i18NUtil.getName(IAtomsMessageCode.IMPORT_VALUE_EMPTY));
			} else {
				//核檢是否檔內重複
				if (serialNumberRepeatMap.containsKey(param)) {
					errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_ROW, new String[]{String.valueOf((row)), serialNumberRepeatMap.get(param).toString()}, null) + 
							i18NUtil.getName(IAtomsMessageCode.SERIAL_NUMBER_REPETITION));
				} else {
					serialNumberRepeatMap.put(param, row);
					//核檢格式是否正確
					if (!ValidateUtils.numberOrEnglish(param)) {
						errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(row)}, null) 
								+ i18NUtil.getName(IAtomsMessageCode.INPUT_LIMIT_ENGLISH_AND_NUMBER, new String[]{cell}, null));
						isError = Boolean.TRUE;
					}
					//核檢長度是否正確
					if (!ValidateUtils.length(param, 1, length)) {
						errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(row)}, null) 
								+ cell
								+ i18NUtil.getName(IAtomsMessageCode.IMPORT_LENGTH_LIMIT, new String[]{IAtomsConstants.PROPERTY_ID_LENGTH}, null));
						isError = Boolean.TRUE;
					}
					//核檢重複
					if (!isError) {
						//根據轉倉批號得到轉倉表相關信息
						DmmRepositoryDTO  repositoryDTO = this.repositoryDAO.checkAssetIsInWarehouse(param, fromWarehouseId);
						//如果結果為空，代表該設備序號不屬於該倉庫下
						if (repositoryDTO == null) {
							errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((row)), null}, null) + i18NUtil.getName(IAtomsMessageCode.NO_SERIAL_NUMBER_IN_WAREHOUSE));
						} else {
							//核檢是否以存在
							//檢查轉倉清單中是否重複的轉倉批號
							Boolean hasSerialNumber = this.assetTransListDAO.isCheckHasSerialNumber(param);
							if (hasSerialNumber) {
								errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((row)), null}, null) + i18NUtil.getName(IAtomsMessageCode.ASSET_NUMBER_IS_REPETITION));
							}
							//核檢該設備序號是否庫存(Task2555-可轉倉新增已拆回狀態)
							if (!IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY.equals(repositoryDTO.getStatus())
									&& !IAtomsConstants.PARAM_ASSET_STATUS_RETURNED.equals(repositoryDTO.getStatus())
									&& !IAtomsConstants.PARAM_ASSET_STATUS_REPAIR.equals(repositoryDTO.getStatus())) {
								errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((row)), null}, null) + i18NUtil.getName(IAtomsMessageCode.SERIAL_NUMBER_NO_IN_REPOSITORY));
							}
						}
					}
				}
				
			}
		}
	}

	/**
	 * Purpose:依據Cell類型獲取數據
	 * @author amandawang
	 * @param cell:取得Excel cell單元值
	 * @param getTime 日期格式
	 * @return String:单元格数据内容
	 */
	private String getCellFormatValue(Cell cell, String getTime) {
	    String cellvalue = "";
	    if (cell != null) {
	        // 判断当前Cell的Type
	        switch (cell.getCellType()) {
	            // 如果当前Cell的Type是否为int
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
	                }
	                break;
	            }
	            // 判断当前的cell是否为String
	            case Cell.CELL_TYPE_STRING:
	            	// 取得当前Cell的String值
	                cellvalue = cell.getStringCellValue();
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
	    return cellvalue;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#exportAsset(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext exportAsset(SessionContext sessionContext) throws ServiceException {
		AssetTransInfoFormDTO assetTransInfoFormDTO = null;
		DmmAssetTransInfoDTO assetTransInfoDTO = null;
		try {
			assetTransInfoFormDTO = (AssetTransInfoFormDTO)sessionContext.getRequestParameter();
			//是否查詢歷史
			Boolean isHistory = assetTransInfoFormDTO.getIsHistory();
			if (isHistory == null) {
				isHistory = Boolean.FALSE;
			}
			//轉倉批號
			String assetTransId = assetTransInfoFormDTO.getAssetTransId();
			if (StringUtils.hasText(assetTransId)) {
				assetTransInfoDTO = this.assetTransInfoDAO.getAssetTransInfoById(assetTransId);
			}
			assetTransInfoDTO.setAssetTransId(assetTransId);
			assetTransInfoFormDTO.setAssetTransInfoDTO(assetTransInfoDTO);
			Boolean isQueryHistory = false;
			if (isHistory) {
				isQueryHistory = true;
			}
			//轉倉清單資料
			List<DmmAssetTransListDTO> assetTransList = this.assetTransInfoDAO.listBy(assetTransId, isHistory, null, null, -1, -1, isQueryHistory, false);
			List<DmmAssetTransListDTO> list = new ArrayList<DmmAssetTransListDTO>();
			if(!CollectionUtils.isEmpty(assetTransList)){
				//統計各個設備的數量
				Map<String, List<String>> map = new HashMap<String, List<String>>();
				List<String> nameList = null;
				for (DmmAssetTransListDTO assetTransListDTO : assetTransList) {
					nameList = map.get(assetTransListDTO.getName());
					if (nameList == null) {
						nameList = new ArrayList<String>();
					}
					nameList.add(assetTransListDTO.getName());
					map.put(assetTransListDTO.getName(), nameList);
				}
				int i = 1;
				for (Map.Entry<String,  List<String>> entry : map.entrySet()) {
					 DmmAssetTransListDTO assetTransListDTO =new DmmAssetTransListDTO();
					 assetTransListDTO.setNumber(i);
					 assetTransListDTO.setName(entry.getKey());
					 assetTransListDTO.setSum(entry.getValue().size());
					 list.add(assetTransListDTO);
					 i++;
				}
				 assetTransInfoFormDTO.setList(list);
			}	 
			sessionContext.setResponseResult(assetTransInfoFormDTO);
		} catch (Exception e) {
			LOGGER.error("exportAsset()", "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	/**
	 * Purpose:發送mail
	 * @author barryzhang
	 * @param toId：收件人
	 * @param fromId：發件人
	 * @param assetTransId：轉倉批號
	 */
	@SuppressWarnings("unchecked")
	private void sendMail(String toId, String assetTransId){
		try {
			if(StringUtils.hasText(toId)){
				String [] toMailId = toId.split(IAtomsConstants.MARK_SEPARATOR); 
				for ( int i = 0;i< toMailId.length;i++) {
					AdmUser to = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, toMailId[i]);
					if(to != null){
						//接收人Mail地址
						String toMailAddress = to.getEmail();
						//如果收件人沒有email則不發送郵件
						if (!StringUtils.hasText(toMailAddress)) {
							continue;
						}
						//收件人姓名
						String toName = to.getCname();
						//邮件主题
						String mailSubject = "iATOMS-轉倉-轉出通知";
						StringBuffer strBuffer= new StringBuffer();
						//【轉倉批號】由【轉出倉】轉入【轉入倉】，還請及時確認。
						if(StringUtils.hasText(assetTransId)){
							DmmAssetTransInfoDTO assetTransInfoDTO = this.assetTransInfoDAO.getAssetTransInfoById(assetTransId);
							if (assetTransInfoDTO!= null) {
								strBuffer.append("【"+assetTransId+"】由【" + assetTransInfoDTO.getFromWarehouseName());
								//Bug #2347 轉入 加【 】--update by 2017/09/04
								strBuffer.append("】轉入【 " + assetTransInfoDTO.getToWarehouseName()+"】");
							}
						}
						strBuffer.append("，還請及時確認。");
						//邮件内容
						String mailContext = strBuffer.toString();
						//邮件主题模板
						String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + "mailExampleSubjectText.txt";
						//邮件内容模板
						String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + "mailExampleContext.html";
						Map<String, Object> variables = new HashMap<String, Object>();
						//接收人名称 
						variables.put("toName", StringUtils.hasText(toName)?toName:"XXX");
						//接收人Maill地址
						variables.put("toMail",StringUtils.hasText(toMailAddress)?toMailAddress:"XXX");
						//邮件主题 
						variables.put("mailSubject",StringUtils.hasText(mailSubject)?mailSubject:"转仓退回");
						//邮件内容
						variables.put("mailContext",StringUtils.hasText(mailContext)?mailContext:"XXX");
						this.mailComponent.mailTo(null, toMailAddress, subjectTemplate, textTemplate, variables);
					}
				}
			}
		} catch (ServiceException e) {
			LOGGER.error("sendMail()", "ServiceException:", e);
		}
	}
	
	/**
	 * Purpose:轉倉退回郵件的發送
	 * @param toId：收件人，轉出倉人員的ID
	 * @param fromId：發件人，當前登錄人的ID
	 * @param assetTransId：轉倉批號
	 * @param refuseReason：轉倉退回的原因
	 * @return boolean：發送成功與否
	 */
	private boolean sendMail(String toId, String assetTransId, String refuseReason){
		boolean result = false;
		try {
			//若存在發件人和收件人
			if(StringUtils.hasText(toId)){
				//獲取收件人的信息
				AdmUser to = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, toId);
				if(to != null){
					//接收人Mail地址
					String toMailAddress = to.getEmail();
					//收件人姓名
					String toName = to.getCname();
					//邮件主题
					String mailSubject = i18NUtil.getName(CheckTransInfoFormDTO.CHECK_TRANS_MAILSUBJECT);
					StringBuffer strBuffer= new StringBuffer();
					if(StringUtils.hasText(assetTransId)){
						strBuffer.append("【"+assetTransId+"】因");
					}
					if(StringUtils.hasText(refuseReason)){
						strBuffer.append("【"+refuseReason+"】以致轉入驗收退回,");
					}
					//Bug #2347 改為 【TN201709010005】因【少了10台】以致轉入驗收退回,還請及時確認。--update by 2017/09/04
					strBuffer.append("還請及時確認。");
					//邮件内容
					String mailContext = strBuffer.toString();
					//邮件主题模板
					String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + "mailExampleSubjectText.txt";
					//邮件内容模板
					String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + "mailExampleContext.html";
					Map<String, Object> variables = new HashMap<String, Object>();
					//接收人名称 
					variables.put(CheckSettingDTO.TO_NAME, StringUtils.hasText(toName)?toName:"XXX");
					//接收人Maill地址
					variables.put(CheckSettingDTO.TO_MAIL,StringUtils.hasText(toMailAddress)?toMailAddress:"XXX");
					//邮件主题 
					variables.put(CheckSettingDTO.MAIL_SUBJECT,StringUtils.hasText(mailSubject)?mailSubject:"XXX");
					//邮件内容
					variables.put(CheckSettingDTO.MAIL_CONTEXT,StringUtils.hasText(mailContext)?mailContext:"XXX");
					this.mailComponent.mailTo(null, toMailAddress, subjectTemplate, textTemplate, variables);
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			LOGGER.error("sendMail()", "error:", e);
		}
		return result;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmUserService#getWareHouseUserNameList()
	 */
	/*public List<Parameter> getWareHouseUserNameList() throws ServiceException {
		List<Parameter> list = null;
		try {
			list = this.assetTransInfoDAO.getWareHouseUserNameList(null, true);
		} catch (DataAccessException e) {
			LOGGER.error("getAssetTransInfoList()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getAssetTransInfoList()", "error:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return list;
	}*/	
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#getWareHouseUserNameList(cafe.core.context.MultiParameterInquiryContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Parameter> getWareHouseUserNameList(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		//List<Parameter> wareHouseUserNameList = null;
		List<Parameter> noticeUserNameList = null;
		try {
			String assetTransId = (String) inquiryContext.getParameter(DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue());
			if (StringUtils.hasText(assetTransId)) {
				DmmAssetTransInfo assetTransInfo = (DmmAssetTransInfo) this.getAssetTransInfoDAO().findByPrimaryKey(DmmAssetTransInfo.class, assetTransId);
				//轉入倉Id
				String transToWarehouseId = assetTransInfo.getToWarehouseId();
				//得到通知人員列表
				//wareHouseUserNameList = this.assetTransInfoDAO.getWareHouseUserNameList(transToWarehouseId, true);
				noticeUserNameList =  this.assetTransInfoDAO.getWareHouseUserNameList(transToWarehouseId, false);
				//wareHouseUserNameList.addAll(NoticeUserNameList);
			}
		} catch (DataAccessException e) {
			LOGGER.error("getWareHouseUserNameList()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("getWareHouseUserNameList()", "is failed!!!", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
		return noticeUserNameList;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#queryTransInfo(cafe.core.context.SessionContext)
	 */
	public SessionContext queryTransInfo(SessionContext sessionContext) throws ServiceException {
		try{
			List<DmmAssetTransListDTO> asstTransListDTOs = new ArrayList<DmmAssetTransListDTO>();
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, 0);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, asstTransListDTOs);
			AssetTransInfoFormDTO formDTO = (AssetTransInfoFormDTO) sessionContext.getRequestParameter();
			//獲取查詢條件
			String queryAssetTransId = formDTO.getAssetTransId();
			Integer pageSize = formDTO.getRows();
			Integer pageIndex = formDTO.getPage();
			String sort = formDTO.getSort();
			String order = formDTO.getOrder();
			Message msg = null;
			//獲取符合查詢條件的總記錄數
			if (StringUtils.hasText(queryAssetTransId)) {
				Integer totalSize = this.assetTransInfoDAO.getCount(queryAssetTransId, Boolean.FALSE);
				//若總記錄數大於0
				if (totalSize > 0) {
					asstTransListDTOs = this.assetTransInfoDAO.listBy(queryAssetTransId, Boolean.FALSE, order, sort, pageIndex, pageSize, false,     false);
					if (!CollectionUtils.isEmpty(asstTransListDTOs)) {
						formDTO.setList(asstTransListDTOs);
						formDTO.getPageNavigation().setRowCount(totalSize);
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
					} else {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
					}
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				}
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, totalSize);
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, asstTransListDTOs);
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		}catch(Exception e){
			LOGGER.error("queryTransInfo()", "is error: ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTransferService#getAssetInfoList(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public String checkSerialNumber(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		Boolean isLive = false;
		try {
			String assetTransId = (String) inquiryContext.getParameter(DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue());
			String serialNumber = (String) inquiryContext.getParameter(DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());

			//如果主鍵存在則求返回值
			if (StringUtils.hasText(assetTransId) && StringUtils.hasText(serialNumber)) {
				isLive = this.assetTransListDAO.checkSerialNumber(assetTransId, serialNumber);
			}
			if (isLive) {
				return serialNumber;
			}
		} catch (DataAccessException e) {
			LOGGER.error("checkSerialNumber()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("checkSerialNumber()", "is failed!!!", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
		return null;
	}
	
	/**
	 * @return the assetTransInfoDAO
	 */
	public IDmmAssetTransInfoDAO getAssetTransInfoDAO() {
		return assetTransInfoDAO;
	}

	/**
	 * @param assetTransInfoDAO the assetTransInfoDAO to set
	 */
	public void setAssetTransInfoDAO(IDmmAssetTransInfoDAO assetTransInfoDAO) {
		this.assetTransInfoDAO = assetTransInfoDAO;
	}

	/**
	 * @return the assetTransListDAO
	 */
	public IDmmAssetTransListDAO getAssetTransListDAO() {
		return assetTransListDAO;
	}

	/**
	 * @param assetTransListDAO the assetTransListDAO to set
	 */
	public void setAssetTransListDAO(IDmmAssetTransListDAO assetTransListDAO) {
		this.assetTransListDAO = assetTransListDAO;
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
	 * @return the mailComponent
	 */
	public MailComponent getMailComponent() {
		return mailComponent;
	}

	/**
	 * @param mailComponent the mailComponent to set
	 */
	public void setMailComponent(MailComponent mailComponent) {
		this.mailComponent = mailComponent;
	}

	/**
	 * @return the admUserDAO
	 */
	public IAdmUserDAO getAdmUserDAO() {
		return admUserDAO;
	}

	/**
	 * @param admUserDAO the admUserDAO to set
	 */
	public void setAdmUserDAO(IAdmUserDAO admUserDAO) {
		this.admUserDAO = admUserDAO;
	}

	/**
	 * @return the warehouseDAO
	 */
	public IWarehouseDAO getWarehouseDAO() {
		return warehouseDAO;
	}
	/**
	 * @param warehouseDAO the warehouseDAO to set
	 */
	public void setWarehouseDAO(IWarehouseDAO warehouseDAO) {
		this.warehouseDAO = warehouseDAO;
	}
	
	
}
