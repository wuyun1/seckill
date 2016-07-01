package org.seckill.dao;

import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//∏ÊÀﬂjunit spring≈‰÷√Œƒº˛
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
	
	@Resource
	private SuccessKilledDao successKilledDao;
	
	@Test
	public void testInsertSuccessKilled() {
		long id = 1003L;
		long phone = 18711180761L;
		int insertSuccessKilled = successKilledDao.insertSuccessKilled(id, phone);
		System.out.println(insertSuccessKilled);
	}

	@Test
	public void testQueryByIdWithSeckill() {
		SuccessKilled queryByIdWithSeckill = successKilledDao.queryByIdWithSeckill(1002,18711180761L);
		System.out.println(queryByIdWithSeckill);
	}

}
