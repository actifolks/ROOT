package com.hema.model;

import java.util.ArrayList;

import com.javadocmd.simplelatlng.LatLng;

public class GenericBoundary {

	public ArrayList<LatLng> boundaryPoints;
	public LatLng startPoint;
	public LatLng endPoint;
	
	public ArrayList<LatLng> getBoundaryPoints() {
		
		if(boundaryPoints == null){
			boundaryPoints = new ArrayList<LatLng>();
		}
		
		return boundaryPoints;
	}
	
}
