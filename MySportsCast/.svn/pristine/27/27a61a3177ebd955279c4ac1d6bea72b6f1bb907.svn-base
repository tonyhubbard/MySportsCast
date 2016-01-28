package com.mysportsshare.mysportscast.adapters;

import android.graphics.Color;
import android.widget.TextView;

import com.vdesmet.lib.calendar.DayAdapter;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CustomDayAdapter implements DayAdapter {
	private static final int[][] CATEGORY_COLORS = {
		null,
		null,
		null,
		{ Color.BLUE },
		{ Color.RED },
		{ Color.argb(255, 0, 102, 0)},{ Color.RED },
		{ Color.CYAN, Color.GREEN, Color.RED, Color.BLUE, Color.BLACK,
			Color.CYAN, Color.GREEN, Color.RED, Color.BLUE, Color.BLACK } };

	private final Random mRandom;
	private final long mToday;

	//Calendar events list
	private HashMap<String,HashMap<String,List<String>>> calendarSummaryEvnetInfo;

	public CustomDayAdapter() {
		mRandom = new Random();

		// Get the time in millis of today
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		mToday = calendar.getTimeInMillis();

	}

	public void updateCalendarEventsList(HashMap<String,HashMap<String,List<String>>> calendarSummaryEvnetInfo){
		this.calendarSummaryEvnetInfo = calendarSummaryEvnetInfo;		
	}
	@Override
	public int[] getCategoryColors(final long dayInMillis) {
		if(calendarSummaryEvnetInfo==null){
			return CATEGORY_COLORS[0];
		}else{
			final Calendar calendar = Calendar.getInstance();
			int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
			int currentMonth = calendar.get(Calendar.MONTH) + 1;
			int currentYear = calendar.get(Calendar.YEAR);
			calendar.setTimeInMillis(dayInMillis);
			int yr =  calendar.get(Calendar.YEAR);
			int mnth =  calendar.get(Calendar.MONTH)+ 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);

			boolean isEventExist = false;

			//Find no. of events per month
			HashMap<String,List<String>> yrEvents = calendarSummaryEvnetInfo.get(yr+"");
			if(yrEvents!=null){
				List<String> mnthEvents = yrEvents.get(mnth+"");
				if(mnthEvents!=null ){
					isEventExist = 	mnthEvents.contains(day+"");
					//					Set<String> uniqueSet = new HashSet<String>(mnthEvents);
					//					for (String temp : uniqueSet) {
					//						int freq = Collections.frequency(mnthEvents, temp);
					//						System.out.println(temp + ": " + freq );
					//						map.put(temp,freq);
					//					}
				}else{
					isEventExist = false;
				}
			}else{
				isEventExist = false;
			}

			if(isEventExist){
				if(yr<currentYear){
					return CATEGORY_COLORS[3];
				}else if(yr==currentYear){
					if(mnth<currentMonth){
						return CATEGORY_COLORS[3];
					}else if(mnth==currentMonth){
						if(day<currentDay){
							return CATEGORY_COLORS[3];
						}else if(day==currentDay){
							return CATEGORY_COLORS[4];
						}else if(day>currentDay){
							return CATEGORY_COLORS[5];
						}
					}else if(mnth>currentMonth){
						return CATEGORY_COLORS[5];
					}
				}else if(yr>currentYear){
					return CATEGORY_COLORS[5];
				}
				return CATEGORY_COLORS[0];
			}else{
				return CATEGORY_COLORS[0];
			}
		}
		//		// Fill the category colors with random int arrays.
		//		final int index = mRandom.nextInt(CATEGORY_COLORS.length);
		//		return CATEGORY_COLORS[4];
	}

	@Override
	public boolean isDayEnabled(final long dayInMillis) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dayInMillis);

		return true;
		// Disable all saturdays
		// return calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY;
	}

	@Override
	public void updateTextView(final TextView dateTextView,
			final long dayInMillis) {
		if (mToday == dayInMillis) {
			// Do something with the selected date
			dateTextView.setTextColor(Color.RED);
		}
		// else, we don't need to update the TextView
	}

	@Override
	public void updateHeaderTextView(final TextView header, final int dayOfWeek) {
		switch (dayOfWeek) {
		case Calendar.SATURDAY:
		case Calendar.SUNDAY:
			header.setTextColor(Color.BLUE);
			break;

		default:
			header.setTextColor(Color.BLUE);
			break;
		}
	}
}
