package interest.sidney.main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;

import bullet.enemyBullet.*;
import bullet.heroBullet.*;
import enemy.lowerEnemy.*;
import enemy.seniorEnemy.*;
import hero.*;
import objectPool.*;
import sky.*;
import superClass.*;

/**
 * 游戏的运行窗口类
 * 
 * 游戏运行中的数据结构：
 * sky、hero、boss各一个引用
 * awards奖励对象集合
 * 
 * 子弹类型集合不能用FlyingObject型，因为不能调用getFirePower方法
 * heroBullets英雄机子弹集合
 * enemyBullets敌方子弹集合
 * 
 * lowerEnemies敌方低级飞行物集合，包括地雷、冲撞机等(可以使用超类型)
 * seniorEnemies高级敌机集合，射击时遍历该集合(使用高级敌人类型即可)
 * 
 * actionList为本类使用的临时集合，用于批量向上造型
 * otherLise为本类外使用的临时集合，用于批量向上造型
 * actionList使用时遇到并发安全问题，所以涉及actionList的代码都加了synchronized块。
 * 
 * 将每一步动作封装到一个方法中，主要包括：
 * 1.随机敌机类型，敌人进入enterAction
 * 2.敌人移动stepAction
 * 3.敌机射击、英雄机射击shootAction
 * 4.boss所有动作bossAction
 * 4.敌机与英雄机的碰撞检测heroBangAction、enemiesBangAction
 * 5.处理英雄机奖励awardAction
 * 6.检查越界、是否处于删除状态，将删除的对象返回对象池。removeAction
 * 7.绘制图像paint，同时显示英雄机状态：血量、命数、必杀技次数
 * 8.检查游戏结束checkGameOverAction
 * 
 * 将所有过程方法封装到action方法中，并定时循环所有过程方法。
 * 在action方法中设置鼠标侦听器。鼠标移动代表英雄机移动。
 * 鼠标左键释放必杀技。
 * 
 * 重新开始游戏初始化方法init：需要将所有游戏中还活着的对象返回对象池，
 * 初始化timeIndex，awards。
 * 
 * 注意：游戏主过程action里面使用Timer类对象实现每10ms一循环。
 * 同时使用鼠标侦听器实现侦听玩家鼠标动作，键盘侦听器侦听键盘动作。
 * 这里会存在多线程情况（鼠标侦听单独一线程），所以herobomb所在的集合要实现并发安全。
 * 
 * @author Sidney
 *
 */

