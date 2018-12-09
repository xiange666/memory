package com.album.controller;

import java.util.List;

import com.album.common.config.BaseResponse;
import com.album.common.config.ResultCodeEnum;
import com.album.model.Comment;
import com.album.model.News;
import com.album.model.Reply;
import com.album.model.ThumbUp;
import com.album.service.NewsService;
import com.album.service.imp.NewsServiceImp;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

public class NewsController extends Controller{
	BaseResponse baseResponse = new BaseResponse();
	NewsService newsService = new NewsServiceImp();
	
	/**
	 * 发布动态
	 */
	public void postNews() {
	    String[] photos=this.getParaValues("photos");
	    
//		List<UploadFile>pFiles=getFiles("photos");
		News news =getModel(News.class);
		if(news.getPublicId()==null||news.getContent()==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
		}else {
			JSONObject content=(JSONObject) JSON.parse(news.getContent());
			//将content中的文件上传到阿里云，得到网络URL
			String url="";
			content.put("url", url);
			boolean flag = newsService.post_news(news);
			if(flag) {
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}else {
				baseResponse.setResult(ResultCodeEnum.ADD_ERROR);
			}
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 删除动态
	 */
	public void delNews() {
		Long userId = getParaToLong("user_id");
		Long newsId = getParaToLong("news_id");
		if(userId==null || newsId ==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
		}else {
			boolean flag = newsService.del_news(userId, newsId);
			if(flag) {
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}
			baseResponse.setResult(ResultCodeEnum.DELETE_ERROR);
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 动态点赞
	 */
	public void thumbUp() {
		ThumbUp thumbUp = getModel(ThumbUp.class);
		if(thumbUp.getNewsId()==null||thumbUp.getUserId()==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
		}else {
			boolean flag = newsService.thumb_up(thumbUp);
			if(flag) {
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}
			baseResponse.setResult(ResultCodeEnum.DELETE_ERROR);
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 对动态进行评论
	 */
	public void sendComment() {
		Comment comment = getModel(Comment.class);
		if(comment.getCommentatorId()==null||comment.getNewsId()==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
		}else {
			baseResponse.setResult(newsService.send_comment(comment));
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 回复评论
	 */
	public void replyComment() {
		Reply reply = getModel(Reply.class);
		if(reply.getCommentId()==null||reply.getRelpyerId()==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);;
		}else {
			baseResponse.setResult(newsService.reply_comment(reply));
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 动态搜索
	 */
	public void searchNews() {
		//没有控制分页
		Long userId =getParaToLong("user_id");
		String searchName = getPara("user_name");
		Integer pageNumber=getParaToInt("page_num");
		Integer pageSize = getParaToInt("page_size");
		if(userId==null||searchName==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
		}else {
			List<News> news=newsService.search_news(searchName, userId,pageNumber,pageSize);
			baseResponse.setData(news);
			baseResponse.setResult(ResultCodeEnum.SUCCESS);
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 展示动态
	 */
	public void showNews() {
		//没有控制分页
		Long userId = getParaToLong("user_id");
		Integer pageNumber=getParaToInt("page_num");
		Integer pageSize = getParaToInt("page_size");
		if(userId==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
		}else {
			List<News> news=newsService.show_news(userId,pageNumber,pageSize);
			baseResponse.setData(news);
			baseResponse.setResult(ResultCodeEnum.SUCCESS);
		}
		renderJson(baseResponse);	
	}
	
	/**
	 * 显示评论
	 */
	public void showComments() {
		Long newsId = getParaToLong("news_id");
		if(newsId==null) {
			baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
		}else {
			JSONArray jsonArray = newsService.show_comment(newsId);
			baseResponse.setData(jsonArray);
			baseResponse.setResult(ResultCodeEnum.SUCCESS);
		}
		renderJson(baseResponse);
	}
	
}
