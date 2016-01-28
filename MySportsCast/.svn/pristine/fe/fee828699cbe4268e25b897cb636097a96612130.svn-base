package com.mysportsshare.mysportscast.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.models.SportModel;

public class SportsAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<SportModel> listSports;

	public SportsAdapter(Context context ,List<SportModel> listSportsInfos ) {
		this.listSports = listSportsInfos;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listSports.size();
	}

	@Override
	public SportModel getItem(int pos) {
		return listSports.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		final StatesViewHolder holder;
		if(convertView == null){
			holder = new StatesViewHolder();
			convertView = inflater.inflate(R.layout.sports_row,null);  
			holder.sports_tv = (TextView) convertView.findViewById(R.id.sports_tv);
			convertView.setTag(holder);
		} else{
			holder = (StatesViewHolder) convertView.getTag();
		}
		if(holder!=null){
			if(holder.sports_tv!=null){
				if(listSports!=null){
					if(pos<listSports.size()){
						holder.sports_tv.setText(listSports.get(pos).getSportName());
					}
				}		
			}
		}		
		return convertView;
	}

	class StatesViewHolder{
		public TextView sports_tv;
	}	
}
