package com.mysportsshare.mysportscast.adapters;

import java.util.List;
import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.models.FollowerUserInfo;
import com.mysportsshare.mysportscast.utils.AdapterCallback;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FollowerUserAdapter extends BaseAdapter {

	LayoutInflater inflater;
	Activity activity;
	List<FollowerUserInfo> followers;
	AdapterCallback adapterListener;
	public FollowerUserAdapter(Activity activity, List<FollowerUserInfo> followersList,AdapterCallback adapterListener){
		this.activity = activity;
		this.followers = followersList;
		this.inflater = LayoutInflater.from(activity);
		this.adapterListener = adapterListener;
	}

	@Override
	public int getCount() {
		return followers.size();
	}

	@Override
	public Object getItem(int pos) {
		return followers.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	public void updateUserAtPosition(int pos, String followStatus){
		try {
			FollowerUserInfo tmpUser = followers.get(pos);
			tmpUser.setFollowStatus(followStatus);
			notifyDataSetChanged();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public View getView(int pos, View contentView, ViewGroup arg2) {
		FollowerHolder tmpHolder;
		if(contentView == null){
			contentView = inflater.inflate(R.layout.fragment_follower_row, null);
			tmpHolder= new FollowerHolder();
			tmpHolder.userName = (TextView) contentView.findViewById(R.id.follower_row_userName);
			tmpHolder.userTag = (TextView) contentView.findViewById(R.id.follower_row_userTag);
			tmpHolder.photo = (ImageView) contentView.findViewById(R.id.follower_row_img);
			tmpHolder.followBtnLayout = (LinearLayout) contentView.findViewById(R.id.follower_row_followbuton);
			tmpHolder.followBtnTxt = (TextView) contentView.findViewById(R.id.follower_btn_text);
			tmpHolder.followBtnImg = (ImageView) contentView.findViewById(R.id.follower_btn_img);
//			//Updating following user item position in the listview			
//			((FollowerUserInfo) followers. get(pos)).setItemPosition(pos);
			tmpHolder.followBtnLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					LinearLayout tmpBtn = (LinearLayout) v;
					FollowerUserInfo tmpFollowUserInfo = (FollowerUserInfo)v.getTag();
					String status="";
					if((tmpFollowUserInfo.getFollowStatus().equalsIgnoreCase(Constants.userProf_follow_status_follow))
							|| (tmpFollowUserInfo.getFollowStatus().equalsIgnoreCase(Constants.userProf_follow_status_requested))){
						//send request to unfollow user
						status = "0";
					}else if(tmpFollowUserInfo.getFollowStatus().equalsIgnoreCase(Constants.userProf_follow_status_not_follow)){
						//send request to follow this user
						status = "1";
					}

					//Send follow/unfollow req to the webserver
					adapterListener.onClickButton(tmpFollowUserInfo.getUserID(), tmpFollowUserInfo.getItemPosition(), status,tmpFollowUserInfo.getProfile_visibility());
				}
			});
			contentView.setTag(tmpHolder);
		}else{
			tmpHolder = (FollowerHolder) contentView.getTag();
//			//Updating following user item position in the listview			
//			((FollowerUserInfo) followers. get(pos)).setItemPosition(pos);
		}

		tmpHolder.userID = followers.get(pos).getUserID();
		tmpHolder.userName.setText(followers.get(pos).getUserName());
		BitmapUtils.setImages(followers.get(pos).getPhoto(),tmpHolder.photo, R.drawable.profile_pic_dummy);

		//When requested user not following this user. 
		//When requested user not following this user. 
		if(followers.get(pos).getFollowStatus().equalsIgnoreCase(Constants.userProf_follow_status_follow)){
			tmpHolder.followBtnTxt.setText(Constants.UI_IamFollowingStatus);
			tmpHolder.followBtnLayout.setBackgroundResource(R.drawable.following_btn);
			tmpHolder.followBtnTxt.setTextColor(activity.getResources().getColor(R.color.white));
			tmpHolder.followBtnImg.setImageResource(R.drawable.following);
		}else if(followers.get(pos).getFollowStatus().equalsIgnoreCase(Constants.userProf_follow_status_not_follow)){
			tmpHolder.followBtnTxt.setText(Constants.UI_IamNotFollowingStatus);
			tmpHolder.followBtnLayout.setBackgroundResource(R.drawable.follow_btn);
			tmpHolder.followBtnTxt.setTextColor(activity.getResources().getColor(R.color.com_facebook_blue));
			tmpHolder.followBtnImg.setImageResource(R.drawable.follow);
		}else if(followers.get(pos).getFollowStatus().equalsIgnoreCase(Constants.userProf_follow_status_requested)){
			tmpHolder.followBtnTxt.setText(Constants.UI_REQUESTED);
			tmpHolder.followBtnLayout.setBackgroundResource(R.drawable.requested_btn);
			tmpHolder.followBtnTxt.setTextColor(activity.getResources().getColor(R.color.white));
			tmpHolder.followBtnImg.setImageResource(R.drawable.requested);
		}else{
			tmpHolder.followBtnLayout.setVisibility(View.GONE);
		}
		
		tmpHolder.followBtnLayout.setTag(followers.get(pos));
//		tmpHolder.followBtnLayout.setFocusable(false);

		return contentView;
	}

	public class FollowerHolder{
		String userID;
		TextView userName;
		TextView userTag;
		LinearLayout followBtnLayout;
		TextView followBtnTxt;
		ImageView followBtnImg;
		ImageView photo;
		public String getUserID() {
			return userID;
		}
		public void setUserID(String userID) {
			this.userID = userID;
		}
		public TextView getUserName() {
			return userName;
		}
		public void setUserName(TextView userName) {
			this.userName = userName;
		}
		public TextView getUserTag() {
			return userTag;
		}
		public void setUserTag(TextView userTag) {
			this.userTag = userTag;
		}		
		public LinearLayout getFollowBtnlayout() {
			return followBtnLayout;
		}
		public void setFollowBtnlayout(LinearLayout followBtnlayout) {
			this.followBtnLayout = followBtnlayout;
		}
		public TextView getFollowBtnText() {
			return followBtnTxt;
		}
		public void setFollowBtnText(TextView followBtnText) {
			this.followBtnTxt = followBtnText;
		}
		public ImageView getFollowBtnImg() {
			return followBtnImg;
		}
		public void setFollowBtnImg(ImageView followBtnImg) {
			this.followBtnImg = followBtnImg;
		}
		public ImageView getPhoto() {
			return photo;
		}
		public void setPhoto(ImageView photo) {
			this.photo = photo;
		}

	}
}
