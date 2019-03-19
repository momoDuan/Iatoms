package com.cybersoft4u.xian.iatoms.web.controllers;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmEdcLabelDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcLabelFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.services.impl.ContractSlaService;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import cafe.core.bean.Message;
import cafe.core.config.SystemConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.SessionHelper;

/**
 * Purpose:刷卡機標籤管理Controller
 * @author NickLin 
 * @since  JDK 1.7
 * @date   2018/06/27
 * @MaintenancePersonnel CyberSoft
 */
public class EdcLabelController extends AbstractMultiActionController<EdcLabelFormDTO> {
	
	/**
	 * 日誌記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(ApiLogController.class);
	
	/**
	 * Constructor: 無參數建構子
	 */
	public EdcLabelController() {
		this.setCommandClass(EdcLabelFormDTO.class);
	}

	/** 
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(EdcLabelFormDTO command) throws CommonException {
		// TODO Auto-generated method stub
		if (command == null) {
			return false;
		}
		// 獲取actionId
		String actionId = command.getActionId();
		Message msg = null;
		String startDate = command.getQueryStartDate();
		String endDate = command.getQueryStartDate();
		if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
			//時間起
			if (!StringUtils.hasText(startDate)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_EDC_LABEL_START_DATE)});
				throw new CommonException(msg);
			}
			//時間迄
			if (!StringUtils.hasText(endDate)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_EDC_LABEL_END_DATE)});
				throw new CommonException(msg);
			}
		}
		return true;
	}

	/** 
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public EdcLabelFormDTO parse(HttpServletRequest request, EdcLabelFormDTO command)
			throws CommonException {
		// TODO Auto-generated method stub
		try {
			if (command == null) {
				command = new EdcLabelFormDTO();
			}
			//獲取actionId
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			if(!StringUtils.hasText(actionId)){
				actionId = IAtomsConstants.ACTION_INIT;
			} else if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
				//查詢條件:特店代號
				String merchantCode = this.getString(request, EdcLabelFormDTO.QUERY_MERCHANT_CODE);
				LOGGER.debug("EdcLabelController.parse() --> merchantCode: " + merchantCode);
				//拼接'%',後置模糊查詢
				if(StringUtils.hasText(merchantCode)) {
					merchantCode = merchantCode + IAtomsConstants.MARK_PERCENT;
				}
				//查詢條件:DTID
				String dtid = this.getString(request, EdcLabelFormDTO.QUERY_DTID);
				LOGGER.debug("EdcLabelController.parse() --> dtid: " + dtid);
				//拼接'%',後置模糊查詢
				if(StringUtils.hasText(dtid)) {
					dtid = dtid + IAtomsConstants.MARK_PERCENT;
				}
				//查詢條件:匯入日期起
				String startDate = this.getString(request, EdcLabelFormDTO.QUERY_START_DATE);
				LOGGER.debug("EdcLabelController.parse() --> startDate: " + startDate);
				//查詢條件:匯入日期迄
				String endDate = this.getString(request, EdcLabelFormDTO.QUERY_END_DATE);
				LOGGER.debug("EdcLabelController.parse() --> endDate: " + endDate);
				//查詢參數:當前頁碼
				int currentPage = this.getInt(request, IAtomsConstants.QUERY_PAGE_INDEX);
				LOGGER.debug("EdcLabelController.parse() --> currentPage: " + currentPage);
				//查詢參數:總筆數
				int pageSize = this.getInt(request, IAtomsConstants.QUERY_PAGE_ROWS);
				LOGGER.debug("EdcLabelController.parse() --> pageSize: " + pageSize);
				//查詢參數:排序方式
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				if (!StringUtils.hasText(order)) {
					order = EdcLabelFormDTO.PARAM_PAGE_ORDER;
				}
				LOGGER.debug("EdcLabelController.parse() --> order: " + order);
				//查詢參數:排序欄位
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (!StringUtils.hasText(sort)) {
					sort = EdcLabelFormDTO.PARAM_PAGE_SORT;
				}
				LOGGER.debug("EdcLabelController.parse() --> sort: " + sort);
				command.setQueryMerchantCode(merchantCode);
				command.setQueryDtid(dtid);
				command.setQueryStartDate(startDate);
				command.setQueryEndDate(endDate);
				command.getPageNavigation().setCurrentPage(currentPage - 1);
				command.getPageNavigation().setPageSize(pageSize);
				command.setOrder(order);
				command.setSort(sort);
			}
			LOGGER.debug("EdcLabelController.parse() --> actionId: " + actionId);
			command.setActionId(actionId);
		} catch(Exception e) {
			LOGGER.error("EdcLabelController.parse() is error: " + e, e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}
	
	/**
	 * Purpose:刷卡機標籤匯入格式下載
	 * @author NickLin
	 * @param request：請求
	 * @param response：響應
	 * @param command：EdcLabelFormDTO對象
	 * @throws CommonException:出錯時抛出CommonException例外
	 * @return void：無返回值
	 */
	public void download(HttpServletRequest request, HttpServletResponse response, EdcLabelFormDTO command) throws CommonException{
		try {
			//模板名稱
			String fileNameCn = EdcLabelFormDTO.EDC_LABEL_TEMPLATE_NAME_FOR_CN;
			String fileNameEn = EdcLabelFormDTO.EDC_LABEL_TEMPLATE_NAME_FOR_EN;
			//模板路徑
			String tempPath = IAtomsConstants.TEMPLATE_DOWNLOAD_PATH;
			StringBuffer buffer = new StringBuffer();
			buffer.append(tempPath).append(fileNameEn);
			String tempFailString = buffer.toString();
			InputStream inputStream = ContractSlaService.class.getResourceAsStream(tempFailString);
			FileUtils.download(request, response, inputStream, fileNameCn);
		} catch (Exception e) {
			LOGGER.error("EdcLabelController.download() is error: " + e, e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * Purpose:刷卡機標籤匯入
	 * @author NickLin
	 * @param request：請求
	 * @param response：響應
	 * @param command：EdcLabelFormDTO對象
	 * @throws CommonException：出錯時抛出CommonException例外
	 * @return ModelAndView：返回ModelAndView
	 */
	@SuppressWarnings("rawtypes")
	public ModelAndView upload(HttpServletRequest request, HttpServletResponse response, EdcLabelFormDTO command) throws CommonException{
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap(); 
			MultipartFile uploadFiled = null;
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {    
				//上傳檔案 
				uploadFiled = entity.getValue();  
				command.setUploadFiled(uploadFiled);
			}
			//調用service方法
			SessionContext ctx = this.getServiceLocator().doService(null, IAtomsConstants.SERVICE_EDC_LABEL_SERVICE, IAtomsConstants.ACTION_UPLOAD, command);
			//返回 ModelAndView
			return new ModelAndView(this.getSuccessView(IAtomsConstants.ACTION_UPLOAD), IAtomsConstants.PARAM_SESSION_CONTEXT, ctx);
		} catch (Exception e) {
			LOGGER.error("EdcLabelController.upload() is error: " + e, e);
		}
		return null;
	}

	/**
	 * Purpose:匯入錯誤結果文件下載
	 * @author NickLin
	 * @param request：請求
	 * @param response：響應
	 * @param command：EdcLabelFormDTO對象
	 * @throws CommonException：出錯時抛出CommonException例外
	 * @return void：無返回值
	 */
	public void downloadErrorFile(HttpServletRequest request, HttpServletResponse response, EdcLabelFormDTO command) throws CommonException{
		try {
			//下載錯誤訊息檔案
			String fileName = this.getString(request, EdcLabelFormDTO.ERROR_FILE_NAME);
			String tempPath = this.getString(request, EdcLabelFormDTO.TEMP_FILE_PATH);
			String errorFileName = IAtomsConstants.UPLOAD_ERROR_MESSAGE_FILE_NAME;
			String inputfile = tempPath.concat(IAtomsConstants.MARK_BACKSLASH).concat(fileName);
			FileUtils.download(request, response, inputfile, errorFileName);
			FileUtils.removeFile(inputfile);
		} catch (Exception e) {
			LOGGER.error("EdcLabelController.downloadErrorFile() is error: " + e, e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	

	/**
	 * Purpose:匯出刷卡機標籤列印檔
	 * @author NickLin
	 * @param request：請求
	 * @param response：響應
	 * @param command：EdcLabelFormDTO對象
	 * @throws CommonException：出錯時抛出CommonException例外
	 * @return ModelAndView：返回ModelAndView
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public ModelAndView printEdcLabel(HttpServletRequest request, HttpServletResponse response, EdcLabelFormDTO command) throws CommonException {
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			//StringBuffer stringBuffer = null;
			IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
			JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
			SessionContext ctx = this.getServiceLocator().doService(logonUser,
					IAtomsConstants.SERVICE_EDC_LABEL_SERVICE, IAtomsConstants.ACTION_PRINT_EDC_LABEL, command);
			List<DmmEdcLabelDTO> edcLabelDTOs = null;
			if (ctx != null) {
				command = (EdcLabelFormDTO) ctx.getResponseResult();
				edcLabelDTOs = command.getList();
			}
			/*if (edcLabelDTOs.size() > 0) {
				String docName = EdcLabelFormDTO.EDC_LABEL_DOC_TEMPLATE_NAME;
				String filePath = IAtomsConstants.POI_FILE_PATH + docName + JasperReportCriteriaDTO.REPORT_EXT_NAME_MSWORD;
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				String ucNo = this.getUseCaseNo();
				String uuid = UUID.randomUUID().toString();
				//臨時文件夾
				String tempPath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH) + File.separator + ucNo + File.separator + uuid;
				stringBuffer = new StringBuffer();
				stringBuffer.append(File.separator).append(IAtomsConstants.PARAM_TEMPLATE)
					.append(IAtomsConstants.MARK_UNDER_LINE)
					.append(docName)
					.append(IAtomsConstants.MARK_UNDER_LINE)
					.append(yearMonthDay)
					.append(JasperReportCriteriaDTO.REPORT_EXT_NAME_MSWORD);
				FileUtils.copyFileByInputStream(filePath, tempPath, stringBuffer.toString());
			}*/
			if (!CollectionUtils.isEmpty(edcLabelDTOs)) {
				//QRCode 匯出圖片的格式
				String fileFormatter = "png";
				//QRCode 匯出圖片的臨時路徑
				String tempPath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH) +
						File.separator + "QRCode Images";
				//創建QRCode 匯出圖片的臨時資料夾
				File qrcodeFolder = new File(tempPath);
				if (!qrcodeFolder.exists() || !qrcodeFolder.isDirectory()) {
					qrcodeFolder.mkdirs();
				}
				//生成QRCode 的相關設定
				Map <EncodeHintType, Object> hintMap = new HashMap <EncodeHintType, Object> ();
				//字串編碼
				hintMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
				//QRCode 容錯率
				hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
				//QRCode 白框
				hintMap.put(EncodeHintType.MARGIN, 1);
				//QRCode 大小
				int width = 128;
				int height = 128;
				//聯絡資訊頁面URL
				String url = SystemConfigManager.getProperty(IAtomsConstants.QRCODE_URL, IAtomsConstants.QRCODE_URL);
				//生成的QRCode 圖片名稱
				String fileName = tempPath + File.separator + "contactInfo" + IAtomsConstants.MARK_NO + fileFormatter;
	            BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hintMap);
	            matrix = deleteWhite(matrix);
	            MatrixToImageWriter.writeToFile(matrix, fileFormatter, new File(fileName));
				
				// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
				criteria.setAutoBuildJasper(false);
				criteria.setResult(edcLabelDTOs);
				//設置所需報表的Name
				criteria.setJrxmlName(EdcLabelFormDTO.PROJECT_REPORT_JRXML_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSWORD);
				//設置報表Name
				criteria.setReportFileName(EdcLabelFormDTO.PROJECT_REPORT_FILE_NAME);
				criteria.setSheetName(EdcLabelFormDTO.PROJECT_REPORT_FILE_NAME);
				ReportExporter.exportReportDoc(criteria, response, false);
				//成功標誌
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
			}
		} catch (ServiceException e) {
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView(IAtomsConstants.ACTION_PRINT_EDC_LABEL), map);
			} catch (Exception e1) {
				logger.error(".printEdcLabel() is error:", e1);
			}
			LOGGER.error("printEdcLabel()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("printEdcLabel()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return null;
	}
	
	/**
	 * Purpose:去除QRCode外邊的白框
	 * @author NickLin
	 * @param matrix
	 * @return
	 */
	private BitMatrix deleteWhite(BitMatrix matrix) {
		int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;
        
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
	}
}
