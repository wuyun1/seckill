package org.seckill.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TTest {
	public static void main(String[] args) throws ClassNotFoundException, InterruptedException {
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=utf8";
//			String username = "root";
//			String password = "123456";
//
//			com.mchange.v2.c3p0.ComboPooledDataSource CDS = new ComboPooledDataSource();
//			CDS.setJdbcUrl(url);
//			CDS.setPassword(password);
//			CDS.setDriverClass("com.mysql.jdbc.Driver");
//			CDS.setUser(username);
//			try {
//				Connection con = CDS.getConnection();
//				Statement createStatement = con.createStatement();
//				ResultSet executeQuery = createStatement
//						.executeQuery("select * from seckill");
//				ResultSetMetaData metaData = executeQuery.getMetaData();
//				int columnCount = metaData.getColumnCount();
//				while (executeQuery.next()) {
//					System.out.println("  \n");
//					for (int i = 1; i <= columnCount; i++) {
//						String columnName = metaData.getColumnName(i);
//						System.out.println(columnName + ":"
//								+ executeQuery.getObject(columnName));
//					}
//				}
//				con.close();
//			} catch (SQLException se) {
//				System.out.println("数据库连接失败！");
//				se.printStackTrace();
//			}
//
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (PropertyVetoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:spring/spring-dao.xml"); 
//		ComboPooledDataSource bean = (ComboPooledDataSource) ac.getBean("dataSource");
//		
//		String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=utf8";
//		String username = "root";
//		String password = "123456";
//		System.out.println(url);
//		System.out.println(bean.getJdbcUrl());
//		
//
//		bean.setJdbcUrl(url);
//		bean.setPassword(password);
//		try {
//			bean.setDriverClass("com.mysql.jdbc.Driver");
//		} catch (PropertyVetoException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		bean.setUser(username);
//
//		Thread.sleep(2000);
//		System.out.println(bean.getUser());
//		System.out.println(bean.getPassword());
//		System.out.println(bean.getDriverClass());
//		System.out.println(bean.getJdbcUrl());
		
		final Logger log = LoggerFactory.getLogger(TTest.class);
		log.info("asdfa");
		
	}

}
