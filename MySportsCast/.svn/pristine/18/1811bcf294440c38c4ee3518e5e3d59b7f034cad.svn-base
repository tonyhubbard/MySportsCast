package com.mysportsshare.mysportscast.fragments;

import java.util.Arrays;

import net.londatiga.android.twitter.Twitter;
import net.londatiga.android.twitter.TwitterRequest;
import net.londatiga.android.twitter.TwitterUser;
import net.londatiga.android.twitter.oauth.OauthAccessToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.dina.oauth.instagram.InstagramApp;
import br.com.dina.oauth.instagram.InstagramApp.OAuthAuthenticationListener;

import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.HomeActivity;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.activity.StartActivity;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.instagram.ApplicationData;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.SocialNetworkingUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class LinkedAccountsFragment extends Fragment{

	/*
	 * views
	 */
	private Button twitterBtn;
	private Button instagramBtn;
	private LoginButton loginBtn;
	private CheckBox facebookCb;
	private CheckBox twitterCb;
	private CheckBox instagramCb;

	//facebook variables
	private String userId;
	private boolean isClicked;
	//instagram
	//	private InstagramApp mApp;
	Activity activity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Make sure that container activity implement the callback interface
		try {
			this.activity = activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement DataPassListener");
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {				
		View view = inflater.inflate(R.layout.fragments_linked_accounts,container,false);
		init(view);
		ImageView backBtn = (ImageView) activity.findViewById(R.id.back_iv);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) activity).onBackPressed();
			}
		});
		if(!Utils.chkStatus()){
			Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
		}else{
			SocialNetworkingUtils.mApp.setListener(listener);
			SocialNetworkingUtils.uiHelper = new UiLifecycleHelper(activity, statusCallback);
			SocialNetworkingUtils.uiHelper.onCreate(savedInstanceState);
		}

		SocialNetworkingUtils.canPresentShareDialog = FacebookDialog.canPresentShareDialog(activity,
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG);

		loginBtn.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		//		loginBtn.setFragment(this);
		loginBtn.setReadPermissions(Arrays.asList("email"));
		loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {

			@Override
			public void onUserInfoFetched(GraphUser user) {
				Log.i("", "Session user: " + user);
				if (user != null) {
					userId = user.getId();
					if (isClicked) {
						if(!Utils.chkStatus()){
							Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
						}else{
							Log.i("", "Session isClicked: " + isClicked);
							facebookCb.setChecked(true);
						}

					} else {
						Log.i("", "Session isClicked: " + isClicked);
					}
					Log.v("", "user: " + user.getName() + " : " + user.getId());	
				} else {
					Log.i("", "Session user: " + user);
					facebookCb.setChecked(false);
					if("fb".equalsIgnoreCase(SharedPreferencesUtils.getLoginType())){
						logOut();
					}
					//UIHelperUtil.showToastMessage("Unable to fetch login details");
				}
			}
		});

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!Utils.chkStatus()){
					Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
				}else{
					isClicked = true;
					//					facebookCb.setChecked(false);
				}
			}
		});
		instagramBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if(SocialNetworkingUtils.mApp.hasAccessToken()){

					final AlertDialog.Builder builder = new AlertDialog.Builder(
							activity);
					if("insta".equalsIgnoreCase(SharedPreferencesUtils.getLoginType())){
						builder.setMessage("Do you want to log out?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog, int id) {
								SocialNetworkingUtils.mApp.resetAccessToken();
								logOut();
								//					instagramBtn.setText("Connect");
							}
						})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
					}else{
						builder.setMessage("Disconnect from Instagram?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog, int id) {
								SocialNetworkingUtils.mApp.resetAccessToken();
								instagramCb.setChecked(false);
								//					instagramBtn.setText("Connect");
							}
						})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
					}

					final AlertDialog alert = builder.create();
					alert.show();

				}else{
					if(!Utils.chkStatus()){
						Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
					}else{
						if(SocialNetworkingUtils.mApp!=null){
							if (SocialNetworkingUtils.mApp.hasAccessToken()) {
								final AlertDialog.Builder builder = new AlertDialog.Builder(
										activity);
								builder.setMessage("Disconnect from Instagram?")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog, int id) {
										SocialNetworkingUtils.mApp.resetAccessToken();
										//					instagramBtn.setText("Connect");
									}
								})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
								final AlertDialog alert = builder.create();
								alert.show();
							} else {
								SocialNetworkingUtils.mApp.authorize();
							}
						}else{
							//						UIHelperUtil.showToastMessage("Unable to fetching credentials, Please try again");
						}
					}
				}


			}
		});
		twitterBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(SocialNetworkingUtils.mTwitter != null){
					signinTwitter();
				}

			}
		});

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		TextView title = (TextView) activity.findViewById(
				R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);
		title.setText("Linked Accounts");
		ImageView settingIv = (ImageView)activity.findViewById(R.id.setting_iv);
		settingIv.setVisibility(View.GONE);

		ImageView searchIv = (ImageView)activity.findViewById(R.id.search_iv);
		searchIv.setVisibility(View.GONE);

		ImageView backIv = (ImageView)activity.findViewById(R.id.back_iv);
		backIv.setVisibility(View.VISIBLE);

		ImageView addaneventIv = (ImageView)activity.findViewById(R.id.add_an_event_iv);
		addaneventIv.setVisibility(View.GONE);

		//facebook
		if(SocialNetworkingUtils.uiHelper != null){
			SocialNetworkingUtils.uiHelper.onResume();
			Session session = Session.getActiveSession();
			if (session != null && session.isOpened()) {
				facebookCb.setChecked(true);
				Log.d("","facebook");
			}else{
				facebookCb.setChecked(false);
			}
		}

		if(SocialNetworkingUtils.mTwitter != null){
			if(SocialNetworkingUtils.mTwitter.getAccessToken() != null){
				twitterCb.setChecked(true);
				Log.d("","twitter");
			}else{
				twitterCb.setChecked(false);
			}
		}

		if(SocialNetworkingUtils.mApp != null){
			if(SocialNetworkingUtils.mApp.hasAccessToken()){
				instagramCb.setChecked(true);
				Log.d("","instagram");
			}else{
				instagramCb.setChecked(false);
			}
		}
	}
	private void init(View view){
		//twitter
		SocialNetworkingUtils.mTwitter = new Twitter(activity, HomeActivity.CONSUMER_KEY, HomeActivity.CONSUMER_SECRET, HomeActivity.CALLBACK_URL);
		loginBtn = (LoginButton)view.findViewById(R.id.btn_facebook_share);
		twitterBtn = (Button)view.findViewById(R.id.btn_twtr_share);
		instagramBtn = (Button)view.findViewById(R.id.btn_instagram_share);
		facebookCb = (CheckBox)view.findViewById(R.id.facebook_cb);
		twitterCb = (CheckBox)view.findViewById(R.id.twitter_cb);
		instagramCb = (CheckBox)view.findViewById(R.id.instagram_cb);

		SocialNetworkingUtils.mApp = new InstagramApp(activity, ApplicationData.CLIENT_ID,
				ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Session.getActiveSession().onActivityResult(activity, requestCode, resultCode, data);
		SocialNetworkingUtils.mSimpleFacebook.onActivityResult(activity, requestCode, resultCode, data);

	}

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
			//			instagramBtn.setText("Disconnect");
			Log.d("","instagram id: "+SocialNetworkingUtils.mApp.getId()+" "+SocialNetworkingUtils.mApp.getName()+" "+SocialNetworkingUtils.mApp.getProfilePic());
			if(!Utils.chkStatus()){
				Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
			}else{
				instagramCb.setChecked(true);
			}
		}

		@Override
		public void onFail(String error) {
			Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
		}
	};

	private void signinTwitter() {
		if(SocialNetworkingUtils.mTwitter.getAccessToken() != null){

			final AlertDialog.Builder builder = new AlertDialog.Builder(
					activity);
			if("twit".equalsIgnoreCase(SharedPreferencesUtils.getLoginType())){

				builder.setMessage("Do you want to log out?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialog, int id) {
						SocialNetworkingUtils.mTwitter.clearSession();
						logOut();
					}
				})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

			}else{
				builder.setMessage("Disconnect from Twitter?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialog, int id) {
						SocialNetworkingUtils.mTwitter.clearSession();
						twitterCb.setChecked(false);
					}
				})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
			}

			final AlertDialog alert = builder.create();
			alert.show();
		}else{
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

	public void showToast(String text) {
		Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
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
					//				showToast("Hello " + user.name+" "+user.toString());
					Log.d("","user details: "+user.description +" "+user.screenName+" "+user.userId+" "+user.hashCode()+" "+user.profileImageUrl);
					if(!Utils.chkStatus()){
						Utils.networkAlertDialog(activity,getResources().getString(R.string.toast_no_network));
					}else{
						twitterCb.setChecked(true);
					}
				}else{
					//					UIHelperUtil.showToastMessage("Unable to fetch login detals. Please try again");
				}

			}

			@Override
			public void onError(String error) {
				progressDlg.dismiss();
				showToast(error);
			}
		});
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
	 *  method for logout
	 */
	private void logOut(){
		if(SharedPreferencesUtils.getUserId() != null && SharedPreferencesUtils.getUserId().length() > 0) {
			SharedPreferencesUtils.setUserName("");
			SharedPreferencesUtils.setUserId("");
			SharedPreferencesUtils.setUserProfilePicPath("");
			SharedPreferencesUtils.setLoginType("");
			SharedPreferencesUtils.setOauthUserId("");
			activity.finish();
			Intent i = new Intent(activity, StartActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			UIHelperUtil.showToastMessage("You are now logged out");
			Utils.unRegisterForGCM(activity);
		}
	}
}
