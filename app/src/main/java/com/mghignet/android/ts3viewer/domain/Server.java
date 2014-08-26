package com.mghignet.android.ts3viewer.domain;

import java.util.ArrayList;
import java.util.List;

public class Server {

	private String id;
	private String name;
	private String address;
	private String statusImage;
	private List<Channel> channels;
	
	public Server() {
		channels = new ArrayList<Channel>();
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
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStatusImage() {
		return statusImage;
	}
	public void setStatusImage(String statusImage) {
		this.statusImage = statusImage;
	}
	public List<Channel> getChannels() {
		return channels;
	}
	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
	
	public String toString() {
		return this.name;
	}
	
}
