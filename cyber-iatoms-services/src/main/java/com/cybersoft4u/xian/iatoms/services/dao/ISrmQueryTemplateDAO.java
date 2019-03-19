package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import com.cybersoft4u.xian.iatoms.services.dmo.SrmQueryTemplate;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

/**
 * Purpose: 用戶欄位模板維護檔 DAO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017/2/24
 * @MaintenancePersonnel CrissZhang
 */
public interface ISrmQueryTemplateDAO extends IGenericBaseDAO<SrmQueryTemplate>{

	/**
	 * Purpose:得到用戶模板集合
	 * @author CrissZhang
	 * @param userId ： 用戶編號
	 * @param isCurrentTemplate ： 查當前模板
	 * @param isSettingOther ： 是否設置其他模板
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>:模板列表
	 */
	public List<Parameter> getUserColumnTemplateList(String userId, boolean isCurrentTemplate, boolean isSettingOther) throws DataAccessException;
	
	/**
	 * Purpose:更新用戶其他模板
	 * @author CrissZhang
	 * @param userId : 用戶編號
	 * @param templateId : 模板編號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void updateOtherTemplate(String userId, String templateId) throws DataAccessException;
	
	/**
	 * Purpose:判斷用戶是否設定其他模板
	 * @author CrissZhang
	 * @param userId : 用戶編號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return boolean
	 */
	public boolean isSettingOther(String userId) throws DataAccessException;
	
	/**
	 * Purpose:判斷該客戶是否已經存在模板名稱
	 * @author CrissZhang
	 * @param templateId : 模板編號
	 * @param userId ： 用戶編號
	 * @param templateName ： 模板名稱
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return boolean ： 返回是否重複的消息
	 */
	public boolean isRepeat(String templateId, String userId, String templateName) throws DataAccessException;
	
	/**
	 * Purpose:檢核用戶的模板數量是否超過限定
	 * @author CrissZhang
	 * @param userId : 用戶編號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return boolean ： 返回是否超過限定
	 */
	public boolean isTemplateOverLimit(String userId) throws DataAccessException;

}
