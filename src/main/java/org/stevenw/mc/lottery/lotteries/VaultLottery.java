package org.stevenw.mc.lottery.lotteries;

import net.milkbowl.vault.economy.Economy;
import org.stevenw.mc.lottery.Lottery;
import org.stevenw.mc.lottery.LotteryData;
import org.stevenw.mc.lottery.data.LotteryPurchase;
import org.stevenw.mc.lottery.data.LotteryWin;
import org.stevenw.mc.lottery.events.LotteryPurchaseEvent;
import org.stevenw.mc.lottery.events.LotteryWinEvent;
import org.stevenw.mc.lottery.sLottery;
import org.stevenw.mc.lottery.data.YAMLLotteryData;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.stevenw.mc.lottery.exceptions.*;

import java.security.SecureRandom;
import java.util.*;

public class VaultLottery implements Lottery {
    private ConfigurationSection config;
    private sLottery plugin;
    private LotteryData data;
    private String name;

    public static Economy econ = null;

    public VaultLottery(sLottery plugin,String lotteryName, LotteryData data, Configuration config) {
        this.name = lotteryName;
        this.plugin = plugin;
        this.config = config.getConfigurationSection("lotteries." + this.name);
        this.data = data;

        if (!setupEconomy() ) {
            plugin.getLogger().severe("Vault not found. Cannot use VaultLottery.");
            plugin.getLotteryManager().disableLottery(this);
        }
    }
    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


    private boolean deposit(Player player, Long amount) {
        return econ.depositPlayer(player, amount).transactionSuccess();
    }
    private boolean withdraw(Player player, Long amount) {
        return econ.withdrawPlayer(player, amount).transactionSuccess();
    }

    @Override
    public LotteryPurchase purchase(Player player, Integer quantity) throws LotteryPurchaseException {
       if(!player.hasPermission("sLotteries.lotteries." + this.name)) throw new LotteryPurchaseNoAccessException(this.name);

        int ticketsPurchased = this.data.getTicketsPurchased(player.getUniqueId()); //5
        int maxPlayerTickets = this.config.getInt("player-ticket-limit"); //6

        if(ticketsPurchased + quantity <= maxPlayerTickets) {
            Long ticketCost = config.getLong("ticket-cost");
            Long totalCost = ticketCost * quantity;
            if(this.withdraw(player, totalCost)) {
                LotteryPurchase purchase = new LotteryPurchase(this, player.getUniqueId(), totalCost, quantity);
                LotteryPurchaseEvent purchaseEvent = new LotteryPurchaseEvent(purchase);
                plugin.getServer().getPluginManager().callEvent(purchaseEvent);
                this.data.addToPot(purchaseEvent.getPurchase().getValue());
                this.data.addToTicketsPurchased(player.getUniqueId(), quantity);
                return purchase;
            } else {
                throw new LotteryPurchaseNoMoneyException();
            }
        } else {
            throw new LotteryPurchaseOverPlayerLimitException("You already have " + ticketsPurchased +"/" + maxPlayerTickets + " tickets!");
        }
    }

    @Override
    public Long claim(Player player) {
        Long amount = 0L;
        List<LotteryWin> unclaimed = this.data.getUnclaimed(player.getUniqueId());
        for(LotteryWin win : unclaimed) {
            amount += win.getAmount();
            this.data.setClaimed(win.getId());
        }
        this.deposit(player, amount);
        return amount;
    }

    @Override
    public boolean isEnabled() {
        return this.config.getBoolean("enabled");
    }

    @Override
    public LotteryData getData() {
        return this.data;
    }

    @Override
    public ConfigurationSection getConfig() {
        return this.config;
    }

    @Override
    public long getNextDraw() {
        return this.data.getLastDraw() + getAutomaticInterval();
    }

    @Override
    public boolean isAutomatic() {
        return this.config.getBoolean("automatic");
    }

    @Override
    public Long getAutomaticInterval() {
        return this.config.getLong("time") * 1000;
    }

    @Override
    public void reset() {
        this.data.resetCurrent();
    }

    private List<UUID> getTickets() {
       return data.getTicketsPurchased();
    }

    public static UUID pickRandomPlayer(List<UUID> tickets) {
        SecureRandom rand = new SecureRandom();
        Collections.shuffle(tickets, rand);
        return tickets.get(rand.nextInt(tickets.size()));
    }

    @Override
    public LotteryWin draw() throws LotteryDrawNoPlayersException {
        this.data.setLastDraw(System.currentTimeMillis());
        if(this.data.getTotalPlayers() == 0) {
            throw new LotteryDrawNoPlayersException(this.name);
        }
        Double pot = this.data.getPot();
        UUID uuid = pickRandomPlayer(getTickets());

        this.reset();

        LotteryWin win = this.data.addWin(uuid, pot);
        LotteryWinEvent winEvent = new LotteryWinEvent(win);
        plugin.getServer().getPluginManager().callEvent(winEvent);
        return win;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public Lottery init(sLottery plugin, String lotteryName, YAMLLotteryData data, Configuration config) {
        return new VaultLottery(plugin,lotteryName,data,config);
    }

}
