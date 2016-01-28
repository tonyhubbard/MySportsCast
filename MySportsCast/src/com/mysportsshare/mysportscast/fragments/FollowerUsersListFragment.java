package com.mysportsshare.mysportscast.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.adapters.FollowerUserAdapter;
import com.mysportsshare.mysportscast.adapters.FollowingUserAdapter;
import com.mysportsshare.mysportscast.models.EventInfo;
import com.mysportsshare.mysportscast.models.FollowerUserInfo;
import com.mysportsshare.mysportscast.models.FollowingUserInfo;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.AdapterCallback;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.DataPassListener;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.mysportsshare.mysportscast.activity.CalendarEventsActivity;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.activity.ShowTagEventActivity;

public class FollowerUsersListFragment extends Fragment implements OnClickListener,AdapterCallback{


	//Title bar components
	TextView title;
	ImageView backBtn;
	ImageView settingBtn;
	private ImageView addEventBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private RelativeLayout search_header_llyt;

	//UI components
	TextView followerListViewText;
	ListView followerUsersList;

	//Holds the call back listener to pass data

	//Holds the call back lister for button pressed in listview
	AdapterCallback btnCallback;	
	int pos; //Holds the position of clicked item in the followingUsersList
	FollowerUserAdapter followersAdapter;
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
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layoutView = inflater.inflate(R.layout.fragment_followerlist, container,false);

		//Initialize layout views
		init(layoutView);

