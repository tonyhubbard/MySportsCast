package com.mysportsshare.mysportscast.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CalendarEventsActivity;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.adapters.SearchViewPagerEventPeopleAdapter;
import com.mysportsshare.mysportscast.adapters.SearchViewPagerEventPeopleAdapter.SearchItemHandler;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.models.SearchInfo;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.CustomLog;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;

public class SearchPeopleFragment extends Fragment implements
		OnItemClickListener {
	GridView searchListView;
	TextView searchListViewText;
	private JsonObjectRequest jsonObjectRequest;
	private RelativeLayout loading_notification_layout;
	Activity activity;
	
	
	static String searchStr=""; //holds current searching string
//	static SearchItemAdapter searchItemsAdapter;
	static SearchViewPagerEventPeopleAdapter searchItemsAdapt;
	static List<SearchInfo> searchList;
	
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
		View view = inflater.inflate(R.layout.fragment_search_event_people,
				container, false);
		init(view);
		return view;
	}

	public void init(View layoutView) {
		searchListView = (GridView) layoutView
				.findViewById(R.id.searchListView);
		searchListViewText = (TextView) layoutView
				.findViewById(R.id.searchListViewText);
		loading_notification_layout = (RelativeLayout) layoutView
				.findViewById(R.id.loading_notification_layout);
		searchItemsAdapt = new SearchViewPagerEventPeopleAdapter(
				activity, Constants.search_flag_event,
				null);
		searchListView.setAdapter(searchItemsAdapt);
		searchListView.setOnItemClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();

		SearchPageFragment parentFragment = (SearchPageFragment) getParentFragment();
		if (parentFragment != null) {
			if (TextUtils.isEmpty(parentFragment.searchData)) {
				searchStr = "";
				serviceToGetDefaultResults();
			}else{
				if(!searchStr.equalsIgnoreCase(parentFragment.searchData)){
					searchStr = parentFragment.searchData;
					getSearchResult(searchStr);
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SearchItemHandler selectedItem = (SearchItemHandler) view.getTag();
		if (selectedItem == null) {

		} else {
			SearchInfo searchData = selectedItem.getSearchData();
			if (searchData != null) {

				UserProfileFragment activeFragment = new UserProfileFragment();
				Bundle arg = new Bundle();
				arg.putString(Constants.dataReceive, searchData.getID());
				activeFragment.setArguments(arg);
				// displays user profile page in search tab.
				if(activity instanceof MainActivity){
					((MainActivity) activity).pushFragments(
							((MainActivity) activity).getmCurrentTab(),
							activeFragment, false, true);
				}else if(activity instanceof CalendarEventsActivity){
					((CalendarEventsActivity) activity).push(activeFragment);
				}
				
			}
		}

	}

	private void updateUIViews(int flag) {
		switch(flag){
		case 0:
			//views displays while loading
			loading_notification_layout.setVisibility(View.VISIBLE);
			searchListViewText.setVisibility(View.GONE);
			searchListView.setVisibility(View.GONE);
			break;
		case 1:
			//views displays after data fetched
			loading_notification_layout.setVisibility(View.GONE);
			searchListViewText.setVisibility(View.GONE);
			searchListView.setVisibility(View.VISIBLE);
			break;
		case 2:
			//views displays when no search data found
			loading_notification_layout.setVisibility(View.GONE);
			searchListViewText.setText("NO USERS FOUND");
			searchListViewText.setVisibility(View.VISIBLE);
			searchListView.setVisibility(View.GONE);
			break;
		}
		
	}
	
	/**************************** SERVER INTERFACES STARTS *************************/

	// Purpose: Fetches followers of specified user
	public void getSearchResult(String searchString) {
		// Saves following users list
		searchList = new ArrayList<SearchInfo>();
		searchStr = searchString;
		updateUIViews(0);
		
		// Url for getting following friends info

		String url = Constants.common_url
				+ MySportsApp.getAppContext().getResources()
						.getString(R.string.search_people_event_info) + "?"
				+ Constants.search_title + "=" + searchString + "&"
				+ Constants.search_flag + "=" + Constants.search_flag_people;

		CustomLog.v("URL", "SearchPeople: " + url);
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
						fecthSearchPeaple(searchList, jsonObj);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						CustomLog.v("", "Volley Error: " + arg0);
						UIHelperUtil.showToastMessage(activity.getString(R.string.toast_no_network));
						MySportsApp.getInstance().cancelPendingRequests(
								"searching_people");
					}

				});

		MySportsApp.getInstance().addToRequestque(jsonObjectRequest,
				"searching_people");

		// String url = Constants.common_url +
		// getString(R.string.search_people_event_info);
		//
		// //input data to web service
		// List<NameValuePair> inputVals = new ArrayList<NameValuePair>() ;
		// inputVals.add(new
		// BasicNameValuePair(Constants.search_title,searchStr));
		// inputVals.add(new
		// BasicNameValuePair(Constants.search_flag,Constants.search_flag_people));
		//
		// //Send req to url & handle the result
		// JsonParser.callBackgroundService(url, inputVals, new
		// JsonServiceListener() {
		// ProgressDialog pd;
		// @Override
		// public void showProgress() {
		// super.showProgress();
		// pd = new ProgressDialog(activity);
		// pd.show();
		// pd.setMessage("Loading...");
		// pd.setCancelable(false);
		// pd.setCanceledOnTouchOutside(false);
		// }
		//
		// @Override
		// public void hideProgress() {
		// super.hideProgress();
		// if (pd != null && pd.isShowing()) {
		// pd.dismiss();
		// }
		// }
		//
		// @Override
		// public void parseJsonResponse(String jsonResponse) {
		// fectHSearchPeaple(searchList, jsonResponse);
		// }
		// });
	}

	/**************************** SERVER INTERFACES ENDS *************************/

	private void fecthSearchPeaple(final List<SearchInfo> searchList,
			JSONObject jsonObj) {
		try {
			Log.d(Constants.logUrl, " Search event Service RESPONSE: "
					+ jsonObj);

			if (jsonObj != null) {
				// JSONObject jsonObject;
				try {
					// jsonObject = new JSONObject(jsonObj);
					JSONObject resObj = jsonObj
							.getJSONObject(Constants.webserver_response);
					String responseStr = resObj
							.getString(Constants.webserver_responseInfo);
					if (responseStr != null
							&& responseStr
									.equalsIgnoreCase(Constants.webserver_response_success)) {
						/*searchListViewText.setVisibility(View.INVISIBLE);
						searchListView.setVisibility(View.VISIBLE);*/

						updateUIViews(1);
						
						// Update UI on the screen
						JSONArray searchObjs = resObj
								.getJSONArray(Constants.search_InfoObj);

						// Displaying user name
						if (searchObjs != null) {
							for (int i = 0; i < searchObjs.length(); i++) {
								SearchInfo searchObj = new SearchInfo();
								searchObj.setID(searchObjs.getJSONObject(i)
										.getString(Constants.search_ID));
								searchObj.setPhoto(searchObjs.getJSONObject(i)
										.getString(Constants.search_image));
								searchObj.setName(searchObjs.getJSONObject(i)
										.getString(Constants.search_name));
								searchList.add(searchObj);
							}
							searchItemsAdapt.updateList(searchList);							
							searchItemsAdapt.notifyDataSetChanged();

						} else {
							UIHelperUtil.showToastMessage("No user found");
						}

					} else if ( !TextUtils.isEmpty(responseStr) 
							&& responseStr
									.equalsIgnoreCase(Constants.webserver_response_fail)) {
						updateUIViews(2);
						/*searchListViewText.setText("NO USER FOUND");
						searchListViewText.setVisibility(View.VISIBLE);
						searchListView.setVisibility(View.INVISIBLE);*/
					} else {
						// Prompt validation message to the user
						UIHelperUtil.showToastMessage(getString(R.string.service_toast));
					}

				} catch (Exception ex) {
					// if(BuildConfig.DEBUG){
					Log.e(Constants.logUrl, ex.getMessage());
					// }
				}
			}
		} catch (Exception ex) {
			if (BuildConfig.DEBUG) {
				Log.e(Constants.logUrl, ex.getMessage());
			}
		}
	}
	
	public void serviceToGetDefaultResults() {
		final List<SearchInfo> searchList = new ArrayList<SearchInfo>();
		String default_search_url = Constants.common_url + MySportsApp.getAppContext().getResources().getString(R.string.default_search_events)
				+ "?user_id=" + SharedPreferencesUtils.getUserId()
				+ "&search_flag="+"people"
				 + "&page_id=" + "0";
		CustomLog.d("DEFAULT_SERVICE", "URL: " + default_search_url);
		JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(Method.GET, default_search_url, null, 
				new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonObj) {
				if (jsonObj == null) {
					return;
				}
				fecthSearchPeaple(searchList, jsonObj);
			}
			
		},new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				CustomLog.v("", "Volley Error: " + arg0);
				UIHelperUtil.showToastMessage(activity.getString(R.string.toast_no_network));
				MySportsApp.getInstance().cancelPendingRequests("default_searching_event");
			}
		});
		MySportsApp.getInstance().addToRequestque(jsonObjectRequest, "default_searching_event");
	}

}
