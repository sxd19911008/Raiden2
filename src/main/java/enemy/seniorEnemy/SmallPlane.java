package enemy.seniorEnemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import bullet.enemyBullet.EnemyBullet;
import interest.sidney.main.World;
import objectPool.ObjectPool;

/**
 * 小敌机类
 * 重写方法：goDead，死亡后增加一个奖励对象进入游戏
 * 
 * @author Sidney
 *
 */
public class SmallPlane extends SeniorEnemy {
	private final static ArrayList<BufferedImage> images;   //小敌机图片
	static {
		images = new ArrayList<BufferedImage>();
		for (int i = 0; i < 14; i++) {
			images.add(loadImage("images/smallplane"+i+".png"));
		}
	}
	
	private final int classID;          //类编号
	
	/**
	 * 构造器
	 * int width, int height, int speed, int blood, int xSpeed
	 */
	public SmallPlane() {
		super(35, 21, 2, 20, 0);
		this.classID = ObjectPool.SMALL_PLANE;
		init();
	}
	
	/**
	 * 初始化血量
	 */
	@Override
	public void initBlood() {
		this.blood = 20;
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
	 * 小敌机向下移动,移动一定距离就射击
	 */
	public void step() {
		this.y += this.speed;
		shoot();  //射击
	}
	
	/**
	 * 射击，在step里调用
	 */
	private void shoot() {
		if(this.shootIndex++>=200) {
			World.enemyBullets.add(((EnemyBullet)ObjectPool.enterFlyingObject(
					this.x+this.width/2-2, this.y+this.height, ObjectPool.ENEMY_BULLET)));
			this.shootIndex = 0;
		}
	}
	
	//get、set方法
	public int getClassID() {
		return classID;
	}
}
