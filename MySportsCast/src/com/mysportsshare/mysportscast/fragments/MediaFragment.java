package com.mysportsshare.mysportscast.fragments;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.tabhost.MyPageAdapter;
import com.mysportsshare.mysportscast.tabhost.MyTabFactory;
import com.mysportsshare.mysportscast.utils.Constants;

public class MediaFragment extends Fragment implements OnClickListener,
OnTabChangeListener, OnPageChangeListener {

	private TabHost mTabHost;
	private MyPageAdapter pageAdapter;
	private ViewPager mViewPager;

	//Title bar components
	TextView title;
	ImageView backBtn;
	ImageView settingBtn;
	private ImageView addEventBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private RelativeLayout search_header_llyt;	
	Activity activity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Make sure that container activity implement the callback interface
		try {
			this.activity = activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement DataPassListener");
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_media, container, false);
		mViewPager = (ViewPager) view.findViewById(R.id.discovery_viewpager);

		// Tab Initialization
		initialiseTabHost(view);

		// Fragments and ViewPager Initialization
		// List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(activity,
				getChildFragmentManager(),getArguments());
		mViewPager.setAdapter(pageAdapter);
		pageAdapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(this);

		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		backBtn = (ImageView) activity.findViewById(R.id.back_iv);
		settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);
		searchHeaderBtn = (ImageView)activity.findViewById(R.id.search_iv);
		addEventBtn = (ImageView)activity.findViewById(R.id.add_an_event_iv);
		tvEventType = (TextView) activity.findViewById(R.id.title_bar_tv_event_type);
		search_header_llyt = (RelativeLayout) activity.findViewById(R.id.search_header_llyt);


/*		backBtn.setVisibility(View.VISIBLE);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity)activity).onBackPressed();
			}
		});*/
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		
		title.setVisibility(View.VISIBLE);
		title.setText("ADD MEDIA");
		backBtn.setVisibility(View.GONE);		
		settingBtn.setVisibility(View.GONE);
		tvEventType.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		search_header_llyt.setVisibility(View.GONE);
//		mViewPager.setCurrentItem(0);
	}

	@Override
	public void onClick(View v) {

	}

	// Method to add a TabHost
	public void AddTab(Context activity, TabHost tabHost,
			TabHost.TabSpec tabSpec) {
		tabSpec.setContent(new MyTabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	// Manages the Tab changes, synchronizing it with Pages
	public void onTabChanged(String tag) {
		int pos = mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(pos);

		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			// this.mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FF0000"));
			// // unselected
			TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title); // Unselected Tabs
			tv.setTextColor(Color.parseColor("#000000"));
			tv.setTextSize(getResources().getDimension(R.dimen.tabhost_title));
		}

		// this.mTabHost.getTabWidget().getChildAt(this.mTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#0000FF"));
		// // selected
		TextView tv = (TextView) mTabHost.getCurrentTabView().findViewById(
				android.R.id.title); // for Selected Tab
		tv.setTextColor(Color.parseColor("#116DA9"));
		tv.setTextSize(getResources().getDimension(R.dimen.tabhost_title));

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// Manages the Page changes, synchronizing it with Tabs
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		int pos = mViewPager.getCurrentItem();
		this.mTabHost.setCurrentTab(pos);
	}

	@Override
	public void onPageSelected(int arg0) {
	}

	/*
	 * private List<Fragment> getFragments(){ List<Fragment> fList = new
	 * ArrayList<Fragment>(); AddACastFragment addacastFrag = new
	 * AddACastFragment(); VideosFragments videosFrag = new VideosFragments();
	 * PhotosFragments photosFrag = new PhotosFragments();
	 * fList.add(addacastFrag); fList.add(videosFrag); fList.add(photosFrag);
	 * return fList; }
	 */

	// Tabs Creation
	private void initialiseTabHost(View v) {
		mTabHost = (TabHost) v.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		AddTab(activity,
				this.mTabHost,
				this.mTabHost.newTabSpec("CAST").setIndicator("CAST"));
		AddTab(activity, this.mTabHost, this.mTabHost.newTabSpec("VIDEOS")
				.setIndicator("VIDEOS"));
		AddTab(activity, this.mTabHost, this.mTabHost.newTabSpec("PHOTOS")
				.setIndicator("PHOTOS"));
		mTabHost.setOnTabChangedListener(this);				

		TextView tv = (TextView) this.mTabHost.getCurrentTabView()
				.findViewById(android.R.id.title); // for Selected Tab
		tv.setTextColor(Color.parseColor("#116DA9"));
		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			// this.mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FF0000"));
			// // unselected
			TextView tvTitle = (TextView) mTabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title); // Unselected Tabs
			tvTitle.setTextSize(getResources().getDimension(R.dimen.tabhost_title));
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();

		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==Constants.REQUEST_IMAGE_CAPTURE || requestCode==Constants.REQUEST_IMAGE_GALLERY || requestCode==Constants.REQUEST_IMAGE_CROP){
			Fragment f = pageAdapter.getFragmentAtIndex(2);
			f.onActivityResult(requestCode, resultCode, data);
		}else if(requestCode==Constants.REQUEST_VIDEO_CAPTURE || requestCode==Constants.REQUEST_VIDEO_GALLERY || requestCode==Constants.REQUEST_VIDEO_CROP){
			Fragment f = pageAdapter.getFragmentAtIndex(1);
			f.onActivityResult(requestCode, resultCode, data);
		}
	}

}
