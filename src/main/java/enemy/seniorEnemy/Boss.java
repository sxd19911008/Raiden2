package enemy.seniorEnemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import bullet.enemyBullet.EnemyBullet;
import interest.sidney.main.World;
import objectPool.ObjectPool;

/**
 * boss类
 * 重写方法：goDead，死亡后增加一个奖励对象进入游戏
 * 
 * 写step方法时注意：boss发射激光后，激光对象未删除前，boss不可移动！
 * 
 * @author Administrator
 *
 */
public class Boss extends SeniorEnemy {
	private final static ArrayList<BufferedImage> images;    //boss图片
	static {
		images = new ArrayList<BufferedImage>();
		for (int i = 0; i < 14; i++) {
			images.add(loadImage("images/boss"+i+".png"));
		}
	}
	
	private int stopIndex;      //boss射击子弹时停止的时间
	private final int classID;  //类编号
	
	/**
	 * 构造器
	 * int width, int height, int speed, int blood, int xSpeed
	 */
	public Boss() {
		super(200, 149, World.WORLDSPEED, 5000,1);
		this.stopIndex = 0;
		this.classID = ObjectPool.BOSS;
		init();
	}

	/**
	 * 初始化
	 * 该类对象不用回收，只需要初始化坐标、x轴方向
	 */
	@Override
	public Boss init() {
		initXY();
		initXDirection();
		return this;
	}
	
	/**
	 * 初始化坐标
	 */
	@Override
	protected void initXY() {
		setX(100);
	}
	
	/**
	 * 初始化射击计时
	 */
	@Override
	public void initShootIndex() {
		this.shootIndex = 0;
	}
	
	/**
	 * 获取图片
	 */
	@Override
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
	@Override
	public void step() {
		if(this.y<50) {
			this.y += this.speed;
		}else {
			if(this.x<1)
				this.xDirection = 1;
			if(this.x>198)
				this.xDirection = -1;
			if(stopIndex<=0) {
				this.x += this.xSpeed*this.xDirection;
				if(World.bossLaserLights.isEmpty())
					shoot();  //射击
			}else {
				if(stopIndex--%40==0) {
					shootBullet();
				}
			}
			
		}
	}
	
	/**
	 * 射击，在step方法里调用
	 */
	private void shoot() {
		shootIndex++;
		if(shootIndex>=World.random.nextInt(51)+150) {
			if(World.random.nextInt(4)>0) {
				stopIndex = 120;  //触发连续射击子弹事件，接下来一段时间内boss不会移动
			}else { //射击激光
				World.bossLaserLights.add((EnemyBullet)ObjectPool.enterFlyingObject( //中间的激光
						this.x+(this.width-10)/2, this.y+this.height, ObjectPool.BOSS_LASER_LIGHT));
				World.bossLaserLights.add((EnemyBullet)ObjectPool.enterFlyingObject( //左边的激光
						this.x+8, this.y+45, ObjectPool.BOSS_LASER_LIGHT));
				World.bossLaserLights.add((EnemyBullet)ObjectPool.enterFlyingObject( //右边的激光
						this.x+182, this.y+45, ObjectPool.BOSS_LASER_LIGHT));
			}
			initShootIndex();
		}
	}
	
	/**
	 * boss射击子弹
	 */
	private void shootBullet() {
		for (int i = 0; i < 5; i++) {
			World.enemyBullets.add(ObjectPool.shootBossBullet(
					this.x+(this.width-10)/2, this.y+this.height, i-2));
		}
	}
	
	/**
	 * 如果boss死亡，激光立即消失
	 */
	@Override
	public void goDead() {
		for (EnemyBullet eb : World.bossLaserLights) {
			if(eb.isAlive())
				eb.goDead();
		}
		super.goDead();
	}
	
	//get、set方法
	@Override
	public int getClassID() {
		return this.classID;
	}
	public int getXDirection() {
		return this.xDirection;
	}
	
	//无用方法
	@Override
	public void initBlood() {}
}



















