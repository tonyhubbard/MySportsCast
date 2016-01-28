package com.mysportsshare.mysportscast.utils;

import android.content.Context;

/**
 * All Constants are located in this class.
 * 
 * @author Koti
 * 
 */
public class Constants {

	// public static final String common_url =
	// "http://182.75.34.62/MySportsShare/web_services/";
//	public static final String common_url = "http://www.mysportscast.mobi/web_services/";
	public static final String common_url = "http://54.175.207.88/web_services/";
	// Tags for maintaining fragments in each tab
	public static final String TAB_EVENT = "event_tab";
	public static final String TAB_SEARCH = "search_tab";
	public static final String TAB_MEDIA = "media_tab";
	public static final String TAB_NOTIFICATION = "notification_tab";
	public static final String TAB_USERS = "users_tab";
	public static final String TAB_LINKED_ACCS = "linked_acc_tab";

	// AdMob credentials
	public static final String AdUnitID_Banner = "ca-app-pub-1589399713821641/6728748219";
	public static final String AdUnitName_Banner = "bannerinnerscreen";
	public static final String AdUnitID_Interstitial = "ca-app-pub-1589399713821641/5252015016";
	public static final String AdUnitName_Interstitial = "interfeedsscreen";
	/**
	 * Common tags
	 */
	public static final String TAG_RESPONSE = "Response";
	public static final String TAG_RESPONSE_INFO = "ResponseInfo";
	public static final String TAG_SUCCESS = "SUCCESS";
	public static final String TAG_FAILURE = "FAILURE";

	/*
	 * Setting menu constants
	 */
	public static final String TAG_MENU_PROFILE_SETTING = "Profile settings";
	public static final String TAG_MENU_CHANGE_PASSWORD = "Change password";
	public static final String TAG_MENU_LOG_OUT = "Log out";
	public static final String TAG_MENU_LINKED_ACCOUNT = "Linked Accounts";

	/**
	 * Used in shared preferences
	 */
	public static final String PREFERENCES = "sportsshare_preferences";

	/**
	 * used in profile pic path
	 */
	public static final String STORAGE_PATH = "/mysportscast/media";
	public static final String PROFILEIMAGEPATH = "/mysportscast/profile_images";
	public static final String IMAGEPATH = "SportsCastImages";
	public static final String VIDEOPATH = "SportsCastVideos";

	/**
	 * These values are updating in LocationService service.
	 */
	public static String longi = "0";
	public static String lati = "0";
	public static String locationName = "My location";

	/**
	 * list of months
	 */
	public static String[] listMonths = { "Jan", "Feb", "Mar", "Apr", "May",
			"June", "July", "Aug", "Sep", "Oct", "Nov", "Dec" };

	// constants used for capturing image from camera & gallery
	/**
	 * used while caturing images
	 */
	public static final int REQUEST_IMAGE_CAPTURE = 100;
	public static final int REQUEST_IMAGE_GALLERY = 200;
	public static final int REQUEST_IMAGE_CROP = 300;
	public static final int REQUEST_VIDEO_CAPTURE = 400;
	public static final int REQUEST_VIDEO_GALLERY = 500;
	public static final int REQUEST_VIDEO_CROP = 600;

	public enum imgFilterList {
		NORMAL, GREY, SNOW, GAMMA, COLOR, CONTRAST, SHINY, SHARP, SMOOTH, GAUSSIAN, EMBOSS, ENGRAVE, TINT, FLEA, BLACK, INKWELL, KELLY, PEARL, AMBER, MAYFAIR, ALICE, ROWAN, DAISY
	}; // ,"TINT","WATER",CREMA,

	public static final int desiredImageWidth = 300, desiredImageHeight = 300;

	public static Context mContext;

	public static String logUrl = "com.sparsh.mysportscast";

	/*-----------------Social media - Key values-------------*/
	// Twitter keys
	public static final String CONSUMER_KEY = "ZYj95YLog8RkDGTkmEVvnCRVM";
	public static final String CONSUMER_SECRET = "AI7z8yRmTuUIyUTOgeC4pLVQ45LNMxngvpMkzVC0KXEipTgIfm";
	public static final String CALLBACK_URL = "https://dev.twitter.com/";

