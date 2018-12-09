package com.album.service;

import java.util.List;

import com.album.common.config.ResultCodeEnum;
import com.album.model.Favorite;

public interface FavoriteService {
	//显示收藏夹
	public List<Favorite> find_my_favorite(Long userId);
	//创建收藏夹
	public ResultCodeEnum add_favorite(Favorite favorite);
	//删除收藏夹
	public ResultCodeEnum del_favorite(Favorite favorite);
}
