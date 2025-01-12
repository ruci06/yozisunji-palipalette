package com.yozisunji.palipalette;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class PaliCircle extends PaliObject {

	PaliCircle(float x, float y, float r)
	{
		super();
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
        
        this.type = PaliCanvas.TOOL_CIRCLE;
		this.x = x;
		this.y = y;
		this.r = r;
		this.rect = new RectF(x-r, y-r, x+r, y+r);
		tagSet();
	}
	
	PaliCircle(float x, float y, float r, int strokeColor, int fillColor, float strokeWidth, float strokeOpacity, float fillOpacity)
	{
		super();
		s_paint = new Paint();        
        s_paint.setAntiAlias(true);        
        s_paint.setStyle(Paint.Style.STROKE);
        s_paint.setColor(strokeColor);
        s_paint.setAlpha((int)strokeOpacity);
        s_paint.setStrokeWidth(strokeWidth);
        
        f_paint = new Paint();        
        f_paint.setAntiAlias(true);
        f_paint.setStyle(Paint.Style.FILL);
        f_paint.setColor(fillColor);
        f_paint.setAlpha((int)fillOpacity);        
        
        this.type = PaliCanvas.TOOL_CIRCLE;
		this.x = x;
		this.y = y;
		this.r = r;
		this.rect = new RectF(x-r, y-r, x+r, y+r);
		tagSet();
	}
	
	PaliCircle(float x, float y, float r, float theta, Paint s_p, Paint f_p)
	{   
		this.x = x;
		this.y = y;
		this.r = r;
		this.rect = new RectF(x-r, y-r, x+r, y+r);
		this.theta = theta;
		s_paint = new Paint(s_p);       
        f_paint = new Paint(f_p);
        tagSet();
	}
		
	public void drawObject(Canvas c) {		
		this.type = PaliCanvas.TOOL_CIRCLE;
		this.rotRect = this.rect;
		c.drawCircle(x, y, r, s_paint);
		c.drawCircle(x, y, r, f_paint);
	}
	public void Move(float dx, float dy)
	{
		this.x += dx;
		this.y += dy;
		this.rect.left += dx; this.rect.right += dx; this.rect.top += dy; this.rect.bottom += dy;
		tagSet();
	}
	public void Scale(float dx, float dy)
	{
		float s = (float) Math.sqrt(dx*dx + dy*dy) / 2;
		
		if(dx+dy > 0) {
			this.r += s;
			this.rect.left -= s; this.rect.right += s; this.rect.top -= s; this.rect.bottom += s;
			
		}
		else {
			this.r -= s;
			this.rect.left += s; this.rect.right -= s; this.rect.top += s; this.rect.bottom -= s;
		}
		
		Move(dx/2, dy/2);
		tagSet();
	}
	@Override
	public PaliObject copy() {
		PaliCircle circle = new PaliCircle(this.x, this.y, this.r, this.theta, this.s_paint, this.f_paint);
		
		circle.svgtag = this.svgtag;
		circle.type = this.type;
		circle.filter = this.filter;

		return circle;
	}
}
