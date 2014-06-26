package com.yozisunji.palipalette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PaliTouchCanvas extends View{
	Path path = new Path();
	float downX=0, downY=0, upX=0, upY=0, moveX=0, moveY=0, premoveX=0, premoveY=0;
	String 			movement="";
	String 			html="";
	
	PaliObject tempObj;
	Paint p;
	
	PaliCanvas canvas;

	public PaliTouchCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		tempObj = null;
		p = new Paint();
	}
	
	public void setCanvasAddr(PaliCanvas c)
	{
		canvas = c;
	}
	
	public void onDraw(Canvas cnvs)
	{
		if(tempObj!=null)
		{
			p.setColor(Color.BLUE);
			p.setStyle(Style.STROKE);
			p.setColor(Color.GREEN);
			p.setStyle(Style.FILL);
			tempObj.drawObject(cnvs);
		}
		
	}
	
	public boolean onTouchEvent(MotionEvent e)
	 {
		 super.onTouchEvent(e);
		 
		 switch(e.getAction())
		 {
		 
		 case MotionEvent.ACTION_DOWN:
			 downX = e.getX();
			 downY = e.getY();
			 			 
			 switch(canvas.selectedTool)
			 {
			 case 0:
				 path.moveTo(downX, downY);
				 tempObj = new PaliFreeDraw();
				 ((PaliFreeDraw)tempObj).getPath().moveTo(downX, downY);
				 break;
			 }
			 return true;
		 case MotionEvent.ACTION_MOVE:
			 moveX = e.getX();
			 moveY = e.getY();
			 
			 switch(canvas.selectedTool)
			 {
			 case 0:
				 movement = movement + " " + moveX + " " + moveY;
				 path.lineTo(moveX, moveY);
				 ((PaliFreeDraw)tempObj).getPath().lineTo(moveX,moveY);
				 break;
			 case 1:
				 tempObj = new PaliCircle(downX, downY, (float)Math.sqrt((float)Math.pow(moveX-downX, 2) + (float)Math.pow(moveY-downY, 2)));
				 break;
			 case 2:
				 tempObj = new PaliRectangle(downX, downY, moveX, moveY);
				 
			 }
			 //this.DrawScreen();
			 this.invalidate();
			 return true;
		 case MotionEvent.ACTION_UP:
			 upX = e.getX();
			 upY = e.getY();	
			 switch(canvas.selectedTool) {
			 case 0: // FreeDraw
				 html = "<path fill=\"none\" stroke=\"black\" d=\"M "+downX+" "+downY+""+movement+"\" />";
				 SVGParser.Layers.get(canvas.currentLayer).objs.add(new PaliFreeDraw(html, path));
				 break;
			 case 1: // Circle
				 float r = (float)Math.sqrt((float)Math.pow(upX-downX, 2) + (float)Math.pow(upY-downY, 2));
				 float cx = downX;
				 float cy = downY;
				 
				 html = "<circle cs="+ cx +" cy=" + cy + " r=" + r + " style=\"fill:yellow;stroke:purple;stroke-width:2\"/> ";
				 SVGParser.Layers.get(canvas.currentLayer).objs.add(new PaliCircle(html,cx,cy,r));
				 break;
			 case 2: // Rectangle
				 float x = Math.min(downX, upX);
				 float y = Math.min(downY, upY);
				 float width = (float)Math.sqrt((float)Math.pow(upX-downX, 2));
				 float height = (float)Math.sqrt((float)Math.pow(upY-downY, 2));
				 
				 float left = downX;
				 float top = downY;
				 float right = upX;
				 float bottom = upY;
				 
				 html = "<rect x="+x+" y="+y+" width="+width+" height="+height+" style=\"fill:rgb(0,0,255);stroke-width:3;stroke:rgb(0,0,0)\" /> ";
				 SVGParser.Layers.get(canvas.currentLayer).objs.add(new PaliRectangle(html,left,top,right,bottom));
				 break;
			 }
			 tempObj=null;
			 //this.DrawScreen();
			 this.invalidate();
			 canvas.DrawScreen();
			 return true;
		 }
		 return false;
	 
	 }

}
