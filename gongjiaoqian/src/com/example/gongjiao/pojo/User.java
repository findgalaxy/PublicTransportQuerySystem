package com.example.gongjiao.pojo;



import java.io.Serializable;

public class User implements Serializable {
String id;
String code;
public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
String username;
String nickname;
String pass,lat,lng;
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
String type;
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getNickname() {
	return nickname;
}
public void setNickname(String nickname) {
	this.nickname = nickname;
}
public String getPass() {
	return pass;
}
public void setPass(String pass) {
	this.pass = pass;
}
public User() {
	super();
}


}
