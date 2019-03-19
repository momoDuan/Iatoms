package com.cybersoft4u.xian.iatoms.common;

import cafe.workflow.bean.WfMessageCode;
/**
 * Purpose: This class defines application error codes.
 * The error code is composed with 
 * <system (or project) code>-MSG-<use case code>-<message type><message sequence no>.
 * Where 
 *   system (or project) code : It is usually named with capitalized abbreviation in 3 characters.
 *   use case code : According to each defined use case code.
 *   message type : E for Error, W for Warning, I for Information
 *   message sequence no : 4 characters of number (0001~9999)
 * @author Allenchen
 * @since  JDK 1.7
 * @date   2016/3/30
 * @MaintenancePersonnel Allenchen
 */
public interface IAtomsMessageCode extends WfMessageCode{
	
	/**
	 * 请先正常登出，再登入 或关闭所有浏览器视窗再登入！
	 */
	public static final String LIMITED_LOGON_ID				 					= "IATOMS-MSG-LOGON-W0001";
	
	/**
	 * 基础值为空
	 */
	public static final String BASE_PARAMETER_VALUE_IS_NULL 					= "IATOMS-MSG-COMMON-W0004";
	
	/**
	 * 非法操作
	 */
	public static final String ILLEGAL_OPERATION				 				= "IATOMS-MSG-LOGON-W0002";
	
	/**
	 * 过久未使用网页，网络连接已超时
	 */
	public static final String SESSION_INVALIDATED_TIME_OUT 		 			= "IATOMS-MSG-HTTP-E0002";
	
	/**
	 * 页面资料检核失败
	 */
	public static final String HTTP_REQUEST_PARAMS_VALIDATION_FAILED 			= "IATOMS-MSG-PAGE-E0002";
	
	/**
	 * 页面资料解析失败
	 */
	public static final String HTTP_REQUEST_PARAMS_PARSING_FAILED 	 			= "IATOMS-MSG-PAGE-E0003";
	
	/**
	 * 页面异常错误，请联络应用系统管理员
	 */
	public static final String WEB_PAGE_ERROR						 			= "IATOMS-MSG-PAGE-E0004";
	/**
	 * 检视成功
	 */
	public static final String VIEW_SUCCESS										= "IATOMS-MSG-COMMON-I0007";
	
	/**
	 * 检视失败
	 */
	public static final String VIEW_FAILURE										= "IATOMS-MSG-COMMON-E0006";
	
	/**
	 * 字段空白or输入有误
	 */
	public static final String CHECK_INPUT_IS_NULL                          	= "IATOMS-MSG-COMMON-W0001"; 
	
	/**
	 * 检核输入--无误
	 */
	public static final String CHECK_INPUT_IS_TRUE								= "IATOMS-MSG-COMMON-I0008";
	
	/**
	 * 上传值错误
	 */
	public static final String IMPORT_VALUE_ERROR								= "IATOMS-MSG-COMMON-I0009";
	
	/**
	 * 上传值为空
	 */
	public static final String IMPORT_VALUE_EMPTY								= "IATOMS-MSG-COMMON-I0010";
	
	/**
	 * 上传值太长
	 */
	public static final String IMPORT_VALUE_TOO_LONG							= "IATOMS-MSG-COMMON-I0011";
	
	/**
	 * 上传值已存在
	 */
	public static final String IMPORT_VALUE_EXIST								= "IATOMS-MSG-COMMON-I0012";
	/**
	 * 不可為空
	 */
	public static final String IMPORT_VALUE_NOT_EMPTY							= "IATOMS-MSG-COMMON-I0014";
	/**
	 * 不存在
	 */
	public static final String IMPORT_VALUE_NOT_FOUND							= "IATOMS-MSG-COMMON-I0015";
	/**
	 * 長度限{0}字符
	 */
	public static final String IMPORT_LENGTH_LIMIT								= "IATOMS-MSG-COMMON-I0016";
	
	/**
	 * 上传值不再所属范围内
	 */
	public static final String IMPORT_VALUE_NOBELONG							= "IATOMS-MSG-COMMON-W0002";
	
	/**
	 * 上传值重复
	 */
	public static final String IMPORT_VALUE_REPEAT								= "IATOMS-MSG-COMMON-I0013";
	
	/**
	 * 审核完成
	 */
	public static final String AUDIT_SUCCESS									="IATOMS-MSG_COMMON_I0006";
	
	/**
	 * 审核失败
	 */
	public static final String AUDIT_FAILURE 									="IATOMS-MSG_COMMON_E0006";
	
	/**
	 * 审核失败
	 */
	public static final String FUNCTION_NOT_FOUND 								="IATOMS-MSG_COMMON_E0007";
	
	/**
	 * 確認取消?
	 */
	public static final String COMFIRM_DELETE                     = "IATOMS-MSG-COMMON-I0020";
	/**
	 * 確認取消?
	 */
	public static final String COMFIRM_CANCEL                     = "IATOMS-MSG-COMMON-I0021";	
	/**
	 * 請輸入{功能變數名稱}
	 * 必填欄位空白
	 */
	public static final String INPUT_BLANK                        = "IATOMS-MSG-COMMON-E0021";
	/**
	 * {功能變數名稱}輸入有誤，請重新輸入
	 */
	public static final String INPUT_FORMAT_ERROR                 = "IATOMS-MSG-COMMON-E0022";
	/**
	 * {0}不可重複，請重新輸入
	 */
	public static final String INPUT_VALUE_REPEAT                 = "IATOMS-MSG-COMMON-E0023";
	/**
	 * ｛0｝限輸入英數字，請重新輸入
	 */
	public static final String INPUT_LIMIT_ENGLISH_AND_NUMBER     = "IATOMS-MSG-COMMON-E0024";
	/**
	 * 成功
	 */
	public static final String IATOMS_SUCCESS                     = "IATOMS-MSG-COMMON-I0025";
	/**
	 * 失敗
	 */
	public static final String IATOMS_FAILURE                     = "IATOMS-MSG-COMMON-E0025";
	/**
	 * {0}限輸入正整數，請重新輸入
	 */
	public static final String INPUT_NOT_POSITIVE_INTEGER         = "IATOMS-MSG-COMMON-E0026";
	/**
	 *不符日期格式｛0｝格式限YYYY/MM/DD
	 */
	public static final String INPUT_NOT_DATE_FORMAT_YYYY_MM_DD    = "IATOMS-MSG-COMMON-E0027";
	/**
	 *｛0｝格式限HH:mm，0.5小時為單位
	 */
	public static final String INPUT_NOT_TIME_FORMAT_HH_MM         = "IATOMS-MSG-COMMON-E0028";
	/**
	 * 檔名｛0｝超過系統限制上限5M，請重新上傳
	 */
	public static final String UPLOAD_FILE_SIZE_MORE_THAN_SETTING   = "IATOMS-MSG-COMMON-E0029";
	/**
	 *不符日期格式｛0｝格式限YYYY/MM
	 */
	public static final String INPUT_NOT_DATE_FORMAT_YYYY_MM        = "IATOMS-MSG-COMMON-E0030";
	/**
	 *行動電話限輸入09開頭且長度為10碼的數字，請重新輸入
	 */
	public static final String INPUT_PHONE_FORMAT_ERROR         = "IATOMS-MSG-COMMON-E0031";
	/**
	 *｛0｝不可大於｛1｝
	 */
	public static final String INPUT_NOT_MORE_THEN                    = "IATOMS-MSG-COMMON-E0033";
	/**
	 *{0}已存在，請重新輸入
	 */
	public static final String INPUT_VALUE_EXISTS                    = "IATOMS-MSG-COMMON-E0034";
	
	/**
	 * {0}已存在
	 */
	public static final String PARAM_EXISTS							= "IATOMS-MSG-COMMON-E0046";
	/**
	 * {0}不存在
	 */
	public static final String PARAM_NOT_EXISTS						= "IATOMS-MSG-COMMON-E0047";
	/**
	 * 電話格式錯誤
	 */
	public static final String INPUT_PHONE_ERROR					= "IATOMS-MSG-COMMON-E0035";
	/**
	 * 第{0}行,{1}不可为空
	 */
	public static final String ROW_INDEX_IS_NOT_EMPTY					= "IATOMS-MSG-COMMON-E0036";
	/**
	 * 第{0}行和第{1}行
	 */
	public static final String ROW_AND_ROW								= "IATOMS-MSG-COMMON-E0037";
	/**
	 * 第{0}行
	 */
	public static final String ROW_INDEX								= "IATOMS-MSG-COMMON-E0038";
	/**
	 * 錯誤訊息
	 */
	public static final String PARAM_ERROR_INFORMATION					= "IATOMS-MSG-COMMON-E0039";
	/**
	 * 匯入筆數超過系統限制上限{0}，請調整後重新匯入
	 */
	public static final String ABOUCHEMENT_FAILURE_FOR_OVER_LIMIT		= "IATOMS-MSG-COMMON-E0040";
	/**
	 *第{0}行,{1}限英數字
	 */
	public static final String ROW_INDEX_LIMIT_ENGLISH_AND_NUMBER		= "IATOMS-MSG-COMMON-E0041";
	/**
	 * 第{0}行,{1}格式有誤
	 */
	public static final String ROW_INDEX_FORMAT_ERROR					= "IATOMS-MSG-COMMON-E0042";
	/**
	 * 第{0}行,{1}在{2}不存在
	 */
	public static final String  ROW_INDEX_IS_NOT_EXIST					= "IATOMS-MSG-COMMON-E0043";
	
	/**
	 * 第{0}行,{1}长度限{2}字符
	 */
	public static final String ROW_INDEX_LIMIT_LENGTH					= "IATOMS-MSG-COMMON-E0044";
	/**
	 * 第{0}行,{1}
	 */
	public static final String ROW_AND_COLUMN_NAME						= "IATOMS-MSG-COMMON-E0045";
	/**
	 * ｛0｝限數字，請重新輸入
	 */
	public static final String INPUT_LIMIT_NUMBER						= "IATOMS-MSG-COMMON-E0051";
	/**
	 * 儲存成功
	 */
	public static final String SAVE_DATA_SUCCESS					= "IATOMS-MSG-DB-I0001";
	/**
	 * {0}儲存成功
	 */
	public static final String DATA_SAVE_SUCCESS					= "FWK-MSG-DB-I0006";
	/**
	 * 儲存失敗
	 */
	public static final String SAVE_DATA_FAILURE					= "IATOMS-MSG-DB-E0001";
	
