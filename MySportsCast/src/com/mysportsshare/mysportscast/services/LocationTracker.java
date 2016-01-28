package com.mysportsshare.mysportscast.services;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/*This service get location details in background 
 * */
public class LocationTracker extends Service implements LocationListener {



	private Location location; 

	//flag used to know whether location get updated or not
	private boolean updateLocation; 

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	// Declaring a Location Manager
	protected LocationManager locationManager;	

	private class waitForMinTimeToUpdateLocation extends AsyncTask<Void, Void, Void>{
		ProgressDialog tmpDialog;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			int count=1;
			try
			{ 
				while(!updateLocation && count<=3){
					Thread.sleep(MIN_TIME_BW_UPDATES/3);
					count ++;
				}

			}catch(Exception ex){

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(Constants.mContext!=null){
				tmpDialog = new ProgressDialog(Constants.mContext);
				tmpDialog.setCancelable(false);
				tmpDialog.setCanceledOnTouchOutside(false);
				tmpDialog.setMessage(getString(R.string.update_location));
				tmpDialog.show();
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if( tmpDialog != null){
				tmpDialog.dismiss();				
			}
		}
	}

	//Update location: latitude & longitude constant parameters
	private void setLocationConstantParams(Location loc){
		if(loc!=null){
			Constants.longi = String.valueOf(location.getLongitude());
			Constants.lati = String.valueOf(location.getLatitude());
		}
	}
	/*Gets current location from network provider & GPS and update location details.
	 * This method waits for minimum time to update location details
	 * */
	public Location getLocation() {
		try {
			if(Constants.mContext!=null){
				locationManager = (LocationManager) Constants.mContext
						.getSystemService(LOCATION_SERVICE);

				// getting GPS status
				isGPSEnabled = locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER);

				// getting network status
				isNetworkEnabled = locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

				if (!isGPSEnabled && !isNetworkEnabled) {
					// need to imp: no network provider is enabled. Prompt user to enable location settings.
					AlertDialog.Builder nonetwork = new AlertDialog.Builder(Constants.mContext);
					nonetwork.setTitle("Alert");
					nonetwork.setMessage(getString(R.string.toast_no_network));
					nonetwork.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) { 
							dialog.dismiss();
						}
					});
					nonetwork.setCancelable(false);
					nonetwork.show();
				} else {
					this.canGetLocation = true;
					// First get location from Network Provider
					if (isNetworkEnabled) {
						updateLocation = false;
						locationManager.requestLocationUpdates(
								LocationManager.NETWORK_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);					
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						setLocationConstantParams(location);
						if (locationManager == null) {                    							
							Log.d("location Network", "Network");						
							//						new waitForMinTimeToUpdateLocation().execute(); //Used to wait until location details get updated
							if(location==null){ //If location is not updated even after waiting for 1min, then checking for last known location
								location = locationManager
										.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
								setLocationConstantParams(location);
							}
						}						
					}							
					if (isGPSEnabled) 
					{ // if GPS Enabled get lat/long using GPS Services
						if (location == null) {

							updateLocation = false;
							locationManager.requestLocationUpdates(
									LocationManager.GPS_PROVIDER,
									MIN_TIME_BW_UPDATES,
									MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							Log.d("GPS Enabled", "GPS Enabled");						
							setLocationConstantParams(location);
							if(location==null){
								//							new waitForMinTimeToUpdateLocation().execute(); //Used to wait until location details get updated
								if(location==null){ //If location is not updated even after waiting for 1min, then checking for last known location
									location = locationManager
											.getLastKnownLocation(LocationManager.GPS_PROVIDER);
									setLocationConstantParams(location);
								}
							}						
						}
					}				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 * */
	public void stopUsingGPS(){
		if(locationManager != null){
			locationManager.removeUpdates(LocationTracker.this);
		}      
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*When ever user wants to update location details, 
	 * they need to check whether location details are updated.
	 * */
	public boolean isLocationUpdated(){
		return updateLocation;
	}

	/*Updated location flag is reseted before requesting for location details.
	 * */
	public void resetUpdateLocation(){
		updateLocation = false;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub		
		this.updateLocation = true;
		this.location = location;
		setLocationConstantParams(location);		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
	public LocationTracker() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		getLocation();
		
		return START_STICKY;
	}

}
