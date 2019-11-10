package com.example.gongjiao.pojo;

public class zd {
	
	
	String id,name,luxian,lat,lng;

	/**
	 * @return the lat
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}

	/**
	 * @return the lng
	 */
	public String getLng() {
		return lng;
	}

	/**
	 * @param lng the lng to set
	 */
	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public zd() {
		super();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLuxian() {
		return luxian;
	}

	public void setLuxian(String luxian) {
		this.luxian = luxian;
	}

}
