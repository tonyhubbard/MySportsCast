package com.mysportsshare.mysportscast.adapters;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.adapters.EventsAdapter.VehicleViewHolder;
import com.mysportsshare.mysportscast.utils.Utils;

public class MenuItemListAdapter extends BaseAdapter{
	//objects
	private Context context;
	private List<HashMap<String, String>> dataList;
	private LayoutInflater inflater;

	public MenuItemListAdapter(Context con,List<HashMap<String, String>> dL){
		this.context = con;
		this.dataList = dL;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {		
		return dataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return dataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		HashMap<String,String> current = dataList.get(position);		
		VehicleViewHolder holder;
		if(convertView == null){
			holder = new VehicleViewHolder();
			convertView = inflater.inflate(R.layout.drawer_layout,null);  
			holder.menuTitle = (TextView) convertView.findViewById(R.id.menuItem);
			
			convertView.setTag(holder);
		} else{
			holder = (VehicleViewHolder) convertView.getTag();
		}

		holder.menuTitle.setText(current.get("menuItemName"));
		return convertView;
	}

	class VehicleViewHolder{
		public TextView menuTitle;
	}

}
