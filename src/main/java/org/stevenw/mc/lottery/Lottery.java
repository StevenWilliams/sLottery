package org.stevenw.mc.lottery;

import org.bukkit.configuration.ConfigurationSection;
import org.stevenw.mc.lottery.data.LotteryPurchase;
import org.stevenw.mc.lottery.data.LotteryWin;
import org.stevenw.mc.lottery.data.YAMLLotteryData;
import org.stevenw.mc.lottery.exceptions.LotteryDrawNoPlayersException;
import org.stevenw.mc.lottery.exceptions.LotteryPurchaseException;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public interface Lottery {

    LotteryPurchase purchase(Player player, Integer quantity) throws LotteryPurchaseException;

    Long claim(Player player);

    boolean isEnabled();

    LotteryData getData();

    ConfigurationSection getConfig();

    long getNextDraw();

    boolean isAutomatic();

    Long getAutomaticInterval();

    void reset();

    LotteryWin draw() throws LotteryDrawNoPlayersException;

    String getName();

    String getDisplayName();

    Lottery init(sLottery plugin, String lotteryName, YAMLLotteryData data, Configuration config);
}