	// For passing information between fragments
	public final static String dataReceive = "data_receive";
	public final static String dataForAddEvent = "data_for_addevent";
	public final static String data_cal_allEvents = "AllEventDates";
	public final static String data_cal_userPostEvents = "UserPostedEventDates";
	public final static String data_cal_userCheckedEvents = "UserCheckedEventDates";
	public final static String data_cal_mnth = "mnth";
	public final static String data_cal_year = "yr";
	public final static String data_cal_day = "day";
	public static String TAG_PAGE_ID = "page_id";
	public final static String dataReceiveUserName = "data_receive_user_name";
	public final static String searchData = "searchData";
	public final static String ToFragment_userProfile = "UserProfileFragment";
	public final static String ToFragment_followingsData = "FollowingUsersList";
	public final static String ToFragment_followersData = "FollowersUsersList";
	public final static String ToFragment_checkinEventsData = "CheckinEventsList";
	public final static String ToFragment_editProfile = "EditProfile";
	public final static String ToFragment_myEvents = "MyEventsList";
	public final static String ToFragment_search = "SearchPage";

	// Constants for sport type
	public static final String SPORT_BASKETBALL = "basketball";
	public static final String SPORT_BASEBALL = "baseball";
	public static final String SPORT_FOOTBALL = "football";
	public static final String SPORT_SOCCER = "soccer";
	public static final String SPORT_SOFTBALL = "softball";

	/*-------------------------Constants defined for web server communication-------------------*/
	// Web server Response result
	public static String webserver_response = "Response";
	public static String webserver_response_success = "SUCCESS";
	public static String webserver_response_fail = "FAILURE";
	public static String webserver_responseInfo = "ResponseInfo";

	public static String TAG_USER_ID = "user_id";

	// Costants for change password
	public static String TAG_CHANGE_OLD_PWD = "old_pass";
	public static String TAG_CHANGE_NEW_PWD = "new_pass";

	// Constants for user profile
	public static String userProf_Title = " PROFILE";
	public static String userProf_Cast = " CAST";
	public static String userProf_Photos = " PHOTOS";
	public static String userProf_videos = " VIDEOS";
	public static String userProf_tags = " TAGS";
	public static String userProf_posts = " POSTS";
	public static String userProf_Notificaton = "NOTIFICATIONS";
	public static String userProf_InfoObj = "ProfileInfo";
	public static String userProf_userID = "user_id";
	public static String userProf_logged_userID = "login_user_id";
	public static String userProf_userName = "user_name";
	public static String userProf_videoPath = "user_video_path";
	public static String userProf_caption = "user_media_caption";
	public static String userProf_firstName = "first_name";
	public static String userProf_userProfPic = "user_profile_pic"; // in
																	// result:
																	// http://mss.local.uploads.s3.amazonaws.com/uploads/250X250/0
	public static String userProf_userFrndsCnt = "no_friends";
	public static String userProf_checkInsCnt = "event_checkin";
	public static String userProf_followersCnt = "followers";
	public static String userProf_iam_followingCnt = "iam_following";
	public static String userProf_follow_status = "follow_status";
	public static String userProf_follow_status_follow = "follow";
	public static String userProf_follow_status_not_follow = "not_follow";
	public static String userProf_follow_status_requested = "requested";
	public static String userProf_visibility = "profile_visibility";
	public static String userProf_visibility_public = "public";
	public static String userProf_visibility_friends = "friends";
	public static String userProf_calendar_checked_Events = "user_check_in";

