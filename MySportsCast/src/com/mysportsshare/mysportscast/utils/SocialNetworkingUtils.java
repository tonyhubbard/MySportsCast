package com.mysportsshare.mysportscast.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.londatiga.android.twitter.Twitter;
import net.londatiga.android.twitter.oauth.OauthAccessToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import br.com.dina.oauth.instagram.InstagramApp;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.PendingCall;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.models.EventInfo;
import com.mysportsshare.mysportscast.models.ProfileMediaInfo;
import com.mysportsshare.mysportscast.utils.HelperMethods.TwitterCallback;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnPublishListener;

public class SocialNetworkingUtils {

	// koti
	/**
	 * twitter variables
	 */
	public static Twitter mTwitter;
	public static final String CONSUMER_KEY = "ZYj95YLog8RkDGTkmEVvnCRVM";
	public static final String CONSUMER_SECRET = "AI7z8yRmTuUIyUTOgeC4pLVQ45LNMxngvpMkzVC0KXEipTgIfm";
	public static final String CALLBACK_URL = "https://dev.twitter.com/";
	public static ProgressDialog mProgress;

	/**
	 * facebook variables
	 */

	public static boolean canPresentShareDialog;
	public static boolean isFirstTimeCalled = false;
	public static PendingAction pendingAction = PendingAction.NONE;
	public static final String PERMISSION = "publish_actions";
	public static GraphPlace place;
	public static List<GraphUser> tags;
	public static UiLifecycleHelper uiHelper;
	public static InstagramApp mApp;
	public static GraphUser user;
	private static Context activitycontext;
	private static String eventlink;
	private static String description;
	public static SimpleFacebook mSimpleFacebook;
	public static List<String> permissions = new ArrayList<String>() {
		{
			add("public_profile");
			add("email");
			add("publish_actions");
		}
	};

	/**
	 * method for posting to twitter
	 */
	/*
	 * public static void postingtoTwitter(View v, final Activity context,
	 * String desc){
	 * 
	 * final ProgressDialog pd; try { pd = new ProgressDialog(context);
	 * pd.show(); pd.setMessage("Sharing..."); pd.setCancelable(false);
	 * pd.setCanceledOnTouchOutside(false);
	 * 
	 * HelperMethods.postToTwitter(context, context, desc, new TwitterCallback()
	 * {
	 * 
	 * @Override public void onFinsihed(Boolean response) { if (pd != null &&
	 * pd.isShowing()) { pd.dismiss(); } Toast.makeText(context,
	 * "posted successfully", Toast.LENGTH_SHORT).show(); } });
	 * 
	 * } catch (Exception ex) { Toast.makeText(context, "ERROR",
	 * Toast.LENGTH_SHORT).show(); }
	 * 
	 * }
	 */

	/**
	 * method for twitter login with params
	 */

