package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.config.SystemConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.parameter.IBaseParameterItemDefDAO;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO;
import com.cybersoft4u.xian.iatoms.services.IContractSlaService;
import com.cybersoft4u.xian.iatoms.services.dao.IContractDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractSlaDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimSla;
/**
 * Purpose:合約SLA設定Service 
 * @author Amanda Wang 
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel Amanda Wang
 */
public class ContractSlaService extends AtomicService implements IContractSlaService{
	/**
	 * 系统日志文件控件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, ContractSlaService.class);
	
	/**
	 * 合約sla設定DAO
	 */
	private IContractSlaDAO contractSlaDAO;
	
	/**
	 * 合約DAO
	 */
	private IContractDAO contractDAO;
	
	/**
	 * 基本參數DAO
	 */
	private IBaseParameterItemDefDAO baseParameterItemDefDAO;
	
	/**
	 * 常量24
	 */
	private Integer multipleNumber;
	
	/**
	 * 上限數500
	 */
	private Integer limitNumber;
	
	/**
	 * 配置是否集合
	 */
	private Map<String, Object> attributes;
	/**
	 * Constructor:無參構造函數
	 */
	public ContractSlaService() {
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractSlaService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		try {
			ContractSlaFormDTO contractSlaFormDTO = (ContractSlaFormDTO) sessionContext.getRequestParameter();
			String size = SystemConfigManager.getProperty(IAtomsConstants.SETTING, IAtomsConstants.UPLOAD_FILE_SIZE);
			if (!StringUtils.hasText(size)) {
				size = IAtomsConstants.PARAM_UPLOAD_FILE_SIZE;
			}
			int rate = Integer.valueOf(IAtomsConstants.PARAM_UPLOAD_CONVERSION_RATE);
			size = String.valueOf(Integer.valueOf(size) * rate);
			contractSlaFormDTO.setUploadFileSize(size);
			// 初始化成功提示消息
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(contractSlaFormDTO);
		} catch (Exception e) {
			LOGGER.error(".init() Exception : ", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractSlaService#query(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		try {
			ContractSlaFormDTO contractSlaFormDTO = (ContractSlaFormDTO) sessionContext.getRequestParameter();
			// 分頁查詢
			List<ContractSlaDTO> contractSlaDTOList = this.contractSlaDAO.listBy(contractSlaFormDTO.getQueryCustomerId(), contractSlaFormDTO.getQueryContractId(), contractSlaFormDTO.getQueryLocation(),
					contractSlaFormDTO.getQueryTicketType(), contractSlaFormDTO.getQueryImportance(), contractSlaFormDTO.getRows(), contractSlaFormDTO.getPage(), 
					contractSlaFormDTO.getSort(), contractSlaFormDTO.getOrder(), true);
			// 结果为空
			if (CollectionUtils.isEmpty(contractSlaDTOList)) {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND));
			} else {
				// 獲得總筆數
				int count = this.contractSlaDAO.count(contractSlaFormDTO.getQueryCustomerId(), contractSlaFormDTO.getQueryContractId(), contractSlaFormDTO.getQueryLocation(),
						contractSlaFormDTO.getQueryTicketType(), contractSlaFormDTO.getQueryImportance());
				// 设置总笔数
				contractSlaFormDTO.getPageNavigation().setRowCount(count);
				contractSlaFormDTO.setList(contractSlaDTOList);
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS));
			}
			sessionContext.setResponseResult(contractSlaFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(".query(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".query(SessionContext sessionContext) is failed!!!", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractSlaService#initEdit(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException {
		try {
			ContractSlaFormDTO contractSlaFormDTO = (ContractSlaFormDTO) sessionContext.getRequestParameter();
			String slaId = contractSlaFormDTO.getSlaId();
			ContractSlaDTO contractSlaDTO = new ContractSlaDTO();
			BimSla bimSla = null;
			Transformer transformer = null;
			// 通过主键查到这条数据
			bimSla = this.contractSlaDAO.findByPrimaryKey(BimSla.class, slaId);
			transformer = new SimpleDtoDmoTransformer();
			// DMO转DTO
			transformer.transform(bimSla, contractSlaDTO);
			// 设置用户
			contractSlaDTO.setCustomerId(contractSlaFormDTO.getCustomerId());
			contractSlaFormDTO.setContractSlaDTO(contractSlaDTO);
			sessionContext.setResponseResult(contractSlaFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(".initEdit(SessionContext sessionContext) is error", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".initEdit(SessionContext sessionContext) is failed!!!", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		} 
		return sessionContext;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractSlaService#save(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		Message msg = null ;
		ContractSlaFormDTO contractSlaFormDTO = (ContractSlaFormDTO) sessionContext.getRequestParameter();
		String slaId = contractSlaFormDTO.getSlaId();
		// 拿到当前登录者
		LogonUser logonUser = contractSlaFormDTO.getLogonUser();
		try {
			// 得到DTO 信息 
			ContractSlaDTO contractSlaDTO = contractSlaFormDTO.getContractSlaDTO();
			// 创建合约sla设定DMO对象
			BimSla bimSla = null;
			// 查重
			boolean isRepeat = false;
			isRepeat = this.contractSlaDAO.isRepeat(slaId, contractSlaDTO.getContractId(), contractSlaDTO.getTicketType(),
					contractSlaDTO.getLocation(), contractSlaDTO.getTicketMode());
			if(StringUtils.hasText(slaId)){
				if(isRepeat){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SLA_SAVE_FAILURE_FOR_REPEAT);
				} else {
					bimSla = this.contractSlaDAO.findByPrimaryKey(BimSla.class, slaId);
					bimSla.setContractId(contractSlaDTO.getContractId());
					bimSla.setTicketType(contractSlaDTO.getTicketType());
					bimSla.setLocation(contractSlaDTO.getLocation());
					bimSla.setTicketMode(contractSlaDTO.getTicketMode());
					// 设置编辑项
					bimSla.setIsWorkDay(contractSlaDTO.getIsWorkDay());
					bimSla.setIsThatDay(contractSlaDTO.getIsThatDay());
					bimSla.setThatDayTime(contractSlaDTO.getThatDayTime());
					bimSla.setResponseHour(contractSlaDTO.getResponseHour());
					bimSla.setResponseWarnning(contractSlaDTO.getResponseWarnning());
					bimSla.setComment(contractSlaDTO.getComment());
					bimSla.setArriveHour(contractSlaDTO.getArriveHour());
					bimSla.setArriveWarnning(contractSlaDTO.getArriveWarnning());
					bimSla.setCompleteHour(contractSlaDTO.getCompleteHour());
					bimSla.setCompleteWarnning(contractSlaDTO.getCompleteWarnning());
					bimSla.setUpdatedById(logonUser.getId());
					bimSla.setUpdatedByName(logonUser.getName());
					bimSla.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					// 编辑返回编辑成功信息
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()});
				}
			} else {
				if(!isRepeat){
					// 合約SLA的DMO
					bimSla = new BimSla();
					// DTO轉換為DMO
					Transformer transformer = new SimpleDtoDmoTransformer();
					transformer.transform(contractSlaDTO, bimSla);
					slaId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_SLA);
					bimSla.setSlaId(slaId);
					// 设置创建人员编号
					bimSla.setCreatedById(logonUser.getId());
					// 设置创建人员名称名称
					bimSla.setCreatedByName(logonUser.getName());
					// 设置创建日期
					bimSla.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					// 设置异动人员编号
					bimSla.setUpdatedById(logonUser.getId());
					// 设置异动人员名称
					bimSla.setUpdatedByName(logonUser.getName());
					// 设置异动日期
					bimSla.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					// 新增
					this.contractSlaDAO.getDaoSupport().saveOrUpdate(bimSla);
					this.contractSlaDAO.getDaoSupport().flush();
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SLA_SAVE_FAILURE_FOR_REPEAT);
				}
			}
			contractSlaFormDTO.setContractSlaDTO(contractSlaDTO);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(contractSlaFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(".save() DataAccess Exception : ", e.getMessage(), e);
			if(StringUtils.hasText(slaId)){
				throw new ServiceException(IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()}, e);
			} else {
				throw new ServiceException(IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()}, e);
			}
		} catch (Exception e) {
			LOGGER.error(".save() Exception : ", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractSlaService#delete(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		try {
			Message msg = null;
			ContractSlaFormDTO contractSlaFormDTO = (ContractSlaFormDTO) sessionContext.getRequestParameter();
			String slaId = contractSlaFormDTO.getSlaId();
			// 合約SLA的DMO
			BimSla bimSla = this.contractSlaDAO.findByPrimaryKey(BimSla.class, slaId);
			if(bimSla != null){
				// 刪除
				this.contractSlaDAO.getDaoSupport().delete(bimSla);
				this.contractSlaDAO.getDaoSupport().flush();
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(contractSlaFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(".delete(SessionContext sessionContext) is error", e.getMessage(), e);
			throw new ServiceException(CoreMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".delete(SessionContext sessionContext) is failed!!!", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractSlaService#copy(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext copy(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			Transformer transformer = new SimpleDtoDmoTransformer();
			ContractSlaFormDTO contractSlaFormDTO = (ContractSlaFormDTO) sessionContext.getRequestParameter();
			// 主键类DMO
		//	BimSlaId bimSlaId = null;
			String slaId = null;
			LogonUser logonUser = contractSlaFormDTO.getLogonUser();
			if(logonUser != null){
				// 客戶編號和合約編號
				String customerId = contractSlaFormDTO.getOriginalCustomerId();
				String contractId = contractSlaFormDTO.getOriginalContractId();
				// 查询要复制给的合約編號是否有sla信息
				List<ContractSlaDTO> contractSlaDTOList = this.contractSlaDAO.getSlaByCustomerAndContract(customerId, contractId);
				// 複製客戶編號和複製合約編號
				String copyCustomerId = contractSlaFormDTO.getCopyCustomerId();
				String copyContractId = contractSlaFormDTO.getCopyContractId();
				// 通过当前选择的複製客户与複製合约编号拿到有SLA信息的列表
				List<ContractSlaDTO> copyContractSlaDTOList = this.contractSlaDAO.getSlaByCustomerAndContract(copyCustomerId, copyContractId);
				// 设置标志
				boolean flag = false ;
				// 合约sla设定DMO
				BimSla bimSla = null;
				// 要複製給的合約編號無sla信息
				if((!CollectionUtils.isEmpty(copyContractSlaDTOList)) && (CollectionUtils.isEmpty(contractSlaDTOList))){
					flag = true;
				} else if((!CollectionUtils.isEmpty(copyContractSlaDTOList)) && (!CollectionUtils.isEmpty(contractSlaDTOList))){
					flag = this.contractSlaDAO.isCopyRepeat(contractId, copyContractId);
				}
				if(flag){
					for(ContractSlaDTO contractSlaDTO : copyContractSlaDTOList){
						// 新建DMO对象
						bimSla = new BimSla();
						transformer.transform(contractSlaDTO, bimSla);
						slaId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_SLA);
						bimSla.setSlaId(slaId);
						// 设置复制的合约编号
						bimSla.setContractId(contractId);
						// 设置创建人员编号
						bimSla.setCreatedById(logonUser.getId());
						// 设置创建人员名称名称
						bimSla.setCreatedByName(logonUser.getName());
						// 设置创建日期
						bimSla.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
						// 设置异动人员编号
						bimSla.setUpdatedById(logonUser.getId());
						// 设置异动人员名称
						bimSla.setUpdatedByName(logonUser.getName());
						// 设置异动日期
						bimSla.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						this.contractSlaDAO.getDaoSupport().saveOrUpdate(bimSla);
					}
					this.contractSlaDAO.getDaoSupport().flush();
					// 设置成功消息
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.COPY_SUCCESS, new String[]{this.getMyName()});
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SLA_COPY_FAILURE_FOR_REPEAT );
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT);
			}
			sessionContext.setResponseResult(contractSlaFormDTO);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error(".copy() DataAccess Exception : ", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".copy() Exception : ", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IContractSlaService#upload(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext upload(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		ContractSlaFormDTO contractSlaFormDTO = null;
		try {
			contractSlaFormDTO = (ContractSlaFormDTO) sessionContext.getRequestParameter();
			LogonUser logonUser = contractSlaFormDTO.getLogonUser();
			String slaId = null;
			//匯入的文件
			MultipartFile uploadFiled = contractSlaFormDTO.getUploadFiled();
			if(logonUser != null){
				if (uploadFiled != null) {
					Map<String, List<String>> errorMap = new LinkedHashMap<String, List<String>>();
					String[] str = new String[1];
					/*List<contractSlaDTO> contractSlaDTOList = this.checkUploadFiled(uploadFiled, errorMap, logonUser, str);*/
					List<BimSla> bimSlaList = this.checkUploadFiled(uploadFiled, errorMap, logonUser, str);
					if(str[0] == null){
						if (CollectionUtils.isEmpty(errorMap)) {
							if (!CollectionUtils.isEmpty(bimSlaList)) {
								slaId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_SLA);
								int i = 1;
								for (BimSla bimSla : bimSlaList) {
									bimSla.setSlaId(slaId + IAtomsConstants.MARK_MIDDLE_LINE.concat(String.valueOf(i++)));
									this.contractSlaDAO.getDaoSupport().saveOrUpdate(bimSla);
								}
								this.contractSlaDAO.getDaoSupport().flush();
								// 成功提示
								msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPLOAD_SECCUSS);
							}
						} else {
							Map<String ,String> map = this.saveErrorMsg(errorMap);
							//String fileName = this.saveErrorMsg(errorMap);
							contractSlaFormDTO.setErrorFileName(map.get(ContractSlaFormDTO.ERROR_FILE_NAME));
							contractSlaFormDTO.setTempFilePath(map.get(ContractSlaFormDTO.TEMP_FILE_PATH));
							// 错误状态
							msg = new Message(Message.STATUS.FAILURE);
						}
					} else {
						msg = new Message(Message.STATUS.FAILURE, str[0], new String[]{String.valueOf(limitNumber)});
					}
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT);
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(contractSlaFormDTO);
			Map map = new HashMap();
			// 存放生成错误文件的文件名
			map.put(ContractSlaFormDTO.ERROR_FILE_NAME, contractSlaFormDTO.getErrorFileName());
			map.put(ContractSlaFormDTO.TEMP_FILE_PATH, contractSlaFormDTO.getTempFilePath());
			if (Message.STATUS.SUCCESS.equals(msg.getStatus())) {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		} catch (DataAccessException e) {
			LOGGER.error(".upload() DataAccess Exception :", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".upload() Exception : ", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	/**
	 * Purpose:檢核上傳文件
	 * @author CrissZhang
	 * @param uploadFiled 
	 * @param errorMap
	 * @return List<BimSla>
	 */
	private List<BimSla> checkUploadFiled(MultipartFile uploadFiled, Map<String, List<String>> errorMap,
			LogonUser logonUser, String[] str) {
		// 返回結果 DMO對象集合
		List<BimSla> resultList = new ArrayList<BimSla>();
		// 臨時處理的slaDTO對象集合
		List<ContractSlaDTO> tempSlaList = new ArrayList<ContractSlaDTO>();
		// dto與dmo對象轉換集合
		Transformer transformer = new SimpleDtoDmoTransformer();
		ContractSlaDTO tempContractSlaDTO = null;
		try {
			if (uploadFiled != null) {
				// 错误消息List
				List<String> errorList = new ArrayList<String>() ;
				// 获取上传文件输入流
				InputStream inputStream = uploadFiled.getInputStream();
				Workbook workbook = null;
				Sheet sheet = null;
				Row row = null;
				int rowCount = 0;
				String fileName = uploadFiled.getOriginalFilename();
				// 文件後綴名
				String fileTxt = fileName.substring(fileName.lastIndexOf(IAtomsConstants.MARK_NO)+1);
				if (IAtomsConstants.FILE_TXT_MSEXCEL.equals(fileTxt)) {
					// 2003版本
					workbook = new HSSFWorkbook(inputStream);
				} else if (IAtomsConstants.FILE_TXT_MSEXCEL_X.equals(fileTxt)) {
					// 2007版本
					workbook = new XSSFWorkbook(inputStream);
				}
				if (workbook != null) {
					sheet = workbook.getSheetAt(0);
				} else {
					errorList = new ArrayList<String>();
					String errorMsg = i18NUtil.getName(IAtomsMessageCode.FILE_FORMAT_ERROR);
					errorList.add(errorMsg);
					errorMap.put(String.valueOf(0), errorList);
					LOGGER.error("workbook is null >>> ");
					throw new ServiceException();
				}
				// 錯誤消息
				String errorMsg = null;
				BimSla bimSla = null;
				//獲取行數
				//rowCount = sheet.getPhysicalNumberOfRows();
				rowCount = this.getExcelRealRowCount(sheet);
				if (rowCount <= 1) {
					errorList = new ArrayList<String>();
					errorList.add(i18NUtil.getName(IAtomsMessageCode.IATOMS_MSG_NONE_DATA));
					errorMap.put(String.valueOf(0), errorList);
				} else if ( rowCount > (limitNumber + 1) ){
					str[0] = IAtomsMessageCode.ABOUCHEMENT_FAILURE_FOR_OVER_LIMIT;
				} else {
					StringBuilder builder = new StringBuilder();
					// 合約編號
					String contractId = null;
					String contractCode = null;
					// 案件類別
					String ticketTypeName = null;
					String ticketType = null;
					// 區域
					String location = null;
					String locationName = null;
					// 案件類型
					String ticketMode = null;
					String ticketModeName = null;
					// 上班日
					String isWorkDay = null;
					// 当天件
					String isThatDay = null;
					// 当天件时间
					String thatDayTime = null;
					// 回應時效
					String strResponseHour = null;
					Double responseHour = null;
					// 回應警示
					String strResponseWarnning = null;
					Double responseWarnning = null;
					// 到場時效
					String strArriveHour = null;
					Double arriveHour = null;
					// 到場警示
					String strArriveWarnning = null;
					Double arriveWarnning = null;
					// 完修時效
					String strCompleteHour = null;
					Double completeHour = null;
					// 完修警示
					String strCompleteWarnning = null;
					Double completeWarnning = null;
					// 說明
					String comment = null;
					
					boolean isRepeat = false;
					// 合约编号列表
					List<Parameter> contractList = (List<Parameter>) this.contractDAO.getContractCodeList("",IAtomsConstants.PARAM_CONTRACT_STATUS_IN_EFFECT, Boolean.FALSE, "", "", Boolean.TRUE);
					// 拿到案件類別列表
					List<Parameter> ticketTypeList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.TICKET_TYPE.getCode(), null);
					// 拿到區域列表
					List<Parameter> locationList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.REGION.getCode(), null);
					// 拿到案件类型列表
					List<Parameter> ticketModeList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.TICKET_MODE.getCode(), null);
					// 是否列表
					List<Parameter> yesOrNoList = (List<Parameter>)this.attributes.get(IATOMS_PARAM_TYPE.YES_OR_NO.getCode());
					//獲取行
					for (int i = 1; i <= rowCount; i++) {
						errorList = new ArrayList<String>();
						row = sheet.getRow(i);
						if (isRowEmpty(row)) {
			                continue;
			            } else {
			            	// 取出各个单元格的值
			            	contractCode = this.getCellFormatValue(row.getCell(0), true);
							ticketTypeName = this.getCellFormatValue(row.getCell(1), true);
							locationName = this.getCellFormatValue(row.getCell(2), true);
							ticketModeName = this.getCellFormatValue(row.getCell(3), true);
							isWorkDay = this.getCellFormatValue(row.getCell(4), true);
							isThatDay = this.getCellFormatValue(row.getCell(5), true);
							thatDayTime = this.getCellFormatValue(row.getCell(6), true);
							strResponseHour = this.getCellFormatValue(row.getCell(7), true);
							strResponseWarnning = this.getCellFormatValue(row.getCell(8), true);
							strArriveHour = this.getCellFormatValue(row.getCell(9), true);
							strArriveWarnning = this.getCellFormatValue(row.getCell(10), true);
							strCompleteHour = this.getCellFormatValue(row.getCell(11), true);
							strCompleteWarnning = this.getCellFormatValue(row.getCell(12), true);
							comment = this.getCellFormatValue(row.getCell(13), true);
							//檢核長度，false为不允許為空true为可以为空
							this.checkCellLengthOrNull(contractCode, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_CONTRACT_ID), false, 200, errorList);
							this.checkCellLengthOrNull(ticketTypeName, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_TICKET_TYPE), false, 200, errorList);
							this.checkCellLengthOrNull(locationName, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_LOCATION), false, 200, errorList);
							this.checkCellLengthOrNull(ticketModeName, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_TICKET_MODE), false, 200, errorList);
							if(CollectionUtils.isEmpty(errorList)){
								if(!CollectionUtils.isEmpty(tempSlaList)){
									int j = 0;
									// 檔內資料判重
									for(ContractSlaDTO contractSlaDTO : tempSlaList){
										j++;
										if(locationName.equals(contractSlaDTO.getLocationName()) && contractCode.equals(contractSlaDTO.getContractCode())
												&& ticketTypeName.equals(contractSlaDTO.getTicketTypeName()) && ticketModeName.equals(contractSlaDTO.getTicketModeName())){
											builder.delete(0, builder.length());
											builder.append(IAtomsConstants.MARK_SEPARATOR).append(i18NUtil.getName(IAtomsMessageCode.REPEAT_FOR_DOCUMENT));
											builder.append(IAtomsConstants.MARK_SEPARATOR).append(i18NUtil.getName(IAtomsMessageCode.SLA_SAVE_FAILURE_FOR_REPEAT));
											errorMsg = builder.toString();
										//	errorMsg = IAtomsConstants.MARK_SEPARATOR + i18NUtil.getName(IAtomsMessageCode.REPEAT_FOR_DOCUMENT) + i18NUtil.getName(IAtomsMessageCode.SLA_SAVE_FAILURE_FOR_REPEAT);
											errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_ROW, new String[]{String.valueOf((i+1)), String.valueOf((j+1))}, null) + errorMsg);
											break;
										}
									}
								}
								if(CollectionUtils.isEmpty(errorList)){
									// 判斷合約編號是否存在
									contractId = this.getValueByName(contractList, contractCode, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_CONTRACT_ID), 
											i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_IS_NOT_EXIST, new String[]{String.valueOf((i+1)), 
													i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_CONTRACT_ID), i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_CONTRACT)}, null), errorList);
									// 拿到案件類別
									ticketType = this.getValueByName(ticketTypeList, ticketTypeName, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_TICKET_TYPE), 
											i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_IS_NOT_EXIST, new String[]{String.valueOf((i+1)), 
													i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_TICKET_TYPE), i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF)}, null), errorList);
									// 拿到區域編號
									location = this.getValueByName(locationList, locationName, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_LOCATION), 
											i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_IS_NOT_EXIST, new String[]{String.valueOf((i+1)), 
													i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_LOCATION), i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF)}, null), errorList);
									// 拿到案件类型
									ticketMode = this.getValueByName(ticketModeList, ticketModeName, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_TICKET_MODE), 
											i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_IS_NOT_EXIST, new String[]{String.valueOf((i+1)), 
													i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_TICKET_MODE), i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF)}, null), errorList);
									//判断数据库是否存在该条数据
									if((StringUtils.hasText(location)) && (StringUtils.hasText(contractId)) && (StringUtils.hasText(ticketType)) && (StringUtils.hasText(ticketMode))){
										// 資料庫數據判重
										isRepeat = this.contractSlaDAO.isRepeat(IAtomsConstants.MARK_EMPTY_STRING, contractId, ticketType, location, ticketMode);
										if(isRepeat){
											builder.delete(0, builder.length());
											builder.append(IAtomsConstants.MARK_SEPARATOR).append(i18NUtil.getName(IAtomsMessageCode.REPEAT_FOR_DATA_BASE));
											builder.append(IAtomsConstants.MARK_SEPARATOR).append(i18NUtil.getName(IAtomsMessageCode.SLA_SAVE_FAILURE_FOR_REPEAT));
											errorMsg = builder.toString();
									//		errorMsg = IAtomsConstants.MARK_SEPARATOR + i18NUtil.getName(IAtomsMessageCode.CONTRACT_SLA_REPEAT) + i18NUtil.getName(IAtomsMessageCode.SLA_SAVE_FAILURE_FOR_REPEAT);;
											errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i+1))}, null) + errorMsg);
										}
									}
								}
							}
							if(CollectionUtils.isEmpty(errorList)){
								this.checkCellLengthOrNull(comment, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMMENT), true, 200, errorList);
								// 判斷輸入的上班日
