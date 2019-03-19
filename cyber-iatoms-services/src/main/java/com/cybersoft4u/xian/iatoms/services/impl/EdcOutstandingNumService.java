package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.config.SystemConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.common.utils.mail.TemplateMailMessageDTO;
import com.cybersoft4u.xian.iatoms.services.IReportService;
import com.cybersoft4u.xian.iatoms.services.dao.ICalendarDayDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO;
/**
 * Purpose: EDC流通在外台數報表service
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/3/3
 * @MaintenancePersonnel CarrieDuan
 */
public class EdcOutstandingNumService extends AtomicService implements IReportService {

	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(EdcOutstandingNumService.class);
	
	/**
	 *  設備管理作業DAO
	 */
	private IDmmRepositoryDAO dmmRepositoryDAO = null;
	/**
	 * 发送mail组件
	 */
	private MailComponent mailComponent = null;
	/**
	 * 注入報表發送功能設定DAO
	 */
	private IReportSettingDAO reportSettingDAO;
	/**
	 * 行事曆日檔DAO
	 */
	private ICalendarDayDAO calendarDayDAO;
 	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcOutstandingNumService#sendMailOutstandingNumReport()
	 */
	public void sendMailOutstandingNumReport() throws ServiceException {
		try {
			Boolean isHoliday = this.calendarDayDAO.checkIsHoliday(DateTimeUtils.toDate(DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH)));
			if (!isHoliday) {
				sendReportMail(DateTimeUtils.getCurrentDate(), null, IAtomsConstants.LEAVE_CASE_STATUS_THREE);
			}
		} catch (Exception e) {
			LOGGER.error("sendMailOutstandingNumReport() ", "DataAccess Exception:", e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportService#sendReportMail(java.util.Date)
	 */
	public void sendReportMail(Date sendDate, String customerId, String reportCode) throws ServiceException {
		StringBuffer filePathBuffer = new StringBuffer();
		try {
			//獲取按照報表資料-按照設備名稱分組
			List<CrossTabReportDTO> crossTabReportDTOsOrderByShort = this.dmmRepositoryDAO.edcInstallNumReport(IAtomsConstants.PARAM_ASSET_STATUS_IN_USE, IAtomsConstants.ASSET_CATEGORY_EDC, "SHORT_NAME");
			List<String> assetNames = new ArrayList<String>();
			//公司名稱排序 map<公司名稱, 序號+公司名稱> --update by 2017-07-20
			Map<String, String> companyMap = new HashMap<String, String>();
			int k = 0;
			Map<String, String> shortColumnName = new HashMap<String, String>();
			int index = 0;
			for (CrossTabReportDTO crossTabReportDTO : crossTabReportDTOsOrderByShort) {
				String name = crossTabReportDTO.getColumnName();
				if (!shortColumnName.containsKey(crossTabReportDTO.getColumnName())) {
					if (index<10) {
						crossTabReportDTO.setColumnName("00" + index + name);
						shortColumnName.put(name, "00" + index);
						index++;
					} else {
						crossTabReportDTO.setColumnName("0" + index + name);
						shortColumnName.put(name, "0" + index);
						index++;
					}
				} else {
					crossTabReportDTO.setColumnName(shortColumnName.get(name) + name);
				}
				//map中是否存在該公司名稱
				if (!companyMap.containsKey(crossTabReportDTO.getRowName())) {
					companyMap.put(crossTabReportDTO.getRowName(), StringUtils.toFixString(4, k) + crossTabReportDTO.getRowName());
					crossTabReportDTO.setRowName(StringUtils.toFixString(4, k) + crossTabReportDTO.getRowName());
					k++;
				} else {
					crossTabReportDTO.setRowName(companyMap.get(crossTabReportDTO.getRowName()));
				}				
				if (assetNames.size() == 0 || !assetNames.contains(crossTabReportDTO.getColumnName())) {
					assetNames.add(crossTabReportDTO.getColumnName());
				}
			}
			//獲取按照報表資料-按照設備資料之維護組別分組
			List<CrossTabReportDTO> crossTabReportDTOsOrderByDept = this.dmmRepositoryDAO.edcInstallNumReport(IAtomsConstants.PARAM_ASSET_STATUS_IN_USE, IAtomsConstants.ASSET_CATEGORY_EDC, "DEPT_NAME");
			List<String> companyNames = new ArrayList<String>();
			int i = 1;
			shortColumnName = new HashMap<String, String>();
			index = 0;
			if (!CollectionUtils.isEmpty(crossTabReportDTOsOrderByDept)) {
				String rowName = crossTabReportDTOsOrderByDept.get(0).getRowName();
				
				for (int j = 0; j< crossTabReportDTOsOrderByDept.size(); j++) {
					String name = crossTabReportDTOsOrderByDept.get(j).getColumnName();
					if (!shortColumnName.containsKey(crossTabReportDTOsOrderByDept.get(j).getColumnName())) {
						if (index<10) {
							crossTabReportDTOsOrderByDept.get(j).setColumnName("00" + index + name);
							shortColumnName.put(name, "00" + index);
							index++;
						} else {
							crossTabReportDTOsOrderByDept.get(j).setColumnName("0" + index + name);
							shortColumnName.put(name, "0" + index);
							index++;
						}
					} else {
						crossTabReportDTOsOrderByDept.get(j).setColumnName(shortColumnName.get(name) + name);
					}
					if (companyNames.size() == 0 || !companyNames.contains(crossTabReportDTOsOrderByDept.get(j).getColumnName())) {
						companyNames.add(crossTabReportDTOsOrderByDept.get(j).getColumnName());
					}
					if (StringUtils.hasText(rowName)) {
						if (!rowName.equals(crossTabReportDTOsOrderByDept.get(j).getRowName())) {
							crossTabReportDTOsOrderByDept.get(j).setRowNo(++i);
							rowName = crossTabReportDTOsOrderByDept.get(j).getRowName();
						} else {
							crossTabReportDTOsOrderByDept.get(j).setRowNo(i);
						}
					} else {
						crossTabReportDTOsOrderByDept.get(j).setRowNo(i);
						if (j + 1 < crossTabReportDTOsOrderByDept.size()) {
							rowName = crossTabReportDTOsOrderByDept.get(j + 1).getRowName();
							if (StringUtils.hasText(rowName)) {
								i++;
							}
						}
					}
				}
			}
			List<CrossTabReportDTO> crossTabReportDTOs = new ArrayList<CrossTabReportDTO>();
			CrossTabReportDTO crossTabReportDTO = new CrossTabReportDTO();
			crossTabReportDTO.setCrossTabReportDTOsOrderByShort(crossTabReportDTOsOrderByShort);
			crossTabReportDTO.setCrossTabReportDTOsOrderByDept(crossTabReportDTOsOrderByDept);
			crossTabReportDTOs.add(crossTabReportDTO);
			
			String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
			String path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
			StringBuffer pathBuffer = new StringBuffer();
			pathBuffer.append(path).append(File.separator).append(yearMonthDay).append(File.separator);
			path = pathBuffer.toString();
			//核檢路徑是否存在，不存在則創建路徑
			File fileDir = new File(path);
    		//判斷儲存路徑是否存在，若不存在，則重新新建
    		if (!fileDir.exists() || !fileDir.isDirectory()) {
    			fileDir.mkdirs();
    		}
			JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
			Map<String, String> subjrXmlNames = new HashMap<String, String>();
			subjrXmlNames.put(IAtomsConstants.EDC_OUTSTANDING_NUM_REPORT_SUBREPORT1, IAtomsConstants.EDC_OUTSTANDING_NUM_REPORT_SUBREPORT_DIR);
			subjrXmlNames.put(IAtomsConstants.EDC_OUTSTANDING_NUM_REPORT_SUBREPORT2, IAtomsConstants.EDC_OUTSTANDING_NUM_REPORT_SUBREPORT_DIR1);
			criteria.setAutoBuildJasper(false);
			criteria.setResult(crossTabReportDTOs);
			//設置所需報表的Name
			criteria.setJrxmlName(IAtomsConstants.EDC_OUTSTANDING_NUM_REPORT_EN_NAME);
			criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_OUTSTANDING_NUM_REPORT_CH_NAME));
			//criteria.set
			//設置報表路徑
			criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
			//設置匯出格式
			criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
			//設置報表Name
			String fileName = i18NUtil.getName(IAtomsConstants.EDC_OUTSTANDING_NUM_REPORT_CH_NAME).concat(IAtomsConstants.MARK_UNDER_LINE).concat(yearMonthDay);
			criteria.setReportFileName(fileName);
			Map<Integer, Map<Integer, Integer>> sheetTitleWidthMap = new HashMap<Integer, Map<Integer,Integer>>();
			Map<Integer, Integer> map2 = new HashMap<Integer, Integer>();
			map2.put(0, (assetNames.size() + 2) * 100);
			map2.put(1, (companyNames.size() + 2) * 100 + 70);
			sheetTitleWidthMap.put(1, map2);
			ReportExporter.exportReportToFile(criteria, subjrXmlNames, path, sheetTitleWidthMap);
			
			//郵件內容
			String mailContext = null;
			//郵件主題模板
			String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_OUTSTANDING_NUM_SUBJECT;
			//郵件內容模板
			String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_OUTSTANDING_NUM_CONTEXT;
			//郵件內容
			Map<String, Object> variables = new HashMap<String, Object>();
			//
			
			filePathBuffer.append(path).append(File.separator);
			filePathBuffer.append(fileName).append(JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL);
			//查詢EDC提示報表的信息
			List<ReportSettingDTO> reportSettingList = this.reportSettingDAO.listByReportCode(customerId, reportCode);
			String[] attachments = new String[]{filePathBuffer.toString()};
			if (!CollectionUtils.isEmpty(reportSettingList)) {
				for (ReportSettingDTO reportSettingDTO : reportSettingList) {
					if (StringUtils.hasText(reportSettingDTO.getRecipient())) {
						variables.put("subjectDate", DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD));
						variables.put("toMail", reportSettingDTO.getRecipient());
						variables.put("ccMail", reportSettingDTO.getCopy());
						if (CollectionUtils.isEmpty(crossTabReportDTOsOrderByShort) && CollectionUtils.isEmpty(crossTabReportDTOsOrderByDept)) {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NULL));
						} else {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NOT_NULL));
						}
						try{
							this.mailComponent.sendMailTo(null, reportSettingDTO.getRecipient(), subjectTemplate, attachments, textTemplate, variables);
						} catch (Exception e) {
							LOGGER.error("sendMailOutstandingNumReport()--->sendMailTo(): ", "DataAccess Exception:", e);
						}
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("sendMailOutstandingNumReport() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("sendMailOutstandingNumReport()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		} finally {
			try {
				FileUtils.removeFile(filePathBuffer.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the dmmRepositoryDAO
	 */
	public IDmmRepositoryDAO getDmmRepositoryDAO() {
		return dmmRepositoryDAO;
	}
	/**
	 * @param dmmRepositoryDAO the dmmRepositoryDAO to set
	 */
	public void setDmmRepositoryDAO(IDmmRepositoryDAO dmmRepositoryDAO) {
		this.dmmRepositoryDAO = dmmRepositoryDAO;
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

	/**
	 * @return the reportSettingDAO
	 */
	public IReportSettingDAO getReportSettingDAO() {
		return reportSettingDAO;
	}

	/**
	 * @param reportSettingDAO the reportSettingDAO to set
	 */
	public void setReportSettingDAO(IReportSettingDAO reportSettingDAO) {
		this.reportSettingDAO = reportSettingDAO;
	}

	/**
	 * @return the calendarDayDAO
	 */
	public ICalendarDayDAO getCalendarDayDAO() {
		return calendarDayDAO;
	}

	/**
	 * @param calendarDayDAO the calendarDayDAO to set
	 */
	public void setCalendarDayDAO(ICalendarDayDAO calendarDayDAO) {
		this.calendarDayDAO = calendarDayDAO;
	}

	
	
}
