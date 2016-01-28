package com.mysportsshare.mysportscast.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ListView;

import com.mysportsshare.mysportscast.GCMIntentService;
import com.mysportsshare.mysportscast.MyGCMRegistrar;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;

public class Utils {

	private static final Context context = MySportsApp.getAppContext();
	/**
	 * check n/w available or not
	 * */
	public static boolean chkStatus() {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager != null) {
			NetworkInfo i = connManager.getActiveNetworkInfo();
			if (i != null) {
				if (i.isConnected())
					return true;
				if (i.isAvailable())
					return true;                
			}
		} 
		return false;
		/*ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected() ||
			connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
				return true;
		}

		return false;*/
	}

	/**
	 * Function to show settings alert dialog On pressing Settings button will
	 * lauch Settings Options
	 * */
	public static void showGPSDialogSettings(final FragmentActivity frgActivity) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(frgActivity);
		alertDialog.setTitle("Location Settings");
		alertDialog.setMessage("Enable Location");
		// setting Dialog cancelable to false
		alertDialog.setCancelable(false);
		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				frgActivity.startActivity(new Intent(
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

	public static void getListViewSize(ListView myListView) {
		/*	ListAdapter myListAdapter = myListView.getAdapter();
		if (myListAdapter == null) {
			//do nothing return null
			return;
		}
		//set listAdapter in loop for getting final size
		int totalHeight = 0;
		for (int size = 0; size < myListAdapter.getCount(); size++) {
			View listItem = myListAdapter.getView(size, null, myListView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		//setting listview item in adapter
		ViewGroup.LayoutParams params = myListView.getLayoutParams();
		params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
		myListView.setLayoutParams(params);*/
		// print height of adapter on log
		//		Log.i("height of listItem:", String.valueOf(totalHeight));
	}

	//method for converting date formating
	public static String convertToString(Date date, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}

	//validating email
	@TargetApi(Build.VERSION_CODES.FROYO)
	public static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	/**
	 * Convert Dp to Pixel
	 */
	public static int dpToPx(float dp, Resources resources){
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
		return (int) px;
	}

	/** 
	 * no n/w dialog
	 */
	public static void networkAlertDialog(final Context activity,String title){
		AlertDialog.Builder nonetwork = new AlertDialog.Builder(activity);
		nonetwork.setTitle("Alert");
		nonetwork.setMessage(title);
		nonetwork.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				dialog.dismiss();
				//				((Activity)activity).finish();
			}
		});
		nonetwork.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				dialog.dismiss();
			}
		});
		nonetwork.setCancelable(false);
		nonetwork.show();
	}

	public static boolean isNetworkConnected(Context _context) {
		boolean forMobileNetworkBool = false;
		boolean connected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo forMobileNetWorkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(forMobileNetWorkInfo != null) {
			forMobileNetworkBool = (forMobileNetWorkInfo.getState() == NetworkInfo.State.CONNECTED);
		} else {
			forMobileNetworkBool = false;
		}
		if(forMobileNetworkBool || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			//we are connected to a network
			connected = true;
		}
		else
			connected = false;
		return connected;
	}

	public static void showAlertDialog(final Context cntxt,String title, String msg){
		AlertDialog.Builder altDialog = new AlertDialog.Builder(cntxt);
		altDialog.setTitle(title);
		altDialog.setMessage(msg);		
		altDialog.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				dialog.dismiss();
			}
		});
		altDialog.setCancelable(false);
		altDialog.show();
	}
	public static void showAlertDialogToCloseActivity(final Context cntxt,String title, String msg){
		AlertDialog.Builder altDialog = new AlertDialog.Builder(cntxt);
		altDialog.setTitle(title);
		altDialog.setMessage(msg);		
		altDialog.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				dialog.dismiss();
				((Activity)cntxt).finish();
			}
		});
		altDialog.setCancelable(false);
		altDialog.show();
	}

	/**
	 * Purpose: Reporting event as abuse
	 */
	public static void reportAbuseService(Context context, final Dialog dialog,String absType,String eventId){
		final String iamAttendingUrl = Constants.common_url
				+ context.getResources().getString(R.string.reporting_abuse);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("abuse_type", absType));
		nameValuePairs.add(new BasicNameValuePair("abuse_type_id", eventId));
		nameValuePairs.add(new BasicNameValuePair("report_user_id", SharedPreferencesUtils.getUserId()));
		JsonParser.callBackgroundService(iamAttendingUrl, nameValuePairs,
				new JsonServiceListener() {

			@Override
			public void parseJsonResponse(String jsonResponse) {
				if (jsonResponse != null) {
					JSONObject jsonObject;
					Log.d("", "Response: "+jsonResponse);
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject
								.getJSONObject(Constants.TAG_RESPONSE);
						String resStr = resObj
								.getString(Constants.TAG_RESPONSE_INFO);
						if (resStr != null
								&& resStr
								.equalsIgnoreCase(Constants.TAG_SUCCESS)) {
							UIHelperUtil
							.showToastMessage("Reporting to the event successfully");
							dialog.dismiss();
						} else if (resStr != null
								&& resStr
								.equalsIgnoreCase(Constants.TAG_FAILURE)) {
							UIHelperUtil
							.showToastMessage("Reporting failure.....");
						} else if(resStr != null && resStr.equalsIgnoreCase("invalid input")){
							UIHelperUtil.showToastMessage("Invalid inputs, please check..!");
						}else{
//							UIHelperUtil.showToastMessage("Server error..!");
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
					//
				} else {
					UIHelperUtil
					.showToastMessage("Something went wrong with internet.....");
				}
			}
		});
	}
	/**
	 * Register for GCM
	 * @param context
	 * @return
	 */
	public static String registerForGCM(Context context) {
		if(TextUtils.isEmpty(SharedPreferencesUtils.getGCM_DeviceId())){
			//Fetch GCM device id 
			MyGCMRegistrar.register(context, Constants.SENDER_ID);
			String dev_id = (MyGCMRegistrar.getRegistrationId(context).equals("") ? "0": MyGCMRegistrar.getRegistrationId(context));
			return dev_id;
		}else{
			if(TextUtils.isEmpty(SharedPreferencesUtils.getUserId())){
				return "0";
			}else{
				GCMIntentService.serviceToRegisterForGCM(MySportsApp.getAppContext(),SharedPreferencesUtils.getUserId(),SharedPreferencesUtils.getGCM_DeviceId());
				return SharedPreferencesUtils.getGCM_DeviceId();
			}
		}
//		return "0";
	}

	/**
	 * UnRegister for GCM
	 * @param context
	 * @return
	 */
	public static void unRegisterForGCM(Context context) {
		MyGCMRegistrar.unregister(context);
	}

}
