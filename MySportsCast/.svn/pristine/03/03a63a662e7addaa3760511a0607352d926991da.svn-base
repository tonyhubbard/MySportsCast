package com.mysportsshare.mysportscast.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.mysportsshare.mysportscast.R;

public class TermAndConditionsActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms_and_conditions);
		
		init();
	}

	private void init() {
		WebView wv = (WebView)findViewById(R.id.terms_and_conditions_webView);
		wv.loadUrl("file:///android_asset/terms_and_conditions.html");
		
	}

}
