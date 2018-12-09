package com.album.service;

import java.util.List;

import com.album.common.config.ResultCodeEnum;
import com.album.model.Comment;
import com.album.model.News;
import com.album.model.Reply;
import com.album.model.ThumbUp;
import com.album.model.User;
import com.alibaba.fastjson.JSONArray;
public interface NewsService {
	//发布动态
	public boolean post_news(News news);
	//删除动态
	public boolean del_news(Long userId,Long newsId);
	//点赞
	public boolean thumb_up(ThumbUp thumbUp);
	//评论
	public ResultCodeEnum send_comment(Comment comment);
	//回复评论
	public ResultCodeEnum reply_comment(Reply reply);
	//根据用户昵称搜索动态
	public List<News> search_news(String searchName,Long userId,Integer pageNumber,Integer pageSize);
	//显示动态,与上一个接口可以合并
	public List<News> show_news(Long userId,Integer pageNumber,Integer pageSize);
	//显示动态相关的评论
	public JSONArray show_comment(Long newsId);
}
