package chap04;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 看p65 例子4.7有感。 测试unmodifiable map. 有三个有趣的现象。
 * 同时测试了一下p66 4.8的map浅拷贝
 * @author xiuzhu
 * 160303
 * 
 * 结果：
 * 	oroginMap: {phan=100, tom=101, jack=101}
	unMap: {phan=100, tom=101, jack=101}
	shallowCopyMap: {phan=100, tom=100}
 */
public class TestUnmodifiableMap {
	
	public static void main(String[] args) {
		Map<String, ObjInMap> oroginMap = new HashMap<String, ObjInMap>();
		oroginMap.put("tom", new ObjInMap(100));
		oroginMap.put("phan", new ObjInMap(100));
		
		Map<String, ObjInMap> unMap = Collections.unmodifiableMap(oroginMap);
		
		//1： change to unmodifiable map is not OK!!!
		try {
			unMap.put("will fail", new ObjInMap(100));
		} catch (Exception e) {
			System.out.println("unsupport!");
		}
		try {
			unMap.remove("tom");
		} catch (Exception e) {
			System.out.println("unsupport!");
		}
		try {
			unMap.replace("phan", new ObjInMap(101));
		} catch (Exception e) {
			System.out.println("unsupport!");
		}
		
		//test p66 静态拷贝/浅拷贝
		Map<String, ObjInMap> shallowCopyMap = new HashMap<String, ObjInMap>(oroginMap);
		
		//2： But still can update original map!!
		oroginMap.put("jack", new ObjInMap(100));
		oroginMap.replace("jack", new ObjInMap(101));
		oroginMap.replace("tom", new ObjInMap(101));
		
		//!!!! 3： And change in origin map, can also be seen in unmodifiable map!!!!!
		System.out.println("oroginMap: " + oroginMap);
		System.out.println("unMap: " + unMap);
		
		//test p66 静态拷贝/浅拷贝 --- 返回的是一个镜像！！
		System.out.println("shallowCopyMap: " + shallowCopyMap);
	}

}
class ObjInMap{		//value in map
	public int key;
	public ObjInMap(int key){
		this.key = key;
	}
	public String toString(){
		return key + "";
	}
}
