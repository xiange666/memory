package com.album.service.imp;

import java.util.List;

import com.album.common.config.Constant;
import com.album.common.config.ResultCodeEnum;
import com.album.model.Favorite;
import com.album.service.FavoriteService;

public class FavoriteServiceImp implements FavoriteService{

	@Override
	public List<Favorite> find_my_favorite(Long userId) {
		//按照时间倒序排列
		return Favorite.dao.find("SELECT favorite_id,favorite_title,description,gmt_create FROM favorite WHERE user_id =? ORDER BY gmt_create DESC",userId);
	}

	@Override
	public ResultCodeEnum add_favorite(Favorite favorite) {
		if(favorite.save()) {
			return ResultCodeEnum.SUCCESS;
		}else {
			return ResultCodeEnum.ADD_ERROR;
		}
	}

	@Override
	public ResultCodeEnum del_favorite(Favorite favorite) {
		if(favorite.delete()) {
			return ResultCodeEnum.SUCCESS;
		}else {
			return ResultCodeEnum.DELETE_ERROR;
		}
	}

}
