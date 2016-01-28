package com.mysportsshare.mysportscast.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.models.Comments;
import com.mysportsshare.mysportscast.models.EventInfo;
import com.mysportsshare.mysportscast.models.ProfileMediaInfo;
import com.mysportsshare.mysportscast.parser.JsonParser;
import com.mysportsshare.mysportscast.parser.JsonServiceListener;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.SharedPreferencesUtils;
import com.mysportsshare.mysportscast.utils.UIHelperUtil;

public class CommentsAdapter extends ArrayAdapter<Comments> implements OnClickListener{
	private LayoutInflater inflater;
	private List<Comments> list;
	private String userId;
	private String eventMediaType;
	private Context context;
	private  Object eventMedia;
	
	public CommentsAdapter(Context context, int resource,String eventMediaType, Object eventMedia,
			List<Comments> list) {
		super(context, resource, list);
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
		userId = SharedPreferencesUtils.getUserId();
		this.eventMediaType = eventMediaType;
		this.eventMedia = eventMedia;
	}

	//	@Override
	//	public CommentsAdapter(Context context ,List<String> list) {
	//	  this.list = list;
	//	  inflater = LayoutInflater.from(context);
	//	}	

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		final StatesViewHolder holder;
		if(convertView == null){
			holder = new StatesViewHolder();
			convertView = inflater.inflate(R.layout.comment_user_row,null); 
			holder.profilePic = (ImageView) convertView.findViewById(R.id.comment_user_img);
			holder.userName = (TextView) convertView.findViewById(R.id.comment_userName);
			holder.CommentTxt = (TextView) convertView.findViewById(R.id.comment_userTag);
			holder.comment_delete = (ImageView) convertView.findViewById(R.id.comment_delete);			
			holder.comment = list.get(pos);
			convertView.setTag(holder);
		} else{
			holder = (StatesViewHolder) convertView.getTag();
		}

		//TODO: user image is not loaded
		if(list.get(pos).getMediaCommentProfileImage().trim().length()==0){
			holder.profilePic.setImageResource(R.drawable.profile_pic_dummy);
		}else{
			BitmapUtils.setImages(list.get(pos).getMediaCommentProfileImage(),holder.profilePic, R.drawable.profile_pic_dummy);	
		}

		
		holder.userName.setText(list.get(pos).getMediaCommentFirstName());
		holder.CommentTxt.setText(list.get(pos).getMediaCommentText());
		if(list.get(pos).getMediaCommentUserId().equalsIgnoreCase(userId)){
			holder.comment_delete.setTag(list.get(pos));
			holder.comment_delete.setVisibility(View.VISIBLE);
			holder.comment_delete.setOnClickListener(this);
		}else{
			holder.comment_delete.setVisibility(View.GONE);
		}
		//used for updating list when one item deleted
		list.get(pos).setPos(pos);
		return convertView;
	}

	public class StatesViewHolder{
		public ImageView profilePic;
		public TextView userName;
		public TextView CommentTxt;
		public Comments comment;
		public ImageView comment_delete;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.comment_delete:
			triggerCommentDelete(v);
			break;
		}
	}

	private void triggerCommentDelete(View v) {
		Comments tmpCmt =  (Comments) v.getTag();
		if(tmpCmt!=null){
			serviceToDeleteComment(tmpCmt.getMediaCommentId(),tmpCmt.getPos());
		}
	}
	//*******************************Server communication starts************************************//
	//Delete comment added the logged user
	private void serviceToDeleteComment(String commentId,final int pos){
		final String url = Constants.common_url
				+ context.getString(R.string.delete_event_media_comment);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_MEDIA_ID, commentId));
		nameValuePairs.add(new BasicNameValuePair(
				Constants.KEY_EVENT_CHEER_MEDIA_TYPE, eventMediaType));
		JsonParser.callBackgroundService(url, nameValuePairs,
				new JsonServiceListener() {
			@Override
			public void parseJsonResponse(String jsonResponse) {
				if (jsonResponse != null) {
					Log.v("", "URL: " + url
							+ " delete event-media comment RESPONSE: "
							+ jsonResponse);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(jsonResponse);
						JSONObject resObj = jsonObject
								.getJSONObject(Constants.TAG_RESPONSE);
						String responseStr = resObj
								.getString(Constants.TAG_RESPONSE_INFO);
						if (responseStr != null
								&& responseStr
								.equalsIgnoreCase(Constants.TAG_SUCCESS)) {
							//TODO: display toast -successfully deleted & remove from list
							list.remove(pos);
							
							//Decrease comments count
							if(eventMedia instanceof EventInfo.EventMedia){
								((EventInfo.EventMedia)eventMedia).decCommentsCnt();
							}else if(eventMedia instanceof ProfileMediaInfo){
								((ProfileMediaInfo)eventMedia).decCommentsCnt();
							}
							notifyDataSetChanged();
						} else {
							//TODO: display toast - fail to delete
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					UIHelperUtil
					.showToastMessage("Something going wrong with server");
				}
			}
		});
	}
	
	public void setReverseCommentsList(){
		Collections.reverse(list);
	}
	
	public List<Comments> getCommentsList(){
		return list;
	}
	public void setCommentsList(List<Comments> list){
		this.list = list;
	}
	//*******************************Server communication ends************************************//
}
