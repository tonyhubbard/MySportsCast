package com.mysportsshare.mysportscast.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.models.SearchInfo;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.CustomLog;

public class SearchViewPagerEventPeopleAdapter extends BaseAdapter {

	LayoutInflater inflater;
	Context context;
	List<SearchInfo> searchList;
	String searchType;
	public SearchViewPagerEventPeopleAdapter(Context activity,String searchType, List<SearchInfo> searchList){
		this.context = activity;
		this.searchList = searchList;
		this.searchType = searchType;
		this.inflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(activity);
	}

	@Override
	public int getCount() {
		if(searchList!=null){
			return searchList.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int pos) {
		if(searchList!=null){
			return searchList.get(pos);
		}else{
			return 0;
		}		
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View contentView, ViewGroup arg2) {
		SearchItemHandler tmpHandler;
		if(contentView == null){
			contentView = inflater.inflate(R.layout.search_viewpager_event_people_row, null);
			tmpHandler= new SearchItemHandler();
			tmpHandler.Name = (TextView) contentView.findViewById(R.id.search_item_text);
			tmpHandler.photo = (ImageView) contentView.findViewById(R.id.search_item_img);			 
			tmpHandler.layout = (LinearLayout) contentView.findViewById(R.id.search_fragment_row_item_layout);
			contentView.setTag(tmpHandler);
		}else{
			tmpHandler = (SearchItemHandler) contentView.getTag();
		}

		tmpHandler.searchData = searchList.get(pos);
		tmpHandler.ID = tmpHandler.searchData.getID();
		tmpHandler.Name.setText(tmpHandler.searchData.getName());
		if(tmpHandler.searchData.isSelected()){
			CustomLog.i("selected", "selected: " + tmpHandler.searchData.getName());
			AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F); 
			alpha.setDuration(0); 
			alpha.setFillAfter(true); 
			//tmpHandler.photo.startAnimation(alpha);
			tmpHandler.photo.setBackgroundColor(context.getResources().getColor(R.color.com_facebook_blue));
//			tmpHandler.layout.setBackgroundResource(context.getResources().getColor(R.color.com_facebook_blue));
		}else{
			CustomLog.i("selected", "Not selected: " + tmpHandler.searchData.getName());
			AlphaAnimation alpha = new AlphaAnimation(1.0F, 1.0F); 
			alpha.setDuration(0); 
			alpha.setFillAfter(true); 
			//tmpHandler.photo.startAnimation(alpha);
			tmpHandler.photo.setBackgroundColor(Color.WHITE);
//			tmpHandler.layout.setBackgroundResource(Color.WHITE);
		}

		if(searchType.equalsIgnoreCase(Constants.search_flag_event)){
			if(searchList.get(pos).getPhoto() != null){
				BitmapUtils.setImages(searchList.get(pos).getPhoto(), tmpHandler.photo,R.drawable.default_event_pic);
			}else{
				tmpHandler.photo.setImageResource(R.drawable.default_event_pic);
			}

		}else{
			Log.d("","else Koti");
			BitmapUtils.setImages(searchList.get(pos).getPhoto(), tmpHandler.photo,R.drawable.profile_pic_dummy);
		}

		return contentView;
	}

	public class SearchItemHandler{
		String ID;
		SearchInfo searchData;
		TextView Name;
		ImageView photo;
		LinearLayout layout;
		public SearchInfo getSearchData() {
			return searchData;
		}
		public void setSearchData(SearchInfo searchData) {
			this.searchData = searchData;
		}
		public ImageView getPhoto() {
			return photo;
		}
		public void setPhoto(ImageView photo) {
			this.photo = photo;
		}	

		public void setLayout(LinearLayout layout) {
			this.layout = layout;
		}

		public LinearLayout getLayout() {
			return layout;
		}
	}

	public void updateUsers(ArrayList<SearchInfo> updatedList) {
		this.searchList = updatedList;
		this.notifyDataSetChanged();
	}

	public ArrayList<SearchInfo> getList() {
		return (ArrayList<SearchInfo>) searchList;
	}

	public List<SearchInfo> getPrevList() {
		return searchList;
	}

	public void updateList(List<SearchInfo> searchList) {
		this.searchList = searchList;
		notifyDataSetChanged();
	}
}