	// web services Constants for profile user events & checked events
	public static String myEvents_Title = "My EVENTS";
	public static String Events_CheckedTitle = "CHECK IN EVENTS";
	public static String Events_userID = "user_id";
	public static String Events_latitude = "user_lat";
	public static String Events_longitude = "user_lng";
	public static String Events_Checkin_Search = "search";
	public static String Events_pageID = "page_id";
	public static String Events_filter = "filter";
	public static String Events_search = "search";
	public static String Events_InfoObj = "uset_post_events";
	public static String Events_Cal_InfoObj = "events_list";
	public static String Events_CheckedInfoObj = "events_list";
	public static String Event_ID = "event_id";
	public static String Event_SportName = "sport_name";
	public static String Event_Title = "event_title";
	public static String Event_description = "event_description";
	public static String Event_team1_ID = "team1_id";
	public static String Event_team1_Type = "team1_type";
	public static String Event_team2_ID = "team2_id";
	public static String Event_team2_Type = "team2_type";
	public static String Event_st_date = "event_start_date";
	public static String Event_st_Time = "event_start_time";
	public static String Event_end_Time = "event_end_time";
	public static String Event_sub_Loc = "event_sub_location";
	public static String Event_addr = "formatted_address";
	public static String Event_dist = "distance_from_user_location";
	public static String Event_likes = "event_likes";
	public static String Event_is_attending = "is_attending";
	public static String Event_img = "event_image";
	public static String Event_filter_none = "none";
	public static String Event_filter_past = "past";
	public static String Event_filter_today = "today";
	public static String Event_filter_upcoming = "upcomming";
	public static String Event_filter_calendar = "calendar";
	// Displaying user added calendar events
	public static String Event_display_calendar = "isCalendarEvent";
	public static String Event_display_calendar_month = "mnth";
	public static String Event_display_calendar_day = "day";
	public static String Event_display_calendar_year = "yr";
	// UI Constants for Titles
	public static String UI_IamFollowingStatus = "FOLLOWING";
	public static String UI_IamNotFollowingStatus = "FOLLOW";
	public static String UI_REQUESTED = "REQUESTED";
	public static String UI_SearchTitle = "DISCOVER SPORTS HISTORY";
	public static String UI_EventsTitle = "EVENTS";
	public static String UI_InviteUserTitle = "SEND INVITATION TO PEOPLE";
	public static String UI_Edit = "EDIT";
	public static String UI_UPLOAD_PHOTO = "UPLOAD PHOTO";
	public static String UI_UPLOAD_VIDEO = "UPLOAD VIDEO";

	// Web services Constants for following users list
	public static String FollowingUsersList_Title = "FOLLOWING";
	public static String FollowingUsersList_InfoObj = "GetFollowingFriends";
	public static String FollowingUsersList_ReqUserID = "user_id";
	public static String FollowingUsersList_userID = "user_id";
	public static String FollowingUsersList_userName = "user_name";
	public static String FollowingUsersList_userPic = "user_profile_pic";
	public static String FollowingUsersList_IamFollowing = "iam_following";

	// Web service Constants for followers users list
	public static String FollowerUsersList_Title = "FOLLOWERS";
	public static String FollowerUsersList_InfoObj = "user_followers";
	public static String FollowerUsersList_ReqUserID = "user_id";
	public static String FollowerUsersList_userID = "user_id";
	public static String FollowerUsersList_userName = "user_name";
	public static String FollowerUsersList_userPic = "user_profile_pic";
	public static String FollowerUsersList_IamFollowing = "following_status";

	// Web service constants for updating follow status
	public static String UpdateFollowStatus_ReqUserID = "follower_id";
	public static String UpdateFollowStatus_ReqFollowUserID = "following_id";
	public static String UpdateFollowStatus_ReqStatus = "status";

	// Web service constants for calendar event dates
	public static String cal_DateList = "event_dates_list";
	public static String cal_DateList_userProfile = "user_post_event_dates_list";
	public static String cal_EventDate = "event_date";

	// Web service constants for search
	public static String search_title = "search_title";
	public static String search_flag = "search_flag";
	public static String search_flag_event = "event";
	public static String search_flag_people = "people";
	public static String search_InfoObj = "search_list";
	public static String search_ID = "id";
	public static String search_name = "name";
	public static String search_image = "image";

	// Web service constants for uploading photo
	public static String Event_media_type = "media_type";
	public static String photo_media_type = "photo";
	public static String video_media_type = "video";
	public static String photo_caption = "post_content";
	public static String photo_media_file = "media_file";
	public static String video_media_file = "video_file";
	public static String photo_media_tagged_users = "tagged_users";

