package objectPool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import bullet.enemyBullet.*;
import bullet.heroBullet.*;
import enemy.lowerEnemy.*;
import enemy.seniorEnemy.*;
import interest.sidney.main.InitXY;
import superClass.FlyingObject;


/**
 * 对象池
 * 该类成员变量与方法都是静态方法
 * 
 * 将每一个多次出现的游戏对象，都建立对应的对象池，使用Linkedist集合。
 * 
 * 每一个游戏对象实现类都设置常量编号，常量名即类名大写。
 * 
 * 取出对象时，需要注意返回值类型。
 * 
 * 回收对象时，可以通过验证对象类编号属性，判断属于哪个集合即可回收。
 * 回收对象时，强制调用总初始化方法。
 * 
 * @author Sidney
 *
 */
public class ObjectPool {
	/*
	 * 游戏中部分实现类的常量编号
	 * 1~10:敌机
	 * 11~20:地雷等固定物体(速度都是世界速度,相对静止)
	 * 51~60:敌人子弹
	 * 61~70:英雄机子弹
	 */
	public static final int HERO = 1;
	public static final int BUMP_PLANE = 2;
	public static final int SMALL_PLANE = 3;
	public static final int BIG_PLANE = 4;
	public static final int BOMBER = 5;
	public static final int BOSS = 6;
	public static final int MINE = 11;
	public static final int ENEMY_BULLET = 51;
	public static final int BOSS_BULLET = 52;
	public static final int BOSS_LASER_LIGHT = 53;
	public static final int HERO_BULLET = 61;
	public static final int HERO_BOMB = 62;
	public static final int HERO_BOMB_BLAST = 63;
	public static final int HERO_TRACKING_BULLET = 64;
	
	/*
	 * classMap:类对象Map
	 * 将需要存入对象池的对象的类保存下来，方便在对象池中没对象时，
	 * 调用该Map中的类对象new一个需要的新对象。
	 * 
	 * listMap:对象链表Map
	 * 每一个对象链表都装到该查找表中，方便初始化时遍历。
	 * K为链表元素的classID，V为链表对象。
	 */
	@SuppressWarnings("rawtypes")
	private static Map<Integer, Class> classMap;
	@SuppressWarnings("rawtypes")
	private static Map<Integer, LinkedList> listMap;
	
	/**
	 * 静态块，初始化对象池
	 */
	static {
		initClassMap();
		initListMap();
	}
	
	/**
	 * 供静态块调用的ClassMap初始化方法。
	 */
	@SuppressWarnings("rawtypes")
	public static void initClassMap() {
		classMap = new HashMap<Integer, Class>(16);
		classMap.put(BUMP_PLANE, BumpPlane.class);
		classMap.put(SMALL_PLANE, SmallPlane.class);
		classMap.put(BIG_PLANE, BigPlane.class);
		classMap.put(BOMBER, Bomber.class);
		classMap.put(MINE, Mine.class);
		classMap.put(ENEMY_BULLET, EnemyBullet.class);
		classMap.put(BOSS_BULLET, BossBullet.class);
		classMap.put(BOSS_LASER_LIGHT, BossLaserLight.class);
		classMap.put(HERO_BULLET, HeroBullet.class);
		classMap.put(HERO_BOMB, HeroBomb.class);
		classMap.put(HERO_BOMB_BLAST, HeroBombBlast.class);
		classMap.put(HERO_TRACKING_BULLET, HeroTrackingBullet.class);
	}
	
	/**
	 * 供静态块调用的listMap初始化方法。
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	private static void initListMap() {
		listMap = new HashMap<Integer,LinkedList>(16);
		listMap.put(BUMP_PLANE, new LinkedList<BumpPlane>());
		listMap.put(SMALL_PLANE, new LinkedList<SeniorEnemy>());
		listMap.put(BIG_PLANE, new LinkedList<SeniorEnemy>());
		listMap.put(BOMBER, new LinkedList<SeniorEnemy>());
		listMap.put(MINE, new LinkedList<Mine>());
		listMap.put(ENEMY_BULLET, new LinkedList<EnemyBullet>());
		listMap.put(BOSS_BULLET, new LinkedList<BossBullet>());
		listMap.put(BOSS_LASER_LIGHT, new LinkedList<BossLaserLight>());
		listMap.put(HERO_BULLET, new LinkedList<HeroBullet>());
		listMap.put(HERO_BOMB, new LinkedList<HeroBullet>());
		listMap.put(HERO_BOMB_BLAST, new LinkedList<HeroBullet>());
		listMap.put(HERO_TRACKING_BULLET, new LinkedList<HeroBullet>());
		
		//设置每个链表中的对象个数
		Map<Integer,Integer> objectNum = new HashMap<Integer,Integer>(16);
		objectNum.put(BUMP_PLANE, 20);
		objectNum.put(SMALL_PLANE, 12);
		objectNum.put(BIG_PLANE, 6);
		objectNum.put(BOMBER, 8);
		objectNum.put(MINE, 12);
		objectNum.put(ENEMY_BULLET, 12);
		objectNum.put(BOSS_BULLET, 30);
		objectNum.put(BOSS_LASER_LIGHT, 3);
		objectNum.put(HERO_BULLET, 22);
		objectNum.put(HERO_BOMB, 2);
		objectNum.put(HERO_BOMB_BLAST, 2);
		objectNum.put(HERO_TRACKING_BULLET, 44);
		
		Set<Integer> IDSet = listMap.keySet();  //获取classID链表
		try {
			for (Integer id : IDSet) {
				Class cls = classMap.get(id);
				for(int i=0; i<objectNum.get(id).intValue(); i++) {
					listMap.get(id).add(cls.newInstance());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("对象池用反射new飞行物对象时，发生错误。");
		}
	}
	
	/**
	 * 飞行物加入游戏
	 * @param classID
	 * @return
	 */
	public static FlyingObject enterFlyingObject(int classID) {
		@SuppressWarnings({ "unchecked"})
		LinkedList<FlyingObject> list = listMap.get(classID);
		if(list.isEmpty())
			try {
//				System.out.println(classID+" : +1");
				return (FlyingObject)classMap.get(classID).newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		return list.poll();
	}
	
	/**
	 * 飞行物加入游戏并设置坐标
	 * 使用此方法的对象，必须实现InitXY接口！！
	 * @param x
	 * @param y
	 * @param classID
	 * @return
	 */
	public static FlyingObject enterFlyingObject(int x, int y, int classID) {
		return ((InitXY)enterFlyingObject(classID)).initXY(x, y);
	}
	
	/**
	 * boss射击子弹，需要传入坐标和x轴方向
	 * @param x
	 * @param y
	 * @param xDirection
	 * @return
	 */
	public static BossBullet shootBossBullet(int x, int y, int xDirection) {
		return ((BossBullet)enterFlyingObject(x, y, BOSS_BULLET))  //获取子弹对象并设置坐标
				.initXDirection(xDirection)  //设置x轴方向
				.setIsBossShoot();  //设置是boss对象射击的子弹（会在移动一定距离有更改方向）
	}
	
	/**
	 * 回收对象,被回收的对象必须调用一次init方法
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	public static void recycle(FlyingObject obj) {
		listMap.get(obj.getClassID()).add(obj.init());
	}
}

































