package com.album.service;



import java.util.List;

import com.album.model.*;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Record;

public interface UserService 
{
	//添加新用户
	public User add_user(String result);
	//根据用户openId查找用户信息
	public User find_by_OpenId(String openId);
	//根据用户userId查找用户信息
	public User find_by_UserId(Long userId);
	//更新用户信息
	public boolean update_User(User user);
	//查看用户发布的动态包括评论
	public JSONArray find_news(Long userId);
	//查找用户所有相册
    public List<Album> find_albums(Long userId);
    //查看用户所有关注者
    public List<User> find_my_Follow(Long userId);
    //查看用户所有粉丝
    public List<User> find_my_Fans(Long userId);
    //取消关注
    public boolean del_follow(Long followerId,Long be_followedId);
    //添加关注
    public boolean add_follow(Long followerId,String followerName,Long be_followedId,String be_followedName);

}
