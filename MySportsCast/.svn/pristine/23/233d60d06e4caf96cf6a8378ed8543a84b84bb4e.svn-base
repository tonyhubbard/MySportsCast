package com.mysportsshare.mysportscast.adapters;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.mysportsshare.mysportscast.fragments.AllEventsCalenderFragment;
import com.mysportsshare.mysportscast.fragments.MyCalendarEventsFragment;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;

public class MyCalendarEventsAdapter extends FragmentPagerAdapter{

	final int PAGE_COUNT = 2;
	// Tab Titles
	private String tabtitles[] = new String[] { "MY EVENTS", "ALL EVENTS" };
	private String tabUserTitles[] = new String[]{"USER EVENTS", "ALL EVENTS"};
	HashMap<String, Fragment> fragmentPages;
	Context context;
	String userId;
	String userName;

	public MyCalendarEventsAdapter(FragmentManager fm,String userId,String userName) {
		super(fm);
		if (fragmentPages == null) {
			fragmentPages = new HashMap();
		}
		this.userId = userId;
		this.userName = userName;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

		// Open FragmentTab1.java
		case 0:
			AllEventsCalenderFragment fragmenttab1;

			if (fragmentPages.get("0") == null) {
				fragmenttab1 = new AllEventsCalenderFragment();
				fragmentPages.put("0", fragmenttab1);
			} else {
				fragmenttab1 = (AllEventsCalenderFragment) fragmentPages.get(position
						+ "");
			}
			Bundle args = new Bundle();
			args.putBoolean(Constants.Event_display_calendar, false);
			args.putString(Constants.userProf_userID, userId);
			args.putString(Constants.userProf_userName, userName);
			args.putString(Constants.dataReceive, Constants.data_cal_userPostEvents);
			args.putString(Constants.Event_display_calendar_day, "");
			args.putString(Constants.Event_display_calendar_month, "");
			args.putString(Constants.Event_display_calendar_year, "");
			fragmenttab1.setArguments(args);
			return fragmenttab1;

			// Open FragmentTab2.java
		case 1:
			AllEventsCalenderFragment fragmenttab2;
			if (fragmentPages.get("1") == null) {
				fragmenttab2 = new AllEventsCalenderFragment();
				fragmentPages.put("1", fragmenttab2);
			} else {
				fragmenttab2 = (AllEventsCalenderFragment) fragmentPages
						.get(position + "");
			}
			Bundle calargs = new Bundle();
			calargs.putBoolean(Constants.Event_display_calendar, false);
			calargs.putString(Constants.userProf_userID, userId);
			calargs.putString(Constants.userProf_userName, userName);
			calargs.putString(Constants.dataReceive,  Constants.data_cal_allEvents);
			calargs.putString(Constants.Event_display_calendar_day, "");
			calargs.putString(Constants.Event_display_calendar_month, "");
			calargs.putString(Constants.Event_display_calendar_year, "");
			fragmenttab2.setArguments(calargs);
			return fragmenttab2;
		default:
			break;
		}
		Log.v("testnull", "null");
		return null;
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	@Override
	public float getPageWidth(int position) {

		return super.getPageWidth(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if(userId.equalsIgnoreCase(SharedPreferencesUtils.getUserId())){
			return tabtitles[position];
		}else{
			return tabUserTitles[position];
		}
		
	}

}
