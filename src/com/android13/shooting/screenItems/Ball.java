package com.android13.shooting.screenItems;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.android13.shooting.Game;
import com.android13.shooting.PlaySound;
import com.android13.shooting.R;
import com.android13.shooting.res.BitmapPool;

/**
 * 篮球
 * 
 * @author Tiga <liangkangabc@gmail.com>
 * 
 */
public class Ball extends ScreenItem {
	private int currentFrame;
	private float speedX, speedY, speedZ;
	/**
	 * 篮球滚回来时Z方向的默认速度
	 */
	private static final float BACK_DEFAULT_SPEEDZ = -200f;
	/**
	 * 篮球没投出去的标识
	 */
	public boolean unShoot;
	/**
	 * 投篮得分的标识，和投篮得分标识的临时变量
	 */
	public boolean isGoal, temp_is_goal;
	// unGoal;
	/**
	 * 碰到篮框
	 */
	public boolean collideHoop;
	/**
	 * 碰到地面
	 */
	public boolean collideGround;
	/**
	 * 碰到场地中间的线
	 */
	public boolean collideMiddle;
	/**
	 * Y方向速度的改变值
	 */
	private float changeSpeedY;
	/**
	 * 篮球的放缩因子
	 */
	private float scaleFactor;
	/**
	 * 篮球往下掉
	 */
	public boolean isDownWard;
	/**
	 * 获得篮框的一个实例
	 */
	public Hoop hoop = Hoop.getInstance();
	/**
	 * 篮球投出去的最高点Y值
	 */
	public float highestY;
	/**
	 * 每一帧，所有篮球入篮的计数，更新得分之后要马上重置为0
	 */
	static int all_goal_count = 0;

	/**
	 * @return 每一帧球入篮的计数
	 */
	public static int goal_count() {
		return all_goal_count;
	}

	/**
	 * 更新得分后马上调用此函数重置计数器
	 */
	public static void reset_goal_count() {
		all_goal_count = 0;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}

	public float getSpeedZ() {
		return speedZ;
	}

	public void setSpeedZ(float speedZ) {
		this.speedZ = speedZ;
	}

	public Ball() {
		unShoot = true;
		isGoal = false;
		// unGoal = false;
		temp_is_goal = false;
		collideHoop = false;
		collideGround = false;
		collideMiddle = false;
		changeSpeedY = 0;
		scaleFactor = 1f; // 放缩因子，默认为1
		isDownWard = false;
		highestY = Game.Constant.SCREEN_HEIGHT;
		this.x = Game.Constant.SCREEN_WIDTH / 2;
		this.y = Game.Constant.SCREEN_HEIGHT - Game.Constant.BALL_RADIUS;
		this.z = Game.Constant.NEAREST;
		bmps = new Bitmap[5];
		for (int i = 0; i < 5; ++i)
			bmps[i] = BitmapPool.getBitmap(R.drawable.ball000 + i);

		bmpWidth = bmps[0].getWidth();
		bmpHeight = bmps[0].getHeight();
		currentFrame = 0;
		speedX = speedY = speedZ = 0;
	}

	/**
	 * 篮球出界回到初始位置，所有的变量都重新初始化
	 */
	public void reset() {

		unShoot = true;
		isGoal = false;
		// unGoal = false;
		temp_is_goal = false;
		collideHoop = false;
		collideGround = false;
		collideMiddle = false;
		changeSpeedY = 0;
		scaleFactor = 1f; // 放缩因子，默认为1
		isDownWard = false;
		highestY = Game.Constant.SCREEN_HEIGHT;
		this.x = Game.Constant.SCREEN_WIDTH / 2;
		this.y = Game.Constant.SCREEN_HEIGHT - Game.Constant.BALL_RADIUS;
		this.z = Game.Constant.NEAREST;
		currentFrame = 0;
		speedX = speedY = speedZ = 0;
	}

