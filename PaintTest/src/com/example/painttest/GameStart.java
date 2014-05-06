/**
 * ゲームの進行画面
 * 
 */
package com.example.painttest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameStart extends Activity implements OnClickListener{
	private FrameLayout frame;
	private ProgressDialog pd;
	private TextView textSubject,textAnswer;
	private Button btnAnsOK;
	private EditText eTAnswer;
	private ProgressBar timeProgress;
	private PaintView paintView;
	private String Answer;
	private Thread mThread;
	private boolean running;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_game_start);
		frame=(FrameLayout)findViewById(R.id.frame);
		textSubject=(TextView)findViewById(R.id.textSubject);
		//--------当てるUI
		textAnswer=(TextView)findViewById(R.id.textAnswer);
		eTAnswer=(EditText)findViewById(R.id.eTAnswer);
		btnAnsOK=(Button)findViewById(R.id.btnAnsOK);
		//-------------------------
		//final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		timeProgress=(ProgressBar)findViewById(R.id.timeProgress);
		timeProgress.setIndeterminate(false);
		paintView=new PaintView(this);
		frame.addView(paintView);
		btnAnsOK.setOnClickListener(this);
		//テーマの表示
		subjectPrepare();

		
	}

	@Override
	protected void onDestroy() {
		btnAnsOK.setOnClickListener(null);
		running=false;
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive()){

			imm.hideSoftInputFromWindow(eTAnswer.getWindowToken(), 0);
		}
		//To check the answer is right or wrong.
		AlertDialog.Builder ad=new AlertDialog.Builder(GameStart.this);
		ad.setCancelable(true);
		String userAns=eTAnswer.getText().toString();
		if(Answer.equals(userAns)){
			//Answer is right
			ad.setTitle("正解");
			ad.setMessage("お見事ですね！もう一回遊びますか？");
			ad.setPositiveButton("はい", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// reset and play again
					frame.removeAllViews();
					paintView=new PaintView(getBaseContext());
					frame.addView(paintView);
					paintView.penCount=0;
					paintView.isDrawing=1;
					subjectPrepare();
				}
			});
			ad.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Do not play 
					finish();
				}
			});
			ad.show();
		}
		else{
			//Answer is wrong
			ad.setTitle("残念");
			ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			ad.show();
		}
		eTAnswer.setText("");
		
	}
	
	/**
	 * テーマの表示して、ゲームを進行する。
	 */
	void subjectPrepare(){
		//回答画面を設定(画面を消す)
		textAnswer.setVisibility(View.GONE);
		btnAnsOK.setVisibility(View.GONE);
		eTAnswer.setVisibility(View.GONE);
		//テーマのタイプの確認
		if(GameConstant.questKind=="CustomQuest"){
			Answer=GameConstant.customquestlist.get((int)(Math.random() * 
					GameConstant.customquestlist.size()));
		}
		else{
			Answer=GameConstant.question[(int)(Math.random() * GameConstant.question.length)];
		}
		//秒読みダイアロック
		pd = new ProgressDialog(this);
		pd.setTitle("秒読み--3秒前");
		pd.setIndeterminate(true);
		pd.setCancelable(true);
		pd.show();
      
		running=true;
		mThread = new Thread(new Runnable() {
			 
			public void run() {
				while(running==true){
					Message message;
				      for(int i=3 ; i >=0 ; i--){
				    	  try{
				    		  Thread.sleep(1000);
				    		  message = handler.obtainMessage(1,Integer.toString(i));
				    		  handler.sendMessage(message);
				    	  }catch (Exception e) {
				    		  e.printStackTrace();
				    	  }
				      } 
				     //秒読みダイアロックを消すメッセージ設定
				      message = handler.obtainMessage(1,"over");
		    		  handler.sendMessage(message);
		    		 //ゲームスタート。描く時間をカウントする。
		    		 while(paintView.penCount<(GameConstant.playerNum-1)){
		    			 if(paintView.isDrawing==1){
			    			  for(int i=0;i<100;i++){
				    			  try{
				    				  Thread.sleep(GameConstant.drawTime/100);
				    				  if(i==99){
				    					  //タイムアウト
				    					  paintView.isDrawing=0;
				    					  paintView.penCount++;
					    				  message=handler.obtainMessage(i,"timeout");
					    				  handler.sendMessage(message);
					    				  
					    			  }
					    			  else{
					    				  //
					  		        	  if(paintView.isDrawing==0){
					  		        		  //描きを完了
					  		        		  i=99;//タイムアウトのように設定する
					  		        		message=handler.obtainMessage(i,"timeout");
					  		        		handler.sendMessage(message);
					  		        		  
					  		        	  }
					  		        	  else{
					  		        		  //描き中
					  		        		   message=handler.obtainMessage(i,"timerun");
					  		        		   handler.sendMessage(message);
					  		        	  }
					    			  }
				    			  }catch(Exception e){
				    				  e.printStackTrace();
				    			  }
				    			  
				    		  }
			    		  }
		    		 }
		    		 running=false;
				}//WhileEnd
			       
			}//RunEnd
			
		});
			 
		mThread.start();

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
		        super.handleMessage(msg);
		        String MsgString = (String)msg.obj;
		        // Edit the dialog's text in the process of preparing time.
		        if(MsgString!="over"){
		        	pd.setMessage(MsgString);
		        }
		        // 
		        if(MsgString=="over"){
		        	pd.dismiss();
		        	textSubject.setText(getString(R.string.subject)+Answer);
		        }
		        
		        if(MsgString=="timerun"){
		        	timeProgress.setVisibility(View.VISIBLE);
		        	timeProgress.setProgress(msg.what);      	
		        		
		        }
		        if(MsgString=="timeout"){
		            timeProgress.setProgress(0);
		        	paintView.isDrawing=0;
  				    
  				    if(paintView.penCount==(GameConstant.playerNum-1)){
  					    //当てる時間
  				    	textAnswer.setVisibility(View.VISIBLE);
  						eTAnswer.setVisibility(View.VISIBLE);
  						btnAnsOK.setVisibility(View.VISIBLE);	
  						textSubject.setText(R.string.subject);
  				    }
  				    else{
  					   //プレーヤー変換、ダイやログの表示 
  				    	playerChangeDialog();
  				    }
		        	
		        }

		}
	};
	
	/**
	 * プレーヤー変換のダイアロック
	 */
	public void playerChangeDialog(){
		final String subject=textSubject.getText().toString();
		textSubject.setText(R.string.subject);
		AlertDialog.Builder ad=new AlertDialog.Builder(this);
		ad.setTitle("プレーヤー変換");
		ad.setMessage("劃数合計："+Integer.toString(paintView.penCount));
		ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				textSubject.setText(subject);
				paintView.isDrawing=1;
			}
		});
		ad.setCancelable(false);
		ad.show();
	}

}
