package com.mysportsshare.mysportscast.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;
/*
 * Resize the displayed image width & height to look like square 
 * */
public class SquareVideoView extends VideoView {

	public SquareVideoView(final Context context)
    {
        super(context);
    }

    public SquareVideoView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SquareVideoView(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
    {
        final int width = getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh)
    {
        super.onSizeChanged(w, w, oldw, oldh);
    }
}
