package com.cybersoft4u.xian.iatoms.common;

import cafe.identity.bean.dto.FunctionDTO;
import cafe.workflow.bean.identity.IAccessControlPolicy;
import cafe.workflow.util.WfConstants;

/**
 * 
 * Purpose: 定義一般常數
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016/3/30
 * @MaintenancePersonnel allenchen
 */
public interface IAtomsConstants extends WfConstants{
	/**
	 * Purpose: 案件類型 
	 * @author evanliu
	 * @since  JDK 1.6
	 * @date   2016年12月12日
	 * @MaintenancePersonnel evanliu
	 */
	public static enum CASE_CATEGORY {
		/**
		 * 裝機
		 */
		INSTALL("INSTALL"),
		/**
		 * 併機
		 */
		MERGE("MERGE"),
		/**
		 * 異動
		 */
		UPDATE("UPDATE"),
		/**
		 * 拆機
		 */
		UNINSTALL("UNINSTALL"),
		/**
		 * 查核
		 */
		CHECK("CHECK"),
		/**
		 * 專案
		 */
		PROJECT("PROJECT"),
		/**
		 * 報修
		 */
		REPAIR("REPAIR"),
		/**
		 * 其他
		 */
		OTHER("OTHER");
		
		String code;
		CASE_CATEGORY(String paramTypeName) {
			code = paramTypeName;
		}
		public String getCode() {
			return this.code;
		}
	}
	/**
	 * Purpose: 交易類別
	 * @author evanliu
	 * @since  JDK 1.6
	 * @date   2016年12月12日
	 * @MaintenancePersonnel evanliu
	 */
	public static enum TRANSACTION_CATEGORY {
		/**
		 * 一般交易
		 */
		COMMON("COMMON"),
		/**
		 * 一般VM
		 */
		COMMON_VM("COMMON_VM"),
		/**
		 * 一般VMJ
		 */
		COMMON_VMJ("COMMON_VMJ"),
		/**
		 * 一般VMJU
		 */
		COMMON_VMJU("COMMON_VMJU"),
		/**
		 * 分期交易
		 */
		INSTALLMENT("Installment"),
		/**
		 * 紅利交易
		 */
		BONUS("BONUS"),
		/**
		 * AE
		 */
		AE("AE"),
		/**
		 * DINERS
		 */
		DINERS("DINERS"),
		/**
		 * DCC
		 */
		DCC("DCC"),
		/**
		 * CUP
		 */
		CUP("CUP"),
		/**
		 * MailOrder
		 */
		MAIL_ORDER("MailOrder"),
		/**
		 * SmartPay
		 */
		SMART_PAY("SmartPay"),
		/**
		 * ChoiceCard
		 */
		CHOICE_CARD("ChoiceCard"),
		/**
		 * 建設公司
		 */
		CONSTRUCTION_COMPANY("ConstructionCompany");
		
		
		String code;
		TRANSACTION_CATEGORY(String paramTypeName) {
			code = paramTypeName;
		}
		public String getCode() {
			return this.code;
		}
	}
	/**
	 * Purpose: 案件狀態
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2016/12/22
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum CASE_STATUS {
		/**
		 * 待派工
		 */
		WAIT_DISPATCH("WaitDispatch"),
		/**
		 * 已派工
		 */
		DISPATCHED("Dispatched"),
		/**
		 * 已回應
		 */
		RESPONSED("Responsed"),
		/**
		 * 延期中
		 */
		DELAYING("Delaying"),
		/**
		 * 已到場
		 */
		ARRIVED("Arrived"),
		/**
		 * 完修
		 */
		COMPLETED("Completed"),
		/**
		 * 待結案審查
		 */
		WAIT_CLOSE("WaitClose"),
		/**
		 * 結案
		 */
		CLOSED("Closed"),
		/**
		 * 立即結案
		 */
		IMMEDIATE_CLOSE("ImmediateClose"),
		/**
		 * 已作廢
		 */
		VOIDED("Voided");
		String code;
		CASE_STATUS(String paramTypeName) {
			code = paramTypeName;
		}
		public String getCode() {
			return this.code;
		}
	}
	
	/**
	 * Purpose: 案件動作
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2016/12/22
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum CASE_ACTION {
		/**
		 * 新增記錄
		 */
		ADD_RECORD("addRecord"),
		/**
		 * 派工
		 */
		DISPATCHING("dispatching"),
		/**
		 * 自動派工
		 */
		AUTO_DISPATCHING("autoDispatching"),
		/**
		 * 回應
		 */
		RESPONSE("response"),
		/**
		 * 到場
		 */
		ARRIVE("arrive"),
		/**
		 * 完修
		 */
		COMPLETE("complete"),
		/**
		 * 簽收
		 */
		SIGN("sign"),
		/**
		 * 線上排除
		 */
		ONLINE_EXCLUSION("onlineExclusion"),
		/**
		 * 退回
		 */
		RETREAT("retreat"),
		/**
		 * 延期
		 */
		DELAY("delay"),
		/**
		 * 催修
		 */
		RUSH_REPAIR("rushRepair"),
		/**
		 * 修改案件類型
		 */
		CHANGE_CASE_TYPE("changeCaseType"),
		/**
		 * 結案
		 */
		CLOSED("closed"),
		/**
		 * 作廢
		 */
		VOID_CASE("voidCase"),
		/**
		 * 立即結案
		 */
		IMMEDIATELY_CLOSING("immediatelyClosing"),
		/**
		 * 修改實際完修時間
		 */
		CHANGE_COMPLETE_DATE("changeCompleteDate"),
		/**
		 * 修改進件時間
		 */
		CHANGE_CREATE_DATE("changeCreateDate"),
		/**
		 * 配送中
		 */
		DISTRIBUTION("distribution"),
		/**
		 * 求償
		 */
		PAYMENT("payment"),
		/**
		 * 授權
		 */
		CONFIRM_AUTHORIZES("confirmAuthorizes"),
		/**
		 * 租賃預載
		 */
		LEASE_PRELOAD("leasePreload"),
		/**
		 * 租賃簽收
		 */
		LEASE_SIGN("leaseSign"),
		/**
		 * 租賃授權取消
		 */
		CANCEL_CONFIRM_AUTHORIZES("cancelConfirmAuthorizes"),
		/**
		 * 協調完成匯入
		 */
		UPLOAD_COORDINATED_COMPLETION("uploadCoordinatedCompletion"),
		/**
		 * 求償(若不是CyberEDC，到貨檢測 按鈕-->顯示為 求償)
		 */
		ARRIVAL_INSPECTION("arrivalInspection");
		
		String code;
		CASE_ACTION(String paramTypeName) {
			code = paramTypeName;
		}
		public String getCode() {
			return this.code;
		}
	}

	/**
	 * 
	 * Purpose: 案件處理角色名称
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2017/3/24
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum CASE_ROLE {
		/**
		 * 客服
		 */
		CUSTOMER_SERVICE("CUSTOMER_SERVICE"),
		/**
		 * CR #2951 廠商客服
		 */
		VENDOR_SERVICE("VENDOR_SERVICE"),
		/**
		 * 客戶
		 */
		CUSTOMER("CUSTOMER"),
		/**
		 * QA
		 */
		QA("QA"),
		/**
		 * TMS
		 */
		TMS("TMS"),
		/**
		 * 部門AGENT
		 */
		AGENT("AGENT"),
		/**
		 * 廠商AGENT
		 */
		VENDOR_AGENT("VENDOR_AGENT"),
		/**
		 * CYBER_AGENT
		 */
		CYBER_AGENT("CYBER_AGENT"),
		/**
		 * 工程師
		 */
		ENGINEER("ENGINEER"),
		/**
		 * cyber客服，同CUSTOMER_SERVICE
		 */
		CYBER_SERVICE("CYBER_SERVICE"),
		/**
		 * 客戶廠商客服
		 */
		CUS_VENDOR_SERVICE("CUS_VENDOR_SERVICE");
		
		/**
		 * CODE
		 */
		String code;
		
		/**
		 * Constructor: 構造函數
		 */
		CASE_ROLE(String paramTypeName) {
			code = paramTypeName;
		}
		public String getCode() {
			return this.code;
		}
	}
	/**
	 * Purpose: 求償動作
	 * @author CarrieDuan
	 * @since  JDK 1.7
	 * @date   2017/2/16
	 * @MaintenancePersonnel CarrieDuan
	 */
	public static enum PAY_ACTION {
		/**
		 * 送出
		 */
		SEND("SEND"),
		/**
		 * 鎖定
		 */
		LOCKING("LOCKING"),
		/**
		 * 還款
		 */
		RETURN("RETURN"),
		/**
		 * 完成
		 */
		COMPLETE("COMPLETE"),
		/**
		 * 修改
		 */
		UPDATE("UPDATE"),
		/**
		 * 退回
		 */
		BACK("BACK");
		
		
		String code;
		PAY_ACTION(String paramTypeName) {
			code = paramTypeName;
		}
		public String getCode() {
			return this.code;
		}
	}
	/**
	 * Purpose: 求償資訊
	 * @author CarrieDuan
	 * @since  JDK 1.7
	 * @date   2017/2/16
	 * @MaintenancePersonnel CarrieDuan
	 */
	public static enum PAYMENT_REASON {
		/**
		 * 遺失
		 */
		LOSS("LOSS"),
		/**
		 * 被竊
		 */
		STOLEN("STOLEN"),
		/**
		 * 損壞
		 */
		DAMAGE("DAMAGE"),
		/**
		 * 天災毀壞
		 */
		SCOURGE_DESTRUCTION("SCOURGE_DESTRUCTION"),
		/**
		 * 無法聯絡拆機
		 */
		UNABLE_TO_CONTACT("UNABLE_TO_CONTACT"),
		/**
		 * 設備維修
		 */
		ASSET_REPAIR("ASSET_REPAIR"),
		/**
		 * SELF_INPUT
		 */
		SELF_INPUT("SELF_INPUT");
		
		
		String code;
		PAYMENT_REASON(String paramTypeName) {
			code = paramTypeName;
		}
		public String getCode() {
			return this.code;
		}
	}
	/**
	 * Purpose: 權限按鈕
	 * @author amandawang
	 * @since  JDK 1.6
	 * @date   2016/12/22
	 * @MaintenancePersonnel amandawang
	 */
	public static enum FUNCTION_TYPE {
		/**
		 * 查詢
		 */
		QUERY("QUERY"),
		/**
		 * 新增
		 */
		ADD("ADD"),
		/**
		 * 修改
		 */
		EDIT("EDIT"),
		/**
		 * 刪除
		 */
		DELETE("DELETE"),
		/**
		 * 匯出
		 */
		EXPORT("EXPORT"),
		/**
		 * 明細
		 */
		DETAIL("DETAIL"),
		/**
		 * 存儲
		 */
		SAVE("SAVE"),
		/**
		 * 列印	
		 */
		PRINT("PRINT"),
		/**
		 * 裝機	
		 */
		INSTALL("INSTALL"),
		/**
		 * 倂機	
		 */
		MERGE("MERGE"),
		/**
		 * 異動	
		 */
		UPDATE("UPDATE"),
		/**
		 * 拆機	
		 */
		UNINSTALL("UNINSTALL"),
		/**
		 * 查核	
		 */
		CHECK("CHECK"),
		/**
		 * 專案	
		 */
		PROJECT("PROJECT"),
		/**
		 * 報修	
		 */
		REPAIR("REPAIR"),
		/**
		 * 案件匯入		
		 */
		CASE_IMPORT("CASEIMPORT"),
		/**
		 * 新增記錄		
		 */
		ADD_RECORD("ADDRECORD"),
		/**
		 * 派工		
		 */
		DISPATCH("DISPATCH"),
		/**
		 * 自動派工	
		 */
		AUTO_DISPATCH("AUTODISPATCH"),
		/**
		 * 顯示記錄		
		 */
		SHOW_DETAIL("SHOWDETAIL"),
		/**
		 * 回應			
		 */
		RESPONSED("RESPONSED"),
		/**
		 * 完修		
		 */
		COMPLETED("COMPLETED"),
		/**
		 * 簽收		
		 */
		SIGN("SIGN"),
		/**
		 * 線上排除		
		 */
		ONLINE_EXCLUSION("ONLINEEXCLUSION"),
		/**
		 * 延期		
		 */
		DELAY("DELAY"),
		/**
		 * 催修		
		 */
		RUSH_REPAIR("RUSHREPAIR"),
		/**
		 * 修改案件類型			
		 */
		CHANGE_CASE_TYPE("CHANGECASETYPE"),
		/**
		 * 結案審查			
		 */
		CLOSED("CLOSED"),
		/**
		 * 作廢				
		 */
		VOID_CASE("VOIDCASE"),
		/**
		 * 立即結案				
		 */
		IMMEDIATELY_CLOSING("IMMEDIATELYCLOSING"),
		/**
		 * 到場				
		 */
		ARRIVE("ARRIVE"),
		/**
		 * 送出			
		 */
		SEND("SEND"),
		/**
		 * 锁定		
		 */
		LOCK("LOCK"),
		/**
		 * 完成			
		 */
		COMPLETE("COMPLETE"),
		/**
		 * 還款				
		 */
		REPAY("REPAY"),
		/**
		 * 退回			
		 */
		BACK("BACK"),
		/**
		 * 領用
		 */
		CARRY("CARRY"),
		/**
		 * 借用
		 */
		BORROW("BORROW"),
		/**
		 * 歸還
		 */
		TOORIGINOWNER("TOORIGINOWNER"),
		/**
		 * 入庫
		 */
		ASSETIN("ASSETIN"),
		/**
		 * 領用
		 */
		REPAIR2("REPAIR2"),
		/**
		 * 送修
		 */
		SENDREPAIR("SENDREPAIR"),
		/**
		 * 待報廢
		 */
		PENDINGDISABLE("PENDINGDISABLE"),
		/**
		 * 報廢
		 */
		DISABLED("DISABLED"),
		/**
		 * 銷毀
		 */
		DESTROY("DESTROY"),
		/**
		 * 其他修改
		 */
		OTHEREDIT("OTHEREDIT"),
		/**
		 * 解除綁定
		 */
		UNBOUND("UNBOUND"),
		/**
		 * 台新租賃
		 */
		TAIXINRENT("TAIXINRENT"),
		/**
		 * 確認通知
		 */
		CONFIRMSEND("CONFIRMSEND"),
		/**
		 * 捷達威維護
		 */
		JDWMAINTENANCE("JDWMAINTENANCE"),
		/**
		 * 修改實際完修時間
		 */
		CHANGE_COMPLETE_DATE("CHANGECOMPLETEDATE"),
		/**
		 * 修改進件時間
		 */
		CHANGE_CREATE_DATE("CHANGECREATEDATE"),
		/**
		 * 租賃預載
		 */
		LEASE_PRELOAD("LEASEPRELOAD"),
		/**
		 * 租賃簽收
		 */
		LEASE_SIGN("LEASESIGN"),
		/**
		 * 配送中
		 */
		DISTRIBUTION("DISTRIBUTION"),
		/**
		 * 求償
		 */
		PAYMENT("PAYMENT"),
		/**
		 * 授權確認
		 */
		CONFIRMAUTHORIZES("CONFIRMAUTHORIZES"),
		/**
		 * 租賃授權取消
		 */
		CANCELCONFIRMAUTHORIZES("CANCELCONFIRMAUTHORIZES"),
		/**
		 * 協調完成匯入
		 */
		COORDINATEDIMPORT("COORDINATEDIMPORT"),
		;
		String code;
		FUNCTION_TYPE(String query) {
			code = query;
		}
		public String getCode() {
			return this.code;
		}
	}
	
	public static enum FUNCTION_CATEGORY {
		MENU(FunctionDTO.CATEGORY_MENU),
		BUTTION(FunctionDTO.CATEGORY_BUTTION),//button action功能/hyper like功能
		TASK(FunctionDTO.CATEGORY_TASK),//關卡功能
		SYSTEM(FunctionDTO.CATEGORY_SYSTEM),//系統
		FIELD(FunctionDTO.CATEGORY_FIELD),//資料欄項
		TAB(FunctionDTO.CATEGORY_TAB),//TAB標籤
		PARAMETER("I");//參數種類
		String code;
		FUNCTION_CATEGORY(String paramTypeName) {
			code = paramTypeName;
		}
		public String getCode() {
			return this.code;
		}
	}
	public static enum ACCESS_RIGHT {
		VIEW(IAccessControlPolicy.ACCESS_RIGHT_VIEW),  //顯示權限(view)　
		READ(IAccessControlPolicy.ACCESS_RIGHT_READ),  //讀取權限(read)
		WRITE(IAccessControlPolicy.ACCESS_RIGHT_WRITE),  //變更權限(write)
		EXECUTE(IAccessControlPolicy.ACCESS_RIGHT_EXECUTE),  //執行權限(execute)
		SCR(IAccessControlPolicy.ACCESS_RIGHT_PRINT_SCR),  //列印畫面權限(print screen)
		CONTROL(IAccessControlPolicy.ACCESS_RIGHT_FULL_CONTROL),  //完全控制權限(full control)
		SELECT(IAccessControlPolicy.ACCESS_RIGHT_SELECT),  //SELECT權限
		RIGHTCLICK(IAccessControlPolicy.ACCESS_RIGHT_MOUSE_RIGHT_CLICK),  //滑鼠右鍵權限(mouse right click)
		DRAG(IAccessControlPolicy.ACCESS_RIGHT_MOUSE_DRAG),  //滑鼠拖曳權限(mouse drag)
		COPY(IAccessControlPolicy.ACCESS_RIGHT_COPY),  //複製畫面權限(copy)
		CTRL(IAccessControlPolicy.ACCESS_RIGHT_CTRL_KEYS),  //CTRL鍵權限(CTRL keys)
		PRINT(IAccessControlPolicy.ACCESS_RIGHT_FILE_PRINT),  //檔案列印權限(file print)
		IMAGELOCK(IAccessControlPolicy.ACCESS_RIGHT_IMAGE_LOCK_UNLOCK),  // 影像鎖定解鎖定權限
		IMAGEDELETE(IAccessControlPolicy.ACCESS_RIGHT_IMAGE_DELETE);  // 影像刪除權限
		String code;
		ACCESS_RIGHT(String paramTypeName) {
			code = paramTypeName;
		}
		public String getCode() {
			return this.code;
		}
	}
	//==========================功能性常量设置 START=========================================================//
	public static final String ENCODE_UTF_8													= "UTF-8";
	public static final String ENCODE_GBK													= "GBK";
	public static final String ENCODE_GB_2312												= "gb2312";
	public static final String ENCODE_ISO8895_1												= "ISO8859-1";

	/**
	 * 上传文件做大限度
	 */
	public static final String PARAM_UPLOAD_FILE_SIZE										= "5000";
	//上傳文件大小轉換率
	public static final String PARAM_UPLOAD_CONVERSION_RATE									= "1000";
	//JRXML_PATH
	public static final String JRXML_PATH													= "/com/cybersoft4u/xian/iatoms/report/jrxml/";
	//JRXML_IMG_PATH
	public static final String POI_FILE_PATH												= "/com/cybersoft4u/xian/iatoms/file/";
		
	// 模板下载路径
	public static final String TEMPLATE_DOWNLOAD_PATH										= "/com/cybersoft4u/xian/iatoms/services/download/Importtemplate/";
	//定金单
	public static final String FILE_PATH_MONEY_FORM											= "E:\\";
	// 合约sla模板名称
	public static final String CONTRACT_SLA_TEMPLATE_NAME									= "合約sla範本.xls";
	//客戶特店模板名稱
	public static final String BIM_MERCHANT_TEMPLATE_NAME									= "客戶特店範本.xls";
	//設備轉倉模板名稱
	public static final String ASSET_TRANS_TEMPLATE_NAME									= "設備轉倉範本.xls";
	//設備庫存表
	public static final String ASSET_STOCK_TABLE_NAME										= "設備庫存表";
	//刷卡機參數表
	public static final String CARD_READER_PARAMETER										= "刷卡機參數表.xls";
	//案件處理範本
	public static final String CASE_TEMPLATE												= "案件處理.xls";
	
	//設備借用明細模板名稱
	public static final String BORROW_DETAIL_PROJECT_REPORT_JRXML_NAME						= "DMM_BORROW_DETAIL";
	//設備借用明細
	public static final String BORROW_DETAIL												= "借用明細";
	//批次匯出案件資料範本模板名稱
	public static final String CASE_TEMPLATE_PROJECT_REPORT_JRXML_NAME						= "SRM_CASE_TEMPLATE";
	//批次匯出案件資料範本
	public static final String EXPORT_CASE_TEMPLATE											= "案件資訊範本.xls";
	//倉管
	public static final String WAREHOUSE_MANAGER											= "倉管";
	//年
	public static final String YEAR															= "年";
	//月
	public static final String MONTH														= "月";
	// config文件设置的文件路径
	public static final String FILE_PATH													= "FILE_PATH";
	//臨時文件路徑
	public static final String FILE_TEMP_PATH												= "FILE_TEMP_PATH";
	//文件上傳的主路徑
	public static final String FILE_UPLOAD_PATH												= "UPLOAD_PATH";
	//批次處理目錄
	public static final String BATCH_FLAG_PATH												= "BATCH_FLAG_PATH";
	// 舊資料轉檔程式版本目錄
	public static final String FILE_TRANSFER_PATH											= "FILE_TRANSFER_PATH";
	// ftp配置
	public static final String FTP_CONFIG													= "FTP_CONFIG";
	// ftp url
	public static final String FTP_URL														= "FTP_URL";
	// ftp 帳號
	public static final String FTP_ACCOUNT													= "FTP_ACCOUNT";
	// ftp 密碼
	public static final String FTP_PWD														= "FTP_PWD";
	// ftp 文件路徑
	public static final String FTP_FILE_PATH												= "FTP_FILE_PATH";
	//parent URL
	public static final String MENU_TYPE_FOLDER												= "FOLDER";
	//child URL
	public static final String MENU_TYPE_MENU												= "MENU";
	//config文件设置
	public static final String SETTING														= "SETTING";
	//汇入上传最大限制
	public static final String UPLOAD_FILE_SIZE												= "UPLOAD_FILE_SIZE";
	public static final String PAGE_PARAM_RETURN_MSG_CODE									= "returnMessageCode";

	//案件資訊範本匯入路徑
	public static final String CASE_TEMPLATES_IMPORT_PATH									= "CASE_TEMPLATES_IMPORT_PATH";
	//案件資訊範本匯出路徑
	public static final String CASE_TEMPLATES_EXPORT_PATH									= "CASE_TEMPLATES_EXPORT_PATH";
	//在config 的派工資訊
	public static final String DISPATCH_INFORMATION											= "DISPATCH_INFORMATION";
	//派工廠商ID
	public static final String DISPATCH_COMPANY_ID											= "COMPANY_ID";
	//派工部門ID
	public static final String DISPATCH_DEPT_ID												= "DISPATCH_DEPT_ID";

	/**
	 * 刷卡機標籤列印QRCODE URL
	 */
	public static final String QRCODE_URL													= "QRCODE_URL";
	
	//基礎參數設定生效日期爲固定值
	public static final String STATIC_VERSION_DATE_FOR_BASE_PARAMETER						= "2016/08/08";
	//ajax請求的header
	public static final String REQUEST_AJAX_HEADER                                          = "x-requested-with";
	//ajax請求對象名稱
	public static final String REQUEST_AJAX_OBJECT_NAME                                     = "XMLHttpRequest";
	//匯入錯誤文檔名稱
	public static final String UPLOAD_ERROR_MESSAGE_FILE_NAME								= "匯入錯誤檔.txt";
	//設備
	public static final String ASSET														= "設備";
	// config預設的cyber用戶
	public static final String CYBER_AUTHENTICATION											= "CYBER_AUTHENTICATION";
	// config預設的cyber用戶帳號
	public static final String ACCOUNT														= "ACCOUNT";
	// config預設的cyber用戶密碼
	public static final String PWD															= "PASSWORD";
	//數據類型-boolean
	public static final String DATA_TYPE_BOOLEAN											= "boolean";
	//數據類型-文本
	public static final String DATA_TYPE_TEXT												= "text";
	//數據類型-數字
	public static final String DTAT_TYPE_NUMBER												= "number";
	// activiti案件類型標記
	public static final String ACTIVITI_CASE_TYPE											= "C";
	// 當前activitiCode--待派工
	public static final String ACTIVITI_CURRENT_ACTIVITI_CODE_WAIT_DISPATCH					= "WAIT_DISPATCH";
	// 當前activitiCode--已派工
		public static final String ACTIVITI_CURRENT_ACTIVITI_CODE_DISPATCH					= "DISPATCHED";
	//當前activitiCode -- 待結案審查
	public static final String ACTIVITI_CURRENT_ACTIVITI_CODE_WAIT_CLOSE					= "WAIT_CLOSE";
	//當前activitiCode -- 結案
	public static final String ACTIVITI_CURRENT_ACTIVITI_CODE_CASE_PROCESS_END				= "CASE_PROCESS_END";
	//下一關卡名稱 - END
	public static final String ACTIVITI_CURRENT_ACTIVITI_NAME_END							= "END";
	// 上班日非當天件時間起時刻
	public static final String PARAM_WORK_TIME_START_HOUR									= "00:00";
	// 上班日非當天件時間迄時刻
	public static final String PARAM_WORK_TIME_END_HOUR										= "24:00";
	//是
	public static final String PARAM_YES_CH													= "YES";
	//否
	public static final String PARAM_NO_CH													= "NO";
	// 催修案件
	public static final String PARAM_RUSH_REPAIR_CASE_TYPE									= "催修";
	// 回應狀態
	public static final String PARAM_RESPONSE_CASE_STATUS									= "response";
	// 到場狀態
	public static final String PARAM_ARRIVE_CASE_STATUS										= "arrive";
	// 完修狀態
	public static final String PARAM_COMPLETE_CASE_STATUS									= "complete";
	// 網絡線
	public static final String PARAM_NET_WORK_LINE											= "NetworkRoute";
	// ECR連線
	public static final String PARAM_ECR_LINE												= "ECRLINE";
	// 8位數字不足補0
	public static final String PARAM_SUPPLEMENT_ZERO_FOR_NUMBER_EIGHT						= "%08d";
	// HH:mm格式
	public static final String PARAM_HOUR_MINUTE_DATE_FORMAT								= "HH:mm";
	// 借用通知(倉管) 參數類別bptdCode
	public static final String PARAM_BORROW_ADVICE_CODE										= "BORROW_ADVICE";
	// 案件轉移 參數代碼itemValue
	public static final String PARAM_CASE_TRANSFER_VALUE									= "CASE_TRANSFER_MONTH";
	// 系統LOG轉移 參數代碼itemValue
	public static final String PARAM_SYSTEM_LOG_VALUE										= "SYSTEM_LOG_MONTH";
	// 密碼歷史清除 參數代碼itemValue
	public static final String PARAM_PWD_HISTORY_MONTH										= "PWD_HISTORY_MONTH";
	// 系統LOG歷史清除 參數代碼itemValue
	public static final String PARAM_LOG_HISTORY_MONTH										= "LOG_HISTORY_MONTH";
	// 庫存歷史月檔清除 參數代碼itemValue
	public static final String PARAM_REPO_MONTHLY_HIS_MONTH									= "REPO_MONTHLY_HIS_MONTH";
	// 入庫資料清除 參數代碼itemValue
	public static final String PARAM_ASSET_IN_MONTH											= "ASSET_IN_MONTH";
	// 轉倉資料清除 參數代碼itemValue
	public static final String PARAM_ASSET_TRANS_MONTH										= "ASSET_TRANS_MONTH";
	// 系統備份或刪除期限 參數類別bptdCode
	public static final String PARAM_SYSTEM_LIMIT_CODE										= "SYSTEM_LIMIT";
	
	// 2位數字不足補0
	public static final String PARAM_SUPPLEMENT_ZERO_FOR_NUMBER_TWO							= "%02d";
	// 忽略刪除標記
	public static final String PARAM_IGNORE_DELETED											= "ignoreDeleted";
	
	// 系統名稱配置
	public static final String PARAM_SYSTEM_NAME_CODE										= "systemName";
	
	// TMS參數匯入API連線字串配置
	public static final String TMS_URL														= "TMSURL";
	
	// 案件地址等預設值
	public static final String PARAM_DEFAULT_CASE_MERCHANT_INFO								= "E";
	//==============================ACCESS_RIGHT(权限名称) START===============================================//
	public static final String ACCESS_RIGHT													= "ACCESS_RIGHT";//按钮权限
	public static final String ACCESS_RIGHT_RETRIEVE										= "RETRIEVE";//查询
	public static final String ACCESS_RIGHT_CREATE											= "CREATE";	//新增
	public static final String ACCESS_RIGHT_SEND											= "SEND";	//送审
	public static final String ACCESS_RIGHT_EXPORT											= "EXPORT"; //汇出
	public static final String ACCESS_RIGHT_IMPORT											= "IMPORT"; //汇入
	public static final String ACCESS_RIGHT_UPDATE											= "UPDATE"; //编辑
	public static final String ACCESS_RIGHT_DELETE											= "DELETE"; //删除
	public static final String ACCESS_RIGHT_VIEW											= "VIEW"; //检视
	
	//================== service id  ex:SERVICE_(SERVICE名称) START=============================================//	
	//系统参数维护
	public static final String SERVICE_BASE_PARAMETER_SERVICE								= "baseParameterService";
	public static final String SERVICE_APLOG_SERVICE										= "apLogService";
	public static final String SERVICE_EMPLOYEE_SERVICE                     				= "employeeService";
	
	//使用者維護
	public static final String SERVICE_ADM_USER_SERVICE                                     = "admUserService";
	//使用者角色維護
	public static final String SERVICE_ADM_ROLE_SERVICE                                     = "admRoleService";
	//使用者部門維護
	public static final String SERVICE_BIM_DEPARTMENT_SERVICE                     			= "departmentService";
	//公司維護
	public static final String SERVICE_VENDOR_SERVICE                                       = "vendorService";
	//系統參數維護
	public static final String SERVICE_SYSTEM_ITEM_SERVICE                                  = "systemItemService";
	//合約維護SERVICE
	public static final String PARAM_CONTRACT_MANAGE_SERVICE                                = "contractService";
	//客戶特店維護
	public static final String SERVICE_MERCHANT_SERVICE                          			= "merchantNewService";
	//特店表頭維護
	public static final String SERVICE_MERCHANT_HEADER_SERVICE                          	= "merchantHeaderService";
	//密碼原則設定
	public static final String SERVICE_PWD_SETTING_SERVICE									= "passwordSettingService";
	//系统参数维护
	public static final String SERVICE_BASE_PARAMETER_MANAGER_SERVICE						= "baseParameterManagerService";
	//系統日志查詢
	public static final String SERVICE_SYSTEM_LOG_SERVICE									= "systemLogService";
	//設備入庫
	public static final String SERVICE_ASSET_IN_SERVICE										= "assetInService";
	//合約SLA設定
	public static final String SERVICE_CONTRACT_SLA_SERVICE									= "contractSlaService";
	//電子郵件羣組維護
	public static final String SERVICE_MAIL_LIST_SERVICE									= "mailListManageService";
	//耗材品项维护
	public static final String SUPPLIES_MAINTENANCE_SERVICE									= "suppliesMaintenanceService";
	//設備品項維護
	public static final String SERVICE_ASSET_TYPE_SERVICE									= "assetTypeService";
	//設備轉倉作業
	public static final String SERVICE_DMM_ASSET_TRANS_INFO_SERVICE							= "assetTransferService";
	//設備盘点
	public static final String SERVICE_DMM_ASSET_STACKTAKE_SERVICE							= "assetStacktakeService";
	//轉倉歷史查詢
	public static final String SERVICE_DMM_ASSET_TRANS_INFO_HISTORY_SERVICE					= "assetTransInfoHistoryService";
	//程式版本維護
	public static final String SERVICE_APPLICATION_SERVICE									= "applicationService";
	//設備轉倉作業--轉倉確認
	public static final String SERVICE_CHECK_TRANS_INFO_SERVICE								= "checkTransInfoService";
	//設備歷史記錄查詢
	public static final String SERVICE_REPO_HISTORY_SERVICE									= "repoHistoryService";
	//設備盤點歷史查詢
	public static final String SERVICE_DMM_ASSET_STOCKTACK_HISTORY_SERVICE					= "assetStocktakeHistoryService";
	//設備狀態報表
	public static final String SERVICE_REPOS_STATUS_REPORT_SERVICE							= "repoStatusReportService";
	//庫存主檔
	public static final String SERVICE_REPOSITORY_SERVICE									="assetManageService";
	public static final String SERVICE_CUSTOMER_REPO_SERVICE								= "customerRepoService";
	//案件處理--建案
	public static final String SERVICE_TICKET_SERVICE										= "ticketService";
	//報表發送功能設定
	public static final String SERVICE_REPORT_SETTING_SERVICE								= "reportSettingService";
	//行事曆
	public static final String SERVICE_BIM_CALENDAR_SERVICE		         					="calendarService";
	public static final String SERVICE_COMPANY_SERVICE										= "companyService";
	// 仓库据点
	public static final String SERVICE_WAREHOUSE_SERVICE									= "warehouseService";
	//DTID號碼管理
	public static final String SERVICE_DTID_DEF_SERVICE										= "dtidDefService";
	//案件處理
	public static final String SERVICE_CASE_MANAGER_SERVICE									= "caseManagerService";
	//求償作業
	public static final String SERVICE_PAYMENT_SERVICE										= "paymentService";
	//耗材品維護
	public static final String SERVICE_SUPPLIES_TYPE_SERVICE								= "suppliesTypeService";
	//工單範本維護
	public static final String SERVICE_SRM_CASE_TEMPLATES_SERVICE							= "srmCaseTemplatesService";
	
	//用戶欄位模板維護檔
	public static final String SERVICE_SRM_QUERY_TEMPLATE_SERVICE							= "srmQueryTemplateService";
	
	//客訴管理
	public static final String SERVICE_COMPLAINT_SERVICE									= "ComplaintService";
	
	//刷卡機標籤管理
	public static final String SERVICE_EDC_LABEL_SERVICE									= "edcLabelService";
	
	//
	public static final String SERVICE_API_AUTHORIZATION_INFO_SERVICE						= "apiAuthorizationInfoService";
	//案件商業性邏輯核檢service
	public static final String SERVICE_CASE_CHECK_INFO_SERVICE								= "caseCheckInfoService";
	
	//錯誤記錄檔下載
	public static final String SERVICE_LOG_FILE_DOWNLOAD									= "logFileDownloadService";
	
	//設備借用service
	public static final String SERVICE_ASSET_BORROW_SERVICE									= "assetBorrowService";
	//================== action id  ex:ACTION_(ACTION名称)=======================================================//	
	public static final String ACTION_CANCEL												= "cancel";
	public static final String ACTION_LOG													= "log";
	public static final String ACTION_GET_EMPLOYEE_LIST										= "getEmployeeList";
	public static final String ACTION_GET_NORMAL_USER_EMAIL_LIST							= "getNormalUserEmailList";
	public static final String ACTION_GET_PROJECT_EMPLOYEE_LIST								= "getProjectEmployeeList";
	public static final String ACTION_GET_EMPLOYEED_LIST									= "getEmployeedList";
	public static final String ACTION_INIT_EDIT_YEAR_CALENDAR								= "initEditYearCalendar";
	public static final String ACTION_GET_EMPLOYEE_SKILL_LIST								= "getEmployeeSkillId";
	public static final String ACTION_GET_CARD_EXPERIENCE									= "getCardExp";
	public static final String ACTION_SETTING_ACTIVE										= "settingActive";
	public static final String ACTION_SETTING_ACTIVE_FINISH									= "settingActiveFinish";
	public static final String ACTION_GET_PARAMETER_ITEMS									= "getParameterItems";
	public static final String ACTION_SAVE_FILE												= "saveFile";
	/**
	 * 根據父類別查找子列表
	 */
	public static final String ACTION_GET_PARAMETER_ITEMS_BY_PARENT							= "getParametersByParent";
	public static final String ACTION_GET_PARAMETER_ITEMS_BY_TEXT_FIELD							= "getParametersByTextField";
	// 保存系統log
	public static final String ACTION_SAVE_SYSTEM_LOG										= "saveSystemLog";
	//獲取入庫批號列表
	public static final String ACTION_GET_ASSET_IN_ID_LIST									= "getAssetInIdList";
	//獲取倉庫列表
	public static final String ACTION_GET_WAREHOUSE_LIST									= "getWarehouseList";
	public static final String ACTION_LIST_ASSET_IN_LIST									= "listAssetInList";
	//獲取待轉倉列表
	public static final String ACTION_GET_ASSET_TRANS_LIST									= "getAssetTransList";
	//確認轉倉
	public static final String ACTION_TRANSFER_WAREHOUSE									= "transferWarehouse";
	//確認轉倉
	public static final String ACTION_CANCLE_TRANSFER_WAREHOUSE								= "cancleTransferWarehouse";
	//刪除轉倉單明細
	public static final String ACTION_DELETE_ASSET_TRANS_LIST								= "deleteAssetTransList";
	//獲得轉倉主檔資料
	public static final String ACTION_GET_ASSET_TRANS_INFO_DTO_BY_ASSET_TRANS_ID			= "getAssetTransInfoDTOByAssetTransId";
	public static final String ACTION_GET_ASSET_INFO_LIST									= "getAssetInfoList";
	public static final String ACTION_GET_ASSET_INFO_BY_ID									= "getAssetTransInfoById";
	//獲取合約編號
	public static final String ACTION_GET_CONTRACT_LIST										= "getContractList";
	//由合約ID獲取刷卡機設備或周邊設備
	public static final String ACTION_GET_ASSET_TYPE_ID_LIST								= "getAssetTypeIdList";
	//獲取合約編號
	public static final String ACTION_GET_ASSET_IN_INFO_BY_CONTRACT_ID						= "getAssetInInfoDTOByContractId";
	public static final String ACTION_GET_ASSET_IN_INFO_DTO_BY_ASSET_IN_ID					= "getAssetInfoDTOByAssetInId";
	public static final String ACTION_ADD_ASSET_IN_LIST										= "addAssetInList";
	public static final String ACTION_DELETE_ASSET_IN_LIST									= "deleteAssetInList";
	public static final String ACTION_ACTUAL_ACCEPTANCE										= "actualAcceptance";
	public static final String ACTION_CUSTOMES_ACCEPTANCE									= "customesAcceptance";
	public static final String ACTION_FINISH_ACCEPTANCE										= "finishAcceptance";
	public static final String ACTION_UPLOAD												= "upload";
	public static final String ACTION_DOWNLOAD												= "download";
	public static final String ACTION_DOWNLOAD_ZIP												="downloadZip";
	public static final String ACTION_DOWNLOAD_ERROR_FILE									= "downloadErrorFile";
	public static final String ACTION_STORAGE												= "storage";
	public static final String ACTION_GET_CONTRACT_ID_LIST									= "getContractIdList";
	public static final String ACTION_GET_LIST_BY_CONTRACT_ID								= "getListByContractId";
	//通過公司ID獲取唯一編號
	public static final String ACTION_GET_UNITYNAME_BY_COMPANYID							= "getUnityNameByCompanyId";
	//獲取盤點批號List
	public static final String ACTION_GET_INVENTORY_NUMBER_LIST								= "getInventoryNumberList";
	//根據盤點批號獲取盤點信息
	public static final String ACTION_GET_ASSET_STOCKTACK_INFO_BY_STOCKTACK_ID				= "getAssetStocktackInfoByStocktackId";
	//獲取合約編號
	public static final String ACTION_LIST_CONTRACT_ID										= "listContractId";
	//獲取所有的列表
	public static final String ACTION_LIST_ALL												= "listAll";
	//獲取角色列表
	public static final String ACTION_GET_ROLE_LIST											= "getRoleList";
	//獲取公司列表
	public static final String ACTION_GET_COMPANY_LIST										= "getCompanyList";
	//獲取廠商列表
	public static final String ACTION_GET_VENDOR_LIST										= "getVendorList";
	//根據用戶編號查找信息
	public static final String ACTION_GET_ROLE_LIST_BY_USER_ID								= "getRoleListByUserId";
	//根據用戶編號查找部門信息
	public static final String ACTION_GET_DEPT_LIST											= "getDeptList";
	//根據部門查找用戶編號
	public static final String ACTION_GET_USER_BY_DEPT										= "getUserByDept";
	//獲取角色代號列表
	public static final String ACTION_GET_ROLE_CODE											= "getRoleCode";
	//獲取系統角色屬性列表
	public static final String ACTION_GET_ROLE_ATTRIBUTE									= "getRoleAttribute";
	//獲取表單角色列表
	public static final String ACTION_GET_WORK_FLOW_ROLE									= "getWorkFlowRoleList";
	//檢測角色是否被人員引用
	public static final String ACTION_CHECK_USE_ROLE										= "checkUseRole";
	//獲取父功能列表
	public static final String ACTION_GET_PARENT_FUNCTION									= "getParentFunction";
	//獲取子功能列表
	public static final String ACTION_GET_FUNCTION_BY_PARENT_ID								= "getFunctionByParentId";
	//獲取相同顧客編號下報表列表
	public static final String ACTION_GET_PRE_REPORT_CODE_LIST								= "getPreReportCodeList";
	//獲取設備類型對應的設備列表
	public static final String ACTION_GET_ASSET_TYPE_LIST									= "getAssetTypeList";
	//根據客戶拿到相應的設備類別
	public static final String ACTION_GET_ASSET_TYPE_BY_CUSTOMER							= "getAssetListTypeByCustomer";
	//由特店表頭ID獲取特店
	public static final String ACTION_GET_MERCHANT_HEADER									= "getMerchantHeader";
	//保存臨時文件路徑
	public static final String ACTION_SAVE_TEMP_FILE										= "saveTempFile";
	public static final String ACTION_GET_FILE_PATH											= "getFilePath";
	//設備轉倉批號列表
	public static final String ACTION_GET_ASSET_TRANS_ID_LIST								= "getAssetTransIdList";
	//倉管姓名列表下拉框
	public static final String ACTION_GET_WARE_HOUSE_USER_NAME_LIST							= "getWareHouseUserNameList";
	//設備盤點批號列表
	public static final String ACTION_GET_STOCKTACK_ID_LIST									= "getStocktackIdList";
		
	public static final String ACTION_GET_ASSET_TRANS_IDS									= "getAssetTransIds";

	public static final String ACTION_GET_COMPANY_ID_LIST									= "getCompanyIdList";
	//獲取部門列表
	public static final String ACTION_GET_CDEPARTMENT_LIST									= "getDepartmentList";
	public static final String ACTION_GET_CDEPARTMENTS										= "getDepartments";

	//建案--派工
	public static final String ACTION_NEW_DISPATCH											= "dispatch";
	//獲取區域列表
	public static final String ACTION_GET_LOCATION_LIST										= "getLocationList";
	//使用者狀態列表
	public static final String ACTION_GET_ACCOUNT_STATUS_LIST								= "getAccountStatusList";
	//檢查
	public static final String ACTION_CHECK													= "check";
	//檢查
	public static final String ACTION_CHECK_ASSET_DATA										= "checkAssetData";
	//查詢角色
	public static final String ACTION_GET_ROLES												= "getRoles";
	//檢查DTID是否重讀
	public static final String ACTION_CHECK_DTID											= "checkDtid";
	//map key
	public static final String ADDRESS														= "address";
	//聯繫地址
	public static final String CONTACT_ADDRESS												= "contactAddress";
	//聯繫地址
	public static final String PARAM_CONTACT_ADDRESS										= "CONTACT_ADDRESS";

	//新密碼驗證, 新密碼不能和前幾次密碼相同
	public static final String ACTION_CHECK_PWD_HISTORY										= "checkNewPasswordHis";
	//獲取客戶列表
	public static final String ACTION_GET_CUSTOMER_INFO_LIST								= "getCustomerInfoList";
	//獲取客戶特店信息
	public static final String ACTION_GET_MERCHANT_INFO										= "getMerchantInfo";
	//獲取郵遞區號信息
	public static final String ACTION_GET_POST_CODE_LIST									= "getPostCodeList";
	//获取参数类型列表
	public static final String ACTION_LIST_PARAMETER_TYPES									= "listParameterTypes";
	//獲取廠商信息下拉框初始化資料
	public static final String ACTION_GET_COMPANY_PARAMETER_LIST							= "getCompanyParameterList";
	//獲取庫存歷史檔資料
	public static final String ACTION_GET_REPOSITORY_BY_HIST_ID								= "getRepositoryHistDTOByHistId";
	//獲取庫存歷史檔資料
	public static final String ACTION_GET_EMAIL_BY_USER_ID									= "getEmailByUserId";
	public static final String ACTION_GET_ASSET_TRANS_INFO_BY_KEY_ID						= "getAssetTransInfoByKeyId";
	//獲取交易參數列表
	public static final String ACTION_GET_TRADING_PARAMETERS								= "getTradingParameters";
	//獲取設備名稱列表
	public static final String ACTION_GET_ASSET_NAME_LIST									= "getAssetNameList";
	//獲取報表明細列表
	public static final String ACTION_GET_REPORT_DETAIL_LIST_BY_ID							= "getReportDetailListById";
	//查詢交易參數
	public static final String ACTION_GET_TRANSACTION_PARAMS								= "getTransactionParams";
	//核檢當前資料是否存在
	public static final String ACTION_CHECK_CASE_ATT_FILE									= "checkCaseAttFile";
	//獲取耗材品項維護名稱下拉列表
	public static final String ACTION_GET_SUPPLIES_TYPE_NAME_LIST							= "getSuppliesTypeNameList";
	//送出等操作時根據求償編號獲取求償列表信息
	public static final String ACTION_GET_PAYMENT_ITEM_BY_PAYMENT_ID						= "getPaymentItemByItemIds";
	//根據caseID獲取案件歷史記錄
	public static final String ACTION_GET_CASE_TRANSACTIONDTO_BY_CASE_ID					= "getCaseTransactionDTOByCaseId";
	//根據caseID獲取案件交易類別
	public static final String ACTION_GET_CASE_TRANSACTION_PARAMETER_BY_CASEID				= "getCaseTransactionParameterByCaseId";
	//獲取上傳的範本的路徑根據範本的id
	public static final String ACTION_GET_CATEGORY_BY_TEMPLATES_ID							= "getCategoryByTemplatesId";
	//獲取需要列印的行資料
	public static final String ACTION_GET_CASE_MANAGER_LIST_BY_CASE_ID						= "getCaseManagerListByCaseId";
	//獲取用戶欄位模板集合
	public static final String ACTION_GET_USER_COLUMN_TEMPLATE_LIST							= "getUserColumnTemplateList";
	//獲取用戶當前欄位模板
	public static final String ACTION_GET_CURRENT_COLUMN_TEMPLATE							= "getCurrentColumnTemplate";
	//獲取需用戶欄位模板
	public static final String ACTION_GET_USER_COLUMN_TEMPLATE								= "getUserColumnTemplate";
	
	//獲取客訴管理附加檔案路徑
	public static final String ACTION_GET_COMPLAINT_FILE_PATH								= "getComplaintFilePath";
	public static final String ACTION_GET_BORROW_CASE_ID									= "getBorrowCaseId";
	public static final String PARAM_ASSET_BORROW_CASE_ID									= "assetBorrowCaseId";
	//公司列表
	public static final String PARAM_LIST_BY												= "listby";
	//公司列表
	public static final String PARAM_COMPANY_LIST											= "companyList";
	//合約列表
	public static final String PARAM_CONTRACT_LIST											= "contractList";
	//區域列表
	public static final String PARAM_LOCATION_LIST											= "locationList";
	//獲取客戶列表
	public static final String PARAM_METHOD_GET_CUSTOMER_LIST 								= "getCustomerList";
	//獲得設備列表
	public static final String PARAM_METHOD_GET_ASSET_LIST 									= "getAssetList";
	//設備名稱
	public static final String PARAM_ASSET_ANME_LIST 										= "assetNameList";
	//
	public static final String PARAM_CUSTOMER_LIST 											= "customerList";
	public static final String PARAM_REPORT_DETAIL_LIST										= "reportDetailList";
	public static final String PARAM_CUSTOMER_GRID_LIST 									= "customerGridList";
	//部門列表
	public static final String PARAM_DEPARTMENT_LIST 										= "departmentList";
	//經貿聯網部門列表
	public static final String PARAM_DEPARTMENT_CYB_LIST									="departmentCybList";

	//耗材列表
	public static final String PARAM_SUPPLIES_LIST 											= "suppliesList";
	public static final String PARAM_SUPPLIES_GRID_LIST 									= "suppliesGridList";
	public static final String PARAM_GET_SUPPLIES_LIST 										= "getSuppliesList";
	public static final String PARAM_GET_SUPPLIES_LIST_BY_CUSTOMERID 						= "getSuppliesListByCustomseId";
	public static final String PARAM_GET_SUPPLIES_NAME_LIST									= "getSuppliesNameList";
	//範本ID
	public static final String PARAM_GET_UPLOAD_TEMPLATES_ID								= "getUploadTemplatesId";
	
	//獲取設備狀態列表
	public static final String PARAM_ASSET_STATUS 											= "ASSET_STATUS";
	//獲取json集合
	public static final String PARAM_CUSTOMER_JSON											= "customerJson";
	public static final String PARAM_SUPPLIES_JSON											= "suppliesJson";
	//倉管姓名列表	
	public static final String PARAM_WARE_HOUSE_USER_NAME_LIST								= "wareHouseUserNameList";
	// 周邊設備列表
	public static final String PARAM_PERIPHERALS_LIST										= "peripheralsList";
	// EDC列表
	public static final String PARAM_EDC_TYPE_LIST											= "edcTypeList";
	//獲取設備名稱列表	
	public static final String PARAM_ASSET_LIST 											= "getAssetTypeList";
	//獲取廠商列表
	public static final String PARAM_METHOD_GET_MANU_FACTURER_LIST 							= "getManuFacturerList";
	//
	public static final String GET_HEADER_DTO_BY_MERCHANTCODE								= "getHeaderDTOByMerchantCode";
	//廠商名稱	
	public static final String PARAM_MANU_FACTURER_LIST 									= "manuFacturerList";
	
	public static final String PARAM_MANU_FACTURER_VALUE_LIST								= "manuFacturerValueList";
	//交易參數項目列表
	public static final String PARAM_TRANSACTION_PARAMETER_ITEM_LIST 						= "transactionParameterItemList";
	//交易參數對應的可編輯列集合
	public static final String PARAM_TRANSACTION_PARAMETER_EDIT_FIELDS_MAP 					= "transactionParameterEditFieldsMap";
	//角色管理-功能權限
	public static final String PARAM_FUNCTION_PERMISSION_MAP								= "functionPermissionMap";
	
	public static final String PARAM_AI														= "AI";
	// 維護廠商列表
	public static final String PARAM_VENDOR_LIST											= "vendorList";
	// 所有公司列表
	public static final String PARAM_ALL_COMPANY_LIST										= "allCompanyList";
	// mailgroup列表
	public static final String PARAM_MAIL_GROUP_LIST										= "mailGroupList";
	// mailgroup列表
	public static final String PARAM_EMP_MAIL_LIST											= "empMailList";
	// 查核結果列表
	public static final String CHECK_RESULTS_LIST_STR										= "checkResultsListStr";
	// 問題原因列表
	public static final String PROBLEM_REASON_LIST_STR										= "problemReasonListStr";
	// 問題原因列表(台新)
	public static final String PROBLEM_REASON_TSB_LIST_STR									= "problemReasonTsbListStr";
	//  問題解決方式列表
	public static final String PROBLEM_SOLUTION_LIST_STR									= "problemSolutionListStr";
	//  問題解決方式列表(台新)
	public static final String PROBLEM_SOLUTION_TSB_LIST_STR								= "problemSolutionTsbListStr";
	// 責任歸屬列表
	public static final String RESPONSIBITY_LIST_STR										= "responsibityListStr";
	// 責任歸屬列表
	public static final String LOGISTICS_VENDOR_LIST_STR									= "logisticsVendorStr";
	// 條件限制列表
	public static final String PARAM_CONDITION_OPERATOR_LIST								= "conditionOperatorList";
	// 合約SLA使用
	public static final String PARAM_SLA_USE												= "SLA_USE";
	public static final String PARAM_PAY_LOAD												= "payLoad";
	//合約列表
	public static final String CONTRACT_ASSET_LIST											= "listContractAsset";
	public static final String CONTRACT_TYPE 												= "CONTRACT_TYPE";
	public static final String CONTRACT_STATUS 												= "CONTRACT_STATUS";
	public static final String MAIL_GROUP 													= "MAIL_GROUP";
	public static final String PAY_MODE 													= "PAY_MODE";
	public static final String TICKET_MODE 													= "TICKET_MODE";
	public static final String COMM_MODE													= "COMM_MODE";
	public static final String AREA															= "AREA";
	public static final String TICKET_TYPE 													= "TICKET_TYPE";
	public static final String PRIORITY 													= "PRIORITY";
	public static final String TRUE_OR_FALSE 												= "TRUE_OR_FALSE";
	public static final String PARAM_MA_TYPE 												= "MA_TYPE";
	//行事曆
	public static final String DAY_SUNDAY   												= "SUNDAY";
	public static final String DAY_MONDAY       											= "MONDAY";
	public static final String DAY_TUESDAY  												= "TUESDAY";
	public static final String DAY_WEDNESDAY 												= "WEDNESDAY";
	public static final String DAY_THURSDAY 												= "THURSDAY";
	public static final String DAY_FRIDAY   												= "FRIDAY";
	public static final String DAY_STAUDAY  												= "SATURDAY";
	// 內建
	public static final String PARAM_BUILT_IN  												= "BUILT_IN";
	// 外接
	public static final String PARAM_EXTERNAL  												= "EXTERNAL";
	// 根据公司code獲取公司資料
	public static final String ACTION_GET_COMPANY_BY_COMPANY_CODE 							= "getCompanyByCompanyCode";
	// 根据公司code獲取公司資料
	public static final String ACTION_GET_COMPANY_BY_COMPANY_ID 							= "getCompanyByCompanyId";
	// 根据公司联动部门
	public static final String ACTION_GET_DEPT_BY_COMPANY_ID 								= "getDeptByCompanyId";
	// 根据公司编号判断登入验证方式是否为iAtoms驗證
	public static final String ACTION_IS_AUTHENTICATION_TYPE_EQUALS_IATOMS 					= "isAuthenticationTypeEqualsIAtoms";
	//根據公司ID判斷DTID生成方式是否為同TID
	public static final String ACTION_IS_DTID_TYPE											= "isDtidType";
	//核檢DTID號碼區間是否已被使用
	public static final String IS_USE_DTID													= "isUseDtid";
	//檢核改筆案件 是否有簽收或者線上排除記錄
	public static final String IS_SIGN_AND_ONLINE_EXCLUSION									= "isSignAndOnlineExclusion";
	//檢核該案件資料是否被通dtid的案件修改過
	public static final String GET_CASELINK_IS_CHANGE										= "getCaseLinkIsChange";
	// 根據客戶和和與狀態得到合約列表
	public static final String ACTION_GET_CONTRACT_BY_CUSTOMER_AND_STATUS 					= "getContractByCustomerAndStatus";
	public static final String ACTION_GET_CONTRACT_CODE_LIST 								= "getContractCodeList";
	// 獲取有EDC的合約
	public static final String ACTION_GET_HAVE_EDC_CONTRACT 								= "getHaveEdcContract";
	// 根據選擇的客戶獲取有SLA信息的合同
	public static final String ACTION_GET_CONTRACT_ID_FROM_SLA 								= "getContractIdFromSla";	
	//獲取有表頭的特店列表
	public static final String ACTION_GET_MERCHANT_LIST										= "getMerchantList";
	//獲取有庫存的設備列表
	public static final String ACTION_IS_ASSET_LIST											= "isAssetList";
	//獲取有sla信息顧客的列表
	public static final String ACTION_GET_SLA_COMPANY_LIST									= "getSlaCompanyList";
	//有sla信息顧客的列表
	public static final String PARAM_SLA_COMPANY_LIST										= "slaCompanyList";
	// 更新用戶狀態
	public static final String ACTION_UPDATE_USER_STATUS									= "updateUserStatus";
	//獲取合約編號的客戶以及廠商保固日期
	public static final String ACTION_GET_CONTRACT_WARRANTY									= "getContractWarranty";
	
	//獲取設備型號
	public static final String GET_ASSET_MODEL_LIST											= "getAssetModelList";
	//獲取設備廠牌
	public static final String GET_ASSET_BRAND_LIST											= "getAssetBrandList";
	//獲取特點表頭信息
	public static final String GET_MERCHANT_HEADER_BY_ID									= "getMerchantHeaderById";
	//獲取特店信息
	public static final String GET_MERCHANTDTO_BY											= "getMerchantDTOBy";
	//獲取特店信息
	public static final String GET_MERCHANS_BY_CODE_AND_COMPANYID							= "getMerchantsByCodeAndCompamyId";
	//獲取特店表頭下拉菜單
	public static final String GET_MERCHANT_HEADER_LIST										= "getMerchantHeaderList";	
	//根據ID獲取對應的案件處理歷史資料檔
	public static final String ACTION_GET_CASE_MESSAGE_BY_CASEID							= "getCaseMessageByCaseId";
	//獲得交易參數項目list
	public static final String ACTION_GET_TRANSACTION_PARAMETER_ITEM_LIST					= "getTransactionParameterItemList";
	//獲取交易參數可以編輯的列名，以交易參數分組
	public static final String ACTION_GET_EDIT_FIELDS_GROUP_BY_TRANSACTION_TYPE				= "getEditFieldsGroupbyTransactionType";
	//
	public static final String GET_TRANSACTION_PARAMETER_DETAIL_DTO							= "getTransactionParameterDetailDTO";
	//
	public static final String ACTION_GET_BUILT_IN_FEATURE									= "getBuiltInFeature";
	//
	public static final String ACTION_GET_PERIPHERALS										= "getPeripherals";
	//
	public static final String ACTION_GET_SOFTWAREVERSIONS									= "getSoftwareVersions";
	//
	public static final String ACTION_GET_VENDORS											= "getVendors";
	//根據公司ID獲取公司下的所有的工程師
	public static final String ACTION_GET_USERLIST_BY_COMPANY								= "getUserListByCompany";
	//獲取設備品相維護中的通訊模式
	public static final String ACTION_GET_CONNECTION_TYPE_LIST								= "getConnectionTypeList";
	// 檢查dtid號碼是否夠用
	public static final String ACTION_CHECK_DTID_NUMBER										= "checkDtidNumber";
	// 得到有重複的支援功能
	public static final String ACTION_GET_REPEAT_SUPPORT_FUN								= "getRepeatSupportFun";
	//依據歷史ID獲取新增求償所需的資料
	public static final String ACTION_GET_PAY_INFO											= "getPayInfo";
	//獲取設備型號
	public static final String GET_CASE_REPEAT_LIST											= "getCaseRepeatList";
	// 得到問題原因的列表
	public static final String ACTION_GET_PROBLEM_REASON_LIST								= "getProblemReasonList";
	// 得到問題解決方式的列表
	public static final String ACTION_GET_PROBLEM_SOLUTION_LIST								= "getProblemSolutionList";
	// 得到倉庫控管列表的方法
	public static final String ACTION_GET_WAREHOUSE_BY_USER_ID								= "getWarehouseByUserId";
	// 查詢案件對應的設備
	public static final String QUERT_CASE_ASSET												= "queryCaseAsset";
	// 倉庫據點集合
	public static final String PARAM_WAREHOUSE_LIST											= "warehouseList";
	//得到工單範本集合
	public static final String ACTION_GET_TEMPLATES_LATES									= "getTemplatesList";
	//工單範本集合
	public static final String PARAM_TEMPLATES_LIST											= "templatesList";
	// 交易類別集合
	public static final String PARAM_TRANS_CATEGORY_LIST									= "transCategoryList";
	// 有角色羣組信息
	public static final String PARAM_HAVE_ROLE_GROUP										= "haveRoleGroup";
	// 角色羣組信息集合
	public static final String PARAM_ROLE_GROUP_LIST										= "roleGroupList";
	// 維護部門預設的集合
	public static final String PARAM_CASE_PART_GROUP_LIST									= "casePartGroupList";
	// 維護部門預設的集合字符串
	public static final String PARAM_CASE_PART_GROUP_LIST_STR								= "casePartGroupListStr";
	// 案件狀態預設集合字符串
	public static final String PARAM_CASE_STATUS_LIST_STR									= "caseStatusListStr";
	// 案件狀態預設集合字符串
	public static final String PARAM_CASE_CATEGORY_LIST_STR									= "caseCategoryListStr";
	// 通過id查案件信息
	public static final String ACTION_GET_CASE_INFO_BY_ID									= "getCaseInfoById";
	// 通過id查案件信息
	public static final String ACTION_GET_CHANGE_CASE_INFO_BY_ID							= "getChangeCaseInfoById";
	//查詢該筆dtid下比本案件建安早的裝機案件數量
	public static final String ACTION_GET_COUNT_BY_INSTALL									= "getCountByInstall";
	//案件匯入保存方法
	public static final String CASE_IMPORT_ASYNCHRONOUS_HANDLE								= "caseImportAsynchronousHandle";
	//是否改變鏈接檔
	public static final String IS_CHANGE_CASE_LINK											= "isChangeCaseLink";
	//設備鏈接當被修改
	public static final String ASSET_LINK_IS_CHANGE											= "assetLinkIsChange";
	//
	public static final String INIT_EDIT_CHECK_UPDATE										= "initEditCheckUpdate";
	//
	public static final String CHECK_ROLE_REPEAT											= "checkRoleRepeat";
	//
	public static final String CHECK_ASSET_USER_IS_TAIXIN_RENT								= "checkAssetUserIsTaixinRent";
	public static final String COUNT_ASSET													= "countAsset";
	//通知收件人集合
	public static final String ACTION_GET_MAIL_GROUP_LIST									= "getMailGroupList";
	public static final String ACTION_GET_NAME_LIST											= "getNameList";
	public static final String ACTION_GET_ASSET_ID_LIST										= "getAssetIdList";
	//發送mail
	public static final String ACTION_SEND_QUERY_MAIL										= "sendQueryMail";
	
	// 欄位模板集合
	public static final String PARAM_COLUMN_TEMPLATE_LIST									= "columnTemplateList";
	// 所有欄位集合
	public static final String PARAM_ALL_COLUMNS_LIST										= "allColumnsList";
	// 用戶當前模板
	public static final String PARAM_CURRENT_COLUMN_TEMPLATE								= "currentColumnTemplate";
	
	public static final String ACTION_CHECK_SERIAL_NUMBER									= "checkSerialNumber";
	
	
	// 客戶聯動合約處理
	public static final String PARAM_EDIT_CONTRACT_LIST										= "editContractList";
	// 合約維護廠商處理
	public static final String PARAM_EDIT_VENDORS											= "editVendors";
	// 維護廠商部門處理
	public static final String PARAM_EDIT_DEPT_LIST											= "editDeptList";
	// 縣市聯動郵遞區號處理(裝機)
	public static final String PARAM_INSTALLED_POST_CODE_LIST								= "installedPostCodes";
	// 縣市聯動郵遞區號處理(聯系)
	public static final String PARAM_CONTACT_POST_CODE_LIST									= "contactPostCodes";
	// 縣市聯動郵遞區號處理(營業)
	public static final String PARAM_LOCATION_POST_CODE_LIST								= "locationPostCodes";
	// 特店特店表頭
	public static final String PARAM_EDIT_MERCHANT_HEADERS									= "editMerchantHeaders";
	// 客戶刷卡機型處理
	public static final String PARAM_EDIT_EDC_ASSETS										= "editEdcAssets";
	// 客戶聯動軟體版本處理
	public static final String PARAM_EDIT_SOFTWARE_VERSIONS									= "editSoftwareVersions";
	// 客戶聯動周邊設備處理
	public static final String PARAM_EDIT_PERIPHERALS_LIST									= "editPeripheralsList";
	// 刷卡機型聯動內建功能處理
	public static final String PARAM_EDIT_BUILT_IN_FEATURES									= "editBuiltInFeatures";
	// 刷卡機型聯動連線方式處理
	public static final String PARAM_EDIT_CONNECTION_TYPES									= "editConnectionTypes";
	// 周邊設備1聯動周邊設備1功能處理
	public static final String PARAM_EDIT_PERIPHERALS_FUNCTIONS								= "editPeripheralsFunctions";
	// 周邊設備2聯動周邊設備2功能處理
	public static final String PARAM_EDIT_PERIPHERALS_FUNCTION2S							= "editPeripheralsFunction2s";
	// 周邊設備3聯動周邊設備3功能處理
	public static final String PARAM_EDIT_PERIPHERALS_FUNCTION3S							= "editPeripheralsFunction3s";
	// 周邊設備3聯動周邊設備3功能處理
	public static final String PARAM_EDIT_CONTRACT_ID										= "editContractId";
	// 檢核改筆案件的上一筆是否為裝機，如果為裝機案件 則上一筆案件 須要完修 的方法
	public static final String GET_CASE_REPEAT_BY_INSTALL_UNCOMPLETE						= "getCaseRepeatByInstallUncomplete";
	
	public static final String PARAM_ASSET_CATEGORY_LIST									= "assetCategoryList";
	// 案件匯入類別
	// 案件匯入類別
	public static final String PARAM_IMPORT_TICKET_TYPES									= "importTicketTypes";
	//匯入TMS流水號  
	public static final String ACTION_CREATE_BATCH_NUM										= "createBatchNum";
	//TMS參數匯入
	public static final String TMS_POST														= "TMSPost";
	//TMS參數匯入，新增(DB)APILOG-CLIENT_CODE參數
	public static final String TMS															= "TMS";
	//TMS整批參數匯入
	public static final String TMS_PARA_CONTENTS											= "TMSParaContents";	
	//電文 上行(RQ)
	public static final String RQ															= "RQ";	
	//電文 下行(RS)
	public static final String RS															= "RS";	
	//GP公司下S80 Ethernet需核檢內容
	public static final String ACTION_CHECKS80_ETHERNET_ASSET								= "checkS80EthernetAsset";
	//GP公司下S80 Ethernet需核檢內容
	public static final String ACTION_CHECKS80_RF_ASSET										= "checkS80RFAsset";
	//GP公司下S90 3G需核檢內容
	public static final String ACTION_CHECKS90_3G_ASSET										= "checkS903GAsset";
	//GP公司下S90 RF需核檢內容
	public static final String ACTION_CHECKS90_RF_ASSET										= "checkS90RFAsset";
	//GP公司下, 交易類別CUP，若有開預先授權功能，核檢CUP手輸是否開啓
	public static final String ACTION_CHECK_TRANSACTION_CUP									= "checkTransactionCup";
	
	//===============Use Case No ex:UC_NO_CMSXXXXX_功能名称 ======================================================//
	public static final String UC_NO_CMS01030                           				    = "CMS101030";
	// 系統管理
	public static final String UC_NO_ADM_01000                                              = "ADM01000";
	//使用者維護
	public static final String UC_NO_ADM_01010                                              = "ADM01010";
	//系統日志查詢
	public static final String UC_NO_AMD_01040												= "ADM01040";
	//系統參數維護
	public static final String UC_NO_AMD_01050												= "ADM01050";
	//特店表頭維護
	public static final String UC_NO_BIM_02080                                              = "BIM02080";
	//密碼原則設定
	public static final String UC_NO_ADM_01060												= "ADM01060";
	//合約維護UCN
	public static final String UC_NO_BIM_02030 												= "BIM02030";
	//倉庫據點維護
	public static final String UC_NO_BIM_02050                                              = "BIM02050";
	//系統角色管理
	public static final String UC_NO_ADM_01030												= "ADM01030";
	//部門維護
	public static final String UC_NO_BIM_02020                                              = "BIM02020";
	//合約SLA設定
	public static final String UC_NO_BIM_02040                                              = "BIM02040";
	//公司基本訊息維護
	public static final String UC_NO_BIM_02010												= "BIM02010";
	//電子郵件羣組維護
	public static final String UC_NO_BIM_02090												= "BIM02090";
	//報表發送功能設定
	public static final String UC_NO_BIM_02100												= "BIM02100";
	//客戶特店維護
	public static final String UC_NO_BIM_02070												= "BIM02070";
	//行事曆
	public static final String UC_NO_BIM_02060												= "BIM02060";
	//設備品項維護
	public static final String UC_NO_DMM_03010												= "DMM03010";
	//耗材品项维护
	public static final String UC_NO_DMM_03020												= "DMM03020";
	//程式版本維護
	public static final String UC_NO_PVM_04010												= "PVM04010";
	// EDC交易參數查詢 
	public static final String UC_NO_PVM_04030												= "PVM04030";
	//財產批號匯入
	public static final String UC_NO_DMM_03030												= "DMM03030";
	
	public static final String UC_NO_DMM_03050												= "DMM03050";
	//設備管理作業
	public static final String UC_NO_DMM_03060												= "DMM03060";
	public static final String UC_NO_DMM_03070												= "DMM03070";
	//設備盤點功能
	public static final String UC_NO_DMM_03080												= "DMM03080";
	//案件處理建案功能
	public static final String UC_NO_SRM_050201												= "SRM050201";
	public static final String UC_NO_DMM_0305003											= "DMM0305003";
	public static final String UC_NO_DMM_03090												= "DMM03090";
	public static final String UC_NO_DMM_03100												= "DMM03100";
	//刷卡機標籤管理
	public static final String UC_NO_DMM_03120												= "DMM03120";
	//案件處理功能
	public static final String UC_NO_SRM_05020												= "SRM05020";
	//DTID帳號管理功能
	public static final String UC_NO_PVM_04020												= "PVM04020";
	//設備入庫
	public static final String UC_NO_DMM_03040												= "DMM03040";
	//客訴管理
	public static final String UC_NO_SRM_05100												= "SRM05100";
	//報修問題分析報表
	public static final String UC_NO_SRM_05080												= "SRM05080";
	//求償作業
	public static final String UC_NO_SRM_05040												= "SRM05040";
	//工單範本
	public static final String UC_NO_SRM_05010												= "SRM05010";
	//完工回覆檔(歐付寶格式)
	public static final String UC_NO_BRM_08090												= "BRM08090";
	//舊資料轉入
	public static final String UC_NO_BAT_09020												= "BAT09020";
	//維護費報表（捷達威格式）
	public static final String UC_NO_AMM_06040												= "AMM06040";
	//維護費報表（環匯格式）
	public static final String UC_NO_AMM_06030												= "AMM06030";
	//維護費報表（大眾格式）
	public static final String UC_NO_AMM_06020												= "AMM06020";
	//AMM06060維護費報表(上銀格式)
	public static final String UC_NO_AMM_06060												= "AMM06060";
	//AMM06060維護費報表(陽新格式)
	public static final String UC_NO_AMM_06070												= "AMM06070";
	//AMM06080維護費報表(彰銀格式)
	public static final String UC_NO_AMM_06080												= "AMM06080";
	//AMM06050維護費報表(綠界格式)
	public static final String UC_NO_AMM_06050												= "AMM06050";
	//DMM03130設備借用
	public static final String UC_NO_DMM_03130												= "DMM03130";
	//DMM031４0設備借用處理
	public static final String UC_NO_DMM_03140												= "DMM03140";
	//=====================查詢參數===================================================//
	//easyui-datagrid 每頁行數
	public static final String QUERY_PAGE_ROWS												= "rows";
	//easyui-datagrid 當前頁碼
	public static final String QUERY_PAGE_INDEX												= "page";
	//easyui-datagrid 升序降序
	public static final String QUERY_PAGE_SORT												= "sort";
	//easyui-datagrid 排序字段
	public static final String QUERY_PAGE_ORDER												= "order";
	//默認每筆大小
	public static final Integer PARAM_PAGE_ROWS												= 10;
	//第一頁
	public static final Integer PARAM_PAGE_INDEX											= 1;
	//升序
	public static final String PARAM_PAGE_ORDER												= "asc";
	//降序
	public static final String PARAM_PAGE_DESCEND_ORDER										= "desc";
	//查詢結果筆數
	public static final String PARAM_QUERY_RESULT_TOTAL										= "total";
	//查詢結果
	public static final String PARAM_QUERY_RESULT_ROWS										= "rows";
	//AJAX響應結果
	public static final String PARAM_ACTION_RESULT_SUCCESS									= "success";
	//AJAX登錄檢查
	public static final String PARAM_ACTION_RESULT_CODE										= "code";
	public static final String PARAM_ACTION_RESULT_SESSION_STATUS									= "sessionstatus";
	public static final String PARAM_ACTION_RESULT_FAIL_CODE_TIMEOUT						= "timeout";
	//AJAX響應信息
	public static final String PARAM_ACTION_RESULT_MSG										= "msg";
	//service result
	public static final String PARAM_ACTION_RESULT											= "result";
	//單筆查詢結果
	public static final String PARAM_QUERY_RESULT_ROW										= "row";
	//設備驗收是否成功
	public static final String PARAM_ACTUAL_ACCEPTANCE										= "actualAcceptance";
	//是否驗收完成
	public static final String PARAM_FINISH_ACCEPTANCE										= "finishAcceptance";
	
	//==============================角色名称=======================================================================//
	public static final String ROLE_NAME_GENERAL_STAFF										= "EMPLOYEE";
	public static final String ROLE_NAME_GENERAL_SYSTEM										= "SYSTEM";
	public static final String ROLE_NAME_PROJECT_LEADER										= "PM";
	public static final String ROLE_NAME_IT_DIRECTOR										= "IT";
	public static final String ROLE_NAME_HR_DIRECTOR										= "HR";
	public static final String ROLE_NAME_DIRECTOR											= "MANAGER";
	public static final String ROLE_NAME_QA													= "QA";
	public static final String ROLE_NAME_ATTENDANCE											= "ATTENDANCE";
	public static final String ROLE_NAME_TMS												= "TMS";
	public static final String NUMBER_SIX													= "06";
	public static final String NUMBER_SEVEN													= "07";
	public static final String NUMBER_EIGHT													= "08";
	public static final String NUMBER_NINE													= "09";
	public static final String TASK_STATUS_FIVE												= "05";
	public static final String TASK_STATUS_FOUR												= "04";
	public static final String PROJECT_STATUS_OPEN											= "01";
	public static final String PROJECT_STATUS_ALL											= "00";
	public static final String TASK_STAUS_TOW												= "02";
	public static final String TASK_STAUS_THREE												= "03";
	public static final String LOG_STAUS                                   					= "4";
	public static final String LOG_STAUS_THREE                                  			= "3";
	public static final String LEAVE_CASE_STATUS_ZERO										= "0";
	public static final String LEAVE_CASE_STATUS_ONE										= "1";
	public static final String LEAVE_CASE_STATUS_TWO										= "2";
	public static final String LEAVE_CASE_STATUS_THREE										= "3";
	public static final String LEAVE_CASE_STATUS_FOUR										= "4";
	public static final String LEAVE_CASE_STATUS_FIVE										= "5";
	public static final String LEAVE_CASE_STATUS_SIX										= "6";
	public static final String LEAVE_CASE_STATUS_SEVEN										= "7";
	public static final String LEAVE_CASE_STATUS_EIGHT										= "8";
	public static final String LEAVE_CASE_STATUS_NINE										= "9";
	public static final String LEAVE_CASE_STATUS_TEN										= "10";
	public static final String LEAVE_CASE_STATUS_ELEVEN										= "11";
	public static final String LEAVE_CASE_STATUS_TWELVE										= "12";
	public static final String LEAVE_CASE_STATUS_THIRTEEN									= "13";
	public static final String LEAVE_CASE_STATUS_FIFTEEN									= "15";
	public static final String LEAVE_CASE_STATUS_SIXTEEN									= "16";
	public static final String PROPERTY_ID_LENGTH											= "20";
	public static final String REQUIREMENT_NO_LENGTH										= "17";
	public static final String CONTACT_ADDRESS_LENGTH										= "100";
	public static final String DESCRIPTION_LENGTH											= "200";
	public static final String CONTACT_USER_LENGTH											= "50";
	public static final String MAXLENGTH_NUMBER_TWO											= "2";
	public static final String MAXLENGTH_NUMBER_THREE										= "3";
	public static final String MAXLENGTH_NUMBER_FIVE										= "5";
	public static final String MAXLENGTH_NUMBER_TEN											= "10";
	public static final String MAXLENGTH_NUMBER_TWENTY										= "20";
	public static final String MAXLENGTH_NUMBER_THIRTY										= "30";
	public static final String MAXLENGTH_NUMBER_FIFTY										= "50";
	public static final String MAXLENGTH_NUMBER_ONE_HUNDRED									= "100";
	public static final String MAXLENGTH_NUMBER_TWO_HUNDRED									= "200";
	public static final String MAXLENGTH_NUMBER_TWO_HUNDRED_FIFTY_FIVE						= "255";
	public static final String MAXLENGTH_NUMBER_FIVE_HUNDRED								= "500";
	public static final String MAXLENGTH_NUMBER_ONE_THOUSAND								= "1000";
	public static final String MAXLENGTH_NUMBER_TWO_THOUSAND								= "2000";
	public static final String MAXLENGTH_NUMBER_SEVEN										= "7";
	public static final String MAXLENGTH_NUMBER_EIGHT										= "8";
	public static final String MAXLENGTH_NUMBER_ONE											= "1";
	public static final String MAXLENGTH_NUMBER_FIFTEEN										= "15";
	public static final String MAXLENGTH_TMS_PARAM_DESC										= "2000";
	public static final String MAXLENGTH_NUMBER_FORTY_FOUR									= "44";
	//經茂聯網
	public static final String CYBERSOFT_CODE												= "10000000-01";
	//大眾格式的報表重送code
	public static final String REPORT_TCB_EDC_FIFTEEN										= "15";
	//綠界格式的報表重送code
	public static final String REPORT_TCB_GREEN_WORLD_SIXTEEN								= "16";
	//上銀格式報表重送的報表code
	public static final String REPORT_TCB_SCSB_SEVENTEEN									= "17";
	//陽信格式的報表重送code
	public static final String REPORT_TCB_SYB_NINETEEN										= "19";
	//張銀格式的報表重送code --改為20 2017/11/17
	public static final String REPORT_TCB_CHB_TWENTY										= "20";
	//完修通知AO --改為21 2017/11/17
	public static final String REPORT_COMPLETE_TWENTY_ONE									= "21";

	public static final Integer LEAVE_CASE_STATUS_ONE_INT									= 1;
	public static final Integer LEAVE_CASE_STATUS_TWO_INT									= 2;
	public static final Integer LEAVE_CASE_STATUS_THREE_INT									= 3;
	public static final String TASK_TYPE													= "TASK_TYPE";
	public static final String TASK_STATUS                                  				= "TASK_STATUS";
	public static final String TASK_PROGRESS												= "TASK_PROGRESS";
	public static final String MERCHANT_AREA												= "AREA";
	public static final String VIP															= "VIP";
	public static final String YES_OR_NO													= "YES_OR_NO";
	public static final String PWD_LENGTH												    = "PWD_LENGTH";
	public static final String ASSET_STATUS													= "ASSET_STATUS";
	public static final String ASSET_CATEGORY												= "ASSET_CATEGORY";
	public static final String DTID_TYPE													= "DTID_TYPE";
	public static final String LOCATION														= "LOCATION";
	public static final String ACTION														= "ACTION";
	public static final String FAULT_DESCRIPTION											= "FAULT_DESCRIPTION";
	//電子郵件群組 - OAS
	public static final String MAIL_GROUP_OAS												= "4";
	public static final String FAULT_COMPONENT												= "FAULT_COMPONENT";
	//報表名稱 - 日報
	public static final String REPORT_NAME_DAY_REPORT										= "1";
	//報表名稱 - 月報
	public static final String REPORT_NAME_MONTH_REPORT										= "2";
	//報表名稱 - EDC流通在外台數報表
	public static final String REPORT_NAME_EDC_OUTSTANDING_NUM_REPORT						= "3";
	//報表名稱 - EDC合約到期提示報表
	public static final String REPORT_NAME_EDC_CONTRACT_EXPIRE_INFO_REPORT					= "4";
	//報表名稱 - 完修逾時率警示通知
	public static final String REPORT_NAME_COMPLETE_OVERDUE_RATE_REPORT						= "12";
	//報表名稱 - 案件報修明細(環匯格式)
	public static final String REPORT_NAME_CASE_REPAIR_DETAIL_REPORT						= "10";
	//報表名稱 - 案件設備明細(環匯格式)
	public static final String REPORT_NAME_CASE_ASSET_DETAIL								= "11";
	//報表名稱 - 完工回覆檔(歐付寶格式)
	public static final String REPORT_NAME_FINISHED_REPLY_REPORT							= "9";
	//維護費報表(大眾格式)
	public static final String REPORT_NAME_FEE_REPORT_FOR_JDW								= "feeReportForJdw";
	public static final String CASE_REMOVE_ASSET											= "R";
	public static final String CASE_DELETE_ASSET											= "D";
	public static final String AOEMAIL														= "AOEMAIL";
	public static final String MERCHANT_AO													= "MerchantAO (特店 AO)";
	public static final String IS_DATE_OR_TIMESTAMP											= "isDateOrTimestamp";
	public static final String NO_DATE_OR_TIMESTAMP											= "noDateOrTimestamp";
	public static final String D_NUMBER_BIGDECIMAL											= "DNumberBigDecimal";
	public static final String PICTONIC														= "Pictonic";
	public static final String WINDOWS_TICKER												= "WindowSticker";
	public static final String COMPANY_CODE_TCB_EDC											= "TCB-EDC";
	//=================== SERVICE NAME for I18NUTIL ex:TB_NAME_tableName=============================================
	//=================== 維護費報表的code=============================================
	//刷卡機(含週邊設備)裝/移機
	public final static String EDC_FEE_REPORT_INSTALLED										= "INSTALLED";
	//刷卡機(含週邊設備)拆/撤機
	public final static String EDC_FEE_REPORT_UNINSTALLED									= "UNINSTALLED";
	//刷卡機(含週邊設備)設定(到場)
	public final static String EDC_FEE_REPORT_ARRIVED										= "ARRIVED";
	//刷卡機(含週邊設備)設定(遠端-軟派)
	public final static String EDC_FEE_REPORT_REMOTE										= "REMOTE";
	//刷卡機(含週邊設備)二次作業(裝/拆/設定/配合特約商店作業)
	public final static String EDC_FEE_REPORT_SECOND										= "SECOND";
	//刷卡機(含週邊設備)抽樣檢測
	public final static String EDC_FEE_REPORT_CHECK											= "CHECK";
	//急件(非都會區於甲方提供資料日次日起算2個工作日內完成)
	public final static String EDC_FEE_REPORT_FAST											= "FAST";
	//特急件(都會區、非都會區於甲方提供資料日次日起算少於24小時內完成)
	public final static String EDC_FEE_REPORT_EXTRA											= "EXTRA";
	//周邊
	public final static String EDC_FEE_REPORT_PERIPHERAL									= "PERIPHERAL";
	//門號月租費
	public final static String EDC_FEE_REPORT_NUMBER_FEE									= "NUMBER_FEE";
	//刷卡機、週邊設備月維護費用
	public final static String EDC_FEE_REPORT_MAINTENANCE_FEE								= "MAINTENANCE_FEE";
	//單價 或者數量 0
	public final static String EDC_FEE_REPORT_PRICE_ZERO									= "0";
	//【拆機類型=到場拆機
	public final static String EDC_FEE_REPORT_ARRIVE_UNINSTALL								= "ARRIVE_UNINSTALL";
	//【拆機類型=遺失報損
	public final static String EDC_FEE_REPORT_LOSS_REPORT									= "LOSS_REPORT";
	//拆機類型=業務自拆
	public final static String EDC_FEE_REPORT_SERVICE_SELF_UNINSTALL						= "SERVICE_SELF_UNINSTALL";
	//處理方式=到場處理
	public final static String EDC_FEE_REPORT_ARRIVE_PROCESS								= "ARRIVE_PROCESS";
	//處理方式 = 軟派
	public final static String EDC_FEE_REPORT_SOFT_DISPATCH									= "SOFT_DISPATCH";
	//門號月租費 (含稅)總計
	public final static String EDC_FEE_REPORT_NUMBER_FEE_TAXSUM								= "numberFeeTaxSum";
	//門號月租費 (未稅)總計
	public final static String EDC_FEE_REPORT_NUMBER_FEE_NO_TAXSUM							= "numberFeeNoTaxSum";
	//維護費(含稅) 總計
	public final static String EDC_FEE_REPORT_MAIN_TENANCE_TAXSUM							= "maintenanceTaxSum";
	//維護費(未稅)總計
	public final static String EDC_FEE_REPORT_MAIN_TENANCE_NO_TAXSUM						= "maintenanceNoTaxSum";
	//非首裝費用總計
	public final static String EDC_FEE_REPORT_NOT_FIRST_INSTALLED_PRICESUM					= "notFirstInstalledPriceSum";
	//非首裝費用總計(未稅)
	public final static String EDC_FEE_REPORT_NOT_FIRST_INSTALLED_NOTAX_PRICESUM			= "notFirstInstalledNoTaxPriceSum";
	//拆機作業費總計
	public final static String EDC_FEE_REPORT_UNINSTALL_PRICESUM							= "unInstallPriceSum";
	//拆機作業費(未稅)總計
	public final static String EDC_FEE_REPORT_UNINSTALL_PRICE_NOTAX_SUM						= "unInstallPriceNoTaxSum";
	//設定費總計
	public final static String EDC_FEE_REPORT_SETTING_PRICESUM								= "settingPriceSum";
	//設定費(未稅)總計
	public final static String EDC_FEE_REPORT_SETTING_PRICE_NOTAX_SUM						= "settingPriceNoTaxSum";
	//急件 特急件 總計
	public final static String EDC_FEE_REPORT_FAST_SUM										= "fastSum";
	//急件 特急件 (未稅)總計
	public final static String EDC_FEE_REPORT_FAST_NOTAX_SUM								= "fastNoTaxSum";
	//POS成本總計 
	public final static String EDC_FEE_REPORT_POS_SUM										= "posSum";
	//ECR線材總費用總計
	public final static String EDC_FEE_REPORT_ECRLINE_SUM_INCASE							= "ecrlineSumInCase";
	//ECR線材總費用(未稅)
	public final static String EDC_FEE_REPORT_ECRLINE_NOTAX_SUM_INCASE						= "ecrlineNoTaxSumInCase";
	//網路線費用總和
	public final static String EDC_FEE_REPORT_NETWORK_ROUTE_SUMINCASE						= "networkRouteSumInCase";
	//網路線費用總和(未稅)
	public final static String EDC_FEE_REPORT_NETWORK_ROUTE_NOTAX_SUMINCASE					= "networkRouteNoTaxSumInCase";
	//線材使用-ECR線材
	public final static String EDC_FEE_REPORT_ECR_LINE_ZH_TW								= "EDC_FEE_REPORT_ECR_LINE";
	//線材使用-網路線
	public final static String EDC_FEE_REPORT_NET_WORK_LINE_ZH_TW							= "EDC_FEE_REPORT_NET_WORK_LINE";
	//sheetName 費用總表
	public final static String EDC_FEE_REPORT_SHEET_NAME_PRICE_SUM							= "EDC_FEE_REPORT_SHEET_NAME_PRICE_SUM";
	//sheetName 作業明細
	public final static String EDC_FEE_REPORT_SHEET_NAME_CASE_INFO							= "EDC_FEE_REPORT_SHEET_NAME_CASE_INFO";
	//sheetName 維修耗材費用-AO已回覆扣款方式
	public final static String EDC_FEE_REPORT_SHEET_NAME_PAYMENT_INFO_REPLY					= "EDC_FEE_REPORT_SHEET_NAME_PAYMENT_INFO_REPLY";
	//sheetName 維修耗材費用-AO未回覆扣款方式
	public final static String EDC_FEE_REPORT_SHEET_NAME_PAYMENT_INFO_NOT_REPLY				= "EDC_FEE_REPORT_SHEET_NAME_PAYMENT_INFO_NOT_REPLY";
	//sheetName 週邊設備單獨裝/拆/設定
	public final static String EDC_FEE_REPORT_SHEET_NAME_PERIPHERAL_SETTING					= "EDC_FEE_REPORT_SHEET_NAME_PERIPHERAL_SETTING";
	//頻繁拆機費
	public final static String EDC_FEE_REPORT_OFFEN											= "OFFEN";
	//=================== TABLE NAME  ex:TB_NAME_tableName=============================================
	public final static String EMAIL_TIME													= "EMAIL_TIME";
	public final static String TO_EMAIL														= "TO_EMAIL";
	public final static String IATOMS_TB_NAME_EMPLOYEE										= "EMPLOYEE";
	public final static String IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF						= "BASE_PARAMETER_ITEM_DEF";
	public final static String IATOMS_TB_NAME_ROLE_PERMISSION 							    = "ADM_ROLE_PERMISSION";
	public static final String IATOMS_TB_NAME_ASSET_IN_INFO									= "ASSET_IN_INFO";
	//使用者
	public static final String IATOMS_TB_NAME_ADM_USER                                      = "ADM_USER";
	//使用者角色
    public static final String IATOMS_TB_NAME_ADM_ROLE                                      = "ADM_ROLE";
    //使用者密碼歷史記錄
    public static final String IATOMS_TB_NAME_ADM_PWD_HISTORY                                = "ADM_PWD_HISTORY";
    //使用者密碼變更原則
  	public static final String IATOMS_TB_NAME_ADM_SECURITY_DEF                               = "ADM_SECURITY_DEF";
  	
  	//電文紀錄
  	public static final String IATOMS_TB_NAME_API_LOG										= "API_LOG";
  	
	//公司
	public static final String IATOMS_TB_NAME_VENDOR                                        = "VENDOR";
	//系統參數
	public static final String IATOMS_TB_NAME_SYSTEM_ITEM                                   = "SYSTEM_ITEM";
	//部門
	public static final String IATOMS_TB_NAME_BIM_DEPARTMENT                                = "BIM_DEPARTMENT";
	//客戶特店
	public static final String IATOMS_TB_NAME_MERCHANT_NEW									= "MERCHANT_NEW";
	//特店表頭
	public static final String IATOMS_TB_NAME_BIM_MERCHANT_HEADER			    			= "BIM_MERCHANT_HEADER";
	//客戶特店
	public static final String IATOMS_TB_NAME_BIM_MERCHANT									= "BIM_MERCHANT";
	//密碼原則設定
	public static final String IATOMS_TB_NAME_SECURITY_DEF									= "SECURITY_DEF";
	//合約
	public static final String IATOMS_TB_NAME_CONTRACT                                      = "BIM_CONTRACT";
	//合約文件
	public static final String IATOMS_TB_NAME_BIM_CONTRACT_ATTACHED_FILE					= "BIM_CONTRACT_ATTACHED_FILE";
	//合約設備
	public static final String IATOMS_TB_NAME_CONTRACT_ASSET                                = "CONTRACT_ASSET";
	//客戶
	public static final String IATOMS_TB_NAME_CUSTOMER                               		= "CUSTOMER";
	//設備類型
	public static final String IATOMS_TB_NAME_DMM_ASSET_TYPE								= "DMM_ASSET_TYPE";
	//倉庫據點
	public static final String IATOMS_TB_NAME_BIM_WAREHOUSE									= "BIM_WAREHOUSE";
	//公司基本訊息維護
	public static final String IATOMS_TB_NAME_BIM_COMPANY									= "BIM_COMPANY";
	//電子郵件羣組維護
	public static final String IATOMS_TB_NAME_BIM_MAIL_LIST									= "BIM_MAIL_LIST";
	//報表發送功能設定
	public static final String IATOMS_TB_NAME_REPORT_SETTING								= "REPORT_SETTING";
	//系統日志查詢
	public static final String IATOMS_TB_NAME_ADM_SYSTEM_LOGGING							= "ADM_SYSTEM_LOGGING";
	//耗材
	public static final String IATOMS_TB_NAME_DMM_SUPPLIES									= "DMM_SUPPLIES";
	//廠商使用設備檔
	public static final String IATOMS_TB_NAME_DMM_ASSET_TYPE_COMPANY						= "DMM_ASSET_TYPE_COMPANY";
	//設備支援通訊模式檔
	public static final String IATOMS_TB_NAME_DMM_ASSET_SUPPORTED_COMM						= "DMM_ASSET_SUPPORTED_COMM";
	//設備支援功能關系檔
	public static final String IATOMS_TB_NAME_DMM_ASSET_SUPPORTED_FUNCTION					= "DMM_ASSET_SUPPORTED_FUNCTION";
	// 生成轉倉批號
	public static final String PARAM_ASSET_TRANS_ID_TW										= "TN";
	//SLA
	public static final String IATOMS_TB_NAME_BIM_SLA  										= "BIM_SLA";
	//程式版本維護
	public static final String IATOMS_TB_NAME_PVM_APPLICATION  								= "PVM_APPLICATION";
	//程式版本維護
	public static final String IATOMS_TB_NAME_APPLICATION  									= "APPLICATION";
	//設備入庫明細
	public static final String IATOMS_TB_NAME_ASSET_IN_LIST  								= "ASSET_IN_LIST";
	//設備轉倉作業
	public static final String IATOMS_TB_NAME_ASSET_TRANS_INFO								= "DMM_ASSET_TRANS_INFO";
	//設備轉倉作業明細
	public static final String IATOMS_TB_NAME_ASSET_TRANS_LIST								= "DMM_ASSET_TRANS_LIST";
	//設備盤點作業主檔
	public static final String IATOMS_TB_NAME_ASSET_STOCKTACK_CATEGROY						= "DMM_ASSET_STOCKTACK_CATEGORY";
	//設備盤點作業主檔
	public static final String IATOMS_TB_NAME_ASSET_STOCKTACK_STATUS						= "DMM_ASSET_STOCKTACK_STATUS";
	//設備盤點主檔
	public static final String IATOMS_TB_NAME_ASSET_STOCKTACK_INFO							= "DMM_ASSET_STOCKTACK_INFO";
	//設備盤點明細檔
	public static final String IATOMS_TB_NAME_ASSET_STOCKTACK_LIST							= "DMM_ASSET_STOCKTACK_LIST";
	//案件主檔
	public static final String IATOMS_TB_NAME_TICKET										= "TICKET";
    //生成盤點批號
	public static final String PARAM_ASSET_STOCKTAC_ID_IN									= "IN";
	//庫存主檔
	public static final String IATOMS_TB_NAME_REPOSITORY									= "DMM_REPOSITORY";
	public static final String IATOMS_TB_NAME_DMM_REPOSITORY								= "DMM_REPOSITORY";
	//庫存歷史檔
	public static final String IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY						= "DMM_REPOSITORY_HISTORY";
	//
	public static final String IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY_MONTHLY				= "DMM_REPOSITORY_HISTORY_MONTHLY";
	public static final String IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY						= "DMM_REPOSITORY_HISTORY_MONTHLY";
	
	//刷卡機標籤管理
	public static final String IATOMS_TB_NAME_DMM_EDC_LABEL									= "DMM_EDC_LABEL";
	
	public static final String IATOMS_TB_NAME_PVM_DTID_DEF									= "PVM_DTID_DEF";
	
	//SRM_客訴管理
	public static final String IATOMS_TB_NAME_SRM_COMPLAINT_CASE							= "SRM_COMPLAINT_CASE";
	//SRM_客訴管理附加檔案
	public static final String IATOMS_TB_NAME_SRM_COMPLAINT_CASE_FILE						= "SRM_COMPLAINT_CASE_FILE";
		
	//SRM_交易參數項目
	public static final String IATOMS_TB_NAME_SRM_TRANSACTION_PARAMETER_ITEM				= "SRM_TRANSACTION_PARAMETER_ITEM";
	//SRM_交易參數列表
	public static final String IATOMS_TB_NAME_SRM_TRANSACTION_PARAMETER_DETAIL				= "SRM_TRANSACTION_PARAMETER_DETAIL";
	//SRM_案件最新交易參數資料檔
	public static final String IATOMS_TB_NAME_SRM_CASE_NEW_TRANSACTION_PARAMETER			= "SRM_CASE_NEW_TRANSACTION_PARAMETER";
	//SRM_案件處理中交易參數資料檔
	public static final String IATOMS_TB_NAME_SRM_CASE_TRANSACTION_PARAMETER				= "SRM_CASE_TRANSACTION_PARAMETER";
	//SRM_案件歷史交易參數資料檔
	public static final String IATOMS_TB_NAME_SRM_HISTORY_CASE_TRANSACTION_PARAMETER		= "SRM_HISTORY_CASE_TRANSACTION_PARAMETER";
	//SRM_案件處理資料檔
	public static final String IATOMS_TB_NAME_SRM_CASE_HANDLE_INFO							= "SRM_CASE_HANDLE_INFO";
	//SRM_案件處理資料檔匯入TMS流水號  add by tonychen 20180314
	public static final String IATOMS_TB_NAME_SRM_CASE_HANDLE_INFO_BATCH_NUM				= "SRM_CASE_HANDLE_INFO_BATCH_NUM";
	//SRM_案件處理歷史資料檔
	public static final String IATOMS_TB_NAME_SRM_HISTORY_CASE_HANDLE_INFO					= "SRM_HISTORY_CASE_HANDLE_INFO";
	//SRM_案件附加檔案
	public static final String IATOMS_TB_NAME_SRM_CASE_ATT_FILE								= "SRM_CASE_ATT_FILE";	
	//SRM_求償資料檔
	public static final String IATOMS_TB_NAME_SRM_PAYMENT_INFO								= "SRM_PAYMENT_INFO";	
	//SRM_求償項目資料表
	public static final String IATOMS_TB_NAME_SRM_PAYMENT_ITEM								= "SRM_PAYMENT_ITEM";	
	//求償處理記錄檔
	public static final String IATOMS_TB_NAME_SRM_PAYMENT_TRANSCATION						= "SRM_PAYMENT_TRANSCATION";
	//SRM_案件處理記錄
	public static final String IATOMS_TB_NAME_SRM_CASE_TRANSACTION							= "SRM_CASE_TRANSACTION";	
	//SRM_案件處理中設備支援功能檔
	public static final String IATOMS_TB_NAME_SRM_CASE_ASSET_FUNCTION						= "SRM_CASE_ASSET_FUNCTION";	
	//SRM_案件處理中設備連接檔
	public static final String IATOMS_TB_NAME_SRM_CASE_ASSET_LINK							= "SRM_CASE_ASSET_LINK";
	//SRM_案件歷史設備連接檔
	public static final String IATOMS_TB_NAME_SRM_HISTORY_CASE_ASSET_LINK					= "SRM_HISTORY_CASE_ASSET_LINK";
	//工單範本
	public static final String IATOMS_TB_NAME_SRM_CASE_TEMPLATES							= "SRM_CASE_TEMPLATES";
	// SRM_案件歷史處理歷程記錄
	public static final String IATOMS_TB_NAME_SRM_HISTORY_CASE_TRANSACTION					= "SRM_HISTORY_CASE_TRANSACTION";
	// SRM_案件最新處理歷程記錄
	public static final String IATOMS_TB_NAME_SRM_CASE_NEW_TRANSACTION						= "SRM_CASE_NEW_TRANSACTION";
	//SRM_案件通訊模式維護檔
	public static final String IATOMS_TB_NAME_SRM_CASE_COMM_MODE							= "SRM_CASE_COMM_MODE";	
	//用戶欄位模板維護檔
	public static final String IATOMS_TB_NAME_SRM_QUERY_TEMPLATE							= "SRM_QUERY_TEMPLATE";	
	public static final String IATOMS_TB_NAME_SRM_CASE_NEW_HANDLE_INFO      				= "SRM_CASE_NEW_HANDLE_INFO";
	public static final String IATOMS_TB_NAME_SRM_CASE_NEW_ASSET_LINK      					= "SRM_CASE_NEW_ASSET_LINK";
	//設備借用主檔
	public static final String IATOMS_TB_NAME_DMM_ASSET_BORROW_INFO							= "DMM_ASSET_BORROW_INFO";
	//設備借用數量表
	public static final String IATOMS_TB_NAME_DMM_ASSET_BORROW_NUMBER						= "DMM_ASSET_BORROW_NUMBER";
	//設備借用列表
	public static final String IATOMS_TB_NAME_DMM_ASSET_BORROW_LIST							= "DMM_ASSET_BORROW_LIST";
	//=====================Mail Server===================================
	public static final String MAIL															= "MAIL";
	public static final String MAIL_PROPS_HOST_NAME											= "host";
	public static final String MAIL_FROM_MAIL												= "fromMail";
	public static final String MAIL_FROM_NAME 												= "fromName";
	public static final String MAIL_TO_NAME     							    			= "toName";
	public static final String MAIL_TO_MAIL     							    			= "toMail";
	public static final String MAIL_CC_MAIL     							    			= "ccMail";
	public static final String MAIL_SUBJECT     							    			= "mailSubject";
	public static final String MAIL_CONTEXT     							    			= "mailContext";
	//=================== TABLE NAME  ex:TB_NAME_tableName=============================================
	public final static String TB_WITH_NOLOCK												= " WITH(NOLOCK)";
    public final static String TB_LEFT_JION													= " LEFT JOIN";
    public final static String TB_ON														= " ON";
	
	public static final String RSB_IATOMS_TB_NAME_EMPLOYEE									= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_EMPLOYEE;//员工资料档
	public static final String RSB_IATOMS_TB_NAME_SESSION_LOGGING							= PREFIX_RS_TB_NAME + TB_NAME_SESSION_LOGGING; //使用者登入紀錄
    public static final String RSB_IATOMS_TB_NAME_ROLE										= PREFIX_RS_TB_NAME + TB_NAME_ROLE;//角色設定
    public final static String RSB_IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF  					= PREFIX_RS_TB_NAME + TB_NAME_BASE_PARAMETER_ITEM_DEF;//系统参数設定
    public final static String RSB_IATOMS_TB_NAME_FUNCTION_TYPE								= PREFIX_RS_TB_NAME + TB_NAME_FUNCTION_TYPE;//角色設定
    //使用者
  	public static final String RSB_IATOMS_TB_NAME_ADM_USER									= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_ADM_USER;
  	//使用者角色
    public static final String RSB_IATOMS_TB_NAME_ADM_ROLE									= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_ADM_ROLE;
    //使用者密碼歷史記錄
    public static final String RSB_IATOMS_TB_NAME_ADM_PWD_HISTORY							= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_ADM_PWD_HISTORY;
    
    //電文記錄
  	public static final String RSB_IATOMS_TB_NAME_API_LOG									= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_API_LOG;
	
  	//公司
  	public static final String RSB_IATOMS_TB_NAME_VENDOR									= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_VENDOR;
  	//系統參數
  	public static final String RSB_IATOMS_TB_NAME_SYSTEM_ITEM								= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_SYSTEM_ITEM;
  	//部門
  	public static final String RSB_IATOMS_TB_NAME_BIM_DEPARTMENT							= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_BIM_DEPARTMENT;
  	//合約
  	public static final String RSB_IATOMS_TB_NAME_CONTRACT									= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_CONTRACT;
  	//合約設備
  	public static final String RSB_IATOMS_TB_NAME_CONTRACT_ASSET							= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_CONTRACT_ASSET;
    //設備類型
  	public static final String RSB_IATOMS_TB_NAME_ASSET_TYPE								= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_DMM_ASSET_TYPE;
  	//客戶特店
  	public static final String RSB_IATOMS_TB_NAME_MERCHANT_NEW 								= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_MERCHANT_NEW; 
  	//特店表頭
  	public static final String RSB_IATOMS_TB_NAME_MERCHANT_HEADER 							= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_BIM_MERCHANT_HEADER; 
  	//客戶
  	public static final String RSB_IATOMS_TB_NAME_CUSTOMER 									= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_CUSTOMER; 
  	//密碼原則設定
  	public static final String RSB_IATOMS_TB_NAME_SECURITY_DEF 								= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_SECURITY_DEF;
  	//倉庫據點
  	public static final String RSB_IATOMS_TB_NAME_BIM_WAREHOUSE								= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_BIM_WAREHOUSE;
  	//系統參數維護
  	public static final String REB_IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF					= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF;
  	//公司基本訊息維護
  	public static final String REB_IATOMS_TB_NAME_COMPANY									= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_BIM_COMPANY;
  	//電子郵件羣組維護
  	public static final String RSB_IATOMS_TB_NAME_MAIL_LIST									= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_BIM_MAIL_LIST;
    //報表發送功能設定
  	public static final String REB_IATOMS_TB_NAME_REPORT_SETTING							= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_REPORT_SETTING;
  	
  	public static final String REB_IATOMS_TB_NAME_SYSTEM_LOGGING							= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_ADM_SYSTEM_LOGGING;
    //耗材品项维护
  	public static final String REB_IATOMS_TB_NAME_SUPPLIES									= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_DMM_SUPPLIES;
  	
  	//廠商使用設備檔
  	public static final String RSB_IATOMS_TB_NAME_ASSET_TYPE_COMPANY						= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_DMM_ASSET_TYPE_COMPANY;
  	//設備支援通訊模式檔
  	public static final String REB_IATOMS_TB_NAME_ASSET_SUPPORTED_COMM						= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_DMM_ASSET_SUPPORTED_COMM;
  	//設備支援功能關系檔
  	public static final String REB_IATOMS_TB_NAME_ASSET_SUPPORTED_FUNCTION					= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_DMM_ASSET_SUPPORTED_FUNCTION;
    //SLA設定
  	public static final String RSB_IATOMS_TB_NAME_SLA										= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_BIM_SLA;
  	//程式版本維護
  	public static final String RSB_IATOMS_TB_NAME_APPLICATION								= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_APPLICATION;
  	//設備入庫明細檔
  	public static final String RSB_IATOMS_TB_NAME_ASSET_IN_LIST								= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_ASSET_IN_LIST;
  	//設備轉倉作業
  	public static final String RSB_IATOMS_TB_NAME_ASSET_TRANS_INFO							= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_ASSET_TRANS_INFO;
  	//設備盤點主檔
  	public static final String RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_INFO						= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_ASSET_STOCKTACK_INFO;
    //設備盤點明細表
  	public static final String RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_LIST						= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_ASSET_STOCKTACK_LIST;
    //設備盤點種類表
  	public static final String RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_CATEGROY					= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_ASSET_STOCKTACK_CATEGROY;
    //設備盤點狀態表
  	public static final String RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_STATUS					= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_ASSET_STOCKTACK_STATUS;
  	
  	//庫存主檔
  	public static final String RSB_IATOMS_TB_NAME_DMM_REPOSITORY							= PREFIX_RS_TB_NAME +IATOMS_TB_NAME_DMM_REPOSITORY;
  	//庫存歷史檔
  	public static final String RSB_IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY					= PREFIX_RS_TB_NAME +IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY;
  	//庫存歷史檔_
  	public static final String RSB_IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY_MONTHLY			= PREFIX_RS_TB_NAME +IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY_MONTHLY;
  	public static final String RSB_IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY					= PREFIX_RS_TB_NAME +IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY;
  	
  	//刷卡機標籤管理
  	public static final String RSB_IATOMS_TB_NAME_DMM_EDC_LABEL								= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_DMM_EDC_LABEL;
  	
  	//SRM_客訴管理
  	public static final String RSB_IATOMS_TB_NAME_SRM_COMPLAINT_CASE						= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_SRM_COMPLAINT_CASE;
  	
  	//SRM_交易參數項目
  	public static final String RSB_IATOMS_TB_NAME_SRM_TRANSACTION_PARAMETER_ITEM			= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_SRM_TRANSACTION_PARAMETER_ITEM;
  	//SRM_交易參數列表
  	public static final String RSB_IATOMS_TB_NAME_SRM_TRANSACTION_PARAMETER_DETAIL			= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_SRM_TRANSACTION_PARAMETER_DETAIL;
  	//SRM_案件最新交易參數資料檔
  	public static final String RSB_IATOMS_TB_NAME_SRM_CASE_NEW_TRANSACTION_PARAMETER		= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_SRM_CASE_NEW_TRANSACTION_PARAMETER;
  	//SRM_案件處理中交易參數資料檔
  	public static final String RSB_IATOMS_TB_NAME_SRM_CASE_TRANSACTION_PARAMETER			= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_SRM_CASE_TRANSACTION_PARAMETER;
  	//SRM_案件歷史交易參數資料檔
  	public static final String RSB_IATOMS_TB_NAME_SRM_HISTORY_CASE_TRANSACTION_PARAMETER	= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_SRM_HISTORY_CASE_TRANSACTION_PARAMETER;
  	//工單範本
  	public static final String RSB_IATOMS_TB_NAME_SRM_CASE_TEMPLATES 						= PREFIX_RS_TB_NAME + IATOMS_TB_NAME_SRM_CASE_TEMPLATES; 
  	//=================================================================================
	//=================== TABLE NAME  ex:TB_NAME_tableName=============================================
    
	//=================================================================================
	/**周末*/
	public static final String PARAM_STRING_RETRIEVE										= "RETRIEVE";
	public static final String PARAM_STRING_CREATE											= "CREATE";
	public static final String PARAM_STRING_UPDATE											= "UPDATE";
	public static final String PARAM_STRING_DELETE											= "DELETE";
	public static final String PARAM_STRING_IMPORT											= "IMPORT";
	public static final String PARAM_STRING_CONTRACT_ATTACHED_FILE							= "CONTRACT_ATTACHED_FILE";
	public static final String PARAM_STRING_EXPORT											= "EXPORT";
	public static final String PARAM_STRING_CASE_FILE										= "CASE_HANDLE_FILE";
	public static final String PARAM_ACCESS_RIGHT											= "ACCESS_RIGHT";
	public static final String PARAM_SKILL_TYPE												= "SKILL_TYPE";
	public static final String PARAM_QUERY_TYPE												= "QUERY_TYPE";
	public static final String PARAM_ZERO_POINT												= " 00:00";
	public static final String PARAM_FILE_SUFFIX_TXT										= "txt";
	
	public static final String STRING_NULL 													= "null";
	public static final String PAGE_PARAM_PAGE												= "page";
	public static final String PARAM_SESSION_CONTEXT										= "sessionContext";
	public static final String PARAM_EMPLOYEE_SKILL_SESSION									= "employeeSKillsession";
	public static final String FIELD_SERVICE_ID												= "serviceId";
	public static final String FIELD_ACTION_ID												= "actionId";
	public static final String FIELD_USE_CASE_NO											= "useCaseNo";
	public static final String TAG_STATUS_ACTIVE											= "active";
	public static final String PARAM_REPEAT													= "repeat";
	public static final String STRING_TOTAL													= "total";
	public static final String STRING_GRAND													= "grand";
	public static final String STRING_EXPORT												= "exportName";
	public static final String CHECK_SETTING_PERCENT										= "PERCENT";
	public static final String CHECK_SETTING_FIXED											= "FIXED";
	public static final String CHECK_SETTING_INTERVAL										= "INTERVAL";
	
	/*****************************BPTD_CODE***********************************************************************/
	public static final String PARAM_BPTD_CODE_COMPANY										= "COMPANY";
	public static final String PARAM_BPTD_CODE_ASSET_TRANS_ID								= "ASSET_TRANS_ID";
	public static final String PARAM_BPTD_CODE_PAY_MODE										= "PAY_MODE";
	public static final String PARAM_BPTD_CODE_CONTRACT_STATUS								= "CONTRACT_STATUS";
	public static final String PARAM_BPTD_CODE_MAIL_GROUP									= "MAIL_GROUP";
	public static final String PARAM_BPTD_CODE_ASSET_CATEGORY								= "ASSET_CATEGORY";
	public static final String PARAM_BPTD_CODE_MA_TYPE										= "MA_TYPE";
	public static final String PARAM_CASE_TRANSACTION_ID									= "caseTransactionId";
	
	/*********** Excel表格列**********/
	public static final String COLUMN_A    													= "A";
	public static final String COLUMN_B    													= "B";
	public static final String COLUMN_C    													= "C";
	public static final String COLUMN_D    													= "D";
	public static final String COLUMN_E    													= "E";
	public static final String COLUMN_F    													= "F";
	public static final String COLUMN_G    													= "G";
	public static final String COLUMN_H    													= "H";
	public static final String COLUMN_I    													= "I";
	public static final String COLUMN_J    													= "J";
	public static final String COLUMN_K    													= "K";
	public static final String COLUMN_L    													= "L";
	public static final String COLUMN_M    													= "M";
	public static final String COLUMN_N    													= "N";
	public static final String COLUMN_O    													= "O";
	public static final String COLUMN_P    													= "P";
	public static final String COLUMN_Q    													= "Q";
	public static final String COLUMN_R    													= "R";
	public static final String COLUMN_V    													= "V";
	public static final String COLUMN_T    													= "T";
	public static final String COLUMN_W    													= "W";
	/*********** 定義符號**********/
  	public static final String MARK_INTERROGATION											= "?";//问号
  	public static final String MARK_BACKSLASH												= "/";//反斜线 
  	public static final String MARK_SLASH													= "\\";//反斜线
  	public static final String MARK_EQUALS													= "=";//等于号
  	public static final String MARK_UNDER_LINE												= "_";//下划线
  	public static final String MARK_MIDDLE_LINE												= "-";//中划线
  	public static final String MARK_PERCENT													= "%";//百分号
  	public static final String MARK_SEPARATOR												= ",";//逗号
  	public static final String MARK_CN_SEPARATOR											= "，";//中文逗号
  	public static final String MARK_AND														= "&";//与符号
  	public static final String MARK_EXCLAMATORY												= "!";//感嘆號
  	public static final String MARK_QUOTES													= "'";
	public static final String MARK_STOP													= "、";
	public static final String MARK_WRAP													= "\n";
	public static final String RETURN_LINE_FEED												= "\r\n";
	public static final String MARK_BRACKETS_LEFT											= "(";
	public static final String MARK_BRACKETS_RIGHT											= ")";
	public static final String MARK_COLON													= ":";//冒号
	public static final String MARK_WELLNO													="#";//井号
	public static final String MARK_NO														=".";//点号
	public static final String MARK_CENTER_LINE												="-";//点号
	public static final String MARK_CARET													= "~";
	public static final String MARK_WAVE_LINE												= "~";
	public static final String MARK_EMPTY_STRING											= "";
	public static final String MARK_EQUALANDPRE												= ">=";
	public static final String MARK_PRE														= ">";
	public static final String MARK_EQUALANDAFTER											= "<=";
	public static final String MARK_AFTER													= "<";
	public static final String MARK_EQUAL													= "==";
	public static final String MARK_WAVE_LINE_SPACE											= " ~ ";	
	public static final String MARK_SPACE													= " ";//空格
	public static final String MARK_SEMICOLON												= ";";//分号
	public static final String MARK_XXX														= "xxx";
	public static final String SINGLE_QUOTATION_MARKS										= "'"; //单引号
	public static final String MARK_ENTER													= "\r\n";
	public static final String FILE_TXT_MSEXCEL    											= "xls";
	public static final String FILE_TXT_MSEXCEL_X    										= "xlsx";
	public static final String MARK_BRACKET_LEFT											= "[";
	public static final String MARK_BRACKET_RIGHT											= "]";
	public static final String MARK_ADD														= "+";//加號
	public static final String MARK_TXT														= ".txt";//加號
	public static final String MARK_CHINESE_BRACKET_LEFT									= "【";
	public static final String MARK_CHINESE_BRACKET_RIGHT									= "】";
	public static final String MARK_DOLLAR													= "$";
	public static final String MARK_BRACES_LEFT												= "{";
	public static final String MARK_BRACES_RIGHT											= "}";
	public static final String MARK_WARNING													= "⚠";
	public static final String MARK_NBSP													= "&nbsp;";
	public static final String MARK_TAB														="	";
	public static final String MARK_MULTIPLICATION											= "*";
	public static final String MARK_CARET2													= "^";
	public static final String MARK_V														= "V";
	//自定义文字分隔符
	public static final String MARK_DELIMITER												="{^^}";
	//物流編號添加超鏈接
	public static final String MARK_DISTRIBUTION											="物流編號：";
	public static final String MARK_A_LEFT_ONE												="<aSpaceStrhref=\"";
	public static final String MARK_A_LEFT_TWO												="\">";
	public static final String MARK_A_RIGHT													="</a>";
	public static final String MARK_SPACE_STR												="SpaceStr";
	
	
	/**检视**/
	public static final String ACTION_APPROVE												= "approve";
	public static final String ACTION_REJECT												= "reject";
	public static final String ACTION_SEND													= "send";
	public static final String ACTION_LOCK													= "lock";
	public static final String ACTION_COMPLETE												= "complete";
	public static final String ACTION_CHANGE_COMPLETE_DATE									= "changeCompleteDate";
	public static final String ACTION_CHANGE_CREATE_DATE									= "changeCreateDate";
	public static final String ACTION_LEASE_PRELOAD											= "leasePreload";
	public static final String DISTRIBUTION													= "distribution";
	public static final String ACTION_REPAYMENT												= "repayment";
	public static final String ACTION_BACK													= "back";
	public static final String ACTION_INIT													= "init";
	public static final String ACTION_INIT_DTID												= "initDtid";
	public static final String ACTION_INIT_SEARCH_EMPLOYEE									= "initSearchEmployee";
	public static final String ACTION_SEARCH_EMPLOYEE										= "searchEmployee";
	public static final String ACTION_INIT_MID												= "initMid";
	public static final String ACTION_QUERY_MID												= "queryMid";
	public static final String ACTION_VIEW													= "view";
	public static final String ACTION_LIST													= "list";
	public static final String ACTION_SAVE													= "save";
	public static final String ACTION_SAVE_TRANFER_CHECK									="saveTranferCheck";
	public static final String ACTION_SAVE_ASSET_TRANS_ID									= "saveAssetTransId";
	public static final String ACTION_SAVE_MERCHANT_NEW										= "saveMerchantNew";
	public static final String ACTION_SAVE_SETTING											= "saveSetting";
	public static final String ACTION_QUERY													= "query";
	public static final String ACTION_ADD													= "add";
	public static final String ACTION_ADD_MEMO												= "addMemo";
	public static final String ACTION_EDIT													= "edit";
	public static final String ACTION_INSERT												= "insert";
	public static final String ACTION_CREATE												= "create";
	public static final String ACTION_UPDATE												= "update";
	public static final String ACTION_DELETE												= "delete";
	public static final String ACTION_INIT_SET_ROLE											= "initSetRole";
	public static final String ACTION_SETTING_ROLE											= "settingRole";
	public static final String ACTION_EXPORT												= "export";
	public static final String ACTION_EXPORT_BORROW_DETAIL									= "exportBorrowDetail";
	public static final String ACTION_EXPORT_CARD_READER									= "exportCardReaderParam";
	public static final String ACTION_VARIABLE_EXPORT										= "variableExport";
	public static final String ACTION_INIT_EDIT												= "initEdit";
	public static final String ACTION_INIT_COPY												= "initCopy";
	public static final String ACTION_INIT_VIEW												= "initView";
	public static final String ACTION_INIT_ADD												= "initAdd";
	public static final String ACTION_INIT_PRIVILEGE										= "initPrivilege";
	public static final String ACTION_INIT_SETTING											= "initSetting";
	public static final String ACTION_INIT_SEND												= "initSend";
	public static final String ACTION_PRIVILEGE_ROLE										= "privilegeRole";
	public static final String ACTION_EXPORTREPORT											= "exportReport";
	public static final String ACTION_UP_DOWN_GRADE											= "upDownGrade";
	public static final String ACTION_UP_DOWN_RANK											= "upDownRank";
	public static final String ACTION_INIT_TASKS_APPLY										= "initTasksApply";
	public static final String ACTION_SAVE_TASKS_APPLY										= "saveTasksApply";
	public static final String ACTION_START													= "start";
	public static final String ACTION_INIT_TAB												= "inittab";
	public static final String ACTION_DETAIL												= "detail";
	public static final String ACTION_HISTORY												= "history";
	public static final String ACTION_EXPORT_ASSET											= "exportAsset";
	public static final String ACTION_LOGOFF												= "logoff";
	public static final String ACTION_INIT_DETAIL											= "initDetail";
	public static final String ACTION_COPY													= "copy";
	public static final String ACTION_SAVE_DETAIL											= "saveDetail";
	public static final String ACTION_CREATE_CASE											= "createCase";
	public static final String ACTION_SAVE_CASE_TRANSACTION									= "saveCaseTransaction";
	public static final String ACTION_SIGN													= "sign";
	public static final String ACTION_QUERY_TRANSACTION										= "queryTransaction";
	public static final String ACTION_QUERY_CASE											= "queryCase";
	public static final String ACTION_ASSET_LINK_COMPANY									= "assetLinkCompany";
	public static final String ACTION_CLOSED												= "closed";
	public static final String ACTION_DISPATCH												= "dispatching";
	public static final String ACTION_IMMEDIATELY_CLOSING									= "immediatelyClosing";
	public static final String ACTION_QUERY_DTID											= "queryDTID";
	public static final String ACTION_SHOW_DETAIL_INFO										= "showDetailInfo";
	public static final String ACTION_GET_REPOSITORY_BY_ASSET_ID							= "getRepositoryByAssetId";

	public static final String ACTION_QUERY_CASE_ASSET_LINK									= "queryCaseAssetLink";
	public static final String ACTION_QUERY_CASE_SUPPLIES_LINK								= "queryCaseSuppliesLink";
	public static final String ACTION_INIT_CHOOSE_EDC										= "initChooseEDC";

	// 打開欄位
	public static final String ACTION_INIT_COLUMN_BLOCK										= "initColumnBlock";
	public static final String ACTION_SAVE_TEMPLATE											= "saveTemplate";
	
	//新增轉倉單明細集合
	public static final String ACTION_ADD_ASSET_TRANS_LIST									= "addAssetTransList";
	// 登入
	public static final String ACTION_LOGIN													= "LOGIN";
	
	public static final String ACTION_QUERY_ROLE_FUNCTION									= "queryRoleFunction";
	public static final String ACTION_DELETE_ROLE_FUNCTION									= "deleteRoleFunction";
	public static final String ACTION_SAVE_ROLE_PERMISSION									= "saveRolePermission";
	public static final String ACTION_INIT_CALENDAR_DAY										= "initDateDetail";
	public static final String ACTION_INIT_CALENDAR_YEAR									= "initYearDetail";
	public static final String ACTION_INIT_PRE_YEAR											= "initPreYear";
	public static final String ACTION_INIT_NEXT_YEAR										= "initNextYear";
	public static final String ACTION_SAVE_CALENDAR_DAY										= "saveCalendarDate";
	public static final String ACTION_SAVE_CALENDAR_YEAR									= "saveCalendarYear";
	public static final String ACTION_QUERY_INSTALL											= "queryInstall";
	public static final String ACTION_SAVE_REPOSITORY_HIST									= "saveRepositoryHist";
	public static final String ACTION_EXPORT_LIST											= "exportList";
	
	//刷卡機標籤列印
	public static final String ACTION_PRINT_EDC_LABEL										= "printEdcLabel";
	
	public static final String PARAMETER													= "parameter";
	public static final String PARAM_FLAG													= "flag";
	public static final String PARAM_BASE_PARAMETER_ITEM_DEF_SERVICE						= "baseParameterItemDefService";
	public static final String PARAM_EMAIL													= "EMAIL";
	public static final String PARAM_REMIND													= "REMIND";
	public static final String PARAM_COMMENT												= "comment";
	//ROLE.ROLE_CODE
	public static final String ROLE_CODE_HR 												= "HR";
	public static final String ROLE_CODE_IT 												= "IT";
	public static final String ROLE_CODE_PM 												= "PM";
	public static final String ROLE_CODE_SYSTEM 											= "SYSTEM";
	public static final String ROLE_CODE_MANAGER 											= "MANAGER";
	public static final String ASSET_NAME_T_SAM 											= "T-SAM";
	//====================列名===============================================================================//

	//=====================================================================================================//
	/**
	 * 域驗證
	 */
	public static final String AUTHENTICATOR_LDAP											= "LDAP";
	/**
	 * activiti登入驗證
	 */
	public static final String AUTHENTICATOR_BPM											= "BPM";
	/**
	 * 域驗證配置標識
	 */
	public static final String PARAM_LDAP_AUTH												= "LDAP_AUTH";
	/**
	 * 域驗證域服務器URL標識
	 */
	public static final String PARAM_LDAP_URL												= "ldapUrl";
	/**
	 * 域驗證安全證明標識
	 */
	public static final String PARAM_LDAP_SECURITY											= "ldapSecurity";
	/**
	 * 域驗證代理帳號標識
	 */
	public static final String PARAM_LDAP_SECURITY_PRINCIPAL								= "ldapSecurityPrincipal";
	/**
	 * 域驗證用戶級別標識
	 */
	public static final String PARAM_LDAP_LEVEL												= "ldapLevel";
	/**
	 * 域驗證網域名標識
	 */
	public static final String PARAM_DOMAIN_NAME											= "domainName";
	/**
	 * database URL
	 */
	public static final String PARAM_DATABASE_URL											= "url";
	/**
	 * database 用戶名
	 */
	public static final String PARAM_DATABASE_USERNAME										= "user";
	/**
	 * database 密碼
	 */
	public static final String PARAM_DATABASE_PWD											= "pwd";
	
	/**
	 * 帳號為終止
	 */
	public static final String ACCOUNT_STATUS_DISABLED										= "DISABLED";
	/**
	 * 帳號停權
	 */
	public static final String ACCOUNT_STATUS_STOP_RIGHT									= "STOP_RIGHT";
	/**
	 * 帳號密碼過期
	 */
	public static final String ACCOUNT_STATUS_PWD_OVERDUE									= "PWD_OVERDUE";
	/**
	 * 帳號鎖定
	 */
	public static final String ACCOUNT_STATUS_LOCK											= "LOCK";
	/**
	 * 新帳號
	 */
	public static final String ACCOUNT_STATUS_NEW											= "NEW";
	/**
	 * 帳號正常
	 */
	public static final String ACCOUNT_STATUS_NORMAL										= "NORMAL";
	
	public static final Integer PARAM_SEQUECE_LENGTH										= 6;
	/**
	 * 角色權限類型--角色
	 */
	public static final String ROLE_PERMISSION_TYPE_ROLE									= "R";
	/**
	 * 角色權限狀態--使用中
	 */
	public static final String ROLE_PERMISSION_ACTIVIT										= "A";
	/**
	 * 權限Query
	 */
	public static final String FUNCTON_ACCESS_RIGHT_QUERY									= "QUERY";
	/**
	 * 權限CREATE
	 */
	public static final String FUNCTON_ACCESS_RIGHT_CREATE									= "CREATE";
	/**
	 * 權限UPDATE
	 */
	public static final String FUNCTON_ACCESS_RIGHT_UPDATE									= "UPDATE";
	/**
	 * 權限DELETE
	 */
	public static final String FUNCTON_ACCESS_RIGHT_DELETE									= "DELETE";
	/**
	 * 權限EXPORT
	 */
	public static final String FUNCTON_ACCESS_RIGHT_EXPORT									= "EXPORT";
	
	/**
	 * 時效逾時
	 */
	public static final String CASE_PARAM_OVER_HOUR											= "OVER_HOUR";
	/**
	 * 警示逾時
	 */
	public static final String CASE_PARAM_OVER_WARNNING										= "OVER_WARNNING";
	/**
	 * 被指派的廠商
	 */
	public static final String CASE_TO_MAIL_CONPANY											= "CASE_TO_MAIL_CONPANY";
	/**
	 * 被指派的廠商部門
	 */
	public static final String CASE_TO_MAIL_DEPT											= "CASE_TO_MAIL_DEPT";
	/**
	 * 被指派的工程師
	 */
	public static final String CASE_TO_MAIL_AGENT											= "CASE_TO_MAIL_AGENT";
	/**
	 * 被指派的角色
	 */
	public static final String CASE_TO_MAIL_ROLE											= "CASE_TO_MAIL_ROLE";
	/**
	 * 建案之客服
	 */
	public static final String CASE_TO_MAIL_CREATE_USER										= "CASE_TO_MAIL_CREATE_USER";
	/**
	 * 建案AO人員
	 */
	public static final String CASE_TO_MAIL_AO_NAME											= "CASE_TO_MAIL_AO_NAME";
	/**
	 * 被指派的客服
	 */
	public static final String CASE_TO_MAIL_ROLE_CUSTOMER_SERVICE							= "CASE_TO_MAIL_ROLE_CUSTOMER_SERVICE";
	/**
	 * 有ecr
	 */
	public static final String CASE_PARAM_HAVE_ECR_LINE										= "haveEcrLine";
	/**
	 * 有延期記錄
	 */
	public static final String CASE_PARAM_HAVE_DELAY										= "haveDelay";
	/**
	 * 無延期記錄
	 */
	public static final String CASE_PARAM_NOT_HAVE_DELAY									= "notHaveDelay";
	
	/**
	 * 無ECR連線
	 */
	public static final String CASE_PARAM_NO_ECR_LINE										= "noEcrLine";
	//apLog
	public static final String FUNCTION_EXPORT												="EXPORT";
	public static final String FUNCTION_PRINT												= "PRINT";
	public static final String EXPORT_SUCCESS												="执行汇出";
	public static final String PRINT														= "执行打印";
	public static final String FUNCTION_LOGON_ID											="CE24010";
	public static final String LOGON_NAME													="登录";
	public static final String LOGON_SUCCESS												="登入成功";
	public static final String FUNCTION_PAGE_ID												="FP0001";
	public static final String FIRST_PAGE													="首页";
	public static final String QUERY_SUCCESS												="查询成功";
	public static final String COMPANY_INFO													= "公司資料檔內";
	public static final String BASE_INFO													= "基礎配置資料";
	public static final String REQUEST_MERCHANT_NEW											="merchantNew.do";
	public static final String REQUEST_MERCHANT_HEADER										="merchantHeader.do";
	public static final String DEPARTMENT_MANANGEMENT										= "department.do";
	public static final String CONTRACT_SLA													= "contractSla.do";
	public static final String ASSET_TRANS_INFO												= "assetTransfer.do";
	public static final String ASSET_STOCKTACK_HISTORY										= "assetStocktackHistory.do";
	public static final String REQUEST_CALENDAR	    										= "calendar.do";
	public static final String COMPLAINT_MANAGE												= "complaintManage.do";
	public static final String API_LOG														= "apiLog.do";
	public static final String EDC_LABEL_MANAGE												= "edcLabelManage.do";
	public static final String LOG_FILE_DOWNLOAD											= "logFileDownload.do";
	//=====================================================================================================//
	//倉庫據點新增共用
	public static final Integer PARAM_DELETED_TRUE											= 1;//已刪除
	public static final Integer PARAM_DELETED_FALSE											= 0;//未刪除
	public static final Integer PARAM_INIT_TOTAL											= 0;//初始記錄數
	public static final String PARAM_INIT_ROWS												= "";//初始行數
	
	//廠商類別--客戶
	public static final String PARAM_COMPANY_TYPE_CUSTOMER									= "CUSTOMER";
	//廠商類別--廠商
	public static final String PARAM_COMPANY_TYPE_VENDOR									= "MAINTENANCE_VENDOR";
	//廠商類別--硬體廠商
	public static final String PARAM_COMPANY_TYPE_HARDWARE_VENDOR							= "HARDWARE_VENDOR";
	public static final String REPORT_DETATIL												= "reportDetail";
	//=============================庫存狀態 || REPOSITORY_STATUS======================================================================//
	//入庫
	public static final String PARAM_YES	= "Y";
	public static final String PARAM_NO	= "N";

	public static final String PARAM_REPOSITORY_STATUS_IN_STORAGE							= "1";
	public static final String PARAM_REPOSITORY_STATUS_IN_TRANSIT							= "2";
	//一般特店
	public static final String PARAM_INSTALL_TYPE_1											= "1";
	//教育訓練機
	public static final String PARAM_INSTALL_TYPE_2											= "2";
	//微型商戶
	public static final String PARAM_INSTALL_TYPE_4											= "4";
	//彰銀公司的code
	public static final String PARAM_CHB_EDC												= "CHB_EDC";
	//台新銀行的code
	public static final String PARAM_TSB_EDC												= "TSB-EDC";
	//cyber的code
	public static final String PARAM_CYB													= "CYB";
	//環匯的code
	public static final String PARAM_GP														= "GP";
	//宣揚的code
	public static final String PARAM_BCC													= "BCC";
	//轉倉結果--確認入庫
	public static final String PARAM_TRANS_STATUS_TRANSFER_SUCCESS							= "TRANSFER_SUCCESS";
	//轉倉結果--取消轉倉
	public static final String PARAM_TRANS_STATUS_CANCEL_SUCCESS							= "CANCEL_SUCCESS";
	//=============================案件處理 設備連接檔 || ITEM_TYPE======================================================================//
	// 10-EDC
	public static final String PARAM_CASE_LINK_EDC_TYPE										= "10";
	// 11-周邊設備1
	public static final String PARAM_CASE_LINK_PERIPHERALS									= "11";
	// 12-周邊設備2
	public static final String PARAM_CASE_LINK_PERIPHERALS2									= "12";
	// 13-周邊設備3
	public static final String PARAM_CASE_LINK_PERIPHERALS3									= "13";
	// 20-耗材
	public static final String PARAM_CASE_LINK_SUPPLIES										= "20";
	//=============================設備狀態 || ASSET_STATUS======================================================================//
	// 設備狀態-領用中
	public static final String PARAM_ASSET_STATUS_IN_APPLY									= "IN_APPLY";
	// 設備狀態-維修中
	public static final String PARAM_ASSET_STATUS_REPAIR									= "REPAIR";
	// 設備狀態-已報廢
	public static final String PARAM_ASSET_STATUS_DISABLED									= "DISABLED";
	// 設備狀態-已销毁
	public static final String PARAM_ASSET_STATUS_DESTROY									= "DESTROY";
	// 設備狀態-在途中
	public static final String PARAM_ASSET_STATUS_ON_WAY									= "ON_WAY";
	// 設備狀態-庫存
	public static final String PARAM_ASSET_STATUS_REPERTORY									= "REPERTORY";
	// 設備狀態-使用中
	public static final String PARAM_ASSET_STATUS_IN_USE									= "IN_USE";
	// 設備狀態-借用中
	public static final String PARAM_ASSET_STATUS_BORROWING									= "BORROWING";
	// 設備狀態-故障
	public static final String PARAM_ASSET_STATUS_FAULT										= "FAULT";
	// 設備狀態-待報廢
	public static final String PARAM_ASSET_STATUS_PENDING_DISABLED							= "PENDING_DISABLED";
	// 設備狀態-待驗中
	public static final String PARAM_ASSET_STATUS_PENDING_CHECKED							= "PENDING_CHECKED";
	// 設備狀態-備機
	public static final String PARAM_ASSET_STATUS_BACKUP									= "BACKUP";
	// 設備狀態-已拆回
	public static final String PARAM_ASSET_STATUS_RETURNED									= "RETURNED";
	// 設備狀態-已遗失
	public static final String PARAM_ASSET_STATUS_LOST										= "LOST";
	// 設備狀態-送修中
	public static final String PARAM_ASSET_STATUS_MAINTENANCE								= "MAINTENANCE";
	// 設備狀態 拆回
	public static final String PARAM_ASSET_STATUS_REMOVE_BACK								= "REMOVE_BACK";
	// 設備狀態 取消連接
	public static final String PARAM_ASSET_STATUS_REMOVE_LINK								= "REMOVE_LINK";
	// 設備狀態 連接
	public static final String PARAM_ASSET_STATUS_LINK										= "LINK";
	// 設備狀態 列印借用單
	public static final String PARAM_ASSET_STATUS_DOWNLOAD_ZIP								= "DOWNLOAD_ZIP";
	// 設備狀態-停用中
	public static final String PARAM_ASSET_STATUS_STOP										= "STOP";
	//案件狀態 已完修 
	public static final String PARAM_COMPLET												= "COMPLET";
	
	//=============================執行作業 || ACTION ======================================================================//
	//執行作業-領用
	public static final String PARAM_ACTION_APPLY											= "APPLY";
	//執行作業-借用
	public static final String PARAM_ACTION_BORROW											= "BORROW";
	//執行作業-維修
	public static final String PARAM_ACTION_REPAIR											= "REPAIR";
	//執行作業-送修
	public static final String PARAM_ACTION_REPAIRED										= "MAINTENANCE";
	//執行作業-待報廢
	public static final String PARAM_ACTION_PENDING_DISABLED								 = "PENDING_DISABLED";
	//執行作業-確認報廢
	public static final String PARAM_ACTION_DISABLED										= "DISABLED";
	//執行作業-銷毀
	public static final String PARAM_ACTION_DESTROY											= "DESTROY";
	//執行作業-歸還
	public static final String PARAM_ACTION_BACK											= "BACK";
	//執行作業-其他修改
	public static final String PARAM_ACTION_OTHER_EDIT										= "OTHER_EDIT";
	//執行作業-入庫
	public static final String PARAM_ACTION_IN_ASSET										= "IN_ASSET";
	//執行作業-退回
	public static final String PARAM_ACTION_RETURN											= "RETURN";
	//執行作業-台新租賃
	public static final String PARAM_ACTION_TSB_RENT										= "TSB_RENT";
	//執行作業-解除綁定
	public static final String PARAM_ACTION_REMOVE_OR_LOSS									= "REMOVE";
	//執行作業-批次異動 
	public static final String PARAM_ACTION_BATCH_UPDATE									= "BATCH_UPDATE";
	//執行作業-轉倉
	public static final String PARAM_ACTION_TRANSFER										= "TRANSFER";
	//執行作業-取消轉倉
	public static final String PARAM_ACTION_CANCEL_TRANSFER									= "CANCEL_TRANSFER";
	//執行作業-轉倉入庫 
	public static final String PARAM_ACTION_TRANSFER_STORAGE								= "TRANSFER_STORAGE";
	//執行作業-捷達威維護
	public static final String PARAM_ACTION_JDW_MAINTENANCE									= "JDW_MAINTENANCE";
	//執行作業-批次異動-台新租賃
	public static final String PARAM_ACTION_BATCH_UPDATE_TSB_RENT							= "BATCH_UPDATE_TSB_RENT";
	//執行作業-批次異動-捷達威維護
	public static final String PARAM_ACTION_BATCH_UPDATE_JDW_MAINTENANCE					= "BATCH_UPDATE_JDW_MAINTENANCE";


	//=============================登入驗證方式 || AUTHENTICATION_TYPE==============================================================//
	public static final String PARAM_IATOMS_AUTHENTICATION_TYPE								= "IATOMS_AUTHEN";
	public static final String PARAM_COMPANY_NETWORK_AUTHENTICATION_TYPE					= "CYBER_AUTHEN";
	//=============================客户DTID方式 || DTID_TYPE==============================================================//
	//同TID
	public static final String PARAM_IATOMS_DTID_TYPE_SAME									= "SAME";
	//自動生成
	public static final String PARAM_IATOMS_DTID_TYPE_AUTO									= "AUTO";
	//=============================合約狀態 || CONTRACT_STATUS==============================================================//
	// 合約狀態-未生效
	public static final String PARAM_CONTRACT_STATUS_NOT_EFFECT								= "NOT_EFFECT";
	// 合約狀態-生效中
	public static final String PARAM_CONTRACT_STATUS_IN_EFFECT								= "IN_EFFECT";
	// 合約狀態-已失效
	public static final String PARAM_CONTRACT_STATUS_INVALID								= "INVALID";
	//=============================角色屬性 || ROLE_ATTRIBUTE==============================================================//
	// 角色屬性-客戶角色
	public static final String CUSTOMER_ROLE_ATTRIBUTE										= "CUSTOMER";
	// 角色屬性-廠商角色
	public static final String VECTOR_ROLE_ATTRIBUTE										= "VENDOR";
	// 角色屬性-客戶廠商
	public static final String CUSTOMER_VECTOR_ROLE_ATTRIBUTE								= "CUSTOMER_VENDOR";
	// 角色表單屬性-銀行Agent
	public static final String WORK_FLOW_ROLE_BANK_AGENT									= "BANK_AGENT";
	// 角色表單屬性-CyberAgent
	public static final String WORK_FLOW_ROLE_CYBER_AGENT									= "CYBER_AGENT";
	// 角色表單屬性-部門Agent
	public static final String WORK_FLOW_ROLE_DEPT_AGENT									= "DEPT_AGENT";
	// 角色表單屬性-廠商Agent
	public static final String WORK_FLOW_ROLE_VENDOR_AGENT									= "VENDOR_AGENT";
	// 角色表單屬性-工程師
	public static final String WORK_FLOW_ROLE_ENGINEER										= "ENGINEER";
	// 角色表單屬性-客戶廠商Agent
	public static final String WORK_FLOW_ROLE_CUS_VENDOR_AGENT								= "CUS_VENDOR_AGENT";
	// 角色表單屬性-(客戶)部門Agent
	public static final String WORK_FLOW_ROLE_CUS_DEPT_AGENT								= "CUS_DEPT_AGENT";
	// 角色表單屬性-(客戶)廠商Agent
	public static final String WORK_FLOW_ROLE_VENDOR_CUS_AGENT								= "VENDOR_CUS_AGENT";
	// 角色表單屬性-(客戶)工程師
	public static final String WORK_FLOW_ROLE_CUS_ENGINEER									= "CUS_ENGINEER";
	//=============================設備類別 || ROLE_ATTRIBUTE==============================================================//
	// 設備類別 - EDC
	public static final String 	ASSET_CATEGORY_EDC											= "EDC";
	// 設備類別 - 周邊設備
	public static final String 	ASSET_CATEGORY_RODUND_ASSET									= "Related_Products";
	public static final String 	ASSET_CATEGORY_RELATED_PRODUCTS								= "週邊設備";
	//=============================設備盤點 || ASSET_STOCKTAKE==============================================================//
	//盤點狀態
	public static final Integer PARAM_NO_STOCKTAKE											= 0; 
	public static final Integer PARAM_ALREADY_STOCKTAKE										= 1;
	public static final Integer PARAM_MORE_STOCKTAKE										= 2;
	public static final Integer PARAM_LESS_STOCKTAKE										= 3; 
	//报表费用
	public static final String PARAM_FEE_PRICE_OFFEN										= "OFFEN"; 
	public static final String PARAM_FEE_PRICE_FAST											= "FAST"; 
	public static final String PARAM_FEE_PRICE_EXTRA										= "EXTRA"; 
	//=============================維護模式 || MA_TYPE==============================================================//
	//租賃
	public static final String MA_TYPE_LEASE												= "LEASE";
	//買斷
	public static final String MA_TYPE_BUYOUT												= "BUYOUT";
	//租賃轉買斷
	public static final String MA_TYPE_LEASE_TO_BUYOUT										= "LEASE_TO_BUYOUT";
	//=============================合約類型 || CONTRACT_TYPE==============================================================//
	//租賃
	public static final String CONTRACT_TYPE_LEASE											= "LEASE";
	//採購(買斷)
	public static final String CONTRACT_TYPE_BUY											= "BUY";
	//維護
	public static final String CONTRACT_TYPE_MAINTENANCE									= "MAINTENANCE";
	//=============================處理方式 || PROCESS_TYPE==============================================================//
	//到場處理
	public static final String PROCESS_TYPE_ARRIVE_PROCESS									= "ARRIVE_PROCESS";
	//不需派工
	public static final String PROCESS_TYPE_NO_DISPATCH										= "NO_DISPATCH";
	//軟派
	public static final String PROCESS_TYPE_SOFT_DISPATCH									= "SOFT_DISPATCH";
	//=============================付款方式 || PAY_MODE==============================================================//
	//每年
	public static final String PAY_MODE_YEAR												= "YEAR";
	//每月
	public static final String PAY_MODE_MONTH												= "MONTH";
	//每天
	public static final String PAY_MODE_DAY													= "DAY";
	//=============================案件類型 || TICKET_MODE==============================================================//
	//一般
	public static final String TICKET_MODE_COMMON											= "COMMON";
	//急件
	public static final String TICKET_MODE_FAST												= "FAST";
	//特急件
	public static final String TICKET_MODE_EXTRA											= "EXTRA";
	//預約	
	public static final String TICKET_MODE_APPOINTMENT										= "APPOINTMENT";
	//=============================交易類別 || TRANSACTION_CATEGORY==============================================================//
	//一般交易
	public static final String TRANSACTION_CATEGORY_VANILLA_TRANSACTION						= "1";
	//分期交易
	public static final String TRANSACTION_CATEGORY_PERIODIZATION_TRANSACTION				= "2";
	//紅利交易	
	public static final String TRANSACTION_CATEGORY_BONUS_TRANSACTION						= "3";
	//AE
	public static final String TRANSACTION_CATEGORY_AE										= "4";
	//DINERS
	public static final String TRANSACTION_CATEGORY_DINERS									= "5";
	//DCC
	public static final String TRANSACTION_CATEGORY_DCC										= "6";
	//CUP
	public static final String TRANSACTION_CATEGORY_CUP										= "7";
	//MailOrder
	public static final String TRANSACTION_CATEGORY_MAILORDER								= "8";
	//ChoiceCard
	public static final String TRANSACTION_CATEGORY_CHOICECARD								= "9";
	//=============================月份==============================================================//
	//一月
	public static final String PARAM_MONTH_JANUARY											= "1";
	//二月
	public static final String PARAM_MONTH_FEBRUARY											= "2";
	//三月
	public static final String PARAM_MONTH_MARCH											= "3";
	//四月
	public static final String PARAM_MONTH_APRIL											= "4";
	//五月
	public static final String PARAM_MONTH_MAY												= "5";
	//六月
	public static final String PARAM_MONTH_JUNE												= "6";
	//七月
	public static final String PARAM_MONTH_JULY												= "7";
	//八月
	public static final String PARAM_MONTH_AUGUST											= "8";
	//九月
	public static final String PARAM_MONTH_SEPTEMBER										= "9";
	//十月
	public static final String PARAM_MONTH_OCTOBER											= "10";
	//十一月
	public static final String PARAM_MONTH_NOVEMBER											= "11";
	//十二月
	public static final String PARAM_MONTH_DECEMBER											= "12";
	//=============================案件類別 || TICKET_TYPE==============================================================//
	//裝機
	public static final String TICKET_TYPE_INSTALLED										= "INSTALLED";
	//拼機
	public static final String TICKET_TYPE_MACHINE											= "MACHINE";
	//異動	
	public static final String TICKET_TYPE_TRANSACTION										= "TRANSACTION";
	//查核
	public static final String TICKET_TYPE_CHECK											= "CHECK";
	//拆機
	public static final String TICKET_TYPE_DISASSEMBLE										= "DISASSEMBLE";
	//報修
	public static final String TICKET_TYPE_REPAIR											= "REPAIR";
	//專案
	public static final String TICKET_TYPE_PROJECT											= "PROJECT";
	
	// 裝機縮寫 生成案件編號時使用
	public static final String TICKET_TYPE_INSTALLED_ABBREVIATE								= "I";
	// 併機縮寫 生成案件編號時使用
	public static final String TICKET_TYPE_MACHINE_ABBREVIATE								= "C";
	// 異動縮寫 生成案件編號時使用
	public static final String TICKET_TYPE_UPDATE_ABBREVIATE								= "M";
	// 查核縮寫 生成案件編號時使用
	public static final String TICKET_TYPE_CHECK_ABBREVIATE									= "A";
	// 拆機縮寫 生成案件編號時使用
	public static final String TICKET_TYPE_UNINSTALL_ABBREVIATE								= "U";
	// 報修縮寫 生成案件編號時使用
	public static final String TICKET_TYPE_REPAIR_ABBREVIATE								= "R";
	// 專案縮寫 生成案件編號時使用
	public static final String TICKET_TYPE_PROJECT_ABBREVIATE								= "P";
	// 其他縮寫 生成案件編號時使用
	public static final String TICKET_TYPE_OTHER_ABBREVIATE									= "O";
	
	// 客訴案件 生成案件編號時使用
	public static final String TICKET_TYPE_COMPLAINT										= "C";
	//=============================支援功能 || SUPPORTED_FUNCTION==============================================================//
	//Pinpad
	public static final String SUPPORTED_FUNCTION_PINPAD									= "Pinpad";
	//Dongle
	public static final String SUPPORTED_FUNCTION_DONGLE									= "Dongle";
	//電子簽名
	public static final String SUPPORTED_FUNCTION_ELECTRONIC_SIGNATURE						= "Electronic_Signature";
	//密碼原則設定
	public static final String UPDATED_DATE													= "UpdatedDate";
	//密碼原則設定	
	public static final String UPDATED_BY_NAME												= "UpdatedByName";
	//設備類別EDC list
	public static final String ASSET_EDC_LIST												= "assetEDCList";
	//設備類別周旁設備list
	public static final String ASSET_LIST													= "assetList";
	//設備類別list
	public static final String ASSET_CATEGORY_KEY_LIST										= "assetCategoryKeyList";
	//要傳到前台的json數據
	public static final String JSON_DATA													= "jsonData";
	//客戶設備總表匯出設備類別
	public static final String COLUMN_NAME_TYPE_EDC											= "columnNameTypeEdc";
	//客戶設備總表匯出設備類別
	public static final String COLUMN_NAME_TYPE												= "columnNameType";
	//總條數
	public static final String COUNT														= "count";
	
	//=============================資料狀態 || DATA_STATUS==============================================================//
	//新增
	public static final String DATA_STATUS_CREATE											= "CREATE";
	//待維修
	public static final String DATA_STATUS_TO_BE_MAINTAINED									= "WAIT_REPAIR";
	//待確認金額
	public static final String DATA_STATUS_AMOUNT_TO_BE_CONFIRMED							= "WAIT_CONFIRM_MONEY";
	//待償確認
	public static final String DATA_STATUS_WAIT_CLAIM_CONFIRM								= "WAIT_PAY_CONFIRM";
	//求償確認
	public static final String DATA_STATUS_CLAIM_CONFIRM									= "PAY_CONFIRM";
	//已請款	
	public static final String DATA_STATUS_REQUEST_FUNDS									= "REQUEST_FUNDS";
	//已取消
	public static final String DATA_STATUS_ALREADY_CANCEL									= "ALREADY_CANCEL";
	//已還款
	public static final String DATA_STATUS_ALREADY_REPAYMENT								= "ALREADY_REPAYMENT";
	//退回
	public static final String DATA_STATUS_BACK												= "BACK";
	//已完成-不需在列表進行顯示
	public static final String DATA_STATUS_COMPLETE											= "COMPLETE";
	
	//=============================求償項目 || PAYMENT_ITEM==============================================================//
	//設備
	public static final String PAYMENT_ITEM_ASSET											= "PAYMENT_ASSET";
	//耗材
	public static final String PAYMENT_ITEM_SUPPLIES										= "PAYMENT_SUPPLIES";
	//=============================求償作業--角色屬性==============================================================//
	//客服角色
	public static final String CUSTOMER_SERVICE												= "PAYMENT_SERVICE";
	//倉管角色
	public static final String WAREHOUSE_KEEPER												= "WAREHOUSE_KEEPER";
	//帳務角色
	public static final String ACCOUNTING													= "ACCOUNTING";
	//=============================求償作業--賠償方式==============================================================//
	//客戶賠償
	public static final String CUSTOMER_PAYMENT												= "CUSTOMER_PAYMENT";
	//特店賠償
	public static final String MERCHANT_PAYMENT												= "MERCHANT_PAYMENT";
	//自行吸收
	public static final String SELF_ABSORB													= "SELF_ABSORB";
	//檢測正常
	public static final String DETECTION_NORMAL												= "DETECTION_NORMAL";
	
	//=============================案件歷史設備連接檔-動作==============================================================//
	//已拆回
	public static final String ACTION_REMOVE												= "REMOVE";
	//遺失
	public static final String ACTION_LOSS													= "LOSS";
	//=============================分類條件 ==============================================================//
	public static final String ORDER_BY_CASE_CATEGORY										= "CASE_CATEGORY";
	public static final String ORDER_BY_DEPT_CODE											= "DEPT_CODE";
	public static final String ORDER_BY_CONTRACT_CODE										= "CONTRACT_CODE";
	//=============================EDC流通在外台數報表 常量定義 ==============================================================//
	//子報表1模板
	public static final String EDC_OUTSTANDING_NUM_REPORT_SUBREPORT1						= "EDC_OUTSTANDING_NUM_REPORT_subreport1";
	//子報表2模板
	public static final String EDC_OUTSTANDING_NUM_REPORT_SUBREPORT2						= "EDC_OUTSTANDING_NUM_REPORT_subreport2";
	public static final String EDC_OUTSTANDING_NUM_REPORT_SUBREPORT_DIR						= "SUBREPORT_DIR";
	public static final String EDC_OUTSTANDING_NUM_REPORT_SUBREPORT_DIR1					= "SUBREPORT_DIR_1";
	//主報表名稱--英文
	public static final String EDC_OUTSTANDING_NUM_REPORT_EN_NAME							= "EDC_OUTSTANDING_NUM_REPORT";
	//報表中文名稱
	public static final String EDC_OUTSTANDING_NUM_REPORT_CH_NAME							= "EDC_OUTSTANDING_NUM_REPORT_NAME";
	//=============================未完修報表(大眾格式) 常量定義 ==============================================================//
	//報表名稱
	public static final String UN_FINISHED_REPORT											= "UN_FINISHED_REPORT";
	//報表中文名稱
	public static final String UN_FINISHED_REPORT_CH_NAME									= "UN_FINISHED_REPORT_NAME";
	//sheetName
	public static final String UN_FINISHED_REPORT_SHEET_NAME								= "UN_FINISHED_REPORT_SHEET_NAME";
	//=============================未完修報表(環匯格式) 常量定義 ==============================================================//
	//報表名稱
	public static final String UN_FINISHED_REPORT_TO_GP										= "UN_FINISHED_REPORT_TO_GP";
	//子報表名稱
	public static final String UN_FINISHED_REPORT_TO_GP_SUBREPORT_1							= "UN_FINISHED_REPORT_TO_GP_subreport1";
	public static final String UN_FINISHED_REPORT_TO_GP_SUBREPORT_DIR						= "SUBREPORT_DIR";
	//=============================完工回覆檔(環匯格式) 常量定義 ==============================================================//
	//報表名稱
	public static final String FINISHED_REPLY_TO_GP_SUBJECT_CH_NAME							= "FINISHED_REPLY_REPORT_TO_OFB";
	//=============================完工回覆檔(大眾格式) 常量定義 ==============================================================//
	//報表名稱
	public static final String FINISHED_REPLY_TO_TCB_SUBJECT_CH_NAME						= "FINISHED_REPLY_TO_TCB_SUBJECT_CH_NAME";
	//sheetName費用總表
	public static final String FEE_PRICE_SHEET_CH_NAME										= "FEE_PRICE_SHEET_CH_NAME";
	//=============================完工回覆檔(歐付寶格式) 常量定義 ==============================================================//
	//報表名稱
	public static final String FINISHED_REPLY_REPORT_TO_OFB_NAME							= "FINISHED_REPLY_REPORT_TO_OFB_CH_NAME";
	
	//=============================案件報修明細(環匯格式) 常量定義============================================================//
	//報表中文名稱
	public static final String CASE_REPAIR_DETAIL_REPORT_TO_GP_NAME							= "CASE_REPAIR_DETAIL_REPORT_TO_GP_NAME";
	//主報表名稱
	public static final String CASE_REPAIR_DETAIL_REPORT_TO_GP								= "CASE_REPAIR_DETAIL_REPORT_TO_GP";
	//子報表名稱
	public static final String CASE_REPAIR_DETAIL_REPORT_TO_GP_SUBREPORTS					= "CASE_REPAIR_DETAIL_REPORT_TO_GP_subreports";
	//子報表名稱
	public static final String CASE_REPAIR_DETAIL_REPORT									= "CASE_REPAIR_DETAIL_REPORT_TO_GP_subreports";
	public static final String CASE_REPAIR_DETAIL_REPORT_SUBREPORT_DIR						= "SUBREPORT_DIR";
	//============================案件設備明細(環匯格式) 常量定義=============================================================//
	//報表中文名稱
	public static final String CASE_ASSET_DETAIL_REPORT_TO_GP_NAME							= "CASE_ASSET_DETAIL_REPORT_TO_GP_NAME";
	//主報表名稱
	public static final String CASE_ASSET_DETAIL_REPORT_TO_GP								= "CASE_ASSET_DETAIL_REPORT_TO_GP";
	//子報表名稱
	public static final String CASE_ASSET_DETAIL_REPORT										= "CASE_ASSET_DETAIL_REPORT_TO_GP_subreport1";
	public static final String CASE_ASSET_DETAIL_REPORT_SUBREPORT_DIR						= "SUBREPORT_DIR";
	//郵件中附件名中 -- 月份
	public static final String CASE_ASSET_DETAIL_REPORT_MAIL_MONTH							= "CASE_ASSET_DETAIL_REPORT_MAIL_MONTH";
	//郵件中附件名中 -- 上半月
	public static final String CASE_ASSET_DETAIL_REPORT_MAIL_FIRST_HALF						= "CASE_ASSET_DETAIL_REPORT_MAIL_FIRST_HALF";
	//郵件中附件名中 -- 下半月
	public static final String CASE_ASSET_DETAIL_REPORT_MAIL_SECOND_HALF					= "CASE_ASSET_DETAIL_REPORT_MAIL_SECOND_HALF";
	//=============================維護費報表(大眾格式) 常量定義=========================================================//
	//(大眾格式)價格jrxml名稱
	public static final String PRICE_SUM_REPORT_CH_NAME										= "PRICE_SUM_REPORT";
	//(大眾格式)設備jrxml名稱.
	public static final String ASSET_INFO_CH_NAME											= "ASSET_INFO";
	//(大眾格式)設備jrxml名稱.
	public static final String ASSET_INFO_NO3G_CH_NAME										= "ASSET_INFO_NO3G";
	//(大眾格式)案件jrxml
	public static final String FEE_CASE_INFO_REPORT_CH_NAME									= "FEE_CASE_INFO_REPORT";
	//(大眾格式)維修耗材費用-AO已/未回覆扣款方式
	public static final String FEE_PAYMENT_INFO_REPORT_CH_NAME								= "FEE_PAYMENT_INFO_REPORT";
	//(大眾格式)報表名稱.
	public static final String EDC_FEE_REPORT_FOR_TCB_SUBJECT_CH_NAME						= "EDC_FEE_REPORT_FOR_TCB_SUBJECT_CH_NAME";
	//=============================維護費報表(綠界格式) 常量定義=========================================================//
	//(綠界格式)總表jrxml名稱
	public static final String GREEN_WORLD_PRICE_SUM_REPORT_CH_NAME							= "GREEN_WORLD_PRICE_SUM_REPORT";
	public static final String GREEN_WORLD_TX_PRICE_SUM_REPORT_CH_NAME						= "GREEN_WORLD_TX_PRICE_SUM_REPORT";
	//報表名稱.維護費明細
	public static final String FEE_REPORT_FOR_GREEN_WORLD_SUBJECT_CH_NAME					= "FEE_REPORT_FOR_GREEN_WORLD_SUBJECT_CH_NAME";
	//(綠界格式)台新設備維護費
	public static final String GREEN_WORLD_TX_ASSET_MAINTAIN								= "GREEN_WORLD_TX_ASSET_MAINTAIN";
	//(綠界格式)台新作業費
	public static final String GREEN_WORLD_TX_CASE_INFO_REPORT								= "GREEN_WORLD_TX_CASE_INFO_REPORT";
	//(綠界格式) 設備明細
	public static final String GREEN_WORLD_ASSET_INFO										= "GREEN_WORLD_ASSET_INFO";
	//(綠界格式) 聊天設備明細
	public static final String GREEN_WORLD_CHAT_ASSET										= "GREEN_WORLD_CHAT_ASSET";
	//(綠界格式) *月作業明細
	public static final String GREEN_WORLD_TX_MONTH_CASE_INFO_REPORT						= "GREEN_WORLD_TX_MONTH_CASE_INFO_REPORT";
	//=============================維護費報表(陽信格式) 常量定義=========================================================//
	//(陽信格式)總表第一個報表jrxml名稱
	public static final String SYB_PRICE_SUM_REPORT 										= "SYB_PRICE_SUM_REPORT";
	//設備明細(支持3G、GPRS、Bluetooth(3G/WIFI)、音源孔(3G/WIFI))
	public static final String SYB_ASSET_INFO												= "SYB_ASSET_INFO";
	//設備明細設備明細(不支持3G、GPRS、Bluetooth(3G/WIFI)、音源孔(3G/WIFI))
	public static final String SYB_NO_3G_ASSET_INFO											= "SYB_NO_3G_ASSET_INFO";
	//*維護費設備
	public static final String SYB_ASSET_MAINTAIN											= "SYB_ASSET_MAINTAIN";
	//*月作業明細
	public static final String SYB_CASE_INFO_REPORT											= "SYB_CASE_INFO_REPORT";
	//=============================維護費報表(上銀格式) 常量定義=========================================================//
	//(上銀格式)總表第一個報表jrxml名稱
	public static final String SCSB_PRICE_SUM_REPORT 										= "SCSB_PRICE_SUM_REPORT";
	//(上銀格式)總表第二個字報表jrxml名稱
	public static final String SCSB_TX_PRICE_SUM_SUB_REPORT3_CH_NAME						= "SCSB_PRICE_SUM_REPORT_subreport3";
	//(上銀格式)總表第一個字報表jrxml名稱
	public static final String SCSB_TX_PRICE_SUM_SUB_REPORT4_CH_NAME						= "SCSB_PRICE_SUM_REPORT_subreport4";
	//主報表裡面子報表的名字
	public static final String SUBREPORT_DIR3												= "SUBREPORT_DIR3";
	//主報表裡面子報表的名字
	public static final String SUBREPORT_DIR4												= "SUBREPORT_DIR4";
	//設備明細
	public static final String SCSB_ASSET_INFO												= "SCSB_ASSET_INFO";
	//*月作業明細
	public static final String SCSB_CASE_INFO_REPORT										= "SCSB_CASE_INFO_REPORT";
	//*維護費設備
	public static final String SCSB_MAINTAIN_ASSET_INFO										= "SCSB_MAINTAIN_ASSET_INFO";
	//=============================维护费报表(捷达威格式) 常量定義=========================================================//
	//費用總表名稱
	public static final String EDC_FEE_REPORT_FOR_JDW_WORK_FEE								= "EDC_FEE_REPORT_FOR_JDW_WORK_FEE";
	//作業明細名稱
	public static final String EDC_FEE_REPORT_FOR_JDW_WORK_DETAILS							= "EDC_FEE_REPORT_FOR_JDW_WORK_DETAILS";
	//維護費設備名稱
	public static final String EDC_FEE_REPORT_FOR_JDW_MAINTENANCE_EQUIPMENT					= "EDC_FEE_REPORT_FOR_JDW_MAINTENANCE_EQUIPMENT";
	//報表名稱
	public static final String EDC_FEE_REPORT_FOR_JDW_NAME									= "EDC_FEE_REPORT_FOR_JDW_NAME";
	//=============================維護費報表(彰銀格式) 常量定義=========================================================//
	//(彰銀格式)案件信息
	public static final String CHB_CASE_INFO_REPORT											= "CHB_CASE_INFO_REPORT";
	//(彰銀格式)設備信息
	public static final String CHB_ASSET_INFO												= "CHB_ASSET_INFO";
	//(彰銀格式)維護費設備
	public static final String CHB_ASSET_MAINTAIN											= "CHB_ASSET_MAINTAIN";

	//=============================维护费报表(環匯格式) 常量定義=========================================================//
	//費用總表名稱
	public static final String EDC_FEE_REPORT_FOR_GP_WORK_FEE								= "EDC_FEE_REPORT_FOR_GP_WORK_FEE";
	//啟用設備明細報表
	public static final String EDC_FEE_REPORT_FOR_GP_ENABLE_ASSET_INFO						= "EDC_FEE_REPORT_FOR_GP_ENABLE_ASSET_INFO";
	//作業明細
	public static final String EDC_FEE_REPORT_FOR_GP_WORK_DETAIL							= "EDC_FEE_REPORT_FOR_GP_WORK_DETAIL";
	//報廢機明細
	public static final String EDC_FEE_REPORT_FOR_GP_SCRAP_ASSET							= "EDC_FEE_REPORT_FOR_GP_SCRAP_ASSET";
	//(環匯格式)維修耗材費用-AO已/未回覆扣款方式
	public static final String EDC_FEE_REPORT_FOR_GP_PAYMENT_INFO							= "EDC_FEE_REPORT_FOR_GP_PAYMENT_INFO";
	//維修耗材費用-AO已回覆扣款方式
	public static final String EDC_FEE_REPORT_FOR_GP_PAYMENT_INFO_IS_REPLY					= "EDC_FEE_REPORT_FOR_GP_PAYMENT_INFO_IS_REPLY";
	//維修耗材費用-AO未回覆扣款方式
	public static final String EDC_FEE_REPORT_FOR_GP_PAYMENT_INFO_IS_NOT_REPLY				= "EDC_FEE_REPORT_FOR_GP_PAYMENT_INFO_IS_NOT_REPLY";
	//報表名稱
	public static final String EDC_FEE_REPORT_FOR_GP_NAME									= "EDC_FEE_REPORT_FOR_GP_NAME";
	//============================案件設備明細(環匯格式) 常量定義=============================================================//
	//10-EDC
	public static final String CASE_ASSET_LINK_ITEM_TYPE_EDC								= "10";
	//11-周邊設備1
	public static final String CASE_ASSET_LINK_ITEM_TYPE_ONE								= "11";
	//12-周邊設備2
	public static final String CASE_ASSET_LINK_ITEM_TYPE_TWO								= "12";
	//13-周邊設備3
	public static final String CASE_ASSET_LINK_ITEM_TYPE_THREE								= "13";
	//20-耗材
	public static final String CASE_ASSET_LINK_ITEM_TYPE_SUPPLIES							= "20";
	//=============================求償作業 -- service 常量定義=========================================================//
	//修改
	public static final String PAYMENT_UPDATE												= "PAYMENT_UPDATE";
	//檢測結果
	public static final String PAYMENT_CHECK_RESULT											= "PAYMENT_CHECK_RESULT";
	//檢測人
	public static final String PAYMENT_CHECK_USER											= "PAYMENT_CHECK_USER";
	//求償費用
	public static final String PAYMENT_FEE													= "PAYMENT_FEE";
	//求償費用說明
	public static final String PAYMENT_FEE_DESC												= "PAYMENT_FEE_DESC";
	//送出
	public static final String PAYMENT_SEND													= "PAYMENT_SEND";
	//送出
	public static final String PAYMENT_BACK													= "PAYMENT_BACK";
	//是否需請款
	public static final String PAYMENT_IS_ASK_PAY											= "PAYMENT_IS_ASK_PAY";
	//求償方式
	public static final String PAYMENT_PAYMENT_TYPE											= "PAYMENT_PAYMENT_TYPE";
	//求償方式說明
	public static final String PAYMENT_PAYMENT_TYPE_DESC									= "PAYMENT_PAYMENT_TYPE_DESC";
	//退回說明
	public static final String PAYMENT_BACK_DESC											= "PAYMENT_BACK_DESC";
	//客服
	public static final String PAYMENT_CUSTOMER												= "PAYMENT_CUSTOMER";
	//帳務
	public static final String PAYMENT_ACCOUNTING											= "PAYMENT_ACCOUNTING";
	//倉管
	public static final String PAYMENT_WAREHOUSE_KEEPER										= "PAYMENT_WAREHOUSE_KEEPER";
	//請款時間
	public static final String PAYMENT_ASKPAY_DATE											= "PAYMENT_ASKPAY_DATE";
	//取消時間
	public static final String PAYMENT_CANCEL_DATE											= "PAYMENT_CANCEL_DATE";
	//還款時間
	public static final String PAYMENT_REPAYMENT_DATE										= "PAYMENT_REPAYMENT_DATE";
	//取消說明
	public static final String PAYMENT_CANCEL_DESC											= "PAYMENT_CANCEL_DESC";
	//完成
	public static final String PAYMENT_COMPLETE												= "PAYMENT_COMPLETE";
	//=============================交易參數 -- jsp使用 常量定義=========================================================//
	//條目類型-combobox
	public static final String PARAMTER_ITEM_TYPE_COMBOBOX									= "combobox";
	//條目類型-combobox
	public static final String PARAMTER_ITEM_TYPE_CLICK										= "click";
	//條目code--MERCHANT_CODE_OTHER
	public static final String PARAMTER_ITEM_CODE_MERCHANT_CODE_OTHER						= "MID2";
	//條目code--MERCHANT_CODE
	public static final String PARAMTER_ITEM_CODE_MERCHANT_CODE								= "MID";
	//條目code--TID
	public static final String PARAMTER_ITEM_CODE_TID										= "TID";
	//條目code--DTID
	public static final String PARAMTER_ITEM_CODE_DTID										= "DTID";
	//=============================LOGO -- jsp使用 常量定義=========================================================//
	//僅LOGO
	public static final String PARAMTER_CASE_LOGO_ONLY_LOGO									= "ONLY_LOGO";
	//LOGO+表頭
	public static final String PARAMTER_CASE_LOGO_LOGO_AND_MERCHANT_HEADER					= "LOGO_AND_MERCHANT_HEADER";
	//僅表頭
	public static final String PARAMTER_CASE_LOGO_ONLY_MERCHANT_HEADER						= "ONLY_MERCHANT_HEADER";
	//=============================CODE -- 捷达威 公司CODE=========================================================//
	public static final String PARAMTER_COMPANY_CODE_JDW_EDC								= "JDW-EDC";
	//=============================CODE -- 維護費報表(環匯格式)=========================================================//
	public static final String PARAMTER_COMPANY_CODE_GP_EDC									= "GP";
	//=============================CODE -- 維護費報表(綠界格式)=========================================================//
	public static final String PARAMTER_COMPANY_CODE_GREEN_WORLD							= "GreenWorld";
	//=============================CODE -- 維護費報表(大眾格式)=========================================================//
	public static final String PARAMTER_COMPANY_CODE_TCB_EDC								= "TCB_EDC";
	//=============================CODE -- 維護費報表(上銀格式)=========================================================//
	public static final String PARAMTER_COMPANY_CODE_SCSB									= "SCSB";
	//=============================CODE -- 維護費報表(陽信格式)=========================================================//
	public static final String PARAMTER_COMPANY_CODE_SYB									= "SYB-EDC";
	//=============================CODE -- 維護費報表(張銀格式)=========================================================//
	public static final String PARAMTER_COMPANY_CODE_CHB									= "CHB_EDC";
	//=============================REPORT_CODE -- 報表種類=========================================================//
	//维护费报表(捷达威格式)
	public static final String REPORT_NAME_EDC_FEE_FOR_JDW									= "feeReportForJdw";
	//維護費報表(環匯格式)
	public static final String FEE_REPORT_FOR_GP											= "feeReportForGp";
	//----------------------------表字段常量--------------------------------------------------------------------------//
	public static final String FIELD_ADM_USER_ACCOUNT										= "ADM_USER_ACCOUNT";
	public static final String FIELD_ADM_USER_COMPANY										= "ADM_USER_COMPANY";
	public static final String FIELD_ADM_USER_CNAME											= "ADM_USER_CNAME";
	public static final String FIELD_ADM_USER_ENAME											= "ADM_USER_ENAME";
	public static final String FIELD_ADM_USER_PWD											= "ADM_USER_PASSWORD";
	public static final String FIELD_ADM_USER_REPWD											= "ADM_USER_REPASSWORD";
	public static final String FIELD_ADM_USER_MOBILE										= "ADM_USER_MOBILE";
	public static final String FIELD_ADM_USER_PHONE											= "ADM_USER_PHONE";
	public static final String FIELD_ADM_USER_EMAIL											= "ADM_USER_EMAIL";
	public static final String FIELD_ADM_USER_MANAGER_EMAIL									= "ADM_USER_MANAGER_EMAIL";
	public static final String FIELD_ADM_USER_DESC											= "ADM_USER_DESC";
	
	public static final String FIELD_ADM_ROLE_ROLE_CODE										= "ADM_ROLE_ROLE_CODE";
	public static final String FIELD_ADM_ROLE_ROLE_NAME										= "ADM_ROLE_ROLE_NAME";
	public static final String FIELD_ADM_ROLE_ROLE_DESC										= "ADM_ROLE_ROLE_DESC";
	
	public static final String FIELD_BIM_MAIL_LIST_MAIL_GROUP								= "BIM_MAIL_LIST_MAIL_GROUP";
	public static final String FIELD_BIM_MAIL_LIST_NAME										= "BIM_MAIL_LIST_NAME";
	public static final String FIELD_BIM_MAIL_LIST_EMAIL									= "BIM_MAIL_LIST_EMAIL";
		
	public static final String FIELD_BIM_CONTRACT_COMPANY_ID								= "BIM_CONTRACT_COMPANY_ID";
	public static final String FIELD_BIM_CONTRACT_CONTRACT_CODE								= "BIM_CONTRACT_CONTRACT_CODE";
	public static final String FIELD_BIM_CONTRACT_CONTRACT_PRICE							= "BIM_CONTRACT_CONTRACT_PRICE";
	public static final String FIELD_BIM_CONTRACT_FACTORY_WARRANTY							= "BIM_CONTRACT_FACTORY_WARRANTY";
	public static final String FIELD_BIM_CONTRACT_CUSTOMER_WARRANTY							= "BIM_CONTRACT_CUSTOMER_WARRANTY";
	public static final String FIELD_BIM_CONTRACT_WORK_HOUR_START_1							= "BIM_CONTRACT_WORK_HOUR_START_1";
	public static final String FIELD_BIM_CONTRACT_WORK_HOUR_START_2							= "BIM_CONTRACT_WORK_HOUR_START_2";
	public static final String FIELD_BIM_CONTRACT_WORK_HOUR_END_1							= "BIM_CONTRACT_WORK_HOUR_END_1";
	public static final String FIELD_BIM_CONTRACT_WORK_HOUR_END_2							= "BIM_CONTRACT_WORK_HOUR_END_2";
	public static final String FIELD_BIM_CONTRACT_START_DATE								= "BIM_CONTRACT_START_DATE";
	public static final String FIELD_BIM_CONTRACT_END_DATE									= "BIM_CONTRACT_END_DATE";
	public static final String FIELD_BIM_CONTRACT_PAY_REQUIRE								= "BIM_CONTRACT_PAY_REQUIRE";
	public static final String FIELD_BIM_CONTRACT_COMMENT									= "BIM_CONTRACT_COMMENT";
	public static final String FIELD_BIM_CONTRACT_ASSET_ASSET_TYPE_ID						= "BIM_CONTRACT_ASSET_ASSET_TYPE_ID";
	public static final String FIELD_BIM_CONTRACT_ASSET_AMOUNT								= "BIM_CONTRACT_ASSET_AMOUNT";
	public static final String FIELD_BIM_CONTRACT_ASSET_SAFETY_STOCK						= "BIM_CONTRACT_ASSET_SAFETY_STOCK";
	public static final String FIELD_BIM_CONTRACT_WINDOW1_CONNECTION						= "BIM_CONTRACT_WINDOW1_CONNECTION";
	public static final String FIELD_BIM_CONTRACT_WINDOW1									= "BIM_CONTRACT_WINDOW1";
	public static final String FIELD_BIM_CONTRACT_WINDOW2_CONNECTION						= "BIM_CONTRACT_WINDOW2_CONNECTION";
	public static final String FIELD_BIM_CONTRACT_WINDOW2									= "BIM_CONTRACT_WINDOW2";
	
	public static final String FIELD_DMM_ASSET_TYPE_ASSET_CATEGORY							= "DMM_ASSET_TYPE_ASSET_CATEGORY";
	
	public static final String FIELD_BIM_COMPANY_SHORT_NAME									= "BIM_COMPANY_SHORT_NAME";
	public static final String FIELD_BIM_COMPANY_TYPE_COMPANY_TYPE							= "BIM_COMPANY_TYPE_COMPANY_TYPE";
	public static final String FIELD_BIM_COMPANY_TYPE_COMPANY_CODE							= "BIM_COMPANY_TYPE_COMPANY_CODE";
	public static final String FIELD_BIM_COMPANY_UNITY_NUMBER								= "BIM_COMPANY_UNITY_NUMBER";
	public static final String FIELD_BIM_COMPANY_INVOICE_HEADER								= "BIM_COMPANY_INVOICE_HEADER";
	public static final String FIELD_BIM_COMPANY_LEADER										= "BIM_COMPANY_LEADER";
	public static final String FIELD_BIM_COMPANY_TEL										= "BIM_COMPANY_TEL";
	public static final String FIELD_BIM_COMPANY_FAX										= "BIM_COMPANY_FAX";
	public static final String FIELD_BIM_COMPANY_APPLY_DATE									= "BIM_COMPANY_APPLY_DATE";
	public static final String FIELD_BIM_COMPANY_PAY_DATE									= "BIM_COMPANY_PAY_DATE";
	public static final String FIELD_BIM_COMPANY_CONTACT									= "BIM_COMPANY_CONTACT";
	public static final String FIELD_BIM_COMPANY_CONTACT_TEL								= "BIM_COMPANY_CONTACT_TEL";
	public static final String FIELD_BIM_COMPANY_CONTACT_EMAIL								= "BIM_COMPANY_CONTACT_EMAIL";
	public static final String FIELD_BIM_COMPANY_COMPANY_EMAIL								= "BIM_COMPANY_COMPANY_EMAIL";
	public static final String FIELD_BIM_COMPANY_AUTHENTICATION_TYPE						= "BIM_COMPANY_AUTHENTICATION_TYPE";
	public static final String FIELD_BIM_COMPANY_CUSTOMER_CODE								= "BIM_COMPANY_CUSTOMER_CODE";
	public static final String FIELD_BIM_COMPANY_DTID_TYPE									= "BIM_COMPANY_DTID_TYPE";
	public static final String FIELD_BIM_COMPANY_COMPANY_ADDRESS							= "BIM_COMPANY_COMPANY_ADDRESS";
	public static final String FIELD_BIM_COMPANY_INVOICE_ADDRESS							= "BIM_COMPANY_INVOICE_ADDRESS";
	public static final String FIELD_BIM_COMPANY_REMARK										= "BIM_COMPANY_REMARK";
	
	public static final String FIELD_BIM_WAREHOUSE_COMPANY_ID								= "BIM_WAREHOUSE_COMPANY_ID";
	public static final String FIELD_BIM_WAREHOUSE_NAME										= "BIM_WAREHOUSE_NAME";
	public static final String FIELD_BIM_WAREHOUSE_CONTACT									= "BIM_WAREHOUSE_CONTACT";
	public static final String FIELD_BIM_WAREHOUSE_TEL 										= "BIM_WAREHOUSE_TEL";
	public static final String FIELD_BIM_WAREHOUSE_FAX 										= "BIM_WAREHOUSE_FAX";
	public static final String FIELD_BIM_WAREHOUSE_LOCATION									= "BIM_WAREHOUSE_LOCATION";
	public static final String FIELD_BIM_WAREHOUSE_ADDRESS									= "BIM_WAREHOUSE_ADDRESS";
	public static final String FIELD_BIM_WAREHOUSE_COMMENT									= "BIM_WAREHOUSE_COMMENT";
	
	public static final String FIELD_BIM_COMPANY_CUSTOMER_ID								= "BIM_COMPANY_CUSTOMER_ID";
	public static final String FIELD_BIM_COMPANY_REPORT_NAEM								= "BIM_COMPANY_REPORT_NAEM";
	public static final String FIELD_BIM_COMPANY_REPORT_DETAIL								= "BIM_COMPANY_REPORT_DETAIL";
	public static final String FIELD_BIM_COMPANY_RECIPIENT									= "BIM_COMPANY_RECIPIENT";
	public static final String FIELD_BIM_COMPANY_COPY										= "BIM_COMPANY_COPY";
	public static final String FIELD_BIM_COMPANY_REMARKS									= "BIM_COMPANY_REMARKS";
	
	public static final String FIELD_BIM_DEPARTMENT_DEPT_NAME								= "BIM_DEPARTMENT_DEPT_NAME";
	public static final String FIELD_BIM_DEPARTMENT_COMPANY_ID								= "BIM_DEPARTMENT_COMPANY_ID";
	public static final String FIELD_BIM_DEPARTMENT_CONTACT									= "BIM_DEPARTMENT_CONTACT";
	public static final String FIELD_BIM_DEPARTMENT_CONTACT_TEL								= "BIM_DEPARTMENT_CONTACT_TEL";
	public static final String FIELD_BIM_DEPARTMENT_CONTACT_FAX								= "BIM_DEPARTMENT_CONTACT_FAX";
	public static final String FIELD_BIM_DEPARTMENT_CONTACT_EMAIL							= "BIM_DEPARTMENT_CONTACT_EMAIL";
	public static final String FIELD_BIM_DEPARTMENT_ADDRESS									= "BIM_DEPARTMENT_ADDRESS";
	public static final String FIELD_BIM_DEPARTMENT_REMARK									= "BIM_DEPARTMENT_REMARKL";
	
	public static final String FIELD_PVM_APPLICATION_NAME									= "PVM_APPLICATION_NAME";
	public static final String FIELD_PVM_APPLICATION_ASSET_GATEGORY							= "PVM_APPLICATION_ASSET_GATEGORY";
	public static final String FIELD_PVM_APPLICATION_VERSION								= "PVM_APPLICATION_VERSION";
	public static final String FIELD_PVM_APPLICATION_ASSET_TYPE_ID							= "PVM_APPLICATION_ASSET_TYPE_ID";
	
	public static final String FIELD_BIM_MERCHANT_HEADER_CODE								= "BIM_MERCHANT_HEADER_CODE";
	public static final String FIELD_BIM_STAGES_MERCHANT_HEADER_CODE						= "BIM_STAGES_MERCHANT_HEADER_CODE"; 
	public static final String FIELD_BIM_MERCHANT_NAME										= "BIM_MERCHANT_NAME";
	public static final String FIELD_BIM_MERCHANT_HEADER_NAME								= "BIM_MERCHANT_HEADER_NAME";
	public static final String FIELD_BIM_MERCHANT_HEADER_AREA								= "BIM_MERCHANT_HEADER_AREA";
	public static final String FIELD_BIM_MERCHANT_HEADER_OPEN_TIME							= "BIM_MERCHANT_HEADER_OPEN_TIME";
	public static final String FIELD_BIM_MERCHANT_HEADER_CLOSE_TIME							= "BIM_MERCHANT_HEADER_CLOSE_TIME";
	public static final String FIELD_BIM_MERCHANT_HEADER_AO_NAME							= "BIM_MERCHANT_HEADER_AO_NAME";
	public static final String FIELD_BIM_MERCHANT_HEADER_AO_EMAIL							= "BIM_MERCHANT_HEADER_AO_EMAIL";
	public static final String FIELD_BIM_MERCHANT_HEADER_CONTACT							= "BIM_MERCHANT_HEADER_CONTACT";
	public static final String FIELD_BIM_MERCHANT_HEADER_CONTACT_TEL						= "BIM_MERCHANT_HEADER_CONTACT_TEL";
	public static final String FIELD_BIM_MERCHANT_HEADER_CMS_CONTACT_TEL					= "BIM_MERCHANT_HEADER_CMS_CONTACT_TEL";
	public static final String FIELD_BIM_MERCHANT_HEADER_CONTACT_TEL2						= "BIM_MERCHANT_HEADER_CONTACT_TEL2";
	public static final String FIELD_BIM_MERCHANT_HEADER_CONTACT_EMAIL						= "BIM_MERCHANT_HEADER_CONTACT_EMAIL";
	public static final String FIELD_BIM_MERCHANT_HEADER_PHONE								= "BIM_MERCHANT_HEADER_PHONE";
	public static final String FIELD_BIM_MERCHANT_HEADER_LOCATION							= "BIM_MERCHANT_HEADER_LOCATION";
	public static final String FIELD_BIM_MERCHANT_HEADER_OPENHOUR							= "BIM_MERCHANT_HEADER_OPENHOUR";
	public static final String FIELD_BIM_MERCHANT_HEADER_CLOSEHOUR							= "BIM_MERCHANT_HEADER_CLOSEHOUR";
	public static final String FIELD_BIM_MERCHANT_HEADER_ADDRESS							= "BIM_MERCHANT_HEADER_ADDRESS";
	
	public static final String FIELD_BIM_SLA_CUSTOMER										= "BIM_SLA_CUSTOMER";
	public static final String FIELD_BIM_SLA_CONTRACT_ID									= "BIM_SLA_CONTRACT_ID";
	public static final String FIELD_BIM_SLA_LOCATION										= "BIM_SLA_LOCATION";
	public static final String FIELD_BIM_SLA_TICKET_MODE									= "BIM_SLA_TICKET_MODE";
	public static final String FIELD_BIM_SLA_TICKET_TYPE									= "BIM_SLA_TICKET_TYPE";
	public static final String FIELD_BIM_SLA_IS_WORK_DAY									= "BIM_SLA_IS_WORK_DAY";
	public static final String FIELD_BIM_SLA_IS_THAT_DAY									= "BIM_SLA_IS_THAT_DAY";
	public static final String FIELD_BIM_SLA_THAT_DAY_TIME									= "BIM_SLA_THAT_DAY_TIME";
	public static final String FIELD_BIM_SLA_RESPONSE_HOUR									= "BIM_SLA_RESPONSE_HOUR";
	public static final String FIELD_BIM_SLA_RESPONSE_WARNNING								= "BIM_SLA_RESPONSE_WARNNING";
	public static final String FIELD_BIM_SLA_ARRIVE_HOUR									= "BIM_SLA_ARRIVE_HOUR";
	public static final String FIELD_BIM_SLA_ARRIVE_WARNNING								= "BIM_SLA_ARRIVE_WARNNING";
	public static final String FIELD_BIM_SLA_COMPLETE_HOUR									= "BIM_SLA_COMPLETE_HOUR";
	public static final String FIELD_BIM_SLA_COMPLETE_WARNNING								= "BIM_SLA_COMPLETE_WARNNING";
	public static final String FIELD_BIM_SLA_COMMENT										= "BIM_SLA_COMMENT";
	public static final String FIELD_BIM_SLA_COPY_CUSTOMER									= "BIM_SLA_COPY_CUSTOMER";
	public static final String FIELD_BIM_SLA_COPY_CONTRACT_ID								= "BIM_SLA_COPY_CONTRACT_ID";
	
	public static final String FIELD_BIM_MERCHANT_MERCHANT_ID								= "BIM_MERCHANT_MERCHANT_ID";
	public static final String FIELD_BIM_MERCHANT_COMPANY_ID								= "BIM_MERCHANT_COMPANY_ID";
	public static final String FIELD_BIM_MERCHANT_MERCHANT_CODE								= "BIM_MERCHANT_MERCHANT_CODE";
	public static final String FIELD_BIM_MERCHANT_STAGES_MERCHANT_CODE						= "BIM_MERCHANT_STAGES_MERCHANT_CODE";
	public static final String FIELD_BIM_MERCHANT_VIP										= "BIM_MERCHANT_VIP";
	public static final String FIELD_BIM_MERCHANT_REMARK									= "BIM_MERCHANT_REMARK";
	
	public static final String FIELD_ADM_SECURITY_DEF_PWD_LEN_BG							= "ADM_SECURITY_DEF_PWD_LEN_BG";
	public static final String FIELD_ADM_SECURITY_DEF_PWD_LEN_ND							= "ADM_SECURITY_DEF_PWD_LEN_ND";
	public static final String FIELD_ADM_SECURITY_DEF_PWD_ERR_CNT							= "ADM_SECURITY_DEF_PWD_ERR_CNT";
	public static final String FIELD_ADM_SECURITY_DEF_PWD_VALID_DAY							= "ADM_SECURITY_DEF_PWD_VALID_DAY";
	public static final String FIELD_ADM_SECURITY_DEF_ID_VALID_DAY							= "ADM_SECURITY_DEF_ID_LEN_BG";
	public static final String FIELD_ADM_SECURITY_DEF_PWD_RP_CNT							= "ADM_SECURITY_DEF_PWD_RP_CNT";
	public static final String FIELD_ADM_SECURITY_DEF_PWD_ALERT_DAY							= "ADM_SECURITY_DEF_PWD_ALERT_DAY";
	
	public static final String FIELD_DMM_ASSET_STOCKTAKE_HOUSE_NAME							= "DMM_ASSET_STOCKTAKE_HOUSE_NAME";
	public static final String FIELD_DMM_ASSET_STOCKTAKE_ASSET_NAME							= "DMM_ASSET_STOCKTAKE_ASSET_NAME";
	public static final String FIELD_DMM_ASSET_STOCKTAKE_ASSET_STATUS						= "DMM_ASSET_STOCKTAKE_ASSET_STATUS";
	public static final String FIELD_DMM_ASSET_STOCKTAKE_REMARK								= "DMM_ASSET_STOCKTAKE_REMARK";
	public static final String FIELD_DMM_ASSET_STOCKTAKE_ID									= "DMM_ASSET_STOCKTAKE_ID";
	public static final String FIELD_DMM_ASSET_STOCKTAKE_ASSET_SERIAL						= "DMM_ASSET_STOCKTAKE_ASSET_SERIAL";
	
	public static final String FIELD_BIM_CALENDAR_WEEKRESTS									= "BIM_CALENDAR_WEEKRESTS";
	public static final String FIELD_BIM_CALENDAR_IS_HOLIDAY								= "BIM_CALENDAR_IS_HOLIDAY";
	
	public static final String FIELD_DMM_ASSET_IN_INFO_OWNER								= "DMM_ASSET_IN_INFO_OWNER";
	public static final String FIELD_DMM_ASSET_IN_INFO_COMPANY_ID							= "DMM_ASSET_IN_INFO_COMPANY_ID";
	public static final String FIELD_DMM_ASSET_IN_INFO_CUSTOMER_ID							= "DMM_ASSET_IN_INFO_CUSTOMER_ID";
	public static final String FIELD_DMM_ASSET_IN_INFO_USER_ID								= "DMM_ASSET_IN_INFO_USER_ID";
	public static final String FIELD_DMM_ASSET_IN_INFO_CYBER_APPROVED_DATE					= "DMM_ASSET_IN_INFO_CYBER_APPROVED_DATE";
	public static final String FIELD_DMM_ASSET_IN_INFO_CONTRACT_ID							= "DMM_ASSET_IN_INFO_CONTRACT_ID";
	public static final String FIELD_DMM_ASSET_IN_INFO_ASSET_TYPE_ID						= "DMM_ASSET_IN_INFO_ASSET_TYPE_ID";
	public static final String FIELD_DMM_ASSET_IN_INFO_WAREHOUSE_ID							= "DMM_ASSET_IN_INFO_WAREHOUSE_ID";
	public static final String FIELD_DMM_ASSET_IN_INFO_ORDER_NO								= "DMM_ASSET_IN_INFO_ORDER_NO";
	public static final String FIELD_DMM_ASSET_IN_INFO_MA_TYPE								= "DMM_ASSET_IN_INFO_MA_TYPE";
	public static final String FIELD_DMM_ASSET_IN_INFO_KEEPER_NAME							= "DMM_ASSET_IN_INFO_KEEPER_NAME";
	public static final String FIELD_DMM_ASSET_IN_INFO_COMMENT								= "DMM_ASSET_IN_INFO_COMMENT";
	public static final String FIELD_DMM_ASSET_IN_INFO_ASSET_IN_ID							= "DMM_ASSET_IN_INFO_ASSET_IN_ID";
	
	
	public static final String FIELD_DMM_ASSET_IN_LIST_PROPERTY_ID							= "DMM_ASSET_IN_LIST_PROPERTY_ID";
	public static final String FIELD_DMM_ASSET_IN_LIST_SERIAL_NUMBER						= "DMM_ASSET_IN_LIST_SERIAL_NUMBER";
	
	public static final String FIELD_DMM_ASSET_TYPE_ASSET_TYPE_ID							= "DMM_ASSET_TYPE_ASSET_TYPE_ID";
	
	
	public static final String FIELD_DMM_ASSET_TYPE_NAME									= "DMM_ASSET_TYPE_NAME";
	public static final String FIELD_DMM_ASSET_TYPE_BRAND									= "DMM_ASSET_TYPE_BRAND";
	public static final String FIELD_DMM_ASSET_TYPE_MODEL									= "DMM_ASSET_TYPE_MODEL";
	public static final String FIELD_BASE_PARAMETER_ITEM_DEF_ASSET_CATEGORY					= "BASE_PARAMETER_ITEM_DEF_ASSET_CATEGORY";
	public static final String FIELD_DMM_ASSET_TYPE_UNIT									= "DMM_ASSET_TYPE_UNIT";
	public static final String FIELD_DMM_ASSET_TYPE_SAFETY_STOCK							= "DMM_ASSET_TYPE_SAFETY_STOCK";
	public static final String FIELD_DMM_ASSET_TYPE_REMARK									= "DMM_ASSET_TYPE_REMARK";
	
	public static final String FIELD_DMM_ASSET_TRANS_LIST_SERIAL_NUMBER						= "DMM_ASSET_TRANS_LIST_SERIAL_NUMBER";
	public static final String FIELD_DMM_ASSET_TRANS_LIST_COMMENT							= "DMM_ASSET_TRANS_LIST_COMMENT";
	/**
	 * 轉倉作業 -- 轉出 - 轉出倉
	 */
	public static final String FIELD_DMM_ASSET_TRANSFER_FROM_WAREHOUSEID					= "DMM_ASSET_TRANSFER_FROM_WAREHOUSEID";
	/**
	 * 轉倉作業 -- 轉出 - 轉入倉
	 */
	public static final String FIELD_DMM_ASSET_TRANSFER_TO_WAREHOUSEID						= "DMM_ASSET_TRANSFER_TO_WAREHOUSEID";
	/**
	 * 轉倉作業 -- 轉出 - 說明
	 */
	public static final String FIELD_DMM_ASSET_TRANSFER_COMMENT								= "DMM_ASSET_TRANSFER_COMMENT";
	/**
	 * 轉倉作業 -- 轉出 -- 設備序號
	 */
	public static final String FIELD_DMM_ASSET_TRANSFER_SERIAL_NUMBER						= "DMM_ASSET_TRANSFER_SERIAL_NUMBER";
	
	public static final String FIELD_BASE_PARAMETER_ITEM_DEF_BPTD_CODE						= "BASE_PARAMETER_ITEM_DEF_BPTD_CODE";
	public static final String FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_VALUE						= "BASE_PARAMETER_ITEM_DEF_ITEM_VALUE";
	public static final String FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_NAME						= "BASE_PARAMETER_ITEM_DEF_ITEM_NAME";
	public static final String FIELD_BASE_PARAMETER_ITEM_DEF_TEXT_FIELD1					= "BASE_PARAMETER_ITEM_DEF_TEXT_FIELD1";
	public static final String FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_ORDER						= "BASE_PARAMETER_ITEM_DEF_ITEM_ORDER";
	public static final String FIELD_BASE_PARAMETER_ITEM_DEF_ITEM_DESC						= "BASE_PARAMETER_ITEM_DEF_ITEM_DESC";
	
	public static final String FIELD_DMM_REPOSITORY_SIM_ENABLE_NO							= "DMM_REPOSITORY_SIM_ENABLE_NO";
	public static final String FIELD_DMM_REPOSITORY_ENABLE_DATE								= "DMM_REPOSITORY_ENABLE_DATE";
	public static final String FIELD_DMM_REPOSITORY_ASSET_IN_TIME							= "DMM_REPOSITORY_ASSET_IN_TIME";
	
	public static final String FIELD_DMM_SUPPILIES_COMPANY_ID								= "DMM_SUPPILIES_COMPANY_ID";
	public static final String FIELD_DMM_SUPPILIES_SUPPLIES_TYPE							= "DMM_SUPPILIES_SUPPLIES_TYPE";
	public static final String FIELD_DMM_SUPPILIES_SUPPLIES_NAME							= "DMM_SUPPILIES_SUPPLIES_NAME";
	public static final String FIELD_DMM_SUPPILIES_PRICE									= "DMM_SUPPILIES_PRICE";
	
	public static final String FIELD_PVM_DTID_DEF_COMPANY_ID								= "PVM_DTID_DEF_COMPANY_ID";
	public static final String FIELD_PVM_DTID_DEF_ASSET_TYPE_ID								= "PVM_DTID_DEF_ASSET_TYPE_ID";
	public static final String FIELD_PVM_DTID_DEF_DTID_START								= "PVM_DTID_DEF_DTID_START";
	public static final String FIELD_PVM_DTID_DEF_DTID_END									= "PVM_DTID_DEF_DTID_END";
	public static final String FIELD_PVM_DTID_DEF_COMMENT									= "PVM_DTID_DEF_COMMENT";
	/* 服務啟動時加載基礎數據-->是否已經做過這個動作 ,參數存放在webApplication中*/
	public static final String IS_LOAD_BASEDATA_FALG										= "isLoadBaseData";
	public static final String ACTION_QUERY_DATA											= "queryData";
	// 系統log查詢
	public static final String FIELD_ADM_SYSTEM_LOGGING_FROM_DATE							= "ADM_SYSTEM_LOGGING_FROM_DATE";
	public static final String FIELD_ADM_SYSTEM_LOGGING_TO_DATE							 	= "ADM_SYSTEM_LOGGING_TO_DATE";
	
	// 電文記錄查詢
	public static final String FIELD_ADM_API_LOG_START_DATE									= "ADM_API_LOG_START_DATE";
	public static final String FIELD_ADM_API_LOG_END_DATE									= "ADM_API_LOG_END_DATE";
	
	// 錯誤記錄檔下載
	public static final String FIELD_ADM_LOG_FILE_DOWNLOAD_LOG_FILE_NAME					= "ADM_LOG_FILE_DOWNLOAD_LOG_FILE_NAME";
	
	//設備管理作業
	public static final String FIELD_DMM_REPOSITORY_CARRY_ACCOUNT							= "DMM_REPOSITORY_CARRY_ACCOUNT";
	public static final String FIELD_DMM_REPOSITORY_REPAIR_COMPANY							= "DMM_REPOSITORY_REPAIR_COMPANY";
	public static final String FIELD_DMM_REPOSITORY_STATUS 									= "DMM_REPOSITORY_STATUS";
	public static final String FIELD_DMM_REPOSITORY_CASE_ID									= "DMM_REPOSITORY_CASE_ID";
	public static final String FIELD_DMM_REPOSITORY_INSTALLED_ADRESS						= "DMM_REPOSITORY_INSTALLED_ADRESS";
	public static final String FIELD_DMM_REPOSITORY_INSTALLED_ADRESS_LOCATION				= "DMM_REPOSITORY_INSTALLED_ADRESS_LOCATION";

	public static final String FIELD_DMM_REPOSITORY_CARRIER 								= "DMM_REPOSITORY_CARRIER";
	public static final String FIELD_DMM_REPOSITORY_CARRY_DATE								= "DMM_REPOSITORY_CARRY_DATE";
	public static final String FIELD_DMM_REPOSITORY_CARRY_COMMENT							= "DMM_REPOSITORY_CARRY_COMMENT";
	public static final String FIELD_DMM_REPOSITORY_BORROWER								= "DMM_REPOSITORY_BORROWER";
	public static final String FIELD_DMM_REPOSITORY_BORROWER_DATE							= "DMM_REPOSITORY_BORROWER_DATE";
	public static final String FIELD_DMM_REPOSITORY_BORROWER_EMAIL							= "DMM_REPOSITORY_BORROWER_EMAIL";
	public static final String FIELD_DMM_REPOSITORY_BORROWER_MGR_EMAIL						= "DMM_REPOSITORY_BORROWER_MGR_EMAIL";
	public static final String FIELD_DMM_REPOSITORY_BORROWER_COMMENT						= "DMM_REPOSITORY_BORROWER_COMMENT";
	public static final String FIELD_DMM_REPOSITORY_BACK_COMMENT							= "DMM_REPOSITORY_BACK_COMMENT";
	public static final String FIELD_DMM_REPOSITORY_DESCRIPTION								= "DMM_REPOSITORY_DESCRIPTION";
	public static final String FIELD_DMM_REPOSITORY_FAULT_COMPONENT							= "DMM_REPOSITORY_FAULT_COMPONENT";
	public static final String FIELD_DMM_REPOSITORY_REPAIR_VENDOR							= "DMM_REPOSITORY_REPAIR_VENDOR";
	public static final String FIELD_DMM_REPOSITORY_FAULT_DESCRIPTION						= "DMM_REPOSITORY_FAULT_DESCRIPTION";
	public static final String FIELD_DMM_REPOSITORY_REPAIR_COMMENT							= "DMM_REPOSITORY_REPAIR_COMMENT";
	public static final String FIELD_DMM_REPOSITORY_RETIRE_REASON_CODE						= "DMM_REPOSITORY_RETIRE_REASON_CODE";
	public static final String FIELD_DMM_REPOSITORY_RETURN_SEASON							= "DMM_REPOSITORY_RETURN_SEASON";
	public static final String FIELD_DMM_REPOSITORY_PROPERTY_ID								= "DMM_REPOSITORY_PROPERTY_ID";
	public static final String FIELD_DMM_REPOSITORY_CONTRACT_ID								= "DMM_REPOSITORY_CONTRACT_ID";
	public static final String FIELD_DMM_REPOSITORY_IS_ENABLED								= "DMM_REPOSITORY_IS_ENABLED";
	public static final String FIELD_DMM_REPOSITORY_IS_SIM_ENABLE							= "DMM_REPOSITORY_IS_SIM_ENABLE";
	public static final String FIELD_DMM_REPOSITORY_SIM_ENABLE_DATE							= "DMM_REPOSITORY_SIM_ENABLE_DATE";
	public static final String FIELD_DMM_REPOSITORY_TID										= "DMM_REPOSITORY_TID";
	public static final String FIELD_DMM_REPOSITORY_DTID									= "DMM_REPOSITORY_DTID";
	public static final String FIELD_DMM_REPOSITORY_CHECKED_DATE							= "DMM_REPOSITORY_CHECKED_DATE";
	public static final String FIELD_DMM_REPOSITORY_FACTORY_WARRANTY_DATE					= "DMM_REPOSITORY_FACTORY_WARRANTY_DATE";
	public static final String FIELD_DMM_REPOSITORY_CUSTOMER_WARRANTY_DATE					= "DMM_REPOSITORY_CUSTOMER_WARRANTY_DATE";
	public static final String FIELD_DMM_REPOSITORY_IASSET_USER								= "DMM_REPOSITORY_IASSET_USER";
	public static final String FIELD_DMM_REPOSITORY_ASSET_OWNER								= "DMM_REPOSITORY_ASSET_OWNER";
	public static final String FIELD_DMM_REPOSITORY_COUNTER									= "DMM_REPOSITORY_COUNTER";
	public static final String FIELD_DMM_REPOSITORY_CARTON_NO								= "DMM_REPOSITORY_CARTON_NO";
	public static final String FIELD_DMM_REPOSITORY_MAINTAIN_COMPANY						= "DMM_REPOSITORY_MAINTAIN_COMPANY";
	public static final String FIELD_DMM_REPOSITORY_MAINTAIN_USER							= "DMM_REPOSITORY_MAINTAIN_USER";
	public static final String FIELD_DMM_REPOSITORY_CASE_COMPLETION_DATE					= "DMM_REPOSITORY_CASE_COMPLETION_DATE";
	public static final String FIELD_DMM_REPOSITORY_ANALYZE_DATE							= "DMM_REPOSITORY_ANALYZE_DATE";
	public static final String FIELD_DMM_REPOSITORY_IS_CUP									= "DMM_REPOSITORY_IS_CUP";
	//設備狀態報表 -- 查詢月份
	public static final String FIELD_DMM_REPOSTATUSREPORT_QUERY_DATE						= "DMM_REPOSTATUSREPORT_QUERY_DATE";
	
	//刷卡機標籤管理
	public static final String FIELD_DMM_EDC_LABEL_START_DATE								= "DMM_EDC_LABEL_START_DATE";
	public static final String FIELD_DMM_EDC_LABEL_END_DATE									= "DMM_EDC_LABEL_END_DATE";
	public static final String FIELD_DMM_EDC_LABEL_MERCHANT_CODE							= "DMM_EDC_LABEL_MERCHANT_CODE";
	public static final String FIELD_DMM_EDC_LABEL_MERCHANT_TYPE							= "DMM_EDC_LABEL_MERCHANT_TYPE";
	public static final String FIELD_DMM_EDC_LABEL_DTID										= "DMM_EDC_LABEL_DTID";
	public static final String FIELD_DMM_EDC_LABEL_RELATION									= "DMM_EDC_LABEL_RELATION";
	public static final String FIELD_DMM_EDC_LABEL_STATUS									= "DMM_EDC_LABEL_STATUS";
	public static final String FIELD_DMM_EDC_LABEL_IP										= "DMM_EDC_LABEL_IP";

	public static final String FIELD_SRM_CASE_HANDLE_CUSTOMER_ID							= "SRM_CASE_HANDLE_CUSTOMER_ID";
	public static final String FIELD_SRM_CASE_HANDLE_CONTRACT_ID							= "SRM_CASE_HANDLE_CONTRACT_ID";
	public static final String FIELD_SRM_CASE_HANDLE_INSTALL_TYPE							= "SRM_CASE_HANDLE_INSTALL_TYPE";
	public static final String FIELD_SRM_CASE_HANDLE_COMPANY_ID								= "SRM_CASE_HANDLE_COMPANY_ID";
	public static final String FIELD_SRM_CASE_HANDLE_CASE_TYPE								= "SRM_CASE_HANDLE_CASE_TYPE";
	public static final String FIELD_SRM_CASE_HANDLE_EXPECTED_COMPLETION_DATE				= "SRM_CASE_HANDLE_EXPECTED_COMPLETION_DATE";
	public static final String FIELD_SRM_CASE_HANDLE_MERCHANT_CODE							= "SRM_CASE_HANDLE_MERCHANT_CODE";
	public static final String FIELD_SRM_CASE_HANDLE_MERCHANT_HEADER_ID						= "SRM_CASE_HANDLE_MERCHANT_HEADER_ID";
	public static final String FIELD_SRM_CASE_HANDLE_INSTALLED_ADRESS						= "SRM_CASE_HANDLE_INSTALLED_ADRESS";
	public static final String FIELD_SRM_CASE_HANDLE_INSTALLED_LOCATION						= "SRM_CASE_HANDLE_INSTALLED_LOCATION";
	public static final String FIELD_SRM_CASE_HANDLE_INSTALLED_CONTACT						= "SRM_CASE_HANDLE_INSTALLED_CONTACT";
	public static final String FIELD_SRM_CASE_HANDLE_INSTALLED_CONTACT_PHONE				= "SRM_CASE_HANDLE_INSTALLED_CONTACT_PHONE";
	public static final String FIELD_SRM_CASE_HANDLE_EDC_TYPE								= "SRM_CASE_HANDLE_EDC_TYPE";
	public static final String FIELD_SRM_CASE_HANDLE_SOFTWARE_VERSION						= "SRM_CASE_HANDLE_SOFTWARE_VERSION";
	public static final String FIELD_SRM_CASE_HANDLE_ECR_CONNECTION							= "SRM_CASE_HANDLE_ECR_CONNECTION";
	public static final String FIELD_SRM_CASE_HANDLE_TRANSACTION_TYPE						= "SRM_CASE_HANDLE_TRANSACTION_TYPE";
	public static final String FIELD_SRM_CASE_HANDLE_TID									= "SRM_CASE_HANDLE_TID";
	public static final String FIELD_SRM_CASE_HANDLE_CASE_NUMBER							= "SRM_CASE_HANDLE_CASE_NUMBER";
	public static final String FIELD_SRM_CASE_HANDLE_DTID									= "SRM_CASE_HANDLE_DTID";
	public static final String FIELD_SRM_CASE_HANDLE_REQUIREMENT_NO							= "SRM_CASE_HANDLE_REQUIREMENT_NO";
	public static final String FIELD_SRM_CASE_HANDLE_UNINSTALL_TYPE							= "SRM_CASE_HANDLE_UNINSTALL_TYPE";
	public static final String FIELD_SRM_CASE_HANDLE_PROJECT_NAME							= "SRM_CASE_HANDLE_PROJECT_NAME";
	public static final String FIELD_SRM_CASE_HANDLE_PROJECT_CODE							= "SRM_CASE_HANDLE_PROJECT_CODE";
	public static final String FIELD_SRM_CASE_HANDLE_REPAIR_REASON							= "SRM_CASE_HANDLE_REPAIR_REASON";
	public static final String FIELD_SRM_CASE_HANDLE_LOCALHOST_IP							= "SRM_CASE_HANDLE_LOCALHOST_IP";
	public static final String FIELD_SRM_CASE_HANDLE_GATEWAY								= "SRM_CASE_HANDLE_GATEWAY";
	public static final String FIELD_SRM_CASE_HANDLE_NETMASK								= "SRM_CASE_HANDLE_NETMASK";
	public static final String FIELD_SRM_CASE_HANDLE_ELECTRONIC_INVOICE						= "SRM_CASE_HANDLE_ELECTRONIC_INVOICE";
	public static final String FIELD_SRM_CASE_CUP_QUICK_PASS								= "SRM_CASE_CUP_QUICK_PASS";
	public static final String FIELD_SRM_CASE_IS_PROJECT									= "SRM_CASE_IS_PROJECT";
	public static final String FIELD_SRM_CASE_HANDLE_CONTACT_ADDRESS_LOCATION				= "SRM_CASE_HANDLE_CONTACT_ADDRESS_LOCATION";
	public static final String FIELD_SRM_CASE_HANDLE_CONTACT_ADDRESS						= "SRM_CASE_HANDLE_CONTACT_ADDRESS";
	public static final String FIELD_SRM_CASE_HANDLE_CONTACT_USER							= "SRM_CASE_HANDLE_CONTACT_USER";
	public static final String FIELD_SRM_CASE_HANDLE_CONTACT_USER_PHONE						= "SRM_CASE_HANDLE_CONTACT_USER_PHONE";
	public static final String FIELD_SRM_CASE_HANDLE_DESCRIPTION							= "SRM_CASE_HANDLE_DESCRIPTION";
	public static final String FIELD_SRM_CASE_HANDLE_OLD_MERCHANT_CODE						= "SRM_CASE_HANDLE_OLD_MERCHANT_CODE";
	public static final String FIELD_SRM_CASE_HANDLE_DEPARTMENT_ID							= "SRM_CASE_HANDLE_DEPARTMENT_ID";
	public static final String FIELD_SRM_CASE_HANDLE_BUILT_IN_FEATURE						= "SRM_CASE_HANDLE_BUILT_IN_FEATURE";
	public static final String FIELD_SRM_CASE_HANDLE_MULTI_MODULE							= "SRM_CASE_HANDLE_MULTI_MODULE";
	public static final String FIELD_SRM_CASE_HANDLE_PERIPHERALS							= "SRM_CASE_HANDLE_PERIPHERALS";
	public static final String FIELD_SRM_CASE_HANDLE_PERIPHERALS_FUNCTION					= "SRM_CASE_HANDLE_PERIPHERALS_FUNCTION";
	public static final String FIELD_SRM_CASE_HANDLE_PERIPHERALS2							= "SRM_CASE_HANDLE_PERIPHERALS2";
	public static final String FIELD_SRM_CASE_HANDLE_PERIPHERALS_FUNCTION2					= "SRM_CASE_HANDLE_PERIPHERALS_FUNCTION2";
	public static final String FIELD_SRM_CASE_HANDLE_PERIPHERALS3							= "SRM_CASE_HANDLE_PERIPHERALS3";
	public static final String FIELD_SRM_CASE_HANDLE_PERIPHERALS_FUNCTION3					= "SRM_CASE_HANDLE_PERIPHERALS_FUNCTION3";
	public static final String FIELD_SRM_CASE_HANDLE_CONNECTION_TYPE						= "SRM_CASE_HANDLE_CONNECTION_TYPE";
	public static final String FIELD_SRM_CASE_HANDLE_NET_VENDOR_ID							= "SRM_CASE_HANDLE_NET_VENDOR_ID";
	public static final String FIELD_SRM_CASE_HANDLE_TMS_PARAM_DESC							= "SRM_CASE_HANDLE_TMS_PARAM_DESC";
	public static final String FIELD_SRM_CASE_HANDLE_RECEIPT_TYPE							= "SRM_CASE_HANDLE_RECEIPT_TYPE";
	public static final String FIELD_SRM_CASE_HANDLE_SAME_INSTALLED							= "SRM_CASE_HANDLE_SAME_INSTALLED";
	public static final String FIELD_SRM_CASE_HANDLE_LOGO									= "SRM_CASE_HANDLE_LOGO";
	public static final String FIELD_SRM_CASE_HANDLE_IS_OPEN_ENCRYPT						= "SRM_CASE_HANDLE_IS_OPEN_ENCRYPT";
	public static final String FIELD_SRM_CASE_HANDLE_ELECTRONIC_PAY_PLATFORM				= "SRM_CASE_HANDLE_ELECTRONIC_PAY_PLATFORM";
	public static final String FIELD_SRM_CASE_HANDLE_IS_TMS									= "SRM_CASE_HANDLE_IS_TMS";
	public static final String FIELD_SRM_HANDLE_IS_BUSSINESS_ADDRESS						= "SRM_CASE_HANDLE_IS_BUSSINESS_ADDRESS";
	public static final String FIELD_SRM_CASE_HANDLE_IS_BUSSINESS_CONTACT					= "SRM_CASE_HANDLE_IS_BUSSINESS_CONTACT";
	public static final String FIELD_SRM_CASE_HANDLE_IS_BUSSINESS_CONTACT_PHONE				= "SRM_CASE_HANDLE_IS_BUSSINESS_CONTACT_PHONE";
	
	public static final String FIELD_SRM_HANDLE_CONTACT_IS_BUSSINESS_ADDRESS				= "SRM_CASE_HANDLE_CONTACT_IS_BUSSINESS_ADDRESS";
	public static final String FIELD_SRM_CASE_HANDLE_CONTACT_IS_BUSSINESS_CONTACT			= "SRM_CASE_HANDLE_CONTACT_IS_BUSSINESS_CONTACT";
	public static final String FIELD_SRM_CASE_HANDLE_CONTACT_IS_BUSSINESS_CONTACT_PHONE		= "SRM_CASE_HANDLE_CONTACT_IS_BUSSINESS_CONTACT_PHONE";
	//問題原因
	public static final String FIELD_SRM_CASE_HANDLE_PROBLEM_REASON							= "SRM_CASE_HANDLE_PROBLEM_REASON";
	//問題解決方式
	public static final String FIELD_SRM_CASE_HANDLE_PROBLEM_SOLUTION						= "SRM_CASE_HANDLE_PROBLEM_SOLUTION";
	//責任歸屬	
	public static final String FIELD_SRM_CASE_HANDLE_RESPONSIBITY							= "SRM_CASE_HANDLE_RESPONSIBITY";
	//處理說明
	public static final String FIELD_SRM_CASE_TRANSACTION_DESCRIPTION						= "SRM_CASE_TRANSACTION_DESCRIPTION";
	//派工至
	public static final String FIELD_SRM_CASE_HANDLE_DISPATCH_TO							= "SRM_CASE_HANDLE_DISPATCH_TO";
	//自動派工至	
	public static final String FIELD_SRM_CASE_HANDLE_AUTO_DISPATCH_TO						= "SRM_CASE_HANDLE_AUTO_DISPATCH_TO";
	// 實際完修時間
	public static final String FIELD_SRM_CASE_HANDLE_COMPLETE_DATE							= "SRM_CASE_HANDLE_COMPLETE_DATE";
	public static final String FIELD_SRM_CASE_HANDLE_CREATE_DATE							= "SRM_CASE_HANDLE_CREATE_DATE";
	//Task #3343
	public static final String FIELD_SRM_CASE_HANDLE_IS_BUSSINESS_CONTACT_MOBILE_PHONE		= "SRM_CASE_HANDLE_IS_BUSSINESS_CONTACT_MOBILE_PHONE";
	public static final String FIELD_SRM_CASE_HANDLE_IS_BUSSINESS_CONTACT_EMAIL				= "SRM_CASE_HANDLE_IS_BUSSINESS_CONTACT_EMAIL";
	public static final String FIELD_SRM_CASE_HANDLE_INSTALLED_CONTACT_MOBILE_PHONE			= "SRM_CASE_HANDLE_INSTALLED_CONTACT_MOBILE_PHONE";
	public static final String FIELD_SRM_CASE_HANDLE_INSTALLED_CONTACT_EMAIL				= "SRM_CASE_HANDLE_INSTALLED_CONTACT_EMAIL";
	public static final String FIELD_SRM_CASE_HANDLE_LOGISTICS_VENDOR						= "SRM_CASE_HANDLE_INSTALLED_LOGISTICS_VENDOR";
	public static final String FIELD_SRM_CASE_HANDLE_LOGISTICS_NUMBER						= "SRM_CASE_HANDLE_INSTALLED_LOGISTICS_NUMBER";
	
	public static final String FIELD_SRM_CASE_HANDLE_CONTACT_IS_BUSSINESS_CONTACT_MOBILE_PHONE		= "SRM_CASE_HANDLE_CONTACT_IS_BUSSINESS_CONTACT_MOBILE_PHONE";
	public static final String FIELD_SRM_CASE_HANDLE_CONTACT_IS_BUSSINESS_CONTACT_EMAIL		= "SRM_CASE_HANDLE_CONTACT_IS_BUSSINESS_CONTACT_EMAIL";
	public static final String FIELD_SRM_CASE_HANDLE_CONTACT_MOBILE_PHONE					= "CASE_HANDLE_CONTACT_MOBILE_PHONE";
	public static final String FIELD_SRM_CASE_HANDLE_CONTACT_USER_EMAIL						= "SRM_CASE_HANDLE_CONTACT_USER_EMAIL";
	public static final String FIELD_SRM_CASE_HANDLE_CASE_ID								= "SRM_CASE_HANDLE_CASE_ID";
	public static final String FIELD_SRM_CASE_HANDLE_INSTALLED_POST_CODE					= "SRM_CASE_HANDLE_INSTALLED_POST_CODE";
	public static final String FIELD_SRM_CASE_HANDLE_CONTACT_POST_CODE						= "SRM_CASE_HANDLE_CONTACT_POST_CODE";
	public static final String FIELD_SRM_CASE_HANDLE_INSTALLED_CONTACT_AREA_NAME			= "SRM_CASE_HANDLE_INSTALLED_CONTACT_AREA_NAME";
	//客訴管理
	public static final String FIELD_SRM_COMPLAINT_SOURCE									= "SRM_COMPLAINT_SOURCE";
	public static final String FIELD_SRM_COMPLAINT_CUSTOMER									= "SRM_COMPLAINT_CUSTOMER";
	public static final String FIELD_SRM_COMPLAINT_MERCHANT_ID								= "SRM_COMPLAINT_MERCHANT_ID";
	public static final String FIELD_SRM_COMPLAINT_COMPANY									= "SRM_COMPLAINT_COMPANY";
	public static final String FIELD_SRM_COMPLAINT_STAFF									= "SRM_COMPLAINT_STAFF";
	public static final String FIELD_SRM_COMPLAINT_USER_NAME								= "SRM_COMPLAINT_USER_NAME";
	public static final String FIELD_SRM_COMPLAINT_LEVEL									= "SRM_COMPLAINT_LEVEL";
	public static final String FIELD_SRM_COMPLAINT_TID										= "SRM_COMPLAINT_TID";
	public static final String FIELD_SRM_COMPLAINT_CONTENT									= "SRM_COMPLAINT_CONTENT";
	public static final String FIELD_SRM_COMPLAINT_REMARK									= "SRM_COMPLAINT_REMARK";
	public static final String FIELD_SRM_COMPLAINT_DATE										= "SRM_COMPLAINT_DATE";
	public static final String FIELD_SRM_COMPLAINT_CONTACT_WAY								= "SRM_COMPLAINT_CONTACT_WAY";
	public static final String FIELD_SRM_COMPLAINT_QUESTION_TYPE							= "SRM_COMPLAINT_QUESTION_TYPE";
	public static final String FIELD_SRM_COMPLAINT_CUSTOMER_AMOUNT							= "SRM_COMPLAINT_CUSTOMER_AMOUNT";
	public static final String FIELD_SRM_COMPLAINT_VENDOR_AMOUNT							= "SRM_COMPLAINT_VENDOR_AMOUNT";
	
	public static final String ACTION_GET_USEER_BY_DEPT_AND_ROLE 							= "getUserByDepartmentAndRole";
	//獲取案件ao人員mail
	public static final String ACTION_GET_AOEMAIL_BY_CASEID 								= "getAoEmailByCaseId";
	//獲取案件相關人員mail
	public static final String ACTION_GET_CASE_EMAIL_BY_CASEID 								= "getCaseEmailByCaseId";
	// 是否異動設備
	public static final String ACTION_IS_CHANGE_ASSET 										= "isChangeAsset";
	
	//
	public static final String ACTION_GET_ASSET_LIST_FOR_CASE								= "getAssetListForCase";
	// 案件動作簡稱
	public static final String CASE_ACTION_MACHINE_ONLINE_EXCLUSION							= "O";
	
	
	// 案件動作名稱
	public static final String FIELD_CASE_ACTION_ADD_RECORD									= "CASE_ACTION_ADD_RECORD";
	public static final String FIELD_CASE_ACTION_DISPATCHING								= "CASE_ACTION_DISPATCHING";
	public static final String FIELD_CASE_ACTION_RESPONSE									= "CASE_ACTION_RESPONSE";
	public static final String FIELD_CASE_ACTION_ARRIVE										= "CASE_ACTION_ARRIVE";
	public static final String FIELD_CASE_ACTION_COMPLETE									= "CASE_ACTION_COMPLETE";
	public static final String FIELD_CASE_ACTION_SIGN										= "CASE_ACTION_SIGN";
	public static final String FIELD_CASE_ACTION_ONLINE_EXCLUSION							= "CASE_ACTION_ONLINE_EXCLUSION";
	public static final String FIELD_CASE_ACTION_RETREAT									= "CASE_ACTION_RETREAT";
	public static final String FIELD_CASE_ACTION_DELAY										= "CASE_ACTION_DELAY";
	public static final String FIELD_CASE_ACTION_RUSH_REPAIR								= "CASE_ACTION_RUSH_REPAIR";
	public static final String FIELD_CASE_ACTION_CHANGE_CASE_TYPE							= "CASE_ACTION_CHANGE_CASE_TYPE";
	public static final String FIELD_CASE_ACTION_CLOSED										= "CASE_ACTION_CLOSED";
	public static final String FIELD_CASE_ACTION_VOID_CASE									= "CASE_ACTION_VOID_CASE";
	public static final String FIELD_CASE_ACTION_IMMEDIATELY_CLOSING						= "CASE_ACTION_IMMEDIATELY_CLOSING";
	public static final String FIELD_CASE_ACTION_AUTO_DISPATCHING							= "CASE_ACTION_AUTO_DISPATCHING";
	public static final String FIELD_CASE_ACTION_CONFIRM_AUTHORIZES							= "CASE_ACTION_AUTO_CONFIRM_AUTHORIZES";
	public static final String FIELD_CASE_NO												= "CASE_NO";
	public static final String FIELD_CASE_ACTION_CHANGE_COMPLETE_DATE						= "CASE_ACTION_CHANGE_COMPLETE_DATE";
	public static final String FIELD_CASE_ACTION_CHANGE_CREATE_DATE							= "CASE_ACTION_CHANGE_CREATE_DATE";
	public static final String FIELD_CASE_ACTION_LEASE_PRELOAD								= "CASE_ACTION_LEASE_PRELOAD";
	public static final String FIELD_CASE_ACTION_PAYMENT									= "CASE_ACTION_PAYMENT";
	public static final String FIELD_CASE_ACTION_CANCEL_CONFIRM_AUTHORIZES					= "CASE_ACTION_CANCEL_CONFIRM_AUTHORIZES";
	// 客服
	public static final String FIELD_CASE_ROLE_CUSTOMER_SERVICE								= "CASE_ROLE_CUSTOMER_SERVICE";
	
	
	public static final String FIELD_SRM_PAYMENT_INFO_PAUMENT_ID							= "SRM_PAYMENT_INFO_PAUMENT_ID";
	public static final String FIELD_SRM_PAYMENT_ITEM_REASON_DETAIL							= "SRM_PAYMENT_ITEM_REASON_DETAIL";
	//設備借用mail名稱
	public static final String ASSET_BORROW_MAIL_NAME										="ASSET_BORROW_MAIL_NAME";
	public static final String ASSET_BORROW_UNBACK_MAIL_NAME								="ASSET_BORROW_UNBACK_MAIL_NAME";
	public static final String ASSET_SEND_MAIL_NAME											="ASSET_SEND_MAIL_NAME";
	//設備借用申請單
	public static final String ASSET_BORROW_ZIP_NAME										="ASSET_BORROW_ZIP_NAME";
	//設備借用單模板
	public static final String ASSET_BORROW_DOC_TEMPLATE_NAME								="repostory";
	//模板
	public static final String PARAM_TEMPLATE												= "template";
	//不可進行解除綁定作業
	public static final String FIELD_ASSET_UN_REMOVE										= "assetUnRemove";
	//廠商倉管
	public static final String FIELD_ROLE_CODE_VENDOR_WAREHOUSE								="VENDOR_WAREHOUSE";
	//客戶廠商倉管
	public static final String FIELD_ROLE_CODE_CUS_VENDOR_WAREHOUSE							="CUS_VENDOR_WAREHOUSE";
	//小計
	public static final String FIELD_REPO_STATUS_REPORT_SUM									="REPO_STATUS_REPORT_SUM";
	//總計
	public static final String FIELD_REPO_STATUS_REPORT_ALL_SUM								="REPO_STATUS_REPORT_ALL_SUM";
	//修改-漢語
	public static final String MGS_UPDATE_LOG 												= "UPDATE_LOG";
	//新增-漢語
	public static final String MGS_CREATE_LOG 												= "CREATE_LOG";
	//修改
	public static final String PARAM_UPDATE =  												"UPDATE";
	//新增
	public static final String PARAM_CREATE =  												"CREATE";
	//列印借用單不可超過1000筆
	public static final String PARAM_MORE_THAN＿ONE_THOUSAND       							="MORE_THAN_ONE_THOUSAND";
	//未派工至單位
	public static final String IS_NOT_DISPATCHING_TO_DEPTMENT 								= "IS_NOT_DISPATCHING_TO_DEPTMENT";
	//mail已寄出，請查收
	public static final String MSG_ASSET_MAIL_IS_SENDED 								= "ASSET_MAIL_IS_SENDED";
	//設備狀態不為領用中或使用中，不可進行台新租賃維護作業
	public static final String ASSET_STATUS_CANNOT_REPAIR								= "assetStatusCannotRepair";
	//設備使用人不為台新，不可進行台新租賃維護作業
	public static final String ASSET_CANNOT_TSB											= "assetCannotTSB";
	//設備狀態不為領用中或使用中，不可進行捷達威維護作業
	public static final String ASSET_STATUS_CANNOT_USE_AND_APPLY						= "assetStatusCannotUseAndApply";
	//設備使用人不為捷達威，不可進行捷達威維護作業
	public static final String ASSET_CANNOT_JDW											= "assetCannotJDW";
	
	//批次报表查询结果－有值
	public static final String REPORT_RESULT_IS_NOT_NULL								= "REPORT_RESULT_IS_NOT_NULL";
	//批次报表查询结果－无值
	public static final String REPORT_RESULT_IS_NULL									= "REPORT_RESULT_IS_NULL";
	
	public static final String FIELD_MOBILE_PHONE										= "MOBILE_PHONE";
	public static final String FIELD_MOBILE_ACTION										= "MOBILE_ACTION";
	
	//CMS案件結果
	public static final String FIELD_CMS_RESULT											= "cmsResult";
	//線上排除后結案狀態
	public static final String FIELD_CLOSED_BY_ONLINE_EXCLUSION							= "closedByOnlineExclusion";
	//配送
	public static final String DELIVERY													= "DELIVERY";
		
	/**
	 * CMS IP標識
	 */
	public static final String CMS_IP_FLAG												= "CMS_IP_FLAG";
	/**
	 * CMS IP
	 */
	public static final String CMS_IP													= "ip";
	//租賃預載資訊
	public static final String FIELD_LEASE_PRELOAD_MSG									= "LEASE_PRELOAD_MSG";
	//設備序號（edc）
	public static final String FIELD_EQUIPMENT_NUMBER									= "EQUIPMENT_NUMBER";
	public static final String FIELD_SRM_CASE_HANLD_INFO								= "SRM_CASE_HANLD_INFO";
	//設備序號（sim卡）
	public static final String FIELD_SIM_EQUIPMENT_NUMBER								= "SIM_EQUIPMENT_NUMBER";
	//微型商戶
	public static final String FIELD_MICRO_MERCHANT										= "MICRO_MERCHANT";
	public static final String FIELD_MAKE_APPOINTMENT									= "MAKE_APPOINTMENT";
	public static final String FIELD_CYBERSOFT											= "CYBERSOFT";
	public static final String FIELD_FAULT												= "FAULT";
	public static final String FIELD_LEASE												= "LEASE";
	public static final String FIELD_BUY_OFF											= "BUY_OFF";
	public static final String FIELD_LEASE_SIGN											= "LEASE_SIGN";
	public static final String FIELD_SRM_CASE_HANDLE_DEAL_DATE							= "SRM_CASE_HANDLE_DEAL_DATE";
	public static final String FIELD_CASE_END_TEMPLATE_NAME_FOR_CN						= "CASE_END_TEMPLATE_NAME_FOR_CN";
	public static final String FIELD_CASE_END_TEMPLATE_NAME_FOR_EN						= "coordinatedCompletionTemplate.xls";

	//--------------------------------------API LOG---------------------------------------------------------------//
	//上行
	public static final String API_OUT_PUT												= "output";
	//下行
	public static final String API_IN_PUT												= "input";
	//下行(RS)
	public static final String API_RS													= "RS";
	//上行(RQ)
	public static final String API_RQ													= "RQ";
	//---------------------------交易參數-交易項目--------------------------------------------------------------------------//
	public static final String PARAMETER_ITEM_JCB										= "jcb";
	public static final String PARAMETER_ITEM_RETURN_TRANSACTION						= "returnTransaction";
	
	//---------------------------IAtoms-API CLIENT_CODE--------------------------------------------------------------------------//
	public static final String PARAM_CHECK_GP_CLIENT_CODE								= "CMS_GP";
	
	public static final String FIELD_SRM_CASE_HANDLE_OVERDUE_MSG						= "SRM_CASE_HANDLE_OVERDUE_MSG";
	public static final String FIELD_SRM_CASE_HANDLE_UNINSTALL_TYPE_COMPULSORY			= "SRM_CASE_HANDLE_UNINSTALL_TYPE_COMPULSORY";
	
	//--------------------------- 案件類別(設備借用作業) ---------------------------//
		//申請
		public static final String FIELD_ASSET_BORROW_CATEGORY_APPLY						= "01";
		//續借
		public static final String FIELD_ASSET_BORROW_CATEGORY_BORROW						= "02";
		//歸還
		public static final String FIELD_ASSET_BORROW_CATEGORY_BACK							= "03";
		//--------------------------- 案件狀態(設備借用作業) ---------------------------//
		//待處理
		public static final String FIELD_ASSET_BORROW_STATUS_WAIT_PROCESS					= "WAIT_PROCESS";
		//已處理
		public static final String FIELD_ASSET_BORROW_STATUS_PROCESS						= "PROCESS";
		//--------------------------- 角色(設備借用作業) ---------------------------//
		//倉管經辦
		public static final String PARAM_ROLE_CODE_STOREHOUSE_HANDLE						= "STOREHOUSE_HANDLE";
		//倉管主管
		public static final String PARAM_ROLE_CODE_STOREHOUSE_SUPERVISO						= "STOREHOUSE_SUPERVISO";
		//--------------------------- 角色(設備借用作業) ---------------------------//
		//只包含【倉管經辦】角色
		public static final String PARAM_ONLY_STOREHOUSE_HANDLE								= "01";
		//只包含【倉管主管】角色
		public static final String PARAM_ONLY_STOREHOUSE_SUPERVISO							= "02";
		//【倉管主管】和【倉管經辦】都包含角色
		public static final String PARAM_ONLY_STOREHOUSE_SUPERVISO_and_HANDLE				= "03";
}

