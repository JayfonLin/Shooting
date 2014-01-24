package com.android13.shooting.res;

import com.android13.shooting.Game;
import com.android13.shooting.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

/**
 * 管理游戏的位图资源
 * 
 * @author Tiga <liangkangabc@gmail.com>
 * 
 */
public class BitmapPool {

	private static SparseArray<Bitmap> pool = null;

	public static void loadAll(Context context) {
		pool = new SparseArray<Bitmap>();

		Resources res = context.getResources();
		/** 先载入原图，再根据屏幕规格进行缩放，并存入pool中 */
		// 篮板
		Bitmap bb = BitmapFactory.decodeResource(res, R.drawable.backboard);
		pool.put(R.drawable.backboard, Bitmap.createScaledBitmap(bb, (int) Game.Constant.BACKBOARD_WIDHT, (int) Game.Constant.BACKBOARD_HEIGHT, true));
		bb.recycle();
		bb = null;
		// 背景
		Bitmap bg = BitmapFactory.decodeResource(res, R.drawable.background);
		pool.put(R.drawable.background, Bitmap.createScaledBitmap(bg, (int) Game.Constant.SCREEN_WIDTH, (int) Game.Constant.SCREEN_HEIGHT, true));
		bg.recycle();
		bg = null;
		// 篮筐
		for (int i = 0; i < 5; ++i) {
			Bitmap hoop = BitmapFactory.decodeResource(res, R.drawable.hoop00 + i);
			pool.put(R.drawable.hoop00 + i, Bitmap.createScaledBitmap(hoop, (int) Game.Constant.HOOP_WIDTH, (int) Game.Constant.HOOP_HEIGHT, true));
			hoop.recycle();
			hoop = null;
		}
		
		// 篮球
		for (int i = 0; i < 5; ++i) {
			Bitmap ball = BitmapFactory.decodeResource(res, R.drawable.ball000 + i);
			pool.put(R.drawable.ball000 + i,
					Bitmap.createScaledBitmap(ball, (int) Game.Constant.BALL_RADIUS * 2, (int) Game.Constant.BALL_RADIUS * 2, true));
			ball.recycle();
			ball = null;
		}

		// 叶子
		Bitmap leaf = BitmapFactory.decodeResource(res, R.drawable.leaf);
		pool.put(R.drawable.leaf, Bitmap.createScaledBitmap(leaf, (int) Game.Constant.LEAF_WIDTH, (int) Game.Constant.LEAF_HEIGHT, true));
		leaf.recycle();
		leaf = null;
	}

	public static Bitmap getBitmap(int what) {
		if (null == pool)
			return null;
		return pool.get(what);
	}
}
