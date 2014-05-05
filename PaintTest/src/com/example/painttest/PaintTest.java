/**
 * ゲーム起動画面
 */
package com.example.painttest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;

public class PaintTest extends Activity implements OnClickListener{

	private RadioGroup rg;
	private CheckBox checkBCstQst;
	//private Button btnStart, btnAddQuest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paint_test);
	}

	@Override
	protected void onStart() {
		
		super.onStart();
		//リスナー登録
		findViewById(R.id.img_title).setOnClickListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.paint_test, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btnStart:
			intent=new Intent(PaintTest.this,GameStart.class);
			startActivity(intent);
			break;
		case R.id.btnAddQuest:
			intent=new Intent(PaintTest.this,CustomizeQuest.class);
			startActivity(intent);
			break;
		case R.id.img_title:
	    	if (findViewById(R.id.img_title).getVisibility() == View.VISIBLE) {
	    		findViewById(R.id.img_title).setVisibility(View.INVISIBLE);
	    		findViewById(R.id.btnStart).setVisibility(View.VISIBLE);
	    		findViewById(R.id.btnAddQuest).setVisibility(View.VISIBLE);
	    		findViewById(R.id.radioGroup1).setVisibility(View.VISIBLE);
	    		findViewById(R.id.checkBCstQst).setVisibility(View.VISIBLE);
	    		findViewById(R.id.txt_playerNum).setVisibility(View.VISIBLE);
	    		//btnStart=(Button)findViewById(R.id.btnStart);
	    		//btnAddQuest=(Button)findViewById(R.id.btnAddQuest);
	    		rg=(RadioGroup)findViewById(R.id.radioGroup1);
	    		checkBCstQst=(CheckBox)findViewById(R.id.checkBCstQst);
	    		findViewById(R.id.btnStart).setOnClickListener(this);
	    		findViewById(R.id.btnAddQuest).setOnClickListener(this);
	    		checkBCstQst.setOnCheckedChangeListener(new OnCheckedChangeListener(){

	    			@Override
	    			public void onCheckedChanged(CompoundButton buttonView,
	    					boolean isChecked) {
	    				// TODO Auto-generated method stub
	    				if(checkBCstQst.isChecked()==true){
	    					GameConstant.questKind="CustomQuest";
	    				}
	    			}
	    			
	    		});
	    		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	    			
	    			@Override
	    			public void onCheckedChanged(RadioGroup group, int checkedId) {
	    				// TODO Auto-generated method stub
	    				switch(checkedId){
	    				case R.id.radio0:
	    					GameConstant.playerNum=5;
	    					break;
	    				case R.id.radio1:
	    					GameConstant.playerNum=4;
	    					break;
	    				case R.id.radio2:
	    					GameConstant.playerNum=3;
	    					break;
	    					
	    				}
	    			}
	    		});

	    	}
	    	break;
	    	default:
	    		break;
		}
		
	}

}
