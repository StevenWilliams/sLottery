package org.stevenw.mc.lottery.exceptions;

public class LotteryPurchaseNoAccessException extends LotteryPurchaseException {
    public LotteryPurchaseNoAccessException(String lotteryName) {
        super("You do not have access to " + lotteryName);
    }
}
