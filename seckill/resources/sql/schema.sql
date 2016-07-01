-- ���ݳ�ʼ���ű�

-- �������ݿ�
CREATE DATABASE seckill;

-- ʹ�����ݿ�
use seckill;
-- ������ɱ����
CREATE TABLE seckill(
seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT '��Ʒ���ID',
name varchar(120) NOT NULL COMMENT '��Ʒ����',
number int NOT NULL COMMENT '�������',
create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
start_time timestamp NOT NULL COMMENT '��ɱ����ʱ��',
end_time timestamp NOT NULL COMMENT '��ɱ����ʱ��',
PRIMARY KEY ( seckill_id )

)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='��ɱ����';

-- ��ʼ������
insert into
	seckill(name,number,start_time,end_time)
values
	('100Ԫ��ɱiPhone6',100,'2016-5-9 00:00:00','2016-5-10 00:00:00'),
	('500Ԫ��ɱipad2',200,'2016-5-9 00:00:00','2016-5-10 00:00:00'),
	('300Ԫ��ɱС��4',300,'2016-5-9 00:00:00','2016-5-10 00:00:00'),
	('200Ԫ��ɱ��ľnote',400,'2016-5-9 00:00:00','2016-5-10 00:00:00');
	
-- ��ɱ�ɹ���ϸ��
-- �û���½��֤��ص���Ϣ
CREATE TABLE success_killed(
seckill_id bigint NOT NULL COMMENT '��Ʒ���ID',
user_phone bigint NOT NULL COMMENT '�û��ֻ���',
create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
state tinyint NOT NULL DEFAULT -1 COMMENT '״̬��ʾ:	-1:��Ч 	0:�ɹ�	1:�Ѹ���	2:�ѷ���',
PRIMARY KEY ( seckill_id,user_phone )
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='��ɱ�ɹ���ϸ��';


