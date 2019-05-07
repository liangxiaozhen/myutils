package com.ganjiangps.wangdaibus.common.file.alioos;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.ganjiangps.wangdaibus.common.response.ProcessBack;
import com.ganjiangps.wangdaibus.common.util.FileUtil;
import com.ganjiangps.wangdaibus.common.util.StringUtil;

/**
 * oos 文件上传
 * @author admin
 *
 */
public class AliyunOSSUtil {
	
	protected static Logger logger = LoggerFactory.getLogger(AliyunOSSUtil.class);
 	
	public static String upload(File file){
        String endpoint=AliOOSConstantProperties.WANGDAIBUS_END_POINT;
        String accessKeyId=AliOOSConstantProperties.WANGDAIBUS_ACCESS_KEY_ID;
        String accessKeySecret=AliOOSConstantProperties.WANGDAIBUS_ACCESS_KEY_SECRET;
        String bucketName=AliOOSConstantProperties.WANGDAIBUS_BUCKET_NAME1;
        String fileHost=AliOOSConstantProperties.WANGDAIBUS_FILE_HOST;
 
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
 
        if(null == file){
            return null;
        }
 
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        try {
            //容器不存在，就创建
            if(! ossClient.doesBucketExist(bucketName)){
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            //创建文件路径
            String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+FileUtil.getFileSuffix(file.getName()));
            System.out.println(fileUrl);
            //上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            //设置权限 这里是公开读
            ossClient.setBucketAcl(bucketName,CannedAccessControlList.PublicRead);
            if(null != result){
                logger.info("==========>OSS文件上传成功,OSS地址："+fileUrl);
                return fileUrl;
            }
        }catch (OSSException oe){
            logger.error(oe.getMessage());
        }catch (ClientException ce){
            logger.error(ce.getMessage());
        }finally {
            //关闭
            ossClient.shutdown();
        }
        return null;
    }
 	
	public static ProcessBack uploadProcessBack(MultipartFile file){
		ProcessBack back = new ProcessBack();
 		if(null != file){
            String filename = file.getOriginalFilename();
            if(!"".equals(filename.trim())){
            	try{
	                File newFile = new File(filename);
	                FileOutputStream os = new FileOutputStream(newFile);
	                os.write(file.getBytes());
	                os.close();
	                file.transferTo(newFile);
	                //上传到OSS
	                String uploadUrl = upload(newFile);
	                if(uploadUrl != null && StringUtil.isNotEmpty(uploadUrl)){
	            		back.setCode(ProcessBack.SUCCESS_CODE);
	             		back.setMessage("图片上传成功");
	             		back.setData(uploadUrl);
	             		return back;
	                }
            	}catch(Exception e){
            		e.printStackTrace();
            	}
            }
        }
 		
 		back.setCode(ProcessBack.FAIL_CODE);
 		back.setMessage("图片上传失败");
 		return back;
    }
}
