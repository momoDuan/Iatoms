package com.cybersoft4u.xian.iatoms.web.controllers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;

import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * 
 * @author felixli
 *
 */
public class JxlsUtil {
	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog log = CafeLogFactory.getLog(JxlsUtil.class);
	
	public static boolean exportSalaryTemplet (JasperReportCriteriaDTO criteria, Map<String, Object> data, HttpServletResponse response) throws Exception {
		log.debug("Enter method ReportExporter.exportReport()");
		if (criteria == null) {
			return false;
		}
		ByteArrayOutputStream outputStream = null;
		ServletOutputStream oStream = null;
		try {
			String jxls = criteria.getJxlsPath() + criteria.getJxlsName() + JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCELX;
			InputStream inputStream = JxlsUtil.class.getResourceAsStream(jxls);
			Workbook workbook = new XLSTransformer().transformXLS(inputStream, data);
			//鎖定Excel,不可編輯
//			int sheetNumbers = workbook.getNumberOfSheets();
//			 Sheet sheet = null;
//			 for (int i = 0; i < sheetNumbers; i++) {
//				  sheet = (Sheet) workbook.getSheetAt(i);
//				  sheet.setSheetName(criteria.getSheetName());
//			 }
			outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			byte[] bytes = outputStream.toByteArray();
			response.setContentType(JasperReportCriteriaDTO.RESPONSE_CONTENT_TYPE_2007_MSEXCEL);
			response.setCharacterEncoding(JasperReportCriteriaDTO.CHARACTER_ENCODING);
			response.setHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(criteria.getReportFileName(), JasperReportCriteriaDTO.CHARACTER_ENCODING) + JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCELX);
			response.setContentLength(bytes.length);
			outputStream.close();
			
			oStream = response.getOutputStream();
			oStream.write(bytes, 0, bytes.length);
			oStream.flush();
			oStream.close();
			return true;
		} catch (Exception e) {
			log.error("Exception: method JxlsUtil.exportSalaryTemplet() - " + e, e);
			return false;
		} finally {
			if (oStream != null) {
				oStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
		
	}
	
}
