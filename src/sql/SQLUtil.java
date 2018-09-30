package sql;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import Bean.ProjectcostBean;
import Util.CollectProjectCost;

import java.sql.Connection;

public class SQLUtil {
	static String TABLEESQL =  "CREATE TABLE projectcost("
			              + "productType varchar(20),"
			              + "productName varchar(30) not null,"
			              + "operateType varchar(20),"
			              + "operateName varchar(40),"
			              + "productOutput int,"
			              + "materialCost float,"
			              + "manalCoat float,"
			              + "depreciatCost float,"
			              + "productCost float,"
			              + "inMaterialCost float,"
			              + "inManalCost float,"
			              + "inDepreciatCost float,"
			              + "inProductCost float"
			              + ")charset=utf8;";
	static String TABLE ="CREATE TABLE name(productName varchar(30) not null)charset=utf8;";
	static String CREATESQL = "create database projectcost";
	static String DROPDATABASE = "drop database if exists projectcost";
	static Connection connection = null;
	static Statement stmt = null;
	static ResultSet rs = null;
	
	public static void initDataSQL() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "123456");
//			System.out.println(connection);
			stmt = connection.createStatement();
			if( stmt != null) {
				System.out.println("连接数据库成功！");
			}

			stmt.execute(DROPDATABASE);				
			stmt.executeUpdate(CREATESQL);	
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectcost","root", "123456");
			stmt = connection.createStatement();
			if(stmt != null) {
				System.out.println("连接新数据库成功！");
			}
			int result = stmt.executeUpdate(TABLEESQL);
			stmt.executeUpdate(TABLE);
			if(result ==0) {
				System.out.println("创建表成功！");
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void closeSQL() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int addName(String name) {
		String sqlInsert = "insert into name(productName) values(?)";
		int result = 0;
		try {
			PreparedStatement presta = connection.prepareStatement(sqlInsert);
			presta.setString(1, name);
			result = presta.executeUpdate();
			presta.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	//添加数据到数据库
	public static int add(ProjectcostBean prBean) {
		String sqlInert = "insert into projectcost(productType,productName,operateType,operateName,productOutput,"
				+ "materialCost,manalCoat,depreciatCost,productCost,inMaterialCost,inManalCost,inDepreciatCost,inProductCost) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int result = 0;
		try {
//			Connection connect  = getConnect();
			PreparedStatement preState = connection.prepareStatement(sqlInert);
			preState.setString(1, prBean.getProductType());
			preState.setString(2, prBean.getProductName());
			System.out.println(prBean.getProductName());
			preState.setString(3, prBean.getOperateType());
			preState.setString(4, prBean.getOperateName());
			preState.setInt(5, prBean.getProductOutput());
			preState.setFloat(6, prBean.getMaterialCost());
			preState.setFloat(7, prBean.getManalCoat());
			preState.setFloat(8, prBean.getDepreciatCost());
			preState.setFloat(9, prBean.getProductCost());
			preState.setFloat(10, prBean.getInMaterialCost());
			preState.setFloat(11, prBean.getInManalCost());
			preState.setFloat(12, prBean.getInDepreciatCost());
			preState.setFloat(13, prBean.getInProductCost());
			result = preState.executeUpdate();
			preState.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static void delete() {
		
	}
	
	public static ResultSet query(String productname) {
		String querysql = "select * from projectcost where productName='"+productname+"'";
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
//			System.err.println(querysql);
			resultSet = statement.executeQuery(querysql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	}
	
	public static ResultSet queryAllName() {
		String query = "select DISTINCT productName from name";
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
//			System.err.println(query);
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	}
	
}
