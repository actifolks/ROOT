package com.hema.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hema.model.HemaData;
import com.javadocmd.simplelatlng.LatLng;

public class DatabaseUtil {

private static DatabaseUtil databaseUtil;
	
	Connection con;
	
	private DatabaseUtil() throws SQLException, ClassNotFoundException{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:riverdata", "system", "riverdata");
	}
	
	public static DatabaseUtil getInstance() throws SQLException, ClassNotFoundException{
		if(databaseUtil == null){
			databaseUtil = new DatabaseUtil();
		}
		return databaseUtil;
	}
	
	public void insertPoint(Integer id, Double lattitude, Double longitude, Integer east_point, Integer west_point, Integer north_point, Integer south_point) throws SQLException{
		
		PreparedStatement ps = con.prepareStatement("insert into hema_data(id, lattitude, longitude, east_point, west_point, north_point, south_point) " +
				"values("+ id +", "+ lattitude +", "+ longitude +", "+ east_point +", "+ west_point  + ", "+ north_point  + ", "+ south_point  +")");
		ps.execute();
		ps.close();
		con.commit();
		
	}
	
	public HemaData getHemaDataById(Integer id) throws SQLException{
		
		PreparedStatement ps = con.prepareStatement("select id, lattitude, longitude, east_point, west_point, north_point, south_point from hema_data" +
				" where id=" + id + " order by id asc");
		
		ResultSet rs = ps.executeQuery();
		HemaData data = null;
		
		while(rs.next()){
			
			Double lattitude = rs.getDouble("lattitude");
			Double longitude = rs.getDouble("longitude");
			Integer east_point = rs.getInt("east_point");
			Integer west_point = rs.getInt("west_point");
			Integer north_point = rs.getInt("north_point");
			Integer south_point = rs.getInt("south_point");
			
			data = new HemaData(id, lattitude, longitude, east_point, west_point, north_point, south_point);
			
		}
		rs.close();
		ps.close();
		con.commit();
		return data;
	}
	
}
