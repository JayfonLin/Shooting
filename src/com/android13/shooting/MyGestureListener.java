package com.android13.shooting;

import java.util.ArrayList;
import java.util.List;
import com.android13.shooting.Game.Constant;
import com.android13.shooting.screenItems.Ball;
import com.android13.shooting.screenItems.Hoop;
import com.android13.shooting.screenItems.ScreenItem;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
	MainSurfaceView mainSurfaceView;
	List<ScreenItem> sortedItems = Game.getSortedItems();
	Hoop hoop = Hoop.getInstance();
	int itemNum = sortedItems.size();
	final float initialSpeed = -20f;
	ArrayList<Ball> balls = new ArrayList<Ball>();
	public MyGestureListener(MainSurfaceView pMainSurfaceView){
		super();
		mainSurfaceView = pMainSurfaceView;
		balls.add((Ball) sortedItems.get(itemNum-1));
		balls.add((Ball) sortedItems.get(itemNum-2));
		balls.add((Ball) sortedItems.get(itemNum-3));
	}
	@Override
	public boolean onDown(MotionEvent e) {
		return super.onDown(e);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		for (Ball ball: balls){
			//判断按下的点是不是在篮球内部
			if (Math.abs((double)e1.getX() - ball.getX()) < Constant.BALL_RADIUS &&
					Math.abs((double)e1.getY() - ball.getY()) < Constant.BALL_RADIUS){
				//是，则判断篮球是否已经投出
				if (ball.unShoot){
					//根据滑动的距离给篮球设置初始速度
					ball.setSpeedX((e2.getX()-e1.getX())*0.3f);
					//Y,Z轴方向的合速度
					float speedYZ = ((e2.getY()-e1.getY())) * 3f;
					/**
					 * 投篮速度不能超过阀值
					 */
					if (speedYZ < 0 && speedYZ < Game.Constant.BOUND_VELOCITY){
						speedYZ = Game.Constant.BOUND_VELOCITY;
					}
					ball.setSpeedY((float) (speedYZ*Math.cos(Game.Constant.ALPHA)));
					ball.setSpeedZ(-(float) (speedYZ*Math.sin(Game.Constant.ALPHA))*1.15f);
					
					ball.unShoot = false;
				}
				return true;
			}
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		super.onLongPress(e);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return super.onSingleTapUp(e);
	}

}
