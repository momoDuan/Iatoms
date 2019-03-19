package com.cybersoft4u.xian.iatoms.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleDocxExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleRtfReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;

/**
 * Purpose: 處理JasperReport工具
 * @author candicechen
 * @since  JDK 1.5
 * @date   2014/9/19
 * @MaintenancePersonnel candicechen
 */
public class ReportExporter {
	private static final Log log = LogFactory.getLog(ReportExporter.class);
	/**
	 * Purpose:導出JasperReport報表
	 * @param criteria:報表匯出DTO
	 * @param tempFolder:暫存路徑
	 * @throws Exception:出錯時返回
	 * @return boolean:返回結果
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean exportReportToFile(JasperReportCriteriaDTO criteria, String tempFolder, Map<Integer,
				Map<Integer, Integer>> sheetTitleWidthMap) throws Exception {
		log.debug("Enter method ReportExporter.exportReportToFile()");
		ArrayList logArrayList = new ArrayList();
		long s = System.currentTimeMillis();
		if (criteria == null) {
			return false;
		}
		//上傳檔案路徑
		File fileFord = new File(tempFolder);
		//判斷儲存路徑是否存在，若不存在，則重新新建
		if (!fileFord.exists() || !fileFord.isDirectory()) {
			fileFord.mkdirs();
		}
		ByteArrayOutputStream outputStream = null;
		FileOutputStream fos  = null;
		OPCPackage opc = null;
		POIFSFileSystem fs = null;
		OutputStream os = null;
		try {
			JasperReport jasperReport = null;
			
			JRDesignBand cTitle = null;
			JRElement[] es_Title = null;
			Map<Integer, Integer> titleMap = null;
			
			// 需要jrXml文檔的方式
			if (criteria.isAutoBuildJasper() == false) {
				String jrXml = criteria.getJrxmlPath() + criteria.getJrxmlName() + JasperReportCriteriaDTO.JRXML_EXT_NAME;
				InputStream inputStream = ReportExporter.class.getResourceAsStream(jrXml);
				//System.setProperty("javax.xml.parsers.SAXParserFactory","org.apache.xerces.jaxp.SAXParserFactoryImpl");
			     System.setProperty("javax.xml.parsers.SAXParserFactory","com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
				JasperDesign design = JRXmlLoader.load(inputStream);
				if (sheetTitleWidthMap != null) {
					if (sheetTitleWidthMap.containsKey(1)) {
						cTitle = (JRDesignBand) design.getTitle();
						if (cTitle != null) {
							es_Title = cTitle.getElements();
							if (es_Title != null && es_Title.length > 0) {
								titleMap = sheetTitleWidthMap.get(1);
								for (int j = 0; j < es_Title.length; j++) {
									if (titleMap.containsKey(j)) {
										es_Title[j].setWidth(titleMap.get(j));
										/*if (j == 2 && titleMap.get(1) == 0) {
											es_Title[j].setX(100);
										} else if (j == 2 && titleMap.get(1) > 0) {
											es_Title[j].setX(100 + titleMap.get(1));
										}*/
									}
								}
							}
						}
					}
				}
				// 編譯報表（.jrxml -> .jasper）
				jasperReport = JasperCompileManager.compileReport(design);
				logArrayList.add("compileReport="+(System.currentTimeMillis()-s)+" ms");
			}else{
				String jasper = criteria.getJrxmlPath() + criteria.getJrxmlName() + JasperReportCriteriaDTO.JASPER_EXT_NAME;
				InputStream inputStream = ReportExporter.class.getResourceAsStream(jasper);
				jasperReport = (JasperReport)JRLoader.loadObject(inputStream);
				logArrayList.add("readJasper="+(System.currentTimeMillis()-s)+" ms");
			}
			// 填充報表
			long f = System.currentTimeMillis();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, criteria.getParameters(), 
						new JRBeanCollectionDataSource(criteria.getResult()));
			logArrayList.add("fillReport="+(System.currentTimeMillis()-f)+" ms");
			
			outputStream = new ByteArrayOutputStream();
			
			JRAbstractExporter exporter = null;
			String reportExtName = "";
			if (StringUtils.hasText(criteria.getType()) == false) {
				exporter = new JRPdfExporter();
				reportExtName = JasperReportCriteriaDTO.REPORT_EXT_NAME_PDF;
			} else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSWORD)) {
				exporter = new JRRtfExporter();
				reportExtName = JasperReportCriteriaDTO.REPORT_EXT_NAME_MSWORD;
			} else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL)
					|| criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCELX)) {
				if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL)) {
					exporter = new JRXlsExporter();
					reportExtName = JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL;
				} else {
					exporter = new JRXlsxExporter();
					reportExtName = JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCELX;
				}
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				String sheetName = criteria.getSheetName();
				configuration.setOnePagePerSheet(true);
				if (StringUtils.hasText(sheetName)) {
					configuration.setSheetNames(new String[]{sheetName});
				}
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporter.setConfiguration(configuration);
			}else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_CSV)){
				exporter = new JRCsvExporter();
				reportExtName = JasperReportCriteriaDTO.REPORT_EXT_NAME_CSV;
			} else {
				exporter = new JRPdfExporter();
				reportExtName = JasperReportCriteriaDTO.REPORT_EXT_NAME_PDF;
			}
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			
			long e = System.currentTimeMillis();
			exporter.exportReport();
			logArrayList.add("exportReport="+(System.currentTimeMillis()-e)+" ms");
			
			byte[] bytes = null;			
			//輸出保教到指定目錄
			bytes = outputStream.toByteArray();
			log.debug("Report File="+tempFolder+criteria.getReportFileName()+reportExtName);
			File file = new File(tempFolder+criteria.getReportFileName()+reportExtName);
			fos = new FileOutputStream(file);
			if(bytes != null ){
				//long w = System.currentTimeMillis();
				fos.write(bytes);
			}
			//excel加密
			/*if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCELX)) {
				fos.close();
				outputStream.close();
				fs = new POIFSFileSystem();  
				EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);  
				Encryptor enc = info.getEncryptor();
				enc.confirmPassword(criteria.getOpenPwd());
				opc = OPCPackage.open(new File(tempFolder+criteria.getReportFileName()+reportExtName), PackageAccess.READ_WRITE);
				os = enc.getDataStream(fs);  
				opc.save(os);
				opc.close();
				fos = new FileOutputStream(tempFolder+criteria.getReportFileName()+reportExtName);
				fs.writeFilesystem(fos);
				if (os != null) {
					os.close();
				}
			}*/
			logArrayList.add("exportReportToFile.total="+(System.currentTimeMillis()-s)+" ms");
			log.debug("Exit method ReportExporter.exportReportToFile()");
			for(int i=0;i<logArrayList.size();i++){
				log.debug(logArrayList.get(i));
			}
			return true;
		} catch (Exception e) {
			log.error("Exception: method ReportExporter.exportReportToFile() - ", e);
			return false;
		} finally {
			if (fos != null) {
				fos.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}
	/**
	 * Purpose:導出多sheet excel報表(包含子报表)
	 * @param criteria:JasperReport條件設定DTO List集合
	 * @param response:HttpServletResponse
	 * @return 導出是否成功
	 * @throws Exception
	 */
	public static boolean exportReportForSheets(List<JasperReportCriteriaDTO> criterias,HttpServletResponse response) throws Exception {
		return exportReportForSheets(criterias, response, null);
	}
	
	/**
	 * Purpose:導出多sheet excel報表(包含子报表)
	 * @param criteria:JasperReport條件設定DTO List集合
	 * @param response:HttpServletResponse
	 * @param sheetTitleWidth:sheet title 的寬度設置， 分別的值為sheetNo， titleNo， 寬度
	 * @return 導出是否成功
	 * @throws Exception
	 */
	public static boolean exportReportForSheets(List<JasperReportCriteriaDTO> criterias,HttpServletResponse response, 
			Map<Integer, Map<Integer, Integer>> sheetTitleWidthMap  ) throws Exception {
		log.debug("Enter method ReportExporter.exportReportForSheets()");
		if (CollectionUtils.isEmpty(criterias)) {
			return false;
		}
		ByteArrayOutputStream outputStream = null;
		ServletOutputStream oStream = null;
		try {
			int sheetsSize = criterias.size();
			JasperReport[] jasperReports = new JasperReport[sheetsSize];
			JasperReportCriteriaDTO criteria = null;
			List<JasperPrint> prints = new ArrayList<JasperPrint>();
			JasperPrint jasperPrint = null;
			//sheet名稱
			String[] sheetNames = new String[sheetsSize];
			String type = criterias.get(0).getType();
			String reportFileName = criterias.get(0).getReportFileName();
			
			JRDesignBand cTitle = null;
			JRElement[] es_Title = null;
			Map<Integer, Integer> titleMap = null;
			JasperReport jasperReport = null;
			for(int i=0;i<sheetsSize;i++){
				criteria = criterias.get(i);
				// 需要jrXml文檔的方式
				if (criteria.isAutoBuildJasper() == false) {
					String jrXml = criteria.getJrxmlPath()
							+ criteria.getJrxmlName()
							+ JasperReportCriteriaDTO.JRXML_EXT_NAME;
					InputStream inputStream = ReportExporter.class
							.getResourceAsStream(jrXml);
					System.setProperty("javax.xml.parsers.SAXParserFactory",
							"com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
					JasperDesign design = JRXmlLoader.load(inputStream);
					if (sheetTitleWidthMap != null) {
						if (sheetTitleWidthMap.containsKey(i)) {
							cTitle = (JRDesignBand) design.getTitle();
							if (cTitle != null) {
								es_Title = cTitle.getElements();
								if (es_Title != null && es_Title.length > 0) {
									titleMap = sheetTitleWidthMap.get(i);
									for (int j = 0; j < es_Title.length; j++) {
										if (titleMap.containsKey(j)) {
											es_Title[j].setWidth(titleMap.get(j));
										}
									}
								}
							}
						}
					}
					// 編譯報表（.jrxml -> .jasper）
					jasperReports[i] = JasperCompileManager.compileReport(design);
					// 編譯sub報表（.jrxml -> .jasper）
					String subjrXml = null;
					InputStream subinputStream = null;
					JasperDesign subDesign = null;
					JasperReport subJasperReport = null;
					Map<String, String> subjrXmlNames = criteria.getSubjrXmlNames();
					if(subjrXmlNames!=null){
						for(String subjrXmlName: subjrXmlNames.keySet()){
							subjrXml = criteria.getJrxmlPath() + subjrXmlName + JasperReportCriteriaDTO.JRXML_EXT_NAME;
							subinputStream = ReportExporter.class.getResourceAsStream(subjrXml);
						    System.setProperty("javax.xml.parsers.SAXParserFactory","com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
						    subDesign = JRXmlLoader.load(subinputStream);
						    subJasperReport = JasperCompileManager.compileReport(subDesign);
						    // update by riverjin 2015/08/31 
							if(criteria.getParameters() == null){
								Map<String, Object> parameters = new  HashMap<String, Object>();
								criteria.setParameters(parameters);
							}
						    criteria.getParameters().put(subjrXmlNames.get(subjrXmlName).toString(), subJasperReport);
						}
					}
					jasperPrint = JasperFillManager.fillReport(
							jasperReports[i], criteria.getParameters(),
							new JRBeanCollectionDataSource(criteria.getResult()));
				} else{
					String jasper = criteria.getJrxmlPath() + criteria.getJrxmlName() + JasperReportCriteriaDTO.JASPER_EXT_NAME;
					InputStream inputStream = ReportExporter.class.getResourceAsStream(jasper);
					jasperReport = (JasperReport)JRLoader.loadObject(inputStream);
					jasperPrint = JasperFillManager.fillReport(
							jasperReport, criteria.getParameters(),
							new JRBeanCollectionDataSource(criteria.getResult()));
				}
				// 填充報表
				prints.add(jasperPrint);
				sheetNames[i] = criteria.getSheetName();
			}
			
			outputStream = new ByteArrayOutputStream();
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(prints));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setSheetNames(sheetNames);
			configuration.setOnePagePerSheet(Boolean.FALSE);
			configuration.setDetectCellType(Boolean.TRUE);
			configuration.setCollapseRowSpan(Boolean.FALSE);
			configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
			configuration.setWhitePageBackground(Boolean.TRUE);
			exporter.setConfiguration(configuration);
			exporter.exportReport();

			byte[] bytes = outputStream.toByteArray();
			response.setContentType(JasperReportCriteriaDTO.getContentTypeMap()
					.get(type));
			response.setCharacterEncoding(JasperReportCriteriaDTO.CHARACTER_ENCODING);
			response.setHeader(
					"Content-disposition",
					"attachment;filename="
							+ URLEncoder.encode(reportFileName,
									JasperReportCriteriaDTO.CHARACTER_ENCODING)
							+ JasperReportCriteriaDTO.getReportExtNameMap()
									.get(type));
			response.setContentLength(bytes.length);
			outputStream.close();
			oStream = response.getOutputStream();
			oStream.write(bytes, 0, bytes.length);
			oStream.flush();
			oStream.close();
			log.debug("Exit method ReportExporter.exportReportForSheets()");
			return true;
		} catch (Exception e) {
			log.error(
					"Exception: method ReportExporter.exportMainAndSubReport() - "
							+ e, e);
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
	/**
	 * Purpose:導出多sheet excel報表(包含子报表)(子報表包含交叉報表)(死參數，不建議使用.)
	 * @param criteria:JasperReport條件設定DTO List集合
	 * @param response:HttpServletResponse
	 * @param sheetTitleWidth:sheet title 的寬度設置， 分別的值為sheetNo， titleNo， 寬度
	 * @return 導出是否成功
	 * @throws Exception
	 */
	public static boolean exportReportForSheetsAndCrossTab(List<JasperReportCriteriaDTO> criterias,HttpServletResponse response, 
			Map<Integer, Map<Integer, Integer>> sheetTitleWidthMap  ) throws Exception {
		log.debug("Enter method ReportExporter.exportReportForSheets()");
		if (CollectionUtils.isEmpty(criterias)) {
			return false;
		}
		ByteArrayOutputStream outputStream = null;
		ServletOutputStream oStream = null;
		try {
			int sheetsSize = criterias.size();
			JasperReport[] jasperReports = new JasperReport[sheetsSize];
			JasperReportCriteriaDTO criteria = null;
			List<JasperPrint> prints = new ArrayList<JasperPrint>();
			JasperPrint jasperPrint = null;
			//sheet名稱
			String[] sheetNames = new String[sheetsSize];
			String type = criterias.get(0).getType();
			String reportFileName = criterias.get(0).getReportFileName();
			
			JRDesignBand cTitle = null;
			JRDesignBand cSummary = null;
			JRElement[] es_Title = null;
			JRElement[] es_Summary = null;
			Map<Integer, Integer> titleMap = null;
			Map<Integer, Integer> summaryMap = null;
			for(int i=0;i<sheetsSize;i++){
				criteria = criterias.get(i);
				// 需要jrXml文檔的方式
				if (criteria.isAutoBuildJasper() == false) {
					String jrXml = criteria.getJrxmlPath()
							+ criteria.getJrxmlName()
							+ JasperReportCriteriaDTO.JRXML_EXT_NAME;
					InputStream inputStream = ReportExporter.class
							.getResourceAsStream(jrXml);
					System.setProperty("javax.xml.parsers.SAXParserFactory",
							"com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
					JasperDesign design = JRXmlLoader.load(inputStream);
					if (sheetTitleWidthMap != null) {
						if (sheetTitleWidthMap.containsKey(i)) {
							cTitle = (JRDesignBand) design.getTitle();
							if (cTitle != null) {
								es_Title = cTitle.getElements();
								if (es_Title != null && es_Title.length > 0) {
									titleMap = sheetTitleWidthMap.get(i);
									for (int j = 0; j < es_Title.length; j++) {
										if (titleMap.containsKey(j)) {
											es_Title[j].setWidth(titleMap.get(j));
											if (j == 2 && titleMap.get(1) == 0) {
												es_Title[j].setX(150);
											} else if (j == 2 && titleMap.get(1) > 0) {
												es_Title[j].setX(150 + titleMap.get(1));
											}
										}
									}
								}
							}
						}
					}
					// 編譯報表（.jrxml -> .jasper）
					jasperReports[i] = JasperCompileManager.compileReport(design);
					// 編譯sub報表（.jrxml -> .jasper）
					String subjrXml = null;
					InputStream subinputStream = null;
					JasperDesign subDesign = null;
					JasperReport subJasperReport = null;
					Map<String, String> subjrXmlNames = criteria.getSubjrXmlNames();
					if(subjrXmlNames!=null){
						for(String subjrXmlName: subjrXmlNames.keySet()){
							subjrXml = criteria.getJrxmlPath() + subjrXmlName + JasperReportCriteriaDTO.JRXML_EXT_NAME;
							subinputStream = ReportExporter.class.getResourceAsStream(subjrXml);
						    System.setProperty("javax.xml.parsers.SAXParserFactory","com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
						    subDesign = JRXmlLoader.load(subinputStream);
						    subJasperReport = JasperCompileManager.compileReport(subDesign);
						    // update by riverjin 2015/08/31 
							if(criteria.getParameters() == null){
								Map<String, Object> parameters = new  HashMap<String, Object>();
								criteria.setParameters(parameters);
							}
						    criteria.getParameters().put(subjrXmlNames.get(subjrXmlName).toString(), subJasperReport);
						}
					}
				}
				// 填充報表
				jasperPrint = JasperFillManager.fillReport(
						jasperReports[i], criteria.getParameters(),
						new JRBeanCollectionDataSource(criteria.getResult()));
				prints.add(jasperPrint);
				sheetNames[i] = criteria.getSheetName();
			}
			
			outputStream = new ByteArrayOutputStream();
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(prints));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setSheetNames(sheetNames);
			configuration.setOnePagePerSheet(Boolean.FALSE);
			configuration.setDetectCellType(Boolean.TRUE);
			configuration.setCollapseRowSpan(Boolean.FALSE);
			configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
			configuration.setWhitePageBackground(Boolean.TRUE);
			exporter.setConfiguration(configuration);
			exporter.exportReport();

			byte[] bytes = outputStream.toByteArray();
			response.setContentType(JasperReportCriteriaDTO.getContentTypeMap()
					.get(type));
			response.setCharacterEncoding(JasperReportCriteriaDTO.CHARACTER_ENCODING);
			response.setHeader(
					"Content-disposition",
					"attachment;filename="
							+ URLEncoder.encode(reportFileName,
									JasperReportCriteriaDTO.CHARACTER_ENCODING)
							+ JasperReportCriteriaDTO.getReportExtNameMap()
									.get(type));
			response.setContentLength(bytes.length);
			outputStream.close();
			oStream = response.getOutputStream();
			oStream.write(bytes, 0, bytes.length);
			oStream.flush();
			oStream.close();
			log.debug("Exit method ReportExporter.exportReportForSheets()");
			return true;
		} catch (Exception e) {
			log.error(
					"Exception: method ReportExporter.exportMainAndSubReport() - "
							+ e, e);
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
	/**
	 * Purpose:導出多sheet excel報表(包含子报表)
	 * @author HungLi
	 * @param criteria:JasperReport條件設定DTO List集合
	 * @param tempFolder:占存路径
	 * @return 導出是否成功
	 * @throws Exception
	 */
	public static boolean exportReportForSheetsToFile(List<JasperReportCriteriaDTO> criterias,String tempFolder) throws Exception {
		log.debug("Enter method ReportExporter.exportReportForSheets()");
		if (CollectionUtils.isEmpty(criterias)) {
			return false;
		}
		//上傳檔案路徑
		File fileFord = new File(tempFolder);
		//判斷儲存路徑是否存在，若不存在，則重新新建
		if (!fileFord.exists() || !fileFord.isDirectory()) {
			fileFord.mkdirs();
		}
		ByteArrayOutputStream outputStream = null;
		ServletOutputStream oStream = null;
		FileOutputStream fos  = null;
		try {
			int sheetsSize = criterias.size();
			JasperReport[] jasperReports = new JasperReport[sheetsSize];
			JasperReportCriteriaDTO criteria = null;
			List<JasperPrint> prints = new ArrayList<JasperPrint>();
			JasperPrint jasperPrint = null;
			//sheet名稱
			String[] sheetNames = new String[sheetsSize];
//			String type = criterias.get(0).getType();
//			String reportFileName = criterias.get(0).getReportFileName();
			for(int i=0;i<sheetsSize;i++){
				criteria = criterias.get(i);
				// 需要jrXml文檔的方式
				if (criteria.isAutoBuildJasper() == false) {
					String jrXml = criteria.getJrxmlPath()
							+ criteria.getJrxmlName()
							+ JasperReportCriteriaDTO.JRXML_EXT_NAME;
					InputStream inputStream = ReportExporter.class
							.getResourceAsStream(jrXml);
					System.setProperty("javax.xml.parsers.SAXParserFactory",
							"com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
					JasperDesign design = JRXmlLoader.load(inputStream);
					// 編譯報表（.jrxml -> .jasper）
					jasperReports[i] = JasperCompileManager.compileReport(design);
					// 編譯sub報表（.jrxml -> .jasper）
					String subjrXml = null;
					InputStream subinputStream = null;
					JasperDesign subDesign = null;
					JasperReport subJasperReport = null;
					Map<String, String> subjrXmlNames = criteria.getSubjrXmlNames();
					if(subjrXmlNames!=null){
						for(String subjrXmlName: subjrXmlNames.keySet()){
							subjrXml = criteria.getJrxmlPath() + subjrXmlName + JasperReportCriteriaDTO.JRXML_EXT_NAME;
							subinputStream = ReportExporter.class.getResourceAsStream(subjrXml);
						    System.setProperty("javax.xml.parsers.SAXParserFactory","com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
						    subDesign = JRXmlLoader.load(subinputStream);
						    subJasperReport = JasperCompileManager.compileReport(subDesign);
						    // update by riverjin 2015/08/31 
							if(criteria.getParameters() == null){
								Map<String, Object> parameters = new  HashMap<String, Object>();
								criteria.setParameters(parameters);
							}
						    criteria.getParameters().put(subjrXmlNames.get(subjrXmlName).toString(), subJasperReport);
						}
					}
				}
				// 填充報表
				jasperPrint = JasperFillManager.fillReport(
						jasperReports[i], criteria.getParameters(),
						new JRBeanCollectionDataSource(criteria.getResult()));
				prints.add(jasperPrint);
				sheetNames[i] = criteria.getSheetName();
			}
			
			outputStream = new ByteArrayOutputStream();
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(prints));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setSheetNames(sheetNames);
			configuration.setOnePagePerSheet(Boolean.FALSE);
			configuration.setDetectCellType(Boolean.TRUE);
			configuration.setCollapseRowSpan(Boolean.FALSE);
			configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
			configuration.setWhitePageBackground(Boolean.TRUE);
			exporter.setConfiguration(configuration);
			//隱藏邊框
			exporter.exportReport();

			byte[] bytes = outputStream.toByteArray();
			   File file = new File(fileFord, criteria.getReportFileName()+JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL);
			   fos = new FileOutputStream(file);
			   if(null != fos && null != bytes ){
			    fos.write(bytes);
			   }   
			log.debug("Exit method ReportExporter.exportReportToFile()");
			return true;
		} catch (Exception e) {
			log.error("Exception: method ReportExporter.exportMainAndSubReport() - "+ e, e);
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
	/**
	 * Purpose:包含子報表的JasperReport報表匯出
	 * @param criteria:報表匯出DTO
	 * @param subjrXmlNames:子報表集合
	 * @param tempFolder:暫存路徑
	 * @param sheetTitleWidthMap:設置表頭寬度
	 * @throws Exception:出錯時返回
	 * @return boolean:返回結果
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean exportReportToFile(JasperReportCriteriaDTO criteria,Map<String,String> subjrXmlNames,String tempFolder,
			Map<Integer, Map<Integer, Integer>> sheetTitleWidthMap) throws Exception {
		log.debug("Enter method ReportExporter.exportReportToFile()");
		if (criteria == null) {
			return false;
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		FileOutputStream fos  = null;
		try {
			JasperReport jasperReport = null;
			// 需要jrXml文檔的方式
			if (criteria.isAutoBuildJasper() == false) {
				//編譯main報表
				String jrXml = criteria.getJrxmlPath() + criteria.getJrxmlName() + JasperReportCriteriaDTO.JRXML_EXT_NAME;
				InputStream inputStream = ReportExporter.class.getResourceAsStream(jrXml);
			    System.setProperty("javax.xml.parsers.SAXParserFactory","com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
				JasperDesign design = JRXmlLoader.load(inputStream);
				if (sheetTitleWidthMap != null) {
					JRDesignBand cTitle = null;
					JRElement[] es_Title = null;
					Map<Integer, Integer> titleMap = null;
					if (sheetTitleWidthMap.containsKey(1)) {
						cTitle = (JRDesignBand) design.getTitle();
						if (cTitle != null) {
							es_Title = cTitle.getElements();
							if (es_Title != null && es_Title.length > 0) {
								titleMap = sheetTitleWidthMap.get(1);
								for (int j = 0; j < es_Title.length; j++) {
									if (titleMap.containsKey(j)) {
										es_Title[j].setWidth(titleMap.get(j));
									}
								}
							}
						}
					}
				}
				jasperReport = JasperCompileManager.compileReport(design);
				// 編譯sub報表（.jrxml -> .jasper）
				String subjrXml = null;
				InputStream subinputStream = null;
				JasperDesign subDesign = null;
				JasperReport subJasperReport = null;
				if (subjrXmlNames != null) {
					for(String subjrXmlName: subjrXmlNames.keySet()){
						subjrXml = criteria.getJrxmlPath() + subjrXmlName + JasperReportCriteriaDTO.JRXML_EXT_NAME;
						subinputStream = ReportExporter.class.getResourceAsStream(subjrXml);
					    System.setProperty("javax.xml.parsers.SAXParserFactory","com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
					    subDesign = JRXmlLoader.load(subinputStream);
					    if (sheetTitleWidthMap != null) {
							JRDesignBand cTitle = null;
							JRElement[] es_Title = null;
							Map<Integer, Integer> titleMap = null;
							if (sheetTitleWidthMap.containsKey(1)) {
								cTitle = (JRDesignBand) subDesign.getTitle();
								if (cTitle != null) {
									es_Title = cTitle.getElements();
									if (es_Title != null && es_Title.length > 0) {
										titleMap = sheetTitleWidthMap.get(1);
										/*for (int j = 0; j < es_Title.length; j++) {
											if (titleMap.containsKey(j)) {
												es_Title[j].setWidth(titleMap.get(j));
											}
										}*/
										es_Title[0].setWidth(titleMap.get(1));
									}
								}
							}
						}
					    subJasperReport = JasperCompileManager.compileReport(subDesign);
					    if(criteria.getParameters() == null){
							Map<String, Object> parameters = new  HashMap<String, Object>();
							criteria.setParameters(parameters);
						}
						criteria.getParameters().put(subjrXmlNames.get(subjrXmlName).toString(), subJasperReport);
					}
				}
			}
			
			// 填充報表
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, criteria.getParameters(), 
						new JRBeanCollectionDataSource(criteria.getResult()));
			
			JRAbstractExporter exporter = null;
			String reportExtName = "";
			if (StringUtils.hasText(criteria.getType()) == false) {
				exporter = new JRPdfExporter();
				reportExtName = JasperReportCriteriaDTO.REPORT_EXT_NAME_PDF;
			} else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSWORD)) {
				exporter = new JRRtfExporter();
				reportExtName = JasperReportCriteriaDTO.REPORT_EXT_NAME_MSWORD;
			} else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL)) {
				exporter = new JRXlsExporter();
				reportExtName = JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL;
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				String sheetName = criteria.getSheetName();
				configuration.setOnePagePerSheet(true);
				if (StringUtils.hasText(sheetName)) {
					configuration.setSheetNames(new String[]{sheetName});
				}
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporter.setConfiguration(configuration);
			}else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_CSV)){
				exporter = new JRCsvExporter();
				reportExtName = JasperReportCriteriaDTO.REPORT_EXT_NAME_CSV;
			} else {
				exporter = new JRPdfExporter();
				reportExtName = JasperReportCriteriaDTO.REPORT_EXT_NAME_PDF;
			}
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			exporter.exportReport();
			
			byte[] bytes = null;			
			//輸出保教到指定目錄
			bytes = outputStream.toByteArray();			

			log.debug("Report File="+tempFolder+criteria.getReportFileName()+reportExtName);
			File file = new File(tempFolder+criteria.getReportFileName()+reportExtName);
			fos = new FileOutputStream(file);
			if( null != bytes ){
				fos.write(bytes);
			}
			log.debug("Exit method ReportExporter.exportReportToFile()");			
			return true;
		} catch (Exception e) {
			log.error("Exception: method ReportExporter.exportReportToFile() - " + e, e);
			return false;
		} finally {
			if (fos != null) {
				fos.close();
			}		
			outputStream.close();			
		}
	}
	/**
	 * 下載Docx報表
	 * @param criteria - JasperReport條件設定DTO
	 * @param response - HttpServletResponse
	 * @param isTemp 是否保存臨時文件
	 * @return 導出是否成功
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean exportReportDoc(JasperReportCriteriaDTO criteria, HttpServletResponse response, boolean isTemp) throws Exception {
		log.debug("Enter method ReportExporter.exportReportZip()");
		
		if (criteria == null) {
			return false;
		}
		//上傳檔案路徑
		File fileFord = new File(AssetManageFormDTO.WORD_FILE_PATH);
		//判斷儲存路徑是否存在，若不存在，則重新新建
		if (!fileFord.exists() || !fileFord.isDirectory()) {
			fileFord.mkdirs();
		}
		ByteArrayOutputStream outputStream = null;
		ServletOutputStream oStream = null;
		FileOutputStream fos  = null;
		try {
			JasperReport jasperReport = null;
			// 需要jrXml文檔的方式
			if (criteria.isAutoBuildJasper() == false) {
				String jrXml = criteria.getJrxmlPath() + criteria.getJrxmlName() + JasperReportCriteriaDTO.JRXML_EXT_NAME;
				InputStream inputStream = ReportExporter.class.getResourceAsStream(jrXml);
				JasperDesign design = JRXmlLoader.load(inputStream);
				// 編譯報表（.jrxml -> .jasper）
				jasperReport = JasperCompileManager.compileReport(design);
			}
			
			// 填充報表
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, criteria.getParameters(), 
						new JRBeanCollectionDataSource(criteria.getResult()));
			
			outputStream = new ByteArrayOutputStream();
			
			JRAbstractExporter exporter = null;
			if (StringUtils.hasText(criteria.getType()) == false) {
				exporter = new JRPdfExporter();
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				exporter.setConfiguration(configuration);
			} else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSWORD)) {
				exporter = new JRDocxExporter();
				SimpleDocxExporterConfiguration configuration = new SimpleDocxExporterConfiguration();
				exporter.setConfiguration(configuration);
			} else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL)) {
				exporter = new JRXlsExporter();
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				configuration.setOnePagePerSheet(true);
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporter.setConfiguration(configuration);
			} else {
				exporter = new JRPdfExporter();
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				exporter.setConfiguration(configuration);
			}
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			
			exporter.exportReport();
			if (!isTemp) {
				
				byte[] bytes = outputStream.toByteArray();
				response.setContentType(JasperReportCriteriaDTO.getContentTypeMap().get(criteria.getType()));
				response.setCharacterEncoding(JasperReportCriteriaDTO.CHARACTER_ENCODING);
				response.setHeader("Content-disposition", "attachment;filename="
						+ URLEncoder.encode(criteria.getReportFileName(), JasperReportCriteriaDTO.CHARACTER_ENCODING) + JasperReportCriteriaDTO.getReportExtNameMap().get(criteria.getType()));
				response.setContentLength(bytes.length);
				outputStream.close();
				
				oStream = response.getOutputStream();
				oStream.write(bytes, 0, bytes.length);
				oStream.flush();
				oStream.close();
				log.debug("Exit method ReportExporter.exportReportDoc()");
				return true;
			} else {
				byte[] bytes = null;			
				//輸出到指定目錄
				bytes = outputStream.toByteArray();			

				log.debug("Report File="+"/com/cybersoft4u/xian/iatoms/file/word"+criteria.getReportFileName()+"");
				File file = new File(fileFord,criteria.getReportFileName()+".doc");
				   
				//File file = new File( +"/cyber-iatoms-common/src/main/resources/com/cybersoft4u/xian/iatoms/file/"+ criteria.getReportFileName()+"");
				fos = new FileOutputStream(file);
				if( null != bytes ){
					fos.write(bytes);
				}
				log.debug("Exit method ReportExporter.exportReportDoc()");			
				return true;
			}
		} catch (Exception e) {
			log.error("Exception: method ReportExporter.exportReportDoc() - " + e, e);
			return false;
		} finally {
			if (oStream != null) {
				oStream.close();
			}
			if (fos != null) {
				fos.close();
			}		
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}
 
	/**
	 * 導出報表緩存到指定位置
	 * @param criteria - JasperReport條件設定DTO
	 * @param path - 暫存路徑
	 * @return 導出是否成功
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean exportReportUnDownload(JasperReportCriteriaDTO criteria, String path, String name) throws Exception {
		log.debug("Enter method ReportExporter.exportReport()");
		if (criteria == null) {
			return false;
		}
		ByteArrayOutputStream outputStream = null;
		ServletOutputStream oStream = null;
		FileOutputStream fos  = null;
		//上傳檔案路徑
		File fileFord = new File(path);
		//判斷儲存路徑是否存在，若不存在，則重新新建
		if (!fileFord.exists() || !fileFord.isDirectory()) {
			fileFord.mkdirs();
		}
		try {
			JasperReport jasperReport = null;
			// 需要jrXml文檔的方式
			if (criteria.isAutoBuildJasper() == false) {
				String jrXml = criteria.getJrxmlPath() + criteria.getJrxmlName() + JasperReportCriteriaDTO.JRXML_EXT_NAME;
				InputStream inputStream = ReportExporter.class.getResourceAsStream(jrXml);
				JasperDesign design = JRXmlLoader.load(inputStream);
				// 編譯報表（.jrxml -> .jasper）
				jasperReport = JasperCompileManager.compileReport(design);
			}
			// 填充報表
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, criteria.getParameters(), 
						new JRBeanCollectionDataSource(criteria.getResult()));
			outputStream = new ByteArrayOutputStream();
			JRAbstractExporter exporter = null;
			if (StringUtils.hasText(criteria.getType()) == false) {
				exporter = new JRPdfExporter();
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				exporter.setConfiguration(configuration);
			} else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSWORD)) {
				exporter = new JRRtfExporter();
				SimpleRtfReportConfiguration configuration = new SimpleRtfReportConfiguration();
				exporter.setConfiguration(configuration);
			} else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL)) {
				exporter = new JRXlsExporter();
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				String sheetName = criteria.getSheetName();
				configuration.setOnePagePerSheet(true);
				if (StringUtils.hasText(sheetName)) {
					configuration.setSheetNames(new String[]{sheetName});
				}
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporter.setConfiguration(configuration);
			} else {
				exporter = new JRPdfExporter();
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				exporter.setConfiguration(configuration);
			}
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			exporter.exportReport();
			byte[] bytes = null;			
			//輸出保教到指定目錄
			bytes = outputStream.toByteArray();
			
			log.debug("Report File="+path+criteria.getReportFileName()+ JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL);
			File file = new File(path+criteria.getReportFileName()+ JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL);
			fos = new FileOutputStream(file);
			if(bytes != null ){
				//long w = System.currentTimeMillis();
				fos.write(bytes);
			}
			log.debug("Exit method ReportExporter.exportReport()");
			return true;
		} catch (Exception e) {
			log.error("Exception: method ReportExporter.exportReport() - " + e, e);
			return false;
		} finally {
			if (oStream != null) {
				oStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (fos != null) {
				fos.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}
	/**
	 * 導出報表
	 * @param criteria - JasperReport條件設定DTO
	 * @param response - HttpServletResponse
	 * @return 導出是否成功
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean exportReport(JasperReportCriteriaDTO criteria, HttpServletResponse response) throws Exception {
		log.debug("Enter method ReportExporter.exportReport()");
		
		if (criteria == null) {
			return false;
		}
		
		ByteArrayOutputStream outputStream = null;
		ServletOutputStream oStream = null;
		try {
			JasperReport jasperReport = null;
			// 需要jrXml文檔的方式
			if (criteria.isAutoBuildJasper() == false) {
				String jrXml = criteria.getJrxmlPath() + criteria.getJrxmlName() + JasperReportCriteriaDTO.JRXML_EXT_NAME;
				InputStream inputStream = ReportExporter.class.getResourceAsStream(jrXml);
				JasperDesign design = JRXmlLoader.load(inputStream);
				// 編譯報表（.jrxml -> .jasper）
				jasperReport = JasperCompileManager.compileReport(design);
			}
			
			// 填充報表
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, criteria.getParameters(), 
						new JRBeanCollectionDataSource(criteria.getResult()));
			
			outputStream = new ByteArrayOutputStream();
			
			JRAbstractExporter exporter = null;
			if (StringUtils.hasText(criteria.getType()) == false) {
				exporter = new JRPdfExporter();
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				exporter.setConfiguration(configuration);
			} else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSWORD)) {
				exporter = new JRRtfExporter();
				SimpleRtfReportConfiguration configuration = new SimpleRtfReportConfiguration();
				exporter.setConfiguration(configuration);
			} else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL)) {
				exporter = new JRXlsExporter();
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				String sheetName = criteria.getSheetName();
				configuration.setOnePagePerSheet(true);
				if (StringUtils.hasText(sheetName)) {
					configuration.setSheetNames(new String[]{sheetName});
				}
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporter.setConfiguration(configuration);
			} else {
				exporter = new JRPdfExporter();
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				exporter.setConfiguration(configuration);
			}
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			exporter.exportReport();
			
			byte[] bytes = outputStream.toByteArray();
			response.setContentType(JasperReportCriteriaDTO.getContentTypeMap().get(criteria.getType()));
			response.setCharacterEncoding(JasperReportCriteriaDTO.CHARACTER_ENCODING);
			response.setHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(criteria.getReportFileName(), JasperReportCriteriaDTO.CHARACTER_ENCODING) + JasperReportCriteriaDTO.getReportExtNameMap().get(criteria.getType()));
			response.setContentLength(bytes.length);
			outputStream.close();
			
			oStream = response.getOutputStream();
			oStream.write(bytes, 0, bytes.length);
			oStream.flush();
			oStream.close();
			log.debug("Exit method ReportExporter.exportReport()");
			return true;
		} catch (Exception e) {
			log.error("Exception: method ReportExporter.exportReport() - " + e, e);
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
	/**
	 * 導出報表(包含子报表)
	 * 
	 * @param criteria
	 *            - JasperReport條件設定DTO
	 * @param response
	 *            - HttpServletResponse
	 * @return 導出是否成功
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean exportMainAndSubReport(
			JasperReportCriteriaDTO criteria,
			Map<String, String> subjrXmlNames, HttpServletResponse response)
			throws Exception {
		log.debug("Enter method ReportExporter.exportMainAndSubReport()");

		if (criteria == null) {
			return false;
		}
		ByteArrayOutputStream outputStream = null;
		ServletOutputStream oStream = null;
		try {
			JasperReport jasperReport = null;
			// 需要jrXml文檔的方式
			if (criteria.isAutoBuildJasper() == false) {
				String jrXml = criteria.getJrxmlPath()
						+ criteria.getJrxmlName()
						+ JasperReportCriteriaDTO.JRXML_EXT_NAME;
				InputStream inputStream = ReportExporter.class
						.getResourceAsStream(jrXml);
				System.setProperty("javax.xml.parsers.SAXParserFactory",
						"com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
				JasperDesign design = JRXmlLoader.load(inputStream);
				// 編譯報表（.jrxml -> .jasper）
				jasperReport = JasperCompileManager.compileReport(design);
				// 編譯sub報表（.jrxml -> .jasper）
				String subjrXml = null;
				InputStream subinputStream = null;
				JasperDesign subDesign = null;
				JasperReport subJasperReport = null;
				for(String subjrXmlName: subjrXmlNames.keySet()){
					subjrXml = criteria.getJrxmlPath() + subjrXmlName + JasperReportCriteriaDTO.JRXML_EXT_NAME;
					subinputStream = ReportExporter.class.getResourceAsStream(subjrXml);
				    System.setProperty("javax.xml.parsers.SAXParserFactory","com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
				    subDesign = JRXmlLoader.load(subinputStream);
				    subJasperReport = JasperCompileManager.compileReport(subDesign);
					criteria.getParameters().put(subjrXmlNames.get(subjrXmlName).toString(), subJasperReport);
				}
			} else {
				String jasper = criteria.getJrxmlPath() + criteria.getJrxmlName() + JasperReportCriteriaDTO.JASPER_EXT_NAME;
				InputStream inputStream = ReportExporter.class.getResourceAsStream(jasper);
				jasperReport = (JasperReport)JRLoader.loadObject(inputStream);
				
				String subjasper = null;
				InputStream subinputStream = null;
				JasperReport subJasperReport = null;
				for(String subjrXmlName: subjrXmlNames.keySet()){
					subjasper = criteria.getJrxmlPath() + subjrXmlName + JasperReportCriteriaDTO.JASPER_EXT_NAME;
					subinputStream = ReportExporter.class.getResourceAsStream(subjasper);
				    subJasperReport = (JasperReport)JRLoader.loadObject(subinputStream);
					criteria.getParameters().put(subjrXmlNames.get(subjrXmlName).toString(), subJasperReport);
				}
			}

			// 填充報表
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, criteria.getParameters(),
					new JRBeanCollectionDataSource(criteria.getResult()));

			outputStream = new ByteArrayOutputStream();

			JRAbstractExporter exporter = null;
			if (StringUtils.hasText(criteria.getType()) == false) {
				exporter = new JRPdfExporter();
			} else if (criteria.getType().equals(
					JasperReportCriteriaDTO.REPORT_TYPE_MSWORD)) {
				exporter = new JRRtfExporter();
			} else if (criteria.getType().equals(
					JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL)) {
				exporter = new JRXlsExporter();
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				String sheetName = criteria.getSheetName();
				configuration.setOnePagePerSheet(true);
				if (StringUtils.hasText(sheetName)) {
					configuration.setSheetNames(new String[]{sheetName});
				}
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporter.setConfiguration(configuration);
			} else {
				exporter = new JRPdfExporter();
			}
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			exporter.exportReport();

			byte[] bytes = outputStream.toByteArray();
			response.setContentType(JasperReportCriteriaDTO.getContentTypeMap()
					.get(criteria.getType()));
			response.setCharacterEncoding(JasperReportCriteriaDTO.CHARACTER_ENCODING);
			response.setHeader(
					"Content-disposition",
					"attachment;filename="
							+ URLEncoder.encode(criteria.getReportFileName(),
									JasperReportCriteriaDTO.CHARACTER_ENCODING)
							+ JasperReportCriteriaDTO.getReportExtNameMap()
									.get(criteria.getType()));
			response.setContentLength(bytes.length);
			outputStream.close();
			oStream = response.getOutputStream();
			oStream.write(bytes, 0, bytes.length);
			oStream.flush();
			oStream.close();
			log.debug("Exit method ReportExporter.exportMainAndSubReport()");
			return true;
		} catch (Exception e) {
			log.error(
					"Exception: method ReportExporter.exportMainAndSubReport() - "
							+ e, e);
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
	
	/**
	 * 導出報表
	 * @param criteria - JasperReport條件設定DTO
	 * @param response - HttpServletResponse
	 * @return 導出是否成功
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean exportReportDynamicColumns(JasperReportCriteriaDTO criteria, 
			String[] columnsHeader,String[] columnField, HttpServletResponse response) throws Exception {
		log.debug("Enter method ReportExporter.exportReport()");
		
		if (criteria == null) {
			return false;
		}
		
		ByteArrayOutputStream outputStream = null;
		ServletOutputStream oStream = null;
		try {
			JasperReport jasperReport = null;
			// 需要jrXml文檔的方式
			if (criteria.isAutoBuildJasper() == false) {
				String jrXml = criteria.getJrxmlPath() + criteria.getJrxmlName() + JasperReportCriteriaDTO.JRXML_EXT_NAME;
				InputStream inputStream = ReportExporter.class.getResourceAsStream(jrXml);
				JasperDesign jasperDesign = JRXmlLoader.load(inputStream);

				Collection<String> coll = Arrays.asList(columnsHeader);
				jasperDesign = dynamiccolumn(jasperDesign, coll); 
				// 編譯報表（.jrxml -> .jasper）
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
			}
			
			// 填充報表
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, criteria.getParameters(), 
						new JRBeanCollectionDataSource(criteria.getResult()));
			
			outputStream = new ByteArrayOutputStream();
			
			JRAbstractExporter exporter = null;
			if (StringUtils.hasText(criteria.getType()) == false) {
				exporter = new JRPdfExporter();
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				exporter.setConfiguration(configuration);
			} else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSWORD)) {
				exporter = new JRRtfExporter();
				SimpleRtfReportConfiguration configuration = new SimpleRtfReportConfiguration();
				exporter.setConfiguration(configuration);
			} else if (criteria.getType().equals(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL)) {
				exporter = new JRXlsExporter();
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				configuration.setOnePagePerSheet(true);
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporter.setConfiguration(configuration);
			} else {
				exporter = new JRPdfExporter();
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				exporter.setConfiguration(configuration);
			}
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			exporter.exportReport();
			
			byte[] bytes = outputStream.toByteArray();
			response.setContentType(JasperReportCriteriaDTO.getContentTypeMap().get(criteria.getType()));
			response.setCharacterEncoding(JasperReportCriteriaDTO.CHARACTER_ENCODING);
			response.setHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(criteria.getReportFileName(), JasperReportCriteriaDTO.CHARACTER_ENCODING) + JasperReportCriteriaDTO.getReportExtNameMap().get(criteria.getType()));
			response.setContentLength(bytes.length);
			outputStream.close();
			
			oStream = response.getOutputStream();
			oStream.write(bytes, 0, bytes.length);
			oStream.flush();
			oStream.close();
			log.debug("Exit method ReportExporter.exportReport()");
			return true;
		} catch (Exception e) {
			log.error("Exception: method ReportExporter.exportReport() - " + e, e);
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
	
	/**
	 * Purpose:動態列處理
	 * @author evanliu
	 * @param jdesign:JasperDesign
	 * @param dynamiccolumns
	 * @return JasperDesign
	 */
	public static JasperDesign dynamiccolumn(JasperDesign jdesign, Collection<String> dynamiccolumns) {  
        /* 
         * 该方法目前仅进行了简单的处理，如需更多业务，且自行添加 比如：1. 修改元素的位置 2.自动调整Title的宽度 
         * 3.自行调整整个报表的宽度 
         */  
        //Collection dynamiccolumns = (Collection) params.get("dynamiccolumn");  
        if (dynamiccolumns != null) {  
  
            JRDesignBand cHeader = (JRDesignBand) jdesign.getColumnHeader();              
            JRBand cDetailBand = jdesign.getDetailSection().getBands()[0];  
            JRDesignBand cDetail = null;  
            if (cDetailBand != null && cDetailBand instanceof JRDesignBand) {  
                cDetail = (JRDesignBand) cDetailBand;  
            }
            //刪除的列數
            Integer delColNum = 0;
            JRElement[] es_header = cHeader.getElements();  
            JRElement[] es_detail = cDetail.getElements();  
            JRDesignElement e;
            JRStaticText text;
            JRDesignElement ee;
            for (int i = 0; i < es_header.length; i++) {  
                e = (JRDesignElement) es_header[i];  
                String v = "";  
                if (e instanceof JRStaticText) {  
                    text = (JRStaticText) e;  
                    v = text.getText();  
                }  
                if (!dynamiccolumns.contains(v)) {
                    for (int j = 0; j < es_detail.length; j++) {  
                        ee = (JRDesignElement) es_detail[j];  
                        if (e.getX() == ee.getX()) {
                            cDetail.removeElement(ee);  
                            break;
                        }
                    }  
                    cHeader.removeElement(e);  
                    delColNum ++;
                }  
            }
            if (delColNum > 0) {
	            Integer pageWidth = jdesign.getColumnWidth();            
	            Integer colSize = es_header.length - delColNum;
	            Integer colWidthInteger = pageWidth / colSize;
	            es_header = JRElementArraySort(cHeader.getElements());  
	            es_detail = JRElementArraySort(cDetail.getElements());
	            Integer colWidth2 = 0;
	            for (int i = 0; i < es_header.length; i ++) {
	            	e = (JRDesignElement) es_header[i];
	            	ee = (JRDesignElement) es_detail[i];  
	            	e.setX(colWidth2);
	            	ee.setX(colWidth2);
	            	if (i == es_header.length - 1) {
	            		e.setWidth(pageWidth - colWidth2);
	            		ee.setWidth(pageWidth - colWidth2);
	            	} else {
	            		e.setWidth(colWidthInteger);
	            		ee.setWidth(colWidthInteger);
	            	}
	            	colWidth2 += colWidthInteger;
	            }
            }
        }  
        return jdesign;  
    }
	
	/**
	 * Purpose:JRElement數組按照X排序
	 * @author evanliu
	 * @return JRElement[]
	 */
	public static JRElement[] JRElementArraySort(JRElement[] jRElementArray) {
		JRElement tempElement;
		int size = jRElementArray.length;
		for(int i = 0; i < size; i++) {
			for (int j = 0; j < size - i - 1; j ++) {
				if (jRElementArray[j].getX() > jRElementArray[j + 1].getX()) {
					tempElement = jRElementArray[j];
					jRElementArray[j] = jRElementArray[j + 1];
					jRElementArray[j + 1] = tempElement;
				}
			}
		}
		return jRElementArray;
	}

	/*public static void main(String[] args){

		try {
			String jrXml = "/com/fet/cms/common/report/jrxml/CMS_CNTR_CONTENT" + JasperReportCriteriaDTO.JRXML_EXT_NAME;
			InputStream inputStream = ReportExporter.class.getResourceAsStream(jrXml);
			//System.setProperty("javax.xml.parsers.SAXParserFactory","org.apache.xerces.jaxp.SAXParserFactoryImpl");
			 System.setProperty("javax.xml.parsers.SAXParserFactory","com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
			JasperDesign design = JRXmlLoader.load(inputStream);
			// 編譯報表（.jrxml -> .jasper）
			JasperCompileManager.compileReportToFile(design, "E:\\JAVA\\CMS_CNTR_CONTENT.jasper");
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
	
	}*/
}
