package com.mysportsshare.mysportscast.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.utils.AdapterCallback;
import com.mysportsshare.mysportscast.utils.BitmapUtils;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.Constants.imgFilterList;
import com.mysportsshare.mysportscast.utils.ImageFilters;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageFiltersAdapter extends BaseAdapter {
	//TODO: clear bitmaps on closing filterImage activity
	imgFilterList[] imgFilters;
	Context context;
	String imgPath;
	ImageFilters imgFiltersContainer;
	Bitmap filterBitmaps[];
	ImageView image;
	AdapterCallback adapterCallback;
	int selectedPos = 0; //Holds current selected filter
	public ImageFiltersAdapter(Context cntxt,final ImageView image,final Constants.imgFilterList[] imgFilters, String imgPath, AdapterCallback adapterCallback) {
		this.context = cntxt;
		this.imgFilters = imgFilters;
		this.imgPath = imgPath;
		this.image = image;
		imgFiltersContainer = new ImageFilters();
		filterBitmaps = new Bitmap[imgFilters.length];
		this.adapterCallback = adapterCallback;

		BitmapUtils.setImagesWithFilter(imgPath, image, R.drawable.profile_pic_dummy, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
			}
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
			}
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap loadedImage) {
				filterBitmaps[0] = loadedImage;
				applyFilterOnImages();
			}
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
			}
		});
	}

	public void setImageViewReference(Context cntxt,final ImageView image){
		this.context = cntxt;
		this.image = image;
	}
	@Override
	public int getCount() {
		return imgFilters.length;
	}

	@Override
	public Object getItem(int position) {
		return imgFilters[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ImageView filterImg;
		TextView filterTxt;
		View filterSelInd;
		LinearLayout image_filter_layout;
		ImageFilterHolder holder;
		if(convertView==null){
			LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.image_filter_item, null);
			image_filter_layout = (LinearLayout) convertView.findViewById(R.id.image_filter_layout);
			filterImg =(ImageView) convertView.findViewById(R.id.image_filter_icon);
			filterTxt =(TextView) convertView.findViewById(R.id.image_filter_title);
			filterSelInd =(View) convertView.findViewById(R.id.image_filter_sel_indicator);

			holder = new ImageFilterHolder();
			holder.setFilterImg(filterImg);
			holder.setFilterTxt(filterTxt);
			holder.setFilterSelInd(filterSelInd);
			holder.setImage_filter_layout(image_filter_layout);
			holder.setPosition(position);
			image_filter_layout.setTag(holder);
		}else{
			holder = (ImageFilterHolder)convertView.getTag();
			image_filter_layout = holder.getImage_filter_layout();
			filterImg =holder.getFilterImg();
			filterTxt =holder.getFilterTxt();
			filterSelInd =holder.getFilterSelInd();
		}

		filterTxt.setText(imgFilters[position].toString());
		filterImg.setImageBitmap(filterBitmaps[position]);
		if(selectedPos == position){
			//To highlight selected filter using indication
			image.setImageBitmap(filterBitmaps[position]);
			filterSelInd.setBackgroundResource(R.color.com_facebook_blue);
		}else{
			filterSelInd.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}

	public void selectImageFiilter(int position){
		selectedPos = position;
		notifyDataSetChanged();
	}

	private void applyFilterOnImages(){
		for( int position=1;position<filterBitmaps.length;position++){
			if(filterBitmaps[position]==null){
				if(filterBitmaps[0]!=null){
					switch(imgFilters[position]){
					//					case NORMAL:
					//						filterBitmaps[i] = loadedImage;
					//						break;
					case MAYFAIR: //Applying grey effect
						//						filterBitmaps[position] = BitmapFactory.decodeResource(context.getResources(),R.drawable.overlay_pink);
						filterBitmaps[position] = ImageFilters.overlayWithGradient(context, filterBitmaps[0],  R.drawable.overlay_pink);
						//						filterBitmaps[position] = imgFiltersContainer.applyGreyscaleEffect(filterBitmaps[0]);	
						break;
					case INKWELL: //Applying BLUE effect
						//						filterBitmaps[position] = BitmapFactory.decodeResource(context.getResources(),R.drawable.overlay_blue);
						filterBitmaps[position] =  ImageFilters.overlayWithGradient(context, filterBitmaps[0],  R.drawable.overlay_blue);
						//						filterBitmaps[position] = imgFiltersContainer.applySnowEffect(filterBitmaps[0]);	
						break;
					case KELLY: //Applying gamma effect
						filterBitmaps[position] =  ImageFilters.overlayWithGradient(context, filterBitmaps[0],  R.drawable.overlay_green);
						//						filterBitmaps[position] = imgFiltersContainer.applyGammaEffect(filterBitmaps[0],1.8, 1.8, 1.8);	
						break;
					case PEARL:
						//Applying color effect
						filterBitmaps[position] =  ImageFilters.overlayWithGradient(context, filterBitmaps[0],  R.drawable.overlay_grey);
						//						filterBitmaps[position] = imgFiltersContainer.applyColorFilterEffect(filterBitmaps[0],1.8, 1.8, 1.8);
						break;
					case AMBER:
						filterBitmaps[position] =  ImageFilters.overlayWithGradient(context, filterBitmaps[0],  R.drawable.overlay_orange);
						//Applying contrast effect
						//						filterBitmaps[position] = imgFiltersContainer.applyContrastEffect(filterBitmaps[0],50);
						break;
					case ALICE:
						filterBitmaps[position] =  ImageFilters.overlayWithGradient(context, filterBitmaps[0],  R.drawable.overlay_purple);
						//Applying bright effect
						//						filterBitmaps[position] = imgFiltersContainer.applyBrightnessEffect(filterBitmaps[0],50);
						break;
					case ROWAN:
						filterBitmaps[position] =  ImageFilters.overlayWithGradient(context, filterBitmaps[0],  R.drawable.overlay_red);
						//Applying sharp effect
						//						filterBitmaps[position] = imgFiltersContainer.applySharpenEffect(filterBitmaps[0],10);
						break;
					case DAISY:
						filterBitmaps[position] =  ImageFilters.overlayWithGradient(context, filterBitmaps[0],  R.drawable.overlay_yellow);
						//Applying smooth effect
						//						filterBitmaps[position] = imgFiltersContainer.applySmoothEffect(filterBitmaps[0],10);
						break;
					case GAUSSIAN:
						//Applying gaussian effect
						filterBitmaps[position] = imgFiltersContainer.applyGaussianBlurEffect(filterBitmaps[0]);
						break;
					case EMBOSS:
						//Applying emboss effect
						filterBitmaps[position] = imgFiltersContainer.applyEmbossEffect(filterBitmaps[0]);
						break;
					case ENGRAVE:
						//Applying engrave effect
						filterBitmaps[position] = imgFiltersContainer.applyEngraveEffect(filterBitmaps[0]);
						break;
					case TINT:
						//Applying tint effect
						filterBitmaps[position] = imgFiltersContainer.applyTintEffect(filterBitmaps[0],30);
						break;
					case FLEA:
						//Applying flea effect
						filterBitmaps[position] = imgFiltersContainer.applyFleaEffect(filterBitmaps[0]);
						break;
					case BLACK:
						//Applying flea effect
						filterBitmaps[position] = imgFiltersContainer.applyBlackFilter(filterBitmaps[0]);
						break;
						//			default:
						//				filterBitmaps[position] =filterBitmaps[0];
					}
				}
			}
		}
		//Update UI
		adapterCallback.onStatusReturn("");	
		notifyDataSetChanged();
	}

	public void clearBitmaps(){
		try {
			if(filterBitmaps!=null){
				for (int i=0;i<filterBitmaps.length;i++){
					if(filterBitmaps[i]!=null){
						filterBitmaps[i].recycle();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public class ImageFilterHolder{
		ImageView filterImg;
		TextView filterTxt;
		View filterSelInd;
		LinearLayout image_filter_layout;
		int position;
		public ImageView getFilterImg() {
			return filterImg;
		}
		public void setFilterImg(ImageView filterImg) {
			this.filterImg = filterImg;
		}
		public TextView getFilterTxt() {
			return filterTxt;
		}
		public void setFilterTxt(TextView filterTxt) {
			this.filterTxt = filterTxt;
		}
		public View getFilterSelInd() {
			return filterSelInd;
		}
		public void setFilterSelInd(View filterSelInd) {
			this.filterSelInd = filterSelInd;
		}
		public LinearLayout getImage_filter_layout() {
			return image_filter_layout;
		}
		public void setImage_filter_layout(LinearLayout image_filter_layout) {
			this.image_filter_layout = image_filter_layout;
		}
		public int getPosition() {
			return position;
		}
		public void setPosition(int position) {
			this.position = position;
		}	  
	}
}
