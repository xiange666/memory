package com.album.service.imp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.album.common.config.Constant;
import com.album.common.config.ResultCodeEnum;
import com.album.model.Album;
import com.album.model.AlbumLinkPhoto;
import com.album.model.CollectPhoto;
import com.album.model.Photo;
import com.album.model.ThumbUp;
import com.album.service.OssClientUtil;
import com.album.service.PhotoService;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

public class PhotoServiceImp implements PhotoService{


	@Override
	public boolean favorite_photo(CollectPhoto collectPhoto) {
		boolean succeed=Db.tx(new IAtom() {	
			@Override
			public boolean run() throws SQLException {
				if(CollectPhoto.dao.findFirst("SELECT * FROM collect_photo WHERE photo_id=?",collectPhoto.getPhotoId())==null) {
					if(collectPhoto.save()) {
						Photo photo=Photo.dao.findById(collectPhoto.getPhotoId());
//						photo.set("collect_id", photo.getCollectId()+1);//数据库未更新，暂时注释
						photo.update();
						return true;
					}else {
						return false;
					}
				}else {
					return false;
				}
			}
		});		
		return succeed;
	}

	@Override
	public Photo show_photo_info(Long photoId) {
		//数据库中需要新加入冗余字段才能这么写，要不然需要连接表，返回值类型改为JSONObject
		return Photo.dao.findFirst("SELECT photo_id,public_id,public_name,photo_url,mark_people,mark_time,mark_place,mark_story,like_num,collcet_num "
									+"FROM photo WHERE photo_id=?",photoId);
	}

	@Override
	public ResultCodeEnum update_photo_info(Photo photo) {
		if(Photo.dao.findById(photo.getPhotoId())!=null) {
			if(photo.update()) {
				return ResultCodeEnum.SUCCESS;
			}else {
				return ResultCodeEnum.UPDATE_ERROR;
			}
		}else {
			return ResultCodeEnum.DATA_EMPTY;
		}
	}

	@Override
	public boolean thumb_photo(ThumbUp thumbUp) {
		//查询，是否已经点赞过，然后点赞表保存，更新冗余字段所在的表
		boolean succeed=Db.tx(new IAtom() {	
			@Override
			public boolean run() throws SQLException {
				if(CollectPhoto.dao.findFirst("SELECT * FROM thumb_photo WHERE photo_id=?",thumbUp.getPhotoId())==null) {
					if(thumbUp.save()) {
						Photo photo=Photo.dao.findById(thumbUp.getPhotoId());
//						photo.set("collect_id", photo.getCollectId()+1);//数据库未更新，暂时注释
						photo.update();
						return true;
					}else {
						return false;
					}
				}else {
					return false;
				}
			}
		});		
		return succeed;
	}

	@Override
	public boolean del_photos(List<Long> photoIds,Long userId) {
		boolean succeed=Db.tx(new IAtom() {	
			@Override
			public boolean run() throws SQLException {
				for(Long photoId:photoIds) {
					Photo delPhoto = Photo.dao.findById(photoId);
					if(delPhoto!=null) {
						//在OSS上删除，在photo表中删除,照片直接级联删除
						
//						OssClientUtil.deleteFile(OSSClient,Constant.bucketName, Constant.folder, key);
						if(!Photo.dao.deleteById(photoId)) {
							return false;
						}
					}else {
						return false;
					}	
				}
				return true;
			}
		});		
		return succeed;
	}

	@Override
	public boolean del_albums(List<Long> albumIds, Long userId) {
		boolean succeed=Db.tx(new IAtom() {	
			@Override
			public boolean run() throws SQLException {
				for(Long albumId:albumIds) {
					Album delAlbum = Album.dao.findById(albumId);
					//先通过相册Id找到对应的照片，删除连接表，然后依次删除这些照片，最后删除相册
					List<AlbumLinkPhoto> albumLinkPhotos = AlbumLinkPhoto.dao.find("SELECT * FROM album_link_photo WHERE album_id = ?",delAlbum.getAlbumId());
					List<Long> photoIds = new ArrayList<>();
					for(AlbumLinkPhoto albumLinkPhoto : albumLinkPhotos) {
						photoIds.add(albumLinkPhoto.getPhotoId());
					}
					del_photos(photoIds,userId);
				}
				return true;
			}
		});		
		return succeed;
	}

}
