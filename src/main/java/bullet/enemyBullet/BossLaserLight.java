package bullet.enemyBullet;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import interest.sidney.main.World;
import objectPool.ObjectPool;

/**
 * boss激光类
 * 激光威力为秒杀，直接让英雄机损失一条命
 * 但是，英雄机被激光打中后，本轮激光将不能对英雄机造成伤害。
 * 
 * 激光必须能停留一段时间再死亡，死亡后逐渐递减透明致5%，然后删除
 * 
 * @author Sidney
 *
 */
public class BossLaserLight extends EnemyBullet {
	private final static ArrayList<BufferedImage> images;    //激光图片
	static {
		images = new ArrayList<BufferedImage>();
		for (int i = 0; i < 10; i++) {
			images.add(loadImage("images/laserlight"+i+".png"));
		}
	}
	
	protected int firePower;    //激光威力
	private int lifeIndex;      //激光生存时间下标
	private int xSpeed;         //x轴速度
	private final int classID;  //类编号
	
	/**
	 * 构造器
	 * int width, int height, int speed, int firePower
	 */
	public BossLaserLight() {
		super(10, 600, 0, 99999);
		this.xSpeed = 1;
		this.classID = ObjectPool.BOSS_LASER_LIGHT;
		init();
	}
	
	/**
	 * 总初始化方法
	 */
	@Override
	public BossLaserLight init() {
		initIndex();
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
	 * 重写初始化坐标方法，改变方法的返回值类型
	 */
	@Override
	public BossLaserLight initXY(int x, int y) {
		super.initXY(x, y);
		return this;
	}
	
	/**
	 * 重写加载图片功能，存活1s自动死亡，死亡后自动删除
	 */
	@Override
	public BufferedImage getImage() {
		if(this.lifeIndex>=150 && isAlive())
			goDead();
		if(isAlive()) {
			this.lifeIndex++;
			return images.get(0);
		}
		if(isDead()){
			if(index>=19)
				state = REMOVED;
			return images.get(index++/2);
		}
		return null;
	}
	
	/**
	 * 激光随着boss移动
	 */
	@Override
	public void step() {
		this.x += this.xSpeed*World.boss.getXDirection();
	}
	
	//get、set方法
	public int getClassID() {
		return classID;
	}
}





























