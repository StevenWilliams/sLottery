package org.stevenw.mc.lottery.events;

import org.stevenw.mc.lottery.Lottery;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class LotteryTicketBuyEvent extends Event {
    public LotteryTicketBuyEvent(Lottery lottery, UUID player, int quantity, long cost) {

    }
    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
