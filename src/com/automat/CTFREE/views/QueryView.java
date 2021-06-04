package com.automat.CTFREE.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.automat.CTFREE.Functions;

public class QueryView extends TextView {
	
	public QueryView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}	
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        canvas = Functions.on_render_queryView(this, canvas);
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent e){    	
    	return true;
    }
	

}
