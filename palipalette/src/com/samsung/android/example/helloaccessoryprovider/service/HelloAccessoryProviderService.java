/*     * Copyright (c) 2014 Samsung Electronics Co., Ltd.    * All rights reserved.    *    * Redistribution and use in source and binary forms, with or without    * modification, are permitted provided that the following conditions are    * met:    *    *     * Redistributions of source code must retain the above copyright    *        notice, this list of conditions and the following disclaimer.   *     * Redistributions in binary form must reproduce the above   *       copyright notice, this list of conditions and the following disclaimer   *       in the documentation and/or other materials provided with the   *       distribution.   *     * Neither the name of Samsung Electronics Co., Ltd. nor the names of its   *       contributors may be used to endorse or promote products derived from   *       this software without specific prior written permission.   *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS   * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT   * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR   * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT   * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,   * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT   * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,   * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY   * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT   * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE   * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */package com.samsung.android.example.helloaccessoryprovider.service;import java.io.IOException;import java.util.HashMap;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import android.content.Intent;import android.graphics.Color;import android.os.Binder;import android.os.IBinder;import android.text.format.Time;import android.util.Log;import android.widget.TextView;import android.widget.Toast;import com.samsung.android.example.helloaccessoryprovider.R;import com.samsung.android.sdk.SsdkUnsupportedException;import com.samsung.android.sdk.accessory.SA;import com.samsung.android.sdk.accessory.SAAgent;import com.samsung.android.sdk.accessory.SAPeerAgent;import com.samsung.android.sdk.accessory.SASocket;import com.yozisunji.palipalette.CustomizingMainActivity;import com.yozisunji.palipalette.MainActivity;import com.yozisunji.palipalette.PaliCanvas;import com.yozisunji.palipalette.PaliTouchCanvas;import com.yozisunji.palipalette.SVGParser;public class HelloAccessoryProviderService extends SAAgent {	public static final String TAG = "HelloAccessoryProviderService";	public static final int SERVICE_CONNECTION_RESULT_OK = 0;	public static final int HELLOACCESSORY_CHANNEL_ID = 104;	//HashMap<Integer, HelloAccessoryProviderConnection> mConnectionsMap = null;		private HelloAccessoryProviderConnection Pv;	private final IBinder mBinder = new LocalBinder();	private int mConnctionID;		private static PaliCanvas canvas;	private static MainActivity activity;				public class LocalBinder extends Binder {		public HelloAccessoryProviderService getService() {			return HelloAccessoryProviderService.this;		}	}	public HelloAccessoryProviderService() {		super(TAG, HelloAccessoryProviderConnection.class);	}		public HelloAccessoryProviderService(MainActivity m) {		super(TAG, HelloAccessoryProviderConnection.class);		activity = m;		canvas = m.customview;	}    @Override    public void onCreate() {        super.onCreate();        Log.i(TAG, "onCreate of smart view Provider Service");                SA mAccessory = new SA();        try {        	mAccessory.initialize(this);        } catch (SsdkUnsupportedException e) {        	// Error Handling        } catch (Exception e1) {            Log.e(TAG, "Cannot initialize Accessory package.");            e1.printStackTrace();			/*			 * Your application can not use Accessory package of Samsung			 * Mobile SDK. You application should work smoothly without using			 * this SDK, or you may want to notify user and close your app			 * gracefully (release resources, stop Service threads, close UI			 * thread, etc.)			 */            stopSelf();        }    }	    @Override     protected void onServiceConnectionRequested(SAPeerAgent peerAgent) {         acceptServiceConnectionRequest(peerAgent);     }     	@Override	protected void onFindPeerAgentResponse(SAPeerAgent arg0, int arg1) {		// TODO Auto-generated method stub		Log.d(TAG, "onFindPeerAgentResponse  arg1 =" + arg1);	}	@Override	protected void onServiceConnectionResponse(SASocket thisConnection,			int result) {		if (result == CONNECTION_SUCCESS) {			if (thisConnection != null) {				//HelloAccessoryProviderConnection myConnection = (HelloAccessoryProviderConnection) thisConnection;				Pv =  (HelloAccessoryProviderConnection) thisConnection;				Pv.setActivity(activity);				/*				if (mConnectionsMap == null) {					mConnectionsMap = new HashMap<Integer, HelloAccessoryProviderConnection>();				}				myConnection.mConnectionId = (int) (System.currentTimeMillis() & 255);				Log.d(TAG, "onServiceConnection connectionID = "						+ myConnection.mConnectionId);								mConnectionsMap.put(myConnection.mConnectionId, myConnection);*/				Toast.makeText(getBaseContext(),						R.string.ConnectionEstablishedMsg, Toast.LENGTH_LONG)						.show();			} else {				Log.e(TAG, "SASocket object is null");			}		} else if (result == CONNECTION_ALREADY_EXIST) {			Log.e(TAG, "onServiceConnectionResponse, CONNECTION_ALREADY_EXIST");		} else {			Log.e(TAG, "onServiceConnectionResponse result error =" + result);		}	}	@Override	public IBinder onBind(Intent arg0) {		return mBinder;	}		public void send(JSONArray ja) throws JSONException	{		JSONArray custom = new JSONArray();		JSONArray screen;		JSONObject item;				screen= new JSONArray();				item = new JSONObject();		item.put("Function",0);		item.put("Num",0);		item.put("PosX",0);		item.put("PosY",0);		screen.put(item);				item = new JSONObject();		item.put("Function",1);		item.put("Num",0);		item.put("PosX",1);		item.put("PosY",2);		screen.put(item);				custom.put(screen);				screen= new JSONArray();				item = new JSONObject();		item.put("Function",0);		item.put("Num",0);		item.put("PosX",0);		item.put("PosY",0);		screen.put(item);				item = new JSONObject();		item.put("Function",1);		item.put("Num",0);		item.put("PosX",1);		item.put("PosY",2);		screen.put(item);				custom.put(screen);				final String hello = custom.toString();		if(Pv == null){			Log.e(TAG,"Error, can not get HelloAccessoryProviderConnection handler");			return;		}		new Thread(new Runnable() {			public void run() {				try {					//데이터 보냄					Pv.send(HELLOACCESSORY_CHANNEL_ID, hello.getBytes());				} catch (IOException e) {					e.printStackTrace();				}			}		}).start();	}				public class HelloAccessoryProviderConnection extends SASocket {		private int mConnectionId;		MainActivity mactivity;				public HelloAccessoryProviderConnection() {			super(HelloAccessoryProviderConnection.class.getName());		}		@Override		public void onError(int channelId, String errorString, int error) {			Log.e(TAG, "Connection is not alive ERROR: " + errorString + "  "					+ error);		}		public void setActivity(MainActivity m)		{			Log.d("pari", m.toString());			this.mactivity = m;		}		//데이터 받음		@Override		public void onReceive(int channelId, byte[] data)		{									Log.d(TAG, "onReceive");								String sData = new String(data, 0, data.length);	        JSONObject jObject;	        	        Log.i("debug", ""+sData);	        try {	        	jObject = new JSONObject(sData);	        	JSONObject obj;	            if(jObject.has("sColor"))	            {	            	obj = new JSONObject(jObject.getString("sColor"));	            	String R = obj.getString("r");		            	String G = obj.getString("g");		            	String B = obj.getString("b");	            	String A = obj.getString("a");	            		            	PaliCanvas.strokeColor = Color.rgb(Integer.parseInt(R), Integer.parseInt(G), Integer.parseInt(B));	            	PaliCanvas.alpha = Integer.parseInt(A);	  	            	mactivity.changeStyle(MainActivity.STYLE_STROKECOLOR);	            }	            else if(jObject.has("fColor"))	            {	            	obj = new JSONObject(jObject.getString("fColor"));	            	String R = obj.getString("r");		            	String G = obj.getString("g");		            	String B = obj.getString("b");	            	String A = obj.getString("a");	            		            	PaliCanvas.fillColor = Color.rgb(Integer.parseInt(R), Integer.parseInt(G), Integer.parseInt(B));	            	PaliCanvas.alpha = Integer.parseInt(A);	            	mactivity.changeStyle(MainActivity.STYLE_FILLCOLOR);	            		            }	            else if(jObject.has("layer"))	            {	            	obj = new JSONObject(jObject.getString("layer"));	            	if(obj.has("currentLayer"))	            	{	            		PaliCanvas.currentLayer = Integer.parseInt(obj.getString("currentLayer"));	            	}	            	else if(obj.has("checkedLayer"))	            	{	            		if(SVGParser.Layers.get(Integer.parseInt(obj.getString("checkedLayer"))).getVisible())	            			SVGParser.Layers.get(Integer.parseInt(obj.getString("checkedLayer"))).setVisible(false);	            		else	            			SVGParser.Layers.get(Integer.parseInt(obj.getString("checkedLayer"))).setVisible(true);	            	}	            	else if(obj.has("deletedLayer"))	            	{	            		SVGParser.Layers.remove(Integer.parseInt(obj.getString("deletedLayer")));	            	}	            	canvas.DrawScreen();	            }	            else if(jObject.has("stroke"))	            {	            	String stroke = jObject.getString("stroke");	            	PaliCanvas.strokeWidth = Integer.parseInt(stroke);	            	mactivity.changeStyle(MainActivity.STYLE_STROKEWIDTH);	            }	            else if(jObject.has("pencil"))	            {	            	PaliCanvas.selectedTool = PaliCanvas.TOOL_PENCIL;	            	mactivity.changeTool();	            }	            else if(jObject.has("brush"))	            {	            	PaliCanvas.selectedTool = PaliCanvas.TOOL_BRUSH;	            	mactivity.changeTool();	            }	            else if(jObject.has("shape"))	            {	            	obj = new JSONObject(jObject.getString("shape"));	            	if(obj.getString("rectangle").equals("1")) {                        PaliCanvas.selectedTool = PaliCanvas.TOOL_RECTANGLE;	                }	                else if(obj.getString("circle").equals("1")) {	                        PaliCanvas.selectedTool = PaliCanvas.TOOL_CIRCLE;	                }	                else if(obj.getString("ellipse").equals("1")) {	                        PaliCanvas.selectedTool = PaliCanvas.TOOL_ELLIPSE;	                }	            	mactivity.changeTool();	            }	            else if(jObject.has("objectPicker")) {	            	PaliCanvas.selectedTool = PaliCanvas.TOOL_PICKOBJECT;	            }	            else if(jObject.has("customize"))	            {	            	CustomizingMainActivity.json = jObject;	            	mactivity.launchCustomizing();	            }	        } catch (JSONException e1) {	        	// TODO Auto-generated catch block	        	e1.printStackTrace();	        }				}				@Override		protected void onServiceConnectionLost(int errorCode) {			Log.e(TAG, "onServiceConectionLost  for peer = " + mConnectionId					+ "error code =" + errorCode);			if (Pv != null) {				//mConnectionsMap.remove(mConnectionId);			}		}	}}