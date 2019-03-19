package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;

import cafe.core.bean.CompositeParameter;
import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.bean.dto.parameter.BaseParameterItemDefDTO;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.BaseParameterInquiryContext;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.criterion.Condition;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.parameter.impl.BaseParameterItemDefDAO;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.dmo.parameter.BaseParameterItemDef;
import cafe.core.util.CoreConstants;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

/**
 * Purpose:複寫基本參數檔查詢條件，iAtoms沒有版本概念，所以刪除查詢版本的sql 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2017年10月9日
 * @MaintenancePersonnel evanliu
 */
public class IAtomsBaseParameterItemDefDAO extends BaseParameterItemDefDAO {
	/**
	 * 日誌
	 */
	private static CafeLog LOG = CafeLogFactory.getLog(GenericConfigManager.DAO, IAtomsBaseParameterItemDefDAO.class);
	/**
	 * (non-Javadoc)
	 * @see cafe.core.dao.parameter.impl.BaseParameterItemDefDAO#getParameterItems(cafe.core.context.BaseParameterInquiryContext)
	 */
	@Override
	public List<? extends Parameter> getParameterItems(BaseParameterInquiryContext inquiryCtx) throws DataAccessException {
		if (inquiryCtx == null || !StringUtils.hasText(inquiryCtx.getParameterType())) {
			LOG.error("BaseParameterItemDefDAO.getParameterItems() is failed: the parameter type can't be null!");
			throw new DataAccessException(CoreMessageCode.ARGUMENT_IS_NULL, new String[]{"參數類型代碼"});
		} 
		try {
			//得到schema的名稱.
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			//sql.addSelectClause("DISTINCT");
			//sql.distinct();
			
		//	Properties columns = this.getColumnMap(inquiryCtx.getParameterType());
			// 重置查詢列
			Properties columns = this.getColumnMapSet();
			if (columns != null && !columns.isEmpty()) {
				Iterator keys = columns.keySet().iterator();
				String columnName = null;
				String fieldName = null;
				while (keys.hasNext()) {
					columnName = (String)keys.next();
					fieldName = columns.getProperty(columnName);
					sql.addSelectClause("t1."+columnName, fieldName);
				}
			}
			sql.setFromExpression(schema + ".BASE_PARAMETER_ITEM_DEF t1");
			sql.addWhereClause("t1.BPTD_CODE = :parameterType", inquiryCtx.getParameterType());
			// 未刪除
			sql.addWhereClause("isnull(t1.DELETED, 'N') = :deleted", IAtomsConstants.NO);
			
			if(StringUtils.hasText(inquiryCtx.getItemValue())){
				sql.addWhereClause("t1.ITEM_VALUE =:itemValue",inquiryCtx.getItemValue());
			}
			//sql.addWhereClause("t1.ITEM_VALUE =:itemValue",inquiryCtx.getItemValue());
			//sql.addWhereClause("t1.REFERENCE_CODE =:referenceCode",inquiryCtx.getReferenceCode());
			//sql.addWhereClause("t1.ITEM_DEPTH =:itemDepth",inquiryCtx.getItemDepth());
			if(StringUtils.hasText(inquiryCtx.getTextField1())){
				sql.addWhereClause("t1.TEXT_FIELD1 =:textField1",inquiryCtx.getTextField1());
			}
			//sql.addWhereClause("t1.TEXT_FIELD1 =:textField1",inquiryCtx.getTextField1());
			//sql.addWhereClause("t1.TEXT_FIELD2 =:textField2",inquiryCtx.getTextField2());
			//sql.addWhereClause("t1.TEXT_FIELD3 =:textField3",inquiryCtx.getTextField3());
			//sql.addWhereClause("t1.TEXT_FIELD4 =:textField4",inquiryCtx.getTextField4());
			//sql.addWhereClause("t1.TEXT_FIELD5 =:textField5",inquiryCtx.getTextField5());
			//sql.addWhereClause("t1.NUMBER_FIELD1 =:numberField1",inquiryCtx.getNumberField1());
			//sql.addWhereClause("t1.NUMBER_FIELD2 =:numberField2",inquiryCtx.getNumberField2());
			// 去除approvedFlag
	//		if (inquiryCtx.isApproved()) sql.addWhereClause("t1.APPROVED_FLAG = :approvedFlag", CoreConstants.YES);

			if(StringUtils.hasText(inquiryCtx.getParentItemId())){
				if (inquiryCtx.isIncludeChildItems()) {
					sql.addWhereClause(Condition.OP_NONE, "CONNECT BY PRIOR t1.BPID_ID = t1.PARENT_BPID_ID START WITH t1.PARENT_BPID_ID = :parentItemId", inquiryCtx.getParentItemId());
				}else {
					sql.addWhereClause("t1.PARENT_BPID_ID = :parentItemId", inquiryCtx.getParentItemId());
				}
			}else{
				if (inquiryCtx.isGetRootItems()) sql.addWhereClause("t1.PARENT_BPID_ID is null");
			}
			if (!inquiryCtx.isIncludeChildItems()) sql.setOrderByExpression("t1.ITEM_ORDER");
			
    		SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
    		
        	Class paramClass = this.getParameterItemClass(inquiryCtx.getParameterType());
        	
            AliasBean aliasBean = sql.createAliasBean(paramClass);

            sql.clear();
            return this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);

		} catch (Exception e) {
			LOG.error("Failed--BaseParameterItemDefDAO.getParameterItems()-" + e);
			throw new DataAccessException(e);
		}
	}
	
    /** (non-Javadoc)
     * @see cafe.core.dao.parameter.IBaseParameterItemDefDAO#getParameterItemByValue(java.lang.String, java.util.Date, java.lang.String)
     */
    public BaseParameterItemDefDTO getParameterItemByValue(String parameterType, Date versionDate, String itemValue) throws DataAccessException{
		if (!StringUtils.hasText(parameterType) || (!StringUtils.hasText(itemValue))) {
			throw new DataAccessException(CoreMessageCode.ARGUMENT_IS_NULL);
		}
       	List<BaseParameterItemDefDTO> items = this.list(new BaseParameterItemDefDTO(parameterType, versionDate, null, itemValue, CoreConstants.YES));
			
       	if (CollectionUtils.isEmpty(items)) return null;
       	return items.get(0);
    }
    
	/** (non-Javadoc)
	 * @see cafe.core.dao.parameter.IBaseParameterItemDefDAO#list(cafe.core.bean.dto.parameter.BaseParameterItemDefDTO)
	 */
    public List<BaseParameterItemDefDTO> list(BaseParameterItemDefDTO example) throws DataAccessException {
		
		if (example == null || !StringUtils.hasText(example.getParameterTypeCode())) {
			throw new DataAccessException(CoreMessageCode.ARGUMENT_IS_NULL);
		}
		Date versionDate = (example.getEffectiveDate() == null) ? null : DateTimeUtils.truncateTime(example.getEffectiveDate());
		try {
			//得到schema的名稱.
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("t1.BPID_ID", BaseParameterItemDefDTO.ATTRIBUTE.ITEM_ID.getValue());
			sql.addSelectClause("t1.BPTD_CODE", BaseParameterItemDefDTO.ATTRIBUTE.PARAMETER_TYPE_CODE.getValue());
			sql.addSelectClause("t1.EFFECTIVE_DATE", BaseParameterItemDefDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue());
			sql.addSelectClause("t1.ITEM_NAME", BaseParameterItemDefDTO.ATTRIBUTE.NAME.getValue());
			sql.addSelectClause("t1.ITEM_VALUE", BaseParameterItemDefDTO.ATTRIBUTE.VALUE.getValue());
//			sql.addSelectClause("t1.REFERENCE_CODE", BaseParameterItemDefDTO.ATTRIBUTE.REFERENCE_CODE.getValue());
			sql.addSelectClause("t1.ITEM_DESC", BaseParameterItemDefDTO.ATTRIBUTE.DESCRIPTION.getValue());
			sql.addSelectClause("t1.ITEM_ORDER", BaseParameterItemDefDTO.ATTRIBUTE.ITEM_ORDER.getValue());
//			sql.addSelectClause("t1.ITEM_DEPTH", BaseParameterItemDefDTO.ATTRIBUTE.DEPTH.getValue());
			// 去除approvedFlag
//			sql.addSelectClause("t1.APPROVED_FLAG", BaseParameterItemDefDTO.ATTRIBUTE.APPROVED_FLAG.getValue());
			sql.addSelectClause("t1.TEXT_FIELD1", BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue());
//			sql.addSelectClause("t1.TEXT_FIELD2", BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD2.getValue());
//			sql.addSelectClause("t1.TEXT_FIELD3", BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD3.getValue());
//			sql.addSelectClause("t1.TEXT_FIELD4", BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD4.getValue());
//			sql.addSelectClause("t1.TEXT_FIELD5", BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD5.getValue());
//			sql.addSelectClause("t1.NUMBER_FIELD1", BaseParameterItemDefDTO.ATTRIBUTE.NUMBER_FIELD1.getValue());
//			sql.addSelectClause("t1.NUMBER_FIELD2", BaseParameterItemDefDTO.ATTRIBUTE.NUMBER_FIELD2.getValue());
			sql.addSelectClause("t1.PARENT_BPID_ID", BaseParameterItemDefDTO.ATTRIBUTE.PARENT_ID.getValue());
			
			sql.setFromExpression(schema + ".BASE_PARAMETER_ITEM_DEF t1");

			sql.addWhereClause("t1.BPTD_CODE = :parameterTypeCode", example.getParameterTypeCode());
			
			//取得有效日期
			SqlStatement subSql = new SqlStatement();
			subSql.addSelectClause("max(EFFECTIVE_DATE)");
			subSql.setFromExpression(schema + ".BASE_PARAMETER_ITEM_DEF");
			subSql.addWhereClause("BPTD_CODE =:parameterTypeCode", example.getParameterTypeCode());
			subSql.addWhereClause("EFFECTIVE_DATE <= :versionDate", versionDate);
			// 未刪除
			sql.addWhereClause("isnull(DELETED, 'N') = :deleted", IAtomsConstants.NO);
//			subSql.addWhereClause("APPROVED_FLAG = :approvedFlag", example.getApprovedFlag());
				
			sql.addWhereClause("t1.EFFECTIVE_DATE", Condition.OP_EQ, subSql);
			
			sql.addWhereClause("t1.ITEM_VALUE =:value",example.getValue());
			sql.addWhereClause("t1.ITEM_NAME =:name",example.getName());
//			sql.addWhereClause("t1.REFERENCE_CODE =:referenceCode",example.getReferenceCode());
//			sql.addWhereClause("t1.ITEM_DEPTH =:depth",example.getDepth());
			sql.addWhereClause("t1.TEXT_FIELD1 =:textField1",example.getTextField1());
//			sql.addWhereClause("t1.TEXT_FIELD2 =:textField2",example.getTextField2());
//			sql.addWhereClause("t1.TEXT_FIELD3 =:textField3",example.getTextField3());
//			sql.addWhereClause("t1.TEXT_FIELD4 =:textField4",example.getTextField4());
//			sql.addWhereClause("t1.TEXT_FIELD5 =:textField5",example.getTextField5());
//			sql.addWhereClause("t1.NUMBER_FIELD1 =:numberField1",example.getNumberField1());
//			sql.addWhereClause("t1.NUMBER_FIELD2 =:numberField2",example.getNumberField2());
			sql.addWhereClause("t1.PARENT_BPID_ID = :parentId", example.getParentId());
			// 未刪除
			sql.addWhereClause("isnull(t1.DELETED, 'N') = :deleted", IAtomsConstants.NO);
//			sql.addWhereClause("t1.APPROVED_FLAG = :approvedFlag", example.getApprovedFlag());
			sql.setOrderByExpression("t1.ITEM_ORDER");
			
    		SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
    		
             AliasBean aliasBean = sql.createAliasBean(BaseParameterItemDefDTO.class);

            sql.clear();
            return this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);

		} catch (Exception e) {
			LOG.error("Failed--BaseParameterItemDefDAO.getParameterItems()-" + e);
				throw new DataAccessException(e);
		}		
	}
    
    /** (non-Javadoc)
     * @see cafe.core.dao.parameter.IBaseParameterItemDefDAO#getAvailableParameterItems(java.lang.String, java.util.Date)
     */
    public List<? extends Parameter> getAvailableParameterItems(String parameterType, Date versionDate) throws DataAccessException{
		return this.getAvailableParameterItems(parameterType, versionDate, null, false, false);
    }
    
    /** (non-Javadoc)
     * @see cafe.core.dao.parameter.IBaseParameterItemDefDAO#getAvailableParameterItems(java.lang.String, java.util.Date, java.lang.String, boolean, boolean)
     */
    public List<? extends Parameter> getAvailableParameterItems(String parameterType, Date versionDate, String parentParameterItemId, boolean getRootItems, boolean includeChildItems) throws DataAccessException{
			BaseParameterInquiryContext inquiryCtx = new BaseParameterInquiryContext();
			inquiryCtx.setParameterType(parameterType);
			inquiryCtx.setParentItemId(parentParameterItemId);
			inquiryCtx.setGetRootItems(getRootItems);
			inquiryCtx.setIncludeChildItems(includeChildItems);
			inquiryCtx.setVersionDate((versionDate == null) ? DateTimeUtils.getCurrentDate() : versionDate);
			return this.getParameterItems(inquiryCtx);
	}
    
    /**
     * 設定需要查詢顯示的列
     * @param parameterTypeCode
     * @return
     */
    private Properties getColumnMapSet() {
		Properties props = new Properties();
		props.setProperty(BaseParameterItemDef.TABLE_COLUMN.ITEM_DESC.name(), CompositeParameter.FIELD_DESCRIPTION);
		// 去除approvedFlag
	//	props.setProperty(BaseParameterItemDef.TABLE_COLUMN.APPROVED_FLAG.name(), CompositeParameter.FIELD_APPROVED_FLAG);
		props.setProperty(BaseParameterItemDef.TABLE_COLUMN.TEXT_FIELD1.name(), CompositeParameter.FIELD_TEXT_FIELD1);
		props.setProperty(BaseParameterItemDef.TABLE_COLUMN.PARENT_BPID_ID.name(), CompositeParameter.FIELD_PARENT_ID);
		props.setProperty(BaseParameterItemDef.TABLE_COLUMN.ITEM_NAME.name(), CompositeParameter.FIELD_NAME);
		props.setProperty(BaseParameterItemDef.TABLE_COLUMN.ITEM_VALUE.name(), CompositeParameter.FIELD_VALUE);
		props.setProperty(BaseParameterItemDef.TABLE_COLUMN.ITEM_ORDER.name(), CompositeParameter.FIELD_PARAMETER_ORDER);
		props.setProperty(BaseParameterItemDef.TABLE_COLUMN.EFFECTIVE_DATE.name(), CompositeParameter.FIELD_VERSION);//版本
		return props;
    }
}
