package org.stevenw.mc.lottery.exceptions;

public class LotteryPurchaseNoMoneyException extends LotteryPurchaseException {
    public LotteryPurchaseNoMoneyException() {
        super("Ticket purchased failed! Do you have enough money?");
    }
}
