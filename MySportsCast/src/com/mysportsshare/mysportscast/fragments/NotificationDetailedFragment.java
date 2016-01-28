package com.mysportsshare.mysportscast.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.activity.ShowTagEventActivity;
import com.mysportsshare.mysportscast.adapters.CommentsAdapter;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.models.Comments;
import com.mysportsshare.mysportscast.models.ProfileMediaInfo;
import com.mysportsshare.mysportscast.models.TeamInfo;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.CustomLog;
import com.mysportsshare.mysportscast.utils.HelperMethods;
import com.mysportsshare.mysportscast.utils.HelperMethods.TwitterCallback;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.SocialNetworkingUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class NotificationDetailedFragment extends Fragment implements OnClickListener{
	private TextView title;
	private ImageView back;
	private ImageView settingBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private LinearLayout footerlyt;
	private FrameLayout fragmentlyt;
	private ImageView addEventBtn;
	private RelativeLayout events_header_llyt;
	private RelativeLayout search_header_llyt;

	private TextView event_title_tv;
	private TextView sport_type_tv;
	private TextView event_loc_tv;
	private TextView caption_tv;
	private ImageView media_pic;
	private ImageView media_video;
	private ImageView media_video_play;
	private TextView media_cast;
	private LinearLayout media_cheer_layout;
	private LinearLayout media_comment_layout;
	private ImageView cheers_tv_icon;
	private TextView cheers_tv;
	private ImageView comment_tv_icon;
	private TextView tvComment;
	private ImageView media_share_iv;
	private Activity activity;

	String userId;
	ProfileMediaInfo mediaItem=null;

	Handler m_Handler;
	Runnable mRunnable;
	Dialog altDlg;

	// Comments dialog views
	ListView commentUsersListView;
	TextView commentListStatus;
	EditText commentEditText;
	ImageView commentPostBtn;
	CommentsAdapter cmtAdapter;
	private String userName;
	String titleStr ="";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layoutView = inflater.inflate(R.layout.user_profile_media_item, container, false);

		init(layoutView);
		userId = SharedPreferencesUtils.getUserId();

		Bundle bundle = activity.getIntent().getExtras();
		if (bundle != null) {
			//			ProfileMediaInfo mediaItem = bundle.getParcelable("");
			//			setAllTheFields(mediaItem);
			titleStr = bundle.getString(Constants.TAG_DISP_NOTIFICATION_MEDIA_TYPE).toUpperCase();
			serviceDisplayMediaFromNotification(bundle);
		}
		CustomLog.v("Tagged", "Tagged: " + bundle);
		return layoutView;
	}

	@Override
	public void onStart() {
		super.onStart();
		title.setText(titleStr);
	}

	private void startUpdateUI() {
		//To update UI only after closing comments dialog 
		m_Handler = new Handler();
		mRunnable = new Runnable(){
			@Override
			public void run() {
				if(altDlg!=null){
					if(altDlg.isShowing()){
						m_Handler.postDelayed(mRunnable, 1000);// move this inside the run method		
					}else{
						setAllTheFields();		
					}
				}
			} 
		};
		new Thread(mRunnable).start();
//		mRunnable.run();
	}

	private void setAllTheFields() {
		if(mediaItem.getMediaType().equalsIgnoreCase(Constants.TAG_MEDIA_TYPE_EVENT)){
			event_title_tv.setVisibility(View.VISIBLE);
			sport_type_tv.setVisibility(View.VISIBLE);
			event_loc_tv.setVisibility(View.VISIBLE);
			event_title_tv.setText(mediaItem.getMediaEventTitle());
			sport_type_tv.setText(mediaItem.getMediaSportName());
			event_loc_tv.setText(mediaItem.getMediaLocation());
		}else{
			event_title_tv.setVisibility(View.GONE);
			sport_type_tv.setVisibility(View.GONE);
			event_loc_tv.setVisibility(View.GONE);
		}

		if(mediaItem.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)){
			media_cast.setVisibility(View.VISIBLE);
			media_pic.setVisibility(View.GONE);
			media_video.setVisibility(View.GONE);
			media_video_play.setVisibility(View.GONE);
			caption_tv.setVisibility(View.GONE);
			media_cast.setText(mediaItem.getMediaCaption());
		}else if(mediaItem.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)){
			media_cast.setVisibility(View.GONE);
			media_pic.setVisibility(View.GONE);
			media_video.setVisibility(View.VISIBLE);
			media_video_play.setVisibility(View.VISIBLE);
			caption_tv.setVisibility(View.VISIBLE);			
			caption_tv.setText(mediaItem.getMediaCaption());
			if(!TextUtils.isEmpty(mediaItem.getMediaUrl())){
				if(!TextUtils.isEmpty(mediaItem.getMediaThumbUrl())){
					BitmapUtils.setImages(mediaItem.getMediaThumbUrl(), media_video, R.drawable.default_event_pic);
				}
				
				/*Uri uri=Uri.parse(mediaItem.getMediaUrl());
				media_video.setVideoURI(uri);
				media_video.seekTo(100);*/ //Display image at 0.1 sec
			}
		}else if(mediaItem.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)){
			media_cast.setVisibility(View.GONE);
			media_pic.setVisibility(View.GONE);
			media_video_play.setVisibility(View.GONE);
			media_pic.setVisibility(View.VISIBLE);
			caption_tv.setVisibility(View.VISIBLE);
			caption_tv.setText(mediaItem.getMediaCaption());
			if(!TextUtils.isEmpty(mediaItem.getMediaUrl())){
				BitmapUtils.setImages(mediaItem.getMediaUrl(), media_pic, R.drawable.default_event_pic);	
			}

		}

		// Updating user liked to event-media img
		if (!TextUtils.isEmpty(mediaItem.getIsUserLiked())
				&& mediaItem.getIsUserLiked().equalsIgnoreCase("true")) {
			cheers_tv_icon.setImageResource(R.drawable.mega_phone_cheer);
		} else {
			cheers_tv_icon.setImageResource(R.drawable.mega_phone);
		}

		// Updating cheers count
		if (TextUtils.isEmpty(mediaItem.getMediaLikeCount())) {
			cheers_tv.setText("0 cheers");
		} else {
			cheers_tv.setText(mediaItem.getMediaLikeCount() + " cheers");
		}

		// Updating comments count
		if (TextUtils.isEmpty(mediaItem.getMediaCommentCount())) {
			tvComment.setText("0 comments");
		} else {
			tvComment.setText(mediaItem.getMediaCommentCount() + " comments");
		}

		//Assigning listeners to media entity
		media_cheer_layout.setOnClickListener(this);
		media_comment_layout.setOnClickListener(this);
		media_share_iv.setOnClickListener(this);
		media_video_play.setOnClickListener(this);

		/*media_cheer_layout.setTag(mediaItem);
		media_comment_layout.setTag(mediaItem);
		media_share_iv.setTag(mediaItem);
		media_video_play.setTag(mediaItem);*/
	}

	private void init(View layoutView) {
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);
		back = (ImageView) activity.findViewById(R.id.back_iv);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);

		events_header_llyt = (RelativeLayout) activity.findViewById(R.id.events_header_llyt);
		search_header_llyt = (RelativeLayout) activity.findViewById(R.id.search_header_llyt);

		searchHeaderBtn = (ImageView)activity.findViewById(R.id.search_iv);
		settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);
		tvEventType = (TextView) activity.findViewById(R.id.title_bar_tv_event_type);
		addEventBtn = (ImageView)activity.findViewById(R.id.add_an_event_iv);
		fragmentlyt = (FrameLayout) activity.findViewById(R.id.home_frame);
		footerlyt = (LinearLayout) activity.findViewById(R.id.footer_llyt);

		event_title_tv = ((TextView)layoutView.findViewById(R.id.event_title_tv));
		sport_type_tv = ((TextView)layoutView.findViewById(R.id.sport_type_tv));
		event_loc_tv = ((TextView)layoutView.findViewById(R.id.event_loc_tv));
		caption_tv = ((TextView)layoutView.findViewById(R.id.caption_tv));
		media_pic = ((ImageView)layoutView.findViewById(R.id.media_pic));
		media_video = ((ImageView)layoutView.findViewById(R.id.media_video));
		media_video_play = ((ImageView)layoutView.findViewById(R.id.media_video_play));
		media_cast = ((TextView)layoutView.findViewById(R.id.media_cast));
		media_cheer_layout = ((LinearLayout)layoutView.findViewById(R.id.media_cheer_layout));
		media_comment_layout = ((LinearLayout)layoutView.findViewById(R.id.media_comment_layout));
		cheers_tv_icon = ((ImageView)layoutView.findViewById(R.id.cheers_tv_icon));
		cheers_tv = ((TextView)layoutView.findViewById(R.id.cheers_tv));
		comment_tv_icon = ((ImageView)layoutView.findViewById(R.id.comment_tv_icon));
		tvComment = ((TextView)layoutView.findViewById(R.id.comment_tv));
		media_share_iv = ((ImageView)layoutView.findViewById(R.id.media_share_iv));

		cheers_tv.setOnClickListener(this);
		cheers_tv_icon.setOnClickListener(this);
		tvComment.setOnClickListener(this);
		comment_tv_icon.setOnClickListener(this);
		media_share_iv.setOnClickListener(this);
		media_video_play.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.cheers_tv:
		case R.id.cheers_tv_icon:
			triggerEventMediaCheer(v);
			break;
		case R.id.comment_tv:
		case R.id.comment_tv_icon:
			triggerCommentsDialogByView(v);
			break;
		case R.id.media_share_iv:
			showShareDialog("Sub-Sample", "Sub-Sample link","",v);
			break;
		case R.id.media_video_play:
			triggerPlayVideo(v);
			break;
		case R.id.back_iv:
			activity.onBackPressed();
			break;
		}
	}

	private void triggerPlayVideo(View v) {
		if(mediaItem!=null){
			Fragment profile_all_Fragment = new VideosPlayFragment();
			Bundle args10 = new Bundle();
			args10.putString(Constants.userProf_userID,userId);
			args10.putString(Constants.userProf_userName,userName);
			args10.putString(Constants.userProf_videoPath,mediaItem.getMediaUrl());
			args10.putString(Constants.userProf_caption,mediaItem.getMediaCaption());
			profile_all_Fragment.setArguments(args10);
			((ShowTagEventActivity)activity).push(profile_all_Fragment);
		}
	}

	public void triggerEventMediaCheer(View v) {
		if (mediaItem != null) {
			if (mediaItem.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)) {
				// Update cheer staus for images & videos
				if (mediaItem.getIsUserLiked().equalsIgnoreCase("false")) {
					// Send cheer status
					updateEventMediaCheerStatus(
							userId,
							mediaItem.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_CAST, "true");
				} else {
					// Send un-cheer status
					updateEventMediaCheerStatus(
							userId,
							mediaItem.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_CAST, "false");
				}
			} else {
				// Update cheer staus for images & videos
				if (mediaItem.getIsUserLiked().equalsIgnoreCase("false")) {
					// Send cheer status
					updateEventMediaCheerStatus(
							userId,
							mediaItem.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_IMG, "true");
				} else {
					// Send un-cheer status
					updateEventMediaCheerStatus(
							userId,
							mediaItem.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_IMG, "false");
				}
			}
		} else {
			UIHelperUtil.showToastMessage("Unable to cheer!....");
		}
	}

	/* Method: updateEventCheerStatus Des: Update the cheer status of an event
	 * media Input: userID: logged user id mediaId : present cheering media
	 * mediaType : Cheered to which media cheerStatus: Cheering or uncheering
	 * status*/

	public void getCommentsList(final String notifyUserId, final String mediaId,
			final String mediaType, final ProfileMediaInfo eventMedia) {
		final String url = Constants.common_url
				+ MySportsApp.getAppContext().getString(R.string.get_event_media_comment);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_MEDIA_ID, mediaId));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_MEDIA_TYPE, mediaType));
		JsonParser.callBackgroundService(url, nameValuePairs,
				new JsonServiceListener() {
			@Override
			public void parseJsonResponse(String jsonResponse) {
				if (jsonResponse != null) {
					Log.v("", "URL: " + url
							+ " event-media comments RESPONSE: "
							+ jsonResponse);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject
								.getJSONObject(Constants.TAG_RESPONSE);
						String responseStr = resObj
								.getString(Constants.TAG_RESPONSE_INFO);
						if (responseStr != null
								&& responseStr
								.equalsIgnoreCase(Constants.TAG_SUCCESS)) {
							// Fetch comments from server
							JSONArray cmtsJSONArray = resObj
									.getJSONArray(Constants.TAG_EVENT_MEDIA_COMMENTS_RES);
							List<Comments> cmtList = new ArrayList<Comments>();
							for (int i = 0; i < cmtsJSONArray.length(); i++) {
								JSONObject cmtJSON = cmtsJSONArray
										.getJSONObject(i);
								Comments cmtObj = new Comments();
								cmtObj.setMediaCommentFirstName(cmtJSON
										.getString(Constants.TAG_EVENT_MEDIA_COMMENT_USER_NAME));
								cmtObj.setMediaCommentId(cmtJSON
										.getString(Constants.TAG_EVENT_MEDIA_COMMENT_ID));
								cmtObj.setMediaCommentUserId(cmtJSON
										.getString(Constants.TAG_EVENT_MEDIA_COMMENT_USER_ID));
								cmtObj.setMediaCommentText(cmtJSON
										.getString(Constants.TAG_EVENT_MEDIA_COMMENT_TEXT));
								cmtObj.setMediaCommentProfileImage(cmtJSON
										.getString(Constants.TAG_EVENT_MEDIA_COMMENT_USER_IMG));
								cmtObj.setMediaCommentCreated(cmtJSON
										.getString(Constants.TAG_EVENT_MEDIA_COMMENT_CREATED));
								cmtList.add(cmtObj);
							}
							// Adding comments to adapter
							cmtAdapter = new CommentsAdapter(activity,
									R.layout.comment_user_row, mediaType,eventMedia, cmtList);
							// Display comments on the screen
							displayCommentsDialog(notifyUserId, mediaId,
									mediaType, eventMedia);
						} else {
							cmtAdapter = null;
							// Display comments on the screen
							displayCommentsDialog(notifyUserId, mediaId,
									mediaType, eventMedia);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					UIHelperUtil
					.showToastMessage("Something going wrong with server");
				}
			}
		});
	}

	public void triggerCommentsDialogByView(View v) {
		if (mediaItem != null) {
			if (mediaItem.getMediaCommentCount().trim().length() > 0) {
				if (mediaItem.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)) {
					// Fetching cast comments
					getCommentsList(userId,
							mediaItem.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_CAST, mediaItem);
				} else {
					// Fetching images and photos comments
					getCommentsList(userId,
							mediaItem.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_IMG, mediaItem);
				}

			} else {
				if (mediaItem.getMediaType().equalsIgnoreCase(Constants.KEYs[3])) {
					// Fetching cast comments
					displayCommentsDialog(userId,
							mediaItem.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_CAST, mediaItem);
				} else {
					// Fetching images and photos comments
					displayCommentsDialog(userId,
							mediaItem.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_IMG, mediaItem);
				}
			}
		} else {
			UIHelperUtil.showToastMessage("Unable to download!....");
		}
	}

	//Display user profile
	public void displayUserProfile(String userId){
		if (userId != null) {
			UserProfileFragment activeFragment = new UserProfileFragment();
			Bundle arg = new Bundle();
			arg.putString(Constants.dataReceive, userId);
			activeFragment.setArguments(arg);
			if(activity!=null){
				//stack & displayed on the Main screen
				((ShowTagEventActivity) activity).push(activeFragment);
			}
		}	
	}

	//Display event media comments
	public void displayCommentsDialog(final String notifyUserId,
			final String mediaId, final String mediaType,
			final ProfileMediaInfo eventMedia) {

		// Prepare comments dialog layout
		LayoutInflater inflater = LayoutInflater.from(activity);
		View dialogView = inflater.inflate(R.layout.comment_layout, null);

		//Prepare display dialog window
		altDlg = new Dialog(activity);
		altDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		altDlg.setContentView(dialogView);

		int width = altDlg.getWindow().getWindowManager().getDefaultDisplay()
				.getWidth() - 10;
		int height = altDlg.getWindow().getWindowManager().getDefaultDisplay()
				.getHeight() - 10;

		altDlg.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		// bldr.setView(dialogView);
		commentListStatus = (TextView) dialogView
				.findViewById(R.id.commentListStatus);
		commentUsersListView = (ListView) dialogView
				.findViewById(R.id.commentUsersListView);
		ImageView cmtLayoutClose = (ImageView) dialogView
				.findViewById(R.id.commentsCloseBtn);

		if (cmtAdapter != null) {
			commentListStatus.setVisibility(View.GONE);
			commentUsersListView.setVisibility(View.VISIBLE);
			commentUsersListView.setAdapter(cmtAdapter);
		} else {
			commentListStatus.setVisibility(View.VISIBLE);
			commentUsersListView.setVisibility(View.GONE);
		}
		commentEditText = (EditText) dialogView
				.findViewById(R.id.commentInputEd);

		//Display commented user profile
		commentUsersListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				altDlg.dismiss();
				Comments comment =
						((CommentsAdapter.StatesViewHolder) v.getTag()).comment;
				String userId = comment.getMediaCommentUserId();
				displayUserProfile(userId);
			}
		});

		// Adds a new comment to the event-media
		commentPostBtn = (ImageView) dialogView
				.findViewById(R.id.commentPostBtn);
		commentPostBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Post the comment to the server
				if (commentEditText.getText().toString().trim().length() > 0) {
					PostComment(notifyUserId, mediaId, mediaType, commentEditText
							.getText().toString().trim(), eventMedia);
					// Hide the key board
					// InputMethodManager KeyboardMgr = (InputMethodManager)
					// context.getSystemService(Context.INPUT_METHOD_SERVICE);
					// KeyboardMgr.hideSoftInputFromWindow(commentEditText.getWindowToken(),
					// 0);

					// Clear input text
					commentEditText.setText("");
				}
			}
		});

		// Closes the dialog when press close button
		cmtLayoutClose = (ImageView) dialogView
				.findViewById(R.id.commentsCloseBtn);
		cmtLayoutClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getTag() != null) {
					Dialog altDlg = (Dialog) v.getTag();
					altDlg.dismiss();
				}
			}
		});

		altDlg.show();
		//Start updating UI using handler
		startUpdateUI();

		cmtLayoutClose.setTag(altDlg);
	}

	/******************** SERVER COMMUNICATION STARTS ***********************/


	/* Method: updateEventCheerStatus Des: Update the cheer status of an event
	 * media Input: userID: logged user id mediaId : present cheering media
	 * mediaType : Cheered to which media cheerStatus: Cheering or un-cheering
	 * status*/

	public void updateEventMediaCheerStatus(String creatorId, String mediaId,
			String mediaType, final String cheerStatus
			) {
		final String url = Constants.common_url
				+ MySportsApp.getAppContext().getString(R.string.update_event_media_cheer);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_USER_ID, userId));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_NOTIFY_USER_ID, creatorId));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_MEDIA_ID, mediaId));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_MEDIA_TYPE, mediaType));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_STATUS, cheerStatus));
		if(mediaItem.getMediaType().equalsIgnoreCase(Constants.TAG_MEDIA_TYPE_EVENT)){
			nameValuePairs.add(new BasicNameValuePair(
					Constants.TAG_CATEGORY, Constants.TAG_CATEGORY_EVENT));
		}else{
			nameValuePairs.add(new BasicNameValuePair(
					Constants.TAG_CATEGORY, Constants.TAG_CATEGORY_PROFILE));
		}
		
		nameValuePairs.add(new BasicNameValuePair(
				Constants.TAG_CATEGORY_TYPE, mediaItem.getMediaType()));
		JsonParser.callBackgroundService(url, nameValuePairs,
				new JsonServiceListener() {
			@Override
			public void parseJsonResponse(String jsonResponse) {
				if (jsonResponse != null) {
					Log.v("", "URL: " + url
							+ " event details RESPONSE: "
							+ jsonResponse);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject
								.getJSONObject(Constants.TAG_RESPONSE);
						String responseStr = resObj
								.getString(Constants.TAG_RESPONSE_INFO);
						List<TeamInfo> myteamList = new ArrayList<TeamInfo>();
						if (responseStr != null
								&& responseStr
								.equalsIgnoreCase(Constants.TAG_SUCCESS)) {
							if (cheerStatus.equalsIgnoreCase("true")) {
								mediaItem.incMediaLikes();
							} else {
								mediaItem.decMediaLikes();
							}
							mediaItem.setIsUserLiked(cheerStatus);

							// Change the cheer icon status
							setAllTheFields();
						} else {
//							UIHelperUtil.showToastMessage("Server error!...");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					UIHelperUtil
					.showToastMessage("Something going wrong with server");
				}
			}
		});
	}	

	private void PostComment(final String notifyUserId, final String mediaId,
			final String mediaType, final String cmtText,
			final ProfileMediaInfo eventMedia) {
		final String url = Constants.common_url
				+ getString(R.string.add_event_media_comment);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(
				Constants.TAG_EVENT_MEDIA_COMMENT_USER_ID, SharedPreferencesUtils.getUserId()));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_MEDIA_NOTIFY_USER_ID, notifyUserId));
		nameValuePairs.add(new BasicNameValuePair(Constants.TAG_EVENT_MEDIA_ID,
				mediaId));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.TAG_EVENT_MEDIA_TYPE, mediaType));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.TAG_EVENT_MEDIA_COMMENT_TEXT, cmtText));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.TAG_CATEGORY, Constants.TAG_CATEGORY_PROFILE));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.TAG_CATEGORY_TYPE, eventMedia.getMediaType()));
		Log.d("","Comment url:"+url+"?"+"user_id=330&pn_user_id="+notifyUserId+"media_id="+mediaId+"media_type="+mediaType+"comment_content="+cmtText+"category=PROFILE"+"category_type="+eventMedia.getMediaType());
		JsonParser.callBackgroundService(url, nameValuePairs,
				new JsonServiceListener() {
			@Override
			public void parseJsonResponse(String jsonResponse) {
				try {
					JSONObject jsonObject = new JSONObject(jsonResponse);
					if (jsonResponse != null) {
						JSONObject resObj = jsonObject
								.getJSONObject(Constants.TAG_RESPONSE);
						String responseStr = resObj
								.getString(Constants.TAG_RESPONSE_INFO);
						if (responseStr != null
								&& responseStr
								.equalsIgnoreCase(Constants.TAG_SUCCESS)) {
							// Update posted comment on the screen.
							Comments cmtObj = new Comments();
							cmtObj.setMediaCommentFirstName(SharedPreferencesUtils
									.getUserName());
							cmtObj.setMediaCommentProfileImage(SharedPreferencesUtils
									.getUserProfilePicPath());
							cmtObj.setMediaCommentId(resObj
									.getString(Constants.TAG_EVENT_MEDIA_COMMENT_ID));
							cmtObj.setMediaCommentProfileImage(SharedPreferencesUtils.getUserProfilePicPath());
							cmtObj.setMediaCommentUserId(SharedPreferencesUtils.getUserId());
							cmtObj.setMediaCommentText(cmtText);
							if (cmtAdapter == null) {
								List<Comments> cmtList = new ArrayList<Comments>();
								cmtList.add(cmtObj);
								cmtAdapter = new CommentsAdapter(
										activity,
										R.layout.comment_user_row, mediaType,eventMedia,
										cmtList);
								commentListStatus
								.setVisibility(View.GONE);
								commentUsersListView
								.setVisibility(View.VISIBLE);
								commentUsersListView
								.setAdapter(cmtAdapter);
							} else {
								cmtAdapter.add(cmtObj);
							}
							eventMedia.incCommentsCnt();
							cmtAdapter.notifyDataSetChanged();
							setAllTheFields();
							commentUsersListView
							.setSelection(cmtAdapter.getCount() - 1);
						} else {
//							UIHelperUtil.showToastMessage("Server error!...");
						}
					} else {
						UIHelperUtil
						.showToastMessage("Something going wrong with server");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	/******************** SERVER COMMUNICATION ENDS ************************//*

	 *//**
	 * Dialgo to show share dialog
	 */
	private void showShareDialog(final String desc, final String link,final String eventPicUrl,View v) {
		if (mediaItem != null) {
			final Dialog dialog = new Dialog(activity);
			//		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_share);
			dialog.setTitle("Share event media via");
			dialog.setCancelable(true);
			Button btnFb = (Button) dialog.findViewById(R.id.fb_btn);
			btnFb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// UIHelper.showToastMessage(DetailsReviewsActivity.this,
					// "you click share button");
					if (Utils.chkStatus()) {
						/*if (SocialNetworkingUtils.isFacebookExist(activity)) {
							SocialNetworkingUtils.onClickPostStatusUpdate(activity,desc, link, eventPicUrl);
							Log.d("Tag", "Success! " + "existed");
						} else {*/
							Session session = Session.getActiveSession();
							if (session != null && session.isOpened()) {
								Log.d("Tag", "Success! " + "if");
								SocialNetworkingUtils.publishFeedDialog(desc, link, eventPicUrl, activity);
							} else {
								Log.d("Tag", "Session! " + "else");
								Session.openActiveSession(activity, true,
										statusCallback);
								SocialNetworkingUtils.isFirstTimeCalled = true;
								Log.d("", "isfirsttimecalled"
										+ SocialNetworkingUtils.isFirstTimeCalled);
							}
//						}
					} else {
						UIHelperUtil.showToastMessage(activity
								.getString(R.string.toast_no_network));
					}
					dialog.dismiss();
				}
			});

			Button btnReport = (Button) dialog.findViewById(R.id.report_btn);
			btnReport.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//				UIHelperUtil.showToastMessage("Yet to implement");
					String absType = "";
					Log.d("","event media:"+mediaItem.getMediaCategory());

					if(mediaItem.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)){
						absType = "ABS_USR_PHOTO";
					}else if(mediaItem.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)){
						absType = "ABS_USR_VIDEO";
					}else if(mediaItem.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)){
						absType = "ABS_EVENT_CAST";
					}

					Utils.reportAbuseService(activity,dialog,absType, mediaItem.getMediaId());
				}
			});

			Button btnTwitter = (Button) dialog.findViewById(R.id.twitter_btn);
			btnTwitter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					SharedPreferences mSharedPref = activity.getSharedPreferences(
							"Android_Twitter_Preferences", Context.MODE_PRIVATE);

					if (mSharedPref.getString(Constants.KEY_TWITTER_ACCESS_TOKEN,
							null) != null) {
						/*SocialNetworkingUtils.postingtoTwitter(v,
							(Activity) ShowTagEventActivity.this, desc);*/
						HelperMethods.postToTwitterWithImage(activity,activity, eventPicUrl, desc, new TwitterCallback() {

							@Override
							public void onFinsihed(Boolean response) {
								Toast.makeText(activity, "posted succeded", Toast.LENGTH_SHORT).show();
							}
						});
					} else {
						// Toast.makeText(activity, "session is null",
						// Toast.LENGTH_SHORT).show();
						SocialNetworkingUtils.signinTwitter(v,
								SocialNetworkingUtils.mTwitter, desc,eventPicUrl,
								activity);
					}
					dialog.dismiss();
				}
			});
			Button btnInstagram = (Button) dialog.findViewById(R.id.instagram_btn);
			btnInstagram.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = activity.getPackageManager()
							.getLaunchIntentForPackage("com.instagram.android");
					Bitmap bitmap = BitmapFactory.decodeResource(
							activity.getResources(), R.drawable.default_event_pic);

					String root = Environment.getExternalStorageDirectory()
							.toString();
					File myDir = new File(root + "/event_images");
					myDir.mkdirs();
					Random generator = new Random();
					int n = 10000;
					n = generator.nextInt(n);
					String fname = "image-" + n + ".jpg";
					File file = new File(myDir, fname);
					if (file.exists())
						file.delete();
					try {
						FileOutputStream out = new FileOutputStream(file);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
						out.flush();
						out.close();

					} catch (Exception e) {
						e.printStackTrace();
					}
					File imageFile2 = new File(Environment
							.getExternalStorageDirectory(), "/event_images/"
									+ "image-" + n + ".jpg");
					String path = imageFile2.getAbsolutePath();
					if (intent != null) {
						Intent shareIntent = new Intent();
						shareIntent.setAction(Intent.ACTION_SEND);
						shareIntent.setPackage("com.instagram.android");
						try {
							shareIntent.putExtra(Intent.EXTRA_STREAM, Uri
									.parse(MediaStore.Images.Media.insertImage(
											activity.getContentResolver(), path, "",
											"")));
							shareIntent.putExtra(Intent.EXTRA_TEXT, desc);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						shareIntent.setType("image/jpeg");

						activity.startActivity(shareIntent);
					} else {
						// bring user to the market to download the app.
						// or let them choose an app?
						intent = new Intent(Intent.ACTION_VIEW);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setData(Uri.parse("market://details?id="
								+ "com.instagram.android"));
						activity.startActivity(intent);
					}
					dialog.dismiss();
				}
			});

			dialog.show();
		}
	}

	private com.facebook.Session.StatusCallback statusCallback = new StatusCallback() {

		@Override
		public void call(com.facebook.Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				session.removeCallback(statusCallback);
				Log.i("", "Session Facebook is opened: ");

			} else {
				Log.i("", "Session Facebook is closed");
			}
		}
	};

	/*********************************Server communication starts **************************************/
	//service to get event media information
	private void serviceDisplayMediaFromNotification(final Bundle args) {
		final String url = Constants.common_url + MySportsApp.getAppContext().getResources().getString(R.string.get_media_detail);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user_id", SharedPreferencesUtils.getUserId()));
		nameValuePairs.add(new BasicNameValuePair("media_id", args.getString(Constants.TAG_DISP_NOTIFICATION_MEDIA_ID)));
		nameValuePairs.add(new BasicNameValuePair("media_type", args.getString(Constants.TAG_DISP_NOTIFICATION_MEDIA_TYPE)));
		nameValuePairs.add(new BasicNameValuePair("resource_type", args.getString(Constants.TAG_DISP_NOTIFICATION_RESOURCE_TYPE)));
		JsonParser.callBackgroundService(url, nameValuePairs, new JsonServiceListener() {
			ProgressDialog pd;
			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(activity);
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
				if (jsonResponse!= null) {
					Log.v("", "URL: " + url +" only event media RESPONSE: " + jsonResponse);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject.getJSONObject(Constants.TAG_RESPONSE);
						String responseStr = resObj.getString(Constants.TAG_RESPONSE_INFO);
						List<TeamInfo> myteamList = new ArrayList<TeamInfo>();
						if (responseStr!=null && responseStr.equalsIgnoreCase(Constants.TAG_SUCCESS)){
							try{
								//Fetching liked users list
								JSONObject notificationMeidaObj = resObj.getJSONObject("user_pfofile_all");

								mediaItem = new ProfileMediaInfo();mediaItem.getMediaType();
								mediaItem.setMediaType(args.getString(Constants.TAG_DISP_NOTIFICATION_MEDIA_TYPE));//notificationMeidaObj.getString(Constants.TAG_MEDIA_TYPE)
								mediaItem.setMediaEventId(notificationMeidaObj.getString(Constants.TAG_MEDIA_EVENT_ID));
								mediaItem.setMediaIsStubhubEvent(notificationMeidaObj.getString(Constants.TAG_MEDIA_EVENT_IS_STUBHUB));
								mediaItem.setMediaEventTitle(notificationMeidaObj.getString(Constants.TAG_MEDIA_EVENT_TITLE));
								mediaItem.setMediaEventType(notificationMeidaObj.getString(Constants.TAG_MEDIA_EVENT_TYPE));
								mediaItem.setMediaSportName(notificationMeidaObj.getString(Constants.TAG_MEDIA_SPORT_NAME));
								mediaItem.setMediaLocation(notificationMeidaObj.getString(Constants.TAG_MEDIA_LOCATION));
								mediaItem.setMediaCaption(notificationMeidaObj.getString(Constants.TAG_MEDIA_CAPTION));
								mediaItem.setMediaId(notificationMeidaObj.getString(Constants.TAG_MEDIA_ID));
								mediaItem.setMediaUrl(notificationMeidaObj.getString(Constants.TAG_MEDIA_URL));
								mediaItem.setMediaThumbUrl(notificationMeidaObj.getString(Constants.TAG_MEDIA_THUMB_URL));
								if (notificationMeidaObj.getString(Constants.TAG_MEDIA_IS_USER_LIKED).equalsIgnoreCase("1")) {
									mediaItem.setIsUserLiked("true");
								}else{
									mediaItem.setIsUserLiked("false");
								}
								mediaItem.setMediaLikeCount(notificationMeidaObj.getString(Constants.TAG_MEDIA_LIKE_COUNT));
								mediaItem.setMediaCommentCount(notificationMeidaObj.getString(Constants.TAG_MEDIA_COMMENT_COUNT));

								//determines info is of cast type
								mediaItem.setMediaCategory(notificationMeidaObj.getString(Constants.TAG_MEDIA_CATEGORY));

								setAllTheFields();
							}catch(JSONException ex){
								//When no user exist
							}
						}else if(responseStr!=null && responseStr.equalsIgnoreCase(Constants.TAG_FAILURE)){
							UIHelperUtil.showToastMessage("No updates found");
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
/*********************************Server communication ends **************************************/}
