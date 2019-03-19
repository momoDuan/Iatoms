package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.bean.Message.STATUS;
import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
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
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowNumberDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.DmmAssetBorrowFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PaymentFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.services.IAssetBorrowService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetBorrowInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetBorrowListDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetBorrowNumberDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetBorrowInfo;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetBorrowList;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetBorrowNumber;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepository;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryHistory;
/**
 * Purpose: 設備借用service
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年7月31日
 * @MaintenancePersonnel CarrieDuan
 */
public class AssetBorrowService extends AtomicService implements IAssetBorrowService {

	/**
	 * 日誌記錄器  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, AssetBorrowService.class);
	
	/**
	 * 設備借用列表DAO
	 */
	private IDmmAssetBorrowListDAO assetBorrowListDAO;
	/**
	 * 設備借用數量DAO
	 */
	private IDmmAssetBorrowNumberDAO assetBorrowNumberDAO;
	/**
	 * 設備借用主檔DAO
	 */
	private IDmmAssetBorrowInfoDAO assetBorrowInfoDAO;
	/**
	 * 庫存主檔DAO
	 */
	private IDmmRepositoryDAO repositoryDAO;
	/**
	 * 庫存歷史主檔DAO
	 */
	private IDmmRepositoryHistoryDAO repositoryHistoryDAO;
	/**
	 * 使用者賬號DAO
	 */
	private IAdmUserDAO admUserDAO;
	/**
	 * 發送mail組件
	 */
	private MailComponent mailComponent;
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetBorrowService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		try {
			DmmAssetBorrowFormDTO assetBorrowFormDTO = (DmmAssetBorrowFormDTO) sessionContext.getRequestParameter();
			if (assetBorrowFormDTO != null) {
				String ucNo = assetBorrowFormDTO.getUseCaseNo();
				if (IAtomsConstants.UC_NO_DMM_03140.equals(ucNo)) {
					IAtomsLogonUser logonUser = (IAtomsLogonUser) assetBorrowFormDTO.getLogonUser();
					List<AdmRoleDTO> admRoleDTOs = logonUser.getUserFunctions();
					if (!CollectionUtils.isEmpty(admRoleDTOs)) {
						List<String> roles = new ArrayList<String>();
						for (AdmRoleDTO admRoleDTO : admRoleDTOs) {
							if (IAtomsConstants.PARAM_ROLE_CODE_STOREHOUSE_HANDLE.equals(admRoleDTO.getRoleCode())
									|| IAtomsConstants.PARAM_ROLE_CODE_STOREHOUSE_SUPERVISO.equals(admRoleDTO.getRoleCode())) {
								roles.add(admRoleDTO.getRoleCode());
							}
						}
						if (!CollectionUtils.isEmpty(roles)) {
							if (roles.size() > 1) {
								assetBorrowFormDTO.setLoginRoles(IAtomsConstants.PARAM_ONLY_STOREHOUSE_SUPERVISO_and_HANDLE);
							} else {
								if (IAtomsConstants.PARAM_ROLE_CODE_STOREHOUSE_HANDLE.equals(roles.get(0))) {
									assetBorrowFormDTO.setLoginRoles(IAtomsConstants.PARAM_ONLY_STOREHOUSE_HANDLE);
								} else {
									assetBorrowFormDTO.setLoginRoles(IAtomsConstants.PARAM_ONLY_STOREHOUSE_SUPERVISO);
								}
							}
						}
					}
				}
			}
			sessionContext.setResponseResult(assetBorrowFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error("init() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("init()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetBorrowService#query(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext query(SessionContext sessionContext)throws ServiceException {
		Message msg = null;
		try {
			if (sessionContext != null) {
				DmmAssetBorrowFormDTO assetBorrowFormDTO = (DmmAssetBorrowFormDTO) sessionContext.getRequestParameter();
				IAtomsLogonUser logonUser = (IAtomsLogonUser) assetBorrowFormDTO.getLogonUser();
				String ucNo = assetBorrowFormDTO.getUseCaseNo();
				String userRoleId = null;
				if (IAtomsConstants.UC_NO_DMM_03130.equals(ucNo)) {
					userRoleId = logonUser.getId();
				}
				String caseStatus = assetBorrowFormDTO.getCaseStatus();
				String caseCategory = assetBorrowFormDTO.getCaseCategory();
				String borrowStartDate = assetBorrowFormDTO.getBorrowStartDate();
				String borrowEndDate = assetBorrowFormDTO.getBorrowEndDate();
				String borrowCaseId = assetBorrowFormDTO.getBorrowCaseId();
				int page = assetBorrowFormDTO.getPage();
				int rows = assetBorrowFormDTO.getRows();
				//獲取符合條件的總筆數
				List<DmmAssetBorrowInfoDTO> borrowInfoDTOs = this.assetBorrowInfoDAO.getBorrowIds(borrowCaseId, borrowStartDate, borrowEndDate, 
						caseCategory, userRoleId, caseStatus, -1, -1);
				if (!CollectionUtils.isEmpty(borrowInfoDTOs)) {
					int count = borrowInfoDTOs.size();
					//獲取15筆資料id
					borrowInfoDTOs = this.assetBorrowInfoDAO.getBorrowIds(borrowCaseId, borrowStartDate, borrowEndDate, 
							caseCategory, userRoleId, caseStatus, page, rows);
					List<String> borrowIds = new ArrayList<String>();
					for (DmmAssetBorrowInfoDTO intoDTO : borrowInfoDTOs) {
						borrowIds.add(intoDTO.getBorrowCaseId());
					}
					borrowInfoDTOs = this.assetBorrowInfoDAO.listBy(null, null, borrowIds, -1, -1);
					int rowNumber = 0;
					String tempId = null;
					String tempCaseStatus = null;
					for (DmmAssetBorrowInfoDTO intoDTO : borrowInfoDTOs) {
						if (!intoDTO.getBorrowCaseId().equals(tempId)) {
							tempId = intoDTO.getBorrowCaseId();
							intoDTO.setRowNumber(++rowNumber);
						} else {
							intoDTO.setRowNumber(rowNumber);
						}
						if (IAtomsConstants.FIELD_ASSET_BORROW_CATEGORY_APPLY.equals(intoDTO.getBorrowCategory())) {
							intoDTO.setBorrowCategory("申請");
						} else if (IAtomsConstants.FIELD_ASSET_BORROW_CATEGORY_BORROW.equals(intoDTO.getBorrowCategory())) {
							intoDTO.setBorrowCategory("續借");
						}
						if (IAtomsConstants.FIELD_ASSET_BORROW_STATUS_WAIT_PROCESS.equals(intoDTO.getBorrowStatus())) {
							intoDTO.setBorrowStatusName(i18NUtil.getName(intoDTO.getBorrowStatus()));
							if (intoDTO.getDirectorCheckByDate() != null) {
								intoDTO.setIsAgentProcess(IAtomsConstants.YES);
							}
						} else {
							if (IAtomsConstants.YES.equals(intoDTO.getDirectorBack())) {
								tempCaseStatus = i18NUtil.getName(intoDTO.getBorrowStatus()) + "(不同意)";
							} else {
								tempCaseStatus = i18NUtil.getName(intoDTO.getBorrowStatus()) + "(同意)";
							}
							intoDTO.setBorrowStatusName(tempCaseStatus);
						}
						
					}
					assetBorrowFormDTO.setList(borrowInfoDTOs);
					assetBorrowFormDTO.getPageNavigation().setRowCount(count);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				}
				sessionContext.setReturnMessage(msg);
				sessionContext.setResponseResult(assetBorrowFormDTO);
			}
		} catch (DataAccessException e) {
			LOGGER.error("query() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("query()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetBorrowService#save(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		try {
			Message msg = null;
			DmmAssetBorrowFormDTO borrowFormDTO = (DmmAssetBorrowFormDTO) sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) borrowFormDTO.getLogonUser();
			DmmAssetBorrowInfoDTO borrowInfoDTO = borrowFormDTO.getAssetBorrowInfoDTO();
			Transformer transformer = new SimpleDtoDmoTransformer();
			if (borrowInfoDTO != null) {
				String assetBorrowId = null;
				List<DmmAssetBorrowNumberDTO> borrowNumberDTOs = borrowInfoDTO.getAssetBorrowNumberDTOs();
				//----- 保存借用設備主檔 start -----//
				DmmAssetBorrowInfo assetBorrowInfo = (DmmAssetBorrowInfo) transformer.transform(borrowInfoDTO, new DmmAssetBorrowInfo());
				//設備入庫批號
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD);
				//流水號
				long id = this.getSequenceNumberControlDAO().getSeqNo(IAtomsConstants.IATOMS_TB_NAME_DMM_ASSET_BORROW_INFO, yearMonthDay);
				if (id == 0) {
					id++;
				}
				String swiftNumber = StringUtils.toFixString(5, id);
				StringBuffer transInfoId = new StringBuffer();
				//生成轉倉批號
				transInfoId.append(DmmAssetBorrowFormDTO.AB).append(IAtomsConstants.FIELD_ASSET_BORROW_CATEGORY_APPLY)
					.append(yearMonthDay).append(swiftNumber);
				if (transInfoId != null) {
					assetBorrowId = transInfoId.toString();
				}
				assetBorrowInfo.setBorrowUser(logonUser.getId());
				assetBorrowInfo.setBorrowCategory(IAtomsConstants.FIELD_ASSET_BORROW_CATEGORY_APPLY);
				assetBorrowInfo.setBorrowStatus(IAtomsConstants.FIELD_ASSET_BORROW_STATUS_WAIT_PROCESS);
				assetBorrowInfo.setBorrowCaseId(assetBorrowId);
				assetBorrowInfo.setCreatedById(logonUser.getId());
				assetBorrowInfo.setCreatedByName(logonUser.getName());
				assetBorrowInfo.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
				assetBorrowInfo.setUpdatedById(logonUser.getId());
				assetBorrowInfo.setUpdatedByName(logonUser.getName());
				assetBorrowInfo.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				this.assetBorrowInfoDAO.getDaoSupport().save(assetBorrowInfo);
				//----- 保存借用設備主檔 end -----//
				//----- 保存借用設備數量 start -----//
				DmmAssetBorrowNumber borrowNumber = null;
				String numberId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_ASSET_BORROW_NUMBER);
				int i = 0;
				for (DmmAssetBorrowNumberDTO dmmAssetBorrowNumberDTO : borrowNumberDTOs) {
					borrowNumber = (DmmAssetBorrowNumber) transformer.transform(dmmAssetBorrowNumberDTO, new DmmAssetBorrowNumber());
					borrowNumber.setBorrowCaseId(assetBorrowInfo.getBorrowCaseId());
					borrowNumber.setId(numberId + "_" + ++i);
					this.assetBorrowNumberDAO.getDaoSupport().save(borrowNumber);
				}
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_SUCCESS, new String[]{this.getMyName()});
				try {
					List<String> roles = new ArrayList<String>();
					roles.add(IAtomsConstants.PARAM_ROLE_CODE_STOREHOUSE_SUPERVISO);
					List<AdmUserDTO> admUserDTOs = this.admUserDAO.getUserDTOsBy(roles);
					if (!CollectionUtils.isEmpty(admUserDTOs)) {
						Map<DmmAssetBorrowInfoDTO, List<DmmAssetBorrowNumberDTO>> map = new HashMap<DmmAssetBorrowInfoDTO, List<DmmAssetBorrowNumberDTO>>();
						StringBuffer buffer = new StringBuffer();
						for (AdmUserDTO admUserDTO : admUserDTOs) {
							buffer.append(admUserDTO.getEmail()).append(IAtomsConstants.MARK_SEMICOLON);
						}
						AdmUser admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, logonUser.getId());
						buffer.append(admUser.getManagerEmail()).append(IAtomsConstants.MARK_SEMICOLON);
						borrowInfoDTO.setSendToMail(buffer.toString());
						borrowInfoDTO.setCreatedByName(logonUser.getName());
						map.put(borrowInfoDTO, borrowNumberDTOs);
						this.sendMail(map, Boolean.FALSE);
					}
				} catch (Exception e) {
					LOGGER.debug(".save() --> sendMail() is error... ");
				}
				//----- 保存借用設備數量 end -----//
			} else {
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("save() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("save()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetBorrowService#getBorrowCaseId(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getBorrowCaseId(MultiParameterInquiryContext param) throws ServiceException {
		List<Parameter> borrowCaseIds = null;
		try {
			if (param != null) {
				String roles = (String) param.getParameter(DmmAssetBorrowFormDTO.PARAM_LOGIN_ROLES);
				if (StringUtils.hasText(roles)) {
					borrowCaseIds = this.assetBorrowInfoDAO.getBorrowCaseIds(roles);
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("getBorrowCaseId() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("getBorrowCaseId()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
		return borrowCaseIds;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetBorrowService#saveProcess(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext saveProcess(SessionContext sessionContext) throws ServiceException {
		try {
			Message msg = null;
			DmmAssetBorrowFormDTO borrowFormDTO = (DmmAssetBorrowFormDTO) sessionContext.getRequestParameter();
			Transformer transformer = new SimpleDtoDmoTransformer();
			if (borrowFormDTO != null) {
				IAtomsLogonUser logonUser = (IAtomsLogonUser) borrowFormDTO.getLogonUser();
				String isBack = borrowFormDTO.getIsBack();
				String isDirectorCheck = borrowFormDTO.getIsDirectorCheck();
				String borrowCaseId = borrowFormDTO.getBorrowCaseId();
				String comment = borrowFormDTO.getComment();
				Map<DmmAssetBorrowInfoDTO, List<DmmAssetBorrowNumberDTO>> sendMap = new HashMap<DmmAssetBorrowInfoDTO, List<DmmAssetBorrowNumberDTO>>();
				DmmAssetBorrowInfoDTO assetBorrowInfoDTO = null;
				List<DmmAssetBorrowNumberDTO> backBorrowNumberDTOs = null;
				if (IAtomsConstants.YES.equals(isDirectorCheck) && StringUtils.hasText(borrowCaseId)) {
					List<String> borrowCaseIds = StringUtils.toList(borrowCaseId, IAtomsConstants.MARK_SEPARATOR);
					DmmAssetBorrowInfo assetBorrowInfo = null;
					for (String caseId : borrowCaseIds) {
						assetBorrowInfo = this.assetBorrowInfoDAO.findByPrimaryKey(DmmAssetBorrowInfo.class, caseId);
						if (IAtomsConstants.YES.equals(isBack)) {
							assetBorrowInfo.setDirectorBack(IAtomsConstants.YES);
							assetBorrowInfo.setBorrowStatus(IAtomsConstants.FIELD_ASSET_BORROW_STATUS_PROCESS);
							backBorrowNumberDTOs = this.assetBorrowNumberDAO.listBy(caseId);
							assetBorrowInfoDTO = (DmmAssetBorrowInfoDTO) transformer.transform(assetBorrowInfo, new DmmAssetBorrowInfoDTO());
							assetBorrowInfoDTO.setBackComment(comment);
							sendMap.put(assetBorrowInfoDTO, backBorrowNumberDTOs);
						}
						assetBorrowInfo.setDirectorCheckByDate(DateTimeUtils.getCurrentTimestamp());
						assetBorrowInfo.setDirectorCheckById(logonUser.getId());
						assetBorrowInfo.setDirectorCheckByName(logonUser.getName());
						assetBorrowInfo.setUpdatedById(logonUser.getId());
						assetBorrowInfo.setUpdatedByName(logonUser.getName());
						assetBorrowInfo.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						this.assetBorrowInfoDAO.getDaoSupport().saveOrUpdate(assetBorrowInfo);
					}
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_SUCCESS, new String[]{this.getMyName()});
				} else {
					DmmAssetBorrowInfo assetBorrowInfo = null;
					if (IAtomsConstants.YES.equals(isBack)) {
						List<String> borrowCaseIds = StringUtils.toList(borrowCaseId, IAtomsConstants.MARK_SEPARATOR);
						assetBorrowInfo = null;
						for (String caseId : borrowCaseIds) {
							assetBorrowInfo = this.assetBorrowInfoDAO.findByPrimaryKey(DmmAssetBorrowInfo.class, caseId);
							assetBorrowInfo.setDirectorBack(IAtomsConstants.YES);
							assetBorrowInfo.setBorrowStatus(IAtomsConstants.FIELD_ASSET_BORROW_STATUS_PROCESS);
							assetBorrowInfo.setAgentByDate(DateTimeUtils.getCurrentTimestamp());
							assetBorrowInfo.setAgentById(logonUser.getId());
							assetBorrowInfo.setAgentByName(logonUser.getName());
							assetBorrowInfo.setUpdatedById(logonUser.getId());
							assetBorrowInfo.setUpdatedByName(logonUser.getName());
							assetBorrowInfo.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
							this.assetBorrowInfoDAO.getDaoSupport().saveOrUpdate(assetBorrowInfo);
							backBorrowNumberDTOs = this.assetBorrowNumberDAO.listBy(caseId);
							assetBorrowInfoDTO = (DmmAssetBorrowInfoDTO) transformer.transform(assetBorrowInfo, new DmmAssetBorrowInfoDTO());
							assetBorrowInfoDTO.setBackComment(comment);
							sendMap.put(assetBorrowInfoDTO, backBorrowNumberDTOs);
						}
					} else {
						List<DmmAssetBorrowInfoDTO> assetBorrowInfoDTOs = borrowFormDTO.getAssetBorrowInfoDTOs();
						DmmAssetBorrowList assetBorrowList = null;
						Map<String, List<DmmAssetBorrowInfoDTO>> assetBorrowInfoDTOMaps = new HashMap<String, List<DmmAssetBorrowInfoDTO>>();
						List<DmmAssetBorrowInfoDTO> infoDTOs = new ArrayList<DmmAssetBorrowInfoDTO>();
						borrowCaseId = assetBorrowInfoDTOs.get(0).getBorrowCaseId();
						DmmRepositoryDTO dmmRepositoryDTO = null;
						DmmRepository repository = null;
						for (DmmAssetBorrowInfoDTO dmmAssetBorrowInfoDTO : assetBorrowInfoDTOs) {
							if (StringUtils.hasText(dmmAssetBorrowInfoDTO.getSerialNumber())) {
								//修改設備狀態
								dmmRepositoryDTO = this.repositoryDAO.getBySerialNumber(dmmAssetBorrowInfoDTO.getSerialNumber());
								//設備序號不存在
								if (dmmRepositoryDTO != null) {
									if (IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY.equals(dmmRepositoryDTO.getStatus())) {
										dmmAssetBorrowInfoDTO.setAssetId(dmmRepositoryDTO.getAssetId());
									}else {
										//設備序號不爲庫存
										if (dmmAssetBorrowInfoDTO.getBorrowCategory().equals("續借")){
											if (!IAtomsConstants.PARAM_ASSET_STATUS_BORROWING.equals(dmmRepositoryDTO.getStatus())) {
												msg = new Message(STATUS.FAILURE, IAtomsMessageCode.SERIAL_NUMBER_NOT_REPERTORY, new String[]{dmmAssetBorrowInfoDTO.getSerialNumber(), "借用中"});
												sessionContext.setReturnMessage(msg);
												return sessionContext;
											} else {
												dmmAssetBorrowInfoDTO.setAssetId(dmmRepositoryDTO.getAssetId());
											}
										} else {
											msg = new Message(STATUS.FAILURE, IAtomsMessageCode.SERIAL_NUMBER_NOT_REPERTORY, new String[]{dmmAssetBorrowInfoDTO.getSerialNumber(), "庫存"});
											sessionContext.setReturnMessage(msg);
											return sessionContext;
										}
										
									}
								} else {
									msg = new Message(STATUS.FAILURE, IAtomsMessageCode.SERIAL_NUMBER_NOT_EXIS, new String[]{dmmAssetBorrowInfoDTO.getSerialNumber()});
									sessionContext.setReturnMessage(msg);
									return sessionContext;
								}
							}
							if (!borrowCaseId.equals(dmmAssetBorrowInfoDTO.getBorrowCaseId())) {
								assetBorrowInfoDTOMaps.put(borrowCaseId, infoDTOs);
								borrowCaseId = dmmAssetBorrowInfoDTO.getBorrowCaseId();
								infoDTOs = new ArrayList<DmmAssetBorrowInfoDTO>();
								infoDTOs.add(dmmAssetBorrowInfoDTO);
							} else {
								infoDTOs.add(dmmAssetBorrowInfoDTO);
							}
						}
						assetBorrowInfoDTOMaps.put(infoDTOs.get(0).getBorrowCaseId(), infoDTOs);
						DmmRepositoryHistory dmmRepositoryHistory = null;
						for(Map.Entry<String, List<DmmAssetBorrowInfoDTO>> map: assetBorrowInfoDTOMaps.entrySet()) {
							infoDTOs = map.getValue();
							borrowCaseId = map.getKey();
							for (DmmAssetBorrowInfoDTO dto : infoDTOs) {
								if (StringUtils.hasText(dto.getSerialNumber())) {
									assetBorrowInfo = this.assetBorrowInfoDAO.findByPrimaryKey(DmmAssetBorrowInfo.class, dto.getBorrowCaseId());
									//修改設備狀態
									repository = this.repositoryDAO.findByPrimaryKey(DmmRepository.class, dto.getAssetId());
									repository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_BORROWING);
									repository.setBorrowerStart(assetBorrowInfo.getBorrowStartDate());
									repository.setBorrowerEnd(dto.getBorrowEndDate());
									repository.setBorrower(assetBorrowInfo.getBorrowUser());
									repository.setCaseId(borrowCaseId);
									repository.setUpdateUser(logonUser.getId());
									repository.setUpdateUserName(logonUser.getName());
									repository.setUpdateDate(DateTimeUtils.getCurrentTimestamp());
									this.repositoryDAO.getDaoSupport().saveOrUpdate(repository);
									//歷史
									dmmRepositoryHistory = (DmmRepositoryHistory) transformer.transform(repository, new DmmRepositoryHistory());
									dmmRepositoryHistory.setAction(IAtomsConstants.PARAM_ACTION_BORROW);
									dmmRepositoryHistory.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
									this.repositoryHistoryDAO.getDaoSupport().save(dmmRepositoryHistory);
									//新增借用設備列表
									if (IAtomsConstants.FIELD_ASSET_BORROW_CATEGORY_BORROW.equals(assetBorrowInfo.getBorrowCategory())) {
										assetBorrowList = this.assetBorrowListDAO.findByPrimaryKey(DmmAssetBorrowList.class, dto.getBorrowListId());
										assetBorrowList.setBorrowEndDate(dto.getBorrowEndDate());
										this.assetBorrowListDAO.getDaoSupport().saveOrUpdate(assetBorrowList);
										this.assetBorrowListDAO.getDaoSupport().flush();
									} else {
										assetBorrowList = new DmmAssetBorrowList();
										assetBorrowList.setAssetTypeId(dto.getAssetTypeId());
										assetBorrowList.setBorrowCaseId(borrowCaseId);
										assetBorrowList.setBorrowEndDate(dto.getBorrowEndDate());
										assetBorrowList.setSerialNumber(dto.getSerialNumber());
										assetBorrowList.setId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_ASSET_BORROW_LIST));
										this.assetBorrowListDAO.getDaoSupport().save(assetBorrowList);
										this.assetBorrowListDAO.getDaoSupport().flush();
									}
									this.repositoryDAO.getDaoSupport().clear();
									this.repositoryHistoryDAO.getDaoSupport().clear();
									this.assetBorrowListDAO.getDaoSupport().clear();
								}
							}
							//更新設備借用主檔
							assetBorrowInfo = this.assetBorrowInfoDAO.findByPrimaryKey(DmmAssetBorrowInfo.class, borrowCaseId);
							assetBorrowInfo.setAgentByDate(DateTimeUtils.getCurrentTimestamp());
							assetBorrowInfo.setAgentById(logonUser.getId());
							assetBorrowInfo.setAgentByName(logonUser.getName());
							assetBorrowInfo.setUpdatedById(logonUser.getId());
							assetBorrowInfo.setUpdatedByName(logonUser.getName());
							assetBorrowInfo.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
							assetBorrowInfo.setBorrowStatus(IAtomsConstants.FIELD_ASSET_BORROW_STATUS_PROCESS);
							this.assetBorrowInfoDAO.getDaoSupport().saveOrUpdate(assetBorrowInfo);
						}
					}
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_SUCCESS, new String[]{this.getMyName()});
				}
				if (IAtomsConstants.YES.equals(isBack)) {
					try {
						AdmUser admUser = null;
						StringBuffer buffer = new StringBuffer();
						for(Map.Entry<DmmAssetBorrowInfoDTO, List<DmmAssetBorrowNumberDTO>> map: sendMap.entrySet()) {
							buffer = new StringBuffer();
							assetBorrowInfoDTO = map.getKey();
							admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, assetBorrowInfoDTO.getCreatedById());
							buffer.append(admUser.getEmail()).append(IAtomsConstants.MARK_SEMICOLON);
							buffer.append(admUser.getManagerEmail()).append(IAtomsConstants.MARK_SEMICOLON);
							if (StringUtils.hasText(assetBorrowInfoDTO.getDirectorCheckById())) {
								admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, assetBorrowInfoDTO.getDirectorCheckById());
								buffer.append(admUser.getEmail()).append(IAtomsConstants.MARK_SEMICOLON);
							}
							assetBorrowInfoDTO.setSendToMail(buffer.toString());
						}
						this.sendMail(sendMap, Boolean.TRUE);
					} catch (Exception e) {
						LOGGER.debug(".save() --> sendMail() is error... ");
					}
				}
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("saveProcess() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("saveProcess()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetBorrowService#getBorrowAssetItemByIds(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<DmmAssetBorrowInfoDTO> getBorrowAssetItemByIds(MultiParameterInquiryContext param) throws ServiceException {
		List<DmmAssetBorrowInfoDTO> borrowInfoDTOs = new ArrayList<DmmAssetBorrowInfoDTO>();
		try {
			if (param != null) {
				String borrowId = (String) param.getParameter(DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_CASE_ID.getValue());
				if (StringUtils.hasText(borrowId)) {
					List<DmmAssetBorrowInfoDTO> assetBorrowInfoDTOs = null;
					List<String> borrowUserIds = StringUtils.toList(borrowId, IAtomsConstants.MARK_SEPARATOR);
					assetBorrowInfoDTOs = this.assetBorrowInfoDAO.getBorrowListId(borrowUserIds);
					String number = null;
					int rowNumber = 1;
					for (DmmAssetBorrowInfoDTO intoDTO : assetBorrowInfoDTOs) {
						if (IAtomsConstants.FIELD_ASSET_BORROW_CATEGORY_APPLY.equals(intoDTO.getBorrowCategory())) {
							intoDTO.setBorrowCategory("申請");
						} else if (IAtomsConstants.FIELD_ASSET_BORROW_CATEGORY_BORROW.equals(intoDTO.getBorrowCategory())) {
							intoDTO.setBorrowCategory("續借");
						}
						number = intoDTO.getBorrowNumber();
						for(int i = 0; i < Integer.valueOf(number); i++) {
							intoDTO.setRowNumber(rowNumber++);
							borrowInfoDTOs.add((DmmAssetBorrowInfoDTO)intoDTO.clone());
						}
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("getBorrowAssetItemByIds() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("getBorrowAssetItemByIds()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return borrowInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetBorrowService#queryProcess(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext queryProcess(SessionContext sessionContext) throws ServiceException {
		try {
			Message msg = null;
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, 0);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, new ArrayList<DmmAssetBorrowInfoDTO>());
			if (sessionContext != null) {
				DmmAssetBorrowFormDTO assetBorrowFormDTO = (DmmAssetBorrowFormDTO) sessionContext.getRequestParameter();
				IAtomsLogonUser logonUser = (IAtomsLogonUser) assetBorrowFormDTO.getLogonUser();
				String ucNo = assetBorrowFormDTO.getUseCaseNo();
				String userRoleId = null;
				if (IAtomsConstants.UC_NO_DMM_03130.equals(ucNo)) {
					userRoleId = logonUser.getId();
				}
				int page = assetBorrowFormDTO.getPage();
				int rows = assetBorrowFormDTO.getRows();
				List<DmmAssetBorrowInfoDTO> borrowInfoDTOs = this.assetBorrowInfoDAO.getBorrowInfoProcess(logonUser.getId(), -1, -1);
				if (!CollectionUtils.isEmpty(borrowInfoDTOs)) {
					int total = borrowInfoDTOs.size();
					borrowInfoDTOs = this.assetBorrowInfoDAO.getBorrowInfoProcess(logonUser.getId(), page, rows);
					/*for (DmmAssetBorrowInfoDTO intoDTO : borrowInfoDTOs) {
						if (IAtomsConstants.FIELD_ASSET_BORROW_CATEGORY_APPLY.equals(intoDTO.getBorrowCategory())) {
							intoDTO.setBorrowCategory("申請");
						} else if (IAtomsConstants.FIELD_ASSET_BORROW_CATEGORY_BORROW.equals(intoDTO.getBorrowCategory())) {
							intoDTO.setBorrowCategory("續借");
						}
					}*/
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, total);
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, borrowInfoDTOs);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				}
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
				sessionContext.setReturnMessage(msg);
				sessionContext.setResponseResult(assetBorrowFormDTO);
			}
		} catch (DataAccessException e) {
			LOGGER.error("queryProcess() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("queryProcess()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetBorrowService#saveBorrow(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext saveBorrow(SessionContext sessionContext) throws ServiceException {
		try {
			Message msg = null;
			DmmAssetBorrowFormDTO borrowFormDTO = (DmmAssetBorrowFormDTO) sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) borrowFormDTO.getLogonUser();
			Transformer transformer = new SimpleDtoDmoTransformer();
			Map map = new HashMap();
			if (borrowFormDTO != null) {
				String borrowListId = borrowFormDTO.getBorrowListId();
				String delayDate = borrowFormDTO.getDelayDate();
				String assetBorrowId = null;
				DmmAssetBorrowInfo assetBorrowInfo = null;
				DmmAssetBorrowNumber borrowNumber = null;
				if (StringUtils.hasText(borrowListId) && StringUtils.hasText(delayDate)) {
					Map<DmmAssetBorrowInfoDTO, List<DmmAssetBorrowNumberDTO>> sendMap = new HashMap<DmmAssetBorrowInfoDTO, List<DmmAssetBorrowNumberDTO>>();
					List<DmmAssetBorrowNumberDTO> backBorrowNumberDTOs = null;
					List<String> borrowListIds = StringUtils.toList(borrowListId, IAtomsConstants.MARK_SEPARATOR);
					DmmAssetBorrowList oldAssetBorrowList = null;
					DmmAssetBorrowList newBorrowList = null;
					for (String id : borrowListIds) {
						oldAssetBorrowList = this.assetBorrowListDAO.findByPrimaryKey(DmmAssetBorrowList.class, id);
						//----- 保存借用設備主檔 start -----//
						assetBorrowInfo = new DmmAssetBorrowInfo();
						//設備入庫批號
						String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD);
						//流水號
						long index = this.getSequenceNumberControlDAO().getSeqNo(IAtomsConstants.IATOMS_TB_NAME_DMM_ASSET_BORROW_INFO, yearMonthDay);
						if (index == 0) {
							index++;
						}
						String swiftNumber = StringUtils.toFixString(5, index);
						StringBuffer transInfoId = new StringBuffer();
						//生成轉倉批號
						transInfoId.append(DmmAssetBorrowFormDTO.AB).append(IAtomsConstants.FIELD_ASSET_BORROW_CATEGORY_BORROW)
							.append(yearMonthDay).append(swiftNumber);
						if (transInfoId != null) {
							assetBorrowId = transInfoId.toString();
						}
						assetBorrowInfo.setBorrowStartDate(oldAssetBorrowList.getBorrowEndDate());
						assetBorrowInfo.setBorrowEndDate(DateTimeUtils.toTimestamp(delayDate));
						assetBorrowInfo.setBorrowUser(logonUser.getId());
						assetBorrowInfo.setBorrowCategory(IAtomsConstants.FIELD_ASSET_BORROW_CATEGORY_BORROW);
						assetBorrowInfo.setBorrowStatus(IAtomsConstants.FIELD_ASSET_BORROW_STATUS_WAIT_PROCESS);
						assetBorrowInfo.setBorrowCaseId(assetBorrowId);
						assetBorrowInfo.setCreatedById(logonUser.getId());
						assetBorrowInfo.setCreatedByName(logonUser.getName());
						assetBorrowInfo.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
						assetBorrowInfo.setUpdatedById(logonUser.getId());
						assetBorrowInfo.setUpdatedByName(logonUser.getName());
						assetBorrowInfo.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						this.assetBorrowInfoDAO.save(assetBorrowInfo);
						//----- 保存借用設備主檔 end -----//
						//----- 保存借用設備數量 start -----//
						borrowNumber = new DmmAssetBorrowNumber();
						borrowNumber.setId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_ASSET_BORROW_NUMBER));
						borrowNumber.setAssetTypeId(oldAssetBorrowList.getAssetTypeId());
						borrowNumber.setBorrowNumber("1");
						borrowNumber.setBorrowCaseId(assetBorrowInfo.getBorrowCaseId());
						this.assetBorrowNumberDAO.save(borrowNumber);
						//----- 保存借用設備數量 end -----//
						//----- 保存借用設備集合 start -----//
						newBorrowList = new DmmAssetBorrowList();
						newBorrowList.setId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_ASSET_BORROW_LIST));
						newBorrowList.setSerialNumber(oldAssetBorrowList.getSerialNumber());
						newBorrowList.setAssetTypeId(oldAssetBorrowList.getAssetTypeId());
						newBorrowList.setBorrowCaseId(assetBorrowInfo.getBorrowCaseId());
						this.assetBorrowListDAO.save(newBorrowList);
						//----- 保存借用設備集合 start -----//
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_SUCCESS, new String[]{this.getMyName()});
						map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.TRUE);
						
						backBorrowNumberDTOs = this.assetBorrowNumberDAO.listBy(assetBorrowInfo.getBorrowCaseId());
						sendMap.put((DmmAssetBorrowInfoDTO) transformer.transform(assetBorrowInfo, new DmmAssetBorrowInfoDTO()), backBorrowNumberDTOs);
					}
					try {
						DmmAssetBorrowInfoDTO assetBorrowInfoDTO = null;
						AdmUser admUser = null;
						StringBuffer buffer = new StringBuffer();
						List<String> roles = new ArrayList<String>();
						roles.add(IAtomsConstants.PARAM_ROLE_CODE_STOREHOUSE_SUPERVISO);
						List<AdmUserDTO> admUserDTOs = this.admUserDAO.getUserDTOsBy(roles);
						StringBuffer buffer2 = new StringBuffer();
						for (AdmUserDTO admUserDTO : admUserDTOs) {
							buffer2.append(admUserDTO.getEmail()).append(IAtomsConstants.MARK_SEMICOLON);
						}
						for(Map.Entry<DmmAssetBorrowInfoDTO, List<DmmAssetBorrowNumberDTO>> map1: sendMap.entrySet()) {
							//backBorrowNumberDTOs = map.getValue();
							buffer = new StringBuffer();
							assetBorrowInfoDTO = map1.getKey();
							admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, assetBorrowInfoDTO.getCreatedById());
							buffer.append(admUser.getManagerEmail()).append(IAtomsConstants.MARK_SEMICOLON);
							buffer.append(buffer2);
							assetBorrowInfoDTO.setSendToMail(buffer.toString());
						}
						this.sendMail(sendMap, Boolean.FALSE);
						/*List<String> roles = new ArrayList<String>();
						roles.add(IAtomsConstants.PARAM_ROLE_CODE_STOREHOUSE_SUPERVISO);
						List<AdmUserDTO> admUserDTOs = this.admUserDAO.getUserDTOsBy(roles);
						if (!CollectionUtils.isEmpty(admUserDTOs)) {
							
							
							StringBuffer buffer = new StringBuffer();
							for (AdmUserDTO admUserDTO : admUserDTOs) {
								buffer.append(admUserDTO.getEmail()).append(IAtomsConstants.MARK_SEMICOLON);
							}
							AdmUser admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, logonUser.getId());
							buffer.append(admUser.getManagerEmail()).append(IAtomsConstants.MARK_SEMICOLON);
							borrowInfoDTO.setSendToMail(buffer.toString());
							map.put(borrowInfoDTO, borrowNumberDTOs);
							this.sendMail(map);
						}*/
					} catch (Exception e) {
						LOGGER.debug(".save() --> sendMail() is error... ");
					}
				} else {
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()});
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				}
			} else {
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()});
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("save() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("save()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetBorrowService#checkSerialNumber(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext checkSerialNumber(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		Map map = new HashMap();
		map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
		try {
			if (sessionContext != null) {
				DmmAssetBorrowFormDTO assetBorrowFormDTO = (DmmAssetBorrowFormDTO) sessionContext.getRequestParameter();
				if (assetBorrowFormDTO != null) {
					DmmAssetBorrowInfoDTO assetBorrowInfoDTO = assetBorrowFormDTO.getAssetBorrowInfoDTO();
					String assetTypeId = assetBorrowInfoDTO.getAssetTypeId();
					String serialNumber = assetBorrowInfoDTO.getSerialNumber();
					List<DmmRepositoryDTO> dmmRepositoryDTOs = this.repositoryDAO.checkBorrowSerialNumber(assetTypeId, serialNumber);
					if (!CollectionUtils.isEmpty(dmmRepositoryDTOs)) {
						//判斷狀態是否爲庫存
						if (!IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY.equals(dmmRepositoryDTOs.get(0).getStatus())) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SERIAL_NUMBER_NOT_REPERTORY, new String[]{serialNumber, "庫存"});
						} else {
							msg = new Message(Message.STATUS.SUCCESS);
							map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.TRUE);
						}
					} else {
						//設備序號不存在
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SERIAL_NUMBER_NOT_EXIS, new String[]{serialNumber});
					}
				}
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("checkSerialNumber() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("checkSerialNumber()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	private void sendMail (Map<DmmAssetBorrowInfoDTO, List<DmmAssetBorrowNumberDTO>> sendMap, Boolean isBack) throws SecurityException {
		try{
			//郵件主題
			String mailSubject = null;
			//郵件內容
			String mailContext = null;
			//郵件主題模板
			String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + DmmAssetBorrowFormDTO.PARAM_BORROW_MAIL_THEME;
			//郵件內容模板
			String textTemplate = null;
			if (isBack) {
				textTemplate = MailComponent.MAIL_TEMPLATE_ADD + DmmAssetBorrowFormDTO.PARAM_BORROW_BACK_MAIL_CONTEXT;
			} else {
				textTemplate = MailComponent.MAIL_TEMPLATE_ADD + DmmAssetBorrowFormDTO.PARAM_BORROW_MAIL_CONTEXT;
			}
			//郵件內容
			Map<String, Object> variables = new HashMap<String, Object>();
			StringBuffer buffer = null;
			String toEmail = null;
			List<DmmAssetBorrowNumberDTO> assetBorrowNumberDTOs = null;
			DmmAssetBorrowInfoDTO assetBorrowInfoDTO = null;
			String assetCategoryName = null;
			String assetTypeName = null;
			String borrowNumber = null;
			for(Map.Entry<DmmAssetBorrowInfoDTO, List<DmmAssetBorrowNumberDTO>> map: sendMap.entrySet()) {
				assetBorrowNumberDTOs = map.getValue();
				assetBorrowInfoDTO = map.getKey();
				toEmail = assetBorrowInfoDTO.getSendToMail();
				buffer = new StringBuffer();
				if (StringUtils.hasText(toEmail) && toEmail.length() > 1) {
					for (DmmAssetBorrowNumberDTO dto : assetBorrowNumberDTOs) {
						assetCategoryName = dto.getAssetCategoryName();
						assetTypeName = dto.getAssetTypeName();
						borrowNumber = dto.getBorrowNumber();
						buffer.append("<tr>");
						if (StringUtils.hasText(assetCategoryName)) {
							buffer.append("<td style= \"border:1px solid black; text-align:center;font-size: 12px; \">").append(assetCategoryName).append("</td>");
						} else {
							buffer.append("<td style= \"border:1px solid black; text-align:center;font-size: 12px; \">").append("&nbsp;&nbsp;</td>");
						}
						
						if (StringUtils.hasText(assetTypeName)) {
							buffer.append("<td style= \"border:1px solid black; text-align:center;font-size: 12px; \">").append(assetTypeName).append("</td>");
						} else {
							buffer.append("<td style= \"border:1px solid black; text-align:center;font-size: 12px; \">").append("&nbsp;&nbsp;</td>");
						}
						
						if (StringUtils.hasText(borrowNumber)) {
							buffer.append("<td style= \"border:1px solid black; text-align:center;font-size: 12px; \">").append(borrowNumber).append("</td>");
						} else {
							buffer.append("<td style= \"border:1px solid black; text-align:center;font-size: 12px; \">").append("&nbsp;&nbsp;</td>");
						}
						buffer.append("</tr>");
					}
					mailContext = buffer.toString();
					variables.put("toMail", toEmail);
					//mial主旨
					if (isBack) {
						variables.put("backComment", StringUtils.hasText(assetBorrowInfoDTO.getBackComment())?assetBorrowInfoDTO.getBackComment():"");
						variables.put("status", "申请不同意");
					} else {
						variables.put("status", "申請");
					}
					//mail内容
					variables.put("mailContext", mailContext);
					variables.put("borrowStartDate", DateTimeUtils.toString(assetBorrowInfoDTO.getBorrowStartDate(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
					variables.put("borrowEndDate", DateTimeUtils.toString(assetBorrowInfoDTO.getBorrowEndDate(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
					variables.put("borrowUser", assetBorrowInfoDTO.getCreatedByName());
					variables.put("comment", StringUtils.hasText(assetBorrowInfoDTO.getComment())?assetBorrowInfoDTO.getComment():"");
					try{
						this.mailComponent.mailTo(null, toEmail, subjectTemplate, textTemplate, variables);
					} catch (Exception e) {
						LOGGER.debug(".sendMail() --> sendMail() is error... ");
					}
				}
			}
			/*if (!CollectionUtils.isEmpty(assetBorrowNumberDTOs)) {
				String assetCategoryName = null;
				String assetTypeName = null;
				String borrowNumber = null;
				for (DmmAssetBorrowNumberDTO paymentItemDTO : assetBorrowNumberDTOs) {
					assetCategoryName = paymentItemDTO.getAssetCategoryName();
					assetTypeName = paymentItemDTO.getAssetTypeName();
					borrowNumber = paymentItemDTO.getBorrowNumber();
					buffer.append("<tr>");
					if (StringUtils.hasText(assetCategoryName)) {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append(assetCategoryName).append("</td>");
					} else {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append("&nbsp;&nbsp;</td>");
					}
					
					if (StringUtils.hasText(assetTypeName)) {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append(assetTypeName).append("</td>");
					} else {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204); \">").append("&nbsp;&nbsp;</td>");
					}
					
					if (StringUtils.hasText(borrowNumber)) {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append(borrowNumber).append("</td>");
					} else {
						buffer.append("<td style=\"border:1px solid RGB(204,204,204);\">").append("&nbsp;&nbsp;</td>");
					}
					buffer.append("</tr>");
				}
				mailContext = buffer.toString();
			}*/
			//如果動作後狀態為待維修，則向倉管人員發信
			/*if (IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED.equals(status)) {
				admRole.add(IAtomsConstants.WAREHOUSE_KEEPER);
				mailSubject = i18NUtil.getName(IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED);
			}*/
			//如果動作後狀態為待確認金額，則向客服人員發信
	/*		if (IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED.equals(status)) {
				admRole.add(IAtomsConstants.ACCOUNTING);
				if (isOther) {
					admRole.add(IAtomsConstants.CUSTOMER_SERVICE);
					mailSubject = i18NUtil.getName(IAtomsConstants.PAYMENT_CHECK_RESULT);
				} else {
					mailSubject = i18NUtil.getName(IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED);
				}
			}
			//如果動作後狀態為請款／已取消，則向賬務、客服、倉管人員發信
			if (IAtomsConstants.DATA_STATUS_REQUEST_FUNDS.equals(status)) {
				admRole.add(IAtomsConstants.ACCOUNTING);
				admRole.add(IAtomsConstants.CUSTOMER_SERVICE);
				admRole.add(IAtomsConstants.WAREHOUSE_KEEPER);
				mailSubject = i18NUtil.getName(IAtomsConstants.PAYMENT_ACCOUNTING) + i18NUtil.getName(IAtomsConstants.PAYMENT_COMPLETE);
			}
			//如果狀態為退回，則向客服人員發信
			if (IAtomsConstants.DATA_STATUS_BACK.equals(status)) {
				admRole.add(IAtomsConstants.CUSTOMER_SERVICE);
				mailSubject = i18NUtil.getName(IAtomsConstants.DATA_STATUS_BACK);
			}
			//如果動作後狀態為待償確認，則向客服人員發信
			if (IAtomsConstants.DATA_STATUS_WAIT_CLAIM_CONFIRM.equals(status)) {
				admRole.add(IAtomsConstants.ACCOUNTING);
				mailSubject = i18NUtil.getName(IAtomsConstants.DATA_STATUS_WAIT_CLAIM_CONFIRM);
			}
			//如果動作後狀態為求償確認，則向帳務人員發信
			if (IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM.equals(status)) {
				admRole.add(IAtomsConstants.ACCOUNTING);
				mailSubject = i18NUtil.getName(IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM);
			}
			if (IAtomsConstants.DATA_STATUS_CREATE.equals(status)) {
				admRole.add(IAtomsConstants.CUSTOMER_SERVICE);
			}
			if (isBack) {
				mailSubject = i18NUtil.getName(IAtomsConstants.DATA_STATUS_BACK);
			}
			List<AdmUserDTO> admUserDTOs = this.admUserDAO.getUserDTOsBy(admRole);
			if (!CollectionUtils.isEmpty(admUserDTOs)) {
				StringBuffer emailBuffer = new StringBuffer();
				String toEmail = null;
				for (AdmUserDTO admUserDTO : admUserDTOs) {
					toEmail = admUserDTO.getEmail();
					if (StringUtils.hasText(toEmail)) {
						emailBuffer.append(toEmail).append(IAtomsConstants.MARK_SEMICOLON);
					}
				}
				if (emailBuffer.length() > 0) {
					toEmail = emailBuffer.substring(0, emailBuffer.length()-1).toString();
					variables.put("toMail", toEmail);
					//mial主旨
					variables.put("status", mailSubject);
					//mail内容
					variables.put("mailContext", mailContext);
					try{
						this.mailComponent.mailTo(null, toEmail, subjectTemplate, textTemplate, variables);
					} catch (Exception e) {
						LOG.debug(".sendMail() --> sendMail() is error... ");
					}
				}*/
		//	}			
		} catch (DataAccessException e) {
			LOGGER.error("sendMail() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("sendMail() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetBorrowService#checkAssetIsBorrow(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public String checkAssetIsBorrow(MultiParameterInquiryContext param) throws ServiceException {
		try {
			String assetTypeId = (String) param.getParameter(DmmAssetBorrowListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			if (StringUtils.hasText(assetTypeId)) {
				List<String> serialNumbers = StringUtils.toList(assetTypeId, IAtomsConstants.MARK_SEPARATOR);
				Integer count = 0;
				for (String serialNumber : serialNumbers) {
					count = this.assetBorrowListDAO.listBy(serialNumber);
					if (count != 0) {
						return "設備序號" + serialNumber + "不可進行續借操作";
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	/**
	 * @return the assetBorrowListDAO
	 */
	public IDmmAssetBorrowListDAO getAssetBorrowListDAO() {
		return assetBorrowListDAO;
	}

	/**
	 * @param assetBorrowListDAO the assetBorrowListDAO to set
	 */
	public void setAssetBorrowListDAO(IDmmAssetBorrowListDAO assetBorrowListDAO) {
		this.assetBorrowListDAO = assetBorrowListDAO;
	}

	/**
	 * @return the assetBorrowNumberDAO
	 */
	public IDmmAssetBorrowNumberDAO getAssetBorrowNumberDAO() {
		return assetBorrowNumberDAO;
	}

	/**
	 * @param assetBorrowNumberDAO the assetBorrowNumberDAO to set
	 */
	public void setAssetBorrowNumberDAO(
			IDmmAssetBorrowNumberDAO assetBorrowNumberDAO) {
		this.assetBorrowNumberDAO = assetBorrowNumberDAO;
	}

	/**
	 * @return the assetBorrowInfoDAO
	 */
	public IDmmAssetBorrowInfoDAO getAssetBorrowInfoDAO() {
		return assetBorrowInfoDAO;
	}

	/**
	 * @param assetBorrowInfoDAO the assetBorrowInfoDAO to set
	 */
	public void setAssetBorrowInfoDAO(IDmmAssetBorrowInfoDAO assetBorrowInfoDAO) {
		this.assetBorrowInfoDAO = assetBorrowInfoDAO;
	}

	/**
	 * @return the repositoryDAO
	 */
	public IDmmRepositoryDAO getRepositoryDAO() {
		return repositoryDAO;
	}

	/**
	 * @param repositoryDAO the repositoryDAO to set
	 */
	public void setRepositoryDAO(IDmmRepositoryDAO repositoryDAO) {
		this.repositoryDAO = repositoryDAO;
	}

	/**
	 * @return the repositoryHistoryDAO
	 */
	public IDmmRepositoryHistoryDAO getRepositoryHistoryDAO() {
		return repositoryHistoryDAO;
	}

	/**
	 * @param repositoryHistoryDAO the repositoryHistoryDAO to set
	 */
	public void setRepositoryHistoryDAO(
			IDmmRepositoryHistoryDAO repositoryHistoryDAO) {
		this.repositoryHistoryDAO = repositoryHistoryDAO;
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

	/**
	 * @return the mailComponent
	 */
	public MailComponent getMailComponent() {
		return mailComponent;
	}

	/**
	 * @param mailComponent the mailComponent to set
	 */
	public void setMailComponent(MailComponent mailComponent) {
		this.mailComponent = mailComponent;
	}

	
	
}
