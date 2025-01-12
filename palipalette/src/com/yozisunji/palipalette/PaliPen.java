package com.yozisunji.palipalette;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

public class PaliPen extends PaliObject {

	PaliPen(String tag, int scolor, int fcolor)
	{
		super(tag,scolor,fcolor);
	}
	PaliPen(Path path, List<Float> movingX, List<Float> movingY, RectF rect)
	{
		s_paint = new Paint();
		s_paint.setAntiAlias(true);
		s_paint.setColor(PaliCanvas.strokeColor);
		s_paint.setStyle(Paint.Style.STROKE);
		s_paint.setAlpha(PaliCanvas.alpha);
		s_paint.setStrokeWidth(PaliCanvas.strokeWidth);
		
		this.type = PaliCanvas.TOOL_PEN;
		this.path = path;
		this.movingX = new ArrayList<Float>(movingX);
		this.movingY = new ArrayList<Float>(movingY);
		this.rect = rect;
		tagSet();
	}
	PaliPen(Path path, int strokeColor, float strokeWidth, float strokeOpacity, float theta)
	{
		s_paint = new Paint();
		s_paint.setAntiAlias(true);
		s_paint.setColor(strokeColor);
		s_paint.setStrokeWidth(strokeWidth);
		s_paint.setAlpha((int)strokeOpacity);
		this.path = path;
		this.theta = theta;
		this.rect = new RectF(100, 100, 500, 500);
	}
	PaliPen(Path path, RectF rect, float theta, Paint s_p)
	{
		
		this.path = new Path(path);
		this.rect = new RectF(rect);
		this.theta = theta;
		s_paint = new Paint(s_p);
		this.Move(PaliTouchCanvas.translateX, PaliTouchCanvas.translateY);
	}
	PaliPen(Path path, RectF rect, float theta, Paint s_p, int flag)
	{
		
		this.path = new Path(path);
		this.rect = new RectF(rect);
		this.theta = theta;
		s_paint = new Paint(s_p);
	}

	PaliPen()
	{	
		this.path = new Path();
	}
	
	public Path getPath()
	{
		return path;
	}
	
	public void drawObject(Canvas c) {
		this.type = PaliCanvas.TOOL_PEN;
		this.rotRect = rotateRect(this.rect, this.theta);
		s_paint.setStyle(Paint.Style.STROKE);
		c.save();
		c.rotate(theta, this.rect.centerX(), this.rect.centerY());		
		c.drawPath(path, s_paint);	
		c.restore();		
	}
	public void Move(float dx, float dy)
	{
		Log.i("debug","dx: "+dx+" dy: "+dy);
		Matrix moveMatrix = new Matrix();
		moveMatrix.setTranslate(dx,dy);
		this.path.transform(moveMatrix);
		this.rect.left += dx; this.rect.right += dx; this.rect.top += dy; this.rect.bottom += dy;
		for(int i=0; i<movingX.size(); i++) {
			movingX.set(i, movingX.get(i)+dx);
			movingY.set(i, movingY.get(i)+dy);
		}
		tagSet();
	
	}
	public void Scale(float dx, float dy)
	{
		float width = 1f+(dx/this.rect.width());
		float height = 1f+(dy/this.rect.height());	
		if(width < 0) width = 0.001f;
		if(height < 0) height = 0.001f;		

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(width, height, this.rect.left, this.rect.top);
		this.path.transform(scaleMatrix);
		
		this.rect.right = this.rect.left + ((this.rect.right - this.rect.left) * width);
		this.rect.bottom = this.rect.top + ((this.rect.bottom - this.rect.top) * height);
	}
	@Override
	public PaliObject copy() {
		PaliPen pen = new PaliPen(this.path, this.rect, this.theta, this.s_paint, 0);
		
		pen.svgtag = this.svgtag;
		pen.type = this.type;
		pen.filter = this.filter;

		return pen;
	}
}
