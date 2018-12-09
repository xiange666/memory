package com.album.service.imp;

import java.util.List;

import com.album.model.Photo;
import com.album.model.User;
import com.album.service.ScoreService;

public class ScoreServiceImp implements ScoreService{

	@Override
	public List<User> man_thumb_up() {
		return User.dao.find(" SELECT  user_id,nick_name,like_num FROM user ORDER BY like_num DESC LIMIT 0,99");
	}

	@Override
	public List<User> man_collect() {
		return User.dao.find(" SELECT  user_id,nick_name,collect_num FROM user ORDER BY collect_num DESC LIMIT 0,99");
	}

	@Override
	public List<Photo> photo_thumb_up() {
		return Photo.dao.find(" SELECT * FROM photo  ORDER BY like_num DESC LIMIT 0,99");
	}

	@Override
	public List<Photo> photo_collect() {
		return Photo.dao.find(" SELECT * FROM photo  ORDER BY collect_num DESC LIMIT 0,99");
	}

}
