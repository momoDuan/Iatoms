package com.cybersoft4u.xian.iatoms.web.controllers;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTransInfoFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.cybersoft4u.xian.iatoms.services.impl.AssetTransferService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
/**
 * Purpose: 設備轉倉單主檔Controller
 * @author Amanda Wang
 * @since  JDK 1.6
 * @date   2016/7/13
 * @MaintenancePersonnel Amanda Wang
 */
public class AssetTransferController extends AbstractMultiActionController<AssetTransInfoFormDTO>{

	/**
	 * 系统日志记录工具
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER,
			AssetTransferController.class);
	
	/**
	 * Constructor:無參構造函數
	 */
	public AssetTransferController(){
		this.setCommandClass(AssetTransInfoFormDTO.class);
	}
	
	/** (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(AssetTransInfoFormDTO parmemters) throws CommonException {
		/*if (parmemters == null) {
			return false;
		}
		Message msg = null;
		//獲取查詢條件 -- 轉出倉 -- 必輸
		int queryFromWarehouseId = parmemters.getQueryFromWarehouseId();
		if (queryFromWarehouseId <= 0) {
			 msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TRANSFER_FROM_WAREHOUSEID)});
				throw new CommonException(msg);
		}
		//獲取查詢條件 -- 轉入倉 -- 必輸
		int queryToWarehouseId = parmemters.getQueryToWarehouseId();
		if (queryToWarehouseId <= 0) {
			 msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TRANSFER_TO_WAREHOUSEID)});
				throw new CommonException(msg);
		}
		//說明 -- 長度小於200
		String comment = parmemters.getComment();
		if ((!StringUtils.hasText(comment)) && (!ValidateUtils.length(comment, 0, 200))) {
			 msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TRANSFER_COMMENT),
					 IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
				throw new CommonException(msg);
		}
		//轉出頁面 -- 新增時
		if (IAtomsConstants.ACTION_ADD_ASSET_TRANS_LIST.equals(parmemters.getActionId())) {
			//設備序號 -- 必輸
			String serialNumber = parmemters.getSerialNumber();
			if (!StringUtils.hasText(serialNumber)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TRANSFER_SERIAL_NUMBER)});
				throw new CommonException(msg);
			}
		}*/
		//前台验证无法卡住换行符，所以放开后台说明栏位验证 2018/01/12
		if (parmemters == null) {
			return false;
		}
		Message msg = null;
		//說明 -- 長度小於200
		String comment = parmemters.getComment();
		if ((StringUtils.hasText(comment)) && (!ValidateUtils.length(comment, 0, 200))) {
			 msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TRANSFER_COMMENT),
					 IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
				throw new CommonException(msg);
		}
		return true;
	}

	/** (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public AssetTransInfoFormDTO parse(HttpServletRequest request, 
			AssetTransInfoFormDTO command) throws CommonException {
		try {
			// 获取actionId
			String actionId = command.getActionId();
			if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
				//綁定頁面值對象
				DmmAssetTransInfoDTO assetTransInfoDTO = BindPageDataUtils.bindValueObject(request, 
						DmmAssetTransInfoDTO.class);
				command.setAssetTransInfoDTO(assetTransInfoDTO);
			} else if (IAtomsConstants.ACTION_UPDATE.equals(actionId) || IAtomsConstants.ACTION_DELETE_ASSET_TRANS_LIST.equals(actionId)) {
				// 獲取轉倉清單中需要儲存的JSON數據
				String assetTransListRow = command.getAssetTransListRow();
				Gson gson = new GsonBuilder().create();
				// 合約中的設備列表轉為LIST
				List<DmmAssetTransListDTO> assetTransDTOList = gson.fromJson(assetTransListRow, 
						new TypeToken<List<DmmAssetTransListDTO>>(){}.getType());
				command.setList(assetTransDTOList);
			} else if (IAtomsConstants.ACTION_UPLOAD.equals(actionId)) {
				//匯入
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Map<String, MultipartFile> fileMap = multipartRequest.getFileMap(); 
				MultipartFile uploadFiled = null;
				for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {    
					// 上传文件   
					uploadFiled = entity.getValue();  
					command.setUploadFiled(uploadFiled);
				}
			}
			return command;
		} catch (Exception e) {
			if (LOGGER != null) {
				LOGGER.error("parse()", "Exception:", e);
			}
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
	}

	/**
	 * Purpose: 匯入格式下載
	 * @author Amanda Wang
	 * @param request: 請求
	 * @param response：響應
	 * @return void
	 */
	public void download(HttpServletRequest request, HttpServletResponse response, 
			AssetTransInfoFormDTO command)throws CommonException{
		try {
			// 模板名称
			String fileNameCn = IAtomsConstants.ASSET_TRANS_TEMPLATE_NAME;
			// 模板路径
			String tempPath = IAtomsConstants.TEMPLATE_DOWNLOAD_PATH;
			//String tempFailString = tempPath + fileNameCn;
			StringBuffer tempFailString = new StringBuffer();
			tempFailString.append(tempPath).append(fileNameCn);
			InputStream inputStream = AssetTransferService.class.getResourceAsStream(tempFailString.toString());
			FileUtils.download(request, response, inputStream, fileNameCn);
		} catch (Exception e) {
			if (LOGGER != null) {
				LOGGER.error("download()", "Exception:", e);
			}
		}
	}
	
	/**
	 * 
	 * Purpose:汇入错误结果文件下载
	 * @author barryzhang
	 * @param request ：请求
	 * @param response ：响应
	 * @param command ：FormDTO对象
	 * @throws CommonException ：出错时抛出CommonException异常
	 * @return void ：无返回值
	 */
	public void downloadErrorFile(HttpServletRequest request, HttpServletResponse response, 
			AssetTransInfoFormDTO command) throws CommonException{
		try {
			//下載错误信息文件
			String fileName = this.getString(request, AssetTransInfoFormDTO.ERROR_FILE_NAME);
			String tempPath = this.getString(request, AssetTransInfoFormDTO.TEMP_ERROR_FILE_PATH);
			String errorFileName = IAtomsConstants.UPLOAD_ERROR_MESSAGE_FILE_NAME;
			FileUtils.download(request, response, tempPath.concat(IAtomsConstants.MARK_BACKSLASH).concat(fileName), IAtomsConstants.UPLOAD_ERROR_MESSAGE_FILE_NAME);
			/*OutputStream outputStream = null;
			FileInputStream fileInputStream = null;
			File file = new File(tempPath, fileName);
			StringBuffer stringBuffer = new StringBuffer();
			try {
				if (file.exists() && file.isFile()) {
					fileInputStream = new FileInputStream(file);  
					if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {  
						errorFileName = URLEncoder.encode(errorFileName, "UTF-8");  
				    } else {  
				    	errorFileName = new String(errorFileName.getBytes("UTF-8"), "ISO8859-1");  
				    }
					outputStream = response.getOutputStream();// 获取文件输出IO流    
					response.setContentType("application/x-download");// 设置response内容的类型
					stringBuffer.append("attachment;filename=").append(errorFileName);
					//response.setHeader("Content-disposition", "attachment;filename=" + errorFileName);// 设置头部信息
					response.setHeader("Content-disposition", stringBuffer.toString());// 设置头部信息
					response.setContentType("application/vnd.ms-excel;charset=UTF-8");
					byte[] buffer = new byte[1024];
					//开始向网络传输文件流    
					int len = -1;
					len = fileInputStream.read(buffer);
					while (len != -1) {    
						outputStream.write(buffer, 0, len);
					}
				} else{
					if (LOGGER != null) {
						LOGGER.error("FileUtils downFile 文件不存在!!!!");
					}
				}
			} catch (Exception e) {
				if (LOGGER != null) {
					LOGGER.error("downloadErrorFile()", "Exception:", e);
				}
			} finally {
				if(fileInputStream != null){
					fileInputStream.close();
				}
				if(outputStream != null){
					outputStream.close();
				}
				
			}
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(tempPath).append(IAtomsConstants.MARK_BACKSLASH).append(fileName);*/
			FileUtils.removeFile(tempPath.concat(IAtomsConstants.MARK_BACKSLASH).concat(fileName));
		} catch (Exception e) {
			if (LOGGER != null) {
				LOGGER.error("downloadErrorFile()", "Exception:", e);
			}
		}
		
	}
	
	/**
	 * Purpose: 匯出
	 * @author Amanda Wang
	 * @param request: 請求
	 * @param response: 響應
	 * @param command: AssetTransInfoFormDTO
	 * @throws CommonException：出錯時拋出CommonException
	 * @return void
	 */
	@SuppressWarnings("rawtypes")
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, 
			AssetTransInfoFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try { 
			if(command != null){
				command.setPage(-1);
				command.setRows(-1);
				IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				//請求service進行數據查詢
				SessionContext returnCtx = this.serviceLocator.doService(logonUser, 
						IAtomsConstants.SERVICE_DMM_ASSET_TRANS_INFO_SERVICE, IAtomsConstants.ACTION_QUERY, command);
				List<DmmAssetTransListDTO> results = null;
				if (returnCtx != null) {
					command = (AssetTransInfoFormDTO) returnCtx.getResponseResult();
					results = command.getList();
					if (!CollectionUtils.isEmpty(results)){
						for (DmmAssetTransListDTO dmmAssetTransListDTO : results) {
							if (IAtomsConstants.YES.equals(dmmAssetTransListDTO.getIsCup())) {
								dmmAssetTransListDTO.setIsCup("是");
							} else {
								dmmAssetTransListDTO.setIsCup("否");
							}
							if (IAtomsConstants.YES.equals(dmmAssetTransListDTO.getIsEnabled())) {
								dmmAssetTransListDTO.setIsEnabled("是");
							} else {
								dmmAssetTransListDTO.setIsEnabled("否");
							}
						}
					}
				}
				//封裝報表對象
				if(!CollectionUtils.isEmpty(results)){
					// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
					criteria.setAutoBuildJasper(false);
					criteria.setResult(results);
					if (command.getIsHistory()!=null && command.getIsHistory()) {
						criteria.setJrxmlName(AssetTransInfoFormDTO.REPORT_JRXML_NAME+"_HIST");
					} else {
						//設置所需報表的Name
						criteria.setJrxmlName(AssetTransInfoFormDTO.REPORT_JRXML_NAME);
					}
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					//設置sheetName
					criteria.setSheetName(AssetTransInfoFormDTO.REPORT_FILE_NAME);
					//設置報表Name
					criteria.setReportFileName(AssetTransInfoFormDTO.REPORT_FILE_NAME);
					ReportExporter.exportReport(criteria, response);
					// 成功標志
					SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
				}
			}
		} catch(Exception e) {
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView(IAtomsConstants.ACTION_EXPORT), map);
			} catch (Exception e1) {
				LOGGER.error(".export() is error:", e1);
			}
			if (LOGGER != null) {
				LOGGER.error("export()", "is error: ", e);
			}
		}
		return null;
	}
	
	/**
	 * Purpose: 列印出貨單
	 * @author barryzhang
	 * @param request 請求對象
	 * @param response 響應對象
	 * @param command formDTO
	 * @throws CommonException 上下文拋出異常
	 * @return void  
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public ModelAndView exportAsset(HttpServletRequest request, HttpServletResponse response, 
			AssetTransInfoFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try{
			if(command != null) {
				IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
				if (command.getIsHistory()!=null && command.getIsHistory()) {
					command.setShowStatusFlag(true);
				}
				SessionContext ctx = this.getServiceLocator().doService(logonUser, 
						this.getServiceId(), IAtomsConstants.ACTION_EXPORT_ASSET, command);
				command = (AssetTransInfoFormDTO) ctx.getResponseResult();
				List<DmmAssetTransListDTO> results = command.getList();
				DmmAssetTransInfoDTO assetTransInfoDTO = command.getAssetTransInfoDTO();
				
				String assetTransId = assetTransInfoDTO.getAssetTransId();
				String fromWarehouseName = assetTransInfoDTO.getFromWarehouseName();
				String toWarehouseName = assetTransInfoDTO.getToWarehouseName();
				String fromWarehouseAdd = assetTransInfoDTO.getFromWarehouseAdd();
				String toWarehouseAdd = assetTransInfoDTO.getToWarehouseAdd();
				String fromWarehouseTel = assetTransInfoDTO.getFromWarehouseTel();
				String toWarehouseTel = assetTransInfoDTO.getToWarehouseTel();
				Timestamp fromDate = assetTransInfoDTO.getFromDate();
				
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				if (!CollectionUtils.isEmpty(results)) {
					criteria.setAutoBuildJasper(false);
					//子報表的map
					Map<String, String> subjrXmlNames = new HashMap<String, String>();
					subjrXmlNames.put(AssetTransInfoFormDTO.REPORT_JRXML_GOOD_NAME_SUB, 
							AssetTransInfoFormDTO.SUBREPORT_DIR);
					List<AssetTransInfoFormDTO> resultFormDTO = new ArrayList<AssetTransInfoFormDTO>();
					resultFormDTO.add(command);
					criteria.setResult(resultFormDTO);
					//參數
					Map map = new HashMap();
					map.put("fromDate", fromDate);
					map.put("assetTransListId", assetTransId);
					map.put("fromWareHouseName", fromWarehouseName);
					map.put("toWareHouseName", toWarehouseName);
					map.put("fromWareHouseAdd", fromWarehouseAdd);
					map.put("toWareHouseAdd", toWarehouseAdd);
					map.put("fromWareHouseTel", fromWarehouseTel);
					map.put("toWareHouseTel", toWarehouseTel);
					criteria.setParameters(map);
					//設置所需報表的Name
					criteria.setJrxmlName(AssetTransInfoFormDTO.REPORT_ASSET_JRXML_NAME);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					//設置sheetName
					criteria.setSheetName(AssetTransInfoFormDTO.REPORT_GOOD_OUT_NAME);
					//設置報表Name
					criteria.setReportFileName(AssetTransInfoFormDTO.REPORT_GOOD_OUT_NAME);
					ReportExporter.exportMainAndSubReport(criteria, subjrXmlNames, response);
					// 成功標志
					SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
				}
			}
		}catch(Exception e){
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView("exportAsset"), map);
			} catch (Exception e1) {
				LOGGER.error(".exportAsset() is error:", e1);
			}
			if (LOGGER != null) {
				LOGGER.error("exportAsset()", "is error: ", e);
			}
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE);
		}
		return null;
	}
	
	/**
	 * Purpose: 匯出
	 * @author barryzhang
	 * @param request 請求對象
	 * @param response 響應對象
	 * @param command formDTO
	 * @throws CommonException 上下文拋出異常
	 * @return void  
	 */
	@SuppressWarnings("rawtypes")
	public void exportCheck(HttpServletRequest request, HttpServletResponse response,
			 AssetTransInfoFormDTO command) throws CommonException{
		try{
			if(command != null){
				IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				//請求service進行數據查詢
				SessionContext returnCtx = this.serviceLocator.doService(logonUser, 
						IAtomsConstants.SERVICE_DMM_ASSET_TRANS_INFO_SERVICE, IAtomsConstants.ACTION_EXPORT, command);
				List<DmmAssetTransListDTO> results = null;
				if(returnCtx != null){
					command = (AssetTransInfoFormDTO) returnCtx.getResponseResult();
					results = command.getList();
				}
				//封裝報表對象
				if(!CollectionUtils.isEmpty(results)){
					// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
					criteria.setAutoBuildJasper(false);
					criteria.setResult(results);
					//設置所需報表的Name
					criteria.setJrxmlName(AssetTransInfoFormDTO.REPORT_JRXML_NAME);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					criteria.setSheetName(AssetTransInfoFormDTO.REPORT_FILE_NAME);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					//設置報表Name
					criteria.setReportFileName(AssetTransInfoFormDTO.REPORT_FILE_NAME);
					ReportExporter.exportReport(criteria, response);
				}
			}
		} catch(Exception e) {
			if (LOGGER != null) {
				LOGGER.error("exportCheck()", "is error: ", e);
			}
		}
	}
}
