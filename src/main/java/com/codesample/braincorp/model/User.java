package com.codesample.braincorp.model;

public class User {
	
	private String name;
	private Integer uid;
	private Integer gid;
	private String comment;
	private String home;
	private String shell;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getShell() {
		return shell;
	}
	public void setShell(String shell) {
		this.shell = shell;
	}
	
	
}
