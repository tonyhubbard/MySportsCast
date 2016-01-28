package com.mysportsshare.mysportscast.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.models.EventInfo;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;

/**
 * This class holds all the events and is responsible for filling up listview.
 * @author sharma
 *
 */
public class CalendarEventsAdapter extends BaseAdapter {

	//objects
	private LayoutInflater inflater;

	//Values
	private List<EventInfo> eventsList;
	private String eventType;
	private boolean isfinished;
	Context mContext;

	public CalendarEventsAdapter(Context context, List<EventInfo> eventsList, String eventType,boolean isfinished) {
		this.eventsList = eventsList;
		this.eventType = eventType;
		this.isfinished = isfinished;
		mContext = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return eventsList.size();
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public Object getItem(int position) {
		return eventsList.get(position);
	}	

	@Override
	public View getView(final int pos, View convertView, ViewGroup arg2) {
		VehicleViewHolder holder;
		if(convertView == null){
			holder = new VehicleViewHolder();
			convertView = inflater.inflate(R.layout.event_calendar_item,null);  
			holder.sportName = (TextView) convertView.findViewById(R.id.event_sport_name);
			holder.eventTitle = (TextView) convertView.findViewById(R.id.event_name);
			holder.location = (TextView) convertView.findViewById(R.id.location_tv);
			holder.datentime = (TextView) convertView.findViewById(R.id.date_time_tv);
			holder.eventPic = (ImageView) convertView.findViewById(R.id.event_img);
			holder.eventType = (TextView)convertView.findViewById(R.id.event_sport_type);
			holder.eventDesc = (TextView)convertView.findViewById(R.id.event_desc);
			convertView.setTag(holder);
		} else{
			holder = (VehicleViewHolder) convertView.getTag();
		}

		holder.sportName.setText(eventsList.get(pos).getSportName().toUpperCase());
		holder.eventTitle.setText(eventsList.get(pos).getEventTitle().toUpperCase());
		holder.eventDesc.setText(eventsList.get(pos).getEventDescription());
//		if(isfinished){
//			holder.distance.setVisibility(View.GONE);
//			holder.distanceIcon.setVisibility(View.GONE);
//		}else{
//			holder.distanceIcon.setVisibility(View.VISIBLE);
//			holder.distance.setVisibility(View.VISIBLE);
//			holder.distance.setText(eventsList.get(pos).getEventDistanceFromUserLoc());
//
//		}

		holder.location.setText(eventsList.get(pos).getEventCity());
		holder.eventType.setText(eventsList.get(pos).getEventType().toUpperCase());
//		holder.cheers.setText(eventsList.get(pos).getEventLikes()+" cheers");
//		if(eventsList.get(pos).getUserName() != null && eventsList.get(pos).getUserName().length() > 0 && eventsList.get(pos).getProfilePicPath() != null && eventsList.get(pos).getProfilePicPath().length() > 0){
//			holder.castLlyt.setVisibility(View.VISIBLE);
//			holder.userName.setText(eventsList.get(pos).getUserName());
//			BitmapUtils.setImagesViaPicaso(eventsList.get(pos).getProfilePicPath(), holder.castProfPic,R.drawable.profile_pic_dummy,mContext);
//			holder.castText.setText(eventsList.get(pos).getLatestCast());
//		}else{
//			holder.castLlyt.setVisibility(View.GONE);
//		}

		holder.datentime.setText(eventsList.get(pos).getEventStartTime()); //eventsList.get(pos).getEventStartDate()+" at "+ 

		//Load sports default pic
		if(eventsList.get(pos).getSportName().equalsIgnoreCase(Constants.SPORT_BASEBALL)){
			BitmapUtils.setImages(eventsList.get(pos).getEventImageUrl(), holder.eventPic, R.drawable.default_baseball);
		}else if(eventsList.get(pos).getSportName().equalsIgnoreCase(Constants.SPORT_BASKETBALL)){
			BitmapUtils.setImages(eventsList.get(pos).getEventImageUrl(), holder.eventPic, R.drawable.default_basketball);
		}else if(eventsList.get(pos).getSportName().equalsIgnoreCase(Constants.SPORT_FOOTBALL)){
			BitmapUtils.setImages(eventsList.get(pos).getEventImageUrl(), holder.eventPic, R.drawable.default_football);
		}else if(eventsList.get(pos).getSportName().equalsIgnoreCase(Constants.SPORT_SOCCER)){
			BitmapUtils.setImages(eventsList.get(pos).getEventImageUrl(), holder.eventPic, R.drawable.default_soccer);
		}else if(eventsList.get(pos).getSportName().equalsIgnoreCase(Constants.SPORT_SOFTBALL)){
			BitmapUtils.setImages(eventsList.get(pos).getEventImageUrl(), holder.eventPic, R.drawable.default_softball);
		}else{
			BitmapUtils.setImages(eventsList.get(pos).getEventImageUrl(), holder.eventPic, R.drawable.default_event_pic);
		}

		return convertView;
	}


	public void addNewEvent(EventInfo event){
		this.eventsList.add(0, event);
		notifyDataSetChanged();
	}
	
	public void updateEvents(List<EventInfo> tempList) {
		this.eventsList = tempList;
		notifyDataSetChanged();
	}

	public List<EventInfo> getOldEventsList() {
		Log.v("", "Old List: " + eventsList.size());
		return eventsList;
	}

	//View Holder
	class VehicleViewHolder{
		public TextView eventDesc;
		public TextView sportName,eventTitle,location,datentime,eventType;
		public LinearLayout castLlyt;
		public ImageView eventPic;
	}

}
