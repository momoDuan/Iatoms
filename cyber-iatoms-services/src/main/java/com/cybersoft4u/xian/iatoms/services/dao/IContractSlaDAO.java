package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimSla;
/**
 * Purpose: 合約SLA設定DAO接口
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel CrissZhang
 */
public interface IContractSlaDAO extends IGenericBaseDAO<BimSla> {
	
	/**
	 * Purpose:查詢並分頁
	 * @author CrissZhang
	 * @param queryCustomerId : 查询条件-客户编号
	 * @param queryContractId : 查询条件-合约编号
	 * @param queryLocation : 查询条件-区域
	 * @param queryTicketType : 查询条件-案件类别
	 * @param queryImportance : 查询条件-案件类型
	 * @param pageSize ：每页条数
	 * @param pageIndex ：当前页码
	 * @param sort ：排序字段
	 * @param orderby ：排序方式
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return List<BimSlaDTO> ：返回BimSlaDTO对象列表
	 */
	public List<ContractSlaDTO> listBy(String queryCustomerId, String queryContractId, String queryLocation, String queryTicketType, String queryImportance, 
			Integer pageSize, Integer pageIndex, String sort, String orderby, boolean isPage) throws DataAccessException;
	/**
	 * Purpose:查询总笔数
	 * @author CrissZhang
	 * @param queryCustomerId ：查詢條件-客戶編號
	 * @param queryContractId ：查詢條件-合同編號
	 * @param queryLocation ：查詢條件-地區編號
	 * @param queryTicketType ：查詢條件-案件類別
	 * @param queryImportance ：查詢條件-案件類型
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return Integer ：总笔数
	 */
	public int count(String queryCustomerId, String queryContractId, String queryLocation, String queryTicketType,
			String queryImportance) throws DataAccessException;
	
	/**
	 * Purpose:通過客戶和合同獲取其SLA信息
	 * @author CrissZhang
	 * @param customerId ：客戶編號
	 * @param contractId ：合同編號
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return List<ContractSlaDTO> ：ContractSlaDTO對象列表
	 */
	public List<ContractSlaDTO> getSlaByCustomerAndContract(String customerId, String contractId) throws DataAccessException;

	/**
	 * Purpose:检查主键是否重复
	 * @author CrissZhang
	 * @param contractId : 原始合约编号
	 * @param copyContractId : 复制合约编号
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return boolean ： 返回布爾類型
	 */
	public boolean isCopyRepeat(String contractId, String copyContractId) throws DataAccessException;
	
	/**
	 * Purpose:檢查聯合唯一鍵是否重複
	 * @author CrissZhang
	 * @param slaId : 合約sla主鍵
	 * @param contractId : 合約編號
	 * @param ticketType ： 案件類別
	 * @param location ： 地區
	 * @param ticketMode ： 案件類型
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return boolean ： 返回布爾類型
	 */
	public boolean isRepeat(String slaId, String contractId, String ticketType, String location, String ticketMode) throws DataAccessException;
	
}