	protected void logic(Canvas canvas) {
		if (x + Game.Constant.BALL_RADIUS * scaleFactor <= 0
				|| x - Game.Constant.BALL_RADIUS * scaleFactor >= Game.Constant.SCREEN_WIDTH
				|| y - Game.Constant.BALL_RADIUS * scaleFactor >= Game.Constant.SCREEN_HEIGHT) {
			if (!temp_is_goal) {
				hoop.goalCount = 0;
				hoop.missCount++;
			}

			reset();
			return;
		}

		if (!unShoot) {
			changeSpeedY += (Game.Constant.GRAVITY * Game.Constant.MOVE_TIME);
			speedY += changeSpeedY;
			isDownWard = (speedY > 0f) ? true : false;
			currentFrame = (currentFrame + 1) % 5;
			if (isDownWard) {
				/**
				 * 与篮框内侧碰撞
				 */
				if (highestY < Game.Constant.BACKBOARD_Y) {
					float bevel_edge, // 斜边，即球心与特定点的直线距离
					dx, // X轴方向的距离
					dy; // Y轴方向的距离

					float speed = (float) Math.sqrt((double) speedX * speedX
							+ speedY * speedY) * 0.7f;
					// 检测碰撞，篮框最左边
					if ((bevel_edge = calculate_distance(x, y, hoop.getX()
							- Game.Constant.HOOP_RADIUS * hoop.scaleFactor,
							Game.Constant.TOP_HOOP_PY)) <= Game.Constant.BALL_RADIUS
							* scaleFactor) {
						playSound();
						collideHoop = true;
						dx = x
								- (hoop.getX() - Game.Constant.HOOP_RADIUS
										* hoop.scaleFactor);
						dy = y - Game.Constant.TOP_HOOP_PY;
						// 按入射方向的反方向弹回
						speedX = speed * (dx / bevel_edge);
						speedY = speed * (dy / bevel_edge);
						speedZ = 0;
					}
					// 检测碰撞，篮框最右边
					else if ((bevel_edge = calculate_distance(x, y, hoop.getX()
							+ Game.Constant.HOOP_RADIUS * hoop.scaleFactor,
							Game.Constant.TOP_HOOP_PY)) <= Game.Constant.BALL_RADIUS
							* scaleFactor) {
						playSound();
						collideHoop = true;
						dx = x
								- (hoop.getX() + Game.Constant.HOOP_RADIUS
										* hoop.scaleFactor);
						dy = y - Game.Constant.TOP_HOOP_PY;
						// 按入射方向的反方向弹回
						speedX = speed * (dx / bevel_edge);
						speedY = speed * (dy / bevel_edge);
						speedZ = 0;
					}
					// 判断是否投篮得分
					if (!temp_is_goal
							&& (bevel_edge = calculate_distance(x, y,
									hoop.getX(), Game.Constant.HOOP_Y)) <= Game.Constant.BALL_RADIUS) {
						temp_is_goal = isGoal = true;
						speedZ = 0;
					}
				}

				/**
				 * 与篮框外侧碰撞
				 */
				else {

					float bevel_edge, dx, dy;
					float speed = (float) Math.sqrt((double) speedX * speedX
							+ speedY * speedY) * 0.7f;
					/**
					 * 与篮框外侧左边碰撞
					 */
					if ((bevel_edge = calculate_distance(x, y, hoop.getX()
							- Game.Constant.HOOP_RADIUS * hoop.scaleFactor,
							Game.Constant.TOP_HOOP_PY)) <= Game.Constant.BALL_RADIUS
							* scaleFactor
							&& !collideHoop) {
						playSound();
						collideHoop = true;
						speedX = -Math.abs(speed * 0.707f);
						speedY = speed * 0.707f;
						speedZ = -Math.abs(speedZ);

					}
					/**
					 * 与篮框外侧右边碰撞
					 */
					else if ((bevel_edge = calculate_distance(x, y, hoop.getX()
							+ Game.Constant.HOOP_RADIUS * hoop.scaleFactor,
							Game.Constant.TOP_HOOP_PY)) <= Game.Constant.BALL_RADIUS
							* scaleFactor
							&& !collideHoop) {
						playSound();
						collideHoop = true;
						speedX = Math.abs(speed * 0.707f);
						speedY = speed * 0.707f;
						speedZ = -Math.abs(speedZ);
					}
					/**
					 * 与篮框外侧中间碰撞
					 */
					else if ((bevel_edge = calculate_distance(x, y,
							Game.Constant.MIDDLE_HOOP_PX,
							Game.Constant.TOP_HOOP_PY)) <= Game.Constant.BALL_RADIUS
							* scaleFactor
							&& !collideHoop) {
						playSound();
						collideHoop = true;
						dx = hoop.getX() + Game.Constant.HOOP_RADIUS
								* hoop.scaleFactor - x;
						dy = Game.Constant.TOP_HOOP_PY - y;
						speedX = speed * (dx / bevel_edge);
						speedY = speed * (dy / bevel_edge);
						speedZ = -Math.abs(speedZ);
					}
				}
				/**
				 * 与场地上边界碰撞
				 */
				if ((temp_is_goal || !collideHoop)
						&& isDownWard
						&& !collideGround
						&& y < Game.Constant.COURT_UPPER_BOUND
						&& Game.Constant.COURT_UPPER_BOUND - y <= Game.Constant.BALL_RADIUS
								* scaleFactor) {
					playSound();
					speedY = -(Math.abs(speedY * 0.7f));
					speedZ = BACK_DEFAULT_SPEEDZ;
					collideGround = true;
				}
				/**
				 * 与场地中间线碰撞
				 */
				if (!collideMiddle
						&& isDownWard
						&& y < Game.Constant.COURT_MIDDLE_BOUND
						&& Game.Constant.COURT_MIDDLE_BOUND - y <= Game.Constant.BALL_RADIUS
								* scaleFactor) {
					playSound();
					collideMiddle = true;
					speedY = -(Math.abs(speedY * 0.7f));
					speedZ = BACK_DEFAULT_SPEEDZ;
					collideGround = true;
				}
			}
		}

		x += speedX * Game.Constant.MOVE_TIME;
		y += speedY * Game.Constant.MOVE_TIME;
		z += speedZ * Game.Constant.MOVE_TIME;
		if (!unShoot) {
			speedX += Wind.getInstance().getSpeedX();
		}
		if (y < highestY) {
			highestY = y;
		}

		// 根据篮球所在z轴位置，设置放缩因子
		scaleFactor = (Game.Constant.SCREEN_HEIGHT - z)
				/ Game.Constant.SCREEN_HEIGHT;
		// System.out.println(hoop.goalCount + " " + hoop.missCount);
		if (isGoal) {
			isGoal = false;
			hoop.isGoal = true;
			Log.i("lin", "goal");
			hoop.goalCount++;
			hoop.missCount = 0;
			all_goal_count++;
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		logic(canvas);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleFactor, scaleFactor);

		canvas.drawBitmap(Bitmap.createBitmap(bmps[currentFrame], 0, 0,
				bmpWidth, bmpHeight, matrix, true), x - bmpWidth * scaleFactor
				/ 2, y - bmpHeight * scaleFactor / 2, paint);
	}

	public float calculate_distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

	void playSound() {
		if (Game.Constant.SOUND_EFFECT_ON){
			if (PlaySound.soundPool == null) {
				System.out.println("error");
			} else {
				PlaySound.play("ball", 0);
			}
		}
	}
}
