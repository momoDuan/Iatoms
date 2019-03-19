package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import cafe.core.bean.dto.parameter.BaseParameterItemDefDTO;
import cafe.core.config.GenericConfigManager;
import cafe.core.config.SystemConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.parameter.IBaseParameterItemDefDAO;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.services.IIAtomsBatchService;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetInInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IPasswordSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISystemLogDAO;
/**
 * Purpose: 批次報表
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/5/26
 * @MaintenancePersonnel HermanWang
 */
public class IAtomsBatchService  extends AtomicService implements IIAtomsBatchService{
	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, IAtomsBatchService.class);
	
	/**
	 * 庫存歷史當
	 */
	private IDmmRepositoryHistoryDAO dmmRepositoryHistoryDAO;
	/**
	 * 基本參數DAO
	 */
	private IBaseParameterItemDefDAO baseParameterItemDefDAO;
	/**
	 * SRM_案件處理資料檔DAO
	 */
	private ISrmCaseHandleInfoDAO srmCaseHandleInfoDAO;
	/**
	 * 系统日志DAO
	 */
	private ISystemLogDAO systemLogDAO;
	/**
	 * 密碼原則設定DAO
	 */
	private IPasswordSettingDAO passwordSettingDAO;
	/**
	 * 庫存歷史月檔DAO
	 */
	private IDmmRepositoryHistoryMonthlyDAO dmmRepositoryHistoryMonthlyDAO;
	/**
	 * 設備入庫單主檔dao
	 */
	private IAssetInInfoDAO assetInInfoDAO;
	/**
	 * 設備轉倉主檔DAO
	 */
	private IDmmAssetTransInfoDAO assetTransInfoDAO;
	/**
	 * 構造器
	 */
	public IAtomsBatchService() {
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IIAtomsBatchService#copyAssetHistoryToMonthTable()
	 */
	@Override
	public void copyAssetHistoryToMonthTable() throws ServiceException {
		try {
			LOGGER.info(this.getClass().getSimpleName() + "iAtoms批次服務-設備庫存資料複製開始。。。");
			//SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM");
			String complete = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMM);
			//Date completeDate = DateTimeUtils.addCalendar(DateTimeUtils.getCurrentDate(), 0, 0, 0);
			//String complete = sf.format(completeDate);
			//String textName = complete.replace(IAtomsConstants.MARK_BACKSLASH, IAtomsConstants.MARK_EMPTY_STRING).concat(IAtomsConstants.MARK_TXT);
			String textName = complete.concat(IAtomsConstants.MARK_TXT);
			String path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.BATCH_FLAG_PATH);
			File file_path=new File(path);
			boolean isExcuted = false;
			String isHis = IAtomsConstants.MARK_EMPTY_STRING; 
			String fileYYYYMMName = IAtomsConstants.MARK_EMPTY_STRING; 
			//當前小時
			int hour = DateTimeUtils.getCurrentTime().getHours();
			//判斷是不是當月第一次進入
			if(hour < Integer.valueOf(1)) {
				isHis = IAtomsConstants.NO;
			} else {
				isHis = IAtomsConstants.YES;
			}
			//判斷儲存路徑是否存在，若不存在，則重新新建
    		if (!file_path.exists() || !file_path.isDirectory()) {
    			file_path.mkdirs();
    		}
    		//拿到目錄下所有的子文件
    		String[] files = new File(path).list();
			if(files.length == 0) {
				//若沒有任何文件，執行設備資料複製功能 在標記文件目錄，修改文件名為當前年月YYYYMM.txt（如果沒有文件則新增文件）
				this.dmmRepositoryHistoryDAO.copyToMonthTable(isHis);
				FileUtils.contentToTxt(path + File.separator + textName, IAtomsConstants.MARK_EMPTY_STRING);
			} else {
				//循環所有的子文件
				for (String file : files) {
					String fileName = file.substring(0,file.length()-4);
					//判斷名稱是否符合YYYYMM/.txt的格式
					isExcuted = checkYYYYMM(fileName);
					if(isExcuted) {
						fileYYYYMMName = file;
					}
				}
				//如果符合yyyymm的格式，驗證是不是當月
				if(isExcuted) {
					//如果文件名稱中YYYY（年份），MM（月份）不是當前月份，則執行設備資料複製功能
					if(!fileYYYYMMName.equals(textName)) {
						this.dmmRepositoryHistoryDAO.copyToMonthTable(isHis);
						String toFileName = path + File.separator + textName;
						//开始更名
						File srcDir = new File(path + File.separator + fileYYYYMMName);  
						srcDir.renameTo(new File(toFileName)); 
					} 
				}
			}
			LOGGER.info(this.getClass().getSimpleName() + "iAtoms批次服務-設備庫存資料複製完成");
		} catch (DataAccessException e) {
			LOGGER.error("copyAssetHistoryToMonthTable() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("copyAssetHistoryToMonthTable() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IIAtomsBatchService#cleanTempFiles()
	 */
	@Override
	public void cleanTempFiles() throws ServiceException {
		try {
			LOGGER.info(this.getClass().getSimpleName() + "iAtoms批次服務-清理臨時目錄文件開始。。。");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
			Date completeDate = DateTimeUtils.addCalendar(DateTimeUtils.getCurrentDate(), 0, 0, -1);
			Date currentDate = DateTimeUtils.addCalendar(DateTimeUtils.getCurrentDate(), 0, 0, 0);
			String complete = sf.format(completeDate);
			String current = sf.format(currentDate);
			String textName = complete.replace(IAtomsConstants.MARK_BACKSLASH, IAtomsConstants.MARK_EMPTY_STRING);
			String currentTextName = current.replace(IAtomsConstants.MARK_BACKSLASH, IAtomsConstants.MARK_EMPTY_STRING);
			String path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
			File file_path=new File(path);
			//判斷儲存路徑是否存在，若不存在，則重新新建
    		if (!file_path.exists() || !file_path.isDirectory()) {
    			file_path.mkdirs();
    		}
    		//拿到目錄下所有的子文件
    		String[] files = new File(path).list();
    		for (String filesName : files) {
    			//文件名稱不等於昨天或者今天。刪除
				if(!(textName.equals(filesName) || currentTextName.equals(filesName))){
					delAllFile(path.concat(File.separator).concat(filesName));
					FileUtils.removeFile(path.concat(File.separator).concat(filesName));
				}
			}
			LOGGER.info(this.getClass().getSimpleName() + "iAtoms批次服務-清理臨時目錄文件完成");
		} catch (DataAccessException e) {
			LOGGER.error("cleanTempFiles() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("cleanTempFiles() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}

	
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IIAtomsBatchService#transferCaseToHistory()
	 */
	@Override
	public void transferCaseToHistory() throws ServiceException {
		Date moveDate = null;
		try{
			// 案件轉移基礎參數配置dto
			BaseParameterItemDefDTO baseParameterItemDefDTO =  this.baseParameterItemDefDAO.getParameterItemByValue(IATOMS_PARAM_TYPE.SYSTEM_LIMIT.getCode(), null, IAtomsConstants.PARAM_CASE_TRANSFER_VALUE);
			// 轉移期限(月份)
			int transferMonth = 0;
			if (baseParameterItemDefDTO != null && StringUtils.hasText(baseParameterItemDefDTO.getTextField1())) {
				transferMonth = Integer.parseInt(baseParameterItemDefDTO.getTextField1());
			}
			if (transferMonth != 0) {
				LOGGER.debug("transferCaseToHistory", "parameter:transferMonth=", transferMonth);
				// 獲取當天的0點0分0秒
				Date tempDate = DateTimeUtils.addCalendar(DateTimeUtils.getCurrentDate(), 0, 0, 0);
				// 當天的0點0分0秒減去轉移期限(月份)計算出最早的結案時間
				moveDate = DateTimeUtils.addCalendar(tempDate, 0, -transferMonth, 0);
				if (moveDate != null) {
					LOGGER.debug("transferCaseToHistory", "parameter:moveDate=", moveDate.toString());
					// 將最早結案時間之前的案件資料移動到案件歷史檔中
					this.srmCaseHandleInfoDAO.moveCaseToHistroy(moveDate);
					LOGGER.debug("transferCaseToHistory", "DO moveCaseToHistroy() SUCCESS ");
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".transferCaseToHistory() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".transferCaseToHistory() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	
	/**
	 * 删除文件夹里面的所有文件
	 * @param path
	 *String 文件夹路径 如 c:/fqf
	 */
	public void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	/**
	 * 删除文件夹
	 * @param filePathAndName
	 * String 文件夹路径及名称 如c:/fqf
	 * @param fileContent
	 * String
	 * @return boolean
	 */
	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹

		} catch (Exception e) {
			System.out.println("删除文件夹操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * Purpose:檢查yyyyMM格式
	 * @author HermanWang
	 * @param yyyyMM:參數
	 */
	private boolean checkYYYYMM(String yyyyMM) {
		boolean isExcuted = false;
		try {
			if(StringUtils.hasText(yyyyMM)){
				String check = "^\\d{4}(0[1-9]|1[0-2])$";
				if(yyyyMM.matches(check)){
					isExcuted = true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception checkYYYYMM()---->"+ e, e);
		}
		return isExcuted;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IIAtomsBatchService#dealSystemLimit()
	 */
	@Override
	public void dealSystemLimit() throws ServiceException {
		try{
			// 轉移當前案件信息
			this.transferCaseToHistory();
			// 刪除密碼歷史檔
			this.deletePwdHistory();
			// 轉移當前系統log
			this.transferSystemLog();
			// 刪除歷史系統log
			this.deleteHistoryLog();
			// 庫存歷史月檔清除
			this.deleteRepoMonthlyHis();
			// 入庫資料清除
			this.deleteAssetIn();
			// 轉倉資料清除
			this.deleteAssetTransOut();
			
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".dealSystemLimit() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".dealSystemLimit() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	
	/**
	 * Purpose:密碼歷史清除
	 * @author CrissZhang
	 * @throws ServiceException
	 * @return void
	 */
	private void deletePwdHistory() throws ServiceException {
		try{
			// 密碼歷史基礎參數配置dto
			BaseParameterItemDefDTO baseParameterItemDefDTO =  this.baseParameterItemDefDAO.getParameterItemByValue(IATOMS_PARAM_TYPE.SYSTEM_LIMIT.getCode(), null, IAtomsConstants.PARAM_PWD_HISTORY_MONTH);
			// 密碼歷史期限(月份)
			int pwdHistoryMonth = 0;
			if (baseParameterItemDefDTO != null && StringUtils.hasText(baseParameterItemDefDTO.getTextField1())) {
				pwdHistoryMonth = Integer.parseInt(baseParameterItemDefDTO.getTextField1());
			}
			if (pwdHistoryMonth != 0) {
				LOGGER.debug("deletePwdHistory", "parameter:pwdHistoryMonth="+ pwdHistoryMonth);
				// 獲取當天時間
				Date currentDate = DateTimeUtils.getCurrentDate();
				// 得到應刪除密碼日期
				Date beforeDate = DateTimeUtils.addCalendar(currentDate, 0, -pwdHistoryMonth, 0);
				if (beforeDate != null) {
					LOGGER.debug("deletePwdHistory", "parameter:beforeDate=" + beforeDate);
					// 刪除這個時間之前的密碼歷史
					this.passwordSettingDAO.deletePwdHistory(beforeDate);
					LOGGER.debug("deletePwdHistory", "DO deletePwdHistory() SUCCESS ");
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".deletePwdHistory() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".deletePwdHistory() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	
	/**
	 * Purpose:轉移系統log
	 * @author CrissZhang
	 * @throws ServiceException
	 * @return void
	 */
	private void transferSystemLog() throws ServiceException {
		try{
			// 轉移系統log基礎參數配置dto
			BaseParameterItemDefDTO baseParameterItemDefDTO =  this.baseParameterItemDefDAO.getParameterItemByValue(IATOMS_PARAM_TYPE.SYSTEM_LIMIT.getCode(), null, IAtomsConstants.PARAM_SYSTEM_LOG_VALUE);
			// 轉移系統log期限(月份)
			int transferLogMonth = 0;
			if (baseParameterItemDefDTO != null && StringUtils.hasText(baseParameterItemDefDTO.getTextField1())) {
				transferLogMonth = Integer.parseInt(baseParameterItemDefDTO.getTextField1());
			}
			if (transferLogMonth != 0) {
				LOGGER.debug("transferSystemLog", "parameter:pwdHistoryMonth="+ transferLogMonth);
				// 獲取當天時間
				Date currentDate = DateTimeUtils.getCurrentDate();
				// 得到應轉移系統log日期
				Date transferDate = DateTimeUtils.addCalendar(currentDate, 0, -transferLogMonth, 0);
				if (transferDate != null) {
					LOGGER.debug("transferSystemLog", "parameter:transferDate=" + transferDate);
					// 轉移這個時間之前的系統log
					this.systemLogDAO.transferSystemLog(transferDate);
					LOGGER.debug("transferSystemLog", "DO transferSystemLog() SUCCESS ");
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".transferSystemLog() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".transferSystemLog() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IIAtomsBatchService#deleteHistoryLog()
	 */
	@Override
	public void deleteHistoryLog() throws ServiceException {
		try{
			// 系統歷史LOG基礎參數配置dto
			BaseParameterItemDefDTO baseParameterItemDefDTO =  this.baseParameterItemDefDAO.getParameterItemByValue(IATOMS_PARAM_TYPE.SYSTEM_LIMIT.getCode(), null, IAtomsConstants.PARAM_LOG_HISTORY_MONTH);
			// 系統LOG歷史清除期限(月份)
			int systemLogMonth = 0;
			if (baseParameterItemDefDTO != null && StringUtils.hasText(baseParameterItemDefDTO.getTextField1())) {
				systemLogMonth = Integer.parseInt(baseParameterItemDefDTO.getTextField1());
			}
			if (systemLogMonth != 0) {
				LOGGER.debug("deleteHistoryLog", "parameter:systemLogMonth="+ systemLogMonth);
				// 獲取當天時間
				Date currentDate = DateTimeUtils.getCurrentDate();
				// 得到應刪除歷史log時間
				Date deleteDate = DateTimeUtils.addCalendar(currentDate, 0, -systemLogMonth, 0);
				if (deleteDate != null) {
					LOGGER.debug("deleteHistoryLog", "parameter:deleteDate=" + deleteDate);
					// 刪除這個時間之前的log
					this.systemLogDAO.deleteHistoryLog(deleteDate);
					LOGGER.debug("deleteHistoryLog", "DO deleteHistoryLog() SUCCESS ");
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".deleteHistoryLog() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".deleteHistoryLog() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	
	/**
	 * Purpose:庫存歷史月檔清除
	 * @author CrissZhang
	 * @throws ServiceException
	 * @return void
	 */
	private void deleteRepoMonthlyHis() throws ServiceException {
		try{
			// 庫存歷史月檔基礎參數配置dto
			BaseParameterItemDefDTO baseParameterItemDefDTO =  this.baseParameterItemDefDAO.getParameterItemByValue(IATOMS_PARAM_TYPE.SYSTEM_LIMIT.getCode(), null, IAtomsConstants.PARAM_REPO_MONTHLY_HIS_MONTH);
			// 庫存歷史月檔清除期限(月份)
			int repoMonthlyHisMonth = 0;
			if (baseParameterItemDefDTO != null && StringUtils.hasText(baseParameterItemDefDTO.getTextField1())) {
				repoMonthlyHisMonth = Integer.parseInt(baseParameterItemDefDTO.getTextField1());
			}
			if (repoMonthlyHisMonth != 0) {
				LOGGER.debug("deleteRepoMonthlyHis", "parameter:repoMonthlyHisMonth="+ repoMonthlyHisMonth);
				// 獲取當天時間
				Date currentDate = DateTimeUtils.getCurrentDate();
				// 得到應刪除庫存歷史月檔時間
				Date deleteDate = DateTimeUtils.addCalendar(currentDate, 0, -repoMonthlyHisMonth, 0);
				String deleteDateString = DateTimeUtils.toString(deleteDate, DateTimeUtils.DT_FMT_YYYYMM);
				if (deleteDate != null) {
					LOGGER.debug("deleteRepoMonthlyHis", "parameter:deleteDateString=" + deleteDateString);
					// 刪除這個時間之前的庫存歷史月檔
					this.dmmRepositoryHistoryMonthlyDAO.deleteRepoMonthlyHis(deleteDateString);
					LOGGER.debug("deleteRepoMonthlyHis", "DO deleteRepoMonthlyHis() SUCCESS ");
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".deleteRepoMonthlyHis() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".deleteRepoMonthlyHis() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * Purpose:入庫資料清除
	 * @author CrissZhang
	 * @throws ServiceException
	 * @return void
	 */
	private void deleteAssetIn() throws ServiceException {
		try{
			// 入庫資料刪除基礎參數配置dto
			BaseParameterItemDefDTO baseParameterItemDefDTO =  this.baseParameterItemDefDAO.getParameterItemByValue(IATOMS_PARAM_TYPE.SYSTEM_LIMIT.getCode(), null, IAtomsConstants.PARAM_ASSET_IN_MONTH);
			// 入庫資料清除期限(月份)
			int assetInMonth = 0;
			if (baseParameterItemDefDTO != null && StringUtils.hasText(baseParameterItemDefDTO.getTextField1())) {
				assetInMonth = Integer.parseInt(baseParameterItemDefDTO.getTextField1());
			}
			if (assetInMonth != 0) {
				LOGGER.debug("deleteAssetIn", "parameter:assetInMonth="+ assetInMonth);
				// 獲取當天時間
				Date currentDate = DateTimeUtils.getCurrentDate();
				// 得到應刪除入庫資料時間
				Date deleteDate = DateTimeUtils.addCalendar(currentDate, 0, -assetInMonth, 0);
				if (deleteDate != null) {
					LOGGER.debug("deleteAssetIn", "parameter:deleteDate=" + deleteDate);
					// 刪除這個時間之前的入庫資料
					this.assetInInfoDAO.deleteAssetIn(deleteDate);
					LOGGER.debug("deleteAssetIn", "DO deleteAssetIn() SUCCESS ");
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".deleteAssetIn() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".deleteAssetIn() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	
	/**
	 * Purpose:轉倉資料清除
	 * @author CrissZhang
	 * @throws ServiceException
	 * @return void
	 */
	private void deleteAssetTransOut() throws ServiceException {
		try{
			// 轉倉資料刪除基礎參數配置dto
			BaseParameterItemDefDTO baseParameterItemDefDTO =  this.baseParameterItemDefDAO.getParameterItemByValue(IATOMS_PARAM_TYPE.SYSTEM_LIMIT.getCode(), null, IAtomsConstants.PARAM_ASSET_TRANS_MONTH);
			// 轉倉資料清除期限(月份)
			int assetTransOutMonth = 0;
			if (baseParameterItemDefDTO != null && StringUtils.hasText(baseParameterItemDefDTO.getTextField1())) {
				assetTransOutMonth = Integer.parseInt(baseParameterItemDefDTO.getTextField1());
			}
			if (assetTransOutMonth != 0) {
				LOGGER.debug("deleteAssetTransOut", "parameter:assetTransOutMonth="+ assetTransOutMonth);
				// 獲取當天時間
				Date currentDate = DateTimeUtils.getCurrentDate();
				// 得到應刪除轉倉資料時間
				Date deleteDate = DateTimeUtils.addCalendar(currentDate, 0, -assetTransOutMonth, 0);
				if (deleteDate != null) {
					LOGGER.debug("deleteAssetTransOut", "parameter:deleteDate=" + deleteDate);
					// 刪除這個時間之前的轉倉資料
					this.assetTransInfoDAO.deleteAssetTransOut(deleteDate);
					LOGGER.debug("deleteAssetTransOut", "DO deleteAssetTransOut() SUCCESS ");
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".deleteAssetTransOut() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".deleteAssetTransOut() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * @return the dmmRepositoryHistoryDAO
	 */
	public IDmmRepositoryHistoryDAO getDmmRepositoryHistoryDAO() {
		return dmmRepositoryHistoryDAO;
	}
	/**
	 * @param dmmRepositoryHistoryDAO the dmmRepositoryHistoryDAO to set
	 */
	public void setDmmRepositoryHistoryDAO(
			IDmmRepositoryHistoryDAO dmmRepositoryHistoryDAO) {
		this.dmmRepositoryHistoryDAO = dmmRepositoryHistoryDAO;
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
	 * @return the srmCaseHandleInfoDAO
	 */
	public ISrmCaseHandleInfoDAO getSrmCaseHandleInfoDAO() {
		return srmCaseHandleInfoDAO;
	}
	/**
	 * @param srmCaseHandleInfoDAO the srmCaseHandleInfoDAO to set
	 */
	public void setSrmCaseHandleInfoDAO(ISrmCaseHandleInfoDAO srmCaseHandleInfoDAO) {
		this.srmCaseHandleInfoDAO = srmCaseHandleInfoDAO;
	}
	/**
	 * @return the systemLogDAO
	 */
	public ISystemLogDAO getSystemLogDAO() {
		return systemLogDAO;
	}
	/**
	 * @param systemLogDAO the systemLogDAO to set
	 */
	public void setSystemLogDAO(ISystemLogDAO systemLogDAO) {
		this.systemLogDAO = systemLogDAO;
	}
	/**
	 * @return the passwordSettingDAO
	 */
	public IPasswordSettingDAO getPasswordSettingDAO() {
		return passwordSettingDAO;
	}
	/**
	 * @param passwordSettingDAO the passwordSettingDAO to set
	 */
	public void setPasswordSettingDAO(IPasswordSettingDAO passwordSettingDAO) {
		this.passwordSettingDAO = passwordSettingDAO;
	}
	/**
	 * @return the dmmRepositoryHistoryMonthlyDAO
	 */
	public IDmmRepositoryHistoryMonthlyDAO getDmmRepositoryHistoryMonthlyDAO() {
		return dmmRepositoryHistoryMonthlyDAO;
	}
	/**
	 * @param dmmRepositoryHistoryMonthlyDAO the dmmRepositoryHistoryMonthlyDAO to set
	 */
	public void setDmmRepositoryHistoryMonthlyDAO(IDmmRepositoryHistoryMonthlyDAO dmmRepositoryHistoryMonthlyDAO) {
		this.dmmRepositoryHistoryMonthlyDAO = dmmRepositoryHistoryMonthlyDAO;
	}
	/**
	 * @return the assetInInfoDAO
	 */
	public IAssetInInfoDAO getAssetInInfoDAO() {
		return assetInInfoDAO;
	}
	/**
	 * @param assetInInfoDAO the assetInInfoDAO to set
	 */
	public void setAssetInInfoDAO(IAssetInInfoDAO assetInInfoDAO) {
		this.assetInInfoDAO = assetInInfoDAO;
	}
	/**
	 * @return the assetTransInfoDAO
	 */
	public IDmmAssetTransInfoDAO getAssetTransInfoDAO() {
		return assetTransInfoDAO;
	}
	/**
	 * @param assetTransInfoDAO the assetTransInfoDAO to set
	 */
	public void setAssetTransInfoDAO(IDmmAssetTransInfoDAO assetTransInfoDAO) {
		this.assetTransInfoDAO = assetTransInfoDAO;
	}
}
