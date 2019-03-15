package enemy.lowerEnemy;

import bullet.heroBullet.HeroBullet;
import interest.sidney.main.Bang;
import interest.sidney.main.InitXY;
import superClass.FlyingObject;

/**
 * 低级敌人类
 * 该类型敌人不会射击，没有血量，没有奖励
 * 
 * 该类在该项目中主要用于提供低级敌人的向上造型
 * 
 * @author Sidney
 *
 */
public abstract class LowerEnemy extends FlyingObject implements Bang,InitXY  {
	/**
	 * 构造器
	 * @param width
	 * @param height
	 * @param speed
	 */
	public LowerEnemy(int width, int height, int speed) {
		super(width, height, speed);
	}

	/**
	 * 重载初始化坐标方法，用于需要在进入游戏前指定坐标的情况
	 * @param x
	 * @param y
	 */
	public LowerEnemy initXY(int x, int y) {
		setX(x);
		setY(y);
		return this;
	}
	

	/**
	 * 与英雄机子弹的碰撞动作
	 */
	public void bang(HeroBullet bullet) {
		goDead();
	}
}
