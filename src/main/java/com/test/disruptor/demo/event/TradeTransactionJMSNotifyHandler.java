package com.test.disruptor.demo.event;

import com.lmax.disruptor.EventHandler;
import com.test.disruptor.demo.domain.TradeTransaction;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sunboyu
 * @date 2018/2/27
 */
@Slf4j
public class TradeTransactionJMSNotifyHandler implements EventHandler<TradeTransaction> {

    @Override
    public void onEvent(TradeTransaction tradeTransaction, long l, boolean b) throws Exception {
        log.info("最后一个消费者C3");
    }
}
