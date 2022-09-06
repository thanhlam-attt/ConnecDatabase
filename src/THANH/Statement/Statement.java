package THANH.Statement;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Statement {
	// Information
	private static String URLname = "jdbc:sqlserver://THANHLAM\\THANH:1433;" + "databaseName=QLSV;encrypt=false";
	private static String Username = "sa";
	private static String Password = "123";

	// Connec to Database
	public static Connection getConnection() throws SQLException {
		Connection connec = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connec = DriverManager.getConnection(URLname, Username, Password);
			System.out.println("CONNECTION SUCCESSFULLY!!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("CONNECTION FAILED!!");
		}
		return connec;
	}

// Close Connection
	public static void closeConnection(Connection connec) {
		try {
			connec.close();
			System.out.println("CONNECTION WAS CLOSED!!!");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("CLOSE CONNECTION FAILED!!");
		}
	}


	public static void main(String[] args) throws SQLException {
		Connection connec = getConnection();
		java.sql.Statement state = connec.createStatement(); // Declare Statement
		
//INSERT AND SHOW DATA -- SOLUTION-1: USED TO STATEMENT		
		// Insert data: executeUpdate(Thực hiện những truy vấn như create, drop, insert, update, delete,...)
			state.executeUpdate("INSERT INTO dbo.Sinhvien VALUES('AT7',N'Lâm Tăng Thành', N'Nam','20021017',N'Hà Nội',N'AT17C')"); // Comment dòng này nếu muốn chạy code
		
		// get data table Sinhvien: thực hiện truy vấn SELECT
		ResultSet rs = state.executeQuery("SELECT * FROM Sinhvien");
		
		// Show data
		while(rs.next()) {
			System.out.println(rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getDate(4) + "  " + rs.getString(5) + "  " + rs.getString(6));
		}
		
//INSERT AND SHOW DATA -- SOLUTION-2: USED TO PREPAREDSTATEMENT				
		// PreparedStatement: is sub-interface of Statement interface
		String sqlInsert = "INSERT INTO Sinhvien VALUES()";
		String selectAll = "SELECT * FROM Sinhvien";
		
		PreparedStatement preState = connec.prepareStatement(sqlInsert);
		// Muốn chạy được code thì comment mấy dòng dưới này
			preState.setString(1, "AT9");
			preState.setString(2, "Lâm Tăng Thành 1");
			preState.setString(3, "Nam");
			preState.setDate(4, Date.valueOf("2002-10-17"));
			preState.setString(5, "Hà Nội");
			preState.setString(6, "AT17B");
			preState.execute(); // Run prepared
		
		preState = connec.prepareStatement(selectAll);
		
		// get data table Sinhvien: thực hiện truy vấn SELECT
		ResultSet resultSet = preState.executeQuery();

		// Show data
		while(resultSet.next()) {
				System.out.println(resultSet.getString(1) + "  " + resultSet.getString(2) + "  " + resultSet.getString(3) + "  " + resultSet.getDate(4) + "  " 
									+ resultSet.getString(5) + "  " + resultSet.getString(6));
		}
		
// DATABASEMETADATA: dùng để lấy metadata của cơ sở dữ liệu
		DatabaseMetaData datameta = connec.getMetaData();
		System.out.println("Driver Name: " + datameta.getDriverName());
        System.out.println("Driver Version: " + datameta.getDriverVersion());
        System.out.println("UserName: " + datameta.getUserName());
        System.out.println("Database Product Name: "
                + datameta.getDatabaseProductName());
        System.out.println("Database Product Version: "
                + datameta.getDatabaseProductVersion());
		
		preState.close();
		closeConnection(connec);
	}
}
