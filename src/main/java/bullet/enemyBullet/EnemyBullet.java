package bullet.enemyBullet;
/**
 * 敌人普通子弹类
 * 继承自FlyingObject,同时也是所有敌方子弹、激光的超类。
 * 
 * 增加公共属性：子弹威力firepower，设置为2
 * 
 * 增加私有静态属性：图片image，用静态块加载图片。
 * 
 * @author Sidney
 *
 */

import java.awt.image.BufferedImage;

import interest.sidney.main.InitXY;
import objectPool.ObjectPool;
import superClass.FlyingObject;

public class EnemyBullet extends FlyingObject implements InitXY {
	private final static BufferedImage image;   //子弹图片
	static {
		image = loadImage("images/enemybullet.png");
	}
	
	protected int firePower;  //子弹威力
	private final int classID = ObjectPool.ENEMY_BULLET;  //类编号

	/**
	 * 供子类使用的构造器
	 * @param width
	 * @param height
	 * @param speed
	 * @param firepower
	 */
	public EnemyBullet(int width, int height, int speed, int firePower) {
		super(width, height, speed);
		this.firePower = firePower;
	}
	
	/**
	 * 供自身使用的构造器
	 * int width, int height, int speed
	 */
	public EnemyBullet() {
		this(5, 5, 4, 20);
		init();
	}

	/**
	 * 总初始化方法
	 */
	@Override
	public EnemyBullet init() {
		initState();
		return this;
	}
	
	/**
	 * 重载初始化坐标方法，用于需要在进入游戏前指定坐标的情况
	 * @param x
	 * @param y
	 */
	public EnemyBullet initXY(int x, int y) {
		setX(x);
		setY(y);
		return this;
	}
	
	/**
	 * 重写加载图片功能，死亡后自动删除
	 */
	@Override
	public BufferedImage getImage() {
		if(isAlive()) {
			return image;
		}else if(isDead()){
			this.state=REMOVED;
			return null;
		}
		return null;
	}
	
	/**
	 * 获取子弹威力值
	 * @return
	 */
	public int getFirePower() {
		return firePower;
	}

	/**
	 * 移动
	 */
	@Override
	public void step() {
		this.y += this.speed;
	}
	
	//get、set方法
	public int getClassID() {
		return classID;
	}
}
































