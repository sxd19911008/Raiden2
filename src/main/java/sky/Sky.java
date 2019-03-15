package sky;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import interest.sidney.main.World;
import superClass.FlyingObject;

/**
 * 天空背景类
 * 一个对象两张相同图片，交替下移显示
 * 两张图片x坐标相同，y坐标相差天空图片高的距离
 * 
 * @author Sidney
 *
 */
public class Sky extends FlyingObject {
	private final static BufferedImage image;   //天空图片
	static {
		image = loadImage("images/sky.png");
	}
	
	private int y1;    //第二张图的y坐标
	
	/**
	 * 构造器
	 * int width, int height, int speed
	 */
	public Sky() {
		super(World.WIDTH, 1024, World.WORLDSPEED);
		init();
	}
	
	/**
	 * 总初始化方法
	 */
	@Override
	public Sky init() {
		initY();
		return this;
	}
	
	/**
	 * 初始化y和y1坐标
	 */
	private void initY() {
		setY(0);
		this.y1 = -this.height;
	}

	/**
	 * 获取图片
	 */
	@Override
	public BufferedImage getImage() {
		return image;
	}
	
	/**
	 * 绘制
	 */
	@Override
	public void paintObject(Graphics g) {
		super.paintObject(g);
		g.drawImage(getImage(), this.x, this.y1, null);
	}
	
	/**
	 * 移动
	 */
	@Override
	public void step() {
		this.y += this.speed;
		this.y1 += this.speed;
		if(this.y>=this.height)
			this.y = -this.height;   //返回上面
		if(this.y1>=this.height)
			this.y1 = -this.height;  //返回上面
	}
	
	//废弃方法
	@Override
	public int getClassID() {
		return 0;
	}
}
