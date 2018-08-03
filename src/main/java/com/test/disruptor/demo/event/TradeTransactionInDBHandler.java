package com.test.disruptor.demo.event;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.test.disruptor.demo.domain.TradeTransaction;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @author sunboyu
 * @date 2018/2/27
 */
@Slf4j
public class TradeTransactionInDBHandler implements EventHandler<TradeTransaction>, WorkHandler<TradeTransaction> {

    @Override
    public void onEvent(TradeTransaction tradeTransaction) throws Exception {
        tradeTransaction.setId(UUID.randomUUID().toString());
        log.info("WorkHandler-第二个消费者C2消费了消息, 消息id: {}", tradeTransaction.getId());
    }

    @Override
    public void onEvent(TradeTransaction tradeTransaction, long l, boolean b) throws Exception {
        tradeTransaction.setId(UUID.randomUUID().toString());
        log.info("第二个消费者C2消费了消息, 消息id: {}", tradeTransaction.getId());
    }
}
