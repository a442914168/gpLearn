package com.hb.spring.demo.service.impl;

import com.hb.spring.demo.service.IQueryService;
import com.hb.spring.framework.annotation.HBService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 查询业务
 * @author Tom
 *
 */
@HBService
public class QueryService implements IQueryService {

	/**
	 * 查询
	 */
	public String query(String name) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
		return json;
	}

}
