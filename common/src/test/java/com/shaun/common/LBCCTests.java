package com.shaun.common;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.support.locks.ExpirableLockRegistry;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

@ActiveProfiles("test")
@SpringBootTest
public class LBCCTests {

    private static final Logger log = LoggerFactory.getLogger(LBCCTests.class);

    private static int COUNT_NOLOCK = 0;

    private static int COUNT_MUTEX = 0;

    private static int RUNNNER_COUNT = 10000;

    private static CountDownLatch countDownLatch_Mutex = new CountDownLatch(RUNNNER_COUNT);

    private static CountDownLatch countDownLatch_NoLock = new CountDownLatch(RUNNNER_COUNT);

    private static CyclicBarrier cyclicBarrier_Mutex = new CyclicBarrier(RUNNNER_COUNT);

    private static CyclicBarrier cyclicBarrier_NoLock = new CyclicBarrier(RUNNNER_COUNT);

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Qualifier("JdbcExpirableLockRegistry")
    ExpirableLockRegistry expirableLockRegistry;

    @Test
    void A_LBCC_Mutex() throws InterruptedException {

        List<Thread> threadList = new ArrayList<>(RUNNNER_COUNT);

        for (int i = 0; i < RUNNNER_COUNT; i++) {
            MutexThread runnable = new MutexThread(cyclicBarrier_Mutex);

//            executorService.submit(runnable);

            Thread thread = new Thread(runnable);

            threadList.add(thread);
        }

        for (int i = 0; i < RUNNNER_COUNT; i++) {
            threadList.get(i).start();
        }

        countDownLatch_Mutex.await();

        log.info("LBCC Result: " + COUNT_MUTEX);

        assert (RUNNNER_COUNT == COUNT_MUTEX);
    }

    @Test
    void B_LBCC_NoLock() throws InterruptedException {

        for (int i = 0; i < RUNNNER_COUNT; i++) {
            SingeThread runnable = new SingeThread(cyclicBarrier_NoLock);
            Thread thread = new Thread(runnable);
            thread.start();
        }

        countDownLatch_NoLock.await();

        log.info("LBCC Result: " + COUNT_NOLOCK);

        assert (RUNNNER_COUNT != COUNT_NOLOCK);
    }


    class MutexThread implements Runnable {

        CyclicBarrier cyclicBarrier;

        MutexThread(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            boolean locked = false;
            Lock lock = expirableLockRegistry.obtain("resource-test");
            try {
                cyclicBarrier.await();

                do {
                    locked = lock.tryLock(3, TimeUnit.SECONDS);
                    log.info("TRY GET MUTEX：" + locked + " " + LocalDateTime.now() + " ---- " + Thread.currentThread().getName());
                } while (!locked);
                log.info("GETTED MUTEX：" + LocalDateTime.now() + " ---- " + Thread.currentThread().getName());

                Thread.sleep(new Random().nextInt(3));
                COUNT_MUTEX++;
                log.info("ADD COUNT：" + COUNT_MUTEX + " ---- " + Thread.currentThread().getName());
                countDownLatch_Mutex.countDown();
            } catch (InterruptedException | BrokenBarrierException e) {
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

        CyclicBarrier cyclicBarrier;

        SingeThread(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                cyclicBarrier.await();

                Thread.sleep(new Random().nextInt(3));
                COUNT_NOLOCK++;
                log.info("ADD COUNT：" + COUNT_NOLOCK + " ---- " + Thread.currentThread().getName());
                countDownLatch_NoLock.countDown();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

}
