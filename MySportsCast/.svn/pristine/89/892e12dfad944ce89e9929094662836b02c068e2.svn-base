package com.mysportsshare.mysportscast.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.londatiga.android.twitter.Twitter;
import net.londatiga.android.twitter.TwitterRequest;
import net.londatiga.android.twitter.TwitterUser;
import net.londatiga.android.twitter.oauth.OauthAccessToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import br.com.dina.oauth.instagram.InstagramApp;
import br.com.dina.oauth.instagram.InstagramApp.OAuthAuthenticationListener;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.HomeActivity;
import com.mysportsshare.mysportscast.activity.ImageFilterActivity;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.activity.VideoFilterActivity;
import com.mysportsshare.mysportscast.instagram.ApplicationData;
import com.mysportsshare.mysportscast.models.ProfileMediaInfo;
import com.mysportsshare.mysportscast.models.SearchInfo;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.HelperMethods;
//import com.example.http.multipartdemo.Config;
import com.mysportsshare.mysportscast.utils.HelperMethods.TwitterCallback;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.CustomLog;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.SocialNetworkingUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;
import com.mysportsshare.mysportscast.view.FlowLayout;


public class FilterVideoBroadCastFragment extends Fragment implements OnClickListener {

	private String videoPath;

	private ImageView backBtn;
	private TextView title;
	private ImageView settingBtn;

	private EditText photo_caption_et;

	private TextView search_people_et;

	private ImageView search_people_iv;

	private TextView search_event_et;

	private Button share_twitter_btn;

	private LoginButton share_facebook_btn;

	private Button share_by_txt_btn;

	private Button share_broadcast;

	Activity activity;

	private VideoView video_iv;
	private String eventId="";
	private String eventPic="";
	private String eventType = "";
	//sharing
	private boolean isfbClick;
	private boolean istwitClick;
	private boolean isinstaClick;
	private boolean isClicked;
	private ImageView imgCheckShareTwit;
	private ImageView imgCheckShareFb;
	private ImageView imgCheckShareBytxt;
	private ImageView search_event_iv;
	private LinearLayout llytSearchEvents;
	private List<SearchInfo> listSearchInfo;
	private FlowLayout llytmultiPeple;
	private LinearLayout llytSearchPeople;
	private String taggedUsers;
	private boolean isFromHome = false;
	private List<SearchInfo> selectedInvitesUsersList = new ArrayList<SearchInfo>();
	private String invitedUsers = "";
	private LinearLayout llytmultiEvent;
	private String shareString = "";
	private ProfileMediaInfo profileEventMedia;
	
	static FilterVideoBroadCastFragment f = new FilterVideoBroadCastFragment();

	public static Fragment getInstance(Context context) {
		if (f == null) {
			f = new FilterVideoBroadCastFragment();
		}
		return f;
	}

	public static Fragment getnewInstance(Context context) {
		f = new FilterVideoBroadCastFragment();
		return f;
	}

	private ImageView media_video_play;

	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View layoutView = inflater.inflate(R.layout.fragment_addvideo, container,false);

		init(layoutView);
		Bundle args = getArguments();
		Log.d("","isfromhome"+isFromHome);
		if(args!=null){
			
			if(args.getSerializable(Constants.KEY_PROFILE_EVENT_MEDIA) != null){
				
				profileEventMedia = (ProfileMediaInfo) args.getSerializable(Constants.KEY_PROFILE_EVENT_MEDIA);
				if(!TextUtils.isEmpty(profileEventMedia.getMediaUrl())){
					photo_caption_et.setText(profileEventMedia.getMediaCaption());
					search_event_et.setText(profileEventMedia.getMediaEventTitle());
					Uri uri = Uri.parse(profileEventMedia.getMediaUrl());
					video_iv.setVideoURI(uri);
				}
				
			}else{
				videoPath = args.getString(Constants.dataReceive);
				if(args.getString(Constants.KEY_EVENT_TYPE) != null && args.getString(Constants.KEY_EVENT_ID) != null){
					eventType = args.getString(Constants.KEY_EVENT_TYPE);
					eventId = args.getString(Constants.KEY_EVENT_ID);
					//				search_event_et.setText(eventType);
					isFromHome = true;
					search_event_iv.setVisibility(View.GONE);
					Log.d("","isfromhomeeventype"+isFromHome);
				}else{
					isFromHome = false;
					search_event_iv.setVisibility(View.VISIBLE);
				}
			}

		}

		//for sharing 
		if(!Utils.chkStatus()){
			Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
			//			showCloseAppDialogue();
		}else{
			SocialNetworkingUtils.mApp.setListener(listener);
			SocialNetworkingUtils.uiHelper = new UiLifecycleHelper(activity, statusCallback);
			SocialNetworkingUtils.uiHelper.onCreate(savedInstanceState);
		}

		isfbClick = false;
		istwitClick = false;
		isinstaClick = false;

