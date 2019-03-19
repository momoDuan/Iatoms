package com.cybersoft4u.xian.iatoms.web.controllers;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SrmCaseTemplatesFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
/**
 * Purpose:  工單範本維護Controller
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/9/23
 * @MaintenancePersonnel HermanWang
 */
public class SrmCaseTemplatesController extends AbstractMultiActionController<SrmCaseTemplatesFormDTO> {
	/**
	 * 日志记录物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmCaseTemplatesController.class);
	/**
	 * Constructor:无参构造函数
	 */
	public SrmCaseTemplatesController() {
		this.setCommandClass(SrmCaseTemplatesFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(SrmCaseTemplatesFormDTO command) throws CommonException {
		return true;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public SrmCaseTemplatesFormDTO parse(HttpServletRequest request, SrmCaseTemplatesFormDTO command) throws CommonException {
		return command;
	}
	/**
	 * Purpose: 下載
	 * @author HermanWang
	 * @param request: 請求對象
	 * @param response: 響應對象 
	 * @param command: formDTO
	 * @throws CommonException: 出錯時拋出CommonException
	 * @return void
	 */
	public void download(HttpServletRequest request, HttpServletResponse response, SrmCaseTemplatesFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_SRM_CASE_TEMPLATES_SERVICE, IAtomsConstants.ACTION_GET_FILE_PATH, command);
			if (sessionContext != null) {
				command = (SrmCaseTemplatesFormDTO) sessionContext.getResponseResult();
				//下載
				FileUtils.download(request, response, command.getPath(), null);
			}
		} catch (Exception e) {
			LOGGER.error("Exception----download()", e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:工單範本的匯入
	 * @author HermanWang
	 * @param request：請求
	 * @param response：響應
	 * @param command：SrmCaseTemplatesFormDTO對象
	 * @throws CommonException：出错时抛出CommonException异常
	 * @return ModelAndView：返回ModelAndView
	 */
	public ModelAndView upload(HttpServletRequest request, HttpServletResponse response, SrmCaseTemplatesFormDTO command) throws CommonException{
		ModelAndView mav = null;
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap(); 
			command.setFileMap(fileMap);
			MultipartFile uploadFiled = null;
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				//上传文件 
				uploadFiled = entity.getValue();
				command.setUploadFiled(uploadFiled);
			}
			// 调service方法
			SessionContext ctx = this.getServiceLocator().doService(null, IAtomsConstants.SERVICE_SRM_CASE_TEMPLATES_SERVICE, IAtomsConstants.ACTION_UPLOAD, command);
			// 返回 ModelAndView
			return new ModelAndView(this.getSuccessView(IAtomsConstants.ACTION_UPLOAD), IAtomsConstants.PARAM_SESSION_CONTEXT, ctx);
		} catch (Exception e) {
			LOGGER.error(".upload() Exception:", e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		//return mav;
	}
}
