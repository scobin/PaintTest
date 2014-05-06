/**
 * 描く画面を制御するビュー
 */
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
		sfh=getHolder();
		sfh.addCallback(this);
		this.setLongClickable(true); //It's important to detect the gesture.
		//背景設定
		this.setZOrderOnTop(true);
		this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		
	}
		
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		float posx,posy,left,right,top,bottom;
		posx=me.getX();
		posy=me.getY();
	
		paintx.setColor(Color.RED);
		paintx.setStrokeWidth(3);
		paintx.setAntiAlias(true);
		//描くのは可能かどうかを判断する
		if(isDrawing==1){
			//ジェスチャーの判断
			if(penCount<(GameConstant.playerNum-1)){
				switch(me.getAction()){
				case MotionEvent.ACTION_DOWN:
					canvas=sfh.lockCanvas(new Rect((int)posx,(int)posy,
							(int)posx+2,(int)posy+2));
					
					canvas.drawPoint(posx, posy, paintx);
					//show point
					sfh.unlockCanvasAndPost(canvas);
					recx=posx;
					recy=posy;
					break;
				case MotionEvent.ACTION_MOVE:
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
						recx=posx;
						recy=posy;
					}	
				
					break;
				case MotionEvent.ACTION_UP:
					penCount++;
					isDrawing=0;
					break;
				default:
					break;
				}
			}
		}
		
		return super.onTouchEvent(me);
	}
	
	/**
	 * ビューのクリアー
	 */
	public void clearView(){
		canvas=sfh.lockCanvas();
		canvas.drawColor(Color.WHITE);
		
		//show the line
		sfh.unlockCanvasAndPost(canvas);
		this.setZOrderOnTop(true);
		penCount=0;
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// 何もしない
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// 何もしない
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// 何もしない
		
	}
}
	
	

