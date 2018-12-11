package com.album.service.imp;

import java.sql.SQLException;
import java.util.ArrayList;
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
	public boolean add_photo(Long album_id, String[] photos_url,String[] photos_key,Long user_id,String user_name) {
		// TODO Auto-generated method stub
		boolean succeed=Db.tx(new IAtom() {
			public boolean run()
			{
				for(int i=0;i<photos_url.length;i++)
				 {
					String url=photos_url[i];
					String key=photos_key[i];
					Photo photo=new Photo();
					photo.setPhotoUrl(url);
					photo.setCosKey(key);
					photo.setUserId(user_id);
					photo.setUserName(user_name);
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
	public boolean del_photo(String[] photos_id) {
		// TODO Auto-generated method stub
		for (String id : photos_id) 
		{
			if(!Photo.dao.deleteById(id))
				return false;
		}
		return true;
/*//		boolean succeed=Db.tx(new IAtom() {
//			
//			@Override
//			public boolean run() throws SQLException {
//				// TODO Auto-generated method stub
//				for (String id : photos_id) {
//					int result1=Db.update("delete from album_link_photo "
//							+ "where  photo_id=?",id);
//					
//					int result2=Db.update("delete from character "
//							+ "where photo_id=?",id);
//					int result3=Db.update("delete from thumb_up "
//							+ "where photo_id=?",id);
//					int result4=Db.update("delete from collect_photo"
//							+ "where photo_id=?",id);
//					if(result1==0||result2==0||result3==0||result4==0)
//						return false;
//					
//				}
//				return true;
//			}
		});*/
//		return succeed;
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
		
		List<Record> time=Db.find("select distinct mark_time from album_link_photo "
				+ "where album_id=? ",album_id);
		for (Record record : time) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("mark_time", record.getDate("mark_time"));
			List<AlbumLinkPhoto> photos=AlbumLinkPhoto.dao.find("select photo_id,photo_url from album_link_photo "
					+ "where mark_time=? and album_id=?",record.getDate("mark_time"),album_id);
			jsonObject.put("photos", photos);
			jsonArray.add(jsonObject);
		}
		
		return jsonArray;
	}

	@Override
	public List<String> find_key(String[] photos_id) {
		// TODO Auto-generated method stub
		List<String> keys=new ArrayList<String>();
		for(String  id:photos_id)
		{
			Photo photo=Photo.dao.findFirst("select * from photo where photo_id =?",id);
			System.out.println(id);
			if(photo==null)
			{
				return null;
			}
			System.out.println(photo.toString());
			System.out.println(photo.getCosKey());
			String key=photo.getCosKey();
			keys.add(key);
		}
		return keys;
	}

	@Override
	public boolean update_album(Album album) {
		// TODO Auto-generated method stub
		if(album.update())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	
	


}
