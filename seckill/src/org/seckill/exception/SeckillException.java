package org.seckill.exception;

/**
 * ��ɱ���ҵ���쳣
 * @author Administrator
 *
 */
public class SeckillException extends RuntimeException{

	public SeckillException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeckillException(String message) {
		super(message);
	}
	
}
