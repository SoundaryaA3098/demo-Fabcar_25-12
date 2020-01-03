package org.example.demoFabcar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DTOData {
	@JsonProperty("orgName")
	private String orgName;
	
	@JsonProperty("admin")
	private String  admin;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("chaincodeName")
	private String  chaincodeName;
//	
//	@JsonProperty("channelName")
//	private String channelName;
	
	@JsonProperty("chaincodeMtd")
	private String  chaincodeMtd;
	
	@JsonProperty("args")
	private String  args[];
	
	

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	
	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChaincodeName() {
		return chaincodeName;
	}

	public void setChaincodeName(String chaincodeName) {
		this.chaincodeName = chaincodeName;
	}

//	public String getChannelName() {
//		return channelName;
//	}
//
//	public void setChannelName(String channelName) {
//		this.channelName = channelName;
//	}


	public String getChaincodeMtd() {
		return chaincodeMtd;
	}

	public void setChaincodeMtd(String chaincodeMtd) {
		this.chaincodeMtd = chaincodeMtd;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}
	
}
