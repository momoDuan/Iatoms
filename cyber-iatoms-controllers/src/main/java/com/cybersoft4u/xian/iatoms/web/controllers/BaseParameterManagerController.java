package com.cybersoft4u.xian.iatoms.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import cafe.core.bean.Message;
import cafe.core.exception.CommonException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.BaseParameterManagerFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;

/**
 * Purpose: 系統參數維護Controller
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年6月27日
 * @MaintenancePersonnel CrissZhang
 */
public class BaseParameterManagerController extends AbstractMultiActionController<BaseParameterManagerFormDTO> {

	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(BaseParameterManagerController.class);
	
	/**
	 * 
	 * Constructor: 構造函數
	 */
	public BaseParameterManagerController(){
		this.setCommandClass(BaseParameterManagerFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	public boolean validate(BaseParameterManagerFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			//檢驗頁面輸入
			BaseParameterItemDefDTO baseParameterItemDefDTO = command.getBaseParameterItemDefDTO();
			if (baseParameterItemDefDTO == null) {
				return false;
			}
			// 驗證參數類別
			String bptdCode = baseParameterItemDefDTO.getBptdCode();
			if(!StringUtils.hasText(bptdCode)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_BPTD_CODE)});
				throw new CommonException(msg);
			}
			// 驗證參數代碼
			String itemValue = baseParameterItemDefDTO.getItemValue();
			if(!StringUtils.hasText(itemValue)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_VALUE)});
				throw new CommonException(msg);
			} else {
				// 長度驗證
				if(!ValidateUtils.length(itemValue, 0, 50)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_VALUE), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			// 驗證參數名稱
			String itemName = baseParameterItemDefDTO.getItemName();
			if(!StringUtils.hasText(itemName)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_NAME)});
				throw new CommonException(msg);
			} else {
				if(!ValidateUtils.length(itemName, 0, 50)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_NAME), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			// 驗證附加欄位
			String textField1 = baseParameterItemDefDTO.getTextField1();
			if(StringUtils.hasText(textField1)){
				if(!ValidateUtils.length(textField1, 0, 50)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_TEXT_FIELD1), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			} 
			// 驗證順序
			Integer itemOrder = baseParameterItemDefDTO.getItemOrder();
			if(itemOrder != null){
				if(!ValidateUtils.length(itemOrder.toString(), 0, 2)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_ORDER), IAtomsConstants.MAXLENGTH_NUMBER_TWO});
					throw new CommonException(msg);
				} else {
					if(!ValidateUtils.number(itemOrder.toString())){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_POSITIVE_INTEGER, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_ORDER)});
						throw new CommonException(msg);
					}
				}
			} 
			// 驗證備註
			String itemDesc = baseParameterItemDefDTO.getItemDesc();
			if(StringUtils.hasText(itemDesc)){
				if(!ValidateUtils.length(itemDesc, 0, 200)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_DESC), IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
					throw new CommonException(msg);
				}
			}
			// 是案件轉移時判斷附加欄位
			if(IATOMS_PARAM_TYPE.SYSTEM_LIMIT.getCode().equals(bptdCode)){
				if(StringUtils.hasText(textField1)){
					// 判斷長度
					if(!ValidateUtils.length(textField1, 0, 3)){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_TEXT_FIELD1), IAtomsConstants.MAXLENGTH_NUMBER_THREE});
						throw new CommonException(msg);
					} else {
						if(!ValidateUtils.number(textField1)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_POSITIVE_INTEGER, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_TEXT_FIELD1)});
							throw new CommonException(msg);
						}
					}
				// 判斷附加欄位必填
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_TEXT_FIELD1)});
					throw new CommonException(msg);
				}
			}
			// 借用通知（倉管）判斷email格式
			if(IAtomsConstants.PARAM_BORROW_ADVICE_CODE.equals(bptdCode)){
				// 郵箱驗證
				if(!ValidateUtils.email(itemValue)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_MANAGER_EMAIL)});
					throw new CommonException(msg);
				}
			}
		}
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	public BaseParameterManagerFormDTO parse(HttpServletRequest request, BaseParameterManagerFormDTO command) throws CommonException {
		try{
			// 获取actionId
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			if (!StringUtils.hasText(actionId)) {
				//若actionId爲空,則預設爲init
				actionId = IAtomsConstants.ACTION_INIT;
			}
			command.setActionId(actionId);
			if (IAtomsConstants.ACTION_QUERY.equals(actionId)){
				// 查詢條件--當前頁碼
				int currentPage = this.getInt(request, IAtomsConstants.QUERY_PAGE_INDEX);
				// 查詢條件--每頁總筆數
				int pageSize = this.getInt(request, IAtomsConstants.QUERY_PAGE_ROWS);
				// 查詢條件--排序方式
				String sortDirection = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				/*if(!StringUtils.hasText(sortDirection)){
					sortDirection = IAtomsConstants.PARAM_PAGE_ORDER;
				}*/
				// 查詢條件--排序欄位
				String sortFieldName = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				// 查詢條件--參數類型
				String queryParamType = this.getString(request, BaseParameterManagerFormDTO.QUERY_PAGE_PARAM_TYPE);
				// 查詢條件--參數代碼
				String queryParamCode = this.getString(request, BaseParameterManagerFormDTO.QUERY_PAGE_PARAM_CODE);
				// 查詢條件--參數名稱
				String queryParamName = this.getString(request, BaseParameterManagerFormDTO.QUERY_PAGE_PARAM_NAME);
				command.getPageNavigation().setCurrentPage(currentPage - 1);
				command.getPageNavigation().setPageSize(pageSize);
				command.setSortDirection(sortDirection);
				command.setSortFieldName(sortFieldName);
				command.setQueryParamType(queryParamType);
				command.setQueryParamCode(queryParamCode);
				command.setQueryParamName(queryParamName);
			}else if(IAtomsConstants.ACTION_SAVE.equals(actionId)){
				//封裝頁面欄位
				BaseParameterItemDefDTO sourceDTO = BindPageDataUtils.bindValueObject(request, BaseParameterItemDefDTO.class);
				command.setBaseParameterItemDefDTO(sourceDTO);
			}else if(IAtomsConstants.ACTION_INIT_EDIT.equals(actionId) || IAtomsConstants.ACTION_DELETE.equals(actionId)){
				//初始化編輯參數 -- 參數類型
				String editBptdCode = this.getString(request, BaseParameterManagerFormDTO.EDIT_BPTD_CODE);
				//初始化編輯參數 -- 參數編號
				String editBpidId = this.getString(request, BaseParameterManagerFormDTO.EDIT_BPID_ID);
				//初始化編輯參數 -- 生效日期
				String editEffectiveDate = this.getString(request, BaseParameterManagerFormDTO.EDIT_EFFECITVE_DATE);
				command.setEditBpidId(editBpidId);
				command.setEditBptdCode(editBptdCode);
				command.setEditEffectiveDate(editEffectiveDate);
			}else if (IAtomsConstants.ACTION_CHECK.equals(actionId)){
				// 檢核條件 -- 參數類型
				String checkBptdCode = this.getString(request, BaseParameterItemDefDTO.ATTRIBUTE.BPTD_CODE.getValue());
				// 檢核條件 -- 參數代碼
				String checkItemValue = this.getString(request, BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue());
				command.setCheckBptdCode(checkBptdCode);
				command.setCheckItemValue(checkItemValue);
			}
		}catch(Exception e){
			LOGGER.error("parse()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}
}
