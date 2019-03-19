package com.cybersoft4u.xian.iatoms.services;

import java.util.List;
import java.util.Map;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO;

/**
 * Purpose: 案件處理Service接口
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年8月5日
 * @MaintenancePersonnel CrissZhang
 */
@SuppressWarnings("rawtypes")
public interface ICaseManagerService extends IAtomicService {

	/**
	 * 
	 * Purpose: 初始化頁面
	 * @author CrissZhang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException Service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:初始化案件詳細畫面（案件建案）
	 * @author CrissZhang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException Service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext initDetail(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:初始化案件欄位頁面
	 * @author CrissZhang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException Service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext initColumnBlock(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:案件查詢
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext queryCase(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:初始化查詢DTID頁面
	 * @author CarrieDuan
	 * @param sessionContext
	 * @throws ServiceException
	 * @return SessionContext
	 */
	public SessionContext initDtid(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢DTID
	 * @author echomou
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext queryDTID(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 根據ID獲取案件資料檔
	 * @author CarrieDuan
	 * @param param：參數
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return CaseHandleInfoDTO：案件處理歷史資料檔
	 */
	public SrmCaseHandleInfoDTO getCaseMessageByCaseId(MultiParameterInquiryContext param) throws ServiceException;
	/**
	 * Purpose: 根據IDList獲取案件資料檔List
	 * @author HermanWang
	 * @param param：參數
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return CaseHandleInfoDTO：案件處理歷史資料檔
	 */
	public SessionContext getCaseManagerListByCaseId(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 根據caseID獲取案件交易類別
	 * @author HermanWang
	 * @param param：參數
	 * @throws ServiceException:出錯時抛出出ServiccseException
	 * @return CaseHandleInfoDTO：案件處理歷史資料檔
	 */
	public SessionContext getCaseTransactionParameterByCaseId(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 根據caseID獲取案件歷史記錄
	 * @author HermanWang
	 * @param param：參數
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return CaseHandleInfoDTO：案件處理歷史資料檔
	 */
	public SessionContext getCaseTransactionDTOByCaseId(SessionContext sessionContext) throws ServiceException;/**
	 * Purpose:獲得交易參數項目list
	 * @author evanliu
	 * @param inquiryContext : ajax傳入的參數
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return List<SrmTransactionParameterItemDTO>：交易參數項目list
	 */
	public List<SrmTransactionParameterItemDTO> getTransactionParameterItemList(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:獲得交易參數項目list--案件匯入時使用
	 * @author CarrieDuan
	 * @param sessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	//public SessionContext getTransactionParameterItemList(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:獲取交易參數可以編輯的列名，以交易參數分組
	 * @author evanliu
	 * @param inquiryContext : ajax傳入的參數
	 * @throws ServiceException：出錯時抛出出ServiceException
	 * @return Map<String,List<String>>：交易參數分組的可以編輯的列集合。key：交易參數code，value：可以編輯的列名list
	 */
	public Map<String,List<String>> getEditFieldsGroupbyTransactionType(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:保存案件資料，裝機，併機，報修，異動等
	 * @author CrissZhang
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException：出錯時抛出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext createCase(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:保存上傳文件
	 * @author CrissZhang
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException：出錯時抛出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext saveFile(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:刪除臨時目錄文件
	 * @author CrissZhang
	 * @param sessionContext : 上下文SessionContext
	 * @throws ServiceException : 出錯時抛出ServiceException
	 * @return SessionContext : 上下文SessionContext
	 */
	public SessionContext deleteTempFile(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose：獲取文件路徑
	 * @author CarrieDuan
	 * @param param
	 * @throws ServiceException：出錯時抛出出ServiceException
	 * @return String：文件路徑
	 */
	public String checkCaseAttFile(MultiParameterInquiryContext param) throws ServiceException;
	
	/**
	 * Purpose：獲取文件信息
	 * @author CarrieDuan
	 * @param command:案件處理FormDTO
	 * @throws ServiceException：出錯時抛出出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext checkCaseAttFile(CaseManagerFormDTO command) throws ServiceException;
	
	/**
	 * Purpose:驗證dtid可用號碼數
	 * @author CrissZhang
	 * @param inquiryContext : ajax傳入的參數
	 * @throws ServiceException : 出錯時拋出ServiceException
	 * @return Map<String, Object> ： 返回一個map類型
	 */
	public Map<String, Object> checkDtidNumber(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose: 判斷內建功能與周邊設備功能選項重複的支援功能
	 * @author CrissZhang
	 * @param inquiryContext ： 傳入的參數
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return String : 返回重複的支援功能
	 */
	public String getRepeatSupportFun(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose: 進入編輯
	 * @author CrissZhang
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:發送郵件
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	//public SessionContext send(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:簽收案件歷史記錄
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext sign(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:顯示記錄查詢案件記錄
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext queryTransaction(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:顯示是否重複進建以及重複進建的caseid
	 * @author HermanWang
	 * @param MultiParameterInquiryContext:MultiParameterInquiryContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public Map<String, String> getCaseRepeatList(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose:檢核改筆案件關聯的設備信息是否被通dtid之前的案件修改過
	 * @author HermanWang
	 * @param inquiryContext :MultiParameterInquiryContext
	 * @throws ServiceException :出錯時拋出ServiceException
	 * @return Map<String,Object> ：返回一個map對象
	 */
	public Map<String, Object> getCaseLinkIsChange(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose:查詢該筆dtid下比本案件建安早的裝機案件數量
	 * @author amandawang
	 * @param inquiryContext :MultiParameterInquiryContext
	 * @throws ServiceException :出錯時拋出ServiceException
	 * @return String ：是否無值， 無：Y，有：N
	 */
	public String getCountByInstall(MultiParameterInquiryContext inquiryContext)throws ServiceException;
	/**
	 * Purpose:檢核改筆案件的上一筆是否為裝機，如果為裝機案件 則上一筆案件 須要完修
	 * @author amandawang
	 * @param inquiryContext :MultiParameterInquiryContext
	 * @throws ServiceException :出錯時拋出ServiceException
	 * @return Map<String,Object> ：返回一個map對象
	 */
	public Map<String, Object> getCaseRepeatByInstallUncomplete(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose:顯示案件所對應的處理中的設備
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext queryCaseAssetLink(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:顯示案件所對應的處理中的耗材
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext queryCaseSuppliesLink(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:初始化EDC界面
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initChooseEDC(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:得到問題原因的下拉框
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter> ： 返回下拉框數據
	 */
	public List<Parameter> getProblemReasonList(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:得到問題解決方式的下拉框
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter> ： 返回下拉框數據
	 */
	public List<Parameter> getProblemSolutionList(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose:查詢案件對應的設備
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext queryCaseAsset(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 案件匯入--核檢數據是否正確
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext upload(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:新增記錄案件動作的保存處理
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext addRecord(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:完修案件動作的保存處理
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext complete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:線上排除案件動作的保存處理
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext onlineExclusion(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:結案動作
	 * @param sessionContext:上下文SessionContext
	 * @return SessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 */
	public SessionContext closed(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:派工動作的保存處理
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext dispatching(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:立即結案動作
	 * @param sessionContext:上下文SessionContext
	 * @return SessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 */
	public SessionContext immediatelyClosing(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:延期動作
	 * @author CarrieDuan
	 * @param sessionContext
	 * @throws ServiceException
	 * @return SessionContext
	 */
	public SessionContext delay(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:獲取上傳的範本的路徑根據範本的id
	 * @author HermanWang
	 * @param SessionContext:上下文SessionContext
	 * @throws ServiceException：出錯時, 丟出ServiceException
	 * @return String
	 */
	public SessionContext getCategoryByTemplatesId(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:建案--派工處理 與處理頁面待派工案件派工
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext dispatch(SessionContext sessionContext) throws ServiceException ;
	
	/**
	 * Purpose:作廢動作
	 * @param sessionContext:上下文SessionContext
	 * @return SessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 */
	public SessionContext voidCase(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 退回動作
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @return SessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 */
	public SessionContext retreat(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:催修動作
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @return SessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 */
	public SessionContext rushRepair(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:到場事件
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @return SessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 */
	public SessionContext arrive(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:回應
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext response(SessionContext sessionContext) throws ServiceException ;
	/**
	 * Purpose:修改案件類型
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext changeCaseType(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:根據案件編號得到案件信息
	 * @author CrissZhang
	 * @param inquiryContext : ajxa傳入參數
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SrmCaseHandleInfo ： 上下文SessionContext
	 */
	public Map getCaseInfoById(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose:根據已更新的案件編號得到案件信息
	 * @author CrissZhang
	 * @param inquiryContext : ajxa傳入參數
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SrmCaseHandleInfo ： 上下文SessionContext
	 */
	public Map getChangeCaseInfoById(MultiParameterInquiryContext inquiryContext)throws ServiceException;
	/**
	 * Purpose:自動派工
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext autoDispatching(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 選擇正在處理的案件，獲取資料
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext showDetailInfo(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose：根據案件編號獲取ao人員的mail
	 * @author HermanWang
	 * @param inquiryContext: ajxa傳入參數
	 * @throws ServiceException： 出錯時拋出ServiceException
	 * @return List<Parameter>:ao人員mailList
	 */
	public Map getAoEmailByCaseId(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose：根據案件編號案件相關的mail
	 * @author HermanWang
	 * @param inquiryContext: ajxa傳入參數
	 * @throws ServiceException： 出錯時拋出ServiceException
	 * @return List<Parameter>:ao人員mailList
	 */
	public Map getCaseEmailByCaseId(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose:判斷改筆案件是否有過簽收或者線上排除的記錄
	 * @author HermanWang
	 * @param param：ajax傳過來的參數
	 * @throws ServiceException： 出錯時拋出ServiceException
	 * @return Boolean：返回一個boolean類型的結果
	 */
	public Boolean isSignAndOnlineExclusion (MultiParameterInquiryContext param) throws ServiceException;
	
	/**
	 * Purpose:驗證是否異動設備
	 * @author CrissZhang
	 * @param param : ajax傳過來的參數
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return boolean：返回boolean
	 */
	public boolean isChangeAsset (MultiParameterInquiryContext param) throws ServiceException;
	/**
	 * Purpose: 行動版 -- 發送mail給客服
	 * @author amandawang
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext sendTo(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 修改實際完修時間
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext changeCompleteDate(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 修改進件時間
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext changeCreateDate(SessionContext sessionContext) throws ServiceException;
	

	/**
	 * Purpose: 處理rest Api
	 * @author amandawang
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext buildCaseByApi(SessionContext sessionContext)throws ServiceException;
	
	/**
	 * Purpose:配送中動作的保存處理
	 * @author neiljing
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext distribution(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 裝機案件確認授權
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext confirmAuthorizes(SessionContext sessionContext)throws ServiceException;
	/**
	 * Purpose:求償動作
	 * @author CarrieDuan
	 * @param sessionContext
	 * @throws ServiceException
	 * @return SessionContext
	 */
	public SessionContext payment(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:求償動作-Task #3500
	 * @author amandawang
	 * @param sessionContext
	 * @throws ServiceException
	 * @return SessionContext
	 */
	public SessionContext arrivalInspection(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 租賃預載
	 * @author amandawang
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext leasePreload(SessionContext sessionContext)throws ServiceException;
	/**
	 * Purpose: 租賃簽收
	 * @author amandawang
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext leaseSign(SessionContext sessionContext) throws ServiceException;
	

	//public void queryCaseInfo() throws ServiceException;

	/**
	 * Purpose:取得批次號碼
	 * @author tonychen
	 * @throws ServiceException
	 * @return String : 批次號碼
	 */
	public String createBatchNum() throws ServiceException;
	
	/**
	 * Purpose:TMS參數post
	 * @author TonyChen
	 * @param sURL:呼叫Server ; data:參數值
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return boolean：返回boolean
	 */
	public Map TMSPost (MultiParameterInquiryContext param) throws ServiceException;
	/**
	 * Purpose: 裝機案件確認授權取消
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext cancelConfirmAuthorizes(SessionContext sessionContext)throws ServiceException;
	/**
	 * Purpose: 協調完成結案匯入
	 * @author amandawang
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext uploadCoordinatedCompletion(SessionContext sessionContext)throws ServiceException;
	/**
	 * Purpose: api查詢案件資訊
	 * @author amandawang
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext queryCaseInfoByApi(SessionContext sessionContext);
	/**
	 * Purpose: api修改案件到場註記
	 * @author amandawang
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext changeCaseInfoByApi(SessionContext sessionContext);
	/**
	 * Purpose:獲取裝機案件編號
	 * @author amandawang
	 * @param inquiryContext
	 * @throws ServiceException
	 * @return String
	 */
	public String getInstallCaseId(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose:前臺核檢客戶爲環匯時，數據是否填寫正確
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext checkGpInfo(SessionContext sessionContext)throws ServiceException;
	/**
	 * Purpose:api修改案件設備逾期未還
	 * @author amandawang
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext changeCaseOverdueByApi(SessionContext sessionContext);

}
