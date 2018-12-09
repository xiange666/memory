package com.album.controller;

import java.util.List;

import com.album.common.config.BaseResponse;
import com.album.common.config.ResultCodeEnum;
import com.album.model.Favorite;
import com.album.service.FavoriteService;
import com.album.service.imp.FavoriteServiceImp;
import com.jfinal.core.Controller;

public class FavoriteController extends Controller{
	BaseResponse baseResponse=new BaseResponse();
	FavoriteService favoriteService = new FavoriteServiceImp();
	//显示我的收藏夹
	public void showAllFavorite() {
		Long userId = getParaToLong("user_id");
		if(userId==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else{
			List<Favorite> favorites=favoriteService.find_my_favorite(userId);
			if(favorites==null)
			{
				baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
			}else{
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
				baseResponse.setData(favorites);
			}
		}
		renderJson(baseResponse);
	}
	//创建我的收藏夹
	public void createMyFavorite() {
		Favorite favorite=getModel(Favorite.class);
		if(favorite.getUserId()==null||favorite.getFavoriteTitle()==null) {
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else {
			baseResponse.setResult(favoriteService.add_favorite(favorite));
		}
		renderJson(baseResponse);
	}
	//删除我的收藏夹
	public void delMyFavorite() {
		Favorite favorite=getModel(Favorite.class);
		if(favorite.getUserId()==null||favorite.getFavoriteId()==null) {
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else {
			baseResponse.setResult(favoriteService.del_favorite(favorite));
		}
		renderJson(baseResponse);
	}
}