	/**
	 * {0}存儲失敗
	 */
	public static final String DATA_SAVE_FAILURE					= "IATOMS-MSG-COMMON-E0050";
	/**
	 * 资料状态已更新，请重新查询
	 */
	public static final String DATA_ALERDAY_UPDATE					= "IATOMS-MSG-COMMON-E0052";
	/**
	 * {0}限正整數，請重新輸入
	 */
	public static final String INPUT_POSITIVE_NUMBER					= "IATOMS-MSG-COMMON-E0053";
	
	/**
	 * 汇入失败,是否下载错误讯息档?
	 */
	public static final String ABOUCHEMENT_FAILURE								="IATOMS-MSG-03010-E0002";
	
	/**
	 * 使用者{0}BPM認證失敗
	 */
	public static final String BPM_AUTHENTICATION_FAILED			 			= "FWK-HW-MSG-BPM-E0002";
	/**
	 * 执行汇入
	 */
	public static final String EXECUTE_ABOUCHEMENT								= "FWK-MSG-DB-L0006";
	
	/**
	 * 数据为空
	 */
	public static final String IATOMS_MSG_NONE_DATA							= "IATOMS-MSG-DATA-W0001";
	
	/**
	 * {0}{1}已存在,請重新輸入!
	 */
	public static final String IATOMS_MSG_EXISTED_DATA						= "IATOMS-MSG-DATA-E0001";
	
	/**
	 * {0}不存在,請重新輸入!
	 */
	public static final String IATOMS_MSG_NAT_EXIST_DATA                      ="IATOMS-MSG-DATA-E0002";
	/**
	 * 发送邮件失败
	 */
	public static final String MAIL_FAILURE										= "IATOMS-MSG-MAIL-E0001";
	
	/**
	 * 系統錯誤
	 */
	public static final String SYSTEM_FAILED                          = "IATOMS-MSG-COMMON-E0099";
	
	/**
	 * UserId為空
	 */
	public static final String USERID_NOT_INPUT                   = "IATOMS-MSG-COMMON-E0098";
	/**
	 * 特店代號已存在
	 */
	public static final String MID_REPEAT							="IATOMS-MSG-BIM02070-E001";
	/**
	 * 合約編號已存在
	 */
	public static final String CONTRACT_ID_REPEAT					="IATOMS-MSG-BIM03030-E001";
	
	/**
	 * 合約編號沒有使用
	 */
	public static final String CONTRACT_ID_NOT_USERD                 = "IATOMS-MSG-ADM03030-I0001";
	/**
	 * 同一廠商下的倉庫名稱不能重複
	 */
	public static final String WAREHOUSE_NAME_REPEAT			= "IATOMS-MSG-BIM02050-E001";
	/**
	 * 同一客戶、同一特店下，表頭不可重複，請重新輸入
	 */
	public static final String MERCHANT_HEADER_NAME_NOT_REPEAT = "IATOMS-MSG-BIM02080-E001";  
	/**
	 * 部門名稱不可重複，請重新輸入
	 */
	public static final String DEPT_NAME_REPEAT					= "IATOMS-MSG-BIM02020-E001";  
	/**
	 * 驗收成功
	 */
	public static final String ACCEPTANCE_SUCCESS				= "IATOMS-MSG-ACCEPTANCE_I0001";
	
	/**
	 * 驗收失敗
	 */
	public static final String ACCEPTANCE_FAILURE				= "IATOMS-MSG-ACCEPTANCE_E0001";
	/**
	 * 驗收完成失敗
	 */
	public static final String FINISH_ACCEPTANCE_FAILURE		= "IATOMS-MSG-DMM03040_E0002";
	/**
	 * 實際驗收未達標準
	 */
	public static final String FINISH_ACCEPTANCE_UNQUALIFIED		= "IATOMS-MSG-DMM03040_E0003";
	/**
	 * 實際驗收失敗
	 */
	public static final String ACTUAL_ACCEPTANCE_FAILURE			= "IATOMS-MSG-DMM03040_E0001";
	/**
	 * 客戶實際驗收失敗
	 */
	public static final String CUSTOMES_ACCEPTANCE_FAILURE			= "IATOMS-MSG-DMM03040_E0004";
	/**
	 * 未驗收成功，不可入庫
	 */
	public static final String ACCEPTANCE_NOUPTO_STANDARD		= "IATOMS-MSG-ACCEPTANCE_E0003";
	/**
	 * 資料未驗收
	 */
	public static final String ACCEPTANCE_MSG					= "IATOMS-MSG-ACCEPTANCE_E0002";
	
	/**
	 * 入庫成功
	 */
	public static final String STORAGE_SUCCESS					= "IATOMS-MSG-STORAGE_I0001";
	/**
	 * 入庫失敗
	 */
	public static final String STORAGE_FAILURE					= "IATOMS-MSG-STORAGE_E0001";
	/**
	 * 轉出成功
	 */
	public static final String TRANSFER_SUCCESS  				= "IATOMS-MSG-TRANSFER_I0001";
	/**
	 * 轉出失敗
	 */
	public static final String TRANSFER_FAILURE					= "IATOMS-MSG-TRANSFER_E0001";
	/**
	 * 文檔格式錯誤，請查驗后再操作
	 */
	public static final String FILE_FORMAT_ERROR				= "IATOMS-MSG-FORMAT_E0001";
	/**
	 * 匯入失敗
	 */
	public static final String UPLOAD_FAILURE					= "IATOMS-MSG-UPLOAD_E0001";
	
	/**
	 * {0}匯入失敗
	 */
	public static final String UPLOAD_FAILURE_TION				= "IATOMS-MSG-UPLOAD_E0002";
	/**
	 * 匯入成功
	 */
	public static final String UPLOAD_SECCUSS					= "IATOMS-MSG-UPLOAD_I0001";
	/**
	 * {0}匯入成功
	 */
	public static final String UPLOAD_SECCUSS_TION					= "IATOMS-MSG-UPLOAD_I0002";
	/**
	 * 格式錯誤，支援上傳副檔名為xls、xlsx
	 */
	public static final String UPLOAD_FORMAT_ERROR					= "IATOMS-MSG-UPLOAD_I0003";
	/**
	 * 此帳號不存在，請洽系統管理員
	 */
	public static final String ACCOUNT_ID_NOT_FOUND               = "IATOMS-MSG-ADM01000-E0001";
	/**
	 * 此帳號已被終止，請洽系統管理員
	 */
	public static final String ACCOUNT_ID_DISABLED               = "IATOMS-MSG-ADM01000-E0002";
	/**
	 * 帳號已被停權無法登入，請洽系統管理員重新設定後才可登入
	 */
	public static final String ACCOUNT_ID_STOP_RIGHT             = "IATOMS-MSG-ADM01000-E0003";
	/**
	 * 密碼錯誤，請重新輸入
	 */
	public static final String PWD_ERROR						= "IATOMS-MSG-ADM01000-E0004";
	/**
	 * 密碼過期，請洽系統管理員
	 */
	public static final String PWD_OVERDUE                  	= "IATOMS-MSG-ADM01000-E0005";
	/**
	 * 密碼錯誤{0}次，達{1}次，則帳號將被鎖定
	 */
	public static final String PWD_ERROR_TIMES					= "IATOMS-MSG-ADM01000-E0006";
	/**
	 * 密碼錯誤已達{0}次，帳號已被鎖定，請洽系統管理員重新設定後才可登入
	 */
	public static final String PWD_ERROR_TIMES_TO_LOCK      	= "IATOMS-MSG-ADM01000-E0007";
	/**
	 * 首次登入需修改密碼
	 */
	public static final String NEW_ACCOUNT_PWD_CHANGE       	= "IATOMS-MSG-ADM01000-E0008";
	/**
	 * 密碼即將過期提醒
	 * 您的密碼將在{0}天後到期，是否進行變更？(密碼過期後，須請管理員重新設定)
	 */
	public static final String PWD_DUE_WARING                   = "IATOMS-MSG-ADM01000-E0009";
	/**
	 * 舊密碼輸入有誤，請重新輸入
	 */
	public static final String OLD_PWD_ERROR             	   = "IATOMS-MSG-ADM01000-E0010";
	/**
	 * 密碼變更成功
	 */
	public static final String PWD_SAVE_SUCCESS					= "IATOMS-MSG-ADM01020-E0008";
	/**
	 * 舊密碼与新密碼相同，請重新輸入
	 */
	public static final String OLDPWD_NOT_EQUAL_NEWPWD     			= "IATOMS-MSG-ADM01020-E0009";
	/**
	 * 新密碼不可與前{0}次相同，請重新輸入
	 */
	public static final String NEW_PWD_REPEAT						= "IATOMS-MSG-ADM01000-E0011";
	/**
	 * 檢核密碼失敗
	 */
	public static final String CHECK_PWD_ERROR             			= "IATOMS-MSG-ADM01000-E0012";
	/**
	 * 此帳號已被鎖定，請洽系統管理員
	 */
	public static final String ACCOUNT_ID_LOCK						= "IATOMS-MSG-ADM01000-E0013";
	
