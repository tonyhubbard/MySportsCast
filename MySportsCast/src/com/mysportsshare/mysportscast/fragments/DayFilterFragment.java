package com.mysportsshare.mysportscast.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.models.GeoData;
import com.mysportsshare.mysportscast.models.SportModel;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.GooglePlacesAutoCompleteAdapter;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;

public class DayFilterFragment extends Fragment implements OnClickListener {
	AutoCompleteTextView filter_loc_actv;
	ImageView filter_current_loc_img;
	RelativeLayout filter_range_sub1;
	RelativeLayout filter_range_sub2;
	RelativeLayout filter_range_sub3;
	RelativeLayout filter_range_sub4;
	RelativeLayout filter_range_sub5;
	View filter_range_view1;
	View filter_range_view2;
	View filter_range_view3;
	View filter_range_view4;
	View filter_range_view5;
	ImageView filter_range_img1;
	ImageView filter_range_img2;
	ImageView filter_range_img3;
	ImageView filter_range_img4;
	ImageView filter_range_img5;
	Button filter_from_date_value_btn;
	Button filter_to_date_value_btn;
	CheckedTextView filter_all_date_value_ctv;
	CheckedTextView filter_all_sport_value_ctv;
	CheckedTextView filter_baseball_sport_value_ctv;
	CheckedTextView filter_basketball_sport_value_ctv;
	CheckedTextView filter_football_sport_value_ctv;
	CheckedTextView filter_soccer_sport_value_ctv;
	CheckedTextView filter_softball_sport_value_ctv;
	CheckedTextView filter_other_sport_value_ctv;
	LinearLayout filter_apply_btn; 
	LinearLayout filter_reset_btn;

	String location;
	String latitude;
	String longitude;
	String range="";
	boolean setForAllDates=false;
	Date fromDt;
	Date toDt;
	boolean setForAllSports=false;
	String sportIds;
	SimpleDateFormat dtFormat;

