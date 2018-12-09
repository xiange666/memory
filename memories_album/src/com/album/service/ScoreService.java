package com.album.service;

import java.util.List;

import com.album.model.Photo;
import com.album.model.User;

public interface ScoreService {
	//人的排行点赞量
	public List<User> man_thumb_up();
	//人的排行收藏量
	public List<User> man_collect();
	//照片的排行点赞量
	public List<Photo> photo_thumb_up();
	//照片的排行收藏量
	public List<Photo> photo_collect();
	
}
