package com.album.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseComment<M extends BaseComment<M>> extends Model<M> implements IBean {

	public void setCommentId(java.lang.Long commentId) {
		set("comment_id", commentId);
	}

	public java.lang.Long getCommentId() {
		return get("comment_id");
	}

	public void setNewsId(java.lang.Long newsId) {
		set("news_id", newsId);
	}

	public java.lang.Long getNewsId() {
		return get("news_id");
	}

	public void setCommentatorId(java.lang.Long commentatorId) {
		set("commentator_id", commentatorId);
	}

	public java.lang.Long getCommentatorId() {
		return get("commentator_id");
	}

	public void setCommentatorName(java.lang.String commentatorName) {
		set("commentator_name", commentatorName);
	}

	public java.lang.String getCommentatorName() {
		return get("commentator_name");
	}

	public void setCommentContent(java.lang.String commentContent) {
		set("comment_content", commentContent);
	}

	public java.lang.String getCommentContent() {
		return get("comment_content");
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