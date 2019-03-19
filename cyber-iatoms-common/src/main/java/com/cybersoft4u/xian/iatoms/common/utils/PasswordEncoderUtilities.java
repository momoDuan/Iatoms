package com.cybersoft4u.xian.iatoms.common.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cafe.core.util.StringUtils;

import com.cybersoft4u.framework.security.crypto.CryptoUtil;
import com.cybersoft4u.framework.security.crypto.KeyFileProvider;

/**
 * Purpose: AES Password Encoder
 * @author: janeyan
 * @since: JDK 1.6
 * @date: 2015-10-12
 * @MaintenancePersonnel: BoltenDeng
 */
public class PasswordEncoderUtilities {

	protected final static Logger logger = LoggerFactory.getLogger(PasswordEncoderUtilities.class);
	
	public static final String DEFAULT_KEY = null;	
	protected static SecretKey secretKey;

	/**
	 * Constructor--.
	 */
	public PasswordEncoderUtilities() {
	
	}
	
	public static void init() {
		try {
			KeyFileProvider keyStore = new KeyFileProvider("KeyFile_Value.properties");
			secretKey = keyStore.getEncryptKey();
		} catch (InvalidKeyException e) {
			logger.error("", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	
	/**
	 * AES加密为base 64 code
	 * @param content 待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的base 64 code
	 * @throws Exception
	 */
	
	public static String encodePassword(String content){
		try {
			init();
			return CryptoUtil.encrypt(secretKey, content);
		} catch (Exception e) {
			logger.error("", e);
		}
		return "";
	}
	
	/**
	 * 将base 64 code AES解密
	 * @param encryptStr 待解密的base 64 code
	 * @param decryptKey 解密密钥
	 * @return 解密后的string
	 * @throws Exception
	 */
	public static String decodePassword(String encryptStr){
		try {
			init();
			return CryptoUtil.decrypt(secretKey, encryptStr);
		} catch (Exception e) {
			logger.error("", e);
		}
		return "";
	}
	  private static final String defaultCharset = "UTF-8";
      private static final String KEY_AES = "AES";
    /**
     * 加密
     *
     * @param data 需要加密的内容
     * @param key 加密密码
     * @return
     */
    public static String encrypt(String data, String key) {
        return doAES(data, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @param key 解密密钥
     * @return
     */
    public static String decrypt(String data, String key) {
        return doAES(data, key, Cipher.DECRYPT_MODE);
    }

    /**
     * 加解密
     *
     * @param data 待处理数据
     * @param password  密钥
     * @param mode 加解密mode
     * @return
     */
    private static String doAES(String data, String key, int mode) {
        try {
            if ((!StringUtils.hasText(data)) || (!StringUtils.hasText(key))) {
                return null;
            }
            //判断是加密还是解密
            boolean encrypt = mode == Cipher.ENCRYPT_MODE;
            byte[] content;
            //true 加密内容 false 解密内容
            if (encrypt) {
                content = data.getBytes(defaultCharset);
            } else {
                content = parseHexStr2Byte(data);
            }
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_AES);
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            kgen.init(256, new SecureRandom(key.getBytes()));
            //3.产生原始对称密钥
            SecretKey secretKey = kgen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] enCodeFormat = secretKey.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, KEY_AES);
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(KEY_AES);// 创建密码器
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(mode, keySpec);// 初始化
            byte[] result = cipher.doFinal(content);
            if (encrypt) {
                //将二进制转换成16进制
                return parseByte2HexStr(result);
            } else {
                return new String(result, defaultCharset);
            }
        } catch (Exception e) {
            logger.error("AES Abnormity processing of ciphertext ", e);
        }
        return null;
    }
    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
	/** 
	 * 使用指定的字符串生成秘钥 
	 */  
	public static String getKeyByPass(String key, int number){  
	    String s = null;
	    try {    
	        KeyGenerator kg = KeyGenerator.getInstance("AES");    
	       // kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256    
	        //SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以生成的秘钥就一样。  
	        kg.init(number, new SecureRandom(key.getBytes()));  
	        SecretKey sk = kg.generateKey();    
	        byte[] b = sk.getEncoded();    
	        s = byteToHexString(b);    
	        System.out.println(s);    
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();    
	        System.out.println("没有此算法。");
	    }
	    return s;
	} 
	/** 
	 * byte数组转化为16进制字符串 
	 * @param bytes 
	 * @return 
	 */  
	public static String byteToHexString(byte[] bytes){       
	    StringBuffer sb = new StringBuffer();       
	    for (int i = 0; i < bytes.length; i++) {       
	         String strHex=Integer.toHexString(bytes[i]);   
	         if(strHex.length() > 3){       
	                sb.append(strHex.substring(6));       
	         } else {    
	              if(strHex.length() < 2){    
	                 sb.append("0" + strHex);    
	              } else {    
	                 sb.append(strHex);       
	              }       
	         }    
	    }    
	   return  sb.toString();       
	}    
	
	/**
	 * 修改系統加密機制
	 * @param key 需生成秘钥的字符串
	 * @param jarUrl 需修改jar的Url
	 * @throws Exception
	 */
	private static void changeSecurityjar (String key, String jarUrl, String jarUrl2) throws Exception {
		if (StringUtils.hasText(key) && StringUtils.hasText(jarUrl)) {
			String ss = getKeyByPass(key, 128);
			changeSecurityjar(ss, jarUrl);
			changeSecurityjar(ss, jarUrl2);
		}
		
	}
	
	private static void changeSecurityjar (String key, String jarUrl) throws Exception {
		if (StringUtils.hasText(key) && StringUtils.hasText(jarUrl)) {
	    	String securityValue[] = {jarUrl,"KeyFile_Value.properties", key};
	    	UpdateJarInfo.main(securityValue);
	    	String securityKeyValue[] = {jarUrl,"KeyFile_KeyValue.properties", "MasterKey=" + key};
	    	UpdateJarInfo.main(securityKeyValue);
		}
	}
	
	public static void main(String[] args) throws Exception {
		//驗證直接存儲密鑰的方式
//		KeyFileProvider keyFileProvider1 = new KeyFileProvider("KeyFile_Value.properties");
//		SecretKey secretKey1 = keyFileProvider1.getEncryptKey();
		String plaintext = "12345!@#$%^&*()";
        String ciphertext = PasswordEncoderUtilities.encodePassword(plaintext);
//		String ciphertext = "ATgyFO3ggBB4Z9nbDbhrYkZd1LiREIotXwa6fEn2Kjk="; 
    	String result = PasswordEncoderUtilities.decodePassword(ciphertext);
////驗證以鍵值對存儲的方式
//		KeyFileProvider keyFileProvider2 = new KeyFileProvider("KeyFile_KeyValue.properties","MasterKey");
//		SecretKey secretKey2 = keyFileProvider2.getEncryptKey();
//		String plaintext1 = "test123456";	
//        String ciphertext1 = CryptoUtil.encrypt(secretKey2,plaintext1);
//    	String result1 = CryptoUtil.decrypt(secretKey2,ciphertext1);
    	String s = null;
    	getKeyByPass("127.0.0.1"+"CMS", 256);
    	s= "mjV0bw/S0Z7qSPydGc73DDYzjHPIvrkDUtUcCCS4LE4=";
	}
}
