package com.mysportsshare.mysportscast.models;

public class Comments {
	private String mediaCommentId;
	private String mediaCommentUserId;
	private String mediaCommentFirstName;
	private String mediaCommentProfileImage;
	private String mediaCommentText;
	private String mediaCommentAddedText;
	private String mediaCommentCreated;
	private int pos; //rep position in the list

	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public String getMediaCommentCreated() {
		return mediaCommentCreated;
	}
	public void setMediaCommentCreated(String mediaCommentCreated) {
		this.mediaCommentCreated = mediaCommentCreated;
	}
	public String getMediaCommentId() {
		return mediaCommentId;
	}
	public void setMediaCommentId(String mediaCommentId) {
		this.mediaCommentId = mediaCommentId;
	}
	public String getMediaCommentUserId() {
		return mediaCommentUserId;
	}
	public void setMediaCommentUserId(String mediaCommentUserId) {
		this.mediaCommentUserId = mediaCommentUserId;
	}
	public String getMediaCommentFirstName() {
		return mediaCommentFirstName;
	}
	public void setMediaCommentFirstName(String mediaCommentFirstName) {
		this.mediaCommentFirstName = mediaCommentFirstName;
	}
	public String getMediaCommentProfileImage() {
		return mediaCommentProfileImage;
	}
	public void setMediaCommentProfileImage(String mediaCommentProfileImage) {
		this.mediaCommentProfileImage = mediaCommentProfileImage;
	}
	public String getMediaCommentText() {
		return mediaCommentText;
	}
	public void setMediaCommentText(String mediaCommentText) {
		this.mediaCommentText = mediaCommentText;
	}
	public String getMediaCommentAddedText() {
		return mediaCommentAddedText;
	}
	public void setMediaCommentAddedText(String mediaCommentAddedText) {
		this.mediaCommentAddedText = mediaCommentAddedText;
	}	
}
