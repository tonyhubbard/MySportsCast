package com.mysportsshare.mysportscast.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.adapters.ProfileMediaItemAdapter;
import com.mysportsshare.mysportscast.models.EventInfo;
import com.mysportsshare.mysportscast.models.ProfileMediaInfo;
import com.mysportsshare.mysportscast.models.TeamInfo;
import com.mysportsshare.mysportscast.models.UserProfileInfo;
import com.mysportsshare.mysportscast.models.EventInfo.EventMedia;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class ProfileAllFragment extends Fragment implements OnClickListener{
	ListView mediaListView;
	ProfileMediaItemAdapter mediaItemAdapter;
	Activity activity;
	private RelativeLayout footerLayout;

	private boolean reload;
	private Handler handler;
	private int pageCount = 0;

	//Title bar components
	TextView title;
	ImageView backBtn;
	ImageView settingBtn;
	private ImageView addEventBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private RelativeLayout search_header_llyt;

	//User details
	private String user_id;
	private String first_name = "";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layoutView = inflater.inflate(R.layout.fragment_user_media, container, false);

		init(layoutView);

		if (Utils.chkStatus()) {
			mediaItemAdapter = null;
			serviceToGetAll();
			/*if(mediaItemAdapter==null){
				serviceToGetAll();	
			}else{
				mediaListView.setAdapter(mediaItemAdapter);
			}*/

		}else{
			UIHelperUtil.showToastMessage(getString(R.string.toast_no_network));
		}

		return layoutView;
	}

	private void init(View layoutView){
		mediaListView = (ListView) layoutView.findViewById(R.id.media_list);
		footerLayout = (RelativeLayout) layoutView
				.findViewById(R.id.loading_footer_layout);

		handler = new Handler();

		//TODO: Change title parameters

		//Reference to Title bar components 
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		backBtn = (ImageView) activity.findViewById(R.id.back_iv);
		settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);
		searchHeaderBtn = (ImageView)activity.findViewById(R.id.search_iv);
		addEventBtn = (ImageView)activity.findViewById(R.id.add_an_event_iv);
		tvEventType = (TextView) activity.findViewById(R.id.title_bar_tv_event_type);
		search_header_llyt = (RelativeLayout) activity.findViewById(R.id.search_header_llyt);

		backBtn.setOnClickListener(this);

		Bundle args = getArguments();		
		//If follower or following user is selected.
		if (args != null) {
			user_id = args.getString(Constants.userProf_userID);
			first_name = args.getString(Constants.userProf_userName);
		}
	}

	@Override
	public void onStart() {
		super.onStart();		

		//Updating title bar fields
		if(user_id.equalsIgnoreCase(SharedPreferencesUtils.getUserId())){
			title.setText("MY "+Constants.userProf_posts);	
		}else{
			title.setText(first_name +" "+Constants.userProf_posts);
		}

		title.setVisibility(View.VISIBLE);
		backBtn.setVisibility(View.VISIBLE);		
		settingBtn.setVisibility(View.GONE);
		tvEventType.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		search_header_llyt.setVisibility(View.GONE);

		mediaListView.removeFooterView(footerLayout);
		mediaListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					if (mediaListView.getLastVisiblePosition() >= mediaListView
							.getCount() - 1 ) {
						if(reload){
							footerLayout.setVisibility(View.VISIBLE);
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									if (Utils.chkStatus()) {
										pageCount += 1;
										footerLayout.setVisibility(View.GONE);
										mediaListView
										.removeFooterView(footerLayout);

										if (reload) {
											//footerLayout.setVisibility(View.VISIBLE);
											serviceToGetAll();
										} else {
											/*UIHelperUtil
											.showToastMessage("The list is up to date.");*/
											UIHelperUtil
											.showToastMessage("This is the Last Post");
											footerLayout.setVisibility(View.GONE);
											mediaListView.removeFooterView(footerLayout);
										}
									} else {
										UIHelperUtil
										.showToastMessage(getString(R.string.toast_no_network));
									}

								}
							}, 750);
						}else{
							/*UIHelperUtil
							.showToastMessage("The list is up to date.");*/
							UIHelperUtil
							.showToastMessage("This is the Last Post");
							footerLayout.setVisibility(View.GONE);
							mediaListView.removeFooterView(footerLayout);
						}

					} else {
						mediaListView.removeFooterView(footerLayout);
						footerLayout.setVisibility(View.GONE);
					}
					Log.d("",
							"last pos:"
									+ mediaListView.getLastVisiblePosition()
									+ " : " + mediaListView.getCount() + " : "
									+ pageCount + " : " + reload);
				} else {
					mediaListView.removeFooterView(footerLayout);
					footerLayout.setVisibility(View.GONE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
	}

	//service to get user photos information
	private void serviceToGetAll() {
		final String url = Constants.common_url + getString(R.string.get_profile_all);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
		nameValuePairs.add(new BasicNameValuePair("login_user_id", SharedPreferencesUtils.getUserId()));
		nameValuePairs.add(new BasicNameValuePair("page_id", pageCount+""));
		JsonParser.callBackgroundService(url, nameValuePairs, new JsonServiceListener() {
			ProgressDialog pd;
			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(activity);
				pd.show();
				pd.setMessage("Loading...");
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
			}

			@Override
			public void hideProgress() {
				super.hideProgress();
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
			}

			@Override
			public void parseJsonResponse(String jsonResponse) {
				if (jsonResponse!= null) {
					Log.v("", "URL: " + url +" all details RESPONSE: " + jsonResponse);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject.getJSONObject(Constants.TAG_RESPONSE);
						String responseStr = resObj.getString(Constants.TAG_RESPONSE_INFO);
						List<TeamInfo> myteamList = new ArrayList<TeamInfo>();
						if (responseStr!=null && responseStr.equalsIgnoreCase(Constants.TAG_SUCCESS)){
							try{
								//Fetching liked users list
								JSONArray profileCastsJSONArray = resObj.getJSONArray(Constants.TAG_MEDIA_ALL_INFO);

								ArrayList<ProfileMediaInfo> profileAllInfoList = new ArrayList<ProfileMediaInfo>();
								for (int i = 0; i < profileCastsJSONArray.length(); i++){
									JSONObject profileCastObj = profileCastsJSONArray.getJSONObject(i);
									ProfileMediaInfo tmpPhoto = new ProfileMediaInfo();
									tmpPhoto.setMediaType(profileCastObj.getString(Constants.TAG_MEDIA_TYPE));
									tmpPhoto.setMediaEventId(profileCastObj.getString(Constants.TAG_MEDIA_EVENT_ID));
									tmpPhoto.setMediaIsStubhubEvent(profileCastObj.getString(Constants.TAG_MEDIA_EVENT_IS_STUBHUB));
									tmpPhoto.setMediaEventTitle(profileCastObj.getString(Constants.TAG_MEDIA_EVENT_TITLE));
									tmpPhoto.setMediaEventType(profileCastObj.getString(Constants.TAG_MEDIA_EVENT_TYPE));
									tmpPhoto.setMediaSportName(profileCastObj.getString(Constants.TAG_MEDIA_SPORT_NAME));
									tmpPhoto.setMediaThumbUrl(profileCastObj.getString(Constants.TAG_MEDIA_THUMB_URL));
									if(!TextUtils.isEmpty(profileCastObj.getString(Constants.TAG_MEDIA_LOCATION)) && profileCastObj.getString(Constants.TAG_MEDIA_LOCATION).equalsIgnoreCase("null")){
										tmpPhoto.setMediaLocation("-");
									}else{
										tmpPhoto.setMediaLocation(profileCastObj.getString(Constants.TAG_MEDIA_LOCATION));	
									}
									
									tmpPhoto.setMediaCaption(profileCastObj.getString(Constants.TAG_MEDIA_CAPTION));
									tmpPhoto.setMediaId(profileCastObj.getString(Constants.TAG_MEDIA_ID));
									tmpPhoto.setMediaUrl(profileCastObj.getString(Constants.TAG_MEDIA_URL));
									if (profileCastObj.getString(Constants.TAG_MEDIA_IS_USER_LIKED).equalsIgnoreCase("1")) {
										tmpPhoto.setIsUserLiked("true");
									}else{
										tmpPhoto.setIsUserLiked("false");
									}
									tmpPhoto.setMediaLikeCount(profileCastObj.getString(Constants.TAG_MEDIA_LIKE_COUNT));
									tmpPhoto.setMediaCommentCount(profileCastObj.getString(Constants.TAG_MEDIA_COMMENT_COUNT));
									if(profileCastObj.getString(Constants.TAG_MEDIA_CATEGORY).equalsIgnoreCase("user_photos_data")){
										if(profileCastObj.getString(Constants.TAG_EVENT_MEDIA_LATEST_COMMENT) != null && profileCastObj.getString(Constants.TAG_EVENT_MEDIA_LATEST_COMMENT).equalsIgnoreCase("no comment")){
											tmpPhoto.setLatestComment(profileCastObj.getString(Constants.TAG_EVENT_MEDIA_LATEST_COMMENT));
										}else if(profileCastObj.getJSONObject(Constants.TAG_EVENT_MEDIA_LATEST_COMMENT) != null){
											JSONObject latestCommentObj = profileCastObj.getJSONObject(Constants.TAG_EVENT_MEDIA_LATEST_COMMENT);
											tmpPhoto.setLatestComment(latestCommentObj.getString("latest_comment"));
											tmpPhoto.setLatestCommentBy(latestCommentObj.getString("latest_commented_by"));
										}
									}
									
									//determines info is of cast type
									tmpPhoto.setMediaCategory(profileCastObj.getString(Constants.TAG_MEDIA_CATEGORY));
									profileAllInfoList.add(tmpPhoto);
								}
								Log.d(Constants.logUrl,"profile all: "+profileAllInfoList.size());

								if(mediaItemAdapter==null){
									mediaItemAdapter = new ProfileMediaItemAdapter(activity, user_id,first_name, profileAllInfoList);
									mediaListView.setAdapter(mediaItemAdapter);

								}else
								{
									mediaItemAdapter.updateMediaList(profileAllInfoList);
									mediaItemAdapter.notifyDataSetChanged();
								}

								if (profileAllInfoList.size() >= 10) {
									reload = true;
									mediaListView.addFooterView(footerLayout);
								} else {
									reload = false;
								}

							}catch(JSONException ex){
								//When no user exist
							}
						}else if(responseStr!=null && responseStr.equalsIgnoreCase(Constants.TAG_FAILURE)){
							UIHelperUtil.showToastMessage("No updates found");
						}else{
							UIHelperUtil.showToastMessage(getString(R.string.service_toast));
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					UIHelperUtil.showToastMessage(getString(R.string.service_toast));
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.back_iv:
			activity.onBackPressed();
			break;
		}	
	}
}
