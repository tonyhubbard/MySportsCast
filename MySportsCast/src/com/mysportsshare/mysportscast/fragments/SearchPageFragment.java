package com.mysportsshare.mysportscast.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.appcompat.BuildConfig;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.adapters.SearchItemAdapter;
import com.mysportsshare.mysportscast.adapters.SearchPageAdapter;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.models.SearchInfo;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.CustomLog;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;
import com.mysportsshare.mysportscast.view.SlidingTabLayout;

public class SearchPageFragment extends Fragment implements OnClickListener,
		TextWatcher {
	EditText searchEd;
	ImageView searchBtn;
	ViewPager searchPg;
	SearchPageAdapter searchItemsAdapter;
	SlidingTabLayout viewPagerTabs;

	// PagerTabStrip pagerTab;

	// Titile bar views
	private TextView titleTv;
	private ImageView back;
	private ImageView settingBtn;
	private ImageView addEventBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private RelativeLayout search_header_llyt;

	public String searchData;
	private View layoutView;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		if (layoutView != null) {
//			return layoutView;
//		}
		layoutView = inflater.inflate(R.layout.fragment_searchpage, container,
				false);
		init(layoutView);

		searchEd.addTextChangedListener(this);
		 
		return layoutView;
	}

	public void init(View layoutView) {
		titleTv = (TextView) activity.findViewById(R.id.title_bar_tv);
		back = (ImageView) activity.findViewById(R.id.back_iv);
		settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);
		searchHeaderBtn = (ImageView) activity
				.findViewById(R.id.search_iv);
		addEventBtn = (ImageView) activity.findViewById(
				R.id.add_an_event_iv);
		tvEventType = (TextView) activity.findViewById(
				R.id.title_bar_tv_event_type);
		search_header_llyt = (RelativeLayout) activity.findViewById(
				R.id.search_header_llyt);

		searchBtn = (ImageView) layoutView.findViewById(R.id.search_page_img);
		searchEd = (EditText) layoutView.findViewById(R.id.search_page_ed);
		searchPg = (ViewPager) layoutView.findViewById(R.id.pager);
		viewPagerTabs = (SlidingTabLayout) layoutView
				.findViewById(R.id.viewPagerTabs);
		viewPagerTabs.setDistributeEvenly(true); // To make the Tabs Fixed set
													// this true, This makes the
													// tabs Space Evenly in
													// Available width
		// Setting Custom Color for the Scroll bar indicator of the Tab View
		viewPagerTabs
				.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
					@Override
					public int getIndicatorColor(int position) {
						return getResources().getColor(R.color.tabsScrollColor);
					}
				});

		// pagerTab = (PagerTabStrip)layoutView.findViewById(R.id.pagerTab);

		searchBtn.setOnClickListener(this);
		searchBtn.setFocusable(true);

		// if(searchItemsAdapter==null){
		searchItemsAdapter = new SearchPageAdapter(getChildFragmentManager());
		// }

		// searchEd.setOnFocusChangeListener(new OnFocusChangeListener() {
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// if(v.getId() == R.id.search_page_ed && !hasFocus) {
		// InputMethodManager imm = (InputMethodManager)
		// activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		// }else if(v.getId() == R.id.search_page_ed && hasFocus){
		// InputMethodManager keyboardMgr = (InputMethodManager)
		// activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		// keyboardMgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
		// }
		// }
		// });

		// Closes keyboard on touching search items
		searchPg.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(activity instanceof MainActivity){
					((MainActivity) activity).hideKeyboard(v);
				}
				
				return false;
			}
		});
		// Closes keyboard on touching search items
		viewPagerTabs.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(activity instanceof MainActivity){
					((MainActivity) activity).hideKeyboard(v);
				}
				
				return false;
			}
		});

		// Handle keyboard search function
		searchEd.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				performSearch();
				return false;
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();

		// When received a search message from event page
		Bundle arg = getArguments();
		if (arg != null) {
			searchData = arg.getCharSequence(Constants.dataReceive).toString()
					.trim();
			searchEd.setText(searchData);
			InputMethodManager keyboardMgr = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			keyboardMgr.hideSoftInputFromWindow(searchEd.getWindowToken(), 0);
		}
		if (Utils.chkStatus()) {
			// When there is no input string
			if (arg == null) {
				searchEd.requestFocus();
			}
		} else {
			Utils.networkAlertDialog(activity,
					getResources().getString(R.string.toast_no_network));
		}

		// Add view pages to the fragment
		if (searchItemsAdapter != null) {
			searchPg.setAdapter(searchItemsAdapter);
			// Setting the ViewPager For the SlidingTabsLayout
			viewPagerTabs.setViewPager(searchPg);
		}

		// pagerTab.set

		// Updating title bar fields
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText(Constants.UI_SearchTitle);
		back.setVisibility(View.GONE);
		tvEventType.setVisibility(View.GONE);
		settingBtn.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		search_header_llyt.setVisibility(View.GONE);
		
	}

	@Override
	public void onResume() {
		super.onResume();
		
		performSearch();
	}
	@Override
	public void onStop() {
		super.onStop();
		InputMethodManager keyboardMgr = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		keyboardMgr.hideSoftInputFromWindow(searchEd.getWindowToken(), 0);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.search_page_img) {
			performSearch();
		}
	}

	private void performSearch() {

		CustomLog.v("SearcgPageFragment", "PerformClick method");
		if (Utils.chkStatus()) {
			searchData = searchEd.getText().toString().trim();
			if (TextUtils.isEmpty(searchData)) {
				// serviceToGetDefaultResults();
			} else {
				Bundle arg = getArguments();
				if (arg != null) {
					arg.putString(Constants.dataReceive, searchData);
				}
			}
			searchItemsAdapter.updateSearchData();
		} else {
			UIHelperUtil.showToastMessage(getString(R.string.service_toast));
		}

		/*InputMethodManager keyboardMgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		keyboardMgr.hideSoftInputFromWindow(searchEd.getWindowToken(), 0);*/


	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (!s.toString().equalsIgnoreCase("")) {
			if (!s.toString().equalsIgnoreCase(searchData)) {
				performSearch();
			}
		}
//		 performSearch();

	}

}
