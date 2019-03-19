package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.List;

/**
 * Purpose: 接收作業通知DTO
 * @author neiljing
 * @since  JDK 1.7
 * @date   2018/4/10
 * @MaintenancePersonnel neiljing
 */
public class ReceiveJobNoticeDTO{

	/**
	 * 作業狀態（必填）
	 * 01：受理中(派工)
	 * 02：配送中(配送中)
	 * 03：已配達(簽收)
	 * 04：已退回(作廢)
	 * 05：協調完成&線上排除
	 * 06：檢測中(求償-建案)
	 * 07：待同意求償(求償-鎖定)
	 * 10：預載
	 * 13：退費-第二台保證金(求償-鎖定)
	 */
	private String JobStatus;
	/**
	 * 特店代號（必填）
	 */
	private String MID;
	/**
	 * 案件編號（必填）
	 */
	private String CaseId;
	/**
	 * DTID（必填）
	 */
	private String DTID;
	/**
	 * 連絡人
	 */
	private String Contact;
	/**
	 * 連絡電話
	 */
	private String ContactTel;
	/**
	 * 連絡手機
	 */
	private String ContactPhone;
	/**
	 * 聯絡EMAIL
	 */
	private String ContactEmail;
	/**
	 * 聯絡地址-縣市
	 */
	private String ContactAddressLocation;
	/**
	 * 聯絡地址
	 */
	private String ContactAddress;
	/**
	 * 報修原因
	 */
	private String Memo;
	/**
	 * 求償編號
	 */
	private String PCaseId;
	/**
	 * 物流編號
	 * 作業類型=配送中，則為必填
	 */
	private String TrackNo;
	/**
	 * 物流時間YYYY/MM/DD HH:MM:SS
	 * 作業類型=配送中，則為必填
	 */
	private String TrackDate;
	/**
	 * 設備序號
	 */
	private String DeviceSn;
	/**
	 * 設備序號-舊
	 */
	private String DeviceSnOld;
	/**
	 * 求償項目集合
	 */
	private List<PayMentCMSDTO> RepayItem;
	
	/**
	 * 作業類型
	 * 01：申請
	 * 02：作廢
	 */
	private String JobType;
	/**
	 * 客戶(特店銀行)
	 */
	private String CustomerName;
	/**
	 * 特店名稱
	 */
	private String MIDName;
	/**
	 * TID
	 */
	private String TID;
	/**
	 * 統一編號
	 */
	private String ImportUnityNumber;
	/**
	 * 特店聯絡人
	 */
	private String ImportContact;
	/**
	 * 特店聯絡電話
	 */
	private String ImportContactTel;
	/**
	 * 特店聯絡EMAIL
	 */
	private String ImportContactEmail;
	/**
	 * 聯絡地址-縣市
	 */
	private String ImportContactAddressLocation;
	/**
	 * 聯絡地址-地址
	 */
	private String ImportContactAddress;
	/**
	 * 行動電話
	 */
	private String ImportContactPhone;
	/**
	 * AO人員
	 */
	private String ImportAoName;
	/**
	 * AO Email
	 */
	private String ImportAoEmail;
	/**
	 * 返回結果
	 */
	private String RESULT;
	/**
	 * 返回信息
	 */
	private String RESULT_MSG;
	/**
	 * 設備類型
	 */
	private String DeviceType;
	/**
	 * 特店 聯絡地址-郵遞區號
	 */
	private String ImportContactAreaCode;
	/**
	 * 特店 聯絡地址-郵遞區域
	 */
	private String ImportContactAreaName;
	/**
	 * 郵遞區號
	 */
	private String ContactAreaCode;
	/**
	 * 郵遞區域
	 */
	private String ContactAreaName;
	/**
	 * 案件類型
	 */
	private String CaseType;
	/**
	 * 專案註記
	 */
	private String ProjectFlag;
	/**
	 * Ecr註記
	 */
	private String EcrFlag;
	/**
	 * 裝機案件-縣市
	 */
	private String InstallContactAddressLocation;
	/**
	 * 裝機案件-郵遞區號
	 */
	private String InstallContactAreaCode;
	/**
	 * 裝機案件-郵遞區域
	 */
	private String InstallContactAreaName;
	/**
	 * 裝機案件-地址
	 */
	private String InstallContactAddress;
	/**
	 * 裝機案件-聯絡人
	 */
	private String InstallContact;
	/**
	 * 裝機案件-聯絡電話
	 */
	private String InstallContactTel;
	/**
	 * 裝機案件-聯絡行動電話
	 */
	private String InstallContactPhone;
	/**
	 * 裝機案件-聯絡EMAIL
	 */
	private String InstallContactEmail;
	/**
	 * 物流廠商
	 */
	private String TrackVendor;
	/**
	 * 無參構造方法
	 */
	public ReceiveJobNoticeDTO() {
		super();
	}
	
