package com.album.controller;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import com.album.common.config.BaseResponse;
import com.album.common.config.Constant;
import com.album.common.config.ResultCodeEnum;
import com.album.model.CollectPhoto;
import com.album.model.Photo;
import com.album.model.ThumbUp;
import com.album.service.PhotoService;
import com.album.service.StorageSts;
import com.album.service.imp.PhotoServiceImp;
import com.jfinal.core.Controller;

public class PhotoController extends Controller{
	BaseResponse baseResponse=new BaseResponse();
	PhotoService photoService = new PhotoServiceImp();
	
	public void photoFavorite() {
		//获得前端传递的参数
		CollectPhoto collectPhoto = getModel(CollectPhoto.class);
		if(collectPhoto.getPhotoId()==null||collectPhoto.getFavoriteId()==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
		}else {
			boolean flag = photoService.favorite_photo(collectPhoto);
			if(flag) {
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}else {
				baseResponse.setResult(ResultCodeEnum.ADD_ERROR);
			}
		}
		renderJson(baseResponse);
	}
	
	public void showPhotoInfo() {
		Long photoId = getParaToLong("photo_id");
		if(photoId==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
		}else {
			baseResponse.setData(photoService.show_photo_info(photoId));
			baseResponse.setResult(ResultCodeEnum.SUCCESS);
		}
		renderJson(baseResponse);
	}
	
	public void updatePhotoInfo() {
		Photo photo = getModel(Photo.class);
		if(photo.getPhotoId()==null||photo.getPhotoId()==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
		}else {
			baseResponse.setResult(photoService.update_photo_info(photo));
		}
		renderJson(baseResponse);
	}
	
	public void delPhoto() {
		List<Long> photoIds=Arrays.asList(getParaValuesToLong("photo_ids[]"));
		Long userId = getParaToLong("user_id");
		if(photoIds.isEmpty()||userId==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
		}else {
			boolean flag = photoService.del_photos(photoIds, userId);
			if(flag) {
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}else {
				baseResponse.setResult(ResultCodeEnum.DELETE_ERROR);
			}
		}
		renderJson(baseResponse);
	}
	public void photoThumbUp() {
		ThumbUp thumbUp = getModel(ThumbUp.class);
		if(thumbUp.getPhotoId()==null||thumbUp.getPublicId()==null||thumbUp.getUserId()==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
		}else {
			boolean flag = photoService.thumb_photo(thumbUp);
			if(flag) {
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}else {
				baseResponse.setResult(ResultCodeEnum.ADD_ERROR);
			}
		}
		renderJson(baseResponse);
	}
	
	public void delAlbum() {
		List<Long> albumIds=Arrays.asList(getParaValuesToLong("album_ids[]"));
		Long userId = getParaToLong("user_id");
		if(albumIds.isEmpty()||userId==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
		}else {
			boolean flag = photoService.del_albums(albumIds, userId);
			if(flag) {
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}else {
				baseResponse.setResult(ResultCodeEnum.DELETE_ERROR);
			}
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 获取cos临时密钥
	 */
	public void getTmpKey()
	{
		TreeMap<String, Object> config = new TreeMap<String, Object>();

        // 您的 SecretID
		config.put("SecretId", Constant.secretId);
		// 您的 SecretKey
		config.put("SecretKey", Constant.secretKey);
		// 临时密钥有效时长，单位是秒
		config.put("durationInSeconds", 1800);
	    com.qcloud.Utilities.Json.JSONObject credential = StorageSts.getCredential(config);
        renderJson(credential);
	}
}