	public static void signinTwitter(final View v, Twitter mTwitter,
			final String desc, final String eventPicUrl, final Activity activity) {
		mTwitter.signin(new Twitter.SigninListener() {
			@Override
			public void onSuccess(OauthAccessToken accessToken, String userId,
					String screenName) {
				// postingtoTwitter(v,activity,desc);
				HelperMethods.postToTwitterWithImage(activity, activity,
						eventPicUrl, desc, new TwitterCallback() {

					@Override
					public void onFinsihed(Boolean response) {
						Toast.makeText(activity, "posted succeded",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onError(String error) {
				UIHelperUtil.showToastMessage(error);
			}
		});
	}


	/**
	 * method just for twitter login with ProfileMediaInfo
	 */

	public static void signinTwitter(Twitter mTwitter,final Activity activity,final ProfileMediaInfo eventMedia,final String shareString) {
		mTwitter.signin(new Twitter.SigninListener() {
			@Override
			public void onSuccess(OauthAccessToken accessToken, String userId,
					String screenName) {
				/*Toast.makeText(activity, "Please try to share again...",
						Toast.LENGTH_SHORT).show();*/

				SharedPreferences mSharedPref = activity.getSharedPreferences(
						"Android_Twitter_Preferences", Context.MODE_PRIVATE);

				if (mSharedPref.getString(Constants.KEY_TWITTER_ACCESS_TOKEN,
						null) != null) {

					if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_CAST_INFO)) {
						//Share event cast info

						HelperMethods.postToTwitter(activity, ((Activity)activity), shareString, new TwitterCallback() {

							@Override
							public void onFinsihed(Boolean response) {
								Toast.makeText(activity, "posted succeded", Toast.LENGTH_SHORT).show();
							}
						});

					}else if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_VIDEO_INFO)) {
						//Video is not shared

						HelperMethods.postToTwitter(activity, ((Activity)activity), shareString, new TwitterCallback() {

							@Override
							public void onFinsihed(Boolean response) {
								Toast.makeText(activity, "posted succeded", Toast.LENGTH_SHORT).show();
							}
						});

					}else if (eventMedia.getMediaCategory().equalsIgnoreCase(Constants.TAG_MEDIA_PHOTO_INFO)) {
						//share event photo info

						new DownloadImageTaskForTwitter(activity, shareString)
						.execute(eventMedia.getMediaUrl());

					}

				}

			}

			@Override
			public void onError(String error) {
				UIHelperUtil.showToastMessage(error);
			}
		});
	}
	
	/**
	 * method just for twitter login with EventInfo.EventMedia
	 */

	public static void signinTwitter(Twitter mTwitter,final Activity activity,final EventInfo.EventMedia eventMedia,final String shareString) {
		mTwitter.signin(new Twitter.SigninListener() {
			@Override
			public void onSuccess(OauthAccessToken accessToken, String userId,
					String screenName) {
				/*Toast.makeText(activity, "Please try to share again...",
						Toast.LENGTH_SHORT).show();*/
				SharedPreferences mSharedPref = activity.getSharedPreferences(
						"Android_Twitter_Preferences", Context.MODE_PRIVATE);
				if (mSharedPref.getString(Constants.KEY_TWITTER_ACCESS_TOKEN,
						null) != null) {

					if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[3])) {
						//Share event cast info

						HelperMethods.postToTwitter(activity, ((Activity)activity), shareString, new TwitterCallback() {

							@Override
							public void onFinsihed(Boolean response) {
								Toast.makeText(activity, "posted succeded", Toast.LENGTH_SHORT).show();
							}
						});

					}else if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[2])) {
						//Video is not shared

						HelperMethods.postToTwitter(activity, ((Activity)activity), shareString, new TwitterCallback() {

							@Override
							public void onFinsihed(Boolean response) {
								Toast.makeText(activity, "posted succeded", Toast.LENGTH_SHORT).show();
							}
						});

					}else if (eventMedia.getMediaType().equalsIgnoreCase(Constants.KEYs[1])) {
						//share event photo info
						new DownloadImageTaskForTwitter(activity, shareString)
						.execute(eventMedia.getMediaPhotoUrl());
					}


				}

			}

