package com.mysportsshare.mysportscast.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.londatiga.android.twitter.Twitter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CalendarEventsActivity;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.activity.ShowTagEventActivity;
import com.mysportsshare.mysportscast.adapters.EventDetailsAdapter;
import com.mysportsshare.mysportscast.adapters.MenuItemListAdapter;
import com.mysportsshare.mysportscast.adapters.UsersListAdapter;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.models.EventInfo;
import com.mysportsshare.mysportscast.models.TeamInfo;
import com.mysportsshare.mysportscast.models.UserProfileInfo;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.HelperMethods;
import com.mysportsshare.mysportscast.utils.HelperMethods.TwitterCallback;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.SocialNetworkingUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;
import com.sromku.simple.fb.SimpleFacebook;

/**
 * This Class will show event details,
 * 
 * @author sharma
 * 
 */
public class EventDetailsFragment extends Fragment implements OnClickListener,
OnItemClickListener {

	/**
	 * Views
	 */
	private TextView title;
	private TextView tvEventSportName;
	private TextView tvEventTitle;
	private TextView tvEventType;
	private TextView tvEventStartDate;
	private TextView tvEventStartTime;
	private TextView tvEventLocation;
	private TextView tvEventCheersCount;
	private TextView tvEventAttendingCount;
	private TextView tvEventDistanceFromUser;
	private ListView lvEventDetails;
	private TextView tvEventTypeTitle;
	private ImageView imgIamAttending;
	private ImageView eventDetailsAddMediaImg;
	private ImageView eventDetailsAttendingCount;
	private ImageView imgLikedEvent;
	private RelativeLayout search_header_llyt;
	private RelativeLayout events_header_llyt;
	private RelativeLayout overlay_menu_llyt;
	private ImageView overlay_menu_iv;
	private TextView overlay_event_details_media_type;
	private TextView i_am_attending;
	private ImageView event_det_location_img;
	private View footer;

	LinearLayout layoutCheers;
	LinearLayout layoutAttending;
	LinearLayout layoutAmAttending;
	LinearLayout layoutAddMedia;
	LinearLayout event_det_dist_layout;
	RelativeLayout event_media_layout;
	LinearLayout no_event_media_layout;
	TextView no_event_media_layout_text;
	LinearLayout add_event_media_layout;

	// For scrolling event details header
	private LinearLayout scrollEventHeader;

	// drawerlayout objects
	private DrawerLayout mDrawerLayout;
	private LinearLayout mDrawer;
	private ListView mDrawerLv;
	private MenuItemListAdapter mAdapter;
	private List<HashMap<String, String>> menuList;
	private String[] mMenus;
	private String ITEM = "menuItemName";
	private String ITEM_POSITION = "position";
	private String eventType="";
	private String eventId;
	private String eventTitle;
	private String eventPic;
	private String event_creator_Id;
	private EventDetailsAdapter adapter;

	EventInfo eventinfo;
	private Activity activity;
	private InterstitialAd interstitial;
	static int adCnt = 0; //Used to limit ads 
	static final int ADS_MAX_CNT = 5; //max limit of ads to display 
//	private ImageView imgBuyTicket;
	private RelativeLayout event_det_buy_ticket_layout;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_event_details,
				container, false);
		init(view);

		SocialNetworkingUtils.isFirstTimeCalled = false;
		if (!Utils.chkStatus()) {
			Utils.networkAlertDialog(activity,
					getResources().getString(R.string.toast_no_network));
		} else {
			SocialNetworkingUtils.uiHelper = new UiLifecycleHelper(activity,
					null);
			SocialNetworkingUtils.uiHelper.onCreate(savedInstanceState);
			SocialNetworkingUtils.mSimpleFacebook = SimpleFacebook.getInstance(getActivity());
		}

		SocialNetworkingUtils.canPresentShareDialog = FacebookDialog
				.canPresentShareDialog(activity,
						FacebookDialog.ShareDialogFeature.SHARE_DIALOG);
		menuList = new ArrayList<HashMap<String, String>>();
		mMenus = getResources().getStringArray(R.array.broadcasts);
		for (int i = 0; i < mMenus.length; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(ITEM, mMenus[i]);
			hm.put(ITEM_POSITION, String.valueOf(i));
			menuList.add(hm);
		}
		mAdapter = new MenuItemListAdapter(activity, menuList);

		mDrawerLv.setAdapter(mAdapter);
		mDrawerLv.setOnItemClickListener(this);
		
		return view;
	}
	/*-------------------- Google Ads displaying--------------*/
	private void configuringAdMob() {
		// Prepare the Interstitial Ad
		interstitial = new InterstitialAd(activity);
		// Insert the Ad Unit ID
		interstitial.setAdUnitId(Constants.AdUnitID_Interstitial);		

		// Request for Ads
		AdRequest adRequest = new AdRequest.Builder()
		.build();

		// Load ads into Interstitial Ads
		interstitial.loadAd(adRequest);

		// Prepare an Interstitial Ad Listener
		interstitial.setAdListener(new AdListener() {
			public void onAdLoaded() {
				if(adCnt%ADS_MAX_CNT == 0){
					// Call displayInterstitial() function
					if(System.currentTimeMillis() - SharedPreferencesUtils.getLastAdTime() > 180000){
						SharedPreferencesUtils.setLastAdTime(System.currentTimeMillis());
						displayInterstitial();	
					}
									
					countEventDetailsPage();
				}
			}		
		});
	}
	
	private void countEventDetailsPage() {
		adCnt++;
	}
	public void displayInterstitial() {
		// If Ads are loaded, show Interstitial else show nothing.
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}
	/*-------------------- Google Ads displaying--------------*/

	@Override
	public void onItemClick(AdapterView<?> adView, View view, int position,
			long arg3) {
		// TODO Auto-generated method stub
		// UIHelperUtil.showToastMessage("Yet to implement");
		Log.v("",
				"Position: " + position + " viewId: " + view.getId() + " : "
						+ R.id.drawer_list + " : " + R.id.events_lv + " : "
						+ adView.getId());
		if (adView.getId() == R.id.drawer_list) {
			showMenuitemDetails(position);
			mDrawerLayout.closeDrawer(mDrawer);
		}
	}

	// show method for each menu item
	private void showMenuitemDetails(int position) {
		tvEventTypeTitle.setText(mMenus[position]);
		overlay_event_details_media_type.setText(mMenus[position]);
		if (adapter != null && adapter.getResultDetails() != null) {
			HashMap<String, ArrayList<EventInfo.EventMedia>> eventDataMap = adapter
					.getResultDetails();
			switch (position) {

			case 0:
				// Display all broadcast event-media
				mDrawerLayout.openDrawer(mDrawer);
				adapter = null;
				
				callingGetEventService("all");
//				serviceToGetEventDetailsInfo(userId, eventId);
//				adapter.setResultDetails(eventDataMap);
				break;

			case 1:
				// Display all photos
				mDrawerLayout.openDrawer(mDrawer);
				adapter = null;
				callingGetEventService(Constants.KEYs[1]);
//				sortBasedOnEventType(Constants.KEYs[1], eventDataMap);
				break;

			case 2:
				// Display all videos
				adapter = null;
				callingGetEventService(Constants.KEYs[2]);
//				sortBasedOnEventType(Constants.KEYs[2], eventDataMap);
				mDrawerLayout.openDrawer(mDrawer);
				break;

			case 3:
				// Display all casts
				mDrawerLayout.openDrawer(mDrawer);
				adapter = null;
				callingGetEventService(Constants.KEYs[3]);
//				sortBasedOnEventType(Constants.KEYs[3], eventDataMap);
				break;

			}
		} else {
			UIHelperUtil.showToastMessage("No data");
		}
	}

	private void callingGetEventService(final String filterBy){
		if(lvEventDetails.getFooterViewsCount() > 0){
			lvEventDetails.removeFooterView(footer);
		}
		Bundle bundle = getArguments();
		if (bundle != null) {
			eventId = bundle.getString(Constants.KEY_EVENT_ID);
			String userId = bundle.getString(Constants.KEY_USER_ID);
			eventType = bundle.getString(Constants.KEY_EVENT_TYPE,"upcomming");
			TextView tvEventType = (TextView) activity
					.findViewById(R.id.title_bar_tv_event_type);
			tvEventType.setVisibility(View.VISIBLE);
			tvEventType.setText(eventType);
			// TODO change this
			serviceToGetEventDetailsInfo(userId, eventId,filterBy);
			// serviceToGetEventDetailsInfo("2", "129"); //Event contains all
			// types of event media. Used for testing purpose
			Log.v("", "Params are: " + eventId + " : " + userId);
		} else {
			Log.v("", "No Params");
		}
	}
	/**
	 * sort based on type for event
	 */
	private void sortBasedOnEventType(String type,
			HashMap<String, ArrayList<EventInfo.EventMedia>> eventDataMap) {
		HashMap<String, ArrayList<EventInfo.EventMedia>> sortedEventDetailsMap = new HashMap<String, ArrayList<EventInfo.EventMedia>>();
		ArrayList<EventInfo.EventMedia> eventsList = new ArrayList<EventInfo.EventMedia>();
		for (Map.Entry<String, ArrayList<EventInfo.EventMedia>> element : eventDataMap
				.entrySet()) {
			if (type.equalsIgnoreCase(element.getKey())) {
				eventsList.addAll(element.getValue());
			}
		}
		sortedEventDetailsMap.put(type, eventsList);
		adapter.setResultDetails(sortedEventDetailsMap);
		adapter.notifyDataSetChanged();
	}

	/**
	 * Initializing all views in EventDetails page.
	 * 
	 * @param v
	 */
	private void init(View v) {
		lvEventDetails = (ListView) v.findViewById(R.id.events_details_lv);
		event_media_layout = (RelativeLayout) v
				.findViewById(R.id.MediaExistLayout);

		View header = activity.getLayoutInflater().inflate(
				R.layout.event_details_header, lvEventDetails, false);
		lvEventDetails.addHeaderView(header, null, false);

		tvEventSportName = (TextView) lvEventDetails
				.findViewById(R.id.event_det_sport_name_tv);
		tvEventTitle = (TextView) lvEventDetails
				.findViewById(R.id.event_det_sub_title);
		tvEventType = (TextView) lvEventDetails
				.findViewById(R.id.event_det_sport_type_tv);
		tvEventStartDate = (TextView) lvEventDetails
				.findViewById(R.id.event_det_date);
		tvEventStartTime = (TextView) lvEventDetails
				.findViewById(R.id.event_det_time);
		/*imgBuyTicket = (ImageView) lvEventDetails
				.findViewById(R.id.event_det_buy_ticket);*/
		event_det_buy_ticket_layout =(RelativeLayout) lvEventDetails
				.findViewById(R.id.event_det_buy_ticket_layout);
		tvEventLocation = (TextView) lvEventDetails
				.findViewById(R.id.event_det_location_tv);
		imgIamAttending = (ImageView) lvEventDetails
				.findViewById(R.id.event_details_iamattending);
		imgLikedEvent = (ImageView) lvEventDetails
				.findViewById(R.id.event_details_cheer_img);
		eventDetailsAddMediaImg = (ImageView) lvEventDetails
				.findViewById(R.id.event_details_add_media_img);
		i_am_attending = (TextView) lvEventDetails
				.findViewById(R.id.i_am_attending);
		event_det_location_img = (ImageView) lvEventDetails
				.findViewById(R.id.event_det_location_img);
		eventDetailsAttendingCount = (ImageView) lvEventDetails
				.findViewById(R.id.event_details_attending_count);
		event_det_dist_layout = (LinearLayout) lvEventDetails
				.findViewById(R.id.event_det_dist_layout);
		/*
		 * TextView tvEventTeam1Type = (TextView)
		 * v.findViewById(R.id.event_det_sport_name_tv); TextView tvEventTeamTy
		 * = (TextView) v.findViewById(R.id.event_det_sub_title);
		 */
		tvEventCheersCount = (TextView) lvEventDetails
				.findViewById(R.id.cheers_count);
		tvEventAttendingCount = (TextView) lvEventDetails
				.findViewById(R.id.attendance);
		tvEventDistanceFromUser = (TextView) lvEventDetails
				.findViewById(R.id.event_det_dist_tv);
		ImageView menuImgBtn = (ImageView) lvEventDetails
				.findViewById(R.id.menu_iv);
		// add_event_media_layout= (LinearLayout)
		// lvEventDetails.findViewById(R.id.addMediaText);
		mDrawerLayout = (DrawerLayout) v.findViewById(R.id.drawer_layout);
		mDrawer = (LinearLayout) v.findViewById(R.id.drawer);
		mDrawerLv = (ListView) v.findViewById(R.id.drawer_list);
		tvEventTypeTitle = (TextView) lvEventDetails
				.findViewById(R.id.event_details_media_type);
		search_header_llyt = (RelativeLayout) activity
				.findViewById(R.id.search_header_llyt);
		events_header_llyt = (RelativeLayout) activity
				.findViewById(R.id.events_header_llyt);
		overlay_menu_llyt = (RelativeLayout) v
				.findViewById(R.id.overlay_menu_llyt);
		overlay_menu_iv = (ImageView) v.findViewById(R.id.overlay_menu_iv);
		overlay_event_details_media_type = (TextView) v
				.findViewById(R.id.overlay_event_details_media_type);

		layoutCheers = (LinearLayout) lvEventDetails
				.findViewById(R.id.event_details_cheers_layout);
		layoutAttending = (LinearLayout) lvEventDetails
				.findViewById(R.id.event_details_attending_count_layout);
		layoutAmAttending = (LinearLayout) lvEventDetails
				.findViewById(R.id.event_details_am_attending_layout);
		layoutAddMedia = (LinearLayout) lvEventDetails
				.findViewById(R.id.event_details_add_media_layout);

		imgIamAttending.setOnClickListener(this);
		imgLikedEvent.setOnClickListener(this);
		event_det_location_img.setOnClickListener(this);
		// layoutCheers.setOnClickListener(this);
		layoutAttending.setOnClickListener(this);
		layoutAmAttending.setOnClickListener(this);
		layoutAddMedia.setOnClickListener(this);
		event_det_dist_layout.setOnClickListener(this);
		overlay_menu_iv.setOnClickListener(this);
		menuImgBtn.setOnClickListener(this);
		tvEventLocation.setOnClickListener(this);
//		imgBuyTicket.setOnClickListener(this);
		event_det_buy_ticket_layout.setOnClickListener(this);
		// add_event_media_layout.setOnClickListener(this);

		lvEventDetails.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem > 0) {
					overlay_menu_llyt.setVisibility(View.VISIBLE);
				} else {
					overlay_menu_llyt.setVisibility(View.GONE);
				}
			}
		});
		// scrollHeader
		SocialNetworkingUtils.mTwitter = new Twitter(activity,
				SocialNetworkingUtils.CONSUMER_KEY,
				SocialNetworkingUtils.CONSUMER_SECRET,
				SocialNetworkingUtils.CALLBACK_URL);

		//Used to log count of 'opening of event details page' 
		countEventDetailsPage();
	}

	@Override
	public void onResume() {
		super.onResume();
		
		lvEventDetails.removeFooterView(footer);
		Log.v("", "EventDetails fragment onResume");
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		if (activity != null) {
			if(eventinfo!=null){
				title.setText(eventinfo.getEventTitle().toUpperCase());
			}else{
				if (activity.getClass().getSimpleName()
						.equalsIgnoreCase("MainActivity")) {
					if (((MainActivity) activity).getCurrentEvent() != null) {
						if(((MainActivity) activity).getCurrentEvent().getEventId().equalsIgnoreCase(eventId+"")){
							title.setText(((MainActivity) activity).getCurrentEvent()
									.getEventTitle().toUpperCase());
						}
					} else {
						title.setText("EVENT TITLE");
					}
				} else {
					title.setText("EVENT TITLE");
				}
			}
		} else {
			title.setText("EVENT TITLE");
		}
		title.setVisibility(View.VISIBLE);
		title.setSelected(true);
		ImageView settingBtn = (ImageView) activity
				.findViewById(R.id.setting_iv);
		settingBtn.setImageResource(R.drawable.event_details_share_white);
		settingBtn.setVisibility(View.VISIBLE);
		ImageView back = (ImageView) activity.findViewById(R.id.back_iv);
		back.setVisibility(View.VISIBLE);
		settingBtn.setOnClickListener(this);
		back.setOnClickListener(this);
		ImageView searchImg = (ImageView) activity.findViewById(R.id.search_iv);
		ImageView addEventImg = (ImageView) activity
				.findViewById(R.id.add_an_event_iv);
		searchImg.setVisibility(View.INVISIBLE);
		addEventImg.setVisibility(View.INVISIBLE);
		events_header_llyt.setVisibility(View.VISIBLE);
		search_header_llyt.setVisibility(View.GONE);

		// facebook
		if (SocialNetworkingUtils.uiHelper != null) {
			SocialNetworkingUtils.uiHelper.onResume();
		}

		if (SocialNetworkingUtils.isFirstTimeCalled) {
			Session session1 = Session.getActiveSession();
			if (session1 != null && session1.isOpened()) {
				Log.d("Tag", "Success1! " + "if" + session1);
				SocialNetworkingUtils.publishFeedDialog(
						eventinfo.getSportName() + " "
								+ eventinfo.getEventType() + " \""
								+ eventinfo.getEventTitle() + "\" is going at "
								+ eventinfo.getEventAddress() + " on "
								+ eventinfo.getEventStartDate() + " "
								+ eventinfo.getEventStartTime()
								+ ".  For more details check out this link: ",
								eventinfo.getEventShareURL(),
								eventinfo.getEventImageUrl(), activity);
				SocialNetworkingUtils.isFirstTimeCalled = false;
			} else {
				UIHelperUtil.showToastMessage("Session is null");
			}
		}
		configuringAdMob();
		Bundle bundle = getArguments();
		if (bundle != null) {
			eventId = bundle.getString(Constants.KEY_EVENT_ID);
			String userId = bundle.getString(Constants.KEY_USER_ID);
			eventType = bundle.getString(Constants.KEY_EVENT_TYPE,"upcomming");
			TextView tvEventType = (TextView) activity
					.findViewById(R.id.title_bar_tv_event_type);
			tvEventType.setVisibility(View.VISIBLE);
			tvEventType.setText(eventType);
			// TODO change this
			serviceToGetEventDetailsInfo(userId, eventId,"all");
			// serviceToGetEventDetailsInfo("2", "129"); //Event contains all
			// types of event media. Used for testing purpose
			Log.v("", "Params are: " + eventId + " : " + userId);
		} else {
			Log.v("", "No Params");
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (activity != null) {
			if (activity.getClass().getSimpleName()
					.equalsIgnoreCase("MainActivity")) {
				if ((((MainActivity) activity).getCurrentEvent() != null) && (((MainActivity) activity).getCurrentEvent().getEventId().equalsIgnoreCase(eventId+""))) {
					if (eventinfo != null) {
						((MainActivity) activity).getCurrentEvent()
						.setAttendingCount(
								eventinfo.getAttendingCount());
						((MainActivity) activity).getCurrentEvent()
						.setEventLikes(eventinfo.getEventLikes());
						((MainActivity) activity).getCurrentEvent()
						.setEventMediaList(
								eventinfo.getEventMediaList());
					}
				}
			}
		}
	}

	/**
	 * Filling the details page with details.
	 * 
	 * @param eventinfo
	 */
	private void setDetailsPage(EventInfo eventinfo) {
		tvEventSportName.setText(eventinfo.getSportName());
		tvEventTitle.setText(eventinfo.getEventTitle());
		tvEventTitle.setSelected(true);
		eventTitle = eventinfo.getEventTitle().toString();
		eventPic = eventinfo.getEventImageUrl();
		tvEventType.setText(eventinfo.getEventType());
		tvEventStartDate.setText(eventinfo.getEventStartDateAndTime() + "  -  "
				+ eventinfo.getEventEndTime());
		tvEventStartTime.setText(eventinfo.getEventStartTime());
		tvEventLocation.setText(eventinfo.getEventSubLocation());
		tvEventCheersCount.setText(eventinfo.getEventLikes() + " cheers");

		tvEventDistanceFromUser
		.setText(eventinfo.getEventDistanceFromUserLoc() + "m");
		tvEventType.setText(eventinfo.getEventType());
		title.setText(eventinfo.getEventTitle());
		if (eventinfo.getIsAttending().equalsIgnoreCase("false")) {
			imgIamAttending
			.setImageResource(R.drawable.event_details_iamattending_empty);
			layoutAddMedia.setEnabled(false);
			eventDetailsAddMediaImg
			.setImageResource(R.drawable.event_details_addmedia);
			//added by koti 29/09/15
			i_am_attending.setText("Check In");
		} else {
			imgIamAttending
			.setImageResource(R.drawable.event_details_iamattending);
			layoutAddMedia.setEnabled(true);
			eventDetailsAddMediaImg
			.setImageResource(R.drawable.event_details_addmedia_enable);
			//added by koti 29/09/15
			i_am_attending.setText("Attending");
		}
		if (eventType.equalsIgnoreCase("FINISHED")) {
			// User should not check the I'm attending status
			i_am_attending.setText("I was here");
			tvEventAttendingCount.setText(eventinfo.getAttendingCount()
					+ " were here");
		} else {
			/*i_am_attending.setText("I am attending");*/
			//comment by koti 29/09/15
//			i_am_attending.setText("Attending");
			tvEventAttendingCount.setText(eventinfo.getAttendingCount()
					+ " attending");
		}
		if (eventinfo.getIsUserLikedEvent().equalsIgnoreCase("true")) {
			// Load user liked image
			imgLikedEvent.setImageResource(R.drawable.event_details_cheered);
		} else {
			// Load user default like image
			imgLikedEvent.setImageResource(R.drawable.event_details_cheers);
		}
		if (eventinfo.getAttendingCount().equalsIgnoreCase("0")) {
			eventDetailsAttendingCount
			.setImageResource(R.drawable.event_details_no_attending);
			layoutAttending.setOnClickListener(null);
		} else {
			eventDetailsAttendingCount
			.setImageResource(R.drawable.event_details_attending);
			layoutAttending.setOnClickListener(this);
		}

		//Enabling buy tickets for upcoming events
		if(eventinfo.isStubhubEvent() && eventinfo.isUpcommingEvent()){
//			imgBuyTicket.setVisibility(View.VISIBLE);
			event_det_buy_ticket_layout.setVisibility(View.VISIBLE);
		}else{
//			imgBuyTicket.setVisibility(View.GONE);
			event_det_buy_ticket_layout.setVisibility(View.GONE);
		}
	}

	// Displays 'no event msg' on the screen. Msg is added to footer of the list
	private void AddMediaUI_to_list() {
		if (eventinfo != null) {
			if (eventinfo.hasEventMedia()) {
				// When event media does not exist for an event
				if (no_event_media_layout != null) {
					no_event_media_layout.setVisibility(View.GONE);
					no_event_media_layout.setOnClickListener(null);
				}
			} else {
				footer = activity.getLayoutInflater().inflate(
						R.layout.event_details_footer, lvEventDetails, false);
				lvEventDetails.addFooterView(footer, null, false);
				no_event_media_layout = (LinearLayout) lvEventDetails
						.findViewById(R.id.addMediaTextLayout);
				no_event_media_layout.setVisibility(View.VISIBLE);
				no_event_media_layout.setOnClickListener(this);
				no_event_media_layout_text = (TextView) lvEventDetails
						.findViewById(R.id.addMediaText);
				no_event_media_layout_text.setOnClickListener(this);
			}
		}
	}

	private void displayOnActivity(Fragment activeFragment) {
		if (activity.getClass().getSimpleName()
				.equalsIgnoreCase("MainActivity")) {
			((MainActivity) activity).pushFragments(
					((MainActivity) activity).getmCurrentTab(), activeFragment,
					false, true);
		} else if (activity.getClass().getSimpleName()
				.equalsIgnoreCase("CalendarEventsActivity")) {
			// stack & displayed on the Calendar screen
			((CalendarEventsActivity) activity).push(activeFragment);
		} else if (activity.getClass().getSimpleName()
				.equalsIgnoreCase("ShowTagEventActivity")) {
			// stack & displayed on the Calendar screen
			((ShowTagEventActivity) activity).push(activeFragment);
		}
	}

	private void popFromActivity() {
		if (activity.getClass().getSimpleName()
				.equalsIgnoreCase("MainActivity")) {
			((MainActivity) activity).popFragments();
		} else if (activity.getClass().getSimpleName()
				.equalsIgnoreCase("CalendarEventsActivity")) {
			// stack & displayed on the Calendar screen
			((CalendarEventsActivity) activity).popFragments();
		} else if (activity.getClass().getSimpleName()
				.equalsIgnoreCase("ShowTagEventActivity")) {
			// stack & displayed on the Calendar screen
			((ShowTagEventActivity) activity).popFragments();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.event_det_location_img: // Click on location image
		case R.id.event_det_location_tv: // Click on location text
		case R.id.event_det_dist_layout: // Click on distance from current loc
			// text

			// Display route from current location
			Uri dstLocQuery = Uri.parse("http://maps.google.com/maps?saddr="
					+ Constants.lati + "," + Constants.longi + "&daddr="
					+ eventinfo.getLat() + "," + eventinfo.getLng());
			Intent intent = new Intent(Intent.ACTION_VIEW, dstLocQuery);
			intent.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			PackageManager packageManager = activity.getPackageManager();
			List activities = packageManager.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
			boolean isIntentSafe = activities.size() > 0;
			if (isIntentSafe) {
				startActivity(intent);
			} else {
				UIHelperUtil
				.showToastMessage("Please install google maps app..");
			}
			break;
		case R.id.back_iv:
			if (activity != null) {
				(activity).onBackPressed();
			}
			break;
		case R.id.event_details_cheer_img:
			// Update event cheer status
			if (eventinfo.getIsUserLikedEvent().equalsIgnoreCase("true")) {
				// User wants to unlike the event
				serviceToUpdateEventCheer(eventId, eventinfo.getCreatorId(),
						"false");
			} else {
				// User wants to like to that event
				serviceToUpdateEventCheer(eventId, eventinfo.getCreatorId(),
						"true");
			}
			break;
		case R.id.setting_iv:
			showShareDialog();
			/*
			 * Log.v("", "Event details fragment:"); //Share message: Baseball
			 * event "IPL12" is going at Hyd on 22-May-2015 12:30. For more
			 * details check out this link IntentUtils.shareWithMedia(activity,
			 * eventinfo
			 * .getSportName()+" "+eventinfo.getEventType()+" \""+eventinfo
			 * .getEventTitle()+"\" is going at "+
			 * eventinfo.getEventAddress()+" on "+ eventinfo.getEventStartDate()
			 * + " "+ eventinfo.getEventStartTime()+
			 * ".  For more details check out this link: " ,
			 * eventinfo.getEventShareURL()); // AlertDialog.Builder dlgBldr =
			 * new AlertDialog.Builder(activity); //
			 * dlgBldr.setTitle("Share event via"); // dlgBldr
			 */
			break;
		case R.id.event_details_cheers_layout:
			// if(eventinfo != null){
			// if(eventinfo.getLikeUsers()==null){
			// Toast.makeText(activity, "No one cheered!...",
			// Toast.LENGTH_SHORT).show();
			// }else{
			// loadUsersList(eventinfo.getLikeUsers());
			// }
			// }
			// serviceToGetCheers("81");
			break;
		case R.id.event_details_attending_count_layout:
			/*
			 * Displays users list from event details if(eventinfo != null){
			 * if(eventinfo.getAttendingUsers()==null){ Toast.makeText(activity,
			 * "No one attends!...", Toast.LENGTH_SHORT).show(); }else{
			 * loadUsersList(eventinfo.getAttendingUsers()); } }
			 */
			serviceToGetAttendingPeople(eventId);
			break;
		case R.id.event_details_iamattending:
			if (eventinfo.getIsAttending().equalsIgnoreCase("true")) {
				// Unregistering user from the event
				serviceToSetIAMAttending(eventId,
						SharedPreferencesUtils.getUserId(), event_creator_Id,
						"0");
			} else {
				// Registering user to the event
				serviceToSetIAMAttending(eventId,
						SharedPreferencesUtils.getUserId(), event_creator_Id,
						"1");
			}

			break;

			// show add media layout on click on 'add media button' & 'text
			// displayed when there is no event media'
		case R.id.addMediaText:
		case R.id.addMediaTextLayout:
		case R.id.event_details_add_media_layout:
			if (eventinfo.getIsAttending().equalsIgnoreCase("false")) {
				Utils.showAlertDialog(activity, "Alert",
						"Unable to add event media.  Please check I am attending for this event to add media.");
			} else {
				showAddMediaDialog();
			}
			break;

		case R.id.menu_iv:
			showMenu();
			break;
		case R.id.overlay_menu_iv:
			showMenu();
			break;
		case R.id.event_det_buy_ticket_layout:
			Intent buyTicketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eventinfo.getEventShareURL()));
			startActivity(buyTicketIntent);
			break;
		}
	}

	// show menu
	public void showMenu() {
		if (mDrawerLayout.isDrawerOpen(mDrawer)) {
			mDrawerLayout.closeDrawer(mDrawer);
		} else {
			mDrawerLayout.openDrawer(mDrawer);
		}
	}

	/**
	 * Dialgo to show add media
	 */
	private void showAddMediaDialog() {
		final Dialog dialog = new Dialog(activity);
		// dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_add_media);
		dialog.setTitle("Add event media");
		dialog.setCancelable(true);
		TextView tvAddCast = (TextView) dialog.findViewById(R.id.addMedia_cast);
		TextView tvAddPhoto = (TextView) dialog
				.findViewById(R.id.addMedia_photo);
		TextView tvAddVideo = (TextView) dialog
				.findViewById(R.id.addMedia_video);
		TextView tvCancel = (TextView) dialog
				.findViewById(R.id.addMedia_cancel);
		tvAddCast.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// UIHelperUtil.showToastMessage("Yet to implement..!");
				Fragment activeFragment = AddACastFragmentFromHome
						.newInstance(activity);
				Bundle bundle = new Bundle();
				// TODO change this used id.
				// TODO change this used id.
				bundle.putString(Constants.KEY_USER_ID,
						SharedPreferencesUtils.getUserId());
				bundle.putString(Constants.KEY_EVENT_ID, eventId);
				bundle.putString(Constants.KEY_EVENT_TYPE, eventTitle);
				bundle.putString(Constants.KEY_EVENT_PIC, eventPic);
				bundle.putBoolean(Constants.KEY_IS_EVENT_DETAILS, true);
				activeFragment.setArguments(bundle);
				// Adds the event-detailed fragment on to 'events fragment'
				if (activity != null) {
					displayOnActivity(activeFragment);
				}
				dialog.dismiss();
			}
		});
		tvAddPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// UIHelperUtil.showToastMessage("Yet to implement..!");
				Fragment activeFragment = PhotosFragmentsFromHome
						.newInstance(activity);
				Bundle bundle = new Bundle();
				// TODO change this used id.
				// TODO change this used id.
				bundle.putString(Constants.KEY_USER_ID,
						SharedPreferencesUtils.getUserId());
				bundle.putString(Constants.KEY_EVENT_ID, eventId);
				bundle.putString(Constants.KEY_EVENT_TYPE, eventTitle);
				bundle.putString(Constants.KEY_EVENT_PIC, eventPic);
				bundle.putBoolean(Constants.KEY_IS_EVENT_DETAILS, true);
				activeFragment.setArguments(bundle);
				// Adds the event-detailed fragment on to 'events fragment'
				if (activity != null) {
					displayOnActivity(activeFragment);
				}
				dialog.dismiss();
			}
		});
		tvAddVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// UIHelperUtil.showToastMessage("Yet to implement..!");
				Fragment activeFragment = VideosFragmentsFromHome
						.newInstance(activity);
				Bundle bundle = new Bundle();
				// TODO change this used id.
				// TODO change this used id.
				bundle.putString(Constants.KEY_USER_ID,
						SharedPreferencesUtils.getUserId());
				bundle.putString(Constants.KEY_EVENT_ID, eventId);
				bundle.putString(Constants.KEY_EVENT_TYPE, eventTitle);
				// bundle.putString(Constants.KEY_EVENT_PIC, eventPic);
				bundle.putBoolean(Constants.KEY_IS_EVENT_DETAILS, true);
				activeFragment.setArguments(bundle);
				// Adds the event-detailed fragment on to 'events fragment'
				if (activity != null) {
					displayOnActivity(activeFragment);
				}
				dialog.dismiss();
			}
		});
		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	/**
	 * Method: loadUsersList Description: Displays the input users in a list in
	 * a dialog Input: users list
	 * */
	private void loadUsersList(List<UserProfileInfo> usersList) {

		UsersListAdapter adapter = new UsersListAdapter(activity,
				R.layout.users_list, usersList);
		final Dialog dialog = new Dialog(activity);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// dialog.
		dialog.setContentView(R.layout.dialog_list);
		final ListView lvList = (ListView) dialog
				.findViewById(R.id.dialog_common_list);
		final Button btnOk = (Button) dialog.findViewById(R.id.dialog_btn_ok);
		lvList.setAdapter(adapter);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		lvList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				dialog.dismiss();
				UserProfileInfo tmpObj = (UserProfileInfo) (((UsersListAdapter.Holder) (v
						.getTag())).getUser());
				displayUserProfile(tmpObj);
			}
		});
		dialog.show();
	}

	// Display user profile
	public void displayUserProfile(UserProfileInfo tmp1) {
		if (tmp1 != null) {
			String userId = tmp1.getUserID();
			UserProfileFragment activeFragment = new UserProfileFragment();
			Bundle arg = new Bundle();
			arg.putString(Constants.dataReceive, userId);
			activeFragment.setArguments(arg);
			if (activity != null) {
				displayOnActivity(activeFragment);
			}
		}
	}

	/**
	 * method call to see the list of people who cheered for an event
	 */
	private void serviceToGetCheers(String eventId) {
		final String cheersUrl = Constants.common_url
				+ getResources().getString(R.string.cheers_for_event);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("event_id", eventId));
		final ArrayList<String> userList = new ArrayList<String>();
		JsonParser.callBackgroundService(cheersUrl, nameValuePairs,
				new JsonServiceListener() {

			@Override
			public void parseJsonResponse(String jsonResponse) {
				System.out.println("cheersURL: " + cheersUrl
						+ " JSON: " + jsonResponse);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(jsonResponse);
					JSONObject resObj = jsonObject
							.getJSONObject(Constants.TAG_RESPONSE);
					String resStr = resObj
							.getString(Constants.TAG_RESPONSE_INFO);
					if (resStr != null
							&& resStr
							.equalsIgnoreCase(Constants.TAG_SUCCESS)) {
						JSONArray userCount = resObj
								.getJSONArray(Constants.KEY_USER_DETAILS);
						for (int i = 0; i < userCount.length(); i++) {
							JSONObject userObj = userCount
									.getJSONObject(i);
							String fName = userObj
									.getString(Constants.KEY_USER_FNAME);
							String lName = userObj
									.getString(Constants.KEY_USER_LNAME);
							String userId = userObj
									.getString(Constants.KEY_USER_USERID);

							userList.add(fName + " " + lName);
						}
						setListWithAdapter(userList);

					} else {
						Log.v("", "Response is failed");
						UIHelperUtil.showToastMessage("No users");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * method to get list of people who are attending an event
	 */
	private void serviceToGetAttendingPeople(String eventId) {
		final String attendingUrl = Constants.common_url
				+ getResources().getString(R.string.attending_for_event);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("event_id", eventId));
		final ArrayList<String> userList = new ArrayList<String>();
		JsonParser.callBackgroundService(attendingUrl, nameValuePairs,
				new JsonServiceListener() {

			@Override
			public void parseJsonResponse(String jsonResponse) {
				System.out.println("attendingURL: " + attendingUrl
						+ " JSON: " + jsonResponse);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(jsonResponse);
					JSONObject resObj = jsonObject
							.getJSONObject(Constants.TAG_RESPONSE);
					String resStr = resObj
							.getString(Constants.TAG_RESPONSE_INFO);
					if (resStr != null
							&& resStr
							.equalsIgnoreCase(Constants.TAG_SUCCESS)) {
						JSONArray likedUsersJSONArray = resObj
								.getJSONArray(Constants.KEY_USER_ATTENDEES_DETAILS);
						ArrayList<UserProfileInfo> likedUsers = new ArrayList<UserProfileInfo>();
						for (int i = 0; i < likedUsersJSONArray
								.length(); i++) {
							JSONObject likedUsersObj = likedUsersJSONArray
									.getJSONObject(i);
							UserProfileInfo tmpUser = new UserProfileInfo();
							tmpUser.setFirstName(likedUsersObj
									.getString(Constants.TAG_EVENT_FIRST_NAME));
							tmpUser.setUserID(likedUsersObj
									.getString(Constants.TAG_EVENT_USER_ID));
							tmpUser.setPhoto(likedUsersObj
									.getString(Constants.TAG_EVENT_PROFILE_IMG));
							likedUsers.add(tmpUser);
						}
						loadUsersList(likedUsers);

					} else {
						Log.v("", "Response is failed");
						UIHelperUtil.showToastMessage("No users");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	protected void setListWithAdapter(ArrayList<String> userList) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_list_item_1, userList);
		final Dialog dialog = new Dialog(activity);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_list);
		final ListView lvList = (ListView) dialog
				.findViewById(R.id.dialog_common_list);
		final Button btnOk = (Button) dialog.findViewById(R.id.dialog_btn_ok);
		lvList.setAdapter(adapter);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * method to set i am attending for an event
	 */
	private void serviceToSetIAMAttending(String eventId, String userId,
			String eventCreatorId, final String status) {
		final String iamAttendingUrl = Constants.common_url
				+ getResources().getString(R.string.iamattending);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("event_id", eventId));
		nameValuePairs.add(new BasicNameValuePair("user_id", userId));
		nameValuePairs.add(new BasicNameValuePair("status", status)); // status:
		// 1->true;
		// 0->
		// false
		nameValuePairs.add(new BasicNameValuePair("event_creator_id",
				eventCreatorId));

		Log.d(Constants.logUrl, "IAm Attending URL: " + iamAttendingUrl
				+ " request: event_id" + eventId + "user_id" + userId
				+ "status" + status + "event_creator_id" + eventCreatorId);
		JsonParser.callBackgroundService(iamAttendingUrl, nameValuePairs,
				new JsonServiceListener() {

			@Override
			public void parseJsonResponse(String jsonResponse) {
				if (jsonResponse != null) {
					Log.d(Constants.logUrl, "IAm Attending URL: "
							+ iamAttendingUrl + " JSON: "
							+ jsonResponse);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject
								.getJSONObject(Constants.TAG_RESPONSE);
						String resStr = resObj
								.getString(Constants.TAG_RESPONSE_INFO);
						if (resStr != null
								&& resStr
								.equalsIgnoreCase(Constants.TAG_SUCCESS)) {
							// UIHelperUtil.showToastMessage("Succesfully updated..");
							// imgIamAttending.setImageResource(R.drawable.event_details_iamattending);
							if (status.equalsIgnoreCase("1")) {
								eventinfo.setIsAttending("true");
								eventinfo.incAttendingCount();
							} else {
								eventinfo.setIsAttending("false");
								eventinfo.decAttendingCount();
							}
							setDetailsPage(eventinfo);
						} else if (resStr != null
								&& resStr
								.equalsIgnoreCase(Constants.TAG_FAILURE)) {
							UIHelperUtil
							.showToastMessage("Unable to update!....");
						} else {
//							UIHelperUtil.showToastMessage("Server error..!");
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				} else {
					UIHelperUtil
					.showToastMessage("Something went wrong with internet.....");
				}
			}
		});
	}

	/**
	 * method to update event cheer status
	 */
	private void serviceToUpdateEventCheer(String eventId,
			String eventCreatorId, final String status) {
		final String iamAttendingUrl = Constants.common_url
				+ getResources().getString(R.string.update_event_cheer);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("event_id", eventId));
		nameValuePairs.add(new BasicNameValuePair("user_id",
				SharedPreferencesUtils.getUserId()));
		nameValuePairs
		.add(new BasicNameValuePair("pn_user_id", eventCreatorId));
		nameValuePairs.add(new BasicNameValuePair("like_status", status));
		JsonParser.callBackgroundService(iamAttendingUrl, nameValuePairs,
				new JsonServiceListener() {

			@Override
			public void parseJsonResponse(String jsonResponse) {
				if (jsonResponse != null) {
					System.out.println("Updating event cheer: "
							+ iamAttendingUrl + " JSON: "
							+ jsonResponse);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject
								.getJSONObject(Constants.TAG_RESPONSE);
						String resStr = resObj
								.getString(Constants.TAG_RESPONSE_INFO);
						if (resStr != null
								&& resStr
								.equalsIgnoreCase(Constants.TAG_SUCCESS)) {
							if (status.equalsIgnoreCase("true")) {
								eventinfo.incEventLikes();
							} else {
								eventinfo.decEventLikes();
							}

							eventinfo.setIsUserLikedEvent(status);
							setDetailsPage(eventinfo);
						} else if (resStr != null
								&& resStr
								.equalsIgnoreCase(Constants.TAG_FAILURE)) {
							UIHelperUtil
							.showToastMessage("Something went wrong with server.....");
						} else {
//							UIHelperUtil.showToastMessage("Server error..!");
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
					//
				} else {
					UIHelperUtil
					.showToastMessage("Something went wrong with internet.....");
				}
			}
		});
	}

	/**
	 * List of details about a event
	 * 
	 * @param eventDataMap
	 */
	private void setEventDetailsAdapter(
			HashMap<String, ArrayList<EventInfo.EventMedia>> eventDataMap) {
		if (eventDataMap == null) {
			adapter = new EventDetailsAdapter(activity, null,eventTitle);
			lvEventDetails.setAdapter(adapter);
		} else {
			adapter = new EventDetailsAdapter(activity, eventDataMap,eventTitle);
			lvEventDetails.setAdapter(adapter);
		}
	}

	// service to get event info
	private void serviceToGetEventDetailsInfo(String userId, String eventId, String filterBy) {
		final String url = Constants.common_url
				+ getString(R.string.get_event_details);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// TODO
		nameValuePairs.add(new BasicNameValuePair("user_id", userId));
		nameValuePairs.add(new BasicNameValuePair("event_id", eventId));
		nameValuePairs.add(new BasicNameValuePair("user_lat", Constants.lati));
		nameValuePairs.add(new BasicNameValuePair("user_lng", Constants.longi));
		nameValuePairs.add(new BasicNameValuePair("login_user_id", SharedPreferencesUtils.getUserId()));
		nameValuePairs.add(new BasicNameValuePair("filter_by", filterBy));
		Log.i("", "LATI: " + Constants.lati + " , " + Constants.longi);
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
				if (jsonResponse != null) {
					Log.v("", "URL: " + url
							+ " event details RESPONSE: "
							+ jsonResponse);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject
								.getJSONObject(Constants.TAG_RESPONSE);
						String responseStr = resObj
								.getString(Constants.TAG_RESPONSE_INFO);
						List<TeamInfo> myteamList = new ArrayList<TeamInfo>();
						if (responseStr != null
								&& responseStr
								.equalsIgnoreCase(Constants.TAG_SUCCESS)) {

							JSONObject eventDetailsObj = resObj
									.getJSONObject(Constants.TAG_EVENT_DETAILS);
							eventinfo = new EventInfo();
							eventinfo.setEventId(eventDetailsObj
									.getString(Constants.TAG_EVENT_ID));
							eventinfo.setCreatorId(eventDetailsObj
									.getString(Constants.TAG_EVENT_CREATOR_ID));
							event_creator_Id = eventDetailsObj
									.getString(Constants.TAG_EVENT_CREATOR_ID);
							eventinfo
							.setSportName(eventDetailsObj
									.getString(
											Constants.TAG_EVENT_SPORT_NAME)
											.toUpperCase());
							eventinfo.setEventTitle(eventDetailsObj
									.getString(
											Constants.TAG_EVENT_TITLE)
											.toUpperCase());
							eventinfo
							.setEventDescription(eventDetailsObj
									.getString(Constants.TAG_EVENT_DESCRIPTION));
							eventinfo.setTeam1Id(eventDetailsObj
									.getString(Constants.TAG_EVENT_TEAM1_ID));
							eventinfo.setTeam1Type(eventDetailsObj
									.getString(Constants.TAG_EVENT_TEAM1_TYPE));
							eventinfo.setTeam2Id(eventDetailsObj
									.getString(Constants.TAG_EVENT_TEAM2_ID));
							eventinfo.setTeam2Type(eventDetailsObj
									.getString(Constants.TAG_EVENT_TEAM2_TYPE));
							eventinfo.setEventStartDate(eventDetailsObj
									.getString(Constants.TAG_EVENT_START_DATE));
							eventinfo.setEventStartTime(eventDetailsObj
									.getString(Constants.TAG_EVENT_START_TIME));
							eventinfo.setEventEndTime(eventDetailsObj
									.getString(Constants.TAG_EVENT_END_TIME));
							if(eventDetailsObj
									.getString("is_stubhub_event").equalsIgnoreCase("yes")){
								eventinfo.setStubhubEvent(true);
							}else{
								eventinfo.setStubhubEvent(false);
							}
							eventinfo
							.setEventSubLocation(eventDetailsObj
									.getString(Constants.TAG_EVENT_SUB_LOCATION));
							eventinfo.setEventAddress(eventDetailsObj
									.getString(Constants.TAG_EVENT_FORMATTED_ADDRESS));
							eventinfo
							.setEventDistanceFromUserLoc(eventDetailsObj
									.getString(Constants.TAG_EVENT_DISTANCE_FROM_USER_LOCATION));
							eventinfo.setEventLikes(eventDetailsObj
									.getString(Constants.TAG_EVENT_LIKES));
							if (eventDetailsObj.getString(
									Constants.TAG_EVENT_IS_LIKED_EVENT)
									.equalsIgnoreCase("1")) {
								eventinfo.setIsUserLikedEvent("true");
							} else {
								eventinfo.setIsUserLikedEvent("false");
							}
							eventinfo.setLat(eventDetailsObj
									.getString(Constants.TAG_EVENT_LAT));
							eventinfo.setLng(eventDetailsObj
									.getString(Constants.TAG_EVENT_LNG));

							try {
								// Fetching liked users list
								JSONArray likedUsersJSONArray = eventDetailsObj
										.getJSONArray(Constants.TAG_EVENT_LIKE_USERS);
								ArrayList<UserProfileInfo> likedUsers = new ArrayList<UserProfileInfo>();
								for (int i = 0; i < likedUsersJSONArray
										.length(); i++) {
									JSONObject likedUsersObj = likedUsersJSONArray
											.getJSONObject(i);
									UserProfileInfo tmpUser = new UserProfileInfo();
									tmpUser.setFirstName(likedUsersObj
											.getString(Constants.TAG_EVENT_FIRST_NAME));
									tmpUser.setUserID(likedUsersObj
											.getString(Constants.TAG_EVENT_USER_ID));
									tmpUser.setPhoto(likedUsersObj
											.getString(Constants.TAG_EVENT_PROFILE_IMG));
									likedUsers.add(tmpUser);
								}
								eventinfo.setLikeUsers(likedUsers);
							} catch (JSONException ex) {
								// When no user exist
							}

							eventinfo
							.setEventType(eventDetailsObj
									.getString(
											Constants.TAG_EVENT_TYPE)
											.toUpperCase());
							eventinfo.setIsAttending(eventDetailsObj
									.getString(Constants.TAG_EVENT_IS_ATTENDIONG));
							eventinfo.setEventImageUrl(eventDetailsObj
									.getString(Constants.TAG_EVENT_IMAGE));
							eventinfo.setAttendingCount(eventDetailsObj
									.getString(Constants.TAG_EVENT_ATTENDING_CNT));
							
							eventinfo.setEventShareURL(eventDetailsObj
									.getString(Constants.TAG_EVENT_URL));

							// checking whether media data available or
							// not for this event
							setDetailsPage(eventinfo);
							if (resObj
									.getString(Constants.TAG_EVENT_MEDIA) != null
									&& !resObj
									.getString(
											Constants.TAG_EVENT_MEDIA)
											.equalsIgnoreCase(
													Constants.TAG_EVENT_MEDIA_NO_DATA)) {
								HashMap<String, ArrayList<EventInfo.EventMedia>> eventDataMap = getEventMediaData(
										resObj, eventinfo);
								eventinfo
								.setEventMediaList(eventDataMap);
								setEventDetailsAdapter(eventDataMap);
								
							} else {
								UIHelperUtil
								.showToastMessage("No event media data");
								setEventDetailsAdapter(null);
							}
							AddMediaUI_to_list();

						} else if (responseStr != null
								&& responseStr
								.equalsIgnoreCase(Constants.TAG_FAILURE)) {
							UIHelperUtil
							.showToastMessage("No event found");
						} else {
							UIHelperUtil
							.showToastMessage(getString(R.string.service_toast));
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					UIHelperUtil
					.showToastMessage(getString(R.string.service_toast));
				}
			}
		});
	}

	/**
	 * This method will return all the event media list with comments
	 * 
	 * @param resObj
	 * @param eventinfo
	 * @return
	 * @throws JSONException
	 */
	private HashMap<String, ArrayList<EventInfo.EventMedia>> getEventMediaData(
			JSONObject resObj, EventInfo eventinfo) throws JSONException {
		HashMap<String, ArrayList<EventInfo.EventMedia>> eventMediaMap = new HashMap<String, ArrayList<EventInfo.EventMedia>>();
		// Log.d("", "MediaData: " +
		// resObj.getString(Constants.TAG_EVENT_MEDIA));
		JSONObject eventMediaObj = resObj
				.getJSONObject(Constants.TAG_EVENT_MEDIA);

		// checking whether cast data available for this media or not
		if (eventMediaObj.has(Constants.TAG_EVENT_MEDIA_CAST_DATA) && eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_CAST_DATA) != null
				&& !eventMediaObj
				.getString(Constants.TAG_EVENT_MEDIA_CAST_DATA)
				.equalsIgnoreCase(
						Constants.TAG_EVENT_MEDIA_NO_MEDIA_DATA)) {
			// Log.d("", "MediaCastData: " +
			// eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_CAST_DATA));
			try {
				/*JSONObject castDataObjs = eventMediaObj
						.getJSONObject(Constants.TAG_EVENT_MEDIA_CAST_DATA);*/
				ArrayList<EventInfo.EventMedia> castList = new ArrayList<EventInfo.EventMedia>();
				//old data
				/*int count = castDataObjs
						.getInt(Constants.TAG_EVENT_MEDIA_CAST_COUNT);*/
				JSONArray mediaCastsJsonArr = eventMediaObj.getJSONArray(Constants.TAG_EVENT_MEDIA_CAST_DATA);
				for (int i = 0; i < mediaCastsJsonArr.length(); i++) {
					//old data
					/*JSONObject castDataObj = castDataObjs.getJSONObject(String
							.valueOf(i));*/
					
					EventInfo.EventMedia eventMedia = eventinfo.new EventMedia();
					eventMedia.setMediaType(Constants.KEYs[3]);
					eventMedia
					.setMediaId(mediaCastsJsonArr.getJSONObject(i).getString(Constants.TAG_EVENT_MEDIA_CAST_MEDIA_ID));
					eventMedia.setMediaUserId(mediaCastsJsonArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_USER_ID));
					eventMedia
					.setMediaFirstName(mediaCastsJsonArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_FIRST_NAME));
					eventMedia
					.setMediaProfileImage(mediaCastsJsonArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_PROFILE_IMAGE));
					eventMedia
					.setMediaData(mediaCastsJsonArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_MEDIA_DATA));
					eventMedia
					.setMediaDateCreated(mediaCastsJsonArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_DATE_CREATED));
					eventMedia.setMediaLikes(mediaCastsJsonArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_LIKE_CNT));
					eventMedia.setCommentsCnt(mediaCastsJsonArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_COMMENT_CNT));
					if (mediaCastsJsonArr.getJSONObject(i).getString(
							Constants.TAG_EVENT_MEDIA_IS_LIKED_EVENT)
							.equalsIgnoreCase("1")) {
						eventMedia.setIsUserLiked("true");
					} else {
						eventMedia.setIsUserLiked("false");
					}

					// TODO: share event-media via social media (Also need to
					// add event-media share link in web service)
					castList.add(eventMedia);
				}
				
				eventMediaMap.put(Constants.KEYs[3], castList);
			} catch (Exception e) {
				// when no cast data exist
				e.printStackTrace();
			}
			// commentsMap.put(, value)

		}

		// checking whether images data available for this media or not
		if (eventMediaObj.has(Constants.TAG_EVENT_MEDIA_PHOTO_DATA) && eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_PHOTO_DATA) != null
				&& !eventMediaObj.getString(
						Constants.TAG_EVENT_MEDIA_PHOTO_DATA).equalsIgnoreCase(
								Constants.TAG_EVENT_MEDIA_NO_MEDIA_DATA)) {
			// Log.d("", "MediaPhotoData: " +
			// eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_PHOTO_DATA));
			try {
				//old data by bhavani
				/*JSONObject photoDataObj = eventMediaObj
						.getJSONObject(Constants.TAG_EVENT_MEDIA_PHOTO_DATA);*/
				JSONArray photoDataObjArr = eventMediaObj.getJSONArray(Constants.TAG_EVENT_MEDIA_PHOTO_DATA);
				ArrayList<EventInfo.EventMedia> photoList = new ArrayList<EventInfo.EventMedia>();
				//old data
				/*int count = photoDataObj
						.getInt(Constants.TAG_EVENT_MEDIA_PHOTO_COUNT);*/
				for (int i = 0; i < photoDataObjArr.length(); i++) {
					//old data
					/*JSONObject firstPhotoObj = photoDataObj
							.getJSONObject(String.valueOf(i));*/
					EventInfo.EventMedia eventMedia = eventinfo.new EventMedia();
					// This is the key for all media objects.
					String mediaId = photoDataObjArr.getJSONObject(i).getString(Constants.TAG_EVENT_MEDIA_CAST_MEDIA_ID);
					eventMedia.setMediaType(Constants.KEYs[1]);
					eventMedia.setMediaId(mediaId);
					eventMedia.setMediaUserId(photoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_USER_ID));
					eventMedia
					.setMediaFirstName(photoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_FIRST_NAME));
					eventMedia
					.setMediaProfileImage(photoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_PROFILE_IMAGE));
					eventMedia.setMediaPhotoUrl(photoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_PHOTO_URL));
					eventMedia
					.setMediaDateCreated(photoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_DATE_CREATED));
					eventMedia.setMediaLikes(photoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_LIKE_CNT));
					eventMedia.setCommentsCnt(photoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_COMMENT_CNT));
					if(photoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_LATEST_COMMENT) != null && photoDataObjArr.getJSONObject(i).getString(Constants.TAG_EVENT_MEDIA_LATEST_COMMENT).equalsIgnoreCase("no comment")){
						eventMedia.setLatestComment(photoDataObjArr.getJSONObject(i).getString(Constants.TAG_EVENT_MEDIA_LATEST_COMMENT));
					}else{
						JSONObject latestCommentObj = photoDataObjArr.getJSONObject(i).getJSONObject(Constants.TAG_EVENT_MEDIA_LATEST_COMMENT);
						eventMedia.setLatestComment(latestCommentObj.getString("latest_comment"));
						eventMedia.setLatestCommentedBy(latestCommentObj.getString("latest_commented_by"));
					}
					
					if (photoDataObjArr.getJSONObject(i).getString(
							Constants.TAG_EVENT_MEDIA_IS_LIKED_EVENT)
							.equalsIgnoreCase("1")) {
						eventMedia.setIsUserLiked("true");
					} else {
						eventMedia.setIsUserLiked("false");
					}

					// the eventMedia onject is added to parent arraqyList which
					// contains all medias.
					photoList.add(eventMedia);
				}
				// the videos(arraylist of all media i.e. videos) are added to
				// this parent hashmap.
				eventMediaMap.put(Constants.KEYs[1], photoList);
			} catch (Exception e) {
				// when no photos are posted
				e.printStackTrace();
			}

		}

		// checking whether video data available for this media or not
		if (eventMediaObj.has(Constants.TAG_EVENT_MEDIA_VIDEO_DATA) && eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_VIDEO_DATA) != null
				&& !eventMediaObj.getString(
						Constants.TAG_EVENT_MEDIA_VIDEO_DATA).equalsIgnoreCase(
								Constants.TAG_EVENT_MEDIA_NO_MEDIA_DATA)) {
			// Log.d("", "MediaVideoData: " +
			// eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_VIDEO_DATA));
			try {
				//old data
				/*JSONObject videoDataObj = eventMediaObj
						.getJSONObject(Constants.TAG_EVENT_MEDIA_VIDEO_DATA);*/
				JSONArray videoDataObjArr = eventMediaObj
						.getJSONArray(Constants.TAG_EVENT_MEDIA_VIDEO_DATA);
				ArrayList<EventInfo.EventMedia> videoList = new ArrayList<EventInfo.EventMedia>();
				//old
				/*int count = videoDataObj
						.getInt(Constants.TAG_EVENT_MEDIA_VIDEO_COUNT);*/
				for (int i = 0; i < videoDataObjArr.length(); i++) {
					/*JSONObject firstvideoObj = videoDataObj
							.getJSONObject(String.valueOf(i));*/
					EventInfo.EventMedia eventMedia = eventinfo.new EventMedia();

					// key for hashmap of comments based on mediaId.
					String mediaId = videoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_MEDIA_ID);
					eventMedia.setMediaType(Constants.KEYs[2]);
					eventMedia.setMediaId(mediaId);
					eventMedia.setMediaUserId(videoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_USER_ID));
					eventMedia
					.setMediaFirstName(videoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_FIRST_NAME));
					eventMedia
					.setMediaProfileImage(videoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_PROFILE_IMAGE));
					eventMedia.setMediaPhotoUrl(videoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_VIDEO_URL));
					eventMedia
					.setMediaDateCreated(videoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_CAST_DATE_CREATED));
					eventMedia.setMediaThumbNail(videoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_VIDEO_THUMBNAIL_URL));
					eventMedia.setMediaLikes(videoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_LIKE_CNT));
					eventMedia.setCommentsCnt(videoDataObjArr.getJSONObject(i)
							.getString(Constants.TAG_EVENT_MEDIA_COMMENT_CNT));
					if (videoDataObjArr.getJSONObject(i).getString(
							Constants.TAG_EVENT_MEDIA_IS_LIKED_EVENT)
							.equalsIgnoreCase("1")) {
						eventMedia.setIsUserLiked("true");
					} else {
						eventMedia.setIsUserLiked("false");
					}
					// the eventMedia onject is added to parent arraqyList which
					// contains all medias.
					videoList.add(eventMedia);
				}
				// the videos(arraylist of all media i.e. videos) are added to
				// this parent hashmap.
				eventMediaMap.put(Constants.KEYs[2], videoList);
			} catch (Exception e) {
				// when no videos are posted
				e.printStackTrace();
			}
		}
		
		
		// checking whether all data available for this media or not
				if (eventMediaObj.has(Constants.TAG_EVENT_ALL_DATA) && eventMediaObj.getString(Constants.TAG_EVENT_ALL_DATA) != null
						&& !eventMediaObj.getString(
								Constants.TAG_EVENT_ALL_DATA).equalsIgnoreCase(
										Constants.TAG_EVENT_MEDIA_NO_MEDIA_DATA)) {
					// Log.d("", "MediaVideoData: " +
					// eventMediaObj.getString(Constants.TAG_EVENT_MEDIA_VIDEO_DATA));
					try {
						//old data
						/*JSONObject videoDataObj = eventMediaObj
								.getJSONObject(Constants.TAG_EVENT_MEDIA_VIDEO_DATA);*/
						JSONArray allDataObjArr = eventMediaObj
								.getJSONArray(Constants.TAG_EVENT_ALL_DATA);
						ArrayList<EventInfo.EventMedia> videoList = new ArrayList<EventInfo.EventMedia>();
						//old
						/*int count = videoDataObj
								.getInt(Constants.TAG_EVENT_MEDIA_VIDEO_COUNT);*/
						for (int i = 0; i < allDataObjArr.length(); i++) {
							/*JSONObject firstvideoObj = videoDataObj
									.getJSONObject(String.valueOf(i));*/
							EventInfo.EventMedia eventMedia = eventinfo.new EventMedia();

							// key for hashmap of comments based on mediaId.
							String mediaId = allDataObjArr.getJSONObject(i)
									.getString(Constants.TAG_EVENT_MEDIA_CAST_MEDIA_ID);
							
							if(allDataObjArr.getJSONObject(i)
									.getString(Constants.TAG_EVENT_MEDIA_TYPE).equalsIgnoreCase(Constants.KEYs[1])){
								eventMedia.setMediaType(Constants.KEYs[1]);
								eventMedia.setMediaPhotoUrl(allDataObjArr.getJSONObject(i)
										.getString(Constants.TAG_EVENT_MEDIA_PHOTO_URL));
								if(allDataObjArr.getJSONObject(i)
										.getString(Constants.TAG_EVENT_MEDIA_LATEST_COMMENT) != null && allDataObjArr.getJSONObject(i).getString(Constants.TAG_EVENT_MEDIA_LATEST_COMMENT).equalsIgnoreCase("no comment")){
									eventMedia.setLatestComment(allDataObjArr.getJSONObject(i).getString(Constants.TAG_EVENT_MEDIA_LATEST_COMMENT));
								}else{
									JSONObject latestCommentObj = allDataObjArr.getJSONObject(i).getJSONObject(Constants.TAG_EVENT_MEDIA_LATEST_COMMENT);
									eventMedia.setLatestComment(latestCommentObj.getString("latest_comment"));
									eventMedia.setLatestCommentedBy(latestCommentObj.getString("latest_commented_by"));
								}
							}else if(allDataObjArr.getJSONObject(i)
									.getString(Constants.TAG_EVENT_MEDIA_TYPE).equalsIgnoreCase(Constants.KEYs[2])){
								eventMedia.setMediaType(Constants.KEYs[2]);
								eventMedia.setMediaPhotoUrl(allDataObjArr.getJSONObject(i)
										.getString(Constants.TAG_EVENT_MEDIA_VIDEO_URL));
								eventMedia.setMediaThumbNail(allDataObjArr.getJSONObject(i)
										.getString(Constants.TAG_EVENT_MEDIA_VIDEO_THUMBNAIL_URL));
							}else if(allDataObjArr.getJSONObject(i)
									.getString(Constants.TAG_EVENT_MEDIA_TYPE).equalsIgnoreCase(Constants.KEYs[3])){
								eventMedia.setMediaType(Constants.KEYs[3]);
								eventMedia.setMediaData(allDataObjArr.getJSONObject(i)
										.getString(Constants.TAG_EVENT_MEDIA_CAST_MEDIA_DATA));
								
							}
							
							eventMedia.setMediaId(mediaId);
							eventMedia.setMediaUserId(allDataObjArr.getJSONObject(i)
									.getString(Constants.TAG_EVENT_MEDIA_CAST_USER_ID));
							eventMedia
							.setMediaFirstName(allDataObjArr.getJSONObject(i)
									.getString(Constants.TAG_EVENT_MEDIA_CAST_FIRST_NAME));
							eventMedia
							.setMediaProfileImage(allDataObjArr.getJSONObject(i)
									.getString(Constants.TAG_EVENT_MEDIA_CAST_PROFILE_IMAGE));
							eventMedia
							.setMediaDateCreated(allDataObjArr.getJSONObject(i)
									.getString(Constants.TAG_EVENT_MEDIA_CAST_DATE_CREATED));
							eventMedia.setMediaLikes(allDataObjArr.getJSONObject(i)
									.getString(Constants.TAG_EVENT_MEDIA_LIKE_CNT));
							eventMedia.setCommentsCnt(allDataObjArr.getJSONObject(i)
									.getString(Constants.TAG_EVENT_MEDIA_COMMENT_CNT));
							if (allDataObjArr.getJSONObject(i).getString(
									Constants.TAG_EVENT_MEDIA_IS_LIKED_EVENT)
									.equalsIgnoreCase("1")) {
								eventMedia.setIsUserLiked("true");
							} else {
								eventMedia.setIsUserLiked("false");
							}

							// the eventMedia onject is added to parent arraqyList which
							// contains all medias.
							videoList.add(eventMedia);
						}
						// the videos(arraylist of all media i.e. videos) are added to
						// this parent hashmap.
						eventMediaMap.put(Constants.KEYs[0], videoList);
					} catch (Exception e) {
						// when no videos are posted
						e.printStackTrace();
					}
				}
				
		// Log.d("", "size: "+ eventMediaMap.size());
		return eventMediaMap;
	}

	private com.facebook.Session.StatusCallback statusCallback = new StatusCallback() {

		@Override
		public void call(com.facebook.Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				session.removeCallback(statusCallback);
				//				shareOnFacebook();
				Log.i("", "Session Facebook is opened: ");

			} else {
				Log.i("", "Session Facebook is closed");
			}
		}
	};

	/**
	 * Dialgo to show share dialog
	 */
	private void showShareDialog() {
		final Dialog dialog = new Dialog(activity);
		// dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_share);
		dialog.setTitle("Share event via");
		dialog.setCancelable(true);
		Button btnFb = (Button) dialog.findViewById(R.id.fb_btn);
		btnFb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//				UIHelper.showToastMessage(DetailsReviewsActivity.this, "you click share button");
				if (Utils.chkStatus()) {
					/*if (SocialNetworkingUtils.isFacebookExist(activity)) {
						SocialNetworkingUtils.onClickPostStatusUpdate(activity,eventinfo.getSportName()+" "+eventinfo.getEventType()+" \""+eventinfo.getEventTitle()+"\" is going at "+ eventinfo.getEventAddress()+" on "+  eventinfo.getEventStartDate() + " "+ eventinfo.getEventStartTime()+".  For more details check out this link: ",eventinfo.getEventShareURL(),eventinfo.getEventImageUrl());	
						Log.d("Tag", "Success! " + "existed");
					} else {*/	
					Session session = Session.getActiveSession();
					if (session != null && session.isOpened()) {
						Log.d("Tag", "Success! " + "if");
						if(MySportsApp.isFacebookSdkSupporting){
							SocialNetworkingUtils.publishFeedExample(eventinfo.getSportName()+" "+eventinfo.getEventType()+" \""+eventinfo.getEventTitle()+"\" is going at "+ eventinfo.getEventAddress()+" on "+  eventinfo.getEventStartDate() + " "+ eventinfo.getEventStartTime()+".  For more details check out this link: ",eventinfo.getEventShareURL(),eventinfo.getEventImageUrl(),activity,SocialNetworkingUtils.mSimpleFacebook);
						}else{
							SocialNetworkingUtils.publishFeedDialog(eventinfo.getSportName()+" "+eventinfo.getEventType()+" \""+eventinfo.getEventTitle()+"\" is going at "+ eventinfo.getEventAddress()+" on "+  eventinfo.getEventStartDate() + " "+ eventinfo.getEventStartTime()+".  For more details check out this link: ",eventinfo.getEventShareURL(),eventinfo.getEventImageUrl(),activity);
						}

					} else {
						Log.d("Tag", "Session! " + "else");
						Session.openActiveSession(activity, true, statusCallback);
						SocialNetworkingUtils.isFirstTimeCalled = true;
						Log.d("", "isfirsttimecalled"+SocialNetworkingUtils.isFirstTimeCalled);
					}
					//					}
				} else {
					UIHelperUtil.showToastMessage(getString(R.string.toast_no_network));
				}
				//				shareOnFacebook();
				dialog.dismiss();
			}
		});
		Button btnReport = (Button) dialog.findViewById(R.id.report_btn);
		btnReport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// UIHelperUtil.showToastMessage("Yet to implement");
				Utils.reportAbuseService(activity, dialog, "ABS_EVENT",
						eventinfo.getEventId());
			}
		});
		Button btnTwitter = (Button) dialog.findViewById(R.id.twitter_btn);
		btnTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences mSharedPref = activity.getSharedPreferences(
						"Android_Twitter_Preferences", Context.MODE_PRIVATE);
				if (!Utils.chkStatus()) {
					Utils.networkAlertDialog(activity, getResources()
							.getString(R.string.toast_no_network));
				} else {
					new DownloadImageTaskForTwitter(dialog, v)
					.execute(eventinfo.getEventImageUrl());
				}

			}
		});
		Button btnInstagram = (Button) dialog.findViewById(R.id.instagram_btn);
		btnInstagram.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!Utils.chkStatus()) {
					Utils.networkAlertDialog(activity, getResources()
							.getString(R.string.toast_no_network));
				} else {
					new DownloadImageTaskForInstagram(dialog).execute(eventinfo
							.getEventImageUrl());
				}

				/*
				 * Intent intent =
				 * activity.getPackageManager().getLaunchIntentForPackage
				 * ("com.instagram.android"); Bitmap bitmap =
				 * BitmapFactory.decodeResource
				 * (getResources(),R.drawable.default_event_pic);
				 * 
				 * String root =
				 * Environment.getExternalStorageDirectory().toString(); File
				 * myDir = new File(root + "/event_images"); myDir.mkdirs();
				 * Random generator = new Random(); int n = 10000; n =
				 * generator.nextInt(n); String fname = "image-"+ n +".jpg";
				 * File file = new File (myDir, fname); if (file.exists ())
				 * file.delete (); try { FileOutputStream out = new
				 * FileOutputStream(file);
				 * bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				 * out.flush(); out.close();
				 * 
				 * } catch (Exception e) { e.printStackTrace(); } File
				 * imageFile2 =new
				 * File(Environment.getExternalStorageDirectory()
				 * ,"/event_images/"+"image-"+ n +".jpg"); String path =
				 * imageFile2.getAbsolutePath(); if (intent != null) { Intent
				 * shareIntent = new Intent();
				 * shareIntent.setAction(Intent.ACTION_SEND);
				 * shareIntent.setPackage("com.instagram.android"); try {
				 * shareIntent.putExtra(Intent.EXTRA_STREAM,
				 * Uri.parse(MediaStore
				 * .Images.Media.insertImage(activity.getContentResolver(),
				 * path, "", ""))); shareIntent.putExtra(Intent.EXTRA_TEXT,
				 * "Sub-Sample"+" "+"Sub-Sample link"); } catch (Exception e) {
				 * // TODO Auto-generated catch block e.printStackTrace(); }
				 * shareIntent.setType("image/jpeg");
				 * 
				 * startActivity(shareIntent); } else { // bring user to the
				 * market to download the app. // or let them choose an app?
				 * intent = new Intent(Intent.ACTION_VIEW);
				 * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 * intent.setData
				 * (Uri.parse("market://details?id="+"com.instagram.android"));
				 * startActivity(intent); } dialog.dismiss();
				 */
			}
		});

		dialog.show();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// SocialNetworkingUtils.mTwitter.clearSession();
		// SocialNetworkingUtils.uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		SocialNetworkingUtils.mSimpleFacebook.onActivityResult(activity, requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(activity, requestCode,
				resultCode, data);
	}
	/*private void shareOnFacebook() {
		// UIHelper.showToastMessage(DetailsReviewsActivity.this,
		// "you click share button");
		if (Utils.chkStatus()) {
			if (false && SocialNetworkingUtils.isFacebookExist(activity)) {
				Session session = Session.getActiveSession();
				if (session != null && session.isOpened()) {
					SocialNetworkingUtils
							.onClickPostStatusUpdate(
									activity,
									eventinfo.getSportName()
											+ " "
											+ eventinfo.getEventType()
											+ " \""
											+ eventinfo.getEventTitle()
											+ "\" is going at "
											+ eventinfo.getEventAddress()
											+ " on "
											+ eventinfo.getEventStartDate()
											+ " "
											+ eventinfo.getEventStartTime()
											+ ".  For more details check out this link: ",
									eventinfo.getEventShareURL(),
									eventinfo.getEventImageUrl());
				} else {

					Session.openActiveSession(activity, true, statusCallback);
				}
				Log.d("Tag", "Success! " + "existed");
			} else {
				Session session = Session.getActiveSession();
				if (session != null && session.isOpened()) {
					Log.d("Tag", "Success! " + "if");
					SocialNetworkingUtils
							.publishFeedDialog(
									eventinfo.getSportName()
											+ " "
											+ eventinfo.getEventType()
											+ " \""
											+ eventinfo.getEventTitle()
											+ "\" is going at "
											+ eventinfo.getEventAddress()
											+ " on "
											+ eventinfo.getEventStartDate()
											+ " "
											+ eventinfo.getEventStartTime()
											+ ".  For more details check out this link: ",
									eventinfo.getEventShareURL(),
									eventinfo.getEventImageUrl(), activity);
				} else {
					Log.d("Tag", "Session! " + "else");
					Session.openActiveSession(activity, true, statusCallback);
					SocialNetworkingUtils.isFirstTimeCalled = true;
					Log.d("", "isfirsttimecalled"
							+ SocialNetworkingUtils.isFirstTimeCalled);
				}
			}
		} else {
			UIHelperUtil.showToastMessage(getString(R.string.toast_no_network));
		}

	}*/

	/**
	 * AsyncTask Downloading pic from urls for twitter.
	 * 
	 * @author koti
	 * 
	 */
	private class DownloadImageTaskForTwitter extends
	AsyncTask<String, Integer, Bitmap> {
		ProgressDialog pd;
		String socialImageUrl;
		Dialog myDialog;
		View view;
		File file;

		public DownloadImageTaskForTwitter(Dialog dialog, View v) {
			myDialog = dialog;
			view = v;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}

		protected Bitmap doInBackground(String... urls) {
			socialImageUrl = urls[0];
			Bitmap bmp = BitmapUtils.downloadImage(urls[0]);
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/twitter_post_pic");
			myDir.mkdirs();
			String fname = "event_pic.jpg";
			file = new File(myDir, fname);
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bmp;
		}

		protected void onPostExecute(Bitmap result) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			if (result != null) {
				myDialog.dismiss();
				try {
					SharedPreferences mSharedPref = activity
							.getSharedPreferences(
									"Android_Twitter_Preferences",
									Context.MODE_PRIVATE);
					String[] addArr = eventinfo.getEventAddress().split(",");
					String strAddr = "";
					if (addArr.length > 1) {
						strAddr = addArr[0] + "," + addArr[1];
					}
					String[] timeArr = eventinfo.getEventStartTime().split(":");
					String amorpm = "";
					String strStartTime = "";
					if (timeArr.length > 2) {
						amorpm = timeArr[2];
						strStartTime = timeArr[0] + ":" + timeArr[1];
					}
					if (amorpm.contains("PM")) {
						amorpm = "pm";
					} else {
						amorpm = "am";
					}
					strStartTime = strStartTime + " " + amorpm;
					// InputStream inputStream =
					// v.getContext().getAssets().open("1.png");
					// Bitmap bmp =
					// BitmapFactory.decodeResource(getResources(),R.drawable.default_event_pic);
					// String filename =
					// Environment.getExternalStorageDirectory().toString() +
					// File.separator + "1.png";
					String filename = file.getAbsolutePath();
					String sharemsg = eventinfo.getSportName() + " "
							+ eventinfo.getEventType() + " \""
							+ eventinfo.getEventTitle() + "\" at " + strAddr
							+ " on " + eventinfo.getEventStartDate() + " "
							+ strStartTime + ". link: "
							+ eventinfo.getEventShareURL();
					if (mSharedPref.getString(
							Constants.KEY_TWITTER_ACCESS_TOKEN, null) != null) {
						// SocialNetworkingUtils.postingtoTwitter(v,activity,eventinfo.getSportName()+" "+eventinfo.getEventType()+" \""+eventinfo.getEventTitle()+"\" is going at "+
						// eventinfo.getEventAddress()+" on "+
						// eventinfo.getEventStartDate() + " "+
						// eventinfo.getEventStartTime()+".  For more details check out this link:   "+
						// eventinfo.getEventShareURL());
						// SocialNetworkingUtils.postingtoTwitter(v,activity,sharemsg);
						HelperMethods.postToTwitterWithImage(activity,
								((Activity) activity), filename, sharemsg,
								new TwitterCallback() {
							@Override
							public void onFinsihed(Boolean response) {
								Toast.makeText(activity,
										"posted succeded",
										Toast.LENGTH_SHORT).show();
							}
						});
					} else {
						// Toast.makeText(activity, "session is null",
						// Toast.LENGTH_SHORT).show();
						SocialNetworkingUtils.signinTwitter(view,
								SocialNetworkingUtils.mTwitter, sharemsg,
								eventinfo.getEventImageUrl(), activity);
					}
				} catch (Exception ex) {
					Toast.makeText(activity, "Please try again",
							Toast.LENGTH_SHORT).show();
				}
			} else {
			}
		}
	}

	/**
	 * AsyncTask Downloading pic from url for instagram.
	 * 
	 * @author koti
	 * 
	 */
	private class DownloadImageTaskForInstagram extends
	AsyncTask<String, Integer, Bitmap> {
		ProgressDialog pd;
		Dialog myDialog;
		File file;

		public DownloadImageTaskForInstagram(Dialog dialog) {
			myDialog = dialog;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}

		protected Bitmap doInBackground(String... urls) {
			Bitmap bmp = BitmapUtils.downloadImage(urls[0]);
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/instagram_post_pic");
			myDir.mkdirs();
			String fname = "event_pic.jpg";
			file = new File(myDir, fname);
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bmp;
		}

		protected void onPostExecute(Bitmap result) {

			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			if (result != null) {
				myDialog.dismiss();
				try {
					String[] addArr = eventinfo.getEventAddress().split(",");
					String strAddr = "";
					if (addArr.length > 1) {
						strAddr = addArr[0] + "," + addArr[1];
					}
					String[] timeArr = eventinfo.getEventStartTime().split(":");
					String amorpm = "";
					String strStartTime = "";
					if (timeArr.length > 2) {
						amorpm = timeArr[2];
						strStartTime = timeArr[0] + ":" + timeArr[1];
					}
					if (amorpm.contains("PM")) {
						amorpm = "pm";
					} else {
						amorpm = "am";
					}
					strStartTime = strStartTime + " " + amorpm;
					// InputStream inputStream =
					// v.getContext().getAssets().open("1.png");
					// Bitmap bmp =
					// BitmapFactory.decodeResource(getResources(),R.drawable.default_event_pic);
					// String filename =
					// Environment.getExternalStorageDirectory().toString() +
					// File.separator + "1.png";
					String filename = file.getAbsolutePath();
					String sharemsg = eventinfo.getSportName() + " "
							+ eventinfo.getEventType() + " \""
							+ eventinfo.getEventTitle() + "\" at " + strAddr
							+ " on " + eventinfo.getEventStartDate() + " "
							+ strStartTime + ". link: "
							+ eventinfo.getEventShareURL();
					Intent intent = activity.getPackageManager()
							.getLaunchIntentForPackage("com.instagram.android");
					if (intent != null) {
						Intent shareIntent = new Intent();
						shareIntent.setAction(Intent.ACTION_SEND);
						shareIntent.setPackage("com.instagram.android");
						try {
							shareIntent.putExtra(Intent.EXTRA_STREAM, Uri
									.parse(MediaStore.Images.Media.insertImage(
											activity.getContentResolver(),
											filename, "", "")));
							shareIntent.putExtra(Intent.EXTRA_TEXT, sharemsg);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						shareIntent.setType("image/jpeg");
						startActivity(shareIntent);
					} else {
						// bring user to the market to download the app.
						// or let them choose an app?
						intent = new Intent(Intent.ACTION_VIEW);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setData(Uri.parse("market://details?id="
								+ "com.instagram.android"));
						startActivity(intent);
					}

				} catch (Exception ex) {
					Toast.makeText(activity, "Please try again",
							Toast.LENGTH_SHORT).show();
				}
			} else {
			}
		}
	}

	/*
	 * @Override public void updateCommentsCnt(int mediaPosition, int
	 * commentCount) { eventinfo. }
	 */

}