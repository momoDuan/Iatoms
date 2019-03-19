package com.cybersoft4u.xian.iatoms.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class UpdateJarInfo {
	/**
     * 读取jar包所有的文件内容，显示JAR文件内容列表 
     * @param jarFileName
     * @throws IOException
     */
    public static void readJARList(String jarFilePath) throws IOException { 
        // 创建JAR文件对象 
        JarFile jarFile = new JarFile(jarFilePath);  
        // 枚举获得JAR文件内的实体,即相对路径
        Enumeration en = jarFile.entries();  
        System.out.println("文件名\t文件大小\t压缩后的大小"); 
       // 遍历显示JAR文件中的内容信息 
        while (en.hasMoreElements()) {
            // 调用方法显示内容 
            process(en.nextElement());  
        }  
    }  
  
    // 显示对象信息
    private static void process(Object obj) {
        // 对象转化成Jar对象
        JarEntry entry = (JarEntry) obj;
        // 文件名称
        String name = entry.getName(); 
        // 文件大小 
        long size = entry.getSize(); 
        // 压缩后的大小 
        long compressedSize = entry.getCompressedSize(); 
        System.out.println(name + "\t" + size + "\t" + compressedSize);  
    }  
    
    /**
     * 读取jar包里面指定文件的内容
     * @param jarFileName jar包文件路径
     * @param fileName  文件名
     * @throws IOException
     */
    public static void readJarFile(String jarFilePath,String fileName) throws IOException{
        JarFile jarFile = new JarFile(jarFilePath);
      /*  Enumeration enu = jarFile.entries();
        while (enu.hasMoreElements()) {
        JarEntry element = (JarEntry) enu.nextElement();
                        String name = element.getName();
                        System.out.println(name+"/t");
        }*/
        //獲取fileName對應的信息。其中包含文件大小、名稱等信息
        JarEntry entry = jarFile.getJarEntry(fileName);
        //獲取指定文件的內容
        InputStream input = jarFile.getInputStream(entry);
        readFile(input);
        jarFile.close();
    }
    
    
    public static void readFile(InputStream input) throws IOException{
        InputStreamReader in = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(in);
        String line ;
        while((line = reader.readLine())!=null){
            System.out.println(line);
        }
        reader.close();
        input.close();
    }
    
    /** 
     * 读取流 
     *  
     * @param inStream 
     * @return 字节数组 
     * @throws Exception 
     */  
    public static byte[] readStream(InputStream inStream) throws Exception {  
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = -1;  
        while ((len = inStream.read(buffer)) != -1) {  
            outSteam.write(buffer, 0, len);  
        }  
        outSteam.close();  
        inStream.close();  
        return outSteam.toByteArray();  
    }  
    
    /**
     * 修改Jar包里的文件或者添加文件
     * @param jarFile jar包路径
     * @param entryName 要写的文件名
     * @param data   文件内容
     * @throws Exception 
     */
    public static void writeJarFile(String jarFilePath,String entryName,byte[] data) throws Exception{
        
        //1、首先将原Jar包里的所有内容读取到内存里，用TreeMap保存
        JarFile  jarFile = new JarFile(jarFilePath);
        //可以保持排列的顺序,所以用TreeMap 而不用HashMap
        TreeMap tm = new TreeMap();
        Enumeration es = jarFile.entries();
        while(es.hasMoreElements()){
            JarEntry je = (JarEntry)es.nextElement();
            byte[] b = readStream(jarFile.getInputStream(je));
            tm.put(je.getName(),b);
        }
        
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFilePath));
        Iterator it = tm.entrySet().iterator();
        boolean has = false;
        
        //2、将TreeMap重新写到原jar里，如果TreeMap里已经有entryName文件那么覆盖，否则在最后添加
        while(it.hasNext()){
            Map.Entry item = (Map.Entry) it.next();
            String name = (String)item.getKey();
            JarEntry entry = new JarEntry(name);
            jos.putNextEntry(entry);
            byte[] temp ;
            if(name.equals(entryName)){
                //覆盖
                temp = data;
                has = true ;
            }else{
                temp = (byte[])item.getValue();
            }
            jos.write(temp, 0, temp.length);
        }
        
        if(!has){
            //最后添加
            JarEntry newEntry = new JarEntry(entryName);
            jos.putNextEntry(newEntry);
            jos.write(data, 0, data.length);
        }
        jos.finish();
        jos.close();
        
    }
    
    
    /**
     * 测试案例
     * @param args
     * @throws Exception 
     */
    public static void main(String args[]) throws Exception{
    	if (args.length == 3) {
    		readJarFile(args[0], args[1]);
            
            String data = args[2];
            
            long start = System.currentTimeMillis();
            
            writeJarFile(args[0], args[1], data.getBytes());
            
            long end = System.currentTimeMillis();
            
            System.out.println(end-start);
            
            readJarFile(args[0], args[1]);
    	}
    }
}
