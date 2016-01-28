package com.mysportsshare.mysportscast.adapters;

import java.util.List;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.adapters.EventsAdapter.VehicleViewHolder;
import com.mysportsshare.mysportscast.models.EventInfo;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MyEventsAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	//	private Context mContext;
	//	private Activity activity;
	private List<EventInfo> eventsList; //Represents events list
	private String eventType; //Represents event type
	private boolean isfinished; //Represents finished events
	Context mContext;

	public MyEventsAdapter(Context context, List<EventInfo> eventsList, String eventType,boolean isfinished){
		//		this.activity = context;
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
	public Object getItem(int position) {
		return eventsList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}


	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.fragment_my_event_row,null);  
			holder.sportName = (TextView) convertView.findViewById(R.id.myEvent_sport_name_tv);
			holder.eventTitle = (TextView) convertView.findViewById(R.id.myEvent_title_tv);
			holder.location = (TextView) convertView.findViewById(R.id.myEvent_location_tv);
			holder.datentime = (TextView) convertView.findViewById(R.id.myEvent_date_time_tv);
			holder.distance = (TextView) convertView.findViewById(R.id.myEvent_distance_tv);
			holder.eventPic = (ImageView) convertView.findViewById(R.id.myEvent_event_pic_iv);
			holder.castProfPic = (ImageView) convertView.findViewById(R.id.myEvent_cast_prof_pic);
			holder.eventType = (TextView)convertView.findViewById(R.id.myEvent_sport_type_tv);
			holder.cheers = (TextView)convertView.findViewById(R.id.myEvent_cheers_tv);
			holder.userName = (TextView)convertView.findViewById(R.id.myEvent_follower_name_tv);
			holder.castText = (TextView)convertView.findViewById(R.id.myEvent_comment_tv);
			holder.castLlyt = (LinearLayout)convertView.findViewById(R.id.myEvent_cast_llyt);
			holder.distanceIcon = (ImageView) convertView.findViewById(R.id.myEvent_distance_icon);
			holder.attendingCount = (TextView)convertView.findViewById(R.id.myEvent_attending_cnt_tv);
			holder.menuIcon = (ImageView)convertView.findViewById(R.id.myEvent_edit_options);			

			convertView.setTag(holder);
		} else{
			holder = (ViewHolder) convertView.getTag();
		}

		//Assigning contextual menu to a view
		holder.menuIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//To display pop-up menu on clicking menu button
				PopupMenu myEventPopupMenu = new PopupMenu(mContext,v);
				//Inflate menu layout.xml to menu dialog 
				myEventPopupMenu.getMenuInflater().inflate(R.menu.myevent_menu, myEventPopupMenu.getMenu());
				myEventPopupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem menuItem) {
						switch (menuItem.getItemId()) {
						case R.id.myEvent_view_op:
							Toast.makeText(mContext, "yet to imp view detailed activity", Toast.LENGTH_SHORT).show();
							return true;
						case R.id.myEvent_edit_op:
							Toast.makeText(mContext, "yet to imp edit event", Toast.LENGTH_SHORT).show();
							return true;
						case R.id.myEvent_delete_op:
							Toast.makeText(mContext, "yet to imp delete event", Toast.LENGTH_SHORT).show();
							return true;
						default:
							return false;
						}
					}
				});
				myEventPopupMenu.show();				
			}
		});

		holder.sportName.setText(eventsList.get(pos).getSportName().toUpperCase());
		holder.eventTitle.setText(eventsList.get(pos).getEventTitle().toUpperCase());
		if(isfinished){
			holder.distance.setVisibility(View.GONE);
			holder.distanceIcon.setVisibility(View.GONE);
		}else{
			holder.distanceIcon.setVisibility(View.VISIBLE);
			holder.distance.setVisibility(View.VISIBLE);
			holder.distance.setText(eventsList.get(pos).getEventDistanceFromUserLoc());

		}

		holder.location.setText(eventsList.get(pos).getEventCity());
		//		holder.eventType.setText(eventsList.get(pos).getEventType().toUpperCase());
		holder.cheers.setText(eventsList.get(pos).getEventLikes()+" cheers");
		if(eventsList.get(pos).getUserName() != null && eventsList.get(pos).getUserName().length() > 0 && eventsList.get(pos).getProfilePicPath() != null && eventsList.get(pos).getProfilePicPath().length() > 0){
			holder.castLlyt.setVisibility(View.VISIBLE);
			holder.userName.setText(eventsList.get(pos).getUserName());
			BitmapUtils.setImagesViaPicaso(eventsList.get(pos).getProfilePicPath(), holder.castProfPic,R.drawable.profile_pic_dummy,mContext);
			holder.castText.setText(eventsList.get(pos).getLatestCast());
		}else{
			holder.castLlyt.setVisibility(View.GONE);
		}

		holder.attendingCount.setText(eventsList.get(pos).getAttendingCount()+" "+eventType);
		holder.datentime.setText(eventsList.get(pos).getEventStartDate()+" at "+ eventsList.get(pos).getEventStartTime());
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
	public List<EventInfo> getOldEventsList() {
		Log.v("", "Old List: " + eventsList.size());
		return eventsList;
	}
	
	public void updateEvents(List<EventInfo> tempList) {
		this.eventsList = tempList;
		notifyDataSetChanged();
	}
	
	class ViewHolder{ 
		public TextView sportName,eventTitle,location,datentime,distance,eventType,attendingCount,cheers,userName,castText;
		public LinearLayout castLlyt;
		public ImageView eventPic,castProfPic,distanceIcon, menuIcon;
	}

}
