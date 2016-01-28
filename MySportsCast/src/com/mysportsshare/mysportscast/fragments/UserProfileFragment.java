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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CalendarEventsActivity;
import com.mysportsshare.mysportscast.activity.CustomMultiMonth;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.activity.ShowTagEventActivity;
import com.mysportsshare.mysportscast.models.FollowingUserInfo;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class UserProfileFragment extends Fragment implements OnClickListener{

	RelativeLayout followerCntLayout;
	RelativeLayout followingCntLayout;
	RelativeLayout checkinsCntLayout;

	TextView followerCntTV;
	TextView followingCntTV;
	TextView checkinsCntTV;

	LinearLayout NavigationAllLayout;
	LinearLayout NavigationVideosLayout;
	LinearLayout NavigationPhotosLayout;
	LinearLayout NavigationCastsLayout;
	LinearLayout NavigationTagsLayout;
	LinearLayout NavigationEventsLayout;
	TextView tagTV;
	TextView eventsTV;


	ImageView userProfileIV;
	TextView userNameTV;
	Button editUserProfileBtn;
	LinearLayout followBtnLayout;
	TextView followBtnTxt;
	ImageView followBtnImg;
	ImageView CalenderIV;
	LinearLayout ViewPrivacyInfoLayout;

	//Title bar components
	TextView title;
	ImageView backBtn;
	ImageView settingBtn;
	private ImageView addEventBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private RelativeLayout search_header_llyt;

	//Holds the call back listener to pass data
	//	DataPassListener mCallback;	

	// for custom calendar views,objects and constants
	private static final String tag = "MyCalendarFragments";
	private Calendar _calendar;
	private int month, year;
	private String dayStr, monthStr, yearStr;
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";
	private Dialog mDialog;
	private TextView currentMonth;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;

	private HashMap<String,HashMap<String,List<String>>> calendarSummaryEvnetInfo;

	private String following_status = Constants.userProf_follow_status_not_follow;
	private String profile_visiblity = Constants.userProf_visibility_friends;
	private String follower_cnt = "0";
	private String following_cnt = "0";
	private String checkins_cnt = "0";
	private String user_name = "";
	private String first_name = "";
	private String user_profile_img = "";
	private RelativeLayout ViewNavigationInfoLayout;
	private String user_id;

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
		View layoutView = inflater.inflate(R.layout.fragment_userprofile, container,false);

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

		followerCntLayout = (RelativeLayout) layoutView.findViewById(R.id.userprof_followersLayout);
		followingCntLayout = (RelativeLayout) layoutView.findViewById(R.id.userprof_followingLayout);
		checkinsCntLayout = (RelativeLayout) layoutView.findViewById(R.id.userprof_CheckinsLayout);

		followerCntTV = (TextView) layoutView.findViewById(R.id.userprofile_followerCntValTV);
		followingCntTV = (TextView) layoutView.findViewById(R.id.userprofile_followingCntValTV);
		checkinsCntTV = (TextView) layoutView.findViewById(R.id.userprofile_CheckinsCntValTV);

		userProfileIV = (ImageView) layoutView.findViewById(R.id.userProfile_img_iv);
		userNameTV = (TextView) layoutView.findViewById(R.id.userProfile_user_name_tv);
		editUserProfileBtn = (Button) layoutView.findViewById(R.id.userProfile_edit_btn);
		followBtnLayout= (LinearLayout) layoutView.findViewById(R.id.follow_btn_layout);
		followBtnTxt= (TextView) layoutView.findViewById(R.id.follow_btn_text);
		followBtnImg= (ImageView) layoutView.findViewById(R.id.follow_btn_img);
		CalenderIV = (ImageView) layoutView.findViewById(R.id.userProfile_Calender_iv);

		ViewPrivacyInfoLayout = (LinearLayout) layoutView.findViewById(R.id.userprofile_navigation_hide);
		ViewNavigationInfoLayout = (RelativeLayout) layoutView.findViewById(R.id.userprofile_navigation_all_layout);
		NavigationAllLayout = (LinearLayout) layoutView.findViewById(R.id.userprofile_navigate_all_layout);
		NavigationVideosLayout = (LinearLayout) layoutView.findViewById(R.id.userprofile_navigate_Videos_layout);
		NavigationPhotosLayout = (LinearLayout) layoutView.findViewById(R.id.userprofile_navigate_Photos_layout);
		NavigationCastsLayout = (LinearLayout) layoutView.findViewById(R.id.userprofile_navigate_Casts_layout);
		NavigationTagsLayout = (LinearLayout) layoutView.findViewById(R.id.userprofile_navigate_Tags_layout);
		NavigationEventsLayout = (LinearLayout) layoutView.findViewById(R.id.userprofile_navigate_Events_layout);		
		tagTV = (TextView) layoutView.findViewById(R.id.userprofile_Tags_TV);
		eventsTV = (TextView) layoutView.findViewById(R.id.userprofile_Events_TV);

		//Edit profile page navigation
		editUserProfileBtn.setOnClickListener(this);
		followBtnLayout.setOnClickListener(this);
		CalenderIV.setOnClickListener(this);

		//To handle followers, following & checking screens
		followerCntLayout.setOnClickListener(this);
		followingCntLayout.setOnClickListener(this);
		checkinsCntLayout.setOnClickListener(this);

		//To handle navigation to photos, videos, casts, tags & events
		NavigationAllLayout.setOnClickListener(this);
		NavigationPhotosLayout.setOnClickListener(this);
		NavigationVideosLayout.setOnClickListener(this);
		NavigationCastsLayout.setOnClickListener(this);
		NavigationTagsLayout.setOnClickListener(this);
		NavigationEventsLayout.setOnClickListener(this);
	}

	/*This method loads the user details from the server
	 *and displays on the screen
	 * */
	@Override
	public void onStart() {	
		super.onStart();

		Bundle args = getArguments();		

		//If follower or following user is selected.
		if (args != null) {
			user_id = args.getString(Constants.dataReceive);
			//Display selected user screen	    	
			getUserProfileInfo(args.getString(Constants.dataReceive));
		}else{
			//Display profile user's screen
			//Get followers, following & check-ins values from server 
			//Displays followers users, following users & checked-event counts on the screen
			backBtn.setVisibility(View.GONE);
			user_id = SharedPreferencesUtils.getUserId();
			getUserProfileInfo(SharedPreferencesUtils.getUserId());	
		}

	}

	@Override
	public void onResume() {
		super.onResume();

		//handling back button click
		backBtn.setOnClickListener(this);

		//Updating title bar fields		
		title.setVisibility(View.VISIBLE);						
		settingBtn.setVisibility(View.GONE);
		tvEventType.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		search_header_llyt.setVisibility(View.GONE);


		Bundle args = getArguments();
		if (args == null) {
			//Displaying profile users page
			backBtn.setVisibility(View.GONE);
			editUserProfileBtn.setVisibility(View.VISIBLE);
			followBtnLayout.setVisibility(View.GONE);
			editUserProfileBtn.setBackgroundColor(activity.getResources().getColor(R.color.com_facebook_blue));
			tagTV.setText("Your Tags");
			eventsTV.setText("Your Events");

		}else{
			//Displaying sub-user profiles
			backBtn.setVisibility(View.VISIBLE);

			if(args.getString(Constants.dataReceive).equals(SharedPreferencesUtils.getUserId())){
				//logged user should be able to edit profile
				editUserProfileBtn.setVisibility(View.VISIBLE);
				followBtnLayout.setVisibility(View.GONE);
				editUserProfileBtn.setBackgroundColor(activity.getResources().getColor(R.color.com_facebook_blue));
				tagTV.setText("Your Tags");
				eventsTV.setText("Your Events");
				title.setText("MY"+Constants.userProf_Title);
			}else{
				//TODO: Show follow status. (need to check whether I'm(logged user) following or not) 

				//				//followers & following users should not be provided with Edit button. Instead their following status is displayed.
				//				editUserProfileBtn.setBackgroundColor(activity.getResources().getColor(R.color.green));
				//				editUserProfileBtn.setText(Constants.UI_IamFollowingStatus);
				//				editUserProfileBtn.setTextColor(activity.getResources().getColor(R.color.white));

				tagTV.setText("Tags");
				eventsTV.setText("Events");
				title.setText(first_name+Constants.userProf_Title);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.userProfile_edit_btn:
			Bundle args1 = getArguments();
			if (args1 == null) {
				openEditUserProfileScreen(SharedPreferencesUtils.getUserId());				
			}else{
				if(args1.getString(Constants.dataReceive).equals(SharedPreferencesUtils.getUserId())){
					//logged user should be able to edit profile
					openEditUserProfileScreen(SharedPreferencesUtils.getUserId());
				}
			}
			break;
		case R.id.follow_btn_layout:
			Bundle args5 = getArguments();
			if (args5 == null) {
				//do nothing
			}else{
				if(following_status.equalsIgnoreCase(Constants.userProf_follow_status_not_follow)){
					//sending request to follow 'unfollowing' user
					//Unfollowing already following user
					updateFollowStatus(args5.getString(Constants.dataReceive),"1");
				}else{
					//Unfollowing already following user (or) previously requested private user but req not accepted
					updateFollowStatus(args5.getString(Constants.dataReceive),"0");
				}
			}
			break;
		case R.id.userProfile_Calender_iv:
			/*Intent i = new Intent(activity,CustomMultiMonth.class);
			i.putExtra(Constants.userProf_userID, user_id);
			i.putExtra(Constants.userProf_userName, first_name);
			i.putExtra(Constants.dataReceive,  Constants.data_cal_userCheckedEvents);
			startActivity(i);*/
			//			servicetoGetCalendarSummaryInfo();
			//TODO: Display calendar events

			//Displays checked in events list
			Bundle args = new Bundle();
			args.putBoolean(Constants.Event_display_calendar, false);
			args.putString(Constants.userProf_userID, user_id);
			args.putString(Constants.userProf_userName, first_name);
			args.putString(Constants.dataReceive, Constants.data_cal_userPostEvents);
			args.putString(Constants.Event_display_calendar_day, "");
			args.putString(Constants.Event_display_calendar_month, "");
			args.putString(Constants.Event_display_calendar_year, "");
			Fragment activeFragment = new MyCalendarEventsFragment();
			activeFragment.setArguments(args);
			displayOnActivity(activeFragment);

			break;
		case R.id.userprof_followersLayout:
			//Displays followers list 
			Bundle args2 = getArguments();
			if(args2!=null){
				openFollowerUsers(args2.getString(Constants.dataReceive));
			}else{
				openFollowerUsers(SharedPreferencesUtils.getUserId());
			}		
			break;
		case R.id.userprof_followingLayout:
			//Displays following users list
			Bundle args3 = getArguments();
			if(args3!=null){
				//Create fragment & pass message(UserID) to followings screen
				openFollowingUsers(args3.getString(Constants.dataReceive));				
			}else{
				openFollowingUsers(SharedPreferencesUtils.getUserId());
			}
			break;
		case R.id.userprof_CheckinsLayout:
			//Displays checked in events list
			Bundle args4 = new Bundle();
			args4.putBoolean(Constants.Event_display_calendar, false);
			args4.putString(Constants.userProf_userID, user_id);
			args4.putString(Constants.userProf_userName, first_name);
			args4.putString(Constants.Event_display_calendar_day, "");
			args4.putString(Constants.Event_display_calendar_month, "");
			args4.putString(Constants.Event_display_calendar_year, "");
			Fragment activeFragment4 = new CheckedEventsFragment();
			activeFragment4.setArguments(args4);
			displayOnActivity(activeFragment4);

			break;
		case R.id.userprofile_navigate_all_layout:
			//			Toast.makeText(activity, "Yet to implement(display ALL)", Toast.LENGTH_SHORT).show();
			Fragment profile_all_Fragment = new ProfileAllFragment();
			Bundle args10 = new Bundle();
			args10.putString(Constants.userProf_userID,user_id);
			args10.putString(Constants.userProf_userName,first_name);
			profile_all_Fragment.setArguments(args10);
			displayOnActivity(profile_all_Fragment);			
			break;
		case R.id.userprofile_navigate_Videos_layout:
			//			Toast.makeText(activity, "Yet to implement(display Videos)", Toast.LENGTH_SHORT).show();
			Fragment profilevideosFragment = new ProfileVideosFragment();
			Bundle args8 = new Bundle();
			args8.putString(Constants.userProf_userID,user_id);
			args8.putString(Constants.userProf_userName,first_name);
			profilevideosFragment.setArguments(args8);
			displayOnActivity(profilevideosFragment);
			break;
		case R.id.userprofile_navigate_Photos_layout:

			//			Toast.makeText(activity, "Yet to implement(display Photos)", Toast.LENGTH_SHORT).show();
			Fragment profilephotosFragment = new ProfilePhotosFragment();
			Bundle args7 = new Bundle();
			args7.putString(Constants.userProf_userID,user_id);
			args7.putString(Constants.userProf_userName,first_name);
			profilephotosFragment.setArguments(args7);
			displayOnActivity(profilephotosFragment);
			break;
		case R.id.userprofile_navigate_Tags_layout:
			//			Toast.makeText(activity, "Yet to implement(display Tags)", Toast.LENGTH_SHORT).show();
			Fragment profiletagsFragment = new ProfileTagsFragment();
			Bundle args9 = new Bundle();
			args9.putString(Constants.userProf_userID,user_id);
			args9.putString(Constants.userProf_userName,first_name);
			profiletagsFragment.setArguments(args9);
			displayOnActivity(profiletagsFragment);
			break;
		case R.id.userprofile_navigate_Casts_layout:
			//			Toast.makeText(activity, "Yet to implement(display Casts)", Toast.LENGTH_SHORT).show();
			Fragment profilecastFragment = new ProfileCastsFragment();
			Bundle args6 = new Bundle();
			args6.putString(Constants.userProf_userID,user_id);
			args6.putString(Constants.userProf_userName,first_name);
			profilecastFragment.setArguments(args6);
			displayOnActivity(profilecastFragment);

			break;
		case R.id.userprofile_navigate_Events_layout:
			//Displays checked in events list
			Bundle eventsargs = new Bundle();
			eventsargs.putBoolean(Constants.Event_display_calendar, false);
			eventsargs.putString(Constants.userProf_userID, user_id);
			eventsargs.putString(Constants.userProf_userName, first_name);
			eventsargs.putString(Constants.dataReceive, Constants.data_cal_userPostEvents);
			eventsargs.putString(Constants.Event_display_calendar_day, "");
			eventsargs.putString(Constants.Event_display_calendar_month, "");
			eventsargs.putString(Constants.Event_display_calendar_year, "");
			Fragment activeFragment1 = new MyCalendarEventsFragment();
			activeFragment1.setArguments(eventsargs);
			displayOnActivity(activeFragment1);
			break;

		case R.id.back_iv:
			popFromActivity();			
			break;
		}
	}
	//by koti
	private void displayOnActivity(Fragment activeFragment,boolean shouldAdd) {
		if(activity.getClass().getSimpleName().equalsIgnoreCase("MainActivity")){
			((MainActivity)activity).pushFragments(((MainActivity)activity).getmCurrentTab(), activeFragment, false,shouldAdd);
		}else if(activity.getClass().getSimpleName().equalsIgnoreCase("CalendarEventsActivity")){
			//stack & displayed on the Calendar screen						
			((CalendarEventsActivity)activity).push(activeFragment);
		}else if(activity.getClass().getSimpleName().equalsIgnoreCase("ShowTagEventActivity")){
			//stack & displayed on the Calendar screen						
			((ShowTagEventActivity)activity).push(activeFragment);
		}
	}

	private void displayOnActivity(Fragment activeFragment) {
		if(activity.getClass().getSimpleName().equalsIgnoreCase("MainActivity")){
			((MainActivity)activity).pushFragments(((MainActivity)activity).getmCurrentTab(), activeFragment, false,true);
		}else if(activity.getClass().getSimpleName().equalsIgnoreCase("CalendarEventsActivity")){
			//stack & displayed on the Calendar screen						
			((CalendarEventsActivity)activity).push(activeFragment);
		}else if(activity.getClass().getSimpleName().equalsIgnoreCase("ShowTagEventActivity")){
			//stack & displayed on the Calendar screen						
			((ShowTagEventActivity)activity).push(activeFragment);
		}
	}
	private void popFromActivity() {
		if(activity.getClass().getSimpleName().equalsIgnoreCase("MainActivity")){
			((MainActivity)activity).popFragments();
		}else if(activity.getClass().getSimpleName().equalsIgnoreCase("CalendarEventsActivity")){
			//stack & displayed on the Calendar screen						
			((CalendarEventsActivity)activity).popFragments();
		}else if(activity.getClass().getSimpleName().equalsIgnoreCase("ShowTagEventActivity")){
			//stack & displayed on the Calendar screen						
			((ShowTagEventActivity)activity).popFragments();
		}
	}

	private void openFollowingUsers(String UserId) {
		FollowingUsersListFragment activeFragment = new FollowingUsersListFragment();
		Bundle arg = new Bundle();
		arg.putString(Constants.dataReceive, UserId);
		activeFragment.setArguments(arg);
		//displays user page in specified tab.
		if(activity instanceof MainActivity && (((MainActivity)activity).mStacks.get(Constants.TAB_USERS).size() > 2)){
				displayOnActivity(activeFragment,false);
		}else {
				displayOnActivity(activeFragment,true);
		}
		

	}

	private void openFollowerUsers(String UserId) {
		FollowerUsersListFragment activeFragment = new FollowerUsersListFragment();
		Bundle arg = new Bundle();
		arg.putString(Constants.dataReceive, UserId);
		activeFragment.setArguments(arg);
		//displays user page in specified tab.

		if(activity instanceof MainActivity){
			if(((MainActivity)activity).mStacks.get(Constants.TAB_USERS).size() > 2){
				displayOnActivity(activeFragment,false);
			}else{
				displayOnActivity(activeFragment,true);
			}
		}else{
			displayOnActivity(activeFragment,true);
		}
	}



	private void openEditUserProfileScreen(String userId) {
		//Opens edit profile page
		EditProfileFragmentPage activeFragment = new EditProfileFragmentPage();
		Bundle arg = new Bundle();
		arg.putString(Constants.dataReceive, userId);
		activeFragment.setArguments(arg);
		//displays user page in specified tab.
		displayOnActivity(activeFragment);
	}

	private void incFollowerCnt(){
		followerCntTV.setText((Integer.parseInt(followerCntTV.getText().toString()) + 1) + "");
	}
	private void decFollowingCnt(){
		if(Integer.parseInt(followerCntTV.getText().toString())<=0){
			followerCntTV.setText("0");
		}else{
			followerCntTV.setText((Integer.parseInt(followerCntTV.getText().toString()) - 1) + "");
		}
	}

	private void updateUI(){
		userNameTV.setText(user_name);
		BitmapUtils.setImages(user_profile_img, userProfileIV,R.drawable.profile_pic_dummy);
		followerCntTV.setText(follower_cnt);
		followingCntTV.setText(following_cnt);
		checkinsCntTV.setText(checkins_cnt);
		if((!user_id.equalsIgnoreCase(SharedPreferencesUtils.getUserId())) &&
				profile_visiblity.equalsIgnoreCase(Constants.userProf_visibility_friends) && 
				(following_status.equalsIgnoreCase(Constants.userProf_follow_status_not_follow) 
						|| following_status.equalsIgnoreCase(Constants.userProf_follow_status_requested) )){
			ViewPrivacyInfoLayout.setVisibility(View.VISIBLE);
			ViewNavigationInfoLayout.setVisibility(View.GONE);
			followerCntLayout.setEnabled(false);
			followingCntLayout.setEnabled(false);
			checkinsCntLayout.setEnabled(false);
			CalenderIV.setEnabled(false);
		}else{
			ViewPrivacyInfoLayout.setVisibility(View.GONE);
			ViewNavigationInfoLayout.setVisibility(View.VISIBLE);
			followerCntLayout.setEnabled(true);
			followingCntLayout.setEnabled(true);
			checkinsCntLayout.setEnabled(true);
			CalenderIV.setEnabled(true);
		}
		if(user_id.equalsIgnoreCase(SharedPreferencesUtils.getUserId())){
			title.setText("MY"+Constants.userProf_Title);	
		}else{
			title.setText(first_name+Constants.userProf_Title);	
		}

		if(user_id.equalsIgnoreCase(SharedPreferencesUtils.getUserId())){
			SharedPreferencesUtils.setUserName(user_name);
			SharedPreferencesUtils.setUserProfilePicPath(user_profile_img);
		}
	}

	/****************************SERVER INTERFACES STARTS*************************/
	private void getUserProfileInfo(final String userID){
		try{
			//Url for getting user profile info
			String url =  Constants.common_url + getString(R.string.get_Profle_Details);

			//input data to web service
			List<NameValuePair> inputVals = new ArrayList<NameValuePair>() ;
			inputVals.add(new BasicNameValuePair(Constants.userProf_userID,userID));
			inputVals.add(new BasicNameValuePair(Constants.userProf_logged_userID,SharedPreferencesUtils.getUserId()));

			//Send req to url & handle the result 
			JsonParser.callBackgroundService(url, inputVals, new JsonServiceListener(
					) {
				ProgressDialog pd;
				public void showProgress() {
					pd = new ProgressDialog(activity);				
					pd.setMessage("Please wait. Updating user details...");
					pd.setCancelable(false);
					pd.setCanceledOnTouchOutside(false);
					pd.show();
				}

				public void hideProgress() {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
				}
				@Override
				public void parseJsonResponse(String jsonResponse) {
					try {
						Log.d(Constants.logUrl," User Profile Service RESPONSE: " + jsonResponse);
						if (jsonResponse!= null) {
							JSONObject jsonObject;
							try {
								jsonObject = new JSONObject(jsonResponse);
								JSONObject resObj = jsonObject.getJSONObject(Constants.webserver_response);
								String responseStr = resObj.getString(Constants.webserver_responseInfo);
								if (responseStr!=null && responseStr.equalsIgnoreCase(Constants.webserver_response_success)){

									//Update UI on the screen
									JSONObject userProfObj = resObj.getJSONObject(Constants.userProf_InfoObj);																		

									//Displaying user name
									if(userProfObj.getString(Constants.userProf_userName)!=null){
										if(userProfObj.getString(Constants.userProf_userName).trim().length()>0){
											if(userProfObj.getString(Constants.userProf_userName).trim().equalsIgnoreCase("null")){
												userNameTV.setText("0");
											}else{
												user_name = userProfObj.getString(Constants.userProf_userName).trim().toUpperCase();
											}
										}else{ //When empty string came
											userNameTV.setText("0");
										}
									}else {//When null string came
										userNameTV.setText("0");
									}

									//Displaying user profile pic
									if(userProfObj.getString(Constants.userProf_userProfPic)!=null){
										if(userProfObj.getString(Constants.userProf_userProfPic).trim().length()>0){
											if(userProfObj.getString(Constants.userProf_userProfPic).trim().equalsIgnoreCase("null")){
												userProfileIV.setImageResource(R.drawable.profile_pic_dummy);
											}else{
												user_profile_img =userProfObj.getString(Constants.userProf_userProfPic).trim();
											}
										}else{ //When empty string came
											userProfileIV.setImageResource(R.drawable.profile_pic_dummy);
										}
									}else {//When null string came
										userProfileIV.setImageResource(R.drawable.profile_pic_dummy);
									}

									//Displaying followers count
									if(userProfObj.getString(Constants.userProf_followersCnt)!=null){
										if(userProfObj.getString(Constants.userProf_followersCnt).trim().length()>0){
											if(userProfObj.getString(Constants.userProf_followersCnt).trim().equalsIgnoreCase("null")){
												followerCntTV.setText("0");
											}else{
												follower_cnt = userProfObj.getString(Constants.userProf_followersCnt).trim();
											}
										}else{ //When empty string came
											followerCntTV.setText("0");
										}
									}else {//When null string came
										followerCntTV.setText("0");
									}

									//Displaying I'm following users count
									if(userProfObj.getString(Constants.userProf_iam_followingCnt)!=null){
										if(userProfObj.getString(Constants.userProf_iam_followingCnt).trim().length()>0){
											if(userProfObj.getString(Constants.userProf_iam_followingCnt).trim().equalsIgnoreCase("null")){
												followingCntTV.setText("0");	
											}else{
												following_cnt = userProfObj.getString(Constants.userProf_iam_followingCnt).trim();
											}
										}else{ //When empty string came
											followingCntTV.setText("0");
										}
									}else {//When null string came
										followingCntTV.setText("0");
									}

									//Displaying checkIns count
									if(userProfObj.getString(Constants.userProf_checkInsCnt)!=null){
										if(userProfObj.getString(Constants.userProf_checkInsCnt).trim().length()>0){
											if(userProfObj.getString(Constants.userProf_checkInsCnt).trim().equalsIgnoreCase("null")){
												checkinsCntTV.setText("0");
												checkins_cnt = "0";
											}else{
												checkins_cnt=userProfObj.getString(Constants.userProf_checkInsCnt).trim();
											}
										}else{ //When empty string came
											checkinsCntTV.setText("0");
											checkins_cnt = "0";
										}
									}else {//When null string came
										checkinsCntTV.setText("0");
										checkins_cnt = "0";
									}
									//First name
									if(!TextUtils.isEmpty(userProfObj.getString(Constants.userProf_firstName))){
										first_name=userProfObj.getString(Constants.userProf_firstName).trim().toUpperCase();
									}else{
										String[] name = TextUtils.split(user_name, " ");
										if(name.length>0){
											first_name = name[0];
										}else{
											first_name = user_name;	
										}
									}

									//Displaying following status
									if(userID.equalsIgnoreCase(SharedPreferencesUtils.getUserId())){
										//Do nothing when logged & requested user ids are same
									}else{
										if(userProfObj.getString(Constants.userProf_follow_status)!=null){
											if(userProfObj.getString(Constants.userProf_follow_status).trim().length()>0){
												if(userProfObj.getString(Constants.userProf_follow_status).trim().equalsIgnoreCase("null")){
													following_status = "";// Constants.userProf_following_status_not_follow;
												}else{
													following_status = userProfObj.getString(Constants.userProf_follow_status);
												}
											}else{ //When empty string came
												following_status = "";//Constants.userProf_following_status_not_follow;
											}
										}else {//When null string came
											following_status = "";//Constants.userProf_following_status_not_follow;
										}

										onStatusReturnUpdateFollowStatus(false);
									}

									if(userProfObj.getString(Constants.userProf_visibility)!=null){
										if(userProfObj.getString(Constants.userProf_visibility).trim().length()>0){
											if(userProfObj.getString(Constants.userProf_visibility).trim().equalsIgnoreCase("null")){
												profile_visiblity = Constants.userProf_visibility_friends;
											}else{
												profile_visiblity = userProfObj.getString(Constants.userProf_visibility);
											}
										}else{ //When empty string came
											profile_visiblity = Constants.userProf_visibility_friends;
										}
									}else {//When null string came
										profile_visiblity = Constants.userProf_visibility_friends;
									}
									//Updating UI
									updateUI();

								}else{
									//Prompt validation message to the user
									//Prompt validation message to the user
									UIHelperUtil.showToastMessage("Something went wrong with server!....");
								}

							}catch(Exception ex){

							}
						}else{
							//Prompt validation message to the user
							UIHelperUtil.showToastMessage("Something went wrong with server!....");
						}
					} catch (Exception ex) {
						if(BuildConfig.DEBUG){
							Log.e(Constants.logUrl,ex.getMessage());
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

	/**
	 * method for fetching user added events dates
	 */
	private void servicetoGetCalendarSummaryInfo() {
		//TODO: update to user added calendar web service
		String url = Constants.common_url + getString(R.string.get_UserPosted_calendarSummaryInfo);
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		//List of user added events dates
		nameValuePair.add(new BasicNameValuePair(Constants.userProf_userID,user_id));
		nameValuePair.add(new BasicNameValuePair(Constants.userProf_calendar_checked_Events,"1"));
		calendarSummaryEvnetInfo = new HashMap<String, HashMap<String,List<String>>>();
		JsonParser.callBackgroundService(url, nameValuePair,
				new JsonServiceListener() {
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
				Log.d("", " Event Dates RESPONSE: " + jsonResponse);
				if (jsonResponse != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject.getJSONObject(Constants.webserver_response);
						String responseStr = resObj.getString(Constants.webserver_responseInfo);
						if (responseStr != null && responseStr.equalsIgnoreCase(Constants.webserver_response_success)){
							JSONArray resEventDatesObj = resObj.getJSONArray(Constants.cal_DateList_userProfile);
							for(int i=0; i<resEventDatesObj.length();i++){
								JSONObject eventDateObj = resEventDatesObj.getJSONObject(i);
								String date = eventDateObj.getString(Constants.cal_EventDate);
								Calendar calObj = Calendar.getInstance();								
								try{
									calObj.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date));
									String yr =  Integer.toString(calObj.get(Calendar.YEAR));
									String mnth =  Integer.toString(calObj.get(Calendar.MONTH)+ 1) ;
									String day = Integer.toString(calObj.get(Calendar.DAY_OF_MONTH));
									HashMap<String,List<String>> MnthList = calendarSummaryEvnetInfo.get(yr);
									if(MnthList == null){
										MnthList = new HashMap<String,List<String>>();
										calendarSummaryEvnetInfo.put(yr, MnthList);
									}
									List<String> DateList = MnthList.get(mnth);
									if(DateList == null){
										DateList = new ArrayList<String>();
										MnthList.put(mnth,DateList);
									}
									DateList.add(day);

								}catch(Exception ex){
									Log.e(Constants.logUrl,"Error in parsing calendar events date");
								}								
							}

							displayCalendar();
						}else if(responseStr != null && responseStr.equalsIgnoreCase(Constants.webserver_response_fail)){

						}
					}catch(JSONException ex){
						Log.d(Constants.logUrl, ex.getMessage());
					}
				}

			}});
	}

	/*Purpose:  Updates the following status of userID
	 * Method: updateFollowStatus
	 * Input:
	 *       userID: Logged user wants to change the follow/following status of userID's user
	 *       followStatus: follow/unfollow status
	 * Output:
	 *         returns success/failure status of updation.
	 * */
	public void updateFollowStatus(String userID, String followStatus ){
		//Saves following users list
		final List<FollowingUserInfo> followersList = new ArrayList<FollowingUserInfo>();

		//Url for getting following friends info
		String url =  Constants.common_url + getString(R.string.updateFollowStatus);

		//input data to web service
		List<NameValuePair> inputVals = new ArrayList<NameValuePair>() ;
		inputVals.add(new BasicNameValuePair(Constants.UpdateFollowStatus_ReqUserID,SharedPreferencesUtils.getUserId()));
		inputVals.add(new BasicNameValuePair(Constants.UpdateFollowStatus_ReqFollowUserID,userID));
		inputVals.add(new BasicNameValuePair(Constants.UpdateFollowStatus_ReqStatus,followStatus));
		inputVals.add(new BasicNameValuePair(Constants.userProf_visibility,profile_visiblity));

		Log.d(Constants.logUrl,inputVals.toString());
		//Send req to url & handle the result 
		JsonParser.callBackgroundService(url, inputVals, new JsonServiceListener() {

			@Override
			public void showProgress() {
				super.showProgress();
			}

			@Override
			public void hideProgress() {
				super.hideProgress();


			}

			@Override
			public void parseJsonResponse(String jsonResponse) {
				try {
					Log.d(Constants.logUrl," Update Follow Status Service RESPONSE: " + jsonResponse);

					if (jsonResponse!= null) {
						JSONObject jsonObject;						
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject.getJSONObject(Constants.webserver_response);
						String responseStr = resObj.getString(Constants.webserver_responseInfo);
						if (responseStr!=null && responseStr.equalsIgnoreCase(Constants.webserver_response_success)){
							//on successfully unfollowing the user
							following_status = resObj.getString(Constants.userProf_follow_status);
							onStatusReturnUpdateFollowStatus(true);
						}else{
							UIHelperUtil.showToastMessage(activity.getString(R.string.service_toast));
						}
					}else{
						UIHelperUtil.showToastMessage(activity.getString(R.string.service_toast));
					}
				} catch (Exception ex) {
					if(BuildConfig.DEBUG){
						Log.e(Constants.logUrl,ex.getMessage());
					}
				}
			}
		});
	}

	public void onStatusReturnUpdateFollowStatus(boolean updateFollowerCnt){
		if(following_status.equalsIgnoreCase(Constants.userProf_follow_status_follow)){
			followBtnLayout.setVisibility(View.VISIBLE);
			editUserProfileBtn.setVisibility(View.GONE);											
			followBtnLayout.setBackgroundResource(R.drawable.following_btn);
			followBtnTxt.setText(Constants.UI_IamFollowingStatus);
			followBtnTxt.setTextColor(Color.WHITE);
			followBtnImg.setImageResource(R.drawable.following);

			if(updateFollowerCnt){
				//Inc other user following count
				incFollowerCnt();
			}
		}else if(following_status.equalsIgnoreCase(Constants.userProf_follow_status_not_follow)) {
			followBtnLayout.setVisibility(View.VISIBLE);
			editUserProfileBtn.setVisibility(View.GONE);											
			followBtnLayout.setBackgroundResource(R.drawable.follow_btn);
			followBtnTxt.setText(Constants.UI_IamNotFollowingStatus);
			followBtnTxt.setTextColor(activity.getResources().getColor(R.color.com_facebook_blue));
			followBtnImg.setImageResource(R.drawable.follow);

			if(profile_visiblity.equalsIgnoreCase(Constants.userProf_visibility_public) && updateFollowerCnt){
				//Dec other user following count
				decFollowingCnt();
			}
		}else if(following_status.equalsIgnoreCase(Constants.userProf_follow_status_requested)) {
			followBtnLayout.setVisibility(View.VISIBLE);
			editUserProfileBtn.setVisibility(View.GONE);											
			followBtnLayout.setBackgroundResource(R.drawable.requested_btn);
			followBtnTxt.setText(Constants.UI_REQUESTED);
			followBtnTxt.setTextColor(activity.getResources().getColor(R.color.white));
			followBtnImg.setImageResource(R.drawable.requested);
		}else{
			followBtnLayout.setVisibility(View.GONE);
			editUserProfileBtn.setVisibility(View.VISIBLE);
		}
	}
	/****************************SERVER INTERFACES ENDS*************************/

	/*************************Displaying calendar*****************************/
	public void displayCalendar() {		

		mDialog = new Dialog(activity);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View content = activity.getLayoutInflater().inflate(
				R.layout.my_calendar_view, null);
		mDialog.setContentView(content);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		mDialog.setCancelable(false);

		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
				+ year);

		/*
		 * selectedDayMonthYearButton = (Button) mDialog
		 * .findViewById(R.id.selectedDayMonthYear);
		 * selectedDayMonthYearButton.setText("Selected: ");
		 */

		ImageView addEvent = (ImageView) mDialog.findViewById(R.id.add_an_event_frm_cal_iv);
		addEvent.setVisibility(View.GONE);
		addEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment activeFragment = new AddanEventFragment();			
				//displays user page in specified tab.
				displayOnActivity(activeFragment);
				mDialog.dismiss();
			}
		});
		Button cancel = (Button) mDialog.findViewById(R.id.calendar_cancel_btn);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		prevMonth = (ImageView) mDialog.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (month <= 1) {
					month = 12;
					year--;
				} else {
					month--;
				}
				Log.d(tag, "Setting Prev Month in GridCellAdapter: "
						+ "Month: " + month + " Year: " + year);
				setGridCellAdapterToDate(month, year);

			}
		});

		currentMonth = (TextView) mDialog.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));

		nextMonth = (ImageView) mDialog.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (month > 11) {
					month = 1;
					year++;
				} else {
					month++;
				}
				Log.d(tag, "Setting Next Month in GridCellAdapter: "
						+ "Month: " + month + " Year: " + year);
				setGridCellAdapterToDate(month, year);

			}
		});

		calendarView = (GridView) mDialog.findViewById(R.id.calendar);

		// Initialised
		adapter = new GridCellAdapter(activity,
				R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
		mDialog.show();

		//Change month on left & right swipes
		calendarView.setOnTouchListener(new OnTouchListener() {			
			float x1,x2;
			float y1, y2;
			boolean actionDown = false;
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent touchevent) {
				switch (touchevent.getAction())
				{
				// when user first touches the screen we get x and y coordinate
				case MotionEvent.ACTION_MOVE: 
				{
					if(!actionDown){
						x1 = touchevent.getX();
						y1 = touchevent.getY();
						actionDown = true;
					}
					break;
				}
				case MotionEvent.ACTION_DOWN:
					x1 = touchevent.getX();
					y1 = touchevent.getY();
					actionDown = true;
					break;
				case MotionEvent.ACTION_UP: 
				{
					actionDown = false;
					x2 = touchevent.getX();
					y2 = touchevent.getY(); 

					//if left to right sweep event on screen
					if (x1 < x2) 
					{
						//Left to Right Swap Performed: Show previous month
						if (month <= 1) {
							month = 12;
							year--;
						} else {
							month--;
						}
						Log.d(tag, "Setting Prev Month in GridCellAdapter: "
								+ "Month: " + month + " Year: " + year);
						setGridCellAdapterToDate(month, year);
					}

					// if right to left sweep event on screen
					if (x1 > x2)
					{
						//Right to Left Swap Performed: Display next month
						if (month > 11) {
							month = 1;
							year++;
						} else {
							month++;
						}
						Log.d(tag, "Setting Next Month in GridCellAdapter: "
								+ "Month: " + month + " Year: " + year);
						setGridCellAdapterToDate(month, year);
					}

					// if UP to Down sweep event on screen
					if (y1 < y2) 
					{
						//  Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
					}

					//if Down to UP sweep event on screen
					if (y1 > y2)
					{
						//  Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
					}
					break;
				}
				}
				return false;
			}
		});
	}

	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(activity,
				R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private int currentMonth;
		private Button gridcell;
		private TextView num_events_per_day;
		private ImageView AreEventsExistPerDay;
		private final HashMap<String, Integer> eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd-MMM-yyyy");

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId,
				int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
					+ "Year: " + year);
			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			setCurrentMonth(calendar.get(Calendar.MONTH));
			Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

			// Print Month
			printMonth(month, year);

			// Find events in  
			eventsPerMonthMap = findNumberOfEventsPerMonth(String.valueOf(year), String.valueOf(month));
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		public int getCurrentMonth() {
			return currentMonth;
		}

		public void setCurrentMonth(int currentMonth) {
			this.currentMonth = currentMonth;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 * 
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
					+ daysInMonth + " days.");

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			Log.d(tag, "Week Day:" + currentWeekDay + " is "
					+ getWeekDayAsString(currentWeekDay));
			Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

			if (cal.isLeapYear(cal.get(Calendar.YEAR)))
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag,
						"PREV MONTH:= "
								+ prevMonth
								+ " => "
								+ getMonthAsString(prevMonth)
								+ " "
								+ String.valueOf((daysInPrevMonth
										- trailingSpaces + DAY_OFFSET)
										+ i));
				list.add(String
						.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
								+ i)
								+ "-GREY"
								+ "-"
								+ getMonthAsString(prevMonth)
								+ "-"
								+ prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " "
						+ getMonthAsString(currentMonth) + " " + yy);
				if ((i == getCurrentDayOfMonth()) && (currentMonth == getCurrentMonth())) {
					list.add(String.valueOf(i) + "-BLUE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-WHITE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-"
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 * 
		 * @param year
		 * @param month
		 * @return
		 */
		private HashMap<String, Integer> findNumberOfEventsPerMonth(String year,
				String month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			//Find no. of events per month
			if(calendarSummaryEvnetInfo!=null){
				HashMap<String,List<String>> yrEvents = calendarSummaryEvnetInfo.get(year);
				if(yrEvents!=null){
					List<String> mnthEvents = yrEvents.get(month);
					if(mnthEvents!=null ){
						Set<String> uniqueSet = new HashSet<String>(mnthEvents);
						for (String temp : uniqueSet) {
							int freq = Collections.frequency(mnthEvents, temp);
							Log.d(Constants.logUrl,temp + ": " + freq );
							map.put(temp,freq);
						}
					}else{
						return null;
					}
				}else{
					return null;
				}
			}

			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setOnClickListener(this);

			// ACCOUNT FOR SPACING

			// Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			String[] day_color = list.get(position).split("-");
			Log.d(tag, "Current Day: " + list.get(position));
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];



			// Set the Day GridCell
			gridcell.setText(theday);
			gridcell.setTag(theday + "-" + getMonthAsNum(themonth) + "-"
					+ theyear);
			Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
					+ theyear);

			if(!day_color[1].equals("GREY")){
				//Displaying event indications only for current active month but not for leading & next months
				if ((eventsPerMonthMap != null) && (!eventsPerMonthMap.isEmpty())) {
					if (eventsPerMonthMap.containsKey(theday)) {
						/* //Display no. of events count
							num_events_per_day = (TextView) row
									.findViewById(R.id.num_events_per_day);
							Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
							num_events_per_day.setText(numEvents.toString()); */

						//Display indication on the calendar when there is an event
						AreEventsExistPerDay = (ImageView) row.findViewById(R.id.has_events_perday);
						AreEventsExistPerDay.setBackgroundResource(R.drawable.event_indication);
					}
				}
			}
			if (day_color[1].equals("GREY")) {
				gridcell.setTextColor(getResources()
						.getColor(R.color.lightgray));
			}
			if (day_color[1].equals("WHITE")) {
				gridcell.setTextColor(getResources().getColor(
						R.color.lightgray02));				
			}
			if (day_color[1].equals("BLUE")) {
				gridcell.setTextColor(getResources().getColor(R.color.orrange));
			}
			return row;
		}

		@Override
		public void onClick(View view) {
			//TODO: Display events on selected date

			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}

			String date_month_year = (String) view.getTag();
			String[] monthYear = date_month_year.split("-");
			dayStr =  (new DecimalFormat("00")).format(Integer.valueOf(monthYear[0]));
			monthStr = (new DecimalFormat("00")).format(Integer.valueOf(monthYear[1]));
			yearStr = monthYear[2];
			// selectedDayMonthYearButton.setText("Selected: " +
			// date_month_year);
			Log.d("Selected date", "Koti Selected date" + monthStr + " "
					+ yearStr);
			if (Utils.chkStatus()) {
				if (!Constants.longi.equals("0") && !Constants.lati.equals("0")) {
					//						titleTv.setText("CALENDAR");
					//						globalEventType = getString(R.string.calender_events);
					Bundle args = new Bundle();
					args.putBoolean(Constants.Event_display_calendar, true);
					args.putString(Constants.userProf_userID, user_id);
					args.putString(Constants.userProf_userName, user_name);
					args.putString(Constants.Event_display_calendar_day, dayStr);
					args.putString(Constants.Event_display_calendar_month, monthStr);
					args.putString(Constants.Event_display_calendar_year, yearStr);
					Fragment activeFragment = new CheckedEventsFragment();
					activeFragment.setArguments(args);
					displayOnActivity(activeFragment);

				} else {
					UIHelperUtil.showToastMessage(activity.getString(R.string.service_toast));
				}

			} else {
				Utils.networkAlertDialog(activity, getResources()
						.getString(R.string.toast_no_network));
			}

		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}

		public int getMonthAsNum(String monthName) {
			int monthIndex = 0;
			for (int i = 0; i < months.length; i++) {
				if (monthName.equalsIgnoreCase(months[i])) {
					monthIndex = i + 1;
					break;
				}
			}
			return monthIndex;
		}

	}		

	/*****************************************Displaying Calendar ends*************************/
}
