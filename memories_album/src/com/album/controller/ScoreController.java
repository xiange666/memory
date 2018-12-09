package com.album.controller;

import java.util.List;

import com.album.common.config.BaseResponse;
import com.album.common.config.ResultCodeEnum;
import com.album.model.Photo;
import com.album.model.User;
import com.album.service.ScoreService;
import com.album.service.imp.ScoreServiceImp;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
/**
 * 
 * @author 邬学猛
 * 四种排行都使用ehcache缓存，现在设置两种方案：
 * 1.直接使用缓存的存活时间来更新排行
 * 2.当点赞，收藏接口被调用时，自动清除缓存，等再调用接口时再缓存
 * 
 * 问题：如果查询失败，ERROR信息被缓存了怎么办？
 */
public class ScoreController extends Controller{
	BaseResponse baseResponse = new BaseResponse();
	ScoreService scoreService = new ScoreServiceImp();
	
	@Before(CacheInterceptor.class)
	@CacheName("manThumbUp")
	public void manThumbUp(){
		List<User>users = scoreService.man_thumb_up();
		if(users==null) {
			baseResponse.setResult(ResultCodeEnum.FIND_ERROR);
		}else {
			baseResponse.setData(users);
			baseResponse.setResult(ResultCodeEnum.SUCCESS);
		}
		renderJson(baseResponse);
	}
	
	@Before(CacheInterceptor.class)
	@CacheName("manCollect")
	public void manCollect(){
		List<User>users = scoreService.man_collect();
		if(users==null) {
			baseResponse.setResult(ResultCodeEnum.FIND_ERROR);
		}else {
			baseResponse.setData(users);
			baseResponse.setResult(ResultCodeEnum.SUCCESS);
		}
		renderJson(baseResponse);
	}
	
	@Before(CacheInterceptor.class)
	@CacheName("photoThumbUp")
	public void photoThumbUp(){
		List<Photo>photos = scoreService.photo_thumb_up();
		if(photos==null) {
			baseResponse.setResult(ResultCodeEnum.FIND_ERROR);
		}else {
			baseResponse.setData(photos);
			baseResponse.setResult(ResultCodeEnum.SUCCESS);
		}
		renderJson(baseResponse);
	}
	
	@Before(CacheInterceptor.class)
	@CacheName("photoCollect")
	public void photoCollect(){
		List<Photo>photos = scoreService.photo_collect();
		if(photos==null) {
			baseResponse.setResult(ResultCodeEnum.FIND_ERROR);
		}else {
			baseResponse.setData(photos);
			baseResponse.setResult(ResultCodeEnum.SUCCESS);
		}
		renderJson(baseResponse);
	}

}
