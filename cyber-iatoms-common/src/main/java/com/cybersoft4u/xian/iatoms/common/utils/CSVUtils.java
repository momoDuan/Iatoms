package com.cybersoft4u.xian.iatoms.common.utils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.List;

import cafe.core.util.StringUtils;

public class CSVUtils {

	 public static void writeCsv(String[] heads,List<String> rows, String fileName) throws  Exception {
		 BufferedOutputStream bo = null;
		 try {
		 		
		        //byte[] bom = {(byte)0xFF, (byte)0xFE};
		       
		        bo = new BufferedOutputStream(new FileOutputStream(fileName));
		       // bo.write(bom);
		        bo.write(join(heads, "").getBytes("BIG5"));
		        bo.write("\r\n".getBytes("BIG5"));
		        for (String row : rows) {
		        	if (!StringUtils.hasText(row)) {
						continue;
					}
		            bo.write(row.getBytes("BIG5"));
		            bo.write("\r\n".getBytes("BIG5"));
		        }
		        bo.close();
			} catch (Exception e) {
				 bo.close();
			}
		 
	 }
	    public static String join(String[] strArr, String delim) {
	        StringBuilder sb = new StringBuilder();
	        for(String s : strArr) {
	        	if (!StringUtils.hasText(s)) {
	        		sb.append(delim);
				} else {
					sb.append(s);
				}
	            sb.append(delim);
	        }
	        String ret;
	        if (strArr.length > 1) {
	            ret = sb.substring(0, sb.length()-1);
	        }
	        else {
	            ret = sb.toString();
	        }
	        return ret;
	    }
	    public static  void main (String[] args) throws  Exception {
	        
	    }
	
}
