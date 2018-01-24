package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户与绑定设备关系表。 
 * 用户与用户关系均通过该表管理查询
 */
@Entity
@Table(name="hf_concern_user")
public class HfConcernUser extends IdEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId; //被关注人id
	private Long concernUserId; //添加设备的人的id ;
	private String relations;    //关注人与被关注人的关系
	private Date createDate; //创建时间
	private String nickName;
	
	public String getRelations() {
		return relations;
	}
	public void setRelations(String relations) {
		this.relations = relations;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getConcernUserId() {
		return concernUserId;
	}
	public void setConcernUserId(Long concernUserId) {
		this.concernUserId = concernUserId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
	
			
}
