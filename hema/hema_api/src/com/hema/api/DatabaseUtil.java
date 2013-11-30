package com.hema.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

import com.hema.model.HemaData;
import com.hema.model.path.DownStreamPath;

public class DatabaseUtil {

private static DatabaseUtil databaseUtil;
	
	Connection con;
	
	private DatabaseUtil() throws SQLException, ClassNotFoundException{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:riverdata", "system", "riverdata");
	}
	
	public static DatabaseUtil getInstance(){
		if(databaseUtil == null){
			try {
				databaseUtil = new DatabaseUtil();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	public void persistQueueData(Queue<HemaData> queueData, HashMap<Integer, DownStreamPath> downStreamPathPerPoint, ArrayList<DownStreamPath> finalDownStreamPaths){
		
		
		
	}
	
	public HashMap<Integer, HemaData> getGridDataInBoundary(double lattitude1, double lattitude2, double longitude1, double longitude2) throws SQLException{
		
		PreparedStatement ps = con.prepareStatement("select id, lattitude, longitude, east_point, west_point, north_point, south_point, elevation from hema_data" +
				" where lattitude >=" + lattitude1 +" and lattitude <= " + lattitude2 + " and longitude >= " + longitude1 +"and longitude <=" + longitude2 + " order by id asc");
		
		HashMap<Integer, HemaData> grid = new HashMap<Integer, HemaData>();
		
		ResultSet rs = ps.executeQuery();
		HemaData data = null;
		
		while(rs.next()){
			
			Integer id = rs.getInt("id");
			Double lattitude = rs.getDouble("lattitude");
			Double longitude = rs.getDouble("longitude");
			Integer east_point = rs.getInt("east_point");
			Integer west_point = rs.getInt("west_point");
			Integer north_point = rs.getInt("north_point");
			Integer south_point = rs.getInt("south_point");
			Double elevation = rs.getDouble("elevation");
			
			data = new HemaData(id, lattitude, longitude, east_point, west_point, north_point, south_point, elevation);
			grid.put(id, data);
		}
		rs.close();
		ps.close();
		con.commit();
		return grid;
		
	}
	
	public static void main(String args[]) throws SQLException{
		
		DatabaseUtil.getInstance().getGridDataInBoundary(0, 0, 0, 0);
		
	}
	
	public ArrayList<HemaData> getNearestPointFromDB(double lattitude, double longitude) throws SQLException{
		
		double lattitude1 = (Math.floor(lattitude * 10000.0))/10000.0;
		double lattitude2 = (Math.floor(lattitude * 10000.0 + 1))/10000.0;
		double longitude1 = (Math.floor(longitude * 10000.0))/10000.0;
		double longitude2 = (Math.floor(longitude * 10000.0 + 1))/10000.0;
		
		ArrayList<HemaData> nearestPoints = new ArrayList<HemaData>();
		
		PreparedStatement ps = con.prepareStatement("select id, lattitude, longitude, east_point, west_point, north_point, south_point, elevation from hema_data" +
				" where lattitude >=" + lattitude1 +" and lattitude <= " + lattitude2 + " and longitude >= " + longitude1 +"and longitude <=" + longitude2 + " order by id asc");
		
		ResultSet rs = ps.executeQuery();
		HemaData data = null;
		
		while(rs.next()){
			
			Integer id = rs.getInt("id");
			Double lattitudeDB = rs.getDouble("lattitude");
			Double longitudeDB = rs.getDouble("longitude");
			Integer east_point = rs.getInt("east_point");
			Integer west_point = rs.getInt("west_point");
			Integer north_point = rs.getInt("north_point");
			Integer south_point = rs.getInt("south_point");
			Double elevation = rs.getDouble("elevation");
			
			data = new HemaData(id, lattitudeDB, longitudeDB, east_point, west_point, north_point, south_point, elevation);
			nearestPoints.add(data);
		}
		rs.close();
		ps.close();
		con.commit();
		return nearestPoints;
		
	}
	
}
