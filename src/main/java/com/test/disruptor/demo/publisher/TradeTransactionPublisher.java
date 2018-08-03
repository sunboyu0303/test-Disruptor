package com.test.disruptor.demo.publisher;

import com.lmax.disruptor.dsl.Disruptor;
import com.test.disruptor.demo.domain.TradeTransaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @author sunboyu
 * @date 2018/2/27
 */
@Slf4j
@AllArgsConstructor
public class TradeTransactionPublisher implements Runnable {

    private Disruptor<TradeTransaction> disruptor;
    private CountDownLatch latch;
    private static final int LOOP = 10000000;

    @Override
    public void run() {
        TradeTransactionEventTranslator tradeTransactionEventTranslator = new TradeTransactionEventTranslator();
        for (int i = 0; i < LOOP; i++) {
            disruptor.publishEvent(tradeTransactionEventTranslator);
        }
        latch.countDown();
    }
}
