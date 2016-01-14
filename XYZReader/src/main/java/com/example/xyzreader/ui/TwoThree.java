package com.example.xyzreader.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by regardtschindler on 2015/12/21.
 */
public class TwoThree extends ImageView {

    private float ratio = 1;

    public TwoThree(Context context) {
        super(context);
    }

    public TwoThree(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoThree(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int threeTwoHeight = MeasureSpec.getSize(widthMeasureSpec) * (int)ratio;
        int threeTwoHeightSpec = MeasureSpec.makeMeasureSpec(threeTwoHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, threeTwoHeightSpec);
    }

    public void setRatio(String ratio){
        this.ratio = Float.valueOf(ratio);
    }

}
