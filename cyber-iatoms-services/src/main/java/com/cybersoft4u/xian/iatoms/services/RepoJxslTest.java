package com.cybersoft4u.xian.iatoms.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import com.cybersoft4u.xian.iatoms.common.bean.dto.TestJXLS;

public class RepoJxslTest {
	public void export(){
		List<TestJXLS> testJXLSs = new ArrayList<TestJXLS>();
		TestJXLS jxls = null;
		for (int i = 0; i < 1; i++) {
			switch (i) {
			case 0:
				//jxls = new TestJXLS("富察蓉音", "26", "女", "皇后");
				jxls = new TestJXLS("張三", 2000, 15698);
				testJXLSs.add(jxls);
				break;
			case 2:
				//jxls = new TestJXLS("魏璎珞", "16", "女", "皇贵妃");
				jxls = new TestJXLS("李四", 3500, 11000);
				testJXLSs.add(jxls);
				break;
			case 3:
				//jxls = new TestJXLS("弘力", "27", "男", "皇上");
				jxls = new TestJXLS("王五", 2200, 21000);
				testJXLSs.add(jxls);
				break;
			case 1:
				//jxls = new TestJXLS("富察傅恒", "20", "男", "御前侍卫");
				jxls = new TestJXLS("朱六", 3100, 5000);
				testJXLSs.add(jxls);
				break;
			case 4:
				//jxls = new TestJXLS("明玉", "19", "女", "一等宫女");
				jxls = new TestJXLS("趙七", 2500, 16010);
				testJXLSs.add(jxls);
				break;
			default:
				break;
			}
		}
//		String templateFileName ="C:\\Users\\felixli.CYBERSOFT\\Desktop\\templateFileName.xlsx";	
		String destFileName ="/home/cybersoft/carrieduan/Test_20180823.xls";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("testJXLSs", testJXLSs);
		map.put("testJXLSs2", testJXLSs);
		//map.put("list2", null);
		XLSTransformer transformer = new XLSTransformer();
		//String templateFileName= "/home/cybersoft/carrieduan/go_test/trunk/cyber-iatoms-services/src/main/resources/com/cybersoft4u/xian/iatoms/services/download/Importtemplate/testJXL.xls";
		String templateFileName= "/home/cybersoft/carrieduan/go_test/trunk/cyber-iatoms-services/src/main/resources/com/cybersoft4u/xian/iatoms/services/download/Importtemplate/testSumJXL.xls";
		try {
			/*InputStream inputStream = new FileInputStream(templateFileName);
			HSSFWorkbook workBook = (HSSFWorkbook) transformer.transformXLS(inputStream, map);
			HSSFSheet sheet = workBook.getSheetAt(0);
			sheet.addMergedRegion(new CellRangeAddress(testJXLSs.size() + 3, testJXLSs.size() + 3, 0, testJXLSs.size()));
			HSSFCellStyle styleBorderThin= workBook.createCellStyle();
			styleBorderThin.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
			styleBorderThin.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
			styleBorderThin.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
			styleBorderThin.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
			styleBorderThin.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//设置背景颜色
			styleBorderThin.setFillForegroundColor(HSSFColor.LIME.index);
		    //solid 填充  foreground  前景色
			styleBorderThin.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			HSSFRow row=sheet.getRow(testJXLSs.size() + 3);
			row.createCell(0).setCellValue("學員信息(班級2)");
			row.getCell(0).setCellStyle(styleBorderThin);
			for(int i = 1; i < testJXLSs.size() + 1; i++) {
				row.createCell(i).setCellStyle(styleBorderThin);
			}
			OutputStream os = new FileOutputStream(destFileName);
	       	workBook.write(os);
	       	inputStream.close();
	        os.flush();*/

			//transformer.
			List<String> sheetNames = new ArrayList<String>();
			sheetNames.add("11");
			//InputStream xlsTemplateIO = new BufferedInputStream(getClass().getResourceAsStream(templateFileName));
			InputStream xlsTemplateIO = new BufferedInputStream(new FileInputStream(templateFileName));
			HSSFWorkbook workbook = (HSSFWorkbook) transformer.transformMultipleSheetsList(xlsTemplateIO, testJXLSs, sheetNames, "map", new HashMap(), 0);
		 OutputStream os = new BufferedOutputStream(new FileOutputStream(destFileName));   
		 workbook.write(os); 
			//transformer.transformXLS(templateFileName, map, destFileName);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void testSheets() throws ParsePropertyException, InvalidFormatException, IOException{
		 //获取Excel模板文件
	    String filePath = "/home/cybersoft/carrieduan/go_test/trunk/cyber-iatoms-services/src/main/resources/com/cybersoft4u/xian/iatoms/services/download/Importtemplate/testSheet.xls";
	    System.out.println("excel template file:" + filePath);
	    InputStream is = new FileInputStream(filePath);
	  //  FileInputStream is = inputStream;
	    //创建测试数据
	    Map<String, Object> map = new HashMap<String, Object>();
	    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	    Map<String, Object> map1 = new HashMap<String, Object>();
	    map1.put("name", "电视");
	    map1.put("price", "3000");
	    map1.put("desc", "3D电视机");
	    map1.put("备注", "中文测试");
	    Map<String, Object> map2 = new HashMap<String, Object>();
	    map2.put("name", "空调");
	    map2.put("price", "2000");
	    map2.put("desc", "变频空调");
	    map1.put("备注", "测试中文");
	    list.add(map1);
	    list.add(map2);
	    //map.put("list", list);
	 
	    ArrayList<List> objects = new ArrayList<List>();
	    objects.add(list);
	    objects.add(list);
	    objects.add(list);
	    objects.add(list);
	 
	    //sheet的名称
	    List<String> listSheetNames = new ArrayList<String>();
	    listSheetNames.add("1");
	    listSheetNames.add("2");
	    listSheetNames.add("3");
	    listSheetNames.add("4");
	 
	    //调用引擎生成excel报表
	    XLSTransformer transformer = new XLSTransformer();
	    Workbook workbook = transformer.transformMultipleSheetsList(is, objects, listSheetNames, "list", new HashMap(), 0);
	    workbook.write(new FileOutputStream("/home/cybersoft/carrieduan/Test_20180823.xls"));
	 

	}
	
	public static void main(String[] args) throws ParsePropertyException, InvalidFormatException, IOException {
		//new RepoJxslTest().export();
		new RepoJxslTest().testSheets();
	}
	
	/**
     * 通过递归得到某一路径下所有的文件的全路径,分装到list里面
     * 
     * @param filePath
     * @param filelist
     * @return
     */
    public static List<String> getFiles(String filePath, List<String> filelist) {

        File root = new File(filePath);
        if (!root.exists()) {
        } else {
            File[] files = root.listFiles();
            Arrays.sort(files, new RepoJxslTest.CompratorByLastModified());  
            for (File file : files) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), filelist);
                } else {
                    filelist.add(file.getName());
                }
            }
        }
        return filelist;
    }
     
    static class CompratorByLastModified implements Comparator<File> {  
        
        public int compare(File f1, File f2) {  
            long diff = f1.lastModified() - f2.lastModified();  
            if (diff < 0) {  
                   return 1;  
            } else if (diff == 0) {  
                   return 0;  
            } else {  
                  return -1;  
            }  
        }  
    }  
}
