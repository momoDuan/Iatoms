package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.owasp.esapi.StringUtilities;

import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PasswordSettingFormDTO;
import com.cybersoft4u.xian.iatoms.services.IPasswordSettingService;
import com.cybersoft4u.xian.iatoms.services.dao.IPasswordSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmSecurityDef;
/**
 * Purpose: 密碼原則設定service
 * @author jasonzhou
 * @since  JDK 1.6
 * @date   2016-6-17
 * @MaintenancePersonnel jasonzhou
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PasswordSettingService extends AtomicService implements IPasswordSettingService {

	/**
	 * 日誌掛件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, PasswordSettingService.class);
	
	/**
	 * 密碼原則設定DAO
	 */
	private IPasswordSettingDAO passwordSettingDAO;

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPasswordSettingService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext)throws ServiceException {
		PasswordSettingFormDTO passwordSettingFormDTO = null;
		try {
			passwordSettingFormDTO = (PasswordSettingFormDTO) sessionContext.getRequestParameter();
			PasswordSettingDTO passwordSettingDTO = this.passwordSettingDAO.getPasswordSettingInfo();
			if(passwordSettingDTO != null) {
				passwordSettingFormDTO.setPasswordSettingDTO(passwordSettingDTO);
			}
			sessionContext.setResponseResult(passwordSettingFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(".init() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".init(SessionContext sessionContext):" + e, e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPasswordSettingService#save(cafe.core.context.SessionContext)
	 */
	public SessionContext saveSetting(SessionContext sessionContext) throws ServiceException {
		PasswordSettingFormDTO passwordSettingFormDTO = null;
		Map map = new HashMap();
		String id = null;
		try {
			passwordSettingFormDTO = (PasswordSettingFormDTO) sessionContext.getRequestParameter();
			//新增密碼原則信息
			PasswordSettingDTO passwordSettingDTO = passwordSettingFormDTO.getPasswordSettingDTO();
			if(passwordSettingDTO != null){
				if(StringUtilities.isEmpty(passwordSettingDTO.getId())){
					id = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_ADM_SECURITY_DEF);
					passwordSettingDTO.setId(id);
				}
				passwordSettingDTO.setUpdatedById(passwordSettingFormDTO.getLogonUser().getId());
				passwordSettingDTO.setUpdatedByName(passwordSettingFormDTO.getLogonUser().getName());
				passwordSettingDTO.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				//DTO轉換為DMO
				AdmSecurityDef admSecurityDef = new AdmSecurityDef();
				Transformer transformer = new SimpleDtoDmoTransformer();
				admSecurityDef = (AdmSecurityDef) transformer.transform(passwordSettingDTO, admSecurityDef);
				//保存密碼原則信息
				this.passwordSettingDAO.getDaoSupport().saveOrUpdate(admSecurityDef);
				this.passwordSettingDAO.getDaoSupport().flush();
				map.put(IAtomsConstants.UPDATED_DATE, passwordSettingDTO.getUpdatedDate());
				map.put(IAtomsConstants.UPDATED_BY_NAME, passwordSettingDTO.getUpdatedByName());
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.SAVE_DATA_SUCCESS));
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(true));
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		} catch (DataAccessException e) {
			LOGGER.error(".saveSetting() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE);
		} catch (Exception e) {
			LOGGER.error(".saveSetting(SessionContext sessionContext):" + e, e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED);
		}
		return sessionContext;
	}
	
	/**
	 * @return the passwordSettingDAO
	 */
	public IPasswordSettingDAO getPasswordSettingDAO() {
		return passwordSettingDAO;
	}

	/**
	 * @param passwordSettingDAO the passwordSettingDAO to set
	 */
	public void setPasswordSettingDAO(IPasswordSettingDAO passwordSettingDAO) {
		this.passwordSettingDAO = passwordSettingDAO;
	}
}