	/**
	 * used while consuming event details webservice.
	 */
	public static final String TAG_EVENT_DETAILS = "event_details";
	public static final String TAG_EVENT_ID = "event_id";
	public static final String TAG_EVENT_SPORT_NAME = "sport_name";
	public static final String TAG_EVENT_CREATOR_ID = "event_creator_id";
	public static final String TAG_EVENT_TITLE = "event_title";
	public static final String TAG_EVENT_DESCRIPTION = "event_description";
	public static final String TAG_EVENT_TEAM1_ID = "team1_id";
	public static final String TAG_EVENT_TEAM1_TYPE = "team1_type";
	public static final String TAG_EVENT_TEAM2_ID = "team2_id";
	public static final String TAG_EVENT_TEAM2_TYPE = "team2_type";
	public static final String TAG_EVENT_START_DATE = "event_start_date";
	public static final String TAG_EVENT_START_TIME = "event_start_time";
	public static final String TAG_EVENT_END_TIME = "event_end_time";
	public static final String TAG_EVENT_SUB_LOCATION = "city";// "event_sub_location";
	public static final String TAG_EVENT_FORMATTED_ADDRESS = "formatted_address";
	public static final String TAG_EVENT_DISTANCE_FROM_USER_LOCATION = "distance_from_user_location";
	public static final String TAG_EVENT_LIKES = "event_likes";
	public static final String TAG_EVENT_LIKE_USERS = "event_like_users";
	public static final String TAG_EVENT_IS_ATTENDIONG = "is_attending";
	public static final String TAG_EVENT_IS_LIKED_EVENT = "is_user_liked";
	public static final String TAG_EVENT_IMAGE = "event_image";
	public static final String TAG_EVENT_TYPE = "event_type";
	public static final String TAG_EVENT_ATTENDING_CNT = "attending_count";
	public static final String TAG_EVENT_ATTENDING_USERS = "attending_users";
	public static final String TAG_EVENT_FIRST_NAME = "first_name";
	public static final String TAG_EVENT_USER_ID = "user_id";
	public static final String TAG_EVENT_PROFILE_IMG = "profile_image";
	public static final String TAG_EVENT_URL = "event_url";
	public static final String TAG_EVENT_LAT = "lat";
	public static final String TAG_EVENT_LNG = "lng";

	// profile casts
	public static final String TAG_PROFILE_CASTINFO = "ProfileCastoInfo";
	public static final String TAG_PROFILE_CAST_EVENT_ID = "event_id";
	public static final String TAG_PROFILE_CAST_ISSTUBHUBEVENT = "is_stubhub_event";
	public static final String TAG_PROFILE_CAST_EVENT_TITLE = "event_title";
	public static final String TAG_PROFILE_CAST_EVENT_TYPE = "event_type";
	public static final String TAG_PROFILE_CAST_EVENT_SPORTNAME = "sport_name";
	public static final String TAG_PROFILE_CAST_EVENT_IMAGE = "event_image";

	// media
	public static final String TAG_EVENT_MEDIA = "event_media";
	public static final String TAG_EVENT_MEDIA_IS_LIKED_EVENT = "is_user_liked";
	public static final String TAG_EVENT_MEDIA_LIKE_CNT = "likes_count";
	public static final String TAG_EVENT_MEDIA_COMMENT_CNT = "comments_count";
	public static final String TAG_EVENT_MEDIA_LATEST_COMMENT = "comment_data";

	// cast
	public static final String TAG_EVENT_MEDIA_CAST_DATA = "cast_data";
	public static final String TAG_EVENT_MEDIA_CAST_COUNT = "cast_count";
	public static final String TAG_EVENT_MEDIA_CAST_MEDIA_ID = "media_id";
	public static final String TAG_EVENT_MEDIA_CAST_USER_ID = "user_id";
	public static final String TAG_EVENT_MEDIA_CAST_FIRST_NAME = "first_name";
	public static final String TAG_EVENT_MEDIA_CAST_PROFILE_IMAGE = "profile_image";
	public static final String TAG_EVENT_MEDIA_CAST_MEDIA_DATA = "media_data";
	public static final String TAG_EVENT_MEDIA_CAST_DATE_CREATED = "date_created";

	// photo
	public static final String TAG_EVENT_MEDIA_PHOTO_DATA = "photo_data";
	public static final String TAG_EVENT_MEDIA_PHOTO_URL = "photo_url";

	public static final String TAG_EVENT_MEDIA_PHOTO_COMMENTS = "photo_comments";
	public static final String TAG_EVENT_MEDIA_PHOTO_COMMENT_ID = "comment_id";
	public static final String TAG_EVENT_MEDIA_PHOTO_COMMENT_TEXT = "comment_text";
	public static final String TAG_EVENT_MEDIA_PHOTO_ADDED_DATE = "added-date";
	public static final String TAG_EVENT_MEDIA_PHOTO_COUNT = "photo_count";
	public static final String TAG_EVENT_MEDIA_PHOTO_COMMENTS_NOT_AVAILABLE = "";

