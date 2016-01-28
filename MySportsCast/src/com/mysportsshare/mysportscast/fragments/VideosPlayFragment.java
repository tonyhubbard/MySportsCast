package com.mysportsshare.mysportscast.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class VideosPlayFragment extends Fragment implements OnClickListener{
	TextView caption_tv;
	VideoView media_video;
	ProgressBar mProgressBar;
	Activity activity;

	//Title bar components
	TextView title;
	ImageView backBtn;
	ImageView settingBtn;
	private ImageView addEventBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private RelativeLayout search_header_llyt;

	//User details
	private String user_id;
	private String first_name = "";
	//video path
	private String videoPath = "";
	private int seekPosition = 0;
	private String videoCaption;
	private ProgressDialog mProgressBarBeforePlayVideo;


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layoutView = inflater.inflate(R.layout.play_video, container, false);

		init(layoutView);

		if (Utils.chkStatus()) {
			//set video parameters
			try{
				MediaController mediacontroller = new MediaController(
						activity);
				mediacontroller.setAnchorView(media_video);
				// Get the URL from String VideoURL
				media_video.setMediaController(mediacontroller);
				media_video.setVideoPath(videoPath);
				
				media_video.requestFocus();
				media_video.setOnPreparedListener(new OnPreparedListener() {
					// Close the progress bar and play the video
					public void onPrepared(MediaPlayer mp) {
						mProgressBarBeforePlayVideo.dismiss();
						media_video.start();
					}
				});
				
				//Start loading progress bar
//				mProgressBarBeforePlayVideo.
				
				//we also set an setOnPreparedListener in order to know when the video file is ready for playback
				/*media_video.setOnPreparedListener(new OnPreparedListener() {

					public void onPrepared(MediaPlayer mediaPlayer) {
						// close the progress bar and play the video
//						mProgressBarBeforePlayVideo.setVisibility();
						//if we have a position on savedInstanceState, the video playback should start from here
						media_video.seekTo(seekPosition);
						if (seekPosition == 0) {
							media_video.start();
						} else {
							//if we come from a resumed activity, video playback will be paused
							media_video.pause();
						}
					}
				});*/

			}catch (Exception e) {
				Log.d(Constants.logUrl+ " Error", e.getMessage());
				e.printStackTrace();
			}
		}else{
			UIHelperUtil.showToastMessage(getString(R.string.toast_no_network));
		}

		return layoutView;
	}

	private void init(View layoutView){
		media_video = (VideoView) layoutView.findViewById(R.id.media_video);
		mProgressBar = (ProgressBar) layoutView.findViewById(R.id.Progressbar);
		mProgressBar.setProgress(0);
		mProgressBar.setMax(100);
		mProgressBarBeforePlayVideo = new ProgressDialog(activity);
		
		caption_tv = (TextView) layoutView
				.findViewById(R.id.caption_tv);

		//Reference to Title bar components 
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		backBtn = (ImageView) activity.findViewById(R.id.back_iv);
		settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);
		searchHeaderBtn = (ImageView)activity.findViewById(R.id.search_iv);
		addEventBtn = (ImageView)activity.findViewById(R.id.add_an_event_iv);
		tvEventType = (TextView) activity.findViewById(R.id.title_bar_tv_event_type);
		search_header_llyt = (RelativeLayout) activity.findViewById(R.id.search_header_llyt);

		backBtn.setOnClickListener(this);

		Bundle args = getArguments();		
		//If follower or following user is selected.
		if (args != null) {
			user_id = args.getString(Constants.userProf_userID);
			first_name = args.getString(Constants.userProf_userName);
			videoPath  = args.getString(Constants.userProf_videoPath);
			videoCaption  = args.getString(Constants.userProf_caption);
		}
	}

	@Override
	public void onStart() {
		super.onStart();		

		//Updating title bar fields
		if(user_id.equalsIgnoreCase(SharedPreferencesUtils.getUserId())){
			title.setText(Constants.userProf_videos);	
		}else{
			title.setText(first_name +" "+Constants.userProf_videos);
		}

		title.setVisibility(View.VISIBLE);
		backBtn.setVisibility(View.VISIBLE);		
		settingBtn.setVisibility(View.GONE);
		tvEventType.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		search_header_llyt.setVisibility(View.GONE);

		caption_tv.setText(videoCaption);
//		new MyAsync().execute();

	}	

	@Override
	public void onStop() {
		super.onStop();
		if((!TextUtils.isEmpty(videoPath)) &&(!videoPath.equalsIgnoreCase("null"))){
			seekPosition  = media_video.getCurrentPosition();
			media_video.pause();
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.back_iv:
			activity.onBackPressed();
			break;
		}	
	}

	//displaying progress bar
	private class MyAsync extends AsyncTask<Void, Integer, Void>
	{
		int duration = 0;
		int current = 0;
		@Override
		protected Void doInBackground(Void... params) {

			if((!TextUtils.isEmpty(videoPath))){ //&&(!videoPath.equalsIgnoreCase("null"))
				media_video.seekTo(seekPosition);
				//			media_video.start();
				media_video.start();	
			}/*else{
				UIHelperUtil.showToastMessage("Unable to play video!!!...");
			}*/
			media_video.setOnPreparedListener(new OnPreparedListener() {

				public void onPrepared(MediaPlayer mp) {
					duration = media_video.getDuration();
				}
			});

			do {
				current = media_video.getCurrentPosition();
				System.out.println("duration - " + duration + " current- "
						+ current);
				try {
					publishProgress((int) (current * 100 / duration));
					if(mProgressBar.getProgress() >= 100){
						break;
					}
				} catch (Exception e) {
				}
			} while (mProgressBar.getProgress() <= 100);

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			System.out.println(values[0]);
			mProgressBar.setProgress(values[0]);
		}
	}
}
