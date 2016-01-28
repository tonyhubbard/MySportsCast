package com.mysportsshare.mysportscast.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.IntentCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.application.MySportsApp;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;

public class RegisterActivity extends Activity {
	private static final String TAG = RegisterActivity.class.getName();
	// views
	private EditText firstName;
	private EditText lastName;
	private EditText email;
	private EditText pwd;
	private EditText confirmPwd;
	private ProgressDialog pd;
	private ImageView imgProfile;

	// values
	private String selectedImagePath;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int REQUEST_IMAGE_GALLERY = 2;
	private int desiredImageWidth = 300, desiredImageHeight = 300;
	private Bitmap rotatedBitmap;
	private boolean isFileImageUploaded;
	private String base64Image = "";
	Bitmap photo=null;
	final String ImgLoadSuccessStatus = "ImageLoaded";
	private Uri selectedImagePathUri;
	private LinearLayout imgProfileLayout;
	private String imgPath;
	private CheckBox termsAndConditionsCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		MySportsApp.setActivityContext(RegisterActivity.this);
		
		findViewById(R.id.back_iv).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		init();
	}

	/**
	 * initialize variables
	 */
	private void init() {
		firstName = (EditText) findViewById(R.id.first_name_et);
		lastName = (EditText) findViewById(R.id.last_name_et);
		email = (EditText) findViewById(R.id.reg_email_et);
		pwd = (EditText) findViewById(R.id.pwd_et);
		confirmPwd = (EditText) findViewById(R.id.pwd_confirm_et);
		imgProfile = (ImageView) findViewById(R.id.addProfilePicture);
		imgProfileLayout = (LinearLayout) findViewById(R.id.addProfilePictureLayout);
		termsAndConditionsCheckBox= (CheckBox) findViewById(R.id.terms_checkbox);

	}

	/**
	 * click event for register button
	 */
	public void clickRegister(View view) {

		if (!firstName.getText().toString().matches("")) {
			if (!lastName.getText().toString().matches("")) {
				if (!email.getText().toString().matches("")) {
					if (Utils.isValidEmail(email.getText().toString())) {
						if (!pwd.getText().toString().matches("")) {
							if (!confirmPwd.getText().toString().matches("")) {
								//This if condition written by Sathish Kumar
								if(termsAndConditionsCheckBox.isChecked())
								{
								if (pwd.getText()
										.toString()
										.matches(
												confirmPwd.getText().toString())) {
									triggerRegistrationService();
									/* Removes photo from manditory field
									 if (isFileImageUploaded) {
										triggerRegistrationService();

									} else {
										UIHelperUtil
										.showToastMessage("Take picture from gallery or capture from camera");
										UIHelperUtil
										.showToastMessage("Please add photo to sign up");
									}*/
								} else {
									UIHelperUtil
									.showToastMessage("Passwords are mismatched");
								}
							}
							else
							{
								UIHelperUtil
								.showToastMessage("Please check Terms & Conditions to sign up");

							} 
							}else {
								UIHelperUtil
								.showToastMessage("Please enter confirm password to sign up");
							}
						} else {
							UIHelperUtil.showToastMessage(getResources()
									.getString(R.string.empty_pwd));
						}
					} else {
						UIHelperUtil.showToastMessage(getResources().getString(
								R.string.invalid_email));
					}
				} else {
					UIHelperUtil.showToastMessage(getResources().getString(
							R.string.empty_email));
				}
			} else {
				UIHelperUtil.showToastMessage(getResources().getString(
						R.string.empty_last_name));
			}

		} else {
			UIHelperUtil.showToastMessage(getResources().getString(
					R.string.empty_first_name));
		}


	}

	private void triggerRegistrationService() {
		if (Utils.chkStatus()) {
			serviceForRegister(firstName
					.getText().toString(),
					lastName.getText()
					.toString(), email
					.getText()
					.toString(), pwd
					.getText()
					.toString());

		} else {
			Utils.networkAlertDialog(
					RegisterActivity.this,
					getResources()
					.getString(
							R.string.toast_no_network));
		}
	}
	
	private String uploadFile(String sourceUri, String ServerUrl,String firstName,String lastName,String email,String pwd) {

		String responseStr = "";
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		String eventLat = Constants.lati;
		String eventLng = Constants.longi;
		try {


			long totalSize = 0;

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(ServerUrl);

				MultipartEntity entity = new MultipartEntity();

				entity.addPart("first_name",	new StringBody(firstName));
				entity.addPart("last_name",	new StringBody(lastName));
				entity.addPart("email",	new StringBody(email));
				entity.addPart("pass",	new StringBody(pwd));
				entity.addPart("birth_date",	new StringBody(""));

				if(isFileImageUploaded){
					File sourceFile = new File(sourceUri);

					// Adding file data to http body
					entity.addPart("media_file", new FileBody(sourceFile));
				}else{
					// Adding file data to http body
					entity.addPart("media_file", new StringBody(""));
				}
				

				totalSize = entity.getContentLength();
				httppost.setEntity(entity);

				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseStr = EntityUtils.toString(r_entity);
				} else {
					responseStr = "Error occurred! Http Status Code: " + statusCode;
				}
			} catch (Exception ex) {
				Log.w("----catch (Exception ex) ---", " ");
				ex.printStackTrace();
				// Exception handling
			}

		}  catch (Exception e) {
			Log.e(Constants.logUrl, "Error connecting to Places API", e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return responseStr;
	}

	/**
	 * method for calling register service
	 */
	private void serviceForRegister(final String firstname, final String lastname,
			final String email, final String pwd) {

		String url = Constants.common_url
				+ getString(R.string.user_register);
		Log.i("", "URL login: " + url + "  " + email + "  " + pwd + ""
				+ firstname);
		
		String[] params = { url, imgPath };


		new AsyncTask<String, Void, String>() {
			ProgressDialog pd;

			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(RegisterActivity.this);
				pd.show();
				pd.setMessage("Please wait. Registering...");
				//				pd.setMessage("Please wait.");
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
			}

			@Override
			protected String doInBackground(String... params) {
				return uploadFile(params[1], params[0],firstname,lastname,email,pwd);
			}

			@Override
			protected void onPostExecute(String jsonResponse) {
				System.out.println("RESPONSE: " + jsonResponse);
				if (jsonResponse != null) {
					JSONObject jsonObject;
					String userId = "";
					String userName = "";
					String profileImg = "";
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject
								.getJSONObject("Response");
						String responseStr = resObj
								.getString("ResponseInfo");
						if (responseStr != null
								&& responseStr
								.equalsIgnoreCase("SUCCESS")) {
							isFileImageUploaded = false;
							userId = resObj.getString("user_id");
							userName = resObj.getString("first_name");
							profileImg = resObj
									.getString("profile_image");
							Intent loginintent = new Intent(
									RegisterActivity.this,
									MainActivity.class);
							loginintent
							.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(loginintent);
							SharedPreferencesUtils.setUserId(userId);
							SharedPreferencesUtils.setUserName(userName);
							SharedPreferencesUtils
							.setUserProfilePicPath(profileImg);
							finish();
						} else if (responseStr != null
								&& responseStr
								.equalsIgnoreCase("EMAIL ALREADY EXIST")) {
//							userId = SharedPreferencesUtils.getUserId();
							userId = resObj.getString("user_id");
							String registerType = resObj.getString("registered_from");
							Intent loginintent = new Intent(
									RegisterActivity.this,
									MainActivity.class);
							loginintent
							.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
							if(registerType != null && !TextUtils.isEmpty(registerType)){
								Utils.showAlertDialog(
										RegisterActivity.this,
										"Registration Fails!...",
										"Entered email already registered with "+registerType+". Please login with "+registerType+" account.");
							}else{
								Utils.showAlertDialog(
										RegisterActivity.this,
										"Registration Fails!...",
										"Entered email already registered. Please try with another email");
							}
							
						} else {
							UIHelperUtil
							.showToastMessage(getString(R.string.service_toast));
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					UIHelperUtil
					.showToastMessage(getString(R.string.service_toast));
				}
				
			};
		}.execute(params);

	}

	/**
	 * selecting image from camera or gallery.
	 * 
	 * @param v
	 */
	public void selectImage(View v) {
		final CharSequence[] options = { "Capture From Camera",
				"Choose from Gallery", "Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(
				RegisterActivity.this);
		builder.setTitle("Upload Photo");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Capture From Camera")) {
					Log.d(TAG, isSDCARDMounted() + " : " + "From Camera");
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
						photoPickerIntent.putExtra("return-data", true);
						startActivityForResult(photoPickerIntent,
								REQUEST_IMAGE_CAPTURE);
						Log.d(TAG, isSDCARDMounted() + " ; "
								+ REQUEST_IMAGE_CAPTURE);
					} else {
						UIHelperUtil
						.showToastMessage("You need to insert SD Card");
					}
				} else if (options[item].equals("Choose from Gallery")) {
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, REQUEST_IMAGE_GALLERY);
				} else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
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
		File root = new File(Environment.getExternalStorageDirectory()
				+ Constants.PROFILEIMAGEPATH, "profile_pic");
		if (!root.exists()) {
			root.mkdirs();
		}
		String filename = "" + System.currentTimeMillis();
		File file = new File(root, filename + ".jpeg");
		imgPath = root+"/"+filename+".jpeg";
		Uri muri = Uri.fromFile(file);
		selectedImagePath = muri.getPath();
		selectedImagePathUri = muri;
		Log.v("Pic paht from Camera", selectedImagePath);
		return muri;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "onActivity: " + requestCode + " : " + resultCode);
		if (resultCode == Activity.RESULT_OK) {
			// CalenderModel model = new CalenderModel();
			if (REQUEST_IMAGE_GALLERY == requestCode) {
				selectedImagePathUri = data.getData();
				selectedImagePath = selectedImagePathUri.toString();
				/*Uri selectedImage = data.getData();
				BitmapUtils.setImages(selectedImage.toString(), imgProfile,
						R.drawable.profile_pic_dummy);
				Log.v("", "selected Image: " + selectedImage);

				try {
					InputStream imgInputStream = getContentResolver().openInputStream(selectedImage);
					byte[] byteArray = new byte[imgInputStream.available()];
					imgInputStream.read(byteArray);
					base64Image = Base64.encodeToString(byteArray,Base64.DEFAULT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				isFileImageUploaded = true;*/
				isFileImageUploaded = true;
				performCrop();
			} else if (requestCode == REQUEST_IMAGE_CAPTURE) {
				/*	Log.d("PATH when choosed from camera: ", "" + selectedImagePath);
				int index = selectedImagePath.lastIndexOf("/");
				String filename = selectedImagePath.substring(index + 1,selectedImagePath.length());
				String filepath = Environment.getExternalStorageDirectory().getAbsolutePath()+Constants.PROFILEIMAGEPATH + "/profile_pic/";			
				BitmapUtils.setImages("file:///"+filepath+filename, imgProfile,
						R.drawable.profile_pic_dummy);

				// Converting image to base64 format
				InputStream imgInputStream = null;
				try {
					File tmpFile = new File(selectedImagePath);
					imgInputStream = new FileInputStream(tmpFile);
					byte[] byteArray = new byte[imgInputStream.available()];
					imgInputStream.read(byteArray);
					base64Image = Base64.encodeToString(byteArray,Base64.DEFAULT);

				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					try {
						if(imgInputStream !=null){
							imgInputStream.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}				
				isFileImageUploaded = true;
				 */
				isFileImageUploaded = true;
				performCrop();
			}else if (requestCode == Constants.REQUEST_IMAGE_CROP) {				
//				if(data!=null){
					//get the returned data
					/*Bundle extras = data.getExtras();
					if (extras != null) {
						if(photo!=null){
							photo.recycle();
						}
						//get the cropped bitmap
						photo = extras.getParcelable("data");
						imgProfile.setImageBitmap(photo);			
						try {
							ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
							photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
							byte[] byteArray = byteArrayOutputStream.toByteArray();
							base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
							selectedImagePath = ImgLoadSuccessStatus;
							isFileImageUploaded = true;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{*/
						int index = selectedImagePath.lastIndexOf("/");
						String filename = selectedImagePath.substring(index + 1,selectedImagePath.length());
						String filepath = Environment.getExternalStorageDirectory().getAbsolutePath()+Constants.PROFILEIMAGEPATH + "/profile_pic/";			
						//				selectedImagePath = "file:///"+filepath+filename;
						BitmapUtils.setImages("file:///"+filepath+filename, imgProfile,
								R.drawable.profile_pic_dummy);

						// Converting image to base64 format
						InputStream imgInputStream = null;
						try {
							File tmpFile = new File(selectedImagePath);
							imgInputStream = new FileInputStream(tmpFile);
							byte[] byteArray = new byte[imgInputStream.available()];
							imgInputStream.read(byteArray);
							base64Image = Base64.encodeToString(byteArray,Base64.DEFAULT);
							isFileImageUploaded = true;
						} catch (Exception e) {
							e.printStackTrace();
						}finally{
							try {
								if(imgInputStream !=null){
									imgInputStream.close();
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
//					}
//				}
			}
		} else {
			Log.v("", "OnActivityResult: " + resultCode);
		}
	}

	// for getting path of image
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA,
				MediaStore.Images.ImageColumns.ORIENTATION };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	private void performCrop(){
		try {
			//call the standard crop action intent (the user device may not support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
			//indicate image type and Uri
			cropIntent.setDataAndType(selectedImagePathUri, "image/*");
			cropIntent.putExtra(MediaStore.EXTRA_OUTPUT,getTempFile());
			cropIntent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
			//set crop properties
			cropIntent.putExtra("crop", "true");
			//indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			//retrieve data on return
//			cropIntent.putExtra("return-data", true);
			//start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, Constants.REQUEST_IMAGE_CROP);
		}
		catch(ActivityNotFoundException anfe){
			//display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			UIHelperUtil.showToastMessage( errorMessage);
		}
	}
	
	public void onTermsAndConditionsTextClick(View view)
	{
		startActivity(new Intent(RegisterActivity.this, TermAndConditionsActivity.class));
	}
}
