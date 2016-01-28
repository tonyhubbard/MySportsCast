package com.mysportsshare.mysportscast.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.SSLHandshakeException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.models.GeoData;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


public class GooglePlacesAutoCompleteAdapter extends ArrayAdapter<GeoData>
implements Filterable {

	private ArrayList<GeoData> resultList;
	Context cntxt;

	//private PlaceAutocompleteAdapter mAdapter;
	private static final String LOG_TAG = "Google Places Autocomplete";
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String TYPE_DETAILS = "/details";
	private static final String OUT_JSON = "/json";
	private static final String API_KEY = "AIzaSyAZ-Z1DQ9LlqdlzHgyJpiqlLLRqb8I72_8";

	public static GeoData selectLoc; // Holds the selected user

	public GooglePlacesAutoCompleteAdapter(Context context,
			int textViewResourceId) {
		super(context, textViewResourceId);
		cntxt = context;
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public GeoData getItem(int index) {
		return resultList.get(index);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) cntxt
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.list_place_items, null);
			TextView place = (TextView) convertView
					.findViewById(R.id.placeItem);
			place.setText(resultList.get(position).getLocation());
			place.setTag(resultList.get(position));
		} else {
			TextView place = (TextView) convertView
					.findViewById(R.id.placeItem);
			place.setText(resultList.get(position).getLocation());
			place.setTag(resultList.get(position));
		}
		convertView.setTag(resultList.get(position));
		return convertView;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults filterResults = new FilterResults();

				if (constraint != null) {
					if (selectLoc != null) {
						if (selectLoc.getLocation().equals(constraint)) {
							return null;
						}
					}
					// Retrieve the autocomplete results.
					resultList = autocomplete(constraint.toString());
					// Assign the data to the FilterResults
					filterResults.values = resultList;
					filterResults.count = resultList.size();
					/*
					 * if(selectLoc!=null){ int i=0; for(i=0; i<
					 * resultList.size(); i++){ GeoData tmp =
					 * (GeoData)resultList.get(i);
					 * if(tmp.getPlaceId().equals(selectLoc.getPlaceId())){
					 * i=-1; //When the selected loc exist in the result
					 * break; } } if(i!=-1){ //Erasing the previously
					 * selected location selectLoc = null; } }
					 */
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
		};
		return filter;
	}

	public static ArrayList<GeoData> autocomplete(String input) {
		ArrayList<GeoData> resultList = null;
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE
					+ TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?key=" + API_KEY);
			sb.append("&location=" + Constants.lati + "," + Constants.longi);
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(Constants.logUrl, "Error processing Places API URL", e);
			return resultList;
		} catch (SSLHandshakeException e) {
			UIHelperUtil.showToastMessage("internet connection problem");//SSL exception
			Log.e(Constants.logUrl, "Error connecting to Places API", e);
			return resultList;

		}
		catch (IOException e) {
			Log.e(Constants.logUrl, "Error connecting to Places API", e);
			return resultList;

		}
		catch (Exception e) {
			Log.e(Constants.logUrl, "Error connecting to Places API", e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<GeoData>(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++) {
				Log.i(Constants.logUrl, predsJsonArray.getJSONObject(i)
						.getString("description"));
				Log.i(Constants.logUrl,
						"============================================================");
				GeoData tmpData = new GeoData();
				tmpData.setLocation(predsJsonArray.getJSONObject(i).getString(
						"description"));
				tmpData.setPlaceId(predsJsonArray.getJSONObject(i).getString(
						"place_id"));
				tmpData.setReference(predsJsonArray.getJSONObject(i).getString(
						"reference"));
				resultList.add(tmpData);
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		}

		return resultList;
	}

	public static void findLatLongFromReference(GeoData tmpData){
		new AsyncTask<GeoData, Void, String>(){
			@Override
			protected String doInBackground(GeoData... tmpData) {
				HttpURLConnection conn = null;
				StringBuilder jsonResults = new StringBuilder();
				//Try to get lat long from google api's using place reference
				try {
					StringBuilder sb = new StringBuilder(PLACES_API_BASE		
							+ TYPE_DETAILS + OUT_JSON);
					sb.append("?key=" + API_KEY);
					sb.append("&reference="+tmpData[0].getReference());
					sb.append("&sensor=false");

					URL url = new URL(sb.toString());
					conn = (HttpURLConnection) url.openConnection();
					InputStreamReader in = new InputStreamReader(conn.getInputStream());

					// Load the results into a Stringgeometry":Builder
					int read;
					char[] buff = new char[1024];
					while ((read = in.read(buff)) != -1) {
						jsonResults.append(buff, 0, read);
					}					

				} catch (MalformedURLException e) {
					Log.e(Constants.logUrl, "Error processing Places API URL", e);
					return "Unable to get location details";
				} catch (SSLHandshakeException e) {
//					UIHelperUtil.showToastMessage("SSL exception");
					Log.e(Constants.logUrl, "Error connecting to Places API", e);
					return "Unable to get location details";
				}
				catch (IOException e) {
					Log.e(Constants.logUrl, "Error connecting to Places API", e);
					return "Unable to get location details";
				}
				catch (Exception e) {
					Log.e(Constants.logUrl, "Error connecting to Places API", e);
					return "Unable to get location details";
				} finally {
					if (conn != null) {
						conn.disconnect();
					}
				}

				//Parsing requested json result for lat long values
				try {
					// Create a JSON object hierarchy from the results
					JSONObject jsonObj = new JSONObject(jsonResults.toString());
					JSONObject ResultJson = jsonObj.getJSONObject("result");

					// Extract the Place details from the results		
					Log.i(Constants.logUrl,
							"============================================================");
					JSONObject GeometryJson = ResultJson.getJSONObject("geometry");
					JSONObject LocationJson = GeometryJson.getJSONObject("location");
					tmpData[0].setLatitude(LocationJson.getString("lat"));
					tmpData[0].setLongitude(LocationJson.getString("lng"));
				} catch (JSONException e) {
					Log.e(LOG_TAG, "Cannot process JSON results", e);
				}
				return "";			
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if(!TextUtils.isEmpty(result)){
					UIHelperUtil.showToastMessage(result);
				}
			}
		}.execute(tmpData);
		
	}
}

