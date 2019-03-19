package com.cybersoft4u.xian.iatoms.common.utils;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;

import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;

public class IAtomsUtils {
	
	/**
	 * Purpose:获取小于等于当前日期里最大的日期
	 * @author jasonzhou
	 * @param effectiveDate：生效日期集合
	 * @return String:日期字符串
	 */
	public static String compare(List<Date> effectiveDate){
		if(effectiveDate==null || effectiveDate.size()<=0){
            return null;
        }
        long gap=Long.MAX_VALUE;
        Date r=null;
        long time=System.currentTimeMillis();
        for(Date t:effectiveDate){
        	if(t.getTime()<=time){
        		long tm=Math.abs(time-t.getTime());
        		if(gap>tm){
        			gap=tm;
        			r=t;
        		}
        	}
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        String str=sdf.format(r);
        return str;
	}
	
	/**
	 * Purpose: 根據數據庫欄位名獲取實體類中的屬性值
	 * @author Eric_Du
	 * @param fieldName 數據庫欄位名稱,如(ITEM_VALUE/NAME)
	 * @param object 属性所在类
	 * @return String 属性值
	 */
    public static String getAttrValueByTBField(String fieldName, Object object) throws Exception {
    	String result = null;
        try { 
        	if (StringUtils.hasText(fieldName)) {
        		String methodName = "get";
        		if (fieldName.contains(IAtomsConstants.MARK_UNDER_LINE)) {
        			String[] tempName = fieldName.split(IAtomsConstants.MARK_UNDER_LINE);
            		if (tempName != null && tempName.length >0) {
            			for(String str:tempName){
            				methodName += str.substring(0, 1)+str.substring(1).toLowerCase();
            			}
            		}
        		}else{
        			methodName += fieldName.substring(0, 1)+fieldName.substring(1).toLowerCase();
        		}
        		if (StringUtils.hasText(methodName)) {
    				Method method = object.getClass().getMethod(methodName, new Class[] {}); 
    				Object obj = method.invoke(object, new Object[] {});
    				if (obj != null) {
    					result = obj.toString();
    				}
    			}
        	}
        } catch (Exception e) { 
            throw e;
        } 
        return result;
    }
    
    /**
	 * Purpose: 根據實體類屬性名稱取實體類中的屬性值
	 * @author Eric_Du
	 * @param fieldName 屬性名稱
	 * @param object 属性所在类
	 * @return String 属性值
	 */
    public static String getFiledValueByName(String fieldName, Object object) throws Exception {
    	String result = null;
        try { 
        	if (StringUtils.hasText(fieldName)) {
        		String methodName = "";
        		if(StringUtils.hasText(fieldName)){
        			methodName = "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
        			Method method = object.getClass().getMethod(methodName, new Class[] {}); 
    				Object obj = method.invoke(object, new Object[] {});
    				//Task #2990
    				if (obj != null) {
    					if(obj instanceof Timestamp){
    						result = DateTimeUtils.toString((Timestamp)obj, DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_SLASH).trim();
    					}else if(obj instanceof Date){
    						result = DateTimeUtils.toString((Date)obj, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).trim();
    					}else{
    						result = obj.toString().trim();
    					}
    				}
        		}
        	}
        } catch (Exception e) { 
            throw e;
        } 
        return result;
    }
    
    /**
     * 
     * Purpose: 獲得ip地址
     * @author evanliu
     * @param request
     * @throws Exception
     * @return String
     */
    public static String getClientIP(ServletRequest request) throws Exception{
    	//通過代理軟件無法獲取真實IP,所以先取得Header，判斷是否有用代理
    	String[] httpHeaders = new String[]{"x-forwarded-for", "Proxy-Client-clientIP", "WL-Proxy-Client-clientIP"};
    	String clientIP = null;
	    try {
	    	for (int i = 0; i < httpHeaders.length; i++) {
	    		clientIP = ((HttpServletRequest) request).getHeader(httpHeaders[i]);  
			    if(StringUtils.hasText(clientIP) && !clientIP.equalsIgnoreCase("unknown")){
			    	break;
			    }
	    	}
		    if(!StringUtils.hasText(clientIP) || clientIP.equalsIgnoreCase("unknown"))  {
		    	clientIP = request.getRemoteAddr();  
		    }

		}catch (Exception e) {
			throw e;
		}
	    return clientIP;
	}
  
    /**
     * 
     * Purpose: 去除重複值
     * @author amandawang
     * @param fromString:原字符串
     * @param splitString：分隔符
     * @throws Exception
     * @return String
     */
	public static String removeDuplicate(String fromString, String splitString)
			throws Exception {
		String string = null;
		try {
			if (StringUtils.hasText(fromString) && StringUtils.hasText(splitString)) {
				String[] array = fromString.split(splitString);
				if (array.length > 0) {
					List<String> list = new ArrayList<String>();
					list.add(array[0]);
					for (int i = 1; i < array.length; i++) {
						//如果不包含該值，則add到list中
						if (!list.contains(array[i])) {
							list.add(array[i]);
						}
					}
					string = list.toString();
					string = org.apache.commons.lang.StringUtils.strip(list.toString(),"[]");
					string = string.replaceAll(", ", splitString);
				}
			} else {
				return fromString;
			}
			return string;
		} catch (Exception e) {
			return fromString;
		}
	}
}
