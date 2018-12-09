package com.album.common.config;

import com.album.service.CosClientUtil;
import com.album.service.OssClientUtil;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CosClientUtil cosClientUtil=new CosClientUtil();
		cosClientUtil.createFolder(cosClientUtil.get_client(), Constant.cos_bucketName, "2345/567");

	}

}