	// video
	public static final String TAG_EVENT_MEDIA_VIDEO_DATA = "video_data";
	public static final String TAG_EVENT_MEDIA_VIDEO_URL = "video_url";
	public static final String TAG_EVENT_MEDIA_VIDEO_THUMBNAIL_URL = "video_thumb_url";

	public static final String TAG_EVENT_ALL_DATA = "all_data";
	public static final String TAG_EVENT_MEDIA_VIDEO_COMMENTS = "video_comments";
	public static final String TAG_EVENT_MEDIA_VIDEO_COMMENT_ID = "comment_id";
	public static final String TAG_EVENT_MEDIA_VIDEO_COMMENT_TEXT = "comment_text";
	public static final String TAG_EVENT_MEDIA_VIDEO_ADDED_DATE = "added-date";
	public static final String TAG_EVENT_MEDIA_VIDEO_COUNT = "video_count";
	public static final String TAG_EVENT_MEDIA_VIDEO_COMMENTS_NOT_AVAILABLE = "";

	// no data
	public static final String TAG_EVENT_MEDIA_NO_DATA = "no media available";
	public static final String TAG_EVENT_MEDIA_NO_MEDIA_DATA = "no data";

	/**
	 * used while consuming event-media comments
	 */
	public static final String TAG_EVENT_MEDIA_ID = "media_id";
	public static final String TAG_EVENT_MEDIA_TYPE = "media_type";
	public static final String TAG_EVENT_MEDIA_TYPE_CAST = "cast";
	public static final String TAG_EVENT_MEDIA_TYPE_IMG = "event_post"; // If
																		// comments
																		// are
																		// requested
																		// for
																		// photos
																		// or
																		// videos
	public static final String TAG_EVENT_MEDIA_COMMENTS_RES = "search_list";
	public static final String TAG_EVENT_MEDIA_COMMENT_ID = "comment_id";
	public static final String TAG_EVENT_MEDIA_COMMENT_USER_IMG = "image";
	public static final String TAG_EVENT_MEDIA_COMMENT_TEXT = "comment_content";
	public static final String TAG_EVENT_MEDIA_COMMENT_USER_NAME = "first_name";
	public static final String TAG_EVENT_MEDIA_COMMENT_USER_ID = "user_id";
	public static final String KEY_EVENT_MEDIA_NOTIFY_USER_ID = "pn_user_id";
	public static final String TAG_EVENT_MEDIA_COMMENT_CREATED = "created";

	// KEYS used while adding data in hashmap.
	public static final String[] KEYs = { "broadcast", "photo", "video", "cast" };// {"BROADCAST","PHOTO",
																					// "VIDEO","CAST"};
	// KEYS used while reporting abusive
	public static final String[] ABS_KEYS = { "ABS_EVENT", "ABS_EVENT_CMNT",
			"ABS_EVENT_PHOTO", "ABS_EVENT_PHOTO_CMNT", "ABS_EVENT_VIDEO",
			"ABS_EVENT_VIDEO_CMNT", "ABS_EVENT_CAST", "ABS_EVENT_CMNT_CAST",
			"ABS_USR_VIDEO", "ABS_USR_VIDEO_CMNT", "ABS_USR_PHOTO",
			"ABS_USR_PHOTO_CMNT" };

	/**
	 * Strings used while sending data from EventFragment to EventDetails
	 * Fragment
	 */
	public static final String KEY_EVENT_ID = "event_id";
	public static final String KEY_USER_ID = "user_id";
	public static final String KEY_EVENT_TYPE = "event_type";
	public static final String KEY_EVENT_PIC = "event_pic";
	public static final String KEY_PROFILE_EVENT_MEDIA = "profile_event_media";
	public static final String KEY_IS_EDIT_EVENT_MEDIA = "edit_event_media";
	public static final String KEY_IS_EDIT_FILTER_IMAGE_FRAGMENT = "is_edit_filter_image_fragment";
	public static final String KEY_IS_EDIT_FILTER_VIDEO_FRAGMENT = "is_edit_filter_video_fragment";
	public static final String KEY_EVENT_MEDIA_ID = "event_media_id";
	/**
	 * Strings used while sending data from EventDetails Fragment to
	 * Addacastfragment
	 */
	public static final String KEY_IS_EVENT_DETAILS = "is_eventdetails";

