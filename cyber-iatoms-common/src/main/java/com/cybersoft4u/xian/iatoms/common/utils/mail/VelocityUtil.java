package com.cybersoft4u.xian.iatoms.common.utils.mail;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * Purpose: 替换邮件模板中关键字
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年8月13日
 * @MaintenancePersonnel candicechen
 */
public class VelocityUtil {
	
	/**
	 * 系統日誌記錄物件
	 */
	private static Log LOGGER = LogFactory.getLog(MailTargetSeparator.class);
	
	/**
	 * Purpose:替换邮件模板中的关键字
	 * @author candicechen
	 * @param templateStr:模板文件
	 * @param map:关键字集合
	 * @param charset:字符集编码
	 * @return String:替换后模板text
	 */
	public static String merge(String templateStr, Map<String, Object> map,
			String charset) {
		try {
			VelocityEngine ve = new VelocityEngine();
			Properties p = new Properties();
			p.put("input.encoding", charset);
			p.put("output.encoding", charset);
			p.put("resource.loader", "srl");
			p.put("srl.resource.loader.class",
					"com.cybersoft4u.xian.iatoms.common.utils.mail.StringResourceLoader");
			ve.init(p);
			
			VelocityContext context = new VelocityContext();
			for (String key : map.keySet()) {
				context.put(key, map.get(key));
			}
			//获取模板
			Template template = ve.getTemplate(templateStr, charset);
			StringWriter sw = new StringWriter();
			template.merge(context, sw);
			return sw.toString();
		} catch (Exception ex) {
			LOGGER.error(ex, ex);
			throw new IllegalStateException(ex);
		}

	}
}
