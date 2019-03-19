package com.cybersoft4u.xian.iatoms.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.web.controller.AbstractBaseFormController;

import com.cybersoft4u.xian.iatoms.common.IAtomsStaticPage;

public class ContactInfoController <T extends IAtomsStaticPage> extends AbstractBaseFormController<T> {

	/** 
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(T arg0) throws CommonException {
		// TODO Auto-generated method stub
		return false;
	}

	/** 
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractBaseFormController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public T parse(HttpServletRequest arg0, T arg1) throws CommonException {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractBaseFormController#postSubmited(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, cafe.core.context.SessionContext)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ModelAndView postSubmited(HttpServletRequest arg0,
			HttpServletResponse arg1, SessionContext arg2)
			throws CommonException {
		// TODO Auto-generated method stub
		return null;
	}

}