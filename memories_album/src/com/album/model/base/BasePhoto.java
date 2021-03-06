package com.album.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BasePhoto<M extends BasePhoto<M>> extends Model<M> implements IBean {

	public void setPhotoId(java.lang.Long photoId) {
		set("photo_id", photoId);
	}

	public java.lang.Long getPhotoId() {
		return get("photo_id");
	}

	public void setPhotoUrl(java.lang.String photoUrl) {
		set("photo_url", photoUrl);
	}

	public java.lang.String getPhotoUrl() {
		return get("photo_url");
	}

	public void setUserId(java.lang.Long userId) {
		set("user_id", userId);
	}

	public java.lang.Long getUserId() {
		return get("user_id");
	}

	public void setUserName(java.lang.String userName) {
		set("user_name", userName);
	}

	public java.lang.String getUserName() {
		return get("user_name");
	}

	public void setMarkTime(java.util.Date markTime) {
		set("mark_time", markTime);
	}

	public java.util.Date getMarkTime() {
		return get("mark_time");
	}

	public void setMarkPlace(java.lang.String markPlace) {
		set("mark_place", markPlace);
	}

	public java.lang.String getMarkPlace() {
		return get("mark_place");
	}

	public void setMarkStory(java.lang.String markStory) {
		set("mark_story", markStory);
	}

	public java.lang.String getMarkStory() {
		return get("mark_story");
	}

	public void setLikeNum(java.lang.Long likeNum) {
		set("like_num", likeNum);
	}

	public java.lang.Long getLikeNum() {
		return get("like_num");
	}

	public void setCollectNum(java.lang.Long collectNum) {
		set("collect_num", collectNum);
	}

	public java.lang.Long getCollectNum() {
		return get("collect_num");
	}

	public void setCosKey(java.lang.String cosKey) {
		set("cos_key", cosKey);
	}

	public java.lang.String getCosKey() {
		return get("cos_key");
	}

	public void setGmtCreate(java.util.Date gmtCreate) {
		set("gmt_create", gmtCreate);
	}

	public java.util.Date getGmtCreate() {
		return get("gmt_create");
	}

	public void setGmtModified(java.util.Date gmtModified) {
		set("gmt_modified", gmtModified);
	}

	public java.util.Date getGmtModified() {
		return get("gmt_modified");
	}

}
