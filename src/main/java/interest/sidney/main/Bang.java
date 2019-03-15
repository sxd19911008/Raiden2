package interest.sidney.main;

import bullet.heroBullet.HeroBullet;
import superClass.FlyingObject;

/**
 * 碰撞动作接口
 * 为低级敌人和高级敌人提供共同的向上造型，执行碰撞动作
 * 
 * @author Sidney
 *
 */
public interface Bang {
	/**
	 * 处理碰撞
	 * @param bullet 英雄机子弹
	 */
	void bang(HeroBullet bullet);
	
	/**
	 * 检查是否碰撞
	 * @param other 其他的飞行物
	 * @return true碰撞，fasle未碰撞
	 */
	boolean hit(FlyingObject other);
	
	/**
	 * 检查是否存活
	 * @return true存活，false死亡
	 */
	boolean isAlive();
}
