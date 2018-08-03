package com.test.disruptor.demo.test;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.test.disruptor.demo.domain.TradeTransaction;
import com.test.disruptor.demo.event.TradeTransactionInDBHandler;
import com.test.disruptor.demo.event.TradeTransactionJMSNotifyHandler;
import com.test.disruptor.demo.event.TradeTransactionVasConsumer;
import com.test.disruptor.demo.publisher.TradeTransactionPublisher;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author sunboyu
 * @date 2018/2/28
 */
@Slf4j
public class Demo3 {

    public static void main(String[] args) throws InterruptedException {

        long beginTime = System.currentTimeMillis();
        int bufferSize = 1024;

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        EventFactory<TradeTransaction> eventFactory = new EventFactory<TradeTransaction>() {
            @Override
            public TradeTransaction newInstance() {
                return new TradeTransaction();
            }
        };

        Disruptor<TradeTransaction> disruptor = new Disruptor<TradeTransaction>(
                eventFactory, bufferSize, executorService, ProducerType.SINGLE, new BusySpinWaitStrategy());

        //使用disruptor创建消费者组C1,C2
        EventHandlerGroup<TradeTransaction> eventHandlerGroup = disruptor.handleEventsWith(
                new TradeTransactionVasConsumer(), new TradeTransactionInDBHandler());

        TradeTransactionJMSNotifyHandler tradeTransactionJMSNotifyHandler = new TradeTransactionJMSNotifyHandler();

        //声明在C1,C2完事之后执行JMS消息发送操作 也就是流程走到C3
        eventHandlerGroup.then(tradeTransactionJMSNotifyHandler);

        //启动
        disruptor.start();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        executorService.submit(new TradeTransactionPublisher(disruptor, countDownLatch));
        countDownLatch.await();
        disruptor.shutdown();
        executorService.shutdown();

        log.info("总耗时: {}", (System.currentTimeMillis() - beginTime));
    }
}