	/**
	 * 密碼已被重置，請修改密碼
	 */
	public static final String RESET_PWD_NEED_EDIT						= "IATOMS-MSG-ADM01000-E0014";
	/**
	 * 帳號檢查成功
	 */
	public static final String ACCOUNT_CHECKED_SUCCESS						= "IATOMS-MSG-ADM01010-I0001";
	/**
	 * 此帳號已存在
	 */
	public static final String ACCOUNT_REPEAT								= "IATOMS-MSG-ADM01010-E0001";
	/**
	 * 帳號限輸入長度為4~20碼的英文或數字，請重新輸入
	 */
	public static final String ACCOUNT_FORMAT_ERROR							= "IATOMS-MSG-ADM01010-E0002";
	/**
	 * 密碼長度限｛0｝~｛1｝之間，請重新輸入
	 */
	public static final String PWD_LENGTH_OVER_LIMIT						= "IATOMS-MSG-ADM01010-E0003";
	/**
	 * 密碼限英數字或英文符號或英數符號，請重新輸入
	 */
	public static final String PWD_CHARACTER_FORMAT_ERROR					= "IATOMS-MSG-ADM01010-E0004";
	/**
	 * 密碼不可為鍵盤上的字母順序(asdf)，請重新輸入
	 */
	public static final String PWD_CHARACTER_ORDER_ERROR					= "IATOMS-MSG-ADM01010-E0005";
	/**
	 * 密碼不可為連續的數字，請重新輸入
	 */
	public static final String PWD_NUMBER_FORMAT_ERROR						= "IATOMS-MSG-ADM01010-E0006";
	/**
	 * 密碼不可使用重複的字元(不可重複三次)，請重新輸入
	 */
	public static final String PWD_CHARACTER_REPEAT_ERROR					= "IATOMS-MSG-ADM01010-E0007";
	/**
	 * 密碼不能與帳號相同或部分相同，請重新輸入
	 */
	public static final String PWD_SAME_ACCOUNT_ERROR						= "IATOMS-MSG-ADM01010-E0008";
	/**
	 * 確認密碼與密碼不一致，請重新輸入
	 */
	public static final String REPWD_EQUAL_TO_PWD							= "IATOMS-MSG-ADM01010-E0009";
	/**
	 * 此帳號非Cyber帳號
	 */
	public static final String ACCOUNT_IS_NOT_CYBER							= "IATOMS-MSG-ADM01010-E0010";
	/**
	 * 公司網域驗證失敗，請聯繫系統管理員
	 */
	public static final String LDAP_IS_FAILURE								= "IATOMS-MSG-ADM01010-E0011";
	/**
	 * 新密碼長度限｛0｝~｛1｝之間，請重新輸入
	 */
	public static final String NEW_PWD_LENGTH_OVER_LIMIT					= "IATOMS-MSG-ADM01020-E0001";
	/**
	 * 新密碼限英數字或英文符號或英數符號，請重新輸入
	 */
	public static final String NEW_PWD_CHARACTER_FORMAT_ERROR				= "IATOMS-MSG-ADM01020-E0002";
	/**
	 * 新密碼不可為鍵盤上的字母順序(asdf)，請重新輸入
	 */
	public static final String NEW_PWD_CHARACTER_ORDER_ERROR				= "IATOMS-MSG-ADM01020-E0003";
	/**
	 * 新密碼不可為連續的數字，請重新輸入
	 */
	public static final String NEW_PWD_NUMBER_FORMAT_ERROR					= "IATOMS-MSG-ADM01020-E0004";
	/**
	 * 新密碼不可使用重複的字元(不可重複三次)，請重新輸入
	 */
	public static final String NEW_PWD_CHARACTER_REPEAT_ERROR				= "IATOMS-MSG-ADM01020-E0005";
	/**
	 * 新密碼不能與帳號相同或部分相同，請重新輸入
	 */
	public static final String NEW_PWD_SAME_ACCOUNT_ERROR					= "IATOMS-MSG-ADM01020-E0006";
	/**
	 * 確認密碼與新密碼不一致，請重新輸入
	 */
	public static final String REPWD_EQUAL_TO_NEW_PWD						= "IATOMS-MSG-ADM01020-E0007";
	
	/**
	 * DTID起迄區間重疊，請重新輸入
	 */
	public static final String DTID_IS_REPEAT								= "IATOMS-MSG-PVM04020-E0001";
	
	public static final String DTID_IS_USE									= "IATOMS-MSG-PVM04020-E0002";
	/**
	 *  角色已被佔用，刪除失敗！
	 */
	public static final String DELETE_ROLE_FAILURE				= "IATOMS-MSG-ADM01030-E001";
	
	/**
	 * 角色代號不能重複
	 */
	public static final String ROLE_CODE_REPEATED				= "IATOMS-MSG-ADM01030-E002";
	
	/**
	 * 角色名稱不能重複
	 */
	public static final String ROLE_NAME_REPEATED				= "IATOMS-MSG-ADM01030-E003";
	
	/**
	 * 角色代號可以使用
	 */
	public static final String ROLE_CODE_NOT_USED				= "IATOMS-MSG-ADM01030-I001";
	
	/**
	 * 角色名稱可以使用
	 */
	public static final String ROLE_NAME_NOT_USED				= "IATOMS-MSG-ADM01030-I002";
	
	/**
	 * 倉庫名稱不能重複
	 */
	public static final String WAREHOUSE_NAME_REPEATED			= "IATOMS-MSG-BIM02050-E001";
	
	/**
	 * 資料權限儲存成功
	 */
	public static final String SAVE_ROLE_PERMISSION_SUCCESS		= "IATOMS-MSG-BIM02050-E010";
	
	/**
	 * 同一種植種類不可設定２筆同樣的Mail
	 */
	public static final String MAIL_ADDRESS_REPEATED			= "IATOMS-MSG-BIM02090-E001";
	/**
	 * 系統參數維護欄位名稱--參數代碼
	 */
	public static final String BASE_PARAMETER_FIELD_ITEM_VALUE	= "IATOMS-MSG-ADM01050-W001";
	/**
	 * 系統參數維護欄位名稱--參數名稱
	 */
	public static final String BASE_PARAMETER_FIELD_ITEM_NAME	= "IATOMS-MSG-ADM01050-W002";
	
	/**
	 * 設備代碼不可重複，請重新輸入
	 */
	public static final String ASSET_TYPE_ID_REPEAT				= "IATOMS-MSG-ADM01050-E001";
	/**
	 * 設備名稱不可重複，請重新輸入
	 */
	public static final String ASSET_NAME_REPEAT				= "IATOMS-MSG-ADM01050-E002";
	/**
	 * 該設備尚有庫存設備，不可刪除
	 */
	public static final String ASSET_HAVE_INVENTORY				= "IATOMS-MSG-DMM03010-E0001";
	/**
	 * 特店代號限輸入英數字，請重新輸入
	 */
	public static final String NUMBER_OR_ENGLISH_INPUT			= "IATOMS-MSG-BIM02070-E006";
	/**
	 * {0}限英數字
	 */
	public static final String NUMBER_OR_ENGLISH				= "IATOMS-MSG-BIM02070-E007";
	/**
	 * 同一客戶下,特店代號不可重複，請重新輸入
	 */
	public static final String MERCHANT_CODE_REPEAT					= "IATOMS-MSG-BIM02070-E001";
	/**
	 * 請輸入特店代號
	 */
	public static final String MERCHANT_CODE_INPUT					= "IATOMS-MSG-BIM02070-E003";
	/**
	 * 同一客戶下,分期特店代號不可重複，請重新輸入
	 */
	public static final String STAGES_MERCHANT_CODE_REPEAT			= "IATOMS-MSG-BIM02070-E002";
	/**
	 * 請輸入特店名稱
	 */
	public static final String NAME_NOT_INPUT						= "IATOMS-MSG-BIM02070-E004";
	/**
	 * 該特店已設置特店表頭，不可刪除
	 */
	public static final String MERCHANT_HAS_HEADER_NOT_DELETE		= "IATOMS-MSG-BIM02070-E005";
	/**
	 * 特店代號已存在
	 */
	public static final String MERCHANT_EXIST						= "IATOMS-MSG-BIM02070-E011";
	/**
	 * 分期特店代號已存在
	 */
	public static final String STAGES_MERCHANT_EXIST				= "IATOMS-MSG-BIM02070-E012";
	/**
	 * 表頭不能重複，請重新輸入
	 */
	public static final String MERCHANT_ANNOUNCED_NAME_NOT_REPEAT = "IATOMS-MSG-BIM02080-E001";
	/**
	 * 同一特店,表頭不可重複,請重新輸入
	 */
	public static final String MERCHANT_HEAD_NAME_NOT_REPEAT 		= "IATOMS-MSG-BIM02070-E0011";
	/**
	 * 同一特店,其他信息輸入不一致
	 */
	public static final String MERCHANT_OTHER_INFO_NOT_CONSISTENT = "IATOMS-MSG-BIM02070-E0002";
	/**
	 * 格式有誤,時間起迄限輸入HH:mm
	 */
	public static final String OPEN_TIME_FORMAT     			= "IATOMS-MSG-BIM02080-E004";
	/**
	 * 營業時間起不可大於營業時間迄
	 */
	public static final String TIME_OUT_INPUT						= "IATOMS-MSG-BIM02070-E008";
	/**
	 * AOEmail格式有誤，請重新輸入
	 */
	public static final String EMAIL_INPUT							= "IATOMS-MSG-BIM02070-E009";
	/**
	 * 行動電話限輸入09開頭且長度為10碼的數字，請重新輸入
	 */
	public static final String PHONE_INPUT							= "IATOMS-MSG-BIM02070-E010";
	/**
	 * 該特店代號不存在
	 */
	public static final String MERCHANT_CODE_NOT_EXIST          = "IATOMS-MSG-BIM02080-E002";
	/**
	 * 儲存失敗
	 */
	public static final String IATOMS_MSG_COMMON_E0001			= "IATOMS-MSG-COMMON-E0001";
	/**
	 * 年度行事曆設定成功
	 */
	public static final String CALENDAR_YEAR_PERMISSION_SUCCESS          = "IATOMS-MSG-BIM02060-E001";
	/**
	 * 行事曆設定成功
	 */
	public static final String CALENDAR_DAY_PERMISSION_SUCCESS			= "IATOMS-MSG-BIM02060-E002";
	
	/**
	 * {0}刪除成功
	 */
	public static final String EXISTED_USER_WITH_COMPANY_ID      = "IATOMS-MSG-COMPANY-E0001";
	/**
	 * 儲存成功，已列入排程處理，稍後會收到結果通知
	 */
	public static final String REPORT_SEND_SUCCESS				= "FWK-MSG-BIM02100-I0001";
	/**
	 * 該部門已設定使用者帳號，不可刪除
	 */
	public static final String EXISTED_USER_WITH_DEPT_CODE      = "IATOMS-MSG-BIM02020-E002";
	/**
	 * 設備序號重複
	 */
	public static final String SERIAL_NUMBER_REPETITION				= "IATOMS-MSG-DMM03030-E001";
	/**
	 * 財產編號重複
	 */
	public static final String PROPERTY_ID_REPETITION				= "IATOMS-MSG-DMM03030-E002";
	
