package com.android13.shooting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


// 2014年1月21日14:25:51  游戏的选关界面，先留着，简陋点
public class LevelSelector extends Activity {
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	PopupWindow mPopWin;
	LinearLayout ll;
	ImageView settingsIV;
	ImageView sureIV, cancelIV, game_music_IV, sound_effect_IV;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ll = (LinearLayout)this.getLayoutInflater().inflate(R.layout.level_selector,null); 
		setContentView(ll);
		sharedPreferences = getSharedPreferences("audiosettings", MODE_PRIVATE);
		editor = sharedPreferences.edit();
		Game.Constant.GAME_MUSIC_ON = sharedPreferences.getBoolean("game_music_on", true);
		Game.Constant.SOUND_EFFECT_ON = sharedPreferences.getBoolean("sound_effect_on", true);

		Button level_1 = (Button) this.findViewById(R.id.button1);
		Button level_2 = (Button) this.findViewById(R.id.button2);
		Button level_3 = (Button) this.findViewById(R.id.button3);
		Button level_4 = (Button) this.findViewById(R.id.button4);
		settingsIV = (ImageView) findViewById(R.id.settingsimageview);

		level_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LevelSelector.this, MainActivity.class);
				intent.putExtra("level", 1);
				startActivity(intent);
			}
		});
		level_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LevelSelector.this, MainActivity.class);
				intent.putExtra("level", 2);
				startActivity(intent);
			}
		});
		level_3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LevelSelector.this, MainActivity.class);
				intent.putExtra("level", 3);
				startActivity(intent);
			}
		});
		level_4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LevelSelector.this, MainActivity.class);
				intent.putExtra("level", 4);
				startActivity(intent);
			}
		});
		settingsIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinearLayout popWin_layout = (LinearLayout)LevelSelector.this.
						getLayoutInflater().inflate(R.layout.audio_settings_layout,null);
				mPopWin = new PopupWindow(popWin_layout, 
						LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

				//mPopWin.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.background));
				mPopWin.setOutsideTouchable(true);

				mPopWin.setFocusable(true);
				mPopWin.showAtLocation(ll, Gravity.CENTER, 0, 0);

				mPopWin.update();
				
				sureIV = (ImageView)popWin_layout.findViewById(R.id.image_sure);
				cancelIV = (ImageView) popWin_layout.findViewById(R.id.image_cancel);
				game_music_IV = (ImageView) popWin_layout.findViewById(R.id.image_game_music);
				sound_effect_IV = (ImageView) popWin_layout.findViewById(R.id.image_sound_effect);
				changeDrawable();
				sureIV.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						editor.commit();
						closePopWin();
					}
				});
				cancelIV.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						closePopWin();
					}
				});
				game_music_IV.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (Game.Constant.GAME_MUSIC_ON){
							Game.Constant.GAME_MUSIC_ON = false;
						}else{
							Game.Constant.GAME_MUSIC_ON = true;
						}
						editor.putBoolean("game_music_on", Game.Constant.GAME_MUSIC_ON);
						
						changeDrawable();
					}
				});
				sound_effect_IV.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (Game.Constant.SOUND_EFFECT_ON){
							Game.Constant.SOUND_EFFECT_ON = false;
						}else{
							Game.Constant.SOUND_EFFECT_ON = true;
						}
						editor.putBoolean("sound_effect_on", Game.Constant.SOUND_EFFECT_ON);
						changeDrawable();
					}
				});
			}
		});
	}
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			
		}
		return true;
	}*/
	
	private void closePopWin(){
		if (mPopWin != null && mPopWin.isShowing()) {
			mPopWin.dismiss();
		}
	}
	
	private void changeDrawable(){
		if (Game.Constant.GAME_MUSIC_ON)
			game_music_IV.setImageDrawable(getResources().getDrawable(R.drawable.volume_on));
		else
			game_music_IV.setImageDrawable(getResources().getDrawable(R.drawable.volume_off));
		if (Game.Constant.SOUND_EFFECT_ON)
			sound_effect_IV.setImageDrawable(getResources().getDrawable(R.drawable.volume_on));
		else 
			sound_effect_IV.setImageDrawable(getResources().getDrawable(R.drawable.volume_off));
	}
}
