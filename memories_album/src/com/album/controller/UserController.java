package com.album.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.album.common.config.BaseResponse;
import com.album.common.config.ResultCodeEnum;
import com.album.model.Album;
import com.album.model.User;
import com.album.service.AesCbcUtil;
import com.album.service.UserService;
import com.album.service.imp.UserServiceImp;

public class UserController extends Controller{
	UserService userService=new UserServiceImp();
	BaseResponse baseResponse=new BaseResponse();
	public void index()
	{
		showInfo();
	}
	
	/**
	 *用户登录
	 * @throws Exception
	 */
	public void login() throws Exception
	{
		String code = this.getPara("code");
		String encryptedData=this.getPara("encryptedData");
		String iv=this.getPara("iv");
		// 小程序唯一标识 (在微信小程序管理后台获取)  
	    String wxspAppid = "wx9272c2b22bebefe7";  
	    // 小程序的 app secret (在微信小程序管理后台获取)  
	    String wxspSecret = "0f5327d3cacf544120c861844a4b7127";  
	    // 授权（必填）  
	    String grant_type = "authorization_code"; 
	    
		////////////////1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid  
		//////////////// ////////////////  
		// 请求参数  
		String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type="  
		   + grant_type;  
		// 发送请求  
		String url="https://api.weixin.qq.com/sns/jscode2session";
		URL realurl=new URL(url+"?"+params);
		URLConnection connection=realurl.openConnection();
		connection.setRequestProperty("accept", "*/*");
		 connection.setRequestProperty("connection", "Keep-Alive");
		 connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		 connection.connect();
		 
		 InputStream iStream=connection.getInputStream();
		 InputStreamReader iReader=new InputStreamReader(iStream,"utf-8");
		 BufferedReader bReader=new BufferedReader(iReader);
		 String str=null;
		 StringBuffer buffer = new StringBuffer();
		 while((str = bReader.readLine()) != null)
		 {
			 buffer.append(str);
		 }
		 bReader.close();
		 iReader.close();
		 iStream.close();
		 
		 System.out.println(buffer.toString());
		 
		// 解析相应内容（转换成json对象） 
		 JSONObject json = (JSONObject) JSONObject.parse(buffer.toString());
		 if(json.get("session_key")!=null&&json.get("openid")!=null)
			{
				// 获取会话密钥（session_key）  
				String session_key = json.get("session_key").toString();  
				// 用户的唯一标识（openid）  
				String openid = (String) json.get("openid"); 
				User user=new User();
				user=userService.find_by_OpenId(openid);
				if(user!=null)
				{
					baseResponse.setResult(ResultCodeEnum.SUCCESS);
					baseResponse.setData(user);
				}else
				{
					String result=AesCbcUtil.decrypt(encryptedData, session_key, iv);
					if(result==null)
					{
						baseResponse.setResult(ResultCodeEnum.DECODE_ERROR);
					}else
					{
						user=userService.add_user(result);
						if(user!=null)
						{
							baseResponse.setResult(ResultCodeEnum.SUCCESS);
							baseResponse.setData(user);
						}
					}
					
				}
				
			}
		 else
		 {
			 baseResponse.setResult(ResultCodeEnum.DECODE_ERROR);
		 }
		 
		 renderJson(baseResponse);
	}
	
	/**
	 * 显示用户基本信息
	 */
	public void showInfo()
	{
		Long  user_id=this.getParaToLong("user_id");
		if(user_id==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else 
		{
			User user=userService.find_by_UserId(user_id);
			if(user!=null)
			{
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
				baseResponse.setData(user);
			}
			else {
				baseResponse.setResult(ResultCodeEnum.FIND_ERROR);
			}
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 用户信息更改
	 */
	public void updateInfo()
	{
		User user=this.getModel(User.class);
		if(userService.update_User(user))
		{
			baseResponse.setResult(ResultCodeEnum.SUCCESS);
		}else
		{
			baseResponse.setResult(ResultCodeEnum.UPDATE_ERROR);
		}
		renderJson(baseResponse);
		
	}
	
	/**
	 * 展示用户发布的动态
	 */
	public void showMyNews()
	{
		Long userId=this.getParaToLong("user_id");
		if(userId==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else
		{
			JSONArray jArray=userService.find_news(userId);
			if(jArray==null)
			{
				baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
			}else
			{
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
				baseResponse.setData(jArray);
			}
		}
		renderJson(baseResponse);
		
	}
	
	/**
	 * 展示用户的所有相册
	 */
	public void showMyAlbum()
	{
		Long  user_id=this.getParaToLong("user_id");
		if(user_id==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else
		{
			List<Album> my_albums=userService.find_albums(user_id);
			if(my_albums!=null)
			{
				baseResponse.setData(my_albums);
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}else
			{
				baseResponse.setResult(ResultCodeEnum.FIND_ERROR);
			}
			
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 展示用户所关注人列表
	 */
	public void showMyFollow()
	{
		Long  user_id=this.getParaToLong("follower_id");
		if(user_id==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else
		{
			List<User> users=userService.find_my_Follow(user_id);
			if(users==null)
			{
				baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
			}else
			{
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
				baseResponse.setData(users);
			}
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 展示用户粉丝列表
	 */
	public void showMyFans()
	{
		Long  user_id=this.getParaToLong("be_followed_id");
		if(user_id==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else
		{
			List<User> users=userService.find_my_Fans(user_id);
			if(users==null)
			{
				baseResponse.setResult(ResultCodeEnum.DATA_EMPTY);
			}else
			{
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
				baseResponse.setData(users);
			}
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 取消关注
	 */
	public void delFollow()
	{
		Long follower_id=this.getParaToLong("follower_id");
		Long be_followed_id=this.getParaToLong("be_followed_id");
		if(follower_id==null||be_followed_id==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else
		{
			if(userService.del_follow(follower_id, be_followed_id))
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			else
				baseResponse.setResult(ResultCodeEnum.DELETE_ERROR);
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 添加关注
	 */
	public void addFollow()
	{
		Long follower_id=this.getParaToLong("follower_id");
		String follower_name=this.getPara("follower_name");
		Long be_followed_id=this.getParaToLong("be_followed_id");
		String be_followed_name=this.getPara("be_followed_name");
		if(follower_id==null||follower_name==null||be_followed_id==null||be_followed_name==null)
		{
			baseResponse.setResult(ResultCodeEnum.NOT_COMPLETE);
		}else
		{
			if(userService.add_follow(follower_id, follower_name, be_followed_id, be_followed_name))
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			else
				baseResponse.setResult(ResultCodeEnum.DELETE_ERROR);
		}
		renderJson(baseResponse);
	}
	
	/**
	 * 展示我的二维码
	 */
	public void myCode()
	{
		
	}
	
	/**
	 * 访问别人主页
	 */
	public void gotoOtherHome()
	{
		
	}
	
	

}
