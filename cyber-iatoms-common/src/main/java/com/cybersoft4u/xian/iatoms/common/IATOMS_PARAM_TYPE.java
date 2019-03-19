package com.cybersoft4u.xian.iatoms.common;


/**
 * Purpose: 基本參數代碼定義
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016/3/30
 * @MaintenancePersonnel allenchen
 */
public enum IATOMS_PARAM_TYPE{
	ACCOUNT_STATUS("ACCOUNT_STATUS"),//帐号状态
	ROLE_ATTRIBUTE("ROLE_ATTRIBUTE"),//角色屬性
	WORK_FLOW_ROLE("WORK_FLOW_ROLE"),//角色表單
	ACCESS_RIGHT("ACCESS_RIGHT"),//功能權限
	FUNCTION_TYPE("FUNCTION_TYPE"),//功能權限
	PASSWORD_LENGTH("PASSWORD_LENGTH"),//密碼長度	
	COMPANY_TYPE("COMPANY_TYPE"),//公司類型
	DTID_TYPE("DTID_TYPE"),//DTID方式
	AUTHENTICATION_TYPE("AUTHENTICATION_TYPE"),//登錄驗證方式
	CONTRACT_TYPE("CONTRACT_TYPE"),//合約類型
	CONTRACT_STATUS("CONTRACT_STATUS"),//合約狀態
	PAY_MODE("PAY_MODE"),//付款方式
	TICKET_TYPE("TICKET_TYPE"),//案件類別
	LOCATION("LOCATION"),//市縣區域
	AREA("AREA"),//地區
	/**
	 * 案件類型. 選項：一般、急件、特急件
	 */
	TICKET_MODE("TICKET_MODE"),
	MAIL_GROUP("MAIL_GROUP"),//郵件通知種類
	REPORT_CODE("REPORT_CODE"),//報表名稱
	REPORT_DETAIL("REPORT_DETAIL"),//報表明細
	COMM_MODE("COMM_MODE"),//通訊模式
	SUPPORTED_FUNCTION("SUPPORTED_FUNCTION"),//支援功能
	SYB_REMAIN_TIME("SYB_REMAIN_TIME"),//各設備剩餘台數
	ASSET_CATEGORY("ASSET_CATEGORY"),//設備類別
	SUPPLIES_NAME("SUPPLIES_NAME"),//耗材名稱
	SUPPLIES_TYPE("SUPPLIES_TYPE"),//耗材分類
	MA_TYPE("MA_TYPE"),//維護模式
	ASSET_STATUS("ASSET_STATUS"),//設備狀態
	RETIRE_REASON("RETIRE_REASON"),//報廢原因
	FAULT_COMPONENT("FAULT_COMPONENT"),//故障組件
	FAULT_DESCRIPTION("FAULT_DESCRIPTION"),//故障現象
	ASSET_NAME("ASSET_NAME"),//設備名稱
	NOTICE_TYPE("NOTICE_TYPE"),//通知類型
	/**
	 * 區域 。選項：外島地區、市區、其他鄉鎮地區、直轄市、非市區、縣轄市
	 */
	REGION("REGION"),
	/**
	 * 處理方式
	 */
	PROCESS_TYPE("PROCESS_TYPE"),
	/**
	 * 裝機類別
	 */
	INSTALL_TYPE("INSTALL_TYPE"),
	/**
	 * 查核結果
	 */
	CHECK_RESULTS("CHECK_RESULTS"),
	/**
	 * 刷卡機型
	 */
	EDC_TYPE("EDC_TYPE"),
	/**
	 * 軟體版本
	 */
	SOFT_VERSION("SOFT_VERSION"),
	/**
	 * 內建功能
	 */
	IN_FUNCTION("IN_FUNCTION"),
	/**
	 * 寬頻連接
	 */
	NET_VENDOR("NET_VENDOR"),
	/**
	 * 周邊設備功能
	 */
	ASSET_FUNCTION("ASSET_FUNCTION"),
	/**
	 * 是/否
	 */
	YES_OR_NO("YES_OR_NO"),
	/**
	 * 否/是
	 */
	NO_OR_YES("NO_OR_YES"),
	/**
	 * 執行作業
	 */
	ACTION("ACTION"),
	/**
	 * 交易類別
	 */
	TRANSACTION_CATEGORY("TRANSACTION_CATEGORY"),
	/**
	 * 設備開啟模式
	 */
	ASSET_OPEN_MODE("ASSET_OPEN_MODE"),
	/**
	 * 星期
	 */
	WEEK("WEEK"),
	/**
	 * 雙模組模式
	 */
	DOUBLE_MODULE("DOUBLE_MODULE"),
	/**
	 * 條件限制類型
	 */
	CONDITION_OPERATOR("CONDITION_OPERATOR"),
	/**
	 * ECR連線
	 */
	ECR_LINE("ECR_LINE"),
	/**
	 * 拆機類型
	 */
	UNINSTALL_TYPE("UNINSTALL_TYPE"),
	/**
	 * 資料狀態
	 */
	PAYMENT_STATUS("PAYMENT_STATUS"),
	/**
	 * 賠償方式
	 */
	PAYMENT_TYPE("PAYMENT_TYPE"),
	/**
	 * 求償資訊
	 */
	PAYMENT_REASON("PAYMENT_REASON"),
	/**
	 * 求償項目
	 */
	PAYMENT_ITEM("PAYMENT_ITEM"),
	/**
	 * 報修原因
	 */
	REPAIR_REASON("REPAIR_REASON"),
	/**
	 * 求償動作
	 */
	PAY_ACTION("PAY_ACTION"),
	/**
	 * 案件狀態
	 */
	CASE_STATUS("CASE_STATUS"),
	/**
	 * 案件動作
	 */
	CASE_ACTION("CASE_ACTION"),
	/**
	 * 處理方式類別
	 */
	SOLVE_TYPE("SOLVE_TYPE"),
	/**
	 * 責任歸屬
	 */
	RESPONSIBITY("RESPONSIBITY"),
	RESPON_ATTRIBUTION("RESPON_ATTRIBUTION"),
	/**
	 * 限制條件
	 */
	LIMIT_CONDITION("LIMIT_CONDITION"),
	/**
	 * 案件關聯設備（動作）
	 */
	ACTIONS("ACTIONS"),
	/**
	 * 報修問題原因
	 */
	PROBLEM_REASON("PROBLEM_REASON"),
	/**
	 * 報修問題原因分類
	 */
	PROBLEM_REASON_CATEGORY("PROBLEM_REASON_CATEGORY"),
	/**
	 * 報修解決方式
	 */
	PROBLEM_SOLUTION("PROBLEM_SOLUTION"),
	/**
	 * 報修解決方式分類
	 */
	PROBLEM_SOLUTION_CATEGORY("PROBLEM_SOLUTION_CATEGORY"),
	/**
	 * 設備解除綁定狀態
	 */
	ASSET_REMOVE_STATUS("ASSET_REMOVE_STATUS"),
	/**
	 * 案件群組
	 */
	CASE_GROUP("CASE_GROUP"),
	/**
	 * 系統備份或刪除期限
	 */
	SYSTEM_LIMIT("SYSTEM_LIMIT"),
	/**
	 * 範本類別
	 */
	CASE_TEMPLATE_CATEGORY("CASE_TEMPLATE_CATEGORY"),
	/**
	 * 案件logo
	 */
	CASE_LOGO("CASE_LOGO"),
	/**
	 * 倉管mail
	 */
	BORROW_ADVICE("BORROW_ADVICE"),
	/**
	 * 剩余免费次数
	 */
	FREE_REMAIN_TIME("FREE_REMAIN_TIME"),
	/**
	 * 剩余免费次数(舊報表)
	 */
	OLD_REMAIN_TIME("OLD_REMAIN_TIME"),
	/**
	 * 問題分類
	 */
	QUESTION_TYPE("QUESTION_TYPE"),
	/**
	 * 物流廠商
	 */
	LOGISTICS_VENDOR("LOGISTICS_VENDOR"),
	/**
	 * RECEIPT_TYPE
	 */
	RECEIPT_TYPE("RECEIPT_TYPE"),
	/**
	 * 借用設備類別
	 */
	BORROW_CASE_CATEGORY("BORROW_CASE_CATEGORY"),
	/**
	 * 借用設備狀態
	 */
	BORROW_CASE_STATUS("BORROW_CASE_STATUS");
	String code;
	IATOMS_PARAM_TYPE(String paramTypeName) {
		code = paramTypeName;
	}
	public String getCode() {
		return code;
	}
}
