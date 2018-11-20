package com.album.service;

import java.util.List;

import com.album.model.Album;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Record;

public interface AlbumService {
	public List<Album> find_albums(Long userId);
	public Long add_album(Album album);
	public boolean add_photo(Long album_id,String[] photos_url);
	public boolean del_photo(Long album_id,Long[] photos_id);
	public List<Album> search_album(Long userId,String condition);
	public JSONArray album_detail(Long album_id);

}
