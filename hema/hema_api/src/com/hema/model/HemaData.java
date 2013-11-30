package com.hema.model;

public class HemaData {
	
	public Integer id;
	public Double lattitude;
	public Double longitude;
	public Integer east_point;
	public Integer west_point;
	public Integer north_point;
	public Integer south_point;
	public Double elevation;


	public HemaData(Integer id) {
		// TODO Auto-generated constructor stub
		
		this.id = id;
		
	}
	
	public HemaData(Integer id, Double lattitude, Double longitude,
			Integer east_point, Integer west_point, Integer north_point,
			Integer south_point) {
		this.id = id;
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.east_point = east_point;
		this.west_point = west_point;
		this.north_point = north_point;
		this.south_point = south_point;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		HemaData hemaData = (HemaData) obj;
		return hemaData.id.equals(this.id);
	}
	
	
}
