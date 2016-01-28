package com.mysportsshare.mysportscast.activity;

import java.util.Stack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mysportsshare.mysportscast.GCMIntentService.OnNotifyEventListener;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;

/*This activity apply filters to the selected picture 
 * & broadcast filtered image to webserver.
 * & also user can share filtered image via social media  
 * */

public class GCM_Notification_Handler_Activity extends ActionBarActivity {

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

	Stack<Fragment> mStacks; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.titlebar);
		ImageView settings = (ImageView)mActionBar.getCustomView().findViewById(R.id.setting_iv);

		if(!Utils.isNetworkConnected(GCM_Notification_Handler_Activity.this)) {
			Utils.networkAlertDialog(GCM_Notification_Handler_Activity.this,getResources().getString(R.string.toast_no_network));
		}

		//Initializing UI layout
		setContentView(R.layout.activity_main);
		init();

		mStacks = new Stack<Fragment>();
		//Loading calendar events list.		
		Fragment activeFragment = new CalendarEventsFragment();
		Bundle args = getIntent().getExtras();
		activeFragment.setArguments(args);
		push(activeFragment);*/
		//		FragmentManager fm = getSupportFragmentManager();
		//		FragmentTransaction ft = fm.beginTransaction();
		//		ft.add(R.id.home_frame, activeFragment);
		//		ft.addToBackStack("eventslist");
		//		ft.commit();
		performNavigation();	
	}
	private void performNavigation(){
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		Bundle bundle = intent
				.getBundleExtra(Constants.KEY_NOTIFICATION_EXTRAS);

		if(isUserAuthenticate()){
			//User already logged in 
			Intent resultIntent = new Intent(GCM_Notification_Handler_Activity.this, MainActivity.class);
			resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			resultIntent.putExtra(Constants.KEY_NOTIFICATION_EXTRAS, bundle);
			startActivity(resultIntent);
		}else{
			Intent resultIntent = new Intent(GCM_Notification_Handler_Activity.this, HomeActivity.class);//SplashActivity
			resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			resultIntent.putExtra(Constants.KEY_NOTIFICATION_EXTRAS, bundle);
			startActivity(resultIntent);
		}
		
	}	

	private boolean isUserAuthenticate(){
		if (SharedPreferencesUtils.getUserId() != null && SharedPreferencesUtils.getUserId().length() > 0) {
			return true;
		}else{
			return false;
		}
	}

}
