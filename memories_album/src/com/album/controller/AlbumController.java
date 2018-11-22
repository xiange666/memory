package com.album.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.album.common.config.BaseResponse;
import com.album.common.config.ResultCodeEnum;
import com.album.model.Album;
import com.album.service.AlbumService;
import com.album.service.OssClientUtil;
import com.album.service.imp.AlbumServiceImp;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

public class AlbumController extends Controller{
	BaseResponse baseResponse=new BaseResponse();
	AlbumService albumService=new AlbumServiceImp();
	
	/**
	 * 展示用户的相册
	 */
	public void showUserAlbum()
	{
		Long  user_id=this.getParaToLong("user_id");
		if(user_id==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else
		{
			List<Album> my_albums=albumService.find_albums(user_id);
			if(my_albums!=null)
			{
				baseResponse.setData(my_albums);
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}else
			{
				baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
			}
			
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 创建新相册
	 */
	public void createAlbum()
	{
		Album album=this.getModel(Album.class);
		if(album==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else {
			album.setAlbumUrl("********");
			Long album_id=albumService.add_album(album);
			if(album_id!=null)
			{
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
				baseResponse.setData(album_id);
			}
				
		}
		renderJson(baseResponse);
		
	}
	
	/**
	 * 向相册添加照片
	 */
	public void addPhoto()
	{
		Long album_id=this.getParaToLong("album_id");
		OssClientUtil ossClientUtil=new OssClientUtil();
		List<UploadFile> photos=this.getFiles();
		if(album_id==null||photos==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else
		{
			List<String > photos_url=new ArrayList<String>();
			for (UploadFile uploadFile : photos) {
				//获取上传的图片url
				String path=uploadFile.getUploadPath();
				String filename=uploadFile.getFileName();
				path=path+"\\"+filename;
				ossClientUtil.uploadImg2Oss(path);
				String url=ossClientUtil.getImgUrl(path);
				if(url==null)
				{
					photos_url=null;
					break;
				}
				photos_url.add(url);
			}
			if(photos_url==null)
			{
				baseResponse.setResult(ResultCodeEnum.UPLOAD_ERROR);
			}else
			{
				if(albumService.add_photo(album_id, photos_url))
				{
					baseResponse.setResult(ResultCodeEnum.SUCCESS);
				}else
				{
					baseResponse.setResult(ResultCodeEnum.ADD_ERROR);
				}
			}
			
		}
		
		renderJson(baseResponse);
		
	}
	
	/**
	 * 删除相册中的照片
	 */
	public void delPhoto()
	{
		Long album_id=this.getParaToLong("album_id");
		Long[] photos_id=this.getParaValuesToLong("photo_id");
		if(album_id==null||photos_id==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else {
			if(albumService.del_photo(album_id, photos_id))
			{
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}else
			{
				baseResponse.setResult(ResultCodeEnum.DELETE_ERROR);
			}
			
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 搜索相册
	 */
	public void searchAlbum()
	{
		Long user_id=this.getParaToLong("user_id");
		String condition=this.getPara("condition");
		if(user_id==null||condition==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else
		{
			List<Album> albums=albumService.search_album(user_id, condition);
			if(albums==null)
			{
				baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
			}else
			{
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
				baseResponse.setData(albums);
			}
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 显示相册的具体内容
	 */
	public void showAlbumDetail()
	{
		Long album_id=this.getParaToLong("album_id");
		if(album_id==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else
		{
			JSONArray jsonarray=albumService.album_detail(album_id);
			if(jsonarray==null)
			{
				baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
			}else
			{
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
				baseResponse.setData(jsonarray);
			}
		}
		renderJson(baseResponse);
	}

}
