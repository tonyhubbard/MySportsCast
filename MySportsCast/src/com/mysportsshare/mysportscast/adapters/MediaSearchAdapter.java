package com.mysportsshare.mysportscast.adapters;

import java.util.HashMap;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.mysportsshare.mysportscast.fragments.AddACastFragment;

public class MediaSearchAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 3;
	// Tab Titles
	private String tabtitles[] = new String[] { "ADD A CAST", "PHOTOS","VIDEOS" };
	HashMap<String, Fragment> fragmentPages;
	Context context;
	public MediaSearchAdapter(FragmentManager fm) {		
		super(fm);	
		fragmentPages = new HashMap();
	}
	
	@Override
	public Fragment getItem(int position) {
		switch (position) {

		// Open FragmentTab1.java
		case 0:
			AddACastFragment fragmenttab1;
			if(fragmentPages.get("0") ==null){
				fragmenttab1 = new AddACastFragment();			
				fragmentPages.put( "0", fragmenttab1);
			}else{
				fragmenttab1 = (AddACastFragment) fragmentPages.get(position);
			}
			return fragmenttab1;

			// Open FragmentTab2.java
		case 1:
			AddACastFragment fragmenttab2;
			if(fragmentPages.get("1") ==null){
				fragmenttab2 = new AddACastFragment();			
				fragmentPages.put("1",fragmenttab2);
			}else{
				fragmenttab2 = (AddACastFragment) fragmentPages.get(position);
			}
			return fragmenttab2;
			
		case 2: // Open FragmentTab3.java
			AddACastFragment fragmenttab3;
			if(fragmentPages.get("2") ==null){
				fragmenttab3 = new AddACastFragment();			
				fragmentPages.put( "2", fragmenttab3);
			}else{
				fragmenttab3 = (AddACastFragment) fragmentPages.get(position);
			}
			return fragmenttab3;
		}
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
		return tabtitles[position];
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		switch(position){
		case 0:
			if(fragmentPages.get("0") !=null){
				
			}		
			return super.instantiateItem(container, position);
		case 1:
			if(fragmentPages.get("1")!=null){
				
			}		
			return super.instantiateItem(container, position);
		case 2:
			if(fragmentPages.get("2")!=null){
				
			}		
			return super.instantiateItem(container, position);
		default:
			//			Fragment g = fragmentPages.get(position); 
			//			GridView gridView = (GridView) g.getView(); 
			return super.instantiateItem(container, position);
		}

	}
}
