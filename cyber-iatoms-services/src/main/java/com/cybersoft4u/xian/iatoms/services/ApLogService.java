package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmSystemLoggingDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IApLogDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmSystemLogging;

/**
 * 
 * Purpose: 系統日志記錄
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月5日
 * @MaintenancePersonnel ericdu
 */
@SuppressWarnings("rawtypes")
public class ApLogService extends AtomicService implements IApLogService {
	
	/**
	 * 系統日志記錄
	 */
	private static final CafeLog log = CafeLogFactory.getLog(ApLogService.class);
	
	/**
	 * 系統日志記錄DAO
	 */
	private IApLogDAO apLogDAO;
	
	/**
	 * 
	 * Constructor: 無參構造子
	 */
	public ApLogService (){
		super();
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IApLogService#log(cafe.core.context.SessionContext)
	 */
	public void log(SessionContext sessionContext) throws ServiceException {
		try{
			if(sessionContext != null){
				AdmSystemLoggingDTO systemLoggingDTO = (AdmSystemLoggingDTO) sessionContext.getRequestParameter();
				if(systemLoggingDTO != null){
					AdmSystemLogging systemLogging = new AdmSystemLogging();
					SimpleDtoDmoTransformer transformer = new SimpleDtoDmoTransformer();
					systemLogging = (AdmSystemLogging) transformer.transform(systemLoggingDTO, systemLogging);
					this.apLogDAO.getDaoSupport().save(systemLogging);
					this.apLogDAO.getDaoSupport().flush();
				}
			}
		}catch(Exception e){
			log.error(this.getClass().getName()+".log() is error:"+e,e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE,new String[]{this.getMyName()});
		}
		
	}

	/**
	 * @return the apLogDAO
	 */
	public IApLogDAO getApLogDAO() {
		return apLogDAO;
	}

	/**
	 * @param apLogDAO the apLogDAO to set
	 */
	public void setApLogDAO(IApLogDAO apLogDAO) {
		this.apLogDAO = apLogDAO;
	}

}
