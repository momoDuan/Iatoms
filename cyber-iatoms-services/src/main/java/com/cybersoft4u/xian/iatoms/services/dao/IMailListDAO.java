package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.MailListDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMailList;

/**
 * 
 * Purpose: 電子郵件羣組DAO interface 
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016-7-1
 * @MaintenancePersonnel CarrieDuan
 */
public interface IMailListDAO extends IGenericBaseDAO<BimMailList> {
	
	/**
	 * Purpose:查询電子郵件羣組維維護列表
	 * @author CarrieDuan
	 * @param mailGroup ：通知種類
	 * @param name ：名稱
	 * @param pageIndex ：頁碼
	 * @param pageSize ：每頁顯示條數
	 * @param sort ：排序列
	 * @param order ：排序方式
	 * @throws DataAccessException:出错时抛出：DataAccessExecption
	 * @return List<MailListDTO> 返回電子郵件羣組列表
	 */
	public List<MailListDTO> listBy(String mailGroup, String name, int pageIndex, int pageSize, String sort, String order) throws DataAccessException;
	/**
	 * 
	 * Purpose:查询電子郵件羣組維維護總條數
	 * @author CarrieDuan
	 * @param mailGroup ：通知種類
	 * @param name ：名稱
	 * @throws DataAccessException:出错时抛出：DataAccessExecption
	 * @return Integer:電子郵件羣組維維護總條數
	 */
	public Integer count(String mailGroup, String name) throws DataAccessException;

	/**
	 * Purpose:檢查同一種設備種類下郵件地址是否重復
	 * @author CarrieDuan
	 * @param mailGroup:通知種類
	 * @param mail:郵件地址
	 * @param mail:ID
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return MailListDTO:獲得同通知種類下的所有郵件地址
	 */
	public Boolean isCheck(String mailGroup, String mail, String mailId) throws DataAccessException;

}
