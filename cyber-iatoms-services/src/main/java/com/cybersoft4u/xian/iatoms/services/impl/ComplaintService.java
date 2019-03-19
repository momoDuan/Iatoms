package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ComplaintDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmComplaintCaseFileDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ComplaintFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.services.IComplaintService;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmComplaintCaseFileDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmComplaintDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompany;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmComplaint;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmComplaintCaseFile;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Message;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.config.SystemConfigManager;
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

/**
 * Purpose: 客訴管理Service 
 * @author	nicklin
 * @since	JDK 1.7
 * @date	2018/03/06
 * @MaintenancePersonnel nicklin
 */

public class ComplaintService extends AtomicService implements IComplaintService {

	/**
	 * 系统log物件  
	 */
	private static CafeLog logger = CafeLogFactory.getLog(GenericConfigManager.DAO, ComplaintService.class);
	
	/**
	 * 客訴管理DAO
	 */
	private ISrmComplaintDAO srmComplaintDAO;

	/**
	 * 公司基本訊息維護DAO
	 */
	private ICompanyDAO companyDAO;
	
	/**
	 * 客訴管理附件檔DAO
	 */
	private ISrmComplaintCaseFileDAO srmComplaintCaseFileDAO;
	
	/**
	 * Constructor:無參數建構子
	 */
	public ComplaintService() {
		super();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IComplaintService#init(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext init(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		ComplaintFormDTO complaintFormDTO = null;
		try {
			complaintFormDTO = (ComplaintFormDTO) sessionContext.getRequestParameter();
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(complaintFormDTO);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".init():" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IComplaintService#query(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext query(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		Message msg = null;
		try {
			ComplaintFormDTO complaintFormDTO = (ComplaintFormDTO) sessionContext.getRequestParameter();
			if (sessionContext != null){
				//查詢條件
				String queryCustomerId = complaintFormDTO.getQueryCustomerId();
				String queryMerchantCode = complaintFormDTO.getQueryMerchant();
				String queryTid = complaintFormDTO.getQueryTid();
				String queryMerchantName = complaintFormDTO.getQueryMerchantName();
				String queryQuestionType = complaintFormDTO.getQueryQuestionType();
				String queryVendor = complaintFormDTO.getQueryVendor();
				String queryIsCustomer = complaintFormDTO.getQueryIsCustomer();
				String queryIsVendor = complaintFormDTO.getQueryIsVendor();
				String queryStartDate = complaintFormDTO.getQueryStartDate();
				String queryEndDate = complaintFormDTO.getQueryEndDate();
				Integer pageIndex = complaintFormDTO.getPage();
				Integer pageSize = complaintFormDTO.getRows();
				String orderby = complaintFormDTO.getOrder();
				String sort = complaintFormDTO.getSort();
				Integer count = null;
				count = srmComplaintDAO.count(queryCustomerId, queryMerchantCode, queryTid, queryMerchantName,
						queryQuestionType, queryVendor, queryIsCustomer, queryIsVendor, queryStartDate, queryEndDate);
				if (count.intValue() > 0) {
					//查詢結果List
					List<ComplaintDTO> complaintDTOs = srmComplaintDAO.listBy(queryCustomerId, queryMerchantCode, queryTid, queryMerchantName,
							queryQuestionType, queryVendor, queryIsCustomer, queryIsVendor, queryStartDate, queryEndDate, pageSize, pageIndex, sort, orderby);
					complaintDTOs = this.convertYesOrNo(complaintDTOs);
					complaintFormDTO.getPageNavigation().setRowCount(count.intValue());
					complaintFormDTO.setList(complaintDTOs);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				}
				sessionContext.setReturnMessage(msg);
				sessionContext.setResponseResult(complaintFormDTO);
			}
		} catch (DataAccessException e) {
			logger.error(this.getClass().getName() + ".query() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".query() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IComplaintService#initEdit(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext initEdit(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		Message msg = null;
		try {
			ComplaintFormDTO complaintFormDTO = (ComplaintFormDTO) sessionContext.getRequestParameter();
			ComplaintDTO complaintDTO = new ComplaintDTO();
			List<ComplaintDTO> complaintDTOs = null;
			List<SrmComplaintCaseFileDTO> srmComplaintCaseFileDTOs = null;
			String caseId = complaintFormDTO.getCaseId();
			
			String fileName = UUID.randomUUID().toString();
			String size = SystemConfigManager.getProperty(IAtomsConstants.SETTING, IAtomsConstants.UPLOAD_FILE_SIZE);
			if (!StringUtils.hasText(size)) {
				size = IAtomsConstants.PARAM_UPLOAD_FILE_SIZE;
			}
			int rate = Integer.valueOf(IAtomsConstants.PARAM_UPLOAD_CONVERSION_RATE);
			size = String.valueOf(Integer.valueOf(size) * rate);
			complaintFormDTO.setUploadFileSize(size);
			complaintFormDTO.setFileName(fileName);
			if (caseId != null) {
				SrmComplaint srmComplaint = srmComplaintDAO.findByPrimaryKey(SrmComplaint.class, caseId);
				//獲取更完整的complaintDTO物件
				if (srmComplaint != null) {
					complaintDTOs = this.srmComplaintDAO.getCaseInfoById(caseId);
					srmComplaintCaseFileDTOs = this.srmComplaintCaseFileDAO.listByCaseId(null, caseId);
					if (!CollectionUtils.isEmpty(complaintDTOs)) {
						complaintDTO = complaintDTOs.get(0);
						complaintFormDTO.setComplaintDTO(complaintDTO);
						if(!CollectionUtils.isEmpty(srmComplaintCaseFileDTOs)) {
							complaintFormDTO.setSrmComplaintCaseFileDTOs(srmComplaintCaseFileDTOs);
						}
					}
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE);
				}
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(complaintFormDTO);
		} catch (DataAccessException e) {
			logger.error(this.getClass().getName() + ".initEdit() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".initEdit() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IComplaintService#save(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext save(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		Message msg = null;
		String caseId = null;
		try {
			ComplaintFormDTO complaintFormDTO = (ComplaintFormDTO) sessionContext.getRequestParameter();
			ComplaintDTO complaintDTO = complaintFormDTO.getComplaintDTO();
			caseId = complaintDTO.getCaseId();
			//獲取登錄信息
			LogonUser logonUser = complaintFormDTO.getLogonUser();
			//當前登入者的Id
			String userId = null; 
			//當前登入者的Name
			String userName = null;
			if (logonUser != null) {
				userId = logonUser.getId();
				userName = logonUser.getName();
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.LIMITED_LOGON_ID);
				sessionContext.setReturnMessage(msg);
				return sessionContext;
			}
			Transformer transformer = new SimpleDtoDmoTransformer();
			SrmComplaint srmComplaint = null;
			String newCaseId = null;
			//無客訴編號為新增，反之為修改
			if (!StringUtils.hasText(caseId)) {
				String customerId = complaintDTO.getCustomerId();
				BimCompany customer = (BimCompany) this.companyDAO.findByPrimaryKey(BimCompany.class, customerId);
				String customerCode = customer.getCustomerCode();
				if (StringUtils.hasText(customerCode)) {
					//String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD).substring(2, 6);
					//流水號
					String happenDate = DateTimeUtils.toString(complaintDTO.getComplaintDate(), DateTimeUtils.DT_FMT_YYYYMMDD).substring(2, 6);
					long id = this.getSequenceNumberControlDAO().getSeqNo(IAtomsConstants.IATOMS_TB_NAME_SRM_COMPLAINT_CASE, happenDate);
					if (id == 0) {
						id++;
					}
					String swiftNumber = StringUtils.toFixString(3, id);
					newCaseId = IAtomsConstants.TICKET_TYPE_COMPLAINT + IAtomsConstants.MARK_MIDDLE_LINE + happenDate + swiftNumber;
					
					srmComplaint = (SrmComplaint) transformer.transform(complaintDTO, new SrmComplaint());
					srmComplaint.setCaseId(newCaseId);
					//設置創建者信息
					srmComplaint.setCreatedById(userId);
					srmComplaint.setCreatedByName(userName);
					srmComplaint.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					//設置更新信息
					srmComplaint.setUpdatedById(userId);
					srmComplaint.setUpdatedByName(userName);
					srmComplaint.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					//設置刪除標記
					srmComplaint.setDeleted(IAtomsConstants.NO);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()});
				}
			} else {
				//獲取刪除文件的ID
				String deleteFileId = complaintFormDTO.getDeleteFileId();
				//如果存在需要刪除的文件，進行刪除
				if (StringUtils.hasText(deleteFileId)) {
					//將ID字符串按照“,”分割開來
					String[] fileIds = deleteFileId.split(IAtomsConstants.MARK_SEPARATOR);
					SrmComplaintCaseFile srmComplaintCaseFile = null;
					File filePath = null;
					for (String fileId : fileIds) {
						//根據ID獲取對應的文件信息
						srmComplaintCaseFile = this.srmComplaintCaseFileDAO.findByPrimaryKey(SrmComplaintCaseFile.class, fileId);
						//如果文件存在，刪除
						if (srmComplaintCaseFile != null) {
							//刪除服務上的文件
							this.srmComplaintCaseFileDAO.getDaoSupport().delete(srmComplaintCaseFile);
							FileUtils.removeFile(srmComplaintCaseFile.getFilePath());
							filePath = new File(srmComplaintCaseFile.getFilePath());
							File[] fa = filePath.listFiles();
							if (fa != null && fa.length == 0) {
								FileUtils.removeFile(srmComplaintCaseFile.getFilePath());
							}
						}
					}
				}
				srmComplaint = this.srmComplaintDAO.findByPrimaryKey(SrmComplaint.class, caseId);
				
				//取得資料庫中的創建信息
				complaintDTO.setCreatedById(srmComplaint.getCreatedById());
				complaintDTO.setCreatedByName(srmComplaint.getCreatedByName());
				complaintDTO.setCreatedDate(srmComplaint.getCreatedDate());
				//設置更新信息
				complaintDTO.setUpdatedById(userId);
				complaintDTO.setUpdatedByName(userName);
				complaintDTO.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				srmComplaint = (SrmComplaint) transformer.transform(complaintDTO, srmComplaint);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()});
			}
			this.srmComplaintDAO.getDaoSupport().saveOrUpdate(srmComplaint);
			
			/*Bug 第一次上傳檔案會上傳失敗
			等待saveFile method 執行完即可*/
			logger.debug("########## sleep one second ##########");
			Thread.sleep(1000);
			//保存上傳的文件
			String fileName = complaintFormDTO.getFileName();
			String tempPath = getSaveTempFilePath(true, fileName, null);
			File tempFilePath = new File(tempPath);
			File[] fa = tempFilePath.listFiles();
			//如果存在需要上傳的文件，進行上傳
			if (fa != null) {
				//獲取保存文件的正式路徑
				String uploadPath = getSaveTempFilePath(false, null, srmComplaint.getCaseId());
				String name = IAtomsConstants.MARK_EMPTY_STRING;
				//循環將文件保存至正式目錄
				for (File file : fa) {
					//獲取文件名稱
					name = file.getName();
					//複製文件至正式路徑
					FileUtils.copyFile(tempPath + name, uploadPath, name);
				}
				FileUtils.removeFile(tempPath);
				SrmComplaintCaseFile srmComplaintCaseFile = null;
				String fileNewName;
				String tempNewName;
				int lastFlagPosition = -1;
				//保存文件名稱及路徑至數據庫
				for (File file : fa) {
					fileNewName = file.getName();
					lastFlagPosition = fileNewName.lastIndexOf(IAtomsConstants.MARK_UNDER_LINE);
					if (lastFlagPosition > 0) {
						fileNewName = fileNewName.substring(0,lastFlagPosition);
						tempNewName = file.getName();
						lastFlagPosition = tempNewName.lastIndexOf(IAtomsConstants.MARK_NO);
						fileNewName += tempNewName.substring(lastFlagPosition);
					}
					srmComplaintCaseFile = new SrmComplaintCaseFile();
					srmComplaintCaseFile.setFileId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_SRM_COMPLAINT_CASE_FILE));
					srmComplaintCaseFile.setCaseId(srmComplaint.getCaseId());
					srmComplaintCaseFile.setFileName(fileNewName);
					srmComplaintCaseFile.setFilePath(uploadPath + file.getName());
					srmComplaintCaseFile.setCreatedById(logonUser.getId());
					srmComplaintCaseFile.setCreatedByName(logonUser.getName());
					srmComplaintCaseFile.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					this.srmComplaintCaseFileDAO.getDaoSupport().saveOrUpdate(srmComplaintCaseFile);
				}
			}
			
