/*package com.mysportsshare.mysportscast.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;

public class LocationService extends Service implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
com.google.android.gms.location.LocationListener {

	private LocationClient locationclient;
	private Location curLocation;
	private LocationRequest locationrequest;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		getLocationFromLocationClient();
		return super.onStartCommand(intent, flags, startId);

	}

	private void getLocationFromLocationClient() {
		int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resp == ConnectionResult.SUCCESS) {
			locationclient = new LocationClient(this, this, this);
			locationclient.connect();
		} else {
			//			Toast.makeText(this, "Google Play Service Error " + resp, //$NON-NLS-1$
			// Toast.LENGTH_LONG).show();

		}

	}

	@Override
	public void onLocationChanged(Location arg0) {
		if (locationclient != null && locationclient.isConnected()) {
			curLocation = arg0;
			if (Constants.lati == null || Constants.lati.equalsIgnoreCase("0")) {
				Constants.lati = curLocation.getLatitude() + "";
				Constants.longi = curLocation.getLongitude() + "";

				//Find location name from current lat lng values
				//				getAddrByWeb(getLocationInfo(locationAddress));  
				SharedPreferencesUtils.setLatitude(Constants.lati);
				SharedPreferencesUtils.setLongitude(Constants.longi);
				try {
					getAddressFromLocation(curLocation.getLatitude(), curLocation.getLongitude(), getApplicationContext());
				} catch (Exception e) {
					Log.d(Constants.logUrl,"Unable to get location name");
					e.printStackTrace();
				}
			}			
		}

	}
	public static JSONObject getLocationInfo(String address) {
		StringBuilder stringBuilder = new StringBuilder();
		try {

			address = address.replaceAll(" ","%20");    

			HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			stringBuilder = new StringBuilder();


			response = client.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}
	private static List<Address> getAddrByWeb(JSONObject jsonObject){
		List<Address> res = new ArrayList<Address>();
		try
		{
			JSONArray array = (JSONArray) jsonObject.get("results");
			for (int i = 0; i < array.length(); i++)
			{
				Double lon = new Double(0);
				Double lat = new Double(0);
				String name = "";
				try
				{
					lon = array.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

					lat = array.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
					name = array.getJSONObject(i).getString("formatted_address");
					Address addr = new Address(Locale.getDefault());
					addr.setLatitude(lat);
					addr.setLongitude(lon);
					addr.setAddressLine(0, name != null ? name : "");
					res.add(addr);
				}
				catch (JSONException e)
				{
					e.printStackTrace();

				}
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();

		}

		return res;
	}
	public static void getAddressFromLocation(final double latitude, final double longitude,
			final Context context) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				Geocoder geocoder = new Geocoder(context, Locale.getDefault());
				String result = null;
				try {
					List<Address> addressList = geocoder.getFromLocation(
							latitude, longitude, 1);
					if (addressList != null && addressList.size() > 0) {
						Address address = addressList.get(0);
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
							sb.append(address.getAddressLine(i)).append("\n");
						}
						sb.append(address.getLocality()).append("\n");
						sb.append(address.getPostalCode()).append("\n");
						sb.append(address.getCountryName());
						result = sb.toString();
						Constants.locationName = address.getLocality()+", " + address.getCountryName();
						SharedPreferencesUtils.setLocaton(Constants.locationName);
					}
				} catch (IOException e) {
					Log.e(Constants.logUrl, "Unable connect to Geocoder", e);
				} finally {

					Message message = Message.obtain();
					message.setTarget(handler);
					if (result != null) {
						message.what = 1;
						Bundle bundle = new Bundle();
						result = "Latitude: " + latitude + " Longitude: " + longitude +
								"\n\nAddress:\n" + result;
						bundle.putString("address", result);
						message.setData(bundle);
					} else {
						message.what = 1;
						Bundle bundle = new Bundle();
						result = "Latitude: " + latitude + " Longitude: " + longitude +
								"\n Unable to get address for this lat-long.";
						bundle.putString("address", result);
						message.setData(bundle);
					}
					message.sendToTarget();
				}
			}
		};
		thread.start();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		if (locationclient != null && locationclient.isConnected()) {
			if (locationclient.getLastLocation() != null) {
				curLocation = locationclient.getLastLocation();				
			}
		}

		locationrequest = LocationRequest.create();
		locationrequest.setNumUpdates(1);
		locationrequest.setSmallestDisplacement(0);
		locationrequest
		.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		if (locationclient != null && locationclient.isConnected()) {
			locationclient.requestLocationUpdates(locationrequest, this);
		}		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

}
*/