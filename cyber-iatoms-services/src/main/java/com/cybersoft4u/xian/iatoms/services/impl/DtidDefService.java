package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.List;

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
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PvmDtidDefDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.DtidDefFormDTO;
import com.cybersoft4u.xian.iatoms.services.IDtidDefService;
import com.cybersoft4u.xian.iatoms.services.dao.IPvmDtidDefDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmDtidDef;

/**
 * Purpose: DTIO帳號管理service實現類
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/9/21
 * @MaintenancePersonnel CarrieDuan
 */
public class DtidDefService extends AtomicService implements IDtidDefService{

	/**
	 * 系统日志记录文件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, DtidDefService.class);
	
	/**
	 * DTIO帳號管理DAO
	 */
	private IPvmDtidDefDAO dtidDefDAO;
	
	/**
	 * 庫存主檔DAO
	 */
	private IDmmRepositoryDAO repositoryDAO;
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IDtidDefService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			DtidDefFormDTO formDTO =  (DtidDefFormDTO) sessionContext.getRequestParameter();
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
	 * @see com.cybersoft4u.xian.iatoms.services.IDtidDefService#query(cafe.core.context.SessionContext)
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		try {
			DtidDefFormDTO dtidDefFormDTO = (DtidDefFormDTO) sessionContext.getRequestParameter();
			Message msg = null;
			//查詢總條數
			Integer count = this.dtidDefDAO.count(dtidDefFormDTO.getQueryCustomer(), dtidDefFormDTO.getQueryAssetTypeId(), dtidDefFormDTO.getQueryDtidStart(), dtidDefFormDTO.getQueryDtidEnd());
			if (Integer.valueOf(0).equals(count)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			} else {
				dtidDefFormDTO.getPageNavigation().setRowCount(count.intValue());
				// 查詢符合條件的數據
				List<PvmDtidDefDTO> pvmDtidDefDTOs = this.dtidDefDAO.listBy(
						dtidDefFormDTO.getQueryCustomer(), dtidDefFormDTO.getQueryAssetTypeId(), dtidDefFormDTO.getQueryDtidStart(), dtidDefFormDTO.getQueryDtidEnd(),
						dtidDefFormDTO.getSort(), 
						dtidDefFormDTO.getOrder(), 
						dtidDefFormDTO.getPage().intValue(), 
						dtidDefFormDTO.getRows().intValue(), true);
				if (!CollectionUtils.isEmpty(pvmDtidDefDTOs)) {
					for (PvmDtidDefDTO pvmDtidDefDTO : pvmDtidDefDTOs) {
						String createdByName = pvmDtidDefDTO.getCreatedByName();
						if(StringUtils.hasText(createdByName) && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
							pvmDtidDefDTO.setCreatedByName(createdByName.substring(createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
						}
						String updatedByName = pvmDtidDefDTO.getUpdatedByName();
						if(StringUtils.hasText(updatedByName) && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
							pvmDtidDefDTO.setUpdatedByName(updatedByName.substring(updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
						}
					}
					dtidDefFormDTO.setList(pvmDtidDefDTOs);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
				}
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(dtidDefFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(".query() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".query() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IDtidDefService#initEdit(cafe.core.context.SessionContext)
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException {
		PvmDtidDefDTO pvmDtidDefDTO = null;
		try {
			DtidDefFormDTO dtidDefFormDTO = (DtidDefFormDTO) sessionContext.getRequestParameter();
			if (dtidDefFormDTO != null) {
				//update by hermanwang 2017/05/17 添加dtidId
				PvmDtidDef pvmDtidDef = this.dtidDefDAO.findByPrimaryKey(PvmDtidDef.class, dtidDefFormDTO.getDtidId());
				if (pvmDtidDef != null) {
					Transformer transformer = new SimpleDtoDmoTransformer();
					pvmDtidDefDTO = (PvmDtidDefDTO) transformer.transform(pvmDtidDef, new PvmDtidDefDTO());
				}
				dtidDefFormDTO.setDtidDefDTO(pvmDtidDefDTO);
			}
			sessionContext.setResponseResult(dtidDefFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(".initEdit() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".initEdit() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IDtidDefService#initAdd(cafe.core.context.SessionContext)
	 */
	public SessionContext initAdd(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			DtidDefFormDTO formDTO =  (DtidDefFormDTO) sessionContext.getRequestParameter();
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
	 * @see com.cybersoft4u.xian.iatoms.services.IDtidDefService#save(cafe.core.context.SessionContext)
	 */
	public SessionContext save(SessionContext sessionContext) throws SecurityException {
		try {
			Message message = null;
			DtidDefFormDTO dtidDefFormDTO = (DtidDefFormDTO) sessionContext.getRequestParameter();
			PvmDtidDefDTO pvmDtidDefDTO = dtidDefFormDTO.getDtidDefDTO();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) dtidDefFormDTO.getLogonUser();
			boolean isRepeat = false;
			if (pvmDtidDefDTO != null) {
				String id = pvmDtidDefDTO.getId();
				Transformer transformer = new SimpleDtoDmoTransformer();
				PvmDtidDef pvmDtidDef = null;
				if (StringUtils.hasText(id)) {
					pvmDtidDef = this.dtidDefDAO.findByPrimaryKey(PvmDtidDef.class, id);
					if (pvmDtidDef != null) {
						String dtidStart = null;
						//判斷是否存在目前號碼
						if (StringUtils.hasText(pvmDtidDef.getCurrentNumber())) {
							dtidStart = pvmDtidDef.getDtidStart();
						} else {
							dtidStart = pvmDtidDefDTO.getDtidStart();
						}
						//判斷dtid區間是否重複
						isRepeat = this.dtidDefDAO.isCheckRepeat(Integer.valueOf(dtidStart), Integer.valueOf(pvmDtidDefDTO.getDtidEnd()), id);
						if (!isRepeat) {
							message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DTID_IS_REPEAT);
						} else {
							pvmDtidDef.setDtidStart(dtidStart);
							pvmDtidDef.setDtidEnd(pvmDtidDefDTO.getDtidEnd());
							pvmDtidDef.setComment(pvmDtidDefDTO.getComment());
							message = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String []{this.getMyName()});
						}
					}
					
				} else {
					isRepeat = this.dtidDefDAO.isCheckRepeat(Integer.valueOf(pvmDtidDefDTO.getDtidStart()), Integer.valueOf(pvmDtidDefDTO.getDtidEnd()), id);
					if (!isRepeat) {
						message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DTID_IS_REPEAT);
					} else {
						pvmDtidDef = (PvmDtidDef) transformer.transform(pvmDtidDefDTO, new PvmDtidDef());
						pvmDtidDef.setId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_PVM_DTID_DEF));
						pvmDtidDef.setCreatedById(logonUser.getId());
						pvmDtidDef.setCreatedByName(logonUser.getName());
						pvmDtidDef.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
						message = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String []{this.getMyName()});
					}
				}
				if (isRepeat) {
					pvmDtidDef.setUpdatedById(logonUser.getId());
					pvmDtidDef.setUpdatedByName(logonUser.getName());
					pvmDtidDef.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					//保存或修改
					this.dtidDefDAO.getDaoSupport().saveOrUpdate(pvmDtidDef);
					//flush
					this.dtidDefDAO.getDaoSupport().flush();
				}
			}
			sessionContext.setReturnMessage(message);
		} catch (DataAccessException e) {
			LOGGER.error(".save() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".save() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IDtidDefService#delete(cafe.core.context.SessionContext)
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		try {
			DtidDefFormDTO dtidDefFormDTO = (DtidDefFormDTO) sessionContext.getRequestParameter();
			LOGGER.debug(this.getClass().getName(), ".delete()--->", "parameters:dtidDefFormDTO="+dtidDefFormDTO);
			if (dtidDefFormDTO != null) {
				//update by hermanwang 2017/05/17 添加dtidId
				String id = dtidDefFormDTO.getDtidId();
				//String id = dtidDefFormDTO.getId();
				LOGGER.debug(this.getClass().getName(), ".delete()--->", "parameters:id="+id);
				if (StringUtils.hasText(id)) {
					PvmDtidDef pvmDtidDef= this.dtidDefDAO.findByPrimaryKey(PvmDtidDef.class, id);
					LOGGER.debug(this.getClass().getName(), ".delete()--->", "parameters:pvmDtidDef="+pvmDtidDef);
					if (pvmDtidDef != null) {
						this.dtidDefDAO.delete(pvmDtidDef);
						this.dtidDefDAO.getDaoSupport().flush();
						sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String []{this.getMyName()})); 
					} else {
						sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String []{this.getMyName()}));
					}
				} else {
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String []{this.getMyName()}));
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(".delete() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String []{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".delete() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String []{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IDtidDefService#isUseDtid(cafe.core.context.MultiParameterInquiryContext)
	 */
	public Boolean isUseDtid(MultiParameterInquiryContext param) throws ServiceException {
		try {
			String id = (String) param.getParameter(PvmDtidDefDTO.ATTRIBUTE.ID.getValue());
			if (StringUtils.hasText(id)) {
				PvmDtidDef pvmDtidDef= this.dtidDefDAO.findByPrimaryKey(PvmDtidDef.class, id);
				if (pvmDtidDef != null) {
					if (StringUtils.hasText(pvmDtidDef.getCurrentNumber())) {
						return true;
					} else {
						return false;
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(".isUseDtid() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String []{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".isUseDtid() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String []{this.getMyName()}, e);
		}
		return null;
	}

	/**
	 * @return the dtidDefDAO
	 */
	public IPvmDtidDefDAO getDtidDefDAO() {
		return dtidDefDAO;
	}

	/**
	 * @param dtidDefDAO the dtidDefDAO to set
	 */
	public void setDtidDefDAO(IPvmDtidDefDAO dtidDefDAO) {
		this.dtidDefDAO = dtidDefDAO;
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

	

}
