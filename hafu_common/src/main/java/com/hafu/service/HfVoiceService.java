package com.hafu.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hafu.entity.HfVoice;
import com.hafu.repository.jpa.HfVoiceDao;

@Service
public class HfVoiceService {
	
	@Resource
	private HfVoiceDao hfVoiceDao;
	
	public List<HfVoice> getVoiceList(final Long userId,final String code){
		return hfVoiceDao.findAll(new Specification<HfVoice>() {
			public Predicate toPredicate(Root<HfVoice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
	}
	/**
	 * 添加 手机用户编号   添加语音的时候 顺便把手机用户编号也添加上去
	 * @param serverId
	 * @param voiceMsg
	 * @param userId
	 * @param voiceSpeakerId
	 * @param code
	 * @param voicePath
	 * @param duration
	 * @param voiceUrl
	 * @return
	 */
	public boolean saveVoice(String serverId,String voiceMsg,Long userId,Long voiceSpeakerId,String code,String voicePath,Integer duration,String voiceUrl){
		HfVoice entity = new HfVoice();
		entity.setServerId(serverId);
		entity.setVoiceMsg(voiceMsg);
		entity.setUserId(userId);
		entity.setVoiceSpeakerId(voiceSpeakerId);
		entity.setCode(code);
		entity.setVoicePath(voicePath);
		entity.setSource(0);
		entity.setDeleteFlag(0);
		entity.setVoiceUrl(voiceUrl);
		entity.setCreateDate(new Date());
		entity.setDuration(duration);
		entity.setIsRead(1);
		entity.setLength(0);
		HfVoice s = hfVoiceDao.save(entity);
		return s.getCode() !="" ? true : false;
	}
	
	public Integer getNotReadNum(final String code,final Long userId){
		List<HfVoice> list = hfVoiceDao.findAll(new Specification<HfVoice>() {
			public Predicate toPredicate(Root<HfVoice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				list.add(cb.equal(root.get("isRead"), 0	));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
		return list.size();
	}

	/**
	 * 根据语音编号  和 手机用户编号一起来修改语音的状态
	 * @param id
	 */
	public void updateReadStatus(Long id,Long voiceSpeakerId ) {
		HfVoice hfVoice = hfVoiceDao.findOne(id);
		hfVoice.setIsRead(1);
		hfVoice.setVoiceSpeakerId(voiceSpeakerId);
		hfVoiceDao.save(hfVoice);
	}
	
	public HfVoice update(HfVoice hfVoice){
		return hfVoiceDao.save(hfVoice);
	}
}