	/**
	 * 設備序號不可重複，請重新輸入
	 */
	public static final String ASSET_NUMBER_IS_REPETITION					= "IATOMS-MSG-DMM03040-E001";
	/**
	 * 財產編號不可重複，請重新輸入
	 */
	public static final String PROPERTY_ID_IS_REPETITION								= "IATOMS-MSG-DMM03040-E002";
	
	/**
	 * 第{0}行,{1}在{2}
	 */
	public static final String ROW_AND_COLUMN_IN						= "IATOMS-MSG-BIM02070-I005";
	
	/**
	 * 格式有誤
	 */
	public static final String FORMATS_ERROR								= "IATOMS-MSG-BIM02070-I006";
	
	/**
	 * 同一合約編號、同一案件類別、同一區域、同一案件類型僅可設定一筆SLA，請重新輸入
	 */
	public static final String SLA_SAVE_FAILURE_FOR_REPEAT					= "IATOMS-MSG-BIM02040-E0001";
	/**
	 * “當天件”，{0}需為24倍數(天)，請重新輸入
	 */
	public static final String MULTIPLE_OF_TWENTY_FOUR						= "IATOMS-MSG-BIM02040-E0002";
	/**
	 * 同一合約編號、同一案件類別、同一區域、同一案件類型僅可設定一筆SLA
	 */
	public static final String CONTRACT_SLA_REPEAT							="IATOMS-MSG-BIM02040-E0003";
	/**
	 * 同一合約編號、同一案件類別、同一區域、同一案件類型僅可設定一筆SLA，複製失敗
	 */
	public static final String SLA_COPY_FAILURE_FOR_REPEAT					= "IATOMS-MSG-BIM02040-E0004";
	/**
	 * 檔內重複
	 */
	public static final String REPEAT_FOR_DOCUMENT							= "IATOMS-MSG-BIM02040-E0005";
	/**
	 * 資料庫重複
	 */
	public static final String REPEAT_FOR_DATA_BASE							= "IATOMS-MSG-BIM02040-E0006";
	/**
	 * 倉庫查無此設備序號
	 */
	public static final String NO_SERIAL_NUMBER_IN_WAREHOUSE			= "IATOMS-MSG-DMM03050-I004";
	/**
	 * 此設備序號狀態不為庫存、已拆回，無法轉出
	 */
	public static final String SERIAL_NUMBER_NO_IN_REPOSITORY			= "IATOMS-MSG-DMM03050-I011";
	/**
	 * 儲存轉倉說明成功
	 */
	public static final String SAVE_TRANS_COMMENT_SUCCESS				= "IATOMS-MSG-DMM03050-I007";
	/**
	 * 儲存轉倉說明失敗
	 */
	public static final String SAVE_TRANS_COMMENT_FALURE				= "IATOMS-MSG-DMM03050-I008";
	/**
	 * {0}轉倉成功
	 */
	public static final String TRANS_SUCCESS							= "IATOMS-MSG-DMM03050-I005";
	/**
	 * {0}轉倉失敗
	 */
	public static final String TRANS_FALURE								= "IATOMS-MSG-DMM03050-E002";
	/**
	 * 轉入倉已被刪除，不可進行確認入庫操作
	 */
	public static final String TRANS_DELETED_TO_WAREHOUSE				= "IATOMS-MSG-DMM03050-E001";
	/**
	 * {0}轉倉資料已送出，待轉入驗收
	 */
	public static final String TRANS_OUT_SUCCESS						= "IATOMS-MSG-DMM03050-I010";
	/**
	 * {0}轉倉退回成功
	 */
	public static final String TRANS_BACK_SUCCESS						= "IATOMS-MSG-DMM03050-I009";
	
	/**
	 * 設備名稱與設備序號不匹配，不可異動財產編號
	 */
	public static final String SERIAL_NUMBER_NOTEQ_ASSET_NAME			= "IATOMS-MSG-DMM03030-E0001";
	/**
	 * {}長度限{}
	 */
	public static final String PARAM_LENGTH_IS_INVALID					= "IATOMS-MSG-COMMON-E0048";
	
	/**
	 * {}為空
	 */
	public static final String PARAM_VALUE_IS_EMPTY						= "IATOMS-MSG-COMMON-E0049";
	/**
	 * {}格式有誤，多筆請用分號區隔，請重新輸入 
	 */
	public static final String PARAM_VALUE_IS_ERROR						= "IATOMS-MSG-COMMON-E0054";
	/**
	 * {}格式錯誤，限英文符號、英文、數字
	 */
	public static final String INPUT_ENGLISH_NUMBER_OR_ENGLISH_SYMBOLS	= "IATOMS-MSG-COMMON-E0055";
	/**
	 * 取消轉倉成功
	 */
	public static final String CANCLE_TRANS_SUCCESS						= "IATOMS-MSG-DMM03050-I006";
	/**
	 * 該公司已設定使用者帳號，不可刪除
	 */
	public static final String COMPANY_USER_EXISTS						= "IATOMS-MSG_BIM02010_E0001";
	public static final String COMPANY_CODE_REPEAT						= "IATOMS-MSG_COMMON_E0023";
	public static final String CUSTOMER_CODE_REPEAT						= "IATOMS-MSG_COMMON_E0023";
	
	/**
	 * 領用成功
	 */
	public static final String PARAM_USE_SUCCESS						= "IATOMS-MSG-USE_I0001";
	
	/**
	 * 領用失敗
	 */
	public static final String PARAM_USE_FAILURE						= "IATOMS-MSG-USE_E0001";

	/**
	 * 借用失敗
	 */
	public static final String PARAM_BORROW_FAILURE						="IATOMS-MSG-BORROW_E0001";
	
	/**
	 * 借用成功
	 */
	public static final String PARAM_BORROW_SUCCESS						="IATOMS-MSG-BORROW_I0001";
	
	/**
	 * 報廢成功
	 */
	public static final String PARAM_SCRAP_SUCCESS						="IATOMS-MSG-SCRAP_I0001";
	
	/**
	 * 報廢失敗
	 */
	public static final String PARAM_SCRAP_FAILURE						="IATOMS-MSG-SCRAP_E0001";
	
	/**
	 * 待報廢失敗
	 */
	public static final String PARAM_TO_BE_SCRAPPED_FAILURE						="IATOMS-MSG-TO_BE_SCRAPPED_E0001";
	
	/**
	 * 待報廢成功
	 */
	public static final String PARAM_TO_BE_SCRAPPED_SUCCESS							="IATOMS-MSG-TO_BE_SCRAPPED_I0001";
	
	/**
	 * 歸還失敗
	 */
	public static final String PARAM_BACK_FAILURE						="IATOMS-MSG-BACK_E0001";
	
	/**
	 * 歸還成功
	 */
	public static final String PARAM_BACK_SUCCESS							="IATOMS-MSG-BACK_I0001";
		
	/**
	 * 退回成功
	 */
	public static final String PARAM_RETURN_SUCCESS							="IATOMS-MSG-RETURN_I0001";
	
	/**
	 * 退回失敗
	 */
	public static final String PARAM_RETURN_FAILURE							="IATOMS-MSG-RETURN_E0001";
	
	/**
	 * 銷毀失敗
	 */
	public static final String PARAM_DESTROY_FAILURE							="IATOMS-MSG-DESTROY_E0001";
	
	/**
	 * 銷毀成功
	 */
	public static final String PARAM_DESTROY_SUCCESS							="IATOMS-MSG-DESTROY_I0001";
	
	/**
	 * 維修成功
	 */
	public static final String PARAM_REPAIR_SUCCESS							="IATOMS-MSG-REPAIRED_I0001";
	
	/**
	 * 維修失敗
	 */
	public static final String PARAM_REPAIR_FAILURE							="IATOMS-MSG-REPAIRED_E0001";
	/**
	 * 送修成功
	 */
	public static final String PARAM_REPAIRED_SUCCESS							="IATOMS-MSG-REPAIR_I0001";
	
	/**
	 * 送修失敗
	 */
	public static final String PARAM_REPAIRED_FAILURE							="IATOMS-MSG-REPAIR_E0001";
	
	/**
	 * 解除綁定成功
	 */
	public static final String PARAM_REMOVE_SUCCESS							="IATOMS-MSG-REMOVE_I0001";
	
	/**
	 * 解除綁定失敗
	 */
	public static final String PARAM_REMOVE_FAILURE							="IATOMS-MSG-REMOVE_E0001";
	
	/**
	 * 台新租賃維護成功
	 */
	public static final String PARAM_TAIXIN_RENT_SUCCESS							="IATOMS-MSG-TAIXIN_RENT_I0001";
	
	/**
	 * 台新租賃維護失敗
	 */
	public static final String PARAM_TAIXIN_RENT_FAILURE							="IATOMS-MSG-TAIXIN_RENT_E0001";
	
	/**
	 * 派工成功
	 */
	public static final String PARAM_DISPACH_SUCCESS								="IATOMS-MSG-SRM05020-I0088";
	/**
	 * 派工失敗
	 */
	public static final String PARAM_DISPACH_FAILURE								="IATOMS-MSG-SRM05020-E0088";
	/**
	 * 案件編號{0}，請設定交易參數
	 */
	public static final String PARAM_CASE_NO_TRANS_PARAM							="IATOMS-MSG-SRM05020-E0089";
	public static final String WARE_HAVE_ASSET								= "IATOMS-MSG_BIM02050_E0001";
	/**
	 * 此倉庫中不存在此設備狀態的該設備
	 */
	public static final String WARHOUSE_NOT_EXIST_STATUS_ASSET              ="IATOMS-MSG_DMM_3080_E0001";
	
	/**
	 * {0}限輸入正整數，請重新輸入
	 */
	public static final String POSITIVE_INTEGER								= "IATOMS-MSG-BIM02030-E001";
	
