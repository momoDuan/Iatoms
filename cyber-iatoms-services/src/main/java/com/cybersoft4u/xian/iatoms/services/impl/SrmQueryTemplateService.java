package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmQueryTemplateDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SrmQueryTemplateFormDTO;
import com.cybersoft4u.xian.iatoms.services.ISrmQueryTemplateService;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmQueryTemplateDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmQueryTemplate;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
/**
 * Purpose:用戶欄位模板維護檔Service 
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel CrissZhang
 */
public class SrmQueryTemplateService extends AtomicService implements ISrmQueryTemplateService{
	/**
	 * 系统日志文件控件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, SrmQueryTemplateService.class);
	
	/**
	 * 用戶欄位模板維護檔DAO
	 */
	private ISrmQueryTemplateDAO srmQueryTemplateDAO;
	
	/**
	 * Constructor:無參構造函數
	 */
	public SrmQueryTemplateService() {
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractSlaService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		try {
			SrmQueryTemplateFormDTO formDTO = (SrmQueryTemplateFormDTO) sessionContext.getRequestParameter();
			// 初始化成功提示消息
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(formDTO);
		} catch (Exception e) {
			LOGGER.error(".init() Exception : ", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractSlaService#save(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext saveTemplate(SessionContext sessionContext) throws ServiceException {
		Message msg = null ;
		SrmQueryTemplateFormDTO formDTO = (SrmQueryTemplateFormDTO) sessionContext.getRequestParameter();
		String templateId = formDTO.getTemplateId();
		// 拿到当前登录者
		LogonUser logonUser = formDTO.getLogonUser();
		try {
			// 得到DTO 信息 
			SrmQueryTemplateDTO userColumnTemplateDTO = formDTO.getAdmUserColumnTemplateDTO();
			// 更新、保存按鈕
			if(!formDTO.getIsOnlyShow()){
				boolean isRepeat = this.srmQueryTemplateDAO.isRepeat(templateId, logonUser.getId(), userColumnTemplateDTO.getTemplateName());
				if(isRepeat){
					// 模板名稱重複
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_REPEAT_TEMPLATE);
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
			}
			// 创建用戶模板DMO对象
			SrmQueryTemplate userColumnTemplate = null;
			if(StringUtils.hasText(templateId)){
				if(!formDTO.getIsOnlyShow()){
					userColumnTemplate = this.srmQueryTemplateDAO.findByPrimaryKey(SrmQueryTemplate.class, templateId);
					if(formDTO.getIsSaveAndShow()){
						userColumnTemplate.setIsDefault(IAtomsConstants.YES);
					}
					userColumnTemplate.setFieldContent(userColumnTemplateDTO.getFieldContent());
					userColumnTemplate.setTemplateName(userColumnTemplateDTO.getTemplateName());
					// 新增
					this.srmQueryTemplateDAO.update(userColumnTemplate);
				}
				
				// 编辑返回编辑成功信息
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_UPDATE_SUCCESS, new String[]{this.getMyName()});
			} else {
				boolean isOverLimit = this.srmQueryTemplateDAO.isTemplateOverLimit(logonUser.getId());
				if(isOverLimit){
					// 模板超過限制
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_OVER_LIMIT);
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
				// 用戶模板的DMO
				userColumnTemplate = new SrmQueryTemplate();
				// DTO轉換為DMO
				Transformer transformer = new SimpleDtoDmoTransformer();
				transformer.transform(userColumnTemplateDTO, userColumnTemplate);
				templateId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_SRM_QUERY_TEMPLATE);
				userColumnTemplate.setTemplateId(templateId);
				userColumnTemplate.setUserId(logonUser.getId());
				if(formDTO.getIsSaveAndShow()){
					userColumnTemplate.setIsDefault(IAtomsConstants.YES);
				} else {
					userColumnTemplate.setIsDefault(IAtomsConstants.NO);
				}
				
				// 新增
				this.srmQueryTemplateDAO.insert(userColumnTemplate);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
			}
			// 處理其他標記位
			if(formDTO.getIsOnlyShow() || formDTO.getIsSaveAndShow()){
				this.srmQueryTemplateDAO.updateOtherTemplate(logonUser.getId(), templateId);
			}
			
			Map map = new HashMap();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				map.put(SrmQueryTemplateDTO.ATTRIBUTE.TEMPLATE_ID.getValue(), templateId);
				map.put(SrmQueryTemplateDTO.ATTRIBUTE.TEMPLATE_NAME.getValue(), userColumnTemplateDTO.getTemplateName());
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(), null));
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(), null));
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
/*			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);*/
		} catch (DataAccessException e) {
			LOGGER.error(".save() DataAccess Exception : ", e.getMessage(), e);
			if(StringUtils.hasText(templateId)){
				throw new ServiceException(IAtomsMessageCode.PARAM_UPDATE_FAILURE, new String[]{this.getMyName()}, e);
			} else {
				throw new ServiceException(IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()}, e);
			}
		} catch (Exception e) {
			LOGGER.error(".save() Exception : ", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractSlaService#delete(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		try {
			Message msg = null;
			SrmQueryTemplateFormDTO formDTO = (SrmQueryTemplateFormDTO) sessionContext.getRequestParameter();
			String templateId = formDTO.getTemplateId();
			// 用戶模板的DMO
			SrmQueryTemplate userColumnTemplate = this.srmQueryTemplateDAO.findByPrimaryKey(SrmQueryTemplate.class, templateId);
			if(userColumnTemplate != null){
				// 刪除
				this.srmQueryTemplateDAO.getDaoSupport().delete(userColumnTemplate);
				this.srmQueryTemplateDAO.getDaoSupport().flush();
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error(".delete(SessionContext sessionContext) is error", e.getMessage(), e);
			throw new ServiceException(CoreMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".delete(SessionContext sessionContext) is failed!!!", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISrmQueryTemplateService#getUserColumnTemplateList()
	 */
	@Override
	public List<Parameter> getUserColumnTemplateList(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		try {
			String userId = (String) inquiryContext.getParameter(SrmQueryTemplateDTO.ATTRIBUTE.USER_ID.getValue());
			return this.srmQueryTemplateDAO.getUserColumnTemplateList(userId, false, false);
		} catch (DataAccessException e) {
			LOGGER.error("getUserColumnTemplateList()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getUserColumnTemplateList()", "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISrmQueryTemplateService#getCurrentColumnTemplate(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public Parameter getCurrentColumnTemplate(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		try {
			String userId = (String) inquiryContext.getParameter(SrmQueryTemplateDTO.ATTRIBUTE.USER_ID.getValue());
			boolean isSettingOther = this.srmQueryTemplateDAO.isSettingOther(userId);
			
			List<Parameter> result = this.srmQueryTemplateDAO.getUserColumnTemplateList(userId, true, isSettingOther);
			if(!CollectionUtils.isEmpty(result)){
				return result.get(0);
			}
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("getUserColumnTemplateList()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getUserColumnTemplateList()", "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISrmQueryTemplateService#getUserColumnTemplate(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public Map getUserColumnTemplate(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		try {
			Map result = new HashMap();
			String templateId = (String) inquiryContext.getParameter(SrmQueryTemplateDTO.ATTRIBUTE.TEMPLATE_ID.getValue());
			// 用戶模板的DMO
			SrmQueryTemplate userColumnTemplate = this.srmQueryTemplateDAO.findByPrimaryKey(SrmQueryTemplate.class, templateId);
			if(userColumnTemplate != null){
				result.put(SrmQueryTemplateDTO.ATTRIBUTE.FIELD_CONTENT.getValue(), userColumnTemplate.getFieldContent());
				result.put(SrmQueryTemplateDTO.ATTRIBUTE.TEMPLATE_NAME.getValue(), userColumnTemplate.getTemplateName());
			}
			return result;
		} catch (DataAccessException e) {
			LOGGER.error("getUserColumnTemplateList()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getUserColumnTemplateList()", "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * @return the srmQueryTemplateDAO
	 */
	public ISrmQueryTemplateDAO getSrmQueryTemplateDAO() {
		return srmQueryTemplateDAO;
	}

	/**
	 * @param srmQueryTemplateDAO the srmQueryTemplateDAO to set
	 */
	public void setSrmQueryTemplateDAO(ISrmQueryTemplateDAO srmQueryTemplateDAO) {
		this.srmQueryTemplateDAO = srmQueryTemplateDAO;
	}

}
