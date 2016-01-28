package com.mysportsshare.mysportscast.adapters;

import java.util.List;
import java.util.zip.Inflater;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.models.FollowingUserInfo;
import com.mysportsshare.mysportscast.utils.AdapterCallback;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FollowingUserAdapter extends BaseAdapter {

	LayoutInflater inflater;
	Activity activity;
	List<FollowingUserInfo> following;
	AdapterCallback adapterListener;

	public FollowingUserAdapter(Activity activity, List<FollowingUserInfo> followingList, AdapterCallback adapterListener){
		this.activity = activity;
		this.following = followingList;
		this.inflater = LayoutInflater.from(activity);
		this.adapterListener = adapterListener;
	}

	@Override
	public int getCount() {
		return following.size();
	}

	@Override
	public Object getItem(int pos) {
		return following.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public void updateUserAtPosition(int pos, String followStatus){
		try {
			FollowingUserInfo tmpUser = following.get(pos);
			tmpUser.setFollowStatus(followStatus);
			notifyDataSetChanged();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public View getView(int pos, View contentView, ViewGroup arg2) {
		FollowingHolder tmpHolder;
		if(contentView == null){
			contentView = inflater.inflate(R.layout.fragment_following_row, null);
			tmpHolder= new FollowingHolder();
			tmpHolder.userName = (TextView) contentView.findViewById(R.id.following_row_userName);
			tmpHolder.userTag = (TextView) contentView.findViewById(R.id.following_row_userTag);
			tmpHolder.photo = (ImageView) contentView.findViewById(R.id.following_row_img);			
			tmpHolder.followBtnLayout = (LinearLayout) contentView.findViewById(R.id.following_row_followbuton);
			tmpHolder.followBtnTxt = (TextView) contentView.findViewById(R.id.following_btn_text);
			tmpHolder.followBtnImg = (ImageView) contentView.findViewById(R.id.following_btn_img);
//			//Updating following user item position in the listview			
//			((FollowingUserInfo) following.get(pos)).setItemPosition(pos);
			tmpHolder.followBtnLayout.setTag(following.get(pos));			
			tmpHolder.followBtnLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					LinearLayout tmpBtn = (LinearLayout) v;
					FollowingUserInfo tmpFollowUserInfo = (FollowingUserInfo)v.getTag();
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
			tmpHolder = (FollowingHolder) contentView.getTag();
		}

		tmpHolder.userID = following.get(pos).getUserID();
		tmpHolder.userName.setText(following.get(pos).getUserName());
		BitmapUtils.setImages(following.get(pos).getPhoto(),tmpHolder.photo, R.drawable.profile_pic_dummy);

		//When requested user not following this user. 
		if(following.get(pos).getFollowStatus().equalsIgnoreCase(Constants.userProf_follow_status_follow)){
			tmpHolder.followBtnTxt.setText(Constants.UI_IamFollowingStatus);
			tmpHolder.followBtnLayout.setBackgroundResource(R.drawable.following_btn);
			tmpHolder.followBtnTxt.setTextColor(activity.getResources().getColor(R.color.white));
			tmpHolder.followBtnImg.setImageResource(R.drawable.following);
		}else if(following.get(pos).getFollowStatus().equalsIgnoreCase(Constants.userProf_follow_status_not_follow)){
			tmpHolder.followBtnTxt.setText(Constants.UI_IamNotFollowingStatus);
			tmpHolder.followBtnLayout.setBackgroundResource(R.drawable.follow_btn);
			tmpHolder.followBtnTxt.setTextColor(activity.getResources().getColor(R.color.com_facebook_blue));
			tmpHolder.followBtnImg.setImageResource(R.drawable.follow);
		}else if(following.get(pos).getFollowStatus().equalsIgnoreCase(Constants.userProf_follow_status_requested)){
			tmpHolder.followBtnTxt.setText(Constants.UI_REQUESTED);
			tmpHolder.followBtnLayout.setBackgroundResource(R.drawable.requested_btn);
			tmpHolder.followBtnTxt.setTextColor(activity.getResources().getColor(R.color.com_facebook_blue));
			tmpHolder.followBtnImg.setImageResource(R.drawable.requested);
		}else{
			tmpHolder.followBtnLayout.setVisibility(View.GONE);
		}
//		tmpHolder.followBtnLayout.setFocusable(false);
		tmpHolder.followBtnLayout.setTag(following.get(pos));
		return contentView;
	}

	public class FollowingHolder{		
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

		public ImageView getPhoto() {
			return photo;
		}
		public void setPhoto(ImageView photo) {
			this.photo = photo;
		}
		public LinearLayout getFollowBtnLayout() {
			return followBtnLayout;
		}
		public void setFollowBtnLayout(LinearLayout followBtnLayout) {
			this.followBtnLayout = followBtnLayout;
		}
		public TextView getFollowBtnTxt() {
			return followBtnTxt;
		}
		public void setFollowBtnTxt(TextView followBtnTxt) {
			this.followBtnTxt = followBtnTxt;
		}
		public ImageView getFollowBtnImg() {
			return followBtnImg;
		}
		public void setFollowBtnImg(ImageView followBtnImg) {
			this.followBtnImg = followBtnImg;
		}

	}
}
