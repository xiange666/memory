package com.album.service.imp;

import java.sql.SQLException;
import java.util.List;

import org.w3c.dom.ls.LSInput;

import com.album.common.config.Constant;
import com.album.model.Album;
import com.album.model.Comment;
import com.album.model.Follow;
import com.album.model.News;
import com.album.model.NewsCircleTimeline;
import com.album.model.User;
import com.album.service.UserService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;


public class UserServiceImp implements UserService{

	@Override
	public User add_user(String result) {
		
		JSONObject jsonObject=JSONObject.parseObject("result");
		User  user=new User();
		user.setOpenId(jsonObject.getString("openId"));
		user.setUserSex(jsonObject.getInteger("gender"));
		user.setNickName(jsonObject.getString("nickName"));
		user.setUserCity(jsonObject.getString("city"));
		user.save();
		return user;
		// TODO Auto-generated method stub
		
	}

	@Override
	public User find_by_OpenId(String openId) {
		// TODO Auto-generated method stub
		return User.dao.findFirst("select * from user where open_id=?",openId);
	}

	@Override
	public User find_by_UserId(Long userId) {
		// TODO Auto-generated method stub
		return User.dao.findFirst("select * from user where user_id=?",userId);
	}

	@Override
	public boolean update_User(User user) {
		// TODO Auto-generated method stub
		return user.update();
	}
	
	@Override
	public JSONArray find_news(Long userId) {
		// TODO Auto-generated method stub
		JSONArray jsonArray=new JSONArray();
		
		List<News> news=News.dao.find("select * from news where public_id=?",userId);
		for (News n : news) {
			List<Comment> comments=Comment.dao.find("select * from comment where news_id=? order by gmt_create desc ",n.getNewsId());
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("news", n);
			jsonObject.put("comments", comments);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
				
	}

	@Override
	public List<Album> find_albums(Long userId) {
		// TODO Auto-generated method stub
		return Album.dao.find("select * from album where user_id=?",userId);
	}

	@Override
	public List<User> find_my_Follow(Long userId) {
		// TODO Auto-generated method stub
		return User.dao.find("select * from user where user_id IN "
				+ "(select be_followed_id from follow where follower_id=?)",userId);
	}

	@Override
	public List<User> find_my_Fans(Long userId) {
		// TODO Auto-generated method stub
		return User.dao.find("select * from user where user_id IN (select follower_id from follow "
				+ "where be_followed_id=?)",userId);
	}

	@Override
	public boolean del_follow(Long followerId, Long be_followedId) {
		// TODO Auto-generated method stub
		boolean succeed=Db.tx(new IAtom() {	
			@Override
			public boolean run() throws SQLException {
				
				int delete_result=Db.update("delete from follow where be_followed_id=? "
						+ "and follower_id=?",be_followedId,followerId);
				//没有删除用户关注表成功回滚
				if(delete_result==0)
				{
					return false;
				}
				//更改被关注用户粉丝人数
				User be_followed=User.dao.findById(be_followedId);
				Long fan_num=be_followed.getLong("fan_num");
				be_followed.setFanNum(fan_num-1);
				//被关注用户表更改失败回滚
				if(!be_followed.save())
					return false;
				
				//更改该用户的关注人数
				User follower=User.dao.findById(followerId);
				Long follow_num=follower.getLong("follow_num");
				follower.setFollowNum(follow_num-1);
				//该用户表更改失败回滚
				if(!follower.save())
					return false;
				
				//删除该用户圈子中关于被关注的所有动态
				int result=Db.update("delete from NewsCircleTimeline "
						+ " where news_id IN (select news_id from news where public_id=? ) and user_id=? "
						+ "and is_own=?",be_followedId,followerId,Constant.IS_OTHER);
				//删除失败回滚
				if(result==0)
					return false;
				//整个过程完全执行，返回true
				return true;
			}
		});
		return succeed;
		
	}

	@Override
	public boolean add_follow(Long followerId,String followerName,Long be_followedId,String be_followedName) {
		// TODO Auto-generated method stub
		boolean succeed=Db.tx(new IAtom() {	
			@Override
			public boolean run() throws SQLException {
				
				Follow follow=new Follow();
				follow.setFollowerId(followerId);
				follow.setFollowerName(followerName);
				follow.setBeFollowedId(be_followedId);
				follow.setBeFollewedName(be_followedName);
				//关注表更改失败回滚
				if(!follow.save())
					return false;

				//更改被关注用户粉丝人数
				User be_followed=User.dao.findById(be_followedId);
				Long fan_num=be_followed.getLong("fan_num");
				be_followed.setFanNum(fan_num+1);
				//被关注用户表更改失败回滚
				if(!be_followed.save())
					return false;
				
				//更改该用户的关注人数
				User follower=User.dao.findById(followerId);
				Long follow_num=follower.getLong("follow_num");
				follower.setFollowNum(follow_num+1);
				//该用户表更改失败回滚
				if(!follower.save())
					return false;
				
				//添加该用户圈子中关于被关注的公开动态
				List<News> news=News.dao.find("select news_id,gmt_create from news where public_id=? "
						+ "and authority=?",be_followedId,Constant.OPEN);
				for (News n : news) {
					NewsCircleTimeline circle=new NewsCircleTimeline();
					circle.setNewsId(n.getNewsId());
					circle.setNewsTime(n.getGmtCreate());
					circle.setUserId(followerId);
					circle.setIsOwn(Constant.IS_OTHER);
					if(!circle.save())
						return false;
				}
				
				//整个过程完全执行，返回true
				return true;
			}
		});
		return succeed;
		

	}

	

}
