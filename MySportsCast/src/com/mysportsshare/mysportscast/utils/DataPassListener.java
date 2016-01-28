package com.mysportsshare.mysportscast.utils;

public interface DataPassListener {
	/*method: passData
	 * Desc: Passes data from current calling fragment to 'toFragment'
	 * Inputs:
	 * 		message: data which is being passed
	 * 		toFragment: the destination fragment
	 * 		
	 * */
	
	public void passData(String message, String toFragment);
}
