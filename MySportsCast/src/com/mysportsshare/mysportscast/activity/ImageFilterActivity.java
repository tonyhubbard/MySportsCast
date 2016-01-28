package com.mysportsshare.mysportscast.activity;

import java.util.Stack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.fragments.FilterImageEditFragment;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.Utils;

/*This activity apply filters to the selected picture 
 * & broadcast filtered image to webserver.
 * & also user can share filtered image via social media  
 * */

public class ImageFilterActivity extends ActionBarActivity   {

	private TextView title;
	private ImageView back;
	private ImageView settingBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private LinearLayout footerlyt;
	private FrameLayout fragmentlyt;
	private ImageView addEventBtn;
	private ImageView searchBtn;

	Fragment imageEditFragment;
	boolean isImageEditFragmentOpened=false;

	Stack<Fragment> mStacks;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MySportsApp.setActivityContext(ImageFilterActivity.this);
		
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.titlebar);
		ImageView settings = (ImageView)mActionBar.getCustomView().findViewById(R.id.setting_iv);

		if(!Utils.isNetworkConnected(ImageFilterActivity.this)) {
			Utils.networkAlertDialog(ImageFilterActivity.this,getResources().getString(R.string.toast_no_network));
		}

		//Initializing UI layout
		setContentView(R.layout.activity_main);
		mStacks = new Stack<Fragment>();
		init();

		//Loading image filtering fragment.		
		loadImageEditingFragment();
		/*Fragment activeFragment = new FilterImageEditFragment();
		Bundle args = getIntent().getExtras();
		activeFragment.setArguments(args);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.home_frame, activeFragment);
		ft.commit();*/
	}

	/**
	 *  initialization
	 */
	private void init(){

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
		settingBtn.setVisibility(View.VISIBLE);
		tvEventType = (TextView) findViewById(R.id.title_bar_tv_event_type);
		tvEventType.setVisibility(View.GONE);
		addEventBtn = (ImageView)findViewById(R.id.add_an_event_iv);
		addEventBtn.setVisibility(View.GONE);
		fragmentlyt = (FrameLayout) findViewById(R.id.home_frame);
		footerlyt = (LinearLayout) findViewById(R.id.footer_llyt);
		footerlyt.setVisibility(View.GONE);
	}	

	/*@Override
	public void onBackPressed() {
		if (!isImageEditFragmentOpened) {
			loadImageEditingFragment();
		} else {
			finish();
		}
	}*/

	private void loadImageEditingFragment(){
		Fragment activeFragment;
		if(imageEditFragment==null){
			activeFragment = new FilterImageEditFragment();
			imageEditFragment = activeFragment;
			Bundle args = getIntent().getExtras();
			activeFragment.setArguments(args);
			Log.d("", "profileEventMedia"+args.getString(Constants.KEY_EVENT_MEDIA_ID));
		}else{
			activeFragment = imageEditFragment;
		}
		/*FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.home_frame, activeFragment);
		ft.addToBackStack(null);
		ft.commit();*/
		push(activeFragment);
	}

	public void setImageEditFragementOpenedFlag(){
		//Called when imageEdit fragment is visible 
		isImageEditFragmentOpened = true;	
	}

	public void resetImageEditFragementOpenedFlag(){
		//Called when image broadcast fragment is visible
		isImageEditFragmentOpened = false;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		title = (TextView) findViewById(R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);
		title.setText("EDIT");
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
			
			//Clears the filtered bitmaps
			((FilterImageEditFragment)imageEditFragment).ClearCreatedBitmaps();
			
			finish();
			return;
		}

		popFragments();
	}

	public void popAllFragments() {
		mStacks.removeAllElements();
	}
}
