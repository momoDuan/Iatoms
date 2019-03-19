package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * Purpose: 案件商業性邏輯核檢service接口
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年6月27日
 * @MaintenancePersonnel CarrieDuan
 */
public interface ICaseCheckInfoService extends IAtomicService {
	/**
	 * Purpose: GP公司下S80 Ethernet需核檢內容
	 * @author CarrieDuan
	 * @param inquiryContext：需要核檢的數據。包含【DTID、鏈接方式、內鍵功能、周邊設備、周邊設備功能、AO人員、交易參數】
	 * @throws ServiceException: 出錯時抛出出ServiccseException
	 * @return String：錯誤信息
	 */
	public String checkS80EthernetAsset(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose: GP公司下S80 RF需核檢內容
	 * @author CarrieDuan
	 * @param inquiryContext：需要核檢的數據。包含【DTID、鏈接方式、內鍵功能、周邊設備、周邊設備功能、AO人員、交易參數】
	 * @throws ServiceException: 出錯時抛出出ServiccseException
	 * @return String：錯誤信息
	 */
	public String checkS80RFAsset(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose: GP公司下S90 3G需核檢內容
	 * @author CarrieDuan
	 * @param caseInfo：需要核檢的數據。包含【DTID、鏈接方式、內鍵功能、周邊設備】
	 * @throws ServiceException: 出錯時抛出出ServiccseException
	 * @return String
	 */
	public String checkS903GAsset(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose: GP公司下S90 RF需核檢內容
	 * @author CarrieDuan
	 * @param inquiryContext：需要核檢的數據。包含【DTID、鏈接方式、內鍵功能、周邊設備】
	 * @throws ServiceException: 出錯時抛出出ServiccseException
	 * @return String
	 */
	public String checkS90RFAsset(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:GP獲取客戶與刷卡機型的軟件版本
	 * @author CarrieDuan
	 * @param inquiryContext：需要核檢的數據
	 * @throws ServiceException: 出錯時抛出出ServiccseException
	 * @return String：返回結果是否正確與軟件版本
	 */
	public String checkVersion(MultiParameterInquiryContext inquiryContext) throws ServiceException;
}
