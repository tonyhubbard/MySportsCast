package com.mysportsshare.mysportscast.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.londatiga.android.twitter.Twitter;
import net.londatiga.android.twitter.TwitterRequest;
import net.londatiga.android.twitter.TwitterUser;
import net.londatiga.android.twitter.oauth.OauthAccessToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.service.voice.AlwaysOnHotwordDetector.EventPayload;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
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
import com.mysportsshare.mysportscast.instagram.ApplicationData;
import com.mysportsshare.mysportscast.models.ProfileMediaInfo;
import com.mysportsshare.mysportscast.models.SearchInfo;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.HelperMethods;
import com.mysportsshare.mysportscast.utils.HelperMethods.TwitterCallback;
import com.mysportsshare.mysportscast.utils.CustomLog;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.SocialNetworkingUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;
import com.mysportsshare.mysportscast.view.FlowLayout;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class FilterImageBroadCastFragment extends Fragment implements
		OnClickListener {

	private String imgPath;
	private String mediaUrl;
	private String twitterEventPic;
	private ImageView backBtn;
	private TextView title;
	private ImageView settingBtn;

	private ImageView photo_iv;

	private EditText photo_caption_et;

	private TextView search_people_et;

	private ImageView search_people_iv;

	private TextView search_event_et;

	private ImageView search_event_iv;
	private ImageView imgCheckShareTwit;
	private ImageView imgCheckShareFb;
	private ImageView imgCheckShareBytxt;
	private String eventType = "";
	private String eventId = "";
	private Button btnShareTwit;

	private LoginButton btnShareFb;

	private Button btnShareByText;

	private Button btnBroadCast;
	private Bitmap shareBitmap;

	private String base64Image = "";
	// private String event_id = "277";
	private boolean isfbClick;
	private boolean istwitClick;
	private boolean isinstaClick;
	private boolean isClicked;
	private boolean isFromHome = false;
	Activity activity;
	private LinearLayout llytSearchEvents;
	private LinearLayout llytSearchPeople;
	private LinearLayout llytmultiEvent;
	private FlowLayout llytmultiPeple;
	// private boolean isCallOnresume;
	private List<SearchInfo> listSearchInfo;
	int pos;
	static FilterImageBroadCastFragment f = new FilterImageBroadCastFragment();
	String taggedUsers;
	private List<SearchInfo> selectedInvitesUsersList = new ArrayList<SearchInfo>();
	private String invitedUsers = "";
	private String shareString = "";

	public static Fragment getInstance(Context context) {
		if (f == null) {
			f = new FilterImageBroadCastFragment();
		}
		return f;
	}

	public static Fragment getnewInstance(Context context) {
		f = new FilterImageBroadCastFragment();
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
		final View layoutView = inflater.inflate(R.layout.fragment_addaphoto,
				container, false);

		init(layoutView);
		Bundle args = getArguments();
		if (args != null) {
			imgPath = args.getString(Constants.dataReceive);
			if (args.getString(Constants.KEY_EVENT_TYPE) != null
					&& args.getString(Constants.KEY_EVENT_ID) != null
					&& args.getString(Constants.KEY_EVENT_PIC) != null) {
				eventType = args.getString(Constants.KEY_EVENT_TYPE);
				eventId = args.getString(Constants.KEY_EVENT_ID);
				mediaUrl = args.getString(Constants.KEY_EVENT_PIC);
				// search_event_et.setText(eventType);
				isFromHome = true;
				search_event_iv.setVisibility(View.GONE);
				Log.d("", "oncreate event type:" + eventType);
			} else {
				isFromHome = false;
				search_event_iv.setVisibility(View.VISIBLE);
			}
		}

		if (!Utils.chkStatus()) {
			Utils.networkAlertDialog(activity,
					getResources().getString(R.string.toast_no_network));
			// showCloseAppDialogue();
		} else {
			SocialNetworkingUtils.mApp.setListener(listener);
			SocialNetworkingUtils.uiHelper = new UiLifecycleHelper(activity,
					statusCallback);
			SocialNetworkingUtils.uiHelper.onCreate(savedInstanceState);
		}
		isfbClick = false;
		istwitClick = false;
		isinstaClick = false;

		btnBroadCast.setOnClickListener(this);
		btnShareByText.setOnClickListener(this);
		btnShareFb.setOnClickListener(this);

		btnShareFb.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		// btnShareFb.setFragment(this);
		btnShareFb.setReadPermissions(Arrays.asList("email"));
		btnShareFb.setUserInfoChangedCallback(new UserInfoChangedCallback() {

			@Override
			public void onUserInfoFetched(GraphUser user) {
				if (user != null) {
					if (isClicked) {
						if (!Utils.chkStatus()) {
							Utils.networkAlertDialog(activity, getResources()
									.getString(R.string.toast_no_network));
						} else {
							Log.i("", "Session isClicked: " + isClicked);
							imgCheckShareFb
									.setImageResource(R.drawable.cb_checked);
							isfbClick = true;

						}

					} else {
						Log.i("", "Session isClicked: " + isClicked);
					}
					Log.v("", "user: " + user.getName() + " : " + user.getId());

				} else {
					Log.i("", "Session user: " + user);
					// Session.openActiveSession(activity, true,
					// statusCallback);
					// UIHelperUtil.showToastMessage("Unable to fetch login details");
				}
			}
		});

		btnShareTwit.setOnClickListener(this);

		return layoutView;
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

	OAuthAuthenticationListener listener = new OAuthAuthenticationListener() {

		@Override
		public void onSuccess() {
			if (!Utils.chkStatus()) {
				Utils.networkAlertDialog(activity,
						getResources().getString(R.string.toast_no_network));
			} else {
				/*
				 * Session session = Session.getActiveSession(); if (session !=
				 * null && session.isOpened()) {
				 * session.closeAndClearTokenInformation();
				 * SocialNetworkingUtils.uiHelper.onDestroy(); }
				 * SocialNetworkingUtils.mTwitter.clearSession();
				 */
				// imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
				imgCheckShareBytxt.setImageResource(R.drawable.cb_checked);
				// imgCheckShareTwit.setImageResource(R.drawable.cb_unchecked);

			}
		}

		@Override
		public void onFail(String error) {
			Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
		}
	};

	private void init(View layoutView) {
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		backBtn = (ImageView) activity.findViewById(R.id.back_iv);
		settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);

		// Accessing layout fields
		photo_iv = (ImageView) layoutView.findViewById(R.id.photo_iv);
		photo_caption_et = (EditText) layoutView
				.findViewById(R.id.photo_caption_et);
		search_people_et = (TextView) layoutView
				.findViewById(R.id.search_people_tv);
		search_people_iv = (ImageView) layoutView
				.findViewById(R.id.search_people_iv);
		// search_people_iv.setOnClickListener(this);
		search_event_et = (TextView) layoutView
				.findViewById(R.id.search_event_tv);
		search_event_iv = (ImageView) layoutView
				.findViewById(R.id.search_event_iv);
		// search_event_iv.setOnClickListener(this);
		btnShareTwit = (Button) layoutView.findViewById(R.id.share_twitter_btn);
		btnShareFb = (LoginButton) layoutView
				.findViewById(R.id.share_facebook_btn);
		btnShareByText = (Button) layoutView
				.findViewById(R.id.share_by_txt_btn);
		btnBroadCast = (Button) layoutView.findViewById(R.id.share_broadcast);
		btnBroadCast.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		settingBtn.setVisibility(View.GONE);
		llytSearchEvents = (LinearLayout) layoutView
				.findViewById(R.id.search_event_llyt);
		llytmultiEvent = (LinearLayout) layoutView
				.findViewById(R.id.search_multi_event_llyt);
		llytSearchEvents.setOnClickListener(this);
		llytSearchPeople = (LinearLayout) layoutView
				.findViewById(R.id.search_people_llyt);
		llytSearchPeople.setOnClickListener(this);

		llytmultiPeple = (FlowLayout) layoutView
				.findViewById(R.id.search_multi_people_llyt);

		imgCheckShareTwit = (ImageView) layoutView
				.findViewById(R.id.twitter_iv);
		imgCheckShareFb = (ImageView) layoutView.findViewById(R.id.fb_iv);
		imgCheckShareBytxt = (ImageView) layoutView
				.findViewById(R.id.by_txt_iv);
		SocialNetworkingUtils.mTwitter = new Twitter(activity,
				HomeActivity.CONSUMER_KEY, HomeActivity.CONSUMER_SECRET,
				HomeActivity.CALLBACK_URL);
		SocialNetworkingUtils.mApp = new InstagramApp(activity,
				ApplicationData.CLIENT_ID, ApplicationData.CLIENT_SECRET,
				ApplicationData.CALLBACK_URL);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (activity != null) {

			if (!(activity instanceof MainActivity)) {
				((ImageFilterActivity) activity)
						.resetImageEditFragementOpenedFlag();
			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		title.setText(Constants.UI_UPLOAD_PHOTO);

		// Load image
		if (TextUtils.isEmpty(imgPath)) {
			Utils.showAlertDialog(activity, "Alert",
					"Unable to process selected image");
		} else {
			// As converted image is stored in local directory
			BitmapUtils.setImagesWithListener("file:///" + imgPath, photo_iv,
					R.drawable.profile_pic_dummy, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
							Bitmap bmp = ((BitmapDrawable) photo_iv.getDrawable())
									.getBitmap();
							shareBitmap = bmp;
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1,
								Bitmap arg2) {
							Bitmap bitmap = ((BitmapDrawable) photo_iv
									.getDrawable()).getBitmap();
							if(bitmap != null){
								shareBitmap = bitmap;
							}else{
								Bitmap bmp = ((BitmapDrawable) photo_iv.getDrawable())
										.getBitmap();
								shareBitmap = bmp;
							}
							

						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}
					});
			twitterEventPic = "file:///" + imgPath;

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_broadcast:
			// upload photo to server

			/*
			 * No need of validations // TODO: validation
			 * if(!photo_caption_et.getText().toString().matches("")){ //No need
			 * to verify whether people & event are tagged uploadPicture(v);
			 * 
			 * if(listSearchInfo != null && listSearchInfo.size() > 0){
			 * if(!eventId.matches("")){ uploadPicture(v);
			 * 
			 * }else{ UIHelperUtil.showToastMessage("Please tag a event"); }
			 * }else{ UIHelperUtil.showToastMessage("Please tag people"); }
			 * }else{ UIHelperUtil.showToastMessage("Please write caption"); }
			 */

			uploadPicture(v);
			break;
		case R.id.back_iv:
			activity.onBackPressed();
			break;
		case R.id.search_people_llyt:
			if (getActivity() != null) {
				SearchCastPeopleFragment fragmentPeople = new SearchCastPeopleFragment();

				Bundle peoplebundle = new Bundle();
				peoplebundle.putString("isfromHome", "photo");
				if (!TextUtils.isEmpty(invitedUsers)) {
					peoplebundle
							.putParcelableArrayList(
									"selected_users",
									(ArrayList<? extends Parcelable>) selectedInvitesUsersList);
				}
				fragmentPeople.setArguments(peoplebundle);

				/*
				 * FragmentManager fragmentManagerPeople = getActivity()
				 * .getSupportFragmentManager(); FragmentTransaction
				 * fragmentTransactionPeople = fragmentManagerPeople
				 * .beginTransaction();
				 * fragmentTransactionPeople.replace(R.id.home_frame,
				 * fragmentPeople);
				 * fragmentTransactionPeople.addToBackStack("searchcastpeople");
				 * fragmentTransactionPeople.commit();
				 */

				if (activity instanceof ImageFilterActivity) {
					((ImageFilterActivity) activity).push(fragmentPeople);
				}
			}
			break;
		case R.id.search_event_llyt:

			if (getActivity() != null) {
				if (!isFromHome) {
					SearchCastEvents1 fragment = new SearchCastEvents1();
					Bundle bundle = new Bundle();
					bundle.putString("isfromHome", "photo");
					fragment.setArguments(bundle);
					/*
					 * FragmentManager fragmentManager = getActivity()
					 * .getSupportFragmentManager(); FragmentTransaction
					 * fragmentTransaction = fragmentManager
					 * .beginTransaction();
					 * fragmentTransaction.replace(R.id.home_frame, fragment);
					 * fragmentTransaction.addToBackStack("searchcastevent");
					 * fragmentTransaction.commit();
					 */
					if (activity instanceof ImageFilterActivity) {
						((ImageFilterActivity) activity).push(fragment);
					}
				}
			}

			break;

		case R.id.share_twitter_btn:
			Log.d("", "twitter");
			if (SocialNetworkingUtils.mTwitter != null) {
				if (SocialNetworkingUtils.mTwitter.getAccessToken() != null) {
					if (!istwitClick) {
						imgCheckShareTwit
								.setImageResource(R.drawable.cb_checked);
						istwitClick = true;
					} else {
						imgCheckShareTwit
								.setImageResource(R.drawable.cb_unchecked);
						istwitClick = false;
					}

				} else {
					// UIHelperUtil.showToastMessage("Yet to implement");
					signinTwitter();
				}
			}

			break;
		case R.id.share_facebook_btn:

			Session session = Session.getActiveSession();
			if (session != null && session.isOpened()) {
				if (!isfbClick) {
					imgCheckShareFb.setImageResource(R.drawable.cb_checked);
					isfbClick = true;
				} else {
					imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
					isfbClick = false;
				}
			} else {
				if (!Utils.chkStatus()) {
					Utils.networkAlertDialog(activity, getResources()
							.getString(R.string.toast_no_network));
				} else {
					isClicked = true;
					imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
				}
			}

			break;

		case R.id.share_by_txt_btn:
			Log.d("", "instagram");
			if (SocialNetworkingUtils.mApp != null
					&& SocialNetworkingUtils.mApp.hasAccessToken()) {
				if (!isinstaClick) {
					imgCheckShareBytxt.setImageResource(R.drawable.cb_checked);
					isinstaClick = true;
				} else {
					imgCheckShareBytxt
							.setImageResource(R.drawable.cb_unchecked);
					isinstaClick = false;
				}

			} else {
				if (SocialNetworkingUtils.mApp.hasAccessToken()) {
					SocialNetworkingUtils.mApp.resetAccessToken();
					imgCheckShareBytxt
							.setImageResource(R.drawable.cb_unchecked);
					// imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
					// imgCheckShareTwit.setImageResource(R.drawable.cb_unchecked);
				} else {

					if (!Utils.chkStatus()) {
						Utils.networkAlertDialog(activity, getResources()
								.getString(R.string.toast_no_network));
					} else {
						if (SocialNetworkingUtils.mApp != null) {
							if (SocialNetworkingUtils.mApp.hasAccessToken()) {
								SocialNetworkingUtils.mApp.resetAccessToken();
								// imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
								imgCheckShareBytxt
										.setImageResource(R.drawable.cb_unchecked);
								// imgCheckShareTwit.setImageResource(R.drawable.cb_unchecked);

							} else {
								SocialNetworkingUtils.mApp.authorize();
							}
						} else {
							// UIHelperUtil.showToastMessage("Unable to fetching credentials, Please try again");
						}
					}
				}
			}
			break;
		}
	}

	private void uploadPicture(View v) {
		if (!Utils.chkStatus()) {
			Utils.networkAlertDialog(activity,
					getResources().getString(R.string.toast_no_network));
			// showCloseAppDialogue();
		} else {
			serviceToUploadPhoto(v);
		}
	}

	/*
	 * private String getBase64Image() { if (!TextUtils.isEmpty(imgPath)) { if
	 * (TextUtils.isEmpty(base64Image)) { FileInputStream tmpInputStream = null;
	 * try {
	 * 
	 * Bitmap bitmap = ((BitmapDrawable) photo_iv.getDrawable()) .getBitmap();
	 * shareBitmap = bitmap; Log.d("koti","1"+shareBitmap);
	 * ByteArrayOutputStream byteArrayOutputStream = new
	 * ByteArrayOutputStream(); bitmap.compress(Bitmap.CompressFormat.PNG, 100,
	 * byteArrayOutputStream); byte[] byteArray =
	 * byteArrayOutputStream.toByteArray(); base64Image =
	 * Base64.encodeToString(byteArray, Base64.DEFAULT); return base64Image; }
	 * catch (Exception e) { e.printStackTrace(); } finally { try { if
	 * (tmpInputStream != null) { tmpInputStream.close(); } } catch (IOException
	 * e) { e.printStackTrace(); } } } else { return base64Image; } } return "";
	 * }
	 */

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

	/**************************** ------SERVER INTERACTION STARTS----- ****************************/
	/*
	 * private void serviceToUploadPhoto(final View view) {
	 * 
	 * taggedUsers = ""; if(listSearchInfo!=null){ for(int i = 0; i <
	 * listSearchInfo.size(); i++){ if( i == listSearchInfo.size() - 1){
	 * taggedUsers = taggedUsers+listSearchInfo.get(i).getID(); }else{
	 * taggedUsers = taggedUsers+listSearchInfo.get(i).getID()+","; } } } String
	 * url = Constants.common_url + getString(R.string.add_photo);
	 * Log.d("Koti",""+url+" "+SharedPreferencesUtils.getUserId()+" "+eventId
	 * +" "+photo_caption_et.getText().toString().trim()+" "+
	 * Constants.photo_media_type+" getBase64Images "+getBase64Image()
	 * +" taggedUsers: "+taggedUsers); List<NameValuePair> nameValuePairs = new
	 * ArrayList<NameValuePair>(); nameValuePairs.add(new
	 * BasicNameValuePair(Constants.userProf_userID,
	 * SharedPreferencesUtils.getUserId())); nameValuePairs.add(new
	 * BasicNameValuePair(Constants.Event_ID, eventId)); nameValuePairs.add(new
	 * BasicNameValuePair(Constants.photo_caption,
	 * photo_caption_et.getText().toString().trim())); nameValuePairs.add(new
	 * BasicNameValuePair(Constants.Event_media_type,
	 * Constants.photo_media_type)); nameValuePairs.add(new
	 * BasicNameValuePair(Constants.photo_media_file, getBase64Image()));
	 * nameValuePairs.add(new
	 * BasicNameValuePair(Constants.photo_media_tagged_users,taggedUsers));
	 * 
	 * //used for notifications if(TextUtils.isEmpty(eventId)){
	 * nameValuePairs.add(new BasicNameValuePair( Constants.TAG_CATEGORY,
	 * Constants.TAG_CATEGORY_PROFILE)); }else{ nameValuePairs.add(new
	 * BasicNameValuePair( Constants.TAG_CATEGORY,
	 * Constants.TAG_CATEGORY_EVENT)); } JsonParser.callBackgroundService(url,
	 * nameValuePairs, new JsonServiceListener() { ProgressDialog pd;
	 * 
	 * public void showProgress() { pd = new ProgressDialog(activity);
	 * pd.setMessage("Please wait. Uploading photo...");
	 * pd.setCancelable(false); pd.setCanceledOnTouchOutside(false); pd.show();
	 * }
	 * 
	 * public void hideProgress() { pd.dismiss(); }
	 * 
	 * @Override public void parseJsonResponse(String jsonResponse) {
	 * Log.v(Constants.logUrl, " Upload Photo RESPONSE: " + jsonResponse); if
	 * (jsonResponse != null) { JSONObject jsonObject; try { if (jsonResponse !=
	 * null) { jsonObject = new JSONObject(jsonResponse); JSONObject resObj =
	 * jsonObject .getJSONObject("Response"); String responseStr = resObj
	 * .getString("ResponseInfo"); if (responseStr != null && responseStr
	 * .equalsIgnoreCase("SUCCESS")) { mediaUrl = resObj.getString("media_url");
	 * Log.d("", "media url:"+mediaUrl); // Prompt successfull message to the //
	 * user UIHelperUtil .showToastMessage("Successfully uploaded");
	 * 
	 * if (istwitClick) { sharingToTwitter(view, shareBitmap); } else if
	 * (isfbClick) { Log.d("", "if facebook"); Session session = Session
	 * .getActiveSession(); if (session != null && session.isOpened()) {
	 * Log.d("", "koti facebook"); publishFeedDialog("Name: " + search_people_et
	 * .getText() .toString() + " Event: " + search_event_et .getText()
	 * .toString() .trim(), "", mediaUrl, activity); } } else if (isinstaClick)
	 * { sharingToInstagram(shareBitmap); } else { search_people_et.setText("");
	 * search_event_et.setText(""); eventId = "";
	 * 
	 * }
	 * 
	 * if(activity!=null){ activity.finish(); } } else { // Prompt validation
	 * message to the user UIHelperUtil .showToastMessage("Unable to upload. " +
	 * responseStr); } } } catch (Exception ex) { } } else { // Prompt
	 * validation message to the user UIHelperUtil
	 * .showToastMessage("Something went wrong with server!..."); }
	 * 
	 * } }); }
	 */

	private void serviceToUploadPhoto(final View view) {
		String url = Constants.common_url + getString(R.string.add_photo);
		Log.d("", "ImagePath:" + imgPath);
		String[] params = { url, imgPath };

		// Upload video file to server.
		new AsyncTask<String, Void, String>() {
			ProgressDialog pd;

			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(activity);
				pd.show();
				// pd.setMessage("Please wait.  Uploading photo...");
				pd.setMessage("Please wait.");
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
			}

			@Override
			protected String doInBackground(String... params) {
				return uploadFile(params[1], params[0]);
			}

			@Override
			protected void onPostExecute(String jsonResponse) {
				// UIHelperUtil.showToastMessage(jsonResponse);
				/*
				 * if (pd != null && pd.isShowing()) { pd.dismiss(); }
				 */
				Log.v(Constants.logUrl, " Upload Photo RESPONSE: "
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
									&& responseStr.equalsIgnoreCase("SUCCESS")) {
								mediaUrl = resObj.getString("media_url");
								Log.d("", "media url:" + mediaUrl);
								// Prompt successfull message to the
								// user
								UIHelperUtil
										.showToastMessage("Successfully uploaded");

								if (TextUtils.isEmpty(shareString)) {
									shareString = "Add photo to his profile"
											+ ". For more details please visit http://www.mysportsshare.com";
								} else {
									shareString = "Add photo to an event "
											+ eventType
											+ ". For more details please visit http://www.mysportsshare.com";
								}

								if (istwitClick) {
									sharingToTwitter(view, shareBitmap, pd);
								} else if (isfbClick) {
									Log.d("", "if facebook");
									Session session = Session
											.getActiveSession();
									if (session != null && session.isOpened()) {
										Log.d("", "koti facebook");
										publishFeedDialog(shareString, "",
												mediaUrl, activity, pd);
									}
								} else if (isinstaClick) {
									sharingToInstagram(shareBitmap, pd);
								} else {
									exitActivity(pd);
									/*
									 * search_people_et.setText("");
									 * search_event_et.setText(""); eventId =
									 * ""; if(activity!=null){
									 * activity.finish(); }
									 */

								}

								/*
								 * if(activity!=null){ activity.finish(); }
								 */
							} else {
								// Prompt validation message to the user
								if (pd != null && pd.isShowing()) {
									pd.dismiss();
								}
								UIHelperUtil
										.showToastMessage("Unable to upload. "
												+ responseStr);
							}
						}
					} catch (Exception ex) {
					}
				} else {
					// Prompt validation message to the user
					UIHelperUtil
							.showToastMessage("Something went wrong with server!...");
				}

			};
		}.execute(params);
	}

	/**************************** ------SERVER INTERACTION ENDS----- ****************************/

	// Uploading video file using multipart/form-data
	private String uploadFile(String sourceUri, String ServerUrl) {
		String responseStr = "";

		taggedUsers = "";
		if (listSearchInfo != null) {
			for (int i = 0; i < listSearchInfo.size(); i++) {
				if (i == listSearchInfo.size() - 1) {
					taggedUsers = taggedUsers + listSearchInfo.get(i).getID();
				} else {
					taggedUsers = taggedUsers + listSearchInfo.get(i).getID()
							+ ",";
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
			if (TextUtils.isEmpty(photo_caption_et.getText())) {
				entity.addPart("post_content", new StringBody(""));
			} else {
				entity.addPart("post_content", new StringBody(photo_caption_et
						.getText().toString().trim()));
			}
			entity.addPart("media_type", new StringBody(
					Constants.photo_media_type));
			entity.addPart("tagged_users", new StringBody(taggedUsers));
			// entity.addPart("media_file",new StringBody(""));
			// used for notifications
			if (TextUtils.isEmpty(eventId)) {
				// entity.addPart(Constants.TAG_CATEGORY, new
				// StringBody("PROFILE"));
				entity.addPart(Constants.TAG_CATEGORY,
						new StringBody("profile"));
			} else {
				// entity.addPart(Constants.TAG_CATEGORY, new
				// StringBody("EVENT"));
				entity.addPart(Constants.TAG_CATEGORY, new StringBody("event"));
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
				responseStr = "Error occurred! Http Status Code: " + statusCode;
			}
		} catch (Exception ex) {
			Log.w("----catch (Exception ex) ---", " ");
			ex.printStackTrace();
			// Exception handling
		}
		return responseStr;
	}

	@Override
	public void onResume() {
		super.onResume();

		title.setText(Constants.UI_UPLOAD_PHOTO);
		Log.d("", "onresume event type:" + eventType);
		setDynamicView();

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

		Session session = Session.getActiveSession();
		if (session != null && session.isOpened() && isfbClick) {
			imgCheckShareFb.setImageResource(R.drawable.cb_checked);
			Log.d("", "facebook");
		} else {
			imgCheckShareFb.setImageResource(R.drawable.cb_unchecked);
		}

		if (SocialNetworkingUtils.mTwitter != null) {
			if (SocialNetworkingUtils.mTwitter.getAccessToken() != null
					&& istwitClick) {
				imgCheckShareTwit.setImageResource(R.drawable.cb_checked);
				Log.d("", "twitter");
			} else {
				imgCheckShareTwit.setImageResource(R.drawable.cb_unchecked);
			}
		}

		if (SocialNetworkingUtils.mApp != null) {
			if (SocialNetworkingUtils.mApp.hasAccessToken() && isinstaClick) {
				imgCheckShareBytxt.setImageResource(R.drawable.cb_checked);
				Log.d("", "instagram");
			} else {
				imgCheckShareBytxt.setImageResource(R.drawable.cb_unchecked);
			}
		}

		if (SocialNetworkingUtils.uiHelper != null) {
			SocialNetworkingUtils.uiHelper.onResume();
		}
	}

	private void setDynamicView() {

		if (!eventType.matches("")) {
			search_event_et.setVisibility(View.GONE);
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
			// tvName.setPadding(3, 3, 3, 3);
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
			if (!isFromHome) {
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

			// search_event_et.setText(eventType);
		}

	}

	public void setEventTypeAndId(String type, String id) {
		eventType = type;
		eventId = id;
		Log.d("", "method event type:" + eventType);
		// isCallOnresume = true;
	}

	public void setSearchInfoList(List<SearchInfo> searchlist) {
		listSearchInfo = searchlist;
		// isCallOnresume = true;
		Log.d("", "koti search people list");
	}

	// facebook
	@Override
	public void onPause() {
		super.onPause();
		if (SocialNetworkingUtils.uiHelper != null) {
			SocialNetworkingUtils.uiHelper.onPause();
		}
		// isCallOnresume = false;
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
			String eventPicUrl, final Context context, final ProgressDialog pd) {
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
									sharingToInstagram(shareBitmap, pd);
								} else {
									// activity.finish();
									/*
									 * search_people_et.setText("");
									 * search_event_et.setText(""); eventId =
									 * ""; if(activity!=null){
									 * activity.finish(); }
									 */
									exitActivity(pd);
								}
							} else {
								// User clicked the Cancel button
								Toast.makeText(context.getApplicationContext(),
										"Facebook publish cancelled",
										Toast.LENGTH_SHORT).show();
								if (isinstaClick) {
									sharingToInstagram(shareBitmap, pd);
								} else {
									// activity.finish();
									exitActivity(pd);
								}

							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(context,
									"Facebook publish cancelled",
									Toast.LENGTH_SHORT).show();
							if (isinstaClick) {
								sharingToInstagram(shareBitmap, pd);
							} else {
								// activity.finish();
								exitActivity(pd);
							}
						} else {
							// Generic, ex: network error
							Toast.makeText(context, "Facebook error posting ",
									Toast.LENGTH_SHORT).show();
							if (isinstaClick) {
								sharingToInstagram(shareBitmap, pd);
							} else {
								// activity.finish();
								exitActivity(pd);
							}
						}
					}

				}).build();
		feedDialog.show();

	}

	/**
	 * AsyncTask Downloading pic from urls for twitter.
	 * 
	 * @author koti
	 * 
	 */
	private void sharingToTwitter(View view, Bitmap shareBmp,
			final ProgressDialog pd) {
		File file;
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/twitter_post_pic");
		myDir.mkdirs();
		String fname = "event_pic.jpg";
		file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			shareBmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			SharedPreferences mSharedPref = activity.getSharedPreferences(
					"Android_Twitter_Preferences", Context.MODE_PRIVATE);
			String filename = file.getAbsolutePath();
			if (mSharedPref.getString(Constants.KEY_TWITTER_ACCESS_TOKEN, null) != null) {
				HelperMethods.postToTwitterWithImage(activity,
						((Activity) activity), filename, shareString,
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
											publishFeedDialog(shareString, "",
													mediaUrl, activity, pd);
										}
									} else if (isinstaClick) {
										sharingToInstagram(shareBitmap, pd);
									} else {
										exitActivity(pd);
									}
								} else {
									Toast.makeText(activity, "posted failed",
											Toast.LENGTH_SHORT).show();
									if (isfbClick) {
										Session session = Session
												.getActiveSession();
										if (session != null
												&& session.isOpened()) {
											publishFeedDialog(shareString, "",
													mediaUrl, activity, pd);
										}
									} else if (isinstaClick) {
										sharingToInstagram(shareBitmap, pd);
									} else {
										exitActivity(pd);
									}
								}

							}
						});
			} else {
				// Toast.makeText(activity, "session is null",
				// Toast.LENGTH_SHORT).show();
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				SocialNetworkingUtils.signinTwitter(view,
						SocialNetworkingUtils.mTwitter, shareString,
						twitterEventPic, activity);
			}
		} catch (Exception ex) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			Toast.makeText(activity, "Please try again", Toast.LENGTH_SHORT)
					.show();
		}

	}

	/**
	 * AsyncTask Downloading pic from url for instagram.
	 * 
	 * @author koti
	 * 
	 */
	private void sharingToInstagram(Bitmap bmp, ProgressDialog pd) {
		File file;
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

		try {
			String filename = file.getAbsolutePath();
			Intent intent = activity.getPackageManager()
					.getLaunchIntentForPackage("com.instagram.android");

			if (intent != null) {

				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.setPackage("com.instagram.android");
				try {
					shareIntent.putExtra(Intent.EXTRA_STREAM, Uri
							.parse(MediaStore.Images.Media.insertImage(
									activity.getContentResolver(), filename,
									"", "")));
					shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				shareIntent.setType("image/jpeg");

				startActivity(shareIntent);
				// activity.finish();
				exitActivity(pd);
			} else {
				exitActivity(pd);
				UIHelperUtil.showToastMessage("Please install Instagram app");
				// bring user to the market to download the app.
				// or let them choose an app?
			/*	intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse("market://details?id="+"com.instagram.android"));
				startActivity(intent);*/
				// activity.finish();

			}

		} catch (Exception ex) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			Toast.makeText(activity, "Failed sharing to instagram",
					Toast.LENGTH_SHORT).show();
			// activity.finish();
		}
	}

	private void addInvitedItem(final SearchInfo searchItem) {
		FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(5, 5, 5, 5);

		int color = Color.TRANSPARENT;
		TextView selectedUserName = new TextView(activity);
		selectedUserName.setBackgroundColor(color);
		// tvName.setBackgroundResource(R.drawable.input_box);
		selectedUserName.setText(searchItem.getName() + "");
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
		/*
		 * if (!isSearchItemExists(searchItem)) {
		 * flowLayout.addView(selectedUserLayout); CustomLog.i("AddItem", "Add "
		 * + searchItem.getID() + " : " + flowLayout.getChildCount() + " : " +
		 * isSearchItemExists(searchItem)); } else { CustomLog.v("AddItem",
		 * "Add " + searchItem.getID() + " : " + flowLayout.getChildCount() +
		 * " : " + isSearchItemExists(searchItem)); }
		 */
		llytmultiPeple.addView(selectedUserLayout);
		imgRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeSeletedItem(searchItem);
			}
		});

		/*
		 * if (selectedInvitesUsersList != null &&
		 * selectedInvitesUsersList.size() > 0) { removeSeletedItem(searchItem);
		 * }
		 */
	}

	private void setSearchList(List<SearchInfo> searchList) {
		this.selectedInvitesUsersList = searchList;
	}

	private void getInviteUserInStringFormat(
			List<SearchInfo> selectedInvitesUsersList) {
		StringBuffer selectedItems = new StringBuffer();
		for (int i = 0; i < selectedInvitesUsersList.size(); i++) {
			if (i < selectedInvitesUsersList.size() - 1) {
				selectedItems.append(selectedInvitesUsersList.get(i).getID()
						+ ",");
			} else {
				selectedItems.append(selectedInvitesUsersList.get(i).getID());
			}
		}
		invitedUsers = selectedItems.toString();
		CustomLog.v("",
				"Selected Users: " + " : " + selectedInvitesUsersList.size()
						+ " : Ids: " + invitedUsers);
	}

	private void removeSeletedItem(SearchInfo searchItem) {

		if (isSearchItemExists(searchItem)) {
			try {
				int index = getIndexItem(searchItem);
				selectedInvitesUsersList.remove(index);
				llytmultiPeple.removeView(getLastView(index));
				if (selectedInvitesUsersList.size() == 0) {
					llytmultiPeple.addView(search_people_et);
				}
				CustomLog.v("Index exists", "index: " + index + " : "
						+ selectedInvitesUsersList.size());
			} catch (Exception e) {
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

	private void exitActivity(ProgressDialog pd) {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}

		search_people_et.setText("");
		search_event_et.setText("");
		eventId = "";
		if (activity != null) {
			activity.finish();
		}

	}
}
