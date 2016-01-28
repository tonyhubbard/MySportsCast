package com.mysportsshare.mysportscast.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.fragments.AddACastFragment;
import com.mysportsshare.mysportscast.services.LocationService;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.Utils;

public class SplashActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		Utils.registerForGCM(getApplicationContext());
		MySportsApp.setActivityContext(SplashActivity.this);
		setContentView(R.layout.activity_splash);
		Constants.mContext = getApplicationContext();
		//startService(new Intent(this, LocationService.class));
		((AddACastFragment)AddACastFragment.getInstance(SplashActivity.this)).isEventSelected = false;
		gotoHomePage();
	}

	public void gotoHomePage() {

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(2000);
				} catch(InterruptedException exception) {
					exception.printStackTrace();
				}finally {
					if (SharedPreferencesUtils.getUserId() != null && SharedPreferencesUtils.getUserId().length() > 0) {
						Intent intent = new Intent(SplashActivity.this,MainActivity.class);
						startActivity(intent);
						finish();
					} else {
						Intent homeintent = new Intent(SplashActivity.this, StartActivity.class);
						startActivity(homeintent);
						finish();
					}

				}
				return null;
			}			
		}.execute();					
	}	
	
	/* Added by Bhavani
	 * onBackPressed : To prevent closing of application while loading splash screen
	 * */
	@Override
	public void onBackPressed(){
		//do nothing
		Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
	}
	
}
