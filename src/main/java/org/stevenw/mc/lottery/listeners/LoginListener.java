package org.stevenw.mc.lottery.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.stevenw.mc.lottery.Lottery;
import org.stevenw.mc.lottery.data.LotteryWin;
import org.stevenw.mc.lottery.sLottery;

import java.util.List;

public class LoginListener implements Listener {
    private sLottery plugin;
    public LoginListener(sLottery plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void remindUnclaimed(PlayerJoinEvent e) {

        Player player = e.getPlayer();
        List<Lottery> lotteries = plugin.getLotteryManager().getLotteries();
        for(Lottery lottery : lotteries) {
            if(lottery.getConfig().getBoolean("remind-unclaimed-on-join", true)) {
                List<LotteryWin> unclaimedWins = lottery.getData().getUnclaimed(player.getUniqueId());
                for (LotteryWin win : unclaimedWins) {
                    player.sendMessage("You have won " + win.getAmount() + " in " + win.getLottery().getName());
                }
            }
        }
    }
}