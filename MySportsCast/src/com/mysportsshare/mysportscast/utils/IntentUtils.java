package com.mysportsshare.mysportscast.utils;

import android.content.Context;
import android.content.Intent;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.application.MySportsApp;

/**
 * All common Intents are located here.
 * @author puneeth
 *
 */
public class IntentUtils {
	
	/**
	 * Common method to share with intent
	 */
	public static void shareWithMedia(Context context, String subject, String text) {
		Intent sendIntent = new Intent();
		sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		sendIntent.putExtra(Intent.EXTRA_TEXT, text);
		sendIntent.setType("text/plain");
		context.startActivity(Intent.createChooser(sendIntent, "Share "+MySportsApp.getAppContext().getResources().getString(R.string.app_name)));
	}
}
