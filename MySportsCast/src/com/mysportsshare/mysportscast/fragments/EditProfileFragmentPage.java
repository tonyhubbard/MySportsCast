package com.mysportsshare.mysportscast.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.models.UserProfileInfo;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.DataPassListener;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class EditProfileFragmentPage extends Fragment implements
		OnClickListener {
	// Creating references to the UI views
	ImageView imgProfile;
	ImageView userProfilePencilImg;
	RelativeLayout edProfileUserImgLayout;
	EditText firstNameEd;
	EditText lastNameEd;
	EditText userNameEd;
	EditText emailIDEd;
	LinearLayout privacyRg;
	ImageView followersRb;
	ImageView EveryoneRb;
	TextView followersTv;
	TextView EveryoneTv;
	Button saveBtn;
	ImageView backBtn;
	TextView title;

	UserProfileInfo userDetails;

	String selectedImagePath = "";
	String base64Image; // holds base64 of selected image
	Bitmap photo = null;
	// Holds the call back listener to pass data
	DataPassListener mCallback;
	final String ImgLoadSuccessStatus = "ImageLoaded";
	private Uri selectedImagePathUri;
	private Activity activity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Make sure that container activity implement the callback interface
		this.activity = activity;
		try {
			mCallback = (DataPassListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement DataPassListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_editprofile_,
				container, false);

		// initializing UI components
		init(view);

		return view;
	}

	/* Initializes all UI views */
	private void init(View view) {
		imgProfile = (ImageView) view.findViewById(R.id.edProfile_user_img);
		userProfilePencilImg = (ImageView) view
				.findViewById(R.id.edProfile_user_img_edit);
		edProfileUserImgLayout = (RelativeLayout) view
				.findViewById(R.id.edProfile_user_img_layout);
		firstNameEd = (EditText) view
				.findViewById(R.id.edProfile_user_fname_text);
		userNameEd = (EditText) view
				.findViewById(R.id.edProfile_user_uname_text);
		lastNameEd = (EditText) view
				.findViewById(R.id.edProfile_user_lname_text);
		emailIDEd = (EditText) view
				.findViewById(R.id.edProfile_user_email_text);
		privacyRg = (LinearLayout) view.findViewById(R.id.edProfile_privacy_rg);
		followersRb = (ImageView) view
				.findViewById(R.id.edProfile_privacy_followers_iv);
		EveryoneRb = (ImageView) view
				.findViewById(R.id.edProfile_privacy_everyone_iv);
		followersTv = (TextView) view
				.findViewById(R.id.edProfile_privacy_followers_tv);
		EveryoneTv = (TextView) view
				.findViewById(R.id.edProfile_privacy_everyone_tv);
		saveBtn = (Button) view.findViewById(R.id.edProfile_save_btn);
		backBtn = (ImageView) activity.findViewById(R.id.back_iv);

		userProfilePencilImg.setOnClickListener(this);
		edProfileUserImgLayout.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		imgProfile.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		followersRb.setOnClickListener(this);
		EveryoneRb.setOnClickListener(this);
		followersTv.setOnClickListener(this);
		EveryoneTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.edProfile_user_img_layout: // User clicks on empty space
		case R.id.edProfile_user_img_edit: // User clicks on edit profile
											// picture
		case R.id.edProfile_user_img:
			// Goes to previous fragment on pressing back
			selectImage();
			break;
		case R.id.back_iv:
			((MainActivity) activity).onBackPressed();
			// mCallback.passData(SharedPreferencesUtils.getUserId(),
			// Constants.ToFragment_userProfile);
			break;
		case R.id.edProfile_save_btn:
			String validStr = validateInputFields();
			if (validStr.trim().length() == 0) {
				// Upload modifications to the web server
				updateUserProfileDetailsToServer();
			} else {
				// Prompt validation message to the user
				Utils.showAlertDialog(activity, "Validation", validStr);
				firstNameEd.setFocusable(true);
			}
			break;
		case R.id.edProfile_privacy_followers_iv: // User selects followers
													// privacy
			privacyRg.setTag("friends"); // Tag used to find which privacy
											// option selected internally
			followersRb
					.setBackgroundResource(R.drawable.edit_profile_radio_selected);
			EveryoneRb
					.setBackgroundResource(R.drawable.edit_profile_radiobutton);
			break;
		case R.id.edProfile_privacy_followers_tv: // User selects followers
													// privacy
			privacyRg.setTag("friends");
			followersRb
					.setBackgroundResource(R.drawable.edit_profile_radio_selected);
			EveryoneRb
					.setBackgroundResource(R.drawable.edit_profile_radiobutton);
			break;
		case R.id.edProfile_privacy_everyone_iv:
			privacyRg.setTag("everyone");
			EveryoneRb
					.setBackgroundResource(R.drawable.edit_profile_radio_selected);
			followersRb
					.setBackgroundResource(R.drawable.edit_profile_radiobutton);
			break;
		case R.id.edProfile_privacy_everyone_tv:
			privacyRg.setTag("everyone");
			EveryoneRb
					.setBackgroundResource(R.drawable.edit_profile_radio_selected);
			followersRb
					.setBackgroundResource(R.drawable.edit_profile_radiobutton);
			break;
		}
	}

	/*
	 * This method loads the user details from the serverand displays on the
	 * screen
	 */
	@Override
	public void onStart() {
		super.onStart();

		// Updating title bar fields
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);
		title.setText("EDIT PROFILE");
		ImageView backBtn = (ImageView) activity.findViewById(R.id.back_iv);
		backBtn.setVisibility(View.VISIBLE);
		ImageView settingBtn = (ImageView) activity
				.findViewById(R.id.setting_iv);
		settingBtn.setVisibility(View.INVISIBLE);

		if (userDetails == null) {
			getUserProfileDetailsFromServer(); // Try to load details from the
												// server
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		// Updating title bar fields
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		title.setVisibility(View.VISIBLE);
		// title.setText(Constants.userProf_Title);
		ImageView backBtn = (ImageView) activity.findViewById(R.id.back_iv);
		backBtn.setVisibility(View.VISIBLE);
		ImageView settingBtn = (ImageView) activity
				.findViewById(R.id.setting_iv);
		settingBtn.setVisibility(View.VISIBLE);

	}

	public String validateInputFields() {
		String tmpString = "";
		if (firstNameEd.getText().toString().trim().length() == 0) {
			tmpString = "Please enter first name";
		} else if (lastNameEd.getText().toString().trim().length() == 0) {
			tmpString = "Please enter last name";
		} else if (userNameEd.getText().toString().trim().length() == 0) {
			tmpString = "Please enter user name";
		}
		return tmpString;
	}

	private void updateUI() {
		if (userDetails != null) {
			firstNameEd.setText(userDetails.getFirstName());
			lastNameEd.setText(userDetails.getLastName());
			userNameEd.setText(userDetails.getUserName());
			if (userDetails.getEmail().length() > 0) {
				emailIDEd.setText(userDetails.getEmail());
			} else {
				emailIDEd.setText("private(Confidential)");
			}
			privacyRg.setClickable(true);
			if (userDetails.getPrivacy().equalsIgnoreCase("friends")) {
				privacyRg.setTag("friends");
				followersRb
						.setBackgroundResource(R.drawable.edit_profile_radio_selected);
				EveryoneRb
						.setBackgroundResource(R.drawable.edit_profile_radiobutton);
			} else {
				privacyRg.setTag("everyone");
				EveryoneRb
						.setBackgroundResource(R.drawable.edit_profile_radio_selected);
				followersRb
						.setBackgroundResource(R.drawable.edit_profile_radiobutton);
			}
			if (selectedImagePath.trim().length() == 0) {
				// Update profile image
				BitmapUtils.setImages(userDetails.getPhoto(), imgProfile,
						R.drawable.profile_pic_dummy);

				// Loading the existing profile pic in base64 format
				if (userDetails.getPhoto().trim().length() > 0) {
					Bitmap bitmap = ((BitmapDrawable) imgProfile.getDrawable())
							.getBitmap();
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100,
							byteArrayOutputStream);
					byte[] byteArray = byteArrayOutputStream.toByteArray();
					base64Image = Base64.encodeToString(byteArray,
							Base64.DEFAULT);
				} else {
					base64Image = "";
				}
			} else {
				// if(selectedImagePath.equalsIgnoreCase(ImgLoadSuccessStatus)){
				// //TODO: display the selected image
				// }else{
				// BitmapUtils.setImages(selectedImagePath, imgProfile,
				// R.drawable.profile_pic_dummy);
				// }
			}

			saveBtn.setEnabled(true);
		} else {
			firstNameEd.setText("");
			lastNameEd.setText("");
			emailIDEd.setText("");
			privacyRg.setClickable(false);
			saveBtn.setEnabled(false);
		}

		firstNameEd.setFocusable(true);
	}

	/*------------------------photo-----------------------------*/
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
		File root = new File(Environment.getExternalStorageDirectory()
				+ Constants.PROFILEIMAGEPATH, "profile_pic");
		if (!root.exists()) {
			root.mkdirs();
		}
		String filename = "" + System.currentTimeMillis();
		File file = new File(root, filename + ".jpeg");
		Uri muri = Uri.fromFile(file);
		selectedImagePath = muri.getPath();
		selectedImagePathUri = muri;
		// Log.v("Pic paht from Camera", selectedImagePath);
		return muri;
	}

	// for getting path of image
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA,
				MediaStore.Images.ImageColumns.ORIENTATION };
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/* For getting image from camera & gallery */
	public void selectImage() {
		final CharSequence[] options = { "Capture From Camera",
				"Choose from Gallery", "Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Upload Photo");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Capture From Camera")) {

					if (isSDCARDMounted()) {
						Intent photoPickerIntent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						/*
						 * File tempfile = new
						 * File(Environment.getExternalStorageDirectory
						 * ()+Utils.PROFILEIMAGEPATH, "sportsprofilepic.jpg");
						 * Uri uritemp = Uri.fromFile(tempfile);
						 */
						photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								getTempFile());
						photoPickerIntent.putExtra("outputFormat",
								Bitmap.CompressFormat.JPEG.toString());
						/*
						 * // ******** code for crop image
						 * photoPickerIntent.putExtra("crop", "true");
						 * photoPickerIntent.putExtra("aspectX", 1);
						 * photoPickerIntent.putExtra("aspectY", 1);
						 * photoPickerIntent.putExtra("scale", true);
						 * photoPickerIntent.putExtra("outputX", 200);
						 * photoPickerIntent.putExtra("outputY", 200);
						 */

						photoPickerIntent.putExtra("return-data", true);
						startActivityForResult(photoPickerIntent,
								Constants.REQUEST_IMAGE_CAPTURE);
					} else {
						UIHelperUtil
								.showToastMessage("You need to insert SD Card");
					}
				} else if (options[item].equals("Choose from Gallery")) {
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					/*
					 * // ******** code for crop image i.putExtra("crop",
					 * "true"); i.putExtra("aspectX", 0); i.putExtra("aspectY",
					 * 0); i.putExtra("outputX", 200); i.putExtra("outputY",
					 * 200);
					 */
					i.putExtra(MediaStore.EXTRA_OUTPUT, getTempFile());
					i.putExtra("outputFormat",
							Bitmap.CompressFormat.JPEG.toString());
					i.putExtra("return-data", true);

					startActivityForResult(i, Constants.REQUEST_IMAGE_GALLERY);
				} else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// CalenderModel model = new CalenderModel();
			if (Constants.REQUEST_IMAGE_GALLERY == requestCode) {
				selectedImagePathUri = data.getData();
				selectedImagePath = selectedImagePathUri.toString();
				/*
				 * BitmapUtils.setImages(selectedImage.toString(), imgProfile,
				 * R.drawable.profile_pic_dummy); Log.v("", "selected Image: " +
				 * selectedImage);
				 * 
				 * try { InputStream imgInputStream =
				 * activity.getContentResolver().openInputStream(selectedImage);
				 * byte[] byteArray = new byte[imgInputStream.available()];
				 * imgInputStream.read(byteArray); base64Image =
				 * Base64.encodeToString(byteArray,Base64.DEFAULT); } catch
				 * (Exception e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */

				performCrop();
				/*
				 * Bundle extras2 = data.getExtras(); if (extras2 != null) {
				 * if(photo!=null){ photo.recycle(); } photo =
				 * extras2.getParcelable("data");
				 * imgProfile.setImageBitmap(photo); try { ByteArrayOutputStream
				 * byteArrayOutputStream = new ByteArrayOutputStream();
				 * photo.compress(Bitmap.CompressFormat.PNG, 100,
				 * byteArrayOutputStream); byte[] byteArray =
				 * byteArrayOutputStream.toByteArray(); base64Image =
				 * Base64.encodeToString(byteArray, Base64.DEFAULT);
				 * selectedImagePath = ImgLoadSuccessStatus; } catch (Exception
				 * e) { // TODO Auto-generated catch block e.printStackTrace();
				 * } }
				 */
			} else if (requestCode == Constants.REQUEST_IMAGE_CAPTURE) {

				/*
				 * Capture image via camera without croping
				 * Log.d("PATH when choosed from camera: ", "" +
				 * selectedImagePath); int index =
				 * selectedImagePath.lastIndexOf("/"); String filename =
				 * selectedImagePath.substring(index +
				 * 1,selectedImagePath.length()); String filepath =
				 * Environment.getExternalStorageDirectory
				 * ().getAbsolutePath()+Constants.PROFILEIMAGEPATH +
				 * "/profile_pic/"; // selectedImagePath =
				 * "file:///"+filepath+filename;
				 * BitmapUtils.setImages("file:///"+filepath+filename,
				 * imgProfile, R.drawable.profile_p if(data!=null){ //get the
				 * returned data Bundle extras = data.getExtras(); if (extras !=
				 * null) { if(photo!=null){ photo.recycle(); } //get the cropped
				 * bitmap photo = extras.getParcelable("data");
				 * imgProfile.setImageBitmap(photo); try { ByteArrayOutputStream
				 * byteArrayOutputStream = new ByteArrayOutputStream();
				 * photo.compress(Bitmap.CompressFormat.PNG, 100,
				 * byteArrayOutputStream); byte[] byteArray =
				 * byteArrayOutputStream.toByteArray(); base64Image =
				 * Base64.encodeToString(byteArray, Base64.DEFAULT);
				 * selectedImagePath = ImgLoadSuccessStatus; } catch (Exception
				 * e) { // TODO Auto-generated catch block e.printStackTrace();
				 * } }else{ int index = selectedImagePath.lastIndexOf("/");
				 * String filename = selectedImagePath.substring(index +
				 * 1,selectedImagePath.length()); String filepath =
				 * Environment.getExternalStorageDirectory
				 * ().getAbsolutePath()+Constants.PROFILEIMAGEPATH +
				 * "/profile_pic/"; // selectedImagePath =
				 * "file:///"+filepath+filename;
				 * BitmapUtils.setImages("file:///"+filepath+filename,
				 * imgProfile, R.drawable.profile_pic_dummy);
				 * 
				 * // Converting image to base64 format InputStream
				 * imgInputStream = null; try { File tmpFile = new
				 * File(selectedImagePath); imgInputStream = new
				 * FileInputStream(tmpFile); byte[] byteArray = new
				 * byte[imgInputStream.available()];
				 * imgInputStream.read(byteArray); base64Image =
				 * Base64.encodeToString(byteArray,Base64.DEFAULT); } catch
				 * (Exception e) { e.printStackTrace(); }finally{ try {
				 * if(imgInputStream !=null){ imgInputStream.close(); } } catch
				 * (IOException e) { e.printStackTrace(); } } } } ic_dummy);
				 * 
				 * // Converting image to base64 format InputStream
				 * imgInputStream = null; try { File tmpFile = new
				 * File(selectedImagePath); imgInputStream = new
				 * FileInputStream(tmpFile); byte[] byteArray = new
				 * byte[imgInputStream.available()];
				 * imgInputStream.read(byteArray); base64Image =
				 * Base64.encodeToString(byteArray,Base64.DEFAULT); } catch
				 * (Exception e) { e.printStackTrace(); }finally{ try {
				 * if(imgInputStream !=null){ imgInputStream.close(); } } catch
				 * (IOException e) { e.printStackTrace(); } }
				 */
				// Crop captured image
				performCrop();
			} else if (requestCode == Constants.REQUEST_IMAGE_CROP) {
				/*
				 * if(data!=null){ //get the returned data Bundle extras =
				 * data.getExtras(); if (extras != null) { if(photo!=null){
				 * photo.recycle(); } //get the cropped bitmap photo =
				 * extras.getParcelable("data");
				 * imgProfile.setImageBitmap(photo); try { ByteArrayOutputStream
				 * byteArrayOutputStream = new ByteArrayOutputStream();
				 * photo.compress(Bitmap.CompressFormat.PNG, 100,
				 * byteArrayOutputStream); byte[] byteArray =
				 * byteArrayOutputStream.toByteArray(); base64Image =
				 * Base64.encodeToString(byteArray, Base64.DEFAULT);
				 * selectedImagePath = ImgLoadSuccessStatus; } catch (Exception
				 * e) { // TODO Auto-generated catch block e.printStackTrace();
				 * } }else{
				 */
				int index = selectedImagePath.lastIndexOf("/");
				String filename = selectedImagePath.substring(index + 1,
						selectedImagePath.length());
				String filepath = Environment.getExternalStorageDirectory()
						.getAbsolutePath()
						+ Constants.PROFILEIMAGEPATH
						+ "/profile_pic/";
				// selectedImagePath = "file:///"+filepath+filename;
				BitmapUtils.setImages("file:///" + filepath + filename,
						imgProfile, R.drawable.profile_pic_dummy);

				// Converting image to base64 format
				InputStream imgInputStream = null;
				try {
					File tmpFile = new File(selectedImagePath);
					imgInputStream = new FileInputStream(tmpFile);
					byte[] byteArray = new byte[imgInputStream.available()];
					imgInputStream.read(byteArray);
					base64Image = Base64.encodeToString(byteArray,
							Base64.DEFAULT);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (imgInputStream != null) {
							imgInputStream.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// }
				// }
			}
		} else {
			Log.v("", "OnActivityResult: " + resultCode);
		}

	}

	private void performCrop() {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(selectedImagePathUri, "image/*");
			cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempFile());
			cropIntent.putExtra("outputFormat",
					Bitmap.CompressFormat.JPEG.toString());
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

	/*-------------------------------------Server interaction starts--------------------------------*/
	/*
	 * Updates user profile details to web server through http request.
	 */
	private void updateUserProfileDetailsToServer() {
		// web service url to update user details
		String url = Constants.common_url
				+ getString(R.string.update_user_details);

		// Prepare name value list data for user details
		List<NameValuePair> userIDNameValuePairs = new ArrayList<NameValuePair>();
		userIDNameValuePairs.add(new BasicNameValuePair("user_id",
				SharedPreferencesUtils.getUserId()));
		userIDNameValuePairs.add(new BasicNameValuePair("first_name",
				firstNameEd.getText().toString().trim()));
		userIDNameValuePairs.add(new BasicNameValuePair("last_name", lastNameEd
				.getText().toString().trim()));
		userIDNameValuePairs.add(new BasicNameValuePair("username", userNameEd
				.getText().toString().trim()));
		userIDNameValuePairs.add(new BasicNameValuePair("profile_image",
				base64Image));
		if (privacyRg.getTag().toString().equals("friends")) {
			userIDNameValuePairs.add(new BasicNameValuePair("privacy_type",
					"friends"));
		} else {
			userIDNameValuePairs.add(new BasicNameValuePair("privacy_type",
					"public"));
		}

		// Pass updated user details to the web server in background
		JsonParser.callBackgroundService(url, userIDNameValuePairs,
				new JsonServiceListener() {
					ProgressDialog pd;

					public void showProgress() {
						pd = new ProgressDialog(activity);
						pd.setMessage("Please wait. Updating user details...");
						pd.setCancelable(false);
						pd.setCanceledOnTouchOutside(false);
						pd.show();
					}

					public void hideProgress() {

						pd.dismiss();
					}

					public void navigateToPrevFragment() {

					}

					@Override
					public void parseJsonResponse(String jsonResponse) {
						System.out.println(" Event Service RESPONSE: "
								+ jsonResponse);
						if (jsonResponse != null) {
							JSONObject jsonObject;
							try {
								if (jsonResponse != null) {
									jsonObject = new JSONObject(jsonResponse);
									JSONObject resObj = jsonObject
											.getJSONObject("Response");
									String responseStr = resObj
											.getString("ResponseInfo");
									if (responseStr != null
											&& responseStr
													.equalsIgnoreCase("SUCCESS")) {
										// Prompt successfull message to the
										// user
										UIHelperUtil
												.showToastMessage("Successfully updated");
										((MainActivity) activity)
												.onBackPressed();
									} else {
										// Prompt validation message to the user
										UIHelperUtil
												.showToastMessage("Unable to to update. "
														+ responseStr);
									}
								}
							} catch (Exception ex) {
								userDetails = null;
							}
						} else {
							// Prompt validation message to the user
							UIHelperUtil
									.showToastMessage("Something went wrong with server!...");
						}
					}

				});
	}

	/*
	 * Receives the user profile details from the server based on the user id.
	 * and update user details in global variable(userDetails)
	 */
	private void getUserProfileDetailsFromServer() {
		// web service url to get user details
		String url = Constants.common_url
				+ getString(R.string.get_user_details);

		// Prepare name value list data(i.e. user_id is embedded in a list)
		List<NameValuePair> userIDNameValuePairs = new ArrayList<NameValuePair>();
		userIDNameValuePairs.add(new BasicNameValuePair("user_id",
				SharedPreferencesUtils.getUserId()));

		// Pass user_id details to the web server in background
		JsonParser.callBackgroundService(url, userIDNameValuePairs,
				new JsonServiceListener() {
					ProgressDialog pd;

					public void showProgress() {
						pd = new ProgressDialog(activity);
						pd.setMessage("Please wait. Loading user details...");
						pd.setCancelable(false);
						pd.setCanceledOnTouchOutside(false);
						pd.show();
					}

					public void hideProgress() {
						pd.dismiss();
					}

					public void navigateToPrevFragment() {

					}

					@Override
					public void parseJsonResponse(String jsonResponse) {
						Log.d(Constants.logUrl, " Event Service RESPONSE: "
								+ jsonResponse);
						if (jsonResponse != null) {
							JSONObject jsonObject;
							try {
								jsonObject = new JSONObject(jsonResponse);
								JSONObject resObj = jsonObject
										.getJSONObject("Response");
								String responseStr = resObj
										.getString("ResponseInfo");
								if (responseStr != null
										&& responseStr
												.equalsIgnoreCase("SUCCESS")) {
									JSONObject userObj = resObj
											.getJSONObject("user_details");
									userDetails = new UserProfileInfo();
									userDetails.setFirstName(userObj
											.getString("first_name"));
									userDetails.setLastName(userObj
											.getString("last_name"));
									userDetails.setUserName(userObj
											.getString("username"));
									userDetails.setEmail(userObj
											.getString("email"));
									userDetails.setPhoto(userObj
											.getString("profile_image"));
									userDetails.setPrivacy(userObj
											.getString("privacy_type"));

									// Update user details on the screen
									updateUI();

								} else {
									userDetails = null;
								}
							} catch (Exception ex) {
								userDetails = null;
							}
						}
					}

				});
	}

}

/*-------------------------------------Server interaction ends--------------------------------*/