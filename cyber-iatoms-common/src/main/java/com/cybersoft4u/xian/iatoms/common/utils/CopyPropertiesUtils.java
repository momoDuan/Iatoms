package com.cybersoft4u.xian.iatoms.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import cafe.core.util.BeanUtils;

/**
 * Purpose: 複製對象屬性工具類
 * 
 * @author KevinShen
 * @since JDK 1.6
 * @date 2016/7/20
 * @MaintenancePersonnel KevinShen
 */
public class CopyPropertiesUtils {
	/**
	 * Purpose: 複製對象屬性
	 * @author FelixLi
	 * @param source	源對象
	 * @param target	需要複製屬性的對象
	 * @param ignoreProperties
	 * @throws BeansException
	 * @return void
	 */
	@SuppressWarnings({ "rawtypes" })
	public void copyProperties(Object source, Object target,
			String[] ignoreProperties) throws BeansException {

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = BeanUtils
				.getPropertyDescriptors(actualEditable);

		List ignoreList = (ignoreProperties != null) ? Arrays
				.asList(ignoreProperties) : null;

		for (int i = 0; i < targetPds.length; i++) {
			PropertyDescriptor targetPd = targetPds[i];
			if (targetPd.getWriteMethod() != null
					&& (ignoreProperties == null || (!ignoreList
							.contains(targetPd.getName())))) {
				PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(
						source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass()
								.getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source, new Object[0]);
						if (value != null) {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod
									.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, new Object[] { value });
						}
					} catch (Throwable ex) {
						throw new FatalBeanException(
								this.getClass().getSimpleName()
										+ "Could not copy properties from source to target",
								ex);
					}
				}
			}
		}
	}
}
