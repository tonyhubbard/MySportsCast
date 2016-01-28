package com.mysportsshare.mysportscast.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.adapters.SportsAdapter.StatesViewHolder;
import com.mysportsshare.mysportscast.models.SportModel;

public class CommonAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<String> list;
	
	public CommonAdapter(Context context ,List<String> list) {
	  this.list = list;
	  inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int pos) {
		return list.get(pos);
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
		holder.sports_tv.setText(list.get(pos));
		return convertView;
	}

	class StatesViewHolder{
		public TextView sports_tv;
	}	
}
