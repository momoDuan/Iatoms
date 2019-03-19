package com.cybersoft4u.xian.iatoms.services.impl;

import cafe.core.bean.CoreMessageCode;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.service.ServiceException;
import cafe.workflow.bean.dto.CaseFormDTO;
import cafe.workflow.dmo.CaseTransaction;
import cafe.workflow.service.impl.AbstractCaseDetailService;

import com.cybersoft4u.xian.iatoms.common.bean.dto.CaseDetailDTO;
import com.cybersoft4u.xian.iatoms.services.IIAtomsCaseDetailService;
import com.cybersoft4u.xian.iatoms.services.dmo.CaseDetail;

/**
 * Purpose: 案件资料Service
 * @author RiverJin
 * @since  JDK 1.6
 * @date   2016/09/09
 * @MaintenancePersonnel RiverJin
 */
public class CaseDetailService extends AbstractCaseDetailService<CaseDetail> implements IIAtomsCaseDetailService {

	/**
	 * Constructor:CaseDetailService空构造函数
	 */
	public CaseDetailService() {
		super();
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.service.ICaseDetailService#generateCaseId(cafe.core.context.MultiParameterInquiryContext)
	 */
	public String generateCaseId(MultiParameterInquiryContext param) throws ServiceException {
		return null;
	}
}
