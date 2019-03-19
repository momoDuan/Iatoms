package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStacktakeFormDTO;
import com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeCategoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeStatusDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetStocktakeCategory;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetStocktakeCategoryId;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetStocktakeInfo;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetStocktakeList;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetStocktakeStatus;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetStocktakeStatusId;

/**
 * 
 * Purpose:設備盤點service 
 * @author echomou
 * @since  JDK 1.7
 * @date   2016-7-15
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class AssetStacktakeService extends AtomicService implements IAssetStacktakeService {

	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, AssetStacktakeService.class);
	/**
	 * 設備盤點主檔
	 */
	private IDmmAssetStacktakeInfoDAO assetStocktackInfoDAO;
	/**
	 * 設備盤點狀態
	 */
	private IDmmAssetStacktakeStatusDAO assetStocktackStatusDAO;
	/**
	 * 設備盤點種類
	 */
	private IDmmAssetStacktakeCategoryDAO assetStocktackCategroyDAO;
	/**
	 * 設備盤點明細
	 */
	private IDmmAssetStacktakeListDAO assetStocktackListDAO;
	/**
	 * 庫存
	 */
	private IDmmRepositoryDAO dmmRepositoryDAO;
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMailListManageService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		// 页面显示信息
		Message msg = null;
		try {
			AssetStacktakeFormDTO formDTO = (AssetStacktakeFormDTO) sessionContext.getRequestParameter();
			sessionContext.setResponseResult(formDTO);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".init(SessionContext sessionContext):" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService#initAdd(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initAdd(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".init:" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService#save(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		try {
			AssetStacktakeFormDTO formDTO = (AssetStacktakeFormDTO) sessionContext.getRequestParameter();
			//獲取當前登錄者信息
			LogonUser logonUser = formDTO.getLogonUser();
			String userId = null;
			String userName = null;
			//核檢當前是否有登錄者，如果沒有，則返回請先登錄
			if (logonUser != null) {
				//得到當前登入者的Id和Name
				userId = logonUser.getId();
				userName = logonUser.getName();
			} else {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.LIMITED_LOGON_ID));
				return sessionContext;
			}
			DmmAssetStacktakeInfoDTO assetStacktakeInfoDTO = formDTO.getAssetStacktakeInfoDTO();
			if (assetStacktakeInfoDTO != null) {
				String assetStatus = assetStacktakeInfoDTO.getAssetStatus();
				String assetTypeId = assetStacktakeInfoDTO.getAssetTypeId();
				String warWarehouseId = assetStacktakeInfoDTO.getWarWarehouseId();
				//檢核該倉庫中是否有該狀態的設備
				boolean isExistAsset = this.assetStocktackInfoDAO.isExistCheck(warWarehouseId, transformationStringMsg(assetTypeId), transformationStringMsg(assetStatus));
				if (!isExistAsset) {
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.WARHOUSE_NOT_EXIST_STATUS_ASSET, new String[]{assetStacktakeInfoDTO.getAssetStatusName(), assetStacktakeInfoDTO.getAssetTypeName()}));
					return sessionContext;
				}
				//生成盤點批號
				String date = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD);
				long id = this.getSequenceNumberControlDAO().getSeqNo(IAtomsConstants.IATOMS_TB_NAME_ASSET_STOCKTACK_INFO, date);
				if (id == 0) {
					id = id + 1;
				}
				String swiftNumber = StringUtils.toFixString(4, id);
				String stocktackId = new StringBuffer().append(IAtomsConstants.PARAM_ASSET_STOCKTAC_ID_IN).append(date).append(swiftNumber).toString();
				assetStacktakeInfoDTO.setStocktackId(stocktackId);
				//轉換
				Transformer transformer = new SimpleDtoDmoTransformer();
				//保存設備盤點主檔信息
				assetStacktakeInfoDTO.setCompleteStatus(IAtomsConstants.NO);
				assetStacktakeInfoDTO.setUpdatedById(userId);
				assetStacktakeInfoDTO.setUpdatedByName(userName);
				assetStacktakeInfoDTO.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				DmmAssetStocktakeInfo assetStocktakeInfo = (DmmAssetStocktakeInfo) transformer.transform(assetStacktakeInfoDTO, new DmmAssetStocktakeInfo());
				this.assetStocktackInfoDAO.getDaoSupport().save(assetStocktakeInfo);
				this.assetStocktackInfoDAO.getDaoSupport().flush();
				//保存設備盤點設備狀態表
				if (StringUtils.hasText(assetStatus)) {
					//將設備狀態按“，”分割為數組
					String[] assetStatusList = assetStatus.split(IAtomsConstants.MARK_SEPARATOR);
					DmmAssetStocktakeStatusId assetStocktackStatusId = null;
					DmmAssetStocktakeStatus assetStocktackStatus = null;
					//保存盤點設備狀態至設備盤點設備狀態表
					for (int i = 0; i <  assetStatusList.length; i++) {
						assetStocktackStatusId = new DmmAssetStocktakeStatusId(stocktackId, assetStatusList[i]);
						assetStocktackStatus = new DmmAssetStocktakeStatus();
						assetStocktackStatus.setId(assetStocktackStatusId);
						this.assetStocktackStatusDAO.getDaoSupport().saveOrUpdate(assetStocktackStatus);
					}
					this.assetStocktackStatusDAO.getDaoSupport().flush();
				}
				//保存設備盤點設備類別表
				if (StringUtils.hasText(assetTypeId)) {
					//將設備按“，”分割為數組
					String[] assetTypeIdList = assetTypeId.split(IAtomsConstants.MARK_SEPARATOR);
					DmmAssetStocktakeCategoryId assetStocktackCategoryId = null;
					DmmAssetStocktakeCategory assetStocktackCategory = null;
					//保存盤點設備至設備盤點設備類別表
					for (int i = 0; i < assetTypeIdList.length; i++) {
						assetStocktackCategoryId = new DmmAssetStocktakeCategoryId(stocktackId, assetTypeIdList[i]);
						assetStocktackCategory = new DmmAssetStocktakeCategory(assetStocktackCategoryId);
						this.assetStocktackCategroyDAO.getDaoSupport().saveOrUpdate(assetStocktackCategory);
					}
					this.assetStocktackCategroyDAO.getDaoSupport().flush();
				}
				//獲取保存設備轉倉資料明細檔
				this.saveStocktackList(assetStacktakeInfoDTO, IAtomsConstants.PARAM_NO_STOCKTAKE, logonUser);
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DATA_SAVE_SUCCESS, new String[]{this.getMyName()}));
				//把主鍵放在MAP集合裡面,用於新增完成後把新增的這個批號顯示在頁面上
				Map map = new HashMap();
				map.put(DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), stocktackId);
				//把主鍵放入MAP
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			} else {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_SAVE_FAILURE, new String[]{this.getMyName()}));
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".save() is Error", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * Purpose:將逗號隔開的字符串增加引號
	 * @author CarrieDuan
	 * @param message
	 * @return String
	 */
	public String transformationStringMsg (String message) {
		try {
			if(StringUtils.hasText(message)){
				StringBuffer messageBuffer = new StringBuffer();
				String[] messages = message.split(IAtomsConstants.MARK_SEPARATOR);
				for (String info : messages) {
					messageBuffer.append(IAtomsConstants.MARK_QUOTES).append(info).append(IAtomsConstants.MARK_QUOTES).append(IAtomsConstants.MARK_SEPARATOR);
				}
				return messageBuffer.substring(0, messageBuffer.length() - 1).toString();
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".transformationStringMsg() is Error", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return null;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService#getInventoryNumberList()
	 */
	@Override
	public List<Parameter> getInventoryNumberList() throws ServiceException {
		List<Parameter> inventoryNumberList = null;
		try {
			inventoryNumberList = this.assetStocktackInfoDAO.getAssetStocktakeIdList(false);
			return inventoryNumberList;
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".getInventoryNumberList() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getInventoryNumberList() is Error", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService#getStocktackIdList(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getStocktackIdList(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		List<Parameter> returnList = null;
		try {
			String warehouseId = (String) inquiryContext.getParameter(WarehouseDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
			String assetTypeId = (String) inquiryContext.getParameter(DmmAssetStacktakeInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			
			returnList = this.assetStocktackInfoDAO.getAssetStocktackIdHistList(warehouseId, assetTypeId);
			return returnList;
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".getStocktackIdList() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getStocktackIdList() is failed!!!  ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService#getAssetStocktackInfoByStocktackId(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public DmmAssetStacktakeListDTO getAssetStocktackInfoByStocktackId(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		DmmAssetStacktakeListDTO assetStocktackInfoDTO = null;
		try {
			//盤點批號
			String stocktackId = (String) inquiryContext.getParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue());
			if (StringUtils.hasText(stocktackId)) {
				assetStocktackInfoDTO = this.assetStocktackInfoDAO.getAssetStocktackInfoDTO(stocktackId);
				Integer count = this.getAssetStocktackListDAO().getCount(stocktackId, false);
				assetStocktackInfoDTO.setCount(count);
				if (assetStocktackInfoDTO != null) {
					String cateGoryName = assetStocktackInfoDTO.getAssetTypeName();
					if (StringUtils.hasText(cateGoryName)) {
						cateGoryName = cateGoryName.substring(0, cateGoryName.length());
						assetStocktackInfoDTO.setAssetTypeName(cateGoryName);
					}
					String statusName = assetStocktackInfoDTO.getAssetStatusName();
					if (StringUtils.hasText(statusName)) {
						statusName = statusName.substring(0, statusName.length());
						assetStocktackInfoDTO.setAssetStatusName(statusName);
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".getAssetStocktackInfoByStocktackId() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getAssetStocktackInfoByStocktackId() is Error ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
		return assetStocktackInfoDTO;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService#list(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext list(SessionContext sessionContext) throws ServiceException {
		try {
			AssetStacktakeFormDTO formDTO = (AssetStacktakeFormDTO) sessionContext.getRequestParameter();
			//入庫資料集合
			List<DmmAssetStacktakeListDTO> assetInInfoDTOs = null;
			String complete = formDTO.getIsComplete();
			boolean isComplete = false;
			if (StringUtils.hasText(complete)){
				isComplete = true;
			}
			String sort = formDTO.getSort();
			String order = formDTO.getOrder();
			//當前頁碼
			Integer currentPage = formDTO.getPage();
			//一頁的條數
			Integer pageSize = formDTO.getRows();
			//Map map = new HashMap();
			String stocktackId = formDTO.getQueryStocktackId();
			Message msg = null;
			if (StringUtils.hasText(stocktackId)) {
				//查詢入庫資料總條數
				Integer count = this.getAssetStocktackListDAO().getListCount(stocktackId, isComplete);
				if (count.intValue() > 0) {
					//查詢入庫資料集合
					assetInInfoDTOs = this.getAssetStocktackListDAO().listAssetInventory(stocktackId, isComplete, sort, order, currentPage, pageSize);
					if (!CollectionUtils.isEmpty(assetInInfoDTOs)) {
						formDTO.setList(assetInInfoDTOs);
					} 
				}
			formDTO.getPageNavigation().setRowCount(count.intValue());
			}
			//設置返回總條數
			//設置返回結果
			sessionContext.setResponseResult(formDTO);
			//設置返回消息
			if (!CollectionUtils.isEmpty(assetInInfoDTOs)) {
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			}
			sessionContext.setReturnMessage(msg);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".list() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".list() is Error", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService#query(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		try {
			AssetStacktakeFormDTO formDTO = (AssetStacktakeFormDTO) sessionContext.getRequestParameter();
			String stocktackId = formDTO.getQueryStocktackId();
			//入庫資料集合
			List<DmmAssetStacktakeListDTO> assetStocktackListDTOs = null;
			//入庫資料總條數
			Integer count = 0;
			Message msg = null;
			if (formDTO != null && StringUtils.hasText(stocktackId)) {
				//查詢入庫資料總條數
				count = this.getAssetStocktackListDAO().getCount(stocktackId, true);
				if (count > 0) {
					//查詢入庫資料集合
					assetStocktackListDTOs = this.getAssetStocktackListDAO().list(stocktackId, null, formDTO.getRows(), formDTO.getPage(), formDTO.getSort(), formDTO.getOrder());
					if (!CollectionUtils.isEmpty(assetStocktackListDTOs)) {
						for(DmmAssetStacktakeListDTO assetStocktackListDTO : assetStocktackListDTOs){
							String updateByName = assetStocktackListDTO.getUpdatedByName();
							if(StringUtils.hasText(updateByName) && updateByName.indexOf("(") >0 && updateByName.indexOf(")") > 0){
								assetStocktackListDTO.setUpdatedByName(updateByName.substring(updateByName.indexOf("(") + 1, updateByName.indexOf(")")));
							}
						}
						formDTO.setList(assetStocktackListDTOs);
					} 
				}
			}
			//設置返回總條數
			//update by 2017/08/18 Bug #2217
			if(formDTO.getPageNavigation() != null) {
				formDTO.getPageNavigation().setRowCount(count);
			}
			//設置返回結果
			sessionContext.setResponseResult(formDTO);
			//設置返回消息
			if (!CollectionUtils.isEmpty(assetStocktackListDTOs)) {
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			}
			sessionContext.setReturnMessage(msg);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".query() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".query() is Error  ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService#delete(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext delete(SessionContext sessionContext)	throws ServiceException {
		Message msg = null;
		try {
			AssetStacktakeFormDTO formDTO =  (AssetStacktakeFormDTO) sessionContext.getRequestParameter();
			String stocktackId = formDTO.getQueryStocktackId();
			if (StringUtils.hasText(stocktackId)) {
				//刪除設備種類
				this.assetStocktackCategroyDAO.deletedAssetStacktakeCategory(stocktackId);
				//刪除設備狀態
				this.assetStocktackStatusDAO.deletedAssetStacktakeStatus(stocktackId);
				//刪除盤點批號下的盤點設備
				this.assetStocktackListDAO.deleteStocktackList(stocktackId);
				//刪除設備盤點批號
				this.assetStocktackInfoDAO.getDaoSupport().delete(stocktackId, DmmAssetStocktakeInfo.class);
				this.assetStocktackInfoDAO.getDaoSupport().flush();
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".delete() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".delete isError", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService#send(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext send(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			AssetStacktakeFormDTO formDTO =  (AssetStacktakeFormDTO) sessionContext.getRequestParameter();
			String stocktackId = formDTO.getQueryStocktackId();
			String sendSerialNumber = formDTO.getSendSerialNumber();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			Transformer transformer = new SimpleDtoDmoTransformer();
			DmmAssetStocktakeList assetStocktack = null;
			if (StringUtils.hasText(sendSerialNumber) && StringUtils.hasText(stocktackId)) {
				//根據輸入的財產編號或設備序號獲取相應的庫存資料
				DmmRepositoryDTO repositoryDTO = this.dmmRepositoryDAO.getBySerialNumber(sendSerialNumber);
				//判斷repositoryDTO是否為空，如果為空，則代表該該設備序號不存在
				if (repositoryDTO != null) {
					//根據查詢出來的庫存DTO獲取設備序號，依據設備序號與盤點批號獲取相應的盤點列表信息
					List<DmmAssetStacktakeListDTO> assetStocktakeListDTO = this.assetStocktackListDAO.list(stocktackId, repositoryDTO.getSerialNumber(), -1, -1, null, null);
					if (CollectionUtils.isEmpty(assetStocktakeListDTO)) {
						assetStocktack = new DmmAssetStocktakeList();
						assetStocktack.setAssetStatus(repositoryDTO.getStatus());
						assetStocktack.setSerialNumber(repositoryDTO.getSerialNumber());
						assetStocktack.setAssetTypeId(repositoryDTO.getAssetTypeId());
						assetStocktack.setStocktackId(stocktackId);
						assetStocktack.setStocktakeStatus(IAtomsConstants.PARAM_MORE_STOCKTAKE);
						assetStocktack.setId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_ASSET_STOCKTACK_LIST));
					} else {
						assetStocktack = (DmmAssetStocktakeList) transformer.transform(assetStocktakeListDTO.get(0), new DmmAssetStocktakeList());
						assetStocktack.setId(assetStocktakeListDTO.get(0).getTackId());
						if (!IAtomsConstants.PARAM_MORE_STOCKTAKE.equals(assetStocktakeListDTO.get(0).getStocktakeStatus())) {
							assetStocktack.setStocktakeStatus(IAtomsConstants.PARAM_ALREADY_STOCKTAKE);
						}
					}
					assetStocktack.setUpdatedById(logonUser.getId());
					assetStocktack.setUpdatedByName(logonUser.getName());
					assetStocktack.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					this.assetStocktackListDAO.getDaoSupport().saveOrUpdate(assetStocktack);
					this.assetStocktackListDAO.getDaoSupport().flush();
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_SUCCESS, new String[]{this.getMyName()});
				} else {
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.IATOMS_MSG_NAT_EXIST_DATA, new String[]{"設備序號/財產編號"}));
					return sessionContext;
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setRequestParameter(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".send() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".send is Error", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService#saveRemark(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext saveRemark(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			AssetStacktakeFormDTO formDTO =  (AssetStacktakeFormDTO) sessionContext.getRequestParameter();
			List<DmmAssetStacktakeListDTO> stocktackListDTOs = formDTO.getAssetStocktackListDTOs();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			DmmAssetStocktakeList assetStocktackList = null;
			if (!CollectionUtils.isEmpty(stocktackListDTOs)) {
				for (DmmAssetStacktakeListDTO assetStocktackListDTO : stocktackListDTOs) {
					assetStocktackList = new DmmAssetStocktakeList();
					assetStocktackList = this.assetStocktackListDAO.findByPrimaryKey(DmmAssetStocktakeList.class, assetStocktackListDTO.getId());
					//assetStocktackList = (DmmAssetStocktakeList) transformer.transform(assetStocktackListDTO, assetStocktackList);
					//assetStocktackList.setId(assetStocktackListDTO.getTackId());
					assetStocktackList.setRemark(assetStocktackListDTO.getRemark());
					assetStocktackList.setUpdatedById(logonUser.getId());
					if(assetStocktackListDTO.getChecked()){
						assetStocktackList.setStocktakeStatus(IAtomsConstants.PARAM_ALREADY_STOCKTAKE);
					}
					assetStocktackList.setUpdatedByName(logonUser.getName());
					assetStocktackList.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					this.assetStocktackListDAO.getDaoSupport().saveOrUpdate(assetStocktackList);
				}
				
				this.assetStocktackListDAO.getDaoSupport().flush();
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_SUCCESS, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setRequestParameter(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".saveRemark() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".saveRemark is Error", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService#complete(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext complete(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			AssetStacktakeFormDTO formDTO = (AssetStacktakeFormDTO) sessionContext.getRequestParameter();
			String stocktackId = formDTO.getQueryStocktackId();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			//設備盤點主檔（設備盤點批號表）
			DmmAssetStocktakeInfo assetStocktackInfo = new DmmAssetStocktakeInfo();
			if (StringUtils.hasText(stocktackId)) {
				//抓取設備盤點批號詳細信息
				assetStocktackInfo = this.assetStocktackInfoDAO.findByPrimaryKey(DmmAssetStocktakeInfo.class,stocktackId);
				//設置盤點完成
				assetStocktackInfo.setCompleteStatus(IAtomsConstants.YES);
				assetStocktackInfo.setUpdatedById(logonUser.getId());
				assetStocktackInfo.setUpdatedByName(logonUser.getName());
				assetStocktackInfo.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				this.assetStocktackInfoDAO.getDaoSupport().saveOrUpdate(assetStocktackInfo);
				this.assetStocktackInfoDAO.getDaoSupport().flush();
				//設置盤點清單中未盤點的設備為盤差
				/*List<AssetStocktakeListDTO> assetStocktackList = this.assetStocktackListDAO.getAssetByStocktackId(stocktackId, null, null);
				if (!CollectionUtils.isEmpty(assetStocktackList)) {
					DmmAssetStocktakeList assetStocktack = null;
					for (AssetStocktakeListDTO assetStocktackListDTO : assetStocktackList) {
						assetStocktack = new DmmAssetStocktakeList();
						String id = assetStocktackListDTO.getTackId();
						assetStocktack = (DmmAssetStocktakeList) this.assetStocktackListDAO.findByPrimaryKey(DmmAssetStocktakeList.class, id);
						if (IAtomsConstants.PARAM_NO_STOCKTAKE.equals(assetStocktack.getStocktakeStatus())) {
							assetStocktack.setStocktakeStatus(IAtomsConstants.PARAM_LESS_STOCKTAKE);
						}
						assetStocktack.setUpdatedById(logonUser.getId());
						assetStocktack.setUpdatedByName(logonUser.getName());
						assetStocktack.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						this.assetStocktackListDAO.getDaoSupport().saveOrUpdate(assetStocktack);
					}
					this.assetStocktackListDAO.getDaoSupport().flush();
				}*/
				this.assetStocktackListDAO.completeStocktack(stocktackId, logonUser.getId(), logonUser.getName());
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_SUCCESS, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setRequestParameter(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".complete() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".complete() is Error", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService#exportInventory(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext exportInventory(SessionContext sessionContext) throws ServiceException {
		try {
			AssetStacktakeFormDTO formDTO = (AssetStacktakeFormDTO) sessionContext.getRequestParameter();
			List<DmmAssetStacktakeListDTO> assetStocktackListDTOs = this.assetStocktackListDAO.export(formDTO);
			List<DmmAssetStacktakeListDTO> stocktackListDTOs = new ArrayList<DmmAssetStacktakeListDTO>();
			//設備序號DTO
			if (!CollectionUtils.isEmpty(assetStocktackListDTOs)) {
				for (DmmAssetStacktakeListDTO assetStocktackListDTO : assetStocktackListDTOs) {
					assetStocktackListDTO.setUpdatedDate(assetStocktackListDTO.getUpdatedDate());
					stocktackListDTOs.add(assetStocktackListDTO);
				}
				formDTO.setAssetStocktackListDTOs(stocktackListDTOs);
			}
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".exportInventory() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".exportInventory is ERRER", e);
			throw new ServiceException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStacktakeService#exportSummary(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext exportSummary(SessionContext sessionContext) throws ServiceException {
		try {
			AssetStacktakeFormDTO formDTO = (AssetStacktakeFormDTO) sessionContext.getRequestParameter();
			//String updateDate = null;
			List<DmmAssetStacktakeListDTO> assetStocktackListDTOs = this.assetStocktackListDAO.exportSummary(formDTO);
			List<DmmAssetStacktakeListDTO> stocktackListDTOs = new ArrayList<DmmAssetStacktakeListDTO>();
			//設備序號DTO
			if (!CollectionUtils.isEmpty(assetStocktackListDTOs)) {
				for (DmmAssetStacktakeListDTO assetStocktackListDTO : assetStocktackListDTOs) {
					//updateDate = assetStocktackListDTO.getUpdatedDate().toString().substring(0,10);
					assetStocktackListDTO.setUpdatedDate(assetStocktackListDTO.getUpdatedDate());
					stocktackListDTOs.add(assetStocktackListDTO);
				}
				formDTO.setAssetStocktackListDTOs(stocktackListDTOs);
			}
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".exportSummary() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".exportSummary is Error", e);
			throw new ServiceException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * Purpose:保存盤點資料於assetStocktackList表
	 * @author echomou
	 * @param assetStocktackList 
	 * @param logonUser
	 * @return void
	 */
	private void saveStocktackList(DmmAssetStacktakeInfoDTO assetStacktakeInfoDTO, Integer stocktackStatus, LogonUser logonUser) {
		try {
			String listId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_ASSET_STOCKTACK_LIST);
			String assetTypeId = transformationStringMsg(assetStacktakeInfoDTO.getAssetTypeId());
			String assetStatus = transformationStringMsg(assetStacktakeInfoDTO.getAssetStatus());
			this.assetStocktackListDAO.saveStocktackList(listId, assetStacktakeInfoDTO.getStocktackId(), assetStacktakeInfoDTO.getWarWarehouseId(), assetTypeId, assetStatus, stocktackStatus, logonUser.getId(), logonUser.getName());
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".saveStocktackList() Service Exception--->" + e,e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * @return the assetStocktackInfoDAO
	 */
	public IDmmAssetStacktakeInfoDAO getAssetStocktackInfoDAO() {
		return assetStocktackInfoDAO;
	}
	/**
	 * @return the assetStocktackStatusDAO
	 */
	public IDmmAssetStacktakeStatusDAO getAssetStocktackStatusDAO() {
		return assetStocktackStatusDAO;
	}
	/**
	 * @return the assetStocktackCategroyDAO
	 */
	public IDmmAssetStacktakeCategoryDAO getAssetStocktackCategroyDAO() {
		return assetStocktackCategroyDAO;
	}
	/**
	 * @return the assetStocktackListDAO
	 */
	public IDmmAssetStacktakeListDAO getAssetStocktackListDAO() {
		return assetStocktackListDAO;
	}
	/**
	 * @param assetStocktackInfoDAO the assetStocktackInfoDAO to set
	 */
	public void setAssetStocktackInfoDAO(IDmmAssetStacktakeInfoDAO assetStocktackInfoDAO) {
		this.assetStocktackInfoDAO = assetStocktackInfoDAO;
	}
	/**
	 * @param assetStocktackStatusDAO the assetStocktackStatusDAO to set
	 */
	public void setAssetStocktackStatusDAO(IDmmAssetStacktakeStatusDAO assetStocktackStatusDAO) {
		this.assetStocktackStatusDAO = assetStocktackStatusDAO;
	}
	/**
	 * @param assetStocktackCategroyDAO the assetStocktackCategroyDAO to set
	 */
	public void setAssetStocktackCategroyDAO(IDmmAssetStacktakeCategoryDAO assetStocktackCategroyDAO) {
		this.assetStocktackCategroyDAO = assetStocktackCategroyDAO;
	}
	/**
	 * @param assetStocktackListDAO the assetStocktackListDAO to set
	 */
	public void setAssetStocktackListDAO(IDmmAssetStacktakeListDAO assetStocktackListDAO) {
		this.assetStocktackListDAO = assetStocktackListDAO;
	}
	/**
	 * @return the dmmRepositoryDAO
	 */
	public IDmmRepositoryDAO getRepositoryDAO() {
		return dmmRepositoryDAO;
	}
	/**
	 * @param dmmRepositoryDAO the dmmRepositoryDAO to set
	 */
	public void setRepositoryDAO(IDmmRepositoryDAO dmmRepositoryDAO) {
		this.dmmRepositoryDAO = dmmRepositoryDAO;
	}
	
}
