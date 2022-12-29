package com.litmethod.android.utlis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.litmethod.android.R;
import com.litmethod.android.ui.WorkoutHistory.WorkoutProgressModel;

import java.util.ArrayList;

public class CustomProgressBar extends androidx.appcompat.widget.AppCompatSeekBar {

    private ArrayList<WorkoutProgressModel> mProgressItemsList;

    public CustomProgressBar(Context context) {
        super(context);
        mProgressItemsList = new ArrayList<WorkoutProgressModel>();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initData(ArrayList<WorkoutProgressModel> progressItemsList) {
        this.mProgressItemsList = progressItemsList;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        if (mProgressItemsList != null) {
            if (mProgressItemsList.size() > 0) {
                int progressBarWidth = getWidth();
                int progressBarHeight = getHeight();
                int thumboffset = getThumbOffset();
                int lastProgressX = 0;
                int progressItemWidth, progressItemRight, progressGapParentage, progressItemWidthNew;
                for (int i = 0; i < mProgressItemsList.size(); i++) {
                    if (i == 0) {
                        lastProgressX = mProgressItemsList.get(i).getStartingValue();
                    }
                    WorkoutProgressModel progressItem = mProgressItemsList.get(i);
                    Paint progressPaint = new Paint();
                    progressPaint.setColor(getResources().getColor(R.color.red));

                    progressItemWidth = (int) (progressItem.getProgressItemPercentage()
                            * progressBarWidth / 100);

                    progressItemRight = lastProgressX + progressItemWidth;

                    drawRoundRect(lastProgressX, thumboffset / 2, progressItemRight,
                            progressBarHeight - thumboffset / 2, progressPaint, canvas);

                    if (i < mProgressItemsList.size() - 1) {
                        progressGapParentage = mProgressItemsList.get(i + 1).getStartingValue() - mProgressItemsList.get(i).getEndingValue();
                        progressItemWidthNew = (int) (progressGapParentage
                                * progressBarWidth / 100);

                        lastProgressX = progressItemRight + progressItemWidthNew;
                    } else {
                        lastProgressX = progressItemRight;
                    }
                }
                super.onDraw(canvas);
            }
        }
    }

    private void drawRoundRect(float left, float top, float right, float bottom, Paint onlinePaint, Canvas canvas) {
        RectF progressRect = new RectF();
        progressRect.set(left, top, right, bottom);

        Path path = new Path();
        path.reset();
        float[] corners = new float[]{
                15, 15,        // Top, left in px
                15, 15,        // Top, right in px
                15, 15,          // Bottom, right in px
                15, 15           // Bottom,left in px
        };
        path.addRoundRect(progressRect, corners, Path.Direction.CW);
        canvas.drawPath(path, onlinePaint);
    }

    public void clearArrayList(){
        if (mProgressItemsList != null) {
            mProgressItemsList.clear();
        }
    }
}
