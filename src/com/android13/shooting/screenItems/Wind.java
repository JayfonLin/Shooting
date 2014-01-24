package com.android13.shooting.screenItems;

import java.util.Iterator;
import java.util.Vector;

import com.android13.shooting.Game;
import com.android13.shooting.PlaySound;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Wind extends ScreenItem {

	public final static int WIND_STOP = 0;
	public final static int WIND_DIRECTION_RIGHT = 1;
	public final static int WIND_DIRECTION_LEFT = 2;

	private int state;
	private float speedX;
	private long timer;
	private long soundTimer;
	private long postTime;
	private Vector<Leaf> leaves;

	private static Wind instance;

	// 实现单例模式
	public static Wind getInstance() {
		if (instance == null) {
			synchronized (Wind.class) {
				if (instance == null)
					instance = new Wind();
			}
		}
		return instance;
	}

	// 设定当前风速
	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	// 获取当前风速
	public float getSpeedX() {
		return this.speedX;
	}

	private Wind() {
		state = WIND_STOP;
		this.speedX = 0;
		leaves = new Vector<Leaf>();
		for (int i = 0; i < 10; i++) {
			leaves.add(new Leaf());
		}
		postTime = System.currentTimeMillis();
		timer = 0;
		soundTimer = 0;
	}

	// 启动风这个对象，初始方向是自左向右
	public void windBegin(float speedX) {
		this.speedX = speedX;
		state = WIND_DIRECTION_RIGHT;
		Iterator<Leaf> iter = leaves.iterator();
		while (iter.hasNext()) {
			Leaf leaf = iter.next();
			leaf.setSpeedX(this.speedX);
		}
	}

	// 停止吹风，树叶暂时没处理
	public void windStop() {
		state = WIND_STOP;
	}

	@Override
	protected void logic() {
		long curTime = System.currentTimeMillis();
		timer += curTime - postTime;
		soundTimer += curTime - postTime;
		if (timer >= 10000) {
			timer = 0;
			if (state != WIND_STOP) {
				change_direction();
			}
		}
		if (soundTimer >= 3000) {
			soundTimer = 0;
			if (Game.Constant.SOUND_EFFECT_ON) {
				if (PlaySound.soundPool == null) {
					System.out.println("error");
				} else if (state != WIND_STOP) {
					PlaySound.play("wind", 0);
				}
			}
		}

		postTime = curTime;
	}

	private void change_direction() {
		if (state == WIND_DIRECTION_RIGHT) {
			state = WIND_DIRECTION_LEFT;
		} else if (state == WIND_DIRECTION_LEFT) {
			state = WIND_DIRECTION_RIGHT;
		}
		speedX = -speedX;
		Iterator<Leaf> iter = leaves.iterator();
		while (iter.hasNext()) {
			Leaf leaf = iter.next();
			leaf.setSpeedX(this.speedX);
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		logic();

		if (!(state == WIND_STOP)) {
			Iterator<Leaf> iter = leaves.iterator();
			while (iter.hasNext()) {
				Leaf leaf = iter.next();
				leaf.draw(canvas, paint);
			}
		}
	}

	@Override
	public void release() {
		instance = null;
		super.release();
	}

}
