package com.example.painttest;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PaintTest extends Activity {

	private RadioGroup rg;
	private CheckBox checkBCstQst;
	private Button btnStart, btnAddQuest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paint_test);
		btnStart=(Button)findViewById(R.id.btnStart);
		btnAddQuest=(Button)findViewById(R.id.btnAddQuest);
		rg=(RadioGroup)findViewById(R.id.radioGroup1);
		checkBCstQst=(CheckBox)findViewById(R.id.checkBCstQst);
		btnStart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(PaintTest.this,GameStart.class);
				startActivity(intent);
			}
			
		});
		btnAddQuest.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(PaintTest.this,CustomizeQuest.class);
				startActivity(intent);
			}
		
		});
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

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.paint_test, menu);
		return true;
	}

}
