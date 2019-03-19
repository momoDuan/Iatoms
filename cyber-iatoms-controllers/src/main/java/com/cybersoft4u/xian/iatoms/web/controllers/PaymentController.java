package com.cybersoft4u.xian.iatoms.web.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.service.ServiceException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.TestJXLS;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PaymentFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
/**
 * Purpose: 求償作業Controller
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/12/6
 * @MaintenancePersonnel CarrieDuan
 */
public class PaymentController extends AbstractMultiActionController<PaymentFormDTO>{

	/**
	 * 日誌記錄器
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, PaymentController.class);
	
	public PaymentController() {
		this.setCommandClass(PaymentFormDTO.class);
	}
	
	@Override
	public boolean validate(PaymentFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			SrmPaymentInfoDTO paymentInfoDTO = command.getPaymentInfoDTO();
			List<SrmPaymentItemDTO> assetTypeDTOs = command.getAssetItemDTOs();
			List<SrmPaymentItemDTO> peripheralSuppliesList = command.getPeripheralSuppliesList();
			String customerId = paymentInfoDTO.getCustomerId();
			String dtid = paymentInfoDTO.getDtid();
			//檢核客戶是否為空
			if (!StringUtils.hasText(customerId) && !StringUtils.hasText(command.getPaymentId())) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_COMPANY_ID)});
				throw new CommonException(msg);
			}
			//檢核DTID是否為空
			if (!StringUtils.hasText(dtid) && !StringUtils.hasText(command.getPaymentId())) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_DTID)});
				throw new CommonException(msg);
			}
			//是否設筆定求償資料
			if (CollectionUtils.isEmpty(peripheralSuppliesList) && CollectionUtils.isEmpty(assetTypeDTOs)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NOT_PAYMENT_INFO);
				throw new CommonException(msg);
			}
			//核檢耗材清單
			if (!CollectionUtils.isEmpty(peripheralSuppliesList)) {
				for (SrmPaymentItemDTO srmPaymentItemDTO : peripheralSuppliesList) {
					//核檢耗材名稱是否輸入
					if (!StringUtils.hasText(srmPaymentItemDTO.getItemName())) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_SUPPILIES_SUPPLIES_NAME)});
						throw new CommonException(msg);
					}
					//核檢數量是否輸入
					if (srmPaymentItemDTO.getSuppliesAmount() != null) {
						if (!ValidateUtils.number(srmPaymentItemDTO.getSuppliesAmount().toString())) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_POSITIVE_NUMBER, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_ASSET_AMOUNT)});
							throw new CommonException(msg);
						}
						if (!ValidateUtils.varcharLength(srmPaymentItemDTO.getSuppliesAmount().toString(), 0, 3)) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_ASSET_AMOUNT), IAtomsConstants.MAXLENGTH_NUMBER_THREE});
							throw new CommonException(msg);
						}
					} else {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_ASSET_AMOUNT)});
						throw new CommonException(msg);
					}
					//核檢求償資訊是否輸入
					if (!StringUtils.hasText(srmPaymentItemDTO.getPaymentReason())) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_PAYMENT_ITEM_REASON_DETAIL)});
						throw new CommonException(msg);
					}
					//核檢求償資訊為自行輸入時，是否輸入說明
					if (StringUtils.hasText(srmPaymentItemDTO.getPaymentReason()) && IAtomsConstants.PAYMENT_REASON.SELF_INPUT.getCode().equals(srmPaymentItemDTO.getPaymentReason())) {
						if (!StringUtils.hasText(srmPaymentItemDTO.getReasonDetail())) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_PAYMENT_ITEM_REASON_DETAIL)});
							throw new CommonException(msg);
						}
						if (!ValidateUtils.length(srmPaymentItemDTO.getReasonDetail(), 0, 100)) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_PAYMENT_ITEM_REASON_DETAIL), IAtomsConstants.MAXLENGTH_NUMBER_ONE_HUNDRED});
							throw new CommonException(msg);
						}
					}
				}
			}
			if (!CollectionUtils.isEmpty(assetTypeDTOs)) {
				for (SrmPaymentItemDTO srmPaymentItemDTO : assetTypeDTOs) {
					//核檢求償資訊是否輸入
					if (!StringUtils.hasText(srmPaymentItemDTO.getPaymentReason()) && IAtomsConstants.YES.equals(srmPaymentItemDTO.getIsPay())) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_PAYMENT_ITEM_REASON_DETAIL)});
						throw new CommonException(msg);
					}
					//核檢求償資訊為自行輸入時，是否輸入說明
					if (StringUtils.hasText(srmPaymentItemDTO.getPaymentReason()) && IAtomsConstants.PAYMENT_REASON.SELF_INPUT.getCode().equals(srmPaymentItemDTO.getPaymentReason())) {
						if (!StringUtils.hasText(srmPaymentItemDTO.getReasonDetail())) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_PAYMENT_ITEM_REASON_DETAIL)});
							throw new CommonException(msg);
						}
						if (!ValidateUtils.length(srmPaymentItemDTO.getReasonDetail(), 0, 100)) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_PAYMENT_ITEM_REASON_DETAIL), IAtomsConstants.MAXLENGTH_NUMBER_ONE_HUNDRED});
							throw new CommonException(msg);
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public PaymentFormDTO parse(HttpServletRequest request, PaymentFormDTO command) throws CommonException {
		try {
			//獲取當前actionId
			String actionId = command.getActionId();
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
				@Override
				public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
					return new Date(json.getAsJsonPrimitive().getAsLong());
				}
			});
			Gson gsons = builder.create();
			if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
				//列表畫面grid查詢
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (StringUtils.hasText(sort)) {
					command.setSort(sort);
				} else {
					command.setSort(PaymentFormDTO.PARAM_SORT);
				}
				//command.setSort(PaymentFormDTO.PARAM_SORT);
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				if (StringUtils.hasText(order)) {
					command.setOrder(order);
				} else {
					command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
				}
				//command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
			} else if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
				// 綁定需要保存的DTO
				SrmPaymentInfoDTO paymentInfoDTO = BindPageDataUtils.bindValueObject(request, SrmPaymentInfoDTO.class);
				// 獲取設備清單
				String assetListRow = command.getAssetTableValue();
				//獲取耗材
				String peripheralSuppliesTableValue = command.getPeripheralSuppliesTableValue();
				Gson gson = new GsonBuilder().create();
				// 
				List<SrmPaymentItemDTO> assetTypeDTOs = gson.fromJson(assetListRow, new TypeToken<List<SrmPaymentItemDTO>>(){}.getType());
				command.setAssetItemDTOs(assetTypeDTOs);
				// 
				List<SrmPaymentItemDTO> peripheralSuppliesList = gson.fromJson(peripheralSuppliesTableValue, new TypeToken<List<SrmPaymentItemDTO>>(){}.getType());
				command.setPeripheralSuppliesList(peripheralSuppliesList);
				command.setPaymentInfoDTO(paymentInfoDTO);
			} else if (IAtomsConstants.ACTION_SEND.equals(actionId) || IAtomsConstants.ACTION_LOCK.equals(actionId) || IAtomsConstants.ACTION_BACK.equals(actionId) 
					|| IAtomsConstants.ACTION_COMPLETE.equals(actionId) || IAtomsConstants.ACTION_REPAYMENT.equals(actionId)) {
				String suppliesList = this.getString(request, PaymentFormDTO.PARAM_SEND_SUPPLIES_LIST);
				List<SrmPaymentItemDTO> paymentItemDTOs = ( List<SrmPaymentItemDTO> ) gsons.fromJson(suppliesList, new TypeToken<List<SrmPaymentItemDTO>>(){}.getType());
				command.setPeripheralSuppliesList(paymentItemDTOs);
			} else if (IAtomsConstants.ACTION_DELETE.equals(actionId)) {
				String paymentId = command.getPaymentId();
			} else if (IAtomsConstants.ACTION_QUERY_DTID.equals(actionId)) {
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (StringUtils.hasText(sort)) {
					command.setSort(sort);
				} else {
					command.setSort(PaymentFormDTO.PARAM_DTID_SORT);
				}
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				if (StringUtils.hasText(order)) {
					command.setOrder(order);
				} else {
					command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
				}
			}
		} catch (Exception e) {
			LOGGER.error("parse()", "Exception.", e);	
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}
	
	/**
	 * Purpose: export
	 * @author CarrieDuan
	 * @param request:HttpServletRequest
	 * @param response:HttpServletResponse
	 * @param command:AdmUserFormDTO
	 * @throws CommonException:發生錯誤時, 丟出Exception
	 * @return ModelAndView
	 */
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, PaymentFormDTO command) throws CommonException {
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			//export();
			String isMicro = request.getParameter("queryMicro");
			if(IAtomsConstants.REPORT_NAME_MONTH_REPORT.equals(isMicro)) {
				command.setIsMicro(true);
			} else {
				command.setIsMicro(false);
			}
			command.setSort(PaymentFormDTO.PARAM_SORT);
			command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
			SessionContext ctx = this.getServiceLocator().doService(command.getLogonUser(), this.getServiceId(), IAtomsConstants.ACTION_EXPORT, command);
			command = (PaymentFormDTO) ctx.getResponseResult();
			JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
			List<SrmPaymentInfoDTO> srmPaymentInfoDTOs = command.getList();
			if (!CollectionUtils.isEmpty(srmPaymentInfoDTOs)) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("paymentInfos", srmPaymentInfoDTOs);
				criteria.setJxlsName("paymentInfos");
				criteria.setJxlsPath("/com/cybersoft4u/xian/iatoms/services/download/Importtemplate/");
				JxlsUtil.exportSalaryTemplet(criteria, map, response);
				//transformer.transformXLS(templateFileName, map, destFileName);
				/*// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
				criteria.setAutoBuildJasper(false);
				criteria.setResult(srmPaymentInfoDTOs);
				//設置所需報表的Name
				criteria.setJrxmlName(PaymentFormDTO.PROJECT_REPORT_JRXML_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setReportFileName(PaymentFormDTO.PROJECT_REPORT_FILE_NAME);
				criteria.setSheetName(PaymentFormDTO.PROJECT_REPORT_FILE_NAME);
				ReportExporter.exportReport(criteria, response);*/
				// 成功標志
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
			}
		} catch (ServiceException e) {
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView(IAtomsConstants.ACTION_EXPORT), map);
			} catch (Exception e1) {
				LOGGER.error(".export() is error:", e1);
			}
			LOGGER.error(".export()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".export()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return null;
	}
	
	public void export(){
		List<TestJXLS> testJXLSs = new ArrayList<TestJXLS>();
		TestJXLS jxls = null;
		for (int i = 0; i < 1; i++) {
			switch (i) {
			case 0:
				//jxls = new TestJXLS("富察蓉音", "26", "女", "皇后");
				jxls = new TestJXLS("張三", 2000, 15698);
				testJXLSs.add(jxls);
				break;
			case 2:
				//jxls = new TestJXLS("魏璎珞", "16", "女", "皇贵妃");
				jxls = new TestJXLS("李四", 3500, 11000);
				testJXLSs.add(jxls);
				break;
			case 3:
				//jxls = new TestJXLS("弘力", "27", "男", "皇上");
				jxls = new TestJXLS("王五", 2200, 21000);
				testJXLSs.add(jxls);
				break;
			case 1:
				//jxls = new TestJXLS("富察傅恒", "20", "男", "御前侍卫");
				jxls = new TestJXLS("朱六", 3100, 5000);
				testJXLSs.add(jxls);
				break;
			case 4:
				//jxls = new TestJXLS("明玉", "19", "女", "一等宫女");
				jxls = new TestJXLS("趙七", 2500, 16010);
				testJXLSs.add(jxls);
				break;
			default:
				break;
			}
		}
//		String templateFileName ="C:\\Users\\felixli.CYBERSOFT\\Desktop\\templateFileName.xlsx";	
		String destFileName ="/home/cybersoft/carrieduan/Test_20180823.xls";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("testJXLSs", testJXLSs);
		map.put("testJXLSs2", testJXLSs);
		//map.put("list2", null);
		XLSTransformer transformer = new XLSTransformer();
		String templateFileName= "/home/cybersoft/carrieduan/go_test/trunk/cyber-iatoms-services/src/main/resources/com/cybersoft4u/xian/iatoms/services/download/Importtemplate/paymentInfos.xlsx";
		try {
			transformer.transformXLS(templateFileName, map, destFileName);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void testSheets() throws ParsePropertyException, InvalidFormatException, IOException{
		 //获取Excel模板文件
	    String filePath = "/home/cybersoft/carrieduan/go_test/trunk/cyber-iatoms-services/src/main/resources/com/cybersoft4u/xian/iatoms/services/download/Importtemplate/testSheet.xls";
	    System.out.println("excel template file:" + filePath);
	    InputStream is = new FileInputStream(filePath);
	  //  FileInputStream is = inputStream;
	    //创建测试数据
	    Map<String, Object> map = new HashMap<String, Object>();
	    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	    Map<String, Object> map1 = new HashMap<String, Object>();
	    map1.put("name", "电视");
	    map1.put("price", "3000");
	    map1.put("desc", "3D电视机");
	    map1.put("备注", "中文测试");
	    Map<String, Object> map2 = new HashMap<String, Object>();
	    map2.put("name", "空调");
	    map2.put("price", "2000");
	    map2.put("desc", "变频空调");
	    map1.put("备注", "测试中文");
	    list.add(map1);
	    list.add(map2);
	    //map.put("list", list);
	 
	    ArrayList<List> objects = new ArrayList<List>();
	    objects.add(list);
	    objects.add(list);
	    objects.add(list);
	    objects.add(list);
	 
	    //sheet的名称
	    List<String> listSheetNames = new ArrayList<String>();
	    listSheetNames.add("1");
	    listSheetNames.add("2");
	    listSheetNames.add("3");
	    listSheetNames.add("4");
	 
	    //调用引擎生成excel报表
	    XLSTransformer transformer = new XLSTransformer();
	    Workbook workbook = transformer.transformMultipleSheetsList(is, objects, listSheetNames, "list", new HashMap(), 0);
	    workbook.write(new FileOutputStream("/home/cybersoft/carrieduan/Test_20180823.xls"));
	 

	}
	
	public static void main(String[] args) {
		new PaymentController().export();
	}
}