	/**
	 * APIOP004,APIOP002
	 */
	public ReceiveJobNoticeDTO(String JobStatus, String CaseId, String MID, String DTID) {
		super();
		this.JobStatus = JobStatus;
		this.CaseId = CaseId;
		this.MID = MID;
		this.DTID = DTID;
	}
	
	
	/**
	 * APIOP004(檢測中(求償-建案))
	 */
	public ReceiveJobNoticeDTO(String jobStatus, String mID, String caseId, String dTID, String pCaseId) {
		super();
		this.JobStatus = jobStatus;
		this.MID = mID;
		this.CaseId = caseId;
		this.DTID = dTID;
		this.PCaseId = pCaseId;
	}

	/**
	 * APIOP003(01：申請)
	 */
	public ReceiveJobNoticeDTO(String mID, String caseId, String dTID,
			String jobType, String customerName, String mIDName, String TID,
			String importUnityNumber, String importContact,
			String importContactTel, String importContactEmail,
			String importContactAddressLocation, String importContactAddress,
			String importContactPhone, String importAoName, String importAoEmail, String DeviceType,
			String ImportContactAreaCode, String ImportContactAreaName, String CaseType, String ProjectFlag, String EcrFlag, String InstallContactAddressLocation, 
			String InstallContactAreaCode, String InstallContactAreaName, String InstallContactAddress, String InstallContact, 
			String InstallContactTel, String InstallContactPhone, String InstallContactEmail) {
		super();
		this.MID = mID;
		this.CaseId = caseId;
		this.DTID = dTID;
		this.JobType = jobType;
		this.CustomerName = customerName;
		this.MIDName = mIDName;
		this.TID = TID;
		this.ImportUnityNumber = importUnityNumber;
		this.ImportContact = importContact;
		this.ImportContactTel = importContactTel;
		this.ImportContactEmail = importContactEmail;
		this.ImportContactAddressLocation = importContactAddressLocation;
		this.ImportContactAddress = importContactAddress;
		this.ImportContactPhone = importContactPhone;
		this.ImportAoName = importAoName;
		this.ImportAoEmail = importAoEmail;
		this.DeviceType = DeviceType;
		this.ImportContactAreaCode = ImportContactAreaCode;
		this.ImportContactAreaName = ImportContactAreaName;
		this.CaseType = CaseType;
		this.ProjectFlag = ProjectFlag;
		this.EcrFlag = EcrFlag;
		this.InstallContactAddressLocation = InstallContactAddressLocation;
		this.InstallContactAreaCode = InstallContactAreaCode;
		this.InstallContactAreaName = InstallContactAreaName;
		this.InstallContactAddress = InstallContactAddress;
		this.InstallContact = InstallContact;
		this.InstallContactTel = InstallContactTel;
		this.InstallContactPhone = InstallContactPhone;
		this.InstallContactEmail = InstallContactEmail;
	}
	
	/**
	 * Task #3336：APIOP004(報修)，APIOP002(裝機)
	 */
	public ReceiveJobNoticeDTO(String jobStatus, String mID, String caseId,
			String dTID, String trackNo, String trackDate, String deviceSn, String trackVendor) {
		super();
		JobStatus = jobStatus;
		MID = mID;
		CaseId = caseId;
		DTID = dTID;
		TrackNo = trackNo;
		TrackDate = trackDate;
		DeviceSn = deviceSn;
		TrackVendor = trackVendor;
	}
	
	
	/**
	 * APIOP004
	 */
	public ReceiveJobNoticeDTO(String jobStatus, String mID, String caseId,
			String dTID, String pCaseId, List<PayMentCMSDTO> repayItem) {
		this.JobStatus = jobStatus;
		this.MID = mID;
		this.CaseId = caseId;
		this.DTID = dTID;
		this.PCaseId = pCaseId;
		this.RepayItem = repayItem;
	}
	
	/**
	 * APIOP005
	 */
	public ReceiveJobNoticeDTO(String jobStatus, String caseId, String dTID,
			String pCaseId, List<PayMentCMSDTO> repayItem) {
		this.JobStatus = jobStatus;
		this.CaseId = caseId;
		this.DTID = dTID;
		this.PCaseId = pCaseId;
		this.RepayItem = repayItem;
	}
	
