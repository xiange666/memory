package com.album.common.config;

public class Constant {
	public static final int UNKNOW_SEX=0; //性别未知
	public static final int MAN=1;        //性别为男
	public static final int WOMAN=2;      //性别为女
	
	public static final int OPEN=0;			//查看权限为公开
	public static final int PRIVATE=1;		//查看权限私有
	public static final int SET_PASSWORD=2;//查看权限设为密码
	
	public static final int THUMB_UP_NEWS=0;	//点赞动态
	public static final int THUMP_UP_PHOTO=1;	//点赞照片
	
	public static final int IS_MY=0;		//表示该条动态是自己的
	public static final int IS_OTHER=1;		//表示该条动态是关注人的
	
	
	//设置访问域名
	public static String endpoint="http://oss-cn-beijing.aliyuncs.com";
	//设置访问密钥
	public static String accessKeyId="LTAIvKZJefbG56Ip";
	public static String accessKeySecret="qjtJeuytBmbsPB5zwK7Ioqc9RRlA4l";
	//选择存储空间
	public static String bucketName="memor-bundle";
	//选择文件夹名称
	public static String folder="test/";
	

}
