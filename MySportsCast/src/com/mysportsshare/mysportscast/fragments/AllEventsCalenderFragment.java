package com.mysportsshare.mysportscast.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CalendarEventsActivity;
import com.mysportsshare.mysportscast.activity.CustomMultiMonth;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.adapters.CustomDayAdapter;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.vdesmet.lib.calendar.MultiCalendarView;
import com.vdesmet.lib.calendar.OnDayClickListener;

public class AllEventsCalenderFragment extends Fragment implements
		OnDayClickListener, OnClickListener {

	private MultiCalendarView mMultiMonth;

	private TextView mSelectedTextView;
	private Typeface mSelectedTypeface;

	private CustomDayAdapter adapter;
	private HashMap<String, HashMap<String, List<String>>> calendarSummaryEvnetInfo;

	private TextView title;

	private ImageView settingBtn;

	private ImageView searchHeaderBtn;

	private ImageView back;

	private TextView tvEventType;

	private ImageView addEventBtn;

	private RelativeLayout events_header_llyt;

	private RelativeLayout search_header_llyt;

	private String userId;
	String fetchEventDatesType;

	private String userName;

	private int FirstYear = 1970;
	private int CurrentYear = 0;
	private int tmpYear = 1970;
	private Activity activity;
	private View layoutView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			this.activity = activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement DataPassListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		tmpYear = 1970;
		layoutView = inflater
				.inflate(R.layout.activity_multi, container, false);
		if (calendarSummaryEvnetInfo == null) {
			Bundle args = getArguments();
			if (args != null) {
				userId = args.getString(Constants.userProf_userID);
				userName = args.getString(Constants.userProf_userName);
				fetchEventDatesType = args.getString(Constants.dataReceive);
			}
		}
		Log.d("", "userId: " + userId + " " + fetchEventDatesType);
		MySportsApp.setActivityContext(activity);
		init(layoutView);
		// Retrieve the CalendarView
		mMultiMonth = (MultiCalendarView) layoutView
				.findViewById(R.id.multi_calendar);
		// mMultiMonth.setIndicatorVisible(true);
		// Set the first valid day
		final Calendar firstValidDay = Calendar.getInstance();
		firstValidDay.set(FirstYear, 0, 1);
		mMultiMonth.setFirstValidDay(firstValidDay);

		// Set the last valid day
		final Calendar lastValidDay = Calendar.getInstance();
		lastValidDay.add(Calendar.MONTH, 12 * 3); // 3 years
		mMultiMonth.setLastValidDay(lastValidDay);

		// Create adapter
		adapter = new CustomDayAdapter();

		// Set listener and adapter
		mMultiMonth.setDayAdapter(adapter);

		// Setting current year
		Calendar calendar = Calendar.getInstance();
		CurrentYear = calendar.get(Calendar.YEAR);

		mMultiMonth.setOnTitlePageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int viewPagerPosition) {

				CurrentYear = (viewPagerPosition / 12) + FirstYear;
				if (tmpYear != CurrentYear) {
					tmpYear = CurrentYear;
					// UIHelperUtil.showToastMessage(viewPagerPosition+" "+CurrentYear);
					TriggerCalendarEvents();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// UIHelperUtil.showToastMessage("Page scrolled"+arg0+"");
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// UIHelperUtil.showToastMessage("Page state changed"+arg0+"");
			}
		});

		// Display events list on selecting a day
		mMultiMonth.setOnDayClickListener(new OnDayClickListener() {
			@Override
			public void onDayClick(long dayInMillis) {
				final Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(dayInMillis);
				String yr = Integer.toString(calendar.get(Calendar.YEAR));
				String mnth = Integer.toString(calendar.get(Calendar.MONTH) + 1);
				String day = Integer.toString(calendar
						.get(Calendar.DAY_OF_MONTH));

				// display calendar events list
				//	commented by koti
				
				
				Intent i = new Intent(activity, CalendarEventsActivity.class);
				i.putExtra(Constants.data_cal_year, yr);
				i.putExtra(Constants.data_cal_mnth, mnth);
				i.putExtra(Constants.data_cal_day, day);
				i.putExtra(Constants.userProf_userID, userId);
				// TODO: replace user name
				i.putExtra(Constants.userProf_userName, userName);
				i.putExtra(Constants.dataReceive, fetchEventDatesType);
				startActivity(i);
				
				/*Bundle args = new Bundle();
				args.putString(Constants.data_cal_year, yr);
				args.putString(Constants.data_cal_mnth, mnth);
				args.putString(Constants.data_cal_day, day);
				args.putString(Constants.userProf_userID, userId);
				args.putString(Constants.userProf_userName, userName);
				args.putString(Constants.dataReceive, fetchEventDatesType);
				Fragment activeFragment = new CalendarEventsFragment();
				activeFragment.setArguments(args);
				displayOnActivity(activeFragment);*/
			}
		});
		return layoutView;
	}

	
	private void displayOnActivity(Fragment activeFragment){
		if(activity.getClass().getSimpleName().equalsIgnoreCase("MainActivity")){
			((MainActivity)activity).pushFragments(((MainActivity)activity).getmCurrentTab(), activeFragment, false,false);
		}
	}
	private void init(View layoutView) {
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		back = (ImageView) activity.findViewById(R.id.back_iv);
		settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);
		searchHeaderBtn = (ImageView) activity.findViewById(R.id.search_iv);
		addEventBtn = (ImageView) activity.findViewById(R.id.add_an_event_iv);
		tvEventType = (TextView) activity
				.findViewById(R.id.title_bar_tv_event_type);

		events_header_llyt = (RelativeLayout) activity
				.findViewById(R.id.events_header_llyt);
		search_header_llyt = (RelativeLayout) activity
				.findViewById(R.id.search_header_llyt);

		title.setVisibility(View.VISIBLE);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		settingBtn.setVisibility(View.VISIBLE);
		settingBtn.setOnClickListener(this);
		settingBtn.setImageDrawable(null);
		settingBtn.setImageResource(R.drawable.addanevent_icon);
		tvEventType.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		search_header_llyt.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.back_iv:
			if (activity != null) {
				(activity).onBackPressed();
			}
			break;
			
		case R.id.setting_iv:
			Fragment activeFragment1 = new AddanEventFragment();
			// displays user page in specified tab.
			((MainActivity) activity).pushFragments(
					((MainActivity) activity).getmCurrentTab(),
					activeFragment1, false, true);
			break;
		}
	}

	@Override
	public void onDayClick(long dayInMillis) {

		// Reset the previously selected TextView to his previous Typeface
		if (mSelectedTextView != null) {
			mSelectedTextView.setTypeface(mSelectedTypeface);
		}

		final TextView day = mMultiMonth.getTextViewForDate(dayInMillis);
		if (day != null) {
			// Remember the selected TextView and it's font
			mSelectedTypeface = day.getTypeface();
			mSelectedTextView = day;

			// Show the selected TextView as bold
			day.setTypeface(Typeface.DEFAULT_BOLD);
		}

	}

	@Override
	public void onStart() {
		super.onStart();

		tvEventType.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		settingBtn.setVisibility(View.VISIBLE);
		searchHeaderBtn.setVisibility(View.GONE);
		search_header_llyt.setVisibility(View.GONE);

	}

	private void TriggerCalendarEvents() {
		if (fetchEventDatesType.equals(Constants.data_cal_allEvents)) {
			servicetoGetCalendarSummaryInfo(
					getString(R.string.get_calendarSummaryInfo), userId, "",
					CurrentYear + "");
			title.setText("CALENDAR EVENTS");
		} else if (fetchEventDatesType
				.equals(Constants.data_cal_userCheckedEvents)) {
			servicetoGetCalendarSummaryInfo(
					getString(R.string.get_UserPosted_calendarSummaryInfo),
					userId, "1", CurrentYear + "");
			if (userId.equalsIgnoreCase(SharedPreferencesUtils.getUserId())) {
				title.setText("MY CHECK-INS");
			} else {
				title.setText(userName + " CHECK-IN");
			}
		} else if (fetchEventDatesType
				.equals(Constants.data_cal_userPostEvents)) {
			servicetoGetCalendarSummaryInfo(
					getString(R.string.get_UserPosted_calendarSummaryInfo),
					userId, "0", CurrentYear + "");
			if (userId.equalsIgnoreCase(SharedPreferencesUtils.getUserId())) {
				title.setText("MY EVENTS");
			} else {
				title.setText(userName + " EVENTS");
			}
		}
	}

	public void updateCalender() {
		adapter.updateCalendarEventsList(calendarSummaryEvnetInfo);

		mMultiMonth.notifyDataSetChangedOnly();
	}

	/************************* ------SERVER INTERACTION STARTS----- ***************************/
	/**
	 * method for fetches events dates
	 */
	private void servicetoGetCalendarSummaryInfo(String service, String userId,
			String user_checkIns, String year) {
		String url = Constants.common_url + service;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		Calendar c = Calendar.getInstance();
		nameValuePairs.add(new BasicNameValuePair("time_zone", TimeZone
				.getDefault().getID()));
		nameValuePairs.add(new BasicNameValuePair("year", year));
		if (TextUtils.isEmpty(user_checkIns)) {

		} else {
			// List of user added events dates
			nameValuePairs.add(new BasicNameValuePair(
					Constants.userProf_userID, userId));
			nameValuePairs.add(new BasicNameValuePair(
					Constants.userProf_calendar_checked_Events, user_checkIns));
		}
		calendarSummaryEvnetInfo = new HashMap<String, HashMap<String, List<String>>>();
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
									JSONArray resEventDatesObj = null;
									if (fetchEventDatesType
											.equals(Constants.data_cal_allEvents)) {
										resEventDatesObj = resObj
												.getJSONArray(Constants.cal_DateList);
									} else {
										resEventDatesObj = resObj
												.getJSONArray(Constants.cal_DateList_userProfile);
									}
									for (int i = 0; i < resEventDatesObj
											.length(); i++) {
										JSONObject eventDateObj = resEventDatesObj
												.getJSONObject(i);
										String date = eventDateObj
												.getString(Constants.cal_EventDate);
										Calendar calObj = Calendar
												.getInstance();
										try {
											// SimpleDateFormat srcFormat = new
											// SimpleDateFormat("MM/dd/yyyy HH:mm");
											// srcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
											// Date srcDt =
											// srcFormat.parse(date);

											// SimpleDateFormat dstFormat = new
											// SimpleDateFormat("MM/dd/yyyy HH:mm");
											// dstFormat.setTimeZone(TimeZone.getDefault());
											// String dstDt =
											// dstFormat.format(srcDt);

											calObj.setTime(new SimpleDateFormat(
													"MM/dd/yyyy hh:mm",
													Locale.ENGLISH).parse(date));
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
												calendarSummaryEvnetInfo.put(
														yr, MnthList);
											}
											List<String> DateList = MnthList
													.get(mnth);
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

									// displayCalendar();
									// TODO: display calendar screen
									updateCalender();
								} else if (responseStr != null
										&& responseStr
												.equalsIgnoreCase(Constants.webserver_response_fail)) {
									UIHelperUtil
											.showToastMessage("No events found");
								}
							} catch (JSONException ex) {
								Log.d(Constants.logUrl, ex.getMessage());
							}
						}

					}
				});
	}
	/************************************* Server communication ends ****************************************/
}
