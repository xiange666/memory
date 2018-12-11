package com.album.service;

import java.util.List;

import com.album.common.config.ResultCodeEnum;
import com.album.model.CollectPhoto;
import com.album.model.Photo;
import com.album.model.ThumbUp;
import com.alibaba.fastjson.JSONObject;

public interface PhotoService {
	//照片收藏
	public boolean favorite_photo(CollectPhoto collectPhoto);
	//显示照片信息
	public JSONObject show_photo_info(Long photoId);
	//照片信息修改
	public ResultCodeEnum update_photo_info(Photo photo,String[] names);
	//照片点赞
	public boolean thumb_photo(ThumbUp thumbUp);
	//照片删除,可批量删除
	public boolean del_photos(List<Long> photoIds,Long userId);
	
	public boolean del_albums(List<Long> albumIds,Long userId);
}
