package com.mysportsshare.mysportscast.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.londatiga.android.twitter.Twitter;
import net.londatiga.android.twitter.TwitterRequest;
import net.londatiga.android.twitter.TwitterUser;
import net.londatiga.android.twitter.oauth.OauthAccessToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import br.com.dina.oauth.instagram.InstagramApp;
import br.com.dina.oauth.instagram.InstagramApp.OAuthAuthenticationListener;

import com.crashlytics.android.Crashlytics;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.instagram.ApplicationData;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.parser.ServiceHandler;
import com.mysportsshare.mysportscast.twitteractivity.BaseActivity;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.SocialNetworkingUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;
import com.sromku.simple.fb.SimpleFacebook;

public class HomeActivity extends BaseActivity{

	//twitter variables
	//	private Twitter mTwitter;
	public static final String CONSUMER_KEY = "ZYj95YLog8RkDGTkmEVvnCRVM";
	public static final String CONSUMER_SECRET = "AI7z8yRmTuUIyUTOgeC4pLVQ45LNMxngvpMkzVC0KXEipTgIfm";
	public static final String CALLBACK_URL = "https://dev.twitter.com/";

	/*public static final String CONSUMER_KEY = "cw6j3Q586Ru46JHEUgCwNg";
	public static final String CONSUMER_SECRET = "ZSd3frxfbZfpZShnT1uyI4HfowE915H0SBL4piieyg";
	public static final String CALLBACK_URL = "https://dev.twitter.com/";*/
	//instagram
	//	private InstagramApp mApp;
	//facebook variables
	private LoginButton loginBtn;
	private boolean isClicked;
	private String userId;
	//	private UiLifecycleHelper uiHelper;
	private Session s1;
	private Button instagramBtn;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		MySportsApp.setActivityContext(HomeActivity.this);
		
		init();
		if(!Utils.chkStatus()){
			Utils.networkAlertDialog(HomeActivity.this,getResources().getString(R.string.toast_no_network));
			//			showCloseAppDialogue();
		}else{
			SocialNetworkingUtils.mApp.setListener(listener);
			SocialNetworkingUtils.uiHelper.onCreate(savedInstanceState);
			//			loginBtn.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
			loginBtn.setReadPermissions(Arrays.asList("email"));
			loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {

				@Override
				public void onUserInfoFetched(GraphUser user) {
					if (user != null) {
						userId = user.getId();
						String email = (String) user.getProperty("email");
						String profileImg = "http://graph.facebook.com/"+userId+"/picture?type=large&redirect=false";
						Log.d("","profile: "+profileImg);
						//						new ProfilePic().execute(profileImg);
						//					Log.v("", "Session user: "+ user);
						if (isClicked) {
							if(!Utils.chkStatus()){
								Utils.networkAlertDialog(HomeActivity.this,getResources().getString(R.string.toast_no_network));
							}else{
								serviceToRegisterUserBySocial("facebook",user.getName(),userId,email,"fb");
							}

						} else {
							Log.i("", "Session isClicked: " + isClicked);
						}
						Log.v("", "user: " + user.getName() + " : " + user.getId());	


					} else {
						Log.i("", "Session user: " + user);
						//						UIHelperUtil.showToastMessage("Unable to fetch login details");
					}
				}
			});

			loginBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if(!Utils.chkStatus()){
						Utils.networkAlertDialog(HomeActivity.this,getResources().getString(R.string.toast_no_network));
					}else{
						isClicked = true;
					}

