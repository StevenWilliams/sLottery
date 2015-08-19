package org.stevenw.mc.lottery.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.stevenw.mc.lottery.Lottery;
import org.stevenw.mc.lottery.data.LotteryWin;
import org.stevenw.mc.lottery.events.LotteryWinEvent;
import org.stevenw.mc.lottery.sLottery;

public class WinListener implements Listener {
    private sLottery plugin;
    public WinListener(sLottery plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void broadcastOnWin(LotteryWinEvent e) {
        LotteryWin win = e.getWin();
        Lottery lottery = win.getLottery();
        if(!lottery.getConfig().getBoolean("broadcast-win", true)) return;
        plugin.getServer().broadcastMessage(win.getPlayer() + " has won " + win.getAmount() + " in " + lottery.getDisplayName());
    }

    @EventHandler
    public void claimIfOnline(LotteryWinEvent e) {
        LotteryWin win = e.getWin();
        Lottery lottery = win.getLottery();
        if(this.plugin.getServer().getOfflinePlayer(win.getPlayer()).isOnline()) {
            Player player = this.plugin.getServer().getPlayer(win.getPlayer());
            lottery.claim(player);
            player.sendMessage("Congratulations! You have received " + win.getAmount() + " from " + lottery.getDisplayName());
        }
    }
}
