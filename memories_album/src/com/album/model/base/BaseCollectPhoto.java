package com.album.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCollectPhoto<M extends BaseCollectPhoto<M>> extends Model<M> implements IBean {

	public void setCollectPhotoId(java.lang.Long collectPhotoId) {
		set("collect_photo_id", collectPhotoId);
	}

	public java.lang.Long getCollectPhotoId() {
		return get("collect_photo_id");
	}

	public void setFavoriteId(java.lang.Long favoriteId) {
		set("favorite_id", favoriteId);
	}

	public java.lang.Long getFavoriteId() {
		return get("favorite_id");
	}

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

	public void setGmtTime(java.util.Date gmtTime) {
		set("gmt_time", gmtTime);
	}

	public java.util.Date getGmtTime() {
		return get("gmt_time");
	}

	public void setGmtModified(java.util.Date gmtModified) {
		set("gmt_modified", gmtModified);
	}

	public java.util.Date getGmtModified() {
		return get("gmt_modified");
	}

}
