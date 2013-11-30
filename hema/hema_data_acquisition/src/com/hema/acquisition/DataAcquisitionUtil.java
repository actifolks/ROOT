package com.hema.acquisition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.hema.api.DatabaseUtil;
import com.hema.model.HemaData;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class DataAcquisitionUtil {

	static LatLng p1 = new LatLng(0, 65);
	static LatLng p2 = new LatLng(0, 100);
	static LatLng p3 = new LatLng(38, 100);
	static LatLng p4 = new LatLng(38, 65);
	
	public static Integer latsSize;
	public static Integer longSize;
	
	public static void acquireData(){
		
		double d = LatLngTool.distance(p1, p2, LengthUnit.METER);
		System.out.println(Math.round(d));
		
		double d1 = LatLngTool.distance(p1, p4, LengthUnit.METER);
		System.out.println(Math.round(d1));
		
		ArrayList<Double> longs = new  ArrayList<Double>();
		
		int i;
		
		for(i = 0; i < (int) d; i ++ ){
			
			LatLng p = LatLngTool.travel(p1, 90, i, LengthUnit.METER);
			longs.add(p.getLongitude());
			
		}
		
		ArrayList<Double> lats = new  ArrayList<Double>();
		
		for(i = 0; i < (int) d1; i ++ ){
			
			LatLng p = LatLngTool.travel(p1, 0, i, LengthUnit.METER);
			lats.add(p.getLatitude());
			
		}
		
		System.out.println(i);
		
		System.out.println(longs.size());
		
		System.out.println(lats.size());
		
		HashMap<String, Integer> pointer = new HashMap<String, Integer>();
		
		Integer id = 1;
		
		latsSize = lats.size();
		longSize = longs.size();
		
		//According to calculation id will be longSize * m + n + 1
		
		for(int m = 0; m < latsSize; m++){
			
			double lat = lats.get(m);
			
			long startTime = Calendar.getInstance().getTimeInMillis();
			
			for(int n = 0; n < longSize; n++){
				
				double lon = longs.get(n);
				
				Integer east_point = longSize * m + n + 2;
				Integer west_point = longSize * m + n;
				Integer north_point = longSize * (m + 1) + n + 1;
				Integer south_point = longSize * (m - 1) + n + 1;
				
				if(m == 0){
					south_point = null;
				}else if(m == latsSize - 1){
					north_point = null;
				}
				
				if(n == 0){
					west_point = null;
				}else if(n == longSize - 1){
					east_point = null;
				}
				
				DatabaseUtil.getInstance().insertPoint(id, lat, lon, east_point, west_point, north_point, south_point);
				
				id = id + 1;
				
			}
			
			long endTime = Calendar.getInstance().getTimeInMillis();
			
			System.out.println(endTime-startTime);
			
			if(m%10 == 0){
				System.out.println(m);
			}
			
		}
		
		System.out.println(pointer.size());
		
		
	}

	public static void main(String args[]){
		
		acquireData();
	
		
		
	}
	
	public void assignGridId(){
		
		Integer gridId = 1;
		
		for(int m = 0; m < latsSize; m = m + 158){
			
			for(int n = 0; n < longSize; n = n + 158){
				
				
				
				gridId++;
				
			}
			
		}
		
	}
	
}
