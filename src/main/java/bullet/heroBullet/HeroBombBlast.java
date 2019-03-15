package bullet.heroBullet;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import objectPool.ObjectPool;

/**
 * 英雄机必杀技爆炸类
 * 该类对象与敌人碰撞时，秒杀非boss敌人，自身不会死亡。
 * 
 * 该类进入游戏2个Timer循环后死亡
 * 
 * @author Sidney
 *
 */
public class HeroBombBlast extends HeroBullet {
	private final static ArrayList<BufferedImage> images;    //英雄机必杀技爆炸图片
	static {
		images = new ArrayList<BufferedImage>();
		for (int i = 0; i < 13; i++) {  //注意，这里只有13张图片，比类似对象少一张
			images.add(loadImage("images/bom"+i+".png"));
		}
	}
	
	private final int classID;      //类编号
	
	/**
	 * 构造器
	 * int width, int height, int speed, int firepower
	 */
	public HeroBombBlast() {
		super(400, 400, 0, 200);
		this.classID = ObjectPool.HERO_BOMB_BLAST;
		init();
	}
	
	/**
	 * 总初始化方法
	 */
	@Override
	public HeroBullet init() {
		initState();
		initIndex();
		return this;
	}
	
	/**
	 * 初始化图片下标
	 */
	@Override
	protected void initIndex() {
		this.index = -1;
	}
	
	/**
	 * 获取图片
	 */
	@Override
	public BufferedImage getImage() {
		if(isRemoved())
			return null;
		this.index++;
		if(isAlive() && this.index>1)
			this.state = DEAD;
		if(this.index==25)
			state = REMOVED;
		return images.get(index/2);
	}
	
	//get、set方法
	public int getClassID() {
		return classID;
	}
	//废弃方法
	@Override
	public void goDead() {}
}






















