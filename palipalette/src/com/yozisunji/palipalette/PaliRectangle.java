package com.yozisunji.palipalette;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class PaliRectangle extends PaliObject {;

	PaliRectangle (RectF r)
	{
		this.rect = new RectF(r);
		this.left = rect.left;
		this.top = rect.top;
		this.right = rect.right;
		this.bottom = rect.bottom;
	}
	PaliRectangle(String tag, int scolor, int fcolor)
	{
		super(tag,scolor,fcolor);
	}
	
	PaliRectangle(float left, float top, float right, float bottom)
	{
		s_paint = new Paint();        
        s_paint.setAntiAlias(true);        
        s_paint.setStyle(Paint.Style.STROKE);
        s_paint.setColor(PaliCanvas.strokeColor);
        s_paint.setAlpha(PaliCanvas.alpha);
        s_paint.setStrokeWidth(PaliCanvas.strokeWidth);
        
        f_paint = new Paint();        
        f_paint.setAntiAlias(true);
        f_paint.setStyle(Paint.Style.FILL);
        f_paint.setColor(PaliCanvas.fillColor);
        f_paint.setAlpha(PaliCanvas.alpha);  
        
        this.type = PaliCanvas.TOOL_RECTANGLE;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		this.rect = new RectF(left, top, right, bottom);
		
		tagSet();
	}
	
	PaliRectangle(String tag, float left, float top, float right, float bottom)
	{
		s_paint = new Paint();        
        s_paint.setAntiAlias(true);        
        s_paint.setStyle(Paint.Style.STROKE);
        s_paint.setColor(PaliCanvas.strokeColor);
        s_paint.setAlpha(PaliCanvas.alpha);
        s_paint.setStrokeWidth(PaliCanvas.strokeWidth);
        
        f_paint = new Paint();        
        f_paint.setAntiAlias(true);
        f_paint.setStyle(Paint.Style.FILL);
        f_paint.setColor(PaliCanvas.fillColor);
        f_paint.setAlpha(PaliCanvas.alpha);  
        
		svgtag = tag;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		this.rect = new RectF(left, top, right, bottom);
	}
	
	PaliRectangle(float left, float top, float right, float bottom, float theta, Paint s_p, Paint f_p)
	{       
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.theta = theta;		
		this.rect = new RectF(left, top, right, bottom);
		s_paint = new Paint(s_p);       
        f_paint = new Paint(f_p);
        tagSet();
	}
	
	PaliRectangle(String tag, float left, float top, float right, float bottom, int scolor, int fcolor)
	{
		svgtag = tag;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		s_paint = new Paint();        
        s_paint.setAntiAlias(true);        
        s_paint.setStyle(Paint.Style.STROKE);
        s_paint.setColor(scolor);
        
        f_paint = new Paint();        
        f_paint.setAntiAlias(true);
        f_paint.setStyle(Paint.Style.FILL);
        f_paint.setColor(fcolor);
		
		this.rect = new RectF(left, top, right, bottom);
	}
	
	public void drawObject(Canvas c) {
		this.type = PaliCanvas.TOOL_RECTANGLE;
		this.rotRect = rotateRect(this.rect, this.theta);
		c.save();
		c.rotate(theta, this.rect.centerX(), this.rect.centerY());
		c.drawRect(left, top, right, bottom, s_paint);
		c.drawRect(left, top, right, bottom, f_paint);
		c.restore();
	}
	public void Move(float dx, float dy)
	{
		this.left += dx;
		this.right += dx;
		this.top += dy;
		this.bottom += dy;
		this.rect.left += dx; this.rect.right += dx; this.rect.top += dy; this.rect.bottom += dy;	
		
		tagSet();
	}
	public void Scale(float dx, float dy)
	{
		if(dx>0) {
			this.right += dx/2;			
			this.rect.right += dx/2; 
		}
		else {
			this.right += dx*2;			
			this.rect.right += dx*2; 
		}
		
		if(dy>0) {
			this.bottom += dy/2;
			this.rect.bottom += dy/2;
		}
		else {
			this.bottom += dy*2;
			this.rect.bottom += dy*2;
		}		
		
		tagSet();
	}
}
