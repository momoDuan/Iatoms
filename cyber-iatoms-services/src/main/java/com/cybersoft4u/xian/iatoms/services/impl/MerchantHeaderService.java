package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantHeaderFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.CopyPropertiesUtils;
import com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService;
import com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterPostCodeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMerchantHeader;

/**
 * Purpose: 特店表頭維護Service
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/6/3
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MerchantHeaderService extends AtomicService implements IMerchantHeaderService{
	
	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOG = CafeLogFactory.getLog(MerchantHeaderService.class);
	/**
	 * Constructor:無參构造函数
	 */
	public MerchantHeaderService() {
		super();
	}
	/**
	 * 特店表頭DAO
	 */
	private IMerchantHeaderDAO merchantHeaderDAO;
	/**
	 * 客戶特店DAO
	 */
	private IMerchantDAO merchantNewDAO;
	/**
	 * 郵遞區號DAO
	 */
	private IBaseParameterPostCodeDAO baseParameterPostCodeDAO;
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		MerchantHeaderFormDTO formDTO = null;
		try {
			formDTO = ( MerchantHeaderFormDTO ) sessionContext.getRequestParameter();
			//Task #3583 當 【客戶廠商角色】 時 不設倉庫控管，可見本公司下所有設備
			setUserRoleAttribute(formDTO);

			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(formDTO);
		} catch (Exception e) {
			LOG.error(".init() is error:", "service Exception", e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * 判斷用戶角色屬性
	 */
	private void setUserRoleAttribute(MerchantHeaderFormDTO formDTO) {
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
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService#query(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		try {
			MerchantHeaderFormDTO merchantHeaderFormDTO = ( MerchantHeaderFormDTO ) sessionContext.getRequestParameter();
			if (merchantHeaderFormDTO != null) {
				//獲得查詢條件
				String queryCustomerId = merchantHeaderFormDTO.getQueryCustomerId();
				String queryMerCode = merchantHeaderFormDTO.getQueryMerchantCode();
				String queryStagesMerCode = merchantHeaderFormDTO.getQueryStagesMerchantCode();
				String queryName = merchantHeaderFormDTO.getQueryName();
				String queryIsVip = merchantHeaderFormDTO.getQueryIsVip();
				String queryHeaderName = merchantHeaderFormDTO.getQueryHeaderName();
				//排序列名
				String sort = merchantHeaderFormDTO.getSort();
				String order = merchantHeaderFormDTO.getOrder();
				//當前頁碼
				int currentPage = merchantHeaderFormDTO.getPage();
				//一頁的條數
				int pageSize = merchantHeaderFormDTO.getRows();
				//獲取查詢結果
				List<BimMerchantHeaderDTO> merchantHeaderList = this.merchantHeaderDAO.listby(queryCustomerId, queryMerCode, queryStagesMerCode, queryName, queryHeaderName, null, queryIsVip, currentPage, pageSize, true, sort, order, null, null);
				if (!CollectionUtils.isEmpty(merchantHeaderList)) {
					//如果查詢結果不為空，放入formDTO
					merchantHeaderFormDTO.setList(merchantHeaderList);
					//查詢總筆數
					Integer count = this.merchantHeaderDAO.getCount(queryCustomerId, queryMerCode, queryName, queryHeaderName, null, queryIsVip);
					//總筆數放入formDTO
					merchantHeaderFormDTO.getPageNavigation().setRowCount(count.intValue());
					sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS));
				}else{
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND));
				}
				sessionContext.setResponseResult(merchantHeaderFormDTO);
			}
		} catch (DataAccessException e) {
			LOG.error(".query() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error(".query():", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService#getMerchantInfo(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext getMerchantInfo(SessionContext sessionContext) throws ServiceException {
		try {
			MerchantHeaderFormDTO merchantHeaderFormDTO = ( MerchantHeaderFormDTO ) sessionContext.getRequestParameter();
			BimMerchantHeaderDTO merchantHeaderDTO = new BimMerchantHeaderDTO();
			//獲取特店編號
			String merchantCode = merchantHeaderFormDTO.getMerchantCode();
			//獲取公司ID
			String companyId = merchantHeaderFormDTO.getCustomerId();
			if (!StringUtils.hasText(merchantCode) || !StringUtils.hasText(companyId)) {
				//未輸入特店代號；數據為空
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.IATOMS_MSG_NONE_DATA));
				return sessionContext;
			}else{
				//根據特店編號和客戶姓名查詢客戶特店信息
				MerchantDTO merchantDTO = this.merchantNewDAO.getMerchantInfo(null, merchantCode, companyId, null);
				Map map = new HashMap();
				if (merchantDTO != null) {
					merchantHeaderDTO.setCompanyId(merchantDTO.getCompanyId());
					merchantHeaderDTO.setCustomerName(merchantDTO.getShortName());
					merchantHeaderDTO.setMerchantId(merchantDTO.getMerchantId());
					merchantHeaderDTO.setName(merchantDTO.getName());
					merchantHeaderDTO.setMerchantCode(merchantCode);
					//merchantHeaderDTO.setStagesMerchantCode(merchantDTO.getStagesMerchantCode());
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROW, merchantHeaderDTO);
				} else {
					//dto為空，該特店代號不存在在客戶特店的表中
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.MERCHANT_CODE_NOT_EXIST));
				}
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			}
		} catch (DataAccessException e) {
			LOG.error(".getMerchantInfo() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error(".getMerchantInfo():", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService#save(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		MerchantHeaderFormDTO merchantHeaderFormDTO = null;
		try {
			merchantHeaderFormDTO = ( MerchantHeaderFormDTO ) sessionContext.getRequestParameter();
			//從formDTO拿到DTO
			BimMerchantHeaderDTO merchantHeaderDTO = merchantHeaderFormDTO.getMerchantHeaderDTO();
			BimMerchantHeader merchantHeader = null;
			//拿到主鍵id
			String merchantHeaderId = merchantHeaderDTO.getMerchantHeaderId();
			//公司id
			String companyId = merchantHeaderDTO.getCompanyId();
			//表頭名
			String headerName = merchantHeaderFormDTO.getHeaderName();
			//獲得特店代號
			String merchantCode = merchantHeaderFormDTO.getMerchantCode();
			//客戶特店編號
			String merchantId = merchantHeaderFormDTO.getMerchantId();
			//驗證同一特店下表頭是否重複
			boolean isRepeat = this.merchantHeaderDAO.check(merchantId, headerName, merchantHeaderId);
			if (isRepeat) {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.MERCHANT_HEADER_NAME_NOT_REPEAT));
				return sessionContext;
			}
			//獲取登錄信息
			LogonUser logonUser = merchantHeaderFormDTO.getLogonUser();
			String userId = null; 
			String userName = null;
			if (logonUser != null) {
				//得到當前登入者的Id和Name
				userId = logonUser.getId();
				userName = logonUser.getName();
			} else {
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.LIMITED_LOGON_ID));
				return sessionContext;
			}
			//判斷特店表頭主鍵id是否存在
			if (StringUtils.hasText(merchantHeaderId)) {
				//修改，根據merchantHeaderId拿到數據庫中的merchantHeader
				merchantHeader = this.merchantHeaderDAO.findByPrimaryKey(BimMerchantHeader.class, merchantHeaderId);
				new CopyPropertiesUtils().copyProperties(merchantHeaderDTO, merchantHeader, null);
				merchantHeader.setUpdatedById(userId);
				merchantHeader.setUpdatedByName(userName);
				merchantHeader.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				merchantHeader.setDeleted(IAtomsConstants.NO);
				//特店表頭修改成功
				this.merchantHeaderDAO.getDaoSupport().update(merchantHeader);
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()}));
			} else {
				MerchantDTO merchantNewDTO = this.merchantNewDAO.getMerchantInfo(null, merchantCode, companyId, null);
				if (merchantNewDTO == null) {
					//不存在
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.MERCHANT_CODE_NOT_EXIST));
					return sessionContext;
				}else {
					//DTO轉換為DMO
					Transformer transformer = new SimpleDtoDmoTransformer();
					merchantHeader = (BimMerchantHeader) transformer.transform(merchantHeaderDTO, new BimMerchantHeader());
					merchantHeader.setMerchantId(merchantNewDTO.getMerchantId());
					merchantHeaderId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_MERCHANT_HEADER);
					merchantHeader.setMerchantHeaderId(merchantHeaderId);
					merchantHeader.setCreatedById(userId);
					merchantHeader.setCreatedByName(userName);
					merchantHeader.setUpdatedById(userId);
					merchantHeader.setUpdatedByName(userName);
					merchantHeader.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					merchantHeader.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					//新增存儲刪除標誌位 update by hermanwang 2017/05/16
					merchantHeader.setDeleted(IAtomsConstants.NO);
					this.merchantHeaderDAO.insert(merchantHeader);
					sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()}));
				}
			}
			Map<String, String> resultMap = new HashMap<String, String>();
			Map map = new HashMap();
			resultMap.put("merchantHeaderId", merchantHeaderId);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROW, resultMap);
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(true));
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			this.merchantHeaderDAO.getDaoSupport().flush();
		} catch (DataAccessException e) {
			LOG.error(".save() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}));
			LOG.error(".save():", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService#initEdit(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException {
		try {
			MerchantHeaderFormDTO merchantHeaderFormDTO = ( MerchantHeaderFormDTO ) sessionContext.getRequestParameter();
			BimMerchantHeaderDTO merchantHeaderDTO = null;
			//獲取主鍵
			String merchantHeaderId = merchantHeaderFormDTO.getMerchantHeaderId();
			if (StringUtils.hasText(merchantHeaderId)) {
				//通過主鍵ID拿到該條特店表頭信息
				List<BimMerchantHeaderDTO> merchantHeaderDTOList = this.merchantHeaderDAO.listby(null, null, null, null, null, null, null, 0, 0, true, null, null, merchantHeaderId, null);
				if (!CollectionUtils.isEmpty(merchantHeaderDTOList)) {
					//得到該DTO
					merchantHeaderDTO = merchantHeaderDTOList.get(0);
					//放到formDTO
					merchantHeaderFormDTO.setMerchantHeaderDTO(merchantHeaderDTO);
				}
			}
			//Task #3583 當 【客戶廠商角色】 時 不設倉庫控管，可見本公司下所有設備
			setUserRoleAttribute(merchantHeaderFormDTO);
			//放入Session
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(merchantHeaderFormDTO);
		} catch (DataAccessException e) {
			LOG.error(".initEdit() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		} catch (Exception e) {
			LOG.error(".initEdit():", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService#delete(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		MerchantHeaderFormDTO merchantHeaderFormDTO = null;
		try {
			merchantHeaderFormDTO = ( MerchantHeaderFormDTO ) sessionContext.getRequestParameter();
			//拿到主鍵ID
			String merchantHeaderId = merchantHeaderFormDTO.getMerchantHeaderId();
			//獲取登錄信息
			LogonUser logonUser = merchantHeaderFormDTO.getLogonUser();
			if (StringUtils.hasText(merchantHeaderId)) {
				//拿到DMO
				BimMerchantHeader merchantHeader = this.merchantHeaderDAO.findByPrimaryKey(BimMerchantHeader.class, merchantHeaderId);
				if (merchantHeader != null) {
					//刪除
					//修改刪除標誌位為Y update by hermanwang 2017/05/16 start
					merchantHeader.setDeleted(IAtomsConstants.YES);
					merchantHeader.setUpdatedById(logonUser.getId());
					merchantHeader.setUpdatedByName(logonUser.getName());
					merchantHeader.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					//修改刪除標誌位為Y update by hermanwang 2017/05/16 end
					this.merchantHeaderDAO.getDaoSupport().saveOrUpdate(merchantHeader);
					this.merchantHeaderDAO.getDaoSupport().flush();
					sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()}));
				} else {
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}));
				}
			}
			
			sessionContext.setResponseResult(merchantHeaderFormDTO);
		} catch (DataAccessException e) {
			LOG.error(".delete() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOG.error(".delete():", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService#initAdd(cafe.core.context.SessionContext)
	 */
	public SessionContext initAdd(SessionContext sessionContext) throws ServiceException {
		try {
			MerchantHeaderFormDTO merchantHeaderFormDTO = ( MerchantHeaderFormDTO ) sessionContext.getRequestParameter();
			//拿到客戶特店的主鍵ID
			String merchantId = merchantHeaderFormDTO.getMerchantId();
			BimMerchantHeaderDTO merchantHeaderDTO = new BimMerchantHeaderDTO();
			if (StringUtils.hasText(merchantId)) {
				//得到客戶特店信息
				MerchantDTO merchantDTO = this.merchantNewDAO.getMerchantInfo(merchantId, null, null, null);
				if (merchantDTO != null) {
					merchantHeaderDTO.setCompanyId(merchantDTO.getCompanyId());
					merchantHeaderDTO.setCustomerName(merchantDTO.getShortName());
					merchantHeaderDTO.setMerchantId(merchantDTO.getMerchantId());
					merchantHeaderDTO.setMerchantCode(merchantDTO.getMerchantCode());
					//merchantHeaderDTO.setStagesMerchantCode(merchantDTO.getStagesMerchantCode());
					merchantHeaderDTO.setName(merchantDTO.getName());
					merchantHeaderFormDTO.setMerchantHeaderDTO(merchantHeaderDTO);
				}
			}
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(merchantHeaderFormDTO);
		} catch (DataAccessException e) {
			LOG.error(".initAdd() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		} catch (Exception e) {
			sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE));
			LOG.error(".initAdd():", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService#getMerchantHeaderById(cafe.core.context.MultiParameterInquiryContext)
	 */
	public BimMerchantHeaderDTO getMerchantHeaderById(MultiParameterInquiryContext param) throws ServiceException {
		List<BimMerchantHeaderDTO> merchantHeaderDTOList = null;
		try {
			String merchantHeaderId = (String) param.getParameter(BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
			if (StringUtils.hasText(merchantHeaderId)) {
				//通過主鍵ID拿到該條特店表頭信息
				merchantHeaderDTOList = this.merchantHeaderDAO.listby(null, null, null, null, null, null, null, 0, 0, true, null, null, merchantHeaderId, null);
			}
			if (!CollectionUtils.isEmpty(merchantHeaderDTOList)) {
				return merchantHeaderDTOList.get(0);
			}
		} catch (DataAccessException e) {
			LOG.error(".getMerchantHeaderById() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error(".getMerchantHeaderById():", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return null;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService#getMerchantHeaderList(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getMerchantHeaderList(MultiParameterInquiryContext param) throws ServiceException {
		List<Parameter> merchantHeaders = null;
		try {
			String customerId = (String) param.getParameter(BimMerchantHeaderDTO.ATTRIBUTE.COMPANY_ID.getValue());
			String merchantCode = (String) param.getParameter(BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			String merchantId = (String) param.getParameter(BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			merchantHeaders = this.merchantHeaderDAO.getMerchantHeaders(customerId, merchantCode, merchantId);
		} catch (DataAccessException e) {
			LOG.error(".getMerchantHeaderList() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error(".getMerchantHeaderList():", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return merchantHeaders;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService#queryMid(cafe.core.context.SessionContext)
	 */
	public SessionContext queryMid(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			MerchantHeaderFormDTO merchantHeaderFormDTO = ( MerchantHeaderFormDTO ) sessionContext.getRequestParameter();
			Integer count = Integer.valueOf(0);
			Map map = new HashMap();
			//查詢符合條件的特護特店
			String companyId = merchantHeaderFormDTO.getQueryMIdCustomerId();
			String name = merchantHeaderFormDTO.getQueryMIdRegisteredName();
			String merchantCode = merchantHeaderFormDTO.getQueryMId();
			String businessAddress = merchantHeaderFormDTO.getQueryMIdAddress();
			//排序列名
			String sort = merchantHeaderFormDTO.getSort();
			String order = merchantHeaderFormDTO.getOrder();
			//當前頁碼
			int currentPage = merchantHeaderFormDTO.getPage();
			//一頁的條數
			int pageSize = merchantHeaderFormDTO.getRows();
			List<BimMerchantHeaderDTO> merchantHeaderList = this.merchantHeaderDAO.listby(companyId, merchantCode, null, name, null, businessAddress, null, currentPage, pageSize, true, sort, order, null, null);
			// 如果沒有查到資料就提示查詢失敗,如果查到資料就把資料都放到FormDTO中
			if (!CollectionUtils.isEmpty(merchantHeaderList)) {
				//把資料放在formDTO中
				count = this.merchantHeaderDAO.getCount(companyId, merchantCode, null, name, businessAddress, null);
				merchantHeaderFormDTO.getPageNavigation().setRowCount(count.intValue());
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			} else {
				//沒有查到資料就返回查無資料信息
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DATA_NOT_FOUND);
			}
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, count);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, merchantHeaderList);
			
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			sessionContext.setReturnMessage(msg);
			return sessionContext;
		} catch (DataAccessException e) {
			LOG.error(".query() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error(".query() Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService#getPostCodeInfo(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getPostCodeList(MultiParameterInquiryContext param) throws ServiceException {
		List<Parameter> postCodeList = null;
		try {
			String cityId = (String) param.getParameter(BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue());
			if(StringUtils.hasText(cityId)){
				postCodeList = this.baseParameterPostCodeDAO.getPostCodeList(cityId,null,null);
			}
		} catch (DataAccessException e) {
			LOG.error(".getPostCodeList() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error(".getPostCodeList():", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return postCodeList;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService#getHeaderDTOByMerchantCode(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public BimMerchantHeaderDTO getHeaderDTOByMerchantCode(MultiParameterInquiryContext param) throws ServiceException {
		BimMerchantHeaderDTO bimMerchantHeaderDTO = null;
		try {
			String merchantCode = (String) param.getParameter(BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			String headerName = (String) param.getParameter(BimMerchantHeaderDTO.ATTRIBUTE.HEADER_NAME.getValue());
			if(StringUtils.hasText(merchantCode) && StringUtils.hasText(headerName)){
				bimMerchantHeaderDTO = this.merchantHeaderDAO.getMerchantHeaderDTOBy(null, headerName, merchantCode);
			}
		} catch (DataAccessException e) {
			LOG.error(".getHeaderDTOByMerchantCode() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error(".getHeaderDTOByMerchantCode():", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return bimMerchantHeaderDTO;
	}
	
	/**
	 * @return the merchantHeaderDAO
	 */
	public IMerchantHeaderDAO getMerchantHeaderDAO() {
		return merchantHeaderDAO;
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
	public IBaseParameterPostCodeDAO getBaseParameterPostCodeDAO() {
		return baseParameterPostCodeDAO;
	}
	public void setBaseParameterPostCodeDAO(
			IBaseParameterPostCodeDAO baseParameterPostCodeDAO) {
		this.baseParameterPostCodeDAO = baseParameterPostCodeDAO;
	}

	
	
	
}
