package com.mysportsshare.mysportscast.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class NotificationInfo implements Serializable{

	private String notification_id; 
	private String sender_id; 
	private String sender_name;
	private String sender_image_url;
	private String notification_type;	
	private String data_id;
	private String text;
	private String data_url;
	private String event_id;
	private String event_name;
	private String is_viewed;
	private String created_date;
	public String getNotification_id() {
		return notification_id;
	}
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public void setNotification_id(String notification_id) {
		this.notification_id = notification_id;
	}
	public String getSender_id() {
		return sender_id;
	}
	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}
	public String getSender_name() {
		return sender_name;
	}
	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}
	public String getSender_image_url() {
		return sender_image_url;
	}
	public void setSender_image_url(String sender_image_url) {
		this.sender_image_url = sender_image_url;
	}
	public String getNotification_type() {
		return notification_type;
	}
	public void setNotification_type(String notification_type) {
		this.notification_type = notification_type;
	}
	public String getData_id() {
		return data_id;
	}
	public void setData_id(String data_id) {
		this.data_id = data_id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getData_url() {
		return data_url;
	}
	public void setData_url(String data_url) {
		this.data_url = data_url;
	}
	public String getIs_viewed() {
		return is_viewed;
	}
	public void setIs_viewed(String is_viewed) {
		this.is_viewed = is_viewed;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

}
