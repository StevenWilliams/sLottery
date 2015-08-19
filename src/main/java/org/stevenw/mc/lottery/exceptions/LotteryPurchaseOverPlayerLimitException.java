package org.stevenw.mc.lottery.exceptions;

public class LotteryPurchaseOverPlayerLimitException extends LotteryPurchaseException {
    public LotteryPurchaseOverPlayerLimitException(String message) {
        super(message);
    }
}
