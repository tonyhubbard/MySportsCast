package com.mysportsshare.mysportscast;

import java.net.URLDecoder;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMConstants;
import com.mysportsshare.mysportscast.activity.GCM_Notification_Handler_Activity;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.CustomLog;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TOKEN = Long.toBinaryString(new Random()
	.nextLong());
	private static final String TAG = "MySportsCast : GCMIntentService";

	public GCMIntentService() {
		super(com.mysportsshare.mysportscast.utils.Constants.SENDER_ID);

	}

	private String ext;

	public GCMIntentService(String senderID) {
		super(Constants.SENDER_ID);
		CustomLog.d(TAG, TAG + " " + TAG);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		CustomLog.d(TAG,TAG+ " "
				+ "register Id: subscribeToPushNotifications: onRegistered registrationId: "
				+ registrationId);
		MyGCMRegistrar.setRegistrationId(context, registrationId);
		String userId = SharedPreferencesUtils.getUserId();
		serviceToRegisterForGCM(context, userId, registrationId);
		// XMLParser.registerUDIDWithServer(getApplicationContext(),
		// registrationId);

	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		CustomLog.d(TAG, TAG + " " + "onUnregistered");
		MyGCMRegistrar.clearRegistrationId(context);
	}

	@Override
	protected void onError(Context context, String errorId) {
		// C2DMessaging.clearRegistrationId(context);
		CustomLog.d(TAG, TAG + " " + "onError");
		if ("SERVICE_NOT_AVAILABLE".equals(errorId)) {
			int backoffTimeMs = MyGCMRegistrar.getBackoff(context);
			Intent retryIntent = new Intent(
					"com.google.android.gcm.intent.RETRY");
			PendingIntent retryPIntent = PendingIntent.getBroadcast(context,
					0 /* requestCode */, retryIntent, 0 /* flags */);
			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			am.set(AlarmManager.ELAPSED_REALTIME, backoffTimeMs, retryPIntent);

			backoffTimeMs *= 2;
			MyGCMRegistrar.setBackoff(context, backoffTimeMs);
		}
	}

	@Override
	protected void onMessage(final Context context, Intent intent) {
		final Bundle extras = intent.getExtras();

		if (extras == null) {
			return;
		}
		if (!extras.containsKey("message")) {
			return;
		}
		String key = URLDecoder.decode(extras.getString("message"));
		CustomLog.d("PNS", "extras: " + extras.toString()+" "+key);
		showNotification(extras, key);
		OnNotifyEventListener eventListener = (OnNotifyEventListener) MainActivity.getInstance();
		if (eventListener != null) {
			eventListener.onNotifyUser();
		}
		//		if (!isAppForground()) {
		//			showNotification(extras, key);
		//		} else {
		//			OnNotifyEventListener eventListener = (OnNotifyEventListener) MainActivity.getInstance();
		//			if (eventListener != null) {
		//				eventListener.onNotifyUser();
		//			}
		//		}
	}

	private int numMessages = 0;
	private String imgUrl;

	private void showNotification(Bundle extras, String mTypePNS) {
		if (mTypePNS != null && !mTypePNS.isEmpty()) {
			final RemoteViews contentView = setInviteNotificationsContent(extras,mTypePNS);

			final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle("MySportsCast ")
			.setContent(contentView);

			//			Intent resultIntent = new Intent(this, MainActivity.class);
			Intent resultIntent = new Intent(this, GCM_Notification_Handler_Activity.class);
			resultIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			/*resultIntent.putExtra("type", mTypePNS);*/
			resultIntent.putExtra(Constants.KEY_NOTIFICATION_EXTRAS, extras);
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			//			stackBuilder.addParentStack(MainActivity.class);
			stackBuilder.addParentStack(GCM_Notification_Handler_Activity.class);
			stackBuilder.addNextIntent(resultIntent);

			resultIntent.setAction(Long.toString(System.currentTimeMillis()));
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
			mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			mBuilder.setAutoCancel(true);
			NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
			String[] events = new String[6];
			setNotificationMessage(extras, mTypePNS);
			
			mBuilder.setStyle(inboxStyle);
			final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			final int notifyId = 1;
			mBuilder.setContentText("" + mTypePNS).setNumber(++numMessages);

			// TODO change this id

			ImageLoader
			.getInstance()
			.loadImage(
					imgUrl,
					new ImageLoadingListener() {

						@Override
						public void onLoadingCancelled(String arg0,
								View arg1) {
						}

						@Override
						public void onLoadingComplete(String arg0,
								View arg1, Bitmap bitmap) {
							if (TextUtils.isEmpty(imgUrl)) {
								contentView.setImageViewResource(
										R.id.notification_invite_image,
										R.drawable.ic_launcher);
							} else {
								contentView.setImageViewBitmap(
										R.id.notification_invite_image,
										bitmap);
							}
							// mBuilder.setContent(contentView);
							mNotificationManager.notify(notifyId, mBuilder.build());
						}

						@Override
						public void onLoadingFailed(String arg0,
								View arg1, FailReason arg2) {

							contentView.setImageViewResource(
									R.id.notification_invite_image,
									R.drawable.ic_launcher);
							mNotificationManager.notify(notifyId, mBuilder.build());
						}

						@Override
						public void onLoadingStarted(String arg0,
								View arg1) {


						}

					});

		}
	}

	private String setNotificationMessage(Bundle extras, String mTypePNS) {
		String content = "";
		if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_INVITE)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" has invitated you for an event \""
					+ extras.getString(Constants.TAG_NOTIFICATION_EVENTNAME,"") + "\"";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_LIKE)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" liked your event \"" + extras.getString(Constants.TAG_NOTIFICATION_EVENTNAME,"")
					+ "\"";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_CAST_COMMENT)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" commented on your cast added to an event \""
					+ extras.getString(Constants.TAG_NOTIFICATION_EVENTNAME,"") + "\"";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_CAST_LIKE)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" liked your cast added to an event \""
					+ extras.getString(Constants.TAG_NOTIFICATION_EVENTNAME,"") + "\"";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_COMMENT)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" commented your photo";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_COMMENT)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					"commented on your photo added to an event \""
					+ extras.getString(Constants.TAG_NOTIFICATION_EVENTNAME,"") + "\"";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_LIKE)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" liked your photo";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_LIKE)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" liked your photo added to an event \""
					+ extras.getString(Constants.TAG_NOTIFICATION_EVENTNAME,"") + "\"";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_PROFILE_PHOTO_TAGGED_USER)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" tagged you to a photo";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_TAGGED_USER)) {
			Log.d("","notification:"+extras.getString(Constants.TAG_NOTIFICATION_EVENTNAME,""));
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" tagged you to a photo added to an event \""
					+ extras.getString(Constants.TAG_NOTIFICATION_EVENTNAME,"") + "\"";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_COMMENT)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" commented on your video";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_COMMENT)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" commented on your video added to an event \""
					+ extras.getString(Constants.TAG_NOTIFICATION_EVENTNAME,"") + "\"";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_LIKE)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" liked your video";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_LIKE)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" liked your video added to an event \""
					+ extras.getString(Constants.TAG_NOTIFICATION_EVENTNAME,"") + "\"";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_PROFILE_VIDEO_TAGGED_USER)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" tagged you to a video";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_TAGGED_USER)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" tagged you to a video added to an event \""
					+ extras.getString(Constants.TAG_NOTIFICATION_EVENTNAME,"") + "\"";
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_FOLLOW_USER)) {
			if (extras.getString(Constants.TAG_NOTIFICATION_TEXT).equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_FOLLOW_USER_STATUS_FOLLOW)) {
				content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
						" started following you";
			} else if (extras.getString(Constants.TAG_NOTIFICATION_TEXT).equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_FOLLOW_USER_STATUS_REQ)) {
				content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
						" wanted to follow you";

			} else if (extras.getString(Constants.TAG_NOTIFICATION_TEXT).equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_FOLLOW_ACCEPT_STATUS_REJECT)) {
				content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
						" rejected your request";
			}else{
				content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
						" started following you";
			}
		} else if (mTypePNS.equalsIgnoreCase(
				Constants.TAG_NOTIFICATION_TYPE_FOLLOW_ACCEPT)) {
			content = extras.getString(Constants.TAG_NOTIFICATION_SENDER_NAME,"")+
					" accepted your request";
			
			/*if (extras.getString(Constants.TAG_NOTIFICATION_TEXT).equalsIgnoreCase(
					Constants.TAG_NOTIFICATION_TYPE_FOLLOW_ACCEPT_STATUS_ACCEPT)) {
				
			}else {
				inboxStyle.setBigContentTitle(
						mTypePNS);
			}*/
		}

		else {
			content = mTypePNS;
		}
		return content;
	}

	private boolean isAppForground() {
		Context ctx = getApplicationContext();

		ActivityManager am = (ActivityManager) ctx
				.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

		PackageManager pm = this.getPackageManager();
		// CustomLog.v("PNS", "PackageName: " + pm);

		try {
			/**
			 * take fore ground activity name
			 */
			ComponentName componentInfo = taskInfo.get(0).topActivity;
			CustomLog.d("PNS", " " + componentInfo.getClassName());
			/**
			 * All activities name of a package to compare with fore ground
			 * activity. if match found, no notification displayed.
			 */
			if (componentInfo.getPackageName().matches(
					"com.mysportsshare.mysportscast")) {
				return true;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		CustomLog.d(TAG, TAG + " " + "onRecoverableError");
		return super.onRecoverableError(context, errorId);
	}

	public void handleRegistration(final Context context, Intent intent) {
		CustomLog.d(TAG, TAG + " " + "handleRegistration");
		final String registrationId = intent
				.getStringExtra(GCMConstants.EXTRA_REGISTRATION_ID);
		String error = intent.getStringExtra(GCMConstants.EXTRA_ERROR);
		String removed = intent.getStringExtra(GCMConstants.EXTRA_UNREGISTERED);
		String action = intent.getAction();

		CustomLog.d(TAG, "dmControl: registrationId = " + registrationId
				+ ", error = " + error + ", removed = " + removed);
		if (removed != null) {
			// Remember we are unregistered
			MyGCMRegistrar.clearRegistrationId(context);
			MyGCMRegistrar.unregister(context);
			return;
		} else if (error != null) {
			// Registration failed
			CustomLog.e(TAG, "Registration error " + error);
			onError(context, error);
			if ("SERVICE_NOT_AVAILABLE".equals(error)) {
				int backoffTimeMs = MyGCMRegistrar.getBackoff(context);
				long nextAttempt = SystemClock.elapsedRealtime()
						+ backoffTimeMs;

				Intent retryIntent = new Intent(
						"com.google.android.gcm.intent.RETRY");
				retryIntent.putExtra("token", TOKEN);
				PendingIntent retryPendingIntent = PendingIntent.getBroadcast(
						context, 0, retryIntent, 0);
				AlarmManager am = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);
				am.set(AlarmManager.ELAPSED_REALTIME, nextAttempt,
						retryPendingIntent);
				backoffTimeMs *= 2; // Next retry should wait longer.
				MyGCMRegistrar.setBackoff(context, backoffTimeMs);
			} else {
				CustomLog.i(TAG, "Received error: " + error);
			}
		} else if (action.equals("com.google.android.gcm.intent.RETRY")) {
			String token = intent.getStringExtra("token");
			// make sure intent was generated by this class, not by a malicious
			// app
			if (TOKEN.equals(token)) {
				if (registrationId != null) {
					MyGCMRegistrar.internalUnregister(context);
					// last operation was attempt to unregister; send UNREGISTER
					// intent again
				} else {
					MyGCMRegistrar.internalRegister(context,
							Constants.SENDER_ID);
					// last operation was attempt to register; send REGISTER
					// intent again
				}
			}
		}
	}

	public static void serviceToRegisterForGCM(Context context, String userId,
			String regId) {
		// http://182.75.34.62/MySportsShare/web_services/update_device_token.php?
		// user_id=296&device_type=ANDROID&device_token=APA91bFGx2thegQxzHfHkoQQBBroXYc7A9VQQgc3qa0GdTWiLC9AHFxTjIvOyTy-otwKB8vJ_tJTinqjU_WECrZR4aE4bqfCVgGGGzDCt029x0CFM4TnPsK6d9GZ8FalCGKGIT1gNfP01ev6gNAEWZIZukBVrGz55g

		String url = Constants.common_url
				+ context.getString(R.string.update_device_token) + "?user_id="
				+ userId + "&device_type=" + Constants.DEVICE_TYPE
				+ "&device_token=" + regId;
		CustomLog.d("PNS", "update device " + url);
		SharedPreferencesUtils.setGCM_DeviceId(regId);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET,
				url, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonObject) {
				registerGCM(jsonObject);
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				CustomLog.v("PNS", "Error: " + error);
			}

		});
		MySportsApp.getInstance().addToRequestque(jsonObjectRequest,
				"MySportCast_app_device_token");
	}

	public static void registerGCM(JSONObject jsonResponseStr) {
		try {
			JSONObject sys = jsonResponseStr.getJSONObject("Response");
			String response = sys.getString("ResponseInfo");
			if (response.equals("SUCCESS")) {
				CustomLog.v("PNS", "Responce: " + response);
			} else {
				CustomLog.v("PNS", "Responce: " + " is failed");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * set the UI for Notification.
	 * @param extras
	 * @return
	 */
	private RemoteViews setInviteNotificationsContent(Bundle extras,String mTypePNS) {
		String name = extras.getString("sender_name");
		String text = extras.getString("event_name");
		imgUrl = extras.getString("image_url");
		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.notifications_invite_layout);

		contentView.setTextViewText(R.id.notification_invite_title, "" + 
				Html.fromHtml(extras.getString("message")));
		/*contentView.setTextViewText(R.id.notification_invite_title, "" + 
				Html.fromHtml(getMessageText(name, extras.getString("message"))));*/
		/*contentView.setTextViewText(R.id.notification_invite_title, "" + 
				Html.fromHtml(setNotificationMessage(extras, mTypePNS)));*/
	/*	contentView.setTextViewText(R.id.notification_invite_title, "" + 
				Html.fromHtml(extras.getString("message")));*/
		//contentView.setTextViewText(R.id.notification_invite_text, ""+ " has invited you for the event " + text);

		CustomLog.d("RemoteViews", "RemoteViews: " + name + " ; " + text);
		return contentView;
	}

	/**
	 * Getting notifications message based on type of action recieved from PNS.
	 * @param person
	 * @param mtype
	 * @return
	 */
	private String getMessageText(String person, String mtype) {
		String msgText = "";
		if (mtype.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_INVITE)) {
			msgText = "<b>" + person + "</b>"+" has invited you for an event ";
		} else if (mtype.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_TAGGED_USER)
				|| (mtype.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_TAGGED_USER))) {
			msgText = "<b>" + person + "</b>" + " has tagged you ";
		} else if(mtype.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_LIKE)) {
			msgText = "<b>" + person + "</b>" + " has liked your event";
		} else if (mtype.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_LIKE)
				|| mtype.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_CAST_LIKE)
				|| mtype.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_LIKE)) {
			msgText = "<b>" + person + "</b>" + " has liked your event media";
		} else if (mtype.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_PHOTO_COMMENT)
				|| mtype.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_CAST_COMMENT)
				|| mtype.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_EVENT_VIDEO_COMMENT)) {
			msgText = "<b>" + person + "</b>" + " has commented on your media";
		} else if (mtype.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_FOLLOW_USER)) {
			msgText = "<b>" + person + "</b>" + " want to follow you";
		}else if (mtype.equalsIgnoreCase(Constants.TAG_NOTIFICATION_TYPE_FOLLOW_ACCEPT)) {
			msgText = "<b>" + person + "</b>" + " accepted your request";
		}
		else{
			msgText = mtype;
		}
		CustomLog.v("PNS type", "PNS Type: " + mtype);
		return msgText;
	}

	/**
	 * This is used for communicating with main activity when application is in opened state.
	 * @author gvsharma
	 *
	 */
	public interface OnNotifyEventListener {
		public void onNotifyUser();
	}

}
