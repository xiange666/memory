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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
			album.setAlbumUrl("https://memory-1257971248.cos.ap-shanghai.myqcloud.com/image_url/v2-33267ef939b7fe585d5cb4a1c9149eb4_r.jpg");
			Long album_id=albumService.add_album(album);
			
			if(album_id!=null)
			{
				JSONObject jObject=new JSONObject();
				jObject.put("album_id", album_id);
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
				baseResponse.setData(jObject);
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
		Long user_id=this.getParaToLong("user_id");
		String user_name=this.getPara("user_name");
		String url=this.getPara("photos_url");
		String key=this.getPara("photos_key");
		
		if(album_id==null||url==null||key==null||user_id==null||user_name==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else
		{
			String[] photos_url=url.split(",");
			String[] photos_key=key.split(",");
				if(albumService.add_photo(album_id, photos_url,photos_key,user_id,user_name))
				{
					baseResponse.setResult(ResultCodeEnum.SUCCESS);
				}else
				{
					baseResponse.setResult(ResultCodeEnum.ADD_ERROR);
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
		String id=this.getPara("photo_id");
		if(album_id==null||id==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else {
			String[] photos_id=id.split(",");
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
	
	public void find_key()
	{
		String id=this.getPara("photo_id");
		if(id==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else {
			String[] photos_id=id.split(",");
			List<String> key=albumService.find_key(photos_id);
			if(key==null)
			{
				baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
			}else {
				baseResponse.setData(key);
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}
		}
		renderJson(baseResponse);
	}

}
