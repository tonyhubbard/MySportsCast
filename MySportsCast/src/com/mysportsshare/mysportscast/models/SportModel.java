package com.mysportsshare.mysportscast.models;

import java.io.Serializable;

public class SportModel implements Serializable{
	private String sportId;
	private String sportName;
	private String maxPlayersOnField;
	private String sportRounds;
	public String getSportId() {
		return sportId;
	}
	public void setSportId(String sportId) {
		this.sportId = sportId;
	}
	public String getSportName() {
		return sportName;
	}
	public void setSportName(String sportName) {
		this.sportName = sportName;
	}
	public String getMaxPlayersOnField() {
		return maxPlayersOnField;
	}
	public void setMaxPlayersOnField(String maxPlayersOnField) {
		this.maxPlayersOnField = maxPlayersOnField;
	}
	public String getSportRounds() {
		return sportRounds;
	}
	public void setSportRounds(String sportRounds) {
		this.sportRounds = sportRounds;
	}
	
}
