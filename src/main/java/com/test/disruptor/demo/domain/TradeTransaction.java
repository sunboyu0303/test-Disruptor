package com.test.disruptor.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sunboyu
 * @date 2018/2/27
 */
@Data
@NoArgsConstructor
public class TradeTransaction {

    /**
     * 交易ID
     */
    private String id;
    /**
     * 交易金额
     */
    private double price;
}
