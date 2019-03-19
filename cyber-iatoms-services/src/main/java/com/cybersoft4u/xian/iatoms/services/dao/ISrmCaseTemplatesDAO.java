package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTemplatesDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseTemplates;


/**
 * Purpose: 工單範本維護DAO Interface
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/9/23
 * @MaintenancePersonnel echomou
 */
public interface ISrmCaseTemplatesDAO  extends IGenericBaseDAO<SrmCaseTemplates> {
	/**
	 * 
	 * Purpose:查詢匯入結果列表
	 * @author HermanWang
	 * @param fileName：範本名
	 * @param category：範本類型
	 * @throws DataAccessException：:出錯時, 丟出DataAccessException
	 * @return List<SrmCaseTemplatesDTO>:返回一個工單範本列表
	 */
	public List<SrmCaseTemplatesDTO> listBy(String fileName, String category) throws DataAccessException;
	/**
	 * Purpose:檢核該範本類別下，文件名不可重複；若有重複
	 * @author HermanWang
	 * @param fileName:範本名
	 * @param category：範本類型
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return boolean 是否重複
	 */
	public boolean isRepeat(String fileName, String category) throws DataAccessException;
	/**
	 * Purpose:根據範本類別和範本名稱獲取上傳範本的id
	 * @author HermanWang
	 * @param category:範本類別
	 * @param fileName:範本名稱
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return String:範本id
	 */
	public List<SrmCaseTemplatesDTO> getUploadTemplatesId(String category, String fileName) throws DataAccessException;
	/**
	 * Purpose:獲取上傳的所有範本List
	 * @author HermanWang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>：返回list Parameter
	 */
	public List<Parameter> getTemplatesList()throws DataAccessException;

}
