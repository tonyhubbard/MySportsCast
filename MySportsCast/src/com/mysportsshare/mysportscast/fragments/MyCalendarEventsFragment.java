package com.mysportsshare.mysportscast.fragments;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CustomMultiMonth;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.adapters.MyCalendarEventsAdapter;
import com.mysportsshare.mysportscast.adapters.SearchPageAdapter;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.view.SlidingTabLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MyCalendarEventsFragment extends Fragment{
	
	private Activity activity;
	private View layoutView;
	private TextView titleTv;
	private ImageView back;
	private ViewPager searchPg;
	private SlidingTabLayout viewPagerTabs;
	MyCalendarEventsAdapter myCalendarEventsAdapter;
	private String user_id;
	private String user_name;
	private boolean isCalendarEvent;
	private String monthStr;
	private String dayStr;
	private String yearStr;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try{
			this.activity = activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()
					+ " must implement DataPassListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.fragment_mycalendarevents, container, false);
		
		Bundle args = getArguments();
		if(args!=null){
			//Viewing another user events
			user_id = args.getString(Constants.userProf_userID);
			user_name = args.getString(Constants.userProf_userName).toUpperCase();
			isCalendarEvent =  args.getBoolean(Constants.Event_display_calendar);
			
			//TODO: display calendar & non-calendar events
			if(isCalendarEvent){
				monthStr = args.getString(Constants.Event_display_calendar_month, "");
				dayStr = args.getString(Constants.Event_display_calendar_day, "");
				yearStr =  args.getString(Constants.Event_display_calendar_year, "");
			}else{
				monthStr = "";
				dayStr = "";
				yearStr = "";
			}
		}else{
			//Viewing logged user events
			user_id = SharedPreferencesUtils.getUserId();
			user_name = "";
		}
		
		init(layoutView);
		return layoutView;
	}

	private void init(View layoutView) {
	
		titleTv = (TextView) activity.findViewById(R.id.title_bar_tv);
		back = (ImageView) activity.findViewById(R.id.back_iv);

		searchPg = (ViewPager) layoutView.findViewById(R.id.pager);
		viewPagerTabs = (SlidingTabLayout) layoutView
				.findViewById(R.id.viewPagerTabs);
		viewPagerTabs.setDistributeEvenly(true); 
		
		// Setting Custom Color for the Scroll bar indicator of the Tab View
		viewPagerTabs
				.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
					@Override
					public int getIndicatorColor(int position) {
						return getResources().getColor(R.color.tabsScrollColor);
					}
				});

		myCalendarEventsAdapter = new MyCalendarEventsAdapter(getChildFragmentManager(),user_id,user_name);

		// Closes keyboard on touching search items
		searchPg.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(activity instanceof MainActivity){
					((MainActivity) activity).hideKeyboard(v);
				}
				
				return false;
			}
		});
		// Closes keyboard on touching search items
		viewPagerTabs.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(activity instanceof MainActivity){
					((MainActivity) activity).hideKeyboard(v);
				}
				
				return false;
			}
		});
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (myCalendarEventsAdapter != null) {
			searchPg.setAdapter(myCalendarEventsAdapter);
			// Setting the ViewPager For the SlidingTabsLayout
			viewPagerTabs.setViewPager(searchPg);
		}
	}

}
