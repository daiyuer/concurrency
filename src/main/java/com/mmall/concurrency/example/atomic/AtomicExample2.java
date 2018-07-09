package com.mmall.concurrency.example.atomic;

import com.mmall.concurrency.annoations.ThreadSafe;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ThreadSafe
public class AtomicExample2 {
  //同时并发的线程数
  private static int threadTotal = 200;
  //请求总数
  private static int clientTotal = 5000;
  private static AtomicLong count = new AtomicLong(0);

  public  static  void  main(String[] args) throws  Exception{
    ExecutorService exec = Executors.newCachedThreadPool();
    final Semaphore semaphore = new Semaphore(threadTotal);
    final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
    for (int index = 0 ; index <clientTotal ; index++) {
      exec.execute(()->{
        try{
          semaphore.acquire();
          add();
          semaphore.release();
        }catch (Exception e){
          log.error("exception",e);
        }
        countDownLatch.countDown();
      });
    }
    countDownLatch.await();
    exec.shutdown();
    log.info("count:{}",count.get());
  }
  private  static void add(){
    count.incrementAndGet();
    //count.getAndIncrement();
  }

}
