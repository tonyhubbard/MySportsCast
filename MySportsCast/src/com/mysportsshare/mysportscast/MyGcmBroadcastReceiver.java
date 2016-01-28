package com.mysportsshare.mysportscast;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

public class MyGcmBroadcastReceiver extends GCMBroadcastReceiver {
	
	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		try {
			return "com.mysportsshare.mysportscast.GCMIntentService";
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
   /* @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(), GCMIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }*/
}
