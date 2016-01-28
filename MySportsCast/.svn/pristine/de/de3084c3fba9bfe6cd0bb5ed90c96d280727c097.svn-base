package com.mysportsshare.mysportscast.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.ImageFilterActivity;
import com.mysportsshare.mysportscast.adapters.ImageFiltersAdapter;
import com.mysportsshare.mysportscast.utils.AdapterCallback;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.Utils;
import com.mysportsshare.mysportscast.view.HorizontalListView;

public class FilterImageEditFragment extends Fragment implements OnClickListener,AdapterCallback{

	private ImageView img_filtered;
	private HorizontalListView img_filter_themes;
	private String imgPath;


	private Constants.imgFilterList img_filters_list[];
	private ImageView backBtn;
	private TextView title;
	private ImageView settingBtn;
	private TextView img_filter_text;	
	Activity activity;

	private ImageFiltersAdapter imgFilterAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View layoutView = inflater.inflate(R.layout.image_filter, container,false);

		img_filters_list = new Constants.imgFilterList[]{
				Constants.imgFilterList.NORMAL,
				Constants.imgFilterList.INKWELL,
				Constants.imgFilterList.KELLY,
				Constants.imgFilterList.PEARL,
				Constants.imgFilterList.AMBER,
				Constants.imgFilterList.MAYFAIR,
				Constants.imgFilterList.ALICE,
				Constants.imgFilterList.ROWAN,
				Constants.imgFilterList.DAISY						
		};
		//		Constants.imgFilterList.GREY,
		//		Constants.imgFilterList.SNOW,
		//		Constants.imgFilterList.GAMMA,
		//		Constants.imgFilterList.COLOR,
		//		Constants.imgFilterList.CONTRAST,
		//		Constants.imgFilterList.SHINY,
		//		Constants.imgFilterList.SHARP,				
		//		Constants.imgFilterList.BLACK
		//		Constants.imgFilterList.SMOOTH,
		//		Constants.imgFilterList.GAUSSIAN,
		//		Constants.imgFilterList.TINT,
		//		Constants.imgFilterList.FLEA,
		init(layoutView);

		return layoutView;
	}

	private void init(View layoutView){
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		backBtn = (ImageView) activity.findViewById(R.id.back_iv);
		settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);

		backBtn.setOnClickListener(this);
		settingBtn.setOnClickListener(this);

		img_filtered = (ImageView)layoutView.findViewById(R.id.img_filtered);
		img_filter_themes = (HorizontalListView)layoutView.findViewById(R.id.img_filter_themes);
		img_filter_text = (TextView)layoutView.findViewById(R.id.img_filter_text);

		updateViewVisibility(0);

		//Display default images
		img_filtered.setImageResource(R.drawable.profile_pic_dummy);

	}

	@Override
	public void onStart(){
		super.onStart();
		if(activity!=null){
			((ImageFilterActivity)activity).setImageEditFragementOpenedFlag();
		}
	}

	public void ClearCreatedBitmaps() {
		imgFilterAdapter.clearBitmaps();
	}

	private void updateViewVisibility(int status){
		switch(status){
		case 0:
			//Image is not loaded. show loading status
			img_filter_text.setVisibility(View.VISIBLE);
			img_filter_themes.setVisibility(View.GONE);
			img_filtered.setVisibility(View.GONE);
			settingBtn.setVisibility(View.VISIBLE);
			break;
		case 1:
			//Show images
			img_filter_text.setVisibility(View.GONE);
			img_filter_themes.setVisibility(View.VISIBLE);
			img_filtered.setVisibility(View.VISIBLE);
			settingBtn.setVisibility(View.VISIBLE);
			break;
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		title.setText(Constants.UI_Edit);

		//Loads selected image path
		Bundle args = getArguments();
		if(args!=null){
			imgPath = args.getString(Constants.dataReceive);
		}

		//Display all the themes
		if(TextUtils.isEmpty(imgPath)){
			Log.d(Constants.logUrl,"No path selected");
			Utils.showAlertDialogToCloseActivity(activity,"Alert","No image is selected.\nPlease select image");
		}else{
			Log.d(Constants.logUrl,imgPath);
			//			BitmapUtils.setImages(imgPath, img_filtered, R.drawable.profile_pic_dummy);
			//Load all filtering images to the list
			if(imgFilterAdapter==null){
				imgFilterAdapter = new ImageFiltersAdapter(activity, img_filtered,img_filters_list, imgPath,FilterImageEditFragment.this);
			}else{
				//Update UI view reference
				imgFilterAdapter.setImageViewReference(activity,img_filtered);

				//Update filter images on screen
				onStatusReturn("");	
			}
			img_filter_themes.setAdapter(imgFilterAdapter);
			img_filter_themes.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					//					ImageFiltersAdapter.ImageFilterHolder holder = (ImageFiltersAdapter.ImageFilterHolder)view.getTag();
					imgFilterAdapter.selectImageFiilter(position);
				}
			});
		}
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.back_iv:
			activity.onBackPressed();
			break;
		case R.id.setting_iv:
			//Save the filtered image
			File root = new File(Environment.getExternalStorageDirectory()+Constants.STORAGE_PATH,Constants.IMAGEPATH);
			if (!root.exists()) {
				root.mkdirs();
			}
			Calendar c = Calendar.getInstance();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyymmdd_hhmmss");
			String dt = fmt.format(c.getTime());
			String filename = "IMG_" + dt;
			File file = new File(root, filename + ".png");
			try {
				FileOutputStream fileWriteStream = new FileOutputStream(file);
				BitmapDrawable imgDrawable = (BitmapDrawable)img_filtered.getDrawable();
				Bitmap bmp = imgDrawable.getBitmap();
				bmp.compress(Bitmap.CompressFormat.PNG, 1, fileWriteStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			Bundle args = new Bundle(getArguments());
			args.putString(Constants.dataReceive, root+"/"+filename+".png");
			Log.d("", "profileEventMedia"+args.getString(Constants.KEY_EVENT_MEDIA_ID));
			Fragment activeFragment=null;
			if(args.getBoolean(Constants.KEY_IS_EDIT_FILTER_IMAGE_FRAGMENT)){
				activeFragment = EditFilterImageBroadCastFragment.getnewInstance(activity);
			}else{
				activeFragment = FilterImageBroadCastFragment.getnewInstance(activity);
			}
			
			activeFragment.setArguments(args);
			if(activity instanceof ImageFilterActivity){
				((ImageFilterActivity)activity).push(activeFragment);
			}
			/*
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.home_frame, activeFragment);
			ft.addToBackStack(null);
			ft.commit();*/

			break;
		}
	}

	@Override
	public void onClickButton(String userID, int itemPos, String status,
			String userPrivacy) {
		//Not implemented		
	}

	@Override
	public void onStatusReturn(String message) {
		//Show images
		updateViewVisibility(1);

	}
	@Override
	public void onResume() {
		super.onResume();
		title.setText(Constants.UI_Edit);
	}
}
