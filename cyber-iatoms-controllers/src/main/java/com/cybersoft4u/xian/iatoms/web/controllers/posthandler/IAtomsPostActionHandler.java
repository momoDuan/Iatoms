package com.cybersoft4u.xian.iatoms.web.controllers.posthandler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;

import cafe.core.bean.Message;
import cafe.core.bean.dto.AbstractSimpleListFormDTO;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.posthandler.IPostActionHandler;
import cafe.core.web.locator.IServiceLocatorProxy;

/**
 * Purpose:Ajax請求PostActionHandler, 返回Json ModelAndView 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年6月30日
 * @MaintenancePersonnel evanliu
 */
public class IAtomsPostActionHandler implements IPostActionHandler {
	/**
	 * log組件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(IAtomsPostActionHandler.class);
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor:
	 */
	public IAtomsPostActionHandler(){
		
	}	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.posthandler.IPostActionHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, ModelAndView mav)
			throws CommonException {
		try {
			if (mav != null && mav.getModel() != null) {
				//獲得SessionContext
				SessionContext sessionContext = (SessionContext)mav.getModel().get(IAtomsConstants.PARAM_SESSION_CONTEXT);
				//獲得actionId
				String actionId = sessionContext.getActionId();
				//返回信息
				Message msg = sessionContext.getReturnMessage();
				//formDTO
				
				Map map = null;
				//用於特殊情況，要向頁面傳值等
				Object object = sessionContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
				if (object != null) {
					if (object instanceof Map) {
						map = (Map) object;						
					} else {
						map = new HashMap();
						map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
					}
				} else {
					map = new HashMap();
				}
				if (IAtomsConstants.ACTION_QUERY.equals(actionId)
						|| IAtomsConstants.ACTION_LIST.equals(actionId)) {
					//查詢	
					AbstractSimpleListFormDTO abstractSimpleListFormDTO = (AbstractSimpleListFormDTO) sessionContext.getResponseResult();
					if (msg.getStatus() == Message.STATUS.SUCCESS) {
						//update by 2017/08/18 Bug #2217 判空
						if(abstractSimpleListFormDTO != null) {
							map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, abstractSimpleListFormDTO.getPageNavigation().getRowCount());
							map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, abstractSimpleListFormDTO.getList());						
						}
					} else {
						map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(0));
						//update by 2017/08/18 Bug #2217 判空
						if(abstractSimpleListFormDTO != null) {
							map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, abstractSimpleListFormDTO.getList());
						}
						map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(), null));
					}					
				} else if (IAtomsConstants.ACTION_SAVE.equals(actionId) || 
						IAtomsConstants.ACTION_DELETE.equals(actionId) || 
						IAtomsConstants.ACTION_CHECK.equals(actionId)) {
					//保存,刪除,檢查
					if (msg.getStatus() == Message.STATUS.SUCCESS) {
						map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.TRUE);
					} else {
						map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
					}
					//返回信息處理
					String[] arguments = msg.getArguments();
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), arguments, null));
				} else {
					//其他情況
					if (msg != null && StringUtils.hasText(msg.getCode())) {
						if (msg.getStatus() == Message.STATUS.SUCCESS) {
							map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.TRUE);
						} else {
							map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
						}
						map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(), null));
					}
				}
				return new ModelAndView(new MappingJacksonJsonView(), map); 
			}
		} catch (Exception e) {
			LOGGER.error("handle()","Exception:", e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return mav;
	}
	/**
	 * @return the serviceLocator
	 */
	public IServiceLocatorProxy getServiceLocator() {
		return serviceLocator;
	}
	/**
	 * @param serviceLocator the serviceLocator to set
	 */
	public void setServiceLocator(IServiceLocatorProxy serviceLocator) {
		this.serviceLocator = serviceLocator;
	}
}
