package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarYearDTO;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

/**
 * Purpose: 舊資料批次數據訪問層接口
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-7-13
 * @MaintenancePersonnel CrissZhang
 */
public interface IOldDataBatchDAO extends IGenericBaseDAO{

	
	/**
	 * Purpose: 查詢Foms行事曆年檔數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<BimCalendarYearDTO>：行事曆年檔列表
	 */
	public List<BimCalendarYearDTO> listCalendarYear() throws DataAccessException;
	
	/**
	 * Purpose: 查詢Foms行事曆日檔數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<BimCalendarDayDTO>：行事曆日檔列表
	 */
	public List<BimCalendarDayDTO> listCalendarDate() throws DataAccessException;
	
}