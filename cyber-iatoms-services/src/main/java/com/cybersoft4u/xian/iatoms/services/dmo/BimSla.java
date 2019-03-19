package com.cybersoft4u.xian.iatoms.services.dmo;

import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;

public class BimSla extends DomainModelObject<String, ContractSlaDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8599284074326817220L;

	private String slaId;
	private String contractId;
	private String ticketType;
	private String location;
	private String ticketMode;
	
	private String isWorkDay = "N";
	private String isThatDay = "N";
	private String thatDayTime;
	private Double responseHour;
	private Double responseWarnning;
	private Double arriveHour;
	private Double arriveWarnning;
	private Double completeHour;
	private Double completeWarnning;
	private String comment;
	private String createdById;
	private String createdByName;
	private Date createdDate;
	private String updatedById;
	private String updatedByName;
	private Date updatedDate;

	public BimSla() {
	}

	public BimSla(String slaId, String contractId, String ticketType,
			String location, String ticketMode, String isWorkDay) {
		this.slaId = slaId;
		this.contractId = contractId;
		this.ticketType = ticketType;
		this.location = location;
		this.ticketMode = ticketMode;
		this.isWorkDay = isWorkDay;
	}

	public BimSla(String slaId, String contractId, String ticketType,
			String location, String ticketMode, String isWorkDay,
			String isThatDay, String thatDayTime, Double responseHour,
			Double responseWarnning, Double arriveHour,
			Double arriveWarnning, Double completeHour,
			Double completeWarnning, String comment, String createdById,
			String createdByName, Date createdDate, String updatedById,
			String updatedByName, Date updatedDate) {
		this.slaId = slaId;
		this.contractId = contractId;
		this.ticketType = ticketType;
		this.location = location;
		this.ticketMode = ticketMode;
		this.isWorkDay = isWorkDay;
		this.isThatDay = isThatDay;
		this.thatDayTime = thatDayTime;
		this.responseHour = responseHour;
		this.responseWarnning = responseWarnning;
		this.arriveHour = arriveHour;
		this.arriveWarnning = arriveWarnning;
		this.completeHour = completeHour;
		this.completeWarnning = completeWarnning;
		this.comment = comment;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
	}

	public String getSlaId() {
		return this.slaId;
	}

	public void setSlaId(String slaId) {
		this.slaId = slaId;
	}

	public String getContractId() {
		return this.contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getTicketType() {
		return this.ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTicketMode() {
		return this.ticketMode;
	}

	public void setTicketMode(String ticketMode) {
		this.ticketMode = ticketMode;
	}
	
	public String getIsWorkDay() {
		return this.isWorkDay;
	}

	public void setIsWorkDay(String isWorkDay) {
		this.isWorkDay = isWorkDay;
	}

	public String getIsThatDay() {
		return this.isThatDay;
	}

	public void setIsThatDay(String isThatDay) {
		this.isThatDay = isThatDay;
	}

	public String getThatDayTime() {
		return this.thatDayTime;
	}

	public void setThatDayTime(String thatDayTime) {
		this.thatDayTime = thatDayTime;
	}

	public Double getResponseHour() {
		return this.responseHour;
	}

	public void setResponseHour(Double responseHour) {
		this.responseHour = responseHour;
	}

	public Double getResponseWarnning() {
		return this.responseWarnning;
	}

	public void setResponseWarnning(Double responseWarnning) {
		this.responseWarnning = responseWarnning;
	}

	public Double getArriveHour() {
		return this.arriveHour;
	}

	public void setArriveHour(Double arriveHour) {
		this.arriveHour = arriveHour;
	}

	public Double getArriveWarnning() {
		return this.arriveWarnning;
	}

	public void setArriveWarnning(Double arriveWarnning) {
		this.arriveWarnning = arriveWarnning;
	}

	public Double getCompleteHour() {
		return this.completeHour;
	}

	public void setCompleteHour(Double completeHour) {
		this.completeHour = completeHour;
	}

	public Double getCompleteWarnning() {
		return this.completeWarnning;
	}

	public void setCompleteWarnning(Double completeWarnning) {
		this.completeWarnning = completeWarnning;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedById() {
		return this.createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public String getCreatedByName() {
		return this.createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedById() {
		return this.updatedById;
	}

	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	public String getUpdatedByName() {
		return this.updatedByName;
	}

	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}