			sessionContext.setResponseResult(complaintDTO);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			logger.error(this.getClass().getName() + ".save() DataAccess Exception:" + e, e);
			if (caseId == null) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()});
			}
			throw new ServiceException(msg);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".save() Exception:" + e, e);
			if (caseId == null) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()});
			}
			throw new ServiceException(msg);
		}
		return sessionContext;
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IComplaintService#delete(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext delete(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		Message msg = null;
		try {
			ComplaintFormDTO complaintFormDTO = (ComplaintFormDTO) sessionContext.getRequestParameter();
			String caseId = complaintFormDTO.getCaseId();
			//獲取登錄信息
			LogonUser logonUser = complaintFormDTO.getLogonUser();
			//當前登入者的Id
			String userId = null; 
			//當前登入者的Name
			String userName = null;
			if (logonUser != null) {
				userId = logonUser.getId();
				userName = logonUser.getName();
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.LIMITED_LOGON_ID);
				sessionContext.setReturnMessage(msg);
				return sessionContext;
			}
			if (StringUtils.hasText(caseId)) {
				SrmComplaint srmComplaint = this.srmComplaintDAO.findByPrimaryKey(SrmComplaint.class, caseId);
				if (srmComplaint != null) {
					//設置刪除標誌
					srmComplaint.setDeleted(IAtomsConstants.YES);
					//設置創建者信息
					srmComplaint.setCreatedById(userId);
					srmComplaint.setCreatedByName(userName);
					srmComplaint.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					this.srmComplaintDAO.getDaoSupport().update(srmComplaint);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
				} else{
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
				}
				sessionContext.setReturnMessage(msg);
				sessionContext.setResponseResult(complaintFormDTO);
			} else {
				//不存在客訴編號時，刪除失敗
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
			}
		} catch (DataAccessException e) {
			logger.error(this.getClass().getName() + "delete() DataAccess Exception:" + e, e);
			throw new ServiceException(CoreMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + "delete() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	
	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IComplaintService#saveFile(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext saveFile(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		try {
			ComplaintFormDTO complaintFormDTO = (ComplaintFormDTO) sessionContext.getRequestParameter();
			Map<String, MultipartFile> fileMap = complaintFormDTO.getFileMap();
			String fileName = complaintFormDTO.getFileName();
			String path = getSaveTempFilePath(true, fileName, null);
			if (fileMap.size()> 0){
				MultipartFile uploadFiled = null;
				File filePath = new File(path);
				if (!filePath.exists() || !filePath.isDirectory()) {
					filePath.mkdirs();
				}
				uploadFiled = fileMap.entrySet().iterator().next().getValue();
				String originalFilename = uploadFiled.getOriginalFilename();
				int lastPoint = originalFilename.lastIndexOf('.');
				String fileNewName;
				if (lastPoint > 0) {
					String currentTime = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(),  DateTimeUtils.DT_FMT_YYYYMMDDHHMMSSSSS);
					fileNewName = originalFilename.substring(0, lastPoint) + IAtomsConstants.MARK_UNDER_LINE + currentTime + originalFilename.substring(lastPoint);
				} else {
					fileNewName = originalFilename;
				}
				FileUtils.upload(path, uploadFiled, fileNewName);
				Map map = new HashMap();
				map.put("success",true);
				map.put("newUuid", fileNewName);
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName() + "saveFile() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IComplaintService#export(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext export(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		Message msg = null;
		try {
			ComplaintFormDTO complaintFormDTO = null;
			List<ComplaintDTO> complaintDTOs = null;
			if (sessionContext != null) {
				complaintFormDTO = (ComplaintFormDTO) sessionContext.getRequestParameter();
				String queryCustomerId = complaintFormDTO.getQueryCustomerId();
				String queryMerchantCode = complaintFormDTO.getQueryMerchant();
				String queryTid = complaintFormDTO.getQueryTid();
				String queryMerchantName = complaintFormDTO.getQueryMerchantName();
				String queryQuestionType = complaintFormDTO.getQueryQuestionType();
				String queryVendor = complaintFormDTO.getQueryVendor();
				String queryIsCustomer = complaintFormDTO.getQueryIsCustomer();
				String queryIsVendor = complaintFormDTO.getQueryIsVendor();
				String queryStartDate = complaintFormDTO.getQueryStartDate();
				String queryEndDate = complaintFormDTO.getQueryEndDate();
				logger.debug("export()", "queryCustomerId : ", queryCustomerId);
				logger.debug("export()", "queryMerchantCode : ", queryMerchantCode);
				logger.debug("export()", "queryTid : ", queryTid);
				logger.debug("export()", "queryMerchantName : ", queryMerchantName);
				logger.debug("export()", "queryQuestionType : ", queryQuestionType);
				logger.debug("export()", "queryVendor : ", queryVendor);
				logger.debug("export()", "queryIsCustomer : ", queryIsCustomer);
				logger.debug("export()", "queryIsVendor : ", queryIsVendor);
				logger.debug("export()", "queryStartDate : ", queryStartDate);
				logger.debug("export()", "queryEndDate : ", queryEndDate);
				complaintDTOs = this.srmComplaintDAO.listBy(queryCustomerId, queryMerchantCode, queryTid,
						queryMerchantName, queryQuestionType, queryVendor, queryIsCustomer, queryIsVendor,
						queryStartDate, queryEndDate, Integer.valueOf(-1), Integer.valueOf(-1), null, null);
				complaintDTOs = this.convertYesOrNo(complaintDTOs);
				complaintFormDTO.setList(complaintDTOs);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS, new String[]{this.getMyName()});
				sessionContext.setReturnMessage(msg);
				sessionContext.setResponseResult(complaintFormDTO);
			}
		} catch (DataAccessException e) {
			logger.error(this.getClass().getName() + ".export() DataAccess Exception:" + e, e);
			msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.EXPORT_REPORT_FAILURE, new String[]{this.getMyName()});
			throw new ServiceException(msg);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".addMemo() Exception:" + e, e);
			msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()});
			throw new ServiceException(msg);
		}
		return sessionContext;
	}

	/**
	 * Purpose:創建文件路徑
	 * @author nicklin
	 * @param  isTemp ：是否為臨時路徑
	 * @return path ：返回路徑
	 */
	public String getSaveTempFilePath(boolean isTemp, String fileName , String caseId) {
		String path = "";
		try {
			String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
			if (!StringUtils.hasText(fileName)) {
				fileName = UUID.randomUUID().toString();
			}
			StringBuilder builder = new StringBuilder();
			if (isTemp) {
				builder.append(SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH));
				builder.append(File.separator).append(yearMonthDay).append(File.separator).append(IAtomsConstants.UC_NO_SRM_05100);
				builder.append(File.separator).append(fileName).append(File.separator);
			} else {
				builder.append(SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_UPLOAD_PATH));
				builder.append(File.separator).append(IAtomsConstants.UC_NO_SRM_05100);
				builder.append(File.separator).append(caseId).append(File.separator);
			}
			path = builder.toString();
		} catch (Exception e) {
			logger.error(this.getClass().getName() + "getSaveTempFilePath() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return path;
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IComplaintService#getComplaintFilePath(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public String getComplaintFilePath(MultiParameterInquiryContext param)
			throws ServiceException {
		// TODO Auto-generated method stub
		String filePath = IAtomsConstants.MARK_EMPTY_STRING;
		try {
			String fileId = (String) param.getParameter(SrmComplaintCaseFileDTO.ATTRIBUTE.FILE_ID.getValue());
			List<SrmComplaintCaseFileDTO> srmComplaintCaseFileDTOs = null;
			srmComplaintCaseFileDTOs = this.srmComplaintCaseFileDAO.listByCaseId(fileId, null);
			if (!CollectionUtils.isEmpty(srmComplaintCaseFileDTOs)) {
				filePath = srmComplaintCaseFileDTOs.get(0).getFilePath();
			}
		} catch (DataAccessException e) {
			logger.error(this.getClass().getName() + ".getComplaintFilePath() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".getComplaintFilePath() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return filePath;
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IComplaintService#getComplaintFilePath(com.cybersoft4u.xian.iatoms.common.bean.formDTO.ComplaintFormDTO)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext getComplaintFilePath(ComplaintFormDTO command)
			throws ServiceException {
		// TODO Auto-generated method stub
		SessionContext sessionContext = new SessionContext();
		try {
			String fileId = command.getFileId();
			SrmComplaintCaseFile srmComplaintCaseFile = this.srmComplaintCaseFileDAO.findByPrimaryKey(SrmComplaintCaseFile.class, fileId);
			command.setFileName(srmComplaintCaseFile.getFileName());
			command.setFilePath(srmComplaintCaseFile.getFilePath());
			sessionContext.setResponseResult(command);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".getComplaintFilePath() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	/**
	 * Purpose:轉換Y/N
	 * @author nicklin
	 * @param  complaintDTOs
	 * @return List<ComplaintDTO>
	 */
	public List<ComplaintDTO> convertYesOrNo(List<ComplaintDTO> complaintDTOs) throws ServiceException {
		try {
			for (ComplaintDTO complaintDTO : complaintDTOs) {
				if (StringUtils.hasText(complaintDTO.getIsVip())) {
					if (complaintDTO.getIsVip().equals(IAtomsConstants.YES)) {
						complaintDTO.setIsVip("是");
					}
					if (complaintDTO.getIsVip().equals(IAtomsConstants.NO)) {
						complaintDTO.setIsVip("否");
					}
				}
				if (StringUtils.hasText(complaintDTO.getIsCustomer())) {
					if (complaintDTO.getIsCustomer().equals(IAtomsConstants.YES)) {
						complaintDTO.setIsCustomer("是");
					}
					if (complaintDTO.getIsCustomer().equals(IAtomsConstants.NO)) {
						complaintDTO.setIsCustomer("否");
					}
				}
				if (StringUtils.hasText(complaintDTO.getIsVendor())) {
					if (complaintDTO.getIsVendor().equals(IAtomsConstants.YES)) {
						complaintDTO.setIsVendor("是");
					}
					if (complaintDTO.getIsVendor().equals(IAtomsConstants.NO)) {
						complaintDTO.setIsVendor("否");
					}
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".getComplaintFilePath() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return complaintDTOs;
	}
	
	/**
	 * @return the srmComplaintDAO
	 */
	public ISrmComplaintDAO getSrmComplaintDAO() {
		return srmComplaintDAO;
	}

	/**
	 * @param srmComplaintDAO the srmComplaintDAO to set
	 */
	public void setSrmComplaintDAO(ISrmComplaintDAO srmComplaintDAO) {
		this.srmComplaintDAO = srmComplaintDAO;
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
	 * @return the srmComplaintCaseFileDAO
	 */
	public ISrmComplaintCaseFileDAO getSrmComplaintCaseFileDAO() {
		return srmComplaintCaseFileDAO;
	}

	/**
	 * @param srmComplaintCaseFileDAO the srmComplaintCaseFileDAO to set
	 */
	public void setSrmComplaintCaseFileDAO(
			ISrmComplaintCaseFileDAO srmComplaintCaseFileDAO) {
		this.srmComplaintCaseFileDAO = srmComplaintCaseFileDAO;
	}
}
