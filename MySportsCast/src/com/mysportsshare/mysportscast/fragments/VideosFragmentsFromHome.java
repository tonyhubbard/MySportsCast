package com.mysportsshare.mysportscast.fragments;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.activity.VideoFilterActivity;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;

public class VideosFragmentsFromHome extends Fragment implements OnClickListener {
	private String selectedVideoPath;
	private Uri selectedVideoPathUri; 

	static VideosFragmentsFromHome f = new VideosFragmentsFromHome();

	final String VideoLoadSuccessStatus = "VideoLoaded";
	final int VideoRecordTimeStamp = 30; //Limit video record time upto 30seconds

	Activity activity;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Make sure that container activity implement the callback interface
		try {
			this.activity = activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement DataPassListener");
		}
	}

	public static Fragment getInstance(Context context) {
		if (f == null) {
			f = new VideosFragmentsFromHome();
		}
		return f;
	}
	private ImageView event_media_video_capture_btn;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_videos, container, false);
		init(view);
		return view;
	}
	private void init(View layoutView){
		event_media_video_capture_btn = (ImageView)layoutView.findViewById(R.id.event_media_video_capture_btn);

		event_media_video_capture_btn.setOnClickListener(this);
	}
	public static Fragment newInstance(Context context) {
		f = new VideosFragmentsFromHome();

		return f;
	}
	@Override
	public void onResume() {
		super.onResume();

		TextView title = (TextView) activity.findViewById(
				R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);

		TextView eventtype = (TextView) activity.findViewById(
				R.id.title_bar_tv_event_type);
		title.setText("ADD VIDEO");
		eventtype.setVisibility(View.GONE);
		ImageView settingIv = (ImageView) activity.findViewById(
				R.id.setting_iv);
		settingIv.setVisibility(View.GONE);

		ImageView searchIv = (ImageView) activity.findViewById(
				R.id.search_iv);
		searchIv.setVisibility(View.GONE);

		ImageView backIv = (ImageView) activity.findViewById(R.id.back_iv);
		backIv.setVisibility(View.VISIBLE);
		backIv.setOnClickListener(this);

		ImageView addaneventIv = (ImageView) activity.findViewById(
				R.id.add_an_event_iv);
		addaneventIv.setVisibility(View.GONE);		
	}
	
	/**
	 * checking whether sd card exists or not
	 * 
	 * @return
	 */
	private boolean isSDCARDMounted() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	/**
	 * creating temp file.
	 * 
	 * @return
	 */
	private Uri getTempFile() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyymmdd_hhmmss");
		String dt = fmt.format(c.getTime()); //.toString()
		File root = new File(Environment.getExternalStorageDirectory()
				+ Constants.STORAGE_PATH, Constants.VIDEOPATH);
		if (!root.exists()) {
			root.mkdirs();
		}
		String filename = "VID_"+dt;
		File file = new File(root, filename + ".mp4");
		Uri muri = Uri.fromFile(file);
		selectedVideoPath = muri.getPath();
		selectedVideoPathUri = muri;
		Log.v("Video paht from Camera", selectedVideoPath);
		return muri;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(Constants.logUrl, "onActivityResult: " + requestCode + " : " + resultCode);
		if (resultCode == Activity.RESULT_OK) {
			if(requestCode == Constants.REQUEST_VIDEO_CAPTURE){

				//				if(data!=null){
				//get the returned data
				//					Uri videoFilePath = data.getData();

				/*int index = selectedVideoPath.lastIndexOf("/");
					String filename = selectedVideoPath.substring(index + 1,selectedVideoPath.length());
					String filepath = Environment.getExternalStorageDirectory().getAbsolutePath()+ Constants.STORAGE_PATH+"/"+Constants.VIDEOPATH+"/";		
					selectedVideoPath = filepath+filename;*/


				Bundle bundle = getArguments();
				if (bundle != null) {
					bundle.putString(Constants.dataReceive, selectedVideoPath);
				}else{
					bundle  = new Bundle();
				}
				
				if(activity instanceof MainActivity){
					Fragment fmt = ((MainActivity)activity).getLastButOneFragment();
					if(fmt instanceof EditFilterVideoBroadCastFragment){
						bundle.putBoolean(Constants.KEY_IS_EDIT_FILTER_VIDEO_FRAGMENT, true);
					}
				}else if(activity instanceof VideoFilterActivity){
					bundle.putBoolean(Constants.KEY_IS_EDIT_FILTER_VIDEO_FRAGMENT, true);
				}
				
				Intent imageFilter = new Intent(getActivity(),VideoFilterActivity.class);
				imageFilter.putExtras(bundle);
				activity.startActivityForResult(imageFilter, 1300);
//				startActivity(imageFilter);
				closeWindow();
			}
		} else {
			Log.v("", "OnActivityResult: " + resultCode);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.event_media_video_capture_btn:



			/*//Parameters object to get the parameters of the camera
			Camera camera = Camera.open();
			Camera.Parameters params = camera.getParameters();
			List<Size> sizes = params.getSupportedPictureSizes();
			// See which sizes the camera supports and choose one of those
			Size mSize = sizes.get(0);
			params.setPictureSize(mSize.width, mSize.height);
			Log.d("","koti size:"+mSize.width+" "+mSize.height);
			camera.setParameters(params);*/

			//Capture video
			if (isSDCARDMounted()) {
				Intent videoPickerIntent = new Intent(
						MediaStore.ACTION_VIDEO_CAPTURE);
				videoPickerIntent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, 0);
				videoPickerIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,
						0);
				videoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						getTempFile());				
				videoPickerIntent.putExtra("android.intent.extra.durationLimit",VideoRecordTimeStamp);
				//				videoPickerIntent.putExtra("return-data", true);
				startActivityForResult(videoPickerIntent,
						Constants.REQUEST_VIDEO_CAPTURE);
				Log.d(Constants.logUrl, isSDCARDMounted() + " ; "
						+ Constants.REQUEST_VIDEO_CAPTURE);
			} else {
				UIHelperUtil
				.showToastMessage("You need to insert SD Card");
			}
			break;
		case R.id.back_iv:
			closeWindow();
			break;
		default:
			break;
		}	
	}

	private void closeWindow() {
		if(activity!=null) {
			(activity).onBackPressed();
		}
	}
}
