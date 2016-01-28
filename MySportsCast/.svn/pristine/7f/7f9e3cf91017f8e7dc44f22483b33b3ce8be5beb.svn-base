package com.mysportsshare.mysportscast.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import net.londatiga.android.twitter.Twitter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.mysportsshare.mysportscast.GCMIntentService.OnNotifyEventListener;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.fragments.ChangePasswordFragment;
import com.mysportsshare.mysportscast.fragments.CheckedEventsFragment;
import com.mysportsshare.mysportscast.fragments.EditProfileFragmentPage;
import com.mysportsshare.mysportscast.fragments.EventDetailsFragment;
import com.mysportsshare.mysportscast.fragments.EventsFragment;
import com.mysportsshare.mysportscast.fragments.FollowerUsersListFragment;
import com.mysportsshare.mysportscast.fragments.FollowingUsersListFragment;
import com.mysportsshare.mysportscast.fragments.LinkedAccountsFragment;
import com.mysportsshare.mysportscast.fragments.MediaFragment;
import com.mysportsshare.mysportscast.fragments.MyEventsFragment;
import com.mysportsshare.mysportscast.fragments.NotificationFragment;
import com.mysportsshare.mysportscast.fragments.SearchPageFragment;
import com.mysportsshare.mysportscast.fragments.UserProfileFragment;
import com.mysportsshare.mysportscast.models.EventInfo;
import com.mysportsshare.mysportscast.models.NotificationInfo;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.CustomLog;
import com.mysportsshare.mysportscast.utils.DataPassListener;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.SocialNetworkingUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;
//import com.google.android.gms.location.places.Places;
//import com.mysportsshare.mysportscast.adapters.PlaceAutocompleteAdapter;

/**
 * This activity is the parent activity for all fragments in this application.
 * 
 */
