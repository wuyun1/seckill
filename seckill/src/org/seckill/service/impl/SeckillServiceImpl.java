package org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

//@Component @Service @Dao @Conroller
@Service
public class SeckillServiceImpl implements SeckillService {
	// md5盐值字符串,用于混淆MD5
	private final String slat = "asdjfa;sffl23lewfnas;dfl90]\nfnfasdf";
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired // @Resource
	private SeckillDao seckillDao;
	@Autowired // @Resource
	private SuccessKilledDao successKilledDao;
	@Autowired
	private RedisDao redisDao;

	@Override
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 5);
	}

	@Override
	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		// 优化点： 缓存优化
		/**
		 * get from cache if null get db else put cache locgoin
		 */
		Seckill seckill = redisDao.getSeckill(seckillId);
		if (seckill == null) {
			seckill = seckillDao.queryById(seckillId);
			if (seckill == null) {
				return new Exposer(false, seckillId);
			} else {
				redisDao.putSeckill(seckill);
			}
		}

		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date nowTime = new Date();
		if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
			return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
		}

		// 转化特定字符串的过程,不可逆
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);
	}

	@Transactional
	/**
	 * 使用注解控制事务方法的优点 1:开发团队达成一致预定,明确标注事务方法的编程风格
	 * 2:保证事务方法的执行时间尽可能短,不要穿插其他的无网络操作,RPC/HTTP请求或者剥离到事务外部
	 * 3:不是所以的方法都需要事务,如只有一条修改操作,只读操作不需要事务控制.
	 */
	@Override
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {

		try {
			if (md5 == null || !md5.equals(getMD5(seckillId))) {
				throw new SeckillException("seckill data rewrite");
			}

			// 执行秒杀逻辑:减库存, 加记录购买行为
			// 记录购买行为
			int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
			// 唯一:seckillId , userPhone
			if (insertCount <= 0) {
				// 重复秒杀
				throw new RepeatKillException("Seckill repeated");
			} else {
				// 减库存，热点商品竞争
				Date nowTime = new Date();
				int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
				if (updateCount <= 0) {
					// 没有更新到记录,
					throw new SeckillCloseException("seckill is closed");
				} else {
					// 秒杀成功
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
				}

			}

		} catch (SeckillCloseException e1) {
			throw e1;
		} catch (RepeatKillException e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 所以编译器异常,转化为运行期异常
			throw new SeckillException(e.getMessage(), e);
		}
	}

	private String getMD5(long seckillId) {
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	@Override
	public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {

		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
		}

		Date killTime = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);

		try {
			seckillDao.killByProcedure(map);
			int result = MapUtils.getInteger(map, "result",-2);
			if(result ==1){
				SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS);
			}else{
				return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
				
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
		}
	}

}
