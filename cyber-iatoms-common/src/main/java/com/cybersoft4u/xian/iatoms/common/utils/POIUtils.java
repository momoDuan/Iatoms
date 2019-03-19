package com.cybersoft4u.xian.iatoms.common.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.springframework.util.CollectionUtils;

import cafe.core.config.GenericConfigManager;
import cafe.core.config.SystemConfigManager;
import cafe.core.util.BeanUtils;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO;
/**
 * Purpose: 調用poi一些copy的方法。主要用於工單列印以及案件匯出等.
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/7/26
 * @MaintenancePersonnel HermanWang
 */
public class POIUtils {
	
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.COMMON, POIUtils.class);
	/**
	 * Purpose:：拷贝sheet copySheet(targetSheet, sourceSheet, targetWork, sourceWork, true)
	 * @author HermanWang
	 * @param targetSheet：目標sheet
	 * @param sourceSheet：原sheet
	 * @param targetWork：目標表文件
	 * @param sourceWork：源表 
	 * @param srmCaseHandleInfoDTOList：要列印的行
	 * @param editFildsMap：每種交易類別支持的交易參數map
	 * @throws Exception
	 * @return void
	 */
	public void copySheet(HSSFSheet targetSheet, HSSFSheet sourceSheet,
			HSSFWorkbook targetWork, HSSFWorkbook sourceWork, 
			List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOList, Map<String,List<String>> editFildsMap) throws Exception{
		if(targetSheet == null || sourceSheet == null || targetWork == null || sourceWork == null){
			throw new IllegalArgumentException("调用PoiUtil.copySheet()方法时，targetSheet、sourceSheet、targetWork、sourceWork都不能为空，故抛出该异常！");
		}
		copySheetAndSetValue(targetSheet, sourceSheet, targetWork, sourceWork, true, srmCaseHandleInfoDTOList, editFildsMap);
	}
	/**
	 * Purpose：拷贝sheet
	 * @author HermanWang
	 * @param targetSheet：目標sheet
	 * @param sourceSheet：原sheet
	 * @param targetWork：目標表文件
	 * @param sourceWork：源表
	 * @param copyStyle：是否copy樣式
	 * @param srmCaseHandleInfoDTOList：要列印的行
	 * @param editFildsMap：每種交易類別支持的交易參數map
	 * @throws Exception
	 * @return void
	 */
	public void copySheetAndSetValue(HSSFSheet targetSheet, HSSFSheet sourceSheet,
			HSSFWorkbook targetWork, HSSFWorkbook sourceWork, boolean copyStyle,
			List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOList, Map<String,List<String>> editFildsMap)throws Exception {
		
		if(targetSheet == null || sourceSheet == null || targetWork == null || sourceWork == null){
			throw new IllegalArgumentException("调用PoiUtil.copySheet()方法时，targetSheet、sourceSheet、targetWork、sourceWork都不能为空，故抛出该异常！");
		}
		//复制源表中的列數目
		int maxColumnNum = 0;
		//設置樣式
		Map styleMap = (copyStyle) ? new HashMap() : null;
		HSSFPatriarch patriarch = targetSheet.createDrawingPatriarch(); //用于复制注释
		int pageSize = sourceSheet.getLastRowNum() - sourceSheet.getFirstRowNum() + 1;
		//選取的案件dtoList
		for(int j=0;j<srmCaseHandleInfoDTOList.size();j++){
			//讀取源表的所有行
			for (int i = sourceSheet.getFirstRowNum(); i <= sourceSheet.getLastRowNum(); i++) {
				//源表的行
				HSSFRow sourceRow = sourceSheet.getRow(i);
				//目標表的行
				HSSFRow targetRow = targetSheet.createRow(i+(j*(sourceSheet.getLastRowNum()+1)));
				//拿到具體的案件資訊進行列印
				SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = srmCaseHandleInfoDTOList.get(j);
				if (sourceRow != null) {
					copyRowAndSetValue(targetRow, sourceRow, targetWork, sourceWork, patriarch, styleMap,
							srmCaseHandleInfoDTO, editFildsMap);
					if (sourceRow.getLastCellNum() > maxColumnNum) {
						maxColumnNum = sourceRow.getLastCellNum();
					}
				}
			}
			//复制源表中的合并单元格
			mergerRegion1(targetSheet, sourceSheet, srmCaseHandleInfoDTOList.size());
			//copy圖片
			List<HSSFPictureData> pictures = sourceWork.getAllPictures();
			if(sourceSheet.getDrawingPatriarch() != null) {
				for (HSSFShape shape : sourceSheet.getDrawingPatriarch().getChildren()) {  
					//坐標位置
					HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();  
					if (shape instanceof HSSFPicture) {  
						HSSFPicture pic = (HSSFPicture) shape;  
						int pictureIndex = pic.getPictureIndex()-1;  
						HSSFClientAnchor targetAnchor = new HSSFClientAnchor
								(anchor.getDx1(), anchor.getDy1(), 
										anchor.getDx2(),  anchor.getDy2(),
										anchor.getCol1(), j*pageSize, 
										anchor.getCol2(), j*pageSize + 1);
						targetAnchor.setAnchorType(anchor.getAnchorType());
						//插入图片    
						HSSFPictureData picData = pictures.get(pictureIndex);
						patriarch.createPicture(targetAnchor, targetWork.addPicture(picData.getData(), anchor.getAnchorType()));
					}  
				}
			}
			//添加分頁符
			targetSheet.setRowBreak((j + 1)*pageSize - 1);
		}
		//设置目标sheet的列宽
		for (int i = 0; i <= maxColumnNum; i++) {
			targetSheet.setColumnWidth(i, sourceSheet.getColumnWidth(i));
		}
	}
	
	/**
	 * Purpose：拷贝row
	 * @author HermanWang
	 * @param targetRow:目標行
	 * @param sourceRow：源表的行
	 * @param targetWork：　目標表
	 * @param sourceWork：　源表
	 * @param targetPatriarch：copy單元格註釋的數據
	 * @param styleMap：是否copy樣式
	 * @param srmCaseHandleInfoDTO：正在處理的案件dto
	 * @param editFildsMap：每種交易類別支持的交易參數map
	 * @throws Exception
	 * @return void
	 */
	public void copyRowAndSetValue(HSSFRow targetRow, HSSFRow sourceRow,
			HSSFWorkbook targetWork, HSSFWorkbook sourceWork,HSSFPatriarch targetPatriarch, Map styleMap,
			SrmCaseHandleInfoDTO srmCaseHandleInfoDTO, Map<String,List<String>> editFildsMap) throws Exception {
		if(targetRow == null || sourceRow == null || targetWork == null || sourceWork == null || targetPatriarch == null){
			throw new IllegalArgumentException("调用PoiUtil.copyRow()方法时，targetRow、sourceRow、targetWork、sourceWork、targetPatriarch都不能为空，故抛出该异常！");
		}
		//设置行高
		targetRow.setHeight(sourceRow.getHeight());
		//循環原表的行.
		//sourceRow.getFirstCellNum() 原表的行的第一列    sourceRow.getLastCellNum()  原表的行的最後一列
		for (int i = sourceRow.getFirstCellNum(); i <= sourceRow.getLastCellNum(); i++) {
			//原行裡面的第I個cell
			HSSFCell sourceCell = sourceRow.getCell(i);
			//目標cell
			HSSFCell targetCell = targetRow.getCell(i);
			if (sourceCell != null) {
				//創建目標cell
				if (targetCell == null) {
					targetCell = targetRow.createCell(i);
				}
				//拷贝单元格，包括内容和样式
				copyCellAndSetValue(targetCell, sourceCell, targetWork, sourceWork, styleMap, 
						srmCaseHandleInfoDTO, editFildsMap);
				
				//拷贝单元格注释
				//copyComment(targetCell,sourceCell,targetPatriarch);
			}
		}
	}
	
	/**
	 * Purpose:拷贝cell，依据styleMap是否为空判断是否拷贝单元格样式
	 * @author HermanWang
	 * @param targetCell:目標單元格			不能为空
	 * @param sourceCell：源單元格			不能为空
	 * @param targetWork：目標表			不能为空
	 * @param sourceWork：源表			不能为空
	 * @param styleMap 樣式mapnewsheet
	 * @param srmCaseHandleInfoDTO:正在處理中的案件信息
	 * @param editFildsMap：支持的交易參數map
	 * @return void
	 */
	public void copyCellAndSetValue(HSSFCell targetCell, HSSFCell sourceCell, HSSFWorkbook targetWork, 
			HSSFWorkbook sourceWork, Map styleMap,
			SrmCaseHandleInfoDTO srmCaseHandleInfoDTO, Map<String,List<String>> editFildsMap) {
		if(targetCell == null || sourceCell == null || targetWork == null || sourceWork == null ){
			throw new IllegalArgumentException("调用PoiUtil.copyCell()方法时，targetCell、sourceCell、targetWork、sourceWork都不能为空，故抛出该异常！");
		}
		
		boolean isTest = false;
		if((IAtomsConstants.MARK_CHINESE_BRACKET_LEFT+IAtomsConstants.MARK_CHINESE_BRACKET_RIGHT).equals(srmCaseHandleInfoDTO.getSoftwareVersionName())) {
			srmCaseHandleInfoDTO.setSoftwareVersionName("");
		}
		//Task #2705 2017/11/02
		String cellContentByNull = null;
		//处理单元格内容
		switch (sourceCell.getCellType()) {
			case HSSFCell.CELL_TYPE_STRING:
				String[] cellContents = null;
				//如果源表cell數據包含 "{"   並且包含  "}"
				if(sourceCell.getRichStringCellValue().toString().contains("{") 
						&& sourceCell.getRichStringCellValue().toString().contains("}")){
					//拿到源cell的value
					String cellValue = sourceCell.getRichStringCellValue().toString();
					//通過 "}"截取
					cellContents = sourceCell.getRichStringCellValue().toString().split("}");
					//循環value截取后的數組
					for(String cellContent : cellContents){
						//模板上面的{XXX}裡面的code XXX
						cellContent = cellContent.substring(cellContent.indexOf("{")+1);
						Object value = null;
						//刷卡機參數和空白套印的商店地址欄位 裝機件：裝機地址 異動件：裝機地址 
						//併機件：聯繫地址 拆機件：聯繫地址 查核件：聯繫地址 專案件：聯繫地址 報修件：聯繫地址
						if(cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue())
								&& (IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory()) ||
										IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory()))) {
							//裝機地址
							value = getFieldValueByName(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue(), srmCaseHandleInfoDTO);
						} else if(cellContent.equals("contactPerson")) {
							//裝機和異動 抓 裝機聯絡人
							if((IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory()) ||
									IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory()))) {
								//裝機聯絡人
								value = getFieldValueByName(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue(), srmCaseHandleInfoDTO);
							//其他案件類型 抓 聯繫 聯絡人	
							} else {
								//聯繫 聯絡人
								value = getFieldValueByName(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue(), srmCaseHandleInfoDTO);
							}
							//沒有再抓特店聯絡人
							if(value == null) {
								value = getFieldValueByName(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT.getValue(), srmCaseHandleInfoDTO);
							}
						} else if(cellContent.equals("contactTelephone")) {
							//裝機和異動 抓 裝機聯絡人電話
							if((IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory()) ||
									IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory()))) {
								//裝機聯絡人電話
								value = getFieldValueByName(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue(), srmCaseHandleInfoDTO);
							//其他案件類型 抓 聯繫聯絡人電話	
							} else {
								//聯繫聯絡人電話	
								value = getFieldValueByName(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue(), srmCaseHandleInfoDTO);
							}
							//沒有再抓特店聯絡人電話
							if(value == null) {
								value = getFieldValueByName(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL.getValue(), srmCaseHandleInfoDTO);
							}
						} else {
							if(cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_INSTALL.getValue())) {
								value = getFieldValueByName(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue(), srmCaseHandleInfoDTO);
							} else {
								if (cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue() + "ByNull")
										|| cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_SERIAL_NUMBER.getValue() + "ByNull")
										|| cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_SERIAL_NUMBER.getValue() + "ByNull")
										|| cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_SERIAL_NUMBER.getValue() + "ByNull")
										|| cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue() + "ByNull")) {
									cellContentByNull =  cellContent.replace("ByNull", "");
									//Task #2705 2017/11/02 根據反射從srmCaseHandleInfoDTO裡面取出對應的value
									value = getFieldValueByName(cellContentByNull, srmCaseHandleInfoDTO);
									if (value != null) {
										if (cellContentByNull.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue())) {
											value = "週邊設備3:" + value;
										} else {
											value = "/SN:" + value;
										}
									}
								} else {
									//根據反射從srmCaseHandleInfoDTO裡面取出對應的value
									value = getFieldValueByName(cellContent, srmCaseHandleInfoDTO);
								}
								
							}
						}
						if(value != null){
							//案件類別 格式化
							if(StringUtils.hasText(value.toString())) {
								if(cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue()) ||
										//是否專案
										cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue()) ||
										cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue()) ||
										//是否vip
										cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_VIP.getValue()) ||
										//是否同裝機地址
										cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue()) ||
										//電子發票載具
										cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue()) ||
										//銀聯閃付
										cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue()) ||
										//是否開啟加密
										cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue()) ||
										//電子化繳費平台
										cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue()) ||
										//logo
										cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue())) {
									value = i18NUtil.getName(value.toString());
								} 
								//Bug #2403
								/*if(cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue())) {
									value = DateTimeUtils.toString( (Date)value,  DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_SLASH);
								}*/
								if(cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue())
										|| cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue())
										|| cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue())) {
									value = DateTimeUtils.toString( (Date)value,  DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
								}
								if(cellContent.equals(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue())) {
									if(value.toString().indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > -1) {
										value = value.toString().split(IAtomsConstants.MARK_SLASH + IAtomsConstants.MARK_BRACKETS_LEFT)[0];
									}
								}
								//替換模板上面的值
								cellValue = cellValue.replace("{"+cellContent+"}", value.toString());
								//該筆案件對應的交易參數dtolist
								srmCaseTransactionParameterDTOList= srmCaseHandleInfoDTO.getCaseTransactionParameterDTOs();
							} else {
								//設置值 為空
								cellValue = cellValue.replace("{"+cellContent+"}", "");
							}
						} else {
							//設置值 為空
							cellValue = cellValue.replace("{"+cellContent+"}", "");
						}
					}
					HSSFRichTextString textString = new HSSFRichTextString(cellValue);
					targetCell.setCellValue(textString);
				//如果源表cell數據包含 "["   並且包含  "]" 說明到達交易參數
				} else if(sourceCell.getRichStringCellValue().toString().contains(IAtomsConstants.MARK_BRACKET_LEFT) 
						&& sourceCell.getRichStringCellValue().toString().contains(IAtomsConstants.MARK_BRACKET_RIGHT)) {
					if(sourceCell.getRowIndex() != row){
						cellColumnMap.clear();
					}
					//記錄下交易參數開始的行號
					row = sourceCell.getRowIndex();
					//key 是 交易參數模板上的 列號。value是模板上面的[XXX]的codeXXX
					cellColumnMap.put(sourceCell.getColumnIndex(), sourceCell.getRichStringCellValue().toString());
					//替換值
					String cellValue = sourceCell.getRichStringCellValue().toString();
					//通過 "]"截取開
					cellContents = sourceCell.getRichStringCellValue().toString().split(IAtomsConstants.MARK_BRACKET_RIGHT);
					for(String cellContent : cellContents){
						//取出模板上 的code 
						cellContent = cellContent.substring(cellContent.indexOf(IAtomsConstants.MARK_BRACKET_LEFT)+1);
						//拿到交易參數dto的所有屬性
						if(!CollectionUtils.isEmpty(srmCaseTransactionParameterDTOList)) {
							String[] files = getFiledName(srmCaseTransactionParameterDTOList.get(0));
							//判斷是否dto屬性的標誌位
							boolean isFiled = false;
							Object transactionTypeCode = null;
							for (String file : files) {
								//如果是dto的一個屬性
								if(file.equals(cellContent)) {
									//如果是交易類別  code ，替換成交易類別 name
									if(file.equals(SrmCaseTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue())) {
										//記錄下當前的交易類別
										transactionTypeCode = getFieldValueByName(file, srmCaseTransactionParameterDTOList.get(0));
										//拿到該交易類別可支持的交易參數list
										editFilds = editFildsMap.get(transactionTypeCode);
										file = SrmCaseTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE_NAME.getValue();
									}
									//根據反射 取到對應的值
									Object value = getFieldValueByName(file, srmCaseTransactionParameterDTOList.get(0));
									if(value != null) {
										cellValue = cellValue.replace(IAtomsConstants.MARK_BRACKET_LEFT+cellContent+IAtomsConstants.MARK_BRACKET_RIGHT, value.toString());
									} else {
										cellValue = cellValue.replace(IAtomsConstants.MARK_BRACKET_LEFT+cellContent+IAtomsConstants.MARK_BRACKET_RIGHT, "");
									}
									isFiled = true;
									// 放置特店代號
									if(cellContent.equals(SrmCaseTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE.getValue())){
										cellContent = IAtomsConstants.PARAMTER_ITEM_CODE_MERCHANT_CODE;
										// 放置DTID
									} else if(cellContent.equals(SrmCaseTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE_OTHER.getValue())) {
										cellContent = IAtomsConstants.PARAMTER_ITEM_CODE_MERCHANT_CODE_OTHER;
										// 放置DTID
									} else if(cellContent.equals(SrmCaseTransactionParameterDTO.ATTRIBUTE.DTID.getValue())){
										cellContent = IAtomsConstants.PARAMTER_ITEM_CODE_DTID;
										// 放置tid
									} else if(cellContent.equals(SrmCaseTransactionParameterDTO.ATTRIBUTE.TID.getValue())){
										cellContent = IAtomsConstants.PARAMTER_ITEM_CODE_TID;
									}
									//交易類別 code 
									if(!SrmCaseTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue().equals(cellContent) && !IAtomsConstants.PARAMTER_ITEM_CODE_DTID.equals(cellContent)) {
										// 固定列 不支持交易參數.
										// Task #2721 所有類別都要能列印 無交易參數也可列印
										if(StringUtils.hasText(cellContent) && editFilds != null && !editFilds.contains(cellContent)) {
											isTest = true;
										}
									}
									break;
								}
							}
							//如果不是固定列
							if(!isFiled) {
								//如果屬於item_value轉換成的map
								// Task #2721 所有類別都要能列印 無交易參數也可列印
								if(srmCaseTransactionParameterDTOList.get(0) != null 
										&& srmCaseTransactionParameterDTOList.get(0).getSrmCaseTransactionParametermap() != null 
										&& srmCaseTransactionParameterDTOList.get(0).getSrmCaseTransactionParametermap().get(cellContent)!=null){
									cellValue = cellValue.replace(IAtomsConstants.MARK_BRACKET_LEFT+cellContent+IAtomsConstants.MARK_BRACKET_RIGHT, (String) srmCaseTransactionParameterDTOList.get(0).getSrmCaseTransactionParametermap().get(cellContent));
								} else {
									//既不屬於 固定列，又不屬於 item_value轉換成的map 
									if(sourceCell.getColumnIndex() == 0) {
										cellValue = cellValue.replace(IAtomsConstants.MARK_BRACKET_LEFT+cellContent+IAtomsConstants.MARK_BRACKET_RIGHT, IAtomsConstants.COLUMN_V);
									} else {
										cellValue = cellValue.replace(IAtomsConstants.MARK_BRACKET_LEFT+cellContent+IAtomsConstants.MARK_BRACKET_RIGHT, "");
									}
								}
								//不給第一列設置背景色
								if(sourceCell.getColumnIndex() != 0) {
									//不是固定列，不支持 交易參數
									// Task #2721 所有類別都要能列印 無交易參數也可列印
									if(editFilds != null && !editFilds.contains(cellContent)) {
										isTest = true;
									}
								}
							}
						}
					}
					HSSFRichTextString textString = new HSSFRichTextString(cellValue);
					targetCell.setCellValue(textString);
				} else {
					targetCell.setCellValue(sourceCell.getRichStringCellValue());
				}
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				targetCell.setCellValue(sourceCell.getNumericCellValue());
				break;
			//如果模板上單元格為空（多筆交易參數模板只寫一行code）
			case HSSFCell.CELL_TYPE_BLANK:
				targetCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
				if(row != 0) {
					//交易參數開始行 加上 此案件總共的交易參數筆數
					if(row < sourceCell.getRowIndex() && sourceCell.getRowIndex() < row + srmCaseTransactionParameterDTOList.size()) {
						//取出模板上面的code
						if(cellColumnMap.get(sourceCell.getColumnIndex()) != null) {
							//拿到dto所有的files屬性
							String[] files = getFiledName(srmCaseTransactionParameterDTOList.get(sourceCell.getRowIndex() - row));
							//模板上的[XX]換成XX
							String cellContent = cellColumnMap.get(sourceCell.getColumnIndex()).toString().replace("[", "").replace("]", "");
							String cellValue = null;
							boolean isFiled = false;
							for (String file : files) {
								//等於 dto裡面的固定列
								if(file.equals(cellContent)) {
									//如果是交易類別  code ，替換成交易類別 name
									if(file.equals(SrmCaseTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue())) {
										//記錄下當前的交易類別
										Object transactionTypeCode = getFieldValueByName(file, srmCaseTransactionParameterDTOList.get(sourceCell.getRowIndex() - row));
										//拿到該交易類別可支持的交易參數list
										editFilds = editFildsMap.get(transactionTypeCode);
										file = SrmCaseTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE_NAME.getValue();
									}
									//根據反射 拿到 value
									Object value = getFieldValueByName(file, srmCaseTransactionParameterDTOList.get(sourceCell.getRowIndex() - row));
									// 放置特店代號
									if(cellContent.equals(SrmCaseTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE.getValue())){
										cellContent = IAtomsConstants.PARAMTER_ITEM_CODE_MERCHANT_CODE;
									// 放置mid2
									} else if(cellContent.equals(SrmCaseTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE_OTHER.getValue())) {
										cellContent = IAtomsConstants.PARAMTER_ITEM_CODE_MERCHANT_CODE_OTHER;
										// 放置DTID
									}else if(cellContent.equals(SrmCaseTransactionParameterDTO.ATTRIBUTE.DTID.getValue())){
										cellContent = IAtomsConstants.PARAMTER_ITEM_CODE_DTID;
										// 放置tid
									} else if(cellContent.equals(SrmCaseTransactionParameterDTO.ATTRIBUTE.TID.getValue())){
										cellContent = IAtomsConstants.PARAMTER_ITEM_CODE_TID;
									}
									//如果是 固定列，且不等於交易類別
									if(!SrmCaseTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue().equals(cellContent)
											 && !IAtomsConstants.PARAMTER_ITEM_CODE_DTID.equals(cellContent)) {
										//不屬於支持的交易參數
										if(!editFilds.contains(cellContent)) {
											isTest = true;
										}
									}
									if(value != null) {
										cellValue = value.toString();
									} else {
										cellValue = IAtomsConstants.MARK_EMPTY_STRING;
									}
									isFiled = true;
									break;
								}
							}
							if(!isFiled) {
								//item_value轉換成的map
								if(srmCaseTransactionParameterDTOList.get(sourceCell.getRowIndex() - row).getSrmCaseTransactionParametermap().get(cellContent)!=null){
									cellValue = srmCaseTransactionParameterDTOList.get(sourceCell.getRowIndex() - row).getSrmCaseTransactionParametermap().get(cellContent);
								} else {
									if(sourceCell.getColumnIndex() == 0) {
										cellValue = IAtomsConstants.COLUMN_V;
									} else {
										cellValue = IAtomsConstants.MARK_EMPTY_STRING;
									} 
								}
								//不給第一列設置背景色
								if(sourceCell.getColumnIndex() != 0) {
									//不是固定列，不支持 交易參數
									if(!editFilds.contains(cellContent)) {
										isTest = true;
									}
								}
							}
							targetCell.setCellValue(cellValue);
						}
					}
				}
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				targetCell.setCellValue(sourceCell.getBooleanCellValue());
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				targetCell.setCellFormula(sourceCell.getCellFormula());
				break;
			default:
			break;
		}
		//处理单元格样式
		if(styleMap != null){
			if (targetWork == sourceWork) {
				targetCell.setCellStyle(sourceCell.getCellStyle());
			} else {
				String stHashCode = "" + sourceCell.getCellStyle().hashCode();
				if (isTest) {
					stHashCode += "_Black";
				}
				HSSFCellStyle targetCellStyle = (HSSFCellStyle) styleMap
						.get(stHashCode);
				if (targetCellStyle == null) {
					targetCellStyle = targetWork.createCellStyle();
					targetCellStyle.cloneStyleFrom(sourceCell.getCellStyle());
					if (isTest) {
						//設置背景色
						targetCellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());  
						targetCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
						targetCellStyle.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
						targetCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
						targetCellStyle.setBorderBottom(CellStyle.BORDER_THIN); //下邊框
						targetCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex()); //下邊框顏色
					}
					styleMap.put(stHashCode, targetCellStyle);
				}
				targetCell.setCellStyle(targetCellStyle);
			}
		}
	}
	/**
	 * Purpose:根据属性名获取属性值 
	 * @author HermanWang
	 * @param fieldName
	 * @param o
	 * @return Object
	 */
	private Object getFieldValueByName(String fieldName, Object o) {  
		try {    
			String firstLetter = fieldName.substring(0, 1).toUpperCase();    
			String getter = "get" + firstLetter + fieldName.substring(1);    
			Method method = o.getClass().getMethod(getter, new Class[] {});    
			Object value = method.invoke(o, new Object[] {});    
			return value;    
		} catch (Exception e) {    
			return null;    
		}    
	}   
	     
	   /** 
	    * 获取属性名数组 
	    * */  
	   private String[] getFiledName(Object o){  
	    Field[] fields=o.getClass().getDeclaredFields();  
	        String[] fieldNames=new String[fields.length];  
	    for(int i=0;i<fields.length;i++){  
	        System.out.println(fields[i].getType());  
	        fieldNames[i]=fields[i].getName();  
	    }  
	    return fieldNames;  
	   } 
	/**
	 * 功能：拷贝comment
	 * @param targetCell
	 * @param sourceCell
	 * @param targetPatriarch
	 */
	public static void copyComment(HSSFCell targetCell,HSSFCell sourceCell,HSSFPatriarch targetPatriarch)throws Exception{
		if(targetCell == null || sourceCell == null || targetPatriarch == null){
			throw new IllegalArgumentException("调用PoiUtil.copyCommentr()方法时，targetCell、sourceCell、targetPatriarch都不能为空，故抛出该异常！");
		}
		
		//处理单元格注释
		HSSFComment comment = sourceCell.getCellComment();
		if(comment != null){
			HSSFComment newComment = targetPatriarch.createComment(new HSSFClientAnchor());
			newComment.setAuthor(comment.getAuthor());
			newComment.setColumn(comment.getColumn());
			newComment.setFillColor(comment.getFillColor());
			newComment.setHorizontalAlignment(comment.getHorizontalAlignment());
			newComment.setLineStyle(comment.getLineStyle());
			newComment.setLineStyleColor(comment.getLineStyleColor());
			newComment.setLineWidth(comment.getLineWidth());
			newComment.setMarginBottom(comment.getMarginBottom());
			newComment.setMarginLeft(comment.getMarginLeft());
			newComment.setMarginTop(comment.getMarginTop());
			newComment.setMarginRight(comment.getMarginRight());
			newComment.setNoFill(comment.isNoFill());
			newComment.setRow(comment.getRow());
			//newComment.setShapeType(comment.getShapeType());
			newComment.setString(comment.getString());
			newComment.setVerticalAlignment(comment.getVerticalAlignment());
			newComment.setVisible(comment.isVisible());
			targetCell.setCellComment(newComment);
		}
	}
	
	/**
	 * 功能：复制原有sheet的合并单元格到新创建的sheet
	 * 
	 * @param sheetCreat
	 * @param sourceSheet
	 */
	public static void mergerRegion1(HSSFSheet targetSheet, HSSFSheet sourceSheet, int index)throws Exception {
		if(targetSheet == null || sourceSheet == null){
			throw new IllegalArgumentException("调用PoiUtil.mergerRegion()方法时，targetSheet或者sourceSheet不能为空，故抛出该异常！");
		}
		
		for (int i = 0; i < sourceSheet.getNumMergedRegions(); i++) {
			CellRangeAddress oldRange = sourceSheet.getMergedRegion(i);
			for(int j=0; j<index; j++){
				CellRangeAddress newRange = new CellRangeAddress(
						oldRange.getFirstRow()+j*(sourceSheet.getLastRowNum()+1), oldRange.getLastRow()+j*(sourceSheet.getLastRowNum()+1),
						oldRange.getFirstColumn(), oldRange.getLastColumn());
				targetSheet.addMergedRegion(newRange);
			}
			
		}
	}
	/**
	 * 功能：重新定义HSSFColor.YELLOW的色值
	 * 
	 * @param workbook
	 * @return
	 */
	public static HSSFColor setMForeColor(HSSFWorkbook workbook) {
		HSSFPalette palette = workbook.getCustomPalette();
		HSSFColor hssfColor = null;
		 byte[] rgb = { (byte) 221, (byte) 241, (byte) 255 };
		 try {
			 hssfColor = palette.findColor(rgb[0], rgb[1], rgb[2]);
			 if (hssfColor == null) {
				 palette.setColorAtIndex(HSSFColor.YELLOW.index, rgb[0], rgb[1],
						 rgb[2]);
				 hssfColor = palette.getColor(HSSFColor.YELLOW.index);
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return hssfColor;
	 }
	/**
	 * 功能：重新定义HSSFColor.PINK的色值
	 * 
	 * @param workbook
	 * @return
	 */
	public static HSSFColor setMBorderColor(HSSFWorkbook workbook) {
		HSSFPalette palette = workbook.getCustomPalette();
		HSSFColor hssfColor = null;
		byte[] rgb = { (byte) 0, (byte) 128, (byte) 192 };
		try {
			hssfColor = palette.findColor(rgb[0], rgb[1], rgb[2]);
			if (hssfColor == null) {
				palette.setColorAtIndex(HSSFColor.PINK.index, rgb[0], rgb[1],
						rgb[2]);
				hssfColor = palette.getColor(HSSFColor.PINK.index);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hssfColor;
	}
	public static void main(String[] args, Map map) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:列印創建excel
	 * @author HermanWang
	 * @param templatesName：臨時文件名稱
	 * @param tempPath：path路徑
	 * @param srmCaseHandleInfoDTOList：正在處理的案件的行
	 * @param editFildsMap：每種交易類別支持的交易參數
	 * @param x
	 * @return void
	 */
	public void createExcel(String templatesName, String tempPath, 
			List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOList, Map<String,List<String>> editFildsMap, int x){
		try {
			POIUtils pOIUtils = new POIUtils();
			//原模板
			FileInputStream sourceFis = new FileInputStream(tempPath + templatesName);
			HSSFWorkbook sourceWork = new HSSFWorkbook(sourceFis);
			HSSFSheet fromSheet = null;
			HSSFWorkbook wbCreat = new HSSFWorkbook();
			HSSFSheet toSheet = null;
			//獲取fromSheet
			fromSheet = sourceWork.getSheetAt(0);
			//獲取toSheet
			toSheet = wbCreat.createSheet(fromSheet.getSheetName());
			
			HSSFPrintSetup printSetup = toSheet.getPrintSetup();
			printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); // 纸张
			//toSheet.setMargin(HSSFSheet.TopMargin,( double ) 1.5 ); // 上边距
			//toSheet.setMargin(HSSFSheet.BottomMargin,( double ) 1.5 ); // 下边距
			//toSheet.setMargin(HSSFSheet.LeftMargin,( double ) 0.9 ); // 左边距
			//toSheet.setMargin(HSSFSheet.RightMargin,( double ) 0. ); // 右边距
			
			toSheet.setMargin(HSSFSheet.TopMargin,fromSheet.getMargin(HSSFSheet.TopMargin));// 页边距（上）    
			toSheet.setMargin(HSSFSheet.BottomMargin,fromSheet.getMargin(HSSFSheet.BottomMargin));// 页边距（下）    
			toSheet.setMargin(HSSFSheet.LeftMargin,fromSheet.getMargin(HSSFSheet.LeftMargin) );// 页边距（左）    
			toSheet.setMargin(HSSFSheet.RightMargin,fromSheet.getMargin(HSSFSheet.RightMargin));// 页边距（右
			
			toSheet.setMargin(HSSFSheet.HeaderMargin, fromSheet.getMargin(HSSFSheet.HeaderMargin));//页眉  
			toSheet.setMargin(HSSFSheet.FooterMargin, fromSheet.getMargin(HSSFSheet.FooterMargin));//页脚 
			
			//copySheet
			pOIUtils.copySheet(toSheet, fromSheet, wbCreat, sourceWork, srmCaseHandleInfoDTOList, editFildsMap);
			FileOutputStream fileOut = new FileOutputStream(tempPath + x+CaseManagerFormDTO.CASE_EXPORT_TEMPLATE_NAME_FOR_EN);
			wbCreat.write(fileOut);
			fileOut.close();
			sourceFis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** 
	 * Purpose:匯出案件數據時使用
	 * @author CarrieDuan
	 * @param path
	 * @param values
	 * @param index
	 * @return void
	 */
	public static void createExcel (String path, List<String> values, List<Integer> index, String caseCategory, Integer startIndex) {
		try {
			FileInputStream sourceFis = new FileInputStream(path);
			HSSFWorkbook sourceWork = new HSSFWorkbook(sourceFis);
			int sheetNum = sourceWork.getNumberOfSheets();
			HSSFSheet sheet = null;
			HSSFCell cell = null;
			HSSFCellStyle cellStyle = null;
			//在匯出舊資料時，文檔只有案件信息sheet，交易參數sheet需要自己創建
			if (sheetNum <= 1 && StringUtils.hasText(caseCategory)) {
				sheet = sourceWork.createSheet("交易參數資料");
			} else {
				sheet = sourceWork.getSheetAt(1);
				
			} 
			//得到Excel工作表的行
			HSSFRow row = sheet.getRow(0);
			HSSFDataFormat format = sourceWork.createDataFormat();
			//匯出舊資料時，交易參數sheet全部為空，需要手動設置樣式
			if (row == null) {
				row = sheet.createRow(0);
				cell = row.createCell(0);
				cellStyle = sourceWork.createCellStyle();
				//設備列名背景色
				cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				//設備表頭字體居中-左右居中
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				//設備表頭字體居中-垂直居中
				cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				//設備表頭字體、大小
				HSSFFont font = sourceWork.createFont(); 
				font.setFontName("Pictonic"); 
				font.setFontHeightInPoints((short) 12);//设置字体大小
				cellStyle.setFont(font);//选择需要用到的字体格式
				//設置邊框
				cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
				cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
				cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
				cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
				cell = row.createCell(0);
				if (IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(caseCategory)) {
					cell.setCellValue("案件序號");
				} else {
					cell.setCellValue("DTID");
				}
				cellStyle.setDataFormat(format.getFormat("@"));
				cell.setCellStyle(cellStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(cellStyle);
				sheet.setColumnWidth(0, 15 * 256);
				cell = row.createCell(1);
				cell.setCellValue("交易項目");
				cellStyle.setDataFormat(format.getFormat("@"));
				cell.setCellStyle(cellStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				sheet.setColumnWidth(1, 15 * 256);
				cell.setCellStyle(cellStyle);
			} else {
				cell = row.createCell(startIndex);
				cellStyle = sourceWork.createCellStyle();
				cellStyle.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				//設備表頭字體居中-左右居中
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				//設備表頭字體居中-垂直居中
				cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				//設備表頭字體、大小
				HSSFFont font = sourceWork.createFont(); 
				font.setFontName("PMingLiU"); 
				font.setFontHeightInPoints((short) 11);//设置字体大小
				cellStyle.setFont(font);//选择需要用到的字体格式
				//設置邊框
				cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
				cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
				cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
				cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
				cell.setCellStyle(cellStyle);
			}
			int i = startIndex;
			for (int m = 0; m<500; m++) {
				i = startIndex;
				if (m != 0) {
					row = sheet.getRow(m);
					if (row == null){
						row = sheet.createRow(m);
						cell = row.createCell(i);
						cellStyle = cell.getCellStyle();
					} else {
						cell = row.createCell(i);
						cellStyle = cell.getCellStyle();
					}
				}
				for (String value : values) {
				if (value.length() < 5) {
						sheet.setColumnWidth(i, 15 * 256);
				} else if (value.length() < 7) {
						sheet.setColumnWidth(i, 25 * 256);
					} else {
						sheet.setColumnWidth(i, 35 * 256);
					}
					cell = row.getCell((short) i);
					if (cell == null) {
						cell = row.createCell(i);
					}
					if (index.contains(i)) {
						cellStyle.setDataFormat(format.getFormat("@"));
						cell.setCellStyle(cellStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					if (m == 0) {
						row.setHeightInPoints(25);
						cell.setCellValue(value);
						cell.setCellStyle(cellStyle);
					}
					i++;
				}
			}
			if (sheetNum <= 1 && StringUtils.hasText(caseCategory)) {
				
			} else {
				/*i = startIndex;
				row = sheet.getRow(0);
				for (String value : values) {
					cell = row.getCell(i);
					cellStyle = cell.getCellStyle();
					cellStyle.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
					cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cell.setCellStyle(cellStyle);
					i++;
				}*/
			}
			FileOutputStream fileOut = new FileOutputStream(path);
			sourceWork.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:創建excel
	 * @author HermanWang
	 * @param exportField：可變列的數組
	 * @param srmCaseHandleInfoDTOs：查詢結果dtolist
	 * @throws Exception
	 * @return void
	 */
	 public static void createExcel(String[] exportField, List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs, String pathString) throws Exception{
			//构造一个工作簿
			HSSFWorkbook wb = new HSSFWorkbook();
			//创建一个工作表
			HSSFSheet sheet = wb.createSheet(i18NUtil.getName(CaseManagerFormDTO.CASE_TEMPLATE));
			CreationHelper helper = wb.getCreationHelper();  
        	Drawing drawing = sheet.createDrawingPatriarch();  
        	
        	double standardHeight = 26;
        	double standardWidth = 26;
        	InputStream isInfo = new FileInputStream(pathString + "assets/jquery-easyui-1.4.3/themes/icons/info.png");  
        	byte[] bytesInfo = IOUtils.toByteArray(isInfo);
        	int pictureIdxInfo = wb.addPicture(bytesInfo, Workbook.PICTURE_TYPE_PNG);
        	
        	InputStream isWarnning = new FileInputStream(pathString + "assets/jquery-easyui-1.4.3/themes/icons/warnning.png");  
        	byte[] bytesWarnning = IOUtils.toByteArray(isWarnning);
        	int pictureIdxWarnning = wb.addPicture(bytesWarnning, Workbook.PICTURE_TYPE_PNG);
			//獲取樣式
			HSSFCellStyle[] cellStyles = getCellStyles(wb);
			HSSFRow row = null;
			BufferedImage bufferImg = null;  
			//创建行,索引从0开始
			for(int i = 0; i <= srmCaseHandleInfoDTOs.size(); i++) {
				row = sheet.createRow(i);
				if(i == 0) {
					row.setHeightInPoints(40);
				} else {
					String maxLengthValue = "";
					for(int j = 0; j < exportField.length; j++) {
						String filedValue = getFiledValueByName(exportField[j], srmCaseHandleInfoDTOs.get(i-1));
						if(filedValue.length() > maxLengthValue.length()) {
							maxLengthValue = filedValue;
						}
					}
					float hieght=getExcelCellAutoHeight(maxLengthValue, 8f);     
	            	//根据字符串的长度设置高度
					row.setHeight((short)(hieght*20));
				}
				//创建单元格,索引从0开始
				for(int j = 0; j <= exportField.length; j++) {
					HSSFCell cell = row.createCell(j);
					//设置单元格的值
					if(i == 0) {
						if(j == 0) {
							cell.setCellValue("序號");
						} else {
							cell.setCellValue(i18NUtil.getName(exportField[j-1]));
						}
						//水平居中
						cell.setCellStyle(cellStyles[2]);
					} else {
						if(j == 0) {
							cell.setCellValue(i);
							//存修
							if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTOs.get(i-1).getRushRepair())) {
								//水平居中(不加粗) 黃色字體
								cell.setCellStyle(cellStyles[5]);
							} else {
								//水平居中(不加粗) 黑色字體
								cell.setCellStyle(cellStyles[1]);
							}
						} else {
							String filedValue = getFiledValueByName(exportField[j-1], srmCaseHandleInfoDTOs.get(i-1));
							//催修，變黃。
							if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTOs.get(i-1).getRushRepair())) {
								if(StringUtils.hasText(filedValue)) {
									if(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_CONDITION.getValue().equals(exportField[j-1])
											|| SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_CONDITION.getValue().equals(exportField[j-1])
											|| SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_CONDITION.getValue().equals(exportField[j-1])) {
										if(IAtomsConstants.CASE_PARAM_OVER_HOUR.equals(filedValue.substring(0, filedValue.length()-17))) {
											cell.setCellValue(IAtomsConstants.MARK_EXCLAMATORY);
											//cell.setCellValue(IAtomsConstants.MARK_WARNING);
/*//											ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream(); 
//											bufferImg = ImageIO.read(new File(pathString + "assets/jquery-easyui-1.4.3/themes/icons/info.png"));
//											ImageIO.write(bufferImg, "jpg", byteArrayOut); 
//											
//											//画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
//											HSSFPatriarch patriarch = sheet.createDrawingPatriarch();     
//											//anchor主要用于设置图片的属性  
//											int heightInPoints = (new Double(row.getHeight())).intValue()/2;
//											HSSFClientAnchor anchor = new HSSFClientAnchor(450, 200, 650, 250,(short) cell.getColumnIndex(), cell.getRowIndex(), (short) cell.getColumnIndex(), cell.getRowIndex());     
//											//HSSFClientAnchor anchor = new HSSFClientAnchor();     
//											anchor.setAnchorType(3);     
//											//插入图片    
//											patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
											ClientAnchor anchor = helper.createClientAnchor();  
					            			anchor.setCol1(j);
					            			anchor.setRow1(i); 
					            			anchor.setDx1(500);
					            			anchor.setDy1(110);
					            			double cellWidth = sheet.getColumnWidthInPixels(cell.getColumnIndex());

					                    	double cellHeight = row.getHeightInPoints()/72*96;
					                    	
					            			double a = standardWidth / cellWidth;
					            			double b = standardHeight / cellHeight;

					            			Picture pict = drawing.createPicture(anchor, pictureIdxInfo);  
					                    	pict.resize(a,b); */
											cell.setCellStyle(cellStyles[9]);
										} else if(IAtomsConstants.CASE_PARAM_OVER_WARNNING.equals(filedValue.substring(0, filedValue.length()-17))) {
											cell.setCellValue(IAtomsConstants.MARK_INTERROGATION);
											//cell.setCellValue(IAtomsConstants.MARK_WARNING);
//											ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
//											bufferImg = ImageIO.read(new File(pathString + "assets/jquery-easyui-1.4.3/themes/icons/warnning.png"));
//											ImageIO.write(bufferImg, "jpg", byteArrayOut); 
//											
//											 //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
//								            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();     
//								            //anchor主要用于设置图片的属性  
//								            int heightInPoints = (new Double(row.getHeight())).intValue()/2;
//								            HSSFClientAnchor anchor = new HSSFClientAnchor(450, 200, 650, 250,(short) cell.getColumnIndex(), cell.getRowIndex(), (short) cell.getColumnIndex(), cell.getRowIndex());     
//								            anchor.setAnchorType(3);     
//								            //插入图片    
//								            patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
											/*ClientAnchor anchor = helper.createClientAnchor();  
					            			anchor.setCol1(j);
					            			anchor.setRow1(i); 
					            			anchor.setDx1(500);
					            			anchor.setDy1(110);
					            			double cellWidth = sheet.getColumnWidthInPixels(cell.getColumnIndex());

					                    	double cellHeight = row.getHeightInPoints()/72*96;
					                    	
					            			double a = standardWidth / cellWidth;
					            			double b = standardHeight / cellHeight;

					            			Picture pict = drawing.createPicture(anchor, pictureIdxWarnning);  
					                    	pict.resize(a,b); */
											cell.setCellStyle(cellStyles[10]);
										} else {
											//默認設置水平居右
											cell.setCellStyle(cellStyles[6]);
										}
									} else {
										if(SrmCaseHandleInfoDTO.ATTRIBUTE.ENABLE_DATE.getValue().equals(exportField[j-1])) {
											if(filedValue.length() != 17) {
												cell.setCellValue(filedValue.substring(0, 10));
											} else {
												cell.setCellValue(filedValue.substring(0, filedValue.length()-17));
											}
										} else {
											// Task #2543 ECR連線欄位使用[V]取代文字，匯出一併調整
											if(SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION.getValue().equals(exportField[j-1])){
												cell.setCellValue(IAtomsConstants.CASE_PARAM_HAVE_ECR_LINE.equals(filedValue.substring(0, filedValue.length()-17)) ? IAtomsConstants.COLUMN_V : IAtomsConstants.MARK_EMPTY_STRING);
											// Task #3205 是否執行過延期
											} else if(SrmCaseHandleInfoDTO.ATTRIBUTE.HAS_DELAY.getValue().equals(exportField[j-1])) {
												cell.setCellValue(IAtomsConstants.YES.equals(filedValue.substring(0, filedValue.length()-17)) ? i18NUtil.getName(IAtomsConstants.CASE_PARAM_HAVE_DELAY) : IAtomsConstants.NO.equals(filedValue.substring(0, filedValue.length()-17)) ? i18NUtil.getName(IAtomsConstants.CASE_PARAM_NOT_HAVE_DELAY) : IAtomsConstants.MARK_EMPTY_STRING);
											} else {
												cell.setCellValue(filedValue.substring(0, filedValue.length()-17));
											}
										//	cell.setCellValue(filedValue.substring(0, filedValue.length()-17));
										}
										if(filedValue.substring(filedValue.length()-17).equals(IAtomsConstants.IS_DATE_OR_TIMESTAMP)) {
											//如果是日期格式 設置水平居中
											cell.setCellStyle(cellStyles[5]);
										} else if (filedValue.substring(filedValue.length()-17).equals(IAtomsConstants.D_NUMBER_BIGDECIMAL)){
											//默認設置水平居右
											cell.setCellStyle(cellStyles[6]);
										} else {
											//默認設置水平居左
											cell.setCellStyle(cellStyles[4]);
										}
									}
								} else {
									cell.setCellValue(filedValue);
									//默認設置水平居左
									cell.setCellStyle(cellStyles[4]);
								}
							} else {
								if(StringUtils.hasText(filedValue)) {
									
									if(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_CONDITION.getValue().equals(exportField[j-1])
											|| SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_CONDITION.getValue().equals(exportField[j-1])
											|| SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_CONDITION.getValue().equals(exportField[j-1])) {
										if(IAtomsConstants.CASE_PARAM_OVER_HOUR.equals(filedValue.substring(0, filedValue.length()-17))) {
											cell.setCellValue(IAtomsConstants.MARK_EXCLAMATORY);
											//ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
//											bufferImg = ImageIO.read(new File(pathString + "assets/jquery-easyui-1.4.3/themes/icons/info.png"));
//											ImageIO.write(bufferImg, "jpg", byteArrayOut); 
//											
//											//画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
//											HSSFPatriarch patriarch = sheet.createDrawingPatriarch();     
//											//anchor主要用于设置图片的属性
//											int heightInPoints = (new Double(row.getHeight())).intValue()/2;
//											HSSFClientAnchor anchor = new HSSFClientAnchor(450, 200, 650, 250,(short) cell.getColumnIndex(), cell.getRowIndex(), (short) cell.getColumnIndex(), cell.getRowIndex());     
//											anchor.setAnchorType(3);     
//											//插入图片    
//											patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
											/*ClientAnchor anchor = helper.createClientAnchor();  
					            			anchor.setCol1(j);
					            			anchor.setRow1(i); 
					            			anchor.setDx1(500);
					            			anchor.setDy1(110);
					            			double cellWidth = sheet.getColumnWidthInPixels(cell.getColumnIndex());

					                    	double cellHeight = row.getHeightInPoints()/72*96;
					                    	
					            			double a = standardWidth / cellWidth;
					            			double b = standardHeight / cellHeight;

					            			Picture pict = drawing.createPicture(anchor, pictureIdxInfo);  
					                    	pict.resize(a,b); */
											cell.setCellStyle(cellStyles[7]);
										} else if(IAtomsConstants.CASE_PARAM_OVER_WARNNING.equals(filedValue.substring(0, filedValue.length()-17))) {
											cell.setCellValue(IAtomsConstants.MARK_INTERROGATION);
											//											ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
//											bufferImg = ImageIO.read(new File(pathString + "assets/jquery-easyui-1.4.3/themes/icons/warnning.png"));
//											ImageIO.write(bufferImg, "jpg", byteArrayOut); 
//											
//											 //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
//								            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();     
//								            //anchor主要用于设置图片的属性  
//								            int heightInPoints = (new Double(row.getHeight())).intValue()/2;
//											HSSFClientAnchor anchor = new HSSFClientAnchor(450, 200, 650, 250,(short) cell.getColumnIndex(), cell.getRowIndex(), (short) cell.getColumnIndex(), cell.getRowIndex());     
//											anchor.setAnchorType(3);     
//											//插入图片    
//											patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
											
											/*ClientAnchor anchor = helper.createClientAnchor();  
					            			anchor.setCol1(j);
					            			anchor.setRow1(i);
					            			anchor.setDx1(500);
					            			anchor.setDy1(110);
					            			double cellWidth = sheet.getColumnWidthInPixels(cell.getColumnIndex());

					                    	double cellHeight = row.getHeightInPoints()/72*96;
					                    	
					            			double a = standardWidth / cellWidth;
					            			double b = standardHeight / cellHeight;

					            			Picture pict = drawing.createPicture(anchor, pictureIdxWarnning);  
					                    	pict.resize(a,b); */
											
											cell.setCellStyle(cellStyles[8]);
										} else {
											//如果是日期格式 設置水平居中
											cell.setCellStyle(cellStyles[1]);
										}
									} else {
										if(SrmCaseHandleInfoDTO.ATTRIBUTE.ENABLE_DATE.getValue().equals(exportField[j-1])) {
											if(filedValue.length() != 17) {
												cell.setCellValue(filedValue.substring(0, 10));
											} else {
												cell.setCellValue(filedValue.substring(0, filedValue.length()-17));
											}
										} else {
											// Task #2543 ECR連線欄位使用[V]取代文字，匯出一併調整
											if(SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION.getValue().equals(exportField[j-1])){
												cell.setCellValue(IAtomsConstants.CASE_PARAM_HAVE_ECR_LINE.equals(filedValue.substring(0, filedValue.length()-17)) ? IAtomsConstants.COLUMN_V : IAtomsConstants.MARK_EMPTY_STRING);
											// Task #3205 是否執行過延期
											} else if(SrmCaseHandleInfoDTO.ATTRIBUTE.HAS_DELAY.getValue().equals(exportField[j-1])) {
												cell.setCellValue(IAtomsConstants.YES.equals(filedValue.substring(0, filedValue.length()-17)) ? i18NUtil.getName(IAtomsConstants.CASE_PARAM_HAVE_DELAY) : IAtomsConstants.NO.equals(filedValue.substring(0, filedValue.length()-17)) ? i18NUtil.getName(IAtomsConstants.CASE_PARAM_NOT_HAVE_DELAY) : IAtomsConstants.MARK_EMPTY_STRING);
											} else {
												cell.setCellValue(filedValue.substring(0, filedValue.length()-17));
											}
										//	cell.setCellValue(filedValue.substring(0, filedValue.length()-17));
										}
										if(filedValue.substring(filedValue.length()-17).equals(IAtomsConstants.IS_DATE_OR_TIMESTAMP)) {
											//如果是日期格式 設置水平居中
											cell.setCellStyle(cellStyles[1]);
										} else if (filedValue.substring(filedValue.length()-17).equals(IAtomsConstants.D_NUMBER_BIGDECIMAL)){
											//默認設置水平居右
											cell.setCellStyle(cellStyles[3]);
										} else {
											//默認設置水平居左
											cell.setCellStyle(cellStyles[0]);
										}
									}
								} else {
									cell.setCellValue(filedValue);
									//默認設置水平居左
									cell.setCellStyle(cellStyles[0]);
								}
							}
						}
					}
					if(j == 0) {
						sheet.setColumnWidth(j, 15*256);
					} else {
						sheet.setColumnWidth(j, 20*256);
					}
				}
			}
			//保存到文件中
			FileOutputStream fos = null;
			try {
				String templatesName = CaseManagerFormDTO.CASE_EXPORT_TEMPLATE_NAME_FOR_EN;
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				String tempPath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH) + File.separator 
						 + yearMonthDay;
				File filePath = new File(tempPath);
				if (!filePath.exists() || !filePath.isDirectory()) {
					filePath.mkdirs();
				}
				fos = new FileOutputStream(filePath + File.separator + templatesName);
				wb.write(fos);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	 public static void updateExcel(String tempPath, String templatesName, String pathString) throws Exception{
		 FileOutputStream fileOut = null;     
	        //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray    
	        try { 
	        	InputStream isWarnning = new FileInputStream(pathString + "assets/jquery-easyui-1.4.3/themes/icons/warnning.png");  
	        	byte[] bytesWarnning = IOUtils.toByteArray(isWarnning);  
	        	
	        	InputStream isInfo = new FileInputStream(pathString + "assets/jquery-easyui-1.4.3/themes/icons/info.png");
	        	byte[] bytesInfo = IOUtils.toByteArray(isInfo); 
	        	
	        	FileInputStream excelFileInputStream = new FileInputStream(tempPath + File.separator + templatesName);
	        	HSSFWorkbook workbook = new HSSFWorkbook(excelFileInputStream);
	        	int pictureIdxWarnning = workbook.addPicture(bytesWarnning, Workbook.PICTURE_TYPE_PNG);  
	        	int pictureIdxInfo = workbook.addPicture(bytesInfo, Workbook.PICTURE_TYPE_PNG);  
	        	CreationHelper helper = workbook.getCreationHelper();  
	        	HSSFSheet sheet = workbook.getSheetAt(0);
	        	Drawing drawing = sheet.createDrawingPatriarch();  
	        	for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
	        		HSSFRow  row1 = sheet.getRow(j);
	        		row1.setHeight((short)(row1.getHeight() + 10));
	            	for (int i = 0; i < row1.getPhysicalNumberOfCells(); i++) {
	            		HSSFCell cell = row1.getCell(i);
	            		if(Cell.CELL_TYPE_STRING ==cell.getCellType()) {
	            			if(StringUtils.hasText(cell.getStringCellValue())) {
	            				if("OVER_WARNNING".equals(cell.getStringCellValue())) {
	            					cell.setCellValue("");
	            					ClientAnchor anchor = helper.createClientAnchor();  
	            					anchor.setCol1(i);
	            					anchor.setRow1(j);  
	            					anchor.setDx1(500);
	            					anchor.setDy1(5);
	            					Picture pict = drawing.createPicture(anchor, pictureIdxWarnning);  
	            					pict.resize(); 
	            				} else if("OVER_HOUR".equals(cell.getStringCellValue())) {
	            					cell.setCellValue("");
	            					ClientAnchor anchor = helper.createClientAnchor();  
	            					anchor.setCol1(i);
	            					anchor.setRow1(j);  
	            					anchor.setDx1(500);
	            					anchor.setDy1(5);
	            					Picture pict = drawing.createPicture(anchor, pictureIdxInfo);  
	            					pict.resize(); 
	            				}
	            			}
	            		}
	            	}
	        	}
	            fileOut = new FileOutputStream(tempPath + File.separator + templatesName);
	            // 写入excel文件     
	            workbook.write(fileOut);
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }finally{  
	            if(fileOut != null){  
	                 try {  
	                    fileOut.close();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
	 }
	/* public static void main(String[] args) {
		//main111("/home/cybersoft/iAtoms/IATOMS/TEMP/20170904", "caseExportTemplate.xls", null);
		main111("/home/cybersoft/桌面", "案件資訊.xls", null);
	}*/
	 public static float getregex(String charStr) {
	        
	        if(charStr==" ")
	        {
	            return 0.5f;
	        }
	        // 判断是否为字母或字符
	        if (Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
	            return 0.5f;
	        }
	        // 判断是否为全角

	        if (Pattern.compile("[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
	            return 1.00f;
	        }
	        //全角符号 及中文
	        if (Pattern.compile("[^x00-xff]").matcher(charStr).matches()) {
	            return 1.00f;
	        }
	        return 0.5f;

	    }
	 public static float getExcelCellAutoHeight(String str, float fontCountInline) {
	        float defaultRowHeight = 13.00f;//每一行的高度指定
	        float defaultCount = 0.00f;
	        for (int i = 0; i < str.length(); i++) {
	            float ff = getregex(str.substring(i, i + 1));
	            defaultCount = defaultCount + ff;
	        }
	        return ((int) (defaultCount / fontCountInline) + 1) * defaultRowHeight;//计算
	    }

	/* public static void main111(String tempPath, String templatesName, String pathString) {  
         FileOutputStream fileOut = null;     
        //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray    
        try { 
        	InputStream is = new FileInputStream("E:\\ico\\warnning.png");  
        	byte[] bytes = IOUtils.toByteArray(is);  
        	FileInputStream excelFileInputStream = new FileInputStream(tempPath+ "/" +templatesName);
        	HSSFWorkbook workbook = new HSSFWorkbook(excelFileInputStream);
        	  
        	int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);  
        	  
        	CreationHelper helper = workbook.getCreationHelper();  
        	HSSFSheet sheet = workbook.getSheetAt(0);
        	Drawing drawing = sheet.createDrawingPatriarch();  
        	
        	double standardHeight = 16;
        	double standardWidth = 16;
        	
        	
        	
        	for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
        		HSSFRow  row1 = sheet.getRow(j);
        		HSSFCell cell = row1.getCell(19);
        		float hieght=getExcelCellAutoHeight(cell.getStringCellValue(), 8f);     
            	//根据字符串的长度设置高度
        		row1.setHeightInPoints(hieght); 
        	}

        	
        	for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
        		HSSFRow  row1 = sheet.getRow(j);
            	for (int i = 0; i < row1.getPhysicalNumberOfCells(); i++) {
            		if(i == 2) {
            			Cell cell = row1.getCell(i);
            			ClientAnchor anchor = helper.createClientAnchor();  
            			anchor.setCol1(i);
            			anchor.setRow1(j);  
            			//anchor.setDx1(500);
            			//anchor.setDy1(5);
            			//anchor.setAnchorType(3);
            			double cellWidth = sheet.getColumnWidthInPixels(cell.getColumnIndex());

                    	double cellHeight = row1.getHeightInPoints()/72*96;
                    	
            			double a = standardWidth / cellWidth;
            			double b = standardHeight / cellHeight;

            			Picture pict = drawing.createPicture(anchor, pictureIdx);  
                    	pict.resize(a,b); 
            		}
            	}
        	}
        	// 图片插入坐标  
        	anchor.setCol1(0);  
        	anchor.setRow1(1);  
        	// 插入图片  
        	Picture pict = drawing.createPicture(anchor, pictureIdx);  
        	pict.resize();  
        	
        	FileInputStream excelFileInputStream = new FileInputStream("/home/cybersoft/桌面/案件資訊.xls");
        	HSSFWorkbook wb = new HSSFWorkbook(excelFileInputStream);
            HSSFSheet sheet1 = wb.getSheetAt(0);
        	//画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
			HSSFPatriarch patriarch = sheet1.createDrawingPatriarch(); 
            for (int j = 0; j < sheet1.getPhysicalNumberOfRows(); j++) {
            	HSSFRow  row1 = sheet1.getRow(j);
            	for (int i = 0; i < row1.getPhysicalNumberOfCells(); i++) {
            		if(i == 3) {
            			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
        	            bufferImg = ImageIO.read(new File("/home/cybersoft/IAtoms_wang/trunk/cyber-iatoms-web/src/main/webapp/assets/jquery-easyui-1.4.3/themes/icons/warnning.png"));     
        	           
        	            ImageIO.write(bufferImg, "jpg", byteArrayOut); 
            			//anchor主要用于设置图片的属性  
            			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,(short) i, j, (short) i, j); 
            			anchor.setAnchorType(3);     
            			//插入图片    
            			patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));   
            		}
            	}
			}
            fileOut = new FileOutputStream("/home/cybersoft/1111111111111111111111Excel.xls");     
            // 写入excel文件     
            workbook.write(fileOut);
             System.out.println("----Excle文件已生成------");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            if(fileOut != null){  
                 try {  
                    fileOut.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
	 
	 public static void main1(String[] args) {  
         FileOutputStream fileOut = null;     
         BufferedImage bufferImg = null;     
        //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray    
        try {  
        	FileInputStream excelFileInputStream = new FileInputStream("/home/cybersoft/桌面/案件資訊.xls");
        	HSSFWorkbook wb = new HSSFWorkbook(excelFileInputStream);
            HSSFSheet sheet1 = wb.getSheetAt(0);
        	//画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
			HSSFPatriarch patriarch = sheet1.createDrawingPatriarch(); 
            for (int j = 0; j < sheet1.getPhysicalNumberOfRows(); j++) {
            	HSSFRow  row1 = sheet1.getRow(j);
            	for (int i = 0; i < row1.getPhysicalNumberOfCells(); i++) {
            		if(i == 3) {
            			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
        	            bufferImg = ImageIO.read(new File("/home/cybersoft/IAtoms_wang/trunk/cyber-iatoms-web/src/main/webapp/assets/jquery-easyui-1.4.3/themes/icons/warnning.png"));     
        	           
        	            ImageIO.write(bufferImg, "jpg", byteArrayOut); 
            			//anchor主要用于设置图片的属性  
            			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,(short) i, j, (short) i, j); 
            			anchor.setAnchorType(3);     
            			//插入图片    
            			patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));   
            		}
            	}
			}
            fileOut = new FileOutputStream("/home/cybersoft/1111111111111111111111Excel.xls");     
            // 写入excel文件     
             wb.write(fileOut);
             System.out.println("----Excle文件已生成------");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            if(fileOut != null){  
                 try {  
                    fileOut.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  */
	 /**
	  * Purpose:獲取樣式list
	  * @author HermanWang
	  * @param wb：HSSFWorkbook
	  * @throws Exception
	  * @return HSSFCellStyle[]
	  */
	 public static HSSFCellStyle[] getCellStyles(HSSFWorkbook wb) throws Exception {
		HSSFCellStyle[] cellStyles = new HSSFCellStyle[11];
		try {
			HSSFFont font = wb.createFont();
			font.setCharSet(HSSFFont.DEFAULT_CHARSET);
			font.setFontName(IAtomsConstants.PICTONIC);
			
			HSSFFont fontAndBoldweight = wb.createFont();
			fontAndBoldweight.setCharSet(HSSFFont.DEFAULT_CHARSET);
			fontAndBoldweight.setFontName(IAtomsConstants.PICTONIC);
			//粗体显示
			fontAndBoldweight.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			fontAndBoldweight.setFontHeightInPoints((short)11);
			
			//水平居左樣式
			HSSFCellStyle cellStyleLeft = wb.createCellStyle();
			//自動換行
			cellStyleLeft.setWrapText(true);
			//垂直居中顯示
			cellStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//設置字體
			cellStyleLeft.setFont(font);
			//設置水平居左
			cellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cellStyleLeft.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
			cellStyleLeft.setBorderBottom(CellStyle.BORDER_THIN); //下邊框
			cellStyleLeft.setBorderLeft(CellStyle.BORDER_THIN); //左邊框
			cellStyleLeft.setBorderTop(CellStyle.BORDER_THIN); //上邊框
			cellStyleLeft.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
			cellStyleLeft.setBottomBorderColor(IndexedColors.BLACK.getIndex()); //下邊框顏色
			cellStyleLeft.setLeftBorderColor(IndexedColors.BLACK.getIndex()); //左邊框顏色
			cellStyleLeft.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上邊框顏色
			cellStyles[0] = cellStyleLeft;
			
			//水平居中樣式
			HSSFCellStyle cellStyleConter = wb.createCellStyle();
			//自動換行
			cellStyleConter.setWrapText(true);
			//垂直居中顯示
			cellStyleConter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//設置字體
			cellStyleConter.setFont(font);
			//設置水平居中
			cellStyleConter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleConter.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
			cellStyleConter.setBorderBottom(CellStyle.BORDER_THIN); //下邊框
			cellStyleConter.setBorderLeft(CellStyle.BORDER_THIN); //左邊框
			cellStyleConter.setBorderTop(CellStyle.BORDER_THIN); //上邊框
			cellStyleConter.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
			cellStyleConter.setBottomBorderColor(IndexedColors.BLACK.getIndex()); //下邊框顏色
			cellStyleConter.setLeftBorderColor(IndexedColors.BLACK.getIndex()); //左邊框顏色
			cellStyleConter.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上邊框顏色
			cellStyles[1] = cellStyleConter;
			
			//水平居中樣式
			HSSFCellStyle cellStyleConterAndBoldweight = wb.createCellStyle();
			//自動換行
			cellStyleConterAndBoldweight.setWrapText(true);
			//垂直居中顯示
			cellStyleConterAndBoldweight.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//設置字體
			cellStyleConterAndBoldweight.setFont(fontAndBoldweight);
			//設置水平居中
			cellStyleConterAndBoldweight.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleConterAndBoldweight.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
			cellStyleConterAndBoldweight.setBorderBottom(CellStyle.BORDER_THIN); //下邊框
			cellStyleConterAndBoldweight.setBorderLeft(CellStyle.BORDER_THIN); //左邊框
			cellStyleConterAndBoldweight.setBorderTop(CellStyle.BORDER_THIN); //上邊框
			cellStyleConterAndBoldweight.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
			cellStyleConterAndBoldweight.setBottomBorderColor(IndexedColors.BLACK.getIndex()); //下邊框顏色
			cellStyleConterAndBoldweight.setLeftBorderColor(IndexedColors.BLACK.getIndex()); //左邊框顏色
			cellStyleConterAndBoldweight.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上邊框顏色
			cellStyles[2] = cellStyleConterAndBoldweight;
			
			//水平居右樣式
			HSSFCellStyle cellStyleRight = wb.createCellStyle();
			//自動換行
			cellStyleRight.setWrapText(true);
			//垂直居中顯示
			cellStyleRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//設置字體
			cellStyleRight.setFont(font);
			//設置水平居中
			cellStyleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			cellStyleRight.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
			cellStyleRight.setBorderBottom(CellStyle.BORDER_THIN); //下邊框
			cellStyleRight.setBorderLeft(CellStyle.BORDER_THIN); //左邊框
			cellStyleRight.setBorderTop(CellStyle.BORDER_THIN); //上邊框
			cellStyleRight.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
			cellStyleRight.setBottomBorderColor(IndexedColors.BLACK.getIndex()); //下邊框顏色
			cellStyleRight.setLeftBorderColor(IndexedColors.BLACK.getIndex()); //左邊框顏色
			cellStyleRight.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上邊框顏色
			cellStyles[3] = cellStyleRight;
			
			//水平居左樣式
			HSSFCellStyle rushRepairCellStyleLeft = wb.createCellStyle();
			//自動換行
			rushRepairCellStyleLeft.setWrapText(true);
			//垂直居中顯示
			rushRepairCellStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//設置字體
			rushRepairCellStyleLeft.setFont(font);
			//設置水平居左
			rushRepairCellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			//背景色黃色
			rushRepairCellStyleLeft.setFillForegroundColor(IndexedColors.YELLOW.getIndex());  
			rushRepairCellStyleLeft.setFillPattern(CellStyle.SOLID_FOREGROUND);
			rushRepairCellStyleLeft.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
			rushRepairCellStyleLeft.setBorderBottom(CellStyle.BORDER_THIN); //下邊框
			rushRepairCellStyleLeft.setBorderLeft(CellStyle.BORDER_THIN); //左邊框
			rushRepairCellStyleLeft.setBorderTop(CellStyle.BORDER_THIN); //上邊框
			rushRepairCellStyleLeft.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
			rushRepairCellStyleLeft.setBottomBorderColor(IndexedColors.BLACK.getIndex()); //下邊框顏色
			rushRepairCellStyleLeft.setLeftBorderColor(IndexedColors.BLACK.getIndex()); //左邊框顏色
			rushRepairCellStyleLeft.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上邊框顏色
			cellStyles[4] = rushRepairCellStyleLeft;
			
			//水平居中樣式
			HSSFCellStyle rushRepairCellStyleConter = wb.createCellStyle();
			//自動換行
			rushRepairCellStyleConter.setWrapText(true);
			//垂直居中顯示
			rushRepairCellStyleConter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//設置字體
			rushRepairCellStyleConter.setFont(font);
			//設置水平居中
			rushRepairCellStyleConter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//背景色黃色
			rushRepairCellStyleConter.setFillForegroundColor(IndexedColors.YELLOW.getIndex());  
			rushRepairCellStyleConter.setFillPattern(CellStyle.SOLID_FOREGROUND);
			rushRepairCellStyleConter.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
			rushRepairCellStyleConter.setBorderBottom(CellStyle.BORDER_THIN); //下邊框
			rushRepairCellStyleConter.setBorderLeft(CellStyle.BORDER_THIN); //左邊框
			rushRepairCellStyleConter.setBorderTop(CellStyle.BORDER_THIN); //上邊框
			rushRepairCellStyleConter.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
			rushRepairCellStyleConter.setBottomBorderColor(IndexedColors.BLACK.getIndex()); //下邊框顏色
			rushRepairCellStyleConter.setLeftBorderColor(IndexedColors.BLACK.getIndex()); //左邊框顏色
			rushRepairCellStyleConter.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上邊框顏色
			cellStyles[5] = rushRepairCellStyleConter;
			
			//水平居右樣式
			HSSFCellStyle rushRepairCellStyleRight = wb.createCellStyle();
			//自動換行
			rushRepairCellStyleRight.setWrapText(true);
			//垂直居中顯示
			rushRepairCellStyleRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//設置字體
			rushRepairCellStyleRight.setFont(font);
			//設置水平居中
			rushRepairCellStyleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			//背景色黃色
			rushRepairCellStyleRight.setFillForegroundColor(IndexedColors.YELLOW.getIndex());  
			rushRepairCellStyleRight.setFillPattern(CellStyle.SOLID_FOREGROUND);
			rushRepairCellStyleRight.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
			rushRepairCellStyleRight.setBorderBottom(CellStyle.BORDER_THIN); //下邊框
			rushRepairCellStyleRight.setBorderLeft(CellStyle.BORDER_THIN); //左邊框
			rushRepairCellStyleRight.setBorderTop(CellStyle.BORDER_THIN); //上邊框
			rushRepairCellStyleRight.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
			rushRepairCellStyleRight.setBottomBorderColor(IndexedColors.BLACK.getIndex()); //下邊框顏色
			rushRepairCellStyleRight.setLeftBorderColor(IndexedColors.BLACK.getIndex()); //左邊框顏色
			rushRepairCellStyleRight.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上邊框顏色
			cellStyles[6] = rushRepairCellStyleRight;
			
			HSSFFont fontInOverHourYellow=wb.createFont();
			fontInOverHourYellow.setColor(HSSFColor.RED.index);//HSSFColor.VIOLET.index //字体颜色黃色
			//fontInOverHourYellow.setFontHeightInPoints((short)12);
			fontInOverHourYellow.setCharSet(HSSFFont.DEFAULT_CHARSET);
			//水平居中樣式
			HSSFCellStyle cellStyleConterInOverHourYellow = wb.createCellStyle();
			//自動換行
			//cellStyleConterInOverHourYellow.setWrapText(true);
			//垂直居中顯示
			cellStyleConterInOverHourYellow.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//設置字體
			cellStyleConterInOverHourYellow.setFont(fontInOverHourYellow);
			//設置水平居中
			cellStyleConterInOverHourYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleConterInOverHourYellow.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
			cellStyleConterInOverHourYellow.setBorderBottom(CellStyle.BORDER_THIN); //下邊框
			cellStyleConterInOverHourYellow.setBorderLeft(CellStyle.BORDER_THIN); //左邊框
			cellStyleConterInOverHourYellow.setBorderTop(CellStyle.BORDER_THIN); //上邊框
			cellStyleConterInOverHourYellow.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
			cellStyleConterInOverHourYellow.setBottomBorderColor(IndexedColors.BLACK.getIndex()); //下邊框顏色
			cellStyleConterInOverHourYellow.setLeftBorderColor(IndexedColors.BLACK.getIndex()); //左邊框顏色
			cellStyleConterInOverHourYellow.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上邊框顏色
			cellStyles[7] = cellStyleConterInOverHourYellow;
			
			HSSFFont fontInOverHourRed=wb.createFont();
			fontInOverHourRed.setColor(HSSFColor.ORANGE.index);//HSSFColor.VIOLET.index //字体颜色紅色
			//fontInOverHourRed.setFontHeightInPoints((short)12);
			fontInOverHourYellow.setCharSet(HSSFFont.DEFAULT_CHARSET);
			//水平居中樣式
			HSSFCellStyle cellStyleConterInOverHourRed = wb.createCellStyle();
			//自動換行
			//cellStyleConterInOverHourRed.setWrapText(true);
			//垂直居中顯示
			cellStyleConterInOverHourRed.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//設置字體
			cellStyleConterInOverHourRed.setFont(fontInOverHourRed);
			//設置水平居中
			cellStyleConterInOverHourRed.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleConterInOverHourRed.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
			cellStyleConterInOverHourRed.setBorderBottom(CellStyle.BORDER_THIN); //下邊框
			cellStyleConterInOverHourRed.setBorderLeft(CellStyle.BORDER_THIN); //左邊框
			cellStyleConterInOverHourRed.setBorderTop(CellStyle.BORDER_THIN); //上邊框
			cellStyleConterInOverHourRed.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
			cellStyleConterInOverHourRed.setBottomBorderColor(IndexedColors.BLACK.getIndex()); //下邊框顏色
			cellStyleConterInOverHourRed.setLeftBorderColor(IndexedColors.BLACK.getIndex()); //左邊框顏色
			cellStyleConterInOverHourRed.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上邊框顏色
			cellStyles[8] = cellStyleConterInOverHourRed;
			
			HSSFFont fontInOverWarnningYellowBackYellow=wb.createFont();
			fontInOverWarnningYellowBackYellow.setColor(HSSFColor.RED.index);//HSSFColor.VIOLET.index //字体颜色黃色
			//fontInOverWarnningYellowBackYellow.setFontHeightInPoints((short)12);
			fontInOverHourYellow.setCharSet(HSSFFont.DEFAULT_CHARSET);
			//水平居中樣式
			HSSFCellStyle cellStyleConterInOverWarnningYellow = wb.createCellStyle();
			//自動換行
			//cellStyleConterInOverWarnningYellow.setWrapText(true);
			//垂直居中顯示
			cellStyleConterInOverWarnningYellow.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//設置字體
			cellStyleConterInOverWarnningYellow.setFont(fontInOverWarnningYellowBackYellow);
			//設置水平居中
			cellStyleConterInOverWarnningYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//背景色黃色
			cellStyleConterInOverWarnningYellow.setFillForegroundColor(IndexedColors.YELLOW.getIndex());  
			cellStyleConterInOverWarnningYellow.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyleConterInOverWarnningYellow.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
			cellStyleConterInOverWarnningYellow.setBorderBottom(CellStyle.BORDER_THIN); //下邊框
			cellStyleConterInOverWarnningYellow.setBorderLeft(CellStyle.BORDER_THIN); //左邊框
			cellStyleConterInOverWarnningYellow.setBorderTop(CellStyle.BORDER_THIN); //上邊框
			cellStyleConterInOverWarnningYellow.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
			cellStyleConterInOverWarnningYellow.setBottomBorderColor(IndexedColors.BLACK.getIndex()); //下邊框顏色
			cellStyleConterInOverWarnningYellow.setLeftBorderColor(IndexedColors.BLACK.getIndex()); //左邊框顏色
			cellStyleConterInOverWarnningYellow.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上邊框顏色
			cellStyles[9] = cellStyleConterInOverWarnningYellow;
			
			HSSFFont fontInOverWarnningRed=wb.createFont();
			fontInOverWarnningRed.setColor(HSSFColor.ORANGE.index);//HSSFColor.VIOLET.index //字体颜色黃色
			//fontInOverWarnningRed.setFontHeightInPoints((short)12);
			fontInOverHourYellow.setCharSet(HSSFFont.DEFAULT_CHARSET);
			//水平居中樣式
			HSSFCellStyle cellStyleConterInOverWarnningRed = wb.createCellStyle();
			//自動換行
			//cellStyleConterInOverWarnningRed.setWrapText(true);
			//垂直居中顯示
			cellStyleConterInOverWarnningRed.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//設置字體
			cellStyleConterInOverWarnningRed.setFont(fontInOverWarnningRed);
			//設置水平居中
			cellStyleConterInOverWarnningRed.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleConterInOverWarnningRed.setFillForegroundColor(IndexedColors.YELLOW.getIndex());  
			cellStyleConterInOverWarnningRed.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyleConterInOverWarnningRed.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
			cellStyleConterInOverWarnningRed.setBorderBottom(CellStyle.BORDER_THIN); //下邊框
			cellStyleConterInOverWarnningRed.setBorderLeft(CellStyle.BORDER_THIN); //左邊框
			cellStyleConterInOverWarnningRed.setBorderTop(CellStyle.BORDER_THIN); //上邊框
			cellStyleConterInOverWarnningRed.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
			cellStyleConterInOverWarnningRed.setBottomBorderColor(IndexedColors.BLACK.getIndex()); //下邊框顏色
			cellStyleConterInOverWarnningRed.setLeftBorderColor(IndexedColors.BLACK.getIndex()); //左邊框顏色
			cellStyleConterInOverWarnningRed.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上邊框顏色
			cellStyles[10] = cellStyleConterInOverWarnningRed;
		}  catch (Exception e) { 
            throw e;
        } 
		return cellStyles;
	 }
	/**
	 * Purpose: 根據實體類屬性名稱取實體類中的屬性值
	 * @author HermanWang
	 * @param fieldName 屬性名稱
	 * @param object 属性所在类
	 * @return String 属性值
	 */
	 public static String getFiledValueByName(String fieldName, Object object) throws Exception {
	    	String result = null;
	        try { 
	        	if (StringUtils.hasText(fieldName)) {
	        		String methodName = "";
	        		if(StringUtils.hasText(fieldName)){
	        			methodName = "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
	        			Object invokeMethod = BeanUtils.invokeMethod(object, methodName, null, null);
	        			Class fieldType = BeanUtils.getFieldType(object, fieldName);
	        			//date
	        			if(fieldType == BeanUtils.UTIL_DATE_CLASS || fieldType == BeanUtils.SQL_DATE_CLASS) {
	        				if(invokeMethod != null) {
	        					result = DateTimeUtils.toString((Date)invokeMethod, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH) + IAtomsConstants.IS_DATE_OR_TIMESTAMP;
	        				} else {
	        					result = IAtomsConstants.IS_DATE_OR_TIMESTAMP;
	        				}
	        			//Timestamp
	        			} else if(fieldType == BeanUtils.TIMESTAMP_CLASS || fieldType == BeanUtils.TIME_CLASS) {
	        				if(invokeMethod != null) {
	        					result = DateTimeUtils.toString((Timestamp)invokeMethod, DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_SLASH) + IAtomsConstants.IS_DATE_OR_TIMESTAMP;
	        				} else {
	        					result = IAtomsConstants.IS_DATE_OR_TIMESTAMP;
	        				}
	        			//String
	        			} else if(fieldType == BeanUtils.STRING_CLASS) {
	        				if(invokeMethod != null && StringUtils.hasText(invokeMethod.toString())) {
	        					//改變頁面上formmit數值 
	    	        			if(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_STATUS.getValue().equals(fieldName)) {
	    	        				//已回應
	    	        				result = i18NUtil.getName(IAtomsConstants.CASE_STATUS.RESPONSED.getCode()) + IAtomsConstants.NO_DATE_OR_TIMESTAMP;
	    	        			} else if(SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_STATUS.getValue().equals(fieldName)) {
	    	        				//已到場
	    	        				result = i18NUtil.getName(IAtomsConstants.CASE_STATUS.ARRIVED.getCode()) + IAtomsConstants.NO_DATE_OR_TIMESTAMP;
	    	        			} else if(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_STATUS.getValue().equals(fieldName)) {
	    	        				//已完修
	    	        				result = i18NUtil.getName(IAtomsConstants.PARAM_COMPLET) + IAtomsConstants.NO_DATE_OR_TIMESTAMP;
	    	        			//專案 電子發票載具 銀聯閃付 是否同裝機地址 是否開啟加密 電子化繳費平台
	    	        			} else if(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue().equals(fieldName)
	    	        					|| SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue().equals(fieldName)
	    	        					|| SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue().equals(fieldName)
	    	        					|| SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue().equals(fieldName)
	    	        					|| SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue().equals(fieldName)
	    	        					|| SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue().equals(fieldName)
	    	        					|| SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue().equals(fieldName)) {
	    	        				if(invokeMethod != null) {
	    	        					result = i18NUtil.getName(invokeMethod.toString()) + IAtomsConstants.NO_DATE_OR_TIMESTAMP;
	    	        				}
	    	        			//回應到場完修
	    	        			} else {
	    	        				result = invokeMethod.toString() + IAtomsConstants.NO_DATE_OR_TIMESTAMP;
	    	        			}
	        				} else {
	        					result = IAtomsConstants.NO_DATE_OR_TIMESTAMP;
	        				}
	        			//數字
	        			} else {
	        				result = invokeMethod + IAtomsConstants.D_NUMBER_BIGDECIMAL;
	        			}
	        			/*Method method = object.getClass().getMethod(methodName, new Class[] {}); 
	    				Object obj = method.invoke(object, new Object[] {});
	    				if (obj != null) {
	    					if(obj instanceof Timestamp){
	    						result = DateTimeUtils.toString((Timestamp)obj, DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_SLASH) + "isDateOrTimestamp";
	    					}else if(obj instanceof Date){
	    						result = DateTimeUtils.toString((Date)obj, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH) + "isDateOrTimestamp";
	    					}else{
	    						result = obj.toString() + "noDateOrTimestamp";
	    					}
	    				}*/
	        		}
	        	}
	        } catch (Exception e) { 
	            throw e;
	        } 
	        return result;
	    }

	 /**
	  * Purpose:處理doc（設備借用申請單）
	  * @author amandawang
	  * @param docMap word所需map
	  * @param dmmRepositoryDTOs 庫存主檔dto集合
	  * @param docName 文檔名稱
	  * @param path 路徑
	  * @throws Exception
	  * @return void
	  */
	public static void dealDoc(Map<String, String> docMap, List<DmmRepositoryDTO> dmmRepositoryDTOs, String docName, String path) throws Exception {  
		InputStream is = null;
		OutputStream os = null;
		try { 
			is = new FileInputStream(path + docName);
			//doc對象
			HWPFDocument doc = new HWPFDocument(is);
			Range range = doc.getRange();
			Set<String> set = docMap.keySet();
			//doc中所需值替換為map中數據
			for (String string : set) {
				range.replaceText(string, docMap.get(string));
			}
			TableIterator it = new TableIterator(range);
			TableCell td = null;
			StringBuffer stringBuffer = null;
			while (it.hasNext()) {
				Table table = (Table) it.next();
				// 迭代行
				for (int i = 1; i < table.numRows(); i++) {
					// 獲取需要的表格
					TableRow tr = table.getRow(i);
					// 替換表格中的數據
					if (i <= dmmRepositoryDTOs.size()) {
						td = tr.getCell(1);
						stringBuffer = new StringBuffer();
						stringBuffer.append(IAtomsConstants.MARK_DOLLAR).append(IAtomsConstants.COLUMN_F).append(IAtomsConstants.MARK_BRACES_LEFT).append(DmmRepositoryDTO.ATTRIBUTE.NAME.getValue()).append(IAtomsConstants.MARK_BRACES_RIGHT);
						LOGGER.debug("POIUtilS.dealDoc dmmRepositoryDTOs.get(i - 1).getName()=" + dmmRepositoryDTOs.get(i - 1).getName());
						td.getParagraph(0).replaceText(stringBuffer.toString() + i, dmmRepositoryDTOs.get(i - 1).getName() != null ? dmmRepositoryDTOs.get(i - 1).getName() : IAtomsConstants.MARK_EMPTY_STRING);
						td = tr.getCell(2);
						stringBuffer = new StringBuffer();
						stringBuffer.append(IAtomsConstants.MARK_DOLLAR).append(IAtomsConstants.COLUMN_F).append(IAtomsConstants.MARK_BRACES_LEFT).append(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()).append(IAtomsConstants.MARK_BRACES_RIGHT);
						LOGGER.debug("POIUtilS.dealDoc dmmRepositoryDTOs.get(i - 1).getSerialNumber()=" + dmmRepositoryDTOs.get(i - 1).getSerialNumber());
						td.getParagraph(0).replaceText(stringBuffer.toString() + i, dmmRepositoryDTOs.get(i - 1).getSerialNumber());
						td = tr.getCell(3);
						stringBuffer = new StringBuffer();
						stringBuffer.append(IAtomsConstants.MARK_DOLLAR).append(IAtomsConstants.COLUMN_V).append(IAtomsConstants.MARK_BRACES_LEFT).append(IAtomsConstants.PARAM_COMMENT).append(IAtomsConstants.MARK_BRACES_RIGHT);
						LOGGER.debug("POIUtilS.dealDoc dmmRepositoryDTOs.get(i - 1).getBorrowerComment()=" + dmmRepositoryDTOs.get(i - 1).getBorrowerComment());
						td.getParagraph(0).replaceText(stringBuffer.toString() + i, dmmRepositoryDTOs.get(i - 1).getBorrowerComment() == null ? IAtomsConstants.MARK_EMPTY_STRING : dmmRepositoryDTOs.get(i - 1).getBorrowerComment());
					} else {
						// 刪除無用行
						tr.delete();
						LOGGER.debug("POIUtilS.dealDoc dmmRepositoryDTOs.get(i - 1) delete row=" + i);
					}
				}
				break;
			}
			os = new FileOutputStream(path+docName);  
			// 把doc输出到输出流中  
			doc.write(os); 
			os.flush();
		} catch(Exception e) {
			LOGGER.error("POIUtilS.dealDoc is error ");
			 e.printStackTrace();  
		} finally{
			closeStream(os);  
			closeStream(is);
		}
	}
	 	/** 
	    * 关闭输入流 
	    * @param is 
	    */  
	   private static void closeStream(InputStream is) {  
	      if (is != null) {  
	         try {  
	            is.close();  
	         } catch (IOException e) {  
	            e.printStackTrace();  
	         }  
	      }  
	   }  
	   /** 
	    * 关闭输出流 
	    * @param os 
	    */  
	   private static void closeStream(OutputStream os) {  
	      if (os != null) {  
	         try {  
	            os.close();  
	         } catch (IOException e) {  
	            e.printStackTrace();  
	         }  
	      }  
	   }  
	/**
	 * 讀取到交易參數時 記錄的行號
	*/
	private int row = 0;
	/**
	 *  存放交易參數的map key是 交易參數的列號， value 是 這一列對應的模板上的code
	*/
	private Map<Integer, String> cellColumnMap = new HashMap<Integer, String>();
	/**
	 * 交易參數dtoList
	 */
	private List<SrmCaseTransactionParameterDTO> srmCaseTransactionParameterDTOList = new ArrayList<SrmCaseTransactionParameterDTO>();
	/**
	 * 工作薄，用於初始化不同的樣式
	 */
	private HSSFWorkbook workbook = new HSSFWorkbook();
	/**
	 * 某個交易類別支持的交易參數的list
	 */
	private List<String> editFilds = new ArrayList<String>();
	
	/**
	 * Purpose:列印創建excel
	 * @author HermanWang
	 * @param templatesName：臨時文件名稱
	 * @param tempPath：path路徑
	 * @param srmCaseHandleInfoDTOList：正在處理的案件的行
	 * @param editFildsMap：每種交易類別支持的交易參數
	 * @param x
	 * @return void
	 */
	public void createExcelDynamic(String templatesName, String tempPath, 
			List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOList, Map<String,List<String>> editFildsMap, int x){
		try {
			POIUtils pOIUtils = new POIUtils();
			//原模板
			FileInputStream sourceFis = new FileInputStream(tempPath + templatesName);
			HSSFWorkbook sourceWork = new HSSFWorkbook(sourceFis);
			HSSFSheet fromSheet1 = null;
			HSSFSheet fromSheet2 = null;
			HSSFWorkbook wbCreat = new HSSFWorkbook();
			HSSFSheet toSheet = null;
			//獲取fromSheet
			fromSheet1 = sourceWork.getSheetAt(0);
			fromSheet2 = sourceWork.getSheetAt(1);
			//獲取toSheet
			toSheet = wbCreat.createSheet(fromSheet1.getSheetName());
			
			HSSFPrintSetup printSetup = toSheet.getPrintSetup();
			printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); // 纸张
			
			toSheet.setMargin(HSSFSheet.TopMargin,fromSheet1.getMargin(HSSFSheet.TopMargin));// 页边距（上）    
			toSheet.setMargin(HSSFSheet.BottomMargin,fromSheet1.getMargin(HSSFSheet.BottomMargin));// 页边距（下）    
			toSheet.setMargin(HSSFSheet.LeftMargin,fromSheet1.getMargin(HSSFSheet.LeftMargin) );// 页边距（左）    
			toSheet.setMargin(HSSFSheet.RightMargin,fromSheet1.getMargin(HSSFSheet.RightMargin));// 页边距（右
			
			toSheet.setMargin(HSSFSheet.HeaderMargin, fromSheet1.getMargin(HSSFSheet.HeaderMargin));//页眉  
			toSheet.setMargin(HSSFSheet.FooterMargin, fromSheet1.getMargin(HSSFSheet.FooterMargin));//页脚 
			
			//copySheet
			pOIUtils.copySheetDynamic(toSheet, fromSheet1,fromSheet2, wbCreat, sourceWork, srmCaseHandleInfoDTOList, editFildsMap);
			FileOutputStream fileOut = new FileOutputStream(tempPath + x+CaseManagerFormDTO.CASE_EXPORT_TEMPLATE_NAME_FOR_EN);
			wbCreat.write(fileOut);
			fileOut.close();
			sourceFis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:：拷贝sheet copySheet(targetSheet, sourceSheet, targetWork, sourceWork, true)
	 * @author HermanWang
	 * @param targetSheet：目標sheet
	 * @param sourceSheet：原sheet
	 * @param targetWork：目標表文件
	 * @param sourceWork：源表 
	 * @param srmCaseHandleInfoDTOList：要列印的行
	 * @param editFildsMap：每種交易類別支持的交易參數map
	 * @throws Exception
	 * @return void
	 */
	public void copySheetDynamic(HSSFSheet targetSheet, HSSFSheet sourceSheet1,HSSFSheet sourceSheet2,
			HSSFWorkbook targetWork, HSSFWorkbook sourceWork, 
			List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOList, Map<String,List<String>> editFildsMap) throws Exception{
		if(targetSheet == null || sourceSheet1 == null || targetWork == null || sourceWork == null){
			throw new IllegalArgumentException("调用PoiUtil.copySheet()方法时，targetSheet、sourceSheet、targetWork、sourceWork都不能为空，故抛出该异常！");
		}
		copySheetAndSetValueDynamic(targetSheet, sourceSheet1,sourceSheet2, targetWork, sourceWork, true, srmCaseHandleInfoDTOList, editFildsMap);
	} 
	
	/**
	 * Purpose：拷贝sheet
	 * @author HermanWang
	 * @param targetSheet：目標sheet
	 * @param sourceSheet：原sheet
	 * @param targetWork：目標表文件
	 * @param sourceWork：源表
	 * @param copyStyle：是否copy樣式
	 * @param srmCaseHandleInfoDTOList：要列印的行
	 * @param editFildsMap：每種交易類別支持的交易參數map
	 * @throws Exception
	 * @return void
	 */
	public void copySheetAndSetValueDynamic(HSSFSheet targetSheet, HSSFSheet sourceSheet1,HSSFSheet sourceSheet2,
			HSSFWorkbook targetWork, HSSFWorkbook sourceWork, boolean copyStyle,
			List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOList, Map<String,List<String>> editFildsMap)throws Exception {
		
		if(targetSheet == null || sourceSheet1 == null || targetWork == null || sourceWork == null){
			throw new IllegalArgumentException("调用PoiUtil.copySheet()方法时，targetSheet、sourceSheet、targetWork、sourceWork都不能为空，故抛出该异常！");
		}
		//复制源表中的列數目
		int maxColumnNum = 0;
		//設置樣式
		Map styleMap = (copyStyle) ? new HashMap() : null;
		HSSFPatriarch patriarch = targetSheet.createDrawingPatriarch(); //用于复制注释
		int pageSize = sourceSheet1.getLastRowNum() - sourceSheet1.getFirstRowNum() + 1;
		int k = 0;
		//選取的案件dtoList
		for(int j=0;j<srmCaseHandleInfoDTOList.size();j++){
			//拿到具體的案件資訊進行列印
			SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = srmCaseHandleInfoDTOList.get(j);
			int n = k;
			if (srmCaseHandleInfoDTO.getCaseTransactionParameterDTOs().size()<=9) {
				//讀取源表的所有行
				for (int i = sourceSheet2.getFirstRowNum(); i <= sourceSheet2.getLastRowNum(); i++) {
					//源表的行
					HSSFRow sourceRow = sourceSheet2.getRow(i);
					//目標表的行
					HSSFRow targetRow = targetSheet.createRow(k);
					if (sourceRow != null) {
						copyRowAndSetValue(targetRow, sourceRow, targetWork, sourceWork, patriarch, styleMap,
								srmCaseHandleInfoDTO, editFildsMap);
						if (sourceRow.getLastCellNum() > maxColumnNum) {
							maxColumnNum = sourceRow.getLastCellNum();
						}
						k++;
					}
				}
			} else {
				//讀取源表的所有行
				for (int i = sourceSheet1.getFirstRowNum(); i <= sourceSheet1.getLastRowNum(); i++) {
					//源表的行
					HSSFRow sourceRow = sourceSheet1.getRow(i);
					//目標表的行
					HSSFRow targetRow = targetSheet.createRow(k);
					if (sourceRow != null) {
						copyRowAndSetValue(targetRow, sourceRow, targetWork, sourceWork, patriarch, styleMap,
								srmCaseHandleInfoDTO, editFildsMap);
						if (sourceRow.getLastCellNum() > maxColumnNum) {
							maxColumnNum = sourceRow.getLastCellNum();
						}
						k++;
					}
				}
			}
			//复制源表中的合并单元格
			mergerRegion1Dynamic(targetSheet, sourceSheet1,sourceSheet2, srmCaseHandleInfoDTO,n,k);
			
			//复制源表中的合并单元格
			//mergerRegion1(targetSheet, sourceSheet1, srmCaseHandleInfoDTOList.size());
			//copy圖片
			List<HSSFPictureData> pictures = sourceWork.getAllPictures();
			if(sourceSheet1.getDrawingPatriarch() != null) {
				for (HSSFShape shape : sourceSheet1.getDrawingPatriarch().getChildren()) {  
					//坐標位置
					HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();  
					if (shape instanceof HSSFPicture) {  
						HSSFPicture pic = (HSSFPicture) shape;  
						int pictureIndex = pic.getPictureIndex()-1;  
						HSSFClientAnchor targetAnchor = new HSSFClientAnchor
								(anchor.getDx1(), anchor.getDy1(), 
										anchor.getDx2(),  anchor.getDy2(),
										anchor.getCol1(), j*pageSize, 
										anchor.getCol2(), j*pageSize + 1);
						targetAnchor.setAnchorType(anchor.getAnchorType());
						//插入图片    
						HSSFPictureData picData = pictures.get(pictureIndex);
						patriarch.createPicture(targetAnchor, targetWork.addPicture(picData.getData(), anchor.getAnchorType()));
					}  
				}
			}
			//添加分頁符
			targetSheet.setRowBreak(k - 1);
		}
		//设置目标sheet的列宽
		for (int i = 0; i <= maxColumnNum; i++) {
			targetSheet.setColumnWidth(i, sourceSheet1.getColumnWidth(i));
		}
	}
	
	/**
	 * 功能：复制原有sheet的合并单元格到新创建的sheet
	 * 
	 * @param sheetCreat
	 * @param sourceSheet
	 */
	public static void mergerRegion1Dynamic(HSSFSheet targetSheet, HSSFSheet sourceSheet1,HSSFSheet sourceSheet2, SrmCaseHandleInfoDTO srmCaseHandleInfoDTO,int n,int k)throws Exception {
		if(targetSheet == null || sourceSheet1 == null){
			throw new IllegalArgumentException("调用PoiUtil.mergerRegion()方法时，targetSheet或者sourceSheet不能为空，故抛出该异常！");
		}
		if (srmCaseHandleInfoDTO.getCaseTransactionParameterDTOs().size()<=9) {
			for (int i = 0; i < sourceSheet2.getNumMergedRegions(); i++) {
				CellRangeAddress oldRange = sourceSheet2.getMergedRegion(i);
				CellRangeAddress newRange = new CellRangeAddress(
						oldRange.getFirstRow()+n, oldRange.getLastRow()+n,
						oldRange.getFirstColumn(), oldRange.getLastColumn());
				targetSheet.addMergedRegion(newRange);
			}
		} else {
			for (int i = 0; i < sourceSheet1.getNumMergedRegions(); i++) {
				CellRangeAddress oldRange = sourceSheet1.getMergedRegion(i);
				CellRangeAddress newRange = new CellRangeAddress(
						oldRange.getFirstRow()+n, oldRange.getLastRow()+n,
						oldRange.getFirstColumn(), oldRange.getLastColumn());
				targetSheet.addMergedRegion(newRange);
			}
		}
	}
	   /**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * @return the cellColumnMap
	 */
	public Map<Integer, String> getCellColumnMap() {
		return cellColumnMap;
	}

	/**
	 * @param cellColumnMap the cellColumnMap to set
	 */
	public void setCellColumnMap(Map<Integer, String> cellColumnMap) {
		this.cellColumnMap = cellColumnMap;
	}

	/**
	 * @return the srmCaseTransactionParameterDTOList
	 */
	public List<SrmCaseTransactionParameterDTO> getSrmCaseTransactionParameterDTOList() {
		return srmCaseTransactionParameterDTOList;
	}

	/**
	 * @param srmCaseTransactionParameterDTOList the srmCaseTransactionParameterDTOList to set
	 */
	public void setSrmCaseTransactionParameterDTOList(
			List<SrmCaseTransactionParameterDTO> srmCaseTransactionParameterDTOList) {
		this.srmCaseTransactionParameterDTOList = srmCaseTransactionParameterDTOList;
	}

	/**
	 * @return the workbook
	 */
	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	/**
	 * @param workbook the workbook to set
	 */
	public void setWorkbook(HSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	/**
	 * @return the editFilds
	 */
	public List<String> getEditFilds() {
		return editFilds;
	}

	/**
	 * @param editFilds the editFilds to set
	 */
	public void setEditFilds(List<String> editFilds) {
		this.editFilds = editFilds;
	}
	
}
