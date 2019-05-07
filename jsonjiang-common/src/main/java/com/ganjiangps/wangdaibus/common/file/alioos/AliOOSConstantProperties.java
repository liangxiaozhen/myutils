package com.ganjiangps.wangdaibus.common.file.alioos;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 阿里云 oos  配置类
 * @author cjm
 *
 */
@Component
@PropertySource("classpath:alioosconfig.properties")
public class AliOOSConstantProperties implements InitializingBean{

	@Value("${wangdaibus.file.endpoint}")
    private String wangdaibus_file_endpoint;
 
    @Value("${wangdaibus.file.keyid}")
    private String wangdaibus_file_keyid;
 
    @Value("${wangdaibus.file.keysecret}")
    private String wangdaibus_file_keysecret;
 
    @Value("${wangdaibus.file.filehost}")
    private String wangdaibus_file_filehost;
 
    @Value("${wangdaibus.file.bucketname1}")
    private String wangdaibus_file_bucketname1;
    
    @Value("${wangdaibus.file.accessurl}")
    private String wangdaibus_file_accessurl;
    
    @Value("${wangdaibus.file.watermarkname}")
    private String wangdaibus_file_watermarkname;
     
    public static String WANGDAIBUS_END_POINT         ;//不同的服务器，地址不同
    public static String WANGDAIBUS_ACCESS_KEY_ID     ;//
    public static String WANGDAIBUS_ACCESS_KEY_SECRET ;//
    public static String WANGDAIBUS_BUCKET_NAME1      ;//
    public static String WANGDAIBUS_FILE_HOST         ;//
    public static String WANGDAIBUS_FILE_ACCESSURL    ;//域名地址
    public static String WANGDAIBUS_FILE_WATERMARKNAME;//水印图片名称
    
    @Override
    public void afterPropertiesSet() throws Exception {
    	WANGDAIBUS_END_POINT = wangdaibus_file_endpoint;
    	WANGDAIBUS_ACCESS_KEY_ID = wangdaibus_file_keyid;
    	WANGDAIBUS_ACCESS_KEY_SECRET = wangdaibus_file_keysecret;
    	WANGDAIBUS_BUCKET_NAME1 = wangdaibus_file_bucketname1;
    	WANGDAIBUS_FILE_HOST = wangdaibus_file_filehost;
    	WANGDAIBUS_FILE_ACCESSURL = wangdaibus_file_accessurl;
    	WANGDAIBUS_FILE_WATERMARKNAME = wangdaibus_file_watermarkname;
    }

 

}
