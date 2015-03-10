package net.vulcanmc.lottery;

import net.vulcanmc.lottery.commands.exceptions.LotteryPurchaseException;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class AbstractLottery {
    public  String name;
    public String displayName;
    private ConfigurationSection data;

    public abstract Long purchase(Player player, Integer quantity) throws LotteryPurchaseException;

    public abstract Integer getTicketsPurchased(Player player);

    public abstract Integer getTotalTickets();

    public abstract Long getPotAmount();

    public abstract ArrayList<OfflinePlayer> getWinners();

    public abstract boolean hasWon(OfflinePlayer player);
   // public abstract boolean hasClaimed(OfflinePlayer player);

    public abstract boolean hasUnclaimed(OfflinePlayer player);

    public abstract Long claim(Player player);

    public abstract boolean isEnabled();
    public abstract void claim();

    public abstract boolean draw();
    public String getName() {
        return this.name;
    }
}
