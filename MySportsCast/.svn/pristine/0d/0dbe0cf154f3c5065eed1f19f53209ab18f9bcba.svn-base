package com.mysportsshare.mysportscast.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.models.EventInfo;
import com.mysportsshare.mysportscast.models.SportModel;
import com.mysportsshare.mysportscast.models.TeamInfo;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;

public class ServicesActivity extends Activity{
	//views
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MySportsApp.setActivityContext(ServicesActivity.this);
		//service for login
		/*reg_type=facebook&name=Test&oauth_id=9438378473683638&email=syedsulemans@sparshcom.net*/
		String regType = "";
		String userName = "";
		String oauthToken = "";
		String email = "";
		//service for register
		//		serviceToRegisterUser(regType,userName,oauthToken,email);

		//service for getting sports list
		/*http://182.75.34.62/MySportsShare/web_services/get_sports_list.php*/	
		//		serviceToGetSportsList();

		//service for getting team info
		//		serviceToGetTeamInfo();

		//service to get event info
		serviceToGetEventInfo();

	}

	//service to get event info
	private void serviceToGetEventInfo(){
		final String url = Constants.common_url + getString(R.string.get_event_details);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		//TODO
		nameValuePairs.add(new BasicNameValuePair("user_id", "3"));
		nameValuePairs.add(new BasicNameValuePair("event_id", "81"));
		nameValuePairs.add(new BasicNameValuePair("user_lat", "43.59685959999999"));
		nameValuePairs.add(new BasicNameValuePair("user_lng", "3.8502617000000328"));

		JsonParser.callBackgroundService(url, nameValuePairs, new JsonServiceListener() {
			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(ServicesActivity.this);
				pd.show();
				pd.setMessage("Loading...");
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
			}

			@Override
			public void hideProgress() {
				super.hideProgress();
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
			}

			@Override
			public void parseJsonResponse(String jsonResponse) {
			/*	Log.v("", "URL: " + url +" event details RESPONSE: " + jsonResponse);
				if (jsonResponse!= null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject.getJSONObject(Constants.TAG_RESPONSE);
						String responseStr = resObj.getString(Constants.TAG_RESPONSE_INFO);
						List<TeamInfo> myteamList = new ArrayList<TeamInfo>();
						if (responseStr!=null && responseStr.equalsIgnoreCase(Constants.TAG_SUCCESS)){

							JSONObject eventDetailsObj = resObj.getJSONObject(Constants.TAG_EVENT_DETAILS);
							EventInfo eventinfo = new EventInfo();
							eventinfo.setEventId(eventDetailsObj.getString(Constants.TAG_EVENT_ID));
							eventinfo.setSportName(eventDetailsObj.getString(Constants.TAG_EVENT_SPORT_NAME));
							eventinfo.setEventTitle(eventDetailsObj.getString(Constants.TAG_EVENT_TITLE));
							eventinfo.setEventDescription(eventDetailsObj.getString(Constants.TAG_EVENT_DESCRIPTION));
							eventinfo.setTeam1Id(eventDetailsObj.getString(Constants.TAG_EVENT_TEAM1_ID));
							eventinfo.setTeam1Type(eventDetailsObj.getString(Constants.TAG_EVENT_TEAM1_TYPE));
							eventinfo.setTeam2Id(eventDetailsObj.getString(Constants.TAG_EVENT_TEAM2_ID));
							eventinfo.setTeam2Type(eventDetailsObj.getString(Constants.TAG_EVENT_TEAM2_TYPE));
							eventinfo.setEventStartDate(eventDetailsObj.getString(Constants.TAG_EVENT_START_DATE));
							eventinfo.setEventStartTime(eventDetailsObj.getString(Constants.TAG_EVENT_START_TIME));
							eventinfo.setEventEndTime(eventDetailsObj.getString(Constants.TAG_EVENT_END_TIME));
							eventinfo.setEventSubLocation(eventDetailsObj.getString(Constants.TAG_EVENT_SUB_LOCATION));
							eventinfo.setEventAddress(eventDetailsObj.getString(Constants.TAG_EVENT_FORMATTED_ADDRESS));
							eventinfo.setEventDistanceFromUserLoc(eventDetailsObj.getString(Constants.TAG_EVENT_DISTANCE_FROM_USER_LOCATION));
							eventinfo.setEventLikes(eventDetailsObj.getString(Constants.TAG_EVENT_LIKES));
							eventinfo.setIsAttending(eventDetailsObj.getString(Constants.TAG_EVENT_IS_ATTENDIONG));
							eventinfo.setEventImageUrl(eventDetailsObj.getString(Constants.TAG_EVENT_IMAGE));

							//checking whether media data available or not for this event
							if (resObj.getString(Constants.TAG_EVENT_MEDIA) != null && 
									!resObj.getString(Constants.TAG_EVENT_MEDIA).equalsIgnoreCase(Constants.TAG_EVENT_MEDIA_NO_DATA)) {
								HashMap<String, ArrayList<EventInfo.EventMedia>> eventDataMap = getEventMediaData(resObj, eventinfo);	

								//iterate all event casts, photos, videos.
								Set<String> keys = eventDataMap.keySet();
								Iterator<String> iterator = keys.iterator();
								while (iterator.hasNext()) {
									String key = iterator.next();
									ArrayList<EventInfo.EventMedia> mediaList = eventDataMap.get(key);
									//									Log.d("", key + " Media Count: " + mediaList.size());
									for (EventInfo.EventMedia eventMedia: mediaList) {
										//										Log.v("", "MediaListItemId: " + eventMedia.getMediaId());
										 Comments are fetched from anther web service
										 * HashMap<String, ArrayList<EventInfo.EventMedia.EventMediaComments>> commentsMap = eventMedia.getMediaCommentsMap();
										Set<String> commentKeys = commentsMap.keySet();
										Iterator<String> iterator2 = commentKeys.iterator();
										while (iterator2.hasNext()) {
											String commentKey = iterator2.next();
											ArrayList<EventInfo.EventMedia.EventMediaComments> commentList = commentsMap.get(commentKey);
//											Log.i("", "MediaCommentKey: "+ commentKey +" CommentList : " + commentList.size());
											for (int k = 0; k < commentList.size(); k++) {
												Log.i("", key + " MediaItem "+ eventMedia.getMediaFirstName()+" Comments: " + commentList.get(k).getMediaCommentId());
											}
										}
									}
								}
							} else {
								UIHelperUtil.showToastMessage("No event media data");
							}


						}else if(responseStr!=null && responseStr.equalsIgnoreCase(Constants.TAG_FAILURE)){
							UIHelperUtil.showToastMessage("No event found");
						}else{
							UIHelperUtil.showToastMessage(getString(R.string.service_toast));
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					UIHelperUtil.showToastMessage(getString(R.string.service_toast));
				}*/
			}
		});
	}

	/*private HashMap<String, ArrayList<EventInfo.EventMedia>> getEventMediaData(JSONObject resObj, EventInfo eventinfo) throws JSONException {
		HashMap<String, ArrayList<EventInfo.EventMedia>> eventMediaMap = new HashMap<String, ArrayList<EventInfo.EventMedia>>();
		//		Log.d("", "MediaData: " + resObj.getString(Constants.TAG_EVENT_MEDIA));
		JSONObject eventMediaObj = resObj.getJSONObject(Constants.TAG_EVENT_MEDIA);

		//checking whether cast data available for this media or not
		if (eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_CAST_DATA) != null &&
				!eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_CAST_DATA).equalsIgnoreCase(Constants.TAG_EVENT_MEDIA_NO_MEDIA_DATA)) {
			//Log.d("", "MediaCastData: " + eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_CAST_DATA));
			JSONArray castDataArray = eventMediaObj.getJSONArray(Constants.TAG_EVENT_MEDIA_CAST_DATA);
			HashMap<String, ArrayList<EventInfo.EventMedia.EventMediaComments>> commentsMap = new HashMap<String, ArrayList<EventInfo.EventMedia.EventMediaComments>>();
			ArrayList<EventInfo.EventMedia> castList = new ArrayList<EventInfo.EventMedia>();
			for (int i = 0; i < castDataArray.length(); i++) {
				JSONObject castDataObj = castDataArray.getJSONObject(i);
				EventInfo.EventMedia eventMedia = eventinfo.new EventMedia();
				eventMedia.setMediaId(castDataObj.getString(Constants.TAG_EVENT_MEDIA_CAST_MEDIA_ID));
				eventMedia.setMediaUserId(castDataObj.getString(Constants.TAG_EVENT_MEDIA_CAST_USER_ID));
				eventMedia.setMediaFirstName(castDataObj.getString(Constants.TAG_EVENT_MEDIA_CAST_FIRST_NAME));
				eventMedia.setMediaProfileImage(castDataObj.getString(Constants.TAG_EVENT_MEDIA_CAST_PROFILE_IMAGE));
				eventMedia.setMediaData(castDataObj.getString(Constants.TAG_EVENT_MEDIA_CAST_MEDIA_DATA));
				eventMedia.setMediaData(castDataObj.getString(Constants.TAG_EVENT_MEDIA_CAST_DATE_CREATED));

				castList.add(eventMedia);
			}
			//			commentsMap.put(, value)
			eventMediaMap.put(Constants.KEYs[3], castList);
		}

		//checking whether images data available for this media or not
		if (eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_PHOTO_DATA) != null &&
				!eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_PHOTO_DATA).equalsIgnoreCase(Constants.TAG_EVENT_MEDIA_NO_MEDIA_DATA)) {
			//Log.d("", "MediaPhotoData: " + eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_PHOTO_DATA));
			JSONObject photoDataObj = eventMediaObj.getJSONObject(Constants.TAG_EVENT_MEDIA_PHOTO_DATA);
			ArrayList<EventInfo.EventMedia> photoList = new ArrayList<EventInfo.EventMedia>();
			int count = photoDataObj.getInt(Constants.TAG_EVENT_MEDIA_PHOTO_COUNT);
			for (int i = 0; i < count; i++) {
				JSONObject firstPhotoObj = photoDataObj.getJSONObject(String.valueOf(i));
				EventInfo.EventMedia eventMedia = eventinfo.new EventMedia();
				//This is the key for all media objects.
				String mediaId = firstPhotoObj.getString(Constants.TAG_EVENT_MEDIA_CAST_MEDIA_ID);
				eventMedia.setMediaId(mediaId);
				eventMedia.setMediaUserId(firstPhotoObj.getString(Constants.TAG_EVENT_MEDIA_CAST_USER_ID));
				eventMedia.setMediaFirstName(firstPhotoObj.getString(Constants.TAG_EVENT_MEDIA_CAST_FIRST_NAME));
				eventMedia.setMediaProfileImage(firstPhotoObj.getString(Constants.TAG_EVENT_MEDIA_CAST_PROFILE_IMAGE));
				eventMedia.setMediaPhotoUrl(firstPhotoObj.getString(Constants.TAG_EVENT_MEDIA_PHOTO_URL));
				eventMedia.setMediaDateCreated(firstPhotoObj.getString(Constants.TAG_EVENT_MEDIA_CAST_DATE_CREATED));

				 Comments are fetched from separate web service	
			  //defined one comments hashmap which will be kept in parent map
				HashMap<String, ArrayList<EventInfo.EventMedia.EventMediaComments>> commentsMap = new HashMap<String, ArrayList<EventInfo.EventMedia.EventMediaComments>>();
				if ((firstPhotoObj.getString(Constants.TAG_EVENT_MEDIA_PHOTO_COMMENTS) != null) && 
						!firstPhotoObj.getString(Constants.TAG_EVENT_MEDIA_PHOTO_COMMENTS).equalsIgnoreCase(Constants.TAG_EVENT_MEDIA_PHOTO_COMMENTS_NOT_AVAILABLE)) {
					ArrayList<EventInfo.EventMedia.EventMediaComments> photoCommentsList = new ArrayList<EventInfo.EventMedia.EventMediaComments>();
					JSONArray photoCommentsArray = firstPhotoObj.getJSONArray(Constants.TAG_EVENT_MEDIA_PHOTO_COMMENTS);
					for (int j = 0; j < photoCommentsArray.length(); j++) {
						JSONObject photoCommentsObj = photoCommentsArray.getJSONObject(j);
						EventInfo.EventMedia.EventMediaComments photoComments = eventMedia.new EventMediaComments();
						photoComments.setMediaCommentId(photoCommentsObj.getString(Constants.TAG_EVENT_MEDIA_PHOTO_COMMENT_ID));
						photoComments.setMediaCommentUserId(photoCommentsObj.getString(Constants.TAG_EVENT_MEDIA_CAST_USER_ID));
						photoComments.setMediaCommentFirstName(photoCommentsObj.getString(Constants.TAG_EVENT_MEDIA_CAST_FIRST_NAME));
						photoComments.setMediaCommentProfileImage(photoCommentsObj.getString(Constants.TAG_EVENT_MEDIA_CAST_PROFILE_IMAGE));
						photoComments.setMediaCommentText(photoCommentsObj.getString(Constants.TAG_EVENT_MEDIA_PHOTO_COMMENT_TEXT));
						photoComments.setMediaCommentAddedText(photoCommentsObj.getString(Constants.TAG_EVENT_MEDIA_PHOTO_ADDED_DATE));

						//comments are added to this arraylist
						photoCommentsList.add(photoComments);
					}

					//Comment arraylist is added to this commentsHashMap.
					commentsMap.put(mediaId, photoCommentsList);
				}
				//CommentsHashMap id handed over to eventMedia obj.
				eventMedia.setMediaCommentsMap(commentsMap);

				//the eventMedia onject is added to parent arraqyList which contains all medias.
				photoList.add(eventMedia);
			}
			//the videos(arraylist of all media i.e. videos) are added to this parent hashmap.
			eventMediaMap.put(Constants.KEYs[1], photoList);

		}

		//checking whether video data available for this media or not
		if (eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_VIDEO_DATA) != null &&
				!eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_VIDEO_DATA).equalsIgnoreCase(Constants.TAG_EVENT_MEDIA_NO_MEDIA_DATA)) {
			//Log.d("", "MediaVideoData: " + eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_VIDEO_DATA));
			JSONObject videoDataObj = eventMediaObj.getJSONObject(Constants.TAG_EVENT_MEDIA_VIDEO_DATA);
			ArrayList<EventInfo.EventMedia> videoList = new ArrayList<EventInfo.EventMedia>();
			int count = videoDataObj.getInt(Constants.TAG_EVENT_MEDIA_VIDEO_COUNT);
			for (int i = 0; i < count; i++) {
				JSONObject firstvideoObj = videoDataObj.getJSONObject(String.valueOf(i));
				EventInfo.EventMedia eventMedia = eventinfo.new EventMedia();

				//key for hashmap of comments based on mediaId.
				String mediaId = firstvideoObj.getString(Constants.TAG_EVENT_MEDIA_CAST_MEDIA_ID);
				eventMedia.setMediaId(mediaId);
				eventMedia.setMediaUserId(firstvideoObj.getString(Constants.TAG_EVENT_MEDIA_CAST_USER_ID));
				eventMedia.setMediaFirstName(firstvideoObj.getString(Constants.TAG_EVENT_MEDIA_CAST_FIRST_NAME));
				eventMedia.setMediaProfileImage(firstvideoObj.getString(Constants.TAG_EVENT_MEDIA_CAST_PROFILE_IMAGE));
				eventMedia.setMediaPhotoUrl(firstvideoObj.getString(Constants.TAG_EVENT_MEDIA_VIDEO_URL));
				eventMedia.setMediaDateCreated(firstvideoObj.getString(Constants.TAG_EVENT_MEDIA_CAST_DATE_CREATED));
				
				 * Comments are fetched from another web service
				//defined one comments hashmap which will be kept in parent map
				HashMap<String, ArrayList<EventInfo.EventMedia.EventMediaComments>> commentsMap = new HashMap<String, ArrayList<EventInfo.EventMedia.EventMediaComments>>();
				if ((firstvideoObj.getString(Constants.TAG_EVENT_MEDIA_VIDEO_COMMENTS) != null) && 
						!firstvideoObj.getString(Constants.TAG_EVENT_MEDIA_VIDEO_COMMENTS).equalsIgnoreCase(Constants.TAG_EVENT_MEDIA_VIDEO_COMMENTS_NOT_AVAILABLE)) {
					ArrayList<EventInfo.EventMedia.EventMediaComments> photoCommentsList = new ArrayList<EventInfo.EventMedia.EventMediaComments>();
					JSONArray photoCommentsArray = firstvideoObj.getJSONArray(Constants.TAG_EVENT_MEDIA_VIDEO_COMMENTS);
					for (int j = 0; j < photoCommentsArray.length(); j++) {
						JSONObject photoCommentsObj = photoCommentsArray.getJSONObject(j);
						EventInfo.EventMedia.EventMediaComments photoComments = eventMedia.new EventMediaComments();
						photoComments.setMediaCommentId(photoCommentsObj.getString(Constants.TAG_EVENT_MEDIA_VIDEO_COMMENT_ID));
						photoComments.setMediaCommentUserId(photoCommentsObj.getString(Constants.TAG_EVENT_MEDIA_CAST_USER_ID));
						photoComments.setMediaCommentFirstName(photoCommentsObj.getString(Constants.TAG_EVENT_MEDIA_CAST_FIRST_NAME));
						photoComments.setMediaCommentProfileImage(photoCommentsObj.getString(Constants.TAG_EVENT_MEDIA_CAST_PROFILE_IMAGE));
						photoComments.setMediaCommentText(photoCommentsObj.getString(Constants.TAG_EVENT_MEDIA_VIDEO_COMMENT_TEXT));
						photoComments.setMediaCommentAddedText(photoCommentsObj.getString(Constants.TAG_EVENT_MEDIA_VIDEO_ADDED_DATE));

						//all comments are adde to this arrayList
						photoCommentsList.add(photoComments);
					}
					//COmments arrayList ias added to this Comments hashmap
					commentsMap.put(mediaId, photoCommentsList);
				}
				//Comments hashmap is handed over to eventMedia object.
				eventMedia.setMediaCommentsMap(commentsMap); 

				//the eventMedia onject is added to parent arraqyList which contains all medias.
				videoList.add(eventMedia);
			}			
			//the videos(arraylist of all media i.e. videos) are added to this parent hashmap.
			eventMediaMap.put(Constants.KEYs[2], videoList);
		}
		return eventMediaMap;

	}*/

	//service to get team info
	private void serviceToGetTeamInfo(){
		String url = Constants.common_url + getString(R.string.get_my_team_info);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user_id", SharedPreferencesUtils.getUserId()));


		JsonParser.callBackgroundService(url, nameValuePairs, new JsonServiceListener() {
			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(ServicesActivity.this);
				pd.show();
				pd.setMessage("Loading...");
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
			}

			@Override
			public void hideProgress() {
				super.hideProgress();
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
			}

			@Override
			public void parseJsonResponse(String jsonResponse) {
				System.out.println("RESPONSE: " + jsonResponse);
				if (jsonResponse!= null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject.getJSONObject("Response");
						String responseStr = resObj.getString("ResponseInfo");
						List<TeamInfo> myteamList = new ArrayList<TeamInfo>();
						if (responseStr!=null && responseStr.equalsIgnoreCase("SUCCESS")){

							JSONArray myteaminfoArr = resObj.getJSONArray("team_list");
							if(myteaminfoArr != null){
								for(int i = 0; i < myteaminfoArr.length(); i++){
									TeamInfo teaminfo = new TeamInfo();
									teaminfo.setTeamId(myteaminfoArr.getJSONObject(i).getString("team_id"));
									teaminfo.setSportName(myteaminfoArr.getJSONObject(i).getString("sport_name"));
									teaminfo.setTeamName(myteaminfoArr.getJSONObject(i).getString("team_name"));
									myteamList.add(teaminfo);
								}

							}else{
								UIHelperUtil.showToastMessage("No teams found");
							}

						}else if(responseStr!=null && responseStr.equalsIgnoreCase("FAILURE")){
							UIHelperUtil.showToastMessage("No teams found");
						}else{
							UIHelperUtil.showToastMessage(getString(R.string.service_toast));
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					UIHelperUtil.showToastMessage(getString(R.string.service_toast));
				}
			}
		});
	}
	//service to get sports list
	private void serviceToGetSportsList() {
		String url = Constants.common_url +
				getResources().getString(R.string.get_sports_list);

		JsonParser.callBackgroundService(url, null, new JsonServiceListener() {
			ProgressDialog pd;
			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(ServicesActivity.this);
				pd.setMessage("Loading...");
				pd.show();
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
			}

			@Override
			public void hideProgress() {
				if(pd != null && pd.isShowing()) {
					pd.dismiss();
				}
			}
			@Override
			public void parseJsonResponse(String jsonResponse) {
				setJsonValues(jsonResponse);
			}
		});
	}

	private void setJsonValues(String jsonResult) {
		try {
			System.out.println("response:  " + jsonResult);
			JSONObject reader = new JSONObject(jsonResult);
			JSONObject sys  = reader.getJSONObject("Response");
			String response = sys.getString("ResponseInfo");
			List<SportModel> sportsList = new ArrayList<SportModel>();
			if(response != null && response.equals("SUCCESS")) {
				JSONArray sportsDetailsJson = sys.getJSONArray("sprots_list");
				for(int i=0; i<sportsDetailsJson.length(); i++) {
					JSONObject sportObject = sportsDetailsJson.getJSONObject(i);
					SportModel sportModel = new SportModel();
					sportModel.setSportId(sportObject.getString("sport_id"));
					sportModel.setSportName(sportObject.getString("sport_name"));
					sportModel.setMaxPlayersOnField(sportObject.getString("max_players_on_field"));
					sportModel.setSportRounds(sportObject.getString("rounds"));
					sportsList.add(sportModel);
					System.out.println("list: " + sportsList.get(i));
				}

			} else if(response!=null && response.equalsIgnoreCase("FAILURE")){
				UIHelperUtil.showToastMessage("No sports list found");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * this method will call register the user
	 * http://182.75.34.62/MySportsShare/web_services/register_social.php
	 * reg_type=facebook&name=Test&oauth_id=9438378473683638&email=syedsulemans@sparshcom.net
	 */

	private void serviceToRegisterUser(String regType,final String userName,final String oauthToken,String email){

		String url = Constants.common_url + getString(R.string.register_social);
		Log.i("", "URL login: " + url+"  "+oauthToken+"  "+userName+"  "+regType+"  "+email);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("reg_type", regType));
		nameValuePairs.add(new BasicNameValuePair("name", userName));
		nameValuePairs.add(new BasicNameValuePair("oauth_id", oauthToken));
		nameValuePairs.add(new BasicNameValuePair("email", email));

		JsonParser.callBackgroundService(url, nameValuePairs, new JsonServiceListener() {
			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(ServicesActivity.this);
				pd.show();
				pd.setMessage("Loading...");
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
			}

			@Override
			public void hideProgress() {
				super.hideProgress();
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
			}

			@Override
			public void parseJsonResponse(String jsonResponse) {
				System.out.println("RESPONSE: " + jsonResponse);
				if (jsonResponse!= null) {
					JSONObject jsonObject;
					String userId = "";
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject.getJSONObject("Response");
						String responseStr = resObj.getString("ResponseInfo");
						if (responseStr!=null && responseStr.equalsIgnoreCase("SUCCESS")) {
							userId = resObj.getString("user_id");
						} else {
							userId = SharedPreferencesUtils.getUserId();
							System.out.println("responce: " + responseStr);
						}
						SharedPreferencesUtils.setUserId(userId);
						SharedPreferencesUtils.setUserName(userName);
						SharedPreferencesUtils.setOauthUserId(oauthToken);
						/*Intent intent = new Intent(LoginActivity.this, SearchPageActivity.class);
						startActivity(intent);*/
						finish();

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					UIHelperUtil.showToastMessage("Some thing went wrong at server side");
				}
			}
		});

	}


}
