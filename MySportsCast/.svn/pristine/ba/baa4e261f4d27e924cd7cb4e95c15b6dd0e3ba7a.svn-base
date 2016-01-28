package com.mysportsshare.mysportscast.fragments;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.adapters.CommonAdapter;
import com.mysportsshare.mysportscast.adapters.SportsAdapter;
import com.mysportsshare.mysportscast.models.GeoData;
import com.mysportsshare.mysportscast.models.SportModel;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

//import com.mysportsshare.mysportscast.adapters.PlaceAutocompleteAdapter;

public class ChangePasswordFragment extends Fragment implements OnClickListener {

	// Title bar components
	TextView title;
	ImageView backBtn;
	ImageView settingBtn;
	private ImageView addEventBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private RelativeLayout search_header_llyt;

	// views
	EditText oldPwdEd;
	EditText newPwdEd;
	EditText confirmPwdEd;
	Button changePwdBtn;
	private Activity activity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_change_password, container,
				false);
		init(view);
		Log.v("", "lati and longi:" + Constants.lati + " " + Constants.longi);


		return view;
	}	

	// initialize variables
	private void init(View fragmentView) {
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		backBtn = (ImageView) activity.findViewById(R.id.back_iv);
		settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);
		searchHeaderBtn = (ImageView) activity
				.findViewById(R.id.search_iv);
		addEventBtn = (ImageView) activity.findViewById(
				R.id.add_an_event_iv);
		tvEventType = (TextView) activity.findViewById(
				R.id.title_bar_tv_event_type);
		search_header_llyt = (RelativeLayout) activity.findViewById(
				R.id.search_header_llyt);

		oldPwdEd = (EditText) fragmentView.findViewById(R.id.old_pwd_et);
		newPwdEd = (EditText) fragmentView.findViewById(R.id.new_pwd_et);
		confirmPwdEd = (EditText) fragmentView.findViewById(R.id.conform_pwd_et);
		changePwdBtn = (Button) fragmentView.findViewById(R.id.change_pwd_btn); 

		backBtn.setOnClickListener(this);
		changePwdBtn.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		title.setText("CHANGE PASSWORD");
		settingBtn.setVisibility(View.INVISIBLE);
		backBtn.setVisibility(View.VISIBLE);
		tvEventType.setVisibility(View.INVISIBLE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		search_header_llyt.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.change_pwd_btn:
			//calls service to change password
			//TODO: validate old, new & confirm password
			if(oldPwdEd.getText().toString().trim().length()>0){
				if(newPwdEd.getText().toString().trim().length()>0){
					if(confirmPwdEd.getText().toString().trim().length()>0){
						if(newPwdEd.getText().toString().equals(confirmPwdEd.getText().toString())){
							serviceToUpdatePassword(oldPwdEd.getText().toString().trim(),newPwdEd.getText().toString().trim());
						}else{
							UIHelperUtil.showToastMessage("New password mismatch with confirmation password");
							confirmPwdEd.requestFocus();
						}
					}else{
						UIHelperUtil.showToastMessage("Please enter confirmation password");
						confirmPwdEd.requestFocus();
					}
				}else{
					UIHelperUtil.showToastMessage("Please enter new password");
					newPwdEd.requestFocus();
				}
			}else{
				UIHelperUtil.showToastMessage("Please enter old password");
				oldPwdEd.requestFocus();
			}

			break;
		case R.id.back_iv:
			//pop up fragment.
			if(activity instanceof MainActivity){
				((MainActivity)activity).onBackPressed();
			}
			
			break;
		}
	}

	// service to get sports list
	private void serviceToUpdatePassword(String oldPwd, String newPwd) {
		String url = Constants.common_url
				+ getResources().getString(R.string.update_password);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(Constants.TAG_USER_ID, SharedPreferencesUtils.getUserId()));
		nameValuePairs.add(new BasicNameValuePair(Constants.TAG_CHANGE_OLD_PWD, oldPwd));
		nameValuePairs.add(new BasicNameValuePair(Constants.TAG_CHANGE_NEW_PWD, newPwd));
		
		Log.v(Constants.logUrl,"update password:  " + url +"?"+Constants.TAG_USER_ID +"="+SharedPreferencesUtils.getUserId() 
				+"&"+ Constants.TAG_CHANGE_OLD_PWD+ ":"+oldPwd+ "&"+Constants.TAG_CHANGE_NEW_PWD + "="+newPwd);
		
		JsonParser.callBackgroundService(url, nameValuePairs, new JsonServiceListener() {
			ProgressDialog pd;

			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(activity);
				pd.setMessage("Please wait... Changing password");
				pd.show();
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
			}

			@Override
			public void hideProgress() {
				if (pd != null && pd.isShowing()) {
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
			if (jsonResult != null) {
				Log.v(Constants.logUrl,"response:  " + jsonResult);
				JSONObject reader = new JSONObject(jsonResult);
				JSONObject sys = reader.getJSONObject("Response");
				String response = sys.getString("ResponseInfo");
				final List<SportModel> sportsList = new ArrayList<SportModel>();
				if (response != null && response.equals("SUCCESS")) {
					UIHelperUtil.showToastMessage("Password changed successfully");
					((MainActivity) activity).onBackPressed();
				} else {
					UIHelperUtil.showToastMessage("Unable to change password.  Please try later");
				}
			} else {
				UIHelperUtil.showToastMessage(activity.getString(R.string.service_toast));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
