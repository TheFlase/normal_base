package com.wgc.thread.safeAndUnsafe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author wgc
 * @Description // 容器线程不安全示例
 * @Date 2021/11/24
 **/
public class ContainerNotSaveDemo {


    public static void main(String[] args) {
//        listNotSave();
//        setNotSave();
        mapNotSave();
    }

    /**
     * ArrayList线程不安全
     * 故障现象：java.util.ConcurrentModificationException
     *
     * 故障原因：
     * 线程不安全
     *
     * 解决方法
     * 1.使用线程安全类 Vector
     * 2.Collections.synchronizedList(new ArrayList<>())
     * 3.使用CopyOnWriteArrayList (写时复制加锁实现)
     * 思想：写时复制（加锁扩容1个容量）；读写分离的思想
     * CopyOnWrite容器即写时复制的容器。往一个容器添加元素的时候，不直接往当前容器Object[]添加，
     * 而是先将当前容器Object[]进行copy，复制出一个新的容器Object[] newElements，
     * 然后往新的容器里添加元素，添加完毕后，将原来容器的引用指向新容器setArray(newElements)。
     * 这样做的好处是可以对CopyOnWrite容器进行并发的读，而不需要加锁，因为当前元素不添加任何元素。
     */
    private static void listNotSave() {
        //        List<String> list = new ArrayList<>();
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
//        List<String> list = new Vector<>();
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 30; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }

    /**
     * HashSet线程不安全
     * 故障现象：java.util.ConcurrentModificationException
     *
     * 故障原因：
     * 线程不安全
     *
     * 解决方法
     * 1.Collections.synchronizedSet(new HashSet<>())
     * 2.使用CopyOnWriteArraySet (写时复制加锁实现)
     *
     * tips:
     * HashSet底层是通过HashMap实现的
     */
    private static void setNotSave() {
//        Set<String> set = new HashSet<>();
//        Set<String> set = Collections.synchronizedSet(new HashSet<>());
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 30; i++) {
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(set);
            },String.valueOf(i)).start();
        }
    }

    /**
     * 解决方法
     * 1.Collections.synchronizedMap(new HashMap<>())
     * 2.ConcurrentHashMap
     */
    private static void mapNotSave() {
//        Map<String,String> map = new HashMap();
//        Map<String,String> map1 = Collections.synchronizedMap(new HashMap<>());
        Map<String,String> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 30; i++) {
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,8));
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }
}
