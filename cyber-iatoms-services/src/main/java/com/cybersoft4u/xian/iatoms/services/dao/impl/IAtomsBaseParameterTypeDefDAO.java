package com.cybersoft4u.xian.iatoms.services.dao.impl;

import com.cybersoft4u.xian.iatoms.services.dao.IIAtomsBaseParameterTypeDefDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterTypeDefIAtoms;

import cafe.core.config.GenericConfigManager;
import cafe.core.dao.AbstractSimpleSettingDAO;
import cafe.core.dao.parameter.impl.BaseParameterTypeDefDAO;
import cafe.core.dao.support.IGenericDaoSupport;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

public class IAtomsBaseParameterTypeDefDAO extends AbstractSimpleSettingDAO<BaseParameterTypeDefIAtoms> implements IIAtomsBaseParameterTypeDefDAO {
	private static CafeLog log = CafeLogFactory.getLog(GenericConfigManager.DAO, BaseParameterTypeDefDAO.class);
	public IAtomsBaseParameterTypeDefDAO() {
		super();
	}

	public IAtomsBaseParameterTypeDefDAO(IGenericDaoSupport daoSupport) {
		super(daoSupport);
	}

}
