package enemy.seniorEnemy;

import bullet.heroBullet.HeroBullet;
import interest.sidney.main.*;
import superClass.FlyingObject;

/**
 * 高级敌人类
 * 
 * 该类继承自FlyingObject，是所有高级敌人的超类。
 * 高级敌人：能射击，有血量，x轴移动，击杀后有奖励。
 * 
 * 增加属性：
 * 血量、x轴速度、奖励类型
 * 
 * 增加方法：受伤掉血hurt
 * 
 * @author Sidney
 *
 */
public abstract class SeniorEnemy extends FlyingObject implements Bang {
	protected int blood;      //血量
	protected int xSpeed;     //x轴速度
	protected int xDirection; //x轴移动方向
	protected int shootIndex; //射击计时，每移动1步加1，到一个数值时归零并让对象射击

	/**
	 * 构造器
	 * @param width
	 * @param height
	 * @param speed
	 * @param blood
	 * @param xSpeed
	 */
	public SeniorEnemy(int width, int height, int speed, int blood, int xSpeed) {
		super(width, height, speed);
		this.blood = blood;
		this.xSpeed = xSpeed;
		this.xDirection = -1;
		this.shootIndex = 0;
	}
	
	/**
	 * 初始化x轴方向
	 */
	public void initXDirection() {
		this.xDirection = -2*World.random.nextInt(2)+1;
	}
	
	/**
	 * 初始化射击计时
	 */
	public void initShootIndex() {
		this.shootIndex = World.random.nextInt(200);
	}
	
	/**
	 * 初始化血量
	 */
	public abstract void initBlood();
	
	/**
	 * 受伤掉血
	 * @param firepower
	 */
	public void hurt(int firepower) {
		this.blood -= firepower;
		if(this.blood<1) {
			goDead();
		}
	}

	/**
	 * 与英雄机子弹的碰撞动作
	 */
	public void bang(HeroBullet bullet) {
		hurt(bullet.getFirePower());
	}
	
	/**
	 * 死亡时增加奖励对象进入游戏
	 */
	@Override
	public void goDead() {
		super.goDead();
		World.awards.add(getClassID());
	}
}
































