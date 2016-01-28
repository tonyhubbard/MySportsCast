package com.mysportsshare.mysportscast.parser;



public abstract class JsonServiceListener {
	
	public void showProgress() {
		
	}
	
	public void hideProgress() {
		
	}
	
	public void navigateToPrevFragment() {

	}
	
	public abstract void parseJsonResponse(String jsonResponse);
}
