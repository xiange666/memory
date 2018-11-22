package com.album.service.imp;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import com.album.model.Album;
import com.album.model.AlbumLinkPhoto;
import com.album.model.Photo;
import com.album.service.AlbumService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

public class AlbumServiceImp implements AlbumService{
	
	@Override
	public List<Album> find_albums(Long userId) {
		// TODO Auto-generated method stub
		return Album.dao.find("select * from album where user_id=? "
				+ "order by gmt_create desc",userId);
	}

	@Override
	public Long add_album(Album album) {
		// TODO Auto-generated method stub
		if(album.save())
		{
			return album.getAlbumId();
		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean add_photo(Long album_id, List<String> photos_url) {
		// TODO Auto-generated method stub
		boolean succeed=Db.tx(new IAtom() {
			public boolean run()
			{
				for (String url : photos_url) {
					Photo photo=new Photo();
					photo.setPhotoUrl(url);
					photo.setMarkTime(new Date());
					if(!photo.save())
					{
						return false;
					}
					AlbumLinkPhoto aLinkPhoto=new AlbumLinkPhoto();
					aLinkPhoto.setAlbumId(album_id);
					aLinkPhoto.setPhotoId(photo.getPhotoId());
					aLinkPhoto.setPhotoUrl(photo.getPhotoUrl());
					aLinkPhoto.setMarkTime(photo.getMarkTime());
					if(!aLinkPhoto.save())
						return false;	
				}
				
				return true;
			}
		});
		return succeed;
			
		
		
	}

	@Override
	public boolean del_photo(Long album_id, Long[] photos_id) {
		// TODO Auto-generated method stub
		boolean succeed=Db.tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				// TODO Auto-generated method stub
				for (Long id : photos_id) {
					int result=Db.update("delete from album_link_photo "
							+ "where album_id=? and photo_id=?",album_id,id);
					if(result==0)
						return false;
					if(!Photo.dao.deleteById(id))
						return false;
				}
				return true;
			}
		});
		return succeed;
	}

	@Override
	public List<Album> search_album(Long userId, String condition) {
		// TODO Auto-generated method stub
		return Album.dao.find("select * from album "
				+ "where album_name like ? and user_id=?","%"+condition+"%",userId);
		
	}

	@Override
	public JSONArray album_detail(Long album_id) {
		// TODO Auto-generated method stub
		JSONArray jsonArray=new JSONArray();
		
		List<Record> time=Db.find("select mark_time,count(photo_id) as num from album_link_photo "
				+ "where album_id=? order by mark_time desc group by mark_time ",album_id);
		for (Record record : time) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("mark_time", record.getDate("mark_time"));
			jsonObject.put("count", record.getBigInteger("num"));
			List<Record> photos=Db.find("select photo_id,photo_url from album_link_photo "
					+ "where mark_time and album_id=?",record.getDate("mark_time"),album_id);
			jsonObject.put("photos", photos);
			jsonArray.add(jsonObject);
		}
		
		return jsonArray;
	}
	
	


}
