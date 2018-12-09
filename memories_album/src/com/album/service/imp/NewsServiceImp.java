package com.album.service.imp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.album.common.config.ResultCodeEnum;
import com.album.model.Comment;
import com.album.model.Follow;
import com.album.model.News;
import com.album.model.NewsCircleTimeline;
import com.album.model.Reply;
import com.album.model.ThumbUp;
import com.album.model.User;
import com.album.service.NewsService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;


public class NewsServiceImp implements NewsService{

	@Override
	public boolean post_news(News news) {
		boolean succeed=Db.tx(new IAtom() {	
			@Override
			public boolean run() throws SQLException {
				//在动态表中存储
				news.save();
				//在动态时间轴表里面存，先存自己，再存粉丝
				List<NewsCircleTimeline> newsCircleTimelines=new ArrayList<>();
				NewsCircleTimeline owner = new NewsCircleTimeline();
				owner.setUserId(news.getPublicId());
				owner.setNewsId(news.getNewsId());
				owner.setIsOwn(0);
				newsCircleTimelines.add(owner);
				List<Follow> follows = Follow.dao.find("SELECT follower_id FROM follow WHERE be_followed_id=?",news.getPublicId());
				for(Follow follow : follows) {
					NewsCircleTimeline fan = new NewsCircleTimeline();
					fan.setUserId(follow.getFollowerId());
					fan.setNewsId(news.getNewsId());
					fan.setIsOwn(1);
					newsCircleTimelines.add(fan);
				}
				//批量插入动态时间轴表中
				String sql="INSERT INTO news_circle_timeline(user_id,news_id,is_own) VALUES (?,?,?)";
				int[] result = Db.batch(sql, "user_id,news_id,is_own", newsCircleTimelines,newsCircleTimelines.size());
				return true;
			}
		});		
		return succeed;
	}

	@Override
	public boolean del_news(Long userId , Long newsId) {
		boolean succeed=Db.tx(new IAtom() {	
			@Override
			public boolean run() throws SQLException {
				//删除点赞表，动态时间轴表，评论回复表，评论表，动态表
				boolean flag1=new ThumbUp().set("public_id", userId).set("news_id", newsId).delete();
				boolean flag2=new NewsCircleTimeline().set("news_id", newsId).delete();//不太安全
				boolean flag3=new Comment().set("news_id", newsId).set("commentator_id", userId).delete();//评论回复表级联删除，有点不方便
				boolean flag4=News.dao.deleteById(newsId);
				if(flag1&&flag2&&flag3&&flag4) {
					return true;
				}else {
					return false;
				}
				
			}
		});		
		return succeed;
	}

	@Override
	public boolean thumb_up(ThumbUp thumbUp) {
		//点赞表里面加入一条数据，将人的点赞数加一,将动态的点赞数加一
		boolean succeed=Db.tx(new IAtom() {	
			@Override
			public boolean run() throws SQLException {
				thumbUp.save();
				User user=User.dao.findFirst("SELECT * FROM user WHERE user_id = ?",thumbUp.getPublicId());
				Long likeNum = user.getLikeNum();
				user.setLikeNum(likeNum+1);
				user.update();
				News news =News.dao.findById(thumbUp.getNewsId());
				Integer likeNum2= news.getLikeNum();
				news.setLikeNum(likeNum2);
				news.update();
				return true;
				
			}
		});		
		return succeed;
	}

	@Override
	public ResultCodeEnum send_comment(Comment comment) {
		if(comment.save()) {
			return ResultCodeEnum.SUCCESS;
		}else {
			return ResultCodeEnum.ADD_ERROR;
		}
	}

	@Override
	public ResultCodeEnum reply_comment(Reply reply) {
		if(reply.save()) {
			return ResultCodeEnum.SUCCESS;
		}else {
			return ResultCodeEnum.ADD_ERROR;
		}
	}

	@Override
	public List<News> search_news(String searchName,Long userId,Integer pageNumber,Integer pageSize) {
		//首先查询动态时间轴表，得到userId可见的newsId
		//然后拼接一个News列表newsId 和 public_name(与searchName相同)
		//batch批量查询得到真正的News列表
		Page<NewsCircleTimeline> newsCircleTimelinePages = NewsCircleTimeline.dao.paginate(pageNumber, pageSize, "SELECT news_id,gmt_create", "FROM news_circle_timeline WHERE user_id=? ORDER BY gmt_create DESC",userId);
		List<NewsCircleTimeline> newsCircleTimelines = newsCircleTimelinePages.getList();
		List<News> nList = new ArrayList<>();
		for(NewsCircleTimeline newsCircleTimeline : newsCircleTimelines) {
			News news = News.dao.findFirst("SELECT * FROM news WHERE news_id=? AND public_name like %"+searchName+"%",newsCircleTimeline.getNewsId());
			nList.add(news);
		}
		return nList;
	}

	@Override
	public List<News> show_news(Long userId,Integer pageNumber,Integer pageSize) {
		//首先查询动态时间轴表，得到userId可见的newsId
				//然后拼接一个News列表newsId 和 public_name(与searchName相同)
				//batch批量查询得到真正的News列表
				Page<NewsCircleTimeline> newsCircleTimelinePages = NewsCircleTimeline.dao.paginate(pageNumber, pageSize, "SELECT news_id,gmt_create", "FROM news_circle_timeline WHERE user_id=? ORDER BY gmt_create DESC",userId);
				List<NewsCircleTimeline> newsCircleTimelines = newsCircleTimelinePages.getList();
				List<News> nList = new ArrayList<>();
				for(NewsCircleTimeline newsCircleTimeline : newsCircleTimelines) {
					News news = News.dao.findFirst("SELECT * FROM news WHERE news_id=?",newsCircleTimeline.getNewsId());
					nList.add(news);
				}
				return nList;
	}

	@Override
	public JSONArray show_comment(Long newsId) {
		JSONArray jsonArray= new JSONArray();
		List<Record> comment=Db.find("SELECT * FROM comment WHERE newsId=? ORDER BY gmt_create",newsId);
		for(Record record : comment) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("comment", record);
			List<Record> replys = Db.find("SELECT * FROM reply WHERE comment_id=? ORDER BY gmt_create",record.get("comment_id"));
			jsonObject.put("reply", replys);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
		
	}

}
