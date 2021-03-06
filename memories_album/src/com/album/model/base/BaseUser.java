package com.album.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean {

	public void setUserId(java.lang.Long userId) {
		set("user_id", userId);
	}

	public java.lang.Long getUserId() {
		return get("user_id");
	}

	public void setOpenId(java.lang.String openId) {
		set("open_id", openId);
	}

	public java.lang.String getOpenId() {
		return get("open_id");
	}

	public void setUserSex(java.lang.Integer userSex) {
		set("user_sex", userSex);
	}

	public java.lang.Integer getUserSex() {
		return get("user_sex");
	}

	public void setNickName(java.lang.String nickName) {
		set("nick_name", nickName);
	}

	public java.lang.String getNickName() {
		return get("nick_name");
	}

	public void setUserBirth(java.util.Date userBirth) {
		set("user_birth", userBirth);
	}

	public java.util.Date getUserBirth() {
		return get("user_birth");
	}

	public void setUserPhone(java.lang.String userPhone) {
		set("user_phone", userPhone);
	}

	public java.lang.String getUserPhone() {
		return get("user_phone");
	}

	public void setUserJob(java.lang.String userJob) {
		set("user_job", userJob);
	}

	public java.lang.String getUserJob() {
		return get("user_job");
	}

	public void setUserCity(java.lang.String userCity) {
		set("user_city", userCity);
	}

	public java.lang.String getUserCity() {
		return get("user_city");
	}

	public void setLikeNum(java.lang.Long likeNum) {
		set("like_num", likeNum);
	}

	public java.lang.Long getLikeNum() {
		return get("like_num");
	}

	public void setFavoriteNum(java.lang.Long favoriteNum) {
		set("favorite_num", favoriteNum);
	}

	public java.lang.Long getFavoriteNum() {
		return get("favorite_num");
	}

	public void setFollowNum(java.lang.Long followNum) {
		set("follow_num", followNum);
	}

	public java.lang.Long getFollowNum() {
		return get("follow_num");
	}

	public void setFanNum(java.lang.Long fanNum) {
		set("fan_num", fanNum);
	}

	public java.lang.Long getFanNum() {
		return get("fan_num");
	}

	public void setGmtCreate(java.util.Date gmtCreate) {
		set("gmt_create", gmtCreate);
	}

	public java.util.Date getGmtCreate() {
		return get("gmt_create");
	}

	public void setLastLogin(java.util.Date lastLogin) {
		set("last_login", lastLogin);
	}

	public java.util.Date getLastLogin() {
		return get("last_login");
	}

	public void setGmtModified(java.util.Date gmtModified) {
		set("gmt_modified", gmtModified);
	}

	public java.util.Date getGmtModified() {
		return get("gmt_modified");
	}

}
