package application;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnect3 {
	
	public Connection conn;
	
	public Connection getconn() {
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String id = "cafe3";
		String password = "1234";
		
		try {
			Class.forName(driver);
			System.out.println("디비 접속 성공-20230516");
			
			conn = DriverManager.getConnection(url, id, password);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("디비 접속 실패");
		}
		return conn;
		
	}

}
