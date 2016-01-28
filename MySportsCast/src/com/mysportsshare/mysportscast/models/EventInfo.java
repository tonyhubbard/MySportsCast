package com.mysportsshare.mysportscast.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * This POJO class is used while working with getEventInfo Webservice.
 * @author sharma
 *
 */
public class EventInfo implements Serializable{

	/**
	 * Default ID, you can change if you want.
	 */
	private static final long serialVersionUID = 1L;
	private String eventId;
	private String creatorId;
	private String sportName;
	private String eventTitle;
	private String eventDescription;
	private String team1Id;
	private String team1Type;
	private String team2Id;
	private String team2Type;
	private String eventStartDate;
	private String eventStartTime;
	private String eventStartDateTime;
	private String eventEndTime;
	private String eventSubLocation;
	private String eventAddress;
	private String eventDistanceFromUserLoc;
	private String eventLikes;
	private String isAttending;
	private String isUserLikedEvent;
	private String eventImageUrl; //holds url of the event image.
	private String latestCast;
	private String eventCity;
	private String eventType;
	private String attendingCount;
	private boolean isStubhubEvent;
	private String lat,lng; //Holds current event destination latitude & longitude
	private ArrayList<UserProfileInfo>likeUsers; //Event like users
	private ArrayList<UserProfileInfo>attendingUsers; //Event attending users
	private HashMap<String, ArrayList<EventInfo.EventMedia>> eventMediaList;
	//for latest cast 

	private String userId;	
	private String userName;
	private String profilePicPath;
	private String castText;

	private String eventShareURL; //holds url of the current event for share via social media

