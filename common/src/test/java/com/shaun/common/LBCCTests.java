package com.shaun.common;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.support.locks.ExpirableLockRegistry;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@ActiveProfiles("test")
@SpringBootTest
public class LBCCTests {

    private static final Logger log = LoggerFactory.getLogger(LBCCTests.class);

    private static int COUNT_NOLOCK = 0;

    private static int COUNT_MUTEX = 0;

    private static int RUNNNER_COUNT = 10000;

    private static CountDownLatch countDownLatch = new CountDownLatch(RUNNNER_COUNT);

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    ExpirableLockRegistry expirableLockRegistry;

    @Test
    void A_LBCC_Mutex() throws InterruptedException {

        List<Thread> threadList = new ArrayList<>(RUNNNER_COUNT);

        for (int i = 0; i < RUNNNER_COUNT; i++) {
            MutexThread runnable = new MutexThread();

//            executorService.submit(runnable);

            Thread thread = new Thread(runnable);

            threadList.add(thread);
        }

        for (int i = 0; i < RUNNNER_COUNT; i++) {
            threadList.get(i).start();
        }

        countDownLatch.await();

        log.info("LBCC Result: " + COUNT_MUTEX);

        assert (RUNNNER_COUNT == COUNT_MUTEX);
    }

    @Test
    void B_LBCC_NoLock() throws InterruptedException {

        List<Thread> threadList = new ArrayList<>(RUNNNER_COUNT);

        for (int i = 0; i < RUNNNER_COUNT; i++) {
            SingeThread runnable = new SingeThread();
            Thread thread = new Thread(runnable);
            threadList.add(thread);
        }

        for (int i = 0; i < RUNNNER_COUNT; i++) {
            threadList.get(i).start();
        }

        countDownLatch.await();

        log.info("LBCC Result: " + COUNT_NOLOCK);

        assert (RUNNNER_COUNT != COUNT_NOLOCK);
    }


    class MutexThread implements Runnable {
        @Override
        public void run() {
            boolean locked = false;
            Lock lock = expirableLockRegistry.obtain("resource-test");
            try {
                do {
                    locked = lock.tryLock(3, TimeUnit.SECONDS);
                    log.info("TRY GET MUTEX：" + locked + " " + LocalDateTime.now() + " ---- " + Thread.currentThread().getName());
                } while (!locked);
                log.info("GETTED MUTEX：" + LocalDateTime.now() + " ---- " + Thread.currentThread().getName());
                ++COUNT_MUTEX;
                log.info("ADD COUNT：" + COUNT_MUTEX + " ---- " + Thread.currentThread().getName());
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (locked) {
                    log.info("UNLOCK MUTEX：" + LocalDateTime.now() + " ---- " + Thread.currentThread().getName());
                    lock.unlock();
                }
            }
        }
    }

    class SingeThread implements Runnable {
        @Override
        public void run() {
            try {
                ++COUNT_NOLOCK;
                log.info("ADD COUNT：" + COUNT_NOLOCK + " ---- " + Thread.currentThread().getName());
                Thread.sleep(0);
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

}
