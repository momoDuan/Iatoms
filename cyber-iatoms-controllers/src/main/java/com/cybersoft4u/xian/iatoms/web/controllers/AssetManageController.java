package com.cybersoft4u.xian.iatoms.web.controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;






import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.config.SystemConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.IAtomsUtils;
import com.cybersoft4u.xian.iatoms.common.utils.POIUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
/**
 * Purpose: 設備管理作業 Controller
 * @author amandawang
 * @since  JDK 1.6
 * @date   2016/7/25
 * @MaintenancePersonnel amandawang
 */
public class AssetManageController extends AbstractMultiActionController<AssetManageFormDTO> {

	/**
	 * 日誌記錄物件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, AssetManageController.class);
	
	/**
	 * Constructor:无參構造函數
	 */
	public AssetManageController() {
		this.setCommandClass(AssetManageFormDTO.class);
	}
	
	/**
	 * 所有要匯出的欄位
	 */
	private Map<String, String> exportField;
	
	/** (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(AssetManageFormDTO command) throws CommonException {
		long startTime = System.currentTimeMillis();
		if (command == null) {
			return false;
		}
		IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
		// 获取actionId
		String actionId = command.getActionId();
		DmmRepositoryDTO dmmRepositoryDTO = command.getDmmRepositoryDTO();
		Message msg = null;
		//其他修改
		if(IAtomsConstants.ACTION_UPDATE.equals(actionId)) {
			String propertyId = dmmRepositoryDTO.getPropertyId();
			String tid = dmmRepositoryDTO.getTid();
			String dtid = dmmRepositoryDTO.getDtid();
			String repairComment = dmmRepositoryDTO.getRepairComment();
			String simEnableNo = dmmRepositoryDTO.getSimEnableNo();
			//核檢財產編號長度是否超出
			if (StringUtils.hasText(propertyId)) {
				if (!ValidateUtils.varcharLength(propertyId, 1, 20)) {
                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_PROPERTY_ID),
                            IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
                    throw new CommonException(msg);
                } else if (!ValidateUtils.numberOrEnglish(propertyId)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_LIMIT_ENGLISH_AND_NUMBER, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_PROPERTY_ID)});
					throw new CommonException(msg);
				} else {
					try {
						SessionContext sessionContext = new SessionContext();
						AssetManageFormDTO assetManageFormDTO = new AssetManageFormDTO();
						DmmRepositoryDTO repositoryDTO = new DmmRepositoryDTO();
						repositoryDTO.setPropertyId(propertyId);
						repositoryDTO.setAssetId(dmmRepositoryDTO.getAssetId());
						assetManageFormDTO.setDmmRepositoryDTO(repositoryDTO);
						sessionContext.setRequestParameter(assetManageFormDTO);
						SessionContext ctx = this.getServiceLocator().doService(logonUser, this.getServiceId(), IAtomsConstants.ACTION_CHECK, command);
						Message message = ctx.getReturnMessage();
						if (!message.isSuccess()) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_VALUE_REPEAT, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_PROPERTY_ID)});
							throw new CommonException(msg);
						}
					} catch (Exception e) {
						if(e instanceof CommonException){
							throw new CommonException(((CommonException) e).getErrorMessage());
						} else {
							return false;
						}
					}
				}
			}
			//租賃編號--新增可編輯欄位update by 2017-06-14
			if (StringUtils.hasText(simEnableNo)) {
				if (!ValidateUtils.length(simEnableNo, 1, 20)) {
                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_SIM_ENABLE_NO),
                            IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
                    throw new CommonException(msg);
                }
			}
			/*try {
				//核檢設備啟用日
				if (enableDate != null) {
					if (!ValidateUtils.length(enableDate.toString().substring(1, 10), 1, 10)) {
						date = DateTimeUtils.parseDate(enableDate.toString(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
						if (!ValidateUtils.checkDate(date.toString())) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_ENABLE_DATE)});
							throw new CommonException(msg);
						}
					}
				}
				//核檢租賃啟用日
				if (simEnableDate != null) {
					if (!ValidateUtils.length(simEnableDate.toString().substring(1, 10), 1, 10)) {
						date = DateTimeUtils.parseDate(simEnableDate.toString(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
						if (!ValidateUtils.checkDate(date.toString())) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_SIM_ENABLE_DATE)});
							throw new CommonException(msg);
						}
					}
				}
				//核檢驗收日期
				if (checkedDate != null) {
					if (!ValidateUtils.length(checkedDate.toString().substring(1, 10), 1, 10)) {
						date = DateTimeUtils.parseDate(checkedDate.toString(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
						if (!ValidateUtils.checkDate(checkedDate.toString())) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_CHECKED_DATE)});
							throw new CommonException(msg);
						}
					}
				}
				//核檢原廠保固日期
				if (factoryWarrantyDate != null) {
					if (!ValidateUtils.length(factoryWarrantyDate.toString().substring(1, 10), 1, 10)) {
						date = DateTimeUtils.parseDate(factoryWarrantyDate.toString(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
						if (!ValidateUtils.checkDate(factoryWarrantyDate.toString())) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_FACTORY_WARRANTY_DATE)});
							throw new CommonException(msg);
						}
					}
				}
				//核檢客戶保固日期
				if (customerWarrantyDate != null) {
					if (!ValidateUtils.length(customerWarrantyDate.toString().substring(1, 10), 1, 10)) {
						date = DateTimeUtils.parseDate(customerWarrantyDate.toString(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
						if (!ValidateUtils.checkDate(customerWarrantyDate.toString())) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_CUSTOMER_WARRANTY_DATE)});
							throw new CommonException(msg);
						}
					}
				}
			} catch (Exception e) {
				if(e instanceof CommonException){
					throw new CommonException(((CommonException) e).getErrorMessage());
				} else {
					return false;
				}
			}*/
			//核檢TID
			if (StringUtils.hasText(tid)) {
				if (!ValidateUtils.length(tid, 1, 8)) {
                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_TID),
                            IAtomsConstants.MAXLENGTH_NUMBER_EIGHT});
                    throw new CommonException(msg);
                } else if (!ValidateUtils.numberOrEnglish(tid)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_LIMIT_NUMBER, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_TID)});
					throw new CommonException(msg);
				}
			}
			//核檢DTID 
			if (StringUtils.hasText(dtid)) {
				if (!ValidateUtils.length(dtid, 1, 8)) {
                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DTID),
                            IAtomsConstants.MAXLENGTH_NUMBER_EIGHT});
                    throw new CommonException(msg);
                } else if (!ValidateUtils.numberOrEnglish(dtid)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_LIMIT_NUMBER, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DTID)});
					throw new CommonException(msg);
				} else {//Task #3069
					/*try {
						SessionContext sessionContext = new SessionContext();
						AssetManageFormDTO assetManageFormDTO = new AssetManageFormDTO();
						DmmRepositoryDTO repositoryDTO = new DmmRepositoryDTO();
						repositoryDTO.setDtid(dtid);
						repositoryDTO.setAssetId(dmmRepositoryDTO.getAssetId());
						assetManageFormDTO.setDmmRepositoryDTO(repositoryDTO);
						sessionContext.setRequestParameter(assetManageFormDTO);
						SessionContext ctx = this.getServiceLocator().doService(logonUser, this.getServiceId(), IAtomsConstants.ACTION_CHECK_DTID, command);
						Message message = ctx.getReturnMessage();
						if (!message.isSuccess()) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_VALUE_REPEAT, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DTID)});
							throw new CommonException(msg);
						}
					} catch (Exception e) {
						if(e instanceof CommonException){
							throw new CommonException(((CommonException) e).getErrorMessage());
						} else {
							return false;
						}
					}*/
				}
			}
			
			//核檢說明/排除方式 
			if (StringUtils.hasText(repairComment)) {
				if (!ValidateUtils.length(repairComment, 1, 200)) {
                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_REPAIR_COMMENT),
                            IAtomsConstants.DESCRIPTION_LENGTH});
                    throw new CommonException(msg);
                }
			}
		}
		if (IAtomsConstants.ACTION_EDIT.equals(actionId)) {
			//領用作業
			if (StringUtils.pathEquals(command.getEditFlag(), AssetManageFormDTO.ACTION_CARRY)) {
				String carryAccount = dmmRepositoryDTO.getCarryAccount();
				if (StringUtils.hasText(carryAccount)) {
					if (!ValidateUtils.length(carryAccount, 1, 50)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_CARRY_ACCOUNT),
	                            IAtomsConstants.CONTACT_USER_LENGTH});
	                    throw new CommonException(msg);
	                }
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_CARRY_ACCOUNT)});
					throw new CommonException(msg);
				}
				String description = dmmRepositoryDTO.getDescription();
				if (StringUtils.hasText(description)) {
					if (!ValidateUtils.length(carryAccount, 1, 200)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DESCRIPTION),
	                            IAtomsConstants.DESCRIPTION_LENGTH});
	                    throw new CommonException(msg);
	                }
				}
			} 
			//借用作業
			if (StringUtils.pathEquals(command.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
				//Timestamp borrowerEnd = dmmRepositoryDTO.getBorrowerEnd();
				String borrower = dmmRepositoryDTO.getBorrower();
				if (StringUtils.hasText(borrower)) {
					if (!ValidateUtils.length(borrower, 1, 50)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_BORROWER),
	                            IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
	                    throw new CommonException(msg);
	                }
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_BORROWER)});
					throw new CommonException(msg);
				}
				String borrowerEmail = dmmRepositoryDTO.getBorrowerEmail();
				
				if (StringUtils.hasText(borrowerEmail)) {
					if (!ValidateUtils.length(borrowerEmail, 1, 50)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_BORROWER_EMAIL),
	                            IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
	                    throw new CommonException(msg);
	                }
					if (!ValidateUtils.email(borrowerEmail)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.FORMAT_ERROR,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_BORROWER_EMAIL),
	                            IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
	                    throw new CommonException(msg);
	                }
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_BORROWER_EMAIL)});
					throw new CommonException(msg);
				}
				String borrowerMgrEmail= dmmRepositoryDTO.getBorrowerMgrEmail();
				if (StringUtils.hasText(borrowerMgrEmail)) {
					if (!ValidateUtils.length(borrowerMgrEmail, 1, 50)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_BORROWER_MGR_EMAIL),
	                            IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
	                    throw new CommonException(msg);
	                }
					if (!ValidateUtils.email(borrowerEmail)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.FORMAT_ERROR,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_BORROWER_MGR_EMAIL),
	                            IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
	                    throw new CommonException(msg);
	                }
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_BORROWER_MGR_EMAIL)});
					throw new CommonException(msg);
				}
				String borrowerComment = dmmRepositoryDTO.getBorrowerComment();
				if (StringUtils.hasText(borrowerComment)) {
					if (!ValidateUtils.length(borrowerComment, 1, 200)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_BORROWER_COMMENT),
	                            IAtomsConstants.DESCRIPTION_LENGTH});
	                    throw new CommonException(msg);
	                }
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_BORROWER_COMMENT)});
					throw new CommonException(msg);
				}
			}
			//歸還作業
			if (StringUtils.pathEquals(command.getEditFlag(), AssetManageFormDTO.ACTION_BACK)) {
				String description = dmmRepositoryDTO.getDescription();
				if (StringUtils.hasText(description)) {
					if (!ValidateUtils.length(description, 1, 200)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DESCRIPTION),
	                            IAtomsConstants.DESCRIPTION_LENGTH});
	                    throw new CommonException(msg);
	                }
				}
			}
			//入庫作業
			if (StringUtils.pathEquals(command.getEditFlag(), AssetManageFormDTO.ACTION_ASSET_IN)) {
				String description = dmmRepositoryDTO.getDescription();
				if (StringUtils.hasText(description)) {
					if (!ValidateUtils.length(description, 1, 200)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DESCRIPTION),
	                            IAtomsConstants.DESCRIPTION_LENGTH});
	                    throw new CommonException(msg);
	                }
				}
			}
			//維修作業
			if (StringUtils.pathEquals(command.getEditFlag(), AssetManageFormDTO.ACTION_REPAIR)) {
				String faultComponentId = dmmRepositoryDTO.getFaultComponentId();
				String repairVendorId = dmmRepositoryDTO.getRepairVendorId();
				String faultDescriptionId = dmmRepositoryDTO.getFaultDescriptionId();
				String description = dmmRepositoryDTO.getDescription();
				if (!StringUtils.hasText(faultComponentId)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_FAULT_COMPONENT)});
					throw new CommonException(msg);
				}
				if (!StringUtils.hasText(repairVendorId)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_REPAIR_VENDOR)});
					throw new CommonException(msg);
				}
				if (!StringUtils.hasText(faultDescriptionId)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_FAULT_DESCRIPTION)});
					throw new CommonException(msg);
				}
				if (StringUtils.hasText(description)) {
					if (!ValidateUtils.length(description, 1, 200)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DESCRIPTION),
	                            IAtomsConstants.DESCRIPTION_LENGTH});
	                    throw new CommonException(msg);
	                }
				}
			}
			//送修作業
			if (StringUtils.pathEquals(command.getEditFlag(), AssetManageFormDTO.ACTION_REPAIRED)) {
				String repairVendorId = dmmRepositoryDTO.getRepairVendorId();
				String description = dmmRepositoryDTO.getDescription();
				if (!StringUtils.hasText(repairVendorId)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_REPAIR_COMPANY)});
					throw new CommonException(msg);
				}
				if (StringUtils.hasText(description)) {
					if (!ValidateUtils.length(description, 1, 200)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DESCRIPTION),
	                            IAtomsConstants.DESCRIPTION_LENGTH});
	                    throw new CommonException(msg);
	                }
				}
			}
			//待報廢作業
			if (StringUtils.pathEquals(command.getEditFlag(), AssetManageFormDTO.ACTION_RETIRE)) {
				String retireComment= dmmRepositoryDTO.getRetireComment();
				String retireReasonCode = dmmRepositoryDTO.getRetireReasonCode();
				if (!StringUtils.hasText(retireReasonCode)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_RETIRE_REASON_CODE)});
					throw new CommonException(msg);
				}
				if (StringUtils.hasText(retireComment)) {
					if (!ValidateUtils.length(retireComment, 1, 200)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DESCRIPTION),
	                            IAtomsConstants.DESCRIPTION_LENGTH});
	                    throw new CommonException(msg);
	                }
				}
			}
			//報廢作業
			if (StringUtils.pathEquals(command.getEditFlag(), AssetManageFormDTO.ACTION_RETIRED)) {
				String retireComment = dmmRepositoryDTO.getRetireComment();
				if (StringUtils.hasText(retireComment)) {
					if (!ValidateUtils.length(retireComment, 1, 200)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DESCRIPTION),
	                            IAtomsConstants.DESCRIPTION_LENGTH});
	                    throw new CommonException(msg);
	                }
				}
			}
			//退回作業
			if (StringUtils.pathEquals(command.getEditFlag(), AssetManageFormDTO.ACTION_RETURN)) {
				String description = dmmRepositoryDTO.getDescription();
				if (StringUtils.hasText(description)) {
					if (!ValidateUtils.length(description, 1, 200)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DESCRIPTION),
	                            IAtomsConstants.DESCRIPTION_LENGTH});
	                    throw new CommonException(msg);
	                }
				}
			}
			//銷毀作業
			if (StringUtils.pathEquals(command.getEditFlag(), AssetManageFormDTO.ACTION_DELETE)) {
				String description = dmmRepositoryDTO.getDescription();
				if (StringUtils.hasText(description)) {
					if (!ValidateUtils.length(description, 1, 200)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DESCRIPTION),
	                            IAtomsConstants.DESCRIPTION_LENGTH});
	                    throw new CommonException(msg);
	                }
				}
			}
			//解除綁定
			if (AssetManageFormDTO.ACTION_REMOVE.equals(command.getEditFlag())) {
				String status = command.getStatus();
				if (!StringUtils.hasText(status)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_STATUS)});
					throw new CommonException(msg);
				}
				String description = dmmRepositoryDTO.getDescription();	
				if (StringUtils.hasText(description)) {
					if (!ValidateUtils.length(description, 1, 200)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DESCRIPTION),
	                            IAtomsConstants.DESCRIPTION_LENGTH});
	                    throw new CommonException(msg);
	                }
				}
			}
			//台新租賃
			if (StringUtils.pathEquals(command.getEditFlag(), AssetManageFormDTO.ACTION_TAIXIN_RENT)) {
				String dtid = dmmRepositoryDTO.getDtid();
				//dtid
				if (!StringUtils.hasText(dtid)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DTID)});
					throw new CommonException(msg);
				}
				if (StringUtils.hasText(dtid)) {
					if (!ValidateUtils.length(dtid, 1, 8)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DTID),
	                            IAtomsConstants.MAXLENGTH_NUMBER_EIGHT});
	                    throw new CommonException(msg);
	                }
				}
				//案件編號
				String caseId = dmmRepositoryDTO.getCaseId();
				if (!StringUtils.hasText(caseId)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_CASE_ID)});
					throw new CommonException(msg);
				}
				if (StringUtils.hasText(caseId)) {
					if (!ValidateUtils.length(caseId, 1, 15)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_CASE_ID),
	                            IAtomsConstants.MAXLENGTH_NUMBER_FIFTEEN});
	                    throw new CommonException(msg);
	                }
				}
				//特店代號//没用
				/*String merchantId = dmmRepositoryDTO.getMerchantId();
				if (!StringUtils.hasText(merchantId)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_MERCHANT_CODE)});
					throw new CommonException(msg);
				}
				if (StringUtils.hasText(merchantId)) {
					if (!ValidateUtils.length(merchantId, 1, 20)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_MERCHANT_CODE),
	                            IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
	                    throw new CommonException(msg);
	                }
				}*/
				//表頭（同對外名稱）
				String merchantHeaderId = dmmRepositoryDTO.getMerchantHeaderId();
				if (!StringUtils.hasText(merchantHeaderId)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_MERCHANT_HEADER_ID)});
					throw new CommonException(msg);
				}
				//維護廠商
				String repairVendor = dmmRepositoryDTO.getMaintainCompany();
				if (!StringUtils.hasText(repairVendor)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_REPAIR_VENDOR)});
					throw new CommonException(msg);
				}
				//裝機地址-縣市
				String installedAdress = dmmRepositoryDTO.getInstalledAdress();
				if (!StringUtils.hasText(installedAdress)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_INSTALLED_ADRESS)});
					throw new CommonException(msg);
				}
				if (StringUtils.hasText(installedAdress)) {
					if (!ValidateUtils.length(installedAdress, 1, 100)) {
	                    msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
	                            new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_INSTALLED_ADRESS),
	                            IAtomsConstants.MAXLENGTH_NUMBER_ONE_HUNDRED});
	                    throw new CommonException(msg);
	                }
				}
				//裝機地址
				String installedAdressLocation = dmmRepositoryDTO.getInstalledAdressLocation();
				if (!StringUtils.hasText(installedAdressLocation)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_INSTALLED_ADRESS_LOCATION)});
					throw new CommonException(msg);
				}
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.debug("validate", " end ", (endTime - startTime));
		return true;
	}

	/** (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public AssetManageFormDTO parse(HttpServletRequest request,
			AssetManageFormDTO command) throws CommonException {
		try {
			long startTime = System.currentTimeMillis();
			if (command == null) {
				command = new AssetManageFormDTO();
			}
			// 获取actionId
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			//String imageName = this.getString(request, "imageName");
			//command.setImageName(imageName);
			IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
			String userId = null; 
			//獲取用戶ID
			if (logonUser != null) {
				userId = logonUser.getId();
				command.setUserId(userId);
			} else {
				throw new CommonException(IAtomsMessageCode.LIMITED_LOGON_ID);
			}
			if (!StringUtils.hasText(actionId)) {
				actionId = IAtomsConstants.ACTION_INIT;
			}
			command.setActionId(actionId);
			if (IAtomsConstants.ACTION_QUERY.equals(actionId) || IAtomsConstants.ACTION_HISTORY.equals(actionId) || IAtomsConstants.ACTION_SEND_QUERY_MAIL.equals(actionId)) {
				//查詢條件	
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (StringUtils.hasText(sort)) {
					//sort = DmmRepositoryDTO.getATTRIBUTENameByValue(sort);
					command.setSort(sort);
				} else {
					if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
						command.setSort(AssetManageFormDTO.PARAM_PAGE_SORT);
					} else {
						command.setSort(null);
					}
				}
				
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);			
				if (StringUtils.hasText(order)) {
					command.setOrder(order);
				} else {
					if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
						command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
					} else {
						command.setOrder(null);
					}
				}
			}
			if (IAtomsConstants.ACTION_SEND_QUERY_MAIL.equals(actionId)) {
				//CR #2563 查詢出來的資料，可以以附件方式，寄給 通知人員(比照轉倉) update by 2017/10/10
				command.setFieldMap(this.getExportField());
				String exportField = command.getExportField();
				String[] exportFields = null;
				if(StringUtils.hasText(exportField)){
					exportFields = exportField.split(IAtomsConstants.MARK_SEPARATOR);
					command.setExportFields(exportFields);
				}
			}
			if ((IAtomsConstants.ACTION_UPDATE.equals(actionId)) 
					|| (IAtomsConstants.ACTION_EDIT.equals(actionId)) 
					|| (IAtomsConstants.ACTION_CHECK.equals(actionId)) 
					|| (IAtomsConstants.ACTION_EXPORT_ASSET.equals(actionId)) 
					|| IAtomsConstants.ACTION_EXPORT.equals(actionId) 
					|| (IAtomsConstants.ACTION_DOWNLOAD_ZIP.equals(actionId)) 
					|| (IAtomsConstants.ACTION_CHECK_DTID.equals(actionId))) {
				String historyExport = this.getString(request, AssetManageFormDTO.HISTORY_PARAM_EXPORT);
				if (IAtomsConstants.ACTION_EXPORT.equals(actionId) && StringUtils.pathEquals(historyExport, IAtomsConstants.PARAM_REPOSITORY_STATUS_IN_STORAGE)) {
					command.setSort(null);
					command.setOrder(null);
				} else {
					command.setSort(AssetManageFormDTO.PARAM_PAGE_SORT);
					command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
				}
				String exportField = this.getString(request, AssetManageFormDTO.EXPORT_PARAM_FIELDS);
				String[] exportFields = null;
				if(StringUtils.hasText(exportField)){
					exportFields = exportField.split(IAtomsConstants.MARK_SEPARATOR);
					command.setExportFields(exportFields);
				}
				DmmRepositoryDTO dmmRepositoryDTO = command.getDmmRepositoryDTO();
				dmmRepositoryDTO = BindPageDataUtils.bindValueObject(request, DmmRepositoryDTO.class);
				command.setDmmRepositoryDTO(dmmRepositoryDTO);
			} 
			if ((IAtomsConstants.ACTION_EDIT.equals(actionId))) {
				//查詢條件	
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (StringUtils.hasText(sort)) {
					//sort = DmmRepositoryDTO.getATTRIBUTENameByValue(sort);
					command.setSort(sort);
				} else {
					if (IAtomsConstants.ACTION_EDIT.equals(actionId)) {
						command.setSort("r.SERIAL_NUMBER");
					} else {
						command.setSort(null);
					}
				}
				
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);			
				if (StringUtils.hasText(order)) {
					command.setOrder(order);
				} else {
					if (IAtomsConstants.ACTION_EDIT.equals(actionId)) {
						command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
					} else {
						command.setOrder(null);
					}
				}
			}
			if (IAtomsConstants.ACTION_INIT_MID.equals(actionId)) {
				MerchantFormDTO merchantFormDTO = BindPageDataUtils.bindValueObject(request, MerchantFormDTO.class);
				CompanyDTO companyDTO = new CompanyDTO();
				companyDTO = BindPageDataUtils.bindValueObject(request, CompanyDTO.class);
				merchantFormDTO.setCompanyDTO(companyDTO);
				command.setMerchantFormDTO(merchantFormDTO);
			}
			if ("getCountByAsset".equals(actionId)) {
				String queryParam = this.getString(request, AssetManageFormDTO.QUERY_PARAM);
				Gson gsonss = new GsonBuilder().create();
				command = (AssetManageFormDTO) gsonss.fromJson(queryParam, new TypeToken<AssetManageFormDTO>(){}.getType());
			}
			if ("getAssetIdList".equals(actionId)) {
				String queryParam = this.getString(request, AssetManageFormDTO.QUERY_PARAM);
				Gson gsonss = new GsonBuilder().create();
				command = (AssetManageFormDTO) gsonss.fromJson(queryParam, new TypeToken<AssetManageFormDTO>(){}.getType());
			}
			long endTime = System.currentTimeMillis();
			LOGGER.debug("parse", " end ", (endTime - startTime));
			return command;
		} catch (Exception e) {
			LOGGER.error("Exception----parse()", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
	}
	
	/**
	 * Purpose: 下載壓縮報表
	 * @author amandawang
	 * @param request 請求對象
	 * @param response 響應對象
	 * @param command formDTO
	 * @throws CommonException 上下文拋出異常
	 * @return void  
	 */
	@SuppressWarnings({ "rawtypes"})
	public ModelAndView downloadZip(HttpServletRequest request, HttpServletResponse response, AssetManageFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		//FileInputStream fileInputStream = null;
		//OutputStream outputStream = null;
		String tempPath = null;
		//String yearMonthDay = null;
		String fileName = null;
		List<String> fileList = null;
		//設備借用申請單.zip
		String zipName = i18NUtil.getName(IAtomsConstants.ASSET_BORROW_ZIP_NAME) + JasperReportCriteriaDTO.REPORT_EXT_NAME_ZIP;
		InputStream fileInputStream = null;
		OutputStream outputStream = null;
		File downLoadFile = null;
		//臨時文件夾下的DMM03060下的隨機數生成的文件夾
		File file = null;
		try{
			IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
			this.getServiceLocator().doService(logonUser, this.getServiceId(), IAtomsConstants.ACTION_SAVE_REPOSITORY_HIST, command);
			SessionContext ctx = this.getServiceLocator().doService(logonUser, this.getServiceId(), IAtomsConstants.ACTION_QUERY, command);
			command = (AssetManageFormDTO) ctx.getResponseResult();
			if(command != null) {
				List<DmmRepositoryDTO> dmmRepositoryDTOs = command.getList();
				// 定义一个map集合用于分组
				Map<String, List<DmmRepositoryDTO>> repositoryMap = new HashMap<String, List<DmmRepositoryDTO>>();
				// 遍历集合以borrower为键，以list为值保存到map中
				for (DmmRepositoryDTO dmmRepositoryTempDTO : dmmRepositoryDTOs) {
					//為防止舊資料轉入借用人為空時列印借用單異常 Task #2566 update by 2017/09/30
					if (!cafe.core.util.StringUtils.hasText(dmmRepositoryTempDTO.getBorrower())) {
						dmmRepositoryTempDTO.setBorrower(IAtomsConstants.MARK_EMPTY_STRING);
					}
					// 取消借用說明欄位，統一存入Description
					dmmRepositoryTempDTO.setBorrowerComment(dmmRepositoryTempDTO.getDescription());
					// 如果在这个map中包含有相同的键，将其存起来
					if (repositoryMap.containsKey(dmmRepositoryTempDTO.getBorrower())) {
						// List<DmmRepositoryDTO> dmmRepositoryDTOList = repositoryMap.get(dmmRepositoryDTO.getBorrower());
						repositoryMap.get(dmmRepositoryTempDTO.getBorrower()).add(dmmRepositoryTempDTO);
						// 如果没有包含相同的键，再创建一个集合保存数据
					} else {
						List<DmmRepositoryDTO> newDmmRepositoryDTOs = new ArrayList<DmmRepositoryDTO>();
						newDmmRepositoryDTOs.add(dmmRepositoryTempDTO);
						repositoryMap.put(dmmRepositoryTempDTO.getBorrower(), newDmmRepositoryDTOs);
					}
				}
				Set<String> set = repositoryMap.keySet();
				List<DmmRepositoryDTO> repositoryDTOList = null;
				Map<String, List<DmmRepositoryDTO>> dmmRepositoryMap = new HashMap<String, List<DmmRepositoryDTO>>();
				for (String string : set) {
					for (DmmRepositoryDTO repositoryDTO : repositoryMap.get(string)) {
						//為防止舊資料轉入借用日期為空時列印借用單異常  update by 2017/09/30 
						if (dmmRepositoryMap.containsKey(string + (repositoryDTO.getBorrowerStart() != null ? new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).format(repositoryDTO.getBorrowerStart()): IAtomsConstants.MARK_EMPTY_STRING) + (repositoryDTO.getBorrowerEnd() != null ? new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).format(repositoryDTO.getBorrowerEnd()) : IAtomsConstants.MARK_EMPTY_STRING))) {
							dmmRepositoryMap.get(string + (repositoryDTO.getBorrowerStart() != null ? new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).format(repositoryDTO.getBorrowerStart()) : IAtomsConstants.MARK_EMPTY_STRING) + (repositoryDTO.getBorrowerEnd() != null ? new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).format(repositoryDTO.getBorrowerEnd()) : IAtomsConstants.MARK_EMPTY_STRING)).add(repositoryDTO);
						} else {
							repositoryDTOList = new ArrayList<DmmRepositoryDTO>();
							repositoryDTOList.add(repositoryDTO);
							dmmRepositoryMap.put(string + (repositoryDTO.getBorrowerStart() != null ? new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).format(repositoryDTO.getBorrowerStart()) : IAtomsConstants.MARK_EMPTY_STRING) + (repositoryDTO.getBorrowerEnd() != null ? new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).format(repositoryDTO.getBorrowerEnd()) : IAtomsConstants.MARK_EMPTY_STRING), repositoryDTOList);
						}
					}
				}
				Map<String, String> docMap = new HashMap<String, String>();
				StringBuffer stringBuffer = null;
				//待壓縮文檔集合
				fileList = new ArrayList<String>();
				String ucNo = this.getUseCaseNo();
				String uuid = UUID.randomUUID().toString();
				//臨時文件夾
				tempPath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH) + File.separator + ucNo + File.separator + uuid;
				file = new File(tempPath);
				if (!file.exists() || !file.isDirectory()) {
					file.mkdirs();
				}
				File docFile = null;
				for (Map.Entry<String, List<DmmRepositoryDTO>> dmmEntry : dmmRepositoryMap.entrySet()) {
					List<DmmRepositoryDTO> repositoryDTOs = dmmEntry.getValue();
					if (!CollectionUtils.isEmpty(repositoryDTOs)) {
						String docName = null;
						if (repositoryDTOs.size()<=100) {
							docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME;
						} else if (repositoryDTOs.size()<=200) {
							docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_2";
						} else if (repositoryDTOs.size()<=300) {
							docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_3";
						} else if (repositoryDTOs.size()<=400) {
							docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_4";
						} else if (repositoryDTOs.size()<=500) {
							docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_5";
						} else if (repositoryDTOs.size()<=600) {
							docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_6";
						} else if (repositoryDTOs.size()<=700) {
							docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_7";
						} else if (repositoryDTOs.size()<=800) {
							docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_8";
						} else if (repositoryDTOs.size()<=900) {
							docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_9";
						} else {
							docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_all";
						}
						LOGGER.debug("downloadZip docName= " + docName);
						docMap = new HashMap<String, String>();
						//設置map鍵值對
						String borrowerStart = (repositoryDTOs.get(0).getBorrowerStart() != null ? new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).format(repositoryDTOs.get(0).getBorrowerStart()) : IAtomsConstants.MARK_EMPTY_STRING);
    					String borrowEnd = (repositoryDTOs.get(0).getBorrowerEnd() != null ? new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).format(repositoryDTOs.get(0).getBorrowerEnd()) : IAtomsConstants.MARK_EMPTY_STRING);
    					stringBuffer = new StringBuffer();
						stringBuffer.append(IAtomsConstants.MARK_DOLLAR)
								.append(IAtomsConstants.MARK_BRACES_LEFT)
								.append(DmmRepositoryDTO.ATTRIBUTE.BORROWERS.getValue())
								.append(IAtomsConstants.MARK_BRACES_RIGHT);
    					docMap.put(stringBuffer.toString(), repositoryDTOs.get(0).getBorrower());
    					stringBuffer = new StringBuffer();
						stringBuffer.append(IAtomsConstants.MARK_DOLLAR)
								.append(IAtomsConstants.MARK_BRACES_LEFT)
								.append(DmmRepositoryDTO.ATTRIBUTE.BORROWER_END.getValue())
								.append(IAtomsConstants.MARK_BRACES_RIGHT);
    					docMap.put(stringBuffer.toString(), borrowEnd);
    					stringBuffer = new StringBuffer();
						stringBuffer.append(IAtomsConstants.MARK_DOLLAR)
								.append(IAtomsConstants.MARK_BRACES_LEFT)
								.append(DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue())
								.append(IAtomsConstants.MARK_BRACES_RIGHT);
						docMap.put(stringBuffer.toString(), borrowerStart);
						borrowerStart = StringUtils.replace(borrowerStart, IAtomsConstants.MARK_BACKSLASH, IAtomsConstants.MARK_EMPTY_STRING);
	    				borrowEnd = StringUtils.replace(borrowEnd, IAtomsConstants.MARK_BACKSLASH, IAtomsConstants.MARK_EMPTY_STRING);
						//模板path
						String filePath = IAtomsConstants.POI_FILE_PATH + docName + JasperReportCriteriaDTO.REPORT_EXT_NAME_MSWORD;
						//臨時文件夾
						//tempPath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH) + File.separator+ ucNo;
						if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
							zipName = URLEncoder.encode(zipName, "UTF-8");  
							fileName = AssetManageFormDTO.ZIP_FILE_NAME + IAtomsConstants.MARK_BRACKETS_LEFT + repositoryDTOs.get(0).getBorrower() + borrowerStart + IAtomsConstants.MARK_MIDDLE_LINE + borrowEnd + IAtomsConstants.MARK_BRACKETS_RIGHT + JasperReportCriteriaDTO.REPORT_EXT_NAME_MSWORD;
						} else {  
							fileName = AssetManageFormDTO.ZIP_FILE_NAME + IAtomsConstants.MARK_BRACKETS_LEFT + repositoryDTOs.get(0).getBorrower() + borrowerStart + IAtomsConstants.MARK_MIDDLE_LINE + borrowEnd + IAtomsConstants.MARK_BRACKETS_RIGHT + JasperReportCriteriaDTO.REPORT_EXT_NAME_MSWORD;
						}
						FileUtils.copyFileByInputStream(filePath, tempPath, File.separator + fileName);
						LOGGER.debug("FileUtils.copyFileByInputStream() filePath= " + filePath);
						LOGGER.debug("FileUtils.copyFileByInputStream() tempPath= " + tempPath);
						LOGGER.debug("FileUtils.copyFileByInputStream() fileName= " + fileName);
						LOGGER.debug("POIUtils.dealDoc() docMap.keySet= " + docMap.keySet());
						LOGGER.debug("POIUtils.dealDoc() repositoryDTOs= " + repositoryDTOs.size());
						POIUtils.dealDoc(docMap, repositoryDTOs, File.separator + fileName, tempPath);
						docFile = new File(tempPath + File.separator + fileName);
						if (docFile.exists() && docFile.isFile()) {
							fileList.add(tempPath + File.separator + fileName);
						}
	    			}
				}
				//如果目錄存在
				if (file.isDirectory()) {  
					FileUtils.compressByMs950(tempPath + File.separator,  zipName, fileList);
					//下載信息文件
					downLoadFile = new File(tempPath + File.separator + zipName);
					if (downLoadFile.exists()){
						FileUtils.download(request, response, tempPath + File.separator + zipName, zipName);
						// 成功標志
						SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
					} else {
						LOGGER.debug(".downloadZip().downLoadFile is error ==== " + downLoadFile);
						SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
						Map map = new HashMap();
						map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
						map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
						return new ModelAndView(this.getFailureView("downloadZip"), map);
					}
					/*if (downLoadFile.exists() && downLoadFile.isFile()) {
						fileInputStream = new FileInputStream(downLoadFile);
						if (request.getHeader("User-Agent").indexOf("Trident") >= 0) {
							fileName = URLEncoder.encode(zipName, "UTF-8");
							fileName = StringUtils.replace(fileName, "+", "%20");
						} else {
							fileName = new String(zipName.getBytes("UTF-8"), "ISO8859-1");
						}
						outputStream = response.getOutputStream();// 获取文件输出IO流
						response.reset();
						response.setContentType("application/x-download");// 设置response内容的类型
						response.setHeader("Content-disposition", "attachment;filename=" + fileName);// 设置头部信息
						// response.setContentType("application/vnd.ms-excel;charset=UTF-8");
						byte[] buffer = new byte[1024];
						// 开始向网络传输文件流
						int len = -1;
						do {
							len = fileInputStream.read(buffer);
							if (len == -1) {
								continue;
							}
							outputStream.write(buffer, 0, len);
						} while (len != -1);
						outputStream.write(buffer);
					} else {
						LOGGER.error("FileUtils downFile file is null !!!!");
					}*/
				}
			}
		} catch(Exception e) {
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView("downloadZip"), map);
			} catch (Exception e1) {
				LOGGER.error(".downloadZip() is error:", e1);
			}
			LOGGER.error("downloadZip() is error: " + e, e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		} finally {
			try {
				if(fileInputStream != null) {
					fileInputStream.close();
				}
				if(outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
				if(file!=null && file.isDirectory()){
					FileUtils.removeFile(tempPath);
				}
			} catch (Exception e) {
				LOGGER.error("FileUtils.removeFile() is error: " + e, e);
				throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
			}

		}
		return null;
	}

	/**
	 * Purpose: 匯出
	 * @author amandawang
	 * @param request: 請求
	 * @param response: 響應
	 * @param command: AssetTransInfoFormDTO
	 * @throws CommonException：出錯時拋出CommonException
	 * @return void
	 */
	@SuppressWarnings("rawtypes")
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, AssetManageFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try{
			IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
			String[] exportFileds = command.getExportFields();
			String historyExport = command.getHistoryExport();
			SessionContext returnCtx = null;
			//JasperReport條件設定DTO
			JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
			List<CrossTabReportDTO> crossTabReportDTOList = new ArrayList<CrossTabReportDTO>();
			// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
			criteria.setAutoBuildJasper(false);
			//报表模板名称
			String jrxmlName =AssetManageFormDTO.EXPORT_JRXML_NAME_LIST;
			//汇出后的xsl名称
			String reportFileName = AssetManageFormDTO.REPORT_FILE_NAME;
			//掃碼槍后匯出
			if (DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue().equals(command.getCodeGunFlag())){
				if (!cafe.core.util.StringUtils.hasText(command.getQuerySerialNumbers())) {
					command.setQuerySerialNumbers(command.getExportCodeGunSerialNumbers());
					command.setTotalSize(null);
				}
			}
			if (DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue().equals(command.getCodeGunFlag())){
				if (!cafe.core.util.StringUtils.hasText(command.getQueryPropertyIds())) {
					command.setQueryPropertyIds(command.getExportCodeGunPropertyIds());
					command.setTotalSize(null);
				}
			}
			//歷史資料查詢
			if (StringUtils.pathEquals(historyExport, IAtomsConstants.PARAM_REPOSITORY_STATUS_IN_STORAGE)) {
				//根据条件查询报表数据
				returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_REPOSITORY_SERVICE, IAtomsConstants.ACTION_HISTORY, command);
				if(returnCtx != null){
					//查询结果
					command = (AssetManageFormDTO) returnCtx.getResponseResult();
					List<DmmRepositoryHistoryDTO> results = command.getRepositoryHistDTOs();
					if (!CollectionUtils.isEmpty(results)) {
						crossTabReportDTOList = getCrossTabReportList(results, null, exportFileds);
					}
				}
			} else {
				//根据条件查询报表数据
				returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_REPOSITORY_SERVICE, IAtomsConstants.ACTION_QUERY, command);
				if(returnCtx != null){
					//查询结果
					command = (AssetManageFormDTO) returnCtx.getResponseResult();
					List<DmmRepositoryDTO> results = command.getList();
					if (!CollectionUtils.isEmpty(results)) {
						crossTabReportDTOList = getCrossTabReportList(null, results, exportFileds);
					}
				}
			}
			criteria.setResult(crossTabReportDTOList);
			//設置所需報表的Name
			criteria.setJrxmlName(jrxmlName);
			//設置報表路徑
			criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
			//設置匯出格式
			criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
			//設置報表Name
			criteria.setReportFileName(reportFileName);
			criteria.setSheetName(reportFileName);
			ReportExporter.exportReport(criteria, response);
			// 成功標志
			SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
		} catch(Exception e) {
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView(IAtomsConstants.ACTION_EXPORT), map);
			} catch (Exception e1) {
				LOGGER.error(".export() is error:", e1);
			}
			LOGGER.error("AssetManageController export() is error: " + e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE);
		}
		return null;
	}
	/**
	 * Purpose:獲取交叉報表所需集合
	 * @author amandawang
	 * @param repositoryHistoryDTOs：庫存歷史集合
	 * @param repositoryDTOs：庫存集合
	 * @param exportFileds：需要的字段
	 * @throws CommonException
	 * @return List<CrossTabReportDTO>：交叉報表集合
	 */
	public List<CrossTabReportDTO> getCrossTabReportList(List<DmmRepositoryHistoryDTO> repositoryHistoryDTOs, List<DmmRepositoryDTO> repositoryDTOs, String[] exportFileds) throws CommonException {
		int size = 0;
		//庫存歷史list不為空則匯出庫存歷史檔資料
		if (!CollectionUtils.isEmpty(repositoryHistoryDTOs)) {
			size = repositoryHistoryDTOs.size();
		} else {
			//反之，匯出庫存檔資料
			size = repositoryDTOs.size();
		}
		String columnName = "";
		String content = "";
		CrossTabReportDTO crossTabDTO = null;
		List<CrossTabReportDTO> crossTabReportDTOList = new ArrayList<CrossTabReportDTO>();
		try {
			//日期格式需要轉換
			for(int i=0; i < size; i++){
				for(int j=0; j < exportFileds.length; j++){
					columnName = exportFileds[j];
					if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.BORROWER_END.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.BORROWER_END.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.BACK_DATE.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.BACK_DATE.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.CASE_COMPLETION_DATE.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.CASE_COMPLETION_DATE.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_TIME.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_TIME.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_CONFIRM.getValue())) {
						content = "";
					} else {
						content = IAtomsUtils.getFiledValueByName(columnName, !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
					}
					crossTabDTO = new CrossTabReportDTO();
					crossTabDTO.setRowNo(i + 1);
					crossTabDTO.setColumnName(this.exportField.get(columnName));
					crossTabDTO.setContent(content);
					crossTabReportDTOList.add(crossTabDTO);
				}
			}
		} catch (Exception e) {
			LOGGER.error("AssetManageController export() getCrossTabReportList() is error: " + e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE);
		}
		
		return crossTabReportDTOList;
	}
	/**
	 * Purpose: 列印借用單
	 * @author amandawang
	 * @param request 請求對象
	 * @param response 響應對象
	 * @param command formDTO
	 * @throws CommonException 上下文拋出異常
	 * @return void  
	 */
	@SuppressWarnings({ "rawtypes"})
	public ModelAndView exportAsset(HttpServletRequest request, HttpServletResponse response, AssetManageFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		//設備借用申請單.doc
		String fileName = i18NUtil.getName(IAtomsConstants.ASSET_BORROW_ZIP_NAME) + JasperReportCriteriaDTO.REPORT_EXT_NAME_MSWORD;
		FileInputStream fileInputStream = null;
		OutputStream outputStream = null;
		String tempPath = null;
		String yearMonthDay = null;
		StringBuffer stringBuffer = null;
		try{
			IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
			SessionContext ctx = this.getServiceLocator().doService(logonUser, this.getServiceId(), IAtomsConstants.ACTION_QUERY, command);
			command = (AssetManageFormDTO) ctx.getResponseResult();
			DmmRepositoryDTO dmmRepositoryDTO = command.getDmmRepositoryDTO();
			List<DmmRepositoryDTO> dmmRepositoryDTOs = command.getList();
			String docName = null;
			if (dmmRepositoryDTOs.size()<=100) {
				docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME;
			} else if (dmmRepositoryDTOs.size()<=200) {
				docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_2";
			} else if (dmmRepositoryDTOs.size()<=300) {
				docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_3";
			} else if (dmmRepositoryDTOs.size()<=400) {
				docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_4";
			} else if (dmmRepositoryDTOs.size()<=500) {
				docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_5";
			} else if (dmmRepositoryDTOs.size()<=600) {
				docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_6";
			} else if (dmmRepositoryDTOs.size()<=700) {
				docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_7";
			} else if (dmmRepositoryDTOs.size()<=800) {
				docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_8";
			} else if (dmmRepositoryDTOs.size()<=900) {
				docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_9";
			} else {
				docName = IAtomsConstants.ASSET_BORROW_DOC_TEMPLATE_NAME+"_all";
			}
			LOGGER.debug("exportAsset docName= " + docName);
			Map<String, String> docMap = new HashMap<String, String>();
			if (dmmRepositoryDTO != null) {
				//設置map鍵值對
				String borrowerStart = new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).format(dmmRepositoryDTO.getBorrowerStart());
				String borrowEnd = new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).format(dmmRepositoryDTO.getBorrowerEnd());
				stringBuffer = new StringBuffer();
				stringBuffer.append(IAtomsConstants.MARK_DOLLAR)
						.append(IAtomsConstants.MARK_BRACES_LEFT)
						.append(DmmRepositoryDTO.ATTRIBUTE.BORROWERS.getValue())
						.append(IAtomsConstants.MARK_BRACES_RIGHT);
				docMap.put(stringBuffer.toString(), dmmRepositoryDTO.getBorrowerName());
				stringBuffer = new StringBuffer();
				stringBuffer.append(IAtomsConstants.MARK_DOLLAR)
						.append(IAtomsConstants.MARK_BRACES_LEFT)
						.append(DmmRepositoryDTO.ATTRIBUTE.BORROWER_END.getValue())
						.append(IAtomsConstants.MARK_BRACES_RIGHT);

				docMap.put(stringBuffer.toString(), borrowEnd);
				stringBuffer = new StringBuffer();
				stringBuffer.append(IAtomsConstants.MARK_DOLLAR)
						.append(IAtomsConstants.MARK_BRACES_LEFT)
						.append(DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue())
						.append(IAtomsConstants.MARK_BRACES_RIGHT);
				docMap.put(stringBuffer.toString(), borrowerStart);
				if (!CollectionUtils.isEmpty(dmmRepositoryDTOs)) {
					for (DmmRepositoryDTO dmmRepositoryDTO2 : dmmRepositoryDTOs) {
						// 取消借用說明欄位，統一存入Description
						dmmRepositoryDTO2.setBorrowerComment(dmmRepositoryDTO.getBorrowerComment());
					}
				}
				String filePath = IAtomsConstants.POI_FILE_PATH + docName + JasperReportCriteriaDTO.REPORT_EXT_NAME_MSWORD;
				yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				String ucNo = this.getUseCaseNo();
				String uuid = UUID.randomUUID().toString();
				//臨時文件夾
				tempPath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH) + File.separator + ucNo + File.separator + uuid;

				//臨時目錄
				//tempPath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
				stringBuffer = new StringBuffer();
				//
				stringBuffer.append(File.separator).append(IAtomsConstants.PARAM_TEMPLATE)
						.append(IAtomsConstants.MARK_UNDER_LINE)
						.append(docName)
						.append(IAtomsConstants.MARK_UNDER_LINE).append(yearMonthDay)
						.append(JasperReportCriteriaDTO.REPORT_EXT_NAME_MSWORD);
				FileUtils.copyFileByInputStream(filePath, tempPath, stringBuffer.toString());
				//列印
				POIUtils.dealDoc(docMap, dmmRepositoryDTOs,stringBuffer.toString(), tempPath);
				//下載信息文件
				File file = new File(tempPath + stringBuffer.toString());
				if (file.exists() && file.isFile()) {
					fileInputStream = new FileInputStream(file);  
					if (request.getHeader("User-Agent").indexOf("Trident") >= 0) {
						fileName = URLEncoder.encode(fileName, "UTF-8"); 
						fileName = StringUtils.replace(fileName, "+", "%20");
					    } else {  
					    	fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");  
					    } 
					outputStream = response.getOutputStream();// 获取文件输出IO流   
					response.reset();
					response.setContentType("application/x-download");// 设置response内容的类型
					response.setHeader("Content-disposition", "attachment;filename=" + fileName);// 设置头部信息
					//response.setContentType("application/vnd.ms-excel;charset=UTF-8");
					byte[] buffer = new byte[1024];
					//开始向网络传输文件流    
					int len = -1;
					do {
						len = fileInputStream.read(buffer);
						if(len == -1) {
							continue;
						}
						outputStream.write(buffer, 0, len);
					} while (len != -1);
					outputStream.write(buffer);
					// 成功標志
					SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
				} else{
					LOGGER.error("AssetManageController exportAsset file is null path=" + tempPath + stringBuffer.toString());
				}
			}
		}catch(Exception e){
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView("exportAsset"), map);
			} catch (Exception e1) {
				LOGGER.error(".exportAsset() is error:", e1);
			}
			LOGGER.error("AssetManageController exportAsset() is error: " + e, e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE);
		} finally {
			try {
				FileUtils.removeFile(tempPath);
			} catch (Exception e) {
				LOGGER.error("AssetManageController exportAsset FileUtils.removeFile()  is error", e);
				e.printStackTrace();
			}
			try {
				if(fileInputStream != null) {
					fileInputStream.close();
				}
				if(outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (Exception e) {
				LOGGER.error("AssetManageController exportAsset fileInputStream or outputStream close is error", e);
			}
		}
		return null;
	}
	
	
	
	/**
	 * Purpose:庫存歷史查詢
	 * @author amandawang
	 * @param request：HttpServletRequest
	 * @param response：HttpServletResponse
	 * @param command：庫存主檔FormDTO
	 * @throws CommonException：發生錯誤時, 丟出CommonException
	 * @return ModelAndView
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ModelAndView history(HttpServletRequest request, HttpServletResponse response,
			AssetManageFormDTO command) throws CommonException{
		try {			
			SessionContext ctx = this.getServiceLocator().doService(null, this.getServiceId(), IAtomsConstants.ACTION_HISTORY, command);
			command = (AssetManageFormDTO) ctx.getResponseResult();
			// 獲得返回信息
			Message msg = ctx.getReturnMessage();
			Map map = new HashMap();
			if (command.getTotalSize() != null) {
				Integer sizeInteger = command.getTotalSize();
				if ((null == sizeInteger) || (Integer.valueOf(0).equals(sizeInteger))) {
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, IAtomsConstants.PARAM_INIT_TOTAL);
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, IAtomsConstants.PARAM_INIT_ROWS);
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(), null));
				} else {
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, command.getTotalSize());
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, command.getRepositoryHistDTOs());
				}
			}
			
			return new ModelAndView(new MappingJacksonJsonView(), map);  
		} catch (ServiceException e) {
			LOGGER.error("history() is error" + e);
			throw new CommonException(IAtomsMessageCode.QUERY_FAILURE);
		} catch (Exception e) {
			LOGGER.error("Exception----history()", e);
			throw new CommonException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}
	
	/**
	 * Purpose:列印借用明細
	 * @author nicklin
	 * @param request：HttpServletRequest
	 * @param response：HttpServletResponse
	 * @param command：庫存主檔FormDTO
	 * @throws CommonException：發生錯誤時, 丟出CommonException
	 * @return ModelAndView
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ModelAndView exportBorrowDetail(HttpServletRequest request, HttpServletResponse response,
			AssetManageFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		List<DmmRepositoryDTO> repositoryDTOs = null;
		DmmRepositoryDTO repositoryDTO = null;
		try {
			if (command != null) {
				IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
				SessionContext ctx = this.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_REPOSITORY_SERVICE, "exportBorrowDetail", command);
				
				if (ctx != null) {
					command = (AssetManageFormDTO) ctx.getResponseResult();
					repositoryDTOs = command.getList();
				}
				if (!CollectionUtils.isEmpty(repositoryDTOs)) {
					repositoryDTO = new DmmRepositoryDTO();
					repositoryDTO.setNumber(repositoryDTOs.size());
					repositoryDTOs.add(repositoryDTO);
					for (int i = 0, count = 1; i < repositoryDTOs.size() - 1; i ++) {
						repositoryDTOs.get(i).setNumber(1);
						if (!repositoryDTOs.get(i).getBorrower().equals(repositoryDTOs.get(i + 1).getBorrower())) {
							repositoryDTO = new DmmRepositoryDTO();
							repositoryDTO.setBorrowerName(repositoryDTOs.get(i).getBorrowerName());
							repositoryDTO.setNumber(count);
							repositoryDTOs.add(i + 1, repositoryDTO);
							count = 1;
							i++;
						} else {
							count++;
						}
					}
				}
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
				criteria.setAutoBuildJasper(false);
				criteria.setResult(repositoryDTOs);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.BORROW_DETAIL_PROJECT_REPORT_JRXML_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setReportFileName(IAtomsConstants.BORROW_DETAIL);
				criteria.setSheetName(IAtomsConstants.BORROW_DETAIL);
				ReportExporter.exportReport(criteria, response);
				//成功標誌
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
			}
		} catch (ServiceException e) {
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView(IAtomsConstants.ACTION_EXPORT_BORROW_DETAIL), map);
			} catch(Exception e1) {
				LOGGER.error("exportBorrowDetail() is error" + e1);
			}
			LOGGER.error("exportBorrowDetail() is error" + e);
			throw new CommonException(IAtomsMessageCode.QUERY_FAILURE);
		} catch (Exception e) {
			LOGGER.error("Exception----exportBorrowDetail()", e);
			throw new CommonException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return null;
	}
	/**
	 * @return the exportField
	 */
	public Map<String, String> getExportField() {
		return exportField;
	}

	/**
	 * @param exportField the exportField to set
	 */
	public void setExportField(Map<String, String> exportField) {
		this.exportField = exportField;
	}
}