		return layoutView;
	}

	private void init(View layoutView){
		try {
			title =(TextView) activity.findViewById(R.id.title_bar_tv);
			backBtn = (ImageView) activity.findViewById(R.id.back_iv);
			settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);
			searchHeaderBtn = (ImageView)activity.findViewById(R.id.search_iv);
			addEventBtn = (ImageView)activity.findViewById(R.id.add_an_event_iv);
			tvEventType = (TextView) activity.findViewById(R.id.title_bar_tv_event_type);
			search_header_llyt = (RelativeLayout) activity.findViewById(R.id.search_header_llyt);	

			followerUsersList = (ListView)layoutView.findViewById(R.id.followers_list);
			//To display the selected user profile
			followerUsersList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					FollowerUserAdapter.FollowerHolder obj = (FollowerUserAdapter.FollowerHolder) view.getTag();
					UserProfileFragment activeFragment = new UserProfileFragment();
					Bundle arg = new Bundle();
					arg.putString(Constants.dataReceive, obj.getUserID());
					activeFragment.setArguments(arg);
					displayOnActivity(activeFragment);
				}
			});
			backBtn.setOnClickListener(this);

			followerListViewText = (TextView) layoutView.findViewById(R.id.follower_listViewText);
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}
	private void displayOnActivity(Fragment activeFragment) {
		if(activity.getClass().getSimpleName().equalsIgnoreCase("MainActivity")){
			//false as true by koti
			((MainActivity)activity).pushFragments(((MainActivity)activity).getmCurrentTab(), activeFragment, false,true);
		}else if(activity.getClass().getSimpleName().equalsIgnoreCase("CalendarEventsActivity")){
			//stack & displayed on the Calendar screen						
			((CalendarEventsActivity)activity).push(activeFragment);
		}else if(activity.getClass().getSimpleName().equalsIgnoreCase("ShowTagEventActivity")){
			//stack & displayed on the Calendar screen						
			((ShowTagEventActivity)activity).push(activeFragment);
		}
	}
	private void popFromActivity() {
		if(activity.getClass().getSimpleName().equalsIgnoreCase("MainActivity")){
			((MainActivity)activity).popFragments();
		}else if(activity.getClass().getSimpleName().equalsIgnoreCase("CalendarEventsActivity")){
			//stack & displayed on the Calendar screen						
			((CalendarEventsActivity)activity).popFragments();
		}else if(activity.getClass().getSimpleName().equalsIgnoreCase("ShowTagEventActivity")){
			//stack & displayed on the Calendar screen						
			((ShowTagEventActivity)activity).popFragments();
		}
	}

	@Override
	public void onResume() {
		super.onResume();


	}

	@Override
	public void onStart() {
		super.onStart();

		title.setText("FOLLOWER");
		settingBtn.setVisibility(View.INVISIBLE);
		backBtn.setVisibility(View.VISIBLE);
		tvEventType.setVisibility(View.INVISIBLE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		search_header_llyt.setVisibility(View.GONE);

		Bundle args = getArguments();
		//If follower or following user is selected.
		if (args != null) {
			//Display selected user screen	    	
			getFollowerUsers(args.getString(Constants.dataReceive));
		}else{
			//Display profile user's screen
			//Get followers, following & check-ins values from server 
			//Displays followers users, following users & checked-event counts on the screen
			getFollowerUsers(SharedPreferencesUtils.getUserId());
		}		
	}

	@Override
	public void onStop() {
		super.onStop();
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.back_iv){
			activity.onBackPressed();
		}
	}

	/****************************SERVER INTERFACES STARTS*************************/

	//Purpose:  Fetches followers of specified user
	public void getFollowerUsers(String userID){
		//Saves following users list
		final List<FollowerUserInfo> followersList = new ArrayList<FollowerUserInfo>();

		//Url for getting following friends info
		String url =  Constants.common_url + getString(R.string.get_follower_users);

		//input data to web service
		List<NameValuePair> inputVals = new ArrayList<NameValuePair>() ;
		inputVals.add(new BasicNameValuePair(Constants.FollowerUsersList_userID,userID));
		inputVals.add(new BasicNameValuePair(Constants.userProf_logged_userID,SharedPreferencesUtils.getUserId()));

		//Send req to url & handle the result 
		JsonParser.callBackgroundService(url, inputVals, new JsonServiceListener() {

			@Override
			public void showProgress() {
				super.showProgress();
			}

			@Override
			public void hideProgress() {
				super.hideProgress();
			}

			@Override
			public void parseJsonResponse(String jsonResponse) {
				try {

					if (jsonResponse!= null) {
						Log.d(Constants.logUrl," Followers Service RESPONSE: " + jsonResponse);
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(jsonResponse);
							JSONObject resObj = jsonObject.getJSONObject(Constants.webserver_response);
							String responseStr = resObj.getString(Constants.webserver_responseInfo);
							if (responseStr!=null && responseStr.equalsIgnoreCase(Constants.webserver_response_success)){

								followerListViewText.setVisibility(View.INVISIBLE);
								followerUsersList.setVisibility(View.VISIBLE);

								//Update UI on the screen
								JSONArray followerUsersObjs = resObj.getJSONArray(Constants.FollowerUsersList_InfoObj);																		

								//Displaying user name
								if(followerUsersObjs != null){
									for(int i = 0; i < followerUsersObjs.length(); i++){
										FollowerUserInfo followerUser = new FollowerUserInfo();
										followerUser.setUserName(followerUsersObjs.getJSONObject(i).getString(Constants.FollowerUsersList_userName).toUpperCase());
										followerUser.setUserID(followerUsersObjs.getJSONObject(i).getString(Constants.FollowerUsersList_userID));
										followerUser.setPhoto(followerUsersObjs.getJSONObject(i).getString(Constants.FollowerUsersList_userPic));
										followerUser.setProfile_visibility(followerUsersObjs.getJSONObject(i).getString(Constants.userProf_visibility));
										followerUser.setFollowStatus(followerUsersObjs.getJSONObject(i).getString(Constants.userProf_follow_status));
										followerUser.setItemPosition(i);
										followersList.add(followerUser);
									}
									followersAdapter = new FollowerUserAdapter(activity, followersList,FollowerUsersListFragment.this);
									followerUsersList.setAdapter(followersAdapter);

								}else{
									UIHelperUtil.showToastMessage("No followers found");
								}

							}else if (responseStr!=null && responseStr.equalsIgnoreCase(Constants.webserver_response_fail)){
								followerListViewText.setVisibility(View.VISIBLE);
								followerUsersList.setVisibility(View.INVISIBLE);
							}
							else{
								//Prompt server error message to the user
								UIHelperUtil.showToastMessage(activity.getString(R.string.service_toast));
							}

						}catch(Exception ex){
							//							if(BuildConfig.DEBUG){
							Log.e(Constants.logUrl,ex.getMessage());
							//							}
						}
					}
				} catch (Exception ex) {
					if(BuildConfig.DEBUG){
						Log.e(Constants.logUrl,ex.getMessage());
					}
				}
			}
		});
	}

	/*Purpose:  Updates the following status of userID
	 * Method: updateFollowStatus
	 * Input:
	 *       userID: Logged user wants to change the follow/following status of userID's user
	 *       followStatus: follow/unfollow status
	 * Output:
	 *         returns success/failure status of updation.
	 * */
	public void updateFollowStatus(String userID, String followStatus,String userPrivacy ){
		//Saves following users list
		final List<FollowingUserInfo> followersList = new ArrayList<FollowingUserInfo>();

		//Url for getting following friends info
		String url =  Constants.common_url + getString(R.string.updateFollowStatus);

		//input data to web service
		List<NameValuePair> inputVals = new ArrayList<NameValuePair>() ;
		inputVals.add(new BasicNameValuePair(Constants.UpdateFollowStatus_ReqUserID,SharedPreferencesUtils.getUserId()));
		inputVals.add(new BasicNameValuePair(Constants.UpdateFollowStatus_ReqFollowUserID,userID));
		inputVals.add(new BasicNameValuePair(Constants.UpdateFollowStatus_ReqStatus,followStatus));
		inputVals.add(new BasicNameValuePair(Constants.userProf_visibility,userPrivacy));

		//Send req to url & handle the result 
		JsonParser.callBackgroundService(url, inputVals, new JsonServiceListener() {

			@Override
			public void showProgress() {
				super.showProgress();
			}

			@Override
			public void hideProgress() {
				super.hideProgress();


			}

			@Override
			public void parseJsonResponse(String jsonResponse) {
				try {
					if (jsonResponse!= null) {
						Log.v(Constants.logUrl," Update Follow Status Service RESPONSE: " + jsonResponse);
						JSONObject jsonObject;						
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject.getJSONObject(Constants.webserver_response);
						String responseStr = resObj.getString(Constants.webserver_responseInfo);
						if (responseStr!=null && responseStr.equalsIgnoreCase(Constants.webserver_response_success)){
							onStatusReturn(resObj.getString(Constants.userProf_follow_status));
						}else{
							onStatusReturn("");
						}
					}else{
						onStatusReturn("");
					}
				} catch (Exception ex) {
					if(BuildConfig.DEBUG){
						Log.e(Constants.logUrl,ex.getMessage());
					}
				}
			}
		});
	}
	/****************************SERVER INTERFACES ENDS*************************/

	/**********************LIST VIEW ADAPTERS CALL BACK METHODS - START********************/
	@Override
	public void onClickButton(String userID, int itemPos, String status,String userPrivacy) {
		// TODO Auto-generated method stub
		//Persists current selected user row number
		this.pos = itemPos;

		//Update follow/unfollow status of userID
		updateFollowStatus(userID, status,userPrivacy);
	}

	@Override
	public void onStatusReturn(String message) {
		if(followersAdapter!=null) {
			if(message.trim().length()>0)
				followersAdapter.updateUserAtPosition(pos,message);
		}
	}
	/**********************LIST VIEW ADAPTERS CALL BACK METHODS - ENDS********************/

}
