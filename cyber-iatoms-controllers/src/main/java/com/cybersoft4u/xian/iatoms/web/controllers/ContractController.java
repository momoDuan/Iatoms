package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cafe.core.bean.Message;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.i18NUtil;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * Purpose:合约维护Controller 
 * @author CarrieDuan	
 * @since  JDK 1.7
 * @date   2016/5/17
 * @MaintenancePersonnel CarrieDuan
 */
public class ContractController extends AbstractMultiActionController<ContractManageFormDTO>{
	
	
	/**
	 * 系统日志记录物件
	 */
	private static final Log LOG = LogFactory.getLog(ContractController.class);
	
	/**
	 * 无参构造子
	 */
	public ContractController() {
		this.setCommandClass(ContractManageFormDTO.class);
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	public boolean validate(ContractManageFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			Message msg = null;
			BimContractDTO contractManageDTO= command.getContractManageDTO();
			String contractId = contractManageDTO.getHiddenContractId();//合約ID
			String companyId = contractManageDTO.getCompanyId();//客戶
			String contractCode = contractManageDTO.getContractCode();//合約編號
			String contractTypeId = contractManageDTO.getContractTypeId();//合約類型
			Date startDate = contractManageDTO.getStartDate();//合約起
			Date endDate = contractManageDTO.getEndDate();//合約止
			Long contractPrice = contractManageDTO.getContractPrice();//合約金額
			String payRequire = contractManageDTO.getPayRequire();//付款條件
			Integer factoryWarranty = contractManageDTO.getFactoryWarranty();//原廠保固期限
			Integer customerWarranty = contractManageDTO.getCustomerWarranty();//客戶保固期限
			String workHourStart1 = contractManageDTO.getWorkHourStart1();//約定上班時間1
			String workHourEnd1 = contractManageDTO.getWorkHourEnd1();//約定上班時間1
			String workHourStart2 = contractManageDTO.getWorkHourStart2();//約定上班時間2
			String workHourEnd2 = contractManageDTO.getWorkHourEnd2();//約定上班時間2
			String window1 = contractManageDTO.getWindow1();//窗口1
			String window1Connection = contractManageDTO.getWindow1Connection();//窗口1聯繫方式
			String window2Connection = contractManageDTO.getWindow2Connection();//窗口2聯繫方式
			String window2 = contractManageDTO.getWindow2();//窗口2
			String comment = contractManageDTO.getComment();//說明
			List<BimContractAssetDTO> assetDTOs = contractManageDTO.getAssetTypeDTOs();//合約設備列表
			Long amount = (long) 0;
			Long safetyStock = (long) 0;
			if (!StringUtils.hasText(contractId)) {
				//檢核客戶是否為空
				if (!StringUtils.hasText(companyId)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_COMPANY_ID)});
					throw new CommonException(msg);
				}
			}
			//核檢合約編號是否為空
			if (!StringUtils.hasText(contractCode)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_CONTRACT_CODE)});
				throw new CommonException(msg);
			}
			//判斷合約類型
			if (StringUtils.hasText(contractTypeId)) {
				String[] typeIds = contractTypeId.split(IAtomsConstants.MARK_SEPARATOR);
				for (String typeId : typeIds) {
					if (IAtomsConstants.CONTRACT_TYPE_LEASE.equals(typeId) || IAtomsConstants.CONTRACT_TYPE_BUY.equals(typeId)) {
						//核檢原廠保固期限是否為空
						if (factoryWarranty == null) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_FACTORY_WARRANTY)});
							throw new CommonException(msg);
						}
						//核檢原廠保固期限是否為空
						if (customerWarranty == null) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_CUSTOMER_WARRANTY)});
							throw new CommonException(msg);
						}
						//核檢是否選擇合約設備
						if(CollectionUtils.isEmpty(assetDTOs)) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.CHOOSE_CONTRACT_ASSET);
							throw new CommonException(msg);
						}
					}
				}
				
			}
			//核檢合約編號長度
			if (StringUtils.hasText(contractCode)) {
				if (!ValidateUtils.length(contractCode, 0, 50)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_CONTRACT_CODE), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			//核檢合約金額
			if (contractPrice != null) {
				if (!ValidateUtils.number(contractPrice.toString())) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_POSITIVE_NUMBER, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_CONTRACT_PRICE)});
					throw new CommonException(msg);
				}
			}
			//核檢付款條件長度
			if (StringUtils.hasText(payRequire)) {
				if (!ValidateUtils.length(payRequire, 0, 100)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_PAY_REQUIRE), IAtomsConstants.CONTACT_ADDRESS_LENGTH});
					throw new CommonException(msg);
				}
			}
			//核檢合約止是否大於合約起
			if (startDate != null && endDate != null) {
				if (startDate.getTime() > endDate.getTime()) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_MORE_THEN, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_START_DATE), i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_END_DATE)});
					throw new CommonException(msg);
				}
			}
			//核檢原廠保固期限是否為正整數
			if (factoryWarranty != null) {
				if (!ValidateUtils.number(factoryWarranty.toString())) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_POSITIVE_NUMBER, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_FACTORY_WARRANTY)});
					throw new CommonException(msg);
				}
			}
			//核檢客戶保固期限是否為正整數
			if (customerWarranty != null) {
				if (!ValidateUtils.number(customerWarranty.toString())) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_POSITIVE_NUMBER, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_CUSTOMER_WARRANTY)});
					throw new CommonException(msg);
				}
			}
			//核檢約定上班時間1的格式
			if (StringUtils.hasText(workHourStart1)) {
				if (!ValidateUtils.valiDataTime(workHourStart1)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_TIME_FORMAT_HH_MM, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WORK_HOUR_START_1)});
					throw new CommonException(msg);
				}
			}
			//核檢約定上班時間1的格式
			if (StringUtils.hasText(workHourEnd1)) {
				if (!ValidateUtils.valiDataTime(workHourEnd1)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_TIME_FORMAT_HH_MM, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WORK_HOUR_END_1)});
					throw new CommonException(msg);
				}
			}
			//核檢約定上班時間2的格式
			if (StringUtils.hasText(workHourEnd2)) {
				if (!ValidateUtils.valiDataTime(workHourEnd2)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_TIME_FORMAT_HH_MM, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WORK_HOUR_END_2)});
					throw new CommonException(msg);
				}
			}
			//核檢約定上班時間2的格式
			if (StringUtils.hasText(workHourStart2)) {
				if (!ValidateUtils.valiDataTime(workHourStart2)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_TIME_FORMAT_HH_MM, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WORK_HOUR_START_2)});
					throw new CommonException(msg);
				}
			}
			//核檢上班時間1起是否大於上班時間1止
			if (StringUtils.hasText(workHourStart1) && StringUtils.hasText(workHourEnd1)) {
				if (!ValidateUtils.compareTimeSize(workHourStart1, workHourEnd1)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_MORE_THEN, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WORK_HOUR_START_1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WORK_HOUR_END_1)});
					throw new CommonException(msg);
				}
			}
			//核檢上班時間2起是否大於上班時間2止
			if (StringUtils.hasText(workHourStart2) && StringUtils.hasText(workHourEnd2)) {
				if (!ValidateUtils.compareTimeSize(workHourStart2, workHourEnd2)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_MORE_THEN, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WORK_HOUR_START_2), i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WORK_HOUR_END_2)});
					throw new CommonException(msg);
				}
			}
			//核檢上班時間1是否大於上班時間2
			if (StringUtils.hasText(workHourStart2) && StringUtils.hasText(workHourEnd1)) {
				if (!ValidateUtils.compareTimeSize(workHourEnd1, workHourStart2)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_MORE_THEN, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WORK_HOUR_END_1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WORK_HOUR_START_2)});
					throw new CommonException(msg);
				}
			}
			//驗證窗口1長度
			if (StringUtils.hasText(window1)) {
				if (!ValidateUtils.length(window1, 0, 50)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WINDOW1), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			//驗證窗口1聯繫方式
			if (StringUtils.hasText(window1Connection)) {
				//驗證長度
				if (!ValidateUtils.length(window1Connection, 0, 20)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WINDOW1_CONNECTION), IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
					throw new CommonException(msg);
				}
			}
			//驗證窗口2長度
			if (StringUtils.hasText(window2)) {
				if (!ValidateUtils.length(window2, 0, 50)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WINDOW2), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			//驗證窗口2聯繫方式
			if (StringUtils.hasText(window2Connection)) {
				//驗證長度
				if (!ValidateUtils.length(window2Connection, 0, 20)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_WINDOW2_CONNECTION), IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
					throw new CommonException(msg);
				}
			}
			//驗證說明長度
			if (StringUtils.hasText(comment)) {
				if (!ValidateUtils.length(comment, 0, 200)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_COMMENT), IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
					throw new CommonException(msg);
				}
			}
			//核檢合約設備
			if (!CollectionUtils.isEmpty(assetDTOs)) {
				for (BimContractAssetDTO contractAssetDTO : assetDTOs) {
					amount = contractAssetDTO.getAmount() == null ? 0 : contractAssetDTO.getAmount();
					safetyStock = contractAssetDTO.getSafetyStock() == null ? 0 : contractAssetDTO.getSafetyStock();
					if (!StringUtils.hasText(contractAssetDTO.getAssetTypeId())) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_ASSET_ASSET_TYPE_ID)});
						throw new CommonException(msg);
					}
					if (amount != 0) {
						if (!ValidateUtils.number(amount.toString())) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_POSITIVE_NUMBER, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_ASSET_AMOUNT)});
							throw new CommonException(msg);
						}
						if (!ValidateUtils.varcharLength(amount.toString(), 0, 10)) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_ASSET_AMOUNT), IAtomsConstants.MAXLENGTH_NUMBER_TEN});
							throw new CommonException(msg);
						}
					}
					if (safetyStock != 0) {
						if (!ValidateUtils.number(safetyStock.toString())) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_POSITIVE_NUMBER, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_ASSET_SAFETY_STOCK)});
							throw new CommonException(msg);
						}
						if (!ValidateUtils.varcharLength(safetyStock.toString(), 0, 10)) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_ASSET_SAFETY_STOCK), IAtomsConstants.MAXLENGTH_NUMBER_TEN});
							throw new CommonException(msg);
						}
					}
					
					if (amount < safetyStock) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_NOT_MORE_THEN, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_ASSET_SAFETY_STOCK), i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_ASSET_AMOUNT)});
						throw new CommonException(msg);
					}
				}
			}
		}
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	public ContractManageFormDTO parse(HttpServletRequest request,
			ContractManageFormDTO command) throws CommonException {
		try {
			String actionId = command.getActionId(); 
			if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
				// 綁定需要保存的DTO
				BimContractDTO contractManageDTO = BindPageDataUtils.bindValueObject(request, BimContractDTO.class);
				// 修改時從hiddenContractId獲得合同ID並設置到ContractId中
				String hiddenContractId = this.getString(request, ContractManageFormDTO.PARAM_HIDDEN_CONTRACT_ID);
				if (StringUtils.hasText(hiddenContractId) && !StringUtils.hasText(contractManageDTO.getContractId())){
					contractManageDTO.setContractId(hiddenContractId);
				}
				// 獲取合約中新增的設備列表JSON數據
				String assetListRow = command.getAssetListRow();
				Gson gson = new GsonBuilder().create();
				// 合約中的設備列表轉為LIST
				List<BimContractAssetDTO> assetTypeDTOs = gson.fromJson(assetListRow, new TypeToken<List<BimContractAssetDTO>>(){}.getType());
				contractManageDTO.setAssetTypeDTOs(assetTypeDTOs);
				command.setContractManageDTO(contractManageDTO);
			}
			if (IAtomsConstants.ACTION_SAVE_FILE.equals(actionId)) {
				// 有文件上傳時，儲存文件
				if(request instanceof MultipartHttpServletRequest){	
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
					command.setFileMap(fileMap);
				}
			}
			if (IAtomsConstants.ACTION_DELETE.equals(actionId) || IAtomsConstants.ACTION_INIT_EDIT.equals(actionId)) {
				String contractId = command.getContractId();
				if (StringUtils.hasText(contractId) ) {
					contractId = new String(contractId.getBytes(ContractManageFormDTO.CODE_ISO), (ContractManageFormDTO.CODE_UTF));
					command.setContractId(contractId);
				}
			}
			if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (!StringUtils.hasText(sort)) {
					sort = BimContractDTO.ATTRIBUTE.CUSTOMER_NAME.getValue().concat( IAtomsConstants.MARK_SEPARATOR.concat( BimContractDTO.ATTRIBUTE.CONTRACT_CODE.getValue()));
				} 
				command.setSort(sort);
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				if (StringUtils.hasText(order)) {
					command.setOrder(order);
				} else {
					command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
				}
			}
			return command;
		} catch (Exception e) {
			LOG.error("Exception----parse()", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
	}
	
	/**
	 * Purpose: 下載
	 * @author KevinShen
	 * @param request: 請求對象
	 * @param response: 響應對象 
	 * @param command: formDTO
	 * @throws CommonException: 出錯時拋出CommonException
	 * @return void
	 */
	public void download(HttpServletRequest request, HttpServletResponse response, ContractManageFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_FILE_PATH, command);
			if (sessionContext != null) {
				command = (ContractManageFormDTO) sessionContext.getResponseResult();
				//下載
				FileUtils.download(request, response, command.getFilePath(), command.getFileName());
			}
		} catch (Exception e) {
			LOG.error("Exception----download()", e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
}
