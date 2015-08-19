package org.stevenw.mc.lottery.runnables;

import org.stevenw.mc.lottery.Lottery;
import org.stevenw.mc.lottery.exceptions.LotteryDrawNoPlayersException;
import org.stevenw.mc.lottery.sLottery;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class LotteryDrawChecker extends BukkitRunnable {
    private sLottery plugin;
    public LotteryDrawChecker(sLottery plugin) {
        this.plugin = plugin;
    }
    @Override
    public void run() {
        List<Lottery> lotteries = plugin.getLotteryManager().getLotteries();
        for(Lottery lottery : lotteries) {
            if(lottery.getNextDraw() <= System.currentTimeMillis())
            {
  //              plugin.getLogger().info("Drawing lottery: " + lottery.getName());
                try {
                    lottery.draw();
                } catch (LotteryDrawNoPlayersException e) {
//                    plugin.getLogger().info("Lottery was not drawn! No players entered!");
                }
                plugin.getLogger().info("Drew lottery: " + lottery.getName());
            }
        }
    }
}
