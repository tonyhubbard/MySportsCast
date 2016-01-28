package com.mysportsshare.mysportscast.adapters;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CalendarEventsActivity;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.activity.ShowTagEventActivity;
import com.mysportsshare.mysportscast.fragments.UserProfileFragment;
import com.mysportsshare.mysportscast.fragments.VideosPlayFragment;
import com.mysportsshare.mysportscast.models.Comments;
import com.mysportsshare.mysportscast.models.EventInfo;
import com.mysportsshare.mysportscast.models.EventInfo.EventMedia;
import com.mysportsshare.mysportscast.models.TeamInfo;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.CustomLog;
import com.mysportsshare.mysportscast.utils.HelperMethods;
import com.mysportsshare.mysportscast.utils.HelperMethods.TwitterCallback;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.SocialNetworkingUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class EventDetailsAdapter extends BaseAdapter implements OnClickListener {

	private Context context;
	private HashMap<String, ArrayList<EventInfo.EventMedia>> eventDetailsMap;
	private ArrayList<EventInfo.EventMedia> eventDetailsList = new ArrayList<EventInfo.EventMedia>();

	// Comments dialog views
	ListView commentUsersListView;
	TextView commentListStatus;
	EditText commentEditText;
	ImageView commentPostBtn;
	CommentsAdapter cmtAdapter;

	Handler m_Handler;
	Runnable mRunnable;
	Dialog altDlg;

	//Event media
	private EventMedia globleEventMedia;
	private String desc = "";
	private String globleLink = "";
	String eventName;
	long lastUpdateTime = 0;
	

	public EventDetailsAdapter(Context context, 
			HashMap<String, ArrayList<EventInfo.EventMedia>> eventDataMap,String eventName) {
		this.context = context;
		if (eventDataMap != null) {
			this.eventDetailsMap = eventDataMap;
			for (Map.Entry<String, ArrayList<EventInfo.EventMedia>> element : eventDetailsMap
					.entrySet()) {
				eventDetailsList.addAll(eventDetailsMap.get(element.getKey()));
			}
		}
		this.eventName = eventName;

	}
	private void startUpdateUI() {
		//To update UI only after closing comments dialog 
		m_Handler = new Handler();
		mRunnable = new Runnable(){
			@Override
			public void run() {
				if(altDlg!=null){
					if(altDlg.isShowing()){
						m_Handler.postDelayed(mRunnable, 1000);// move this inside the run method		
					}else{
						notifyDataSetChanged();		
					}
				}
			} 
		};
		new Thread(mRunnable).start();
//		mRunnable.run();
	}
	public void stopUIUpdate(){
		if(m_Handler!=null){
			m_Handler.removeCallbacks(mRunnable);
		}
	}
	private void commentUpdated() {
		//To update 
		mRunnable = new Runnable(){
			@Override
			public void run() {
				notifyDataSetChanged();
			} 
		};
		mRunnable.run();
	}
	@Override
	public int getCount() {
		return eventDetailsList.size();
	}

	@Override
	public Object getItem(int position) {
		return eventDetailsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		final EventInfo.EventMedia eventMedia = eventDetailsList.get(position);
		//		eventMedia = eventDetailsList.get(position);
		EventDetailsHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_event_details,
					parent, false);
			holder = new EventDetailsHolder();
			holder.tvMediaPerson = (TextView) convertView
					.findViewById(R.id.event_details_media_person);
			holder.ImgCheer = (ImageView) convertView
					.findViewById(R.id.event_details_comment_media_cheers);
			holder.tvCheers = (TextView) convertView
					.findViewById(R.id.event_details_comment_media_cheers_tv);
			holder.ImgComment = (ImageView) convertView
					.findViewById(R.id.event_details_comment_media_status);
			holder.tvComment = (TextView) convertView
					.findViewById(R.id.event_details_comment_media_total_comments);
			holder.tvCast = (TextView) convertView
					.findViewById(R.id.event_details_cast);
			holder.imgMedia = (ImageView) convertView
					.findViewById(R.id.event_details_comment_media_pic);
			holder.imgMediaComment = (TextView) convertView.findViewById(R.id.latest_comment_tv);
			holder.videoMedia = (ImageView) convertView
					.findViewById(R.id.event_details_comment_media_video);
			holder.imgProfilePic = (ImageView) convertView
					.findViewById(R.id.event_details_comment_profile_pic);
			holder.imgMediaShare = (ImageView) convertView
					.findViewById(R.id.event_details_comment_media_share);

			holder.media_video_play = (ImageView) convertView
					.findViewById(R.id.media_video_play);
			convertView.setTag(holder);
		} else {
			holder = (EventDetailsHolder) convertView.getTag();
		}

		if(!eventMedia.getMediaFirstName().isEmpty()){
			holder.tvMediaPerson.setText(eventMedia.getMediaFirstName());
		}else{
			holder.tvMediaPerson.setText(context.getString(R.string.no_name).toString());
		}
		
		BitmapUtils.setImages(eventMedia.getMediaProfileImage(),
				holder.imgProfilePic, R.drawable.default_event_pic);
		// Updating comments count
		if (eventMedia.getCommentsCnt() != null
				&& eventMedia.getCommentsCnt().trim().length() > 0) {
				holder.tvComment.setText(eventMedia.getCommentsCnt() + " Comments");
		} else {
			holder.tvComment.setText("0 comments");
		}
		// Updating cheers count
		if (eventMedia.getMediaLikes() != null
				&& eventMedia.getMediaLikes().trim().length() > 0) {
			holder.tvCheers.setText(eventMedia.getMediaLikes() + " cheers");
		} else {
			holder.tvCheers.setText("0 cheers");
		}
		// Updating user liked to event-media img
		if (eventMedia.getIsUserLiked() != null
				&& eventMedia.getIsUserLiked().trim().equalsIgnoreCase("true")) {
			// TODO: need to change image to new one
			holder.ImgCheer.setImageResource(R.drawable.mega_phone_cheer);
		} else {
			holder.ImgCheer.setImageResource(R.drawable.mega_phone);
		}
		if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[3])) {
			holder.imgMedia.setVisibility(View.GONE);
			holder.imgMediaComment.setVisibility(View.GONE);
			holder.videoMedia.setVisibility(View.GONE);
			holder.media_video_play.setVisibility(View.GONE);
			holder.tvCast.setVisibility(View.VISIBLE);
			holder.tvCast.setText(eventMedia.getMediaData());
			System.out.println("eventMedia: " + eventMedia.getMediaData());
		} else if (eventMedia.getMediaType()
				.equalsIgnoreCase(Constants.KEYs[2])) {
			holder.imgMedia.setVisibility(View.GONE);
			holder.imgMediaComment.setVisibility(View.GONE);
			holder.videoMedia.setVisibility(View.VISIBLE);
			holder.media_video_play.setVisibility(View.VISIBLE);
			holder.media_video_play.setOnClickListener(this);
			holder.media_video_play.setTag(eventMedia);
			holder.tvCast.setVisibility(View.GONE);
			// TODO: Play video(it may be youtube or normal video)

			//			  Uri vidUri = Uri.parse(eventMedia.getMediaPhotoUrl());
			//			  holder.videoMedia.setVideoURI(vidUri); holder.videoMedia.start();
			/*MediaController vidControl = new MediaController(context);
			  vidControl.setAnchorView(holder.videoMedia);
			  holder.videoMedia.setMediaController(vidControl);
			 */
			if((!TextUtils.isEmpty(eventMedia.getMediaPhotoUrl())) && (!eventMedia.getMediaPhotoUrl().equalsIgnoreCase("null"))){
//				Uri uri=Uri.parse(eventMedia.getMediaPhotoUrl());
				if(!TextUtils.isEmpty(eventMedia.getMediaThumbNail())){
					BitmapUtils.setImages(eventMedia.getMediaThumbNail(),
							holder.videoMedia, R.drawable.default_event_pic);
				}
				
//				holder.videoMedia.setVideoURI(uri);
//				holder.videoMedia.seekTo(100); //Display image at 0.1 sec
			}
		} else if (eventMedia.getMediaType()
				.equalsIgnoreCase(Constants.KEYs[1])) {
			holder.tvCast.setVisibility(View.GONE);
			holder.videoMedia.setVisibility(View.GONE);
			holder.media_video_play.setVisibility(View.GONE);
			holder.imgMedia.setVisibility(View.VISIBLE);
			if(!eventMedia.getLatestComment().equalsIgnoreCase("no comment")){
				holder.imgMediaComment.setVisibility(View.VISIBLE);
				if(!eventMedia.getLatestCommentedBy().isEmpty()){
					holder.imgMediaComment.setText(Html.fromHtml("<b>"+eventMedia.getLatestCommentedBy()+":</b> "+eventMedia.getLatestComment()+""));
				}else{
					holder.imgMediaComment.setText(Html.fromHtml("<b>"+context.getResources().getString(R.string.no_name)+":</b> "+eventMedia.getLatestComment()+""));
				}
				
			}else{
				holder.imgMediaComment.setVisibility(View.GONE);
			}
			BitmapUtils.setImages(eventMedia.getMediaPhotoUrl(),
					holder.imgMedia, R.drawable.default_event_pic);
		}

		holder.ImgCheer.setOnClickListener(this);
		holder.tvCheers.setOnClickListener(this);
		holder.tvComment.setOnClickListener(this);
		holder.imgMediaShare.setOnClickListener(this);
		holder.imgProfilePic.setOnClickListener(this);

		// Assigning event-media instance to the views for further communication
		holder.ImgCheer.setTag(eventMedia);
		holder.tvCheers.setTag(eventMedia);
		holder.imgMediaShare.setTag(eventMedia);
		holder.tvComment.setTag(eventMedia);
		holder.imgMediaShare.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showShareDialog("http://www.mysportsshare.com",eventMedia.getMediaPhotoUrl(),eventMedia);
			}
		});
		holder.imgProfilePic.setTag(eventMedia);
		return convertView;
	}

	// event details holder
	class EventDetailsHolder {
		private TextView tvMediaPerson;
		private ImageView ImgCheer;
		private TextView tvCheers;
		private ImageView ImgComment;
		private TextView tvComment;
		private TextView tvCast;
		private ImageView imgMedia;
		private TextView imgMediaComment;
		private ImageView imgProfilePic;
		private ImageView imgMediaShare;
		private ImageView videoMedia;
		private ImageView media_video_play;

		private TextView tvCommentPerson1;
		private TextView tvComment1;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.event_details_comment_media_cheers_tv:
			triggerEventMediaCheer(v);
			break;
		case R.id.event_details_comment_media_cheers:
			triggerEventMediaCheer(v);
			break;
		case R.id.event_details_comment_media_status:
			triggerCommentsDialogByView(v);
			break;
		case R.id.event_details_comment_media_total_comments:
			if(System.currentTimeMillis() - lastUpdateTime > 1000){
				lastUpdateTime = System.currentTimeMillis();
				triggerCommentsDialogByView(v);
			}
			
			break;
			/*case R.id.event_details_comment_media_share:

			 * if(v.getTag()!=null){ EventInfo.EventMedia eventInfo =
			 * (EventInfo.EventMedia)v.getTag();
			 * IntentUtils.shareWithMedia(context, "Sub-Sample",
			 * "Sub-Sample link"); }

			// UIHelperUtil.showToastMessage("Yet to implement..!");
			showShareDialog("Sub-Sample", "Sub-Sample link","");
			break;*/
		case R.id.event_details_comment_profile_pic:
			//show users profiles
			EventInfo.EventMedia tmp1 = (EventInfo.EventMedia) v.getTag();
			if(tmp1!=null){
				String userId = tmp1.getMediaUserId();
				displayUserProfile(userId);	
			}
			break;
		case R.id.media_video_play:
			triggerPlayVideo(v);
			break;
		}
	}

	private void triggerPlayVideo(View v) {
		EventInfo.EventMedia tmp1 = (EventInfo.EventMedia) v.getTag();
		Fragment profile_all_Fragment = new VideosPlayFragment();
		Bundle args10 = new Bundle();
		args10.putString(Constants.userProf_userID,SharedPreferencesUtils.getUserId());
		args10.putString(Constants.userProf_userName,SharedPreferencesUtils.getUserName());
		args10.putString(Constants.userProf_videoPath,tmp1.getMediaPhotoUrl());
		args10.putString(Constants.userProf_caption,tmp1.getMediaData());
		profile_all_Fragment.setArguments(args10);

		displayOnActivity(profile_all_Fragment); 
	}

	/*
	 * @Override public Object getChild(int groupPosition, int childPosition) {
	 * // TODO Auto-generated method stub // System.out.println("getChild: " +
	 * eventDetailsList
	 * .get(groupPosition).getMediaCommentsMap().get(childPosition).size() +
	 * ", "+ eventDetailsList.get(groupPosition)); return
	 * eventDetailsList.get(groupPosition
	 * ).getMediaCommentsMap().get(childPosition); }
	 * 
	 * @Override public long getChildId(int groupPosition, int childPosition) {
	 * // TODO Auto-generated method stub return childPosition; }
	 * 
	 * @Override public View getChildView(int groupPosition, int childPosition,
	 * boolean isLastChild, View convertView, ViewGroup parent) { // TODO
	 * Auto-generated method stub LayoutInflater inflater =
	 * LayoutInflater.from(context); EventInfo.EventMedia.EventMediaComments
	 * eventComments = (EventMediaComments) getChild(groupPosition,
	 * childPosition); EventDetailsHolder holder; if (convertView == null) {
	 * convertView = inflater.inflate(R.layout.list_child_item_event_details,
	 * parent, false); holder = new EventDetailsHolder();
	 * holder.tvCommentPerson1 = (TextView)
	 * convertView.findViewById(R.id.tvCommentPerson1); holder.tvComment1 =
	 * (TextView) convertView.findViewById(R.id.tvComment1);
	 * convertView.setTag(holder); } else { holder = (EventDetailsHolder)
	 * convertView.getTag(); }
	 * 
	 * //
	 * holder.tvCommentPerson1.setText(eventComments.getMediaCommentFirstName(
	 * )); // holder.tvComment1.setText(eventComments.getMediaCommentText());
	 * 
	 * 
	 * return convertView; }
	 * 
	 * @Override public int getChildrenCount(int groupPosition) { // TODO
	 * Auto-generated method stub // System.out.println("Child Count: " +
	 * eventDetailsList.get(groupPosition).getMediaCommentsMap().size()); return
	 * eventDetailsList.get(groupPosition).getMediaCommentsMap().size(); }
	 * 
	 * @Override public Object getGroup(int groupPosition) { // TODO
	 * Auto-generated method stub return eventDetailsList.get(groupPosition); }
	 * 
	 * @Override public int getGroupCount() { // TODO Auto-generated method stub
	 * return eventDetailsList.size(); }
	 * 
	 * @Override public long getGroupId(int groupPosition) { // TODO
	 * Auto-generated method stub return groupPosition; }
	 * 
	 * @Override public View getGroupView(int groupPosition, boolean isExpanded,
	 * View convertView, ViewGroup parent) { LayoutInflater inflater =
	 * LayoutInflater.from(context); EventInfo.EventMedia eventMedia =
	 * eventDetailsList.get(groupPosition); EventDetailsHolder holder; if
	 * (convertView == null) { convertView =
	 * inflater.inflate(R.layout.list_item_event_details, parent, false); holder
	 * = new EventDetailsHolder(); holder.tvMediaPerson = (TextView)
	 * convertView.findViewById(R.id.event_details_media_person);
	 * holder.tvCheers = (TextView)
	 * convertView.findViewById(R.id.event_details_comment_media_cheers_tv);
	 * holder.tvViews = (TextView)
	 * convertView.findViewById(R.id.event_details_comment_media_total_comments
	 * ); holder.imgMedia = (ImageView)
	 * convertView.findViewById(R.id.event_details_comment_media_pic);
	 * holder.imgProfilePic = (ImageView)
	 * convertView.findViewById(R.id.event_details_comment_profile_pic);
	 * holder.imgMediaShare = (ImageView)
	 * convertView.findViewById(R.id.event_details_comment_media_share);
	 * convertView.setTag(holder); } else { holder = (EventDetailsHolder)
	 * convertView.getTag(); }
	 * 
	 * holder.tvMediaPerson.setText(eventMedia.getMediaFirstName());
	 * BitmapUtils.setImages(eventMedia.getMediaProfileImage(),
	 * holder.imgProfilePic); if (eventMedia.getMediaCommentsMap()!=null &&
	 * eventMedia.getMediaCommentsMap().size() > 0) { //TODO
	 * for(Map.Entry<String, ArrayList<EventInfo.EventMedia.EventMediaComments>>
	 * element : eventMedia.getMediaCommentsMap().entrySet()) {
	 * System.out.println("List : " + element.getValue().size() +", "+
	 * element.getKey() +", "+element.getValue());
	 * holder.tvViews.setText(element.getValue().size() + " Comments"); } } //
	 * holder
	 * .tvCheers.setText(eventMedia.getMediaCommentsMap().get(holder).get(0).)
	 * 
	 * holder.tvCheers.setOnClickListener(this);
	 * holder.tvViews.setOnClickListener(this);
	 * holder.imgMediaShare.setOnClickListener(this); return convertView; }
	 * 
	 * @Override public boolean hasStableIds() { // TODO Auto-generated method
	 * stub return false; }
	 * 
	 * @Override public boolean isChildSelectable(int groupPosition, int
	 * childPosition) { // TODO Auto-generated method stub return true; }
	 */

	/**
	 * getting allBroadCasts EventsMedia, dont change this data.
	 * 
	 * @return
	 */
	public HashMap<String, ArrayList<EventInfo.EventMedia>> getResultDetails() {
		return eventDetailsMap;
	}

	/**
	 * assigning new data to arraylist not the map, be careful while changing
	 * 
	 * @param eventDataMap
	 */
	public void setResultDetails(
			HashMap<String, ArrayList<EventInfo.EventMedia>> eventDataMap) {
		System.out.println("Count OLD LIST: " + eventDetailsList.size());
		eventDetailsList.clear();
		Log.v("", "EVENTSSSS NEW LIST: " + eventDataMap.size());
		for (Map.Entry<String, ArrayList<EventInfo.EventMedia>> element : eventDataMap
				.entrySet()) {
			eventDetailsList.addAll(eventDataMap.get(element.getKey()));
			System.out.println("Count new LIST: " + eventDetailsList.size());
		}
		notifyDataSetChanged();
	}

	public void updateEventMediaCheerStatusOnScreen(
			EventInfo.EventMedia eventMedia, String status) {
		eventMedia.setIsUserLiked(status);
		notifyDataSetChanged();
	}

	public void updateEventMediaAdapter() {
		notifyDataSetChanged();
	}
	private void displayOnActivity(Fragment activeFragment) {
		if(context.getClass().getSimpleName().equalsIgnoreCase("MainActivity")){
			((MainActivity)context).pushFragments(((MainActivity)context).getmCurrentTab(), activeFragment, false,true);
		}else if(context.getClass().getSimpleName().equalsIgnoreCase("CalendarEventsActivity")){
			//stack & displayed on the Calendar screen						
			((CalendarEventsActivity)context).push(activeFragment);
		}else if(context.getClass().getSimpleName().equalsIgnoreCase("ShowTagEventActivity")){
			//stack & displayed on the Calendar screen						
			((ShowTagEventActivity)context).push(activeFragment);
		}
	}
	private void popFromActivity() {
		if(context.getClass().getSimpleName().equalsIgnoreCase("MainActivity")){
			((MainActivity)context).popFragments();
		}else if(context.getClass().getSimpleName().equalsIgnoreCase("CalendarEventsActivity")){
			//stack & displayed on the Calendar screen						
			((CalendarEventsActivity)context).popFragments();
		}else if(context.getClass().getSimpleName().equalsIgnoreCase("ShowTagEventActivity")){
			//stack & displayed on the Calendar screen						
			((ShowTagEventActivity)context).popFragments();
		}
	}

	public void triggerEventMediaCheer(View v) {
		EventInfo.EventMedia tmp1 = (EventInfo.EventMedia) v.getTag();
		if (tmp1 != null) {
			if (tmp1.getMediaType().equalsIgnoreCase(Constants.KEYs[3])) {
				// Update cheer staus for images & videos
				if (tmp1.getIsUserLiked().equalsIgnoreCase("false")) {
					// Send cheer status
					updateEventMediaCheerStatus(
							tmp1.getMediaUserId(),
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_CAST, "true", tmp1);
				} else {
					// Send un-cheer status
					updateEventMediaCheerStatus(
							tmp1.getMediaUserId(),
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_CAST, "false", tmp1);
				}
			} else {
				// Update cheer staus for images & videos
				if (tmp1.getIsUserLiked().equalsIgnoreCase("false")) {
					// Send cheer status
					updateEventMediaCheerStatus(
							tmp1.getMediaUserId(),
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_IMG, "true", tmp1);
				} else {
					// Send un-cheer status
					updateEventMediaCheerStatus(
							tmp1.getMediaUserId(),
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_IMG, "false", tmp1);
				}
			}
		} else {
			UIHelperUtil.showToastMessage("Unable to cheer!....");
		}
	}

	public void triggerCommentsDialogByView(View v) {
		EventInfo.EventMedia tmp1 = (EventInfo.EventMedia) v.getTag();
		if (tmp1 != null) {
			if (tmp1.getCommentsCnt().trim().length() > 0) {
				if (tmp1.getMediaType().equalsIgnoreCase(Constants.KEYs[3])) {
					// Fetching cast comments
					getCommentsList(tmp1.getMediaUserId(),
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_CAST, tmp1);
				} else {
					// Fetching images and photos comments
					getCommentsList(tmp1.getMediaUserId(),
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_IMG, tmp1);
				}

			} else {
				if (tmp1.getMediaType().equalsIgnoreCase(Constants.KEYs[3])) {
					// Fetching cast comments
					displayCommentsDialog(tmp1.getMediaUserId(),
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_CAST, tmp1);
				} else {
					// Fetching images and photos comments
					displayCommentsDialog(tmp1.getMediaUserId(),
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_IMG, tmp1);
				}
			}
		} else {
			UIHelperUtil.showToastMessage("Unable to download!....");
		}
	}

	//Display user profile
	public void displayUserProfile(String userId){
		if (userId != null) {
			UserProfileFragment activeFragment = new UserProfileFragment();
			Bundle arg = new Bundle();
			arg.putString(Constants.dataReceive, userId);
			activeFragment.setArguments(arg);
			if(context!=null){
				displayOnActivity(activeFragment);				
			}
		}	
	}

	//Display event media comments
	public void displayCommentsDialog(final String notifyUserId,
			final String mediaId, final String mediaType,
			final EventInfo.EventMedia eventMedia) {

		// Prepare comments dialog layout
		LayoutInflater inflater = LayoutInflater.from(context);
		View dialogView = inflater.inflate(R.layout.comment_layout, null);

		//Prepare display dialog window
		altDlg = new Dialog(context);
		altDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		altDlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		altDlg.setContentView(dialogView);

		int width = altDlg.getWindow().getWindowManager().getDefaultDisplay()
				.getWidth() - 10;
		int height = altDlg.getWindow().getWindowManager().getDefaultDisplay()
				.getHeight() - 10;

		altDlg.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		// bldr.setView(dialogView);
		commentListStatus = (TextView) dialogView
				.findViewById(R.id.commentListStatus);
		commentUsersListView = (ListView) dialogView
				.findViewById(R.id.commentUsersListView);
		ImageView cmtLayoutClose = (ImageView) dialogView
				.findViewById(R.id.commentsCloseBtn);

		if (cmtAdapter != null) {
			commentListStatus.setVisibility(View.GONE);
			commentUsersListView.setVisibility(View.VISIBLE);
			cmtAdapter.setReverseCommentsList();
			commentUsersListView.setAdapter(cmtAdapter);
		} else {
			commentListStatus.setVisibility(View.VISIBLE);
			commentUsersListView.setVisibility(View.GONE);
		}
		commentEditText = (EditText) dialogView
				.findViewById(R.id.commentInputEd);

		//Display commented user profile
		commentUsersListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				altDlg.dismiss();
				Comments comment =
						((CommentsAdapter.StatesViewHolder) v.getTag()).comment;
				String userId = comment.getMediaCommentUserId();
				displayUserProfile(userId);
			}
		});

		// Adds a new comment to the event-media
		commentPostBtn = (ImageView) dialogView
				.findViewById(R.id.commentPostBtn);
		commentPostBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Post the comment to the server
				if (commentEditText.getText().toString().trim().length() > 0) {
					PostComment(notifyUserId, mediaId, mediaType, commentEditText
							.getText().toString().trim(), eventMedia);
					// Hide the key board
					// InputMethodManager KeyboardMgr = (InputMethodManager)
					// context.getSystemService(Context.INPUT_METHOD_SERVICE);
					// KeyboardMgr.hideSoftInputFromWindow(commentEditText.getWindowToken(),
					// 0);

					// Clear input text
					commentEditText.setText("");
				}
			}
		});

		// Closes the dialog when press close button
		cmtLayoutClose = (ImageView) dialogView
				.findViewById(R.id.commentsCloseBtn);
		cmtLayoutClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getTag() != null) {
					Dialog altDlg = (Dialog) v.getTag();
					altDlg.dismiss();
				}
			}
		});

		altDlg.show();
		startUpdateUI();
		cmtLayoutClose.setTag(altDlg);
	}

	/******************** SERVER COMMUNICATION STARTS ***********************/

	/*
	 * Method: updateEventCheerStatus Des: Update the cheer status of an event
	 * media Input: userID: logged user id mediaId : present cheering media
	 * mediaType : Cheered to which media cheerStatus: Cheering or un-cheering
	 * status
	 */
	public void updateEventMediaCheerStatus(String creatorId, String mediaId,
			String mediaType, final String cheerStatus,
			final EventInfo.EventMedia eventMedia) {
		final String url = Constants.common_url
				+ context.getString(R.string.update_event_media_cheer);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_USER_ID, SharedPreferencesUtils.getUserId()));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_NOTIFY_USER_ID, creatorId));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_MEDIA_ID, mediaId));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_MEDIA_TYPE, mediaType));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_STATUS, cheerStatus));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.TAG_CATEGORY, Constants.TAG_CATEGORY_EVENT));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.TAG_CATEGORY_TYPE, eventMedia.getMediaType()));
		JsonParser.callBackgroundService(url, nameValuePairs,
				new JsonServiceListener() {
			@Override
			public void parseJsonResponse(String jsonResponse) {
				if (jsonResponse != null) {
					Log.v("", "URL: " + url
							+ " event details RESPONSE: "
							+ jsonResponse);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject
								.getJSONObject(Constants.TAG_RESPONSE);
						String responseStr = resObj
								.getString(Constants.TAG_RESPONSE_INFO);
						List<TeamInfo> myteamList = new ArrayList<TeamInfo>();
						if (responseStr != null
								&& responseStr
								.equalsIgnoreCase(Constants.TAG_SUCCESS)) {
							if (cheerStatus.equalsIgnoreCase("true")) {
								eventMedia.incMediaLikes();
							} else {
								eventMedia.decMediaLikes();
							}
							eventMedia.setIsUserLiked(cheerStatus);
							// Change the cheer icon status
							updateEventMediaAdapter();
						} else {
//							UIHelperUtil.showToastMessage("Server error!...");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					UIHelperUtil
					.showToastMessage("Something going wrong with server");
				}
			}
		});
	}

	/*
	 * Method: updateEventCheerStatus Des: Update the cheer status of an event
	 * media Input: userID: logged user id mediaId : present cheering media
	 * mediaType : Cheered to which media cheerStatus: Cheering or uncheering
	 * status
	 */
	public void getCommentsList(final String notifyUserId, final String mediaId,
			final String mediaType, final EventInfo.EventMedia eventMedia) {
		final String url = Constants.common_url
				+ context.getString(R.string.get_event_media_comment);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_MEDIA_ID, mediaId));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_MEDIA_TYPE, mediaType));
		JsonParser.callBackgroundService(url, nameValuePairs,
				new JsonServiceListener() {
			@Override
			public void parseJsonResponse(String jsonResponse) {
				if (jsonResponse != null) {
					Log.v("", "URL: " + url
							+ " event-media comments RESPONSE: "
							+ jsonResponse);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject
								.getJSONObject(Constants.TAG_RESPONSE);
						String responseStr = resObj
								.getString(Constants.TAG_RESPONSE_INFO);
						if (responseStr != null
								&& responseStr
								.equalsIgnoreCase(Constants.TAG_SUCCESS)) {
							// Fetch comments from server
							JSONArray cmtsJSONArray = resObj
									.getJSONArray(Constants.TAG_EVENT_MEDIA_COMMENTS_RES);
							List<Comments> cmtList = new ArrayList<Comments>();
							for (int i = 0; i < cmtsJSONArray.length(); i++) {
								JSONObject cmtJSON = cmtsJSONArray
										.getJSONObject(i);
								Comments cmtObj = new Comments();
								cmtObj.setMediaCommentFirstName(cmtJSON
										.getString(Constants.TAG_EVENT_MEDIA_COMMENT_USER_NAME));
								cmtObj.setMediaCommentId(cmtJSON
										.getString(Constants.TAG_EVENT_MEDIA_COMMENT_ID));
								cmtObj.setMediaCommentUserId(cmtJSON
										.getString(Constants.TAG_EVENT_MEDIA_COMMENT_USER_ID));
								cmtObj.setMediaCommentText(cmtJSON
										.getString(Constants.TAG_EVENT_MEDIA_COMMENT_TEXT));
								cmtObj.setMediaCommentProfileImage(cmtJSON
										.getString(Constants.TAG_EVENT_MEDIA_COMMENT_USER_IMG));
								cmtObj.setMediaCommentCreated(cmtJSON
										.getString(Constants.TAG_EVENT_MEDIA_COMMENT_CREATED));
								cmtList.add(cmtObj);
							}
							// Adding comments to adapter
							cmtAdapter = new CommentsAdapter(context,
									R.layout.comment_user_row,mediaType, eventMedia,cmtList);
							// Display comments on the screen
							displayCommentsDialog(notifyUserId, mediaId,
									mediaType, eventMedia);
						} else {
							cmtAdapter = null;
							// Display comments on the screen
							displayCommentsDialog(notifyUserId, mediaId,
									mediaType, eventMedia);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					UIHelperUtil
					.showToastMessage("Something going wrong with server");
				}
			}
		});
	}

	private void PostComment(final String notifyUserId, final String mediaId,
			final String mediaType, final String cmtText,
			final EventInfo.EventMedia eventMedia) {
		final String url = Constants.common_url
				+ context.getString(R.string.add_event_media_comment);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(
				Constants.TAG_EVENT_MEDIA_COMMENT_USER_ID, SharedPreferencesUtils.getUserId()));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_MEDIA_NOTIFY_USER_ID, notifyUserId));
		nameValuePairs.add(new BasicNameValuePair(Constants.TAG_EVENT_MEDIA_ID,
				mediaId));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.TAG_EVENT_MEDIA_TYPE, mediaType));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.TAG_EVENT_MEDIA_COMMENT_TEXT, cmtText));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.TAG_CATEGORY, Constants.TAG_CATEGORY_EVENT));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.TAG_CATEGORY_TYPE, eventMedia.getMediaType()));
		JsonParser.callBackgroundService(url, nameValuePairs,
				new JsonServiceListener() {
			@Override
			public void parseJsonResponse(String jsonResponse) {
				CustomLog.v("comment", "comment: " + jsonResponse);
				try {
					JSONObject jsonObject = new JSONObject(jsonResponse);
					if (jsonResponse != null) {
						JSONObject resObj = jsonObject
								.getJSONObject(Constants.TAG_RESPONSE);
						String responseStr = resObj
								.getString(Constants.TAG_RESPONSE_INFO);
						if (responseStr != null
								&& responseStr
								.equalsIgnoreCase(Constants.TAG_SUCCESS)) {
							// Update posted comment on the screen.
							Comments cmtObj = new Comments();
							cmtObj.setMediaCommentFirstName(SharedPreferencesUtils.capitalizeString(SharedPreferencesUtils
									.getUserName()));
							cmtObj.setMediaCommentProfileImage(SharedPreferencesUtils
									.getUserProfilePicPath());
							cmtObj.setMediaCommentId(resObj
									.getString(Constants.TAG_EVENT_MEDIA_COMMENT_ID));
							cmtObj.setMediaCommentUserId(SharedPreferencesUtils.getUserId());
							cmtObj.setMediaCommentText(cmtText);
							if (cmtAdapter == null) {
								List<Comments> cmtList = new ArrayList<Comments>();
								cmtList.add(cmtObj);
								cmtAdapter = new CommentsAdapter(
										context,
										R.layout.comment_user_row,
										mediaType,eventMedia,
										cmtList);
								commentListStatus
								.setVisibility(View.GONE);
								commentUsersListView
								.setVisibility(View.VISIBLE);
								commentUsersListView
								.setAdapter(cmtAdapter);
							} else {
								List<Comments> cmtList = cmtAdapter.getCommentsList();
								Collections.reverse(cmtList);
								cmtList.add(cmtObj);
								Collections.reverse(cmtList);
								cmtAdapter.setCommentsList(cmtList);
//								cmtAdapter.add(cmtObj);
							}
							eventMedia.incCommentsCnt();
							eventMedia.setLatestComment(cmtText);
							eventMedia.setLatestCommentedBy(SharedPreferencesUtils.capitalizeString(SharedPreferencesUtils
									.getUserName()));
							cmtAdapter.notifyDataSetChanged();
							updateEventMediaAdapter();
							commentUsersListView
							.setSelection(0);
						} else {
//							UIHelperUtil.showToastMessage("Server error!...");
						}
					} else {
						UIHelperUtil
						.showToastMessage("Something going wrong with server");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	/******************** SERVER COMMUNICATION ENDS ************************/

	/**
	 * Dialgo to show share dialog
	 */
	private void showShareDialog(final String link,final String eventPicUrl,final EventInfo.EventMedia eventMedia) {
		Log.d("","koti pic url "+eventPicUrl+" "+eventMedia.getMediaProfileImage());
		if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[3])) {
			//Share event cast info
			desc = "Add cast to an \'"+eventName+"\' event. ";
		}else if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[2])) {
			//Share event video info
			desc = "Add video to an \'"+eventName+"\' event. Video shared at "+eventMedia.getMediaPhotoUrl()+" For more details please visit http://www.mysportsshare.com";
		}else if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[1])) {
			//share event photo info
			desc = "Add photo to an \'"+eventName+"\' event. ";
		}

		final Dialog dialog = new Dialog(context);
		//		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_share);
		dialog.setTitle("Share event media via");
		dialog.setCancelable(true);
		Button btnFb = (Button) dialog.findViewById(R.id.fb_btn);
		btnFb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// UIHelper.showToastMessage(DetailsReviewsActivity.this,
				// "you click share button");
				if (Utils.chkStatus()) {
					/*if (SocialNetworkingUtils.isFacebookExist(context)) {
						SocialNetworkingUtils.onClickPostStatusUpdate(context,desc, link, eventPicUrl);
						Log.d("Tag", "Success! " + "existed");
					} else {*/
					Session session = Session.getActiveSession();
					if (session != null && session.isOpened()) {
						Log.d("Tag", "Success! " + "if");
						if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[3])) {
							//Share event cast info
							SocialNetworkingUtils.publishFeedDialog(desc, link, eventPicUrl, context);
							//							publishFeedDialog(desc,"",eventPicUrl,context);
						}else if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[2])) {
							//Video is not shared
							SocialNetworkingUtils.publishVideoFeedDialog(desc, link, eventMedia.getMediaPhotoUrl(), context);
						}else if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[1])) {
							//share event photo info
							SocialNetworkingUtils.publishFeedDialog(desc, link, eventPicUrl, context);
						}


					} else {
						Log.d("Tag", "Session! " + "else");
						Session.openActiveSession((Activity) context, true,
								statusCallback);
						globleEventMedia = eventMedia;
						globleLink = link;
						
					}
					//					}
				} else {
					UIHelperUtil.showToastMessage(context
							.getString(R.string.toast_no_network));
				}
				dialog.dismiss();
			}
		});

		Button btnReport = (Button) dialog.findViewById(R.id.report_btn);
		btnReport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//				UIHelperUtil.showToastMessage("Yet to implement");
				String absType = "";
				Log.d("","event media:"+eventMedia.getMediaType());

				if(eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[1])){
					absType = "ABS_USR_PHOTO";
				}else if(eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[2])){
					absType = "ABS_USR_VIDEO";
				}else if(eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[3])){
					absType = "ABS_EVENT_CAST";
				}

				Utils.reportAbuseService(context,dialog,absType, eventMedia.getMediaId());
			}
		});

		Button btnTwitter = (Button) dialog.findViewById(R.id.twitter_btn);
		btnTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences mSharedPref = context.getSharedPreferences(
						"Android_Twitter_Preferences", Context.MODE_PRIVATE);
				if (!Utils.chkStatus()) {
					Utils.networkAlertDialog(context, context.getResources()
							.getString(R.string.toast_no_network));
				} else	if (mSharedPref.getString(Constants.KEY_TWITTER_ACCESS_TOKEN,
						null) != null) {
					/*SocialNetworkingUtils.postingtoTwitter(v,
							(Activity) context, desc);*/

					if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[3])) {
						//Share event cast info

						HelperMethods.postToTwitter(context, ((Activity)context), desc, new TwitterCallback() {

							@Override
							public void onFinsihed(Boolean response) {
								Toast.makeText(context, "posted succeded", Toast.LENGTH_SHORT).show();
							}
						});

					}else if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[2])) {
						//Video is not shared

						HelperMethods.postToTwitter(context, ((Activity)context), desc, new TwitterCallback() {

							@Override
							public void onFinsihed(Boolean response) {
								Toast.makeText(context, "posted succeded", Toast.LENGTH_SHORT).show();
							}
						});

					}else if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[1])) {
						//share event photo info
						Log.d("","koti pic url "+eventPicUrl);
						new DownloadImageTaskForTwitter(dialog, desc, v)
						.execute(eventPicUrl);
					}


				} else {
					// Toast.makeText(getActivity(), "session is null",
					// Toast.LENGTH_SHORT).show();
//					SocialNetworkingUtils.signinTwitter(SocialNetworkingUtils.mTwitter,(Activity)context);
					SocialNetworkingUtils.signinTwitter(SocialNetworkingUtils.mTwitter, (Activity)context,eventMedia,desc);
					/*SocialNetworkingUtils.signinTwitter(v,
							SocialNetworkingUtils.mTwitter, desc,eventPicUrl,
							(Activity)context);*/
				}
				dialog.dismiss();
			}
		});
		Button btnInstagram = (Button) dialog.findViewById(R.id.instagram_btn);
		btnInstagram.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!Utils.chkStatus()) {
					Utils.networkAlertDialog(context, context.getResources()
							.getString(R.string.toast_no_network));
				} else {

					if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[3])) {
						//Share event cast info
						SocialNetworkingUtils.sharingToInstagram(context,desc);

					}else if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[2])) {
						//Video is not shared
						SocialNetworkingUtils.sharingToInstagram(context,desc);

					}else if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[1])) {
						//share event photo info
						new DownloadImageTaskForInstagram(dialog,desc).execute(eventPicUrl);
					}

				}

			}
		});

		dialog.show();
	}


	private com.facebook.Session.StatusCallback statusCallback = new StatusCallback() {

		@Override
		public void call(com.facebook.Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				session.removeCallback(statusCallback);
				facebookSessionExistedorNot();
				Log.i("", "Session Facebook is opened: ");

			} else {
				Log.i("", "Session Facebook is closed");
			}
		}
	};

	
	
	/**
	 * AsyncTask Downloading pic from urls for twitter.
	 * 
	 * @author koti
	 * 
	 */
	private class DownloadImageTaskForTwitter extends
	AsyncTask<String, Integer, Bitmap> {
		ProgressDialog pd;
		String socialImageUrl;
		Dialog myDialog;
		View view;
		File file;
		String desc;

		public DownloadImageTaskForTwitter(Dialog dialog,String sharedesc, View v) {
			myDialog = dialog;
			view = v;
			desc = sharedesc;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}

		protected Bitmap doInBackground(String... urls) {
			socialImageUrl = urls[0];
			Bitmap bmp = BitmapUtils.downloadImage(urls[0]);
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/twitter_post_pic");
			myDir.mkdirs();
			String fname = "event_pic.jpg";
			file = new File(myDir, fname);
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bmp;
		}

		protected void onPostExecute(Bitmap result) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			if (result != null) {
				myDialog.dismiss();
				try {
					SharedPreferences mSharedPref = context
							.getSharedPreferences(
									"Android_Twitter_Preferences",
									Context.MODE_PRIVATE);
					String filename = file.getAbsolutePath();
					if (mSharedPref.getString(
							Constants.KEY_TWITTER_ACCESS_TOKEN, null) != null) {
						// SocialNetworkingUtils.postingtoTwitter(v,activity,eventinfo.getSportName()+" "+eventinfo.getEventType()+" \""+eventinfo.getEventTitle()+"\" is going at "+
						// eventinfo.getEventAddress()+" on "+
						// eventinfo.getEventStartDate() + " "+
						// eventinfo.getEventStartTime()+".  For more details check out this link:   "+
						// eventinfo.getEventShareURL());
						// SocialNetworkingUtils.postingtoTwitter(v,activity,sharemsg);
						HelperMethods.postToTwitterWithImage(context,
								((Activity) context), filename, desc,
								new TwitterCallback() {
							@Override
							public void onFinsihed(Boolean response) {
								Toast.makeText(context,
										"posted succeded",
										Toast.LENGTH_SHORT).show();
							}
						});
					} else {
						// Toast.makeText(activity, "session is null",
						// Toast.LENGTH_SHORT).show();
						SocialNetworkingUtils.signinTwitter(view,
								SocialNetworkingUtils.mTwitter, desc,
								socialImageUrl, ((Activity) context));
					}
				} catch (Exception ex) {
					Toast.makeText(context, "Please try again",
							Toast.LENGTH_SHORT).show();
				}
			} else {
			}
		}
	}
	
	
	/**
	 * AsyncTask Downloading pic from url for instagram.
	 * 
	 * @author koti
	 * 
	 */
	private class DownloadImageTaskForInstagram extends
	AsyncTask<String, Integer, Bitmap> {
		ProgressDialog pd;
		Dialog myDialog;
		String shareMsg;
		File file;

		public DownloadImageTaskForInstagram(Dialog dialog,String desc) {
			myDialog = dialog;
			shareMsg = desc;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}

		protected Bitmap doInBackground(String... urls) {
			Bitmap bmp = BitmapUtils.downloadImage(urls[0]);
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/instagram_post_pic");
			myDir.mkdirs();
			String fname = "event_pic.jpg";
			file = new File(myDir, fname);
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bmp;
		}

		protected void onPostExecute(Bitmap result) {

			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			if (result != null) {
				myDialog.dismiss();
				try {
					String filename = file.getAbsolutePath();

					Intent intent = context.getPackageManager()
							.getLaunchIntentForPackage("com.instagram.android");
					if (intent != null) {
						Intent shareIntent = new Intent();
						shareIntent.setAction(Intent.ACTION_SEND);
						shareIntent.setPackage("com.instagram.android");
						try {
							shareIntent.putExtra(Intent.EXTRA_STREAM, Uri
									.parse(MediaStore.Images.Media.insertImage(
											context.getContentResolver(),
											filename, "", "")));
							shareIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						shareIntent.setType("image/jpeg");
						context.startActivity(shareIntent);
					} else {
						Toast.makeText(context, "Please install instagram app",
								Toast.LENGTH_SHORT).show();
						
					}

				} catch (Exception ex) {
					Toast.makeText(context, "Please try again",
							Toast.LENGTH_SHORT).show();
				}
			} else {
			}
		}
	}

	private void facebookSessionExistedorNot(){
		Session session1 = Session.getActiveSession();
		if (session1 != null && session1.isOpened()) {
			
			if (globleEventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[3])) {
				//Share event cast info
				SocialNetworkingUtils.publishFeedDialog(desc, globleLink, globleEventMedia.getMediaPhotoUrl(), context);
				//							publishFeedDialog(desc,"",eventPicUrl,context);
			}else if (globleEventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[2])) {
				//Video is not shared
				SocialNetworkingUtils.publishVideoFeedDialog(desc, globleLink, globleEventMedia.getMediaPhotoUrl(), context);
			}else if (globleEventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[1])) {
				//share event photo info
				SocialNetworkingUtils.publishFeedDialog(desc, globleLink, globleEventMedia.getMediaPhotoUrl(), context);
			}
			
		} else {
			
		}
	}
}
