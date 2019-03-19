package com.cybersoft4u.xian.iatoms.web.controllers.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cafe.core.bean.Message;
import cafe.core.context.SessionContext;

import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;

/**
 * 
 * Purpose:  控制IE地址栏输入过滤器
 * @author Allenchen
 * @since  JDK 1.7
 * @date   2016/4/19
 * @MaintenancePersonnel Allenchen
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AuthFilter implements Filter{

	/**
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	/**
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request; 
	        HttpServletResponse resp = (HttpServletResponse) response; 
	        String conString = req.getHeader("REFERER");//獲取父URL--如果不是直接輸入的話就是先前的訪問過來的頁面，要是用戶輸入了，這個父URL是不存在的 
	        if("".equals(conString) || conString==null){ //判斷如果上一個目錄為空的話，說明是用戶直接輸入URL訪問的  或window.open
	            String servletPath = req.getServletPath(); //當前請求URL，去掉幾個可以直接訪問的頁面
	            String actionId = request.getParameter("actionId");//获取请求actionId
	            if(servletPath.contains("/index.jsp") || servletPath.contains("/logon.do") || servletPath.contains("/mlogon.do") ||
	            		servletPath.contains("/logoff.do") || servletPath.contains("/contactInfo.do") || (servletPath.contains("/cardManage.do") && "settingActive".equals(actionId))){ 
	            	chain.doFilter(request, response);
	            } else {
	            	SessionContext sc = new SessionContext();
	            	Message msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ILLEGAL_OPERATION);
	            	sc.setReturnMessage(msg);
	            	req.setAttribute("sessionContext", sc);
	            	req.getRequestDispatcher("/logoff.do?actionId=logoff").forward(req, resp);
	            } 
	        } else { 
	            chain.doFilter(request, response); 
	        } 
		}catch(Exception e) {
			throw new ServletException(e);
		}
	}
	@Override
	public void destroy() {
	}
}
