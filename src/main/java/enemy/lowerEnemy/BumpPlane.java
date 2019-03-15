package enemy.lowerEnemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import objectPool.ObjectPool;

/**
 * 冲撞机类
 * 冲撞机会和小敌机一样随机进入游戏（随机坐标即可），
 * 也会和地雷一样成组进入游戏（进入前需要指定坐标）。
 * 
 * 冲撞机对象回收时需要初始化坐标。
 * 
 * @author Sidney
 *
 */
public class BumpPlane extends LowerEnemy {
	private final static ArrayList<BufferedImage> images;    //冲撞机图片
	static {
		images = new ArrayList<BufferedImage>();
		for (int i = 0; i < 14; i++) {
			images.add(loadImage("images/bumpplane"+i+".png"));
		}
	}
	
	private final int bumpSpeed;     //冲撞速度
	private final int classID;       //类编号
	
	/**
	 * 构造器
	 * int width, int height, int speed
	 */
	public BumpPlane() {
		super(30, 23, 1);
		this.bumpSpeed = 5;
		this.classID = ObjectPool.BUMP_PLANE;
		init();
	}
	
	/**
	 * 总初始化方法
	 */
	@Override
	public BumpPlane init() {
		initIndex();
		initState();
		initXY();
		return this;
	}

	/**
	 * 获取图片
	 */
	public BufferedImage getImage() {
		if(isAlive()) {
			return images.get(0);
		}else if(isDead()){
			if(index>=13) {
				state = REMOVED;
			}
			return images.get(index++);
		}
		return null;
	}

	/**
	 * 移动，y>50后开始冲撞
	 */
	public void step() {
		if(this.y<50) {
			this.y += this.speed;
		}else {
			this.y += this.bumpSpeed;
		}
	}
	
	//get、set方法
	public int getClassID() {
		return classID;
	}
}





















