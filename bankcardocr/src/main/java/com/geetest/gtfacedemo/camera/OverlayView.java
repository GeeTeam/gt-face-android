package com.geetest.gtfacedemo.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class OverlayView extends View {

    private static final int CARD_WIDTH = 8;
    private static final int CARD_HEIGHT = 5;

    private static final float GUIDE_FONT_SIZE = 20.0f;

    private static final int GUIDE_STROKE_WIDTH = 4;

    private boolean mIsVerticalCard = false;
    private float mScale = 1;

    private Rect mGuide;
    private Paint mGuidePaint = null;
    private Paint mBackgroundPaint = null;
    private Paint mTextPaint = null;
    private Path mLockedBackgroundPath;

    public OverlayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        // designed for an hdpi screen (density = 1.5);
        mScale = getResources().getDisplayMetrics().density / 1.5f;

        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                mLockedBackgroundPath = new Path();
                mLockedBackgroundPath.addRect(new RectF(new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight())),
                        Path.Direction.CW);
                mGuide = getGuideFrame(getMeasuredWidth(), getMeasuredHeight());
                mLockedBackgroundPath.addRect(new RectF(mGuide), Path.Direction.CCW);
            }
        });
    }

    public void setIsVerticalCard(boolean isVerticalCard) {
        mIsVerticalCard = isVerticalCard;
    }

    public Rect getCardRect() {
        return mGuide;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();

        // draw lock shadow.
        canvas.drawPath(mLockedBackgroundPath, getBackgroundPaint());

        // Draw guide lines
        int tickLength;

        if (!mIsVerticalCard) {
            tickLength = (mGuide.bottom - mGuide.top) / 8;
        } else {
            tickLength = (mGuide.right - mGuide.left) / 8;
        }

        // top left
        Rect rect = guideStrokeRect(mGuide.left, mGuide.top, mGuide.left + tickLength, mGuide.top);
        canvas.drawRect(rect, getGuidePaint());
        canvas.drawRect(guideStrokeRect(mGuide.left, mGuide.top, mGuide.left, mGuide.top + tickLength),
                getGuidePaint());

        // top right
        canvas.drawRect(guideStrokeRect(mGuide.right, mGuide.top, mGuide.right - tickLength, mGuide.top),
                getGuidePaint());
        canvas.drawRect(guideStrokeRect(mGuide.right, mGuide.top, mGuide.right, mGuide.top + tickLength),
                getGuidePaint());

        // bottom left
        canvas.drawRect(guideStrokeRect(mGuide.left, mGuide.bottom, mGuide.left + tickLength, mGuide.bottom),
                getGuidePaint());
        canvas.drawRect(guideStrokeRect(mGuide.left, mGuide.bottom, mGuide.left, mGuide.bottom - tickLength),
                getGuidePaint());

        // bottom right guide
        canvas.drawRect(guideStrokeRect(mGuide.right, mGuide.bottom, mGuide.right - tickLength, mGuide.bottom),
                getGuidePaint());
        canvas.drawRect(guideStrokeRect(mGuide.right, mGuide.bottom, mGuide.right, mGuide.bottom - tickLength),
                getGuidePaint());

        // Translate and rotate text
        float guideFontSize = GUIDE_FONT_SIZE * mScale;
        canvas.translate(mGuide.left + mGuide.width() / 2, mGuide.top - mGuide.height() / 10);
        if (mTextId > 0) {
            canvas.drawText(getResources().getString(mTextId), 0, (guideFontSize / 2 - 3), getTextPaint());
        }
        // canvas restore
        canvas.restore();
    }

    private int mTextId = -1;
    public void setText(int id, int color) {
        mTextId = id;
        getTextPaint().setColor(color);
        invalidate();
    }
    // Drawing methods
    private Rect guideStrokeRect(int x1, int y1, int x2, int y2) {
        Rect r;
        int t2 = (int) (GUIDE_STROKE_WIDTH / 2 * mScale);
        r = new Rect();

        r.left = Math.min(x1, x2) - t2;
        r.right = Math.max(x1, x2) + t2;

        r.top = Math.min(y1, y2) - t2;
        r.bottom = Math.max(y1, y2) + t2;

        return r;
    }

    private Rect getGuideFrame(int previewWidth, int previewHeight) {
        if (previewWidth == 0 || previewHeight == 0) {
            return null;
        }

        int targetWidth;
        int targetHeight;
        if (mIsVerticalCard) {
            targetHeight = previewHeight / 4 * 3;
            targetWidth = targetHeight / CARD_WIDTH * CARD_HEIGHT;
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                targetWidth = (int) (previewWidth * 0.8);
                targetHeight = targetWidth / CARD_WIDTH * CARD_HEIGHT;
            } else {
                targetHeight = (int) (previewHeight * 0.8);
                targetWidth = targetHeight / CARD_HEIGHT * CARD_WIDTH;
            }
        }
        int left = (previewWidth - targetWidth) / 2;
        int top = (previewHeight - targetHeight) / 2;
        return new Rect(left, top, left + targetWidth, top + targetHeight);
    }

    private Paint getBackgroundPaint() {
        if (mBackgroundPaint != null) {
            return mBackgroundPaint;
        }
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.clearShadowLayer();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(0xbb000000); // 75% black
        mBackgroundPaint.setAlpha(200);//set BackGround alpha, range of value 0~255
        return mBackgroundPaint;
    }

    private Paint getGuidePaint() {
        if (mGuidePaint != null) {
            return mGuidePaint;
        }
        mGuidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGuidePaint.clearShadowLayer();
        mGuidePaint.setStyle(Paint.Style.FILL);
        mGuidePaint.setColor(getResources().getColor(android.R.color.white));
        return mGuidePaint;
    }

    private Paint getTextPaint() {
        if (mTextPaint != null) {
            return mTextPaint;
        }
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.clearShadowLayer();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        mTextPaint.setAntiAlias(true);
        float[] black = {0f, 0f, 0f};
        mTextPaint.setShadowLayer(1.5f, 0.5f, 0f, Color.HSVToColor(200, black));
        mTextPaint.setTextAlign(Align.CENTER);
        mTextPaint.setTextSize(GUIDE_FONT_SIZE * mScale);
        int textColor = Color.WHITE;
        mTextPaint.setColor(textColor);
        return mTextPaint;
    }
}