		share_facebook_btn.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		//		btnShareFb.setFragment(this);
		share_facebook_btn.setReadPermissions(Arrays.asList("email"));
		share_facebook_btn.setUserInfoChangedCallback(new UserInfoChangedCallback() {

			@Override
			public void onUserInfoFetched(GraphUser user) {
				if (user != null) {
					if (isClicked) {
						if(!Utils.chkStatus()){
							Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
						}else{
							Log.i("", "Session isClicked: " + isClicked);
							imgCheckShareFb.setImageResource(R.drawable.cb_checked);
							isfbClick = true;

						}

					} else {
						Log.i("", "Session isClicked: " + isClicked);
					}
					Log.v("", "user: " + user.getName() + " : " + user.getId());	


				} else {
					Log.i("", "Session user: " + user);
					//					Session.openActiveSession(activity, true, statusCallback);
					//					UIHelperUtil.showToastMessage("Unable to fetch login details");
				}
			}
		});

		return layoutView;
	}

	private void init(View layoutView){
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		backBtn = (ImageView) activity.findViewById(R.id.back_iv);
		settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);

		//Accessing layout fields
		video_iv = (VideoView) layoutView.findViewById(R.id.video_iv);
		llytSearchPeople = (LinearLayout) layoutView
				.findViewById(R.id.search_people_llyt);
		llytSearchPeople.setOnClickListener(this);
		llytSearchEvents = (LinearLayout) layoutView
				.findViewById(R.id.search_event_llyt);
		llytmultiPeple = (FlowLayout) layoutView
				.findViewById(R.id.search_multi_people_llyt);
		llytSearchEvents.setOnClickListener(this);
		llytmultiEvent = (LinearLayout) layoutView
				.findViewById(R.id.search_multi_event_llyt);
		media_video_play = (ImageView) layoutView.findViewById(R.id.media_video_play);

		photo_caption_et =(EditText)layoutView.findViewById(R.id.photo_caption_et);
		search_people_et = (TextView)layoutView.findViewById(R.id.search_people_tv);
		search_people_iv = (ImageView)layoutView.findViewById(R.id.search_people_iv);
		search_event_iv = (ImageView)layoutView.findViewById(R.id.search_event_iv);
		search_event_et = (TextView)layoutView.findViewById(R.id.search_event_tv);
		share_twitter_btn = (Button)layoutView.findViewById(R.id.share_twitter_btn);
		share_twitter_btn.setOnClickListener(this);
		share_facebook_btn = (LoginButton)layoutView.findViewById(R.id.share_facebook_btn);
		share_facebook_btn.setOnClickListener(this);
		share_by_txt_btn =(Button)layoutView.findViewById(R.id.share_by_txt_btn);
		share_by_txt_btn.setOnClickListener(this);
		share_broadcast = (Button)layoutView.findViewById(R.id.share_broadcast);
		share_broadcast.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		settingBtn.setVisibility(View.GONE);
		imgCheckShareTwit = (ImageView) layoutView.findViewById(R.id.twitter_iv);
		imgCheckShareFb = (ImageView) layoutView.findViewById(R.id.fb_iv);
		imgCheckShareBytxt = (ImageView) layoutView.findViewById(R.id.by_txt_iv);
		//sharing 
		SocialNetworkingUtils.mTwitter = new Twitter(activity, HomeActivity.CONSUMER_KEY, HomeActivity.CONSUMER_SECRET, HomeActivity.CALLBACK_URL);
		SocialNetworkingUtils.mApp = new InstagramApp(activity, ApplicationData.CLIENT_ID,
				ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);

		//Displaying limited fields for video broadcast
		video_iv.setVisibility(View.VISIBLE);
		media_video_play.setVisibility(View.VISIBLE);
		media_video_play.setOnClickListener(this);
		//		llytSearchPeople.setVisibility(View.GONE);
	}	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		title.setText(Constants.UI_UPLOAD_VIDEO);

		//Load image
		if(profileEventMedia == null){
			if(TextUtils.isEmpty(videoPath)){
				Utils.showAlertDialog(activity, "Alert", "Unable to process selected image");
			}else{
				//As converted image is stored in local directory
				Log.d(Constants.logUrl,videoPath);

				video_iv.setVideoPath(videoPath);
				//			BitmapUtils.setImages("file:///"+videoPath, photo_iv, R.drawable.profile_pic_dummy); 
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.share_broadcast:
			//upload photo to server

			/* No need of validations
			 //TODO: validation
			if(!photo_caption_et.getText().toString().matches("")){
				//No need of tag events & people for uploading video
				uploadVideo(v);
				if(listSearchInfo != null && listSearchInfo.size() > 0){
					if(!eventId.matches("")){
						uploadVideo(v);

					}else{
						UIHelperUtil.showToastMessage("Please tag a event");
					}
				}else{
					UIHelperUtil.showToastMessage("Please tag people");
				}
			}else{
				UIHelperUtil.showToastMessage("Please write caption");
			}*/
			uploadVideo(v);
			break;

		case R.id.share_twitter_btn:
			Log.d("","twitter");
			if(SocialNetworkingUtils.mTwitter != null){
				if(SocialNetworkingUtils.mTwitter.getAccessToken() != null){
					if(!istwitClick){
						imgCheckShareTwit.setImageResource(R.drawable.cb_checked);
						istwitClick = true;
					}else{
						imgCheckShareTwit.setImageResource(R.drawable.cb_unchecked);
						istwitClick = false;
					}

				}else{
					//					UIHelperUtil.showToastMessage("Yet to implement");
					signinTwitter();
				}
			}


			break;
		case R.id.search_people_llyt:
			if(getActivity()!=null){
				SearchCastPeopleFragment fragmentPeople = new SearchCastPeopleFragment();
				Bundle bundle = new Bundle();
				bundle.putString("isfromHome", "video");
				if (!TextUtils.isEmpty(invitedUsers)) {
					bundle.putParcelableArrayList("selected_users", (ArrayList<? extends Parcelable>) selectedInvitesUsersList);
				}
				fragmentPeople.setArguments(bundle);
				/*FragmentManager fragmentManagerPeople = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransactionPeople = fragmentManagerPeople
						.beginTransaction();
				fragmentTransactionPeople.replace(R.id.home_frame, fragmentPeople);
				fragmentTransactionPeople.addToBackStack("searchcastpeople");
				fragmentTransactionPeople.commit();*/
				if(activity instanceof VideoFilterActivity){
					((VideoFilterActivity)activity).push(fragmentPeople);
				}else if(activity instanceof MainActivity){
					((MainActivity) activity).pushFragments(
							((MainActivity) activity).getmCurrentTab(), fragmentPeople,
							false, true);
				}
			}
			break;

		case R.id.share_facebook_btn:

			Session session = Session.getActiveSession();
			if (session != null && session.isOpened()) {
				if(!isfbClick){
					imgCheckShareFb.setImageResource(R.drawable.cb_checked);
					isfbClick = true;
				}else{
					imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
					isfbClick = false;
				}
			}else{
				if(!Utils.chkStatus()){
					Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
				}else{
					isClicked = true;
					imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
				}
			}

			break;

		case R.id.share_by_txt_btn:
			Log.d("","instagram");
			if (SocialNetworkingUtils.mApp != null	&& SocialNetworkingUtils.mApp.hasAccessToken()) {
				if(!isinstaClick){
					imgCheckShareBytxt.setImageResource(R.drawable.cb_checked);
					isinstaClick = true;
				}else{
					imgCheckShareBytxt.setImageResource(R.drawable.cb_unchecked);
					isinstaClick = false;
				}

			}else{
				if(SocialNetworkingUtils.mApp.hasAccessToken()){
					SocialNetworkingUtils.mApp.resetAccessToken();
					imgCheckShareBytxt.setImageResource(R.drawable.cb_unchecked);
					//					imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
					//					imgCheckShareTwit.setImageResource(R.drawable.cb_unchecked);
				}else{

					if(!Utils.chkStatus()){
						Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
					}else{
						if(SocialNetworkingUtils.mApp!=null){
							if (SocialNetworkingUtils.mApp.hasAccessToken()) {
								SocialNetworkingUtils.mApp.resetAccessToken();
								//								imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
								imgCheckShareBytxt.setImageResource(R.drawable.cb_unchecked);
								//								imgCheckShareTwit.setImageResource(R.drawable.cb_unchecked);

							} else {
								SocialNetworkingUtils.mApp.authorize();
							}
						}else{
							//						UIHelperUtil.showToastMessage("Unable to fetching credentials, Please try again");
						}
					}
				}
			}
			break;
		case R.id.search_event_llyt:
			Log.d("",""+isFromHome);
			if(getActivity()!=null){
				if(!isFromHome){
					SearchCastEvents1 fragment = new SearchCastEvents1();
					Bundle bund = new Bundle();
					bund.putString("isfromHome", "video");
					fragment.setArguments(bund);
					/*FragmentManager fragmentManager = getActivity()
							.getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(R.id.home_frame, fragment);
					fragmentTransaction.addToBackStack("searchcastevent");
					fragmentTransaction.commit();*/
					if(activity instanceof VideoFilterActivity){
						((VideoFilterActivity)activity).push(fragment);
					}else if(activity instanceof MainActivity){
						((MainActivity) activity).pushFragments(
								((MainActivity) activity).getmCurrentTab(), fragment,
								false, true);
					}
				}
			}
			break;
		case R.id.media_video_play:
			triggerPlayVideo(v);
			break;
		case R.id.back_iv:
			activity.onBackPressed();
			break;
		default:
			break;
		}
	}
	private void uploadVideo(View v) {
		if(!Utils.chkStatus()){
			Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
			//			showCloseAppDialogue();
		}else{
			serviceToUploadVideo(v);
		}
	}

	private void triggerPlayVideo(View v) {
		VideosPlayFragment activeFragment = new VideosPlayFragment();
		Bundle args10 = new Bundle();
		args10.putString(Constants.userProf_userID,SharedPreferencesUtils.getUserId());
		args10.putString(Constants.userProf_userName,SharedPreferencesUtils.getUserName());
		args10.putString(Constants.userProf_videoPath,videoPath);
		args10.putString(Constants.userProf_caption,"");
		activeFragment.setArguments(args10);

		/*FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.home_frame, activeFragment);
		ft.addToBackStack(null);
		ft.commit();*/
		if(activity instanceof VideoFilterActivity){
			((VideoFilterActivity)activity).push(activeFragment);
			((VideoFilterActivity)activity).setVideoViewFragementOpenedFlag();
		}else if(activity instanceof MainActivity){
			((MainActivity)activity).pushFragments(((MainActivity)activity).getmCurrentTab(), activeFragment, false,true);
//			((MainActivity)activity).setVideoViewFragementOpenedFlag();
		}
		
	}

	/*private String getBase64Image(){
		if(! TextUtils.isEmpty(videoPath)){
			if(TextUtils.isEmpty(base64Image)){
				FileInputStream tmpInputStream = null;
				try {

					Bitmap bitmap = ((BitmapDrawable) photo_iv.getDrawable()).getBitmap();
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
					byte[] byteArray = byteArrayOutputStream.toByteArray();
					base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
					return base64Image;
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					try {
						if(tmpInputStream !=null){
							tmpInputStream.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}		
			}
			else{
				return base64Image;
			}
		}
		return "";
	}*/

	/****************************------SERVER INTERACTION STARTS-----****************************/
	private void serviceToUploadVideo(final View view){
		String url = Constants.common_url + getString(R.string.add_photo);
		Log.d("","videoPath:"+videoPath);
		String[] params = {url, videoPath};
		//Upload video file to server.
		new AsyncTask<String, Void, String>() {
			ProgressDialog pd;
			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(activity);
				pd.show();
				pd.setMessage("Please wait.  Uploading video...");
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
			}
			@Override
			protected String doInBackground(String... params) {
				return uploadFile(params[1], params[0]);
			}

			@Override
			protected void onPostExecute(String jsonResponse) {
				//				UIHelperUtil.showToastMessage(jsonResponse);
				/*if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}*/
				Log.v(Constants.logUrl, " Upload video RESPONSE: "
						+ jsonResponse);
				if (jsonResponse != null) {
					JSONObject jsonObject;
					try {
						if (jsonResponse != null) {
							jsonObject = new JSONObject(jsonResponse);
							JSONObject resObj = jsonObject
									.getJSONObject("Response");
							String responseStr = resObj
									.getString("ResponseInfo");
							if (responseStr != null
									&& responseStr
									.equalsIgnoreCase("SUCCESS")) {
								String mediaUrl = resObj.getString("media_url");
								Log.v(Constants.logUrl, "media url:"+mediaUrl);
								// Prompt success message to the user
								UIHelperUtil.showToastMessage("Successfully uploaded");
								if(TextUtils.isEmpty(shareString)){
									shareString = "Add video "+mediaUrl+" to his profile" + ". For more details please visit http://www.mysportsshare.com";
								}else{
									shareString  = "Add video "+mediaUrl+" to an event "+ eventType+ ". For more details please visit http://www.mysportsshare.com";
								}
								if (istwitClick) {
									sharingToTwitter(view,mediaUrl,pd);
								} else if (isfbClick) {
									Log.d("", "if facebook");
									Session session = Session
											.getActiveSession();
									if (session != null
											&& session.isOpened()) {
										Log.d("", "koti facebook");
										publishFeedDialog(shareString, "",
												mediaUrl, activity,pd);
									}
								} else if (isinstaClick) {
									sharingToInstagram(mediaUrl,pd);
								} else {
									//									activity.finish();
									exitActivity(pd);
									/*search_people_et.setText("");
									search_event_et.setText("");
									eventId = "";
									if(activity!=null){
										activity.finish();
									}*/
								}

								//Close the screen
								/*if(activity!=null){
									activity.finish();
								}*/
							}else{
								UIHelperUtil.showToastMessage(getString(R.string.service_toast));
							}
						}
					}catch(Exception ex){

					}
				}
			};
		}.execute(params);		
	}

	//Uploading video file using multipart/form-data
	private String uploadFile(String sourceUri, String ServerUrl) {
		String responseStr=""; 

		taggedUsers = "";
		if(listSearchInfo!=null){
			for(int i = 0; i < listSearchInfo.size(); i++){
				if( i == listSearchInfo.size() - 1){
					taggedUsers = taggedUsers+listSearchInfo.get(i).getID();
				}else{
					taggedUsers = taggedUsers+listSearchInfo.get(i).getID()+",";
				}
			}
		}

		long totalSize = 0;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(ServerUrl);

			MultipartEntity entity = new MultipartEntity();

			entity.addPart("user_id",
					new StringBody(SharedPreferencesUtils.getUserId()));
			entity.addPart("event_id", new StringBody(eventId));
			if(TextUtils.isEmpty(photo_caption_et.getText())){
				entity.addPart("post_content", new StringBody(""));
			}else{
				entity.addPart("post_content", new StringBody(photo_caption_et.getText().toString().trim()));
			}
			entity.addPart("media_type", new StringBody(Constants.video_media_type));
			entity.addPart("tagged_users", new StringBody(taggedUsers));
			//			entity.addPart("media_file",new StringBody(""));
			//used for notifications
			if(TextUtils.isEmpty(eventId)){
				/*entity.addPart(
						Constants.TAG_CATEGORY, new StringBody("PROFILE"));*/
				entity.addPart(
						Constants.TAG_CATEGORY, new StringBody("profile"));
			}else{
				/*entity.addPart(
						Constants.TAG_CATEGORY, new StringBody("EVENT"));*/
				entity.addPart(
						Constants.TAG_CATEGORY, new StringBody("event"));
			}
			File sourceFile = new File(sourceUri);

			// Adding file data to http body
			entity.addPart("media_file", new FileBody(sourceFile)); 

			totalSize = entity.getContentLength(); 
			httppost.setEntity(entity);

			// Making server call
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity r_entity = response.getEntity();

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				// Server response
				responseStr = EntityUtils.toString(r_entity);
			} else {
				responseStr = "Error occurred! Http Status Code: "
						+ statusCode;
			}
		}catch (Exception ex)
		{
			Log.w("----catch (Exception ex) ---", " ");
			ex.printStackTrace();
			// Exception handling
		}
		return responseStr;
	}

	/****************************------SERVER INTERACTION ENDS-----****************************/	


	/**
	 * for sharing 
	 */
	private com.facebook.Session.StatusCallback statusCallback = new StatusCallback() {

		@Override
		public void call(com.facebook.Session session, SessionState state, Exception exception) {
			if (state.isOpened()) {
				session.removeCallback(statusCallback);
				Log.i("", "Session Facebook is opened: ");

			} else {
				Log.i("", "Session Facebook is closed");

			}
		}
	};

	OAuthAuthenticationListener listener = new OAuthAuthenticationListener() {

		@Override
		public void onSuccess() {
			if(!Utils.chkStatus()){
				Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
			}else{
				/*Session session = Session.getActiveSession();
				if (session != null && session.isOpened()) {
					session.closeAndClearTokenInformation();
					SocialNetworkingUtils.uiHelper.onDestroy();
				}
				SocialNetworkingUtils.mTwitter.clearSession();*/
				//				imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
				imgCheckShareBytxt.setImageResource(R.drawable.cb_checked);
				//				imgCheckShareTwit.setImageResource(R.drawable.cb_unchecked);	

			}
		}

		@Override
		public void onFail(String error) {
			Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void onResume() {
		super.onResume();

		Log.d("","onresume"+eventType+" "+eventId);
		//		search_event_et.setText(eventType);

		if (listSearchInfo != null && listSearchInfo.size() > 0) {

			if (llytmultiPeple.getChildCount() > 0) {
				llytmultiPeple.removeAllViews();
			}
			for (SearchInfo searchInfo : listSearchInfo) {
				addInvitedItem(searchInfo);
			}
			setSearchList(listSearchInfo);
			getInviteUserInStringFormat(listSearchInfo);
		}

		setDynamicView();
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened() && isfbClick) {
			imgCheckShareFb.setImageResource(R.drawable.cb_checked);
			Log.d("","facebook");
		}else{
			imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
		}

		if(SocialNetworkingUtils.mTwitter != null){
			if(SocialNetworkingUtils.mTwitter.getAccessToken() != null && istwitClick){
				imgCheckShareTwit.setImageResource(R.drawable.cb_checked);
				Log.d("","twitter");
			}else{
				imgCheckShareTwit.setImageResource(R.drawable.cb_unchecked);
			}
		}

		if(SocialNetworkingUtils.mApp != null){
			if(SocialNetworkingUtils.mApp.hasAccessToken() && isinstaClick){
				imgCheckShareBytxt.setImageResource(R.drawable.cb_checked);
				Log.d("","instagram");
			}else{
				imgCheckShareBytxt.setImageResource(R.drawable.cb_unchecked);
			}
		}

		if(SocialNetworkingUtils.uiHelper != null){
			SocialNetworkingUtils.uiHelper.onResume();
		}
	}

	private void setDynamicView(){

		if (!eventType.matches("")) {
			search_event_et.setVisibility(View.GONE);
			LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(5, 5, 5, 5);
			llytmultiEvent.removeAllViews();
			TextView tvName = new TextView(activity);
			tvName.setText(eventType);
			tvName.setTextSize(getResources().getDimension(
					R.dimen.very_small_people_text_size));
			tvName.setGravity(Gravity.CENTER);
//			tvName.setPadding(3, 3, 3, 3);
			tvName.setLayoutParams(layoutParams);

			ImageView ivDel = new ImageView(activity);
			ivDel.setImageResource(R.drawable.delete);
			ivDel.setLayoutParams(new LayoutParams(40, 40));
			ivDel.setPadding(3, 3, 3, 3);

			final LinearLayout llyt = new LinearLayout(activity);
			llyt.setBackgroundResource(R.drawable.multi_edit_corner);
			llyt.setOrientation(LinearLayout.HORIZONTAL);
			llyt.setGravity(Gravity.CENTER);
			llyt.setLayoutParams(layoutParams);
			llyt.addView(tvName);
			if(!isFromHome){
				llyt.addView(ivDel);
			}

			llytmultiEvent.addView(llyt);

			ivDel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					llytmultiEvent.removeView(llyt);
					search_event_et.setVisibility(View.VISIBLE);
					llytmultiEvent.addView(search_event_et);

				}
			});


		}


	}

	private void setSearchList(List<SearchInfo> searchList) {
		this.selectedInvitesUsersList = searchList;
	}	

	private void getInviteUserInStringFormat(List<SearchInfo> selectedInvitesUsersList) {
		StringBuffer selectedItems = new StringBuffer();
		for (int i = 0; i < selectedInvitesUsersList.size(); i++) {
			if (i < selectedInvitesUsersList.size()-1) {
				selectedItems.append(selectedInvitesUsersList.get(i).getID()+",");	
			} else {
				selectedItems.append(selectedInvitesUsersList.get(i).getID());
			}
		}
		invitedUsers = selectedItems.toString();
		CustomLog.v("", "Selected Users: " + " : " + selectedInvitesUsersList.size()+ " : Ids: " + invitedUsers);
	}


	private void addInvitedItem(final SearchInfo searchItem) {
		FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(5, 5, 5, 5);

		int color = Color.TRANSPARENT;
		TextView selectedUserName = new TextView(activity);
		selectedUserName.setBackgroundColor(color);
		//tvName.setBackgroundResource(R.drawable.input_box);
		selectedUserName.setText(searchItem.getName() +"");
		int left, right, top, bottom;
		left = right = top = bottom = 3;
		selectedUserName.setPadding(left, top, right, bottom);
		selectedUserName.setLayoutParams(layoutParams);

		ImageView imgRemove = new ImageView(activity);
		imgRemove.setImageResource(R.drawable.delete);
		imgRemove.setLayoutParams(new LayoutParams(40, 40));
		imgRemove.setPadding(left, top, right, bottom);

		final LinearLayout selectedUserLayout = new LinearLayout(activity);
		selectedUserLayout.setBackgroundResource(R.drawable.multi_edit_corner);
		selectedUserLayout.setOrientation(LinearLayout.HORIZONTAL);
		selectedUserLayout.setGravity(Gravity.CENTER);
		selectedUserLayout.setLayoutParams(params);
		selectedUserLayout.addView(selectedUserName);
		selectedUserLayout.addView(imgRemove);


		params.rightMargin = 10;
		params.topMargin = 10;
		params.leftMargin = 5;
		params.bottomMargin = 5;
		/*if (!isSearchItemExists(searchItem)) {
			flowLayout.addView(selectedUserLayout);	
			CustomLog.i("AddItem", "Add " + searchItem.getID() + " : " + flowLayout.getChildCount() + " : " + isSearchItemExists(searchItem));
		} else {
			CustomLog.v("AddItem", "Add " + searchItem.getID() + " : " + flowLayout.getChildCount() + " : " + isSearchItemExists(searchItem));
		}*/
		llytmultiPeple.addView(selectedUserLayout);
		imgRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeSeletedItem(searchItem);
			}
		});

		/*if (selectedInvitesUsersList != null &&
				selectedInvitesUsersList.size() > 0) {
			removeSeletedItem(searchItem);
		}*/
	}

	private void signinTwitter() {
		if(SocialNetworkingUtils.mTwitter != null){
			SocialNetworkingUtils.mTwitter.signin(new Twitter.SigninListener() {				
				@Override
				public void onSuccess(OauthAccessToken accessToken, String userId, String screenName) {
					getCredentials();
				}

				@Override
				public void onError(String error) {
					showToast(error);
				}
			});
		}

	}


	private void removeSeletedItem(SearchInfo searchItem) {

		if (isSearchItemExists(searchItem)) {
			try {
				int index = getIndexItem(searchItem);
				selectedInvitesUsersList.remove(index);
				llytmultiPeple.removeView(getLastView(index));
				if(selectedInvitesUsersList.size() == 0){
					llytmultiPeple.addView(search_people_et);
				}
				CustomLog.v("Index exists", "index: "+index + " : " + selectedInvitesUsersList.size());
			} catch(Exception e) {
				e.printStackTrace();
				return;
			}

		}

	}

	private boolean isSearchItemExists(SearchInfo searchInfo) {
		boolean exists = false;
		for (SearchInfo info : selectedInvitesUsersList) {
			if (info.getID().equalsIgnoreCase(searchInfo.getID())) {
				exists = true;
			}
		}
		return exists;
	}

	private int getIndexItem(SearchInfo searchInfo) {
		int index = 0;
		for (SearchInfo info : selectedInvitesUsersList) {
			if (info.getID().equalsIgnoreCase(searchInfo.getID())) {
				index = selectedInvitesUsersList.indexOf(info);
			}
		}
		return index;
	}

	private View getLastView(int position) {
		return llytmultiPeple.getChildAt(position);
	}

	private void getCredentials() {
		final ProgressDialog progressDlg = new ProgressDialog(activity);
		//		progressDlg.setMessage("Getting credentials...");
		progressDlg.setMessage("wait...");
		progressDlg.setCancelable(false);
		progressDlg.show();
		TwitterRequest request = new TwitterRequest(SocialNetworkingUtils.mTwitter.getConsumer(), SocialNetworkingUtils.mTwitter.getAccessToken());
		request.verifyCredentials(new TwitterRequest.VerifyCredentialListener() {
			@Override
			public void onSuccess(TwitterUser user) {
				progressDlg.dismiss();
				if(user!=null){
					if(!Utils.chkStatus()){
						Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
					}else{
						/*Session session = Session.getActiveSession();
						if (session != null && session.isOpened()) {
							session.closeAndClearTokenInformation();
							SocialNetworkingUtils.uiHelper.onDestroy();
						}
						SocialNetworkingUtils.mApp.resetAccessToken();*/
						//						imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
						//						imgCheckShareBytxt.setImageResource(R.drawable.cb_unchecked);
						imgCheckShareTwit.setImageResource(R.drawable.cb_checked);
					}
				}else{
					UIHelperUtil.showToastMessage("Unable to fetch login detals. Please try again");
				}

			}

			@Override
			public void onError(String error) {
				progressDlg.dismiss();
				showToast(error);
			}
		});
	}
	public void showToast(String text) {
		Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
	}

	//facebook 
	@Override
	public void onPause() {
		super.onPause();
		if(SocialNetworkingUtils.uiHelper != null){
			SocialNetworkingUtils.uiHelper.onPause();
		}
	}
	//facebook 
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(SocialNetworkingUtils.uiHelper != null){
			SocialNetworkingUtils.uiHelper.onSaveInstanceState(outState);
		}

	}

	/**
	 * facebook post dialog
	 */

	private void publishFeedDialog(String desc,String link,final String mediaUrl, final Context context,final ProgressDialog pd) {
		Bundle params = new Bundle();
		params.putString("link", link);
		params.putString("description",  desc);
		params.putString("name", "MySportsCast");
		params.putString("source", mediaUrl);
		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(
				context, Session.getActiveSession(), params))
				.setOnCompleteListener(new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(context, "Facebook posted",
										Toast.LENGTH_SHORT).show();
								if(isinstaClick){
									sharingToInstagram(mediaUrl,pd);
									/*Log.d("","koti insta"+eventPic);
										new DownloadImageTaskForInstagram().execute(eventPic);*/
								}else{
									exitActivity(pd);
									/*search_people_et.setText("");
									search_event_et.setText("");
									eventId = "";
									if(activity!=null){
										activity.finish();
									}*/
								}
							} else {
								// User clicked the Cancel button
								Toast.makeText(
										context
										.getApplicationContext(),
										"Facebook publish cancelled", Toast.LENGTH_SHORT)
										.show();
								if(isinstaClick){
									sharingToInstagram(mediaUrl,pd);
									/*Log.d("","koti insta"+eventPic);
										new DownloadImageTaskForInstagram().execute(eventPic);*/
								}else{
									exitActivity(pd);
									/*search_people_et.setText("");
									search_event_et.setText("");
									eventId = "";
									if(activity!=null){
										activity.finish();
									}*/
								}

							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(
									context,
									"Facebook publish cancelled", Toast.LENGTH_SHORT)
									.show();
							if(isinstaClick){
								sharingToInstagram(mediaUrl,pd);
								/*Log.d("","koti insta"+eventPic);
									new DownloadImageTaskForInstagram().execute(eventPic);*/
							}else{
								exitActivity(pd);
								/*search_people_et.setText("");
								search_event_et.setText("");
								eventId = "";
								if(activity!=null){
									activity.finish();
								}*/
							}
						} else {
							// Generic, ex: network error
							Toast.makeText(
									context,
									"Facebook error posting story", Toast.LENGTH_SHORT)
									.show();
							if(isinstaClick){
								sharingToInstagram(mediaUrl,pd);
								/*Log.d("","koti insta"+eventPic);
									new DownloadImageTaskForInstagram().execute(eventPic);*/
							}else{
								exitActivity(pd);
								/*search_people_et.setText("");
								search_event_et.setText("");
								eventId = "";
								if(activity!=null){
									activity.finish();
								}*/
							}
						}
					}

				}).build();
		feedDialog.show();

	}

	public void setEventTypeAndId(String type, String id) {
		eventType = type;
		eventId = id;
	}

	public void setSearchInfoList(List<SearchInfo> searchlist) {
		listSearchInfo = searchlist;
		// isCallOnresume = true;
	}

	/**
	 * AsyncTask Downloading pic from urls for twitter.
	 * 
	 * @author koti
	 * 
	 */
	private void sharingToTwitter(View view,final String mediaUrl,final ProgressDialog pd) {

		try {
			SharedPreferences mSharedPref = activity.getSharedPreferences(
					"Android_Twitter_Preferences", Context.MODE_PRIVATE);
			if (mSharedPref.getString(Constants.KEY_TWITTER_ACCESS_TOKEN, null) != null) {
				HelperMethods.postToTwitter(activity,
						((Activity) activity), shareString,
								new TwitterCallback() {

					@Override
					public void onFinsihed(Boolean response) {

						if (response) {
							UIHelperUtil
							.showToastMessage("posted succeded");
							if (isfbClick) {
								Session session = Session
										.getActiveSession();
								if (session != null
										&& session.isOpened()) {
									publishFeedDialog(shareString,
											"", mediaUrl, activity,pd);
								}
							} else if (isinstaClick) {
								sharingToInstagram(mediaUrl,pd);
							} else {
								//								activity.finish();
								exitActivity(pd);
								/*search_people_et.setText("");
								search_event_et.setText("");
								eventId = "";
								if(activity!=null){
									activity.finish();
								}*/
							}
						} else {
							Toast.makeText(activity,
									"posted failed" + response,
									Toast.LENGTH_SHORT).show();
							if (isfbClick) {
								Session session = Session
										.getActiveSession();
								if (session != null
										&& session.isOpened()) {
									publishFeedDialog(shareString,
											"", mediaUrl, activity,pd);
								}
							} else if (isinstaClick) {
								sharingToInstagram(mediaUrl,pd);
							}else{
								exitActivity(pd);
								/*search_people_et.setText("");
								search_event_et.setText("");
								eventId = "";
								if(activity!=null){
									activity.finish();
								}*/
							}
						}

					}
				});
			} else {
				// Toast.makeText(activity, "session is null",
				// Toast.LENGTH_SHORT).show();
				signinTwitterForVideo(view,
						SocialNetworkingUtils.mTwitter, shareString,
								mediaUrl, activity,pd);
			}
		} catch (Exception ex) {
			Toast.makeText(activity, "Please try again",
					Toast.LENGTH_SHORT).show();
		}

	}


	private void signinTwitterForVideo(final View v,Twitter mTwitter,final String desc,final String mediaUrl,final Activity activity,final ProgressDialog pd) {
		mTwitter.signin(new Twitter.SigninListener() {				
			@Override
			public void onSuccess(OauthAccessToken accessToken, String userId, String screenName) {
				//				postingtoTwitter(v,activity,desc);
				HelperMethods.postToTwitter(activity,
						((Activity) activity), shareString+" Video: "+mediaUrl,
								new TwitterCallback() {

					@Override
					public void onFinsihed(Boolean response) {

						if (response) {
							UIHelperUtil
							.showToastMessage("posted succeded");
							if (isfbClick) {
								Session session = Session
										.getActiveSession();
								if (session != null
										&& session.isOpened()) {
									publishFeedDialog(shareString,
											"", mediaUrl, activity,pd);
								}
							} else if (isinstaClick) {
								//								sharingToInstagram(shareBitmap);
							} else {
								activity.finish();
							}
						} else {
							Toast.makeText(activity,
									"posted failed" + response,
									Toast.LENGTH_SHORT).show();
							if (isfbClick) {
								Session session = Session
										.getActiveSession();
								if (session != null
										&& session.isOpened()) {
									publishFeedDialog(shareString,
											"", mediaUrl, activity,pd);
								}
							} else if (isinstaClick) {
								//								sharingToInstagram(shareBitmap);
							}
						}

					}
				});
			}

			@Override
			public void onError(String error) {
				UIHelperUtil.showToastMessage(error);
			}
		});
	}

	/**
	 * AsyncTask Downloading pic from url for instagram.
	 * 
	 * @author koti
	 * 
	 */
	private void sharingToInstagram(String mediaUrl,ProgressDialog pd) {


		//		File file = downloadVideoFile(mediaUrl,"Sample.mp4");  
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
				shareIntent.putExtra(Intent.EXTRA_TEXT, shareString+" check link:"+mediaUrl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			shareIntent.setType("image/jpeg");

			activity.startActivity(shareIntent);
			exitActivity(pd);
			/*search_people_et.setText("");
			search_event_et.setText("");
			eventId = "";
			if(activity!=null){
				activity.finish();
			}*/
		} else {
			// bring user to the market to download the app.
			// or let them choose an app?
			
			UIHelperUtil.showToastMessage("Please install Instagram app");
			exitActivity(pd);
			/*intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse("market://details?id="
					+ "com.instagram.android"));
			activity.startActivity(intent);*/
		}
		
	}

	public File downloadVideoFile(String fileURL, String fileName) {
		File yourFile = null;
		try {
			String RootDir = Environment.getExternalStorageDirectory()
					+ File.separator + "Video";
			File RootFile = new File(RootDir);
			RootFile.mkdir();
			// File root = Environment.getExternalStorageDirectory();
			URL u = new URL(fileURL);
			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();
			FileOutputStream f = new FileOutputStream(new File(RootFile,
					fileName));
			InputStream in = c.getInputStream();
			byte[] buffer = new byte[1024];
			int len1 = 0;

			while ((len1 = in.read(buffer)) > 0) {                          
				f.write(buffer, 0, len1);               
			}       
			f.close();

			yourFile = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "Video", "Sample.mp4");

		} catch (Exception e) {

			Log.d("Error....", e.toString());
		}

		return yourFile;

	}

	private void exitActivity(ProgressDialog pd){
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}

		search_people_et.setText("");
		search_event_et.setText("");
		eventId = "";
		if(activity!=null){
			activity.finish();
		}

	}
}
