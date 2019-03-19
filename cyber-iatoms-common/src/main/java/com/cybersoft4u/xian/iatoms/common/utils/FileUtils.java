package com.cybersoft4u.xian.iatoms.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;


/**
 * Purpose: 處理文件上傳下載工具
 * @author candicechen
 * @since  JDK 1.5
 * @date   2014/9/15
 * @MaintenancePersonnel candicechen
 */
public class FileUtils {
	private static final Log log = LogFactory.getLog(FileUtils.class);
	public static final int BUFFER = 8192;
	public static final String ZIP_SUFFIX = ".zip";
	public static final String ZIP_TEMP_PATH = "C:\\CMS_FILE_TEMP\\";
	/**
	 * Purpose:文件上傳
	 * @param filePath:需要上傳Server的路徑
	 * @param inputFile:前段頁面選擇上傳的文件路徑
	 * @throws Exception:出錯時返回Exception
	 * @return void
	 */
	public static void upload(String filePath,String inputFile) throws Exception{
		upload(filePath,inputFile,null);
	}
	/**
	 * Purpose:文件上傳
	 * @param filePath:需要上傳Server的路徑
	 * @param inputFile:前段頁面選擇上傳的文件路徑
	 * @param inpjutFileName:需要寫入新文件的名稱
	 * @throws Exception:出錯時返回Exception
	 * @return void
	 */
	public static void upload(String filePath,String inputFile,String inputFileName) throws Exception{
		log.debug(" in FileUtils upload method filePath---@@@@@"+filePath);
		log.debug(" in FileUtils upload method inputFile---@@@@@"+inputFile);
		//上傳檔案路徑
		File fileFord = new File(filePath);
		//判斷儲存路徑是否存在，若不存在，則重新新建
		if (!fileFord.exists() || !fileFord.isDirectory()) {
			fileFord.mkdirs();
		}
		
		File file = new File(inputFile);
		String fileName = file.getName();
		String attachment = "";
		if(StringUtils.hasText(inputFileName)){
			attachment = filePath+inputFileName;
		}else{
			attachment = filePath+fileName;
		}
		File attachmentFile = new File(attachment);
		InputStream is = null;
		OutputStream fos = null;
		try {
			is = new FileInputStream(inputFile);
			fos = new FileOutputStream(attachmentFile);
			byte[] buffer=new byte[1024];
			int length=0;
			while(-1!=(length=(is.read(buffer))))
			{
				fos.write(buffer, 0, length);
			}
			fos.close();
		} catch (IOException e) {
			log.error("FileUtils upload Exception "+ e);
		} finally{
			try {
				if(is!=null){
					is.close();
				}				
			} catch (IOException e) {
				log.error("FileUtils upload Exception "+ e);
			}
			try {				
				if(fos!=null){
					fos.close();
				}
			} catch (IOException e) {
				log.error("FileUtils upload Exception "+ e);
			}
		}
	}
	/**
	 * Purpose:刪除server上已有的檔案
	 * @param inputfile:要刪除的檔案
	 * @throws Exception:出錯時拋出Exception
	 * @return void
	 */
	public static void removeFile(String inputfile) throws Exception{
		try{
			File file = new File(inputfile);
			if(file.exists()){
				if(file.isFile()){
					file.delete();
				}else if(file.isDirectory()){
					File files[] = file.listFiles(); 
					for(int i=0;i<files.length;i++){ 
						files[i].delete();
					}
					file.delete();
				}
			}
		}catch(Exception e){
			log.error("FileUtils removeFile Exception "+ e);
		}
	}
	
