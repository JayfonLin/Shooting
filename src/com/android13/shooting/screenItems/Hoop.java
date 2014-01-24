package com.android13.shooting.screenItems;

import com.android13.shooting.Game;
import com.android13.shooting.R;
import com.android13.shooting.res.BitmapPool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * 篮圈，Singleton
 * 
 * @author Tiga <liangkangabc@gmail.com>
 * 
 */
public class Hoop extends ScreenItem {

	private int currentFrame;
	/**
	 * 移动方向：0 不动
	 *          1 向左
	 *          2 向右
	 */
	public int move_direction;
	public float speedX;
	private static Hoop instance;
	public float scaleFactor;
	/**
	 * 投篮得分
	 */
	public boolean isGoal = false;
	public int goalCount = 0;
	public int missCount = 0;
	private boolean flag = false;
	private long start = 0, end = 0;
	private long biggerTimer = 0, smallerTimer = 0;
	private long lastTime = System.currentTimeMillis();
	private boolean isBigger = false;
	private boolean isSmaller = false;
	
	
	public static Hoop getInstance() {
		if (instance == null) {
			synchronized (Hoop.class) {
				if (instance == null)
					instance = new Hoop();
			}
		}
		return instance;
	}

	private Hoop() {
		this.x = Game.Constant.HOOP_X;
		this.y = Game.Constant.HOOP_Y;
		this.z = Game.Constant.FARTHEST - Game.Constant.HOOP_RADIUS;
		move_direction = 0;
		speedX = 1f;
		scaleFactor = 1f;
		bmps = new Bitmap[5];
		for (int i = 0; i < 5; ++i)
			bmps[i] = BitmapPool.getBitmap(R.drawable.hoop00 + i);

		bmpWidth = bmps[0].getWidth();
		bmpHeight = bmps[0].getHeight();

		currentFrame = 0;
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		logic();

		long curTimer = System.currentTimeMillis();
		if (isBigger) {
			biggerTimer += curTimer - lastTime;
			if (biggerTimer <= 5000) {
				scaleFactor = 1.2f;
				Matrix matrix = new Matrix();
				matrix.postScale(scaleFactor, scaleFactor);

				canvas.drawBitmap(Bitmap.createBitmap(bmps[currentFrame], 0, 0,
						bmpWidth, bmpHeight, matrix, true), 
						x - bmpWidth*scaleFactor/2
						, y - bmpHeight/2, paint);
			} else {
				scaleFactor = 1f;
				isBigger = false;
				canvas.drawBitmap(bmps[currentFrame], x - bmpWidth / 2, y
						- bmpHeight / 2, paint);
			}
		} else if (isSmaller) {
			smallerTimer += curTimer - lastTime;
			if (smallerTimer <= 5000) {
				scaleFactor = 0.9f;
				Matrix matrix = new Matrix();
				matrix.postScale(scaleFactor, scaleFactor);

				canvas.drawBitmap(Bitmap.createBitmap(bmps[currentFrame], 0, 0,
						bmpWidth, bmpHeight, matrix, true),
						x - bmpWidth * scaleFactor / 2,
						y - bmpHeight / 2, paint);
			} else {
				scaleFactor = 1f;
				isSmaller = false;
				canvas.drawBitmap(bmps[currentFrame], x - bmpWidth / 2, y
						- bmpHeight / 2, paint);
			}
		} else {
			canvas.drawBitmap(bmps[currentFrame], x - bmpWidth / 2, y
					- bmpHeight / 2, paint);
		}
		lastTime = System.currentTimeMillis();
	}

	@Override
	protected void logic() {

		// 得分
		if (isGoal) {
			// 马上切换篮网状态帧
			if (currentFrame == 0) {
				currentFrame++;
			}
			if (!flag) {
				start = System.currentTimeMillis();
				flag = true;
			} else {
				end = System.currentTimeMillis();
				// 每隔10毫秒切换一帧
				if (end - start > 10) {
					flag = false;
					currentFrame = currentFrame + 1;
					if (currentFrame == 5) {
						currentFrame = 0;
						isGoal = false;
					}
				}
			}
		}

		// 2014年1月21日14:25:15 如果大于第二关开始检测连续投进或者连续丢分
		if (Game.getLevel() >= 2) {
			if (goalCount >= 3) {
				goalCount = 0;
				isBigger = true;
				biggerTimer = 0;
			}

			if (missCount >= 5) {
				missCount = 0;
				isSmaller = true;
				smallerTimer = 0;
			}
		}
		
		//如果是第3关及以上的关卡，篮筐左右移动
		if (Game.getLevel() >= 3){
			if (move_direction == 0){
				move_direction = 1;
			}else if (move_direction == 1){
				if (x <= Game.Constant.SCREEN_WIDTH/4+Game.Constant.BACKBOARD_WIDHT/4){
					move_direction = 2;
				}
			}else if (move_direction == 2){
				if (x >= Game.Constant.SCREEN_WIDTH*3/4-Game.Constant.BACKBOARD_WIDHT/4){
					move_direction = 1;
				}
			}
			
			if (move_direction == 1){
				x -= speedX;
			}else if (move_direction == 2){
				x += speedX;
			}
		}
		
		
	}
	
	@Override
	public void release() {
		instance = null;
		super.release();
	}
}
