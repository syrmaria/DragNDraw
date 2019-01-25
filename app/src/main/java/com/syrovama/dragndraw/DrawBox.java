package com.syrovama.dragndraw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class DrawBox extends View {
    public static final String TAG = "DrawBox";
    private Paint mPaint;
    private int mMainColor, mMoveColor;
    private PointF mCenter;
    private float mSizeX, mSizeY;
    private boolean isMoving;
    private Callback mCallback;

    public interface Callback {
        void onPositionChanged(PointF center);
    }

    public void setCallback(Callback listener) {
        mCallback = listener;
    }

    public DrawBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray settings = getContext().obtainStyledAttributes(attrs, R.styleable.DrawBox);
        float default_value = getResources().getDimension(R.dimen.def_value);
        mSizeX = settings.getDimension(R.styleable.DrawBox_x_size, default_value);
        mSizeY = settings.getDimension(R.styleable.DrawBox_y_size, default_value);
        mCenter = new PointF();
        mCenter.x = settings.getDimension(R.styleable.DrawBox_x_start, default_value);
        mCenter.y = settings.getDimension(R.styleable.DrawBox_y_start, default_value);
        mMainColor = settings.getColor(R.styleable.DrawBox_main_color, getResources().getColor(R.color.colorPrimary));
        mMoveColor = settings.getColor(R.styleable.DrawBox_move_color, getResources().getColor(R.color.colorAccent));
        mPaint = new Paint();
        mPaint.setColor(mMainColor);
        settings.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(mCenter.x-mSizeX/2, mCenter.y-mSizeY/2, mCenter.x+mSizeX/2, mCenter.y+mSizeY/2, mPaint);
        mCallback.onPositionChanged(mCenter);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (checkIfInside(event.getX(), event.getY())) {
                    isMoving = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMoving) {
                    mCenter.set(event.getX(),event.getY());
                    mPaint.setColor(mMoveColor);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mPaint.setColor(mMainColor);
                isMoving = false;
                invalidate();
                break;
        }
        return true;
    }

    private boolean checkIfInside(float x, float y) {
        return ((x > mCenter.x-mSizeX/2) && (y > mCenter.y-mSizeY/2) &&
                (x < mCenter.x+mSizeX/2) && (y < mCenter.y+mSizeY/2));
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle state = new Bundle();
        state.putParcelable("superState", super.onSaveInstanceState());
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        state.putFloat("centerX", mCenter.x/metrics.widthPixels);
        state.putFloat("centerY", mCenter.y/metrics.heightPixels);
        return state;
    }

    @Override
    public void onRestoreInstanceState(Parcelable savedState) {
        if (savedState instanceof Bundle) {
            Bundle state = (Bundle) savedState;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            mCenter.x = state.getFloat("centerX") * metrics.widthPixels;
            mCenter.y = state.getFloat("centerY") * metrics.heightPixels;
            savedState = state.getParcelable("superState");
        }
        super.onRestoreInstanceState(savedState);
    }
}