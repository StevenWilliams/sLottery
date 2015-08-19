package org.stevenw.mc.lottery.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.stevenw.mc.lottery.data.LotteryWin;

public class LotteryWinEvent extends Event {
    private LotteryWin win;
    public LotteryWinEvent(LotteryWin win) {
        this.win = win;
    }
    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    public LotteryWin getWin(){
        return this.win;
    }
}
