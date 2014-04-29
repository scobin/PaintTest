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
		
		subjectPrepare();

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_start, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
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
	
	void subjectPrepare(){
		
		//繪畫過程中回答畫面消失
		textAnswer.setVisibility(View.GONE);
		btnAnsOK.setVisibility(View.GONE);
		eTAnswer.setVisibility(View.GONE);
		//設定題目類型是預設問題或是自定問題
		if(GameConstant.questKind=="CustomQuest"){
			Answer=GameConstant.customquestlist.get((int)(Math.random() * 
					GameConstant.customquestlist.size()));
		}
		else{
			Answer=GameConstant.question[(int)(Math.random() * GameConstant.question.length)];
		}
		//
		pd = new ProgressDialog(this);
		pd.setTitle("秒読み--3秒前");
		//pd.setMessage("");
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
				     // The time for preparing is over.Dismiss the dialog
				      message = handler.obtainMessage(1,"over");
		    		  handler.sendMessage(message);
		    		 // Game start and show the time bar.
		    		 while(paintView.penCount<(GameConstant.playerNum-1)){
		    			 if(paintView.isDrawing==1){
			    			  for(int i=0;i<100;i++){
				    			  try{
				    				  Thread.sleep(GameConstant.drawTime/100);
				    				  if(i==99){
				    					  //畫圖時間到,對paintView控制,修改筆畫數and Drawing state.
				    					  paintView.isDrawing=0;
				    					  paintView.penCount++;
					    				  message=handler.obtainMessage(i,"timeout");
					    				  handler.sendMessage(message);
					    				  
					    			  }
					    			  else{
					    				 //畫圖時間未到 
					    				  //確認是否已經劃線，若劃線完畢時間軸停止，並顯示換玩家對話框，若尚未劃完則繼續時間倒數
					  		        	  if(paintView.isDrawing==0){
					  		        		  //劃線完畢
					  		        		  i=99;//跳出for迴圈，讓時間倒數結束
					  		        		message=handler.obtainMessage(i,"timeout");
					  		        		handler.sendMessage(message);
					  		        		  
					  		        	  }
					  		        	  else{
					  		        		  //尚未劃線或劃線中
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
		    		 Log.d("thd", "thread is over");
		    		 running=false;
				}
			       
			}
			
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
		        //Edit the progress bar in the process of playing game.
		        
		        if(MsgString=="timerun"){
		        	timeProgress.setVisibility(View.VISIBLE);
		        	timeProgress.setProgress(msg.what);
		        	//check the drawing is done or not.If done, let timeProgress disappear.
		        	
		        		
		        }
		        if(MsgString=="timeout"){
		            timeProgress.setProgress(0);
		            //timeProgress.setVisibility(View.GONE);
		        	//出現dialog表示換玩家or guess
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
  				    	//paintView.playerChangeDialog();
  				    	playerChangeDialog();
  				    }
		        	
		        }

		}
	};
	
    
	public void playerChangeDialog(){
		
		final String subject=textSubject.getText().toString();
		textSubject.setText(R.string.subject);
		AlertDialog.Builder ad=new AlertDialog.Builder(this);
		ad.setTitle("プレーヤー変換");
		ad.setMessage("劃数合計："+Integer.toString(paintView.penCount));
		ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				textSubject.setText(subject);
				paintView.isDrawing=1;
			}
		});
		ad.setCancelable(false);
		ad.show();
	}

}
