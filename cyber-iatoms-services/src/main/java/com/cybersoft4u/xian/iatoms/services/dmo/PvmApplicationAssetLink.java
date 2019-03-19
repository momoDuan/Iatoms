package com.cybersoft4u.xian.iatoms.services.dmo;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationAssetLinkDTO;

import cafe.core.dmo.DomainModelObject;

public class PvmApplicationAssetLink extends DomainModelObject<PvmApplicationAssetLinkId, ApplicationAssetLinkDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8175360801016404725L;
	private PvmApplicationAssetLinkId id;

	public PvmApplicationAssetLink() {
		super();
	}
	public PvmApplicationAssetLink(PvmApplicationAssetLinkId id) {
		super();
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public PvmApplicationAssetLinkId getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(PvmApplicationAssetLinkId id) {
		this.id = id;
	}
	
}
