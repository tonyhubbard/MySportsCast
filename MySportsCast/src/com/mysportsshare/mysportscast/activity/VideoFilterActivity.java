package com.mysportsshare.mysportscast.activity;

import java.util.Stack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.fragments.EditFilterVideoBroadCastFragment;
import com.mysportsshare.mysportscast.fragments.FilterVideoBroadCastFragment;
import com.mysportsshare.mysportscast.utils.Constants;

/*This activity apply filters to the selected picture 
 * & broadcast filtered image to webserver.
 * & also user can share filtered image via social media  
 * */

public class VideoFilterActivity extends ActionBarActivity   {

	private TextView title;
	private ImageView back;
	private ImageView settingBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private LinearLayout footerlyt;
	private FrameLayout fragmentlyt;
	private ImageView addEventBtn;
	private ImageView searchBtn;

	Fragment videoBroadCastFragment;
	boolean isVideoViewFragmentOpened=false;
	Stack<Fragment> mStacks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MySportsApp.setActivityContext(VideoFilterActivity.this);

		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.titlebar);
		ImageView settings = (ImageView)mActionBar.getCustomView().findViewById(R.id.setting_iv);		

		//Initializing UI layout
		setContentView(R.layout.activity_main);
		init();

		//Loading image filtering fragment.		
		openVideoBroadCastFragment();
	}

	private void openVideoBroadCastFragment() {
		Fragment activeFragment;
		if(videoBroadCastFragment!=null){
			activeFragment = videoBroadCastFragment;
		}else{
			
			Bundle args = getIntent().getExtras();
			if(args.getBoolean(Constants.KEY_IS_EDIT_FILTER_VIDEO_FRAGMENT)){
				activeFragment = EditFilterVideoBroadCastFragment.getnewInstance(VideoFilterActivity.this);
			}else{
				activeFragment = FilterVideoBroadCastFragment.getnewInstance(VideoFilterActivity.this);
			}
			activeFragment.setArguments(args);
			videoBroadCastFragment = activeFragment;
		}
		/*FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.home_frame, activeFragment);	
		ft.addToBackStack(null);
		ft.commit();*/
		push(activeFragment);
		resetVideoViewFragementOpenedFlag(); 
	}

	/**
	 *  initialization
	 */
	private void init(){
		mStacks = new Stack<Fragment>();
		title = (TextView) findViewById(R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);
		title.setText("EDIT");

		back = (ImageView) findViewById(R.id.back_iv);
		back.setVisibility(View.VISIBLE);

		searchBtn = (ImageView) findViewById(R.id.search_iv);
		searchBtn.setVisibility(View.GONE);		
		settingBtn = (ImageView) findViewById(R.id.setting_iv);
		settingBtn.setImageResource(R.drawable.back_button);
		settingBtn.setRotation(180);
		settingBtn.setVisibility(View.GONE);
		tvEventType = (TextView) findViewById(R.id.title_bar_tv_event_type);
		tvEventType.setVisibility(View.GONE);
		addEventBtn = (ImageView)findViewById(R.id.add_an_event_iv);
		addEventBtn.setVisibility(View.GONE);
		fragmentlyt = (FrameLayout) findViewById(R.id.home_frame);
		footerlyt = (LinearLayout) findViewById(R.id.footer_llyt);
		footerlyt.setVisibility(View.GONE);
	}	

	public void setVideoViewFragementOpenedFlag(){
		//Called when imageEdit fragment is visible 
		isVideoViewFragmentOpened = true;
	}

	public void resetVideoViewFragementOpenedFlag(){
		//Called when image broadcast fragment is visible
		isVideoViewFragmentOpened = false;
	}

	/*@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(isVideoViewFragmentOpened){
			openVideoBroadCastFragment();
		}else{
			finish();
		}
	}*/
	@Override
	protected void onStop() {
		super.onStop();
		back.setVisibility(View.GONE);
	}

	public void push(Fragment fragment){
		mStacks.push(fragment);
		FragmentManager   manager         =   getSupportFragmentManager();
		FragmentTransaction ft            =   manager.beginTransaction();		
		ft.replace(R.id.home_frame, fragment);
		ft.commit();
	}

	public void popFragments(){
		/*    
		 *    Select the second last fragment in current tab's stack.. 
		 *    which will be shown after the fragment transaction given below 
		 */
		Fragment fragment  =   mStacks.elementAt(mStacks.size() - 2);

		/*pop current fragment from stack.. */
		mStacks.pop();

		/* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
		FragmentManager   manager         =   getSupportFragmentManager();
		FragmentTransaction ft            =   manager.beginTransaction();
		//		ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
		ft.replace(R.id.home_frame, fragment);
		ft.commit();
	}  

	public Fragment getLastButOneFragment(){
		/*    
		 *    Select the second last fragment in current tab's stack.. 
		 *    which will be shown after the fragment transaction given below 
		 */
		if(mStacks.size()>1){
			Fragment fragment  =   mStacks.elementAt(mStacks.size() - 2);
			return fragment;
		}else{
			return null;
		}
	}

	@Override
	public void onBackPressed() {
		if(mStacks.size() == 1){
			// We are already showing first fragment of current tab, so when back pressed, we will finish this activity..
			finish();
			return;
		}

		popFragments();
	}

	public void popAllFragments() {
		mStacks.removeAllElements();
	}
}
