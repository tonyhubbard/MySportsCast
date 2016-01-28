package com.mysportsshare.mysportscast.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CalendarEventsActivity;
import com.mysportsshare.mysportscast.activity.InvitePeopleActivity;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.adapters.CommonAdapter;
import com.mysportsshare.mysportscast.adapters.SportsAdapter;
import com.mysportsshare.mysportscast.models.GeoData;
import com.mysportsshare.mysportscast.models.SearchInfo;
import com.mysportsshare.mysportscast.models.SportModel;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.CustomLog;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;
import com.mysportsshare.mysportscast.utils.Utils;
import com.mysportsshare.mysportscast.view.FlowLayout;

//import com.mysportsshare.mysportscast.adapters.PlaceAutocompleteAdapter;

public class AddanEventFragment extends Fragment implements OnClickListener {

	// Title bar components
	private TextView title;
	private ImageView backBtn;
	private ImageView settingBtn;
	private ImageView coverPhotoImg;
	private ImageView addEventBtn;
	private ImageView searchHeaderBtn;
	private TextView tvEventType;
	private RelativeLayout search_header_llyt;
	
	// views
	private LinearLayout selectTeamLlyt;
	private EditText eventNameTxt;
	private Spinner sportsSpnr;
	private Spinner eventTypeSpnr;
	private EditText eventName;
	private Button startDate;
	private Button endDate;
	private Button startTimeBtn;
	private Button finishTimeBtn;
	private AutoCompleteTextView location;
	private EditText subLocation;
	private EditText eventDesc;
	private EditText selectTeam1;
	private EditText selectTeam2;
	private Button coverPhotoBtn;
	private Button saveBtn;
	private FlowLayout flowLayout;
	// private RadioGroup teamType1RG;
	// private RadioGroup teamType2RG;
	// objects
	private List<String> listEventTypes;
	private List<String> listStartTimings;
	private List<String> listEndTimings;
	private String selectedImagePath = "";
	private String eventType;
	private String startTime;
	private String endTime;
	private String sportId;
	private String team1Type;
	private String team2Type;
	private String invitedUsers = "";
	private String imgPath;
	private boolean isFileImageUploaded;
	// for custom calendar views,objects and constants

	private static final String tag = "MyCalendarFragments";

	private TextView currentMonth;
	private TextView teamtype1Hometv;
	private TextView teamtype1Awaytv;
	private TextView teamtype2Hometv;
	private TextView teamtype2Awaytv;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	private int month, year;
	private String monthStr, yearStr, dayStr, datemonthYear;
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";
	private Dialog mDialog;

	private ImageView team1home;
	private ImageView team1away;
	private ImageView team2home;
	private ImageView team2away;

	private LinearLayout team1homellyt;
	private LinearLayout team1awayllyt;
	private LinearLayout team2homellyt;
	private LinearLayout team2awayllyt;

	// private PlaceAutocompleteAdapter mAdapter;
	private static final String LOG_TAG = "Google Places Autocomplete";
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String TYPE_DETAILS = "/details";
	private static final String OUT_JSON = "/json";
	private static final String API_KEY = "AIzaSyAZ-Z1DQ9LlqdlzHgyJpiqlLLRqb8I72_8";
	private GeoData selectLoc; // Holds the selected user
	private String base64Image = "";
	private Uri selectedImagePathUri;
	private Bitmap photo;

	private int starthour;
	private int startminute;
	private int endhour;
	private int endminute;
	private Calendar mStartCalendar;
	private Calendar mEndCalendar;

