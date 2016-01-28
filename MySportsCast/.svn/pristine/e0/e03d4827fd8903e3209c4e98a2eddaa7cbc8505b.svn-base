package com.mysportsshare.mysportscast.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.appcompat.BuildConfig;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CalendarEventsActivity;
import com.mysportsshare.mysportscast.activity.ImageFilterActivity;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.activity.VideoFilterActivity;
import com.mysportsshare.mysportscast.adapters.SearchItemAdapter;
import com.mysportsshare.mysportscast.adapters.SearchItemAdapter.SearchItemHandler;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.models.SearchInfo;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.CustomLog;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class SearchCastEvents1 extends Fragment implements OnItemClickListener,
		OnClickListener, TextWatcher {

	private GridView searchListView;
	private TextView searchListViewText;
	private ImageView imgSearch;
	private EditText searchEd;
	private String isFromHome = "";
	private int pageCount = 0;
	private SearchItemAdapter searchItemsAdapter;
	private String searchStr;
	private boolean reload;
	private RelativeLayout footerLayout;
	private Handler handler;
	private JsonObjectRequest jsonObjectRequest;
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
		View view = inflater.inflate(R.layout.fragment_searchcast_events,
				container, false);
		Bundle bundle = getArguments();
		if (bundle != null) {
			isFromHome = bundle.getString("isfromHome");
		}
		Log.d("", "" + isFromHome);
		init(view);
		searchStr = "";
		imgSearch.setOnClickListener(this);
		searchListView.setOnScrollListener(new OnScrollListener() {
			

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					if (searchListView.getLastVisiblePosition() >= searchListView
							.getCount() - 1) {
						if (reload) {
							footerLayout.setVisibility(View.VISIBLE);
							handler.postDelayed(new Runnable() {

								@Override
								public void run() {
									if (Utils.chkStatus()) {
										pageCount += 1;
										footerLayout.setVisibility(View.GONE);
										if (reload) {
											footerLayout
													.setVisibility(View.VISIBLE);
											servicetoFetchCheckedEvents(
													getString(R.string.get_checked_events),
													Constants.longi,
													Constants.lati,
													String.valueOf(pageCount),
													searchStr);

										} else {
											UIHelperUtil
													.showToastMessage("The list is up to date.");
											footerLayout
													.setVisibility(View.GONE);

										}
									} else {
										UIHelperUtil
												.showToastMessage(getString(R.string.toast_no_network));
									}

								}
							}, 750);
						} else {
							UIHelperUtil
									.showToastMessage("The list is up to date.");
							footerLayout.setVisibility(View.GONE);
						}
					} else {
						footerLayout.setVisibility(View.GONE);
					}
					Log.d("",
							"last pos:"
									+ searchListView.getLastVisiblePosition()
									+ " : " + searchListView.getCount() + " : "
									+ pageCount + " : " + reload);
				} else {
					footerLayout.setVisibility(View.GONE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.d("on scroll",visibleItemCount+"");
			}
		});

		if (Utils.chkStatus()) {
			if (!Constants.longi.equals("0") && !Constants.lati.equals("0")) {
				if (searchItemsAdapter == null) {
					servicetoFetchCheckedEvents(
							getString(R.string.get_checked_events),
							Constants.longi, Constants.lati,
							String.valueOf(pageCount), searchStr);
				} else {
					searchListView.setAdapter(searchItemsAdapter);
					// searchListView.setSelection(pos);
					searchListView.setOnItemClickListener(this);
				}
			} else {
				UIHelperUtil.showGPSDialogSettingsFromAppContext(activity);
			}

		} else {
			Utils.networkAlertDialog(activity,
					getResources().getString(R.string.toast_no_network));
		}
		Log.d("", "Lati and langi" + Constants.lati + "  " + Constants.longi);

		return view;
	}

	/**
	 * Function to show settings alert dialog On pressing Settings button will
	 * lauch Settings Options
	 * */
	private void showGPSDialogSettings() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
		alertDialog.setTitle("Location Settings");
		alertDialog.setMessage("Enable Location");
		// setting Dialog cancelable to false
		alertDialog.setCancelable(false);
		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						dialog.cancel();
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Close",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		// Showing Alert Message
		alertDialog.show();

	}

	public void init(View layoutView) {
		searchListView = (GridView) layoutView
				.findViewById(R.id.searchListView);
		searchListViewText = (TextView) layoutView
				.findViewById(R.id.searchListViewText);
		searchListView.setOnItemClickListener(this);
		imgSearch = (ImageView) layoutView.findViewById(R.id.search_page_img);
		searchEd = (EditText) layoutView.findViewById(R.id.search_page_ed);
		searchEd.addTextChangedListener(this);
		footerLayout = (RelativeLayout) layoutView
				.findViewById(R.id.loading_footer_layout);
		handler = new Handler();

	}

	@Override
	public void onResume() {
		super.onResume();

		TextView title = (TextView) activity.findViewById(R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);
		title.setText("TAG EVENTS");
		ImageView backIv = (ImageView) activity.findViewById(R.id.back_iv);
		backIv.setVisibility(View.VISIBLE);
		backIv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * case R.id.search_page_img: pageCount = 0;
		 * footerLayout.setVisibility(View.GONE); InputMethodManager keyboardMgr
		 * = (InputMethodManager) activity
		 * .getSystemService(Context.INPUT_METHOD_SERVICE);
		 * keyboardMgr.hideSoftInputFromWindow(searchEd.getWindowToken(), 0);
		 * searchStr = searchEd.getText().toString();
		 * 
		 * if (searchStr != null) { if (searchStr.trim().length() != 0) { //
		 * getSearchResult(searchStr);
		 * servicetoFetchCheckedEvents(getString(R.string.get_checked_events),
		 * Constants.longi,Constants.lati, String.valueOf(pageCount),
		 * searchStr); } } break;
		 */
		case R.id.back_iv:
			if (activity != null) {
				activity.onBackPressed();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		SearchItemHandler selectedItem = (SearchItemHandler) view.getTag();
		if (selectedItem != null) {
			SearchInfo searchData = selectedItem.getSearchData();
			Log.d("", "search cast:" + searchData.getName().trim() + " "
					+ searchData.getPhoto());
			if (searchData != null) {
				Bundle bundle = new Bundle();
				// TODO change this used id.
				bundle.putString(Constants.KEY_EVENT_PIC, searchData.getPhoto());
				bundle.putString(Constants.KEY_EVENT_ID, searchData.getID());
				bundle.putString(Constants.KEY_EVENT_TYPE, searchData.getName()
						.trim());

				if (activity instanceof ImageFilterActivity) {
					Log.d("", "search cast type: image filter activity");
					Fragment fmt = ((ImageFilterActivity) activity)
							.getLastButOneFragment();
					/*
					 * if(fmt instanceof FilterImageBroadCastFragmentFromHome){
					 * (
					 * (FilterImageBroadCastFragmentFromHome)fmt).setEventTypeAndId
					 * (searchData.getName().toString(),searchData.getID());
					 * }else
					 */

					if (fmt instanceof FilterImageBroadCastFragment) {
						Log.d("", "search cast type: image broad cast fragment");
						((FilterImageBroadCastFragment) fmt).setEventTypeAndId(
								searchData.getName().toString(),
								searchData.getID());
					}
					((ImageFilterActivity) activity).popFragments();
				} else if (activity instanceof VideoFilterActivity) {
					Log.d("", "search cast type: video filter activity");
					Fragment fmt = ((VideoFilterActivity) activity)
							.getLastButOneFragment();
					/*
					 * if(fmt instanceof FilterVideoBroadCastFragmentFromHome){
					 * (
					 * (FilterVideoBroadCastFragmentFromHome)fmt).setEventTypeAndId
					 * (searchData.getName().toString(),searchData.getID());
					 * }else
					 */

					if (fmt instanceof FilterVideoBroadCastFragment) {
						Log.d("", "search cast type: video broadcast");
						((FilterVideoBroadCastFragment) fmt).setEventTypeAndId(
								searchData.getName().toString(),
								searchData.getID());
					}
					((VideoFilterActivity) activity).popFragments();
				} else if (activity instanceof MainActivity) {
					/*Log.d("", "search cast type: main activity");
					Fragment fmt = ((MainActivity) activity)
							.getLastButOneFragment();
					fmt.setArguments(bundle);
					((MainActivity) getActivity()).popFragments();*/
					
					Fragment fmt = ((MainActivity) activity).getLastButOneFragment();
					if (fmt instanceof FilterImageBroadCastFragment) {
						Log.d("", "profile search cast type: image broad cast fragment");
						((FilterImageBroadCastFragment) fmt).setEventTypeAndId(
								searchData.getName().toString(),
								searchData.getID());
					}else if(fmt instanceof FilterVideoBroadCastFragment){
						Log.d("", "profile search cast type: video broadcast");
						((FilterVideoBroadCastFragment) fmt).setEventTypeAndId(
								searchData.getName().toString(),
								searchData.getID());
					}else if(fmt instanceof MediaFragment){
						Log.d("", "search cast type: main activity");
						fmt.setArguments(bundle);
					}else if(fmt instanceof AddACastFragment){
						fmt.setArguments(bundle);
					}
					((MainActivity) getActivity()).popFragments();
					
					
				} else if (activity instanceof CalendarEventsActivity) {
					Log.d("", "search cast type: calender activity");
					Fragment fmt = ((CalendarEventsActivity) activity)
							.getLastButOneFragment();
					fmt.setArguments(bundle);
					((CalendarEventsActivity) getActivity()).popFragments();
				}

				/*
				 * if (isFromHome.equalsIgnoreCase("home")) { Log.d("",
				 * "isfromhome"); AddACastFragmentFromHome.getInstance(activity)
				 * .setArguments(bundle); if
				 * (getActivity().getClass().getSimpleName()
				 * .equalsIgnoreCase("MainActivity")) { // if (((MainActivity)
				 * getActivity()).getCurrentEvent() != null) { ((MainActivity)
				 * getActivity()).popFragments(); // } else { // } } else {
				 * ((CalendarEventsActivity) getActivity()).popFragments(); } }
				 * else if (isFromHome.equalsIgnoreCase("photo")) {
				 * if(getActivity()!=null){ ((FilterImageBroadCastFragment)
				 * FilterImageBroadCastFragment .getInstance(activity))
				 * .setEventTypeAndId
				 * (searchData.getName().toString(),searchData.getID());
				 * getActivity().getSupportFragmentManager().popBackStack(
				 * "searchcastevent", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				 * } }else if(isFromHome.equalsIgnoreCase("homephoto")){
				 * ((FilterImageBroadCastFragmentFromHome)
				 * FilterImageBroadCastFragmentFromHome
				 * .getInstance(getActivity()))
				 * .setEventTypeAndId(searchData.getName
				 * ().toString(),searchData.getID());
				 * getActivity().getSupportFragmentManager().popBackStack(
				 * "searchcasteventfromhome",
				 * FragmentManager.POP_BACK_STACK_INCLUSIVE); }else if
				 * (isFromHome.equalsIgnoreCase("video")) {
				 * if(getActivity()!=null){ ((FilterVideoBroadCastFragment)
				 * FilterVideoBroadCastFragment .getInstance(activity))
				 * .setEventTypeAndId
				 * (searchData.getName().toString(),searchData.getID());
				 * getActivity().getSupportFragmentManager().popBackStack(
				 * "searchcastevent", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				 * } } else if (isFromHome.equalsIgnoreCase("homevideo")) {
				 * ((FilterVideoBroadCastFragmentFromHome)
				 * FilterVideoBroadCastFragmentFromHome
				 * .getInstance(getActivity()))
				 * .setEventTypeAndId(searchData.getName
				 * ().toString(),searchData.getID());
				 * getActivity().getSupportFragmentManager().popBackStack(
				 * "searchcasteventhome",
				 * FragmentManager.POP_BACK_STACK_INCLUSIVE);
				 * 
				 * } else { Log.d("", "isnotfromhome");
				 * AddACastFragment.getInstance(activity).setArguments( bundle);
				 * if (activity.getClass().getSimpleName()
				 * .equalsIgnoreCase("MainActivity")) { // if (((MainActivity)
				 * activity).getCurrentEvent() != null) { ((MainActivity)
				 * activity).popFragments(); // } else { // } } else {
				 * ((CalendarEventsActivity) activity).popFragments(); } }
				 */
			}
		}
	}

	/*
	 * ---------------------------method for fetching logged user - events from
	 * the server -----------------------------
	 */
	private void servicetoFetchCheckedEvents(final String eventsType,
			String longi, String lati, final String pageId,
			final String searchStr) {

		Log.d("", "servicetoFetchCheckedEvents");
		String url = Constants.common_url + eventsType + "?"
				+ Constants.Events_userID + "="
				+ SharedPreferencesUtils.getUserId() + "&"
				+ Constants.Events_latitude + "=" + lati + "&"
				+ Constants.Events_longitude + "=" + longi + "&"
				+ Constants.Events_pageID + "=" + pageId + "&"
				+ Constants.Events_filter + "=" + "none" + "&"
				+ Constants.Events_search + "=" + searchStr;

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
						getSearchEventsList(jsonObj, pageId);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						CustomLog.v("", "Volley Error: " + arg0);
						UIHelperUtil.showToastMessage("Error: "
								+ arg0.getMessage());
						MySportsApp.getInstance().cancelPendingRequests(
								"tagged_searching_events");
					}

				});

		MySportsApp.getInstance().addToRequestque(jsonObjectRequest,
				"tagged_searching_events");

		/*
		 * final List<SearchInfo> eventsList = new ArrayList<SearchInfo>();
		 * String url = Constants.common_url + eventsType; List<NameValuePair>
		 * nameValuePairs = new ArrayList<NameValuePair>();
		 * nameValuePairs.add(new BasicNameValuePair(Constants.Events_userID,
		 * SharedPreferencesUtils
		 * .getUserId()));//SharedPreferencesUtils.getUserId()
		 * nameValuePairs.add(new BasicNameValuePair(Constants.Events_latitude,
		 * lati)); nameValuePairs.add(new
		 * BasicNameValuePair(Constants.Events_longitude, longi));
		 * nameValuePairs.add(new BasicNameValuePair(Constants.Events_pageID,
		 * pageId)); nameValuePairs.add(new
		 * BasicNameValuePair(Constants.Events_filter, "none"));
		 * nameValuePairs.add(new BasicNameValuePair(Constants.Events_search,
		 * searchStr)); Log.d(Constants.logUrl," Checked Event url: " + url +
		 * " user_id:"
		 * +SharedPreferencesUtils.getUserId()+" lat:"+lati+" longi:"+longi
		 * +" page_id:"+pageId); JsonParser.callBackgroundService(url,
		 * nameValuePairs, new JsonServiceListener() { ProgressDialog pd;
		 * 
		 * @Override public void showProgress() { super.showProgress(); pd = new
		 * ProgressDialog(activity); pd.show(); pd.setMessage("Loading...");
		 * pd.setCancelable(false); pd.setCanceledOnTouchOutside(false); }
		 * 
		 * @Override public void hideProgress() { super.hideProgress(); if (pd
		 * != null && pd.isShowing()) { pd.dismiss(); } }
		 * 
		 * @Override public void parseJsonResponse(String jsonResponse) {
		 * Log.d(Constants.logUrl," Checked Event Service RESPONSE: " +
		 * jsonResponse); if (jsonResponse!= null) { JSONObject jsonObject; try
		 * { jsonObject = new JSONObject(jsonResponse); JSONObject resObj =
		 * jsonObject.getJSONObject(Constants.webserver_response); String
		 * responseStr = resObj.getString(Constants.webserver_responseInfo); if
		 * (responseStr!=null &&
		 * responseStr.equalsIgnoreCase(Constants.webserver_response_success)){
		 * 
		 * JSONArray upcomingEventsArr ; String upcomingEventsMsg;
		 * upcomingEventsMsg =
		 * resObj.optString(Constants.Events_CheckedInfoObj);//
		 * 
		 * if(TextUtils.isEmpty(upcomingEventsMsg)){
		 * UIHelperUtil.showToastMessage(getString(R.string.service_toast));
		 * }else{ if(upcomingEventsMsg.equalsIgnoreCase("no events found")){
		 * UIHelperUtil.showToastMessage("No events found"); }else{
		 * upcomingEventsArr =
		 * resObj.getJSONArray(Constants.Events_CheckedInfoObj);
		 * if(upcomingEventsArr != null){
		 * 
		 * for(int i = 0; i < upcomingEventsArr.length(); i++){ SearchInfo
		 * eventinfo = new SearchInfo();
		 * eventinfo.setID(upcomingEventsArr.getJSONObject
		 * (i).getString(Constants.Event_ID));
		 * if(!upcomingEventsArr.getJSONObject
		 * (i).getString(Constants.Event_Title).equalsIgnoreCase("null")){
		 * eventinfo
		 * .setName(upcomingEventsArr.getJSONObject(i).getString(Constants
		 * .Event_Title)); }else{ eventinfo.setName("-"); }
		 * eventsList.add(eventinfo); } searchItemsAdapter = new
		 * SearchItemAdapter( activity,Constants.search_flag_event,eventsList);
		 * searchListView.setAdapter(searchItemsAdapter);
		 * searchItemsAdapter.notifyDataSetChanged();
		 * searchItemsAdapter.notifyDataSetInvalidated();
		 * 
		 * if (eventsList.size() >= 10) { reload = true; } else { reload =
		 * false; } setAdapter(eventsList, pageId); }
		 * 
		 * } }
		 * 
		 * }else if(responseStr!=null &&
		 * responseStr.equalsIgnoreCase("FAILURE")){
		 * UIHelperUtil.showToastMessage("No events found"); }else{
		 * UIHelperUtil.showToastMessage(getString(R.string.service_toast)); }
		 * 
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }finally { Log.v("",
		 * "URL getEvents: " +eventsList.size() + " : " + pageId);
		 * 
		 * } } else {
		 * UIHelperUtil.showToastMessage(getString(R.string.service_toast)); } }
		 * });
		 */

	}

	/************************* ------SERVER INTERACTION ENDS----- ***************************/

	protected void getSearchEventsList(JSONObject jsonObj, String pageId) {
		if (jsonObj != null) {
			final List<SearchInfo> eventsList = new ArrayList<SearchInfo>();
			try {
				JSONObject resObj = jsonObj
						.getJSONObject(Constants.webserver_response);
				String responseStr = resObj
						.getString(Constants.webserver_responseInfo);
				if (responseStr != null
						&& responseStr
								.equalsIgnoreCase(Constants.webserver_response_success)) {

					JSONArray upcomingEventsArr;
					String upcomingEventsMsg;
					upcomingEventsMsg = resObj
							.optString(Constants.Events_CheckedInfoObj);//

					if (TextUtils.isEmpty(upcomingEventsMsg)) {
						UIHelperUtil
								.showToastMessage(getString(R.string.service_toast));
					} else {
						if (upcomingEventsMsg
								.equalsIgnoreCase("no events found")) {
							UIHelperUtil.showToastMessage("No events found");
							reload = false;
						} else {
							upcomingEventsArr = resObj
									.getJSONArray(Constants.Events_CheckedInfoObj);
							if (upcomingEventsArr != null) {

								for (int i = 0; i < upcomingEventsArr.length(); i++) {
									SearchInfo eventinfo = new SearchInfo();
									eventinfo.setID(upcomingEventsArr
											.getJSONObject(i).getString(
													Constants.Event_ID));
									if (!upcomingEventsArr.getJSONObject(i)
											.getString(Constants.Event_Title)
											.equalsIgnoreCase("null")) {
										eventinfo.setName(upcomingEventsArr
												.getJSONObject(i).getString(
														Constants.Event_Title));
									} else {
										eventinfo.setName("-");
									}
									if (!upcomingEventsArr.getJSONObject(i)
											.getString(Constants.Event_img)
											.equalsIgnoreCase("null")) {
										eventinfo.setPhoto(upcomingEventsArr
												.getJSONObject(i).getString(
														Constants.Event_img));
									}
									eventsList.add(eventinfo);
								}
								/*
								 * searchItemsAdapter = new SearchItemAdapter(
								 * activity
								 * ,Constants.search_flag_event,eventsList);
								 * searchListView
								 * .setAdapter(searchItemsAdapter);
								 * searchItemsAdapter.notifyDataSetChanged();
								 * searchItemsAdapter
								 * .notifyDataSetInvalidated();
								 */

								if (eventsList.size() >= 10) {
									reload = true;
								} else {
									reload = false;
								}
								setAdapter(eventsList, pageId);
							} else {
								reload = false;
							}
						}
					}

				} else if (responseStr != null
						&& responseStr.equalsIgnoreCase("FAILURE")) {
					UIHelperUtil.showToastMessage("No events found");
					reload = false;
				} else {
					UIHelperUtil
							.showToastMessage(getString(R.string.service_toast));
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				Log.v("", "URL getEvents: " + eventsList.size() + " : "
						+ pageId);

			}
		} else {
			UIHelperUtil.showToastMessage(getString(R.string.service_toast));
		}
		footerLayout.setVisibility(View.GONE);
	}

	public void setAdapter(List<SearchInfo> eventsList, String pageId) {
		List<SearchInfo> tempList = new ArrayList<SearchInfo>();
		if (searchItemsAdapter != null) {
			Log.d("", "kotiif");
			tempList.addAll(searchItemsAdapter.getPrevList());
			tempList.addAll(eventsList);
		}

		if (pageId.equalsIgnoreCase("0") && searchItemsAdapter == null) {
			searchItemsAdapter = new SearchItemAdapter(activity,
					Constants.search_flag_event, eventsList);
			searchListView.setAdapter(searchItemsAdapter);
		} else {
			Log.d("", "kotiifelse");
			searchItemsAdapter.updateList(tempList);
		}

	}

	/**************************** SERVER INTERFACES STARTS *************************/
	/*
	 * 
	 * // Purpose: Fetches followers of specified user public void
	 * getSearchResult(String searchStr) { // Saves following users list final
	 * List<SearchInfo> searchList = new ArrayList<SearchInfo>();
	 * 
	 * // Url for getting following friends info String url =
	 * Constants.common_url + getString(R.string.search_people_event_info);
	 * 
	 * 
	 * servicetoFetchCheckedEvents(getString(R.string.get_checked_events),
	 * Constants.longi, Constants.lati, "","","","0",eventType);
	 * 
	 * String url = Constants.common_url + eventsType; List<NameValuePair>
	 * nameValuePairs = new ArrayList<NameValuePair>(); nameValuePairs.add(new
	 * BasicNameValuePair(Constants.Events_userID,
	 * user_id));//SharedPreferencesUtils.getUserId() nameValuePairs.add(new
	 * BasicNameValuePair(Constants.Events_latitude, lati));
	 * nameValuePairs.add(new BasicNameValuePair(Constants.Events_longitude,
	 * longi)); nameValuePairs.add(new
	 * BasicNameValuePair(Constants.Events_pageID, pageId));
	 * nameValuePairs.add(new BasicNameValuePair(Constants.Events_filter,
	 * eventType));
	 * 
	 * // input data to web service List<NameValuePair> inputVals = new
	 * ArrayList<NameValuePair>(); inputVals .add(new
	 * BasicNameValuePair(Constants.search_title, searchStr)); inputVals.add(new
	 * BasicNameValuePair(Constants.search_flag, Constants.search_flag_event));
	 * 
	 * // Send req to url & handle the result
	 * JsonParser.callBackgroundService(url, inputVals, new
	 * JsonServiceListener() {
	 * 
	 * ProgressDialog pd;
	 * 
	 * @Override public void showProgress() { super.showProgress(); pd = new
	 * ProgressDialog(activity); pd.show(); pd.setMessage("Loading...");
	 * pd.setCancelable(false); pd.setCanceledOnTouchOutside(false); }
	 * 
	 * @Override public void hideProgress() { super.hideProgress(); if (pd !=
	 * null && pd.isShowing()) { pd.dismiss(); } }
	 * 
	 * @Override public void parseJsonResponse(String jsonResponse) { try {
	 * Log.d(Constants.logUrl, " Search event Service RESPONSE: " +
	 * jsonResponse);
	 * 
	 * if (jsonResponse != null) { JSONObject jsonObject; try { jsonObject = new
	 * JSONObject(jsonResponse); JSONObject resObj = jsonObject
	 * .getJSONObject(Constants.webserver_response); String responseStr = resObj
	 * .getString(Constants.webserver_responseInfo); if (responseStr != null &&
	 * responseStr .equalsIgnoreCase(Constants.webserver_response_success)) {
	 * searchListViewText .setVisibility(View.INVISIBLE); searchListView
	 * .setVisibility(View.VISIBLE); // Update UI on the screen JSONArray
	 * searchObjs = resObj .getJSONArray(Constants.search_InfoObj);
	 * 
	 * // Displaying user name if (searchObjs != null) { for (int i = 0; i <
	 * searchObjs .length(); i++) { SearchInfo searchObj = new SearchInfo();
	 * searchObj .setID(searchObjs .getJSONObject( i) .getString(
	 * Constants.search_ID)); searchObj .setPhoto(searchObjs .getJSONObject( i)
	 * .getString( Constants.search_image)); searchObj .setName(searchObjs
	 * .getJSONObject( i) .getString( Constants.search_name));
	 * searchList.add(searchObj); } SearchItemAdapter searchItemsAdapter = new
	 * SearchItemAdapter( activity, Constants.search_flag_event, searchList);
	 * searchListView .setAdapter(searchItemsAdapter); searchItemsAdapter
	 * .notifyDataSetChanged(); searchItemsAdapter .notifyDataSetInvalidated();
	 * 
	 * } else { UIHelperUtil .showToastMessage("No followers found"); }
	 * 
	 * } else if (responseStr != null && responseStr
	 * .equalsIgnoreCase(Constants.webserver_response_fail)) {
	 * searchListViewText .setVisibility(View.VISIBLE); searchListView
	 * .setVisibility(View.INVISIBLE); } else { // Prompt validation message to
	 * the user Utils.showAlertDialog(activity, "Alert",
	 * "Fail to updated. \n Please check internet connection"); }
	 * 
	 * } catch (Exception ex) { // if(BuildConfig.DEBUG){
	 * Log.e(Constants.logUrl, ex.getMessage()); // } } } } catch (Exception ex)
	 * { if (BuildConfig.DEBUG) { Log.e(Constants.logUrl, ex.getMessage()); } }
	 * } }); }
	 *//**************************** SERVER INTERFACES ENDS *************************/
	/*
	 */

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		pageCount = 0;
		searchItemsAdapter = null;
		if (!s.toString().equalsIgnoreCase("")) {
			servicetoFetchCheckedEvents(getString(R.string.get_checked_events),
					Constants.longi, Constants.lati, String.valueOf(pageCount),
					s.toString());
		} else {
			searchListView.setAdapter(null);
		}
	}
}
