package com.cybersoft4u.xian.iatoms.web.controllers;

import cafe.workflow.bean.dto.AbstractHumanTaskFormDTO;
import cafe.workflow.web.controller.AbstractHumanTaskActivityController;

/**
 * Purpose:抽象案件流程Controller 
 * @author RiverJin
 * @since  JDK 1.6
 * @date   2016/09/14
 * @MaintenancePersonnel RiverJin
 */
public abstract class AbstractIAtomsHumanTaskActivityController<T extends AbstractHumanTaskFormDTO> extends AbstractHumanTaskActivityController<T> {

	/**
	 * AbstractCmsHumanTaskActivityController建構子
	 */
	public AbstractIAtomsHumanTaskActivityController() {
		super();
	}

	/**
	 * AbstractCmsHumanTaskActivityController建構子
	 * @param delegate
	 */
	public AbstractIAtomsHumanTaskActivityController(Object delegate) {
		super(delegate);
	}

}