	/**
	 * Purpose:下載檔案
	 * @param response:上下文response請求
	 * @param filePath:需要下載的檔案
	 * @return void
	 */
	public static void download(HttpServletResponse response,String filePath) {
		InputStream ins = null;
		BufferedInputStream bins = null;
		OutputStream outs = null;
		BufferedOutputStream bouts = null;
		try {
			filePath = validate(filePath);
			File file = new File(filePath);
			String fileName = file.getName();
			if (file.exists()) {
				filePath = validate(filePath);
				ins = new FileInputStream(filePath);    
				bins = new BufferedInputStream(ins);// 放到缓冲流里面    
				outs = response.getOutputStream();// 获取文件输出IO流    
				bouts = new BufferedOutputStream(outs);    
				response.setContentType("application/x-download");// 设置response内容的类型
				response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));// 设置头部信息    
				int bytesRead = 0;    
				byte[] buffer = new byte[ins.available()];    
				//开始向网络传输文件流    
				while ((bytesRead = bins.read(buffer, 0, 1024)) != -1) {    
					bouts.write(buffer, 0, bytesRead);    
				}    
			} else{
				log.error("FileUtils downFile 文件不存在!!!!");
			}
			if( bouts != null )
				bouts.flush();// 這裡一定要調用flush()方法			
			if( ins != null )
				ins.close();			
			if( bins != null )
				bins.close();			
			if( outs != null )
				outs.close();				
			if( bouts != null )
				bouts.close();				
		} catch (Exception e) {
			log.error("FileUtils downFile Exception "+ e);
		}
		/*
		 * Security:Variable 'ins' is not closed within a "finally" block
		 * Security:Variable 'bins' is not closed within a "finally" block
		 * Security:Variable 'bouts' is not closed within a "finally" block
		 * Modified by Akuma 20141208
		 */
		finally{
			try{
				if (ins != null) ins.close();
				if (bins != null) bins.close();
				if (bouts != null) bouts.close();				
			}catch(Exception ee){
				log.error(" download failed !! " + ee );
			}
		}
	}
	
	/**
	 * Purpose: 文件名稱
	 * @author CarrieDuan
	 * @param request 請求對象
	 * @param response	響應對象
	 * @param filePath ：文件路徑
	 * @param fileName ：文件名稱
	 * @return void
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, InputStream inputStream, String fileName) {
		InputStream ins = null;
		BufferedInputStream bins = null;
		OutputStream outs = null;
		BufferedOutputStream bouts = null;
		try {
			if (inputStream != null) {
				 if (request.getHeader("User-Agent").indexOf("Trident") >= 0) {
					 fileName = URLEncoder.encode(fileName, "UTF-8"); 
					 fileName = StringUtils.replace(fileName, "+", "%20");
				    } else {  
				    	fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");  
				    }  
				OutputStream outputStream = response.getOutputStream();// 获取文件输出IO流    
				response.setContentType("application/x-download");// 设置response内容的类型
				response.setHeader("Content-disposition","attachment;filename="+ fileName);// 设置头部信息
				response.setContentType("application/vnd.ms-excel;charset=UTF-8");
				byte[] buffer = new byte[1024];
				//开始向网络传输文件流    
				int len = -1;
				while ((len = inputStream.read(buffer)) != -1) {    
					outputStream.write(buffer, 0, len);
				}
				outputStream.flush();
				outputStream.close();
			} else{
				log.error("FileUtils downFile 文件不存在!!!!");
			}
		} catch (Exception e) {
			log.error("FileUtils downFile Exception "+ e);
		} finally {
			if (bins != null){
				try {
					bins.close();
				} catch (IOException e) {
					log.error(" download failed !! " + e );
				}
			}
			if (bouts != null){
				try {
					bouts.close();
				} catch (IOException e) {
					log.error(" download failed !! " + e );
				}				
			} 
		}
	}
	
	
	/**
	 * Purpose: 下载文件
	 * @author wadelei
	 * @param response :上下文response請求
	 * @param filePath :文件路径
	 * @param fileName :文件名
	 * @return void
	 */
	public static void download(HttpServletResponse response,String filePath, String fileName) {
		FileInputStream fileInputStream = null;
		OutputStream outputStream = null;
		try {
			filePath = validate(filePath);
			File file = new File(filePath,fileName);
			if (file.exists() && file.isFile()) {
				fileInputStream = new FileInputStream(file);    
				outputStream = response.getOutputStream();// 获取文件输出IO流    
				response.setContentType("application/x-download");// 设置response内容的类型
				response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));// 设置头部信息
				response.setContentType("application/vnd.ms-excel;charset=UTF-8");
				byte[] buffer = new byte[1024];
				//开始向网络传输文件流    
				int len = -1;
				while ((len = fileInputStream.read(buffer)) != -1) {    
					outputStream.write(buffer, 0, len);
				}
			} else{
				log.error("FileUtils downFile 文件不存在!!!!");
			}
		} catch (Exception e) {
			log.error("FileUtils downFile Exception "+ e);
		}
		finally{
			try{
				if (fileInputStream != null) fileInputStream.close();
				if (outputStream != null) outputStream.close();
			}catch(Exception ee){
				log.error(" download failed !! " + ee );
			}
		}
	}
	
	/**
	 * Purpose: 下載文件 避免firefox、chrome、safari、opera瀏覽器下載時文件名亂碼
	 * @author KevinShen
	 * @param request	請求對象
	 * @param response	響應對象
	 * @param filePath	文件全路經
	 * @return void
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response,String filePath, String fileName) {
		InputStream ins = null;
		BufferedInputStream bins = null;
		OutputStream outs = null;
		BufferedOutputStream bouts = null;
		try {
			filePath = validate(filePath);
			File file = new File(filePath);
			if (file.exists()) {
				if (!StringUtils.hasText(fileName)) {
					fileName = file.getName();
				}
			    if (request.getHeader("User-Agent").indexOf("Trident") >= 0) {
			    	fileName = URLEncoder.encode(fileName, "UTF-8"); 
			    	fileName = StringUtils.replace(fileName, "+", "%20");
			    } else {  
			    	fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");  
			    } 
				filePath = validate(filePath);
				ins = new FileInputStream(filePath);    
				bins = new BufferedInputStream(ins);// 放到缓冲流里面    
				outs = response.getOutputStream();// 获取文件输出IO流    
				bouts = new BufferedOutputStream(outs);    
				response.setContentType("application/x-download");// 设置response内容的类型
				response.setHeader("Content-disposition","attachment;filename="+ fileName);// 设置头部信息   
				
				
				int bytesRead = 0;    
				byte[] buffer = new byte[1024];    
				//开始向网络传输文件流    
				while ((bytesRead = bins.read(buffer, 0, 1024)) != -1) {    
					bouts.write(buffer, 0, bytesRead);    
				}
			} else{
				log.error("FileUtils downFile 文件不存在!!!!");
			}
			if( bouts != null ){
				bouts.flush();// 這裡一定要調用flush()方法			
			}
		} catch (Exception e) {
			log.error("FileUtils downFile Exception "+ e);
		} finally {
			if (bins != null){
				try {
					bins.close();
				} catch (IOException e) {
					log.error(" download failed !! " + e );
				}
			}
			if (bouts != null){
				try {
					bouts.close();
				} catch (IOException e) {
					log.error(" download failed !! " + e );
				}				
			} 
		}
	}
	
	
	
	/**
	 * Purpose:上傳檔案
	 * @param:需要上傳至文件的路徑
	 * @param file:要上傳的檔案
	 * @return void
	 */
    public static File upload(String uploadFilePath,MultipartFile file){
    	return upload(uploadFilePath,file,null);
    }
    
    /**
     * Purpose::上傳檔案
     * @param uploadFilePath:需要上傳至文件的路徑
     * @param file:要上傳的檔案
     * @param outPutFileName:輸出的文件名稱
     * @return File
     */
    public static File upload(String uploadFilePath,MultipartFile file,String outPutFileName){
    	BufferedOutputStream buffStream = null;
    	FileOutputStream outPutStream = null;
    	if (!file.isEmpty()) {
            try {
                String fileName = "";
                byte[] bytes = file.getBytes();
                File fileDir = new File(uploadFilePath);
        		//判斷儲存路徑是否存在，若不存在，則重新新建
        		if (!fileDir.exists() || !fileDir.isDirectory()) {
        			fileDir.mkdirs();
        		}
                //讀取文件
        		if(StringUtils.hasText(outPutFileName)){
        			fileName = outPutFileName;
        		}else{
        			fileName = file.getOriginalFilename();
        		}
                File uploadFile = new File(uploadFilePath+fileName);
                //定義outputStream
                outPutStream = new FileOutputStream(uploadFile);
                buffStream = new BufferedOutputStream(outPutStream);
                buffStream.write(bytes);
                buffStream.close();
                return uploadFile;
            } catch (Exception e) {
            	log.error("FileUtils downFile Exception "+ e);
            }finally{
            	try {
            		if(outPutStream!=null){
            			outPutStream.close();
            		}
					if(buffStream!=null){
						buffStream.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.error("FileUtils downFile Exception "+ e);
					e.printStackTrace();
				}
            }
        }
    	return null;
    }
    /**
     * 
     * Purpose:
     * @param src
     * @param dest
     * @param newName
     * @throws Exception
     * @return void
     */
    public static void copyFile(String src ,String dest,String newName) throws Exception {
		File fileFord = new File(dest);
		//判斷儲存路徑是否存在，若不存在，則重新新建
		if (!fileFord.exists() || !fileFord.isDirectory()) {
			fileFord.mkdirs();
		}
    	if(StringUtils.hasText(src)){
    		File file = new File(src);
        	if(file.exists()&&file.isFile()){
        		InputStream is = null;
        	    OutputStream os = null;
        	    try {
        	        is = new FileInputStream(src);
        	        os = new FileOutputStream(dest+newName);
        	        byte[] buffer = new byte[1024];
        	        int length;
        	        while ((length = is.read(buffer)) > 0) {
        	            os.write(buffer, 0, length);
        	        }
        	        os.close();
        	    }catch(Exception e){
        	    	throw new Exception(e);
        	    }finally {
        			try {
        				if(is!=null){
        					is.close();
        				}				
        			} catch (IOException e) {
        				log.error("FileUtils copyFile Exception "+ e);
        			}
        			try {				
        				if(os!=null){
        					os.close();
        				}
        			} catch (IOException e) {
        				log.error("FileUtils copyFile Exception "+ e);
        			}
//        	    	if(os != null)
//        	    		os.close();
//        	    	if(is != null)
//        	    		is.close();
        	    }
    		}else{
    			log.error("文件不存在");
    		}
    	}else{
    		log.error("文件不存在");
    	}
	}
    /**
     * 
     * Purpose:使用輸入流複製文件
     * @param path 地址
     * @param dest 新地址
     * @param newName 新名稱
     * @throws Exception
     * @return void
     */
    public static void copyFileByInputStream(String path ,String dest,String newName) throws Exception {
		File fileFord = new File(dest);
		//判斷儲存路徑是否存在，若不存在，則重新新建
		if (!fileFord.exists() || !fileFord.isDirectory()) {
			fileFord.mkdirs();
		}
    	
		InputStream is=ReportExporter.class.getResourceAsStream(path);
        	    OutputStream os = null;
        	    if (is.available() != 0) {
        	    try {
        	       // is = new FileInputStream(is);
        	        os = new FileOutputStream(dest+newName);
        	        byte[] buffer = new byte[1024];
        	        int length;
        	        while ((length = is.read(buffer)) > 0) {
        	            os.write(buffer, 0, length);
        	        }
        	        //os.flush();
        	        os.close();
        	    }catch(Exception e){
        	    	throw new Exception(e);
        	    }finally {
        			try {
        				if(is!=null){
        					is.close();
        				}				
        			} catch (IOException e) {
        				log.error("FileUtils copyFile Exception "+ e);
        			}
        			try {				
        				if(os!=null){
        					os.close();
        				}
        			} catch (IOException e) {
        				log.error("FileUtils copyFile Exception "+ e);
        			}

        	    }
        	 }
    	
	}
    
    /**
     * Purpose:压缩文件
     * @param zipFile:压缩后的文件路徑
     * @param zipFileName:壓縮后文件名稱
     * @param srcPathNames:待壓縮文件
     * @throws RuntimeException
     * @throws IOException
     * @return void
     */
    public static void compress(String zipFile, String zipFileName,List<String> srcPathNames) throws RuntimeException, IOException{
    	log.debug(FileUtils.class.getName()+".compress zipFile ==== >"+zipFile);
    	log.debug(FileUtils.class.getName()+".compress zipFileName ==== >"+zipFileName);
    	ZipOutputStream zos = null;
		try {
			File fileFord = new File(zipFile);
			if (!fileFord.exists() || !fileFord.isDirectory()) {
				fileFord.mkdirs();
			}
			OutputStream os = new FileOutputStream(zipFile+zipFileName);
		    BufferedOutputStream bos = new BufferedOutputStream(os);
		    zos = new ZipOutputStream(bos);
		    //zos.setEncoding(System.getProperty("sun.jnu.encoding"));//设置文件名编码方式
		    //update by hungli 2015/10/15 zos.setEncoding(System.getProperty("sun.jnu.encoding"));這句設置為當前系統的編碼方式，當打成war包時會導致壓縮的文件名亂碼
		    zos.setEncoding(IAtomsConstants.ENCODE_UTF_8);
	    	List<File> fileList = new ArrayList<File>();
	    	File file = null;
	    	if(!CollectionUtils.isEmpty(srcPathNames)){
	    		for (String srcPathName : srcPathNames) {
	    			log.debug(FileUtils.class.getName()+".compress srcPathName ==== >"+srcPathName);
	    			file = new File(srcPathName);
	    			if (!file.exists()){
	    				log.error(FileUtils.class.getName()+".compress "+srcPathName+" 不存在！");
	    			}else{
	    				fileList.add(file);
	    			}
				}
	    		compressFiles(fileList, zos);
	    		zos.close();
	    	}else{
	    		log.debug(FileUtils.class.getName()+".compress 带压缩文件为空");
	    	}
		} catch (Exception e) {
			log.error(FileUtils.class.getName()+".compress is Failed!!!!",e);
			throw new RuntimeException(e);
		}finally {
		   if(zos != null) {
			    zos.closeEntry();
			    zos.close();
		   }
	   }
	}
    /**
     * Purpose:壓縮文檔
     * @author amandawang
     * @param fileLists:待壓縮文檔
     * @param out：輸出流
     * @throws RuntimeException
     * @return void
     */
    private static void compressWordFile(List<File> fileLists, OutputStream out) throws RuntimeException {
		try {
			if (!CollectionUtils.isEmpty(fileLists)) {
				log.debug(FileUtils.class.getName() + ".compressWordFile fileLists size ====》 " + fileLists.size());
				for (File file : fileLists) {
					log.debug(FileUtils.class.getName() + ".compressWordFile file ====》 " + file.getAbsolutePath() + file.getName());
					if (file.exists()) {
						compressFileByArchiveOutputStream(file, out);
					}
				}
			} else {
				log.debug(FileUtils.class.getName() + ".compressWordFile 待压缩文件为空");
			}
		} catch (Exception e) {
			log.error(FileUtils.class.getName() + ".compressWordFile is Failed!!!!", e);
			throw new RuntimeException(e);
		}
    }
	
	
	public static void compressPwd(String zipFilePath, String zipFileName,List<String> srcPathNames, String pwd) {
		try {
			//创建压缩文件
			ZipFile zipFile = new ZipFile(zipFilePath + zipFileName);
			ArrayList<File> files = new ArrayList<File>();
			File file = null;
			if(!CollectionUtils.isEmpty(srcPathNames)){
				for (String srcPathName : srcPathNames) {
					log.debug(FileUtils.class.getName()+".compressPwd srcPathName ==== >"+srcPathName);
					file = new File(srcPathName);
					if (!file.exists()){
						log.error(FileUtils.class.getName()+".compressPwd "+srcPathName+" 不存在！");
					}else{
						files.add(file);
					}
				}
			}else{
				log.debug(FileUtils.class.getName()+".compressPwd 待压缩文件为空");
			}
			//设置压缩文件参数
			ZipParameters parameters = new ZipParameters();
			//设置压缩方法
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			// 压缩级别  
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			//设置压缩文件加密
			parameters.setEncryptFiles(true);
			//设置加密方法
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
			//设置密码
			parameters.setPassword(pwd);
			//添加文件到压缩文件
			zipFile.addFiles(files, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * Purpose:压缩文件 --以MS950方式
     * @param zipFile:压缩后的文件路徑
     * @param zipFileName:壓縮后文件名稱
     * @param srcPathNames:待壓縮文件
     * @throws RuntimeException
     * @throws IOException
     * @return void
     */
    public static void compressByMs950(String zipFile, String zipFileName,List<String> srcPathNames) throws RuntimeException, IOException{
    	log.debug(FileUtils.class.getName()+".compressByMs950 zipFile ==== >"+zipFile);
    	log.debug(FileUtils.class.getName()+".compressByMs950 zipFileName ==== >"+zipFileName);
    	ZipOutputStream zos = null;
		try {
			File fileFord = new File(zipFile);
			if (!fileFord.exists() || !fileFord.isDirectory()) {
				fileFord.mkdirs();
			}
			OutputStream os = new FileOutputStream(zipFile+zipFileName);
		    BufferedOutputStream bos = new BufferedOutputStream(os);
		    zos = new ZipOutputStream(bos);
		    zos.setEncoding("MS950");
	    	List<File> fileList = new ArrayList<File>();
	    	File file = null;
	    	if(!CollectionUtils.isEmpty(srcPathNames)){
	    		for (String srcPathName : srcPathNames) {
	    			log.debug(FileUtils.class.getName()+".compressByMs950 srcPathName ==== >"+srcPathName);
	    			file = new File(srcPathName);
	    			if (!file.exists()){
	    				log.error(FileUtils.class.getName()+".compressByMs950 "+srcPathName+" 不存在！");
	    			}else{
	    				fileList.add(file);
	    			}
				}
	    		compressFiles(fileList, zos);
	    		zos.close();
	    	}else{
	    		log.debug(FileUtils.class.getName()+".compressByMs950 待压缩文件为空");
	    	}
		} catch (Exception e) {
			log.error(FileUtils.class.getName()+".compressByMs950 is Failed!!!!",e);
			throw new RuntimeException(e);
		}finally {
		   if(zos != null) {
			    zos.closeEntry();
			    zos.close();
		   }
	   }
	}
  
    /**
     * Purpose:压缩文件
     * @param zipFile:压缩后的文件路徑
     * @param zipFileName:壓縮后文件名稱
     * @param srcPathNames:待壓縮文件
     * @throws RuntimeException
     * @throws IOException
     * @return void
     */
    public static void compressByEncode(HttpServletRequest request, String zipFile, String zipFileName,List<String> srcPathNames) throws RuntimeException, IOException{
    	log.debug(FileUtils.class.getName()+".compressByEncode zipFile ==== >"+zipFile);
    	log.debug(FileUtils.class.getName()+".compressByEncode zipFileName ==== >"+zipFileName);
    	ZipOutputStream zos = null;
		try {
			File file = null;
			File newFile = null;
			if (CollectionUtils.isEmpty(srcPathNames) || srcPathNames.get(0)==null) {
				log.error(FileUtils.class.getName()+".compressByEncode is Failed --file is not exists ");
				return;
			}
			file = new File(srcPathNames.get(0));
			File fileFord = file.getParentFile();
			if (!fileFord.exists()) {
				throw new RuntimeException(zipFile + " is not exists");
			}
			Project prj = new Project();
			Zip zip = new Zip();
			zip.setProject(prj);
			zip.setDestFile(new File(zipFile + zipFileName));
			FileSet fileSet = new FileSet();
			fileSet.setProject(prj);
			fileSet.setDir(fileFord);
			zip.setEncoding("MS950");
			for (String srcPathName : srcPathNames) {
    			log.debug(FileUtils.class.getName()+".compressByEncode srcPathName ==== >"+srcPathName);
    			file = new File(srcPathName);
    			file.createNewFile();
    			newFile = new File(new String(file.getName().getBytes(), "MS950"));
    			file.renameTo(newFile);
			}
			fileSet.setIncludes("*.doc");
			zip.addFileset(fileSet);
			zip.execute();
			if (newFile.exists()) {
				log.debug(FileUtils.class.getName()+".compressByEncode newFile by MS950 ==== >" + new String(newFile.getName().getBytes(), "UTF-8"));
			}
		} catch (Exception e) {
			log.error(FileUtils.class.getName()+".compressByEncode is Failed!!!!",e);
			throw new RuntimeException(e);
		}finally {
			if(zos != null) {
			    zos.closeEntry();
			    zos.close();
		   }
	   }
	}
    /**
     * Purpose:將多個文件壓縮為1個
     * @param fileLists:帶壓縮文件集合
     * @param out:ZIP輸出流
     * @throws RuntimeException
     * @return void
     */
	private static void compressFiles(List<File> fileLists, ZipOutputStream out) throws RuntimeException {
		try{

			if (!CollectionUtils.isEmpty(fileLists)){
				log.debug(FileUtils.class.getName()+".compressFiles fileLists size ====》 "+fileLists.size());
				for (File file : fileLists) {
					log.debug(FileUtils.class.getName()+".compressFiles file ====》 "+file.getAbsolutePath()+file.getName());
					if(file.exists()){
						compressFile(file, out, file.getParent());
					}
				}
			}else{
				log.debug(FileUtils.class.getName()+".compressFiles 带压缩文件为空");
			}
		}catch (Exception e) {
			log.error(FileUtils.class.getName()+".compressFiles is Failed!!!!",e);
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * Purpose:使用ZipArchiveOutputStream壓縮單個文檔
	 * @author amandawang
	 * @param file：文件
	 * @param out：輸出流
	 * @return void
	 */
	private static void compressFileByArchiveOutputStream(File file, OutputStream out) {
		log.debug(FileUtils.class.getName() + "compressFileByArchiveOutputStream  file=" + file);
		try {
			ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
			if (os instanceof ZipArchiveOutputStream) {
				((ZipArchiveOutputStream) os).setEncoding("GBK");
			}
			os.putArchiveEntry(new ZipArchiveEntry(file, file.getName()));
			FileInputStream fileInputStream = new FileInputStream(file);
			IOUtils.copy(fileInputStream, os);
			fileInputStream.close();
			os.closeArchiveEntry();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ArchiveException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:压缩单个文件
	 * @param file:待压缩文件
	 * @param out:输出的文件流
	 * @param basedir:压缩存放的位置
	 * @return void
	 * @throws IOException 
	 */
    private static void compressFile(File file, ZipOutputStream out, String baseDir) throws IOException {
    	BufferedInputStream bis = null;
		try {
			log.debug(FileUtils.class.getName()+".compressFile file Name ====》 "+file.getAbsolutePath()+file.getName());
			if (!file.exists()) {
				return;
			}
			bis = new BufferedInputStream(new FileInputStream(file));
			String pathName = file.getPath().substring(baseDir.length() + 1);
			out.putNextEntry(new ZipEntry(pathName));
			int count = 0;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data)) != -1) {
				out.write(data, 0, count);
			}
		} catch (Exception e) {
			log.error(FileUtils.class.getName()+".compressFile is Failed!!!!",e);
			throw new RuntimeException(e);
		}finally {
			if(bis != null) {
				bis.close();
			}
		}
	}
    
    public static String validate(String filePath) {
    	log.debug(" filePath >>> " + filePath );
		return filePath;    	
    }
    public static void contentToTxt(String filePath, String count) {  
        try {  
            File f = new File(filePath); 
            File parentFile = f.getParentFile();
            if(!parentFile.exists()){
            	parentFile.mkdirs();
            }
            if (!f.exists()) {  
            	f.createNewFile();// 不存在则创建
            }
            FileOutputStream fos=new FileOutputStream(f);
            fos.write(count.getBytes());
            fos.close();
           /* BufferedReader input = new BufferedReader(new FileReader(f));  
  
            while ((str = input.readLine()) != null) {  
                s1 += str + "\n";  
            }  
            input.close();  
            s1 += content;  
  
            BufferedWriter output = new BufferedWriter(new FileWriter(f));  
            output.write(s1);  
            output.close(); */ 
        } catch (Exception e) {  
            e.printStackTrace();  
  
        }  
    }
    
    public static void contentToTxt(String filePath, List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs, String code) {  
        try {
            File f = new File(filePath);
            File parentFile = f.getParentFile();
            if(parentFile.exists()){
            	FileUtils.removeFile(filePath);
            }
            parentFile.mkdirs();
            if (!f.exists()) {  
            	f.createNewFile();// 不存在则创建  
            }
            FileOutputStream fileOutputStream = new FileOutputStream(f, true);
           /* FileOutputStream fos=new FileOutputStream(f);*/
            OutputStreamWriter outputStreamWriter = null;
            if (StringUtils.hasText(code)) {
            	outputStreamWriter = new OutputStreamWriter(fileOutputStream, code);
            } else {
            	outputStreamWriter = new OutputStreamWriter(fileOutputStream) ;
            }
            BufferedWriter out = new BufferedWriter(new BufferedWriter(outputStreamWriter));
            SrmCaseHandleInfoDTO caseHandleInfoDTO = null;
            if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
            	String data = null;
            	for (int i = 0; i < caseHandleInfoDTOs.size(); i++) {
            		caseHandleInfoDTO = caseHandleInfoDTOs.get(i);
            		if (i == caseHandleInfoDTOs.size() - 1) {
            			data = caseHandleInfoDTO.getDescription().substring(0, caseHandleInfoDTO.getDescription().length() - 1);
            		} else {
            			data = caseHandleInfoDTO.getDescription();
            		}
            		out.write(data);
    			}
            } else {
            	out.write("");
            }
            out.close();
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
}