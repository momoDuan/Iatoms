package com.cybersoft4u.xian.iatoms.web.controllers;

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
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantHeaderFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;

/**
 * Purpose: 特店表頭維護Controller
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/6/8
 * @MaintenancePersonnel echomou
 */
public class MerchantHeaderController extends AbstractMultiActionController<MerchantHeaderFormDTO> {
	/**
	 * 日志记录物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(MerchantHeaderController.class);
	
	/**
	 * Constructor:无参构造函数
	 */
	public MerchantHeaderController() {
		this.setCommandClass(MerchantHeaderFormDTO.class);
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public MerchantHeaderFormDTO parse(HttpServletRequest request, MerchantHeaderFormDTO command) throws CommonException {
		try {
			String actionId = command.getActionId();
			//保存
			if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
				//綁定需要保存的DTO
				BimMerchantHeaderDTO merchantHeaderDTO = BindPageDataUtils.bindValueObject(request, BimMerchantHeaderDTO.class);
				command.setMerchantHeaderDTO(merchantHeaderDTO);
			}
		} catch (Exception e) {
			if (LOGGER != null) {
				LOGGER.error(this.getClass().getName() + ".parse() Exception.", e);
			}
		}
		return command;
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(MerchantHeaderFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			//檢驗頁面輸入
			BimMerchantHeaderDTO headerDTO = command.getMerchantHeaderDTO();
			if (headerDTO == null) {
				return false;
			}
			String companyId = headerDTO.getCompanyId();
			String merchantCode = headerDTO.getMerchantCode();
			String headerName = headerDTO.getHeaderName();
			String merchantArea = headerDTO.getArea();
			String phone = headerDTO.getPhone();
			String openTime = headerDTO.getOpenHour();
			String closeTime = headerDTO.getCloseHour();
			String aoName = headerDTO.getAoName();
			String aoEmail = headerDTO.getAoemail();
			String contact = headerDTO.getContact();
 			String tel = headerDTO.getContactTel();
			String tel2 = headerDTO.getContactTel2();
			String address = headerDTO.getAddress();
			String isAssetManage = headerDTO.getIsAssetMManage();
			//未輸入客戶
			if (!StringUtils.hasText(companyId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_COMPANY_ID)});
				throw new CommonException(msg);
			}
			//檢核特店代號
			if (!StringUtils.hasText(merchantCode)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CODE)});
				throw new CommonException(msg);
			} else {
				//英數字
				if (!ValidateUtils.numberOrEnglish(merchantCode)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_LIMIT_ENGLISH_AND_NUMBER, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CODE)});
					throw new CommonException(msg);
				} else {
					//長度
					if (!ValidateUtils.length(merchantCode, 0, 20)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CODE),
								IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
						throw new CommonException(msg);
					}
				}
			}
			//檢核表頭
			if (!StringUtils.hasText(headerName)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_NAME)});
				throw new CommonException(msg);
			} else {
				if (!ValidateUtils.length(headerName, 0, 100)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_NAME),
							IAtomsConstants.MAXLENGTH_NUMBER_ONE_HUNDRED});
					throw new CommonException(msg);
				}
			}
			//檢核特店區域
			if (!StringUtils.hasText(merchantArea) && !IAtomsConstants.YES.equals(isAssetManage)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_AREA)});
				throw new CommonException(msg);
			} 
			//檢核行動電話格式
			if (StringUtils.hasText(phone)) {
				if (!ValidateUtils.twMobile(phone)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.MOBLIE_PHINE_FORMAT_ERROR);
					throw new CommonException(msg);
				}
				//長度
				if (!ValidateUtils.varcharLength(phone, 0, 10)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsMessageCode.MOBLIE_PHINE_FORMAT_ERROR),
							IAtomsConstants.MAXLENGTH_NUMBER_TEN});
					throw new CommonException(msg);
				}
			}
			//營業時間
			if (StringUtils.hasText(openTime)) {
				if (!ValidateUtils.valiDataTime(openTime)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.OPEN_TIME_FORMAT);
					throw new CommonException(msg);
				} else {
					if (!ValidateUtils.length(openTime, 0, 5)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_OPEN_TIME),
								IAtomsConstants.MAXLENGTH_NUMBER_FIVE});
						throw new CommonException(msg);
					}
				}
			}
			if (StringUtils.hasText(closeTime)) {
				if (!ValidateUtils.valiDataTime(closeTime)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.OPEN_TIME_FORMAT);
					throw new CommonException(msg);
				} else {
					if (!ValidateUtils.length(closeTime, 0, 5)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CLOSE_TIME),
								IAtomsConstants.MAXLENGTH_NUMBER_FIVE});
						throw new CommonException(msg);
					}
				}
			}
			//檢核AO人員
			if (StringUtils.hasText(aoName)) {
				if (!ValidateUtils.length(aoName, 0, 50)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_AO_NAME),
							IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			//檢核AOemail
			if (StringUtils.hasText(aoEmail)) {
				if (!ValidateUtils.length(aoEmail, 0, 50)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CLOSE_TIME),
							IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				} else {
					if (!ValidateUtils.email(aoEmail)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.FORMAT_ERROR, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_AO_EMAIL)});
						throw new CommonException(msg);
					}
				}
			}
			//檢核聯絡人
			if (StringUtils.hasText(contact)) {
				if (!ValidateUtils.length(contact, 0, 50)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CONTACT_TEL),
							IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			//檢核電話1
			if (StringUtils.hasText(tel)) {
				if (!ValidateUtils.length(tel, 0, 20)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CONTACT_TEL),
							IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
					throw new CommonException(msg);
				}
			}
			//檢核電話2
			if (StringUtils.hasText(tel2)) {
				if (!ValidateUtils.length(tel2, 0, 20)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_CONTACT_TEL2),
							IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
					throw new CommonException(msg);
				}
			}
			//檢核地址
			if (StringUtils.hasText(address)) {
				if (!ValidateUtils.length(address, 0, 100)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_ADDRESS),
							IAtomsConstants.MAXLENGTH_NUMBER_ONE_HUNDRED});
					throw new CommonException(msg);
				}
			}
		}
		return true;
	}
}
