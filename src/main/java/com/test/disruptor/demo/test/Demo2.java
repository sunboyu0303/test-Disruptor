package com.test.disruptor.demo.test;

import com.lmax.disruptor.*;
import com.test.disruptor.demo.domain.TradeTransaction;
import com.test.disruptor.demo.event.TradeTransactionInDBHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author sunboyu
 * @date 2018/2/28
 */
public class Demo2 {

    private static final int BUFFER_SIZE = 1024;
    private static final int THREAD_NUMBERS = 4;

    public static void main(String[] args) throws InterruptedException {

        EventFactory<TradeTransaction> eventFactory = new EventFactory<TradeTransaction>() {
            @Override
            public TradeTransaction newInstance() {
                return new TradeTransaction();
            }
        };

        RingBuffer<TradeTransaction> ringBuffer = RingBuffer.createSingleProducer(eventFactory, BUFFER_SIZE);

        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMBERS);

        WorkHandler<TradeTransaction> workHandler = new TradeTransactionInDBHandler();

        WorkerPool<TradeTransaction> workerPool = new WorkerPool<TradeTransaction>(
                ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), workHandler);

        workerPool.start(executorService);

        for (int i = 0; i < 1000; i++) {
            long seq = ringBuffer.next();
            ringBuffer.get(seq).setPrice(Math.random() * 9999);
            ringBuffer.publish(seq);
        }

        Thread.sleep(1000);
        workerPool.halt();
        executorService.shutdown();
    }
}
