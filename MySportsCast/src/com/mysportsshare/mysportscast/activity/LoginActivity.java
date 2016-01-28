package com.mysportsshare.mysportscast.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class LoginActivity extends Activity implements OnClickListener{
	//views
	private EditText emailEt;
	private EditText passwordEt;
	private TextView loginResetPwdLink;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		MySportsApp.setActivityContext(LoginActivity.this);
		
		findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		init();
	}

	/**
	 * init method for initialization
	 */
	private void init(){
		emailEt  = (EditText)findViewById(R.id.user_email_et);
		passwordEt = (EditText)findViewById(R.id.login_pwd_et);
		loginResetPwdLink = (TextView) findViewById(R.id.login_reset_pwd_link);
		loginResetPwdLink.setOnClickListener(this);
	}

	/**
	 * click event for login button
	 */
	public void clickLogin(View view){
		if(!emailEt.getText().toString().matches("")){
			if(Utils.isValidEmail(emailEt.getText().toString())){
				if(passwordEt.getText().toString().length() > 0){
					if(Utils.chkStatus()){
						serviceForLogin(emailEt.getText().toString(), passwordEt.getText().toString());
					}else{
						Utils.networkAlertDialog(LoginActivity.this,getResources().getString(R.string.toast_no_network));
					}
				}else{
					UIHelperUtil.showToastMessage(getResources().getString(R.string.empty_pwd));
				}
			}else{
				UIHelperUtil.showToastMessage(getResources().getString(R.string.invalid_email));
			}
		}else{
			UIHelperUtil.showToastMessage(getResources().getString(R.string.empty_email));
		}
	}

	/**
	 * method for calling login service
	 */

	private void serviceForLogin(String email,String pwd){
		String url = Constants.common_url + getString(R.string.user_login);
		Log.i("", "URL login: " + url+"  "+email+"  "+pwd);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("pass", pwd));

		JsonParser.callBackgroundService(url, nameValuePairs, new JsonServiceListener() {
			ProgressDialog pd;
			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(LoginActivity.this);
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
				Log.d(Constants.logUrl,"RESPONSE: " + jsonResponse);
				if (jsonResponse!= null) {
					JSONObject jsonObject;
					String userId = "";
					String userName = "";
					String profileImg = "";
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject.getJSONObject("Response");
						String responseStr = resObj.getString("ResponseInfo");
						if (responseStr!=null && responseStr.equalsIgnoreCase("SUCCESS")) {
							userId = resObj.getString("user_id");
							userName = resObj.getString("first_name");
							profileImg = resObj.getString("profile_image");
							Intent loginintent = new Intent(LoginActivity.this, MainActivity.class);
							loginintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(loginintent);
							finish();
						} else if(responseStr!=null && responseStr.equalsIgnoreCase("EMAIL ALREADY EXIST")){
							userId = SharedPreferencesUtils.getUserId();
							Intent loginintent = new Intent(LoginActivity.this, MainActivity.class);
							loginintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(loginintent);
							finish();
							System.out.println("responce: " + responseStr);
						}else if(responseStr!=null && responseStr.equalsIgnoreCase("FAILURE")){
							UIHelperUtil.showToastMessage(getString(R.string.invalid_cred_toast));
						}else{
							UIHelperUtil.showToastMessage(getString(R.string.service_toast));
						}
						SharedPreferencesUtils.setUserId(userId);
						SharedPreferencesUtils.setUserName(userName);
						SharedPreferencesUtils.setUserProfilePicPath(profileImg);
						SharedPreferencesUtils.setUserLoginViaSocialMedia("false");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					UIHelperUtil.showToastMessage(getString(R.string.service_toast));
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.login_reset_pwd_link){
			Intent forgetPwdIntent = new Intent(this, ForgetPasswordActivity.class);
			startActivity(forgetPwdIntent);
		}
	}
}
