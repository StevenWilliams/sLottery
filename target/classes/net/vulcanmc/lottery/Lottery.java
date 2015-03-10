package net.vulcanmc.lottery;

import net.vulcanmc.lottery.commands.exceptions.LotteryPurchaseException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.*;

public class Lottery extends AbstractLottery{
    private ConfigurationSection config;
    private VulcanLottery plugin;
    private LotteryData data;

    public Lottery(String lotteryname, VulcanLottery plugin, LotteryData data) {
        this.name = lotteryname;
        this.plugin = plugin;
        this.config = plugin.getConfig().getConfigurationSection("lotteries." + this.name);
        this.data = data;
    }

    @Override
    public Long purchase(Player player, Integer quantity) throws LotteryPurchaseException {
        Long price = config.getLong("ticket-cost");
        Long cost = price * quantity;
        Integer maxPlayerTickets = config.getInt("player-ticket-limit");

        Long pot = this.data.getData().getLong("current.pot");
        if(pot == null) {
            pot = 0L;
        }

        Integer purchasedTickets = getTicketsPurchased(player);

        if(purchasedTickets + quantity <= maxPlayerTickets) {
            //withdraw from player
            if(plugin.withdraw(player, cost)) {
                pot = pot + cost;
                purchasedTickets = purchasedTickets + quantity;
                this.data.getData().set("current.pot", pot);
                this.data.getData().set("current.purchases." + player.getUniqueId(), purchasedTickets);
                this.data.saveData();
                return cost;
            } else {
                throw new LotteryPurchaseException("Ticket purchased failed! Do you have enough money?");
            }
        } else {
            //cannot purchase this many tickets. throw exception?
            throw new LotteryPurchaseException("You already have " + purchasedTickets +"/" + maxPlayerTickets + " tickets!");
        }
    }

    @Override
    public Integer getTicketsPurchased(Player player) {
        return getTicketsPurchased(player.getUniqueId());
    }
    public Integer getTicketsPurchased(String uuid) {
        return getTicketsPurchased(UUID.fromString(uuid));
    }
    public Integer getTicketsPurchased(UUID uuid) {
        Integer purchasedTickets = this.data.getData().getInt("current.purchases." + uuid);
        if(purchasedTickets == null) {
            purchasedTickets = 0;
        }
        return purchasedTickets;
    }
    @Override
    public Integer getTotalTickets() {
        return this.getTickets().size();
    }

    @Override
    public Long getPotAmount() {
        return this.data.getData().getLong("current.pot", 0L);
    }

    @Override
    public ArrayList<OfflinePlayer> getWinners() {
        return null;
    }

    @Override
    public boolean hasWon(OfflinePlayer player) {
        ConfigurationSection winner = this.data.getData().getConfigurationSection("winners." + player.getUniqueId());
        if(winner == null) {
            return false;
        }
        if(winner.getInt("won", -1) > 0 ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasUnclaimed(OfflinePlayer player) {
        ConfigurationSection winner = this.data.getData().getConfigurationSection("winners." + player.getUniqueId());
        if(winner == null) {
            return false;
        }
        for (String win : winner.getConfigurationSection("wins").getKeys(false)) {
            if(!winner.getBoolean("wins." + win + ".claimed")) {
                return true;
            }
        }
        return false;
    }
    @Override
    public Long claim(Player player) {
        Long claim = 0L;
        if(hasUnclaimed(player)) {
            ConfigurationSection winner = this.data.getData().getConfigurationSection("winners." + player.getUniqueId());
            for (String win : winner.getConfigurationSection("wins").getKeys(false)) {
                if(!winner.getBoolean("wins." + win + ".claimed")) {
                    Long amount = winner.getLong("wins." + win + ".amount");
                    VulcanLottery.plugin.deposit(player, amount);
                    claim = claim + amount;
                    winner.set("wins." + win + ".claimed", true);
                }
            }
            this.data.saveData();
        }
        return claim;
    }
    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void claim() {

    }

    private List<String> getTickets() {
        List<String> tickets = new ArrayList<String>();
        if(this.data.getData().getConfigurationSection("current.purchases") != null) {

            Set<String> players = this.data.getData().getConfigurationSection("current.purchases").getKeys(false);
            for (String uuid : players) {
                Integer ticketsPurchased = getTicketsPurchased(uuid);
                for (int tick = 0; tick < ticketsPurchased; tick++) {
                    tickets.add(uuid);
                }
            }
        }
        return tickets;
    }

    //returns UUID as string
    public static String pickRandomPlayer(List<String> tickets) {
        SecureRandom rand = new SecureRandom();
        Collections.shuffle(tickets, rand);
        return tickets.get(rand.nextInt(tickets.size()));
    }

    //Returns uuid as string.
    @Override
    public boolean draw() {
        if(this.data.getData().getConfigurationSection("current.purchases") == null) {
            return false;
        }

        Long pot = this.data.getData().getLong("current.pot");
        String uuid = pickRandomPlayer(getTickets());
        ConfigurationSection winner = this.data.getData().getConfigurationSection("winners." + uuid);
        Integer win = 0;
        if(winner != null) {
            if(winner.getInt("won", -1) != -1) {
                win = winner.getInt("won");
            }
        }
        win = win + 1;

        this.data.getData().set("winners." + uuid + ".won", win);
        this.data.getData().set("winners." + uuid + ".wins." + win  +".amount", pot);
        this.data.getData().set("winners." + uuid + ".wins." + win + ".claimed", false);
        java.util.Date date= new java.util.Date();
        this.data.getData().set("winners." + uuid + ".wins." + win + ".timestamp", new Timestamp(date.getTime()));
        this.data.saveData();

        Bukkit.getServer().broadcastMessage(ChatColor.AQUA + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() + " has won $" + pot + " in " + this.name);

        //clear current, move to archiving it later...
        this.data.getData().set("current.pot", null);
        this.data.getData().set("current.purchases", null);
        this.data.saveData();

        return true;
    }
}
