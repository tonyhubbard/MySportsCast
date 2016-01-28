package com.mysportsshare.mysportscast.utils;

import android.util.Log;

public class CustomLog {
	public static boolean isInProduction = false;

	public static void v(String tag, String meesage) {
		if (!isInProduction)
			Log.v(tag, meesage);

	}

	public static void w(String tag, String meesage) {
		if (!isInProduction)
			Log.w(tag, meesage);

	}

	public static void i(String tag, String meesage) {
		if (!isInProduction)
			Log.i(tag, meesage);

	}

	public static void e(String tag, String meesage) {
		if (!isInProduction)
			Log.e(tag, meesage);

	}

	public static void e(String tag, String meesage, Throwable throwable) {
		if (!isInProduction)
			Log.e(tag, meesage, throwable);

	}

	public static void d(String tag, String meesage) {
		if (!isInProduction)
			Log.d(tag, meesage);

	}

	public static void d(String tag, String meesage, Exception e) {
		if (!isInProduction)
			Log.d(tag, meesage, e);

	}

	public static void e(String tag, String meesage, Exception e) {
		if (!isInProduction)
			Log.d(tag, meesage, e);

	}

	public static void w(String tag, String meesage, Exception e) {
		if (!isInProduction)
			Log.w(tag, meesage, e);

	}
}
