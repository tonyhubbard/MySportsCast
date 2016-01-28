package com.mysportsshare.mysportscast.parser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.mysportsshare.mysportscast.utils.UIHelperUtil;

public class JsonParser {

	public synchronized static void callBackgroundService(final String urltemp, final List<NameValuePair> paramsNameValuePairs, final JsonServiceListener jsonServiceListener) {
		/*String verifyUser = "";
		String url = "";
		List<NameValuePair> paramsNameValuePairs;*/
		try {
			final String url = urltemp.replaceAll(" ", "%20");
			new AsyncTask<Void, Void, String>(){
				@Override
				protected void onPreExecute() {
					jsonServiceListener.showProgress();
					//				System.out.println("onpreexecute");
				}

				@Override
				protected String doInBackground(Void... params) {
					try {
						int iterate = 0;
						boolean sucess = false;
						String responseMessage = null;
						do {
							try {
								if(responseMessage == null) { 
									// http client
									DefaultHttpClient httpClient = new DefaultHttpClient();
									//ConnectTimeout
									httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
									//SocketTimeout
									httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
									HttpEntity httpEntity = null;
									HttpResponse httpResponse = null;
									// Checking http request method type
									HttpPost httpPost = new HttpPost(url);
									// adding post params
									if (paramsNameValuePairs != null) {
										httpPost.setEntity(new UrlEncodedFormEntity(paramsNameValuePairs, "UTF-8"));
										//								Log.i("", "POST method: " + httpPost.toString());
									}
									httpResponse = httpClient.execute(httpPost);
									httpEntity = httpResponse.getEntity();

									responseMessage = EntityUtils.toString(httpEntity);
									//							Log.d("response http", "response message" + responseMessage);
									sucess = true;
								}
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch(SocketTimeoutException se) {
								se.printStackTrace();
							}catch(ConnectTimeoutException cte) {
								cte.printStackTrace();
							}catch (IOException e) {
								e.printStackTrace();
							}
							catch(Exception e) {
								e.printStackTrace();
								return null;
							}
						} while(sucess && ++iterate < 3);
						return responseMessage;
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}

				@Override
				protected void onPostExecute(String result) {
					jsonServiceListener.hideProgress();
					if (TextUtils.isEmpty(result)) {
						UIHelperUtil.showToastMessage("Please check your internet connection.");
						return;
					}
					jsonServiceListener.parseJsonResponse(result);
					jsonServiceListener.navigateToPrevFragment();
				}
			}.execute();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
