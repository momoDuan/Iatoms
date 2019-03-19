package com.cybersoft4u.xian.iatoms.web.controllers;

import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.cybersoft4u.xian.iatoms.services.impl.ContractSlaService;
/**
 * Purpose: 合約SLA設定Controller
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel CrissZhang
 */
public class ContractSlaController extends AbstractMultiActionController<ContractSlaFormDTO>{
	/**
	 * 系统日志记录工具
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, ContractSlaController.class);
	
	/**
	 * Constructor:無參構造函數
	 */
	public ContractSlaController(){
		this.setCommandClass(ContractSlaFormDTO.class);
	}
	/** (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(ContractSlaFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		Message msg = null;
		String actionId = command.getActionId();
		if(IAtomsConstants.ACTION_SAVE.equals(actionId)){
			//檢驗頁面輸入
			ContractSlaDTO contractSlaDTO = command.getContractSlaDTO();
			if (contractSlaDTO == null) {
				return false;
			}
			String customerId = contractSlaDTO.getCustomerId();
			//若未選擇客戶
			if(!StringUtils.hasText(customerId)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_CUSTOMER)});
				throw new CommonException(msg);
			}
			String contractId = contractSlaDTO.getContractId();
			//若未選擇合約編號
			if(!StringUtils.hasText(contractId)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_CONTRACT_ID)});
				throw new CommonException(msg);
			}
			//若未選擇案件類別
			String ticketType = contractSlaDTO.getTicketType();
			if(!StringUtils.hasText(ticketType)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_TICKET_TYPE)});
				throw new CommonException(msg);
			}
			//若未選擇區域
			String location = contractSlaDTO.getLocation();
			if(!StringUtils.hasText(location)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_LOCATION)});
				throw new CommonException(msg);
			}
			//若未選擇案件類型
			String ticketMode = contractSlaDTO.getTicketMode();
			if(!StringUtils.hasText(ticketMode)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_TICKET_MODE)});
				throw new CommonException(msg);
			}
			// 若未選擇上班日
			String isWorkDay = contractSlaDTO.getIsWorkDay();
			if(!StringUtils.hasText(isWorkDay)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_IS_WORK_DAY)});
				throw new CommonException(msg);
			}
			// 若未選擇當天件
			String isThatDay = contractSlaDTO.getIsThatDay();
			if(!StringUtils.hasText(isThatDay)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_IS_THAT_DAY)});
				throw new CommonException(msg);
			}
			// 當天件選是當天件建案時間
			if(IAtomsConstants.YES.equals(isThatDay)){
				// 當天件建案時間
				String thatDayTime = contractSlaDTO.getThatDayTime();
				if(!StringUtils.hasText(thatDayTime)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_THAT_DAY_TIME)});
					throw new CommonException(msg);
				} else {
					if(!ValidateUtils.valiDataTime(thatDayTime)){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_THAT_DAY_TIME)});
						throw new CommonException(msg);
					}
				}
			}
			// 若未輸入到場時效
			Double arriveHour = contractSlaDTO.getArriveHour();
			if(arriveHour == null){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_HOUR)});
				throw new CommonException(msg);
			} else {
				if(!ValidateUtils.varcharLength(arriveHour.toString(), 3, 7)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_HOUR)});
					throw new CommonException(msg);
				} else {
					if(!ValidateUtils.oneBitFloat(arriveHour.toString())){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_POSITIVE_INTEGER, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_HOUR)});
						throw new CommonException(msg);
					} else {
						if(IAtomsConstants.YES.equals(isThatDay)){
							if(arriveHour.intValue() % 24 != 0){
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.MULTIPLE_OF_TWENTY_FOUR, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_HOUR)});
								throw new CommonException(msg);
							}
						}
					}
				}
			}
			// 若未輸入到場警示
			Double arriveWarnning = contractSlaDTO.getArriveWarnning();
			if(arriveWarnning == null){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_WARNNING)});
				throw new CommonException(msg);
			} else {
				if(!ValidateUtils.varcharLength(arriveWarnning.toString(), 3, 7)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_WARNNING)});
					throw new CommonException(msg);
				} else {
					if(!ValidateUtils.oneBitFloat(arriveWarnning.toString())){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_POSITIVE_INTEGER, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_WARNNING)});
						throw new CommonException(msg);
					} else{
						if(arriveWarnning.intValue() > arriveHour.intValue()){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_MORE_THEN, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_WARNNING), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_HOUR)});
							throw new CommonException(msg);
						}
					}
				}
			}
			// 若未輸入完修時效
			Double completeHour = contractSlaDTO.getCompleteHour();
			if(completeHour == null){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_HOUR)});
				throw new CommonException(msg);
			} else {
				if(!ValidateUtils.varcharLength(completeHour.toString(), 3, 7)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_HOUR)});
					throw new CommonException(msg);
				} else {
					if(!ValidateUtils.oneBitFloat(completeHour.toString())){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_POSITIVE_INTEGER, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_HOUR)});
						throw new CommonException(msg);
					} else{
						if(IAtomsConstants.YES.equals(isThatDay)){
							if(completeHour.intValue() % 24 != 0){
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.MULTIPLE_OF_TWENTY_FOUR, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_HOUR)});
								throw new CommonException(msg);
							}
						}
					}
				}
			}
			// 若未輸入完修警示
			Double completeWarnning = contractSlaDTO.getCompleteWarnning();
			if(completeWarnning == null){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_WARNNING)});
				throw new CommonException(msg);
			} else {
				if(!ValidateUtils.varcharLength(completeWarnning.toString(), 3, 7)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_WARNNING)});
					throw new CommonException(msg);
				} else {
					if(!ValidateUtils.oneBitFloat(completeWarnning.toString())){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_POSITIVE_INTEGER, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_WARNNING)});
						throw new CommonException(msg);
					} else{
						if(completeWarnning.intValue() > completeHour.intValue()){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_MORE_THEN, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_WARNNING), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_HOUR)});
							throw new CommonException(msg);
						}
					}
				}
			}
			// 回應時效驗證
			Double responseHour = contractSlaDTO.getResponseHour();
			if(responseHour != null){
				if(!ValidateUtils.varcharLength(responseHour.toString(), 3, 7)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_RESPONSE_HOUR)});
					throw new CommonException(msg);
				} else {
					if(!ValidateUtils.oneBitFloat(responseHour.toString())){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_POSITIVE_INTEGER, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_RESPONSE_HOUR)});
						throw new CommonException(msg);
					}
				}
			}
			// 回應警示驗證
			Double responseWarnning = contractSlaDTO.getResponseWarnning();
			if(responseWarnning != null){
				if(!ValidateUtils.varcharLength(responseWarnning.toString(), 3, 7)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_RESPONSE_WARNNING)});
					throw new CommonException(msg);
				} else {
					if(!ValidateUtils.oneBitFloat(responseWarnning.toString())){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_POSITIVE_INTEGER, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_RESPONSE_WARNNING)});
						throw new CommonException(msg);
					} else{
						if(responseHour != null){
							if(responseWarnning.intValue() > responseHour.intValue()){
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_MORE_THEN, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_RESPONSE_WARNNING), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_RESPONSE_HOUR)});
								throw new CommonException(msg);
							}
						}
					}
				}
			}
			// 說明驗證
			String comment = contractSlaDTO.getComment();
			if(StringUtils.hasText(comment)){
				if(!ValidateUtils.length(comment, 1, 200)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMMENT), IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
					throw new CommonException(msg);
				}
			}
		}
		if(IAtomsConstants.ACTION_COPY.equals(actionId)){
			String customerId = command.getOriginalCustomerId();
			//若未選擇客戶
			if(!StringUtils.hasText(customerId)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_CUSTOMER)});
				throw new CommonException(msg);
			}
			String contractId = command.getOriginalContractId();
			//若未選擇合約編號
			if(!StringUtils.hasText(contractId)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_CONTRACT_ID)});
				throw new CommonException(msg);
			}
			String copyCustomerId = command.getCopyCustomerId();
			//若未選擇複製合約編號
			if(!StringUtils.hasText(copyCustomerId)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COPY_CUSTOMER)});
				throw new CommonException(msg);
			}
			String copyContractId = command.getCopyContractId();
			//若未選擇複製合約編號
			if(!StringUtils.hasText(copyContractId)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COPY_CONTRACT_ID)});
				throw new CommonException(msg);
			}
		}
		return true;
	}

	/** (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public ContractSlaFormDTO parse(HttpServletRequest request, ContractSlaFormDTO command) throws CommonException {
		try {
			// 得到actionId
			String actionId = command.getActionId();
			if (!StringUtils.hasText(actionId)) {
				actionId = IAtomsConstants.ACTION_INIT;
			} else if (IAtomsConstants.ACTION_SAVE.equals(actionId)){
				ContractSlaDTO contractSlaDTO = BindPageDataUtils.bindValueObject(request, ContractSlaDTO.class);
				command.setContractSlaDTO(contractSlaDTO);
			}
		} catch (Exception e) {
			LOGGER.error("parse()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}	
	
	/**
	 * Purpose:汇入格式下载
	 * @author CrissZhang
	 * @param request ：请求
	 * @param response ：响应
	 * @param command ：FormDTO对象
	 * @throws CommonException ：出错时抛出CommonException异常
	 * @return void ：无返回值
	 */
	public void download(HttpServletRequest request, HttpServletResponse response, ContractSlaFormDTO command) throws CommonException{
		try {
			// 模板名称
			String fileNameCn = ContractSlaFormDTO.CONTRACT_SLA_TEMPLATE_NAME_FOR_CN;
			String fileNameEn = ContractSlaFormDTO.CONTRACT_SLA_TEMPLATE_NAME_FOR_EN;
			// 模板路径
			String tempPath = IAtomsConstants.TEMPLATE_DOWNLOAD_PATH;
			StringBuffer buffer = new StringBuffer();
			buffer.append(tempPath).append(fileNameEn);
			String tempFailString = buffer.toString();
		//	String tempFailString = tempPath.concat(fileNameEn);
			InputStream inputStream = ContractSlaService.class.getResourceAsStream(tempFailString);
			FileUtils.download(request, response, inputStream, fileNameCn);
		} catch (Exception e) {
			LOGGER.error("download()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * 
	 * Purpose:汇入错误结果文件下载
	 * @author CrissZhang
	 * @param request ：请求
	 * @param response ：响应
	 * @param command ：FormDTO对象
	 * @throws CommonException ：出错时抛出CommonException异常
	 * @return void ：无返回值
	 */
	public void downloadErrorFile(HttpServletRequest request, HttpServletResponse response, ContractSlaFormDTO command) throws CommonException{
		try {
			//下載错误信息文件
			String fileName = this.getString(request, ContractSlaFormDTO.ERROR_FILE_NAME);
			String tempPath = this.getString(request, ContractSlaFormDTO.TEMP_FILE_PATH);
			String errorFileName = IAtomsConstants.UPLOAD_ERROR_MESSAGE_FILE_NAME;
		//	FileUtils.download(request,response, tempPath, errorFileName);
			String inputfile = tempPath.concat(IAtomsConstants.MARK_BACKSLASH).concat(fileName);
			FileUtils.download(request, response, inputfile, errorFileName);
			FileUtils.removeFile(inputfile);
		} catch (Exception e) {
			LOGGER.error("downloadErrorFile()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:汇入
	 * @author CrissZhang
	 * @param request ：请求
	 * @param response ：响应
	 * @param command ：FormDTO对象
	 * @throws CommonException ：出错时抛出CommonException异常
	 * @return ModelAndView ：返回ModelAndView
	 */
	public ModelAndView upload(HttpServletRequest request, HttpServletResponse response, ContractSlaFormDTO command) throws CommonException{
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap(); 
			MultipartFile uploadFiled = null;
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {    
				// 上传文件   
				uploadFiled = entity.getValue();  
				command.setUploadFiled(uploadFiled);
			}
			// 调service方法
			SessionContext ctx = this.getServiceLocator().doService(command.getLogonUser(), this.getServiceId(), IAtomsConstants.ACTION_UPLOAD, command);
			// 返回 ModelAndView
			return new ModelAndView(this.getSuccessView(IAtomsConstants.ACTION_UPLOAD), IAtomsConstants.PARAM_SESSION_CONTEXT, ctx);
		} catch (Exception e) {
			LOGGER.error("upload()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}

}
