package com.hema.model.path;

import java.util.ArrayList;

public class DownStreamPath {

	public ArrayList<Path> path;
	public double result;
	
	public DownStreamPath(ArrayList<Path> path, double result) {
		// TODO Auto-generated constructor stub
		this.path = path;
		this.result = result;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("(Result : " + this.result + ")");
		
		for(Path p : path){
			sb.append(" (" + p.p1.id + "-->" + p.p2.id + ")" + " | ");
		}
		
		return sb.toString();
	}
	
}