	public boolean isStubhubEvent() {
		return isStubhubEvent;
	}
	public void setStubhubEvent(boolean isStubhubEvent) {
		this.isStubhubEvent = isStubhubEvent;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getIsUserLikedEvent() {
		return isUserLikedEvent;
	}
	public void setIsUserLikedEvent(String isUserLikedEvent) {
		this.isUserLikedEvent = isUserLikedEvent;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getProfilePicPath() {
		return profilePicPath;
	}
	public void setProfilePicPath(String profilePicPath) {
		this.profilePicPath = profilePicPath;
	}
	public String getCastText() {
		return castText;
	}
	public void setCastText(String castText) {
		this.castText = castText;
	}
	public String getEventCity() {
		return eventCity;
	}
	public void setEventCity(String eventCity) {
		this.eventCity = eventCity;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		if(TextUtils.isEmpty(eventType) 
				|| eventType.equalsIgnoreCase("null")){
			this.eventType = "-";	
		}else{
			this.eventType = eventType;	
		}

	}
	public String getAttendingCount() {
		return attendingCount;
	}
	public void setAttendingCount(String attendingCount) {
		this.attendingCount = attendingCount;
	}	
	public void incAttendingCount(){
		this.attendingCount = String.valueOf(Integer.parseInt(attendingCount)+1);
	}
	public void decAttendingCount(){
		this.attendingCount = String.valueOf(Integer.parseInt(attendingCount)-1);
	}
	public ArrayList<UserProfileInfo> getAttendingUsers() {
		return attendingUsers;
	}
	public void setAttendingUsers(ArrayList<UserProfileInfo> attendingUsers) {
		this.attendingUsers = attendingUsers;
	}


	public String getLatestCast() {
		return latestCast;
	}
	public void setLatestCast(String latestCast) {
		this.latestCast = latestCast;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getSportName() {
		return sportName;
	}
	public void setSportName(String sportName) {
		if(TextUtils.isEmpty(sportName) 
				|| sportName.equalsIgnoreCase("null")){
			this.sportName = "-";	
		}else{
			this.sportName = sportName;	
		}
	}
	public String getEventTitle() {
		return eventTitle;
	}
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;		
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	public String getTeam1Id() {
		return team1Id;
	}
	public void setTeam1Id(String team1Id) {
		this.team1Id = team1Id;
	}
	public String getTeam1Type() {
		return team1Type;
	}
	public void setTeam1Type(String team1Type) {
		this.team1Type = team1Type;
	}
	public String getTeam2Id() {
		return team2Id;
	}
	public void setTeam2Id(String team2Id) {
		this.team2Id = team2Id;
	}
	public String getTeam2Type() {
		return team2Type;
	}
	public void setTeam2Type(String team2Type) {
		this.team2Type = team2Type;
	}
	public String getEventStartDate() {
		return eventStartDate;
	}
	public void setEventStartDate(String eventStartDate) {
		try {
			/*
			 *Old format of parsing date & time  
			String oldFormat = "EEE, MMMM dd yyyy";
			SimpleDateFormat oldDtTmeFormat = new SimpleDateFormat(oldFormat);
			Date oldDateFormat = oldDtTmeFormat.parse(eventStartDate);
			String newFormat = "EEE, MMM dd yy";
			SimpleDateFormat newDtTmeFormat = new SimpleDateFormat(newFormat);
			String newDateFormat = newDtTmeFormat.format(oldDateFormat);

			eventStartDate = newDateFormat;*/
			TimeZone UTC_TZ = TimeZone.getTimeZone("UTC");
			SimpleDateFormat srcFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			srcFormat.setTimeZone(UTC_TZ);
			Date utcStartDate = srcFormat.parse(eventStartDate);

			TimeZone defaultTZ = TimeZone.getDefault();
			//by koti
//			SimpleDateFormat destFormat = new SimpleDateFormat("EEE,MMM dd yy");
			SimpleDateFormat destFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			destFormat.setTimeZone(defaultTZ);
			eventStartDate = destFormat.format(utcStartDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.eventStartDate = eventStartDate;
	}
	public String getEventStartTime() {
		return eventStartTime;
	}

	public String getEventStartDateAndTime(){
		return eventStartDateTime;
	}

	public boolean isUpcommingEvent(){
		SimpleDateFormat destFormatForDtTme = new SimpleDateFormat("MM/dd/yyyy hh:mma");
		try {
			Date eventDatTime = destFormatForDtTme.parse(eventStartDateTime);
			Calendar currentDateTime = Calendar.getInstance();
			if(eventDatTime.after(currentDateTime.getTime())){
				return true;
			}else{
				return false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;		
	}
	
	public void setEventStartTime(String eventStartTime) {
		try {			
			TimeZone UTC_TZ = TimeZone.getTimeZone("UTC");
			SimpleDateFormat srcFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			srcFormat.setTimeZone(UTC_TZ);
			Date utcStartDate = srcFormat.parse(eventStartTime);

			TimeZone defaultTZ = TimeZone.getDefault();
			SimpleDateFormat destFormat = new SimpleDateFormat("hh:mma");
			destFormat.setTimeZone(defaultTZ);
			eventStartTime = destFormat.format(utcStartDate);

			SimpleDateFormat destFormatForDtTme = new SimpleDateFormat("MM/dd/yyyy hh:mma");
			destFormatForDtTme.setTimeZone(defaultTZ);
			eventStartDateTime = destFormatForDtTme.format(utcStartDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.eventStartTime = eventStartTime;
	}
	public String getEventEndTime() {
		return eventEndTime;
	}
	public void setEventEndTime(String eventEndTime) {
		try {			
			TimeZone UTC_TZ = TimeZone.getTimeZone("UTC");
			SimpleDateFormat srcFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			srcFormat.setTimeZone(UTC_TZ);
			Date utcEndDate = srcFormat.parse(eventEndTime);

			TimeZone defaultTZ = TimeZone.getDefault();
			SimpleDateFormat destFormat = new SimpleDateFormat("MM/dd/yyyy hh:mma");
			destFormat.setTimeZone(defaultTZ);
			eventEndTime = destFormat.format(utcEndDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.eventEndTime = eventEndTime;
	}
	public String getEventSubLocation() {
		return eventSubLocation;
	}
	public void setEventSubLocation(String eventSubLocation) {
		this.eventSubLocation = eventSubLocation;
	}
	public String getEventAddress() {
		return eventAddress;
	}
	public void setEventAddress(String eventAddress) {
		this.eventAddress = eventAddress;
	}
	public String getEventDistanceFromUserLoc() {
		return eventDistanceFromUserLoc;
	}
	public void setEventDistanceFromUserLoc(String eventDistanceFromUserLoc) {
		this.eventDistanceFromUserLoc = eventDistanceFromUserLoc;
	}	
	public ArrayList<UserProfileInfo> getLikeUsers() {
		return likeUsers;
	}
	public void setLikeUsers(ArrayList<UserProfileInfo> likeUsers) {
		this.likeUsers = likeUsers;
	}
	public String getEventLikes() {
		return eventLikes;
	}
	public void incEventLikes(){
		eventLikes = String.valueOf(Integer.parseInt(eventLikes) + 1);
	}
	public void decEventLikes(){
		eventLikes = String.valueOf(Integer.parseInt(eventLikes) - 1);
	}
	public void setEventLikes(String eventLikes) {
		this.eventLikes = eventLikes;
	}
	public String getIsAttending() {
		return isAttending;
	}
	public void setIsAttending(String isAttending) {
		this.isAttending = isAttending;
	}
	public String getEventImageUrl() {
		return eventImageUrl;
	}
	public void setEventImageUrl(String eventImageUrl) {
		this.eventImageUrl = eventImageUrl;
	}		
	public boolean hasEventMedia(){
		if(eventMediaList!=null){
			if(eventMediaList.isEmpty()){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
	public HashMap<String, ArrayList<EventInfo.EventMedia>> getEventMediaList() {
		return eventMediaList;
	}
	public void setEventMediaList(HashMap<String, ArrayList<EventInfo.EventMedia>> eventMediaList) {
		this.eventMediaList = eventMediaList;
	}
	public String getEventShareURL() {
		return eventShareURL;
	}
	public void setEventShareURL(String eventShareURL) {
		this.eventShareURL = eventShareURL;
	}

	//This class is for event media types
	public class EventMedia {
		private String mediaType;
		private String mediaId;
		private String mediaUserId;
		private String mediaFirstName;
		private String mediaProfileImage;
		private String mediaData;
		private String mediaDateCreated;
		private String mediaPhotoUrl;
		private String mediaThumbNail;
		
		
		private String isUserLiked;		

		private String mediaLikes; //Media likes count
		private ArrayList<UserProfileInfo> medialikeUsers; //Media like users

		private String commentsCnt; //Media comments count
		//		private  ArrayList<EventMediaComments> mediaCommentsMap;//Commented users with commented text
		private String latestComment;
		
		public String getMediaThumbNail() {
			return mediaThumbNail;
		}

		public void setMediaThumbNail(String mediaThumbNail) {
			this.mediaThumbNail = mediaThumbNail;
		}
		
		public String getLatestComment() {
			return latestComment;
		}

		public void setLatestComment(String latestComment) {
			this.latestComment = latestComment;
		}

		public String getLatestCommentedBy() {
			return latestCommentedBy;
		}

		public void setLatestCommentedBy(String latestCommentedBy) {
			this.latestCommentedBy = latestCommentedBy;
		}
		private String latestCommentedBy;
		public  EventMedia(){		
		}

		public String getIsUserLiked() {
			return isUserLiked;
		}
		public void setIsUserLiked(String isUserLiked) {
			this.isUserLiked = isUserLiked;
		}
		public String getCommentsCnt() {
			return commentsCnt;
		}
		public void setCommentsCnt(String commentsCnt) {
			this.commentsCnt = commentsCnt;
		}
		public void incCommentsCnt(){
			this.commentsCnt = String.valueOf(Integer.parseInt(commentsCnt)+1);			
		}
		public void decCommentsCnt(){
			this.commentsCnt = String.valueOf(Integer.parseInt(commentsCnt)-1);			
		}
		public void incMediaLikes(){
			this.mediaLikes = String.valueOf(Integer.parseInt(mediaLikes)+1);			
		}
		public void decMediaLikes(){
			this.mediaLikes = String.valueOf(Integer.parseInt(mediaLikes)-1);			
		}
		public String getMediaLikes() {
			return mediaLikes;
		}
		public void setMediaLikes(String mediaLikes) {
			this.mediaLikes = mediaLikes;
		}
		public String getMediaType() {
			return mediaType;
		}
		public void setMediaType(String mediaType) {
			this.mediaType = mediaType;
		}
		public String getMediaId() {
			return mediaId;
		}
		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
		}
		public String getMediaUserId() {
			return mediaUserId;
		}
		public void setMediaUserId(String mediaUserId) {
			this.mediaUserId = mediaUserId;
		}
		public String getMediaFirstName() {
			return mediaFirstName;
		}
		public void setMediaFirstName(String mediaFirstName) {
			this.mediaFirstName = mediaFirstName;
		}
		public String getMediaProfileImage() {
			return mediaProfileImage;
		}
		public void setMediaProfileImage(String mediaProfileImage) {
			this.mediaProfileImage = mediaProfileImage;
		}
		public String getMediaData() {
			return mediaData;
		}
		public void setMediaData(String mediaData) {
			this.mediaData = mediaData;
		}
		public String getMediaDateCreated() {
			return mediaDateCreated;
		}
		public void setMediaDateCreated(String mediaDateCreated) {
			this.mediaDateCreated = mediaDateCreated;
		}
		public String getMediaPhotoUrl() {
			return mediaPhotoUrl;
		}
		public void setMediaPhotoUrl(String mediaPhotoUrl) {
			this.mediaPhotoUrl = mediaPhotoUrl;
		}

		/*
		public ArrayList<EventMediaComments> getMediaCommentsMap() {
			return mediaCommentsMap;
		}
		public void setMediaCommentsMap(
				ArrayList<EventMediaComments> mediaCommentsMap) {
			this.mediaCommentsMap = mediaCommentsMap;
		}*/


		/**
		 * This inner class is used for media comments.
		 * @author sharma
		 *
		 */
		/*public class EventMediaComments {
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

	//Parses input events list & generate EventInfo objects
	//Parses only received events info are local time stamp
	public static void parseData(JSONArray upcomingEventsArr,List<EventInfo> eventsList){

		try {
			if (upcomingEventsArr != null) {
				for (int i = 0; i < upcomingEventsArr
						.length(); i++) {
					EventInfo eventinfoObj = new EventInfo();
					eventinfoObj
					.setEventId(upcomingEventsArr.getJSONObject(i).getString("event_id"));

					if (!upcomingEventsArr
							.getJSONObject(i).getString("sport_name").equalsIgnoreCase("null")) {
						eventinfoObj.setSportName(upcomingEventsArr.getJSONObject(i).getString("sport_name"));
					} else {
						eventinfoObj.setSportName("-");
					}

					if (!upcomingEventsArr
							.getJSONObject(i)
							.getString("event_title")
							.equalsIgnoreCase("null")) 
					{
						eventinfoObj.setEventTitle(upcomingEventsArr.getJSONObject(i)
								.getString("event_title"));
					} else {
						eventinfoObj.setEventTitle("-");
					}
					if (!upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"event_description")
									.equalsIgnoreCase("null")) {
						eventinfoObj.setEventDescription(upcomingEventsArr
								.getJSONObject(i)
								.getString("event_description"));
					} else {
						eventinfoObj.setEventDescription("-");
					}

					if (!upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"event_start_date")
									.equalsIgnoreCase("null")) {
						eventinfoObj.eventStartDate = upcomingEventsArr.getJSONObject(i).getString("event_start_date");
						//						.setEventStartDate(upcomingEventsArr
						//								.getJSONObject(i)
						//								.getString("event_start_date"));
					} else {
						eventinfoObj
						.setEventStartDate("-");
					}

					if (!upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"event_start_time")
									.equalsIgnoreCase("null")) {
						try{
							eventinfoObj.eventStartTime = upcomingEventsArr.getJSONObject(i).getString("event_start_time");
//							TimeZone UTC_TZ = TimeZone.getTimeZone("UTC");
							TimeZone UTC_TZ = TimeZone.getDefault();
							SimpleDateFormat srcFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
							srcFormat.setTimeZone(UTC_TZ);
							Date utcStartDate = srcFormat.parse(eventinfoObj.eventStartTime);
							SimpleDateFormat dstFormat = new SimpleDateFormat("h:mm a");
							eventinfoObj.eventStartTime = dstFormat.format(utcStartDate);
						} catch (ParseException e) {
							e.printStackTrace();
							eventinfoObj.eventStartTime = "-";
						}
						//						.setEventStartTime(upcomingEventsArr
						//								.getJSONObject(i)
						//										.getString("event_start_time"));
					} else {
						eventinfoObj
						.setEventStartTime("-");
					}

					if (!upcomingEventsArr
							.getJSONObject(i)
							.getString("event_sub_location")
							.equalsIgnoreCase("null")) {
						eventinfoObj
						.setEventSubLocation(upcomingEventsArr
								.getJSONObject(
										i)
										.getString(
												"event_sub_location"));
					} else {
						eventinfoObj
						.setEventSubLocation("-");
					}

					if (!upcomingEventsArr
							.getJSONObject(i)
							.getString("city")
							.equalsIgnoreCase("null")
							&& upcomingEventsArr
							.getJSONObject(i)
							.getString("city")
							.length() > 0) {
						eventinfoObj
						.setEventCity(upcomingEventsArr
								.getJSONObject(
										i)
										.getString(
												"city"));
					} else {
						eventinfoObj.setEventCity("-");
					}

					if (!upcomingEventsArr
							.getJSONObject(i)
							.getString("event_image")
							.equalsIgnoreCase("null")) {
						eventinfoObj
						.setEventImageUrl(upcomingEventsArr
								.getJSONObject(
										i)
										.getString(
												"event_image"));
					} else {

					}
					eventinfoObj
					.setTeam1Id(upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"team1_id"));
					eventinfoObj
					.setTeam1Type(upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"team1_type"));
					eventinfoObj
					.setTeam2Id(upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"team2_id"));
					eventinfoObj
					.setTeam2Type(upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"team2_type"));

					try{
						eventinfoObj.eventEndTime = upcomingEventsArr.getJSONObject(i).getString("event_end_time");
						TimeZone UTC_TZ = TimeZone.getTimeZone("UTC");
						SimpleDateFormat srcFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
						srcFormat.setTimeZone(UTC_TZ);
						Date utcStartDate = srcFormat.parse(eventinfoObj.eventStartTime);
						SimpleDateFormat dstFormat = new SimpleDateFormat("h:mm a");
						eventinfoObj.eventEndTime = dstFormat.format(utcStartDate);
					} catch (ParseException e) {
						e.printStackTrace();
						eventinfoObj.eventEndTime = "-";
					}					
					//					.setEventEndTime(upcomingEventsArr
					//							.getJSONObject(i)
					//							.getString(
					//									"event_end_time"));
					eventinfoObj
					.setEventAddress(upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"formatted_address"));
					eventinfoObj
					.setEventDistanceFromUserLoc(upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"distance_from_user_location"));
					eventinfoObj
					.setEventLikes(upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"event_likes"));
					eventinfoObj
					.setIsAttending(upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"is_attending"));
					eventinfoObj
					.setEventType(upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"event_type"));
					eventinfoObj
					.setAttendingCount(upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"attending_count"));

					if (upcomingEventsArr
							.getJSONObject(i)
							.getString("latest_cast") != null
							&& upcomingEventsArr
							.getJSONObject(i)
							.getString(
									"latest_cast")
									.equalsIgnoreCase(
											"no cast")) {
						eventinfoObj
						.setLatestCast(upcomingEventsArr
								.getJSONObject(
										i)
										.getString(
												"latest_cast"));
					} else {
						JSONObject castObj = upcomingEventsArr
								.getJSONObject(i)
								.getJSONObject(
										"latest_cast");
						eventinfoObj.setUserId(castObj
								.getString("user_id"));
						eventinfoObj.setUserName(castObj
								.getString("name"));
						eventinfoObj
						.setProfilePicPath(castObj
								.getString("profile_image"));
						eventinfoObj.setLatestCast(castObj
								.getString("cast_text"));
					}

					eventsList.add(eventinfoObj);
				}			
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