	/**
	 * APIOP004 Task #3404
	 */
	public ReceiveJobNoticeDTO(String jobStatus, String mID, String caseId,
			String dTID, String contact, String contactTel,
			String contactPhone, String contactEmail,
			String contactAddressLocation, String contactAddress, String Memo,
			String ContactAreaCode, String ContactAreaName) {
		this.JobStatus = jobStatus;
		this.MID = mID;
		this.CaseId = caseId;
		this.DTID = dTID;
		this.Contact = contact;
		this.ContactTel = contactTel;
		this.ContactPhone = contactPhone;
		this.ContactEmail = contactEmail;
		this.ContactAddressLocation = contactAddressLocation;
		this.ContactAddress = contactAddress;
		this.Memo = Memo;
		this.ContactAreaCode = ContactAreaCode;
		this.ContactAreaName = ContactAreaName;
	}
	
	 /**
	  * Task #3358：APIOP004(報修)
	  */
	 public ReceiveJobNoticeDTO(String jobStatus, String mID, String caseId,
	   String dTID, String deviceSn, String deviceSnOld) {
	  super();
	  this.JobStatus = jobStatus;
	  this.MID = mID;
	  this.CaseId = caseId;
	  this.DTID = dTID;
	  this.DeviceSn = deviceSn;
	  this.DeviceSnOld = deviceSnOld;
	 }
	
	/**
	 * APIOP001
	 */
	public ReceiveJobNoticeDTO(String caseId, String dTID) {
		super();
		this.CaseId = caseId;
		this.DTID = dTID;
	}

	
	
	/**
	 * APIOP003(02：作廢)
	 */
	public ReceiveJobNoticeDTO(String jobType, String mID, String caseId,
			String dTID, String deviceType, String customerName, String mIDName,
			String tID, String caseType, String ProjectFlag, String EcrFlag) {
		this.JobType = jobType;
		this.MID = mID;
		this.CaseId = caseId;
		this.DTID = dTID;
		this.DeviceType = deviceType;
		this.CustomerName = customerName;
		this.MIDName = mIDName;
		this.TID = tID;
		this.CaseType = caseType;
		this.ProjectFlag = ProjectFlag;
		this.EcrFlag = EcrFlag;
	}

	public String getJobStatus() {
		return JobStatus;
	}
	public void setJobStatus(String jobStatus) {
		JobStatus = jobStatus;
	}
	public String getCaseId() {
		return CaseId;
	}
	public void setCaseId(String caseId) {
		CaseId = caseId;
	}
	public String getDTID() {
		return DTID;
	}
	public void setDTID(String dTID) {
		DTID = dTID;
	}
	public String getContact() {
		return Contact;
	}
	public void setContact(String contact) {
		Contact = contact;
	}
	public String getContactTel() {
		return ContactTel;
	}
	public void setContactTel(String contactTel) {
		ContactTel = contactTel;
	}
	public String getContactPhone() {
		return ContactPhone;
	}
	public void setContactPhone(String contactPhone) {
		ContactPhone = contactPhone;
	}
	public String getContactEmail() {
		return ContactEmail;
	}
	public void setContactEmail(String contactEmail) {
		ContactEmail = contactEmail;
	}
	public String getContactAddressLocation() {
		return ContactAddressLocation;
	}
	public void setContactAddressLocation(String contactAddressLocation) {
		ContactAddressLocation = contactAddressLocation;
	}
	public String getContactAddress() {
		return ContactAddress;
	}
	public void setContactAddress(String contactAddress) {
		ContactAddress = contactAddress;
	}
	public String getMemo() {
		return Memo;
	}
	public void setMemo(String memo) {
		Memo = memo;
	}
	public String getPCaseId() {
		return PCaseId;
	}
	public void setPCaseId(String pCaseId) {
		PCaseId = pCaseId;
	}
	public String getTrackNo() {
		return TrackNo;
	}
	public void setTrackNo(String trackNo) {
		TrackNo = trackNo;
	}
	public String getTrackDate() {
		return TrackDate;
	}
	public void setTrackDate(String trackDate) {
		TrackDate = trackDate;
	}
	public String getDeviceSn() {
		return DeviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		DeviceSn = deviceSn;
	}
	public String getDeviceSnOld() {
		return DeviceSnOld;
	}
	public void setDeviceSnOld(String deviceSnOld) {
		DeviceSnOld = deviceSnOld;
	}
	public List<PayMentCMSDTO> getRepayItem() {
		return RepayItem;
	}
	public void setRepayItem(List<PayMentCMSDTO> repayItem) {
		RepayItem = repayItem;
	}