public class MainActivity extends ActionBarActivity implements
		GoogleApiClient.OnConnectionFailedListener, DataPassListener,
		OnNotifyEventListener, ConnectionCallbacks {
	// bhavani
	// Views
	private Fragment mFrag;
	private FrameLayout fmLyt;
	private RelativeLayout foothomeRlyt, footsearchRlyt, footcameraRlyt,
			footnotifiRlyt, footprofilePicRlyt;

	// Title layouts
	private TextView title;
	private ImageView back;
	private ImageView settingBtn;
	private ImageView addEventBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private TextView tvAppNotifyCount;
	private RelativeLayout events_header_llyt;
	private RelativeLayout search_header_llyt;

	/* A HashMap of stacks, where we use tab identifier as keys.. */
	public HashMap<String, Stack<Fragment>> mStacks;

	/* Save current tabs identifier in this.. */
	private String mCurrentTab;

	private static MainActivity mInstance;

	/* Your Tab host */
	private String mTabHost;

	private boolean isNewEventAdded = false; // Indicates whether new event is
	// added or not in add
	// event-fragment.
	private EventInfo currentEventObj; // Holds current selected or newly added

	// event object in event-fragment

	// location maintainance
	private String latitude;
	private String longitude;
	private String deviceId;
	private GoogleApiClient googleApiClient;
	private LocationRequest locationRequest;
	private boolean isForFirstTime = true;
	private ProgressDialog progressDlg;
	final static int REQUEST_LOCATION = 199;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// if (GoogleAutoCompleteLocation.mGoogleApiClient == null) {
		// rebuildGoogleApiClient();
		// }

		// for location by koti
		// buildGoogleApiClient();
		progressDlg = new ProgressDialog(this);
		// progressDlg.setMessage("Getting credentials...");
		progressDlg.setMessage("Please wait\nwe are fetching the location");
		progressDlg.setCancelable(false);

		progressDlg.show();

		enableLoc();
		timerDelayRemoveDialog(120000, progressDlg);

		// old main code
		// mainCode();

	}

	public void timerDelayRemoveDialog(long time, final ProgressDialog d) {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (!isForFirstTime) {
					return;
				}
				try {
					if (!SharedPreferencesUtils.getLatitude().equals("0")
							&& !SharedPreferencesUtils.getLongitude().equals(
									"0")) {
						d.dismiss();
						isForFirstTime = false;
						Constants.lati = SharedPreferencesUtils.getLatitude();
						Constants.longi = SharedPreferencesUtils.getLongitude();
						Constants.locationName = SharedPreferencesUtils
								.getLocaton();
						mainCode();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, time);
	}

	// setting window
	public void popup(View v) {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.setting_popup,
				(ViewGroup) findViewById(R.id.popup_element_setting));

		final PopupWindow pwindo = new PopupWindow(MainActivity.this);
		pwindo.setWidth(LayoutParams.WRAP_CONTENT);
		pwindo.setHeight(LayoutParams.WRAP_CONTENT);
		pwindo.setContentView(layout);
		pwindo.setFocusable(true);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int mWidth = displaymetrics.widthPixels;
		pwindo.showAsDropDown(v, -5, 0);
		pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
		final ListView lv_popup = (ListView) layout
				.findViewById(R.id.settinglv);
		ArrayList<String> settingList = new ArrayList<String>();

		settingList.add("Profile Settings");
		settingList.add("Log Out");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				MainActivity.this, R.layout.setting_list_item,
				R.id.setting_list_element, settingList);
		lv_popup.setAdapter(adapter);
		Log.d("", "Popup window");
		lv_popup.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// write click actions for facebook twitter and google+ sharing

				String selectedItem = lv_popup.getItemAtPosition(position)
						.toString().trim();

				if (selectedItem.equalsIgnoreCase("Profile Settings")) {
					clickFooterProfilePic(v);
					pwindo.dismiss();
				} else if (selectedItem.equalsIgnoreCase("Log Out")) {
					logOut(v);
					pwindo.dismiss();

				}

			}
		});

	}

	public void showSettingsMenu() {
		// Setting options
		CharSequence settingOptions[] = {};

		List<String> listItems = new ArrayList<String>();
		if (SharedPreferencesUtils.isUserLoginViaSocialMedia()
				.equalsIgnoreCase("true")) {
			listItems.add(Constants.TAG_MENU_PROFILE_SETTING);
			listItems.add(Constants.TAG_MENU_LINKED_ACCOUNT);
			listItems.add(Constants.TAG_MENU_LOG_OUT);
		} else {
			listItems.add(Constants.TAG_MENU_PROFILE_SETTING);
			listItems.add(Constants.TAG_MENU_CHANGE_PASSWORD);
			listItems.add(Constants.TAG_MENU_LINKED_ACCOUNT);
			listItems.add(Constants.TAG_MENU_LOG_OUT);
		}
		settingOptions = listItems.toArray(new CharSequence[listItems.size()]);
		AlertDialog.Builder bldr = new AlertDialog.Builder(MainActivity.this);
		bldr.setTitle("Settings");
		bldr.setItems(settingOptions, new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int pos) {
				dialog.dismiss();
				if (SharedPreferencesUtils.isUserLoginViaSocialMedia()
						.equalsIgnoreCase("true")) {
					switch (pos) {
					case 0:
						// Remove all the fragments above the logged user
						// profile fragment
						while (mStacks.get(Constants.TAB_USERS).size() > 1) {
							mStacks.get(Constants.TAB_USERS).pop();
						}
						clickFooterProfilePic(null);
						break;
					case 1:
						Fragment linkedAccFragment = new LinkedAccountsFragment();
						pushFragments(getmCurrentTab(), linkedAccFragment,
								false, true);
						break;
					case 2:
						logOut(null);
						break;
					}
				} else {
					switch (pos) {
					case 0:
						// Remove all the fragments above the logged user
						// profile fragment
						while (mStacks.get(Constants.TAB_USERS).size() > 1) {
							mStacks.get(Constants.TAB_USERS).pop();
						}
						clickFooterProfilePic(null);
						break;
					case 1:
						// Create fragment & pass message(UserID) users profile
						Fragment activeFragment = new ChangePasswordFragment();
						// displays user page in specified tab.
						pushFragments(getmCurrentTab(), activeFragment, false,
								true);
						break;
					case 2:
						Fragment linkedAccFragment = new LinkedAccountsFragment();
						pushFragments(getmCurrentTab(), linkedAccFragment,
								false, true);
						break;
					case 3:
						logOut(null);
						break;

					}
				}
			}
		});
		bldr.setCancelable(true);
		bldr.show();
	}

	/*
	 * public void showSettingsMenu(){ //Setting options CharSequence
	 * settingOptions[]={"Profile settings","Log out"};
	 * 
	 * List<String> listItems = new ArrayList<String>();
	 * if(SharedPreferencesUtils
	 * .isUserLoginViaSocialMedia().equalsIgnoreCase("true")){
	 * listItems.add(Constants.TAG_MENU_PROFILE_SETTING);
	 * listItems.add(Constants.TAG_MENU_LOG_OUT); }else{
	 * listItems.add(Constants.TAG_MENU_PROFILE_SETTING);
	 * listItems.add(Constants.TAG_MENU_CHANGE_PASSWORD);
	 * listItems.add(Constants.TAG_MENU_LOG_OUT); } settingOptions =
	 * listItems.toArray(new CharSequence[listItems.size()]);
	 * AlertDialog.Builder bldr = new AlertDialog.Builder(MainActivity.this);
	 * bldr.setTitle("Settings"); bldr.setItems(settingOptions, new
	 * Dialog.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int pos) {
	 * dialog.dismiss();
	 * if(SharedPreferencesUtils.isUserLoginViaSocialMedia().equalsIgnoreCase
	 * ("true")){ switch(pos){ case 0: clickFooterProfilePic(null); break; case
	 * 1: logOut(null); break; } }else{ switch(pos){ case 0:
	 * clickFooterProfilePic(null); break; case 1: //Create fragment & pass
	 * message(UserID) users profile Fragment activeFragment = new
	 * ChangePasswordFragment(); //displays user page in specified tab.
	 * pushFragments(getmCurrentTab(), activeFragment, false,true); break; case
	 * 2:
	 * 
	 * logOut(null); break; } } } }); bldr.setCancelable(true); bldr.show(); }
	 */
	/**
	 * method for logout
	 */
	private void logOut(View v) {
		if (v != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		}
		if (SharedPreferencesUtils.getUserId() != null
				&& SharedPreferencesUtils.getUserId().length() > 0) {
			SharedPreferencesUtils.setUserName("");
			SharedPreferencesUtils.setUserId("");
			SharedPreferencesUtils.setUserProfilePicPath("");
			SharedPreferencesUtils.setOauthUserId("");
			finish();
			Intent i = new Intent(MainActivity.this, StartActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			UIHelperUtil.showToastMessage("You are now logged out");
			Utils.unRegisterForGCM(getApplicationContext());
		}
		// by koti
		SharedPreferencesUtils.resetFilterApplyedFlag();
		SharedPreferencesUtils.setRange("200");
		SharedPreferencesUtils.setForAllDate();
		SharedPreferencesUtils.setForAllSports();
		SharedPreferencesUtils.setSportIds("0");
	}

	/**
	 * initialization
	 */
	private void init() {
		foothomeRlyt = (RelativeLayout) findViewById(R.id.foothome_rlyt);
		footsearchRlyt = (RelativeLayout) findViewById(R.id.footsearch_rlyt);
		footcameraRlyt = (RelativeLayout) findViewById(R.id.footcamera_rlyt);
		footnotifiRlyt = (RelativeLayout) findViewById(R.id.footnotification_rlyt);
		footprofilePicRlyt = (RelativeLayout) findViewById(R.id.footprofilePic_rlyt);
		title = (TextView) findViewById(R.id.title_bar_tv);
		back = (ImageView) findViewById(R.id.back_iv);
		settingBtn = (ImageView) findViewById(R.id.setting_iv);
		searchHeaderBtn = (ImageView) findViewById(R.id.search_iv);
		addEventBtn = (ImageView) findViewById(R.id.add_an_event_iv);
		tvEventType = (TextView) findViewById(R.id.title_bar_tv_event_type);
		tvAppNotifyCount = (TextView) findViewById(R.id.app_notify_count);

		events_header_llyt = (RelativeLayout) findViewById(R.id.events_header_llyt);
		search_header_llyt = (RelativeLayout) findViewById(R.id.search_header_llyt);
	}

	// Returns current selected or created event object
	public EventInfo getCurrentEvent() {
		return currentEventObj;
	}

	// Holds current selected or created event object
	public void setCurrentEvent(EventInfo event) {
		currentEventObj = event;
	}

	// Indicates new event created & reference is hold in 'currentEventObj'
	public void setNewlyCreatedEvent() {
		isNewEventAdded = true;
	}

	// Indicates no new event created, but an event is selected from list & its
	// reference is hold in 'currentEventObj'
	public void resetNewlyCreatedEvent() {
		isNewEventAdded = false;
	}

	// Indicates whether new event is created or not
	public boolean isNewlyCreatedEvent() {
		return isNewEventAdded;
	}

	/**
	 * Hides virtual keyboard
	 */
	public void hideKeyboard(View view) {
		InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * Hides virtual keyboard
	 */
	public void openKeyboard(View view) {
		InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		in.toggleSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.SHOW_IMPLICIT, 0);
	}

	/**
	 * click event for footer calendar
	 */
	public void clickFooterHome(View view) {
		// Display header layout
		// events_header_llyt.setVisibility(View.VISIBLE);
		// search_header_llyt.setVisibility(View.GONE);
		// title.setText(Constants.UI_EventsTitle);
		// back.setVisibility(View.GONE);
		// tvEventType.setVisibility(View.INVISIBLE);
		// settingBtn.setVisibility(View.VISIBLE);
		// searchHeaderBtn.setVisibility(View.VISIBLE);
		// addEventBtn.setVisibility(View.VISIBLE);

		if (mStacks.get(Constants.TAB_EVENT).size() == 0) {
			pushFragments(Constants.TAB_EVENT, new EventsFragment(), false,
					true);
		} else {
			// Displays last viewed fragment under events tab
			pushFragments(Constants.TAB_EVENT, mStacks.get(Constants.TAB_EVENT)
					.lastElement(), false, false);
		}
		// Holding current tab
		setmCurrentTab(Constants.TAB_EVENT);
		/*
		 * Deleted as fragments stack is maintained for each tab Fragment
		 * activeFragment = new EventsFragment(); FragmentManager
		 * fragmentManager = getSupportFragmentManager(); FragmentTransaction ft
		 * = fragmentManager.beginTransaction(); ft.replace(R.id.home_frame,
		 * activeFragment); ft.commit();
		 */

		// Display header layout
		events_header_llyt.setVisibility(View.VISIBLE);

		foothomeRlyt.setBackgroundColor(Color.parseColor("#000000"));
		footsearchRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		footcameraRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		footnotifiRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		footprofilePicRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		// UIHelperUtil.showToastMessage("Yet to implement");
	}

	/**
	 * click event for footer search
	 */
	public void clickFooterSearch(View view) {
		// Display header layout
		events_header_llyt.setVisibility(View.VISIBLE);
		search_header_llyt.setVisibility(View.GONE);
		// title.setText(Constants.UI_SearchTitle);
		// back.setVisibility(View.GONE);
		tvEventType.setVisibility(View.GONE);
		settingBtn.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		if (mStacks.get(Constants.TAB_SEARCH).size() == 0) {
			pushFragments(Constants.TAB_SEARCH, new SearchPageFragment(),
					false, true);
		} else {
			// Displays last viewed fragment under search tab
			pushFragments(Constants.TAB_SEARCH,
					mStacks.get(Constants.TAB_SEARCH).lastElement(), false,
					false);
		}
		// Holding current tab
		setmCurrentTab(Constants.TAB_SEARCH);

		// Fragment activeFragment = new SearchPageFragment();
		// FragmentManager fragmentManager = getSupportFragmentManager();
		// FragmentTransaction ft = fragmentManager.beginTransaction();
		// ft.replace(R.id.home_frame, activeFragment);
		// ft.commit();

		foothomeRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		footsearchRlyt.setBackgroundColor(Color.parseColor("#000000"));
		footcameraRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		footnotifiRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		footprofilePicRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		// UIHelperUtil.showToastMessage("Yet to implement");
	}

	/**
	 * click event for footer camera
	 */
	public void clickFooterCamera(View view) {
		// Display header layout
		events_header_llyt.setVisibility(View.VISIBLE);
		search_header_llyt.setVisibility(View.GONE);
		// title.setText("MEDIA");
		// back.setVisibility(View.GONE);
		tvEventType.setVisibility(View.GONE);
		settingBtn.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		if (mStacks.get(Constants.TAB_MEDIA).size() == 0) {
			pushFragments(Constants.TAB_MEDIA, new MediaFragment(), false, true);
		} else {
			// Displays last viewed fragment under media tab
			pushFragments(Constants.TAB_MEDIA, mStacks.get(Constants.TAB_MEDIA)
					.lastElement(), false, false);
		}
		// Holding current tab
		setmCurrentTab(Constants.TAB_MEDIA);

		// Fragment activeFragment = new YetToImplement();
		// FragmentManager fragmentManager = getSupportFragmentManager();
		// FragmentTransaction ft = fragmentManager.beginTransaction();
		// ft.replace(R.id.home_frame, activeFragment);
		// ft.commit();

		footcameraRlyt.setBackgroundColor(Color.parseColor("#000000"));
		footsearchRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		foothomeRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		footnotifiRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		footprofilePicRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		// UIHelperUtil.showToastMessage("Yet to implement");
	}

	/**
	 * click event for footer profile pic
	 */
	public void clickFooterProfilePic(View view) {

		// Display header layout
		events_header_llyt.setVisibility(View.VISIBLE);
		search_header_llyt.setVisibility(View.GONE);
		// title.setText(Constants.userProf_Title);
		// back.setVisibility(View.GONE);
		tvEventType.setVisibility(View.GONE);
		settingBtn.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);

		if (mStacks.get(Constants.TAB_USERS).size() == 0) {
			pushFragments(Constants.TAB_USERS, new UserProfileFragment(),
					false, true);
		} else {
			// Displays last viewed fragment under media tab
			mStacks.get(Constants.TAB_USERS).removeAllElements();
			pushFragments(Constants.TAB_USERS, new UserProfileFragment(),
					false, true);
			/*
			 * pushFragments(Constants.TAB_USERS,
			 * mStacks.get(Constants.TAB_USERS) .lastElement(), false, false);
			 */
		}
		// Holding current tab
		setmCurrentTab(Constants.TAB_USERS);

		/*
		 * Fragment activeFragment = new UserProfileFragment(); FragmentManager
		 * fragmentManager = getSupportFragmentManager(); FragmentTransaction ft
		 * = fragmentManager.beginTransaction(); ft.replace(R.id.home_frame,
		 * activeFragment); ft.commit();
		 */
		// Display header layout
		events_header_llyt.setVisibility(View.VISIBLE);
		search_header_llyt.setVisibility(View.GONE);

		footprofilePicRlyt.setBackgroundColor(Color.parseColor("#000000"));
		footsearchRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		foothomeRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		footnotifiRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		footcameraRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		// UIHelperUtil.showToastMessage("Yet to implement");
	}

	/**
	 * click event for footer notifications
	 */
	public void clickFooterNotifications(View view) {
		// Display header layout
		events_header_llyt.setVisibility(View.VISIBLE);
		search_header_llyt.setVisibility(View.GONE);
		// title.setText("NOTIFICATIONS");
		// back.setVisibility(View.GONE);
		tvEventType.setVisibility(View.GONE);
		settingBtn.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);

		if (mStacks.get(Constants.TAB_NOTIFICATION).size() == 0) {
			pushFragments(Constants.TAB_NOTIFICATION,
					new NotificationFragment(), false, true);
		} else {
			// Displays last viewed fragment under media tab
			pushFragments(Constants.TAB_NOTIFICATION,
					mStacks.get(Constants.TAB_NOTIFICATION).lastElement(),
					false, false);
		}
		// Holding current tab
		setmCurrentTab(Constants.TAB_NOTIFICATION);

		/*
		 * Fragment activeFragment = new YetToImplement(); FragmentManager
		 * fragmentManager = getSupportFragmentManager(); FragmentTransaction ft
		 * = fragmentManager.beginTransaction(); ft.replace(R.id.home_frame,
		 * activeFragment); ft.commit();
		 */
		// Display header layout
		events_header_llyt.setVisibility(View.VISIBLE);
		search_header_llyt.setVisibility(View.INVISIBLE);

		footnotifiRlyt.setBackgroundColor(Color.parseColor("#000000"));
		footsearchRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		footcameraRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		foothomeRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		footprofilePicRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		// UIHelperUtil.showToastMessage("Yet to implement");
	}

	// show exit dialog method
	private void showExitAlert() {
		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.dialog_two_btn);
		TextView message = (TextView) dialog
				.findViewById(R.id.dialog_text_title);
		Button no = (Button) dialog.findViewById(R.id.dialog_btn_n);
		Button yes = (Button) dialog.findViewById(R.id.dialog_btn_s);
		message.setText("Are you sure you want to Exit?");
		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				/*
				 * if(StartActivity.instance != null){
				 * StartActivity.instance.finish(); }
				 */
			}
		});
		dialog.show();
	}

	/**
	 * click event for back button
	 */
	public void backClick() {
		if (SharedPreferencesUtils.getUserId() != null
				&& SharedPreferencesUtils.getUserId().length() > 0) {
			showExitAlert();
		} else {
			finish();
		}

	}

	// @Override
	// public void onBackPressed() {
	// if (getSupportFragmentManager().getBackStackEntryCount()==1) {
	// backClick();
	// } else {
	// super.onBackPressed();
	// }
	// }

	/**
	 * This overridden method handles all the click actions from the user when
	 * he clicks on a place out side the edit text fields.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(fmLyt.getWindowToken(), 0);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// stopService(new Intent(MainActivity.this, LocationService.class));
		SharedPreferencesUtils.setLastAdTime(0);
		if (googleApiClient != null && googleApiClient.isConnected())
			googleApiClient.disconnect();
	}

	// /**
	// * Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using
	// AutoManage
	// * functionality.
	// * This automatically sets up the API client to handle Activity lifecycle
	// events.
	// */
	// private void rebuildGoogleApiClient() {
	// // When we build the GoogleApiClient we specify where connected and
	// connection failed
	// // callbacks should be returned and which Google APIs our app uses.
	// GoogleAutoCompleteLocation.mGoogleApiClient = new
	// GoogleApiClient.Builder(this)
	// .enableAutoManage(this, 0 /* clientId */, this)
	// .addConnectionCallbacks(this)
	// .addApi(Places.GEO_DATA_API)
	// .build();
	// }

	// @Override
	// public void onConnected(Bundle connectionHint) {
	// // TODO Auto-generated method stub
	// GoogleAutoCompleteLocation.mAdapter.setGoogleApiClient(GoogleAutoCompleteLocation.mGoogleApiClient);
	// Log.i("", "GoogleApiClient connected.");
	// }
	//
	// @Override
	// public void onConnectionSuspended(int cause) {
	// // TODO Auto-generated method stub
	// GoogleAutoCompleteLocation.mAdapter.setGoogleApiClient(null);
	// Log.e("", "GoogleApiClient connection suspended.");
	// }

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		Log.e("", "onConnectionFailed: ConnectionResult.getErrorCode() = "
				+ connectionResult.getErrorCode());

		// TODO(Developer): Check error code and notify the user of error state
		// and resolution.
		Toast.makeText(
				this,
				"Could not connect to Google API Client: Error "
						+ connectionResult.getErrorCode(), Toast.LENGTH_SHORT)
				.show();

		// // Disable API access in the adapter because the client was not
		// initialised correctly.
		// GoogleAutoCompleteLocation.mAdapter.setGoogleApiClient(null);
	}

	/*********
	 * ---------------------For communication between fragments
	 * STARTS-------------
	 **********/

	/*
	 * method: passData
	 */
	@Override
	public void passData(String message, String toFragment) {
		FragmentManager fm = getSupportFragmentManager();

		if (toFragment.equalsIgnoreCase(Constants.ToFragment_userProfile)) {
			// Create fragment & pass message(UserID) users profile
			UserProfileFragment activeFragment = new UserProfileFragment();
			Bundle arg = new Bundle();
			arg.putString(Constants.dataReceive, message);
			activeFragment.setArguments(arg);
			// displays user page in specified tab.
			pushFragments(mCurrentTab, activeFragment, false, true);
		} else if (toFragment
				.equalsIgnoreCase(Constants.ToFragment_followingsData)) {
			// Create fragment & pass message(UserID) to followings screen
			FollowingUsersListFragment activeFragment = new FollowingUsersListFragment();
			Bundle arg = new Bundle();
			arg.putString(Constants.dataReceive, message);
			activeFragment.setArguments(arg);
			// displays user page in specified tab.
			pushFragments(mCurrentTab, activeFragment, false, true);
		} else if (toFragment
				.equalsIgnoreCase(Constants.ToFragment_followersData)) {
			// Create fragment & pass message(UserID) to followers screen
			FollowerUsersListFragment activeFragment = new FollowerUsersListFragment();
			Bundle arg = new Bundle();
			arg.putString(Constants.dataReceive, message);
			activeFragment.setArguments(arg);
			// displays user page in specified tab.
			pushFragments(mCurrentTab, activeFragment, false, true);
		} else if (toFragment
				.equalsIgnoreCase(Constants.ToFragment_checkinEventsData)) {
			// Create fragment & pass message(UserID) to checked events
			CheckedEventsFragment activeFragment = new CheckedEventsFragment();
			Bundle arg = new Bundle();
			arg.putString(Constants.dataReceive, message);
			activeFragment.setArguments(arg);
			// displays user page in specified tab.
			pushFragments(mCurrentTab, activeFragment, false, true);
		} else if (toFragment
				.equalsIgnoreCase(Constants.ToFragment_editProfile)) {
			// Create fragment & pass message(UserID) to checked events
			EditProfileFragmentPage activeFragment = new EditProfileFragmentPage();
			Bundle arg = new Bundle();
			arg.putString(Constants.dataReceive, message);
			activeFragment.setArguments(arg);
			// displays user page in specified tab.
			pushFragments(mCurrentTab, activeFragment, false, true);
		} else if (toFragment.equalsIgnoreCase(Constants.ToFragment_myEvents)) {
			// Create fragment & pass message(UserID) to checked events
			MyEventsFragment activeFragment = new MyEventsFragment();
			Bundle arg = new Bundle();
			arg.putString(Constants.dataReceive, message);
			activeFragment.setArguments(arg);
			// displays user page in specified tab.
			pushFragments(mCurrentTab, activeFragment, false, true);
		} else if (toFragment.equalsIgnoreCase(Constants.ToFragment_search)) {
			// Create fragment & pass message(search message) to search fragment
			SearchPageFragment activeFragment;
			if (mStacks.get(Constants.TAB_SEARCH).size() == 0) {
				activeFragment = new SearchPageFragment();
			} else {
				// Displays last viewed fragment under search tab
				activeFragment = (SearchPageFragment) mStacks.get(
						Constants.TAB_SEARCH).lastElement();
			}
			Bundle arg = new Bundle();
			arg.putString(Constants.dataReceive, message);
			activeFragment.setArguments(arg);
			// displays user page in search tab it self.
			setmCurrentTab(Constants.TAB_SEARCH);

			if (mStacks.get(Constants.TAB_SEARCH).size() == 0) {
				pushFragments(Constants.TAB_SEARCH, activeFragment, false, true);
			} else {
				// Displays already created fragment under search tab
				pushFragments(Constants.TAB_SEARCH, activeFragment, false,
						false);
			}
			// Show search tab in the footer
			foothomeRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
			footsearchRlyt.setBackgroundColor(Color.parseColor("#000000"));
			footcameraRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
			footnotifiRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
			footprofilePicRlyt.setBackgroundColor(Color.parseColor("#2E2E2E"));
		}

	}

	/*********
	 * ---------------------For communication between fragments
	 * ENDS-------------
	 **********/

	/****
	 * -----------------------Stack of fragments are maintained for each tab
	 * -------START--------------
	 ****/

	/*
	 * To add fragment to a tab. tag -> Tab identifier fragment -> Fragment to
	 * show, in tab identified by tag shouldAnimate -> should animate
	 * transaction. false when we switch tabs, or adding first fragment to a tab
	 * true when when we are pushing more fragment into navigation stack.
	 * shouldAdd -> Should add to fragment navigation stack (mStacks.get(tag)).
	 * false when we are switching tabs (except for the first time) true in all
	 * other cases.
	 */
	public void pushFragments(String tag, Fragment fragment,
			boolean shouldAnimate, boolean shouldAdd) {
		if (shouldAdd)
			mStacks.get(tag).push(fragment);
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		// if(shouldAnimate)
		// ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		ft.replace(R.id.home_frame, fragment);
		ft.commit();
	}

	public String getmCurrentTab() {
		return mCurrentTab;
	}

	public void setmCurrentTab(String mCurrentTab) {
		this.mCurrentTab = mCurrentTab;
	}

	public Fragment getLastButOneFragment() {
		/*
		 * Select the second last fragment in current tab's stack.. which will
		 * be shown after the fragment transaction given below
		 */
		if (mStacks.get(mCurrentTab).size() > 1) {
			Fragment fragment = mStacks.get(mCurrentTab).elementAt(
					mStacks.get(mCurrentTab).size() - 2);
			return fragment;
		} else {
			return null;
		}
	}

	public void popFragments() {
		/*
		 * Select the second last fragment in current tab's stack.. which will
		 * be shown after the fragment transaction given below
		 */
		Fragment fragment = mStacks.get(mCurrentTab).elementAt(
				mStacks.get(mCurrentTab).size() - 2);

		/* pop current fragment from stack.. */
		mStacks.get(mCurrentTab).pop();

		/*
		 * We have the target fragment in hand.. Just show it.. Show a standard
		 * navigation animation
		 */
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		// ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
		ft.replace(R.id.home_frame, fragment);
		ft.commit();
	}

	public void popFragmentsWithOutDisplay(String tab) {
		/* pop current fragment from stack.. */
		mStacks.get(tab).pop();
	}

	public int getNumberOfItems(String tab) {
		/* pop current fragment from stack.. */
		return mStacks.get(tab).size();
	}

	@Override
	public void onBackPressed() {
		if (mStacks.get(mCurrentTab).size() == 1) {
			// We are already showing first fragment of current tab, so when
			// back pressed, we will finish this activity..
			backClick();
			return;
		}

		/*
		 * Each fragment represent a screen in application (at least in my
		 * requirement, just like an activity used to represent a screen). So if
		 * I want to do any particular action when back button is pressed, I can
		 * do that inside the fragment itself. For this I used AppBaseFragment,
		 * so that each fragment can override onBackPressed() or
		 * onActivityResult() kind of events, and activity can pass it to them.
		 * Make sure just do your non navigation (popping) logic in fragment,
		 * since popping of fragment is done here itself.
		 */
		// ((Fragment)mStacks.get(mCurrentTab).lastElement()).onBackPressed();

		/* Goto previous fragment in navigation stack of this tab */
		popFragments();
	}

	// /*
	// * Imagine if you wanted to get an image selected using ImagePicker intent
	// to the fragment. Ofcourse I could have created a public function
	// * in that fragment, and called it from the activity. But couldn't resist
	// myself.
	// */
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if(mStacks.get(mCurrentTab).size() == 0){
	// return;
	// }
	//
	// /*Now current fragment on screen gets onActivityResult callback..*/
	// mStacks.get(mCurrentTab).lastElement().onActivityResult(requestCode,
	// resultCode, data);
	// }
	/****
	 * ----------------Stack of fragments are maintained for each tab
	 * -----END------------------
	 ****/

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 1201) {
			popFragments();
		} else if (resultCode == 1301) {
			popFragments();
		}

		if (Session.getActiveSession() != null) {
			Session.getActiveSession().onActivityResult(MainActivity.this,
					requestCode, resultCode, data);
		}

		if (requestCode == REQUEST_LOCATION) {
			switch (resultCode) {
			case Activity.RESULT_CANCELED: {
				finish();
				break;
			}
			default: {
				break;
			}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mInstance = MainActivity.this;

	}

	@Override
	protected void onStop() {
		super.onStop();
		mInstance = null;
		if (googleApiClient != null)
			googleApiClient.disconnect();
	}

	public static MainActivity getInstance() {
		return mInstance;
	}

	private void getPNSDetails() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		if (intent == null) { // || !intent.hasExtra("message")
			return;
		}
		Bundle bundle = intent
				.getBundleExtra(Constants.KEY_NOTIFICATION_EXTRAS);
		if (bundle == null
				|| !bundle.containsKey(Constants.TAG_NOTIFICATION_TYPE)) {
			return;
		}
		// navigateToScreenBasedOnPNS(bundle);
		NotificationInfo tmpNotification = new NotificationInfo();
		tmpNotification.setSender_id(bundle
				.getString(Constants.TAG_NOTIFICATION_SENDER_ID));
		tmpNotification.setNotification_id(bundle
				.getString(Constants.TAG_NOTIFICATION_ID));
		tmpNotification.setSender_name(bundle
				.getString(Constants.TAG_NOTIFICATION_SENDER_NAME));
		tmpNotification.setSender_image_url(bundle
				.getString(Constants.TAG_NOTIFICATION_SENDER_PIC_URL));
		tmpNotification.setNotification_type(bundle
				.getString(Constants.TAG_NOTIFICATION_TYPE));
		tmpNotification.setData_id(bundle
				.getString(Constants.TAG_NOTIFICATION_DATA_ID));
		tmpNotification.setText(bundle
				.getString(Constants.TAG_NOTIFICATION_TEXT));
		tmpNotification.setData_url(bundle
				.getString(Constants.TAG_NOTIFICATION_DATA_URL));
		tmpNotification.setIs_viewed(bundle
				.getString(Constants.TAG_NOTIFICATION_VIEW_STATUS));
		tmpNotification.setCreated_date(bundle
				.getString(Constants.TAG_NOTIFICATION_DATE));
		tmpNotification.setEvent_id(bundle
				.getString(Constants.TAG_NOTIFICATION_EVENTID));
		if (!TextUtils.isEmpty(bundle
				.getString(Constants.TAG_NOTIFICATION_EVENTNAME))) {
			tmpNotification.setEvent_name(bundle.getString(
					Constants.TAG_NOTIFICATION_EVENTNAME).toUpperCase());
		}
		navigateToScreen(tmpNotification);
		CustomLog.d("MAINActivity", "" + bundle);
	}

	/*
	 * private void navigateToScreenBasedOnPNS(Bundle bundle) { String mType =
	 * bundle.getString("message"); if
	 * (mType.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_INVITE)) {
	 * triggerEventDetailsScreen(bundle); } else if
	 * (mType.equalsIgnoreCase(Constants
	 * .TAG_NOTIFICATION_TYPE_EVENT_PHOTO_TAGGED_USER)) {
	 * triggerEventMediaScreen(bundle); } else if
	 * (mType.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_LIKE)
	 * || mType.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_CAST_LIKE) ||
	 * mType.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_LIKE))
	 * { pushFragments(Constants.TAB_EVENT, new EventsFragment(), false); } else
	 * if
	 * (mType.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_COMMENT
	 * ) || mType.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_CAST_COMMENT)
	 * ||
	 * mType.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_COMMENT
	 * )) { triggerEventMediaScreen(bundle); } else if
	 * (mType.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_FOLLOW_USER)) {
	 * displayUserProfile(bundle); } CustomLog.v("PNS", "MType: " + mType); }
	 */

	private void triggerEventMediaScreen(Bundle bundle) {
		Intent intent = new Intent(getApplicationContext(),
				ShowTagEventActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void pushFragments(String tag, Fragment fragment, boolean shouldAdd) {
		if (shouldAdd) {
			mStacks.get(tag).push(fragment);
		}
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.replace(R.id.home_frame, fragment);
		ft.commit();
	}

	/*
	 * private void triggerEventDetailsScreen(Bundle tmpInfo) { if(tmpInfo !=
	 * null){ Fragment activeFragment = new EventDetailsFragment(); Bundle
	 * bundle = new Bundle(); // TODO change this used id.
	 * bundle.putString(Constants.KEY_USER_ID,
	 * SharedPreferencesUtils.getUserId());
	 * bundle.putString(Constants.KEY_EVENT_ID,
	 * tmpInfo.getString(Constants.TAG_NOTIFICATION_EVENTID));
	 * bundle.putString(Constants.KEY_EVENT_TYPE,
	 * tmpInfo.getString(Constants.TAG_NOTIFICATION_EVENTNAME));
	 * activeFragment.setArguments(bundle); //Adds the event-detailed fragment
	 * on to 'events fragment' stack & displayed on the screen
	 * pushFragments(getmCurrentTab(), activeFragment, false,true); } }
	 * //Display user profile public void displayUserProfile(Bundle tmpInfo){
	 * if(tmpInfo != null){ String userId =
	 * tmpInfo.getString(Constants.TAG_NOTIFICATION_SENDER_ID); if (userId !=
	 * null) { UserProfileFragment activeFragment = new UserProfileFragment();
	 * Bundle arg = new Bundle(); arg.putString(Constants.dataReceive, userId);
	 * activeFragment.setArguments(arg); pushFragments(getmCurrentTab(),
	 * activeFragment, false,true); } } }
	 */

	@Override
	public void onNotifyUser() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				tvAppNotifyCount.setVisibility(View.VISIBLE);
				tvAppNotifyCount.setText("" + getNotificationsCount());
			}
		});
	}

	private int getNotificationsCount() {
		int notificationsCount = SharedPreferencesUtils.getNotificationCount();
		SharedPreferencesUtils.setNotificationCount(++notificationsCount);
		return notificationsCount;
	}

	private void navigateToScreen(NotificationInfo tmpInfo) {
		if (tmpInfo != null) {
			if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_EVENT_INVITE)
					|| tmpInfo.getNotification_type().equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_EVENT_LIKE)) {
				triggerEventDetailsScreen(tmpInfo);
			} else if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_FOLLOW_USER)
					|| (tmpInfo.getNotification_type()
							.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_TAGGED_USER))) {
				displayUserProfile(tmpInfo);
			} else if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_CAST_COMMENT)
					|| tmpInfo.getNotification_type().equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_CAST_LIKE)) {
				triggerEventMediaScreen(tmpInfo, "cast", "event");
			} else if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_COMMENT)
					|| tmpInfo.getNotification_type().equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_LIKE)
					|| tmpInfo
							.getNotification_type()
							.equalsIgnoreCase(
									Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_TAGGED_USER)) {
				triggerEventMediaScreen(tmpInfo, "photo", "event");
			} else if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_COMMENT)
					|| tmpInfo.getNotification_type().equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_LIKE)
					|| tmpInfo
							.getNotification_type()
							.equalsIgnoreCase(
									Constants.TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_TAGGED_USER)) {
				triggerEventMediaScreen(tmpInfo, "photo", "profile");
			} else if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_COMMENT)
					|| tmpInfo.getNotification_type().equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_LIKE)
					|| tmpInfo
							.getNotification_type()
							.equalsIgnoreCase(
									Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_TAGGED_USER)) {
				triggerEventMediaScreen(tmpInfo, "video", "event");
			} else if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_COMMENT)
					|| tmpInfo.getNotification_type().equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_LIKE)
					|| tmpInfo
							.getNotification_type()
							.equalsIgnoreCase(
									Constants.TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_TAGGED_USER)) {
				triggerEventMediaScreen(tmpInfo, "video", "profile");
			}
		}
	}

	private void triggerEventMediaScreen(NotificationInfo tmpInfo,
			String mediaType, String resourceType) {
		if (tmpInfo != null) {
			Bundle args = new Bundle();
			args.putString(Constants.TAG_DISP_NOTIFICATION_MEDIA_ID,
					tmpInfo.getData_id());
			args.putString(Constants.TAG_DISP_NOTIFICATION_MEDIA_TYPE,
					mediaType);
			args.putString(Constants.TAG_DISP_NOTIFICATION_RESOURCE_TYPE,
					resourceType);
			Intent intent = new Intent(MainActivity.this,
					ShowTagEventActivity.class);
			intent.putExtras(args);
			startActivity(intent);
		}
	}

	private void triggerEventDetailsScreen(NotificationInfo tmpInfo) {
		if (tmpInfo != null) {
			Fragment activeFragment = new EventDetailsFragment();
			Bundle bundle = new Bundle();
			// TODO change this used id.
			bundle.putString(Constants.KEY_USER_ID,
					SharedPreferencesUtils.getUserId());
			bundle.putString(Constants.KEY_EVENT_ID, tmpInfo.getEvent_id());
			bundle.putString(Constants.KEY_EVENT_TYPE, tmpInfo.getEvent_name());
			activeFragment.setArguments(bundle);
			// Adds the event-detailed fragment on to 'events fragment' stack &
			// displayed on the screen
			pushFragments(getmCurrentTab(), activeFragment, false, true);
		}
	}

	// Display user profile
	public void displayUserProfile(NotificationInfo tmpInfo) {
		if (tmpInfo != null) {
			String userId = tmpInfo.getSender_id();
			if (userId != null) {
				UserProfileFragment activeFragment = new UserProfileFragment();
				Bundle arg = new Bundle();
				arg.putString(Constants.dataReceive, userId);
				activeFragment.setArguments(arg);
				pushFragments(getmCurrentTab(), activeFragment, false, true);
			}
		}
	}

	// for location

	protected synchronized void buildGoogleApiClient() {
		googleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Location mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(googleApiClient);
		if (mLastLocation != null) {
			latitude = mLastLocation.getLatitude() + "";
			longitude = mLastLocation.getLongitude() + "";

		}
		createLocationRequest();
		LocationServices.FusedLocationApi.requestLocationUpdates(
				googleApiClient, locationRequest, new LocationListener() {

					@Override
					public void onLocationChanged(Location location) {
						if (location != null) {
							longitude = location.getLongitude() + "";
							latitude = location.getLatitude() + "";
						}
						startTheActualProcess();

					}
				});

		if (longitude != null && latitude != null) {
			startTheActualProcess();
		}

	}

	/*
	 * getting location name
	 */
	private void getAddressFromLocation(final double latitude2,
			final double longitude2, MainActivity mainActivity) {

		Thread thread = new Thread() {
			@Override
			public void run() {
				Geocoder geocoder = new Geocoder(MainActivity.this,
						Locale.getDefault());
				String result = null;
				try {
					List<Address> addressList = geocoder.getFromLocation(
							latitude2, longitude2, 1);
					if (addressList != null && addressList.size() > 0) {
						Address address = addressList.get(0);
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
							sb.append(address.getAddressLine(i)).append("\n");
						}
						sb.append(address.getLocality()).append("\n");
						sb.append(address.getPostalCode()).append("\n");
						sb.append(address.getCountryName());
						result = sb.toString();
						if (address.getLocality() != null) {
							Constants.locationName = address.getLocality()
									+ ", " + address.getCountryName();
						} else {
							Constants.locationName = address.getCountryName();
						}

						SharedPreferencesUtils
								.setLocaton(Constants.locationName);
					}
				} catch (IOException e) {
					Log.e(Constants.logUrl, "Unable connect to Geocoder", e);
				} finally {

				}
			}
		};
		thread.start();

	}

	private void mainCode() {
		mInstance = MainActivity.this;
		MySportsApp.setActivityContext(MainActivity.this);
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.titlebar);
		ImageView settings = (ImageView) mActionBar.getCustomView()
				.findViewById(R.id.setting_iv);

		/*
		 * Navigation stacks for each tab gets created.. tab identifier is used
		 * as key to get respective stack for each tab
		 */
		mStacks = new HashMap<String, Stack<Fragment>>();
		mStacks.put(Constants.TAB_EVENT, new Stack<Fragment>());
		mStacks.put(Constants.TAB_SEARCH, new Stack<Fragment>());
		mStacks.put(Constants.TAB_MEDIA, new Stack<Fragment>());
		mStacks.put(Constants.TAB_NOTIFICATION, new Stack<Fragment>());
		mStacks.put(Constants.TAB_USERS, new Stack<Fragment>());

		if (SharedPreferencesUtils.getUserId() != null
				&& SharedPreferencesUtils.getUserId().length() > 0) {
			if (mStacks.get(Constants.TAB_EVENT).size() == 0) {
				pushFragments(Constants.TAB_EVENT, new EventsFragment(), false,
						true);
			} else {
				// Displays last viewed fragment under events tab
				pushFragments(Constants.TAB_EVENT,
						mStacks.get(Constants.TAB_EVENT).lastElement(), false,
						false);
			}
			// Holding current tab
			setmCurrentTab(Constants.TAB_EVENT);
		} else {
			Intent getstartIntent = new Intent(MainActivity.this,
					StartActivity.class);
			startActivity(getstartIntent);
			finish();
		}

		// intent.putExtra("type", "loginAlreadyDone");
		setContentView(R.layout.activity_main);
		init();
		foothomeRlyt.setBackgroundColor(Color.parseColor("#000000"));
		fmLyt = (FrameLayout) findViewById(R.id.home_frame);
		Constants.mContext = MainActivity.this;
		Utils.registerForGCM(getApplicationContext());

		if (SocialNetworkingUtils.mTwitter == null)
			SocialNetworkingUtils.mTwitter = new Twitter(this,
					HomeActivity.CONSUMER_KEY, HomeActivity.CONSUMER_SECRET,
					HomeActivity.CALLBACK_URL);
		getPNSDetails();
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub

	}

	private void enableLoc() {

		if (googleApiClient == null) {
			googleApiClient = new GoogleApiClient.Builder(this)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(
							new GoogleApiClient.OnConnectionFailedListener() {
								@Override
								public void onConnectionFailed(
										ConnectionResult connectionResult) {

								}
							}).build();
			googleApiClient.connect();

			createLocationRequest();
			final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				showGPSSettingsAlert();
			}
		}

	}

	private void showGPSSettingsAlert() {
		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
				.addLocationRequest(locationRequest);
		builder.setAlwaysShow(true);
		PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
				.checkLocationSettings(googleApiClient, builder.build());
		result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
			@Override
			public void onResult(LocationSettingsResult result) {
				final Status status = result.getStatus();
				switch (status.getStatusCode()) {
				case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
					try {
						// Show the dialog by calling
						// startResolutionForResult(),
						// and check the result in onActivityResult().
						status.startResolutionForResult(MainActivity.this,
								REQUEST_LOCATION);
					} catch (IntentSender.SendIntentException e) {
						// Ignore the error.
					}
					break;
				}
			}
		});
	}

	private void createLocationRequest() {
		if (locationRequest == null) {
			locationRequest = LocationRequest.create();
			locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			locationRequest.setInterval(5 * 60 * 1000);
			locationRequest.setFastestInterval(5 * 1000);
		}
	}

	private void startTheActualProcess() {
		if (isForFirstTime) {
			progressDlg.dismiss();
			Constants.lati = latitude;
			Constants.longi = longitude;
			SharedPreferencesUtils.setLatitude(latitude);
			SharedPreferencesUtils.setLongitude(longitude);
			getAddressFromLocation(Double.parseDouble(latitude),
					Double.parseDouble(longitude), MainActivity.this);
			mainCode();
			isForFirstTime = false;
		}
	}
}