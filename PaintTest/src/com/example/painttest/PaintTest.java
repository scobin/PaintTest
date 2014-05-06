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
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.ViewSwitcher;

public class PaintTest extends Activity implements OnClickListener{

	private RadioGroup rg;
	private CheckBox checkBCstQst;
	private ViewSwitcher vSContent;
	//private Button btnStart, btnAddQuest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_paint_test);
		vSContent = (ViewSwitcher) findViewById(R.id.vSContent);
	}

	@Override
	protected void onStart() {
		
		super.onStart();
		//リスナー登録
		findViewById(R.id.img_title).setOnClickListener(this);
	}
	
	@Override
	protected void onStop() {
		
		super.onStart();
		//リスナー解除
		findViewById(R.id.img_title).setOnClickListener(null);
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
			if (vSContent.getDisplayedChild() == 0) {
				vSContent.setInAnimation(this, R.anim.anim_in);
				vSContent.setOutAnimation(this, R.anim.anim_out);
				vSContent.showNext();
				rg=(RadioGroup)findViewById(R.id.radioGroup1);
	    		checkBCstQst=(CheckBox)findViewById(R.id.checkBCstQst);
	    		findViewById(R.id.btnStart).setOnClickListener(this);
	    		findViewById(R.id.btnAddQuest).setOnClickListener(this);
	    		//チェックボックスのリスナー。(設定した問題を使うかどうか。)
	    		checkBCstQst.setOnCheckedChangeListener(new OnCheckedChangeListener(){

	    			@Override
	    			public void onCheckedChanged(CompoundButton buttonView,
	    					boolean isChecked) {
	    				if(checkBCstQst.isChecked()==true){
	    					GameConstant.questKind="CustomQuest";
	    				}
	    			}
	    			
	    		});
	    		//ラジオボタンのリスナー。(プレーヤー人数の選択。)
	    		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	    			
	    			@Override
	    			public void onCheckedChanged(RadioGroup group, int checkedId) {
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
	    				default:
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
