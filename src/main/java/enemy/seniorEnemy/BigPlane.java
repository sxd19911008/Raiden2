package enemy.seniorEnemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import bullet.enemyBullet.EnemyBullet;
import interest.sidney.main.World;
import objectPool.ObjectPool;
/**
 * 大敌机类
 * 重写方法：goDead，死亡后增加一个奖励对象进入游戏
 * 
 * @author Sidney
 *
 */
public class BigPlane extends SeniorEnemy {
	private  final static ArrayList<BufferedImage> images;   //大敌机图片
	static {
		images = new ArrayList<BufferedImage>();
		for (int i = 0; i < 14; i++) {
			images.add(loadImage("images/bigplane"+i+".png"));
		}
	}
	
	private final int classID;          //类编号
	
	/**
	 * 构造器
	 * (int width, int height, int speed, int blood, int xSpeed
	 */
	public BigPlane() {
		super(60, 40, 2, 60, 1);
		this.classID = ObjectPool.BIG_PLANE;
		init();
	}
	
	/**
	 * 初始化血量
	 */
	@Override
	public void initBlood() {
		this.blood = 60;
	}
	
	/**
	 * 总初始化方法
	 */
	@Override
	public SeniorEnemy init() {
		initIndex();
		initState();
		initXY();
		initXDirection();
		initShootIndex();
		initBlood();
		return this;
	}

	/**
	 * 获取图片
	 */
	public BufferedImage getImage() {
		if(isAlive()) {
			return images.get(0);
		}else if(isDead()){
			if(index>=13)
				state = REMOVED;
			return images.get(index++);
		}
		return null;
	}

	/**
	 * 移动
	 */
	public void step() {
		this.y += this.speed;
		this.x += this.xSpeed*this.xDirection;
		shoot();  //射击
	}
	
	/**
	 * 射击，在step里调用
	 */
	private void shoot() {
		if(this.shootIndex++>=150) {
			World.enemyBullets.add(((EnemyBullet)ObjectPool.enterFlyingObject(
					this.x+this.width/2-2, this.y+this.height, ObjectPool.ENEMY_BULLET)));
			this.shootIndex = 0;
		}
	}
	
	/**
	 * 越界检查
	 */
	@Override
	public boolean outOfBounds() {
		return super.outOfBounds() || this.x>400 || this.x<-this.width;
	}

	//get、set方法
	public int getClassID() {
		return classID;
	}
}
