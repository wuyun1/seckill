-- ��ɱִ�д洢����
DELIMITER $$



  -- console ; ת���� $$
-- ����洢����
-- ������ in ��������� out �������
-- row_count()��������һ���޸�����sql(delete,insert,update)��Ӱ������
-- row_count()�� 0��δ�޸�����    >0 ��ʾ�޸ĵ�����  <0 sql����/δִ���޸�sql
CREATE PROCEDURE `seckill`.`execute_seckill`(
  IN v_seckill_id BIGINT,
  IN v_phone BIGINT,
  IN v_kill_time TIMESTAMP,
  OUT r_result INT
)
BEGIN
	DECLARE
		insert_count INT DEFAULT 0 ;
	START TRANSACTION;
	INSERT IGNORE INTO  success_killed(seckill_id, user_phone, create_time)
		VALUES(  v_seckill_id,  v_phone,  v_kill_time);
	SELECT ROW_COUNT() INTO insert_count;
	IF(insert_count = 0) THEN
		ROLLBACK;
		SET r_result = -1;
	ELSEIF(insert_count < 0) THEN
		ROLLBACK;
		SET R_result = -2;
	ELSE
		UPDATE seckill SET NUMBER = NUMBER -1 WHERE
			seckill_id = v_seckill_id 
			AND end_time > v_kill_time 
			AND start_time < v_kill_time 
			AND NUMBER > 0;
		SELECT ROW_COUNT() INTO insert_count;
		IF(insert_count = 0) THEN
			ROLLBACK;
			SET  r_result = 0;
		ELSEIF(insert_count < 0) THEN
			ROLLBACK;
			SET  r_result = -2;
		ELSE
			commit;
			SET  r_result = 1;
		END IF;
	END IF;
  END;
  $$
  -- �洢�γ̽���
  DELIMITER ;
  
  SET @r_result=-3;
  -- ִ�д洢����
  call execute_seckill(1003,18711180769,now(),@r_result);
  
  -- ��ȡ���
  select @r_result;
  
  -- �洢����
  -- 1�� �洢�����Ż��� �����м������е�ʱ��
  -- 2����Ҫ���������洢����
  -- 3���򵥵��߼�����Ӧ�ô洢����
  -- 4��QPS�� һ����ɱ��6000