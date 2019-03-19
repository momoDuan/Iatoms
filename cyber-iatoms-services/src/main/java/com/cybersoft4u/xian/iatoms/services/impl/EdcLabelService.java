package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmEdcLabelDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcLabelFormDTO;
import com.cybersoft4u.xian.iatoms.services.IEdcLabelService;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmEdcLabelDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmEdcLabel;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmEdcLabelId;

import cafe.core.bean.Message;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.config.SystemConfigManager;
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

/**
 * Purpose:刷卡機標籤管理Service
 * @author NickLin 
 * @since  JDK 1.7
 * @date   2018/06/27
 * @MaintenancePersonnel CyberSoft
 */
public class EdcLabelService extends AtomicService implements IEdcLabelService {

	/**
	 * 系统log物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, EdcLabelService.class);
	
	/**
	 * 刷卡機標籤DAO
	 */
	private IDmmEdcLabelDAO dmmEdcLabelDAO;

	/**
	 * 上限數500
	 */
	private Integer limitNumber = 500;
	
	/**
	 * Constructor:無參數建構子
	 */
	public EdcLabelService() {
		super();
	}
	
	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcLabelService#init(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext init(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		EdcLabelFormDTO edcLabelFormDTO = null;
		try {
			edcLabelFormDTO = (EdcLabelFormDTO) sessionContext.getRequestParameter();
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(edcLabelFormDTO);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+".init():" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcLabelService#query(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext query(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		Message msg = null;
		try {
			EdcLabelFormDTO edcLabelFormDTO = (EdcLabelFormDTO) sessionContext.getRequestParameter();
			if (sessionContext != null){
				String queryMerchantCode = edcLabelFormDTO.getQueryMerchantCode();
				String queryDtid = edcLabelFormDTO.getQueryDtid();
				String queryStartDate = edcLabelFormDTO.getQueryStartDate();
				String queryEndDate = edcLabelFormDTO.getQueryEndDate();
				Integer pageIndex = edcLabelFormDTO.getPage();
				Integer pageSize = edcLabelFormDTO.getRows();
				String orderby = edcLabelFormDTO.getOrder();
				String sort = edcLabelFormDTO.getSort();
				Integer count = null;
				count = this.dmmEdcLabelDAO.count(queryMerchantCode, queryDtid, queryStartDate, queryEndDate);
				if (count.intValue() > 0) {
					//查詢結果List
					List<DmmEdcLabelDTO> edcLabelDTOs = this.dmmEdcLabelDAO.listBy(queryMerchantCode, queryDtid,
							queryStartDate, queryEndDate, pageSize, pageIndex, sort, orderby);
					edcLabelFormDTO.getPageNavigation().setRowCount(count.intValue());
					edcLabelFormDTO.setList(edcLabelDTOs);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				}
				sessionContext.setReturnMessage(msg);
				sessionContext.setResponseResult(edcLabelFormDTO);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".query() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".query() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcLabelService#upload(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	@Override
	public SessionContext upload(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		Message msg = null;
		DmmEdcLabel edcLabel = null;
		Transformer transformer = new SimpleDtoDmoTransformer();
		LogonUser logonUser = null;
		try {
			EdcLabelFormDTO edcLabelFormDTO = (EdcLabelFormDTO) sessionContext.getRequestParameter();
			logonUser = edcLabelFormDTO.getLogonUser();
			//獲得上傳文件
			MultipartFile uploadFiled = edcLabelFormDTO.getUploadFiled();
			//解析資料
			if(uploadFiled != null) {
				Map<String, List<String>> errorMap = new LinkedHashMap<String, List<String>>();
				String[] str = new String[1];
				List<DmmEdcLabelDTO> tempEdcLabelDTOs = this.checkUploadFiled(uploadFiled, errorMap, str);
				if (str[0] == null) {
					if (!CollectionUtils.isEmpty(errorMap)) {
						//寫入錯誤信息
						Map<String, String> map = this.saveErrorMsg(errorMap);
						edcLabelFormDTO.setErrorFileName(map.get(EdcLabelFormDTO.ERROR_FILE_NAME));
						edcLabelFormDTO.setTempFilePath(map.get(EdcLabelFormDTO.TEMP_FILE_PATH));
						msg = new Message(Message.STATUS.FAILURE);
					} else {
						for (DmmEdcLabelDTO edcLabelDTO : tempEdcLabelDTOs) {
							//如果DB有相同merchantCode 與dtid 的資料，該筆資料為修改；反之為新增 
							if (this.dmmEdcLabelDAO.isUpdate(edcLabelDTO.getMerchantCode(), edcLabelDTO.getDtid())) {
								DmmEdcLabelId dmmEdcLabelId = new DmmEdcLabelId(edcLabelDTO.getMerchantCode(), edcLabelDTO.getDtid());
								edcLabel = this.dmmEdcLabelDAO.findByPrimaryKey(DmmEdcLabel.class, dmmEdcLabelId);
								edcLabel.setMerchantType(edcLabelDTO.getMerchantType());
								edcLabel.setRelation(edcLabelDTO.getRelation());
								edcLabel.setStatus(edcLabelDTO.getStatus());
								edcLabel.setIp(edcLabelDTO.getIp());
								edcLabel.setUpdatedById(logonUser.getId());
								edcLabel.setUpdatedByName(logonUser.getName());
								edcLabel.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
								this.dmmEdcLabelDAO.getDaoSupport().saveOrUpdate(edcLabel);
								this.dmmEdcLabelDAO.getDaoSupport().flush();
							} else {
								DmmEdcLabelId dmmEdcLabelId = new DmmEdcLabelId(edcLabelDTO.getMerchantCode(), edcLabelDTO.getDtid());
								edcLabel = (DmmEdcLabel) transformer.transform(edcLabelDTO, new DmmEdcLabel());
								edcLabel.setId(dmmEdcLabelId);
								edcLabel.setCreatedById(logonUser.getId());
								edcLabel.setCreatedByName(logonUser.getName());
								edcLabel.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
								edcLabel.setUpdatedById(logonUser.getId());
								edcLabel.setUpdatedByName(logonUser.getName());
								edcLabel.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
								this.dmmEdcLabelDAO.getDaoSupport().saveOrUpdate(edcLabel);
								this.dmmEdcLabelDAO.getDaoSupport().flush();
							}
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPLOAD_SECCUSS_TION,new String[]{""});
						}
					}
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ABOUCHEMENT_FAILURE_FOR_OVER_LIMIT, new String[]{"" + limitNumber});
				}
				sessionContext.setResponseResult(edcLabelFormDTO);
				sessionContext.setReturnMessage(msg);
				Map map = new HashMap();
				map.put(edcLabelFormDTO.ERROR_FILE_NAME, edcLabelFormDTO.getErrorFileName());
				map.put(edcLabelFormDTO.TEMP_FILE_PATH, edcLabelFormDTO.getTempFilePath());
				if (Message.STATUS.SUCCESS.equals(msg.getStatus())) {
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				} else {
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				}
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".upload() DataAccess Exception : " + e, e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".upload() Exception : " + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcLabelService#delete(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext delete(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		Message msg = null;
		try {
			EdcLabelFormDTO edcLabelFormDTO = (EdcLabelFormDTO) sessionContext.getRequestParameter();
			String[] compositeKeys = edcLabelFormDTO.getCompositeKeys().split(IAtomsConstants.MARK_SEMICOLON);
			String merchantCode = null;
			String dtid = null;
			DmmEdcLabel edcLabel = null;
			boolean deletedFlag = false;
			for (String compositeKey : compositeKeys) {
				merchantCode = compositeKey.split(IAtomsConstants.MARK_SEPARATOR)[0];
				dtid = compositeKey.split(IAtomsConstants.MARK_SEPARATOR)[1];
				DmmEdcLabelId dmmEdcLabelId = new DmmEdcLabelId(merchantCode, dtid);
				edcLabel = this.dmmEdcLabelDAO.findByPrimaryKey(DmmEdcLabel.class, dmmEdcLabelId);
				if (edcLabel != null) {
					this.dmmEdcLabelDAO.getDaoSupport().delete(edcLabel);
					this.dmmEdcLabelDAO.getDaoSupport().flush();
					deletedFlag = true;
				} else {
					deletedFlag = false;
				}
			}
			if (deletedFlag) {
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[] { this.getMyName() });
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".delete() DataAccess Exception : " + e, e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".delete() Exception : " + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcLabelService#printEdcLabel(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext printEdcLabel(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		Message msg = null;
		try {
			EdcLabelFormDTO edcLabelFormDTO = (EdcLabelFormDTO) sessionContext.getRequestParameter();
			String[] compositeKeys = edcLabelFormDTO.getCompositeKeys().split(IAtomsConstants.MARK_SEMICOLON);
			String merchantCode = null;
			String dtid = null;
			DmmEdcLabelDTO edcLabelDTO = null;
			List<DmmEdcLabelDTO> edcLabelDTOs = new ArrayList<DmmEdcLabelDTO>();
			for (String compositeKey : compositeKeys) {
				merchantCode = compositeKey.split(IAtomsConstants.MARK_SEPARATOR)[0];
				dtid = compositeKey.split(IAtomsConstants.MARK_SEPARATOR)[1];
				edcLabelDTO = this.dmmEdcLabelDAO.getInfoByCompositeKey(merchantCode, dtid).get(0);
				//edcLabelDTO.setUrl("http://59.120.231.182:8080/cyber-iatoms-web/contactInfo.do");
				edcLabelDTOs.add(edcLabelDTO);
			}
			edcLabelFormDTO.setList(edcLabelDTOs);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS, new String[]{this.getMyName()});
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(edcLabelFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".printEdcLabel() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".printEdcLabel() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/**
	 * Purpose:檢查上傳文件
	 * @author NickLin
	 * @param uploadFiled：上傳文件
	 * @param errorMap：錯誤訊息
	 * @return List<DmmEdcLabelDTO>：返回一個DmmEdcLabelDTO對象
	 */
	private List<DmmEdcLabelDTO> checkUploadFiled(MultipartFile uploadFiled, Map<String, List<String>> errorMap, String[] str) {
		DmmEdcLabelDTO edcLabelDTO = null;
		List<DmmEdcLabelDTO> tempEdcLabelDTOs = new ArrayList<DmmEdcLabelDTO>();
		try {
			if (uploadFiled != null) {
				//錯誤訊息List
				List<String> errorList = new ArrayList<String>();
				InputStream inputStream = uploadFiled.getInputStream();
				Workbook workbook = null;
				Sheet sheet = null;
				Row row = null;
				int rowCount = 0;
				String fileName = uploadFiled.getOriginalFilename();
				String fileTxt = fileName.substring(fileName.lastIndexOf(IAtomsConstants.MARK_NO) + 1);
				if (IAtomsConstants.FILE_TXT_MSEXCEL.equals(fileTxt)) {
					//2003版本
					workbook = new HSSFWorkbook(inputStream);
				} else if (IAtomsConstants.FILE_TXT_MSEXCEL_X.equals(fileTxt)) {
					//2007版本
					workbook = new XSSFWorkbook(inputStream);
				}
				if (workbook != null) {
					sheet = workbook.getSheetAt(0);
				} else {
					errorList = new ArrayList<String>();
					String errorMsg = i18NUtil.getName(IAtomsMessageCode.FILE_FORMAT_ERROR);
					errorList.add(errorMsg);
					errorMap.put(String.valueOf(0), errorList);
					LOGGER.error("workbook is null >>>");
					throw new ServiceException();
				}
				String merchantCode = null;
				String merchantType = null;
				String dtid = null;
				String relation = null;
				String status = null;
				String ip = null;
				// 獲取行數
				rowCount = this.getExcelRealRowCount(sheet);
				int i;
				if (rowCount <= 1) {
					errorList = new ArrayList<String>();
					errorList.add(i18NUtil.getName(IAtomsMessageCode.IATOMS_MSG_NONE_DATA));
					errorMap.put(String.valueOf(0), errorList);
				} else if (rowCount > limitNumber + 1){
					str[0] = IAtomsMessageCode.ABOUCHEMENT_FAILURE_FOR_OVER_LIMIT;
				} else {
					for (i = 1; i < rowCount; i++) {
						//獲取行
						row = sheet.getRow(i);
						if (row == null) {
							continue;
						} 
						//設置設備序號的單元格屬性
						for(int j = 0; j < 6; j++){
							if (row.getCell(j) != null) {
								row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
							}
						}
						merchantCode = this.getCellFormatValue(row.getCell(0), false);
						merchantType = this.getCellFormatValue(row.getCell(1), false);
						dtid = this.getCellFormatValue(row.getCell(2), false);
						relation = this.getCellFormatValue(row.getCell(3), false);
						status = this.getCellFormatValue(row.getCell(4), false);
						ip = this.getCellFormatValue(row.getCell(5), false);
						this.check(merchantCode, i + 1, i18NUtil.getName(IAtomsConstants.FIELD_DMM_EDC_LABEL_MERCHANT_CODE), false, 20, errorList);
						this.check(merchantType, i + 1, i18NUtil.getName(IAtomsConstants.FIELD_DMM_EDC_LABEL_MERCHANT_TYPE), true, 10, errorList);
						this.check(dtid, i + 1, i18NUtil.getName(IAtomsConstants.FIELD_DMM_EDC_LABEL_DTID), false, 10, errorList);
						this.check(relation, i + 1, i18NUtil.getName(IAtomsConstants.FIELD_DMM_EDC_LABEL_RELATION), true, 10, errorList);
						this.check(status, i + 1, i18NUtil.getName(IAtomsConstants.FIELD_DMM_EDC_LABEL_STATUS), true, 10, errorList);
						this.check(ip, i + 1, i18NUtil.getName(IAtomsConstants.FIELD_DMM_EDC_LABEL_IP), true, 15, errorList);
						edcLabelDTO = new DmmEdcLabelDTO();
						edcLabelDTO.setMerchantCode(merchantCode);
						edcLabelDTO.setMerchantType(merchantType);
						edcLabelDTO.setDtid(dtid);
						edcLabelDTO.setRelation(relation);
						edcLabelDTO.setStatus(status);
						edcLabelDTO.setIp(ip);
						tempEdcLabelDTOs.add(edcLabelDTO);
					}
					
					if (!CollectionUtils.isEmpty(errorList)) {
						errorMap.put(String.valueOf(i), errorList);
					}
					//檢查從excel 匯入的資料有無重複
					/*if (CollectionUtils.isEmpty(errorList)) {
						for (DmmEdcLabelDTO dmmEdcLabelDTO : tempEdcLabelDTOs) {
							
						}
					}*/
					//從DB檢查是否有相同特店代號與DTID的資料
				}
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".checkUploadFiled() Exception : " + e, e);
		}
		return tempEdcLabelDTOs;
	}

	/**
	 * Purpose:求出真實的匯入行數（有值的）
	 * @author HermanWang
	 * @param sheet
	 * @return
	 */
	private int getExcelRealRowCount(Sheet sheet) {
		int rowCount = 0;
		if(sheet != null){
			//拿到sheet下第一行的行號
			int beginRow = sheet.getFirstRowNum();
			//拿到最後一行的行號
			int endRow = sheet.getLastRowNum();  
			int beginCell = 0;
			int endCell = 0;
			Row tempRow = null;
			Boolean emptyRow = false;
			//循環sheet裡面的所有行
			for (int i = beginRow; i <= endRow; i++) {  
				tempRow = sheet.getRow(i);
				emptyRow = false;
				if(tempRow != null){
					//拿到第一個cell
					beginCell = tempRow.getFirstCellNum();
					//拿到最後一個cell
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
	 * Purpose:獲取上傳的數據
	 * @author HermanWang
	 * @param cell:獲取上傳的單元格
	 * @param getTime:是否為時間格式
	 * @return String：單元數據格內容
	 */
	private String getCellFormatValue(Cell cell, boolean getTime) {
		String cellvalue = "";
		try {
			if (cell != null) {
				// 判断当前Cell的Type
				switch (cell.getCellType()) {
					// 如果当前Cell的Type为NUMERIC
					case Cell.CELL_TYPE_NUMERIC:
					case Cell.CELL_TYPE_FORMULA: {
						// 判断当前的cell是否为Date
						if (DateUtil.isCellDateFormatted(cell)) {
							SimpleDateFormat sdf = null;
							Date date = cell.getDateCellValue();
							if (getTime) {
								sdf = new SimpleDateFormat("HH:mm");
							}
							cellvalue = sdf.format(date);
						}else {
							// 取得当前Cell的数值
							cellvalue = String.valueOf(((cell.getNumericCellValue())));
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
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getCellFormatValue() Exception : " + e, e);
		}
		return cellvalue;
	}
	
	/**
	 * Purpose:檢查參數的長度或者是否為空
	 * @author HermanWang
	 * @param param：參數
	 * @param row：行號
	 * @param cell：列號
	 * @param isNull：是否可以為空，為空則不需檢查長度,
	 * @param length：允許最大長度
	 * @param errorList:錯誤消息集合
	 */
	private void check(String param, int row, String cell, boolean isNull, int length, List<String> errorList) {
		try {
			if (isNull) {
				//可以為空
				if (StringUtils.hasText(param) && param.trim().length() > length) {
					errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_LIMIT_LENGTH, new String[]{String.valueOf(row), cell, length+""}, null));
				}
			} else {
				if (!StringUtils.hasText(param)) {
					errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_IS_NOT_EMPTY, new String[]{String.valueOf(row), cell}, null));
				} else {
					if (param.trim().length() > length) {
						errorList.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX_LIMIT_LENGTH, new String[]{String.valueOf(row), cell,length+""}, null));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".check() Exception : " + e, e);
		}
	}
	
	/**
	 * Purpose::將錯誤訊息存入txt文檔
	 * @author HermanWang
	 * @param errorMap:錯誤信息集合
	 * @return String 文檔名
	 */
	private Map<String, String> saveErrorMsg(Map<String, List<String>> errorMap) {
		PrintWriter printWriter = null;
		String fileName = null; 
		Map<String, String> map = new HashMap<String, String>();
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
				//文件路徑
				String errorFilePath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH) + File.separator 
						+ yearMonthDay + File.separator + IAtomsConstants.UC_NO_DMM_03120 + File.separator + IAtomsConstants.PARAM_STRING_IMPORT;
				File filePath = new File(errorFilePath);
				if (!filePath.exists() || !filePath.isDirectory()) {
					filePath.mkdirs();
				}
				map.put(EdcLabelFormDTO.ERROR_FILE_NAME, fileName);
				map.put(EdcLabelFormDTO.TEMP_FILE_PATH, errorFilePath);
				File saveFile = new File(errorFilePath, fileName);
				printWriter = new PrintWriter(saveFile, IAtomsConstants.ENCODE_UTF_8);
				printWriter.print(stringBuffer.toString());
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".saveErrorMsg() Exception : " + e, e);
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
		return map;
	}
	
	/**
	 * @return the dmmEdcLabelDAO
	 */
	public IDmmEdcLabelDAO getDmmEdcLabelDAO() {
		return dmmEdcLabelDAO;
	}

	/**
	 * @param dmmEdcLabelDAO the dmmEdcLabelDAO to set
	 */
	public void setDmmEdcLabelDAO(IDmmEdcLabelDAO dmmEdcLabelDAO) {
		this.dmmEdcLabelDAO = dmmEdcLabelDAO;
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
}