	/**
	 * cheered users list constants
	 */
	public static final String KEY_USER_DETAILS = "user_details"; // attendees_list
	public static final String KEY_USER_ATTENDEES_DETAILS = "attendees_list";
	public static final String KEY_USER_FNAME = "first_name";
	public static final String KEY_USER_LNAME = "last_name";
	public static final String KEY_USER_USERID = "user_id";

	/**
	 * Updating event media cheer constants
	 * */
	public static final String KEY_EVENT_CHEER_USER_ID = "user_id";
	public static final String KEY_EVENT_CHEER_NOTIFY_USER_ID = "pn_user_id";
	public static final String KEY_EVENT_CHEER_MEDIA_ID = "media_id";
	public static final String KEY_EVENT_CHEER_MEDIA_TYPE = "media_type";
	public static final String KEY_EVENT_CHEER_STATUS = "cheer_status";

	/**
	 * twitter
	 */
	public static final String KEY_TWITTER_ACCESS_TOKEN = "access_token";
	public static final String KEY_TWITTER_ACCESS_TOKEN_SECRET = "access_token_secret";

	/**
	 * GCM
	 */
	public static final String SENDER_ID = "564365121883";
	public static final int GCMNOTIFICATIONID = 123456789;
	public static final String KEY_NOTIFICATION_EXTRAS = "push_extras";
	public static final String DEVICE_TYPE = "ANDROID";
	public static final String NOTIFICATION_COUNT = "Count";
	public static final String GCM_DEVICE_ID = "DeviceId";

	/**
	 * Ad time limitations
	 */
	public static final String AD_LAST_TIME = "ad_last_time";

	/*
	 * User cast, video, photo & tags web service fields
	 */
	public static final String TAG_MEDIA_CAST_INFO = "user_casts_data";
	public static final String TAG_MEDIA_VIDEO_INFO = "user_videos_data";
	public static final String TAG_MEDIA_PHOTO_INFO = "user_photos_data";
	public static final String TAG_MEDIA_TAG_INFO = "user_tagged_data";
	public static final String TAG_MEDIA_ALL_INFO = "user_pfofile_all";
	public static final String TAG_MEDIA_CATEGORY = "category";
	public static final String TAG_MEDIA_TYPE = "type";
	public static final String TAG_MEDIA_TYPE_EVENT = "EVENT";
	public static final String TAG_MEDIA_TYPE_PROFILE = "PROFILE";
	public static final String TAG_MEDIA_EVENT_ID = "event_id";
	public static final String TAG_MEDIA_EVENT_IS_STUBHUB = "is_stubhub_event";
	public static final String TAG_MEDIA_EVENT_TITLE = "event_title";
	public static final String TAG_MEDIA_EVENT_TYPE = "event_type";
	public static final String TAG_MEDIA_SPORT_NAME = "sport_name";
	public static final String TAG_MEDIA_THUMB_URL = "media_thumb_url";
	public static final String TAG_MEDIA_LOCATION = "formatted_address";
	public static final String TAG_MEDIA_ID = "media_id";
	public static final String TAG_MEDIA_CAPTION = "caption";
	public static final String TAG_MEDIA_URL = "media_url";
	public static final String TAG_MEDIA_IS_USER_LIKED = "is_user_liked";
	public static final String TAG_MEDIA_LIKE_COUNT = "likes_count";
	public static final String TAG_MEDIA_COMMENT_COUNT = "comments_count";
	public static final String TAG_MEDIA_DATE_CREATED = "date_created";

