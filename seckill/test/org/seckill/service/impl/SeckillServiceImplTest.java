package org.seckill.service.impl;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml" })
public class SeckillServiceImplTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SeckillService seckillService;

	@Test
	public void testGetSeckillList() {
		List<Seckill> list = seckillService.getSeckillList();
		logger.info("list={}", list);
	}

	@Test
	public void testGetById() {
		long id = 1000;
		Seckill seckill = seckillService.getById(id);
		logger.info("seckill={}", seckill);
	}

	@Test
	public void testSeckillLogic() throws Exception {
		long seckillId = 1001;
		Exposer exportSeckillUrl = seckillService.exportSeckillUrl(seckillId);
		if (exportSeckillUrl.isExposed()) {
			long userPhone = 1861113372l;
			String md5 = exportSeckillUrl.getMd5();
			try {
				SeckillExecution executeSeckill = seckillService.executeSeckill(seckillId, userPhone, md5);
				logger.info("executeSeckill={}", executeSeckill);
			} catch (RepeatKillException e) {
				logger.error(e.getMessage());
			} catch (SeckillCloseException e) {
				logger.error(e.getMessage());
			} catch (SeckillException e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.warn("expose={}", exportSeckillUrl);
		}

	}

	@Test
	public void testExecuteSeckillProcedure() {
		long seckillId = 1001;
		long phone = 1368011101;
		Exposer export = seckillService.exportSeckillUrl(seckillId);
		if (export.isExposed()) {
			String md5 = export.getMd5();
			SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
			logger.info(execution.getStateInfo());
		}
	}

}
