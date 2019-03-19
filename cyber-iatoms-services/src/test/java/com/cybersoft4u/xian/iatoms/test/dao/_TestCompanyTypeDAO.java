package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.List;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyTypeDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.impl.CompanyTypeDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose:公司類型DAO的單元測試
 * @author ElvaHe
 * @since JDK 1.6
 * @date 2017/05/11
 * @MaintenancePersonnel ElvaHe
 */
public class _TestCompanyTypeDAO extends AbstractTestCase {
	
	/**
	 * 日誌記錄物件
	 */
	private static final CafeLog LOGEER = CafeLogFactory.getLog(CompanyTypeDAO.class);
	
	/**
	 * 注入公司類型DAO
	 */
	private ICompanyTypeDAO companyTypeDAO;
	
	/**
	 * Purpose：測試根據公司編號查找公司類型
	 * @author ElvaHe
	 * @return void
	 */
	public void testListByCompanyId(){
		try {
			//公司編號
			String companyId = "1470648902571-0000as";
			List<CompanyTypeDTO> companyTypeDTOs = this.companyTypeDAO.listByCompanyId(companyId);
			if (!CollectionUtils.isEmpty(companyTypeDTOs)) {
				Assert.assertNotNull(companyTypeDTOs);
			} else {
				Assert.assertFalse(companyTypeDTOs == null);
			}
		} catch (Exception e) {
			LOGEER.error("_TestCompanyTypeDAO.testListByCompanyId()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:根據公司編號刪除公司類型
	 * @author ElvaHe
	 * @return void
	 */
	public void testDeleteByCompanyId(){
		try {
			String companyId = "1470648902571-0000";
			if(StringUtils.hasText(companyId)){
				this.companyTypeDAO.deleteByCompanyId(companyId);
				Assert.assertTrue(StringUtils.hasText(companyId));
			} else {
				Assert.assertFalse(!StringUtils.hasText(companyId));
			}
		} catch (Exception e) {
			LOGEER.error("_TestCompanyTypeDAO.testDeleteByCompanyId()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據公司ID檢核是否符合公司類型
	 * @author ElvaHe
	 * @return void
	 */
	public void testCheckCompanyType(){
		try {
			String companyName = "英雄聯盟a";
			String companyType = "CUSTOMER";
			boolean result =  this.companyTypeDAO.checkCompanyType(companyName, companyType);
			if(result){
				Assert.assertTrue(result);
			} else {
				Assert.assertFalse(result);
			}
		} catch (Exception e) {
			LOGEER.error("_TestCompanyTypeDAO.testCheckCompanyType()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the companyTypeDAO
	 */
	public ICompanyTypeDAO getCompanyTypeDAO() {
		return companyTypeDAO;
	}

	/**
	 * @param companyTypeDAO the companyTypeDAO to set
	 */
	public void setCompanyTypeDAO(ICompanyTypeDAO companyTypeDAO) {
		this.companyTypeDAO = companyTypeDAO;
	}
	
}