	/**
	 * 請至少輸入一筆設備資料
	 */
	public static final String CHOOSE_CONTRACT_ASSET						= "IATOMS-MSG-BIM02030-E002";
	/**
	 * 無資料權限，無法異動
	 */
	public static final String NO_UPDATE_PERMISSIONS						= "IATOMS-MSG-BIM02030-E003";
	/**
	 * 行動電話限輸入09開頭且長度為10碼的數字，請重新輸入
	 */
	public static final String MOBLIE_PHINE_FORMAT_ERROR						= "IATOMS-MSG-BIM02080-E006";
	/**
	 * {0}格式有誤，請重新輸入
	 */
	public static final String FORMAT_ERROR			            			= "IATOMS-MSG-BIM02080-E007";
	/**
	 * 無資料，不可匯出
	 */
	public static final String NO_DATA_FOR_EXPORT										= "IATOMS-MSG-DMM03100-E0001";
	/**
	 * 程式名稱+版本編號不可重複，請重新輸入
	 */
	public static final String APPLICATION_VERSION_NAME_REPEAT				= "IATOMS-MSG-PVM04010-E001";
	/**
	 * 耗材名稱不可重複,請重新輸入
	 */
	public static final String SUPPLIES_NAME_REPEAT							= "IATOMS-MSG-BIM03020-E0001";
	
	/**
	 * 交易參數
	 */
	public static final String TABLE_SRM_TRANSACTION_PARAMETER					= "IATOMS-MSG-TABLE-I0001";
	
	/**
	 * 客戶：{客戶} +，機型：{機型}無可使用之DTID，請于【DTID號碼管理】設定
	 */
	public static final String NO_DITD_FOR_CUSTOMER_AND_TYPE						= "IATOMS-MSG-SRM05020-E0001";
	
	/**
	 * 週邊設備選項XXX已重覆
	 */
	public static final String PERIPHERALS_IS_REPEAT								= "IATOMS-MSG-SRM05020-E0002";
	
	/**
	 * 設備功能XXX已重覆
	 */
	public static final String BUILT_IN_FEATURE_IS_REPEAT							= "IATOMS-MSG-SRM05020-E0003";
	
	/**
	 * {0}限輸入正整數
	 */
	public static final String ONLY_ALLOW_INTEGER									= "IATOMS-MSG-SRM05020-E0004";
	
	/**
	 * 請至少設定一筆交易參數
	 */
	public static final String AT_LEAST_ONE_TRANS_PARAMETER							= "IATOMS-MSG-SRM05020-E0005";
	
	/**
	 * 請設定交易類別為“一般交易”的交易參數
	 */
	public static final String TRANSACTION_TYPE_IS_COMMON							= "IATOMS-MSG-SRM05020-E0006";
	
	/**
	 * 未選取CUP或Smartpay交易類別，不可選取Pinpad設備及功能
	 */
	public static final String NO_CUP_OR_SMARTPAY_TRANS_TYPE						= "IATOMS-MSG-SRM05020-E0007";
	
	/**
	 * 選取CUP或Smartpay交易類別，要選取Pinpad設備及功能
	 */
	public static final String NEED_PINPAD_ASSET_FUNCTION							= "IATOMS-MSG-SRM05020-E0008";
	
	/**
	 * 無對應SLA，請到【合約SLA設定】設定
	 */
	public static final String NO_SLA_FOR_CASE										= "IATOMS-MSG-SRM05020-E0009";
	/**
	 * 案件編號{0}無對應SLA，請到【合約SLA設定】設定
	 */
	public static final String NO_SLA_FOR_CASE_PARAM								= "IATOMS-MSG-SRM05020-E0018";
	/**
	 * 該合約未設定上班時間，請到【合約維護】設定
	 */
	public static final String NO_WORK_TIME_FOR_CONTRACT							= "IATOMS-MSG-SRM05020-E0019";
	/**
	 * 請至少設定一筆新的交易參數
	 */
	public static final String AT_LEAST_ONE_NEW_TRANS_PARAMETER						= "IATOMS-MSG-SRM05020-E0010";
	
	/**
	 * 新增
	 */
	public static final String UPDATE_DESCRIPTION_ADD								= "IATOMS-MSG-SRM05020-I0001";
	/**
	 * 刪除
	 */
	public static final String UPDATE_DESCRIPTION_DELETE							= "IATOMS-MSG-SRM05020-I0034";
	/**
	 * 郵件發送成功
	 */
	public static final String SENDEMAIL_SUCCESS									= "IATOMS-MSG-SRM05020-I0011";
	
	/**
	 * 郵件發送失敗
	 */
	public static final String SENDEMAIL_FAILURE									= "IATOMS-MSG-SRM05020-E0024";
	/**
	 * 送出成功
	 */
	public static final String SEND_SUCCESS											= "IATOMS-MSG-SRM05040-I0007";
	/**
	 * 送出失敗
	 */
	public static final String SEND_FAILURE											= "IATOMS-MSG-SRM05040-E0007";
	/**
	 * 退回成功
	 */
	public static final String BACK_SUCCESS											= "IATOMS-MSG-SRM05040-I0008";
	/**
	 * 退回失敗
	 */
	public static final String BACK_FAILURE											= "IATOMS-MSG-SRM05040-E0008";
	/**
	 * 鎖定成功
	 */
	public static final String LOCKING_SUCCESS										= "IATOMS-MSG-SRM05040-I0009";
	/**
	 * 鎖定失敗
	 */
	public static final String LOCKING_FAILURE										= "IATOMS-MSG-SRM05040-E0009";
	/**
	 * 完成成功
	 */
	public static final String COMPLETE_SUCCESS										= "IATOMS-MSG-SRM05040-I0010";
	/**
	 * 完成失敗
	 */
	public static final String COMPLETE_FAILURE										= "IATOMS-MSG-SRM05040-E0010";
	/**
	 * {0}輸入錯誤
	 */
	public static final String INPUT_ERROR											= "IATOMS-MSG-SRM05020-E0014";
	/**
	 * {0}重複
	 */
	public static final String INPUT_REPEAT											= "IATOMS-MSG-SRM05020-E0015";
	/**
	 * {0}{1}不屬於{2}{3}
	 */
	public static final String INPUT_NOT_MATCHING									= "IATOMS-MSG-SRM05020-E0016";
	/**
	 * {0}{1}不可操作{2}
	 */
	public static final String INPUT_NOT_UPDATE										= "IATOMS-MSG-SRM05020-E0017";
	/**
	 * 還款成功
	 */
	public static final String RETURN_SUCCESS										= "IATOMS-MSG-SRM05040-I0011";
	/**
	 * 還款失敗
	 */
	public static final String RETURN_FAILURE										= "IATOMS-MSG-SRM05040-E0011";
	/**
	 * 還款設備狀態不為庫存
	 */
	public static final String NOT_IS_REPERTORY										= "IATOMS-MSG-SRM05040-E0012";
	/**
	 * 求償作業查無資料
	 */
	public static final String NOT_QUERY_PAYMENT_INFO								= "IATOMS-MSG-SRM05040-E0013";
	/**
	 * 問題原因：
	 */
	public static final String CASE_PROBLEM_REASON									= "IATOMS-MSG-SRM05020-E0011";
	/**
	 * 解決方式：
	 */
	public static final String CASE_PROBLEM_SOLUTION								= "IATOMS-MSG-SRM05020-E0012";
	/**
	 * 責任歸屬：
	 */
	public static final String CASE_RESPONSIBITY									= "IATOMS-MSG-SRM05020-E0013";
	/**
	 * 案件匯入--核檢完成提示-
	 */
	public static final String CHECK_END_MESSAGE									= "IATOMS-MSG-SRM05020-I0002";
	/**
	 * 設備異動資訊
	 */
	public static final String ASSET_CHANGE_INFORMATION								= "IATOMS-MSG-SRM05020-I0003";
	/**
	 * 新增-EDC:{0},序號:{1}
	 */
	public static final String DESCRIPTION_FOR_ADD_EDC								= "IATOMS-MSG-SRM05020-I0004";
	/**
	 * 新增-週邊設備:{0},序號:{1}
	 */
	public static final String DESCRIPTION_FOR_ADD_PERIPHERALS						= "IATOMS-MSG-SRM05020-I0005";
	/**
	 * 原設備-EDC:{0},序號:{1}
	 */
	public static final String DESCRIPTION_FOR_ORIGINALLY_EDC						= "IATOMS-MSG-SRM05020-I0006";
	/**
	 * 原設備-週邊設備:{0},序號:{1}
	 */
	public static final String DESCRIPTION_FOR_ORIGINALLY_PERIPHERALS				= "IATOMS-MSG-SRM05020-I0007";
	/**
	 * 移除-EDC:{0},序號:{1}
	 */
	public static final String DESCRIPTION_FOR_DELETE_EDC							= "IATOMS-MSG-SRM05020-I0008";
	/**
	 * 移除-週邊設備:{0},序號:{1}
	 */
	public static final String DESCRIPTION_FOR_DELETE_PERIPHERALS					= "IATOMS-MSG-SRM05020-I0009";
	/**
	 * 移除-原設備-EDC:{0},序號:{1}
	 */
	public static final String DESCRIPTION_FOR_DELETE_ORIGINALLY_EDC				= "IATOMS-MSG-SRM05020-I0017";
	/**
	 * 新增-EDC:{0},序號:{1}
	 */
	public static final String DESCRIPTION_FOR_ADD_ORIGINALLY_EDC					= "IATOMS-MSG-SRM05020-I0018";
	/**
	 * 週邊設備:{0},序號:{1}
	 */
	public static final String DESCRIPTION_FOR_PERIPHERALS							= "IATOMS-MSG-SRM05020-I0019";
	/**
	 * 移除-原設備-週邊設備:{0},序號:{1}
	 */
	public static final String DESCRIPTION_FOR_DELETE_ORIGINALLY_PERIPHERALS		= "IATOMS-MSG-SRM05020-I0020";
	/**
	 * EDC:{0},序號:{1}
	 */
	public static final String DESCRIPTION_FOR_EDC									= "IATOMS-MSG-SRM05020-I0021";
	/**
	 * 查核結果:
	 */
	public static final String DESCRIPTION_FOR_CHECK_RESULT							= "IATOMS-MSG-SRM05020-I0010";

