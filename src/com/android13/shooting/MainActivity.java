package com.android13.shooting;

import java.io.IOException;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * 游戏的入口Activity
 * 
 * @author Tiga <liangkangabc@gmail.com>
 * 
 */
public class MainActivity extends Activity {

	private MediaPlayer mediaPlayer;
	SurfaceView mainSurfaceView;
	PopupWindow mPopWin;
	ImageView resumeIV, replayIV;
	Bundle initial_state;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initial_state = savedInstanceState;
		PlaySound.init(this, 5, AudioManager.STREAM_MUSIC, 0);
		mediaPlayer = MediaPlayer.create(getApplicationContext(),
				R.raw.music_game);
		mediaPlayer.setLooping(true);
		startMusic();

		Intent intent = getIntent();
		int level = intent.getIntExtra("level", 1);
		// 2014年1月21日14:26:46 以第几关初始化游戏
		Game.init(this, level);
		mainSurfaceView = new MainSurfaceView(this);
		setContentView(mainSurfaceView);

	}

	private void startMusic() {
		if (Game.Constant.GAME_MUSIC_ON){
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}

			try {
				mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mediaPlayer.start();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onPause();
			
			
			LinearLayout popWin_layout = (LinearLayout)MainActivity.this.
					getLayoutInflater().inflate(R.layout.pause_popwin_layout,null);
			mPopWin = new PopupWindow(popWin_layout, 
					LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

			//mPopWin.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.background));
			mPopWin.setOutsideTouchable(true);

			mPopWin.setFocusable(true);
			mPopWin.showAtLocation(mainSurfaceView, Gravity.CENTER, 0, 0);

			mPopWin.update();
			resumeIV = (ImageView) popWin_layout.findViewById(R.id.resumeIV);
			resumeIV.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					closePopWin();
					onResume();
				}
			});
			replayIV = (ImageView) popWin_layout.findViewById(R.id.replayIV);
			replayIV.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					closePopWin();
					Game.release();
					mediaPlayer.stop();
					finish();
				}
			});
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		if (Game.Constant.GAME_MUSIC_ON){
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
			
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Game.Constant.GAME_PAUSE = true;
		if (Game.Constant.GAME_MUSIC_ON){
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		Game.Constant.GAME_PAUSE = false;
		if (Game.Constant.GAME_MUSIC_ON) {
			if (!mediaPlayer.isPlaying()) {
				startMusic();
			}
		}
		super.onResume();
	}
	
	private void closePopWin(){
		if (mPopWin != null && mPopWin.isShowing()) {
			mPopWin.dismiss();
		}
	}
	
	
}
