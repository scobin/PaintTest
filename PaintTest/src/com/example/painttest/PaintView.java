package com.example.painttest;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;

import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class PaintView extends SurfaceView 
	implements SurfaceHolder.Callback{

	
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paintx=new Paint();
	private TextView textSubject;
	private float recx=0,recy=0;
	public int penCount=0;
	public int isDrawing=1;
	public PaintView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		sfh=getHolder();
		sfh.addCallback(this);
		this.setLongClickable(true); //It's important to detect the gesture.
		//設置背景
		
		this.setBackgroundResource(R.drawable.bc_frame);
		this.setZOrderOnTop(true);
		this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		
	}
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		Log.d("test", "surfaceChanged");
		
		//canvas=sfh.lockCanvas();
		
		//canvas.drawColor(Color.WHITE);
		//sfh.unlockCanvasAndPost(canvas);
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		Log.d("test", "surfaceCreated");
		
		//canvas=sfh.lockCanvas();
		//canvas.drawColor(Color.WHITE);
		//sfh.unlockCanvasAndPost(canvas);
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.d("test", "surfaceDestroyed");
		
	}

		
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		// TODO Auto-generated method stub
		float posx,posy,left,right,top,bottom;
		posx=me.getX();
		posy=me.getY();
	
		paintx.setColor(Color.RED);
		paintx.setStrokeWidth(3);
		paintx.setAntiAlias(true);
		//事件觸發後判斷是否可以劃線
		if(isDrawing==1){
			//當可以劃線時判斷手勢
			if(penCount<(GameConstant.playerNum-1)){
				switch(me.getAction()){
				case MotionEvent.ACTION_DOWN:
					Log.d("test", "Action Down");
					Log.d("test", ":(posx,posy)=("+posx+ ", "+posy+")");
					canvas=sfh.lockCanvas(new Rect((int)posx,(int)posy,
							(int)posx+2,(int)posy+2));
					
					canvas.drawPoint(posx, posy, paintx);
					//show point
					sfh.unlockCanvasAndPost(canvas);
					recx=posx;
					recy=posy;
					break;
				case MotionEvent.ACTION_MOVE:
					Log.d("test", "Action Move");
					Log.d("test", ":(recx,recy)=("+recx+ ", "+recy+")");
					Log.d("test", ":(posx,posy)=("+posx+ ", "+posy+")");
					//if the distance of moving satisfies the cond. of draw line 
					if((Math.abs(recx-posx)+Math.abs(recy-posy))>=0 ){
						if(recx>posx){
							left=posx;
							right=recx+2;
						}
						else {
							left=recx;
							right=posx+2;
						}
						if(recy>posy) {
							top=posy;
							bottom=recy+2;
						}
						else{
							top=recy;
							bottom=posy+2;
						}

						
						
						canvas=sfh.lockCanvas(new Rect((int)left,(int)top,(int)right,(int)bottom));
						
						canvas.drawLine(recx, recy, posx, posy, paintx);
						//show the line
						sfh.unlockCanvasAndPost(canvas);
					    //update recx,recy
						
						recx=posx;
						recy=posy;
					}	
				
					break;
				case MotionEvent.ACTION_UP:
					Log.d("test", "Action Up");
					
					penCount++;
					isDrawing=0;
					
					break;
				
				}
			}
			else{
				
			}
		}
		
		return super.onTouchEvent(me);
	}
	public void clearView(){
		canvas=sfh.lockCanvas();
		//this.setZOrderOnTop(false);
		canvas.drawColor(Color.WHITE);
		
		//show the line
		sfh.unlockCanvasAndPost(canvas);
		//this.setBackgroundResource(R.drawable.bc_frame);
		this.setZOrderOnTop(true);
		//this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		penCount=0;
	}
	public void playerChangeDialog(){
		FrameLayout fl=(FrameLayout)this.getParent();
		RelativeLayout rl=(RelativeLayout)fl.getParent();
		textSubject=(TextView)rl.findViewById(R.id.textSubject);
		final String subject=textSubject.getText().toString();
		textSubject.setText(R.string.subject);
		AlertDialog.Builder ad=new AlertDialog.Builder(getContext());
		ad.setTitle("プレーヤー変換");
		ad.setMessage("劃数合計："+Integer.toString(penCount));
		ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				textSubject.setText(subject);
				isDrawing=1;
			}
		});
		ad.setCancelable(false);
		ad.show();
	}
}
	
	

