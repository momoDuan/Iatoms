package com.cybersoft4u.xian.iatoms.services.workflow.impl;


import cafe.core.bean.Message;
import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;
/**
 * Purpose: 流程关卡Service
 * @author RiverJin
 * @since  JDK 1.6
 * @date   2016/09/12
 * @MaintenancePersonnel RiverJin
 */
public class IAtomsHumanTaskActivityService extends AbstractIAtomsHumanTaskActivityService {

	/**
	 * CmsHumanTaskActivityService建構子
	 */
	public IAtomsHumanTaskActivityService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.service.IHumanTaskActivityService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext taskCtx) throws ServiceException {
		// TODO Auto-generated method stub
		taskCtx.setReturnMessage(new Message(Message.STATUS.SUCCESS));
		return taskCtx;
	}
	
}
