package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.config.SystemConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CompanyFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.PasswordEncoderUtilities;
import com.cybersoft4u.xian.iatoms.services.ICompanyService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompany;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompanyType;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompanyTypeId;

/**
 * Purpose: 公司基本訊息維護Service
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年7月1日
 * @MaintenancePersonnel ElvaHe
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CompanyService extends AtomicService implements ICompanyService{
	
	/**
	 *日志記錄物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, CompanyService.class);
	
	/**
	 * 公司基本訊息維護DAO
	 */
	private ICompanyDAO companyDAO;
	
	/**
	 * 公司類型DAO
	 */
	private ICompanyTypeDAO companyTypeDAO;
	
	/**
	 * 使用者帳號DAO
	 */
	private IAdmUserDAO admUserDAO;
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		CompanyFormDTO companyFormDTO = null;
		try {
			companyFormDTO = (CompanyFormDTO) sessionContext.getRequestParameter();
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(companyFormDTO);
		} catch (Exception e) {
			LOGGER.error("init(SessionContext sessionContext):", "error", e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#query(cafe.core.context.SessionContext)
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		CompanyFormDTO formDTO = null;
		Message msg = null;
		try {
			formDTO = (CompanyFormDTO) sessionContext.getRequestParameter();
			//獲取查詢條件 - 公司類型
			String companyType = formDTO.getQueryCompanyType();
			//獲取查詢條件 - 公司簡稱
			String shortName = formDTO.getQueryShortName();
			//獲取排序列
			String sort = formDTO.getSort();
			//獲取排序方式
			String order = formDTO.getOrder();
			//每頁筆數
			Integer pageSize = formDTO.getRows();
			//頁碼
			Integer pageIndex = formDTO.getPage();
			//符合查詢條件的信息數
			Integer totalSize = this.companyDAO.count(companyType, shortName);
			//若存在符合條件的查詢結果
			if (totalSize.intValue() > 0) {
				//根據查詢條件查詢符合條件的信息集合
				List<CompanyDTO> companyDTOs = this.companyDAO.listBy(companyType, shortName, sort, order, pageSize, pageIndex);
				formDTO.setList(companyDTOs);
				//設置總筆數
				formDTO.getPageNavigation().setRowCount(totalSize.intValue());
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			} else {
				//查無資料
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			}
			sessionContext.setResponseResult(formDTO);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("query()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("query()", "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#initEdit(cafe.core.context.SessionContext)
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException {
		CompanyFormDTO formDTO = null;
		Message msg = new Message();
		try {
			formDTO = (CompanyFormDTO) sessionContext.getRequestParameter();
			Transformer transformer = new SimpleDtoDmoTransformer();
			CompanyDTO companyDTO = null;
			BimCompany company = null;
			//公司類型信息集合
			List<CompanyTypeDTO> companyTypeDTOs = null;
			List<String> companyTypes = new ArrayList<String>(); 
			//公司基本訊息主鍵編號
			String companyId = formDTO.getCompanyId();
			//若存在要修改的公司編號
			if (StringUtils.hasText(companyId)) {
				//根據公司編號查詢該公司的信息
				company = this.companyDAO.findByPrimaryKey(BimCompany.class, companyId);
				//判斷查詢的公司信息
				if (company == null) {
					//若查詢的公司信息為空
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE);
				} else {
					//若查詢的公司信息不為空時
					companyDTO = (CompanyDTO) transformer.transform(company, new CompanyDTO());
					if(companyDTO != null){
						//根據公司編號獲取所有的公司類型
						companyTypeDTOs = this.companyTypeDAO.listByCompanyId(companyId);
						String companyType = null;
						//若存在公司類型集合時
						if(!CollectionUtils.isEmpty(companyTypeDTOs)){
							//該公司具有的公司類型的數量
							int size = companyTypeDTOs.size();
							//若存在公司類型時
							if (size > 0) {
								//循環該公司的公司類型，依次設置到公司DTO中
								for(int i = 0; i < size; i++){
									companyType = companyTypeDTOs.get(i).getCompanyType();
									companyTypes.add(companyType);
									companyDTO.setCompanyTypes(companyTypes);
								}
							}
							//將擁有的公司類型設置到公司信息DTO中
							companyDTO.setCompanyTypeDTOs(companyTypeDTOs);
						}
						//將公司DTO設置到formDTO中
						formDTO.setCompanyDTO(companyDTO);
					}
				}
			} else {
				//不存在要修改的公司編號
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE);
			}
			sessionContext.setResponseResult(formDTO);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("initEdit()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("initEdit()", "SessionContext sessionContext:", e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#save(cafe.core.context.SessionContext)
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		CompanyFormDTO formDTO = null;
		Message msg = null;
		boolean isRepate = false;
		try {
			formDTO = (CompanyFormDTO) sessionContext.getRequestParameter();
			//獲取要儲存的公司信息
			CompanyDTO companyDTO = formDTO.getCompanyDTO();
			//公司代號
			String companyCode = null;
			//公司簡稱
			String shortName = null;
			//客戶碼
			String customerCode = null;
			//登錄者信息
			String userId = formDTO.getLogonUser().getId();
			String userName = formDTO.getLogonUser().getName();
			//公司編號
			String companyId = null;
			BimCompany company = null;
			//若存在公司DTO
			if (companyDTO != null) {
				//依次獲取公司信息：公司編號、公司代號、公司簡稱、客戶碼
				companyId = companyDTO.getCompanyId();	
				companyCode = companyDTO.getCompanyCode();
				shortName = companyDTO.getShortName();
				customerCode = companyDTO.getCustomerCode();
				company = new BimCompany();
				//若存在公司編號
				if (StringUtils.hasText(companyId)) {
					//修改操作
					//根據公司編號查詢該公司的信息
					company = this.companyDAO.findByPrimaryKey(BimCompany.class, companyId);
					//獲取公司簡稱
					shortName = formDTO.getCompanyDTO().getShortName();
					company.setCompanyId(companyId);
					//若存在公司簡稱
					if (StringUtils.hasText(shortName)) {
						//判斷公司簡稱是否重複
						isRepate = this.companyDAO.isCheck(companyId, null, shortName, null);
						if (isRepate) {
							//公司簡稱重複
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.COMPANY_CODE_REPEAT,  new String[]{i18NUtil.getName(IAtomsMessageCode.MSG_SHORT_NAME)});
						} else{
							//公司簡稱不重複時，將輸入的數據設置到要保存的公司dmo中
							company.setShortName(shortName);
							company.setUnityNumber(formDTO.getCompanyDTO().getUnityNumber());
							company.setInvoiceHeader(formDTO.getCompanyDTO().getInvoiceHeader());
							company.setLeader(formDTO.getCompanyDTO().getLeader());
							company.setTel(formDTO.getCompanyDTO().getTel());
							company.setFax(formDTO.getCompanyDTO().getFax());
							company.setApplyDate(formDTO.getCompanyDTO().getApplyDate());
							company.setPayDate(formDTO.getCompanyDTO().getPayDate());
							company.setContact(formDTO.getCompanyDTO().getContact());
							company.setContactTel(formDTO.getCompanyDTO().getContactTel());
							company.setContactEmail(formDTO.getCompanyDTO().getContactEmail());
							company.setCompanyEmail(formDTO.getCompanyDTO().getCompanyEmail());
							company.setAddressLocation(formDTO.getCompanyDTO().getAddressLocation());
							company.setAddress(formDTO.getCompanyDTO().getAddress());
							company.setInvoiceAddressLocation(formDTO.getCompanyDTO().getInvoiceAddressLocation());
							company.setInvoiceAddress(formDTO.getCompanyDTO().getInvoiceAddress());
							company.setRemark(formDTO.getCompanyDTO().getRemark());
							company.setUpdatedById(userId);
							company.setUpdatedByName(userName);
							company.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
							company.setDeleted(IAtomsConstants.NO);
							company.setIsNotifyAo(companyDTO.getIsNotifyAo());
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()});
							this.companyDAO.getDaoSupport().update(company);
							this.companyDAO.getDaoSupport().flush();
						}
					} else {
						//公司簡稱必輸
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_SHORT_NAME)});
					}
				} else {
					//新增操作
					//存在公司代號
					if (StringUtils.hasText(companyCode)) {
						//判斷公司代號是否重複
						isRepate = this.companyDAO.isCheck(companyId, companyCode, null, null);
						if (isRepate) {
							//公司代號重複
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.COMPANY_CODE_REPEAT, new String[]{i18NUtil.getName(IAtomsMessageCode.MSG_COMPANY_CODE_NAME)});
						} else {
							//存在公司簡稱
							if (StringUtils.hasText(shortName)) {
								//判斷公司簡稱是否重複
								isRepate = this.companyDAO.isCheck(companyId, null, shortName, null);
								if (isRepate) {
									//公司簡稱重複
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.COMPANY_CODE_REPEAT, new String[]{i18NUtil.getName(IAtomsMessageCode.MSG_SHORT_NAME)});
								} else{
									//判斷是否存在客戶碼，存在時判斷是否重複
									if (StringUtils.hasText(customerCode)) {
										//判斷客戶碼是否重複
										isRepate = this.companyDAO.isCheck(companyId, null, null, customerCode);
										if (isRepate) {
											//客戶碼重複
											msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.CUSTOMER_CODE_REPEAT, new String[]{i18NUtil.getName(IAtomsMessageCode.MSG_CUSTOMER_CODE_NAME)});
										} else{
											//存在客戶碼且不重複，調用保存數據的方法
											this.saveData(companyDTO, userId, userName);
											msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
										}
									} else {
										//不存在客戶碼，調用保存數據的方法
										this.saveData(companyDTO, userId, userName);
										msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
									}
								}
							} else {
								//公司簡稱必輸
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_SHORT_NAME)});
							}
						}
					} else {
						//公司代號必輸
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_TYPE_COMPANY_CODE)});
					}
				}
			} else {
				//不存在公司DTO時，儲存失敗
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error("save()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}  catch (Exception e) {
			LOGGER.error("save()", "SessionContext sessionContext:", e);
			throw new ServiceException( IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#delete(cafe.core.context.SessionContext)
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		CompanyFormDTO formDTO = null;
		BimCompany company = null;
		Message msg = null;
		List<CompanyTypeDTO> companyTypeDTOs = null;
		List<AdmUserDTO> admUserDTOs = null;
		BimCompanyType companyType = null;
		try {
			formDTO = (CompanyFormDTO) sessionContext.getRequestParameter();
			Transformer transformer = new SimpleDtoDmoTransformer();
			//公司編號
			String companyId = formDTO.getCompanyId();
			//登錄者信息
			String useId = formDTO.getLogonUser().getId();
			String userName = formDTO.getLogonUser().getName();
			//若存在公司編號
			if(StringUtils.hasText(companyId)){
				company = this.companyDAO.findByPrimaryKey(BimCompany.class, companyId);
				//若存在公司對象
				if (company == null) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
				} else {
					//根據公司編號獲取該公司的所有類型信息
					companyTypeDTOs = this.companyTypeDAO.listByCompanyId(companyId);
					//檢核該公司下是否設定使用者帳號資料
					admUserDTOs = this.admUserDAO.listBy(companyId, null);
					//若存在使用者信息
					if (CollectionUtils.isEmpty(admUserDTOs)) {
						//遍歷擁有的公司類型，刪除公司類型表中的數據
						for (CompanyTypeDTO companyTypeDTO : companyTypeDTOs) {
							companyType = (BimCompanyType) transformer.transform(companyTypeDTO, new BimCompanyType());
							this.companyTypeDAO.getDaoSupport().delete(companyType);
						}
						//設置異動人員信息
						company.setUpdatedById(useId);
						company.setUpdatedByName(userName);
						company.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						//設置刪除位的值
						company.setDeleted(IAtomsConstants.YES);
						this.companyDAO.getDaoSupport().update(company);
						this.companyDAO.getDaoSupport().flush();
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
					} else {
						//該公司已設定使用者帳號，不可刪除
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.COMPANY_USER_EXISTS);
					}
				}
			} else {
				//不存在公司編號時，刪除失敗
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setResponseResult(formDTO);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("delete()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("delete()", "SessionContext sessionContext:", e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#export(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public SessionContext export(SessionContext sessionContext) throws ServiceException {
		try{
			CompanyFormDTO formDTO = null;
			List<CompanyDTO> companyDTOs = null;
			if(sessionContext != null){
				formDTO = (CompanyFormDTO) sessionContext.getRequestParameter();
				//查詢條件 - 公司類型
				String queryCompanyType = formDTO.getQueryCompanyType();
				LOGGER.debug("export()", "queryCompanyType :", queryCompanyType);
				//查詢條件 - 公司簡稱
				String queryShortName = formDTO.getQueryShortName();
				LOGGER.debug("export()", "queryShortName:", queryShortName);
				//依條件查出公司信息
				companyDTOs = this.companyDAO.listBy(queryCompanyType, queryShortName, null, null, Integer.valueOf(-1), Integer.valueOf(-1));
				//將查詢結果設置到formDTO中
				formDTO.setList(companyDTOs);
				sessionContext.setResponseResult(formDTO);
			}
		}catch(DataAccessException e){
			LOGGER.error("export()", "error in DataAccess:", e);
			throw new ServiceException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		}catch(Exception e){
			LOGGER.error("export()", "error in Service:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#getUnityNameByCompanyId(cafe.core.context.MultiParameterInquiryContext)
	 */
	public String getUnityNameByCompanyId(MultiParameterInquiryContext parameterInquiryContext)throws ServiceException {
		try {
			//獲取公司編號
			String companyId = (String) parameterInquiryContext.getParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue());
			//若存在公司編號
			if (StringUtils.hasText(companyId)) {
				//根據公司編號獲取該公司的信息
				BimCompany bimCompany = this.companyDAO.findByPrimaryKey(BimCompany.class, companyId);
				//若公司信息存在時，返回統一編號
				if (bimCompany != null) {
					return bimCompany.getUnityNumber();
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("getUnityNameByCompanyId()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE);
		} catch (Exception e) {
			LOGGER.error("getUnityNameByCompanyId()", "SessionContext sessionContext:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED); 
		}
		return null;
	
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#getCompanyByCompanyCode(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public CompanyDTO getCompanyByCompanyCode(MultiParameterInquiryContext parameterInquiryContext)throws ServiceException {
		try {
			//獲取公司code
			String companyCode = (String) parameterInquiryContext.getParameter(CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
			//若存在公司code
			if (StringUtils.hasText(companyCode)) {
				//根據公司code獲取該公司的信息
				CompanyDTO bimCompany = this.companyDAO.getCompanyByCompanyCode(companyCode);
				//若公司信息存在時，返回統一編號
				if (bimCompany != null) {
					return bimCompany;
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("getCompanyByCompanyCode()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE);
		} catch (Exception e) {
			LOGGER.error("getCompanyByCompanyCode()", "SessionContext sessionContext:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED); 
		}
		return null;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#getCompanyList(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getCompanyList(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		long startQueryCompanyTime = System.currentTimeMillis();
		List<Parameter> list = null;
		List<String> companyTypeList = new ArrayList<String>();
		try {
			//公司類型
			Object companyTypeObject = inquiryContext.getParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue());
			//根據公司類型的數據類型設置公司類型集合的值
			if (companyTypeObject instanceof String) {
				companyTypeList.add((String) companyTypeObject);
				LOGGER.debug(".getCompanyList() --> companyTypeObject: ", (String) companyTypeObject);
			} else if (companyTypeObject instanceof List) {
				companyTypeList = (List<String>) companyTypeObject;
			} else {
				companyTypeList = null;
			}
			// 登入驗證方式
			String authenticationType = (String) inquiryContext.getParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue());
			LOGGER.debug(".getCompanyList() --> authenticationType: ", authenticationType);
			// 是否有sla
			Boolean isHaveSla = Boolean.valueOf(false);
			//獲取是否有sla
			Object isHaveSlaObject = inquiryContext.getParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue());
			//若獲取的是否有sla的數據類型是boolean，則將值賦值為isHaveSla
			if(isHaveSlaObject instanceof Boolean){
				isHaveSla = (Boolean) isHaveSlaObject;
			} 
			//dtid生成方式
			String dtidType = (String) inquiryContext.getParameter(CompanyDTO.ATTRIBUTE.DTID_TYPE.getValue());
			list = getCompanyParameters(companyTypeList, authenticationType, isHaveSla, dtidType);
		} catch (DataAccessException e) {
			LOGGER.error("getCompanyList()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getCompanyList()", "SessionContext sessionContext:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		long endQueryCompanyTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> load", "Service getCompanyList:" + (endQueryCompanyTime - startQueryCompanyTime));
		return list;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#getCompanyParameters(java.util.List, java.lang.String, java.lang.Boolean, java.lang.String)
	 */
	@Override
	public List<Parameter> getCompanyParameters(List<String> companyTypeList, String authenticationType, Boolean isHaveSla, String dtidType) throws ServiceException {
		List<Parameter> list = null;
		try {
			//調用DAO層的方法獲取下拉列表
			list = companyDAO.getCompanyList(companyTypeList, authenticationType, isHaveSla, dtidType);
		} catch (DataAccessException e) {
			LOGGER.error("getCompanyParameters()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getCompanyParameters()", "SessionContext sessionContext:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return list;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#saveData(com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO, java.lang.String, java.lang.String)
	 */
	@Override
	public void saveData(CompanyDTO companyDTO, String userId, String userName) throws ServiceException {
		try {
			BimCompany company = new BimCompany();
			BimCompanyTypeId companyTypeId = null;
			BimCompanyType companyType = null;
			//生成公司編號
			String companyId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_COMPANY);
			//依次將要保存的信息設置到DMO
			company.setCompanyId(companyId);
			company.setCompanyCode(companyDTO.getCompanyCode());
			company.setShortName(companyDTO.getShortName());
			company.setUnityNumber(companyDTO.getUnityNumber());
			company.setInvoiceHeader(companyDTO.getInvoiceHeader());
			company.setLeader(companyDTO.getLeader());
			company.setTel(companyDTO.getTel());
			company.setFax(companyDTO.getFax());
			company.setApplyDate(companyDTO.getApplyDate());
			company.setPayDate(companyDTO.getPayDate());
			company.setContact(companyDTO.getContact());
			company.setContactTel(companyDTO.getContactTel());
			company.setContactEmail(companyDTO.getContactEmail());
			company.setCompanyEmail(companyDTO.getCompanyEmail());
			company.setCustomerCode(companyDTO.getCustomerCode());
			company.setDtidType(companyDTO.getDtidType());
			company.setAuthenticationType(companyDTO.getAuthenticationType());
			company.setAddressLocation(companyDTO.getAddressLocation());
			company.setAddress(companyDTO.getAddress());
			company.setInvoiceAddressLocation(companyDTO.getInvoiceAddressLocation());
			company.setInvoiceAddress(companyDTO.getInvoiceAddress());
			company.setRemark(companyDTO.getRemark());
			company.setCreatedById(userId);
			company.setCreatedByName(userName);
			company.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
			company.setUpdatedById(userId);
			company.setUpdatedByName(userName);
			company.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
			company.setDeleted(IAtomsConstants.NO);
			company.setIsNotifyAo(companyDTO.getIsNotifyAo());
			this.companyDAO.getDaoSupport().save(company);
			//獲取公司類型
			String types = companyDTO.getCompanyType();
			//若存在公司類型
			if (StringUtils.hasText(types)) {
				//按“，”進行拆分
				String[] str = types.split(IAtomsConstants.MARK_SEPARATOR);
				for (String s : str) {
					//創建公司類型ID對象
					companyTypeId = new BimCompanyTypeId();
					//設置公司編號、公司類型編號
					companyTypeId.setCompanyId(companyId);
					companyTypeId.setCompanyType(s);
					//創建公司類型對象
					companyType = new BimCompanyType();
					companyType.setId(companyTypeId);
					//保存公司類型
					this.companyTypeDAO.getDaoSupport().save(companyType);
					this.companyTypeDAO.getDaoSupport().flush();
				}
			}
			this.companyDAO.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("saveData()", "SessionContext sessionContext:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#isDtidType(cafe.core.context.MultiParameterInquiryContext)
	 */
	public boolean isDtidType(MultiParameterInquiryContext inquiryContext)throws ServiceException {
		try {
			//公司編號
			String companyId = (String) inquiryContext.getParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue());
			if (StringUtils.hasText(companyId)) {
				//獲取客戶的DTID生成方式
				CompanyDTO companyDTO = this.companyDAO.getCompanyDTOByCompanyId(companyId);
				//若存在公司DTO對該公司的DTID方式進行判斷，若不存在則返回false
				if (companyDTO != null) {
					//客戶DTID方式
					String dtidType = companyDTO.getDtidType();
					if (StringUtils.hasText(dtidType)) {
						//判斷該客戶DTID生成方式是否同TID
						if (IAtomsConstants.PARAM_IATOMS_DTID_TYPE_SAME.equals(dtidType)) {
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("isDtidType()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("isDtidType()", "SessionContext sessionContext:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return false;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#isAuthenticationTypeEqualsIAtoms(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public boolean isAuthenticationTypeEqualsIAtoms(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		boolean result = false;
		try {
			// 公司编号
			String companyId = (String) inquiryContext.getParameter(AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue());
			if(companyId != null){
				//根据公司编号得到登入验证方式
				BimCompany company = this.companyDAO.findByPrimaryKey(BimCompany.class, companyId);
				//若登入驗證方式為iAtoms驗證返回true
				if(IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE.equals(company.getAuthenticationType())){
					result = true;
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("isAuthenticationTypeEqualsIAtoms()", "DataAccess Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("isAuthenticationTypeEqualsIAtoms()", "Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return result;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICompanyService#getCompanyByCompanyId(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public CompanyDTO getCompanyByCompanyId( MultiParameterInquiryContext parameterInquiryContext) throws ServiceException {
		CompanyDTO companyDTO = null;
		try {
			//獲取公司code
			String companyId = (String) parameterInquiryContext.getParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue());
			//若存在公司code
			if (StringUtils.hasText(companyId)) {
				//根據公司code獲取該公司的信息
				BimCompany bimCompany = this.companyDAO.findByPrimaryKey(BimCompany.class, companyId);
				Transformer transformer = new SimpleDtoDmoTransformer();
				companyDTO = (CompanyDTO) transformer.transform(bimCompany, new CompanyDTO());
				if (companyDTO != null) {
					return companyDTO;
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("getCompanyByCompanyCode()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE);
		} catch (Exception e) {
			LOGGER.error("getCompanyByCompanyCode()", "SessionContext sessionContext:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED); 
		}
		return companyDTO;
	}
	
	/**
	 * @return the companyDAO
	 */
	public ICompanyDAO getCompanyDAO() {
		return companyDAO;
	}

	/**
	 * @param companyDAO the companyDAO to set
	 */
	public void setCompanyDAO(ICompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	/**
	 * @return the companyTypeDAO
	 */
	public ICompanyTypeDAO getCompanyTypeDAO() {
		return companyTypeDAO;
	}

	/**
	 * @param companyTypeDAO the companyTypeDAO to set
	 */
	public void setCompanyTypeDAO(ICompanyTypeDAO companyTypeDAO) {
		this.companyTypeDAO = companyTypeDAO;
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