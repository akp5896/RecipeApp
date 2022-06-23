package com.example.recipeapp.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


// Since ListView has won't work correctly with "wrap_content" property, here is custom list to fix it
public class WrappingListView extends ListView {

    public WrappingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrappingListView(Context context) {
        super(context);
    }

    public WrappingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}