	/**
	 * 設備資料修改成功
	 */
	public static final String RESPOSTORY_UPDATE_SUCCESS								="IATOMS-MSG-UPDATE-I001";

	/**
	 * 設備資料修改失敗
	 */
	public static final String RESPOSTORY_UPDATE_FAILURE								="IATOMS-MSG-UPDATE-E001";
	/**
	 * 請選擇同一資料狀態的求償資料
	 */
	public static final String PAYMENT_STATUS_ERROR										= "IATOMS-MSG-SRM05040-E0001";
	
	/**
	 * 案件編號為：{0}的taskId值為空
	 */
	public static final String CASE_TASKID_IS_NULL										= "IATOMS-MSG-SRM05020-E0020";
	
	/**
	 * 請選擇同一案件狀態、案件類別的案件
	 */
	public static final String SAME_STATUS_AND_SAME_CATEGORY							= "IATOMS-MSG-SRM05020-E0021";
	/**
	 * 原端代{0}修改后实际端代为{1}
	 */
	public static final String CHANGE_DTID_MSG											= "IATOMS-MSG-SRM05020-I0033";
	/**
	 * 案件狀態不符，不可進行{0}
	 */
	public static final String CASE_STATUS_NOT_MATCH									= "IATOMS-MSG-SRM05020-E0022";
	/**
	 * 來自{0}訊息：請先將{1}結案
	 */
	public static final String PLEASE_CLOSE_CASE										= "IATOMS-MSG-SRM05020-E0023";
	/**
	 * {0}失敗
	 */
	public static final String CASE_ACTION_FAILURE										= "IATOMS-MSG-SRM05020-E0030";
	/**
	 * {0}成功
	 */
	public static final String CASE_ACTION_SUCCESS										= "IATOMS-MSG-SRM05020-I0030";
	/**
	 * 案件儲存成功，案件編號為：{0}
	 */
	public static final String SAVE_CASE_SUCCESS										= "IATOMS-MSG-SRM05020-I0012";
	/**
	 * 案件複製成功
	 */
	public static final String COPY_CASE_SUCCESS										= "IATOMS-MSG-SRM05020-I0013";
	/**
	 * 案件派工成功，案件編號為：{0}
	 */
	public static final String DISPATCH_CASE_SUCCESS										= "IATOMS-MSG-SRM05020-I0014";
	/**
	 * 異動前：{0}，異動后：{1}
	 */
	public static final String UPDATE_BEFORE_AND_UPDATE_AFTER								= "IATOMS-MSG-SRM05020-I0015";
	/**
	 * 移除【交易類別】
	 */
	public static final String REMOVE_TRANSACTION_PARAMETER									= "IATOMS-MSG-SRM05020-I0016";
	
	/**
	 * (複製)案件派工成功，案件編號為：{0}
	 */
	public static final String DISPATCH_MANY_CASES_SUCCESS										= "IATOMS-MSG-SRM05020-I0031";
	/**
	 * 報表重送異常處理郵件內容：異常時間
	 */
	public static final String REPORT_SEND_EXCEPTION_TIME								= "IATOMS_MSG_BIM02100_I0002";
	/**
	 * 報表重送異常處理郵件內容：異常原因
	 */
	public static final String REPORT_SEND_EXCEPTION_CAUSE								= "IATOMS_MSG_BIM02100_I0003";
	/**
	 * 報表重送異常處理郵件內容：異常位置
	 */
	public static final String REPORT_SEND_EXCEPTION_PATH								= "IATOMS_MSG_BIM02100_I0004";
	/**
	 * 報表重送異常處理郵件主題：${0}_報表重送異常通知
	 */
	public static final String REPORT_SEND_EXCEPTION_SUBJECT							= "IATOMS_MSG_BIM02100_I0005";
	/**
	 * 報表重送異常處理郵件內容：${0}_重送${1}時發生異常，請查看相關日誌，及時進行處理。異常信息如下：
	 */
	public static final String REPORT_SEND_EXCEPTION_CONTEXT							= "IATOMS_MSG_BIM02100_I0006";
	
	/**
	 * 公司基本訊息維護 -- 公司代號重複提示信息的佔位符：公司代號
	 */
	public static final String MSG_COMPANY_CODE_NAME									= "IATOMS_MSG_BIM02010_I0001";
	/**
	 * 公司基本訊息維護 -- 公司簡稱重複提示信息的佔位符：公司簡稱
	 */
	public static final String MSG_SHORT_NAME											= "IATOMS_MSG_BIM02010_I0002";
	/**
	 * 公司基本訊息維護 -- 客戶碼重複提示信息的佔位符：客戶碼
	 */
	public static final String MSG_CUSTOMER_CODE_NAME									= "IATOMS_MSG_BIM02010_I0003";
	/**
	 * 公司基本訊息維護 -- 匯出的報表的sheet名稱/報表名稱
	 */
	public static final String COMPANY_REPORT_SHEET_NAME								= "IATOMS_MSG_BIM02010_I0004";
	/**
	 * DTID起限輸入長度為8碼的數字，請重新輸入
	 */
	public static final String INPUT_DTID_START_ERROR									= "IATOMS_MSG_PVM04020_E0001";
	/**
	 * DTID訖限輸入長度為8碼的數字，請重新輸入
	 */
	public static final String INPUT_DTID_END_ERROR										= "IATOMS_MSG_PVM04020_E0002";
	
	/**
	 * 設備狀態報表匯出的報表名稱
	 */
	public static final String REPOSTATUS_REPORT_FILE_NAME_DETAIL						= "IATOMS_MSG_DMM03090_I0001";
	/**
	 * 設備狀態報表匯出的報表名稱集合
	 */
	public static final String REPOSTATUS_REPORT_FILE_NAME_LIST							= "IATOMS_MSG_DMM03090_I0002";
	/**
	 * 設備狀態報表 -- 匯出報表中的查詢月份
	 */
	public static final String REPOSTATUS_REPORT_QUERY_MOUNTH							= "IATOMS_MSG_DMM03090_I0003";
	/**
	 * 請至少設定一筆待求償資料
	 */
	public static final String NOT_PAYMENT_INFO											= "IATOMS_MSG_SRM05040_E0001";
	/**
	 * DTID:{0}，對應案件資料錯誤信息
	 */
	public static final String CASE_UPLOAD_ERROR_MSG									= "IATOMS_MSG_SRM05020_E0090";
	/**
	 * DTID:{0}，對應交易參數錯誤信息
	 */
	public static final String CASE_PARAMETER_UPLOAD_ERROR_MSG							= "IATOMS_MSG_SRM05020_E0091";
	/**
	 * 第{0}行查無資料
	 */
	public static final String CASE_UPLOAD_DATA_NOT_FOUND								= "IATOMS_MSG_SRM05020_E0092";
	/**
	 * 第{0}行不可異動該交易參數
	 */
	public static final String CASE_UPLOAD_NOT_UPDATE_TRANS_TYPE						= "IATOMS_MSG_SRM05020_E0093";
	/**
	 * 案件序號:{0}，對應案件資料錯誤信息
	 */
	public static final String CASE_UPLOAD_CASE_NO_ERROR_MSG							= "IATOMS_MSG_SRM05020_E0094";
	/**
	 * 案件序號:{0}，對應交易參數錯誤信息
	 */
	public static final String CASE_PARAMETER_UPLOAD_CASE_NO_ERROR_MSG					= "IATOMS_MSG_SRM05020_E0095";
	/**
	 * 第{0}行交易參數無對應案件資料
	 */
	public static final String CASE_PARAMETER_UPLOAD_NO_CASE_INFO						= "IATOMS_MSG_SRM05020_E0096";
	/**
	 * 當前登錄者角色為客戶，不可異動公司{0}
	 */
	public static final String CASE_UPLOAD_CUSTOMER_ROLE_ERROR							= "IATOMS_MSG_SRM05020_E0097";
	
	/**
	 * TMS，QA只能處理派工給TMS或QA的案件
	 */
	public static final String TMS_QA_MUCH_TMS_QA										= "IATOMS_MSG_SRM05020_E0035";

	/**
	 * 案件狀態不為待結案查、完修，不可進行退回
	 */
	public static final String CASE_STATUS_MUCH_WAIT_CLOSE_CAN_RETREAT					= "IATOMS_MSG_SRM05020_E0036";
	/**
	 * 同一案件下，一般VM、一般VMJ、一般VMJU有且只能有一個
	 */
	public static final String CASE_UPLOAD_COMMON_TRANS_ERROR							= "IATOMS_MSG_SRM05020_E0037";
	/**
	 * {0}限輸入是/否
	 */
	public static final String INPUT_YES_OR_NO											= "IATOMS_MSG_SRM05020_E0038";
	/**
	 * {0}限數字
	 */
	public static final String INPUT_NUMBER												= "IATOMS_MSG_SRM05020_E0039";
	/**
	 * 案件資料
	 */
	public static final String CASE_INFO_MESSAGE										= "IATOMS_MSG_BAT09020_I0001";
	/**
	 * 已有{0}筆實際驗收
	 */
	public static final String ACTUAL_ACCEPTANCE										= "IATOMS_MSG_DM03040_I0001";
	/**
	 * 已有{0}筆客戶實際驗收
	 */
	public static final String CUSTOMES_ACCEPTANCE										= "IATOMS_MSG_DM03040_I0002";
	/**
	 * 驗收已完成
	 */
	public static final String FINISH_ACCEPTANCE										= "IATOMS_MSG_DM03040_I0003";
	/**
	 * {0}舊資料
	 */
	public static final String OLD_DATATRANS											= "IATOMS_MSG_BAT09020_I0002";
	/**
	 * 周邊設備({0}) 通過設備管理作業解除綁定 原因:{1}
	 */
	public static final String ASSET_REMOVE_OR_LOSS										= "IATOMS-MSG-DMM03060-E0001";
	/**
	 * {0}月剩余台数{1}次
	 */
	public static final String EDC_FEE_REPORT_FOR_JDW_MESSAGE							= "IATOMS-MSG-AMM06040-I0001";
	/**
	 * {0}月費用統計表
	 */
	public static final String EDC_FEE_REPORT_FOR_JDW_TITLE								= "IATOMS-MSG-AMM06040-I0002";
	/**
	 * 全部设备于验收后三年开始收维护费
		以下维护费依据当时总数量计算
		维护费${0}/每台(维护目标总数{1}台(含)以内)
		维护费${2}/每台(维护目标总数{3}台以上)

	 */
	public static final String EDC_FEE_REPORT_FOR_JDW_REMARKS							= "IATOMS-MSG-AMM06040-I0003";
	/**
	 * 首裝TMS設定服務費免費：優惠合約下的設備，首次裝機不用TMS設定費，顯示台數
	 */
	public static final String EDC_FEE_REPORT_FOR_JDW_IS_FIRST_INSTALL					= "IATOMS-MSG-AMM06040-I0004";
	/**
	 * 維護費({0})
	 */
	public static final String ASSET_MAINTAIN_FEE										= "IATOMS-MSG-AMM06030-I0001";
	/**
	 * 備註
	 */
	public static final String FEE_TOTAL_REMARK											= "IATOMS-MSG-AMM06030-I0002";
	/**
	 * 使用中台數共｛0｝台+啟用中台數共｛1｝台
	 */
	public static final String IN_USE_AND_IN_ENABLE_NUMBER								= "IATOMS-MSG-AMM06030-I0003";
	
