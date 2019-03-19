package com.cybersoft4u.xian.iatoms.web.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cafe.core.bean.Message;
import cafe.core.exception.CommonException;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetInInfoFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.cybersoft4u.xian.iatoms.services.impl.AssetInService;
import com.cybersoft4u.xian.iatoms.services.impl.PropertyImportService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
/**
 * Purpose: 設備入庫Controller
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel CarrieDuan
 */
public class AssetInController extends AbstractMultiActionController<AssetInInfoFormDTO> {
	
	/**
	 * 系统日志纪记录物件
	 */
	public static final CafeLog LOGGER = CafeLogFactory.getLog(AssetInController.class);
	/**
	 * 
	 * Constructor:无参构造
	 */
	public AssetInController () {
		this.setCommandClass(AssetInInfoFormDTO.class);
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	public boolean validate(AssetInInfoFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			AssetInInfoDTO assetInInfoDTO = command.getAssetInInfoDTO();
			//硬體廠商
			String companyId = assetInInfoDTO.getCompanyId();
			//維護模式
			String maType = assetInInfoDTO.getMaType();
			//合約ID
			String contractId = assetInInfoDTO.getContractId();
			//設備ID
			String assetTypeId = assetInInfoDTO.getAssetTypeId();
			//倉庫名稱
			String warehouseId = assetInInfoDTO.getWarehouseId();
			//資產owner
			String owner = assetInInfoDTO.getOwner();
			//使用人
			String userId = assetInInfoDTO.getUserId();
			//保管人
			String keeperName = assetInInfoDTO.getKeeperName();
			//cyber驗收日期
			Date cyberApprovedDate = assetInInfoDTO.getCyberApprovedDate();
			//說明
			String comment = assetInInfoDTO.getComment();
			//核檢硬體廠商是否為空
			if (!StringUtils.hasText(companyId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_COMPANY_ID)});
				throw new CommonException(msg);
			}
			//核檢維護模式是否為空
			if (!StringUtils.hasText(maType)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_MA_TYPE)});
				throw new CommonException(msg);
			}
			//核檢合約ID是否為空
			if (!StringUtils.hasText(contractId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_CONTRACT_ID)});
				throw new CommonException(msg);
			}
			//核檢設備ID是否為空
			if (!StringUtils.hasText(assetTypeId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_ASSET_TYPE_ID)});
				throw new CommonException(msg);
			}
			//核檢倉庫名稱是否為空
			if (!StringUtils.hasText(warehouseId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_WAREHOUSE_ID)});
				throw new CommonException(msg);
			}
			//核檢倉庫名稱是否為空
			if (!StringUtils.hasText(warehouseId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_WAREHOUSE_ID)});
				throw new CommonException(msg);
			}
			//核檢資產owner是否為空
			if (!StringUtils.hasText(owner)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_OWNER)});
				throw new CommonException(msg);
			}
			//核檢使用人是否為空
			if (!StringUtils.hasText(userId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_USER_ID)});
				throw new CommonException(msg);
			}
			//核檢cyber驗收日期是否為空
			if (cyberApprovedDate == null) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_CYBER_APPROVED_DATE)});
				throw new CommonException(msg);
			}
			//核檢保管人長度是否超出
			if (StringUtils.hasText(keeperName)) {
				if (!ValidateUtils.length(keeperName, 1, 50)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_KEEPER_NAME), IAtomsConstants.CONTACT_USER_LENGTH});
					throw new CommonException(msg);
				}
			}
			//核檢說明長度是否超出
			if (StringUtils.hasText(comment)) {
					if (!ValidateUtils.length(comment, 1, 200)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_COMMENT), IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
						throw new CommonException(msg);
					}
			}
		}
		if (IAtomsConstants.ACTION_ADD_ASSET_IN_LIST.equals(actionId)){
			AssetInInfoDTO assetInInfoDTO = command.getAssetInInfoDTO();
			//獲取設備序號
			String serialNumber = assetInInfoDTO.getSerialNumber();
			//獲取財產編號
			String property = assetInInfoDTO.getPropertyId();
			//核檢設備序號是否輸入
			if(!StringUtils.hasText(serialNumber)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_SERIAL_NUMBER)});
				throw new CommonException(msg);
			} else {
				//核檢設備序號長度是否超出
				if (!ValidateUtils.varcharLength(serialNumber, 1, 20)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_SERIAL_NUMBER)});
					throw new CommonException(msg);
				}
				//核檢設備序號是否只是英數字
				if (!ValidateUtils.numberOrEnglish(serialNumber)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_SERIAL_NUMBER)});
					throw new CommonException(msg);
				}
			}
			if (StringUtils.hasText(property)) {
				//核檢財產編號長度是否超出
				if (!ValidateUtils.varcharLength(property, 0, 20)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_PROPERTY_ID)});
					throw new CommonException(msg);
				}
				//核檢財產編號是否只是英數字
				if (!ValidateUtils.numberOrEnglish(property)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_PROPERTY_ID)});
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
	public AssetInInfoFormDTO parse(HttpServletRequest request, AssetInInfoFormDTO command) throws CommonException {
		if (command == null) {
			command = new AssetInInfoFormDTO();
		} 
		try {
			//actionId
			String actionId = command.getActionId();
			//綁值
			AssetInInfoDTO assetInInfoDTO = BindPageDataUtils.bindValueObject(request, AssetInInfoDTO.class);
			command.setAssetInInfoDTO(assetInInfoDTO);
			if (IAtomsConstants.ACTION_LIST_ASSET_IN_LIST.equals(actionId)) {//查詢待入庫清單
				//設置查詢條件--是否入庫為false
				command.setDone(false);
				if (!StringUtils.hasText(command.getSort())) {
					command.setSort(command.PARAM_SORT);
				}
				if (!StringUtils.hasText(command.getOrder())) {
					command.setOrder(command.PARAM_ORDER);
				}
			} else if (IAtomsConstants.ACTION_UPLOAD.equals(actionId)) {//匯入
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Map<String, MultipartFile> fileMap = multipartRequest.getFileMap(); 
				MultipartFile uploadFiled = null;
				for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {    
					// 上传文件   
					uploadFiled = entity.getValue();  
					command.setUploadFiled(uploadFiled);
				}	        
			} else if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
				if (!StringUtils.hasText(command.getSort())) {
					command.setSort(command.PARAM_SORT);
				}
				if (!StringUtils.hasText(command.getOrder())) {
					command.setOrder(command.PARAM_ORDER);
				}
			} else if (IAtomsConstants.ACTION_DELETE_ASSET_IN_LIST.equals(actionId) || IAtomsConstants.ACTION_ACTUAL_ACCEPTANCE.equals(actionId)
					|| IAtomsConstants.ACTION_CUSTOMES_ACCEPTANCE.equals(actionId)) {
				Gson gsonss = new GsonBuilder().create();
				if (StringUtils.hasText(command.getDeleteAssetListIds())) {
					List<String> ids = (List<String>) gsonss.fromJson(command.getDeleteAssetListIds(), new TypeToken<List<String>>(){}.getType());
					command.setAssetListIds(ids);
				}
			}
		} catch (Exception e) {
			LOGGER.error("parse()", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
		
	}
	/**
	 * 
	 * Purpose: 匯入格式下載
	 * @author hungli
	 * @param request
	 * @param response
	 * @return void
	 */
	public void download(HttpServletRequest request, HttpServletResponse response, AssetInInfoFormDTO command)throws CommonException{
		try {
			String fileName = this.getString(request, AssetInInfoFormDTO.ERROR_FILE_NAME);
			if (StringUtils.hasText(fileName)) {
				String filePath = this.getString(request, AssetInInfoFormDTO.ERROR_FILE_PATH);
				String inputfile = filePath.concat(IAtomsConstants.MARK_BACKSLASH).concat(fileName);
				FileUtils.download(request, response, inputfile, IAtomsConstants.UPLOAD_ERROR_MESSAGE_FILE_NAME);
				FileUtils.removeFile(inputfile);
			} else {
				String fileNameCH = AssetInInfoFormDTO.UPLOAD_FILE_NAME_CH;
				String fileNameEN = AssetInInfoFormDTO.UPLOAD_FILE_NAME_EN;
				String tempPath = IAtomsConstants.TEMPLATE_DOWNLOAD_PATH;
				String tempFailString = tempPath.concat(fileNameEN);
				InputStream inputStream = AssetInService.class.getResourceAsStream(tempFailString);
				FileUtils.download(request, response, inputStream, fileNameCH);
			}
			
		} catch (Exception e) {
			LOGGER.error("Exception----save()", e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		
	}
	
	/**
	 * Purpose: 文件下載
	 * @author CarrieDuan
	 * @param request
	 * @param response
	 * @param fileName ：文件名稱
	 * @param tempPath ：路徑
	 * @return void
	 */
	private void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName, String tempPath) {
		OutputStream outputStream = null;
		try {
			String fileNameCH = AssetInInfoFormDTO.UPLOAD_FILE_NAME_CH;
			File file = new File(tempPath, fileName);
			String tempFailString = file.toString();
			InputStream inputStream = PropertyImportService.class.getResourceAsStream(tempFailString);
			if (inputStream != null) {
				if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {  
					fileNameCH = URLEncoder.encode(fileNameCH, "UTF-8");  
				} else {  
					fileNameCH = new String(fileNameCH.getBytes("UTF-8"), "ISO8859-1");  
				}
				outputStream = response.getOutputStream();// 获取文件输出IO流    
				response.setContentType("application/x-download");// 设置response内容的类型
				response.setHeader("Content-disposition", "attachment;filename = ".concat(fileNameCH));// 设置头部信息
				response.setContentType("application/vnd.ms-excel;charset=UTF-8");
				byte[] buffer = new byte[1024];
				//开始向网络传输文件流    
				/*int len = -1;
				while ((len = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, len);
				}*/
				int len = inputStream.read(buffer);
				while (len != -1) {
					outputStream.write(buffer, 0, len);
					len = inputStream.read(buffer);
				}
				outputStream.flush();
			} else{
				LOGGER.error("FileUtils downFile 文件不存在!!!!");
			}
		} catch (Exception e) {
			LOGGER.error("download()---->Exception", e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					LOGGER.error("download()---->Exception", e);
				}
			}
		}
		
		
	}
}
