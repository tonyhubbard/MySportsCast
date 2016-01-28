package com.mysportsshare.mysportscast.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Session;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.ImageFilterActivity;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SocialNetworkingUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;

public class PhotosFragmentsFromHome extends Fragment implements
OnClickListener {

	private ImageView event_media_img_pick_gallery_btn;
	private ImageView event_media_img_pick_camera_btn;

	private String selectedImagePath;
	private Uri selectedImagePathUri;

	final String ImgLoadSuccessStatus = "ImageLoaded";

	Bitmap photo = null;

	Activity activity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_photos, container, false);
		init(view);
		return view;
	}

	private void init(View layoutView) {
		event_media_img_pick_gallery_btn = (ImageView) layoutView
				.findViewById(R.id.event_media_img_pick_gallery_btn);
		event_media_img_pick_camera_btn = (ImageView) layoutView
				.findViewById(R.id.event_media_img_pick_camera_btn);

		event_media_img_pick_gallery_btn.setOnClickListener(this);
		event_media_img_pick_camera_btn.setOnClickListener(this);
	}

	public static Fragment newInstance(Context context) {
		f = new PhotosFragmentsFromHome();

		return f;
	}

	static PhotosFragmentsFromHome f = new PhotosFragmentsFromHome();

	public static Fragment getInstance(Context context) {
		if (f == null) {
			f = new PhotosFragmentsFromHome();
		}
		return f;
	}

	@Override
	public void onResume() {
		super.onResume();

		TextView title = (TextView) activity.findViewById(R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);

		TextView eventtype = (TextView) activity
				.findViewById(R.id.title_bar_tv_event_type);
		title.setText("ADD PHOTO");
		eventtype.setVisibility(View.GONE);

		ImageView settingIv = (ImageView) activity
				.findViewById(R.id.setting_iv);
		settingIv.setVisibility(View.GONE);

		ImageView searchIv = (ImageView) activity.findViewById(R.id.search_iv);
		searchIv.setVisibility(View.GONE);

		ImageView backIv = (ImageView) activity.findViewById(R.id.back_iv);
		backIv.setVisibility(View.VISIBLE);
		backIv.setOnClickListener(this);

		ImageView addaneventIv = (ImageView) activity
				.findViewById(R.id.add_an_event_iv);
		addaneventIv.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.event_media_img_pick_camera_btn:
			if (isSDCARDMounted()) {
				Intent photoPickerIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						getTempFile());
				photoPickerIntent.putExtra("outputFormat",
						Bitmap.CompressFormat.PNG.toString());
				photoPickerIntent.putExtra("return-data", true);
				startActivityForResult(photoPickerIntent,
						Constants.REQUEST_IMAGE_CAPTURE);
				Log.d(Constants.logUrl, isSDCARDMounted() + " ; "
						+ Constants.REQUEST_IMAGE_CAPTURE);
			} else {
				UIHelperUtil.showToastMessage("You need to insert SD Card");
			}
			// UIHelperUtil.showToastMessage("Yet to implement");
			break;
		case R.id.event_media_img_pick_gallery_btn:
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, Constants.REQUEST_IMAGE_GALLERY);
			// UIHelperUtil.showToastMessage("Yet to implement");

			break;
		case R.id.back_iv:
			closeWindow();
			break;
		default:
			UIHelperUtil.showToastMessage("Select button");
			break;
		}
	}

	private void closeWindow() {
		if (activity != null) {
			(activity).onBackPressed();
		}
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
		String dt = fmt.format(c.getTime()); // .toString()
		File root = new File(Environment.getExternalStorageDirectory()
				+ Constants.STORAGE_PATH, Constants.IMAGEPATH);
		if (!root.exists()) {
			root.mkdirs();
		}
		String filename = "IMG_" + dt;
		File file = new File(root, filename + ".jpeg");
		Uri muri = Uri.fromFile(file);
		selectedImagePath = muri.getPath();
		selectedImagePathUri = muri;
		Log.v("Pic paht from Camera", selectedImagePath);
		return muri;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(Constants.logUrl, "onActivityResult: " + requestCode + " : "
				+ resultCode);
		if (resultCode == Activity.RESULT_OK) {
			// CalenderModel model = new CalenderModel();
			if (Constants.REQUEST_IMAGE_GALLERY == requestCode) {
				selectedImagePathUri = data.getData();
				selectedImagePath = selectedImagePathUri.toString();

				/*
				 * Uri selectedImage = data.getData(); Intent imageFilter = new
				 * Intent(activity,ImageFilterActivity.class);
				 * imageFilter.putExtra(Constants.dataReceive,
				 * selectedImage.toString());
				 * activity.startActivity(imageFilter);
				 */
				performCrop();
			} else if (requestCode == Constants.REQUEST_IMAGE_CAPTURE) {
				Log.d("PATH when choosed from camera: ", "" + selectedImagePath);
				/*
				 * int index = selectedImagePath.lastIndexOf("/"); String
				 * filename = selectedImagePath.substring(index +
				 * 1,selectedImagePath.length()); String filepath =
				 * Environment.getExternalStorageDirectory
				 * ().getAbsolutePath()+Constants.STORAGE_PATH +
				 * "/"+Constants.IMAGEPATH;
				 * 
				 * Intent imageFilter = new
				 * Intent(activity,ImageFilterActivity.class);
				 * imageFilter.putExtra(Constants.dataReceive,
				 * "file:///"+filename + filepath);
				 * activity.startActivity(imageFilter);
				 */

				Calendar c = Calendar.getInstance();
				SimpleDateFormat fmt = new SimpleDateFormat("yyyymmdd_hhmmss");
				String dt = fmt.format(c.getTime()); // .toString()
				File root = new File(Environment.getExternalStorageDirectory()
						+ Constants.STORAGE_PATH, Constants.IMAGEPATH);
				if (!root.exists()) {
					root.mkdirs();
				}
				String filename = "IMG_" + dt;
				File file = new File(root, filename + ".jpeg");
				adjustImageOrientation(file);
				performCrop();
			} else if (requestCode == Constants.REQUEST_IMAGE_CROP) {

				/*
				 * if(data!=null){ //get the returned data Bundle extras =
				 * data.getExtras(); if (extras != null) { if(photo!=null){
				 * photo.recycle(); } //get the cropped bitmap photo =
				 * extras.getParcelable("data"); try {
				 * 
				 * //Save crop bitmap to file Uri imgPath = getTempFile(); File
				 * file = new File(selectedImagePath); FileOutputStream fos =
				 * new FileOutputStream(file);
				 * photo.compress(CompressFormat.PNG, 1, fos); int index =
				 * selectedImagePath.lastIndexOf("/"); String filename =
				 * selectedImagePath.substring(index +
				 * 1,selectedImagePath.length()); String filepath =
				 * Environment.getExternalStorageDirectory().getAbsolutePath()+
				 * Constants.STORAGE_PATH+"/"+Constants.IMAGEPATH+"/";
				 * selectedImagePath = "file:///"+filepath+filename; //Start
				 * image filter activity Intent imageFilter = new
				 * Intent(activity,ImageFilterActivity.class);
				 * imageFilter.putExtra(Constants.dataReceive,
				 * selectedImagePath); activity.startActivity(imageFilter); }
				 * catch (Exception e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); } }else{ int index =
				 * selectedImagePath.lastIndexOf("/"); String filename =
				 * selectedImagePath.substring(index +
				 * 1,selectedImagePath.length()); String filepath =
				 * Environment.getExternalStorageDirectory().getAbsolutePath()+
				 * Constants.STORAGE_PATH+"/"+Constants.IMAGEPATH+"/";
				 * selectedImagePath = "file:///"+filepath+filename;
				 * 
				 * 
				 * Intent imageFilter = new
				 * Intent(activity,ImageFilterActivity.class);
				 * imageFilter.putExtra(Constants.dataReceive,
				 * selectedImagePath); activity.startActivity(imageFilter); } }
				 */
				performFilterActionOnImage();

			}
		} else {
			Log.v("", "OnActivityResult: " + resultCode);
		}
	}

	
	public void adjustImageOrientation(File f) {
		Bitmap image = decodeFileFromPath(selectedImagePath);
		int rotate = 0;
		try {

			ExifInterface exif = new ExifInterface(selectedImagePath);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;

			case ExifInterface.ORIENTATION_NORMAL:
				rotate = 0;
				break;
			case 0: rotate = 90;
				break;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rotate != 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(rotate);

			image = Bitmap.createBitmap(image, 0, 0, image.getWidth(),
					image.getHeight(), matrix, true);
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(selectedImagePath);
			image.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
			// PNG is a lossless format, the compression factor (100) is ignored
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		image.recycle();
	}
	
	private Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }
	
	private Bitmap decodeFileFromPath(String path){
        Uri uri = getImageUri(path);
        InputStream in = null;
        try {
            in = activity.getContentResolver().openInputStream(uri);

            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            int inSampleSize = 1024;
            if (o.outHeight > inSampleSize || o.outWidth > inSampleSize) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(inSampleSize / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = activity.getContentResolver().openInputStream(uri);
            Bitmap b = BitmapFactory.decodeStream(in, null, o2);
            in.close();

            return b;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	private void performFilterActionOnImage() {
		int index = selectedImagePath.lastIndexOf("/");
		String filename = selectedImagePath.substring(index + 1,
				selectedImagePath.length());
		String filepath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ Constants.STORAGE_PATH
				+ "/"
				+ Constants.IMAGEPATH + "/";
		selectedImagePath = "file:///" + filepath + filename;

		Bundle bundle = getArguments();
		if (bundle == null) {
			bundle = new Bundle();
		}
		if(activity instanceof MainActivity){
			Fragment fmt = ((MainActivity) activity).getLastButOneFragment();
			if (fmt instanceof EditFilterImageBroadCastFragment) {
				bundle.putBoolean(Constants.KEY_IS_EDIT_FILTER_IMAGE_FRAGMENT, true);
			}
		}else if(activity instanceof ImageFilterActivity){
			bundle.putBoolean(Constants.KEY_IS_EDIT_FILTER_IMAGE_FRAGMENT, true);
		}
		Log.d("",
				"profileEventMedia" + bundle.getString(Constants.KEY_EVENT_MEDIA_ID));
		bundle.putString(Constants.dataReceive, selectedImagePath);
		Intent imageFilter = new Intent(activity, ImageFilterActivity.class);
		imageFilter.putExtras(bundle);
		activity.startActivityForResult(imageFilter, 1200);
		closeWindow();

	}

	// Perform crop
	private void performCrop() {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(selectedImagePathUri, "image/*");
			cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempFile());
			// cropIntent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			// cropIntent.putExtra("outputX", 200);
			// cropIntent.putExtra("outputY", 200);
			// retrieve data on return
			cropIntent.putExtra("return-data", false);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, Constants.REQUEST_IMAGE_CROP);
		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			UIHelperUtil.showToastMessage(errorMessage);
		}
	}
}
