package com.geetest.facelivenessdetection.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.geetest.facelivenessdetection.R;


public class SendView extends RelativeLayout {

    public RelativeLayout backLayout, selectLayout;

    public SendView(Context context) {
        super(context);
        init(context);
    }

    public SendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutParams params = new LayoutParams(getWidthPixels(context), dp2px(180f, context));
        setLayoutParams(params);
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.widget_view_send_btn, null, false);
        layout.setLayoutParams(params);
        backLayout = (RelativeLayout) layout.findViewById(R.id.return_layout);
        selectLayout = (RelativeLayout) layout.findViewById(R.id.select_layout);
        addView(layout);
        setVisibility(GONE);
    }

    public void startAnim() {
        setVisibility(VISIBLE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(backLayout, "translationX", 0, -360),
                ObjectAnimator.ofFloat(selectLayout, "translationX", 0, 360)
        );
        set.setDuration(250).start();
    }

    public void stopAnim() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(backLayout, "translationX", -360, 0),
                ObjectAnimator.ofFloat(selectLayout, "translationX", 360, 0)
        );
        set.setDuration(250).start();
        setVisibility(GONE);
    }

    public int getWidthPixels(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Configuration cf = context.getResources().getConfiguration();
        int ori = cf.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            return displayMetrics.heightPixels;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {// 竖屏
            return displayMetrics.widthPixels;
        }
        return 0;
    }

    public int dp2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
