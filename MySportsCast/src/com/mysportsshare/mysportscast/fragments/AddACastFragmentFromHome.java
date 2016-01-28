package com.mysportsshare.mysportscast.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.londatiga.android.twitter.Twitter;
import net.londatiga.android.twitter.TwitterRequest;
import net.londatiga.android.twitter.TwitterUser;
import net.londatiga.android.twitter.oauth.OauthAccessToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.dina.oauth.instagram.InstagramApp;
import br.com.dina.oauth.instagram.InstagramApp.OAuthAuthenticationListener;

import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.WebDialog;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CalendarEventsActivity;
import com.mysportsshare.mysportscast.activity.HomeActivity;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.instagram.ApplicationData;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.HelperMethods;
import com.mysportsshare.mysportscast.utils.HelperMethods.TwitterCallback;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.SocialNetworkingUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class AddACastFragmentFromHome extends Fragment implements OnClickListener,TextWatcher{

	/*
	 * views
	 */
	private EditText etComment;
	private TextView commentCountDown;
	private TextView etSearch;
	private ImageView search_event_iv;
	private Button btnShareTwit;
	private LoginButton btnShareFb;
	private Button btnShareByText;
	private Button btnBroadCast;
	private ImageView imgCheckShareTwit;
	private ImageView imgCheckShareFb;
	private ImageView imgCheckShareBytxt;
	private boolean isfbClick;
	private boolean istwitClick;
	private boolean isinstaClick;
	private String eventId="";
	private boolean isClicked;
	private String eventPic="";
	private LinearLayout llytSearchEvents;
	static AddACastFragmentFromHome f = new AddACastFragmentFromHome();
	private boolean addSuccessFlag = false;
	private Activity activity;
	private LinearLayout llytmultiEvent;
	private String eventType;
	private String shareString = "";

	public static Fragment getInstance(Context context) {
		if (f == null) {
			f = new AddACastFragmentFromHome();
		}
		return f;
	}

	public static Fragment newInstance(Context context) {

		f = new AddACastFragmentFromHome();
		return f;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.activity = activity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_addacast, container,
				false);
		init(view);

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

		btnBroadCast.setOnClickListener(this);
		btnShareByText.setOnClickListener(this);
		btnShareFb.setOnClickListener(this);

		btnShareFb.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		//		btnShareFb.setFragment(this);
		btnShareFb.setReadPermissions(Arrays.asList("email"));
		btnShareFb.setUserInfoChangedCallback(new UserInfoChangedCallback() {

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

		btnShareTwit.setOnClickListener(this);

		return view;
	}

	/*@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Session.getActiveSession().onActivityResult(activity, requestCode, resultCode, data);

	}*/

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

	/**
	 * Initializing all views in Add a cast page.
	 * 
	 * @param view
	 */
	private void init(View view) {

		llytSearchEvents = (LinearLayout) view.findViewById(R.id.search_event_llyt);
		llytSearchEvents.setOnClickListener(this);
		commentCountDown = (TextView)view.findViewById(R.id.comment_cnt_tv);
		llytmultiEvent = (LinearLayout) view.findViewById(R.id.search_multi_event_llyt);
		etComment = (EditText) view.findViewById(R.id.comment_et);
		etSearch = (TextView) view.findViewById(R.id.search_event_tv);
		search_event_iv = (ImageView)view.findViewById(R.id.search_event_iv);
		search_event_iv.setVisibility(View.GONE);
		btnShareTwit = (Button) view
				.findViewById(R.id.share_twitter_btn);
		btnShareFb = (LoginButton) view
				.findViewById(R.id.share_facebook_btn);
		btnShareByText = (Button) view
				.findViewById(R.id.share_by_txt_btn);
		btnBroadCast = (Button) view.findViewById(R.id.share_broadcast);
		imgCheckShareTwit = (ImageView) view.findViewById(R.id.twitter_iv);
		imgCheckShareFb = (ImageView) view.findViewById(R.id.fb_iv);
		imgCheckShareBytxt = (ImageView) view.findViewById(R.id.by_txt_iv);
		SocialNetworkingUtils.mTwitter = new Twitter(activity, HomeActivity.CONSUMER_KEY, HomeActivity.CONSUMER_SECRET, HomeActivity.CALLBACK_URL);
		SocialNetworkingUtils.mApp = new InstagramApp(activity, ApplicationData.CLIENT_ID,
				ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
		etComment.addTextChangedListener(this);
		etComment.requestFocus();
	}
	@Override
	public void onResume() {
		super.onResume();

		TextView title = (TextView) activity.findViewById(
				R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);

		TextView eventtype = (TextView) activity.findViewById(
				R.id.title_bar_tv_event_type);

		Bundle bundle = getArguments();
		if (bundle != null) {
			boolean isEventDetails = bundle.getBoolean(Constants.KEY_IS_EVENT_DETAILS);
			eventId = bundle.getString(Constants.KEY_EVENT_ID);
			eventType = bundle.getString(Constants.KEY_EVENT_TYPE);
			eventPic = bundle.getString(Constants.KEY_EVENT_PIC);
			//			etSearch.setText(eventType);
			if(isEventDetails){
				title.setText("ADD A CAST");
				eventtype.setVisibility(View.GONE);
			}
			setDynamicView();
			Log.d("cast event","cast event"+eventId+" "+eventPic);
		}

		ImageView settingIv = (ImageView) activity.findViewById(
				R.id.setting_iv);
		settingIv.setVisibility(View.GONE);

		ImageView searchIv = (ImageView) activity.findViewById(
				R.id.search_iv);
		searchIv.setVisibility(View.GONE);

		ImageView backIv = (ImageView) activity.findViewById(R.id.back_iv);
		backIv.setVisibility(View.VISIBLE);
		backIv.setOnClickListener(this);

		ImageView addaneventIv = (ImageView) activity.findViewById(
				R.id.add_an_event_iv);
		addaneventIv.setVisibility(View.GONE);

		/*Bundle eventbundle = getArguments();
		if (eventbundle != null) {
			eventId = eventbundle.getString(Constants.KEY_EVENT_ID);
			String eventType = eventbundle.getString(Constants.KEY_EVENT_TYPE);
			eventPic = eventbundle.getString(Constants.KEY_EVENT_PIC);
			etSearch.setText(eventType);
			Log.v("", "event bundle koti"+eventType+" "+eventId+" "+eventPic);
		} else {
			Log.v("", "No Params");
		}*/

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
			etSearch.setVisibility(View.GONE);
			llytmultiEvent.removeAllViews();
			final View tag_layout = LayoutInflater.from(getActivity()).inflate(
					R.layout.tag_event, null);
			((TextView) tag_layout.findViewById(R.id.tag_tv))
			.setText(eventType);
			ImageView crossImageView = ((ImageView) tag_layout
					.findViewById(R.id.tag_cross_image));
			crossImageView.setVisibility(View.GONE);
			llytmultiEvent.addView(tag_layout);
			//			search_event_et.setText(eventType);
		}


	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_broadcast:

			InputMethodManager input = (InputMethodManager) activity
			.getSystemService(Activity.INPUT_METHOD_SERVICE);
			input.hideSoftInputFromWindow(
					activity.getCurrentFocus()
					.getWindowToken(), 0);
			/*if(!etComment.getText().toString().matches("")){			
				if(!TextUtils.isEmpty(eventId)){
					if(!Utils.chkStatus()){
						Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
						//			showCloseAppDialogue();
					}else{
						serviceToAddACast(v);
					}

				}else{
					UIHelperUtil.showToastMessage("Please select event");
				}
			}else{
				UIHelperUtil.showToastMessage("Please enter comment");
			}*/
			if(!TextUtils.isEmpty(etComment.getText().toString())){
				serviceToAddACast(v);
			}
			else{
				UIHelperUtil.showToastMessage("Please enter text to add cast");
			}
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
			/*Fragment activeFragment = new SearchCastEvents1();	
			Bundle bundle = new Bundle();
			bundle.putString("isfromHome", "home");
			activeFragment.setArguments(bundle);
			if(activity!=null){
				if(activity.getClass().getSimpleName().equalsIgnoreCase("MainActivity")){
					//displays user page in specified tab.
					((MainActivity)activity).pushFragments(((MainActivity)activity).getmCurrentTab(), activeFragment, false,true);
				}else{
					//					FragmentTransaction transaction = activity
					//							.getSupportFragmentManager().beginTransaction(); 
					//					transaction.add(R.id.home_frame, activeFragment);
					//					transaction.addToBackStack("searchCastEventFragment");
					//					transaction.commit();
					((CalendarEventsActivity)activity).push(activeFragment);
				}
			}*/
			break;
		case R.id.back_iv:
			if(activity!=null) {
				(activity).onBackPressed();
			}
			break;

		default:
			break;
		}
	}

	private void signinTwitter() {
		if (SocialNetworkingUtils.mTwitter != null) {
			SocialNetworkingUtils.mTwitter.signin(new Twitter.SigninListener() {
				@Override
				public void onSuccess(OauthAccessToken accessToken,
						String userId, String screenName) {
					getCredentials();
				}

				@Override
				public void onError(String error) {
					showToast(error);
				}
			});
		}

	}

	private void getCredentials() {
		final ProgressDialog progressDlg = new ProgressDialog(activity);
		// progressDlg.setMessage("Getting credentials...");
		progressDlg.setMessage("wait...");
		progressDlg.setCancelable(false);
		progressDlg.show();
		TwitterRequest request = new TwitterRequest(
				SocialNetworkingUtils.mTwitter.getConsumer(),
				SocialNetworkingUtils.mTwitter.getAccessToken());
		request.verifyCredentials(new TwitterRequest.VerifyCredentialListener() {
			@Override
			public void onSuccess(TwitterUser user) {
				progressDlg.dismiss();
				if (user != null) {
					if (!Utils.chkStatus()) {
						Utils.networkAlertDialog(activity, getResources()
								.getString(R.string.toast_no_network));
					} else {
						/*
						 * Session session = Session.getActiveSession(); if
						 * (session != null && session.isOpened()) {
						 * session.closeAndClearTokenInformation();
						 * SocialNetworkingUtils.uiHelper.onDestroy(); }
						 * SocialNetworkingUtils.mApp.resetAccessToken();
						 */
						// imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
						// imgCheckShareBytxt.setImageResource(R.drawable.cb_unchecked);
						imgCheckShareTwit
						.setImageResource(R.drawable.cb_checked);
					}
				} else {
					UIHelperUtil
					.showToastMessage("Unable to fetch login detals. Please try again");
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

	/**
	 * method to add a cast
	 */
	private void serviceToAddACast(final View view) {
		final String iamAttendingUrl = Constants.common_url
				+ getResources().getString(R.string.add_a_cast);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("event_id", eventId));
		nameValuePairs.add(new BasicNameValuePair("user_id",
				SharedPreferencesUtils.getUserId()));
		nameValuePairs.add(new BasicNameValuePair("cast_info", etComment
				.getText().toString()));
		JsonParser.callBackgroundService(iamAttendingUrl, nameValuePairs,
				new JsonServiceListener() {

			/*@Override
					public void navigateToPrevFragment() {
						super.hideProgress();
						if (addSuccessFlag) {
							if (activity != null) {
								(activity).onBackPressed();
							}
						}
					}*/
			ProgressDialog pd;

			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(activity);
				pd.setMessage("Please wait...");
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
				pd.show();
				addSuccessFlag = false;
			}

			@Override
			public void parseJsonResponse(String jsonResponse) {
				if (jsonResponse != null) {
					JSONObject jsonObject;
					Log.d("", "Response: " + jsonResponse);
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject
								.getJSONObject(Constants.TAG_RESPONSE);
						String resStr = resObj
								.getString(Constants.TAG_RESPONSE_INFO);
						if (resStr != null
								&& resStr
								.equalsIgnoreCase(Constants.TAG_SUCCESS)) {
							UIHelperUtil
							.showToastMessage("Adding a cast successfully");
							if(TextUtils.isEmpty(shareString)){
								shareString = "Add cast to his profile" + ". For more details please visit http://www.mysportsshare.com";
							}else{
								shareString = "Add cast to an event " + eventType + ". For more details please visit http://www.mysportsshare.com";
							}
							if (istwitClick) {
								new DownloadImageTaskForTwitter(view,pd)
								.execute(eventPic);
							} else if (isfbClick) {
								Session session = Session
										.getActiveSession();
								if (session != null
										&& session.isOpened()) {
									publishFeedDialog(shareString, "",
											eventPic, activity,pd);
								}
							} else if (isinstaClick) {
								Log.d("", "koti insta" + eventPic);
								new DownloadImageTaskForInstagram(pd)
								.execute(eventPic);
							}else{
								exitActivity(pd);
								/*etComment.setText("");
										eventId = "";
										etSearch.setText("");
										addSuccessFlag = true;*/
							}


						} else if (resStr != null
								&& resStr
								.equalsIgnoreCase(Constants.TAG_FAILURE)) {
							UIHelperUtil
							.showToastMessage("Something went wrong with server.....");
						} else {
							//									UIHelperUtil.showToastMessage("Server error..!");
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
					//
				} else {
					UIHelperUtil
					.showToastMessage("Something went wrong with internet.....");
				}
			}
		});
	}

	/**
	 * AsyncTask Downloading pic from urls for twitter.
	 * 
	 * @author koti
	 * 
	 */
	private class DownloadImageTaskForTwitter extends
	AsyncTask<String, Integer, Bitmap> {
		ProgressDialog pd;
		String socialImageUrl;
		File file;
		View view;

		public DownloadImageTaskForTwitter(View v,ProgressDialog pd) {
			view = v;
			this.pd = pd;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*pd = new ProgressDialog(getActivity());
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.show();*/
		}

		protected Bitmap doInBackground(String... urls) {
			socialImageUrl = urls[0];
			Bitmap bmp = BitmapUtils.downloadImage(urls[0]);
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/twitter_post_pic");
			myDir.mkdirs();
			String fname = "event_pic.jpg";
			file = new File(myDir, fname);
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return bmp;
		}

		protected void onPostExecute(Bitmap result) {
			/*if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}*/
			if (result != null) {
				try {
					SharedPreferences mSharedPref = activity
							.getSharedPreferences(
									"Android_Twitter_Preferences",
									Context.MODE_PRIVATE);
					String filename = file.getAbsolutePath();
					if (mSharedPref.getString(
							Constants.KEY_TWITTER_ACCESS_TOKEN, null) != null) {
						HelperMethods.postToTwitterWithImage(activity,
								((Activity) activity), filename, shareString,
								new TwitterCallback() {

							@Override
							public void onFinsihed(Boolean response) {

								if (response) {
									Toast.makeText(activity,
											"posted succeded",
											Toast.LENGTH_SHORT).show();
									if (isfbClick) {
										Session session = Session
												.getActiveSession();
										if (session != null
												&& session.isOpened()) {
											publishFeedDialog(
													shareString,
													"", eventPic,
													activity,pd);
										}
									} else if (isinstaClick) {
										Log.d("", "koti insta"
												+ eventPic);
										new DownloadImageTaskForInstagram(pd)
										.execute(eventPic);
									}else{
										exitActivity(pd);
										/*etComment.setText("");
												eventId = "";
												etSearch.setText("");
												addSuccessFlag = true;*/
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
											publishFeedDialog(
													shareString,
													"", eventPic,
													activity,pd);
										}
									} else if (isinstaClick) {
										Log.d("", "koti insta"
												+ eventPic);
										new DownloadImageTaskForInstagram(pd)
										.execute(eventPic);
									}else{
										exitActivity(pd);
										/*etComment.setText("");
												eventId = "";
												etSearch.setText("");
												addSuccessFlag = true;*/
									}
								}

							}
						});
					} else {
						// Toast.makeText(activity, "session is null",
						// Toast.LENGTH_SHORT).show();
						SocialNetworkingUtils.signinTwitter(view,
								SocialNetworkingUtils.mTwitter, shareString,
								eventPic, activity);
					}
				} catch (Exception ex) {
					Toast.makeText(activity, "Please try again",
							Toast.LENGTH_SHORT).show();
				}
			} else {
			}
		}
	}

	/**
	 * AsyncTask Downloading pic from url for instagram.
	 * 
	 * @author koti
	 * 
	 */
	private class DownloadImageTaskForInstagram extends
	AsyncTask<String, Integer, Bitmap> {
		ProgressDialog pd;
		File file;

		public DownloadImageTaskForInstagram(ProgressDialog pd) {
			this.pd = pd;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*pd = new ProgressDialog(activity);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.show()*/;
		}

		protected Bitmap doInBackground(String... urls) {
			Bitmap bmp = BitmapUtils.downloadImage(urls[0]);
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/instagram_post_pic");
			myDir.mkdirs();
			String fname = "event_pic.jpg";
			file = new File(myDir, fname);
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return bmp;
		}

		protected void onPostExecute(Bitmap result) {

			/*if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}*/
			if (result != null) {
				Log.d("", "koti insta if");
				try {
					String filename = file.getAbsolutePath();
					Intent intent = MySportsApp.getAppContext()
							.getPackageManager()
							.getLaunchIntentForPackage("com.instagram.android");

					if (intent != null) {

						Intent shareIntent = new Intent();
						shareIntent.setAction(Intent.ACTION_SEND);
						shareIntent.setPackage("com.instagram.android");
						try {
							shareIntent.putExtra(Intent.EXTRA_STREAM, Uri
									.parse(MediaStore.Images.Media.insertImage(
											activity.getContentResolver(),
											filename, "", "")));
							shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						shareIntent.setType("image/jpeg");

						startActivity(shareIntent);
						exitActivity(pd);
						/*etComment.setText("");
						eventId = "";
						etSearch.setText("");
						addSuccessFlag = true;*/
					} else {
						UIHelperUtil
						.showToastMessage("Please install Instagram app");
						exitActivity(pd);
						/*intent = new Intent(Intent.ACTION_VIEW);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setData(Uri.parse("market://details?id="+"com.instagram.android"));
						startActivity(intent);*/
						// bring user to the market to download the app.
						// or let them choose an app?
						/*
						 * intent = new Intent(Intent.ACTION_VIEW);
						 * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						 * intent.setData(Uri.parse("market://details?id="+
						 * "com.instagram.android")); startActivity(intent);
						 */
					}

				} catch (Exception ex) {
					if (activity != null)
						Toast.makeText(activity, "Please try again",
								Toast.LENGTH_SHORT).show();
				}
			} else {
				Log.d("", "koti insta else");
			}
		}
	}

	// facebook
	@Override
	public void onPause() {
		super.onPause();
		if (SocialNetworkingUtils.uiHelper != null) {
			SocialNetworkingUtils.uiHelper.onPause();
		}

	}

	// facebook
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (SocialNetworkingUtils.uiHelper != null) {
			SocialNetworkingUtils.uiHelper.onSaveInstanceState(outState);
		}

	}

	/**
	 * facebook post dialog
	 */

	private void publishFeedDialog(String desc, String link,
			String eventPicUrl, final Context context,final ProgressDialog pd) {
		Bundle params = new Bundle();
		params.putString("link", link);
		params.putString("description", desc);
		params.putString("name", "MySportsCast");
		params.putString("picture", eventPicUrl);
		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(context,
				Session.getActiveSession(), params)).setOnCompleteListener(
						new OnCompleteListener() {

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
										if (isinstaClick) {
											Log.d("", "koti insta" + eventPic);
											new DownloadImageTaskForInstagram(pd)
											.execute(eventPic);
										}else{
											exitActivity(pd);
											/*etComment.setText("");
									eventId = "";
									etSearch.setText("");
									addSuccessFlag = true;*/
										}
									} else {
										// User clicked the Cancel button
										Toast.makeText(context.getApplicationContext(),
												"Facebook publish cancelled",
												Toast.LENGTH_SHORT).show();
										if (isinstaClick) {
											Log.d("", "koti insta" + eventPic);
											new DownloadImageTaskForInstagram(pd)
											.execute(eventPic);
										}else{
											exitActivity(pd);
											/*etComment.setText("");
									eventId = "";
									etSearch.setText("");
									addSuccessFlag = true;*/
										}

									}
								} else if (error instanceof FacebookOperationCanceledException) {
									// User clicked the "x" button
									Toast.makeText(context,
											"Facebook publish cancelled",
											Toast.LENGTH_SHORT).show();
									if (isinstaClick) {
										Log.d("", "koti insta" + eventPic);
										new DownloadImageTaskForInstagram(pd)
										.execute(eventPic);
									}else{
										exitActivity(pd);
										/*	etComment.setText("");
								eventId = "";
								etSearch.setText("");
								addSuccessFlag = true;*/
									}
								} else {
									// Generic, ex: network error
									Toast.makeText(context,
											"Facebook error posting story",
											Toast.LENGTH_SHORT).show();
									if (isinstaClick) {
										Log.d("", "koti insta" + eventPic);
										new DownloadImageTaskForInstagram(pd)
										.execute(eventPic);
									}else{
										exitActivity(pd);
										/*etComment.setText("");
								eventId = "";
								etSearch.setText("");
								addSuccessFlag = true;*/
									}
								}
							}

						}).build();
		feedDialog.show();

	}
	/*
	 * private void publishFeedDialog(String desc,String link,String
	 * eventPicUrl, final Context context){ new UploadPic().execute(); } class
	 * SampleUploadListener implements AsyncFacebookRunner.RequestListener {
	 * 
	 * @Override public void onComplete(String response, Object state) { }
	 * 
	 * @Override public void onIOException(IOException e, Object state) { }
	 * 
	 * @Override public void onFileNotFoundException(FileNotFoundException e,
	 * Object state) { }
	 * 
	 * @Override public void onMalformedURLException(MalformedURLException e,
	 * Object state) { }
	 * 
	 * @Override public void onFacebookError(FacebookError e, Object state) { }
	 * 
	 * }
	 * 
	 * private class UploadPic extends AsyncTask<Void, Void, Void> {
	 * ProgressDialog pd;
	 * 
	 * public UploadPic() { }
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute(); pd = new
	 * ProgressDialog(activity); pd.setMessage("Please wait...");
	 * pd.setCancelable(false); pd.setCanceledOnTouchOutside(false); pd.show();
	 * }
	 * 
	 * 
	 * protected void onPostExecute(String result) { if (pd != null &&
	 * pd.isShowing()) { pd.dismiss(); }
	 * 
	 * }
	 * 
	 * @Override protected Void doInBackground(Void... paams) {
	 * 
	 * byte[] data = null;
	 * 
	 * Facebook facebook = new Facebook("613125242155496"); Session session =
	 * Session.getActiveSession(); if (session != null && session.isOpened()) {
	 * facebook.setAccessToken(session.getAccessToken()); }
	 * 
	 * Bitmap bi = BitmapFactory.decodeResource(getResources(),
	 * R.drawable.ic_launcher); ByteArrayOutputStream baos = new
	 * ByteArrayOutputStream(); bi.compress(Bitmap.CompressFormat.JPEG, 100,
	 * baos); data = baos.toByteArray(); Bundle params = new Bundle();
	 * params.putString(Facebook.TOKEN, facebook.getAccessToken());
	 * params.putString("method", "photos.upload");
	 * params.putByteArray("picture", data); // image to post
	 * params.putString("caption", "My text on wall with Image "); // text to
	 * post AsyncFacebookRunner mAsyncRunner = new
	 * AsyncFacebookRunner(facebook); mAsyncRunner.request(null, params, "POST",
	 * new SampleUploadListener(), null);
	 * 
	 * return null; } }
	 */

	private void exitActivity(ProgressDialog pd){
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		etComment.setText("");
		eventId = "";
		etSearch.setText("");
		addSuccessFlag = true;
		if (activity != null) {
			(activity).onBackPressed();
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		String commentContent = etComment.getText().toString();
		int countDown = commentContent.length();
		int remainingCount = 140 - countDown;
		commentCountDown.setText("Type comment here ("+remainingCount+")");
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}
}
