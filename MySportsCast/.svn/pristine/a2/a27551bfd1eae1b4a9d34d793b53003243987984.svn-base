package com.mysportsshare.mysportscast.application;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class MySportsApp extends Application {

	// context object
	private static MySportsApp appContext;
	private static Context activityContext;
	private RequestQueue requestQueue;
	public static boolean isFacebookSdkSupporting;

	@Override
	public void onCreate() {
		super.onCreate();
		Crashlytics.start(this);
		isFacebookSdkSupporting = true;
		appContext = MySportsApp.this;
		activityContext= MySportsApp.this;
		// Image Loader Setup
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheOnDisk(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(options)
				.memoryCache(new WeakMemoryCache())
				.discCacheSize(1024 * 1024 * 1024).build();

		ImageLoader.getInstance().init(configuration);
	}

	/**
	 * This Method will return the app instance.
	 * 
	 * @return
	 */
	public static Context getAppContext() {
		return appContext;
	}

	public static void setActivityContext(Context cntxt){
		activityContext = cntxt;
	}
	public static Context getActivityContext(Context cntxt){
		return activityContext;
	}
	public RequestQueue getRequestque() {
		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return requestQueue;
	}

	public void cancelPendingRequests(Object tag) {
		if (requestQueue != null) {
			requestQueue.cancelAll(tag);
		}
	}

	public <T> void addToRequestque(Request<T> tag) {
		tag.setTag("MySportsCast");
		getRequestque().add(tag);
	}

	public <T> void addToRequestque(Request<T> tag, String str) {
		tag.setTag(TextUtils.isEmpty(str) ? "MySportsCast" : str);
		getRequestque().add(tag);
	}

	public static MySportsApp getInstance() {
		return appContext;
	}

	private static Context mContext;

	public static Context getCurrentActivityContext() {
		return mContext;
	}

	public static void setCurrentACtivityContext(Context context) {
		mContext = context;
	}

	
}
