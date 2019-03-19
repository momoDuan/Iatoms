package com.cybersoft4u.xian.iatoms.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cafe.core.config.SystemConfigManager;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;

/**
 * Purpose: NET請求工具（POST）
 * @author neiljing
 * @since  JDK 1.7
 * @date   2018/4/9
 * @MaintenancePersonnel neiljing
 */
public class NetUtil {

	/**
	 *  系統日誌記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(NetUtil.class);
	
    private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    //忽略Https證書
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        	LOGGER.error("NetUtil --> trustAllHosts() --> ", "trustAllHosts error!!!", e);
        }
    }

    /**
	 * Purpose:CMS_API POST請求（Https忽略證書）
	 * @param ApiNo:請求的API編號
	 * @param json:請求的json數據
	 * @return String
	 */
    public static Map<String, String> sendHtppsNew(String ApiNo, String json) {
    	Map<String, String> map = new HashMap<String, String>();
    	//Task #3519 上行
    	Boolean output = false;
    	//Task #3519 下行
    	Boolean input = false;
        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;
        HttpURLConnection conn;
        try {
            trustAllHosts();
            String url = "https://"+ 
                    SystemConfigManager.getProperty(IAtomsConstants.CMS_IP_FLAG, IAtomsConstants.CMS_IP) +
                    "/api/" + ApiNo;
            URL realUrl = new URL(url);
            //通過請求地址判斷請求類型(http或者是https)
            if (realUrl.getProtocol().toLowerCase().equals("https")) {
                HttpsURLConnection https = (HttpsURLConnection) realUrl.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                conn = https;
            } else {
                conn = (HttpURLConnection) realUrl.openConnection();
            }
            // 設置通用的請求屬性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8"); 
            // 發送POST請求必須設置如下兩行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 獲取URLConnection對象對應的輸出流
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));  
            //out = new PrintWriter(conn.getOutputStream());
            // 發送請求參數
            out.print(json);
            // flush輸出流的緩衝
            out.flush();
            output = true;
            
            // 定義BufferedReader輸入流來讀取URL的響應
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            input = true;
        } catch (Exception e) {
        	LOGGER.error("NetUtil --> sendHtpps() --> ", "call CMS_API error!!!", e);
        } finally {// 使用finally塊來關閉輸出流、輸入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            	LOGGER.error("NetUtil --> sendHtpps() --> ", "call CMS_API error!!!", ex);
            }
        }
        map.put(IAtomsConstants.API_OUT_PUT, output.toString());	
        map.put(IAtomsConstants.PARAM_ACTION_RESULT, result);	
        return map;
    }
    /**
	 * Purpose:CMS_API POST請求（Https忽略證書）
	 * @param ApiNo:請求的API編號
	 * @param json:請求的json數據
	 * @return String
	 */
    public static String sendHtpps(String ApiNo, String json) {
    	
        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;
        HttpURLConnection conn;
        try {
            trustAllHosts();
            String url = "https://"+ 
                    SystemConfigManager.getProperty(IAtomsConstants.CMS_IP_FLAG, IAtomsConstants.CMS_IP) +
                    "/api/" + ApiNo;
            URL realUrl = new URL(url);
            //通過請求地址判斷請求類型(http或者是https)
            if (realUrl.getProtocol().toLowerCase().equals("https")) {
                HttpsURLConnection https = (HttpsURLConnection) realUrl.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                conn = https;
            } else {
                conn = (HttpURLConnection) realUrl.openConnection();
            }
            // 設置通用的請求屬性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8"); 
            // 發送POST請求必須設置如下兩行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 獲取URLConnection對象對應的輸出流
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));  
            //out = new PrintWriter(conn.getOutputStream());
            // 發送請求參數
            out.print(json);
            // flush輸出流的緩衝
            out.flush();
            
            // 定義BufferedReader輸入流來讀取URL的響應
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	LOGGER.error("NetUtil --> sendHtpps() --> ", "call CMS_API error!!!", e);
        } finally {// 使用finally塊來關閉輸出流、輸入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            	LOGGER.error("NetUtil --> sendHtpps() --> ", "call CMS_API error!!!", ex);
            }
        }
       
        return result;
    }
}
