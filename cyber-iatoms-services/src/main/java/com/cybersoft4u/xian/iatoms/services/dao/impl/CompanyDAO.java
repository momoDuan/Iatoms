package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;

import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;

import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CompanyFormDTO;

import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompany;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;


/**
 * Purpose: 公司基本訊息維護DAO
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年7月1日
 * @MaintenancePersonnel ElvaHe
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CompanyDAO extends GenericBaseDAO<BimCompany> implements ICompanyDAO{

	/**
	 * 日誌記錄物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(CompanyDAO.class);
	
	/**
	 * (non-Javadoc) 
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<CompanyDTO> listBy (String companyType, String shortName, String sort, String order, Integer pageSize, Integer pageIndex) throws DataAccessException {
		LOGGER.debug("listBy()", "companyType:", companyType);
		LOGGER.debug("listBy()", "shortName:", shortName);
		LOGGER.debug("listBy()", "sort:", sort);
		LOGGER.debug("listBy()", "pageSize:", String.valueOf(pageSize));
		LOGGER.debug("listBy()", "order:", order);
		LOGGER.debug("listBy()", "pageIndex:", String.valueOf(pageIndex));
		List<CompanyDTO> companyDTOs = null;
		String schema = this.getMySchema();
		//SqlStatement sql = new SqlStatement();
		//子表
		SqlStatement sqlStatement = new SqlStatement();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			//拼接子表SQL語句
			sqlStatement.addSelectClause("DISTINCT company1.COMPANY_ID", CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue());
			stringBuffer.append("convert(varchar(1000),STUFF((SELECT ',' + LTRIM( typeBase.ITEM_NAME) FROM ").append(schema).append(".BIM_COMPANY_TYPE type1 ,");
			stringBuffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF typeBase WHERE type1.COMPANY_ID = company1.COMPANY_ID AND typeBase.BPTD_CODE = :companyType ");
			stringBuffer.append("AND type1.COMPANY_TYPE = typebase.ITEM_VALUE FOR XML PATH('')) , 1 , 1 ,''))");
			sqlStatement.addSelectClause(stringBuffer.toString(), CompanyDTO.ATTRIBUTE.COMPANY_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("company1.COMPANY_CODE", CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
			sqlStatement.addSelectClause("company1.SHORT_NAME", CompanyDTO.ATTRIBUTE.SHORT_NAME.getValue());
			sqlStatement.addSelectClause("company1.UNITY_NUMBER", CompanyDTO.ATTRIBUTE.UNITY_NUMBER.getValue());
			sqlStatement.addSelectClause("company1.INVOICE_HEADER", CompanyDTO.ATTRIBUTE.INVOICE_HEADER.getValue());
			sqlStatement.addSelectClause("company1.Leader", CompanyDTO.ATTRIBUTE.LEADER.getValue());
			sqlStatement.addSelectClause("company1.TEL", CompanyDTO.ATTRIBUTE.TEL.getValue());
			sqlStatement.addSelectClause("company1.FAX", CompanyDTO.ATTRIBUTE.FAX.getValue());
			sqlStatement.addSelectClause("company1.APPLY_DATE", CompanyDTO.ATTRIBUTE.APPLY_DATE.getValue());
			sqlStatement.addSelectClause("company1.PAY_DATE", CompanyDTO.ATTRIBUTE.PAY_DATE.getValue());
			sqlStatement.addSelectClause("company1.CONTACT", CompanyDTO.ATTRIBUTE.CONTACT.getValue());
			sqlStatement.addSelectClause("company1.CONTACT_TEL", CompanyDTO.ATTRIBUTE.CONTACT_TEL.getValue());
			sqlStatement.addSelectClause("company1.CONTACT_EMAIL", CompanyDTO.ATTRIBUTE.CONTACT_EMAIL.getValue());
			sqlStatement.addSelectClause("company1.CUSTOMER_CODE", CompanyDTO.ATTRIBUTE.CUSTOMER_CODE.getValue());
			sqlStatement.addSelectClause("company1.DTID_TYPE", CompanyDTO.ATTRIBUTE.DTID_TYPE.getValue());
			sqlStatement.addSelectClause("baseDTID.ITEM_NAME", CompanyDTO.ATTRIBUTE.DTID_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("company1.AUTHENTICATION_TYPE", CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue());
			sqlStatement.addSelectClause("baseAuthen.ITEM_NAME", CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("company1.COMPANY_EMAIL", CompanyDTO.ATTRIBUTE.COMPANY_EMAIL.getValue());
			sqlStatement.addSelectClause("company1.ADDRESS_LOCATION", CompanyDTO.ATTRIBUTE.ADDRESS_LOCATION.getValue());
			sqlStatement.addSelectClause("ISNULL(baseAddress.ITEM_NAME,'')+ISNULL(company1.ADDRESS,'')", CompanyDTO.ATTRIBUTE.ADDRESS_LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("company1.ADDRESS", CompanyDTO.ATTRIBUTE.ADDRESS.getValue());
			sqlStatement.addSelectClause("company1.INVOICE_ADDRESS_LOCATION", CompanyDTO.ATTRIBUTE.INVOICE_ADDRESS_LOCATION.getValue());
			sqlStatement.addSelectClause("ISNULL(baseInvoice.ITEM_NAME,'')+ISNULL(company1.INVOICE_ADDRESS,'')", CompanyDTO.ATTRIBUTE.INVOICE_ADDRESS_LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("company1.INVOICE_ADDRESS", CompanyDTO.ATTRIBUTE.INVOICE_ADDRESS.getValue());
			sqlStatement.addSelectClause("company1.REMARK", CompanyDTO.ATTRIBUTE.REMARK.getValue());
			sqlStatement.addSelectClause("company1.IS_NOTIFY_AO", CompanyDTO.ATTRIBUTE.IS_NOTIFY_AO.getValue());
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(schema).append(".BIM_COMPANY company1 LEFT JOIN ").append(schema);
			stringBuffer.append(".BIM_COMPANY_TYPE type2 ON company1.COMPANY_ID = type2.COMPANY_ID LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF baseDTID ON baseDTID.BPTD_CODE = :dtidType AND baseDTID.ITEM_VALUE = company1.DTID_TYPE LEFT JOIN ");
			stringBuffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF baseAddress ON baseAddress.BPTD_CODE = :location AND baseAddress.ITEM_VALUE = company1.ADDRESS_LOCATION LEFT JOIN ").append(schema);
			stringBuffer.append(".BASE_PARAMETER_ITEM_DEF baseInvoice ON baseInvoice.BPTD_CODE = :location AND baseInvoice.ITEM_VALUE = company1.INVOICE_ADDRESS_LOCATION LEFT JOIN ").append(schema);
			stringBuffer.append(".BASE_PARAMETER_ITEM_DEF baseAuthen ON baseAuthen.BPTD_CODE = :authenType AND baseAuthen.ITEM_VALUE = company1.AUTHENTICATION_TYPE");
			sqlStatement.addFromExpression(stringBuffer.toString());
			sqlStatement.addWhereClause("isnull(company1.DELETED,'N') = :deleted");
			//若存在companyType
			if (StringUtils.hasText(companyType)) {
				sqlStatement.addWhereClause("type2.COMPANY_TYPE = :type");
			}
			//若存在ShortName
			if (StringUtils.hasText(shortName)) {
				sqlStatement.addWhereClause("company1.SHORT_NAME LIKE :shortName");
			}
			//打印子表SQL語句
			LOGGER.debug("listBy()", "子表SQL:", sqlStatement.toString());
			/*stringBuffer.delete(0, stringBuffer.length());
			//拼接主表SQL
			stringBuffer.append("DISTINCT company2.companyId, company2.companyTypeName, company2.companyCode,company2.shortName, company2.unityNumber, company2.invoiceHeader,");
			stringBuffer.append("company2.leader, company2.tel, company2.fax, company2.applyDate, company2.payDate, company2.contact, company2.contactTel, company2.contactEmail,");
			stringBuffer.append("company2.customerCode, company2.dtidType, company2.dtidTypeName, company2.companyEmail, company2.addressLocation, company2.addressLocationName,");
			stringBuffer.append("company2.address, company2.invoiceAddressLocation, company2.invoiceAddressLocationName, company2.invoiceAddress, company2.remark, company2.authenticationType, company2.authenticationTypeName");
			sql.addSelectClause(stringBuffer.toString());
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append("(").append(sqlStatement.toString()).append(") company2"); 
			sql.addFromExpression(stringBuffer.toString());*/
			//排序方式
			if ((StringUtils.hasText(sort)) && (StringUtils.hasText(order))) {
				//有選擇排序方式和參考列時
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
				sqlStatement.setOrderByExpression(stringBuffer.toString());
			} else {
				//默認的排序方式和參考列
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(CompanyFormDTO.PARAM_SORT_NAME).append(IAtomsConstants.MARK_SPACE).append(IAtomsConstants.PARAM_PAGE_ORDER);
				sqlStatement.setOrderByExpression(stringBuffer.toString());
			}
			//分頁
			sqlStatement.setPageSize(pageSize.intValue());
			sqlStatement.setStartPage(pageIndex.intValue() - 1);
			//打印SQL語句
			LOGGER.debug("listBy()", "SQL:", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			//設置佔位符
			if (StringUtils.hasText(companyType)) {
				sqlQueryBean.setParameter("type", companyType);
			}
			if (StringUtils.hasText(shortName)) {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(shortName).append(IAtomsConstants.MARK_PERCENT);
				sqlQueryBean.setParameter("shortName",  stringBuffer.toString());
			}
			sqlQueryBean.setParameter("companyType", IATOMS_PARAM_TYPE.COMPANY_TYPE.getCode());
			sqlQueryBean.setParameter("authenType", IATOMS_PARAM_TYPE.AUTHENTICATION_TYPE.getCode());
			sqlQueryBean.setParameter("dtidType", IATOMS_PARAM_TYPE.DTID_TYPE.getCode());
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("deleted", IAtomsConstants.NO);
			AliasBean aliasBean = sqlStatement.createAliasBean(CompanyDTO.class);
			//滿足條件的公司信息集合
			companyDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.debug("listBy()", "error:", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_COMPANY)}, e);
		}
		return companyDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO#count(java.lang.String, java.lang.String)
	 */
	@Override
	public Integer count(String queryCompanyType, String queryShortName)throws DataAccessException {
		LOGGER.debug("listBy()", "queryCompanyType:", queryCompanyType);
		LOGGER.debug("listBy()", "queryShortName:", queryShortName);
		SqlStatement sqlStatement = new SqlStatement();
		String schema = this.getMySchema();
		StringBuffer stringBuffer = new StringBuffer();
		Integer count = null;
		try {
			//拼接SQL
			sqlStatement.addSelectClause("count(1)"); 
			sqlStatement.addWhereClause("isnull(c.DELETED,'N') = :deleted");
			//存在公司類型時
			if (StringUtils.hasText(queryCompanyType)){
				stringBuffer.append(schema).append(".BIM_COMPANY c left join ").append(schema).append(".BIM_COMPANY_TYPE t on t.COMPANY_ID=c.COMPANY_ID");
				sqlStatement.addFromExpression(stringBuffer.toString());
				sqlStatement.addWhereClause(" t.COMPANY_TYPE = :companyType", queryCompanyType);
			} else {
				//不存在公司類型時
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(schema).append(".BIM_COMPANY c");
				sqlStatement.addFromExpression(stringBuffer.toString());
			}
			//存在公司簡稱
			if (StringUtils.hasText(queryShortName)) {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(queryShortName).append(IAtomsConstants.MARK_PERCENT);
				sqlStatement.addWhereClause(" c.SHORT_NAME like :shortName", stringBuffer.toString());
			}
			LOGGER.debug("count()", "SQL", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("deleted", IAtomsConstants.NO);
			//查詢結果
			List<Integer> list = super.getDaoSupport().findByNativeSql(sqlQueryBean);
			//當有查詢結果時獲取第一個參數作為最終結果
			if(!CollectionUtils.isEmpty(list)){
				count = list.get(0);
			}
			return count;
		} catch (Exception e) {
			LOGGER.debug("count()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_COMPANY)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO#getCompanyList(java.util.List, java.lang.String, java.lang.Boolean, java.lang.String)
	 */
	@Override
	public List<Parameter> getCompanyList(List<String> companyTypeList, String authenticationType, Boolean isHaveSla, String dtidType) throws DataAccessException {
		LOGGER.debug("getCompanyList()", "companyTypeList:", String.valueOf(companyTypeList));
		LOGGER.debug("getCompanyList()", "authenticationType:", authenticationType);
		LOGGER.debug("getCompanyList()", "isHaveSla:",String.valueOf(isHaveSla));
		long startQueryCompanyTime = System.currentTimeMillis();
		List<Parameter> result = null;
		//得到schema
		String schema = this.getMySchema();
		//創建SqlStatement
		SqlStatement sql = new SqlStatement();
		int size = 0;
		String companyType = null;
		//存在公司類型結合時
		if(!CollectionUtils.isEmpty(companyTypeList)){
			//公司類型的數量
			size = companyTypeList.size();
			//公司數量為1時設置公司類型
			if(size == 1){
				companyType = companyTypeList.get(0);
			}
		}
		try {
			sql.addSelectClause("a.value", Parameter.FIELD_VALUE);
			sql.addSelectClause("a.name", Parameter.FIELD_NAME);
			StringBuffer buffer = new StringBuffer();
			buffer.append("(select c.COMPANY_ID as value, c.SHORT_NAME as name");
			if(size != 1){
				buffer.append(",").append("min(base.ITEM_ORDER) as typeOrder");
			}
			buffer.append(" from ");
			buffer.append(schema).append(".BIM_COMPANY c,");
			buffer.append(schema).append(".BIM_COMPANY_TYPE type");
			if(size != 1){
				buffer.append(",").append(schema).append(".BASE_PARAMETER_ITEM_DEF base");
				buffer.append(" where base.BPTD_CODE =:companyTypeCode");
				buffer.append(" and base.ITEM_VALUE = type.COMPANY_TYPE and");
			} else {
				buffer.append(" where");
			}
			buffer.append(" isnull(c.DELETED,'N') = 'N'");
			if(StringUtils.hasText(authenticationType)){
				buffer.append(" and c.AUTHENTICATION_TYPE =:authenticationType");
			}
			buffer.append(" AND c.COMPANY_ID = type.COMPANY_ID");
			if(size == 1){
				buffer.append(" and type.COMPANY_TYPE = :companyType");
			} else if(size != 0){
				buffer.append(" and type.COMPANY_TYPE in (:companyTypeList)");
			}
			if (isHaveSla.booleanValue()) {
				buffer.append(" and exists(select 1 from ").append(schema).append(".BIM_CONTRACT contract,").append(schema).append(".BIM_SLA sla");
				buffer.append(" where contract.COMPANY_ID = c.COMPANY_ID AND isnull(contract.DELETED,'N') = 'N' and contract.CONTRACT_ID = sla.CONTRACT_ID)");
			}
			if (StringUtils.hasText(dtidType)){
				buffer.append(" and c.DTID_TYPE = :dtidType");
			}
			buffer.append(" group by c.COMPANY_ID,c.SHORT_NAME)a");
			sql.addFromExpression(buffer.toString());
			if(size == 1){
				sql.setOrderByExpression("a.name asc");
			} else {
				sql.setOrderByExpression("a.typeOrder,a.name asc");
			}
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			
			if(size == 1){
				sqlQueryBean.setParameter("companyType", companyType);
			} else {
				if(size != 0){
					sqlQueryBean.setParameter("companyTypeList", companyTypeList);
				}
				sqlQueryBean.setParameter("companyTypeCode", IATOMS_PARAM_TYPE.COMPANY_TYPE.getCode());
			}
			if(StringUtils.hasText(authenticationType)){
				sqlQueryBean.setParameter("authenticationType", authenticationType);
			}
			if (StringUtils.hasText(dtidType)){
				sqlQueryBean.setParameter(CompanyDTO.ATTRIBUTE.DTID_TYPE.getValue(), dtidType);
			}
			//記錄sql語句
			LOGGER.debug("getCompanyList()", "SQL:", sql.toString());
			AliasBean aliasBean = new AliasBean(Parameter.class);				
			result = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCompanyList()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_COMPANY)}, e);
		}
		long endQueryCompanyTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> load", "DAO getCompanyList:" + (endQueryCompanyTime - startQueryCompanyTime));
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO#countUserByCompanyId(java.lang.String, java.lang.String)
	 */
	@Override
	public Integer countUserByCompanyId(String companyId, String deleted) throws DataAccessException {
		LOGGER.debug("countUserByCompanyId()", "companyId:", companyId);
		LOGGER.debug("countUserByCompanyId()", "deleted:", deleted);
		Integer resultValue = null;
		try{
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStatement.addSelectClause("count(1)");
			stringBuffer.append(schema).append(".ADM_USER u");
			sqlStatement.addFromExpression(stringBuffer.toString());
			//存在公司編號
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("u.COMPANY_ID = :companyId", companyId);
			} 
			//存在刪除標誌位
			if (StringUtils.hasText(deleted)) {
				sqlStatement.addWhereClause("isnull(u.DELETED,'N')=:deleted", deleted);
			} 
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("countUserByCompanyId()", "SQL:", sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			//存在查詢結果是獲取第一個參數作為符合條件的數量
			if (!CollectionUtils.isEmpty(result)) {
				resultValue = result.get(0);
			}
			return resultValue;
		} catch (Exception e) {
			LOGGER.error("countUserByCompanyId()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO#isCheck(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isCheck(String companyId, String companyCode,  String shortName, String customerCode) throws DataAccessException {
		boolean result = false;
		try {
			//打印參數
			LOGGER.debug("check()", "companyId:", companyId);
			LOGGER.debug("check()", "companyCode:", companyCode);
			LOGGER.debug("check()", "shortName:", shortName);
			LOGGER.debug("check()", "customerCode:", customerCode);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer stringBuffer = new StringBuffer();
			//拼接SQL
			sqlStatement.addSelectClause("COUNT(1)");
			stringBuffer.append(schema).append(".Bim_COMPANY c");
			sqlStatement.addFromExpression(stringBuffer.toString());
			sqlStatement.addWhereClause("isnull(c.DELETED,'N') = :deleted", IAtomsConstants.NO);
			//新增時不判斷公司編號
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("c.COMPANY_ID <> :companyId", companyId);
			}
			//校驗公司代號時
			if (StringUtils.hasText(companyCode)) {
				sqlStatement.addWhereClause("c.COMPANY_CODE = :companyCode", companyCode);
			}
			//檢驗公司簡稱時
			if (StringUtils.hasText(shortName)) {
				sqlStatement.addWhereClause("c.SHORT_NAME = :shortName", shortName);
			}
			//檢驗客戶碼時
			if (StringUtils.hasText(customerCode)) {
				sqlStatement.addWhereClause("c.CUSTOMER_CODE = :customerCode", customerCode);
			}
			//打印SQL語句
			LOGGER.debug("check()", "SQL:", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> count = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			//若存在結果
			if (!CollectionUtils.isEmpty(count)) {
				//獲取查詢結果
				int number = count.get(0).intValue();
				//若查詢結果大於0則代表重複
				if (number > 0 ) {
					result = true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("check()", "error:", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_WAREHOUSE)}, e);
		}
		return result;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO#getCompanyByCompanyCode(java.lang.String)
	 */
	@Override
	public CompanyDTO getCompanyByCompanyCode(String companyCode)
			throws DataAccessException {
		LOGGER.debug("getCompanyByCompanyCode()", "companyICode:", companyCode);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStatement.addSelectClause("company.COMPANY_ID", CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("company.SHORT_NAME", CompanyDTO.ATTRIBUTE.SHORT_NAME.getValue());
			stringBuffer.append(schema).append(".BIM_COMPANY company ");
			sqlStatement.addFromExpression(stringBuffer.toString());
			if (StringUtils.hasText(companyCode)) {
				sqlStatement.addWhereClause(" company.COMPANY_CODE = :code", companyCode);
			}
			sqlStatement.addWhereClause("company.DELETED = :deleted", IAtomsConstants.NO);
			//記錄sql語句
			LOGGER.debug("getCompanyByCompanyCode()", "SQL:", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(CompanyDTO.class);
			List<CompanyDTO> result = this.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
			//當查詢結果不為空時獲取對應的公司編號
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0);
			}
		}  catch (Exception e) {
			LOGGER.error("getCompanyByCompanyCode()", "error:", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO#getCompanyIdByName(java.lang.String)
	 */
	public String getCompanyIdByName(String name) throws DataAccessException {
		LOGGER.debug("getCompanyIdByName()", "name:", name);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStatement.addSelectClause("company.COMPANY_ID", CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue());
			stringBuffer.append(schema).append(".BIM_COMPANY company ");
			sqlStatement.addFromExpression(stringBuffer.toString());
			//判斷重複的設備名稱
			if (StringUtils.hasText(name)) {
				sqlStatement.addWhereClause(" company.SHORT_NAME = :name", name);
			}
			sqlStatement.addWhereClause("company.DELETED = :deleted", IAtomsConstants.NO);
			//記錄sql語句
			LOGGER.debug("getCompanyIdByName()", "SQL:", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(CompanyDTO.class);
			List<CompanyDTO> result = this.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
			//當查詢結果不為空時獲取對應的公司編號
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0).getCompanyId();
			}
		} catch (Exception e) {
			LOGGER.error("getCompanyIdByName()", "error:", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO#getCompanyDTOByCompanyId(java.lang.String)
	 */
	public CompanyDTO getCompanyDTOByCompanyId(String companyId) throws DataAccessException {
		LOGGER.debug("getCompanyDTOByCompanyId()", "companyId:", companyId);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			//拼接SQL語句
			sqlStatement.addSelectClause("COMPANY_ID", CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("SHORT_NAME", CompanyDTO.ATTRIBUTE.SHORT_NAME.getValue());
			sqlStatement.addSelectClause("DTID_TYPE", CompanyDTO.ATTRIBUTE.DTID_TYPE.getValue());
			stringBuffer.append(schema).append(".BIM_COMPANY ");
			sqlStatement.addFromExpression(stringBuffer.toString());
			//存在公司編號
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("COMPANY_ID = :companyId", companyId);
			}
			sqlStatement.addWhereClause("DELETED = :deleted", IAtomsConstants.NO);
			LOGGER.debug("geyCompanyDTOByCompanyId()", "SQL:", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(CompanyDTO.class);
			List<CompanyDTO> result = this.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
			//存在查詢結果時獲取第一筆數據
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0);
			}
		} catch(Exception e) {
			LOGGER.error("getCompanyDTOByCompanyId()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO#getCompanyList(boolean)
	 */
	@Override
	public List<Parameter> getCompanyList(boolean isCodeList) throws DataAccessException {
		LOGGER.debug("getCompanyList()", "isCodeList:" +  isCodeList);
		List<Parameter> result = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			//拼接SQL語句
			sqlStatement.addSelectClause("COMPANY_ID", Parameter.FIELD_VALUE);
			if(isCodeList){
				sqlStatement.addSelectClause("COMPANY_CODE", Parameter.FIELD_NAME);
			} else {
				sqlStatement.addSelectClause("SHORT_NAME", Parameter.FIELD_NAME);
			}
			sqlStatement.addFromExpression(schema + ".BIM_COMPANY ");
		//	sqlStatement.addWhereClause("COMPANY_ID <> '10000000-01'");
			LOGGER.debug("getCompanyList()", "SQL:", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch(Exception e) {
			LOGGER.error("getCompanyDTOByCompanyId()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO#deleteTransferData()
	 */
	@Override
	public void deleteTransferData() throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_FUNCTION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_LINK; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_TRANSACTION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_ATT_FILE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_COMM_MODE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_HANDLE_INFO; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_ASSET_LINK; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_ASSET_FUNCTION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_TRANSACTION_PARAMETER; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_COMM_MODE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_HANDLE_INFO; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_FAULT_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_FAULT_COM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_COMM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".PVM_APPLICATION_ASSET_LINK; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".PVM_APPLICATION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".PVM_DTID_DEF; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_ASSET_TYPE_COMPANY; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_SLA; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_ASSET_IN_LIST; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_ASSET_IN_INFO; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_CONTRACT_VENDOR; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_CONTRACT_TYPE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_CONTRACT_ATTACHED_FILE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_CONTRACT_ASSET; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_CONTRACT; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_MERCHANT_HEADER; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_MERCHANT; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".ADM_USER_ROLE where USER_ID <> '1000000000-0001';");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".ACT_ID_MEMBERSHIP where USER_ID_ <> 'iatoms';");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".ACT_ID_USER where ID_ <> 'iatoms';");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".ADM_USER_WAREHOUSE where USER_ID <> '1000000000-0001';");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".ADM_PWD_HISTORY where USER_ID <> '1000000000-0001';");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".ADM_USER where USER_ID <> '1000000000-0001';");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_WAREHOUSE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_DEPARTMENT where COMPANY_ID <> '10000000-01';");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_SUPPLIES; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_REPORT_SETTING_DETAIL; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_REPORT_SETTING; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_COMPANY_TYPE where COMPANY_ID <> '10000000-01';");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_COMPANY where COMPANY_ID <> '10000000-01';");
			
			LOGGER.debug("deleteTransferData()", "sql:" + sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("deleteTransferData()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO#getDtidTypeByCompanyId(java.lang.String)
	 */
	/*public String getDtidTypeByCompanyId(String companyId) throws DataAccessException {
		LOGGER.debug("getDtidTypeByCompanyId()", "companyId:", companyId);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			//拼接SQL
			sqlStatement.addSelectClause("COMPANY_ID", CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("DTID_TYPE", CompanyDTO.ATTRIBUTE.DTID_TYPE.getValue());
			stringBuffer.append(schema).append(".BIM_COMPANY ");
			sqlStatement.addFromExpression(stringBuffer.toString());
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("COMPANY_ID = :companyId", companyId);
			}
			sqlStatement.addWhereClause("DELETED = :deleted", IAtomsConstants.NO);
			LOGGER.debug("getDtidTypeByCompanyId()", "SQL:", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(CompanyDTO.class);
			List<CompanyDTO> result = this.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0).getDtidType();
			}
		} catch(Exception e) {
			LOGGER.error("getDtidTypeByCompanyId()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return null;
	}*/
	
}
