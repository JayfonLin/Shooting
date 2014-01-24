package com.android13.shooting.screenItems;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.android13.shooting.Game;
import com.android13.shooting.R;
import com.android13.shooting.res.BitmapPool;

public class Leaf extends ScreenItem {

	public Bitmap leafBitmap;
	// 叶子的旋转角度
	private float rotation;
	// 叶子的水平速度
	private float speedX;

	// 叶子的垂直速度
	private float speedY;

	// 叶子的旋转速度
	private float rotateSpeed;
	private Random random;

	public Leaf() {
		random = new Random();
		this.x = random.nextInt((int) Game.Constant.SCREEN_WIDTH);
		this.y = random.nextInt((int) Game.Constant.SCREEN_HEIGHT);
		this.z = Game.Constant.NEAREST;
		leafBitmap = BitmapPool.getBitmap(R.drawable.leaf);
		this.rotateSpeed = random.nextFloat() * 20 + 10;
		this.speedX = 0;
		speedY = random.nextFloat() * 12f - 2f;
	}

	@Override
	protected void logic() {
		// Log.d("LeafSpeed", Float.toString(speedX));
		this.x += this.speedX * 3f;
		this.y += this.speedY;
		if (x > Game.Constant.SCREEN_WIDTH) {
			x = 0;
		} else if (x < 0) {
			x = Game.Constant.SCREEN_WIDTH;
		}
		if (y > Game.Constant.SCREEN_HEIGHT) {
			y = 0;
		} else if (y < 0) {
			y = Game.Constant.SCREEN_HEIGHT;
		}
		this.rotation += this.rotateSpeed;
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		logic();
		canvas.save();
		canvas.rotate(this.rotation, this.x, this.y);
		canvas.drawBitmap(leafBitmap, this.x, this.y, paint);
		canvas.restore();
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}
}