			@Override
			public void onError(String error) {
				UIHelperUtil.showToastMessage(error);
			}
		});
	}
	
	/**
	 * enum
	 */
	public static enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	/**
	 * facebook functionalities
	 */
	public static void performPublish(PendingAction action,
			boolean allowNoSession, final Context context, String desc,
			String link, String eventPicUrl) {
		Session session = Session.getActiveSession();
		Log.v("", "Sessionsss: " + session);
		if (session != null) {
			SocialNetworkingUtils.pendingAction = action;
			// System.out.println("boolean: " + hasPublishPermission() + " : " +
			// allowNoSession);
			if (hasPublishPermission()) {
				// We can do the action right away.
				handlePendingAction(context, desc, link, eventPicUrl);
				// return;
			} else if (session.isOpened()) {
				// We need to get new permissions, then complete the action when
				// we get called back.
				session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
						(Activity) context, SocialNetworkingUtils.PERMISSION));
				// return;
			}
		} else {
			Session session2 = Session.openActiveSession((Activity) context,
					true, null);
			session2.requestNewPublishPermissions(new Session.NewPermissionsRequest(
					(Activity) context, SocialNetworkingUtils.PERMISSION));
			// System.out.println("Session new: " + session2);
		}
		/*
		 * Log.v("", "AllowSession: " + allowNoSession); if (allowNoSession) {
		 * SocialNetworkingUtils.pendingAction = action;
		 * handlePendingAction(context, desc,link, eventPicUrl); //
		 * System.out.println("Session new: " + session); }
		 */
	}

	public static boolean hasPublishPermission() {
		Session session = Session.getActiveSession();
		// System.out.println("session object: " + session);
		return session != null
				&& session.getPermissions().contains("publish_actions");
	}

	public static void handlePendingAction(Context context, String desc,
			String link, String eventPicUrl) {
		PendingAction previouslyPendingAction = SocialNetworkingUtils.pendingAction;
		Log.d("", "Sess Pending: " + SocialNetworkingUtils.pendingAction
				+ " : " + previouslyPendingAction);

		switch (previouslyPendingAction) {
		case POST_STATUS_UPDATE:
			postStatusUpdate(context, desc, link, eventPicUrl);
			break;
		}
	}

	public static void postStatusUpdate(final Context context, String desc,
			String link, String eventPicUrl) {

		if (SocialNetworkingUtils.canPresentShareDialog) {
			FacebookDialog shareDialog = createShareDialogBuilderForLink(
					context, desc, link, eventPicUrl).build();
			PendingCall pendingCall = shareDialog.present();
			SocialNetworkingUtils.uiHelper.trackPendingDialogCall(pendingCall);
			System.out.println("PendingCall: " + pendingCall);
		} else if (SocialNetworkingUtils.user != null && hasPublishPermission()) {
			final String message = context.getString(R.string.status_update,
					SocialNetworkingUtils.user.getFirstName(),
					(new Date().toString()));
			Request request = Request.newStatusUpdateRequest(
					Session.getActiveSession(), message,
					SocialNetworkingUtils.place, SocialNetworkingUtils.tags,
					new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							showPublishResult(message,
									response.getGraphObject(),
									response.getError(), context);
						}
					});
			request.executeAsync();
		} else {
			SocialNetworkingUtils.pendingAction = PendingAction.POST_STATUS_UPDATE;
		}

	}

	public static FacebookDialog.ShareDialogBuilder createShareDialogBuilderForLink(
			Context context, String desc, String link, String eventPicUrl) {
		return new FacebookDialog.ShareDialogBuilder((Activity) context)
		.setName("MysportsCast").setLink(link).setDescription(desc)
		.setPicture(eventPicUrl);
	}

	/**
	 * facebook app existed or not
	 */
	public static boolean isFacebookExist(Context context) {
		try {
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo("com.facebook.katana", 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	/*public static void onClickPostStatusUpdate(Context context,String desc,String link,String eventPicUrl) {
		performPublish(SocialNetworkingUtils.PendingAction.POST_STATUS_UPDATE, SocialNetworkingUtils.canPresentShareDialog,context, desc, link, eventPicUrl);
=======

	public static void onClickPostStatusUpdate(Context context, String desc,
			String link, String eventPicUrl) {
		performPublish(SocialNetworkingUtils.PendingAction.POST_STATUS_UPDATE,
				SocialNetworkingUtils.canPresentShareDialog, context, desc,
				link, eventPicUrl);
>>>>>>> .r4805
	}*/

	/**
	 * facebook post dialog
	 */

	public static void publishFeedDialog(String desc, String link,
			String eventPicUrl, final Context context) {
		activitycontext = context;
		Bundle params = new Bundle();
		eventlink = link;
		description = desc;
		params.putString("link", link);
		params.putString("description", desc);
		params.putString("name", "MySportsCast");
		params.putString("picture", eventPicUrl);
		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(context,
				Session.getActiveSession(), params)).setOnCompleteListener(
						new OnCompleteListener() {

							@Override
							public void onComplete(Bundle values,
									FacebookException error) {
								if (error == null) {
									// When the story is posted, echo the success
									// and the post Id.
									final String postId = values.getString("post_id");
									if (postId != null) {
										Toast.makeText(context, "Posted",
												Toast.LENGTH_SHORT).show();
									} else {
										// User clicked the Cancel button
										Toast.makeText(context.getApplicationContext(),
												"Publish cancelled", Toast.LENGTH_SHORT)
												.show();
									}
								} else if (error instanceof FacebookOperationCanceledException) {
									// User clicked the "x" button
									Toast.makeText(context, "Publish cancelled",
											Toast.LENGTH_SHORT).show();
								} else {
									// Generic, ex: network error
									Toast.makeText(context, "Error posting story",
											Toast.LENGTH_SHORT).show();
								}
							}

						}).build();
		feedDialog.show();

	}


	/**
	 * Publish feed example. <br>
	 * Must use {@link Permissions#PUBLISH_ACTION}
	 */
	public static void publishFeedExample(String desc,String link,String eventPicUrl, final Context context,SimpleFacebook mSimpleFacebook) {
		// listener for publishing action
		final OnPublishListener onPublishListener = new OnPublishListener() {

			@Override
			public void onFail(String reason) {
				hideDialog(context);
				// insure that you are logged in before publishing
				//				Log.w(TAG, "Failed to publish");
			}

			@Override
			public void onException(Throwable throwable) {
				hideDialog(context);
				//				Log.e(TAG, "Bad thing happened", throwable);
			}

			@Override
			public void onThinking() {
				// show progress bar or something to the user while publishing
				showDialog(context);
			}

			@Override
			public void onComplete(String postId) {
				hideDialog(context);
				//				toast("Published successfully.");
				Toast.makeText(context, "Published successfully.", Toast.LENGTH_SHORT).show();
			}
		};

		// feed builder
		final Feed feed = new Feed.Builder()
		.setName("MySportsCast")
		.setDescription(desc)
		.setPicture(eventPicUrl)
		.setLink(link)
		.build();
		mSimpleFacebook.publish(feed, true, onPublishListener);
	}


	public static void showPublishResult(String message, GraphObject result,
			FacebookRequestError error, Context context) {
		String title = null;
		String alertMessage = null;
		if (error == null) {
			title = "success";
			// String id = result.cast(GraphObjectWithId.class).getId();
			alertMessage = "successfully_posted";
		} else {
			title = "error";
			alertMessage = error.getErrorMessage();
		}

		new AlertDialog.Builder(context).setTitle(title)
		.setMessage(alertMessage).setPositiveButton("OK", null).show();
	}

	public static void hideDialog(Context context) {
		if (mProgress != null) {
			mProgress.hide();
		}
	}

	public static void showDialog(Context context) {
		mProgress = ProgressDialog.show(context, "Thinking", "Waiting for Facebook", true);
	}


	public static void publishVideoFeedDialog(String desc, String link,
			String mediaUrl, final Context context) {
		Bundle params = new Bundle();
		params.putString("link", link);
		params.putString("description", desc);
		params.putString("name", "MySportsCast");
		params.putString("source", mediaUrl);
		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(context,
				Session.getActiveSession(), params)).setOnCompleteListener(
						new OnCompleteListener() {

							@Override
							public void onComplete(Bundle values,
									FacebookException error) {
								if (error == null) {
									// When the story is posted, echo the success
									// and the post Id.
									final String postId = values.getString("post_id");
									if (postId != null) {
										Toast.makeText(context, "Posted",
												Toast.LENGTH_SHORT).show();
									} else {
										// User clicked the Cancel button
										Toast.makeText(context.getApplicationContext(),
												"Publish cancelled", Toast.LENGTH_SHORT)
												.show();
									}
								} else if (error instanceof FacebookOperationCanceledException) {
									// User clicked the "x" button
									Toast.makeText(context, "Publish cancelled",
											Toast.LENGTH_SHORT).show();
								} else {
									// Generic, ex: network error
									Toast.makeText(context, "Error posting story",
											Toast.LENGTH_SHORT).show();
								}
							}

						}).build();
		feedDialog.show();

	}

	public static void sharingToInstagram(Context context,String desc) {


		//		File file = downloadVideoFile(mediaUrl,"Sample.mp4");  
		Intent intent = context.getPackageManager()
				.getLaunchIntentForPackage("com.instagram.android");
		Bitmap bitmap = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.default_event_pic);

		String root = Environment.getExternalStorageDirectory()
				.toString();
		File myDir = new File(root + "/event_images");
		myDir.mkdirs();
		Random generator = new Random();
		int n = 10000;
		n = generator.nextInt(n);
		String fname = "image-" + n + ".jpg";
		File file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		File imageFile2 = new File(Environment
				.getExternalStorageDirectory(), "/event_images/"
						+ "image-" + n + ".jpg");
		String path = imageFile2.getAbsolutePath();
		if (intent != null) {
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.setPackage("com.instagram.android");
			try {
				shareIntent.putExtra(Intent.EXTRA_STREAM, Uri
						.parse(MediaStore.Images.Media.insertImage(
								context.getContentResolver(), path, "",
								"")));
				shareIntent.putExtra(Intent.EXTRA_TEXT, desc);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			shareIntent.setType("image/jpeg");

			context.startActivity(shareIntent);
			/*search_people_et.setText("");
			search_event_et.setText("");
			eventId = "";
			if(activity!=null){
				activity.finish();
			}*/
		} else {
			// bring user to the market to download the app.
			// or let them choose an app?
			intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse("market://details?id="
					+ "com.instagram.android"));
			context.startActivity(intent);
		}

	}

	/**
	 * AsyncTask Downloading pic from urls for twitter.
	 * 
	 * @author koti
	 * 
	 */
	public static class DownloadImageTaskForTwitter extends
	AsyncTask<String, Integer, Bitmap> {
		ProgressDialog pd;
		String socialImageUrl;
		File file;
		String desc;
		Context context;

		public DownloadImageTaskForTwitter(Context context,String sharedesc) {
			this.context = context;
			desc = sharedesc;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}

		protected Bitmap doInBackground(String... urls) {
			socialImageUrl = urls[0];
			Bitmap bmp = BitmapUtils.downloadImage(urls[0]);
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/twitter_post_pic");
			myDir.mkdirs();
			String fname = "event_pic.jpg";
			file = new File(myDir, fname);
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bmp;
		}

		protected void onPostExecute(Bitmap result) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			if (result != null) {
				try {
					SharedPreferences mSharedPref = context
							.getSharedPreferences(
									"Android_Twitter_Preferences",
									Context.MODE_PRIVATE);
					String filename = file.getAbsolutePath();
					if (mSharedPref.getString(
							Constants.KEY_TWITTER_ACCESS_TOKEN, null) != null) {
						
						HelperMethods.postToTwitterWithImage(context,
								((Activity) context), filename, desc,
								new TwitterCallback() {
							@Override
							public void onFinsihed(Boolean response) {
								Toast.makeText(context,
										"posted succeded",
										Toast.LENGTH_SHORT).show();
							}
						});
					} 

				} catch (Exception ex) {
					Toast.makeText(context, "Please try again",
							Toast.LENGTH_SHORT).show();
				}
			} else {
			}
		}
	}

}
