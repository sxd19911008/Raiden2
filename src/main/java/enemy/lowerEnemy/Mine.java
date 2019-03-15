package enemy.lowerEnemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import interest.sidney.main.World;
import objectPool.ObjectPool;

/**
 * 地雷类
 * 每次地雷进入游戏都是一排地雷，排列有序，
 * 所以地雷进入游戏必定指定坐标。
 * 
 * 同理，地雷对象回收时不需要初始化坐标。
 * 
 * @author Sidney
 *
 */
public class Mine extends LowerEnemy {
	private final static ArrayList<BufferedImage> images;   //地雷图片
	static {
		images = new ArrayList<BufferedImage>();
		for (int i = 0; i < 14; i++) {
			images.add(loadImage("images/mine"+i+".png"));
		}
	}
	
	private final int classID;          //类编号
	
	/**
	 * 构造器
	 * int width, int height, int speed
	 */
	public Mine() {
		super(25, 25, World.WORLDSPEED);
		this.classID = ObjectPool.MINE;
		init();
	}
	
	/**
	 * 总初始化方法
	 * 不需要初始化坐标，因为进入游戏时必须重新初始化坐标
	 */
	@Override
	public Mine init() {
		initIndex();
		initState();
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
	 * 移动
	 */
	public void step() {
		this.y += this.speed;
	}

	//get、set方法
	public int getClassID() {
		return classID;
	}
}
