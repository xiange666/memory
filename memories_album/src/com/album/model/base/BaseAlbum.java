package com.album.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAlbum<M extends BaseAlbum<M>> extends Model<M> implements IBean {

	public void setAlbumId(java.lang.Long albumId) {
		set("album_id", albumId);
	}

	public java.lang.Long getAlbumId() {
		return get("album_id");
	}

	public void setUserId(java.lang.Long userId) {
		set("user_id", userId);
	}

	public java.lang.Long getUserId() {
		return get("user_id");
	}

	public void setAlbumName(java.lang.String albumName) {
		set("album_name", albumName);
	}

	public java.lang.String getAlbumName() {
		return get("album_name");
	}

	public void setAlbumUrl(java.lang.String albumUrl) {
		set("album_url", albumUrl);
	}

	public java.lang.String getAlbumUrl() {
		return get("album_url");
	}

	public void setAlbumLabel(java.lang.String albumLabel) {
		set("album_label", albumLabel);
	}

	public java.lang.String getAlbumLabel() {
		return get("album_label");
	}

	public void setBrowseNum(java.lang.Long browseNum) {
		set("browse_num", browseNum);
	}

	public java.lang.Long getBrowseNum() {
		return get("browse_num");
	}

	public void setAuthority(java.lang.Integer authority) {
		set("authority", authority);
	}

	public java.lang.Integer getAuthority() {
		return get("authority");
	}

	public void setPassword(java.lang.String password) {
		set("password", password);
	}

	public java.lang.String getPassword() {
		return get("password");
	}

	public void setGmtCreate(java.util.Date gmtCreate) {
		set("gmt_create", gmtCreate);
	}

	public java.util.Date getGmtCreate() {
		return get("gmt_create");
	}

}
