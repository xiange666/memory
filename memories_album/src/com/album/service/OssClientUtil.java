package com.album.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import com.album.common.config.Constant;
import com.alibaba.druid.util.StringUtils;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

public class OssClientUtil 
{
	//阿里云API的内或外网域名
    private static String ENDPOINT;
    //阿里云API的密钥Access Key ID
    private static String ACCESS_KEY_ID;
    //阿里云API的密钥Access Key Secret
    private static String ACCESS_KEY_SECRET;
    //阿里云API的bucket名称
    private static String BACKET_NAME;
    //阿里云API的文件夹名称
    private static String FOLDER;
    //阿里云客户端对象
    private OSSClient ossClient;
    
  //初始化属性
    static{
        ENDPOINT = Constant.endpoint;
        ACCESS_KEY_ID = Constant.accessKeyId;
        ACCESS_KEY_SECRET = Constant.accessKeySecret;
        BACKET_NAME = Constant.bucketName;
        FOLDER = Constant.folder;
    }
    
    public OssClientUtil()
    {
    	ossClient=new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }
    
    /**
     * 初始化
     */
    public void init()
    {
    	ossClient=new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }
    
    /**
     * 销毁
     */
	public void destroy()
	{
		ossClient.shutdown();
	}
	
	/**
	 * 创建存储空间
	 */
	public String createBucket(OSSClient ossClient,String bucketName)
	{
		if(!ossClient.doesBucketExist(bucketName))
		{
			Bucket bucket=ossClient.createBucket(bucketName);
			System.out.println("创建存储空间成功");
			return bucket.getName();
		}
		return bucketName;
		
	}
	/**
     * 删除存储空间buckName
     */
    public void deleteBucket(OSSClient ossClient, String bucketName){
        ossClient.deleteBucket(bucketName);
        System.out.println("删除" + bucketName + "Bucket成功");
    }
    
    /**
     * 创建文件夹，folder名字必须为"zong/"
     */
    public  String createFolder(OSSClient ossClient,String bucketName,String folder){
        //判断文件夹是否存在，不存在则创建
        if(!ossClient.doesObjectExist(bucketName, folder)){
            //创建文件夹
            ossClient.putObject(bucketName, folder, new ByteArrayInputStream(new byte[0]));
            System.out.println("创建文件夹成功");
            //得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, folder);
            String fileDir=object.getKey();
            return fileDir;
        }
        return folder;
    }
    
    /**
     * 根据key删除OSS服务器上的文件
     * @param key Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public static void deleteFile(OSSClient ossClient, String bucketName, String folder, String key){
        ossClient.deleteObject(bucketName, folder + key);
        System.out.println("删除" + bucketName + "下的文件" + folder + key + "成功");
    }
    
    /**
     * 上传图片
     */ 
    public void uploadImg2Oss(String url) {
        File fileOnServer = new File(url);
        System.out.println(url);
        InputStream fin;
        try {
          fin = new FileInputStream(fileOnServer);
          String[] split = url.split("\\\\");
          this.uploadFile2OSS(fin, split[split.length - 1]);
        } catch (FileNotFoundException e) {
          System.out.println("图片上传失败");
        }
      }
    
//    public String uploadImg2Oss(MultipartFile file) {
//        if (file.getSize() > 1024 * 1024) {
//        	System.out.println("上传图片大小不能超过1M！");
//        }
//        String originalFilename = file.getOriginalFilename();
//        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
//        Random random = new Random();
//        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
//        try {
//          InputStream inputStream = file.getInputStream();
//          this.uploadFile2OSS(inputStream, name);
//          return name;
//        } catch (Exception e) {
//        	System.out.println("图片上传失败");
//        }
//      }
    /**
	 * 获得图片路径
	 *
	 * @param fileUrl
	 * @return
	 */
	public String getImgUrl(String fileUrl) {
		System.out.println(fileUrl);
		if (!StringUtils.isEmpty(fileUrl)) {
			String[] split = fileUrl.split("\\\\");
			return this.getUrl(this.FOLDER + split[split.length - 1]);
		}
		return null;
	}
    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param instream 文件输入流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFile2OSS(InputStream instream, String fileName) {
      String ret = "";
      try {
        //创建上传Object的Metadata 
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(instream.available());//上传文件长度
        objectMetadata.setCacheControl("no-cache");			//文件被下载时，网页的缓冲行为
        objectMetadata.setHeader("Pragma", "no-cache");		//设置文件的header
        objectMetadata.setContentEncoding("utf-8");			//下载内容编码
        objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
        objectMetadata.setContentDisposition("filename=" + fileName);//文件被下载时的名称
        System.out.println(fileName);
        //上传文件
        PutObjectResult putResult = ossClient.putObject(BACKET_NAME, FOLDER + fileName, instream, objectMetadata);
        System.out.println("ret:");
        ret = putResult.getETag();
        System.out.println(ret);
      } catch (IOException e) {
//        log.error(e.getMessage(), e);
    	  
      } finally {
        try {
          if (instream != null) {
            instream.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      return ret;
    }
    
    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String FilenameExtension) {
      if (FilenameExtension.equalsIgnoreCase(".bmp")) {
        return "image/bmp";
      }
      if (FilenameExtension.equalsIgnoreCase(".gif")) {
        return "image/gif";
      }
      if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
          FilenameExtension.equalsIgnoreCase(".jpg") ||
          FilenameExtension.equalsIgnoreCase(".png")) {
        return "image/jpeg";
      }
      if (FilenameExtension.equalsIgnoreCase(".html")) {
        return "text/html";
      }
      if (FilenameExtension.equalsIgnoreCase(".txt")) {
        return "text/plain";
      }
      if (FilenameExtension.equalsIgnoreCase(".vsd")) {
        return "application/vnd.visio";
      }
      if (FilenameExtension.equalsIgnoreCase(".pptx") ||
          FilenameExtension.equalsIgnoreCase(".ppt")) {
        return "application/vnd.ms-powerpoint";
      }
      if (FilenameExtension.equalsIgnoreCase(".docx") ||
          FilenameExtension.equalsIgnoreCase(".doc")) {
        return "application/msword";
      }
      if (FilenameExtension.equalsIgnoreCase(".xml")) {
        return "text/xml";
      }
      return "image/jpeg";
    }
    
    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
      // 设置URL过期时间为10年  3600l* 1000*24*365*10
      Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
      // 生成URL
      URL url = ossClient.generatePresignedUrl(BACKET_NAME, key, expiration);
      if (url != null) {
        return url.toString();
      }
      return null;
    }
    
}
