package org.stevenw.mc.lottery.exceptions;

public class LotteryPurchaseOverLimitException extends LotteryPurchaseException {
    public LotteryPurchaseOverLimitException(String message) {
        super(message);
    }
}
