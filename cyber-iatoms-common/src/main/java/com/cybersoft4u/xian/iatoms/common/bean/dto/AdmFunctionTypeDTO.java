package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 
 * @author HaimingWang
 * @since  JDK 1.6
 * @date   2016/7/7
 * @MaintenancePersonnel HaimingWang
 */
public class AdmFunctionTypeDTO extends DataTransferObject<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4338564345957372343L;

	public static enum ATTRIBUTE {
		FUNCTION_ID("functionId"),
		FUNCTION_NAME("functionName"),
		FUNCTION_CODE("functionCode"),
		FUNCTION_DESCRIPTION("functionDescription"),
		FUNCTION_URL("functionUrl"),
		STATUS("status"),
		PARENT_FUNCTION_ID("parentFunctionId"),
		FUNCTION_CATEGORY("functionCategory"),
		FUNCTION_ORDER("functionOrder"),
		ISCLOSE("isclose"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		PARENT_FUNCTION_NAME("parentFunctionName"),
		ACCESS_RIGHT("accessRight"),
		QUERY("query"),
		ADD("add"),
		EDIT("edit"),
		DELETE("delete"),
		EXPORT("export"),
		SAVE("save"),
		DETAIL("detail"),
		CARRY("carry"),
		BORROW("borrow"),
		CARRY_BACK("tooriginowner"),
		ASSET_IN("assetin"),
		SEND_REPAIR("sendrepair"),
		PENDING_DISABLED("pendingdisable"),
		DISABLED("disabled"),
		BACK("back"),
		DESTROY("destroy"),
		OTHER_EDIT("otheredit"),
		PRINT("print"),
		INSTALL("install"),
		MERGE("merge"),
		UPDATE("update"),
		UNINSTALL("uninstall"),
		CHECK("check"),
		PROJECT("project"),
		REPAIR("repair"),
		CASE_IMPORT("caseimport"),
		ADD_RECORD("addrecord"),
		DISPATCH("dispatch"),
		SHOW_DETAIL("showdetail"),
		RESPONSED("responsed"),
		COMPLETED("completed"),
		SIGN("sign"),
		ONLINE_EXCLUSION("onlineexclusion"),
		DELAY("delay"),
		RUSH_REPAIR("rushrepair"),
		CHANGE_CASE_TYPE("changecasetype"),
		CLOSED("closed"),
		VOID_CASE("voidcase"),
		IMMEDIATELY_CLOSING("immediatelyclosing"),
		ARRIVE("arrive"),
		SEND("send"),
		LOCK("lock"),
		COMPLETE("complete"),
		REPAY("repay"),
		AUTODISPATCH("autodispatch"),
		TAIXINRENT("taixinrent"),
		UNBOUND("unbound"),
		REPAIR2("repair2"),
		CONFIRMSEND("confirmsend"),
		JDWMAINTENANCE("jdwmaintenance"),
		OTHER("other"),
		CHANGECOMPLETEDATE("changecompletedate"),
		CHANGECREATEDATE("changecreatedate"),
		LEASEPRELOAD("leasepreload"),
		LEASESIGN("leasesign"),
		DISTRIBUTION("distribution"),
		PAYMENT("payment"),
		CONFIRMAUTHORIZES("confirmauthorizes"),
		CANCELCONFIRMAUTHORIZES("cancelconfirmauthorizes"),
		COORDINATEDIMPORT("coordinatedimport");
		

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * 功能編號
	 */
	private String functionId;
	/**
	 * 功能名稱
	 */
	private String functionName;
	
	/**
	 * 功能代號
	 */
	private String functionCode;
	
	/**
	 * 功能描述
	 */
	private String functionDescription;
	
	/**
	 * 功能路徑
	 */
	private String functionUrl;
	
	/**
	 * 狀態
	 */
	private String status;
	
	/**
	 * 父功能編號
	 */
	private String parentFunctionId;
	
	/**
	 * 功能類別
	 */
	private String functionCategory;
	
	/**
	 * 功能順序
	 */
	private Integer functionOrder;
	
	/**
	 * 是否關閉
	 */
	private String isclose;
	
	/**
	 * 建立人員編號
	 */
	private String createdById;
	
	/**
	 * 建立人員姓名
	 */
	private String createdByName;
	
	/**
	 * 建立日期
	 */
	private Date createdDate;
	
	/**
	 * 異動人員編號
	 */
	private String updatedById;
	
	/**
	 * 異動人員姓名
	 */
	private String updatedByName;
	
	/**
	 * 異動日期
	 */
	private Date updatedDate;

	/**
	 * 父功能名稱
	 */
	private String parentFunctionName;
	
	/**
	 * 權限
	 */
	private String accessRight;
	/**
	 * 查詢
	 */
	private String query;
	
	/**
	 * 新增
	 */
	private String add;

	/**
	 * 修改
	 */
	private String edit;
	
	/**
	 * 刪除
	 */
	private String delete;
	
	/**
	 * 匯出
	 */
	private String export;
	/**
	 * 儲存
	 */
	private String save;
	/**
	 * 明細
	 */
	private String detail;
	
	/**
	 * 領用
	 */
	private String carry;
	/**
	 * 借用
	 */
	private String borrow;
	/**
	 * 歸還
	 */
	private String tooriginowner;
	/**
	 * 入庫
	 */
	private String assetin;
	/**
	 * 送修
	 */
	private String sendrepair;
	/**
	 * 待報廢
	 */
	private String pendingdisable;
	/**
	 * 報廢
	 */
	private String disabled;
	/**
	 * 退回
	 */
	private String back;
	/**
	 * 銷毀
	 */
	private String destroy;
	/**
	 * 其他修改
	 */
	private String otheredit;
	
	/**
	 * 列印
	 */
	private String print;
	/**
	 * 裝機
	 */
	private String install;
	/**
	 * 倂機
	 */
	private String merge;
	/**
	 * 異動
	 */
	private String update;
	/**
	 * 拆機
	 */
	private String uninstall;
	/**
	 * 查核
	 */
	private String check;
	/**
	 * 專案
	 */
	private String project;
	/**
	 * 報修
	 */
	private String repair;
	
	/**
	 * 案件匯入
	 */
	private String caseimport;
	/**
	 * 新增記錄
	 */
	private String addrecord;
	/**
	 * 派工
	 */
	private String dispatch;
	/**
	 * 顯示記錄
	 */
	private String showdetail;
	
	
	/**
	 * 回應
	 */
	private String responsed;
	/**
	 * 完修
	 */
	private String completed;
	/**
	 * 簽收
	 */
	private String sign;
	/**
	 * 線上排除
	 */
	private String onlineexclusion;
	/**
	 * 延期
	 */
	private String delay;
	/**
	 * 催修
	 */
	private String rushrepair;
	/**
	 * 修改案件類型
	 */
	private String changecasetype;
	/**
	 * 結案審查
	 */
	private String closed;
	/**
	 * 作廢
	 */
	private String voidcase;
	/**
	 * 立即結案
	 */
	private String immediatelyclosing;
	/**
	 * 到場
	 */
	private String arrive;
	/**
	 * 送出
	 */
	private String send;
	/**
	 * 锁定
	 */
	private String lock;
	/**
	 * 完成
	 */
	private String complete;
	/**
	 * 還款
	 */
	private String repay;
	/**
	 * 維修
	 */
	private String repair2;
	/**
	 * 其他
	 */
	private String other;
	/**
	 * 解除綁定
	 */
	private String unbound;
	/**
	 * 台新租賃
	 */
	private String taixinrent;
	/**
	 * 自動派公
	 */
	private String autodispatch;
	/**
	 * 確認通知
	 */
	private String confirmsend;
	/**
	 * 捷達威維護
	 */
	private String jdwmaintenance;
	/**
	 * 修改實際完修日期
	 */
	private String changecompletedate;
	/**
	 * 修改進件日期
	 */
	private String changecreatedate;
	/**
	 * 求償
	 */
	private String payment;
	/**
	 * 租賃簽收
	 */
	private String leasesign;
	/**
	 * 配送中
	 */
	private String distribution;
	/**
	 * 租賃預載
	 */
	private String leasepreload;
	/**
	 * 授權確認
	 */
	private String confirmauthorizes;
	/**
	 * 租賃授權取消
	 */
	private String cancelconfirmauthorizes;
	/**
	 * 協調完成匯入
	 */
	private String coordinatedimport;
	
	/**
	 * Constructor:無參構造
	 */
	public AdmFunctionTypeDTO() {
	}

	/**
	 * Constructor:有參構造
	 */
	/**
	 * Constructor:
	 */
	public AdmFunctionTypeDTO(String functionId, String functionName,
			String functionCode, String functionDescription,
			String functionUrl, String status, String parentFunctionId,
			String functionCategory, Integer functionOrder, String isclose,
			String createdById, String createdByName, Date createdDate,
			String updatedById, String updatedByName, Date updatedDate,
			String parentFunctionName, String accessRight, String query,
			String add, String edit, String delete, String export, String save,
			String detail, String carry, String borrow, String carryBack,
			String assetIn, String sendRepair, String pendingDisabled,
			String disabled, String back, String destroy, String otherEdit,
			String print, String install, String merge, String update,
			String uninstall, String check, String project, String repair,
			String caseImport, String addRecord, String dispatch,
			String showdetail, String responsed, String completed, String sign,
			String onlineExclusion, String delay, String rushRepair,
			String changeCaseType, String closed, String voidCase,
			String immediatelyClosing, String arrive, String send, String lock,
			String complete) {
		super();
		this.functionId = functionId;
		this.functionName = functionName;
		this.functionCode = functionCode;
		this.functionDescription = functionDescription;
		this.functionUrl = functionUrl;
		this.status = status;
		this.parentFunctionId = parentFunctionId;
		this.functionCategory = functionCategory;
		this.functionOrder = functionOrder;
		this.isclose = isclose;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.parentFunctionName = parentFunctionName;
		this.accessRight = accessRight;
		this.query = query;
		this.add = add;
		this.edit = edit;
		this.delete = delete;
		this.export = export;
		this.save = save;
		this.detail = detail;
		this.carry = carry;
		this.borrow = borrow;
		this.tooriginowner = carryBack;
		this.assetin = assetIn;
		this.sendrepair = sendRepair;
		this.pendingdisable = pendingDisabled;
		this.disabled = disabled;
		this.back = back;
		this.destroy = destroy;
		this.otheredit = otherEdit;
		this.print = print;
		this.install = install;
		this.merge = merge;
		this.update = update;
		this.uninstall = uninstall;
		this.check = check;
		this.project = project;
		this.repair = repair;
		this.caseimport = caseImport;
		this.addrecord = addRecord;
		this.dispatch = dispatch;
		this.showdetail = showdetail;
		this.responsed = responsed;
		this.completed = completed;
		this.sign = sign;
		this.onlineexclusion = onlineExclusion;
		this.delay = delay;
		this.rushrepair = rushRepair;
		this.changecasetype = changeCaseType;
		this.closed = closed;
		this.voidcase = voidCase;
		this.immediatelyclosing = immediatelyClosing;
		this.arrive = arrive;
		this.send = send;
		this.lock = lock;
		this.complete = complete;
	}

	/**
	 * @return the functionId
	 */
	public String getFunctionId() {
		return functionId;
	}

	/**
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	/**
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	/**
	 * @return the functionCode
	 */
	public String getFunctionCode() {
		return functionCode;
	}

	/**
	 * @param functionCode the functionCode to set
	 */
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	/**
	 * @return the functionDescription
	 */
	public String getFunctionDescription() {
		return functionDescription;
	}

	/**
	 * @param functionDescription the functionDescription to set
	 */
	public void setFunctionDescription(String functionDescription) {
		this.functionDescription = functionDescription;
	}

	/**
	 * @return the functionUrl
	 */
	public String getFunctionUrl() {
		return functionUrl;
	}

	/**
	 * @param functionUrl the functionUrl to set
	 */
	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the parentFunctionId
	 */
	public String getParentFunctionId() {
		return parentFunctionId;
	}

	/**
	 * @param parentFunctionId the parentFunctionId to set
	 */
	public void setParentFunctionId(String parentFunctionId) {
		this.parentFunctionId = parentFunctionId;
	}

	/**
	 * @return the functionCategory
	 */
	public String getFunctionCategory() {
		return functionCategory;
	}

	/**
	 * @param functionCategory the functionCategory to set
	 */
	public void setFunctionCategory(String functionCategory) {
		this.functionCategory = functionCategory;
	}

	/**
	 * @return the functionOrder
	 */
	public Integer getFunctionOrder() {
		return functionOrder;
	}

	/**
	 * @param functionOrder the functionOrder to set
	 */
	public void setFunctionOrder(Integer functionOrder) {
		this.functionOrder = functionOrder;
	}

	/**
	 * @return the isclose
	 */
	public String getIsclose() {
		return isclose;
	}

	/**
	 * @param isclose the isclose to set
	 */
	public void setIsclose(String isclose) {
		this.isclose = isclose;
	}

	/**
	 * @return the createdById
	 */
	public String getCreatedById() {
		return createdById;
	}

	/**
	 * @param createdById the createdById to set
	 */
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	/**
	 * @return the createdByName
	 */
	public String getCreatedByName() {
		return createdByName;
	}

	/**
	 * @param createdByName the createdByName to set
	 */
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the updatedById
	 */
	public String getUpdatedById() {
		return updatedById;
	}

	/**
	 * @param updatedById the updatedById to set
	 */
	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	/**
	 * @return the updatedByName
	 */
	public String getUpdatedByName() {
		return updatedByName;
	}

	/**
	 * @param updatedByName the updatedByName to set
	 */
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the parentFunctionName
	 */
	public String getParentFunctionName() {
		return parentFunctionName;
	}

	/**
	 * @param parentFunctionName the parentFunctionName to set
	 */
	public void setParentFunctionName(String parentFunctionName) {
		this.parentFunctionName = parentFunctionName;
	}

	/**
	 * @return the accessRight
	 */
	public String getAccessRight() {
		return accessRight;
	}

	/**
	 * @param accessRight the accessRight to set
	 */
	public void setAccessRight(String accessRight) {
		this.accessRight = accessRight;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return the add
	 */
	public String getAdd() {
		return add;
	}

	/**
	 * @param add the add to set
	 */
	public void setAdd(String add) {
		this.add = add;
	}

	/**
	 * @return the edit
	 */
	public String getEdit() {
		return edit;
	}

	/**
	 * @param edit the edit to set
	 */
	public void setEdit(String edit) {
		this.edit = edit;
	}

	/**
	 * @return the delete
	 */
	public String getDelete() {
		return delete;
	}

	/**
	 * @param delete the delete to set
	 */
	public void setDelete(String delete) {
		this.delete = delete;
	}

	/**
	 * @return the export
	 */
	public String getExport() {
		return export;
	}

	/**
	 * @param export the export to set
	 */
	public void setExport(String export) {
		this.export = export;
	}

	/**
	 * @return the save
	 */
	public String getSave() {
		return save;
	}

	/**
	 * @param save the save to set
	 */
	public void setSave(String save) {
		this.save = save;
	}

	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * @return the carry
	 */
	public String getCarry() {
		return carry;
	}

	/**
	 * @param carry the carry to set
	 */
	public void setCarry(String carry) {
		this.carry = carry;
	}

	/**
	 * @return the borrow
	 */
	public String getBorrow() {
		return borrow;
	}

	/**
	 * @param borrow the borrow to set
	 */
	public void setBorrow(String borrow) {
		this.borrow = borrow;
	}

	

	/**
	 * @return the tooriginowner
	 */
	public String getTooriginowner() {
		return tooriginowner;
	}

	/**
	 * @param tooriginowner the tooriginowner to set
	 */
	public void setTooriginowner(String tooriginowner) {
		this.tooriginowner = tooriginowner;
	}

	/**
	 * @return the pendingdisable
	 */
	public String getPendingdisable() {
		return pendingdisable;
	}

	/**
	 * @param pendingdisable the pendingdisable to set
	 */
	public void setPendingdisable(String pendingdisable) {
		this.pendingdisable = pendingdisable;
	}

	/**
	 * @return the assetin
	 */
	public String getAssetin() {
		return assetin;
	}

	/**
	 * @param assetin the assetin to set
	 */
	public void setAssetin(String assetin) {
		this.assetin = assetin;
	}

	/**
	 * @return the sendrepair
	 */
	public String getSendrepair() {
		return sendrepair;
	}

	/**
	 * @param sendrepair the sendrepair to set
	 */
	public void setSendrepair(String sendrepair) {
		this.sendrepair = sendrepair;
	}

	/**
	 * @return the disabled
	 */
	public String getDisabled() {
		return disabled;
	}

	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	/**
	 * @return the back
	 */
	public String getBack() {
		return back;
	}

	/**
	 * @param back the back to set
	 */
	public void setBack(String back) {
		this.back = back;
	}

	/**
	 * @return the destroy
	 */
	public String getDestroy() {
		return destroy;
	}

	/**
	 * @param destroy the destroy to set
	 */
	public void setDestroy(String destroy) {
		this.destroy = destroy;
	}

	/**
	 * @return the otheredit
	 */
	public String getOtheredit() {
		return otheredit;
	}

	/**
	 * @param otheredit the otheredit to set
	 */
	public void setOtheredit(String otheredit) {
		this.otheredit = otheredit;
	}

	/**
	 * @return the print
	 */
	public String getPrint() {
		return print;
	}

	/**
	 * @param print the print to set
	 */
	public void setPrint(String print) {
		this.print = print;
	}

	/**
	 * @return the install
	 */
	public String getInstall() {
		return install;
	}

	/**
	 * @param install the install to set
	 */
	public void setInstall(String install) {
		this.install = install;
	}

	/**
	 * @return the merge
	 */
	public String getMerge() {
		return merge;
	}

	/**
	 * @param merge the merge to set
	 */
	public void setMerge(String merge) {
		this.merge = merge;
	}

	/**
	 * @return the update
	 */
	public String getUpdate() {
		return update;
	}

	/**
	 * @param update the update to set
	 */
	public void setUpdate(String update) {
		this.update = update;
	}

	/**
	 * @return the uninstall
	 */
	public String getUninstall() {
		return uninstall;
	}

	/**
	 * @param uninstall the uninstall to set
	 */
	public void setUninstall(String uninstall) {
		this.uninstall = uninstall;
	}

	/**
	 * @return the check
	 */
	public String getCheck() {
		return check;
	}

	/**
	 * @param check the check to set
	 */
	public void setCheck(String check) {
		this.check = check;
	}

	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}

	/**
	 * @return the repair
	 */
	public String getRepair() {
		return repair;
	}

	/**
	 * @param repair the repair to set
	 */
	public void setRepair(String repair) {
		this.repair = repair;
	}

	/**
	 * @return the caseimport
	 */
	public String getCaseimport() {
		return caseimport;
	}

	/**
	 * @param caseimport the caseimport to set
	 */
	public void setCaseimport(String caseimport) {
		this.caseimport = caseimport;
	}

	/**
	 * @return the addrecord
	 */
	public String getAddrecord() {
		return addrecord;
	}

	/**
	 * @param addrecord the addrecord to set
	 */
	public void setAddrecord(String addrecord) {
		this.addrecord = addrecord;
	}

	/**
	 * @return the dispatch
	 */
	public String getDispatch() {
		return dispatch;
	}

	/**
	 * @param dispatch the dispatch to set
	 */
	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	/**
	 * @return the showdetail
	 */
	public String getShowdetail() {
		return showdetail;
	}

	/**
	 * @param showdetail the showdetail to set
	 */
	public void setShowdetail(String showdetail) {
		this.showdetail = showdetail;
	}

	/**
	 * @return the responsed
	 */
	public String getResponsed() {
		return responsed;
	}

	/**
	 * @param responsed the responsed to set
	 */
	public void setResponsed(String responsed) {
		this.responsed = responsed;
	}

	/**
	 * @return the completed
	 */
	public String getCompleted() {
		return completed;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(String completed) {
		this.completed = completed;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * @return the onlineexclusion
	 */
	public String getOnlineexclusion() {
		return onlineexclusion;
	}

	/**
	 * @param onlineexclusion the onlineexclusion to set
	 */
	public void setOnlineexclusion(String onlineexclusion) {
		this.onlineexclusion = onlineexclusion;
	}

	/**
	 * @return the delay
	 */
	public String getDelay() {
		return delay;
	}

	/**
	 * @param delay the delay to set
	 */
	public void setDelay(String delay) {
		this.delay = delay;
	}

	/**
	 * @return the rushrepair
	 */
	public String getRushrepair() {
		return rushrepair;
	}

	/**
	 * @param rushrepair the rushrepair to set
	 */
	public void setRushrepair(String rushrepair) {
		this.rushrepair = rushrepair;
	}

	/**
	 * @return the changecasetype
	 */
	public String getChangecasetype() {
		return changecasetype;
	}

	/**
	 * @param changecasetype the changecasetype to set
	 */
	public void setChangecasetype(String changecasetype) {
		this.changecasetype = changecasetype;
	}

	/**
	 * @return the closed
	 */
	public String getClosed() {
		return closed;
	}

	/**
	 * @param closed the closed to set
	 */
	public void setClosed(String closed) {
		this.closed = closed;
	}

	/**
	 * @return the voidcase
	 */
	public String getVoidcase() {
		return voidcase;
	}

	/**
	 * @param voidcase the voidcase to set
	 */
	public void setVoidcase(String voidcase) {
		this.voidcase = voidcase;
	}

	/**
	 * @return the immediatelyclosing
	 */
	public String getImmediatelyclosing() {
		return immediatelyclosing;
	}

	/**
	 * @param immediatelyclosing the immediatelyclosing to set
	 */
	public void setImmediatelyclosing(String immediatelyclosing) {
		this.immediatelyclosing = immediatelyclosing;
	}

	/**
	 * @return the arrive
	 */
	public String getArrive() {
		return arrive;
	}

	/**
	 * @param arrive the arrive to set
	 */
	public void setArrive(String arrive) {
		this.arrive = arrive;
	}

	/**
	 * @return the send
	 */
	public String getSend() {
		return send;
	}

	/**
	 * @param send the send to set
	 */
	public void setSend(String send) {
		this.send = send;
	}

	/**
	 * @return the lock
	 */
	public String getLock() {
		return lock;
	}

	/**
	 * @param lock the lock to set
	 */
	public void setLock(String lock) {
		this.lock = lock;
	}

	/**
	 * @return the complete
	 */
	public String getComplete() {
		return complete;
	}

	/**
	 * @param complete the complete to set
	 */
	public void setComplete(String complete) {
		this.complete = complete;
	}

	/**
	 * @return the repay
	 */
	public String getRepay() {
		return repay;
	}

	/**
	 * @param repay the repay to set
	 */
	public void setRepay(String repay) {
		this.repay = repay;
	}

	/**
	 * @return the repair2
	 */
	public String getRepair2() {
		return repair2;
	}

	/**
	 * @param repair2 the repair2 to set
	 */
	public void setRepair2(String repair2) {
		this.repair2 = repair2;
	}

	/**
	 * @return the unbound
	 */
	public String getUnbound() {
		return unbound;
	}

	/**
	 * @param unbound the unbound to set
	 */
	public void setUnbound(String unbound) {
		this.unbound = unbound;
	}

	/**
	 * @return the taixinrent
	 */
	public String getTaixinrent() {
		return taixinrent;
	}

	/**
	 * @param taixinrent the taixinrent to set
	 */
	public void setTaixinrent(String taixinrent) {
		this.taixinrent = taixinrent;
	}

	/**
	 * @return the autodispatch
	 */
	public String getAutodispatch() {
		return autodispatch;
	}

	/**
	 * @param autodispatch the autodispatch to set
	 */
	public void setAutodispatch(String autodispatch) {
		this.autodispatch = autodispatch;
	}

	/**
	 * @return the confirmsend
	 */
	public String getConfirmsend() {
		return confirmsend;
	}

	/**
	 * @param confirmsend the confirmsend to set
	 */
	public void setConfirmsend(String confirmsend) {
		this.confirmsend = confirmsend;
	}

	/**
	 * @return the jdwmaintenance
	 */
	public String getJdwmaintenance() {
		return jdwmaintenance;
	}

	/**
	 * @param jdwmaintenance the jdwmaintenance to set
	 */
	public void setJdwmaintenance(String jdwmaintenance) {
		this.jdwmaintenance = jdwmaintenance;
	}

	/**
	 * @return the other
	 */
	public String getOther() {
		return other;
	}

	/**
	 * @param other the other to set
	 */
	public void setOther(String other) {
		this.other = other;
	}

	/**
	 * @return the changecompletedate
	 */
	public String getChangecompletedate() {
		return changecompletedate;
	}

	/**
	 * @param changecompletedate the changecompletedate to set
	 */
	public void setChangecompletedate(String changecompletedate) {
		this.changecompletedate = changecompletedate;
	}

	/**
	 * @return the changecreatedate
	 */
	public String getChangecreatedate() {
		return changecreatedate;
	}

	/**
	 * @param changecreatedate the changecreatedate to set
	 */
	public void setChangecreatedate(String changecreatedate) {
		this.changecreatedate = changecreatedate;
	}

	/**
	 * @return the payment
	 */
	public String getPayment() {
		return payment;
	}

	/**
	 * @param payment the payment to set
	 */
	public void setPayment(String payment) {
		this.payment = payment;
	}

	/**
	 * @return the distribution
	 */
	public String getDistribution() {
		return distribution;
	}

	/**
	 * @param distribution the distribution to set
	 */
	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}

	/**
	 * @return the leasesign
	 */
	public String getLeasesign() {
		return leasesign;
	}

	/**
	 * @param leasesign the leasesign to set
	 */
	public void setLeasesign(String leasesign) {
		this.leasesign = leasesign;
	}

	/**
	 * @return the leasepreload
	 */
	public String getLeasepreload() {
		return leasepreload;
	}

	/**
	 * @param leasepreload the leasepreload to set
	 */
	public void setLeasepreload(String leasepreload) {
		this.leasepreload = leasepreload;
	}

	/**
	 * @return the confirmauthorizes
	 */
	public String getConfirmauthorizes() {
		return confirmauthorizes;
	}

	/**
	 * @param confirmauthorizes the confirmauthorizes to set
	 */
	public void setConfirmauthorizes(String confirmauthorizes) {
		this.confirmauthorizes = confirmauthorizes;
	}

	/**
	 * @return the cancelconfirmauthorizes
	 */
	public String getCancelconfirmauthorizes() {
		return cancelconfirmauthorizes;
	}

	/**
	 * @param cancelconfirmauthorizes the cancelconfirmauthorizes to set
	 */
	public void setCancelconfirmauthorizes(String cancelconfirmauthorizes) {
		this.cancelconfirmauthorizes = cancelconfirmauthorizes;
	}

	/**
	 * @return the coordinatedimport
	 */
	public String getCoordinatedimport() {
		return coordinatedimport;
	}

	/**
	 * @param coordinatedimport the coordinatedimport to set
	 */
	public void setCoordinatedimport(String coordinatedimport) {
		this.coordinatedimport = coordinatedimport;
	}
	
}
