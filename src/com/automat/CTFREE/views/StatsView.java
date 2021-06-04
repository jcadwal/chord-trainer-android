package com.automat.CTFREE.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.automat.CTFREE.Functions;

public class StatsView extends View{

	public StatsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {		
		canvas = Functions.on_render_statsView(this, canvas);		
	}

	@Override
	public boolean onTouchEvent(MotionEvent e){    	
		return true;
	}


}



