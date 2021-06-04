package com.automat.CTFREE.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.automat.CTFREE.Functions;

public class BarGraph extends View {

	public BarGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	float value;
	float maxValue;
	
	public void setValue(float v){
		this.value = v;
	}
	
	public void setMaxValue(float v){
		this.maxValue = v;
	}

	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	//this.setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
		//System.out.println("bar graph measure " + MeasureSpec.getMode(widthMeasureSpec) + " " + MeasureSpec.getMode(heightMeasureSpec) + " " + value + " " + getMeasuredWidth() + " " + getMeasuredHeight());
		
    	//this.setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	canvas = Functions.on_render_barGraph(this, canvas, value, maxValue);    	
    }
    
}
