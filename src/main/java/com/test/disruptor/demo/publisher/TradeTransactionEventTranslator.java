package com.test.disruptor.demo.publisher;

import com.lmax.disruptor.EventTranslator;
import com.test.disruptor.demo.domain.TradeTransaction;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @author sunboyu
 * @date 2018/2/28
 */
@Slf4j
public class TradeTransactionEventTranslator implements EventTranslator<TradeTransaction> {

    private Random random = new Random();

    @Override
    public void translateTo(TradeTransaction tradeTransaction, long sequence) {
        log.info("当前执行seq: {}", sequence);
        this.generateTradeTransaction(tradeTransaction);
    }

    private TradeTransaction generateTradeTransaction(TradeTransaction tradeTransaction) {
        tradeTransaction.setPrice(random.nextDouble() * 9999);
        return tradeTransaction;
    }
}
