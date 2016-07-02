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
	// md5��ֵ�ַ���,���ڻ���MD5
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
		// �Ż��㣺 �����Ż�
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

		// ת���ض��ַ����Ĺ���,������
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);
	}

	@Transactional
	/**
	 * ʹ��ע��������񷽷����ŵ� 1:�����ŶӴ��һ��Ԥ��,��ȷ��ע���񷽷��ı�̷��
	 * 2:��֤���񷽷���ִ��ʱ�価���ܶ�,��Ҫ�������������������,RPC/HTTP������߰��뵽�����ⲿ
	 * 3:�������Եķ�������Ҫ����,��ֻ��һ���޸Ĳ���,ֻ����������Ҫ�������.
	 */
	@Override
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {

		try {
			if (md5 == null || !md5.equals(getMD5(seckillId))) {
				throw new SeckillException("seckill data rewrite");
			}

			// ִ����ɱ�߼�:�����, �Ӽ�¼������Ϊ
			// ��¼������Ϊ
			int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
			// Ψһ:seckillId , userPhone
			if (insertCount <= 0) {
				// �ظ���ɱ
				throw new RepeatKillException("Seckill repeated");
			} else {
				// ����棬�ȵ���Ʒ����
				Date nowTime = new Date();
				int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
				if (updateCount <= 0) {
					// û�и��µ���¼,
					throw new SeckillCloseException("seckill is closed");
				} else {
					// ��ɱ�ɹ�
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
			// ���Ա������쳣,ת��Ϊ�������쳣
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
