package com.mysportsshare.mysportscast.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.adapters.SearchItemAdapter;
import com.mysportsshare.mysportscast.adapters.SearchItemAdapter.SearchItemHandler;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.models.SearchInfo;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.CustomLog;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;
import com.mysportsshare.mysportscast.view.FlowLayout;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Messenger;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.appcompat.BuildConfig;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class InvitePeopleActivity extends ActionBarActivity implements OnItemClickListener, OnClickListener, TextWatcher {
	
	private ListView invitePeopleListView; 
	private TextView invitePeopleListViewText;
	private ImageView invite_search_people_img;
	private EditText invite_people_ed;

	//Titile bar views
	private TextView titleTv;
	private ImageView back;
	private ImageView settingBtn;
	private ImageView addEventBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private RelativeLayout search_header_llyt;
	private Button invite_people_skip_btn;
	private FlowLayout flowLayout;

	private JsonObjectRequest jsonObjectRequest;
	private SearchItemAdapter searchUsersadapter;
	
	private List<SearchInfo> searchList = new ArrayList<SearchInfo>();
	private RelativeLayout events_header_llyt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_invite_people_to_event);
		MySportsApp.setActivityContext(InvitePeopleActivity.this);
		
		setActionBar();
		init();
		Intent intent = getIntent();
		if (intent != null && intent.hasExtra("selected_users")) {
			List<SearchInfo> selectedInvitesUsersList = intent.getExtras().getParcelableArrayList("selected_users");
			setSearchList(selectedInvitesUsersList);
			addSelectedInvitedPeople(selectedInvitesUsersList);
		}
	}
	

	private void setActionBar() {
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.titlebar);
	}


	public void init(){
		//Header items
		titleTv = (TextView)getActionBar().getCustomView().findViewById(R.id.title_bar_tv);
		back = (ImageView)getActionBar().getCustomView().findViewById(R.id.back_iv);
		settingBtn = (ImageView)getActionBar().getCustomView().findViewById(R.id.setting_iv);
		searchHeaderBtn = (ImageView)getActionBar().getCustomView().findViewById(R.id.search_iv);
		addEventBtn = (ImageView)getActionBar().getCustomView().findViewById(R.id.add_an_event_iv);
		tvEventType = (TextView)getActionBar().getCustomView().findViewById(R.id.title_bar_tv_event_type);
		events_header_llyt = (RelativeLayout)getActionBar().getCustomView().findViewById(R.id.events_header_llyt);
		search_header_llyt = (RelativeLayout)getActionBar().getCustomView().findViewById(R.id.search_header_llyt);
		back.setOnClickListener(this);
		back.setVisibility(View.INVISIBLE);

		invite_search_people_img = (ImageView) findViewById(R.id.invite_search_people_img);
		invite_people_ed = (EditText) findViewById(R.id.invite_people_ed);
		invitePeopleListView = (ListView) findViewById(R.id.invite_users_list);
		invitePeopleListViewText = (TextView) findViewById(R.id.invite_people_listViewText);
		invite_people_skip_btn = (Button) findViewById(R.id.invite_people_done_btn);
		flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
		invitePeopleListView.setOnItemClickListener(this);
		invite_search_people_img.setOnClickListener(this);
		invite_people_skip_btn.setOnClickListener(this);
		
		invite_people_ed.addTextChangedListener(this);
	}
	
	private void addSelectedInvitedPeople(List<SearchInfo> searchItems) {
		for (SearchInfo searchInfo : searchItems) {
			addSelectedInvitedUser(searchInfo);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		//Updating title bar fields		
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText(Constants.UI_InviteUserTitle);		
		back.setVisibility(View.VISIBLE);
		tvEventType.setVisibility(View.GONE);
		settingBtn.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		events_header_llyt.setVisibility(View.VISIBLE);
		search_header_llyt.setVisibility(View.GONE);

		invite_people_ed.requestFocus();
	}

	/******************************-----------Listeners Methods Starts-------------************************************/	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		/*
		 * Displays selected user profile
		SearchItemHandler selectedItem = (SearchItemHandler)view.getTag();
		if(selectedItem == null){ 

		}else{
			SearchInfo searchData = selectedItem.getSearchData();
			if(searchData!=null){

				UserProfileFragment activeFragment = new UserProfileFragment();
				Bundle arg = new Bundle();
				arg.putString(Constants.dataReceive, searchData.getID());
				activeFragment.setArguments(arg);
				//displays user profile page in search tab.
				((MainActivity)getActivity()).pushFragments(((MainActivity)getActivity()).getmCurrentTab(), activeFragment, false,true);
			}
		}*/

		//Select displayed user on single click
		SearchItemHandler selectedItem = (SearchItemHandler)view.getTag();
		ImageView userPic = selectedItem.getPhoto();
		//LinearLayout userLayout = selectedItem.getLayout();
		if(selectedItem == null){ 

		}else{
			if(selectedItem!=null){
				SearchInfo searchInfo = selectedItem.getSearchData();
				if(searchInfo.isSelected()){
					//flowLayout.setVisibility(View.GONE);
					//invite_search_people_img.setVisibility(View.VISIBLE);
					//invite_people_ed.setVisibility(View.VISIBLE);
					searchInfo.setSelected(false);
					userPic.setBackgroundColor(Color.TRANSPARENT);
					//userLayout.setBackgroundColor(Color.WHITE);
					removeSeletedItem(searchInfo);
				}else{
					//flowLayout.setVisibility(View.VISIBLE);
					//invite_search_people_img.setVisibility(View.GONE);
					//invite_people_ed.setVisibility(View.GONE);
					searchInfo.setSelected(true);
					userPic.setBackgroundColor(getResources().getColor(R.color.com_facebook_blue));
					//userLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.com_facebook_blue));
					setSelectedItem(searchInfo);
				}

			}
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.invite_search_people_img:
			if(invite_people_ed.getText().toString().trim().length()>0){
				serviceToGetSearchUsers(invite_people_ed.getText().toString().trim());

				//Closes keybord on showing result
				InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				keyboardMgr.hideSoftInputFromWindow(invite_people_ed.getWindowToken(), 0);
			}
			break;
		case R.id.invite_people_done_btn:
			/*StringBuffer selectedItems = new StringBuffer();
			for (int i = 0; i < searchList.size(); i++) {
				if (i < searchList.size()-1) {
					selectedItems.append(searchList.get(i)+",");	
				} else {
					selectedItems.append(searchList.get(i));
				}
			}
			Intent intent = new Intent();
			intent.putExtra("invited", selectedItems.toString());
			intent.putExtra("invited_size", searchList.size());
			setResult(Activity.RESULT_OK, intent);
			finish();*/
			/*Intent intent = new Intent();
			intent.putExtra("invited", new Messenger(target))
			setResult(Activity.RESULT_OK, intent);
			finish();*/
			Intent intent = new Intent();
			intent.putParcelableArrayListExtra("invited", (ArrayList<? extends Parcelable>) searchList);
			intent.putExtra("invited_size", searchList.size());
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		case R.id.back_iv:
			finish();
			break;
		}		
	}
	/******************************-----------Listeners Methods Ends-------------************************************/

	/****************************SERVER INTERFACES STARTS*************************/

	//Purpose:  Fetches followers of specified user
	public void serviceToGetSearchUsers(String searchStr){
		String url =  Constants.common_url + getString(R.string.search_people_event_info)
				+"?"+Constants.search_title+"="+searchStr
				+"&"+Constants.search_flag+"="+Constants.search_flag_people;

		CustomLog.v("", "Searchservice URL: " + url);
		if (jsonObjectRequest != null) {
			jsonObjectRequest.cancel();
		}
		jsonObjectRequest = new JsonObjectRequest(Method.GET, url, null,
				new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonObj) {
				if (jsonObj == null) {
					return;
				}
				getSearchUsersList(jsonObj);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				CustomLog.v("", "Volley Error: " + arg0);
				UIHelperUtil.showToastMessage("Error: " + arg0.getMessage());
				MySportsApp.getInstance().cancelPendingRequests("invite_searching_users");
			}

		}) ;

		MySportsApp.getInstance().addToRequestque(jsonObjectRequest, "invite_searching_users");
	}
	/****************************SERVER INTERFACES ENDS*************************/

	protected void getSearchUsersList(JSONObject jsonObj) {
		final List<SearchInfo> searchUsersList = new ArrayList<SearchInfo>();
		try {
			Log.d(Constants.logUrl," Search event Service RESPONSE: " + jsonObj);

			if (jsonObj != null) {
				try {
					//jsonObject = new JSONObject(jsonResponse);
					JSONObject resObj = jsonObj.getJSONObject(Constants.webserver_response);
					String responseStr = resObj.getString(Constants.webserver_responseInfo);
					if (responseStr!=null && responseStr.equalsIgnoreCase(Constants.webserver_response_success)){								
						//Update UI on the screen
						JSONArray searchObjs = resObj.getJSONArray(Constants.search_InfoObj);																		

						//Displaying user name
						if(searchObjs != null){
							for(int i = 0; i < searchObjs.length(); i++){
								SearchInfo searchObj = new SearchInfo();
								searchObj.setID(searchObjs.getJSONObject(i).getString(Constants.search_ID));										
								searchObj.setPhoto(searchObjs.getJSONObject(i).getString(Constants.search_image));
								searchObj.setName(searchObjs.getJSONObject(i).getString(Constants.search_name));
								
								if(isSearchItemExists(searchObj)) {
									searchObj.setSelected(true);
								} else {
									searchObj.setSelected(false);
								}
								searchUsersList.add(searchObj);
							}
							invitePeopleListViewText.setVisibility(View.INVISIBLE);
							invitePeopleListView.setVisibility(View.VISIBLE);
						}else{
							invitePeopleListViewText.setVisibility(View.VISIBLE);
							invitePeopleListView.setVisibility(View.INVISIBLE);
						}

					}else if (responseStr!=null && responseStr.equalsIgnoreCase(Constants.webserver_response_fail)){
						invitePeopleListViewText.setVisibility(View.VISIBLE);
						invitePeopleListView.setVisibility(View.INVISIBLE);
						UIHelperUtil.showToastMessage("Error");
					}else{
						//Prompt validation message to the user
						Utils.showAlertDialog(this,"Alert","Fail to updated. \n Please check internet connection");
					}

				}catch(Exception ex){
					Log.e(Constants.logUrl,ex.getMessage());
				}
			}
		} catch (Exception ex) {
			if(BuildConfig.DEBUG){
				Log.e(Constants.logUrl,ex.getMessage());
			}
		} finally {
			setAdapter(searchUsersList);
		}
	}
	
	private void setAdapter(List<SearchInfo> searchUsersList) {
		searchUsersadapter = new SearchItemAdapter(InvitePeopleActivity.this, Constants.search_flag_people, searchUsersList);
		invitePeopleListView.setAdapter(searchUsersadapter);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (!s.toString().equalsIgnoreCase("")) {
			serviceToGetSearchUsers(s.toString());	
		} else {
			invitePeopleListView.setAdapter(null);
		}
		
	}
	
	private boolean isSearchItemExists(SearchInfo searchInfo) {
		boolean exists = false;
		for (SearchInfo info : searchList) {
			if (info.getID().equalsIgnoreCase(searchInfo.getID())) {
				exists = true;
			}
		}
		return exists;
	}
	
	/*private List<String> searchList = new ArrayList();
	private void setSelectedItem(SearchInfo searchItem) {
		boolean isExist = false;
		if (!searchList.contains(searchItem.getID())) {
			isExist = searchList.add(searchItem.getID());		
		}
		if (searchList.size()  == 0) {
			isExist = searchList.add(searchItem.getID());	
		}
		if (isExist) {
			addItem(searchItem.getName());
		}
		System.out.println("isExists: " + isExist);
	}
	
	private void removeSeletedItem(SearchInfo searchItem) {
		//removeItem(searchItem);
		if (searchList.contains(searchItem.getID())) {
			int index = searchList.indexOf(searchItem.getID());
			searchList.remove(index);
			flowLayout.removeView(getLastView(index+1));
		}

	}*/
	

	private void setSelectedItem(SearchInfo searchItem) {
		boolean isExist = false;
		if (!searchList.contains(searchItem)) {
			isExist = searchList.add(searchItem);		
		}
		if (searchList.size()  == 0) {
			isExist = searchList.add(searchItem);	
		}
		if (isExist) {
			addSelectedInvitedUser(searchItem);
		}
	}
	
	//remove the view when clicked on selected item again
	private void removeSeletedItem(SearchInfo searchItem) {
		//removeItem(searchItem);
		if (isSearchItemExists(searchItem)) {
			try {
				int index = getIndexItem(searchItem);
				searchList.remove(index);
				flowLayout.removeView(getLastView(index));
				searchItem.setSelected(false);
				updateAdapterListWithChanges(searchItem);
				CustomLog.v("Index exists", "index: "+index + " : " + searchList.size());
			} catch(Exception e) {
				e.printStackTrace();
				return;
			}
			
		}

	}
	
	private void updateAdapterListWithChanges(SearchInfo searchItem) {
		if (searchUsersadapter == null) {
			return;
		}
		ArrayList<SearchInfo> oldList = searchUsersadapter.getList();
		for (SearchInfo info : oldList) {
			if (info.getID().equalsIgnoreCase(searchItem.getID())) {
				searchItem.setSelected(false);
			}
		}
		searchUsersadapter.updateUsers(oldList);
	}
	
	private void addSelectedInvitedUser(final SearchInfo searchItem) {
		FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(5, 5, 5, 5);
		
		int color = Color.TRANSPARENT;
		TextView selectedUserName = new TextView(this);
		selectedUserName.setBackgroundColor(color);
		//tvName.setBackgroundResource(R.drawable.input_box);
		selectedUserName.setText(searchItem.getName() +"");
		int left, right, top, bottom;
		left = right = top = bottom = 3;
		selectedUserName.setPadding(left, top, right, bottom);
		selectedUserName.setLayoutParams(layoutParams);
		
		ImageView imgRemove = new ImageView(this);
		imgRemove.setImageResource(R.drawable.delete);
		imgRemove.setLayoutParams(new LayoutParams(30, 30));
		
		final LinearLayout selectedUserLayout = new LinearLayout(this);
		selectedUserLayout.setBackgroundResource(R.drawable.multi_edit_corner);
		selectedUserLayout.setOrientation(LinearLayout.HORIZONTAL);
		selectedUserLayout.setGravity(Gravity.CENTER);
		selectedUserLayout.setLayoutParams(params);
		selectedUserLayout.addView(selectedUserName);
		selectedUserLayout.addView(imgRemove);
		
		
		params.rightMargin = 10;
		params.topMargin = 10;
		flowLayout.addView(selectedUserLayout);
		
		imgRemove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeSeletedItem(searchItem);
			}
		});
	}

	private View getLastView(int position) {
		return flowLayout.getChildAt(position);
	}
	
	private int getIndexItem(SearchInfo searchInfo) {
		int index = 0;
		for (SearchInfo info : searchList) {
			if (info.getID().equalsIgnoreCase(searchInfo.getID())) {
				index = searchList.indexOf(info);
			}
		}
		return index;
	}

	public void setSearchList(List<SearchInfo> searchList) {
		this.searchList = searchList;
	}	
			
}
