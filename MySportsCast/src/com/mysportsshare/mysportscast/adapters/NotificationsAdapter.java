package com.mysportsshare.mysportscast.adapters;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CalendarEventsActivity;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.activity.ShowTagEventActivity;
import com.mysportsshare.mysportscast.fragments.EventDetailsFragment;
import com.mysportsshare.mysportscast.fragments.UserProfileFragment;
import com.mysportsshare.mysportscast.models.NotificationInfo;
import com.mysportsshare.mysportscast.models.TeamInfo;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;

public class NotificationsAdapter extends BaseAdapter implements
		OnClickListener {

	private Context context;
	private List<NotificationInfo> notifications_list;
	private String userId;

	// Comments dialog views
	ListView commentUsersListView;
	TextView commentListStatus;
	EditText commentEditText;
	ImageView commentPostBtn;
	CommentsAdapter cmtAdapter;
	private String userName;

	public NotificationsAdapter(Context context, String userId,
			String userName, List<NotificationInfo> notification_list) {
		this.context = context;
		this.notifications_list = notification_list;
		this.userId = userId;
		this.userName = userName;
	}

	@Override
	public int getCount() {
		return notifications_list.size();
	}

	@Override
	public Object getItem(int position) {
		return notifications_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void updateNotificationsList(
			List<NotificationInfo> notifications_list) {
		if (notifications_list != null) {
			this.notifications_list.addAll(notifications_list);
			notifyDataSetChanged();
		}
	}

	public void updateListAdapter() {
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(context);
		final NotificationInfo notificationItem = notifications_list
				.get(position);

		notificationItemHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.notification_row, parent,
					false);
			holder = new notificationItemHolder();
			holder.setUserName((TextView) convertView
					.findViewById(R.id.userName));
			holder.setUser_img((ImageView) convertView
					.findViewById(R.id.user_img));
			holder.setUserTag((TextView) convertView.findViewById(R.id.userTag));
			holder.setFrnd_req_action_btn_layout((LinearLayout) convertView
					.findViewById(R.id.frnd_req_action_btn_layout));
			holder.setAccept_btn((LinearLayout) convertView
					.findViewById(R.id.accept_btn));
			holder.setReject_btn((LinearLayout) convertView
					.findViewById(R.id.reject_btn));
			convertView.setTag(holder);
		} else {
			holder = (notificationItemHolder) convertView.getTag();
		}

		// Assigning listeners to notification entity
		if (!TextUtils.isEmpty(notificationItem.getSender_image_url())) {
			BitmapUtils.setImages(notificationItem.getSender_image_url(),
					holder.getUser_img(), R.drawable.profile_pic_dummy);
		}
		if (TextUtils.isEmpty(notificationItem.getSender_name())) {
			holder.getUserName().setText("user");
		} else {
			holder.getUserName().setText(notificationItem.getSender_name());
		}

		holder.getFrnd_req_action_btn_layout().setVisibility(View.GONE);
		if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_INVITE)) {
			holder.getUserTag().setText(
					"send invitation for event \""
							+ notificationItem.getEvent_name() + "\"");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_LIKE)) {
			holder.getUserTag().setText(
					"liked your event \"" + notificationItem.getEvent_name()
							+ "\"");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_CAST_COMMENT)) {
			holder.getUserTag().setText(
					"commented on your cast added to an event \""
							+ notificationItem.getEvent_name() + "\"");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_CAST_LIKE)) {
			holder.getUserTag().setText(
					"liked your cast added to an event \""
							+ notificationItem.getEvent_name() + "\"");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_COMMENT)) {
			holder.getUserTag().setText("commented on your photo");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_COMMENT)) {
			holder.getUserTag().setText(
					"commented on your photo added to an event \""
							+ notificationItem.getEvent_name() + "\"");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_LIKE)) {
			holder.getUserTag().setText("liked your photo");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_LIKE)) {
			holder.getUserTag().setText(
					"liked your photo added to an event \""
							+ notificationItem.getEvent_name() + "\"");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_TAGGED_USER)) {
			holder.getUserTag().setText("tagged you to a photo");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_TAGGED_USER)) {
			holder.getUserTag().setText(
					"tagged you to a photo added to an event \""
							+ notificationItem.getEvent_name() + "\"");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_COMMENT)) {
			holder.getUserTag().setText("commented on your video");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_COMMENT)) {
			holder.getUserTag().setText(
					"commented on your video added to an event \""
							+ notificationItem.getEvent_name() + "\"");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_LIKE)) {
			holder.getUserTag().setText("liked your video");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_LIKE)) {
			holder.getUserTag().setText(
					"liked your video added to an event \""
							+ notificationItem.getEvent_name() + "\"");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_TAGGED_USER)) {
			holder.getUserTag().setText("tagged you to a video");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_TAGGED_USER)) {
			holder.getUserTag().setText(
					"tagged you to a video added to an event \""
							+ notificationItem.getEvent_name() + "\"");
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_FOLLOW_USER)) {
			if (notificationItem.getText().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_FOLLOW_USER_STATUS_FOLLOW)) {
				holder.getUserTag().setText("started following you");
			} else if (notificationItem.getText().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_FOLLOW_USER_STATUS_REQ)) {
				holder.getUserTag().setText("requested to follow you");
				holder.getFrnd_req_action_btn_layout().setVisibility(
						View.VISIBLE);
				holder.getAccept_btn().setTag(notificationItem);
				holder.getReject_btn().setTag(notificationItem);
				holder.getAccept_btn().setOnClickListener(this);
				holder.getReject_btn().setOnClickListener(this);
			} else if (notificationItem
					.getText()
					.equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_FOLLOW_ACCEPT_STATUS_REJECT)) {
				holder.getUserTag().setText("request is rejected");
			}
		} else if (notificationItem.getNotification_type().equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_FOLLOW_ACCEPT)) {
			if (notificationItem
					.getText()
					.equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_FOLLOW_ACCEPT_STATUS_ACCEPT)) {
				holder.getUserTag().setText("accepted your request");
			}else {
				holder.getUserTag().setText("accepted your request");
			}
		}

		else {
			holder.getUserTag().setText("user tag");
		}
		holder.getUser_img().setTag(notificationItem);
		holder.getUserTag().setTag(notificationItem);

		holder.getUserTag().setOnClickListener(this);

		holder.getUser_img().setOnClickListener(this);
		return convertView;
	}

	// event details holder
	class notificationItemHolder {
		private TextView userName;
		private TextView userTag;
		private ImageView user_img;
		private LinearLayout frnd_req_action_btn_layout;
		private LinearLayout accept_btn;
		private LinearLayout reject_btn;

		public TextView getUserName() {
			return userName;
		}

		public void setUserName(TextView userName) {
			this.userName = userName;
		}

		public TextView getUserTag() {
			return userTag;
		}

		public void setUserTag(TextView userTag) {
			this.userTag = userTag;
		}

		public ImageView getUser_img() {
			return user_img;
		}

		public void setUser_img(ImageView user_img) {
			this.user_img = user_img;
		}

		public LinearLayout getFrnd_req_action_btn_layout() {
			return frnd_req_action_btn_layout;
		}

		public void setFrnd_req_action_btn_layout(
				LinearLayout frnd_req_action_btn_layout) {
			this.frnd_req_action_btn_layout = frnd_req_action_btn_layout;
		}

		public LinearLayout getAccept_btn() {
			return accept_btn;
		}

		public void setAccept_btn(LinearLayout accept_btn) {
			this.accept_btn = accept_btn;
		}

		public LinearLayout getReject_btn() {
			return reject_btn;
		}

		public void setReject_btn(LinearLayout reject_btn) {
			this.reject_btn = reject_btn;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_img:
			displayUserProfile(v);
			break;
		case R.id.userTag:
			navigateToScreen(v);
			break;
		case R.id.accept_btn:
			triggerUserRequestAction(v, "1");
			break;
		case R.id.reject_btn:
			triggerUserRequestAction(v, "0");
			break;
		default:
			break;
		}
	}

	private void triggerUserRequestAction(View v, String status) {
		NotificationInfo tmpInfo = (NotificationInfo) v.getTag();
		if (tmpInfo != null) {
			serviceToAcceptUserRequest(status, tmpInfo);
		}
	}

	private void navigateToScreen(View v) {
		NotificationInfo tmpInfo = (NotificationInfo) v.getTag();
		if (tmpInfo != null) {
			if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_EVENT_INVITE)
					|| tmpInfo.getNotification_type().equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_EVENT_LIKE)) {
				triggerEventDetailsScreen(v);
			} else if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_FOLLOW_USER)) {
				// TODO: Open user profile
				displayUserProfile(v);
			} else if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_CAST_COMMENT)
					|| tmpInfo.getNotification_type().equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_CAST_LIKE)) {
				triggerEventMediaScreen(v, tmpInfo, "cast", "event");
			} else if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_COMMENT)
					|| tmpInfo.getNotification_type().equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_LIKE)
					|| tmpInfo
							.getNotification_type()
							.equalsIgnoreCase(
									Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_TAGGED_USER)) {
				triggerEventMediaScreen(v, tmpInfo, "photo", "event");
			} else if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_COMMENT)
					|| tmpInfo.getNotification_type().equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_LIKE)
					|| tmpInfo
							.getNotification_type()
							.equalsIgnoreCase(
									Constants.TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_TAGGED_USER)) {
				triggerEventMediaScreen(v, tmpInfo, "photo", "profile");
			} else if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_COMMENT)
					|| tmpInfo.getNotification_type().equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_LIKE)
					|| tmpInfo
							.getNotification_type()
							.equalsIgnoreCase(
									Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_TAGGED_USER)) {
				triggerEventMediaScreen(v, tmpInfo, "video", "event");
			} else if (tmpInfo.getNotification_type().equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_COMMENT)
					|| tmpInfo.getNotification_type().equalsIgnoreCase(
							Constants.TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_LIKE)
					|| tmpInfo
							.getNotification_type()
							.equalsIgnoreCase(
									Constants.TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_TAGGED_USER)) {
				triggerEventMediaScreen(v, tmpInfo, "video", "profile");
			}
		}
	}

	private void triggerEventMediaScreen(View v, NotificationInfo tmpInfo,
			String mediaType, String resourceType) {
		NotificationInfo tmpObj = (NotificationInfo) v.getTag();
		if (tmpInfo != null) {
			Bundle args = new Bundle();
			args.putString(Constants.TAG_DISP_NOTIFICATION_MEDIA_ID,
					tmpObj.getData_id());
			args.putString(Constants.TAG_DISP_NOTIFICATION_MEDIA_TYPE,
					mediaType);
			args.putString(Constants.TAG_DISP_NOTIFICATION_RESOURCE_TYPE,
					resourceType);
			Intent intent = new Intent(context, ShowTagEventActivity.class);
			intent.putExtras(args);
			context.startActivity(intent);
		}
	}

	private void triggerEventDetailsScreen(View v) {
		NotificationInfo tmpInfo = (NotificationInfo) v.getTag();
		if (tmpInfo != null) {
			Fragment activeFragment = new EventDetailsFragment();
			Bundle bundle = new Bundle();
			// TODO change this used id.
			bundle.putString(Constants.KEY_USER_ID,
					SharedPreferencesUtils.getUserId());
			bundle.putString(Constants.KEY_EVENT_ID, tmpInfo.getEvent_id());
			bundle.putString(Constants.KEY_EVENT_TYPE, tmpInfo.getEvent_name());
			activeFragment.setArguments(bundle);
			// Adds the event-detailed fragment on to 'events fragment' stack &
			// displayed on the screen
			if(context instanceof MainActivity){
				((MainActivity) context).pushFragments(
						((MainActivity) context).getmCurrentTab(), activeFragment,
						false, true);
			}else if(context instanceof CalendarEventsActivity){
				((CalendarEventsActivity) context).push(activeFragment);
			}
			
		}
	}

	// Display user profile
	public void displayUserProfile(View v) {
		NotificationInfo tmpInfo = (NotificationInfo) v.getTag();
		if (tmpInfo != null) {
			String userId = tmpInfo.getSender_id();
			if (userId != null) {
				UserProfileFragment activeFragment = new UserProfileFragment();
				Bundle arg = new Bundle();
				arg.putString(Constants.dataReceive, userId);
				activeFragment.setArguments(arg);
				if (context != null) {
					if (context.getClass().getSimpleName()
							.equalsIgnoreCase("MainActivity")) {
						// stack & displayed on the Main screen
						((MainActivity) context).pushFragments(
								((MainActivity) context).getmCurrentTab(),
								activeFragment, false, true);
					}else if(context instanceof CalendarEventsActivity){
						((CalendarEventsActivity) context).push(activeFragment);
					}
				}
			}
		}
	}

	/**************************** SERVER INTERFACES STARTS *************************/
	private void serviceToAcceptUserRequest(final String followStatus,
			final NotificationInfo tmpInfo) {
		try {
			// Url for getting user profile info
			String url = Constants.common_url
					+ context.getString(R.string.acceptUserRequest);

			// input data to web service
			List<NameValuePair> inputVals = new ArrayList<NameValuePair>();
			inputVals.add(new BasicNameValuePair(
					Constants.UpdateFollowStatus_ReqUserID, tmpInfo
							.getSender_id()));
			inputVals.add(new BasicNameValuePair(
					Constants.UpdateFollowStatus_ReqFollowUserID,
					SharedPreferencesUtils.getUserId()));
			inputVals.add(new BasicNameValuePair(
					Constants.UpdateFollowStatus_ReqStatus, followStatus));

			// Send req to url & handle the result
			JsonParser.callBackgroundService(url, inputVals,
					new JsonServiceListener() {
						@Override
						public void parseJsonResponse(String jsonResponse) {
							if (jsonResponse != null) {
								Log.v(Constants.logUrl,
										"Accept user response: " + jsonResponse);
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
										try {
											if (followStatus
													.equalsIgnoreCase("1")) {
												tmpInfo.setText(Constants.TAG_NOTIFICATION_TYPE_FOLLOW_USER_STATUS_FOLLOW);
												updateListAdapter();
											} else {
												tmpInfo.setText(Constants.TAG_NOTIFICATION_TYPE_FOLLOW_ACCEPT_STATUS_REJECT);
												updateListAdapter();
											}
											Log.d(Constants.logUrl,
													"user request: ");
										} catch (Exception ex) {
										}
									} else {
										UIHelperUtil.showToastMessage(context
												.getString(R.string.service_toast));
									}
								} catch (Exception ex) {

								}
							}
						}

					});
		} catch (Exception ex) {
			if (BuildConfig.DEBUG) {
				Log.e(Constants.logUrl, ex.getMessage());
			}
		}
	}
	/**************************** SERVER INTERFACES ENDS *************************/
}