	/**
	 * @return the mID
	 */
	public String getMID() {
		return MID;
	}

	/**
	 * @param mID the mID to set
	 */
	public void setMID(String mID) {
		MID = mID;
	}

	/**
	 * @return the jobType
	 */
	public String getJobType() {
		return JobType;
	}

	/**
	 * @param jobType the jobType to set
	 */
	public void setJobType(String jobType) {
		JobType = jobType;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return CustomerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	/**
	 * @return the mIDName
	 */
	public String getMIDName() {
		return MIDName;
	}

	/**
	 * @param mIDName the mIDName to set
	 */
	public void setMIDName(String mIDName) {
		MIDName = mIDName;
	}

	/**
	 * @return the tID
	 */
	public String getTID() {
		return TID;
	}

	/**
	 * @param tID the tID to set
	 */
	public void setTID(String tID) {
		TID = tID;
	}

	/**
	 * @return the importUnityNumber
	 */
	public String getImportUnityNumber() {
		return ImportUnityNumber;
	}

	/**
	 * @param importUnityNumber the importUnityNumber to set
	 */
	public void setImportUnityNumber(String importUnityNumber) {
		ImportUnityNumber = importUnityNumber;
	}

	/**
	 * @return the importContact
	 */
	public String getImportContact() {
		return ImportContact;
	}

	/**
	 * @param importContact the importContact to set
	 */
	public void setImportContact(String importContact) {
		ImportContact = importContact;
	}

	/**
	 * @return the importContactTel
	 */
	public String getImportContactTel() {
		return ImportContactTel;
	}

	/**
	 * @param importContactTel the importContactTel to set
	 */
	public void setImportContactTel(String importContactTel) {
		ImportContactTel = importContactTel;
	}

	/**
	 * @return the importContactEmail
	 */
	public String getImportContactEmail() {
		return ImportContactEmail;
	}

	/**
	 * @param importContactEmail the importContactEmail to set
	 */
	public void setImportContactEmail(String importContactEmail) {
		ImportContactEmail = importContactEmail;
	}

	/**
	 * @return the importContactAddressLocation
	 */
	public String getImportContactAddressLocation() {
		return ImportContactAddressLocation;
	}

	/**
	 * @param importContactAddressLocation the importContactAddressLocation to set
	 */
	public void setImportContactAddressLocation(String importContactAddressLocation) {
		ImportContactAddressLocation = importContactAddressLocation;
	}

	/**
	 * @return the importContactAddress
	 */
	public String getImportContactAddress() {
		return ImportContactAddress;
	}

	/**
	 * @param importContactAddress the importContactAddress to set
	 */
	public void setImportContactAddress(String importContactAddress) {
		ImportContactAddress = importContactAddress;
	}

	/**
	 * @return the importContactPhone
	 */
	public String getImportContactPhone() {
		return ImportContactPhone;
	}

	/**
	 * @param importContactPhone the importContactPhone to set
	 */
	public void setImportContactPhone(String importContactPhone) {
		ImportContactPhone = importContactPhone;
	}

	/**
	 * @return the importAoName
	 */
	public String getImportAoName() {
		return ImportAoName;
	}

	/**
	 * @param importAoName the importAoName to set
	 */
	public void setImportAoName(String importAoName) {
		ImportAoName = importAoName;
	}

	/**
	 * @return the importAoEmail
	 */
	public String getImportAoEmail() {
		return ImportAoEmail;
	}

	/**
	 * @param importAoEmail the importAoEmail to set
	 */
	public void setImportAoEmail(String importAoEmail) {
		ImportAoEmail = importAoEmail;
	}

	/**
	 * @return the rESULT
	 */
	public String getRESULT() {
		return RESULT;
	}

	/**
	 * @param rESULT the rESULT to set
	 */
	public void setRESULT(String rESULT) {
		RESULT = rESULT;
	}

	/**
	 * @return the rESULT_MSG
	 */
	public String getRESULT_MSG() {
		return RESULT_MSG;
	}

	/**
	 * @param rESULT_MSG the rESULT_MSG to set
	 */
	public void setRESULT_MSG(String rESULT_MSG) {
		RESULT_MSG = rESULT_MSG;
	}

	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return DeviceType;
	}

	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		DeviceType = deviceType;
	}

	/**
	 * @return the importContactAreaCode
	 */
	public String getImportContactAreaCode() {
		return ImportContactAreaCode;
	}

	/**
	 * @param importContactAreaCode the importContactAreaCode to set
	 */
	public void setImportContactAreaCode(String importContactAreaCode) {
		ImportContactAreaCode = importContactAreaCode;
	}

	/**
	 * @return the importContactAreaName
	 */
	public String getImportContactAreaName() {
		return ImportContactAreaName;
	}

	/**
	 * @param importContactAreaName the importContactAreaName to set
	 */
	public void setImportContactAreaName(String importContactAreaName) {
		ImportContactAreaName = importContactAreaName;
	}

	public String getContactAreaCode() {
		return ContactAreaCode;
	}

	public void setContactAreaCode(String contactAreaCode) {
		ContactAreaCode = contactAreaCode;
	}

	public String getContactAreaName() {
		return ContactAreaName;
	}

	public void setContactAreaName(String contactAreaName) {
		ContactAreaName = contactAreaName;
	}

	/**
	 * @return the caseType
	 */
	public String getCaseType() {
		return CaseType;
	}

	/**
	 * @param caseType the caseType to set
	 */
	public void setCaseType(String caseType) {
		CaseType = caseType;
	}

	/**
	 * @return the projectFlag
	 */
	public String getProjectFlag() {
		return ProjectFlag;
	}

	/**
	 * @param projectFlag the projectFlag to set
	 */
	public void setProjectFlag(String projectFlag) {
		ProjectFlag = projectFlag;
	}

	/**
	 * @return the ecrFlag
	 */
	public String getEcrFlag() {
		return EcrFlag;
	}

	/**
	 * @param ecrFlag the ecrFlag to set
	 */
	public void setEcrFlag(String ecrFlag) {
		EcrFlag = ecrFlag;
	}

	/**
	 * @return the installContactAddressLocation
	 */
	public String getInstallContactAddressLocation() {
		return InstallContactAddressLocation;
	}

	/**
	 * @param installContactAddressLocation the installContactAddressLocation to set
	 */
	public void setInstallContactAddressLocation(
			String installContactAddressLocation) {
		InstallContactAddressLocation = installContactAddressLocation;
	}

	/**
	 * @return the installContactAreaCode
	 */
	public String getInstallContactAreaCode() {
		return InstallContactAreaCode;
	}

	/**
	 * @param installContactAreaCode the installContactAreaCode to set
	 */
	public void setInstallContactAreaCode(String installContactAreaCode) {
		InstallContactAreaCode = installContactAreaCode;
	}

	/**
	 * @return the installContactAreaName
	 */
	public String getInstallContactAreaName() {
		return InstallContactAreaName;
	}

	/**
	 * @param installContactAreaName the installContactAreaName to set
	 */
	public void setInstallContactAreaName(String installContactAreaName) {
		InstallContactAreaName = installContactAreaName;
	}

	/**
	 * @return the installContactAddress
	 */
	public String getInstallContactAddress() {
		return InstallContactAddress;
	}

	/**
	 * @param installContactAddress the installContactAddress to set
	 */
	public void setInstallContactAddress(String installContactAddress) {
		InstallContactAddress = installContactAddress;
	}

	/**
	 * @return the installContact
	 */
	public String getInstallContact() {
		return InstallContact;
	}

	/**
	 * @param installContact the installContact to set
	 */
	public void setInstallContact(String installContact) {
		InstallContact = installContact;
	}

	/**
	 * @return the installContactTel
	 */
	public String getInstallContactTel() {
		return InstallContactTel;
	}

	/**
	 * @param installContactTel the installContactTel to set
	 */
	public void setInstallContactTel(String installContactTel) {
		InstallContactTel = installContactTel;
	}

	/**
	 * @return the installContactPhone
	 */
	public String getInstallContactPhone() {
		return InstallContactPhone;
	}

	/**
	 * @param installContactPhone the installContactPhone to set
	 */
	public void setInstallContactPhone(String installContactPhone) {
		InstallContactPhone = installContactPhone;
	}

	/**
	 * @return the installContactEmail
	 */
	public String getInstallContactEmail() {
		return InstallContactEmail;
	}

	/**
	 * @param installContactEmail the installContactEmail to set
	 */
	public void setInstallContactEmail(String installContactEmail) {
		InstallContactEmail = installContactEmail;
	}

	/**
	 * @return the trackVendor
	 */
	public String getTrackVendor() {
		return TrackVendor;
	}

	/**
	 * @param trackVendor the trackVendor to set
	 */
	public void setTrackVendor(String trackVendor) {
		TrackVendor = trackVendor;
	}
	
}
