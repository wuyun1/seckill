package org.seckill.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * 配置spring和junit整合,junit启动时加载springIOC容器
 * spring-test,junit
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	
	//注入Dao实现类依赖
	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void testReduceNumber() {
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.DAY_OF_MONTH, -1);
		System.out.println(instance.getTime());
		int reduceNumber = seckillDao.reduceNumber(1002,instance.getTime());
		System.out.println("updateCount="+reduceNumber);
	}

	@Test
	public void testQueryById() {
		long id = 1002;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);
	}

	@Test
	public void testQueryAll() {
		List<Seckill> queryAll = seckillDao.queryAll(0, 100);
		for (Seckill seckill : queryAll) {
			System.out.println(seckill);
		}
		
	}

}
