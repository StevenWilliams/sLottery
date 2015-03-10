package net.vulcanmc.lottery;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class LotteryManager {
    private Plugin plugin;

    public LotteryManager(Plugin plugin) {
        this.plugin = plugin;
    }

    private ArrayList<AbstractLottery> lotteries = new ArrayList();

    public ArrayList<AbstractLottery> getLotteries() {
        return this.lotteries;
    }
    public boolean enableLottery(AbstractLottery lottery) {
        return this.lotteries.add(lottery);
    }
    public boolean disableLottery(AbstractLottery lottery) {
        return this.lotteries.remove(lottery);
    }

    public AbstractLottery getLottery(String name) {
        for (AbstractLottery lottery : this.lotteries) {
            if(lottery.name.toLowerCase().equals(name.toLowerCase())) {
                return lottery;
            }
        }
        return null;
    }
}
