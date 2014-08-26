package com.mghignet.android.ts3viewer.domain;

public class Client {

	private String id;
	private String name;
	private int treeLevel;
	private String statusImage;
	private String channelAdminImage;
	private String serverAdminImage;
	
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
	public int getTreeLevel() {
		return treeLevel;
	}
	public void setTreeLevel(int treeLevel) {
		this.treeLevel = treeLevel;
	}
	public String toString() {
		return this.name;
	}
	public String getStatusImage() {
		return statusImage;
	}
	public void setStatusImage(String statusImage) {
		this.statusImage = statusImage;
	}
	public String getChannelAdminImage() {
		return channelAdminImage;
	}
	public void setChannelAdminImage(String channelAdminImage) {
		this.channelAdminImage = channelAdminImage;
	}
	public String getServerAdminImage() {
		return serverAdminImage;
	}
	public void setServerAdminImage(String serverAdminImage) {
		this.serverAdminImage = serverAdminImage;
	}
	
}
