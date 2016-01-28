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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.models.ForgetPasswordBean;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class ForgetPasswordActivity extends Activity implements OnClickListener{
	//views
	private EditText emailEt;
	Button forgetPwdBtn;

	private final String url="";   
	private final String TAG_RESPONSE="Response";
	private final String TAG_RESPONSEINFO="ResponseInfo";
	private final String TAG_NEW_PASSWORD="new_password";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
		MySportsApp.setActivityContext(ForgetPasswordActivity.this);
		
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
		emailEt  = (EditText)findViewById(R.id.forget_pwd_email);
		forgetPwdBtn = (Button)findViewById(R.id.forget_pwd_btn);
		forgetPwdBtn.setOnClickListener(this);
	}	

	/**
	 * method for calling reset passowrd service
	 */

	private void serviceForResetPassword(String email){
		String url = Constants.common_url + getString(R.string.forget_password);
		Log.i("", "URL forget password: " + url+"  "+email);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email", email));

		JsonParser.callBackgroundService(url, nameValuePairs, new JsonServiceListener() {
			ProgressDialog pd;
			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(ForgetPasswordActivity.this);
				pd.show();
				pd.setMessage("Please wait. Reseting password....");
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
					try {
						JSONObject json= new JSONObject(jsonResponse);

						JSONObject Response_obj = json.getJSONObject(TAG_RESPONSE);

						ForgetPasswordBean mResponseBean = new ForgetPasswordBean();
						if(Response_obj.has(TAG_RESPONSEINFO)){
							String str_ResponseInfo = Response_obj.getString(TAG_RESPONSEINFO);
							mResponseBean.setResponseInfo(str_ResponseInfo);
						}
						if(Response_obj.has(TAG_NEW_PASSWORD)){
							String str_new_password = Response_obj.getString(TAG_NEW_PASSWORD);
							mResponseBean.setNewPassword(str_new_password);
						}

						//Show response
						if(mResponseBean.getResponseInfo().equalsIgnoreCase(Constants.TAG_SUCCESS)){
							UIHelperUtil.showToastMessage(getString(R.string.forget_pwd_success_msg));
							finish();
						}else if(mResponseBean.getResponseInfo().equalsIgnoreCase("Error sending mail")){
							UIHelperUtil.showToastMessage(getString(R.string.forget_pwd_mail_error_msg));
						}
						else {
							UIHelperUtil.showToastMessage(getString(R.string.forget_pwd_fail_msg));
						}

					} catch (Exception e){
					}
				} else {
					UIHelperUtil.showToastMessage(getString(R.string.service_toast));
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.forget_pwd_btn){
			//TODO: reset password process should start
			if(!emailEt.getText().toString().matches("")){
				if(Utils.isValidEmail(emailEt.getText().toString())){
					if(Utils.chkStatus()){
						serviceForResetPassword(emailEt.getText().toString());
					}else{
						Utils.networkAlertDialog(ForgetPasswordActivity.this,getResources().getString(R.string.toast_no_network));
					}
				}else{
					UIHelperUtil.showToastMessage(getResources().getString(R.string.invalid_email));
				}
			}else{
				UIHelperUtil.showToastMessage(getResources().getString(R.string.empty_email));
			}
		}
	}
}
