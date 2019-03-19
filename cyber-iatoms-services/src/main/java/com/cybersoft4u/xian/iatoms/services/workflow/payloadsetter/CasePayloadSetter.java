package com.cybersoft4u.xian.iatoms.services.workflow.payloadsetter;

import com.cybersoft4u.xian.iatoms.common.bean.dto.CaseDetailDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.IAtomsCaseFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.workflow.CaseProcessPayloadContext;

import cafe.core.bean.CoreMessageCode;
import cafe.core.service.ServiceException;
import cafe.core.util.convert.ConvertException;
import cafe.workflow.service.util.AbstractPerformHumanTaskPayloadSetter;

/**
 * Purpose:案件處理payloadSetter 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2017年3月10日
 * @MaintenancePersonnel evanliu
 */
public class CasePayloadSetter extends AbstractPerformHumanTaskPayloadSetter<IAtomsCaseFormDTO, com.cybersoft4u.xian.iatoms.common.bean.workflow.CaseProcessPayloadContext> {
	/**
	 * Constructor:
	 */
	public CasePayloadSetter() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Purpose:请假/销假起案activiti参数设定
	 * @author candicechen
	 * @param fromObject:caseFormDTO
	 * @param payloadContext:payload 上下文Context
	 * @throws ConvertException:出错时返回ConvertException
	 * @return void
	 */
	public void set(IAtomsCaseFormDTO fromObject, CaseProcessPayloadContext payloadContext) throws ConvertException {
		super.set(fromObject, payloadContext);
		CaseDetailDTO caseDetailDTO = fromObject.getCaseDetail();
		if (caseDetailDTO == null) throw new ServiceException(CoreMessageCode.ARGUMENT_IS_NULL, new String[]{"案件明細"});
		//leaveType	假别	String
		//dHours	请假时数	String
		//startDate	请假时数起	String
		//endDate	请假时间迄	String
		//agentUser	代理人姓名	String
		/*payloadContext.setToEmail(fromObject.getToEmail());
		payloadContext.setFromEmail(fromObject.getFromEmail());
		if(StringUtils.hasText(fromObject.getCcEmail())){
			payloadContext.setCcEmail(fromObject.getCcEmail());
		}
		payloadContext.setToName(fromObject.getToName());
		payloadContext.setFromName(fromObject.getFromName());
*/		
		//if(fromObject.getCaseHandleInfoDTO() != null){
			//SrmCaseHandleInfoDTO caseHandleInfoDTO = fromObject.getCaseHandleInfoDTO();
			//payloadContext.setCaseCategory(caseHandleInfoDTO.getCaseCategory());
			//設置下一關審核人
			//payloadContext.setCurrentAssigner(fromObject.getNextAssignerId());
			payloadContext.setCaseId(fromObject.getCaseId());
			payloadContext.setCandidateGroup(fromObject.getCandidateGroup());
			/*//下一關審核人姓名
			payloadContext.setCurrentAssignerName(fromObject.getNextAssignerName());
			payloadContext.setCaseCategory(fromObject.getCurrentAssigner());*/
			/*payloadContext.setCurrentAssigneeUsers(fromObject.getNextAssignerId());
			payloadContext.setCurrentAssigner(fromObject.getNextAssignerId());
			payloadContext.setCurrentAssignerName(fromObject.getNextAssignerName());*/
			//payloadContext.setCustomerId(caseHandleInfoDTO.getCustomerId());
			
			payloadContext.setOutcome(fromObject.getOutcome());
			
		//}
	}
}