	/**
	 * 匯出錯誤消息
	 */
	public static final String EXPORT_ERROR_MESSAGE										= "IATOMS-MSG-COMMON-E0056";
	/**
	 * {0}限輸入09開頭且長度為10碼的數字
	 */
	public static final String TW_PHONE_INPUT_ERROR										= "IATOMS-MSG-COMMON-E0057";
	/**
	 * {0}件汇入{1}件成功
	 */
	public static final String CASE_UPLOAD_SUCCESS										= "IATOMS-MSG-SRM05020-I0032";
	/**
	 * {0}件汇入{1}件失败
	 */
	public static final String CASE_UPLOAD_ERROR										= "IATOMS-MSG-SRM05020-E0031";
	
	/**
	 * 延期日期:{0}
	 */
	public static final String PARAM_DEAL_DATE											= "IATOMS_MSG_SRM05020_E0040";
	/**
	 * 本月裝機共{0}台，內含{1}台非首裝設備
	 */
	public static final String EDC_FEE_REPORT_INSTALLED_COUNT							= "IATOMS-MSG-AMM06020-I0001";
	/**
	 * 本月拆機共{0}台，內含{1}台POS機為自拆寄回故無需收費
	 */
	public static final String EDC_FEE_REPORT_UNINSTALLED_COUNT							= "IATOMS-MSG-AMM06020-I0002";
	/**
	 * 啟用設備明細- {0} (計算方式)
	 */
	public static final String EDC_FEE_REPORT_ASSET_INFO								= "IATOMS-MSG-AMM06020-I0003";
	/**
	 * 實際啟用量+備品量(即”(訂單數量-報廢量)*5%”)超過訂單數量-報廢量，故月維護費計算方式以訂單數量-報廢量：({0}-{1})*{2}元={3}
	 */
	public static final String EDC_FEE_REPORT_MAINTENANCE_FEE_MSG1							= "IATOMS-MSG-AMM06020-I0004";
	/**
	 * 訂單數量-報廢量超過實際啟用量+備品量(即”(訂單數量-報廢量)*5%”)，故月維護費計算方式以實際啟用量+備品量：({0}+{1})*{2}元={3}
	 */
	public static final String EDC_FEE_REPORT_MAINTENANCE_FEE_MSG2							= "IATOMS-MSG-AMM06020-I0005";
	/**
	 * 捷達威維護成功
	 */
	public static final String PARAM_JDW_SUCCESS							="IATOMS-MSG-JDW_I0001";
	
	/**
	 * 捷達威維護失敗
	 */
	public static final String PARAM_JDW_FAILURE							="IATOMS-MSG-JDW_E0001";
	
	/**
	 * 該模板名稱已存在，請重新輸入
	 */
	public static final String PARAM_REPEAT_TEMPLATE						="IATOMS_MSG_SRM05020_E0041";
	
	/**
	 * 用戶模板已超過限制數量，不可新增
	 */
	public static final String PARAM_OVER_LIMIT								="IATOMS_MSG_SRM05020_E0042";
	
	/**
	 * {0}更新成功
	 */
	public static final String PARAM_UPDATE_SUCCESS							="IATOMS-MSG-SRM05020-I0035";
	
	/**
	 * {0}更新失敗
	 */
	public static final String PARAM_UPDATE_FAILURE							="IATOMS-MSG-SRM05020-I0036";
	/**
	 * 台新租認維護錯誤信息
	 */
	public static final String TAI_XIN_ERROR_MESSAGE						= "IATOMS-MSG-DMM03030-E0002";
	/**
	 * 捷達威維護錯誤信息
	 */
	public static final String JDW_ERROR_MESSAGE							= "IATOMS-MSG-DMM03030-E0003";
	/**
	 * {0}設備狀態不為領用中或使用中，不可進行台新租賃維護作業
	 */
	public static final String TAI_XIN_STATUS_ERROR							= "IATOMS-MSG-DMM03030-E0004";
	/**
	 * {0}設備使用人不為台新，不可進行台新租賃維護作業
	 */
	public static final String TAI_XIN_ASSET_USER_ERROR						= "IATOMS-MSG-DMM03030-E0005";
	/**
	 * {0}設備狀態不為領用中或使用中，不可進行捷達威維護作業
	 */
	public static final String JDW_STATUS_ERROR								= "IATOMS-MSG-DMM03030-E0006";
	/**
	 * {0}設備使用人不為捷達威，不可進行捷達威維護作業
	 */
	public static final String JDW_ASSET_USER_ERROR							= "IATOMS-MSG-DMM03030-E0007";
	/**
	 * 設備資料批次異動錯誤信息
	 */
	public static final String ASSET_UPDATE_ERROR_MESSAGE					= "IATOMS-MSG-DMM03030-E0008";
	/**
	 * 請輸入修改月份
	 */
	public static final String UPDATE_INPUT_MONTH_YEAR						= "IATOMS-MSG-DMM03030-E0009";
	/**
	 * 異動目的地限輸入最新設備檔或設備歷史月檔
	 */
	public static final String UPDATE_TABLE_INPUT_ERROR						= "IATOMS-MSG-DMM03030-E0010";
	/**
	 * 帳號不存在或密碼錯誤，請重新輸入
	 */
	public static final String LOGON_ID_NOT_FOUND_OR_WRONG_PWD				= "FWK-MSG-LOGIN-E0016";
	
	/**
	 * 域驗證配置有誤，請洽系統管理員
	 */
	public static final String LDAP_IS_ERROR								= "IATOMS-MSG-ADM01000-E0015";
	
	/**
	 * 退回至[客服]
	 */
	public static final String RETREAT_CASE_TO_CUSTOMER_SERVICE				= "IATOMS-MSG-SRM05020-I0037";
	
