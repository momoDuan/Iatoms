package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.context.SessionContext;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.RepoStatusReportFormDTO;
import com.cybersoft4u.xian.iatoms.services.IRepoStatusReportService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IRepoStatusReportDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;

/**
 * Purpose: 設備狀態報表Service
 * @author barryzhang
 * @since  JDK 1.7
 * @date   2016年7月22日
 * @MaintenancePersonnel barryzhang
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RepoStatusReportService extends AtomicService implements IRepoStatusReportService {

	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(RepoStatusReportService.class);
	
	/**
	 * 設備狀態報表DAO注入
	 */
	private IRepoStatusReportDAO repoStatusReportDAO;
	
	/**
	 * 使用者DAO
	 */
	private IWarehouseDAO warehouseDAO;
	/**
	 * 使用者帳號管理DAO
	 */
	private IAdmUserDAO admUserDAO;
	
	/**
	 * Constructor: 無參構造
	 */
	public RepoStatusReportService() {

	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IRepoHistoryService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		try{
			RepoStatusReportFormDTO formDTO = (RepoStatusReportFormDTO) sessionContext.getRequestParameter();
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			//當前登入者對應之公司
			String  logonUserCompanyId = logonUser.getAdmUserDTO().getCompanyId();
			//當前登入者角色屬性
			String userAttribute = null;
			//得到用戶角色列表
			List<AdmRoleDTO> userRoleList = logonUser.getUserFunctions();
			//是否為客戶
			boolean isCustomerAttribute = true;
			//遍歷所有的角色
			for (AdmRoleDTO admRoleDTO : userRoleList) {
				//獲取角色Code
				userAttribute = admRoleDTO.getAttribute();
				//是廠商角色
				if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(userAttribute)) {
					//如果即是客戶角色又是廠商角色則設置為廠商角色
					formDTO.setRoleAttribute(IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
					//不是客戶
					isCustomerAttribute = false;
					break;
				} else {
					//設置角色屬性為客戶
					formDTO.setRoleAttribute(IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE);
				}
			}
			//登入者如果是客戶屬性，則顯示對應之公司信息
			if (isCustomerAttribute) {
				formDTO.setLogonUserCompanyId(logonUserCompanyId);
			}
			//查詢月份預設系統當前月份
			String currentMonth = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
			formDTO.setQueryDate(currentMonth);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		} catch (Exception e) {
			LOGGER.error("RepoStatusReportService.init():", "is error", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	/**
	 * 判斷用戶角色屬性
	 */
	private void setUserRoleAttribute(RepoStatusReportFormDTO formDTO) {
		IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
		if (logonUser != null) {
			//當前登入者角色屬性
			String userAttribute = null;
			// 是客戶角色
			Boolean isCustomerAttribute = false;
			// 是廠商角色
			Boolean isVendorAttribute = false;
			// Task #3583  是客戶廠商角色
			Boolean isCustomerVendorAttribute = false;
			//得到用戶角色列表
			List<AdmRoleDTO> userRoleList = logonUser.getUserFunctions();
			//遍歷所有的角色
			for (AdmRoleDTO admRoleDTO : userRoleList) {
				userAttribute = admRoleDTO.getAttribute();
				// 是廠商角色
				if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(userAttribute)) {
					// 如果即是客戶角色又是廠商角色則設置為廠商角色
					isVendorAttribute = true;
					//Task #3583  客戶廠商      如果即是客戶廠商角色又是廠商角色則設置為廠商角色
				} else if (IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(userAttribute)) {
					isCustomerVendorAttribute = true;
					// 是客戶角色
				} else if(IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE.equals(userAttribute)){
					isCustomerAttribute = true;
				}
			}
			if (isVendorAttribute) {
				//如果即是客戶角色又是廠商角色則設置為廠商角色
				formDTO.setRoleAttribute(IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
			} else if (isCustomerVendorAttribute) {
				formDTO.setRoleAttribute(IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE);
			} else if (isCustomerAttribute) {
				formDTO.setRoleAttribute(IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE);
			}
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IRepoStatusReportService#query(cafe.core.context.SessionContext)
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		RepoStatusReportFormDTO formDTO = null;
		//查詢結果集
		List<RepoStatusReportDTO> results = new ArrayList<RepoStatusReportDTO>();
		Message message = null;
		try {
			formDTO = (RepoStatusReportFormDTO) sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			//控管倉庫列表
			List<String> warehouseIdList  = new ArrayList<String>();
			//可見的倉庫信息列表
			List<Parameter> tempWarehouseList = null;
			//得到當前登入者信息
			AdmUser admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, logonUser.getId());
			if (admUser != null) {
				//判斷是否倉庫控管
				String dataAcl = admUser.getDataAcl();
				//Task #3583 當 【客戶廠商角色】 時 不設倉庫控管，可見本公司下所有設備
				setUserRoleAttribute(formDTO);
				if (IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(formDTO.getRoleAttribute())) {
					dataAcl = IAtomsConstants.NO;
				}
				//是倉庫的時
				if (IAtomsConstants.YES.equals(dataAcl)) {
					//根據使用者的編號查詢倉庫據信息
					tempWarehouseList = this.warehouseDAO.getWarehouseByUserId(admUser.getId());
					//若存在倉庫據點信息
					if (!CollectionUtils.isEmpty(tempWarehouseList)) {
						//遍歷倉庫信息將倉庫的值依次加到控管的倉庫列表中
						for (Parameter warehouse : tempWarehouseList) {
							warehouseIdList.add((String) warehouse.getValue());
						}
					} else {
						//不存在倉庫信息時查無資料
						sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND));
						sessionContext.setResponseResult(formDTO);
						return sessionContext;
					}
				} 
				//查詢條件--設備名稱
				String queryAssetNames = formDTO.getQueryAssetNames();
				//查詢條件--通訊模式
				String queryCommModes = formDTO.getQueryCommModes();
				//查詢條件--客戶
				String queryCustomer = formDTO.getQueryCustomer();
				//查詢條件--維護模式
				String queryMaType = formDTO.getQueryMaType();
				//查詢條件--查詢日期
				String queryDate = formDTO.getQueryDate();
				String yyyyMM = formDTO.getYyyyMM();
				//查詢條件--排序方式
				String order = formDTO.getOrder();
				//查詢條件--排序方式
				String sort = formDTO.getSort();
				//當前頁碼
				Integer page = formDTO.getPage();
				//每頁顯示多少筆
				Integer rows = formDTO.getRows();
				//得到系統當前月份
				String strCurrentDate = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMM);
				//查詢月份小於系統當前月份，則從設備資料月檔抓取資料；反之，則從設備最新資料檔抓取資料。
				String queryTableName = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY;
				//當查詢月份不是當前月時查詢欄位來自DMM_REPOSITORY表
				if (strCurrentDate.equals(yyyyMM)) {
					queryTableName = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY;
				}
				//如果查詢條件 - 是否有客戶決定查詢的dao層方法
				if (StringUtils.hasText(queryCustomer)) {
					//查詢條件有客戶
					//調用DAO的方法查詢
					results = this.repoStatusReportDAO.listBy(queryTableName, queryCustomer, queryMaType, queryAssetNames, queryCommModes, yyyyMM, order, sort, warehouseIdList, dataAcl);
					if (!CollectionUtils.isEmpty(results)) {
						RepoStatusReportDTO reportDTO = new RepoStatusReportDTO();
							//按查詢結果分類合計
							results = combine(results, queryDate);
							//遍歷查詢結果計算每筆數據的小記
							for (RepoStatusReportDTO repoStatusReportDTO : results) {
								//當設備名稱為小計時分別給每個欄位設置相應的值
								if (repoStatusReportDTO.getAssetTypeName().equals(i18NUtil.getName(IAtomsConstants.FIELD_REPO_STATUS_REPORT_SUM))){
									reportDTO.setInStorage(reportDTO.getInStorage() + repoStatusReportDTO.getInStorage());
									reportDTO.setInApply(reportDTO.getInApply() + repoStatusReportDTO.getInApply());
									reportDTO.setInBorrow(reportDTO.getInBorrow() + repoStatusReportDTO.getInBorrow());
									reportDTO.setInUse(reportDTO.getInUse() + repoStatusReportDTO.getInUse());
									reportDTO.setInFault(reportDTO.getInFault() + repoStatusReportDTO.getInFault());
									reportDTO.setInLoss(reportDTO.getInLoss() + repoStatusReportDTO.getInLoss());
									reportDTO.setToScrap(reportDTO.getToScrap() + repoStatusReportDTO.getToScrap());
									reportDTO.setToIdentify(reportDTO.getToIdentify() + repoStatusReportDTO.getToIdentify());
									reportDTO.setInTrans(reportDTO.getInTrans() + repoStatusReportDTO.getInTrans());
									reportDTO.setInRepair(reportDTO.getInRepair() + repoStatusReportDTO.getInRepair());
									//Bug #2382 update by 2017/09/12
									reportDTO.setInRepaired(reportDTO.getInRepaired() + repoStatusReportDTO.getInRepaired());
									reportDTO.setInScrap(reportDTO.getInScrap() + repoStatusReportDTO.getInScrap());
									reportDTO.setInDestroy(repoStatusReportDTO.getInDestroy() + reportDTO.getInDestroy());
									//Task #3242
									reportDTO.setInReturned(repoStatusReportDTO.getInReturned() + reportDTO.getInReturned());
									reportDTO.setInLost(repoStatusReportDTO.getInLost() + reportDTO.getInLost());
									reportDTO.setTotal(reportDTO.getTotal() + repoStatusReportDTO.getTotal());
								} 
							}
							reportDTO.setAssetTypeName(i18NUtil.getName(IAtomsConstants.FIELD_REPO_STATUS_REPORT_ALL_SUM));
							reportDTO.setCompanyName(results.get(0).getCompanyName());
							results.add(reportDTO);
						// 設置序號
						for(RepoStatusReportDTO repoStatusReportDTO : results){
							repoStatusReportDTO.setNumber(Integer.parseInt(IAtomsConstants.LEAVE_CASE_STATUS_ONE));
						}
						formDTO.getPageNavigation().setRowCount(Integer.parseInt(IAtomsConstants.LEAVE_CASE_STATUS_ONE));
					}
				} else {
					//查詢條件無客戶
					int sum = 0;
					List<RepoStatusReportDTO> tempResults = null;
					//查詢當前頁客戶列表repoStatusReportDTO
					List<RepoStatusReportDTO> repoStatusReportDTOList = this.repoStatusReportDAO.companylistBy(queryTableName, queryMaType, queryAssetNames, queryCommModes, yyyyMM, order, sort, warehouseIdList, dataAcl, rows, page);
					//存在查詢結果時
					if (!CollectionUtils.isEmpty(repoStatusReportDTOList)) {
						//查詢結果的筆數
						int repoStatusReportDTOListSize = repoStatusReportDTOList.size();
						//遍歷查詢結果
						for (int i = 0;i < repoStatusReportDTOListSize; i++) {
							//判斷遍歷中的對象，若對象存在
							if (repoStatusReportDTOList.get(i) != null) {
								sum = 0;
								RepoStatusReportDTO reportDTO = new RepoStatusReportDTO();
								//調用DAO層的方法查詢
								tempResults = this.repoStatusReportDAO.listBy(queryTableName, repoStatusReportDTOList.get(i).getCompanyId(), queryMaType, queryAssetNames, queryCommModes, yyyyMM, order, sort, warehouseIdList, dataAcl);
								if (!CollectionUtils.isEmpty(tempResults)) {
	 								//按查詢結果分類合計
	 								tempResults = combine(tempResults, queryDate);
	 								// 設置序號
	 								for (RepoStatusReportDTO repoStatusReportDTO : tempResults) {
	 									//當設備名稱為小計時分別給每個欄位設置相應的值
	 									if (repoStatusReportDTO.getAssetTypeName().equals(i18NUtil.getName(IAtomsConstants.FIELD_REPO_STATUS_REPORT_SUM))){
											reportDTO.setInStorage(repoStatusReportDTO.getInStorage() + reportDTO.getInStorage());
											reportDTO.setInApply(repoStatusReportDTO.getInApply() + reportDTO.getInApply());
											reportDTO.setInBorrow(repoStatusReportDTO.getInBorrow() + reportDTO.getInBorrow());
											reportDTO.setInUse(repoStatusReportDTO.getInUse() + reportDTO.getInUse());
											reportDTO.setInFault(repoStatusReportDTO.getInFault() + reportDTO.getInFault());
											reportDTO.setInLoss(repoStatusReportDTO.getInLoss() + reportDTO.getInLoss());
											reportDTO.setToScrap(repoStatusReportDTO.getToScrap() + reportDTO.getToScrap());
											reportDTO.setToIdentify(repoStatusReportDTO.getToIdentify() + reportDTO.getToIdentify());
											reportDTO.setInTrans(repoStatusReportDTO.getInTrans() + reportDTO.getInTrans());
											reportDTO.setInRepair(repoStatusReportDTO.getInRepair() + reportDTO.getInRepair());
											//Bug #2382 update by 2017/09/12
											reportDTO.setInRepaired(repoStatusReportDTO.getInRepaired() + reportDTO.getInRepaired());
											reportDTO.setInScrap(repoStatusReportDTO.getInScrap() + reportDTO.getInScrap());
											reportDTO.setInDestroy(repoStatusReportDTO.getInDestroy() + reportDTO.getInDestroy());
											//Task #3242
											reportDTO.setInReturned(repoStatusReportDTO.getInReturned() + reportDTO.getInReturned());
											reportDTO.setInLost(repoStatusReportDTO.getInLost() + reportDTO.getInLost());
											reportDTO.setTotal(repoStatusReportDTO.getTotal() + reportDTO.getTotal());
										}
									}
	 								reportDTO.setAssetTypeName(i18NUtil.getName(IAtomsConstants.FIELD_REPO_STATUS_REPORT_ALL_SUM));
	 								reportDTO.setCompanyName(repoStatusReportDTOList.get(i).getCompanyName());
	 								tempResults.add(reportDTO);
									for(RepoStatusReportDTO repoStatusReportDTO : tempResults){
										repoStatusReportDTO.setNumber(rows * (page - 1) + i + 1);
									}
								}
							}
							results.addAll(tempResults);
	 					}
						//全查
						List<RepoStatusReportDTO> repoStatusReportDTOListCount = this.repoStatusReportDAO.companylistBy(queryTableName, queryMaType, queryAssetNames, queryCommModes, yyyyMM, order, sort, warehouseIdList, dataAcl, -1, -1);
	 					//設置查詢結果的總筆數
						formDTO.getPageNavigation().setRowCount(repoStatusReportDTOListCount.size());
					} 
				}
				if (!CollectionUtils.isEmpty(results)) {
					formDTO.setList(results);
				} else {
					message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				}
			} /*else {
				message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.QUERY_FAILURE);
			}*/
			sessionContext.setReturnMessage(message);
			sessionContext.setResponseResult(formDTO);
		} catch (Exception e) {
			LOGGER.error(".query() Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IRepoStatusReportService#export(cafe.core.context.SessionContext)
	 */
	public SessionContext export(SessionContext sessionContext) throws ServiceException {
		RepoStatusReportFormDTO formDTO = null;
		List<RepoStatusReportDTO> results = null;
		try{
			formDTO = (RepoStatusReportFormDTO) sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			//控管倉庫列表
			List<String> warehouseIdList  = new ArrayList<String>();
			//可見的倉庫信息列表
			List<Parameter> tempWarehouseList = null;
			//得到當前登入者信息
			AdmUserDTO admUserDTO = logonUser.getAdmUserDTO();
			//判斷是否倉庫控管
			String dataAcl = admUserDTO.getDataAcl();
			//Task #3583 當 【客戶廠商角色】 時 不設倉庫控管，可見本公司下所有設備
			setUserRoleAttribute(formDTO);
			if (IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(formDTO.getRoleAttribute())) {
				dataAcl = IAtomsConstants.NO;
			}
			//若是倉管，調用DAO層的查詢管轄的倉庫信息
			if (IAtomsConstants.YES.equals(dataAcl)) {
				tempWarehouseList = this.warehouseDAO.getWarehouseByUserId(admUserDTO.getUserId());
				//若存在管轄的倉庫就遍歷查詢結果一次將查詢結果添加到匯出的數據中
				if (!CollectionUtils.isEmpty(tempWarehouseList)) {
					for (Parameter warehouse : tempWarehouseList) {
						warehouseIdList.add((String) warehouse.getValue());
					}
				}
			} 
			//查詢條件--設備名稱
			String queryAssetNames = formDTO.getQueryAssetNames();
			LOGGER.debug("export()", "parameters:queryAssetNames=", queryAssetNames);
			//查詢條件--通訊模式
			String queryCommModes = formDTO.getQueryCommModes();
			LOGGER.debug("export()", "parameters:queryCommModes=", queryCommModes);
			//查詢條件--設備名稱
			String queryCustomer = formDTO.getQueryCustomer();
			LOGGER.debug("export()", "parameters:queryCustomer=", queryCustomer);
			//查詢條件--維護模式
			String queryMaType = formDTO.getQueryMaType();
			LOGGER.debug("export()", "parameters:queryMaType=", queryMaType);
			//查詢條件--查詢日期
			String queryDate = formDTO.getQueryDate();
			LOGGER.debug("export()", "parameters:queryDate=", queryDate);
			//數據庫匹配的日期格式
			String yyyyMM = formDTO.getYyyyMM();
			//查詢條件--排序方式
			String order = formDTO.getOrder();
			//匯出報表模式
			String reportType = formDTO.getReportType();
			//轉換當前時間，若未 當前月份則查詢庫存主檔，否則查詢月表中對應的月份資料
			String strCurrentDate = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMM);
			String queryTableName = null;
			//根據查詢月份確定查詢SQL中的數據表
			if(strCurrentDate.equals(yyyyMM)){
				queryTableName = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY;
			} else {
				queryTableName = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY;
			}
			//查詢結果--總筆數
			Integer rowCount = 0;
			rowCount = this.repoStatusReportDAO.count(queryTableName, queryCustomer, queryMaType, queryAssetNames, queryCommModes, yyyyMM, warehouseIdList, dataAcl);
			if (rowCount > 0) {
				//匯出清單
				if (StringUtils.hasText(reportType) && reportType.equals(RepoStatusReportFormDTO.EXPORT_REPORT_TYPE_LIST)) {
					results = this.repoStatusReportDAO.listRepositoryStatus(queryTableName, queryCustomer, queryMaType, queryAssetNames, queryCommModes, yyyyMM, warehouseIdList, dataAcl);
				//匯出
				} else {
					results = this.repoStatusReportDAO.listBy(queryTableName, queryCustomer, queryMaType, queryAssetNames, queryCommModes, yyyyMM, order, null, warehouseIdList, dataAcl);
					results = combine(results, queryDate);
				}
				//得到不重複查詢結果的客戶列表
				List<String> companyIdList= new ArrayList<String>();
				if (!CollectionUtils.isEmpty(results)) {
					for (RepoStatusReportDTO repoStatusReportDTO:results) {
						companyIdList.add(repoStatusReportDTO.getCompanyId());
						if (StringUtils.hasText(reportType) && reportType.equals(RepoStatusReportFormDTO.EXPORT_REPORT_TYPE_LIST)) {
							if (StringUtils.hasText(repoStatusReportDTO.getIsEnabled())) {
								repoStatusReportDTO.setIsEnabled(i18NUtil.getName(repoStatusReportDTO.getIsEnabled()));
							}
							if (StringUtils.hasText(repoStatusReportDTO.getIsCup())) {
								repoStatusReportDTO.setIsCup(i18NUtil.getName(repoStatusReportDTO.getIsCup()));
							}
							if (StringUtils.hasText(repoStatusReportDTO.getIsChecked())) {
								repoStatusReportDTO.setIsChecked(i18NUtil.getName(repoStatusReportDTO.getIsChecked()));
							}
							if (StringUtils.hasText(repoStatusReportDTO.getCustomerChecked())) {
								repoStatusReportDTO.setCustomerChecked(i18NUtil.getName(repoStatusReportDTO.getCustomerChecked()));
							}
						}
					}
				}
				//去除List中重複的客戶
				Set set = new  LinkedHashSet(companyIdList); 
				companyIdList.clear(); 
				companyIdList.addAll(set);
				//移除所有的null
				List<Integer> nullList = new ArrayList<Integer>();  
				nullList.add(null); 
				companyIdList.removeAll(nullList); 
				formDTO.setCompanyIdList(companyIdList);
				formDTO.setList(results);
			}
			sessionContext.setResponseResult(formDTO);
		} catch (Exception e) {
			LOGGER.error(".export() Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * 
	 * Purpose: 按廠商與倉庫分類合計
	 * @author barryzhang
	 * @param sourceList 查詢的原始結果
	 * @param queryDate 查詢日期
	 * @throws ServiceException 錯誤時拋出的Service異常
	 * @return List<RepoStatusReportDTO> 分類合計后的結果集
	 */
	private List<RepoStatusReportDTO> combine(List<RepoStatusReportDTO> sourceList, String queryDate) throws ServiceException{
		List<RepoStatusReportDTO> results = null;
		try{
			if(!CollectionUtils.isEmpty(sourceList)){
				RepoStatusReportDTO compareDto = null;
				RepoStatusReportDTO tempDto = null;
				RepoStatusReportDTO sumDTO = null;
				//顯示結果集
				results = new ArrayList<RepoStatusReportDTO>();
				results.addAll(sourceList);
				//用來判斷是否比較到最後一個
				boolean flag = false;
				int count = 0;
				int sourceListSize = sourceList.size();
				//對結果集進行遍歷
				for(int i=0;i < sourceListSize ;i++){
					compareDto = sourceList.get(i);
					compareDto.setYyyyMM(queryDate);
					sumDTO = new RepoStatusReportDTO();
					sumDTO.setCompanyId(compareDto.getCompanyId());
					sumDTO.setCompanyName(compareDto.getCompanyName());
					sumDTO.setWarehouseId(compareDto.getWarehouseId());
					sumDTO.setWarehouseName(compareDto.getWarehouseName());
					sumDTO.setAssetCategory(compareDto.getAssetCategory());
					sumDTO.setAssetTypeName(i18NUtil.getName(IAtomsConstants.FIELD_REPO_STATUS_REPORT_SUM));
					sumDTO = sum(sumDTO, compareDto);
					count ++;
					//遍歷信息求每筆數據的總計
					for(int j = i + 1;j < sourceListSize;j++){
						tempDto = sourceList.get(j);
						//對應兩次遍歷的數據中倉庫名、客戶名、庫存狀態是否相等
						if(compareDto.getWarehouseId().equals(tempDto.getWarehouseId())){
							if(compareDto.getWarehouseName().equals(tempDto.getWarehouseName())){
								if(compareDto.getCompanyId().equals(tempDto.getCompanyId())){
									if(compareDto.getAssetCategory().equals(tempDto.getAssetCategory())){
										//調用計算的方法
										sumDTO = sum(sumDTO, tempDto);
										if (j == sourceList.size() - 1) {
											flag = true;
										}
									} else{
										i = j - 1;
										break;
									}
								}else{
									i = j - 1;
									break;
								}
							}else{
								i = j - 1;
								break;
							}
						}else{
							i = j - 1;
							break;
						}
					}
					if (flag) {
						results.add(sumDTO);
						break;
					} else {
						results.add((i + count), sumDTO);
					}
				}
			}
		}catch(Exception e){
			LOGGER.error(".combine() Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return results;
	}
	
	/**
	 * Purpose:計算兩個dto中數據屬性的總和
	 * @author barryzhang
	 * @param sumDTO: 設備狀態報表DTO
	 * @param dto：設備狀態報表DTO
	 * @throws ServiceException：錯誤時拋出的Service異常
	 * @return RepoStatusReportDTO: 設備狀態報表DTO
	 */
	private RepoStatusReportDTO sum(RepoStatusReportDTO sumDTO, RepoStatusReportDTO dto) throws ServiceException {
		try {
			//分別將計算的結果設置到對應的欄位
			if (dto != null) {
				sumDTO.setInStorage(dto.getInStorage() + sumDTO.getInStorage());
				sumDTO.setInApply(dto.getInApply() + sumDTO.getInApply());
				sumDTO.setInBorrow(dto.getInBorrow() + sumDTO.getInBorrow());
				sumDTO.setInUse(dto.getInUse() + sumDTO.getInUse());
				sumDTO.setInFault(dto.getInFault() + sumDTO.getInFault());
				sumDTO.setInLoss(dto.getInLoss() + sumDTO.getInLoss());
				sumDTO.setToScrap(dto.getToScrap() + sumDTO.getToScrap());
				sumDTO.setToIdentify(dto.getToIdentify() + sumDTO.getToIdentify());
				sumDTO.setInTrans(dto.getInTrans() + sumDTO.getInTrans());
				sumDTO.setInRepair(dto.getInRepair() + sumDTO.getInRepair());
				//Bug #2382 update by 2017/09/12
				sumDTO.setInRepaired(dto.getInRepaired() + sumDTO.getInRepaired());
				sumDTO.setInScrap(dto.getInScrap() + sumDTO.getInScrap());
				sumDTO.setInDestroy(dto.getInDestroy() + sumDTO.getInDestroy());
				//
				sumDTO.setInReturned(dto.getInReturned() + sumDTO.getInReturned());
				sumDTO.setInLost(dto.getInLost() + sumDTO.getInLost());
				sumDTO.setTotal(dto.getTotal() + sumDTO.getTotal());
			}
		} catch (Exception e) {
			LOGGER.error(".sum() Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sumDTO;
	}

	/**
	 * @return the repoStatusReportDAO
	 */
	public IRepoStatusReportDAO getRepoStatusReportDAO() {
		return repoStatusReportDAO;
	}

	/**
	 * @param repoStatusReportDAO the repoStatusReportDAO to set
	 */
	public void setRepoStatusReportDAO(IRepoStatusReportDAO repoStatusReportDAO) {
		this.repoStatusReportDAO = repoStatusReportDAO;
	}

	/**
	 * @return the warehouseDAO
	 */
	public IWarehouseDAO getWarehouseDAO() {
		return warehouseDAO;
	}

	/**
	 * @param warehouseDAO the warehouseDAO to set
	 */
	public void setWarehouseDAO(IWarehouseDAO warehouseDAO) {
		this.warehouseDAO = warehouseDAO;
	}

	/**
	 * @return the admUserDAO
	 */
	public IAdmUserDAO getAdmUserDAO() {
		return admUserDAO;
	}

	/**
	 * @param admUserDAO the admUserDAO to set
	 */
	public void setAdmUserDAO(IAdmUserDAO admUserDAO) {
		this.admUserDAO = admUserDAO;
	}
	
}
