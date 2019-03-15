package hero;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import bullet.heroBullet.HeroBullet;
import interest.sidney.main.World;
import objectPool.ObjectPool;
import superClass.FlyingObject;

/**
 * 英雄机类
 * 射击动作和敌机一样，在step方法里执行
 * 
 * @author Sidney
 *
 */
public class Hero extends FlyingObject {
	private final static ArrayList<BufferedImage> images;    //英雄机图片
	static {
		images = new ArrayList<BufferedImage>();
		for (int i = 0; i < 2; i++) {
			images.add(loadImage("images/hero"+i+".png"));
		}
	}
	
	private int doubleFire;   //双倍火力
	private int bombs;        //必杀技数量
	private int shootIndex;   //射击计时
	private int blood;        //血量
	private int lives;        //命
	private int unHurtIndex;  //无敌时间
	
	/**
	 * 构造器
	 */
	public Hero() {
		super(50, 41, 0);
		init();
	}
	
	/**
	 * 总初始化方法
	 */
	@Override
	public FlyingObject init() {
		initShootIndex();
		initDoubleFire();
		initBombs();
		initBlood();
		initLives();
		initUnHurtIndex();
		initXY();
		return this;
	}
	
	/*
	 * 初始化各个属性的方法
	 */
	private void initShootIndex() { //初始化射击计时
		this.shootIndex = 0;
	}
	private void initDoubleFire() { //初始化双倍火力
		this.doubleFire = 0;
	}
	private void initBlood() {      //初始化血量
		this.blood = 100;
	}
	private void initLives() {      //初始化生命
		this.lives = 3;
	}
	private void initBombs() {      //初始化必杀技数量
		this.bombs = 1;
	}
	private void initUnHurtIndex() {//初始化无敌时间
		this.unHurtIndex = 150;
	}
	@Override
	protected void initXY() {       //初始化坐标
		setX((World.WIDTH-this.width)/2);
		setY(600);
	}

	/**
	 * 获取图片
	 */
	@Override
	public BufferedImage getImage() {
		this.unHurtIndex--;
		if(isUnHurt() && unHurtIndex%8<4) //处于无敌状态，图片会闪烁
			return null;
		if(this.doubleFire<15)
			return images.get(0);
		return images.get(1);
	}
	
	/**
	 * 英雄机随鼠标移动
	 * @param x
	 * @param y
	 */
	public void moveTo(int x,int y) {
		setX(x);
		setY(y);
	}
	
	/**
	 * 此方法仅仅用于射击动作
	 */
	@Override
	public void step() {
		shoot();
	}
	
	/**
	 * 射击
	 */
	private void shoot() {
		if(shootIndex++>=15) {
			if(this.doubleFire<=15) {
				World.heroBullets.add((HeroBullet)ObjectPool.enterFlyingObject(
						this.x+this.width/2-1, this.y-16, ObjectPool.HERO_BULLET));
			}else {
				World.heroBullets.add((HeroBullet)ObjectPool.enterFlyingObject(
						this.x+this.width/4, this.y-3, ObjectPool.HERO_BULLET));
				World.heroBullets.add((HeroBullet)ObjectPool.enterFlyingObject(
						this.x+3*this.width/4-1, this.y-3, ObjectPool.HERO_BULLET));
			}
			if(this.doubleFire>=40) {
				World.heroBullets.add((HeroBullet)ObjectPool.enterFlyingObject(
						this.x+3, this.y+this.height-19, ObjectPool.HERO_TRACKING_BULLET));
				World.heroBullets.add((HeroBullet)ObjectPool.enterFlyingObject(
						this.x+this.width-8, this.y+this.height-19, ObjectPool.HERO_TRACKING_BULLET));
			}
			shootIndex = 0;
		}
	}
	
	/**
	 * 检查是否处于无敌状态
	 * @return
	 */
	public boolean isUnHurt() {
		return this.unHurtIndex>0;
	}
	
	/**
	 * 中弹掉血
	 * 血量掉光则掉命补满血，同时清空双倍火力值
	 */
	public void hurt(int firePower) {
		this.blood -= firePower;
		if(this.blood<=0) {
			initBlood();
			initDoubleFire();
			initUnHurtIndex();
			this.lives -= 1;
		}
	}
	
	/**
	 * 处理奖励的获取，同时删除奖励对象
	 */
	public void getAwards(LinkedList<Integer> list) {
		for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
			Integer award = (Integer) iterator.next();
			switch(award.intValue()) {
				case ObjectPool.SMALL_PLANE:
					addDoubleFire(World.random.nextInt(2));
					break;
				case ObjectPool.BIG_PLANE:
					addDoubleFire(World.random.nextInt(5));
					break;
				case ObjectPool.BOMBER:
					int t = World.random.nextInt(50);
					if(t<4) {
						this.blood +=40;
						if(this.blood>100)
							initBlood();
					}else if(t>24) {
						addDoubleFire(World.random.nextInt(5)+10);
					}
					break;
				case ObjectPool.BOSS:
					addDoubleFire(100);
					this.lives += 1;
					initBlood();
					break;
			}
			iterator.remove();
		}
	}
	
	/**
	 * 处理奖励时增加双倍火力的方法
	 * @param award
	 */
	private void addDoubleFire(int award) {
		this.doubleFire += award;
		if(this.doubleFire>=100) {
			this.doubleFire -= 50;
			this.bombs += 1;
		}
	}
	
	/**
	 * 炸弹数量减少
	 */
	public void subBomb() {
		this.bombs -= 1;
	}
	
	//get、set方法
	public int getBlood() {
		return blood;
	}
	public int getDoubleFire() {
		return doubleFire;
	}
	public int getLives() {
		return lives;
	}
	public int getBombs() {
		return bombs;
	}

	//废弃方法
	@Override
	public int getClassID() {
		return 0;
	}
}
