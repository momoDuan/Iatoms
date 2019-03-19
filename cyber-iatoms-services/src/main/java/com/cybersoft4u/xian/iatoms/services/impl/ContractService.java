package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAttachedFileDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.CopyPropertiesUtils;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.services.IContractService;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractAssetDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractAttachedFileDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractVendorDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContract;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractAsset;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractAssetId;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractAttachedFile;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractType;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractTypeId;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractVendor;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractVendorId;

/**
 * Purpose:合约维护Service 
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/5/17
 * @MaintenancePersonnel CarrieDuan
 */
public class ContractService extends AtomicService implements IContractService {
	
	/**
	 * 系统日志记录文件
	 */
	private static final CafeLog LOG = CafeLogFactory.getLog(GenericConfigManager.SERVICE, ContractService.class);
	
	/**
	 * 合约DAO
	 */
	private IContractDAO contractDAO;
	
	/**
	 * 設備DAO
	 */
	private IContractAssetDAO contractAssetDAO;
	
	/**
	 * 公司DAO
	 */
	private ICompanyDAO companyDAO;
	
	/**
	 * 合約類型DAO
	 */
	private IContractTypeDAO contractTypeDAO;
	
	/**
	 * 合約廠商編號
	 */
	private IContractVendorDAO contractVendorDAO;
	
	/**
	 * 合約文件DAO
	 */
	private IContractAttachedFileDAO contractAttachedFileDAO;
	
