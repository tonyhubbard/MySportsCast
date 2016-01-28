package com.mysportsshare.mysportscast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysportsshare.mysportscast.R;

public class CheckTrackFragmentPage extends Fragment{



	public static Fragment newInstance(Context context) {
		CheckTrackFragmentPage f = new CheckTrackFragmentPage();	

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.checktrackfragmentpage, null);		
		return root;
	}
}
