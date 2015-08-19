package org.stevenw.mc.lottery.data;

import org.stevenw.mc.lottery.Lottery;

import java.util.List;
import java.util.UUID;

public class LotteryWinner {
    private UUID player;
    private Lottery lottery;
    public LotteryWinner(Lottery lottery, UUID player) {
        this.player = player;
        this.lottery = lottery;
    }
    public UUID getWinner() {
        return player;
    }
    public Lottery getLottery() {
        return lottery;
    }
    public List<LotteryWin> getWins() {
        return lottery.getData().getWins(player);
    }
}
