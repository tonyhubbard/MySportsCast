package com.mysportsshare.mysportscast.fragments;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CustomMultiMonth;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.adapters.NotificationsAdapter;
import com.mysportsshare.mysportscast.adapters.ProfileMediaItemAdapter;
import com.mysportsshare.mysportscast.models.FollowingUserInfo;
import com.mysportsshare.mysportscast.models.NotificationInfo;
import com.mysportsshare.mysportscast.models.NotificationInfo;
import com.mysportsshare.mysportscast.models.TeamInfo;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.DataPassListener;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class NotificationFragment extends Fragment implements OnClickListener{
	ListView notificationListView;
	Activity activity;
	NotificationsAdapter notificationItemAdapter;
	RelativeLayout footerLayout;

	private boolean reload;
	private Handler handler;
	private int pageCount = 0;

	//Title bar components
	TextView title;
	ImageView backBtn;
	ImageView settingBtn;
	private ImageView addEventBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private RelativeLayout search_header_llyt;	

	private String user_name = "";
	private String first_name = "";
	private String user_profile_img = "";
	private RelativeLayout ViewNavigationInfoLayout;
	private String user_id;
	private int pos;
	private TextView no_notification_text;
	private RelativeLayout notification_loading_Layout;
	private TextView tvAppNotifyCount;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layoutView = inflater.inflate(R.layout.fragment_notification, container,false);

		init(layoutView);

		return layoutView;
	}	

	//Initializing method
	private void init(View layoutView){
		//Reference to Title bar components 
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		backBtn = (ImageView) activity.findViewById(R.id.back_iv);
		settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);
		searchHeaderBtn = (ImageView)activity.findViewById(R.id.search_iv);
		addEventBtn = (ImageView)activity.findViewById(R.id.add_an_event_iv);
		tvEventType = (TextView) activity.findViewById(R.id.title_bar_tv_event_type);
		search_header_llyt = (RelativeLayout) activity.findViewById(R.id.search_header_llyt);
		tvAppNotifyCount = (TextView) activity.findViewById(R.id.app_notify_count);

		//Referring to layout views
		no_notification_text = (TextView) layoutView.findViewById(R.id.no_notification_text);
		notificationListView = (ListView) layoutView.findViewById(R.id.media_list);
		footerLayout = (RelativeLayout) layoutView
				.findViewById(R.id.loading_footer_layout);
		notification_loading_Layout = (RelativeLayout) layoutView
				.findViewById(R.id.loading_notification_layout);

		handler = new Handler();


		user_id = SharedPreferencesUtils.getUserId();
	}

	/*This method loads the user details from the server
	 *and displays on the screen
	 * */
	public void updateUI() {	

		//Updating title bar fields
		title.setText(Constants.userProf_Notificaton);	

		title.setVisibility(View.VISIBLE);
		backBtn.setVisibility(View.GONE);		
		settingBtn.setVisibility(View.GONE);
		tvEventType.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		search_header_llyt.setVisibility(View.GONE);

		notificationListView.removeFooterView(footerLayout);
		notificationListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					if (notificationListView.getLastVisiblePosition() >= notificationListView
							.getCount() - 1 ) {
						if(reload){
							footerLayout.setVisibility(View.VISIBLE);
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									if (Utils.chkStatus()) {
										pageCount += 1;
										footerLayout.setVisibility(View.GONE);
										notificationListView
										.removeFooterView(footerLayout);

										if (reload) {
											//footerLayout.setVisibility(View.VISIBLE);
											serviceToGetUserNotification(user_id);
										} else {
											/*UIHelperUtil
											.showToastMessage("The list is up to date.");*/
											
											UIHelperUtil
											.showToastMessage("This is the Last Post");
											footerLayout.setVisibility(View.GONE);
											notificationListView.removeFooterView(footerLayout);
										}
									} else {
										UIHelperUtil
										.showToastMessage(getString(R.string.toast_no_network));
									}

								}
							}, 750);
						}else{
							/*UIHelperUtil
							.showToastMessage("The list is up to date.");*/
							UIHelperUtil
							.showToastMessage("This is the Last Post");
							footerLayout.setVisibility(View.GONE);
							notificationListView.removeFooterView(footerLayout);
						}

					}
					/*else  if (notificationListView.getFirstVisiblePosition() <=0) {
						footerLayout.setVisibility(View.VISIBLE);
						UIHelperUtil.showToastMessage(notificationListView.getFirstVisiblePosition()+"");
						notificationItemAdapter=null;
						pageCount = 0;
						serviceToGetUserNotification(user_id);
						footerLayout.setVisibility(View.GONE);
					}*/ 
					else {
						notificationListView.removeFooterView(footerLayout);
						footerLayout.setVisibility(View.GONE);
					}
					Log.d("",
							"last pos:"
									+ notificationListView.getLastVisiblePosition()
									+ " : " + notificationListView.getCount() + " : "
									+ pageCount + " : " + reload);
				} else {
					notificationListView.removeFooterView(footerLayout);
					footerLayout.setVisibility(View.GONE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

		downloadNotifications();
	}

	private void downloadNotifications() {
		if(notificationItemAdapter==null){
			if (Utils.chkStatus()) {
				notification_loading_Layout.setVisibility(View.VISIBLE);
				no_notification_text.setVisibility(View.GONE);
				notificationListView.setVisibility(View.GONE);
				
				//Clear push notifications from the home screen
				NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.cancelAll();
				
				pageCount = 0;
				serviceToGetUserNotification(user_id);
			}
			else{
				UIHelperUtil
				.showToastMessage(getString(R.string.toast_no_network));
			}
		}else{
			notification_loading_Layout.setVisibility(View.GONE);
			no_notification_text.setVisibility(View.GONE);
			notificationListView.setVisibility(View.VISIBLE);
			notificationListView.setAdapter(notificationItemAdapter);
//			notificationListView.setSelection(0);
			
			//Clear push notifications from the home screen
			NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancelAll();
		}
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if(savedInstanceState!=null){
			pos = savedInstanceState.getInt("ListItemPosition", 0);	
		}
		updateUI();
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if(notificationItemAdapter!=null){
			outState.putInt("ListItemPosition", notificationListView.getFirstVisiblePosition());	
		}

	}
	@Override
	public void onStart() {
		super.onStart();

		//Updating title bar fields		
		title.setVisibility(View.VISIBLE);						
		settingBtn.setVisibility(View.GONE);
		tvEventType.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		search_header_llyt.setVisibility(View.GONE);

		int notificationsCount = SharedPreferencesUtils.getNotificationCount();
		if(notificationsCount>0){
			//refresh notifications items
			notificationItemAdapter=null;
			SharedPreferencesUtils.setNotificationCount(0);
		}
		tvAppNotifyCount.setVisibility(View.GONE);
//		downloadNotifications();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.back_iv:
			if(activity instanceof MainActivity){
				((MainActivity)activity).popFragments();
			}
			
			break;
		}
	}



	/****************************SERVER INTERFACES STARTS*************************/
	private void serviceToGetUserNotification(final String userID){
		try{
			//Url for getting user profile info
			String url =  Constants.common_url + activity.getString(R.string.get_notifications);

			//input data to web service
			List<NameValuePair> inputVals = new ArrayList<NameValuePair>() ;
			inputVals.add(new BasicNameValuePair(Constants.userProf_userID,userID));
			inputVals.add(new BasicNameValuePair(Constants.TAG_PAGE_ID,pageCount+""));

			//Send req to url & handle the result 
			JsonParser.callBackgroundService(url, inputVals, new JsonServiceListener(
					) {
				@Override
				public void parseJsonResponse(String jsonResponse) {
					if (jsonResponse!= null) {
						Log.v(Constants.logUrl, "Notification details RESPONSE: " + jsonResponse);
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(jsonResponse);
							JSONObject resObj = jsonObject.getJSONObject(Constants.TAG_RESPONSE);
							String responseStr = resObj.getString(Constants.TAG_RESPONSE_INFO);
							List<TeamInfo> myteamList = new ArrayList<TeamInfo>();
							if (responseStr!=null && responseStr.equalsIgnoreCase(Constants.TAG_SUCCESS)){
								try{
									//Fetching liked users list
									JSONArray profileCastsJSONArray = resObj.getJSONArray(Constants.TAG_NOTIFICATION_INFO);

									ArrayList<NotificationInfo> notificationInfoList = new ArrayList<NotificationInfo>();
									if(profileCastsJSONArray.length() >0){
										for (int i = 0; i < profileCastsJSONArray.length(); i++){
											JSONObject notificationtObj = profileCastsJSONArray.getJSONObject(i);
											NotificationInfo tmpNotification = new NotificationInfo();
											tmpNotification.setSender_id(notificationtObj.getString(Constants.TAG_NOTIFICATION_SENDER_ID));
											tmpNotification.setNotification_id(notificationtObj.getString(Constants.TAG_NOTIFICATION_ID));
											tmpNotification.setSender_name(notificationtObj.getString(Constants.TAG_NOTIFICATION_SENDER_NAME));
											tmpNotification.setSender_image_url(notificationtObj.getString(Constants.TAG_NOTIFICATION_SENDER_PIC_URL));
											tmpNotification.setNotification_type(notificationtObj.getString(Constants.TAG_NOTIFICATION_TYPE));
											tmpNotification.setData_id(notificationtObj.getString(Constants.TAG_NOTIFICATION_DATA_ID));
											tmpNotification.setText(notificationtObj.getString(Constants.TAG_NOTIFICATION_TEXT));
											tmpNotification.setData_url(notificationtObj.getString(Constants.TAG_NOTIFICATION_DATA_URL));
											tmpNotification.setIs_viewed(notificationtObj.getString(Constants.TAG_NOTIFICATION_VIEW_STATUS));
											tmpNotification.setCreated_date(notificationtObj.getString(Constants.TAG_NOTIFICATION_DATE));
											tmpNotification.setEvent_id(notificationtObj.getString(Constants.TAG_NOTIFICATION_EVENTID));
											if(!TextUtils.isEmpty(notificationtObj.getString(Constants.TAG_NOTIFICATION_EVENTNAME))){
												tmpNotification.setEvent_name(notificationtObj.getString(Constants.TAG_NOTIFICATION_EVENTNAME).toUpperCase());
											}

											//determines info is of cast type
											notificationInfoList.add(tmpNotification);
										}
										Log.d("","profile Casts: "+notificationInfoList.size());

										if(notificationItemAdapter==null){
											notificationItemAdapter = new NotificationsAdapter(activity, user_id,first_name, notificationInfoList);
											notificationListView.setAdapter(notificationItemAdapter);
											notificationListView.setVisibility(View.VISIBLE);
											notification_loading_Layout.setVisibility(View.GONE);
											no_notification_text.setVisibility(View.GONE);
										}else
										{
											notificationItemAdapter.updateNotificationsList(notificationInfoList);
											//										notificationItemAdapter.notifyDataSetChanged();
										}

										if (notificationInfoList.size() >= 10) {
											reload = true;
											notificationListView.addFooterView(footerLayout);
										} else {
											reload = false;
										}
									}else{
										if(notificationItemAdapter ==null){
											//No notification item found
											notificationListView.setVisibility(View.GONE);
											notification_loading_Layout.setVisibility(View.GONE);
											no_notification_text.setVisibility(View.VISIBLE);
										}
									}
								}catch(Exception ex){
								}
							}else{
								if(notificationItemAdapter==null){
									//No notification item found
									notificationListView.setVisibility(View.GONE);
									notification_loading_Layout.setVisibility(View.GONE);
									no_notification_text.setVisibility(View.VISIBLE);
								}
							}
						}catch(Exception ex){

						}
					}
				}

			});
		}catch(Exception ex){
			if(BuildConfig.DEBUG){
				Log.e(Constants.logUrl,ex.getMessage());
			}
		}
	}
	/****************************SERVER INTERFACES ENDS*************************/
}
