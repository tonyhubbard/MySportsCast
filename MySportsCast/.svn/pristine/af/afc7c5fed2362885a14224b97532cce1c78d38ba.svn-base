package com.mysportsshare.mysportscast.adapters;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mysportsshare.mysportscast.fragments.EditACastFragment;
import com.mysportsshare.mysportscast.fragments.EditFilterImageBroadCastFragment;
import com.mysportsshare.mysportscast.fragments.EditFilterVideoBroadCastFragment;
import com.mysportsshare.mysportscast.fragments.UserProfileFragment;
import com.mysportsshare.mysportscast.fragments.VideosPlayFragment;
import com.mysportsshare.mysportscast.models.Comments;
import com.mysportsshare.mysportscast.models.ProfileMediaInfo;
import com.mysportsshare.mysportscast.models.TeamInfo;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.HelperMethods;
import com.mysportsshare.mysportscast.utils.HelperMethods.TwitterCallback;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.SocialNetworkingUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class ProfileMediaItemAdapter extends BaseAdapter implements OnClickListener {

	private static Activity context;
	private List<ProfileMediaInfo> media_list;
	private String userId;

	// Comments dialog views
	ListView commentUsersListView;
	TextView commentListStatus;
	EditText commentEditText;
	ImageView commentPostBtn;
	CommentsAdapter cmtAdapter;
	private String userName;

	Handler m_Handler;
	Runnable mRunnable;
	Dialog altDlg;
	private String shareString = "";
	private ProfileMediaInfo globalEventMedia;
	private String globleLink = "";
	private boolean isTagEvents = false;
	private long lastTimeUpdate;

	public ProfileMediaItemAdapter(Activity context, String userId, String userName, List<ProfileMediaInfo> media_list) {
		this.context = context;
		this.media_list = media_list;
		this.userId = userId;
		this.userName = userName;		
	}

	public ProfileMediaItemAdapter(Activity context, String userId, String userName, List<ProfileMediaInfo> media_list,boolean isTagEvents) {
		this.context = context;
		this.media_list = media_list;
		this.userId = userId;
		this.userName = userName;		
		this.isTagEvents = isTagEvents;
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
	@Override
	public int getCount() {
		return media_list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void updateMediaList(List<ProfileMediaInfo> media_list){
		if(media_list!=null){
			this.media_list.addAll(media_list);
		}
	}

	public void updateMediaAdapter() {
		notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		final ProfileMediaInfo mediaItem = media_list.get(position);

		mediaItemHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.user_profile_media_item,
					parent, false);
			holder = new mediaItemHolder();
			holder.setEvent_title_tv((TextView)convertView.findViewById(R.id.event_title_tv));
			holder.setSport_type_tv((TextView)convertView.findViewById(R.id.sport_type_tv));
			holder.setEvent_loc_tv((TextView)convertView.findViewById(R.id.event_loc_tv));
			holder.setCaption_tv((TextView)convertView.findViewById(R.id.caption_tv));
			holder.setMedia_pic((ImageView)convertView.findViewById(R.id.media_pic));
			holder.setMediaLatestComment((TextView)convertView.findViewById(R.id.latest_comment_tv));
			holder.setMedia_video((ImageView)convertView.findViewById(R.id.media_video));
			holder.setMedia_video_play((ImageView)convertView.findViewById(R.id.media_video_play));
			holder.setMedia_cast((TextView)convertView.findViewById(R.id.media_cast));
			holder.setMedia_cheer_layout((LinearLayout)convertView.findViewById(R.id.media_cheer_layout));
			holder.setMedia_comment_layout((LinearLayout)convertView.findViewById(R.id.media_comment_layout));
			holder.setCheers_tv_icon((ImageView)convertView.findViewById(R.id.cheers_tv_icon));
			holder.setCheers_tv((TextView)convertView.findViewById(R.id.cheers_tv));
			holder.setComment_tv_icon((ImageView)convertView.findViewById(R.id.comment_tv_icon));
			holder.setTvComment((TextView)convertView.findViewById(R.id.comment_tv));
			holder.setMedia_share_iv((ImageView)convertView.findViewById(R.id.media_share_iv));
			holder.setEditMedia((ImageView)convertView.findViewById(R.id.edt_media));
			holder.setDeleteMedia((ImageView)convertView.findViewById(R.id.delete_media));
			convertView.setTag(holder);
		}else{
			holder = (mediaItemHolder) convertView.getTag();
		}

		if(mediaItem.getMediaType().equalsIgnoreCase(Constants.TAG_MEDIA_TYPE_EVENT)){
			holder.getEvent_title_tv().setVisibility(View.VISIBLE);
			holder.getSport_type_tv().setVisibility(View.VISIBLE);
			holder.getEvent_loc_tv().setVisibility(View.VISIBLE);
			holder.getEvent_title_tv().setText(mediaItem.getMediaEventTitle());
			holder.getSport_type_tv().setText(mediaItem.getMediaSportName());
			holder.getEvent_loc_tv().setText(mediaItem.getMediaLocation());
		}else{
			holder.getEvent_title_tv().setVisibility(View.GONE);
			holder.getSport_type_tv().setVisibility(View.GONE);
			holder.getEvent_loc_tv().setVisibility(View.GONE);
		}


		if(mediaItem.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)){
			holder.getMedia_cast().setVisibility(View.VISIBLE);
			holder.getMedia_pic().setVisibility(View.GONE);
			holder.getMediaLatestComment().setVisibility(View.GONE);
			holder.getMedia_video().setVisibility(View.GONE);
			holder.getMedia_video_play().setVisibility(View.GONE);
			holder.getCaption_tv().setVisibility(View.GONE);
			holder.getMedia_cast().setText(mediaItem.getMediaCaption());
		}else if(mediaItem.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)){
			holder.getMedia_cast().setVisibility(View.GONE);
			holder.getMedia_pic().setVisibility(View.GONE);
			holder.getMediaLatestComment().setVisibility(View.GONE);
			holder.getMedia_video().setVisibility(View.VISIBLE);
			holder.getMedia_video_play().setVisibility(View.VISIBLE);
			holder.getCaption_tv().setVisibility(View.VISIBLE);			
			holder.getCaption_tv().setText(mediaItem.getMediaCaption());
			if((!TextUtils.isEmpty(mediaItem.getMediaUrl())) && (!mediaItem.getMediaUrl().equalsIgnoreCase("null"))){
				if(!TextUtils.isEmpty(mediaItem.getMediaThumbUrl())){
					BitmapUtils.setImages(mediaItem.getMediaThumbUrl(), holder.getMedia_video(), R.drawable.default_event_pic);
				}
				
				/*Uri uri=Uri.parse(mediaItem.getMediaUrl());
				holder.getMedia_video().setVideoURI(uri);
				holder.getMedia_video().seekTo(100); *///Display image at 0.1 sec
			}
		}else if(mediaItem.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)){
			holder.getMedia_cast().setVisibility(View.GONE);
			holder.getMedia_video().setVisibility(View.GONE);
			holder.getMedia_video_play().setVisibility(View.GONE);
			holder.getMedia_pic().setVisibility(View.VISIBLE);
			if(mediaItem.getLatestComment() != null){
				if(!mediaItem.getLatestComment().equalsIgnoreCase("no comment")){
					holder.getMediaLatestComment().setVisibility(View.VISIBLE);
					if(!mediaItem.getLatestCommentBy().isEmpty()){
						holder.getMediaLatestComment().setText(Html.fromHtml("<b>"+mediaItem.getLatestCommentBy()+":</b> "+mediaItem.getLatestComment()+""));
					}else{
						holder.getMediaLatestComment().setText(Html.fromHtml("<b>"+context.getResources().getString(R.string.no_name)+":</b> "+mediaItem.getLatestComment()+""));
					}
					
				}else{
					holder.getMediaLatestComment().setVisibility(View.GONE);
				}
			}
			
			holder.getCaption_tv().setVisibility(View.VISIBLE);
			holder.getCaption_tv().setText(mediaItem.getMediaCaption());
			if(!TextUtils.isEmpty(mediaItem.getMediaUrl())){
				BitmapUtils.setImages(mediaItem.getMediaUrl(), holder.getMedia_pic(), R.drawable.default_event_pic);	
			}

		}

		// Updating user liked to event-media img
		if (!TextUtils.isEmpty(mediaItem.getIsUserLiked())
				&& mediaItem.getIsUserLiked().equalsIgnoreCase("true")) {
			holder.getCheers_tv_icon().setImageResource(R.drawable.mega_phone_cheer);
		} else {
			holder.getCheers_tv_icon().setImageResource(R.drawable.mega_phone);
		}

		// Updating cheers count
		if (TextUtils.isEmpty(mediaItem.getMediaLikeCount())) {
			holder.getCheers_tv().setText("0 cheers");
		} else {
			holder.getCheers_tv().setText(mediaItem.getMediaLikeCount() + " cheers");
		}

		// Updating comments count
		if (TextUtils.isEmpty(mediaItem.getMediaCommentCount())) {
			holder.getTvComment().setText("0 comments");
		} else {
			holder.getTvComment().setText(mediaItem.getMediaCommentCount() + " comments");
		}

		if(userId.equalsIgnoreCase(SharedPreferencesUtils.getUserId()) && !isTagEvents){
			holder.getEditMedia().setVisibility(View.VISIBLE);	
			holder.getDeleteMedia().setVisibility(View.VISIBLE);
		}else{
			holder.getEditMedia().setVisibility(View.GONE);
			holder.getDeleteMedia().setVisibility(View.GONE);
		}

		//Assigning listeners to media entity
		holder.getMedia_cheer_layout().setOnClickListener(this);
		holder.getMedia_comment_layout().setOnClickListener(this);
		holder.getMedia_share_iv().setOnClickListener(this);
		holder.getMedia_video_play().setOnClickListener(this);
		holder.getEditMedia().setOnClickListener(this);
		holder.getDeleteMedia().setOnClickListener(this);

		holder.getMedia_cheer_layout().setTag(mediaItem);
		holder.getMedia_comment_layout().setTag(mediaItem);
		holder.getMedia_share_iv().setTag(mediaItem);
		holder.getMedia_video_play().setTag(mediaItem);
		holder.getEditMedia().setTag(mediaItem);
		holder.getDeleteMedia().setTag(mediaItem);
		if(userId.equalsIgnoreCase(SharedPreferencesUtils.getUserId())){
			holder.getMedia_share_iv().setVisibility(View.VISIBLE);	
		}else{
			holder.getMedia_share_iv().setVisibility(View.GONE);
		}
		return convertView;
	}

	// event details holder
	class mediaItemHolder {
		private TextView event_title_tv;
		private TextView sport_type_tv;
		private TextView event_loc_tv;
		private TextView caption_tv;
		private ImageView media_pic;
		private TextView mediaLatestComment;
		private ImageView media_video;
		private ImageView media_video_play;
		private TextView media_cast;
		private LinearLayout media_cheer_layout;
		private LinearLayout media_comment_layout;
		private ImageView cheers_tv_icon;
		private TextView cheers_tv;
		private ImageView comment_tv_icon;
		private TextView tvComment;
		private ImageView media_share_iv;
		private ImageView edit_iv;
		private ImageView delete_iv;

		public TextView getMediaLatestComment() {
			return mediaLatestComment;
		}

		public void setMediaLatestComment(TextView mediaLatestComment) {
			this.mediaLatestComment = mediaLatestComment;
		}
		
		public ImageView getEditMedia(){
			return edit_iv;
		}

		public void setEditMedia(ImageView edit_media){
			this.edit_iv = edit_media;
		}

		public ImageView getDeleteMedia(){
			return delete_iv;
		}

		public void setDeleteMedia(ImageView delete_media){
			this.delete_iv = delete_media;
		}

		public ImageView getMedia_video_play() {
			return media_video_play;
		}
		public void setMedia_video_play(ImageView media_video_play) {
			this.media_video_play = media_video_play;
		}
		public ImageView getMedia_share_iv() {
			return media_share_iv;
		}
		public void setMedia_share_iv(ImageView media_share_iv) {
			this.media_share_iv = media_share_iv;
		}
		public TextView getEvent_title_tv() {
			return event_title_tv;
		}
		public void setEvent_title_tv(TextView event_title_tv) {
			this.event_title_tv = event_title_tv;
		}
		public TextView getSport_type_tv() {
			return sport_type_tv;
		}
		public void setSport_type_tv(TextView sport_type_tv) {
			this.sport_type_tv = sport_type_tv;
		}
		public TextView getEvent_loc_tv() {
			return event_loc_tv;
		}
		public void setEvent_loc_tv(TextView event_loc_tv) {
			this.event_loc_tv = event_loc_tv;
		}
		public TextView getCaption_tv() {
			return caption_tv;
		}
		public void setCaption_tv(TextView caption_tv) {
			this.caption_tv = caption_tv;
		}
		public ImageView getMedia_pic() {
			return media_pic;
		}
		public void setMedia_pic(ImageView media_pic) {
			this.media_pic = media_pic;
		}
		public ImageView getMedia_video() {
			return media_video;
		}
		public void setMedia_video(ImageView media_video) {
			this.media_video = media_video;
		}
		public TextView getMedia_cast() {
			return media_cast;
		}
		public void setMedia_cast(TextView media_cast) {
			this.media_cast = media_cast;
		}
		public LinearLayout getMedia_cheer_layout() {
			return media_cheer_layout;
		}
		public void setMedia_cheer_layout(LinearLayout media_cheer_layout) {
			this.media_cheer_layout = media_cheer_layout;
		}
		public LinearLayout getMedia_comment_layout() {
			return media_comment_layout;
		}
		public void setMedia_comment_layout(LinearLayout media_comment_layout) {
			this.media_comment_layout = media_comment_layout;
		}
		public ImageView getCheers_tv_icon() {
			return cheers_tv_icon;
		}
		public void setCheers_tv_icon(ImageView cheers_tv_icon) {
			this.cheers_tv_icon = cheers_tv_icon;
		}
		public TextView getCheers_tv() {
			return cheers_tv;
		}
		public void setCheers_tv(TextView cheers_tv) {
			this.cheers_tv = cheers_tv;
		}
		public ImageView getComment_tv_icon() {
			return comment_tv_icon;
		}
		public void setComment_tv_icon(ImageView comment_tv_icon) {
			this.comment_tv_icon = comment_tv_icon;
		}
		public TextView getTvComment() {
			return tvComment;
		}
		public void setTvComment(TextView tvComment) {
			this.tvComment = tvComment;
		}	

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.media_cheer_layout:
			triggerEventMediaCheer(v);
			break;
		case R.id.media_comment_layout:	
			if(System.currentTimeMillis() - lastTimeUpdate > 1000){
				lastTimeUpdate = System.currentTimeMillis();
				triggerCommentsDialogByView(v);
			}
			break;
		case R.id.media_share_iv:
			triggerSharing(v);
			break;
		case R.id.media_video_play:
			triggerPlayVideo(v);
			break;
		case R.id.edt_media: 
			triggerEditProfileEventMedia(v);
			break;

		case R.id.delete_media: 
			showDeleteAlert(v);
			break;
		}
	}


	// show exit dialog method
	private void showDeleteAlert(View v) {

		final ProfileMediaInfo temp = (ProfileMediaInfo)v.getTag();
		String alertMsg = "";
		if(temp.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)){
			alertMsg = "Are you sure you want to delete cast?";
		}else if(temp.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)){
			alertMsg = "Are you sure you want to delete photo?";
		}else if(temp.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)){
			alertMsg = "Are you sure you want to delete video?";
		}

		final Dialog dialog = new Dialog(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.dialog_delete_media);
		TextView message = (TextView) dialog
				.findViewById(R.id.dialog_text_title);
		Button no = (Button) dialog.findViewById(R.id.dialog_btn_n);
		Button yes = (Button) dialog.findViewById(R.id.dialog_btn_s);
		message.setText(alertMsg);
		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				triggerRemoveProfileEventMedia(temp,dialog);
			}
		});
		dialog.show();
	}
	protected void triggerRemoveProfileEventMedia(ProfileMediaInfo temp, Dialog dialog) {

		if(temp != null){
			deleteProfileEventMedia(temp,dialog);
		}else{
			UIHelperUtil.showToastMessage("Unable to remove!...");
		}

	}

	private void deleteProfileEventMedia(final ProfileMediaInfo profileEventMedia,final Dialog dialog){
		final String url = Constants.common_url
				+ context.getString(R.string.delete_media);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_MEDIA_ID, profileEventMedia.getMediaId()));
		String type = "event_post";
		if(profileEventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)){
			type = "cast";
		}

		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_MEDIA_TYPE, type));
		JsonParser.callBackgroundService(url, nameValuePairs,
				new JsonServiceListener() {

			ProgressDialog pd;

			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(context);
				pd.show();
				pd.setMessage("Please wait...");
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
				if (jsonResponse != null) {
					Log.v("", "URL: " + url
							+ " event-media delete RESPONSE: "
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
							media_list.remove(profileEventMedia);
							if(dialog != null && dialog.isShowing()){
								dialog.dismiss();
							}
							updateMediaAdapter();

						} else {
							UIHelperUtil
							.showToastMessage("Something going wrong with server,Please try later");
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
	private void triggerEditProfileEventMedia(View v) {
		ProfileMediaInfo temp = (ProfileMediaInfo)v.getTag();
		if(temp != null){
			updateProfileEventMedia(temp);
		}else{
			UIHelperUtil.showToastMessage("Unable to edit!...");
		}

	}
	private void updateProfileEventMedia(ProfileMediaInfo profileEventMedia) {
		Fragment activeFragment = null;
		Log.d("", "koti profile videos:"+profileEventMedia.getMediaCategory());
		if(profileEventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)){
			activeFragment= EditACastFragment.newInstance(context);

		}else if(profileEventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)){
			activeFragment = EditFilterImageBroadCastFragment.getnewInstance(context);

		}else if(profileEventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)){
			activeFragment = EditFilterVideoBroadCastFragment.getnewInstance(context);
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.KEY_PROFILE_EVENT_MEDIA, profileEventMedia);
		bundle.putBoolean(Constants.KEY_IS_EDIT_EVENT_MEDIA, true);
		activeFragment.setArguments(bundle);
		if (context != null && activeFragment != null) {
			displayOnActivity(activeFragment);
		}
	}

	private void displayOnActivity(Fragment activeFragment) {
		if (context.getClass().getSimpleName()
				.equalsIgnoreCase("MainActivity")) {
			((MainActivity) context).pushFragments(
					((MainActivity) context).getmCurrentTab(), activeFragment,
					false, true);
		}else if(context instanceof CalendarEventsActivity){
			((CalendarEventsActivity) context).push(activeFragment);
		}
	}

	private void triggerSharing(View v) {
		final ProfileMediaInfo eventMedia = (ProfileMediaInfo) v.getTag();
		if (eventMedia != null) {
			showShareDialog("http://www.mysportsshare.com",eventMedia.getMediaUrl(),v);
		}
	}

	private void triggerPlayVideo(View v) {
		ProfileMediaInfo tmp1 = (ProfileMediaInfo) v.getTag();
		Fragment profile_all_Fragment = new VideosPlayFragment();
		Bundle args10 = new Bundle();
		args10.putString(Constants.userProf_userID,userId);
		args10.putString(Constants.userProf_userName,userName);
		args10.putString(Constants.userProf_videoPath,tmp1.getMediaUrl());
		args10.putString(Constants.userProf_caption,tmp1.getMediaCaption());
		profile_all_Fragment.setArguments(args10);
		if(context instanceof MainActivity){
			((MainActivity)context).pushFragments(((MainActivity)context).getmCurrentTab(), profile_all_Fragment, false,true);
		}else if(context instanceof CalendarEventsActivity) {
			((CalendarEventsActivity)context).push(profile_all_Fragment);
		}
		
	}

	public void triggerEventMediaCheer(View v) {
		ProfileMediaInfo tmp1 = (ProfileMediaInfo) v.getTag();
		if (tmp1 != null) {
			if (tmp1.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)) {
				// Update cheer staus for images & videos
				if (tmp1.getIsUserLiked().equalsIgnoreCase("false")) {
					// Send cheer status
					updateEventMediaCheerStatus(
							userId,
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_CAST, "true", tmp1);
				} else {
					// Send un-cheer status
					updateEventMediaCheerStatus(
							userId,
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_CAST, "false", tmp1);
				}
			} else {
				// Update cheer staus for images & videos
				if (tmp1.getIsUserLiked().equalsIgnoreCase("false")) {
					// Send cheer status
					updateEventMediaCheerStatus(
							userId,
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_IMG, "true", tmp1);
				} else {
					// Send un-cheer status
					updateEventMediaCheerStatus(
							userId,
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_IMG, "false", tmp1);
				}
			}
		} else {
			UIHelperUtil.showToastMessage("Unable to cheer!....");
		}
	}

	/*
	 * Method: updateEventCheerStatus Des: Update the cheer status of an event
	 * media Input: userID: logged user id mediaId : present cheering media
	 * mediaType : Cheered to which media cheerStatus: Cheering or uncheering
	 * status
	 */
	public void getCommentsList(final String notifyUserId, final String mediaId,
			final String mediaType, final ProfileMediaInfo eventMedia) {
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
									R.layout.comment_user_row, mediaType,eventMedia, cmtList);
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

	public void triggerCommentsDialogByView(View v) {
		ProfileMediaInfo tmp1 = (ProfileMediaInfo) v.getTag();
		if (tmp1 != null) {
			if (tmp1.getMediaCommentCount().trim().length() > 0) {
				if (tmp1.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)) {
					// Fetching cast comments
					getCommentsList(userId,
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_CAST, tmp1);
				} else {
					// Fetching images and photos comments
					getCommentsList(userId,
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_IMG, tmp1);
				}

			} else {
				if (tmp1.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)) {
					// Fetching cast comments
					displayCommentsDialog(userId,
							tmp1.getMediaId(),
							Constants.TAG_EVENT_MEDIA_TYPE_CAST, tmp1);
				} else {
					// Fetching images and photos comments
					displayCommentsDialog(userId,
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
				if(context.getClass().getSimpleName().equalsIgnoreCase("MainActivity")){
					//stack & displayed on the Main screen
					((MainActivity) context).pushFragments(
							((MainActivity) context).getmCurrentTab(),
							activeFragment, false, true);
				}else if(context instanceof CalendarEventsActivity){
					//stack & displayed on the Calendar screen						
					((CalendarEventsActivity)context).push(activeFragment);
				}
			}
		}	
	}

	//Display event media comments
	public void displayCommentsDialog(final String notifyUserId,
			final String mediaId, final String mediaType,
			final ProfileMediaInfo eventMedia) {

		// Prepare comments dialog layout
		LayoutInflater inflater = LayoutInflater.from(context);
		View dialogView = inflater.inflate(R.layout.comment_layout, null);

		//Prepare display dialog window
		altDlg = new Dialog(context);
		altDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

		//Start updating UI using handler
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
			final ProfileMediaInfo eventMedia) {
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
		if(eventMedia.getMediaType().equalsIgnoreCase(Constants.TAG_MEDIA_TYPE_EVENT)){
			nameValuePairs.add(new BasicNameValuePair(
					Constants.TAG_CATEGORY, Constants.TAG_CATEGORY_EVENT));
		}else{
			nameValuePairs.add(new BasicNameValuePair(
					Constants.TAG_CATEGORY, Constants.TAG_CATEGORY_PROFILE));
		}

		if(eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)){
			nameValuePairs.add(new BasicNameValuePair(
					Constants.TAG_CATEGORY_TYPE, "cast"));
		}else if(eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)){
			nameValuePairs.add(new BasicNameValuePair(
					Constants.TAG_CATEGORY_TYPE, "video"));
		}else if(eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)){
			nameValuePairs.add(new BasicNameValuePair(
					Constants.TAG_CATEGORY_TYPE, "photo"));
		}

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
							updateMediaAdapter();
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

	private void PostComment(final String notifyUserId, final String mediaId,
			final String mediaType, final String cmtText,
			final ProfileMediaInfo eventMedia) {
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
				Constants.TAG_CATEGORY, Constants.TAG_CATEGORY_PROFILE));
		if(eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)){
			nameValuePairs.add(new BasicNameValuePair(
					Constants.TAG_CATEGORY_TYPE, "cast"));
		}else if(eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)){
			nameValuePairs.add(new BasicNameValuePair(
					Constants.TAG_CATEGORY_TYPE, "video"));
		}else if(eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)){
			nameValuePairs.add(new BasicNameValuePair(
					Constants.TAG_CATEGORY_TYPE, "photo"));
		}

		JsonParser.callBackgroundService(url, nameValuePairs,
				new JsonServiceListener() {
			@Override
			public void parseJsonResponse(String jsonResponse) {
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
							cmtObj.setMediaCommentProfileImage(SharedPreferencesUtils.getUserProfilePicPath());
							cmtObj.setMediaCommentUserId(SharedPreferencesUtils.getUserId());
							cmtObj.setMediaCommentText(cmtText);
							if (cmtAdapter == null) {
								List<Comments> cmtList = new ArrayList<Comments>();
								cmtList.add(cmtObj);
								cmtAdapter = new CommentsAdapter(
										context,
										R.layout.comment_user_row, mediaType,eventMedia,
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
							eventMedia.setLatestCommentBy(SharedPreferencesUtils.capitalizeString(SharedPreferencesUtils
									.getUserName()));
							cmtAdapter.notifyDataSetChanged();
							updateMediaAdapter();
//							startUpdateUI();
							commentUsersListView.setSelection(0);
//							commentUsersListView
//							.setSelection(cmtAdapter.getCount() - 1);
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
	private void showShareDialog(final String link,final String eventPicUrl,View v) {
		final ProfileMediaInfo eventMedia = (ProfileMediaInfo) v.getTag();
		Log.d("","koti pic url "+eventPicUrl);
		if (eventMedia != null) {
			if(eventMedia.getMediaType().equalsIgnoreCase(Constants.TAG_MEDIA_TYPE_EVENT)){

				if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)) {
					//Share event cast info
					shareString = "Add cast to an \'"+eventMedia.getMediaEventTitle()+"\' event.";
				}else if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)) {
					//Share event video info
					shareString = "Add video to an \'"+eventMedia.getMediaEventTitle()+"\' event. Video shared at "+eventMedia.getMediaUrl()+" For more details please visit http://www.mysportsshare.com .";
				}else if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)) {
					//share event photo info
					shareString = "Add photo to an \'"+eventMedia.getMediaEventTitle()+"\' event.";
				}

			}else{

				if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)) {
					//Share event cast info
					shareString = "Add cast on his profile ";
				}else if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)) {
					//Share event video info
					shareString = "Add video to his profile "+" Video shared at "+eventMedia.getMediaUrl()+" For more details please visit http://www.mysportsshare.com";
				}else if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)) {
					//share event photo info
					shareString = "Add photo to his profile ";
				}

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

					Log.d("","click facebook");
					// UIHelper.showToastMessage(DetailsReviewsActivity.this,
					// "you click share button");
					if (Utils.chkStatus()) {

						Session session = Session.getActiveSession();
						if (session != null && session.isOpened()) {
							Log.d("Tag", "Success! " + "if");

							if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)) {
								//Share event cast info
								SocialNetworkingUtils.publishFeedDialog(shareString, link, eventPicUrl, context);
								//							publishFeedDialog(desc,"",eventPicUrl,context);
							}else if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)) {
								//Video is not shared
								SocialNetworkingUtils.publishVideoFeedDialog(shareString, link, eventMedia.getMediaUrl(), context);
							}else if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)) {
								//share event photo info
								SocialNetworkingUtils.publishFeedDialog(shareString, link, eventPicUrl, context);
							}

							//SocialNetworkingUtils.publishFeedDialog(shareString, link, eventPicUrl, context);

						} else {
							Log.d("Tag", "Session! " + "else");
							Session.openActiveSession((Activity) context, true,
									statusCallback);
							globalEventMedia = eventMedia;
							globleLink = link;
							/*Log.d("", "isfirsttimecalled"
									+ SocialNetworkingUtils.isFirstTimeCalled);*/
						}
						//						}
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
					Log.d("","event media:"+eventMedia.getMediaCategory());

					if(eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)){
						absType = "ABS_USR_PHOTO";
					}else if(eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)){
						absType = "ABS_USR_VIDEO";
					}else if(eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)){
						absType = "ABS_EVENT_CAST";
					}

					Utils.reportAbuseService(context,dialog,absType, eventMedia.getMediaId());
				}
			});

			Button btnTwitter = (Button) dialog.findViewById(R.id.twitter_btn);
			btnTwitter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d("","click twitter");
					SharedPreferences mSharedPref = context.getSharedPreferences(
							"Android_Twitter_Preferences", Context.MODE_PRIVATE);

					if (mSharedPref.getString(Constants.KEY_TWITTER_ACCESS_TOKEN,
							null) != null) {

						if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)) {
							//Share event cast info

							HelperMethods.postToTwitter(context, ((Activity)context), shareString, new TwitterCallback() {

								@Override
								public void onFinsihed(Boolean response) {
									Toast.makeText(context, "posted succeded", Toast.LENGTH_SHORT).show();
								}
							});

						}else if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)) {
							//Video is not shared

							HelperMethods.postToTwitter(context, ((Activity)context), shareString, new TwitterCallback() {

								@Override
								public void onFinsihed(Boolean response) {
									Toast.makeText(context, "posted succeded", Toast.LENGTH_SHORT).show();
								}
							});

						}else if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)) {
							//share event photo info

							new DownloadImageTaskForTwitter(dialog, shareString, v)
							.execute(eventPicUrl);

						}

					} else {
						try{
							SocialNetworkingUtils.signinTwitter(SocialNetworkingUtils.mTwitter, context,eventMedia,shareString);
						}catch(Exception e){
							Toast.makeText(context, "Please try later...",Toast.LENGTH_SHORT).show();
						}

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

						if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)) {
							//Share event cast info
							SocialNetworkingUtils.sharingToInstagram(context,shareString);

						}else if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)) {
							//Video is not shared
							SocialNetworkingUtils.sharingToInstagram(context,shareString);

						}else if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)) {
							//share event photo info
							new DownloadImageTaskForInstagram(dialog,shareString).execute(eventPicUrl);
						}

					}
				}
			});

			dialog.show();
		}
	}


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


	private com.facebook.Session.StatusCallback statusCallback = new StatusCallback() {

		@Override
		public void call(com.facebook.Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				session.removeCallback(statusCallback);
				//				SocialNetworkingUtils.isFirstTimeCalled = true;
				facebookSessionExistedorNot();
				Log.i("", "Session Facebook is opened: ");

			} else {
				Log.i("", "Session Facebook is closed");
			}
		}
	};




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
						// bring user to the market to download the app.
						// or let them choose an app?
						/*intent = new Intent(Intent.ACTION_VIEW);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setData(Uri.parse("market://details?id="
								+ "com.instagram.android"));
						context.startActivity(intent);*/
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
			//			UIHelperUtil.showToastMessage("Please try to share again...");
			if (globalEventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)) {
				//Share event cast info
				SocialNetworkingUtils.publishFeedDialog(shareString, globleLink, globalEventMedia.getMediaUrl(), context);
				//							publishFeedDialog(desc,"",eventPicUrl,context);
			}else if (globalEventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)) {
				//Video is not shared
				SocialNetworkingUtils.publishVideoFeedDialog(shareString, globleLink, globalEventMedia.getMediaUrl(), context);
			}else if (globalEventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)) {
				//share event photo info
				SocialNetworkingUtils.publishFeedDialog(shareString, globleLink, globalEventMedia.getMediaUrl(), context);
			}
			//			SocialNetworkingUtils.isFirstTimeCalled = false;
		} else {
			//			UIHelperUtil.showToastMessage("Session is null");
		}
	}
}