public class World extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 400;          //窗口宽
	public static final int HEIGHT = 700;         //窗口高
	public static final int WORLDSPEED = 1;       //世界基本速度
	public static final int START = 0;            //游戏准备开始状态
	public static final int RUNNING = 1;          //游戏运行状态
	public static final int PAUSE = 2;            //游戏暂停状态
	public static final int GAME_OVER = 3;        //游戏结束状态
	public static final int WIN = 4;              //游戏胜利状态
	
	private static int state = START;             //游戏当前状态，初始化为准备开始状态
	private int timeIndex = 1;                    //游戏运行计时
	
	private static BufferedImage start;           //准备开始界面
	private static BufferedImage pause;           //暂停界面
	private static BufferedImage gameOver;        //游戏结束界面
	private static KeyAdapter l;                  //键盘侦听对象，由JFrame对象加入
	
	private static Map<Integer, Integer> enterMap;//随机数Map
	public static LinkedList<Integer> awards;     //奖励对象集合，暂时用int值代替奖励对象
	
	public static Sky sky;     //天空对象
	public static Hero hero;   //英雄机
	public static Boss boss;   //关底boss
	
	public static LinkedList<HeroBullet> heroBullets;        //英雄机子弹集合
	public static LinkedList<HeroBullet> heroBombs;          //英雄机炸弹集合
	public static LinkedList<EnemyBullet> enemyBullets;      //敌机子弹集合
	public static LinkedList<EnemyBullet> bossLaserLights;   //boss激光
	public static LinkedList<LowerEnemy> lowerEnemies;       //低级敌人集合
	public static LinkedList<SeniorEnemy> seniorEnemies;     //高级敌人集合
	
	private static LinkedList<FlyingObject> actionList;      //临时集合，此集合只能在本类【中】使用！
	public static LinkedList<FlyingObject> otherList;		 //临时集合，此集合只能在本类【外】使用！！
	public static LinkedList<Bang> bangList;                 //用于碰撞动作的临时集合
	
	public static Random random;  //用于在游戏中获取随机数
	
	static {
		start = FlyingObject.loadImage("images/start.png");
		pause = FlyingObject.loadImage("images/pause.png");
		gameOver = FlyingObject.loadImage("images/gameover.png");
		initL();
		
		initEnterMap();
		awards = new LinkedList<Integer>();
		
		sky = new Sky();
		hero = new Hero();
		boss = null;
		
		heroBullets = new LinkedList<HeroBullet>();
		heroBombs = new LinkedList<HeroBullet>();
		enemyBullets = new LinkedList<EnemyBullet>();
		bossLaserLights = new LinkedList<EnemyBullet>();
		lowerEnemies = new LinkedList<LowerEnemy>();
		seniorEnemies = new LinkedList<SeniorEnemy>();
		
		actionList = new LinkedList<FlyingObject>();
		otherList = new LinkedList<FlyingObject>();
		bangList = new LinkedList<Bang>();
		
		random = new Random();
	}
	
	/**
	 * 供静态块调用的键盘适配器的初始化方法
	 */
	private static void initL() {
		l = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(state == RUNNING) {
					if(e.getKeyCode()==32 && hero.getBombs()>0) //按下空格发射必杀技
						synchronized (heroBombs) {
							heroBombs.add((HeroBullet)ObjectPool.enterFlyingObject(
									hero.getX()+25-5, hero.getY()-18, ObjectPool.HERO_BOMB));
							hero.subBomb(); //英雄机炸弹数减1
						}
					if(e.getKeyCode()==10) //按下回车暂停
						state = PAUSE;
				}
			}
		};
	}
	
	/**
	 * 供静态块调用的enterMap初始化方法。
	 */
	private static void initEnterMap() {
		enterMap = new HashMap<Integer, Integer>(50);
		for (int i = 0; i < 35; i++) {
			if(i<20) {
				enterMap.put(i, ObjectPool.SMALL_PLANE);
			}else if (i<30) {
				enterMap.put(i, ObjectPool.BIG_PLANE);
			}else {
				enterMap.put(i, ObjectPool.BUMP_PLANE);
			}
		}
	}
	
	/**
	 * 将所有对象加入临时集合actionList
	 */
	public void addActionList() {
		actionList.addAll(enemyBullets);
		actionList.addAll(heroBullets);
		actionList.addAll(seniorEnemies);
		actionList.addAll(lowerEnemies);
	}
	
	/**
	 * 单个敌人与英雄机子弹的碰撞动作
	 */
	public void enemyBang(Bang b) {
		for (HeroBullet hb : heroBullets) {
			if(b.hit(hb) && b.isAlive() && hb.isAlive()) {
				b.bang(hb);
				hb.goDead();
			}
		}
	}
	
	/**
	 * 敌人与英雄机子弹碰撞
	 */
	public void enemyBangAction() {
		bangList.addAll(seniorEnemies);
		bangList.addAll(lowerEnemies);
		for (Bang b : bangList) {
			enemyBang(b);
		}
		bangList.clear();
	}
	
	/**
	 * 英雄机与敌方子弹、敌机碰撞
	 * 碰撞达成条件：英雄机与敌方单位碰撞了、英雄机没有无敌状态、敌方单位活着
	 */
	public void heroBangAction() {
		for (EnemyBullet eb : enemyBullets) { //英雄机与敌人子弹碰撞
			if(hero.hit(eb) && !hero.isUnHurt() && eb.isAlive()) {
				eb.goDead();
				hero.hurt(eb.getFirePower());
			}
		}
		for (EnemyBullet eb : bossLaserLights) { //英雄机与boss激光碰撞
			if(hero.hit(eb) && !hero.isUnHurt() && eb.isAlive()) {
				hero.hurt(eb.getFirePower());
			}
		}
		synchronized (actionList) {
			actionList.addAll(seniorEnemies);
			actionList.addAll(lowerEnemies);
			for (FlyingObject fb : actionList) { //英雄机与敌人单位碰撞
				if(hero.hit(fb) && !hero.isUnHurt() && fb.isAlive()) {
					fb.goDead();
					hero.hurt(99999);
				}
			}
			actionList.clear();
		}
	}
	
	/**
	 * 敌机加入游戏
	 */
	public void enterAction() {
		if(timeIndex<=12000) {
			if(timeIndex%60==0) {
				int id =enterMap.get(random.nextInt(35));
				switch (id) {
				case ObjectPool.BUMP_PLANE:
					for(int i=0;i<random.nextInt(2)+1;i++) {
					lowerEnemies.add((LowerEnemy)ObjectPool.enterFlyingObject(id));
					}
					break;
				case ObjectPool.SMALL_PLANE:
					for(int i=0;i<random.nextInt(2)+1;i++) {
						seniorEnemies.add((SeniorEnemy)ObjectPool.enterFlyingObject(id));
					}
					break;
				case ObjectPool.BIG_PLANE:
					seniorEnemies.add((SeniorEnemy)ObjectPool.enterFlyingObject(id));
					break;
				}
			}
			if(timeIndex%1500==0 && timeIndex<12000) {
				for (int i = 0; i < 2; i++) {
					seniorEnemies.add((SeniorEnemy)ObjectPool.enterFlyingObject(ObjectPool.BOMBER));
				}
			}
			if(timeIndex%3000==0) {
				int t = random.nextInt(2);
				int rx = random.nextInt(100);
				for (int i = 0; i < 8; i++)
					lowerEnemies.add((LowerEnemy)ObjectPool.enterFlyingObject(
							rx+50+i*25, -200*t+(-1*i*25*(-2*t+1)), ObjectPool.MINE));
			}
		}
		if(timeIndex==12000) {
			for (int i = 0; i < 8; i++)
				lowerEnemies.add((LowerEnemy)ObjectPool.enterFlyingObject(
						10+i*50, -23, ObjectPool.BUMP_PLANE));
		}
		if(timeIndex==12500)
			boss = new Boss();
	}
	
	/**
	 * 飞行物移动
	 */
	public void stepAction() {
		synchronized (actionList) {
			actionList.add(sky);
			addActionList();
			actionList.addAll(bossLaserLights);
			for (FlyingObject fo : actionList) {
				fo.step();
			}
			actionList.clear();
			synchronized (heroBombs) {
				for (HeroBullet hb : heroBombs) {
					hb.step();
				}
			}
		}
		hero.step();   //hero的step方法用于射击动作
	}
	
	/**
	 * 飞行物碰撞
	 */
	public void BangAction() {
		enemyBangAction();
		heroBangAction();
	}
	
	/**
	 * boss所有动作，写在一起方便检查boss是否为null
	 * 画出图像动作在paint方法里执行,检查游戏结束在
	 * boss的移动、检测是否与英雄机、英雄机子弹碰撞都在这里进行
	 */
	public void bossAction() {
		boss.step();     //boss一定比激光后移动，因为boss移动会发射激光
		enemyBang(boss); //boss与英雄机子弹碰撞
		if(hero.hit(boss) && boss.isAlive()) //英雄机碰到boss直接死亡，游戏结束
			hero.hurt(99999);
	}
	
	/**
	 * 删除飞行物
	 */
	public void removeAction() {
		removeFlyingObject(heroBullets);
		removeFlyingObject(enemyBullets);
		removeFlyingObject(lowerEnemies);
		removeFlyingObject(seniorEnemies);
		removeFlyingObject(bossLaserLights);
		synchronized (heroBombs) {
			removeFlyingObject(heroBombs);
		}
		hero.getAwards(awards);  //遍历奖励对象，让英雄机获取奖励
	}
	
	/**
	 * 删除链表中的越界、待删除的飞行物，并回收到对象池
	 * @param list
	 */
	@SuppressWarnings("rawtypes")
	private void removeFlyingObject(LinkedList list) {
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			FlyingObject fo = (FlyingObject) iterator.next();
			if(fo.isRemoved() || fo.outOfBounds()) {
				ObjectPool.recycle(fo);
				iterator.remove();
			}
		}
	}
	
	/**
	 * 检查游戏是否结束
	 */
	public void checkGameOverAction() {
		if(hero.getLives()<=0)
			state = GAME_OVER;
		if(boss!=null && boss.isRemoved())
			state = WIN;
	}
	
	/**
	 * 初始化游戏状态，重新开始游戏
	 */
	public void init() {
		addActionList();
		actionList.addAll(bossLaserLights);
		actionList.addAll(heroBombs);
		for (FlyingObject fo : actionList) {
			ObjectPool.recycle(fo);
		}
		actionList.clear();
		enemyBullets.clear();
		heroBullets.clear();
		heroBombs.clear();
		seniorEnemies.clear();
		lowerEnemies.clear();
		bossLaserLights.clear();
		hero.init();
		sky.init();
		boss = null;
		awards.clear();
		timeIndex = 1;
		state = START;
	}
	
	/**
	 * 游戏运行过程
	 */
	public void action() {
		//鼠标侦听，键盘侦听直接在窗口对象中添加
		mouseListener();
		//定时循环，10毫秒间隔
		Timer timer = new Timer();
		long interval = 15;
		timer.schedule(new TimerTask() {
			public void run() {
				if(state==RUNNING) {
					timeIndex++;
					enterAction();
					BangAction();
					stepAction();
					if(boss!=null)
						bossAction();
					removeAction();
					checkGameOverAction();
				}
				repaint();
			}
		},interval,interval);
	}
	
	/**
	 * 侦听鼠标操作并作出相应动作
	 */
	private void mouseListener() {
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if(state==RUNNING) {
					int x = e.getX();      //获取鼠标x坐标
					int y = e.getY();      //获取鼠标y坐标
					hero.moveTo(x-25, y);  //英雄机随鼠标移动
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				switch(state) {
				case START:
					state = RUNNING;
					break;
				case PAUSE:
					state = RUNNING;
					break;
				case GAME_OVER:
				case WIN:
					init();   //初始化游戏并重新开始
					break;
				}
			}
		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
	}
	
	/**
	 * 绘制图像，主要通过repaint调用
	 * 注意：绘制时，一定要让天空对象处于首位，最先画出！！
	 */
	public void paint(Graphics g) {
		synchronized (actionList) {
			actionList.add(sky);
			addActionList();
			actionList.addAll(heroBombs);
			actionList.addAll(bossLaserLights);
			for (FlyingObject fo : actionList) {
				fo.paintObject(g);
			}
			actionList.clear();
		}
		hero.paintObject(g);
		if(boss!=null)
			boss.paintObject(g);
		//在窗口中显示英雄机属性
		g.setColor(Color.WHITE);
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		g.drawString("生 命: "+hero.getLives(), 10, 20);
		g.drawString("血 量: "+hero.getBlood(), 10, 45);
		g.drawString("必杀技: "+hero.getBombs(), 10, 70);
		g.drawString("火力值: "+hero.getDoubleFire(), 10, 95);
		
		switch (state) {   //绘制游戏状态图片，最后绘制，显示在所有图片上面
		case START:
			g.drawImage(start, 0, 10, null);
			g.setColor(Color.YELLOW);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
			g.drawString("回车键暂停  空格键发射必杀技", 45, 400);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 10, null);
			break;
		case GAME_OVER:
			g.drawImage(gameOver, 0, 10, null);
			g.setColor(Color.RED);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
			g.drawString("点击鼠标左键重新开始", 90, 400);
			break;
		case WIN:
			g.setColor(Color.RED);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
			g.drawString("胜    利", 130, 300);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
			g.drawString("点击鼠标左键重新开始", 90, 400);
			break;
		}
	}
	
	/**
	 * 游戏运行主方法
	 * @param args
	 */
	public static void main(String[] args) {
		new ObjectPool(); //游戏运行开始，先强制加载对象池类
		//窗口对象
		JFrame frame = new JFrame();
		World world = new World();
		frame.add(world);
		frame.addKeyListener(l); //添加键盘侦听对象
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null); 
		frame.setVisible(true);     //绘制窗口，并且调用paint函数
		
		//将鼠标设置为透明
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img=tk.getImage("");
		Cursor cu=tk.createCustomCursor(img,new Point(10,10),"stick");
		frame.setCursor(cu);
		
		world.action();  //游戏运行
	}
}