	/**
	 * Constructor:无参构造子
	 */
	public ContractService() {
		
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISetSlaLevelService#init(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SessionContext init(SessionContext sessionContext)
			throws ServiceException {
		// 页面显示信息
		Message msg = null;
		try {
			ContractManageFormDTO formDTO =  (ContractManageFormDTO) sessionContext.getRequestParameter();
			sessionContext.setResponseResult(formDTO);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOG.error("init(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#query(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		Message msg = new Message();
		try {
			ContractManageFormDTO formDTO = (ContractManageFormDTO) sessionContext.getRequestParameter();
			//查詢總條數
			Integer count = this.contractDAO.count(formDTO.getQueryCustomerId(), formDTO.getQueryManuFacturer());
			if (count.equals(Integer.valueOf(0))) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			} else {
				//查询当前页
				List<BimContractDTO> contractManageDTOs = this.contractDAO.listBy(formDTO.getQueryCustomerId(), formDTO.getQueryManuFacturer(), null, 
															formDTO.getOrder(), formDTO.getSort(), formDTO.getPage().intValue(), formDTO.getRows().intValue());
				if (!CollectionUtils.isEmpty(contractManageDTOs)) {
					for (BimContractDTO contractManageDTO : contractManageDTOs) {
						if (IAtomsConstants.MARK_WAVE_LINE.equals(contractManageDTO.getWorkHour1())) {
							contractManageDTO.setWorkHour1(IAtomsConstants.MARK_EMPTY_STRING);
						}
						if (IAtomsConstants.MARK_WAVE_LINE.equals(contractManageDTO.getWorkHour2())) {
							contractManageDTO.setWorkHour2(IAtomsConstants.MARK_EMPTY_STRING);
						}
						if (IAtomsConstants.MARK_WAVE_LINE.equals(contractManageDTO.getContractDate())) {
							contractManageDTO.setContractDate(null);
						}
					}
				}
				formDTO.setList(contractManageDTOs);
				formDTO.getPageNavigation().setRowCount(count.intValue());
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOG.error("query() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}  catch (Exception e) {
			LOG.error("query(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.IAdmUserService#initEdit(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			ContractManageFormDTO contractManageFormDTO = (ContractManageFormDTO) sessionContext.getRequestParameter();
			String contractId = contractManageFormDTO.getContractId();
			BimContractDTO contractManageDTO = new BimContractDTO();
			List<BimContractAttachedFileDTO> contractAttachedFileDTOs = null;
			// 修改初始化
			if (StringUtils.hasText(contractId)) {
				//獲取合約信息
				List<BimContractDTO> contractManageDTOs = this.contractDAO.listBy(null, null, contractId, null, null, -1, -1);
				//獲取合約下的文件
				contractAttachedFileDTOs = this.contractAttachedFileDAO.getContractAttachedFileByContractId(contractId);
				//如果合約信息不為空，則轉換為DTO
				if (contractManageDTOs.get(0) != null) {
					contractManageDTO = contractManageDTOs.get(0);
					List<String> vendors = new ArrayList<String>();
					List<String> companyTypes = new ArrayList<String>();
					if (contractManageDTO.getCompanyIds() != null) {
						String[] company = contractManageDTO.getCompanyIds().split(IAtomsConstants.MARK_SEPARATOR);
						for (String vendor : company) {
							vendors.add(vendor);
						}
						contractManageDTO.setVendors(vendors);
					}
					if (contractManageDTO.getContractTypeId() != null) {
						String[] companyType = contractManageDTO.getContractTypeId().split(IAtomsConstants.MARK_SEPARATOR);
						for (String type : companyType) {
							companyTypes.add(type);
						}
						contractManageDTO.setContractTypes(companyTypes);
					}
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
					String fileName = UUID.randomUUID().toString();
					String size = SystemConfigManager.getProperty(IAtomsConstants.SETTING, IAtomsConstants.UPLOAD_FILE_SIZE);
					if (!StringUtils.hasText(size)) {
						size = IAtomsConstants.PARAM_UPLOAD_FILE_SIZE;
					}
					int rate = Integer.valueOf(IAtomsConstants.PARAM_UPLOAD_CONVERSION_RATE);
					size = String.valueOf(Integer.valueOf(size) * rate);
					contractManageFormDTO.setUploadFileSize(size);
					contractManageFormDTO.setFileName(fileName);
					contractManageFormDTO.setContractManageDTO(contractManageDTO);
					contractManageFormDTO.setContractAttachedFileDTOs(contractAttachedFileDTOs);
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE);
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE);
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(contractManageFormDTO);
		} catch (DataAccessException e) {
			LOG.error("initEdit() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error(".initEdit(SessionContext sessionContext):", e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#initAdd(cafe.core.context.SessionContext)
	 */
	public SessionContext initAdd(SessionContext sessionContext) throws ServiceException {
		try {
			ContractManageFormDTO contractManageFormDTO = (ContractManageFormDTO) sessionContext.getRequestParameter();
			String fileName = UUID.randomUUID().toString();
			String size = SystemConfigManager.getProperty(IAtomsConstants.SETTING, IAtomsConstants.UPLOAD_FILE_SIZE);
			if (!StringUtils.hasText(size)) {
				size = IAtomsConstants.PARAM_UPLOAD_FILE_SIZE;
			}
			int rate = Integer.valueOf(IAtomsConstants.PARAM_UPLOAD_CONVERSION_RATE);
			size = String.valueOf(Integer.valueOf(size) * rate);
			contractManageFormDTO.setUploadFileSize(size);
			contractManageFormDTO.setFileName(fileName);
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(contractManageFormDTO);
		} catch (DataAccessException e) {
			LOG.error("initEdit() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error("initEdit(SessionContext sessionContext):", e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#getContractAssetList(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SessionContext getContractAssetList(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			ContractManageFormDTO formDTO = (ContractManageFormDTO) sessionContext.getRequestParameter();
			//獲取合約ID
			String contractId = formDTO.getContractId();
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(0));
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, new ArrayList<BimContractAssetDTO>());
			//如果合約ID不為空，則根據合約id進行查詢，並將查詢結果放入map中
			if (StringUtils.hasText(contractId)){
				List<BimContractAssetDTO> contractAssetList = this.contractAssetDAO.listContractAssetByContractId(contractId);
				if (!CollectionUtils.isEmpty(contractAssetList)) {
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(contractAssetList.size()));
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, contractAssetList);
				} else {
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.DATA_NOT_FOUND));
				}
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.DATA_NOT_FOUND));
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOG.error("getContractAssetList() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error("getContractAssetList() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#save(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SessionContext save(SessionContext sessionContext)throws ServiceException {
		// 页面显示信息
		Message msg = null;
		//新增和修改的actionId
		String opActionId = null;
		//合約設備的集合
		List<BimContractAssetDTO> assetTypeDTOs = null;
		try {
			ContractManageFormDTO formDTO = (ContractManageFormDTO) sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			BimContractDTO contractManageDTO = formDTO.getContractManageDTO();
			String contractId = null;
			if (contractManageDTO != null) {
				contractId = contractManageDTO.getHiddenContractId();
			}
			//DTO与與DMO轉換工具
			Transformer transformer = new SimpleDtoDmoTransformer();
			opActionId = formDTO.getOpActionId();
			BimContract contract = null;
			//檢驗合約編號是否重複。如果重複，返回合約編號重複MSG
			Boolean isRepeat = this.contractDAO.isCheck(contractManageDTO.getContractCode(), contractId);
			if (isRepeat.booleanValue()) {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.CONTRACT_ID_REPEAT));
				return sessionContext;
			} 
			if (!StringUtils.hasText(contractId)) {
				// 新增
				contract = new BimContract();
				contract = (BimContract) transformer.transform(contractManageDTO, contract);
				contractId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_CONTRACT);
				contract.setDeleted(IAtomsConstants.NO);
				contract.setCreatedById(logonUser.getId());
				contract.setCreatedByName(logonUser.getName());
				contract.setCreatedDate(DateTimeUtils.getCurrentDate());
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
			} else {
				// 修改
				contract = this.contractDAO.findByPrimaryKey(BimContract.class, contractId);
				// 把要修改的值複製到contract中
				new CopyPropertiesUtils().copyProperties(contractManageDTO, contract, null);
				contract.setCancelDate(contractManageDTO.getCancelDate());
				contract.setStartDate(contractManageDTO.getStartDate());
				contract.setEndDate(contractManageDTO.getEndDate());
				contract.setContractPrice(contractManageDTO.getContractPrice());
				contract.setCustomerWarranty(contractManageDTO.getCustomerWarranty());
				contract.setFactoryWarranty(contractManageDTO.getFactoryWarranty());
				// 先刪除合約下的設備和廠商以及合約類型
				this.contractAssetDAO.deleteByContractId(contractId);
				this.contractTypeDAO.deleteByContractId(contractId);
				this.contractVendorDAO.deleteByContractId(contractId);
				this.contractAssetDAO.getDaoSupport().flush();
				this.contractTypeDAO.getDaoSupport().flush();
				this.contractVendorDAO.getDaoSupport().flush();
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()});
				//獲取刪除文件的ID
				String deleteFileId = formDTO.getDeleteFileId();
				//如果存在需要刪除的文件，進行刪除
				if (StringUtils.hasText(deleteFileId)) {
					//將ID字符串按照“,”分割開來
					String[] fileIds = deleteFileId.split(IAtomsConstants.MARK_SEPARATOR);
					BimContractAttachedFile contractAttachedFile = null;
					File filePath = null;
					for (String fileId : fileIds) {
						//根據ID獲取對應的文件信息
						contractAttachedFile = this.contractAttachedFileDAO.findByPrimaryKey(BimContractAttachedFile.class, fileId);
						//如果文件存在，刪除
						if (contractAttachedFile != null) {
							this.contractAttachedFileDAO.delete(contractAttachedFile);
							//刪除服務上的文件
							FileUtils.removeFile(contractAttachedFile.getAttachedFile());
							filePath = new File(contractAttachedFile.getAttachedFile());
							File[] fa = filePath.listFiles();
							if (fa != null && fa.length == 0) {
								FileUtils.removeFile(contractAttachedFile.getAttachedFile());
							}
						}
					}
				}
			}
			contract.setContractId(contractId);
			contract.setUpdatedById(logonUser.getId());
			contract.setUpdatedByName(logonUser.getName());
			contract.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
			//保存使用者信息
			this.contractDAO.getDaoSupport().saveOrUpdate(contract);
			this.contractDAO.getDaoSupport().flush();			
			BimContractVendor bimContractVendor = null;
			//獲取新選擇的供應商ID
			String companyIds = contractManageDTO.getCompanyIds();
			String[] vendors;
			//將供應商ID按照，分割開來,添加合約廠商信息
			if (StringUtils.hasText(companyIds)) {
				vendors = companyIds.split(IAtomsConstants.MARK_SEPARATOR);
				BimContractVendorId bimContractVendorId;
				for (String vendor : vendors) {
					bimContractVendorId = new BimContractVendorId(contractId, vendor);
					bimContractVendor = new BimContractVendor(bimContractVendorId, null);
					this.contractVendorDAO.getDaoSupport().save(bimContractVendor);
				}
				this.contractVendorDAO.getDaoSupport().flush();
			}
			//添加合約類型信息
			//用於保存拆分之後的合約類型
			String[] contractTypes;
			if (StringUtils.hasText(contractManageDTO.getContractTypeId())) {
				contractTypes = contractManageDTO.getContractTypeId().split(IAtomsConstants.MARK_SEPARATOR);
				//添加合約類型
				BimContractType bimContractType = null;
				for (String contractType : contractTypes) {
					bimContractType = new BimContractType(new BimContractTypeId(contractId, contractType));
					this.contractTypeDAO.getDaoSupport().save(bimContractType);
				}
				this.contractTypeDAO.getDaoSupport().flush();
			}
			//獲取合約設備
			assetTypeDTOs = contractManageDTO.getAssetTypeDTOs();
			BimContractAsset contractAsset = null;
			for (BimContractAssetDTO contractAssetDTO : assetTypeDTOs) {
				contractAsset = (BimContractAsset) transformer.transform(contractAssetDTO, new BimContractAsset());
				contractAsset.setId(new BimContractAssetId(contractId, contractAssetDTO.getAssetTypeId()));
				this.contractAssetDAO.getDaoSupport().saveOrUpdate(contractAsset);
			}
			this.contractAssetDAO.getDaoSupport().flush();
			//添加上傳的文件
			//獲取文件的臨時保存路徑
			String fileName = formDTO.getFileName();
			String tempPath = getSaveTempFilePath(true, fileName, null);
			File tempFilePath = new File(tempPath);
			File[] fa = tempFilePath.listFiles();
			//如果存在需要上傳的文件，進行上傳
			if (fa != null) {
				//獲取保存文件的正式路徑
				String uploadPath = getSaveTempFilePath(false, null, contractId);
				String name = "";
				//循環將文件保存至正式目錄
				for (File file : fa) {
					//獲取文件名稱
					name = file.getName();
					//複製文件至正式路徑
					FileUtils.copyFile(tempPath.concat(name), uploadPath, name);
				}
				FileUtils.removeFile(tempPath);
				BimContractAttachedFile contractAttachedFile = null;
				String fileNewName;
				String tempNewName;
				int lastFlagPosition = -1;
				//保存文件名稱及路徑至數據庫
				for (File file : fa) {
					fileNewName = file.getName();
					lastFlagPosition = fileNewName.lastIndexOf('_');
					if (lastFlagPosition > 0) {
						fileNewName = fileNewName.substring(0, lastFlagPosition);
						tempNewName = file.getName();
						lastFlagPosition = tempNewName.lastIndexOf('.');
						fileNewName += tempNewName.substring(lastFlagPosition);
					}
					
					contractAttachedFile = new BimContractAttachedFile();
					contractAttachedFile.setAttachedFileId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_CONTRACT_ATTACHED_FILE));
					contractAttachedFile.setFileName(fileNewName);
					contractAttachedFile.setAttachedFile(uploadPath.concat(file.getName()));
					contractAttachedFile.setContractId(contractId);
					this.contractAttachedFileDAO.getDaoSupport().save(contractAttachedFile);
				}
			}
			sessionContext.setReturnMessage(msg);
		}catch (DataAccessException e) {
			LOG.error("save() DataAccess Exception:", e);
			if (IAtomsConstants.ACTION_INIT_ADD.equals(opActionId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()});
			} else if (IAtomsConstants.ACTION_INIT_ADD.equals(opActionId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPLOAD_FAILURE, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()});
			}
			throw new ServiceException(msg);
		} catch (Exception e) {
			LOG.error("save(SessionContext sessionContext):", e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#delete(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings("unchecked")
	public SessionContext delete(SessionContext sessionContext)
			throws ServiceException {
		try {
			ContractManageFormDTO formDTO = (ContractManageFormDTO) sessionContext.getRequestParameter();
			String contractId = formDTO.getContractId();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			if (StringUtils.hasText(contractId)) {
				Message msg = null;
				BimContract contract = this.contractDAO.findByPrimaryKey(BimContract.class, contractId);
				if (contract != null) {
					if (IAtomsConstants.YES.equals(contract.getDeleted())) {
						msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_ALERDAY_UPDATE);
					} else {
						//刪除合約,修改刪除狀態為Y，表示已經刪除，更新數據
						contract.setDeleted(IAtomsConstants.YES);
						//將設備狀態變為以遺失效
						contract.setContractStatus(IAtomsConstants.PARAM_CONTRACT_STATUS_INVALID);
						contract.setUpdatedById(logonUser.getId());
						contract.setUpdatedByName(logonUser.getName());
						contract.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						this.contractDAO.getDaoSupport().saveOrUpdate(contract);
						this.contractDAO.getDaoSupport().flush();
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
					}
				} else {
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
				}
				sessionContext.setReturnMessage(msg);
			}
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOG.error("delete() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOG.error("delete is Failed !!!", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED);
		}
		return sessionContext;
	}
	
	/**
	 * Purpose:獲取設備名稱列表
	 * @author allenchen
	 * @throws ServiceException:出錯時拋出ServiceExecption
	 * @return List<Parameter>：設備集合
	 */
	public List<Parameter> getAssetList() throws ServiceException {
		List<Parameter> manuFacturerList = null;
		try {
			manuFacturerList = this.contractAssetDAO.listAsset();
		}catch (DataAccessException e) {
			LOG.error("getAssetList() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE);
		} catch (Exception e) {
			LOG.error("getAssetList() is failed!!!", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED);
		}
		return manuFacturerList;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#getContractListByCustomerId(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getContractByCustomerAndStatus(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		List<Parameter> contractList = null;
		try {
			// 客戶編號
			String customerId = (String) inquiryContext.getParameter(ContractSlaFormDTO.ORIGINAL_CUSTOMER_ID);
			String contractStatus = (String) inquiryContext.getParameter(BimContractDTO.ATTRIBUTE.CONTRACT_STATUS.getValue());
			String companyType = (String) inquiryContext.getParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue());
			// 获取合約下拉框值
			contractList = this.contractDAO.getContractByCustomerAndStatus(customerId, contractStatus, companyType);
		} catch (DataAccessException e) {
			LOG.error(".getContractByCustomerAndStatus() DataAccess Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error("getContractByCustomerAndStatus() Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return contractList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#getContractCodeList(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getContractCodeList(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		long startQueryContractTime = System.currentTimeMillis();
		List<Parameter> contractCodeList = null;
		try {
			// 客戶編號
			String customerId = (String) inquiryContext.getParameter(ContractSlaFormDTO.ORIGINAL_CUSTOMER_ID);
			// 合約狀態
			String contractStatus = (String) inquiryContext.getParameter(BimContractDTO.ATTRIBUTE.CONTRACT_STATUS.getValue());
			//獲取排序列
			String order = (String) inquiryContext.getParameter(IAtomsConstants.QUERY_PAGE_ORDER);
			//獲取排序方式
			String sort = (String) inquiryContext.getParameter(IAtomsConstants.PARAM_PAGE_ORDER);
			// 是否有sla
			Boolean isHaveSla = Boolean.FALSE;
			Object isHaveSlaObject = inquiryContext.getParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue());
			if (isHaveSlaObject instanceof Boolean) {
				isHaveSla = (Boolean)isHaveSlaObject;
			} 
			// 是否忽略刪除
			Boolean ignoreDeleted = null;
			Object ignoreDeletedObject = inquiryContext.getParameter(IAtomsConstants.PARAM_IGNORE_DELETED);
			if (ignoreDeletedObject instanceof Boolean) {
				ignoreDeleted = (Boolean)ignoreDeletedObject;
			}
			if(ignoreDeleted == null){
				ignoreDeleted = Boolean.TRUE;
			}
			// 获取合約下拉框值
			contractCodeList = this.contractDAO.getContractCodeList(customerId, contractStatus, isHaveSla, order, sort, ignoreDeleted);
		} catch (DataAccessException e) {
			LOG.error(".getContractCodeList() DataAccess Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error("getContractCodeList() Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		long endQueryContractTime = System.currentTimeMillis();
		LOG.debug("calculate time --> load", "Service getContractCodeList:" + (endQueryContractTime - startQueryContractTime));
		return contractCodeList;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#getContractWarranty(cafe.core.context.MultiParameterInquiryContext)
	 */
	public Integer getContractWarranty(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		//合約ID
		String contractId = (String) inquiryContext.getParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue());
		String factoryWarranty = (String) inquiryContext.getParameter(BimContractDTO.ATTRIBUTE.FACTORY_WARRANTY.getValue());
		String customerWarranty = (String) inquiryContext.getParameter(BimContractDTO.ATTRIBUTE.CUSTOMER_WARRANTY.getValue());
		BimContract contract = this.contractDAO.findByPrimaryKey(BimContract.class, contractId);
		if (contract != null) {
			if (StringUtils.hasText(customerWarranty)) {
				return contract.getCustomerWarranty();
			}
			if (StringUtils.hasText(factoryWarranty)) {
				return contract.getFactoryWarranty();
			}
		}
		return null;
	}
	
	/**
	 * Purpose:刪除臨時目錄文件
	 * @author evanliu
	 * @param sessionContext
	 * @throws ServiceException
	 * @return SessionContext
	 */
	public SessionContext deleteTempFile(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			ContractManageFormDTO formDTO =  (ContractManageFormDTO) sessionContext.getRequestParameter();
			String fileName = formDTO.getFileName();
			String path = getSaveTempFilePath(true, fileName, null);
			FileUtils.removeFile(path + formDTO.getQquuid());
			sessionContext.setResponseResult(formDTO);
			msg = new Message(Message.STATUS.SUCCESS,IAtomsMessageCode.DELETE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOG.error("init(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	
	/**
	 * Purpose: 創建文件路徑
	 * @author CarrieDuan
	 * @param isTemp ：是否為臨時路徑
	 * @return String ：返回路徑
	 */
	public String getSaveTempFilePath(boolean isTemp, String fileName , String contractId) {
		String path = "";
		try {
			String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
			if (!StringUtils.hasText(fileName)) {
				fileName = UUID.randomUUID().toString();
			}
			if (isTemp) {
				path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
			} else {
				path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_UPLOAD_PATH);
			}
			/*path += File.separator + yearMonthDay + File.separator + IAtomsConstants.UC_NO_BIM_02030 + File.separator 
					+ IAtomsConstants.PARAM_STRING_CONTRACT_ATTACHED_FILE + File.separator + fileName + File.separator;*/
			path += File.separator + IAtomsConstants.UC_NO_BIM_02030 + File.separator + contractId + File.separator 
					+ IAtomsConstants.PARAM_STRING_CONTRACT_ATTACHED_FILE + File.separator + fileName + File.separator;
		} catch (Exception e) {
			LOG.error("getSaveTempFilePath():", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return path;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#saveTempFile(cafe.core.context.MultiParameterInquiryContext)
	 */
	public SessionContext saveFile(SessionContext sessionContext) throws ServiceException {
		try {
			ContractManageFormDTO formDTO =  (ContractManageFormDTO) sessionContext.getRequestParameter();
			Map<String, MultipartFile> fileMap = formDTO.getFileMap();
			String fileName = formDTO.getFileName();
			String path = getSaveTempFilePath(true, fileName, null);
			if(fileMap.size()> 0){
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
					fileNewName = originalFilename.substring(0, lastPoint) + "_" + currentTime + originalFilename.substring(lastPoint);
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
			LOG.error("saveTempFile(MultiParameterInquiryContext inquiryContext):", e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#getfilePath(cafe.core.context.MultiParameterInquiryContext)
	 */
	public SessionContext getFilePath(ContractManageFormDTO command) throws ServiceException {
		SessionContext sessionContext = new SessionContext();
		try {
			String attachedFileId = command.getAttachedFileId();
			BimContractAttachedFile attachedFile = this.contractAttachedFileDAO.findByPrimaryKey(BimContractAttachedFile.class, attachedFileId);
			command.setFileName(attachedFile.getFileName());
			command.setFilePath(attachedFile.getAttachedFile());// + attachedFile.getFileName());
			sessionContext.setResponseResult(command);
		} catch (Exception e) {
			LOG.error("getFilePath(ContractManageFormDTO command):", e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#getFilePath(cafe.core.context.MultiParameterInquiryContext)
	 */
	public String getFilePath(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		String filePath = "";
		try {
			String fileId = (String) inquiryContext.getParameter(BimContractAttachedFileDTO.ATTRIBUTE.ATTACHED_FILE_ID.getValue());
			if (StringUtils.hasText(fileId)) {
				BimContractAttachedFile attachedFile = this.contractAttachedFileDAO.findByPrimaryKey(BimContractAttachedFile.class, fileId);
				if (attachedFile != null) {
					filePath = attachedFile.getAttachedFile();
				}
			}
		} catch (Exception e) {
			LOG.error("getFilePath(MultiParameterInquiryContext inquiryContext):", e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED);
		}
		return filePath;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#getPeripherals(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getPeripherals(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		List<Parameter> peripherals = null;
		try {
			String assetCategory = (String) inquiryContext.getParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue());
			String contractId = (String) inquiryContext.getParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			peripherals = this.contractAssetDAO.listAssetCategorysByContractId(contractId, assetCategory);
		} catch (Exception e) {
			LOG.error("getFilePath(MultiParameterInquiryContext inquiryContext):", e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED);
		}
		return peripherals;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#getVendors(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getVendors(MultiParameterInquiryContext inquiryContext)throws ServiceException {
		List<Parameter> vendors = null;
		try{
			String contractId = (String) inquiryContext.getParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			if (StringUtils.hasText(contractId)) {
				vendors = this.contractVendorDAO.listVendorsByContractId(contractId);
			}
		} catch (DataAccessException e) {
			LOG.error("getVendors() DataAccess Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error("getVendors() Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return vendors;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractService#getHaveEdcContract(java.lang.String)
	 */
	@Override
	public String getHaveEdcContract(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		String result = null;
		try{
			String companyId = (String) inquiryContext.getParameter(BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue());
			List<BimContractDTO> contractDTOs = this.contractDAO.getEdcAssetContract(companyId);
			if(!CollectionUtils.isEmpty(contractDTOs)){
				result = contractDTOs.get(0).getContractId();
			}
		} catch (DataAccessException e) {
			LOG.error("getHaveEdcContract() DataAccess Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOG.error("getHaveEdcContract() Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return result;
	}
	/**
	 * @return the contractDAO
	 */
	public IContractDAO getContractDAO() {
		return contractDAO;
	}

	/**
	 * @param contractDAO the contractDAO to set
	 */
	public void setContractDAO(IContractDAO contractDAO) {
		this.contractDAO = contractDAO;
	}

	/**
	 * @return the contractAssetDAO
	 */
	public IContractAssetDAO getContractAssetDAO() {
		return contractAssetDAO;
	}

	/**
	 * @param contractAssetDAO the contractAssetDAO to set
	 */
	public void setContractAssetDAO(IContractAssetDAO contractAssetDAO) {
		this.contractAssetDAO = contractAssetDAO;
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
	 * @return the contractTypeDAO
	 */
	public IContractTypeDAO getContractTypeDAO() {
		return contractTypeDAO;
	}

	/**
	 * @param contractTypeDAO the contractTypeDAO to set
	 */
	public void setContractTypeDAO(IContractTypeDAO contractTypeDAO) {
		this.contractTypeDAO = contractTypeDAO;
	}

	/**
	 * @return the contractVendorDAO
	 */
	public IContractVendorDAO getContractVendorDAO() {
		return contractVendorDAO;
	}

	/**
	 * @param contractVendorDAO the contractVendorDAO to set
	 */
	public void setContractVendorDAO(IContractVendorDAO contractVendorDAO) {
		this.contractVendorDAO = contractVendorDAO;
	}

	/**
	 * @return the contractAttachedFileDAO
	 */
	public IContractAttachedFileDAO getContractAttachedFileDAO() {
		return contractAttachedFileDAO;
	}

	/**
	 * @param contractAttachedFileDAO the contractAttachedFileDAO to set
	 */
	public void setContractAttachedFileDAO(
			IContractAttachedFileDAO contractAttachedFileDAO) {
		this.contractAttachedFileDAO = contractAttachedFileDAO;
	}

}
