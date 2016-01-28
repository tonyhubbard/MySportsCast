package com.mysportsshare.mysportscast.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.application.MySportsApp;

/**
 * All Shared preferences will be here.
 * @author Koti
 *
 */
public class SharedPreferencesUtils {

	private static final Context context = MySportsApp.getAppContext();

	public static SharedPreferences getSharedPreferences() {
		return context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);		
	}

	public static void setUserId(String userId) {
		Editor editor = getSharedPreferences().edit();
		editor.putString("userId", userId);
		editor.commit();
	}

	public static String getUserId() {
		return getSharedPreferences().getString("userId", "");
	}

	public static void setLoginType(String loginType) {
		Editor editor = getSharedPreferences().edit();
		editor.putString("loginType", loginType);
		editor.commit();
	}

	public static String getLoginType() {
		return getSharedPreferences().getString("loginType", "");
	}

	public static void setUserName(String userName) {
		Editor editor = getSharedPreferences().edit();
		editor.putString("userName", userName);
		editor.commit();
	}

	public static String getUserProfilePicPath() {
		return getSharedPreferences().getString("userProfilePic", "");
	}

	public static void setUserProfilePicPath(String userProfilePic) {
		Editor editor = getSharedPreferences().edit();
		editor.putString("userProfilePic", userProfilePic);
		editor.commit();
	}

	public static String getUserName() {
		return getSharedPreferences().getString("userName", "");
	}

	public static void setOauthUserId(String userId) {
		Editor editor = getSharedPreferences().edit();
		editor.putString("fbUserId", userId);
		editor.commit();
	}

	public static String getOauthUserId() {
		return getSharedPreferences().getString("fbUserId", "");
	}

	/*Specifies whether user login via social media or through registered mail id
	 * true: login via social media
	 * false: login via registered mail id
	 * */
	public static String isUserLoginViaSocialMedia(){
		return getSharedPreferences().getString("LogInViaSocialMedia", "false");
	}

	public static void setUserLoginViaSocialMedia(String loginViaSociaMediaStatus){
		Editor editor = getSharedPreferences().edit();
		editor.putString("LogInViaSocialMedia", loginViaSociaMediaStatus);
		editor.commit();
	}

	/************************Filters related data in preference file******************/

	public static void setLatitude(String lat){
		Editor editor = getSharedPreferences().edit();
		editor.putString("filter_lat", lat);
		editor.commit();
	}
	public static void setLongitude(String longitude){
		Editor editor = getSharedPreferences().edit();
		editor.putString("filter_long", longitude);
		editor.commit();
	}
	
	public static void daysetLatitude(String lat){
		Editor editor = getSharedPreferences().edit();
		editor.putString("day_filter_lat", lat);
		editor.commit();
	}
	public static void daysetLongitude(String longitude){
		Editor editor = getSharedPreferences().edit();
		editor.putString("day_filter_long", longitude);
		editor.commit();
	}
	
	public static void setLocaton(String location){
		Editor editor = getSharedPreferences().edit();
		editor.putString("filter_location", location);
		editor.commit();
	}
	
	public static void daysetLocaton(String location){
		Editor editor = getSharedPreferences().edit();
		editor.putString("day_filter_location", location);
		editor.commit();
	}

	//Radius for searching events
	public static void setRange(String range){
		Editor editor = getSharedPreferences().edit();
		editor.putString("filter_range", range);
		editor.commit();
	}
	
	public static void daysetRange(String range){
		Editor editor = getSharedPreferences().edit();
		editor.putString("day_filter_range", range);
		editor.commit();
	}

	public static void setFromDate(String fromDate){
		Editor editor = getSharedPreferences().edit();
		editor.putString("filter_fromDate", fromDate);
		editor.commit();
	}
	
	public static void daysetFromDate(String fromDate){
		Editor editor = getSharedPreferences().edit();
		editor.putString("day_filter_fromDate", fromDate);
		editor.commit();
	}
	
	public static void setToDate(String toDate){
		Editor editor = getSharedPreferences().edit();
		editor.putString("filter_toDate", toDate);
		editor.commit();
	}
	
	public static void daysetToDate(String toDate){
		Editor editor = getSharedPreferences().edit();
		editor.putString("day_filter_toDate", toDate);
		editor.commit();
	}
	
	public static void setForAllDate(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("filter_allDates", true);
		editor.commit();
	}
	public static void daysetForAllDate(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("day_filter_allDates", true);
		editor.commit();
	}
	public static void resetForAllDate(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("filter_allDates", false);
		editor.commit();
	}
	public static void dayresetForAllDate(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("day_filter_allDates", false);
		editor.commit();
	}
	public static void setForAllSports(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("filter_allSports", true);
		editor.commit();
	}
	
	public static void daysetForAllSports(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("day_filter_allSports", true);
		editor.commit();
	}
	
	public static void setFilterApplyedFlag(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("filter_apply", true);
		editor.commit();
	}
	public static void daysetFilterApplyedFlag(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("day_filter_apply", true);
		editor.commit();
	}
	public static void resetFilterApplyedFlag(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("filter_apply", false);
		editor.commit();
	}
	public static void dayresetFilterApplyedFlag(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("day_filter_apply", false);
		editor.commit();
	}
	public static void setFilterChangedFlag(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("filter_change", true);
		editor.commit();
	}
	public static void daysetFilterChangedFlag(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("day_filter_change", true);
		editor.commit();
	}
	public static void resetFilterChangedFlag(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("filter_change", false);
		editor.commit();
	}
	public static void resetForAllSports(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("filter_allSports", false);
		editor.commit();
	}
	
	public static void dayresetForAllSports(){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("day_filter_allSports", false);
		editor.commit();
	}
	
	public static void setSportIds(String sportIds){
		Editor editor = getSharedPreferences().edit();
		editor.putString("filter_sportIds", sportIds);
		editor.commit();
	}
	
	public static void daysetSportIds(String sportIds){
		Editor editor = getSharedPreferences().edit();
		editor.putString("day_filter_sportIds", sportIds);
		editor.commit();
	}

	public static String getLatitude(){		
		return getSharedPreferences().getString("filter_lat", Constants.lati);
	}
	public static String getLongitude(){
		return getSharedPreferences().getString("filter_long", Constants.longi);
	}
	
	public static String daygetLatitude(){		
		return getSharedPreferences().getString("day_filter_lat", Constants.lati);
	}
	public static String daygetLongitude(){
		return getSharedPreferences().getString("day_filter_long", Constants.longi);
	}
	
	public static String getLocaton(){		
		return getSharedPreferences().getString("filter_location", Constants.locationName);
	}	
	
	public static String daygetLocaton(){		
		return getSharedPreferences().getString("day_filter_location", Constants.locationName);
	}	
	/*//Radius for searching events
	public static String getRange(){
		return getSharedPreferences().getString("filter_range", "100");		
	}*/

	//Radius for searching events
	public static String getRange(){
		return getSharedPreferences().getString("filter_range", "200");		
	}
	
	public static String daygetRange(){
		return getSharedPreferences().getString("day_filter_range", "200");		
	}

	public static String getFromDate(){
		return getSharedPreferences().getString("filter_fromDate", "");		
	}
	public static String daygetFromDate(){
		return getSharedPreferences().getString("day_filter_fromDate", "");		
	}
	public static String getToDate(){
		return getSharedPreferences().getString("filter_toDate", "");		
	}
	public static String daygetToDate(){
		return getSharedPreferences().getString("day_filter_toDate", "");		
	}
	public static boolean getForAllDate(){
		return getSharedPreferences().getBoolean("filter_allDates", true);		
	}
	
	public static boolean daygetForAllDate(){
		return getSharedPreferences().getBoolean("day_filter_allDates", true);		
	}
	
	public static boolean getForAllSports(){
		return getSharedPreferences().getBoolean("filter_allSports", true);		
	}	
	public static boolean daygetForAllSports(){
		return getSharedPreferences().getBoolean("day_filter_allSports", true);		
	}
	public static String getSportIds(){
		return getSharedPreferences().getString("filter_sportIds", "0");
	}
	public static String daygetSportIds(){
		return getSharedPreferences().getString("day_filter_sportIds", "0");
	}
	public static boolean isFilterApplyed(){
		return getSharedPreferences().getBoolean("filter_apply", false);
	}
	
	public static boolean dayisFilterApplyed(){
		return getSharedPreferences().getBoolean("day_filter_apply", false);
	}
	
	public static boolean isFilterChanged(){
		return getSharedPreferences().getBoolean("filter_change", false);
	}

	public static void setNotificationCount(int count) {
		Editor editor = getSharedPreferences().edit();
		editor.putInt(Constants.NOTIFICATION_COUNT, count);
		editor.commit();
	}

	public static int getNotificationCount() {
		return getSharedPreferences().getInt(Constants.NOTIFICATION_COUNT, 0);
	}

	//GCM device id
	public static void setGCM_DeviceId(String deviceId) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Constants.GCM_DEVICE_ID, deviceId);
		editor.commit();
	}

	public static String getGCM_DeviceId() {
		return getSharedPreferences().getString(Constants.GCM_DEVICE_ID, "");
	}
	
	public static long getLastAdTime(){
		return getSharedPreferences().getLong(Constants.AD_LAST_TIME, 0);
	}
	
	public static void setLastAdTime(long adlasttime){
		Editor editor = getSharedPreferences().edit();
		editor.putLong(Constants.AD_LAST_TIME, adlasttime);
		editor.commit();
	}
	
	//capitalize string
	public static String capitalizeString(String username){
		if(!username.isEmpty()){
			String name = username.split("\\s+")[0];
			name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
			return name;
		}
		return context.getResources().getString(R.string.no_name);
	}
}
