package com.cybersoft4u.xian.iatoms.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;

/**
 * Purpose: 通用驗證類 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年7月26日
 * @MaintenancePersonnel evanliu
 */
public class ValidateUtils {
	
	/**
	 * Purpose:檢核字串長度範圍
	 * @author evanliu
	 * @param str: 要檢核的字符串
	 * @param min:最小長度
	 * @param max:最大長度
	 * @return Boolean:檢核結果
	 */
	public static boolean length(String str, int min, int max){		
		if (!StringUtils.hasText(str)) {
			return false;
		}
		int len = str.length();
		if (len > max || len < min) {
			return false;
		}
        return true; 
	}
	/**
	 * Purpose:檢核字串長度範圍
	 * @author evanliu
	 * @param str: 要檢核的字符串
	 * @param min:最小長度
	 * @param max:最大長度
	 * @return Boolean:檢核結果
	 */
	public static boolean equalsLength(String str, int length){		
		if (!StringUtils.hasText(str)) {
			return false;
		}
		int len = str.length();
		if (len != length) {
			return false;
		}
        return true; 
	}
	/**
	 * Purpose:檢查字串是否只含有英文和數字
	 * @author evanliu
	 * @param str: 要檢核的字符串
	 * @return Boolean:檢核結果
	 */
	public static boolean numberOrEnglish(String str){
		String regex = "[a-zA-Z0-9]+"; 
		return Pattern.matches(regex, str); 
	}
	
	/**
	 * Purpose:檢查時間格式是否為(HH:mm)
	 * @author CrissZhang
	 * @param str ：要檢核的字符串
	 * @return Boolean : 檢核結果
	 */
	public static boolean valiDataTime(String str){
		String regex = "^([0-1]{1}[0-9]|2[0-3]):([0-5][0-9])$"; 
		return Pattern.matches(regex, str); 
	}
	
	/**
	 * Purpose:檢查是否為一位小數的浮點數且小數部分為0
	 * @author CrissZhang
	 * @param str ：要檢核的字符串
	 * @return Boolean : 檢核結果
	 */
	public static boolean oneBitFloat(String str){
	//	String regex = "^[1-9]d*$";
		String regex = "^[0-9]+(.0{1})?$";
		return Pattern.matches(regex, str);
	}
	
	/**
	 * Purpose:核檢正整數
	 * @author CarrieDuan
	 * @param str ：要檢核的字符串
	 * @return Boolean ： 檢核結果
	 */
	public static boolean number(String str) {
		String regex = "^[1-9]\\d*$";
		return Pattern.matches(regex, str);
	}
	
