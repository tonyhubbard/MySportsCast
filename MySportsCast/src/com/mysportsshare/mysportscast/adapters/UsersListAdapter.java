package com.mysportsshare.mysportscast.adapters;

import java.util.List;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.activity.CalendarEventsActivity;
import com.mysportsshare.mysportscast.activity.MainActivity;
import com.mysportsshare.mysportscast.fragments.UserProfileFragment;
import com.mysportsshare.mysportscast.models.UserProfileInfo;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UsersListAdapter extends ArrayAdapter<UserProfileInfo> {

	Context context;
	public UsersListAdapter(Context context, int resource, List<UserProfileInfo> commentUsersList) {
		super(context, resource, commentUsersList);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		UserProfileInfo user = getItem(position);    
		// Check if an existing view is being reused, otherwise inflate the view
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.users_list, parent, false);
			// Lookup view for data population
			TextView tvName = (TextView) convertView.findViewById(R.id.user_Name);
			ImageView imgUser = (ImageView) convertView.findViewById(R.id.user_img);
			holder.setImgUser(imgUser);
			holder.setTvName(tvName);
			holder.setUser(user);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}

		// Populate the data into the template view using the data object
		holder.getTvName().setText(user.getFirstName());
		holder.getImgUser().setTag(holder);
		BitmapUtils.setImages(user.getPhoto(), holder.getImgUser(), R.drawable.profile_pic_dummy);

		/*//Setting onclick listener
		holder.getImgUser().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// To open the user profile 
				displayUserProfile(v);
			}
		});*/

		// Return the completed view to render on screen
		return convertView;
	}
	public class Holder{
		TextView tvName;
		ImageView imgUser;
		UserProfileInfo user;
		public TextView getTvName() {
			return tvName;
		}
		public void setTvName(TextView tvName) {
			this.tvName = tvName;
		}
		public ImageView getImgUser() {
			return imgUser;
		}
		public void setImgUser(ImageView imgUser) {
			this.imgUser = imgUser;
		}
		public UserProfileInfo getUser() {
			return user;
		}
		public void setUser(UserProfileInfo user) {
			this.user = user;
		}
		
	}

	//Display user profile
	public void displayUserProfile(View v){
		UserProfileInfo tmp1 = (UserProfileInfo) v.getTag();
		if (tmp1 != null) {
			String userId = tmp1.getUserID();
			UserProfileFragment activeFragment = new UserProfileFragment();
			Bundle arg = new Bundle();
			arg.putString(Constants.dataReceive, userId);
			activeFragment.setArguments(arg);
			if(context!=null){
				if(context.getClass().getSimpleName().equalsIgnoreCase("MainActivity")){
					//stack & displayed on the Main screen
					((MainActivity) context).pushFragments(
							((MainActivity) context).getmCurrentTab(),
							activeFragment, false, true);
				}else if(context instanceof CalendarEventsActivity){
					//stack & displayed on the Calendar screen						
					((CalendarEventsActivity)context).push(activeFragment);
				}
			}
		}	
	}
}
