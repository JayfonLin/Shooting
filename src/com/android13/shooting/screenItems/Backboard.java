package com.android13.shooting.screenItems;

import com.android13.shooting.Game;
import com.android13.shooting.R;
import com.android13.shooting.res.BitmapPool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 篮板，Singleton
 * @author Tiga <liangkangabc@gmail.com>
 *
 */
public class Backboard extends ScreenItem {
	
	private static Backboard instance;
	public static Backboard getInstance() {
		if(instance == null) {
			synchronized (Backboard.class) {
				if(instance == null)
					instance = new Backboard();
			}
		}
		return instance;
	}
	
	private Backboard() {
		this.x = Game.Constant.BACKBOARD_X;
		this.y = Game.Constant.BACKBOARD_Y;
		this.z = Game.Constant.FARTHEST;
		bmps = new Bitmap[1];
		bmps[0] = BitmapPool.getBitmap(R.drawable.backboard);
		
		bmpWidth = bmps[0].getWidth();
		bmpHeight = bmps[0].getHeight();
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bmps[0], x - bmpWidth / 2, y - bmpHeight / 2, paint);
	}
	
	@Override
	public void release() {
		instance = null;
		super.release();
	}
	
}
