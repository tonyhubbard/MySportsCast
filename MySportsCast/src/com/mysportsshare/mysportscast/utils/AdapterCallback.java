package com.mysportsshare.mysportscast.utils;

public interface AdapterCallback {
	
	//To provide interface between the 'follow/following' button at adapter to fragment 	
	public void onClickButton(String userID, int itemPos, String status,String userPrivacy);
	
	//To udpate UI, after successful updation of follow/unfollow status. 
	public void onStatusReturn(String message);
}
