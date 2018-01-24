package com.hafu.persistence;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class DynamicPageSort {

	
	/**
	 * 创建分页请求.
	 * 多条件 排序
	 */
	@SuppressWarnings("unused")
	public static PageRequest byPageRequest(int pageNumber, int pagzSize, final Collection<String> sortType) {
		Sort sort = null;		
		if(null!=sortType && sortType.size()>0)
		{	
			for(String st:sortType){
				if(!StringUtils.isEmpty(st)){
					String [] arr=st.split("_");
					if(arr[0]!=null && arr[1]!=null ){
						if(arr[1].equals("DESC")){
							if(sort == null){
								sort = new Sort(Direction.DESC, arr[0]);
							}else{
								sort.and(new Sort(Direction.DESC, arr[0]));
							}
						}else{
							if(sort == null){
								sort = new Sort(Direction.DESC, arr[0]);
							}else{
								sort.and(new Sort(Direction.ASC, arr[0])); 
							}
						}
					}
				}
			}
		}		
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	/**
	 * 分页请求 ，
	 * 单条件排序
	 * @return
	 */
	
	@SuppressWarnings("unused")
	public static PageRequest byPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;		
		if(null!=sortType && sortType.length()>0)
		{
			String [] arr=sortType.split("_");
			if(arr[0]!=null && arr[1]!=null){
				if(arr[1].equals("DESC")){
					sort = new Sort(Direction.DESC, arr[0]);
				}else{
					sort=new Sort(Direction.ASC, arr[0]); 
				}
			}
		}		
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
}
