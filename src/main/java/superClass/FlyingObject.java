package superClass;
/**
 * 所有飞行物的超类
 * 
 * 基本常量
 * 活着alive、死了dead、删除了remove
 * 
 * 基础属性
 * 宽、高、x坐标、y坐标、速度、当前生存状态
 * 
 * 基本方法
 * 加载图片loadImage(静态)、绘制paintObject、判断是否越界outOfBounds、
 * 判断碰撞hit、死亡goDead
 * 
 * 检查飞行物状态isAlive/isDead/isRemove
 * 
 * 抽象方法：
 * 移动step、获取图片getImage
 * 
 * @author Sidney
 *
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import interest.sidney.main.World;

public abstract class FlyingObject {
	public static final int ALIVE = 0;   //活着
	public static final int DEAD = 1;    //死亡
	public static final int REMOVED = 2; //待删除
	
	protected int width;   //宽
	protected int height;  //高
	protected int x;       //x坐标
	protected int y;       //y坐标
	protected int speed;   //速度
	protected int state;   //生存状态
	protected int index;   //死亡后图片下标
	
	/**
	 * 构造器
	 * @param width
	 * @param height
	 * @param speed
	 */
	public FlyingObject(int width, int height, int speed) {
		this.width = width;
		this.height = height;
		this.x = 0;
		this.y = -height;
		this.speed = speed;
		this.state = ALIVE;
		this.index = 1;
	}
	
	/**
	 * 总初始化方法，用于对象加入游戏前
	 */
	public abstract FlyingObject init();
	
	/**
	 * 初始化死亡后图片下标
	 */
	protected void initIndex() {
		this.index = 1;
	}
	
	protected void initState() {
		this.state = ALIVE;
	}
	
	/**
	 * 初始化坐标
	 */
	protected void initXY() {
		this.x = World.random.nextInt(World.WIDTH-this.width);
		this.y = -this.height;
	}

	/**
	 * 加载图片
	 * @param fileName
	 * @return
	 */
	public static BufferedImage loadImage(String fileName) {
		try {
			return ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * 获取图片
	 * @return
	 */
	public abstract BufferedImage getImage();
	
	/**
	 * 绘制
	 * @param g
	 */
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), this.x, this.y, null);
	}
	
	/**
	 * 获取类ID
	 * @return
	 */
	public abstract int getClassID();
	
	/**
	 * 飞行物移动
	 */
	public abstract void step();
	
	//判断生存状态
	public boolean isAlive() {
		return state==ALIVE;
	}
	public boolean isDead() {
		return state==DEAD;
	}
	public boolean isRemoved() {
		return state==REMOVED;
	}
	
	/**
	 * 判断飞行物是否越界
	 * @return
	 */
	public boolean outOfBounds() {
		return this.y>700; //注意：英雄机子弹对象须重写该处。
	}
	
	/**
	 * 判断飞行物是否发生碰撞
	 * @return
	 */
	public boolean hit(FlyingObject other) {
		int up = this.y-other.getHeight();
		int down = this.y+this.height;
		int left = this.x-other.getWidth();
		int right = this.x+this.width;
		int x2 = other.getX();
		int y2 = other.getY();
		return y2>up && y2<down && x2>left && x2<right;
	}
	
	/**
	 * 飞行物去死
	 */
	public void goDead() {
		if(this.state==ALIVE) {
			this.state = DEAD;
		}
	}
	
	/**
	 * 从游戏中删除并返回对象池
	 * 需要重置一些变量，比如死亡后图片下标等。
	 */

	//get、set方法
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}























