package com.mysportsshare.mysportscast.adapters;

import com.mysportsshare.mysportscast.fragments.BroadCastPhotoVideoFragmentPage;
import com.mysportsshare.mysportscast.fragments.BuyTicketsFragmentPage;
import com.mysportsshare.mysportscast.fragments.CheckTrackFragmentPage;
import com.mysportsshare.mysportscast.fragments.CreateEventFragmentPage;
import com.mysportsshare.mysportscast.fragments.FindSportsEventFragmentPage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
	private Context _context;
	public static int totalPage=5;
	public ViewPagerAdapter(Context context, FragmentManager fm) {
		super(fm);	
		_context=context;

	}
	@Override
	public Fragment getItem(int position) {
		Fragment f = new Fragment();
		switch(position){
		case 0:
			f=CreateEventFragmentPage.newInstance(_context);	
			break;
		case 1:
			f=FindSportsEventFragmentPage.newInstance(_context);	
			break;
		case 2:
			f=CheckTrackFragmentPage.newInstance(_context);	
			break;
		case 3:
			f=BuyTicketsFragmentPage.newInstance(_context);	
			break;
		case 4:
			f=BroadCastPhotoVideoFragmentPage.newInstance(_context);	
			break;
		}
		return f;
	}
	@Override
	public int getCount() {
		return totalPage;
	}

}
