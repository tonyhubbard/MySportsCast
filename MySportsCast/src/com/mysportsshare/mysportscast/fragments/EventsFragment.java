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
import java.util.TimeZone;

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
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CustomMultiMonth;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.activity.StartActivity;
import com.mysportsshare.mysportscast.adapters.EventsAdapter;
import com.mysportsshare.mysportscast.adapters.MenuItemListAdapter;
import com.mysportsshare.mysportscast.adapters.SportsAdapter;
import com.mysportsshare.mysportscast.models.EventInfo;
import com.mysportsshare.mysportscast.models.SportModel;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.DataPassListener;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class EventsFragment extends Fragment implements OnClickListener,
		OnItemClickListener {

	// views
	private TextView titleTv;
	private ListView eventsListView;
	private ImageView menuIv;
	private LinearLayout mDrawer;
	private ListView mDrawerLv;
	// private LinearLayout mFilterDrawer;
	// private ListView mFilterDrawerLv;
	private DrawerLayout mDrawerLayout;
	private RelativeLayout footerLayout;

	private RelativeLayout events_header_llyt;

	// Search bar view
	private RelativeLayout search_header_llyt;
	private ImageView search_header_back_iv;
	private EditText search_header_et;
	private ImageView search_header_iv;
	private AdView adView;

	private TextView currentMonth;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private ImageView addAnEvent;
	private ImageView filterBtn;
	private ImageView search;
	private ImageView back;

	// objects
	private MenuItemListAdapter mAdapter;
	private List<HashMap<String, String>> menuList;
	private String[] mMenus;
	private Handler handler;
	private EventsAdapter eventsAdapter;
	private GridCellAdapter adapter;
	private MenuItemListAdapter mFilterAdapter;
	private List<HashMap<String, String>> menuFilterList;
	private String[] mFilterMenus;
	private Spinner sportsSpnr;
	private String sportId = "0";

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
	private Calendar _calendar;
	private int month, year;
	private String dayStr, monthStr, yearStr;
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";
	private Dialog mDialog;
	private HashMap<String, HashMap<String, List<String>>> calendarSummaryEvnetInfo;

	// Call back method used for communication between fragments
	private DataPassListener mListener;
	private int pos; // Holds current position of the events - list when user
						// leaves current fragments
	private Activity activity;
	private TextView noEventsStatus;
	private TextView createEventTv;
	private String monthNameStr;

	// Display interstitial adds
	// private InterstitialAd interstitial;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (DataPassListener) activity;
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_events, container,
				false);
		if (!Utils.isNetworkConnected(activity)) {
			Utils.networkAlertDialog(activity,
					getResources().getString(R.string.toast_no_network));
		}
		globalEventType = "get_calendar_events.php";
		init(view);

		Calendar currentDate = Calendar.getInstance();
		yearStr = Long.toString(currentDate.get(Calendar.YEAR));
		monthNameStr = Constants.listMonths[currentDate.get(Calendar.MONTH)];
		monthStr = Long.toString(currentDate.get(Calendar.MONTH) + 1);
		dayStr = Long.toString(currentDate.get(Calendar.DAY_OF_MONTH));

		menuIv.setOnClickListener(this);
		addAnEvent.setOnClickListener(this);
		filterBtn.setOnClickListener(this);
		createEventTv.setOnClickListener(this);

		// Display events menu
		menuList = new ArrayList<HashMap<String, String>>();
		mMenus = getResources().getStringArray(R.array.menuItems);
		for (int i = 0; i < mMenus.length; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(ITEM, mMenus[i]);
			hm.put(ITEM_POSITION, String.valueOf(i));
			menuList.add(hm);
		}
		mAdapter = new MenuItemListAdapter(activity, menuList);
		mDrawerLv.setAdapter(mAdapter);
		mDrawerLv.setOnItemClickListener(this);

		/*
		 * //Displays filter menu menuFilterList = new ArrayList<HashMap<String,
		 * String>>(); mFilterMenus =
		 * getResources().getStringArray(R.array.filtermenuItems); for (int i =
		 * 0; i < mFilterMenus.length; i++) { HashMap<String, String> hm = new
		 * HashMap<String, String>(); hm.put(ITEM, mFilterMenus[i]);
		 * hm.put(ITEM_POSITION, String.valueOf(i)); menuFilterList.add(hm); }
		 * mFilterAdapter = new MenuItemListAdapter(activity, menuFilterList);
		 * mFilterDrawerLv.setAdapter(mFilterAdapter);
		 * mFilterDrawerLv.setOnItemClickListener(this);
		 */

		boolean inEmulator = "generic".equals(Build.BRAND.toLowerCase());
		if (inEmulator) {
			Constants.lati = "36.9808704578";
			Constants.longi = "97.86034637037";
		}
		// eventsListView.setOnItemClickListener(this);

		// TODO
		getPNSDetails();
		return view;
	}

	private void getPNSDetails() {
		Bundle bundle = getArguments();
		if (bundle == null || !bundle.containsKey("message")) {

		}
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			pos = savedInstanceState.getInt("ListItemPosition", 0);
		}
		updateUI();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (eventsAdapter != null) {
			outState.putInt("ListItemPosition",
					eventsListView.getFirstVisiblePosition());
		}

	}

	/**
	 * 
	 * @param month
	 * @param year
	 */
	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(activity, R.id.calendar_day_gridcell,
				month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	/**
	 * initialization
	 */
	private void init(View view) {
		titleTv = (TextView) view.findViewById(R.id.title_tv);
		eventsListView = (ListView) view.findViewById(R.id.events_lv);
		noEventsStatus = (TextView) view.findViewById(R.id.eventsStatus);
		createEventTv = (TextView) view.findViewById(R.id.createEvent);
		menuIv = (ImageView) view.findViewById(R.id.event_menu_iv);
		mDrawer = (LinearLayout) view.findViewById(R.id.drawer);
		mDrawerLv = (ListView) view.findViewById(R.id.drawer_list);
		mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
		/*
		 * mFilterDrawer = (LinearLayout) view.findViewById(R.id.filterdrawer);
		 * mFilterDrawerLv = (ListView)
		 * view.findViewById(R.id.filter_drawer_list);
		 */
		footerLayout = (RelativeLayout) view
				.findViewById(R.id.loading_footer_layout);

		addAnEvent = (ImageView) view.findViewById(R.id.add_an_event_iv);

		filterBtn = (ImageView) view.findViewById(R.id.filter_btn);

		events_header_llyt = (RelativeLayout) activity
				.findViewById(R.id.events_header_llyt);

		search = (ImageView) activity.findViewById(R.id.search_iv);
		addAnEvent = (ImageView) activity.findViewById(R.id.add_an_event_iv);
		back = (ImageView) activity.findViewById(R.id.back_iv);

		search_header_llyt = (RelativeLayout) activity
				.findViewById(R.id.search_header_llyt);
		search_header_back_iv = (ImageView) activity
				.findViewById(R.id.search_header_back_iv);
		search_header_et = (EditText) activity
				.findViewById(R.id.search_header_et);
		search_header_iv = (ImageView) activity
				.findViewById(R.id.search_header_btn_iv);

		search_header_back_iv.setOnClickListener(this);
		search_header_iv.setOnClickListener(this);
		handler = new Handler();

		search_header_et
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							onClick(search_header_iv);
						}
						return false;
					}
				});

		RelativeLayout mainLayout = (RelativeLayout) view
				.findViewById(R.id.mainEventLayout);
		mainLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (v.getId() == R.id.search_header_et) {
					((MainActivity) activity).openKeyboard(v);
				} else {
					((MainActivity) activity).hideKeyboard(v);
				}
				return false;
			}
		});
		eventsListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				((MainActivity) activity).hideKeyboard(v);
				return false;
			}
		});
	}

	/*-------------------- Google Ads displaying--------------*/
	private void configuringAdMob() {
		// // Prepare the Interstitial Ad
		// interstitial = new InterstitialAd(activity);
		// // Insert the Ad Unit ID
		// interstitial.setAdUnitId(Constants.AdUnitID_Banner);

		adView = (AdView) activity.findViewById(R.id.adView);

		// Request for Ads
		AdRequest adRequest = new AdRequest.Builder().build();

		// Load ads into Banner Ads
		adView.loadAd(adRequest);

		// // Load ads into Interstitial Ads
		// interstitial.loadAd(adRequest);
		//
		// // Prepare an Interstitial Ad Listener
		// interstitial.setAdListener(new AdListener() {
		// public void onAdLoaded() {
		// // Call displayInterstitial() function
		// displayInterstitial();
		// }
		// });
	}

	// public void displayInterstitial() {
	// // If Ads are loaded, show Interstitial else show nothing.
	// if (interstitial.isLoaded()) {
	// interstitial.show();
	// }
	// }
	/*-------------------- Google Ads displaying--------------*/
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.event_menu_iv:
			showMenu();
			break;

		case R.id.createEvent:
			Fragment addanEventFragment = new AddanEventFragment();
			// displays user page in specified tab.
			((MainActivity) activity).pushFragments(
					((MainActivity) activity).getmCurrentTab(),
					addanEventFragment, false, true);
			
			break;
		case R.id.search_header_back_iv:
			search_header_et.setText("");
			search_header_llyt.setVisibility(View.GONE);
			events_header_llyt.setVisibility(View.VISIBLE);
			((MainActivity) activity).hideKeyboard(v);
			break;

		case R.id.search_iv:
			search_header_llyt.setVisibility(View.VISIBLE);

			events_header_llyt.setVisibility(View.GONE);
			InputMethodManager keyboardMgr = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (search_header_et.requestFocus()) {
				((MainActivity) activity).openKeyboard(v);
			}

			break;

		case R.id.search_header_btn_iv:
			search_header_llyt.setVisibility(View.GONE);
			events_header_llyt.setVisibility(View.VISIBLE);

			// displays user page in specified tab.
			if (search_header_et.getText().toString().trim().length() == 0) {
				// No action performed when there is no search string
			} else {
				// Remove all the fragments above the logged user profile
				// fragment
				while (((MainActivity) activity)
						.getNumberOfItems(Constants.TAB_SEARCH) > 1) {
					((MainActivity) activity)
							.popFragmentsWithOutDisplay(Constants.TAB_SEARCH);
				}
				mListener.passData(
						search_header_et.getText().toString().trim(),
						Constants.ToFragment_search);
				// Clear search text & Search page is opened with entered text.
				search_header_et.setText("");
				((MainActivity) activity).hideKeyboard(v);
			}
			break;

		case R.id.add_an_event_iv:
			// Creates & loads addEvent fragment
			// Fragment activeFragment = new AddanEventFragment();
			// FragmentManager fragmentManager = getFragmentManager();
			// FragmentTransaction ft = fragmentManager.beginTransaction();
			// ft.replace(R.id.home_frame, activeFragment);
			// ft.addToBackStack(null);
			// ft.commit();

			// Create fragment & pass message(UserID) users profile

			Intent i = new Intent(activity, CustomMultiMonth.class);
			i.putExtra(Constants.userProf_userID,
					SharedPreferencesUtils.getUserId());
			i.putExtra(Constants.userProf_userName,
					SharedPreferencesUtils.getUserName());
			i.putExtra(Constants.dataReceive, Constants.data_cal_allEvents);
			startActivity(i);

			/*
			 * Fragment activeFragment = new AddanEventFragment(); //displays
			 * user page in specified tab.
			 * ((MainActivity)activity).pushFragments
			 * (((MainActivity)activity).getmCurrentTab(), activeFragment,
			 * false,true);
			 */

			break;

		case R.id.filter_btn:
			// UIHelperUtil.showToastMessage("Yet to implement..!");
			// showFilterMenu();
			// Create fragment & pass message(UserID) users profile
			Fragment activeFragment1 = new FiltersFragment();
			// displays user page in specified tab.
			((MainActivity) activity).pushFragments(
					((MainActivity) activity).getmCurrentTab(),
					activeFragment1, false, true);
			break;

		case R.id.back_iv:
			/*
			 * InputMethodManager imm = (InputMethodManager)
			 * activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			 * imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
			 */

			((MainActivity) activity).onBackPressed();
			// ((MainActivity)activity).popFragments();

			break;

		// case R.id.logout_btn:
		// logOut(v);
		// break;

		case R.id.setting_iv:
			Log.v("", "Event fragment:");
			((MainActivity) activity).showSettingsMenu();
			// ((MainActivity) activity).popup(v);
			break;

		}
	}


	public void updateUI() {
		Log.v("", "Event fragment updated");
		TextView title = (TextView) activity.findViewById(R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);
		title.setText("EVENTS");
		TextView tvEventType = (TextView) activity
				.findViewById(R.id.title_bar_tv_event_type);
		tvEventType.setVisibility(View.GONE);
		ImageView search = (ImageView) activity.findViewById(R.id.search_iv);
		ImageView back = (ImageView) activity.findViewById(R.id.back_iv);
		ImageView settingBtn = (ImageView) activity
				.findViewById(R.id.setting_iv);
		settingBtn.setImageResource(R.drawable.settings_btn);
		settingBtn.setOnClickListener(this);
		settingBtn.setVisibility(View.VISIBLE);
		filterBtn.setVisibility(View.VISIBLE);
		back.setVisibility(View.GONE);

		search.setVisibility(View.VISIBLE);
		addAnEvent.setVisibility(View.VISIBLE);

		search.setOnClickListener(this);

		eventsListView.removeFooterView(footerLayout);
		eventsListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					if (eventsListView.getLastVisiblePosition() >= eventsListView
							.getCount() - 1) {
						if (reload) {
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

												if (globalEventType
														.equalsIgnoreCase(getString(R.string.calender_events))) {
													servicetoGettingEvents(
															getString(R.string.calender_events),
															Constants.longi,
															Constants.lati,
															yearStr,
															monthStr,
															dayStr,
															String.valueOf(pageCount),
															"0");
												} else {
													servicetoGettingEvents(
															globalEventType,
															Constants.longi,
															Constants.lati,
															SharedPreferencesUtils
																	.getFromDate(),
															SharedPreferencesUtils
																	.getToDate(),
															dayStr,
															String.valueOf(pageCount),
															"0");
												}

											} else {
												servicetoGettingEvents(
														globalEventType,
														Constants.longi,
														Constants.lati,
														"",
														"",
														"",
														String.valueOf(pageCount),
														sportId);
											}
										} else {
											/*
											 * UIHelperUtil .showToastMessage(
											 * "The list is up to date.");
											 */
											UIHelperUtil
													.showToastMessage("This is the Last Post");
											footerLayout
													.setVisibility(View.GONE);
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
							/*
							 * UIHelperUtil
							 * .showToastMessage("The list is up to date.");
							 */
							UIHelperUtil
									.showToastMessage("This is the Last Post");
							footerLayout.setVisibility(View.GONE);
							eventsListView.removeFooterView(footerLayout);
						}
					} else {
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

		// Updating filter icon
		if (SharedPreferencesUtils.isFilterApplyed()) {
			filterBtn.setImageResource(R.drawable.filter_apply);
		} else {
			filterBtn.setImageResource(R.drawable.filter_icon);
		}

		if (Utils.chkStatus()) {
			if (!Constants.longi.equals("0") && !Constants.lati.equals("0")) {
				if ((eventsAdapter == null)
						|| ((MainActivity) activity).isNewlyCreatedEvent()
						|| SharedPreferencesUtils.isFilterChanged()) {
					if (SharedPreferencesUtils.isFilterChanged()) {
						pageCount = 0;
						sportId = "0";
						eventsAdapter = null;
						if (!SharedPreferencesUtils.getForAllDate()) {
							// Date range is set in filters
							titleTv.setText(SharedPreferencesUtils
									.getFromDate()
									+ " - "
									+ SharedPreferencesUtils.getToDate());
							globalEventType = getString(R.string.calender_range_events);
							isCalenderEvents = true;
							servicetoGettingEvents(
									getString(R.string.calender_range_events),
									Constants.longi, Constants.lati,
									SharedPreferencesUtils.getFromDate(),
									SharedPreferencesUtils.getToDate(), dayStr,
									"0", "0");
						} else {
							if (globalEventType
									.equalsIgnoreCase(getString(R.string.calender_events))) {
								titleTv.setText("TODAY'S EVENTS");
								// titleTv.setText(monthNameStr +". "+ dayStr
								// +","+yearStr);
								globalEventType = getString(R.string.calender_events);
								isCalenderEvents = true;
								// eventsAdapter = null;
								pageCount = 0;
								servicetoGettingEvents(
										getString(R.string.calender_events),
										Constants.longi, Constants.lati,
										yearStr, monthStr, dayStr, "0", "0");
							} else {
								titleTv.setText("TODAY'S EVENTS");
								// titleTv.setText(monthNameStr +". "+ dayStr
								// +","+yearStr);
								globalEventType = getString(R.string.calender_events);
								isCalenderEvents = true;
								// eventsAdapter = null;
								pageCount = 0;
								servicetoGettingEvents(
										getString(R.string.calender_events),
										Constants.longi, Constants.lati,
										yearStr, monthStr, dayStr, "0", "0");
							}
						}
						((MainActivity) activity).resetNewlyCreatedEvent();
//						SharedPreferencesUtils.resetFilterChangedFlag();
					} else {
						titleTv.setText("TODAY'S EVENTS");
						// titleTv.setText(monthNameStr +". "+ dayStr
						// +","+yearStr);
						globalEventType = getString(R.string.calender_events);
						servicetoGettingEvents(
								getString(R.string.calender_events),
								Constants.longi, Constants.lati, yearStr,
								monthStr, dayStr, "0", "0");
						isCalenderEvents = true;
						eventsAdapter = null;
						pageCount = 0;
						/*
						 * servicetoGettingEvents(getString(R.string.upcoming_events
						 * ), Constants.longi, Constants.lati, "", "","",
						 * "0","0");
						 */

						((MainActivity) activity).resetNewlyCreatedEvent();
					}
				} else {
					if (globalEventType
							.equalsIgnoreCase(getString(R.string.upcoming_events))) {
						titleTv.setText("UPCOMING");
					} else if (globalEventType
							.equalsIgnoreCase(getString(R.string.inprogress_events))) {
						titleTv.setText("IN-PROGRESS");
					} else if (globalEventType
							.equalsIgnoreCase(getString(R.string.finished_events))) {
						titleTv.setText("FINISHED");
					} else if (globalEventType
							.equalsIgnoreCase(getString(R.string.calender_events))) {
						// titleTv.setText(monthStr + "/"+dayStr+"/"+yearStr);
						Log.d("", "events count:" + eventsListView.getCount());
						titleTv.setText("TODAY'S EVENTS");
						// titleTv.setText(monthNameStr +". "+ dayStr
						// +","+yearStr);
						globalEventType = getString(R.string.calender_events);
						isCalenderEvents = true;
						eventsAdapter = null;
						pageCount = 0;
						servicetoGettingEvents(
								getString(R.string.calender_events),
								Constants.longi, Constants.lati, yearStr,
								monthStr, dayStr, "0", "0");
					}

					// eventsListView.setAdapter(eventsAdapter);
					eventsListView.setSelection(pos);
					eventsListView.setOnItemClickListener(this);

					/*
					 * 
					 * titleTv.setText("UPCOMING"); eventsAdapter = null;
					 * servicetoGettingEvents
					 * (getString(R.string.upcoming_events), Constants.longi,
					 * Constants.lati, "", "","", "0","0"); }else
					 * if(globalEventType
					 * .equalsIgnoreCase(getString(R.string.inprogress_events
					 * ))){ titleTv.setText("INPROGRESS"); eventsAdapter = null;
					 * servicetoGettingEvents
					 * (getString(R.string.inprogress_events), Constants.longi,
					 * Constants.lati, "", "","", "0","0"); }else
					 * if(globalEventType
					 * .equalsIgnoreCase(getString(R.string.finished_events))){
					 * titleTv.setText("FINISHED"); eventsAdapter = null;
					 * servicetoGettingEvents
					 * (getString(R.string.finished_events), Constants.longi,
					 * Constants.lati, "", "","", "0","0"); }else
					 * if(globalEventType
					 * .equalsIgnoreCase(getString(R.string.calender_events))){
					 * titleTv.setText(monthStr + "/"+dayStr+"/"+yearStr);
					 * globalEventType = getString(R.string.calender_events);
					 * isCalenderEvents = true; eventsAdapter = null;
					 * servicetoGettingEvents
					 * (getString(R.string.calender_events), Constants.longi,
					 * Constants.lati, yearStr, monthStr,dayStr, "0","0"); }
					 */
				}
			} else {
				globalEventType = getString(R.string.calender_events);
				// UIHelperUtil.showGPSDialogSettingsFromAppContext(activity);
				 UIHelperUtil.showGPSDialogSettingsFromAppContext(activity);
			}

		} else {
			Utils.networkAlertDialog(activity,
					getResources().getString(R.string.toast_no_network));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// Close drawers on starting fragment
		if (mDrawerLayout.isDrawerOpen(mDrawer)) {
			mDrawerLayout.closeDrawer(mDrawer);
		}

		/* AdMob configuration */
		configuringAdMob();
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

	// show menu
	public void showMenu() {
		// Opening menu drawer
		if (mDrawerLayout.isDrawerOpen(mDrawer)) {
			mDrawerLayout.closeDrawer(mDrawer);
		} else {
			mDrawerLayout.openDrawer(mDrawer);
		}

		/*
		 * //Closing filter drawer if it is opened if
		 * (mDrawerLayout.isDrawerOpen(mFilterDrawer)) {
		 * mDrawerLayout.closeDrawer(mFilterDrawer); }
		 */
	}

	// show filter menu
	public void showFilterMenu() {
		// Opens filter
		/*
		 * if (mDrawerLayout.isDrawerOpen(mFilterDrawer)) {
		 * mDrawerLayout.closeDrawer(mFilterDrawer); } else {
		 * mDrawerLayout.openDrawer(mFilterDrawer); }
		 */

		/*
		 * //Closing menu drawer if it is opened if
		 * (mDrawerLayout.isDrawerOpen(mDrawer)) {
		 * mDrawerLayout.closeDrawer(mDrawer); }
		 */
	}

	// show method for each menu item
	private void showMenuitemDetails(int position) {
		switch (position) {
		/*
		 * case 0: pageCount = 0; sportId="0"; eventsAdapter = null; if
		 * (Utils.chkStatus()) { if (Constants.longi != "0" && Constants.lati !=
		 * "0") { titleTv.setText("UPCOMING"); globalEventType =
		 * getString(R.string.upcoming_events); isCalenderEvents = false;
		 * servicetoGettingEvents(getString(R.string.upcoming_events),
		 * Constants.longi, Constants.lati, "", "","", "0","0"); } else {
		 * UIHelperUtil.showGPSDialogSettingsFromAppContext(activity); }
		 * 
		 * } else { Utils.networkAlertDialog(activity, getResources()
		 * .getString(R.string.toast_no_network)); }
		 * mDrawerLayout.openDrawer(mDrawer); break;
		 * 
		 * case 1: pageCount = 0; sportId="0"; eventsAdapter = null; if
		 * (Utils.chkStatus()) { if (Constants.longi != "0" && Constants.lati !=
		 * "0") { titleTv.setText("IN-PROGRESS"); globalEventType =
		 * getString(R.string.inprogress_events); isCalenderEvents = false;
		 * servicetoGettingEvents( getString(R.string.inprogress_events),
		 * Constants.longi, Constants.lati, "", "", "","0","0"); } else {
		 * UIHelperUtil.showGPSDialogSettingsFromAppContext(activity); }
		 * 
		 * } else { Utils.networkAlertDialog(activity, getResources()
		 * .getString(R.string.toast_no_network)); }
		 * mDrawerLayout.openDrawer(mDrawer); break;
		 */
		case 0:

			pageCount = 0;
			sportId = "0";
			eventsAdapter = null;
			SharedPreferencesUtils.setForAllDate();
			if (Utils.chkStatus()) {
				if (!Constants.longi.equals("0") && !Constants.lati.equals("0")) {
					titleTv.setText("TODAY'S EVENTS");
					// titleTv.setText(monthNameStr +". "+ dayStr +","+yearStr);
					globalEventType = getString(R.string.calender_events);
					isCalenderEvents = true;
					pageCount = 0;
					servicetoGettingEvents(getString(R.string.calender_events),
							Constants.longi, Constants.lati, yearStr, monthStr,
							dayStr, "0", "0");
				} else {
					UIHelperUtil.showGPSDialogSettingsFromAppContext(activity);
				}

			} else {
				Utils.networkAlertDialog(activity,
						getResources().getString(R.string.toast_no_network));
			}
			mDrawerLayout.openDrawer(mDrawer);

			break;
		/*
		 * case 1: pageCount = 0; sportId="0"; eventsAdapter = null; if
		 * (Utils.chkStatus()) { if (Constants.longi != "0" && Constants.lati !=
		 * "0") { titleTv.setText("FINISHED"); globalEventType =
		 * getString(R.string.finished_events); isCalenderEvents = false;
		 * servicetoGettingEvents(getString(R.string.finished_events),
		 * Constants.longi, Constants.lati, "", "", "","0","0"); } else {
		 * UIHelperUtil.showGPSDialogSettingsFromAppContext(activity); }
		 * 
		 * } else { Utils.networkAlertDialog(activity, getResources()
		 * .getString(R.string.toast_no_network)); }
		 * mDrawerLayout.openDrawer(mDrawer); break;
		 */

		case 1:
			/*
			 * pageCount = 0; sportId="0"; eventsAdapter = null;
			 * servicetoGetCalendarSummaryInfo();
			 * mDrawerLayout.openDrawer(mDrawer);
			 */
			// Displayed calendar when 'all dates' filter is apply, otherwise
			// events with in set range of dates are shown
			/*if (SharedPreferencesUtils.getForAllDate()) {
				Intent i = new Intent(activity, CustomMultiMonth.class);
				i.putExtra(Constants.userProf_userID,
						SharedPreferencesUtils.getUserId());
				i.putExtra(Constants.userProf_userName,
						SharedPreferencesUtils.getUserName());
				i.putExtra(Constants.dataReceive, Constants.data_cal_allEvents);
				startActivity(i);
			} else {
				pageCount = 0;
				sportId = "0";
				eventsAdapter = null;

				titleTv.setText(SharedPreferencesUtils.getFromDate() + " - "
						+ SharedPreferencesUtils.getToDate());
				globalEventType = getString(R.string.calender_range_events);
				isCalenderEvents = true;
				servicetoGettingEvents(
						getString(R.string.calender_range_events),
						Constants.longi, Constants.lati,
						SharedPreferencesUtils.getFromDate(),
						SharedPreferencesUtils.getToDate(), dayStr, "0", "0");
			}*/
			
			Intent i = new Intent(activity, CustomMultiMonth.class);
			i.putExtra(Constants.userProf_userID,
					SharedPreferencesUtils.getUserId());
			i.putExtra(Constants.userProf_userName,
					SharedPreferencesUtils.getUserName());
			i.putExtra(Constants.dataReceive, Constants.data_cal_allEvents);
			startActivity(i);

			break;
		case 2:
			mDrawerLayout.closeDrawer(mDrawer);
			// Create fragment & pass message(UserID) users profile
			Fragment activeFragment = new AddanEventFragment();
			// displays user page in specified tab.
			((MainActivity) activity).pushFragments(
					((MainActivity) activity).getmCurrentTab(), activeFragment,
					false, true);
			break;
		default:
			break;
		}
	}

	private void showFilterMenuitemDetails(int position) {
		/*
		 * switch (position) { case 0: pageCount = 0; eventsAdapter = null;
		 * serviceToGetSportsList(); mDrawerLayout.closeDrawer(mFilterDrawer);
		 * break; case 1: pageCount = 0;
		 * 
		 * mDrawerLayout.closeDrawer(mFilterDrawer); Intent i = new
		 * Intent(activity,CustomMultiMonth.class);
		 * i.putExtra(Constants.userProf_userID,
		 * SharedPreferencesUtils.getUserId());
		 * i.putExtra(Constants.dataReceive, Constants.data_cal_allEvents);
		 * startActivity(i); break; }
		 */
	}

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

		ImageView addEvent = (ImageView) mDialog
				.findViewById(R.id.add_an_event_frm_cal_iv);
		addEvent.setVisibility(View.VISIBLE);
		addEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment activeFragment = new AddanEventFragment();
				// displays user page in specified tab.
				((MainActivity) activity).pushFragments(
						((MainActivity) activity).getmCurrentTab(),
						activeFragment, false, true);
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
		adapter = new GridCellAdapter(activity, R.id.calendar_day_gridcell,
				month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
		mDialog.show();

		// Change month on left & right swipes
		calendarView.setOnTouchListener(new OnTouchListener() {
			float x1, x2;
			float y1, y2;
			boolean actionDown = false;

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent touchevent) {
				switch (touchevent.getAction()) {
				// when user first touches the screen we get x and y coordinate
				case MotionEvent.ACTION_MOVE: {
					if (!actionDown) {
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
				case MotionEvent.ACTION_UP: {
					actionDown = false;
					x2 = touchevent.getX();
					y2 = touchevent.getY();

					// if left to right sweep event on screen
					if (x1 < x2) {
						// Left to Right Swap Performed: Show previous month
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
					if (x1 > x2) {
						// Right to Left Swap Performed: Display next month
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
					if (y1 < y2) {
						// Toast.makeText(this, "UP to Down Swap Performed",
						// Toast.LENGTH_LONG).show();
					}

					// if Down to UP sweep event on screen
					if (y1 > y2) {
						// Toast.makeText(this, "Down to UP Swap Performed",
						// Toast.LENGTH_LONG).show();
					}
					break;
				}
				}
				return false;
			}
		});
	}

	public void setAdapter(List<EventInfo> eventsList, String pageId,
			String eventType, boolean isfinishedevent) {
		List<EventInfo> tempList = new ArrayList<EventInfo>();
		if (eventsAdapter != null) {
			tempList.addAll(eventsAdapter.getOldEventsList());
			tempList.addAll(eventsList);
		}
		Log.v("", "Temp: " + tempList.size());
		if (eventsAdapter == null && pageId.equalsIgnoreCase("0")) {
			eventsAdapter = new EventsAdapter(activity, eventsList, eventType,
					isfinishedevent);
			eventsListView.setAdapter(eventsAdapter);
			Utils.getListViewSize(eventsListView);
		} else {
			eventsAdapter.updateEvents(tempList);
		}
		eventsListView.setOnItemClickListener(this);
		if (eventsListView.getCount() > 0) {
			eventsListView.setVisibility(View.VISIBLE);
			noEventsStatus.setVisibility(View.GONE);
			createEventTv.setVisibility(View.GONE);
		} else {
			eventsListView.setVisibility(View.GONE);
			noEventsStatus.setVisibility(View.VISIBLE);
			createEventTv.setVisibility(View.VISIBLE);
		}
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
			eventsPerMonthMap = findNumberOfEventsPerMonth(
					String.valueOf(year), String.valueOf(month));
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
				if ((i == getCurrentDayOfMonth())
						&& (currentMonth == getCurrentMonth())) {
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
		private HashMap<String, Integer> findNumberOfEventsPerMonth(
				String year, String month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			// Find no. of events per month
			if (calendarSummaryEvnetInfo != null) {
				HashMap<String, List<String>> yrEvents = calendarSummaryEvnetInfo
						.get(year);
				if (yrEvents != null) {
					List<String> mnthEvents = yrEvents.get(month);
					if (mnthEvents != null) {
						Set<String> uniqueSet = new HashSet<String>(mnthEvents);
						for (String temp : uniqueSet) {
							int freq = Collections.frequency(mnthEvents, temp);
							System.out.println(temp + ": " + freq);
							map.put(temp, freq);
						}
					} else {
						return null;
					}
				} else {
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

			if (!day_color[1].equals("GREY")) {
				// Displaying event indications only for current active month
				// but not for leading & next months
				if ((eventsPerMonthMap != null)
						&& (!eventsPerMonthMap.isEmpty())) {
					if (eventsPerMonthMap.containsKey(theday)) {
						/*
						 * //Display no. of events count num_events_per_day =
						 * (TextView) row
						 * .findViewById(R.id.num_events_per_day); Integer
						 * numEvents = (Integer) eventsPerMonthMap.get(theday);
						 * num_events_per_day.setText(numEvents.toString());
						 */

						// Display indication on the calendar when there is an
						// event
						AreEventsExistPerDay = (ImageView) row
								.findViewById(R.id.has_events_perday);
						AreEventsExistPerDay
								.setBackgroundResource(R.drawable.event_indication);
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
			// TODO: Display events on selected date

			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}

			String date_month_year = (String) view.getTag();
			String[] monthYear = date_month_year.split("-");
			dayStr = (new DecimalFormat("00")).format(Integer
					.valueOf(monthYear[0]));
			monthStr = (new DecimalFormat("00")).format(Integer
					.valueOf(monthYear[1]));
			yearStr = monthYear[2];
			// selectedDayMonthYearButton.setText("Selected: " +
			// date_month_year);
			Log.d("Selected date", "Koti Selected date" + monthStr + " "
					+ yearStr);
			if (Utils.chkStatus()) {
				if (!Constants.longi.equals("0") && !Constants.lati.equals("0")) {
					// titleTv.setText("CALENDAR");
					titleTv.setText(monthStr + "/" + dayStr + "/" + yearStr);
					globalEventType = getString(R.string.calender_events);
					isCalenderEvents = true;
					servicetoGettingEvents(getString(R.string.calender_events),
							Constants.longi, Constants.lati, yearStr, monthStr,
							dayStr, "0", "0");
				} else {
					UIHelperUtil.showGPSDialogSettingsFromAppContext(activity);
				}

			} else {
				Utils.networkAlertDialog(activity,
						getResources().getString(R.string.toast_no_network));
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

	@Override
	public void onItemClick(AdapterView<?> adView, View view, int position,
			long arg3) {
		if (adView.getId() == R.id.drawer_list) {
			showMenuitemDetails(position);
			mDrawerLayout.closeDrawer(mDrawer);
		} else if (adView.getId() == R.id.events_lv) {
			EventInfo eventInfo = (EventInfo) adView
					.getItemAtPosition(position);
			Log.v("", "EventId: " + eventInfo + " : " + position + " : "
					+ eventInfo.getEventId());

			Fragment activeFragment = new EventDetailsFragment();
			Bundle bundle = new Bundle();
			// TODO change this used id.
			bundle.putString(Constants.KEY_USER_ID,
					SharedPreferencesUtils.getUserId());
			bundle.putString(Constants.KEY_EVENT_ID, eventInfo.getEventId());
			bundle.putString(Constants.KEY_EVENT_TYPE, titleTv.getText()
					.toString().trim());
			activeFragment.setArguments(bundle);

			// Saving the current selected event obj
			((MainActivity) activity).resetNewlyCreatedEvent();
			((MainActivity) activity).setCurrentEvent(eventInfo);

			// Adds the event-detailed fragment on to 'events fragment' stack &
			// displayed on the screen
			((MainActivity) activity).pushFragments(
					((MainActivity) activity).getmCurrentTab(), activeFragment,
					false, true);
		}
		/*
		 * else if(adView.getId() == R.id.filter_drawer_list){
		 * showFilterMenuitemDetails(position);
		 * mDrawerLayout.closeDrawer(mDrawer); }
		 */
	}

	/************************* ------SERVER INTERACTION STARTS----- ***************************/
	/**
	 * method for fetches events dates
	 */
	private void servicetoGetCalendarSummaryInfo() {
		String url = Constants.common_url
				+ getString(R.string.get_calendarSummaryInfo);
		calendarSummaryEvnetInfo = new HashMap<String, HashMap<String, List<String>>>();
		JsonParser.callBackgroundService(url, null, new JsonServiceListener() {
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
						JSONObject resObj = jsonObject
								.getJSONObject(Constants.webserver_response);
						String responseStr = resObj
								.getString(Constants.webserver_responseInfo);
						if (responseStr != null
								&& responseStr
										.equalsIgnoreCase(Constants.webserver_response_success)) {
							JSONArray resEventDatesObj = resObj
									.getJSONArray(Constants.cal_DateList);
							for (int i = 0; i < resEventDatesObj.length(); i++) {
								JSONObject eventDateObj = resEventDatesObj
										.getJSONObject(i);
								String date = eventDateObj
										.getString(Constants.cal_EventDate);
								Calendar calObj = Calendar.getInstance();
								try {
									calObj.setTime(new SimpleDateFormat(
											"yyyy-MM-dd", Locale.ENGLISH)
											.parse(date));
									String yr = Integer.toString(calObj
											.get(Calendar.YEAR));
									String mnth = Integer.toString(calObj
											.get(Calendar.MONTH) + 1);
									String day = Integer.toString(calObj
											.get(Calendar.DAY_OF_MONTH));
									HashMap<String, List<String>> MnthList = calendarSummaryEvnetInfo
											.get(yr);
									if (MnthList == null) {
										MnthList = new HashMap<String, List<String>>();
										calendarSummaryEvnetInfo.put(yr,
												MnthList);
									}
									List<String> DateList = MnthList.get(mnth);
									if (DateList == null) {
										DateList = new ArrayList<String>();
										MnthList.put(mnth, DateList);
									}
									DateList.add(day);

								} catch (Exception ex) {
									Log.e(Constants.logUrl,
											"Error in parsing calendar events date");
								}
							}
							pageCount = 0;
							eventsAdapter = null;
							// displayCalendar();
							// TODO: display calendar screen
						} else if (responseStr != null
								&& responseStr
										.equalsIgnoreCase(Constants.webserver_response_fail)) {

						}
					} catch (JSONException ex) {
						Log.d(Constants.logUrl, ex.getMessage());
					}
				}

			}
		});
	}

	/**
	 * method for fetching events from the server
	 */
	private void servicetoGettingEvents(final String eventsType, String longi,
			String lati, String yearStr, String monthStr, String dayStr,
			final String pageId, String sport_id) {

		final List<EventInfo> eventsList = new ArrayList<EventInfo>();
		/*
		 * get_upcoming_events.php?user_id=3&user_lat=43.59685959999999&user_lng=
		 * 3.8502617000000328&page_id=0
		 */
		String url = Constants.common_url + eventsType;
		// Log.v("", "URL getEvents: " +
		// SharedPreferencesUtils.getUserId()+" "+lati+" "+longi+" "+pageId+""+"");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user_id",
				SharedPreferencesUtils.getUserId()));
		nameValuePairs.add(new BasicNameValuePair("user_lat",
				SharedPreferencesUtils.getLatitude()));
		nameValuePairs.add(new BasicNameValuePair("user_lng",
				SharedPreferencesUtils.getLongitude()));
		if (eventsType
				.equalsIgnoreCase(getString(R.string.calender_range_events))) {
			nameValuePairs.add(new BasicNameValuePair("from_date",
					SharedPreferencesUtils.getFromDate()));
			nameValuePairs.add(new BasicNameValuePair("to_date",
					SharedPreferencesUtils.getToDate()));
			nameValuePairs.add(new BasicNameValuePair("time_zone", TimeZone
					.getDefault().getID()));
		}
		// Before including calendar range events
		if (yearStr.length() > 0 && monthStr.length() > 0) {
			nameValuePairs.add(new BasicNameValuePair("year", yearStr));
			nameValuePairs.add(new BasicNameValuePair("month", monthStr));
			nameValuePairs.add(new BasicNameValuePair("day", dayStr));
			nameValuePairs.add(new BasicNameValuePair("time_zone", TimeZone
					.getDefault().getID()));
		}
		nameValuePairs.add(new BasicNameValuePair("page_id", pageId));
		nameValuePairs.add(new BasicNameValuePair("radious",
				SharedPreferencesUtils.getRange()));
		nameValuePairs.add(new BasicNameValuePair("sport_id",
				SharedPreferencesUtils.getSportIds()));

		if (eventsType.equalsIgnoreCase(getString(R.string.calender_events))) {
			// TODO: set filter only for calendar events
			nameValuePairs.add(new BasicNameValuePair("sport_id",
					SharedPreferencesUtils.getSportIds()));
			nameValuePairs.add(new BasicNameValuePair("radious",
					SharedPreferencesUtils.getRange()));
			/*
			 * nameValuePairs.add(new BasicNameValuePair("sport_id", "0"));
			 * nameValuePairs.add(new BasicNameValuePair("radious", "all"));
			 */
			// nameValuePairs.add(new BasicNameValuePair("radious", "100"));
		}

		Log.v("", "GetEventInfo URL: " + url + "user_id="
				+ SharedPreferencesUtils.getUserId() + "user_lat=" + lati
				+ "&user_lng=" + longi + "&page_id=" + pageId + "&year="
				+ yearStr + "&month=" + monthStr + "&day" + dayStr);
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

									JSONArray upcomingEventsArr = resObj
											.getJSONArray("events_list");
									if (upcomingEventsArr != null) {
										for (int i = 0; i < upcomingEventsArr
												.length(); i++) {
											EventInfo eventinfo = new EventInfo();
											eventinfo
													.setEventId(upcomingEventsArr
															.getJSONObject(i)
															.getString(
																	"event_id"));

											if (!upcomingEventsArr
													.getJSONObject(i)
													.getString("sport_name")
													.equalsIgnoreCase("null")) {
												eventinfo
														.setSportName(upcomingEventsArr
																.getJSONObject(
																		i)
																.getString(
																		"sport_name"));
											} else {
												eventinfo.setSportName("-");
											}

											if (!upcomingEventsArr
													.getJSONObject(i)
													.getString("event_title")
													.equalsIgnoreCase("null")) {
												eventinfo
														.setEventTitle(upcomingEventsArr
																.getJSONObject(
																		i)
																.getString(
																		"event_title"));
											} else {
												eventinfo.setEventTitle("-");
											}
											if (!upcomingEventsArr
													.getJSONObject(i)
													.getString(
															"event_description")
													.equalsIgnoreCase("null")) {
												eventinfo
														.setEventDescription(upcomingEventsArr
																.getJSONObject(
																		i)
																.getString(
																		"event_description"));
											} else {
												eventinfo
														.setEventDescription("-");
											}

											if (!upcomingEventsArr
													.getJSONObject(i)
													.getString(
															"event_start_date")
													.equalsIgnoreCase("null")) {
												eventinfo
														.setEventStartDate(upcomingEventsArr
																.getJSONObject(
																		i)
																.getString(
																		"event_start_date"));
											} else {
												eventinfo
														.setEventStartDate("-");
											}

											if (!upcomingEventsArr
													.getJSONObject(i)
													.getString(
															"event_start_time")
													.equalsIgnoreCase("null")) {
												eventinfo
														.setEventStartTime(upcomingEventsArr
																.getJSONObject(
																		i)
																.getString(
																		"event_start_time"));
											} else {
												eventinfo
														.setEventStartTime("-");
											}

											if (!upcomingEventsArr
													.getJSONObject(i)
													.getString(
															"event_sub_location")
													.equalsIgnoreCase("null")) {
												eventinfo
														.setEventSubLocation(upcomingEventsArr
																.getJSONObject(
																		i)
																.getString(
																		"event_sub_location"));
											} else {
												eventinfo
														.setEventSubLocation("-");
											}

											if (!upcomingEventsArr
													.getJSONObject(i)
													.getString("city")
													.equalsIgnoreCase("null")
													&& upcomingEventsArr
															.getJSONObject(i)
															.getString("city")
															.length() > 0) {
												eventinfo
														.setEventCity(upcomingEventsArr
																.getJSONObject(
																		i)
																.getString(
																		"city"));
											} else {
												eventinfo.setEventCity("-");
											}

											if (!upcomingEventsArr
													.getJSONObject(i)
													.getString("event_image")
													.equalsIgnoreCase("null")) {
												eventinfo
														.setEventImageUrl(upcomingEventsArr
																.getJSONObject(
																		i)
																.getString(
																		"event_image"));
											} else {

											}
											eventinfo
													.setTeam1Id(upcomingEventsArr
															.getJSONObject(i)
															.getString(
																	"team1_id"));
											eventinfo
													.setTeam1Type(upcomingEventsArr
															.getJSONObject(i)
															.getString(
																	"team1_type"));
											eventinfo
													.setTeam2Id(upcomingEventsArr
															.getJSONObject(i)
															.getString(
																	"team2_id"));
											eventinfo
													.setTeam2Type(upcomingEventsArr
															.getJSONObject(i)
															.getString(
																	"team2_type"));
											eventinfo
													.setEventEndTime(upcomingEventsArr
															.getJSONObject(i)
															.getString(
																	"event_end_time"));
											eventinfo
													.setEventAddress(upcomingEventsArr
															.getJSONObject(i)
															.getString(
																	"formatted_address"));
											eventinfo
													.setEventDistanceFromUserLoc(upcomingEventsArr
															.getJSONObject(i)
															.getString(
																	"distance_from_user_location"));
											eventinfo
													.setEventLikes(upcomingEventsArr
															.getJSONObject(i)
															.getString(
																	"event_likes"));
											eventinfo
													.setIsAttending(upcomingEventsArr
															.getJSONObject(i)
															.getString(
																	"is_attending"));
											eventinfo
													.setEventType(upcomingEventsArr
															.getJSONObject(i)
															.getString(
																	"event_type"));
											eventinfo
													.setAttendingCount(upcomingEventsArr
															.getJSONObject(i)
															.getString(
																	"attending_count"));

											if (upcomingEventsArr
													.getJSONObject(i)
													.getString("latest_cast") != null
													&& upcomingEventsArr
															.getJSONObject(i)
															.getString(
																	"latest_cast")
															.equalsIgnoreCase(
																	"no cast")) {
												eventinfo
														.setLatestCast(upcomingEventsArr
																.getJSONObject(
																		i)
																.getString(
																		"latest_cast"));
											} else {
												JSONObject castObj = upcomingEventsArr
														.getJSONObject(i)
														.getJSONObject(
																"latest_cast");
												eventinfo.setUserId(castObj
														.getString("user_id"));
												eventinfo.setUserName(castObj
														.getString("name"));
												eventinfo
														.setProfilePicPath(castObj
																.getString("profile_image"));
												eventinfo.setLatestCast(castObj
														.getString("cast_text"));
											}

											eventsList.add(eventinfo);
										}

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

	/**
	 * service to get sports list from server
	 * */
	/*
	 * private void serviceToGetSportsList() { String url = Constants.common_url
	 * + getResources().getString(R.string.get_sports_list);
	 * 
	 * JsonParser.callBackgroundService(url, null, new JsonServiceListener() {
	 * ProgressDialog pd;
	 * 
	 * @Override public void showProgress() { super.showProgress(); pd = new
	 * ProgressDialog(activity); pd.setMessage("Loading..."); pd.show();
	 * pd.setCancelable(false); pd.setCanceledOnTouchOutside(false); }
	 * 
	 * @Override public void hideProgress() { if(pd != null && pd.isShowing()) {
	 * pd.dismiss(); } }
	 * 
	 * @Override public void parseJsonResponse(String jsonResponse) {
	 * setJsonValues(jsonResponse); } }); }
	 */

	/*
	 * private void setJsonValues(String jsonResult) { try {
	 * if(jsonResult!=null){ Log.d(Constants.logUrl,"response:  " + jsonResult);
	 * JSONObject reader = new JSONObject(jsonResult); JSONObject sys =
	 * reader.getJSONObject("Response"); String response =
	 * sys.getString("ResponseInfo"); final List<SportModel> sportsList = new
	 * ArrayList<SportModel>(); if(response != null &&
	 * response.equals("SUCCESS")) { JSONArray sportsDetailsJson =
	 * sys.getJSONArray("sprots_list"); for(int i=0;
	 * i<sportsDetailsJson.length(); i++) { JSONObject sportObject =
	 * sportsDetailsJson.getJSONObject(i); SportModel sportModel = new
	 * SportModel(); sportModel.setSportId(sportObject.getString("sport_id"));
	 * sportModel.setSportName(sportObject.getString("sport_name"));
	 * sportModel.setMaxPlayersOnField
	 * (sportObject.getString("max_players_on_field"));
	 * sportModel.setSportRounds(sportObject.getString("rounds"));
	 * sportsList.add(sportModel); System.out.println("list: " +
	 * sportsList.get(i)); }
	 * 
	 * if(sportsList != null){ SportsAdapter sportsAdapter = new
	 * SportsAdapter(activity, sportsList); AlertDialog.Builder bldr = new
	 * AlertDialog.Builder(activity); bldr.setTitle("Select sport");
	 * bldr.setOnCancelListener(new OnCancelListener() {
	 * 
	 * @Override public void onCancel(DialogInterface dialog) {
	 * dialog.dismiss(); } }); bldr.setAdapter(sportsAdapter,new
	 * DialogInterface.OnClickListener(){
	 * 
	 * @Override public void onClick(DialogInterface dialog, int pos) { sportId
	 * = sportsList.get(pos).getSportId(); if (Constants.longi != "0" &&
	 * Constants.lati != "0") { //Applying filter on current displayed events
	 * if(
	 * globalEventType.equalsIgnoreCase(getString(R.string.upcoming_events))){
	 * titleTv.setText("UPCOMING"); isCalenderEvents = false;
	 * servicetoGettingEvents(getString(R.string.upcoming_events),
	 * Constants.longi, Constants.lati, "", "", "","0",sportId); }else
	 * if(globalEventType
	 * .equalsIgnoreCase(getString(R.string.inprogress_events))){
	 * titleTv.setText("IN-PROGRESS"); isCalenderEvents = false;
	 * servicetoGettingEvents(getString(R.string.inprogress_events),
	 * Constants.longi, Constants.lati, "", "", "","0",sportId); }else
	 * if(globalEventType
	 * .equalsIgnoreCase(getString(R.string.finished_events))){
	 * titleTv.setText("FINISHED"); isCalenderEvents = false;
	 * servicetoGettingEvents(getString(R.string.finished_events),
	 * Constants.longi, Constants.lati, "", "","", "0",sportId); }else
	 * if(globalEventType
	 * .equalsIgnoreCase(getString(R.string.calender_events))){ //
	 * titleTv.setText("CALENDAR"); titleTv.setText(monthStr +
	 * "/"+dayStr+"/"+yearStr); isCalenderEvents = true;
	 * servicetoGettingEvents(globalEventType,Constants.longi,Constants.lati,
	 * yearStr,monthStr,dayStr, String.valueOf(pageCount),sportId); } else {
	 * UIHelperUtil.showGPSDialogSettingsFromAppContext(activity); } }
	 * 
	 * }}); bldr.show(); } } else if(response!=null &&
	 * response.equalsIgnoreCase("FAILURE")){
	 * UIHelperUtil.showToastMessage("No sports list found"); } } else{
	 * UIHelperUtil.showToastMessage("No sports list found"); } } catch
	 * (JSONException e) { e.printStackTrace(); }
	 * 
	 * 
	 * }
	 */
	/************************* ------SERVER INTERACTION ENDS----- ***************************/
}
