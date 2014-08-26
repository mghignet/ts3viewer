package com.mghignet.android.ts3viewer.domain;

import java.util.ArrayList;
import java.util.List;

public class Channel {

	private String id;
	private String name;
	private int treeLevel;
	private String statusImage;
	private String flagImage;
	private List<Client> clients;
	
	public Channel() {
		clients = new ArrayList<Client>();
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
	public int getTreeLevel() {
		return treeLevel;
	}
	public void setTreeLevel(int treeLevel) {
		this.treeLevel = treeLevel;
	}
	public String getStatusImage() {
		return statusImage;
	}
	public void setStatusImage(String statusImage) {
		this.statusImage = statusImage;
	}
	public String getFlagImage() {
		return flagImage;
	}
	public void setFlagImage(String flagImage) {
		this.flagImage = flagImage;
	}
	public List<Client> getClients() {
		return clients;
	}
	public void setClients(List<Client> clients) {
		this.clients = clients;
	}
	
	public String toString() {
		return this.name;
	}
	
		
}
