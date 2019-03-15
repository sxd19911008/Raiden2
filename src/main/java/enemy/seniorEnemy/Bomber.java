package enemy.seniorEnemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import bullet.enemyBullet.EnemyBullet;
import interest.sidney.main.World;
import objectPool.ObjectPool;

/**
 * 轰炸机类
 * 继承自SeniorEnemy类
 * 
 * 重写方法：goDead，死亡后增加一个奖励对象进入游戏
 * 
 * @author Sidney
 *
 */
public class Bomber extends SeniorEnemy {
	private final static ArrayList<BufferedImage> images;   //轰炸机图片
	static {
		images = new ArrayList<BufferedImage>();
		for (int i = 0; i < 14; i++) {
			images.add(loadImage("images/bomber"+i+".png"));
		}
	}
	
	private final int classID;          //类编号
	
	/**
	 * 构造器
	 * int width, int height, int speed, int blood, int xSpeed
	 */
	public Bomber() {
		super(100, 65, 2, 200, 2);
		classID = ObjectPool.BOMBER;
		init();
	}
	
	/**
	 * 初始化血量
	 */
	@Override
	public void initBlood() {
		this.blood = 200;
	}
	
	/**
	 * 初始化坐标
	 */
	@Override
	protected void initXY() {
		this.x = World.random.nextInt(200)+50;
		this.y = -this.width;
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
	 * 轰炸机移动
	 * 先下降到this.y=100的位置，然后x轴移动
	 */
	public void step() {
		if(this.y<100) {
			this.y += this.speed;
		}else {
			if(this.x<50 || this.x>250)
				this.xDirection *= -1;
			this.x += this.xSpeed*this.xDirection;
			shoot();//射击
		}
	}
	
	/**
	 * 射击，在step里调用
	 */
	private void shoot() {
		if(this.shootIndex++>=250) {
			for (int i = 0; i < 3; i++) {
				World.enemyBullets.add(((EnemyBullet)ObjectPool.enterFlyingObject(
						this.x+(i+1)*this.width/4-2, this.y+this.height, ObjectPool.BOSS_BULLET)));
			}
			this.shootIndex = 0;
		}
	}
	
	//get、set方法
	public int getClassID() {
		return classID;
	}
}
