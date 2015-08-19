package org.stevenw.mc.lottery.data;

import org.stevenw.mc.lottery.Lottery;

import java.sql.Timestamp;
import java.util.UUID;

public class LotteryWin {
    private int id;
    private long amount;
    private UUID player;
    private Timestamp timestamp;
    private Lottery lottery;

    public LotteryWin(int id, Lottery lottery, long amount, UUID player, Timestamp timestamp)
    {
        this.id = id;
        this.amount = amount;
        this.player = player;
        this.timestamp = timestamp;
        this.lottery = lottery;
    }
    public int getId() {
        return id;
    }
    public long getAmount() {
        return amount;
    }
    public UUID getPlayer() {
        return player;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public Lottery getLottery() { return lottery;}
}
