package com.yuanteoh.ics440pa2;

// consist of all information from the station and weather data
public class Data implements Cloneable {
	private String id; 
	private float latitude; 
	private float longitude; 
	private float elevation;
	private String state; 
	private String name;
	private int days;
	
	private int year;
	private int month;
	private int day;
	private String element;
	private int value;
	private String qflag;
	
	// getter and setter for data instance variable
	public String getId() {
		return id;
	}
	
	public float getLatitude() {
		return latitude;
	}
	
	public float getLongitude() {
		return longitude;
	}
	
	public float getElevation() {
		return elevation;
	}
	
	public String getState() {
		return state;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDays() {
		return days;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	public void setElevation(float elevation) {
		this.elevation = elevation;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDays(int days) {
		this.days = days;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}
	
	public String getElement() {
		return element;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getQflag() {
		return qflag;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public void setElement(String element) {
		this.element = element;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void setQflag(String qflag) {
		this.qflag = qflag;
	}
	
	// copy a new data object
	public Data clone() {
		try {
			return (Data)super.clone();
		} catch (Exception e) {
			return null;
		}
	}
}