					//				Session.openActiveSession(LoginActivity.this, true, statusCallback);
				}
			});

			if(SocialNetworkingUtils.mApp != null){
				if (SocialNetworkingUtils.mApp.hasAccessToken()) {
					SocialNetworkingUtils.mApp.resetAccessToken();
				}
			}

		}

	}



	OAuthAuthenticationListener listener = new OAuthAuthenticationListener() {

		@Override
		public void onSuccess() {
			//			instagramBtn.setText("Disconnect");
			Log.d("","instagram id: "+SocialNetworkingUtils.mApp.getId()+" "+SocialNetworkingUtils.mApp.getName()+" "+SocialNetworkingUtils.mApp.getProfilePic());
			if(!Utils.chkStatus()){
				Utils.networkAlertDialog(HomeActivity.this,getResources().getString(R.string.toast_no_network));
			}else{
				serviceToRegisterUserBySocial("instagram",SocialNetworkingUtils.mApp.getName(),SocialNetworkingUtils.mApp.getId(),SocialNetworkingUtils.mApp.getUserName(),"insta");

			}
		}

		@Override
		public void onFail(String error) {
			Toast.makeText(HomeActivity.this, error, Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * click event for instagram login button
	 */
	public void clickLoginInstagram(View view){

		if(!Utils.chkStatus()){
			Utils.networkAlertDialog(HomeActivity.this,getResources().getString(R.string.toast_no_network));
		}else{
			if(SocialNetworkingUtils.mApp != null){
				if (SocialNetworkingUtils.mApp.hasAccessToken()) {
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							HomeActivity.this);
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
				//				UIHelperUtil.showToastMessage("Unable to fetching credentials, Please try again");
			}
		}

	}

	/**
	 * initialize variables
	 */
	private void init(){
		instagramBtn = (Button)findViewById(R.id.btn_instagram);
		//instagram
		SocialNetworkingUtils.mApp = new InstagramApp(this, ApplicationData.CLIENT_ID,
				ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
		loginBtn = (LoginButton) findViewById(R.id.btn_facebook);
		//twitter
		SocialNetworkingUtils.mTwitter = new Twitter(this, CONSUMER_KEY, CONSUMER_SECRET, CALLBACK_URL);
		//facebook
		SocialNetworkingUtils.uiHelper = new UiLifecycleHelper(this, statusCallback);
	}
	//click event for twitter login
	public void loginTwitter(View view){
		if(!Utils.chkStatus()){
			Utils.networkAlertDialog(HomeActivity.this,getResources().getString(R.string.toast_no_network));
		}else{
			signinTwitter();
		}
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

	private void getCredentials() {
		final ProgressDialog progressDlg = new ProgressDialog(this);

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
					saveCredential(user.screenName, user.name, user.profileImageUrl);
					if(!Utils.chkStatus()){
						Utils.networkAlertDialog(HomeActivity.this,getResources().getString(R.string.toast_no_network));
					}else{
						serviceToRegisterUserBySocial("twitter",user.name,user.userId,"","twit");
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		/*SocialNetworkingUtils.mApp.resetAccessToken();
		SocialNetworkingUtils.mTwitter.clearSession();
		SocialNetworkingUtils.uiHelper.onDestroy();*/
		clearCredential();
	}

	//facebook 
	private com.facebook.Session.StatusCallback statusCallback = new StatusCallback() {

		@Override
		public void call(com.facebook.Session session, SessionState state, Exception exception) {
			//			Log.d("", "Session status call back: " + session + " :state: " + state + " : " + session.getApplicationId());
			if(session != null){
				if (state.isOpened()) {
					s1 = session;
					session.removeCallback(statusCallback);
					Log.i("", "Session Facebook is opened: ");

				} else {
					Log.i("", "Session Facebook is closed");
					//				session.closeAndClearTokenInformation();
				}
			}
		}
	};
	//facebook 
	protected void onResume() {
		super.onResume();
		if(SocialNetworkingUtils.uiHelper != null){
			SocialNetworkingUtils.uiHelper.onResume();
		}


	};
	//facebook 
	@Override
	protected void onPause() {
		super.onPause();
		if(SocialNetworkingUtils.uiHelper != null){
			SocialNetworkingUtils.uiHelper.onPause();
		}
		/*if(SocialNetworkingUtils.mApp != null){
			SocialNetworkingUtils.mApp.resetAccessToken();
		}*/


	}
	//facebook 
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(SocialNetworkingUtils.uiHelper != null){
			SocialNetworkingUtils.uiHelper.onSaveInstanceState(outState);
		}

	}
	//facebook 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(SocialNetworkingUtils.uiHelper != null){
			SocialNetworkingUtils.uiHelper.onActivityResult(requestCode, resultCode, data);
			SocialNetworkingUtils.mSimpleFacebook = SimpleFacebook.getInstance(getActivity());
			if(SocialNetworkingUtils.mSimpleFacebook !=null){
				SocialNetworkingUtils.mSimpleFacebook.onActivityResult(getActivity(), requestCode, resultCode, data);
				Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
			}
		}

	}

	/**
	 * this method will call register the user
	 * http://182.75.34.62/MySportsShare/web_services/register_social.php
	 * reg_type=facebook&name=Test&oauth_id=9438378473683638&email=syedsulemans@sparshcom.net
	 */

	private void serviceToRegisterUserBySocial(String regType,final String userName,final String oauthToken,String email,final String type){

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
				pd = new ProgressDialog(HomeActivity.this);
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
					String profileImg = "";
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject.getJSONObject("Response");
						String responseStr = resObj.getString("ResponseInfo");
						if (responseStr!=null && responseStr.equalsIgnoreCase("SUCCESS")) {
							userId = resObj.getString("user_id");
							//							userName = resObj.getString("username");
							profileImg = resObj.getString("profile_image");
							startActivity(new Intent(getActivity(), MainActivity.class));
							finish();
						} else if(responseStr!=null && responseStr.equalsIgnoreCase("EMAIL ALREADY EXIST")){
							userId = resObj.getString("user_id");
							profileImg = resObj.getString("profile_image");
							startActivity(new Intent(getActivity(), MainActivity.class));
							finish();
							System.out.println("responce: " + responseStr);
						}else{
							UIHelperUtil.showToastMessage(getString(R.string.service_toast));
						}
						SharedPreferencesUtils.setUserId(userId);
						SharedPreferencesUtils.setLoginType(type);
						SharedPreferencesUtils.setUserName(userName);
						SharedPreferencesUtils.setOauthUserId(oauthToken);
						SharedPreferencesUtils.setUserLoginViaSocialMedia("true");
						SharedPreferencesUtils.setUserProfilePicPath(profileImg);
						Log.d("","User Id"+SharedPreferencesUtils.getUserId());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					UIHelperUtil.showToastMessage(getString(R.string.service_toast));
				}
			}
		});

	}

	/**
	 * click event for email button for login
	 */

	public void clickEmailRegister(View view){
		startActivity(new Intent(getActivity(), LoginActivity.class));
	}

	/**
	 * click event for register activity
	 */
	public void clickToRegister(View view){
		startActivity(new Intent(getActivity(),RegisterActivity.class));
	}

	/**
	 * getting image from facebook.
	 *
	 */
	public class ProfilePic extends AsyncTask<String, Void, String>{

		ProgressDialog pDialog = null;
		String image = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(HomeActivity.this);
			pDialog.setTitle("Please Wait...");
			pDialog.setMessage("Requesting");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(params[0], ServiceHandler.GET);
			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					JSONObject jsonObj1 = jsonObj.getJSONObject("data");
					for (int i = 0; i < jsonObj1.length(); i++) {		
						image = jsonObj1.getString("url");
						Log.d("", "URL Image: " + image);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				UIHelperUtil.showToastMessage("Couldn't get any data from the facebook");
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
			//			new DownloadImageTask().execute(image);
		}

	}


	public static String getAccessToken(Context ctx){
		SharedPreferences mSharedPref = ctx.getSharedPreferences("Android_Twitter_Preferences", Context.MODE_PRIVATE);
		return mSharedPref.getString(Constants.KEY_TWITTER_ACCESS_TOKEN, null);
	}

	public static String getAccessTokenSecret(Context ctx){
		SharedPreferences mSharedPref = ctx.getSharedPreferences("Android_Twitter_Preferences", Context.MODE_PRIVATE);
		return mSharedPref.getString(Constants.KEY_TWITTER_ACCESS_TOKEN_SECRET, null);
	}
}
