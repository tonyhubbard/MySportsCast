package com.mysportsshare.mysportscast.tabhost;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mysportsshare.mysportscast.fragments.AddACastFragment;
import com.mysportsshare.mysportscast.fragments.PhotosFragments;
import com.mysportsshare.mysportscast.fragments.VideosFragments;

public class MyPageAdapter extends FragmentPagerAdapter {

	private Context _context;
	public static int totalPage = 3;
	private Fragment fragments[];
	Bundle bundle;
	public MyPageAdapter(Context context, FragmentManager fm, Bundle bundle) {
		super(fm);
		_context = context;
		fragments = new Fragment[3];
		this.bundle = bundle;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment f = new Fragment();
		switch (position) {
		case 0:			
			f = AddACastFragment.getInstance(_context);
			f.setArguments(bundle);
			fragments[0]=f;
			break;
		case 1:
			f = VideosFragments.getInstance(_context);
			fragments[1]=f;
			break;
		case 2:
			f = PhotosFragments.getInstance(_context);
			fragments[2]=f;
			break;

		}
		return f;
	}

	@Override
	public int getCount() {
		return totalPage;
	}

	public Fragment getFragmentAtIndex(int position){
		if(position<totalPage){
			return fragments[position];
		}else{
			return null;
		}
	}
	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((View) view);
	}
	/*
	 * private List<Fragment> fragments;
	 * 
	 * public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
	 * super(fm); this.fragments = fragments; }
	 * 
	 * @Override public Fragment getItem(int position) { return
	 * this.fragments.get(position); }
	 * 
	 * @Override public int getCount() { return this.fragments.size(); }
	 */
}