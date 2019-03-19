package com.cybersoft4u.xian.iatoms.common.web.taglib;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Purpose: 將Class內的靜態變數、Enum的元素轉成JSTL可讀取方式. 
 * copy ccl.{@link http://stackoverflow.com/questions/3732608/how-to-reference-constants-in-el#answer-9726738} 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年12月12日
 * @MaintenancePersonnel evanliu
 */
public class iAtomsConstantsTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7653934375711987138L;
	
	private String path = "";

	private String var = "";

	private static Map<String, Map<String, Object>> constantsMap = new HashMap<String, Map<String, Object>>();
	
	/**
	 * (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		// Use Reflection to look up the desired field.
		try {
			Class<?> clazz = null;
			try {
				clazz = Class.forName(path);
			} catch (ClassNotFoundException ex) {
				throw new JspException("Class " + path + " not found.");
			}

			Map<String, Object> constMap = null;
			if (constantsMap.containsKey(path)) {
				constMap = constantsMap.get(path);
			} else {
				constMap = new HashMap<String, Object>();
				if (clazz.isEnum()) {
					for (Object obj : clazz.getEnumConstants()) {
						constMap.put(obj.toString(), obj);
					}
				} else {
					setMapConst(constMap, clazz);
				}
				constantsMap.put(path, constMap);
			}

			// Export the Map as a Page variable.
			pageContext.setAttribute(var, constMap);
		} catch (Exception ex) {
			if (!(ex instanceof JspException)) {
				throw new JspException("Could not process constants from class " + path);
			} else {
				throw (JspException) ex;
			}
		}
		return SKIP_BODY;
	}

	/**
	 * 遞回程式，找尋所有的super class and interface
	 * @param constMap
	 * @param clazz
	 */
	private void setMapConst(Map<String, Object> constMap, Class<?> clazz) {

		if(clazz.getSuperclass() != null) {
			setMapConst(constMap, clazz.getSuperclass());
		}
		if(clazz.getInterfaces() != null) {
			for(Class<?> c : clazz.getInterfaces()) {
				setMapConst(constMap, c);
			}
		}
		
		Field[] flds = clazz.getDeclaredFields();
		// Go through all the fields, and put static ones in a Map.
		for (int i = 0; i < flds.length; i++) {
			// Check to see if this is public static final. If not, it's not a constant.
			int mods = flds[i].getModifiers();
			if (!Modifier.isFinal(mods) || !Modifier.isStatic(mods) || !Modifier.isPublic(mods)) {
				continue;
			}
			Object val = null;
			try {
				val = flds[i].get(null); // null for static fields.
			} catch (Exception ex) {
				System.out.println("Problem getting value of " + flds[i].getName());
				continue;
			}
			// flds [i].get () automatically wraps primitives.
			// Place the constant into the Map.
			constMap.put(flds[i].getName(), val);
		}
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public void setVar(String var) {
		this.var = var;
	}
}
