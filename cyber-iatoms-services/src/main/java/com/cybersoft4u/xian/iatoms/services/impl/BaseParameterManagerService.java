package com.cybersoft4u.xian.iatoms.services.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.context.BaseParameterInquiryContext;
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
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.BaseParameterManagerFormDTO;
import com.cybersoft4u.xian.iatoms.services.IBaseParameterManagerService;
import com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterManagerDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterItemDef;
import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterItemDefId;

/**
 * Purpose: 系統參數維護Service
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年6月27日
 * @MaintenancePersonnel CrissZhang
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BaseParameterManagerService extends AtomicService implements
		IBaseParameterManagerService {
	
	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(BaseParameterManagerService.class);
	
	/**
	 * 固定的生效日期設定
	 */
	public static final String STATIC_EFFECTIVE_DATE = IAtomsConstants.STATIC_VERSION_DATE_FOR_BASE_PARAMETER;

	/**
	 * 参数维护DAO注入
	 */
	private IBaseParameterManagerDAO baseParameterManagerDAO;
	
	/**
	 * 框架基本參數DAO注入
	 */
	private IBaseParameterItemDefDAO baseParameterItemDefDAO;
	/**
	 * 
	 * Constructor: 無參構造子
	 */
	public BaseParameterManagerService(){
		
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IBaseParameterManagerService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext)
			throws ServiceException {
		try{
			BaseParameterManagerFormDTO admParameterFormDTO = null;
			Message message = null;
			if(sessionContext != null){
				admParameterFormDTO = (BaseParameterManagerFormDTO) sessionContext.getRequestParameter();
				message = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
				sessionContext.setResponseResult(admParameterFormDTO);
				sessionContext.setReturnMessage(message);
			}
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+".init() is error in Service:"+e,e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE,e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IBaseParameterManagerService#listParameterTypes(cafe.core.context.SessionContext)
	 */
	public SessionContext listParameterTypes(SessionContext sessionContext)throws ServiceException {
		try{
			List<Parameter> results = null;
			if(sessionContext != null){
				// 調用查type的方法
				results = this.baseParameterManagerDAO.listParameterTypes();
				sessionContext.setResponseResult(results);
			}
		}catch(DataAccessException e){
			LOGGER.error(this.getClass().getName()+".listParameterTypes() is error in DataAccess:"+e,e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE,e);
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+".listParameterTypes() is error in Service:"+e,e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE,e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IBaseParameterManagerService#list(cafe.core.context.SessionContext)
	 */
	public SessionContext query(SessionContext sessionContext)
			throws ServiceException {
		try{
			List<BaseParameterItemDefDTO> results = null;
			Message message = null;
			BaseParameterManagerFormDTO admBaseParameterFormDTO = null;
			if(sessionContext != null){
				admBaseParameterFormDTO = (BaseParameterManagerFormDTO) sessionContext.getRequestParameter();
				if(admBaseParameterFormDTO != null){
					// 查詢條件
					String queryParamType = admBaseParameterFormDTO.getQueryParamType();
					LOGGER.debug(this.getClass().getName()+".query() --> queryParamType :"+queryParamType);
					String queryParamCode = admBaseParameterFormDTO.getQueryParamCode();
					LOGGER.debug(this.getClass().getName()+".query() --> queryParamCode :"+queryParamCode);
					String queryParamName = admBaseParameterFormDTO.getQueryParamName();
					LOGGER.debug(this.getClass().getName()+".query() --> queryParamName :"+queryParamName);
					String sortDirection = admBaseParameterFormDTO.getSortDirection();
					LOGGER.debug(this.getClass().getName()+".query() --> sortDirection :"+sortDirection);
					String sortFieldName = admBaseParameterFormDTO.getSortFieldName();
					LOGGER.debug(this.getClass().getName()+".query() --> sortFieldName :"+sortFieldName);
					Integer currentPage = admBaseParameterFormDTO.getPageNavigation().getCurrentPage();
					LOGGER.debug(this.getClass().getName()+".query() --> currentPage :"+currentPage);
					Integer pageSize = admBaseParameterFormDTO.getPageNavigation().getPageSize();
					LOGGER.debug(this.getClass().getName()+".query() --> pageSize :"+pageSize);
					Integer rowCount = this.baseParameterManagerDAO.count(queryParamType, queryParamCode, queryParamName);
					LOGGER.debug(this.getClass().getName()+".query() --> rowCount :"+rowCount);
					if(rowCount > 0){
						// 查詢基本參數信息
						results = this.baseParameterManagerDAO.listBy(queryParamType, queryParamCode, queryParamName, sortDirection, sortFieldName, currentPage, pageSize);
						for(BaseParameterItemDefDTO baseParameterItemDefDTO : results){
							String updatedByName = baseParameterItemDefDTO.getUpdatedByName();
							// 處理異動人員信息
							if(StringUtils.hasText(updatedByName) && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) >0 && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
								baseParameterItemDefDTO.setUpdatedByName(updatedByName.substring(updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
							}
						}
						message = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
					}else{
						message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
					}
					admBaseParameterFormDTO.setList(results);
					admBaseParameterFormDTO.getPageNavigation().setRowCount(rowCount);
				}
				sessionContext.setResponseResult(admBaseParameterFormDTO);
				sessionContext.setReturnMessage(message);
			}
		} catch(DataAccessException e){
			LOGGER.error(this.getClass().getName()+".query() is error in DataAccess:"+e,e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE,e);
		} catch(Exception e){
			LOGGER.error(this.getClass().getName()+".query() is error in Service:"+e,e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IBaseParameterManagerService#save(cafe.core.context.SessionContext)
	 */
	public SessionContext save(SessionContext sessionContext)
			throws ServiceException {
		String messageCode = IAtomsConstants.MARK_EMPTY_STRING;
		try{
			BaseParameterManagerFormDTO formDTO = null;
			Message message = null;
			IAtomsLogonUser logonUser = null;
			//欲儲存對象
			BaseParameterItemDef baseParameterItemDef = null;
			//欲儲存對象Id
			BaseParameterItemDefId baseParameterItemDefId = null;
			if(sessionContext != null){
				formDTO = (BaseParameterManagerFormDTO) sessionContext.getRequestParameter();
				if(formDTO != null){
					logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
					//頁面數據封裝對象
					BaseParameterItemDefDTO sourceDTO = formDTO.getBaseParameterItemDefDTO();
					if(sourceDTO != null){
						/*message = this.check(formDTO);
						if(message.getStatus() == Message.STATUS.FAILURE){
							sessionContext.setReturnMessage(message);
							return sessionContext;
						}*/
						//初始化編輯條件--參數編號
						String editBpidId = formDTO.getEditBpidId();
						LOGGER.debug(this.getClass().getName()+".save() --> editBpidId : "+editBpidId);
						//初始化編輯條件--參數類別
						String editBptdCode = formDTO.getEditBptdCode();
						LOGGER.debug(this.getClass().getName()+".save() --> editBptdCode : "+editBptdCode);
						//當前時間
						Timestamp currentTime = DateTimeUtils.getCurrentTimestamp();
						LOGGER.debug(this.getClass().getName()+".save() --> currentTime : "+currentTime);
						//固定的生效日期
						Date effectiveDate = DateTimeUtils.parseDate(STATIC_EFFECTIVE_DATE, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
						baseParameterItemDef = new BaseParameterItemDef();
						baseParameterItemDefId = new BaseParameterItemDefId();
						//若存在初始化編輯條件,則爲修改數據
						if(StringUtils.hasText(editBpidId) && StringUtils.hasText(editBptdCode)){
							
							// 編輯時驗證重復
							if (!IATOMS_PARAM_TYPE.SYB_REMAIN_TIME.getCode().equals(editBptdCode)
									&& !IATOMS_PARAM_TYPE.FREE_REMAIN_TIME.getCode().equals(editBptdCode)
									&& !IATOMS_PARAM_TYPE.OLD_REMAIN_TIME.getCode().equals(editBptdCode)) {
								boolean isRepeat = this.baseParameterManagerDAO.isRepeat(editBpidId, editBptdCode, null, sourceDTO.getItemName());
								if(isRepeat){
									message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_VALUE_EXISTS, new String[]{sourceDTO.getItemName() + i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_NAME)});
									sessionContext.setReturnMessage(message);
									return sessionContext;
								}
							}
							//獲取數據庫已存在資料--由於effectiveDate無法匹配則未使用findByPrimaryKey方法
							BaseParameterItemDefDTO existDTO = this.baseParameterManagerDAO.getBaseParameterItemDefDTO(editBpidId, null, editBptdCode, null, null);
							if(existDTO != null){
								//將數據庫原始資料轉換爲欲存儲對象
								SimpleDtoDmoTransformer transformer = new SimpleDtoDmoTransformer();
								baseParameterItemDef = (BaseParameterItemDef) transformer.transform(existDTO, baseParameterItemDef);
								//設置Id
								baseParameterItemDefId.setBpidId(existDTO.getBpidId());
								baseParameterItemDefId.setBptdCode(existDTO.getBptdCode());
								baseParameterItemDefId.setEffectiveDate(existDTO.getEffectiveDate());
								baseParameterItemDef.setId(baseParameterItemDefId);
								messageCode = IAtomsMessageCode.UPDATE_SUCCESS;
							}
						} else {//新增
							// 新增驗證value重復
							if (!IATOMS_PARAM_TYPE.SYB_REMAIN_TIME.getCode().equals(sourceDTO.getBptdCode())
									&& !IATOMS_PARAM_TYPE.FREE_REMAIN_TIME.getCode().equals(editBptdCode)
									&& !IATOMS_PARAM_TYPE.OLD_REMAIN_TIME.getCode().equals(editBptdCode)) {
								boolean isRepeatItemValue = this.baseParameterManagerDAO.isRepeat(null, sourceDTO.getBptdCode(), sourceDTO.getItemValue(), null);
								if(isRepeatItemValue){
									message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_VALUE_EXISTS, new String[]{sourceDTO.getItemValue() + i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_VALUE)});
									sessionContext.setReturnMessage(message);
									return sessionContext;
								}
								// 新增驗證name重復
								boolean isRepeatItemName = this.baseParameterManagerDAO.isRepeat(null, sourceDTO.getBptdCode(), null, sourceDTO.getItemName());
								if(isRepeatItemName){
									message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_VALUE_EXISTS, new String[]{sourceDTO.getItemName() + i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_NAME)});
									sessionContext.setReturnMessage(message);
									return sessionContext;
								}
							}
							//uuid方式獲取參數編號
							editBpidId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF);
							baseParameterItemDefId.setBpidId(editBpidId);
							//設置Id
							baseParameterItemDefId.setBptdCode(sourceDTO.getBptdCode());
							baseParameterItemDefId.setEffectiveDate(effectiveDate);
							baseParameterItemDef.setId(baseParameterItemDefId);
							//設置新增者信息
//							baseParameterItemDef.setCreatedById(logonUser.getId());
//							baseParameterItemDef.setCreatedByName(logonUser.getName());
//							baseParameterItemDef.setCreatedDate(currentTime);
							messageCode = IAtomsMessageCode.INSERT_SUCCESS;
						}
						//設置頁面輸入欄位
						baseParameterItemDef.setItemValue(sourceDTO.getItemValue());
						baseParameterItemDef.setItemName(sourceDTO.getItemName());
						baseParameterItemDef.setItemOrder(sourceDTO.getItemOrder());
						baseParameterItemDef.setItemDesc(sourceDTO.getItemDesc());
						baseParameterItemDef.setTextField1(sourceDTO.getTextField1());
						//設置更新者信息
						baseParameterItemDef.setUpdatedById(logonUser.getId());
						baseParameterItemDef.setUpdatedByName(logonUser.getName());
						baseParameterItemDef.setUpdatedDate(currentTime);
						//設置復核標記爲Y
				//		baseParameterItemDef.setApprovedFlag(IAtomsConstants.YES);
						baseParameterItemDef.setDeleted(IAtomsConstants.NO);
						//存儲
						this.baseParameterManagerDAO.getDaoSupport().saveOrUpdate(baseParameterItemDef);
						this.baseParameterManagerDAO.getDaoSupport().flush();
						message = new Message(Message.STATUS.SUCCESS, messageCode, new String[]{this.getMyName()});
					}
				}
				sessionContext.setReturnMessage(message);
			}
		}catch(DataAccessException e){
			LOGGER.error(this.getClass().getName()+".save() is error in DataAccess:"+e,e);
			if(messageCode.equals(IAtomsMessageCode.INSERT_SUCCESS)){
				messageCode = IAtomsMessageCode.INSERT_FAILURE;
			}else{
				messageCode = IAtomsMessageCode.UPDATE_FAILURE;
			}
			throw new ServiceException(messageCode,new String[]{this.getMyName()});
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+".save() is error in Service:"+e,e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IBaseParameterManagerService#initEdit(cafe.core.context.SessionContext)
	 */
	public SessionContext initEdit(SessionContext sessionContext)
			throws ServiceException {
		try{
			BaseParameterManagerFormDTO formDTO = null;
			BaseParameterItemDefDTO baseParameterItemDefDTO = null;
			String editBpidId = IAtomsConstants.MARK_EMPTY_STRING;
			String editBptdCode = IAtomsConstants.MARK_EMPTY_STRING;
			if(sessionContext != null){
				formDTO = (BaseParameterManagerFormDTO) sessionContext.getRequestParameter();
				if(formDTO != null){
					// 獲取初始化編輯條件
					editBpidId = formDTO.getEditBpidId();
					LOGGER.debug(this.getClass().getName()+".initEdit() --> editBpidId : "+editBpidId);
					editBptdCode = formDTO.getEditBptdCode();
					LOGGER.debug(this.getClass().getName()+".initEdit() --> editBptdCode : "+editBptdCode);
					if(StringUtils.hasText(editBpidId) && StringUtils.hasText(editBptdCode)){
						baseParameterItemDefDTO = this.baseParameterManagerDAO.getBaseParameterItemDefDTO(editBpidId, null, editBptdCode, null, null);
					}
					formDTO.setBaseParameterItemDefDTO(baseParameterItemDefDTO);
				}
				sessionContext.setResponseResult(formDTO);
			}
		}catch(DataAccessException e){
			LOGGER.error(this.getClass().getName()+".initEdit() is error in DataAccess:"+e,e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+".initEdit() is error in Service:"+e,e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IBaseParameterManagerService#delete(cafe.core.context.SessionContext)
	 */
	public SessionContext delete(SessionContext sessionContext)
			throws ServiceException {
		try{
			BaseParameterManagerFormDTO formDTO = null;
			BaseParameterItemDefDTO baseParameterItemDefDTO = null;
			BaseParameterItemDef baseParameterItemDef = null;
			BaseParameterItemDefId baseParameterItemDefId = null;
			Message message = null;
			LogonUser logonUser = null;
			if(sessionContext != null){
				formDTO = (BaseParameterManagerFormDTO) sessionContext.getRequestParameter();
				if(formDTO != null){
					logonUser = formDTO.getLogonUser();
					//获取要删除资料的bpidId,bptdCode
					String editBpidId = formDTO.getEditBpidId();
					LOGGER.debug(this.getClass().getName()+".delete() --> editBpidId :"+editBpidId);
					String editBptdCode = formDTO.getEditBptdCode();
					LOGGER.debug(this.getClass().getName()+".delete() --> editBptdCode :"+editBptdCode);
					if(StringUtils.hasText(editBptdCode) && StringUtils.hasText(editBpidId)){
						//根据bpidId,bptdCode在数据库查询存在的资料
						baseParameterItemDefDTO = this.baseParameterManagerDAO.getBaseParameterItemDefDTO(editBpidId, null, editBptdCode, null, null);
						if(baseParameterItemDefDTO != null){
							//将查询出的资料转换为dmo
							SimpleDtoDmoTransformer transformer = new SimpleDtoDmoTransformer();
							baseParameterItemDef = new BaseParameterItemDef();
							baseParameterItemDef = (BaseParameterItemDef) transformer.transform(baseParameterItemDefDTO, baseParameterItemDef);
							if(baseParameterItemDef != null){
								//设置主键
								baseParameterItemDefId = new BaseParameterItemDefId(baseParameterItemDefDTO.getBpidId(), baseParameterItemDefDTO.getBptdCode(), baseParameterItemDefDTO.getEffectiveDate());
								baseParameterItemDef.setId(baseParameterItemDefId);
								//删除资料(僅將復核標記設置爲N)
					//			baseParameterItemDef.setApprovedFlag(IAtomsConstants.NO);
								baseParameterItemDef.setDeleted(IAtomsConstants.YES);
								baseParameterItemDef.setUpdatedById(logonUser.getId());
								baseParameterItemDef.setUpdatedByName(logonUser.getName());
								baseParameterItemDef.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
								baseParameterItemDef.setDeletedDate(DateTimeUtils.getCurrentTimestamp());
								this.baseParameterManagerDAO.getDaoSupport().update(baseParameterItemDef);
								this.baseParameterManagerDAO.getDaoSupport().flush();
								//设置操作结果信息
								message = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
							} else {
								message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
							}
						} else {
							message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
						}
					}
				}
				sessionContext.setResponseResult(formDTO);
				sessionContext.setReturnMessage(message);
			}
		}catch(DataAccessException e){
			LOGGER.error(this.getClass().getName()+".delete() is error in DataAccess:"+e,e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE,new String[]{this.getMyName()});
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+".delete() is error in Service:"+e,e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED);
		}
		return sessionContext;
	}
	
	/**
	 * Purpose:檢核相同參數類型下, 參數值不可重複
	 * @author ericdu
	 * @param formDTO : 傳入一個BaseParameterManagerFormDTO對象
	 * @throws ServiceException ： 錯誤時拋出service异常类
	 * @return Message ： 返回一個Message類型
	 */
	private Message check(BaseParameterManagerFormDTO formDTO)
			throws ServiceException {
		Message result = new Message(Message.STATUS.SUCCESS);
		try{
			if(formDTO != null){
				String editBptdCode = formDTO.getEditBptdCode();
				String editBpidId = formDTO.getEditBpidId();
				BaseParameterItemDefDTO sourceDTO = formDTO.getBaseParameterItemDefDTO();
				List<BaseParameterItemDefDTO> existedDTOs = this.baseParameterManagerDAO.listBy(sourceDTO.getBptdCode(), null, null, null, null, -1, -1);
				if(StringUtils.hasText(editBpidId) || StringUtils.hasText(editBptdCode)){
					//修改時驗證--存在已復合的參數名稱
					for(BaseParameterItemDefDTO dto : existedDTOs){
						if(dto.getItemName().equals(sourceDTO.getItemName()) 
								&& !dto.getItemValue().equals(sourceDTO.getItemValue()) ){
							return new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_VALUE_EXISTS, new String[]{sourceDTO.getItemName() + i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_NAME)});
						}
					}
				}else{
					//新增時驗證--存在此參數代碼或存在已複核的參數名稱
					for(BaseParameterItemDefDTO dto : existedDTOs){
						if(dto.getItemValue().equals(sourceDTO.getItemValue())){
							return new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_VALUE_EXISTS, new String[]{sourceDTO.getItemValue() + i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_VALUE)});
						}
						if(dto.getItemName().equals(sourceDTO.getItemName())){
							return new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_VALUE_EXISTS, new String[]{sourceDTO.getItemName() + i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_NAME)});
						}
					}
				}
			}
		}catch(DataAccessException e){
			LOGGER.error(this.getClass().getName()+".check() is error in DataAccess: "+e,e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE);
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+".check() is error in Service: "+e,e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IBaseParameterManagerService#getParametersByParent(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getParametersByParent(MultiParameterInquiryContext inquiryContext)
			throws ServiceException {
		List<Parameter> result = null;
		try {
			// 父項code
			String parentBptdCode = (String) inquiryContext.getParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_BPTD_CODE);
			// 父項value
			String parentItemValue = (String) inquiryContext.getParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_ITEM_VALUE);
			// 子項code
			String childrenBptdCode = (String) inquiryContext.getParameter(BaseParameterManagerFormDTO.PARAMETER_CHILDREN_BPTD_CODE);
			// TEXT_FIELD1
			String textFiled1 = (String) inquiryContext.getParameter(BaseParameterManagerFormDTO.PARAMETER_TEXT_FIELD1);
			//有效時間
			Date editEffectiveDate = (Date) inquiryContext.getParameter(BaseParameterManagerFormDTO.EDIT_EFFECITVE_DATE);
			if(StringUtils.hasText(parentBptdCode) && StringUtils.hasText(parentItemValue)){
				// 根據底層方法查詢系統參數信息
				cafe.core.bean.dto.parameter.BaseParameterItemDefDTO baseParameterItemDefDTO =  this.baseParameterItemDefDAO.getParameterItemByValue(parentBptdCode, editEffectiveDate, parentItemValue);
				BaseParameterInquiryContext inquiryCtx = new BaseParameterInquiryContext();
				inquiryCtx.setParameterType(childrenBptdCode);
				if(baseParameterItemDefDTO != null){
					inquiryCtx.setParentItemId(baseParameterItemDefDTO.getItemId());
					inquiryCtx.setVersionDate(editEffectiveDate);
					// 根據父項id與子項code查詢相關參數列表
					result = (List<Parameter>) this.baseParameterItemDefDAO.getParameterItems(inquiryCtx);
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getSimpleName() + ".getParameterItems() DataAccess Exception : " + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getSimpleName() + ".getParameterItems() Exception : " + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return result;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IBaseParameterManagerService#getParametersByTextField(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getParametersByTextField( MultiParameterInquiryContext inquiryContext) throws ServiceException {
		List<Parameter> result = null;
		try {
			//bptdCode
			String bptdCode = (String) inquiryContext.getParameter(BaseParameterManagerFormDTO.EDIT_BPTD_CODE);
			//TEXT_FIELD1
			String textField1 = (String) inquiryContext.getParameter(BaseParameterManagerFormDTO.PARAMETER_TEXT_FIELD1);
			if (StringUtils.hasText(textField1) && StringUtils.hasText(bptdCode)) {
				BaseParameterInquiryContext inquiryCtx = new BaseParameterInquiryContext();
				inquiryCtx.setParameterType(bptdCode);
				inquiryCtx.setTextField1(textField1);
				result = (List<Parameter>) this.baseParameterItemDefDAO.getParameterItems(inquiryCtx);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getSimpleName() + ".getParametersByTextField() DataAccess Exception : " + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getSimpleName() + ".getParametersByTextField() Exception : " + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return result;
	}
	
	/**
	 * @return the baseParameterManagerDAO
	 */
	public IBaseParameterManagerDAO getBaseParameterManagerDAO() {
		return baseParameterManagerDAO;
	}

	/**
	 * @param baseParameterManagerDAO the baseParameterManagerDAO to set
	 */
	public void setBaseParameterManagerDAO(
			IBaseParameterManagerDAO baseParameterManagerDAO) {
		this.baseParameterManagerDAO = baseParameterManagerDAO;
	}

	/**
	 * @return the baseParameterItemDefDAO
	 */
	public IBaseParameterItemDefDAO getBaseParameterItemDefDAO() {
		return baseParameterItemDefDAO;
	}

	/**
	 * @param baseParameterItemDefDAO the baseParameterItemDefDAO to set
	 */
	public void setBaseParameterItemDefDAO(IBaseParameterItemDefDAO baseParameterItemDefDAO) {
		this.baseParameterItemDefDAO = baseParameterItemDefDAO;
	}

	
}
