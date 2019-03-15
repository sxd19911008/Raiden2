package bullet.enemyBullet;

import java.awt.image.BufferedImage;
import objectPool.ObjectPool;

/**
 * Boss专用子弹
 * 继承自EnemyBullet,子弹威力为4
 * 
 * @author Sidney
 *
 */
public class BossBullet extends EnemyBullet {
	private final static BufferedImage image;   //子弹图片
	static {
		image = loadImage("images/bossbullet.png");
	}
	
	private boolean isBossShoot;  //是否是boss射击出来的
	private int xDirection;       //x轴坐标移动方向
	private final int classID;    //类编号
	
	/**
	 * 构造器
	 * int width, int height, int speed, int firePower
	 */
	public BossBullet() {
		super(10, 10, 3, 40);
		this.classID = ObjectPool.BOSS_BULLET;
		this.xDirection = 0;
		init();
	}
	
	/**
	 * 总初始化方法
	 */
	@Override
	public EnemyBullet init() {
		initState();
		initIsBossShoot();
		return this;
	}
	
	/**
	 * 初始化boss射击状态，默认不是boss射击的
	 */
	private BossBullet initIsBossShoot() {
		this.isBossShoot = false;
		return this;
	}
	
	/**
	 * 重写改变返回值类型
	 */
	@Override
	public BossBullet initXY(int x, int y) {
		super.initXY(x, y);
		return this;
	}
	
	/**
	 * 设置x轴方向
	 * @param xDirection
	 */
	public BossBullet initXDirection(int xDirection) {
		this.xDirection = xDirection;
		return this;
	}
	
	/**
	 * 当boss射击该子弹时，设置isBossShoot为true
	 * @return
	 */
	public BossBullet setIsBossShoot() {
		this.isBossShoot = true;
		return this;
	}
	
	/**
	 * 加载图片功能，死亡后自动删除
	 */
	public BufferedImage getImage() {
		if(isAlive()) {
			return image;
		}else if(isDead()){
			this.state=REMOVED;
			return null;
		}
		return null;
	}

	/**
	 * 加入boss射击状态下的移动方式
	 */
	public void step() {
		if(this.isBossShoot && this.y>250 && this.y<=500)
			this.x += this.xDirection;
		super.step();
	}
	
	//get、set方法
	public int getClassID() {
		return classID;
	}
}
