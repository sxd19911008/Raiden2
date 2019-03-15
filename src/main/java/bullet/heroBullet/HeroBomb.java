package bullet.heroBullet;

import java.awt.image.BufferedImage;

import interest.sidney.main.World;
import objectPool.ObjectPool;

/**
 * 英雄机炸弹类
 * 英雄机必杀技发射该类对象
 * 
 * 该类对象不会与敌人碰撞，飞行到一定距离爆炸，发射爆炸对象blast
 * 
 * 重写越界检测，该类的对象永远没有越界。
 * 
 * @author Sidney
 *
 */
public class HeroBomb extends HeroBullet {
	private final static BufferedImage image;   //英雄机炸弹图片
	static {
		image = loadImage("images/herobomb.png");
	}
	
	private int lifeIndex;      //生存时间下标
	private final int classID;  //类编号
	
	/**
	 * 构造器
	 * int width, int height, int speed, int firepower
	 */
	public HeroBomb() {
		super(10,18,5,0);
		this.classID = ObjectPool.HERO_BOMB;
		init();
	}
	
	/**
	 * 总初始化方法
	 */
	@Override
	public HeroBullet init() {
		initLifeIndex();
		initState();
		return this;
	}
	
	/**
	 * 初始化生存时间下标
	 */
	private void initLifeIndex() {
		this.lifeIndex = 0;
	}
	
	/**
	 * 获取图片
	 */
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
	 * 移动到一定距离后死亡
	 */
	@Override
	public void step() {
		super.step();
		if(lifeIndex++>=60 && isAlive())
			goDead();
	}
	
	/**
	 * 死亡动作中加入发射blast对象的动作
	 */
	@Override
	public void goDead() {
		super.goDead();
		World.heroBullets.add((HeroBullet)ObjectPool.enterFlyingObject(
				this.x-(400-this.width)/2, this.y-(400-this.height)/2, ObjectPool.HERO_BOMB_BLAST));
	}
	
	/**
	 * 永远不会越界
	 */
	@Override
	public boolean outOfBounds() {
		return false;
	}

	//get、set方法
	public int getClassID() {
		return classID;
	}
}
