	/*
	 * Notification tags
	 */
	public static final String TAG_NOTIFICATION_INFO = "notifications_list";
	public static final String TAG_NOTIFICATION_ID = "notification_id";
	public static final String TAG_NOTIFICATION_SENDER_ID = "sender_id";
	public static final String TAG_NOTIFICATION_EVENTID = "event_id";
	public static final String TAG_NOTIFICATION_EVENTNAME = "event_name";
	public static final String TAG_NOTIFICATION_SENDER_NAME = "sender_name";
	public static final String TAG_NOTIFICATION_SENDER_PIC_URL = "image_url";
	public static final String TAG_NOTIFICATION_TYPE = "notification_type";
	public static final String TAG_NOTIFICATION_DATA_ID = "data_id";
	public static final String TAG_NOTIFICATION_TEXT = "text";
	public static final String TAG_NOTIFICATION_DATA_URL = "data_url";
	public static final String TAG_NOTIFICATION_VIEW_STATUS = "is_viewed";
	public static final String TAG_NOTIFICATION_DATE = "created_date";
	public static final String TAG_NOTIFICATION_TYPE_EVENT_INVITE = "EVENT_INVITE";
	public static final String TAG_NOTIFICATION_TYPE_EVENT_LIKE = "EVENT_LIKE";
	public static final String TAG_NOTIFICATION_TYPE_CAST_LIKE = "EVENT_CAST_LIKE";
	public static final String TAG_NOTIFICATION_TYPE_CAST_COMMENT = "EVENT_CAST_COMMENT";
	public static final String TAG_NOTIFICATION_TYPE_FOLLOW_USER = "FOLLOW_USER";
	public static final String TAG_NOTIFICATION_TYPE_FOLLOW_USER_STATUS_FOLLOW = "Follow";
	public static final String TAG_NOTIFICATION_TYPE_FOLLOW_USER_STATUS_REQ = "requested";
	public static final String TAG_NOTIFICATION_TYPE_FOLLOW_ACCEPT = "FOLLOW_ACCEPTED";
	public static final String TAG_NOTIFICATION_TYPE_FOLLOW_ACCEPT_STATUS_ACCEPT = "accepted";
	public static final String TAG_NOTIFICATION_TYPE_FOLLOW_ACCEPT_STATUS_REJECT = "rejected";
	public static final String TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_TAGGED_USER = "PROFILE_PHOTO_TAGGED_USER";
	public static final String TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_LIKE = "PROFILE_PHOTO_LIKE";
	public static final String TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_COMMENT = "PROFILE_PHOTO_COMMENT";
	public static final String TAG_NOTIFICATION_TYPE_EVENT_PHOTO_TAGGED_USER = "EVENT_PHOTO_TAGGED_USER";
	public static final String TAG_NOTIFICATION_TYPE_EVENT_PHOTO_LIKE = "EVENT_PHOTO_LIKE";
	public static final String TAG_NOTIFICATION_TYPE_EVENT_PHOTO_COMMENT = "EVENT_PHOTO_COMMENT";
	public static final String TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_TAGGED_USER = "PROFILE_VIDEO_TAGGED_USER";
	public static final String TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_LIKE = "PROFILE_VIDEO_LIKE";
	public static final String TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_COMMENT = "PROFILE_VIDEO_COMMENT";
	public static final String TAG_NOTIFICATION_TYPE_EVENT_VIDEO_TAGGED_USER = "EVENT_VIDEO_TAGGED_USER";
	public static final String TAG_NOTIFICATION_TYPE_EVENT_VIDEO_LIKE = "EVENT_VIDEO_LIKE";
	public static final String TAG_NOTIFICATION_TYPE_EVENT_VIDEO_COMMENT = "EVENT_VIDEO_COMMENT";

	/* display detailed notification media */
	public static final String TAG_DISP_NOTIFICATION_USER_ID = "user_id";
	public static final String TAG_DISP_NOTIFICATION_MEDIA_ID = "media_id";
	public static final String TAG_DISP_NOTIFICATION_MEDIA_TYPE = "media_type";
	public static final String TAG_DISP_NOTIFICATION_RESOURCE_TYPE = "resource_type";

	public static final String TAG_CATEGORY = "category";
	public static final String TAG_CATEGORY_EVENT = "EVENT";
	public static final String TAG_CATEGORY_PROFILE = "PROFILE";
	public static final String TAG_CATEGORY_TYPE = "category_type";
	public static final String TAG_CATEGORY_TYPE_photo = "PHOTO";
	public static final String TAG_CATEGORY_TYPE_video = "VIDEO";

	// by koti
	public static final String TAG_FROM_CALENDAR_EVENTS = "is_from_calendar_events_fragment";
}
