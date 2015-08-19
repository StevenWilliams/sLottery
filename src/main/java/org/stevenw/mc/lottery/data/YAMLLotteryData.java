package org.stevenw.mc.lottery.data;


import org.stevenw.mc.lottery.Lottery;
import org.stevenw.mc.lottery.LotteryData;
import org.stevenw.mc.lottery.sLottery;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class YAMLLotteryData extends LotteryData {
    private FileConfiguration lotteryData = null;
    private File lotteryDataFile = null;
    private sLottery plugin;
    private Lottery lottery;

    public YAMLLotteryData(sLottery plugin, Lottery lottery) {
        this.plugin = plugin;
        this.lottery = lottery;
    }


    @Override
    public void setPot(double amount) {
        this.lotteryData.set("current.pot", amount);
    }

    @Override
    public double getPot() {
        return this.lotteryData.getDouble("current.pot", 0);
    }

    @Override
    public void setTicketsPurchased(UUID player, int amount) {
        this.lotteryData.set("current.purchases." + player.toString(), amount);
    }

    @Override
    public int getTicketsPurchased(UUID player) {
        return this.lotteryData.getInt("current.purchases." + player, 0);
    }

    @Override
    public List<UUID> getTicketsPurchased() {
        List<UUID> tickets = new ArrayList<>();
        if(this.lotteryData.getConfigurationSection("current.purchases") != null) {

            Set<String> players = this.lotteryData.getConfigurationSection("current.purchases").getKeys(false);
            for (String uuidString : players) {
                UUID uuid = UUID.fromString(uuidString);
                Integer ticketsPurchased = getTicketsPurchased(uuid);
                for (int tick = 0; tick < ticketsPurchased; tick++) {
                    tickets.add(uuid);
                }
            }
        }
        return tickets;
    }

    @Override
    public int getTotalPlayers() {
        if(lotteryData.getConfigurationSection("current.purchases") != null) {
            return lotteryData.getConfigurationSection("current.purchases").getKeys(false).size();
        }
        return 0;
    }

    @Override
    public List<LotteryWinner> getWinners() { //TODO: return winners ordered recent-leastrecent;
        List<LotteryWinner> winners = new ArrayList<>();
        Set<String> winnersString = this.lotteryData.getConfigurationSection("winners").getKeys(false);
        for(String winnerString : winnersString) {
            LotteryWinner winner = new LotteryWinner(lottery, UUID.fromString(winnerString));
            winners.add(winner);
        }
        return winners;
    }

    @Override
    public List<LotteryWin> getWins(UUID player) {
        List<LotteryWin> wins = new ArrayList<>();
        List<Integer> winIDs = this.lotteryData.getIntegerList("winners." + player + ".wins");
        for(int winID : winIDs) {
            wins.add(getWin(winID));
        }
        return wins;
    }

    @Override
    public LotteryWin addWin(UUID player, double amount) {
        int winID = this.lotteryData.getConfigurationSection("wins").getKeys(false).size();
        this.lotteryData.set("wins." + winID + ".winner", player.toString());
        this.lotteryData.set("wins." + winID + ".amount", amount);
        this.lotteryData.set("wins." + winID + ".timestamp", new Timestamp(System.currentTimeMillis()));
        List<Integer> wins = this.lotteryData.getIntegerList("winners." + player + ".wins");
        wins.add(winID);
        this.lotteryData.set("winners." + player + ".wins", wins);
        this.saveData();
        return getWin(winID);
    }

    @Override
    public List<LotteryWin> getUnclaimed(UUID player) {
        List<LotteryWin> unclaimed = new ArrayList<>();
        if(this.getWins(player) != null) {
            for(LotteryWin win : this.getWins(player)) {
                if(!this.hasClaimed(win)) {
                    unclaimed.add(win);
                }
            }
        }
        return unclaimed;
    }
    @Override
    public LotteryWin getWin(int id) {
        long amount = this.lotteryData.getLong("wins." + id + ".amount");
        UUID player = UUID.fromString(this.lotteryData.getString("wins." + id + ".winner"));
        Timestamp timestamp = (Timestamp) this.lotteryData.get("wins." + id + ".timestamp");
        return new LotteryWin(id, plugin.getLotteryManager().getLottery(this.lottery.getName()), amount, player, timestamp);
    }

    @Override
    public boolean hasClaimed(int win) {
        return this.lotteryData.getBoolean("wins." + win + ".claimed", false);
    }

    @Override
    public boolean hasClaimed(LotteryWin win) {
        return hasClaimed(win.getId());
    }

    @Override
    public void setClaimed(int win) {
        this.lotteryData.set("wins." + win + ".claimed", true);
        this.saveData();
    }

    @Override
    public void setLastDraw(long timeMillis) {
        this.lotteryData.set("lastDraw", timeMillis);
        this.saveData();
    }

    @Override
    public long getLastDraw() {
        return this.lotteryData.getLong("lastDraw", 0);
    }

    @Override
    public void resetCurrent() {
        this.lotteryData.set("current.purchases", null);
        this.lotteryData.set("current.pot", null);
        this.lotteryData.createSection("current.purchases");
        this.lotteryData.createSection("current.pot");
        this.saveData();
    }

    @Override
    public void reloadData(Lottery lottery) {
        this.lottery = lottery;
        if (lotteryDataFile == null) {
            lotteryDataFile = new File(plugin.getDataFolder() + "/data/lotteries", lottery.getName() + ".yml");
            if(!lotteryDataFile.exists()) {
                lotteryDataFile.getParentFile().mkdirs();
                try {
                    lotteryDataFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        lotteryData = YamlConfiguration.loadConfiguration(lotteryDataFile);
       // lotteryData.createSection("wins");
        //lotteryData.createSection("winners");
    }


    private FileConfiguration getData() {
        if (lotteryData == null) {
            reloadData(lottery);
        }
        return lotteryData;
    }

    public void saveData() {
        if (lotteryData == null || lotteryDataFile == null) {
            return;
        }
        try {
            getData().save(lotteryDataFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + lotteryDataFile, ex);
        }
    }
}