/*								if(!IAtomsConstants.YES.equals(isWorkDay) && !IAtomsConstants.NO.equals(isWorkDay)){
									errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_FORMAT_ERROR, new String[]{String.valueOf((i+1)), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_IS_WORK_DAY)}, null));
								} */
								if(StringUtils.hasText(isWorkDay)){
									isWorkDay = this.getValueByName(yesOrNoList, isWorkDay, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_IS_WORK_DAY), 
											i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_IS_NOT_EXIST, new String[]{String.valueOf((i+1)), 
													i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_IS_WORK_DAY), i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF)}, null), errorList);
								} else {
									this.checkCellLengthOrNull(isWorkDay, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_IS_WORK_DAY), false, 1, errorList);
								}
								// 判斷輸入的当天件
								/*if(!IAtomsConstants.YES.equals(isThatDay) && !IAtomsConstants.NO.equals(isThatDay)){
									errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_FORMAT_ERROR, new String[]{String.valueOf((i+1)), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_IS_THAT_DAY)}, null));
								} */
								if(StringUtils.hasText(isThatDay)){
									isThatDay = this.getValueByName(yesOrNoList, isThatDay, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_IS_THAT_DAY), 
											i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_IS_NOT_EXIST, new String[]{String.valueOf((i+1)), 
													i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_IS_THAT_DAY), i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF)}, null), errorList);
								} else {
									this.checkCellLengthOrNull(isThatDay, (i+1), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_IS_THAT_DAY), false, 1, errorList);
								}
								// 判断当天件建案时间
								if(IAtomsConstants.YES.equals(isThatDay)){
									this.valiDataTime(thatDayTime, i + 1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_THAT_DAY_TIME), errorList);
								} 
								// 判斷回應時效
								if (this.valiDataNum(strResponseHour, i + 1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_RESPONSE_HOUR), errorList, true)) {
									if(StringUtils.hasText(strResponseHour)){
										responseHour = Double.parseDouble(strResponseHour);
									}
								}
								// 判斷回應警示
								if (this.valiDataNum(strResponseWarnning, i + 1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_RESPONSE_WARNNING), errorList, true)) {
									if(StringUtils.hasText(strResponseWarnning)){
										responseWarnning = Double.parseDouble(strResponseWarnning);
									}
									if((responseHour != null) && (responseWarnning != null)){
										if(responseWarnning > responseHour){
											errorMsg = i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_MORE_THEN,new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_RESPONSE_WARNNING),i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_RESPONSE_HOUR)},null);
											errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i+1))}, null) + IAtomsConstants.MARK_SEPARATOR + errorMsg);
											//errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_FORMAT_ERROR, new String[]{String.valueOf((i+1)), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_RESPONSE_WARNNING)}, null));
										}
									}
								}
								// 判斷到場時效
								if (this.valiDataNum(strArriveHour, i + 1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_HOUR), errorList, false)) {
									arriveHour = Double.parseDouble(strArriveHour);
									if(IAtomsConstants.YES.equals(isThatDay)){
										if(arriveHour % multipleNumber != 0){
											//errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_FORMAT_ERROR, new String[]{String.valueOf((i+1)), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_HOUR)}, null));
											errorMsg = i18NUtil.getName(IAtomsMessageCode.MULTIPLE_OF_TWENTY_FOUR,new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_HOUR)},null);
											errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i+1))}, null) + IAtomsConstants.MARK_SEPARATOR + errorMsg);
										}
									}
								}
								// 判斷到場警示
								if (this.valiDataNum(strArriveWarnning, i + 1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_WARNNING), errorList, false)) {
									arriveWarnning = Double.parseDouble(strArriveWarnning);
									if((arriveHour != null)&& (arriveWarnning != null)){
										if(arriveWarnning > arriveHour){
											errorMsg = i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_MORE_THEN,new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_WARNNING),i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_HOUR)},null);
											errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i+1))}, null) + IAtomsConstants.MARK_SEPARATOR + errorMsg);
											//errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_FORMAT_ERROR, new String[]{String.valueOf((i+1)), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_ARRIVE_WARNNING)}, null));
										}
									}
								}
								// 判斷完成時效
								if (this.valiDataNum(strCompleteHour, i + 1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_HOUR), errorList, false)) {
									completeHour = Double.parseDouble(strCompleteHour);
									if(IAtomsConstants.YES.equals(isThatDay)){
										if(completeHour % multipleNumber != 0){
											//errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_FORMAT_ERROR, new String[]{String.valueOf((i+1)), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_HOUR)}, null));
											errorMsg = i18NUtil.getName(IAtomsMessageCode.MULTIPLE_OF_TWENTY_FOUR,new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_HOUR)},null);
											errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i+1))}, null) + IAtomsConstants.MARK_SEPARATOR + errorMsg);
										}
									} 
								}
								// 判斷完成警示
								if (this.valiDataNum(strCompleteWarnning, i + 1, i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_WARNNING), errorList, false)) {
									completeWarnning = Double.parseDouble(strCompleteWarnning);
									if((completeHour != null) && (completeWarnning != null)){
										if(completeWarnning > completeHour){
											//errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_FORMAT_ERROR, new String[]{String.valueOf((i+1)), i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_WARNNING)}, null));
											errorMsg = i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_MORE_THEN,new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_WARNNING),i18NUtil.getName(IAtomsConstants.FIELD_BIM_SLA_COMPLETE_HOUR)},null);
											errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i+1))}, null) + IAtomsConstants.MARK_SEPARATOR + errorMsg);
										}
									}
								}
								tempContractSlaDTO = new ContractSlaDTO();
								tempContractSlaDTO.setContractId(contractId);
								tempContractSlaDTO.setContractCode(contractCode);
								tempContractSlaDTO.setTicketType(ticketType);
								tempContractSlaDTO.setTicketTypeName(ticketTypeName);
								tempContractSlaDTO.setTicketMode(ticketMode);
								tempContractSlaDTO.setTicketModeName(ticketModeName);
								tempContractSlaDTO.setLocation(location);
								tempContractSlaDTO.setLocationName(locationName);
								tempContractSlaDTO.setIsWorkDay(isWorkDay);
								tempContractSlaDTO.setIsThatDay(isThatDay);
								if(StringUtils.hasText(isThatDay) && IAtomsConstants.YES.equals(isThatDay)){
									tempContractSlaDTO.setThatDayTime(thatDayTime);
								}
								tempContractSlaDTO.setResponseHour(responseHour);
								tempContractSlaDTO.setResponseWarnning(responseWarnning);
								tempContractSlaDTO.setArriveHour(arriveHour);
								tempContractSlaDTO.setArriveWarnning(arriveWarnning);
								tempContractSlaDTO.setCompleteHour(completeHour);
								tempContractSlaDTO.setCompleteWarnning(completeWarnning);
								tempContractSlaDTO.setComment(comment);
								// 设置创建人员编号
								tempContractSlaDTO.setCreatedById(logonUser.getId());
								// 设置创建人员名称名称
								tempContractSlaDTO.setCreatedByName(logonUser.getName());
								// 设置创建日期
								tempContractSlaDTO.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
								// 设置异动人员编号
								tempContractSlaDTO.setUpdatedById(logonUser.getId());
								// 设置异动人员名称
								tempContractSlaDTO.setUpdatedByName(logonUser.getName());
								// 设置异动日期
								tempContractSlaDTO.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
								tempSlaList.add(tempContractSlaDTO);
							}
							// 無誤，保存	
							if (CollectionUtils.isEmpty(errorList)) {
								bimSla = (BimSla) transformer.transform(tempContractSlaDTO, new BimSla());
								resultList.add(bimSla);
							} else {
								errorMap.put(String.valueOf(i), errorList);
							}
						}
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(".checkUploadFiled() DataAccess Exception : ", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		} catch (Exception e) {
			LOGGER.error(".checkUploadFiled() Exception : ", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return resultList;
	}
	/**
	 * Purpose:獲取excel表格真實行數
	 * @author CrissZhang
	 * @param sheet ： 傳入參數sheet
	 * @return int : 返回行數
	 */
	private int getExcelRealRowCount(Sheet sheet) {
		int rowCount = 0;
		if(sheet != null){
			int beginRow = sheet.getFirstRowNum();  
		    int endRow = sheet.getLastRowNum();  
		    int beginCell = 0;
		    int endCell = 0;
		    Row tempRow = null;
		    Boolean emptyRow = false;
		    for (int i = beginRow; i <= endRow; i++) {  
		    	tempRow = sheet.getRow(i);
		    	emptyRow = false;
		    	if(tempRow != null){
		    		beginCell = tempRow.getFirstCellNum();
			    	endCell = tempRow.getLastCellNum();
			    	for(int j = beginCell; j <= endCell; j++){
				        if (!StringUtils.hasText(this.getCellFormatValue(tempRow.getCell(j), true))) {  
				            continue;  
				        } else {
				        	emptyRow = true;
				        	break;
				        }
			    	}
			    	if(emptyRow){
			    		rowCount ++;
			    	} else {
			    		break;
			    	}
		    	}
		    }  
		}
		return rowCount;
	}
	/**
	 * Purpose:判斷Row是否為空
	 * @author CrissZhang
	 * @return Boolean
	 */
	private Boolean isRowEmpty(Row row){
		Boolean flag = true;
		if(row != null){
		  int beginCell = row.getFirstCellNum();
		  int endCell = row.getLastCellNum();
		  for(int i = beginCell; i <= endCell; i++){
		        if (!StringUtils.hasText(this.getCellFormatValue(row.getCell(i), true))) {  
		            continue;  
		        } else {
		        	flag = false;
		        	break;
		        }
	    	}
		} 
		return flag;
	}
	
	
	
	/**
	 * Purpose:通过name值得到value值，若得不到給出錯誤信息
	 * @author CrissZhang
	 * @param parameterList:下拉框列表
	 * @param name ：name值
	 * @return String
	 */
	private String getValueByName(List<Parameter> parameterList, String name, int row, String cell, String errorMsg, List<String> errorList){
		String value = null;
		if(StringUtils.hasText(name)){
			// 错误信息
			for (Parameter param : parameterList){
				if((param.getName()).equals(name)){
					value = (String) param.getValue();
					break;
				}
			}
		}
		// 没有得到相应的value值给出提示信息
		if(!StringUtils.hasText(value)){
			errorList.add(errorMsg);
		}
		return value;
	}
	/**
	 * Purpose:依據Cell類型獲取數據
	 * @author CrissZhang
	 * @param getTime:时间格式
	 * @param cell:取得Excel cell單元值
	 * @return String:单元格数据内容
	 */
	private String getCellFormatValue(Cell cell, boolean getTime) {
	    String cellvalue = "";
	    if (cell != null) {
	        // 判断当前Cell的Type
	        switch (cell.getCellType()) {
	            // 如果当前Cell的Type为NUMERIC
	            case Cell.CELL_TYPE_NUMERIC:
	            case Cell.CELL_TYPE_FORMULA: {
	                // 判断当前的cell是否为Date
	                if (DateUtil.isCellDateFormatted(cell)) {
	                	Date date = cell.getDateCellValue();
	                	if (getTime) {
	                		cellvalue = DateTimeUtils.toString(date, IAtomsConstants.PARAM_HOUR_MINUTE_DATE_FORMAT);
	                	} else {
	                		cellvalue = DateTimeUtils.toString(date, DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
	                	}
	                }else {
	                    // 取得当前Cell的数值
	                    cellvalue = String.valueOf((((Double)cell.getNumericCellValue())));
	                    if (StringUtils.hasText(cellvalue)) {
	                    	String[] cellValues = cellvalue.split("\\.");
	                    	if (IAtomsConstants.LEAVE_CASE_STATUS_ZERO.equals(cellValues[1])) {
	                    		cellvalue = cellValues[0];
	                    	}
	                    }
	                }
	                break;
	            }
	            case Cell.CELL_TYPE_STRING:
	                cellvalue = cell.getRichStringCellValue().getString();
	                break;
	            case Cell.CELL_TYPE_BOOLEAN:
	            	cellvalue = String.valueOf(cell.getBooleanCellValue());
	    		    break;
	    		case Cell.CELL_TYPE_BLANK:
	    			cellvalue = "";
	    		    break;
	            default:
	                cellvalue = " ";
	        }
	    } else {
	        cellvalue = "";
	    }
	    return cellvalue;
	}
	/**
	 * Purpose:数字检验
	 * @author CrissZhang
	 * @param num 要检验数字字符串
	 * @param row 行数
	 * @param cell 列数
	 * @param errorList 错误信息列表
	 * @param allowNull 字段是否可以为空
	 * @return boolean
	 */
	private boolean valiDataNum(String num, int row, String cell, List<String> errorList, boolean allowNull){
		boolean result = true;
		if (StringUtils.hasText(num)) {
			if (!num.matches("^[1-9]\\d{0,4}$")) {
				errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_FORMAT_ERROR, new String[]{String.valueOf(row), cell}, null));
				result =false;
			}
		} else if (!allowNull) {
			errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_IS_NOT_EMPTY, new String[]{String.valueOf(row), cell}, null));
			result = false;
		}
		return result;
	}
	/**
	 * Purpose:判断时间格式是否正确
	 * @author CrissZhang
	 * @param num 要检验数字字符串
	 * @param row 行数
	 * @param cell 列数
	 * @param errorList 错误信息列表
	 * @return boolean
	 */
	private boolean valiDataTime(String num, int row, String cell, List<String> errorList){
		boolean result = true;
		if (StringUtils.hasText(num)) {
			if (!num.matches("^([0-1]{1}[0-9]|2[0-3]):([0-5][0-9])$")) {
				errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_FORMAT_ERROR, new String[]{String.valueOf(row), cell}, null));
				result =false;
			} 
		} else {
			errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_IS_NOT_EMPTY, new String[]{String.valueOf(row), cell}, null));
			result =false;
		}
		return result;
	}
	/**
	 * Purpose:檢查參數的長度或者是否為空
	 * @author CrissZhang
	 * @param param 參數
	 * @param row 行號
	 * @param cell 列號
	 * @param isNull 是否可以為空，為空則不需檢查長度,
	 * @param length 允許的最大長度
	 * @param errorList：錯誤消息集合
	 * @return void
	 */
	private void checkCellLengthOrNull(String param,int row,String cell,boolean isNull,int length, List<String> errorList) {
		//可以為空
		if (isNull) {
			if (StringUtils.hasText(param) && param.trim().length() > length) {
				errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_LIMIT_LENGTH, new String[]{String.valueOf(row), cell, String.valueOf(length)}, null));
			}
		} else {
			if (!StringUtils.hasText(param)) {
				errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_IS_NOT_EMPTY, new String[]{String.valueOf(row), cell}, null));
			} else {
				if (param.trim().length() > length) {
					errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_LIMIT_LENGTH, new String[]{String.valueOf(row), cell, String.valueOf(length)}, null));
				}
			}
		}
	}
	/**
	 * Purpose:將錯誤訊息存入txt文檔
	 * @author CrissZhang
	 * @param errorMap：錯誤信息集合
	 * @return String 文檔名
	 */
	private Map<String, String> saveErrorMsg(Map<String, List<String>> errorMap) {
		Map<String, String> map = new HashMap<String, String>();
		PrintWriter printWriter = null;
		String fileName = null; 
		try {
			if (!CollectionUtils.isEmpty(errorMap)) {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(i18NUtil.getName(IAtomsMessageCode.PARAM_ERROR_INFORMATION) + IAtomsConstants.MARK_ENTER);
				Set<String> keySet = errorMap.keySet();
				Iterator<String> iterator = keySet.iterator();
				String key = null;
				List<String> errorList = null;
				while (iterator.hasNext()) {
					key = iterator.next();
					errorList = errorMap.get(key);
					if (!CollectionUtils.isEmpty(errorList)) {
						for (String errorMsg : errorList) {
							stringBuffer.append(errorMsg + IAtomsConstants.MARK_ENTER);
						}
					}
				}
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				//錯誤信息文件名
				fileName = UUID.randomUUID().toString() + IAtomsConstants.MARK_NO + IAtomsConstants.PARAM_FILE_SUFFIX_TXT;
			//	fileName = IAtomsConstants.UPLOAD_ERROR_MESSAGE_FILE_NAME;
				//文件路徑
				String errorFilePath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH) + File.separator 
						+ yearMonthDay + File.separator + IAtomsConstants.UC_NO_BIM_02040 + File.separator + IAtomsConstants.PARAM_STRING_IMPORT;
			//	String errorFilePath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.SLA_TEMP_PATH);

				File filePath = new File(errorFilePath);
				if (!filePath.exists() || !filePath.isDirectory()) {
					filePath.mkdirs();
				}
				map.put(ContractSlaFormDTO.ERROR_FILE_NAME, fileName);
				map.put(ContractSlaFormDTO.TEMP_FILE_PATH, errorFilePath);
				File saveFile = new File(filePath, fileName);
				printWriter = new PrintWriter(saveFile, IAtomsConstants.ENCODE_UTF_8);
				printWriter.print(stringBuffer.toString());
			}
		} catch (Exception e) {
			LOGGER.error("saveEorrorMsg() Error -->", e.getMessage(), e);
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
		return map;
	}
	
	/**
	 * @return the contractSlaDAO
	 */
	public IContractSlaDAO getContractSlaDAO() {
		return contractSlaDAO;
	}

	/**
	 * @param contractSlaDAO the contractSlaDAO to set
	 */
	public void setContractSlaDAO(IContractSlaDAO contractSlaDAO) {
		this.contractSlaDAO = contractSlaDAO;
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
	 * @return the baseParameterItemDefDAO
	 */
	public IBaseParameterItemDefDAO getBaseParameterItemDefDAO() {
		return baseParameterItemDefDAO;
	}

	/**
	 * @param baseParameterItemDefDAO the baseParameterItemDefDAO to set
	 */
	public void setBaseParameterItemDefDAO(
			IBaseParameterItemDefDAO baseParameterItemDefDAO) {
		this.baseParameterItemDefDAO = baseParameterItemDefDAO;
	}

	/**
	 * @return the multipleNumber
	 */
	public Integer getMultipleNumber() {
		return multipleNumber;
	}

	/**
	 * @param multipleNumber the multipleNumber to set
	 */
	public void setMultipleNumber(Integer multipleNumber) {
		this.multipleNumber = multipleNumber;
	}

	/**
	 * @return the limitNumber
	 */
	public Integer getLimitNumber() {
		return limitNumber;
	}

	/**
	 * @param limitNumber the limitNumber to set
	 */
	public void setLimitNumber(Integer limitNumber) {
		this.limitNumber = limitNumber;
	}

	/**
	 * @return the attributes
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

}
