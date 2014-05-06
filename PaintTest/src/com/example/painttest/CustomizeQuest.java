/**
 * テーマを作成する画面
 */
package com.example.painttest;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CustomizeQuest extends Activity {
	private EditText editTQ1,editTQ2,editTQ3;
	private Button btnCstQstAddOk, btnCstQstEnd;
	private TextView textViewQuestNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_customize_quest);
		
		editTQ1=(EditText)findViewById(R.id.editTQ1);
		editTQ2=(EditText)findViewById(R.id.editTQ2);
		editTQ3=(EditText)findViewById(R.id.editTQ3);
		btnCstQstAddOk=(Button)findViewById(R.id.btnCstQstAddOk);
		btnCstQstEnd=(Button)findViewById(R.id.btnCstQstEnd);
        textViewQuestNum=(TextView)findViewById(R.id.textViewQuestNum);
		btnCstQstAddOk.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if("".equals(editTQ1.getText().toString()) != true){
					GameConstant.customquestlist.add(editTQ1.getText().toString());
				}
				if("".equals(editTQ2.getText().toString()) != true){
					GameConstant.customquestlist.add(editTQ2.getText().toString());
					
				}
				if("".equals(editTQ3.getText().toString()) != true){
					GameConstant.customquestlist.add(editTQ3.getText().toString());
				}
				int l=GameConstant.customquestlist.size();
				String str=getString(R.string.QuestNum)+" "+Integer.toString(l);
				textViewQuestNum.setText(str);
				//清除editText文字
				editTQ1.setText("");
				editTQ2.setText("");
				editTQ3.setText("");
			}
			
		});
		btnCstQstEnd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
	}


}
