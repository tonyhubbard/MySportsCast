/*
 * Copyright 2013 - learnNcode (learnncode@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.mysportsshare.mysportscast.utils;

import java.io.File;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mysportsshare.mysportscast.activity.HomeActivity;

public class HelperMethods {
	private static final String TAG = "HelperMethods";
	
	public HelperMethods() {}

	public static void postToTwitter(Context context, final Activity callingActivity, final String message, final TwitterCallback postResponse){
		SharedPreferences mSharedPref = context.getSharedPreferences("Android_Twitter_Preferences", Context.MODE_PRIVATE);
		if(mSharedPref.getString(Constants.KEY_TWITTER_ACCESS_TOKEN, null) == null){
			postResponse.onFinsihed(false);
			return;
		}

		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(HomeActivity.CONSUMER_KEY);
		configurationBuilder.setOAuthConsumerSecret(HomeActivity.CONSUMER_SECRET);
		configurationBuilder.setOAuthAccessToken(HomeActivity.getAccessToken((context)));
		configurationBuilder.setOAuthAccessTokenSecret(HomeActivity.getAccessTokenSecret(context));
		Configuration configuration = configurationBuilder.build();
		final Twitter twitter = new TwitterFactory(configuration).getInstance();

		new Thread(new Runnable() {

			private double x;

			@Override
			public void run() {
				boolean success = true;
				try {
					x = Math.random();
					twitter.updateStatus(message);
				} catch (TwitterException e) {
					e.printStackTrace();
					success = false;
				}

				final boolean finalSuccess = success;

				callingActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						postResponse.onFinsihed(finalSuccess);
					}
				});

			}
		}).start();
	}


	public static void postToTwitterWithImage(Context context, final Activity callingActivity, final String imageUrl, final String message, final TwitterCallback postResponse){
		SharedPreferences mSharedPref = context.getSharedPreferences("Android_Twitter_Preferences", Context.MODE_PRIVATE);
		if(mSharedPref.getString(Constants.KEY_TWITTER_ACCESS_TOKEN, null) == null){
			postResponse.onFinsihed(false);
			return;
		}

		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(HomeActivity.CONSUMER_KEY);
		configurationBuilder.setOAuthConsumerSecret(HomeActivity.CONSUMER_SECRET);
		configurationBuilder.setOAuthAccessToken(HomeActivity.getAccessToken((context)));
		configurationBuilder.setOAuthAccessTokenSecret(HomeActivity.getAccessTokenSecret(context));
		Configuration configuration = configurationBuilder.build();
		final Twitter twitter = new TwitterFactory(configuration).getInstance();

		final File file = new File(imageUrl);

		new Thread(new Runnable() {


			@Override
			public void run() {
				boolean success = true;
				try {
					if (file.exists()) {
						StatusUpdate status = new StatusUpdate(message);
						status.setMedia(file);
						twitter.updateStatus(status);
					}else{
						Log.d(TAG, "----- Invalid File ----------");
						success = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					success = false;
				}



				final boolean finalSuccess = success;

				callingActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						postResponse.onFinsihed(finalSuccess);
					}
				});

			}
		}).start();
	}

	public static abstract class TwitterCallback{
		public abstract void onFinsihed(Boolean success);
	}
}
