package bullet.heroBullet;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import interest.sidney.main.World;
import objectPool.ObjectPool;
import superClass.FlyingObject;

/**
 * 英雄机跟踪弹类
 * 跟踪弹移动时，需要留下残影
 * 
 * 跟踪弹移动前，必须确定一个距离英雄机最近的敌人进行跟踪
 * 
 * @author Sidney
 *
 */
public class HeroTrackingBullet extends HeroBullet {
	private final static ArrayList<BufferedImage> images;    //跟踪弹图片
	static {
		images = new ArrayList<BufferedImage>();
		for (int i = 0; i < 5; i++) {
			images.add(loadImage("images/herotrackingbullet"+i+".png"));
		}
	}
	
	private final int xSpeed;   //x轴速度
	private final int[] x1;     //残影x坐标
	private final int[] y1;     //残影y坐标
	private int trackingIndex;  //追踪时间计时
	private final int classID;  //类编号
	
	/**
	 * 构造器
	 * int width, int height, int speed, int firepower
	 */
	public HeroTrackingBullet() {
		super(5, 5, 4, 2);
		this.xSpeed = 3;
		this.classID = ObjectPool.HERO_TRACKING_BULLET;
		this.x1 = new int[4];
		this.y1 = new int[4];
		init();
	}
	
	/**
	 * 总初始化方法
	 */
	@Override
	public HeroTrackingBullet init() {
		initState();
		initTrackingIndex();
		return this;
	}
	
	private void initTrackingIndex() {
		this.trackingIndex = 100;
	}
	
	/**
	 * 进入游戏前初始化坐标，同时初始化残影坐标
	 */
	@Override
	public HeroTrackingBullet initXY(int x, int y) {
		super.initXY(x, y);
		initShadowXY();
		return this;
	}
	
	/**
	 * 初始化残影坐标
	 */
	private void initShadowXY(){
		for (int i = 0; i < x1.length; i++) {
			this.x1[i] = this.x;
			this.y1[i] = this.y;
		}
	}
	
	/**
	 * 获取图片
	 */
	@Override
	public BufferedImage getImage() {
		if(isAlive()) {
			return images.get(0);
		}else if(isDead()){
			this.state=REMOVED;
			return null;
		}
		return null;
	}
	
	/**
	 * 重写加入残影
	 */
	@Override
	public void paintObject(Graphics g) {
		super.paintObject(g);
		for (int i = 0; i < x1.length; i++) {
			g.drawImage(images.get(i+1), this.x1[i], this.y1[i], null);
		}
	}
	
	/**
	 * 根据距离英雄机最近的敌机位置移动
	 */
	@Override
	public void step() {
		this.trackingIndex--;
		for (int i = x1.length-1; i > 0; i--) {
			x1[i] = x1[i-1];
			y1[i] = y1[i-1];
		}
		x1[0] = this.x;
		y1[0] = this.y;
		if(this.trackingIndex<0 || checkNearestEnemy()==null) {
			super.step();
		}else {
			if(xDistance(checkNearestEnemy())>0) {
				this.x -= this.xSpeed;
			}else if(xDistance(checkNearestEnemy())<0) {
				this.x += this.xSpeed;
			}
			if(yDistance(checkNearestEnemy())>0) {
				this.y -= this.speed;
			}else if(yDistance(checkNearestEnemy())<0) {
				this.y += this.speed;
			}
		}
	}
	
	/**
	 * 计算距离这个子弹最近的敌人，如果没有敌人返回null
	 * @return
	 */
	private FlyingObject checkNearestEnemy() {
		FlyingObject nearestFb = null; //当前最近的敌人
		int distance = 0;  //当前最近的敌人与这个子弹的距离
		int t = 0; //用于代数的变量
		World.otherList.addAll(World.lowerEnemies);
		World.otherList.addAll(World.seniorEnemies);
		if(World.boss!=null)
			World.otherList.add(World.boss);
		if(!World.otherList.isEmpty()) {
			nearestFb = World.otherList.get(0);
			distance = getEnemyDistance(nearestFb);
			for (FlyingObject fb : World.otherList) {
				if((t=getEnemyDistance(fb))<distance) {
					nearestFb = fb;
					distance = t;
				}
			}
			World.otherList.clear();
		}
		return nearestFb;
	}
	
	/**
	 * 获取这个子弹与敌人之间的距离
	 * @param fb
	 * @return
	 */
	private int getEnemyDistance(FlyingObject fb) {
		return (int)(Math.pow(xDistance(fb), 2)+Math.pow(yDistance(fb), 2));
	}
	
	/**
	 * 获取这个子弹中心x轴坐标减去敌人中心x轴坐标的值
	 * @param fb
	 * @return
	 */
	private int xDistance(FlyingObject fb) {
		return this.x+this.width/2-fb.getX()-fb.getWidth()/2;
	}
	
	/**
	 * 获取这个子弹中心y轴坐标减去敌人中心y轴坐标的值
	 * @param fb
	 * @return
	 */
	private int yDistance(FlyingObject fb) {
		return this.y+this.height/2-fb.getY()-fb.getHeight()/2;
	}
	
	//get、set方法
	public int getClassID() {
		return classID;
	}
}