	/**
	 * Purpose: 比較time2是否大於time1
	 * @author CarrieDuan
	 * @param time1 ：要檢核的字符串 
	 * @param time2 ：要檢核的字符串
	 * @return Boolean ： 檢核結果
	 */
	public static boolean compareTimeSize(String time1, String time2) {
		String[] param1 = time1.split(":");
		String[] param2 = time2.split(":");
		if (param1[0].equals("23")  && param2[0].equals("00")){
			return true;
			
		} else {
			if (Integer.valueOf(param2[0]) < Integer.valueOf(param1[0])){
				return false;
			} else if (Integer.valueOf(param2[0]) == Integer.valueOf(param1[0])){
				if (Integer.valueOf(param2[1]) < Integer.valueOf(param1[1])){
					return false;
				} else {
					return true;
				} 
			} else {
				return true;
			}
		}
	}
	/**
	 * Purpose:只能輸入英數字或英文符號或英數符號
	 * @author CrissZhang
	 * @param str : 要檢核的字符串
	 * @param isHaveEnglish : 是否必須含有英文
	 * @return Boolean ： 檢核結果
	 */
	public static boolean inputCharacter(String str, boolean isHaveEnglish) {
		int letter = 0;
		int number = 0;
		int character = 0;
		int space = 0;
		String letterStr = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
		String numberStr = "0123456789";
		String characterStr = "~!@#$%^&*()_+`-=/*\\':'\"\'<>,.?/";
		String spaceStr = " ";
		for (int i = 0; i < str.length(); i++) {
			String temp = str.substring(i, i+1);
			if (letterStr.indexOf(temp) >= 0) {
				letter++;
			} else if (numberStr.indexOf(temp) >= 0) {
				number++;
			} else if (characterStr.indexOf(temp) >= 0) {
				character++;
			} else if (spaceStr.indexOf(temp) >= 0){
				space++;
			} else {
				space++;
			}
		}
		if (isHaveEnglish) {
			if ((letter > 0) && (number > 0 || character > 0) && space == 0) {
				return true;
			} else {
				return false;
			}
		} else {
			if ((letter > 0 || number > 0 || character > 0) && space == 0) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Purpose:字符串不可為鍵盤上的字母順序(asdf)
	 * @author CrissZhang
	 * @param str : 要檢核的字符串
	 * @return Boolean ： 檢核結果
	 */
	public static boolean inputCharacterOrder(String str) {
		String firstLine = "qwertyuiop";
		String secondLine = "asdfghjkl";
		String thirdLine = "zxcvbnm";
		String firstLineReverse = new StringBuffer(firstLine).reverse().toString();
		String secondLineReverse = new StringBuffer(secondLine).reverse().toString();
		String thirdLineReverse = new StringBuffer(thirdLine).reverse().toString();
			if (str.length() >= 4) {
				for (int i = 0; i <= str.length() - 4; i++) {
					String temp = str.substring(i, i + 4);
					if (firstLine.indexOf(temp) >= 0 || firstLineReverse.indexOf(temp) >= 0 
							|| firstLine.toUpperCase().indexOf(temp) >= 0 || firstLineReverse.toUpperCase().indexOf(temp) >= 0
							|| secondLine.indexOf(temp) >= 0 || secondLineReverse.indexOf(temp) >= 0 
							|| secondLine.toUpperCase().indexOf(temp) >= 0 ||secondLineReverse.toUpperCase().indexOf(temp) >= 0
							|| thirdLine.indexOf(temp) >= 0 || thirdLineReverse.indexOf(temp) >= 0 
							|| thirdLine.toUpperCase().indexOf(temp) >= 0 ||thirdLineReverse.toUpperCase().indexOf(temp) >= 0){
						return false;
					}
				}
			}
		return true;
	}
	
	/**
	 * Purpose:字符串不可為連續的數字(1234)
	 * @author CrissZhang
	 * @param str : 要檢核的字符串
	 * @return Boolean ： 檢核結果
	 */
	public static boolean inputNumberOrder(String str) {
		String number = "0123456789";
		String numberReverse = new StringBuffer(number).reverse().toString();
			if (str.length() >= 4) {
				for (int i = 0; i <= str.length() - 4; i++) {
					String temp = str.substring(i, i + 4);
					if (number.indexOf(temp) >= 0 || numberReverse.indexOf(temp) >= 0) {
						return false;
					}
				}
			}
		return true;
	}
	
	/**
	 * Purpose:字符串不可使用重複的字元(不可重複三次)
	 * @author CrissZhang
	 * @param str : 要檢核的字符串
	 * @return Boolean ： 檢核結果
	 */
	public static boolean inputCharRepeat(String str) {
		char preChar;
		int time = 0;
		char currentChar;
		for(int i = 0; i < str.length(); i++) {
			for(int j = 0; j < str.length(); j++){
				currentChar = str.charAt(i);
				preChar = str.charAt(j);
				if (currentChar == preChar) {
					time ++;
					if (time >= 3) {
						return false;
					}
				} 
			}
			time = 0;
		}
		return true;
	}
	
	/**
	 * Purpose:不能與已知的字符串相同或部分相同
	 * @author CrissZhang
	 * @param str : 要比較的字符串
	 * @param input : 輸入的字符串
	 * @return Boolean ： 檢核結果
	 */
	public static boolean inputSameAsStr(String str, String input) {
		//全都轉化為小寫
		String strLower = str.toLowerCase();
		String inputLower = input.toLowerCase();
		String strReverseLower = new StringBuffer(strLower).reverse().toString();
		boolean same = true;
		int strLength = str.length();
		int inputLength = input.length();
		if (strLength >= 3 && inputLength >= 3) {
			String newStr;
			for (int j = 0; j < inputLength; j ++) {
				newStr = inputLower.substring(j,j+3);
				if (strLower.indexOf(newStr) >= 0 || strReverseLower.indexOf(newStr) >= 0) {
					same = false;
					break;
				}
				if (j + 3 >= inputLength) {
					break;
				}
			}
		}
		return same;
	}
	/**
	 * Purpose:驗證台灣行動電話
	 * @author CrissZhang
	 * @param str : 要檢核的字符串
	 * @return Boolean ： 檢核結果
	 */
	public static boolean twMobile(String str){
		String regex = "^(09)\\d{8}$"; 
		return Pattern.matches(regex, str); 
	}
	/**
	 * Purpose:驗證email
	 * @author CrissZhang
	 * @param str : 要檢核的字符串
	 * @return Boolean ： 檢核結果
	 */
	public static boolean email(String str){
		String regex = "^[a-zA-Z0-9_+.-]+\\@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,4}$"; 
		return Pattern.matches(regex, str); 
	}
	/**
	 * Purpose:核檢多筆mail
	 * @author CarrieDuan
	 * @param str: 要檢核的字符串
	 * @return boolean： 檢核結果
	 */
	public static boolean manyEmail(String str){
		if (StringUtils.hasText(str)) {
			String[] mail = str.split(IAtomsConstants.MARK_SEMICOLON);
			String regex = "^[a-zA-Z0-9_+.-]+\\@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,4}$"; 
			for (String info : mail) {
				if (!Pattern.matches(regex, info)) {
					return Boolean.FALSE;
				}
			}
			return Boolean.TRUE;
		} else {
			return Boolean.TRUE;
		}
		
	}
	
	/**
	 * Purpose:只能輸入中文
	 * @author CrissZhang
	 * @param str : 要檢核的字符串
	 * @return Boolean ： 檢核結果
	 */
	public static boolean onlyAllowChinese(String str){
		String regex = "^[\u4e00-\u9fa5]{0,}$"; 
		return Pattern.matches(regex, str); 
	}
	
	/**
	 * Purpose:驗證電話格式
	 * @author CarrieDuan
	 * @param str : 要檢核的字符串
	 * @return Boolean： 檢核結果
	 */
	public static boolean phone(String str) {
		String regex = "^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?$"; 
		return Pattern.matches(regex, str); 
	}
	/**
	 * Purpose:驗證電話格式
	 * @author CarrieDuan
	 * @param str : 要檢核的字符串
	 * @return Boolean： 檢核結果
	 */
	public static boolean twPhone(String str) {
		String regex = "^(09)\\d{8}$"; 
		return Pattern.matches(regex, str); 
	}
	
	/**
	 * Purpose:驗證數字
	 * @author amandawang
	 * @param str : 要檢核的字符串
	 * @return Boolean： 檢核結果
	 */
	public static boolean numberOnly(String str) {
		//String regex = "^[+\\-]?\\d+(.\\d+)?$";   /^\+?[1-9][0-9]*$/i.test(value) ^[1-9]\\d*$
		String regex = "^[0-9]\\d*$";
		return Pattern.matches(regex, str); 
	}
	/**
	 * Purpose:驗證字符串真實長度(處理數據庫varchar類型)
	 * @author CrissZhang
	 * @param str : 要檢核的字符串
	 * @return Boolean： 檢核結果
	 */
	public static boolean realLength(String str, Integer min, Integer max){
		if (!StringUtils.hasText(str)) {
			return false;
		}
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < str.length(); i++) {
		/* 获取一个字符 */
			String temp = str.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		int len = valueLength;
		if (len > max || len < min) {
			return false;
		}
        return true; 
	}
	
	/**
	 * Purpose: 核檢日期格式 YYYY/MM/DD
	 * @author CarrieDuan
	 * @param str :需要核檢的日期
	 * @return Boolean
	 */
	public static boolean checkDate(String str) {
		String regex = "^[0-9]{4}[/][0-9]{2}[/][0-9]{2}$"; 
		if (Pattern.matches(regex, str)) {
			String[] dates = str.split("/");
			if (Integer.valueOf(dates[1]) > 12 || Integer.valueOf(dates[1]) < 1) {
				return false;
			} 
			if (Integer.valueOf(dates[1]) == 1 || Integer.valueOf(dates[1]) == 3 || Integer.valueOf(dates[1]) == 5 
					|| Integer.valueOf(dates[1]) == 7 || Integer.valueOf(dates[1]) == 8 
						|| Integer.valueOf(dates[1]) == 10 || Integer.valueOf(dates[1]) == 12) {
				if (Integer.valueOf(dates[2]) > 31 || Integer.valueOf(dates[2]) < 1) {
					return false;
				} 
			} else if (Integer.valueOf(dates[1]) == 2) {
				if ((Integer.valueOf(dates[0])%4==0 && Integer.valueOf(dates[0])%100 != 0) || Integer.valueOf(dates[0])%400 == 0) {
					if (Integer.valueOf(dates[2]) > 29 || Integer.valueOf(dates[2]) < 1) {
						return false;
					}
				} else {
					if (Integer.valueOf(dates[2]) > 28 || Integer.valueOf(dates[2]) < 1) {
						return false;
					}
				}
			} else {
				if (Integer.valueOf(dates[2]) > 30 || Integer.valueOf(dates[2]) < 1) {
					return false;
				} 
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Purpose:檢核DB varchar類型字串長度範圍
	 * @author CrissZhang
	 * @param str: 要檢核的字符串
	 * @param min:最小長度
	 * @param max:最大長度
	 * @return Boolean:檢核結果
	 */
	public static boolean varcharLength(String str, int min, int max){
		if (!StringUtils.hasText(str)) {
			return false;
		}
		int len = 0;
		// 字符串長度
		int strLength = str.length();
		// 按編碼求字符長度
		int strCodeLength = 0;
		try {
			strCodeLength = str.getBytes(IAtomsConstants.ENCODE_GBK).length;
		} catch (UnsupportedEncodingException e) {
			strCodeLength = 0;
		}
		// 字符串長度與字符長度不等
		if(strLength != strCodeLength){
			len = strCodeLength;
		} else {
			len = strLength;
		}
		if (len > max || len < min) {
			return false;
		}
        return true; 
	}
}
