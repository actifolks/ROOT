package com.hema.api;

import java.sql.SQLException;
import java.util.HashMap;

import com.hema.model.GenericBoundary;
import com.hema.model.HemaData;
import com.hema.model.RectangleBoundary;
import com.javadocmd.simplelatlng.LatLng;

public class HemaRectangleGridUtil {

	public static RectangleBoundary makeRectangurlarGrid(GenericBoundary userInputBoundary){
		
		RectangleBoundary rectangleBoundary = new RectangleBoundary();
		
		Double lattitude1 = null;
		Double lattitude2 = null;
		Double longitude1 = null;
		Double longitude2 = null;
		
		for(LatLng boundaryPoint : userInputBoundary.boundaryPoints){
			
			if(lattitude1 == null || lattitude1 >= boundaryPoint.getLatitude() ){
				lattitude1 = boundaryPoint.getLatitude();
			}
			if(lattitude2 == null || lattitude2 <= boundaryPoint.getLatitude() ){
				lattitude2 = boundaryPoint.getLatitude();
			}
			if(longitude1 == null || longitude1 >= boundaryPoint.getLongitude() ){
				longitude1 = boundaryPoint.getLongitude();
			}
			if(longitude2 == null || longitude2 <= boundaryPoint.getLongitude() ){
				longitude2 = boundaryPoint.getLongitude();
			}
			
		}
		
		HashMap<Integer, HemaData> grid;
		
		try {
			grid = DatabaseUtil.getInstance().getGridDataInBoundary(lattitude1, lattitude2, longitude1, longitude2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		rectangleBoundary.grid = grid;
		
		HemaData startPoint = HemaApi.getNearestPointFromDB(userInputBoundary.startPoint);
		HemaData endPoint = HemaApi.getNearestPointFromDB(userInputBoundary.endPoint);
		
		rectangleBoundary.startPoint = startPoint;
		rectangleBoundary.endPoint = endPoint;
		
		rectangleBoundary.p1 = HemaApi.getNearestPointFromDB(new LatLng(lattitude1, longitude1));
		rectangleBoundary.p2 = HemaApi.getNearestPointFromDB(new LatLng(lattitude1, longitude2));
		rectangleBoundary.p3 = HemaApi.getNearestPointFromDB(new LatLng(lattitude2, longitude2));
		rectangleBoundary.p4 = HemaApi.getNearestPointFromDB(new LatLng(lattitude2, longitude1));
		
		return rectangleBoundary;
		
	}
	
}
