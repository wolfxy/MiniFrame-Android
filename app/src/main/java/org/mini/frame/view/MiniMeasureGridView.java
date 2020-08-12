package org.mini.frame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.GridView;

import org.mini.frame.uitools.MiniDisplayUtil;

/**
 * Created by gassion on 15/4/15.
 */
public class MiniMeasureGridView extends GridView {

    private Context context;

    public MiniMeasureGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MiniMeasureGridView(Context context) {
        super(context);
        this.context = context;
    }

    public MiniMeasureGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int childCount = getAdapter().getCount();

        int newCount;

        if (childCount == 2 || childCount == 4) {
            int width =  (screenWidth  - MiniDisplayUtil.DP_2_PX(24, context))/3;
            newCount = width * 2;
            setNumColumns(2);
        } else {
            int width = (screenWidth  - MiniDisplayUtil.DP_2_PX(24, context))/3;
            newCount = width * 3;
            setNumColumns(3);
        }

        setHorizontalSpacing(MiniDisplayUtil.DP_2_PX(3, context));
        setVerticalSpacing(MiniDisplayUtil.DP_2_PX(3, context));

        int expandSpecWidth = MeasureSpec.makeMeasureSpec(newCount,
                MeasureSpec.AT_MOST);
        int expandSpecHeight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(expandSpecWidth, expandSpecHeight);

    }

}