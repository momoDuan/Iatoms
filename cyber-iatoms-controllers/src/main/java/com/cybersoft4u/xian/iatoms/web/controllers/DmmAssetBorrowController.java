package com.cybersoft4u.xian.iatoms.web.controllers;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import cafe.core.exception.CommonException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowNumberDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.DmmAssetBorrowFormDTO;
import com.cybersoft4u.xian.iatoms.web.controllers.authenticator.AuthenticatorHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
/**
 * Purpose: 設備借用管理controller
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年7月31日
 * @MaintenancePersonnel CarrieDuan
 */
public class DmmAssetBorrowController extends AbstractMultiActionController<DmmAssetBorrowFormDTO> {
	/**
	 * 日志记录物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(DmmAssetBorrowController.class);
	/**
	 * 识别管理服务类别Service
	 */
	private AuthenticatorHelper authenticatorHelper;
	
	/**
	 * Constructor:无参构造函数
	 */
	public DmmAssetBorrowController() {
		this.setCommandClass(DmmAssetBorrowFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(DmmAssetBorrowFormDTO parmemters) throws CommonException {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public DmmAssetBorrowFormDTO parse(HttpServletRequest request, DmmAssetBorrowFormDTO command) throws CommonException {
		try {
			String actionId = command.getActionId();
			if (IAtomsConstants.ACTION_QUERY.equals(actionId) || "queryProcess".equals(actionId)) {
				//查詢條件	
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (StringUtils.hasText(sort)) {
				//	sort = AdmUserDTO.getATTRIBUTENameByValue(sort);
					command.setSort(sort);
				} else {
					command.setSort("");
				}
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);			
				if (StringUtils.hasText(order)) {
					command.setOrder(order);
				} else {
					command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
				}
			} else if (IAtomsConstants.ACTION_SAVE.equals(actionId) || "checkSerialNumber".equals(actionId)) {
				// 綁定需要保存的DTO
				DmmAssetBorrowInfoDTO assetBorrowInfoDTO = BindPageDataUtils.bindValueObject(request, DmmAssetBorrowInfoDTO.class);
				if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
					// 獲取合約中新增的設備列表JSON數據
					String assetBorrowNumberRow = command.getAssetBorrowNumber();
					Gson gson = new GsonBuilder().create();
					// 合約中的設備列表轉為LIST
					List<DmmAssetBorrowNumberDTO> assetBorrowNumberDTOs = gson.fromJson(assetBorrowNumberRow, new TypeToken<List<DmmAssetBorrowNumberDTO>>(){}.getType());
					assetBorrowInfoDTO.setAssetBorrowNumberDTOs(assetBorrowNumberDTOs);
				}
				command.setAssetBorrowInfoDTO(assetBorrowInfoDTO);
			} else if ("saveProcess".equals(actionId)) {
				// 綁定需要保存的DTO
				//DmmAssetBorrowInfoDTO assetBorrowInfoDTO = BindPageDataUtils.bindValueObject(request, DmmAssetBorrowInfoDTO.class);
				String isDirectorCheck = command.getIsDirectorCheck();
				if (IAtomsConstants.NO.equals(isDirectorCheck)) {
					String saveAssetBorrowInfoDTO = command.getSaveAssetBorrowInfoDTO();
					GsonBuilder builder = new GsonBuilder();
					builder.registerTypeAdapter(Timestamp.class, new JsonDeserializer<Timestamp>() {
						@Override
						public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
							return new Timestamp(json.getAsJsonPrimitive().getAsLong());
						}
					});
					Gson gsons = builder.create();
					// 合約中的設備列表轉為LIST
					List<DmmAssetBorrowInfoDTO> assetBorrowInfoDTOs = gsons.fromJson(saveAssetBorrowInfoDTO, new TypeToken<List<DmmAssetBorrowInfoDTO>>(){}.getType());
					command.setAssetBorrowInfoDTOs(assetBorrowInfoDTOs);
				}
			}/* else if ("saveBorrow".equals(actionId)) {
				// 獲取合約中新增的設備列表JSON數據
				String assetBorrowNumberRow = command.getAssetBorrowNumber();
				Gson gson = new GsonBuilder().create();
				// 合約中的設備列表轉為LIST
				List<DmmAssetBorrowInfoDTO> assetBorrowInfoDTOs = gson.fromJson(assetBorrowNumberRow, new TypeToken<List<DmmAssetBorrowInfoDTO>>(){}.getType());
				command.setAssetBorrowInfoDTOs(assetBorrowInfoDTOs);
			}*/
		} catch (Exception e) {
			LOGGER.error("parse()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}

	/**
	 * @return the authenticatorHelper
	 */
	public AuthenticatorHelper getAuthenticatorHelper() {
		return authenticatorHelper;
	}

	/**
	 * @param authenticatorHelper the authenticatorHelper to set
	 */
	public void setAuthenticatorHelper(AuthenticatorHelper authenticatorHelper) {
		this.authenticatorHelper = authenticatorHelper;
	}
	
}
