package bullet.heroBullet;

import java.awt.image.BufferedImage;

import interest.sidney.main.InitXY;
import objectPool.ObjectPool;
import superClass.FlyingObject;

/**
 * 英雄机普通子弹类
 * 同时也是所有英雄机子弹的超类
 * 子弹威力为2
 * 
 * @author Sidney
 *
 */
public class HeroBullet extends FlyingObject implements InitXY {
	private final static BufferedImage image;   //子弹图片
	static {
		image = loadImage("images/herobullet.png");
	}
	
	protected int firePower;  //子弹威力
	private final int classID = ObjectPool.HERO_BULLET;  //类编号

	/**
	 * 供子类使用的构造器
	 * @param width
	 * @param height
	 * @param speed
	 * @param firepower
	 */
	public HeroBullet(int width, int height, int speed, int firepower) {
		super(width, height, speed);
		this.firePower = firepower;
	}
	
	/**
	 * 供自身使用的构造器
	 * int width, int height, int speed
	 */
	public HeroBullet() {
		this(3, 17, 4, 10);
		init();
	}

	/**
	 * 总初始化方法
	 */
	@Override
	public HeroBullet init() {
		initState();
		return this;
	}
	
	/**
	 * 重载初始化坐标方法，用于需要在进入游戏前指定坐标的情况
	 * @param x
	 * @param y
	 */
	public HeroBullet initXY(int x, int y) {
		setX(x);
		setY(y);
		return this;
	}
	
	/**
	 * 获取图片
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
	 * 移动
	 */
	@Override
	public void step() {
		this.y -= this.speed;
	}
	
	/**
	 * 重写越界检测方法
	 */
	@Override
	public boolean outOfBounds() {
		return this.y<-this.height;
	}
	
	//get、set方法
	public int getClassID() {
		return classID;
	}
	public int getFirePower() {
		return firePower;
	}
}




