	/*"sport_id":"1","sport_name":"Baseball","max_players_on_field":"10","rounds":"9"},{"sport_id":"2","sport_name":"Basketball","max_players_on_field":"5","rounds":"5"},{"sport_id":"3","sport_name":"Football","max_players_on_field":"11","rounds":"5"},{"sport_id":"4","sport_name":"Soccer","max_players_on_field":"11","rounds":"5"},{"sport_id":"5","sport_name":"Softball","max_players_on_field":"11","rounds":"9"}*/
	Activity activity;
	private TextView title;
	private ImageView backBtn;
	private ImageView settingBtn;
	private ImageView searchHeaderBtn;
	private ImageView addEventBtn;
	private TextView tvEventType;
	private RelativeLayout search_header_llyt;
	private LinearLayout filter_sub_layout;
	List<SportModel> sportsList;
	CheckedTextView sportsItems[];
	private RelativeLayout events_header_llyt;
	private LinearLayout filterDateLayout;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layoutView = inflater.inflate(R.layout.filter_layout, null);
		init(layoutView);
		Bundle args = getArguments();
		if(args != null){
			if(args.getBoolean(Constants.TAG_FROM_CALENDAR_EVENTS)){
				filterDateLayout.setVisibility(View.GONE);
			}
		}
		return layoutView;
	}
	public void init(View layoutView){
		//Titile bar views
		title = (TextView) activity.findViewById(R.id.title_bar_tv);
		backBtn = (ImageView) activity.findViewById(R.id.back_iv);
		settingBtn = (ImageView) activity.findViewById(R.id.setting_iv);
		searchHeaderBtn = (ImageView) activity
				.findViewById(R.id.search_iv);
		addEventBtn = (ImageView) activity.findViewById(
				R.id.add_an_event_iv);
		tvEventType = (TextView) activity.findViewById(
				R.id.title_bar_tv_event_type);
		search_header_llyt = (RelativeLayout) activity.findViewById(
				R.id.search_header_llyt);
		events_header_llyt = (RelativeLayout) activity
				.findViewById(R.id.events_header_llyt);

		//Fragment views
		filterDateLayout = (LinearLayout) layoutView.findViewById(R.id.date_filterllyt);
		filter_loc_actv = (AutoCompleteTextView) layoutView.findViewById(R.id.filter_loc_actv);
		filter_sub_layout = (LinearLayout) layoutView.findViewById(R.id.filter_sub_layout);
		filter_current_loc_img = (ImageView) layoutView.findViewById(R.id.filter_current_loc_img);
		filter_range_sub1 = (RelativeLayout) layoutView.findViewById(R.id.filter_range_sub1);
		filter_range_sub2 = (RelativeLayout) layoutView.findViewById(R.id.filter_range_sub2);
		filter_range_sub3 = (RelativeLayout) layoutView.findViewById(R.id.filter_range_sub3);
		filter_range_sub4 = (RelativeLayout) layoutView.findViewById(R.id.filter_range_sub4);
		filter_range_sub5 = (RelativeLayout) layoutView.findViewById(R.id.filter_range_sub5);
		filter_range_view1  = (View) layoutView.findViewById(R.id.filter_range_view1);
		filter_range_view2  = (View) layoutView.findViewById(R.id.filter_range_view2);
		filter_range_view3  = (View) layoutView.findViewById(R.id.filter_range_view3);
		filter_range_view4  = (View) layoutView.findViewById(R.id.filter_range_view4);
		filter_range_view5  = (View) layoutView.findViewById(R.id.filter_range_view5);
		filter_range_img1   = (ImageView) layoutView.findViewById(R.id.filter_range_img1);
		filter_range_img2   = (ImageView) layoutView.findViewById(R.id.filter_range_img2);
		filter_range_img3   = (ImageView) layoutView.findViewById(R.id.filter_range_img3);
		filter_range_img4   = (ImageView) layoutView.findViewById(R.id.filter_range_img4);
		filter_range_img5   = (ImageView) layoutView.findViewById(R.id.filter_range_img5);
		filter_from_date_value_btn = (Button) layoutView.findViewById(R.id.filter_from_date_value_btn);
		filter_to_date_value_btn = (Button) layoutView.findViewById(R.id.filter_to_date_value_btn);
		filter_all_date_value_ctv = (CheckedTextView) layoutView.findViewById(R.id.filter_all_date_value_ctv);
		filter_all_sport_value_ctv = (CheckedTextView) layoutView.findViewById(R.id.filter_all_sport_value_ctv);
		filter_baseball_sport_value_ctv = (CheckedTextView) layoutView.findViewById(R.id.filter_baseball_sport_value_ctv);
		filter_basketball_sport_value_ctv = (CheckedTextView) layoutView.findViewById(R.id.filter_basketball_sport_value_ctv);
		filter_football_sport_value_ctv = (CheckedTextView) layoutView.findViewById(R.id.filter_football_sport_value_ctv);
		filter_soccer_sport_value_ctv = (CheckedTextView) layoutView.findViewById(R.id.filter_soccer_sport_value_ctv);
		filter_softball_sport_value_ctv = (CheckedTextView) layoutView.findViewById(R.id.filter_softball_sport_value_ctv);
		filter_other_sport_value_ctv = (CheckedTextView) layoutView.findViewById(R.id.filter_other_sport_value_ctv);
		filter_apply_btn  = (LinearLayout) layoutView.findViewById(R.id.filter_apply_btn); 
		filter_reset_btn  = (LinearLayout) layoutView.findViewById(R.id.filter_reset_btn); 

		GooglePlacesAutoCompleteAdapter placesAdapter = new GooglePlacesAutoCompleteAdapter(activity,R.layout.list_place_items);
		filter_loc_actv.setAdapter(placesAdapter);		
		filter_loc_actv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (view.getTag() != null) {
					//selects location
					GeoData tmpData = (GeoData) view.getTag();
					GooglePlacesAutoCompleteAdapter.selectLoc = tmpData;
					filter_loc_actv.setText(tmpData.getLocation());
					location = tmpData.getLocation();

					//get latitude & longitude value from selected location reference
					GooglePlacesAutoCompleteAdapter.findLatLongFromReference(tmpData);
					//					latitude = tmpData.getLatitude();
					//					longitude = tmpData.getLongitude();
				} else {
					filter_loc_actv.setText("");
				}
			}
		});

		//Set to current location
		filter_current_loc_img.setOnClickListener(this);

		//Specify the range
		filter_range_sub1.setOnClickListener(this);
		filter_range_sub2.setOnClickListener(this);
		filter_range_sub3.setOnClickListener(this);
		filter_range_sub4.setOnClickListener(this);
		filter_range_sub5.setOnClickListener(this);
		
		//Set date range
		filter_from_date_value_btn.setOnClickListener(this);
		filter_to_date_value_btn.setOnClickListener(this);

		//apply or reset filter changes
		filter_apply_btn.setOnClickListener(this);
		filter_reset_btn.setOnClickListener(this);

		filter_all_date_value_ctv.setOnClickListener(this);
		filter_all_sport_value_ctv.setOnClickListener(this);
		filter_baseball_sport_value_ctv.setOnClickListener(this);
		filter_basketball_sport_value_ctv.setOnClickListener(this);
		filter_football_sport_value_ctv.setOnClickListener(this);
		filter_soccer_sport_value_ctv.setOnClickListener(this);
		filter_softball_sport_value_ctv.setOnClickListener(this);
		filter_other_sport_value_ctv.setOnClickListener(this);

		//on back button pressed in the title
		backBtn.setOnClickListener(this);

		//Loading current filter values
		location = SharedPreferencesUtils.daygetLocaton();
		latitude = SharedPreferencesUtils.daygetLatitude();
		longitude = SharedPreferencesUtils.daygetLongitude();		
		range = SharedPreferencesUtils.daygetRange();
		if(range.equalsIgnoreCase("all")){
			range = "300";
		}
		setForAllDates=SharedPreferencesUtils.daygetForAllDate();
		dtFormat = new SimpleDateFormat("MM/dd/yyyy");
		try {
			fromDt = dtFormat.parse(SharedPreferencesUtils.daygetFromDate());
			toDt = dtFormat.parse(SharedPreferencesUtils.daygetToDate());
		} catch (ParseException e) {
			Calendar cal = Calendar.getInstance();
			fromDt = cal.getTime();
			toDt = cal.getTime();
			e.printStackTrace();
		}		
		setForAllSports = SharedPreferencesUtils.daygetForAllSports();
		sportIds = SharedPreferencesUtils.daygetSportIds();

		serviceToGetSportsList();

		updateUI();
	}
	@Override
	public void onResume() {
		super.onResume();
		title.setText("FILTERS");
		settingBtn.setVisibility(View.INVISIBLE);
		backBtn.setVisibility(View.VISIBLE);
		tvEventType.setVisibility(View.INVISIBLE);
		searchHeaderBtn.setVisibility(View.GONE);
		addEventBtn.setVisibility(View.GONE);
		search_header_llyt.setVisibility(View.GONE);
		events_header_llyt.setVisibility(View.VISIBLE);
	}

	private void setRange(int rangeFlg){
		switch (rangeFlg){
		case 10:
			filter_range_view1.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img1.setImageResource(R.drawable.filter_range_select);
			filter_range_view2.setBackgroundResource(R.drawable.filter_range_reset);
			filter_range_img2.setImageResource(R.drawable.filter_range_unselect);
			filter_range_view3.setBackgroundResource(R.drawable.filter_range_reset);
			filter_range_img3.setImageResource(R.drawable.filter_range_unselect);
			filter_range_view4.setBackgroundResource(R.drawable.filter_range_reset);
			filter_range_img4.setImageResource(R.drawable.filter_range_unselect);
			filter_range_view5.setBackgroundResource(R.drawable.filter_range_reset);
			filter_range_img5.setImageResource(R.drawable.filter_range_unselect);
			//set filter range to 10 miles
			range = "10";
			break;
		case 50:
			filter_range_view1.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img1.setImageResource(R.drawable.filter_range_select);
			filter_range_view2.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img2.setImageResource(R.drawable.filter_range_select);
			filter_range_view3.setBackgroundResource(R.drawable.filter_range_reset);
			filter_range_img3.setImageResource(R.drawable.filter_range_unselect);
			filter_range_view4.setBackgroundResource(R.drawable.filter_range_reset);
			filter_range_img4.setImageResource(R.drawable.filter_range_unselect);
			filter_range_view5.setBackgroundResource(R.drawable.filter_range_reset);
			filter_range_img5.setImageResource(R.drawable.filter_range_unselect);
			//set filter range to 50 miles
			range = "50";
			break;
		case 100:
			filter_range_view1.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img1.setImageResource(R.drawable.filter_range_select);
			filter_range_view2.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img2.setImageResource(R.drawable.filter_range_select);
			filter_range_view3.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img3.setImageResource(R.drawable.filter_range_select);
			filter_range_view4.setBackgroundResource(R.drawable.filter_range_reset);
			filter_range_img4.setImageResource(R.drawable.filter_range_unselect);
			filter_range_view5.setBackgroundResource(R.drawable.filter_range_reset);
			filter_range_img5.setImageResource(R.drawable.filter_range_unselect);
			//set filter range to 100 miles
			range = "100";
			break;
		case 200:
			filter_range_view1.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img1.setImageResource(R.drawable.filter_range_select);
			filter_range_view2.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img2.setImageResource(R.drawable.filter_range_select);
			filter_range_view3.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img3.setImageResource(R.drawable.filter_range_select);
			filter_range_view4.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img4.setImageResource(R.drawable.filter_range_select);
			filter_range_view5.setBackgroundResource(R.drawable.filter_range_reset);
			filter_range_img5.setImageResource(R.drawable.filter_range_unselect);
			//set filter range to 200 miles
			range = "200";
			break;
		case 300:
			filter_range_view1.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img1.setImageResource(R.drawable.filter_range_select);
			filter_range_view2.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img2.setImageResource(R.drawable.filter_range_select);
			filter_range_view3.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img3.setImageResource(R.drawable.filter_range_select);
			filter_range_view4.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img4.setImageResource(R.drawable.filter_range_select);
			filter_range_view5.setBackgroundResource(R.drawable.filter_range_set);
			filter_range_img5.setImageResource(R.drawable.filter_range_select);
			//set filter range to all miles(300 miles means no range)
			Log.d("","all koti");
			range = "all";
			break;
		}
	}

	private void ToggleCheckbox(View v){
		if(v instanceof CheckedTextView){
			//Toggles the check box's
			((CheckedTextView) v).toggle();	

			// uncheck the all sports value when any sport is unchecked  
			if(!((CheckedTextView) v).isChecked()){
				setForAllSports = false;
				filter_all_sport_value_ctv.setChecked(false);
			}
		}
	}

	private void selectSports(int id){
		switch(id){
		case 0:
			//Cheked 'All sport ids selected'
			filter_baseball_sport_value_ctv.setChecked(true);
			filter_basketball_sport_value_ctv.setChecked(true);
			filter_football_sport_value_ctv.setChecked(true);
			filter_soccer_sport_value_ctv.setChecked(true);
			filter_softball_sport_value_ctv.setChecked(true);
			filter_other_sport_value_ctv.setChecked(true);
			break;
		case 1:
			filter_baseball_sport_value_ctv.setChecked(true);
			break;
		case 2:
			filter_basketball_sport_value_ctv.setChecked(true);
			break;
		case 3:
			filter_football_sport_value_ctv.setChecked(true);
			break;
		case 4:
			filter_soccer_sport_value_ctv.setChecked(true);
			break;
		case 5:
			filter_softball_sport_value_ctv.setChecked(true);
			break;
		case 6:
			filter_other_sport_value_ctv.setChecked(true);
			break;
		}
	}
	private void setAllDates(){
		//Disable from & to date change.
		filter_all_date_value_ctv.setChecked(true);
		filter_from_date_value_btn.setTextColor(Color.GRAY);
		filter_to_date_value_btn.setTextColor(Color.GRAY);
		filter_from_date_value_btn.setOnClickListener(null);
		filter_to_date_value_btn.setOnClickListener(null);
	}
	private void resetAllDates(){
		filter_all_date_value_ctv.setChecked(false);
		filter_from_date_value_btn.setTextColor(Color.BLACK);
		filter_to_date_value_btn.setTextColor(Color.BLACK);
		filter_from_date_value_btn.setOnClickListener(this);
		filter_to_date_value_btn.setOnClickListener(this);
	}
	private void setAllSports(){
		/*
		filter_baseball_sport_value_ctv.setChecked(true);
		filter_basketball_sport_value_ctv.setChecked(true);
		filter_football_sport_value_ctv.setChecked(true);
		filter_soccer_sport_value_ctv.setChecked(true);
		filter_softball_sport_value_ctv.setChecked(true);
		filter_other_sport_value_ctv.setChecked(true);*/
		filter_all_sport_value_ctv.setChecked(true);
		if(sportsItems!=null){
			for( int i=0;i<sportsItems.length;i++){
				sportsItems[i].setChecked(true);
			}
		}
	}

	private void resetAllSports(){
		/*
		filter_baseball_sport_value_ctv.setChecked(false);
		filter_basketball_sport_value_ctv.setChecked(false);
		filter_football_sport_value_ctv.setChecked(false);
		filter_soccer_sport_value_ctv.setChecked(false);
		filter_softball_sport_value_ctv.setChecked(false);
		filter_other_sport_value_ctv.setChecked(false);*/
		filter_all_sport_value_ctv.setChecked(false);
		if(sportsItems!=null){
			for( int i=0;i<sportsItems.length;i++){
				sportsItems[i].setChecked(false);
			}
		}
	}
	private String getSelectedSportsIds(){
		sportIds = "";
		for( int i=0;i<sportsItems.length;i++){
			if(sportsItems[i].isChecked()){
				sportIds = sportIds + " "+sportsList.get(i).getSportId() +",";
			}
		}
		/*	if(filter_baseball_sport_value_ctv.isChecked()){
			sportIds="1";
		}
		if(filter_basketball_sport_value_ctv.isChecked()){
			if(TextUtils.isEmpty(sportIds)){
				sportIds="2";	
			}else{
				sportIds= sportIds + ",2";
			}
		}
		if(filter_football_sport_value_ctv.isChecked()){
			if(TextUtils.isEmpty(sportIds)){
				sportIds="3";	
			}else{
				sportIds= sportIds + ",3";
			}
		}
		if(filter_soccer_sport_value_ctv.isChecked()){
			if(TextUtils.isEmpty(sportIds)){
				sportIds="4";	
			}else{
				sportIds= sportIds + ",4";
			}
		}
		if(filter_softball_sport_value_ctv.isChecked()){
			if(TextUtils.isEmpty(sportIds)){
				sportIds="5";	
			}else{
				sportIds= sportIds + ",5";
			}
		}
		if(filter_other_sport_value_ctv.isChecked()){
			if(TextUtils.isEmpty(sportIds)){
				sportIds="0";	
			}
		}*/
		return sportIds;
	}
	private void updateUI(){
		//set location val on UI
		filter_loc_actv.setText(location);
		setRange(Integer.parseInt(range));

		//set date val on UI
		if(setForAllDates){
			setAllDates();	
		}else{
			resetAllDates();
		}
		filter_from_date_value_btn.setText(dtFormat.format(fromDt));
		filter_to_date_value_btn.setText(dtFormat.format(toDt));

		/*//Clear all sports check marks		
		if(setForAllSports){
			setAllSports();
		}else{
			resetAllSports();
			String[] sportIdsArray = sportIds.split(",");
			for(int i=0; i<sportIdsArray.length; i++){
				selectSports(Integer.parseInt(sportIdsArray[i]));
			}	
		}*/
	}

	private void clearChanges(){
		location = Constants.locationName;
		if(TextUtils.isEmpty(location)){
			location = "My location";
		}
		latitude = Constants.lati;
		longitude = Constants.longi;
//		range = "100";
		range = "200";
		setForAllDates=true;
		setForAllSports=true;
		saveChanges();
	}
	private void saveChanges(){
		if(validations()){
			if(GooglePlacesAutoCompleteAdapter.selectLoc != null){
				location = GooglePlacesAutoCompleteAdapter.selectLoc.getLocation();
				latitude = GooglePlacesAutoCompleteAdapter.selectLoc.getLatitude();
				longitude = GooglePlacesAutoCompleteAdapter.selectLoc.getLongitude();
			}
			SharedPreferencesUtils.daysetLocaton(location);
			SharedPreferencesUtils.daysetLatitude(latitude);
			SharedPreferencesUtils.daysetLongitude(longitude);
			SharedPreferencesUtils.daysetRange(range);
//			Constants.lati = latitude;
//			Constants.longi = longitude;
//			Constants.locationName = location;

			//setting dates 
			if(setForAllDates){
				SharedPreferencesUtils.daysetForAllDate();	
			}else{
				SharedPreferencesUtils.dayresetForAllDate();
				SharedPreferencesUtils.daysetFromDate(dtFormat.format(fromDt));
				SharedPreferencesUtils.daysetToDate(dtFormat.format(toDt));
			}

			//Sport id's
			if(setForAllSports){
				//For all sports events to display
				SharedPreferencesUtils.daysetForAllSports();	
				SharedPreferencesUtils.daysetSportIds("0");
			}else{
				SharedPreferencesUtils.dayresetForAllSports();
				//TODO: get sports Ids details
				SharedPreferencesUtils.daysetSportIds(getSelectedSportsIds());
			}

			//Display success saving tost and close filter frag
			//Log filter change flag
			UIHelperUtil.showToastMessage("saved successfully");
			SharedPreferencesUtils.daysetFilterChangedFlag();
			activity.onBackPressed();
		}
	}
	private boolean validations(){
		if(TextUtils.isEmpty(location)){
			UIHelperUtil.showToastMessage("Please enter location");
			return false;
		}
		if(TextUtils.isEmpty(range)){
			UIHelperUtil.showToastMessage("Please set range");
			return false;
		}
		if(!setForAllDates){
			if(TextUtils.isEmpty(fromDt.toString())){
				UIHelperUtil.showToastMessage("Please enter from date");
				return false;
			}else if(TextUtils.isEmpty(toDt.toString())){
				UIHelperUtil.showToastMessage("Please enter to date");
				return false;
			}
		}
		if(!setForAllSports){
			if(TextUtils.isEmpty(getSelectedSportsIds())){
				UIHelperUtil.showToastMessage("Please select sport");
				return false;
			}
		}
		return true;
	}
	private void setCurrentLocation(){
		if(TextUtils.isEmpty(Constants.locationName)){
			UIHelperUtil.showToastMessage("Unable to get current location");
			Constants.locationName = SharedPreferencesUtils.daygetLocaton();
		}
		location = Constants.locationName;
		latitude = Constants.lati;
		longitude = Constants.longi;
		filter_loc_actv.setText(location);
		GooglePlacesAutoCompleteAdapter.selectLoc = null;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.filter_current_loc_img:
			setCurrentLocation();
			break;
		case R.id.filter_range_sub1:
			setRange(10);
			break;
		case R.id.filter_range_sub2:
			setRange(50);
			break;
		case R.id.filter_range_sub3:
			setRange(100);
			break;
		case R.id.filter_range_sub4:
			setRange(200);
			break;		

		case R.id.filter_range_sub5:
			setRange(300);
			break;
		case R.id.back_iv: //On back button pressed in the title bar
			activity.onBackPressed();
			break;
		case R.id.filter_reset_btn:
			//clear all changes & save
			clearChanges();
			SharedPreferencesUtils.dayresetFilterApplyedFlag();
			break;
		case R.id.filter_apply_btn:
			// save all changes
			
			if(!filter_loc_actv.getText().toString().isEmpty()){
				saveChanges();
				SharedPreferencesUtils.daysetFilterApplyedFlag();
			}else{
				UIHelperUtil.showToastMessage("Select location please");
			}
			
			
			break;
		case R.id.filter_all_date_value_ctv:
			setForAllDates = !setForAllDates;
			if(setForAllDates){
				setAllDates();	
			}else{
				resetAllDates();
			}
			break;
		case R.id.filter_all_sport_value_ctv:
			setForAllSports = !setForAllSports;
			if(setForAllSports){
				setAllSports();
			}else{
				resetAllSports();
			}
			break;
		case R.id.filter_baseball_sport_value_ctv:
		case R.id.filter_basketball_sport_value_ctv:
		case R.id.filter_football_sport_value_ctv:
		case R.id.filter_soccer_sport_value_ctv:
		case R.id.filter_softball_sport_value_ctv:
		case R.id.filter_other_sport_value_ctv:
			ToggleCheckbox(v);
			break;
		case R.id.filter_from_date_value_btn:
			DialogFragment newFragment = new SelectDateFragment();
			newFragment.show(getChildFragmentManager(), "FromDatePicker");
			break;
		case R.id.filter_to_date_value_btn:			
			DialogFragment ToDtFragment = new SelectDateFragment();
			ToDtFragment.show(getChildFragmentManager(), "ToDatePicker");
			break;
		} 
	}
	public class SelectDateFragment extends DialogFragment implements OnDateSetListener {
		//Displays date picker and set from & to date values to selected dates 
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			if(this.getTag().toString().equalsIgnoreCase("FromDatePicker")){				
				calendar.setTime(fromDt);
			}else{
				calendar.setTime(toDt);
			}
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(activity, this, yy, mm, dd);
		}

		@Override
		public void onDateSet(DatePicker view, int yy, int mm, int dd) {

			populateSetDate(yy, mm+1, dd,this.getTag().toString());
		}

		public void populateSetDate(int year, int month, int day, String tag) {
			if(tag.equalsIgnoreCase("FromDatePicker")){
				try {
					fromDt = dtFormat.parse(month+"/"+day+"/"+year);
					filter_from_date_value_btn.setText(dtFormat.format(fromDt));					
				} catch (ParseException e) {
					e.printStackTrace();
					Calendar cal = Calendar.getInstance();
					fromDt = cal.getTime();
					filter_from_date_value_btn.setText(dtFormat.format(fromDt));
				}
				//Performing validations: Change 'to' date when 'from' date changed
				if(fromDt.getTime() > toDt.getTime()){
					toDt = fromDt;
					filter_to_date_value_btn.setText(dtFormat.format(toDt));
				}
			}else{
				try
				{
					toDt = dtFormat.parse(month+"/"+day+"/"+year);
					filter_to_date_value_btn.setText(dtFormat.format(toDt));
				} catch (ParseException e) {
					e.printStackTrace();
					Calendar cal = Calendar.getInstance();
					toDt = cal.getTime();
					filter_to_date_value_btn.setText(dtFormat.format(toDt));
				}
				//Performing validations: Change 'from' date when 'to' date changed
				if(toDt.getTime() < fromDt.getTime()){
					fromDt = toDt;
					filter_from_date_value_btn.setText(dtFormat.format(fromDt));
				}
			}
		}
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
				sportsList = new ArrayList<SportModel>();
				if (response != null && response.equals("SUCCESS")) {
					JSONArray sportsDetailsJson = sys
							.getJSONArray("sprots_list");

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
						//Update sports list on UI
						sportsItems = new CheckedTextView[sportsList.size()];
						for( int i=0;i<sportsList.size();i++){
							sportsItems[i] = new CheckedTextView(activity);
							sportsItems[i].setText(sportsList.get(i).getSportName());
							sportsItems[i].setCheckMarkDrawable(R.drawable.filter_checkbox_background);
							sportIds = SharedPreferencesUtils.daygetSportIds();							
							filter_sub_layout.addView(sportsItems[i]);
							sportsItems[i].setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									((CheckedTextView) v).toggle();
									if(!((CheckedTextView) v).isChecked()){
										setForAllSports= false;
										filter_all_sport_value_ctv.setChecked(false);
									}
								}
							});
						}
					}
					//Clear all sports check marks		
					if(setForAllSports){
						setAllSports();
					}else{
						setSelectedSports();	
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

	private void setSelectedSports() {
		sportIds = SharedPreferencesUtils.daygetSportIds();
		filter_all_sport_value_ctv.setChecked(true);
		setForAllSports = true;
		for( int i=0;i<sportsItems.length;i++){
			if((!TextUtils.isEmpty(sportIds) && sportIds.contains(" "+sportsList.get(i).getSportId()+","))
					|| sportIds.equals("0")){
				sportsItems[i].setChecked(true);
			}else{
				sportsItems[i].setChecked(false);
				filter_all_sport_value_ctv.setChecked(false);
				setForAllSports = false;
			}
		}

	}
}
