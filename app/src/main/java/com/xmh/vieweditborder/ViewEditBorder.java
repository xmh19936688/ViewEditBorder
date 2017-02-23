package com.xmh.vieweditborder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by void on 2017/2/20.
 */

public class ViewEditBorder extends RelativeLayout {

    private View mRootView;
    private View mAnchor;
    private int mExtraWidth;
    private int mExtraHeight;

    public ViewEditBorder(Context context) {
        this(context, null);
    }

    public ViewEditBorder(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewEditBorder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRootView = LayoutInflater.from(context).inflate(R.layout.layout_editable_border_controller, this, true);

        findViewById(R.id.iv1).setOnTouchListener(new OnLeftTopTouchListener());
        findViewById(R.id.iv2).setOnTouchListener(new OnRightTopTouchListener());
        findViewById(R.id.iv3).setOnTouchListener(new OnLeftBottomTouchListener());
        findViewById(R.id.iv4).setOnTouchListener(new OnRightBottomTouchListener());
        setOnTouchListener(new OnRootTouchListener());
    }

    public void show(View anchor) {
        mAnchor = anchor;
        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mExtraWidth = findViewById(R.id.iv1).getMeasuredWidth();
        mExtraHeight = findViewById(R.id.iv1).getMeasuredWidth();
        MarginLayoutParams anchorLayoutParams = (MarginLayoutParams) anchor.getLayoutParams();
        LayoutParams layoutParams = new LayoutParams(anchor.getMeasuredWidth() + mExtraWidth, anchor.getMeasuredHeight() + mExtraHeight);
        layoutParams.leftMargin = anchorLayoutParams.leftMargin - mExtraWidth / 2;
        layoutParams.topMargin = anchorLayoutParams.topMargin - mExtraWidth / 2;
        setLayoutParams(layoutParams);

        //根据parent判断是否addView
        if (getParent() == null) ((ViewGroup) anchor.getParent()).addView(this);
        if (getParent() != anchor.getParent()) {
            ((ViewGroup) getParent()).removeView(this);
            ((ViewGroup) anchor.getParent()).addView(this);
        }
    }

    /**
     * 位置拖拽监听
     */
    class OnRootTouchListener implements OnTouchListener {
        float originX;
        float originY;

        int originLeftMargin;
        int originTopMargin;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    originX = event.getRawX();
                    originY = event.getRawY();
                    MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
                    originLeftMargin = layoutParams.leftMargin;
                    originTopMargin = layoutParams.topMargin;
                    return true;
                }
                case MotionEvent.ACTION_MOVE: {
                    float deltaX = event.getRawX() - originX;
                    float deltaY = event.getRawY() - originY;

                    // 忽略小范围移动
                    if (Math.abs(deltaX) < 5 && Math.abs(deltaY) < 5) {
                        return false;
                    }

                    //refresh view layout
                    MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
                    layoutParams.leftMargin = (int) (originLeftMargin + deltaX);
                    layoutParams.topMargin = (int) (originTopMargin + deltaY);
                    view.setLayoutParams(layoutParams);

                    //refresh anchor layout
                    MarginLayoutParams anchorLayoutParams = (MarginLayoutParams) mAnchor.getLayoutParams();
                    anchorLayoutParams.leftMargin = layoutParams.leftMargin + mExtraWidth / 2;
                    anchorLayoutParams.topMargin = layoutParams.topMargin + mExtraHeight / 2;
                    mAnchor.setLayoutParams(anchorLayoutParams);
                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 左上角点击监听
     */
    class OnLeftTopTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            return false;
        }
    }

    /**
     * 右上角点击监听
     */
    class OnRightTopTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            return false;

        }
    }

    /**
     * 左下角点击监听
     */
    class OnLeftBottomTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            return false;

        }
    }

    /**
     * 右下角点击监听
     */
    class OnRightBottomTouchListener implements OnTouchListener {

        float originX;
        float originY;

        int originWidth;
        int originHeight;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    originX = event.getRawX();
                    originY = event.getRawY();
                    originWidth = mAnchor.getMeasuredWidth();
                    originHeight = mAnchor.getMeasuredHeight();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = event.getRawX() - originX;
                    float deltaY = event.getRawY() - originY;

                    // 忽略小范围移动
                    if (Math.abs(deltaX) < 5 && Math.abs(deltaY) < 5) {
                        return false;
                    }

                    //refresh anchor layout
                    ViewGroup.LayoutParams anchorLayoutParams = mAnchor.getLayoutParams();
                    anchorLayoutParams.width = (int) (originWidth + deltaX);
                    anchorLayoutParams.height = (int) (originHeight + deltaY);
                    mAnchor.setLayoutParams(anchorLayoutParams);

                    //refresh controller layout
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.width = anchorLayoutParams.width + mExtraWidth;
                    layoutParams.height = anchorLayoutParams.height + mExtraHeight;
                    setLayoutParams(layoutParams);

                    return true;
                case MotionEvent.ACTION_UP:
                    return true;
            }
            return false;
        }
    }

}
