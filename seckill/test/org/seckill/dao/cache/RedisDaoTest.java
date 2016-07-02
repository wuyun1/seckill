package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//∏ÊÀﬂjunit spring≈‰÷√Œƒº˛
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
	
	private long id = 1001;
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private SeckillDao seckillDao;
	
	@Test
	public void testSeckill() throws Exception {
		
		Seckill seckill = redisDao.getSeckill(id);
		
		if(seckill == null ) {
			seckill = seckillDao.queryById(id);
			if(seckill !=null){
				String result = redisDao.putSeckill(seckill);
				System.out.println("redis put result: " +result);
				Seckill seckill2 =redisDao.getSeckill(id);
				System.out.println("2 exist: "+seckill2);
			}
			
		}else{
			Seckill seckill2 =redisDao.getSeckill(id);
			System.out.println("exist: "+seckill2);
		}
		
		
	}

}
