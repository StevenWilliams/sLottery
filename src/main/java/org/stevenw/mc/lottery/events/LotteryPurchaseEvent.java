package org.stevenw.mc.lottery.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.stevenw.mc.lottery.data.LotteryPurchase;
import org.stevenw.mc.lottery.data.LotteryWin;

public class LotteryPurchaseEvent extends Event {

    private LotteryPurchase purchase;
    public LotteryPurchaseEvent(LotteryPurchase purchase) {
        this.purchase = purchase;
    }
    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    public LotteryPurchase getPurchase(){
        return this.purchase;
    }
}
