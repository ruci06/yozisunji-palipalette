package com.yozisunji.palipalette;

import java.util.ArrayList;










import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.widget.GridLayout;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class PaliScreen extends GridLayout{
	ArrayList<PaliItemView> items;
	ArrayList<ArrayList<Integer>>itemNumbers;
	Context mContext;
	
	int sWidth, sHeight;
	
	private boolean mPressed = false;
	private int selected = 0;
	private int indexX, indexY;
	
	private boolean touchable = false;
	
	public boolean mLongPressed = false;
	private Handler mHandler = new Handler();
	private LongPressCheckRunnable mLongPressCheckRunnable = new LongPressCheckRunnable();
	private int mLongPressTimeout=700;
	
	CustomizingActivity parent = null;
	
	public PaliScreen(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		items = new ArrayList<PaliItemView>();
		itemNumbers = new ArrayList<ArrayList<Integer>>();
		itemNumbers.add(new ArrayList<Integer>());
		itemNumbers.add(new ArrayList<Integer>());
		itemNumbers.add(new ArrayList<Integer>());
		this.setRowCount(3);
		this.setColumnCount(3);
		touchable = false;
		this.setBackgroundResource(R.drawable.screen_background);
		mLongPressTimeout = ViewConfiguration.getLongPressTimeout();
		initialize();
	}
	
	public PaliScreen(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		items = new ArrayList<PaliItemView>();
		itemNumbers = new ArrayList<ArrayList<Integer>>();
		itemNumbers.add(new ArrayList<Integer>());
		itemNumbers.add(new ArrayList<Integer>());
		itemNumbers.add(new ArrayList<Integer>());
		this.setBackgroundResource(R.drawable.screen_background);
		this.setRowCount(3);
		this.setColumnCount(3);
		touchable = false;
		mLongPressTimeout = ViewConfiguration.getLongPressTimeout();
		initialize();		
	}
	public void setParent(CustomizingActivity p)
	{
		parent = p;
	}
	public void initialize()
	{
		this.items.clear();
		this.removeAllViews();
		putNullItem(0,0);
		putNullItem(1,0);
		putNullItem(2,0);
		putNullItem(0,1);
		putNullItem(1,1);
		putNullItem(2,1);
		putNullItem(0,2);
		putNullItem(1,2);
		putNullItem(2,2);
	}
	public void putNullItem(int x, int y)
	{
		PaliItemView temp = new PaliItemView(CustomizingMainActivity.Common, 0, x, y, mContext);
		this.items.add(y*3+x,temp);
		this.addView(temp , temp.gl);
		
		//for(int i=0;i<temp.iteminfo.width;i++)
		{
			
		}

		/*
		if(temp.iteminfo.height>1)
		{
			for(int i=y ; i<temp.iteminfo.height ; i++)
			{
				for(int j=x; j<temp.iteminfo.width ; j++)
				{
					this.itemNumbers.get(i).set(j, y*3+x);
				}
			}
		}
		else if(temp.iteminfo.width>1)
		{
			for(int j=x; j<temp.iteminfo.width ; j++)
			{
				this.itemNumbers.get(y).set(j, y*3+x);
			}
		}*/
	}
	
	public boolean isPuttable(int func, int item, int posX, int posY)
	{
		PaliItem tempI = new PaliItem();
		tempI = CustomizingMainActivity.GearUIList.get(func).items.get(item);
		if(this.items.get(0).iteminfo.funcNum==3 && this.items.get(0).iteminfo.itemNum==0)
			return false;
		if(this.items.get(0).iteminfo.funcNum==0 && this.items.get(0).iteminfo.itemNum==2)
			return false;
		if(this.items.get(posX).iteminfo.funcNum==3 && this.items.get(posX).iteminfo.itemNum==1)
			return false;
		if(tempI.height>1)
		{
			for(int i=posY; i<tempI.height+posY ; i++)
			{
				for(int j=posX; j<tempI.width+posX; j++)
				{
					if(this.items.get(i*3+j).iteminfo.funcNum!=CustomizingMainActivity.Common)
						return false;
				}
			}
		}
		else
		{
			for(int j=posX; j<tempI.width+posX; j++)
			{
				if(this.items.get(posY*3+j).iteminfo.funcNum!=CustomizingMainActivity.Common)
					return false;
			}
		}
		
		return true;
	}
	public void putItem(int func, int item, int posX, int posY)
	{
		PaliItemView temp =this.items.get(posY*3+posX);
		
		if(temp.iteminfo.height>1)
		{
			for(int i=posY+1; i<temp.iteminfo.height+posY ; i++)
			{
				for(int j=posX+1; j<temp.iteminfo.width+posX; j++)
				{
					this.items.get(i*3+j).iteminfo = CustomizingMainActivity.GearUIList.get(CustomizingMainActivity.Common).items.get(0);
					this.items.get(i*3+j).setVisibility(View.INVISIBLE);
				}
			}
		}
		else
		{
			for(int j=posX+1; j<temp.iteminfo.width+posX; j++)
			{
				this.items.get(posY*3+j).setVisibility(View.INVISIBLE);
			}
		}
		
		temp.iteminfo = CustomizingMainActivity.GearUIList.get(func).items.get(item);
		temp.setImageResource(temp.iteminfo.imageid);
		
		temp.gl = (GridLayout.LayoutParams)temp.getLayoutParams();
		temp.gl.rowSpec = GridLayout.spec(posY,temp.iteminfo.height);
		temp.gl.columnSpec = GridLayout.spec(posX,temp.iteminfo.width);
		
		temp.setLayoutParams(temp.gl);
		
		this.items.set(posY*3+posX, temp);
		
		//if(func!=PaliCanvas.TOOL_COMMON)
		//	CustomizingMainActivity.GearUIViewList.get(func).removeItem(item);
		this.invalidate();
	}
	public void copy(PaliScreen p, int width, int height)
	{	
		PaliItemView temp;
		PaliItemView get;
		
		this.removeAllViews();
		this.items.clear();
				
		for(int i = 0; i<p.items.size();i++)
		{
			get = p.items.get(i);
			temp = new PaliItemView(get.iteminfo.funcNum, get.iteminfo.itemNum, get.x , get.y, mContext);
			this.items.add(i,temp);
			this.addView(temp , get.gl);
		}
		
		this.setSize(width, height);
		this.invalidate();
	}
	public void setSize(int width, int height)
	{
		this.getLayoutParams().width = width;
		this.getLayoutParams().height = height;
		
		for(int i = 0; i<items.size() ; i++)
		{
			items.get(i).setSize(width/3, height/3);
		}
	}
	
	public void setTouchable(boolean t)
	{
		touchable = t;
		for(int i=0;i<this.items.size();i++)
		{
			items.get(i).setTouchable(t);
		}
	}
	
	public void putJSON(JSONArray json) throws JSONException
	{
		JSONObject temp;
		
		for(int i=0; i<json.length() ; i++)
		{
			temp = json.getJSONObject(i);
			
			this.putItem(temp.getInt("Function"), temp.getInt("Num"), temp.getInt("PosX"), temp.getInt("PosY"));
		}
		this.invalidate();
	}
	
	public JSONArray getJSON() throws JSONException
	{
		JSONArray json = new JSONArray();
		
		
		for(int i=0 ; i<items.size(); i++)
		{
			if(items.get(i).iteminfo.funcNum!=CustomizingMainActivity.Common)
			{
				JSONObject temp = new JSONObject();
				temp.put("Function", items.get(i).iteminfo.funcNum);
				temp.put("Num", items.get(i).iteminfo.itemNum);
				temp.put("PosX",items.get(i).x);
				temp.put("PosY",items.get(i).y);
				
				json.put(temp);
			}
		}
		return json;
	}
	
	public PaliPoint getPos(MotionEvent e, PaliItem sel)
	{
		PaliPoint pt = new PaliPoint(999,999);
		Rect r = new Rect();
		
		for(int i=0;i<items.size();i++)
		{
			if(items.get(i).iteminfo.funcNum==CustomizingMainActivity.Common)
			{
				items.get(i).getGlobalVisibleRect(r);
				if(r.contains((int)e.getX(),(int)e.getY()))
				{
					if(sel.height>1)
					{
						for(int o=items.get(i).y+1; i<sel.height+items.get(i).y ; i++)
						{
							for(int p=items.get(i).x+1; p<sel.width+items.get(i).x; p++)
							{
								if(o>2 || o<0 || p>2 || p<0)
								{
									return pt;
								}
								if(items.get(o*3+p).iteminfo.funcNum!=CustomizingMainActivity.Common)
									return pt;
							}
						}
					}
					else
					{
						for(int p=items.get(i).x+1; p<sel.width+items.get(i).x; p++)
						{
							if(p>2 || p<0)
							{
								return pt;
							}
							if(items.get(items.get(i).y*3+p).iteminfo.funcNum!=CustomizingMainActivity.Common)
								return pt;
						}
					}
					pt.x = items.get(i).x;
					pt.y = items.get(i).y;
					return pt;
				}
			}
		}
		return pt;
	}
	public boolean hasCustomize()
	{
		for(int i=0;i<items.size();i++)
		{
			if(items.get(i).iteminfo.funcNum==CustomizingMainActivity.Config && items.get(i).iteminfo.itemNum==1)
				return true;
		}
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		//return gDetector.onTouchEvent(e);
		if(!touchable)
			return false;
		
		super.onTouchEvent(e);		 
		Rect r = new Rect();
		 switch(e.getAction() & MotionEvent.ACTION_MASK)
		 {		 
		 case MotionEvent.ACTION_DOWN:
			 
			for(int i=0;i<items.size();i++)
			{
				if(items.get(i).iteminfo.funcNum!=CustomizingMainActivity.Common)
				{
					items.get(i).getGlobalVisibleRect(r);
					if(r.contains((int)e.getX(),(int)e.getY()))
					{
						selected=i;
						mPressed=true;
						items.get(selected).setBackgroundColor(Color.argb(70, 170, 170, 170));
						if(!(items.get(i).iteminfo.funcNum == CustomizingMainActivity.Config && items.get(i).iteminfo.itemNum == 1))
							startTimeout();
						break;
					}
				}
			}
			 return false;
		 case MotionEvent.ACTION_MOVE:
			 if(items.get(selected).iteminfo.itemType==PaliItem.TYPE_WIDGET)
			 {
				 mPressed=false;
				 items.get(selected).setBackgroundColor(Color.argb(0, 255, 255, 255));
				 return true;
			 }
			 if(mPressed)
			 {	
				 finishLongPressed();
				for(int i=0;i<items.size();i++)
				{
					 if(i!=selected && items.get(i).iteminfo.funcNum==CustomizingMainActivity.Common)
					 {
						items.get(i).getGlobalVisibleRect(r);
						if(r.contains((int)e.getX(),(int)e.getY()))
						{	
							stopTimeout();
							PaliItemView temp = items.get(selected);
					    	PaliItemView change = items.get(i);
					    	
					    	
					    	GridLayout.LayoutParams gridLayoutParam = (GridLayout.LayoutParams)temp.getLayoutParams();
					    	GridLayout.LayoutParams changeLayoutParam = (GridLayout.LayoutParams)change.getLayoutParams();
					    	temp.setLayoutParams(changeLayoutParam);
					    	change.setLayoutParams(gridLayoutParam);
					    	
					    	int tempX, tempY;
					    	tempX = temp.x;
					    	tempY = temp.y;
					    	
					    	temp.x = change.x;
					    	temp.y = change.y;
					    	
					    	change.x = tempX;
					    	change.y = tempY;
					    	
					    	//selected = i;
					    	break;
					    
						}
					 }
				}
			 }
			 return true;
		 case MotionEvent.ACTION_UP:
			 if(mLongPressed)
			 {

					finishLongPressed();
					return true;
			 }
			 else{
				 if(mPressed)
				 {
					 mPressed=false;
					 items.get(selected).setBackgroundColor(Color.argb(0, 255, 255, 255));
					 
				 }
				 return true;
			 }
		 }
		 return true;
	}
	
	public void startTimeout() {
		mLongPressed = false;
		mHandler.postDelayed(mLongPressCheckRunnable, mLongPressTimeout);
	}

	public void stopTimeout() {
		if (!mLongPressed)
			mHandler.removeCallbacks(mLongPressCheckRunnable);
	}

	public void finishLongPressed() {
		this.mLongPressed = false;
	}
	private class LongPressCheckRunnable implements Runnable {
		@Override
		public void run() {
			mLongPressed = true;

			PaliItemView temp = items.get(selected);
			
			//if(temp.iteminfo.funcNum!=PaliCanvas.TOOL_COMMON)
				//CustomizingMainActivity.GearUIViewList.get(temp.iteminfo.funcNum).restoreItem(temp.iteminfo.itemNum);
			
			if(temp.iteminfo.height>1)
			{
				for(int i=temp.y; i<temp.iteminfo.height+temp.y ; i++)
				{
					for(int j=temp.x; j<temp.iteminfo.width+temp.x; j++)
					{
						if(!(i==temp.y && j==temp.x))
							items.get(i*3+j).setVisibility(View.VISIBLE);
					}
				}
			}
			else
			{
				for(int j=temp.x+1; j<temp.iteminfo.width+temp.x; j++)
				{
					items.get(temp.y*3+j).setVisibility(View.VISIBLE);
				}
			}
			
			temp.iteminfo = CustomizingMainActivity.GearUIList.get(CustomizingMainActivity.Common).items.get(0);
			temp.setImageResource(temp.iteminfo.imageid);
			
			temp.gl = (GridLayout.LayoutParams)temp.getLayoutParams();
			temp.gl.rowSpec = GridLayout.spec(temp.y,temp.iteminfo.height);
			temp.gl.columnSpec = GridLayout.spec(temp.x,temp.iteminfo.width);
			temp.setLayoutParams(temp.gl);
			
			items.set(selected, temp);
			items.get(selected).setBackgroundColor(Color.argb(0, 255, 255, 255));
			
			
			for(int i=0;i<3;i++)
			{
				for(int j=0;j<3;j++)
				{
					items.get(i*3+j).setVisibility(View.VISIBLE);
				}
			}
			invalidate();
			if(parent!=null)
				parent.reDraw();
			
			mPressed = false;
		}
	}
}
