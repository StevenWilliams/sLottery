package org.stevenw.mc.lottery.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.stevenw.mc.lottery.data.LotteryPurchase;
import org.stevenw.mc.lottery.events.LotteryPurchaseEvent;
import org.stevenw.mc.lottery.sLottery;

public class PurchaseListener implements Listener {
    private sLottery plugin;
    public PurchaseListener(sLottery sLottery) {
        this.plugin = sLottery;
    }
    @EventHandler
    public void notifyPlayer(LotteryPurchaseEvent e) {
        LotteryPurchase purchase = e.getPurchase();
        Player player = plugin.getServer().getPlayer(purchase.getPlayer());
        player.sendMessage("You have bought " + purchase.getQuantity() + " tickets for " + purchase.getLottery().getName());
    }
    @EventHandler
    public void taxPurchase(LotteryPurchaseEvent e) {
        LotteryPurchase purchase = e.getPurchase();
        double tax = purchase.getCost() * .10; //TODO: config it. now 10 percent tax
        plugin.getLogger().info("tax: " + tax);
        purchase.setValue(purchase.getCost() - tax);
    }

}
