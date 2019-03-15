package interest.sidney.main;

import superClass.FlyingObject;

/**
 * 初始化坐标接口
 * 
 * @author Sidney
 *
 */
public interface InitXY {
	/**
	 * 飞行物加入游戏前，初始化坐标
	 * @param x x坐标
	 * @param y y坐标
	 * @return 飞行物对象本身
	 */
	FlyingObject initXY(int x, int y);
}
