package com.hema.downstream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.hema.api.DatabaseUtil;
import com.hema.api.HemaApi;
import com.hema.api.HemaRectangleGridUtil;
import com.hema.model.GenericBoundary;
import com.hema.model.HemaData;
import com.hema.model.RectangleBoundary;
import com.hema.model.path.DownStreamPath;
import com.hema.model.path.Path;
import com.javadocmd.simplelatlng.LatLng;

public class DownstreamAlgo {

	public static void solveDownstreamPath(GenericBoundary boundary, LatLng startPoint, LatLng endPoint){
		
		// Code to make a rectangular grid
		RectangleBoundary rectangleBoundary = HemaRectangleGridUtil.makeRectangurlarGrid(boundary);
		
		boolean areValidPoints = HemaApi.isInsideBoundary(startPoint, rectangleBoundary) && HemaApi.isInsideBoundary(endPoint, rectangleBoundary);
		
		if(!areValidPoints){
			return;
		}
		
		solveDownstreamPath(rectangleBoundary);
		
	}

	public static void solveDownstreamPath(RectangleBoundary rectangleBoundary) {
		// TODO Auto-generated method stub
		
		Queue<HemaData> queue = new LinkedList<HemaData>();
		HashMap<Integer, DownStreamPath> downStreamPathPerPoint = new HashMap<Integer, DownStreamPath>();
		ArrayList<DownStreamPath> finalDownStreamPaths = new ArrayList<DownStreamPath>();
		
		queue.add(rectangleBoundary.startPoint);
		
		while(!queue.isEmpty()){
			
			HemaData present = queue.peek();
			
			ArrayList<HemaData> allNeighbourPoints = HemaApi.getAllNeighbourPoints(present, rectangleBoundary.grid);
			
			for(HemaData hemaData : allNeighbourPoints){
				
				//Ignore if it is a starting point
				if(hemaData.equals(rectangleBoundary.startPoint)){
					continue;
				}
				
				//Computing the elevation difference
				double thisDiff = present.elevation - hemaData.elevation;
				
				if(thisDiff > 0){
					
					Path pathp1 = new Path(present, hemaData);
					DownStreamPath downStreamPath = downStreamPathPerPoint.get(present.id);
					
					//Final Point
					if(hemaData.equals(rectangleBoundary.endPoint)){
						
						ArrayList<Path> currentPath = new ArrayList<Path>();
						currentPath.addAll(downStreamPath.path);
						currentPath.add(pathp1);
						
						DownStreamPath newPath = new DownStreamPath(currentPath, downStreamPath.result + thisDiff);
						finalDownStreamPaths.add(newPath);
						
					}else{

						if(downStreamPath == null){
							
							ArrayList<Path> pathList = new ArrayList<Path>();
							pathList.add(pathp1);
							downStreamPath = new DownStreamPath(pathList, thisDiff);
							downStreamPathPerPoint.put(hemaData.id, downStreamPath);
							
						}else{
							ArrayList<Path> currentPath = new ArrayList<Path>();
							currentPath.addAll(downStreamPath.path);
							currentPath.add(pathp1);
							
							DownStreamPath newPath = new DownStreamPath(currentPath, downStreamPath.result + thisDiff);
							downStreamPathPerPoint.put(hemaData.id, newPath);
						}
						
						//Add the present point to compute in the interation
						queue.add(hemaData);
					}
					
					
				}
				
			}
			
			//Remove the point after computation
			downStreamPathPerPoint.remove(present.id);
			queue.poll();
			
			//Persist Queue and data for each transaction
			DatabaseUtil.getInstance().persistQueueData(queue, downStreamPathPerPoint, finalDownStreamPaths);
			
		}
		
	}
	
	
}
