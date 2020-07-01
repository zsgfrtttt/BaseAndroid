package com.hydbest.baseandroid.activity.concurrent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReentrantLockActivity extends AppCompatActivity{

    private ReentrantLock mLock = new ReentrantLock();

    private Thread mThread ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hydbest.baseandroid.R.layout.activity_reentrant_lock);

    }

    public void interupt(View view){
        mThread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    mLock.lockInterruptibly();
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    if (mLock.isHeldByCurrentThread()){
                        mLock.unlock();
                    }
                }
            }
        };
        mThread.start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                mThread.interrupt();
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mLock.lock();
                    Log.i("csz", "gogogo!");
                }

        }).start();

    }

    public void await(View view){
        ReentrantLock lock = new ReentrantLock();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mLock.lock();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {

                }
                Log.i("csz","kkk");
                if (mLock.isHeldByCurrentThread()) {
                    mLock.unlock();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mLock.tryLock(3, TimeUnit.SECONDS)) {
                        Log.i("csz","hasLock");
                    }else{
                        Log.i("csz","no Lock!");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void fail(View view){
        final ReentrantLock lock = new ReentrantLock(true);
        for (int i=0;i<100;i++){
            final int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        lock.lock();
                        Thread.sleep(1000);
                        Log.i("csz","i:="+j);
                        lock.unlock();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void condition(View view){
        final ReentrantLock lock = new ReentrantLock(true);
        final Condition condition = lock.newCondition();
        final Condition condition11 = lock.newCondition();

        new Thread(new Runnable() {
            @Override
            public void run() {
               // lock.newCondition();
                try {
                   lock.lock();
                   Log.i("csz",Thread.currentThread().getName() + "-线程开始等待...");
                    condition.await();
                    Log.i("csz",Thread.currentThread().getName() + "-线程继续进行了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                   lock.unlock();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // lock.newCondition();
                try {
                    lock.lock();
                    Log.i("csz",Thread.currentThread().getName() + "11-线程开始等待...");
                    condition.await();
                    Log.i("csz",Thread.currentThread().getName() + "11-线程继续进行了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("csz","nitify");
                lock.lock();
                condition.signalAll(); // 调用该方法前需要获取到创建该对象的锁否则会产生
                // java.lang.IllegalMonitorStateException异常
                lock.unlock();
            }
        }).start();
    }
}
