package com.album.controller;
import com.album.service.StorageSts;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.TreeMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Duang;
import com.jfinal.core.Const;
import com.jfinal.core.Controller;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.wxaapp.api.WxaUserApi;
import com.album.common.config.BaseResponse;
import com.album.common.config.Constant;
import com.album.common.config.ResultCodeEnum;
import com.album.model.Album;
import com.album.model.User;
import com.album.service.AesCbcUtil;
import com.album.service.UserService;
import com.album.service.imp.UserServiceImp;

public class UserController extends Controller{
	UserService userService=new UserServiceImp();
	BaseResponse baseResponse=new BaseResponse();
	protected WxaUserApi wxaUserApi = Duang.duang(WxaUserApi.class);
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
		String codes=this.getPara("code");
		String encryptedData=this.getPara("encryptedData");//加密数据带有特殊符号"+" url不能直接使用
		String iv=this.getPara("iv");
		if(codes==null)
		{
			return ;
		}
		System.out.println(codes);
//		ApiConfig api=new ApiConfig();
//		api.setAppId("wxcf64167eca427fa3");
//		api.setAppSecret("950efbd0a22411c1025e67603e9e84d4");
//		WxaUserApi wxaUserApi=new WxaUserApi();
//		
		ApiResult apiResult=wxaUserApi.getSessionKey(codes);
//		 
//		 System.out.println(buffer);
		if(apiResult.isSucceed())
		{
			String openid=apiResult.getStr("openid");
			String sessionkey=apiResult.getStr("session_key");
//			String token=UUIDKit.getUUID32();
//			Jedis jedis=RedisUtil.getJedis();
//			jedis.set(token,openid);
//			//三天后过期
//			jedis.expire(token, MainConstant.LOGIN_EXPIRATION_TIME);
//			RedisUtil.returnResource(jedis);
			System.out.println(openid+"   "+sessionkey);
			User user=new User();
			user=userService.find_by_OpenId(openid);
			System.out.println(user==null);
			JSONObject jsonObject=new JSONObject();
			if(user!=null)
			{
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
				
				jsonObject.put("user_id", user.getUserId());
				baseResponse.setData(jsonObject);
			}else
			{
				String result=AesCbcUtil.decrypt(encryptedData, sessionkey, iv);
				System.out.println(result);
				if(result==null)
				{
					baseResponse.setResult(ResultCodeEnum.DECODE_ERROR);
				}else
				{
					user=userService.add_user(result);
					if(user!=null)
					{
						
						baseResponse.setResult(ResultCodeEnum.SUCCESS);
						jsonObject.put("user_id", user.getUserId());
						baseResponse.setData(jsonObject);
					}else
					{
						baseResponse.setResult(ResultCodeEnum.ADD_ERROR);
					}
				}
				
			}
			
			
		}
		 
//		// 解析相应内容（转换成json对象） 
//		 JSONObject json = (JSONObject) JSONObject.parse(buffer.toString());
//		 if(json.get("session_key")!=null&&json.get("openid")!=null)
//			{
//				// 获取会话密钥（session_key）  
//				String session_key = json.get("session_key").toString();  
//				// 用户的唯一标识（openid）  
//				String openid = (String) json.get("openid"); 
//				User user=new User();
//				user=userService.find_by_OpenId(openid);
//				System.out.println(user==null);
//				if(user!=null)
//				{
//					baseResponse.setResult(ResultCodeEnum.SUCCESS);
//					baseResponse.setData(user);
//				}else
//				{
//					String result=AesCbcUtil.decrypt(encryptedData, session_key, iv);
//					System.out.println(result);
//					if(result==null)
//					{
//						baseResponse.setResult(ResultCodeEnum.DECODE_ERROR);
//					}else
//					{
//						user=userService.add_user(result);
//						if(user!=null)
//						{
//							JSONObject jsonObject=new JSONObject();
//							baseResponse.setResult(ResultCodeEnum.SUCCESS);
//							jsonObject.put("user_id", user.getUserId());
//							baseResponse.setData(jsonObject);
//						}else
//						{
//							baseResponse.setResult(ResultCodeEnum.ADD_ERROR);
//						}
//					}
//					
//				}
//				
//			}
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
