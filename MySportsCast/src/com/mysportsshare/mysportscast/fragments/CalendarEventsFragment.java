package com.mysportsshare.mysportscast.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CalendarEventsActivity;
import com.mysportsshare.mysportscast.activity.StartActivity;
import com.mysportsshare.mysportscast.adapters.EventsAdapter;
import com.mysportsshare.mysportscast.adapters.MenuItemListAdapter;
import com.mysportsshare.mysportscast.adapters.SportsAdapter;
import com.mysportsshare.mysportscast.models.EventInfo;
import com.mysportsshare.mysportscast.models.SportModel;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class CalendarEventsFragment extends Fragment implements OnClickListener,
OnItemClickListener {

	// views
	private TextView titleTv;
	private ListView eventsListView;
	private ImageView menuIv;
	private LinearLayout mDrawer;
	private ListView mDrawerLv;
	/*private LinearLayout mFilterDrawer;
	private ListView mFilterDrawerLv;*/
	private DrawerLayout mDrawerLayout;
	private RelativeLayout footerLayout;	

	private RelativeLayout events_header_llyt;

	// Search bar view
	private RelativeLayout search_header_llyt;
	private ImageView search_header_back_iv;
	private EditText search_header_et;
	private ImageView search_header_iv;

	private TextView currentMonth;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private ImageView addAnEvent;
	private ImageView filterBtn;
	private ImageView search;
	private ImageView back;
	private TextView noEventsStatus;

	// objects
	private MenuItemListAdapter mAdapter;	
	private List<HashMap<String, String>> menuList;
	private String[] mMenus;
	private Handler handler;
	//	private CalendarEventsAdapter eventsAdapter;	commented by koti
	private EventsAdapter eventsAdapter;
	private MenuItemListAdapter mFilterAdapter;	
	private List<HashMap<String, String>> menuFilterList;
	private String[] mFilterMenus;
	private Spinner sportsSpnr;
	private String sportId="0";

	// constants
	private String ITEM = "menuItemName";
	private String ITEM_POSITION = "position";
	private int pageCount = 0;
	private boolean reload;
	private String globalEventType;
	private Boolean isCalenderEvents;
	private boolean isfinishedevent = false;

	// for custom calendar views,objects and constants
	private static final String tag = "MyCalendarFragments";	
	private String dayStr = "", monthStr = "", yearStr = "";
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();

	//Call back method used for communication between fragments
	private int pos; //Holds current position of the events - list when user leaves current fragments	
	private String fetchEventDatesType;
	private String service;
	private String userID;
	private String userName;
	private TextView title;
	private Activity activity;
	private String monthNameStr;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_events, container,false);
		//		if (!Utils.isNetworkConnected(activity)) {
		//			Utils.networkAlertDialog(activity,
		//					getResources().getString(R.string.toast_no_network));
		//		}
		init(view);

		//		menuIv.setOnClickListener(this);
		//		addAnEvent.setOnClickListener(this);
		filterBtn.setOnClickListener(this);
		menuIv.setVisibility(View.GONE);
		addAnEvent.setVisibility(View.GONE);
		filterBtn.setVisibility(View.VISIBLE);

		//Display events menu
		//		menuList = new ArrayList<HashMap<String, String>>();
		//		mMenus = getResources().getStringArray(R.array.menuItems);
		//		for (int i = 0; i < mMenus.length; i++) {
		//			HashMap<String, String> hm = new HashMap<String, String>();
		//			hm.put(ITEM, mMenus[i]);
		//			hm.put(ITEM_POSITION, String.valueOf(i));
		//			menuList.add(hm);
		//		}
		//		mAdapter = new MenuItemListAdapter(activity, menuList);
		//		mDrawerLv.setAdapter(mAdapter);
		//		mDrawerLv.setOnItemClickListener(this);

		//Displays filter menu
		//		menuFilterList = new ArrayList<HashMap<String, String>>();
		//		mFilterMenus = getResources().getStringArray(R.array.filtermenuItems);
		//		for (int i = 0; i < mFilterMenus.length; i++) {
		//			HashMap<String, String> hm = new HashMap<String, String>();
		//			hm.put(ITEM, mFilterMenus[i]);
		//			hm.put(ITEM_POSITION, String.valueOf(i));
		//			menuFilterList.add(hm);
		//		}
		//		mFilterAdapter = new MenuItemListAdapter(activity, menuFilterList);
		//		mFilterDrawerLv.setAdapter(mFilterAdapter);
		//		mFilterDrawerLv.setOnItemClickListener(this);

		boolean inEmulator = "generic".equals(Build.BRAND.toLowerCase());
		if (inEmulator) {
			Constants.lati = "36.9808704578";
			Constants.longi = "97.86034637037";
		}
		// eventsListView.setOnItemClickListener(this);

		//Selected date details
		Bundle args = getArguments();
		if(args!=null){
			dayStr = args.getString(Constants.data_cal_day);
			monthStr = args.getString(Constants.data_cal_mnth);
			monthNameStr = Constants.listMonths[Integer.parseInt(monthStr) - 1];
			yearStr = args.getString(Constants.data_cal_year);	
			fetchEventDatesType = args.getString(Constants.dataReceive);
			userID= args.getString(Constants.userProf_userID);
			userName= args.getString(Constants.userProf_userName);
			if(fetchEventDatesType.equalsIgnoreCase( Constants.data_cal_userCheckedEvents)){
				service = getString(R.string.get_user_posted_cal_events);
			}else if(fetchEventDatesType.equalsIgnoreCase( Constants.data_cal_userPostEvents)){
				service = getString(R.string.get_user_posted_cal_events);
			}else{
				service = getString(R.string.calender_events);
			}
		}

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if(savedInstanceState!=null){
			pos = savedInstanceState.getInt("ListItemPosition", 0);	
		}
		//		updateUI();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(eventsAdapter!=null){
			outState.putInt("ListItemPosition", eventsListView.getFirstVisiblePosition());	
		}

	}



	/**
	 * initialization
	 */
	private void init(View view) {
		titleTv = (TextView) view.findViewById(R.id.title_tv);
		eventsListView = (ListView) view.findViewById(R.id.events_lv);
		menuIv = (ImageView) view.findViewById(R.id.event_menu_iv);
		mDrawer = (LinearLayout) view.findViewById(R.id.drawer);
		mDrawerLv = (ListView) view.findViewById(R.id.drawer_list);
		mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
		noEventsStatus = (TextView) view.findViewById(R.id.eventsStatus);
		/*mFilterDrawer = (LinearLayout) view.findViewById(R.id.filterdrawer);
		mFilterDrawerLv = (ListView) view.findViewById(R.id.filter_drawer_list);*/
		footerLayout = (RelativeLayout) view
				.findViewById(R.id.loading_footer_layout);

		addAnEvent = (ImageView) view.findViewById(R.id.add_an_event_iv);

		filterBtn = (ImageView) view.findViewById(R.id.filter_btn);

		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);

		events_header_llyt = (RelativeLayout) activity.findViewById(
				R.id.events_header_llyt);

		search = (ImageView) activity.findViewById(R.id.search_iv);
		addAnEvent = (ImageView) activity.findViewById(
				R.id.add_an_event_iv);
		back = (ImageView) activity.findViewById(R.id.back_iv);


		search_header_llyt = (RelativeLayout) activity.findViewById(
				R.id.search_header_llyt);
		search_header_back_iv = (ImageView) activity.findViewById(
				R.id.search_header_back_iv);
		search_header_et = (EditText) activity.findViewById(
				R.id.search_header_et);
		search_header_iv = (ImageView) activity.findViewById(
				R.id.search_header_btn_iv);

		search_header_back_iv.setOnClickListener(this);
		search_header_iv.setOnClickListener(this);
		handler = new Handler();		

		//		RelativeLayout mainLayout = (RelativeLayout) view.findViewById(R.id.mainEventLayout);
		//		mainLayout.setOnTouchListener(new OnTouchListener() {
		//			@Override
		//			public boolean onTouch(View v, MotionEvent event) {
		//				if(v.getId() == R.id.search_header_et){
		//					((CalendarEventActivity)activity).openKeyboard(v);
		//				}else{
		//					((CalendarEventActivity)activity).hideKeyboard(v);
		//				}
		//				return false;
		//			}
		//		});
		//		eventsListView.setOnTouchListener(new OnTouchListener() {
		//			@Override
		//			public boolean onTouch(View v, MotionEvent event) {
		//				((CalendarEventActivity)activity).hideKeyboard(v);
		//				return false;
		//			}
		//		});
	}	

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		//		case R.id.event_menu_iv:
		//			showMenu();
		//			break;

		//		case R.id.search_header_back_iv:
		//			search_header_llyt.setVisibility(View.GONE);
		//			events_header_llyt.setVisibility(View.VISIBLE);
		//			((CalendarEventActivity)activity).hideKeyboard(v);
		//			break;

		//		case R.id.search_iv:
		//			search_header_llyt.setVisibility(View.VISIBLE);
		//
		//			events_header_llyt.setVisibility(View.GONE);
		//			InputMethodManager keyboardMgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		//			if(search_header_et.requestFocus()){
		//				((MainActivity)activity).openKeyboard(v);
		//			}
		//
		//			break;

		//		case R.id.search_header_btn_iv:
		//			search_header_llyt.setVisibility(View.GONE);
		//			events_header_llyt.setVisibility(View.VISIBLE);
		//
		//			//displays user page in specified tab.
		//			if(search_header_et.getText().toString().trim().length()==0){
		//				//No action performed when there is no search string 
		//			}else{
		//				//Search page is opened with entered text.
		//				mListener.passData(search_header_et.getText().toString().trim(), Constants.ToFragment_search);	
		//				((MainActivity)activity).hideKeyboard(v);
		//			}			
		//			break;

		//		case R.id.add_an_event_iv:
		//			// Creates & loads addEvent fragment
		//			//			Fragment activeFragment = new AddanEventFragment();
		//			//			FragmentManager fragmentManager = getFragmentManager();
		//			//			FragmentTransaction ft = fragmentManager.beginTransaction();
		//			//			ft.replace(R.id.home_frame, activeFragment);
		//			//			ft.addToBackStack(null);
		//			//			ft.commit();
		//
		//			//Create fragment & pass message(UserID) users profile			
		//			Fragment activeFragment = new AddanEventFragment();			
		//			//displays user page in specified tab.
		//			((MainActivity)activity).pushFragments(((MainActivity)activity).getmCurrentTab(), activeFragment, false,true);
		//
		//			break;

		case R.id.filter_btn:
			//					//			UIHelperUtil.showToastMessage("Yet to implement..!");
			//					showFilterMenu();
			//					//Loads events dates from server
			//					//			servicetoGetCalendarSummaryInfo();
			//displays user page in specified tab.
			Fragment activeFragment1 = new DayFilterFragment();			
			//displays user page in specified tab.
			Bundle args = new Bundle();
			args.putBoolean(Constants.TAG_FROM_CALENDAR_EVENTS, true);
			activeFragment1.setArguments(args);
			((CalendarEventsActivity)activity).push(activeFragment1);
			break;

		case R.id.back_iv:
			/*
			 * InputMethodManager imm = (InputMethodManager)
			 * activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			 * imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
			 */

			((CalendarEventsActivity)activity).onBackPressed();
			//			((MainActivity)activity).popFragments();

			break;

			//		case R.id.logout_btn:
			//			logOut(v);
			//			break;

			//		case R.id.setting_iv:
			//			Log.v("", "Event fragment:");
			//			((CalendarEventActivity) activity).showSettingsMenu();
			//			//			((MainActivity) activity).popup(v);
			//			break;

		}
	}

	/**
	 * method for logout
	 */
	private void logOut(View v) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

		if (SharedPreferencesUtils.getUserId() != null
				&& SharedPreferencesUtils.getUserId().length() > 0) {

			SharedPreferencesUtils.setUserName("");
			SharedPreferencesUtils.setUserId("");
			SharedPreferencesUtils.setOauthUserId("");
			SharedPreferencesUtils.setUserProfilePicPath("");
			/*
			 * Intent intentUserDetails = new Intent(activity,
			 * StartActivity.class);
			 * intentUserDetails.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
			 * Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			 * startActivity(intentUserDetails);
			 */
			activity.finish();
			Intent i = new Intent(activity, StartActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			/*
			 * if(StartActivity.instance != null){
			 * StartActivity.instance.finish(); }
			 */

			UIHelperUtil.showToastMessage("You are now logged out");
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.v("", "Event fragment updated");
		eventsAdapter = null;
		TextView title = (TextView) activity.findViewById(
				R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);
		//TODO: display selected date
		title.setText("EVENTS");
		TextView tvEventType = (TextView) activity.findViewById(
				R.id.title_bar_tv_event_type);
		tvEventType.setVisibility(View.GONE);
		ImageView search = (ImageView) activity.findViewById(
				R.id.search_iv);
		ImageView settingBtn = (ImageView) activity.findViewById(
				R.id.setting_iv);
		settingBtn.setImageResource(R.drawable.settings_btn);
		settingBtn.setOnClickListener(this);
		settingBtn.setVisibility(View.GONE);
		back.setVisibility(View.VISIBLE);

		search.setVisibility(View.GONE);
		addAnEvent.setVisibility(View.GONE);

		back.setOnClickListener(this);

		//Close drawers on starting fragment
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		//		mDrawerLayout.closeDrawer(mFilterDrawer);

		eventsListView.removeFooterView(footerLayout);
		eventsListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					if (eventsListView.getLastVisiblePosition() >= eventsListView
							.getCount() - 1 && reload) {
						footerLayout.setVisibility(View.VISIBLE);
						handler.postDelayed(new Runnable() {

							@Override
							public void run() {
								if (Utils.chkStatus()) {
									pageCount += 1;
									footerLayout.setVisibility(View.GONE);
									eventsListView
									.removeFooterView(footerLayout);
									Log.d("", "Koti event type:"
											+ globalEventType);
									if (reload) {
										// footerLayout.setVisibility(View.VISIBLE);
										if (isCalenderEvents) {
											servicetoGettingEvents(
													globalEventType,
													Constants.longi,
													Constants.lati, yearStr,
													monthStr,dayStr,
													String.valueOf(pageCount),"0");
										} else {
											servicetoGettingEvents(
													globalEventType,
													Constants.longi,
													Constants.lati, "", "","",
													String.valueOf(pageCount),sportId);
										}
									} else {
										/*UIHelperUtil
										.showToastMessage("The list is up to date.");*/
										UIHelperUtil
										.showToastMessage("This is the Last Post");
										footerLayout.setVisibility(View.GONE);
										eventsListView
										.removeFooterView(footerLayout);
									}
								} else {
									UIHelperUtil
									.showToastMessage(getString(R.string.toast_no_network));
								}

							}
						}, 750);
					} else {
						UIHelperUtil
						.showToastMessage("This is the Last Post");
						eventsListView.removeFooterView(footerLayout);
						footerLayout.setVisibility(View.GONE);
					}
					Log.d("",
							"last pos:"
									+ eventsListView.getLastVisiblePosition()
									+ " : " + eventsListView.getCount() + " : "
									+ pageCount + " : " + reload);
				} else {
					eventsListView.removeFooterView(footerLayout);
					footerLayout.setVisibility(View.GONE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

		if (Utils.chkStatus()) {
			if (!Constants.longi.equals("0") && !Constants.lati.equals("0")) {
				if((eventsAdapter==null)){
					titleTv.setText(monthStr + "/"+dayStr+"/"+yearStr);
					//					titleTv.setText(monthNameStr +". "+ dayStr +","+yearStr);
					globalEventType = getString(R.string.calender_events);
					isCalenderEvents = true;

					servicetoGettingEvents(
							globalEventType,
							Constants.longi,
							Constants.lati, yearStr,
							monthStr,dayStr,
							String.valueOf(pageCount),"0");

				}else{
					titleTv.setText(monthStr + "/"+dayStr+"/"+yearStr);
					//					titleTv.setText(monthNameStr +". "+ dayStr +","+yearStr);
					globalEventType = getString(R.string.calender_events);
					isCalenderEvents = true;						
					eventsListView.setAdapter(eventsAdapter);
					eventsListView.setSelection(pos);
					eventsListView.setOnItemClickListener(this);						
				}
			} else {
				globalEventType = getString(R.string.upcoming_events);
				UIHelperUtil.showGPSDialogSettingsFromAppContext(activity);
			}

		} else {
			Utils.networkAlertDialog(activity,
					getResources().getString(R.string.toast_no_network));
		}
		Log.d("", "Lati and langi" + Constants.lati + "  " + Constants.longi);

		if(fetchEventDatesType.equalsIgnoreCase( Constants.data_cal_userCheckedEvents)){
			if(userID.equalsIgnoreCase(SharedPreferencesUtils.getUserId())){
				title.setText("MY CHECK-INS");
			}else{
				title.setText(userName +" CHECK-IN");	
			}
		}else if(fetchEventDatesType.equalsIgnoreCase( Constants.data_cal_userPostEvents)){
			if(userID.equalsIgnoreCase(SharedPreferencesUtils.getUserId())){
				title.setText("MY EVENTS");
			}else{
				title.setText(userName +" EVENTS");	
			}
		}else{
			title.setText("EVENTS");
		}

		//Updating filter icon
		if(SharedPreferencesUtils.dayisFilterApplyed()){
			filterBtn.setImageResource(R.drawable.filter_apply);
		}else{
			filterBtn.setImageResource(R.drawable.filter_icon);
		}

	}

	/**
	 * Function to show settings alert dialog On pressing Settings button will
	 * lauch Settings Options
	 * */
	private void showGPSDialogSettings() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
		alertDialog.setTitle("Location Settings");
		alertDialog.setMessage("Enable Location");
		// setting Dialog cancelable to false
		alertDialog.setCancelable(false);
		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				startActivity(new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				dialog.cancel();
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Close",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		// Showing Alert Message
		alertDialog.show();

	}

	//	// show menu
	//	public void showMenu() {
	//		//Opening menu drawer
	//		if (mDrawerLayout.isDrawerOpen(mDrawer)) {
	//			mDrawerLayout.closeDrawer(mDrawer);
	//		} else {
	//			mDrawerLayout.openDrawer(mDrawer);
	//		}
	//
	//		//Closing filter drawer if it is opened
	//		if (mDrawerLayout.isDrawerOpen(mFilterDrawer)) {
	//			mDrawerLayout.closeDrawer(mFilterDrawer);
	//		}
	//	}
	//
	//	// show filter menu
	//	public void showFilterMenu() {
	//		//Opens filter
	//		if (mDrawerLayout.isDrawerOpen(mFilterDrawer)) {
	//			mDrawerLayout.closeDrawer(mFilterDrawer);
	//		} else {
	//			mDrawerLayout.openDrawer(mFilterDrawer);
	//		}
	//
	//		//Closing menu drawer if it is opened
	//		if (mDrawerLayout.isDrawerOpen(mDrawer)) {
	//			mDrawerLayout.closeDrawer(mDrawer);
	//		}
	//	}
	//
	//	// show method for each menu item
	//	private void showMenuitemDetails(int position) {
	//		switch (position) {
	//		case 0:
	//			pageCount = 0;
	//			sportId="0";
	//			eventsAdapter = null;
	//			if (Utils.chkStatus()) {
	//				if (Constants.longi != "0" && Constants.lati != "0") {
	//					titleTv.setText("UPCOMING");
	//					globalEventType = getString(R.string.upcoming_events);
	//					isCalenderEvents = false;
	//					servicetoGettingEvents(getString(R.string.upcoming_events),
	//							Constants.longi, Constants.lati, "", "","", "0","0");
	//				} else {
	//					showGPSDialogSettings();
	//				}
	//
	//			} else {
	//				Utils.networkAlertDialog(activity, getResources()
	//						.getString(R.string.toast_no_network));
	//			}
	//			mDrawerLayout.openDrawer(mDrawer);
	//			break;
	//
	//		case 1:
	//			pageCount = 0;
	//			sportId="0";
	//			eventsAdapter = null;
	//			if (Utils.chkStatus()) {
	//				if (Constants.longi != "0" && Constants.lati != "0") {
	//					titleTv.setText("INPROGRESS");
	//					globalEventType = getString(R.string.inprogress_events);
	//					isCalenderEvents = false;
	//					servicetoGettingEvents(
	//							getString(R.string.inprogress_events),
	//							Constants.longi, Constants.lati, "", "", "","0","0");
	//				} else {
	//					showGPSDialogSettings();
	//				}
	//
	//			} else {
	//				Utils.networkAlertDialog(activity, getResources()
	//						.getString(R.string.toast_no_network));
	//			}
	//			mDrawerLayout.openDrawer(mDrawer);
	//			break;
	//		case 2:
	//			pageCount = 0;
	//			sportId="0";
	//			eventsAdapter = null;
	//			if (Utils.chkStatus()) {
	//				if (Constants.longi != "0" && Constants.lati != "0") {
	//					titleTv.setText("FINISHED");
	//					globalEventType = getString(R.string.finished_events);
	//					isCalenderEvents = false;
	//					servicetoGettingEvents(getString(R.string.finished_events),
	//							Constants.longi, Constants.lati, "", "", "","0","0");
	//				} else {
	//					showGPSDialogSettings();
	//				}
	//
	//			} else {
	//				Utils.networkAlertDialog(activity, getResources()
	//						.getString(R.string.toast_no_network));
	//			}
	//			mDrawerLayout.openDrawer(mDrawer);
	//			break;
	//
	//		case 3:
	//			/*	pageCount = 0;
	//			sportId="0";
	//			eventsAdapter = null;
	//			servicetoGetCalendarSummaryInfo();
	//			mDrawerLayout.openDrawer(mDrawer);*/
	//
	//			Intent i = new Intent(activity,CustomMultiMonth.class);
	//			i.putExtra(Constants.dataReceive,  Constants.data_cal_allEvents);
	//			startActivity(i);
	//			break;
	//		case 4:
	//			mDrawerLayout.closeDrawer(mDrawer);
	//			//Create fragment & pass message(UserID) users profile			
	//			Fragment activeFragment = new AddanEventFragment();			
	//			//displays user page in specified tab.
	//			//			((CalendarEventActivity)activity).pushFragments(((CalendarEventActivity)activity).getmCurrentTab(), activeFragment, false,true);						
	//			break;
	//		default:
	//			break;
	//		}
	//	}
	//
	//	private void showFilterMenuitemDetails(int position) {
	//		switch (position) {
	//		case 0:
	//			pageCount = 0;
	//			eventsAdapter = null;
	//			serviceToGetSportsList();
	//			mDrawerLayout.closeDrawer(mFilterDrawer);
	//			break;
	//		case 1:
	//			pageCount = 0;
	//			eventsAdapter = null;
	//			servicetoGetCalendarSummaryInfo();
	//			mDrawerLayout.closeDrawer(mFilterDrawer);
	//			break;
	//		}
	//	}
	//
	public void setAdapter(List<EventInfo> eventsList, String pageId,
			String eventType, boolean isfinishedevent) {
		List<EventInfo> tempList = new ArrayList<EventInfo>();
		if (eventsAdapter != null) {
			tempList.addAll(eventsAdapter.getOldEventsList());
			tempList.addAll(eventsList);
		}
		Log.v("", "Temp: " + tempList.size());
		if (eventsAdapter == null && pageId.equalsIgnoreCase("0")) {
			eventsAdapter = new EventsAdapter(activity, eventsList,
					eventType, isfinishedevent);
			eventsListView.setAdapter(eventsAdapter);
			//			Utils.getListViewSize(eventsListView);
		} else {
			eventsAdapter.updateEvents(tempList);
		}
		eventsListView.setOnItemClickListener(this);
		if(eventsListView.getCount() > 0){
			eventsListView.setVisibility(View.VISIBLE);
			noEventsStatus.setVisibility(View.GONE);
		}else{
			eventsListView.setVisibility(View.GONE);
			noEventsStatus.setVisibility(View.VISIBLE);
		}
	}


	@Override
	public void onItemClick(AdapterView<?> adView, View view, int position,
			long arg3) {
		if (adView.getId() == R.id.events_lv) {
			EventInfo eventInfo = (EventInfo) adView
					.getItemAtPosition(position);
			Log.v("", "EventId: " + eventInfo + " : " + position + " : "
					+ eventInfo.getEventId());

			Fragment activeFragment = new EventDetailsFragment();
			//			FragmentTransaction transaction = activity
			//					.getSupportFragmentManager().beginTransaction();
			Bundle bundle = new Bundle();
			// TODO change this used id.
			bundle.putString(Constants.KEY_USER_ID,
					SharedPreferencesUtils.getUserId());
			bundle.putString(Constants.KEY_EVENT_ID, eventInfo.getEventId());
			bundle.putString(Constants.KEY_EVENT_TYPE, titleTv.getText()
					.toString().trim());
			activeFragment.setArguments(bundle);			
			((CalendarEventsActivity)activity).push(activeFragment);
			//			transaction.add(R.id.home_frame, activeFragment);	
			//			transaction.addToBackStack("eventDetailsFragment");
			//			transaction.commit();
		}
	}
	/*************************------SERVER INTERACTION STARTS-----***************************/

	/**
	 * method for fetching events from the server
	 */
	private void servicetoGettingEvents(final String eventsType, String longi,
			String lati, String yearStr, String monthStr, String dayStr,final String pageId, String sport_id) {

		final List<EventInfo> eventsList = new ArrayList<EventInfo>();
		/*
		 * get_upcoming_events.php?user_id=3&user_lat=43.59685959999999&user_lng=
		 * 3.8502617000000328&page_id=0
		 */
		String url = Constants.common_url + service;
		// Log.v("", "URL getEvents: " +
		// SharedPreferencesUtils.getUserId()+" "+lati+" "+longi+" "+pageId+""+"");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user_id",
				userID));
		nameValuePairs.add(new BasicNameValuePair("user_lat", SharedPreferencesUtils.getLatitude()));
		nameValuePairs.add(new BasicNameValuePair("user_lng", SharedPreferencesUtils.getLongitude()));				
		if (yearStr.length() > 0 && monthStr.length() > 0) {
			nameValuePairs.add(new BasicNameValuePair("year", yearStr));
			nameValuePairs.add(new BasicNameValuePair("month", monthStr));
			nameValuePairs.add(new BasicNameValuePair("day", dayStr));
			nameValuePairs.add(new BasicNameValuePair("time_zone",TimeZone.getDefault().getID()));
			if(fetchEventDatesType.equalsIgnoreCase( Constants.data_cal_userCheckedEvents)){
				nameValuePairs.add(new BasicNameValuePair(Constants.userProf_calendar_checked_Events, "1"));
			}else if(fetchEventDatesType.equalsIgnoreCase( Constants.data_cal_userPostEvents)){
				nameValuePairs.add(new BasicNameValuePair(Constants.userProf_calendar_checked_Events, "0"));
			}
			nameValuePairs.add(new BasicNameValuePair("page_id", pageId));	
		}
		if(service.equalsIgnoreCase(getString(R.string.calender_events))){
			//TODO: set filter only for calendar events
			/*nameValuePairs.add(new BasicNameValuePair("sport_id", SharedPreferencesUtils.getSportIds()));
			nameValuePairs.add(new BasicNameValuePair("radious", SharedPreferencesUtils.getRange()));*/
			nameValuePairs.add(new BasicNameValuePair("sport_id", SharedPreferencesUtils.daygetSportIds()));
			nameValuePairs.add(new BasicNameValuePair("radious", SharedPreferencesUtils.daygetRange()));
			//			nameValuePairs.add(new BasicNameValuePair("radious", "100"));
		}else{
			//don't set filter only for checked and poseted events
			nameValuePairs.add(new BasicNameValuePair("sport_id", SharedPreferencesUtils.daygetSportIds()));
			nameValuePairs.add(new BasicNameValuePair("radious", SharedPreferencesUtils.daygetRange()));
			//			nameValuePairs.add(new BasicNameValuePair("radious", "100"));
		}

		Log.v("", "GetEventInfo URL: " + url + "user_id="
				+ SharedPreferencesUtils.getUserId() + "user_lat=" + lati
				+ "&user_lng=" + longi + "&page_id=" + pageId + "&year="
				+ yearStr + "&month=" + monthStr+"&day"+dayStr);
		JsonParser.callBackgroundService(url, nameValuePairs,
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
				Log.d("", " Event Service RESPONSE: " + jsonResponse);
				if (jsonResponse != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject
								.getJSONObject("Response");
						String responseStr = resObj
								.getString("ResponseInfo");
						if (responseStr != null
								&& responseStr
								.equalsIgnoreCase("SUCCESS")) {

							JSONArray upcomingEventsArr = null;
							try {
								upcomingEventsArr = resObj
										.getJSONArray("events_list");
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (upcomingEventsArr != null) {
								EventInfo.parseData(upcomingEventsArr,eventsList); 
							} else {
								UIHelperUtil
								.showToastMessage("No events found");
							}

						} else if (responseStr != null
								&& responseStr
								.equalsIgnoreCase("FAILURE")) {
							UIHelperUtil
							.showToastMessage("No events found");
						} else {
							UIHelperUtil
							.showToastMessage(getString(R.string.service_toast));
						}
						String eventtype = "";
						if (eventsType
								.equalsIgnoreCase(getString(R.string.upcoming_events))) {
							eventtype = "Attending";
							isfinishedevent = false;
						} else if (eventsType
								.equalsIgnoreCase(getString(R.string.inprogress_events))) {
							eventtype = "here";
							isfinishedevent = false;
						} else if (eventsType
								.equalsIgnoreCase(getString(R.string.finished_events))) {
							eventtype = "were here";
							isfinishedevent = true;
						} else {
							isfinishedevent = false;
							eventtype = "Attending";
						}
						setAdapter(eventsList, pageId, eventtype,
								isfinishedevent);

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						Log.v("", "URL getEvents: " + eventsList.size()
								+ " : " + pageId);
						if (eventsList.size() >= 10) {
							reload = true;
						} else {
							reload = false;
						}
					}
				} else {
					UIHelperUtil
					.showToastMessage(getString(R.string.service_toast));
				}
			}
		});
	}

	/**service to get sports list from server
	 * */
	private void serviceToGetSportsList() {
		String url = Constants.common_url +
				getResources().getString(R.string.get_sports_list);

		JsonParser.callBackgroundService(url, null, new JsonServiceListener() {
			ProgressDialog pd;
			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(activity);
				pd.setMessage("Loading...");
				pd.show();
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
			}

			@Override
			public void hideProgress() {
				if(pd != null && pd.isShowing()) {
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
			if(jsonResult!=null){
				Log.d(Constants.logUrl,"response:  " + jsonResult);
				JSONObject reader = new JSONObject(jsonResult);
				JSONObject sys  = reader.getJSONObject("Response");
				String response = sys.getString("ResponseInfo");
				final List<SportModel> sportsList = new ArrayList<SportModel>();
				if(response != null && response.equals("SUCCESS")) {
					JSONArray sportsDetailsJson = sys.getJSONArray("sprots_list");
					for(int i=0; i<sportsDetailsJson.length(); i++) {
						JSONObject sportObject = sportsDetailsJson.getJSONObject(i);
						SportModel sportModel = new SportModel();
						sportModel.setSportId(sportObject.getString("sport_id"));
						sportModel.setSportName(sportObject.getString("sport_name"));
						sportModel.setMaxPlayersOnField(sportObject.getString("max_players_on_field"));
						sportModel.setSportRounds(sportObject.getString("rounds"));
						sportsList.add(sportModel);
						System.out.println("list: " + sportsList.get(i));
					}

					if(sportsList != null){
						SportsAdapter sportsAdapter = new SportsAdapter(activity, sportsList);  
						AlertDialog.Builder bldr = new AlertDialog.Builder(activity);
						bldr.setTitle("Select sport");					
						bldr.setOnCancelListener(new OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								dialog.dismiss();
							}
						});
						bldr.setAdapter(sportsAdapter,new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int pos) {
								sportId = sportsList.get(pos).getSportId();																					
								if (!Constants.longi.equals("0") && !Constants.lati.equals("0")) {
									//Applying filter on current displayed events 
									if(globalEventType.equalsIgnoreCase(getString(R.string.upcoming_events))){
										titleTv.setText("UPCOMING");
										isCalenderEvents = false;	
										servicetoGettingEvents(getString(R.string.upcoming_events),
												Constants.longi, Constants.lati, "", "", "","0",sportId);
									}else if(globalEventType.equalsIgnoreCase(getString(R.string.inprogress_events))){
										titleTv.setText("INPROGRESS");
										isCalenderEvents = false;
										servicetoGettingEvents(getString(R.string.inprogress_events),
												Constants.longi, Constants.lati, "", "", "","0",sportId);
									}else if(globalEventType.equalsIgnoreCase(getString(R.string.finished_events))){
										titleTv.setText("FINISHED");
										isCalenderEvents = false;									
										servicetoGettingEvents(getString(R.string.finished_events),
												Constants.longi, Constants.lati, "", "","", "0",sportId);
									}else if(globalEventType.equalsIgnoreCase(getString(R.string.calender_events))){
										//										titleTv.setText("CALENDAR");
										titleTv.setText(monthStr + "/"+dayStr+"/"+yearStr);
										//										titleTv.setText(monthNameStr +". "+ dayStr +","+yearStr);
										isCalenderEvents = true;
										servicetoGettingEvents(globalEventType,Constants.longi,Constants.lati, yearStr,monthStr,dayStr, String.valueOf(pageCount),sportId);
									} else {
										UIHelperUtil.showGPSDialogSettingsFromAppContext(activity);
									}
								}

							}});
						bldr.show();
					}
				} else if(response!=null && response.equalsIgnoreCase("FAILURE")){
					UIHelperUtil.showToastMessage("No sports list found");
				}
			} else{
				UIHelperUtil.showToastMessage("No sports list found");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}


	}
	/*************************------SERVER INTERACTION ENDS-----***************************/

}
