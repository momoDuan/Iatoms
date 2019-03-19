package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import cafe.core.bean.Message;
import cafe.core.exception.CommonException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CalendarFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * Purpose: 行事歷controller
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/6/30
 * @MaintenancePersonnel cybersoft
 */
public class CalendarController extends AbstractMultiActionController<CalendarFormDTO> {
	/**
	 * 日志记录物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(CalendarController.class);

	/**
	 * Constructor:无参构造函数
	 */
	public CalendarController() {
		this.setCommandClass(CalendarFormDTO.class);
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public CalendarFormDTO parse(HttpServletRequest request, CalendarFormDTO command) throws CommonException {
		try {
			// 获取actionId
			String actionId = command.getActionId();
			//保存年度行事歷
			if (IAtomsConstants.ACTION_SAVE_CALENDAR_YEAR.equals(actionId)) {
				//拿到json格式的對象
				String weekRests = command.getWeekRests();
				Gson gson = new GsonBuilder().create();  
				List<Integer> weekRestsList = (List<Integer>)gson.fromJson(weekRests, new TypeToken<List<Integer>>(){}.getType());
				//轉換
				command.setWeekRestLists(weekRestsList);
			}
			//保存行事曆
			if (IAtomsConstants.ACTION_SAVE_CALENDAR_DAY.equals(actionId)) {
				//綁定需要保存的DTO
				BimCalendarDayDTO bimCalendarDayDTO = BindPageDataUtils.bindValueObject(request, BimCalendarDayDTO.class);
				command.setCalendarDayDTO(bimCalendarDayDTO);
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".controller Exception:", e);
		}
		return command;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(CalendarFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE_CALENDAR_YEAR.equals(actionId)) {
			//檢驗頁面輸入
			List<Integer> weekRestsList = command.getWeekRestLists();
			if (weekRestsList == null) {
				return false;
			}
			String weekRests = command.getWeekRests();
			//未輸入周休日
			if (!StringUtils.hasText(weekRests)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CALENDAR_WEEKRESTS)});
				throw new CommonException(msg);
			}
		}
		if (IAtomsConstants.ACTION_SAVE_CALENDAR_DAY.equals(actionId)) {
			//檢驗頁面輸入
			BimCalendarDayDTO dayDTO = command.getCalendarDayDTO();
			if (dayDTO == null) {
				return false;
			}
			String isHoliday = dayDTO.getIsHoliday(); 
			String remark = dayDTO.getComment();
			//未輸入是否假日
			if (!StringUtils.hasText(isHoliday)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CALENDAR_IS_HOLIDAY)});
				throw new CommonException(msg);
			}
			//檢核說明
			if (StringUtils.hasText(remark)) {
				if (!ValidateUtils.length(remark, 0, 200)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_STOCKTAKE_REMARK),IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
					throw new CommonException(msg);
				}
			}
		}
		return true;
	}
}
