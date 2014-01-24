package com.android13.shooting;

import android.view.View.OnTouchListener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

/**
 * 显示游戏主体画面的SurfaceView
 * @author Tiga <liangkangabc@gmail.com>
 *
 */
public class MainSurfaceView extends SurfaceView implements 
Callback, Runnable, OnTouchListener{
	
	private boolean flag;
	
	private SurfaceHolder surfaceHolder;
	private Canvas canvas;
	private Paint paint;
	private MyGestureListener myGestureListener;
	private GestureDetector gestureDetector;
	private Thread thread;

	public MainSurfaceView(Context context) {
		super(context);
		
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		myGestureListener = new MyGestureListener(this);
		gestureDetector = new GestureDetector(context, myGestureListener);
		setLongClickable(true);
		setClickable(true);
		setOnTouchListener(this);
		setKeepScreenOn(true);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		flag = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}
	
	@Override
	public void run() {
		while(flag) {
			if (!Game.Constant.GAME_PAUSE){
				long start = System.currentTimeMillis();
	
				Game.refreshScreen(surfaceHolder, canvas, paint);
				
				long end = System.currentTimeMillis();
				try {
					//控制FPS 为标准的 30
					if(end - start < 30)
						Thread.sleep(30 - (end - start));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return false;
	}

}
