package com.howstheweather.animatedicons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.howstheweather.GeneralUtils;
import com.howstheweather.R;

/**
 * Animated loading icon
 */
public class NoPermissionView extends View {

    private static final int POST_INVALIDATE_DELAY = 20;
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 100;
    private static final int MSG_INVALIDATE = 0;
    private static final int INCREMENT = 1;

    private RectF mapRect = new RectF();
    private RectF clippedRect = new RectF();
    private RectF pathRect = new RectF();
    private Bitmap cloudBitmap;
    private Bitmap pathBitmap;
    private Paint paint = new Paint();
    private int layout_width;
    private int layout_height;
    private Path path = new Path();
    private  int clipXEnd = 0;

    Handler handler = new Handler((message -> {
        invalidate();
        return true;
    }));

    public NoPermissionView(Context context) {
        this(context, null, 0);
    }

    public NoPermissionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoPermissionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int[] attrsArray = new int[]{
                android.R.attr.id, // 0
                android.R.attr.background, // 1
                android.R.attr.layout_width, // 2
                android.R.attr.layout_height // 3
        };

        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        layout_width = ta.getDimensionPixelSize(2, (int) GeneralUtils.convertDpToPixel(DEFAULT_WIDTH));
        layout_height = ta.getDimensionPixelSize(3, (int) GeneralUtils.convertDpToPixel(DEFAULT_HEIGHT));

        cloudBitmap = GeneralUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.map,
                (int) GeneralUtils.convertDpToPixel(DEFAULT_WIDTH), (int) GeneralUtils.convertDpToPixel(DEFAULT_HEIGHT));

        pathBitmap = GeneralUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.path,
                (int) GeneralUtils.convertDpToPixel(DEFAULT_WIDTH), (int) GeneralUtils.convertDpToPixel(DEFAULT_HEIGHT));

        mapRect.set(getPaddingLeft(), getPaddingTop(), layout_width - getPaddingRight(), layout_height - getPaddingBottom());
        pathRect.set(getPaddingLeft(), getPaddingTop(), layout_width - getPaddingRight(), layout_height - getPaddingBottom());

        clippedRect.set(getPaddingLeft(), getPaddingTop(), clipXEnd, mapRect.bottom);

        new PathAnimationThread().start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();

        path.moveTo(0, 0);
        path.addRect(clippedRect, Path.Direction.CW);
        path.close();
        canvas.drawBitmap(cloudBitmap, null, mapRect, paint);
        canvas.clipPath(path);
        canvas.drawBitmap(pathBitmap, null, pathRect, paint);
    }

    /**
     * changes the right bounds of clipped rect
     */
    private class PathAnimationThread extends Thread {

        private boolean animate = true;

        @Override
        public void run() {
            super.run();
            while (animate) {
                clipXEnd = clipXEnd + INCREMENT;
                clippedRect.set(getPaddingLeft(), getPaddingTop(), GeneralUtils.convertDpToPixel(clipXEnd), mapRect.bottom);

                if (GeneralUtils.convertDpToPixel(clipXEnd) >= mapRect.right) {
                    animate = false;
                }

                try {
                    Thread.sleep(POST_INVALIDATE_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(MSG_INVALIDATE);
            }
        }
    }

}
