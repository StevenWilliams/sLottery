package org.stevenw.mc.lottery;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class LotteryManager {
    private Plugin plugin;

    public LotteryManager(Plugin plugin) {
        this.plugin = plugin;
    }

    private ArrayList<Lottery> lotteries = new ArrayList<>();

    public List<Lottery> getLotteries() {
        return this.lotteries;
    }
    public boolean enableLottery(Lottery lottery) {
        return this.lotteries.add(lottery);
    }
    public boolean disableLottery(Lottery lottery) {
        return this.lotteries.remove(lottery);
    }

    public Lottery getLottery(String name) {
        for (Lottery lottery : this.lotteries) {
            if(lottery.getName().toLowerCase().equals(name.toLowerCase())) {
                return lottery;
            }
        }
        return null;
    }
}
