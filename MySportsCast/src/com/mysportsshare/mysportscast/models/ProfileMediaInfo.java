package com.mysportsshare.mysportscast.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileMediaInfo implements Serializable{

	private String mediaCategory; //media is cast/photo/video
	private String mediaType; //media added to event or profile
	private String mediaEventId;
	private String mediaIsStubhubEvent;
	private String mediaEventTitle;	
	private String mediaEventType;
	private String mediaSportName;
	private String mediaLocation;
	private String mediaId;
	private String mediaCaption;
	private String mediaUrl;
	private String isUserLiked; //0=>not liked; 1=>liked
	private String mediaLikeCount;
	private String mediaCommentCount;
	private String latestComment;
	private String latestCommentBy;
	private String mediaThumbUrl;

	public String getMediaThumbUrl() {
		return mediaThumbUrl;
	}

	public void setMediaThumbUrl(String mediaThumbUrl) {
		this.mediaThumbUrl = mediaThumbUrl;
	}

	public String getLatestComment() {
		return latestComment;
	}

	public void setLatestComment(String latestComment) {
		this.latestComment = latestComment;
	}

	public String getLatestCommentBy() {
		return latestCommentBy;
	}

	public void setLatestCommentBy(String latestCommentBy) {
		this.latestCommentBy = latestCommentBy;
	}

	public String getMediaCategory() {
		return mediaCategory;
	}

	public void setMediaCategory(String mediaCategory) {
		this.mediaCategory = mediaCategory;
	}
	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaEventId() {
		return mediaEventId;
	}

	public void setMediaEventId(String mediaEventId) {
		this.mediaEventId = mediaEventId;
	}

	public String getMediaIsStubhubEvent() {
		return mediaIsStubhubEvent;
	}

	public void setMediaIsStubhubEvent(String mediaIsStubhubEvent) {
		this.mediaIsStubhubEvent = mediaIsStubhubEvent;
	}

	public String getMediaEventTitle() {
		return mediaEventTitle;
	}

	public void setMediaEventTitle(String mediaEventTitle) {
		this.mediaEventTitle = mediaEventTitle;
	}

	public String getMediaEventType() {
		return mediaEventType;
	}

	public void setMediaEventType(String mediaEventType) {
		this.mediaEventType = mediaEventType;
	}

	public String getMediaSportName() {
		return mediaSportName;
	}

	public void setMediaSportName(String mediaSportName) {
		this.mediaSportName = mediaSportName;
	}

	public String getMediaLocation() {
		return mediaLocation;
	}

	public void setMediaLocation(String mediaLocation) {
		this.mediaLocation = mediaLocation;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getMediaCaption() {
		return mediaCaption;
	}

	public void setMediaCaption(String mediaCaption) {
		this.mediaCaption = mediaCaption;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public String getMediaLikeCount() {
		return mediaLikeCount;
	}

	public void setMediaLikeCount(String mediaLikeCount) {
		this.mediaLikeCount = mediaLikeCount;
	}
	public void incMediaLikes(){
		this.mediaLikeCount = String.valueOf(Integer.parseInt(mediaLikeCount)+1);			
	}
	public void decMediaLikes(){
		this.mediaLikeCount = String.valueOf(Integer.parseInt(mediaLikeCount)-1);			
	}
	public String getIsUserLiked() {
		return isUserLiked;
	}

	public void setIsUserLiked(String isUserLiked) {
		this.isUserLiked = isUserLiked;
	}

	public String getMediaCommentCount() {
		return mediaCommentCount;
	}

	public void setMediaCommentCount(String mediaCommentCount) {
		this.mediaCommentCount = mediaCommentCount;
	}
	public void incCommentsCnt(){
		this.mediaCommentCount = String.valueOf(Integer.parseInt(mediaCommentCount)+1);			
	}
	public void decCommentsCnt(){
		this.mediaCommentCount = String.valueOf(Integer.parseInt(mediaCommentCount)-1);			
	}
	/**
	 * This inner class is used for media comments.
	 * @author sharma
	 *
	 *//*
	public class EventMediaComments {
		private String mediaCommentId;
		private String mediaCommentUserId;
		private String mediaCommentFirstName;
		private String mediaCommentProfileImage;
		private String mediaCommentText;
		private String mediaCommentAddedText;
		private String mediaCommentCreated;

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
	}*/
}
