package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.bean.identity.LogonUser;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarYearDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CalendarFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.CopyPropertiesUtils;
import com.cybersoft4u.xian.iatoms.services.ICalendarService;
import com.cybersoft4u.xian.iatoms.services.dao.ICalendarDayDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICalendarYearDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCalendarDay;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCalendarYear;
/**
 * Purpose: 行事曆service
 * @author echomou	
 * @since  JDK 1.6
 * @date   2016/6/30
 * @MaintenancePersonnel cybersoft
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CalendarService extends AtomicService implements ICalendarService {
	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(CalendarService.class);
	/**
	 * Constructor:無參构造函数
	 */
	public CalendarService() {
		super();
	}
	/**
	 * 年度行事曆DAO
	 */
	private ICalendarYearDAO calendarYearDAO;
	/**
	 * 行事曆
	 */
	private ICalendarDayDAO calendarDayDAO;
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICalendarService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		CalendarFormDTO calendarFormDTO = null;
		try{
			//拿到formDTO
			calendarFormDTO = (CalendarFormDTO) sessionContext.getRequestParameter();
			//頁面上的年份
			Integer year = calendarFormDTO.getQueryYear();
			//如果formDTO里沒有year值，則賦予當前年份
			if (year == null) {
				year = Integer.valueOf(DateTimeUtils.getCurrentCalendar(DateTimeUtils.YEAR));
			}
			Map map = new HashMap();
			if (year.intValue() > 0) {
				//頁面初始化時
				calendarFormDTO.setQueryYear(year);
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROW, year);
				//得到該年份“行事曆”設定的所有假日
				List<BimCalendarDayDTO> bimCalendarDayDTOs = this.calendarDayDAO.listBy(year);
				if (!CollectionUtils.isEmpty(bimCalendarDayDTOs)) {
					//bimCalendarDayDTOs放入formDTO
					calendarFormDTO.setCalendarDayDTOs(bimCalendarDayDTOs);
					//bimCalendarDayDTOs放入map
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, bimCalendarDayDTOs);
					sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, 
							IAtomsMessageCode.QUERY_SUCCESS));
				} else {
					sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, 
							IAtomsMessageCode.DATA_NOT_FOUND));
				}
			}
			//封裝map
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			sessionContext.setResponseResult(calendarFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".init() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".init():" + e, e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICalendarService#initPreYear(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initPreYear(SessionContext sessionContext) throws ServiceException {
		CalendarFormDTO calendarFormDTO = null;
		try {
			//拿到formDTO
			calendarFormDTO = (CalendarFormDTO) sessionContext.getRequestParameter();
			//頁面上的年份
			Integer year = calendarFormDTO.getQueryYear();
			//如果formDTO里沒有year值，則賦予當前年份
			if (year == null) {
				year = Integer.valueOf(DateTimeUtils.getCurrentCalendar(DateTimeUtils.YEAR));
			}
			year = Integer.valueOf(year.intValue() - 1);
			calendarFormDTO.setQueryYear(year);
			sessionContext.setResponseResult(calendarFormDTO);
			init(sessionContext);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".initPreYear():" + e, e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICalendarService#initNextYear(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initNextYear(SessionContext sessionContext) throws ServiceException {
		CalendarFormDTO calendarFormDTO = null;
		try {
			//拿到formDTO
			calendarFormDTO = (CalendarFormDTO) sessionContext.getRequestParameter();
			//頁面上的年份
			Integer year = calendarFormDTO.getQueryYear();
			//如果formDTO里沒有year值，則賦予當前年份
			if (year == null) {
				year = Integer.valueOf(DateTimeUtils.getCurrentCalendar(DateTimeUtils.YEAR));
			}
			year = Integer.valueOf(year.intValue() + 1);
			calendarFormDTO.setQueryYear(year);
			sessionContext.setResponseResult(calendarFormDTO);
			init(sessionContext);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".initNextYear():" + e, e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICalendarService#initYearDetail(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initYearDetail(SessionContext sessionContext) throws ServiceException {
		CalendarFormDTO calendarFormDTO = null;
		try {
			calendarFormDTO = (CalendarFormDTO) sessionContext.getRequestParameter();
			BimCalendarYearDTO calendarYearDTO = new BimCalendarYearDTO();
			//拿到頁面上的年份
			Integer year = calendarFormDTO.getQueryYear();
			if (year.intValue() > 0) {
				//得到該年的周休日
				BimCalendarYear bimCalendarYear = this.calendarYearDAO.findByPrimaryKey(BimCalendarYear.class, year);
				if (bimCalendarYear != null) {
					Transformer transformer = new SimpleDtoDmoTransformer();
					transformer.transform(bimCalendarYear, calendarYearDTO);
				}
				calendarYearDTO.setYear(year);
				calendarFormDTO.setCalendarYearDTO(calendarYearDTO);
			}
			sessionContext.setResponseResult(calendarFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".initYear() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".initYear():" + e, e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICalendarService#initDateDetail(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initDateDetail(SessionContext sessionContext) throws ServiceException {
		CalendarFormDTO calendarFormDTO = null;
		try {
			calendarFormDTO = (CalendarFormDTO) sessionContext.getRequestParameter();
			BimCalendarDayDTO calendarDayDTO = new BimCalendarDayDTO();
			//拿到所選擇的日期
			Date day = calendarFormDTO.getDay();
			if (day != null) {
				calendarDayDTO.setDay(day);
				BimCalendarDay bimCalendarDay = this.calendarDayDAO.findByPrimaryKey(BimCalendarDay.class, day);
				if (bimCalendarDay != null) {
					//如果“行事曆”存在，DMO轉為DTO
					Transformer transformer = new SimpleDtoDmoTransformer();
					transformer.transform(bimCalendarDay, calendarDayDTO);
				} 
				calendarFormDTO.setCalendarDayDTO(calendarDayDTO);
			}
			sessionContext.setResponseResult(calendarFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".initDay() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".initDay():" + e, e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICalendarService#saveCalendarYear(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext saveCalendarYear(SessionContext sessionContext) throws ServiceException {
		CalendarFormDTO calendarFormDTO = null;
		try {
			calendarFormDTO = (CalendarFormDTO) sessionContext.getRequestParameter();
			BimCalendarYearDTO calendarYearDTO = new BimCalendarYearDTO();
			Transformer transformer = new SimpleDtoDmoTransformer();
			//拿到年份
			Integer year = calendarFormDTO.getQueryYear();
			//放入DTO
			calendarYearDTO.setYear(year);
			List<BimCalendarDay> saveHolidayList = new ArrayList<BimCalendarDay>();
			if (year.intValue() > 0) {
				//查找該年的DMO是否存在
				BimCalendarYear bimCalendarYear = this.calendarYearDAO.findByPrimaryKey(BimCalendarYear.class, year);
				//獲取登錄信息
				LogonUser logonUser = calendarFormDTO.getLogonUser();
				String userId = null; 
				String userName = null;
				if (logonUser != null) {
					//得到當前登入者的Id和Name
					userId = logonUser.getId();
					userName = logonUser.getName();
				} else {
					sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, 
							IAtomsMessageCode.LIMITED_LOGON_ID));
					return sessionContext;
				}
				if (bimCalendarYear == null) { 
					//保存，DTO轉DMO
					bimCalendarYear = (BimCalendarYear) transformer.transform(calendarYearDTO, new BimCalendarYear());
					bimCalendarYear.setCreatedById(userId);
					bimCalendarYear.setCreatedByName(userName);
					bimCalendarYear.setUpdatedById(userId);
					bimCalendarYear.setUpdatedByName(userName);
					bimCalendarYear.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					bimCalendarYear.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				} else {
					//把修改的值複製到bimCalendarYear中
					new CopyPropertiesUtils().copyProperties(calendarYearDTO, bimCalendarYear, null);
					bimCalendarYear.setUpdatedById(userId);
					bimCalendarYear.setUpdatedByName(userName);
					bimCalendarYear.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				}
				//給周休日N值
				bimCalendarYear.setMonday(IAtomsConstants.NO);
				bimCalendarYear.setTuesday(IAtomsConstants.NO);
				bimCalendarYear.setWednesday(IAtomsConstants.NO);
				bimCalendarYear.setThursday(IAtomsConstants.NO);
				bimCalendarYear.setFriday(IAtomsConstants.NO);
				bimCalendarYear.setSaturday(IAtomsConstants.NO);
				bimCalendarYear.setSunday(IAtomsConstants.NO);
				//拿到周休日，是周休日的給Y
				List<Integer> weekList = calendarFormDTO.getWeekRestLists();
				int weekLength = weekList.size();
				for (int i = 0; i < weekLength; i++) {
					if (Calendar.MONDAY == weekList.get(i).intValue()) {
						bimCalendarYear.setMonday(IAtomsConstants.YES);
					}
					if (Calendar.TUESDAY == weekList.get(i).intValue()) {
						bimCalendarYear.setTuesday(IAtomsConstants.YES);
					}
					if (Calendar.WEDNESDAY == weekList.get(i).intValue()) {
						bimCalendarYear.setWednesday(IAtomsConstants.YES);
					}
					if (Calendar.THURSDAY == weekList.get(i).intValue()) {
						bimCalendarYear.setThursday(IAtomsConstants.YES);
					}
					if (Calendar.FRIDAY == weekList.get(i).intValue()) {
						bimCalendarYear.setFriday(IAtomsConstants.YES);
					}
					if (Calendar.SATURDAY == weekList.get(i).intValue()) {
						bimCalendarYear.setSaturday(IAtomsConstants.YES);
					}
					if (Calendar.SUNDAY == weekList.get(i).intValue()) {
						bimCalendarYear.setSunday(IAtomsConstants.YES);
					}
				}
				//刪除該年在“行事曆”中所有的記錄
				this.calendarDayDAO.deleteHolidayByYear(year);
				
				//保存“年事歷”
				this.calendarYearDAO.getDaoSupport().saveOrUpdate(bimCalendarYear);
				this.calendarYearDAO.getDaoSupport().flush();
				
				//根據年份查詢存在說明的日期
				List<BimCalendarDayDTO> calendarDays = this.calendarDayDAO.queryDateByYear(year);
				//得到當前年第一天和最後一天
				Date firstDay = DateTimeUtils.toDate(year.intValue(), Calendar.JANUARY + 1, 1);
				Date endDay = DateTimeUtils.toDate(year.intValue(), Calendar.DECEMBER + 1, 31);
				//當前年的天數
				int yearDays = (int)((endDay.getTime() - firstDay.getTime()) / (24 * 3600 * 1000));
				int week = -1;
				BimCalendarDay calendarDay = null;
				//遍歷整年，得到周休日的日期
				for (int i = 0; i < yearDays + 1; i++) {
					//firstDay在一周中第幾天
					week = DateTimeUtils.getCalendar(firstDay, Calendar.DAY_OF_WEEK);
					if (weekList.contains(Integer.valueOf(week))) {
						calendarDay = new BimCalendarDay();
						calendarDay.setDay(firstDay);
						calendarDay.setIsHoliday(IAtomsConstants.YES);
						calendarDay.setCreatedById(userId);
						calendarDay.setCreatedByName(userName);
						calendarDay.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
						if (!CollectionUtils.isEmpty(calendarDays)) {
							//循環之前存在說明的對象
							for (BimCalendarDayDTO bimCalendarDayDTO : calendarDays) {
								//將DTO轉換為DMO
								BimCalendarDay bimCalendarDay = new BimCalendarDay();
								bimCalendarDay = (BimCalendarDay) transformer.transform(bimCalendarDayDTO, new BimCalendarDay());
								//當該日期存在說明是設置說明
								if (firstDay.compareTo(bimCalendarDay.getDay()) == 0) {
									calendarDay.setComment(bimCalendarDay.getComment());
									calendarDay.setIsHoliday(IAtomsConstants.YES);
									saveHolidayList.add(calendarDay); 
								} 
							}
						}
						saveHolidayList.add(calendarDay);
						//每次加一天
						firstDay = DateTimeUtils.addCalendar(firstDay, 0, 0, 1);	
					} else {
						//修改年度行事曆前說明欄位有值
						//當存在又說明的信息列表時
						if (!CollectionUtils.isEmpty(calendarDays)) {
							calendarDay = new BimCalendarDay();
							calendarDay.setDay(firstDay);
							calendarDay.setCreatedById(userId);
							calendarDay.setCreatedByName(userName);
							calendarDay.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
							//循環之前存在說明的對象
							for (BimCalendarDayDTO bimCalendarDayDTO : calendarDays) {
								//將DTO轉換為DMO
								BimCalendarDay bimCalendarDay = new BimCalendarDay();
								bimCalendarDay = (BimCalendarDay) transformer.transform(bimCalendarDayDTO, new BimCalendarDay());
								//當前日期存在說明時設置節假日標記位和說明欄位的值
								if (firstDay.compareTo(bimCalendarDay.getDay()) == 0) {
									calendarDay.setComment(bimCalendarDay.getComment());
									calendarDay.setIsHoliday(IAtomsConstants.NO);
									//將要保存的日期對象添加到日期列表中
									saveHolidayList.add(calendarDay); 
								} 
							}
						}
						//每次加一天
						firstDay = DateTimeUtils.addCalendar(firstDay, 0, 0, 1);
					}
				}
				//將周休日的具體日期保存在“行事曆”
				for (BimCalendarDay bimCalendarDay : saveHolidayList) { 
					this.calendarDayDAO.getDaoSupport().saveOrUpdate(bimCalendarDay);
					this.calendarDayDAO.getDaoSupport().flush();
				}
				//調用初始化方法；得到所有假日
				this.init(sessionContext);
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, 
						IAtomsMessageCode.CALENDAR_YEAR_PERMISSION_SUCCESS));
			}	
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".saveYear() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, 
					IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}));
			LOGGER.error(this.getClass().getName() + ".saveYear():" + e, e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICalendarService#saveCalendarDate(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext saveCalendarDate(SessionContext sessionContext) throws ServiceException {
		CalendarFormDTO calendarFormDTO = null;
		try {
			calendarFormDTO = (CalendarFormDTO) sessionContext.getRequestParameter();
			BimCalendarDayDTO bimCalendarDayDTO = calendarFormDTO.getCalendarDayDTO();
			Transformer transformer = new SimpleDtoDmoTransformer();
			//得到Day
			Date day = calendarFormDTO.getDay();
			if (day != null) {
				//查找該天的DMO是否存在
				BimCalendarDay bimCalendarDay = this.calendarDayDAO.findByPrimaryKey(BimCalendarDay.class, day);
				//獲取登錄信息
				LogonUser logonUser = calendarFormDTO.getLogonUser();
				String userId = null; 
				String userName = null;
				if (logonUser != null) {
					//得到當前登入者的Id和Name
					userId = logonUser.getId();
					userName = logonUser.getName();
				}
				if (bimCalendarDay == null) { 
					//保存，DTO轉DMO
					bimCalendarDay = (BimCalendarDay) transformer.transform(bimCalendarDayDTO, new BimCalendarDay());
					bimCalendarDay.setCreatedById(userId);
					bimCalendarDay.setCreatedByName(userName);
					bimCalendarDay.setUpdatedById(userId);
					bimCalendarDay.setUpdatedByName(userName);
					bimCalendarDay.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					bimCalendarDay.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				} else {
					// 把修改的值複製到bimCalendarYear中
					new CopyPropertiesUtils().copyProperties(bimCalendarDayDTO, bimCalendarDay, null);
					bimCalendarDay.setUpdatedById(userId);
					bimCalendarDay.setUpdatedByName(userName);
					bimCalendarDay.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				}
				bimCalendarDay.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
				this.calendarDayDAO.getDaoSupport().saveOrUpdate(bimCalendarDay);
				this.calendarDayDAO.getDaoSupport().flush();
				//調用初始化方法；得到周修日與假日
				this.init(sessionContext);
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS,
						IAtomsMessageCode.CALENDAR_DAY_PERMISSION_SUCCESS));
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".saveDay() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, 
					IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}));
			LOGGER.error(this.getClass().getName() + ".saveDay():" + e, e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	/**
	 * @return the calendarYearDAO
	 */
	public ICalendarYearDAO getCalendarYearDAO() {
		return calendarYearDAO;
	}
	/**
	 * @param calendarYearDAO the calendarYearDAO to set
	 */
	public void setCalendarYearDAO(ICalendarYearDAO calendarYearDAO) {
		this.calendarYearDAO = calendarYearDAO;
	}
	/**
	 * @return the calendarDayDAO
	 */
	public ICalendarDayDAO getCalendarDayDAO() {
		return calendarDayDAO;
	}
	/**
	 * @param calendarDayDAO the calendarDayDAO to set
	 */
	public void setCalendarDayDAO(ICalendarDayDAO calendarDayDAO) {
		this.calendarDayDAO = calendarDayDAO;
	}
	
}
