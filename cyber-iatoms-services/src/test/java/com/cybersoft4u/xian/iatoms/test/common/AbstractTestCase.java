/**
 * 
 */
package com.cybersoft4u.xian.iatoms.test.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * @author edwardyen
 *
 */
@SuppressWarnings("deprecation")
public class AbstractTestCase extends AbstractDependencyInjectionSpringContextTests {
	protected Log logger = LogFactory.getLog(getClass());
	public AbstractTestCase(){
			this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	protected String[] getConfigLocations() {
	        String[] baseConfigs = new String[] { 
	                "/cyber-iatoms-application-context.xml"
	        	};
	        String[] extentConfigs = getExtentConfigs();
	        int length = baseConfigs.length + extentConfigs.length;

	        String[] allConfigs = new String[length];

	        for (int i = 0; i < baseConfigs.length; i++) {
	            allConfigs[i] = baseConfigs[i];
	        }

	        for (int i = 0; i < extentConfigs.length; i++) {
	            allConfigs[i + baseConfigs.length] = extentConfigs[i];
	        }

	        return allConfigs;
	}
	//开发人员可以覆盖这个方法，添加自己的配置文件
	protected String[] getExtentConfigs() {
	        return new String[] {};
	}
	public void testInit(){
			
	}

}
