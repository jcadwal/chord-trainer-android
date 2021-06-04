package com.automat.CTFREE.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.automat.CTFREE.Functions;

public class SelectorView extends View{
	
	public SelectorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}	
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        //canvas = Functions.render_keyboard(canvas, this);
    	canvas = Functions.on_render_selectorView(this, canvas);    	
    }
    
        
    @Override
	public boolean onTouchEvent(MotionEvent e){
    	
    	if(e.getAction() == MotionEvent.ACTION_DOWN){
    		Functions.on_selector_down(this);
    	}
    	if(e.getAction() == MotionEvent.ACTION_UP){
    		Functions.on_selector_up(this);
    	}
    	
    	return true;
    }

}
