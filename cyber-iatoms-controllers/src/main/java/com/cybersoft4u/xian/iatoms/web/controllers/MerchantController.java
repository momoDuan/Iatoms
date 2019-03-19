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
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.cybersoft4u.xian.iatoms.services.impl.ContractSlaService;

/**
 * Purpose: 客戶特店維護Controller
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/6/14
 * @MaintenancePersonnel DavidZheng
 */
public class MerchantController extends AbstractMultiActionController<MerchantFormDTO>{
	
	/**
	 * 日志记录物件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, MerchantController.class);	
	
	/**
	 * Constructor:无参构造函数
	 */
	public MerchantController() {
		this.setCommandClass(MerchantFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(MerchantFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			//檢驗頁面輸入
			MerchantDTO bimMerchantDTO = command.getBimMerchantDTO();
			if (bimMerchantDTO == null) {
				return false;
			}
			//String merchantId = bimMerchantDTO.getMerchantId();
			String companyId = bimMerchantDTO.getCompanyId();
			String merchantCode = bimMerchantDTO.getMerchantCode();
			//String stagesMerchantCode = bimMerchantDTO.getStagesMerchantCode();
			String name = bimMerchantDTO.getName();
			String remark = bimMerchantDTO.getRemark();
			//未輸入客戶
			if (!StringUtils.hasText(companyId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_MERCHANT_ID)});
				throw new CommonException(msg);
			}
			//檢核特店代號
			if (!StringUtils.hasText(merchantCode)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_MERCHANT_CODE)});
				throw new CommonException(msg);
			} else {
				if (!ValidateUtils.numberOrEnglish(merchantCode)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_LIMIT_ENGLISH_AND_NUMBER, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_MERCHANT_CODE)});
					throw new CommonException(msg);
				} else {
					if (!ValidateUtils.length(merchantCode, 0, 20)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_MERCHANT_CODE), 
								IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
						throw new CommonException(msg);
					}
				}
				
			}
			/*//檢核分期特店代號
			if(StringUtils.hasText(stagesMerchantCode)){
				if (!ValidateUtils.numberOrEnglish(stagesMerchantCode)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_LIMIT_ENGLISH_AND_NUMBER, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_STAGES_MERCHANT_CODE)});
					throw new CommonException(msg);
				} else {
					if (!ValidateUtils.length(stagesMerchantCode, 0, 20)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_STAGES_MERCHANT_CODE)});
						throw new CommonException(msg);
					}
				}
			}*/
			//檢核特店名稱
			if (!StringUtils.hasText(name)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_NAME)});
				throw new CommonException(msg);
			} else {
				if (!ValidateUtils.length(name, 0, 50)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_NAME), 
							IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			
			//Task #3249 統一編號 --- 長度小於10 英數字
			String unityNumber = bimMerchantDTO.getUnityNumber();
			if (StringUtils.hasText(unityNumber)){
				if (!ValidateUtils.length(unityNumber, 0, 10)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_UNITY_NUMBER),
							IAtomsConstants.MAXLENGTH_NUMBER_TEN});
					throw new CommonException(msg);
				} else {
					if (!ValidateUtils.numberOrEnglish(unityNumber)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_LIMIT_ENGLISH_AND_NUMBER, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_UNITY_NUMBER)});
						throw new CommonException(msg);
					}
				}
			}
			//檢核備註長度
			if (StringUtils.hasText(remark)) {
				if (!ValidateUtils.length(remark, 0, 200)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_REMARK), 
							IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
					throw new CommonException(msg);
				}
			}
		}
		return true;
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public MerchantFormDTO parse(HttpServletRequest request, MerchantFormDTO command) {
		try {
			//獲取actionId
			String actionId = command.getActionId();
			LOGGER.debug(" .parse() ---> actionId : " + actionId);
			if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
				MerchantDTO bimMerchantDTO = BindPageDataUtils.bindValueObject(request, MerchantDTO.class);
				command.setBimMerchantDTO(bimMerchantDTO);
			}
		} catch (Exception e) {
			LOGGER.error(".parse() Exception.", e);
		}
		return command;
	}
	/**
	 * Purpose:客戶特店匯入格式下載
	 * @author HermanWang
	 * @param request：請求
	 * @param response：響應
	 * @param command：MerchantNewFormDTO對象
	 * @throws CommonException:出错时抛出CommonException异常
	 * @return void：無返回值
	 */
	public void download(HttpServletRequest request, HttpServletResponse response, MerchantFormDTO command) throws CommonException{
		try {
			// 模板名称
			String fileNameCn = MerchantFormDTO.MERCHANT_TEMPLATE_NAME_FOR_CN;
			String fileNameEn = MerchantFormDTO.MERCHANT_TEMPLATE_NAME_FOR_EN;
			// 模板路径
			String tempPath = IAtomsConstants.TEMPLATE_DOWNLOAD_PATH;
			StringBuffer buffer = new StringBuffer();
			buffer.append(tempPath).append(fileNameEn);
			String tempFailString = buffer.toString();
			//輸入流
			InputStream inputStream = ContractSlaService.class.getResourceAsStream(tempFailString);
			FileUtils.download(request, response, inputStream, fileNameCn);
		} catch (Exception e) {
			LOGGER.error("download()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:客戶特店的匯入
	 * @author HermanWang
	 * @param request：請求
	 * @param response：響應
	 * @param command：MerchantNewFormDTO對象
	 * @throws CommonException：出错时抛出CommonException异常
	 * @return ModelAndView：返回ModelAndView
	 */
	public ModelAndView upload(HttpServletRequest request, HttpServletResponse response, MerchantFormDTO command) throws CommonException{
		ModelAndView mav = null;
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap(); 
			MultipartFile uploadFiled = null;
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {    
				//上传文件 
				uploadFiled = entity.getValue();  
				command.setUploadFiled(uploadFiled);
			}
			// 调service方法
			SessionContext ctx = this.getServiceLocator().doService(null, IAtomsConstants.SERVICE_MERCHANT_SERVICE, IAtomsConstants.ACTION_UPLOAD, command);
			// 返回 ModelAndView
			return new ModelAndView(this.getSuccessView(IAtomsConstants.ACTION_UPLOAD), IAtomsConstants.PARAM_SESSION_CONTEXT, ctx);
		} catch (Exception e) {
			LOGGER.error(".upload() Exception:", e);
		}
		return mav;
	}
	/**
	 * Purpose:匯入錯誤結果文件下載
	 * @author HermanWang
	 * @param request：請求
	 * @param response：響應
	 * @param command：MerchantNewFormDTO對象
	 * @throws CommonException：出错时抛出CommonException异常
	 * @return void：無返回值
	 */
	public void downloadErrorFile(HttpServletRequest request, HttpServletResponse response, MerchantFormDTO command) throws CommonException{
		try {
			//下載错误信息文件
			String fileName = this.getString(request, MerchantFormDTO.ERROR_FILE_NAME);
			String tempPath = this.getString(request, MerchantFormDTO.TEMP_FILE_PATH);
			String errorFileName = IAtomsConstants.UPLOAD_ERROR_MESSAGE_FILE_NAME;
			String inputfile = tempPath.concat(IAtomsConstants.MARK_BACKSLASH).concat(fileName);
			FileUtils.download(request, response, inputfile, errorFileName);
			FileUtils.removeFile(inputfile);
		} catch (Exception e) {
			LOGGER.error("downloadErrorFile()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
}
