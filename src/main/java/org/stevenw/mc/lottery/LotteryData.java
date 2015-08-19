package org.stevenw.mc.lottery;

import org.stevenw.mc.lottery.data.LotteryWin;
import org.stevenw.mc.lottery.data.LotteryWinner;

import java.util.List;
import java.util.UUID;

public abstract class LotteryData {
    public abstract void setPot(double amount);
    public abstract double getPot();
    public double addToPot(double amount) {
        double newPot = this.getPot() + amount;
        this.setPot(newPot);
        return newPot;
    }
    public abstract void setTicketsPurchased(UUID player, int amount);
    public abstract int getTicketsPurchased(UUID player);
    public int addToTicketsPurchased(UUID player, int amount) {
        int newAmount = getTicketsPurchased(player) + amount;
        this.setTicketsPurchased(player, newAmount);
        return newAmount;
    }
    public abstract List<UUID> getTicketsPurchased();

    public int getTotalTicketsPurchased() {
        return this.getTicketsPurchased().size();
    }

    public abstract int getTotalPlayers();
    public abstract List<LotteryWinner> getWinners();
    public abstract List<LotteryWin> getWins(UUID player);

    public abstract LotteryWin addWin(UUID player, double amount);

    public abstract List<LotteryWin> getUnclaimed(UUID player);

    public abstract LotteryWin getWin(int id);

    public abstract boolean hasClaimed(int win);

    public abstract boolean hasClaimed(LotteryWin win);

    public abstract void setClaimed(int win);

    public abstract void setLastDraw(long timeMillis);
    public abstract long getLastDraw();

    public abstract void resetCurrent();

    public abstract void reloadData(Lottery lottery);

}