	/**
	 * 案件編號{0}，通知CMS案件作廢失敗，請洽系統管理人員
	 */
	public static final String VOID_CASE_CMS_API_ERROR						= "IATOMS_MSG_SRM05020_E0043";
	/**
	 * {0}
	 */
	public static final String PARAM_INPUT_MESSAGE							= "IATOMS-MSG-COMMON-E0058";
	/**
	 * 案件編號{0}，{1}
	 */
	public static final String PARAM_CMS_CASE_ERROR							= "IATOMS-MSG-COMMON-E0059";
	/**
	 * 案件編號{0}，未完成申請與繳費，請聯絡微型商戶至CMS完成相關作業
	 */
	public static final String PARAM_CMS_APIOP001_MSG						= "IATOMS-MSG-APIOP001-E0001";
	/**
	 * 求償編號{0}，{1}
	 */
	public static final String PARAM_CMS_PAYMENT_ERROR						= "IATOMS-MSG-APIOP005-E0001";
	/**
	 * 授權成功
	 */
	public static final String PARAM_CMS_CONFIRM_AUTHORIZES_SUCCESS			= "IATOMS-MSG-APIOP003-E0001";
	/**
	 * 授權成功
	 */
	public static final String PARAM_CMS_CANCEL_CONFIRM_AUTHORIZES_SUCCESS	= "IATOMS-MSG-APIOP003-E0002";
	/**
	 * 簽收設備序號(主機)與預載時輸入不相同，請再確認
	 */
	public static final String PARAM_CMS_SERIAL_NUMBER_COMPARE_ERROR		= "IATOMS-MSG-APIOP002-E0001";
	/**
	 * 簽收設備序號(SIM卡)與預載時輸入不相同，請再確認
	 */
	public static final String PARAM_CMS_SIM_ERIAL_NUMBER_COMPARE_ERROR		= "IATOMS-MSG-APIOP002-E0002";
	/**
	 * 維護廠商錯誤，應為經貿聯網
	 */
	public static final String PARAM_CMS_COMPANY_ERROR						= "IATOMS-MSG-SRM05020-I0038";
	/**
	 * 維護廠商錯誤，應為經貿聯網下的部門
	 */
	public static final String PARAM_CMS_DEPARTMENT_ERROR					= "IATOMS-MSG-SRM05020-I0039";
	/**
	 * 第{0}行，郵遞區號(區域)錯誤，不屬於該縣市
	 */
	public static final String PARAM_POST_CODE_ERROR						= "IATOMS-MSG-BIM02080-E008";
	/**
	 * 第{0}行，若存在郵遞區號(區域)，則縣市不可為空
	 */
	public static final String PARAM_LOCATION_ERROR							= "IATOMS-MSG-BIM02080-E009";
	/**
	 * 陽信銀行、上海商銀、彰化銀行，「交易類別無CUP」或「交易類別有CUP但內建功能或週邊功能無Dongle」，銀聯閃付需為"否"
	 */
	public static final String PARAM_NO_CUP_AND_DONGLE						= "IATOMS_MSG_SRM05020_E0044";
	/**
	 * 陽信銀行、彰化銀行，交易有CUP，是否開啟加密需為"是"
	 */
	public static final String PARAM_CUP_AND_OPEN_ENCRYPT					= "IATOMS_MSG_SRM05020_E0045";
	/**
	 * 陽信銀行、彰化銀行，交易有Smartpay退貨，是否開啟加密需為"是"
	 */
	public static final String PARAM_SMARTPAY_AND_OPEN_ENCRYPT				= "IATOMS_MSG_SRM05020_E0046";
	/**
	 * 陽信銀行、彰化銀行，無CUP與無Smartpay退貨，是否開啟加密需為"否"
	 */
	public static final String PARAM_SMARTPAY_AND_CUP_OPEN_ENCRYPT			= "IATOMS_MSG_SRM05020_E0047";
	/**
	 * 陽信銀行、上海商銀，選取一般交易類別，JCB$700項目不可以勾選
	 */
	public static final String PARAM_COMMON_AND_JCB							= "IATOMS_MSG_SRM05020_E0048";
	/**
	 * 陽信銀行選取Smartpay交易類別，週邊需有TSAM卡
	 */
	public static final String PARAM_PERIPHERALS_AND_T_SAM					= "IATOMS_MSG_SRM05020_E0049";
	/**
	 * {0}格式限YYYY/MM/DD HH:mm
	 */
	public static final String INPUT_NOT_TIME_FORMAT_YYYY_MM_DD_HH_MM         = "IATOMS-MSG-COMMON-E0060";
	/**
	 * 協調完成汇入成功
	 */
	public static final String CASE_COMPLETION_UPLOAD_SUCCESS				= "IATOMS-MSG-SRM05020-I0040";
	/**
	 * CyberEDC案件不可進行協調完成匯入操作
	 */
	public static final String CYBER_EDC_CAN_NOT_IMPORT						= "IATOMS-MSG-COMMON-E0061";
	/**
	 * 此案件已經過簽收或線上排除，不可進行協調完成
	 */
	public static final String CASE_RETREAT_CAN_NOT_IMPORT						= "IATOMS-MSG-COMMON-E0062";
	/**
	 * DTID之裝機案件不存在，請派工客服作廢
	 */
	public static final String CASE_NOT_EXIST								= "IATOMS-MSG-COMMON-E0063";
	/**
	 * 案件設備有異動，不可進行協調完成
	 */
	public static final String CASE_ASSET_HAS_CHANGE								= "IATOMS-MSG-COMMON-E0064";
	/**
	 * 請客服先將{0}結案
	 */
	public static final String END_THE_CASE_BEFORE								= "IATOMS-MSG-COMMON-E0065";
	/**
	 * 本案件dtid之EDC設備已拆除，請退回客服作廢
	 */
	public static final String ASSET_IS_REMOVED									= "IATOMS-MSG-COMMON-E0066";
	/**
	 * {0}於{1}已更新案件最新設備連接資料，請至處理頁面點✔重新帶入最新資料
	 */
	public static final String CASE_NEW_IS_CHANGE								= "IATOMS-MSG-COMMON-E0067";
	/**
	 * {0}於{1}異動設備與此案件不符，請聯繫Cyber客服確認
	 */
	public static final String CASE_IS_CHANGE_CALL_CYBER						= "IATOMS-MSG-COMMON-E0068";
	/**
	 * {0}於{1}異動設備與此案件不符，請聯繫客服確認
	 */
	public static final String CASE_IS_CHANGE_CALL_SERVICE						= "IATOMS-MSG-COMMON-E0069";
	
	/**
	 * 環匯刷卡機型S80 Ethernet，TID需為5080開頭
	 */
	public static final String GP_S80_ETHERNET_ERROR_DTID						= "IATOMS_MSG_SRM05020_E0050";
	/**
	 * 環匯刷卡機型S80 Ethernet，連接方式不可是G
	 */
	public static final String GP_S80_ETHERNET_ERROR_CONNECTION_TYPE			= "IATOMS_MSG_SRM05020_E0051";
	/**
	 * 環匯刷卡機型S80 Ethernet，內建功能不可有Dongle
	 */
	public static final String GP_S80_ETHERNET_ERROR_DONGLE						= "IATOMS_MSG_SRM05020_E0052";
	/**
	 * 環匯刷卡機型S80 Ethernet，週邊S200或S300Q功能需有Dongle
	 */
	public static final String GP_S80_ETHERNET_ERROR_S200_DONGLE				= "IATOMS_MSG_SRM05020_E0053";
	/**
	 * 環匯刷卡機型S80 Ethernet，交易類別有CUP或Smartpay，週邊S200或S300Q功能需有Pinpad
	 */
	public static final String GP_S80_ETHERNET_ERROR_S200_PINPAD				= "IATOMS_MSG_SRM05020_E0054";
	/**
	 * 環匯刷卡機型S80 Ethernet，交易類別有CUP且AO人員為I5，則需有週邊SP20且功能有Pinpad
	 */
	public static final String GP_S80_ETHERNET_ERROR_NO_S200_PINPAD				= "IATOMS_MSG_SRM05020_E0055";
	/**
	 * 環匯刷卡機型S80 Ethernet，交易類別有CUP或Smartpay且AO人員不為I5，內建功能要有Pinpad
	 */
	public static final String GP_S80_ETHERNET_ERROR_NO_I5_PINPAD				= "IATOMS_MSG_SRM05020_E0056";
	
	/**
	 * 環匯刷卡機型S80 RF，TID需為5085開頭
	 */
	public static final String GP_S80_RF_ERROR_DTID								= "IATOMS_MSG_SRM05020_E0057";
	/**
	 * 環匯刷卡機型S80 RF，連接方式不可是G
	 */
	public static final String GP_S80_RF_ERROR_CONNECTION_TYPE					= "IATOMS_MSG_SRM05020_E0058";
	/**
	 * 環匯刷卡機型S80 RF，內建功能要有Dongle
	 */
	public static final String GP_S80_RF_ERROR_DONGLE							= "IATOMS_MSG_SRM05020_E0059";
	/**
	 * 環匯刷卡機型S80 RF，交易類別有CUP且AO人員為I5，則需有週邊SP20且功能有PINPAD
	 */
	public static final String GP_S80_RF_ERROR_S200_PINPAD						= "IATOMS_MSG_SRM05020_E0060";
	/**
	 * 環匯刷卡機型S80 RF，交易類別有CUP或Smartpay且AO人員不為I5，內建功能要有Pinpad
	 */
	public static final String GP_S80_RF_ERROR_I5_PINPAD						= "IATOMS_MSG_SRM05020_E0061";
	
	/**
	 * 環匯刷卡機型S90 3G，TID需為5020開頭
	 */
	public static final String GP_S80_3G_ERROR_DTID								= "IATOMS_MSG_SRM05020_E0062";
	/**
	 * 環匯刷卡機型S90 3G，連接方式要是G
	 */
	public static final String GP_S80_3G_ERROR_CONNECTION_TYPE					= "IATOMS_MSG_SRM05020_E0063";
	/**
	 * 環匯刷卡機型S90 3G，內建功能不能有Dongle
	 */
	public static final String GP_S80_3G_ERROR_DONGLE							= "IATOMS_MSG_SRM05020_E0064";
	/**
	 * 環匯刷卡機型S90 3G，不可有週邊R50、S200、SP20
	 */
	public static final String GP_S80_3G_ERROR_PERIPHERALS						= "IATOMS_MSG_SRM05020_E0065";
	
	/**
	 *環匯刷卡機型S90 RF，TID需為5025開頭
	 */
	public static final String GP_S90_RF_ERROR_DTID								= "IATOMS_MSG_SRM05020_E0066";
	/**
	 * 環匯刷卡機型S90 RF，連接方式要是G
	 */
	public static final String GP_S90_RF_ERROR_CONNECTION_TYPE					= "IATOMS_MSG_SRM05020_E0067";
	/**
	 * 環匯刷卡機型S90 RF，內建功能要有Dongle
	 */
	public static final String GP_S90_RF_ERROR_DONGLE							= "IATOMS_MSG_SRM05020_E0068";
	/**
	 * 環匯刷卡機型S90 RF，不可有週邊R50、S200、SP20
	 */
	public static final String GP_S90_RF_ERROR_PERIPHERALS						= "IATOMS_MSG_SRM05020_E0069";
	
	/**
	 * CUP有開預先授權功能→CUP手輸也要開啟
	 */
	public static final String GP_CUP_ERROR										= "IATOMS_MSG_SRM05020_E0070";
	
	/**
	 * 請輸入Receipt_type
	 */
	public static final String INPUT_RECEIPT_TYPE								= "IATOMS_MSG_SRM05020_E0071";
	/**
	 * 環匯刷卡機型S80 Ethernet，無S200、S300Q週邊功能不可有Dongle
	 */
	public static final String GP_S80_ETHERNET_NO_S200_DONGLE					= "IATOMS_MSG_SRM05020_E0072";
	/**
	 * 請輸入連接方式
	 */
	public static final String INPUT_CONNECTION_TYPE							= "IATOMS_MSG_SRM05020_E0073";
	/**
	 * 此DTID台新裝機件已存在
	 */
	public static final String TSB_INSTALL_CASE_IS_EXIST							= "IATOMS-MSG-COMMON-E0070";
	/**
	 * 此DTID台新裝機件尚未建案
	 */
	public static final String NO_TSB_INSTALL_CASE								= "IATOMS-MSG-COMMON-E0071";
	/**
	 * 當前登陸者角色不符，不可新增非本公司特店
	 */
	public static final String CAN_NOT_NEW_OTHER_MID							= "IATOMS-MSG-BIM02080-E010";
	/**
	 * 設備序號 {0}不存在
	 */
	public static final String SERIAL_NUMBER_NOT_EXIS							= "IATOMS-MSG-DMM03140-E0001";
	/**
	 * 設備序號 {0}狀態不爲{1}
	 */
	public static final String SERIAL_NUMBER_NOT_REPERTORY						= "IATOMS-MSG-DMM03140-E0002";
}
