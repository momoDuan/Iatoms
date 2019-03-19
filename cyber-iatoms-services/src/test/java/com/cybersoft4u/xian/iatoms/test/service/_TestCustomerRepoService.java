package com.cybersoft4u.xian.iatoms.test.service;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CustomerRepoFormDTO;
import com.cybersoft4u.xian.iatoms.services.ICustomerRepoService;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose: 客戶設備總表servie的單元測試
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/5/12
 * @MaintenancePersonnel HermanWang
 */
public class _TestCustomerRepoService extends AbstractTestCase  {
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestCustomerRepoService.class);
	/**
	 * 客戶設備總表service注入
	 */
	private ICustomerRepoService customerRepoService;
	/**
	 * 無參構造
	 */
	@SuppressWarnings("deprecation")
	public _TestCustomerRepoService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	public void testQuery(){
		try {
			SessionContext  ctx =  new SessionContext();
			CustomerRepoFormDTO formDTO = new CustomerRepoFormDTO();
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			logonUser.setId("148058048ii6159-0008");
			logonUser.setAdmUserDTO(new AdmUserDTO());
			formDTO.setLogonUser(logonUser);
			formDTO.setRows(10);
			formDTO.setPage(1);
			formDTO.setOrder(null);
			formDTO.setSort(null);
			formDTO.setQueryCustomer("12345");
			formDTO.setYyyyMM("201705");
			formDTO.setQueryMaType("");
			ctx.setRequestParameter(formDTO);
			ctx = customerRepoService.queryData(ctx);
			formDTO = (CustomerRepoFormDTO) ctx.getResponseResult();
			if(!CollectionUtils.isEmpty(formDTO.getList())) {
				Assert.assertNotNull(formDTO.getList());
			} else {
				Assert.assertTrue(formDTO.getList().size() == 0);
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testQuery()", e);
			e.printStackTrace();
		}
	}
	/**
	 * @return the customerRepoService
	 */
	public ICustomerRepoService getCustomerRepoService() {
		return customerRepoService;
	}
	/**
	 * @param customerRepoService the customerRepoService to set
	 */
	public void setCustomerRepoService(ICustomerRepoService customerRepoService) {
		this.customerRepoService = customerRepoService;
	}
	
}
