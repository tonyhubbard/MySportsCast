package com.mysportsshare.mysportscast.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchInfo  implements Parcelable {

	String ID;
	String Name;
	String Photo;
	boolean selected;
	public SearchInfo() {
		selected = false; 
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getPhoto() {
		return Photo;
	}
	public void setPhoto(String photo) {
		Photo = photo;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(ID);
		dest.writeString(Name);
		dest.writeString(Photo);
	}	
	
	public static Creator<SearchInfo> CREATOR = new Creator<SearchInfo>() {

		@Override
		public SearchInfo createFromParcel(Parcel source) {
			SearchInfo searchInfo = new SearchInfo();
			searchInfo.ID = source.readString();
			searchInfo.Name = source.readString();
			searchInfo.Photo = source.readString();
			return searchInfo;
		}

		@Override
		public SearchInfo[] newArray(int size) {
			return new SearchInfo[size];
		}

	};
}
