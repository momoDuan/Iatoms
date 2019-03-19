package com.cybersoft4u.xian.iatoms.common.utils;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import java.io.File;  
import java.io.IOException;  
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
    
import org.apache.log4j.Logger;

public class FtpClient {
		  
	  private static final Logger logger = Logger.getLogger(FtpClient.class);  
	    
	  public static void main(String[] args) {
		  try {
			  
				List<String> pathList = new ArrayList<String>();
				pathList.add("c:\\IATOMS\\TEMP\\TICKET.CSV");
				pathList.add("c:\\IATOMS\\TEMP\\TICKET_RECORD.CSV");
				pathList.add("c:\\IATOMS\\TEMP\\PARAMETER.CSV");
				pathList.add("c:\\IATOMS\\TEMP\\REPOSITORY.CSV");
				uploadFile("192.168.96.33", 990, "CMSUser", "1qaz2wsx!", "/CYBER/SYS_IN/",pathList);
				//uploadFile("192.168.93.88", 990, "amada", "1qa2ws#", "new",pathList);
				//uploadFile("192.168.93.67", 990, "amanda", "4d8rYXgG", "d:\\FTPS",pathList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	  /**
		 * Description: 向FTP服务器上传文件
		 * @param url FTP服务器hostname
		 * @param port FTP服务器端口
		 * @param username FTP登录账号
		 * @param password FTP登录密码
		 * @param path FTP服务器保存目录
		 * @param filename 上传到FTP服务器上的文件名
		 * @return 成功返回true，否则返回false
		 * @throws KeyManagementException 
		 * @throws FTPException 
		 * @throws FTPIllegalReplyException 
		 * @throws IllegalStateException 
		 */
		public static boolean uploadFile(String url,int port,String username, String password, String path, List<String> pathNames) throws KeyManagementException, IllegalStateException, FTPIllegalReplyException, FTPException {
			boolean success = false;
			FTPClient client = new FTPClient();
			try {
				SSLContext sslContext = null;
				try {
					TrustManager[] trustManager = new TrustManager[] { new X509TrustManager() {
						public X509Certificate[] getAcceptedIssuers() {
							 return null;
						}
						public void checkClientTrusted(X509Certificate[] certs,
								String authType) {
						}
						public void checkServerTrusted(X509Certificate[] certs,
								String authType) {
						}
					} };
					sslContext = SSLContext.getInstance("SSL");
					sslContext.init(null, trustManager, new SecureRandom());
					SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
					client.setSSLSocketFactory(sslSocketFactory);
					client.setSecurity(FTPClient.SECURITY_FTPS);
					
					logger.debug("ftp uploadFile() begin to connect...");
					client.connect(url, port);
					logger.debug("ftp uploadFile() is connected...");
					client.setType(FTPClient.TYPE_BINARY);
					client.login(username, password);
					logger.debug("ftp uploadFile() is login...");
					client.changeDirectory(path);
					// 当前文件夹  
					String dir = client.currentDirectory();
					//client.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
					//client.execPROT("P");
					logger.debug("ftp uploadFile()--> currentDirectory="+dir);
					System.out.println(dir); 
					
					File file = null;
					for (int i = 0; i < pathNames.size(); i++) {
						file=new File(pathNames.get(i));
						if (file.exists()) {
							try {
								client.upload(file);
								//client.upload(file, listener)
								System.out.println("upload file "+i+" ok.."); 
							} catch (FTPDataTransferException e) {
								logger.error("ftp uploadFile()--> upload failed...",e);
								return success;
							} catch (FTPAbortedException e) {
								e.printStackTrace();
								logger.error("ftp uploadFile()--> upload failed...",e);
								return success;
							}
						}
					}
					System.out.println("upload files all..."); 
					success = true;
				} catch (NoSuchAlgorithmException e) {
					logger.error("ftp uploadFile()--> failed...",e);
					e.printStackTrace();
				}
			} catch (IOException e) {
				logger.error("ftp uploadFile()--> failed...",e);
				e.printStackTrace();
			} finally {
				if (client.isConnected()) {
					try {
						client.disconnect(true);
					} catch (IllegalStateException e) {
						logger.error("ftp uploadFile()--> disconnect failed...",e);
						e.printStackTrace();
					} catch (IOException e) {
						logger.error("ftp uploadFile()--> disconnect failed...",e);
						e.printStackTrace();
					} catch (FTPIllegalReplyException e) {
						logger.error("ftp uploadFile()--> disconnect failed...",e);
						e.printStackTrace();
					} catch (FTPException e) {
						logger.error("ftp uploadFile()--> disconnect failed...",e);
						e.printStackTrace();
					}
				}
			}
			return success;
		}
	  } 
