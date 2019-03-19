package com.cybersoft4u.xian.iatoms.services.impl;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiLogDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PayMentCMSDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReceiveJobNoticeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmHistoryCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentTranscationDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PaymentFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.NetUtil;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.services.IPaymentService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IApiLogDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentItemDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentTranscationDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.ApiLog;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmPaymentInfo;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmPaymentItem;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmPaymentTranscation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
/**
 * Purpose: 求償作業Service
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/7
 * @MaintenancePersonnel CarrieDuan
 */
public class PaymentService extends AtomicService implements IPaymentService {

	/**
	 * 系统日志记录文件
	 */
	private static final CafeLog LOG = CafeLogFactory.getLog(GenericConfigManager.SERVICE, PaymentService.class);
	
	/**
	 * 案件歷史處理資料檔DAO
	 */
	private ISrmCaseHandleInfoDAO caseHandleInfoDAO;
	/**
	 * 求償資料檔DAO
	 */
	private ISrmPaymentInfoDAO paymentInfoDAO;
	/**
	 * 求償項目資料表
	 */
	private ISrmPaymentItemDAO srmPaymentItemDAO;
	/**
	 * 求償處理記錄檔DAO
	 */
	private ISrmPaymentTranscationDAO paymentTranscationDAO;
	/**
	 * 人員管理DAO
	 */
	private IAdmUserDAO admUserDAO;
	/**
	 * 發送mail組件
	 */
	private MailComponent mailComponent;
	/**
	 * 庫存DAO
	 */
	private IDmmRepositoryDAO repositoryDAO;
	/**
	 * 寫入API_LOG
	 */
	private IApiLogDAO apiLogDAO;
	/**
	 * Constructor:无参构造子
	 */
	public PaymentService() {
		
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			PaymentFormDTO formDTO =  (PaymentFormDTO) sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			List<AdmRoleDTO> roleDTOs = logonUser.getUserFunctions();
			List<String> roleUsers = new ArrayList<String>();
			//獲取當前登錄者的角色
			for (AdmRoleDTO admRoleDTO : roleDTOs) {
				if (IAtomsConstants.CUSTOMER_SERVICE.equals(admRoleDTO.getRoleCode())) {
					roleUsers.add(IAtomsConstants.CUSTOMER_SERVICE);
				}
				if (IAtomsConstants.WAREHOUSE_KEEPER.equals(admRoleDTO.getRoleCode())) {
					roleUsers.add(IAtomsConstants.WAREHOUSE_KEEPER);
				}
				if (IAtomsConstants.ACCOUNTING.equals(admRoleDTO.getRoleCode())) {
					roleUsers.add(IAtomsConstants.ACCOUNTING);
				}
			}
			formDTO.setRoleUsers(roleUsers);
			sessionContext.setResponseResult(formDTO);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOG.error("init(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#initEdit(cafe.core.context.SessionContext)
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			PaymentFormDTO formDTO =  (PaymentFormDTO) sessionContext.getRequestParameter();
			String paymentId = formDTO.getPaymentId();
			if (StringUtils.hasText(paymentId)) {
				//獲取當前求償編號的基礎信息
				List<SrmPaymentInfoDTO> srmPaymentInfoDTOs = this.srmPaymentItemDAO.listBy(paymentId, Boolean.FALSE);
				if (!CollectionUtils.isEmpty(srmPaymentInfoDTOs)) {
					List<SrmPaymentInfoDTO> paymentAssetList = new ArrayList<SrmPaymentInfoDTO>();
					List<SrmPaymentInfoDTO> paymentSuppliesList = new ArrayList<SrmPaymentInfoDTO>();
					for (SrmPaymentInfoDTO srmPaymentInfoDTO : srmPaymentInfoDTOs) {
						//如求償資訊選擇的是字行輸入，則需將求償資訊以及描述連接起來輸出
						if (IAtomsConstants.PAYMENT_REASON.SELF_INPUT.getCode().equals(srmPaymentInfoDTO.getPaymentReason())) {
							srmPaymentInfoDTO.setPayInfo(srmPaymentInfoDTO.getPaymentReasonName() + IAtomsConstants.MARK_MIDDLE_LINE +
									srmPaymentInfoDTO.getReasonDetail());
						} else {
							srmPaymentInfoDTO.setPayInfo(srmPaymentInfoDTO.getPaymentReasonName());
						}
						//核檢該筆設備狀態是設備還是耗材。
						if (srmPaymentInfoDTO.getPaymentItem().equals(IAtomsConstants.PAYMENT_ITEM_ASSET)){
							paymentAssetList.add(srmPaymentInfoDTO);
						} else {
							paymentSuppliesList.add(srmPaymentInfoDTO);
						}
					}
					GsonBuilder builder =  new GsonBuilder();
					builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
						@Override
						public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
							return new Date(json.getAsJsonPrimitive().getAsLong());
						}
					});
					Gson gsons = builder.create();
					//将集合转化为JSON字符串
					String paymentAsset = gsons.toJson(paymentAssetList);
					String paymentSupplies = gsons.toJson(paymentSuppliesList);
					formDTO.setAssetTableValue(paymentAsset);
					formDTO.setPeripheralSuppliesTableValue(paymentSupplies);
					formDTO.setPaymentInfoDTO(srmPaymentInfoDTOs.get(0));
				}
			}
			sessionContext.setResponseResult(formDTO);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOG.error("initEdit() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOG.error("initEdit(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#initView(cafe.core.context.SessionContext)
	 */
	public SessionContext initView(SessionContext sessionContext) throws SecurityException {
		// 页面显示信息
		Message msg = null;
		try {
			PaymentFormDTO formDTO =  (PaymentFormDTO) sessionContext.getRequestParameter();
			sessionContext.setResponseResult(formDTO);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOG.error("initView(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	 
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#query(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext query(SessionContext sessionContext)throws ServiceException {
		try {
			PaymentFormDTO claimWorkFormDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
			Message msg = null;
			if (claimWorkFormDTO != null ) {
				Timestamp caseStart = null;
				Timestamp caseEnd = null;
				Timestamp lostDayStartDate = null;
				Timestamp lostDayEndDate = null;
				String createDateStart = claimWorkFormDTO.getQueryCreateCaseDateStart();
				if (StringUtils.hasText(createDateStart)) {
					caseStart = DateTimeUtils.toTimestamp(createDateStart);
				}
				String caseEndDate = claimWorkFormDTO.getQueryCreateCaseDateEnd();
				SimpleDateFormat sf = new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_SLASH);
				if (StringUtils.hasText(caseEndDate)) {
					Calendar calendar = new GregorianCalendar(); 
					calendar.setTime(DateTimeUtils.toDate(caseEndDate)); 
					calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动 
					caseEnd = DateTimeUtils.toTimestamp(sf.format(calendar.getTime()));
				}
				String lostDayStart = claimWorkFormDTO.getQueryLostDayStart();
				if (StringUtils.hasText(lostDayStart)) {
					lostDayStartDate = DateTimeUtils.toTimestamp(lostDayStart);
				}
				String lostDayEnd = claimWorkFormDTO.getQueryLostDayEnd();
				if (StringUtils.hasText(lostDayEnd)) {
					Calendar calendar = new GregorianCalendar(); 
					calendar.setTime(DateTimeUtils.toDate(lostDayEnd)); 
					calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动 
					lostDayEndDate = DateTimeUtils.toTimestamp(sf.format(calendar.getTime()));
				}
//				String lostDayStart = claimWorkFormDTO.getQueryLostDayStart();
//				if (StringUtils.hasText(lostDayStart)) {
//					realFinishDate = DateTimeUtils.toTimestamp(lostDayStart);
//				}
				//Task #3452 是否微型商戶
				Boolean isMicro = claimWorkFormDTO.getIsMicro();
				//獲取符合要求的總比數
				List<SrmPaymentInfoDTO> srmPaymentInfoDTOs = this.paymentInfoDAO.getIds(claimWorkFormDTO.getQueryCompanyId(), null, caseStart, caseEnd, 
						claimWorkFormDTO.getQueryMerchantCode(), claimWorkFormDTO.getQueryDtid(), claimWorkFormDTO.getQueryTid(), claimWorkFormDTO.getQueryDataStatus(),
						 claimWorkFormDTO.getQueryCompensationMethod(), lostDayStartDate, lostDayEndDate,
						claimWorkFormDTO.getQuerySerialNumber(), claimWorkFormDTO.getOrder(), claimWorkFormDTO.getSort(), 
						-1, -1, isMicro);
				
				if (srmPaymentInfoDTOs.size() > 0) {
					Integer count = srmPaymentInfoDTOs.size();
					//獲取符合查詢條件的求償ID
					srmPaymentInfoDTOs = this.paymentInfoDAO.getIds(claimWorkFormDTO.getQueryCompanyId(), null, caseStart, caseEnd, 
							claimWorkFormDTO.getQueryMerchantCode(), claimWorkFormDTO.getQueryDtid(), claimWorkFormDTO.getQueryTid(), 
							claimWorkFormDTO.getQueryDataStatus(), claimWorkFormDTO.getQueryCompensationMethod(), lostDayStartDate, lostDayEndDate,
							claimWorkFormDTO.getQuerySerialNumber(), claimWorkFormDTO.getOrder(), claimWorkFormDTO.getSort(), 
							claimWorkFormDTO.getPage().intValue(), claimWorkFormDTO.getRows().intValue(), isMicro);
					List<String> paymentIds = new ArrayList<String>();
					for (SrmPaymentInfoDTO srmPaymentInfoDTO : srmPaymentInfoDTOs) {
						paymentIds.add(srmPaymentInfoDTO.getPaymentId());
					}
					//根據查詢出的ID，獲取所有符合條件的求償信息
					List<SrmPaymentInfoDTO> paymentInfoDTOs = this.paymentInfoDAO.listBy(null, paymentIds, null, null, null,
							null, null, null, null, null, null, null, claimWorkFormDTO.getOrder(), claimWorkFormDTO.getSort(), 
							-1, -1, null);
					for (SrmPaymentInfoDTO dto : paymentInfoDTOs) {
						if (StringUtils.hasText(dto.getStatus())) {
							String[] action = dto.getStatus().split(IAtomsConstants.MARK_CENTER_LINE);
							if (action.length > 1) {
								dto.setStatusName(i18NUtil.getName(action[0]) + IAtomsConstants.MARK_CENTER_LINE + i18NUtil.getName(action[1]));
								dto.setStatus(action[1]);
							} else {
								dto.setStatusName(i18NUtil.getName(dto.getStatus()));
							}
						}
					}
					claimWorkFormDTO.setList(paymentInfoDTOs);
					claimWorkFormDTO.getPageNavigation().setRowCount(count);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS, new String[]{this.getMyName()});
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(claimWorkFormDTO);
		} catch (DataAccessException e) {
			LOG.error("query() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOG.error("query(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#save(cafe.core.context.SessionContext)
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		try {
			PaymentFormDTO claimWorkFormDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
			//DTO与與DMO轉換工具
			Transformer transformer = new SimpleDtoDmoTransformer();
			Message msg = null;
			if (claimWorkFormDTO != null) {
				IAtomsLogonUser logonUser = (IAtomsLogonUser) claimWorkFormDTO.getLogonUser();
				//獲取保存的求償基礎資料
				SrmPaymentInfoDTO paymentInfoDTO = claimWorkFormDTO.getPaymentInfoDTO();
				//獲取設備資料列表數據
				List<SrmPaymentItemDTO> assetItemDTOs = claimWorkFormDTO.getAssetItemDTOs();
				//獲取耗材資料列表數據
				List<SrmPaymentItemDTO> peripheralSuppliesList = claimWorkFormDTO.getPeripheralSuppliesList();
				String paymentId = claimWorkFormDTO.getPaymentId();
				//保存設備清單、耗材清單時，記錄首次id
				String tempId = null;
				//如果存在求償Id，則為修改，反之保存。
				if (!StringUtils.hasText(paymentId)) {
					paymentId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_SRM_PAYMENT_INFO);
					//保存求償基本信息
					if (paymentInfoDTO != null) {
						SrmPaymentInfo srmPaymentInfo = (SrmPaymentInfo) transformer.transform(paymentInfoDTO, new SrmPaymentInfo());
						srmPaymentInfo.setPaymentId(paymentId);
						srmPaymentInfo.setCreatedById(logonUser.getId());
						srmPaymentInfo.setCreatedByName(logonUser.getName());
						srmPaymentInfo.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
						srmPaymentInfo.setUpdatedById(logonUser.getId());
						srmPaymentInfo.setUpdatedByName(logonUser.getName());
						srmPaymentInfo.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						this.paymentInfoDAO.getDaoSupport().saveOrUpdate(srmPaymentInfo);
						this.paymentInfoDAO.getDaoSupport().flush();
					}
					//保存求償的設備清單信息
					Integer i = 0;
					SrmPaymentItem srmPaymentItem = null;
					//保存耗材資料數據
					tempId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_SRM_PAYMENT_ITEM);
					
					for (SrmPaymentItemDTO srmPaymentItemDTO : peripheralSuppliesList) {
						srmPaymentItem = (SrmPaymentItem) transformer.transform(srmPaymentItemDTO, new SrmPaymentItem());
						srmPaymentItem.setItemId(tempId + IAtomsConstants.MARK_UNDER_LINE + (i++));
						srmPaymentItem.setStatus(IAtomsConstants.DATA_STATUS_CREATE);
						srmPaymentItem.setPaymentId(paymentId);
						srmPaymentItem.setPaymentItem(IAtomsConstants.PAYMENT_ITEM_SUPPLIES);
						srmPaymentItem.setUpdatedById(logonUser.getId());
						srmPaymentItem.setUpdatedByName(logonUser.getName());
						srmPaymentItem.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						this.srmPaymentItemDAO.getDaoSupport().saveOrUpdate(srmPaymentItem);
					}
					
					tempId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_SRM_PAYMENT_ITEM);
					i = 0;
					for (SrmPaymentItemDTO srmPaymentItemDTO : assetItemDTOs) {
						if (IAtomsConstants.YES.equals(srmPaymentItemDTO.getIsPay())) {
							srmPaymentItem = (SrmPaymentItem) transformer.transform(srmPaymentItemDTO, new SrmPaymentItem());
							srmPaymentItem.setItemId(tempId + IAtomsConstants.MARK_UNDER_LINE + (i++));
							srmPaymentItem.setPaymentId(paymentId);
							srmPaymentItem.setStatus(IAtomsConstants.DATA_STATUS_CREATE);
							srmPaymentItem.setPaymentItem(IAtomsConstants.PAYMENT_ITEM_ASSET);
							srmPaymentItem.setUpdatedById(logonUser.getId());
							srmPaymentItem.setUpdatedByName(logonUser.getName());
							srmPaymentItem.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
							this.srmPaymentItemDAO.getDaoSupport().saveOrUpdate(srmPaymentItem);
						}
					}
					
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
				} else {
					//核檢資料是否已被修改
					Long updatedDate = claimWorkFormDTO.getOldUpdatedDate();
					if (updatedDate != null) {
						List<SrmPaymentInfoDTO> infoDTOs = this.srmPaymentItemDAO.listBy(paymentId, Boolean.FALSE);
						if (!CollectionUtils.isEmpty(infoDTOs)) {
							if (updatedDate != infoDTOs.get(0).getUpdatedDate().getTime()) {
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_ALERDAY_UPDATE);
								sessionContext.setReturnMessage(msg);
								return sessionContext;
							}
						} else {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_ALERDAY_UPDATE);
							sessionContext.setReturnMessage(msg);
							return sessionContext;
						}
					}
					//獲取耗材的信息
					SrmPaymentItem srmPaymentItem = null;
					//修改求償主表的信息
					SrmPaymentInfo paymentInfo = this.paymentInfoDAO.findByPrimaryKey(SrmPaymentInfo.class, paymentId);
					if (paymentInfo != null) {
						paymentInfo.setUpdatedById(logonUser.getId());
						paymentInfo.setUpdatedByName(logonUser.getName());
						paymentInfo.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					}
					//獲取數據庫已存在的耗材資料
					List<SrmPaymentItemDTO> srmPaymentItemOldDTOs = this.srmPaymentItemDAO.getItemIds(paymentId, IAtomsConstants.PAYMENT_ITEM_SUPPLIES);
					Boolean isExist = false;
					for (SrmPaymentItemDTO srmPaymentItemOldDTO : srmPaymentItemOldDTOs) {
						isExist = false;
						for (SrmPaymentItemDTO srmPaymentItemDTO : peripheralSuppliesList) {
							if (srmPaymentItemOldDTO.getItemId().equals(srmPaymentItemDTO.getItemId())) {
								isExist = true;
								break;
							}
						}
						//如果isExist為FALSE，則表示需要刪除該筆耗材資料
						if (!isExist) {
							srmPaymentItem = this.srmPaymentItemDAO.findByPrimaryKey(SrmPaymentItem.class, srmPaymentItemOldDTO.getItemId());
							this.srmPaymentItemDAO.delete(srmPaymentItem);
						}
					}
					//保存耗材資料
					for (SrmPaymentItemDTO srmPaymentItemDTO : peripheralSuppliesList) {
						if (StringUtils.hasText(srmPaymentItemDTO.getItemId())) {
							srmPaymentItem = this.srmPaymentItemDAO.findByPrimaryKey(SrmPaymentItem.class, srmPaymentItemDTO.getItemId());
							srmPaymentItem.setSuppliesType(srmPaymentItemDTO.getSuppliesType());
							srmPaymentItem.setSuppliesAmount(srmPaymentItemDTO.getSuppliesAmount());
							srmPaymentItem.setItemName(srmPaymentItemDTO.getItemName());
							srmPaymentItem.setPaymentReason(srmPaymentItemDTO.getPaymentReason());
							srmPaymentItem.setReasonDetail(srmPaymentItemDTO.getReasonDetail());
						} else {
							srmPaymentItem = (SrmPaymentItem) transformer.transform(srmPaymentItemDTO, new SrmPaymentItem());
							srmPaymentItem.setItemId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_SRM_PAYMENT_ITEM));
							srmPaymentItem.setStatus(claimWorkFormDTO.getSaveStatus());
							srmPaymentItem.setPaymentId(paymentId);
							srmPaymentItem.setPaymentItem(IAtomsConstants.PAYMENT_ITEM_SUPPLIES);
						}
						srmPaymentItem.setUpdatedById(logonUser.getId());
						srmPaymentItem.setUpdatedByName(logonUser.getName());
						srmPaymentItem.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						this.srmPaymentItemDAO.getDaoSupport().saveOrUpdate(srmPaymentItem);
					}
					
					//處理設備清單資料
					for (SrmPaymentItemDTO srmPaymentItemDTO : assetItemDTOs) {
						srmPaymentItem = this.srmPaymentItemDAO.findByPrimaryKey(SrmPaymentItem.class, srmPaymentItemDTO.getItemId());
						if (srmPaymentItem != null) {
							if (IAtomsConstants.YES.equals(srmPaymentItemDTO.getIsPay())) {
								srmPaymentItem.setIsAskPay(srmPaymentItemDTO.getIsAskPay());
								srmPaymentItem.setIsPay(IAtomsConstants.YES);
								srmPaymentItem.setPaymentReason(srmPaymentItemDTO.getPaymentReason());
								srmPaymentItem.setReasonDetail(srmPaymentItemDTO.getReasonDetail());
								srmPaymentItem.setUpdatedById(logonUser.getId());
								srmPaymentItem.setUpdatedByName(logonUser.getName());
								srmPaymentItem.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
								this.srmPaymentItemDAO.getDaoSupport().saveOrUpdate(srmPaymentItem);
							} else {
								this.srmPaymentItemDAO.delete(srmPaymentItem);
							}
						}
					}
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()});
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOG.error("save() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()}, e);
		}  catch (Exception e) {
			LOG.error("save(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#queryClaimCourse(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext queryPaymentTranscation(SessionContext sessionContext) throws ServiceException {
		try {
			List<SrmPaymentTranscationDTO> srmPaymentTranscationDTOs = null;
			PaymentFormDTO claimWorkFormDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
			String paymentId = claimWorkFormDTO.getPaymentId();
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(0));
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, new ArrayList<SrmPaymentTranscationDTO>());
			Message msg = null;
			if (StringUtils.hasText(paymentId)) {
				srmPaymentTranscationDTOs = this.paymentTranscationDAO.listBy(paymentId);
				if (!CollectionUtils.isEmpty(srmPaymentTranscationDTOs)) {
					for (SrmPaymentTranscationDTO srmPaymentTranscationDTO : srmPaymentTranscationDTOs) {
						if (StringUtils.hasText(srmPaymentTranscationDTO.getStatus())) {
							String[] status = srmPaymentTranscationDTO.getStatus().split(IAtomsConstants.MARK_CENTER_LINE);
							if (status.length > 1) {
								srmPaymentTranscationDTO.setStatus(i18NUtil.getName(status[0]) + IAtomsConstants.MARK_CENTER_LINE + i18NUtil.getName(status[1]));
							} else {
								srmPaymentTranscationDTO.setStatus(i18NUtil.getName(srmPaymentTranscationDTO.getStatus()));
							}
						}
					}
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, srmPaymentTranscationDTOs.get(0));
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, srmPaymentTranscationDTOs);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.QUERY_FAILURE);
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			sessionContext.setResponseResult(claimWorkFormDTO);
		} catch (DataAccessException e) {
			LOG.error("query() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}  catch (Exception e) {
			LOG.error("query(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#queryDTID(cafe.core.context.SessionContext)
	 */
	public SessionContext queryDTID(SessionContext sessionContext) throws SecurityException {
		try {
			PaymentFormDTO claimWorkFormDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
			Message msg = null;
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, 0);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, new ArrayList<SrmHistoryCaseHandleInfoDTO>());
			if (claimWorkFormDTO != null) {
				Integer count = this.caseHandleInfoDAO.getCount(null, claimWorkFormDTO.getDtidQueryCompanyId(), 
						claimWorkFormDTO.getDtidQueryMerchantCode(), claimWorkFormDTO.getDtidQueryDtid(), 
						claimWorkFormDTO.getDtidQueryTid(), claimWorkFormDTO.getDtidQuerySerialNumber(), claimWorkFormDTO.getDtidQueryCaseId());	
				if (count == null || count.intValue() == 0) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				} else {
					List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = this.caseHandleInfoDAO.listBy(null, claimWorkFormDTO.getDtidQueryCompanyId(), 
							claimWorkFormDTO.getDtidQueryMerchantCode(), claimWorkFormDTO.getDtidQueryDtid(), 
							claimWorkFormDTO.getDtidQueryTid(), claimWorkFormDTO.getDtidQuerySerialNumber(), claimWorkFormDTO.getDtidQueryCaseId(), 
							claimWorkFormDTO.getSort(), claimWorkFormDTO.getOrder(), claimWorkFormDTO.getRows(), claimWorkFormDTO.getPage());
					if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
						//總筆數放入formDTO
						claimWorkFormDTO.getPageNavigation().setRowCount(count);
						map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, count);
						map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, caseHandleInfoDTOs);
						sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS));
					} else {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.QUERY_FAILURE);
					}
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.QUERY_FAILURE);
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(claimWorkFormDTO);
		} catch (DataAccessException e) {
			LOG.error("query() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}  catch (Exception e) {
			LOG.error("query(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#getPayInfo(cafe.core.context.MultiParameterInquiryContext)
	 */
	public SrmCaseHandleInfoDTO getPayInfo(MultiParameterInquiryContext param) throws SecurityException {
		try {
			if (param != null) {
				String customerId = (String) param.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
				String caseId = (String) param.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
				String dtid = (String) param.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
				if (StringUtils.hasText(caseId) || StringUtils.hasText(dtid)) {
					List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = this.caseHandleInfoDAO.listBy(null, customerId, 
							null, dtid, null, null, caseId, SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), null, -1, -1);
					if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
						List<SrmPaymentItemDTO> paymentItemDTOs = new ArrayList<SrmPaymentItemDTO>();
						SrmPaymentItemDTO srmPaymentItemDTO = null;
						for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : caseHandleInfoDTOs) {
							srmPaymentItemDTO = new SrmPaymentItemDTO();
							srmPaymentItemDTO.setItemName(srmCaseHandleInfoDTO.getAssetName());
							srmPaymentItemDTO.setSerialNumber(srmCaseHandleInfoDTO.getSerialNumber());
							srmPaymentItemDTO.setContractId(srmCaseHandleInfoDTO.getContractId());
							srmPaymentItemDTO.setAssetId(srmCaseHandleInfoDTO.getAssetTypeId());
							srmPaymentItemDTO.setContractCode(srmCaseHandleInfoDTO.getContractCode());
							paymentItemDTOs.add(srmPaymentItemDTO);
						}
						caseHandleInfoDTOs.get(0).setSrmPaymentItemDTOs(paymentItemDTOs);
						return caseHandleInfoDTOs.get(0);
					}
				}
			}
		} catch (DataAccessException e) {
			LOG.error("getPayInfo() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}  catch (Exception e) {
			LOG.error("getPayInfo(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return null;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#getContractAssetList(cafe.core.context.SessionContext)
	 */
	public SessionContext initSuppliesValues(SessionContext sessionContext) throws SecurityException {
		Message msg = null;
		try {
			PaymentFormDTO formDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
			//獲取合約ID
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(0));
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, new ArrayList<SrmPaymentItemDTO>());
			sessionContext.setReturnMessage(msg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOG.error("initSuppliesValues() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error("initSuppliesValues() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#send(cafe.core.context.SessionContext)
	 */
	public SessionContext send(SessionContext sessionContext) throws SecurityException {
		Message msg = null;
		try {
			PaymentFormDTO formDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
			List<SrmPaymentItemDTO> paymentItemDTOs = formDTO.getPeripheralSuppliesList();
			StringBuffer transcation = new StringBuffer();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			SrmPaymentItem srmPaymentItem = null;
			String action = "";
			String status = "";
			if (!CollectionUtils.isEmpty(paymentItemDTOs)){
				status = this.checkPaymentItemStatus(paymentItemDTOs, null);
				if (!StringUtils.hasText(status)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PAYMENT_STATUS_ERROR);
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
				if (!status.equals(IAtomsConstants.DATA_STATUS_CREATE) && !status.equals(IAtomsConstants.DATA_STATUS_BACK)) {
					status = "";
				}
				if (!checkPaymentItemStatusUpdate(paymentItemDTOs, status)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_ALERDAY_UPDATE);
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
				if (msg == null) {
					//如果設備狀態為--待維修
					if (IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED.equals(paymentItemDTOs.get(0).getStatus())) {
						//記錄當前求償狀態-送出
						action = IAtomsConstants.PAY_ACTION.SEND.getCode();
						//記錄動作后求償資料狀態-待確認金額
						status = IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED;
						for (int i = 0; i < paymentItemDTOs.size(); i++) {
							SrmPaymentItemDTO srmPaymentItemDTO = paymentItemDTOs.get(i);
							srmPaymentItem = this.srmPaymentItemDAO.findByPrimaryKey(SrmPaymentItem.class, srmPaymentItemDTO.getItemId());
							if (srmPaymentItem != null) {
								//記錄歷史進程資料
								transcation.append(srmPaymentItemDTO.getItemName()).append(IAtomsConstants.MARK_MIDDLE_LINE)
									.append(i18NUtil.getName(IAtomsConstants.PAYMENT_CHECK_RESULT))
									.append(IAtomsConstants.MARK_COLON).append(srmPaymentItemDTO.getCheckResult()).append(IAtomsConstants.MARK_SEPARATOR).append(IAtomsConstants.MARK_SPACE)
									.append(i18NUtil.getName(IAtomsConstants.PAYMENT_CHECK_USER)).append(IAtomsConstants.MARK_COLON)
									.append(srmPaymentItemDTO.getCheckUser()).append(IAtomsConstants.MARK_SEMICOLON).append(IAtomsConstants.MARK_WRAP);
								srmPaymentItem.setStatus(IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED);
								srmPaymentItem.setCheckResult(srmPaymentItemDTO.getCheckResult());
								srmPaymentItem.setCheckUser(srmPaymentItemDTO.getCheckUser());
								srmPaymentItem.setUpdatedById(logonUser.getId());
								srmPaymentItem.setUpdatedByName(logonUser.getName());
								srmPaymentItem.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
								srmPaymentItem.setBackDesc(null);
								this.srmPaymentItemDAO.getDaoSupport().saveOrUpdate(srmPaymentItem);
								//增加一筆歷史進程資料
								if (i + 1 == paymentItemDTOs.size() || !srmPaymentItemDTO.getPaymentId().equals(paymentItemDTOs.get(i + 1).getPaymentId())) {
									saveSrmPaymentTranscation(srmPaymentItemDTO.getPaymentId(), action, status, transcation.substring(0, transcation.length()-1), logonUser);
									transcation = new StringBuffer();
								}
							}
						}
						try{
							this.sendMail(paymentItemDTOs, status, Boolean.FALSE, Boolean.FALSE);
							this.sendMail(paymentItemDTOs, status, Boolean.FALSE, Boolean.TRUE);
						} catch (Exception e) {
							LOG.debug(".send() --> send() is error... ");
						}
						//如果狀態為代確認金額
					} else if (IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED.equals(paymentItemDTOs.get(0).getStatus())) {
						//記錄當前求償狀態-送出
						action = IAtomsConstants.PAY_ACTION.SEND.getCode();
						//記錄動作后求償資料狀態-待償確認
						status = IAtomsConstants.DATA_STATUS_WAIT_CLAIM_CONFIRM;
						for (int i = 0; i < paymentItemDTOs.size(); i++) {
							SrmPaymentItemDTO srmPaymentItemDTO = paymentItemDTOs.get(i);
							srmPaymentItem = this.srmPaymentItemDAO.findByPrimaryKey(SrmPaymentItem.class, srmPaymentItemDTO.getItemId());
							if (srmPaymentItem != null) {
								//記錄歷史進程資料
								transcation.append(srmPaymentItemDTO.getItemName()).append(IAtomsConstants.MARK_MIDDLE_LINE)
									.append(i18NUtil.getName(IAtomsConstants.PAYMENT_FEE))
									.append(IAtomsConstants.MARK_COLON).append(srmPaymentItemDTO.getFee()).append(IAtomsConstants.MARK_SEPARATOR).append(IAtomsConstants.MARK_SPACE)
									.append(i18NUtil.getName(IAtomsConstants.PAYMENT_FEE_DESC)).append(IAtomsConstants.MARK_COLON)
									.append(srmPaymentItemDTO.getFeeDesc()).append(IAtomsConstants.MARK_SEMICOLON).append(IAtomsConstants.MARK_WRAP);
								srmPaymentItem.setStatus(IAtomsConstants.DATA_STATUS_WAIT_CLAIM_CONFIRM);
								if (StringUtils.hasText(srmPaymentItemDTO.getFee())) {
									srmPaymentItem.setFee(srmPaymentItemDTO.getFee());
								} else {
									srmPaymentItem.setFee(null);
								}
								srmPaymentItem.setFeeDesc(srmPaymentItemDTO.getFeeDesc());
								srmPaymentItem.setUpdatedById(logonUser.getId());
								srmPaymentItem.setUpdatedByName(logonUser.getName());
								srmPaymentItem.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
								this.srmPaymentItemDAO.getDaoSupport().saveOrUpdate(srmPaymentItem);
								//增加一筆歷史進程資料
								if (i + 1 == paymentItemDTOs.size() || !srmPaymentItemDTO.getPaymentId().equals(paymentItemDTOs.get(i + 1).getPaymentId())) {
									saveSrmPaymentTranscation(srmPaymentItemDTO.getPaymentId(), action, status, transcation.substring(0, transcation.length()-1), logonUser);
									transcation = new StringBuffer();
								}
							}
						}
						try{
							this.sendMail(paymentItemDTOs, status, Boolean.FALSE, Boolean.FALSE);
						} catch (Exception e) {
							LOG.debug(".send() --> send() is error... ");
						}
						
						//設備狀態為新增
					} else if (IAtomsConstants.DATA_STATUS_CREATE.equals(paymentItemDTOs.get(0).getStatus()) || IAtomsConstants.DATA_STATUS_BACK.equals(paymentItemDTOs.get(0).getStatus())){
						//記錄當前求償狀態-送出
						action = IAtomsConstants.PAY_ACTION.SEND.getCode();
						//記錄動作后求償資料狀態-待維修
						Boolean isAllLoss = Boolean.TRUE;
						Map<String, String> paymentReasonMap = new HashMap<String, String>();
						SrmPaymentItemDTO srmPaymentItemDTO = null;
						StringBuffer itemIds = new StringBuffer();
						for (int i = 0; i < paymentItemDTOs.size(); i++) {
							srmPaymentItemDTO = paymentItemDTOs.get(i);
							if (srmPaymentItemDTO != null) {
								if (!IAtomsConstants.PAYMENT_REASON.LOSS.getCode().equals(srmPaymentItemDTO.getPaymentReason())) {
									isAllLoss = Boolean.FALSE;
								}
								//增加一筆歷史進程資料
								if (i + 1 == paymentItemDTOs.size() || !srmPaymentItemDTO.getPaymentId().equals(paymentItemDTOs.get(i + 1).getPaymentId())) {
									if (isAllLoss) {
										paymentReasonMap.put(srmPaymentItemDTO.getPaymentId(), IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED);
									} else {
										paymentReasonMap.put(srmPaymentItemDTO.getPaymentId(), IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED);
									}
									isAllLoss = Boolean.TRUE;
								}
							}
							itemIds.append(IAtomsConstants.MARK_QUOTES).append(srmPaymentItemDTO.getItemId()).append(IAtomsConstants.MARK_QUOTES)
								.append(IAtomsConstants.MARK_SEPARATOR);
						}
						List<SrmPaymentItemDTO> lossPaymentItemDTOs = new ArrayList<SrmPaymentItemDTO>();
						paymentItemDTOs = this.srmPaymentItemDAO.getPaymentItemDTOByItemId(itemIds.substring(0, itemIds.length() - 1).toString(), null);
						for (int i = 0; i < paymentItemDTOs.size(); i++) {
							srmPaymentItemDTO = paymentItemDTOs.get(i);
							srmPaymentItem = this.srmPaymentItemDAO.findByPrimaryKey(SrmPaymentItem.class, srmPaymentItemDTO.getItemId());
							if (srmPaymentItem != null) {
								//記錄歷史進程資料
								transcation.append(srmPaymentItemDTO.getItemName()).append(IAtomsConstants.MARK_MIDDLE_LINE)
									.append(i18NUtil.getName(IAtomsConstants.PAYMENT_SEND)).append(IAtomsConstants.MARK_SEMICOLON).append(IAtomsConstants.MARK_WRAP);
								srmPaymentItem.setStatus(paymentReasonMap.get(srmPaymentItemDTO.getPaymentId()));
								srmPaymentItem.setUpdatedById(logonUser.getId());
								srmPaymentItem.setUpdatedByName(logonUser.getName());
								srmPaymentItem.setBackDesc("");
								srmPaymentItem.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
								this.srmPaymentItemDAO.getDaoSupport().saveOrUpdate(srmPaymentItem);
								if (IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED.equals(paymentReasonMap.get(srmPaymentItemDTO.getPaymentId()))) {
									lossPaymentItemDTOs.add(srmPaymentItemDTO);
								}
								//增加一筆歷史進程資料
								if (i + 1 == paymentItemDTOs.size() || !srmPaymentItemDTO.getPaymentId().equals(paymentItemDTOs.get(i + 1).getPaymentId())) {
									saveSrmPaymentTranscation(srmPaymentItemDTO.getPaymentId(), action, paymentReasonMap.get(srmPaymentItemDTO.getPaymentId()), transcation.substring(0, transcation.length()-1), logonUser);
									transcation = new StringBuffer();
								}
							}
						}
						try{
							if (CollectionUtils.isEmpty(lossPaymentItemDTOs)) {
								this.sendMail(paymentItemDTOs, IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED, Boolean.FALSE, Boolean.FALSE);
							} else {
								for (SrmPaymentItemDTO dto : lossPaymentItemDTOs) {
									for (SrmPaymentItemDTO paymentItemDTO : paymentItemDTOs) {
										if (StringUtils.hasText(dto.getItemId()) && dto.getItemId().equals(paymentItemDTO.getItemId())) {
											paymentItemDTOs.remove(paymentItemDTO);
											break;
										}
									}
								}
								this.sendMail(lossPaymentItemDTOs, IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED, Boolean.FALSE, Boolean.FALSE);
								if (!CollectionUtils.isEmpty(paymentItemDTOs)) {
									this.sendMail(paymentItemDTOs, IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED, Boolean.FALSE, Boolean.FALSE);
								}
							}
						} catch (Exception e) {
							LOG.debug(".back() --> send() is error... ");
						}
					}
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SEND_SUCCESS);
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SEND_FAILURE);
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOG.error("initSuppliesValues() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SEND_FAILURE, e);
		} catch (Exception e) {
			LOG.error("initSuppliesValues() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SEND_FAILURE, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#lock(cafe.core.context.SessionContext)
	 */
	public SessionContext lock(SessionContext sessionContext) throws SecurityException {
		String message = null;
		String json = null;
		try {
			Message msg = null;
			PaymentFormDTO formDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
			//獲取需要保存信息列表
			List<SrmPaymentItemDTO> paymentItemDTOs = formDTO.getPeripheralSuppliesList();
			SrmPaymentItem srmPaymentItem = null;
			StringBuffer transcation = new StringBuffer();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			Map resultMap = new HashMap();
			if (!CollectionUtils.isEmpty(paymentItemDTOs)) {
				//核檢當前所選的資料，狀態是否一致
				String status = this.checkPaymentItemStatus(paymentItemDTOs, null);
				if (!StringUtils.hasText(status)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PAYMENT_STATUS_ERROR);
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
				if (!checkPaymentItemStatusUpdate(paymentItemDTOs, null)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_ALERDAY_UPDATE);
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
				//Task #3360
				Map<String, List<SrmPaymentItemDTO>> paymentDtoMap = new HashMap<String, List<SrmPaymentItemDTO>>();
				List<SrmPaymentItemDTO> tempDto = null;
				for (int i = 0; i < paymentItemDTOs.size(); i++) {
					if (paymentDtoMap.get(paymentItemDTOs.get(i).getPaymentId()) == null) {
						tempDto = new ArrayList<SrmPaymentItemDTO>();
					} else {
						tempDto = paymentDtoMap.get(paymentItemDTOs.get(i).getPaymentId());
					}
					tempDto.add(paymentItemDTOs.get(i));
					paymentDtoMap.put(paymentItemDTOs.get(i).getPaymentId(), tempDto);
				}
				Integer price = 0;
				SrmPaymentItem paymentItem = null;
				SrmPaymentInfoDTO infoDTO = null;
				String caseCategory = null;
				PayMentCMSDTO payMentCMSDTO = null;
				List<PayMentCMSDTO> payMentCMSDTOs = new ArrayList<PayMentCMSDTO>();
				String api = null;
				ReceiveJobNoticeDTO jobNoticeDTO = null;
				Gson gson = new GsonBuilder().create();
				ReceiveJobNoticeDTO resultMsg = new ReceiveJobNoticeDTO();
				StringBuffer errorMsg = new StringBuffer();
				Map<String, List<SrmPaymentItemDTO>> successDto = new HashMap<String, List<SrmPaymentItemDTO>>();
				//Task #3336 call CMS API Task #3519
				Map<String, String> cmsResultMap = null;
				//Task #3519
				String apiLogResult = null;
				//Task #3519 存儲call cms 成功的案件 key 為 caseid， value 為 下行電文
		    	Map<String, String> callCmsMap = new HashMap<String, String>();
				for(Map.Entry<String, List<SrmPaymentItemDTO>> map: paymentDtoMap.entrySet()) {
					payMentCMSDTOs = new ArrayList<PayMentCMSDTO>();
					//查詢求償對應的案件信息，判斷是否爲CMS案件。以及案件類別是否爲拆機、報修
					infoDTO = this.paymentInfoDAO.getPaymentCaseInfo(map.getKey());
					if (infoDTO != null) {
						if (IAtomsConstants.YES.equals(infoDTO.getCmsCase())
								&& (IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(infoDTO.getCaseCategory())
										|| IAtomsConstants.CASE_CATEGORY.REPAIR.getCode().equals(infoDTO.getCaseCategory()))) {
							caseCategory = infoDTO.getCaseCategory();
							if (IAtomsConstants.CASE_CATEGORY.REPAIR.getCode().equals(caseCategory)
									&& (infoDTO.getDispatchDate() == null && IAtomsConstants.YES.equals(infoDTO.getIsIatomsCreateCms()))) {
								successDto.put(map.getKey(), map.getValue());
								continue;
							}
						} else {
							successDto.put(map.getKey(), map.getValue());
							continue;
						}
					}
					price = 0;
					Integer suppliesAmount = null;
					//統計需求償以及求償方式爲特店賠償的金額
					for (SrmPaymentItemDTO dto : map.getValue()) {
						if (IAtomsConstants.YES.equals(dto.getIsAskPay()) && IAtomsConstants.MERCHANT_PAYMENT.equals(dto.getPaymentType())) {
							paymentItem = this.srmPaymentItemDAO.findByPrimaryKey(SrmPaymentItem.class, dto.getItemId());
							suppliesAmount = paymentItem.getSuppliesAmount();
							payMentCMSDTO = new PayMentCMSDTO(dto.getPaymentItemName(), dto.getItemName(), 
									suppliesAmount == null?IAtomsConstants.PAYMENT_ITEM_ASSET.equals(paymentItem.getPaymentItem())?1:0:Integer.valueOf(suppliesAmount), 
											StringUtils.hasText(paymentItem.getFee())?Integer.valueOf(paymentItem.getFee()):0);
							payMentCMSDTOs.add(payMentCMSDTO);
							price += StringUtils.hasText(paymentItem.getFee())?Integer.valueOf(paymentItem.getFee()):0;
						}
					}
					if (IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(caseCategory)) {
						api = "APIOP005";
						jobNoticeDTO = new ReceiveJobNoticeDTO(price == 0?"08":"07", infoDTO.getCaseId(), infoDTO.getDtid(), 
								map.getKey(), price == 0 ? null:payMentCMSDTOs);
					} else {
						api = "APIOP004";
						jobNoticeDTO = new ReceiveJobNoticeDTO(price == 0?"13":"07", infoDTO.getMerchantCode(), infoDTO.getCaseId(), infoDTO.getDtid(), 
								map.getKey(), price == 0 ? null:payMentCMSDTOs);
					}
					json = gson.toJson(jobNoticeDTO);
					LOG.debug("paymentService --> lock() --> ", "json=" + json);
					cmsResultMap =  NetUtil.sendHtppsNew(api, json);
					LOG.debug("paymentService --> lock() --> ", "message=" + message);
					if (map != null && cmsResultMap.containsKey(IAtomsConstants.PARAM_ACTION_RESULT)) {
						message = cmsResultMap.get(IAtomsConstants.PARAM_ACTION_RESULT);
					}
					if (map != null && cmsResultMap.containsKey(IAtomsConstants.API_OUT_PUT) && "true".endsWith(cmsResultMap.get(IAtomsConstants.API_OUT_PUT))) {
						apiLogResult = "true";
					} else {
						apiLogResult = "false";
					}
					LOG.debug("paymentService --> lock() --> ", "message=" + message);
					//上行(RQ)
					this.insertApiLog(IAtomsConstants.API_RQ, map.getKey(), null, logonUser, apiLogResult, null, json);
					if (StringUtils.hasText(message)) {
						Object responseJson = (Object) gson.fromJson(message, Object.class);
						resultMsg = (ReceiveJobNoticeDTO) gson.fromJson(responseJson.toString(), new TypeToken<ReceiveJobNoticeDTO>(){}.getType());
						if (resultMsg.getRESULT().equals("True")) {
							successDto.put(map.getKey(), map.getValue());
							callCmsMap.put(map.getKey(), message);
						} else {
							errorMsg.append(i18NUtil.getName(IAtomsMessageCode.PARAM_CMS_PAYMENT_ERROR, new String[]{map.getKey(), resultMsg.getRESULT_MSG()}, null)).append("</br>");
							apiLogResult = "false";
							//下行(RS)
							this.insertApiLog(IAtomsConstants.API_RS, map.getKey(), null, logonUser, apiLogResult, resultMsg.getRESULT_MSG(), message);
						}
					} else {
						errorMsg.append(i18NUtil.getName(IAtomsMessageCode.PARAM_CMS_PAYMENT_ERROR, new String[]{map.getKey(), "cms請求失敗"}, null)).append("</br>");
						apiLogResult = "false";
						//下行(RS)
						this.insertApiLog(IAtomsConstants.API_RS, map.getKey(), null, logonUser, apiLogResult, null, "");
					}
				}
				List<SrmPaymentItemDTO> successDtos = new ArrayList<SrmPaymentItemDTO>();
				//進行鎖定操作
				for(Map.Entry<String, List<SrmPaymentItemDTO>> map: successDto.entrySet()) {
					List<SrmPaymentItemDTO> itemDTOs = map.getValue();
					for (SrmPaymentItemDTO dto : itemDTOs) {
						successDtos.add(dto);
						srmPaymentItem = this.srmPaymentItemDAO.findByPrimaryKey(SrmPaymentItem.class, dto.getItemId());
						if (srmPaymentItem != null) {
							//記錄一筆求償歷程
							transcation.append(dto.getItemName()).append(IAtomsConstants.MARK_MIDDLE_LINE).append(i18NUtil.getName(IAtomsConstants.PAYMENT_IS_ASK_PAY))
								.append(IAtomsConstants.MARK_COLON).append((dto.getIsAskPay().equals(IAtomsConstants.YES))?i18NUtil.getName(IAtomsConstants.YES):i18NUtil.getName(IAtomsConstants.NO))
								.append(IAtomsConstants.MARK_SEPARATOR).append(IAtomsConstants.MARK_SPACE).append(i18NUtil.getName(IAtomsConstants.PAYMENT_PAYMENT_TYPE))
								.append(IAtomsConstants.MARK_COLON).append(dto.getPaymentTypeName())
								.append(IAtomsConstants.MARK_SEPARATOR).append(IAtomsConstants.MARK_SPACE).append(i18NUtil.getName(IAtomsConstants.PAYMENT_PAYMENT_TYPE_DESC)).append(IAtomsConstants.MARK_COLON)
								.append(dto.getPaymentTypeDesc()).append(IAtomsConstants.MARK_SEMICOLON).append(IAtomsConstants.MARK_WRAP);
							srmPaymentItem.setIsAskPay(dto.getIsAskPay());
							srmPaymentItem.setPaymentType(dto.getPaymentType());
							srmPaymentItem.setPaymentTypeDesc(dto.getPaymentTypeDesc());
							srmPaymentItem.setStatus(IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM);
							srmPaymentItem.setLockedDate(DateTimeUtils.getCurrentTimestamp());
							srmPaymentItem.setUpdatedById(logonUser.getId());
							srmPaymentItem.setUpdatedByName(logonUser.getName());
							srmPaymentItem.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
							this.srmPaymentItemDAO.getDaoSupport().saveOrUpdate(srmPaymentItem);
						}
					}
					//saveSrmPaymentTranscation(map.getKey(), IAtomsConstants.PAY_ACTION.LOCKING.getCode(), IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM, transcation.substring(0, transcation.length()-1), logonUser);
					SrmPaymentTranscation paymentTranscation = new SrmPaymentTranscation();
					//增加一筆歷史進程資料
					paymentTranscation.setTranscationId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_SRM_PAYMENT_TRANSCATION));
					paymentTranscation.setPaymentId(map.getKey());
					paymentTranscation.setAction(IAtomsConstants.PAY_ACTION.LOCKING.getCode());
					paymentTranscation.setStatus(IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM);
					paymentTranscation.setPaymentContent(transcation.substring(0, transcation.length()-1));
					paymentTranscation.setUpdatedById(logonUser.getId());
					paymentTranscation.setUpdatedByName(logonUser.getName());
					paymentTranscation.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					this.paymentTranscationDAO.getDaoSupport().saveOrUpdate(paymentTranscation);
					//Task #3519 存儲下行電文 要有案件歷程id
					if(callCmsMap.containsKey(map.getKey())){
						apiLogResult = "true";
						this.insertApiLog(IAtomsConstants.API_RS, map.getKey(), paymentTranscation.getTranscationId(), logonUser, apiLogResult, null, callCmsMap.get(map.getKey()));
					}
					transcation = new StringBuffer();
				}
				/*for (int i = 0; i < paymentItemDTOs.size(); i++) {
					SrmPaymentItemDTO srmPaymentItemDTO = paymentItemDTOs.get(i);
					srmPaymentItem = this.srmPaymentItemDAO.findByPrimaryKey(SrmPaymentItem.class, srmPaymentItemDTO.getItemId());
					if (srmPaymentItem != null) {
						//記錄一筆求償歷程
						transcation.append(srmPaymentItemDTO.getItemName()).append(IAtomsConstants.MARK_MIDDLE_LINE).append(i18NUtil.getName(IAtomsConstants.PAYMENT_IS_ASK_PAY))
							.append(IAtomsConstants.MARK_COLON).append((srmPaymentItemDTO.getIsAskPay().equals(IAtomsConstants.YES))?i18NUtil.getName(IAtomsConstants.YES):i18NUtil.getName(IAtomsConstants.NO))
							.append(IAtomsConstants.MARK_SEPARATOR).append(IAtomsConstants.MARK_SPACE).append(i18NUtil.getName(IAtomsConstants.PAYMENT_PAYMENT_TYPE))
							.append(IAtomsConstants.MARK_COLON).append(srmPaymentItemDTO.getPaymentTypeName())
							.append(IAtomsConstants.MARK_SEPARATOR).append(IAtomsConstants.MARK_SPACE).append(i18NUtil.getName(IAtomsConstants.PAYMENT_PAYMENT_TYPE_DESC)).append(IAtomsConstants.MARK_COLON)
							.append(srmPaymentItemDTO.getPaymentTypeDesc()).append(IAtomsConstants.MARK_SEMICOLON).append(IAtomsConstants.MARK_WRAP);
						srmPaymentItem.setIsAskPay(srmPaymentItemDTO.getIsAskPay());
						srmPaymentItem.setPaymentType(srmPaymentItemDTO.getPaymentType());
						srmPaymentItem.setPaymentTypeDesc(srmPaymentItemDTO.getPaymentTypeDesc());
						srmPaymentItem.setStatus(IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM);
						srmPaymentItem.setLockedDate(DateTimeUtils.getCurrentTimestamp());
						srmPaymentItem.setUpdatedById(logonUser.getId());
						srmPaymentItem.setUpdatedByName(logonUser.getName());
						srmPaymentItem.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						this.srmPaymentItemDAO.getDaoSupport().saveOrUpdate(srmPaymentItem);
						//增加一筆歷史進程資料
						if (i + 1 == paymentItemDTOs.size() || !srmPaymentItemDTO.getPaymentId().equals(paymentItemDTOs.get(i + 1).getPaymentId())) {
							saveSrmPaymentTranscation(srmPaymentItemDTO.getPaymentId(), IAtomsConstants.PAY_ACTION.LOCKING.getCode(), IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM, transcation.substring(0, transcation.length()-1), logonUser);
							transcation = new StringBuffer();
						}
					}
				}*/
				if (errorMsg.length() > 0) {
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_INPUT_MESSAGE, new String[]{errorMsg.toString()});
					resultMap.put(IAtomsConstants.FIELD_CMS_RESULT, false);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, resultMap);
				} else {
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.LOCKING_SUCCESS);
				}
				try{
					this.sendMail(successDtos, IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM, Boolean.FALSE, Boolean.FALSE);
				} catch (Exception e) {
					LOG.debug(".lock() --> send() is error... ");
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.LOCKING_FAILURE);
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOG.error("paymentService --> lock() --> ", "message=" + message);
			LOG.error("paymentService --> lock() --> ", "json=" + json);
			LOG.error("lock() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.LOCKING_FAILURE, e);
		} catch (Exception e) {
			LOG.error("paymentService --> lock() --> ", "message=" + message);
			LOG.error("paymentService --> lock() --> ", "json=" + json);
			LOG.error("lock() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.LOCKING_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#back(cafe.core.context.SessionContext)
	 */
	public SessionContext back(SessionContext sessionContext) throws SecurityException {
		try {
			Message msg = null;
			PaymentFormDTO formDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
			List<SrmPaymentItemDTO> paymentItemDTOs = formDTO.getPeripheralSuppliesList();
			SrmPaymentItem srmPaymentItem = null;
			StringBuffer transcation = new StringBuffer();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			if (!CollectionUtils.isEmpty(paymentItemDTOs)) {
				if (!checkPaymentItemStatusUpdate(paymentItemDTOs, null)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_ALERDAY_UPDATE);
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
				//增加一筆歷史進程資料
				Map<String, String> paymentReasonMap = new HashMap<String, String>();
				SrmPaymentItemDTO srmPaymentItemDTO = null;
				Boolean isAllLoss = Boolean.TRUE;
				for (int i = 0; i < paymentItemDTOs.size(); i++) {
					srmPaymentItemDTO = paymentItemDTOs.get(i);
					if (srmPaymentItemDTO != null) {
						if (IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED.equals(srmPaymentItemDTO.getStatus())
								&&	!IAtomsConstants.PAYMENT_REASON.LOSS.getCode().equals(srmPaymentItemDTO.getPaymentReason())) {
							isAllLoss = Boolean.FALSE;
						}
						//增加一筆歷史進程資料
						if (i + 1 == paymentItemDTOs.size() || !srmPaymentItemDTO.getPaymentId().equals(paymentItemDTOs.get(i + 1).getPaymentId())) {
							if (IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED.equals(paymentItemDTOs.get(0).getStatus())) {
								paymentReasonMap.put(srmPaymentItemDTO.getPaymentId(), IAtomsConstants.DATA_STATUS_CREATE);
							} else if (IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED.equals(paymentItemDTOs.get(0).getStatus())) {
								if (isAllLoss) {
									paymentReasonMap.put(srmPaymentItemDTO.getPaymentId(), IAtomsConstants.DATA_STATUS_CREATE);
								} else {
									paymentReasonMap.put(srmPaymentItemDTO.getPaymentId(), IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED);
									isAllLoss = Boolean.TRUE;
								}
							} else if (IAtomsConstants.DATA_STATUS_WAIT_CLAIM_CONFIRM.equals(paymentItemDTOs.get(0).getStatus())) {
								paymentReasonMap.put(srmPaymentItemDTO.getPaymentId(), IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED);
							} else if (IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM.equals(paymentItemDTOs.get(0).getStatus())) {
								paymentReasonMap.put(srmPaymentItemDTO.getPaymentId(), IAtomsConstants.DATA_STATUS_WAIT_CLAIM_CONFIRM);
							} 
							
						}
					}
				}
				List<SrmPaymentItemDTO> lossPaymentItemDTOs = new ArrayList<SrmPaymentItemDTO>();
				for (int i = 0; i < paymentItemDTOs.size(); i++) {
					srmPaymentItemDTO = paymentItemDTOs.get(i);
					srmPaymentItem = this.srmPaymentItemDAO.findByPrimaryKey(SrmPaymentItem.class, srmPaymentItemDTO.getItemId());
					if (srmPaymentItem != null) {
						transcation.append(srmPaymentItemDTO.getItemName()).append(IAtomsConstants.MARK_MIDDLE_LINE)
							.append(i18NUtil.getName(IAtomsConstants.PAYMENT_BACK_DESC))
							.append(IAtomsConstants.MARK_COLON).append(srmPaymentItemDTO.getBackDesc())
							.append(IAtomsConstants.MARK_SEMICOLON).append(IAtomsConstants.MARK_WRAP);
						srmPaymentItem.setBackDesc(srmPaymentItemDTO.getBackDesc());
						srmPaymentItem.setStatus(IAtomsConstants.DATA_STATUS_BACK + IAtomsConstants.MARK_MIDDLE_LINE + paymentReasonMap.get(srmPaymentItemDTO.getPaymentId()));
						srmPaymentItem.setUpdatedById(logonUser.getId());
						srmPaymentItem.setUpdatedByName(logonUser.getName());
						srmPaymentItem.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						this.srmPaymentItemDAO.getDaoSupport().saveOrUpdate(srmPaymentItem);
						if (IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED.equals(srmPaymentItemDTO.getStatus())) {
							if (IAtomsConstants.DATA_STATUS_CREATE.equals(paymentReasonMap.get(srmPaymentItemDTO.getPaymentId()))) {
								lossPaymentItemDTOs.add(srmPaymentItemDTO);
							}
						}
						//增加一筆歷史進程資料
						if (i + 1 == paymentItemDTOs.size() || !srmPaymentItemDTO.getPaymentId().equals(paymentItemDTOs.get(i + 1).getPaymentId())) {
							saveSrmPaymentTranscation(srmPaymentItem.getPaymentId(), IAtomsConstants.PAY_ACTION.BACK.getCode(),  IAtomsConstants.DATA_STATUS_BACK + IAtomsConstants.MARK_MIDDLE_LINE + paymentReasonMap.get(srmPaymentItemDTO.getPaymentId()), transcation.substring(0, transcation.length()-1), logonUser);
							transcation = new StringBuffer();
						}
					}
				}
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.BACK_SUCCESS);
				try{
					if (IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED.equals(paymentItemDTOs.get(0).getStatus())) {
						if (CollectionUtils.isEmpty(lossPaymentItemDTOs)) {
							this.sendMail(paymentItemDTOs, paymentReasonMap.get(paymentItemDTOs.get(0).getPaymentId()), Boolean.TRUE, Boolean.FALSE);
						} else {
							for (SrmPaymentItemDTO dto : lossPaymentItemDTOs) {
								for (SrmPaymentItemDTO paymentItemDTO : paymentItemDTOs) {
									if (StringUtils.hasText(dto.getItemId()) && dto.getItemId().equals(paymentItemDTO.getItemId())) {
										paymentItemDTOs.remove(paymentItemDTO);
										break;
									}
								}
							}
							this.sendMail(lossPaymentItemDTOs, paymentReasonMap.get(lossPaymentItemDTOs.get(0).getPaymentId()), Boolean.TRUE, Boolean.FALSE);
							if (!CollectionUtils.isEmpty(paymentItemDTOs)) {
								this.sendMail(paymentItemDTOs, paymentReasonMap.get(paymentItemDTOs.get(0).getPaymentId()), Boolean.TRUE, Boolean.FALSE);
							}
						}
					} else {
						this.sendMail(paymentItemDTOs, paymentReasonMap.get(paymentItemDTOs.get(0).getPaymentId()), Boolean.TRUE, Boolean.FALSE);
					}
				} catch (Exception e) {
					LOG.debug(".back() --> send() is error... ");
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.BACK_FAILURE);
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOG.error("back() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.BACK_FAILURE, e);
		} catch (Exception e) {
			LOG.error("back() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.BACK_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#complete(cafe.core.context.SessionContext)
	 */
	public SessionContext complete(SessionContext sessionContext) throws SecurityException {
		try {
			Message msg = null;
			PaymentFormDTO formDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
			List<SrmPaymentItemDTO> paymentItemDTOs = formDTO.getPeripheralSuppliesList();
			SrmPaymentItem srmPaymentItem = null;
			StringBuffer transcation = new StringBuffer();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			if (!CollectionUtils.isEmpty(paymentItemDTOs)) {
				String status = null;
				status = this.checkPaymentItemStatus(paymentItemDTOs, null);
				if (!StringUtils.hasText(status)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PAYMENT_STATUS_ERROR);
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
				if (!checkPaymentItemStatusUpdate(paymentItemDTOs, null)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_ALERDAY_UPDATE);
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
				SimpleDateFormat formatter = new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
				String askDate = "";
				String desc = i18NUtil.getName(IAtomsConstants.PAYMENT_BACK_DESC);
				for (int i = 0; i < paymentItemDTOs.size(); i++) {
					SrmPaymentItemDTO srmPaymentItemDTO = paymentItemDTOs.get(i);
					srmPaymentItem = this.srmPaymentItemDAO.findByPrimaryKey(SrmPaymentItem.class, srmPaymentItemDTO.getItemId());
					if (srmPaymentItem != null) {
						paymentItemDTOs.get(i).setFee(srmPaymentItem.getFee());
						paymentItemDTOs.get(i).setFeeDesc(srmPaymentItem.getFeeDesc());
						if (srmPaymentItemDTO.getAskPayDate() != null) {
							srmPaymentItem.setAskPayDate(srmPaymentItemDTO.getAskPayDate());
							srmPaymentItem.setStatus(IAtomsConstants.DATA_STATUS_REQUEST_FUNDS);
							askDate = formatter.format(srmPaymentItemDTO.getAskPayDate());
							status = i18NUtil.getName(IAtomsConstants.DATA_STATUS_REQUEST_FUNDS);
						} else {
							srmPaymentItem.setStatus(IAtomsConstants.DATA_STATUS_ALREADY_CANCEL);
							askDate = "";
							status = i18NUtil.getName(IAtomsConstants.DATA_STATUS_ALREADY_CANCEL);
						}
						if (i18NUtil.getName(IAtomsConstants.DATA_STATUS_ALREADY_CANCEL).equals(status)) {
							transcation.append(srmPaymentItemDTO.getItemName()).append(IAtomsConstants.MARK_MIDDLE_LINE).append(status)
							.append(IAtomsConstants.MARK_SEPARATOR).append(IAtomsConstants.MARK_SPACE)
							.append(desc.substring(2, desc.length())).append(IAtomsConstants.MARK_COLON).append(srmPaymentItemDTO.getAskPayDesc())
							.append(IAtomsConstants.MARK_SEMICOLON).append(IAtomsConstants.MARK_WRAP);
						} else {
							transcation.append(srmPaymentItemDTO.getItemName()).append(IAtomsConstants.MARK_MIDDLE_LINE).append(status)
							.append(IAtomsConstants.MARK_MIDDLE_LINE).append(i18NUtil.getName(IAtomsConstants.PAYMENT_ASKPAY_DATE))
							.append(IAtomsConstants.MARK_COLON).append(askDate)
							.append(IAtomsConstants.MARK_SEPARATOR).append(IAtomsConstants.MARK_SPACE)
							.append(desc.substring(2, desc.length())).append(IAtomsConstants.MARK_COLON).append(srmPaymentItemDTO.getAskPayDesc())
							.append(IAtomsConstants.MARK_SEMICOLON).append(IAtomsConstants.MARK_WRAP);
						}
						srmPaymentItem.setAskPayDesc(srmPaymentItemDTO.getAskPayDesc());
						srmPaymentItem.setUpdatedById(logonUser.getId());
						srmPaymentItem.setUpdatedByName(logonUser.getName());
						srmPaymentItem.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						this.srmPaymentItemDAO.getDaoSupport().saveOrUpdate(srmPaymentItem);
						//增加一筆歷史進程資料
						if (i + 1 == paymentItemDTOs.size() || !srmPaymentItemDTO.getPaymentId().equals(paymentItemDTOs.get(i + 1).getPaymentId())) {
							saveSrmPaymentTranscation(srmPaymentItem.getPaymentId(), IAtomsConstants.PAY_ACTION.COMPLETE.getCode(), IAtomsConstants.DATA_STATUS_COMPLETE, transcation.substring(0, transcation.length()-1), logonUser);
							transcation = new StringBuffer();
						}
					}
				}
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.COMPLETE_SUCCESS);
				try{
					this.sendMail(paymentItemDTOs, IAtomsConstants.DATA_STATUS_REQUEST_FUNDS, Boolean.FALSE, Boolean.FALSE);
				} catch (Exception e) {
					LOG.debug(".back() --> send() is error... ");
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.COMPLETE_FAILURE);
			}
			sessionContext.setReturnMessage(msg);
		}catch (DataAccessException e) {
			LOG.error("complete() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.COMPLETE_FAILURE, e);
		} catch (Exception e) {
			LOG.error("complete() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.COMPLETE_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#repayment(cafe.core.context.SessionContext)
	 */
	public SessionContext repayment(SessionContext sessionContext) throws SecurityException {
		try {
			Message msg = null;
			PaymentFormDTO formDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
			List<SrmPaymentItemDTO> paymentItemDTOs = formDTO.getPeripheralSuppliesList();
			SrmPaymentItem srmPaymentItem = null;
			StringBuffer transcation = new StringBuffer();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			if (!CollectionUtils.isEmpty(paymentItemDTOs)) {
				if (!checkPaymentItemStatusUpdate(paymentItemDTOs, null)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_ALERDAY_UPDATE);
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
				SimpleDateFormat formatter = new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
				String serialNumber = null;
				for (SrmPaymentItemDTO srmPaymentItemDTO : paymentItemDTOs) {
					if (srmPaymentItemDTO.getCancelDate() == null && srmPaymentItemDTO.getRepaymentDate() == null) {
						continue;
					}
					serialNumber = srmPaymentItemDTO.getSerialNumber();
					if (StringUtils.hasText(serialNumber)) {					
						serialNumber = srmPaymentItemDTO.getSerialNumber();
						//核檢該設備序號下的設備是否為庫存狀態
						DmmRepositoryDTO dmmRepositoryDTO = this.repositoryDAO.getBySerialNumber(serialNumber);
						if (dmmRepositoryDTO != null) {
							if (!IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY.equals(dmmRepositoryDTO.getStatus())) {
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NOT_IS_REPERTORY, new String[]{srmPaymentItemDTO.getSerialNumber(), srmPaymentItemDTO.getItemName()});
								sessionContext.setReturnMessage(msg);
								return sessionContext;
							}
						}
					}
				}
				for (int i = 0; i < paymentItemDTOs.size(); i++) {
					SrmPaymentItemDTO srmPaymentItemDTO = paymentItemDTOs.get(i);
					srmPaymentItem = this.srmPaymentItemDAO.findByPrimaryKey(SrmPaymentItem.class, srmPaymentItemDTO.getItemId());
					if (srmPaymentItemDTO.getCancelDate() != null && srmPaymentItemDTO.getRepaymentDate() != null) {
						transcation.append(srmPaymentItemDTO.getItemName()).append(IAtomsConstants.MARK_MIDDLE_LINE).append(i18NUtil.getName(IAtomsConstants.PAYMENT_CANCEL_DATE))
						.append(IAtomsConstants.MARK_COLON).append(formatter.format(srmPaymentItemDTO.getCancelDate()))
						.append(IAtomsConstants.MARK_SEPARATOR).append(IAtomsConstants.MARK_SPACE).append(i18NUtil.getName(IAtomsConstants.PAYMENT_REPAYMENT_DATE))
						.append(IAtomsConstants.MARK_COLON).append(formatter.format(srmPaymentItemDTO.getRepaymentDate()))
						.append(IAtomsConstants.MARK_SEPARATOR).append(IAtomsConstants.MARK_SPACE).append(i18NUtil.getName(IAtomsConstants.PAYMENT_CANCEL_DESC))
						.append(IAtomsConstants.MARK_COLON).append(srmPaymentItemDTO.getCancelDesc())
						.append(IAtomsConstants.MARK_SEMICOLON).append(IAtomsConstants.MARK_WRAP);
						srmPaymentItem.setCancelDate(srmPaymentItemDTO.getCancelDate());
						srmPaymentItem.setCancelDesc(srmPaymentItemDTO.getCancelDesc());
						srmPaymentItem.setRepaymentDate(srmPaymentItemDTO.getRepaymentDate());
						srmPaymentItem.setStatus(IAtomsConstants.DATA_STATUS_ALREADY_REPAYMENT);
						srmPaymentItem.setUpdatedById(logonUser.getId());
						srmPaymentItem.setUpdatedByName(logonUser.getName());
						srmPaymentItem.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						this.srmPaymentItemDAO.getDaoSupport().saveOrUpdate(srmPaymentItem);
						//增加一筆歷史進程資料
						if (i + 1 == paymentItemDTOs.size() || !srmPaymentItemDTO.getPaymentId().equals(paymentItemDTOs.get(i + 1).getPaymentId())) {
							saveSrmPaymentTranscation(srmPaymentItemDTO.getPaymentId(), IAtomsConstants.PAY_ACTION.RETURN.getCode(), IAtomsConstants.DATA_STATUS_ALREADY_REPAYMENT, transcation.substring(0, transcation.length()-1), logonUser);
							transcation = new StringBuffer();
						}
					} else {
						if (transcation.length() > 0 && (i + 1 == paymentItemDTOs.size() || !srmPaymentItemDTO.getPaymentId().equals(paymentItemDTOs.get(i + 1).getPaymentId()))) {
							saveSrmPaymentTranscation(srmPaymentItemDTO.getPaymentId(), IAtomsConstants.PAY_ACTION.RETURN.getCode(), IAtomsConstants.DATA_STATUS_ALREADY_REPAYMENT, transcation.substring(0, transcation.length()-1), logonUser);
							transcation = new StringBuffer();
						}
					}
				}
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.RETURN_SUCCESS);
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.RETURN_FAILURE);
			}
			sessionContext.setReturnMessage(msg);
		}catch (DataAccessException e) {
			LOG.error("repayment() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.RETURN_FAILURE, e);
		} catch (Exception e) {
			LOG.error("repayment() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.RETURN_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * Purpose: 求償處理記錄檔
	 * @author CarrieDuan
	 * @param paymentId
	 * @param action
	 * @param status
	 * @param info
	 * @param logonUser
	 * @throws SecurityException
	 * @return void
	 */
	public String saveSrmPaymentTranscation(String paymentId, String action, String status, String info, IAtomsLogonUser logonUser) throws SecurityException {
		String paymentTransId = null;
		try {
			SrmPaymentTranscation paymentTranscation = new SrmPaymentTranscation();
			//增加一筆歷史進程資料
			paymentTransId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_SRM_PAYMENT_TRANSCATION);
			paymentTranscation.setTranscationId(paymentTransId);
			paymentTranscation.setPaymentId(paymentId);
			paymentTranscation.setAction(action);
			paymentTranscation.setStatus(status);
			paymentTranscation.setPaymentContent(info);
			paymentTranscation.setUpdatedById(logonUser.getId());
			paymentTranscation.setUpdatedByName(logonUser.getName());
			paymentTranscation.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
			this.paymentTranscationDAO.getDaoSupport().saveOrUpdate(paymentTranscation);
		} catch (DataAccessException e) {
			LOG.error("save() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, e);
		} catch (Exception e) {
			LOG.error("save() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, e);
		}
		return paymentTransId;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#getPaymentItemByItemIds(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<SrmPaymentItemDTO> getPaymentItemByItemIds(MultiParameterInquiryContext param) throws SecurityException {
		List<SrmPaymentItemDTO> paymentItemDTOs = null;
		try {
			String itemId = (String) param.getParameter(SrmPaymentItemDTO.ATTRIBUTE.ITEM_ID.getValue());
			String updateDates = (String) param.getParameter(SrmPaymentItemDTO.ATTRIBUTE.CHECK_UPDATE_DATE.getValue());
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
				@Override
				public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
					return new Date(json.getAsJsonPrimitive().getAsLong());
				}
			});
			Gson gsons = builder.create();
			List<SrmPaymentItemDTO> updateColumns = (List<SrmPaymentItemDTO>) gsons.fromJson(updateDates, new TypeToken<List<SrmPaymentItemDTO>>(){}.getType());
			if (StringUtils.hasText(itemId)) {
				paymentItemDTOs = this.srmPaymentItemDAO.getPaymentItemDTOByItemId(itemId, null);
				if (!CollectionUtils.isEmpty(paymentItemDTOs)) {
					for (SrmPaymentItemDTO srmPaymentItemDTO : paymentItemDTOs) {
						for (SrmPaymentItemDTO dto : updateColumns) {
							if (srmPaymentItemDTO.getItemId().equals(dto.getItemId())) {
								if (srmPaymentItemDTO.getUpdatedDate().getTime() != dto.getUpdatedDate().getTime()) {
									return null;
								}
							}
						}
					}
					for (SrmPaymentItemDTO srmPaymentItemDTO : paymentItemDTOs) {
						if (StringUtils.hasText(srmPaymentItemDTO.getStatus())) {
							String[] action = srmPaymentItemDTO.getStatus().split(IAtomsConstants.MARK_CENTER_LINE);
							if (action.length > 1) {
								srmPaymentItemDTO.setStatus(action[1]);
							}
						}
						if (!IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM.equals(srmPaymentItemDTO.getStatus())) {
							srmPaymentItemDTO.setIsAskPay(null);
						}
						srmPaymentItemDTO.setUpdateDateLong(DateTimeUtils.toNumeric(srmPaymentItemDTO.getUpdatedDate()));
					}
				} else {
					return null;
				}
			}
		} catch (DataAccessException e) {
			LOG.error("getPaymentItemByItemIds() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error("getPaymentItemByItemIds() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return paymentItemDTOs;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#delete(cafe.core.context.SessionContext)
	 */
	public SessionContext delete(SessionContext sessionContext) throws SecurityException {
		try {
			Message msg = null;
			PaymentFormDTO formDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
			String paymentId = formDTO.getPaymentId();
			if (StringUtils.hasText(paymentId)) {
				SrmPaymentInfo srmPaymentInfo = this.paymentInfoDAO.findByPrimaryKey(SrmPaymentInfo.class, paymentId);
				if (srmPaymentInfo != null) {
					//刪除求償列表資料
					this.srmPaymentItemDAO.deletedByPaymentId(paymentId);
					//刪除歷程資料
					this.paymentTranscationDAO.deleteByPaymentId(paymentId);
					this.paymentInfoDAO.delete(srmPaymentInfo);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_ALERDAY_UPDATE);
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOG.error("delete() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOG.error("delete() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * Purpose: 發送郵件
	 * @author CarrieDuan
	 * @param paymentInfoDTOs
	 * @throws SecurityException
	 * @return void
	 */
	private void sendMail (List<SrmPaymentItemDTO> paymentItemDTOs, String status, Boolean isBack, Boolean isOther) throws SecurityException {
		try{
			//郵件主題
			String mailSubject = null;
			//郵件內容
			String mailContext = null;
			//郵件主題模板
			String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + PaymentFormDTO.PARAM_PAYMENT_MAIL_THEME;
			//郵件內容模板
			String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + PaymentFormDTO.PARAM_PAYMENT_MAIL_CONTEXT;
			//郵件內容
			Map<String, Object> variables = new HashMap<String, Object>();
			StringBuffer buffer = new StringBuffer();
			//List<SrmPaymentInfoDTO> paymentInfoDTOs = this.srmPaymentItemDAO.listBy(paymentId, Boolean.TRUE);
			List<String> admRole = new ArrayList<String>();
			if (!CollectionUtils.isEmpty(paymentItemDTOs)) {
				String customerName = null;
				String paymentItem = null;
				String itemName = null;
				String contractCode = null;
				Integer suppliesAmount = null;
				String paymentReasonName = null;
				String suppliesName = null;
				String serialNumber = null;
				buffer.append("<thead >");
				buffer.append("<tr style=\"text-align: center; \">");
				if (IAtomsConstants.DATA_STATUS_REQUEST_FUNDS.equals(status) && !isBack) {
					buffer.append("<th style= \"border:1px solid RGB(204,204,204); width :10%; \">客戶</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :5%;\">求償項目</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :10%;\">設備名稱/耗材名稱</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :10%;\">設備序號</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :10%; \">合約編號</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :5%; \">耗材數量</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :10%; \">求償資訊</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204); width :10%; \">求償費用</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :10%;\">求償費用說明</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :10%;\">請款時間</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :10%;\">說明</th>");
				} else {
					buffer.append("<th style= \"border:1px solid RGB(204,204,204); width :15%; \">客戶</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :5%;\">求償項目</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :15%;\">設備名稱/耗材名稱</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :10%;\">設備序號</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :10%; \">合約編號</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :5%; \">耗材數量</th>");
					buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :15%; \">求償資訊</th>");
					if (IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED.equals(status) && !isBack && isOther) {
						buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :10%; \">檢測結果</th>");
						buffer.append("<th style= \"border:1px solid RGB(204,204,204);  width :10%; \">檢測人</th>");
					}
				}
				buffer.append("</tr>");
				buffer.append("</thead>");
				buffer.append("<tbody>");
				for (SrmPaymentItemDTO paymentItemDTO : paymentItemDTOs) {
					customerName = paymentItemDTO.getCustomerName();
					paymentItem = paymentItemDTO.getPaymentItemName();
					itemName = paymentItemDTO.getItemName();
					contractCode = paymentItemDTO.getContractCode();
					suppliesAmount = paymentItemDTO.getSuppliesAmount();
					paymentReasonName = paymentItemDTO.getPaymentReasonName();
					suppliesName = paymentItemDTO.getSuppliesName();
					serialNumber = paymentItemDTO.getSerialNumber();
					buffer.append("<tr>");
					if (StringUtils.hasText(customerName)) {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append(customerName).append("</td>");
					} else {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append("&nbsp;&nbsp;</td>");
					}
					
					if (StringUtils.hasText(paymentItem)) {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append(paymentItem).append("</td>");
					} else {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append("&nbsp;&nbsp;</td>");
					}
					
					if (StringUtils.hasText(suppliesName)) {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append(suppliesName).append("</td>");
					} else {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append(itemName).append("</td>");
					}
					
					if (StringUtils.hasText(serialNumber)) {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append(serialNumber).append("</td>");
					} else {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append("&nbsp;&nbsp;</td>");
					}
					
					if (StringUtils.hasText(contractCode)) {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append(contractCode).append("</td>");
					} else {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append("&nbsp;&nbsp;</td>");
					}
					
					if (suppliesAmount != null) {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204); text-align :right;\">").append(suppliesAmount).append("</td>");
					} else {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append("&nbsp;&nbsp;</td>");
					}
					
					if (StringUtils.hasText(paymentReasonName)) {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append(paymentReasonName).append("</td>");
					} else {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append("&nbsp;&nbsp;</td>");
					}
					if (IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED.equals(status) && !isBack && isOther) {
						if (StringUtils.hasText(paymentItemDTO.getCheckResult())) {
							buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append(paymentItemDTO.getCheckResult()).append("</td>");
						} else {
							buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append("&nbsp;&nbsp;</td>");
						}
						if (StringUtils.hasText(paymentItemDTO.getCheckUser())) {
							buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append(paymentItemDTO.getCheckUser()).append("</td>");
						} else {
							buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append("&nbsp;&nbsp;</td>");
						}
					}
					if (IAtomsConstants.DATA_STATUS_REQUEST_FUNDS.equals(status) && !isBack) {
						if (StringUtils.hasText(paymentItemDTO.getFee())) {
							buffer.append("<td style=\"border:1px solid RGB(204,204,204); text-align :right;\">").append(paymentItemDTO.getFee()).append("</td>");
						} else {
							buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append("&nbsp;&nbsp;</td>");
						}
						if (StringUtils.hasText(paymentItemDTO.getFeeDesc())) {
							buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append(paymentItemDTO.getFeeDesc()).append("</td>");
						} else {
							buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append("&nbsp;&nbsp;</td>");
						}
						if (paymentItemDTO.getAskPayDate() != null) {
							buffer.append("<td style=\"border:1px solid RGB(204,204,204); text-align :center;\">").append(DateTimeUtils.toString(paymentItemDTO.getAskPayDate(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH)).append("</td>");
						} else {
							buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append("&nbsp;&nbsp;</td>");
						}
						if (StringUtils.hasText(paymentItemDTO.getAskPayDesc())) {
							buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append(paymentItemDTO.getAskPayDesc()).append("</td>");
						} else {
							buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append("&nbsp;&nbsp;</td>");
						}
					}
					buffer.append("</tr>");
				}
				buffer.append("</tbody>");
				mailContext = buffer.toString();
			}
			//如果動作後狀態為待維修，則向倉管人員發信
			if (IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED.equals(status)) {
				admRole.add(IAtomsConstants.WAREHOUSE_KEEPER);
				mailSubject = i18NUtil.getName(IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED);
			}
			//如果設備狀態為待確認金額，則向帳務人員發信
//			if (IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED.equals(status)
//					|| IAtomsConstants.DATA_STATUS_REQUEST_FUNDS.equals(status)) {
//				admRole.add(IAtomsConstants.ACCOUNTING);
//				if (!(IAtomsConstants.DATA_STATUS_CREATE.equals(paymentItemDTOs.get(0).getStatus())
//						&& IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED.equals(status))) {
//					admRole.add(IAtomsConstants.CUSTOMER_SERVICE);
//				}
//				if (IAtomsConstants.DATA_STATUS_REQUEST_FUNDS.equals(status)) {
//					admRole.add(IAtomsConstants.WAREHOUSE_KEEPER);
//					mailSubject = i18NUtil.getName(IAtomsConstants.PAYMENT_ACCOUNTING) + i18NUtil.getName(IAtomsConstants.PAYMENT_COMPLETE);
//				} else {
//					mailSubject = i18NUtil.getName(IAtomsConstants.PAYMENT_CHECK_RESULT);
//				}
//			}
			//如果動作後狀態為待確認金額，則向客服人員發信
			if (IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED.equals(status)) {
				admRole.add(IAtomsConstants.ACCOUNTING);
				if (isOther) {
					admRole.add(IAtomsConstants.CUSTOMER_SERVICE);
					mailSubject = i18NUtil.getName(IAtomsConstants.PAYMENT_CHECK_RESULT);
				} else {
					mailSubject = i18NUtil.getName(IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED);
				}
			}
			//如果動作後狀態為請款／已取消，則向賬務、客服、倉管人員發信
			if (IAtomsConstants.DATA_STATUS_REQUEST_FUNDS.equals(status)) {
				admRole.add(IAtomsConstants.ACCOUNTING);
				admRole.add(IAtomsConstants.CUSTOMER_SERVICE);
				admRole.add(IAtomsConstants.WAREHOUSE_KEEPER);
				mailSubject = i18NUtil.getName(IAtomsConstants.PAYMENT_ACCOUNTING) + i18NUtil.getName(IAtomsConstants.PAYMENT_COMPLETE);
			}
			//如果狀態為退回，則向客服人員發信
			/*if (IAtomsConstants.DATA_STATUS_BACK.equals(status)) {
				admRole.add(IAtomsConstants.CUSTOMER_SERVICE);
				mailSubject = i18NUtil.getName(IAtomsConstants.DATA_STATUS_BACK);
			}*/
			//如果動作後狀態為待償確認，則向客服人員發信
			if (IAtomsConstants.DATA_STATUS_WAIT_CLAIM_CONFIRM.equals(status)) {
				admRole.add(IAtomsConstants.ACCOUNTING);
				mailSubject = i18NUtil.getName(IAtomsConstants.DATA_STATUS_WAIT_CLAIM_CONFIRM);
			}
			//如果動作後狀態為求償確認，則向帳務人員發信
			if (IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM.equals(status)) {
				admRole.add(IAtomsConstants.ACCOUNTING);
				mailSubject = i18NUtil.getName(IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM);
			}
			if (IAtomsConstants.DATA_STATUS_CREATE.equals(status)) {
				admRole.add(IAtomsConstants.CUSTOMER_SERVICE);
			}
			if (isBack) {
				mailSubject = i18NUtil.getName(IAtomsConstants.DATA_STATUS_BACK);
			}
			List<AdmUserDTO> admUserDTOs = this.admUserDAO.getUserDTOsBy(admRole);
			if (!CollectionUtils.isEmpty(admUserDTOs)) {
				StringBuffer emailBuffer = new StringBuffer();
				String toEmail = null;
				for (AdmUserDTO admUserDTO : admUserDTOs) {
					toEmail = admUserDTO.getEmail();
					if (StringUtils.hasText(toEmail)) {
						emailBuffer.append(toEmail).append(IAtomsConstants.MARK_SEMICOLON);
					}
				}
				if (emailBuffer.length() > 0) {
					toEmail = emailBuffer.substring(0, emailBuffer.length()-1).toString();
					variables.put("toMail", toEmail);
					//mial主旨
					variables.put("status", mailSubject);
					//mail内容
					variables.put("mailContext", mailContext);
					try{
						this.mailComponent.mailTo(null, toEmail, subjectTemplate, textTemplate, variables);
					} catch (Exception e) {
						LOG.debug(".sendMail() --> sendMail() is error... ");
					}
				}
			}			
		} catch (DataAccessException e) {
			LOG.error("sendMail() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOG.error("sendMail() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#export(cafe.core.context.SessionContext)
	 */
	public SessionContext export(SessionContext sessionContext) throws ServiceException {
		try {
			PaymentFormDTO claimWorkFormDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
			Timestamp caseStart = null;
			Timestamp caseEnd = null;
			Timestamp lostDayEndDate = null;
			Timestamp lostDayStartDate = null;
			
			String createDateStart = claimWorkFormDTO.getQueryCreateCaseDateStart();
			if (StringUtils.hasText(createDateStart)) {
				caseStart = DateTimeUtils.toTimestamp(createDateStart);
			}
			String caseEndDate = claimWorkFormDTO.getQueryCreateCaseDateEnd();
			SimpleDateFormat sf = new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
			if (StringUtils.hasText(caseEndDate)) {
				Calendar calendar = new GregorianCalendar(); 
				calendar.setTime(DateTimeUtils.toDate(caseEndDate)); 
				calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动 
				caseEnd = DateTimeUtils.toTimestamp(sf.format(calendar.getTime()).toString());
			}
			String lostDayStart = claimWorkFormDTO.getQueryLostDayStart();
			if (StringUtils.hasText(lostDayStart)) {
				lostDayStartDate = DateTimeUtils.toTimestamp(lostDayStart);
			}
			String lostDayEnd = claimWorkFormDTO.getQueryLostDayEnd();
			if (StringUtils.hasText(lostDayEnd)) {
				Calendar calendar = new GregorianCalendar(); 
				calendar.setTime(DateTimeUtils.toDate(lostDayEnd)); 
				calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动 
				lostDayEndDate = DateTimeUtils.toTimestamp(sf.format(calendar.getTime()));
			}
			//Task #3452 是否微型商戶
			Boolean isMicro = claimWorkFormDTO.getIsMicro();
			List<SrmPaymentInfoDTO> paymentInfoDTOs = this.paymentInfoDAO.listBy(claimWorkFormDTO.getQueryCompanyId(), null, caseStart, caseEnd, 
					claimWorkFormDTO.getQueryMerchantCode(), claimWorkFormDTO.getQueryDtid(), claimWorkFormDTO.getQueryTid(), 
					claimWorkFormDTO.getQueryDataStatus(), claimWorkFormDTO.getQueryCompensationMethod(), lostDayStartDate, lostDayEndDate,
					claimWorkFormDTO.getQuerySerialNumber(), claimWorkFormDTO.getOrder(), claimWorkFormDTO.getSort(), 
					-1, -1, isMicro);
			claimWorkFormDTO.setList(paymentInfoDTOs);
			int i = 0;
			String paymentId = null;
			String space = IAtomsConstants.MARK_SPACE;
			for (SrmPaymentInfoDTO srmPaymentInfoDTO : paymentInfoDTOs) {
				if (StringUtils.hasText(paymentId) && paymentId.equals(srmPaymentInfoDTO.getPaymentId())) {
					srmPaymentInfoDTO.setIndex(Integer.toString(i));
					srmPaymentInfoDTO.setCustomerName(srmPaymentInfoDTO.getCustomerName() + space);
				} else {
					srmPaymentInfoDTO.setIndex(Integer.toString(++i));
					paymentId = srmPaymentInfoDTO.getPaymentId();
					if (space.equals(IAtomsConstants.MARK_SPACE)){
						space = IAtomsConstants.MARK_SPACE + IAtomsConstants.MARK_SPACE;
					} else {
						space = IAtomsConstants.MARK_SPACE;
					}
					srmPaymentInfoDTO.setCustomerName(srmPaymentInfoDTO.getCustomerName() + space);
				}
				if (StringUtils.hasText(srmPaymentInfoDTO.getStatus())) {
					String[] action = srmPaymentInfoDTO.getStatus().split(IAtomsConstants.MARK_CENTER_LINE);
					if (action.length > 1) {
						srmPaymentInfoDTO.setStatusName(i18NUtil.getName(action[0]) + IAtomsConstants.MARK_CENTER_LINE + i18NUtil.getName(action[1]));
						srmPaymentInfoDTO.setStatus(action[1]);
					} else {
						srmPaymentInfoDTO.setStatusName(i18NUtil.getName(srmPaymentInfoDTO.getStatus()));
					}
				}
			}
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(claimWorkFormDTO);
		} catch (DataAccessException e) {
			LOG.error(".export() DataAccess Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOG.error(".export(SessionContext sessionContext):", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * Purpose :檢查傳入的求償項目狀態是否一致
	 * @author CarrieDuan
	 * @param srmPaymentItemDTOs :求償項目列表
	 * @param status :求償項目狀態,如果傳入空，則取列表第一個狀態
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return String :如果一致則返回一致的status，如過不一致，則返回null
	 */
	public String checkPaymentItemStatus(List<SrmPaymentItemDTO> srmPaymentItemDTOs, String status) throws ServiceException {
		try {
			if (!CollectionUtils.isEmpty(srmPaymentItemDTOs)) {
				if (!StringUtils.hasText(status)) {
					status = srmPaymentItemDTOs.get(0).getStatus();
				}
				//記錄狀態一致的求償id
				List<String> paymentSameIds = new ArrayList<String>();
				//記錄狀態不一致的求償id
				List<String> paymentNotSameIds = new ArrayList<String>();
				//求償id
				String paymentId = null;
				for (SrmPaymentItemDTO srmPaymentItemDTO : srmPaymentItemDTOs) {
					paymentId = srmPaymentItemDTO.getPaymentId();
					if (paymentSameIds.contains(paymentId)) {
						continue;
					}
					if (!status.equals(srmPaymentItemDTO.getStatus())) {
						paymentNotSameIds.add(paymentId);
					} else {
						paymentSameIds.add(paymentId);
						if (paymentNotSameIds.contains(paymentId)) {
							paymentNotSameIds.remove(paymentId);
						}
					}
				}
				if (paymentNotSameIds.size() > 0) {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			LOG.error(".checkPaymentItemStatus(SessionContext sessionContext):", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return status;
	}
	
	/**
	 * Purpose:核檢當前操作的數據狀態是否變化
	 * @author CarrieDuan
	 * @param srmPaymentItemDTOs
	 * @param status
	 * @throws ServiceException
	 * @return String
	 */
	public Boolean checkPaymentItemStatusUpdate(List<SrmPaymentItemDTO> srmPaymentItemDTOs, String status) throws ServiceException {
		try {
			if (!CollectionUtils.isEmpty(srmPaymentItemDTOs)) {
				List<SrmPaymentItemDTO> srmPaymentItems = null;
				for (SrmPaymentItemDTO srmPaymentItemDTO : srmPaymentItemDTOs) {
					srmPaymentItems = this.srmPaymentItemDAO.getPaymentItemDTOByItemId(IAtomsConstants.MARK_QUOTES + srmPaymentItemDTO.getItemId() + IAtomsConstants.MARK_QUOTES, null);
					if (!CollectionUtils.isEmpty(srmPaymentItems)) {
						if (StringUtils.hasText(status)) {
							if (srmPaymentItemDTO.getUpdatedDate().getTime() != srmPaymentItems.get(0).getUpdatedDate().getTime()) {
								return Boolean.FALSE;
							}
						} else {
							if (srmPaymentItemDTO.getUpdateDateLong() != DateTimeUtils.toNumeric(srmPaymentItems.get(0).getUpdatedDate())) {
								return Boolean.FALSE;
							}
						}
					} else {
						return Boolean.FALSE;
					}
				}
			}
		} catch (Exception e) {
			LOG.error(".checkPaymentItemStatusUpdate(SessionContext sessionContext):", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return Boolean.TRUE;
	}
	
	/**
	 * Purpose:在進行操作之前，核檢當前的求償狀態是否可以進行當前操作。
	 * @author CarrieDuan
	 * @param srmPaymentItemDTOs :求償項目列表
	 * @param status：需要匹配的設備狀態
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return String：如果可以進行操作則返回一致的status，如過不可，則返回null
	 */
	private String checkNowPaymentStatus (List<SrmPaymentItemDTO> srmPaymentItemDTOs, String status) throws ServiceException {
		try {
			
		} catch (Exception e) {
			LOG.error(".checkNowPaymentStatus(SessionContext sessionContext):", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return status;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#initEditCheckUpdate(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public Boolean initEditCheckUpdate(MultiParameterInquiryContext param) throws ServiceException {
		try {
			String paymentId = (String) param.getParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ID.getValue());
			Timestamp updatedDate = (Timestamp) param.getParameter(SrmPaymentInfoDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			if (StringUtils.hasText(paymentId) && updatedDate != null) {
				List<SrmPaymentInfoDTO> paymentInfoDTOs = this.srmPaymentItemDAO.listBy(paymentId, Boolean.FALSE);
				if (!CollectionUtils.isEmpty(paymentInfoDTOs)) {
					if (updatedDate.getTime() != paymentInfoDTOs.get(0).getUpdatedDate().getTime()) {
						return Boolean.TRUE;
					}
				} else {
					return Boolean.TRUE;
				}
			}
		} catch (DataAccessException e) {
			LOG.error(".initEditCheckUpdate() DataAccess Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOG.error(".initEditCheckUpdate(SessionContext sessionContext):", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return Boolean.FALSE;
	}

	/**
	 * 存儲案件call cms的log
	 * @param msgType：電文類型,01：上行(RQ),02：下行(RS)
	 * @param masterId：交易主檔
	 * @param detailId：交易明細檔
	 * @param userId：登陸者id
	 * @param userName：登陸者name
	 * @param result：交易結果
	 * @param failReasonDesc：交易回覆錯誤訊息
	 * @param message：電文內容
	 * @throws ServiceException
	 */
	private void insertApiLog(String msgType, String masterId, String detailId,IAtomsLogonUser logonUser, String result, String failReasonDesc, String message) throws ServiceException{
		LOG.debug(".insertApiLog()--> msgType=" + msgType);
		LOG.debug(".insertApiLog()--> masterId=" + masterId);
		LOG.debug(".insertApiLog()--> detailId=" + detailId);
		LOG.debug(".insertApiLog()--> userId=" + logonUser.getId());
		LOG.debug(".insertApiLog()--> userName=" + logonUser.getName());
		LOG.debug(".insertApiLog()--> result=" + result);
		LOG.debug(".insertApiLog()--> failReasonDesc=" + failReasonDesc);
		LOG.debug(".insertApiLog()--> message=" + message);
		try {
			  //上下文寫入API_LOG
			  ApiLogDTO apiLogDTO = new ApiLogDTO();
			  apiLogDTO.setId(this.generateGeneralUUID("API_LOG"));
			  apiLogDTO.setIp(logonUser.getFromIp());
			  apiLogDTO.setClientCode("CMS");
			  apiLogDTO.setFunctionCode("SRM05020");
			  apiLogDTO.setMsgType(msgType);//上行
			  apiLogDTO.setMasterId(masterId);
			  apiLogDTO.setDetailId(detailId);//歷程id
			  apiLogDTO.setResult(result);
			  apiLogDTO.setMessage(message);
			  apiLogDTO.setFailReasonDesc(failReasonDesc);
			  apiLogDTO.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
			  apiLogDTO.setCreatedById(logonUser.getId());
			  apiLogDTO.setCreatedByName(logonUser.getName());
			  Transformer transformer = new SimpleDtoDmoTransformer();
			  ApiLog apiLog = null;
			  apiLog = (ApiLog)transformer.transform(apiLogDTO, new ApiLog());			  
			  this.apiLogDAO.getDaoSupport().save(apiLog); 
		} catch (DataAccessException e) {
			LOG.error(this.getClass().getName() + ".insertApiLog() is error" + e);
			throw new ServiceException(IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOG.error(this.getClass().getName() + ".insertApiLog():" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPaymentService#changePaymentCompleteByApi(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext changePaymentCompleteByApi(SessionContext sessionContext) throws ServiceException {
		try {
			Message msg = null;
			PaymentFormDTO claimWorkFormDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
			if (claimWorkFormDTO != null) {
				SrmPaymentInfoDTO paymentInfoDTO = claimWorkFormDTO.getPaymentInfoDTO();
				if (paymentInfoDTO != null) {
					SrmPaymentInfo paymentInfo = this.paymentInfoDAO.findByPrimaryKey(SrmPaymentInfo.class, paymentInfoDTO.getPaymentId());
					if (paymentInfo != null) {
						List<SrmPaymentItemDTO> paymentItemDTOs = this.srmPaymentItemDAO.getPaymentItemDTOByItemId(null, paymentInfoDTO.getPaymentId());
						if (!CollectionUtils.isEmpty(paymentItemDTOs)) {
							if (IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM.equals(paymentItemDTOs.get(0).getStatus())) {
								SrmPaymentItem paymentItem = null;
								String status = null;
								StringBuffer transcation = new StringBuffer();
								String desc = i18NUtil.getName(IAtomsConstants.PAYMENT_BACK_DESC);
								for (SrmPaymentItemDTO srmPaymentItemDTO : paymentItemDTOs) {
									paymentItem = this.srmPaymentItemDAO.findByPrimaryKey(SrmPaymentItem.class, srmPaymentItemDTO.getItemId());
									srmPaymentItemDTO.setFee(paymentItem.getFee());
									srmPaymentItemDTO.setFeeDesc(paymentItem.getFeeDesc());
									if (IAtomsConstants.CUSTOMER_PAYMENT.equals(paymentItem.getPaymentType()) 
											|| IAtomsConstants.MERCHANT_PAYMENT.equals(paymentItem.getPaymentType())) {
										paymentItem.setAskPayDesc(paymentInfoDTO.getAskPayDesc());
										srmPaymentItemDTO.setAskPayDesc(paymentInfoDTO.getAskPayDesc());
									}
									if (IAtomsConstants.SELF_ABSORB.equals(paymentItem.getPaymentType()) || IAtomsConstants.DETECTION_NORMAL.equals(paymentItem.getPaymentType())
											|| IAtomsConstants.CUSTOMER_PAYMENT.equals(paymentItem.getPaymentType())) {
										paymentItem.setStatus(IAtomsConstants.DATA_STATUS_ALREADY_CANCEL);
									} else {
										paymentItem.setStatus(IAtomsConstants.DATA_STATUS_REQUEST_FUNDS);
										paymentItem.setAskPayDate(DateTimeUtils.getCurrentTimestamp());
										srmPaymentItemDTO.setAskPayDate(paymentItem.getAskPayDate());
									}
									paymentItem.setUpdatedById("CMS000000-0001");
									paymentItem.setUpdatedByName("cms_to_iatoms");
									paymentItem.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
									this.srmPaymentItemDAO.getDaoSupport().update(paymentItem);
									status = paymentItem.getStatus();
									if (IAtomsConstants.DATA_STATUS_ALREADY_CANCEL.equals(status)) {
										transcation.append(srmPaymentItemDTO.getItemName()).append(IAtomsConstants.MARK_MIDDLE_LINE).append(i18NUtil.getName(status))
										.append(IAtomsConstants.MARK_SEPARATOR).append(IAtomsConstants.MARK_SPACE)
										.append(desc.substring(2, desc.length())).append(IAtomsConstants.MARK_COLON).append(paymentItem.getAskPayDesc() != null ? paymentItem.getAskPayDesc() : "")
										.append(IAtomsConstants.MARK_SEMICOLON).append(IAtomsConstants.MARK_WRAP);
									} else {
										transcation.append(srmPaymentItemDTO.getItemName()).append(IAtomsConstants.MARK_MIDDLE_LINE).append(i18NUtil.getName(status))
										.append(IAtomsConstants.MARK_MIDDLE_LINE).append(i18NUtil.getName(IAtomsConstants.PAYMENT_ASKPAY_DATE))
										.append(IAtomsConstants.MARK_COLON).append(DateTimeUtils.toString(paymentItem.getAskPayDate(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH))
										.append(IAtomsConstants.MARK_SEPARATOR).append(IAtomsConstants.MARK_SPACE)
										.append(desc.substring(2, desc.length())).append(IAtomsConstants.MARK_COLON).append(paymentItem.getAskPayDesc() != null ? paymentItem.getAskPayDesc() : "")
										.append(IAtomsConstants.MARK_SEMICOLON).append(IAtomsConstants.MARK_WRAP);
									}
								}
								IAtomsLogonUser logonUser = new IAtomsLogonUser();
								logonUser.setId("CMS000000-0001");
								logonUser.setName("cms_to_iatoms");
								String transactionId = saveSrmPaymentTranscation(paymentInfoDTO.getPaymentId(), IAtomsConstants.PAY_ACTION.COMPLETE.getCode(), IAtomsConstants.DATA_STATUS_COMPLETE, transcation.substring(0, transcation.length()-1), logonUser);
									transcation = new StringBuffer();
								sessionContext.setAttribute("apiTransactionId", transactionId);
								msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS);
								try{
									this.sendMail(paymentItemDTOs, IAtomsConstants.DATA_STATUS_REQUEST_FUNDS, Boolean.FALSE, Boolean.FALSE);
								} catch (Exception e) {
									LOG.debug(".back() --> send() is error... ");
								}
							} else {
								LOG.debug("changePaymentCompleteByApi() ==> status=" + paymentItemDTOs.get(0).getStatus());
								msg = new Message(Message.STATUS.FAILURE, IAtomsConstants.MAXLENGTH_NUMBER_FORTY_FOUR);
							}
						} else {
							LOG.debug("changePaymentCompleteByApi() ==> paymentItemDTOs is null");
						}
					} else {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NOT_QUERY_PAYMENT_INFO);
						LOG.debug("changePaymentCompleteByApi() ==> paymentInfo is null");
					}
				} else {
					LOG.debug("changePaymentCompleteByApi() ==> paymentInfoDTO is null");
				}
			} else {
				LOG.debug("changePaymentCompleteByApi() ==> claimWorkFormDTO is null");
			}
			if (msg == null) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SYSTEM_FAILED);
			}
			sessionContext.setResponseResult(claimWorkFormDTO);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOG.error(this.getClass().getName() + ".changePaymentCompleteByApi() is error" + e);
			throw new ServiceException(IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOG.error(this.getClass().getName() + ".changePaymentCompleteByApi():" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	/**
	 * @return the caseHandleInfoDAO
	 */
	public ISrmCaseHandleInfoDAO getCaseHandleInfoDAO() {
		return caseHandleInfoDAO;
	}

	/**
	 * @param caseHandleInfoDAO the caseHandleInfoDAO to set
	 */
	public void setCaseHandleInfoDAO(ISrmCaseHandleInfoDAO caseHandleInfoDAO) {
		this.caseHandleInfoDAO = caseHandleInfoDAO;
	}

	/**
	 * @return the paymentInfoDAO
	 */
	public ISrmPaymentInfoDAO getPaymentInfoDAO() {
		return paymentInfoDAO;
	}

	/**
	 * @param paymentInfoDAO the paymentInfoDAO to set
	 */
	public void setPaymentInfoDAO(ISrmPaymentInfoDAO paymentInfoDAO) {
		this.paymentInfoDAO = paymentInfoDAO;
	}

	/**
	 * @return the srmPaymentItemDAO
	 */
	public ISrmPaymentItemDAO getSrmPaymentItemDAO() {
		return srmPaymentItemDAO;
	}

	/**
	 * @param srmPaymentItemDAO the srmPaymentItemDAO to set
	 */
	public void setSrmPaymentItemDAO(ISrmPaymentItemDAO srmPaymentItemDAO) {
		this.srmPaymentItemDAO = srmPaymentItemDAO;
	}

	/**
	 * @return the paymentTranscationDAO
	 */
	public ISrmPaymentTranscationDAO getPaymentTranscationDAO() {
		return paymentTranscationDAO;
	}

	/**
	 * @param paymentTranscationDAO the paymentTranscationDAO to set
	 */
	public void setPaymentTranscationDAO(
			ISrmPaymentTranscationDAO paymentTranscationDAO) {
		this.paymentTranscationDAO = paymentTranscationDAO;
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
	 * @return the apiLogDAO
	 */
	public IApiLogDAO getApiLogDAO() {
		return apiLogDAO;
	}

	/**
	 * @param apiLogDAO the apiLogDAO to set
	 */
	public void setApiLogDAO(IApiLogDAO apiLogDAO) {
		this.apiLogDAO = apiLogDAO;
	}

	
	
}
