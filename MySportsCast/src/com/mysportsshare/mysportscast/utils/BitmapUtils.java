package com.mysportsshare.mysportscast.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

/**
 * This class contains all the methods related to bitmap manupulations
 * 
 * @author Koti
 * 
 */
public class BitmapUtils {

	// set the images with image URL.
	public static void setImages(String url, ImageView imgView,
			int showImgOnLoadFail) {
		// Log.d("", "Image URl: " + url);

		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).resetViewBeforeLoading(true)
				.showStubImage(showImgOnLoadFail)
				.showImageOnFail(showImgOnLoadFail)
				.bitmapConfig(Config.RGB_565).build();
		imageLoader.displayImage(url, imgView, options);
	}

	public static void setImagesWithListener(String url, ImageView imgView,
			int showImgOnLoadFail, ImageLoadingListener listener) {
		// Log.d("", "Image URl: " + url);

		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).resetViewBeforeLoading(true)
				.showStubImage(showImgOnLoadFail)
				.showImageOnFail(showImgOnLoadFail)
				.bitmapConfig(Config.RGB_565).build();
		imageLoader.displayImage(url, imgView, options, listener);
	}

	// set the images with image URL.
	public static void setImagesWithFilter(String url, ImageView imgView,
			int showImgOnLoadFail, ImageLoadingListener imgLoadingListener) {
		// Log.d("", "Image URl: " + url);

		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).resetViewBeforeLoading(true)
				.showStubImage(showImgOnLoadFail)
				.showImageOnFail(showImgOnLoadFail).build();
		imageLoader.displayImage(url, imgView, options, imgLoadingListener);
	}

	public static void setImagesViaPicaso(String url, ImageView imgView,
			int showImgOnLoadFail, Context context) {
		Picasso.with(context).load(url).placeholder(showImgOnLoadFail).fit()
				.centerCrop().into(imgView);
		/*
		 * Picasso.with(context).load(url).fit().centerCrop() .into(imgView);
		 */
	}

	// downloading image from its http path
	public static Bitmap downloadImage(String path) {
		InputStream inputStream;
		Log.v("", "Image Path: " + path);
		BufferedInputStream bufferedInputStream;
		try {
			URL url = new URL(path);
			inputStream = url.openStream();
			bufferedInputStream = new BufferedInputStream(inputStream);
			Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
			if (inputStream != null) {
				inputStream.close();
			}
			if (bufferedInputStream != null) {
				bufferedInputStream.close();
			}
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Log.v("", "bitmap Util; download image: " + path);
		}
		return null;
	}
}
