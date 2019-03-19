package com.cybersoft4u.xian.iatoms.web.controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cafe.core.exception.CommonException;
import cafe.core.util.StringUtils;
import cafe.core.web.controller.AbstractMultiActionController;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PropertyNumberImportFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.services.impl.PropertyImportService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

/**
 * Purpose: 財產批號匯入Controller
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016-7-7
 * @MaintenancePersonnel CarrieDuan
 */
public class PropertyImportController extends AbstractMultiActionController<PropertyNumberImportFormDTO> {
	/**
	 * 日志记录物件
	 */
	private static final Log LOGGER = LogFactory.getLog(PropertyImportController.class);
	
	/**
	 * Constructor:无参构造函数
	 */
	public PropertyImportController() {
		this.setCommandClass(PropertyNumberImportFormDTO.class);
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(PropertyNumberImportFormDTO command) throws CommonException {
		return true;
	}

	/**	
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public PropertyNumberImportFormDTO parse(HttpServletRequest request, PropertyNumberImportFormDTO command) throws CommonException {
		try {
			if (command == null) {
				command = new PropertyNumberImportFormDTO();
			}
			String actionId = command.getActionId();
			String assetListRow = null;
			List<DmmRepositoryDTO> list = null;
			if (IAtomsConstants.ACTION_CHECK_ASSET_DATA.equals(actionId)) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Map<String, MultipartFile> fileMap = multipartRequest.getFileMap(); 
				MultipartFile uploadFiled = null;
				for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {    
					// 上传文件   
					uploadFiled = entity.getValue();  
					command.setUploadFiled(uploadFiled);
				}
				String updateColumn = this.getString(request, PropertyNumberImportFormDTO.UPDATE_COLUMN_LIST);
				Gson gsonss = new GsonBuilder().create();
				Map<String, String> updateColumns = (Map<String, String>) gsonss.fromJson(updateColumn, new TypeToken<Map<String, String>>(){}.getType());
				command.setAssetMap(updateColumns);
			} else if (IAtomsConstants.ACTION_UPLOAD.equals(actionId)) {
				String assetListTaiXinRow = command.getAssetListTaiXinRow();
				String assetListJdwRow = command.getAssetListJdwRow();
				List<DmmRepositoryDTO> repositoryDTOTaiXins = null;
				List<DmmRepositoryDTO> repositoryDTOJdws = null;
				//獲取頁面的上傳列表
				assetListRow = command.getAssetListRow();
				String updateColumn = this.getString(request, PropertyNumberImportFormDTO.UPDATE_COLUMN_LIST);
				//将jason字符串转化为list集合
				Type type = new TypeToken<ArrayList<DmmRepositoryDTO>>() {}.getType();//获取对象类型
				GsonBuilder builder = new GsonBuilder();
				builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
					@Override
					public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
						return new Date(json.getAsJsonPrimitive().getAsLong());
					}
				});
				Gson gsons = builder.create();
				list= gsons.fromJson(assetListRow, type);
				if (StringUtils.hasText(assetListTaiXinRow)) {
					repositoryDTOTaiXins = gsons.fromJson(assetListTaiXinRow, type);
				}
				if (StringUtils.hasText(assetListJdwRow)) {
					repositoryDTOJdws = gsons.fromJson(assetListJdwRow, type);
				}
				Map<String, String> updateColumns = (Map<String, String>) gsons.fromJson(updateColumn, new TypeToken<Map<String, String>>(){}.getType());
				command.setRepositoryDTOs(list);
				command.setRepositoryDTOJdws(repositoryDTOJdws);
				command.setRepositoryDTOTaiXins(repositoryDTOTaiXins);
				command.setAssetMap(updateColumns);
			}
			return command;
		} catch (Exception e) {
			LOGGER.error(".parse() Exception.", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
	}
	
	/**
	 * 
	 * Purpose:下載要上傳文件的範本
	 * @author allenchen
	 * @param request:頁面請求
	 * @param response:回應
	 * @param formDTO:PropertyNumberImportFormDTO
	 * @throws CommonException:公共Exception
	 * @return void
	 */
	public void download (HttpServletRequest request, HttpServletResponse response, PropertyNumberImportFormDTO formDTO) throws CommonException {
		try {
			//下載匯入格式文本
			String tempPath = IAtomsConstants.TEMPLATE_DOWNLOAD_PATH;
			String fileNameCN = PropertyNumberImportFormDTO.UPLOAD_CN_FILE_NAME;
			String fileNameEN = PropertyNumberImportFormDTO.UPLOAD_EN_FILE_NAME;
			String tempFailString = tempPath.concat(fileNameEN);
			InputStream inputStream = PropertyImportService.class.getResourceAsStream(tempFailString);
			FileUtils.download(request, response, inputStream, fileNameCN);
		} catch (Exception e) {
			LOGGER.error(".download() Exception", e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}

	public void downloadErrorFile(HttpServletRequest request, HttpServletResponse response, PropertyNumberImportFormDTO command) throws CommonException{
		try {
			//下載错误信息文件
			String fileName = this.getString(request, PropertyNumberImportFormDTO.ERROR_FILE_NAME);
			String tempPath = this.getString(request, PropertyNumberImportFormDTO.ERROR_FILE_PATH);
			String inputfile = tempPath.concat(IAtomsConstants.MARK_BACKSLASH).concat(fileName);
			FileUtils.download(request, response, inputfile, IAtomsConstants.UPLOAD_ERROR_MESSAGE_FILE_NAME);
			FileUtils.removeFile(inputfile);
		} catch (Exception e) {
			LOGGER.error(".downloadErrorFile() Exception:", e);
		}
		
	}
	
	
}
