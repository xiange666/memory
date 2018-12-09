
package com.album.service;

import com.album.common.config.Constant;

import com.qcloud.cos.COSClient;

import com.qcloud.cos.ClientConfig;

import com.qcloud.cos.auth.BasicCOSCredentials;

import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.ObjectMetadata;

import com.qcloud.cos.model.PutObjectResult;

import com.qcloud.cos.region.Region;
import com.qcloud.cos.utils.StringUtils;

import java.io.*;

import java.net.URL;

import java.util.Date;

import java.util.Random;

 

/**

 * @author XiaoLuo

 * @ClassName: com.neusoft.controller.util.COSClientUtil

 * @Description: ${todo}

 * @date 2018/3/12 15:37

 */

public class CosClientUtil {

 

	//todo 这些变量信息自行到 腾讯云对象存储控制台 获取

	// 存储通名称替换成自己的

	private static  String bucketName ;

	//secretId 替换成自己的

	private static  String secretId ;

	// secretKey替换成自己的

	private static  String secretKey ;

	 private static String FOLDER;
	 
	static {
		secretId=Constant.secretId;
		secretKey=Constant.secretKey;
		bucketName=Constant.bucketName;
		FOLDER = Constant.folder;
	}
	
	// 1 初始化用户身份信息(secretId, secretKey)
	private static final COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

	// 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224

	private static final ClientConfig clientConfig = new ClientConfig(new Region("ap-shanghai"));

	// 3 生成cos客户端

	private static final COSClient cosClient = new COSClient(cred, clientConfig);

	// 文件存储目录

	//private String filedir = "blog/";
	

	private COSClient cOSClient;

 

	public CosClientUtil() {

		cOSClient = new COSClient(cred, clientConfig);

	}
	
	public COSClient get_client()
	{
		return cOSClient;
	}
 

	/**

	 * 销毁

	 */

	public void destory() {

		cOSClient.shutdown();

	}

	//创建文件夹
	public  String createFolder(COSClient cosClient,String bucketName,String folder){
        //判断文件夹是否存在，不存在则创建
        if(!cosClient.doesObjectExist(bucketName, folder)){
            //创建文件夹
        	cosClient.putObject(bucketName, folder, new ByteArrayInputStream(new byte[0]), null);
            System.out.println("创建文件夹成功");
            //得到文件夹名
            COSObject object = cosClient.getObject(bucketName, folder);
            String fileDir=object.getKey();
            return fileDir;
        }
        return folder;
    }
 

	/**

	 * 上传图片

	 *

	 * @param url

	 */

	public void uploadImg2Cos(String url) throws Exception {

		File fileOnServer = new File(url);

		FileInputStream fin;

		try {

			fin = new FileInputStream(fileOnServer);

			String[] split = url.split("\\\\");

			this.uploadFile2Cos(fin, split[split.length - 1]);

		} catch (FileNotFoundException e) {

			throw new Exception("图片上传失败");

		}

	}

 

//	public String uploadFile2Cos(MultipartFile file) throws Exception {
//
//		if (file.getSize() > 10 * 1024 * 1024) {
//
//			throw new Exception("上传图片大小不能超过10M！");
//
//		}
//
//		String originalFilename = file.getOriginalFilename();
//
//		String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
//
//		Random random = new Random();
//
//		String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
//
//		try {
//
//			InputStream inputStream = file.getInputStream();
//
//			this.uploadFile2Cos(inputStream, name);
//
//			return name;
//
//		} catch (Exception e) {
//
//			throw new Exception("图片上传失败");
//
//		}
//
//	}

 

	/**

	 * 获得图片路径

	 *

	 * @param fileUrl

	 * @return

	 */

	public String getImgUrl(String fileUrl) {
		if (!StringUtils.isNullOrEmpty(fileUrl)) {
			String[] split = fileUrl.split("\\\\");
			return this.getUrl(FOLDER + split[split.length - 1]);
		}
		return null;

	}

 

	/**

	 * 获得url链接

	 *

	 * @param key

	 * @return

	 */

	public String getUrl(String key) {

		// 设置URL过期时间为10年 3600l* 1000*24*365*10

		Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);

		// 生成URL

		URL url = cosClient.generatePresignedUrl(bucketName, key, expiration);

		if (url != null) {

			return url.toString();

		}

		return null;

	}

 

	/**

	 * 上传到COS服务器 如果同名文件会覆盖服务器上的

	 *

	 * @param instream

	 *            文件流

	 * @param fileName

	 *            文件名称 包括后缀名

	 * @return 出错返回"" ,唯一MD5数字签名

	 */

	public String uploadFile2Cos(InputStream instream, String fileName) {

		String ret = "";

		try {

			// 创建上传Object的Metadata

			ObjectMetadata objectMetadata = new ObjectMetadata();

			objectMetadata.setContentLength(instream.available());

			objectMetadata.setCacheControl("no-cache");

			objectMetadata.setHeader("Pragma", "no-cache");

			objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));

			objectMetadata.setContentDisposition("inline;filename=" + fileName);

			// 上传文件

			PutObjectResult putResult = cOSClient.putObject(bucketName,  FOLDER+fileName, instream, objectMetadata);

			ret = putResult.getETag();

		} catch (IOException e) {

			e.printStackTrace();

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

	 * Description: 判断Cos服务文件上传时文件的contentType

	 *

	 * @param filenameExtension 文件后缀

	 * @return String

	 */

	public static String getcontentType(String filenameExtension) {

		if (filenameExtension.equalsIgnoreCase("bmp")) {

			return "image/bmp";

		}

		if (filenameExtension.equalsIgnoreCase("gif")) {

			return "image/gif";

		}

		if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")

				|| filenameExtension.equalsIgnoreCase("png")) {

			return "image/jpeg";

		}

		if (filenameExtension.equalsIgnoreCase("html")) {

			return "text/html";

		}

		if (filenameExtension.equalsIgnoreCase("txt")) {

			return "text/plain";

		}

		if (filenameExtension.equalsIgnoreCase("vsd")) {

			return "application/vnd.visio";

		}

		if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {

			return "application/vnd.ms-powerpoint";

		}

		if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {

			return "application/msword";

		}

		if (filenameExtension.equalsIgnoreCase("xml")) {

			return "text/xml";

		}

		return "image/jpeg";

	}

 

 

}
