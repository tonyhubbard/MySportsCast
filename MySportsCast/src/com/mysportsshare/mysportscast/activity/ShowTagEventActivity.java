package com.mysportsshare.mysportscast.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.fragments.CalendarEventsFragment;
import com.mysportsshare.mysportscast.fragments.FilterImageEditFragment;
import com.mysportsshare.mysportscast.fragments.NotificationDetailedFragment;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * We will show this activity when we recieve a PNS (type tagged users).
 * @author gvsharma
 *
 */
public class ShowTagEventActivity extends  ActionBarActivity implements OnClickListener{

	private TextView title;
	private ImageView back;
	private ImageView settingBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private LinearLayout footerlyt;
	private FrameLayout fragmentlyt;
	private ImageView addEventBtn;
	private RelativeLayout events_header_llyt;
	private RelativeLayout search_header_llyt;

	
	static Stack<Fragment> mStacks; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MySportsApp.setActivityContext(ShowTagEventActivity.this);
		
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

		if(mStacks==null){
			mStacks = new Stack<Fragment>();
		}
		//Loading event-media item.		
		Fragment activeFragment = new NotificationDetailedFragment();
		Bundle args = getIntent().getExtras();		
		activeFragment.setArguments(args);
		push(activeFragment);
	}

	/**
	 *  initialization
	 */
	private void init(){

		title = (TextView) findViewById(R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);

		back = (ImageView) findViewById(R.id.back_iv);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);

		events_header_llyt = (RelativeLayout) findViewById(R.id.events_header_llyt);
		search_header_llyt = (RelativeLayout) findViewById(R.id.search_header_llyt);

		searchHeaderBtn = (ImageView)findViewById(R.id.search_iv);
		settingBtn = (ImageView) findViewById(R.id.setting_iv);
		tvEventType = (TextView) findViewById(R.id.title_bar_tv_event_type);
		addEventBtn = (ImageView)findViewById(R.id.add_an_event_iv);
		fragmentlyt = (FrameLayout) findViewById(R.id.home_frame);
		footerlyt = (LinearLayout) findViewById(R.id.footer_llyt);

	}	

	@Override
	protected void onStart() {
		super.onStart();
		//TODO: display selected calendar date
		
		tvEventType.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		settingBtn.setVisibility(View.GONE);
		footerlyt.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		search_header_llyt.setVisibility(View.GONE);
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

	@Override
	public void onBackPressed() {
		if(mStacks.size() <= 1){
			// We are already showing first fragment of current tab, so when back pressed, we will finish this activity..
			mStacks.pop();
			finish();
			return;
		}

		/*  Each fragment represent a screen in application (at least in my requirement, just like an activity used to represent a screen). So if I want to do any particular action
		 *  when back button is pressed, I can do that inside the fragment itself. For this I used AppBaseFragment, so that each fragment can override onBackPressed() or onActivityResult()
		 *  kind of events, and activity can pass it to them. Make sure just do your non navigation (popping) logic in fragment, since popping of fragment is done here itself.
		 */
		//		((Fragment)mStacks.get(mCurrentTab).lastElement()).onBackPressed();

		/* Goto previous fragment in navigation stack of this tab */
		popFragments();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.back_iv:
			onBackPressed();
			break;
		}	
	}
}
