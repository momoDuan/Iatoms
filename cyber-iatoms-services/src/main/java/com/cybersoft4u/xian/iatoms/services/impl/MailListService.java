package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MailListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.MailListManageFormDTO;
import com.cybersoft4u.xian.iatoms.services.IMailListService;
import com.cybersoft4u.xian.iatoms.services.dao.IMailListDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMailList;

/** 
 * Purpose: 電子郵件群組維護service
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016年7月4日
 * @MaintenancePersonnel CarrieDuan
 */
public class MailListService extends AtomicService implements IMailListService {

	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, MailListService.class);
	
	/**
	 * 電子郵件羣組維護DAO
	 */
	private IMailListDAO mailListDAO; 
	
	/**
	 * 無慘構造函數
	 */
	public MailListService() {
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMailListService#init(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		// 页面显示信息
		Message msg = null;
		try {
			MailListManageFormDTO formDTO = (MailListManageFormDTO) sessionContext.getRequestParameter();
			sessionContext.setResponseResult(formDTO);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOGGER.error(".init(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMailListService#query(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			MailListManageFormDTO formDTO = (MailListManageFormDTO) sessionContext.getRequestParameter();
			//獲取總頁數
			Integer totalSize = this.mailListDAO.count(formDTO.getQueryNoticeType(), formDTO.getQueryName());
			//如果總也數為0，則返回查無數據
			if (Integer.valueOf(0).equals(totalSize)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			} else {
				//查詢當前頁
				List<MailListDTO> mailListDTOs = this.mailListDAO.listBy(formDTO.getQueryNoticeType(), formDTO.getQueryName(), formDTO.getPage().intValue(),
						formDTO.getRows().intValue(), formDTO.getSort(), formDTO.getOrder());
				//查詢結果不為空，保存查詢接過信息，返回
				if (!CollectionUtils.isEmpty(mailListDTOs)) {
					for (MailListDTO mailListDTO : mailListDTOs) {
						//將修改人員名稱改為中文
						String updatedByName = mailListDTO.getUpdatedByName();
						if(StringUtils.hasText(updatedByName) && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
							mailListDTO.setUpdatedByName(updatedByName.substring(updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
						}
					}
					formDTO.setList(mailListDTOs);
					formDTO.getPageNavigation().setRowCount(totalSize.intValue());
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
				}
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error("query() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}  catch (Exception e) {
			LOGGER.error("query(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMailListService#save(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	@Override
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		// 页面显示信息
		MailListDTO mailListDTO = null;
		try {
			MailListManageFormDTO formDTO = (MailListManageFormDTO) sessionContext.getRequestParameter();
			mailListDTO = formDTO.getMailListDTO();
			if (mailListDTO != null) {
				//電子郵件羣組維護主鍵，null爲新增，not null爲修改
				String mailId = mailListDTO.getMailId();
				String mailGroup = mailListDTO.getMailGroup();
				String mail = mailListDTO.getEmail();
				Boolean isRepeat = this.mailListDAO.isCheck(mailGroup, mail, mailId);
				if (isRepeat.booleanValue()) {
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.MAIL_ADDRESS_REPEATED));
					return sessionContext;
				}
				IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
				//電子郵件羣組維護DMO
				BimMailList mailList = new BimMailList();
				Transformer transformer = new SimpleDtoDmoTransformer();
				transformer.transform(mailListDTO, mailList);
				if (mailId != null) { 
					//修改
					sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()}));
				} else {
					//生成主键
					String mailIdString = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_MAIL_LIST);
					//新增
					mailList.setMailId(mailIdString);
					mailList.setCreatedById(logonUser.getId());
					mailList.setCreatedByName(logonUser.getName());
					mailList.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()}));
				}
				mailList.setUpdatedById(logonUser.getId());
				mailList.setUpdatedByName(logonUser.getName());
				mailList.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				this.mailListDAO.getDaoSupport().saveOrUpdate(mailList);
				this.mailListDAO.getDaoSupport().flush();
			}
		} catch (DataAccessException e) {
			LOGGER.error(".save() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SAVE_FAILURE));
			LOGGER.error(".save(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMailListService#delete(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		try {
			MailListManageFormDTO formDTO = (MailListManageFormDTO) sessionContext.getRequestParameter();
			String mailId = formDTO.getMailId();
			Message msg = null;
			BimMailList mailList = this.mailListDAO.findByPrimaryKey(BimMailList.class, mailId);
			if (mailList != null) {
				//刪除電子郵件羣組
				this.mailListDAO.getDaoSupport().delete(mailId, BimMailList.class);
				this.mailListDAO.getDaoSupport().flush();
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_ALERDAY_UPDATE);
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error(".delete() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".delete is Failed !!!", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * @return the mailListDAO
	 */
	public IMailListDAO getMailListDAO() {
		return mailListDAO;
	}
	/**
	 * @param mailListDAO the mailListDAO to set
	 */
	public void setMailListDAO(IMailListDAO mailListDAO) {
		this.mailListDAO = mailListDAO;
	}
}
