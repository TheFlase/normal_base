package com.wgc.concurrent.atomic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author wgc
 * @Description // 原子引用  解决ABA问题
 * @Date 2021/11/24
 **/

public class AtomicReferenceDemo {
    public static void main(String[] args) {
        User zhangsan = new User("张三",23);
        User lisi = new User("李四",22);
        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(zhangsan);
        boolean updateResult = atomicReference.compareAndSet(zhangsan, lisi);
        System.out.println("第一次更新结果：" + updateResult+"\t "+atomicReference.get().toString());
        System.out.println("第二次更新结果：" + atomicReference.compareAndSet(zhangsan, lisi)+"\t "+atomicReference.get().toString());

    }


    @Getter
    @ToString
    @AllArgsConstructor
    static class User{
        private String userName;
        private Integer age;
    }
}

