package com.mysportsshare.mysportscast.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.adapters.ViewPagerAdapter;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.utils.Utils;

public class StartActivity extends FragmentActivity {

	// Constants

	// Views
	// public static StartActivity instance = null;
	private ViewPager myViewPager;
	private ViewPagerAdapter adapter;
	private Button circle1, circle2, circle3, circle4, circle5;
	private int minWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		MySportsApp.setActivityContext(StartActivity.this);
		// instance = this;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		minWidth = width / 48;
		Log.d("", "screen width: " + width);
		init();
		setTab();

	}

	public static StartActivity getMainActivityInstance() {
		StartActivity activity = new StartActivity();
		return activity;
	}

	private void setTab() {
		myViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int position) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				 btnAction(position);
			}

		});

	}

	private void btnAction(int action) {
		switch (action) {
		case 0:
			setButton(circle1, "", minWidth + 3, minWidth + 3,
					R.drawable.selected_circle);
			setButton(circle2, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle3, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle4, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle5, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			break;

		case 1:
			setButton(circle2, "", minWidth + 3, minWidth + 3,
					R.drawable.selected_circle);
			setButton(circle1, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle3, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle4, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle5, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			break;

		case 2:
			setButton(circle3, "", minWidth + 3, minWidth + 3,
					R.drawable.selected_circle);
			setButton(circle1, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle2, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle4, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle5, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			break;
		case 3:
			setButton(circle4, "", minWidth + 3, minWidth + 3,
					R.drawable.selected_circle);
			setButton(circle1, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle2, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle3, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle5, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			break;
		case 4:
			setButton(circle5, "", minWidth + 3, minWidth + 3,
					R.drawable.selected_circle);
			setButton(circle1, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle2, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle3, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			setButton(circle4, "", minWidth + 2, minWidth + 2,
					R.drawable.unselected_circle);
			break;
		}
	}

	/**
	 * initializing views
	 */
	private void init() {
		myViewPager = (ViewPager) findViewById(R.id.pager);
		adapter = new ViewPagerAdapter(getApplicationContext(),
				getSupportFragmentManager());
		myViewPager.setAdapter(adapter);
		myViewPager.setCurrentItem(0);
		initButton();
	}

	private void initButton() {
		circle1 = (Button) findViewById(R.id.btn1);
		circle2 = (Button) findViewById(R.id.btn2);
		circle3 = (Button) findViewById(R.id.btn3);
		circle4 = (Button) findViewById(R.id.btn4);
		circle5 = (Button) findViewById(R.id.btn5);
		setButton(circle1, "", minWidth + 3, minWidth + 3,
				R.drawable.selected_circle);
		setButton(circle2, "", minWidth + 2, minWidth + 2,
				R.drawable.unselected_circle);
		setButton(circle3, "", minWidth + 2, minWidth + 2,
				R.drawable.unselected_circle);
		setButton(circle4, "", minWidth + 2, minWidth + 2,
				R.drawable.unselected_circle);
		setButton(circle5, "", minWidth + 2, minWidth + 2,
				R.drawable.unselected_circle);
	}

	private void setButton(Button btn, String text, int h, int w,
			int selectedCircle) {
		btn.setWidth(w);
		btn.setHeight(h);
		btn.setBackgroundResource(selectedCircle);
		btn.setText(text);
	}

	@Override
	public void onBackPressed() {
		showExitAlert();
	}

	/**
	 * Method for showing Alert dialog to the user to come out of application.
	 */
	private void showExitAlert() {
		final Dialog dialog = new Dialog(StartActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.dialog_two_btn);
		TextView message = (TextView) dialog
				.findViewById(R.id.dialog_text_title);
		Button no = (Button) dialog.findViewById(R.id.dialog_btn_n);
		Button yes = (Button) dialog.findViewById(R.id.dialog_btn_s);
		message.setText("Are you sure you want to Exit?");
		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		dialog.show();
	}

	/**
	 * click event for get started button
	 */
	public void clickGetStarting(View view) {
		Intent getstartIntent = new Intent(StartActivity.this,
				HomeActivity.class);
		startActivity(getstartIntent);
		// finish();
	}
}
