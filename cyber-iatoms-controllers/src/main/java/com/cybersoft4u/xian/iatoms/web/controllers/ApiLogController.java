package com.cybersoft4u.xian.iatoms.web.controllers;

import javax.servlet.http.HttpServletRequest;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ApiLogFormDTO;

import cafe.core.bean.Message;
import cafe.core.exception.CommonException;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;

/**
 * Purpose:電文紀錄查詢Controller
 * @author NickLin
 * @since  JDK 1.7
 * @date   2018/06/12
 * @MaintenancePersonnel CyberSoft
 */
public class ApiLogController extends AbstractMultiActionController<ApiLogFormDTO> {
	
	/**
	 * 日誌記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(ApiLogController.class);
	
	/**
	 * Constructor: 無參數建構子
	 */
	public ApiLogController() {
		this.setCommandClass(ApiLogFormDTO.class);
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(ApiLogFormDTO command) throws CommonException {
		// TODO Auto-generated method stub
		if (command == null) {
			return false;
		}
		// 獲取actionId
		String actionId = command.getActionId();
		Message msg = null;
		String startDate = command.getQueryStartDate();
		String endDate = command.getQueryStartDate();
		if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
			//時間起
			if (!StringUtils.hasText(startDate)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_API_LOG_START_DATE)});
				throw new CommonException(msg);
			}
			//時間迄
			if (!StringUtils.hasText(endDate)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_API_LOG_END_DATE)});
				throw new CommonException(msg);
			}
		}
		return true;
	}

	/** 
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public ApiLogFormDTO parse(HttpServletRequest request, ApiLogFormDTO command)
			throws CommonException {
		// TODO Auto-generated method stub
		try {
			if (command == null) {
				command = new ApiLogFormDTO();
			}
			//獲取actionId
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			if(!StringUtils.hasText(actionId)){
				actionId = IAtomsConstants.ACTION_INIT;
			} else if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
				//查詢條件:Client_Code
				String clientCode = this.getString(request, ApiLogFormDTO.QUERY_CLIENT_CODE);
				LOGGER.debug("ApiLogController.parse() --> clientCode: " + clientCode);
				//拼接'%',後置模糊查詢
				if(StringUtils.hasText(clientCode)) {
					clientCode = clientCode + IAtomsConstants.MARK_PERCENT;
				}
				//查詢條件:建檔日期起
				String startDate = this.getString(request, ApiLogFormDTO.QUERY_START_DATE);
				LOGGER.debug("ApiLogController.parse() --> startDate: " + startDate);
				//查詢條件:建檔日期迄
				String endDate = this.getString(request, ApiLogFormDTO.QUERY_END_DATE);
				LOGGER.debug("ApiLogController.parse() --> endDate: " + endDate);
				//查詢參數:當前頁碼
				int currentPage = this.getInt(request, IAtomsConstants.QUERY_PAGE_INDEX);
				LOGGER.debug("ApiLogController.parse() --> currentPage: " + currentPage);
				//查詢參數:總筆數
				int pageSize = this.getInt(request, IAtomsConstants.QUERY_PAGE_ROWS);
				LOGGER.debug("ApiLogController.parse() --> pageSize: " + pageSize);
				//查詢參數:排序方式
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				if (!StringUtils.hasText(order)) {
					order = ApiLogFormDTO.PARAM_PAGE_ORDER;
				}
				LOGGER.debug("ApiLogController.parse() --> order: " + order);
				//查詢參數:排序欄位
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (!StringUtils.hasText(sort)) {
					sort = ApiLogFormDTO.PARAM_PAGE_SORT;
				}
				LOGGER.debug("ApiLogController.parse() --> sort: " + sort);
				command.setQueryClientCode(clientCode);
				command.setQueryStartDate(startDate);
				command.setQueryEndDate(endDate);
				command.getPageNavigation().setCurrentPage(currentPage - 1);
				command.getPageNavigation().setPageSize(pageSize);
				command.setOrder(order);
				command.setSort(sort);
			}
			LOGGER.debug("ApiLogController.parse() --> actionId: " + actionId);
			command.setActionId(actionId);
		} catch(Exception e) {
			LOGGER.error("ApiLogController.parse() is error: " + e, e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}
}
