package com.hema.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.hema.model.Direction;
import com.hema.model.HemaData;
import com.hema.model.RectangleBoundary;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class HemaApi {
	
	public final static double roundFactor = 10000.0;
	
	public static HemaData getNeighbourPoint(HemaData presentPoint, HashMap<Integer, HemaData> grid, Direction direction){
		
		Integer neigbhourPointId = null;
		
		switch (direction) {
		case EAST:
			neigbhourPointId = presentPoint.east_point;
			break;
		case WEST:
			neigbhourPointId = presentPoint.west_point;
			break;
		case NORTH:
			neigbhourPointId = presentPoint.north_point;
			break;
		case SOUTH:
			neigbhourPointId = presentPoint.south_point;
			break;
		case NORTHEAST:
			HemaData eastNPoint = getNeighbourPoint(presentPoint, grid, Direction.EAST);
			neigbhourPointId = eastNPoint.north_point;
			break;
		case NORTHWEST:
			HemaData westNPoint = getNeighbourPoint(presentPoint, grid, Direction.WEST);
			neigbhourPointId = westNPoint.north_point;
			break;
		case SOUTHEAST:
			HemaData eastSPoint = getNeighbourPoint(presentPoint, grid, Direction.EAST);
			neigbhourPointId = eastSPoint.south_point;
			break;
		case SOUTHWEST:
			HemaData westSPoint = getNeighbourPoint(presentPoint, grid, Direction.WEST);
			neigbhourPointId = westSPoint.south_point;
			break;
		default:
			break;
		}
	
		if(neigbhourPointId == null){
			return null;
		}
		
		HemaData neigbhourPoint = grid.get(neigbhourPointId);
		
		if(neigbhourPoint == null){
			
			try {
				neigbhourPoint = DatabaseUtil.getInstance().getHemaDataById(neigbhourPointId);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
		
		return neigbhourPoint;
		
	}
	
	public static ArrayList<HemaData> getAllNeighbourPoints(HemaData presentPoint, HashMap<Integer, HemaData> grid){
		
		ArrayList<HemaData> allNeighbourPoints = new ArrayList<HemaData>();
		
		allNeighbourPoints.add(getNeighbourPoint(presentPoint, grid, Direction.EAST));
		allNeighbourPoints.add(getNeighbourPoint(presentPoint, grid, Direction.WEST));
		allNeighbourPoints.add(getNeighbourPoint(presentPoint, grid, Direction.NORTH));
		allNeighbourPoints.add(getNeighbourPoint(presentPoint, grid, Direction.SOUTH));
		allNeighbourPoints.add(getNeighbourPoint(presentPoint, grid, Direction.NORTHEAST));
		allNeighbourPoints.add(getNeighbourPoint(presentPoint, grid, Direction.NORTHWEST));
		allNeighbourPoints.add(getNeighbourPoint(presentPoint, grid, Direction.SOUTHEAST));
		allNeighbourPoints.add(getNeighbourPoint(presentPoint, grid, Direction.SOUTHWEST));
		
		return allNeighbourPoints;
		
	}
	
	public static boolean isInsideBoundary(LatLng point, RectangleBoundary boundary){
		
		if((point.getLongitude() > boundary.p1.longitude && point.getLongitude() < boundary.p2.longitude) && 
				(point.getLatitude() > boundary.p1.lattitude && point.getLatitude() < boundary.p4.lattitude)){
			return true;
		}
		return false;
	}
	
	public static HemaData getNearestPointInDataSet(LatLng point, RectangleBoundary boundary){
		
		if(isInsideBoundary(point, boundary)){
			return null;
		}
		
		ArrayList<HemaData> possibleNearestPoints = getPossibleNearestPoints(point, boundary.grid);
		
		HemaData nearestPoint = null;
		Double tempDistance = null;
		
		for(HemaData hemaData : possibleNearestPoints){
			
			LatLng latLng = new LatLng(hemaData.lattitude, hemaData.longitude);
			double distance = LatLngTool.distance(point, latLng, LengthUnit.METER);
			
			if(tempDistance == null || distance < tempDistance){
				tempDistance = distance;
				nearestPoint = hemaData;
			}
			
		}
		
		return nearestPoint;
		
	}

	public static HemaData getNearestPointFromDB(LatLng point){
		
		/*if(isInsideBoundary(point, boundary)){
			return null;
		}*/
		
		ArrayList<HemaData> possibleNearestPoints;
		try {
			possibleNearestPoints = DatabaseUtil.getInstance().getNearestPointFromDB(point.getLatitude(), point.getLongitude());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		HemaData nearestPoint = null;
		Double tempDistance = null;
		
		for(HemaData hemaData : possibleNearestPoints){
			
			LatLng latLng = new LatLng(hemaData.lattitude, hemaData.longitude);
			double distance = LatLngTool.distance(point, latLng, LengthUnit.METER);
			
			if(tempDistance == null || distance < tempDistance){
				tempDistance = distance;
				nearestPoint = hemaData;
			}
			
		}
		
		return nearestPoint;
		
	}
	
	private static ArrayList<HemaData> getPossibleNearestPoints(LatLng point,
			HashMap<Integer, HemaData> grid) {
		// TODO Auto-generated method stub
		
		ArrayList<HemaData> nearestPossiblePoints = new ArrayList<HemaData>();
		
		double presentPointLattitude = round(point.getLatitude());
		double presentPointLongitude = round(point.getLongitude());
		
		Iterator<HemaData> it = grid.values().iterator();
		
		while(it.hasNext()){

			HemaData hemaData = it.next();
			
			if(round(hemaData.lattitude) == presentPointLattitude && round(hemaData.longitude) == presentPointLongitude) {
				nearestPossiblePoints.add(hemaData);
			}
			
		}
		
		return nearestPossiblePoints;
		
	}
	
	private static double round(double d){
		return (Math.round(d) * roundFactor)/roundFactor;
	}
	
}
