package com.cybersoft4u.xian.iatoms.web.controllers;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import cafe.core.bean.Message;
import cafe.core.exception.CommonException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTypeFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;

/**
 * Purpose: 設備品項維護Controller
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年7月8日
 * @MaintenancePersonnel HermanWang
 */
public class AssetTypeController extends AbstractMultiActionController<AssetTypeFormDTO> {

	/**
	 * 序列號
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AssetTypeController.class);
	
	/**
	 * 
	 * Constructor: 無參構造
	 */
	public AssetTypeController(){
		this.setCommandClass(AssetTypeFormDTO.class);
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	public boolean validate(AssetTypeFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			AssetTypeDTO assetTypeDTO = command.getAssetTypeDTO();
			//String assetTypeId = assetTypeDTO.getAssetTypeId();
			String name = assetTypeDTO.getName();
			String brand = assetTypeDTO.getBrand();
			String model = assetTypeDTO.getModel();
			String assetCategory = assetTypeDTO.getAssetCategory();
			String unit = assetTypeDTO.getUnit();
			BigDecimal safetyStock = assetTypeDTO.getSafetyStock();
			String remark = assetTypeDTO.getRemark();
			/*//若未輸入設備代碼
			if (!StringUtils.hasText(assetTypeId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_ASSET_TYPE_ID)});
				throw new CommonException(msg);
			} else {
				if (!ValidateUtils.length(assetTypeId, 0, 10)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_ASSET_TYPE_ID)});
					throw new CommonException(msg);
				}
			}*/
			//若未輸入設備名稱
			if (!StringUtils.hasText(name)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_NAME)});
				throw new CommonException(msg);
			} else {
				if (!ValidateUtils.length(name, 0, 50)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_NAME), 
							IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			//對設備廠牌的長度進行判斷
			if(StringUtils.hasText(brand)){
				if (!ValidateUtils.length(brand, 0, 500)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_BRAND), 
							IAtomsConstants.MAXLENGTH_NUMBER_FIVE_HUNDRED});
					throw new CommonException(msg);
				}
			}
			//對設備型號的長度進行判斷
			if(StringUtils.hasText(model)){
				if (!ValidateUtils.length(model, 0, 500)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_MODEL), 
							IAtomsConstants.MAXLENGTH_NUMBER_FIVE_HUNDRED});
					throw new CommonException(msg);
				}
			}
			//判斷設備類別是否選擇(必選)
			if (!StringUtils.hasText(assetCategory)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_ASSET_CATEGORY)});
				throw new CommonException(msg);
			} else {
				//如果選擇edc那麼通訊模式必選
				if(assetCategory.equals(IAtomsConstants.PROJECT_STATUS_OPEN)){
					int commModeName = assetTypeDTO.getCommModeIds().size();
					if (commModeName == 0) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BASE_PARAMETER_ITEM_DEF_ASSET_CATEGORY)});
						throw new CommonException(msg);
					} 
				}
			}
			//對單位輸入長度進行判斷
			if(StringUtils.hasText(unit)){
				if (!ValidateUtils.length(unit, 0, 10)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_UNIT), 
							IAtomsConstants.MAXLENGTH_NUMBER_TEN});
					throw new CommonException(msg);
				}
			}
			//對安全庫存長度進行判斷
			if(safetyStock != null){
				String safetyStockString = safetyStock.toString();
				if (!ValidateUtils.length(safetyStockString, 0, 10)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_SAFETY_STOCK), 
							IAtomsConstants.MAXLENGTH_NUMBER_TEN});
					throw new CommonException(msg);
				} else {
					//驗證格式 正整數
					if(!ValidateUtils.number(safetyStockString)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_POSITIVE_NUMBER, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_SAFETY_STOCK)});
						throw new CommonException(msg);
					}
				}
			}
			//對說明的長度進行判定
			if(StringUtils.hasText(remark)){
				if (!ValidateUtils.length(remark, 0, 200)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_REMARK), 
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
	public AssetTypeFormDTO parse(HttpServletRequest request, AssetTypeFormDTO command) throws CommonException {
		try{
			String actionId = this.getString(request, IAtomsConstants.PARAM_ACTION_ID);
			if(!StringUtils.hasText(actionId)) {
				actionId = IAtomsConstants.ACTION_INIT;
			}
			LOGGER.debug(".parse() --> actionId: ", actionId);
			command.setActionId(actionId);
			if(IAtomsConstants.ACTION_QUERY.equals(actionId)) {
				//查詢條件 -- 設備類別
				String queryAssetCategoryCode = this.getString(request, AssetTypeFormDTO.QUERY_PAGE_PARAM_ASSET_CATEGORY_CODE);
				LOGGER.debug(".parse() --> queryAssetCategoryCode: ", queryAssetCategoryCode);		
				//查詢條件 -- 排序欄位 
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				LOGGER.debug(".parse() --> sort: ", sort);
				//查詢條件 -- 排序方式
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				LOGGER.debug(".parse() --> order: ", order);
				//查詢條件 -- 當前頁碼
				int currentPage = this.getInt(request, IAtomsConstants.QUERY_PAGE_INDEX);
				LOGGER.debug(".parse() --> currentPage: ", String.valueOf(currentPage));
				// 查詢條件--每頁總筆數
				int pageSize = this.getInt(request, IAtomsConstants.QUERY_PAGE_ROWS);
				LOGGER.debug(".parse() --> pageSize: ", String.valueOf(pageSize));
				command.setQueryAssetCategoryCode(queryAssetCategoryCode);
				command.setOrder(order);
				command.setSort(sort);
				command.getPageNavigation().setCurrentPage(currentPage - 1);
				command.getPageNavigation().setPageSize(pageSize);
			}else if(IAtomsConstants.ACTION_INIT_EDIT.equals(actionId) || IAtomsConstants.ACTION_DELETE.equals(actionId)) {
				//獲取編輯數據條件
				String editAssetTypeId = this.getString(request, AssetTypeFormDTO.EDIT_PAGE_PARAM_ASSET_TYPE_ID);
				LOGGER.debug(".parse() --> editAssetTypeId: ", editAssetTypeId);
				command.setEditAssetTypeId(editAssetTypeId);
			}else if(IAtomsConstants.ACTION_SAVE.equals(actionId)) {
				//獲取複選框的傳入值
				String[] companyIds = request.getParameterValues(AssetTypeFormDTO.EDIT_PAGE_PARAM_ARRAY_COMPANY_ID);
				String[] commModeIds = request.getParameterValues(AssetTypeFormDTO.EDIT_PAGE_PARAM_ARRAY_COMM_MODE_ID);
				String[] functionIds = request.getParameterValues(AssetTypeFormDTO.EDIT_PAGE_PARAM_ARRAY_FUNCTION_ID);
				String safetyStockStr = this.getString(request, "safetyStockStr");
				BigDecimal safetyStock = null;
				if(StringUtils.hasText(safetyStockStr)) {
					safetyStock = new BigDecimal(safetyStockStr);
				}
				AssetTypeDTO assetTypeDTO = BindPageDataUtils.bindValueObject(request, AssetTypeDTO.class);
				assetTypeDTO.setSafetyStock(safetyStock);
				//將頁面傳入的多選框的值轉爲集合存儲（設備廠商）
				Set<String> set = null;
				if(companyIds != null){
					set = new HashSet<String>();
					for(String str : companyIds){
						if(StringUtils.hasText(str)){
							set.add(str);
						}
					}
					assetTypeDTO.setCompanyIds(set);
				}
				//將頁面傳入的多選框的值轉爲集合存儲(通訊模式)
				if(commModeIds != null){
					set = new HashSet<String>();
					for(String str : commModeIds){
						if(StringUtils.hasText(str)){
							set.add(str);
						}
					}
					assetTypeDTO.setCommModeIds(set);
				}
				//將頁面傳入的多選框的值轉爲集合存儲（支援功能）
				if(functionIds != null){
					set = new HashSet<String>();
					for(String str : functionIds){
						if(StringUtils.hasText(str)){
							set.add(str);
						}
					}
					assetTypeDTO.setFunctionIds(set);
				}
				//欲編輯數據編號
				String editAssetTypeId = this.getString(request, AssetTypeFormDTO.EDIT_PAGE_PARAM_ASSET_TYPE_ID);
				LOGGER.error(".parse() -- > editAssetTypeId : ", editAssetTypeId);
				command.setEditAssetTypeId(editAssetTypeId);
				command.setAssetTypeDTO(assetTypeDTO);
			}
		}catch(Exception e){
			LOGGER.error(this.getClass().getName(), ".parse() is error:", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}
	
}
