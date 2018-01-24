package com.hafu.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hafu.entity.HfPrompt;
import com.hafu.repository.jpa.HfPromptDao;
import com.hafu.repository.mybatis.HfPromptMybatisDao;

@Service
public class HfPromptService {

	@Resource
	private HfPromptDao promptDao;

	@Resource
	private HfPromptMybatisDao promptMybatisDao;

	public HfPrompt save(HfPrompt prompt) {
		return promptDao.save(prompt);
	}

	public List<HfPrompt> getPromptByCode(String code) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("code", code);
		param.put("today", date);
		return promptMybatisDao.listPrompt(param);
	}

	public HfPrompt deletePrompt(final Long id, final String code) {
		HfPrompt prompt = promptDao.findOne(new Specification<HfPrompt>() {

			@Override
			public Predicate toPredicate(Root<HfPrompt> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.and(cb.equal(root.get("code"), code), cb.equal(root.get("id"), id),
						cb.equal(root.get("deleteFlag"), 0));
			}
		});
		if (prompt != null) {
			prompt.setDeleteFlag(1);
			return promptDao.save(prompt);
		}
		return null;
	}

	public HfPrompt findOne(final Long id, final String code) {
		return promptDao.findOne(new Specification<HfPrompt>() {

			@Override
			public Predicate toPredicate(Root<HfPrompt> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.and(cb.equal(root.get("code"), code), cb.equal(root.get("id"), id),
						cb.equal(root.get("deleteFlag"), 0));
			}
		});
	}
	
	public List<HfPrompt> findAll() {
		return promptDao.findAll(new Specification<HfPrompt>() {
			@Override
			public Predicate toPredicate(Root<HfPrompt> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.and(cb.equal(root.get("deleteFlag"), 0));
			}
		});
	}
}