	private List<SearchInfo> selectedInvitesUsersList = new ArrayList<SearchInfo>();
	private Activity activity;
	private RelativeLayout events_header_llyt;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_addanevent, container,
				false);
		init(view);

		/********* display current time on screen Start ********/

		mStartCalendar = Calendar.getInstance();
		mEndCalendar = Calendar.getInstance();
		// Current Hour
		starthour = mStartCalendar.get(Calendar.HOUR_OF_DAY);
		endhour = mEndCalendar.get(Calendar.HOUR_OF_DAY);
		// Current Minute
		startminute = mStartCalendar.get(Calendar.MINUTE);
		endminute = mStartCalendar.get(Calendar.MINUTE);

		// set current time into output textview
		setTime(starthour, startminute, "");

		/********* display current time on screen End ********/

		Log.v("", "lati and longi:" + Constants.lati + " " + Constants.longi);

		team1homellyt.setOnClickListener(this);
		team1awayllyt.setOnClickListener(this);
		team2homellyt.setOnClickListener(this);
		team2awayllyt.setOnClickListener(this);

		if (Utils.chkStatus()) {
			serviceToGetSportsList();
		} else {
			Utils.networkAlertDialog(activity,
					getResources().getString(R.string.toast_no_network));
		}

		listEventTypes = new ArrayList<String>();
		listStartTimings = new ArrayList<String>();
		listEndTimings = new ArrayList<String>();
		listStartTimings = Arrays.asList(getResources().getStringArray(
				R.array.time_array));
		listEndTimings = Arrays.asList(getResources().getStringArray(
				R.array.time_array));
		listEventTypes = Arrays.asList(getResources().getStringArray(
				R.array.eventtypes_array));

		CommonAdapter eventTypeadapter = new CommonAdapter(activity,
				listEventTypes);
		/*
		 * ArrayAdapter<CharSequence> eventTypeadapter =
		 * ArrayAdapter.createFromResource(activity, R.array.eventtypes_array,
		 * android.R.layout.simple_spinner_item);
		 */
		// eventTypeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventTypeSpnr.setAdapter(eventTypeadapter);

		CommonAdapter startTimingsAdapter = new CommonAdapter(activity,
				listStartTimings);
		/*
		 * ArrayAdapter<CharSequence> startTimingsAdapter =
		 * ArrayAdapter.createFromResource(activity, R.array.start_time_array,
		 * android.R.layout.simple_spinner_item); startTimingsAdapter
		 * .setDropDownViewResource
		 * (android.R.layout.simple_spinner_dropdown_item );
		 */
		// startTimeSpnr.setAdapter(startTimingsAdapter);

		CommonAdapter endTimingsAdapter = new CommonAdapter(activity,
				listEndTimings);
		/*
		 * ArrayAdapter<CharSequence> endTimingsAdapter =
		 * ArrayAdapter.createFromResource(activity, R.array.end_time_array,
		 * android.R.layout.simple_spinner_item); endTimingsAdapter
		 * .setDropDownViewResource
		 * (android.R.layout.simple_spinner_dropdown_item );
		 */
		// finishTimeSpnr.setAdapter(endTimingsAdapter);

		eventTypeSpnr
		.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent,
					View view, int pos, long id) {
				eventType = listEventTypes.get(pos);
				if (eventType.equalsIgnoreCase("Game")) {
					selectTeamLlyt.setVisibility(View.VISIBLE);
					team1Type = "Home";
					team2Type = "Home";
					team1home
					.setImageResource(R.drawable.edit_profile_radio_selected);
					team2home
					.setImageResource(R.drawable.edit_profile_radio_selected);
				} else {
					team1Type = "";
					team2Type = "";
					selectTeamLlyt.setVisibility(View.GONE);
				}

			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		/*
		 * startTimeSpnr .setOnItemSelectedListener(new
		 * AdapterView.OnItemSelectedListener() { public void
		 * onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		 * String typeId = String.valueOf(startTimeSpnr
		 * .getSelectedItemPosition() + 1); startTime =
		 * listStartTimings.get(pos); //
		 * UIHelperUtil.showToastMessage(""+startTime);
		 * 
		 * }
		 * 
		 * public void onNothingSelected(AdapterView<?> parent) { } });
		 */

		/*
		 * finishTimeSpnr .setOnItemSelectedListener(new
		 * AdapterView.OnItemSelectedListener() { public void
		 * onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		 * String typeId = String.valueOf(finishTimeSpnr
		 * .getSelectedItemPosition() + 1); endTime = listEndTimings.get(pos);
		 * // UIHelperUtil.showToastMessage(""+endTime);
		 * 
		 * }
		 * 
		 * public void onNothingSelected(AdapterView<?> parent) { } });
		 */
		startDate.setOnClickListener(this);
		endDate.setOnClickListener(this);
		coverPhotoBtn.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		return view;
	}

	// service to get sports list
	private void serviceToGetSportsList() {
		String url = Constants.common_url
				+ getResources().getString(R.string.get_sports_list);

		JsonParser.callBackgroundService(url, null, new JsonServiceListener() {
			ProgressDialog pd;

			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(activity);
				pd.setMessage("Loading...");
				pd.show();
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
			}

			@Override
			public void hideProgress() {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
			}

			@Override
			public void parseJsonResponse(String jsonResponse) {
				setJsonValues(jsonResponse);
			}
		});
	}

	private void setJsonValues(String jsonResult) {
		try {
			if (jsonResult != null) {
				System.out.println("response:  " + jsonResult);
				JSONObject reader = new JSONObject(jsonResult);
				JSONObject sys = reader.getJSONObject("Response");
				String response = sys.getString("ResponseInfo");
				final List<SportModel> sportsList = new ArrayList<SportModel>();
				if (response != null && response.equals("SUCCESS")) {
					JSONArray sportsDetailsJson = sys
							.getJSONArray("sprots_list");

					// for default item for spinner
					SportModel sprtModel = new SportModel();
					sprtModel.setSportName("Select Sport");
					sportsList.add(sprtModel);
					//

					for (int i = 0; i < sportsDetailsJson.length(); i++) {
						JSONObject sportObject = sportsDetailsJson
								.getJSONObject(i);
						SportModel sportModel = new SportModel();
						sportModel
						.setSportId(sportObject.getString("sport_id"));
						sportModel.setSportName(sportObject
								.getString("sport_name"));
						sportModel.setMaxPlayersOnField(sportObject
								.getString("max_players_on_field"));
						sportModel.setSportRounds(sportObject
								.getString("rounds"));
						sportsList.add(sportModel);
						System.out.println("list: " + sportsList.get(i));
					}

					if (sportsList != null) {
						SportsAdapter sportsAdapter = new SportsAdapter(
								activity, sportsList);
						sportsSpnr.setAdapter(sportsAdapter);
						sportsSpnr
						.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							public void onItemSelected(
									AdapterView<?> parent, View view,
									int pos, long id) {
								sportId = sportsList.get(pos)
										.getSportId();
							}

							public void onNothingSelected(
									AdapterView<?> parent) {
							}
						});
					}
				} else if (response != null
						&& response.equalsIgnoreCase("FAILURE")) {
					UIHelperUtil.showToastMessage("No sports list found");
				}
			} else {
				UIHelperUtil.showToastMessage("No sports list found");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// initialize variables
	private void init(View fragmentView) {
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		backBtn = (ImageView) activity.findViewById(R.id.back_iv);
		settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);
		searchHeaderBtn = (ImageView) activity.findViewById(R.id.search_iv);
		addEventBtn = (ImageView) activity.findViewById(R.id.add_an_event_iv);

		tvEventType = (TextView) activity
				.findViewById(R.id.title_bar_tv_event_type);
		events_header_llyt = (RelativeLayout) activity
				.findViewById(R.id.events_header_llyt);
		search_header_llyt = (RelativeLayout) activity
				.findViewById(R.id.search_header_llyt);

		eventNameTxt = (EditText) fragmentView
				.findViewById(R.id.addevent_name_txt);
		// edtInviteUsers = (EditText)
		// fragmentView.findViewById(R.id.flow_layout_edit_text);
		flowLayout = (FlowLayout) fragmentView
				.findViewById(R.id.invite_users_add_event_flowlayout);
		flowLayout.setOnClickListener(this);
		selectTeamLlyt = (LinearLayout) fragmentView
				.findViewById(R.id.select_team_llyt);
		coverPhotoImg = (ImageView) fragmentView
				.findViewById(R.id.cover_photo_iv);
		sportsSpnr = (Spinner) fragmentView.findViewById(R.id.sports_spnr);
		eventTypeSpnr = (Spinner) fragmentView
				.findViewById(R.id.event_type_spnr);
		eventName = (EditText) fragmentView
				.findViewById(R.id.addevent_name_txt);
		startDate = (Button) fragmentView
				.findViewById(R.id.addevent_start_date_button);
		endDate = (Button) fragmentView
				.findViewById(R.id.addevent_end_date_button);
		startTimeBtn = (Button) fragmentView
				.findViewById(R.id.addevent_st_time_btn);
		startTimeBtn.setOnClickListener(this);
		finishTimeBtn = (Button) fragmentView
				.findViewById(R.id.addevent_end_time_btn);
		finishTimeBtn.setOnClickListener(this);
		location = (AutoCompleteTextView) fragmentView
				.findViewById(R.id.addanevent_loc_et);
		subLocation = (EditText) fragmentView
				.findViewById(R.id.addanevent_subloc_et);
		eventDesc = (EditText) fragmentView.findViewById(R.id.description_et);
		coverPhotoBtn = (Button) fragmentView
				.findViewById(R.id.cover_photo_btn);
		saveBtn = (Button) fragmentView.findViewById(R.id.save_btn);
		selectTeam1 = (EditText) fragmentView
				.findViewById(R.id.select_team1_et);
		selectTeam2 = (EditText) fragmentView
				.findViewById(R.id.select_team2_et);
		// teamType1RG =
		// (RadioGroup)fragmentView.findViewById(R.id.team_type1_rg);
		// teamType2RG =
		// (RadioGroup)fragmentView.findViewById(R.id.team_type2_rg);

		team1home = (ImageView) fragmentView
				.findViewById(R.id.team_type1_home_iv);
		team1away = (ImageView) fragmentView
				.findViewById(R.id.team_type1_away_iv);
		team2home = (ImageView) fragmentView
				.findViewById(R.id.team_type2_home_iv);
		team2away = (ImageView) fragmentView
				.findViewById(R.id.team_type2_away_iv);

		team1homellyt = (LinearLayout) fragmentView
				.findViewById(R.id.select_team1_home_llyt);
		team1awayllyt = (LinearLayout) fragmentView
				.findViewById(R.id.select_team1_away_llyt);
		team2homellyt = (LinearLayout) fragmentView
				.findViewById(R.id.select_team2_home_llyt);
		team2awayllyt = (LinearLayout) fragmentView
				.findViewById(R.id.select_team2_away_llyt);

		teamtype1Hometv = (TextView) fragmentView
				.findViewById(R.id.team_type1_home_tv);
		teamtype1Awaytv = (TextView) fragmentView
				.findViewById(R.id.team_type1_away_tv);
		teamtype2Hometv = (TextView) fragmentView
				.findViewById(R.id.team_type2_home_tv);
		teamtype2Awaytv = (TextView) fragmentView
				.findViewById(R.id.team_type2_away_tv);

		location.setAdapter(new GooglePlacesAutocompleteAdapter(activity,
				R.layout.list_place_items));
		location.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (view.getTag() != null) {
					GeoData tmpData = (GeoData) view.getTag();
					selectLoc = tmpData;
					location.setText(tmpData.getLocation());
				} else {
					location.setText("");
				}
			}
		});

		// (GoogleAutoCompleteLocation.mAdapter);
		backBtn.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		title.setText("ADD AN EVENT");
		settingBtn.setVisibility(View.GONE);
		backBtn.setVisibility(View.VISIBLE);
		tvEventType.setVisibility(View.GONE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		events_header_llyt.setVisibility(View.VISIBLE);
		search_header_llyt.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {

		int startYear = 0;
		int startMonth = 0;
		int startDay = 0;
		startYear = mStartCalendar.get(Calendar.YEAR);
		startMonth = mStartCalendar.get(Calendar.MONTH);
		startDay = mStartCalendar.get(Calendar.DAY_OF_MONTH);

		int endYear = 0;
		int endMonth = 0;
		int endDay = 0;
		endYear = mEndCalendar.get(Calendar.YEAR);
		endMonth = mEndCalendar.get(Calendar.MONTH);
		endDay = mEndCalendar.get(Calendar.DAY_OF_MONTH);

		switch (v.getId()) {
		case R.id.addevent_start_date_button:
			new DatePickerDialog(activity, startdatePickerListener, startYear,
					startMonth, startDay).show();
			// displayCalendar();
			break;
		case R.id.addevent_end_date_button:
			DatePickerDialog mDatePickerDialog = new DatePickerDialog(activity,
					enddatePickerListener, endYear, endMonth, endDay);
			DatePicker mDatePicker = mDatePickerDialog.getDatePicker();
			mDatePicker.setMinDate(mStartCalendar.getTimeInMillis());
			mDatePickerDialog.show();
			// displayCalendar();
			break;
		case R.id.cover_photo_btn:
			selectImage();
			break;
		case R.id.save_btn:
			saveFunction();
			Log.d("", "team type:" + team1Type + "  " + team2Type);
			break;
		case R.id.select_team1_home_llyt:
			team1home.setImageResource(R.drawable.edit_profile_radio_selected);
			team1away.setImageResource(R.drawable.edit_profile_radiobutton);
			team1Type = "Home";
			Log.d("", "team type:" + team1Type);
			break;
		case R.id.select_team1_away_llyt:
			team1Type = "Away";
			team1home.setImageResource(R.drawable.edit_profile_radiobutton);
			team1away.setImageResource(R.drawable.edit_profile_radio_selected);
			break;
		case R.id.select_team2_home_llyt:
			team2Type = "Home";
			team2home.setImageResource(R.drawable.edit_profile_radio_selected);
			team2away.setImageResource(R.drawable.edit_profile_radiobutton);
			break;
		case R.id.select_team2_away_llyt:
			team2Type = "Away";
			team2home.setImageResource(R.drawable.edit_profile_radiobutton);
			team2away.setImageResource(R.drawable.edit_profile_radio_selected);
			break;
		case R.id.back_iv:
			if(activity instanceof CalendarEventsActivity){
				activity.finish();
			}else if(activity instanceof MainActivity){
				((MainActivity) activity).onBackPressed();
			}

			break;
		case R.id.addevent_st_time_btn:
			new TimePickerDialog(activity, starttimePickerListener, starthour,
					startminute, false).show();
			break;
		case R.id.addevent_end_time_btn:
			new TimePickerDialog(activity, endtimePickerListener, endhour,
					endminute, false).show();
			break;
		case R.id.invite_users_add_event_flowlayout:
			CustomLog.v("invite_users", "invite clicked");
			Intent intent = new Intent(activity, InvitePeopleActivity.class);
			if (!TextUtils.isEmpty(invitedUsers)) {
				intent.putParcelableArrayListExtra(
						"selected_users",
						(ArrayList<? extends Parcelable>) selectedInvitesUsersList);
			}
			startActivityForResult(intent, 2);

			break;
		default:
			break;
		}
	}

	private TimePickerDialog.OnTimeSetListener starttimePickerListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
			// TODO Auto-generated method stub
			starthour = hourOfDay;
			startminute = minutes;
			mStartCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			mStartCalendar.set(Calendar.MINUTE, minutes);
			setTime(starthour, startminute, "st");
			setTime(starthour, startminute, "ed");

		}

	};

	private DatePickerDialog.OnDateSetListener startdatePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			mStartCalendar.set(Calendar.YEAR, selectedYear);
			mStartCalendar.set(Calendar.MONTH, selectedMonth);
			mStartCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String weekday = df.format(mStartCalendar.getTime());
			startDate.setText(weekday);
			endDate.setText(weekday);
			mEndCalendar = (Calendar) mStartCalendar.clone();

		}
	};

	private DatePickerDialog.OnDateSetListener enddatePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			Log.d("",
					"date:" + mStartCalendar.getTime() + " "
							+ mEndCalendar.getTime());
			mEndCalendar.set(Calendar.YEAR, selectedYear);
			mEndCalendar.set(Calendar.MONTH, selectedMonth);
			mEndCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String weekday = df.format(mEndCalendar.getTime());

			Date startDate = mStartCalendar.getTime();
			Date endDated = mEndCalendar.getTime();
			Log.d("",
					"date:" + mStartCalendar.getTime() + " "
							+ mEndCalendar.getTime());

			if (endDated.getTime() >= startDate.getTime()) {
				endDate.setText(weekday);
			} else {
				Utils.showAlertDialog(activity, "Validation",
						"Select correct date and time");
			}
		}
	};

	private TimePickerDialog.OnTimeSetListener endtimePickerListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {

			endhour = hourOfDay;
			endminute = minutes;
			mEndCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			mEndCalendar.set(Calendar.MINUTE, minutes);

			Date startDate = mStartCalendar.getTime();
			Date endDate = mEndCalendar.getTime();

			if (endDate.getTime() >= startDate.getTime()) {
				setTime(endhour, endminute, "ed");
			} else {
				Utils.showAlertDialog(activity, "Validation",
						"Select correct date and time");
			}

		}

	};

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
			UIHelperUtil.showToastMessage("SSL exception");
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
				resultList.add(tmpData);
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		}

		return resultList;
	}

	class GooglePlacesAutocompleteAdapter extends ArrayAdapter<GeoData>
	implements Filterable {
		private ArrayList<GeoData> resultList;
		Context cntxt;

		public GooglePlacesAutocompleteAdapter(Context context,
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
	}

	// method for save function
	private void saveFunction() {
		if (String.valueOf(sportsSpnr.getSelectedItemPosition()) != null
				&& !String.valueOf(sportsSpnr.getSelectedItemPosition())
				.matches("0")) {
			if (eventType.equalsIgnoreCase("Game")) {
				if (!selectTeam1.getText().toString().matches("")) {
					if (!selectTeam2.getText().toString().matches("")) {
						restofValidations();
					} else {
						// UIHelperUtil.showToastMessage("Please enter select team2");
						Utils.showAlertDialog(activity, "Validation",
								"Please enter select team2");
						selectTeam2.requestFocus();
					}

				} else {
					// UIHelperUtil.showToastMessage("Please enter select team1");
					Utils.showAlertDialog(activity, "Validation",
							"Please enter select team1");
					selectTeam1.requestFocus();
				}

			} else {
				restofValidations();
			}

		} else {
			// UIHelperUtil.showToastMessage("Please select sports");
			Utils.showAlertDialog(activity, "Validation",
					"Please select sports");
		}
	}

	// method for validations
	private void restofValidations() {

		if (eventNameTxt.getText().toString().trim().length() != 0) {
			if (startDate.getText().toString().trim().length() != 0) {
				if (endDate.getText().toString().trim().length() != 0) {
					if (startTimeBtn.getText().toString().trim().length() != 0) {
						if (finishTimeBtn.getText().toString().trim().length() != 0) {

							Date startDate = mStartCalendar.getTime();
							Date endDate = mEndCalendar.getTime();

							if (endDate.getTime() >= startDate.getTime()) { // endDate.compareTo(startDate)
								// >
								// 0

								if (location.getText().toString().trim()
										.length() != 0
										&& selectLoc != null) {
									// Mentioned: sub-location is not mandatory
									// if
									// (subLocation.getText().toString().trim()
									// .length() != 0) {
									if (!TextUtils.isEmpty(eventDesc.getText()
											.toString())) {
										serviceForAddanEvent();
										/*
										 * Image uploading is not compulsory if
										 * (!TextUtils.isEmpty(base64Image)) {
										 * serviceForAddanEvent(); // new
										 * AddAnEventAsync().execute(); } else {
										 * Utils.showAlertDialog( activity,
										 * "Validation",
										 * "Please add event cover photo"); }
										 */

									} else {
										// UIHelperUtil.showToastMessage("Please enter description");
										Utils.showAlertDialog(activity,
												"Validation",
												"Please enter description");
										eventDesc.requestFocus();
									}
									// } else {
									// //
									// UIHelperUtil.showToastMessage("Please enter sub location");
									// Utils.showAlertDialog(activity,
									// "Validation",
									// "Please enter sub location");
									// subLocation.requestFocus();
									// }
								} else {
									// UIHelperUtil.showToastMessage("Please enter location");
									Utils.showAlertDialog(activity,
											"Validation",
											"Please choose location from drop down list");
									location.requestFocus();
								}

							} else {
								Utils.showAlertDialog(activity, "Validation",
										"Select correct date and time");
							}

						} else {
							// UIHelperUtil.showToastMessage("Please choose end time");
							Utils.showAlertDialog(activity, "Validation",
									"Please choose end time");
						}
					} else {
						// UIHelperUtil.showToastMessage("Please choose start time");
						Utils.showAlertDialog(activity, "Validation",
								"Please choose start time");
					}
				} else {
					Utils.showAlertDialog(activity, "Validation",
							"Please select date");
				}

			} else {
				// UIHelperUtil.showToastMessage("Please select date");
				Utils.showAlertDialog(activity, "Validation",
						"Please select date");
			}
		} else {
			// UIHelperUtil.showToastMessage("Please enter event name");
			Utils.showAlertDialog(activity, "Validation",
					"Please enter event name");
			eventNameTxt.requestFocus();
		}

	}

	// generates event start time in UTC(Coordinated Universal Time) format
	private String convertToUTC_EventStartTime() {
		try {
			// Mobile specific time
			TimeZone defaultTZ = TimeZone.getDefault();
			SimpleDateFormat srcFormat = new SimpleDateFormat(
					"MM/dd/yyyy h:m a");
			srcFormat.setTimeZone(defaultTZ);
			Date parsed = srcFormat.parse(startDate.getText().toString().trim()
					+ " " + startTimeBtn.getText().toString().trim());

			// UTC time format
			TimeZone UTC_TZ = TimeZone.getTimeZone("UTC");
			SimpleDateFormat destFormat = new SimpleDateFormat(
					"MM/dd/yyyy hh:mma");
			destFormat.setTimeZone(UTC_TZ);
			String utcStartDate = destFormat.format(parsed);
			Log.d(Constants.logUrl, startDate.getText().toString().trim() + " "
					+ startTimeBtn.getText().toString().trim());
			Log.d(Constants.logUrl, "UTC start: " + utcStartDate);
			return utcStartDate;
		} catch (ParseException e) {
			e.printStackTrace();
			Log.d(Constants.logUrl, e.getMessage());
			return null;
		}
	}

	// generates event end time in UTC(Coordinated Universal Time) format
	private String convertToUTC_EventEndTime() {
		try {
			// Mobile specific time
			TimeZone defaultTZ = TimeZone.getDefault();
			SimpleDateFormat srcFormat = new SimpleDateFormat(
					"MM/dd/yyyy h:m a");
			srcFormat.setTimeZone(defaultTZ);
			Date parsed = srcFormat.parse(endDate.getText().toString().trim()
					+ " " + finishTimeBtn.getText().toString().trim());

			// UTC time format
			TimeZone UTC_TZ = TimeZone.getTimeZone("UTC");
			SimpleDateFormat destFormat = new SimpleDateFormat(
					"MM/dd/yyyy hh:mma");
			destFormat.setTimeZone(UTC_TZ);
			String utcStartDate = destFormat.format(parsed);
			Log.d(Constants.logUrl, utcStartDate);
			return utcStartDate;
		} catch (ParseException e) {
			e.printStackTrace();
			Log.d(Constants.logUrl, e.getMessage());
			return null;
		}
	}

	// display calender dialog
	public void displayCalendar() {

		mDialog = new Dialog(activity);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View content = activity.getLayoutInflater().inflate(
				R.layout.my_calendar_view, null);
		mDialog.setContentView(content);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		mDialog.setCancelable(false);

		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
				+ year);

		/*
		 * selectedDayMonthYearButton = (Button) mDialog
		 * .findViewById(R.id.selectedDayMonthYear);
		 * selectedDayMonthYearButton.setText("Selected: ");
		 */
		ImageView addEvent = (ImageView) mDialog
				.findViewById(R.id.add_an_event_frm_cal_iv);
		addEvent.setVisibility(View.GONE);

		Button cancel = (Button) mDialog.findViewById(R.id.calendar_cancel_btn);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		prevMonth = (ImageView) mDialog.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (month <= 1) {
					month = 12;
					year--;
				} else {
					month--;
				}
				Log.d(tag, "Setting Prev Month in GridCellAdapter: "
						+ "Month: " + month + " Year: " + year);
				setGridCellAdapterToDate(month, year);

			}
		});

		currentMonth = (TextView) mDialog.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));

		nextMonth = (ImageView) mDialog.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (month > 11) {
					month = 1;
					year++;
				} else {
					month++;
				}
				Log.d(tag, "Setting Next Month in GridCellAdapter: "
						+ "Month: " + month + " Year: " + year);
				setGridCellAdapterToDate(month, year);

			}
		});

		calendarView = (GridView) mDialog.findViewById(R.id.calendar);

		// Initialised
		adapter = new GridCellAdapter(activity, R.id.calendar_day_gridcell,
				month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
		mDialog.show();

		// Change month on left & right swipes
		calendarView.setOnTouchListener(new OnTouchListener() {
			float x1, x2;
			float y1, y2;
			boolean actionDown = false;

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent touchevent) {
				switch (touchevent.getAction()) {
				// when user first touches the screen we get x and y coordinate
				case MotionEvent.ACTION_MOVE: {
					if (!actionDown) {
						x1 = touchevent.getX();
						y1 = touchevent.getY();
						actionDown = true;
					}
					break;
				}
				case MotionEvent.ACTION_DOWN:
					x1 = touchevent.getX();
					y1 = touchevent.getY();
					actionDown = true;
					break;
				case MotionEvent.ACTION_UP: {
					actionDown = false;
					x2 = touchevent.getX();
					y2 = touchevent.getY();

					// if left to right sweep event on screen
					if (x1 < x2) {
						// Left to Right Swap Performed: Show previous month
						if (month <= 1) {
							month = 12;
							year--;
						} else {
							month--;
						}
						Log.d(tag, "Setting Prev Month in GridCellAdapter: "
								+ "Month: " + month + " Year: " + year);
						setGridCellAdapterToDate(month, year);
					}

					// if right to left sweep event on screen
					if (x1 > x2) {
						// Right to Left Swap Performed: Display next month
						if (month > 11) {
							month = 1;
							year++;
						} else {
							month++;
						}
						Log.d(tag, "Setting Next Month in GridCellAdapter: "
								+ "Month: " + month + " Year: " + year);
						setGridCellAdapterToDate(month, year);
					}

					// if UP to Down sweep event on screen
					if (y1 < y2) {
						// Toast.makeText(this, "UP to Down Swap Performed",
						// Toast.LENGTH_LONG).show();
					}

					// if Down to UP sweep event on screen
					if (y1 > y2) {
						// Toast.makeText(this, "Down to UP Swap Performed",
						// Toast.LENGTH_LONG).show();
					}
					break;
				}
				}
				return false;
			}
		});

	}

	/**
	 * 
	 * @param month
	 * @param year
	 */
	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(activity, R.id.calendar_day_gridcell,
				month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private int currentMonth;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap<String, Integer> eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd-MMM-yyyy");

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId,
				int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
					+ "Year: " + year);
			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			setCurrentMonth(calendar.get(Calendar.MONTH));
			Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

			// Print Month
			printMonth(month, year);

			// Find Number of Events
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		public int getCurrentMonth() {
			return currentMonth;
		}

		public void setCurrentMonth(int currentMonth) {
			this.currentMonth = currentMonth;
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 * 
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
					+ daysInMonth + " days.");

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			Log.d(tag, "Week Day:" + currentWeekDay + " is "
					+ getWeekDayAsString(currentWeekDay));
			Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

			if (cal.isLeapYear(cal.get(Calendar.YEAR)))
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag,
						"PREV MONTH:= "
								+ prevMonth
								+ " => "
								+ getMonthAsString(prevMonth)
								+ " "
								+ String.valueOf((daysInPrevMonth
										- trailingSpaces + DAY_OFFSET)
										+ i));
				list.add(String
						.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
								+ i)
								+ "-GREY"
								+ "-"
								+ getMonthAsString(prevMonth)
								+ "-"
								+ prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " "
						+ getMonthAsString(currentMonth) + " " + yy);
				if ((i == getCurrentDayOfMonth())
						&& (currentMonth == getCurrentMonth())) {
					list.add(String.valueOf(i) + "-BLUE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-WHITE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-"
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 * 
		 * @param year
		 * @param month
		 * @return
		 */
		private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
				int month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setOnClickListener(this);

			// ACCOUNT FOR SPACING

			// Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			String[] day_color = list.get(position).split("-");
			Log.d(tag, "Current Day: " + list.get(position));
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(theday)) {
					num_events_per_day = (TextView) row
							.findViewById(R.id.num_events_per_day);
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
				}
			}

			// Set the Day GridCell
			gridcell.setText(theday);
			gridcell.setTag(theday + "-" + getMonthAsNum(themonth) + "-"
					+ theyear);
			Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
					+ theyear);

			if (day_color[1].equals("GREY")) {
				gridcell.setTextColor(getResources()
						.getColor(R.color.lightgray));
			}
			if (day_color[1].equals("WHITE")) {
				gridcell.setTextColor(getResources().getColor(
						R.color.lightgray02));
			}
			if (day_color[1].equals("BLUE")) {
				gridcell.setTextColor(getResources().getColor(R.color.orrange));
			}
			return row;
		}

		@Override
		public void onClick(View view) {
			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}

			String date_month_year = (String) view.getTag();
			datemonthYear = date_month_year;
			String[] monthYear = date_month_year.split("-");
			dayStr = monthYear[0];
			monthStr = monthYear[1];
			yearStr = monthYear[2];
			startDate.setText(monthStr + "/" + dayStr + "/" + yearStr);
			Log.d("Selected date", monthStr + "/" + dayStr + "/" + yearStr);
			endDate.setText(monthStr + "/" + dayStr + "/" + yearStr);
			Log.d("Selected date", monthStr + "/" + dayStr + "/" + yearStr);

		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}

		public int getMonthAsNum(String monthName) {
			int monthIndex = 0;
			for (int i = 0; i < months.length; i++) {
				if (monthName.equalsIgnoreCase(months[i])) {
					monthIndex = i + 1;
					break;
				}
			}
			return monthIndex;
		}
	}

	/*
	 * Loading sports information from server
	 */
	private SportModel[] getSportsInfoFromServer() {
		// get_sports_list
		return null;
	}

	/**
	 * method for calling add an event service
	 */

	private void serviceForAddanEvent() {
		
		final String url = Constants.common_url
				+ getString(R.string.add_an_event);
		String[] params = { url, imgPath };


		new AsyncTask<String, Void, String>() {
			ProgressDialog pd;

			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(activity);
				pd.show();
				pd.setMessage("Please wait. Adding new event...");
				//				pd.setMessage("Please wait.");
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
			}

			@Override
			protected String doInBackground(String... params) {
				return uploadFile(params[1], params[0]);
			}

			@Override
			protected void onPostExecute(String jsonResponse) {


				System.out.println("Add Event RESPONSE: "
						+ jsonResponse);
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
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

							UIHelperUtil
							.showToastMessage("Event successfully posted....");

							isFileImageUploaded = false;
							
							if(activity instanceof MainActivity){
								InputMethodManager input = (InputMethodManager) activity
										.getSystemService(Activity.INPUT_METHOD_SERVICE);
								input.hideSoftInputFromWindow(
										activity.getCurrentFocus()
										.getWindowToken(), 0);

								// New event created successfully.
								((MainActivity) activity)
								.setNewlyCreatedEvent();

								// Return to event-main screen
								((MainActivity) activity).onBackPressed();
							}else if(activity instanceof CalendarEventsActivity){
								activity.finish();
							}


						} else if (responseStr != null
								&& responseStr
								.equalsIgnoreCase("invalid input")) {
							Log.d("", "Add Event invalid");
						} else if (responseStr != null
								&& responseStr
								.equalsIgnoreCase("FAILURE")) {
							Log.d("", "Add Event failed");
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

	
	private String uploadFile(String sourceUri, String ServerUrl) {

		String responseStr = "";
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		String eventLat = Constants.lati;
		String eventLng = Constants.longi;
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE
					+ TYPE_DETAILS + OUT_JSON);
			sb.append("?key=" + API_KEY);
			sb.append("&placeid=" + selectLoc.getPlaceId());

			URL googlePlaceAPIUrl = new URL(sb.toString());
			conn = (HttpURLConnection) googlePlaceAPIUrl
					.openConnection();
			InputStreamReader in = new InputStreamReader(
					conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}

			try {
				// Create a JSON object hierarchy from the results
				JSONObject jsonObj = new JSONObject(
						jsonResults.toString());
				JSONObject jsonResult = jsonObj.getJSONObject("result");
				JSONObject jsonGeometryList = jsonResult
						.getJSONObject("geometry");
				JSONObject jsonLocList = jsonGeometryList
						.getJSONObject("location");
				eventLat = jsonLocList.getString("lat");
				eventLng = jsonLocList.getString("lng");

			} catch (JSONException e) {
				Log.e(LOG_TAG, "Cannot process JSON results", e);
			}


			long totalSize = 0;

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(ServerUrl);

				MultipartEntity entity = new MultipartEntity();

				entity.addPart("user_id",
						new StringBody(SharedPreferencesUtils.getUserId()));
				entity.addPart("sports_id", new StringBody(sportId));
				entity.addPart("event_name", new StringBody(eventNameTxt.getText().toString()));
				entity.addPart("sports_id", new StringBody(sportId));
				entity.addPart("team1_id", new StringBody("1"));
				entity.addPart("team2_id", new StringBody("1"));
				entity.addPart("team1_type", new StringBody("Home"));
				entity.addPart("team2_type", new StringBody("Home"));
				entity.addPart("formatted_address", new StringBody(location.getText().toString()));
				entity.addPart("sub_location", new StringBody(subLocation.getText().toString()));

				entity.addPart("description", new StringBody(eventDesc.getText().toString()));
				entity.addPart("lat", new StringBody(eventLat));
				entity.addPart("lang", new StringBody(eventLng));
				entity.addPart("date", new StringBody(convertToUTC_EventStartTime()));

				entity.addPart("start_time", new StringBody(convertToUTC_EventStartTime()));
				entity.addPart("end_time", new StringBody(convertToUTC_EventEndTime()));
				entity.addPart("route", new StringBody(" "));
				entity.addPart("city", new StringBody(selectLoc.getLocation()));
				entity.addPart("street_number", new StringBody(" "));
				entity.addPart("state", new StringBody(" "));
				entity.addPart("country", new StringBody(" "));
				entity.addPart("postal_code", new StringBody(" "));

				entity.addPart("event_type", new StringBody(eventType));
				entity.addPart("unique_id", new StringBody(selectLoc.getPlaceId()));

				entity.addPart("invite_user_ids", new StringBody(invitedUsers));



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

		} catch (MalformedURLException e) {
			Log.e(Constants.logUrl, "Error processing Places API URL",
					e);
		} catch (IOException e) {
			Log.e(Constants.logUrl, "Error connecting to Places API", e);
		} catch (Exception e) {
			Log.e(Constants.logUrl, "Error connecting to Places API", e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return responseStr;
	}
	
	private void addEventWebService(String url,
			List<NameValuePair> nameValuePairs) {
		JsonParser.callBackgroundService(url, nameValuePairs,
				new JsonServiceListener() {
			ProgressDialog pd;

			@Override
			public void showProgress() {
				super.showProgress();
				pd = new ProgressDialog(activity);
				pd.show();
				pd.setMessage("Please wait. Adding new event...");
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
			}

			@Override
			public void hideProgress() {
				super.hideProgress();
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
			}

			@Override
			public void parseJsonResponse(String jsonResponse) {
				System.out.println("Add Event RESPONSE: "
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

							UIHelperUtil
							.showToastMessage("Event successfully posted....");

							if(activity instanceof MainActivity){
								InputMethodManager input = (InputMethodManager) activity
										.getSystemService(Activity.INPUT_METHOD_SERVICE);
								input.hideSoftInputFromWindow(
										activity.getCurrentFocus()
										.getWindowToken(), 0);

								// New event created successfully.
								((MainActivity) activity)
								.setNewlyCreatedEvent();

								// Return to event-main screen
								((MainActivity) activity).onBackPressed();
							}else if(activity instanceof CalendarEventsActivity){
								activity.finish();
							}


							// Displaying invite screen
							/*
							 * Fragment activeFragment = new
							 * InvitePeopleFragment(); ((MainActivity)
							 * activity) .pushFragments( ((MainActivity)
							 * activity) .getmCurrentTab(),
							 * activeFragment, false, true);
							 */

						} else if (responseStr != null
								&& responseStr
								.equalsIgnoreCase("invalid input")) {
							Log.d("", "Add Event invalid");
						} else if (responseStr != null
								&& responseStr
								.equalsIgnoreCase("FAILURE")) {
							Log.d("", "Add Event failed");
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
			}
		});

	}

	// class AddAnEventAsync extends AsyncTask<Void, Integer, String> {
	//
	// ProgressDialog dialog;
	// Map<String, String> params = new HashMap<String, String>();
	//
	// public AddAnEventAsync() {
	//
	// /*nique_id=2b14ddbcb06abbb83a6a9224b25e4f24094e9497*/
	//
	// params.put("user_id", SharedPreferencesUtils.getUserId());
	// params.put("sports_id", sportId);
	// params.put("event_name", eventNameTxt.getText().toString());
	// params.put("team1_id", "1");
	// params.put("team2_id", "2");
	// params.put("team1_type", "Home");
	// params.put("team2_type", "Home");
	// params.put("formatted_address", location.getText().toString());
	// params.put("sub_location", subLocation.getText().toString());
	// params.put("description", eventDesc.getText().toString());
	// params.put("lat", Constants.lati);
	// params.put("lang", Constants.longi);
	// params.put("date", "16/05/2015");
	// params.put("start_time", startTime);
	// params.put("end_time", endTime);
	// params.put("route", "Hyd");
	// params.put("city", "Hyd");
	// params.put("street_number", "123");
	// params.put("state", "AP");
	// params.put("country", "India");
	// params.put("postal_code", "500016");
	// params.put("event_type", eventType);
	// params.put("unique_id", "2b14ddbcb06abbb83a6a9224b25e4f24094e9497");
	// params.put("event_image", "FILE");
	//
	//
	// }
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// dialog = new ProgressDialog(activity);
	// dialog.setTitle("Requesting");
	// dialog.setMessage("Wait this may take few seconds..!");
	// dialog.setCancelable(false);
	// dialog.show();
	// }
	// @Override
	// protected void onPostExecute(String result) {
	// super.onPostExecute(result);
	// Log.v("", "Responce onPostExecute: " + result);
	// if (dialog != null && dialog.isShowing()) {
	// dialog.dismiss();
	// if (result != null && !result.equalsIgnoreCase("")) {
	// ResponceAfterAddAnEvent(result);
	// } else {
	// UIHelperUtil.showToastMessage("Something went wrong at server side..!");
	// }
	// }
	// }
	//
	// @Override
	// protected String doInBackground(Void... params0) {
	// String Url = Constants.common_url +getString(R.string.add_an_event);
	// Log.v("", "ImagePath: " + selectedImagePath);
	//
	// return uploadEventPic("", Url, params);
	// }
	//
	// }

	/**
	 * responce after calling addanevent web service.
	 * 
	 * @param jsonResponse
	 */
	// public void ResponceAfterAddAnEvent(String jsonResponse) {
	// Log.v("", "json responce: " + jsonResponse);
	// JSONObject jsonObject;
	// try {
	// jsonObject = new JSONObject(jsonResponse);
	// JSONObject resObj = jsonObject.getJSONObject("Response");
	// String resStr = resObj.getString("ResponseInfo");
	// Log.v("", "Responce Str: " + resStr);
	// if (resStr!=null) {
	// if (resStr.equalsIgnoreCase("SUCCESS")) {
	//
	// } else if (resStr.equalsIgnoreCase("invalid input")) {
	// UIHelperUtil.showToastMessage("invalid inputs please check");
	// } else {
	//
	// }
	// } else {
	// }
	// } catch(Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 
	 * @param params2
	 */
	// public String uploadEventPic(String sourceUri, String uploadServer,
	// Map<String, String> params) {
	// String responce = "";
	// int serverResponceCode = 0;
	// HttpURLConnection httpConnection = null;
	// DataOutputStream dos = null;
	// String lineEnd = "\r\n";
	// String hyphens = "--";
	// String boundary = "*****";
	// int bytesRead;
	// int bytesAvailable;
	// int bufferSize;
	// byte[] buffer;
	// int maxBufferSize = 1024 * 1024;
	//
	// File sourceFile = new File(sourceUri);
	// if (!sourceFile.isFile()) {
	// return String.valueOf(0);
	// }
	//
	// File tempFile = createTempFile(sourceUri);
	// try {
	// //getting http connection
	// FileInputStream fis = new FileInputStream(tempFile);
	// URL url = new URL(uploadServer);
	// httpConnection = (HttpURLConnection)url.openConnection();
	// httpConnection.setDoInput(true);
	// httpConnection.setDoOutput(true);
	// httpConnection.setUseCaches(false);
	// httpConnection.setRequestMethod("POST");
	// httpConnection.setRequestProperty("connection", "Keep-Alive");
	// httpConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
	// httpConnection.setRequestProperty("Content-Type",
	// "multipart/form-data;boundary=" + boundary);
	// httpConnection.setRequestProperty("profil_pic", sourceUri);
	//
	// //uploading user profile pic
	// dos = new DataOutputStream(httpConnection.getOutputStream());
	// dos.writeBytes(hyphens + boundary + lineEnd);
	// dos.writeBytes("Content-Disposition: form-data; name=\"profile_pic\";filename=\""
	// + tempFile.getPath() +"\"" + lineEnd);
	// dos.writeBytes(lineEnd);
	// bytesAvailable = fis.available();
	// bufferSize = Math.min(bytesAvailable, maxBufferSize);
	// buffer = new byte[bufferSize];
	// bytesRead = fis.read(buffer, 0, bufferSize);
	// while(bytesRead > 0) {
	// dos.write(buffer, 0, bufferSize);
	// bytesAvailable = fis.available();
	// bufferSize = Math.min(bytesAvailable, maxBufferSize);
	// bytesRead = fis.read(buffer, 0, bufferSize);
	// }
	// dos.writeBytes(lineEnd);
	//
	// //uploading user description
	// Iterator<String> keys = params.keySet().iterator();
	// while (keys.hasNext()) {
	// String key = keys.next();
	// String value = params.get(key);
	// dos.writeBytes(hyphens + boundary + lineEnd);
	// dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" +
	// lineEnd);
	// dos.writeBytes("Content-Type: text/plain" + lineEnd);
	// dos.writeBytes(lineEnd);
	// dos.writeBytes(value);
	// dos.writeBytes(lineEnd);
	// }
	// dos.writeBytes(hyphens + boundary + hyphens + lineEnd);
	// serverResponceCode = httpConnection.getResponseCode();
	//
	// if (200 != serverResponceCode) {
	// UIHelperUtil.showToastMessage("Error at server side in signup..!");
	// } else {
	// responce = convertStreamToString(httpConnection.getInputStream());
	// Log.v("", "Responce Success : " + responce);
	// }
	//
	// Log.d("", "Responce returned: " + responce);
	// if (responce== null || responce.equalsIgnoreCase("")) {
	// responce = convertStreamToString(httpConnection.getInputStream());
	// } else {
	// fis.close();
	// dos.flush();
	// dos.close();
	// }
	// } catch(IOException e ) {
	// e.printStackTrace();
	// UIHelperUtil.showToastMessage("IO Exception");
	// } catch (Exception e) {
	// e.printStackTrace();
	// UIHelperUtil.showToastMessage("Exception");
	// }finally {
	// tempFile.delete();
	// }
	//
	// return responce;
	// }

	/**
	 * creating temp file.
	 * 
	 * @param filePath
	 * @return
	 */
	// public File createTempFile(String filePath) {
	// File tempFile = null;
	// try {
	// Log.v("", "TempFile Bitmap Before: " + filePath);
	// Bitmap bitmap = decodeSampledBitmapFromPath(filePath,
	// Constants.desiredImageWidth, Constants.desiredImageHeight);
	// ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	// tempFile = new File(activity.getExternalCacheDir() + File.separator
	// + new Date().getTime() + ".jpg");
	// tempFile.createNewFile();
	// FileOutputStream fo = new FileOutputStream(tempFile);
	// fo.write(bytes.toByteArray());
	// fo.close();
	// Log.v("", "Tempfile Bitmap After: " + tempFile.getPath());
	// return tempFile;
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// return null;
	// } catch (IOException e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	/**
	 * decoding bitmap from its path
	 * 
	 * @param path
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	// public static Bitmap decodeSampledBitmapFromPath(String path, int
	// reqWidth, int reqHeight) {
	// final BitmapFactory.Options options = new BitmapFactory.Options();
	// options.inJustDecodeBounds = true;
	// BitmapFactory.decodeFile(path, options);
	// options.inSampleSize = calculateInSampleSize(options, reqWidth,
	// reqHeight);
	// options.inJustDecodeBounds = false;
	// Bitmap bmp = BitmapFactory.decodeFile(path, options);
	// return bmp;
	// }

	/**
	 * Calculating inSample size
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	// public static int calculateInSampleSize(BitmapFactory.Options options,
	// int reqWidth, int reqHeight) {
	// final int height = options.outHeight;
	// final int width = options.outWidth;
	// int inSampleSize = 1;
	//
	// if (height > reqHeight || width > reqWidth) {
	// if (width > height) {
	// inSampleSize = Math.round((float) height / (float) reqHeight);
	// } else {
	// inSampleSize = Math.round((float) width / (float) reqWidth);
	// }
	// }
	// return inSampleSize;
	// }
	/**
	 * converting the inputstream after signup(username webservice)
	 * 
	 * @param is
	 * @return
	 */
	// private String convertStreamToString(InputStream is) {
	// try {
	// Log.v("", "input stream: " + is.available());
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	// StringBuilder sb = new StringBuilder();
	// String line = null;
	// try {
	// while ((line = reader.readLine()) != null) {
	// sb.append(line);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// is.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// Log.i("", "Responce convert to stream: " + sb.toString());
	// return sb.toString();
	// }

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
					startActivityForResult(i, Constants.REQUEST_IMAGE_GALLERY);
				} else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
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
				+ Constants.STORAGE_PATH, Constants.IMAGEPATH);
		if (!root.exists()) {
			root.mkdirs();
		}
		String filename = "" + System.currentTimeMillis();
		File file = new File(root, filename + ".png");
		imgPath = root+"/"+filename+".png";
		Uri muri = Uri.fromFile(file);
		selectedImagePath = muri.getPath();
		selectedImagePathUri = muri;
		// Log.v("Pic paht from Camera", selectedImagePath);
		return muri;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// CalenderModel model = new CalenderModel();
			if (requestCode == 2) {
				// edtInviteUsers.setVisibility(View.GONE);
				List<SearchInfo> usersList = data
						.getParcelableArrayListExtra("invited");
				CustomLog.i("Invited:", "Invited: " + usersList.size());
				if (flowLayout.getChildCount() > 0) {
					flowLayout.removeAllViews();
				}
				for (SearchInfo searchInfo : usersList) {
					addInvitedItem(searchInfo);
				}
				setSearchList(usersList);
				getInviteUserInStringFormat(usersList);
			}
			if (Constants.REQUEST_IMAGE_GALLERY == requestCode) {
				selectedImagePathUri = data.getData();
				selectedImagePath = selectedImagePathUri.toString(); // Convert
				// Uri
				// to
				// string
				Log.v("", "selected Image: " + selectedImagePathUri);
				/*
				 * try { InputStream imgInputStream =
				 * activity.getContentResolver
				 * ().openInputStream(selectedImagePathUri); byte[] byteArray =
				 * new byte[imgInputStream.available()];
				 * imgInputStream.read(byteArray); base64Image =
				 * Base64.encodeToString(byteArray,Base64.DEFAULT); } catch
				 * (Exception e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */
				performCrop();
			} else if (requestCode == Constants.REQUEST_IMAGE_CAPTURE) {
				/*
				 * Log.d("PATH when choosed from camera: ", "selected Image" +
				 * selectedImagePath); InputStream imgInputStream = null; try {
				 * File tmpFile = new File(selectedImagePath); imgInputStream =
				 * new FileInputStream(tmpFile); byte[] byteArray = new
				 * byte[imgInputStream.available()];
				 * imgInputStream.read(byteArray); base64Image =
				 * Base64.encodeToString(byteArray,Base64.DEFAULT);
				 * 
				 * } catch (Exception e) { e.printStackTrace(); }finally{ try {
				 * if(imgInputStream !=null){ imgInputStream.close(); } } catch
				 * (IOException e) { e.printStackTrace(); } }
				 */
				File root = new File(Environment.getExternalStorageDirectory()
						+ Constants.STORAGE_PATH, Constants.IMAGEPATH);
				if (!root.exists()) {
					root.mkdirs();
				}
				String filename = "" + System.currentTimeMillis();
				File file = new File(root, filename + ".png");
				adjustImageOrientation(file);
				performCrop();
			} else if (requestCode == Constants.REQUEST_IMAGE_CROP) {
				// if(data!=null){
				// //get the returned data
				// Bundle extras = data.getExtras();
				// coverPhotoImg.setVisibility(View.VISIBLE);
				// if (extras != null) {
				// if(photo!=null){
				// photo.recycle();
				// }
				// //get the cropped bitmap
				// photo = extras.getParcelable("data");
				// coverPhotoImg.setImageBitmap(photo);
				// try {
				// ByteArrayOutputStream byteArrayOutputStream = new
				// ByteArrayOutputStream();
				// photo.compress(Bitmap.CompressFormat.PNG, 1,
				// byteArrayOutputStream);
				// byte[] byteArray = byteArrayOutputStream.toByteArray();
				// base64Image = Base64.encodeToString(byteArray,
				// Base64.DEFAULT);
				// // selectedImagePath = ImgLoadSuccessStatus;
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }else{
				isFileImageUploaded = true;
				coverPhotoImg.setVisibility(View.VISIBLE);
				int index = selectedImagePath.lastIndexOf("/");
				// Converting image to base64 format
				String filename = selectedImagePath.substring(index + 1,
						selectedImagePath.length());
				String filepath = Environment.getExternalStorageDirectory()
						.getAbsolutePath()
						+ Constants.STORAGE_PATH
						+ "/"
						+ Constants.IMAGEPATH + "/";
				BitmapUtils.setImages("file:///" + filepath + filename,
						coverPhotoImg, R.drawable.camera);
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
			Log.v("", "OnActivityResult: requestcode: " + resultCode
					+ " : requestcode: " + requestCode);
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
	 
	private void performCrop() {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			isFileImageUploaded = true;
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(selectedImagePathUri, "image/*");
			cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempFile());
			cropIntent.putExtra("outputFormat",
					Bitmap.CompressFormat.PNG.toString());
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, Constants.REQUEST_IMAGE_CROP);
		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			UIHelperUtil.showToastMessage(errorMessage);
		}
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

	private static Spanned formatPlaceDetails(Resources res, CharSequence name,
			String id, CharSequence address, CharSequence phoneNumber,
			Uri websiteUri) {
		Log.e("", res.getString(R.string.place_details, name, id, address,
				phoneNumber, websiteUri));
		return Html.fromHtml(res.getString(R.string.place_details, name, id,
				address, phoneNumber, websiteUri));

	}

	// Used to convert 24hr format to 12hr format with AM/PM values
	private void setTime(int hours, int mins, String type) {

		String timeSet = "";
		if (hours > 12) {
			hours -= 12;
			timeSet = "PM";
		} else if (hours == 0) {
			hours += 12;
			timeSet = "AM";
		} else if (hours == 12)
			timeSet = "PM";
		else
			timeSet = "AM";

		String minutes = "";
		if (mins < 10)
			minutes = "0" + mins;
		else
			minutes = String.valueOf(mins);

		// Append in a StringBuilder
		String aTime = new StringBuilder().append(hours).append(':')
				.append(minutes).append(" ").append(timeSet).toString();

		if (type.matches("")) {
			startTimeBtn.setText(aTime);
			finishTimeBtn.setText(aTime);
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String weekday = df.format(mStartCalendar.getTime());
			startDate.setText(weekday);
			endDate.setText(weekday);

		} else if (type.equalsIgnoreCase("st")) {
			startTimeBtn.setText(aTime);
		} else if (type.equalsIgnoreCase("ed")) {
			finishTimeBtn.setText(aTime);
		}

	}

	private void removeSeletedItem(SearchInfo searchItem) {
		// removeItem(searchItem);
		/*
		 * if (selectedInvitesUsersList.contains(searchItem.getID())) { int
		 * index = selectedInvitesUsersList.indexOf(searchItem.getID());
		 * selectedInvitesUsersList.remove(index);
		 * flowLayout.removeView(getLastView(index+1));
		 * getInviteUserInStringFormat(selectedInvitesUsersList); }
		 */

		// removeItem(searchItem);
		if (isSearchItemExists(searchItem)) {
			try {
				int index = getIndexItem(searchItem);
				selectedInvitesUsersList.remove(index);
				flowLayout.removeView(getLastView(index));
				CustomLog.v("Index exists", "index: " + index + " : "
						+ selectedInvitesUsersList.size());
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

		}

	}

	private boolean isSearchItemExists(SearchInfo searchInfo) {
		boolean exists = false;
		for (SearchInfo info : selectedInvitesUsersList) {
			if (info.getID().equalsIgnoreCase(searchInfo.getID())) {
				exists = true;
			}
		}
		return exists;
	}

	private void addInvitedItem(final SearchInfo searchItem) {
		FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(5, 5, 5, 5);

		int color = Color.TRANSPARENT;
		TextView selectedUserName = new TextView(activity);
		selectedUserName.setBackgroundColor(color);
		// tvName.setBackgroundResource(R.drawable.input_box);
		selectedUserName.setText(searchItem.getName() + "");
		int left, right, top, bottom;
		left = right = top = bottom = 3;
		selectedUserName.setPadding(left, top, right, bottom);
		selectedUserName.setLayoutParams(layoutParams);

		ImageView imgRemove = new ImageView(activity);
		imgRemove.setImageResource(R.drawable.delete);
		imgRemove.setLayoutParams(new LayoutParams(30, 30));

		final LinearLayout selectedUserLayout = new LinearLayout(activity);
		selectedUserLayout.setBackgroundResource(R.drawable.multi_edit_corner);
		selectedUserLayout.setOrientation(LinearLayout.HORIZONTAL);
		selectedUserLayout.setGravity(Gravity.CENTER);
		selectedUserLayout.setLayoutParams(params);
		selectedUserLayout.addView(selectedUserName);
		selectedUserLayout.addView(imgRemove);

		params.rightMargin = 10;
		params.topMargin = 10;
		/*
		 * if (!isSearchItemExists(searchItem)) {
		 * flowLayout.addView(selectedUserLayout); CustomLog.i("AddItem", "Add "
		 * + searchItem.getID() + " : " + flowLayout.getChildCount() + " : " +
		 * isSearchItemExists(searchItem)); } else { CustomLog.v("AddItem",
		 * "Add " + searchItem.getID() + " : " + flowLayout.getChildCount() +
		 * " : " + isSearchItemExists(searchItem)); }
		 */
		flowLayout.addView(selectedUserLayout);
		imgRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeSeletedItem(searchItem);
			}
		});

		/*
		 * if (selectedInvitesUsersList != null &&
		 * selectedInvitesUsersList.size() > 0) { removeSeletedItem(searchItem);
		 * }
		 */
	}

	private void getInviteUserInStringFormat(
			List<SearchInfo> selectedInvitesUsersList) {
		StringBuffer selectedItems = new StringBuffer();
		for (int i = 0; i < selectedInvitesUsersList.size(); i++) {
			if (i < selectedInvitesUsersList.size() - 1) {
				selectedItems.append(selectedInvitesUsersList.get(i).getID()
						+ ",");
			} else {
				selectedItems.append(selectedInvitesUsersList.get(i).getID());
			}
		}
		invitedUsers = selectedItems.toString();
		CustomLog.v("",
				"Selected Users: " + " : " + selectedInvitesUsersList.size()
				+ " : Ids: " + invitedUsers);
	}

	private View getLastView(int position) {
		return flowLayout.getChildAt(position);
	}

	private int getIndexItem(SearchInfo searchInfo) {
		int index = 0;
		for (SearchInfo info : selectedInvitesUsersList) {
			if (info.getID().equalsIgnoreCase(searchInfo.getID())) {
				index = selectedInvitesUsersList.indexOf(info);
			}
		}
		return index;
	}

	private void setSearchList(List<SearchInfo> searchList) {
		this.selectedInvitesUsersList = searchList;
	}

	/*
	 * private boolean isInvitedListModified(SearchInfo searchList) { boolean
	 * exists = false; for (SearchInfo searchInfo : selectedInvitesUsersList) {
	 * 
	 * } }
	 */
}
