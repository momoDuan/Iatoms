package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CustomerRepoFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.IAtomsUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;

/**
 * 
 * Purpose: 客戶設備總表Controller
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年7月29日
 * @MaintenancePersonnel HermanWang
 */
public class CustomerRepoController extends
		AbstractMultiActionController<CustomerRepoFormDTO> {
	
	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(CustomerRepoController.class);
	
	/**
	 * 設備狀態
	 */
	private Map<String, String> assetStatus;

	/**
	 * Constructor: 無參構造
	 */
	public CustomerRepoController() {
		this.setCommandClass(CustomerRepoFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	public boolean validate(CustomerRepoFormDTO command)
			throws CommonException {
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	public CustomerRepoFormDTO parse(HttpServletRequest request,
			CustomerRepoFormDTO command) throws CommonException {
		try{
			if(command != null) {
				String ucNo = command.getUseCaseNo();
				LOGGER.debug(".parse() --> ucNo:", ucNo);
				// 获取actionId
				String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
				//若actionId爲空,則預設爲init
				if (!StringUtils.hasText(actionId)) {
					actionId = IAtomsConstants.ACTION_INIT;
				}
				command.setActionId(actionId);
				LOGGER.debug(".parse() --> actionId:", actionId);
				if(IAtomsConstants.ACTION_QUERY_DATA.equals(actionId) || IAtomsConstants.ACTION_EXPORT.equals(actionId)){
					// 查詢條件--查询日期
					String queryDate = this.getString(request, CustomerRepoFormDTO.QUERY_PAGE_PARAM_DATE);
					String yyyyMM = IAtomsConstants.MARK_EMPTY_STRING;
					if(StringUtils.hasText(queryDate) && queryDate.contains(IAtomsConstants.MARK_BACKSLASH)){
						yyyyMM = queryDate.replace(IAtomsConstants.MARK_BACKSLASH, IAtomsConstants.MARK_EMPTY_STRING);
					}
					LOGGER.debug(".parse() --> queryDate: ", queryDate);
					// 查詢條件--客户
				//	String queryCustomer = this.getString(request, CustomerRepoFormDTO.QUERY_PAGE_PARAM_CUSTOMER);
					String queryCustomer = command.getQueryCustomer();
					LOGGER.debug(".parse() --> queryCustomer: ", queryCustomer);
					Integer page = command.getPage();
					LOGGER.debug(".parse() --> page: ", page.toString());
					Integer rows = command.getRows();
					LOGGER.debug(".parse() --> rows: ", rows.toString());
					String sort = command.getSort();
					LOGGER.debug(".parse() --> sort: ", sort);
					String order = command.getOrder();
					LOGGER.debug(".parse() --> order: ", order);
					// 查詢條件--维护模式
					String maTypes = this.getString(request, CustomerRepoFormDTO.QUERY_PAGE_PARAM_MA_TYPES);
					LOGGER.debug(".parse() --> maTypes: ", maTypes);
					command.setPage(page);
					command.setRows(rows);
					command.setSort(sort);
					command.setOrder(order);
					command.setQueryCustomer(queryCustomer);
					command.setQueryDate(queryDate);
					command.setYyyyMM(yyyyMM);
					command.setQueryMaType(maTypes);
				}
				command.setAssetStatus(assetStatus);
			}
		}catch(Exception e){
			LOGGER.error(".parse() is error: ", e);
		}
		return command;
	}
	
	/**
	 * 
	 * Purpose: 匯出
	 * @author HermanWang
	 * @param request 請求對象
	 * @param response 相應對象
	 * @param command formDTO
	 * @throws CommonException 錯誤時拋出的Controller異常類
	 * @return void
	 */
	@SuppressWarnings("rawtypes")
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, CustomerRepoFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try{
			command.setQueryCustomer(command.getExportQueryCustomer());
			//根据条件查询报表数据
			SessionContext sessionContext = this.doService(this.getServiceId(), IAtomsConstants.ACTION_EXPORT, command) ;
			if(sessionContext != null){
				//查询结果
				String queryDate = this.getString(request, CustomerRepoFormDTO.QUERY_PAGE_PARAM_DATE);
				String yyyy = null;
				String month = null;
				//拿到當前查詢時間格式為YYYY/MM，切割成 YYYY 和 MM 報表標題用
				if(StringUtils.hasText(queryDate) && queryDate.contains(IAtomsConstants.MARK_BACKSLASH)){
					String[] yyyyMM = queryDate.split(IAtomsConstants.MARK_BACKSLASH);
					if (StringUtils.hasText(queryDate)) {
						yyyy = yyyyMM[0].trim();
						month = yyyyMM[1].trim();
					}
				}
				//拼接成YYYY年MM月的格式
				String date = yyyy + IAtomsConstants.YEAR + month + IAtomsConstants.MONTH;
				command = (CustomerRepoFormDTO) sessionContext.getResponseResult();
				List<RepoStatusReportDTO> results = command.getList();
				List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
				String shortName = null;
				//將map 改為 LinkedHashMap 2017/12/28
				Map<String, List<RepoStatusReportDTO>> map = new LinkedHashMap<String, List<RepoStatusReportDTO>>();
				List<RepoStatusReportDTO> list = null;
				//遍歷results，拿到不重複的客戶的個數，以及每個公司對應的設備數量
				for (RepoStatusReportDTO repoStatusReportDTO : results) {
					shortName = repoStatusReportDTO.getShortName();
					list = map.get(shortName);
					if (list == null) {
						list = new ArrayList<RepoStatusReportDTO>();
					}
					list.add(repoStatusReportDTO);
					map.put(shortName, list);
				}
				if (!CollectionUtils.isEmpty(results)) {
					//JasperReport條件設定DTO
					JasperReportCriteriaDTO criteria = null;
					List<CrossTabReportDTO> crossTabReportDTOList = null;
					CrossTabReportDTO crossTabDTO = null;
					//List<String> columnNameTypeList = null;
					//List<String> AssetNameList = null;
					
					//列名称
					String columnName = IAtomsConstants.MARK_EMPTY_STRING;
					String columnNameType = IAtomsConstants.MARK_EMPTY_STRING;
					//對應欄位
					String content = IAtomsConstants.MARK_EMPTY_STRING;
					//RepoStatusReportDTO dto = null;
					//從對應的狀態列表取出每個狀態
					Set<String> keySet = null;
					if(assetStatus != null) {
						keySet = assetStatus.keySet();
					}
					//主報表裡面的參數
					Map<String, String> subjrXmlNames = new HashMap<String, String>();
					//把主報表裡面的子報表名替換成真正的子報表名
					subjrXmlNames.put(CustomerRepoFormDTO.CUSTOMER_REPOSITORY_SUBREPORT, CustomerRepoFormDTO.SUBREPORT_DIR);
					List<CustomerRepoFormDTO> formDTOs = null;
					CustomerRepoFormDTO tempFormDTO = null;
					//存放每個sheet，以及每個sheet裡面存放的title數量，和title的寬度
					Map<Integer, Map<Integer, Integer>> sheetTitleWidthMap = new HashMap<Integer, Map<Integer,Integer>>();
					//公司的數量
					int companyCount = 0;
					Map<Integer, Integer> titleWidthMap = null;
					//Map<Integer, Integer> summaryWidthMap = null;
					//循環公司
					for(String key : map.keySet()){
						titleWidthMap = new HashMap<Integer, Integer>();
						//summaryWidthMap = new HashMap<Integer, Integer>();
						crossTabReportDTOList = new ArrayList<CrossTabReportDTO>();
						criteria = new JasperReportCriteriaDTO();
						list = map.get(key);
						int width = 0;
						int summaryEdcCount = 0;
						int summaryRelatedCount = 0;
						int edcWidth = 0;
						int relatedWidth = 0;
						//AssetNameList = new ArrayList<String>();
						Map titleNameMap = new HashMap();
						//循環公司對應的設備
						for(RepoStatusReportDTO repoStatusReportDTO : list){
							columnNameType = repoStatusReportDTO.getAssetCategory();
							columnName = repoStatusReportDTO.getAssetTypeName();
							if(columnNameType.equals(IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET)){
								columnNameType = IAtomsConstants.ASSET_CATEGORY_RELATED_PRODUCTS;
								summaryRelatedCount ++;
							} else {
								summaryEdcCount++;
							}
							shortName = repoStatusReportDTO.getShortName();
							
							//設置報表的標題名字(YYYY年MM月XX公司設備庫存表)
							titleNameMap.put(IAtomsConstants.STRING_EXPORT, date + IAtomsConstants.MARK_SPACE + shortName + IAtomsConstants.ASSET_STOCK_TABLE_NAME);
							//columnNameTypeList = new ArrayList<String>();
							//if(!columnNameType.contains(columnNameType)){
							//	columnNameTypeList.add(columnNameType);
							//}
							if(columnNameType.equals(IAtomsConstants.ASSET_CATEGORY_EDC) 
									&& titleNameMap.get(IAtomsConstants.COLUMN_NAME_TYPE_EDC) == null){
								titleNameMap.put(IAtomsConstants.COLUMN_NAME_TYPE_EDC, columnNameType);
							}
							if (columnNameType.equals(IAtomsConstants.ASSET_CATEGORY_RELATED_PRODUCTS)
									&& titleNameMap.get(IAtomsConstants.COLUMN_NAME_TYPE) == null){
								titleNameMap.put(IAtomsConstants.COLUMN_NAME_TYPE, columnNameType);
							}
							/*AssetNameList.add(columnName);
							if(AssetNameList.size()==list.size()){
								titleNameMap.put("columnName", columnName);
							}*/
							criteria = new JasperReportCriteriaDTO();
							criteria.setParameters(titleNameMap);
							//計數器，循環到狀態列表的倒數第二個停止.
							int i = 0;
							//循環每個設備對應的狀態
							for(String field : keySet){
								if(i < (keySet.size() - 1)){
									i++;
									content = IAtomsUtils.getFiledValueByName(field, repoStatusReportDTO);
									crossTabDTO = new CrossTabReportDTO();
									//如果狀態名稱為總計，那麼將他變為合計
									if(field.equals(IAtomsConstants.STRING_TOTAL)){
										field = IAtomsConstants.STRING_GRAND;
									}
									crossTabDTO.setRowName(this.assetStatus.get(field));
									crossTabDTO.setColumnName(columnName);
									crossTabDTO.setColumnNameType(columnNameType);
									crossTabDTO.setIntContent(Integer.parseInt(content));
									crossTabReportDTOList.add(crossTabDTO);
								}
							}
							//拿出來要設置的標題的寬度
							width = (list.size() + 1) * 150;
						}
						edcWidth = summaryEdcCount * 150;
						relatedWidth = summaryRelatedCount * 150;
						//將要設置的標題寬度存入寬度的map
						titleWidthMap.put(Integer.valueOf(0), Integer.valueOf(width));
						titleWidthMap.put(Integer.valueOf(1), Integer.valueOf(edcWidth));
						titleWidthMap.put(Integer.valueOf(2), Integer.valueOf(relatedWidth));
						//主報表
						formDTOs = new ArrayList<CustomerRepoFormDTO>();
						tempFormDTO = new CustomerRepoFormDTO();
						tempFormDTO.setCrossTabReportDTOList(crossTabReportDTOList);
						//將formDTOs放在criteria裡面
						formDTOs.add(tempFormDTO);
						criteria.setResult(formDTOs);
						// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
						criteria.setAutoBuildJasper(false);
						//設置所需報表的Name
						criteria.setJrxmlName(CustomerRepoFormDTO.EXPORT_JRXML_NAME);
						//criteria.setSubjrXmlNames(subjrXmlNames);
						//設置報表路徑
						criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
						criteria.setSheetName(key);
						//設置匯出格式
						criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
						//設置報表Name
						criteria.setReportFileName(i18NUtil.getName(CustomerRepoFormDTO.EXPORT_FILE_NAME));
						criteria.setSubjrXmlNames(subjrXmlNames);
						criterias.add(criteria);
						//將要設置寬度的sheet，以及要設置的標題和標題寬度存入sheetTitleWidthMap
						sheetTitleWidthMap.put(Integer.valueOf(companyCount), titleWidthMap);
						companyCount ++;
					}
					ReportExporter.exportReportForSheetsAndCrossTab(criterias, response, sheetTitleWidthMap);
					// 成功標志
					SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
				}
			}
		}catch(Exception e){
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
			LOGGER.error(".export() is error:", e);
		}
		return null;
	}
	
	/**
	 * @return the assetStatus
	 */
	public Map<String, String> getAssetStatus() {
		return assetStatus;
	}

	/**
	 * @param assetStatus the assetStatus to set
	 */
	public void setAssetStatus(Map<String, String> assetStatus) {
		this.assetStatus = assetStatus;
	}
	
}
