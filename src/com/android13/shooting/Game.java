package com.android13.shooting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.android13.shooting.res.BitmapPool;
import com.android13.shooting.screenItems.Backboard;
import com.android13.shooting.screenItems.Background;
import com.android13.shooting.screenItems.Ball;
import com.android13.shooting.screenItems.Hoop;
import com.android13.shooting.screenItems.ScreenItem;
import com.android13.shooting.screenItems.Wind;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;

/**
 * 游戏控制类，负责游戏整体的逻辑调度
 * 
 * @author Tiga <liangkangabc@gmail.com>
 * 
 */
public class Game {

	/** 根据 z 坐标排序 */
	private static List<ScreenItem> sortedItems;
	private static int level;
	public static int goal_count = 0;
	// public static ArrayList<Ball> balls;
	public static int getLevel() {
		return level;
	}

	public static void init(Activity activity, int lv) {

		level = lv;
		/** 根据屏幕实际参数，初始化所有距离相关的常量 */
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		Constant.init(dm.widthPixels, dm.heightPixels);

		/** 一次加载所有图片资源 */
		BitmapPool.loadAll(activity);
		// balls = new ArrayList<Ball>();
		sortedItems = new ArrayList<ScreenItem>();
		sortedItems.add(Background.getInstance());
		sortedItems.add(Backboard.getInstance());
		sortedItems.add(Hoop.getInstance());
		sortedItems.add(Wind.getInstance());
		if (level >= 4) {
			Wind.getInstance().windBegin(Constant.WIND_SPEED);
		}
		// 三个篮球
		for (int i = 0; i < 3; i++) {
			Ball ball = new Ball();
			sortedItems.add(ball);
			// balls.add(ball);
		}

	}

	public static List<ScreenItem> getSortedItems() {
		return sortedItems;
	}

	public static void refreshScreen(SurfaceHolder surfaceHolder,
			Canvas canvas, Paint paint) {

		gameLogic();

		try {
			canvas = surfaceHolder.lockCanvas();
			canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
					Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

			if (canvas != null) {
				for (ScreenItem item : sortedItems)
					item.draw(canvas, paint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null)
				surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	private static void gameLogic() {
		/** 根据 z 坐标对 ScreenItem 排序 */
		Collections.sort(sortedItems);
	}
	
	public static void release(){
		for(int i = 0; i < sortedItems.size() ; i++){
			sortedItems.get(i).release();
		}
	}
	
	/**
	 * 定义了游戏的很多全局常量，如视野内三维空间的坐标范围
	 */
	public static class Constant {

		private static void init(int sw, int sh) {
			SCREEN_WIDTH = sw;
			SCREEN_HEIGHT = sh;
			BACKBOARD_WIDHT = SCREEN_WIDTH / 2;
			BACKBOARD_HEIGHT = SCREEN_WIDTH / 3;
			BACKBOARD_X = SCREEN_WIDTH / 2;
			BACKBOARD_Y = SCREEN_HEIGHT / 3.5f;
			HOOP_WIDTH = BACKBOARD_WIDHT / 2.5f;
			HOOP_HEIGHT = HOOP_WIDTH;
			HOOP_X = SCREEN_WIDTH / 2;
			HOOP_Y = BACKBOARD_Y + BACKBOARD_HEIGHT / 2;
			HOOP_RADIUS = HOOP_WIDTH / 2;
			BALL_RADIUS = HOOP_RADIUS * 1.2f;
			FARTHEST = SCREEN_HEIGHT;
			NEAREST = 0;

			MIDDLE_HOOP_PX = HOOP_X;
			LEFT_HOOP_PX = MIDDLE_HOOP_PX - HOOP_RADIUS;
			LEFT_HOOP_MID_PX = MIDDLE_HOOP_PX - HOOP_RADIUS / 2;
			RIGHT_HOOP_MID_PX = MIDDLE_HOOP_PX + HOOP_RADIUS / 2;
			RIGHT_HOOP_PX = MIDDLE_HOOP_PX + HOOP_RADIUS;
			TOP_HOOP_PY = HOOP_Y - HOOP_HEIGHT / 2;

			float vy = (float) Math.pow(2 * GRAVITY * SCREEN_HEIGHT, 0.5f);
			BOUND_VELOCITY = -(float) (vy / Math.cos(ALPHA)) * 3.65f;
			MOVE_TIME = 0.07f;
			COURT_UPPER_BOUND = 12.6f / 16.0f * SCREEN_HEIGHT;
			COURT_MIDDLE_BOUND = COURT_UPPER_BOUND
					+ (SCREEN_HEIGHT - COURT_UPPER_BOUND) / 2f;

			LEAF_WIDTH = BALL_RADIUS / 2f;
			LEAF_HEIGHT = BALL_RADIUS / 2f;

			WIND_SPEED = 2f;
			GAME_PAUSE = false;
		}

		public static float WIND_SPEED;

		public static float SCREEN_WIDTH, SCREEN_HEIGHT;
		public static float BACKBOARD_WIDHT, BACKBOARD_HEIGHT, BACKBOARD_X,
				BACKBOARD_Y;
		public static float HOOP_WIDTH, HOOP_HEIGHT, HOOP_X, HOOP_Y,
				HOOP_RADIUS;
		public static float LEFT_HOOP_PX, LEFT_HOOP_MID_PX, MIDDLE_HOOP_PX,
				RIGHT_HOOP_MID_PX, RIGHT_HOOP_PX, TOP_HOOP_PY,
				COURT_UPPER_BOUND, // 场地上边缘
				COURT_MIDDLE_BOUND; // 场地中间
		public static float GRAVITY = 10f;
		/**
		 * 投篮的仰角
		 */
		public static float ALPHA = (float) (Math.PI / 18f);
		/**
		 * 投篮速度的阀值（最大）
		 */
		public static float BOUND_VELOCITY;
		/**
		 * 每帧，篮球对应的移动时间
		 */
		public static float MOVE_TIME;

		/**
		 * #Tips# 篮球在投向篮筐过程中，由于透视，在屏幕上看起来越来越小，BALL_RADIUS 为篮球投出前
		 * 显示的半径，透视效果应使得Ball在篮筐位置附近时半径小于Hoop的半径
		 */
		public static float BALL_RADIUS;
		/** 可见3维区域的 z 坐标范围，最远即为篮板的 z 坐标，最近即为篮球初始位置 */
		public static float FARTHEST, NEAREST;

		public static float LEAF_WIDTH, LEAF_HEIGHT;
		/** 背景音乐，开 */
		public static boolean GAME_MUSIC_ON;
		/** 音效，开 */
		public static boolean SOUND_EFFECT_ON;
		public static boolean GAME_PAUSE;
	}
}
